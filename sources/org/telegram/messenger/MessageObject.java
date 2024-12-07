package org.telegram.messenger;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import androidx.collection.LongSparseArray;
import androidx.core.graphics.ColorUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.CodeHighlighting;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.ringtone.RingtoneDataStore;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Business.QuickRepliesController;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.Forum.ForumBubbleDrawable;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.QuoteSpan;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.Reactions.ReactionsUtils;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.TranscribeButton;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanBrowser;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanNoUnderlineBold;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.PeerColorActivity;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.web.BotWebViewContainer;

/* loaded from: classes3.dex */
public class MessageObject {
    public static final int ENTITIES_ALL = 0;
    public static final int ENTITIES_ONLY_HASHTAGS = 1;
    private static final int LINES_PER_BLOCK = 10;
    private static final int LINES_PER_BLOCK_WITH_EMOJI = 5;
    public static final int MESSAGE_SEND_STATE_EDITING = 3;
    public static final int MESSAGE_SEND_STATE_SENDING = 1;
    public static final int MESSAGE_SEND_STATE_SEND_ERROR = 2;
    public static final int MESSAGE_SEND_STATE_SENT = 0;
    public static final int POSITION_FLAG_BOTTOM = 8;
    public static final int POSITION_FLAG_LEFT = 1;
    public static final int POSITION_FLAG_RIGHT = 2;
    public static final int POSITION_FLAG_TOP = 4;
    public static final int TYPE_ACTION_PHOTO = 11;
    public static final int TYPE_ACTION_WALLPAPER = 22;
    public static final int TYPE_ANIMATED_STICKER = 15;
    public static final int TYPE_CONTACT = 12;
    public static final int TYPE_DATE = 10;
    public static final int TYPE_EMOJIS = 19;
    public static final int TYPE_EXTENDED_MEDIA_PREVIEW = 20;
    public static final int TYPE_FILE = 9;
    public static final int TYPE_GEO = 4;
    public static final int TYPE_GIF = 8;
    public static final int TYPE_GIFT_PREMIUM = 18;
    public static final int TYPE_GIFT_PREMIUM_CHANNEL = 25;
    public static final int TYPE_GIFT_STARS = 30;
    public static final int TYPE_GIVEAWAY = 26;
    public static final int TYPE_GIVEAWAY_RESULTS = 28;
    public static final int TYPE_JOINED_CHANNEL = 27;
    public static final int TYPE_LOADING = 6;
    public static final int TYPE_MUSIC = 14;
    public static final int TYPE_PAID_MEDIA = 29;
    public static final int TYPE_PHONE_CALL = 16;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_POLL = 17;
    public static final int TYPE_ROUND_VIDEO = 5;
    public static final int TYPE_STICKER = 13;
    public static final int TYPE_STORY = 23;
    public static final int TYPE_STORY_MENTION = 24;
    public static final int TYPE_SUGGEST_PHOTO = 21;
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_VOICE = 2;
    private static CharSequence channelSpan;
    static final String[] excludeWords = {" vs. ", " vs ", " versus ", " ft. ", " ft ", " featuring ", " feat. ", " feat ", " presents ", " pres. ", " pres ", " and ", " & ", " . "};
    private static CharSequence groupSpan;
    public static Pattern instagramUrlPattern;
    private static Pattern loginCodePattern;
    public static Pattern urlPattern;
    private static CharSequence[] userSpan;
    public static Pattern videoTimeUrlPattern;
    public long actionDeleteGroupEventId;
    public boolean animateComments;
    public int animatedEmojiCount;
    public boolean attachPathExists;
    public double attributeDuration;
    public int audioPlayerDuration;
    public float audioProgress;
    public int audioProgressMs;
    public int audioProgressSec;
    public StringBuilder botButtonsLayout;
    public float bufferedProgress;
    public boolean business;
    public Boolean cachedIsSupergroup;
    public boolean cancelEditing;
    public CharSequence caption;
    private boolean captionTranslated;
    private boolean channelJoined;
    public boolean channelJoinedExpanded;
    public ArrayList<TLRPC.PollAnswer> checkedVotes;
    public int contentType;
    public int currentAccount;
    public TLRPC.TL_channelAdminLogEvent currentEvent;
    public Drawable customAvatarDrawable;
    public String customName;
    public String customReplyName;
    public String dateKey;
    public int dateKeyInt;
    public boolean deleted;
    public boolean deletedByThanos;
    public boolean drawServiceWithDefaultTypeface;
    public CharSequence editingMessage;
    public ArrayList<TLRPC.MessageEntity> editingMessageEntities;
    public boolean editingMessageSearchWebPage;
    public TLRPC.Document emojiAnimatedSticker;
    public String emojiAnimatedStickerColor;
    public Long emojiAnimatedStickerId;
    private boolean emojiAnimatedStickerLoading;
    public TLRPC.VideoSize emojiMarkup;
    public int emojiOnlyCount;
    public long eventId;
    public HashSet<Integer> expandedQuotes;
    public long extendedMediaLastCheckTime;
    public boolean factCheckExpanded;
    private CharSequence factCheckText;
    public boolean flickerLoading;
    public boolean forceAvatar;
    public boolean forceExpired;
    public boolean forcePlayEffect;
    public float forceSeekTo;
    public boolean forceUpdate;
    private float generatedWithDensity;
    private int generatedWithMinSize;
    public float gifState;
    public boolean hadAnimationNotReadyLoading;
    public boolean hasCode;
    public boolean hasCodeAtBottom;
    public boolean hasCodeAtTop;
    public boolean hasQuote;
    public boolean hasQuoteAtBottom;
    public boolean hasRtl;
    public boolean hasSingleCode;
    public boolean hasSingleQuote;
    private boolean hasUnwrappedEmoji;
    public boolean hasWideCode;
    public boolean hideSendersName;
    public VideoPlayer.VideoUri highestQuality;
    public ArrayList<String> highlightedWords;
    public boolean isDateObject;
    public boolean isDownloadingFile;
    public boolean isMediaSpoilersRevealed;
    public boolean isMediaSpoilersRevealedInSharedMedia;
    public Boolean isOutOwnerCached;
    public boolean isPrimaryGroupMessage;
    public boolean isReactionPush;
    public boolean isRepostPreview;
    public boolean isRepostVideoPreview;
    public boolean isRestrictedMessage;
    private int isRoundVideoCached;
    public boolean isSaved;
    public boolean isSavedFiltered;
    public Boolean isSensitiveCached;
    public boolean isSpoilersRevealed;
    public boolean isStoryMentionPush;
    public boolean isStoryPush;
    public boolean isStoryPushHidden;
    public boolean isStoryReactionPush;
    public boolean isTopicMainMessage;
    public boolean isVideoConversionObject;
    public Object lastGeoWebFileLoaded;
    public Object lastGeoWebFileSet;
    public int lastLineWidth;
    private boolean layoutCreated;
    public CharSequence linkDescription;
    public long loadedFileSize;
    public boolean loadingCancelled;
    public boolean localChannel;
    public boolean localEdit;
    public long localGroupId;
    public String localName;
    public long localSentGroupId;
    public boolean localSupergroup;
    public int localType;
    public String localUserName;
    public boolean mediaExists;
    public ImageLocation mediaSmallThumb;
    public ImageLocation mediaThumb;
    public TLRPC.Message messageOwner;
    public CharSequence messageText;
    public CharSequence messageTextForReply;
    public CharSequence messageTextShort;
    public CharSequence messageTrimmedToHighlight;
    public boolean messageTrimmedToHighlightCut;
    public String monthKey;
    public boolean notime;
    public int overrideLinkColor;
    public long overrideLinkEmoji;
    public StoriesController.StoriesList parentStoriesList;
    public int parentWidth;
    public SvgHelper.SvgDrawable pathThumb;
    public ArrayList<TLRPC.PhotoSize> photoThumbs;
    public ArrayList<TLRPC.PhotoSize> photoThumbs2;
    public TLObject photoThumbsObject;
    public TLObject photoThumbsObject2;
    public boolean playedGiftAnimation;
    public long pollLastCheckTime;
    public boolean pollVisibleOnScreen;
    public boolean preview;
    public boolean previewForward;
    public String previousAttachPath;
    public TLRPC.MessageMedia previousMedia;
    public String previousMessage;
    public ArrayList<TLRPC.MessageEntity> previousMessageEntities;
    public boolean putInDownloadsStore;
    public TLRPC.Document qualityToSave;
    public String quick_reply_shortcut;
    private byte[] randomWaveform;
    public boolean reactionsChanged;
    public long reactionsLastCheckTime;
    public int realDate;
    public MessageObject replyMessageObject;
    public boolean replyTextEllipsized;
    public boolean replyTextRevealed;
    public TLRPC.TL_forumTopic replyToForumTopic;
    public boolean resendAsIs;
    public boolean revealingMediaSpoilers;
    public boolean scheduled;
    public boolean scheduledSent;
    public int searchType;
    private CharSequence secretOnceSpan;
    private CharSequence secretPlaySpan;
    public SendAnimationData sendAnimationData;
    public TLRPC.Peer sendAsPeer;
    public boolean sendPreview;
    public MediaController.PhotoEntry sendPreviewEntry;
    public boolean settingAvatar;
    public boolean shouldRemoveVideoEditedInfo;
    private boolean spoiledLoginCode;
    public String sponsoredAdditionalInfo;
    public String sponsoredButtonText;
    public boolean sponsoredCanReport;
    public TLRPC.TL_peerColor sponsoredColor;
    public byte[] sponsoredId;
    public String sponsoredInfo;
    public TLRPC.MessageMedia sponsoredMedia;
    public TLRPC.Photo sponsoredPhoto;
    public boolean sponsoredRecommended;
    public String sponsoredTitle;
    public String sponsoredUrl;
    public int stableId;
    public TL_stories.StoryItem storyItem;
    private TLRPC.WebPage storyMentionWebpage;
    public BitmapDrawable strippedThumb;
    public ArrayList<TextLayoutBlock> textLayoutBlocks;
    public int textWidth;
    public float textXOffset;
    public VideoPlayer.VideoUri thumbQuality;
    public Drawable[] topicIconDrawable;
    public int totalAnimatedEmojiCount;
    public boolean translated;
    public int type;
    public StoriesController.UploadingStory uploadingStory;
    public boolean useCustomPhoto;
    public CharSequence vCardData;
    public VideoEditedInfo videoEditedInfo;
    public ArrayList<VideoPlayer.Quality> videoQualities;
    private Boolean videoQualitiesCached;
    public boolean viewsReloaded;
    public int wantedBotKeyboardWidth;
    public boolean wasJustSent;
    public boolean wasUnread;
    public ArrayList<TLRPC.MessageEntity> webPageDescriptionEntities;
    public CharSequence youtubeDescription;

    public static class GroupedMessagePosition {
        public float aspectRatio;
        public boolean edge;
        public int flags;
        public boolean last;
        public float left;
        public int leftSpanOffset;
        public byte maxX;
        public byte maxY;
        public byte minX;
        public byte minY;
        public float ph;
        public int photoHeight;
        public int photoWidth;
        public int pw;
        public float[] siblingHeights;
        public int spanSize;
        public float top;

        public void set(int i, int i2, int i3, int i4, int i5, float f, int i6) {
            this.minX = (byte) i;
            this.maxX = (byte) i2;
            this.minY = (byte) i3;
            this.maxY = (byte) i4;
            this.pw = i5;
            this.spanSize = i5;
            this.ph = f;
            this.flags = (byte) i6;
        }
    }

    public static class GroupedMessages {
        public boolean captionAbove;
        public MessageObject captionMessage;
        public long groupId;
        public boolean hasCaption;
        public boolean hasSibling;
        public boolean isDocuments;
        public boolean reversed;
        public ArrayList<MessageObject> messages = new ArrayList<>();
        public ArrayList<GroupedMessagePosition> posArray = new ArrayList<>();
        public HashMap<MessageObject, GroupedMessagePosition> positions = new HashMap<>();
        public LongSparseArray positionsArray = new LongSparseArray();
        private int maxSizeWidth = 800;
        public final TransitionParams transitionParams = new TransitionParams();

        private static class MessageGroupedLayoutAttempt {
            public float[] heights;
            public int[] lineCounts;

            public MessageGroupedLayoutAttempt(int i, int i2, float f, float f2) {
                this.lineCounts = new int[]{i, i2};
                this.heights = new float[]{f, f2};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, float f, float f2, float f3) {
                this.lineCounts = new int[]{i, i2, i3};
                this.heights = new float[]{f, f2, f3};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, int i4, float f, float f2, float f3, float f4) {
                this.lineCounts = new int[]{i, i2, i3, i4};
                this.heights = new float[]{f, f2, f3, f4};
            }
        }

        public static class TransitionParams {
            public boolean backgroundChangeBounds;
            public int bottom;
            public float captionEnterProgress = 1.0f;
            public ChatMessageCell cell;
            public boolean drawBackgroundForDeletedItems;
            public boolean drawCaptionLayout;
            public boolean isNewGroup;
            public int left;
            public float offsetBottom;
            public float offsetLeft;
            public float offsetRight;
            public float offsetTop;
            public boolean pinnedBotton;
            public boolean pinnedTop;
            public int right;
            public int top;

            public void reset() {
                this.captionEnterProgress = 1.0f;
                this.offsetBottom = 0.0f;
                this.offsetTop = 0.0f;
                this.offsetRight = 0.0f;
                this.offsetLeft = 0.0f;
                this.backgroundChangeBounds = false;
            }
        }

        private float multiHeight(float[] fArr, int i, int i2) {
            float f = 0.0f;
            while (i < i2) {
                f += fArr[i];
                i++;
            }
            return this.maxSizeWidth / f;
        }

        /* JADX WARN: Code restructure failed: missing block: B:157:0x0890, code lost:
        
            if ((r1.flags & 1) != 0) goto L309;
         */
        /* JADX WARN: Code restructure failed: missing block: B:303:0x07a8, code lost:
        
            if (r15[2] > r15[3]) goto L253;
         */
        /* JADX WARN: Code restructure failed: missing block: B:87:0x00ac, code lost:
        
            if ((org.telegram.messenger.MessageObject.getMedia(r15.messageOwner) instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) == false) goto L44;
         */
        /* JADX WARN: Removed duplicated region for block: B:119:0x0862  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0068  */
        /* JADX WARN: Removed duplicated region for block: B:19:0x006e  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00ef  */
        /* JADX WARN: Removed duplicated region for block: B:45:0x00ff  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x011c  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x0137  */
        /* JADX WARN: Removed duplicated region for block: B:59:0x0153  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x015c  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x014e  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x0106  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x00f2  */
        /* JADX WARN: Removed duplicated region for block: B:90:0x00c2  */
        /* JADX WARN: Removed duplicated region for block: B:91:0x006b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            float f;
            int i8;
            int i9;
            int i10;
            GroupedMessagePosition groupedMessagePosition;
            int i11;
            boolean z;
            int i12;
            MessageObject messageObject;
            int i13;
            TLRPC.Message message;
            float f2;
            Boolean bool;
            this.posArray.clear();
            this.positions.clear();
            this.positionsArray.clear();
            Boolean bool2 = null;
            this.captionMessage = null;
            this.maxSizeWidth = 800;
            int size = this.messages.size();
            if (size == 1) {
                this.captionMessage = this.messages.get(0);
                return;
            }
            if (size < 1) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            this.hasSibling = false;
            this.hasCaption = false;
            this.captionAbove = false;
            int i14 = this.reversed ? size - 1 : 0;
            boolean z2 = false;
            float f3 = 1.0f;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = true;
            while (true) {
                if (this.reversed) {
                    if (i14 < 0) {
                        break;
                    }
                    messageObject = this.messages.get(i14);
                    if (i14 != (!this.reversed ? size - 1 : 0)) {
                        messageObject.isOutOwnerCached = bool2;
                        z4 = messageObject.isOutOwner();
                        if (!z4) {
                            TLRPC.Message message2 = messageObject.messageOwner;
                            TLRPC.MessageFwdHeader messageFwdHeader = message2.fwd_from;
                            if (messageFwdHeader != null && messageFwdHeader.saved_from_peer != null) {
                                i13 = size;
                            } else if (message2.from_id instanceof TLRPC.TL_peerUser) {
                                TLRPC.Peer peer = message2.peer_id;
                                i13 = size;
                                if (peer.channel_id == 0) {
                                    if (peer.chat_id == 0) {
                                        if (!(MessageObject.getMedia(message2) instanceof TLRPC.TL_messageMediaGame)) {
                                        }
                                    }
                                }
                            }
                            z2 = true;
                            if (!messageObject.isMusic() || messageObject.isDocument()) {
                                this.isDocuments = true;
                            }
                        }
                        i13 = size;
                        z2 = false;
                        if (!messageObject.isMusic()) {
                        }
                        this.isDocuments = true;
                    } else {
                        i13 = size;
                    }
                    message = messageObject.messageOwner;
                    if (message != null && message.invert_media) {
                        this.captionAbove = true;
                    }
                    TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                    GroupedMessagePosition groupedMessagePosition2 = new GroupedMessagePosition();
                    groupedMessagePosition2.last = this.reversed ? i14 == i13 + (-1) : i14 == 0;
                    float f4 = closestPhotoSizeWithSize != null ? 1.0f : closestPhotoSizeWithSize.w / closestPhotoSizeWithSize.h;
                    groupedMessagePosition2.aspectRatio = f4;
                    sb.append(f4 <= 1.2f ? "w" : f4 < 0.8f ? "n" : "q");
                    f2 = groupedMessagePosition2.aspectRatio;
                    f3 += f2;
                    if (f2 > 2.0f) {
                        z3 = true;
                    }
                    this.positions.put(messageObject, groupedMessagePosition2);
                    boolean z6 = z2;
                    this.positionsArray.put(messageObject.getId(), groupedMessagePosition2);
                    this.posArray.add(groupedMessagePosition2);
                    if (messageObject.caption == null) {
                        if (z5 && this.captionMessage == null) {
                            this.captionMessage = messageObject;
                            bool = null;
                            z5 = false;
                        } else if (this.isDocuments) {
                            bool = null;
                        } else {
                            bool = null;
                            this.captionMessage = null;
                        }
                        this.hasCaption = true;
                    } else {
                        bool = null;
                    }
                    i14 = !this.reversed ? i14 - 1 : i14 + 1;
                    bool2 = bool;
                    size = i13;
                    z2 = z6;
                } else {
                    if (i14 >= size) {
                        break;
                    }
                    messageObject = this.messages.get(i14);
                    if (i14 != (!this.reversed ? size - 1 : 0)) {
                    }
                    message = messageObject.messageOwner;
                    if (message != null) {
                        this.captionAbove = true;
                    }
                    TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                    GroupedMessagePosition groupedMessagePosition22 = new GroupedMessagePosition();
                    groupedMessagePosition22.last = this.reversed ? i14 == i13 + (-1) : i14 == 0;
                    if (closestPhotoSizeWithSize2 != null) {
                    }
                    groupedMessagePosition22.aspectRatio = f4;
                    sb.append(f4 <= 1.2f ? "w" : f4 < 0.8f ? "n" : "q");
                    f2 = groupedMessagePosition22.aspectRatio;
                    f3 += f2;
                    if (f2 > 2.0f) {
                    }
                    this.positions.put(messageObject, groupedMessagePosition22);
                    boolean z62 = z2;
                    this.positionsArray.put(messageObject.getId(), groupedMessagePosition22);
                    this.posArray.add(groupedMessagePosition22);
                    if (messageObject.caption == null) {
                    }
                    if (!this.reversed) {
                    }
                    bool2 = bool;
                    size = i13;
                    z2 = z62;
                }
            }
            int i15 = size;
            if (this.isDocuments) {
                for (int i16 = 0; i16 < i15; i16++) {
                    GroupedMessagePosition groupedMessagePosition3 = this.posArray.get(i16);
                    groupedMessagePosition3.flags = 3;
                    if (i16 == 0) {
                        groupedMessagePosition3.flags = 7;
                    } else if (i16 == i15 - 1) {
                        groupedMessagePosition3.flags = 11;
                        groupedMessagePosition3.last = true;
                        groupedMessagePosition3.edge = true;
                        groupedMessagePosition3.aspectRatio = 1.0f;
                        groupedMessagePosition3.minX = (byte) 0;
                        groupedMessagePosition3.maxX = (byte) 0;
                        byte b = (byte) i16;
                        groupedMessagePosition3.minY = b;
                        groupedMessagePosition3.maxY = b;
                        groupedMessagePosition3.spanSize = 1000;
                        groupedMessagePosition3.pw = this.maxSizeWidth;
                        groupedMessagePosition3.ph = 100.0f;
                    }
                    groupedMessagePosition3.last = false;
                    groupedMessagePosition3.edge = true;
                    groupedMessagePosition3.aspectRatio = 1.0f;
                    groupedMessagePosition3.minX = (byte) 0;
                    groupedMessagePosition3.maxX = (byte) 0;
                    byte b2 = (byte) i16;
                    groupedMessagePosition3.minY = b2;
                    groupedMessagePosition3.maxY = b2;
                    groupedMessagePosition3.spanSize = 1000;
                    groupedMessagePosition3.pw = this.maxSizeWidth;
                    groupedMessagePosition3.ph = 100.0f;
                }
                return;
            }
            if (z2) {
                this.maxSizeWidth -= 50;
                i = NotificationCenter.didSetNewWallpapper;
            } else {
                i = NotificationCenter.storyQualityUpdate;
            }
            int dp = AndroidUtilities.dp(120.0f);
            float dp2 = AndroidUtilities.dp(120.0f);
            Point point = AndroidUtilities.displaySize;
            int min = (int) (dp2 / (Math.min(point.x, point.y) / this.maxSizeWidth));
            float dp3 = AndroidUtilities.dp(40.0f);
            Point point2 = AndroidUtilities.displaySize;
            float min2 = Math.min(point2.x, point2.y);
            float f5 = this.maxSizeWidth;
            int i17 = (int) (dp3 / (min2 / f5));
            float f6 = f5 / 814.0f;
            float f7 = f3 / i15;
            float dp4 = AndroidUtilities.dp(100.0f) / 814.0f;
            if (i15 != 1) {
                if (z3 || !(i15 == 2 || i15 == 3 || i15 == 4)) {
                    int size2 = this.posArray.size();
                    float[] fArr = new float[size2];
                    for (int i18 = 0; i18 < i15; i18++) {
                        if (f7 > 1.1f) {
                            fArr[i18] = Math.max(1.0f, this.posArray.get(i18).aspectRatio);
                        } else {
                            fArr[i18] = Math.min(1.0f, this.posArray.get(i18).aspectRatio);
                        }
                        fArr[i18] = Math.max(0.66667f, Math.min(1.7f, fArr[i18]));
                    }
                    ArrayList arrayList = new ArrayList();
                    for (int i19 = 1; i19 < size2; i19++) {
                        int i20 = size2 - i19;
                        if (i19 <= 3 && i20 <= 3) {
                            arrayList.add(new MessageGroupedLayoutAttempt(i19, i20, multiHeight(fArr, 0, i19), multiHeight(fArr, i19, size2)));
                        }
                    }
                    for (int i21 = 1; i21 < size2 - 1; i21++) {
                        int i22 = 1;
                        while (true) {
                            int i23 = size2 - i21;
                            if (i22 < i23) {
                                int i24 = i23 - i22;
                                if (i21 <= 3) {
                                    if (i22 <= (f7 < 0.85f ? 4 : 3) && i24 <= 3) {
                                        int i25 = i21 + i22;
                                        arrayList.add(new MessageGroupedLayoutAttempt(i21, i22, i24, multiHeight(fArr, 0, i21), multiHeight(fArr, i21, i25), multiHeight(fArr, i25, size2)));
                                    }
                                }
                                i22++;
                            }
                        }
                    }
                    for (int i26 = 1; i26 < size2 - 2; i26++) {
                        int i27 = 1;
                        while (true) {
                            int i28 = size2 - i26;
                            if (i27 < i28) {
                                int i29 = 1;
                                while (true) {
                                    int i30 = i28 - i27;
                                    if (i29 < i30) {
                                        int i31 = i30 - i29;
                                        if (i26 > 3 || i27 > 3 || i29 > 3 || i31 > 3) {
                                            i4 = i28;
                                        } else {
                                            int i32 = i26 + i27;
                                            i4 = i28;
                                            int i33 = i32 + i29;
                                            arrayList.add(new MessageGroupedLayoutAttempt(i26, i27, i29, i31, multiHeight(fArr, 0, i26), multiHeight(fArr, i26, i32), multiHeight(fArr, i32, i33), multiHeight(fArr, i33, size2)));
                                        }
                                        i29++;
                                        i28 = i4;
                                    }
                                }
                                i27++;
                            }
                        }
                    }
                    float f8 = (this.maxSizeWidth / 3) * 4;
                    int i34 = 0;
                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                    float f9 = 0.0f;
                    while (i34 < arrayList.size()) {
                        MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = (MessageGroupedLayoutAttempt) arrayList.get(i34);
                        int i35 = 0;
                        float f10 = Float.MAX_VALUE;
                        float f11 = 0.0f;
                        while (true) {
                            float[] fArr2 = messageGroupedLayoutAttempt2.heights;
                            if (i35 >= fArr2.length) {
                                break;
                            }
                            float f12 = fArr2[i35];
                            f11 += f12;
                            if (f12 < f10) {
                                f10 = f12;
                            }
                            i35++;
                        }
                        float abs = Math.abs(f11 - f8);
                        int[] iArr = messageGroupedLayoutAttempt2.lineCounts;
                        float f13 = f8;
                        ArrayList arrayList2 = arrayList;
                        if (iArr.length > 1) {
                            int i36 = iArr[0];
                            int i37 = iArr[1];
                            if (i36 <= i37 && (iArr.length <= 2 || i37 <= iArr[2])) {
                                if (iArr.length > 3) {
                                }
                            }
                            abs *= 1.2f;
                        }
                        if (f10 < min) {
                            abs *= 1.5f;
                        }
                        if (messageGroupedLayoutAttempt == null || abs < f9) {
                            f9 = abs;
                            messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                        }
                        i34++;
                        f8 = f13;
                        arrayList = arrayList2;
                    }
                    if (messageGroupedLayoutAttempt == null) {
                        return;
                    }
                    int i38 = 0;
                    int i39 = 0;
                    int i40 = 0;
                    while (true) {
                        int[] iArr2 = messageGroupedLayoutAttempt.lineCounts;
                        if (i39 >= iArr2.length) {
                            break;
                        }
                        int i41 = iArr2[i39];
                        float f14 = messageGroupedLayoutAttempt.heights[i39];
                        int i42 = this.maxSizeWidth;
                        int i43 = i41 - 1;
                        int max = Math.max(i40, i43);
                        int i44 = i42;
                        int i45 = 0;
                        GroupedMessagePosition groupedMessagePosition4 = null;
                        while (i45 < i41) {
                            float[] fArr3 = fArr;
                            int i46 = (int) (fArr[i38] * f14);
                            i44 -= i46;
                            int i47 = max;
                            GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(i38);
                            int i48 = i41;
                            int i49 = i39 == 0 ? 4 : 0;
                            if (i39 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                                i49 |= 8;
                            }
                            if (i45 == 0) {
                                i49 |= 1;
                                if (z4) {
                                    groupedMessagePosition4 = groupedMessagePosition5;
                                }
                            }
                            if (i45 == i43) {
                                i49 |= 2;
                                if (!z4) {
                                    i3 = i49;
                                    groupedMessagePosition4 = groupedMessagePosition5;
                                    groupedMessagePosition5.set(i45, i45, i39, i39, i46, Math.max(dp4, f14 / 814.0f), i3);
                                    i38++;
                                    i45++;
                                    max = i47;
                                    fArr = fArr3;
                                    i41 = i48;
                                }
                            }
                            i3 = i49;
                            groupedMessagePosition5.set(i45, i45, i39, i39, i46, Math.max(dp4, f14 / 814.0f), i3);
                            i38++;
                            i45++;
                            max = i47;
                            fArr = fArr3;
                            i41 = i48;
                        }
                        groupedMessagePosition4.pw += i44;
                        groupedMessagePosition4.spanSize += i44;
                        i39++;
                        i40 = max;
                        fArr = fArr;
                    }
                    i2 = i40;
                } else {
                    if (i15 == 2) {
                        GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(0);
                        GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(1);
                        String sb2 = sb.toString();
                        if (sb2.equals("ww")) {
                            double d = f7;
                            double d2 = f6;
                            Double.isNaN(d2);
                            if (d > d2 * 1.4d) {
                                float f15 = groupedMessagePosition6.aspectRatio;
                                float f16 = groupedMessagePosition7.aspectRatio;
                                if (f15 - f16 < 0.2d) {
                                    float f17 = this.maxSizeWidth;
                                    float round = Math.round(Math.min(f17 / f15, Math.min(f17 / f16, 407.0f))) / 814.0f;
                                    groupedMessagePosition6.set(0, 0, 0, 0, this.maxSizeWidth, round, 7);
                                    groupedMessagePosition7.set(0, 0, 1, 1, this.maxSizeWidth, round, 11);
                                }
                            }
                        }
                        if (sb2.equals("ww") || sb2.equals("qq")) {
                            int i50 = this.maxSizeWidth / 2;
                            float f18 = i50;
                            i5 = 0;
                            i6 = 0;
                            i7 = i50;
                            f = Math.round(Math.min(f18 / groupedMessagePosition6.aspectRatio, Math.min(f18 / groupedMessagePosition7.aspectRatio, 814.0f))) / 814.0f;
                            groupedMessagePosition6.set(0, 0, 0, 0, i7, f, 13);
                            i8 = 14;
                            i9 = 1;
                            i10 = 1;
                            groupedMessagePosition = groupedMessagePosition7;
                        } else {
                            float f19 = this.maxSizeWidth;
                            float f20 = groupedMessagePosition6.aspectRatio;
                            int max2 = (int) Math.max(0.4f * f19, Math.round((f19 / f20) / ((1.0f / f20) + (1.0f / groupedMessagePosition7.aspectRatio))));
                            int i51 = this.maxSizeWidth - max2;
                            if (i51 < min) {
                                max2 -= min - i51;
                            } else {
                                min = i51;
                            }
                            i5 = 0;
                            i6 = 0;
                            f = Math.min(814.0f, Math.round(Math.min(min / groupedMessagePosition6.aspectRatio, max2 / groupedMessagePosition7.aspectRatio))) / 814.0f;
                            groupedMessagePosition6.set(0, 0, 0, 0, min, f, 13);
                            i8 = 14;
                            i9 = 1;
                            i10 = 1;
                            groupedMessagePosition = groupedMessagePosition7;
                            i7 = max2;
                        }
                    } else {
                        if (i15 == 3) {
                            GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(0);
                            GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(1);
                            GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(2);
                            if (sb.charAt(0) == 'n') {
                                float f21 = groupedMessagePosition9.aspectRatio;
                                float min3 = Math.min(407.0f, Math.round((this.maxSizeWidth * f21) / (groupedMessagePosition10.aspectRatio + f21)));
                                int max3 = (int) Math.max(min, Math.min(this.maxSizeWidth * 0.5f, Math.round(Math.min(groupedMessagePosition10.aspectRatio * min3, groupedMessagePosition9.aspectRatio * r10))));
                                int round2 = Math.round(Math.min((groupedMessagePosition8.aspectRatio * 814.0f) + i17, this.maxSizeWidth - max3));
                                groupedMessagePosition8.set(0, 0, 0, 1, round2, 1.0f, 13);
                                float f22 = (814.0f - min3) / 814.0f;
                                groupedMessagePosition9.set(1, 1, 0, 0, max3, f22, 6);
                                float f23 = min3 / 814.0f;
                                groupedMessagePosition10.set(0, 1, 1, 1, max3, f23, 10);
                                int i52 = this.maxSizeWidth;
                                groupedMessagePosition10.spanSize = i52;
                                groupedMessagePosition8.siblingHeights = new float[]{f23, f22};
                                if (z4) {
                                    groupedMessagePosition8.spanSize = i52 - max3;
                                } else {
                                    groupedMessagePosition9.spanSize = i52 - round2;
                                    groupedMessagePosition10.leftSpanOffset = round2;
                                }
                                this.hasSibling = true;
                            } else {
                                float round3 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition8.aspectRatio, 537.24005f)) / 814.0f;
                                groupedMessagePosition8.set(0, 1, 0, 0, this.maxSizeWidth, round3, 7);
                                int i53 = this.maxSizeWidth / 2;
                                float f24 = 814.0f - round3;
                                float f25 = i53;
                                float min4 = Math.min(f24, Math.round(Math.min(f25 / groupedMessagePosition9.aspectRatio, f25 / groupedMessagePosition10.aspectRatio))) / 814.0f;
                                if (min4 >= dp4) {
                                    dp4 = min4;
                                }
                                i5 = 1;
                                i6 = 1;
                                i7 = i53;
                                f = dp4;
                                groupedMessagePosition9.set(0, 0, 1, 1, i7, f, 9);
                                i8 = 10;
                                i9 = 1;
                                i10 = 1;
                                groupedMessagePosition = groupedMessagePosition10;
                            }
                        } else {
                            GroupedMessagePosition groupedMessagePosition11 = this.posArray.get(0);
                            GroupedMessagePosition groupedMessagePosition12 = this.posArray.get(1);
                            GroupedMessagePosition groupedMessagePosition13 = this.posArray.get(2);
                            GroupedMessagePosition groupedMessagePosition14 = this.posArray.get(3);
                            if (sb.charAt(0) == 'w') {
                                float round4 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition11.aspectRatio, 537.24005f)) / 814.0f;
                                groupedMessagePosition11.set(0, 2, 0, 0, this.maxSizeWidth, round4, 7);
                                float round5 = Math.round(this.maxSizeWidth / ((groupedMessagePosition12.aspectRatio + groupedMessagePosition13.aspectRatio) + groupedMessagePosition14.aspectRatio));
                                float f26 = min;
                                int max4 = (int) Math.max(f26, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition12.aspectRatio * round5));
                                int max5 = (int) Math.max(Math.max(f26, this.maxSizeWidth * 0.33f), groupedMessagePosition14.aspectRatio * round5);
                                int i54 = (this.maxSizeWidth - max4) - max5;
                                if (i54 < AndroidUtilities.dp(58.0f)) {
                                    int dp5 = AndroidUtilities.dp(58.0f) - i54;
                                    i54 = AndroidUtilities.dp(58.0f);
                                    int i55 = dp5 / 2;
                                    max4 -= i55;
                                    max5 -= dp5 - i55;
                                }
                                int i56 = max4;
                                float min5 = Math.min(814.0f - round4, round5) / 814.0f;
                                if (min5 >= dp4) {
                                    dp4 = min5;
                                }
                                float f27 = dp4;
                                groupedMessagePosition12.set(0, 0, 1, 1, i56, f27, 9);
                                groupedMessagePosition13.set(1, 1, 1, 1, i54, f27, 8);
                                groupedMessagePosition14.set(2, 2, 1, 1, max5, f27, 10);
                                i2 = 2;
                            } else {
                                int max6 = Math.max(min, Math.round(814.0f / (((1.0f / groupedMessagePosition12.aspectRatio) + (1.0f / groupedMessagePosition13.aspectRatio)) + (1.0f / groupedMessagePosition14.aspectRatio))));
                                float f28 = dp;
                                float f29 = max6;
                                float min6 = Math.min(0.33f, Math.max(f28, f29 / groupedMessagePosition12.aspectRatio) / 814.0f);
                                float min7 = Math.min(0.33f, Math.max(f28, f29 / groupedMessagePosition13.aspectRatio) / 814.0f);
                                float f30 = (1.0f - min6) - min7;
                                int round6 = Math.round(Math.min((groupedMessagePosition11.aspectRatio * 814.0f) + i17, this.maxSizeWidth - max6));
                                groupedMessagePosition11.set(0, 0, 0, 2, round6, min6 + min7 + f30, 13);
                                groupedMessagePosition12.set(1, 1, 0, 0, max6, min6, 6);
                                groupedMessagePosition13.set(0, 1, 1, 1, max6, min7, 2);
                                groupedMessagePosition13.spanSize = this.maxSizeWidth;
                                groupedMessagePosition14.set(0, 1, 2, 2, max6, f30, 10);
                                int i57 = this.maxSizeWidth;
                                groupedMessagePosition14.spanSize = i57;
                                if (z4) {
                                    groupedMessagePosition11.spanSize = i57 - max6;
                                } else {
                                    groupedMessagePosition12.spanSize = i57 - round6;
                                    groupedMessagePosition13.leftSpanOffset = round6;
                                    groupedMessagePosition14.leftSpanOffset = round6;
                                }
                                groupedMessagePosition11.siblingHeights = new float[]{min6, min7, f30};
                                this.hasSibling = true;
                            }
                        }
                        i2 = 1;
                    }
                    groupedMessagePosition.set(i9, i10, i6, i5, i7, f, i8);
                    i2 = 1;
                }
                i11 = 0;
                while (i11 < i15) {
                    GroupedMessagePosition groupedMessagePosition15 = this.posArray.get(i11);
                    if (z4) {
                        if (groupedMessagePosition15.minX == 0) {
                            groupedMessagePosition15.spanSize += i;
                        }
                        if ((groupedMessagePosition15.flags & 2) != 0) {
                            z = true;
                            groupedMessagePosition15.edge = z;
                        }
                        MessageObject messageObject2 = this.messages.get(i11);
                        if (!z4 && messageObject2.needDrawAvatarInternal()) {
                            if (groupedMessagePosition15.edge) {
                                int i58 = groupedMessagePosition15.spanSize;
                                if (i58 != 1000) {
                                    groupedMessagePosition15.spanSize = i58 + 108;
                                }
                                groupedMessagePosition15.pw += 108;
                                i12 = 1;
                                i11 += i12;
                            } else if ((groupedMessagePosition15.flags & 2) != 0) {
                                int i59 = groupedMessagePosition15.spanSize;
                                if (i59 != 1000) {
                                    groupedMessagePosition15.spanSize = i59 - 108;
                                } else {
                                    int i60 = groupedMessagePosition15.leftSpanOffset;
                                    if (i60 != 0) {
                                        groupedMessagePosition15.leftSpanOffset = i60 + 108;
                                    }
                                }
                                i12 = 1;
                                i11 += i12;
                            }
                        }
                        i12 = 1;
                        i11 += i12;
                    } else {
                        if (groupedMessagePosition15.maxX == i2 || (groupedMessagePosition15.flags & 2) != 0) {
                            groupedMessagePosition15.spanSize += i;
                        }
                        z = true;
                    }
                }
            }
            GroupedMessagePosition groupedMessagePosition16 = this.posArray.get(0);
            float f31 = this.maxSizeWidth / groupedMessagePosition16.aspectRatio;
            groupedMessagePosition16.set(0, 0, 0, 0, this.maxSizeWidth, Math.round(Math.min(f31, Math.min(f31, 407.0f))) / 814.0f, 15);
            i2 = 0;
            i11 = 0;
            while (i11 < i15) {
            }
        }

        public boolean contains(int i) {
            if (this.messages == null) {
                return false;
            }
            for (int i2 = 0; i2 < this.messages.size(); i2++) {
                MessageObject messageObject = this.messages.get(i2);
                if (messageObject != null && messageObject.getId() == i) {
                    return true;
                }
            }
            return false;
        }

        public MessageObject findCaptionMessageObject() {
            if (!this.messages.isEmpty() && this.positions.isEmpty()) {
                calculate();
            }
            MessageObject messageObject = null;
            for (int i = 0; i < this.messages.size(); i++) {
                MessageObject messageObject2 = this.messages.get(i);
                if (!TextUtils.isEmpty(messageObject2.caption)) {
                    if (messageObject != null) {
                        return null;
                    }
                    messageObject = messageObject2;
                }
            }
            return messageObject;
        }

        public MessageObject findMessageWithFlags(int i) {
            if (!this.messages.isEmpty() && this.positions.isEmpty()) {
                calculate();
            }
            for (int i2 = 0; i2 < this.messages.size(); i2++) {
                MessageObject messageObject = this.messages.get(i2);
                GroupedMessagePosition groupedMessagePosition = this.positions.get(messageObject);
                if (groupedMessagePosition != null && (groupedMessagePosition.flags & i) == i) {
                    return messageObject;
                }
            }
            return null;
        }

        public MessageObject findPrimaryMessageObject() {
            return findMessageWithFlags(this.reversed ? 10 : 5);
        }

        public GroupedMessagePosition getPosition(MessageObject messageObject) {
            if (messageObject == null) {
                return null;
            }
            GroupedMessagePosition groupedMessagePosition = this.positions.get(messageObject);
            return groupedMessagePosition == null ? (GroupedMessagePosition) this.positionsArray.get(messageObject.getId()) : groupedMessagePosition;
        }
    }

    public static class SendAnimationData {
        public float currentScale;
        public float currentX;
        public float currentY;
        public ChatMessageCell.TransitionParams fromParams;
        public boolean fromPreview;
        public float height;
        public float progress;
        public float timeAlpha;
        public float width;
        public float x;
        public float y;
    }

    public static class TextLayoutBlock {
        public static final int FLAG_NOT_RTL = 2;
        public static final int FLAG_RTL = 1;
        public int charactersEnd;
        public int charactersOffset;
        public boolean code;
        public ButtonBounce collapsedBounce;
        public int collapsedHeight;
        public Drawable copyIcon;
        public int copyIconColor;
        public Drawable copySelector;
        public int copySelectorColor;
        public Paint copySeparator;
        public Text copyText;
        public byte directionFlags;
        public boolean first;
        public boolean hasCodeCopyButton;
        public int height;
        public int heightByOffset;
        public int index;
        public String language;
        public int languageHeight;
        public Text languageLayout;
        public boolean last;
        public float maxRight;
        public MessageObject messageObject;
        public int originalWidth;
        public int padBottom;
        public int padTop;
        public boolean quote;
        public boolean quoteCollapse;
        public StaticLayout textLayout;
        public AtomicReference<Layout> spoilersPatchedTextLayout = new AtomicReference<>();
        public List<SpoilerEffect> spoilers = new ArrayList();

        private static String capitalizeFirst(String str) {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }

        private static String capitalizeLanguage(String str) {
            if (str == null) {
                return null;
            }
            String replaceAll = str.toLowerCase().replaceAll("\\W|lang$", "");
            replaceAll.hashCode();
            switch (replaceAll) {
                case "actionscript":
                    return "ActionScript";
                case "aspnet":
                    return "ASP.NET";
                case "csharp":
                case "cs":
                    return "C#";
                case "docker":
                case "dockerfile":
                case "kotlin":
                case "pascal":
                case "arduino":
                case "c":
                case "go":
                case "lua":
                case "dart":
                case "fift":
                case "java":
                case "rust":
                case "swift":
                    return capitalizeFirst(str);
                case "python":
                case "py":
                    return "Python";
                case "typescript":
                case "ts":
                    return "TypeScript";
                case "r":
                case "tl":
                case "asm":
                case "css":
                case "csv":
                case "ini":
                case "jsx":
                case "php":
                case "tsx":
                case "xml":
                case "yml":
                case "glsl":
                case "hlsl":
                case "html":
                case "http":
                case "json":
                case "less":
                case "nasm":
                case "scss":
                case "wasm":
                case "yaml":
                case "cobol":
                case "json5":
                    return str.toUpperCase();
                case "js":
                case "javascript":
                    return "JavaScript";
                case "md":
                case "markdown":
                    return "Markdown";
                case "rb":
                case "ruby":
                    return "Ruby";
                case "cpp":
                    return "C++";
                case "tlb":
                case "tl-b":
                    return "TL-B";
                case "func":
                    return "FunC";
                case "objc":
                case "objectivec":
                    return "Objective-C";
                case "autohotkey":
                    return "AutoHotKey";
                default:
                    return str;
            }
        }

        public float collapsed(ChatMessageCell.TransitionParams transitionParams) {
            boolean collapsed;
            if (transitionParams.animateExpandedQuotes) {
                HashSet hashSet = transitionParams.animateExpandedQuotesFrom;
                collapsed = hashSet == null || !hashSet.contains(Integer.valueOf(this.index));
            } else {
                collapsed = collapsed();
            }
            return AndroidUtilities.lerp(collapsed ? 1.0f : 0.0f, collapsed() ? 1.0f : 0.0f, transitionParams.animateChangeProgress);
        }

        public boolean collapsed() {
            HashSet<Integer> hashSet;
            MessageObject messageObject = this.messageObject;
            return messageObject == null || (hashSet = messageObject.expandedQuotes) == null || !hashSet.contains(Integer.valueOf(this.index));
        }

        public void drawCopyCodeButton(Canvas canvas, RectF rectF, int i, int i2, float f) {
            if (this.hasCodeCopyButton) {
                int multAlpha = Theme.multAlpha(i, 0.1f);
                if (this.copySelectorColor != multAlpha) {
                    Drawable drawable = this.copySelector;
                    this.copySelectorColor = multAlpha;
                    Theme.setSelectorDrawableColor(drawable, multAlpha, true);
                }
                this.copySelector.setBounds(((int) rectF.left) + AndroidUtilities.dp(3.0f), (int) (rectF.bottom - AndroidUtilities.dp(38.0f)), (int) rectF.right, (int) rectF.bottom);
                int i3 = (int) (255.0f * f);
                this.copySelector.setAlpha(i3);
                if (this.copySelector.getCallback() != null) {
                    this.copySelector.draw(canvas);
                }
                this.copySeparator.setColor(ColorUtils.setAlphaComponent(i2, 38));
                canvas.drawRect(AndroidUtilities.dp(10.0f) + rectF.left, (rectF.bottom - AndroidUtilities.dp(38.0f)) - AndroidUtilities.getShadowHeight(), rectF.right - AndroidUtilities.dp(6.66f), rectF.bottom - AndroidUtilities.dp(38.0f), this.copySeparator);
                float centerX = rectF.centerX() - (Math.min(rectF.width() - AndroidUtilities.dp(12.0f), ((this.copyIcon.getIntrinsicWidth() * 0.8f) + AndroidUtilities.dp(5.0f)) + this.copyText.getCurrentWidth()) / 2.0f);
                float dp = rectF.bottom - (AndroidUtilities.dp(38.0f) / 2.0f);
                if (this.copyIconColor != i) {
                    Drawable drawable2 = this.copyIcon;
                    this.copyIconColor = i;
                    drawable2.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
                }
                this.copyIcon.setAlpha(i3);
                this.copyIcon.setBounds((int) centerX, (int) (dp - ((r10.getIntrinsicHeight() * 0.8f) / 2.0f)), (int) ((this.copyIcon.getIntrinsicWidth() * 0.8f) + centerX), (int) (((this.copyIcon.getIntrinsicHeight() * 0.8f) / 2.0f) + dp));
                this.copyIcon.draw(canvas);
                this.copyText.ellipsize(((int) (r1 - ((this.copyIcon.getIntrinsicWidth() * 0.8f) + AndroidUtilities.dp(5.0f)))) + AndroidUtilities.dp(12.0f)).draw(canvas, centerX + (this.copyIcon.getIntrinsicWidth() * 0.8f) + AndroidUtilities.dp(5.0f), dp, i, f);
            }
        }

        public int height() {
            return (this.quoteCollapse && collapsed()) ? this.collapsedHeight : this.height;
        }

        public int height(ChatMessageCell.TransitionParams transitionParams) {
            return !this.quoteCollapse ? this.height : AndroidUtilities.lerp(this.height, this.collapsedHeight, collapsed(transitionParams));
        }

        public boolean isRtl() {
            byte b = this.directionFlags;
            return (b & 1) != 0 && (b & 2) == 0;
        }

        public void layoutCode(String str, int i, boolean z) {
            boolean z2 = i >= 75 && !z;
            this.hasCodeCopyButton = z2;
            if (z2) {
                this.copyText = new Text(LocaleController.getString(R.string.CopyCode).toUpperCase(), SharedConfig.fontSize - 3, AndroidUtilities.bold());
                Drawable mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.msg_copy).mutate();
                this.copyIcon = mutate;
                mutate.setColorFilter(new PorterDuffColorFilter(this.copyIconColor, PorterDuff.Mode.SRC_IN));
                this.copySelector = Theme.createRadSelectorDrawable(this.copySelectorColor, 0, 0, Math.min(5, SharedConfig.bubbleRadius), 0);
                this.copySeparator = new Paint(1);
            }
            if (TextUtils.isEmpty(str)) {
                this.language = null;
                this.languageLayout = null;
            } else {
                this.language = str;
                Text text = new Text(capitalizeLanguage(str), (SharedConfig.fontSize - 1) - (CodeHighlighting.getTextSizeDecrement(i) / 2), AndroidUtilities.bold());
                this.languageLayout = text;
                this.languageHeight = ((int) (text.getTextSize() * 1.714f)) + AndroidUtilities.dp(4.0f);
            }
        }

        public float textYOffset(ArrayList<TextLayoutBlock> arrayList) {
            TextLayoutBlock textLayoutBlock;
            if (arrayList == null) {
                return 0.0f;
            }
            int i = 0;
            for (int i2 = 0; i2 < arrayList.size() && (textLayoutBlock = arrayList.get(i2)) != this; i2++) {
                i += textLayoutBlock.padTop + textLayoutBlock.height() + textLayoutBlock.padBottom;
            }
            return i;
        }

        public float textYOffset(ArrayList<TextLayoutBlock> arrayList, ChatMessageCell.TransitionParams transitionParams) {
            TextLayoutBlock textLayoutBlock;
            if (arrayList == null) {
                return 0.0f;
            }
            int i = 0;
            for (int i2 = 0; i2 < arrayList.size() && (textLayoutBlock = arrayList.get(i2)) != this; i2++) {
                i += textLayoutBlock.padTop + textLayoutBlock.height(transitionParams) + textLayoutBlock.padBottom;
            }
            return i;
        }
    }

    public static class TextLayoutBlocks {
        public boolean hasCode;
        public boolean hasCodeAtBottom;
        public boolean hasCodeAtTop;
        public boolean hasQuote;
        public boolean hasQuoteAtBottom;
        public boolean hasRtl;
        public boolean hasSingleCode;
        public boolean hasSingleQuote;
        public int lastLineWidth;
        public final CharSequence text;
        public final ArrayList<TextLayoutBlock> textLayoutBlocks = new ArrayList<>();
        public int textWidth;
        public float textXOffset;

        /* JADX WARN: Can't wrap try/catch for region: R(16:210|211|212|213|214|(1:216)(11:246|(1:248)|218|219|220|(1:222)|223|(2:225|(3:227|(5:231|232|(1:237)|234|235)|236))(1:243)|242|(1:241)(6:229|231|232|(0)|234|235)|236)|217|218|219|220|(0)|223|(0)(0)|242|(0)(0)|236) */
        /* JADX WARN: Can't wrap try/catch for region: R(43:117|(1:119)|120|(1:122)(1:356)|123|(1:125)(1:355)|126|(1:128)|(1:130)|(1:354)(1:135)|136|(2:138|(3:(1:336)|337|338)(1:141))(2:339|(8:341|(1:343)(1:353)|344|(1:346)(1:352)|347|(1:349)(1:351)|350|338))|142|(3:144|(1:146)(2:330|(1:332)(1:333))|147)(1:334)|148|(1:150)(1:(1:328)(1:329))|151|(3:153|(1:307)(4:159|(1:161)(1:306)|162|163)|164)(3:308|(2:310|311)(6:312|313|314|(1:321)(1:318)|319|320)|277)|165|(1:171)|172|173|174|(1:178)|179|180|181|182|(1:184)|185|(1:187)|188|(3:190|(7:192|193|194|195|196|198|199)|205)|206|(6:208|(16:210|211|212|213|214|(1:216)(11:246|(1:248)|218|219|220|(1:222)|223|(2:225|(3:227|(5:231|232|(1:237)|234|235)|236))(1:243)|242|(1:241)(6:229|231|232|(0)|234|235)|236)|217|218|219|220|(0)|223|(0)(0)|242|(0)(0)|236)|251|252|(2:(1:255)|256)(1:(1:284))|257)(3:285|(5:287|(1:289)(1:296)|290|(1:292)(1:295)|293)(1:297)|294)|258|(3:260|(1:262)(1:264)|263)|265|(1:282)(3:271|(1:273)(3:278|(1:280)|281)|274)|275|276|277|115) */
        /* JADX WARN: Code restructure failed: missing block: B:245:0x0553, code lost:
        
            r2 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:299:0x04c3, code lost:
        
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:300:0x04c4, code lost:
        
            org.telegram.messenger.FileLog.e(r0);
            r0 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:302:0x04ad, code lost:
        
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:303:0x04b1, code lost:
        
            if (r8 == 0) goto L248;
         */
        /* JADX WARN: Code restructure failed: missing block: B:304:0x04b3, code lost:
        
            r31.textXOffset = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:305:0x04b6, code lost:
        
            org.telegram.messenger.FileLog.e(r0);
            r12 = 0.0f;
         */
        /* JADX WARN: Removed duplicated region for block: B:100:0x01d8  */
        /* JADX WARN: Removed duplicated region for block: B:103:0x01f8  */
        /* JADX WARN: Removed duplicated region for block: B:106:0x01ff  */
        /* JADX WARN: Removed duplicated region for block: B:117:0x0271  */
        /* JADX WARN: Removed duplicated region for block: B:222:0x055c  */
        /* JADX WARN: Removed duplicated region for block: B:225:0x0563  */
        /* JADX WARN: Removed duplicated region for block: B:229:0x058d  */
        /* JADX WARN: Removed duplicated region for block: B:237:0x059b A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:241:0x059b A[ADDED_TO_REGION, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:243:0x0578  */
        /* JADX WARN: Removed duplicated region for block: B:372:0x0201  */
        /* JADX WARN: Removed duplicated region for block: B:373:0x01fa  */
        /* JADX WARN: Removed duplicated region for block: B:374:0x01df  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0107  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public TextLayoutBlocks(MessageObject messageObject, CharSequence charSequence, TextPaint textPaint, int i) {
            boolean z;
            int i2;
            int dp;
            StaticLayout staticLayout;
            CharSequence charSequence2;
            boolean z2;
            ArrayList arrayList;
            int i3;
            int dp2;
            TextPaint textPaint2;
            CharSequence charSequence3;
            int i4;
            ArrayList arrayList2;
            boolean z3;
            MessageObject messageObject2;
            int i5;
            boolean z4;
            float f;
            float f2;
            int i6;
            int i7;
            float dp3;
            int i8;
            TLRPC.Message message;
            MessageObject messageObject3 = messageObject;
            CharSequence charSequence4 = charSequence;
            this.text = charSequence4;
            this.textWidth = 0;
            boolean z5 = (messageObject3 == null || (message = messageObject3.messageOwner) == null || !message.noforwards) ? false : true;
            if (messageObject3 == null || z5) {
                z = z5;
            } else {
                TLRPC.Chat chat = MessagesController.getInstance(messageObject3.currentAccount).getChat(Long.valueOf(-messageObject.getDialogId()));
                z = chat != null && chat.noforwards;
            }
            boolean z6 = charSequence4 instanceof Spanned;
            this.hasCode = z6 && ((CodeHighlighting.Span[]) ((Spanned) charSequence4).getSpans(0, charSequence.length(), CodeHighlighting.Span.class)).length > 0;
            this.hasQuote = z6 && ((QuoteSpan.QuoteStyleSpan[]) ((Spanned) charSequence4).getSpans(0, charSequence.length(), QuoteSpan.QuoteStyleSpan.class)).length > 0;
            this.hasSingleQuote = false;
            this.hasSingleCode = false;
            if (z6) {
                Spanned spanned = (Spanned) charSequence4;
                QuoteSpan[] quoteSpanArr = (QuoteSpan[]) spanned.getSpans(0, spanned.length(), QuoteSpan.class);
                for (QuoteSpan quoteSpan : quoteSpanArr) {
                    quoteSpan.adaptLineHeight = false;
                }
                this.hasSingleQuote = quoteSpanArr.length == 1 && spanned.getSpanStart(quoteSpanArr[0]) == 0 && spanned.getSpanEnd(quoteSpanArr[0]) == spanned.length();
                CodeHighlighting.Span[] spanArr = (CodeHighlighting.Span[]) spanned.getSpans(0, spanned.length(), CodeHighlighting.Span.class);
                this.hasSingleCode = spanArr.length == 1 && spanned.getSpanStart(spanArr[0]) == 0 && spanned.getSpanEnd(spanArr[0]) == spanned.length();
            }
            float f3 = 32.0f;
            try {
                if (this.hasSingleQuote) {
                    dp = AndroidUtilities.dp(32.0f);
                } else {
                    if (!this.hasSingleCode) {
                        i2 = i;
                        StaticLayout makeStaticLayout = MessageObject.makeStaticLayout(charSequence, textPaint, i2, 1.0f, 0.0f, false);
                        CharSequence charSequence5 = charSequence4;
                        if (messageObject3 != null) {
                            charSequence5 = charSequence4;
                            if (messageObject3.isRepostPreview) {
                                int i9 = messageObject3.type != 0 ? messageObject.hasValidGroupId() ? 7 : 12 : 22;
                                i9 = messageObject.isWebpage() ? i9 - 8 : i9;
                                charSequence5 = charSequence4;
                                if (makeStaticLayout.getLineCount() > i9) {
                                    String string = LocaleController.getString(R.string.ReadMore);
                                    int ceil = (int) Math.ceil(textPaint.measureText("… " + string) + AndroidUtilities.dp(1.0f));
                                    float f4 = 0.0f;
                                    for (int i10 = 0; i10 < i9; i10++) {
                                        f4 = Math.max(f4, makeStaticLayout.getLineRight(i10));
                                    }
                                    int i11 = i9 - 1;
                                    int lineStart = makeStaticLayout.getLineStart(i11);
                                    int lineEnd = makeStaticLayout.getLineEnd(i11) - 1;
                                    while (lineEnd >= lineStart && makeStaticLayout.getPrimaryHorizontal(lineEnd) >= f4 - ceil) {
                                        lineEnd--;
                                    }
                                    while (lineEnd >= lineStart && !Character.isWhitespace(charSequence4.charAt(lineEnd))) {
                                        lineEnd--;
                                    }
                                    SpannableStringBuilder append = new SpannableStringBuilder(charSequence4.subSequence(0, lineEnd)).append((CharSequence) "… ").append((CharSequence) string);
                                    append.setSpan(new CharacterStyle() { // from class: org.telegram.messenger.MessageObject.TextLayoutBlocks.1
                                        @Override // android.text.style.CharacterStyle
                                        public void updateDrawState(TextPaint textPaint3) {
                                            textPaint3.setColor(Theme.chat_msgTextPaint.linkColor);
                                        }
                                    }, append.length() - string.length(), append.length(), 33);
                                    try {
                                        makeStaticLayout = MessageObject.makeStaticLayout(append, textPaint, i2, 1.0f, 0.0f, false);
                                        charSequence5 = append;
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                        return;
                                    }
                                }
                            }
                        }
                        staticLayout = makeStaticLayout;
                        charSequence2 = charSequence5;
                        int dp4 = !this.hasSingleQuote ? i2 + AndroidUtilities.dp(32.0f) : this.hasSingleCode ? i2 + AndroidUtilities.dp(15.0f) : i2;
                        int lineCount = staticLayout.getLineCount();
                        z2 = Build.VERSION.SDK_INT < 24;
                        int i12 = 10;
                        int ceil2 = !z2 ? 1 : (int) Math.ceil(lineCount / 10);
                        arrayList = new ArrayList();
                        if (!(charSequence2 instanceof Spanned) && (this.hasQuote || this.hasCode)) {
                            MessageObject.cutIntoRanges(charSequence2, arrayList);
                        } else if (!z2 || ceil2 == 1) {
                            arrayList.add(new TextRange(0, staticLayout.getText().length()));
                        } else {
                            int i13 = 0;
                            int i14 = 0;
                            while (i13 < ceil2) {
                                int min = Math.min(i12, lineCount - i14);
                                int lineStart2 = staticLayout.getLineStart(i14);
                                int i15 = min + i14;
                                int lineEnd2 = staticLayout.getLineEnd(i15 - 1);
                                if (lineEnd2 >= lineStart2) {
                                    arrayList.add(new TextRange(lineStart2, lineEnd2));
                                    i14 = i15;
                                }
                                i13++;
                                i12 = 10;
                            }
                        }
                        int size = arrayList.size();
                        this.hasCodeAtTop = false;
                        this.hasCodeAtBottom = false;
                        this.hasQuoteAtBottom = false;
                        this.hasSingleQuote = false;
                        StaticLayout staticLayout2 = staticLayout;
                        int i16 = i2;
                        i3 = 0;
                        while (i3 < arrayList.size()) {
                            TextLayoutBlock textLayoutBlock = new TextLayoutBlock();
                            TextRange textRange = (TextRange) arrayList.get(i3);
                            textLayoutBlock.code = textRange.code;
                            textLayoutBlock.quote = textRange.quote;
                            boolean z7 = textRange.collapse;
                            textLayoutBlock.quoteCollapse = z7;
                            if (z7) {
                                textLayoutBlock.messageObject = messageObject3;
                            }
                            textLayoutBlock.index = i3;
                            textLayoutBlock.first = i3 == 0;
                            boolean z8 = i3 == arrayList.size() - 1;
                            textLayoutBlock.last = z8;
                            boolean z9 = textLayoutBlock.first;
                            if (z9) {
                                this.hasCodeAtTop = textLayoutBlock.code;
                            }
                            if (z8) {
                                this.hasQuoteAtBottom = textLayoutBlock.quote;
                                this.hasCodeAtBottom = textLayoutBlock.code;
                            }
                            this.hasSingleQuote = z9 && z8 && textLayoutBlock.quote;
                            if (textLayoutBlock.quote) {
                                if (z9 && z8) {
                                    int dp5 = AndroidUtilities.dp(6.0f);
                                    textLayoutBlock.padBottom = dp5;
                                    textLayoutBlock.padTop = dp5;
                                } else {
                                    textLayoutBlock.padTop = AndroidUtilities.dp(z9 ? 8.0f : 6.0f);
                                    dp2 = AndroidUtilities.dp(7.0f);
                                    textLayoutBlock.padBottom = dp2;
                                }
                            } else if (textLayoutBlock.code) {
                                textLayoutBlock.layoutCode(textRange.language, textRange.end - textRange.start, z);
                                textLayoutBlock.padTop = AndroidUtilities.dp(4.0f) + textLayoutBlock.languageHeight + (textLayoutBlock.first ? 0 : AndroidUtilities.dp(5.0f));
                                dp2 = AndroidUtilities.dp(4.0f) + (textLayoutBlock.last ? 0 : AndroidUtilities.dp(7.0f)) + (textLayoutBlock.hasCodeCopyButton ? AndroidUtilities.dp(38.0f) : 0);
                                textLayoutBlock.padBottom = dp2;
                            }
                            boolean z10 = textLayoutBlock.code;
                            if (z10) {
                                int i17 = textRange.end - textRange.start;
                                textPaint2 = i17 > 220 ? Theme.chat_msgTextCode3Paint : i17 > 80 ? Theme.chat_msgTextCode2Paint : Theme.chat_msgTextCodePaint;
                            } else {
                                textPaint2 = textPaint;
                            }
                            int dp6 = textLayoutBlock.quote ? dp4 - AndroidUtilities.dp(f3) : z10 ? dp4 - AndroidUtilities.dp(15.0f) : dp4;
                            if (size == 1) {
                                if (textLayoutBlock.code && !textLayoutBlock.quote && (staticLayout2.getText() instanceof Spannable)) {
                                    SpannableString highlighted = !TextUtils.isEmpty(textRange.language) ? CodeHighlighting.getHighlighted(charSequence2.subSequence(textRange.start, textRange.end).toString(), textRange.language) : new SpannableString(charSequence2.subSequence(textRange.start, textRange.end));
                                    textLayoutBlock.originalWidth = dp6;
                                    staticLayout2 = MessageObject.makeStaticLayout(highlighted, textPaint2, dp6, 1.0f, 0.0f, false);
                                    i16 = dp6;
                                } else {
                                    textLayoutBlock.originalWidth = i16;
                                }
                                textLayoutBlock.textLayout = staticLayout2;
                                textLayoutBlock.charactersOffset = 0;
                                textLayoutBlock.charactersEnd = staticLayout2.getText().length();
                                textLayoutBlock.height = staticLayout2.getHeight();
                                textLayoutBlock.collapsedHeight = (int) Math.min(textPaint.getTextSize() * 1.4f * 3.0f, textLayoutBlock.height);
                            } else {
                                int i18 = textRange.start;
                                int i19 = textRange.end;
                                if (i19 < i18) {
                                    charSequence3 = charSequence2;
                                    i4 = dp4;
                                    arrayList2 = arrayList;
                                    z3 = z;
                                    messageObject2 = messageObject3;
                                } else {
                                    textLayoutBlock.charactersOffset = i18;
                                    textLayoutBlock.charactersEnd = i19;
                                    try {
                                        SpannableString valueOf = (!textLayoutBlock.code || textLayoutBlock.quote) ? SpannableString.valueOf(charSequence2.subSequence(i18, i19)) : CodeHighlighting.getHighlighted(charSequence2.subSequence(i18, i19).toString(), textRange.language);
                                        textLayoutBlock.originalWidth = dp6;
                                        StaticLayout makeStaticLayout2 = MessageObject.makeStaticLayout(valueOf, textPaint2, dp6, 1.0f, 0.0f, false);
                                        textLayoutBlock.textLayout = makeStaticLayout2;
                                        textLayoutBlock.height = makeStaticLayout2.getHeight();
                                        textLayoutBlock.collapsedHeight = (int) Math.min(textPaint.getTextSize() * 1.4f * 3.0f, textLayoutBlock.height);
                                    } catch (Exception e2) {
                                        charSequence3 = charSequence2;
                                        i4 = dp4;
                                        arrayList2 = arrayList;
                                        z3 = z;
                                        messageObject2 = messageObject3;
                                        FileLog.e(e2);
                                    }
                                }
                                i3++;
                                dp4 = i4;
                                messageObject3 = messageObject2;
                                charSequence2 = charSequence3;
                                arrayList = arrayList2;
                                z = z3;
                                f3 = 32.0f;
                            }
                            if (textLayoutBlock.code && (textLayoutBlock.textLayout.getText() instanceof Spannable) && TextUtils.isEmpty(textRange.language)) {
                                CodeHighlighting.highlight((Spannable) textLayoutBlock.textLayout.getText(), 0, textLayoutBlock.textLayout.getText().length(), textRange.language, 0, null, true);
                            }
                            this.textLayoutBlocks.add(textLayoutBlock);
                            int lineCount2 = textLayoutBlock.textLayout.getLineCount();
                            float lineLeft = textLayoutBlock.textLayout.getLineLeft(lineCount2 - 1);
                            if (i3 == 0 && lineLeft >= 0.0f) {
                                this.textXOffset = lineLeft;
                            }
                            float f5 = lineLeft;
                            float f6 = textLayoutBlock.textLayout.getLineWidth(lineCount2 - 1);
                            int ceil3 = (int) Math.ceil(f6);
                            ceil3 = ceil3 > dp4 + 80 ? dp4 : ceil3;
                            int i20 = size - 1;
                            if (i3 == i20) {
                                this.lastLineWidth = ceil3;
                            }
                            float f7 = ceil3;
                            charSequence3 = charSequence2;
                            StaticLayout staticLayout3 = staticLayout2;
                            int i21 = i16;
                            int ceil4 = (int) Math.ceil(f7 + Math.max(0.0f, f5));
                            if (textLayoutBlock.quote) {
                                textLayoutBlock.maxRight = 0.0f;
                                int i22 = 0;
                                while (i22 < lineCount2) {
                                    try {
                                        i8 = ceil3;
                                        try {
                                            textLayoutBlock.maxRight = Math.max(textLayoutBlock.maxRight, textLayoutBlock.textLayout.getLineRight(i22));
                                        } catch (Exception unused) {
                                            textLayoutBlock.maxRight = this.textWidth;
                                            i22++;
                                            ceil3 = i8;
                                        }
                                    } catch (Exception unused2) {
                                        i8 = ceil3;
                                    }
                                    i22++;
                                    ceil3 = i8;
                                }
                            }
                            int i23 = ceil3;
                            if (lineCount2 > 1) {
                                arrayList2 = arrayList;
                                int i24 = i23;
                                boolean z11 = false;
                                float f8 = 0.0f;
                                float f9 = 0.0f;
                                int i25 = 0;
                                int i26 = ceil4;
                                while (i25 < lineCount2) {
                                    int i27 = lineCount2;
                                    try {
                                        f = textLayoutBlock.textLayout.getLineWidth(i25);
                                        z4 = z;
                                    } catch (Exception unused3) {
                                        z4 = z;
                                        f = 0.0f;
                                    }
                                    if (textLayoutBlock.quote) {
                                        dp3 = AndroidUtilities.dp(32.0f);
                                    } else {
                                        dp3 = textLayoutBlock.code ? AndroidUtilities.dp(15.0f) : dp3;
                                        f2 = textLayoutBlock.textLayout.getLineLeft(i25);
                                        if (f > dp4 + 20) {
                                            f = dp4;
                                            f2 = 0.0f;
                                        }
                                        if (f2 > 0.0f) {
                                            i6 = dp4;
                                            if (textLayoutBlock.textLayout.getParagraphDirection(i25) != -1) {
                                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                                i7 = 1;
                                                if (z11 && f2 == 0.0f) {
                                                    try {
                                                        if (textLayoutBlock.textLayout.getParagraphDirection(i25) != i7) {
                                                        }
                                                    } catch (Exception unused4) {
                                                    }
                                                    z11 = true;
                                                }
                                                f9 = Math.max(f9, f);
                                                float f10 = f2 + f;
                                                float max = Math.max(f8, f10);
                                                i24 = Math.max(i24, (int) Math.ceil(f));
                                                i26 = Math.max(i26, (int) Math.ceil(f10));
                                                i25++;
                                                z11 = z11;
                                                f8 = max;
                                                lineCount2 = i27;
                                                z = z4;
                                                dp4 = i6;
                                            }
                                        } else {
                                            i6 = dp4;
                                        }
                                        this.textXOffset = Math.min(this.textXOffset, f2);
                                        i7 = 1;
                                        textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                        this.hasRtl = true;
                                        if (z11) {
                                            if (textLayoutBlock.textLayout.getParagraphDirection(i25) != i7) {
                                            }
                                            z11 = true;
                                        }
                                        f9 = Math.max(f9, f);
                                        float f102 = f2 + f;
                                        float max2 = Math.max(f8, f102);
                                        i24 = Math.max(i24, (int) Math.ceil(f));
                                        i26 = Math.max(i26, (int) Math.ceil(f102));
                                        i25++;
                                        z11 = z11;
                                        f8 = max2;
                                        lineCount2 = i27;
                                        z = z4;
                                        dp4 = i6;
                                    }
                                    f += dp3;
                                    f2 = textLayoutBlock.textLayout.getLineLeft(i25);
                                    if (f > dp4 + 20) {
                                    }
                                    if (f2 > 0.0f) {
                                    }
                                    this.textXOffset = Math.min(this.textXOffset, f2);
                                    i7 = 1;
                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                    this.hasRtl = true;
                                    if (z11) {
                                    }
                                    f9 = Math.max(f9, f);
                                    float f1022 = f2 + f;
                                    float max22 = Math.max(f8, f1022);
                                    i24 = Math.max(i24, (int) Math.ceil(f));
                                    i26 = Math.max(i26, (int) Math.ceil(f1022));
                                    i25++;
                                    z11 = z11;
                                    f8 = max22;
                                    lineCount2 = i27;
                                    z = z4;
                                    dp4 = i6;
                                }
                                int i28 = dp4;
                                z3 = z;
                                if (z11) {
                                    if (i3 == i20) {
                                        this.lastLineWidth = ceil4;
                                    }
                                    f9 = f8;
                                } else if (i3 == i20) {
                                    this.lastLineWidth = i24;
                                }
                                this.textWidth = Math.max(this.textWidth, (int) Math.ceil(f9));
                                ceil4 = i26;
                                i4 = i28;
                            } else {
                                int i29 = dp4;
                                arrayList2 = arrayList;
                                z3 = z;
                                if (f5 > 0.0f) {
                                    float min2 = Math.min(this.textXOffset, f5);
                                    this.textXOffset = min2;
                                    i5 = min2 == 0.0f ? (int) (f7 + f5) : i23;
                                    this.hasRtl = size != 1;
                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                } else {
                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                    i5 = i23;
                                }
                                i4 = i29;
                                this.textWidth = Math.max(this.textWidth, Math.min(i4, i5));
                            }
                            Text text = textLayoutBlock.languageLayout;
                            if (text != null) {
                                this.textWidth = (int) Math.max(this.textWidth, Math.min(text.getCurrentWidth() + AndroidUtilities.dp(15.0f), textLayoutBlock.textLayout == null ? 0.0f : r7.getWidth()));
                            }
                            messageObject2 = messageObject;
                            if (messageObject2 != null && !messageObject2.isSpoilersRevealed && !messageObject.spoiledLoginCode) {
                                SpoilerEffect.addSpoilers(null, textLayoutBlock.textLayout, -1, textLayoutBlock.quote ? ceil4 - AndroidUtilities.dp(32.0f) : textLayoutBlock.code ? ceil4 - AndroidUtilities.dp(15.0f) : ceil4, null, textLayoutBlock.spoilers);
                            }
                            staticLayout2 = staticLayout3;
                            i16 = i21;
                            i3++;
                            dp4 = i4;
                            messageObject3 = messageObject2;
                            charSequence2 = charSequence3;
                            arrayList = arrayList2;
                            z = z3;
                            f3 = 32.0f;
                        }
                        return;
                    }
                    dp = AndroidUtilities.dp(15.0f);
                }
                StaticLayout makeStaticLayout3 = MessageObject.makeStaticLayout(charSequence, textPaint, i2, 1.0f, 0.0f, false);
                CharSequence charSequence52 = charSequence4;
                if (messageObject3 != null) {
                }
                staticLayout = makeStaticLayout3;
                charSequence2 = charSequence52;
                if (!this.hasSingleQuote) {
                }
                int lineCount3 = staticLayout.getLineCount();
                if (Build.VERSION.SDK_INT < 24) {
                }
                int i122 = 10;
                if (!z2) {
                }
                arrayList = new ArrayList();
                if (!(charSequence2 instanceof Spanned)) {
                }
                if (z2) {
                }
                arrayList.add(new TextRange(0, staticLayout.getText().length()));
                int size2 = arrayList.size();
                this.hasCodeAtTop = false;
                this.hasCodeAtBottom = false;
                this.hasQuoteAtBottom = false;
                this.hasSingleQuote = false;
                StaticLayout staticLayout22 = staticLayout;
                int i162 = i2;
                i3 = 0;
                while (i3 < arrayList.size()) {
                }
                return;
            } catch (Exception e3) {
                FileLog.e(e3);
                return;
            }
            i2 = i - dp;
        }

        public void bounceFrom(TextLayoutBlocks textLayoutBlocks) {
            if (textLayoutBlocks == null) {
                return;
            }
            for (int i = 0; i < Math.min(this.textLayoutBlocks.size(), textLayoutBlocks.textLayoutBlocks.size()); i++) {
                this.textLayoutBlocks.get(i).collapsedBounce = textLayoutBlocks.textLayoutBlocks.get(i).collapsedBounce;
            }
        }

        public int textHeight() {
            int i = 0;
            for (int i2 = 0; i2 < this.textLayoutBlocks.size(); i2++) {
                i += this.textLayoutBlocks.get(i2).padTop + this.textLayoutBlocks.get(i2).height() + this.textLayoutBlocks.get(i2).padBottom;
            }
            return i;
        }

        public int textHeight(ChatMessageCell.TransitionParams transitionParams) {
            int i = 0;
            for (int i2 = 0; i2 < this.textLayoutBlocks.size(); i2++) {
                i += this.textLayoutBlocks.get(i2).padTop + this.textLayoutBlocks.get(i2).height(transitionParams) + this.textLayoutBlocks.get(i2).padBottom;
            }
            return i;
        }
    }

    public static class TextRange {
        public boolean code;
        public boolean collapse;
        public int end;
        public String language;
        public boolean quote;
        public int start;

        public TextRange(int i, int i2) {
            this.start = i;
            this.end = i2;
        }

        public TextRange(int i, int i2, boolean z, boolean z2, boolean z3, String str) {
            this.start = i;
            this.end = i2;
            this.quote = z;
            this.code = z2;
            this.collapse = z && z3;
            this.language = str;
        }
    }

    public static class VCardData {
        private String company;
        private ArrayList<String> emails = new ArrayList<>();
        private ArrayList<String> phones = new ArrayList<>();

        public static CharSequence parse(String str) {
            byte[] decodeQuotedPrintable;
            try {
                BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
                boolean z = false;
                VCardData vCardData = null;
                String str2 = null;
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        try {
                            break;
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    } else if (!readLine.startsWith("PHOTO")) {
                        if (readLine.indexOf(58) >= 0) {
                            if (readLine.startsWith("BEGIN:VCARD")) {
                                vCardData = new VCardData();
                            } else if (readLine.startsWith("END:VCARD") && vCardData != null) {
                                z = true;
                            }
                        }
                        if (str2 != null) {
                            readLine = str2 + readLine;
                            str2 = null;
                        }
                        if (readLine.contains("=QUOTED-PRINTABLE") && readLine.endsWith("=")) {
                            str2 = readLine.substring(0, readLine.length() - 1);
                        } else {
                            int indexOf = readLine.indexOf(":");
                            String[] strArr = indexOf >= 0 ? new String[]{readLine.substring(0, indexOf), readLine.substring(indexOf + 1).trim()} : new String[]{readLine.trim()};
                            int i = 2;
                            if (strArr.length >= 2 && vCardData != null) {
                                if (strArr[0].startsWith("ORG")) {
                                    String[] split = strArr[0].split(";");
                                    int length = split.length;
                                    int i2 = 0;
                                    String str3 = null;
                                    String str4 = null;
                                    while (i2 < length) {
                                        String[] split2 = split[i2].split("=");
                                        if (split2.length == i) {
                                            if (split2[0].equals("CHARSET")) {
                                                str4 = split2[1];
                                            } else if (split2[0].equals("ENCODING")) {
                                                str3 = split2[1];
                                            }
                                        }
                                        i2++;
                                        i = 2;
                                    }
                                    vCardData.company = strArr[1];
                                    if (str3 != null && str3.equalsIgnoreCase("QUOTED-PRINTABLE") && (decodeQuotedPrintable = AndroidUtilities.decodeQuotedPrintable(AndroidUtilities.getStringBytes(vCardData.company))) != null && decodeQuotedPrintable.length != 0) {
                                        vCardData.company = new String(decodeQuotedPrintable, str4);
                                    }
                                    vCardData.company = vCardData.company.replace(';', ' ');
                                } else if (strArr[0].startsWith("TEL")) {
                                    if (strArr[1].length() > 0) {
                                        vCardData.phones.add(strArr[1]);
                                    }
                                } else if (strArr[0].startsWith("EMAIL")) {
                                    String str5 = strArr[1];
                                    if (str5.length() > 0) {
                                        vCardData.emails.add(str5);
                                    }
                                }
                            }
                        }
                    }
                }
                bufferedReader.close();
                if (!z) {
                    return null;
                }
                StringBuilder sb = new StringBuilder();
                for (int i3 = 0; i3 < vCardData.phones.size(); i3++) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                    }
                    String str6 = vCardData.phones.get(i3);
                    if (!str6.contains("#") && !str6.contains("*")) {
                        str6 = PhoneFormat.getInstance().format(str6);
                    }
                    sb.append(str6);
                }
                for (int i4 = 0; i4 < vCardData.emails.size(); i4++) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                    }
                    sb.append(PhoneFormat.getInstance().format(vCardData.emails.get(i4)));
                }
                if (!TextUtils.isEmpty(vCardData.company)) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                    }
                    sb.append(vCardData.company);
                }
                return sb;
            } catch (Throwable unused) {
                return null;
            }
        }
    }

    public MessageObject(int i, TLRPC.Message message, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2) {
        this(i, message, null, null, null, longSparseArray, longSparseArray2, z, z2, 0L, false, false, false);
    }

    public MessageObject(int i, TLRPC.Message message, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2, boolean z3) {
        this(i, message, null, null, null, longSparseArray, longSparseArray2, z, z2, 0L, false, false, z3);
    }

    public MessageObject(int i, TLRPC.Message message, LongSparseArray longSparseArray, boolean z, boolean z2) {
        this(i, message, longSparseArray, (LongSparseArray) null, z, z2);
    }

    public MessageObject(int i, TLRPC.Message message, String str, String str2, String str3, boolean z, boolean z2, boolean z3, boolean z4) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.actionDeleteGroupEventId = -1L;
        this.overrideLinkColor = -1;
        this.overrideLinkEmoji = -1L;
        this.messageTrimmedToHighlightCut = true;
        this.topicIconDrawable = new Drawable[1];
        this.spoiledLoginCode = false;
        this.translated = false;
        this.localType = z ? 2 : 1;
        this.currentAccount = i;
        this.localName = str2;
        this.localUserName = str3;
        this.messageText = str;
        this.messageOwner = message;
        this.localChannel = z2;
        this.localSupergroup = z3;
        this.localEdit = z4;
    }

    public MessageObject(int i, TLRPC.Message message, AbstractMap<Long, TLRPC.User> abstractMap, AbstractMap<Long, TLRPC.Chat> abstractMap2, boolean z, boolean z2) {
        this(i, message, abstractMap, abstractMap2, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC.Message message, AbstractMap<Long, TLRPC.User> abstractMap, AbstractMap<Long, TLRPC.Chat> abstractMap2, boolean z, boolean z2, long j) {
        this(i, message, null, abstractMap, abstractMap2, null, null, z, z2, j);
    }

    public MessageObject(int i, TLRPC.Message message, AbstractMap<Long, TLRPC.User> abstractMap, boolean z, boolean z2) {
        this(i, message, abstractMap, (AbstractMap<Long, TLRPC.Chat>) null, z, z2);
    }

    public MessageObject(int i, TLRPC.Message message, MessageObject messageObject, AbstractMap<Long, TLRPC.User> abstractMap, AbstractMap<Long, TLRPC.Chat> abstractMap2, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2, long j) {
        this(i, message, messageObject, abstractMap, abstractMap2, longSparseArray, longSparseArray2, z, z2, j, false, false, false);
    }

    public MessageObject(int i, TLRPC.Message message, MessageObject messageObject, AbstractMap<Long, TLRPC.User> abstractMap, AbstractMap<Long, TLRPC.Chat> abstractMap2, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2, long j, boolean z3, boolean z4, boolean z5) {
        this(i, message, messageObject, abstractMap, abstractMap2, longSparseArray, longSparseArray2, z, z2, j, z3, z4, z5, 0);
    }

    public MessageObject(int i, TLRPC.Message message, MessageObject messageObject, AbstractMap<Long, TLRPC.User> abstractMap, AbstractMap<Long, TLRPC.Chat> abstractMap2, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2, long j, boolean z3, boolean z4, boolean z5, int i2) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.actionDeleteGroupEventId = -1L;
        this.overrideLinkColor = -1;
        this.overrideLinkEmoji = -1L;
        this.messageTrimmedToHighlightCut = true;
        this.topicIconDrawable = new Drawable[1];
        this.spoiledLoginCode = false;
        this.translated = false;
        Theme.createCommonMessageResources();
        this.isRepostPreview = z3;
        this.isRepostVideoPreview = z4;
        this.isSaved = z5 || getDialogId(message) == UserConfig.getInstance(i).getClientUserId();
        this.searchType = i2;
        this.currentAccount = i;
        this.messageOwner = message;
        this.replyMessageObject = messageObject;
        this.eventId = j;
        this.wasUnread = !message.out && message.unread;
        TLRPC.Message message2 = message.replyMessage;
        if (message2 != null) {
            this.replyMessageObject = new MessageObject(i, message2, null, abstractMap, abstractMap2, longSparseArray, longSparseArray2, false, z2, j);
        }
        TLRPC.Peer peer = message.from_id;
        if (peer instanceof TLRPC.TL_peerUser) {
            getUser(abstractMap, longSparseArray, peer.user_id);
        }
        updateMessageText(abstractMap, abstractMap2, longSparseArray, longSparseArray2);
        setType();
        if (z) {
            updateTranslation(false);
        }
        measureInlineBotButtons();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(this.messageOwner.date * 1000);
        int i3 = gregorianCalendar.get(6);
        int i4 = gregorianCalendar.get(1);
        int i5 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i3));
        this.dateKeyInt = (i5 * 10000) + i4 + (i3 * MediaController.VIDEO_BITRATE_480);
        this.monthKey = String.format("%d_%02d", Integer.valueOf(i4), Integer.valueOf(i5));
        createMessageSendInfo();
        generateCaption();
        if (z) {
            TextPaint textPaint = getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
            int[] iArr = allowsBigEmoji() ? new int[1] : null;
            CharSequence replaceEmoji = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), false, iArr);
            this.messageText = replaceEmoji;
            Spannable replaceAnimatedEmoji = replaceAnimatedEmoji(replaceEmoji, textPaint.getFontMetricsInt());
            this.messageText = replaceAnimatedEmoji;
            if (iArr != null && iArr[0] > 1) {
                replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr);
            }
            checkEmojiOnly(iArr);
            checkBigAnimatedEmoji();
            setType();
            createPathThumb();
        }
        this.layoutCreated = z;
        generateThumbs(false);
        if (z2) {
            checkMediaExistance();
        }
    }

    public MessageObject(int i, TLRPC.Message message, MessageObject messageObject, boolean z, boolean z2) {
        this(i, message, messageObject, null, null, null, null, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC.Message message, boolean z, boolean z2) {
        this(i, message, null, null, null, null, null, z, z2, 0L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x01e0, code lost:
    
        r1 = org.telegram.messenger.R.string.EventLogGroupJoined;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1042:0x12fe, code lost:
    
        if (r1.length() == 0) goto L889;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x01de, code lost:
    
        if (r41.megagroup != false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x0b0a, code lost:
    
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x0b60, code lost:
    
        r0.media.webpage.description = r1.message;
        r1 = r1.entities;
     */
    /* JADX WARN: Code restructure failed: missing block: B:676:0x0b53, code lost:
    
        r0.media.webpage.description = org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.EventLogOriginalCaptionEmpty);
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x0b51, code lost:
    
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0163, code lost:
    
        if (r41.megagroup != false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x01e4, code lost:
    
        r1 = org.telegram.messenger.R.string.EventLogChannelJoined;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:18:0x18e1  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x1933  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x1936  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x19d1 A[ADDED_TO_REGION, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x19d2  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x19e7  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x19f7  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x1a03  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x1a0e  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x1a43  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x1a11  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x1a06  */
    /* JADX WARN: Removed duplicated region for block: B:665:0x0acd  */
    /* JADX WARN: Removed duplicated region for block: B:669:0x0af1  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x19eb  */
    /* JADX WARN: Removed duplicated region for block: B:678:0x0ada  */
    /* JADX WARN: Removed duplicated region for block: B:702:0x0b1f  */
    /* JADX WARN: Removed duplicated region for block: B:703:0x0b29  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x19c6  */
    /* JADX WARN: Type inference failed for: r12v25 */
    /* JADX WARN: Type inference failed for: r12v26, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r12v31 */
    /* JADX WARN: Type inference failed for: r12v32 */
    /* JADX WARN: Type inference failed for: r14v11 */
    /* JADX WARN: Type inference failed for: r14v12 */
    /* JADX WARN: Type inference failed for: r14v4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MessageObject(int i, TLRPC.TL_channelAdminLogEvent tL_channelAdminLogEvent, ArrayList<MessageObject> arrayList, HashMap<String, ArrayList<MessageObject>> hashMap, TLRPC.Chat chat, int[] iArr, boolean z) {
        String str;
        TLRPC.User user;
        TLRPC.TL_channelAdminLogEvent tL_channelAdminLogEvent2;
        TLRPC.User user2;
        boolean z2;
        TLRPC.Chat chat2;
        CharSequence charSequence;
        TLRPC.Message message;
        TLRPC.ChannelParticipant channelParticipant;
        TLRPC.ChannelParticipant channelParticipant2;
        MessagesController messagesController;
        char c;
        StringBuilder sb;
        String str2;
        TLRPC.TL_channelAdminLogEvent tL_channelAdminLogEvent3;
        String str3;
        String str4;
        String str5;
        CharSequence charSequence2;
        CharSequence charSequence3;
        SpannableString spannableString;
        SpannableString spannableString2;
        int i2;
        CharSequence string;
        SpannableString spannableString3;
        boolean z3;
        SpannableString spannableString4;
        SpannableStringBuilder spannableStringBuilder;
        SpannableStringBuilder spannableStringBuilder2;
        SpannableStringBuilder spannableStringBuilder3;
        SpannableStringBuilder spannableStringBuilder4;
        SpannableStringBuilder spannableStringBuilder5;
        CharSequence replaceWithLink;
        TLObject tLObject;
        CharSequence replaceWithLink2;
        TLObject tLObject2;
        String formatString;
        CharSequence charSequence4;
        CharSequence charSequence5;
        TLRPC.Message message2;
        int i3;
        char c2;
        String formatPluralString;
        TLObject user3;
        CharSequence replaceWithLink3;
        TLRPC.Message message3;
        CharSequence charSequence6;
        int i4;
        int i5;
        ArrayList<TLRPC.MessageEntity> arrayList2;
        ArrayList<TLRPC.MessageEntity> arrayList3;
        boolean z4;
        boolean z5;
        TLRPC.Photo photo;
        TLRPC.Photo photo2;
        TLRPC.Peer peer;
        String str6;
        TLRPC.TL_messageMediaEmpty tL_messageMediaEmpty;
        TLRPC.WebPage webPage;
        String str7;
        int i6;
        CharSequence replace;
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.User user4;
        String str8;
        CharSequence format;
        StringBuilder sb2;
        boolean z6;
        char c3;
        int i7;
        String formatPluralString2;
        int i8;
        int i9;
        boolean z7;
        CharSequence sb3;
        MediaController mediaController;
        ArrayList<MessageObject> arrayList4;
        int[] iArr2;
        ?? r12;
        int i10;
        int[] iArr3;
        Spannable replaceAnimatedEmoji;
        int i11;
        boolean z8;
        boolean z9;
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.actionDeleteGroupEventId = -1L;
        this.overrideLinkColor = -1;
        this.overrideLinkEmoji = -1L;
        this.messageTrimmedToHighlightCut = true;
        this.topicIconDrawable = new Drawable[1];
        this.spoiledLoginCode = false;
        this.translated = false;
        this.currentEvent = tL_channelAdminLogEvent;
        this.currentAccount = i;
        if (tL_channelAdminLogEvent.user_id > 0) {
            str = "%2$s";
            user = MessagesController.getInstance(i).getUser(Long.valueOf(tL_channelAdminLogEvent.user_id));
        } else {
            str = "%2$s";
            user = null;
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(tL_channelAdminLogEvent.date * 1000);
        int i12 = gregorianCalendar.get(6);
        int i13 = gregorianCalendar.get(1);
        int i14 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i13), Integer.valueOf(i14), Integer.valueOf(i12));
        this.dateKeyInt = i13 + (i14 * 1000) + (i12 * 100000);
        this.monthKey = String.format("%d_%02d", Integer.valueOf(i13), Integer.valueOf(i14));
        TLRPC.TL_peerChannel tL_peerChannel = new TLRPC.TL_peerChannel();
        tL_peerChannel.channel_id = chat.id;
        TLRPC.ChannelAdminLogEventAction channelAdminLogEventAction = tL_channelAdminLogEvent.action;
        String str9 = "";
        String str10 = str;
        String str11 = "un1";
        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeTitle) {
            String str12 = ((TLRPC.TL_channelAdminLogEventActionChangeTitle) channelAdminLogEventAction).new_value;
            string = chat.megagroup ? LocaleController.formatString("EventLogEditedGroupTitle", R.string.EventLogEditedGroupTitle, str12) : LocaleController.formatString("EventLogEditedChannelTitle", R.string.EventLogEditedChannelTitle, str12);
        } else {
            if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangePhoto) {
                TLRPC.TL_channelAdminLogEventActionChangePhoto tL_channelAdminLogEventActionChangePhoto = (TLRPC.TL_channelAdminLogEventActionChangePhoto) channelAdminLogEventAction;
                TLRPC.TL_messageService tL_messageService = new TLRPC.TL_messageService();
                this.messageOwner = tL_messageService;
                if (tL_channelAdminLogEventActionChangePhoto.new_photo instanceof TLRPC.TL_photoEmpty) {
                    tL_messageService.action = new TLRPC.TL_messageActionChatDeletePhoto();
                    i3 = chat.megagroup ? R.string.EventLogRemovedWGroupPhoto : R.string.EventLogRemovedChannelPhoto;
                } else {
                    tL_messageService.action = new TLRPC.TL_messageActionChatEditPhoto();
                    this.messageOwner.action.photo = tL_channelAdminLogEventActionChangePhoto.new_photo;
                    i3 = chat.megagroup ? isVideoAvatar() ? R.string.EventLogEditedGroupVideo : R.string.EventLogEditedGroupPhoto : isVideoAvatar() ? R.string.EventLogEditedChannelVideo : R.string.EventLogEditedChannelPhoto;
                }
            } else if (!(channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantJoin)) {
                if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantLeave) {
                    TLRPC.TL_messageService tL_messageService2 = new TLRPC.TL_messageService();
                    this.messageOwner = tL_messageService2;
                    tL_messageService2.action = new TLRPC.TL_messageActionChatDeleteUser();
                    this.messageOwner.action.user_id = tL_channelAdminLogEvent.user_id;
                    i3 = chat.megagroup ? R.string.EventLogLeftGroup : R.string.EventLogLeftChannel;
                } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantInvite) {
                    TLRPC.TL_messageService tL_messageService3 = new TLRPC.TL_messageService();
                    this.messageOwner = tL_messageService3;
                    tL_messageService3.action = new TLRPC.TL_messageActionChatAddUser();
                    long peerId = getPeerId(((TLRPC.TL_channelAdminLogEventActionParticipantInvite) channelAdminLogEventAction).participant.peer);
                    TLObject user5 = peerId > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId));
                    TLRPC.Peer peer2 = this.messageOwner.from_id;
                    if (!(peer2 instanceof TLRPC.TL_peerUser) || peerId != peer2.user_id) {
                        string = replaceWithLink(LocaleController.getString(R.string.EventLogAdded), "un2", user5);
                        this.messageText = string;
                    }
                } else {
                    boolean z10 = channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin;
                    if (z10) {
                        tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                        user2 = user;
                        z2 = z10;
                    } else {
                        boolean z11 = channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleBan;
                        if (z11) {
                            z2 = z10;
                            TLRPC.TL_channelAdminLogEventActionParticipantToggleBan tL_channelAdminLogEventActionParticipantToggleBan = (TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) channelAdminLogEventAction;
                            str2 = "/";
                            if ((tL_channelAdminLogEventActionParticipantToggleBan.prev_participant instanceof TLRPC.TL_channelParticipantAdmin) && (tL_channelAdminLogEventActionParticipantToggleBan.new_participant instanceof TLRPC.TL_channelParticipant)) {
                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                user2 = user;
                            }
                        } else {
                            str2 = "/";
                        }
                        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionDefaultBannedRights) {
                            TLRPC.TL_channelAdminLogEventActionDefaultBannedRights tL_channelAdminLogEventActionDefaultBannedRights = (TLRPC.TL_channelAdminLogEventActionDefaultBannedRights) channelAdminLogEventAction;
                            TLRPC.TL_message tL_message = new TLRPC.TL_message();
                            this.messageOwner = tL_message;
                            tL_message.realId = -1;
                            TLRPC.TL_chatBannedRights tL_chatBannedRights = tL_channelAdminLogEventActionDefaultBannedRights.prev_banned_rights;
                            TLRPC.TL_chatBannedRights tL_chatBannedRights2 = tL_channelAdminLogEventActionDefaultBannedRights.new_banned_rights;
                            StringBuilder sb4 = new StringBuilder(LocaleController.getString(R.string.EventLogDefaultPermissions));
                            tL_chatBannedRights = tL_chatBannedRights == null ? new TLRPC.TL_chatBannedRights() : tL_chatBannedRights;
                            tL_chatBannedRights2 = tL_chatBannedRights2 == null ? new TLRPC.TL_chatBannedRights() : tL_chatBannedRights2;
                            if (tL_chatBannedRights.send_messages != tL_chatBannedRights2.send_messages) {
                                sb4.append('\n');
                                sb4.append('\n');
                                sb4.append(!tL_chatBannedRights2.send_messages ? '+' : '-');
                                sb4.append(' ');
                                sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendMessages));
                                z7 = true;
                            } else {
                                z7 = false;
                            }
                            if (tL_chatBannedRights.send_stickers != tL_chatBannedRights2.send_stickers || tL_chatBannedRights.send_inline != tL_chatBannedRights2.send_inline || tL_chatBannedRights.send_gifs != tL_chatBannedRights2.send_gifs || tL_chatBannedRights.send_games != tL_chatBannedRights2.send_games) {
                                if (!z7) {
                                    sb4.append('\n');
                                    z7 = true;
                                }
                                sb4.append('\n');
                                sb4.append(!tL_chatBannedRights2.send_stickers ? '+' : '-');
                                sb4.append(' ');
                                sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendStickers));
                            }
                            if (tL_chatBannedRights.send_media != tL_chatBannedRights2.send_media) {
                                if (!z7) {
                                    sb4.append('\n');
                                    z7 = true;
                                }
                                sb4.append('\n');
                                sb4.append(!tL_chatBannedRights2.send_media ? '+' : '-');
                                sb4.append(' ');
                                sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendMedia));
                            }
                            if (tL_chatBannedRights.send_polls != tL_chatBannedRights2.send_polls) {
                                if (!z7) {
                                    sb4.append('\n');
                                    z7 = true;
                                }
                                sb4.append('\n');
                                sb4.append(!tL_chatBannedRights2.send_polls ? '+' : '-');
                                sb4.append(' ');
                                sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendPolls));
                            }
                            if (tL_chatBannedRights.embed_links != tL_chatBannedRights2.embed_links) {
                                if (!z7) {
                                    sb4.append('\n');
                                    z7 = true;
                                }
                                sb4.append('\n');
                                sb4.append(!tL_chatBannedRights2.embed_links ? '+' : '-');
                                sb4.append(' ');
                                sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendEmbed));
                            }
                            if (tL_chatBannedRights.change_info != tL_chatBannedRights2.change_info) {
                                if (!z7) {
                                    sb4.append('\n');
                                    z7 = true;
                                }
                                sb4.append('\n');
                                sb4.append(!tL_chatBannedRights2.change_info ? '+' : '-');
                                sb4.append(' ');
                                sb4.append(LocaleController.getString(R.string.EventLogRestrictedChangeInfo));
                            }
                            if (tL_chatBannedRights.invite_users != tL_chatBannedRights2.invite_users) {
                                if (!z7) {
                                    sb4.append('\n');
                                    z7 = true;
                                }
                                sb4.append('\n');
                                sb4.append(!tL_chatBannedRights2.invite_users ? '+' : '-');
                                sb4.append(' ');
                                sb4.append(LocaleController.getString(R.string.EventLogRestrictedInviteUsers));
                            }
                            if (tL_chatBannedRights.pin_messages != tL_chatBannedRights2.pin_messages) {
                                if (!z7) {
                                    sb4.append('\n');
                                }
                                sb4.append('\n');
                                sb4.append(!tL_chatBannedRights2.pin_messages ? '+' : '-');
                                sb4.append(' ');
                                sb4.append(LocaleController.getString(R.string.EventLogRestrictedPinMessages));
                            }
                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                            user2 = user;
                            message = null;
                            chat2 = chat;
                            sb3 = sb4.toString();
                            this.messageText = sb3;
                            message3 = message;
                            arrayList3 = null;
                            if (this.messageOwner == null) {
                                this.messageOwner = new TLRPC.TL_messageService();
                            }
                            this.messageOwner.message = this.messageText.toString();
                            this.messageOwner.from_id = new TLRPC.TL_peerUser();
                            TLRPC.Message message4 = this.messageOwner;
                            message4.from_id.user_id = tL_channelAdminLogEvent2.user_id;
                            message4.date = tL_channelAdminLogEvent2.date;
                            int i15 = iArr[0];
                            iArr[0] = i15 + 1;
                            message4.id = i15;
                            this.eventId = tL_channelAdminLogEvent2.id;
                            message4.out = false;
                            message4.peer_id = new TLRPC.TL_peerChannel();
                            TLRPC.Message message5 = this.messageOwner;
                            message5.peer_id.channel_id = chat2.id;
                            message5.unread = false;
                            mediaController = MediaController.getInstance();
                            this.isOutOwnerCached = null;
                            message3 = message3 instanceof TLRPC.TL_messageEmpty ? null : message3;
                            if (message3 == null) {
                                message3.out = false;
                                message3.realId = message3.id;
                                int i16 = iArr[0];
                                iArr[0] = i16 + 1;
                                message3.id = i16;
                                message3.flags &= -32769;
                                message3.dialog_id = -chat2.id;
                                if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                                    i11 = message3.date;
                                    message3.date = tL_channelAdminLogEvent2.date;
                                } else {
                                    i11 = 0;
                                }
                                MessageObject messageObject = new MessageObject(this.currentAccount, message3, (AbstractMap<Long, TLRPC.User>) null, (AbstractMap<Long, TLRPC.Chat>) null, true, true, this.eventId);
                                messageObject.realDate = i11;
                                messageObject.currentEvent = tL_channelAdminLogEvent2;
                                if (messageObject.contentType >= 0) {
                                    if (mediaController.isPlayingMessage(messageObject)) {
                                        MessageObject playingMessageObject = mediaController.getPlayingMessageObject();
                                        messageObject.audioProgress = playingMessageObject.audioProgress;
                                        messageObject.audioProgressSec = playingMessageObject.audioProgressSec;
                                    }
                                    z8 = false;
                                    z8 = false;
                                    z9 = true;
                                    z9 = true;
                                    createDateArray(this.currentAccount, tL_channelAdminLogEvent, arrayList, hashMap, z);
                                    arrayList4 = arrayList;
                                    if (z) {
                                        arrayList4.add(0, messageObject);
                                    } else {
                                        arrayList4.add(arrayList.size() - 1, messageObject);
                                    }
                                } else {
                                    arrayList4 = arrayList;
                                    z8 = false;
                                    z9 = true;
                                    this.contentType = -1;
                                }
                                if (arrayList3 != null) {
                                    messageObject.webPageDescriptionEntities = arrayList3;
                                    iArr2 = null;
                                    messageObject.linkDescription = null;
                                    messageObject.generateLinkDescription();
                                    r12 = z8;
                                    i10 = z9;
                                } else {
                                    iArr2 = null;
                                    r12 = z8;
                                    i10 = z9;
                                }
                            } else {
                                arrayList4 = arrayList;
                                iArr2 = null;
                                r12 = 0;
                                i10 = 1;
                            }
                            if ((tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) && this.contentType >= 0) {
                                createDateArray(this.currentAccount, tL_channelAdminLogEvent, arrayList, hashMap, z);
                                if (z) {
                                    arrayList4.add(arrayList.size() - i10, this);
                                } else {
                                    arrayList4.add(r12, this);
                                }
                                if (this.messageText == null) {
                                    this.messageText = str9;
                                }
                                TextPaint textPaint = !(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame) ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
                                iArr3 = !allowsBigEmoji() ? new int[i10] : iArr2;
                                CharSequence replaceEmoji = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), (boolean) r12, iArr3);
                                this.messageText = replaceEmoji;
                                replaceAnimatedEmoji = replaceAnimatedEmoji(replaceEmoji, textPaint.getFontMetricsInt());
                                this.messageText = replaceAnimatedEmoji;
                                if (iArr3 != null && iArr3[r12] > i10) {
                                    replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr3);
                                }
                                checkEmojiOnly(iArr3);
                                setType();
                                measureInlineBotButtons();
                                generateCaption();
                                if (mediaController.isPlayingMessage(this)) {
                                    MessageObject playingMessageObject2 = mediaController.getPlayingMessageObject();
                                    this.audioProgress = playingMessageObject2.audioProgress;
                                    this.audioProgressSec = playingMessageObject2.audioProgressSec;
                                }
                                generateLayout(user2);
                                this.layoutCreated = i10;
                                generateThumbs(r12);
                                checkMediaExistance();
                                return;
                            }
                            return;
                        }
                        if (z11) {
                            TLRPC.TL_channelAdminLogEventActionParticipantToggleBan tL_channelAdminLogEventActionParticipantToggleBan2 = (TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) channelAdminLogEventAction;
                            TLRPC.TL_message tL_message2 = new TLRPC.TL_message();
                            this.messageOwner = tL_message2;
                            tL_message2.realId = -1;
                            long peerId2 = getPeerId(tL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.peer);
                            TLObject user6 = peerId2 > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId2)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId2));
                            TLRPC.TL_chatBannedRights tL_chatBannedRights3 = tL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.banned_rights;
                            TLRPC.TL_chatBannedRights tL_chatBannedRights4 = tL_channelAdminLogEventActionParticipantToggleBan2.new_participant.banned_rights;
                            if (!chat.megagroup || (tL_chatBannedRights4 != null && tL_chatBannedRights4.view_messages && (tL_chatBannedRights3 == null || tL_chatBannedRights4.until_date == tL_chatBannedRights3.until_date))) {
                                user4 = user;
                                str8 = "";
                                String string2 = LocaleController.getString((tL_chatBannedRights4 == null || !(tL_chatBannedRights3 == null || tL_chatBannedRights4.view_messages)) ? R.string.EventLogChannelUnrestricted : R.string.EventLogChannelRestricted);
                                format = String.format(string2, getUserName(user6, this.messageOwner.entities, string2.indexOf("%1$s")));
                            } else {
                                if (tL_chatBannedRights4 != null && !AndroidUtilities.isBannedForever(tL_chatBannedRights4)) {
                                    sb2 = new StringBuilder();
                                    int i17 = tL_chatBannedRights4.until_date - tL_channelAdminLogEvent.date;
                                    int i18 = ((i17 / 60) / 60) / 24;
                                    int i19 = i17 - (86400 * i18);
                                    str8 = "";
                                    int i20 = (i19 / 60) / 60;
                                    int i21 = (i19 - (i20 * 3600)) / 60;
                                    int i22 = 3;
                                    int i23 = 0;
                                    int i24 = 0;
                                    while (true) {
                                        if (i23 >= i22) {
                                            user4 = user;
                                            break;
                                        }
                                        user4 = user;
                                        if (i23 != 0) {
                                            i7 = i18;
                                            if (i23 == 1) {
                                                if (i20 != 0) {
                                                    formatPluralString2 = LocaleController.formatPluralString("Hours", i20, new Object[0]);
                                                    i8 = i24 + 1;
                                                    i9 = i8;
                                                }
                                                i9 = i24;
                                                formatPluralString2 = null;
                                            } else {
                                                if (i21 != 0) {
                                                    formatPluralString2 = LocaleController.formatPluralString("Minutes", i21, new Object[0]);
                                                    i8 = i24 + 1;
                                                    i9 = i8;
                                                }
                                                i9 = i24;
                                                formatPluralString2 = null;
                                            }
                                        } else if (i18 != 0) {
                                            i8 = i24 + 1;
                                            i7 = i18;
                                            formatPluralString2 = LocaleController.formatPluralString("Days", i18, new Object[0]);
                                            i9 = i8;
                                        } else {
                                            i7 = i18;
                                            i9 = i24;
                                            formatPluralString2 = null;
                                        }
                                        if (formatPluralString2 != null) {
                                            if (sb2.length() > 0) {
                                                sb2.append(", ");
                                            }
                                            sb2.append(formatPluralString2);
                                        }
                                        if (i9 == 2) {
                                            break;
                                        }
                                        i23++;
                                        i18 = i7;
                                        i24 = i9;
                                        user = user4;
                                        i22 = 3;
                                    }
                                } else {
                                    user4 = user;
                                    str8 = "";
                                    sb2 = new StringBuilder(LocaleController.getString(R.string.UserRestrictionsUntilForever));
                                }
                                String string3 = LocaleController.getString(R.string.EventLogRestrictedUntil);
                                StringBuilder sb5 = new StringBuilder(String.format(string3, getUserName(user6, this.messageOwner.entities, string3.indexOf("%1$s")), sb2.toString()));
                                tL_chatBannedRights3 = tL_chatBannedRights3 == null ? new TLRPC.TL_chatBannedRights() : tL_chatBannedRights3;
                                tL_chatBannedRights4 = tL_chatBannedRights4 == null ? new TLRPC.TL_chatBannedRights() : tL_chatBannedRights4;
                                if (tL_chatBannedRights3.view_messages != tL_chatBannedRights4.view_messages) {
                                    sb5.append('\n');
                                    sb5.append('\n');
                                    sb5.append(!tL_chatBannedRights4.view_messages ? '+' : '-');
                                    sb5.append(' ');
                                    sb5.append(LocaleController.getString(R.string.EventLogRestrictedReadMessages));
                                    z6 = true;
                                } else {
                                    z6 = false;
                                }
                                if (tL_chatBannedRights3.send_messages != tL_chatBannedRights4.send_messages) {
                                    if (!z6) {
                                        sb5.append('\n');
                                        z6 = true;
                                    }
                                    sb5.append('\n');
                                    sb5.append(!tL_chatBannedRights4.send_messages ? '+' : '-');
                                    sb5.append(' ');
                                    sb5.append(LocaleController.getString(R.string.EventLogRestrictedSendMessages));
                                }
                                if (tL_chatBannedRights3.send_stickers != tL_chatBannedRights4.send_stickers || tL_chatBannedRights3.send_inline != tL_chatBannedRights4.send_inline || tL_chatBannedRights3.send_gifs != tL_chatBannedRights4.send_gifs || tL_chatBannedRights3.send_games != tL_chatBannedRights4.send_games) {
                                    if (!z6) {
                                        sb5.append('\n');
                                        z6 = true;
                                    }
                                    sb5.append('\n');
                                    sb5.append(!tL_chatBannedRights4.send_stickers ? '+' : '-');
                                    sb5.append(' ');
                                    sb5.append(LocaleController.getString(R.string.EventLogRestrictedSendStickers));
                                }
                                if (tL_chatBannedRights3.send_media != tL_chatBannedRights4.send_media) {
                                    if (!z6) {
                                        sb5.append('\n');
                                        z6 = true;
                                    }
                                    sb5.append('\n');
                                    sb5.append(!tL_chatBannedRights4.send_media ? '+' : '-');
                                    sb5.append(' ');
                                    sb5.append(LocaleController.getString(R.string.EventLogRestrictedSendMedia));
                                }
                                if (tL_chatBannedRights3.send_polls != tL_chatBannedRights4.send_polls) {
                                    if (!z6) {
                                        sb5.append('\n');
                                        z6 = true;
                                    }
                                    sb5.append('\n');
                                    sb5.append(!tL_chatBannedRights4.send_polls ? '+' : '-');
                                    sb5.append(' ');
                                    sb5.append(LocaleController.getString(R.string.EventLogRestrictedSendPolls));
                                }
                                if (tL_chatBannedRights3.embed_links != tL_chatBannedRights4.embed_links) {
                                    if (!z6) {
                                        sb5.append('\n');
                                        z6 = true;
                                    }
                                    sb5.append('\n');
                                    sb5.append(!tL_chatBannedRights4.embed_links ? '+' : '-');
                                    sb5.append(' ');
                                    sb5.append(LocaleController.getString(R.string.EventLogRestrictedSendEmbed));
                                }
                                if (tL_chatBannedRights3.change_info != tL_chatBannedRights4.change_info) {
                                    if (!z6) {
                                        sb5.append('\n');
                                        z6 = true;
                                    }
                                    sb5.append('\n');
                                    sb5.append(!tL_chatBannedRights4.change_info ? '+' : '-');
                                    sb5.append(' ');
                                    sb5.append(LocaleController.getString(R.string.EventLogRestrictedChangeInfo));
                                }
                                if (tL_chatBannedRights3.invite_users != tL_chatBannedRights4.invite_users) {
                                    if (!z6) {
                                        sb5.append('\n');
                                        z6 = true;
                                    }
                                    sb5.append('\n');
                                    sb5.append(!tL_chatBannedRights4.invite_users ? '+' : '-');
                                    sb5.append(' ');
                                    sb5.append(LocaleController.getString(R.string.EventLogRestrictedInviteUsers));
                                }
                                if (tL_chatBannedRights3.pin_messages != tL_chatBannedRights4.pin_messages) {
                                    if (z6) {
                                        c3 = '\n';
                                    } else {
                                        c3 = '\n';
                                        sb5.append('\n');
                                    }
                                    sb5.append(c3);
                                    sb5.append(!tL_chatBannedRights4.pin_messages ? '+' : '-');
                                    sb5.append(' ');
                                    sb5.append(LocaleController.getString(R.string.EventLogRestrictedPinMessages));
                                }
                                format = sb5.toString();
                            }
                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                            charSequence3 = format;
                            str9 = str8;
                            user2 = user4;
                        } else {
                            TLRPC.User user7 = user;
                            if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionUpdatePinned) {
                                TLRPC.TL_channelAdminLogEventActionUpdatePinned tL_channelAdminLogEventActionUpdatePinned = (TLRPC.TL_channelAdminLogEventActionUpdatePinned) channelAdminLogEventAction;
                                message2 = tL_channelAdminLogEventActionUpdatePinned.message;
                                user2 = user7;
                                if (user7 == null || user2.id != 136817688 || (messageFwdHeader = message2.fwd_from) == null || !(messageFwdHeader.from_id instanceof TLRPC.TL_peerChannel)) {
                                    i3 = ((message2 instanceof TLRPC.TL_messageEmpty) || !message2.pinned) ? R.string.EventLogUnpinnedMessages : R.string.EventLogPinnedMessages;
                                    str9 = "";
                                    string = LocaleController.getString(i3);
                                    tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                    str5 = "un1";
                                    replaceWithLink3 = string;
                                    user3 = user2;
                                    str11 = str5;
                                    CharSequence replaceWithLink4 = replaceWithLink(replaceWithLink3, str11, user3);
                                    message = message2;
                                    replace = replaceWithLink4;
                                    chat2 = chat;
                                    sb3 = replace;
                                } else {
                                    user3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tL_channelAdminLogEventActionUpdatePinned.message.fwd_from.from_id.channel_id));
                                    TLRPC.Message message6 = tL_channelAdminLogEventActionUpdatePinned.message;
                                    replaceWithLink3 = LocaleController.getString(((message6 instanceof TLRPC.TL_messageEmpty) || !message6.pinned) ? R.string.EventLogUnpinnedMessages : R.string.EventLogPinnedMessages);
                                    tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                    str9 = "";
                                    CharSequence replaceWithLink42 = replaceWithLink(replaceWithLink3, str11, user3);
                                    message = message2;
                                    replace = replaceWithLink42;
                                    chat2 = chat;
                                    sb3 = replace;
                                }
                            } else {
                                user2 = user7;
                                if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionStopPoll) {
                                    message2 = ((TLRPC.TL_channelAdminLogEventActionStopPoll) channelAdminLogEventAction).message;
                                    i3 = ((getMedia(message2) instanceof TLRPC.TL_messageMediaPoll) && ((TLRPC.TL_messageMediaPoll) getMedia(message2)).poll.quiz) ? R.string.EventLogStopQuiz : R.string.EventLogStopPoll;
                                } else {
                                    if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionToggleSignatures) {
                                        i3 = ((TLRPC.TL_channelAdminLogEventActionToggleSignatures) channelAdminLogEventAction).new_value ? R.string.EventLogToggledSignaturesOn : R.string.EventLogToggledSignaturesOff;
                                    } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantSubExtend) {
                                        i3 = R.string.EventLogSubExtend;
                                    } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionToggleSignatureProfiles) {
                                        i3 = ((TLRPC.TL_channelAdminLogEventActionToggleSignatureProfiles) channelAdminLogEventAction).value ? R.string.EventLogToggledSignaturesProfilesOn : R.string.EventLogToggledSignaturesProfilesOff;
                                    } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionToggleInvites) {
                                        i3 = ((TLRPC.TL_channelAdminLogEventActionToggleInvites) channelAdminLogEventAction).new_value ? R.string.EventLogToggledInvitesOn : R.string.EventLogToggledInvitesOff;
                                    } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                                        message2 = ((TLRPC.TL_channelAdminLogEventActionDeleteMessage) channelAdminLogEventAction).message;
                                        if (user2 == null || user2.id != MessagesController.getInstance(this.currentAccount).telegramAntispamUserId) {
                                            i3 = R.string.EventLogDeletedMessages;
                                        } else {
                                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                            message = message2;
                                            replace = LocaleController.getString(R.string.EventLogDeletedMessages).replace("un1", UserObject.getUserName(user2));
                                            str9 = "";
                                            chat2 = chat;
                                            sb3 = replace;
                                        }
                                    } else {
                                        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeLinkedChat) {
                                            TLRPC.TL_channelAdminLogEventActionChangeLinkedChat tL_channelAdminLogEventActionChangeLinkedChat = (TLRPC.TL_channelAdminLogEventActionChangeLinkedChat) channelAdminLogEventAction;
                                            long j = tL_channelAdminLogEventActionChangeLinkedChat.new_value;
                                            long j2 = tL_channelAdminLogEventActionChangeLinkedChat.prev_value;
                                            if (chat.megagroup) {
                                                if (j == 0) {
                                                    user3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
                                                    i6 = R.string.EventLogRemovedLinkedChannel;
                                                } else {
                                                    user3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                                                    i6 = R.string.EventLogChangedLinkedChannel;
                                                }
                                            } else if (j == 0) {
                                                user3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
                                                i6 = R.string.EventLogRemovedLinkedGroup;
                                            } else {
                                                user3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                                                i6 = R.string.EventLogChangedLinkedGroup;
                                            }
                                            replaceWithLink3 = replaceWithLink(LocaleController.getString(i6), "un1", user2);
                                            this.messageText = replaceWithLink3;
                                            str11 = "un2";
                                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                            str9 = "";
                                        } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden) {
                                            i3 = ((TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden) channelAdminLogEventAction).new_value ? R.string.EventLogToggledInvitesHistoryOff : R.string.EventLogToggledInvitesHistoryOn;
                                            str9 = "";
                                            message2 = null;
                                            string = LocaleController.getString(i3);
                                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                            str5 = "un1";
                                            replaceWithLink3 = string;
                                            user3 = user2;
                                            str11 = str5;
                                            CharSequence replaceWithLink422 = replaceWithLink(replaceWithLink3, str11, user3);
                                            message = message2;
                                            replace = replaceWithLink422;
                                            chat2 = chat;
                                            sb3 = replace;
                                        } else {
                                            if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeAbout) {
                                                this.messageText = replaceWithLink(LocaleController.getString(chat.megagroup ? R.string.EventLogEditedGroupDescription : R.string.EventLogEditedChannelDescription), "un1", user2);
                                                message3 = new TLRPC.TL_message();
                                                message3.out = false;
                                                message3.unread = false;
                                                TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                                                message3.from_id = tL_peerUser;
                                                tL_channelAdminLogEvent3 = tL_channelAdminLogEvent;
                                                tL_peerUser.user_id = tL_channelAdminLogEvent3.user_id;
                                                message3.peer_id = tL_peerChannel;
                                                message3.date = tL_channelAdminLogEvent3.date;
                                                TLRPC.TL_channelAdminLogEventActionChangeAbout tL_channelAdminLogEventActionChangeAbout = (TLRPC.TL_channelAdminLogEventActionChangeAbout) tL_channelAdminLogEvent3.action;
                                                message3.message = tL_channelAdminLogEventActionChangeAbout.new_value;
                                                if (TextUtils.isEmpty(tL_channelAdminLogEventActionChangeAbout.prev_value)) {
                                                    str9 = "";
                                                    tL_messageMediaEmpty = new TLRPC.TL_messageMediaEmpty();
                                                    message3.media = tL_messageMediaEmpty;
                                                } else {
                                                    TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage = new TLRPC.TL_messageMediaWebPage();
                                                    message3.media = tL_messageMediaWebPage;
                                                    tL_messageMediaWebPage.webpage = new TLRPC.TL_webPage();
                                                    TLRPC.WebPage webPage2 = message3.media.webpage;
                                                    webPage2.flags = 10;
                                                    str9 = "";
                                                    webPage2.display_url = str9;
                                                    webPage2.url = str9;
                                                    webPage2.site_name = LocaleController.getString(R.string.EventLogPreviousGroupDescription);
                                                    webPage = message3.media.webpage;
                                                    str7 = ((TLRPC.TL_channelAdminLogEventActionChangeAbout) tL_channelAdminLogEvent3.action).prev_value;
                                                    webPage.description = str7;
                                                }
                                            } else {
                                                tL_channelAdminLogEvent3 = tL_channelAdminLogEvent;
                                                str9 = "";
                                                if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeTheme) {
                                                    this.messageText = replaceWithLink(LocaleController.getString(chat.megagroup ? R.string.EventLogEditedGroupTheme : R.string.EventLogEditedChannelTheme), "un1", user2);
                                                    message3 = new TLRPC.TL_message();
                                                    message3.out = false;
                                                    message3.unread = false;
                                                    TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
                                                    message3.from_id = tL_peerUser2;
                                                    tL_peerUser2.user_id = tL_channelAdminLogEvent3.user_id;
                                                    message3.peer_id = tL_peerChannel;
                                                    message3.date = tL_channelAdminLogEvent3.date;
                                                    TLRPC.TL_channelAdminLogEventActionChangeTheme tL_channelAdminLogEventActionChangeTheme = (TLRPC.TL_channelAdminLogEventActionChangeTheme) tL_channelAdminLogEvent3.action;
                                                    message3.message = tL_channelAdminLogEventActionChangeTheme.new_value;
                                                    if (TextUtils.isEmpty(tL_channelAdminLogEventActionChangeTheme.prev_value)) {
                                                        tL_messageMediaEmpty = new TLRPC.TL_messageMediaEmpty();
                                                        message3.media = tL_messageMediaEmpty;
                                                    } else {
                                                        TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage2 = new TLRPC.TL_messageMediaWebPage();
                                                        message3.media = tL_messageMediaWebPage2;
                                                        tL_messageMediaWebPage2.webpage = new TLRPC.TL_webPage();
                                                        TLRPC.WebPage webPage3 = message3.media.webpage;
                                                        webPage3.flags = 10;
                                                        webPage3.display_url = str9;
                                                        webPage3.url = str9;
                                                        webPage3.site_name = LocaleController.getString(R.string.EventLogPreviousGroupTheme);
                                                        webPage = message3.media.webpage;
                                                        str7 = ((TLRPC.TL_channelAdminLogEventActionChangeTheme) tL_channelAdminLogEvent3.action).prev_value;
                                                        webPage.description = str7;
                                                    }
                                                } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeUsername) {
                                                    String str13 = ((TLRPC.TL_channelAdminLogEventActionChangeUsername) channelAdminLogEventAction).new_value;
                                                    this.messageText = replaceWithLink(LocaleController.getString(!TextUtils.isEmpty(str13) ? chat.megagroup ? R.string.EventLogChangedGroupLink : R.string.EventLogChangedChannelLink : chat.megagroup ? R.string.EventLogRemovedGroupLink : R.string.EventLogRemovedChannelLink), "un1", user2);
                                                    TLRPC.TL_message tL_message3 = new TLRPC.TL_message();
                                                    tL_message3.out = false;
                                                    tL_message3.unread = false;
                                                    TLRPC.TL_peerUser tL_peerUser3 = new TLRPC.TL_peerUser();
                                                    tL_message3.from_id = tL_peerUser3;
                                                    tL_peerUser3.user_id = tL_channelAdminLogEvent3.user_id;
                                                    tL_message3.peer_id = tL_peerChannel;
                                                    tL_message3.date = tL_channelAdminLogEvent3.date;
                                                    if (TextUtils.isEmpty(str13)) {
                                                        str6 = str2;
                                                        tL_message3.message = str9;
                                                    } else {
                                                        StringBuilder sb6 = new StringBuilder();
                                                        sb6.append("https://");
                                                        sb6.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
                                                        str6 = str2;
                                                        sb6.append(str6);
                                                        sb6.append(str13);
                                                        tL_message3.message = sb6.toString();
                                                    }
                                                    TLRPC.TL_messageEntityUrl tL_messageEntityUrl = new TLRPC.TL_messageEntityUrl();
                                                    tL_messageEntityUrl.offset = 0;
                                                    tL_messageEntityUrl.length = tL_message3.message.length();
                                                    tL_message3.entities.add(tL_messageEntityUrl);
                                                    if (TextUtils.isEmpty(((TLRPC.TL_channelAdminLogEventActionChangeUsername) tL_channelAdminLogEvent3.action).prev_value)) {
                                                        tL_message3.media = new TLRPC.TL_messageMediaEmpty();
                                                    } else {
                                                        TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage3 = new TLRPC.TL_messageMediaWebPage();
                                                        tL_message3.media = tL_messageMediaWebPage3;
                                                        tL_messageMediaWebPage3.webpage = new TLRPC.TL_webPage();
                                                        TLRPC.WebPage webPage4 = tL_message3.media.webpage;
                                                        webPage4.flags = 10;
                                                        webPage4.display_url = str9;
                                                        webPage4.url = str9;
                                                        webPage4.site_name = LocaleController.getString(R.string.EventLogPreviousLink);
                                                        tL_message3.media.webpage.description = "https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + str6 + ((TLRPC.TL_channelAdminLogEventActionChangeUsername) tL_channelAdminLogEvent3.action).prev_value;
                                                    }
                                                    message3 = tL_message3;
                                                } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionEditMessage) {
                                                    message3 = new TLRPC.TL_message();
                                                    message3.out = false;
                                                    message3.unread = false;
                                                    message3.peer_id = tL_peerChannel;
                                                    message3.date = tL_channelAdminLogEvent3.date;
                                                    TLRPC.TL_channelAdminLogEventActionEditMessage tL_channelAdminLogEventActionEditMessage = (TLRPC.TL_channelAdminLogEventActionEditMessage) tL_channelAdminLogEvent3.action;
                                                    TLRPC.Message message7 = tL_channelAdminLogEventActionEditMessage.new_message;
                                                    TLRPC.Message message8 = tL_channelAdminLogEventActionEditMessage.prev_message;
                                                    if (message8 == null) {
                                                        if (message7 != null) {
                                                            message3.reply_to = message7.reply_to;
                                                            i5 = message7.id;
                                                        }
                                                        if (message7 != null || (peer = message7.from_id) == null) {
                                                            TLRPC.TL_peerUser tL_peerUser4 = new TLRPC.TL_peerUser();
                                                            message3.from_id = tL_peerUser4;
                                                            tL_peerUser4.user_id = tL_channelAdminLogEvent3.user_id;
                                                        } else {
                                                            message3.from_id = peer;
                                                        }
                                                        if (getMedia(message7) != null || (getMedia(message7) instanceof TLRPC.TL_messageMediaEmpty) || (getMedia(message7) instanceof TLRPC.TL_messageMediaWebPage)) {
                                                            this.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedMessages), "un1", user2);
                                                            if (message7.action instanceof TLRPC.TL_messageActionGroupCall) {
                                                                message3.message = message7.message;
                                                                message3.entities = message7.entities;
                                                                TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage4 = new TLRPC.TL_messageMediaWebPage();
                                                                message3.media = tL_messageMediaWebPage4;
                                                                tL_messageMediaWebPage4.webpage = new TLRPC.TL_webPage();
                                                                message3.media.webpage.site_name = LocaleController.getString(R.string.EventLogOriginalMessages);
                                                            } else {
                                                                message7.media = new TLRPC.TL_messageMediaEmpty();
                                                                message3 = message7;
                                                                arrayList2 = null;
                                                            }
                                                        } else {
                                                            boolean z12 = !TextUtils.equals(message7.message, message8.message);
                                                            TLRPC.MessageMedia media = getMedia(message7);
                                                            TLRPC.MessageMedia media2 = getMedia(message8);
                                                            if (media2 == null) {
                                                                z4 = true;
                                                            } else if (media.getClass() == media2.getClass() && (((photo = media.photo) == null || (photo2 = media2.photo) == null || photo.id == photo2.id) && (media.document == null || media2.document == null || getMedia(message7).document.id == media2.document.id))) {
                                                                z4 = false;
                                                            } else {
                                                                z4 = false;
                                                                z5 = true;
                                                                this.messageText = replaceWithLink(LocaleController.getString(!z4 ? R.string.EventLogAddedMedia : (z5 && z12) ? R.string.EventLogEditedMediaCaption : z12 ? R.string.EventLogEditedCaption : R.string.EventLogEditedMedia), "un1", user2);
                                                                TLRPC.MessageMedia media3 = getMedia(message7);
                                                                message3.media = media3;
                                                                if (z12) {
                                                                    media3.webpage = new TLRPC.TL_webPage();
                                                                    message3.media.webpage.site_name = LocaleController.getString(R.string.EventLogOriginalCaption);
                                                                }
                                                                arrayList2 = null;
                                                            }
                                                            z5 = false;
                                                            this.messageText = replaceWithLink(LocaleController.getString(!z4 ? R.string.EventLogAddedMedia : (z5 && z12) ? R.string.EventLogEditedMediaCaption : z12 ? R.string.EventLogEditedCaption : R.string.EventLogEditedMedia), "un1", user2);
                                                            TLRPC.MessageMedia media32 = getMedia(message7);
                                                            message3.media = media32;
                                                            if (z12) {
                                                            }
                                                            arrayList2 = null;
                                                        }
                                                        message3.reply_markup = message7.reply_markup;
                                                        TLRPC.WebPage webPage5 = message3.media.webpage;
                                                        if (webPage5 != null) {
                                                            webPage5.flags = 10;
                                                            webPage5.display_url = str9;
                                                            webPage5.url = str9;
                                                        }
                                                        tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                        arrayList3 = arrayList2;
                                                        chat2 = chat;
                                                        if (this.messageOwner == null) {
                                                        }
                                                        this.messageOwner.message = this.messageText.toString();
                                                        this.messageOwner.from_id = new TLRPC.TL_peerUser();
                                                        TLRPC.Message message42 = this.messageOwner;
                                                        message42.from_id.user_id = tL_channelAdminLogEvent2.user_id;
                                                        message42.date = tL_channelAdminLogEvent2.date;
                                                        int i152 = iArr[0];
                                                        iArr[0] = i152 + 1;
                                                        message42.id = i152;
                                                        this.eventId = tL_channelAdminLogEvent2.id;
                                                        message42.out = false;
                                                        message42.peer_id = new TLRPC.TL_peerChannel();
                                                        TLRPC.Message message52 = this.messageOwner;
                                                        message52.peer_id.channel_id = chat2.id;
                                                        message52.unread = false;
                                                        mediaController = MediaController.getInstance();
                                                        this.isOutOwnerCached = null;
                                                        if (message3 instanceof TLRPC.TL_messageEmpty) {
                                                        }
                                                        if (message3 == null) {
                                                        }
                                                        if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                                                            return;
                                                        }
                                                        createDateArray(this.currentAccount, tL_channelAdminLogEvent, arrayList, hashMap, z);
                                                        if (z) {
                                                        }
                                                        if (this.messageText == null) {
                                                        }
                                                        if (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame)) {
                                                        }
                                                        if (!allowsBigEmoji()) {
                                                        }
                                                        CharSequence replaceEmoji2 = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), (boolean) r12, iArr3);
                                                        this.messageText = replaceEmoji2;
                                                        replaceAnimatedEmoji = replaceAnimatedEmoji(replaceEmoji2, textPaint.getFontMetricsInt());
                                                        this.messageText = replaceAnimatedEmoji;
                                                        if (iArr3 != null) {
                                                            replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr3);
                                                        }
                                                        checkEmojiOnly(iArr3);
                                                        setType();
                                                        measureInlineBotButtons();
                                                        generateCaption();
                                                        if (mediaController.isPlayingMessage(this)) {
                                                        }
                                                        generateLayout(user2);
                                                        this.layoutCreated = i10;
                                                        generateThumbs(r12);
                                                        checkMediaExistance();
                                                        return;
                                                    }
                                                    message3.reply_to = message8.reply_to;
                                                    i5 = message8.id;
                                                    message3.id = i5;
                                                    if (message7 != null) {
                                                    }
                                                    TLRPC.TL_peerUser tL_peerUser42 = new TLRPC.TL_peerUser();
                                                    message3.from_id = tL_peerUser42;
                                                    tL_peerUser42.user_id = tL_channelAdminLogEvent3.user_id;
                                                    if (getMedia(message7) != null) {
                                                    }
                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedMessages), "un1", user2);
                                                    if (message7.action instanceof TLRPC.TL_messageActionGroupCall) {
                                                    }
                                                } else {
                                                    if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeEmojiStickerSet) {
                                                        TLRPC.InputStickerSet inputStickerSet = ((TLRPC.TL_channelAdminLogEventActionChangeEmojiStickerSet) channelAdminLogEventAction).new_stickerset;
                                                        i3 = (inputStickerSet == null || (inputStickerSet instanceof TLRPC.TL_inputStickerSetEmpty)) ? R.string.EventLogRemovedEmojiPack : R.string.EventLogChangedEmojiPack;
                                                    } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeStickerSet) {
                                                        TLRPC.InputStickerSet inputStickerSet2 = ((TLRPC.TL_channelAdminLogEventActionChangeStickerSet) channelAdminLogEventAction).new_stickerset;
                                                        i3 = (inputStickerSet2 == null || (inputStickerSet2 instanceof TLRPC.TL_inputStickerSetEmpty)) ? R.string.EventLogRemovedStickersSet : R.string.EventLogChangedStickersSet;
                                                    } else {
                                                        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeLocation) {
                                                            TLRPC.ChannelLocation channelLocation = ((TLRPC.TL_channelAdminLogEventActionChangeLocation) channelAdminLogEventAction).new_value;
                                                            if (channelLocation instanceof TLRPC.TL_channelLocationEmpty) {
                                                                i3 = R.string.EventLogRemovedLocation;
                                                            } else {
                                                                string = LocaleController.formatString("EventLogChangedLocation", R.string.EventLogChangedLocation, ((TLRPC.TL_channelLocation) channelLocation).address);
                                                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                str5 = "un1";
                                                                message2 = null;
                                                                replaceWithLink3 = string;
                                                                user3 = user2;
                                                                str11 = str5;
                                                            }
                                                        } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionToggleSlowMode) {
                                                            int i25 = ((TLRPC.TL_channelAdminLogEventActionToggleSlowMode) channelAdminLogEventAction).new_value;
                                                            if (i25 == 0) {
                                                                i3 = R.string.EventLogToggledSlowmodeOff;
                                                            } else {
                                                                string = LocaleController.formatString("EventLogToggledSlowmodeOn", R.string.EventLogToggledSlowmodeOn, i25 < 60 ? LocaleController.formatPluralString("Seconds", i25, new Object[0]) : i25 < 3600 ? LocaleController.formatPluralString("Minutes", i25 / 60, new Object[0]) : LocaleController.formatPluralString("Hours", (i25 / 60) / 60, new Object[0]));
                                                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                str5 = "un1";
                                                                message2 = null;
                                                                replaceWithLink3 = string;
                                                                user3 = user2;
                                                                str11 = str5;
                                                            }
                                                        } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionStartGroupCall) {
                                                            i3 = (!ChatObject.isChannel(chat) || (chat.megagroup && !chat.gigagroup)) ? R.string.EventLogStartedVoiceChat : R.string.EventLogStartedLiveStream;
                                                        } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionDiscardGroupCall) {
                                                            i3 = (!ChatObject.isChannel(chat) || (chat.megagroup && !chat.gigagroup)) ? R.string.EventLogEndedVoiceChat : R.string.EventLogEndedLiveStream;
                                                        } else {
                                                            if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantMute) {
                                                                long peerId3 = getPeerId(((TLRPC.TL_channelAdminLogEventActionParticipantMute) channelAdminLogEventAction).participant.peer);
                                                                user3 = peerId3 > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId3)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId3));
                                                                i4 = R.string.EventLogVoiceChatMuted;
                                                            } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantUnmute) {
                                                                long peerId4 = getPeerId(((TLRPC.TL_channelAdminLogEventActionParticipantUnmute) channelAdminLogEventAction).participant.peer);
                                                                user3 = peerId4 > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId4)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId4));
                                                                i4 = R.string.EventLogVoiceChatUnmuted;
                                                            } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionToggleGroupCallSetting) {
                                                                i3 = ((TLRPC.TL_channelAdminLogEventActionToggleGroupCallSetting) channelAdminLogEventAction).join_muted ? R.string.EventLogVoiceChatNotAllowedToSpeak : R.string.EventLogVoiceChatAllowedToSpeak;
                                                            } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantJoinByInvite) {
                                                                TLRPC.TL_channelAdminLogEventActionParticipantJoinByInvite tL_channelAdminLogEventActionParticipantJoinByInvite = (TLRPC.TL_channelAdminLogEventActionParticipantJoinByInvite) channelAdminLogEventAction;
                                                                this.messageText = replaceWithLink(LocaleController.getString(tL_channelAdminLogEventActionParticipantJoinByInvite.via_chatlist ? ChatObject.isChannelAndNotMegaGroup(chat) ? R.string.ActionInviteChannelUserFolder : R.string.ActionInviteUserFolder : ChatObject.isChannelAndNotMegaGroup(chat) ? R.string.ActionInviteChannelUser : R.string.ActionInviteUser), "un1", user2);
                                                                TLRPC.TL_chatInviteExported tL_chatInviteExported = tL_channelAdminLogEventActionParticipantJoinByInvite.invite;
                                                                if (tL_chatInviteExported != null && !TextUtils.isEmpty(tL_chatInviteExported.link)) {
                                                                    charSequence6 = TextUtils.concat(this.messageText, " ", tL_channelAdminLogEventActionParticipantJoinByInvite.invite.link);
                                                                    tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                    charSequence5 = charSequence6;
                                                                    chat2 = chat;
                                                                    charSequence = charSequence5;
                                                                    message = null;
                                                                    sb3 = charSequence;
                                                                }
                                                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                chat2 = chat;
                                                                message3 = null;
                                                                arrayList3 = null;
                                                                if (this.messageOwner == null) {
                                                                }
                                                                this.messageOwner.message = this.messageText.toString();
                                                                this.messageOwner.from_id = new TLRPC.TL_peerUser();
                                                                TLRPC.Message message422 = this.messageOwner;
                                                                message422.from_id.user_id = tL_channelAdminLogEvent2.user_id;
                                                                message422.date = tL_channelAdminLogEvent2.date;
                                                                int i1522 = iArr[0];
                                                                iArr[0] = i1522 + 1;
                                                                message422.id = i1522;
                                                                this.eventId = tL_channelAdminLogEvent2.id;
                                                                message422.out = false;
                                                                message422.peer_id = new TLRPC.TL_peerChannel();
                                                                TLRPC.Message message522 = this.messageOwner;
                                                                message522.peer_id.channel_id = chat2.id;
                                                                message522.unread = false;
                                                                mediaController = MediaController.getInstance();
                                                                this.isOutOwnerCached = null;
                                                                if (message3 instanceof TLRPC.TL_messageEmpty) {
                                                                }
                                                                if (message3 == null) {
                                                                }
                                                                if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                                                                }
                                                            } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionToggleNoForwards) {
                                                                TLRPC.TL_channelAdminLogEventActionToggleNoForwards tL_channelAdminLogEventActionToggleNoForwards = (TLRPC.TL_channelAdminLogEventActionToggleNoForwards) channelAdminLogEventAction;
                                                                boolean z13 = ChatObject.isChannel(chat) && !chat.megagroup;
                                                                i3 = tL_channelAdminLogEventActionToggleNoForwards.new_value ? z13 ? R.string.ActionForwardsRestrictedChannel : R.string.ActionForwardsRestrictedGroup : z13 ? R.string.ActionForwardsEnabledChannel : R.string.ActionForwardsEnabledGroup;
                                                            } else {
                                                                if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionExportedInviteDelete) {
                                                                    replaceWithLink = replaceWithLink(LocaleController.formatString("ActionDeletedInviteLinkClickable", R.string.ActionDeletedInviteLinkClickable, new Object[0]), "un1", user2);
                                                                    this.messageText = replaceWithLink;
                                                                    tLObject = ((TLRPC.TL_channelAdminLogEventActionExportedInviteDelete) channelAdminLogEventAction).invite;
                                                                    str3 = "un2";
                                                                } else {
                                                                    str3 = "un2";
                                                                    if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionExportedInviteRevoke) {
                                                                        TLRPC.TL_channelAdminLogEventActionExportedInviteRevoke tL_channelAdminLogEventActionExportedInviteRevoke = (TLRPC.TL_channelAdminLogEventActionExportedInviteRevoke) channelAdminLogEventAction;
                                                                        replaceWithLink = replaceWithLink(LocaleController.formatString("ActionRevokedInviteLinkClickable", R.string.ActionRevokedInviteLinkClickable, tL_channelAdminLogEventActionExportedInviteRevoke.invite.link), "un1", user2);
                                                                        this.messageText = replaceWithLink;
                                                                        tLObject = tL_channelAdminLogEventActionExportedInviteRevoke.invite;
                                                                    } else {
                                                                        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionExportedInviteEdit) {
                                                                            TLRPC.TL_channelAdminLogEventActionExportedInviteEdit tL_channelAdminLogEventActionExportedInviteEdit = (TLRPC.TL_channelAdminLogEventActionExportedInviteEdit) channelAdminLogEventAction;
                                                                            String str14 = tL_channelAdminLogEventActionExportedInviteEdit.prev_invite.link;
                                                                            this.messageText = replaceWithLink((str14 == null || !str14.equals(tL_channelAdminLogEventActionExportedInviteEdit.new_invite.link)) ? LocaleController.formatString("ActionEditedInviteLinkClickable", R.string.ActionEditedInviteLinkClickable, new Object[0]) : LocaleController.formatString("ActionEditedInviteLinkToSameClickable", R.string.ActionEditedInviteLinkToSameClickable, new Object[0]), "un1", user2);
                                                                            replaceWithLink2 = replaceWithLink(this.messageText, str3, tL_channelAdminLogEventActionExportedInviteEdit.prev_invite);
                                                                            this.messageText = replaceWithLink2;
                                                                            tLObject2 = tL_channelAdminLogEventActionExportedInviteEdit.new_invite;
                                                                            str4 = "un3";
                                                                        } else {
                                                                            str4 = "un3";
                                                                            if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantVolume) {
                                                                                TLRPC.TL_channelAdminLogEventActionParticipantVolume tL_channelAdminLogEventActionParticipantVolume = (TLRPC.TL_channelAdminLogEventActionParticipantVolume) channelAdminLogEventAction;
                                                                                long peerId5 = getPeerId(tL_channelAdminLogEventActionParticipantVolume.participant.peer);
                                                                                user3 = peerId5 > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId5)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId5));
                                                                                double participantVolume = ChatObject.getParticipantVolume(tL_channelAdminLogEventActionParticipantVolume.participant);
                                                                                Double.isNaN(participantVolume);
                                                                                double d = participantVolume / 100.0d;
                                                                                replaceWithLink3 = replaceWithLink(LocaleController.formatString("ActionVolumeChanged", R.string.ActionVolumeChanged, Integer.valueOf((int) (d > 0.0d ? Math.max(d, 1.0d) : 0.0d))), "un1", user2);
                                                                                this.messageText = replaceWithLink3;
                                                                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                str11 = str3;
                                                                            } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeHistoryTTL) {
                                                                                TLRPC.TL_channelAdminLogEventActionChangeHistoryTTL tL_channelAdminLogEventActionChangeHistoryTTL = (TLRPC.TL_channelAdminLogEventActionChangeHistoryTTL) channelAdminLogEventAction;
                                                                                if (chat.megagroup) {
                                                                                    int i26 = tL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                                                    if (i26 == 0) {
                                                                                        i3 = R.string.ActionTTLDisabled;
                                                                                    } else {
                                                                                        if (i26 > 86400) {
                                                                                            c2 = 0;
                                                                                            formatPluralString = LocaleController.formatPluralString("Days", i26 / 86400, new Object[0]);
                                                                                        } else {
                                                                                            c2 = 0;
                                                                                            formatPluralString = i26 >= 3600 ? LocaleController.formatPluralString("Hours", i26 / 3600, new Object[0]) : i26 >= 60 ? LocaleController.formatPluralString("Minutes", i26 / 60, new Object[0]) : LocaleController.formatPluralString("Seconds", i26, new Object[0]);
                                                                                        }
                                                                                        int i27 = R.string.ActionTTLChanged;
                                                                                        Object[] objArr = new Object[1];
                                                                                        objArr[c2] = formatPluralString;
                                                                                        string = LocaleController.formatString("ActionTTLChanged", i27, objArr);
                                                                                        tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                        str5 = "un1";
                                                                                        message2 = null;
                                                                                        replaceWithLink3 = string;
                                                                                        user3 = user2;
                                                                                        str11 = str5;
                                                                                    }
                                                                                } else {
                                                                                    int i28 = tL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                                                    charSequence6 = i28 != 0 ? LocaleController.formatString("ActionTTLChannelChanged", R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i28)) : LocaleController.getString(R.string.ActionTTLChannelDisabled);
                                                                                    tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                    charSequence5 = charSequence6;
                                                                                    chat2 = chat;
                                                                                    charSequence = charSequence5;
                                                                                    message = null;
                                                                                    sb3 = charSequence;
                                                                                }
                                                                            } else {
                                                                                if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionParticipantJoinByRequest) {
                                                                                    TLRPC.TL_channelAdminLogEventActionParticipantJoinByRequest tL_channelAdminLogEventActionParticipantJoinByRequest = (TLRPC.TL_channelAdminLogEventActionParticipantJoinByRequest) channelAdminLogEventAction;
                                                                                    TLRPC.ExportedChatInvite exportedChatInvite = tL_channelAdminLogEventActionParticipantJoinByRequest.invite;
                                                                                    if (((exportedChatInvite instanceof TLRPC.TL_chatInviteExported) && "https://t.me/+PublicChat".equals(((TLRPC.TL_chatInviteExported) exportedChatInvite).link)) || (tL_channelAdminLogEventActionParticipantJoinByRequest.invite instanceof TLRPC.TL_chatInvitePublicJoinRequests)) {
                                                                                        replaceWithLink = replaceWithLink(LocaleController.getString(R.string.JoinedViaRequestApproved), "un1", user2);
                                                                                        this.messageText = replaceWithLink;
                                                                                        tLObject = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tL_channelAdminLogEventActionParticipantJoinByRequest.approved_by));
                                                                                    } else {
                                                                                        CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString(R.string.JoinedViaInviteLinkApproved), "un1", user2);
                                                                                        this.messageText = replaceWithLink5;
                                                                                        replaceWithLink2 = replaceWithLink(replaceWithLink5, str3, tL_channelAdminLogEventActionParticipantJoinByRequest.invite);
                                                                                        this.messageText = replaceWithLink2;
                                                                                        tLObject2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tL_channelAdminLogEventActionParticipantJoinByRequest.approved_by));
                                                                                    }
                                                                                } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionSendMessage) {
                                                                                    message2 = ((TLRPC.TL_channelAdminLogEventActionSendMessage) channelAdminLogEventAction).message;
                                                                                    i3 = R.string.EventLogSendMessages;
                                                                                    string = LocaleController.getString(i3);
                                                                                    tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                    str5 = "un1";
                                                                                    replaceWithLink3 = string;
                                                                                    user3 = user2;
                                                                                    str11 = str5;
                                                                                } else {
                                                                                    if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeAvailableReactions) {
                                                                                        TLRPC.TL_channelAdminLogEventActionChangeAvailableReactions tL_channelAdminLogEventActionChangeAvailableReactions = (TLRPC.TL_channelAdminLogEventActionChangeAvailableReactions) channelAdminLogEventAction;
                                                                                        boolean z14 = (tL_channelAdminLogEventActionChangeAvailableReactions.prev_value instanceof TLRPC.TL_chatReactionsSome) && (tL_channelAdminLogEventActionChangeAvailableReactions.new_value instanceof TLRPC.TL_chatReactionsSome);
                                                                                        CharSequence stringFrom = getStringFrom(tL_channelAdminLogEventActionChangeAvailableReactions.new_value);
                                                                                        if (z14) {
                                                                                            SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder(replaceWithLink(LocaleController.formatString("ActionReactionsChangedList", R.string.ActionReactionsChangedList, "**new**"), "un1", user2));
                                                                                            int indexOf = spannableStringBuilder6.toString().indexOf("**new**");
                                                                                            charSequence4 = spannableStringBuilder6;
                                                                                            if (indexOf > 0) {
                                                                                                spannableStringBuilder6.replace(indexOf, indexOf + 7, stringFrom);
                                                                                                charSequence4 = spannableStringBuilder6;
                                                                                            }
                                                                                        } else {
                                                                                            CharSequence stringFrom2 = getStringFrom(tL_channelAdminLogEventActionChangeAvailableReactions.prev_value);
                                                                                            SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder(replaceWithLink(LocaleController.formatString("ActionReactionsChanged", R.string.ActionReactionsChanged, "**old**", "**new**"), "un1", user2));
                                                                                            int indexOf2 = spannableStringBuilder7.toString().indexOf("**old**");
                                                                                            if (indexOf2 > 0) {
                                                                                                spannableStringBuilder7.replace(indexOf2, indexOf2 + 7, stringFrom2);
                                                                                            }
                                                                                            int indexOf3 = spannableStringBuilder7.toString().indexOf("**new**");
                                                                                            if (indexOf3 > 0) {
                                                                                                spannableStringBuilder7.replace(indexOf3, indexOf3 + 7, stringFrom);
                                                                                            }
                                                                                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                            charSequence5 = spannableStringBuilder7;
                                                                                            chat2 = chat;
                                                                                            charSequence = charSequence5;
                                                                                            message = null;
                                                                                            sb3 = charSequence;
                                                                                        }
                                                                                    } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeUsernames) {
                                                                                        TLRPC.TL_channelAdminLogEventActionChangeUsernames tL_channelAdminLogEventActionChangeUsernames = (TLRPC.TL_channelAdminLogEventActionChangeUsernames) channelAdminLogEventAction;
                                                                                        ArrayList<String> arrayList5 = tL_channelAdminLogEventActionChangeUsernames.prev_value;
                                                                                        ArrayList<String> arrayList6 = tL_channelAdminLogEventActionChangeUsernames.new_value;
                                                                                        this.messageText = null;
                                                                                        if (arrayList5 != null && arrayList6 != null) {
                                                                                            if (arrayList6.size() + 1 == arrayList5.size()) {
                                                                                                int i29 = 0;
                                                                                                String str15 = null;
                                                                                                while (true) {
                                                                                                    if (i29 >= arrayList5.size()) {
                                                                                                        break;
                                                                                                    }
                                                                                                    String str16 = arrayList5.get(i29);
                                                                                                    if (!arrayList6.contains(str16)) {
                                                                                                        if (str15 != null) {
                                                                                                            str15 = null;
                                                                                                            break;
                                                                                                        }
                                                                                                        str15 = str16;
                                                                                                    }
                                                                                                    i29++;
                                                                                                }
                                                                                                if (str15 != null) {
                                                                                                    formatString = LocaleController.formatString("EventLogDeactivatedUsername", R.string.EventLogDeactivatedUsername, "@" + str15);
                                                                                                    this.messageText = replaceWithLink(formatString, "un1", user2);
                                                                                                }
                                                                                            } else if (arrayList5.size() + 1 == arrayList6.size()) {
                                                                                                int i30 = 0;
                                                                                                String str17 = null;
                                                                                                while (true) {
                                                                                                    if (i30 >= arrayList6.size()) {
                                                                                                        break;
                                                                                                    }
                                                                                                    String str18 = arrayList6.get(i30);
                                                                                                    if (!arrayList5.contains(str18)) {
                                                                                                        if (str17 != null) {
                                                                                                            str17 = null;
                                                                                                            break;
                                                                                                        }
                                                                                                        str17 = str18;
                                                                                                    }
                                                                                                    i30++;
                                                                                                }
                                                                                                if (str17 != null) {
                                                                                                    formatString = LocaleController.formatString("EventLogActivatedUsername", R.string.EventLogActivatedUsername, "@" + str17);
                                                                                                    this.messageText = replaceWithLink(formatString, "un1", user2);
                                                                                                }
                                                                                            }
                                                                                            replaceWithLink3 = string;
                                                                                            user3 = user2;
                                                                                            str11 = str5;
                                                                                        }
                                                                                        if (this.messageText == null) {
                                                                                            string = LocaleController.formatString("EventLogChangeUsernames", R.string.EventLogChangeUsernames, getUsernamesString(arrayList5), getUsernamesString(arrayList6));
                                                                                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                            str5 = "un1";
                                                                                            message2 = null;
                                                                                            replaceWithLink3 = string;
                                                                                            user3 = user2;
                                                                                            str11 = str5;
                                                                                        }
                                                                                        tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                        chat2 = chat;
                                                                                        message3 = null;
                                                                                        arrayList3 = null;
                                                                                        if (this.messageOwner == null) {
                                                                                        }
                                                                                        this.messageOwner.message = this.messageText.toString();
                                                                                        this.messageOwner.from_id = new TLRPC.TL_peerUser();
                                                                                        TLRPC.Message message4222 = this.messageOwner;
                                                                                        message4222.from_id.user_id = tL_channelAdminLogEvent2.user_id;
                                                                                        message4222.date = tL_channelAdminLogEvent2.date;
                                                                                        int i15222 = iArr[0];
                                                                                        iArr[0] = i15222 + 1;
                                                                                        message4222.id = i15222;
                                                                                        this.eventId = tL_channelAdminLogEvent2.id;
                                                                                        message4222.out = false;
                                                                                        message4222.peer_id = new TLRPC.TL_peerChannel();
                                                                                        TLRPC.Message message5222 = this.messageOwner;
                                                                                        message5222.peer_id.channel_id = chat2.id;
                                                                                        message5222.unread = false;
                                                                                        mediaController = MediaController.getInstance();
                                                                                        this.isOutOwnerCached = null;
                                                                                        if (message3 instanceof TLRPC.TL_messageEmpty) {
                                                                                        }
                                                                                        if (message3 == null) {
                                                                                        }
                                                                                        if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                                                                                        }
                                                                                    } else {
                                                                                        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionToggleForum) {
                                                                                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                            string = ((TLRPC.TL_channelAdminLogEventActionToggleForum) channelAdminLogEventAction).new_value ? LocaleController.formatString("EventLogSwitchToForum", R.string.EventLogSwitchToForum, new Object[0]) : LocaleController.formatString("EventLogSwitchToGroup", R.string.EventLogSwitchToGroup, new Object[0]);
                                                                                        } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionCreateTopic) {
                                                                                            replaceWithLink = replaceWithLink(LocaleController.formatString("EventLogCreateTopic", R.string.EventLogCreateTopic, new Object[0]), "un1", user2);
                                                                                            this.messageText = replaceWithLink;
                                                                                            tLObject = ((TLRPC.TL_channelAdminLogEventActionCreateTopic) channelAdminLogEventAction).topic;
                                                                                        } else {
                                                                                            if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionEditTopic) {
                                                                                                TLRPC.TL_channelAdminLogEventActionEditTopic tL_channelAdminLogEventActionEditTopic = (TLRPC.TL_channelAdminLogEventActionEditTopic) channelAdminLogEventAction;
                                                                                                TLRPC.ForumTopic forumTopic = tL_channelAdminLogEventActionEditTopic.prev_topic;
                                                                                                boolean z15 = forumTopic instanceof TLRPC.TL_forumTopic;
                                                                                                if (z15) {
                                                                                                    TLRPC.ForumTopic forumTopic2 = tL_channelAdminLogEventActionEditTopic.new_topic;
                                                                                                    if (forumTopic2 instanceof TLRPC.TL_forumTopic) {
                                                                                                        boolean z16 = ((TLRPC.TL_forumTopic) forumTopic).hidden;
                                                                                                        boolean z17 = ((TLRPC.TL_forumTopic) forumTopic2).hidden;
                                                                                                        if (z16 != z17) {
                                                                                                            charSequence6 = replaceWithLink(LocaleController.getString(z17 ? R.string.TopicHidden2 : R.string.TopicShown2), "%s", user2);
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                if (z15) {
                                                                                                    TLRPC.ForumTopic forumTopic3 = tL_channelAdminLogEventActionEditTopic.new_topic;
                                                                                                    if (forumTopic3 instanceof TLRPC.TL_forumTopic) {
                                                                                                        boolean z18 = ((TLRPC.TL_forumTopic) forumTopic).closed;
                                                                                                        boolean z19 = ((TLRPC.TL_forumTopic) forumTopic3).closed;
                                                                                                        if (z18 != z19) {
                                                                                                            this.messageText = replaceWithLink(LocaleController.getString(z19 ? R.string.EventLogClosedTopic : R.string.EventLogReopenedTopic), "%s", user2);
                                                                                                            replaceWithLink = this.messageText;
                                                                                                            tLObject = tL_channelAdminLogEventActionEditTopic.new_topic;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString(R.string.EventLogEditTopic), "un1", user2);
                                                                                                this.messageText = replaceWithLink6;
                                                                                                replaceWithLink2 = replaceWithLink(replaceWithLink6, str3, tL_channelAdminLogEventActionEditTopic.prev_topic);
                                                                                                this.messageText = replaceWithLink2;
                                                                                                tLObject2 = tL_channelAdminLogEventActionEditTopic.new_topic;
                                                                                            } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionDeleteTopic) {
                                                                                                replaceWithLink = replaceWithLink(LocaleController.getString(R.string.EventLogDeleteTopic), "un1", user2);
                                                                                                this.messageText = replaceWithLink;
                                                                                                tLObject = ((TLRPC.TL_channelAdminLogEventActionDeleteTopic) channelAdminLogEventAction).topic;
                                                                                            } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionPinTopic) {
                                                                                                TLRPC.TL_channelAdminLogEventActionPinTopic tL_channelAdminLogEventActionPinTopic = (TLRPC.TL_channelAdminLogEventActionPinTopic) channelAdminLogEventAction;
                                                                                                TLRPC.ForumTopic forumTopic4 = tL_channelAdminLogEventActionPinTopic.new_topic;
                                                                                                if ((forumTopic4 instanceof TLRPC.TL_forumTopic) && ((TLRPC.TL_forumTopic) forumTopic4).pinned) {
                                                                                                    CharSequence replaceWithLink7 = replaceWithLink(LocaleController.formatString("EventLogPinTopic", R.string.EventLogPinTopic, new Object[0]), "un1", user2);
                                                                                                    this.messageText = replaceWithLink7;
                                                                                                    charSequence4 = replaceWithLink(replaceWithLink7, str3, tL_channelAdminLogEventActionPinTopic.new_topic);
                                                                                                } else {
                                                                                                    replaceWithLink = replaceWithLink(LocaleController.formatString("EventLogUnpinTopic", R.string.EventLogUnpinTopic, new Object[0]), "un1", user2);
                                                                                                    this.messageText = replaceWithLink;
                                                                                                    tLObject = tL_channelAdminLogEventActionPinTopic.new_topic;
                                                                                                }
                                                                                            } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionToggleAntiSpam) {
                                                                                                charSequence6 = replaceWithLink(LocaleController.getString(((TLRPC.TL_channelAdminLogEventActionToggleAntiSpam) channelAdminLogEventAction).new_value ? R.string.EventLogEnabledAntiSpam : R.string.EventLogDisabledAntiSpam), "un1", user2);
                                                                                            } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeColor) {
                                                                                                boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(chat);
                                                                                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                                TLRPC.TL_channelAdminLogEventActionChangeColor tL_channelAdminLogEventActionChangeColor = (TLRPC.TL_channelAdminLogEventActionChangeColor) tL_channelAdminLogEvent2.action;
                                                                                                string = LocaleController.formatString(isChannelAndNotMegaGroup ? R.string.EventLogChangedColor : R.string.EventLogChangedColorGroup, AvatarDrawable.colorName(tL_channelAdminLogEventActionChangeColor.prev_value).toLowerCase(), AvatarDrawable.colorName(tL_channelAdminLogEventActionChangeColor.new_value).toLowerCase());
                                                                                            } else {
                                                                                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                                if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangePeerColor) {
                                                                                                    boolean isChannelAndNotMegaGroup2 = ChatObject.isChannelAndNotMegaGroup(chat);
                                                                                                    TLRPC.TL_channelAdminLogEventActionChangePeerColor tL_channelAdminLogEventActionChangePeerColor = (TLRPC.TL_channelAdminLogEventActionChangePeerColor) tL_channelAdminLogEvent2.action;
                                                                                                    SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder(LocaleController.getString(isChannelAndNotMegaGroup2 ? R.string.EventLogChangedPeerColorIcon : R.string.EventLogChangedPeerColorIconGroup));
                                                                                                    spannableStringBuilder = new SpannableStringBuilder();
                                                                                                    if ((tL_channelAdminLogEventActionChangePeerColor.prev_value.flags & 1) != 0) {
                                                                                                        spannableStringBuilder.append((CharSequence) "c");
                                                                                                        spannableStringBuilder5 = spannableStringBuilder8;
                                                                                                        spannableStringBuilder.setSpan(new PeerColorActivity.PeerColorSpan(false, this.currentAccount, tL_channelAdminLogEventActionChangePeerColor.prev_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
                                                                                                    } else {
                                                                                                        spannableStringBuilder5 = spannableStringBuilder8;
                                                                                                    }
                                                                                                    if ((tL_channelAdminLogEventActionChangePeerColor.prev_value.flags & 2) != 0) {
                                                                                                        if (spannableStringBuilder.length() > 0) {
                                                                                                            spannableStringBuilder.append((CharSequence) ", ");
                                                                                                        }
                                                                                                        spannableStringBuilder.append((CharSequence) "e");
                                                                                                        spannableStringBuilder.setSpan(new AnimatedEmojiSpan(tL_channelAdminLogEventActionChangePeerColor.prev_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
                                                                                                    }
                                                                                                    if (spannableStringBuilder.length() == 0) {
                                                                                                        spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                    }
                                                                                                    spannableStringBuilder3 = new SpannableStringBuilder();
                                                                                                    if ((tL_channelAdminLogEventActionChangePeerColor.new_value.flags & 1) != 0) {
                                                                                                        spannableStringBuilder3.append((CharSequence) "c");
                                                                                                        spannableStringBuilder3.setSpan(new PeerColorActivity.PeerColorSpan(false, this.currentAccount, tL_channelAdminLogEventActionChangePeerColor.new_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder3.length() - 1, spannableStringBuilder3.length(), 33);
                                                                                                    }
                                                                                                    if ((tL_channelAdminLogEventActionChangePeerColor.new_value.flags & 2) != 0) {
                                                                                                        if (spannableStringBuilder3.length() > 0) {
                                                                                                            spannableStringBuilder3.append((CharSequence) ", ");
                                                                                                        }
                                                                                                        spannableStringBuilder3.append((CharSequence) "e");
                                                                                                        spannableStringBuilder3.setSpan(new AnimatedEmojiSpan(tL_channelAdminLogEventActionChangePeerColor.new_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder3.length() - 1, spannableStringBuilder3.length(), 33);
                                                                                                    }
                                                                                                    spannableStringBuilder4 = spannableStringBuilder5;
                                                                                                } else if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeProfilePeerColor) {
                                                                                                    boolean isChannelAndNotMegaGroup3 = ChatObject.isChannelAndNotMegaGroup(chat);
                                                                                                    TLRPC.TL_channelAdminLogEventActionChangeProfilePeerColor tL_channelAdminLogEventActionChangeProfilePeerColor = (TLRPC.TL_channelAdminLogEventActionChangeProfilePeerColor) tL_channelAdminLogEvent2.action;
                                                                                                    SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder(LocaleController.getString(isChannelAndNotMegaGroup3 ? R.string.EventLogChangedProfileColorIcon : R.string.EventLogChangedProfileColorIconGroup));
                                                                                                    spannableStringBuilder = new SpannableStringBuilder();
                                                                                                    if ((tL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.flags & 1) != 0) {
                                                                                                        spannableStringBuilder.append((CharSequence) "c");
                                                                                                        spannableStringBuilder2 = spannableStringBuilder9;
                                                                                                        spannableStringBuilder.setSpan(new PeerColorActivity.PeerColorSpan(true, this.currentAccount, tL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
                                                                                                    } else {
                                                                                                        spannableStringBuilder2 = spannableStringBuilder9;
                                                                                                    }
                                                                                                    if ((tL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.flags & 2) != 0) {
                                                                                                        if (spannableStringBuilder.length() > 0) {
                                                                                                            spannableStringBuilder.append((CharSequence) ", ");
                                                                                                        }
                                                                                                        spannableStringBuilder.append((CharSequence) "e");
                                                                                                        spannableStringBuilder.setSpan(new AnimatedEmojiSpan(tL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
                                                                                                    }
                                                                                                    if (spannableStringBuilder.length() == 0) {
                                                                                                        spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                    }
                                                                                                    SpannableStringBuilder spannableStringBuilder10 = new SpannableStringBuilder();
                                                                                                    if ((tL_channelAdminLogEventActionChangeProfilePeerColor.new_value.flags & 1) != 0) {
                                                                                                        spannableStringBuilder10.append((CharSequence) "c");
                                                                                                        spannableStringBuilder10.setSpan(new PeerColorActivity.PeerColorSpan(true, this.currentAccount, tL_channelAdminLogEventActionChangeProfilePeerColor.new_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder10.length() - 1, spannableStringBuilder10.length(), 33);
                                                                                                    }
                                                                                                    if ((tL_channelAdminLogEventActionChangeProfilePeerColor.new_value.flags & 2) != 0) {
                                                                                                        if (spannableStringBuilder10.length() > 0) {
                                                                                                            spannableStringBuilder10.append((CharSequence) ", ");
                                                                                                        }
                                                                                                        spannableStringBuilder10.append((CharSequence) "e");
                                                                                                        spannableStringBuilder10.setSpan(new AnimatedEmojiSpan(tL_channelAdminLogEventActionChangeProfilePeerColor.new_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder10.length() - 1, spannableStringBuilder10.length(), 33);
                                                                                                    }
                                                                                                    if (spannableStringBuilder10.length() == 0) {
                                                                                                        spannableStringBuilder3 = spannableStringBuilder10;
                                                                                                        spannableStringBuilder4 = spannableStringBuilder2;
                                                                                                        spannableStringBuilder3.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                        string = AndroidUtilities.replaceCharSequence(str10, AndroidUtilities.replaceCharSequence("%1$s", spannableStringBuilder4, spannableStringBuilder), spannableStringBuilder3);
                                                                                                    } else {
                                                                                                        spannableStringBuilder3 = spannableStringBuilder10;
                                                                                                        spannableStringBuilder4 = spannableStringBuilder2;
                                                                                                        string = AndroidUtilities.replaceCharSequence(str10, AndroidUtilities.replaceCharSequence("%1$s", spannableStringBuilder4, spannableStringBuilder), spannableStringBuilder3);
                                                                                                    }
                                                                                                } else {
                                                                                                    if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeEmojiStatus) {
                                                                                                        boolean isChannelAndNotMegaGroup4 = ChatObject.isChannelAndNotMegaGroup(chat);
                                                                                                        TLRPC.TL_channelAdminLogEventActionChangeEmojiStatus tL_channelAdminLogEventActionChangeEmojiStatus = (TLRPC.TL_channelAdminLogEventActionChangeEmojiStatus) tL_channelAdminLogEvent2.action;
                                                                                                        if (tL_channelAdminLogEventActionChangeEmojiStatus.prev_value instanceof TLRPC.TL_emojiStatusEmpty) {
                                                                                                            spannableString3 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                            z3 = true;
                                                                                                        } else {
                                                                                                            spannableString3 = new SpannableString("e");
                                                                                                            spannableString3.setSpan(new AnimatedEmojiSpan(DialogObject.getEmojiStatusDocumentId(tL_channelAdminLogEventActionChangeEmojiStatus.prev_value), Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                                                                            z3 = false;
                                                                                                        }
                                                                                                        TLRPC.EmojiStatus emojiStatus = tL_channelAdminLogEventActionChangeEmojiStatus.new_value;
                                                                                                        boolean z20 = emojiStatus instanceof TLRPC.TL_emojiStatusUntil;
                                                                                                        if (emojiStatus instanceof TLRPC.TL_emojiStatusEmpty) {
                                                                                                            spannableString4 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                            str5 = "un1";
                                                                                                        } else {
                                                                                                            SpannableString spannableString5 = new SpannableString("e");
                                                                                                            str5 = "un1";
                                                                                                            spannableString5.setSpan(new AnimatedEmojiSpan(DialogObject.getEmojiStatusDocumentId(tL_channelAdminLogEventActionChangeEmojiStatus.new_value), Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                                                                            spannableString4 = spannableString5;
                                                                                                        }
                                                                                                        SpannableStringBuilder replaceCharSequence = AndroidUtilities.replaceCharSequence(str10, AndroidUtilities.replaceCharSequence("%1$s", new SpannableStringBuilder(LocaleController.getString(z3 ? z20 ? isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFor : R.string.EventLogChangedEmojiStatusForGroup : isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatus : R.string.EventLogChangedEmojiStatusGroup : z20 ? isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFromFor : R.string.EventLogChangedEmojiStatusFromForGroup : isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFrom : R.string.EventLogChangedEmojiStatusFromGroup)), spannableString3), spannableString4);
                                                                                                        string = z20 ? AndroidUtilities.replaceCharSequence("%3$s", replaceCharSequence, LocaleController.formatTTLString((int) ((DialogObject.getEmojiStatusUntil(tL_channelAdminLogEventActionChangeEmojiStatus.new_value) - tL_channelAdminLogEvent2.date) * 1.05f))) : replaceCharSequence;
                                                                                                    } else {
                                                                                                        str5 = "un1";
                                                                                                        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeWallpaper) {
                                                                                                            TLRPC.TL_channelAdminLogEventActionChangeWallpaper tL_channelAdminLogEventActionChangeWallpaper = (TLRPC.TL_channelAdminLogEventActionChangeWallpaper) channelAdminLogEventAction;
                                                                                                            boolean isChannelAndNotMegaGroup5 = ChatObject.isChannelAndNotMegaGroup(chat);
                                                                                                            TLRPC.WallPaper wallPaper = tL_channelAdminLogEventActionChangeWallpaper.new_value;
                                                                                                            if ((wallPaper instanceof TLRPC.TL_wallPaperNoFile) && wallPaper.id == 0 && wallPaper.settings == null) {
                                                                                                                i2 = isChannelAndNotMegaGroup5 ? R.string.EventLogRemovedWallpaper : R.string.EventLogRemovedWallpaperGroup;
                                                                                                            } else {
                                                                                                                ArrayList<TLRPC.PhotoSize> arrayList7 = new ArrayList<>();
                                                                                                                this.photoThumbs = arrayList7;
                                                                                                                TLRPC.Document document = tL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                                                                                                                if (document != null) {
                                                                                                                    arrayList7.addAll(document.thumbs);
                                                                                                                    this.photoThumbsObject = tL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                                                                                                                }
                                                                                                                i2 = isChannelAndNotMegaGroup5 ? R.string.EventLogChangedWallpaper : R.string.EventLogChangedWallpaperGroup;
                                                                                                            }
                                                                                                            string = LocaleController.getString(i2);
                                                                                                        } else {
                                                                                                            if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeBackgroundEmoji) {
                                                                                                                boolean isChannelAndNotMegaGroup6 = ChatObject.isChannelAndNotMegaGroup(chat);
                                                                                                                TLRPC.TL_channelAdminLogEventActionChangeBackgroundEmoji tL_channelAdminLogEventActionChangeBackgroundEmoji = (TLRPC.TL_channelAdminLogEventActionChangeBackgroundEmoji) tL_channelAdminLogEvent2.action;
                                                                                                                this.messageText = replaceWithLink(LocaleController.getString(isChannelAndNotMegaGroup6 ? R.string.EventLogChangedEmoji : R.string.EventLogChangedEmojiGroup), str5, user2);
                                                                                                                if (tL_channelAdminLogEventActionChangeBackgroundEmoji.prev_value == 0) {
                                                                                                                    spannableString = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                                } else {
                                                                                                                    spannableString = new SpannableString("e");
                                                                                                                    spannableString.setSpan(new AnimatedEmojiSpan(tL_channelAdminLogEventActionChangeBackgroundEmoji.prev_value, Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                                                                                }
                                                                                                                this.messageText = AndroidUtilities.replaceCharSequence("%1$s", this.messageText, spannableString);
                                                                                                                if (tL_channelAdminLogEventActionChangeBackgroundEmoji.new_value == 0) {
                                                                                                                    spannableString2 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                                } else {
                                                                                                                    spannableString2 = new SpannableString("e");
                                                                                                                    spannableString2.setSpan(new AnimatedEmojiSpan(tL_channelAdminLogEventActionChangeBackgroundEmoji.new_value, Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                                                                                }
                                                                                                                charSequence2 = AndroidUtilities.replaceCharSequence(str10, this.messageText, spannableString2);
                                                                                                            } else {
                                                                                                                charSequence2 = "unsupported " + tL_channelAdminLogEvent2.action;
                                                                                                            }
                                                                                                            charSequence3 = charSequence2;
                                                                                                        }
                                                                                                    }
                                                                                                    message2 = null;
                                                                                                    replaceWithLink3 = string;
                                                                                                    user3 = user2;
                                                                                                    str11 = str5;
                                                                                                }
                                                                                            }
                                                                                            message = null;
                                                                                            sb3 = charSequence;
                                                                                        }
                                                                                        str5 = "un1";
                                                                                        message2 = null;
                                                                                        replaceWithLink3 = string;
                                                                                        user3 = user2;
                                                                                        str11 = str5;
                                                                                    }
                                                                                    tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                    charSequence5 = charSequence4;
                                                                                    chat2 = chat;
                                                                                    charSequence = charSequence5;
                                                                                    message = null;
                                                                                    sb3 = charSequence;
                                                                                }
                                                                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                                charSequence5 = charSequence6;
                                                                                chat2 = chat;
                                                                                charSequence = charSequence5;
                                                                                message = null;
                                                                                sb3 = charSequence;
                                                                            }
                                                                        }
                                                                        charSequence6 = replaceWithLink(replaceWithLink2, str4, tLObject2);
                                                                        tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                        charSequence5 = charSequence6;
                                                                        chat2 = chat;
                                                                        charSequence = charSequence5;
                                                                        message = null;
                                                                        sb3 = charSequence;
                                                                    }
                                                                }
                                                                charSequence6 = replaceWithLink(replaceWithLink, str3, tLObject);
                                                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                                charSequence5 = charSequence6;
                                                                chat2 = chat;
                                                                charSequence = charSequence5;
                                                                message = null;
                                                                sb3 = charSequence;
                                                            }
                                                            replaceWithLink3 = replaceWithLink(LocaleController.getString(i4), "un1", user2);
                                                            this.messageText = replaceWithLink3;
                                                            str11 = "un2";
                                                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                        }
                                                        CharSequence replaceWithLink4222 = replaceWithLink(replaceWithLink3, str11, user3);
                                                        message = message2;
                                                        replace = replaceWithLink4222;
                                                        chat2 = chat;
                                                        sb3 = replace;
                                                    }
                                                    message2 = null;
                                                    string = LocaleController.getString(i3);
                                                    tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                                    str5 = "un1";
                                                    replaceWithLink3 = string;
                                                    user3 = user2;
                                                    str11 = str5;
                                                    CharSequence replaceWithLink42222 = replaceWithLink(replaceWithLink3, str11, user3);
                                                    message = message2;
                                                    replace = replaceWithLink42222;
                                                    chat2 = chat;
                                                    sb3 = replace;
                                                }
                                            }
                                            chat2 = chat;
                                            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent3;
                                            arrayList3 = null;
                                            if (this.messageOwner == null) {
                                            }
                                            this.messageOwner.message = this.messageText.toString();
                                            this.messageOwner.from_id = new TLRPC.TL_peerUser();
                                            TLRPC.Message message42222 = this.messageOwner;
                                            message42222.from_id.user_id = tL_channelAdminLogEvent2.user_id;
                                            message42222.date = tL_channelAdminLogEvent2.date;
                                            int i152222 = iArr[0];
                                            iArr[0] = i152222 + 1;
                                            message42222.id = i152222;
                                            this.eventId = tL_channelAdminLogEvent2.id;
                                            message42222.out = false;
                                            message42222.peer_id = new TLRPC.TL_peerChannel();
                                            TLRPC.Message message52222 = this.messageOwner;
                                            message52222.peer_id.channel_id = chat2.id;
                                            message52222.unread = false;
                                            mediaController = MediaController.getInstance();
                                            this.isOutOwnerCached = null;
                                            if (message3 instanceof TLRPC.TL_messageEmpty) {
                                            }
                                            if (message3 == null) {
                                            }
                                            if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                                            }
                                        }
                                        message2 = null;
                                        CharSequence replaceWithLink422222 = replaceWithLink(replaceWithLink3, str11, user3);
                                        message = message2;
                                        replace = replaceWithLink422222;
                                        chat2 = chat;
                                        sb3 = replace;
                                    }
                                    str9 = "";
                                    message2 = null;
                                    string = LocaleController.getString(i3);
                                    tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                    str5 = "un1";
                                    replaceWithLink3 = string;
                                    user3 = user2;
                                    str11 = str5;
                                    CharSequence replaceWithLink4222222 = replaceWithLink(replaceWithLink3, str11, user3);
                                    message = message2;
                                    replace = replaceWithLink4222222;
                                    chat2 = chat;
                                    sb3 = replace;
                                }
                                str9 = "";
                                string = LocaleController.getString(i3);
                                tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
                                str5 = "un1";
                                replaceWithLink3 = string;
                                user3 = user2;
                                str11 = str5;
                                CharSequence replaceWithLink42222222 = replaceWithLink(replaceWithLink3, str11, user3);
                                message = message2;
                                replace = replaceWithLink42222222;
                                chat2 = chat;
                                sb3 = replace;
                            }
                            this.messageText = sb3;
                            message3 = message;
                            arrayList3 = null;
                            if (this.messageOwner == null) {
                            }
                            this.messageOwner.message = this.messageText.toString();
                            this.messageOwner.from_id = new TLRPC.TL_peerUser();
                            TLRPC.Message message422222 = this.messageOwner;
                            message422222.from_id.user_id = tL_channelAdminLogEvent2.user_id;
                            message422222.date = tL_channelAdminLogEvent2.date;
                            int i1522222 = iArr[0];
                            iArr[0] = i1522222 + 1;
                            message422222.id = i1522222;
                            this.eventId = tL_channelAdminLogEvent2.id;
                            message422222.out = false;
                            message422222.peer_id = new TLRPC.TL_peerChannel();
                            TLRPC.Message message522222 = this.messageOwner;
                            message522222.peer_id.channel_id = chat2.id;
                            message522222.unread = false;
                            mediaController = MediaController.getInstance();
                            this.isOutOwnerCached = null;
                            if (message3 instanceof TLRPC.TL_messageEmpty) {
                            }
                            if (message3 == null) {
                            }
                            if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                            }
                        }
                        message = null;
                        replace = charSequence3;
                        chat2 = chat;
                        sb3 = replace;
                        this.messageText = sb3;
                        message3 = message;
                        arrayList3 = null;
                        if (this.messageOwner == null) {
                        }
                        this.messageOwner.message = this.messageText.toString();
                        this.messageOwner.from_id = new TLRPC.TL_peerUser();
                        TLRPC.Message message4222222 = this.messageOwner;
                        message4222222.from_id.user_id = tL_channelAdminLogEvent2.user_id;
                        message4222222.date = tL_channelAdminLogEvent2.date;
                        int i15222222 = iArr[0];
                        iArr[0] = i15222222 + 1;
                        message4222222.id = i15222222;
                        this.eventId = tL_channelAdminLogEvent2.id;
                        message4222222.out = false;
                        message4222222.peer_id = new TLRPC.TL_peerChannel();
                        TLRPC.Message message5222222 = this.messageOwner;
                        message5222222.peer_id.channel_id = chat2.id;
                        message5222222.unread = false;
                        mediaController = MediaController.getInstance();
                        this.isOutOwnerCached = null;
                        if (message3 instanceof TLRPC.TL_messageEmpty) {
                        }
                        if (message3 == null) {
                        }
                        if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                        }
                    }
                    if (z2) {
                        TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin tL_channelAdminLogEventActionParticipantToggleAdmin = (TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin) channelAdminLogEventAction;
                        channelParticipant = tL_channelAdminLogEventActionParticipantToggleAdmin.prev_participant;
                        channelParticipant2 = tL_channelAdminLogEventActionParticipantToggleAdmin.new_participant;
                    } else {
                        TLRPC.TL_channelAdminLogEventActionParticipantToggleBan tL_channelAdminLogEventActionParticipantToggleBan3 = (TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) channelAdminLogEventAction;
                        channelParticipant = tL_channelAdminLogEventActionParticipantToggleBan3.prev_participant;
                        channelParticipant2 = tL_channelAdminLogEventActionParticipantToggleBan3.new_participant;
                    }
                    TLRPC.TL_message tL_message4 = new TLRPC.TL_message();
                    this.messageOwner = tL_message4;
                    tL_message4.realId = -1;
                    long peerId6 = getPeerId(channelParticipant.peer);
                    if (peerId6 > 0) {
                        messagesController = MessagesController.getInstance(this.currentAccount);
                    } else {
                        messagesController = MessagesController.getInstance(this.currentAccount);
                        peerId6 = -peerId6;
                    }
                    TLRPC.User user8 = messagesController.getUser(Long.valueOf(peerId6));
                    if ((channelParticipant instanceof TLRPC.TL_channelParticipantCreator) || !(channelParticipant2 instanceof TLRPC.TL_channelParticipantCreator)) {
                        TLRPC.TL_chatAdminRights tL_chatAdminRights = channelParticipant.admin_rights;
                        TLRPC.TL_chatAdminRights tL_chatAdminRights2 = channelParticipant2.admin_rights;
                        tL_chatAdminRights = tL_chatAdminRights == null ? new TLRPC.TL_chatAdminRights() : tL_chatAdminRights;
                        tL_chatAdminRights2 = tL_chatAdminRights2 == null ? new TLRPC.TL_chatAdminRights() : tL_chatAdminRights2;
                        String string4 = LocaleController.getString(tL_chatAdminRights2.other ? R.string.EventLogPromotedNoRights : R.string.EventLogPromoted);
                        StringBuilder sb7 = new StringBuilder(String.format(string4, getUserName(user8, this.messageOwner.entities, string4.indexOf("%1$s"))));
                        sb7.append("\n");
                        if (TextUtils.equals(channelParticipant.rank, channelParticipant2.rank)) {
                            c = '+';
                        } else if (TextUtils.isEmpty(channelParticipant2.rank)) {
                            sb7.append('\n');
                            sb7.append('-');
                            sb7.append(' ');
                            sb7.append(LocaleController.getString(R.string.EventLogPromotedRemovedTitle));
                            c = '+';
                        } else {
                            sb7.append('\n');
                            c = '+';
                            sb7.append('+');
                            sb7.append(' ');
                            sb7.append(LocaleController.formatString("EventLogPromotedTitle", R.string.EventLogPromotedTitle, channelParticipant2.rank));
                        }
                        if (tL_chatAdminRights.change_info != tL_chatAdminRights2.change_info) {
                            sb7.append('\n');
                            sb7.append(tL_chatAdminRights2.change_info ? '+' : '-');
                            sb7.append(' ');
                            chat2 = chat;
                            sb7.append(LocaleController.getString(chat2.megagroup ? R.string.EventLogPromotedChangeGroupInfo : R.string.EventLogPromotedChangeChannelInfo));
                        } else {
                            chat2 = chat;
                        }
                        if (!chat2.megagroup) {
                            if (tL_chatAdminRights.post_messages != tL_chatAdminRights2.post_messages) {
                                sb7.append('\n');
                                sb7.append(tL_chatAdminRights2.post_messages ? '+' : '-');
                                sb7.append(' ');
                                sb7.append(LocaleController.getString(R.string.EventLogPromotedPostMessages));
                            }
                            if (tL_chatAdminRights.edit_messages != tL_chatAdminRights2.edit_messages) {
                                sb7.append('\n');
                                sb7.append(tL_chatAdminRights2.edit_messages ? '+' : '-');
                                sb7.append(' ');
                                sb7.append(LocaleController.getString(R.string.EventLogPromotedEditMessages));
                            }
                        }
                        if (tL_chatAdminRights.post_stories != tL_chatAdminRights2.post_stories) {
                            sb7.append('\n');
                            sb7.append(tL_chatAdminRights2.post_stories ? '+' : '-');
                            sb7.append(' ');
                            sb7.append(LocaleController.getString(R.string.EventLogPromotedPostStories));
                        }
                        if (tL_chatAdminRights.edit_stories != tL_chatAdminRights2.edit_stories) {
                            sb7.append('\n');
                            sb7.append(tL_chatAdminRights2.edit_stories ? '+' : '-');
                            sb7.append(' ');
                            sb7.append(LocaleController.getString(R.string.EventLogPromotedEditStories));
                        }
                        if (tL_chatAdminRights.delete_stories != tL_chatAdminRights2.delete_stories) {
                            sb7.append('\n');
                            sb7.append(tL_chatAdminRights2.delete_stories ? '+' : '-');
                            sb7.append(' ');
                            sb7.append(LocaleController.getString(R.string.EventLogPromotedDeleteStories));
                        }
                        if (tL_chatAdminRights.delete_messages != tL_chatAdminRights2.delete_messages) {
                            sb7.append('\n');
                            sb7.append(tL_chatAdminRights2.delete_messages ? '+' : '-');
                            sb7.append(' ');
                            sb7.append(LocaleController.getString(R.string.EventLogPromotedDeleteMessages));
                        }
                        if (tL_chatAdminRights.add_admins != tL_chatAdminRights2.add_admins) {
                            sb7.append('\n');
                            sb7.append(tL_chatAdminRights2.add_admins ? '+' : '-');
                            sb7.append(' ');
                            sb7.append(LocaleController.getString(R.string.EventLogPromotedAddAdmins));
                        }
                        if (tL_chatAdminRights.anonymous != tL_chatAdminRights2.anonymous) {
                            sb7.append('\n');
                            sb7.append(tL_chatAdminRights2.anonymous ? '+' : '-');
                            sb7.append(' ');
                            sb7.append(LocaleController.getString(R.string.EventLogPromotedSendAnonymously));
                        }
                        if (chat2.megagroup) {
                            if (tL_chatAdminRights.ban_users != tL_chatAdminRights2.ban_users) {
                                sb7.append('\n');
                                sb7.append(tL_chatAdminRights2.ban_users ? '+' : '-');
                                sb7.append(' ');
                                sb7.append(LocaleController.getString(R.string.EventLogPromotedBanUsers));
                            }
                            if (tL_chatAdminRights.manage_call != tL_chatAdminRights2.manage_call) {
                                sb7.append('\n');
                                sb7.append(tL_chatAdminRights2.manage_call ? '+' : '-');
                                sb7.append(' ');
                                sb7.append(LocaleController.getString(R.string.EventLogPromotedManageCall));
                            }
                        }
                        if (tL_chatAdminRights.invite_users != tL_chatAdminRights2.invite_users) {
                            sb7.append('\n');
                            sb7.append(tL_chatAdminRights2.invite_users ? '+' : '-');
                            sb7.append(' ');
                            sb7.append(LocaleController.getString(R.string.EventLogPromotedAddUsers));
                        }
                        if (chat2.megagroup && tL_chatAdminRights.pin_messages != tL_chatAdminRights2.pin_messages) {
                            sb7.append('\n');
                            sb7.append(tL_chatAdminRights2.pin_messages ? c : '-');
                            sb7.append(' ');
                            sb7.append(LocaleController.getString(R.string.EventLogPromotedPinMessages));
                        }
                        sb = sb7;
                    } else {
                        String string5 = LocaleController.getString(R.string.EventLogChangedOwnership);
                        sb = new StringBuilder(String.format(string5, getUserName(user8, this.messageOwner.entities, string5.indexOf("%1$s"))));
                        chat2 = chat;
                    }
                    charSequence = sb.toString();
                    message = null;
                    sb3 = charSequence;
                    this.messageText = sb3;
                    message3 = message;
                    arrayList3 = null;
                    if (this.messageOwner == null) {
                    }
                    this.messageOwner.message = this.messageText.toString();
                    this.messageOwner.from_id = new TLRPC.TL_peerUser();
                    TLRPC.Message message42222222 = this.messageOwner;
                    message42222222.from_id.user_id = tL_channelAdminLogEvent2.user_id;
                    message42222222.date = tL_channelAdminLogEvent2.date;
                    int i152222222 = iArr[0];
                    iArr[0] = i152222222 + 1;
                    message42222222.id = i152222222;
                    this.eventId = tL_channelAdminLogEvent2.id;
                    message42222222.out = false;
                    message42222222.peer_id = new TLRPC.TL_peerChannel();
                    TLRPC.Message message52222222 = this.messageOwner;
                    message52222222.peer_id.channel_id = chat2.id;
                    message52222222.unread = false;
                    mediaController = MediaController.getInstance();
                    this.isOutOwnerCached = null;
                    if (message3 instanceof TLRPC.TL_messageEmpty) {
                    }
                    if (message3 == null) {
                    }
                    if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                    }
                }
            }
            user2 = user;
            message2 = null;
            string = LocaleController.getString(i3);
            tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
            str5 = "un1";
            replaceWithLink3 = string;
            user3 = user2;
            str11 = str5;
            CharSequence replaceWithLink422222222 = replaceWithLink(replaceWithLink3, str11, user3);
            message = message2;
            replace = replaceWithLink422222222;
            chat2 = chat;
            sb3 = replace;
            this.messageText = sb3;
            message3 = message;
            arrayList3 = null;
            if (this.messageOwner == null) {
            }
            this.messageOwner.message = this.messageText.toString();
            this.messageOwner.from_id = new TLRPC.TL_peerUser();
            TLRPC.Message message422222222 = this.messageOwner;
            message422222222.from_id.user_id = tL_channelAdminLogEvent2.user_id;
            message422222222.date = tL_channelAdminLogEvent2.date;
            int i1522222222 = iArr[0];
            iArr[0] = i1522222222 + 1;
            message422222222.id = i1522222222;
            this.eventId = tL_channelAdminLogEvent2.id;
            message422222222.out = false;
            message422222222.peer_id = new TLRPC.TL_peerChannel();
            TLRPC.Message message522222222 = this.messageOwner;
            message522222222.peer_id.channel_id = chat2.id;
            message522222222.unread = false;
            mediaController = MediaController.getInstance();
            this.isOutOwnerCached = null;
            if (message3 instanceof TLRPC.TL_messageEmpty) {
            }
            if (message3 == null) {
            }
            if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
            }
        }
        str5 = "un1";
        tL_channelAdminLogEvent2 = tL_channelAdminLogEvent;
        user2 = user;
        message2 = null;
        replaceWithLink3 = string;
        user3 = user2;
        str11 = str5;
        CharSequence replaceWithLink4222222222 = replaceWithLink(replaceWithLink3, str11, user3);
        message = message2;
        replace = replaceWithLink4222222222;
        chat2 = chat;
        sb3 = replace;
        this.messageText = sb3;
        message3 = message;
        arrayList3 = null;
        if (this.messageOwner == null) {
        }
        this.messageOwner.message = this.messageText.toString();
        this.messageOwner.from_id = new TLRPC.TL_peerUser();
        TLRPC.Message message4222222222 = this.messageOwner;
        message4222222222.from_id.user_id = tL_channelAdminLogEvent2.user_id;
        message4222222222.date = tL_channelAdminLogEvent2.date;
        int i15222222222 = iArr[0];
        iArr[0] = i15222222222 + 1;
        message4222222222.id = i15222222222;
        this.eventId = tL_channelAdminLogEvent2.id;
        message4222222222.out = false;
        message4222222222.peer_id = new TLRPC.TL_peerChannel();
        TLRPC.Message message5222222222 = this.messageOwner;
        message5222222222.peer_id.channel_id = chat2.id;
        message5222222222.unread = false;
        mediaController = MediaController.getInstance();
        this.isOutOwnerCached = null;
        if (message3 instanceof TLRPC.TL_messageEmpty) {
        }
        if (message3 == null) {
        }
        if (tL_channelAdminLogEvent2.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
        }
    }

    public MessageObject(int i, TL_stories.StoryItem storyItem) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.actionDeleteGroupEventId = -1L;
        this.overrideLinkColor = -1;
        this.overrideLinkEmoji = -1L;
        this.messageTrimmedToHighlightCut = true;
        this.topicIconDrawable = new Drawable[1];
        this.spoiledLoginCode = false;
        this.translated = false;
        this.currentAccount = i;
        this.storyItem = storyItem;
        if (storyItem != null) {
            TLRPC.TL_message tL_message = new TLRPC.TL_message();
            this.messageOwner = tL_message;
            tL_message.id = storyItem.messageId;
            tL_message.realId = storyItem.id;
            tL_message.date = storyItem.date;
            tL_message.dialog_id = storyItem.dialogId;
            tL_message.message = storyItem.caption;
            tL_message.entities = storyItem.entities;
            tL_message.media = storyItem.media;
            tL_message.attachPath = storyItem.attachPath;
        }
        this.photoThumbs = new ArrayList<>();
        this.photoThumbs2 = new ArrayList<>();
    }

    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<TLRPC.MessageEntity> arrayList, boolean z, boolean z2, boolean z3, boolean z4) {
        return addEntitiesToText(charSequence, arrayList, z, z2, z3, z4, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:119:0x013c, code lost:
    
        if (r22 == false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0148, code lost:
    
        r4.flags = 64;
        r4.urlEntity = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x0144, code lost:
    
        if (r22 == false) goto L29;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0236 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0419 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0186  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0233  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<TLRPC.MessageEntity> arrayList, boolean z, boolean z2, boolean z3, boolean z4, int i) {
        int i2;
        String str;
        TextStyleSpan.TextStyleRun textStyleRun;
        Object uRLSpanNoUnderline;
        boolean z5;
        boolean z6;
        Object uRLSpanUserMention;
        Object uRLSpanReplacement;
        int i3;
        int i4;
        int i5;
        int size;
        int i6;
        TextStyleSpan.TextStyleRun textStyleRun2;
        CharSequence charSequence2 = charSequence;
        if (!(charSequence2 instanceof Spannable)) {
            return false;
        }
        Spannable spannable = (Spannable) charSequence2;
        URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, charSequence.length(), URLSpan.class);
        boolean z7 = uRLSpanArr != null && uRLSpanArr.length > 0;
        if (arrayList == null || arrayList.isEmpty()) {
            return z7;
        }
        byte b = z3 ? (byte) 2 : z ? (byte) 1 : (byte) 0;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList(arrayList);
        Collections.sort(arrayList3, new Comparator() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda11
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$addEntitiesToText$2;
                lambda$addEntitiesToText$2 = MessageObject.lambda$addEntitiesToText$2((TLRPC.MessageEntity) obj, (TLRPC.MessageEntity) obj2);
                return lambda$addEntitiesToText$2;
            }
        });
        int size2 = arrayList3.size();
        int i7 = 0;
        while (i7 < size2) {
            TLRPC.MessageEntity messageEntity = (TLRPC.MessageEntity) arrayList3.get(i7);
            if (messageEntity.length > 0 && (i4 = messageEntity.offset) >= 0 && i4 < charSequence.length()) {
                if (messageEntity.offset + messageEntity.length > charSequence.length()) {
                    messageEntity.length = charSequence.length() - messageEntity.offset;
                }
                if ((!z4 || (messageEntity instanceof TLRPC.TL_messageEntityBold) || (messageEntity instanceof TLRPC.TL_messageEntityItalic) || (messageEntity instanceof TLRPC.TL_messageEntityStrike) || (messageEntity instanceof TLRPC.TL_messageEntityUnderline) || (messageEntity instanceof TLRPC.TL_messageEntityBlockquote) || (messageEntity instanceof TLRPC.TL_messageEntityCode) || (messageEntity instanceof TLRPC.TL_messageEntityPre) || (messageEntity instanceof TLRPC.TL_messageEntityMentionName) || (messageEntity instanceof TLRPC.TL_inputMessageEntityMentionName) || (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) || (messageEntity instanceof TLRPC.TL_messageEntitySpoiler) || (messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji)) && uRLSpanArr != null && uRLSpanArr.length > 0) {
                    for (int i8 = 0; i8 < uRLSpanArr.length; i8++) {
                        URLSpan uRLSpan = uRLSpanArr[i8];
                        if (uRLSpan != null) {
                            int spanStart = spannable.getSpanStart(uRLSpan);
                            int spanEnd = spannable.getSpanEnd(uRLSpanArr[i8]);
                            int i9 = messageEntity.offset;
                            if ((i9 <= spanStart && messageEntity.length + i9 >= spanStart) || (i9 <= spanEnd && i9 + messageEntity.length >= spanEnd)) {
                                spannable.removeSpan(uRLSpanArr[i8]);
                                uRLSpanArr[i8] = null;
                            }
                        }
                    }
                }
                if ((i != 1 || (messageEntity instanceof TLRPC.TL_messageEntityHashtag)) && !(messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji) && !(messageEntity instanceof TLRPC.TL_messageEntityBlockquote) && !(messageEntity instanceof TLRPC.TL_messageEntityPre)) {
                    TextStyleSpan.TextStyleRun textStyleRun3 = new TextStyleSpan.TextStyleRun();
                    int i10 = messageEntity.offset;
                    textStyleRun3.start = i10;
                    textStyleRun3.end = i10 + messageEntity.length;
                    if (messageEntity instanceof TLRPC.TL_messageEntitySpoiler) {
                        i5 = 256;
                    } else if (messageEntity instanceof TLRPC.TL_messageEntityStrike) {
                        i5 = 8;
                    } else if (messageEntity instanceof TLRPC.TL_messageEntityUnderline) {
                        i5 = 16;
                    } else if (messageEntity instanceof TLRPC.TL_messageEntityBold) {
                        i5 = 1;
                    } else {
                        if (messageEntity instanceof TLRPC.TL_messageEntityItalic) {
                            textStyleRun3.flags = 2;
                        } else if (messageEntity instanceof TLRPC.TL_messageEntityCode) {
                            textStyleRun3.flags = 4;
                        } else if (!(messageEntity instanceof TLRPC.TL_messageEntityMentionName)) {
                            if (!(messageEntity instanceof TLRPC.TL_inputMessageEntityMentionName)) {
                                if ((!z4 || (messageEntity instanceof TLRPC.TL_messageEntityTextUrl)) && (((!(messageEntity instanceof TLRPC.TL_messageEntityUrl) && !(messageEntity instanceof TLRPC.TL_messageEntityTextUrl)) || !Browser.isPassportUrl(messageEntity.url)) && (!(messageEntity instanceof TLRPC.TL_messageEntityMention) || z2))) {
                                    textStyleRun3.flags = 128;
                                    textStyleRun3.urlEntity = messageEntity;
                                    if (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) {
                                        textStyleRun3.flags = 1152;
                                    }
                                }
                            }
                        }
                        size = arrayList2.size();
                        i6 = 0;
                        while (i6 < size) {
                            TextStyleSpan.TextStyleRun textStyleRun4 = (TextStyleSpan.TextStyleRun) arrayList2.get(i6);
                            int i11 = size2;
                            if ((textStyleRun4.flags & 256) == 0 || textStyleRun3.start < textStyleRun4.start || textStyleRun3.end > textStyleRun4.end) {
                                int i12 = textStyleRun3.start;
                                int i13 = textStyleRun4.start;
                                if (i12 > i13) {
                                    int i14 = textStyleRun4.end;
                                    if (i12 < i14) {
                                        if (textStyleRun3.end < i14) {
                                            TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun(textStyleRun3);
                                            textStyleRun5.merge(textStyleRun4);
                                            arrayList2.add(i6 + 1, textStyleRun5);
                                            textStyleRun2 = new TextStyleSpan.TextStyleRun(textStyleRun4);
                                            textStyleRun2.start = textStyleRun3.end;
                                            i6 += 2;
                                            size += 2;
                                        } else {
                                            textStyleRun2 = new TextStyleSpan.TextStyleRun(textStyleRun3);
                                            textStyleRun2.merge(textStyleRun4);
                                            textStyleRun2.end = textStyleRun4.end;
                                            i6++;
                                            size++;
                                        }
                                        arrayList2.add(i6, textStyleRun2);
                                        int i15 = textStyleRun3.start;
                                        textStyleRun3.start = textStyleRun4.end;
                                        textStyleRun4.end = i15;
                                    }
                                } else {
                                    int i16 = textStyleRun3.end;
                                    if (i13 < i16) {
                                        int i17 = textStyleRun4.end;
                                        if (i16 != i17) {
                                            if (i16 < i17) {
                                                TextStyleSpan.TextStyleRun textStyleRun6 = new TextStyleSpan.TextStyleRun(textStyleRun4);
                                                textStyleRun6.merge(textStyleRun3);
                                                textStyleRun6.end = textStyleRun3.end;
                                                i6++;
                                                size++;
                                                arrayList2.add(i6, textStyleRun6);
                                                textStyleRun4.start = textStyleRun3.end;
                                                textStyleRun3.end = i13;
                                            } else {
                                                TextStyleSpan.TextStyleRun textStyleRun7 = new TextStyleSpan.TextStyleRun(textStyleRun3);
                                                textStyleRun7.start = textStyleRun4.end;
                                                i6++;
                                                size++;
                                                arrayList2.add(i6, textStyleRun7);
                                            }
                                        }
                                        textStyleRun4.merge(textStyleRun3);
                                        textStyleRun3.end = i13;
                                    }
                                }
                            }
                            i6++;
                            size2 = i11;
                        }
                        i3 = size2;
                        if (textStyleRun3.start >= textStyleRun3.end) {
                            arrayList2.add(textStyleRun3);
                        }
                        i7++;
                        size2 = i3;
                    }
                    textStyleRun3.flags = i5;
                    size = arrayList2.size();
                    i6 = 0;
                    while (i6 < size) {
                    }
                    i3 = size2;
                    if (textStyleRun3.start >= textStyleRun3.end) {
                    }
                    i7++;
                    size2 = i3;
                }
            }
            i3 = size2;
            i7++;
            size2 = i3;
        }
        int size3 = arrayList2.size();
        boolean z8 = z7;
        int i18 = 0;
        while (i18 < size3) {
            TextStyleSpan.TextStyleRun textStyleRun8 = (TextStyleSpan.TextStyleRun) arrayList2.get(i18);
            if (i != 1 || (textStyleRun8.urlEntity instanceof TLRPC.TL_messageEntityHashtag)) {
                TLRPC.MessageEntity messageEntity2 = textStyleRun8.urlEntity;
                if (messageEntity2 != null) {
                    int i19 = messageEntity2.offset;
                    str = TextUtils.substring(charSequence2, i19, messageEntity2.length + i19);
                } else {
                    str = null;
                }
                TLRPC.MessageEntity messageEntity3 = textStyleRun8.urlEntity;
                if (messageEntity3 instanceof TLRPC.TL_messageEntityBotCommand) {
                    uRLSpanReplacement = new URLSpanBotCommand(str, b, textStyleRun8);
                } else {
                    if ((messageEntity3 instanceof TLRPC.TL_messageEntityHashtag) || (messageEntity3 instanceof TLRPC.TL_messageEntityMention) || (messageEntity3 instanceof TLRPC.TL_messageEntityCashtag)) {
                        textStyleRun = textStyleRun8;
                        uRLSpanNoUnderline = new URLSpanNoUnderline(str, textStyleRun);
                    } else {
                        if (messageEntity3 instanceof TLRPC.TL_messageEntityEmail) {
                            uRLSpanUserMention = new URLSpanReplacement("mailto:" + str, textStyleRun8);
                        } else if (messageEntity3 instanceof TLRPC.TL_messageEntityUrl) {
                            if (!str.toLowerCase().contains("://")) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(BotWebViewContainer.isTonsite(str) ? "tonsite://" : "http://");
                                sb.append(str);
                                str = sb.toString();
                            }
                            if (str != null) {
                                str = str.replaceAll("∕|⁄|%E2%81%84|%E2%88%95", "/");
                            }
                            if (Browser.isTonsitePunycode(str)) {
                                z8 = true;
                            } else {
                                spannable.setSpan(new URLSpanBrowser(str, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                                textStyleRun = textStyleRun8;
                                z5 = true;
                                z6 = false;
                                if (z6 && (textStyleRun.flags & 256) != 0) {
                                    spannable.setSpan(new TextStyleSpan(textStyleRun), textStyleRun.start, textStyleRun.end, 33);
                                }
                                z8 = z5;
                                i18++;
                                charSequence2 = charSequence;
                            }
                        } else {
                            if (messageEntity3 instanceof TLRPC.TL_messageEntityBankCard) {
                                uRLSpanUserMention = new URLSpanNoUnderline("card:" + str, textStyleRun8);
                            } else if (messageEntity3 instanceof TLRPC.TL_messageEntityPhone) {
                                String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str);
                                if (str.startsWith("+")) {
                                    stripExceptNumbers = "+" + stripExceptNumbers;
                                }
                                uRLSpanUserMention = new URLSpanNoUnderline("tel:" + stripExceptNumbers, textStyleRun8);
                            } else if (messageEntity3 instanceof TLRPC.TL_messageEntityTextUrl) {
                                String str2 = messageEntity3.url;
                                if (str2 != null) {
                                    str2 = str2.replaceAll("∕|⁄|%E2%81%84|%E2%88%95", "/");
                                }
                                if (!Browser.isTonsitePunycode(str2)) {
                                    uRLSpanReplacement = new URLSpanReplacement(str2, textStyleRun8);
                                }
                            } else if (messageEntity3 instanceof TLRPC.TL_messageEntityMentionName) {
                                uRLSpanUserMention = new URLSpanUserMention("" + ((TLRPC.TL_messageEntityMentionName) textStyleRun8.urlEntity).user_id, b, textStyleRun8);
                            } else if (messageEntity3 instanceof TLRPC.TL_inputMessageEntityMentionName) {
                                uRLSpanUserMention = new URLSpanUserMention("" + ((TLRPC.TL_inputMessageEntityMentionName) textStyleRun8.urlEntity).user_id.user_id, b, textStyleRun8);
                            } else if ((textStyleRun8.flags & 4) != 0) {
                                uRLSpanNoUnderline = r9;
                                URLSpanMono uRLSpanMono = new URLSpanMono(spannable, textStyleRun8.start, textStyleRun8.end, b, textStyleRun8);
                                textStyleRun = textStyleRun8;
                            } else {
                                textStyleRun = textStyleRun8;
                                spannable.setSpan(new TextStyleSpan(textStyleRun), textStyleRun.start, textStyleRun.end, 33);
                                z5 = z8;
                                z6 = true;
                                if (z6) {
                                    spannable.setSpan(new TextStyleSpan(textStyleRun), textStyleRun.start, textStyleRun.end, 33);
                                }
                                z8 = z5;
                                i18++;
                                charSequence2 = charSequence;
                            }
                            z8 = true;
                        }
                        spannable.setSpan(uRLSpanUserMention, textStyleRun8.start, textStyleRun8.end, 33);
                        z5 = z8;
                        textStyleRun = textStyleRun8;
                        z6 = false;
                        if (z6) {
                        }
                        z8 = z5;
                        i18++;
                        charSequence2 = charSequence;
                    }
                    spannable.setSpan(uRLSpanNoUnderline, textStyleRun.start, textStyleRun.end, 33);
                    z5 = z8;
                    z6 = false;
                    if (z6) {
                    }
                    z8 = z5;
                    i18++;
                    charSequence2 = charSequence;
                }
                spannable.setSpan(uRLSpanReplacement, textStyleRun8.start, textStyleRun8.end, 33);
                textStyleRun = textStyleRun8;
                z5 = z8;
                z6 = false;
                if (z6) {
                }
                z8 = z5;
                i18++;
                charSequence2 = charSequence;
            }
            i18++;
            charSequence2 = charSequence;
        }
        int size4 = arrayList3.size();
        for (int i20 = 0; i20 < size4; i20++) {
            TLRPC.MessageEntity messageEntity4 = (TLRPC.MessageEntity) arrayList3.get(i20);
            if (messageEntity4.length > 0 && (i2 = messageEntity4.offset) >= 0 && i2 < charSequence.length()) {
                if (messageEntity4.offset + messageEntity4.length > charSequence.length()) {
                    messageEntity4.length = charSequence.length() - messageEntity4.offset;
                }
                if (messageEntity4 instanceof TLRPC.TL_messageEntityBlockquote) {
                    int i21 = messageEntity4.offset;
                    QuoteSpan.putQuote(spannable, i21, messageEntity4.length + i21, messageEntity4.collapsed);
                } else if (messageEntity4 instanceof TLRPC.TL_messageEntityPre) {
                    int i22 = messageEntity4.offset;
                    int i23 = messageEntity4.length + i22;
                    spannable.setSpan(new CodeHighlighting.Span(true, 0, null, messageEntity4.language, spannable.subSequence(i22, i23).toString()), i22, i23, 33);
                }
            }
        }
        return z8;
    }

    private boolean addEntitiesToText(CharSequence charSequence, boolean z) {
        return addEntitiesToText(charSequence, false, z);
    }

    public static void addLinks(boolean z, CharSequence charSequence) {
        addLinks(z, charSequence, true, false);
    }

    public static void addLinks(boolean z, CharSequence charSequence, boolean z2, boolean z3) {
        addLinks(z, charSequence, z2, z3, false);
    }

    public static void addLinks(boolean z, CharSequence charSequence, boolean z2, boolean z3, boolean z4) {
        if ((charSequence instanceof Spannable) && containsUrls(charSequence)) {
            try {
                AndroidUtilities.addLinksSafe((Spannable) charSequence, 1, z4, false);
            } catch (Exception e) {
                FileLog.e(e);
            }
            addPhoneLinks(charSequence);
            addUrlsByPattern(z, charSequence, z2, 0, 0, z3);
        }
    }

    public static void addPaidReactions(int i, TLRPC.MessageReactions messageReactions, int i2, boolean z, boolean z2) {
        TLRPC.MessageReactor messageReactor = null;
        TLRPC.ReactionCount reactionCount = null;
        for (int i3 = 0; i3 < messageReactions.results.size(); i3++) {
            if (messageReactions.results.get(i3).reaction instanceof TLRPC.TL_reactionPaid) {
                reactionCount = messageReactions.results.get(i3);
            }
        }
        int i4 = 0;
        while (true) {
            if (i4 >= messageReactions.top_reactors.size()) {
                break;
            }
            if (messageReactions.top_reactors.get(i4).my) {
                messageReactor = messageReactions.top_reactors.get(i4);
                break;
            }
            i4++;
        }
        if (reactionCount == null && i2 > 0) {
            reactionCount = new TLRPC.TL_reactionCount();
            reactionCount.reaction = new TLRPC.TL_reactionPaid();
            messageReactions.results.add(0, reactionCount);
        }
        if (reactionCount != null) {
            reactionCount.chosen = z2;
            int max = Math.max(0, reactionCount.count + i2);
            reactionCount.count = max;
            if (max <= 0) {
                messageReactions.results.remove(reactionCount);
            }
        }
        if (messageReactor == null && i2 > 0) {
            messageReactor = new TLRPC.TL_messageReactor();
            messageReactor.my = true;
            messageReactor.peer_id = MessagesController.getInstance(i).getPeer(UserConfig.getInstance(i).getClientUserId());
            messageReactions.top_reactors.add(messageReactor);
        }
        if (messageReactor != null) {
            int max2 = Math.max(0, messageReactor.count + i2);
            messageReactor.count = max2;
            messageReactor.anonymous = z;
            if (max2 <= 0) {
                messageReactions.top_reactors.remove(messageReactor);
            }
        }
    }

    public static void addPhoneLinks(CharSequence charSequence) {
    }

    /* JADX WARN: Removed duplicated region for block: B:93:0x01d4 A[Catch: Exception -> 0x001e, TryCatch #0 {Exception -> 0x001e, blocks: (B:10:0x0011, B:12:0x0015, B:13:0x0021, B:14:0x0041, B:17:0x004a, B:18:0x004d, B:20:0x0053, B:23:0x0064, B:27:0x0074, B:28:0x0076, B:34:0x008f, B:44:0x0214, B:46:0x021e, B:48:0x0221, B:49:0x0226, B:50:0x00b6, B:53:0x00db, B:54:0x00fc, B:55:0x011d, B:58:0x0125, B:60:0x013e, B:62:0x014a, B:63:0x0153, B:74:0x0089, B:77:0x015d, B:80:0x019c, B:84:0x01af, B:87:0x01be, B:89:0x01c8, B:91:0x01cf, B:93:0x01d4, B:99:0x01df, B:100:0x020d, B:101:0x01f7, B:109:0x0024, B:111:0x0028, B:112:0x0030, B:113:0x0033, B:115:0x0037, B:116:0x003f), top: B:6:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01dc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void addUrlsByPattern(boolean z, CharSequence charSequence, boolean z2, int i, int i2, boolean z3) {
        Pattern pattern;
        String str;
        URLSpan[] uRLSpanArr;
        int i3;
        URLSpanNoUnderline uRLSpanNoUnderline;
        Object[] objArr;
        if (charSequence == null) {
            return;
        }
        int i4 = 3;
        int i5 = 1;
        try {
            if (i == 3 || i == 4) {
                if (videoTimeUrlPattern == null) {
                    videoTimeUrlPattern = Pattern.compile("\\b(?:(\\d{1,2}):)?(\\d{1,3}):([0-5][0-9])\\b(?: - |)([^\\n]*)");
                }
                pattern = videoTimeUrlPattern;
            } else if (i == 1) {
                if (instagramUrlPattern == null) {
                    instagramUrlPattern = Pattern.compile("(^|\\s|\\()@[a-zA-Z\\d_.]{1,32}|(^|\\s|\\()#[\\w.]+");
                }
                pattern = instagramUrlPattern;
            } else {
                if (urlPattern == null) {
                    urlPattern = Pattern.compile("(^|\\s)/[a-zA-Z@\\d_]{1,255}|(^|\\s|\\()@[a-zA-Z\\d_]{1,32}|(^|\\s|\\()#[^0-9][\\w.]+(@[^0-9][\\w.]+)?|(^|\\s|\\()\\$[^0-9][\\w.]+(@[^0-9][\\w.]+)?|(^|\\s)\\$[A-Z]{3,8}([ ,.]|$)");
                }
                pattern = urlPattern;
            }
            Matcher matcher = pattern.matcher(charSequence);
            if (charSequence instanceof Spannable) {
                Spannable spannable = (Spannable) charSequence;
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    if (i == i4 || i == 4) {
                        matcher.groupCount();
                        int start2 = matcher.start(i5);
                        int end2 = matcher.end(i5);
                        int start3 = matcher.start(2);
                        int end3 = matcher.end(2);
                        int start4 = matcher.start(i4);
                        int end4 = matcher.end(i4);
                        int start5 = matcher.start(4);
                        int end5 = matcher.end(4);
                        int intValue = Utilities.parseInt(charSequence.subSequence(start3, end3)).intValue();
                        int intValue2 = Utilities.parseInt(charSequence.subSequence(start4, end4)).intValue();
                        int intValue3 = (start2 < 0 || end2 < 0) ? -1 : Utilities.parseInt(charSequence.subSequence(start2, end2)).intValue();
                        if (start5 >= 0 && end5 >= 0) {
                            str = charSequence.subSequence(start5, end5).toString();
                            if (start5 < 0 || end5 >= 0) {
                                end = end4;
                            }
                            uRLSpanArr = (URLSpan[]) spannable.getSpans(start, end, URLSpan.class);
                            if (uRLSpanArr != null || uRLSpanArr.length <= 0) {
                                i3 = intValue2 + (intValue * 60);
                                if (intValue3 > 0) {
                                    i3 += intValue3 * 3600;
                                }
                                if (i3 <= i2) {
                                    if (i == 3) {
                                        uRLSpanNoUnderline = new URLSpanNoUnderline("video?" + i3);
                                    } else {
                                        uRLSpanNoUnderline = new URLSpanNoUnderline("audio?" + i3);
                                    }
                                    uRLSpanNoUnderline.label = str;
                                }
                            }
                            i4 = 3;
                            i5 = 1;
                        }
                        str = null;
                        if (start5 < 0) {
                        }
                        end = end4;
                        uRLSpanArr = (URLSpan[]) spannable.getSpans(start, end, URLSpan.class);
                        if (uRLSpanArr != null) {
                        }
                        i3 = intValue2 + (intValue * 60);
                        if (intValue3 > 0) {
                        }
                        if (i3 <= i2) {
                        }
                    } else {
                        char charAt = charSequence.charAt(start);
                        if (i != 0) {
                            if (charAt != '@' && charAt != '#') {
                                start++;
                            }
                            charAt = charSequence.charAt(start);
                            if (charAt != '@' && charAt != '#') {
                            }
                        } else if (charAt != '@' && charAt != '#' && charAt != '/' && charAt != '$') {
                            start++;
                        }
                        if (i == i5) {
                            if (charAt == '@') {
                                uRLSpanNoUnderline = new URLSpanNoUnderline("https://instagram.com/" + charSequence.subSequence(start + 1, end).toString());
                            } else {
                                uRLSpanNoUnderline = new URLSpanNoUnderline("https://www.instagram.com/explore/tags/" + charSequence.subSequence(start + 1, end).toString());
                            }
                        } else if (i == 2) {
                            if (charAt == '@') {
                                uRLSpanNoUnderline = new URLSpanNoUnderline("https://twitter.com/" + charSequence.subSequence(start + 1, end).toString());
                            } else {
                                uRLSpanNoUnderline = new URLSpanNoUnderline("https://twitter.com/hashtag/" + charSequence.subSequence(start + 1, end).toString());
                            }
                        } else if (charSequence.charAt(start) != '/') {
                            String charSequence2 = charSequence.subSequence(start, end).toString();
                            if (charSequence2 != null) {
                                charSequence2 = charSequence2.replaceAll("∕|⁄|%E2%81%84|%E2%88%95", "/");
                            }
                            uRLSpanNoUnderline = new URLSpanNoUnderline(charSequence2);
                        } else if (z2) {
                            uRLSpanNoUnderline = new URLSpanBotCommand(charSequence.subSequence(start, end).toString(), z ? 1 : 0);
                        } else {
                            uRLSpanNoUnderline = null;
                        }
                    }
                    if (uRLSpanNoUnderline != null) {
                        if (z3 && (objArr = (ClickableSpan[]) spannable.getSpans(start, end, ClickableSpan.class)) != null && objArr.length > 0) {
                            spannable.removeSpan(objArr[0]);
                        }
                        spannable.setSpan(uRLSpanNoUnderline, start, end, 0);
                    }
                    i4 = 3;
                    i5 = 1;
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private boolean allowsBigEmoji() {
        TLRPC.Peer peer;
        if (!SharedConfig.allowBigEmoji) {
            return false;
        }
        TLRPC.Message message = this.messageOwner;
        if (message == null || (peer = message.peer_id) == null || (peer.channel_id == 0 && peer.chat_id == 0)) {
            return true;
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        TLRPC.Peer peer2 = this.messageOwner.peer_id;
        long j = peer2.channel_id;
        if (j == 0) {
            j = peer2.chat_id;
        }
        TLRPC.Chat chat = messagesController.getChat(Long.valueOf(j));
        return (chat != null && chat.gigagroup) || !ChatObject.isActionBanned(chat, 8) || ChatObject.hasAdminRights(chat);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0060, code lost:
    
        if (r9.messageOwner.send_state == 0) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0066, code lost:
    
        if (r9.messageOwner.id >= 0) goto L38;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean applyEntities() {
        TLRPC.TL_textWithEntities tL_textWithEntities;
        generateLinkDescription();
        ArrayList<TLRPC.MessageEntity> arrayList = (!this.translated || (tL_textWithEntities = this.messageOwner.translatedText) == null) ? this.messageOwner.entities : tL_textWithEntities.entities;
        spoilLoginCode();
        boolean z = false;
        if (!(this.messageOwner.send_state != 0 ? false : !arrayList.isEmpty())) {
            if (this.eventId == 0) {
                TLRPC.Message message = this.messageOwner;
                if (!(message instanceof TLRPC.TL_message_old)) {
                    if (!(message instanceof TLRPC.TL_message_old2)) {
                        if (!(message instanceof TLRPC.TL_message_old3)) {
                            if (!(message instanceof TLRPC.TL_message_old4)) {
                                if (!(message instanceof TLRPC.TL_messageForwarded_old)) {
                                    if (!(message instanceof TLRPC.TL_messageForwarded_old2)) {
                                        if (!(message instanceof TLRPC.TL_message_secret)) {
                                            if (!(getMedia(message) instanceof TLRPC.TL_messageMediaInvoice)) {
                                                if (isOut()) {
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            z = true;
        }
        if (z) {
            addLinks(isOutOwner(), this.messageText, true, true);
        } else {
            addPhoneLinks(this.messageText);
        }
        if (isYouTubeVideo()) {
            addUrlsByPattern(isOutOwner(), this.messageText, false, 3, ConnectionsManager.DEFAULT_DATACENTER_ID, false);
        } else {
            applyTimestampsHighlightForReplyMsg();
        }
        if (!(this.messageText instanceof Spannable)) {
            this.messageText = new SpannableStringBuilder(this.messageText);
        }
        return addEntitiesToText(this.messageText, z);
    }

    public static boolean canAutoplayAnimatedSticker(TLRPC.Document document) {
        return (isAnimatedStickerDocument(document, true) || isVideoStickerDocument(document)) && LiteMode.isEnabled(1);
    }

    public static boolean canCreateStripedThubms() {
        return SharedConfig.getDevicePerformanceClass() == 2;
    }

    public static boolean canDeleteMessage(int i, boolean z, TLRPC.Message message, TLRPC.Chat chat) {
        TLRPC.TL_chatAdminRights tL_chatAdminRights;
        TLRPC.Peer peer;
        if (message == null) {
            return false;
        }
        if (ChatObject.isChannelAndNotMegaGroup(chat) && (message.action instanceof TLRPC.TL_messageActionChatJoinedByRequest)) {
            return false;
        }
        if (message.id < 0) {
            return true;
        }
        if (chat == null && (peer = message.peer_id) != null && peer.channel_id != 0) {
            chat = MessagesController.getInstance(i).getChat(Long.valueOf(message.peer_id.channel_id));
        }
        if (!ChatObject.isChannel(chat)) {
            return z || isOut(message) || !ChatObject.isChannel(chat);
        }
        if (z && !chat.megagroup) {
            if (!chat.creator) {
                TLRPC.TL_chatAdminRights tL_chatAdminRights2 = chat.admin_rights;
                if (tL_chatAdminRights2 == null) {
                    return false;
                }
                if (!tL_chatAdminRights2.delete_messages && !message.out) {
                    return false;
                }
            }
            return true;
        }
        boolean z2 = message.out;
        if (z2 && (message instanceof TLRPC.TL_messageService)) {
            return message.id != 1 && ChatObject.canUserDoAdminAction(chat, 13);
        }
        if (!z) {
            if (message.id == 1) {
                return false;
            }
            if (!chat.creator && (((tL_chatAdminRights = chat.admin_rights) == null || (!tL_chatAdminRights.delete_messages && (!z2 || (!chat.megagroup && !tL_chatAdminRights.post_messages)))) && (!chat.megagroup || !z2))) {
                return false;
            }
        }
        return true;
    }

    public static boolean canEditMessage(int i, TLRPC.Message message, TLRPC.Chat chat, boolean z) {
        TLRPC.MessageAction messageAction;
        TLRPC.TL_chatAdminRights tL_chatAdminRights;
        TLRPC.TL_chatAdminRights tL_chatAdminRights2;
        TLRPC.TL_chatBannedRights tL_chatBannedRights;
        TLRPC.TL_chatAdminRights tL_chatAdminRights3;
        if (z && message.date < ConnectionsManager.getInstance(i).getCurrentTime() - 60) {
            return false;
        }
        if (chat != null && ((chat.left || chat.kicked) && (!chat.megagroup || !chat.has_link))) {
            return false;
        }
        TLRPC.MessageMedia media = getMedia(message);
        if (message != null && message.peer_id != null && ((media == null || (!isRoundVideoDocument(media.document) && !isStickerDocument(media.document) && !isAnimatedStickerDocument(media.document, true) && !isLocationMessage(message))) && (((messageAction = message.action) == null || (messageAction instanceof TLRPC.TL_messageActionEmpty)) && !isForwardedMessage(message) && message.via_bot_id == 0 && message.id >= 0))) {
            TLRPC.Peer peer = message.from_id;
            if (peer instanceof TLRPC.TL_peerUser) {
                long j = peer.user_id;
                if (j == message.peer_id.user_id && j == UserConfig.getInstance(i).getClientUserId() && !isLiveLocationMessage(message) && !(media instanceof TLRPC.TL_messageMediaContact)) {
                    return true;
                }
            }
            if (chat == null && message.peer_id.channel_id != 0 && (chat = MessagesController.getInstance(i).getChat(Long.valueOf(message.peer_id.channel_id))) == null) {
                return false;
            }
            if (media != null && !(media instanceof TLRPC.TL_messageMediaEmpty) && !(media instanceof TLRPC.TL_messageMediaPhoto) && !(media instanceof TLRPC.TL_messageMediaDocument) && !(media instanceof TLRPC.TL_messageMediaWebPage) && !(media instanceof TLRPC.TL_messageMediaPaidMedia)) {
                return false;
            }
            if (ChatObject.isChannel(chat) && !chat.megagroup && (chat.creator || ((tL_chatAdminRights3 = chat.admin_rights) != null && tL_chatAdminRights3.edit_messages))) {
                return true;
            }
            if (message.out && chat != null && chat.megagroup && (chat.creator || (((tL_chatAdminRights2 = chat.admin_rights) != null && tL_chatAdminRights2.pin_messages) || ((tL_chatBannedRights = chat.default_banned_rights) != null && !tL_chatBannedRights.pin_messages)))) {
                return true;
            }
            if (!z && Math.abs(message.date - ConnectionsManager.getInstance(i).getCurrentTime()) > MessagesController.getInstance(i).maxEditTime) {
                return false;
            }
            if (message.peer_id.channel_id == 0) {
                if (!message.out) {
                    TLRPC.Peer peer2 = message.from_id;
                    if (!(peer2 instanceof TLRPC.TL_peerUser) || peer2.user_id != UserConfig.getInstance(i).getClientUserId()) {
                        return false;
                    }
                }
                return (media instanceof TLRPC.TL_messageMediaPhoto) || !(!(media instanceof TLRPC.TL_messageMediaDocument) || isStickerMessage(message) || isAnimatedStickerMessage(message)) || (media instanceof TLRPC.TL_messageMediaEmpty) || (media instanceof TLRPC.TL_messageMediaWebPage) || (media instanceof TLRPC.TL_messageMediaPaidMedia) || media == null;
            }
            if (((chat != null && chat.megagroup && message.out) || (chat != null && !chat.megagroup && ((chat.creator || ((tL_chatAdminRights = chat.admin_rights) != null && (tL_chatAdminRights.edit_messages || (message.out && tL_chatAdminRights.post_messages)))) && message.post))) && ((media instanceof TLRPC.TL_messageMediaPhoto) || (((media instanceof TLRPC.TL_messageMediaDocument) && !isStickerMessage(message) && !isAnimatedStickerMessage(message)) || (media instanceof TLRPC.TL_messageMediaEmpty) || (media instanceof TLRPC.TL_messageMediaWebPage) || (media instanceof TLRPC.TL_messageMediaPaidMedia) || media == null))) {
                return true;
            }
        }
        return false;
    }

    public static boolean canEditMessageAnytime(int i, TLRPC.Message message, TLRPC.Chat chat) {
        TLRPC.MessageAction messageAction;
        TLRPC.TL_chatAdminRights tL_chatAdminRights;
        TLRPC.TL_chatBannedRights tL_chatBannedRights;
        TLRPC.TL_chatAdminRights tL_chatAdminRights2;
        if (message != null && message.peer_id != null && ((getMedia(message) == null || (!isRoundVideoDocument(getMedia(message).document) && !isStickerDocument(getMedia(message).document) && !isAnimatedStickerDocument(getMedia(message).document, true))) && (((messageAction = message.action) == null || (messageAction instanceof TLRPC.TL_messageActionEmpty)) && !isForwardedMessage(message) && message.via_bot_id == 0 && message.id >= 0))) {
            TLRPC.Peer peer = message.from_id;
            if (peer instanceof TLRPC.TL_peerUser) {
                long j = peer.user_id;
                if (j == message.peer_id.user_id && j == UserConfig.getInstance(i).getClientUserId() && !isLiveLocationMessage(message)) {
                    return true;
                }
            }
            if (chat == null && message.peer_id.channel_id != 0 && (chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(message.peer_id.channel_id))) == null) {
                return false;
            }
            if (ChatObject.isChannel(chat) && !chat.megagroup && (chat.creator || ((tL_chatAdminRights2 = chat.admin_rights) != null && tL_chatAdminRights2.edit_messages))) {
                return true;
            }
            if (message.out && chat != null && chat.megagroup && (chat.creator || (((tL_chatAdminRights = chat.admin_rights) != null && tL_chatAdminRights.pin_messages) || ((tL_chatBannedRights = chat.default_banned_rights) != null && !tL_chatBannedRights.pin_messages)))) {
                return true;
            }
        }
        return false;
    }

    public static boolean canEditMessageScheduleTime(int i, TLRPC.Message message, TLRPC.Chat chat) {
        if (message.video_processing_pending) {
            return false;
        }
        if (chat == null && message.peer_id.channel_id != 0 && (chat = MessagesController.getInstance(i).getChat(Long.valueOf(message.peer_id.channel_id))) == null) {
            return false;
        }
        if (!ChatObject.isChannel(chat) || chat.megagroup || chat.creator) {
            return true;
        }
        TLRPC.TL_chatAdminRights tL_chatAdminRights = chat.admin_rights;
        return tL_chatAdminRights != null && (tL_chatAdminRights.edit_messages || message.out);
    }

    public static boolean canPreviewDocument(TLRPC.Document document) {
        String str;
        if (document != null && (str = document.mime_type) != null) {
            if ((isDocumentHasThumb(document) && (str.equalsIgnoreCase("image/png") || str.equalsIgnoreCase("image/jpg") || str.equalsIgnoreCase("image/jpeg"))) || (Build.VERSION.SDK_INT >= 26 && str.equalsIgnoreCase("image/heic"))) {
                for (int i = 0; i < document.attributes.size(); i++) {
                    TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                        TLRPC.TL_documentAttributeImageSize tL_documentAttributeImageSize = (TLRPC.TL_documentAttributeImageSize) documentAttribute;
                        return tL_documentAttributeImageSize.w < 6000 && tL_documentAttributeImageSize.h < 6000;
                    }
                }
            } else if (BuildVars.DEBUG_PRIVATE_VERSION) {
                String documentFileName = FileLoader.getDocumentFileName(document);
                if ((documentFileName.startsWith("tg_secret_sticker") && documentFileName.endsWith("json")) || documentFileName.endsWith(".svg")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static CharSequence channelSpan() {
        if (channelSpan == null) {
            channelSpan = new SpannableStringBuilder("c");
            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.msg_folders_channels);
            coloredImageSpan.setScale(0.7f, 0.7f);
            ((SpannableStringBuilder) channelSpan).setSpan(coloredImageSpan, 0, 1, 33);
        }
        return channelSpan;
    }

    private void checkEmojiOnly(Integer num) {
        TextPaint textPaint;
        if (num == null || num.intValue() < 1 || this.messageOwner == null || hasNonEmojiEntities()) {
            CharSequence charSequence = this.messageText;
            AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spannable) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class);
            if (animatedEmojiSpanArr == null || animatedEmojiSpanArr.length <= 0) {
                this.totalAnimatedEmojiCount = 0;
                return;
            }
            this.totalAnimatedEmojiCount = animatedEmojiSpanArr.length;
            for (int i = 0; i < animatedEmojiSpanArr.length; i++) {
                animatedEmojiSpanArr[i].replaceFontMetrics(Theme.chat_msgTextPaint.getFontMetricsInt(), (int) (Theme.chat_msgTextPaint.getTextSize() + AndroidUtilities.dp(4.0f)), -1);
                animatedEmojiSpanArr[i].full = false;
            }
            return;
        }
        CharSequence charSequence2 = this.messageText;
        Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) ((Spannable) charSequence2).getSpans(0, charSequence2.length(), Emoji.EmojiSpan.class);
        CharSequence charSequence3 = this.messageText;
        AnimatedEmojiSpan[] animatedEmojiSpanArr2 = (AnimatedEmojiSpan[]) ((Spannable) charSequence3).getSpans(0, charSequence3.length(), AnimatedEmojiSpan.class);
        this.emojiOnlyCount = Math.max(num.intValue(), (emojiSpanArr == null ? 0 : emojiSpanArr.length) + (animatedEmojiSpanArr2 == null ? 0 : animatedEmojiSpanArr2.length));
        this.totalAnimatedEmojiCount = animatedEmojiSpanArr2 == null ? 0 : animatedEmojiSpanArr2.length;
        this.animatedEmojiCount = 0;
        if (animatedEmojiSpanArr2 != null) {
            for (AnimatedEmojiSpan animatedEmojiSpan : animatedEmojiSpanArr2) {
                if (!animatedEmojiSpan.standard) {
                    this.animatedEmojiCount++;
                }
            }
        }
        int i2 = this.emojiOnlyCount;
        boolean z = (i2 - (emojiSpanArr == null ? 0 : emojiSpanArr.length)) - (animatedEmojiSpanArr2 == null ? 0 : animatedEmojiSpanArr2.length) > 0;
        this.hasUnwrappedEmoji = z;
        if (i2 == 0 || z) {
            if (animatedEmojiSpanArr2 == null || animatedEmojiSpanArr2.length <= 0) {
                return;
            }
            for (int i3 = 0; i3 < animatedEmojiSpanArr2.length; i3++) {
                animatedEmojiSpanArr2[i3].replaceFontMetrics(Theme.chat_msgTextPaint.getFontMetricsInt(), (int) (Theme.chat_msgTextPaint.getTextSize() + AndroidUtilities.dp(4.0f)), -1);
                animatedEmojiSpanArr2[i3].full = false;
            }
            return;
        }
        int i4 = this.animatedEmojiCount;
        boolean z2 = i2 == i4;
        int i5 = 2;
        switch (Math.max(i2, i4)) {
            case 0:
            case 1:
            case 2:
                TextPaint[] textPaintArr = Theme.chat_msgTextPaintEmoji;
                textPaint = z2 ? textPaintArr[0] : textPaintArr[2];
                i5 = 1;
                break;
            case 3:
                TextPaint[] textPaintArr2 = Theme.chat_msgTextPaintEmoji;
                textPaint = z2 ? textPaintArr2[1] : textPaintArr2[3];
                i5 = 1;
                break;
            case 4:
                TextPaint[] textPaintArr3 = Theme.chat_msgTextPaintEmoji;
                textPaint = z2 ? textPaintArr3[2] : textPaintArr3[4];
                i5 = 1;
                break;
            case 5:
                TextPaint[] textPaintArr4 = Theme.chat_msgTextPaintEmoji;
                if (z2) {
                    textPaint = textPaintArr4[3];
                    break;
                } else {
                    textPaint = textPaintArr4[5];
                    break;
                }
            case 6:
                TextPaint[] textPaintArr5 = Theme.chat_msgTextPaintEmoji;
                if (z2) {
                    textPaint = textPaintArr5[4];
                    break;
                } else {
                    textPaint = textPaintArr5[5];
                    break;
                }
            default:
                int i6 = this.emojiOnlyCount > 9 ? 0 : -1;
                textPaint = Theme.chat_msgTextPaintEmoji[5];
                i5 = i6;
                break;
        }
        int textSize = (int) (textPaint.getTextSize() + AndroidUtilities.dp(4.0f));
        if (emojiSpanArr != null && emojiSpanArr.length > 0) {
            for (Emoji.EmojiSpan emojiSpan : emojiSpanArr) {
                emojiSpan.replaceFontMetrics(textPaint.getFontMetricsInt(), textSize);
            }
        }
        if (animatedEmojiSpanArr2 == null || animatedEmojiSpanArr2.length <= 0) {
            return;
        }
        for (int i7 = 0; i7 < animatedEmojiSpanArr2.length; i7++) {
            animatedEmojiSpanArr2[i7].replaceFontMetrics(textPaint.getFontMetricsInt(), textSize, i5);
            animatedEmojiSpanArr2[i7].full = true;
        }
    }

    private void checkEmojiOnly(int[] iArr) {
        checkEmojiOnly(iArr == null ? null : Integer.valueOf(iArr[0]));
    }

    public static boolean containsUrls(CharSequence charSequence) {
        if (charSequence != null && charSequence.length() >= 2 && charSequence.length() <= 20480) {
            int length = charSequence.length();
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            char c = 0;
            while (i < length) {
                char charAt = charSequence.charAt(i);
                if (charAt >= '0' && charAt <= '9') {
                    i2++;
                    if (i2 >= 6) {
                        return true;
                    }
                    i3 = 0;
                    i4 = 0;
                } else if (charAt == ' ' || i2 <= 0) {
                    i2 = 0;
                }
                if ((charAt != '@' && charAt != '#' && charAt != '/' && charAt != '$') || i != 0) {
                    if (i != 0) {
                        int i5 = i - 1;
                        if (charSequence.charAt(i5) != ' ') {
                            if (charSequence.charAt(i5) == '\n') {
                            }
                        }
                    }
                    if (charAt == ':') {
                        if (i3 == 0) {
                            i3 = 1;
                        }
                        i3 = 0;
                    } else if (charAt != '/') {
                        if (charAt == '.') {
                            if (i4 == 0 && c != ' ') {
                                i4++;
                            }
                        } else if (charAt != ' ' && c == '.' && i4 == 1) {
                            return true;
                        }
                        i4 = 0;
                    } else {
                        if (i3 == 2) {
                            return true;
                        }
                        if (i3 == 1) {
                            i3++;
                        }
                        i3 = 0;
                    }
                    i++;
                    c = charAt;
                }
                return true;
            }
        }
        return false;
    }

    private void createDateArray(int i, TLRPC.TL_channelAdminLogEvent tL_channelAdminLogEvent, ArrayList<MessageObject> arrayList, HashMap<String, ArrayList<MessageObject>> hashMap, boolean z) {
        if (hashMap.get(this.dateKey) == null) {
            hashMap.put(this.dateKey, new ArrayList<>());
            TLRPC.TL_message tL_message = new TLRPC.TL_message();
            tL_message.message = LocaleController.formatDateChat(tL_channelAdminLogEvent.date);
            tL_message.id = 0;
            tL_message.date = tL_channelAdminLogEvent.date;
            MessageObject messageObject = new MessageObject(i, tL_message, false, false);
            messageObject.type = 10;
            messageObject.contentType = 1;
            messageObject.isDateObject = true;
            if (z) {
                arrayList.add(0, messageObject);
            } else {
                arrayList.add(messageObject);
            }
        }
    }

    private void createPathThumb() {
        TLRPC.Document document = getDocument();
        if (document == null) {
            return;
        }
        this.pathThumb = DocumentObject.getSvgThumb(document, Theme.key_chat_serviceBackground, 1.0f);
    }

    public static void cutIntoRanges(CharSequence charSequence, ArrayList<TextRange> arrayList) {
        String str;
        int i;
        if (charSequence == null) {
            return;
        }
        if (!(charSequence instanceof Spanned)) {
            arrayList.add(new TextRange(0, charSequence.length()));
            return;
        }
        TreeSet treeSet = new TreeSet();
        HashMap hashMap = new HashMap();
        Spanned spanned = (Spanned) charSequence;
        QuoteSpan.QuoteStyleSpan[] quoteStyleSpanArr = (QuoteSpan.QuoteStyleSpan[]) spanned.getSpans(0, spanned.length(), QuoteSpan.QuoteStyleSpan.class);
        for (int i2 = 0; i2 < quoteStyleSpanArr.length; i2++) {
            QuoteSpan.QuoteStyleSpan quoteStyleSpan = quoteStyleSpanArr[i2];
            quoteStyleSpan.span.adaptLineHeight = false;
            int spanStart = spanned.getSpanStart(quoteStyleSpan);
            int spanEnd = spanned.getSpanEnd(quoteStyleSpanArr[i2]);
            treeSet.add(Integer.valueOf(spanStart));
            hashMap.put(Integer.valueOf(spanStart), Integer.valueOf((hashMap.containsKey(Integer.valueOf(spanStart)) ? ((Integer) hashMap.get(Integer.valueOf(spanStart))).intValue() : 0) | (quoteStyleSpanArr[i2].span.isCollapsing ? 16 : 1)));
            treeSet.add(Integer.valueOf(spanEnd));
            hashMap.put(Integer.valueOf(spanEnd), Integer.valueOf((hashMap.containsKey(Integer.valueOf(spanEnd)) ? ((Integer) hashMap.get(Integer.valueOf(spanEnd))).intValue() : 0) | 2));
        }
        Iterator it = treeSet.iterator();
        while (it.hasNext()) {
            Integer num = (Integer) it.next();
            int intValue = num.intValue();
            if (intValue >= 0 && intValue < spanned.length() && hashMap.containsKey(num)) {
                int intValue2 = ((Integer) hashMap.get(num)).intValue();
                if ((intValue2 & 1) != 0 && (intValue2 & 2) != 0 && spanned.charAt(intValue) != '\n' && (intValue - 1 <= 0 || spanned.charAt(i) != '\n')) {
                    it.remove();
                    hashMap.remove(num);
                }
            }
        }
        CodeHighlighting.Span[] spanArr = (CodeHighlighting.Span[]) spanned.getSpans(0, spanned.length(), CodeHighlighting.Span.class);
        for (int i3 = 0; i3 < spanArr.length; i3++) {
            int spanStart2 = spanned.getSpanStart(spanArr[i3]);
            int spanEnd2 = spanned.getSpanEnd(spanArr[i3]);
            treeSet.add(Integer.valueOf(spanStart2));
            hashMap.put(Integer.valueOf(spanStart2), Integer.valueOf((hashMap.containsKey(Integer.valueOf(spanStart2)) ? ((Integer) hashMap.get(Integer.valueOf(spanStart2))).intValue() : 0) | 4));
            treeSet.add(Integer.valueOf(spanEnd2));
            hashMap.put(Integer.valueOf(spanEnd2), Integer.valueOf((hashMap.containsKey(Integer.valueOf(spanEnd2)) ? ((Integer) hashMap.get(Integer.valueOf(spanEnd2))).intValue() : 0) | 8));
        }
        Iterator it2 = treeSet.iterator();
        boolean z = false;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (it2.hasNext()) {
            Integer num2 = (Integer) it2.next();
            int intValue3 = num2.intValue();
            int intValue4 = ((Integer) hashMap.get(num2)).intValue();
            if (i6 != intValue3) {
                int i8 = intValue3 - 1;
                if (i8 >= 0 && i8 < charSequence.length() && charSequence.charAt(i8) == '\n') {
                    intValue3--;
                }
                int i9 = intValue3;
                if ((intValue4 & 8) == 0 || i5 >= spanArr.length) {
                    str = null;
                } else {
                    str = spanArr[i5].lng;
                    i5++;
                }
                int i10 = i5;
                arrayList.add(new TextRange(i6, i9, i4 > 0, i7 > 0, z, str));
                i6 = i9 + 1;
                if (i6 >= charSequence.length() || charSequence.charAt(i9) != '\n') {
                    i6 = i9;
                }
                i5 = i10;
            }
            if ((intValue4 & 2) != 0) {
                i4--;
            }
            if ((intValue4 & 1) != 0 || (intValue4 & 16) != 0) {
                i4++;
                z = (intValue4 & 16) != 0;
            }
            if ((intValue4 & 8) != 0) {
                i7--;
            }
            if ((intValue4 & 4) != 0) {
                i7++;
            }
        }
        if (i6 < charSequence.length()) {
            arrayList.add(new TextRange(i6, charSequence.length(), i4 > 0, i7 > 0, z, null));
        }
    }

    public static boolean expandedQuotesEquals(HashSet<Integer> hashSet, HashSet<Integer> hashSet2) {
        if (hashSet == null && hashSet2 == null) {
            return true;
        }
        return (hashSet == null ? 0 : hashSet.size()) == (hashSet2 == null ? 0 : hashSet2.size()) && hashSet != null && hashSet.equals(hashSet2);
    }

    public static String findAnimatedEmojiEmoticon(TLRPC.Document document) {
        return findAnimatedEmojiEmoticon(document, "😀");
    }

    public static String findAnimatedEmojiEmoticon(TLRPC.Document document, String str) {
        return findAnimatedEmojiEmoticon(document, str, null);
    }

    public static String findAnimatedEmojiEmoticon(TLRPC.Document document, String str, Integer num) {
        if (document == null) {
            return str;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if ((documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji) || (documentAttribute instanceof TLRPC.TL_documentAttributeSticker)) {
                if (num != null) {
                    TLRPC.TL_messages_stickerSet stickerSet = MediaDataController.getInstance(num.intValue()).getStickerSet(documentAttribute.stickerset, true);
                    StringBuilder sb = new StringBuilder("");
                    if (stickerSet != null && stickerSet.packs != null) {
                        for (int i2 = 0; i2 < stickerSet.packs.size(); i2++) {
                            TLRPC.TL_stickerPack tL_stickerPack = stickerSet.packs.get(i2);
                            if (tL_stickerPack.documents.contains(Long.valueOf(document.id))) {
                                sb.append(tL_stickerPack.emoticon);
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(sb)) {
                        return sb.toString();
                    }
                }
                return documentAttribute.alt;
            }
        }
        return str;
    }

    public static int findQuoteStart(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return -1;
        }
        if (i == -1) {
            return str.indexOf(str2);
        }
        if (str2.length() + i < str.length() && str.startsWith(str2, i)) {
            return i;
        }
        int indexOf = str.indexOf(str2, i);
        int lastIndexOf = str.lastIndexOf(str2, i);
        return indexOf == -1 ? lastIndexOf : (lastIndexOf != -1 && indexOf - i >= i - lastIndexOf) ? lastIndexOf : indexOf;
    }

    public static ArrayList<String> findStickerEmoticons(TLRPC.Document document, Integer num) {
        if (document == null) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if ((documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji) || (documentAttribute instanceof TLRPC.TL_documentAttributeSticker)) {
                if (num != null) {
                    TLRPC.TL_messages_stickerSet stickerSet = MediaDataController.getInstance(num.intValue()).getStickerSet(documentAttribute.stickerset, true);
                    if (stickerSet != null && stickerSet.packs != null) {
                        for (int i2 = 0; i2 < stickerSet.packs.size(); i2++) {
                            TLRPC.TL_stickerPack tL_stickerPack = stickerSet.packs.get(i2);
                            if (tL_stickerPack.documents.contains(Long.valueOf(document.id)) && Emoji.getEmojiDrawable(tL_stickerPack.emoticon) != null) {
                                arrayList.add(tL_stickerPack.emoticon);
                            }
                        }
                    }
                    if (!arrayList.isEmpty()) {
                        return arrayList;
                    }
                }
                if (!TextUtils.isEmpty(documentAttribute.alt) && Emoji.getEmojiDrawable(documentAttribute.alt) != null) {
                    arrayList.add(documentAttribute.alt);
                    return arrayList;
                }
            }
        }
        return null;
    }

    public static void fixMessagePeer(ArrayList<TLRPC.Message> arrayList, long j) {
        if (arrayList == null || arrayList.isEmpty() || j == 0) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC.Message message = arrayList.get(i);
            if (message instanceof TLRPC.TL_messageEmpty) {
                TLRPC.TL_peerChannel tL_peerChannel = new TLRPC.TL_peerChannel();
                message.peer_id = tL_peerChannel;
                tL_peerChannel.channel_id = j;
            }
        }
    }

    public static long getChannelId(TLRPC.Message message) {
        TLRPC.Peer peer = message.peer_id;
        if (peer != null) {
            return peer.channel_id;
        }
        return 0L;
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0017  */
    /* JADX WARN: Removed duplicated region for block: B:8:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TLRPC.Chat getChat(AbstractMap<Long, TLRPC.Chat> abstractMap, LongSparseArray longSparseArray, long j) {
        TLRPC.Chat chat;
        Object obj;
        if (abstractMap != null) {
            obj = abstractMap.get(Long.valueOf(j));
        } else {
            if (longSparseArray == null) {
                chat = null;
                return chat != null ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j)) : chat;
            }
            obj = longSparseArray.get(j);
        }
        chat = (TLRPC.Chat) obj;
        if (chat != null) {
        }
    }

    public static long getChatId(TLRPC.Message message) {
        if (message == null) {
            return 0L;
        }
        TLRPC.Peer peer = message.peer_id;
        if (peer instanceof TLRPC.TL_peerChat) {
            return peer.chat_id;
        }
        if (peer instanceof TLRPC.TL_peerChannel) {
            return peer.channel_id;
        }
        return 0L;
    }

    public static long getDialogId(TLRPC.Message message) {
        TLRPC.Peer peer;
        long j;
        if (message.dialog_id == 0 && (peer = message.peer_id) != null) {
            long j2 = peer.chat_id;
            if (j2 != 0) {
                j = -j2;
            } else {
                long j3 = peer.channel_id;
                if (j3 != 0) {
                    j = -j3;
                } else {
                    j = ((message.from_id == null || isOut(message)) ? message.peer_id : message.from_id).user_id;
                }
            }
            message.dialog_id = j;
        }
        return message.dialog_id;
    }

    public static TLRPC.Document getDocument(TLRPC.Message message) {
        TLRPC.MessageMedia messageMedia;
        TLRPC.Document document;
        if (getMedia(message) instanceof TLRPC.TL_messageMediaWebPage) {
            return getMedia(message).webpage.document;
        }
        if (getMedia(message) instanceof TLRPC.TL_messageMediaGame) {
            return getMedia(message).game.document;
        }
        if (getMedia(message) instanceof TLRPC.TL_messageMediaStory) {
            TL_stories.StoryItem storyItem = ((TLRPC.TL_messageMediaStory) getMedia(message)).storyItem;
            if (storyItem != null && (messageMedia = storyItem.media) != null && (document = messageMedia.document) != null) {
                return document;
            }
        } else if (getMedia(message) instanceof TLRPC.TL_messageMediaPaidMedia) {
            TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) getMedia(message);
            if (tL_messageMediaPaidMedia.extended_media.size() == 1 && (tL_messageMediaPaidMedia.extended_media.get(0) instanceof TLRPC.TL_messageExtendedMedia)) {
                return ((TLRPC.TL_messageExtendedMedia) tL_messageMediaPaidMedia.extended_media.get(0)).media.document;
            }
        }
        if (getMedia(message) != null) {
            return getMedia(message).document;
        }
        return null;
    }

    public static double getDocumentDuration(TLRPC.Document document) {
        int i;
        if (document == null) {
            return 0.0d;
        }
        int size = document.attributes.size();
        while (i < size) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            i = ((documentAttribute instanceof TLRPC.TL_documentAttributeVideo) || (documentAttribute instanceof TLRPC.TL_documentAttributeAudio)) ? 0 : i + 1;
            return documentAttribute.duration;
        }
        return 0.0d;
    }

    public static TLRPC.VideoSize getDocumentVideoThumb(TLRPC.Document document) {
        if (document == null || document.video_thumbs.isEmpty()) {
            return null;
        }
        return document.video_thumbs.get(0);
    }

    private TLRPC.Document getDocumentWithId(TLRPC.WebPage webPage, long j) {
        if (webPage != null && webPage.cached_page != null) {
            TLRPC.Document document = webPage.document;
            if (document != null && document.id == j) {
                return document;
            }
            for (int i = 0; i < webPage.cached_page.documents.size(); i++) {
                TLRPC.Document document2 = webPage.cached_page.documents.get(i);
                if (document2.id == j) {
                    return document2;
                }
            }
        }
        return null;
    }

    public static String getFileName(TLRPC.Document document) {
        return FileLoader.getAttachFileName(document);
    }

    public static String getFileName(TLRPC.Message message) {
        TLRPC.Document document;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        if (getMedia(message) instanceof TLRPC.TL_messageMediaDocument) {
            document = getDocument(message);
        } else {
            if (getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) {
                ArrayList<TLRPC.PhotoSize> arrayList = getMedia(message).photo.sizes;
                return (arrayList.size() <= 0 || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) == null) ? "" : FileLoader.getAttachFileName(closestPhotoSizeWithSize);
            }
            if (!(getMedia(message) instanceof TLRPC.TL_messageMediaWebPage) || getMedia(message).webpage == null) {
                return "";
            }
            document = getMedia(message).webpage.document;
        }
        return getFileName(document);
    }

    public static String getFileName(TLRPC.MessageMedia messageMedia) {
        TLRPC.WebPage webPage;
        TLRPC.Document document;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
            document = messageMedia.document;
        } else {
            if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
                ArrayList<TLRPC.PhotoSize> arrayList = messageMedia.photo.sizes;
                return (arrayList.size() <= 0 || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) == null) ? "" : FileLoader.getAttachFileName(closestPhotoSizeWithSize);
            }
            if (!(messageMedia instanceof TLRPC.TL_messageMediaWebPage) || (webPage = messageMedia.webpage) == null) {
                return "";
            }
            document = webPage.document;
        }
        return FileLoader.getAttachFileName(document);
    }

    public static long getFromChatId(TLRPC.Message message) {
        return getPeerId(message.from_id);
    }

    public static int getInlineResultDuration(TLRPC.BotInlineResult botInlineResult) {
        int webDocumentDuration = (int) getWebDocumentDuration(botInlineResult.content);
        return webDocumentDuration == 0 ? (int) getWebDocumentDuration(botInlineResult.thumb) : webDocumentDuration;
    }

    public static int[] getInlineResultWidthAndHeight(TLRPC.BotInlineResult botInlineResult) {
        int[] webDocumentWidthAndHeight = getWebDocumentWidthAndHeight(botInlineResult.content);
        if (webDocumentWidthAndHeight != null) {
            return webDocumentWidthAndHeight;
        }
        int[] webDocumentWidthAndHeight2 = getWebDocumentWidthAndHeight(botInlineResult.thumb);
        return webDocumentWidthAndHeight2 == null ? new int[]{0, 0} : webDocumentWidthAndHeight2;
    }

    public static TLRPC.InputStickerSet getInputStickerSet(TLRPC.Document document) {
        if (document == null) {
            return null;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if ((documentAttribute instanceof TLRPC.TL_documentAttributeSticker) || (documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji)) {
                TLRPC.InputStickerSet inputStickerSet = documentAttribute.stickerset;
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return null;
                }
                return inputStickerSet;
            }
        }
        return null;
    }

    public static TLRPC.InputStickerSet getInputStickerSet(TLRPC.Message message) {
        TLRPC.Document document = getDocument(message);
        if (document != null) {
            return getInputStickerSet(document);
        }
        return null;
    }

    public static TLRPC.MessageMedia getMedia(MessageObject messageObject) {
        TLRPC.Message message;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return null;
        }
        TLRPC.MessageMedia messageMedia = messageObject.sponsoredMedia;
        return messageMedia != null ? messageMedia : getMedia(message);
    }

    public static TLRPC.MessageMedia getMedia(TLRPC.Message message) {
        TLRPC.MessageMedia messageMedia = message.media;
        return (messageMedia == null || (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) || messageMedia.extended_media.isEmpty() || !(message.media.extended_media.get(0) instanceof TLRPC.TL_messageExtendedMedia)) ? message.media : ((TLRPC.TL_messageExtendedMedia) message.media.extended_media.get(0)).media;
    }

    public static long getMediaSize(TLRPC.MessageMedia messageMedia) {
        TLRPC.WebPage webPage;
        TLRPC.Document document = (!(messageMedia instanceof TLRPC.TL_messageMediaWebPage) || (webPage = messageMedia.webpage) == null) ? messageMedia instanceof TLRPC.TL_messageMediaGame ? messageMedia.game.document : messageMedia != null ? messageMedia.document : null : webPage.document;
        if (document != null) {
            return document.size;
        }
        return 0L;
    }

    private MessageObject getMessageObjectForBlock(TLRPC.WebPage webPage, TLRPC.PageBlock pageBlock) {
        TLRPC.TL_message tL_message;
        if (pageBlock instanceof TLRPC.TL_pageBlockPhoto) {
            TLRPC.Photo photoWithId = getPhotoWithId(webPage, ((TLRPC.TL_pageBlockPhoto) pageBlock).photo_id);
            if (photoWithId == webPage.photo) {
                return this;
            }
            tL_message = new TLRPC.TL_message();
            TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = new TLRPC.TL_messageMediaPhoto();
            tL_message.media = tL_messageMediaPhoto;
            tL_messageMediaPhoto.photo = photoWithId;
        } else if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
            TLRPC.TL_pageBlockVideo tL_pageBlockVideo = (TLRPC.TL_pageBlockVideo) pageBlock;
            if (getDocumentWithId(webPage, tL_pageBlockVideo.video_id) == webPage.document) {
                return this;
            }
            TLRPC.TL_message tL_message2 = new TLRPC.TL_message();
            TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
            tL_message2.media = tL_messageMediaDocument;
            tL_messageMediaDocument.document = getDocumentWithId(webPage, tL_pageBlockVideo.video_id);
            tL_message = tL_message2;
        } else {
            tL_message = null;
        }
        tL_message.message = "";
        tL_message.realId = getId();
        tL_message.id = Utilities.random.nextInt();
        TLRPC.Message message = this.messageOwner;
        tL_message.date = message.date;
        tL_message.peer_id = message.peer_id;
        tL_message.out = message.out;
        tL_message.from_id = message.from_id;
        return new MessageObject(this.currentAccount, tL_message, false, true);
    }

    public static long getMessageSize(TLRPC.Message message) {
        return getMediaSize(getMedia(message));
    }

    public static long getObjectPeerId(TLObject tLObject) {
        if (tLObject == null) {
            return 0L;
        }
        if (tLObject instanceof TLRPC.Chat) {
            return -((TLRPC.Chat) tLObject).id;
        }
        if (tLObject instanceof TLRPC.User) {
            return ((TLRPC.User) tLObject).id;
        }
        return 0L;
    }

    private int getParentWidth() {
        int i;
        return (!this.preview || (i = this.parentWidth) <= 0) ? AndroidUtilities.displaySize.x : i;
    }

    public static long getPeerId(TLRPC.Peer peer) {
        long j;
        if (peer == null) {
            return 0L;
        }
        if (peer instanceof TLRPC.TL_peerChat) {
            j = peer.chat_id;
        } else {
            if (!(peer instanceof TLRPC.TL_peerChannel)) {
                return peer.user_id;
            }
            j = peer.channel_id;
        }
        return -j;
    }

    public static String getPeerObjectName(TLObject tLObject) {
        return tLObject instanceof TLRPC.User ? UserObject.getUserName((TLRPC.User) tLObject) : tLObject instanceof TLRPC.Chat ? ((TLRPC.Chat) tLObject).title : "DELETED";
    }

    public static TLRPC.Photo getPhoto(TLRPC.Message message) {
        if (getMedia(message) instanceof TLRPC.TL_messageMediaWebPage) {
            return getMedia(message).webpage.photo;
        }
        if (getMedia(message) != null) {
            return getMedia(message).photo;
        }
        return null;
    }

    private TLRPC.Photo getPhotoWithId(TLRPC.WebPage webPage, long j) {
        if (webPage != null && webPage.cached_page != null) {
            TLRPC.Photo photo = webPage.photo;
            if (photo != null && photo.id == j) {
                return photo;
            }
            for (int i = 0; i < webPage.cached_page.photos.size(); i++) {
                TLRPC.Photo photo2 = webPage.cached_page.photos.get(i);
                if (photo2.id == j) {
                    return photo2;
                }
            }
        }
        return null;
    }

    public static TLRPC.VideoSize getPremiumStickerAnimation(TLRPC.Document document) {
        if (document != null && document.thumbs != null) {
            for (int i = 0; i < document.video_thumbs.size(); i++) {
                if ("f".equals(document.video_thumbs.get(i).type)) {
                    return document.video_thumbs.get(i);
                }
            }
        }
        return null;
    }

    public static int getQuickReplyId(int i, TLRPC.Message message) {
        QuickRepliesController.QuickReply findReply;
        if (message == null) {
            return 0;
        }
        if ((message.flags & 1073741824) != 0) {
            return message.quick_reply_shortcut_id;
        }
        TLRPC.InputQuickReplyShortcut inputQuickReplyShortcut = message.quick_reply_shortcut;
        if (inputQuickReplyShortcut instanceof TLRPC.TL_inputQuickReplyShortcutId) {
            return ((TLRPC.TL_inputQuickReplyShortcutId) inputQuickReplyShortcut).shortcut_id;
        }
        String quickReplyName = getQuickReplyName(message);
        if (quickReplyName == null || (findReply = QuickRepliesController.getInstance(i).findReply(quickReplyName)) == null) {
            return 0;
        }
        return findReply.id;
    }

    public static int getQuickReplyId(TLRPC.Message message) {
        if (message == null) {
            return 0;
        }
        if ((message.flags & 1073741824) != 0) {
            return message.quick_reply_shortcut_id;
        }
        TLRPC.InputQuickReplyShortcut inputQuickReplyShortcut = message.quick_reply_shortcut;
        if (inputQuickReplyShortcut instanceof TLRPC.TL_inputQuickReplyShortcutId) {
            return ((TLRPC.TL_inputQuickReplyShortcutId) inputQuickReplyShortcut).shortcut_id;
        }
        return 0;
    }

    public static String getQuickReplyName(TLRPC.Message message) {
        if (message == null) {
            return null;
        }
        TLRPC.InputQuickReplyShortcut inputQuickReplyShortcut = message.quick_reply_shortcut;
        if (inputQuickReplyShortcut instanceof TLRPC.TL_inputQuickReplyShortcut) {
            return ((TLRPC.TL_inputQuickReplyShortcut) inputQuickReplyShortcut).shortcut;
        }
        return null;
    }

    public static long getReplyToDialogId(TLRPC.Message message) {
        TLRPC.MessageReplyHeader messageReplyHeader = message.reply_to;
        if (messageReplyHeader == null) {
            return 0L;
        }
        TLRPC.Peer peer = messageReplyHeader.reply_to_peer_id;
        return peer != null ? getPeerId(peer) : getDialogId(message);
    }

    public static long getSavedDialogId(long j, TLRPC.Message message) {
        TLRPC.Peer peer;
        TLRPC.Peer peer2 = message.saved_peer_id;
        if (peer2 == null) {
            if (message.from_id.user_id != j) {
                return 0L;
            }
            TLRPC.MessageFwdHeader messageFwdHeader = message.fwd_from;
            return (messageFwdHeader == null || (peer = messageFwdHeader.saved_from_peer) == null) ? ((messageFwdHeader == null || messageFwdHeader.from_id == null) && messageFwdHeader != null) ? UserObject.ANONYMOUS : j : DialogObject.getPeerDialogId(peer);
        }
        long j2 = peer2.chat_id;
        if (j2 != 0) {
            return -j2;
        }
        long j3 = peer2.channel_id;
        return j3 != 0 ? -j3 : peer2.user_id;
    }

    public static TLRPC.Peer getSavedDialogPeer(long j, TLRPC.Message message) {
        TLRPC.Peer peer;
        TLRPC.TL_peerUser tL_peerUser;
        TLRPC.Peer peer2;
        TLRPC.Peer peer3 = message.saved_peer_id;
        if (peer3 != null) {
            return peer3;
        }
        TLRPC.Peer peer4 = message.peer_id;
        if (peer4 == null || peer4.user_id != j || (peer = message.from_id) == null || peer.user_id != j) {
            return null;
        }
        TLRPC.MessageFwdHeader messageFwdHeader = message.fwd_from;
        if (messageFwdHeader != null && (peer2 = messageFwdHeader.saved_from_peer) != null) {
            return peer2;
        }
        if (messageFwdHeader != null && messageFwdHeader.from_id != null) {
            tL_peerUser = new TLRPC.TL_peerUser();
        } else {
            if (messageFwdHeader != null) {
                TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
                tL_peerUser2.user_id = UserObject.ANONYMOUS;
                return tL_peerUser2;
            }
            tL_peerUser = new TLRPC.TL_peerUser();
        }
        tL_peerUser.user_id = j;
        return tL_peerUser;
    }

    public static long getStickerSetId(TLRPC.Document document) {
        if (document == null) {
            return -1L;
        }
        for (int i = 0; i < document.attributes.size(); i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                TLRPC.InputStickerSet inputStickerSet = documentAttribute.stickerset;
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return -1L;
                }
                return inputStickerSet.id;
            }
        }
        return -1L;
    }

    public static String getStickerSetName(TLRPC.Document document) {
        if (document == null) {
            return null;
        }
        for (int i = 0; i < document.attributes.size(); i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                TLRPC.InputStickerSet inputStickerSet = documentAttribute.stickerset;
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return null;
                }
                return inputStickerSet.short_name;
            }
        }
        return null;
    }

    private CharSequence getStringFrom(TLRPC.ChatReactions chatReactions) {
        int i;
        if (chatReactions instanceof TLRPC.TL_chatReactionsAll) {
            i = R.string.AllReactions;
        } else {
            if (chatReactions instanceof TLRPC.TL_chatReactionsSome) {
                TLRPC.TL_chatReactionsSome tL_chatReactionsSome = (TLRPC.TL_chatReactionsSome) chatReactions;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                for (int i2 = 0; i2 < tL_chatReactionsSome.reactions.size(); i2++) {
                    if (i2 != 0) {
                        spannableStringBuilder.append((CharSequence) " ");
                    }
                    spannableStringBuilder.append(Emoji.replaceEmoji(ReactionsUtils.reactionToCharSequence(tL_chatReactionsSome.reactions.get(i2)), null, false));
                }
                return spannableStringBuilder;
            }
            i = R.string.NoReactions;
        }
        return LocaleController.getString(i);
    }

    private static long getTopicId(int i, TLRPC.Message message) {
        return getTopicId(i, message, false);
    }

    public static long getTopicId(int i, TLRPC.Message message, boolean z) {
        int i2;
        long clientUserId = UserConfig.getInstance(i).getClientUserId();
        if ((message.flags & 1073741824) != 0 && DialogObject.getPeerDialogId(message.peer_id) == clientUserId) {
            i2 = message.quick_reply_shortcut_id;
        } else {
            if (!z && i >= 0 && DialogObject.getPeerDialogId(message.peer_id) == clientUserId) {
                return getSavedDialogId(clientUserId, message);
            }
            TLRPC.MessageAction messageAction = message.action;
            if (!(messageAction instanceof TLRPC.TL_messageActionTopicCreate)) {
                TLRPC.MessageReplyHeader messageReplyHeader = message.reply_to;
                if (messageReplyHeader == null || !messageReplyHeader.forum_topic) {
                    return z ? 1L : 0L;
                }
                if (!(message instanceof TLRPC.TL_messageService) || (messageAction instanceof TLRPC.TL_messageActionPinMessage)) {
                    int i3 = messageReplyHeader.reply_to_top_id;
                    if (i3 == 0) {
                        i3 = messageReplyHeader.reply_to_msg_id;
                    }
                    return i3;
                }
                int i4 = messageReplyHeader.reply_to_msg_id;
                if (i4 == 0) {
                    i4 = messageReplyHeader.reply_to_top_id;
                }
                return i4;
            }
            i2 = message.id;
        }
        return i2;
    }

    private static long getTopicId(MessageObject messageObject) {
        if (messageObject == null) {
            return 0L;
        }
        return getTopicId(messageObject.currentAccount, messageObject.messageOwner, false);
    }

    public static int getUnreadFlags(TLRPC.Message message) {
        int i = !message.unread ? 1 : 0;
        return !message.media_unread ? i | 2 : i;
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0017  */
    /* JADX WARN: Removed duplicated region for block: B:8:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TLRPC.User getUser(AbstractMap<Long, TLRPC.User> abstractMap, LongSparseArray longSparseArray, long j) {
        TLRPC.User user;
        Object obj;
        if (abstractMap != null) {
            obj = abstractMap.get(Long.valueOf(j));
        } else {
            if (longSparseArray == null) {
                user = null;
                return user != null ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j)) : user;
            }
            obj = longSparseArray.get(j);
        }
        user = (TLRPC.User) obj;
        if (user != null) {
        }
    }

    private String getUserName(TLObject tLObject, ArrayList<TLRPC.MessageEntity> arrayList, int i) {
        String str;
        String publicUsername;
        long j;
        String str2;
        String str3;
        long j2;
        if (tLObject == null) {
            str2 = "";
            str3 = null;
            j2 = 0;
        } else {
            if (tLObject instanceof TLRPC.User) {
                TLRPC.User user = (TLRPC.User) tLObject;
                str = user.deleted ? LocaleController.getString(R.string.HiddenName) : ContactsController.formatName(user.first_name, user.last_name);
                publicUsername = UserObject.getPublicUsername(user);
                j = user.id;
            } else {
                TLRPC.Chat chat = (TLRPC.Chat) tLObject;
                str = chat.title;
                publicUsername = ChatObject.getPublicUsername(chat);
                j = -chat.id;
            }
            str2 = str;
            str3 = publicUsername;
            j2 = j;
        }
        if (i >= 0) {
            TLRPC.TL_messageEntityMentionName tL_messageEntityMentionName = new TLRPC.TL_messageEntityMentionName();
            tL_messageEntityMentionName.user_id = j2;
            tL_messageEntityMentionName.offset = i;
            tL_messageEntityMentionName.length = str2.length();
            arrayList.add(tL_messageEntityMentionName);
        }
        if (TextUtils.isEmpty(str3)) {
            return str2;
        }
        if (i >= 0) {
            TLRPC.TL_messageEntityMentionName tL_messageEntityMentionName2 = new TLRPC.TL_messageEntityMentionName();
            tL_messageEntityMentionName2.user_id = j2;
            tL_messageEntityMentionName2.offset = i + str2.length() + 2;
            tL_messageEntityMentionName2.length = str3.length() + 1;
            arrayList.add(tL_messageEntityMentionName2);
        }
        return String.format("%1$s (@%2$s)", str2, str3);
    }

    private String getUsernamesString(ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return LocaleController.getString(R.string.UsernameEmpty).toLowerCase();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            sb.append("@");
            sb.append(arrayList.get(i));
            if (i < arrayList.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static String getVideoCodec(TLRPC.Document document) {
        if (document == null) {
            return null;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                return ((TLRPC.TL_documentAttributeVideo) documentAttribute).video_codec;
            }
        }
        return null;
    }

    public static int getVideoHeight(TLRPC.Document document) {
        if (document == null) {
            return 0;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                return documentAttribute.h;
            }
        }
        return 0;
    }

    public static int getVideoWidth(TLRPC.Document document) {
        if (document == null) {
            return 0;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                return documentAttribute.w;
            }
        }
        return 0;
    }

    public static double getWebDocumentDuration(TLRPC.WebDocument webDocument) {
        int i;
        if (webDocument == null) {
            return 0.0d;
        }
        int size = webDocument.attributes.size();
        while (i < size) {
            TLRPC.DocumentAttribute documentAttribute = webDocument.attributes.get(i);
            i = ((documentAttribute instanceof TLRPC.TL_documentAttributeVideo) || (documentAttribute instanceof TLRPC.TL_documentAttributeAudio)) ? 0 : i + 1;
            return documentAttribute.duration;
        }
        return 0.0d;
    }

    public static int[] getWebDocumentWidthAndHeight(TLRPC.WebDocument webDocument) {
        if (webDocument == null) {
            return null;
        }
        int size = webDocument.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = webDocument.attributes.get(i);
            if ((documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) || (documentAttribute instanceof TLRPC.TL_documentAttributeVideo)) {
                return new int[]{documentAttribute.w, documentAttribute.h};
            }
        }
        return null;
    }

    public static CharSequence groupSpan() {
        if (groupSpan == null) {
            groupSpan = new SpannableStringBuilder(ImageLoader.AUTOPLAY_FILTER);
            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.msg_folders_groups);
            coloredImageSpan.setScale(0.7f, 0.7f);
            ((SpannableStringBuilder) groupSpan).setSpan(coloredImageSpan, 0, 1, 33);
        }
        return groupSpan;
    }

    private void handleFoundWords(ArrayList<String> arrayList, String[] strArr, boolean z) {
        handleFoundWords(arrayList, strArr, z, true);
    }

    private void handleFoundWords(ArrayList<String> arrayList, String[] strArr, boolean z, boolean z2) {
        TLRPC.Message message;
        TLRPC.MessageReplyHeader messageReplyHeader;
        boolean z3;
        if (arrayList.isEmpty()) {
            return;
        }
        boolean z4 = false;
        for (int i = 0; i < arrayList.size(); i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= strArr.length) {
                    break;
                }
                if (arrayList.get(i).contains(strArr[i2])) {
                    z4 = true;
                    break;
                }
                i2++;
            }
            if (z4) {
                break;
            }
        }
        if (z4) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                int i4 = 0;
                while (true) {
                    if (i4 >= strArr.length) {
                        z3 = false;
                        break;
                    } else {
                        if (arrayList.get(i3).contains(strArr[i4])) {
                            z3 = true;
                            break;
                        }
                        i4++;
                    }
                }
                if (!z3) {
                    arrayList.remove(i3);
                    i3--;
                }
                i3++;
            }
            if (arrayList.size() > 0) {
                Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda9
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$handleFoundWords$3;
                        lambda$handleFoundWords$3 = MessageObject.lambda$handleFoundWords$3((String) obj, (String) obj2);
                        return lambda$handleFoundWords$3;
                    }
                });
                String str = arrayList.get(0);
                arrayList.clear();
                arrayList.add(str);
            }
        }
        this.highlightedWords = arrayList;
        if (this.messageOwner.message != null) {
            applyEntities();
            CharSequence replaceMultipleCharSequence = AndroidUtilities.replaceMultipleCharSequence("\n", !TextUtils.isEmpty(this.caption) ? this.caption : this.messageText, " ");
            if (z && (message = this.messageOwner) != null && (messageReplyHeader = message.reply_to) != null && messageReplyHeader.quote_text != null) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.messageOwner.reply_to.quote_text);
                addEntitiesToText(spannableStringBuilder, this.messageOwner.reply_to.quote_entities, isOutOwner(), false, false, false);
                SpannableString spannableString = new SpannableString("q ");
                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_quote);
                coloredImageSpan.setOverrideColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
                spannableString.setSpan(coloredImageSpan, 0, 1, 33);
                replaceMultipleCharSequence = new SpannableStringBuilder(spannableString).append((CharSequence) spannableStringBuilder).append('\n').append(replaceMultipleCharSequence);
            }
            String charSequence = replaceMultipleCharSequence.toString();
            int length = charSequence.length();
            int indexOf = charSequence.toLowerCase().indexOf(arrayList.get(0));
            if (indexOf < 0) {
                indexOf = 0;
            }
            if (length > 120 && z2) {
                float f = 120;
                int max = Math.max(0, indexOf - ((int) (0.1f * f)));
                replaceMultipleCharSequence = replaceMultipleCharSequence.subSequence(max, Math.min(length, (indexOf - max) + indexOf + ((int) (f * 0.9f))));
            }
            this.messageTrimmedToHighlight = replaceMultipleCharSequence;
            this.messageTrimmedToHighlightCut = z2;
        }
    }

    private boolean hasNonEmojiEntities() {
        TLRPC.Message message = this.messageOwner;
        if (message != null && message.entities != null) {
            for (int i = 0; i < this.messageOwner.entities.size(); i++) {
                if (!(this.messageOwner.entities.get(i) instanceof TLRPC.TL_messageEntityCustomEmoji)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasUnreadReactions(TLRPC.Message message) {
        if (message == null) {
            return false;
        }
        return hasUnreadReactions(message.reactions);
    }

    public static boolean hasUnreadReactions(TLRPC.TL_messageReactions tL_messageReactions) {
        if (tL_messageReactions == null) {
            return false;
        }
        for (int i = 0; i < tL_messageReactions.recent_reactions.size(); i++) {
            if (tL_messageReactions.recent_reactions.get(i).unread) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnimatedEmoji(TLRPC.Document document) {
        if (document == null) {
            return false;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            if (document.attributes.get(i) instanceof TLRPC.TL_documentAttributeCustomEmoji) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnimatedStickerDocument(TLRPC.Document document) {
        return document != null && document.mime_type.equals("video/webm");
    }

    public static boolean isAnimatedStickerDocument(TLRPC.Document document, boolean z) {
        if (document != null && (("application/x-tgsticker".equals(document.mime_type) && !document.thumbs.isEmpty()) || "application/x-tgsdice".equals(document.mime_type))) {
            if (z) {
                return true;
            }
            int size = document.attributes.size();
            for (int i = 0; i < size; i++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                    return documentAttribute.stickerset instanceof TLRPC.TL_inputStickerSetShortName;
                }
                if (documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAnimatedStickerMessage(TLRPC.Message message) {
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(message.dialog_id);
        if ((!isEncryptedDialog || message.stickerVerified == 1) && getMedia(message) != null) {
            return isAnimatedStickerDocument(getMedia(message).document, !isEncryptedDialog || message.out);
        }
        return false;
    }

    public static boolean isContentUnread(TLRPC.Message message) {
        return message.media_unread;
    }

    public static boolean isDocumentHasAttachedStickers(TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                if (document.attributes.get(i) instanceof TLRPC.TL_documentAttributeHasStickers) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDocumentHasThumb(TLRPC.Document document) {
        if (document != null && !document.thumbs.isEmpty()) {
            int size = document.thumbs.size();
            for (int i = 0; i < size; i++) {
                TLRPC.PhotoSize photoSize = document.thumbs.get(i);
                if (photoSize != null && !(photoSize instanceof TLRPC.TL_photoSizeEmpty) && (!(photoSize.location instanceof TLRPC.TL_fileLocationUnavailable) || photoSize.bytes != null)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExtendedVideo(TLRPC.MessageExtendedMedia messageExtendedMedia) {
        if (!(messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia)) {
            return (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMediaPreview) && (((TLRPC.TL_messageExtendedMediaPreview) messageExtendedMedia).flags & 4) != 0;
        }
        TLRPC.MessageMedia messageMedia = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media;
        return (messageMedia instanceof TLRPC.TL_messageMediaDocument) && isVideoDocument(messageMedia.document);
    }

    public static boolean isForwardedMessage(TLRPC.Message message) {
        return ((message.flags & 4) == 0 || message.fwd_from == null) ? false : true;
    }

    public static boolean isFreeEmoji(TLRPC.Document document) {
        if (document == null) {
            return false;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji) {
                return ((TLRPC.TL_documentAttributeCustomEmoji) documentAttribute).free;
            }
        }
        return false;
    }

    public static boolean isGameMessage(TLRPC.Message message) {
        return getMedia(message) instanceof TLRPC.TL_messageMediaGame;
    }

    public static boolean isGifDocument(WebFile webFile) {
        return webFile != null && (webFile.mime_type.equals("image/gif") || isNewGifDocument(webFile));
    }

    public static boolean isGifDocument(TLRPC.Document document) {
        return isGifDocument(document, false);
    }

    public static boolean isGifDocument(TLRPC.Document document, boolean z) {
        String str;
        return (document == null || (str = document.mime_type) == null || ((!str.equals("image/gif") || z) && !isNewGifDocument(document))) ? false : true;
    }

    public static boolean isGifMessage(TLRPC.Message message) {
        if (getMedia(message) instanceof TLRPC.TL_messageMediaWebPage) {
            return isGifDocument(getMedia(message).webpage.document);
        }
        if (getMedia(message) != null) {
            return isGifDocument(getMedia(message).document, (message.grouped_id > 0L ? 1 : (message.grouped_id == 0L ? 0 : -1)) != 0);
        }
        return false;
    }

    public static boolean isImageWebDocument(WebFile webFile) {
        return (webFile == null || isGifDocument(webFile) || !webFile.mime_type.startsWith("image/")) ? false : true;
    }

    public static boolean isInvoiceMessage(TLRPC.Message message) {
        return getMedia(message) instanceof TLRPC.TL_messageMediaInvoice;
    }

    public static boolean isLiveLocationMessage(TLRPC.Message message) {
        return getMedia(message) instanceof TLRPC.TL_messageMediaGeoLive;
    }

    public static boolean isLocationMessage(TLRPC.Message message) {
        return (getMedia(message) instanceof TLRPC.TL_messageMediaGeo) || (getMedia(message) instanceof TLRPC.TL_messageMediaGeoLive) || (getMedia(message) instanceof TLRPC.TL_messageMediaVenue);
    }

    public static boolean isMaskDocument(TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if ((documentAttribute instanceof TLRPC.TL_documentAttributeSticker) && documentAttribute.mask) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMaskMessage(TLRPC.Message message) {
        return getMedia(message) != null && isMaskDocument(getMedia(message).document);
    }

    public static boolean isMediaEmpty(TLRPC.Message message) {
        return isMediaEmpty(message, true);
    }

    public static boolean isMediaEmpty(TLRPC.Message message, boolean z) {
        return message == null || getMedia(message) == null || (getMedia(message) instanceof TLRPC.TL_messageMediaEmpty) || (z && (getMedia(message) instanceof TLRPC.TL_messageMediaWebPage));
    }

    public static boolean isMediaEmptyWebpage(TLRPC.Message message) {
        return message == null || getMedia(message) == null || (getMedia(message) instanceof TLRPC.TL_messageMediaEmpty);
    }

    public static boolean isMusicDocument(TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                if (document.attributes.get(i) instanceof TLRPC.TL_documentAttributeAudio) {
                    return !r2.voice;
                }
            }
            if (!TextUtils.isEmpty(document.mime_type)) {
                String lowerCase = document.mime_type.toLowerCase();
                if (lowerCase.equals("audio/flac") || lowerCase.equals("audio/ogg") || lowerCase.equals("audio/opus") || lowerCase.equals("audio/x-opus+ogg") || (lowerCase.equals("application/octet-stream") && FileLoader.getDocumentFileName(document).endsWith(".opus"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMusicMessage(TLRPC.Message message) {
        return getMedia(message) instanceof TLRPC.TL_messageMediaWebPage ? isMusicDocument(getMedia(message).webpage.document) : getMedia(message) != null && isMusicDocument(getMedia(message).document);
    }

    public static Boolean isMyPaidReactionAnonymous(TLRPC.MessageReactions messageReactions) {
        ArrayList<TLRPC.MessageReactor> arrayList;
        if (messageReactions == null || (arrayList = messageReactions.top_reactors) == null) {
            return null;
        }
        Iterator<TLRPC.MessageReactor> it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC.MessageReactor next = it.next();
            if (next != null && next.my) {
                return Boolean.valueOf(next.anonymous);
            }
        }
        return null;
    }

    public static boolean isNewGifDocument(WebFile webFile) {
        if (webFile != null && "video/mp4".equals(webFile.mime_type)) {
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < webFile.attributes.size(); i3++) {
                TLRPC.DocumentAttribute documentAttribute = webFile.attributes.get(i3);
                if (!(documentAttribute instanceof TLRPC.TL_documentAttributeAnimated) && (documentAttribute instanceof TLRPC.TL_documentAttributeVideo)) {
                    i = documentAttribute.w;
                    i2 = documentAttribute.h;
                }
            }
            if (i <= 1280 && i2 <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNewGifDocument(TLRPC.Document document) {
        if (document != null && "video/mp4".equals(document.mime_type)) {
            boolean z = false;
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < document.attributes.size(); i3++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i3);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeAnimated) {
                    z = true;
                } else if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                    i = documentAttribute.w;
                    i2 = documentAttribute.h;
                }
            }
            if (z && i <= 1280 && i2 <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNewGifMessage(TLRPC.Message message) {
        return getMedia(message) instanceof TLRPC.TL_messageMediaWebPage ? isNewGifDocument(getMedia(message).webpage.document) : getMedia(message) != null && isNewGifDocument(getMedia(message).document);
    }

    public static boolean isOut(TLRPC.Message message) {
        return message.out;
    }

    public static boolean isPaidVideo(TLRPC.MessageMedia messageMedia) {
        return (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) && messageMedia.extended_media.size() == 1 && isExtendedVideo(messageMedia.extended_media.get(0));
    }

    public static boolean isPhoto(TLRPC.Message message) {
        TLRPC.MessageAction messageAction;
        TLRPC.Photo photo;
        return getMedia(message) instanceof TLRPC.TL_messageMediaWebPage ? (getMedia(message).webpage.photo instanceof TLRPC.TL_photo) && !(getMedia(message).webpage.document instanceof TLRPC.TL_document) : (message == null || (messageAction = message.action) == null || (photo = messageAction.photo) == null) ? getMedia(message) instanceof TLRPC.TL_messageMediaPhoto : photo instanceof TLRPC.TL_photo;
    }

    public static boolean isPremiumEmojiPack(TLRPC.StickerSetCovered stickerSetCovered) {
        TLRPC.StickerSet stickerSet;
        if (stickerSetCovered != null && (stickerSet = stickerSetCovered.set) != null && !stickerSet.emojis) {
            return false;
        }
        ArrayList<TLRPC.Document> arrayList = stickerSetCovered instanceof TLRPC.TL_stickerSetFullCovered ? ((TLRPC.TL_stickerSetFullCovered) stickerSetCovered).documents : stickerSetCovered.covers;
        if (stickerSetCovered != null && arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (!isFreeEmoji(arrayList.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPremiumEmojiPack(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        TLRPC.StickerSet stickerSet;
        if ((tL_messages_stickerSet == null || (stickerSet = tL_messages_stickerSet.set) == null || stickerSet.emojis) && tL_messages_stickerSet != null && tL_messages_stickerSet.documents != null) {
            for (int i = 0; i < tL_messages_stickerSet.documents.size(); i++) {
                if (!isFreeEmoji(tL_messages_stickerSet.documents.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPremiumSticker(TLRPC.Document document) {
        if (document != null && document.thumbs != null) {
            for (int i = 0; i < document.video_thumbs.size(); i++) {
                if ("f".equals(document.video_thumbs.get(i).type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isQuickReply(TLRPC.Message message) {
        return (message == null || ((message.flags & 1073741824) == 0 && message.quick_reply_shortcut == null)) ? false : true;
    }

    public static boolean isRoundVideoDocument(TLRPC.Document document) {
        if (document != null && "video/mp4".equals(document.mime_type)) {
            boolean z = false;
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < document.attributes.size(); i3++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i3);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                    i = documentAttribute.w;
                    i2 = documentAttribute.h;
                    z = documentAttribute.round_message;
                }
            }
            if (z && i <= 1280 && i2 <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRoundVideoMessage(TLRPC.Message message) {
        return (!(getMedia(message) instanceof TLRPC.TL_messageMediaWebPage) || getMedia(message).webpage == null) ? getMedia(message) != null && isRoundVideoDocument(getMedia(message).document) : isRoundVideoDocument(getMedia(message).webpage.document);
    }

    public static boolean isSecretMedia(TLRPC.Message message) {
        if (message instanceof TLRPC.TL_message_secret) {
            return ((getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) || isRoundVideoMessage(message) || isVideoMessage(message)) && getMedia(message).ttl_seconds != 0;
        }
        if (message instanceof TLRPC.TL_message) {
            return ((getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) || (getMedia(message) instanceof TLRPC.TL_messageMediaDocument)) && getMedia(message).ttl_seconds != 0;
        }
        return false;
    }

    public static boolean isSecretPhotoOrVideo(TLRPC.Message message) {
        int i;
        if (message instanceof TLRPC.TL_message_secret) {
            return ((getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) || isRoundVideoMessage(message) || isVideoMessage(message)) && (i = message.ttl) > 0 && i <= 60;
        }
        if (message instanceof TLRPC.TL_message) {
            return ((getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) || (getMedia(message) instanceof TLRPC.TL_messageMediaDocument)) && getMedia(message).ttl_seconds != 0;
        }
        return false;
    }

    public static boolean isStaticStickerDocument(TLRPC.Document document) {
        return document != null && document.mime_type.equals("image/webp");
    }

    public static boolean isStickerDocument(TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                if (document.attributes.get(i) instanceof TLRPC.TL_documentAttributeSticker) {
                    return "image/webp".equals(document.mime_type) || "video/webm".equals(document.mime_type);
                }
            }
        }
        return false;
    }

    public static boolean isStickerHasSet(TLRPC.Document document) {
        TLRPC.InputStickerSet inputStickerSet;
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if ((documentAttribute instanceof TLRPC.TL_documentAttributeSticker) && (inputStickerSet = documentAttribute.stickerset) != null && !(inputStickerSet instanceof TLRPC.TL_inputStickerSetEmpty)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isStickerMessage(TLRPC.Message message) {
        return getMedia(message) != null && isStickerDocument(getMedia(message).document);
    }

    public static boolean isSystemSignUp(MessageObject messageObject) {
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            if ((message instanceof TLRPC.TL_messageService) && (((TLRPC.TL_messageService) message).action instanceof TLRPC.TL_messageActionContactSignUp)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTextColorEmoji(TLRPC.Document document) {
        if (document == null) {
            return false;
        }
        getInputStickerSet(document);
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji) {
                TLRPC.InputStickerSet inputStickerSet = documentAttribute.stickerset;
                if ((inputStickerSet instanceof TLRPC.TL_inputStickerSetID) && inputStickerSet.id == 1269403972611866647L) {
                    return true;
                }
                return ((TLRPC.TL_documentAttributeCustomEmoji) documentAttribute).text_color;
            }
        }
        return false;
    }

    public static boolean isTextColorSet(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        TLRPC.StickerSet stickerSet;
        if (tL_messages_stickerSet != null && (stickerSet = tL_messages_stickerSet.set) != null) {
            if (stickerSet.text_color) {
                return true;
            }
            ArrayList<TLRPC.Document> arrayList = tL_messages_stickerSet.documents;
            if (arrayList != null && !arrayList.isEmpty()) {
                return isTextColorEmoji(tL_messages_stickerSet.documents.get(0));
            }
        }
        return false;
    }

    public static boolean isTopicActionMessage(MessageObject messageObject) {
        TLRPC.Message message;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return false;
        }
        TLRPC.MessageAction messageAction = message.action;
        return (messageAction instanceof TLRPC.TL_messageActionTopicCreate) || (messageAction instanceof TLRPC.TL_messageActionTopicEdit);
    }

    public static boolean isUnread(TLRPC.Message message) {
        return message.unread;
    }

    public static boolean isVideoDocument(TLRPC.Document document) {
        int lastIndexOf;
        if (document == null) {
            return false;
        }
        String str = null;
        boolean z = false;
        int i = 0;
        int i2 = 0;
        boolean z2 = false;
        for (int i3 = 0; i3 < document.attributes.size(); i3++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i3);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                if (documentAttribute.round_message) {
                    return false;
                }
                i = documentAttribute.w;
                i2 = documentAttribute.h;
                z2 = true;
            } else if (documentAttribute instanceof TLRPC.TL_documentAttributeAnimated) {
                z = true;
            } else if (documentAttribute instanceof TLRPC.TL_documentAttributeFilename) {
                str = documentAttribute.file_name;
            }
        }
        if (str != null && (lastIndexOf = str.lastIndexOf(".")) >= 0) {
            switch (str.substring(lastIndexOf + 1).toLowerCase().hashCode()) {
                case 3669:
                case 96796:
                case 98689:
                case 99351:
                case 99582:
                case 104987:
                case 3213227:
                    break;
            }
            return false;
        }
        if (z && (i > 1280 || i2 > 1280)) {
            z = false;
        }
        if (SharedConfig.streamMkv && !z2 && "video/x-matroska".equals(document.mime_type)) {
            z2 = true;
        }
        return z2 && !z;
    }

    public static boolean isVideoMessage(TLRPC.Message message) {
        if (getMedia(message) == null || !isVideoSticker(getMedia(message).document)) {
            return getMedia(message) instanceof TLRPC.TL_messageMediaWebPage ? isVideoDocument(getMedia(message).webpage.document) : getMedia(message) != null && isVideoDocument(getMedia(message).document);
        }
        return false;
    }

    public static boolean isVideoSticker(TLRPC.Document document) {
        return document != null && isVideoStickerDocument(document);
    }

    public static boolean isVideoStickerDocument(TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if ((documentAttribute instanceof TLRPC.TL_documentAttributeSticker) || (documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji)) {
                    return "video/webm".equals(document.mime_type);
                }
            }
        }
        return false;
    }

    public static boolean isVideoWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.startsWith("video/");
    }

    public static boolean isVoiceDocument(TLRPC.Document document) {
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                    return documentAttribute.voice;
                }
            }
        }
        return false;
    }

    public static boolean isVoiceMessage(TLRPC.Message message) {
        return getMedia(message) instanceof TLRPC.TL_messageMediaWebPage ? isVoiceDocument(getMedia(message).webpage.document) : getMedia(message) != null && isVoiceDocument(getMedia(message).document);
    }

    public static boolean isVoiceWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.equals("audio/ogg");
    }

    public static boolean isWebM(TLRPC.Document document) {
        return document != null && "video/webm".equals(document.mime_type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$addEntitiesToText$2(TLRPC.MessageEntity messageEntity, TLRPC.MessageEntity messageEntity2) {
        int i = messageEntity.offset;
        int i2 = messageEntity2.offset;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$handleFoundWords$3(String str, String str2) {
        return str2.length() - str.length();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAnimatedEmojiDocument$0(TLRPC.Document document) {
        this.emojiAnimatedSticker = document;
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.animatedEmojiDocumentLoaded, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAnimatedEmojiDocument$1(final TLRPC.Document document) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                MessageObject.this.lambda$loadAnimatedEmojiDocument$0(document);
            }
        });
    }

    public static StaticLayout makeStaticLayout(CharSequence charSequence, TextPaint textPaint, int i, float f, float f2, boolean z) {
        StaticLayout.Builder obtain;
        StaticLayout.Builder lineSpacing;
        StaticLayout.Builder breakStrategy;
        StaticLayout.Builder hyphenationFrequency;
        StaticLayout.Builder alignment;
        StaticLayout build;
        StaticLayout.Builder obtain2;
        StaticLayout.Builder lineSpacing2;
        StaticLayout.Builder breakStrategy2;
        StaticLayout.Builder hyphenationFrequency2;
        StaticLayout.Builder alignment2;
        StaticLayout build2;
        int i2 = Build.VERSION.SDK_INT;
        if (i2 < 24) {
            return new StaticLayout(charSequence, textPaint, i, Layout.Alignment.ALIGN_NORMAL, f, f2, false);
        }
        obtain = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, i);
        lineSpacing = obtain.setLineSpacing(f2, f);
        breakStrategy = lineSpacing.setBreakStrategy(1);
        hyphenationFrequency = breakStrategy.setHyphenationFrequency(0);
        alignment = hyphenationFrequency.setAlignment(Layout.Alignment.ALIGN_NORMAL);
        if (z) {
            alignment.setIncludePad(false);
            if (i2 >= 28) {
                alignment.setUseLineSpacingFromFallbacks(false);
            }
        }
        build = alignment.build();
        for (int i3 = 0; i3 < build.getLineCount(); i3++) {
            if (build.getLineRight(i3) > i) {
                obtain2 = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, i);
                lineSpacing2 = obtain2.setLineSpacing(f2, f);
                breakStrategy2 = lineSpacing2.setBreakStrategy(0);
                hyphenationFrequency2 = breakStrategy2.setHyphenationFrequency(0);
                alignment2 = hyphenationFrequency2.setAlignment(Layout.Alignment.ALIGN_NORMAL);
                if (z) {
                    alignment2.setIncludePad(false);
                    if (Build.VERSION.SDK_INT >= 28) {
                        alignment2.setUseLineSpacingFromFallbacks(false);
                    }
                }
                build2 = alignment2.build();
                return build2;
            }
        }
        return build;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needDrawAvatarInternal() {
        TLRPC.Chat chat;
        if (this.isRepostPreview || this.isSaved || this.forceAvatar || this.customAvatarDrawable != null || this.searchType != 0) {
            return true;
        }
        boolean z = getDialogId() >= 0 ? getDialogId() == UserObject.VERIFY : !((chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()))) == null || !chat.signature_profiles);
        if (!isSponsored()) {
            if ((isFromChat() && isFromUser()) || isFromGroup() || z || this.eventId != 0) {
                return true;
            }
            TLRPC.MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
            if (messageFwdHeader != null && messageFwdHeader.saved_from_peer != null) {
                return true;
            }
        }
        return false;
    }

    public static void normalizeFlags(TLRPC.Message message) {
        TLRPC.Peer peer = message.from_id;
        if (peer == null) {
            message.flags &= -257;
        }
        if (peer == null) {
            message.flags &= -5;
        }
        if (message.reply_to == null) {
            message.flags &= -9;
        }
        if (message.media == null) {
            message.flags &= -513;
        }
        if (message.reply_markup == null) {
            message.flags &= -65;
        }
        if (message.replies == null) {
            message.flags &= -8388609;
        }
        if (message.reactions == null) {
            message.flags &= -1048577;
        }
    }

    public static CharSequence peerNameWithIcon(int i, long j) {
        return peerNameWithIcon(i, j, false);
    }

    public static CharSequence peerNameWithIcon(int i, long j, boolean z) {
        MessagesController messagesController = MessagesController.getInstance(i);
        if (j >= 0) {
            TLRPC.User user = messagesController.getUser(Long.valueOf(j));
            return user != null ? AndroidUtilities.removeDiacritics(UserObject.getUserName(user)) : "";
        }
        TLRPC.Chat chat = messagesController.getChat(Long.valueOf(-j));
        if (chat != null) {
            return new SpannableStringBuilder(ChatObject.isChannelAndNotMegaGroup(chat) ? channelSpan() : groupSpan()).append((CharSequence) " ").append((CharSequence) AndroidUtilities.removeDiacritics(chat.title));
        }
        return "";
    }

    public static CharSequence peerNameWithIcon(int i, TLRPC.Peer peer) {
        return peerNameWithIcon(i, peer, !(peer instanceof TLRPC.TL_peerUser));
    }

    public static CharSequence peerNameWithIcon(int i, TLRPC.Peer peer, boolean z) {
        TLRPC.Chat chat;
        SpannableStringBuilder spannableStringBuilder;
        SpannableStringBuilder append;
        String str;
        if (peer instanceof TLRPC.TL_peerUser) {
            TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(peer.user_id));
            if (user == null) {
                return "";
            }
            if (!z) {
                return UserObject.getUserName(user);
            }
            append = new SpannableStringBuilder(userSpan()).append((CharSequence) " ");
            str = UserObject.getUserName(user);
        } else {
            if (peer instanceof TLRPC.TL_peerChat) {
                chat = MessagesController.getInstance(i).getChat(Long.valueOf(peer.chat_id));
                if (chat == null) {
                    return "";
                }
                if (!z) {
                    return chat.title;
                }
                spannableStringBuilder = new SpannableStringBuilder(ChatObject.isChannelAndNotMegaGroup(chat) ? channelSpan() : groupSpan());
            } else {
                if (!(peer instanceof TLRPC.TL_peerChannel) || (chat = MessagesController.getInstance(i).getChat(Long.valueOf(peer.channel_id))) == null) {
                    return "";
                }
                if (!z) {
                    return chat.title;
                }
                spannableStringBuilder = new SpannableStringBuilder(ChatObject.isChannelAndNotMegaGroup(chat) ? channelSpan() : groupSpan());
            }
            append = spannableStringBuilder.append((CharSequence) " ");
            str = chat.title;
        }
        return append.append((CharSequence) str);
    }

    public static boolean peersEqual(TLRPC.Chat chat, TLRPC.Peer peer) {
        if (chat == null && peer == null) {
            return true;
        }
        if (chat != null && peer != null) {
            return (ChatObject.isChannel(chat) && (peer instanceof TLRPC.TL_peerChannel)) ? chat.id == peer.channel_id : !ChatObject.isChannel(chat) && (peer instanceof TLRPC.TL_peerChat) && chat.id == peer.chat_id;
        }
        return false;
    }

    public static boolean peersEqual(TLRPC.InputPeer inputPeer, TLRPC.InputPeer inputPeer2) {
        if (inputPeer == null && inputPeer2 == null) {
            return true;
        }
        if (inputPeer != null && inputPeer2 != null) {
            if ((inputPeer instanceof TLRPC.TL_inputPeerChat) && (inputPeer2 instanceof TLRPC.TL_inputPeerChat)) {
                return inputPeer.chat_id == inputPeer2.chat_id;
            }
            if ((inputPeer instanceof TLRPC.TL_inputPeerChannel) && (inputPeer2 instanceof TLRPC.TL_inputPeerChannel)) {
                return inputPeer.channel_id == inputPeer2.channel_id;
            }
            if ((inputPeer instanceof TLRPC.TL_inputPeerUser) && (inputPeer2 instanceof TLRPC.TL_inputPeerUser)) {
                return inputPeer.user_id == inputPeer2.user_id;
            }
            if ((inputPeer instanceof TLRPC.TL_inputPeerSelf) && (inputPeer2 instanceof TLRPC.TL_inputPeerSelf)) {
                return true;
            }
        }
        return false;
    }

    public static boolean peersEqual(TLRPC.InputPeer inputPeer, TLRPC.Peer peer) {
        if (inputPeer == null && peer == null) {
            return true;
        }
        if (inputPeer != null && peer != null) {
            return ((inputPeer instanceof TLRPC.TL_inputPeerChat) && (peer instanceof TLRPC.TL_peerChat)) ? inputPeer.chat_id == peer.chat_id : ((inputPeer instanceof TLRPC.TL_inputPeerChannel) && (peer instanceof TLRPC.TL_peerChannel)) ? inputPeer.channel_id == peer.channel_id : (inputPeer instanceof TLRPC.TL_inputPeerUser) && (peer instanceof TLRPC.TL_peerUser) && inputPeer.user_id == peer.user_id;
        }
        return false;
    }

    public static boolean peersEqual(TLRPC.Peer peer, TLRPC.Peer peer2) {
        if (peer == null && peer2 == null) {
            return true;
        }
        if (peer != null && peer2 != null) {
            return ((peer instanceof TLRPC.TL_peerChat) && (peer2 instanceof TLRPC.TL_peerChat)) ? peer.chat_id == peer2.chat_id : ((peer instanceof TLRPC.TL_peerChannel) && (peer2 instanceof TLRPC.TL_peerChannel)) ? peer.channel_id == peer2.channel_id : (peer instanceof TLRPC.TL_peerUser) && (peer2 instanceof TLRPC.TL_peerUser) && peer.user_id == peer2.user_id;
        }
        return false;
    }

    public static Spannable replaceAnimatedEmoji(CharSequence charSequence, ArrayList<TLRPC.MessageEntity> arrayList, Paint.FontMetricsInt fontMetricsInt) {
        return replaceAnimatedEmoji(charSequence, arrayList, fontMetricsInt, false);
    }

    public static Spannable replaceAnimatedEmoji(CharSequence charSequence, ArrayList<TLRPC.MessageEntity> arrayList, Paint.FontMetricsInt fontMetricsInt, boolean z) {
        Spannable spannableString = charSequence instanceof Spannable ? (Spannable) charSequence : new SpannableString(charSequence);
        if (arrayList == null) {
            return spannableString;
        }
        Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) spannableString.getSpans(0, spannableString.length(), Emoji.EmojiSpan.class);
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC.MessageEntity messageEntity = arrayList.get(i);
            if (messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji) {
                TLRPC.TL_messageEntityCustomEmoji tL_messageEntityCustomEmoji = (TLRPC.TL_messageEntityCustomEmoji) messageEntity;
                for (int i2 = 0; i2 < emojiSpanArr.length; i2++) {
                    Emoji.EmojiSpan emojiSpan = emojiSpanArr[i2];
                    if (emojiSpan != null) {
                        int spanStart = spannableString.getSpanStart(emojiSpan);
                        int spanEnd = spannableString.getSpanEnd(emojiSpan);
                        int i3 = tL_messageEntityCustomEmoji.offset;
                        if (AndroidUtilities.intersect1d(i3, tL_messageEntityCustomEmoji.length + i3, spanStart, spanEnd)) {
                            spannableString.removeSpan(emojiSpan);
                            emojiSpanArr[i2] = null;
                        }
                    }
                }
                if (messageEntity.offset + messageEntity.length <= spannableString.length()) {
                    int i4 = messageEntity.offset;
                    AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannableString.getSpans(i4, messageEntity.length + i4, AnimatedEmojiSpan.class);
                    if (animatedEmojiSpanArr != null && animatedEmojiSpanArr.length > 0) {
                        for (AnimatedEmojiSpan animatedEmojiSpan : animatedEmojiSpanArr) {
                            spannableString.removeSpan(animatedEmojiSpan);
                        }
                    }
                    AnimatedEmojiSpan animatedEmojiSpan2 = tL_messageEntityCustomEmoji.document != null ? new AnimatedEmojiSpan(tL_messageEntityCustomEmoji.document, fontMetricsInt) : new AnimatedEmojiSpan(tL_messageEntityCustomEmoji.document_id, fontMetricsInt);
                    animatedEmojiSpan2.top = z;
                    int i5 = messageEntity.offset;
                    spannableString.setSpan(animatedEmojiSpan2, i5, messageEntity.length + i5, 33);
                }
            }
        }
        return spannableString;
    }

    public static CharSequence replaceWithLink(CharSequence charSequence, String str, TLObject tLObject) {
        String str2;
        CharSequence charSequence2;
        String str3;
        StringBuilder sb;
        long j;
        TLObject tLObject2;
        int indexOf = TextUtils.indexOf(charSequence, str);
        if (indexOf < 0) {
            return charSequence;
        }
        if (tLObject instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) tLObject;
            charSequence2 = UserObject.getUserName(user).replace('\n', ' ');
            sb = new StringBuilder();
            sb.append("");
            j = user.id;
        } else {
            if (!(tLObject instanceof TLRPC.Chat)) {
                if (!(tLObject instanceof TLRPC.TL_game)) {
                    if (tLObject instanceof TLRPC.TL_chatInviteExported) {
                        TLRPC.TL_chatInviteExported tL_chatInviteExported = (TLRPC.TL_chatInviteExported) tLObject;
                        charSequence2 = tL_chatInviteExported.link.replace('\n', ' ');
                        str3 = "invite";
                        tLObject2 = tL_chatInviteExported;
                    } else if (tLObject instanceof TLRPC.ForumTopic) {
                        charSequence2 = ForumUtilities.getTopicSpannedName((TLRPC.ForumTopic) tLObject, null, false);
                        str3 = "topic";
                        tLObject2 = tLObject;
                    } else {
                        str2 = "0";
                        charSequence2 = "";
                    }
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{charSequence2}));
                    URLSpanNoUnderlineBold uRLSpanNoUnderlineBold = new URLSpanNoUnderlineBold("" + str3);
                    uRLSpanNoUnderlineBold.setObject(tLObject2);
                    spannableStringBuilder.setSpan(uRLSpanNoUnderlineBold, indexOf, charSequence2.length() + indexOf, 33);
                    return spannableStringBuilder;
                }
                charSequence2 = ((TLRPC.TL_game) tLObject).title.replace('\n', ' ');
                str2 = "game";
                str3 = str2;
                tLObject2 = null;
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{charSequence2}));
                URLSpanNoUnderlineBold uRLSpanNoUnderlineBold2 = new URLSpanNoUnderlineBold("" + str3);
                uRLSpanNoUnderlineBold2.setObject(tLObject2);
                spannableStringBuilder2.setSpan(uRLSpanNoUnderlineBold2, indexOf, charSequence2.length() + indexOf, 33);
                return spannableStringBuilder2;
            }
            TLRPC.Chat chat = (TLRPC.Chat) tLObject;
            charSequence2 = chat.title.replace('\n', ' ');
            sb = new StringBuilder();
            sb.append("");
            j = -chat.id;
        }
        sb.append(j);
        str2 = sb.toString();
        str3 = str2;
        tLObject2 = null;
        SpannableStringBuilder spannableStringBuilder22 = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{charSequence2}));
        URLSpanNoUnderlineBold uRLSpanNoUnderlineBold22 = new URLSpanNoUnderlineBold("" + str3);
        uRLSpanNoUnderlineBold22.setObject(tLObject2);
        spannableStringBuilder22.setSpan(uRLSpanNoUnderlineBold22, indexOf, charSequence2.length() + indexOf, 33);
        return spannableStringBuilder22;
    }

    public static void setUnreadFlags(TLRPC.Message message, int i) {
        message.unread = (i & 1) == 0;
        message.media_unread = (i & 2) == 0;
    }

    public static boolean shouldEncryptPhotoOrVideo(int i, TLRPC.Message message) {
        int i2;
        if ((message == null || message.media == null || !((isVoiceDocument(getDocument(message)) || isRoundVideoMessage(message)) && message.media.ttl_seconds == Integer.MAX_VALUE)) && !(getMedia(message) instanceof TLRPC.TL_messageMediaPaidMedia)) {
            return message instanceof TLRPC.TL_message_secret ? ((getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) || isVideoMessage(message)) && (i2 = message.ttl) > 0 && i2 <= 60 : ((getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) || (getMedia(message) instanceof TLRPC.TL_messageMediaDocument)) && getMedia(message).ttl_seconds != 0;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:1009:0x14b4, code lost:
    
        if ((((org.telegram.tgnet.TLRPC.TL_messageExtendedMediaPreview) r4).flags & 4) != 0) goto L989;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b7, code lost:
    
        if (org.telegram.messenger.ChatObject.isChannelAndNotMegaGroup(r10) != false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00b9, code lost:
    
        r0 = org.telegram.messenger.R.string.ActionSetWallpaperForThisChannel;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00bc, code lost:
    
        r0 = org.telegram.messenger.R.string.ActionSetWallpaperForThisGroup;
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x0d20, code lost:
    
        if (isOut() != false) goto L652;
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x0d8f, code lost:
    
        r0 = org.telegram.messenger.R.string.ActionTakeScreenshoot;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x0d8c, code lost:
    
        r0 = org.telegram.messenger.R.string.ActionTakeScreenshootYou;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x0d8a, code lost:
    
        if (isOut() != false) goto L652;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01bd, code lost:
    
        if (org.telegram.messenger.ChatObject.isChannelAndNotMegaGroup(r10) != false) goto L35;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1105:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x0597  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x05b4  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x16b3  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x0609  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x0629  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x05fb  */
    /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:334:0x067a  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x06a0  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x07b5  */
    /* JADX WARN: Removed duplicated region for block: B:442:0x0916  */
    /* JADX WARN: Removed duplicated region for block: B:446:0x0924  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x096e  */
    /* JADX WARN: Removed duplicated region for block: B:469:0x0971  */
    /* JADX WARN: Removed duplicated region for block: B:486:0x09ae  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x09b2  */
    /* JADX WARN: Removed duplicated region for block: B:583:0x0b80  */
    /* JADX WARN: Removed duplicated region for block: B:603:0x0c14  */
    /* JADX WARN: Removed duplicated region for block: B:6:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:927:0x1328  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0048  */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v182, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r2v19, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r2v39 */
    /* JADX WARN: Type inference failed for: r2v57 */
    /* JADX WARN: Type inference failed for: r2v64 */
    /* JADX WARN: Type inference failed for: r2v77 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateMessageText(AbstractMap<Long, TLRPC.User> abstractMap, AbstractMap<Long, TLRPC.Chat> abstractMap2, LongSparseArray longSparseArray, LongSparseArray longSparseArray2) {
        TLRPC.User user;
        TLRPC.Chat chat;
        TLRPC.Message message;
        Pattern pattern;
        String str;
        int i;
        char c;
        String formatPluralString;
        ?? r2;
        String publicUsername;
        String str2;
        String str3;
        String str4;
        String str5;
        CharSequence charSequence;
        int indexOf;
        SpannableStringBuilder valueOf;
        String formatCurrency;
        StringBuilder sb;
        String format;
        String str6;
        String str7;
        int i2;
        String str8;
        int i3;
        String publicUsername2;
        String str9;
        String string;
        CharSequence charSequence2;
        int i4;
        int i5;
        CharSequence string2;
        TLRPC.Chat chat2;
        String sb2;
        TLRPC.Chat chat3;
        long j;
        CharSequence formatString;
        CharSequence replaceCharSequence;
        TLObject tLObject;
        String str10;
        String str11;
        Object obj;
        long j2;
        TLRPC.Chat chat4;
        TLRPC.Chat chat5;
        int i6;
        TLRPC.Chat chat6;
        ArrayList<TLRPC.VideoSize> arrayList;
        CharSequence spannableStringBuilder;
        int indexOf2;
        TLObject chat7;
        CharSequence replaceTags;
        TLRPC.Chat chat8;
        TLRPC.TL_messageActionGiveawayResults tL_messageActionGiveawayResults;
        String formatPluralString2;
        TLRPC.Chat chat9;
        String str12;
        boolean z;
        TLRPC.Chat chat10;
        int i7;
        int i8;
        int i9;
        int i10;
        CharSequence charSequence3;
        String formatPluralString3;
        int i11;
        CharSequence charSequence4;
        TLRPC.Peer peer = this.messageOwner.from_id;
        if (peer instanceof TLRPC.TL_peerUser) {
            user = getUser(abstractMap, longSparseArray, peer.user_id);
        } else if (peer instanceof TLRPC.TL_peerChannel) {
            chat = getChat(abstractMap2, longSparseArray2, peer.channel_id);
            user = null;
            TLObject tLObject2 = user == null ? user : chat;
            this.drawServiceWithDefaultTypeface = false;
            this.channelJoined = false;
            message = this.messageOwner;
            if (message instanceof TLRPC.TL_messageService) {
                String str13 = "";
                TLRPC.Chat chat11 = chat;
                this.isRestrictedMessage = false;
                String restrictionReason = MessagesController.getInstance(this.currentAccount).getRestrictionReason(this.messageOwner.restriction_reason);
                if (!TextUtils.isEmpty(restrictionReason)) {
                    this.messageText = restrictionReason;
                    this.isRestrictedMessage = true;
                    str3 = str13;
                } else if (isMediaEmpty() || isSponsored()) {
                    String str14 = this.messageOwner.message;
                    charSequence = str14;
                    str5 = str13;
                    if (str14 != null) {
                        try {
                            if (str14.length() > 200) {
                                pattern = AndroidUtilities.BAD_CHARS_MESSAGE_LONG_PATTERN;
                                str = this.messageOwner.message;
                            } else {
                                pattern = AndroidUtilities.BAD_CHARS_MESSAGE_PATTERN;
                                str = this.messageOwner.message;
                            }
                            this.messageText = pattern.matcher(str).replaceAll("\u200c");
                            str3 = str13;
                        } catch (Throwable unused) {
                        }
                    }
                    r2 = charSequence;
                    str4 = str5;
                } else {
                    if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGiveaway) {
                        TLRPC.MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
                        if (messageFwdHeader != null) {
                            TLRPC.Peer peer2 = messageFwdHeader.from_id;
                            if (peer2 instanceof TLRPC.TL_peerChannel) {
                                chat11 = getChat(abstractMap2, longSparseArray2, peer2.channel_id);
                            }
                        }
                        if (ChatObject.isChannelAndNotMegaGroup(chat11)) {
                            i = R.string.BoostingGiveawayChannelStarted;
                            str2 = str13;
                        } else {
                            i = R.string.BoostingGiveawayGroupStarted;
                            str2 = str13;
                        }
                    } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGiveawayResults) {
                        i = R.string.BoostingGiveawayResults;
                        str2 = str13;
                    } else {
                        if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaStory) {
                            if (getMedia(this.messageOwner).via_mention) {
                                TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getMedia(this.messageOwner).user_id));
                                String str15 = (user2 == null || (publicUsername = UserObject.getPublicUsername(user2)) == null) ? null : MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername + "/s/" + getMedia(this.messageOwner).id;
                                if (str15 != null) {
                                    SpannableString spannableString = new SpannableString(str15);
                                    this.messageText = spannableString;
                                    spannableString.setSpan(new URLSpanReplacement("https://" + str15, new TextStyleSpan.TextStyleRun()), 0, this.messageText.length(), 33);
                                    str3 = str13;
                                } else {
                                    r2 = str13;
                                    str4 = str13;
                                }
                            } else {
                                i = R.string.ForwardedStory;
                                str2 = str13;
                            }
                        } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDice) {
                            charSequence = getDiceEmoji();
                            str5 = str13;
                        } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPoll) {
                            if (((TLRPC.TL_messageMediaPoll) getMedia(this.messageOwner)).poll.quiz) {
                                i = R.string.QuizPoll;
                                str2 = str13;
                            } else {
                                i = R.string.Poll;
                                str2 = str13;
                            }
                        } else if (isVoiceOnce()) {
                            i = R.string.AttachOnceAudio;
                            str2 = str13;
                        } else if (isRoundOnce()) {
                            i = R.string.AttachOnceRound;
                            str2 = str13;
                        } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPaidMedia) {
                            TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) getMedia(this.messageOwner);
                            int size = tL_messageMediaPaidMedia.extended_media.size();
                            boolean z2 = false;
                            for (int i12 = 0; i12 < size; i12++) {
                                TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i12);
                                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                                    TLRPC.MessageMedia messageMedia = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media;
                                    z2 = (messageMedia instanceof TLRPC.TL_messageMediaDocument) && isVideoDocument(messageMedia.document);
                                } else if (!(messageExtendedMedia instanceof TLRPC.TL_messageExtendedMediaPreview)) {
                                }
                                if (z2) {
                                    break;
                                }
                            }
                            if (size == 1) {
                                formatPluralString = LocaleController.getString(z2 ? R.string.AttachVideo : R.string.AttachPhoto);
                                c = 0;
                            } else {
                                c = 0;
                                formatPluralString = LocaleController.formatPluralString(z2 ? "Media" : "Photos", size, new Object[0]);
                            }
                            this.messageText = formatPluralString;
                            int i13 = R.string.AttachPaidMedia;
                            Object[] objArr = new Object[1];
                            objArr[c] = formatPluralString;
                            charSequence = StarsIntroActivity.replaceStars(LocaleController.formatString(i13, objArr));
                            str5 = str13;
                        } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto) {
                            if (getMedia(this.messageOwner).ttl_seconds != 0 && !(this.messageOwner instanceof TLRPC.TL_message_secret)) {
                                i = R.string.AttachDestructingPhoto;
                                str2 = str13;
                            } else if (getGroupId() != 0) {
                                i = R.string.Album;
                                str2 = str13;
                            } else {
                                i = R.string.AttachPhoto;
                                str2 = str13;
                            }
                        } else if (isVideo() || ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument) && (((getDocument() instanceof TLRPC.TL_documentEmpty) || getDocument() == null) && getMedia(this.messageOwner).ttl_seconds != 0))) {
                            if (getMedia(this.messageOwner).ttl_seconds != 0) {
                                TLRPC.Message message2 = this.messageOwner;
                                if (!(message2 instanceof TLRPC.TL_message_secret)) {
                                    if (getMedia(message2).voice) {
                                        i = R.string.AttachVoiceExpired;
                                        str2 = str13;
                                    } else if (getMedia(this.messageOwner).round) {
                                        i = R.string.AttachRoundExpired;
                                        str2 = str13;
                                    } else {
                                        i = R.string.AttachDestructingVideo;
                                        str2 = str13;
                                    }
                                }
                            }
                            i = R.string.AttachVideo;
                            str2 = str13;
                        } else if (isVoice()) {
                            i = R.string.AttachAudio;
                            str2 = str13;
                        } else if (isRoundVideo()) {
                            i = R.string.AttachRound;
                            str2 = str13;
                        } else if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGeo) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaVenue)) {
                            i = R.string.AttachLocation;
                            str2 = str13;
                        } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGeoLive) {
                            i = R.string.AttachLiveLocation;
                            str2 = str13;
                        } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaContact) {
                            this.messageText = LocaleController.getString(R.string.AttachContact);
                            str3 = str13;
                            if (!TextUtils.isEmpty(getMedia(this.messageOwner).vcard)) {
                                this.vCardData = VCardData.parse(getMedia(this.messageOwner).vcard);
                                str3 = str13;
                            }
                        } else {
                            if (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame)) {
                                if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaInvoice) {
                                    charSequence = getMedia(this.messageOwner).description;
                                    str5 = str13;
                                } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaUnsupported) {
                                    i = R.string.UnsupportedMedia2;
                                    str2 = str13;
                                } else {
                                    str3 = str13;
                                    if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument) {
                                        if (isSticker() || isAnimatedStickerDocument(getDocument(), true)) {
                                            String stickerChar = getStickerChar();
                                            if (stickerChar == null || stickerChar.length() <= 0) {
                                                i = R.string.AttachSticker;
                                                str2 = str13;
                                            } else {
                                                charSequence = String.format("%s %s", stickerChar, LocaleController.getString(R.string.AttachSticker));
                                                str5 = str13;
                                            }
                                        } else if (isMusic()) {
                                            i = R.string.AttachMusic;
                                            str2 = str13;
                                        } else if (isGif()) {
                                            i = R.string.AttachGif;
                                            str2 = str13;
                                        } else {
                                            CharSequence documentFileName = FileLoader.getDocumentFileName(getDocument());
                                            boolean isEmpty = TextUtils.isEmpty(documentFileName);
                                            charSequence = documentFileName;
                                            str5 = str13;
                                            if (isEmpty) {
                                                i = R.string.AttachDocument;
                                                str2 = str13;
                                            }
                                        }
                                    }
                                }
                            }
                            charSequence = this.messageOwner.message;
                            str5 = str13;
                        }
                        r2 = charSequence;
                        str4 = str5;
                    }
                    charSequence = LocaleController.getString(i);
                    str5 = str2;
                    r2 = charSequence;
                    str4 = str5;
                }
                if (this.messageText == null) {
                }
            } else {
                TLRPC.MessageAction messageAction = message.action;
                if (messageAction != null) {
                    if (!(messageAction instanceof TLRPC.TL_messageActionSetSameChatWallPaper)) {
                        if (messageAction instanceof TLRPC.TL_messageActionSetChatWallPaper) {
                            this.contentType = 1;
                            TLRPC.TL_messageActionSetChatWallPaper tL_messageActionSetChatWallPaper = (TLRPC.TL_messageActionSetChatWallPaper) messageAction;
                            this.type = 22;
                            ArrayList<TLRPC.PhotoSize> arrayList2 = new ArrayList<>();
                            this.photoThumbs = arrayList2;
                            TLRPC.Document document = tL_messageActionSetChatWallPaper.wallpaper.document;
                            if (document != null) {
                                arrayList2.addAll(document.thumbs);
                                this.photoThumbsObject = tL_messageActionSetChatWallPaper.wallpaper.document;
                            }
                            TLRPC.User user3 = getUser(abstractMap, longSparseArray, isOutOwner() ? 0L : getDialogId());
                            TLRPC.User user4 = getUser(abstractMap, longSparseArray, getDialogId());
                            if (user3 != null) {
                                if (user3.id != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                    SpannableString spannableString2 = new SpannableString(UserObject.getFirstName(user3));
                                    spannableString2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString2.length(), 33);
                                    if (tL_messageActionSetChatWallPaper.same) {
                                        this.type = 10;
                                        i11 = R.string.ActionSetSameWallpaperForThisChat;
                                    } else {
                                        i11 = tL_messageActionSetChatWallPaper.for_both ? R.string.ActionSetWallpaperForThisChatBoth : R.string.ActionSetWallpaperForThisChat;
                                    }
                                    this.messageText = LocaleController.getString(i11);
                                    charSequence4 = AndroidUtilities.replaceCharSequence("%s", this.messageText, spannableString2);
                                } else if (tL_messageActionSetChatWallPaper.same) {
                                    this.type = 10;
                                    charSequence4 = LocaleController.formatString(R.string.ActionSetSameWallpaperForThisChatSelf, new Object[0]);
                                } else if (!tL_messageActionSetChatWallPaper.for_both || user4 == null) {
                                    i = R.string.ActionSetWallpaperForThisChatSelf;
                                    str2 = "";
                                    charSequence = LocaleController.getString(i);
                                    str5 = str2;
                                    r2 = charSequence;
                                    str4 = str5;
                                } else {
                                    this.messageText = LocaleController.getString(R.string.ActionSetWallpaperForThisChatSelfBoth);
                                    SpannableString spannableString3 = new SpannableString(UserObject.getFirstName(user4));
                                    spannableString3.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString3.length(), 33);
                                    charSequence4 = AndroidUtilities.replaceCharSequence("%s", this.messageText, spannableString3);
                                }
                            } else if (chat == null) {
                                if (user != null) {
                                    charSequence4 = LocaleController.formatString(R.string.ActionSetWallpaperForThisGroupByUser, UserObject.getFirstName(user));
                                }
                            }
                            str5 = "";
                            charSequence = charSequence4;
                            r2 = charSequence;
                            str4 = str5;
                        } else {
                            if (messageAction instanceof TLRPC.TL_messageActionGroupCallScheduled) {
                                TLRPC.TL_messageActionGroupCallScheduled tL_messageActionGroupCallScheduled = (TLRPC.TL_messageActionGroupCallScheduled) messageAction;
                                charSequence4 = ((message.peer_id instanceof TLRPC.TL_peerChat) || isSupergroup()) ? LocaleController.formatString(R.string.ActionGroupCallScheduled, LocaleController.formatStartsTime(tL_messageActionGroupCallScheduled.schedule_date, 3, false)) : LocaleController.formatString(R.string.ActionChannelCallScheduled, LocaleController.formatStartsTime(tL_messageActionGroupCallScheduled.schedule_date, 3, false));
                            } else {
                                String str16 = "un1";
                                if (messageAction instanceof TLRPC.TL_messageActionGroupCall) {
                                    int i14 = messageAction.duration;
                                    if (i14 != 0) {
                                        int i15 = i14 / 86400;
                                        if (i15 > 0) {
                                            formatPluralString3 = LocaleController.formatPluralString("Days", i15, new Object[0]);
                                        } else {
                                            int i16 = i14 / 3600;
                                            if (i16 > 0) {
                                                formatPluralString3 = LocaleController.formatPluralString("Hours", i16, new Object[0]);
                                            } else {
                                                int i17 = i14 / 60;
                                                formatPluralString3 = i17 > 0 ? LocaleController.formatPluralString("Minutes", i17, new Object[0]) : LocaleController.formatPluralString("Seconds", i14, new Object[0]);
                                            }
                                        }
                                        if (!(this.messageOwner.peer_id instanceof TLRPC.TL_peerChat) && !isSupergroup()) {
                                            charSequence4 = LocaleController.formatString(R.string.ActionChannelCallEnded, formatPluralString3);
                                        } else if (isOut()) {
                                            charSequence4 = LocaleController.formatString(R.string.ActionGroupCallEndedByYou, formatPluralString3);
                                        } else {
                                            formatString = LocaleController.formatString(R.string.ActionGroupCallEndedBy, formatPluralString3);
                                            str6 = "un1";
                                            str7 = "";
                                            str16 = str6;
                                            tLObject = tLObject2;
                                            charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                            str5 = str7;
                                            charSequence = charSequence3;
                                        }
                                    } else {
                                        if (!(message.peer_id instanceof TLRPC.TL_peerChat) && !isSupergroup()) {
                                            i = R.string.ActionChannelCallJustStarted;
                                        } else if (isOut()) {
                                            i = R.string.ActionGroupCallStartedByYou;
                                        } else {
                                            i2 = R.string.ActionGroupCallStarted;
                                            str6 = "un1";
                                            str7 = "";
                                            formatString = LocaleController.getString(i2);
                                            str16 = str6;
                                            tLObject = tLObject2;
                                            charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                            str5 = str7;
                                            charSequence = charSequence3;
                                        }
                                        str2 = "";
                                        charSequence = LocaleController.getString(i);
                                        str5 = str2;
                                    }
                                } else {
                                    TLRPC.Chat chat12 = chat;
                                    String str17 = "un2";
                                    if (messageAction instanceof TLRPC.TL_messageActionInviteToGroupCall) {
                                        long j3 = messageAction.user_id;
                                        if (j3 == 0 && messageAction.users.size() == 1) {
                                            j3 = this.messageOwner.action.users.get(0).longValue();
                                        }
                                        if (j3 != 0) {
                                            tLObject = getUser(abstractMap, longSparseArray, j3);
                                            if (isOut()) {
                                                i10 = R.string.ActionGroupCallYouInvited;
                                                formatString = LocaleController.getString(i10);
                                                str16 = "un2";
                                                str7 = "";
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            } else if (j3 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                i2 = R.string.ActionGroupCallInvitedYou;
                                                str6 = "un1";
                                                str7 = "";
                                                formatString = LocaleController.getString(i2);
                                                str16 = str6;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            } else {
                                                i9 = R.string.ActionGroupCallInvited;
                                                formatString = replaceWithLink(LocaleController.getString(i9), "un2", tLObject);
                                                this.messageText = formatString;
                                                str6 = "un1";
                                                str7 = "";
                                                str16 = str6;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            }
                                        } else if (isOut()) {
                                            i8 = R.string.ActionGroupCallYouInvited;
                                            charSequence4 = replaceWithLink(LocaleController.getString(i8), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                        } else {
                                            i7 = R.string.ActionGroupCallInvited;
                                            formatString = replaceWithLink(LocaleController.getString(i7), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                            this.messageText = formatString;
                                            str6 = "un1";
                                            str7 = "";
                                            str16 = str6;
                                            tLObject = tLObject2;
                                            charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                            str5 = str7;
                                            charSequence = charSequence3;
                                        }
                                    } else {
                                        if (messageAction instanceof TLRPC.TL_messageActionGeoProximityReached) {
                                            TLRPC.TL_messageActionGeoProximityReached tL_messageActionGeoProximityReached = (TLRPC.TL_messageActionGeoProximityReached) messageAction;
                                            long peerId = getPeerId(tL_messageActionGeoProximityReached.from_id);
                                            TLObject user5 = peerId > 0 ? getUser(abstractMap, longSparseArray, peerId) : getChat(abstractMap2, longSparseArray2, -peerId);
                                            long peerId2 = getPeerId(tL_messageActionGeoProximityReached.to_id);
                                            long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                            if (peerId2 == clientUserId) {
                                                formatString = LocaleController.formatString(R.string.ActionUserWithinRadius, LocaleController.formatDistance(tL_messageActionGeoProximityReached.distance, 2));
                                            } else {
                                                chat7 = peerId2 > 0 ? getUser(abstractMap, longSparseArray, peerId2) : getChat(abstractMap2, longSparseArray2, -peerId2);
                                                if (peerId == clientUserId) {
                                                    replaceTags = LocaleController.formatString(R.string.ActionUserWithinYouRadius, LocaleController.formatDistance(tL_messageActionGeoProximityReached.distance, 2));
                                                } else {
                                                    formatString = replaceWithLink(LocaleController.formatString(R.string.ActionUserWithinOtherRadius, LocaleController.formatDistance(tL_messageActionGeoProximityReached.distance, 2)), "un2", chat7);
                                                    this.messageText = formatString;
                                                }
                                            }
                                            tLObject = user5;
                                            str7 = "";
                                            charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                            str5 = str7;
                                            charSequence = charSequence3;
                                        } else if (messageAction instanceof TLRPC.TL_messageActionCustomAction) {
                                            charSequence4 = messageAction.message;
                                        } else if (messageAction instanceof TLRPC.TL_messageActionChatCreate) {
                                            if (isOut()) {
                                                i = R.string.ActionYouCreateGroup;
                                                str2 = "";
                                                charSequence = LocaleController.getString(i);
                                                str5 = str2;
                                            } else {
                                                i2 = R.string.ActionCreateGroup;
                                                str6 = "un1";
                                                str7 = "";
                                                formatString = LocaleController.getString(i2);
                                                str16 = str6;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            }
                                        } else if (messageAction instanceof TLRPC.TL_messageActionChatDeleteUser) {
                                            if (isFromUser()) {
                                                TLRPC.Message message3 = this.messageOwner;
                                                if (message3.action.user_id == message3.from_id.user_id) {
                                                    if (isOut()) {
                                                        i = R.string.ActionYouLeftUser;
                                                        str2 = "";
                                                        charSequence = LocaleController.getString(i);
                                                        str5 = str2;
                                                    } else {
                                                        i2 = R.string.ActionLeftUser;
                                                        str6 = "un1";
                                                        str7 = "";
                                                        formatString = LocaleController.getString(i2);
                                                        str16 = str6;
                                                        tLObject = tLObject2;
                                                        charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                        str5 = str7;
                                                        charSequence = charSequence3;
                                                    }
                                                }
                                            }
                                            tLObject = getUser(abstractMap, longSparseArray, this.messageOwner.action.user_id);
                                            if (isOut()) {
                                                i10 = R.string.ActionYouKickUser;
                                                formatString = LocaleController.getString(i10);
                                                str16 = "un2";
                                                str7 = "";
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            } else if (this.messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                i2 = R.string.ActionKickUserYou;
                                                str6 = "un1";
                                                str7 = "";
                                                formatString = LocaleController.getString(i2);
                                                str16 = str6;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            } else {
                                                i9 = R.string.ActionKickUser;
                                                formatString = replaceWithLink(LocaleController.getString(i9), "un2", tLObject);
                                                this.messageText = formatString;
                                                str6 = "un1";
                                                str7 = "";
                                                str16 = str6;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            }
                                        } else if (messageAction instanceof TLRPC.TL_messageActionPaymentRefunded) {
                                            TLRPC.TL_messageActionPaymentRefunded tL_messageActionPaymentRefunded = (TLRPC.TL_messageActionPaymentRefunded) messageAction;
                                            long peerDialogId = DialogObject.getPeerDialogId(tL_messageActionPaymentRefunded.peer);
                                            charSequence4 = StarsIntroActivity.replaceStars(replaceWithLink(LocaleController.formatString(R.string.ActionRefunded, tL_messageActionPaymentRefunded.currency + " " + LocaleController.formatNumber(tL_messageActionPaymentRefunded.total_amount, ',')), "un1", peerDialogId >= 0 ? getUser(abstractMap, longSparseArray, peerDialogId) : getChat(abstractMap2, longSparseArray2, -peerDialogId)));
                                        } else if (messageAction instanceof TLRPC.TL_messageActionChatAddUser) {
                                            long j4 = messageAction.user_id;
                                            if (j4 == 0 && messageAction.users.size() == 1) {
                                                j4 = this.messageOwner.action.users.get(0).longValue();
                                            }
                                            if (j4 != 0) {
                                                tLObject = getUser(abstractMap, longSparseArray, j4);
                                                long j5 = this.messageOwner.peer_id.channel_id;
                                                TLRPC.Chat chat13 = j5 != 0 ? getChat(abstractMap2, longSparseArray2, j5) : null;
                                                TLRPC.Peer peer3 = this.messageOwner.from_id;
                                                if (peer3 != null && j4 == peer3.user_id) {
                                                    if (ChatObject.isChannel(chat13) && !chat13.megagroup) {
                                                        this.channelJoined = true;
                                                        i = R.string.ChannelJoined;
                                                    } else if (this.messageOwner.peer_id.channel_id != 0) {
                                                        if (j4 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                            i = R.string.ChannelMegaJoined;
                                                        } else {
                                                            i2 = R.string.ActionAddUserSelfMega;
                                                        }
                                                    } else if (isOut()) {
                                                        i = R.string.ActionAddUserSelfYou;
                                                    } else {
                                                        i2 = R.string.ActionAddUserSelf;
                                                    }
                                                    str2 = "";
                                                    charSequence = LocaleController.getString(i);
                                                    str5 = str2;
                                                } else if (isOut()) {
                                                    i10 = R.string.ActionYouAddUser;
                                                    formatString = LocaleController.getString(i10);
                                                    str16 = "un2";
                                                    str7 = "";
                                                    charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                    str5 = str7;
                                                    charSequence = charSequence3;
                                                } else if (j4 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                    i2 = this.messageOwner.peer_id.channel_id != 0 ? (chat13 == null || !chat13.megagroup) ? R.string.ChannelAddedBy : R.string.MegaAddedBy : R.string.ActionAddUserYou;
                                                } else {
                                                    i9 = R.string.ActionAddUser;
                                                    formatString = replaceWithLink(LocaleController.getString(i9), "un2", tLObject);
                                                    this.messageText = formatString;
                                                    str6 = "un1";
                                                    str7 = "";
                                                    str16 = str6;
                                                    tLObject = tLObject2;
                                                    charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                    str5 = str7;
                                                    charSequence = charSequence3;
                                                }
                                                str6 = "un1";
                                                str7 = "";
                                                formatString = LocaleController.getString(i2);
                                                str16 = str6;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            } else if (isOut()) {
                                                i8 = R.string.ActionYouAddUser;
                                                charSequence4 = replaceWithLink(LocaleController.getString(i8), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                            } else {
                                                i7 = R.string.ActionAddUser;
                                                formatString = replaceWithLink(LocaleController.getString(i7), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                                this.messageText = formatString;
                                                str6 = "un1";
                                                str7 = "";
                                                str16 = str6;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            }
                                        } else if (messageAction instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                                            if (isOut()) {
                                                i = R.string.ActionInviteYou;
                                                str2 = "";
                                                charSequence = LocaleController.getString(i);
                                                str5 = str2;
                                            } else {
                                                i2 = R.string.ActionInviteUser;
                                                str6 = "un1";
                                                str7 = "";
                                                formatString = LocaleController.getString(i2);
                                                str16 = str6;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                str5 = str7;
                                                charSequence = charSequence3;
                                            }
                                        } else if (messageAction instanceof TLRPC.TL_messageActionGiveawayLaunch) {
                                            TLRPC.TL_messageActionGiveawayLaunch tL_messageActionGiveawayLaunch = (TLRPC.TL_messageActionGiveawayLaunch) messageAction;
                                            TLRPC.Peer peer4 = message.peer_id;
                                            if (peer4 != null) {
                                                long j6 = peer4.channel_id;
                                                if (j6 != 0) {
                                                    chat10 = getChat(abstractMap2, longSparseArray2, j6);
                                                    boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(chat10);
                                                    charSequence4 = (tL_messageActionGiveawayLaunch.flags & 1) == 0 ? LocaleController.formatPluralStringComma(isChannelAndNotMegaGroup ? "BoostingStarsGiveawayJustStarted" : "BoostingStarsGiveawayJustStartedGroup", (int) tL_messageActionGiveawayLaunch.stars, chat10 != null ? chat10.title : "") : LocaleController.formatString(isChannelAndNotMegaGroup ? R.string.BoostingGiveawayJustStarted : R.string.BoostingGiveawayJustStartedGroup, chat10 != null ? chat10.title : "");
                                                }
                                            }
                                            chat10 = null;
                                            boolean isChannelAndNotMegaGroup2 = ChatObject.isChannelAndNotMegaGroup(chat10);
                                            if ((tL_messageActionGiveawayLaunch.flags & 1) == 0) {
                                            }
                                        } else if (messageAction instanceof TLRPC.TL_messageActionBoostApply) {
                                            TLRPC.Peer peer5 = message.peer_id;
                                            if (peer5 != null) {
                                                long j7 = peer5.channel_id;
                                                if (j7 != 0) {
                                                    chat9 = getChat(abstractMap2, longSparseArray2, j7);
                                                    boolean isChannelAndNotMegaGroup3 = ChatObject.isChannelAndNotMegaGroup(chat9);
                                                    TLRPC.TL_messageActionBoostApply tL_messageActionBoostApply = (TLRPC.TL_messageActionBoostApply) this.messageOwner.action;
                                                    if (tLObject2 instanceof TLRPC.User) {
                                                        str12 = tLObject2 instanceof TLRPC.Chat ? ((TLRPC.Chat) tLObject2).title : "";
                                                        z = false;
                                                    } else {
                                                        TLRPC.User user6 = (TLRPC.User) tLObject2;
                                                        z = UserObject.isUserSelf(user6);
                                                        str12 = UserObject.getFirstName(user6);
                                                    }
                                                    if (z) {
                                                        int i18 = tL_messageActionBoostApply.boosts;
                                                        charSequence4 = i18 <= 1 ? LocaleController.formatString(isChannelAndNotMegaGroup3 ? R.string.BoostingBoostsChannelByUserServiceMsg : R.string.BoostingBoostsGroupByUserServiceMsg, str12) : LocaleController.formatPluralString(isChannelAndNotMegaGroup3 ? "BoostingBoostsChannelByUserServiceMsgCount" : "BoostingBoostsGroupByUserServiceMsgCount", i18, str12);
                                                    } else {
                                                        int i19 = tL_messageActionBoostApply.boosts;
                                                        if (i19 <= 1) {
                                                            int i20 = isChannelAndNotMegaGroup3 ? R.string.BoostingBoostsChannelByYouServiceMsg : R.string.BoostingBoostsGroupByYouServiceMsg;
                                                            charSequence4 = LocaleController.getString(i20);
                                                        } else {
                                                            charSequence4 = LocaleController.formatPluralString(isChannelAndNotMegaGroup3 ? "BoostingBoostsChannelByYouServiceMsgCount" : "BoostingBoostsGroupByYouServiceMsgCount", i19, new Object[0]);
                                                        }
                                                    }
                                                }
                                            }
                                            chat9 = null;
                                            boolean isChannelAndNotMegaGroup32 = ChatObject.isChannelAndNotMegaGroup(chat9);
                                            TLRPC.TL_messageActionBoostApply tL_messageActionBoostApply2 = (TLRPC.TL_messageActionBoostApply) this.messageOwner.action;
                                            if (tLObject2 instanceof TLRPC.User) {
                                            }
                                            if (z) {
                                            }
                                        } else if (messageAction instanceof TLRPC.TL_messageActionGiveawayResults) {
                                            TLRPC.Peer peer6 = message.peer_id;
                                            if (peer6 != null) {
                                                long j8 = peer6.channel_id;
                                                if (j8 != 0) {
                                                    chat8 = getChat(abstractMap2, longSparseArray2, j8);
                                                    boolean isChannelAndNotMegaGroup4 = ChatObject.isChannelAndNotMegaGroup(chat8);
                                                    tL_messageActionGiveawayResults = (TLRPC.TL_messageActionGiveawayResults) this.messageOwner.action;
                                                    r2 = new SpannableStringBuilder();
                                                    if (tL_messageActionGiveawayResults.stars) {
                                                        r2.append(LocaleController.formatPluralString("BoostingGiveawayServiceWinnersSelected", tL_messageActionGiveawayResults.winners_count, new Object[0]));
                                                        if (tL_messageActionGiveawayResults.unclaimed_count > 0) {
                                                            r2.append("\n");
                                                            formatPluralString2 = LocaleController.formatPluralString(isChannelAndNotMegaGroup4 ? "BoostingGiveawayServiceUndistributed" : "BoostingGiveawayServiceUndistributedGroup", tL_messageActionGiveawayResults.unclaimed_count, new Object[0]);
                                                            r2.append(formatPluralString2);
                                                        }
                                                        str4 = "";
                                                    } else {
                                                        r2.append(LocaleController.formatPluralStringComma("BoostingStarsGiveawayServiceWinnersSelected", tL_messageActionGiveawayResults.winners_count));
                                                        if (tL_messageActionGiveawayResults.unclaimed_count > 0) {
                                                            r2.append("\n");
                                                            formatPluralString2 = LocaleController.formatPluralString(isChannelAndNotMegaGroup4 ? "BoostingStarsGiveawayServiceUndistributed" : "BoostingStarsGiveawayServiceUndistributedGroup", tL_messageActionGiveawayResults.unclaimed_count, new Object[0]);
                                                            r2.append(formatPluralString2);
                                                        }
                                                        str4 = "";
                                                    }
                                                }
                                            }
                                            chat8 = null;
                                            boolean isChannelAndNotMegaGroup42 = ChatObject.isChannelAndNotMegaGroup(chat8);
                                            tL_messageActionGiveawayResults = (TLRPC.TL_messageActionGiveawayResults) this.messageOwner.action;
                                            r2 = new SpannableStringBuilder();
                                            if (tL_messageActionGiveawayResults.stars) {
                                            }
                                        } else if (messageAction instanceof TLRPC.TL_messageActionPrizeStars) {
                                            TLRPC.TL_messageActionPrizeStars tL_messageActionPrizeStars = (TLRPC.TL_messageActionPrizeStars) messageAction;
                                            chat7 = getChat(abstractMap2, longSparseArray2, -DialogObject.getPeerDialogId(tL_messageActionPrizeStars.boost_peer));
                                            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("ActionStarGiveawayPrize", (int) tL_messageActionPrizeStars.stars));
                                        } else if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
                                            TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction;
                                            TL_stars.StarGift starGift = tL_messageActionStarGift.gift;
                                            int i21 = starGift != null ? (int) starGift.stars : 0;
                                            boolean z3 = tLObject2 instanceof TLRPC.User;
                                            if (z3 && ((TLRPC.User) tLObject2).self && !tL_messageActionStarGift.forceIn) {
                                                this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftOutbound)), "un1", getUser(abstractMap, longSparseArray, message.peer_id.user_id));
                                                TLRPC.TL_textWithEntities tL_textWithEntities = tL_messageActionStarGift.message;
                                                if (tL_textWithEntities != null && !TextUtils.isEmpty(tL_textWithEntities.text)) {
                                                    spannableStringBuilder = new SpannableStringBuilder(tL_messageActionStarGift.message.text);
                                                    addEntitiesToText(spannableStringBuilder, tL_messageActionStarGift.message.entities, isOutOwner(), false, false, false);
                                                }
                                                spannableStringBuilder = LocaleController.getString(R.string.ActionStarGift);
                                            } else if (z3 && UserObject.isService(((TLRPC.User) tLObject2).id)) {
                                                this.messageText = TextUtils.replace(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), new String[]{"un1"}, new CharSequence[]{LocaleController.getString(R.string.StarsTransactionUnknown)});
                                                indexOf2 = this.messageText.toString().indexOf("un2");
                                                if (indexOf2 != -1) {
                                                    charSequence4 = SpannableStringBuilder.valueOf(this.messageText).replace(indexOf2, indexOf2 + 3, (CharSequence) LocaleController.formatPluralStringComma("Gift2StarsCount", i21));
                                                }
                                            } else {
                                                this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), "un1", tLObject2);
                                                TLRPC.TL_textWithEntities tL_textWithEntities2 = tL_messageActionStarGift.message;
                                                if (tL_textWithEntities2 != null && !TextUtils.isEmpty(tL_textWithEntities2.text)) {
                                                    spannableStringBuilder = new SpannableStringBuilder(tL_messageActionStarGift.message.text);
                                                    addEntitiesToText(spannableStringBuilder, tL_messageActionStarGift.message.entities, isOutOwner(), false, false, false);
                                                }
                                                spannableStringBuilder = LocaleController.getString(R.string.ActionStarGift);
                                            }
                                            this.messageTextShort = spannableStringBuilder;
                                            indexOf2 = this.messageText.toString().indexOf("un2");
                                            if (indexOf2 != -1) {
                                            }
                                        } else {
                                            String str18 = "";
                                            String str19 = ", ";
                                            if (messageAction instanceof TLRPC.TL_messageActionGiftStars) {
                                                boolean z4 = tLObject2 instanceof TLRPC.User;
                                                this.messageText = (z4 && ((TLRPC.User) tLObject2).self) ? replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftOutbound)), "un1", getUser(abstractMap, longSparseArray, message.peer_id.user_id)) : (z4 && UserObject.isService(((TLRPC.User) tLObject2).id)) ? TextUtils.replace(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), new String[]{"un1"}, new CharSequence[]{LocaleController.getString(R.string.StarsTransactionUnknown)}) : replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), "un1", tLObject2);
                                                indexOf = this.messageText.toString().indexOf("un2");
                                                str3 = str18;
                                                if (indexOf != -1) {
                                                    valueOf = SpannableStringBuilder.valueOf(this.messageText);
                                                    BillingController billingController = BillingController.getInstance();
                                                    TLRPC.MessageAction messageAction2 = this.messageOwner.action;
                                                    formatCurrency = billingController.formatCurrency(messageAction2.amount, messageAction2.currency);
                                                    if ((this.messageOwner.action.flags & 1) != 0) {
                                                        sb = new StringBuilder();
                                                        double d = this.messageOwner.action.cryptoAmount;
                                                        double pow = Math.pow(10.0d, -9.0d);
                                                        Double.isNaN(d);
                                                        format = String.format("%.2f", Double.valueOf(d * pow));
                                                        sb.append(format);
                                                        sb.append(" ");
                                                        sb.append(this.messageOwner.action.cryptoCurrency);
                                                        sb.append(" (~ ");
                                                        sb.append((Object) formatCurrency);
                                                        sb.append(")");
                                                        formatCurrency = sb.toString();
                                                    }
                                                    charSequence = valueOf.replace(indexOf, indexOf + 3, (CharSequence) formatCurrency);
                                                    str5 = str18;
                                                }
                                            } else {
                                                boolean z5 = messageAction instanceof TLRPC.TL_messageActionGiftCode;
                                                if (z5 && ((TLRPC.TL_messageActionGiftCode) messageAction).boost_peer != null) {
                                                    i = R.string.BoostingReceivedGiftNoName;
                                                    str2 = str18;
                                                } else if ((messageAction instanceof TLRPC.TL_messageActionGiftPremium) || z5) {
                                                    this.messageText = ((tLObject2 instanceof TLRPC.User) && ((TLRPC.User) tLObject2).self) ? replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftOutbound)), "un1", getUser(abstractMap, longSparseArray, message.peer_id.user_id)) : replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), "un1", tLObject2);
                                                    indexOf = this.messageText.toString().indexOf("un2");
                                                    str3 = str18;
                                                    if (indexOf != -1) {
                                                        valueOf = SpannableStringBuilder.valueOf(this.messageText);
                                                        BillingController billingController2 = BillingController.getInstance();
                                                        TLRPC.MessageAction messageAction3 = this.messageOwner.action;
                                                        formatCurrency = billingController2.formatCurrency(messageAction3.amount, messageAction3.currency);
                                                        if ((this.messageOwner.action.flags & 1) != 0) {
                                                            sb = new StringBuilder();
                                                            double d2 = this.messageOwner.action.cryptoAmount;
                                                            double pow2 = Math.pow(10.0d, -9.0d);
                                                            Double.isNaN(d2);
                                                            format = String.format("%.2f", Double.valueOf(d2 * pow2));
                                                            sb.append(format);
                                                            sb.append(" ");
                                                            sb.append(this.messageOwner.action.cryptoCurrency);
                                                            sb.append(" (~ ");
                                                            sb.append((Object) formatCurrency);
                                                            sb.append(")");
                                                            formatCurrency = sb.toString();
                                                        }
                                                        charSequence = valueOf.replace(indexOf, indexOf + 3, (CharSequence) formatCurrency);
                                                        str5 = str18;
                                                    }
                                                } else if (messageAction instanceof TLRPC.TL_messageActionSuggestProfilePhoto) {
                                                    TLRPC.Photo photo = messageAction.photo;
                                                    if (photo == null || (arrayList = photo.video_sizes) == null || arrayList.isEmpty()) {
                                                        i = R.string.ActionSuggestPhotoShort;
                                                        str2 = str18;
                                                    } else {
                                                        i = R.string.ActionSuggestVideoShort;
                                                        str2 = str18;
                                                    }
                                                } else {
                                                    if (messageAction instanceof TLRPC.TL_messageActionChatEditPhoto) {
                                                        TLRPC.Peer peer7 = message.peer_id;
                                                        if (peer7 != null) {
                                                            long j9 = peer7.channel_id;
                                                            if (j9 != 0) {
                                                                chat6 = getChat(abstractMap2, longSparseArray2, j9);
                                                                if (ChatObject.isChannel(chat6) || chat6.megagroup) {
                                                                    if (isOut()) {
                                                                        i2 = isVideoAvatar() ? R.string.ActionChangedVideo : R.string.ActionChangedPhoto;
                                                                        str6 = "un1";
                                                                        str7 = str18;
                                                                        formatString = LocaleController.getString(i2);
                                                                        str16 = str6;
                                                                        tLObject = tLObject2;
                                                                    } else if (isVideoAvatar()) {
                                                                        i = R.string.ActionYouChangedVideo;
                                                                        str2 = str18;
                                                                    } else {
                                                                        i = R.string.ActionYouChangedPhoto;
                                                                        str2 = str18;
                                                                    }
                                                                } else if (isVideoAvatar()) {
                                                                    i = R.string.ActionChannelChangedVideo;
                                                                    str2 = str18;
                                                                } else {
                                                                    i = R.string.ActionChannelChangedPhoto;
                                                                    str2 = str18;
                                                                }
                                                            }
                                                        }
                                                        chat6 = null;
                                                        if (ChatObject.isChannel(chat6)) {
                                                        }
                                                        if (isOut()) {
                                                        }
                                                    } else {
                                                        if (messageAction instanceof TLRPC.TL_messageActionChatEditTitle) {
                                                            TLRPC.Peer peer8 = message.peer_id;
                                                            if (peer8 != null) {
                                                                long j10 = peer8.channel_id;
                                                                if (j10 != 0) {
                                                                    chat5 = getChat(abstractMap2, longSparseArray2, j10);
                                                                    if (!ChatObject.isChannel(chat5) && !chat5.megagroup) {
                                                                        i6 = R.string.ActionChannelChangedTitle;
                                                                    } else if (isOut()) {
                                                                        formatString = LocaleController.getString(R.string.ActionChangedTitle).replace("un2", this.messageOwner.action.title);
                                                                        str6 = "un1";
                                                                        str7 = str18;
                                                                        str16 = str6;
                                                                        tLObject = tLObject2;
                                                                    } else {
                                                                        i6 = R.string.ActionYouChangedTitle;
                                                                    }
                                                                    charSequence = LocaleController.getString(i6).replace("un2", this.messageOwner.action.title);
                                                                    str5 = str18;
                                                                }
                                                            }
                                                            chat5 = null;
                                                            if (!ChatObject.isChannel(chat5)) {
                                                            }
                                                            if (isOut()) {
                                                            }
                                                        } else if (messageAction instanceof TLRPC.TL_messageActionChatDeletePhoto) {
                                                            TLRPC.Peer peer9 = message.peer_id;
                                                            if (peer9 != null) {
                                                                long j11 = peer9.channel_id;
                                                                if (j11 != 0) {
                                                                    chat4 = getChat(abstractMap2, longSparseArray2, j11);
                                                                    if (!ChatObject.isChannel(chat4) && !chat4.megagroup) {
                                                                        i = R.string.ActionChannelRemovedPhoto;
                                                                        str2 = str18;
                                                                    } else if (isOut()) {
                                                                        i2 = R.string.ActionRemovedPhoto;
                                                                        str6 = "un1";
                                                                        str7 = str18;
                                                                        formatString = LocaleController.getString(i2);
                                                                        str16 = str6;
                                                                        tLObject = tLObject2;
                                                                    } else {
                                                                        i = R.string.ActionYouRemovedPhoto;
                                                                        str2 = str18;
                                                                    }
                                                                }
                                                            }
                                                            chat4 = null;
                                                            if (!ChatObject.isChannel(chat4)) {
                                                            }
                                                            if (isOut()) {
                                                            }
                                                        } else if (messageAction instanceof TLRPC.TL_messageActionTTLChange) {
                                                            if (messageAction.ttl != 0) {
                                                                if (isOut()) {
                                                                    charSequence = LocaleController.formatString(R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(this.messageOwner.action.ttl));
                                                                    str5 = str18;
                                                                } else {
                                                                    charSequence = LocaleController.formatString(R.string.MessageLifetimeChanged, UserObject.getFirstName(user), LocaleController.formatTTLString(this.messageOwner.action.ttl));
                                                                    str5 = str18;
                                                                }
                                                            } else if (isOut()) {
                                                                str7 = str18;
                                                                i = R.string.MessageLifetimeYouRemoved;
                                                                str2 = str7;
                                                            } else {
                                                                charSequence = LocaleController.formatString(R.string.MessageLifetimeRemoved, UserObject.getFirstName(user));
                                                                str5 = str18;
                                                            }
                                                        } else if (messageAction instanceof TLRPC.TL_messageActionRequestedPeer) {
                                                            ArrayList arrayList3 = new ArrayList();
                                                            int i22 = 0;
                                                            int i23 = 0;
                                                            int i24 = 0;
                                                            for (TLRPC.Peer peer10 : ((TLRPC.TL_messageActionRequestedPeer) this.messageOwner.action).peers) {
                                                                boolean z6 = peer10 instanceof TLRPC.TL_peerUser;
                                                                if (z6) {
                                                                    str10 = str17;
                                                                    str11 = str16;
                                                                    obj = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peer10.user_id));
                                                                    if (obj == null) {
                                                                        obj = getUser(abstractMap, longSparseArray, peer10.user_id);
                                                                    }
                                                                } else {
                                                                    str10 = str17;
                                                                    str11 = str16;
                                                                    if (peer10 instanceof TLRPC.TL_peerChat) {
                                                                        obj = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(peer10.chat_id));
                                                                        if (obj == null) {
                                                                            j2 = peer10.chat_id;
                                                                            obj = getChat(abstractMap2, longSparseArray2, j2);
                                                                        }
                                                                    } else if (peer10 instanceof TLRPC.TL_peerChannel) {
                                                                        obj = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(peer10.channel_id));
                                                                        if (obj == null) {
                                                                            j2 = peer10.channel_id;
                                                                            obj = getChat(abstractMap2, longSparseArray2, j2);
                                                                        }
                                                                    } else {
                                                                        obj = null;
                                                                    }
                                                                }
                                                                if (z6) {
                                                                    i22++;
                                                                } else if (peer10 instanceof TLRPC.TL_peerChat) {
                                                                    i24++;
                                                                } else {
                                                                    i23++;
                                                                }
                                                                if (obj != null) {
                                                                    arrayList3.add(obj);
                                                                }
                                                                str16 = str11;
                                                                str17 = str10;
                                                            }
                                                            String str20 = str17;
                                                            String str21 = str16;
                                                            if (i22 > 0 && i22 != arrayList3.size()) {
                                                                replaceCharSequence = LocaleController.getPluralString("ActionRequestedPeerUserPlural", i22);
                                                            } else if (i23 > 0 && i23 != arrayList3.size()) {
                                                                replaceCharSequence = LocaleController.getPluralString("ActionRequestedPeerChannelPlural", i23);
                                                            } else if (i24 <= 0 || i24 == arrayList3.size()) {
                                                                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                                                                int i25 = 0;
                                                                while (i25 < arrayList3.size()) {
                                                                    String str22 = str21;
                                                                    spannableStringBuilder2.append(replaceWithLink(str22, str22, (TLObject) arrayList3.get(i25)));
                                                                    String str23 = str19;
                                                                    if (i25 < arrayList3.size() - 1) {
                                                                        spannableStringBuilder2.append((CharSequence) str23);
                                                                    }
                                                                    i25++;
                                                                    str19 = str23;
                                                                    str21 = str22;
                                                                }
                                                                replaceCharSequence = AndroidUtilities.replaceCharSequence(str21, LocaleController.getString(R.string.ActionRequestedPeer), spannableStringBuilder2);
                                                            } else {
                                                                replaceCharSequence = LocaleController.getPluralString("ActionRequestedPeerChatPlural", i24);
                                                            }
                                                            this.messageText = replaceCharSequence;
                                                            TLObject user7 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
                                                            if (user7 == null) {
                                                                user7 = getUser(abstractMap, longSparseArray, getDialogId());
                                                            }
                                                            tLObject = user7;
                                                            formatString = this.messageText;
                                                            str7 = str18;
                                                            str16 = str20;
                                                        } else {
                                                            str6 = "un1";
                                                            if (messageAction instanceof TLRPC.TL_messageActionSetMessagesTTL) {
                                                                TLRPC.TL_messageActionSetMessagesTTL tL_messageActionSetMessagesTTL = (TLRPC.TL_messageActionSetMessagesTTL) messageAction;
                                                                TLRPC.Peer peer11 = message.peer_id;
                                                                if (peer11 != null) {
                                                                    long j12 = peer11.channel_id;
                                                                    if (j12 != 0) {
                                                                        chat3 = getChat(abstractMap2, longSparseArray2, j12);
                                                                        if (chat3 != null || chat3.megagroup) {
                                                                            j = tL_messageActionSetMessagesTTL.auto_setting_from;
                                                                            if (j == 0) {
                                                                                this.drawServiceWithDefaultTypeface = true;
                                                                                if (j == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                                                    charSequence = AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AutoDeleteGlobalActionFromYou, LocaleController.formatTTLString(tL_messageActionSetMessagesTTL.period)));
                                                                                    str5 = str18;
                                                                                } else {
                                                                                    TLRPC.Chat chat14 = longSparseArray != null ? (TLObject) longSparseArray.get(tL_messageActionSetMessagesTTL.auto_setting_from) : null;
                                                                                    if (chat14 == null && abstractMap != null) {
                                                                                        chat14 = abstractMap.get(Long.valueOf(tL_messageActionSetMessagesTTL.auto_setting_from));
                                                                                    }
                                                                                    if (chat14 == null && abstractMap2 != null) {
                                                                                        chat14 = abstractMap2.get(Long.valueOf(tL_messageActionSetMessagesTTL.auto_setting_from));
                                                                                    }
                                                                                    if (chat14 == null) {
                                                                                        long j13 = tL_messageActionSetMessagesTTL.auto_setting_from;
                                                                                        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                        chat14 = j13 > 0 ? messagesController.getUser(Long.valueOf(tL_messageActionSetMessagesTTL.auto_setting_from)) : messagesController.getChat(Long.valueOf(-tL_messageActionSetMessagesTTL.auto_setting_from));
                                                                                    }
                                                                                    if (chat14 != null) {
                                                                                        tLObject2 = chat14;
                                                                                    }
                                                                                    formatString = AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AutoDeleteGlobalAction, LocaleController.formatTTLString(tL_messageActionSetMessagesTTL.period)));
                                                                                    str7 = str18;
                                                                                    str16 = str6;
                                                                                    tLObject = tLObject2;
                                                                                }
                                                                            } else if (tL_messageActionSetMessagesTTL.period != 0) {
                                                                                if (isOut()) {
                                                                                    charSequence = LocaleController.formatString(R.string.ActionTTLYouChanged, LocaleController.formatTTLString(tL_messageActionSetMessagesTTL.period));
                                                                                    str5 = str18;
                                                                                } else {
                                                                                    formatString = LocaleController.formatString(R.string.ActionTTLChanged, LocaleController.formatTTLString(tL_messageActionSetMessagesTTL.period));
                                                                                    str7 = str18;
                                                                                    str16 = str6;
                                                                                    tLObject = tLObject2;
                                                                                }
                                                                            } else if (isOut()) {
                                                                                i = R.string.ActionTTLYouDisabled;
                                                                                str2 = str18;
                                                                            } else {
                                                                                i2 = R.string.ActionTTLDisabled;
                                                                                str7 = str18;
                                                                                formatString = LocaleController.getString(i2);
                                                                                str16 = str6;
                                                                                tLObject = tLObject2;
                                                                            }
                                                                        } else {
                                                                            int i26 = tL_messageActionSetMessagesTTL.period;
                                                                            if (i26 != 0) {
                                                                                charSequence = LocaleController.formatString(R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i26));
                                                                                str5 = str18;
                                                                            } else {
                                                                                i = R.string.ActionTTLChannelDisabled;
                                                                                str2 = str18;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                chat3 = null;
                                                                if (chat3 != null) {
                                                                }
                                                                j = tL_messageActionSetMessagesTTL.auto_setting_from;
                                                                if (j == 0) {
                                                                }
                                                            } else if (messageAction instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                                                                long j14 = message.date * 1000;
                                                                if (LocaleController.getInstance().getFormatterDay() == null || LocaleController.getInstance().getFormatterYear() == null) {
                                                                    StringBuilder sb3 = new StringBuilder();
                                                                    str7 = str18;
                                                                    sb3.append(str7);
                                                                    sb3.append(this.messageOwner.date);
                                                                    sb2 = sb3.toString();
                                                                } else {
                                                                    sb2 = LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(j14), LocaleController.getInstance().getFormatterDay().format(j14));
                                                                    str7 = str18;
                                                                }
                                                                TLRPC.User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                                                                if (currentUser == null) {
                                                                    currentUser = getUser(abstractMap, longSparseArray, this.messageOwner.peer_id.user_id);
                                                                }
                                                                String firstName = currentUser != null ? UserObject.getFirstName(currentUser) : str7;
                                                                int i27 = R.string.NotificationUnrecognizedDevice;
                                                                TLRPC.MessageAction messageAction4 = this.messageOwner.action;
                                                                charSequence3 = LocaleController.formatString(i27, firstName, sb2, messageAction4.title, messageAction4.address);
                                                            } else {
                                                                str7 = str18;
                                                                if ((messageAction instanceof TLRPC.TL_messageActionUserJoined) || (messageAction instanceof TLRPC.TL_messageActionContactSignUp)) {
                                                                    charSequence3 = LocaleController.formatString(R.string.NotificationContactJoined, UserObject.getUserName(user));
                                                                } else if (messageAction instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                                                                    charSequence3 = LocaleController.formatString(R.string.NotificationContactNewPhoto, UserObject.getUserName(user));
                                                                } else if (messageAction instanceof TLRPC.TL_messageEncryptedAction) {
                                                                    TLRPC.DecryptedMessageAction decryptedMessageAction = messageAction.encryptedAction;
                                                                    if (!(decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages)) {
                                                                        if (decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                                                                            TLRPC.TL_decryptedMessageActionSetMessageTTL tL_decryptedMessageActionSetMessageTTL = (TLRPC.TL_decryptedMessageActionSetMessageTTL) decryptedMessageAction;
                                                                            if (tL_decryptedMessageActionSetMessageTTL.ttl_seconds != 0) {
                                                                                charSequence3 = isOut() ? LocaleController.formatString(R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(tL_decryptedMessageActionSetMessageTTL.ttl_seconds)) : LocaleController.formatString(R.string.MessageLifetimeChanged, UserObject.getFirstName(user), LocaleController.formatTTLString(tL_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                                                            } else {
                                                                                if (!isOut()) {
                                                                                    charSequence3 = LocaleController.formatString(R.string.MessageLifetimeRemoved, UserObject.getFirstName(user));
                                                                                }
                                                                                i = R.string.MessageLifetimeYouRemoved;
                                                                                str2 = str7;
                                                                            }
                                                                        }
                                                                        str3 = str7;
                                                                    }
                                                                } else if (!(messageAction instanceof TLRPC.TL_messageActionScreenshotTaken)) {
                                                                    if (messageAction instanceof TLRPC.TL_messageActionCreatedBroadcastList) {
                                                                        i = R.string.YouCreatedBroadcastList;
                                                                    } else if (messageAction instanceof TLRPC.TL_messageActionChannelCreate) {
                                                                        TLRPC.Peer peer12 = message.peer_id;
                                                                        if (peer12 != null) {
                                                                            long j15 = peer12.channel_id;
                                                                            if (j15 != 0) {
                                                                                chat2 = getChat(abstractMap2, longSparseArray2, j15);
                                                                                i = (ChatObject.isChannel(chat2) || !chat2.megagroup) ? R.string.ActionCreateChannel : R.string.ActionCreateMega;
                                                                            }
                                                                        }
                                                                        chat2 = null;
                                                                        if (ChatObject.isChannel(chat2)) {
                                                                        }
                                                                    } else if ((messageAction instanceof TLRPC.TL_messageActionChatMigrateTo) || (messageAction instanceof TLRPC.TL_messageActionChannelMigrateFrom)) {
                                                                        i = R.string.ActionMigrateFromGroup;
                                                                    } else {
                                                                        if (messageAction instanceof TLRPC.TL_messageActionPinMessage) {
                                                                            generatePinMessageText(user, user == null ? getChat(abstractMap2, longSparseArray2, message.peer_id.channel_id) : null);
                                                                        } else if (messageAction instanceof TLRPC.TL_messageActionHistoryClear) {
                                                                            i = R.string.HistoryCleared;
                                                                        } else {
                                                                            if (messageAction instanceof TLRPC.TL_messageActionTopicCreate) {
                                                                                this.messageText = LocaleController.getString(R.string.TopicCreated);
                                                                                TLRPC.TL_messageActionTopicCreate tL_messageActionTopicCreate = (TLRPC.TL_messageActionTopicCreate) this.messageOwner.action;
                                                                                TLRPC.TL_forumTopic tL_forumTopic = new TLRPC.TL_forumTopic();
                                                                                tL_forumTopic.icon_emoji_id = tL_messageActionTopicCreate.icon_emoji_id;
                                                                                tL_forumTopic.title = tL_messageActionTopicCreate.title;
                                                                                tL_forumTopic.icon_color = tL_messageActionTopicCreate.icon_color;
                                                                                string2 = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString(R.string.TopicWasCreatedAction), ForumUtilities.getTopicSpannedName(tL_forumTopic, null, false));
                                                                            } else if (messageAction instanceof TLRPC.TL_messageActionTopicEdit) {
                                                                                TLRPC.TL_messageActionTopicEdit tL_messageActionTopicEdit = (TLRPC.TL_messageActionTopicEdit) messageAction;
                                                                                if (user != null) {
                                                                                    str9 = ContactsController.formatName(user.first_name, user.last_name);
                                                                                } else if (chat12 != null) {
                                                                                    user = chat12;
                                                                                    str9 = chat12.title;
                                                                                } else {
                                                                                    str9 = null;
                                                                                    user = null;
                                                                                }
                                                                                String trim = str9 != null ? str9.trim() : "DELETED";
                                                                                TLRPC.MessageAction messageAction5 = this.messageOwner.action;
                                                                                int i28 = messageAction5.flags;
                                                                                if ((i28 & 8) > 0) {
                                                                                    if (((TLRPC.TL_messageActionTopicEdit) messageAction5).hidden) {
                                                                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicHidden2), "%s", user);
                                                                                        i5 = R.string.TopicHidden;
                                                                                    } else {
                                                                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicShown2), "%s", user);
                                                                                        i5 = R.string.TopicShown;
                                                                                    }
                                                                                } else if ((4 & i28) <= 0) {
                                                                                    int i29 = 2 & i28;
                                                                                    if (i29 != 0 && (i28 & 1) != 0) {
                                                                                        TLRPC.TL_forumTopic tL_forumTopic2 = new TLRPC.TL_forumTopic();
                                                                                        tL_forumTopic2.icon_emoji_id = tL_messageActionTopicEdit.icon_emoji_id;
                                                                                        tL_forumTopic2.title = tL_messageActionTopicEdit.title;
                                                                                        tL_forumTopic2.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                                                                        charSequence2 = ForumUtilities.getTopicSpannedName(tL_forumTopic2, null, this.topicIconDrawable, false);
                                                                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString(R.string.TopicChangeIconAndTitleTo), trim), charSequence2);
                                                                                        this.messageTextShort = LocaleController.getString(R.string.TopicRenamed);
                                                                                        i4 = R.string.TopicChangeIconAndTitleToInReply;
                                                                                    } else if (i29 != 0) {
                                                                                        TLRPC.TL_forumTopic tL_forumTopic3 = new TLRPC.TL_forumTopic();
                                                                                        tL_forumTopic3.icon_emoji_id = tL_messageActionTopicEdit.icon_emoji_id;
                                                                                        tL_forumTopic3.title = str7;
                                                                                        tL_forumTopic3.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                                                                        charSequence2 = ForumUtilities.getTopicSpannedName(tL_forumTopic3, null, this.topicIconDrawable, false);
                                                                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString(R.string.TopicIconChangedTo), trim), charSequence2);
                                                                                        this.messageTextShort = LocaleController.getString(R.string.TopicIconChanged);
                                                                                        i4 = R.string.TopicIconChangedToInReply;
                                                                                    } else if ((1 & i28) != 0) {
                                                                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString(R.string.TopicRenamedTo), trim), tL_messageActionTopicEdit.title);
                                                                                        this.messageTextShort = LocaleController.getString(R.string.TopicRenamed);
                                                                                        string = LocaleController.getString(R.string.TopicRenamedToInReply);
                                                                                        charSequence2 = tL_messageActionTopicEdit.title;
                                                                                        this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", string, charSequence2);
                                                                                    }
                                                                                    string = LocaleController.getString(i4);
                                                                                    this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", string, charSequence2);
                                                                                } else if (((TLRPC.TL_messageActionTopicEdit) messageAction5).closed) {
                                                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicClosed2), "%s", user);
                                                                                    i5 = R.string.TopicClosed;
                                                                                } else {
                                                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicRestarted2), "%s", user);
                                                                                    i5 = R.string.TopicRestarted;
                                                                                }
                                                                                string2 = LocaleController.getString(i5);
                                                                            } else if (messageAction instanceof TLRPC.TL_messageActionGameScore) {
                                                                                generateGameMessageText(user);
                                                                            } else if (messageAction instanceof TLRPC.TL_messageActionPhoneCall) {
                                                                                TLRPC.TL_messageActionPhoneCall tL_messageActionPhoneCall = (TLRPC.TL_messageActionPhoneCall) messageAction;
                                                                                boolean z7 = tL_messageActionPhoneCall.reason instanceof TLRPC.TL_phoneCallDiscardReasonMissed;
                                                                                this.messageText = LocaleController.getString((isFromUser() && this.messageOwner.from_id.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) ? z7 ? tL_messageActionPhoneCall.video ? R.string.CallMessageVideoOutgoingMissed : R.string.CallMessageOutgoingMissed : tL_messageActionPhoneCall.video ? R.string.CallMessageVideoOutgoing : R.string.CallMessageOutgoing : z7 ? tL_messageActionPhoneCall.video ? R.string.CallMessageVideoIncomingMissed : R.string.CallMessageIncomingMissed : tL_messageActionPhoneCall.reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy ? tL_messageActionPhoneCall.video ? R.string.CallMessageVideoIncomingDeclined : R.string.CallMessageIncomingDeclined : tL_messageActionPhoneCall.video ? R.string.CallMessageVideoIncoming : R.string.CallMessageIncoming);
                                                                                int i30 = tL_messageActionPhoneCall.duration;
                                                                                if (i30 > 0) {
                                                                                    String formatCallDuration = LocaleController.formatCallDuration(i30);
                                                                                    String formatString2 = LocaleController.formatString(R.string.CallMessageWithDuration, this.messageText, formatCallDuration);
                                                                                    this.messageText = formatString2;
                                                                                    String charSequence5 = formatString2.toString();
                                                                                    int indexOf3 = charSequence5.indexOf(formatCallDuration);
                                                                                    if (indexOf3 != -1) {
                                                                                        SpannableString spannableString4 = new SpannableString(this.messageText);
                                                                                        int length = formatCallDuration.length() + indexOf3;
                                                                                        if (indexOf3 > 0 && charSequence5.charAt(indexOf3 - 1) == '(') {
                                                                                            indexOf3--;
                                                                                        }
                                                                                        if (length < charSequence5.length() && charSequence5.charAt(length) == ')') {
                                                                                            length++;
                                                                                        }
                                                                                        spannableString4.setSpan(new TypefaceSpan(Typeface.DEFAULT), indexOf3, length, 0);
                                                                                        str4 = str7;
                                                                                        r2 = spannableString4;
                                                                                    }
                                                                                }
                                                                            } else if (messageAction instanceof TLRPC.TL_messageActionPaymentSent) {
                                                                                generatePaymentSentMessageText(getUser(abstractMap, longSparseArray, getDialogId()), false);
                                                                            } else if (messageAction instanceof TLRPC.TL_messageActionPaymentSentMe) {
                                                                                generatePaymentSentMessageText(getUser(abstractMap, longSparseArray, getDialogId()), true);
                                                                            } else {
                                                                                boolean z8 = messageAction instanceof TLRPC.TL_messageActionBotAllowed;
                                                                                if (z8) {
                                                                                    TLRPC.TL_messageActionBotAllowed tL_messageActionBotAllowed = (TLRPC.TL_messageActionBotAllowed) messageAction;
                                                                                    String str24 = tL_messageActionBotAllowed.domain;
                                                                                    TLRPC.BotApp botApp = tL_messageActionBotAllowed.app;
                                                                                    if (tL_messageActionBotAllowed.from_request) {
                                                                                        i = R.string.ActionBotAllowedWebapp;
                                                                                    } else if (botApp != null) {
                                                                                        String str25 = botApp.title;
                                                                                        if (str25 == null) {
                                                                                            str25 = str7;
                                                                                        }
                                                                                        String string3 = LocaleController.getString(R.string.ActionBotAllowedApp);
                                                                                        int indexOf4 = string3.indexOf("%1$s");
                                                                                        SpannableString spannableString5 = new SpannableString(String.format(string3, str25));
                                                                                        TLRPC.User user8 = getUser(abstractMap, longSparseArray, getDialogId());
                                                                                        if (indexOf4 >= 0 && user8 != null && (publicUsername2 = UserObject.getPublicUsername(user8)) != null) {
                                                                                            spannableString5.setSpan(new URLSpanNoUnderlineBold("https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername2 + "/" + botApp.short_name), indexOf4, str25.length() + indexOf4, 33);
                                                                                        }
                                                                                        str4 = str7;
                                                                                        r2 = spannableString5;
                                                                                    } else {
                                                                                        if (str24 == null) {
                                                                                            str24 = str7;
                                                                                        }
                                                                                        String string4 = LocaleController.getString(R.string.ActionBotAllowed);
                                                                                        int indexOf5 = string4.indexOf("%1$s");
                                                                                        SpannableString spannableString6 = new SpannableString(String.format(string4, str24));
                                                                                        if (indexOf5 >= 0 && !TextUtils.isEmpty(str24)) {
                                                                                            spannableString6.setSpan(new URLSpanNoUnderlineBold("http://" + str24), indexOf5, str24.length() + indexOf5, 33);
                                                                                        }
                                                                                        str4 = str7;
                                                                                        r2 = spannableString6;
                                                                                    }
                                                                                } else if ((messageAction instanceof TLRPC.TL_messageActionAttachMenuBotAllowed) || (z8 && ((TLRPC.TL_messageActionBotAllowed) messageAction).attach_menu)) {
                                                                                    i = R.string.ActionAttachMenuBotAllowed;
                                                                                } else if (messageAction instanceof TLRPC.TL_messageActionSecureValuesSent) {
                                                                                    TLRPC.TL_messageActionSecureValuesSent tL_messageActionSecureValuesSent = (TLRPC.TL_messageActionSecureValuesSent) messageAction;
                                                                                    StringBuilder sb4 = new StringBuilder();
                                                                                    int size2 = tL_messageActionSecureValuesSent.types.size();
                                                                                    for (int i31 = 0; i31 < size2; i31++) {
                                                                                        TLRPC.SecureValueType secureValueType = tL_messageActionSecureValuesSent.types.get(i31);
                                                                                        if (sb4.length() > 0) {
                                                                                            sb4.append(str19);
                                                                                        }
                                                                                        if (secureValueType instanceof TLRPC.TL_secureValueTypePhone) {
                                                                                            i3 = R.string.ActionBotDocumentPhone;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypeEmail) {
                                                                                            i3 = R.string.ActionBotDocumentEmail;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypeAddress) {
                                                                                            i3 = R.string.ActionBotDocumentAddress;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                                                                                            i3 = R.string.ActionBotDocumentIdentity;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypePassport) {
                                                                                            i3 = R.string.ActionBotDocumentPassport;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                                                                                            i3 = R.string.ActionBotDocumentDriverLicence;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                                                                                            i3 = R.string.ActionBotDocumentIdentityCard;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                                                                                            i3 = R.string.ActionBotDocumentUtilityBill;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypeBankStatement) {
                                                                                            i3 = R.string.ActionBotDocumentBankStatement;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                                                                                            i3 = R.string.ActionBotDocumentRentalAgreement;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypeInternalPassport) {
                                                                                            i3 = R.string.ActionBotDocumentInternalPassport;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                                                                                            i3 = R.string.ActionBotDocumentPassportRegistration;
                                                                                        } else if (secureValueType instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                                                                                            i3 = R.string.ActionBotDocumentTemporaryRegistration;
                                                                                        }
                                                                                        sb4.append(LocaleController.getString(i3));
                                                                                    }
                                                                                    TLRPC.Peer peer13 = this.messageOwner.peer_id;
                                                                                    charSequence3 = LocaleController.formatString("ActionBotDocuments", R.string.ActionBotDocuments, UserObject.getFirstName(peer13 != null ? getUser(abstractMap, longSparseArray, peer13.user_id) : null), sb4.toString());
                                                                                } else if (messageAction instanceof TLRPC.TL_messageActionWebViewDataSent) {
                                                                                    charSequence3 = LocaleController.formatString("ActionBotWebViewData", R.string.ActionBotWebViewData, ((TLRPC.TL_messageActionWebViewDataSent) messageAction).text);
                                                                                } else {
                                                                                    if (messageAction instanceof TLRPC.TL_messageActionSetChatTheme) {
                                                                                        String str26 = ((TLRPC.TL_messageActionSetChatTheme) messageAction).emoticon;
                                                                                        String firstName2 = UserObject.getFirstName(user);
                                                                                        boolean z9 = user == null && chat12 != null;
                                                                                        if (z9) {
                                                                                            firstName2 = chat12.title;
                                                                                        }
                                                                                        boolean isUserSelf = UserObject.isUserSelf(user);
                                                                                        if (TextUtils.isEmpty(str26)) {
                                                                                            str8 = isUserSelf ? LocaleController.formatString("ChatThemeDisabledYou", R.string.ChatThemeDisabledYou, new Object[0]) : LocaleController.formatString(z9 ? R.string.ChannelThemeDisabled : R.string.ChatThemeDisabled, firstName2, str26);
                                                                                        } else if (isUserSelf) {
                                                                                            str8 = LocaleController.formatString("ChatThemeChangedYou", R.string.ChatThemeChangedYou, str26);
                                                                                        } else {
                                                                                            str8 = LocaleController.formatString(z9 ? R.string.ChannelThemeChangedTo : R.string.ChatThemeChangedTo, firstName2, str26);
                                                                                        }
                                                                                    } else if (messageAction instanceof TLRPC.TL_messageActionChatJoinedByRequest) {
                                                                                        if (UserObject.isUserSelf(user)) {
                                                                                            str8 = LocaleController.getString(ChatObject.isChannelAndNotMegaGroup(this.messageOwner.peer_id.channel_id, this.currentAccount) ? R.string.RequestToJoinChannelApproved : R.string.RequestToJoinGroupApproved);
                                                                                        } else {
                                                                                            i2 = R.string.UserAcceptedToGroupAction;
                                                                                            formatString = LocaleController.getString(i2);
                                                                                            str16 = str6;
                                                                                            tLObject = tLObject2;
                                                                                        }
                                                                                    }
                                                                                    r2 = str8;
                                                                                    str4 = str7;
                                                                                }
                                                                            }
                                                                            this.messageTextShort = string2;
                                                                        }
                                                                        str3 = str7;
                                                                    }
                                                                    str2 = str7;
                                                                }
                                                            }
                                                        }
                                                        str5 = str7;
                                                        charSequence = charSequence3;
                                                    }
                                                    charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                                    str5 = str7;
                                                    charSequence = charSequence3;
                                                }
                                                charSequence = LocaleController.getString(i);
                                                str5 = str2;
                                            }
                                        }
                                        tLObject = chat7;
                                        formatString = replaceTags;
                                        str7 = "";
                                        charSequence3 = replaceWithLink(formatString, str16, tLObject);
                                        str5 = str7;
                                        charSequence = charSequence3;
                                    }
                                }
                                r2 = charSequence;
                                str4 = str5;
                            }
                            str5 = "";
                            charSequence = charSequence4;
                            r2 = charSequence;
                            str4 = str5;
                        }
                        if (this.messageText == null) {
                            this.messageText = str3;
                            return;
                        }
                        return;
                    }
                    this.contentType = 1;
                    this.type = 10;
                    TLRPC.TL_messageActionSetSameChatWallPaper tL_messageActionSetSameChatWallPaper = (TLRPC.TL_messageActionSetSameChatWallPaper) messageAction;
                    TLRPC.User user9 = getUser(abstractMap, longSparseArray, isOutOwner() ? 0L : getDialogId());
                    ArrayList<TLRPC.PhotoSize> arrayList4 = new ArrayList<>();
                    this.photoThumbs = arrayList4;
                    TLRPC.Document document2 = tL_messageActionSetSameChatWallPaper.wallpaper.document;
                    if (document2 != null) {
                        arrayList4.addAll(document2.thumbs);
                        this.photoThumbsObject = tL_messageActionSetSameChatWallPaper.wallpaper.document;
                    }
                    if (user9 != null) {
                        charSequence4 = user9.id == UserConfig.getInstance(this.currentAccount).clientUserId ? LocaleController.formatString(R.string.ActionSetSameWallpaperForThisChatSelf, new Object[0]) : LocaleController.formatString(R.string.ActionSetSameWallpaperForThisChat, user9.first_name);
                    } else if (chat == null) {
                        if (user != null) {
                            charSequence4 = LocaleController.formatString(R.string.ActionSetWallpaperForThisGroupByUser, UserObject.getFirstName(user));
                        }
                    }
                    str5 = "";
                    charSequence = charSequence4;
                    r2 = charSequence;
                    str4 = str5;
                }
                str3 = "";
                if (this.messageText == null) {
                }
            }
            this.messageText = r2;
            str3 = str4;
            if (this.messageText == null) {
            }
        } else {
            user = null;
        }
        chat = null;
        if (user == null) {
        }
        this.drawServiceWithDefaultTypeface = false;
        this.channelJoined = false;
        message = this.messageOwner;
        if (message instanceof TLRPC.TL_messageService) {
        }
        this.messageText = r2;
        str3 = str4;
        if (this.messageText == null) {
        }
    }

    private static void updatePhotoSizeLocations(ArrayList<TLRPC.PhotoSize> arrayList, List<TLRPC.PhotoSize> list) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC.PhotoSize photoSize = arrayList.get(i);
            if (photoSize != null) {
                int size2 = list.size();
                int i2 = 0;
                while (true) {
                    if (i2 < size2) {
                        TLRPC.PhotoSize photoSize2 = list.get(i2);
                        if (!(photoSize2 instanceof TLRPC.TL_photoSizeEmpty) && !(photoSize2 instanceof TLRPC.TL_photoCachedSize) && photoSize2 != null && photoSize2.type.equals(photoSize.type)) {
                            photoSize.location = photoSize2.location;
                            break;
                        }
                        i2++;
                    }
                }
            }
        }
    }

    public static void updatePollResults(TLRPC.TL_messageMediaPoll tL_messageMediaPoll, TLRPC.PollResults pollResults) {
        ArrayList arrayList;
        byte[] bArr;
        ArrayList<TLRPC.TL_pollAnswerVoters> arrayList2;
        if (tL_messageMediaPoll == null || pollResults == null) {
            return;
        }
        if ((pollResults.flags & 2) != 0) {
            if (!pollResults.min || (arrayList2 = tL_messageMediaPoll.results.results) == null) {
                arrayList = null;
                bArr = null;
            } else {
                int size = arrayList2.size();
                arrayList = null;
                bArr = null;
                for (int i = 0; i < size; i++) {
                    TLRPC.TL_pollAnswerVoters tL_pollAnswerVoters = tL_messageMediaPoll.results.results.get(i);
                    if (tL_pollAnswerVoters.chosen) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(tL_pollAnswerVoters.option);
                    }
                    if (tL_pollAnswerVoters.correct) {
                        bArr = tL_pollAnswerVoters.option;
                    }
                }
            }
            TLRPC.PollResults pollResults2 = tL_messageMediaPoll.results;
            ArrayList<TLRPC.TL_pollAnswerVoters> arrayList3 = pollResults.results;
            pollResults2.results = arrayList3;
            if (arrayList != null || bArr != null) {
                int size2 = arrayList3.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    TLRPC.TL_pollAnswerVoters tL_pollAnswerVoters2 = tL_messageMediaPoll.results.results.get(i2);
                    if (arrayList != null) {
                        int size3 = arrayList.size();
                        int i3 = 0;
                        while (true) {
                            if (i3 >= size3) {
                                break;
                            }
                            if (Arrays.equals(tL_pollAnswerVoters2.option, (byte[]) arrayList.get(i3))) {
                                tL_pollAnswerVoters2.chosen = true;
                                arrayList.remove(i3);
                                break;
                            }
                            i3++;
                        }
                        if (arrayList.isEmpty()) {
                            arrayList = null;
                        }
                    }
                    if (bArr != null && Arrays.equals(tL_pollAnswerVoters2.option, bArr)) {
                        tL_pollAnswerVoters2.correct = true;
                        bArr = null;
                    }
                    if (arrayList == null && bArr == null) {
                        break;
                    }
                }
            }
            tL_messageMediaPoll.results.flags |= 2;
        }
        if ((pollResults.flags & 4) != 0) {
            TLRPC.PollResults pollResults3 = tL_messageMediaPoll.results;
            pollResults3.total_voters = pollResults.total_voters;
            pollResults3.flags |= 4;
        }
        if ((pollResults.flags & 8) != 0) {
            TLRPC.PollResults pollResults4 = tL_messageMediaPoll.results;
            pollResults4.recent_voters = pollResults.recent_voters;
            pollResults4.flags |= 8;
        }
        if ((pollResults.flags & 16) != 0) {
            TLRPC.PollResults pollResults5 = tL_messageMediaPoll.results;
            pollResults5.solution = pollResults.solution;
            pollResults5.solution_entities = pollResults.solution_entities;
            pollResults5.flags |= 16;
        }
    }

    public static void updateReactions(TLRPC.Message message, TLRPC.TL_messageReactions tL_messageReactions) {
        if (message == null || tL_messageReactions == null) {
            return;
        }
        TLRPC.TL_messageReactions tL_messageReactions2 = message.reactions;
        if (tL_messageReactions2 != null) {
            int size = tL_messageReactions2.results.size();
            boolean z = false;
            for (int i = 0; i < size; i++) {
                TLRPC.ReactionCount reactionCount = message.reactions.results.get(i);
                int size2 = tL_messageReactions.results.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    TLRPC.ReactionCount reactionCount2 = tL_messageReactions.results.get(i2);
                    if (ReactionsLayoutInBubble.equalsTLReaction(reactionCount.reaction, reactionCount2.reaction)) {
                        if (!z && tL_messageReactions.min && reactionCount.chosen) {
                            reactionCount2.chosen = true;
                            z = true;
                        }
                        reactionCount2.lastDrawnPosition = reactionCount.lastDrawnPosition;
                    }
                }
                if (reactionCount.chosen) {
                    z = true;
                }
            }
        }
        message.reactions = tL_messageReactions;
        message.flags |= FileLoaderPriorityQueue.PRIORITY_VALUE_MAX;
    }

    public static CharSequence userSpan() {
        return userSpan(0);
    }

    public static CharSequence userSpan(int i) {
        if (userSpan == null) {
            userSpan = new CharSequence[2];
        }
        CharSequence[] charSequenceArr = userSpan;
        if (charSequenceArr[i] == null) {
            charSequenceArr[i] = new SpannableStringBuilder("u");
            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_reply_user);
            coloredImageSpan.spaceScaleX = 0.9f;
            if (i == 0) {
                coloredImageSpan.translate(0.0f, AndroidUtilities.dp(1.0f));
            }
            ((SpannableStringBuilder) userSpan[i]).setSpan(coloredImageSpan, 0, 1, 33);
        }
        return userSpan[i];
    }

    public boolean addEntitiesToText(CharSequence charSequence, boolean z, boolean z2) {
        ArrayList<TLRPC.MessageEntity> arrayList;
        if (charSequence == null) {
            return false;
        }
        if (!this.isRestrictedMessage && !(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaUnsupported)) {
            if (this.translated) {
                TLRPC.TL_textWithEntities tL_textWithEntities = this.messageOwner.translatedText;
                arrayList = tL_textWithEntities == null ? null : tL_textWithEntities.entities;
            } else {
                arrayList = this.messageOwner.entities;
            }
            return addEntitiesToText(charSequence, arrayList, isOutOwner(), true, z, z2);
        }
        ArrayList arrayList2 = new ArrayList();
        TLRPC.TL_messageEntityItalic tL_messageEntityItalic = new TLRPC.TL_messageEntityItalic();
        tL_messageEntityItalic.offset = 0;
        tL_messageEntityItalic.length = charSequence.length();
        arrayList2.add(tL_messageEntityItalic);
        return addEntitiesToText(charSequence, arrayList2, isOutOwner(), true, z, z2);
    }

    public void addPaidReactions(int i, boolean z, boolean z2) {
        TLRPC.Message message = this.messageOwner;
        if (message.reactions == null) {
            message.reactions = new TLRPC.TL_messageReactions();
            TLRPC.Message message2 = this.messageOwner;
            message2.reactions.reactions_as_tags = getDialogId(message2) == UserConfig.getInstance(this.currentAccount).getClientUserId();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        addPaidReactions(this.currentAccount, this.messageOwner.reactions, i, z2, z);
    }

    public void applyMediaExistanceFlags(int i) {
        if (i == -1) {
            checkMediaExistance();
        } else {
            this.attachPathExists = (i & 1) != 0;
            this.mediaExists = (i & 2) != 0;
        }
    }

    public void applyNewText() {
        this.translated = false;
        applyNewText(this.messageOwner.message);
    }

    public void applyNewText(CharSequence charSequence) {
        TLRPC.TL_textWithEntities tL_textWithEntities;
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        TLRPC.User user = isFromUser() ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id)) : null;
        this.messageText = charSequence;
        ArrayList<TLRPC.MessageEntity> arrayList = (!this.translated || (tL_textWithEntities = this.messageOwner.translatedText) == null) ? this.messageOwner.entities : tL_textWithEntities.entities;
        TextPaint textPaint = getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
        int[] iArr = allowsBigEmoji() ? new int[1] : null;
        CharSequence replaceEmoji = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), false, iArr);
        this.messageText = replaceEmoji;
        Spannable replaceAnimatedEmoji = replaceAnimatedEmoji(replaceEmoji, arrayList, textPaint.getFontMetricsInt());
        this.messageText = replaceAnimatedEmoji;
        if (iArr != null && iArr[0] > 1) {
            replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr);
        }
        checkEmojiOnly(iArr);
        generateLayout(user);
        setType();
    }

    public void applyQuickReply(String str, int i) {
        TLRPC.Message message = this.messageOwner;
        if (message == null) {
            return;
        }
        if (i != 0) {
            message.flags |= 1073741824;
            message.quick_reply_shortcut_id = i;
            TLRPC.TL_inputQuickReplyShortcutId tL_inputQuickReplyShortcutId = new TLRPC.TL_inputQuickReplyShortcutId();
            tL_inputQuickReplyShortcutId.shortcut_id = i;
            this.messageOwner.quick_reply_shortcut = tL_inputQuickReplyShortcutId;
            return;
        }
        if (str != null) {
            TLRPC.TL_inputQuickReplyShortcut tL_inputQuickReplyShortcut = new TLRPC.TL_inputQuickReplyShortcut();
            tL_inputQuickReplyShortcut.shortcut = str;
            this.messageOwner.quick_reply_shortcut = tL_inputQuickReplyShortcut;
        } else {
            message.flags &= -1073741825;
            message.quick_reply_shortcut_id = 0;
            message.quick_reply_shortcut = null;
        }
    }

    public void applyTimestampsHighlightForReplyMsg() {
        boolean isOutOwner;
        CharSequence charSequence;
        int duration;
        int i;
        boolean z;
        boolean z2;
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject == null) {
            return;
        }
        if (messageObject.isYouTubeVideo()) {
            isOutOwner = isOutOwner();
            charSequence = this.messageText;
            duration = ConnectionsManager.DEFAULT_DATACENTER_ID;
            z = false;
            z2 = false;
            i = 3;
        } else {
            if (!messageObject.isVideo()) {
                if (messageObject.isMusic() || messageObject.isVoice()) {
                    addUrlsByPattern(isOutOwner(), this.messageText, false, 4, (int) messageObject.getDuration(), false);
                    return;
                }
                return;
            }
            isOutOwner = isOutOwner();
            charSequence = this.messageText;
            duration = (int) messageObject.getDuration();
            i = 3;
            z = false;
            z2 = false;
        }
        addUrlsByPattern(isOutOwner, charSequence, z2, i, duration, z);
    }

    public boolean canBeSensitive() {
        int i;
        return (this.messageOwner == null || ((i = this.type) != 1 && i != 3 && i != 9 && i != 8 && i != 5) || this.sendPreview || this.isRepostPreview || isOutOwner() || this.messageOwner.send_state != 0) ? false : true;
    }

    public boolean canDeleteMessage(boolean z, TLRPC.Chat chat) {
        TLRPC.Message message;
        return (isStory() && (message = this.messageOwner) != null && message.dialog_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) || (this.eventId == 0 && this.sponsoredId == null && canDeleteMessage(this.currentAccount, z, this.messageOwner, chat));
    }

    public boolean canEditMedia() {
        if (isSecretMedia()) {
            return false;
        }
        if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto) {
            return true;
        }
        return getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument ? (isVoice() || isSticker() || isAnimatedSticker() || isRoundVideo()) ? false : true : isMediaEmpty();
    }

    public boolean canEditMessage(TLRPC.Chat chat) {
        return canEditMessage(this.currentAccount, this.messageOwner, chat, this.scheduled);
    }

    public boolean canEditMessageAnytime(TLRPC.Chat chat) {
        return canEditMessageAnytime(this.currentAccount, this.messageOwner, chat);
    }

    public boolean canEditMessageScheduleTime(TLRPC.Chat chat) {
        return canEditMessageScheduleTime(this.currentAccount, this.messageOwner, chat);
    }

    public boolean canForwardMessage() {
        return (isQuickReply() || this.type == 30 || (this.messageOwner instanceof TLRPC.TL_message_secret) || needDrawBluredPreview() || isLiveLocation() || this.type == 16 || isSponsored() || this.messageOwner.noforwards) ? false : true;
    }

    public boolean canPreviewDocument() {
        return canPreviewDocument(getDocument());
    }

    public boolean canStreamVideo() {
        if (hasVideoQualities()) {
            return true;
        }
        TLRPC.Document document = getDocument();
        if (document != null && !(document instanceof TLRPC.TL_documentEncrypted)) {
            if (SharedConfig.streamAllVideo) {
                return true;
            }
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                    return documentAttribute.supports_streaming;
                }
            }
            if (SharedConfig.streamMkv && "video/x-matroska".equals(document.mime_type)) {
                return true;
            }
        }
        return false;
    }

    public boolean canUnvote() {
        TLRPC.TL_messageMediaPoll tL_messageMediaPoll;
        TLRPC.PollResults pollResults;
        if (this.type == 17 && (pollResults = (tL_messageMediaPoll = (TLRPC.TL_messageMediaPoll) getMedia(this.messageOwner)).results) != null && !pollResults.results.isEmpty() && !tL_messageMediaPoll.poll.quiz) {
            int size = tL_messageMediaPoll.results.results.size();
            for (int i = 0; i < size; i++) {
                if (tL_messageMediaPoll.results.results.get(i).chosen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canViewThread() {
        MessageObject messageObject;
        if (this.messageOwner.action != null) {
            return false;
        }
        return hasReplies() || !(((messageObject = this.replyMessageObject) == null || messageObject.messageOwner.replies == null) && getReplyTopMsgId() == 0);
    }

    protected void checkBigAnimatedEmoji() {
        AnimatedEmojiSpan[] animatedEmojiSpanArr;
        String str;
        int i;
        this.emojiAnimatedSticker = null;
        this.emojiAnimatedStickerId = null;
        if (this.emojiOnlyCount == 1 && !(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && !(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaInvoice) && ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaEmpty) || getMedia(this.messageOwner) == null)) {
            TLRPC.Message message = this.messageOwner;
            if (message.grouped_id == 0) {
                if (message.entities.isEmpty()) {
                    CharSequence charSequence = this.messageText;
                    int indexOf = TextUtils.indexOf(charSequence, "🏻");
                    if (indexOf >= 0) {
                        str = "_c1";
                    } else {
                        indexOf = TextUtils.indexOf(charSequence, "🏼");
                        if (indexOf >= 0) {
                            str = "_c2";
                        } else {
                            indexOf = TextUtils.indexOf(charSequence, "🏽");
                            if (indexOf >= 0) {
                                str = "_c3";
                            } else {
                                indexOf = TextUtils.indexOf(charSequence, "🏾");
                                if (indexOf >= 0) {
                                    str = "_c4";
                                } else {
                                    indexOf = TextUtils.indexOf(charSequence, "🏿");
                                    if (indexOf >= 0) {
                                        str = "_c5";
                                    } else {
                                        this.emojiAnimatedStickerColor = "";
                                        if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) && (i = indexOf + 2) < this.messageText.length()) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(charSequence.toString());
                                            CharSequence charSequence2 = this.messageText;
                                            sb.append(charSequence2.subSequence(i, charSequence2.length()).toString());
                                            charSequence = sb.toString();
                                        }
                                        if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) || EmojiData.emojiColoredMap.contains(charSequence.toString())) {
                                            this.emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(charSequence);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.emojiAnimatedStickerColor = str;
                    charSequence = charSequence.subSequence(0, indexOf);
                    if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(charSequence.toString());
                        CharSequence charSequence22 = this.messageText;
                        sb2.append(charSequence22.subSequence(i, charSequence22.length()).toString());
                        charSequence = sb2.toString();
                    }
                    if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor)) {
                    }
                    this.emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(charSequence);
                } else if (this.messageOwner.entities.size() == 1 && (this.messageOwner.entities.get(0) instanceof TLRPC.TL_messageEntityCustomEmoji)) {
                    try {
                        long j = ((TLRPC.TL_messageEntityCustomEmoji) this.messageOwner.entities.get(0)).document_id;
                        this.emojiAnimatedStickerId = Long.valueOf(j);
                        TLRPC.Document findDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, j);
                        this.emojiAnimatedSticker = findDocument;
                        if (findDocument == null) {
                            CharSequence charSequence3 = this.messageText;
                            if ((charSequence3 instanceof Spanned) && (animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence3).getSpans(0, charSequence3.length(), AnimatedEmojiSpan.class)) != null && animatedEmojiSpanArr.length == 1) {
                                this.emojiAnimatedSticker = animatedEmojiSpanArr[0].document;
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        }
        if (this.emojiAnimatedSticker == null && this.emojiAnimatedStickerId == null) {
            generateLayout(null);
        } else {
            this.type = isSticker() ? 13 : isAnimatedSticker() ? 15 : 1000;
        }
    }

    public void checkForScam() {
    }

    public boolean checkLayout() {
        CharSequence charSequence;
        int i = this.type;
        if ((i == 0 || i == 19) && this.messageOwner.peer_id != null && (charSequence = this.messageText) != null && charSequence.length() != 0) {
            if (this.layoutCreated) {
                if (Math.abs(this.generatedWithMinSize - (AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : AndroidUtilities.displaySize.x)) > AndroidUtilities.dp(52.0f) || this.generatedWithDensity != AndroidUtilities.density) {
                    this.layoutCreated = false;
                }
            }
            if (!this.layoutCreated) {
                this.layoutCreated = true;
                if (isFromUser()) {
                    MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
                }
                TextPaint textPaint = getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
                int[] iArr = allowsBigEmoji() ? new int[1] : null;
                CharSequence replaceEmoji = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), false, iArr);
                this.messageText = replaceEmoji;
                Spannable replaceAnimatedEmoji = replaceAnimatedEmoji(replaceEmoji, textPaint.getFontMetricsInt());
                this.messageText = replaceAnimatedEmoji;
                if (iArr != null && iArr[0] > 1) {
                    replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr);
                }
                checkEmojiOnly(iArr);
                checkBigAnimatedEmoji();
                setType();
                return true;
            }
        }
        return false;
    }

    public void checkMediaExistance() {
        checkMediaExistance(true);
    }

    public void checkMediaExistance(boolean z) {
        boolean exists;
        int i;
        File pathToAttach;
        this.attachPathExists = false;
        this.mediaExists = false;
        int i2 = this.type;
        if (i2 == 20) {
            TLRPC.TL_messageExtendedMediaPreview tL_messageExtendedMediaPreview = (TLRPC.TL_messageExtendedMediaPreview) this.messageOwner.media.extended_media.get(0);
            if (tL_messageExtendedMediaPreview.thumb != null) {
                File pathToAttach2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(tL_messageExtendedMediaPreview.thumb, z);
                if (!this.mediaExists) {
                    exists = pathToAttach2.exists() || (tL_messageExtendedMediaPreview.thumb instanceof TLRPC.TL_photoStrippedSize);
                    this.mediaExists = exists;
                }
            }
        } else if (i2 == 1 && FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
            File pathToMessage = FileLoader.getInstance(this.currentAccount).getPathToMessage(this.messageOwner, z);
            if (needDrawBluredPreview()) {
                this.mediaExists = new File(pathToMessage.getAbsolutePath() + ".enc").exists();
            }
            if (!this.mediaExists) {
                exists = pathToMessage.exists();
                this.mediaExists = exists;
            }
        }
        if ((!this.mediaExists && this.type == 8) || (i = this.type) == 3 || i == 9 || i == 2 || i == 14 || i == 5) {
            String str = this.messageOwner.attachPath;
            if (str != null && str.length() > 0) {
                this.attachPathExists = new File(this.messageOwner.attachPath).exists();
            }
            if (!this.attachPathExists) {
                File pathToMessage2 = FileLoader.getInstance(this.currentAccount).getPathToMessage(this.messageOwner, z);
                if ((this.type == 3 && needDrawBluredPreview()) || isVoiceOnce() || isRoundOnce()) {
                    this.mediaExists = new File(pathToMessage2.getAbsolutePath() + ".enc").exists();
                }
                if (!this.mediaExists) {
                    this.mediaExists = pathToMessage2.exists();
                }
            }
        }
        if (!this.mediaExists) {
            TLObject document = getDocument();
            if (document != null) {
                if (!isWallpaper()) {
                    pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, false, z);
                    this.mediaExists = pathToAttach.exists();
                }
                pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, true, z);
                this.mediaExists = pathToAttach.exists();
            } else {
                int i3 = this.type;
                if (i3 == 0) {
                    document = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
                    if (document == null) {
                        return;
                    }
                    pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, true, z);
                    this.mediaExists = pathToAttach.exists();
                } else if (i3 == 11) {
                    TLRPC.Photo photo = this.messageOwner.action.photo;
                    if (photo == null || photo.video_sizes.isEmpty()) {
                        return;
                    }
                    pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(photo.video_sizes.get(0), null, true, z);
                    this.mediaExists = pathToAttach.exists();
                }
            }
        }
        updateQualitiesCached(z);
    }

    public void copyStableParams(MessageObject messageObject) {
        ArrayList<TextLayoutBlock> arrayList;
        TLRPC.MessageMedia messageMedia;
        ArrayList<TLRPC.ReactionCount> arrayList2;
        TLRPC.TL_messageReactions tL_messageReactions;
        this.stableId = messageObject.stableId;
        TLRPC.Message message = this.messageOwner;
        message.premiumEffectWasPlayed = messageObject.messageOwner.premiumEffectWasPlayed;
        this.forcePlayEffect = messageObject.forcePlayEffect;
        this.wasJustSent = messageObject.wasJustSent;
        TLRPC.TL_messageReactions tL_messageReactions2 = message.reactions;
        if (tL_messageReactions2 != null && (arrayList2 = tL_messageReactions2.results) != null && !arrayList2.isEmpty() && (tL_messageReactions = messageObject.messageOwner.reactions) != null && tL_messageReactions.results != null) {
            for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
                TLRPC.ReactionCount reactionCount = this.messageOwner.reactions.results.get(i);
                for (int i2 = 0; i2 < messageObject.messageOwner.reactions.results.size(); i2++) {
                    TLRPC.ReactionCount reactionCount2 = messageObject.messageOwner.reactions.results.get(i2);
                    if (ReactionsLayoutInBubble.equalsTLReaction(reactionCount.reaction, reactionCount2.reaction)) {
                        reactionCount.lastDrawnPosition = reactionCount2.lastDrawnPosition;
                    }
                }
            }
        }
        boolean z = messageObject.isSpoilersRevealed;
        this.isSpoilersRevealed = z;
        TLRPC.Message message2 = this.messageOwner;
        TLRPC.Message message3 = messageObject.messageOwner;
        message2.replyStory = message3.replyStory;
        TLRPC.MessageMedia messageMedia2 = message2.media;
        if (messageMedia2 != null && (messageMedia = message3.media) != null) {
            messageMedia2.storyItem = messageMedia.storyItem;
        }
        if (!z || (arrayList = this.textLayoutBlocks) == null) {
            return;
        }
        Iterator<TextLayoutBlock> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().spoilers.clear();
        }
    }

    public void createMediaThumbs() {
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        TLRPC.PhotoSize closestPhotoSizeWithSize2;
        ImageLocation forDocument;
        TLRPC.MessageMedia messageMedia;
        if (isStoryMedia()) {
            TL_stories.StoryItem storyItem = getMedia(this.messageOwner).storyItem;
            if (storyItem == null || (messageMedia = storyItem.media) == null) {
                return;
            }
            TLRPC.Document document = messageMedia.document;
            if (document != null) {
                TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
                this.mediaThumb = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320, false, null, true), document);
                forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize3, document);
            } else {
                closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 50);
                closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 320, false, closestPhotoSizeWithSize, true);
                this.mediaThumb = ImageLocation.getForObject(closestPhotoSizeWithSize2, this.photoThumbsObject);
                forDocument = ImageLocation.getForObject(closestPhotoSizeWithSize, this.photoThumbsObject);
            }
        } else if (isVideo()) {
            TLRPC.Document document2 = getDocument();
            TLRPC.PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(document2.thumbs, 50);
            this.mediaThumb = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document2.thumbs, 320), document2);
            forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize4, document2);
        } else {
            if (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto) || getMedia(this.messageOwner).photo == null || this.photoThumbs.isEmpty()) {
                return;
            }
            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 50);
            closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 320, false, closestPhotoSizeWithSize, false);
            this.mediaThumb = ImageLocation.getForObject(closestPhotoSizeWithSize2, this.photoThumbsObject);
            forDocument = ImageLocation.getForObject(closestPhotoSizeWithSize, this.photoThumbsObject);
        }
        this.mediaSmallThumb = forDocument;
    }

    public void createMessageSendInfo() {
        HashMap<String, String> hashMap;
        String str;
        VideoEditedInfo videoEditedInfo = this.videoEditedInfo;
        boolean z = videoEditedInfo != null && videoEditedInfo.notReadyYet;
        TLRPC.Message message = this.messageOwner;
        if (message.message != null) {
            if ((message.id < 0 || isEditing()) && (hashMap = this.messageOwner.params) != null) {
                String str2 = hashMap.get("ve");
                if (str2 != null && (isVideo() || isNewGif() || isRoundVideo() || isVideoSticker() || isPaidVideo(getMedia(this)))) {
                    VideoEditedInfo videoEditedInfo2 = new VideoEditedInfo();
                    this.videoEditedInfo = videoEditedInfo2;
                    if (videoEditedInfo2.parseString(str2)) {
                        this.videoEditedInfo.roundVideo = isRoundVideo();
                        this.videoEditedInfo.notReadyYet = z;
                    } else {
                        this.videoEditedInfo = null;
                    }
                }
                TLRPC.Message message2 = this.messageOwner;
                if (message2.send_state != 3 || (str = message2.params.get("prevMedia")) == null) {
                    return;
                }
                SerializedData serializedData = new SerializedData(Base64.decode(str, 0));
                this.previousMedia = TLRPC.MessageMedia.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                this.previousMessage = serializedData.readString(false);
                this.previousAttachPath = serializedData.readString(false);
                int readInt32 = serializedData.readInt32(false);
                this.previousMessageEntities = new ArrayList<>(readInt32);
                for (int i = 0; i < readInt32; i++) {
                    this.previousMessageEntities.add(TLRPC.MessageEntity.TLdeserialize(serializedData, serializedData.readInt32(false), false));
                }
                serializedData.cleanup();
            }
        }
    }

    public void createStrippedThumb() {
        if (this.photoThumbs != null) {
            if ((canCreateStripedThubms() || hasExtendedMediaPreview()) && this.strippedThumb == null) {
                try {
                    String str = isRoundVideo() ? "br" : "b";
                    int size = this.photoThumbs.size();
                    for (int i = 0; i < size; i++) {
                        TLRPC.PhotoSize photoSize = this.photoThumbs.get(i);
                        if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                            this.strippedThumb = new BitmapDrawable(ApplicationLoader.applicationContext.getResources(), ImageLoader.getStrippedPhotoBitmap(photoSize.bytes, str));
                            return;
                        }
                    }
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
    }

    public boolean didSpoilLoginCode() {
        return this.spoiledLoginCode;
    }

    public boolean doesPaidReactionExist() {
        TLRPC.Message message = this.messageOwner;
        if (message.reactions == null) {
            message.reactions = new TLRPC.TL_messageReactions();
            TLRPC.Message message2 = this.messageOwner;
            message2.reactions.reactions_as_tags = getDialogId(message2) == UserConfig.getInstance(this.currentAccount).getClientUserId();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (this.messageOwner.reactions.results.get(i).reaction instanceof TLRPC.TL_reactionPaid) {
                return true;
            }
        }
        return false;
    }

    public boolean ensurePaidReactionsExist(boolean z) {
        TLRPC.Message message = this.messageOwner;
        if (message.reactions == null) {
            message.reactions = new TLRPC.TL_messageReactions();
            TLRPC.Message message2 = this.messageOwner;
            message2.reactions.reactions_as_tags = getDialogId(message2) == UserConfig.getInstance(this.currentAccount).getClientUserId();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        TLRPC.ReactionCount reactionCount = null;
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (this.messageOwner.reactions.results.get(i).reaction instanceof TLRPC.TL_reactionPaid) {
                reactionCount = this.messageOwner.reactions.results.get(i);
            }
        }
        if (reactionCount != null) {
            return false;
        }
        TLRPC.TL_reactionCount tL_reactionCount = new TLRPC.TL_reactionCount();
        tL_reactionCount.reaction = new TLRPC.TL_reactionPaid();
        tL_reactionCount.count = 1;
        tL_reactionCount.chosen = z;
        this.messageOwner.reactions.results.add(0, tL_reactionCount);
        return true;
    }

    public boolean equals(MessageObject messageObject) {
        return messageObject != null && getId() == messageObject.getId() && getDialogId() == messageObject.getDialogId();
    }

    public void expandChannelRecommendations(boolean z) {
        SharedPreferences.Editor edit = MessagesController.getInstance(this.currentAccount).getMainSettings().edit();
        String str = "c" + getDialogId() + "_rec";
        this.channelJoinedExpanded = z;
        edit.putBoolean(str, z).apply();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x004b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateCaption() {
        boolean z;
        boolean z2;
        boolean isOutOwner;
        CharSequence charSequence;
        int duration;
        int i;
        TL_stories.StoryItem storyItem;
        if ((this.caption == null || this.translated != this.captionTranslated) && !isRoundVideo()) {
            TLRPC.Message message = this.messageOwner;
            String str = message.message;
            ArrayList<TLRPC.MessageEntity> arrayList = message.entities;
            if (this.type == 23) {
                TLRPC.MessageMedia messageMedia = message.media;
                if (messageMedia != null && (storyItem = messageMedia.storyItem) != null) {
                    str = storyItem.caption;
                    arrayList = storyItem.entities;
                    z = true;
                    z2 = this.translated;
                    this.captionTranslated = z2;
                    if (z2) {
                        TLRPC.TL_textWithEntities tL_textWithEntities = this.messageOwner.translatedText;
                        String str2 = tL_textWithEntities.text;
                        arrayList = tL_textWithEntities.entities;
                        str = str2;
                    }
                    if (!isMediaEmpty() || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame) || TextUtils.isEmpty(str)) {
                        return;
                    }
                    CharSequence replaceEmoji = Emoji.replaceEmoji((CharSequence) str, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    this.caption = replaceEmoji;
                    this.caption = replaceAnimatedEmoji(replaceEmoji, arrayList, Theme.chat_msgTextPaint.getFontMetricsInt(), false);
                    boolean z3 = z || (!(this.messageOwner.send_state != 0 ? false : arrayList.isEmpty() ^ true) && (this.eventId != 0 || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto_old) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto_layer68) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto_layer74) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument_old) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument_layer68) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument_layer74) || ((isOut() && this.messageOwner.send_state != 0) || this.messageOwner.id < 0)));
                    if (z3) {
                        if (containsUrls(this.caption)) {
                            try {
                                AndroidUtilities.addLinksSafe((Spannable) this.caption, 5, false, true);
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                        addUrlsByPattern(isOutOwner(), this.caption, true, 0, 0, true);
                    }
                    addEntitiesToText(this.caption, z3);
                    if (isVideo()) {
                        isOutOwner = isOutOwner();
                        charSequence = this.caption;
                        duration = (int) getDuration();
                        i = 3;
                    } else {
                        if (!isMusic() && !isVoice()) {
                            return;
                        }
                        isOutOwner = isOutOwner();
                        charSequence = this.caption;
                        duration = (int) getDuration();
                        i = 4;
                    }
                    addUrlsByPattern(isOutOwner, charSequence, true, i, duration, false);
                    return;
                }
                arrayList = new ArrayList<>();
                str = "";
            } else if (hasExtendedMedia()) {
                TLRPC.Message message2 = this.messageOwner;
                str = message2.media.description;
                message2.message = str;
            }
            z = false;
            z2 = this.translated;
            this.captionTranslated = z2;
            if (z2) {
            }
            if (isMediaEmpty()) {
            }
        }
    }

    public void generateGameMessageText(TLRPC.User user) {
        CharSequence replaceWithLink;
        if (user == null && isFromUser()) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
        }
        MessageObject messageObject = this.replyMessageObject;
        TLRPC.TL_game tL_game = (messageObject == null || getMedia(messageObject) == null || getMedia(this.replyMessageObject).game == null) ? null : getMedia(this.replyMessageObject).game;
        if (tL_game == null) {
            replaceWithLink = (user == null || user.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) ? replaceWithLink(LocaleController.formatString("ActionUserScored", R.string.ActionUserScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", user) : LocaleController.formatString("ActionYouScored", R.string.ActionYouScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
        } else {
            this.messageText = (user == null || user.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) ? replaceWithLink(LocaleController.formatString("ActionUserScoredInGame", R.string.ActionUserScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", user) : LocaleController.formatString("ActionYouScoredInGame", R.string.ActionYouScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
            replaceWithLink = replaceWithLink(this.messageText, "un2", tL_game);
        }
        this.messageText = replaceWithLink;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:266|(3:267|268|269)|270|(1:272)(11:302|(1:304)|274|275|276|(1:278)|279|(2:281|(3:283|(5:287|288|(1:293)|290|291)|292))(1:299)|298|(1:297)(6:285|287|288|(0)|290|291)|292)|273|274|275|276|(0)|279|(0)(0)|298|(0)(0)|292) */
    /* JADX WARN: Can't wrap try/catch for region: R(48:144|(1:146)|147|(1:149)(1:419)|150|(1:152)(1:418)|153|(1:155)|(1:157)|(1:417)(1:162)|163|(1:416)(1:170)|171|(2:173|(3:(1:398)|399|400)(1:176))(2:401|(8:403|(1:405)(1:415)|406|(1:408)(1:414)|409|(1:411)(1:413)|412|400))|177|(3:179|(1:181)(1:(1:394)(1:395))|182)(1:396)|183|(1:185)(2:389|(1:391)(1:392))|186|(5:188|(1:365)(8:194|(1:196)(1:364)|197|198|(1:200)(1:363)|201|(1:203)(1:362)|204)|205|(3:207|(2:209|(2:211|(1:213))(1:215))(1:216)|214)|217)(3:366|(2:368|369)(8:370|371|372|(1:383)(1:376)|377|378|(1:380)(1:382)|381)|331)|218|219|220|221|(2:225|226)|358|232|233|234|(1:236)(17:352|(1:354)|238|(1:240)|241|(1:243)|244|(3:246|(7:248|249|250|251|252|254|255)|261)|262|(6:264|(16:266|267|268|269|270|(1:272)(11:302|(1:304)|274|275|276|(1:278)|279|(2:281|(3:283|(5:287|288|(1:293)|290|291)|292))(1:299)|298|(1:297)(6:285|287|288|(0)|290|291)|292)|273|274|275|276|(0)|279|(0)(0)|298|(0)(0)|292)|307|308|(2:(1:311)|312)(1:(1:338))|313)(3:339|(5:341|(1:343)(1:350)|344|(1:346)(1:349)|347)(1:351)|348)|314|(3:316|(1:318)(1:320)|319)|321|(1:336)(3:325|(1:327)(3:332|(1:334)|335)|328)|329|330|331)|237|238|(0)|241|(0)|244|(0)|262|(0)(0)|314|(0)|321|(1:323)|336|329|330|331|142) */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x05f6, code lost:
    
        r13 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x0550, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0551, code lost:
    
        org.telegram.messenger.FileLog.e(r0);
        r0 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x053d, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x053e, code lost:
    
        r11 = 0.0f;
     */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x023c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0243  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0257  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x02f1  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0559  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x0578  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x057d  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x0597  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x05bf  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x05ff  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x0606  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x0630  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x063e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:297:0x063e A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:299:0x061b  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x06d7  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x0705  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x068d  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x0562  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x0755  */
    /* JADX WARN: Removed duplicated region for block: B:438:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:452:0x02b3  */
    /* JADX WARN: Removed duplicated region for block: B:455:0x02b6  */
    /* JADX WARN: Removed duplicated region for block: B:463:0x0245  */
    /* JADX WARN: Removed duplicated region for block: B:465:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:471:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:472:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x014a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLayout(TLRPC.User user) {
        boolean z;
        int dp;
        CharSequence charSequence;
        int i;
        float f;
        boolean z2;
        ArrayList arrayList;
        int i2;
        boolean z3;
        int i3;
        int i4;
        boolean z4;
        int dp2;
        TextPaint textPaint;
        boolean z5;
        TextPaint textPaint2;
        ArrayList arrayList2;
        CharSequence charSequence2;
        float f2;
        float dp3;
        int ceil;
        int i5;
        StaticLayout staticLayout;
        int i6;
        Text text;
        int i7;
        ArrayList arrayList3;
        float f3;
        float f4;
        int i8;
        int i9;
        float f5;
        int i10;
        int i11;
        float f6;
        int i12 = this.type;
        if ((i12 != 0 && i12 != 19 && i12 != 24) || this.messageOwner.peer_id == null || TextUtils.isEmpty(this.messageText)) {
            return;
        }
        applyEntities();
        TLRPC.Message message = this.messageOwner;
        boolean z6 = message != null && message.noforwards;
        if (z6) {
            z = z6;
        } else {
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()));
            z = chat != null && chat.noforwards;
        }
        this.textLayoutBlocks = new ArrayList<>();
        this.textWidth = 0;
        CharSequence charSequence3 = this.messageText;
        this.hasCode = (charSequence3 instanceof Spanned) && ((CodeHighlighting.Span[]) ((Spanned) charSequence3).getSpans(0, charSequence3.length(), CodeHighlighting.Span.class)).length > 0;
        CharSequence charSequence4 = this.messageText;
        this.hasQuote = (charSequence4 instanceof Spanned) && ((QuoteSpan.QuoteStyleSpan[]) ((Spanned) charSequence4).getSpans(0, charSequence4.length(), QuoteSpan.QuoteStyleSpan.class)).length > 0;
        this.hasSingleQuote = false;
        this.hasSingleCode = false;
        CharSequence charSequence5 = this.messageText;
        if (charSequence5 instanceof Spanned) {
            Spanned spanned = (Spanned) charSequence5;
            QuoteSpan[] quoteSpanArr = (QuoteSpan[]) spanned.getSpans(0, spanned.length(), QuoteSpan.class);
            for (QuoteSpan quoteSpan : quoteSpanArr) {
                quoteSpan.adaptLineHeight = false;
            }
            this.hasSingleQuote = quoteSpanArr.length == 1 && spanned.getSpanStart(quoteSpanArr[0]) == 0 && spanned.getSpanEnd(quoteSpanArr[0]) == spanned.length();
            CodeHighlighting.Span[] spanArr = (CodeHighlighting.Span[]) spanned.getSpans(0, spanned.length(), CodeHighlighting.Span.class);
            this.hasSingleCode = spanArr.length == 1 && spanned.getSpanStart(spanArr[0]) == 0 && spanned.getSpanEnd(spanArr[0]) == spanned.length();
        }
        int maxMessageTextWidth = getMaxMessageTextWidth();
        try {
            if (!this.hasSingleQuote) {
                if (this.hasSingleCode) {
                    dp = AndroidUtilities.dp(15.0f);
                }
                TextPaint textPaint3 = !(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame) ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
                CharSequence charSequence6 = this.messageText;
                StaticLayout makeStaticLayout = makeStaticLayout(charSequence6, textPaint3, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f, this.emojiOnlyCount <= 0);
                charSequence = charSequence6;
                if (this.isRepostPreview) {
                    int i13 = this.type != 0 ? hasValidGroupId() ? 7 : 12 : 22;
                    if (isWebpage()) {
                        i13 -= 8;
                    }
                    charSequence = charSequence6;
                    if (makeStaticLayout.getLineCount() > i13) {
                        String string = LocaleController.getString(R.string.ReadMore);
                        int ceil2 = (int) Math.ceil(textPaint3.measureText("… " + string) + AndroidUtilities.dp(1.0f));
                        float f7 = 0.0f;
                        for (int i14 = 0; i14 < i13; i14++) {
                            f7 = Math.max(f7, makeStaticLayout.getLineRight(i14));
                        }
                        int i15 = i13 - 1;
                        int lineStart = makeStaticLayout.getLineStart(i15);
                        int lineEnd = makeStaticLayout.getLineEnd(i15) - 1;
                        while (lineEnd >= lineStart && makeStaticLayout.getPrimaryHorizontal(lineEnd) >= f7 - ceil2) {
                            lineEnd--;
                        }
                        while (lineEnd >= lineStart && !Character.isWhitespace(charSequence6.charAt(lineEnd))) {
                            lineEnd--;
                        }
                        SpannableStringBuilder append = new SpannableStringBuilder(charSequence6.subSequence(0, lineEnd)).append((CharSequence) "… ").append((CharSequence) string);
                        append.setSpan(new CharacterStyle() { // from class: org.telegram.messenger.MessageObject.2
                            @Override // android.text.style.CharacterStyle
                            public void updateDrawState(TextPaint textPaint4) {
                                textPaint4.setColor(Theme.chat_msgTextPaint.linkColor);
                            }
                        }, append.length() - string.length(), append.length(), 33);
                        try {
                            makeStaticLayout = makeStaticLayout(append, textPaint3, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount >= 4 ? -1.0f : 0.0f, this.emojiOnlyCount > 0);
                            charSequence = append;
                        } catch (Exception e) {
                            FileLog.e(e);
                            return;
                        }
                    }
                }
                if (!this.hasSingleQuote) {
                    f = 32.0f;
                } else {
                    if (!this.hasSingleCode) {
                        i = maxMessageTextWidth;
                        int lineCount = makeStaticLayout.getLineCount();
                        int i16 = this.totalAnimatedEmojiCount;
                        int i17 = i16 >= 50 ? 5 : 10;
                        z2 = Build.VERSION.SDK_INT < 24 && i16 < 50;
                        int ceil3 = z2 ? 1 : (int) Math.ceil(lineCount / i17);
                        arrayList = new ArrayList();
                        if (!(charSequence instanceof Spanned) && (this.hasQuote || this.hasCode)) {
                            cutIntoRanges(charSequence, arrayList);
                        } else {
                            if (!z2 || ceil3 == 1) {
                                i2 = maxMessageTextWidth;
                                z3 = false;
                                arrayList.add(new TextRange(0, makeStaticLayout.getText().length()));
                                int size = arrayList.size();
                                this.hasCodeAtTop = z3;
                                this.hasCodeAtBottom = z3;
                                this.hasQuoteAtBottom = z3;
                                this.hasSingleQuote = z3;
                                this.hasSingleCode = z3;
                                int i18 = i2;
                                i4 = 0;
                                CharSequence charSequence7 = charSequence;
                                while (i4 < arrayList.size()) {
                                    TextLayoutBlock textLayoutBlock = new TextLayoutBlock();
                                    TextRange textRange = (TextRange) arrayList.get(i4);
                                    textLayoutBlock.code = textRange.code;
                                    textLayoutBlock.quote = textRange.quote;
                                    boolean z7 = textRange.collapse;
                                    textLayoutBlock.quoteCollapse = z7;
                                    if (z7) {
                                        textLayoutBlock.messageObject = this;
                                    }
                                    textLayoutBlock.index = i4;
                                    textLayoutBlock.first = i4 == 0;
                                    boolean z8 = i4 == arrayList.size() - 1;
                                    textLayoutBlock.last = z8;
                                    boolean z9 = textLayoutBlock.first;
                                    if (z9) {
                                        this.hasCodeAtTop = textLayoutBlock.code;
                                    }
                                    if (z8) {
                                        this.hasQuoteAtBottom = textLayoutBlock.quote;
                                        this.hasCodeAtBottom = textLayoutBlock.code;
                                    }
                                    this.hasSingleQuote = z9 && z8 && textLayoutBlock.quote;
                                    this.hasSingleCode = z9 && z8 && !textLayoutBlock.quote && textLayoutBlock.code;
                                    if (textLayoutBlock.quote) {
                                        if (z9 && z8) {
                                            int dp4 = AndroidUtilities.dp(6.0f);
                                            textLayoutBlock.padBottom = dp4;
                                            textLayoutBlock.padTop = dp4;
                                        } else {
                                            textLayoutBlock.padTop = AndroidUtilities.dp(z9 ? 8.0f : 6.0f);
                                            dp2 = AndroidUtilities.dp(7.0f);
                                            textLayoutBlock.padBottom = dp2;
                                        }
                                    } else if (textLayoutBlock.code) {
                                        textLayoutBlock.layoutCode(textRange.language, textRange.end - textRange.start, z);
                                        textLayoutBlock.padTop = AndroidUtilities.dp(4.0f) + textLayoutBlock.languageHeight + (textLayoutBlock.first ? 0 : AndroidUtilities.dp(5.0f));
                                        dp2 = AndroidUtilities.dp(4.0f) + (textLayoutBlock.last ? 0 : AndroidUtilities.dp(7.0f)) + (textLayoutBlock.hasCodeCopyButton ? AndroidUtilities.dp(38.0f) : 0);
                                        textLayoutBlock.padBottom = dp2;
                                    }
                                    if (textLayoutBlock.code) {
                                        int i19 = textRange.end - textRange.start;
                                        textPaint = i19 > 220 ? Theme.chat_msgTextCode3Paint : i19 > 80 ? Theme.chat_msgTextCode2Paint : Theme.chat_msgTextCodePaint;
                                    } else {
                                        textPaint = textPaint3;
                                    }
                                    CharSequence subSequence = charSequence7.subSequence(textRange.start, textRange.end);
                                    int dp5 = textLayoutBlock.quote ? i - AndroidUtilities.dp(24.0f) : textLayoutBlock.code ? i - AndroidUtilities.dp(15.0f) : i;
                                    if (size == 1) {
                                        if (textLayoutBlock.code && !textLayoutBlock.quote && (makeStaticLayout.getText() instanceof Spannable)) {
                                            SpannableString highlighted = !TextUtils.isEmpty(textRange.language) ? CodeHighlighting.getHighlighted(subSequence.toString(), textRange.language) : new SpannableString(subSequence.toString());
                                            textLayoutBlock.originalWidth = dp5;
                                            makeStaticLayout = makeStaticLayout(highlighted, textPaint, dp5, 1.0f, this.totalAnimatedEmojiCount >= 4 ? -1.0f : 0.0f, this.emojiOnlyCount > 0);
                                            i18 = dp5;
                                        } else {
                                            textLayoutBlock.originalWidth = i18;
                                        }
                                        textLayoutBlock.textLayout = makeStaticLayout;
                                        textLayoutBlock.charactersOffset = 0;
                                        textLayoutBlock.charactersEnd = makeStaticLayout.getText().length();
                                        textLayoutBlock.height = makeStaticLayout.getHeight();
                                        textLayoutBlock.collapsedHeight = (int) Math.min(textPaint3.getTextSize() * 1.4f * 3.0f, textLayoutBlock.height);
                                        int i20 = this.emojiOnlyCount;
                                        if (i20 != 0) {
                                            if (i20 == 1) {
                                                i11 = textLayoutBlock.padTop;
                                                f6 = 5.3f;
                                            } else if (i20 == 2) {
                                                i11 = textLayoutBlock.padTop;
                                                f6 = 4.5f;
                                            } else if (i20 == 3) {
                                                i11 = textLayoutBlock.padTop;
                                                f6 = 4.2f;
                                            }
                                            textLayoutBlock.padTop = i11 - AndroidUtilities.dp(f6);
                                        }
                                    } else {
                                        int i21 = textRange.start;
                                        int i22 = textRange.end;
                                        if (i22 < i21) {
                                            z5 = z;
                                            textPaint2 = textPaint3;
                                            arrayList2 = arrayList;
                                            charSequence2 = charSequence7;
                                        } else {
                                            textLayoutBlock.charactersOffset = i21;
                                            textLayoutBlock.charactersEnd = i22;
                                            try {
                                                SpannableString valueOf = (!textLayoutBlock.code || textLayoutBlock.quote) ? SpannableString.valueOf(subSequence) : CodeHighlighting.getHighlighted(subSequence.toString(), textRange.language);
                                                textLayoutBlock.originalWidth = dp5;
                                                StaticLayout makeStaticLayout2 = makeStaticLayout(valueOf, textPaint, dp5, 1.0f, this.totalAnimatedEmojiCount >= 4 ? -1.0f : 0.0f, false);
                                                textLayoutBlock.textLayout = makeStaticLayout2;
                                                textLayoutBlock.height = makeStaticLayout2.getHeight();
                                                textLayoutBlock.collapsedHeight = (int) Math.min(textPaint3.getTextSize() * 1.4f * 3.0f, textLayoutBlock.height);
                                            } catch (Exception e2) {
                                                z5 = z;
                                                textPaint2 = textPaint3;
                                                arrayList2 = arrayList;
                                                charSequence2 = charSequence7;
                                                FileLog.e(e2);
                                            }
                                        }
                                        i4++;
                                        z = z5;
                                        textPaint3 = textPaint2;
                                        arrayList = arrayList2;
                                        charSequence7 = charSequence2;
                                    }
                                    this.textLayoutBlocks.add(textLayoutBlock);
                                    int lineCount2 = textLayoutBlock.textLayout.getLineCount();
                                    float lineLeft = textLayoutBlock.textLayout.getLineLeft(lineCount2 - 1);
                                    float f8 = 0.0f;
                                    if (i4 == 0 && lineLeft >= 0.0f) {
                                        try {
                                            this.textXOffset = lineLeft;
                                        } catch (Exception e3) {
                                            e = e3;
                                            if (i4 == 0) {
                                                this.textXOffset = f8;
                                            }
                                            FileLog.e(e);
                                            f2 = 0.0f;
                                            float f9 = textLayoutBlock.textLayout.getLineWidth(lineCount2 - 1);
                                            if (textLayoutBlock.quote) {
                                            }
                                            f9 += dp3;
                                            ceil = (int) Math.ceil(f9);
                                            if (ceil > i + 80) {
                                            }
                                            i5 = size - 1;
                                            if (i4 == i5) {
                                            }
                                            float f10 = ceil;
                                            int i23 = i18;
                                            z5 = z;
                                            textPaint2 = textPaint3;
                                            int ceil4 = (int) Math.ceil(f10 + Math.max(0.0f, f2));
                                            if (textLayoutBlock.quote) {
                                            }
                                            int i24 = ceil;
                                            if (lineCount2 > 1) {
                                            }
                                            text = textLayoutBlock.languageLayout;
                                            if (text != null) {
                                            }
                                            textLayoutBlock.spoilers.clear();
                                            if (this.isSpoilersRevealed) {
                                            }
                                            i18 = i23;
                                            makeStaticLayout = staticLayout;
                                            i4++;
                                            z = z5;
                                            textPaint3 = textPaint2;
                                            arrayList = arrayList2;
                                            charSequence7 = charSequence2;
                                        }
                                    }
                                    f2 = lineLeft;
                                    float f92 = textLayoutBlock.textLayout.getLineWidth(lineCount2 - 1);
                                    if (textLayoutBlock.quote) {
                                        if (textLayoutBlock.code) {
                                            dp3 = AndroidUtilities.dp(15.0f);
                                        }
                                        ceil = (int) Math.ceil(f92);
                                        if (ceil > i + 80) {
                                            ceil = i;
                                        }
                                        i5 = size - 1;
                                        if (i4 == i5) {
                                            this.lastLineWidth = ceil;
                                        }
                                        float f102 = ceil;
                                        int i232 = i18;
                                        z5 = z;
                                        textPaint2 = textPaint3;
                                        int ceil42 = (int) Math.ceil(f102 + Math.max(0.0f, f2));
                                        if (textLayoutBlock.quote) {
                                            textLayoutBlock.maxRight = 0.0f;
                                            int i25 = 0;
                                            while (i25 < lineCount2) {
                                                try {
                                                    i10 = ceil;
                                                    try {
                                                        textLayoutBlock.maxRight = Math.max(textLayoutBlock.maxRight, textLayoutBlock.textLayout.getLineRight(i25));
                                                    } catch (Exception unused) {
                                                        textLayoutBlock.maxRight = this.textWidth;
                                                        i25++;
                                                        ceil = i10;
                                                    }
                                                } catch (Exception unused2) {
                                                    i10 = ceil;
                                                }
                                                i25++;
                                                ceil = i10;
                                            }
                                        }
                                        int i242 = ceil;
                                        if (lineCount2 > 1) {
                                            staticLayout = makeStaticLayout;
                                            int i26 = i242;
                                            int i27 = 0;
                                            float f11 = 0.0f;
                                            float f12 = 0.0f;
                                            boolean z10 = false;
                                            int i28 = ceil42;
                                            CharSequence charSequence8 = charSequence7;
                                            while (i27 < lineCount2) {
                                                int i29 = lineCount2;
                                                try {
                                                    f3 = textLayoutBlock.textLayout.getLineWidth(i27);
                                                    arrayList3 = arrayList;
                                                } catch (Exception unused3) {
                                                    arrayList3 = arrayList;
                                                    f3 = 0.0f;
                                                }
                                                CharSequence charSequence9 = charSequence8;
                                                if (!textLayoutBlock.quote) {
                                                    f5 = textLayoutBlock.code ? 15.0f : 32.0f;
                                                    f4 = textLayoutBlock.textLayout.getLineLeft(i27);
                                                    if (f3 > i + 20) {
                                                        f3 = i;
                                                        f4 = 0.0f;
                                                    }
                                                    if (f4 > 0.0f) {
                                                        i8 = i;
                                                        if (textLayoutBlock.textLayout.getParagraphDirection(i27) != -1) {
                                                            textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                                            i9 = 1;
                                                            if (z10 && f4 == 0.0f) {
                                                                try {
                                                                    if (textLayoutBlock.textLayout.getParagraphDirection(i27) != i9) {
                                                                    }
                                                                } catch (Exception unused4) {
                                                                }
                                                                z10 = true;
                                                            }
                                                            f11 = Math.max(f11, f3);
                                                            float f13 = f4 + f3;
                                                            f12 = Math.max(f12, f13);
                                                            i26 = Math.max(i26, (int) Math.ceil(f3));
                                                            i28 = Math.max(i28, (int) Math.ceil(f13));
                                                            i27++;
                                                            lineCount2 = i29;
                                                            arrayList = arrayList3;
                                                            charSequence8 = charSequence9;
                                                            i = i8;
                                                        }
                                                    } else {
                                                        i8 = i;
                                                    }
                                                    this.textXOffset = Math.min(this.textXOffset, f4);
                                                    i9 = 1;
                                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                                    this.hasRtl = true;
                                                    if (z10) {
                                                        if (textLayoutBlock.textLayout.getParagraphDirection(i27) != i9) {
                                                        }
                                                        z10 = true;
                                                    }
                                                    f11 = Math.max(f11, f3);
                                                    float f132 = f4 + f3;
                                                    f12 = Math.max(f12, f132);
                                                    i26 = Math.max(i26, (int) Math.ceil(f3));
                                                    i28 = Math.max(i28, (int) Math.ceil(f132));
                                                    i27++;
                                                    lineCount2 = i29;
                                                    arrayList = arrayList3;
                                                    charSequence8 = charSequence9;
                                                    i = i8;
                                                }
                                                f3 += AndroidUtilities.dp(f5);
                                                f4 = textLayoutBlock.textLayout.getLineLeft(i27);
                                                if (f3 > i + 20) {
                                                }
                                                if (f4 > 0.0f) {
                                                }
                                                this.textXOffset = Math.min(this.textXOffset, f4);
                                                i9 = 1;
                                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                                this.hasRtl = true;
                                                if (z10) {
                                                }
                                                f11 = Math.max(f11, f3);
                                                float f1322 = f4 + f3;
                                                f12 = Math.max(f12, f1322);
                                                i26 = Math.max(i26, (int) Math.ceil(f3));
                                                i28 = Math.max(i28, (int) Math.ceil(f1322));
                                                i27++;
                                                lineCount2 = i29;
                                                arrayList = arrayList3;
                                                charSequence8 = charSequence9;
                                                i = i8;
                                            }
                                            int i30 = i;
                                            arrayList2 = arrayList;
                                            charSequence2 = charSequence8;
                                            if (z10) {
                                                if (i4 == i5) {
                                                    this.lastLineWidth = ceil42;
                                                }
                                                f11 = f12;
                                            } else if (i4 == i5) {
                                                this.lastLineWidth = i26;
                                            }
                                            this.textWidth = Math.max(this.textWidth, (int) Math.ceil(f11));
                                            ceil42 = i28;
                                            i = i30;
                                        } else {
                                            int i31 = i;
                                            staticLayout = makeStaticLayout;
                                            arrayList2 = arrayList;
                                            charSequence2 = charSequence7;
                                            if (f2 > 0.0f) {
                                                float min = Math.min(this.textXOffset, f2);
                                                this.textXOffset = min;
                                                i6 = min == 0.0f ? (int) (f102 + f2) : i242;
                                                this.hasRtl = size != 1;
                                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                            } else {
                                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                                i6 = i242;
                                            }
                                            i = i31;
                                            this.textWidth = Math.max(this.textWidth, Math.min(i, i6));
                                        }
                                        text = textLayoutBlock.languageLayout;
                                        if (text != null) {
                                            this.textWidth = (int) Math.max(this.textWidth, Math.min(text.getCurrentWidth() + AndroidUtilities.dp(15.0f), textLayoutBlock.textLayout == null ? 0.0f : r8.getWidth()));
                                        }
                                        textLayoutBlock.spoilers.clear();
                                        if (this.isSpoilersRevealed && !this.spoiledLoginCode) {
                                            if (textLayoutBlock.quote) {
                                                i7 = ceil42 - AndroidUtilities.dp(32.0f);
                                            } else {
                                                if (textLayoutBlock.code) {
                                                    ceil42 -= AndroidUtilities.dp(15.0f);
                                                }
                                                i7 = ceil42;
                                            }
                                            SpoilerEffect.addSpoilers(null, textLayoutBlock.textLayout, -1, i7, null, textLayoutBlock.spoilers);
                                        }
                                        i18 = i232;
                                        makeStaticLayout = staticLayout;
                                        i4++;
                                        z = z5;
                                        textPaint3 = textPaint2;
                                        arrayList = arrayList2;
                                        charSequence7 = charSequence2;
                                    } else {
                                        dp3 = AndroidUtilities.dp(32.0f);
                                    }
                                    f92 += dp3;
                                    ceil = (int) Math.ceil(f92);
                                    if (ceil > i + 80) {
                                    }
                                    i5 = size - 1;
                                    if (i4 == i5) {
                                    }
                                    float f1022 = ceil;
                                    int i2322 = i18;
                                    z5 = z;
                                    textPaint2 = textPaint3;
                                    int ceil422 = (int) Math.ceil(f1022 + Math.max(0.0f, f2));
                                    if (textLayoutBlock.quote) {
                                    }
                                    int i2422 = ceil;
                                    if (lineCount2 > 1) {
                                    }
                                    text = textLayoutBlock.languageLayout;
                                    if (text != null) {
                                    }
                                    textLayoutBlock.spoilers.clear();
                                    if (this.isSpoilersRevealed) {
                                    }
                                    i18 = i2322;
                                    makeStaticLayout = staticLayout;
                                    i4++;
                                    z = z5;
                                    textPaint3 = textPaint2;
                                    arrayList = arrayList2;
                                    charSequence7 = charSequence2;
                                }
                                if (this.hasCode) {
                                    if (this.textWidth > this.generatedWithMinSize - AndroidUtilities.dp(80 + ((!needDrawAvatarInternal() || isOutOwner() || this.messageOwner.isThreadMessage) ? 0 : 52))) {
                                        z4 = true;
                                        this.hasWideCode = z4;
                                        this.factCheckText = null;
                                        return;
                                    }
                                }
                                z4 = false;
                                this.hasWideCode = z4;
                                this.factCheckText = null;
                                return;
                            }
                            int i32 = 0;
                            int i33 = 0;
                            while (i33 < ceil3) {
                                int min2 = z2 ? lineCount : Math.min(i17, lineCount - i32);
                                int lineStart2 = makeStaticLayout.getLineStart(i32);
                                int i34 = min2 + i32;
                                int i35 = maxMessageTextWidth;
                                int lineEnd2 = makeStaticLayout.getLineEnd(i34 - 1);
                                int i36 = i32;
                                int i37 = lineEnd2 - 1;
                                if (i37 >= 0) {
                                    i3 = lineCount;
                                    if (i37 < makeStaticLayout.getText().length()) {
                                        if (makeStaticLayout.getText().charAt(i37) == '\n') {
                                            lineEnd2--;
                                        }
                                        if (lineEnd2 >= lineStart2) {
                                            i32 = i36;
                                        } else {
                                            arrayList.add(new TextRange(lineStart2, lineEnd2));
                                            i32 = i34;
                                        }
                                        i33++;
                                        maxMessageTextWidth = i35;
                                        lineCount = i3;
                                    }
                                } else {
                                    i3 = lineCount;
                                }
                                if (lineEnd2 >= lineStart2) {
                                }
                                i33++;
                                maxMessageTextWidth = i35;
                                lineCount = i3;
                            }
                        }
                        i2 = maxMessageTextWidth;
                        z3 = false;
                        int size2 = arrayList.size();
                        this.hasCodeAtTop = z3;
                        this.hasCodeAtBottom = z3;
                        this.hasQuoteAtBottom = z3;
                        this.hasSingleQuote = z3;
                        this.hasSingleCode = z3;
                        int i182 = i2;
                        i4 = 0;
                        CharSequence charSequence72 = charSequence;
                        while (i4 < arrayList.size()) {
                        }
                        if (this.hasCode) {
                        }
                        z4 = false;
                        this.hasWideCode = z4;
                        this.factCheckText = null;
                        return;
                    }
                    f = 15.0f;
                }
                i = AndroidUtilities.dp(f) + maxMessageTextWidth;
                int lineCount3 = makeStaticLayout.getLineCount();
                int i162 = this.totalAnimatedEmojiCount;
                if (i162 >= 50) {
                }
                if (Build.VERSION.SDK_INT < 24) {
                }
                if (z2) {
                }
                arrayList = new ArrayList();
                if (!(charSequence instanceof Spanned)) {
                }
                if (z2) {
                }
                i2 = maxMessageTextWidth;
                z3 = false;
                arrayList.add(new TextRange(0, makeStaticLayout.getText().length()));
                int size22 = arrayList.size();
                this.hasCodeAtTop = z3;
                this.hasCodeAtBottom = z3;
                this.hasQuoteAtBottom = z3;
                this.hasSingleQuote = z3;
                this.hasSingleCode = z3;
                int i1822 = i2;
                i4 = 0;
                CharSequence charSequence722 = charSequence;
                while (i4 < arrayList.size()) {
                }
                if (this.hasCode) {
                }
                z4 = false;
                this.hasWideCode = z4;
                this.factCheckText = null;
                return;
            }
            dp = AndroidUtilities.dp(32.0f);
            StaticLayout makeStaticLayout3 = makeStaticLayout(charSequence6, textPaint3, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f, this.emojiOnlyCount <= 0);
            charSequence = charSequence6;
            if (this.isRepostPreview) {
            }
            if (!this.hasSingleQuote) {
            }
            i = AndroidUtilities.dp(f) + maxMessageTextWidth;
            int lineCount32 = makeStaticLayout3.getLineCount();
            int i1622 = this.totalAnimatedEmojiCount;
            if (i1622 >= 50) {
            }
            if (Build.VERSION.SDK_INT < 24) {
            }
            if (z2) {
            }
            arrayList = new ArrayList();
            if (!(charSequence instanceof Spanned)) {
            }
            if (z2) {
            }
            i2 = maxMessageTextWidth;
            z3 = false;
            arrayList.add(new TextRange(0, makeStaticLayout3.getText().length()));
            int size222 = arrayList.size();
            this.hasCodeAtTop = z3;
            this.hasCodeAtBottom = z3;
            this.hasQuoteAtBottom = z3;
            this.hasSingleQuote = z3;
            this.hasSingleCode = z3;
            int i18222 = i2;
            i4 = 0;
            CharSequence charSequence7222 = charSequence;
            while (i4 < arrayList.size()) {
            }
            if (this.hasCode) {
            }
            z4 = false;
            this.hasWideCode = z4;
            this.factCheckText = null;
            return;
        } catch (Exception e4) {
            FileLog.e(e4);
            return;
        }
        maxMessageTextWidth -= dp;
        if (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame)) {
        }
        CharSequence charSequence62 = this.messageText;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:57:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLinkDescription() {
        boolean z;
        int i;
        Spannable.Factory factory;
        String str;
        TLRPC.TL_webPageAttributeStory tL_webPageAttributeStory;
        TL_stories.StoryItem storyItem;
        if (this.linkDescription != null) {
            return;
        }
        TLRPC.WebPage webPage = this.storyMentionWebpage;
        if (webPage == null) {
            webPage = getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage ? ((TLRPC.TL_messageMediaWebPage) getMedia(this.messageOwner)).webpage : null;
        }
        if (webPage != null) {
            for (int i2 = 0; i2 < webPage.attributes.size(); i2++) {
                TLRPC.WebPageAttribute webPageAttribute = webPage.attributes.get(i2);
                if ((webPageAttribute instanceof TLRPC.TL_webPageAttributeStory) && (storyItem = (tL_webPageAttributeStory = (TLRPC.TL_webPageAttributeStory) webPageAttribute).storyItem) != null && storyItem.caption != null) {
                    this.linkDescription = new SpannableStringBuilder(tL_webPageAttributeStory.storyItem.caption);
                    this.webPageDescriptionEntities = tL_webPageAttributeStory.storyItem.entities;
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (this.linkDescription == null) {
            if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC.TL_webPage) && getMedia(this.messageOwner).webpage.description != null) {
                this.linkDescription = Spannable.Factory.getInstance().newSpannable(getMedia(this.messageOwner).webpage.description);
                String str2 = getMedia(this.messageOwner).webpage.site_name;
                if (str2 != null) {
                    str2 = str2.toLowerCase();
                }
                if ("instagram".equals(str2)) {
                    i = 1;
                } else if ("twitter".equals(str2)) {
                    i = 2;
                }
                if (TextUtils.isEmpty(this.linkDescription)) {
                    if (containsUrls(this.linkDescription)) {
                        try {
                            AndroidUtilities.addLinksSafe((Spannable) this.linkDescription, 1, false, true);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                    CharSequence replaceEmoji = Emoji.replaceEmoji(this.linkDescription, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    this.linkDescription = replaceEmoji;
                    ArrayList<TLRPC.MessageEntity> arrayList = this.webPageDescriptionEntities;
                    if (arrayList != null) {
                        addEntitiesToText(replaceEmoji, arrayList, isOut(), z, false, !z);
                        replaceAnimatedEmoji(this.linkDescription, this.webPageDescriptionEntities, Theme.chat_msgTextPaint.getFontMetricsInt());
                    }
                    if (i != 0) {
                        if (!(this.linkDescription instanceof Spannable)) {
                            this.linkDescription = new SpannableStringBuilder(this.linkDescription);
                        }
                        addUrlsByPattern(isOutOwner(), this.linkDescription, false, i, 0, false);
                        return;
                    }
                    return;
                }
                return;
            }
            if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame) && getMedia(this.messageOwner).game.description != null) {
                factory = Spannable.Factory.getInstance();
                str = getMedia(this.messageOwner).game.description;
            } else if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaInvoice) && getMedia(this.messageOwner).description != null) {
                factory = Spannable.Factory.getInstance();
                str = getMedia(this.messageOwner).description;
            }
            this.linkDescription = factory.newSpannable(str);
        }
        i = 0;
        if (TextUtils.isEmpty(this.linkDescription)) {
        }
    }

    public void generatePaymentSentMessageText(TLRPC.User user, boolean z) {
        String str;
        String formatString;
        if (user == null) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
        }
        String firstName = user != null ? UserObject.getFirstName(user) : "";
        try {
            if ("XTR".equals(this.messageOwner.action.currency)) {
                str = "XTR " + this.messageOwner.action.total_amount;
            } else {
                LocaleController localeController = LocaleController.getInstance();
                TLRPC.MessageAction messageAction = this.messageOwner.action;
                str = localeController.formatCurrencyString(messageAction.total_amount, messageAction.currency);
            }
        } catch (Exception e) {
            FileLog.e(e);
            str = "<error>";
        }
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject == null || !(getMedia(messageObject) instanceof TLRPC.TL_messageMediaInvoice)) {
            TLRPC.MessageAction messageAction2 = this.messageOwner.action;
            int i = messageAction2.subscription_until_date;
            formatString = i != 0 ? z ? LocaleController.formatString(R.string.PaymentSuccessfullyPaidMeNoItemSubscription, firstName, str, LocaleController.formatDateTime(i, false)) : LocaleController.formatString(R.string.PaymentSuccessfullyPaidSubscriptionNoItem, str, firstName, LocaleController.formatDateTime(i, false)) : (!messageAction2.recurring_init || z) ? LocaleController.formatString(R.string.PaymentSuccessfullyPaidNoItem, str, firstName) : LocaleController.formatString(R.string.PaymentSuccessfullyPaidNoItemRecurrent, str, firstName);
        } else {
            TLRPC.MessageAction messageAction3 = this.messageOwner.action;
            formatString = messageAction3.subscription_until_date != 0 ? z ? LocaleController.formatString(R.string.PaymentSuccessfullyPaidMeSubscription, firstName, str, getMedia(this.replyMessageObject).title, LocaleController.formatDateTime(this.messageOwner.action.subscription_until_date, false)) : LocaleController.formatString(R.string.PaymentSuccessfullyPaidSubscription, str, firstName, getMedia(this.replyMessageObject).title, LocaleController.formatDateTime(this.messageOwner.action.subscription_until_date, false)) : (!messageAction3.recurring_init || z) ? LocaleController.formatString(R.string.PaymentSuccessfullyPaid, str, firstName, getMedia(this.replyMessageObject).title) : LocaleController.formatString(R.string.PaymentSuccessfullyPaidRecurrent, str, firstName, getMedia(this.replyMessageObject).title);
        }
        this.messageText = formatString;
        this.messageText = StarsIntroActivity.replaceStars(this.messageText);
    }

    /* JADX WARN: Code restructure failed: missing block: B:111:0x025b, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0264, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x00db, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x026d, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0071, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0270, code lost:
    
        r8 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0271, code lost:
    
        r8 = replaceWithLink(r0, "un1", r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0083, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0095, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00a7, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b9, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00ef, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0103, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0117, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x012b, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x014d, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0157, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x016b, code lost:
    
        if (r8 != null) goto L137;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v0, types: [org.telegram.tgnet.TLRPC$Chat] */
    /* JADX WARN: Type inference failed for: r9v1, types: [org.telegram.tgnet.TLRPC$Chat] */
    /* JADX WARN: Type inference failed for: r9v8, types: [org.telegram.tgnet.TLRPC$Chat] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generatePinMessageText(TLRPC.User user, TLRPC.Chat chat) {
        CharSequence string;
        CharSequence charSequence;
        boolean z;
        MessagesController messagesController;
        long j;
        if (user == null && chat == 0) {
            if (isFromUser()) {
                user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
            }
            if (user == null) {
                TLRPC.Peer peer = this.messageOwner.peer_id;
                if (peer instanceof TLRPC.TL_peerChannel) {
                    messagesController = MessagesController.getInstance(this.currentAccount);
                    j = this.messageOwner.peer_id.channel_id;
                } else if (peer instanceof TLRPC.TL_peerChat) {
                    messagesController = MessagesController.getInstance(this.currentAccount);
                    j = this.messageOwner.peer_id.chat_id;
                }
                chat = messagesController.getChat(Long.valueOf(j));
            }
        }
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            if (!(message instanceof TLRPC.TL_messageEmpty) && !(message.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                if (messageObject.isMusic()) {
                    string = LocaleController.getString(R.string.ActionPinnedMusic);
                } else if (this.replyMessageObject.isVideo()) {
                    string = LocaleController.getString(R.string.ActionPinnedVideo);
                } else if (this.replyMessageObject.isGif()) {
                    string = LocaleController.getString(R.string.ActionPinnedGif);
                } else if (this.replyMessageObject.isVoice()) {
                    string = LocaleController.getString(R.string.ActionPinnedVoice);
                } else if (this.replyMessageObject.isRoundVideo()) {
                    string = LocaleController.getString(R.string.ActionPinnedRound);
                } else if ((this.replyMessageObject.isSticker() || this.replyMessageObject.isAnimatedSticker()) && !this.replyMessageObject.isAnimatedEmoji()) {
                    string = LocaleController.getString(R.string.ActionPinnedSticker);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC.TL_messageMediaDocument) {
                    string = LocaleController.getString(R.string.ActionPinnedFile);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC.TL_messageMediaGeo) {
                    string = LocaleController.getString(R.string.ActionPinnedGeo);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC.TL_messageMediaGeoLive) {
                    string = LocaleController.getString(R.string.ActionPinnedGeoLive);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC.TL_messageMediaContact) {
                    string = LocaleController.getString(R.string.ActionPinnedContact);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC.TL_messageMediaPoll) {
                    if (((TLRPC.TL_messageMediaPoll) getMedia(this.replyMessageObject)).poll.quiz) {
                        string = LocaleController.getString(R.string.ActionPinnedQuiz);
                    } else {
                        string = LocaleController.getString(R.string.ActionPinnedPoll);
                    }
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC.TL_messageMediaPhoto) {
                    string = LocaleController.getString(R.string.ActionPinnedPhoto);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC.TL_messageMediaPaidMedia) {
                    charSequence = LocaleController.formatPluralString("NotificationPinnedPaidMedia", (int) ((TLRPC.TL_messageMediaPaidMedia) getMedia(this.replyMessageObject)).stars_amount, chat != 0 ? chat.title : UserObject.getUserName(user));
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC.TL_messageMediaGame) {
                    String formatString = LocaleController.formatString("ActionPinnedGame", R.string.ActionPinnedGame, "🎮 " + getMedia(this.replyMessageObject).game.title);
                    if (user == null) {
                        user = chat;
                    }
                    CharSequence replaceWithLink = replaceWithLink(formatString, "un1", user);
                    this.messageText = replaceWithLink;
                    charSequence = Emoji.replaceEmoji(replaceWithLink, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                } else {
                    CharSequence charSequence2 = this.replyMessageObject.messageText;
                    if (charSequence2 == null || charSequence2.length() <= 0) {
                        string = LocaleController.getString(R.string.ActionPinnedNoText);
                    } else {
                        CharSequence cloneSpans = AnimatedEmojiSpan.cloneSpans(this.replyMessageObject.messageText);
                        if (cloneSpans.length() > 20) {
                            cloneSpans = cloneSpans.subSequence(0, 20);
                            z = true;
                        } else {
                            z = false;
                        }
                        CharSequence replaceEmoji = Emoji.replaceEmoji(cloneSpans, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), true);
                        MessageObject messageObject2 = this.replyMessageObject;
                        if (messageObject2 != null && messageObject2.messageOwner != null) {
                            replaceEmoji = messageObject2.replaceAnimatedEmoji(replaceEmoji, Theme.chat_msgTextPaint.getFontMetricsInt());
                        }
                        MediaDataController.addTextStyleRuns(this.replyMessageObject, (Spannable) replaceEmoji);
                        if (z) {
                            if (replaceEmoji instanceof SpannableStringBuilder) {
                                ((SpannableStringBuilder) replaceEmoji).append((CharSequence) "...");
                            } else if (replaceEmoji != null) {
                                replaceEmoji = new SpannableStringBuilder(replaceEmoji).append((CharSequence) "...");
                            }
                        }
                        string = AndroidUtilities.formatSpannable(LocaleController.getString(R.string.ActionPinnedText), replaceEmoji);
                    }
                }
                this.messageText = charSequence;
                return;
            }
        }
        string = LocaleController.getString(R.string.ActionPinnedNoText);
    }

    /* JADX WARN: Code restructure failed: missing block: B:123:0x0198, code lost:
    
        if (r8.isEmpty() == false) goto L185;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x02d4, code lost:
    
        r8 = r7.photoThumbs;
        r1 = r0.thumbs;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x02d2, code lost:
    
        if (r8.isEmpty() == false) goto L185;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateThumbs(boolean z) {
        TLObject tLObject;
        ArrayList<TLRPC.PhotoSize> arrayList;
        ArrayList<TLRPC.PhotoSize> arrayList2;
        ArrayList<TLRPC.PhotoSize> arrayList3;
        ArrayList<TLRPC.PhotoSize> arrayList4;
        ArrayList<TLRPC.PhotoSize> arrayList5;
        ArrayList<TLRPC.PhotoSize> arrayList6;
        TLRPC.Photo photo;
        TLRPC.Photo photo2;
        TLRPC.Document document;
        TLRPC.Document document2;
        TLRPC.Document document3;
        ArrayList<TLRPC.PhotoSize> arrayList7;
        ArrayList<TLRPC.PhotoSize> arrayList8;
        ArrayList<TLRPC.PhotoSize> arrayList9;
        ArrayList<TLRPC.PhotoSize> arrayList10;
        ArrayList<TLRPC.PhotoSize> arrayList11;
        if (!hasExtendedMediaPreview()) {
            TLRPC.Message message = this.messageOwner;
            if (message instanceof TLRPC.TL_messageService) {
                TLRPC.MessageAction messageAction = message.action;
                if (!(messageAction instanceof TLRPC.TL_messageActionChatEditPhoto)) {
                    return;
                }
                TLRPC.Photo photo3 = messageAction.photo;
                if (z) {
                    ArrayList<TLRPC.PhotoSize> arrayList12 = this.photoThumbs;
                    if (arrayList12 != null && !arrayList12.isEmpty()) {
                        for (int i = 0; i < this.photoThumbs.size(); i++) {
                            TLRPC.PhotoSize photoSize = this.photoThumbs.get(i);
                            int i2 = 0;
                            while (true) {
                                if (i2 < photo3.sizes.size()) {
                                    TLRPC.PhotoSize photoSize2 = photo3.sizes.get(i2);
                                    if (!(photoSize2 instanceof TLRPC.TL_photoSizeEmpty) && photoSize2.type.equals(photoSize.type)) {
                                        photoSize.location = photoSize2.location;
                                        break;
                                    }
                                    i2++;
                                }
                            }
                        }
                    }
                } else {
                    this.photoThumbs = new ArrayList<>(photo3.sizes);
                }
                if (photo3.dc_id != 0 && (arrayList11 = this.photoThumbs) != null) {
                    int size = arrayList11.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        TLRPC.FileLocation fileLocation = this.photoThumbs.get(i3).location;
                        if (fileLocation != null) {
                            fileLocation.dc_id = photo3.dc_id;
                            fileLocation.file_reference = photo3.file_reference;
                        }
                    }
                }
                tLObject = this.messageOwner.action.photo;
            } else {
                if (this.emojiAnimatedSticker == null && this.emojiAnimatedStickerId == null) {
                    if (getMedia(message) == null || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaEmpty)) {
                        TLRPC.MessageMedia messageMedia = this.sponsoredMedia;
                        if (messageMedia != null) {
                            TLRPC.Photo photo4 = messageMedia.photo;
                            TLRPC.Document document4 = messageMedia.document;
                            if (photo4 != null) {
                                if (!z || (arrayList4 = this.photoThumbs) == null) {
                                    this.photoThumbs = new ArrayList<>(photo4.sizes);
                                    photo2 = photo4;
                                } else {
                                    photo2 = photo4;
                                    if (!arrayList4.isEmpty()) {
                                        arrayList5 = this.photoThumbs;
                                        arrayList6 = photo4.sizes;
                                        photo = photo4;
                                        updatePhotoSizeLocations(arrayList5, arrayList6);
                                        photo2 = photo;
                                    }
                                }
                                this.photoThumbsObject = photo2;
                                return;
                            }
                            if (document4 == null || !isDocumentHasThumb(document4)) {
                                return;
                            }
                            if (z) {
                                ArrayList<TLRPC.PhotoSize> arrayList13 = this.photoThumbs;
                                document3 = document4;
                                if (arrayList13 != null) {
                                    document3 = document4;
                                    document2 = document4;
                                }
                            } else {
                                arrayList3 = new ArrayList<>();
                                document = document4;
                                this.photoThumbs = arrayList3;
                                arrayList3.addAll(document.thumbs);
                                document3 = document;
                            }
                        } else {
                            if (this.sponsoredPhoto == null) {
                                return;
                            }
                            if (!z || (arrayList2 = this.photoThumbs) == null) {
                                this.photoThumbs = new ArrayList<>(this.sponsoredPhoto.sizes);
                            } else if (!arrayList2.isEmpty()) {
                                updatePhotoSizeLocations(this.photoThumbs, this.sponsoredPhoto.sizes);
                            }
                            this.photoThumbsObject = this.sponsoredPhoto;
                            if (this.strippedThumb != null) {
                                return;
                            }
                        }
                    } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto) {
                        TLRPC.Photo photo5 = getMedia(this.messageOwner).photo;
                        if (z && ((arrayList10 = this.photoThumbs) == null || arrayList10.size() == photo5.sizes.size())) {
                            ArrayList<TLRPC.PhotoSize> arrayList14 = this.photoThumbs;
                            if (arrayList14 != null && !arrayList14.isEmpty()) {
                                for (int i4 = 0; i4 < this.photoThumbs.size(); i4++) {
                                    TLRPC.PhotoSize photoSize3 = this.photoThumbs.get(i4);
                                    if (photoSize3 != null) {
                                        int i5 = 0;
                                        while (true) {
                                            if (i5 >= photo5.sizes.size()) {
                                                break;
                                            }
                                            TLRPC.PhotoSize photoSize4 = photo5.sizes.get(i5);
                                            if (photoSize4 != null && !(photoSize4 instanceof TLRPC.TL_photoSizeEmpty)) {
                                                if (photoSize4.type.equals(photoSize3.type)) {
                                                    photoSize3.location = photoSize4.location;
                                                    break;
                                                } else if ("s".equals(photoSize3.type) && (photoSize4 instanceof TLRPC.TL_photoStrippedSize)) {
                                                    this.photoThumbs.set(i4, photoSize4);
                                                    break;
                                                }
                                            }
                                            i5++;
                                        }
                                    }
                                }
                            }
                        } else {
                            this.photoThumbs = new ArrayList<>(photo5.sizes);
                        }
                        tLObject = getMedia(this.messageOwner).photo;
                    } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument) {
                        TLRPC.Document document5 = getDocument();
                        if (!isDocumentHasThumb(document5)) {
                            return;
                        }
                        if (!z || (r8 = this.photoThumbs) == null) {
                            arrayList3 = new ArrayList<>();
                            document = document5;
                            this.photoThumbs = arrayList3;
                            arrayList3.addAll(document.thumbs);
                            document3 = document;
                        } else {
                            document3 = document5;
                            document2 = document5;
                        }
                    } else {
                        if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame) {
                            TLRPC.Document document6 = getMedia(this.messageOwner).game.document;
                            if (document6 != null && isDocumentHasThumb(document6)) {
                                if (z) {
                                    ArrayList<TLRPC.PhotoSize> arrayList15 = this.photoThumbs;
                                    if (arrayList15 != null && !arrayList15.isEmpty()) {
                                        updatePhotoSizeLocations(this.photoThumbs, document6.thumbs);
                                    }
                                } else {
                                    ArrayList<TLRPC.PhotoSize> arrayList16 = new ArrayList<>();
                                    this.photoThumbs = arrayList16;
                                    arrayList16.addAll(document6.thumbs);
                                }
                                this.photoThumbsObject = document6;
                            }
                            TLRPC.Photo photo6 = getMedia(this.messageOwner).game.photo;
                            if (photo6 != null) {
                                if (!z || (arrayList9 = this.photoThumbs2) == null) {
                                    this.photoThumbs2 = new ArrayList<>(photo6.sizes);
                                } else if (!arrayList9.isEmpty()) {
                                    updatePhotoSizeLocations(this.photoThumbs2, photo6.sizes);
                                }
                                this.photoThumbsObject2 = photo6;
                            }
                            if (this.photoThumbs != null || (arrayList8 = this.photoThumbs2) == null) {
                                return;
                            }
                            this.photoThumbs = arrayList8;
                            this.photoThumbs2 = null;
                            this.photoThumbsObject = this.photoThumbsObject2;
                            this.photoThumbsObject2 = null;
                            return;
                        }
                        if (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage)) {
                            return;
                        }
                        TLRPC.Photo photo7 = getMedia(this.messageOwner).webpage.photo;
                        TLRPC.Document document7 = getMedia(this.messageOwner).webpage.document;
                        if (photo7 == 0) {
                            if (document7 == 0 || !isDocumentHasThumb(document7)) {
                                return;
                            }
                            if (z) {
                                ArrayList<TLRPC.PhotoSize> arrayList17 = this.photoThumbs;
                                photo2 = document7;
                                if (arrayList17 != null) {
                                    photo2 = document7;
                                    if (!arrayList17.isEmpty()) {
                                        arrayList5 = this.photoThumbs;
                                        arrayList6 = document7.thumbs;
                                        photo = document7;
                                        updatePhotoSizeLocations(arrayList5, arrayList6);
                                        photo2 = photo;
                                    }
                                }
                            } else {
                                ArrayList<TLRPC.PhotoSize> arrayList18 = new ArrayList<>();
                                this.photoThumbs = arrayList18;
                                arrayList18.addAll(document7.thumbs);
                                photo2 = document7;
                            }
                            this.photoThumbsObject = photo2;
                            return;
                        }
                        if (!z || (arrayList7 = this.photoThumbs) == null) {
                            this.photoThumbs = new ArrayList<>(photo7.sizes);
                            document3 = photo7;
                        } else {
                            document3 = photo7;
                            if (!arrayList7.isEmpty()) {
                                ArrayList<TLRPC.PhotoSize> arrayList19 = this.photoThumbs;
                                ArrayList<TLRPC.PhotoSize> arrayList20 = photo7.sizes;
                                TLRPC.Document document8 = photo7;
                                updatePhotoSizeLocations(arrayList19, arrayList20);
                                document3 = document8;
                            }
                        }
                    }
                    this.photoThumbsObject = document3;
                    return;
                }
                if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) || !isDocumentHasThumb(this.emojiAnimatedSticker)) {
                    return;
                }
                if (!z || (arrayList = this.photoThumbs) == null) {
                    ArrayList<TLRPC.PhotoSize> arrayList21 = new ArrayList<>();
                    this.photoThumbs = arrayList21;
                    arrayList21.addAll(this.emojiAnimatedSticker.thumbs);
                } else if (!arrayList.isEmpty()) {
                    updatePhotoSizeLocations(this.photoThumbs, this.emojiAnimatedSticker.thumbs);
                }
                tLObject = this.emojiAnimatedSticker;
            }
            this.photoThumbsObject = tLObject;
            return;
        }
        TLRPC.TL_messageExtendedMediaPreview tL_messageExtendedMediaPreview = (TLRPC.TL_messageExtendedMediaPreview) this.messageOwner.media.extended_media.get(0);
        if (z) {
            updatePhotoSizeLocations(this.photoThumbs, Collections.singletonList(tL_messageExtendedMediaPreview.thumb));
        } else {
            this.photoThumbs = new ArrayList<>(Collections.singletonList(tL_messageExtendedMediaPreview.thumb));
        }
        this.photoThumbsObject = this.messageOwner;
        if (this.strippedThumb != null) {
            return;
        }
        createStrippedThumb();
    }

    public int getApproximateHeight() {
        int i;
        int min;
        int min2;
        int i2 = this.type;
        int i3 = 0;
        if (i2 == 0) {
            int textHeight = textHeight();
            if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC.TL_webPage)) {
                i3 = AndroidUtilities.dp(100.0f);
            }
            int i4 = textHeight + i3;
            return isReply() ? i4 + AndroidUtilities.dp(42.0f) : i4;
        }
        if (i2 == 20) {
            return AndroidUtilities.getPhotoSize();
        }
        if (i2 == 2) {
            return AndroidUtilities.dp(72.0f);
        }
        if (i2 == 12) {
            return AndroidUtilities.dp(71.0f);
        }
        if (i2 == 9) {
            return AndroidUtilities.dp(100.0f);
        }
        if (i2 == 4) {
            return AndroidUtilities.dp(114.0f);
        }
        if (i2 == 14) {
            return AndroidUtilities.dp(82.0f);
        }
        if (i2 == 10) {
            return AndroidUtilities.dp(30.0f);
        }
        if (i2 == 11 || i2 == 18 || i2 == 30 || i2 == 25 || i2 == 21) {
            return AndroidUtilities.dp(50.0f);
        }
        if (i2 == 5) {
            return AndroidUtilities.roundMessageSize;
        }
        if (i2 == 19) {
            return textHeight() + AndroidUtilities.dp(30.0f);
        }
        if (i2 != 13 && i2 != 15) {
            if (AndroidUtilities.isTablet()) {
                min = AndroidUtilities.getMinTabletSide();
            } else {
                Point point = AndroidUtilities.displaySize;
                min = Math.min(point.x, point.y);
            }
            int i5 = (int) (min * 0.7f);
            int dp = AndroidUtilities.dp(100.0f) + i5;
            if (i5 > AndroidUtilities.getPhotoSize()) {
                i5 = AndroidUtilities.getPhotoSize();
            }
            if (dp > AndroidUtilities.getPhotoSize()) {
                dp = AndroidUtilities.getPhotoSize();
            }
            if (FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
                int i6 = (int) (r3.h / (r3.w / i5));
                if (i6 == 0) {
                    i6 = AndroidUtilities.dp(100.0f);
                }
                if (i6 <= dp) {
                    dp = i6 < AndroidUtilities.dp(120.0f) ? AndroidUtilities.dp(120.0f) : i6;
                }
                if (needDrawBluredPreview()) {
                    if (AndroidUtilities.isTablet()) {
                        min2 = AndroidUtilities.getMinTabletSide();
                    } else {
                        Point point2 = AndroidUtilities.displaySize;
                        min2 = Math.min(point2.x, point2.y);
                    }
                    dp = (int) (min2 * 0.5f);
                }
            }
            return dp + AndroidUtilities.dp(14.0f);
        }
        float f = AndroidUtilities.displaySize.y * 0.4f;
        float minTabletSide = (AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : AndroidUtilities.displaySize.x) * 0.5f;
        TLRPC.Document document = getDocument();
        if (document != null) {
            int size = document.attributes.size();
            for (int i7 = 0; i7 < size; i7++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i7);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize) {
                    i3 = documentAttribute.w;
                    i = documentAttribute.h;
                    break;
                }
            }
        }
        i = 0;
        if (i3 == 0) {
            i = (int) f;
            i3 = AndroidUtilities.dp(100.0f) + i;
        }
        float f2 = i;
        if (f2 > f) {
            i3 = (int) (i3 * (f / f2));
            i = (int) f;
        }
        float f3 = i3;
        if (f3 > minTabletSide) {
            i = (int) (i * (minTabletSide / f3));
        }
        return i + AndroidUtilities.dp(14.0f);
    }

    public String getArtworkUrl(boolean z) {
        TLRPC.Document document = getDocument();
        if (document == null || "audio/ogg".equals(document.mime_type)) {
            return null;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                if (documentAttribute.voice) {
                    return null;
                }
                String str = documentAttribute.performer;
                String str2 = documentAttribute.title;
                if (!TextUtils.isEmpty(str)) {
                    int i2 = 0;
                    while (true) {
                        String[] strArr = excludeWords;
                        if (i2 >= strArr.length) {
                            break;
                        }
                        str = str.replace(strArr[i2], " ");
                        i2++;
                    }
                }
                if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) {
                    return null;
                }
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("athumb://itunes.apple.com/search?term=");
                    sb.append(URLEncoder.encode(str + " - " + str2, "UTF-8"));
                    sb.append("&entity=song&limit=4");
                    sb.append(z ? "&s=1" : "");
                    return sb.toString();
                } catch (Exception unused) {
                    continue;
                }
            }
        }
        return null;
    }

    public long getChannelId() {
        return getChannelId(this.messageOwner);
    }

    public long getChatId() {
        TLRPC.Peer peer = this.messageOwner.peer_id;
        if (peer instanceof TLRPC.TL_peerChat) {
            return peer.chat_id;
        }
        if (peer instanceof TLRPC.TL_peerChannel) {
            return peer.channel_id;
        }
        return 0L;
    }

    public int getChatMode() {
        if (this.scheduled) {
            return 1;
        }
        return isQuickReply() ? 5 : 0;
    }

    public ArrayList<ReactionsLayoutInBubble.VisibleReaction> getChoosenReactions() {
        ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList = new ArrayList<>();
        if (this.messageOwner.reactions == null) {
            return arrayList;
        }
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (this.messageOwner.reactions.results.get(i).chosen) {
                arrayList.add(ReactionsLayoutInBubble.VisibleReaction.fromTL(this.messageOwner.reactions.results.get(i).reaction));
            }
        }
        return arrayList;
    }

    public long getDialogId() {
        return getDialogId(this.messageOwner);
    }

    public String getDiceEmoji() {
        if (!isDice()) {
            return null;
        }
        TLRPC.TL_messageMediaDice tL_messageMediaDice = (TLRPC.TL_messageMediaDice) getMedia(this.messageOwner);
        return TextUtils.isEmpty(tL_messageMediaDice.emoticon) ? "🎲" : tL_messageMediaDice.emoticon.replace("️", "");
    }

    public int getDiceValue() {
        if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDice) {
            return ((TLRPC.TL_messageMediaDice) getMedia(this.messageOwner)).value;
        }
        return -1;
    }

    public TLRPC.Document getDocument() {
        VideoPlayer.VideoUri videoUri;
        TLRPC.Document document = this.emojiAnimatedSticker;
        return document != null ? document : (!hasVideoQualities() || (videoUri = this.highestQuality) == null) ? getDocument(this.messageOwner) : videoUri.document;
    }

    public String getDocumentName() {
        return FileLoader.getDocumentFileName(getDocument());
    }

    public double getDuration() {
        TL_stories.StoryItem storyItem;
        TLRPC.MessageMedia messageMedia;
        double d = this.attributeDuration;
        if (d > 0.0d) {
            return d;
        }
        TLRPC.Document document = getDocument();
        if (document == null && this.type == 23 && (storyItem = getMedia(this.messageOwner).storyItem) != null && (messageMedia = storyItem.media) != null) {
            document = messageMedia.document;
        }
        if (document == null) {
            return 0.0d;
        }
        int i = this.audioPlayerDuration;
        if (i > 0) {
            return i;
        }
        for (int i2 = 0; i2 < document.attributes.size(); i2++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i2);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                double d2 = documentAttribute.duration;
                this.attributeDuration = d2;
                return d2;
            }
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                double d3 = documentAttribute.duration;
                this.attributeDuration = d3;
                return d3;
            }
        }
        return this.audioPlayerDuration;
    }

    public TLRPC.TL_availableEffect getEffect() {
        TLRPC.Message message = this.messageOwner;
        if (message == null || (message.flags2 & 4) == 0) {
            return null;
        }
        return MessagesController.getInstance(this.currentAccount).getEffect(this.messageOwner.effect);
    }

    public long getEffectId() {
        TLRPC.Message message = this.messageOwner;
        if (message == null || (message.flags2 & 4) == 0) {
            return 0L;
        }
        return message.effect;
    }

    public int getEmojiOnlyCount() {
        return this.emojiOnlyCount;
    }

    public String getExtension() {
        String fileName = getFileName();
        int lastIndexOf = fileName.lastIndexOf(46);
        String substring = lastIndexOf != -1 ? fileName.substring(lastIndexOf + 1) : null;
        if (substring == null || substring.length() == 0) {
            substring = getDocument().mime_type;
        }
        if (substring == null) {
            substring = "";
        }
        return substring.toUpperCase();
    }

    public TLRPC.TL_factCheck getFactCheck() {
        return FactCheckController.getInstance(this.currentAccount).getFactCheck(this);
    }

    public CharSequence getFactCheckText() {
        if (!isFactCheckable()) {
            return null;
        }
        TLRPC.TL_factCheck factCheck = getFactCheck();
        if (factCheck == null || factCheck.text == null) {
            this.factCheckText = null;
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(factCheck.text.text);
        addEntitiesToText(spannableStringBuilder, factCheck.text.entities, isOutOwner(), false, false, false);
        this.factCheckText = spannableStringBuilder;
        return spannableStringBuilder;
    }

    public String getFileName() {
        return getDocument() != null ? getFileName(getDocument()) : getFileName(this.messageOwner);
    }

    public Long getForwardedFromId() {
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Peer peer;
        TLRPC.Message message = this.messageOwner;
        if (message == null || (messageFwdHeader = message.fwd_from) == null || (peer = messageFwdHeader.from_id) == null) {
            return null;
        }
        return Long.valueOf(DialogObject.getPeerDialogId(peer));
    }

    public String getForwardedName() {
        TLRPC.MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
        if (messageFwdHeader == null) {
            return null;
        }
        TLRPC.Peer peer = messageFwdHeader.from_id;
        if (peer instanceof TLRPC.TL_peerChannel) {
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.channel_id));
            if (chat != null) {
                return chat.title;
            }
            return null;
        }
        if (peer instanceof TLRPC.TL_peerChat) {
            TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.chat_id));
            if (chat2 != null) {
                return chat2.title;
            }
            return null;
        }
        if (peer instanceof TLRPC.TL_peerUser) {
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.fwd_from.from_id.user_id));
            if (user != null) {
                return UserObject.getUserName(user);
            }
            return null;
        }
        String str = messageFwdHeader.from_name;
        if (str != null) {
            return str;
        }
        return null;
    }

    public long getFromChatId() {
        return getFromChatId(this.messageOwner);
    }

    public TLObject getFromPeerObject() {
        TLRPC.Message message = this.messageOwner;
        if (message == null) {
            return null;
        }
        TLRPC.Peer peer = message.from_id;
        if ((peer instanceof TLRPC.TL_peerChannel_layer131) || (peer instanceof TLRPC.TL_peerChannel)) {
            return MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.channel_id));
        }
        if ((peer instanceof TLRPC.TL_peerUser_layer131) || (peer instanceof TLRPC.TL_peerUser)) {
            return MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
        }
        if ((peer instanceof TLRPC.TL_peerChat_layer131) || (peer instanceof TLRPC.TL_peerChat)) {
            return MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.chat_id));
        }
        return null;
    }

    public long getGroupId() {
        long j = this.localGroupId;
        return j != 0 ? j : getGroupIdForUse();
    }

    public long getGroupIdForUse() {
        long j = this.localSentGroupId;
        return j != 0 ? j : this.messageOwner.grouped_id;
    }

    public int getId() {
        return this.messageOwner.id;
    }

    public TLRPC.InputStickerSet getInputStickerSet() {
        return getInputStickerSet(this.messageOwner);
    }

    public int getMaxMessageTextWidth() {
        int dp;
        Uri parse;
        String lastPathSegment;
        this.generatedWithMinSize = (!AndroidUtilities.isTablet() || this.eventId == 0) ? AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : getParentWidth() : AndroidUtilities.dp(530.0f);
        this.generatedWithDensity = AndroidUtilities.density;
        if (this.hasCode && !this.isSaved) {
            dp = this.generatedWithMinSize - AndroidUtilities.dp(60.0f);
            if (needDrawAvatarInternal() && !isOutOwner() && !this.messageOwner.isThreadMessage) {
                dp -= AndroidUtilities.dp(52.0f);
            }
        } else if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_background".equals(getMedia(this.messageOwner).webpage.type)) {
            try {
                parse = Uri.parse(getMedia(this.messageOwner).webpage.url);
                lastPathSegment = parse.getLastPathSegment();
            } catch (Exception unused) {
            }
            if (parse.getQueryParameter("bg_color") != null) {
                dp = AndroidUtilities.dp(220.0f);
            } else {
                if (lastPathSegment.length() == 6 || (lastPathSegment.length() == 13 && lastPathSegment.charAt(6) == '-')) {
                    dp = AndroidUtilities.dp(200.0f);
                }
                dp = 0;
            }
        } else {
            if (isAndroidTheme()) {
                dp = AndroidUtilities.dp(200.0f);
            }
            dp = 0;
        }
        if (dp == 0) {
            dp = this.generatedWithMinSize - AndroidUtilities.dp(80.0f);
            if (needDrawAvatarInternal() && !isOutOwner() && !this.messageOwner.isThreadMessage) {
                dp -= AndroidUtilities.dp(52.0f);
            }
            if (needDrawShareButton() && (this.isSaved || !isOutOwner())) {
                dp -= AndroidUtilities.dp((this.isSaved && isOutOwner()) ? 40.0f : 14.0f);
            }
            if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame) {
                dp -= AndroidUtilities.dp(10.0f);
            }
        }
        int i = this.emojiOnlyCount;
        if (i < 1) {
            return dp;
        }
        int i2 = this.totalAnimatedEmojiCount;
        if (i2 <= 100) {
            return i - i2 < (SharedConfig.getDevicePerformanceClass() < 2 ? 50 : 100) ? (hasValidReplyMessageObject() || isForwarded()) ? Math.min(dp, (int) (this.generatedWithMinSize * 0.65f)) : dp : dp;
        }
        return dp;
    }

    public int getMediaExistanceFlags() {
        boolean z = this.attachPathExists;
        return this.mediaExists ? (z ? 1 : 0) | 2 : z ? 1 : 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00e2, code lost:
    
        if (isVideoDocument(r4.document) != false) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f1, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00ef, code lost:
    
        if ((((org.telegram.tgnet.TLRPC.TL_messageExtendedMediaPreview) r5).flags & 4) != 0) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0164, code lost:
    
        if (r2 != null) goto L109;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0168, code lost:
    
        if (r8.ttl_seconds == 0) goto L109;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CharSequence getMediaTitle(TLRPC.MessageMedia messageMedia) {
        String publicUsername;
        if (messageMedia instanceof TLRPC.TL_messageMediaGiveaway) {
            return LocaleController.getString(R.string.BoostingGiveaway);
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaGiveawayResults) {
            return LocaleController.getString(R.string.BoostingGiveawayResults);
        }
        String str = null;
        if (messageMedia instanceof TLRPC.TL_messageMediaStory) {
            if (!messageMedia.via_mention) {
                return LocaleController.getString(R.string.ForwardedStory);
            }
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageMedia.user_id));
            if (user != null && (publicUsername = UserObject.getPublicUsername(user)) != null) {
                str = MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername + "/s/" + messageMedia.id;
            }
            if (str == null) {
                return "";
            }
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new URLSpanReplacement("https://" + str, new TextStyleSpan.TextStyleRun()), 0, spannableString.length(), 33);
            return spannableString;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaDice) {
            return getDiceEmoji();
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
            return ((TLRPC.TL_messageMediaPoll) messageMedia).poll.quiz ? LocaleController.getString(R.string.QuizPoll) : LocaleController.getString(R.string.Poll);
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) {
            TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia;
            int size = tL_messageMediaPaidMedia.extended_media.size();
            boolean z = false;
            for (int i = 0; i < size; i++) {
                TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i);
                if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                    TLRPC.MessageMedia messageMedia2 = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media;
                    if (messageMedia2 instanceof TLRPC.TL_messageMediaDocument) {
                    }
                    z = false;
                } else if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMediaPreview) {
                }
                if (z) {
                    break;
                }
            }
            return StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.AttachPaidMedia, size == 1 ? LocaleController.getString(z ? R.string.AttachVideo : R.string.AttachPhoto) : LocaleController.formatPluralString(z ? "Media" : "Photos", size, new Object[0])));
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
            return (messageMedia.ttl_seconds == 0 || (this.messageOwner instanceof TLRPC.TL_message_secret)) ? getGroupId() != 0 ? LocaleController.getString(R.string.Album) : LocaleController.getString(R.string.AttachPhoto) : LocaleController.getString(R.string.AttachDestructingPhoto);
        }
        if (messageMedia != null) {
            if (!isVideoDocument(messageMedia.document)) {
                if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                    TLRPC.Document document = messageMedia.document;
                    if (!(document instanceof TLRPC.TL_documentEmpty)) {
                    }
                }
            }
            return (messageMedia.ttl_seconds == 0 || (this.messageOwner instanceof TLRPC.TL_message_secret)) ? LocaleController.getString(R.string.AttachVideo) : messageMedia.voice ? LocaleController.getString(R.string.AttachVoiceExpired) : messageMedia.round ? LocaleController.getString(R.string.AttachRoundExpired) : LocaleController.getString(R.string.AttachDestructingVideo);
        }
        if (messageMedia != null && isVoiceDocument(messageMedia.document)) {
            return LocaleController.getString(R.string.AttachAudio);
        }
        if (messageMedia != null && isRoundVideoDocument(messageMedia.document)) {
            return LocaleController.getString(R.string.AttachRound);
        }
        if ((messageMedia instanceof TLRPC.TL_messageMediaGeo) || (messageMedia instanceof TLRPC.TL_messageMediaVenue)) {
            return LocaleController.getString(R.string.AttachLocation);
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaGeoLive) {
            return LocaleController.getString(R.string.AttachLiveLocation);
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaContact) {
            return LocaleController.getString(R.string.AttachContact);
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
            return this.messageOwner.message;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaInvoice) {
            return messageMedia.description;
        }
        if (messageMedia instanceof TLRPC.TL_messageMediaUnsupported) {
            return LocaleController.getString(R.string.UnsupportedMedia2);
        }
        if (!(messageMedia instanceof TLRPC.TL_messageMediaDocument)) {
            return null;
        }
        if (isStickerDocument(messageMedia.document) || isAnimatedStickerDocument(messageMedia.document, true)) {
            String stickerChar = getStickerChar();
            return (stickerChar == null || stickerChar.length() <= 0) ? LocaleController.getString(R.string.AttachSticker) : String.format("%s %s", stickerChar, LocaleController.getString(R.string.AttachSticker));
        }
        if (isMusic()) {
            return LocaleController.getString(R.string.AttachMusic);
        }
        if (isGif()) {
            return LocaleController.getString(R.string.AttachGif);
        }
        String documentFileName = FileLoader.getDocumentFileName(messageMedia.document);
        return !TextUtils.isEmpty(documentFileName) ? documentFileName : LocaleController.getString(R.string.AttachDocument);
    }

    public int getMediaType() {
        if (isVideo()) {
            return 2;
        }
        if (isVoice()) {
            return 1;
        }
        if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument) {
            return 3;
        }
        return getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto ? 0 : 4;
    }

    public String getMimeType() {
        TLRPC.Document document = getDocument();
        if (document != null) {
            return document.mime_type;
        }
        if (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaInvoice)) {
            return getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto ? "image/jpeg" : (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) || getMedia(this.messageOwner).webpage.photo == null) ? "" : "image/jpeg";
        }
        TLRPC.WebDocument webDocument = ((TLRPC.TL_messageMediaInvoice) getMedia(this.messageOwner)).webPhoto;
        return webDocument != null ? webDocument.mime_type : "";
    }

    public String getMusicAuthor() {
        return getMusicAuthor(true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:85:0x0038, code lost:
    
        if (r3.round_message != false) goto L21;
     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x010e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x011f A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String getMusicAuthor(boolean z) {
        int i;
        MessagesController messagesController;
        TLRPC.Peer peer;
        MessagesController messagesController2;
        TLRPC.Peer peer2;
        TLRPC.Peer peer3;
        String str;
        TLRPC.Chat chat;
        long j;
        TLRPC.Document document = getDocument();
        if (document != null) {
            boolean z2 = false;
            for (int i2 = 0; i2 < document.attributes.size(); i2++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i2);
                if (!(documentAttribute instanceof TLRPC.TL_documentAttributeAudio)) {
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                    }
                    if (!z2) {
                        TLRPC.User user = null;
                        if (!z) {
                            return null;
                        }
                        if (!isOutOwner()) {
                            TLRPC.MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
                            if (messageFwdHeader != null) {
                                TLRPC.Peer peer4 = messageFwdHeader.from_id;
                                if ((peer4 instanceof TLRPC.TL_peerUser) && peer4.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                }
                            }
                            TLRPC.Message message = this.messageOwner;
                            TLRPC.MessageFwdHeader messageFwdHeader2 = message.fwd_from;
                            if (messageFwdHeader2 == null || !(messageFwdHeader2.from_id instanceof TLRPC.TL_peerChannel)) {
                                if (messageFwdHeader2 == null || !(messageFwdHeader2.from_id instanceof TLRPC.TL_peerChat)) {
                                    if (messageFwdHeader2 != null && (messageFwdHeader2.from_id instanceof TLRPC.TL_peerUser)) {
                                        messagesController = MessagesController.getInstance(this.currentAccount);
                                        peer = this.messageOwner.fwd_from.from_id;
                                    } else {
                                        if (messageFwdHeader2 != null && (str = messageFwdHeader2.from_name) != null) {
                                            return str;
                                        }
                                        TLRPC.Peer peer5 = message.from_id;
                                        if (peer5 instanceof TLRPC.TL_peerChat) {
                                            messagesController2 = MessagesController.getInstance(this.currentAccount);
                                            peer3 = this.messageOwner.from_id;
                                        } else if (peer5 instanceof TLRPC.TL_peerChannel) {
                                            messagesController2 = MessagesController.getInstance(this.currentAccount);
                                            peer2 = this.messageOwner.from_id;
                                        } else if (peer5 != null || message.peer_id.channel_id == 0) {
                                            messagesController = MessagesController.getInstance(this.currentAccount);
                                            peer = this.messageOwner.from_id;
                                        } else {
                                            messagesController2 = MessagesController.getInstance(this.currentAccount);
                                            peer2 = this.messageOwner.peer_id;
                                        }
                                    }
                                    TLRPC.User user2 = messagesController.getUser(Long.valueOf(peer.user_id));
                                    chat = null;
                                    user = user2;
                                    if (user != null) {
                                        return UserObject.getUserName(user);
                                    }
                                    if (chat != null) {
                                        return chat.title;
                                    }
                                } else {
                                    messagesController2 = MessagesController.getInstance(this.currentAccount);
                                    peer3 = this.messageOwner.fwd_from.from_id;
                                }
                                j = peer3.chat_id;
                                chat = messagesController2.getChat(Long.valueOf(j));
                                if (user != null) {
                                }
                            } else {
                                messagesController2 = MessagesController.getInstance(this.currentAccount);
                                peer2 = this.messageOwner.fwd_from.from_id;
                            }
                            j = peer2.channel_id;
                            chat = messagesController2.getChat(Long.valueOf(j));
                            if (user != null) {
                            }
                        }
                        i = R.string.FromYou;
                        break;
                    }
                } else if (!documentAttribute.voice) {
                    String str2 = documentAttribute.performer;
                    return (TextUtils.isEmpty(str2) && z) ? LocaleController.getString(R.string.AudioUnknownArtist) : str2;
                }
                z2 = true;
                if (!z2) {
                }
            }
        }
        i = R.string.AudioUnknownArtist;
        return LocaleController.getString(i);
    }

    public String getMusicTitle() {
        return getMusicTitle(true);
    }

    public String getMusicTitle(boolean z) {
        TLRPC.Document document = getDocument();
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                    if (documentAttribute.voice) {
                        if (z) {
                            return LocaleController.formatDateAudio(this.messageOwner.date, true);
                        }
                        return null;
                    }
                    String str = documentAttribute.title;
                    if (str != null && str.length() != 0) {
                        return str;
                    }
                    String documentFileName = FileLoader.getDocumentFileName(document);
                    return (TextUtils.isEmpty(documentFileName) && z) ? LocaleController.getString(R.string.AudioUnknownTitle) : documentFileName;
                }
                if ((documentAttribute instanceof TLRPC.TL_documentAttributeVideo) && documentAttribute.round_message) {
                    if (!isQuickReply()) {
                        return LocaleController.formatDateAudio(this.messageOwner.date, true);
                    }
                    return LocaleController.formatString(R.string.BusinessInReplies, "/" + getQuickReplyDisplayName());
                }
            }
            String documentFileName2 = FileLoader.getDocumentFileName(document);
            if (!TextUtils.isEmpty(documentFileName2)) {
                return documentFileName2;
            }
        }
        return LocaleController.getString(R.string.AudioUnknownTitle);
    }

    public TLObject getPeerObject() {
        TLRPC.Message message = this.messageOwner;
        if (message == null) {
            return null;
        }
        TLRPC.Peer peer = message.peer_id;
        if ((peer instanceof TLRPC.TL_peerChannel_layer131) || (peer instanceof TLRPC.TL_peerChannel)) {
            return MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.channel_id));
        }
        if ((peer instanceof TLRPC.TL_peerUser_layer131) || (peer instanceof TLRPC.TL_peerUser)) {
            return MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.peer_id.user_id));
        }
        if ((peer instanceof TLRPC.TL_peerChat_layer131) || (peer instanceof TLRPC.TL_peerChat)) {
            return MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.chat_id));
        }
        return null;
    }

    public long getPollId() {
        if (this.type != 17) {
            return 0L;
        }
        return ((TLRPC.TL_messageMediaPoll) getMedia(this.messageOwner)).poll.id;
    }

    public TLRPC.VideoSize getPremiumStickerAnimation() {
        return getPremiumStickerAnimation(getDocument());
    }

    public float getProgress() {
        return 0.0f;
    }

    public String getQuickReplyDisplayName() {
        String quickReplyName = getQuickReplyName();
        if (quickReplyName != null) {
            return quickReplyName;
        }
        QuickRepliesController.QuickReply findReply = QuickRepliesController.getInstance(this.currentAccount).findReply(getQuickReplyId());
        return findReply != null ? findReply.name : "";
    }

    public int getQuickReplyId() {
        return getQuickReplyId(this.messageOwner);
    }

    public String getQuickReplyName() {
        return getQuickReplyName(this.messageOwner);
    }

    public TLRPC.MessagePeerReaction getRandomUnreadReaction() {
        ArrayList<TLRPC.MessagePeerReaction> arrayList;
        TLRPC.TL_messageReactions tL_messageReactions = this.messageOwner.reactions;
        if (tL_messageReactions == null || (arrayList = tL_messageReactions.recent_reactions) == null || arrayList.isEmpty()) {
            return null;
        }
        return this.messageOwner.reactions.recent_reactions.get(0);
    }

    public int getRealId() {
        TLRPC.Message message = this.messageOwner;
        int i = message.realId;
        return i != 0 ? i : message.id;
    }

    public int getRepliesCount() {
        TLRPC.MessageReplies messageReplies = this.messageOwner.replies;
        if (messageReplies != null) {
            return messageReplies.replies;
        }
        return 0;
    }

    public int getReplyAnyMsgId() {
        TLRPC.MessageReplyHeader messageReplyHeader = this.messageOwner.reply_to;
        if (messageReplyHeader == null) {
            return 0;
        }
        int i = messageReplyHeader.reply_to_top_id;
        return i != 0 ? i : messageReplyHeader.reply_to_msg_id;
    }

    public int getReplyMsgId() {
        TLRPC.MessageReplyHeader messageReplyHeader = this.messageOwner.reply_to;
        if (messageReplyHeader != null) {
            return messageReplyHeader.reply_to_msg_id;
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x0058, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_peerUser) != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x006a, code lost:
    
        r0 = peerNameWithIcon(r10.currentAccount, r5, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0063, code lost:
    
        r0 = peerNameWithIcon(r10.currentAccount, r5, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0061, code lost:
    
        if ((r5 instanceof org.telegram.tgnet.TLRPC.TL_peerUser) != false) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CharSequence getReplyQuoteNameWithIcon() {
        CharSequence charSequence;
        CharSequence append;
        TLRPC.Message message = this.messageOwner;
        if (message == null) {
            return "";
        }
        TLRPC.MessageReplyHeader messageReplyHeader = message.reply_to;
        CharSequence charSequence2 = null;
        if (messageReplyHeader != null) {
            if (messageReplyHeader.reply_from != null) {
                TLRPC.Peer peer = messageReplyHeader.reply_to_peer_id;
                boolean z = peer == null || DialogObject.getPeerDialogId(peer) != getDialogId();
                TLRPC.MessageFwdHeader messageFwdHeader = this.messageOwner.reply_to.reply_from;
                TLRPC.Peer peer2 = messageFwdHeader.from_id;
                if (peer2 == null) {
                    peer2 = messageFwdHeader.saved_from_peer;
                    if (peer2 == null) {
                        if (!TextUtils.isEmpty(messageFwdHeader.from_name)) {
                            append = z ? new SpannableStringBuilder(userSpan()).append((CharSequence) " ").append((CharSequence) this.messageOwner.reply_to.reply_from.from_name) : new SpannableStringBuilder(this.messageOwner.reply_to.reply_from.from_name);
                            charSequence2 = append;
                            charSequence = null;
                        }
                    }
                }
            }
            charSequence = null;
        } else if (DialogObject.isChatDialog(getDialogId())) {
            charSequence = peerNameWithIcon(this.currentAccount, getDialogId());
        } else {
            append = peerNameWithIcon(this.currentAccount, getDialogId());
            charSequence2 = append;
            charSequence = null;
        }
        TLRPC.Peer peer3 = this.messageOwner.reply_to.reply_to_peer_id;
        if (peer3 != null && DialogObject.getPeerDialogId(peer3) != getDialogId()) {
            TLRPC.Peer peer4 = this.messageOwner.reply_to.reply_to_peer_id;
            if (peer4 instanceof TLRPC.TL_peerUser) {
                charSequence2 = peerNameWithIcon(this.currentAccount, peer4, true);
            } else {
                charSequence = peerNameWithIcon(this.currentAccount, peer4);
            }
        }
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            if (DialogObject.isChatDialog(messageObject.getSenderId())) {
                if (charSequence == null) {
                    charSequence = peerNameWithIcon(this.currentAccount, this.replyMessageObject.getSenderId());
                }
            } else if (charSequence2 == null) {
                charSequence2 = peerNameWithIcon(this.currentAccount, this.replyMessageObject.getSenderId());
            }
        }
        return (charSequence == null || charSequence2 == null) ? charSequence != null ? charSequence : charSequence2 != null ? charSequence2 : LocaleController.getString(R.string.Loading) : new SpannableStringBuilder(charSequence2).append((CharSequence) " ").append(charSequence);
    }

    public int getReplyTopMsgId() {
        TLRPC.MessageReplyHeader messageReplyHeader = this.messageOwner.reply_to;
        if (messageReplyHeader != null) {
            return messageReplyHeader.reply_to_top_id;
        }
        return 0;
    }

    public int getReplyTopMsgId(boolean z) {
        TLRPC.MessageReplyHeader messageReplyHeader = this.messageOwner.reply_to;
        if (messageReplyHeader == null) {
            return 0;
        }
        if (z && (messageReplyHeader.flags & 2) > 0 && messageReplyHeader.reply_to_top_id == 0) {
            return 1;
        }
        return messageReplyHeader.reply_to_top_id;
    }

    public long getSavedDialogId() {
        return getSavedDialogId(UserConfig.getInstance(this.currentAccount).getClientUserId(), this.messageOwner);
    }

    public int getSecretTimeLeft() {
        TLRPC.Message message = this.messageOwner;
        int i = message.ttl;
        int i2 = message.destroyTime;
        return i2 != 0 ? Math.max(0, i2 - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) : i;
    }

    public CharSequence getSecretTimeString() {
        String str;
        if (!isSecretMedia()) {
            return null;
        }
        if (this.messageOwner.ttl == Integer.MAX_VALUE) {
            if (this.secretOnceSpan == null) {
                this.secretOnceSpan = new SpannableString("v");
                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_viewonce);
                coloredImageSpan.setTranslateX(-AndroidUtilities.dp(3.0f));
                coloredImageSpan.setWidth(AndroidUtilities.dp(13.0f));
                CharSequence charSequence = this.secretOnceSpan;
                ((Spannable) charSequence).setSpan(coloredImageSpan, 0, charSequence.length(), 33);
            }
            return TextUtils.concat(this.secretOnceSpan, "1");
        }
        int secretTimeLeft = getSecretTimeLeft();
        if (secretTimeLeft < 60) {
            str = secretTimeLeft + "s";
        } else {
            str = (secretTimeLeft / 60) + "m";
        }
        if (this.secretPlaySpan == null) {
            this.secretPlaySpan = new SpannableString("p");
            ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(R.drawable.play_mini_video);
            coloredImageSpan2.setTranslateX(AndroidUtilities.dp(1.0f));
            coloredImageSpan2.setWidth(AndroidUtilities.dp(13.0f));
            CharSequence charSequence2 = this.secretPlaySpan;
            ((Spannable) charSequence2).setSpan(coloredImageSpan2, 0, charSequence2.length(), 33);
        }
        return TextUtils.concat(this.secretPlaySpan, str);
    }

    public long getSenderId() {
        TLRPC.Peer peer;
        TLRPC.Message message = this.messageOwner;
        TLRPC.MessageFwdHeader messageFwdHeader = message.fwd_from;
        if (messageFwdHeader == null || (peer = messageFwdHeader.saved_from_peer) == null) {
            TLRPC.Peer peer2 = message.from_id;
            if (peer2 instanceof TLRPC.TL_peerUser) {
                return peer2.user_id;
            }
            if (peer2 instanceof TLRPC.TL_peerChannel) {
                return -peer2.channel_id;
            }
            if (peer2 instanceof TLRPC.TL_peerChat) {
                return -peer2.chat_id;
            }
            if (message.post) {
                return message.peer_id.channel_id;
            }
        } else {
            long j = peer.user_id;
            if (j != 0) {
                TLRPC.Peer peer3 = messageFwdHeader.from_id;
                return peer3 instanceof TLRPC.TL_peerUser ? peer3.user_id : j;
            }
            if (peer.channel_id != 0) {
                if (isSavedFromMegagroup()) {
                    TLRPC.Peer peer4 = this.messageOwner.fwd_from.from_id;
                    if (peer4 instanceof TLRPC.TL_peerUser) {
                        return peer4.user_id;
                    }
                }
                TLRPC.MessageFwdHeader messageFwdHeader2 = this.messageOwner.fwd_from;
                TLRPC.Peer peer5 = messageFwdHeader2.from_id;
                return peer5 instanceof TLRPC.TL_peerChannel ? -peer5.channel_id : peer5 instanceof TLRPC.TL_peerChat ? -peer5.chat_id : -messageFwdHeader2.saved_from_peer.channel_id;
            }
            long j2 = peer.chat_id;
            if (j2 != 0) {
                TLRPC.Peer peer6 = messageFwdHeader.from_id;
                return peer6 instanceof TLRPC.TL_peerUser ? peer6.user_id : peer6 instanceof TLRPC.TL_peerChannel ? -peer6.channel_id : peer6 instanceof TLRPC.TL_peerChat ? -peer6.chat_id : -j2;
            }
        }
        return 0L;
    }

    public long getSize() {
        VideoPlayer.VideoUri videoUri = this.highestQuality;
        if (videoUri != null) {
            return videoUri.document.size;
        }
        VideoPlayer.VideoUri videoUri2 = this.thumbQuality;
        return videoUri2 != null ? videoUri2.document.size : getMessageSize(this.messageOwner);
    }

    public String getStickerChar() {
        TLRPC.Document document = getDocument();
        if (document == null) {
            return null;
        }
        Iterator<TLRPC.DocumentAttribute> it = document.attributes.iterator();
        while (it.hasNext()) {
            TLRPC.DocumentAttribute next = it.next();
            if (next instanceof TLRPC.TL_documentAttributeSticker) {
                return next.alt;
            }
        }
        return null;
    }

    public String getStickerEmoji() {
        TLRPC.Document document = getDocument();
        if (document == null) {
            return null;
        }
        for (int i = 0; i < document.attributes.size(); i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if ((documentAttribute instanceof TLRPC.TL_documentAttributeSticker) || (documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji)) {
                String str = documentAttribute.alt;
                if (str == null || str.length() <= 0) {
                    return null;
                }
                return documentAttribute.alt;
            }
        }
        return null;
    }

    public TLRPC.WebPage getStoryMentionWebpage() {
        if (!isStoryMention()) {
            return null;
        }
        TLRPC.WebPage webPage = this.storyMentionWebpage;
        if (webPage != null) {
            return webPage;
        }
        TLRPC.TL_webPage tL_webPage = new TLRPC.TL_webPage();
        tL_webPage.type = "telegram_story";
        TLRPC.TL_webPageAttributeStory tL_webPageAttributeStory = new TLRPC.TL_webPageAttributeStory();
        tL_webPageAttributeStory.id = this.messageOwner.media.id;
        tL_webPageAttributeStory.peer = MessagesController.getInstance(this.currentAccount).getPeer(this.messageOwner.media.user_id);
        TL_stories.StoryItem storyItem = this.messageOwner.media.storyItem;
        if (storyItem != null) {
            tL_webPageAttributeStory.flags |= 1;
            tL_webPageAttributeStory.storyItem = storyItem;
        }
        tL_webPage.attributes.add(tL_webPageAttributeStory);
        this.storyMentionWebpage = tL_webPage;
        return tL_webPage;
    }

    public TextPaint getTextPaint() {
        if (this.emojiOnlyCount < 1 || this.messageOwner == null || hasNonEmojiEntities()) {
            return Theme.chat_msgTextPaint;
        }
        int i = this.emojiOnlyCount;
        int i2 = this.animatedEmojiCount;
        boolean z = i == i2;
        switch (Math.max(i, i2)) {
            case 0:
            case 1:
            case 2:
                TextPaint[] textPaintArr = Theme.chat_msgTextPaintEmoji;
                return z ? textPaintArr[0] : textPaintArr[2];
            case 3:
                TextPaint[] textPaintArr2 = Theme.chat_msgTextPaintEmoji;
                return z ? textPaintArr2[1] : textPaintArr2[3];
            case 4:
                TextPaint[] textPaintArr3 = Theme.chat_msgTextPaintEmoji;
                return z ? textPaintArr3[2] : textPaintArr3[4];
            case 5:
                TextPaint[] textPaintArr4 = Theme.chat_msgTextPaintEmoji;
                return z ? textPaintArr4[3] : textPaintArr4[5];
            case 6:
                TextPaint[] textPaintArr5 = Theme.chat_msgTextPaintEmoji;
                return z ? textPaintArr5[4] : textPaintArr5[5];
            default:
                return Theme.chat_msgTextPaintEmoji[5];
        }
    }

    public CharSequence getVoiceTranscription() {
        String str;
        TLRPC.Message message = this.messageOwner;
        if (message == null || (str = message.voiceTranscription) == null) {
            return null;
        }
        if (!TextUtils.isEmpty(str)) {
            String str2 = this.messageOwner.voiceTranscription;
            return !TextUtils.isEmpty(str2) ? Emoji.replaceEmoji((CharSequence) str2, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false) : str2;
        }
        SpannableString spannableString = new SpannableString(LocaleController.getString(R.string.NoWordsRecognized));
        spannableString.setSpan(new CharacterStyle() { // from class: org.telegram.messenger.MessageObject.1
            @Override // android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setTextSize(textPaint.getTextSize() * 0.8f);
                textPaint.setColor(Theme.chat_timePaint.getColor());
            }
        }, 0, spannableString.length(), 33);
        return spannableString;
    }

    public byte[] getWaveform() {
        if (getDocument() == null) {
            return null;
        }
        int i = 0;
        for (int i2 = 0; i2 < getDocument().attributes.size(); i2++) {
            TLRPC.DocumentAttribute documentAttribute = getDocument().attributes.get(i2);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                byte[] bArr = documentAttribute.waveform;
                if (bArr == null || bArr.length == 0) {
                    MediaController.getInstance().generateWaveform(this);
                }
                return documentAttribute.waveform;
            }
        }
        if (!isRoundVideo()) {
            return null;
        }
        if (this.randomWaveform == null) {
            this.randomWaveform = new byte[120];
            while (true) {
                byte[] bArr2 = this.randomWaveform;
                if (i >= bArr2.length) {
                    break;
                }
                bArr2[i] = (byte) (Math.random() * 255.0d);
                i++;
            }
        }
        return this.randomWaveform;
    }

    public ArrayList<MessageObject> getWebPagePhotos(ArrayList<MessageObject> arrayList, ArrayList<TLRPC.PageBlock> arrayList2) {
        TLRPC.WebPage webPage;
        TLRPC.Page page;
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        if (getMedia(this.messageOwner) == null || getMedia(this.messageOwner).webpage == null || (page = (webPage = getMedia(this.messageOwner).webpage).cached_page) == null) {
            return arrayList;
        }
        if (arrayList2 == null) {
            arrayList2 = page.blocks;
        }
        for (int i = 0; i < arrayList2.size(); i++) {
            TLRPC.PageBlock pageBlock = arrayList2.get(i);
            if (pageBlock instanceof TLRPC.TL_pageBlockSlideshow) {
                TLRPC.TL_pageBlockSlideshow tL_pageBlockSlideshow = (TLRPC.TL_pageBlockSlideshow) pageBlock;
                for (int i2 = 0; i2 < tL_pageBlockSlideshow.items.size(); i2++) {
                    arrayList.add(getMessageObjectForBlock(webPage, tL_pageBlockSlideshow.items.get(i2)));
                }
            } else if (pageBlock instanceof TLRPC.TL_pageBlockCollage) {
                TLRPC.TL_pageBlockCollage tL_pageBlockCollage = (TLRPC.TL_pageBlockCollage) pageBlock;
                for (int i3 = 0; i3 < tL_pageBlockCollage.items.size(); i3++) {
                    arrayList.add(getMessageObjectForBlock(webPage, tL_pageBlockCollage.items.get(i3)));
                }
            }
        }
        return arrayList;
    }

    public boolean hasAttachedStickers() {
        if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto) {
            return getMedia(this.messageOwner).photo != null && getMedia(this.messageOwner).photo.has_stickers;
        }
        if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument) {
            return isDocumentHasAttachedStickers(getMedia(this.messageOwner).document);
        }
        return false;
    }

    public boolean hasEntitiesFromServer() {
        TLRPC.Message message = this.messageOwner;
        if (message == null || message.entities == null) {
            return false;
        }
        for (int i = 0; i < this.messageOwner.entities.size(); i++) {
            TLRPC.MessageEntity messageEntity = this.messageOwner.entities.get(i);
            if ((messageEntity instanceof TLRPC.TL_messageEntityPhone) || (messageEntity instanceof TLRPC.TL_messageEntityBankCard)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasExtendedMedia() {
        TLRPC.MessageMedia messageMedia = this.messageOwner.media;
        return (messageMedia == null || (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) || messageMedia.extended_media.isEmpty()) ? false : true;
    }

    public boolean hasExtendedMediaPreview() {
        TLRPC.MessageMedia messageMedia = this.messageOwner.media;
        return (messageMedia == null || (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) || messageMedia.extended_media.isEmpty() || !(this.messageOwner.media.extended_media.get(0) instanceof TLRPC.TL_messageExtendedMediaPreview)) ? false : true;
    }

    public boolean hasHighlightedWords() {
        ArrayList<String> arrayList = this.highlightedWords;
        return (arrayList == null || arrayList.isEmpty()) ? false : true;
    }

    public boolean hasInlineBotButtons() {
        TLRPC.Message message;
        if (!this.isRestrictedMessage && !this.isRepostPreview && (message = this.messageOwner) != null) {
            TLRPC.ReplyMarkup replyMarkup = message.reply_markup;
            if ((replyMarkup instanceof TLRPC.TL_replyInlineMarkup) && !replyMarkup.rows.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLinkMediaToMakeSmall() {
        boolean z = !this.isRestrictedMessage && (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC.TL_webPage);
        TLRPC.WebPage webPage = z ? getMedia(this.messageOwner).webpage : null;
        String str = webPage != null ? webPage.type : null;
        if (!z || isGiveawayOrGiveawayResults() || webPage == null) {
            return false;
        }
        if (webPage.photo != null || isVideoDocument(webPage.document)) {
            return ((TextUtils.isEmpty(webPage.description) && TextUtils.isEmpty(webPage.title)) || isSponsored() || "telegram_megagroup".equals(str) || "telegram_background".equals(str) || "telegram_voicechat".equals(str) || "telegram_videochat".equals(str) || "telegram_livestream".equals(str) || "telegram_user".equals(str) || "telegram_story".equals(str) || "telegram_channel_boost".equals(str) || "telegram_group_boost".equals(str) || "telegram_chat".equals(str)) ? false : true;
        }
        return false;
    }

    public boolean hasMediaSpoilers() {
        TLRPC.MessageMedia messageMedia;
        return (!this.isRepostPreview && (((messageMedia = this.messageOwner.media) != null && messageMedia.spoiler) || needDrawBluredPreview())) || isHiddenSensitive();
    }

    public boolean hasPaidMediaPreview() {
        TLRPC.MessageMedia messageMedia = this.messageOwner.media;
        return messageMedia != null && (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) && !messageMedia.extended_media.isEmpty() && (this.messageOwner.media.extended_media.get(0) instanceof TLRPC.TL_messageExtendedMediaPreview);
    }

    public boolean hasReaction(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        if (hasReactions() && visibleReaction != null) {
            for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
                if (visibleReaction.isSame(this.messageOwner.reactions.results.get(i).reaction)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasReactions() {
        TLRPC.TL_messageReactions tL_messageReactions = this.messageOwner.reactions;
        return (tL_messageReactions == null || tL_messageReactions.results.isEmpty()) ? false : true;
    }

    public boolean hasReplies() {
        TLRPC.MessageReplies messageReplies = this.messageOwner.replies;
        return messageReplies != null && messageReplies.replies > 0;
    }

    public boolean hasRevealedExtendedMedia() {
        TLRPC.MessageMedia messageMedia = this.messageOwner.media;
        return (messageMedia == null || (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) || messageMedia.extended_media.isEmpty() || !(this.messageOwner.media.extended_media.get(0) instanceof TLRPC.TL_messageExtendedMedia)) ? false : true;
    }

    public boolean hasValidGroupId() {
        ArrayList<TLRPC.PhotoSize> arrayList;
        int i;
        return getGroupId() != 0 && (!((arrayList = this.photoThumbs) == null || arrayList.isEmpty()) || (i = this.type) == 3 || i == 1 || isMusic() || isDocument());
    }

    public boolean hasValidReplyMessageObject() {
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            if (!(message instanceof TLRPC.TL_messageEmpty)) {
                TLRPC.MessageAction messageAction = message.action;
                if (!(messageAction instanceof TLRPC.TL_messageActionHistoryClear) && !(messageAction instanceof TLRPC.TL_messageActionTopicCreate)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasVideoQualities() {
        TLRPC.MessageMedia messageMedia;
        if (this.videoQualitiesCached == null) {
            try {
                TLRPC.Message message = this.messageOwner;
                boolean z = false;
                if (message != null && (messageMedia = message.media) != null && messageMedia.document != null && !messageMedia.alt_documents.isEmpty()) {
                    int i = this.currentAccount;
                    TLRPC.Message message2 = this.messageOwner;
                    ArrayList<VideoPlayer.Quality> qualities = VideoPlayer.getQualities(i, message2 != null ? message2.media : null);
                    this.videoQualities = qualities;
                    if (qualities != null && qualities.size() > 1) {
                        z = true;
                    }
                    this.videoQualitiesCached = Boolean.valueOf(z);
                    this.highestQuality = VideoPlayer.getQualityForPlayer(this.videoQualities);
                    this.thumbQuality = VideoPlayer.getQualityForThumb(this.videoQualities);
                }
                this.videoQualitiesCached = Boolean.FALSE;
                return false;
            } catch (Exception e) {
                FileLog.e(e);
                this.videoQualitiesCached = Boolean.FALSE;
            }
        }
        return this.videoQualitiesCached.booleanValue();
    }

    public boolean isAlbumSingle() {
        return getMedia(this) instanceof TLRPC.TL_messageMediaPaidMedia;
    }

    public boolean isAndroidTheme() {
        if (getMedia(this.messageOwner) != null && getMedia(this.messageOwner).webpage != null && !getMedia(this.messageOwner).webpage.attributes.isEmpty()) {
            int size = getMedia(this.messageOwner).webpage.attributes.size();
            for (int i = 0; i < size; i++) {
                TLRPC.WebPageAttribute webPageAttribute = getMedia(this.messageOwner).webpage.attributes.get(i);
                if (webPageAttribute instanceof TLRPC.TL_webPageAttributeTheme) {
                    TLRPC.TL_webPageAttributeTheme tL_webPageAttributeTheme = (TLRPC.TL_webPageAttributeTheme) webPageAttribute;
                    ArrayList<TLRPC.Document> arrayList = tL_webPageAttributeTheme.documents;
                    int size2 = arrayList.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        if ("application/x-tgtheme-android".equals(arrayList.get(i2).mime_type)) {
                            return true;
                        }
                    }
                    if (tL_webPageAttributeTheme.settings != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isAnimatedAnimatedEmoji() {
        return isAnimatedEmoji() && isAnimatedEmoji(getDocument());
    }

    public boolean isAnimatedEmoji() {
        return (this.emojiAnimatedSticker == null && this.emojiAnimatedStickerId == null) ? false : true;
    }

    public boolean isAnimatedEmojiStickerSingle() {
        return this.emojiAnimatedStickerId != null;
    }

    public boolean isAnimatedEmojiStickers() {
        return this.type == 19;
    }

    public boolean isAnimatedSticker() {
        int i = this.type;
        if (i != 1000) {
            return i == 15;
        }
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(getDialogId());
        if (isEncryptedDialog && this.messageOwner.stickerVerified != 1) {
            return false;
        }
        if (this.emojiAnimatedStickerId == null || this.emojiAnimatedSticker != null) {
            return isAnimatedStickerDocument(getDocument(), (this.emojiAnimatedSticker == null && isEncryptedDialog && !isOut()) ? false : true);
        }
        return true;
    }

    public boolean isAnyGift() {
        int i = this.type;
        return i == 30 || i == 18 || i == 25;
    }

    public boolean isAnyKindOfSticker() {
        int i = this.type;
        return i == 13 || i == 15 || i == 19;
    }

    public boolean isBotPreview() {
        return this.storyItem instanceof StoriesController.BotPreview;
    }

    public boolean isComments() {
        TLRPC.MessageReplies messageReplies = this.messageOwner.replies;
        return messageReplies != null && messageReplies.comments;
    }

    public boolean isContentUnread() {
        return this.messageOwner.media_unread;
    }

    public boolean isCurrentWallpaper() {
        TLRPC.Message message;
        TLRPC.MessageAction messageAction;
        TLRPC.UserFull userFull;
        TLRPC.WallPaper wallPaper;
        return (!isWallpaperAction() || (message = this.messageOwner) == null || (messageAction = message.action) == null || messageAction.wallpaper == null || (userFull = MessagesController.getInstance(this.currentAccount).getUserFull(getDialogId())) == null || (wallPaper = userFull.wallpaper) == null || !userFull.wallpaper_overridden || this.messageOwner.action.wallpaper.id != wallPaper.id) ? false : true;
    }

    public boolean isDice() {
        return getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDice;
    }

    public boolean isDocument() {
        return (getDocument() == null || isVideo() || isMusic() || isVoice() || isAnyKindOfSticker()) ? false : true;
    }

    public boolean isEdited() {
        TLRPC.Message message = this.messageOwner;
        return (message == null || (message.flags & 32768) == 0 || message.edit_date == 0 || message.edit_hide) ? false : true;
    }

    public boolean isEditing() {
        TLRPC.Message message = this.messageOwner;
        return message.send_state == 3 && message.id > 0;
    }

    public boolean isEditingMedia() {
        return getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto ? getMedia(this.messageOwner).photo.id == 0 : (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument) && getMedia(this.messageOwner).document.dc_id == 0;
    }

    public boolean isExpiredLiveLocation(int i) {
        TLRPC.Message message = this.messageOwner;
        return message.date + getMedia(message).period <= i;
    }

    public boolean isExpiredStory() {
        int i = this.type;
        return (i == 23 || i == 24) && (this.messageOwner.media.storyItem instanceof TL_stories.TL_storyItemDeleted);
    }

    public boolean isFactCheckable() {
        int i;
        return getId() >= 0 && !isSponsored() && ((i = this.type) == 0 || i == 2 || i == 1 || i == 3 || i == 8 || i == 9);
    }

    public boolean isFcmMessage() {
        return this.localType != 0;
    }

    public boolean isForwarded() {
        return isForwardedMessage(this.messageOwner);
    }

    public boolean isForwardedChannelPost() {
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Message message = this.messageOwner;
        TLRPC.Peer peer = message.from_id;
        if ((peer instanceof TLRPC.TL_peerChannel) && (messageFwdHeader = message.fwd_from) != null && messageFwdHeader.channel_post != 0) {
            TLRPC.Peer peer2 = messageFwdHeader.saved_from_peer;
            if ((peer2 instanceof TLRPC.TL_peerChannel) && peer.channel_id == peer2.channel_id) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isFromChannel() {
        TLRPC.Chat chat;
        TLRPC.Peer peer;
        TLRPC.Peer peer2 = this.messageOwner.peer_id;
        TLRPC.Chat chat2 = null;
        if (peer2 != null) {
            long j = peer2.channel_id;
            if (j != 0) {
                chat = getChat(null, null, j);
                if (!(this.messageOwner.peer_id instanceof TLRPC.TL_peerChannel) && ChatObject.isChannelAndNotMegaGroup(chat)) {
                    return true;
                }
                peer = this.messageOwner.from_id;
                if (peer != null) {
                    long j2 = peer.channel_id;
                    if (j2 != 0) {
                        chat2 = getChat(null, null, j2);
                    }
                }
                return (this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) && ChatObject.isChannelAndNotMegaGroup(chat2);
            }
        }
        chat = null;
        if (!(this.messageOwner.peer_id instanceof TLRPC.TL_peerChannel)) {
        }
        peer = this.messageOwner.from_id;
        if (peer != null) {
        }
        if (this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) {
            return false;
        }
    }

    public boolean isFromChat() {
        TLRPC.Peer peer;
        if (getDialogId() == UserConfig.getInstance(this.currentAccount).clientUserId) {
            return true;
        }
        TLRPC.Peer peer2 = this.messageOwner.peer_id;
        TLRPC.Chat chat = null;
        if (peer2 != null) {
            long j = peer2.channel_id;
            if (j != 0) {
                chat = getChat(null, null, j);
            }
        }
        if (!(ChatObject.isChannel(chat) && chat.megagroup) && ((peer = this.messageOwner.peer_id) == null || peer.chat_id == 0)) {
            return (peer == null || peer.channel_id == 0 || chat == null || !chat.megagroup) ? false : true;
        }
        return true;
    }

    public boolean isFromGroup() {
        TLRPC.Message message = this.messageOwner;
        if (message == null) {
            return false;
        }
        TLRPC.Peer peer = message.peer_id;
        TLRPC.Chat chat = null;
        if (peer != null) {
            long j = peer.channel_id;
            if (j != 0) {
                chat = getChat(null, null, j);
            }
        }
        return (this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) && ChatObject.isChannel(chat) && chat.megagroup;
    }

    public boolean isFromUser() {
        TLRPC.Message message = this.messageOwner;
        return (message.from_id instanceof TLRPC.TL_peerUser) && !message.post;
    }

    public boolean isGame() {
        return isGameMessage(this.messageOwner);
    }

    public boolean isGif() {
        return isGifMessage(this.messageOwner);
    }

    public boolean isGiveaway() {
        return this.type == 26;
    }

    public boolean isGiveawayOrGiveawayResults() {
        return isGiveaway() || isGiveawayResults();
    }

    public boolean isGiveawayResults() {
        return this.type == 28;
    }

    public boolean isHiddenSensitive() {
        return isSensitive() && !MessagesController.getInstance(this.currentAccount).showSensitiveContent();
    }

    public boolean isImportedForward() {
        TLRPC.MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
        return messageFwdHeader != null && messageFwdHeader.imported;
    }

    public boolean isInvoice() {
        return isInvoiceMessage(this.messageOwner);
    }

    public boolean isLinkMediaSmall() {
        TLRPC.WebPage webPage = (!this.isRestrictedMessage && (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC.TL_webPage)) ? getMedia(this.messageOwner).webpage : null;
        String str = webPage != null ? webPage.type : null;
        return !(webPage != null && TextUtils.isEmpty(webPage.description) && TextUtils.isEmpty(webPage.title)) && ("app".equals(str) || "profile".equals(str) || "article".equals(str) || "telegram_bot".equals(str) || "telegram_user".equals(str) || "telegram_channel".equals(str) || "telegram_megagroup".equals(str) || "telegram_voicechat".equals(str) || "telegram_videochat".equals(str) || "telegram_livestream".equals(str) || "telegram_channel_boost".equals(str) || "telegram_group_boost".equals(str) || "telegram_chat".equals(str));
    }

    public boolean isLinkedToChat(long j) {
        TLRPC.MessageReplies messageReplies = this.messageOwner.replies;
        return messageReplies != null && (j == 0 || messageReplies.channel_id == j);
    }

    public boolean isLiveLocation() {
        return isLiveLocationMessage(this.messageOwner);
    }

    public boolean isLocation() {
        return isLocationMessage(this.messageOwner);
    }

    public boolean isMask() {
        return isMaskMessage(this.messageOwner);
    }

    public boolean isMediaEmpty() {
        return isMediaEmpty(this.messageOwner);
    }

    public boolean isMediaEmpty(boolean z) {
        return isMediaEmpty(this.messageOwner, z);
    }

    public boolean isMediaEmptyWebpage() {
        return isMediaEmptyWebpage(this.messageOwner);
    }

    public boolean isMusic() {
        return (!isMusicMessage(this.messageOwner) || isVideo() || isRoundVideo()) ? false : true;
    }

    public Boolean isMyPaidReactionAnonymous() {
        TLRPC.TL_messageReactions tL_messageReactions;
        ArrayList<TLRPC.MessageReactor> arrayList;
        TLRPC.Message message = this.messageOwner;
        if (message == null || (tL_messageReactions = message.reactions) == null || (arrayList = tL_messageReactions.top_reactors) == null) {
            return null;
        }
        Iterator<TLRPC.MessageReactor> it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC.MessageReactor next = it.next();
            if (next != null && next.my) {
                return Boolean.valueOf(next.anonymous);
            }
        }
        return null;
    }

    public boolean isNewGif() {
        return getMedia(this.messageOwner) != null && isNewGifDocument(getDocument());
    }

    public boolean isOut() {
        return this.messageOwner.out;
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0068, code lost:
    
        if (r0.user_id != r2) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x006b, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0084, code lost:
    
        if (r5.user_id == r2) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0088, code lost:
    
        if (r0.saved_out != false) goto L41;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isOutOwner() {
        Boolean valueOf;
        boolean z = true;
        if (this.previewForward) {
            return true;
        }
        Boolean bool = this.isOutOwnerCached;
        if (bool != null) {
            return bool.booleanValue();
        }
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (!this.isSaved && getDialogId() != clientUserId) {
            TLRPC.Peer peer = this.messageOwner.peer_id;
            TLRPC.Chat chat = null;
            if (peer != null) {
                long j = peer.channel_id;
                if (j != 0) {
                    chat = getChat(null, null, j);
                }
            }
            TLRPC.Message message = this.messageOwner;
            if (message.out) {
                TLRPC.Peer peer2 = message.from_id;
                if ((peer2 instanceof TLRPC.TL_peerUser) || ((peer2 instanceof TLRPC.TL_peerChannel) && !ChatObject.isChannelAndNotMegaGroup(chat))) {
                    TLRPC.Message message2 = this.messageOwner;
                    if (!message2.post) {
                        TLRPC.MessageFwdHeader messageFwdHeader = message2.fwd_from;
                        if (messageFwdHeader != null) {
                            TLRPC.Peer peer3 = messageFwdHeader.saved_from_peer;
                            if (peer3 != null) {
                            }
                            valueOf = Boolean.valueOf(z);
                        }
                        valueOf = Boolean.TRUE;
                    }
                }
            }
            this.isOutOwnerCached = Boolean.FALSE;
            return false;
        }
        TLRPC.MessageFwdHeader messageFwdHeader2 = this.messageOwner.fwd_from;
        if (messageFwdHeader2 != null) {
            TLRPC.Peer peer4 = messageFwdHeader2.from_id;
            if (peer4 != null) {
            }
        }
        valueOf = Boolean.TRUE;
        this.isOutOwnerCached = valueOf;
        return z;
    }

    public boolean isPaidReactionChosen() {
        if (this.messageOwner.reactions == null) {
            return false;
        }
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (this.messageOwner.reactions.results.get(i).reaction instanceof TLRPC.TL_reactionPaid) {
                return this.messageOwner.reactions.results.get(i).chosen;
            }
        }
        return false;
    }

    public boolean isPhoto() {
        return isPhoto(this.messageOwner);
    }

    public boolean isPoll() {
        return this.type == 17;
    }

    public boolean isPollClosed() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC.TL_messageMediaPoll) getMedia(this.messageOwner)).poll.closed;
    }

    public boolean isPremiumSticker() {
        if (getMedia(this.messageOwner) == null || !getMedia(this.messageOwner).nopremium) {
            return isPremiumSticker(getDocument());
        }
        return false;
    }

    public boolean isPrivateForward() {
        TLRPC.MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
        return (messageFwdHeader == null || TextUtils.isEmpty(messageFwdHeader.from_name)) ? false : true;
    }

    public boolean isPublicPoll() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC.TL_messageMediaPoll) getMedia(this.messageOwner)).poll.public_voters;
    }

    public boolean isQuickReply() {
        return isQuickReply(this.messageOwner);
    }

    public boolean isQuiz() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC.TL_messageMediaPoll) getMedia(this.messageOwner)).poll.quiz;
    }

    public boolean isReactionsAvailable() {
        return (isEditing() || isSponsored() || !isSent() || this.messageOwner.action != null || isExpiredStory()) ? false : true;
    }

    public boolean isReply() {
        TLRPC.Message message;
        TLRPC.MessageReplyHeader messageReplyHeader;
        MessageObject messageObject = this.replyMessageObject;
        return ((messageObject != null && (messageObject.messageOwner instanceof TLRPC.TL_messageEmpty)) || (messageReplyHeader = (message = this.messageOwner).reply_to) == null || (messageReplyHeader.reply_to_msg_id == 0 && messageReplyHeader.reply_to_random_id == 0) || (message.flags & 8) == 0) ? false : true;
    }

    public boolean isReplyToStory() {
        TLRPC.Message message;
        TLRPC.MessageReplyHeader messageReplyHeader;
        MessageObject messageObject = this.replyMessageObject;
        return ((messageObject != null && (messageObject.messageOwner instanceof TLRPC.TL_messageEmpty)) || (messageReplyHeader = (message = this.messageOwner).reply_to) == null || messageReplyHeader.story_id == 0 || (message.flags & 8) == 0) ? false : true;
    }

    public boolean isRoundOnce() {
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        return isRoundVideo() && (message = this.messageOwner) != null && (messageMedia = message.media) != null && messageMedia.ttl_seconds == Integer.MAX_VALUE;
    }

    public boolean isRoundVideo() {
        if (this.isRoundVideoCached == 0) {
            this.isRoundVideoCached = (this.type == 5 || isRoundVideoMessage(this.messageOwner)) ? 1 : 2;
        }
        return this.isRoundVideoCached == 1;
    }

    public boolean isSavedFromMegagroup() {
        TLRPC.Peer peer;
        TLRPC.MessageFwdHeader messageFwdHeader = this.messageOwner.fwd_from;
        if (messageFwdHeader == null || (peer = messageFwdHeader.saved_from_peer) == null || peer.channel_id == 0) {
            return false;
        }
        return ChatObject.isMegagroup(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.saved_from_peer.channel_id)));
    }

    public boolean isSecretMedia() {
        int i;
        TLRPC.Message message = this.messageOwner;
        return message instanceof TLRPC.TL_message_secret ? (((getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) || isGif()) && (i = this.messageOwner.ttl) > 0 && i <= 60) || isVoice() || isRoundVideo() || isVideo() : (message instanceof TLRPC.TL_message) && getMedia(message) != null && getMedia(this.messageOwner).ttl_seconds != 0 && ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument));
    }

    public boolean isSendError() {
        TLRPC.Message message = this.messageOwner;
        if (message.send_state != 2 || message.id >= 0) {
            if (this.scheduled && message.id > 0) {
                if (message.date < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - (this.messageOwner.video_processing_pending ? NotificationCenter.uploadStoryProgress : 60)) {
                }
            }
            return false;
        }
        return true;
    }

    public boolean isSending() {
        TLRPC.Message message = this.messageOwner;
        return message.send_state == 1 && message.id < 0;
    }

    public boolean isSensitive() {
        TLRPC.Chat chat;
        Boolean bool = this.isSensitiveCached;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (this.messageOwner == null || !canBeSensitive()) {
            return false;
        }
        if (!this.messageOwner.restriction_reason.isEmpty()) {
            for (int i = 0; i < this.messageOwner.restriction_reason.size(); i++) {
                TLRPC.RestrictionReason restrictionReason = this.messageOwner.restriction_reason.get(i);
                if ("sensitive".equals(restrictionReason.reason) && ("all".equals(restrictionReason.platform) || (((!ApplicationLoader.isStandaloneBuild() && !BuildVars.isBetaApp()) || BuildVars.DEBUG_PRIVATE_VERSION) && "android".equals(restrictionReason.platform)))) {
                    break;
                }
            }
        }
        if (getDialogId() < 0 && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()))) != null && chat.restriction_reason != null) {
            for (int i2 = 0; i2 < chat.restriction_reason.size(); i2++) {
                TLRPC.RestrictionReason restrictionReason2 = chat.restriction_reason.get(i2);
                if (!"sensitive".equals(restrictionReason2.reason) || (!"all".equals(restrictionReason2.platform) && (((ApplicationLoader.isStandaloneBuild() || BuildVars.isBetaApp()) && !BuildVars.DEBUG_PRIVATE_VERSION) || !"android".equals(restrictionReason2.platform)))) {
                }
                this.isSensitiveCached = Boolean.TRUE;
                return true;
            }
        }
        this.isSensitiveCached = Boolean.FALSE;
        return false;
    }

    public boolean isSent() {
        TLRPC.Message message = this.messageOwner;
        return message.send_state == 0 || message.id > 0;
    }

    public boolean isSponsored() {
        return this.sponsoredId != null;
    }

    public boolean isStarGiftAction() {
        TLRPC.Message message = this.messageOwner;
        return message != null && (message.action instanceof TLRPC.TL_messageActionStarGift);
    }

    public boolean isSticker() {
        int i = this.type;
        return i != 1000 ? i == 13 : isStickerDocument(getDocument()) || isVideoSticker(getDocument());
    }

    public boolean isStory() {
        return this.storyItem != null;
    }

    public boolean isStoryMedia() {
        TLRPC.Message message = this.messageOwner;
        return message != null && (message.media instanceof TLRPC.TL_messageMediaStory);
    }

    public boolean isStoryMention() {
        return this.type == 24 && !isExpiredStory();
    }

    public boolean isSupergroup() {
        if (this.localSupergroup) {
            return true;
        }
        Boolean bool = this.cachedIsSupergroup;
        if (bool != null) {
            return bool.booleanValue();
        }
        TLRPC.Peer peer = this.messageOwner.peer_id;
        if (peer != null) {
            long j = peer.channel_id;
            if (j != 0) {
                TLRPC.Chat chat = getChat(null, null, j);
                if (chat == null) {
                    return false;
                }
                boolean z = chat.megagroup;
                this.cachedIsSupergroup = Boolean.valueOf(z);
                return z;
            }
        }
        this.cachedIsSupergroup = Boolean.FALSE;
        return false;
    }

    public boolean isTheme() {
        return (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_theme".equals(getMedia(this.messageOwner).webpage.type);
    }

    public boolean isUnread() {
        TLRPC.Message message = this.messageOwner;
        return message != null && message.unread;
    }

    public boolean isUnsupported() {
        return getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaUnsupported;
    }

    public boolean isVideo() {
        return isVideoMessage(this.messageOwner);
    }

    public boolean isVideoAvatar() {
        TLRPC.Photo photo;
        TLRPC.MessageAction messageAction = this.messageOwner.action;
        return (messageAction == null || (photo = messageAction.photo) == null || photo.video_sizes.isEmpty()) ? false : true;
    }

    public boolean isVideoCall() {
        TLRPC.MessageAction messageAction = this.messageOwner.action;
        return (messageAction instanceof TLRPC.TL_messageActionPhoneCall) && messageAction.video;
    }

    public boolean isVideoSticker() {
        return getDocument() != null && isVideoStickerDocument(getDocument());
    }

    public boolean isVideoStory() {
        TL_stories.StoryItem storyItem;
        TLRPC.MessageMedia messageMedia;
        TLRPC.MessageMedia media = getMedia(this.messageOwner);
        if (media == null || (storyItem = media.storyItem) == null || (messageMedia = storyItem.media) == null) {
            return false;
        }
        return isVideoDocument(messageMedia.document);
    }

    public boolean isVoice() {
        return isVoiceMessage(this.messageOwner);
    }

    public boolean isVoiceOnce() {
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        return isVoice() && (message = this.messageOwner) != null && (messageMedia = message.media) != null && messageMedia.ttl_seconds == Integer.MAX_VALUE;
    }

    public boolean isVoiceTranscriptionOpen() {
        if (this.messageOwner != null && (isVoice() || (isRoundVideo() && TranscribeButton.isVideoTranscriptionOpen(this)))) {
            TLRPC.Message message = this.messageOwner;
            if (message.voiceTranscriptionOpen && message.voiceTranscription != null && (message.voiceTranscriptionFinal || TranscribeButton.isTranscribing(this))) {
                return true;
            }
        }
        return false;
    }

    public boolean isVoted() {
        TLRPC.TL_messageMediaPoll tL_messageMediaPoll;
        TLRPC.PollResults pollResults;
        if (this.type == 17 && (pollResults = (tL_messageMediaPoll = (TLRPC.TL_messageMediaPoll) getMedia(this.messageOwner)).results) != null && !pollResults.results.isEmpty()) {
            int size = tL_messageMediaPoll.results.results.size();
            for (int i = 0; i < size; i++) {
                if (tL_messageMediaPoll.results.results.get(i).chosen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWallpaper() {
        return (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_background".equals(getMedia(this.messageOwner).webpage.type);
    }

    public boolean isWallpaperAction() {
        TLRPC.Message message;
        return this.type == 22 || ((message = this.messageOwner) != null && (message.action instanceof TLRPC.TL_messageActionSetSameChatWallPaper));
    }

    public boolean isWallpaperForBoth() {
        TLRPC.Message message;
        if (isWallpaperAction() && (message = this.messageOwner) != null) {
            TLRPC.MessageAction messageAction = message.action;
            if ((messageAction instanceof TLRPC.TL_messageActionSetChatWallPaper) && ((TLRPC.TL_messageActionSetChatWallPaper) messageAction).for_both) {
                return true;
            }
        }
        return false;
    }

    public boolean isWebpage() {
        return getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage;
    }

    public boolean isWebpageDocument() {
        return (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) || getMedia(this.messageOwner).webpage.document == null || isGifDocument(getMedia(this.messageOwner).webpage.document)) ? false : true;
    }

    public boolean isYouTubeVideo() {
        return (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && !TextUtils.isEmpty(getMedia(this.messageOwner).webpage.embed_url) && "YouTube".equals(getMedia(this.messageOwner).webpage.site_name);
    }

    public void loadAnimatedEmojiDocument() {
        if (this.emojiAnimatedSticker != null || this.emojiAnimatedStickerId == null || this.emojiAnimatedStickerLoading) {
            return;
        }
        this.emojiAnimatedStickerLoading = true;
        AnimatedEmojiDrawable.getDocumentFetcher(this.currentAccount).fetchDocument(this.emojiAnimatedStickerId.longValue(), new AnimatedEmojiDrawable.ReceivedDocument() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.ReceivedDocument
            public final void run(TLRPC.Document document) {
                MessageObject.this.lambda$loadAnimatedEmojiDocument$1(document);
            }
        });
    }

    public void markReactionsAsRead() {
        TLRPC.TL_messageReactions tL_messageReactions = this.messageOwner.reactions;
        if (tL_messageReactions == null || tL_messageReactions.recent_reactions == null) {
            return;
        }
        boolean z = false;
        for (int i = 0; i < this.messageOwner.reactions.recent_reactions.size(); i++) {
            if (this.messageOwner.reactions.recent_reactions.get(i).unread) {
                this.messageOwner.reactions.recent_reactions.get(i).unread = false;
                z = true;
            }
        }
        if (z) {
            MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            TLRPC.Message message = this.messageOwner;
            messagesStorage.markMessageReactionsAsRead(message.dialog_id, getTopicId(this.currentAccount, message), this.messageOwner.id, true);
        }
    }

    public void measureInlineBotButtons() {
        TLRPC.TL_messageReactions tL_messageReactions;
        CharSequence replaceEmoji;
        if (this.isRestrictedMessage) {
            return;
        }
        this.wantedBotKeyboardWidth = 0;
        if (((this.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && !hasExtendedMedia()) || ((tL_messageReactions = this.messageOwner.reactions) != null && !tL_messageReactions.results.isEmpty())) {
            Theme.createCommonMessageResources();
            StringBuilder sb = this.botButtonsLayout;
            if (sb == null) {
                this.botButtonsLayout = new StringBuilder();
            } else {
                sb.setLength(0);
            }
        }
        if (!(this.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) || hasExtendedMedia() || this.messageOwner.reply_markup.rows == null) {
            return;
        }
        for (int i = 0; i < this.messageOwner.reply_markup.rows.size(); i++) {
            TLRPC.TL_keyboardButtonRow tL_keyboardButtonRow = this.messageOwner.reply_markup.rows.get(i);
            int size = tL_keyboardButtonRow.buttons.size();
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC.KeyboardButton keyboardButton = tL_keyboardButtonRow.buttons.get(i3);
                StringBuilder sb2 = this.botButtonsLayout;
                sb2.append(i);
                sb2.append(i3);
                if (!(keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) || (getMedia(this.messageOwner).flags & 4) == 0) {
                    String str = keyboardButton.text;
                    if (str == null) {
                        str = "";
                    }
                    replaceEmoji = Emoji.replaceEmoji((CharSequence) str, Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false);
                } else {
                    replaceEmoji = LocaleController.getString(R.string.PaymentReceipt);
                }
                StaticLayout staticLayout = new StaticLayout(replaceEmoji, Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(2000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                if (staticLayout.getLineCount() > 0) {
                    float lineWidth = staticLayout.getLineWidth(0);
                    float lineLeft = staticLayout.getLineLeft(0);
                    if (lineLeft < lineWidth) {
                        lineWidth -= lineLeft;
                    }
                    i2 = Math.max(i2, ((int) Math.ceil(lineWidth)) + AndroidUtilities.dp(4.0f));
                }
            }
            this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, ((i2 + AndroidUtilities.dp(12.0f)) * size) + (AndroidUtilities.dp(5.0f) * (size - 1)));
        }
    }

    public float measureVoiceTranscriptionHeight() {
        StaticLayout staticLayout;
        StaticLayout.Builder obtain;
        StaticLayout.Builder breakStrategy;
        StaticLayout.Builder hyphenationFrequency;
        StaticLayout.Builder alignment;
        CharSequence voiceTranscription = getVoiceTranscription();
        if (voiceTranscription == null) {
            return 0.0f;
        }
        int dp = AndroidUtilities.displaySize.x - AndroidUtilities.dp(needDrawAvatar() ? 147.0f : 95.0f);
        if (Build.VERSION.SDK_INT >= 24) {
            obtain = StaticLayout.Builder.obtain(voiceTranscription, 0, voiceTranscription.length(), Theme.chat_msgTextPaint, dp);
            breakStrategy = obtain.setBreakStrategy(1);
            hyphenationFrequency = breakStrategy.setHyphenationFrequency(0);
            alignment = hyphenationFrequency.setAlignment(Layout.Alignment.ALIGN_NORMAL);
            staticLayout = alignment.build();
        } else {
            staticLayout = new StaticLayout(voiceTranscription, Theme.chat_msgTextPaint, dp, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        return staticLayout.getHeight();
    }

    public boolean mediaExists() {
        VideoPlayer.VideoUri videoUri;
        return (!hasVideoQualities() || (videoUri = this.highestQuality) == null) ? this.mediaExists : videoUri.isCached();
    }

    public boolean needDrawAvatar() {
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Chat chat;
        if (this.type == 27) {
            return false;
        }
        if (this.isRepostPreview || this.isSaved || this.forceAvatar || this.customAvatarDrawable != null || this.searchType != 0) {
            return true;
        }
        boolean z = getDialogId() >= 0 ? getDialogId() == UserObject.VERIFY : !((chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()))) == null || !chat.signature_profiles);
        if (isSponsored()) {
            return false;
        }
        return isFromUser() || isFromGroup() || z || this.eventId != 0 || !((messageFwdHeader = this.messageOwner.fwd_from) == null || messageFwdHeader.saved_from_peer == null);
    }

    public boolean needDrawBluredPreview() {
        if (this.isRepostPreview) {
            return false;
        }
        if (hasExtendedMediaPreview()) {
            return true;
        }
        TLRPC.Message message = this.messageOwner;
        if (message instanceof TLRPC.TL_message_secret) {
            int max = Math.max(message.ttl, getMedia(message).ttl_seconds);
            if (max > 0) {
                return (((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto) || isVideo() || isGif()) && max <= 60) || isRoundVideo();
            }
            return false;
        }
        if (!(message instanceof TLRPC.TL_message) || getMedia(message) == null || getMedia(this.messageOwner).ttl_seconds == 0) {
            return false;
        }
        return (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument);
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x009a, code lost:
    
        if (r1.channel_id == r0.channel_id) goto L54;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean needDrawForwarded() {
        TLRPC.MessageFwdHeader messageFwdHeader;
        if (this.type == 23 && !isExpiredStory()) {
            return true;
        }
        if (getDialogId() == UserObject.VERIFY) {
            return false;
        }
        if (this.isSaved) {
            TLRPC.Message message = this.messageOwner;
            if (message == null || message.fwd_from == null) {
                return false;
            }
            long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            long savedDialogId = getSavedDialogId(clientUserId, this.messageOwner);
            long peerDialogId = DialogObject.getPeerDialogId(this.messageOwner.fwd_from.saved_from_peer);
            if (peerDialogId >= 0) {
                peerDialogId = DialogObject.getPeerDialogId(this.messageOwner.fwd_from.from_id);
            }
            return peerDialogId == 0 ? savedDialogId >= 0 && savedDialogId != UserObject.ANONYMOUS : (savedDialogId == peerDialogId || peerDialogId == clientUserId) ? false : true;
        }
        if (((this.messageOwner.flags & 4) != 0 || (getMedia(this) instanceof TLRPC.TL_messageMediaPaidMedia)) && (messageFwdHeader = this.messageOwner.fwd_from) != null && !messageFwdHeader.imported) {
            TLRPC.Peer peer = messageFwdHeader.saved_from_peer;
            if (peer != null) {
                TLRPC.Peer peer2 = messageFwdHeader.from_id;
                if (peer2 instanceof TLRPC.TL_peerChannel) {
                }
            }
            if (UserConfig.getInstance(this.currentAccount).getClientUserId() != getDialogId()) {
                return true;
            }
        }
        return false;
    }

    public boolean needDrawShareButton() {
        int i;
        TLRPC.Message message;
        TLRPC.MessageFwdHeader messageFwdHeader;
        if (this.isRepostPreview || getDialogId() == UserObject.VERIFY) {
            return false;
        }
        if (this.isSaved) {
            long j = UserConfig.getInstance(this.currentAccount).clientUserId;
            long savedDialogId = getSavedDialogId(j, this.messageOwner);
            if (savedDialogId == j || savedDialogId == UserObject.ANONYMOUS || (message = this.messageOwner) == null || (messageFwdHeader = message.fwd_from) == null) {
                return false;
            }
            return (messageFwdHeader.from_id == null && messageFwdHeader.saved_from_id == null) ? false : true;
        }
        if (this.type == 27 || isSponsored() || this.hasCode || this.preview || this.scheduled || this.eventId != 0) {
            return false;
        }
        if (this.searchType == 2) {
            return true;
        }
        TLRPC.Message message2 = this.messageOwner;
        if (message2.noforwards) {
            return false;
        }
        if (message2.fwd_from != null && !isOutOwner() && this.messageOwner.fwd_from.saved_from_peer != null && getDialogId() == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            return true;
        }
        int i2 = this.type;
        if (i2 != 13 && i2 != 15 && i2 != 19) {
            TLRPC.MessageFwdHeader messageFwdHeader2 = this.messageOwner.fwd_from;
            if (messageFwdHeader2 != null && (messageFwdHeader2.from_id instanceof TLRPC.TL_peerChannel) && !isOutOwner()) {
                return true;
            }
            if (isFromUser()) {
                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
                if (user != null && user.bot && ("reviews_bot".equals(UserObject.getPublicUsername(user)) || "ReviewInsightsBot".equals(UserObject.getPublicUsername(user)))) {
                    return true;
                }
                if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaEmpty) || getMedia(this.messageOwner) == null || ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && !(getMedia(this.messageOwner).webpage instanceof TLRPC.TL_webPage))) {
                    return false;
                }
                if (user != null && user.bot && !hasExtendedMedia()) {
                    return true;
                }
                if (!isOut()) {
                    if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame) || (((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaInvoice) && !hasExtendedMedia()) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage))) {
                        return true;
                    }
                    TLRPC.Peer peer = this.messageOwner.peer_id;
                    TLRPC.Chat chat = null;
                    if (peer != null) {
                        long j2 = peer.channel_id;
                        if (j2 != 0) {
                            chat = getChat(null, null, j2);
                        }
                    }
                    return ChatObject.isChannel(chat) && chat.megagroup && ChatObject.isPublic(chat) && !(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaContact) && !(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGeo);
                }
            } else {
                TLRPC.Message message3 = this.messageOwner;
                if ((message3.from_id instanceof TLRPC.TL_peerChannel) || message3.post) {
                    if ((getMedia(message3) instanceof TLRPC.TL_messageMediaWebPage) && !isOutOwner()) {
                        return true;
                    }
                    if (isSupergroup()) {
                        return false;
                    }
                    TLRPC.Message message4 = this.messageOwner;
                    if (message4.peer_id.channel_id != 0 && ((message4.via_bot_id == 0 && message4.reply_to == null) || ((i = this.type) != 13 && i != 15))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean probablyRingtone() {
        if (!isVoiceOnce() && getDocument() != null && RingtoneDataStore.ringtoneSupportedMimeType.contains(getDocument().mime_type) && getDocument().size < MessagesController.getInstance(this.currentAccount).ringtoneSizeMax * 2) {
            for (int i = 0; i < getDocument().attributes.size(); i++) {
                TLRPC.DocumentAttribute documentAttribute = getDocument().attributes.get(i);
                if ((documentAttribute instanceof TLRPC.TL_documentAttributeAudio) && documentAttribute.duration < 5.0d) {
                    return true;
                }
            }
        }
        return false;
    }

    public Spannable replaceAnimatedEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt) {
        TLRPC.TL_textWithEntities tL_textWithEntities;
        return replaceAnimatedEmoji(charSequence, (!this.translated || (tL_textWithEntities = this.messageOwner.translatedText) == null) ? this.messageOwner.entities : tL_textWithEntities.entities, fontMetricsInt, false);
    }

    public void replaceEmojiToLottieFrame(CharSequence charSequence, int[] iArr) {
        boolean z;
        if (charSequence instanceof Spannable) {
            Spannable spannable = (Spannable) charSequence;
            Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) spannable.getSpans(0, spannable.length(), Emoji.EmojiSpan.class);
            AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannable.getSpans(0, spannable.length(), AnimatedEmojiSpan.class);
            if (emojiSpanArr != null) {
                if (((iArr == null ? 0 : iArr[0]) - emojiSpanArr.length) - (animatedEmojiSpanArr == null ? 0 : animatedEmojiSpanArr.length) > 0) {
                    return;
                }
                for (int i = 0; i < emojiSpanArr.length; i++) {
                    CharSequence charSequence2 = emojiSpanArr[i].emoji;
                    if (Emoji.endsWithRightArrow(charSequence2)) {
                        charSequence2 = charSequence2.subSequence(0, charSequence2.length() - 2);
                        z = true;
                    } else {
                        z = false;
                    }
                    TLRPC.Document emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(charSequence2);
                    if (emojiAnimatedSticker != null) {
                        int spanStart = spannable.getSpanStart(emojiSpanArr[i]);
                        int spanEnd = spannable.getSpanEnd(emojiSpanArr[i]);
                        spannable.removeSpan(emojiSpanArr[i]);
                        AnimatedEmojiSpan animatedEmojiSpan = new AnimatedEmojiSpan(emojiAnimatedSticker, emojiSpanArr[i].fontMetrics);
                        animatedEmojiSpan.standard = true;
                        animatedEmojiSpan.invert = z;
                        spannable.setSpan(animatedEmojiSpan, spanStart, spanEnd, 33);
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0086 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CharSequence replaceWithLink(CharSequence charSequence, String str, ArrayList<Long> arrayList, AbstractMap<Long, TLRPC.User> abstractMap, LongSparseArray longSparseArray) {
        TLRPC.User user;
        Object obj;
        if (TextUtils.indexOf(charSequence, str) < 0) {
            return charSequence;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("");
        for (int i = 0; i < arrayList.size(); i++) {
            if (abstractMap != null) {
                obj = abstractMap.get(arrayList.get(i));
            } else if (longSparseArray != null) {
                obj = longSparseArray.get(arrayList.get(i).longValue());
            } else {
                user = null;
                if (user == null) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(arrayList.get(i));
                }
                if (user == null) {
                    String userName = UserObject.getUserName(user);
                    int length = spannableStringBuilder.length();
                    if (spannableStringBuilder.length() != 0) {
                        spannableStringBuilder.append((CharSequence) ", ");
                    }
                    spannableStringBuilder.append((CharSequence) userName);
                    spannableStringBuilder.setSpan(new URLSpanNoUnderlineBold("" + user.id), length, userName.length() + length, 33);
                }
            }
            user = (TLRPC.User) obj;
            if (user == null) {
            }
            if (user == null) {
            }
        }
        return TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{spannableStringBuilder});
    }

    public void resetLayout() {
        this.layoutCreated = false;
    }

    public void resetPlayingProgress() {
        this.audioProgress = 0.0f;
        this.audioProgressSec = 0;
        this.bufferedProgress = 0.0f;
    }

    public boolean selectReaction(ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
        MessagesController messagesController;
        long dialogId;
        int i;
        TLRPC.TL_messageReactions tL_messageReactions;
        TLRPC.Message message = this.messageOwner;
        if (message.reactions == null) {
            message.reactions = new TLRPC.TL_messageReactions();
            TLRPC.Message message2 = this.messageOwner;
            message2.reactions.reactions_as_tags = getDialogId(message2) == UserConfig.getInstance(this.currentAccount).getClientUserId();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        ArrayList arrayList = new ArrayList();
        TLRPC.ReactionCount reactionCount = null;
        int i2 = 0;
        for (int i3 = 0; i3 < this.messageOwner.reactions.results.size(); i3++) {
            TLRPC.ReactionCount reactionCount2 = this.messageOwner.reactions.results.get(i3);
            if (reactionCount2.chosen && !(reactionCount2.reaction instanceof TLRPC.TL_reactionPaid)) {
                arrayList.add(reactionCount2);
                int i4 = reactionCount2.chosen_order;
                if (i4 > i2) {
                    i2 = i4;
                }
            }
            TLRPC.Reaction reaction = this.messageOwner.reactions.results.get(i3).reaction;
            if (reaction instanceof TLRPC.TL_reactionEmoji) {
                String str = visibleReaction.emojicon;
                if (str != null) {
                    if (((TLRPC.TL_reactionEmoji) reaction).emoticon.equals(str)) {
                        reactionCount = this.messageOwner.reactions.results.get(i3);
                    }
                }
            }
            if (reaction instanceof TLRPC.TL_reactionCustomEmoji) {
                long j = visibleReaction.documentId;
                if (j != 0 && ((TLRPC.TL_reactionCustomEmoji) reaction).document_id == j) {
                    reactionCount = this.messageOwner.reactions.results.get(i3);
                }
            }
        }
        if (!arrayList.isEmpty() && arrayList.contains(reactionCount) && z) {
            return true;
        }
        int maxUserReactionsCount = MessagesController.getInstance(this.currentAccount).getMaxUserReactionsCount();
        if (!arrayList.isEmpty() && arrayList.contains(reactionCount)) {
            if (reactionCount != null) {
                reactionCount.chosen = false;
                int i5 = reactionCount.count - 1;
                reactionCount.count = i5;
                if (i5 <= 0) {
                    this.messageOwner.reactions.results.remove(reactionCount);
                }
            }
            if (this.messageOwner.reactions.can_see_list) {
                int i6 = 0;
                while (i6 < this.messageOwner.reactions.recent_reactions.size()) {
                    if (getPeerId(this.messageOwner.reactions.recent_reactions.get(i6).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId() && ReactionsUtils.compare(this.messageOwner.reactions.recent_reactions.get(i6).reaction, visibleReaction)) {
                        this.messageOwner.reactions.recent_reactions.remove(i6);
                        i6--;
                    }
                    i6++;
                }
            }
            this.reactionsChanged = true;
            return false;
        }
        while (!arrayList.isEmpty() && arrayList.size() >= maxUserReactionsCount) {
            int i7 = 0;
            for (int i8 = 1; i8 < arrayList.size(); i8++) {
                if (!(((TLRPC.ReactionCount) arrayList.get(i8)).reaction instanceof TLRPC.TL_reactionPaid) && ((TLRPC.ReactionCount) arrayList.get(i8)).chosen_order < ((TLRPC.ReactionCount) arrayList.get(i7)).chosen_order) {
                    i7 = i8;
                }
            }
            TLRPC.ReactionCount reactionCount3 = (TLRPC.ReactionCount) arrayList.get(i7);
            reactionCount3.chosen = false;
            int i9 = reactionCount3.count - 1;
            reactionCount3.count = i9;
            if (i9 <= 0) {
                this.messageOwner.reactions.results.remove(reactionCount3);
            }
            arrayList.remove(reactionCount3);
            if (this.messageOwner.reactions.can_see_list) {
                int i10 = 0;
                while (i10 < this.messageOwner.reactions.recent_reactions.size()) {
                    if (getPeerId(this.messageOwner.reactions.recent_reactions.get(i10).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId() && ReactionsUtils.compare(this.messageOwner.reactions.recent_reactions.get(i10).reaction, visibleReaction)) {
                        this.messageOwner.reactions.recent_reactions.remove(i10);
                        i10--;
                    }
                    i10++;
                }
            }
        }
        if (reactionCount == null) {
            int chatMaxUniqReactions = MessagesController.getInstance(this.currentAccount).getChatMaxUniqReactions(getDialogId());
            TLRPC.Message message3 = this.messageOwner;
            if (message3 == null || (tL_messageReactions = message3.reactions) == null) {
                i = 0;
            } else {
                Iterator<TLRPC.ReactionCount> it = tL_messageReactions.results.iterator();
                i = 0;
                while (it.hasNext()) {
                    if (!(it.next().reaction instanceof TLRPC.TL_reactionPaid)) {
                        i++;
                    }
                }
            }
            if (i + 1 > chatMaxUniqReactions) {
                return false;
            }
            reactionCount = new TLRPC.TL_reactionCount();
            reactionCount.reaction = visibleReaction.toTLReaction();
            this.messageOwner.reactions.results.add(reactionCount);
        }
        reactionCount.chosen = true;
        reactionCount.count++;
        reactionCount.chosen_order = i2 + 1;
        TLRPC.Message message4 = this.messageOwner;
        if (message4.reactions.can_see_list || (message4.dialog_id > 0 && maxUserReactionsCount > 1)) {
            TLRPC.TL_messagePeerReaction tL_messagePeerReaction = new TLRPC.TL_messagePeerReaction();
            TLRPC.Message message5 = this.messageOwner;
            if (!message5.isThreadMessage || message5.fwd_from == null) {
                messagesController = MessagesController.getInstance(this.currentAccount);
                dialogId = getDialogId();
            } else {
                messagesController = MessagesController.getInstance(this.currentAccount);
                dialogId = getFromChatId();
            }
            tL_messagePeerReaction.peer_id = messagesController.getSendAsSelectedPeer(dialogId);
            this.messageOwner.reactions.recent_reactions.add(0, tL_messagePeerReaction);
            if (visibleReaction.emojicon != null) {
                TLRPC.TL_reactionEmoji tL_reactionEmoji = new TLRPC.TL_reactionEmoji();
                tL_messagePeerReaction.reaction = tL_reactionEmoji;
                tL_reactionEmoji.emoticon = visibleReaction.emojicon;
            } else {
                TLRPC.TL_reactionCustomEmoji tL_reactionCustomEmoji = new TLRPC.TL_reactionCustomEmoji();
                tL_messagePeerReaction.reaction = tL_reactionCustomEmoji;
                tL_reactionCustomEmoji.document_id = visibleReaction.documentId;
            }
        }
        this.reactionsChanged = true;
        return true;
    }

    public void setContentIsRead() {
        this.messageOwner.media_unread = false;
    }

    public void setIsRead() {
        this.messageOwner.unread = false;
    }

    public void setMyPaidReactionAnonymous(boolean z) {
        TLRPC.TL_messageReactions tL_messageReactions;
        ArrayList<TLRPC.MessageReactor> arrayList;
        TLRPC.Message message = this.messageOwner;
        if (message == null || (tL_messageReactions = message.reactions) == null || (arrayList = tL_messageReactions.top_reactors) == null) {
            return;
        }
        Iterator<TLRPC.MessageReactor> it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC.MessageReactor next = it.next();
            if (next != null && next.my) {
                next.anonymous = z;
            }
        }
    }

    public void setQuery(String str) {
        setQuery(str, true);
    }

    public void setQuery(String str, boolean z) {
        String str2;
        int indexOf;
        if (TextUtils.isEmpty(str)) {
            this.highlightedWords = null;
            this.messageTrimmedToHighlight = null;
            this.messageTrimmedToHighlightCut = true;
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        String lowerCase = str.trim().toLowerCase();
        String[] split = lowerCase.split("[^\\p{L}#$]+");
        ArrayList arrayList2 = new ArrayList();
        TLRPC.MessageReplyHeader messageReplyHeader = this.messageOwner.reply_to;
        if (messageReplyHeader != null && !TextUtils.isEmpty(messageReplyHeader.quote_text)) {
            String lowerCase2 = this.messageOwner.reply_to.quote_text.trim().toLowerCase();
            if (lowerCase2.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
                handleFoundWords(arrayList, split, true, z);
                return;
            }
            arrayList2.addAll(Arrays.asList(lowerCase2.split("[^\\p{L}#$]+")));
        }
        if (!TextUtils.isEmpty(this.messageOwner.message)) {
            String lowerCase3 = this.messageOwner.message.trim().toLowerCase();
            if (lowerCase3.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
                handleFoundWords(arrayList, split, false, z);
                return;
            }
            arrayList2.addAll(Arrays.asList(lowerCase3.split("[^\\p{L}#$]+")));
        }
        if (getDocument() != null) {
            String lowerCase4 = FileLoader.getDocumentFileName(getDocument()).toLowerCase();
            if (lowerCase4.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
            }
            arrayList2.addAll(Arrays.asList(lowerCase4.split("[^\\p{L}#$]+")));
        }
        if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC.TL_webPage)) {
            TLRPC.WebPage webPage = getMedia(this.messageOwner).webpage;
            String str3 = webPage.title;
            if (str3 == null) {
                str3 = webPage.site_name;
            }
            if (str3 != null) {
                String lowerCase5 = str3.toLowerCase();
                if (lowerCase5.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                    arrayList.add(lowerCase);
                }
                arrayList2.addAll(Arrays.asList(lowerCase5.split("[^\\p{L}#$]+")));
            }
        }
        String musicAuthor = getMusicAuthor();
        if (musicAuthor != null) {
            String lowerCase6 = musicAuthor.toLowerCase();
            if (lowerCase6.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
            }
            arrayList2.addAll(Arrays.asList(lowerCase6.split("[^\\p{L}#$]+")));
        }
        for (String str4 : split) {
            if (str4.length() >= 2) {
                for (int i = 0; i < arrayList2.size(); i++) {
                    if (!arrayList.contains(arrayList2.get(i)) && (indexOf = (str2 = (String) arrayList2.get(i)).indexOf(str4.charAt(0))) >= 0) {
                        int max = Math.max(str4.length(), str2.length());
                        if (indexOf != 0) {
                            str2 = str2.substring(indexOf);
                        }
                        int min = Math.min(str4.length(), str2.length());
                        int i2 = 0;
                        for (int i3 = 0; i3 < min && str2.charAt(i3) == str4.charAt(i3); i3++) {
                            i2++;
                        }
                        if (i2 / max >= 0.5d) {
                            arrayList.add((String) arrayList2.get(i));
                        }
                    }
                }
            }
        }
        handleFoundWords(arrayList, split, false, z);
    }

    /* JADX WARN: Code restructure failed: missing block: B:182:0x0324, code lost:
    
        if (isAnimatedSticker() != false) goto L208;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x036e, code lost:
    
        r5 = 15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0369, code lost:
    
        if (isSticker() != false) goto L207;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0126, code lost:
    
        if ((r3 instanceof org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL) == false) goto L75;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setType() {
        int i;
        int i2;
        TLObject tLObject;
        TLRPC.WallPaper wallPaper;
        int i3 = this.type;
        this.type = 1000;
        this.isRoundVideoCached = 0;
        if (!isSponsored()) {
            i = 1;
            if (this.channelJoined) {
                this.type = 27;
                this.channelJoinedExpanded = MessagesController.getInstance(this.currentAccount).getMainSettings().getBoolean("c" + getDialogId() + "_rec", true);
            } else {
                TLRPC.Message message = this.messageOwner;
                if (!(message instanceof TLRPC.TL_message) && !(message instanceof TLRPC.TL_messageForwarded_old2)) {
                    TLRPC.TL_channelAdminLogEvent tL_channelAdminLogEvent = this.currentEvent;
                    if (tL_channelAdminLogEvent != null) {
                        TLRPC.ChannelAdminLogEventAction channelAdminLogEventAction = tL_channelAdminLogEvent.action;
                        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeWallpaper) {
                            TLRPC.TL_channelAdminLogEventActionChangeWallpaper tL_channelAdminLogEventActionChangeWallpaper = (TLRPC.TL_channelAdminLogEventActionChangeWallpaper) channelAdminLogEventAction;
                            this.contentType = 1;
                            TLRPC.WallPaper wallPaper2 = tL_channelAdminLogEventActionChangeWallpaper.new_value;
                            if (!(wallPaper2 instanceof TLRPC.TL_wallPaperNoFile) || wallPaper2.id != 0 || wallPaper2.settings != null) {
                                this.type = 22;
                                ArrayList<TLRPC.PhotoSize> arrayList = new ArrayList<>();
                                this.photoThumbs = arrayList;
                                TLRPC.Document document = tL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                                if (document != null) {
                                    arrayList.addAll(document.thumbs);
                                    wallPaper = tL_channelAdminLogEventActionChangeWallpaper.new_value;
                                    tLObject = wallPaper.document;
                                    this.photoThumbsObject = tLObject;
                                }
                            }
                            i = 10;
                            this.type = i;
                        }
                    }
                    if (message instanceof TLRPC.TL_messageService) {
                        TLRPC.MessageAction messageAction = message.action;
                        if (!(messageAction instanceof TLRPC.TL_messageActionSetSameChatWallPaper)) {
                            if (messageAction instanceof TLRPC.TL_messageActionSetChatWallPaper) {
                                this.contentType = 1;
                                this.type = 22;
                                TLRPC.TL_messageActionSetChatWallPaper tL_messageActionSetChatWallPaper = (TLRPC.TL_messageActionSetChatWallPaper) messageAction;
                                ArrayList<TLRPC.PhotoSize> arrayList2 = new ArrayList<>();
                                this.photoThumbs = arrayList2;
                                TLRPC.Document document2 = tL_messageActionSetChatWallPaper.wallpaper.document;
                                if (document2 != null) {
                                    arrayList2.addAll(document2.thumbs);
                                    wallPaper = tL_messageActionSetChatWallPaper.wallpaper;
                                    tLObject = wallPaper.document;
                                    this.photoThumbsObject = tLObject;
                                }
                            } else if (messageAction instanceof TLRPC.TL_messageActionSuggestProfilePhoto) {
                                this.contentType = 1;
                                this.type = 21;
                                ArrayList<TLRPC.PhotoSize> arrayList3 = new ArrayList<>();
                                this.photoThumbs = arrayList3;
                                arrayList3.addAll(this.messageOwner.action.photo.sizes);
                                tLObject = this.messageOwner.action.photo;
                                this.photoThumbsObject = tLObject;
                            } else if (!(messageAction instanceof TLRPC.TL_messageActionLoginUnknownLocation)) {
                                boolean z = messageAction instanceof TLRPC.TL_messageActionGiftCode;
                                if (z && ((TLRPC.TL_messageActionGiftCode) messageAction).boost_peer != null) {
                                    this.contentType = 1;
                                    i = 25;
                                } else if ((messageAction instanceof TLRPC.TL_messageActionGiftPremium) || z) {
                                    this.contentType = 1;
                                    i = 18;
                                } else if ((messageAction instanceof TLRPC.TL_messageActionGiftStars) || (messageAction instanceof TLRPC.TL_messageActionStarGift) || (messageAction instanceof TLRPC.TL_messageActionPrizeStars)) {
                                    this.contentType = 1;
                                    i = 30;
                                } else if ((messageAction instanceof TLRPC.TL_messageActionChatEditPhoto) || (messageAction instanceof TLRPC.TL_messageActionUserUpdatedPhoto)) {
                                    this.contentType = 1;
                                    i = 11;
                                } else if (messageAction instanceof TLRPC.TL_messageEncryptedAction) {
                                    TLRPC.DecryptedMessageAction decryptedMessageAction = messageAction.encryptedAction;
                                    if (!(decryptedMessageAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages)) {
                                    }
                                } else {
                                    if (!(messageAction instanceof TLRPC.TL_messageActionHistoryClear)) {
                                        if (messageAction instanceof TLRPC.TL_messageActionPhoneCall) {
                                            i = 16;
                                        }
                                    }
                                    i = -1;
                                    this.contentType = -1;
                                }
                                this.type = i;
                            }
                        }
                        this.contentType = 1;
                        i = 10;
                        this.type = i;
                    }
                } else if (!this.isRestrictedMessage) {
                    if (this.emojiAnimatedSticker == null && this.emojiAnimatedStickerId == null) {
                        if (message.media instanceof TLRPC.TL_messageMediaPaidMedia) {
                            i = 29;
                        } else if (isMediaEmpty(false) && !isDice() && !isSponsored() && this.emojiOnlyCount >= 1 && !this.hasUnwrappedEmoji && this.messageOwner != null && !hasNonEmojiEntities()) {
                            i = 19;
                        } else if (isMediaEmpty()) {
                            this.type = 0;
                            if (TextUtils.isEmpty(this.messageText) && this.eventId == 0) {
                                this.messageText = "";
                            }
                        } else if (hasExtendedMediaPreview()) {
                            i = 20;
                        } else {
                            if (getMedia(this.messageOwner).ttl_seconds == 0 || (!(getMedia(this.messageOwner).photo instanceof TLRPC.TL_photoEmpty) && !(getDocument() instanceof TLRPC.TL_documentEmpty) && ((!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument) || getDocument() != null) && !this.forceExpired))) {
                                if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGiveaway) {
                                    i = 26;
                                } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGiveawayResults) {
                                    i = 28;
                                } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDice) {
                                    this.type = 15;
                                    if (getMedia(this.messageOwner).document == null) {
                                        getMedia(this.messageOwner).document = new TLRPC.TL_document();
                                        getMedia(this.messageOwner).document.file_reference = new byte[0];
                                        getMedia(this.messageOwner).document.mime_type = "application/x-tgsdice";
                                        getMedia(this.messageOwner).document.dc_id = Integer.MIN_VALUE;
                                        getMedia(this.messageOwner).document.id = -2147483648L;
                                        TLRPC.TL_documentAttributeImageSize tL_documentAttributeImageSize = new TLRPC.TL_documentAttributeImageSize();
                                        tL_documentAttributeImageSize.w = 512;
                                        tL_documentAttributeImageSize.h = 512;
                                        getMedia(this.messageOwner).document.attributes.add(tL_documentAttributeImageSize);
                                    }
                                } else if (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPhoto)) {
                                    if ((getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGeo) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaVenue) || (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGeoLive)) {
                                        i = 4;
                                    } else if (isRoundVideo()) {
                                        i = 5;
                                    } else if (isVideo()) {
                                        i = 3;
                                    } else if (isVoice()) {
                                        i = 2;
                                    } else if (isMusic()) {
                                        i = 14;
                                    } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaContact) {
                                        i = 12;
                                    } else if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaPoll) {
                                        this.type = 17;
                                        this.checkedVotes = new ArrayList<>();
                                    } else if (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaUnsupported)) {
                                        if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaDocument) {
                                            TLRPC.Document document3 = getDocument();
                                            if (document3 != null && document3.mime_type != null) {
                                                if (isGifDocument(document3, hasValidGroupId())) {
                                                    i = 8;
                                                } else {
                                                    if (!isSticker()) {
                                                    }
                                                    i = 13;
                                                }
                                            }
                                            i = 9;
                                        } else if (!(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaGame) && !(getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaInvoice)) {
                                            if (getMedia(this.messageOwner) instanceof TLRPC.TL_messageMediaStory) {
                                                int i4 = getMedia(this.messageOwner).via_mention ? 24 : 23;
                                                this.type = i4;
                                                if (i4 == 24) {
                                                    this.contentType = 1;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            this.contentType = 1;
                            i = 10;
                        }
                    }
                    this.type = i;
                }
            }
            if (i3 != 1000 || i3 == (i2 = this.type) || i2 == 19) {
                return;
            }
            updateMessageText(MessagesController.getInstance(this.currentAccount).getUsers(), MessagesController.getInstance(this.currentAccount).getChats(), null, null);
            generateThumbs(false);
            return;
        }
        i = 0;
        this.type = i;
        if (i3 != 1000) {
        }
    }

    public boolean shouldAnimateSending() {
        return this.wasJustSent && (this.type == 5 || isVoice() || ((isAnyKindOfSticker() && this.sendAnimationData != null) || !(this.messageText == null || this.sendAnimationData == null)));
    }

    public boolean shouldDrawReactions() {
        return !this.isRepostPreview;
    }

    public boolean shouldDrawReactionsInLayout() {
        return true;
    }

    public boolean shouldDrawWithoutBackground() {
        int i;
        return !isSponsored() && ((i = this.type) == 13 || i == 15 || i == 5 || i == 19 || isExpiredStory());
    }

    public boolean shouldEncryptPhotoOrVideo() {
        return shouldEncryptPhotoOrVideo(this.currentAccount, this.messageOwner);
    }

    public void spoilLoginCode() {
        TLRPC.Message message;
        if (this.spoiledLoginCode || this.messageText == null || (message = this.messageOwner) == null || message.entities == null) {
            return;
        }
        TLRPC.Peer peer = message.from_id;
        if (peer instanceof TLRPC.TL_peerUser) {
            long j = peer.user_id;
            if (j == 777000 || j == UserObject.VERIFY) {
                if (loginCodePattern == null) {
                    loginCodePattern = Pattern.compile("[\\d\\-]{5,8}");
                }
                try {
                    Matcher matcher = loginCodePattern.matcher(this.messageText);
                    if (matcher.find()) {
                        TLRPC.TL_messageEntitySpoiler tL_messageEntitySpoiler = new TLRPC.TL_messageEntitySpoiler();
                        tL_messageEntitySpoiler.offset = matcher.start();
                        tL_messageEntitySpoiler.length = matcher.end() - tL_messageEntitySpoiler.offset;
                        this.messageOwner.entities.add(tL_messageEntitySpoiler);
                    }
                } catch (Exception e) {
                    FileLog.e((Throwable) e, false);
                }
                this.spoiledLoginCode = true;
            }
        }
    }

    public int textHeight() {
        if (this.textLayoutBlocks == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.textLayoutBlocks.size(); i2++) {
            i += this.textLayoutBlocks.get(i2).padTop + this.textLayoutBlocks.get(i2).height() + this.textLayoutBlocks.get(i2).padBottom;
        }
        return i;
    }

    public int textHeight(ChatMessageCell.TransitionParams transitionParams) {
        if (this.textLayoutBlocks == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.textLayoutBlocks.size(); i2++) {
            i += this.textLayoutBlocks.get(i2).padTop + this.textLayoutBlocks.get(i2).height(transitionParams) + this.textLayoutBlocks.get(i2).padBottom;
        }
        return i;
    }

    public void toggleChannelRecommendations() {
        expandChannelRecommendations(!this.channelJoinedExpanded);
    }

    public void updateMessageText() {
        updateMessageText(MessagesController.getInstance(this.currentAccount).getUsers(), MessagesController.getInstance(this.currentAccount).getChats(), null, null);
    }

    public void updateQualitiesCached(boolean z) {
        ArrayList<VideoPlayer.Quality> arrayList = this.videoQualities;
        if (arrayList == null) {
            return;
        }
        Iterator<VideoPlayer.Quality> it = arrayList.iterator();
        while (it.hasNext()) {
            Iterator it2 = it.next().uris.iterator();
            while (it2.hasNext()) {
                ((VideoPlayer.VideoUri) it2.next()).updateCached(z);
            }
        }
    }

    public boolean updateTranslation() {
        return updateTranslation(false);
    }

    public boolean updateTranslation(boolean z) {
        String str;
        TLRPC.Message message;
        MessageObject messageObject = this.replyMessageObject;
        boolean z2 = (messageObject == null || messageObject == this || !messageObject.updateTranslation(z)) ? false : true;
        TranslateController translateController = MessagesController.getInstance(this.currentAccount).getTranslateController();
        if (!TranslateController.isTranslatable(this) || !translateController.isTranslatingDialog(getDialogId()) || translateController.isTranslateDialogHidden(getDialogId()) || (message = this.messageOwner) == null || message.translatedText == null || !TextUtils.equals(translateController.getDialogTranslateTo(getDialogId()), this.messageOwner.translatedToLanguage)) {
            TLRPC.Message message2 = this.messageOwner;
            if (message2 == null || !(z || this.translated)) {
                return z2;
            }
            this.translated = false;
            str = message2.message;
        } else {
            if (this.translated) {
                return z2;
            }
            this.translated = true;
            str = this.messageOwner.translatedText.text;
        }
        applyNewText(str);
        generateCaption();
        return true;
    }
}
