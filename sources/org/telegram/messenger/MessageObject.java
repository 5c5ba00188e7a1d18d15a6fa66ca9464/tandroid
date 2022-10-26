package org.telegram.messenger;

import android.graphics.Paint;
import android.graphics.Point;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.ringtone.RingtoneDataStore;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$ChannelAdminLogEventAction;
import org.telegram.tgnet.TLRPC$ChannelLocation;
import org.telegram.tgnet.TLRPC$ChannelParticipant;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatInvite;
import org.telegram.tgnet.TLRPC$ChatReactions;
import org.telegram.tgnet.TLRPC$DecryptedMessageAction;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$ExportedChatInvite;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$ForumTopic;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageExtendedMedia;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$MessagePeerReaction;
import org.telegram.tgnet.TLRPC$MessageReplies;
import org.telegram.tgnet.TLRPC$Page;
import org.telegram.tgnet.TLRPC$PageBlock;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$PollResults;
import org.telegram.tgnet.TLRPC$Reaction;
import org.telegram.tgnet.TLRPC$ReactionCount;
import org.telegram.tgnet.TLRPC$SecureValueType;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEvent;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeAbout;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeLinkedChat;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeLocation;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangePhoto;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeStickerSet;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeTheme;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeTitle;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeUsername;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeUsernames;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionCreateTopic;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionDefaultBannedRights;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionDeleteMessage;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionDeleteTopic;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionDiscardGroupCall;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionEditMessage;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionEditTopic;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionExportedInviteDelete;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionExportedInviteEdit;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantInvite;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantJoin;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantLeave;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantMute;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantToggleBan;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantUnmute;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantVolume;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionPinTopic;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionSendMessage;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionStartGroupCall;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionStopPoll;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleForum;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleInvites;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleNoForwards;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleSignatures;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleSlowMode;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionUpdatePinned;
import org.telegram.tgnet.TLRPC$TL_channelLocation;
import org.telegram.tgnet.TLRPC$TL_channelLocationEmpty;
import org.telegram.tgnet.TLRPC$TL_channelParticipant;
import org.telegram.tgnet.TLRPC$TL_channelParticipantAdmin;
import org.telegram.tgnet.TLRPC$TL_channelParticipantCreator;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_chatInvitePublicJoinRequests;
import org.telegram.tgnet.TLRPC$TL_chatReactionsAll;
import org.telegram.tgnet.TLRPC$TL_chatReactionsSome;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionScreenshotMessages;
import org.telegram.tgnet.TLRPC$TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeCustomEmoji;
import org.telegram.tgnet.TLRPC$TL_documentAttributeHasStickers;
import org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_documentEncrypted;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_game;
import org.telegram.tgnet.TLRPC$TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageActionBotAllowed;
import org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate;
import org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom;
import org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser;
import org.telegram.tgnet.TLRPC$TL_messageActionChatCreate;
import org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto;
import org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser;
import org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto;
import org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle;
import org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink;
import org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest;
import org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo;
import org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp;
import org.telegram.tgnet.TLRPC$TL_messageActionCreatedBroadcastList;
import org.telegram.tgnet.TLRPC$TL_messageActionCustomAction;
import org.telegram.tgnet.TLRPC$TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC$TL_messageActionGameScore;
import org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached;
import org.telegram.tgnet.TLRPC$TL_messageActionGiftPremium;
import org.telegram.tgnet.TLRPC$TL_messageActionGroupCall;
import org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled;
import org.telegram.tgnet.TLRPC$TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall;
import org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation;
import org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC$TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken;
import org.telegram.tgnet.TLRPC$TL_messageActionSecureValuesSent;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme;
import org.telegram.tgnet.TLRPC$TL_messageActionSetMessagesTTL;
import org.telegram.tgnet.TLRPC$TL_messageActionTTLChange;
import org.telegram.tgnet.TLRPC$TL_messageActionTopicCreate;
import org.telegram.tgnet.TLRPC$TL_messageActionTopicEdit;
import org.telegram.tgnet.TLRPC$TL_messageActionUserJoined;
import org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC$TL_messageActionWebViewDataSent;
import org.telegram.tgnet.TLRPC$TL_messageEmpty;
import org.telegram.tgnet.TLRPC$TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC$TL_messageEntityBankCard;
import org.telegram.tgnet.TLRPC$TL_messageEntityBlockquote;
import org.telegram.tgnet.TLRPC$TL_messageEntityBold;
import org.telegram.tgnet.TLRPC$TL_messageEntityBotCommand;
import org.telegram.tgnet.TLRPC$TL_messageEntityCashtag;
import org.telegram.tgnet.TLRPC$TL_messageEntityCode;
import org.telegram.tgnet.TLRPC$TL_messageEntityCustomEmoji;
import org.telegram.tgnet.TLRPC$TL_messageEntityEmail;
import org.telegram.tgnet.TLRPC$TL_messageEntityHashtag;
import org.telegram.tgnet.TLRPC$TL_messageEntityItalic;
import org.telegram.tgnet.TLRPC$TL_messageEntityMention;
import org.telegram.tgnet.TLRPC$TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_messageEntityPhone;
import org.telegram.tgnet.TLRPC$TL_messageEntityPre;
import org.telegram.tgnet.TLRPC$TL_messageEntitySpoiler;
import org.telegram.tgnet.TLRPC$TL_messageEntityStrike;
import org.telegram.tgnet.TLRPC$TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC$TL_messageEntityUnderline;
import org.telegram.tgnet.TLRPC$TL_messageEntityUrl;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMedia;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview;
import org.telegram.tgnet.TLRPC$TL_messageForwarded_old;
import org.telegram.tgnet.TLRPC$TL_messageForwarded_old2;
import org.telegram.tgnet.TLRPC$TL_messageMediaContact;
import org.telegram.tgnet.TLRPC$TL_messageMediaDice;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument_layer68;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument_layer74;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument_old;
import org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC$TL_messageMediaGame;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto_layer68;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto_layer74;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto_old;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_messagePeerReaction;
import org.telegram.tgnet.TLRPC$TL_messageReactions;
import org.telegram.tgnet.TLRPC$TL_messageReplyHeader;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_message_old;
import org.telegram.tgnet.TLRPC$TL_message_old2;
import org.telegram.tgnet.TLRPC$TL_message_old3;
import org.telegram.tgnet.TLRPC$TL_message_old4;
import org.telegram.tgnet.TLRPC$TL_message_secret;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_pageBlockCollage;
import org.telegram.tgnet.TLRPC$TL_pageBlockPhoto;
import org.telegram.tgnet.TLRPC$TL_pageBlockSlideshow;
import org.telegram.tgnet.TLRPC$TL_pageBlockVideo;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonBusy;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonMissed;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photoCachedSize;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC$TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC$TL_pollAnswer;
import org.telegram.tgnet.TLRPC$TL_pollAnswerVoters;
import org.telegram.tgnet.TLRPC$TL_reactionCount;
import org.telegram.tgnet.TLRPC$TL_reactionCustomEmoji;
import org.telegram.tgnet.TLRPC$TL_reactionEmoji;
import org.telegram.tgnet.TLRPC$TL_replyInlineMarkup;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeAddress;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeBankStatement;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeDriverLicense;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeEmail;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeIdentityCard;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeInternalPassport;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePassport;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePassportRegistration;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePersonalDetails;
import org.telegram.tgnet.TLRPC$TL_secureValueTypePhone;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeRentalAgreement;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeTemporaryRegistration;
import org.telegram.tgnet.TLRPC$TL_secureValueTypeUtilityBill;
import org.telegram.tgnet.TLRPC$TL_stickerSetFullCovered;
import org.telegram.tgnet.TLRPC$TL_webPage;
import org.telegram.tgnet.TLRPC$TL_webPageAttributeTheme;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WebDocument;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.Forum.ForumBubbleDrawable;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.Reactions.ReactionsUtils;
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
import org.telegram.ui.Components.spoilers.SpoilerEffect;
/* loaded from: classes.dex */
public class MessageObject {
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
    public static final int TYPE_ANIMATED_STICKER = 15;
    public static final int TYPE_EMOJIS = 19;
    public static final int TYPE_EXTENDED_MEDIA_PREVIEW = 20;
    public static final int TYPE_GEO = 4;
    public static final int TYPE_GIFT_PREMIUM = 18;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_POLL = 17;
    public static final int TYPE_ROUND_VIDEO = 5;
    public static final int TYPE_STICKER = 13;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_VOICE = 2;
    static final String[] excludeWords = {" vs. ", " vs ", " versus ", " ft. ", " ft ", " featuring ", " feat. ", " feat ", " presents ", " pres. ", " pres ", " and ", " & ", " . "};
    public static Pattern instagramUrlPattern;
    public static Pattern urlPattern;
    public static Pattern videoTimeUrlPattern;
    public boolean animateComments;
    public boolean attachPathExists;
    public int audioPlayerDuration;
    public float audioProgress;
    public int audioProgressMs;
    public int audioProgressSec;
    public StringBuilder botButtonsLayout;
    public String botStartParam;
    public float bufferedProgress;
    public Boolean cachedIsSupergroup;
    public boolean cancelEditing;
    public CharSequence caption;
    public ArrayList<TLRPC$TL_pollAnswer> checkedVotes;
    public int contentType;
    public int currentAccount;
    public TLRPC$TL_channelAdminLogEvent currentEvent;
    public Drawable customAvatarDrawable;
    public String customName;
    public String customReplyName;
    public String dateKey;
    public boolean deleted;
    public CharSequence editingMessage;
    public ArrayList<TLRPC$MessageEntity> editingMessageEntities;
    public boolean editingMessageSearchWebPage;
    public TLRPC$Document emojiAnimatedSticker;
    public String emojiAnimatedStickerColor;
    public Long emojiAnimatedStickerId;
    private boolean emojiAnimatedStickerLoading;
    private int emojiOnlyCount;
    public long eventId;
    public long extendedMediaLastCheckTime;
    public boolean forcePlayEffect;
    public float forceSeekTo;
    public boolean forceUpdate;
    private float generatedWithDensity;
    private int generatedWithMinSize;
    public float gifState;
    public boolean hadAnimationNotReadyLoading;
    public boolean hasRtl;
    private boolean hasUnwrappedEmoji;
    public boolean hideSendersName;
    public ArrayList<String> highlightedWords;
    public boolean isDateObject;
    public boolean isDownloadingFile;
    public boolean isReactionPush;
    public boolean isRestrictedMessage;
    private int isRoundVideoCached;
    public boolean isSpoilersRevealed;
    public boolean isTopicMainMessage;
    public int lastLineWidth;
    private boolean layoutCreated;
    public int linesCount;
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
    public TLRPC$Message messageOwner;
    public CharSequence messageText;
    public CharSequence messageTextShort;
    public String messageTrimmedToHighlight;
    public String monthKey;
    public int parentWidth;
    public SvgHelper.SvgDrawable pathThumb;
    public ArrayList<TLRPC$PhotoSize> photoThumbs;
    public ArrayList<TLRPC$PhotoSize> photoThumbs2;
    public TLObject photoThumbsObject;
    public TLObject photoThumbsObject2;
    public boolean playedGiftAnimation;
    public long pollLastCheckTime;
    public boolean pollVisibleOnScreen;
    public boolean preview;
    public String previousAttachPath;
    public TLRPC$MessageMedia previousMedia;
    public String previousMessage;
    public ArrayList<TLRPC$MessageEntity> previousMessageEntities;
    public boolean putInDownloadsStore;
    private byte[] randomWaveform;
    public boolean reactionsChanged;
    public long reactionsLastCheckTime;
    public MessageObject replyMessageObject;
    public boolean resendAsIs;
    public boolean scheduled;
    public SendAnimationData sendAnimationData;
    public TLRPC$Peer sendAsPeer;
    public boolean shouldRemoveVideoEditedInfo;
    public int sponsoredChannelPost;
    public TLRPC$ChatInvite sponsoredChatInvite;
    public String sponsoredChatInviteHash;
    public byte[] sponsoredId;
    public boolean sponsoredRecommended;
    public boolean sponsoredShowPeerPhoto;
    public int stableId;
    public BitmapDrawable strippedThumb;
    public int textHeight;
    public ArrayList<TextLayoutBlock> textLayoutBlocks;
    public int textWidth;
    public float textXOffset;
    public ForumBubbleDrawable[] topicIconDrawable;
    private int totalAnimatedEmojiCount;
    public int type;
    public boolean useCustomPhoto;
    public CharSequence vCardData;
    public VideoEditedInfo videoEditedInfo;
    public boolean viewsReloaded;
    public int wantedBotKeyboardWidth;
    public boolean wasJustSent;
    public boolean wasUnread;
    public ArrayList<TLRPC$MessageEntity> webPageDescriptionEntities;
    public CharSequence youtubeDescription;

    /* loaded from: classes.dex */
    public static class SendAnimationData {
        public float currentScale;
        public float currentX;
        public float currentY;
        public float height;
        public float timeAlpha;
        public float width;
        public float x;
        public float y;
    }

    public void checkForScam() {
    }

    public static boolean hasUnreadReactions(TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message == null) {
            return false;
        }
        return hasUnreadReactions(tLRPC$Message.reactions);
    }

    public static boolean hasUnreadReactions(TLRPC$TL_messageReactions tLRPC$TL_messageReactions) {
        if (tLRPC$TL_messageReactions == null) {
            return false;
        }
        for (int i = 0; i < tLRPC$TL_messageReactions.recent_reactions.size(); i++) {
            if (tLRPC$TL_messageReactions.recent_reactions.get(i).unread) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPremiumSticker(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && tLRPC$Document.thumbs != null) {
            for (int i = 0; i < tLRPC$Document.video_thumbs.size(); i++) {
                if ("f".equals(tLRPC$Document.video_thumbs.get(i).type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getTopicId(TLRPC$Message tLRPC$Message) {
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader;
        if (tLRPC$Message == null || !(tLRPC$Message.action instanceof TLRPC$TL_messageActionTopicCreate)) {
            if (tLRPC$Message == null || (tLRPC$TL_messageReplyHeader = tLRPC$Message.reply_to) == null || !tLRPC$TL_messageReplyHeader.forum_topic) {
                return 0;
            }
            if ((tLRPC$Message instanceof TLRPC$TL_messageService) && !(tLRPC$Message.action instanceof TLRPC$TL_messageActionPinMessage)) {
                int i = tLRPC$TL_messageReplyHeader.reply_to_msg_id;
                return i == 0 ? tLRPC$TL_messageReplyHeader.reply_to_top_id : i;
            }
            int i2 = tLRPC$TL_messageReplyHeader.reply_to_top_id;
            return i2 == 0 ? tLRPC$TL_messageReplyHeader.reply_to_msg_id : i2;
        }
        return tLRPC$Message.id;
    }

    public static boolean isTopicActionMessage(MessageObject messageObject) {
        TLRPC$Message tLRPC$Message;
        if (messageObject == null || (tLRPC$Message = messageObject.messageOwner) == null) {
            return false;
        }
        TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
        return (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicCreate) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicEdit);
    }

    public int getEmojiOnlyCount() {
        return this.emojiOnlyCount;
    }

    public boolean shouldDrawReactionsInLayout() {
        if (getDialogId() < 0 || UserConfig.getInstance(this.currentAccount).isPremium()) {
            return true;
        }
        TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
        return user != null && user.premium;
    }

    public TLRPC$MessagePeerReaction getRandomUnreadReaction() {
        ArrayList<TLRPC$MessagePeerReaction> arrayList;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        if (tLRPC$TL_messageReactions == null || (arrayList = tLRPC$TL_messageReactions.recent_reactions) == null || arrayList.isEmpty()) {
            return null;
        }
        return this.messageOwner.reactions.recent_reactions.get(0);
    }

    public void markReactionsAsRead() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        if (tLRPC$TL_messageReactions == null || tLRPC$TL_messageReactions.recent_reactions == null) {
            return;
        }
        boolean z = false;
        for (int i = 0; i < this.messageOwner.reactions.recent_reactions.size(); i++) {
            if (this.messageOwner.reactions.recent_reactions.get(i).unread) {
                this.messageOwner.reactions.recent_reactions.get(i).unread = false;
                z = true;
            }
        }
        if (!z) {
            return;
        }
        MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        TLRPC$Message tLRPC$Message = this.messageOwner;
        messagesStorage.markMessageReactionsAsRead(tLRPC$Message.dialog_id, getTopicId(tLRPC$Message), this.messageOwner.id, true);
    }

    public boolean isPremiumSticker() {
        if (getMedia(this.messageOwner) == null || !getMedia(this.messageOwner).nopremium) {
            return isPremiumSticker(getDocument());
        }
        return false;
    }

    public TLRPC$VideoSize getPremiumStickerAnimation() {
        return getPremiumStickerAnimation(getDocument());
    }

    public static TLRPC$VideoSize getPremiumStickerAnimation(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && tLRPC$Document.thumbs != null) {
            for (int i = 0; i < tLRPC$Document.video_thumbs.size(); i++) {
                if ("f".equals(tLRPC$Document.video_thumbs.get(i).type)) {
                    return tLRPC$Document.video_thumbs.get(i);
                }
            }
        }
        return null;
    }

    public void copyStableParams(MessageObject messageObject) {
        ArrayList<TextLayoutBlock> arrayList;
        ArrayList<TLRPC$ReactionCount> arrayList2;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        this.stableId = messageObject.stableId;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        tLRPC$Message.premiumEffectWasPlayed = messageObject.messageOwner.premiumEffectWasPlayed;
        this.forcePlayEffect = messageObject.forcePlayEffect;
        this.wasJustSent = messageObject.wasJustSent;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions2 = tLRPC$Message.reactions;
        if (tLRPC$TL_messageReactions2 != null && (arrayList2 = tLRPC$TL_messageReactions2.results) != null && !arrayList2.isEmpty() && (tLRPC$TL_messageReactions = messageObject.messageOwner.reactions) != null && tLRPC$TL_messageReactions.results != null) {
            for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
                TLRPC$ReactionCount tLRPC$ReactionCount = this.messageOwner.reactions.results.get(i);
                for (int i2 = 0; i2 < messageObject.messageOwner.reactions.results.size(); i2++) {
                    TLRPC$ReactionCount tLRPC$ReactionCount2 = messageObject.messageOwner.reactions.results.get(i2);
                    if (ReactionsLayoutInBubble.equalsTLReaction(tLRPC$ReactionCount.reaction, tLRPC$ReactionCount2.reaction)) {
                        tLRPC$ReactionCount.lastDrawnPosition = tLRPC$ReactionCount2.lastDrawnPosition;
                    }
                }
            }
        }
        boolean z = messageObject.isSpoilersRevealed;
        this.isSpoilersRevealed = z;
        if (!z || (arrayList = this.textLayoutBlocks) == null) {
            return;
        }
        Iterator<TextLayoutBlock> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().spoilers.clear();
        }
    }

    public ArrayList<ReactionsLayoutInBubble.VisibleReaction> getChoosenReactions() {
        ArrayList<ReactionsLayoutInBubble.VisibleReaction> arrayList = new ArrayList<>();
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (this.messageOwner.reactions.results.get(i).chosen) {
                arrayList.add(ReactionsLayoutInBubble.VisibleReaction.fromTLReaction(this.messageOwner.reactions.results.get(i).reaction));
            }
        }
        return arrayList;
    }

    /* loaded from: classes.dex */
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
                    if (readLine != null) {
                        if (!readLine.startsWith("PHOTO")) {
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
                                int i = 2;
                                String[] strArr = indexOf >= 0 ? new String[]{readLine.substring(0, indexOf), readLine.substring(indexOf + 1).trim()} : new String[]{readLine.trim()};
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
                    } else {
                        try {
                            break;
                        } catch (Exception e) {
                            FileLog.e(e);
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
                        sb.append(PhoneFormat.getInstance().format(str6));
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

    /* loaded from: classes.dex */
    public static class TextLayoutBlock {
        public static final int FLAG_NOT_RTL = 2;
        public static final int FLAG_RTL = 1;
        public int charactersEnd;
        public int charactersOffset;
        public byte directionFlags;
        public int height;
        public int heightByOffset;
        public StaticLayout textLayout;
        public float textYOffset;
        public AtomicReference<Layout> spoilersPatchedTextLayout = new AtomicReference<>();
        public List<SpoilerEffect> spoilers = new ArrayList();

        public boolean isRtl() {
            byte b = this.directionFlags;
            return (b & 1) != 0 && (b & 2) == 0;
        }
    }

    /* loaded from: classes.dex */
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

    /* loaded from: classes.dex */
    public static class GroupedMessages {
        public long groupId;
        public boolean hasCaption;
        public boolean hasSibling;
        public boolean isDocuments;
        public ArrayList<MessageObject> messages = new ArrayList<>();
        public ArrayList<GroupedMessagePosition> posArray = new ArrayList<>();
        public HashMap<MessageObject, GroupedMessagePosition> positions = new HashMap<>();
        private int maxSizeWidth = 800;
        public final TransitionParams transitionParams = new TransitionParams();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class MessageGroupedLayoutAttempt {
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

        private float multiHeight(float[] fArr, int i, int i2) {
            float f = 0.0f;
            while (i < i2) {
                f += fArr[i];
                i++;
            }
            return this.maxSizeWidth / f;
        }

        /* JADX WARN: Code restructure failed: missing block: B:31:0x0072, code lost:
            if ((org.telegram.messenger.MessageObject.getMedia(r13.messageOwner) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaInvoice) == false) goto L32;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            int i;
            byte b;
            int i2;
            float f;
            int i3;
            int i4;
            float f2;
            int i5;
            this.posArray.clear();
            this.positions.clear();
            this.maxSizeWidth = 800;
            int size = this.messages.size();
            if (size <= 1) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            this.hasSibling = false;
            this.hasCaption = false;
            int i6 = 0;
            boolean z = false;
            float f3 = 1.0f;
            boolean z2 = false;
            boolean z3 = false;
            while (i6 < size) {
                MessageObject messageObject = this.messages.get(i6);
                if (i6 == 0) {
                    z3 = messageObject.isOutOwner();
                    if (!z3) {
                        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
                        if (tLRPC$MessageFwdHeader == null || tLRPC$MessageFwdHeader.saved_from_peer == null) {
                            if (tLRPC$Message.from_id instanceof TLRPC$TL_peerUser) {
                                TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
                                if (tLRPC$Peer.channel_id == 0) {
                                    if (tLRPC$Peer.chat_id == 0) {
                                        if (!(MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGame)) {
                                        }
                                    }
                                }
                            }
                        }
                        z = true;
                        if (!messageObject.isMusic() || messageObject.isDocument()) {
                            this.isDocuments = true;
                        }
                    }
                    z = false;
                    if (!messageObject.isMusic()) {
                    }
                    this.isDocuments = true;
                }
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                GroupedMessagePosition groupedMessagePosition = new GroupedMessagePosition();
                groupedMessagePosition.last = i6 == size + (-1);
                float f4 = closestPhotoSizeWithSize == null ? 1.0f : closestPhotoSizeWithSize.w / closestPhotoSizeWithSize.h;
                groupedMessagePosition.aspectRatio = f4;
                if (f4 > 1.2f) {
                    sb.append("w");
                } else if (f4 < 0.8f) {
                    sb.append("n");
                } else {
                    sb.append("q");
                }
                float f5 = groupedMessagePosition.aspectRatio;
                f3 += f5;
                if (f5 > 2.0f) {
                    z2 = true;
                }
                this.positions.put(messageObject, groupedMessagePosition);
                this.posArray.add(groupedMessagePosition);
                if (messageObject.caption != null) {
                    this.hasCaption = true;
                }
                i6++;
            }
            if (this.isDocuments) {
                for (int i7 = 0; i7 < size; i7++) {
                    GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(i7);
                    int i8 = groupedMessagePosition2.flags | 3;
                    groupedMessagePosition2.flags = i8;
                    if (i7 == 0) {
                        groupedMessagePosition2.flags = i8 | 4;
                    } else if (i7 == size - 1) {
                        groupedMessagePosition2.flags = i8 | 8;
                        groupedMessagePosition2.last = true;
                    }
                    groupedMessagePosition2.edge = true;
                    groupedMessagePosition2.aspectRatio = 1.0f;
                    groupedMessagePosition2.minX = (byte) 0;
                    groupedMessagePosition2.maxX = (byte) 0;
                    byte b2 = (byte) i7;
                    groupedMessagePosition2.minY = b2;
                    groupedMessagePosition2.maxY = b2;
                    groupedMessagePosition2.spanSize = 1000;
                    groupedMessagePosition2.pw = this.maxSizeWidth;
                    groupedMessagePosition2.ph = 100.0f;
                }
                return;
            }
            if (z) {
                this.maxSizeWidth -= 50;
                i = 250;
            } else {
                i = 200;
            }
            int dp = AndroidUtilities.dp(120.0f);
            Point point = AndroidUtilities.displaySize;
            int dp2 = (int) (AndroidUtilities.dp(120.0f) / (Math.min(point.x, point.y) / this.maxSizeWidth));
            Point point2 = AndroidUtilities.displaySize;
            int i9 = this.maxSizeWidth;
            int dp3 = (int) (AndroidUtilities.dp(40.0f) / (Math.min(point2.x, point2.y) / i9));
            float f6 = i9 / 814.0f;
            float f7 = f3 / size;
            float dp4 = AndroidUtilities.dp(100.0f) / 814.0f;
            if (z2 || !(size == 2 || size == 3 || size == 4)) {
                int size2 = this.posArray.size();
                float[] fArr = new float[size2];
                for (int i10 = 0; i10 < size; i10++) {
                    if (f7 > 1.1f) {
                        fArr[i10] = Math.max(1.0f, this.posArray.get(i10).aspectRatio);
                    } else {
                        fArr[i10] = Math.min(1.0f, this.posArray.get(i10).aspectRatio);
                    }
                    fArr[i10] = Math.max(0.66667f, Math.min(1.7f, fArr[i10]));
                }
                ArrayList arrayList = new ArrayList();
                for (int i11 = 1; i11 < size2; i11++) {
                    int i12 = size2 - i11;
                    if (i11 <= 3 && i12 <= 3) {
                        arrayList.add(new MessageGroupedLayoutAttempt(i11, i12, multiHeight(fArr, 0, i11), multiHeight(fArr, i11, size2)));
                    }
                }
                for (int i13 = 1; i13 < size2 - 1; i13++) {
                    int i14 = 1;
                    while (true) {
                        int i15 = size2 - i13;
                        if (i14 < i15) {
                            int i16 = i15 - i14;
                            if (i13 <= 3) {
                                if (i14 <= (f7 < 0.85f ? 4 : 3) && i16 <= 3) {
                                    int i17 = i13 + i14;
                                    arrayList.add(new MessageGroupedLayoutAttempt(i13, i14, i16, multiHeight(fArr, 0, i13), multiHeight(fArr, i13, i17), multiHeight(fArr, i17, size2)));
                                }
                            }
                            i14++;
                        }
                    }
                }
                for (int i18 = 1; i18 < size2 - 2; i18++) {
                    int i19 = 1;
                    while (true) {
                        int i20 = size2 - i18;
                        if (i19 < i20) {
                            int i21 = 1;
                            while (true) {
                                int i22 = i20 - i19;
                                if (i21 < i22) {
                                    int i23 = i22 - i21;
                                    if (i18 > 3 || i19 > 3 || i21 > 3 || i23 > 3) {
                                        i3 = i20;
                                    } else {
                                        int i24 = i18 + i19;
                                        i3 = i20;
                                        int i25 = i24 + i21;
                                        arrayList.add(new MessageGroupedLayoutAttempt(i18, i19, i21, i23, multiHeight(fArr, 0, i18), multiHeight(fArr, i18, i24), multiHeight(fArr, i24, i25), multiHeight(fArr, i25, size2)));
                                    }
                                    i21++;
                                    i20 = i3;
                                }
                            }
                            i19++;
                        }
                    }
                }
                float f8 = (this.maxSizeWidth / 3) * 4;
                int i26 = 0;
                MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                float f9 = 0.0f;
                while (i26 < arrayList.size()) {
                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = (MessageGroupedLayoutAttempt) arrayList.get(i26);
                    float f10 = Float.MAX_VALUE;
                    int i27 = 0;
                    float f11 = 0.0f;
                    while (true) {
                        float[] fArr2 = messageGroupedLayoutAttempt2.heights;
                        if (i27 >= fArr2.length) {
                            break;
                        }
                        f11 += fArr2[i27];
                        if (fArr2[i27] < f10) {
                            f10 = fArr2[i27];
                        }
                        i27++;
                    }
                    float abs = Math.abs(f11 - f8);
                    int[] iArr = messageGroupedLayoutAttempt2.lineCounts;
                    float f12 = f8;
                    ArrayList arrayList2 = arrayList;
                    if (iArr.length > 1) {
                        if (iArr[0] <= iArr[1]) {
                            if (iArr.length > 2 && iArr[1] > iArr[2]) {
                                f = 1.2f;
                                abs *= f;
                            } else if (iArr.length <= 3 || iArr[2] <= iArr[3]) {
                            }
                        }
                        f = 1.2f;
                        abs *= f;
                    }
                    if (f10 < dp2) {
                        abs *= 1.5f;
                    }
                    if (messageGroupedLayoutAttempt == null || abs < f9) {
                        f9 = abs;
                        messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                    }
                    i26++;
                    arrayList = arrayList2;
                    f8 = f12;
                }
                if (messageGroupedLayoutAttempt == null) {
                    return;
                }
                int i28 = 0;
                byte b3 = 0;
                int i29 = 0;
                while (true) {
                    int[] iArr2 = messageGroupedLayoutAttempt.lineCounts;
                    if (i28 >= iArr2.length) {
                        break;
                    }
                    int i30 = iArr2[i28];
                    float f13 = messageGroupedLayoutAttempt.heights[i28];
                    int i31 = this.maxSizeWidth;
                    int i32 = i30 - 1;
                    b3 = Math.max((int) b3, i32);
                    int i33 = i31;
                    GroupedMessagePosition groupedMessagePosition3 = null;
                    int i34 = i29;
                    int i35 = 0;
                    while (i35 < i30) {
                        float[] fArr3 = fArr;
                        int i36 = (int) (fArr[i34] * f13);
                        i33 -= i36;
                        int i37 = b3;
                        GroupedMessagePosition groupedMessagePosition4 = this.posArray.get(i34);
                        int i38 = i30;
                        int i39 = i28 == 0 ? 4 : 0;
                        if (i28 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                            i39 |= 8;
                        }
                        if (i35 == 0) {
                            i39 |= 1;
                            if (z3) {
                                groupedMessagePosition3 = groupedMessagePosition4;
                            }
                        }
                        if (i35 == i32) {
                            i39 |= 2;
                            if (!z3) {
                                i2 = i39;
                                groupedMessagePosition3 = groupedMessagePosition4;
                                groupedMessagePosition4.set(i35, i35, i28, i28, i36, Math.max(dp4, f13 / 814.0f), i2);
                                i34++;
                                i35++;
                                b3 = i37;
                                fArr = fArr3;
                                i30 = i38;
                            }
                        }
                        i2 = i39;
                        groupedMessagePosition4.set(i35, i35, i28, i28, i36, Math.max(dp4, f13 / 814.0f), i2);
                        i34++;
                        i35++;
                        b3 = i37;
                        fArr = fArr3;
                        i30 = i38;
                    }
                    groupedMessagePosition3.pw += i33;
                    groupedMessagePosition3.spanSize += i33;
                    i28++;
                    i29 = i34;
                    fArr = fArr;
                }
                b = b3;
            } else if (size == 2) {
                GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(0);
                GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(1);
                String sb2 = sb.toString();
                if (sb2.equals("ww")) {
                    i5 = dp2;
                    double d = f6;
                    Double.isNaN(d);
                    if (f7 > d * 1.4d) {
                        float f14 = groupedMessagePosition5.aspectRatio;
                        float f15 = groupedMessagePosition6.aspectRatio;
                        if (f14 - f15 < 0.2d) {
                            int i40 = this.maxSizeWidth;
                            float round = Math.round(Math.min(i40 / f14, Math.min(i40 / f15, 407.0f))) / 814.0f;
                            groupedMessagePosition5.set(0, 0, 0, 0, this.maxSizeWidth, round, 7);
                            groupedMessagePosition6.set(0, 0, 1, 1, this.maxSizeWidth, round, 11);
                            i4 = 0;
                            b = i4;
                        }
                    }
                } else {
                    i5 = dp2;
                }
                if (sb2.equals("ww") || sb2.equals("qq")) {
                    int i41 = this.maxSizeWidth / 2;
                    float f16 = i41;
                    float round2 = Math.round(Math.min(f16 / groupedMessagePosition5.aspectRatio, Math.min(f16 / groupedMessagePosition6.aspectRatio, 814.0f))) / 814.0f;
                    groupedMessagePosition5.set(0, 0, 0, 0, i41, round2, 13);
                    groupedMessagePosition6.set(1, 1, 0, 0, i41, round2, 14);
                    i4 = 1;
                    b = i4;
                } else {
                    int i42 = this.maxSizeWidth;
                    float f17 = groupedMessagePosition5.aspectRatio;
                    int max = (int) Math.max(i42 * 0.4f, Math.round((i42 / f17) / ((1.0f / f17) + (1.0f / groupedMessagePosition6.aspectRatio))));
                    int i43 = this.maxSizeWidth - max;
                    int i44 = i5;
                    if (i43 < i44) {
                        max -= i44 - i43;
                        i43 = i44;
                    }
                    float min = Math.min(814.0f, Math.round(Math.min(i43 / groupedMessagePosition5.aspectRatio, max / groupedMessagePosition6.aspectRatio))) / 814.0f;
                    groupedMessagePosition5.set(0, 0, 0, 0, i43, min, 13);
                    groupedMessagePosition6.set(1, 1, 0, 0, max, min, 14);
                    i4 = 1;
                    b = i4;
                }
            } else if (size == 3) {
                GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(0);
                GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(1);
                GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(2);
                if (sb.charAt(0) == 'n') {
                    float f18 = groupedMessagePosition8.aspectRatio;
                    float min2 = Math.min(407.0f, Math.round((this.maxSizeWidth * f18) / (groupedMessagePosition9.aspectRatio + f18)));
                    int max2 = (int) Math.max(dp2, Math.min(this.maxSizeWidth * 0.5f, Math.round(Math.min(groupedMessagePosition9.aspectRatio * min2, groupedMessagePosition8.aspectRatio * f2))));
                    int round3 = Math.round(Math.min((groupedMessagePosition7.aspectRatio * 814.0f) + dp3, this.maxSizeWidth - max2));
                    groupedMessagePosition7.set(0, 0, 0, 1, round3, 1.0f, 13);
                    float f19 = (814.0f - min2) / 814.0f;
                    groupedMessagePosition8.set(1, 1, 0, 0, max2, f19, 6);
                    float f20 = min2 / 814.0f;
                    groupedMessagePosition9.set(0, 1, 1, 1, max2, f20, 10);
                    int i45 = this.maxSizeWidth;
                    groupedMessagePosition9.spanSize = i45;
                    groupedMessagePosition7.siblingHeights = new float[]{f20, f19};
                    if (z3) {
                        groupedMessagePosition7.spanSize = i45 - max2;
                    } else {
                        groupedMessagePosition8.spanSize = i45 - round3;
                        groupedMessagePosition9.leftSpanOffset = round3;
                    }
                    this.hasSibling = true;
                } else {
                    float round4 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition7.aspectRatio, 537.24005f)) / 814.0f;
                    groupedMessagePosition7.set(0, 1, 0, 0, this.maxSizeWidth, round4, 7);
                    int i46 = this.maxSizeWidth / 2;
                    float f21 = i46;
                    float min3 = Math.min(814.0f - round4, Math.round(Math.min(f21 / groupedMessagePosition8.aspectRatio, f21 / groupedMessagePosition9.aspectRatio))) / 814.0f;
                    if (min3 < dp4) {
                        min3 = dp4;
                    }
                    float f22 = min3;
                    groupedMessagePosition8.set(0, 0, 1, 1, i46, f22, 9);
                    groupedMessagePosition9.set(1, 1, 1, 1, i46, f22, 10);
                }
                b = 1;
            } else {
                GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(0);
                GroupedMessagePosition groupedMessagePosition11 = this.posArray.get(1);
                GroupedMessagePosition groupedMessagePosition12 = this.posArray.get(2);
                GroupedMessagePosition groupedMessagePosition13 = this.posArray.get(3);
                if (sb.charAt(0) == 'w') {
                    float round5 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition10.aspectRatio, 537.24005f)) / 814.0f;
                    groupedMessagePosition10.set(0, 2, 0, 0, this.maxSizeWidth, round5, 7);
                    float round6 = Math.round(this.maxSizeWidth / ((groupedMessagePosition11.aspectRatio + groupedMessagePosition12.aspectRatio) + groupedMessagePosition13.aspectRatio));
                    float f23 = dp2;
                    int max3 = (int) Math.max(f23, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition11.aspectRatio * round6));
                    int max4 = (int) Math.max(Math.max(f23, this.maxSizeWidth * 0.33f), groupedMessagePosition13.aspectRatio * round6);
                    int i47 = (this.maxSizeWidth - max3) - max4;
                    if (i47 < AndroidUtilities.dp(58.0f)) {
                        int dp5 = AndroidUtilities.dp(58.0f) - i47;
                        i47 = AndroidUtilities.dp(58.0f);
                        int i48 = dp5 / 2;
                        max3 -= i48;
                        max4 -= dp5 - i48;
                    }
                    int i49 = max3;
                    float min4 = Math.min(814.0f - round5, round6) / 814.0f;
                    if (min4 < dp4) {
                        min4 = dp4;
                    }
                    float f24 = min4;
                    groupedMessagePosition11.set(0, 0, 1, 1, i49, f24, 9);
                    groupedMessagePosition12.set(1, 1, 1, 1, i47, f24, 8);
                    groupedMessagePosition13.set(2, 2, 1, 1, max4, f24, 10);
                    i4 = 2;
                    b = i4;
                } else {
                    int max5 = Math.max(dp2, Math.round(814.0f / (((1.0f / groupedMessagePosition11.aspectRatio) + (1.0f / groupedMessagePosition12.aspectRatio)) + (1.0f / groupedMessagePosition13.aspectRatio))));
                    float f25 = dp;
                    float f26 = max5;
                    float min5 = Math.min(0.33f, Math.max(f25, f26 / groupedMessagePosition11.aspectRatio) / 814.0f);
                    float min6 = Math.min(0.33f, Math.max(f25, f26 / groupedMessagePosition12.aspectRatio) / 814.0f);
                    float f27 = (1.0f - min5) - min6;
                    int round7 = Math.round(Math.min((814.0f * groupedMessagePosition10.aspectRatio) + dp3, this.maxSizeWidth - max5));
                    groupedMessagePosition10.set(0, 0, 0, 2, round7, min5 + min6 + f27, 13);
                    groupedMessagePosition11.set(1, 1, 0, 0, max5, min5, 6);
                    groupedMessagePosition12.set(0, 1, 1, 1, max5, min6, 2);
                    groupedMessagePosition12.spanSize = this.maxSizeWidth;
                    groupedMessagePosition13.set(0, 1, 2, 2, max5, f27, 10);
                    int i50 = this.maxSizeWidth;
                    groupedMessagePosition13.spanSize = i50;
                    if (z3) {
                        groupedMessagePosition10.spanSize = i50 - max5;
                    } else {
                        groupedMessagePosition11.spanSize = i50 - round7;
                        groupedMessagePosition12.leftSpanOffset = round7;
                        groupedMessagePosition13.leftSpanOffset = round7;
                    }
                    groupedMessagePosition10.siblingHeights = new float[]{min5, min6, f27};
                    this.hasSibling = true;
                    i4 = 1;
                    b = i4;
                }
            }
            for (int i51 = 0; i51 < size; i51++) {
                GroupedMessagePosition groupedMessagePosition14 = this.posArray.get(i51);
                if (z3) {
                    if (groupedMessagePosition14.minX == 0) {
                        groupedMessagePosition14.spanSize += i;
                    }
                    if ((groupedMessagePosition14.flags & 2) != 0) {
                        groupedMessagePosition14.edge = true;
                    }
                } else {
                    if (groupedMessagePosition14.maxX == b || (groupedMessagePosition14.flags & 2) != 0) {
                        groupedMessagePosition14.spanSize += i;
                    }
                    if ((groupedMessagePosition14.flags & 1) != 0) {
                        groupedMessagePosition14.edge = true;
                    }
                }
                MessageObject messageObject2 = this.messages.get(i51);
                if (!z3 && messageObject2.needDrawAvatarInternal()) {
                    if (groupedMessagePosition14.edge) {
                        int i52 = groupedMessagePosition14.spanSize;
                        if (i52 != 1000) {
                            groupedMessagePosition14.spanSize = i52 + 108;
                        }
                        groupedMessagePosition14.pw += 108;
                    } else {
                        if ((groupedMessagePosition14.flags & 2) != 0) {
                            int i53 = groupedMessagePosition14.spanSize;
                            if (i53 != 1000) {
                                groupedMessagePosition14.spanSize = i53 - 108;
                            } else {
                                int i54 = groupedMessagePosition14.leftSpanOffset;
                                if (i54 != 0) {
                                    groupedMessagePosition14.leftSpanOffset = i54 + 108;
                                }
                            }
                        }
                    }
                }
            }
        }

        public MessageObject findPrimaryMessageObject() {
            return findMessageWithFlags(5);
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

        /* loaded from: classes.dex */
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
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, String str, String str2, String str3, boolean z, boolean z2, boolean z3, boolean z4) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        int i2 = 1;
        this.topicIconDrawable = new ForumBubbleDrawable[1];
        this.localType = z ? 2 : i2;
        this.currentAccount = i;
        this.localName = str2;
        this.localUserName = str3;
        this.messageText = str;
        this.messageOwner = tLRPC$Message;
        this.localChannel = z2;
        this.localSupergroup = z3;
        this.localEdit = z4;
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, boolean z, boolean z2) {
        this(i, tLRPC$Message, abstractMap, (AbstractMap<Long, TLRPC$Chat>) null, z, z2);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, LongSparseArray<TLRPC$User> longSparseArray, boolean z, boolean z2) {
        this(i, tLRPC$Message, longSparseArray, (LongSparseArray<TLRPC$Chat>) null, z, z2);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, boolean z, boolean z2) {
        this(i, tLRPC$Message, null, null, null, null, null, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, boolean z, boolean z2) {
        this(i, tLRPC$Message, messageObject, null, null, null, null, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, boolean z, boolean z2) {
        this(i, tLRPC$Message, abstractMap, abstractMap2, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2, boolean z, boolean z2) {
        this(i, tLRPC$Message, null, null, null, longSparseArray, longSparseArray2, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, boolean z, boolean z2, long j) {
        this(i, tLRPC$Message, null, abstractMap, abstractMap2, null, null, z, z2, j);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2, boolean z, boolean z2, long j) {
        TextPaint textPaint;
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.topicIconDrawable = new ForumBubbleDrawable[1];
        Theme.createCommonMessageResources();
        this.currentAccount = i;
        this.messageOwner = tLRPC$Message;
        this.replyMessageObject = messageObject;
        this.eventId = j;
        this.wasUnread = !tLRPC$Message.out && tLRPC$Message.unread;
        TLRPC$Message tLRPC$Message2 = tLRPC$Message.replyMessage;
        if (tLRPC$Message2 != null) {
            this.replyMessageObject = new MessageObject(i, tLRPC$Message2, null, abstractMap, abstractMap2, longSparseArray, longSparseArray2, false, z2, j);
        }
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
        if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
            getUser(abstractMap, longSparseArray, tLRPC$Peer.user_id);
        }
        updateMessageText(abstractMap, abstractMap2, longSparseArray, longSparseArray2);
        setType();
        measureInlineBotButtons();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(this.messageOwner.date * 1000);
        int i2 = gregorianCalendar.get(6);
        int i3 = gregorianCalendar.get(1);
        int i4 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i2));
        this.monthKey = String.format("%d_%02d", Integer.valueOf(i3), Integer.valueOf(i4));
        createMessageSendInfo();
        generateCaption();
        if (z) {
            if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                textPaint = Theme.chat_msgGameTextPaint;
            } else {
                textPaint = Theme.chat_msgTextPaint;
            }
            int[] iArr = allowsBigEmoji() ? new int[1] : null;
            CharSequence replaceEmoji = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, iArr);
            this.messageText = replaceEmoji;
            Spannable replaceAnimatedEmoji = replaceAnimatedEmoji(replaceEmoji, this.messageOwner.entities, textPaint.getFontMetricsInt());
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

    private void checkBigAnimatedEmoji() {
        AnimatedEmojiSpan[] animatedEmojiSpanArr;
        int i;
        this.emojiAnimatedSticker = null;
        this.emojiAnimatedStickerId = null;
        if (this.emojiOnlyCount == 1 && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice) && ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaEmpty) || getMedia(this.messageOwner) == null)) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message.grouped_id == 0) {
                if (tLRPC$Message.entities.isEmpty()) {
                    String str = this.messageText;
                    int indexOf = TextUtils.indexOf(str, "🏻");
                    if (indexOf >= 0) {
                        this.emojiAnimatedStickerColor = "_c1";
                        str = str.subSequence(0, indexOf);
                    } else {
                        indexOf = TextUtils.indexOf(str, "🏼");
                        if (indexOf >= 0) {
                            this.emojiAnimatedStickerColor = "_c2";
                            str = str.subSequence(0, indexOf);
                        } else {
                            indexOf = TextUtils.indexOf(str, "🏽");
                            if (indexOf >= 0) {
                                this.emojiAnimatedStickerColor = "_c3";
                                str = str.subSequence(0, indexOf);
                            } else {
                                indexOf = TextUtils.indexOf(str, "🏾");
                                if (indexOf >= 0) {
                                    this.emojiAnimatedStickerColor = "_c4";
                                    str = str.subSequence(0, indexOf);
                                } else {
                                    indexOf = TextUtils.indexOf(str, "🏿");
                                    if (indexOf >= 0) {
                                        this.emojiAnimatedStickerColor = "_c5";
                                        str = str.subSequence(0, indexOf);
                                    } else {
                                        this.emojiAnimatedStickerColor = "";
                                    }
                                }
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) && (i = indexOf + 2) < this.messageText.length()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str.toString());
                        CharSequence charSequence = this.messageText;
                        sb.append(charSequence.subSequence(i, charSequence.length()).toString());
                        str = sb.toString();
                    }
                    if (TextUtils.isEmpty(this.emojiAnimatedStickerColor) || EmojiData.emojiColoredMap.contains(str.toString())) {
                        this.emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(str);
                    }
                } else if (this.messageOwner.entities.size() == 1 && (this.messageOwner.entities.get(0) instanceof TLRPC$TL_messageEntityCustomEmoji)) {
                    try {
                        Long valueOf = Long.valueOf(((TLRPC$TL_messageEntityCustomEmoji) this.messageOwner.entities.get(0)).document_id);
                        this.emojiAnimatedStickerId = valueOf;
                        TLRPC$Document findDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, valueOf.longValue());
                        this.emojiAnimatedSticker = findDocument;
                        if (findDocument == null) {
                            CharSequence charSequence2 = this.messageText;
                            if ((charSequence2 instanceof Spanned) && (animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence2).getSpans(0, charSequence2.length(), AnimatedEmojiSpan.class)) != null && animatedEmojiSpanArr.length == 1) {
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
            return;
        }
        this.type = 1000;
        if (isSticker()) {
            this.type = 13;
        } else if (!isAnimatedSticker()) {
        } else {
            this.type = 15;
        }
    }

    private void createPathThumb() {
        TLRPC$Document document = getDocument();
        if (document == null) {
            return;
        }
        this.pathThumb = DocumentObject.getSvgThumb(document, "chat_serviceBackground", 1.0f);
    }

    public void createStrippedThumb() {
        if (this.photoThumbs != null) {
            if (SharedConfig.getDevicePerformanceClass() != 2 && !hasExtendedMediaPreview()) {
                return;
            }
            try {
                int size = this.photoThumbs.size();
                for (int i = 0; i < size; i++) {
                    TLRPC$PhotoSize tLRPC$PhotoSize = this.photoThumbs.get(i);
                    if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                        this.strippedThumb = new BitmapDrawable(ApplicationLoader.applicationContext.getResources(), ImageLoader.getStrippedPhotoBitmap(tLRPC$PhotoSize.bytes, "b"));
                        return;
                    }
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    private void createDateArray(int i, TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent, ArrayList<MessageObject> arrayList, HashMap<String, ArrayList<MessageObject>> hashMap, boolean z) {
        if (hashMap.get(this.dateKey) == null) {
            hashMap.put(this.dateKey, new ArrayList<>());
            TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
            tLRPC$TL_message.message = LocaleController.formatDateChat(tLRPC$TL_channelAdminLogEvent.date);
            tLRPC$TL_message.id = 0;
            tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent.date;
            MessageObject messageObject = new MessageObject(i, tLRPC$TL_message, false, false);
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

    private void checkEmojiOnly(int[] iArr) {
        checkEmojiOnly(iArr == null ? null : Integer.valueOf(iArr[0]));
    }

    private void checkEmojiOnly(Integer num) {
        int i;
        TextPaint textPaint;
        int i2 = -1;
        if (num != null && num.intValue() >= 1) {
            CharSequence charSequence = this.messageText;
            Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) ((Spannable) charSequence).getSpans(0, charSequence.length(), Emoji.EmojiSpan.class);
            CharSequence charSequence2 = this.messageText;
            AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spannable) charSequence2).getSpans(0, charSequence2.length(), AnimatedEmojiSpan.class);
            this.emojiOnlyCount = Math.max(num.intValue(), (emojiSpanArr == null ? 0 : emojiSpanArr.length) + (animatedEmojiSpanArr == null ? 0 : animatedEmojiSpanArr.length));
            this.totalAnimatedEmojiCount = animatedEmojiSpanArr == null ? 0 : animatedEmojiSpanArr.length;
            if (animatedEmojiSpanArr != null) {
                i = 0;
                for (AnimatedEmojiSpan animatedEmojiSpan : animatedEmojiSpanArr) {
                    if (!animatedEmojiSpan.standard) {
                        i++;
                    }
                }
            } else {
                i = 0;
            }
            int i3 = this.emojiOnlyCount;
            boolean z = (i3 - (emojiSpanArr == null ? 0 : emojiSpanArr.length)) - (animatedEmojiSpanArr == null ? 0 : animatedEmojiSpanArr.length) > 0;
            this.hasUnwrappedEmoji = z;
            if (i3 == 0 || z) {
                if (animatedEmojiSpanArr == null || animatedEmojiSpanArr.length <= 0) {
                    return;
                }
                for (int i4 = 0; i4 < animatedEmojiSpanArr.length; i4++) {
                    animatedEmojiSpanArr[i4].replaceFontMetrics(Theme.chat_msgTextPaint.getFontMetricsInt(), (int) (Theme.chat_msgTextPaint.getTextSize() + AndroidUtilities.dp(4.0f)), -1);
                    animatedEmojiSpanArr[i4].full = false;
                }
                return;
            }
            boolean z2 = i3 == i;
            int i5 = 2;
            switch (i3) {
                case 0:
                case 1:
                    TextPaint[] textPaintArr = Theme.chat_msgTextPaintEmoji;
                    textPaint = z2 ? textPaintArr[0] : textPaintArr[2];
                    i5 = 1;
                    break;
                case 2:
                    TextPaint[] textPaintArr2 = Theme.chat_msgTextPaintEmoji;
                    textPaint = z2 ? textPaintArr2[0] : textPaintArr2[2];
                    i5 = 1;
                    break;
                case 3:
                    TextPaint[] textPaintArr3 = Theme.chat_msgTextPaintEmoji;
                    textPaint = z2 ? textPaintArr3[1] : textPaintArr3[3];
                    i5 = 1;
                    break;
                case 4:
                    TextPaint[] textPaintArr4 = Theme.chat_msgTextPaintEmoji;
                    textPaint = z2 ? textPaintArr4[2] : textPaintArr4[4];
                    i5 = 1;
                    break;
                case 5:
                    TextPaint[] textPaintArr5 = Theme.chat_msgTextPaintEmoji;
                    if (z2) {
                        textPaint = textPaintArr5[3];
                        break;
                    } else {
                        textPaint = textPaintArr5[5];
                        break;
                    }
                case 6:
                    TextPaint[] textPaintArr6 = Theme.chat_msgTextPaintEmoji;
                    if (z2) {
                        textPaint = textPaintArr6[4];
                        break;
                    } else {
                        textPaint = textPaintArr6[5];
                        break;
                    }
                default:
                    if (i3 > 9) {
                        i2 = 0;
                    }
                    textPaint = Theme.chat_msgTextPaintEmoji[5];
                    i5 = i2;
                    break;
            }
            int textSize = (int) (textPaint.getTextSize() + AndroidUtilities.dp(4.0f));
            if (emojiSpanArr != null && emojiSpanArr.length > 0) {
                for (Emoji.EmojiSpan emojiSpan : emojiSpanArr) {
                    emojiSpan.replaceFontMetrics(textPaint.getFontMetricsInt(), textSize);
                }
            }
            if (animatedEmojiSpanArr == null || animatedEmojiSpanArr.length <= 0) {
                return;
            }
            for (int i6 = 0; i6 < animatedEmojiSpanArr.length; i6++) {
                animatedEmojiSpanArr[i6].replaceFontMetrics(textPaint.getFontMetricsInt(), textSize, i5);
                animatedEmojiSpanArr[i6].full = true;
            }
            return;
        }
        CharSequence charSequence3 = this.messageText;
        AnimatedEmojiSpan[] animatedEmojiSpanArr2 = (AnimatedEmojiSpan[]) ((Spannable) charSequence3).getSpans(0, charSequence3.length(), AnimatedEmojiSpan.class);
        if (animatedEmojiSpanArr2 != null && animatedEmojiSpanArr2.length > 0) {
            this.totalAnimatedEmojiCount = animatedEmojiSpanArr2.length;
            for (int i7 = 0; i7 < animatedEmojiSpanArr2.length; i7++) {
                animatedEmojiSpanArr2[i7].replaceFontMetrics(Theme.chat_msgTextPaint.getFontMetricsInt(), (int) (Theme.chat_msgTextPaint.getTextSize() + AndroidUtilities.dp(4.0f)), -1);
                animatedEmojiSpanArr2[i7].full = false;
            }
            return;
        }
        this.totalAnimatedEmojiCount = 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x15f2  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x1640  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x1643  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x04b9  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x04d3 A[LOOP:0: B:217:0x0486->B:230:0x04d3, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x04ec A[EDGE_INSN: B:231:0x04ec->B:232:0x04ec ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x16c6  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x04cd  */
    /* JADX WARN: Removed duplicated region for block: B:501:0x0ca6  */
    /* JADX WARN: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x16bf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MessageObject(int i, TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent, ArrayList<MessageObject> arrayList, HashMap<String, ArrayList<MessageObject>> hashMap, TLRPC$Chat tLRPC$Chat, int[] iArr, boolean z) {
        String str;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant2;
        TLRPC$User user;
        String string;
        char c;
        StringBuilder sb;
        int i2;
        String str2;
        String str3;
        TLRPC$Message tLRPC$Message;
        char c2;
        String formatPluralString;
        TLObject chat;
        TLObject chat2;
        TLObject chat3;
        char c3;
        String formatPluralString2;
        ArrayList<TLRPC$MessageEntity> arrayList2;
        TLRPC$WebPage tLRPC$WebPage;
        ArrayList<TLRPC$MessageEntity> arrayList3;
        TLRPC$Peer tLRPC$Peer;
        int i3;
        String str4;
        int i4;
        String str5;
        int i5;
        String str6;
        TLRPC$TL_message tLRPC$TL_message;
        int i6;
        String str7;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLObject chat4;
        String string2;
        StringBuilder sb2;
        boolean z2;
        char c4;
        String formatPluralString3;
        int i7;
        int i8;
        int i9;
        boolean z3;
        TLObject chat5;
        ArrayList<MessageObject> arrayList4;
        int[] iArr2;
        TextPaint textPaint;
        TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.topicIconDrawable = new ForumBubbleDrawable[1];
        this.currentEvent = tLRPC$TL_channelAdminLogEvent2;
        this.currentAccount = i;
        TLRPC$User user2 = tLRPC$TL_channelAdminLogEvent2.user_id > 0 ? MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEvent2.user_id)) : null;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(tLRPC$TL_channelAdminLogEvent2.date * 1000);
        int i10 = gregorianCalendar.get(6);
        int i11 = gregorianCalendar.get(1);
        int i12 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i11), Integer.valueOf(i12), Integer.valueOf(i10));
        this.monthKey = String.format("%d_%02d", Integer.valueOf(i11), Integer.valueOf(i12));
        TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
        tLRPC$TL_peerChannel.channel_id = tLRPC$Chat.id;
        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction = tLRPC$TL_channelAdminLogEvent2.action;
        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTitle) {
            String str8 = ((TLRPC$TL_channelAdminLogEventActionChangeTitle) tLRPC$ChannelAdminLogEventAction).new_value;
            if (tLRPC$Chat.megagroup) {
                this.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedGroupTitle", R.string.EventLogEditedGroupTitle, str8), "un1", user2);
            } else {
                this.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedChannelTitle", R.string.EventLogEditedChannelTitle, str8), "un1", user2);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangePhoto) {
            TLRPC$TL_channelAdminLogEventActionChangePhoto tLRPC$TL_channelAdminLogEventActionChangePhoto = (TLRPC$TL_channelAdminLogEventActionChangePhoto) tLRPC$ChannelAdminLogEventAction;
            TLRPC$TL_messageService tLRPC$TL_messageService = new TLRPC$TL_messageService();
            this.messageOwner = tLRPC$TL_messageService;
            if (tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo instanceof TLRPC$TL_photoEmpty) {
                tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatDeletePhoto();
                if (tLRPC$Chat.megagroup) {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedWGroupPhoto", R.string.EventLogRemovedWGroupPhoto), "un1", user2);
                } else {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedChannelPhoto", R.string.EventLogRemovedChannelPhoto), "un1", user2);
                }
            } else {
                tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatEditPhoto();
                this.messageOwner.action.photo = tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo;
                if (tLRPC$Chat.megagroup) {
                    if (isVideoAvatar()) {
                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedGroupVideo", R.string.EventLogEditedGroupVideo), "un1", user2);
                    } else {
                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedGroupPhoto", R.string.EventLogEditedGroupPhoto), "un1", user2);
                    }
                } else if (isVideoAvatar()) {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedChannelVideo", R.string.EventLogEditedChannelVideo), "un1", user2);
                } else {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedChannelPhoto", R.string.EventLogEditedChannelPhoto), "un1", user2);
                }
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoin) {
            if (tLRPC$Chat.megagroup) {
                this.messageText = replaceWithLink(LocaleController.getString("EventLogGroupJoined", R.string.EventLogGroupJoined), "un1", user2);
            } else {
                this.messageText = replaceWithLink(LocaleController.getString("EventLogChannelJoined", R.string.EventLogChannelJoined), "un1", user2);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantLeave) {
            TLRPC$TL_messageService tLRPC$TL_messageService2 = new TLRPC$TL_messageService();
            this.messageOwner = tLRPC$TL_messageService2;
            tLRPC$TL_messageService2.action = new TLRPC$TL_messageActionChatDeleteUser();
            this.messageOwner.action.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
            if (tLRPC$Chat.megagroup) {
                this.messageText = replaceWithLink(LocaleController.getString("EventLogLeftGroup", R.string.EventLogLeftGroup), "un1", user2);
            } else {
                this.messageText = replaceWithLink(LocaleController.getString("EventLogLeftChannel", R.string.EventLogLeftChannel), "un1", user2);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantInvite) {
            TLRPC$TL_messageService tLRPC$TL_messageService3 = new TLRPC$TL_messageService();
            this.messageOwner = tLRPC$TL_messageService3;
            tLRPC$TL_messageService3.action = new TLRPC$TL_messageActionChatAddUser();
            long peerId = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantInvite) tLRPC$ChannelAdminLogEventAction).participant.peer);
            if (peerId > 0) {
                chat5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId));
            } else {
                chat5 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId));
            }
            TLRPC$Peer tLRPC$Peer2 = this.messageOwner.from_id;
            if ((tLRPC$Peer2 instanceof TLRPC$TL_peerUser) && peerId == tLRPC$Peer2.user_id) {
                if (tLRPC$Chat.megagroup) {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogGroupJoined", R.string.EventLogGroupJoined), "un1", user2);
                } else {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogChannelJoined", R.string.EventLogChannelJoined), "un1", user2);
                }
            } else {
                CharSequence replaceWithLink = replaceWithLink(LocaleController.getString("EventLogAdded", R.string.EventLogAdded), "un2", chat5);
                this.messageText = replaceWithLink;
                this.messageText = replaceWithLink(replaceWithLink, "un1", user2);
            }
        } else {
            if ((tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin) || ((tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) && (((TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction).prev_participant instanceof TLRPC$TL_channelParticipantAdmin) && (((TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction).new_participant instanceof TLRPC$TL_channelParticipant))) {
                str = "";
                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin) {
                    TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin = (TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin) tLRPC$ChannelAdminLogEventAction;
                    tLRPC$ChannelParticipant = tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin.prev_participant;
                    tLRPC$ChannelParticipant2 = tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin.new_participant;
                } else {
                    TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                    tLRPC$ChannelParticipant = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan.prev_participant;
                    tLRPC$ChannelParticipant2 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan.new_participant;
                }
                this.messageOwner = new TLRPC$TL_message();
                long peerId2 = getPeerId(tLRPC$ChannelParticipant.peer);
                if (peerId2 > 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId2));
                } else {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(-peerId2));
                }
                if (!(tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator) && (tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator)) {
                    String string3 = LocaleController.getString("EventLogChangedOwnership", R.string.EventLogChangedOwnership);
                    sb = new StringBuilder(String.format(string3, getUserName(user, this.messageOwner.entities, string3.indexOf("%1$s"))));
                } else {
                    TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$ChannelParticipant.admin_rights;
                    TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2 = tLRPC$ChannelParticipant2.admin_rights;
                    tLRPC$TL_chatAdminRights = tLRPC$TL_chatAdminRights == null ? new TLRPC$TL_chatAdminRights() : tLRPC$TL_chatAdminRights;
                    tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights2 == null ? new TLRPC$TL_chatAdminRights() : tLRPC$TL_chatAdminRights2;
                    if (tLRPC$TL_chatAdminRights2.other) {
                        string = LocaleController.getString("EventLogPromotedNoRights", R.string.EventLogPromotedNoRights);
                    } else {
                        string = LocaleController.getString("EventLogPromoted", R.string.EventLogPromoted);
                    }
                    StringBuilder sb3 = new StringBuilder(String.format(string, getUserName(user, this.messageOwner.entities, string.indexOf("%1$s"))));
                    sb3.append("\n");
                    if (TextUtils.equals(tLRPC$ChannelParticipant.rank, tLRPC$ChannelParticipant2.rank)) {
                        c = '+';
                    } else if (TextUtils.isEmpty(tLRPC$ChannelParticipant2.rank)) {
                        sb3.append('\n');
                        sb3.append('-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedRemovedTitle", R.string.EventLogPromotedRemovedTitle));
                        c = '+';
                    } else {
                        sb3.append('\n');
                        c = '+';
                        sb3.append('+');
                        sb3.append(' ');
                        sb3.append(LocaleController.formatString("EventLogPromotedTitle", R.string.EventLogPromotedTitle, tLRPC$ChannelParticipant2.rank));
                    }
                    if (tLRPC$TL_chatAdminRights.change_info != tLRPC$TL_chatAdminRights2.change_info) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.change_info ? '+' : '-');
                        sb3.append(' ');
                        if (tLRPC$Chat.megagroup) {
                            i2 = R.string.EventLogPromotedChangeGroupInfo;
                            str2 = "EventLogPromotedChangeGroupInfo";
                        } else {
                            i2 = R.string.EventLogPromotedChangeChannelInfo;
                            str2 = "EventLogPromotedChangeChannelInfo";
                        }
                        sb3.append(LocaleController.getString(str2, i2));
                    }
                    if (!tLRPC$Chat.megagroup) {
                        if (tLRPC$TL_chatAdminRights.post_messages != tLRPC$TL_chatAdminRights2.post_messages) {
                            sb3.append('\n');
                            sb3.append(tLRPC$TL_chatAdminRights2.post_messages ? '+' : '-');
                            sb3.append(' ');
                            sb3.append(LocaleController.getString("EventLogPromotedPostMessages", R.string.EventLogPromotedPostMessages));
                        }
                        if (tLRPC$TL_chatAdminRights.edit_messages != tLRPC$TL_chatAdminRights2.edit_messages) {
                            sb3.append('\n');
                            sb3.append(tLRPC$TL_chatAdminRights2.edit_messages ? '+' : '-');
                            sb3.append(' ');
                            sb3.append(LocaleController.getString("EventLogPromotedEditMessages", R.string.EventLogPromotedEditMessages));
                        }
                    }
                    if (tLRPC$TL_chatAdminRights.delete_messages != tLRPC$TL_chatAdminRights2.delete_messages) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.delete_messages ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedDeleteMessages", R.string.EventLogPromotedDeleteMessages));
                    }
                    if (tLRPC$TL_chatAdminRights.add_admins != tLRPC$TL_chatAdminRights2.add_admins) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.add_admins ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedAddAdmins", R.string.EventLogPromotedAddAdmins));
                    }
                    if (tLRPC$TL_chatAdminRights.anonymous != tLRPC$TL_chatAdminRights2.anonymous) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.anonymous ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedSendAnonymously", R.string.EventLogPromotedSendAnonymously));
                    }
                    if (tLRPC$Chat.megagroup) {
                        if (tLRPC$TL_chatAdminRights.ban_users != tLRPC$TL_chatAdminRights2.ban_users) {
                            sb3.append('\n');
                            sb3.append(tLRPC$TL_chatAdminRights2.ban_users ? '+' : '-');
                            sb3.append(' ');
                            sb3.append(LocaleController.getString("EventLogPromotedBanUsers", R.string.EventLogPromotedBanUsers));
                        }
                        if (tLRPC$TL_chatAdminRights.manage_call != tLRPC$TL_chatAdminRights2.manage_call) {
                            sb3.append('\n');
                            sb3.append(tLRPC$TL_chatAdminRights2.manage_call ? '+' : '-');
                            sb3.append(' ');
                            sb3.append(LocaleController.getString("EventLogPromotedManageCall", R.string.EventLogPromotedManageCall));
                        }
                    }
                    if (tLRPC$TL_chatAdminRights.invite_users != tLRPC$TL_chatAdminRights2.invite_users) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.invite_users ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedAddUsers", R.string.EventLogPromotedAddUsers));
                    }
                    if (tLRPC$Chat.megagroup && tLRPC$TL_chatAdminRights.pin_messages != tLRPC$TL_chatAdminRights2.pin_messages) {
                        sb3.append('\n');
                        sb3.append(!tLRPC$TL_chatAdminRights2.pin_messages ? '-' : c);
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedPinMessages", R.string.EventLogPromotedPinMessages));
                    }
                    sb = sb3;
                }
                this.messageText = sb.toString();
            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) {
                TLRPC$TL_channelAdminLogEventActionDefaultBannedRights tLRPC$TL_channelAdminLogEventActionDefaultBannedRights = (TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) tLRPC$ChannelAdminLogEventAction;
                this.messageOwner = new TLRPC$TL_message();
                TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights = tLRPC$TL_channelAdminLogEventActionDefaultBannedRights.prev_banned_rights;
                TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2 = tLRPC$TL_channelAdminLogEventActionDefaultBannedRights.new_banned_rights;
                StringBuilder sb4 = new StringBuilder(LocaleController.getString("EventLogDefaultPermissions", R.string.EventLogDefaultPermissions));
                tLRPC$TL_chatBannedRights = tLRPC$TL_chatBannedRights == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights;
                tLRPC$TL_chatBannedRights2 = tLRPC$TL_chatBannedRights2 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights2;
                if (tLRPC$TL_chatBannedRights.send_messages != tLRPC$TL_chatBannedRights2.send_messages) {
                    sb4.append('\n');
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_messages ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendMessages", R.string.EventLogRestrictedSendMessages));
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (tLRPC$TL_chatBannedRights.send_stickers != tLRPC$TL_chatBannedRights2.send_stickers || tLRPC$TL_chatBannedRights.send_inline != tLRPC$TL_chatBannedRights2.send_inline || tLRPC$TL_chatBannedRights.send_gifs != tLRPC$TL_chatBannedRights2.send_gifs || tLRPC$TL_chatBannedRights.send_games != tLRPC$TL_chatBannedRights2.send_games) {
                    if (!z3) {
                        sb4.append('\n');
                        z3 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_stickers ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendStickers", R.string.EventLogRestrictedSendStickers));
                }
                if (tLRPC$TL_chatBannedRights.send_media != tLRPC$TL_chatBannedRights2.send_media) {
                    if (!z3) {
                        sb4.append('\n');
                        z3 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_media ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendMedia", R.string.EventLogRestrictedSendMedia));
                }
                if (tLRPC$TL_chatBannedRights.send_polls != tLRPC$TL_chatBannedRights2.send_polls) {
                    if (!z3) {
                        sb4.append('\n');
                        z3 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_polls ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendPolls", R.string.EventLogRestrictedSendPolls));
                }
                if (tLRPC$TL_chatBannedRights.embed_links != tLRPC$TL_chatBannedRights2.embed_links) {
                    if (!z3) {
                        sb4.append('\n');
                        z3 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.embed_links ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendEmbed", R.string.EventLogRestrictedSendEmbed));
                }
                if (tLRPC$TL_chatBannedRights.change_info != tLRPC$TL_chatBannedRights2.change_info) {
                    if (!z3) {
                        sb4.append('\n');
                        z3 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.change_info ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedChangeInfo", R.string.EventLogRestrictedChangeInfo));
                }
                if (tLRPC$TL_chatBannedRights.invite_users != tLRPC$TL_chatBannedRights2.invite_users) {
                    if (!z3) {
                        sb4.append('\n');
                        z3 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.invite_users ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedInviteUsers", R.string.EventLogRestrictedInviteUsers));
                }
                if (tLRPC$TL_chatBannedRights.pin_messages != tLRPC$TL_chatBannedRights2.pin_messages) {
                    if (!z3) {
                        sb4.append('\n');
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.pin_messages ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedPinMessages", R.string.EventLogRestrictedPinMessages));
                }
                this.messageText = sb4.toString();
            } else {
                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) {
                    TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2 = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                    this.messageOwner = new TLRPC$TL_message();
                    long peerId3 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.peer);
                    if (peerId3 > 0) {
                        chat4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId3));
                    } else {
                        chat4 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId3));
                    }
                    TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights3 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.banned_rights;
                    TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights4 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.new_participant.banned_rights;
                    if (tLRPC$Chat.megagroup && (tLRPC$TL_chatBannedRights4 == null || !tLRPC$TL_chatBannedRights4.view_messages || (tLRPC$TL_chatBannedRights3 != null && tLRPC$TL_chatBannedRights4.until_date != tLRPC$TL_chatBannedRights3.until_date))) {
                        if (tLRPC$TL_chatBannedRights4 != null && !AndroidUtilities.isBannedForever(tLRPC$TL_chatBannedRights4)) {
                            sb2 = new StringBuilder();
                            int i13 = tLRPC$TL_chatBannedRights4.until_date - tLRPC$TL_channelAdminLogEvent2.date;
                            int i14 = ((i13 / 60) / 60) / 24;
                            int i15 = i13 - (((i14 * 60) * 60) * 24);
                            int i16 = (i15 / 60) / 60;
                            int i17 = (i15 - ((i16 * 60) * 60)) / 60;
                            str3 = "";
                            int i18 = 0;
                            int i19 = 0;
                            for (int i20 = 3; i18 < i20; i20 = 3) {
                                if (i18 == 0) {
                                    if (i14 != 0) {
                                        formatPluralString3 = LocaleController.formatPluralString("Days", i14, new Object[0]);
                                        i7 = i19 + 1;
                                        i8 = i7;
                                        if (formatPluralString3 != null) {
                                            if (sb2.length() > 0) {
                                                i9 = i16;
                                                sb2.append(", ");
                                            } else {
                                                i9 = i16;
                                            }
                                            sb2.append(formatPluralString3);
                                        } else {
                                            i9 = i16;
                                        }
                                        if (i8 == 2) {
                                            break;
                                        }
                                        i18++;
                                        i16 = i9;
                                        i19 = i8;
                                    }
                                    i8 = i19;
                                    formatPluralString3 = null;
                                    if (formatPluralString3 != null) {
                                    }
                                    if (i8 == 2) {
                                    }
                                } else {
                                    if (i18 == 1) {
                                        if (i16 != 0) {
                                            i7 = i19 + 1;
                                            formatPluralString3 = LocaleController.formatPluralString("Hours", i16, new Object[0]);
                                            i8 = i7;
                                            if (formatPluralString3 != null) {
                                            }
                                            if (i8 == 2) {
                                            }
                                        }
                                    } else if (i17 != 0) {
                                        formatPluralString3 = LocaleController.formatPluralString("Minutes", i17, new Object[0]);
                                        i7 = i19 + 1;
                                        i8 = i7;
                                        if (formatPluralString3 != null) {
                                        }
                                        if (i8 == 2) {
                                        }
                                    }
                                    i8 = i19;
                                    formatPluralString3 = null;
                                    if (formatPluralString3 != null) {
                                    }
                                    if (i8 == 2) {
                                    }
                                }
                            }
                        } else {
                            str3 = "";
                            sb2 = new StringBuilder(LocaleController.getString("UserRestrictionsUntilForever", R.string.UserRestrictionsUntilForever));
                        }
                        String string4 = LocaleController.getString("EventLogRestrictedUntil", R.string.EventLogRestrictedUntil);
                        StringBuilder sb5 = new StringBuilder(String.format(string4, getUserName(chat4, this.messageOwner.entities, string4.indexOf("%1$s")), sb2.toString()));
                        tLRPC$TL_chatBannedRights3 = tLRPC$TL_chatBannedRights3 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights3;
                        tLRPC$TL_chatBannedRights4 = tLRPC$TL_chatBannedRights4 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights4;
                        if (tLRPC$TL_chatBannedRights3.view_messages != tLRPC$TL_chatBannedRights4.view_messages) {
                            sb5.append('\n');
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.view_messages ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedReadMessages", R.string.EventLogRestrictedReadMessages));
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (tLRPC$TL_chatBannedRights3.send_messages != tLRPC$TL_chatBannedRights4.send_messages) {
                            if (!z2) {
                                sb5.append('\n');
                                z2 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_messages ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendMessages", R.string.EventLogRestrictedSendMessages));
                        }
                        if (tLRPC$TL_chatBannedRights3.send_stickers != tLRPC$TL_chatBannedRights4.send_stickers || tLRPC$TL_chatBannedRights3.send_inline != tLRPC$TL_chatBannedRights4.send_inline || tLRPC$TL_chatBannedRights3.send_gifs != tLRPC$TL_chatBannedRights4.send_gifs || tLRPC$TL_chatBannedRights3.send_games != tLRPC$TL_chatBannedRights4.send_games) {
                            if (!z2) {
                                sb5.append('\n');
                                z2 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_stickers ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendStickers", R.string.EventLogRestrictedSendStickers));
                        }
                        if (tLRPC$TL_chatBannedRights3.send_media != tLRPC$TL_chatBannedRights4.send_media) {
                            if (!z2) {
                                sb5.append('\n');
                                z2 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_media ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendMedia", R.string.EventLogRestrictedSendMedia));
                        }
                        if (tLRPC$TL_chatBannedRights3.send_polls != tLRPC$TL_chatBannedRights4.send_polls) {
                            if (!z2) {
                                sb5.append('\n');
                                z2 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_polls ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendPolls", R.string.EventLogRestrictedSendPolls));
                        }
                        if (tLRPC$TL_chatBannedRights3.embed_links != tLRPC$TL_chatBannedRights4.embed_links) {
                            if (!z2) {
                                sb5.append('\n');
                                z2 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.embed_links ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendEmbed", R.string.EventLogRestrictedSendEmbed));
                        }
                        if (tLRPC$TL_chatBannedRights3.change_info != tLRPC$TL_chatBannedRights4.change_info) {
                            if (!z2) {
                                sb5.append('\n');
                                z2 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.change_info ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedChangeInfo", R.string.EventLogRestrictedChangeInfo));
                        }
                        if (tLRPC$TL_chatBannedRights3.invite_users != tLRPC$TL_chatBannedRights4.invite_users) {
                            if (!z2) {
                                sb5.append('\n');
                                z2 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.invite_users ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedInviteUsers", R.string.EventLogRestrictedInviteUsers));
                        }
                        if (tLRPC$TL_chatBannedRights3.pin_messages != tLRPC$TL_chatBannedRights4.pin_messages) {
                            if (!z2) {
                                c4 = '\n';
                                sb5.append('\n');
                            } else {
                                c4 = '\n';
                            }
                            sb5.append(c4);
                            sb5.append(!tLRPC$TL_chatBannedRights4.pin_messages ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedPinMessages", R.string.EventLogRestrictedPinMessages));
                        }
                        this.messageText = sb5.toString();
                    } else {
                        str3 = "";
                        if (tLRPC$TL_chatBannedRights4 != null && (tLRPC$TL_chatBannedRights3 == null || tLRPC$TL_chatBannedRights4.view_messages)) {
                            string2 = LocaleController.getString("EventLogChannelRestricted", R.string.EventLogChannelRestricted);
                        } else {
                            string2 = LocaleController.getString("EventLogChannelUnrestricted", R.string.EventLogChannelUnrestricted);
                        }
                        this.messageText = String.format(string2, getUserName(chat4, this.messageOwner.entities, string2.indexOf("%1$s")));
                    }
                } else {
                    str3 = "";
                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionUpdatePinned) {
                        TLRPC$TL_channelAdminLogEventActionUpdatePinned tLRPC$TL_channelAdminLogEventActionUpdatePinned = (TLRPC$TL_channelAdminLogEventActionUpdatePinned) tLRPC$ChannelAdminLogEventAction;
                        tLRPC$Message = tLRPC$TL_channelAdminLogEventActionUpdatePinned.message;
                        if (user2 != null && user2.id == 136817688 && (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) != null && (tLRPC$MessageFwdHeader.from_id instanceof TLRPC$TL_peerChannel)) {
                            TLRPC$Chat chat6 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$TL_channelAdminLogEventActionUpdatePinned.message.fwd_from.from_id.channel_id));
                            TLRPC$Message tLRPC$Message2 = tLRPC$TL_channelAdminLogEventActionUpdatePinned.message;
                            if ((tLRPC$Message2 instanceof TLRPC$TL_messageEmpty) || !tLRPC$Message2.pinned) {
                                this.messageText = replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", R.string.EventLogUnpinnedMessages), "un1", chat6);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.getString("EventLogPinnedMessages", R.string.EventLogPinnedMessages), "un1", chat6);
                            }
                        } else if ((tLRPC$Message instanceof TLRPC$TL_messageEmpty) || !tLRPC$Message.pinned) {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", R.string.EventLogUnpinnedMessages), "un1", user2);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogPinnedMessages", R.string.EventLogPinnedMessages), "un1", user2);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStopPoll) {
                        tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionStopPoll) tLRPC$ChannelAdminLogEventAction).message;
                        if ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPoll) && ((TLRPC$TL_messageMediaPoll) getMedia(tLRPC$Message)).poll.quiz) {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogStopQuiz", R.string.EventLogStopQuiz), "un1", user2);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogStopPoll", R.string.EventLogStopPoll), "un1", user2);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSignatures) {
                        if (((TLRPC$TL_channelAdminLogEventActionToggleSignatures) tLRPC$ChannelAdminLogEventAction).new_value) {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOn", R.string.EventLogToggledSignaturesOn), "un1", user2);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOff", R.string.EventLogToggledSignaturesOff), "un1", user2);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleInvites) {
                        if (((TLRPC$TL_channelAdminLogEventActionToggleInvites) tLRPC$ChannelAdminLogEventAction).new_value) {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesOn", R.string.EventLogToggledInvitesOn), "un1", user2);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesOff", R.string.EventLogToggledInvitesOff), "un1", user2);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                        tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionDeleteMessage) tLRPC$ChannelAdminLogEventAction).message;
                        this.messageText = replaceWithLink(LocaleController.getString("EventLogDeletedMessages", R.string.EventLogDeletedMessages), "un1", user2);
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) {
                        long j = ((TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) tLRPC$ChannelAdminLogEventAction).new_value;
                        long j2 = ((TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) tLRPC$ChannelAdminLogEventAction).prev_value;
                        if (tLRPC$Chat.megagroup) {
                            if (j == 0) {
                                TLRPC$Chat chat7 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
                                CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString("EventLogRemovedLinkedChannel", R.string.EventLogRemovedLinkedChannel), "un1", user2);
                                this.messageText = replaceWithLink2;
                                this.messageText = replaceWithLink(replaceWithLink2, "un2", chat7);
                            } else {
                                TLRPC$Chat chat8 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                                CharSequence replaceWithLink3 = replaceWithLink(LocaleController.getString("EventLogChangedLinkedChannel", R.string.EventLogChangedLinkedChannel), "un1", user2);
                                this.messageText = replaceWithLink3;
                                this.messageText = replaceWithLink(replaceWithLink3, "un2", chat8);
                            }
                        } else if (j == 0) {
                            TLRPC$Chat chat9 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
                            CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString("EventLogRemovedLinkedGroup", R.string.EventLogRemovedLinkedGroup), "un1", user2);
                            this.messageText = replaceWithLink4;
                            this.messageText = replaceWithLink(replaceWithLink4, "un2", chat9);
                        } else {
                            TLRPC$Chat chat10 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                            CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString("EventLogChangedLinkedGroup", R.string.EventLogChangedLinkedGroup), "un1", user2);
                            this.messageText = replaceWithLink5;
                            this.messageText = replaceWithLink(replaceWithLink5, "un2", chat10);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) {
                        if (((TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) tLRPC$ChannelAdminLogEventAction).new_value) {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOff", R.string.EventLogToggledInvitesHistoryOff), "un1", user2);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOn", R.string.EventLogToggledInvitesHistoryOn), "un1", user2);
                        }
                    } else {
                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAbout) {
                            if (tLRPC$Chat.megagroup) {
                                i6 = R.string.EventLogEditedGroupDescription;
                                str7 = "EventLogEditedGroupDescription";
                            } else {
                                i6 = R.string.EventLogEditedChannelDescription;
                                str7 = "EventLogEditedChannelDescription";
                            }
                            this.messageText = replaceWithLink(LocaleController.getString(str7, i6), "un1", user2);
                            tLRPC$TL_message = new TLRPC$TL_message();
                            tLRPC$TL_message.out = false;
                            tLRPC$TL_message.unread = false;
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            tLRPC$TL_message.from_id = tLRPC$TL_peerUser;
                            tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                            tLRPC$TL_peerUser.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                            tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                            tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent2.date;
                            TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction2 = tLRPC$TL_channelAdminLogEvent2.action;
                            tLRPC$TL_message.message = ((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$ChannelAdminLogEventAction2).new_value;
                            if (!TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$ChannelAdminLogEventAction2).prev_value)) {
                                TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage = new TLRPC$TL_messageMediaWebPage();
                                tLRPC$TL_message.media = tLRPC$TL_messageMediaWebPage;
                                tLRPC$TL_messageMediaWebPage.webpage = new TLRPC$TL_webPage();
                                TLRPC$WebPage tLRPC$WebPage2 = tLRPC$TL_message.media.webpage;
                                tLRPC$WebPage2.flags = 10;
                                str = str3;
                                tLRPC$WebPage2.display_url = str;
                                tLRPC$WebPage2.url = str;
                                tLRPC$WebPage2.site_name = LocaleController.getString("EventLogPreviousGroupDescription", R.string.EventLogPreviousGroupDescription);
                                tLRPC$TL_message.media.webpage.description = ((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                            } else {
                                str = str3;
                                tLRPC$TL_message.media = new TLRPC$TL_messageMediaEmpty();
                            }
                        } else {
                            tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                            str = str3;
                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTheme) {
                                if (tLRPC$Chat.megagroup) {
                                    i5 = R.string.EventLogEditedGroupTheme;
                                    str6 = "EventLogEditedGroupTheme";
                                } else {
                                    i5 = R.string.EventLogEditedChannelTheme;
                                    str6 = "EventLogEditedChannelTheme";
                                }
                                this.messageText = replaceWithLink(LocaleController.getString(str6, i5), "un1", user2);
                                tLRPC$TL_message = new TLRPC$TL_message();
                                tLRPC$TL_message.out = false;
                                tLRPC$TL_message.unread = false;
                                TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                                tLRPC$TL_message.from_id = tLRPC$TL_peerUser2;
                                tLRPC$TL_peerUser2.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction3 = tLRPC$TL_channelAdminLogEvent2.action;
                                tLRPC$TL_message.message = ((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$ChannelAdminLogEventAction3).new_value;
                                if (!TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$ChannelAdminLogEventAction3).prev_value)) {
                                    TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage2 = new TLRPC$TL_messageMediaWebPage();
                                    tLRPC$TL_message.media = tLRPC$TL_messageMediaWebPage2;
                                    tLRPC$TL_messageMediaWebPage2.webpage = new TLRPC$TL_webPage();
                                    TLRPC$WebPage tLRPC$WebPage3 = tLRPC$TL_message.media.webpage;
                                    tLRPC$WebPage3.flags = 10;
                                    tLRPC$WebPage3.display_url = str;
                                    tLRPC$WebPage3.url = str;
                                    tLRPC$WebPage3.site_name = LocaleController.getString("EventLogPreviousGroupTheme", R.string.EventLogPreviousGroupTheme);
                                    tLRPC$TL_message.media.webpage.description = ((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                } else {
                                    tLRPC$TL_message.media = new TLRPC$TL_messageMediaEmpty();
                                }
                            } else {
                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeUsername) {
                                    String str9 = ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$ChannelAdminLogEventAction).new_value;
                                    if (!TextUtils.isEmpty(str9)) {
                                        if (tLRPC$Chat.megagroup) {
                                            i4 = R.string.EventLogChangedGroupLink;
                                            str5 = "EventLogChangedGroupLink";
                                        } else {
                                            i4 = R.string.EventLogChangedChannelLink;
                                            str5 = "EventLogChangedChannelLink";
                                        }
                                        this.messageText = replaceWithLink(LocaleController.getString(str5, i4), "un1", user2);
                                    } else {
                                        if (tLRPC$Chat.megagroup) {
                                            i3 = R.string.EventLogRemovedGroupLink;
                                            str4 = "EventLogRemovedGroupLink";
                                        } else {
                                            i3 = R.string.EventLogRemovedChannelLink;
                                            str4 = "EventLogRemovedChannelLink";
                                        }
                                        this.messageText = replaceWithLink(LocaleController.getString(str4, i3), "un1", user2);
                                    }
                                    TLRPC$TL_message tLRPC$TL_message2 = new TLRPC$TL_message();
                                    tLRPC$TL_message2.out = false;
                                    tLRPC$TL_message2.unread = false;
                                    TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                                    tLRPC$TL_message2.from_id = tLRPC$TL_peerUser3;
                                    tLRPC$TL_peerUser3.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                    tLRPC$TL_message2.peer_id = tLRPC$TL_peerChannel;
                                    tLRPC$TL_message2.date = tLRPC$TL_channelAdminLogEvent2.date;
                                    if (!TextUtils.isEmpty(str9)) {
                                        tLRPC$TL_message2.message = "https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + str9;
                                    } else {
                                        tLRPC$TL_message2.message = str;
                                    }
                                    TLRPC$TL_messageEntityUrl tLRPC$TL_messageEntityUrl = new TLRPC$TL_messageEntityUrl();
                                    tLRPC$TL_messageEntityUrl.offset = 0;
                                    tLRPC$TL_messageEntityUrl.length = tLRPC$TL_message2.message.length();
                                    tLRPC$TL_message2.entities.add(tLRPC$TL_messageEntityUrl);
                                    if (!TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$TL_channelAdminLogEvent2.action).prev_value)) {
                                        TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage3 = new TLRPC$TL_messageMediaWebPage();
                                        tLRPC$TL_message2.media = tLRPC$TL_messageMediaWebPage3;
                                        tLRPC$TL_messageMediaWebPage3.webpage = new TLRPC$TL_webPage();
                                        TLRPC$WebPage tLRPC$WebPage4 = tLRPC$TL_message2.media.webpage;
                                        tLRPC$WebPage4.flags = 10;
                                        tLRPC$WebPage4.display_url = str;
                                        tLRPC$WebPage4.url = str;
                                        tLRPC$WebPage4.site_name = LocaleController.getString("EventLogPreviousLink", R.string.EventLogPreviousLink);
                                        tLRPC$TL_message2.media.webpage.description = "https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                    } else {
                                        tLRPC$TL_message2.media = new TLRPC$TL_messageMediaEmpty();
                                    }
                                    tLRPC$Message = tLRPC$TL_message2;
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionEditMessage) {
                                    TLRPC$Message tLRPC$TL_message3 = new TLRPC$TL_message();
                                    tLRPC$TL_message3.out = false;
                                    tLRPC$TL_message3.unread = false;
                                    tLRPC$TL_message3.peer_id = tLRPC$TL_peerChannel;
                                    tLRPC$TL_message3.date = tLRPC$TL_channelAdminLogEvent2.date;
                                    TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction4 = tLRPC$TL_channelAdminLogEvent2.action;
                                    TLRPC$Message tLRPC$Message3 = ((TLRPC$TL_channelAdminLogEventActionEditMessage) tLRPC$ChannelAdminLogEventAction4).new_message;
                                    TLRPC$Message tLRPC$Message4 = ((TLRPC$TL_channelAdminLogEventActionEditMessage) tLRPC$ChannelAdminLogEventAction4).prev_message;
                                    if (tLRPC$Message3 != null && (tLRPC$Peer = tLRPC$Message3.from_id) != null) {
                                        tLRPC$TL_message3.from_id = tLRPC$Peer;
                                    } else {
                                        TLRPC$TL_peerUser tLRPC$TL_peerUser4 = new TLRPC$TL_peerUser();
                                        tLRPC$TL_message3.from_id = tLRPC$TL_peerUser4;
                                        tLRPC$TL_peerUser4.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                    }
                                    if (getMedia(tLRPC$Message3) != null && !(getMedia(tLRPC$Message3) instanceof TLRPC$TL_messageMediaEmpty) && !(getMedia(tLRPC$Message3) instanceof TLRPC$TL_messageMediaWebPage)) {
                                        boolean z4 = !TextUtils.equals(tLRPC$Message3.message, tLRPC$Message4.message);
                                        if (((getMedia(tLRPC$Message3).getClass() == tLRPC$Message4.media.getClass() && (getMedia(tLRPC$Message3).photo == null || tLRPC$Message4.media.photo == null || getMedia(tLRPC$Message3).photo.id == tLRPC$Message4.media.photo.id) && (getMedia(tLRPC$Message3).document == null || tLRPC$Message4.media.document == null || getMedia(tLRPC$Message3).document.id == tLRPC$Message4.media.document.id)) ? false : true) && z4) {
                                            this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMediaCaption", R.string.EventLogEditedMediaCaption), "un1", user2);
                                        } else if (z4) {
                                            this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedCaption", R.string.EventLogEditedCaption), "un1", user2);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMedia", R.string.EventLogEditedMedia), "un1", user2);
                                        }
                                        TLRPC$MessageMedia media = getMedia(tLRPC$Message3);
                                        tLRPC$TL_message3.media = media;
                                        if (z4) {
                                            media.webpage = new TLRPC$TL_webPage();
                                            tLRPC$TL_message3.media.webpage.site_name = LocaleController.getString("EventLogOriginalCaption", R.string.EventLogOriginalCaption);
                                            if (TextUtils.isEmpty(tLRPC$Message4.message)) {
                                                tLRPC$TL_message3.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", R.string.EventLogOriginalCaptionEmpty);
                                            } else {
                                                tLRPC$TL_message3.media.webpage.description = tLRPC$Message4.message;
                                                arrayList2 = tLRPC$Message4.entities;
                                                tLRPC$TL_message3.reply_markup = tLRPC$Message3.reply_markup;
                                                tLRPC$WebPage = tLRPC$TL_message3.media.webpage;
                                                if (tLRPC$WebPage != null) {
                                                }
                                                arrayList3 = arrayList2;
                                                tLRPC$Message = tLRPC$TL_message3;
                                            }
                                        }
                                        arrayList2 = null;
                                        tLRPC$TL_message3.reply_markup = tLRPC$Message3.reply_markup;
                                        tLRPC$WebPage = tLRPC$TL_message3.media.webpage;
                                        if (tLRPC$WebPage != null) {
                                        }
                                        arrayList3 = arrayList2;
                                        tLRPC$Message = tLRPC$TL_message3;
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMessages", R.string.EventLogEditedMessages), "un1", user2);
                                        if (tLRPC$Message3.action instanceof TLRPC$TL_messageActionGroupCall) {
                                            tLRPC$Message3.media = new TLRPC$TL_messageMediaEmpty();
                                            tLRPC$TL_message3 = tLRPC$Message3;
                                        } else {
                                            tLRPC$TL_message3.message = tLRPC$Message3.message;
                                            tLRPC$TL_message3.entities = tLRPC$Message3.entities;
                                            TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage4 = new TLRPC$TL_messageMediaWebPage();
                                            tLRPC$TL_message3.media = tLRPC$TL_messageMediaWebPage4;
                                            tLRPC$TL_messageMediaWebPage4.webpage = new TLRPC$TL_webPage();
                                            tLRPC$TL_message3.media.webpage.site_name = LocaleController.getString("EventLogOriginalMessages", R.string.EventLogOriginalMessages);
                                            if (TextUtils.isEmpty(tLRPC$Message4.message)) {
                                                tLRPC$TL_message3.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", R.string.EventLogOriginalCaptionEmpty);
                                            } else {
                                                tLRPC$TL_message3.media.webpage.description = tLRPC$Message4.message;
                                                arrayList2 = tLRPC$Message4.entities;
                                                tLRPC$TL_message3.reply_markup = tLRPC$Message3.reply_markup;
                                                tLRPC$WebPage = tLRPC$TL_message3.media.webpage;
                                                if (tLRPC$WebPage != null) {
                                                    tLRPC$WebPage.flags = 10;
                                                    tLRPC$WebPage.display_url = str;
                                                    tLRPC$WebPage.url = str;
                                                }
                                                arrayList3 = arrayList2;
                                                tLRPC$Message = tLRPC$TL_message3;
                                            }
                                        }
                                        arrayList2 = null;
                                        tLRPC$TL_message3.reply_markup = tLRPC$Message3.reply_markup;
                                        tLRPC$WebPage = tLRPC$TL_message3.media.webpage;
                                        if (tLRPC$WebPage != null) {
                                        }
                                        arrayList3 = arrayList2;
                                        tLRPC$Message = tLRPC$TL_message3;
                                    }
                                    if (this.messageOwner == null) {
                                        this.messageOwner = new TLRPC$TL_messageService();
                                    }
                                    this.messageOwner.message = this.messageText.toString();
                                    this.messageOwner.from_id = new TLRPC$TL_peerUser();
                                    TLRPC$Message tLRPC$Message5 = this.messageOwner;
                                    tLRPC$Message5.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                    tLRPC$Message5.date = tLRPC$TL_channelAdminLogEvent2.date;
                                    int i21 = iArr[0];
                                    iArr[0] = i21 + 1;
                                    tLRPC$Message5.id = i21;
                                    this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                    tLRPC$Message5.out = false;
                                    tLRPC$Message5.peer_id = new TLRPC$TL_peerChannel();
                                    TLRPC$Message tLRPC$Message6 = this.messageOwner;
                                    tLRPC$Message6.peer_id.channel_id = tLRPC$Chat.id;
                                    tLRPC$Message6.unread = false;
                                    MediaController mediaController = MediaController.getInstance();
                                    tLRPC$Message = tLRPC$Message instanceof TLRPC$TL_messageEmpty ? null : tLRPC$Message;
                                    if (tLRPC$Message == null) {
                                        tLRPC$Message.out = false;
                                        int i22 = iArr[0];
                                        iArr[0] = i22 + 1;
                                        tLRPC$Message.id = i22;
                                        int i23 = tLRPC$Message.flags & (-9);
                                        tLRPC$Message.flags = i23;
                                        tLRPC$Message.reply_to = null;
                                        tLRPC$Message.flags = i23 & (-32769);
                                        MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$Message, (AbstractMap<Long, TLRPC$User>) null, (AbstractMap<Long, TLRPC$Chat>) null, true, true, this.eventId);
                                        if (messageObject.contentType >= 0) {
                                            if (mediaController.isPlayingMessage(messageObject)) {
                                                MessageObject playingMessageObject = mediaController.getPlayingMessageObject();
                                                messageObject.audioProgress = playingMessageObject.audioProgress;
                                                messageObject.audioProgressSec = playingMessageObject.audioProgressSec;
                                            }
                                            createDateArray(this.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                            if (z) {
                                                arrayList4 = arrayList;
                                                arrayList4.add(0, messageObject);
                                            } else {
                                                arrayList4 = arrayList;
                                                arrayList4.add(arrayList.size() - 1, messageObject);
                                            }
                                        } else {
                                            arrayList4 = arrayList;
                                            this.contentType = -1;
                                        }
                                        if (arrayList3 != null) {
                                            messageObject.webPageDescriptionEntities = arrayList3;
                                            iArr2 = null;
                                            messageObject.linkDescription = null;
                                            messageObject.generateLinkDescription();
                                            if (this.contentType < 0) {
                                                return;
                                            }
                                            createDateArray(this.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                            if (z) {
                                                arrayList4.add(0, this);
                                            } else {
                                                arrayList4.add(arrayList.size() - 1, this);
                                            }
                                            if (this.messageText == null) {
                                                this.messageText = str;
                                            }
                                            if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                                                textPaint = Theme.chat_msgGameTextPaint;
                                            } else {
                                                textPaint = Theme.chat_msgTextPaint;
                                            }
                                            iArr2 = allowsBigEmoji() ? new int[1] : iArr2;
                                            CharSequence replaceEmoji = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, iArr2);
                                            this.messageText = replaceEmoji;
                                            Spannable replaceAnimatedEmoji = replaceAnimatedEmoji(replaceEmoji, this.messageOwner.entities, textPaint.getFontMetricsInt());
                                            this.messageText = replaceAnimatedEmoji;
                                            if (iArr2 != null && iArr2[0] > 1) {
                                                replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr2);
                                            }
                                            checkEmojiOnly(iArr2);
                                            setType();
                                            measureInlineBotButtons();
                                            generateCaption();
                                            if (mediaController.isPlayingMessage(this)) {
                                                MessageObject playingMessageObject2 = mediaController.getPlayingMessageObject();
                                                this.audioProgress = playingMessageObject2.audioProgress;
                                                this.audioProgressSec = playingMessageObject2.audioProgressSec;
                                            }
                                            generateLayout(user2);
                                            this.layoutCreated = true;
                                            generateThumbs(false);
                                            checkMediaExistance();
                                            return;
                                        }
                                    } else {
                                        arrayList4 = arrayList;
                                    }
                                    iArr2 = null;
                                    if (this.contentType < 0) {
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeStickerSet) {
                                    TLRPC$InputStickerSet tLRPC$InputStickerSet = ((TLRPC$TL_channelAdminLogEventActionChangeStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                    TLRPC$InputStickerSet tLRPC$InputStickerSet2 = ((TLRPC$TL_channelAdminLogEventActionChangeStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                    if (tLRPC$InputStickerSet == null || (tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedStickersSet", R.string.EventLogRemovedStickersSet), "un1", user2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogChangedStickersSet", R.string.EventLogChangedStickersSet), "un1", user2);
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLocation) {
                                    TLRPC$ChannelLocation tLRPC$ChannelLocation = ((TLRPC$TL_channelAdminLogEventActionChangeLocation) tLRPC$ChannelAdminLogEventAction).new_value;
                                    if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocationEmpty) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedLocation", R.string.EventLogRemovedLocation), "un1", user2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.formatString("EventLogChangedLocation", R.string.EventLogChangedLocation, ((TLRPC$TL_channelLocation) tLRPC$ChannelLocation).address), "un1", user2);
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSlowMode) {
                                    int i24 = ((TLRPC$TL_channelAdminLogEventActionToggleSlowMode) tLRPC$ChannelAdminLogEventAction).new_value;
                                    if (i24 == 0) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSlowmodeOff", R.string.EventLogToggledSlowmodeOff), "un1", user2);
                                    } else {
                                        if (i24 < 60) {
                                            c3 = 0;
                                            formatPluralString2 = LocaleController.formatPluralString("Seconds", i24, new Object[0]);
                                        } else {
                                            c3 = 0;
                                            if (i24 < 3600) {
                                                formatPluralString2 = LocaleController.formatPluralString("Minutes", i24 / 60, new Object[0]);
                                            } else {
                                                formatPluralString2 = LocaleController.formatPluralString("Hours", (i24 / 60) / 60, new Object[0]);
                                            }
                                        }
                                        int i25 = R.string.EventLogToggledSlowmodeOn;
                                        Object[] objArr = new Object[1];
                                        objArr[c3] = formatPluralString2;
                                        this.messageText = replaceWithLink(LocaleController.formatString("EventLogToggledSlowmodeOn", i25, objArr), "un1", user2);
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStartGroupCall) {
                                    if (ChatObject.isChannel(tLRPC$Chat) && (!tLRPC$Chat.megagroup || tLRPC$Chat.gigagroup)) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogStartedLiveStream", R.string.EventLogStartedLiveStream), "un1", user2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogStartedVoiceChat", R.string.EventLogStartedVoiceChat), "un1", user2);
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDiscardGroupCall) {
                                    if (ChatObject.isChannel(tLRPC$Chat) && (!tLRPC$Chat.megagroup || tLRPC$Chat.gigagroup)) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEndedLiveStream", R.string.EventLogEndedLiveStream), "un1", user2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEndedVoiceChat", R.string.EventLogEndedVoiceChat), "un1", user2);
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantMute) {
                                    long peerId4 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantMute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                    if (peerId4 > 0) {
                                        chat3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId4));
                                    } else {
                                        chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId4));
                                    }
                                    CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString("EventLogVoiceChatMuted", R.string.EventLogVoiceChatMuted), "un1", user2);
                                    this.messageText = replaceWithLink6;
                                    this.messageText = replaceWithLink(replaceWithLink6, "un2", chat3);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantUnmute) {
                                    long peerId5 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantUnmute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                    if (peerId5 > 0) {
                                        chat2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId5));
                                    } else {
                                        chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId5));
                                    }
                                    CharSequence replaceWithLink7 = replaceWithLink(LocaleController.getString("EventLogVoiceChatUnmuted", R.string.EventLogVoiceChatUnmuted), "un1", user2);
                                    this.messageText = replaceWithLink7;
                                    this.messageText = replaceWithLink(replaceWithLink7, "un2", chat2);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) {
                                    if (((TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) tLRPC$ChannelAdminLogEventAction).join_muted) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogVoiceChatNotAllowedToSpeak", R.string.EventLogVoiceChatNotAllowedToSpeak), "un1", user2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogVoiceChatAllowedToSpeak", R.string.EventLogVoiceChatAllowedToSpeak), "un1", user2);
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) {
                                    TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite = (TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) tLRPC$ChannelAdminLogEventAction;
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionInviteUser", R.string.ActionInviteUser), "un1", user2);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleNoForwards) {
                                    TLRPC$TL_channelAdminLogEventActionToggleNoForwards tLRPC$TL_channelAdminLogEventActionToggleNoForwards = (TLRPC$TL_channelAdminLogEventActionToggleNoForwards) tLRPC$ChannelAdminLogEventAction;
                                    boolean z5 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup;
                                    if (tLRPC$TL_channelAdminLogEventActionToggleNoForwards.new_value) {
                                        if (z5) {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionForwardsRestrictedChannel", R.string.ActionForwardsRestrictedChannel), "un1", user2);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionForwardsRestrictedGroup", R.string.ActionForwardsRestrictedGroup), "un1", user2);
                                        }
                                    } else if (z5) {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionForwardsEnabledChannel", R.string.ActionForwardsEnabledChannel), "un1", user2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionForwardsEnabledGroup", R.string.ActionForwardsEnabledGroup), "un1", user2);
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteDelete) {
                                    CharSequence replaceWithLink8 = replaceWithLink(LocaleController.formatString("ActionDeletedInviteLinkClickable", R.string.ActionDeletedInviteLinkClickable, new Object[0]), "un1", user2);
                                    this.messageText = replaceWithLink8;
                                    this.messageText = replaceWithLink(replaceWithLink8, "un2", ((TLRPC$TL_channelAdminLogEventActionExportedInviteDelete) tLRPC$ChannelAdminLogEventAction).invite);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke) {
                                    TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke = (TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke) tLRPC$ChannelAdminLogEventAction;
                                    CharSequence replaceWithLink9 = replaceWithLink(LocaleController.formatString("ActionRevokedInviteLinkClickable", R.string.ActionRevokedInviteLinkClickable, tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke.invite.link), "un1", user2);
                                    this.messageText = replaceWithLink9;
                                    this.messageText = replaceWithLink(replaceWithLink9, "un2", tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke.invite);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteEdit) {
                                    TLRPC$TL_channelAdminLogEventActionExportedInviteEdit tLRPC$TL_channelAdminLogEventActionExportedInviteEdit = (TLRPC$TL_channelAdminLogEventActionExportedInviteEdit) tLRPC$ChannelAdminLogEventAction;
                                    String str10 = tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite.link;
                                    if (str10 != null && str10.equals(tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite.link)) {
                                        this.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkToSameClickable", R.string.ActionEditedInviteLinkToSameClickable, new Object[0]), "un1", user2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkClickable", R.string.ActionEditedInviteLinkClickable, new Object[0]), "un1", user2);
                                    }
                                    CharSequence replaceWithLink10 = replaceWithLink(this.messageText, "un2", tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite);
                                    this.messageText = replaceWithLink10;
                                    this.messageText = replaceWithLink(replaceWithLink10, "un3", tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantVolume) {
                                    TLRPC$TL_channelAdminLogEventActionParticipantVolume tLRPC$TL_channelAdminLogEventActionParticipantVolume = (TLRPC$TL_channelAdminLogEventActionParticipantVolume) tLRPC$ChannelAdminLogEventAction;
                                    long peerId6 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant.peer);
                                    if (peerId6 > 0) {
                                        chat = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId6));
                                    } else {
                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId6));
                                    }
                                    double participantVolume = ChatObject.getParticipantVolume(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant);
                                    Double.isNaN(participantVolume);
                                    double d = participantVolume / 100.0d;
                                    int i26 = R.string.ActionVolumeChanged;
                                    Object[] objArr2 = new Object[1];
                                    objArr2[0] = Integer.valueOf((int) (d > 0.0d ? Math.max(d, 1.0d) : 0.0d));
                                    CharSequence replaceWithLink11 = replaceWithLink(LocaleController.formatString("ActionVolumeChanged", i26, objArr2), "un1", user2);
                                    this.messageText = replaceWithLink11;
                                    this.messageText = replaceWithLink(replaceWithLink11, "un2", chat);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) {
                                    TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL = (TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) tLRPC$ChannelAdminLogEventAction;
                                    if (!tLRPC$Chat.megagroup) {
                                        int i27 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                        if (i27 != 0) {
                                            this.messageText = LocaleController.formatString("ActionTTLChannelChanged", R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i27));
                                        } else {
                                            this.messageText = LocaleController.getString("ActionTTLChannelDisabled", R.string.ActionTTLChannelDisabled);
                                        }
                                    } else {
                                        int i28 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                        if (i28 == 0) {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionTTLDisabled", R.string.ActionTTLDisabled), "un1", user2);
                                        } else {
                                            if (i28 > 86400) {
                                                c2 = 0;
                                                formatPluralString = LocaleController.formatPluralString("Days", i28 / 86400, new Object[0]);
                                            } else {
                                                c2 = 0;
                                                if (i28 >= 3600) {
                                                    formatPluralString = LocaleController.formatPluralString("Hours", i28 / 3600, new Object[0]);
                                                } else if (i28 >= 60) {
                                                    formatPluralString = LocaleController.formatPluralString("Minutes", i28 / 60, new Object[0]);
                                                } else {
                                                    formatPluralString = LocaleController.formatPluralString("Seconds", i28, new Object[0]);
                                                }
                                            }
                                            int i29 = R.string.ActionTTLChanged;
                                            Object[] objArr3 = new Object[1];
                                            objArr3[c2] = formatPluralString;
                                            this.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", i29, objArr3), "un1", user2);
                                        }
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) {
                                    TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest = (TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) tLRPC$ChannelAdminLogEventAction;
                                    TLRPC$ExportedChatInvite tLRPC$ExportedChatInvite = tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite;
                                    if (((tLRPC$ExportedChatInvite instanceof TLRPC$TL_chatInviteExported) && "https://t.me/+PublicChat".equals(((TLRPC$TL_chatInviteExported) tLRPC$ExportedChatInvite).link)) || (tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite instanceof TLRPC$TL_chatInvitePublicJoinRequests)) {
                                        CharSequence replaceWithLink12 = replaceWithLink(LocaleController.getString("JoinedViaRequestApproved", R.string.JoinedViaRequestApproved), "un1", user2);
                                        this.messageText = replaceWithLink12;
                                        this.messageText = replaceWithLink(replaceWithLink12, "un2", MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by)));
                                    } else {
                                        CharSequence replaceWithLink13 = replaceWithLink(LocaleController.getString("JoinedViaInviteLinkApproved", R.string.JoinedViaInviteLinkApproved), "un1", user2);
                                        this.messageText = replaceWithLink13;
                                        CharSequence replaceWithLink14 = replaceWithLink(replaceWithLink13, "un2", tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite);
                                        this.messageText = replaceWithLink14;
                                        this.messageText = replaceWithLink(replaceWithLink14, "un3", MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by)));
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionSendMessage) {
                                    tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionSendMessage) tLRPC$ChannelAdminLogEventAction).message;
                                    this.messageText = replaceWithLink(LocaleController.getString("EventLogSendMessages", R.string.EventLogSendMessages), "un1", user2);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) {
                                    TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions = (TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) tLRPC$ChannelAdminLogEventAction;
                                    CharSequence stringFrom = getStringFrom(tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.prev_value);
                                    CharSequence stringFrom2 = getStringFrom(tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.new_value);
                                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(replaceWithLink(LocaleController.formatString("ActionReactionsChanged", R.string.ActionReactionsChanged, "**old**", "**new**"), "un1", user2));
                                    int indexOf = spannableStringBuilder.toString().indexOf("**old**");
                                    if (indexOf > 0) {
                                        spannableStringBuilder.replace(indexOf, indexOf + 7, stringFrom);
                                    }
                                    int indexOf2 = spannableStringBuilder.toString().indexOf("**new**");
                                    if (indexOf2 > 0) {
                                        spannableStringBuilder.replace(indexOf2, indexOf2 + 7, stringFrom2);
                                    }
                                    this.messageText = spannableStringBuilder;
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeUsernames) {
                                    TLRPC$TL_channelAdminLogEventActionChangeUsernames tLRPC$TL_channelAdminLogEventActionChangeUsernames = (TLRPC$TL_channelAdminLogEventActionChangeUsernames) tLRPC$ChannelAdminLogEventAction;
                                    ArrayList<String> arrayList5 = tLRPC$TL_channelAdminLogEventActionChangeUsernames.prev_value;
                                    ArrayList<String> arrayList6 = tLRPC$TL_channelAdminLogEventActionChangeUsernames.new_value;
                                    this.messageText = null;
                                    if (arrayList5 != null && arrayList6 != null) {
                                        if (arrayList6.size() + 1 == arrayList5.size()) {
                                            String str11 = null;
                                            int i30 = 0;
                                            while (true) {
                                                if (i30 >= arrayList5.size()) {
                                                    break;
                                                }
                                                String str12 = arrayList5.get(i30);
                                                if (!arrayList6.contains(str12)) {
                                                    if (str11 != null) {
                                                        str11 = null;
                                                        break;
                                                    }
                                                    str11 = str12;
                                                }
                                                i30++;
                                            }
                                            if (str11 != null) {
                                                this.messageText = replaceWithLink(LocaleController.formatString("EventLogDeactivatedUsername", R.string.EventLogDeactivatedUsername, "@" + str11), "un1", user2);
                                            }
                                        } else if (arrayList5.size() + 1 == arrayList6.size()) {
                                            String str13 = null;
                                            int i31 = 0;
                                            while (true) {
                                                if (i31 >= arrayList6.size()) {
                                                    break;
                                                }
                                                String str14 = arrayList6.get(i31);
                                                if (!arrayList5.contains(str14)) {
                                                    if (str13 != null) {
                                                        str13 = null;
                                                        break;
                                                    }
                                                    str13 = str14;
                                                }
                                                i31++;
                                            }
                                            if (str13 != null) {
                                                this.messageText = replaceWithLink(LocaleController.formatString("EventLogActivatedUsername", R.string.EventLogActivatedUsername, "@" + str13), "un1", user2);
                                            }
                                        }
                                    }
                                    if (this.messageText == null) {
                                        this.messageText = replaceWithLink(LocaleController.formatString("EventLogChangeUsernames", R.string.EventLogChangeUsernames, getUsernamesString(arrayList5), getUsernamesString(arrayList6)), "un1", user2);
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleForum) {
                                    if (((TLRPC$TL_channelAdminLogEventActionToggleForum) tLRPC$ChannelAdminLogEventAction).new_value) {
                                        this.messageText = replaceWithLink(LocaleController.formatString("EventLogSwitchToForum", R.string.EventLogSwitchToForum, new Object[0]), "un1", user2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.formatString("EventLogSwitchToGroup", R.string.EventLogSwitchToGroup, new Object[0]), "un1", user2);
                                    }
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionCreateTopic) {
                                    CharSequence replaceWithLink15 = replaceWithLink(LocaleController.formatString("EventLogCreateTopic", R.string.EventLogCreateTopic, new Object[0]), "un1", user2);
                                    this.messageText = replaceWithLink15;
                                    this.messageText = replaceWithLink(replaceWithLink15, "un2", ((TLRPC$TL_channelAdminLogEventActionCreateTopic) tLRPC$ChannelAdminLogEventAction).topic);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionEditTopic) {
                                    TLRPC$TL_channelAdminLogEventActionEditTopic tLRPC$TL_channelAdminLogEventActionEditTopic = (TLRPC$TL_channelAdminLogEventActionEditTopic) tLRPC$ChannelAdminLogEventAction;
                                    CharSequence replaceWithLink16 = replaceWithLink(LocaleController.formatString("EventLogEditTopic", R.string.EventLogEditTopic, new Object[0]), "un1", user2);
                                    this.messageText = replaceWithLink16;
                                    CharSequence replaceWithLink17 = replaceWithLink(replaceWithLink16, "un2", tLRPC$TL_channelAdminLogEventActionEditTopic.prev_topic);
                                    this.messageText = replaceWithLink17;
                                    this.messageText = replaceWithLink(replaceWithLink17, "un3", tLRPC$TL_channelAdminLogEventActionEditTopic.new_topic);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDeleteTopic) {
                                    CharSequence replaceWithLink18 = replaceWithLink(LocaleController.getString("EventLogDeleteTopic", R.string.EventLogDeleteTopic), "un1", user2);
                                    this.messageText = replaceWithLink18;
                                    this.messageText = replaceWithLink(replaceWithLink18, "un2", ((TLRPC$TL_channelAdminLogEventActionDeleteTopic) tLRPC$ChannelAdminLogEventAction).topic);
                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionPinTopic) {
                                    TLRPC$TL_channelAdminLogEventActionPinTopic tLRPC$TL_channelAdminLogEventActionPinTopic = (TLRPC$TL_channelAdminLogEventActionPinTopic) tLRPC$ChannelAdminLogEventAction;
                                    TLRPC$ForumTopic tLRPC$ForumTopic = tLRPC$TL_channelAdminLogEventActionPinTopic.new_topic;
                                    if ((tLRPC$ForumTopic instanceof TLRPC$TL_forumTopic) && ((TLRPC$TL_forumTopic) tLRPC$ForumTopic).pinned) {
                                        CharSequence replaceWithLink19 = replaceWithLink(LocaleController.formatString("EventLogPinTopic", R.string.EventLogPinTopic, new Object[0]), "un1", user2);
                                        this.messageText = replaceWithLink19;
                                        this.messageText = replaceWithLink(replaceWithLink19, "un2", tLRPC$TL_channelAdminLogEventActionPinTopic.new_topic);
                                    } else {
                                        CharSequence replaceWithLink20 = replaceWithLink(LocaleController.formatString("EventLogUnpinTopic", R.string.EventLogUnpinTopic, new Object[0]), "un1", user2);
                                        this.messageText = replaceWithLink20;
                                        this.messageText = replaceWithLink(replaceWithLink20, "un2", tLRPC$TL_channelAdminLogEventActionPinTopic.new_topic);
                                    }
                                } else {
                                    this.messageText = "unsupported " + tLRPC$TL_channelAdminLogEvent2.action;
                                }
                                arrayList3 = null;
                                if (this.messageOwner == null) {
                                }
                                this.messageOwner.message = this.messageText.toString();
                                this.messageOwner.from_id = new TLRPC$TL_peerUser();
                                TLRPC$Message tLRPC$Message52 = this.messageOwner;
                                tLRPC$Message52.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                tLRPC$Message52.date = tLRPC$TL_channelAdminLogEvent2.date;
                                int i212 = iArr[0];
                                iArr[0] = i212 + 1;
                                tLRPC$Message52.id = i212;
                                this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                tLRPC$Message52.out = false;
                                tLRPC$Message52.peer_id = new TLRPC$TL_peerChannel();
                                TLRPC$Message tLRPC$Message62 = this.messageOwner;
                                tLRPC$Message62.peer_id.channel_id = tLRPC$Chat.id;
                                tLRPC$Message62.unread = false;
                                MediaController mediaController2 = MediaController.getInstance();
                                if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
                                }
                                if (tLRPC$Message == null) {
                                }
                                iArr2 = null;
                                if (this.contentType < 0) {
                                }
                            }
                        }
                        tLRPC$Message = tLRPC$TL_message;
                        arrayList3 = null;
                        if (this.messageOwner == null) {
                        }
                        this.messageOwner.message = this.messageText.toString();
                        this.messageOwner.from_id = new TLRPC$TL_peerUser();
                        TLRPC$Message tLRPC$Message522 = this.messageOwner;
                        tLRPC$Message522.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                        tLRPC$Message522.date = tLRPC$TL_channelAdminLogEvent2.date;
                        int i2122 = iArr[0];
                        iArr[0] = i2122 + 1;
                        tLRPC$Message522.id = i2122;
                        this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                        tLRPC$Message522.out = false;
                        tLRPC$Message522.peer_id = new TLRPC$TL_peerChannel();
                        TLRPC$Message tLRPC$Message622 = this.messageOwner;
                        tLRPC$Message622.peer_id.channel_id = tLRPC$Chat.id;
                        tLRPC$Message622.unread = false;
                        MediaController mediaController22 = MediaController.getInstance();
                        if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
                        }
                        if (tLRPC$Message == null) {
                        }
                        iArr2 = null;
                        if (this.contentType < 0) {
                        }
                    }
                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                    str = str3;
                    arrayList3 = null;
                    if (this.messageOwner == null) {
                    }
                    this.messageOwner.message = this.messageText.toString();
                    this.messageOwner.from_id = new TLRPC$TL_peerUser();
                    TLRPC$Message tLRPC$Message5222 = this.messageOwner;
                    tLRPC$Message5222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                    tLRPC$Message5222.date = tLRPC$TL_channelAdminLogEvent2.date;
                    int i21222 = iArr[0];
                    iArr[0] = i21222 + 1;
                    tLRPC$Message5222.id = i21222;
                    this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                    tLRPC$Message5222.out = false;
                    tLRPC$Message5222.peer_id = new TLRPC$TL_peerChannel();
                    TLRPC$Message tLRPC$Message6222 = this.messageOwner;
                    tLRPC$Message6222.peer_id.channel_id = tLRPC$Chat.id;
                    tLRPC$Message6222.unread = false;
                    MediaController mediaController222 = MediaController.getInstance();
                    if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
                    }
                    if (tLRPC$Message == null) {
                    }
                    iArr2 = null;
                    if (this.contentType < 0) {
                    }
                }
                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                str = str3;
            }
            tLRPC$Message = null;
            arrayList3 = null;
            if (this.messageOwner == null) {
            }
            this.messageOwner.message = this.messageText.toString();
            this.messageOwner.from_id = new TLRPC$TL_peerUser();
            TLRPC$Message tLRPC$Message52222 = this.messageOwner;
            tLRPC$Message52222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
            tLRPC$Message52222.date = tLRPC$TL_channelAdminLogEvent2.date;
            int i212222 = iArr[0];
            iArr[0] = i212222 + 1;
            tLRPC$Message52222.id = i212222;
            this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
            tLRPC$Message52222.out = false;
            tLRPC$Message52222.peer_id = new TLRPC$TL_peerChannel();
            TLRPC$Message tLRPC$Message62222 = this.messageOwner;
            tLRPC$Message62222.peer_id.channel_id = tLRPC$Chat.id;
            tLRPC$Message62222.unread = false;
            MediaController mediaController2222 = MediaController.getInstance();
            if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
            }
            if (tLRPC$Message == null) {
            }
            iArr2 = null;
            if (this.contentType < 0) {
            }
        }
        str = "";
        tLRPC$Message = null;
        arrayList3 = null;
        if (this.messageOwner == null) {
        }
        this.messageOwner.message = this.messageText.toString();
        this.messageOwner.from_id = new TLRPC$TL_peerUser();
        TLRPC$Message tLRPC$Message522222 = this.messageOwner;
        tLRPC$Message522222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
        tLRPC$Message522222.date = tLRPC$TL_channelAdminLogEvent2.date;
        int i2122222 = iArr[0];
        iArr[0] = i2122222 + 1;
        tLRPC$Message522222.id = i2122222;
        this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
        tLRPC$Message522222.out = false;
        tLRPC$Message522222.peer_id = new TLRPC$TL_peerChannel();
        TLRPC$Message tLRPC$Message622222 = this.messageOwner;
        tLRPC$Message622222.peer_id.channel_id = tLRPC$Chat.id;
        tLRPC$Message622222.unread = false;
        MediaController mediaController22222 = MediaController.getInstance();
        if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
        }
        if (tLRPC$Message == null) {
        }
        iArr2 = null;
        if (this.contentType < 0) {
        }
    }

    private CharSequence getStringFrom(TLRPC$ChatReactions tLRPC$ChatReactions) {
        if (tLRPC$ChatReactions instanceof TLRPC$TL_chatReactionsAll) {
            return LocaleController.getString("AllReactions", R.string.AllReactions);
        }
        if (tLRPC$ChatReactions instanceof TLRPC$TL_chatReactionsSome) {
            TLRPC$TL_chatReactionsSome tLRPC$TL_chatReactionsSome = (TLRPC$TL_chatReactionsSome) tLRPC$ChatReactions;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (int i = 0; i < tLRPC$TL_chatReactionsSome.reactions.size(); i++) {
                if (i != 0) {
                    spannableStringBuilder.append((CharSequence) ", ");
                }
                spannableStringBuilder.append(ReactionsUtils.reactionToCharSequence(tLRPC$TL_chatReactionsSome.reactions.get(i)));
            }
        }
        return LocaleController.getString("NoReactions", R.string.NoReactions);
    }

    private String getUsernamesString(ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return LocaleController.getString("UsernameEmpty", R.string.UsernameEmpty).toLowerCase();
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

    private String getUserName(TLObject tLObject, ArrayList<TLRPC$MessageEntity> arrayList, int i) {
        String str;
        String publicUsername;
        long j;
        String str2;
        long j2;
        String formatName;
        if (tLObject == null) {
            str2 = null;
            j2 = 0;
            str = "";
        } else {
            if (tLObject instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                if (tLRPC$User.deleted) {
                    formatName = LocaleController.getString("HiddenName", R.string.HiddenName);
                } else {
                    formatName = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                }
                str = formatName;
                publicUsername = tLRPC$User.username;
                j = tLRPC$User.id;
            } else {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLObject;
                str = tLRPC$Chat.title;
                publicUsername = ChatObject.getPublicUsername(tLRPC$Chat);
                j = -tLRPC$Chat.id;
            }
            str2 = publicUsername;
            j2 = j;
        }
        if (i >= 0) {
            TLRPC$TL_messageEntityMentionName tLRPC$TL_messageEntityMentionName = new TLRPC$TL_messageEntityMentionName();
            tLRPC$TL_messageEntityMentionName.user_id = j2;
            tLRPC$TL_messageEntityMentionName.offset = i;
            tLRPC$TL_messageEntityMentionName.length = str.length();
            arrayList.add(tLRPC$TL_messageEntityMentionName);
        }
        if (!TextUtils.isEmpty(str2)) {
            if (i >= 0) {
                TLRPC$TL_messageEntityMentionName tLRPC$TL_messageEntityMentionName2 = new TLRPC$TL_messageEntityMentionName();
                tLRPC$TL_messageEntityMentionName2.user_id = j2;
                tLRPC$TL_messageEntityMentionName2.offset = i + str.length() + 2;
                tLRPC$TL_messageEntityMentionName2.length = str2.length() + 1;
                arrayList.add(tLRPC$TL_messageEntityMentionName2);
            }
            return String.format("%1$s (@%2$s)", str, str2);
        }
        return str;
    }

    public void applyNewText() {
        applyNewText(this.messageOwner.message);
    }

    public void applyNewText(CharSequence charSequence) {
        TextPaint textPaint;
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        int[] iArr = null;
        TLRPC$User user = isFromUser() ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id)) : null;
        this.messageText = charSequence;
        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
            textPaint = Theme.chat_msgGameTextPaint;
        } else {
            textPaint = Theme.chat_msgTextPaint;
        }
        if (allowsBigEmoji()) {
            iArr = new int[1];
        }
        CharSequence replaceEmoji = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, iArr);
        this.messageText = replaceEmoji;
        Spannable replaceAnimatedEmoji = replaceAnimatedEmoji(replaceEmoji, this.messageOwner.entities, textPaint.getFontMetricsInt());
        this.messageText = replaceAnimatedEmoji;
        if (iArr != null && iArr[0] > 1) {
            replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr);
        }
        checkEmojiOnly(iArr);
        generateLayout(user);
        setType();
    }

    private boolean allowsBigEmoji() {
        TLRPC$Peer tLRPC$Peer;
        if (!SharedConfig.allowBigEmoji) {
            return false;
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (tLRPC$Peer = tLRPC$Message.peer_id) == null || (tLRPC$Peer.channel_id == 0 && tLRPC$Peer.chat_id == 0)) {
            return true;
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        TLRPC$Peer tLRPC$Peer2 = this.messageOwner.peer_id;
        long j = tLRPC$Peer2.channel_id;
        if (j == 0) {
            j = tLRPC$Peer2.chat_id;
        }
        TLRPC$Chat chat = messagesController.getChat(Long.valueOf(j));
        return (chat != null && chat.gigagroup) || !ChatObject.isActionBanned(chat, 8) || ChatObject.hasAdminRights(chat);
    }

    public void generateGameMessageText(TLRPC$User tLRPC$User) {
        if (tLRPC$User == null && isFromUser()) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
        }
        TLRPC$TL_game tLRPC$TL_game = null;
        if (this.replyMessageObject != null && getMedia(this.messageOwner) != null && getMedia(this.messageOwner).game != null) {
            tLRPC$TL_game = getMedia(this.messageOwner).game;
        }
        if (tLRPC$TL_game == null) {
            if (tLRPC$User == null || tLRPC$User.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScored", R.string.ActionUserScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", tLRPC$User);
                return;
            } else {
                this.messageText = LocaleController.formatString("ActionYouScored", R.string.ActionYouScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
                return;
            }
        }
        if (tLRPC$User == null || tLRPC$User.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScoredInGame", R.string.ActionUserScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", tLRPC$User);
        } else {
            this.messageText = LocaleController.formatString("ActionYouScoredInGame", R.string.ActionYouScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
        }
        this.messageText = replaceWithLink(this.messageText, "un2", tLRPC$TL_game);
    }

    public boolean hasValidReplyMessageObject() {
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if (!(tLRPC$Message instanceof TLRPC$TL_messageEmpty) && !(tLRPC$Message.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                return true;
            }
        }
        return false;
    }

    public void generatePaymentSentMessageText(TLRPC$User tLRPC$User) {
        String str;
        if (tLRPC$User == null) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
        }
        String firstName = tLRPC$User != null ? UserObject.getFirstName(tLRPC$User) : "";
        try {
            LocaleController localeController = LocaleController.getInstance();
            TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
            str = localeController.formatCurrencyString(tLRPC$MessageAction.total_amount, tLRPC$MessageAction.currency);
        } catch (Exception e) {
            FileLog.e(e);
            str = "<error>";
        }
        if (this.replyMessageObject != null && (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice)) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message.action.recurring_init) {
                this.messageText = LocaleController.formatString(R.string.PaymentSuccessfullyPaidRecurrent, str, firstName, getMedia(tLRPC$Message).title);
            } else {
                this.messageText = LocaleController.formatString("PaymentSuccessfullyPaid", R.string.PaymentSuccessfullyPaid, str, firstName, getMedia(tLRPC$Message).title);
            }
        } else if (this.messageOwner.action.recurring_init) {
            this.messageText = LocaleController.formatString(R.string.PaymentSuccessfullyPaidNoItemRecurrent, str, firstName);
        } else {
            this.messageText = LocaleController.formatString("PaymentSuccessfullyPaidNoItem", R.string.PaymentSuccessfullyPaidNoItem, str, firstName);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v1 */
    public void generatePinMessageText(TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat) {
        boolean z;
        if (tLRPC$User == null && tLRPC$Chat == 0) {
            if (isFromUser()) {
                tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
            }
            if (tLRPC$User == null) {
                TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
                if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                    tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.channel_id));
                } else if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                    tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.chat_id));
                }
            }
        }
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if (!(tLRPC$Message instanceof TLRPC$TL_messageEmpty) && !(tLRPC$Message.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                if (messageObject.isMusic()) {
                    String string = LocaleController.getString("ActionPinnedMusic", R.string.ActionPinnedMusic);
                    TLRPC$Chat tLRPC$Chat2 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat2 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string, "un1", tLRPC$Chat2);
                    return;
                } else if (this.replyMessageObject.isVideo()) {
                    String string2 = LocaleController.getString("ActionPinnedVideo", R.string.ActionPinnedVideo);
                    TLRPC$Chat tLRPC$Chat3 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat3 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string2, "un1", tLRPC$Chat3);
                    return;
                } else if (this.replyMessageObject.isGif()) {
                    String string3 = LocaleController.getString("ActionPinnedGif", R.string.ActionPinnedGif);
                    TLRPC$Chat tLRPC$Chat4 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat4 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string3, "un1", tLRPC$Chat4);
                    return;
                } else if (this.replyMessageObject.isVoice()) {
                    String string4 = LocaleController.getString("ActionPinnedVoice", R.string.ActionPinnedVoice);
                    TLRPC$Chat tLRPC$Chat5 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat5 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string4, "un1", tLRPC$Chat5);
                    return;
                } else if (this.replyMessageObject.isRoundVideo()) {
                    String string5 = LocaleController.getString("ActionPinnedRound", R.string.ActionPinnedRound);
                    TLRPC$Chat tLRPC$Chat6 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat6 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string5, "un1", tLRPC$Chat6);
                    return;
                } else if ((this.replyMessageObject.isSticker() || this.replyMessageObject.isAnimatedSticker()) && !this.replyMessageObject.isAnimatedEmoji()) {
                    String string6 = LocaleController.getString("ActionPinnedSticker", R.string.ActionPinnedSticker);
                    TLRPC$Chat tLRPC$Chat7 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat7 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string6, "un1", tLRPC$Chat7);
                    return;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) {
                    String string7 = LocaleController.getString("ActionPinnedFile", R.string.ActionPinnedFile);
                    TLRPC$Chat tLRPC$Chat8 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat8 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string7, "un1", tLRPC$Chat8);
                    return;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeo) {
                    String string8 = LocaleController.getString("ActionPinnedGeo", R.string.ActionPinnedGeo);
                    TLRPC$Chat tLRPC$Chat9 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat9 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string8, "un1", tLRPC$Chat9);
                    return;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeoLive) {
                    String string9 = LocaleController.getString("ActionPinnedGeoLive", R.string.ActionPinnedGeoLive);
                    TLRPC$Chat tLRPC$Chat10 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat10 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string9, "un1", tLRPC$Chat10);
                    return;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaContact) {
                    String string10 = LocaleController.getString("ActionPinnedContact", R.string.ActionPinnedContact);
                    TLRPC$Chat tLRPC$Chat11 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat11 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string10, "un1", tLRPC$Chat11);
                    return;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPoll) {
                    if (((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.quiz) {
                        String string11 = LocaleController.getString("ActionPinnedQuiz", R.string.ActionPinnedQuiz);
                        TLRPC$Chat tLRPC$Chat12 = tLRPC$User;
                        if (tLRPC$User == null) {
                            tLRPC$Chat12 = tLRPC$Chat;
                        }
                        this.messageText = replaceWithLink(string11, "un1", tLRPC$Chat12);
                        return;
                    }
                    String string12 = LocaleController.getString("ActionPinnedPoll", R.string.ActionPinnedPoll);
                    TLRPC$Chat tLRPC$Chat13 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat13 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string12, "un1", tLRPC$Chat13);
                    return;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                    String string13 = LocaleController.getString("ActionPinnedPhoto", R.string.ActionPinnedPhoto);
                    TLRPC$Chat tLRPC$Chat14 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat14 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string13, "un1", tLRPC$Chat14);
                    return;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                    int i = R.string.ActionPinnedGame;
                    String formatString = LocaleController.formatString("ActionPinnedGame", i, "🎮 " + getMedia(this.messageOwner).game.title);
                    TLRPC$Chat tLRPC$Chat15 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat15 = tLRPC$Chat;
                    }
                    CharSequence replaceWithLink = replaceWithLink(formatString, "un1", tLRPC$Chat15);
                    this.messageText = replaceWithLink;
                    this.messageText = Emoji.replaceEmoji(replaceWithLink, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    return;
                } else {
                    CharSequence charSequence = this.replyMessageObject.messageText;
                    if (charSequence != null && charSequence.length() > 0) {
                        CharSequence cloneSpans = AnimatedEmojiSpan.cloneSpans(this.replyMessageObject.messageText);
                        if (cloneSpans.length() > 20) {
                            cloneSpans = cloneSpans.subSequence(0, 20);
                            z = true;
                        } else {
                            z = false;
                        }
                        CharSequence replaceEmoji = Emoji.replaceEmoji(cloneSpans, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                        MessageObject messageObject2 = this.replyMessageObject;
                        Spannable spannable = replaceEmoji;
                        if (messageObject2 != null) {
                            TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
                            spannable = replaceEmoji;
                            if (tLRPC$Message2 != null) {
                                spannable = replaceAnimatedEmoji(replaceEmoji, tLRPC$Message2.entities, Theme.chat_msgTextPaint.getFontMetricsInt());
                            }
                        }
                        MediaDataController.addTextStyleRuns(this.replyMessageObject, (Spannable) spannable);
                        SpannableStringBuilder spannableStringBuilder = spannable;
                        if (z) {
                            boolean z2 = spannable instanceof SpannableStringBuilder;
                            spannableStringBuilder = spannable;
                            if (z2) {
                                ((SpannableStringBuilder) spannable).append((CharSequence) "...");
                                spannableStringBuilder = spannable;
                            } else if (spannable != null) {
                                spannableStringBuilder = new SpannableStringBuilder(spannable).append((CharSequence) "...");
                            }
                        }
                        SpannableStringBuilder formatSpannable = AndroidUtilities.formatSpannable(LocaleController.getString("ActionPinnedText", R.string.ActionPinnedText), spannableStringBuilder);
                        TLRPC$Chat tLRPC$Chat16 = tLRPC$User;
                        if (tLRPC$User == null) {
                            tLRPC$Chat16 = tLRPC$Chat;
                        }
                        this.messageText = replaceWithLink(formatSpannable, "un1", tLRPC$Chat16);
                        return;
                    }
                    String string14 = LocaleController.getString("ActionPinnedNoText", R.string.ActionPinnedNoText);
                    TLRPC$Chat tLRPC$Chat17 = tLRPC$User;
                    if (tLRPC$User == null) {
                        tLRPC$Chat17 = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string14, "un1", tLRPC$Chat17);
                    return;
                }
            }
        }
        String string15 = LocaleController.getString("ActionPinnedNoText", R.string.ActionPinnedNoText);
        TLRPC$Chat tLRPC$Chat18 = tLRPC$User;
        if (tLRPC$User == null) {
            tLRPC$Chat18 = tLRPC$Chat;
        }
        this.messageText = replaceWithLink(string15, "un1", tLRPC$Chat18);
    }

    public static void updateReactions(TLRPC$Message tLRPC$Message, TLRPC$TL_messageReactions tLRPC$TL_messageReactions) {
        if (tLRPC$Message == null || tLRPC$TL_messageReactions == null) {
            return;
        }
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions2 = tLRPC$Message.reactions;
        if (tLRPC$TL_messageReactions2 != null) {
            int size = tLRPC$TL_messageReactions2.results.size();
            boolean z = false;
            for (int i = 0; i < size; i++) {
                TLRPC$ReactionCount tLRPC$ReactionCount = tLRPC$Message.reactions.results.get(i);
                int size2 = tLRPC$TL_messageReactions.results.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    TLRPC$ReactionCount tLRPC$ReactionCount2 = tLRPC$TL_messageReactions.results.get(i2);
                    if (ReactionsLayoutInBubble.equalsTLReaction(tLRPC$ReactionCount.reaction, tLRPC$ReactionCount2.reaction)) {
                        if (!z && tLRPC$TL_messageReactions.min && tLRPC$ReactionCount.chosen) {
                            tLRPC$ReactionCount2.chosen = true;
                            z = true;
                        }
                        tLRPC$ReactionCount2.lastDrawnPosition = tLRPC$ReactionCount.lastDrawnPosition;
                    }
                }
                if (tLRPC$ReactionCount.chosen) {
                    z = true;
                }
            }
        }
        tLRPC$Message.reactions = tLRPC$TL_messageReactions;
        tLRPC$Message.flags |= 1048576;
    }

    public boolean hasReactions() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        return tLRPC$TL_messageReactions != null && !tLRPC$TL_messageReactions.results.isEmpty();
    }

    public static void updatePollResults(TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll, TLRPC$PollResults tLRPC$PollResults) {
        ArrayList arrayList;
        byte[] bArr;
        ArrayList<TLRPC$TL_pollAnswerVoters> arrayList2;
        if (tLRPC$TL_messageMediaPoll == null || tLRPC$PollResults == null) {
            return;
        }
        if ((tLRPC$PollResults.flags & 2) != 0) {
            if (!tLRPC$PollResults.min || (arrayList2 = tLRPC$TL_messageMediaPoll.results.results) == null) {
                arrayList = null;
                bArr = null;
            } else {
                int size = arrayList2.size();
                arrayList = null;
                bArr = null;
                for (int i = 0; i < size; i++) {
                    TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters = tLRPC$TL_messageMediaPoll.results.results.get(i);
                    if (tLRPC$TL_pollAnswerVoters.chosen) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(tLRPC$TL_pollAnswerVoters.option);
                    }
                    if (tLRPC$TL_pollAnswerVoters.correct) {
                        bArr = tLRPC$TL_pollAnswerVoters.option;
                    }
                }
            }
            TLRPC$PollResults tLRPC$PollResults2 = tLRPC$TL_messageMediaPoll.results;
            ArrayList<TLRPC$TL_pollAnswerVoters> arrayList3 = tLRPC$PollResults.results;
            tLRPC$PollResults2.results = arrayList3;
            if (arrayList != null || bArr != null) {
                int size2 = arrayList3.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters2 = tLRPC$TL_messageMediaPoll.results.results.get(i2);
                    if (arrayList != null) {
                        int size3 = arrayList.size();
                        int i3 = 0;
                        while (true) {
                            if (i3 >= size3) {
                                break;
                            } else if (Arrays.equals(tLRPC$TL_pollAnswerVoters2.option, (byte[]) arrayList.get(i3))) {
                                tLRPC$TL_pollAnswerVoters2.chosen = true;
                                arrayList.remove(i3);
                                break;
                            } else {
                                i3++;
                            }
                        }
                        if (arrayList.isEmpty()) {
                            arrayList = null;
                        }
                    }
                    if (bArr != null && Arrays.equals(tLRPC$TL_pollAnswerVoters2.option, bArr)) {
                        tLRPC$TL_pollAnswerVoters2.correct = true;
                        bArr = null;
                    }
                    if (arrayList == null && bArr == null) {
                        break;
                    }
                }
            }
            tLRPC$TL_messageMediaPoll.results.flags |= 2;
        }
        if ((tLRPC$PollResults.flags & 4) != 0) {
            TLRPC$PollResults tLRPC$PollResults3 = tLRPC$TL_messageMediaPoll.results;
            tLRPC$PollResults3.total_voters = tLRPC$PollResults.total_voters;
            tLRPC$PollResults3.flags |= 4;
        }
        if ((tLRPC$PollResults.flags & 8) != 0) {
            TLRPC$PollResults tLRPC$PollResults4 = tLRPC$TL_messageMediaPoll.results;
            tLRPC$PollResults4.recent_voters = tLRPC$PollResults.recent_voters;
            tLRPC$PollResults4.flags |= 8;
        }
        if ((tLRPC$PollResults.flags & 16) == 0) {
            return;
        }
        TLRPC$PollResults tLRPC$PollResults5 = tLRPC$TL_messageMediaPoll.results;
        tLRPC$PollResults5.solution = tLRPC$PollResults.solution;
        tLRPC$PollResults5.solution_entities = tLRPC$PollResults.solution_entities;
        tLRPC$PollResults5.flags |= 16;
    }

    public void loadAnimatedEmojiDocument() {
        if (this.emojiAnimatedSticker != null || this.emojiAnimatedStickerId == null || this.emojiAnimatedStickerLoading) {
            return;
        }
        this.emojiAnimatedStickerLoading = true;
        AnimatedEmojiDrawable.getDocumentFetcher(this.currentAccount).fetchDocument(this.emojiAnimatedStickerId.longValue(), new AnimatedEmojiDrawable.ReceivedDocument() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.ReceivedDocument
            public final void run(TLRPC$Document tLRPC$Document) {
                MessageObject.this.lambda$loadAnimatedEmojiDocument$1(tLRPC$Document);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAnimatedEmojiDocument$1(final TLRPC$Document tLRPC$Document) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MessageObject.this.lambda$loadAnimatedEmojiDocument$0(tLRPC$Document);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAnimatedEmojiDocument$0(TLRPC$Document tLRPC$Document) {
        this.emojiAnimatedSticker = tLRPC$Document;
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.animatedEmojiDocumentLoaded, this);
    }

    public boolean isPollClosed() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.closed;
    }

    public boolean isQuiz() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.quiz;
    }

    public boolean isPublicPoll() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.public_voters;
    }

    public boolean isPoll() {
        return this.type == 17;
    }

    public boolean canUnvote() {
        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll;
        TLRPC$PollResults tLRPC$PollResults;
        if (this.type == 17 && (tLRPC$PollResults = (tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).results) != null && !tLRPC$PollResults.results.isEmpty() && !tLRPC$TL_messageMediaPoll.poll.quiz) {
            int size = tLRPC$TL_messageMediaPoll.results.results.size();
            for (int i = 0; i < size; i++) {
                if (tLRPC$TL_messageMediaPoll.results.results.get(i).chosen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isVoted() {
        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll;
        TLRPC$PollResults tLRPC$PollResults;
        if (this.type == 17 && (tLRPC$PollResults = (tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).results) != null && !tLRPC$PollResults.results.isEmpty()) {
            int size = tLRPC$TL_messageMediaPoll.results.results.size();
            for (int i = 0; i < size; i++) {
                if (tLRPC$TL_messageMediaPoll.results.results.get(i).chosen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSponsored() {
        return this.sponsoredId != null;
    }

    public long getPollId() {
        if (this.type != 17) {
            return 0L;
        }
        return ((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.id;
    }

    private TLRPC$Photo getPhotoWithId(TLRPC$WebPage tLRPC$WebPage, long j) {
        if (tLRPC$WebPage != null && tLRPC$WebPage.cached_page != null) {
            TLRPC$Photo tLRPC$Photo = tLRPC$WebPage.photo;
            if (tLRPC$Photo != null && tLRPC$Photo.id == j) {
                return tLRPC$Photo;
            }
            for (int i = 0; i < tLRPC$WebPage.cached_page.photos.size(); i++) {
                TLRPC$Photo tLRPC$Photo2 = tLRPC$WebPage.cached_page.photos.get(i);
                if (tLRPC$Photo2.id == j) {
                    return tLRPC$Photo2;
                }
            }
        }
        return null;
    }

    private TLRPC$Document getDocumentWithId(TLRPC$WebPage tLRPC$WebPage, long j) {
        if (tLRPC$WebPage != null && tLRPC$WebPage.cached_page != null) {
            TLRPC$Document tLRPC$Document = tLRPC$WebPage.document;
            if (tLRPC$Document != null && tLRPC$Document.id == j) {
                return tLRPC$Document;
            }
            for (int i = 0; i < tLRPC$WebPage.cached_page.documents.size(); i++) {
                TLRPC$Document tLRPC$Document2 = tLRPC$WebPage.cached_page.documents.get(i);
                if (tLRPC$Document2.id == j) {
                    return tLRPC$Document2;
                }
            }
        }
        return null;
    }

    public boolean isSupergroup() {
        if (this.localSupergroup) {
            return true;
        }
        Boolean bool = this.cachedIsSupergroup;
        if (bool != null) {
            return bool.booleanValue();
        }
        TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
        if (tLRPC$Peer != null) {
            long j = tLRPC$Peer.channel_id;
            if (j != 0) {
                TLRPC$Chat chat = getChat(null, null, j);
                if (chat == null) {
                    return false;
                }
                Boolean valueOf = Boolean.valueOf(chat.megagroup);
                this.cachedIsSupergroup = valueOf;
                return valueOf.booleanValue();
            }
        }
        this.cachedIsSupergroup = Boolean.FALSE;
        return false;
    }

    private MessageObject getMessageObjectForBlock(TLRPC$WebPage tLRPC$WebPage, TLRPC$PageBlock tLRPC$PageBlock) {
        TLRPC$TL_message tLRPC$TL_message;
        if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockPhoto) {
            TLRPC$Photo photoWithId = getPhotoWithId(tLRPC$WebPage, ((TLRPC$TL_pageBlockPhoto) tLRPC$PageBlock).photo_id);
            if (photoWithId == tLRPC$WebPage.photo) {
                return this;
            }
            tLRPC$TL_message = new TLRPC$TL_message();
            TLRPC$TL_messageMediaPhoto tLRPC$TL_messageMediaPhoto = new TLRPC$TL_messageMediaPhoto();
            tLRPC$TL_message.media = tLRPC$TL_messageMediaPhoto;
            tLRPC$TL_messageMediaPhoto.photo = photoWithId;
        } else if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockVideo) {
            TLRPC$TL_pageBlockVideo tLRPC$TL_pageBlockVideo = (TLRPC$TL_pageBlockVideo) tLRPC$PageBlock;
            if (getDocumentWithId(tLRPC$WebPage, tLRPC$TL_pageBlockVideo.video_id) == tLRPC$WebPage.document) {
                return this;
            }
            TLRPC$TL_message tLRPC$TL_message2 = new TLRPC$TL_message();
            TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = new TLRPC$TL_messageMediaDocument();
            tLRPC$TL_message2.media = tLRPC$TL_messageMediaDocument;
            tLRPC$TL_messageMediaDocument.document = getDocumentWithId(tLRPC$WebPage, tLRPC$TL_pageBlockVideo.video_id);
            tLRPC$TL_message = tLRPC$TL_message2;
        } else {
            tLRPC$TL_message = null;
        }
        tLRPC$TL_message.message = "";
        tLRPC$TL_message.realId = getId();
        tLRPC$TL_message.id = Utilities.random.nextInt();
        TLRPC$Message tLRPC$Message = this.messageOwner;
        tLRPC$TL_message.date = tLRPC$Message.date;
        tLRPC$TL_message.peer_id = tLRPC$Message.peer_id;
        tLRPC$TL_message.out = tLRPC$Message.out;
        tLRPC$TL_message.from_id = tLRPC$Message.from_id;
        return new MessageObject(this.currentAccount, tLRPC$TL_message, false, true);
    }

    public ArrayList<MessageObject> getWebPagePhotos(ArrayList<MessageObject> arrayList, ArrayList<TLRPC$PageBlock> arrayList2) {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$Page tLRPC$Page;
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        if (getMedia(this.messageOwner) == null || getMedia(this.messageOwner).webpage == null || (tLRPC$Page = (tLRPC$WebPage = getMedia(this.messageOwner).webpage).cached_page) == null) {
            return arrayList;
        }
        if (arrayList2 == null) {
            arrayList2 = tLRPC$Page.blocks;
        }
        for (int i = 0; i < arrayList2.size(); i++) {
            TLRPC$PageBlock tLRPC$PageBlock = arrayList2.get(i);
            if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockSlideshow) {
                TLRPC$TL_pageBlockSlideshow tLRPC$TL_pageBlockSlideshow = (TLRPC$TL_pageBlockSlideshow) tLRPC$PageBlock;
                for (int i2 = 0; i2 < tLRPC$TL_pageBlockSlideshow.items.size(); i2++) {
                    arrayList.add(getMessageObjectForBlock(tLRPC$WebPage, tLRPC$TL_pageBlockSlideshow.items.get(i2)));
                }
            } else if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockCollage) {
                TLRPC$TL_pageBlockCollage tLRPC$TL_pageBlockCollage = (TLRPC$TL_pageBlockCollage) tLRPC$PageBlock;
                for (int i3 = 0; i3 < tLRPC$TL_pageBlockCollage.items.size(); i3++) {
                    arrayList.add(getMessageObjectForBlock(tLRPC$WebPage, tLRPC$TL_pageBlockCollage.items.get(i3)));
                }
            }
        }
        return arrayList;
    }

    public void createMessageSendInfo() {
        HashMap<String, String> hashMap;
        String str;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.message != null) {
            if ((tLRPC$Message.id >= 0 && !isEditing()) || (hashMap = this.messageOwner.params) == null) {
                return;
            }
            String str2 = hashMap.get("ve");
            if (str2 != null && (isVideo() || isNewGif() || isRoundVideo())) {
                VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
                this.videoEditedInfo = videoEditedInfo;
                if (!videoEditedInfo.parseString(str2)) {
                    this.videoEditedInfo = null;
                } else {
                    this.videoEditedInfo.roundVideo = isRoundVideo();
                }
            }
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            if (tLRPC$Message2.send_state != 3 || (str = tLRPC$Message2.params.get("prevMedia")) == null) {
                return;
            }
            SerializedData serializedData = new SerializedData(Base64.decode(str, 0));
            this.previousMedia = TLRPC$MessageMedia.TLdeserialize(serializedData, serializedData.readInt32(false), false);
            this.previousMessage = serializedData.readString(false);
            this.previousAttachPath = serializedData.readString(false);
            int readInt32 = serializedData.readInt32(false);
            this.previousMessageEntities = new ArrayList<>(readInt32);
            for (int i = 0; i < readInt32; i++) {
                this.previousMessageEntities.add(TLRPC$MessageEntity.TLdeserialize(serializedData, serializedData.readInt32(false), false));
            }
            serializedData.cleanup();
        }
    }

    public void measureInlineBotButtons() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        int i;
        CharSequence replaceEmoji;
        if (this.isRestrictedMessage) {
            return;
        }
        this.wantedBotKeyboardWidth = 0;
        if (((this.messageOwner.reply_markup instanceof TLRPC$TL_replyInlineMarkup) && !hasExtendedMedia()) || ((tLRPC$TL_messageReactions = this.messageOwner.reactions) != null && !tLRPC$TL_messageReactions.results.isEmpty())) {
            Theme.createCommonMessageResources();
            StringBuilder sb = this.botButtonsLayout;
            if (sb == null) {
                this.botButtonsLayout = new StringBuilder();
            } else {
                sb.setLength(0);
            }
        }
        if ((this.messageOwner.reply_markup instanceof TLRPC$TL_replyInlineMarkup) && !hasExtendedMedia()) {
            for (int i2 = 0; i2 < this.messageOwner.reply_markup.rows.size(); i2++) {
                TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = this.messageOwner.reply_markup.rows.get(i2);
                int size = tLRPC$TL_keyboardButtonRow.buttons.size();
                int i3 = 0;
                for (int i4 = 0; i4 < size; i4++) {
                    TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i4);
                    StringBuilder sb2 = this.botButtonsLayout;
                    sb2.append(i2);
                    sb2.append(i4);
                    if ((tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonBuy) && (getMedia(this.messageOwner).flags & 4) != 0) {
                        replaceEmoji = LocaleController.getString("PaymentReceipt", R.string.PaymentReceipt);
                    } else {
                        String str = tLRPC$KeyboardButton.text;
                        if (str == null) {
                            str = "";
                        }
                        replaceEmoji = Emoji.replaceEmoji(str, Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false);
                    }
                    StaticLayout staticLayout = new StaticLayout(replaceEmoji, Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(2000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    if (staticLayout.getLineCount() > 0) {
                        float lineWidth = staticLayout.getLineWidth(0);
                        float lineLeft = staticLayout.getLineLeft(0);
                        if (lineLeft < lineWidth) {
                            lineWidth -= lineLeft;
                        }
                        i3 = Math.max(i3, ((int) Math.ceil(lineWidth)) + AndroidUtilities.dp(4.0f));
                    }
                }
                this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, ((i3 + AndroidUtilities.dp(12.0f)) * size) + (AndroidUtilities.dp(5.0f) * (size - 1)));
            }
            return;
        }
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions2 = this.messageOwner.reactions;
        if (tLRPC$TL_messageReactions2 == null) {
            return;
        }
        int size2 = tLRPC$TL_messageReactions2.results.size();
        for (int i5 = 0; i5 < size2; i5++) {
            TLRPC$ReactionCount tLRPC$ReactionCount = this.messageOwner.reactions.results.get(i5);
            StringBuilder sb3 = this.botButtonsLayout;
            sb3.append(0);
            sb3.append(i5);
            StaticLayout staticLayout2 = new StaticLayout(Emoji.replaceEmoji(String.format("%d %s", Integer.valueOf(tLRPC$ReactionCount.count), tLRPC$ReactionCount.reaction), Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false), Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(2000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (staticLayout2.getLineCount() > 0) {
                float lineWidth2 = staticLayout2.getLineWidth(0);
                float lineLeft2 = staticLayout2.getLineLeft(0);
                if (lineLeft2 < lineWidth2) {
                    lineWidth2 -= lineLeft2;
                }
                i = Math.max(0, ((int) Math.ceil(lineWidth2)) + AndroidUtilities.dp(4.0f));
            } else {
                i = 0;
            }
            this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, ((i + AndroidUtilities.dp(12.0f)) * size2) + (AndroidUtilities.dp(5.0f) * (size2 - 1)));
        }
    }

    public boolean isVideoAvatar() {
        TLRPC$Photo tLRPC$Photo;
        TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
        return (tLRPC$MessageAction == null || (tLRPC$Photo = tLRPC$MessageAction.photo) == null || tLRPC$Photo.video_sizes.isEmpty()) ? false : true;
    }

    public boolean isFcmMessage() {
        return this.localType != 0;
    }

    private TLRPC$User getUser(AbstractMap<Long, TLRPC$User> abstractMap, LongSparseArray<TLRPC$User> longSparseArray, long j) {
        TLRPC$User tLRPC$User;
        if (abstractMap != null) {
            tLRPC$User = abstractMap.get(Long.valueOf(j));
        } else {
            tLRPC$User = longSparseArray != null ? longSparseArray.get(j) : null;
        }
        return tLRPC$User == null ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j)) : tLRPC$User;
    }

    private TLRPC$Chat getChat(AbstractMap<Long, TLRPC$Chat> abstractMap, LongSparseArray<TLRPC$Chat> longSparseArray, long j) {
        TLRPC$Chat tLRPC$Chat;
        if (abstractMap != null) {
            tLRPC$Chat = abstractMap.get(Long.valueOf(j));
        } else {
            tLRPC$Chat = longSparseArray != null ? longSparseArray.get(j) : null;
        }
        return tLRPC$Chat == null ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j)) : tLRPC$Chat;
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x110f  */
    /* JADX WARN: Removed duplicated region for block: B:141:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:205:0x05b1  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x05cf  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0631  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0647  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0693  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x069f  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x076c  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x07a6  */
    /* JADX WARN: Removed duplicated region for block: B:548:0x0e86  */
    /* JADX WARN: Removed duplicated region for block: B:649:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:6:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateMessageText(AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2) {
        TLRPC$User tLRPC$User;
        TLRPC$Chat chat;
        TLRPC$Message tLRPC$Message;
        String str;
        String str2;
        String string;
        String formatString;
        String formatString2;
        String str3;
        TLRPC$Chat tLRPC$Chat;
        String sb;
        TLRPC$Chat tLRPC$Chat2;
        TLRPC$Chat tLRPC$Chat3;
        TLRPC$Chat tLRPC$Chat4;
        TLRPC$Chat tLRPC$Chat5;
        TLObject chat2;
        TLObject chat3;
        String formatPluralString;
        TLRPC$Peer tLRPC$Peer = this.messageOwner.from_id;
        if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
            tLRPC$User = getUser(abstractMap, longSparseArray, tLRPC$Peer.user_id);
        } else if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
            chat = getChat(abstractMap2, longSparseArray2, tLRPC$Peer.channel_id);
            tLRPC$User = null;
            TLRPC$Chat tLRPC$Chat6 = tLRPC$User == null ? tLRPC$User : chat;
            tLRPC$Message = this.messageOwner;
            if (!(tLRPC$Message instanceof TLRPC$TL_messageService)) {
                TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                if (tLRPC$MessageAction != null) {
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGroupCallScheduled) {
                        TLRPC$TL_messageActionGroupCallScheduled tLRPC$TL_messageActionGroupCallScheduled = (TLRPC$TL_messageActionGroupCallScheduled) tLRPC$MessageAction;
                        if ((tLRPC$Message.peer_id instanceof TLRPC$TL_peerChat) || isSupergroup()) {
                            this.messageText = LocaleController.formatString("ActionGroupCallScheduled", R.string.ActionGroupCallScheduled, LocaleController.formatStartsTime(tLRPC$TL_messageActionGroupCallScheduled.schedule_date, 3, false));
                        } else {
                            this.messageText = LocaleController.formatString("ActionChannelCallScheduled", R.string.ActionChannelCallScheduled, LocaleController.formatStartsTime(tLRPC$TL_messageActionGroupCallScheduled.schedule_date, 3, false));
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGroupCall) {
                        int i = tLRPC$MessageAction.duration;
                        if (i != 0) {
                            int i2 = i / 86400;
                            if (i2 > 0) {
                                formatPluralString = LocaleController.formatPluralString("Days", i2, new Object[0]);
                            } else {
                                int i3 = i / 3600;
                                if (i3 > 0) {
                                    formatPluralString = LocaleController.formatPluralString("Hours", i3, new Object[0]);
                                } else {
                                    int i4 = i / 60;
                                    if (i4 > 0) {
                                        formatPluralString = LocaleController.formatPluralString("Minutes", i4, new Object[0]);
                                    } else {
                                        formatPluralString = LocaleController.formatPluralString("Seconds", i, new Object[0]);
                                    }
                                }
                            }
                            if (!(this.messageOwner.peer_id instanceof TLRPC$TL_peerChat) && !isSupergroup()) {
                                this.messageText = LocaleController.formatString("ActionChannelCallEnded", R.string.ActionChannelCallEnded, formatPluralString);
                            } else if (isOut()) {
                                this.messageText = LocaleController.formatString("ActionGroupCallEndedByYou", R.string.ActionGroupCallEndedByYou, formatPluralString);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.formatString("ActionGroupCallEndedBy", R.string.ActionGroupCallEndedBy, formatPluralString), "un1", tLRPC$Chat6);
                            }
                        } else if ((tLRPC$Message.peer_id instanceof TLRPC$TL_peerChat) || isSupergroup()) {
                            if (isOut()) {
                                this.messageText = LocaleController.getString("ActionGroupCallStartedByYou", R.string.ActionGroupCallStartedByYou);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallStarted", R.string.ActionGroupCallStarted), "un1", tLRPC$Chat6);
                            }
                        } else {
                            this.messageText = LocaleController.getString("ActionChannelCallJustStarted", R.string.ActionChannelCallJustStarted);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionInviteToGroupCall) {
                        long j = tLRPC$MessageAction.user_id;
                        if (j == 0 && tLRPC$MessageAction.users.size() == 1) {
                            j = this.messageOwner.action.users.get(0).longValue();
                        }
                        if (j != 0) {
                            TLRPC$User user = getUser(abstractMap, longSparseArray, j);
                            if (isOut()) {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallYouInvited", R.string.ActionGroupCallYouInvited), "un2", user);
                            } else if (j == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallInvitedYou", R.string.ActionGroupCallInvitedYou), "un1", tLRPC$Chat6);
                            } else {
                                CharSequence replaceWithLink = replaceWithLink(LocaleController.getString("ActionGroupCallInvited", R.string.ActionGroupCallInvited), "un2", user);
                                this.messageText = replaceWithLink;
                                this.messageText = replaceWithLink(replaceWithLink, "un1", tLRPC$Chat6);
                            }
                        } else if (isOut()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallYouInvited", R.string.ActionGroupCallYouInvited), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                        } else {
                            CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString("ActionGroupCallInvited", R.string.ActionGroupCallInvited), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                            this.messageText = replaceWithLink2;
                            this.messageText = replaceWithLink(replaceWithLink2, "un1", tLRPC$Chat6);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                        TLRPC$TL_messageActionGeoProximityReached tLRPC$TL_messageActionGeoProximityReached = (TLRPC$TL_messageActionGeoProximityReached) tLRPC$MessageAction;
                        long peerId = getPeerId(tLRPC$TL_messageActionGeoProximityReached.from_id);
                        if (peerId > 0) {
                            chat2 = getUser(abstractMap, longSparseArray, peerId);
                        } else {
                            chat2 = getChat(abstractMap2, longSparseArray2, -peerId);
                        }
                        TLObject tLObject = chat2;
                        long peerId2 = getPeerId(tLRPC$TL_messageActionGeoProximityReached.to_id);
                        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                        if (peerId2 == clientUserId) {
                            this.messageText = replaceWithLink(LocaleController.formatString("ActionUserWithinRadius", R.string.ActionUserWithinRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un1", tLObject);
                        } else {
                            if (peerId2 > 0) {
                                chat3 = getUser(abstractMap, longSparseArray, peerId2);
                            } else {
                                chat3 = getChat(abstractMap2, longSparseArray2, -peerId2);
                            }
                            if (peerId == clientUserId) {
                                this.messageText = replaceWithLink(LocaleController.formatString("ActionUserWithinYouRadius", R.string.ActionUserWithinYouRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un1", chat3);
                            } else {
                                CharSequence replaceWithLink3 = replaceWithLink(LocaleController.formatString("ActionUserWithinOtherRadius", R.string.ActionUserWithinOtherRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un2", chat3);
                                this.messageText = replaceWithLink3;
                                this.messageText = replaceWithLink(replaceWithLink3, "un1", tLObject);
                            }
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCustomAction) {
                        this.messageText = tLRPC$MessageAction.message;
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatCreate) {
                        if (isOut()) {
                            this.messageText = LocaleController.getString("ActionYouCreateGroup", R.string.ActionYouCreateGroup);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionCreateGroup", R.string.ActionCreateGroup), "un1", tLRPC$Chat6);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatDeleteUser) {
                        if (isFromUser()) {
                            TLRPC$Message tLRPC$Message2 = this.messageOwner;
                            if (tLRPC$Message2.action.user_id == tLRPC$Message2.from_id.user_id) {
                                if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouLeftUser", R.string.ActionYouLeftUser);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionLeftUser", R.string.ActionLeftUser), "un1", tLRPC$Chat6);
                                }
                            }
                        }
                        TLRPC$User user2 = getUser(abstractMap, longSparseArray, this.messageOwner.action.user_id);
                        if (isOut()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionYouKickUser", R.string.ActionYouKickUser), "un2", user2);
                        } else if (this.messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionKickUserYou", R.string.ActionKickUserYou), "un1", tLRPC$Chat6);
                        } else {
                            CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString("ActionKickUser", R.string.ActionKickUser), "un2", user2);
                            this.messageText = replaceWithLink4;
                            this.messageText = replaceWithLink(replaceWithLink4, "un1", tLRPC$Chat6);
                        }
                    } else {
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatAddUser) {
                            long j2 = tLRPC$MessageAction.user_id;
                            if (j2 == 0 && tLRPC$MessageAction.users.size() == 1) {
                                j2 = this.messageOwner.action.users.get(0).longValue();
                            }
                            if (j2 != 0) {
                                TLRPC$User user3 = getUser(abstractMap, longSparseArray, j2);
                                str2 = "";
                                long j3 = this.messageOwner.peer_id.channel_id;
                                TLRPC$Chat chat4 = j3 != 0 ? getChat(abstractMap2, longSparseArray2, j3) : null;
                                TLRPC$Peer tLRPC$Peer2 = this.messageOwner.from_id;
                                if (tLRPC$Peer2 != null && j2 == tLRPC$Peer2.user_id) {
                                    if (ChatObject.isChannel(chat4) && !chat4.megagroup) {
                                        this.messageText = LocaleController.getString("ChannelJoined", R.string.ChannelJoined);
                                    } else if (this.messageOwner.peer_id.channel_id != 0) {
                                        if (j2 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                            this.messageText = LocaleController.getString("ChannelMegaJoined", R.string.ChannelMegaJoined);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelfMega", R.string.ActionAddUserSelfMega), "un1", tLRPC$Chat6);
                                        }
                                    } else if (isOut()) {
                                        this.messageText = LocaleController.getString("ActionAddUserSelfYou", R.string.ActionAddUserSelfYou);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelf", R.string.ActionAddUserSelf), "un1", tLRPC$Chat6);
                                    }
                                } else if (isOut()) {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionYouAddUser", R.string.ActionYouAddUser), "un2", user3);
                                } else if (j2 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    if (this.messageOwner.peer_id.channel_id != 0) {
                                        if (chat4 != null && chat4.megagroup) {
                                            this.messageText = replaceWithLink(LocaleController.getString("MegaAddedBy", R.string.MegaAddedBy), "un1", tLRPC$Chat6);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ChannelAddedBy", R.string.ChannelAddedBy), "un1", tLRPC$Chat6);
                                        }
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserYou", R.string.ActionAddUserYou), "un1", tLRPC$Chat6);
                                    }
                                } else {
                                    CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString("ActionAddUser", R.string.ActionAddUser), "un2", user3);
                                    this.messageText = replaceWithLink5;
                                    this.messageText = replaceWithLink(replaceWithLink5, "un1", tLRPC$Chat6);
                                }
                            } else {
                                str2 = "";
                                if (isOut()) {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionYouAddUser", R.string.ActionYouAddUser), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                } else {
                                    CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString("ActionAddUser", R.string.ActionAddUser), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                    this.messageText = replaceWithLink6;
                                    this.messageText = replaceWithLink(replaceWithLink6, "un1", tLRPC$Chat6);
                                }
                            }
                        } else {
                            str2 = "";
                            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatJoinedByLink) {
                                if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionInviteYou", R.string.ActionInviteYou);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionInviteUser", R.string.ActionInviteUser), "un1", tLRPC$Chat6);
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftPremium) {
                                if ((tLRPC$Chat6 instanceof TLRPC$User) && ((TLRPC$User) tLRPC$Chat6).self) {
                                    this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftOutbound)), "un1", getUser(abstractMap, longSparseArray, tLRPC$Message.peer_id.user_id));
                                } else {
                                    this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), "un1", tLRPC$Chat6);
                                }
                                int indexOf = this.messageText.toString().indexOf("un2");
                                if (indexOf != -1) {
                                    BillingController billingController = BillingController.getInstance();
                                    TLRPC$MessageAction tLRPC$MessageAction2 = this.messageOwner.action;
                                    this.messageText = SpannableStringBuilder.valueOf(this.messageText).replace(indexOf, indexOf + 3, (CharSequence) billingController.formatCurrency(tLRPC$MessageAction2.amount, tLRPC$MessageAction2.currency));
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto) {
                                TLRPC$Peer tLRPC$Peer3 = tLRPC$Message.peer_id;
                                if (tLRPC$Peer3 != null) {
                                    long j4 = tLRPC$Peer3.channel_id;
                                    if (j4 != 0) {
                                        tLRPC$Chat5 = getChat(abstractMap2, longSparseArray2, j4);
                                        if (!ChatObject.isChannel(tLRPC$Chat5) && !tLRPC$Chat5.megagroup) {
                                            if (isVideoAvatar()) {
                                                this.messageText = LocaleController.getString("ActionChannelChangedVideo", R.string.ActionChannelChangedVideo);
                                            } else {
                                                this.messageText = LocaleController.getString("ActionChannelChangedPhoto", R.string.ActionChannelChangedPhoto);
                                            }
                                        } else if (!isOut()) {
                                            if (isVideoAvatar()) {
                                                this.messageText = LocaleController.getString("ActionYouChangedVideo", R.string.ActionYouChangedVideo);
                                            } else {
                                                this.messageText = LocaleController.getString("ActionYouChangedPhoto", R.string.ActionYouChangedPhoto);
                                            }
                                        } else if (isVideoAvatar()) {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionChangedVideo", R.string.ActionChangedVideo), "un1", tLRPC$Chat6);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionChangedPhoto", R.string.ActionChangedPhoto), "un1", tLRPC$Chat6);
                                        }
                                    }
                                }
                                tLRPC$Chat5 = null;
                                if (!ChatObject.isChannel(tLRPC$Chat5)) {
                                }
                                if (!isOut()) {
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditTitle) {
                                TLRPC$Peer tLRPC$Peer4 = tLRPC$Message.peer_id;
                                if (tLRPC$Peer4 != null) {
                                    long j5 = tLRPC$Peer4.channel_id;
                                    if (j5 != 0) {
                                        tLRPC$Chat4 = getChat(abstractMap2, longSparseArray2, j5);
                                        if (!ChatObject.isChannel(tLRPC$Chat4) && !tLRPC$Chat4.megagroup) {
                                            this.messageText = LocaleController.getString("ActionChannelChangedTitle", R.string.ActionChannelChangedTitle).replace("un2", this.messageOwner.action.title);
                                        } else if (!isOut()) {
                                            this.messageText = LocaleController.getString("ActionYouChangedTitle", R.string.ActionYouChangedTitle).replace("un2", this.messageOwner.action.title);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionChangedTitle", R.string.ActionChangedTitle).replace("un2", this.messageOwner.action.title), "un1", tLRPC$Chat6);
                                        }
                                    }
                                }
                                tLRPC$Chat4 = null;
                                if (!ChatObject.isChannel(tLRPC$Chat4)) {
                                }
                                if (!isOut()) {
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatDeletePhoto) {
                                TLRPC$Peer tLRPC$Peer5 = tLRPC$Message.peer_id;
                                if (tLRPC$Peer5 != null) {
                                    long j6 = tLRPC$Peer5.channel_id;
                                    if (j6 != 0) {
                                        tLRPC$Chat3 = getChat(abstractMap2, longSparseArray2, j6);
                                        if (!ChatObject.isChannel(tLRPC$Chat3) && !tLRPC$Chat3.megagroup) {
                                            this.messageText = LocaleController.getString("ActionChannelRemovedPhoto", R.string.ActionChannelRemovedPhoto);
                                        } else if (!isOut()) {
                                            this.messageText = LocaleController.getString("ActionYouRemovedPhoto", R.string.ActionYouRemovedPhoto);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionRemovedPhoto", R.string.ActionRemovedPhoto), "un1", tLRPC$Chat6);
                                        }
                                    }
                                }
                                tLRPC$Chat3 = null;
                                if (!ChatObject.isChannel(tLRPC$Chat3)) {
                                }
                                if (!isOut()) {
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTTLChange) {
                                if (tLRPC$MessageAction.ttl != 0) {
                                    if (isOut()) {
                                        this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(this.messageOwner.action.ttl));
                                    } else {
                                        this.messageText = LocaleController.formatString("MessageLifetimeChanged", R.string.MessageLifetimeChanged, UserObject.getFirstName(tLRPC$User), LocaleController.formatTTLString(this.messageOwner.action.ttl));
                                    }
                                } else if (isOut()) {
                                    this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", R.string.MessageLifetimeYouRemoved);
                                } else {
                                    this.messageText = LocaleController.formatString("MessageLifetimeRemoved", R.string.MessageLifetimeRemoved, UserObject.getFirstName(tLRPC$User));
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) {
                                TLRPC$TL_messageActionSetMessagesTTL tLRPC$TL_messageActionSetMessagesTTL = (TLRPC$TL_messageActionSetMessagesTTL) tLRPC$MessageAction;
                                TLRPC$Peer tLRPC$Peer6 = tLRPC$Message.peer_id;
                                if (tLRPC$Peer6 != null) {
                                    long j7 = tLRPC$Peer6.channel_id;
                                    if (j7 != 0) {
                                        tLRPC$Chat2 = getChat(abstractMap2, longSparseArray2, j7);
                                        if (tLRPC$Chat2 == null && !tLRPC$Chat2.megagroup) {
                                            int i5 = tLRPC$TL_messageActionSetMessagesTTL.period;
                                            if (i5 != 0) {
                                                this.messageText = LocaleController.formatString("ActionTTLChannelChanged", R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i5));
                                            } else {
                                                this.messageText = LocaleController.getString("ActionTTLChannelDisabled", R.string.ActionTTLChannelDisabled);
                                            }
                                        } else if (tLRPC$TL_messageActionSetMessagesTTL.period == 0) {
                                            if (isOut()) {
                                                this.messageText = LocaleController.formatString("ActionTTLYouChanged", R.string.ActionTTLYouChanged, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period));
                                            } else {
                                                this.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", R.string.ActionTTLChanged, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period)), "un1", tLRPC$Chat6);
                                            }
                                        } else if (isOut()) {
                                            this.messageText = LocaleController.getString("ActionTTLYouDisabled", R.string.ActionTTLYouDisabled);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionTTLDisabled", R.string.ActionTTLDisabled), "un1", tLRPC$Chat6);
                                        }
                                    }
                                }
                                tLRPC$Chat2 = null;
                                if (tLRPC$Chat2 == null) {
                                }
                                if (tLRPC$TL_messageActionSetMessagesTTL.period == 0) {
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                                long j8 = tLRPC$Message.date * 1000;
                                if (LocaleController.getInstance().formatterDay != null && LocaleController.getInstance().formatterYear != null) {
                                    sb = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(j8), LocaleController.getInstance().formatterDay.format(j8));
                                    str = str2;
                                } else {
                                    StringBuilder sb2 = new StringBuilder();
                                    str = str2;
                                    sb2.append(str);
                                    sb2.append(this.messageOwner.date);
                                    sb = sb2.toString();
                                }
                                TLRPC$User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                                if (currentUser == null) {
                                    currentUser = getUser(abstractMap, longSparseArray, this.messageOwner.peer_id.user_id);
                                }
                                String firstName = currentUser != null ? UserObject.getFirstName(currentUser) : str;
                                int i6 = R.string.NotificationUnrecognizedDevice;
                                TLRPC$MessageAction tLRPC$MessageAction3 = this.messageOwner.action;
                                this.messageText = LocaleController.formatString("NotificationUnrecognizedDevice", i6, firstName, sb, tLRPC$MessageAction3.title, tLRPC$MessageAction3.address);
                            } else {
                                str = str2;
                                if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                                    this.messageText = LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, UserObject.getUserName(tLRPC$User));
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                                    this.messageText = LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, UserObject.getUserName(tLRPC$User));
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
                                    TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
                                    if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages) {
                                        if (isOut()) {
                                            this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", R.string.ActionTakeScreenshootYou, new Object[0]);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", R.string.ActionTakeScreenshoot), "un1", tLRPC$Chat6);
                                        }
                                    } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL) {
                                        TLRPC$TL_decryptedMessageActionSetMessageTTL tLRPC$TL_decryptedMessageActionSetMessageTTL = (TLRPC$TL_decryptedMessageActionSetMessageTTL) tLRPC$DecryptedMessageAction;
                                        if (tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds != 0) {
                                            if (isOut()) {
                                                this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                            } else {
                                                this.messageText = LocaleController.formatString("MessageLifetimeChanged", R.string.MessageLifetimeChanged, UserObject.getFirstName(tLRPC$User), LocaleController.formatTTLString(tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                            }
                                        } else if (isOut()) {
                                            this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", R.string.MessageLifetimeYouRemoved);
                                        } else {
                                            this.messageText = LocaleController.formatString("MessageLifetimeRemoved", R.string.MessageLifetimeRemoved, UserObject.getFirstName(tLRPC$User));
                                        }
                                    }
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionScreenshotTaken) {
                                    if (isOut()) {
                                        this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", R.string.ActionTakeScreenshootYou, new Object[0]);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", R.string.ActionTakeScreenshoot), "un1", tLRPC$Chat6);
                                    }
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCreatedBroadcastList) {
                                    this.messageText = LocaleController.formatString("YouCreatedBroadcastList", R.string.YouCreatedBroadcastList, new Object[0]);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChannelCreate) {
                                    TLRPC$Peer tLRPC$Peer7 = tLRPC$Message.peer_id;
                                    if (tLRPC$Peer7 != null) {
                                        long j9 = tLRPC$Peer7.channel_id;
                                        if (j9 != 0) {
                                            tLRPC$Chat = getChat(abstractMap2, longSparseArray2, j9);
                                            if (!ChatObject.isChannel(tLRPC$Chat) && tLRPC$Chat.megagroup) {
                                                this.messageText = LocaleController.getString("ActionCreateMega", R.string.ActionCreateMega);
                                            } else {
                                                this.messageText = LocaleController.getString("ActionCreateChannel", R.string.ActionCreateChannel);
                                            }
                                        }
                                    }
                                    tLRPC$Chat = null;
                                    if (!ChatObject.isChannel(tLRPC$Chat)) {
                                    }
                                    this.messageText = LocaleController.getString("ActionCreateChannel", R.string.ActionCreateChannel);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatMigrateTo) {
                                    this.messageText = LocaleController.getString("ActionMigrateFromGroup", R.string.ActionMigrateFromGroup);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChannelMigrateFrom) {
                                    this.messageText = LocaleController.getString("ActionMigrateFromGroup", R.string.ActionMigrateFromGroup);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPinMessage) {
                                    generatePinMessageText(tLRPC$User, tLRPC$User == null ? getChat(abstractMap2, longSparseArray2, tLRPC$Message.peer_id.channel_id) : null);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear) {
                                    this.messageText = LocaleController.getString("HistoryCleared", R.string.HistoryCleared);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicCreate) {
                                    this.messageText = LocaleController.getString("TopicCreated", R.string.TopicCreated);
                                    TLRPC$TL_messageActionTopicCreate tLRPC$TL_messageActionTopicCreate = (TLRPC$TL_messageActionTopicCreate) this.messageOwner.action;
                                    TLRPC$TL_forumTopic tLRPC$TL_forumTopic = new TLRPC$TL_forumTopic();
                                    tLRPC$TL_forumTopic.icon_emoji_id = tLRPC$TL_messageActionTopicCreate.icon_emoji_id;
                                    tLRPC$TL_forumTopic.title = tLRPC$TL_messageActionTopicCreate.title;
                                    tLRPC$TL_forumTopic.icon_color = tLRPC$TL_messageActionTopicCreate.icon_color;
                                    this.messageTextShort = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString("TopicWasCreatedAction", R.string.TopicWasCreatedAction), ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic, null));
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicEdit) {
                                    TLRPC$TL_messageActionTopicEdit tLRPC$TL_messageActionTopicEdit = (TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction;
                                    if (tLRPC$User != null) {
                                        str3 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                                    } else {
                                        str3 = chat != null ? chat.title : null;
                                    }
                                    String trim = str3 != null ? str3.trim() : "DELETED";
                                    TLRPC$MessageAction tLRPC$MessageAction4 = this.messageOwner.action;
                                    int i7 = tLRPC$MessageAction4.flags;
                                    if ((i7 & 4) > 0) {
                                        if (((TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction4).closed) {
                                            this.messageText = LocaleController.formatString("TopicClosed2", R.string.TopicClosed2, trim);
                                            this.messageTextShort = LocaleController.getString("TopicClosed", R.string.TopicClosed);
                                        } else {
                                            this.messageText = LocaleController.formatString("TopicRestarted2", R.string.TopicRestarted2, trim);
                                            this.messageTextShort = LocaleController.getString("TopicRestarted", R.string.TopicRestarted);
                                        }
                                    } else if ((i7 & 2) != 0 && (i7 & 1) != 0) {
                                        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = new TLRPC$TL_forumTopic();
                                        tLRPC$TL_forumTopic2.icon_emoji_id = tLRPC$TL_messageActionTopicEdit.icon_emoji_id;
                                        tLRPC$TL_forumTopic2.title = tLRPC$TL_messageActionTopicEdit.title;
                                        tLRPC$TL_forumTopic2.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString("TopicRenamedTo", R.string.TopicRenamedTo), trim), ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic2, null, this.topicIconDrawable));
                                        this.messageTextShort = LocaleController.getString("TopicRenamed", R.string.TopicRenamed);
                                    } else if ((i7 & 2) != 0) {
                                        TLRPC$TL_forumTopic tLRPC$TL_forumTopic3 = new TLRPC$TL_forumTopic();
                                        tLRPC$TL_forumTopic3.icon_emoji_id = tLRPC$TL_messageActionTopicEdit.icon_emoji_id;
                                        tLRPC$TL_forumTopic3.title = str;
                                        tLRPC$TL_forumTopic3.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString("TopicIconChangedTo", R.string.TopicIconChangedTo), trim), ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic3, null, this.topicIconDrawable));
                                        this.messageTextShort = LocaleController.getString("TopicIconChanged", R.string.TopicIconChanged);
                                    } else if ((1 & i7) != 0) {
                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString("TopicRenamedTo", R.string.TopicRenamedTo), trim), tLRPC$TL_messageActionTopicEdit.title);
                                    }
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) {
                                    generateGameMessageText(tLRPC$User);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                                    TLRPC$TL_messageActionPhoneCall tLRPC$TL_messageActionPhoneCall = (TLRPC$TL_messageActionPhoneCall) tLRPC$MessageAction;
                                    boolean z = tLRPC$TL_messageActionPhoneCall.reason instanceof TLRPC$TL_phoneCallDiscardReasonMissed;
                                    if (!isFromUser() || this.messageOwner.from_id.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        if (z) {
                                            if (tLRPC$TL_messageActionPhoneCall.video) {
                                                this.messageText = LocaleController.getString("CallMessageVideoIncomingMissed", R.string.CallMessageVideoIncomingMissed);
                                            } else {
                                                this.messageText = LocaleController.getString("CallMessageIncomingMissed", R.string.CallMessageIncomingMissed);
                                            }
                                        } else if (tLRPC$TL_messageActionPhoneCall.reason instanceof TLRPC$TL_phoneCallDiscardReasonBusy) {
                                            if (tLRPC$TL_messageActionPhoneCall.video) {
                                                this.messageText = LocaleController.getString("CallMessageVideoIncomingDeclined", R.string.CallMessageVideoIncomingDeclined);
                                            } else {
                                                this.messageText = LocaleController.getString("CallMessageIncomingDeclined", R.string.CallMessageIncomingDeclined);
                                            }
                                        } else if (tLRPC$TL_messageActionPhoneCall.video) {
                                            this.messageText = LocaleController.getString("CallMessageVideoIncoming", R.string.CallMessageVideoIncoming);
                                        } else {
                                            this.messageText = LocaleController.getString("CallMessageIncoming", R.string.CallMessageIncoming);
                                        }
                                    } else if (z) {
                                        if (tLRPC$TL_messageActionPhoneCall.video) {
                                            this.messageText = LocaleController.getString("CallMessageVideoOutgoingMissed", R.string.CallMessageVideoOutgoingMissed);
                                        } else {
                                            this.messageText = LocaleController.getString("CallMessageOutgoingMissed", R.string.CallMessageOutgoingMissed);
                                        }
                                    } else if (tLRPC$TL_messageActionPhoneCall.video) {
                                        this.messageText = LocaleController.getString("CallMessageVideoOutgoing", R.string.CallMessageVideoOutgoing);
                                    } else {
                                        this.messageText = LocaleController.getString("CallMessageOutgoing", R.string.CallMessageOutgoing);
                                    }
                                    int i8 = tLRPC$TL_messageActionPhoneCall.duration;
                                    if (i8 > 0) {
                                        String formatCallDuration = LocaleController.formatCallDuration(i8);
                                        String formatString3 = LocaleController.formatString("CallMessageWithDuration", R.string.CallMessageWithDuration, this.messageText, formatCallDuration);
                                        this.messageText = formatString3;
                                        String charSequence = formatString3.toString();
                                        int indexOf2 = charSequence.indexOf(formatCallDuration);
                                        if (indexOf2 != -1) {
                                            SpannableString spannableString = new SpannableString(this.messageText);
                                            int length = formatCallDuration.length() + indexOf2;
                                            if (indexOf2 > 0 && charSequence.charAt(indexOf2 - 1) == '(') {
                                                indexOf2--;
                                            }
                                            if (length < charSequence.length() && charSequence.charAt(length) == ')') {
                                                length++;
                                            }
                                            spannableString.setSpan(new TypefaceSpan(Typeface.DEFAULT), indexOf2, length, 0);
                                            this.messageText = spannableString;
                                        }
                                    }
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent) {
                                    generatePaymentSentMessageText(getUser(abstractMap, longSparseArray, getDialogId()));
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionBotAllowed) {
                                    String str4 = ((TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction).domain;
                                    String string2 = LocaleController.getString("ActionBotAllowed", R.string.ActionBotAllowed);
                                    int indexOf3 = string2.indexOf("%1$s");
                                    SpannableString spannableString2 = new SpannableString(String.format(string2, str4));
                                    if (indexOf3 >= 0) {
                                        spannableString2.setSpan(new URLSpanNoUnderlineBold("http://" + str4), indexOf3, str4.length() + indexOf3, 33);
                                    }
                                    this.messageText = spannableString2;
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSecureValuesSent) {
                                    TLRPC$TL_messageActionSecureValuesSent tLRPC$TL_messageActionSecureValuesSent = (TLRPC$TL_messageActionSecureValuesSent) tLRPC$MessageAction;
                                    StringBuilder sb3 = new StringBuilder();
                                    int size = tLRPC$TL_messageActionSecureValuesSent.types.size();
                                    for (int i9 = 0; i9 < size; i9++) {
                                        TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_messageActionSecureValuesSent.types.get(i9);
                                        if (sb3.length() > 0) {
                                            sb3.append(", ");
                                        }
                                        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentPhone", R.string.ActionBotDocumentPhone));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentEmail", R.string.ActionBotDocumentEmail));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentAddress", R.string.ActionBotDocumentAddress));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentIdentity", R.string.ActionBotDocumentIdentity));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentPassport", R.string.ActionBotDocumentPassport));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentDriverLicence", R.string.ActionBotDocumentDriverLicence));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentIdentityCard", R.string.ActionBotDocumentIdentityCard));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentUtilityBill", R.string.ActionBotDocumentUtilityBill));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentBankStatement", R.string.ActionBotDocumentBankStatement));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentRentalAgreement", R.string.ActionBotDocumentRentalAgreement));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentInternalPassport", R.string.ActionBotDocumentInternalPassport));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentPassportRegistration", R.string.ActionBotDocumentPassportRegistration));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                                            sb3.append(LocaleController.getString("ActionBotDocumentTemporaryRegistration", R.string.ActionBotDocumentTemporaryRegistration));
                                        }
                                    }
                                    TLRPC$Peer tLRPC$Peer8 = this.messageOwner.peer_id;
                                    this.messageText = LocaleController.formatString("ActionBotDocuments", R.string.ActionBotDocuments, UserObject.getFirstName(tLRPC$Peer8 != null ? getUser(abstractMap, longSparseArray, tLRPC$Peer8.user_id) : null), sb3.toString());
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionWebViewDataSent) {
                                    this.messageText = LocaleController.formatString("ActionBotWebViewData", R.string.ActionBotWebViewData, ((TLRPC$TL_messageActionWebViewDataSent) tLRPC$MessageAction).text);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                                    String str5 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                                    String firstName2 = UserObject.getFirstName(tLRPC$User);
                                    boolean isUserSelf = UserObject.isUserSelf(tLRPC$User);
                                    if (TextUtils.isEmpty(str5)) {
                                        if (isUserSelf) {
                                            formatString2 = LocaleController.formatString("ChatThemeDisabledYou", R.string.ChatThemeDisabledYou, new Object[0]);
                                        } else {
                                            formatString2 = LocaleController.formatString("ChatThemeDisabled", R.string.ChatThemeDisabled, firstName2, str5);
                                        }
                                        this.messageText = formatString2;
                                    } else {
                                        if (isUserSelf) {
                                            formatString = LocaleController.formatString("ChatThemeChangedYou", R.string.ChatThemeChangedYou, str5);
                                        } else {
                                            formatString = LocaleController.formatString("ChatThemeChangedTo", R.string.ChatThemeChangedTo, firstName2, str5);
                                        }
                                        this.messageText = formatString;
                                    }
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatJoinedByRequest) {
                                    if (UserObject.isUserSelf(tLRPC$User)) {
                                        if (ChatObject.isChannelAndNotMegaGroup(this.messageOwner.peer_id.channel_id, this.currentAccount)) {
                                            string = LocaleController.getString("RequestToJoinChannelApproved", R.string.RequestToJoinChannelApproved);
                                        } else {
                                            string = LocaleController.getString("RequestToJoinGroupApproved", R.string.RequestToJoinGroupApproved);
                                        }
                                        this.messageText = string;
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("UserAcceptedToGroupAction", R.string.UserAcceptedToGroupAction), "un1", tLRPC$Chat6);
                                    }
                                }
                            }
                        }
                        str = str2;
                    }
                }
                str = "";
            } else {
                str = "";
                this.isRestrictedMessage = false;
                String restrictionReason = MessagesController.getRestrictionReason(tLRPC$Message.restriction_reason);
                if (!TextUtils.isEmpty(restrictionReason)) {
                    this.messageText = restrictionReason;
                    this.isRestrictedMessage = true;
                } else if (!isMediaEmpty()) {
                    if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDice) {
                        this.messageText = getDiceEmoji();
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPoll) {
                        if (((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.quiz) {
                            this.messageText = LocaleController.getString("QuizPoll", R.string.QuizPoll);
                        } else {
                            this.messageText = LocaleController.getString("Poll", R.string.Poll);
                        }
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                        if (getMedia(this.messageOwner).ttl_seconds != 0 && !(this.messageOwner instanceof TLRPC$TL_message_secret)) {
                            this.messageText = LocaleController.getString("AttachDestructingPhoto", R.string.AttachDestructingPhoto);
                        } else if (getGroupId() != 0) {
                            this.messageText = LocaleController.getString("Album", R.string.Album);
                        } else {
                            this.messageText = LocaleController.getString("AttachPhoto", R.string.AttachPhoto);
                        }
                    } else if (isVideo() || ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) && (getDocument() instanceof TLRPC$TL_documentEmpty) && getMedia(this.messageOwner).ttl_seconds != 0)) {
                        if (getMedia(this.messageOwner).ttl_seconds != 0 && !(this.messageOwner instanceof TLRPC$TL_message_secret)) {
                            this.messageText = LocaleController.getString("AttachDestructingVideo", R.string.AttachDestructingVideo);
                        } else {
                            this.messageText = LocaleController.getString("AttachVideo", R.string.AttachVideo);
                        }
                    } else if (isVoice()) {
                        this.messageText = LocaleController.getString("AttachAudio", R.string.AttachAudio);
                    } else if (isRoundVideo()) {
                        this.messageText = LocaleController.getString("AttachRound", R.string.AttachRound);
                    } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeo) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaVenue)) {
                        this.messageText = LocaleController.getString("AttachLocation", R.string.AttachLocation);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeoLive) {
                        this.messageText = LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaContact) {
                        this.messageText = LocaleController.getString("AttachContact", R.string.AttachContact);
                        if (!TextUtils.isEmpty(getMedia(this.messageOwner).vcard)) {
                            this.vCardData = VCardData.parse(getMedia(this.messageOwner).vcard);
                        }
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                        this.messageText = this.messageOwner.message;
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice) {
                        this.messageText = getMedia(this.messageOwner).description;
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaUnsupported) {
                        this.messageText = LocaleController.getString("UnsupportedMedia", R.string.UnsupportedMedia);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) {
                        if (isSticker() || isAnimatedStickerDocument(getDocument(), true)) {
                            String stickerChar = getStickerChar();
                            if (stickerChar == null || stickerChar.length() <= 0) {
                                this.messageText = LocaleController.getString("AttachSticker", R.string.AttachSticker);
                            } else {
                                this.messageText = String.format("%s %s", stickerChar, LocaleController.getString("AttachSticker", R.string.AttachSticker));
                            }
                        } else if (isMusic()) {
                            this.messageText = LocaleController.getString("AttachMusic", R.string.AttachMusic);
                        } else if (isGif()) {
                            this.messageText = LocaleController.getString("AttachGif", R.string.AttachGif);
                        } else {
                            String documentFileName = FileLoader.getDocumentFileName(getDocument());
                            if (!TextUtils.isEmpty(documentFileName)) {
                                this.messageText = documentFileName;
                            } else {
                                this.messageText = LocaleController.getString("AttachDocument", R.string.AttachDocument);
                            }
                        }
                    }
                } else {
                    String str6 = this.messageOwner.message;
                    if (str6 != null) {
                        try {
                            if (str6.length() > 200) {
                                this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_LONG_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                            } else {
                                this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                            }
                        } catch (Throwable unused) {
                            this.messageText = this.messageOwner.message;
                        }
                    } else {
                        this.messageText = str6;
                    }
                }
            }
            if (this.messageText == null) {
                return;
            }
            this.messageText = str;
            return;
        } else {
            tLRPC$User = null;
        }
        chat = null;
        if (tLRPC$User == null) {
        }
        tLRPC$Message = this.messageOwner;
        if (!(tLRPC$Message instanceof TLRPC$TL_messageService)) {
        }
        if (this.messageText == null) {
        }
    }

    public static TLRPC$MessageMedia getMedia(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia != null) {
            TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$MessageMedia.extended_media;
            return tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia ? ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media : tLRPC$MessageMedia;
        }
        return tLRPC$MessageMedia;
    }

    public boolean hasRevealedExtendedMedia() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return tLRPC$MessageMedia != null && (tLRPC$MessageMedia.extended_media instanceof TLRPC$TL_messageExtendedMedia);
    }

    public boolean hasExtendedMedia() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia == null || tLRPC$MessageMedia.extended_media == null) ? false : true;
    }

    public boolean hasExtendedMediaPreview() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return tLRPC$MessageMedia != null && (tLRPC$MessageMedia.extended_media instanceof TLRPC$TL_messageExtendedMediaPreview);
    }

    public void setType() {
        int i;
        int i2 = this.type;
        this.type = 1000;
        this.isRoundVideoCached = 0;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if ((tLRPC$Message instanceof TLRPC$TL_message) || (tLRPC$Message instanceof TLRPC$TL_messageForwarded_old2)) {
            if (this.isRestrictedMessage) {
                this.type = 0;
            } else if (this.emojiAnimatedSticker != null || this.emojiAnimatedStickerId != null) {
                if (isSticker()) {
                    this.type = 13;
                } else {
                    this.type = 15;
                }
            } else if (!isDice() && this.emojiOnlyCount >= 1 && !this.hasUnwrappedEmoji) {
                this.type = 19;
            } else if (isMediaEmpty()) {
                this.type = 0;
                if (TextUtils.isEmpty(this.messageText) && this.eventId == 0) {
                    this.messageText = "Empty message";
                }
            } else if (hasExtendedMediaPreview()) {
                this.type = 20;
            } else if (getMedia(this.messageOwner).ttl_seconds != 0 && ((getMedia(this.messageOwner).photo instanceof TLRPC$TL_photoEmpty) || (getDocument() instanceof TLRPC$TL_documentEmpty))) {
                this.contentType = 1;
                this.type = 10;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDice) {
                this.type = 15;
                if (getMedia(this.messageOwner).document == null) {
                    getMedia(this.messageOwner).document = new TLRPC$TL_document();
                    getMedia(this.messageOwner).document.file_reference = new byte[0];
                    getMedia(this.messageOwner).document.mime_type = "application/x-tgsdice";
                    getMedia(this.messageOwner).document.dc_id = Integer.MIN_VALUE;
                    getMedia(this.messageOwner).document.id = -2147483648L;
                    TLRPC$TL_documentAttributeImageSize tLRPC$TL_documentAttributeImageSize = new TLRPC$TL_documentAttributeImageSize();
                    tLRPC$TL_documentAttributeImageSize.w = 512;
                    tLRPC$TL_documentAttributeImageSize.h = 512;
                    getMedia(this.messageOwner).document.attributes.add(tLRPC$TL_documentAttributeImageSize);
                }
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                this.type = 1;
            } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeo) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaVenue) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeoLive)) {
                this.type = 4;
            } else if (isRoundVideo()) {
                this.type = 5;
            } else if (isVideo()) {
                this.type = 3;
            } else if (isVoice()) {
                this.type = 2;
            } else if (isMusic()) {
                this.type = 14;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaContact) {
                this.type = 12;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPoll) {
                this.type = 17;
                this.checkedVotes = new ArrayList<>();
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaUnsupported) {
                this.type = 0;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) {
                TLRPC$Document document = getDocument();
                if (document != null && document.mime_type != null) {
                    if (isGifDocument(document, hasValidGroupId())) {
                        this.type = 8;
                    } else if (isSticker()) {
                        this.type = 13;
                    } else if (isAnimatedSticker()) {
                        this.type = 15;
                    } else {
                        this.type = 9;
                    }
                } else {
                    this.type = 9;
                }
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                this.type = 0;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice) {
                this.type = 0;
            }
        } else if (tLRPC$Message instanceof TLRPC$TL_messageService) {
            TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                this.type = 0;
            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftPremium) {
                this.contentType = 1;
                this.type = 18;
            } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto)) {
                this.contentType = 1;
                this.type = 11;
            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
                TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
                if ((tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages) || (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL)) {
                    this.contentType = 1;
                    this.type = 10;
                } else {
                    this.contentType = -1;
                    this.type = -1;
                }
            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear) {
                this.contentType = -1;
                this.type = -1;
            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                this.type = 16;
            } else {
                this.contentType = 1;
                this.type = 10;
            }
        }
        if (i2 == 1000 || i2 == (i = this.type) || i == 19) {
            return;
        }
        updateMessageText(MessagesController.getInstance(this.currentAccount).getUsers(), MessagesController.getInstance(this.currentAccount).getChats(), null, null);
        generateThumbs(false);
    }

    public boolean checkLayout() {
        CharSequence charSequence;
        TextPaint textPaint;
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
                if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                    textPaint = Theme.chat_msgGameTextPaint;
                } else {
                    textPaint = Theme.chat_msgTextPaint;
                }
                int[] iArr = allowsBigEmoji() ? new int[1] : null;
                CharSequence replaceEmoji = Emoji.replaceEmoji(this.messageText, textPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, iArr);
                this.messageText = replaceEmoji;
                Spannable replaceAnimatedEmoji = replaceAnimatedEmoji(replaceEmoji, this.messageOwner.entities, textPaint.getFontMetricsInt());
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

    public void resetLayout() {
        this.layoutCreated = false;
    }

    public String getMimeType() {
        TLRPC$Document document = getDocument();
        if (document != null) {
            return document.mime_type;
        }
        if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice)) {
            return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto ? "image/jpeg" : (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(this.messageOwner).webpage.photo == null) ? "" : "image/jpeg";
        }
        TLRPC$WebDocument tLRPC$WebDocument = ((TLRPC$TL_messageMediaInvoice) getMedia(this.messageOwner)).photo;
        return tLRPC$WebDocument != null ? tLRPC$WebDocument.mime_type : "";
    }

    public boolean canPreviewDocument() {
        return canPreviewDocument(getDocument());
    }

    public static boolean isAnimatedStickerDocument(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && tLRPC$Document.mime_type.equals("video/webm");
    }

    public static boolean isGifDocument(WebFile webFile) {
        return webFile != null && (webFile.mime_type.equals("image/gif") || isNewGifDocument(webFile));
    }

    public static boolean isGifDocument(TLRPC$Document tLRPC$Document) {
        return isGifDocument(tLRPC$Document, false);
    }

    public static boolean isGifDocument(TLRPC$Document tLRPC$Document, boolean z) {
        String str;
        return (tLRPC$Document == null || (str = tLRPC$Document.mime_type) == null || ((!str.equals("image/gif") || z) && !isNewGifDocument(tLRPC$Document))) ? false : true;
    }

    public static boolean isDocumentHasThumb(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && !tLRPC$Document.thumbs.isEmpty()) {
            int size = tLRPC$Document.thumbs.size();
            for (int i = 0; i < size; i++) {
                TLRPC$PhotoSize tLRPC$PhotoSize = tLRPC$Document.thumbs.get(i);
                if (tLRPC$PhotoSize != null && !(tLRPC$PhotoSize instanceof TLRPC$TL_photoSizeEmpty) && !(tLRPC$PhotoSize.location instanceof TLRPC$TL_fileLocationUnavailable)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canPreviewDocument(TLRPC$Document tLRPC$Document) {
        String str;
        if (tLRPC$Document != null && (str = tLRPC$Document.mime_type) != null) {
            String lowerCase = str.toLowerCase();
            if ((isDocumentHasThumb(tLRPC$Document) && (lowerCase.equals("image/png") || lowerCase.equals("image/jpg") || lowerCase.equals("image/jpeg"))) || (Build.VERSION.SDK_INT >= 26 && lowerCase.equals("image/heic"))) {
                for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) {
                        TLRPC$TL_documentAttributeImageSize tLRPC$TL_documentAttributeImageSize = (TLRPC$TL_documentAttributeImageSize) tLRPC$DocumentAttribute;
                        return tLRPC$TL_documentAttributeImageSize.w < 6000 && tLRPC$TL_documentAttributeImageSize.h < 6000;
                    }
                }
            } else if (BuildVars.DEBUG_PRIVATE_VERSION) {
                String documentFileName = FileLoader.getDocumentFileName(tLRPC$Document);
                if ((documentFileName.startsWith("tg_secret_sticker") && documentFileName.endsWith("json")) || documentFileName.endsWith(".svg")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isRoundVideoDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && "video/mp4".equals(tLRPC$Document.mime_type)) {
            boolean z = false;
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < tLRPC$Document.attributes.size(); i3++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i3);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    i = tLRPC$DocumentAttribute.w;
                    i2 = tLRPC$DocumentAttribute.h;
                    z = tLRPC$DocumentAttribute.round_message;
                }
            }
            if (z && i <= 1280 && i2 <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNewGifDocument(WebFile webFile) {
        if (webFile != null && "video/mp4".equals(webFile.mime_type)) {
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < webFile.attributes.size(); i3++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = webFile.attributes.get(i3);
                if (!(tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAnimated) && (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
                    i = tLRPC$DocumentAttribute.w;
                    i2 = tLRPC$DocumentAttribute.h;
                }
            }
            if (i <= 1280 && i2 <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNewGifDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null && "video/mp4".equals(tLRPC$Document.mime_type)) {
            boolean z = false;
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < tLRPC$Document.attributes.size(); i3++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i3);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAnimated) {
                    z = true;
                } else if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    i = tLRPC$DocumentAttribute.w;
                    i2 = tLRPC$DocumentAttribute.h;
                }
            }
            if (z && i <= 1280 && i2 <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSystemSignUp(MessageObject messageObject) {
        if (messageObject != null) {
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if ((tLRPC$Message instanceof TLRPC$TL_messageService) && (((TLRPC$TL_messageService) tLRPC$Message).action instanceof TLRPC$TL_messageActionContactSignUp)) {
                return true;
            }
        }
        return false;
    }

    public void generateThumbs(boolean z) {
        ArrayList<TLRPC$PhotoSize> arrayList;
        ArrayList<TLRPC$PhotoSize> arrayList2;
        ArrayList<TLRPC$PhotoSize> arrayList3;
        ArrayList<TLRPC$PhotoSize> arrayList4;
        ArrayList<TLRPC$PhotoSize> arrayList5;
        ArrayList<TLRPC$PhotoSize> arrayList6;
        ArrayList<TLRPC$PhotoSize> arrayList7;
        if (hasExtendedMediaPreview()) {
            TLRPC$TL_messageExtendedMediaPreview tLRPC$TL_messageExtendedMediaPreview = (TLRPC$TL_messageExtendedMediaPreview) this.messageOwner.media.extended_media;
            if (!z) {
                this.photoThumbs = new ArrayList<>(Collections.singletonList(tLRPC$TL_messageExtendedMediaPreview.thumb));
            } else {
                updatePhotoSizeLocations(this.photoThumbs, Collections.singletonList(tLRPC$TL_messageExtendedMediaPreview.thumb));
            }
            this.photoThumbsObject = this.messageOwner;
            if (this.strippedThumb != null) {
                return;
            }
            createStrippedThumb();
            return;
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message instanceof TLRPC$TL_messageService) {
            TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
            if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto)) {
                return;
            }
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageAction.photo;
            if (!z) {
                this.photoThumbs = new ArrayList<>(tLRPC$Photo.sizes);
            } else {
                ArrayList<TLRPC$PhotoSize> arrayList8 = this.photoThumbs;
                if (arrayList8 != null && !arrayList8.isEmpty()) {
                    for (int i = 0; i < this.photoThumbs.size(); i++) {
                        TLRPC$PhotoSize tLRPC$PhotoSize = this.photoThumbs.get(i);
                        int i2 = 0;
                        while (true) {
                            if (i2 < tLRPC$Photo.sizes.size()) {
                                TLRPC$PhotoSize tLRPC$PhotoSize2 = tLRPC$Photo.sizes.get(i2);
                                if (!(tLRPC$PhotoSize2 instanceof TLRPC$TL_photoSizeEmpty) && tLRPC$PhotoSize2.type.equals(tLRPC$PhotoSize.type)) {
                                    tLRPC$PhotoSize.location = tLRPC$PhotoSize2.location;
                                    break;
                                }
                                i2++;
                            }
                        }
                    }
                }
            }
            if (tLRPC$Photo.dc_id != 0 && (arrayList7 = this.photoThumbs) != null) {
                int size = arrayList7.size();
                for (int i3 = 0; i3 < size; i3++) {
                    TLRPC$FileLocation tLRPC$FileLocation = this.photoThumbs.get(i3).location;
                    if (tLRPC$FileLocation != null) {
                        tLRPC$FileLocation.dc_id = tLRPC$Photo.dc_id;
                        tLRPC$FileLocation.file_reference = tLRPC$Photo.file_reference;
                    }
                }
            }
            this.photoThumbsObject = this.messageOwner.action.photo;
        } else if (this.emojiAnimatedSticker != null || this.emojiAnimatedStickerId != null) {
            if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) || !isDocumentHasThumb(this.emojiAnimatedSticker)) {
                return;
            }
            if (!z || (arrayList = this.photoThumbs) == null) {
                ArrayList<TLRPC$PhotoSize> arrayList9 = new ArrayList<>();
                this.photoThumbs = arrayList9;
                arrayList9.addAll(this.emojiAnimatedSticker.thumbs);
            } else if (!arrayList.isEmpty()) {
                updatePhotoSizeLocations(this.photoThumbs, this.emojiAnimatedSticker.thumbs);
            }
            this.photoThumbsObject = this.emojiAnimatedSticker;
        } else if (getMedia(tLRPC$Message) != null && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaEmpty)) {
            if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                TLRPC$Photo tLRPC$Photo2 = getMedia(this.messageOwner).photo;
                if (!z || ((arrayList6 = this.photoThumbs) != null && arrayList6.size() != tLRPC$Photo2.sizes.size())) {
                    this.photoThumbs = new ArrayList<>(tLRPC$Photo2.sizes);
                } else {
                    ArrayList<TLRPC$PhotoSize> arrayList10 = this.photoThumbs;
                    if (arrayList10 != null && !arrayList10.isEmpty()) {
                        for (int i4 = 0; i4 < this.photoThumbs.size(); i4++) {
                            TLRPC$PhotoSize tLRPC$PhotoSize3 = this.photoThumbs.get(i4);
                            if (tLRPC$PhotoSize3 != null) {
                                int i5 = 0;
                                while (true) {
                                    if (i5 >= tLRPC$Photo2.sizes.size()) {
                                        break;
                                    }
                                    TLRPC$PhotoSize tLRPC$PhotoSize4 = tLRPC$Photo2.sizes.get(i5);
                                    if (tLRPC$PhotoSize4 != null && !(tLRPC$PhotoSize4 instanceof TLRPC$TL_photoSizeEmpty)) {
                                        if (tLRPC$PhotoSize4.type.equals(tLRPC$PhotoSize3.type)) {
                                            tLRPC$PhotoSize3.location = tLRPC$PhotoSize4.location;
                                            break;
                                        } else if ("s".equals(tLRPC$PhotoSize3.type) && (tLRPC$PhotoSize4 instanceof TLRPC$TL_photoStrippedSize)) {
                                            this.photoThumbs.set(i4, tLRPC$PhotoSize4);
                                            break;
                                        }
                                    }
                                    i5++;
                                }
                            }
                        }
                    }
                }
                this.photoThumbsObject = getMedia(this.messageOwner).photo;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) {
                TLRPC$Document document = getDocument();
                if (!isDocumentHasThumb(document)) {
                    return;
                }
                if (!z || (arrayList5 = this.photoThumbs) == null) {
                    ArrayList<TLRPC$PhotoSize> arrayList11 = new ArrayList<>();
                    this.photoThumbs = arrayList11;
                    arrayList11.addAll(document.thumbs);
                } else if (!arrayList5.isEmpty()) {
                    updatePhotoSizeLocations(this.photoThumbs, document.thumbs);
                }
                this.photoThumbsObject = document;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                TLRPC$Document tLRPC$Document = getMedia(this.messageOwner).game.document;
                if (tLRPC$Document != null && isDocumentHasThumb(tLRPC$Document)) {
                    if (!z) {
                        ArrayList<TLRPC$PhotoSize> arrayList12 = new ArrayList<>();
                        this.photoThumbs = arrayList12;
                        arrayList12.addAll(tLRPC$Document.thumbs);
                    } else {
                        ArrayList<TLRPC$PhotoSize> arrayList13 = this.photoThumbs;
                        if (arrayList13 != null && !arrayList13.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document.thumbs);
                        }
                    }
                    this.photoThumbsObject = tLRPC$Document;
                }
                TLRPC$Photo tLRPC$Photo3 = getMedia(this.messageOwner).game.photo;
                if (tLRPC$Photo3 != null) {
                    if (!z || (arrayList4 = this.photoThumbs2) == null) {
                        this.photoThumbs2 = new ArrayList<>(tLRPC$Photo3.sizes);
                    } else if (!arrayList4.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs2, tLRPC$Photo3.sizes);
                    }
                    this.photoThumbsObject2 = tLRPC$Photo3;
                }
                if (this.photoThumbs != null || (arrayList3 = this.photoThumbs2) == null) {
                    return;
                }
                this.photoThumbs = arrayList3;
                this.photoThumbs2 = null;
                this.photoThumbsObject = this.photoThumbsObject2;
                this.photoThumbsObject2 = null;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) {
                TLRPC$Photo tLRPC$Photo4 = getMedia(this.messageOwner).webpage.photo;
                TLRPC$Document tLRPC$Document2 = getMedia(this.messageOwner).webpage.document;
                if (tLRPC$Photo4 != null) {
                    if (!z || (arrayList2 = this.photoThumbs) == null) {
                        this.photoThumbs = new ArrayList<>(tLRPC$Photo4.sizes);
                    } else if (!arrayList2.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs, tLRPC$Photo4.sizes);
                    }
                    this.photoThumbsObject = tLRPC$Photo4;
                } else if (tLRPC$Document2 != null && isDocumentHasThumb(tLRPC$Document2)) {
                    if (!z) {
                        ArrayList<TLRPC$PhotoSize> arrayList14 = new ArrayList<>();
                        this.photoThumbs = arrayList14;
                        arrayList14.addAll(tLRPC$Document2.thumbs);
                    } else {
                        ArrayList<TLRPC$PhotoSize> arrayList15 = this.photoThumbs;
                        if (arrayList15 != null && !arrayList15.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document2.thumbs);
                        }
                    }
                    this.photoThumbsObject = tLRPC$Document2;
                }
            }
        }
    }

    private static void updatePhotoSizeLocations(ArrayList<TLRPC$PhotoSize> arrayList, List<TLRPC$PhotoSize> list) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC$PhotoSize tLRPC$PhotoSize = arrayList.get(i);
            if (tLRPC$PhotoSize != null) {
                int size2 = list.size();
                int i2 = 0;
                while (true) {
                    if (i2 < size2) {
                        TLRPC$PhotoSize tLRPC$PhotoSize2 = list.get(i2);
                        if (!(tLRPC$PhotoSize2 instanceof TLRPC$TL_photoSizeEmpty) && !(tLRPC$PhotoSize2 instanceof TLRPC$TL_photoCachedSize) && tLRPC$PhotoSize2 != null && tLRPC$PhotoSize2.type.equals(tLRPC$PhotoSize.type)) {
                            tLRPC$PhotoSize.location = tLRPC$PhotoSize2.location;
                            break;
                        }
                        i2++;
                    }
                }
            }
        }
    }

    public CharSequence replaceWithLink(CharSequence charSequence, String str, ArrayList<Long> arrayList, AbstractMap<Long, TLRPC$User> abstractMap, LongSparseArray<TLRPC$User> longSparseArray) {
        if (TextUtils.indexOf(charSequence, str) >= 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("");
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC$User tLRPC$User = null;
                if (abstractMap != null) {
                    tLRPC$User = abstractMap.get(arrayList.get(i));
                } else if (longSparseArray != null) {
                    tLRPC$User = longSparseArray.get(arrayList.get(i).longValue());
                }
                if (tLRPC$User == null) {
                    tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(arrayList.get(i));
                }
                if (tLRPC$User != null) {
                    String userName = UserObject.getUserName(tLRPC$User);
                    int length = spannableStringBuilder.length();
                    if (spannableStringBuilder.length() != 0) {
                        spannableStringBuilder.append((CharSequence) ", ");
                    }
                    spannableStringBuilder.append((CharSequence) userName);
                    spannableStringBuilder.setSpan(new URLSpanNoUnderlineBold("" + tLRPC$User.id), length, userName.length() + length, 33);
                }
            }
            return TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{spannableStringBuilder});
        }
        return charSequence;
    }

    public static CharSequence replaceWithLink(CharSequence charSequence, String str, TLObject tLObject) {
        String str2;
        CharSequence charSequence2;
        String str3;
        TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User tLRPC$User;
        int indexOf = TextUtils.indexOf(charSequence, str);
        if (indexOf >= 0) {
            TLObject tLObject2 = null;
            if (tLObject instanceof TLRPC$User) {
                charSequence2 = UserObject.getUserName((TLRPC$User) tLObject).replace('\n', ' ');
                str2 = "" + tLRPC$User.id;
            } else if (tLObject instanceof TLRPC$Chat) {
                charSequence2 = ((TLRPC$Chat) tLObject).title.replace('\n', ' ');
                str2 = "" + (-tLRPC$Chat.id);
            } else if (tLObject instanceof TLRPC$TL_game) {
                charSequence2 = ((TLRPC$TL_game) tLObject).title.replace('\n', ' ');
                str2 = "game";
            } else {
                if (tLObject instanceof TLRPC$TL_chatInviteExported) {
                    TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported2 = (TLRPC$TL_chatInviteExported) tLObject;
                    charSequence2 = tLRPC$TL_chatInviteExported2.link.replace('\n', ' ');
                    str3 = "invite";
                    tLRPC$TL_chatInviteExported = tLRPC$TL_chatInviteExported2;
                } else if (tLObject instanceof TLRPC$ForumTopic) {
                    charSequence2 = ForumUtilities.getTopicSpannedName((TLRPC$ForumTopic) tLObject, null);
                    str3 = "topic";
                    tLRPC$TL_chatInviteExported = tLObject;
                } else {
                    str2 = "0";
                    charSequence2 = "";
                }
                String str4 = str3;
                tLObject2 = tLRPC$TL_chatInviteExported;
                str2 = str4;
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{charSequence2}));
            URLSpanNoUnderlineBold uRLSpanNoUnderlineBold = new URLSpanNoUnderlineBold("" + str2);
            uRLSpanNoUnderlineBold.setObject(tLObject2);
            spannableStringBuilder.setSpan(uRLSpanNoUnderlineBold, indexOf, charSequence2.length() + indexOf, 33);
            return spannableStringBuilder;
        }
        return charSequence;
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

    public String getFileName() {
        return getFileName(this.messageOwner);
    }

    public static String getFileName(TLRPC$Message tLRPC$Message) {
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument) {
            return FileLoader.getAttachFileName(getDocument(tLRPC$Message));
        }
        if (!(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto)) {
            return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage ? FileLoader.getAttachFileName(getMedia(tLRPC$Message).webpage.document) : "";
        }
        ArrayList<TLRPC$PhotoSize> arrayList = getMedia(tLRPC$Message).photo.sizes;
        return (arrayList.size() <= 0 || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) == null) ? "" : FileLoader.getAttachFileName(closestPhotoSizeWithSize);
    }

    public int getMediaType() {
        if (isVideo()) {
            return 2;
        }
        if (isVoice()) {
            return 1;
        }
        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) {
            return 3;
        }
        return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto ? 0 : 4;
    }

    private static boolean containsUrls(CharSequence charSequence) {
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
                    } else if (i3 == 2) {
                        return true;
                    } else {
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

    public void generateLinkDescription() {
        int i;
        int i2;
        if (this.linkDescription != null) {
            return;
        }
        if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage) && getMedia(this.messageOwner).webpage.description != null) {
            this.linkDescription = Spannable.Factory.getInstance().newSpannable(getMedia(this.messageOwner).webpage.description);
            String str = getMedia(this.messageOwner).webpage.site_name;
            if (str != null) {
                str = str.toLowerCase();
            }
            if ("instagram".equals(str)) {
                i2 = 1;
            } else {
                i2 = "twitter".equals(str) ? 2 : 0;
            }
            i = i2;
        } else {
            if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) && getMedia(this.messageOwner).game.description != null) {
                this.linkDescription = Spannable.Factory.getInstance().newSpannable(getMedia(this.messageOwner).game.description);
            } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice) && getMedia(this.messageOwner).description != null) {
                this.linkDescription = Spannable.Factory.getInstance().newSpannable(getMedia(this.messageOwner).description);
            }
            i = 0;
        }
        if (TextUtils.isEmpty(this.linkDescription)) {
            return;
        }
        if (containsUrls(this.linkDescription)) {
            try {
                AndroidUtilities.addLinks((Spannable) this.linkDescription, 1);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        CharSequence replaceEmoji = Emoji.replaceEmoji(this.linkDescription, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
        this.linkDescription = replaceEmoji;
        ArrayList<TLRPC$MessageEntity> arrayList = this.webPageDescriptionEntities;
        if (arrayList != null) {
            addEntitiesToText(replaceEmoji, arrayList, isOut(), false, false, true);
            replaceAnimatedEmoji(this.linkDescription, this.webPageDescriptionEntities, Theme.chat_msgTextPaint.getFontMetricsInt());
        }
        if (i == 0) {
            return;
        }
        if (!(this.linkDescription instanceof Spannable)) {
            this.linkDescription = new SpannableStringBuilder(this.linkDescription);
        }
        addUrlsByPattern(isOutOwner(), this.linkDescription, false, i, 0, false);
    }

    public CharSequence getVoiceTranscription() {
        String str;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (str = tLRPC$Message.voiceTranscription) == null) {
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            SpannableString spannableString = new SpannableString(LocaleController.getString("NoWordsRecognized", R.string.NoWordsRecognized));
            spannableString.setSpan(new CharacterStyle() { // from class: org.telegram.messenger.MessageObject.1
                @Override // android.text.style.CharacterStyle
                public void updateDrawState(TextPaint textPaint) {
                    textPaint.setTextSize(textPaint.getTextSize() * 0.8f);
                    textPaint.setColor(Theme.chat_timePaint.getColor());
                }
            }, 0, spannableString.length(), 33);
            return spannableString;
        }
        String str2 = this.messageOwner.voiceTranscription;
        return !TextUtils.isEmpty(str2) ? Emoji.replaceEmoji(str2, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false) : str2;
    }

    public float measureVoiceTranscriptionHeight() {
        StaticLayout staticLayout;
        CharSequence voiceTranscription = getVoiceTranscription();
        if (voiceTranscription == null) {
            return 0.0f;
        }
        int dp = AndroidUtilities.displaySize.x - AndroidUtilities.dp(needDrawAvatar() ? 147.0f : 95.0f);
        if (Build.VERSION.SDK_INT >= 24) {
            staticLayout = StaticLayout.Builder.obtain(voiceTranscription, 0, voiceTranscription.length(), Theme.chat_msgTextPaint, dp).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
        } else {
            staticLayout = new StaticLayout(voiceTranscription, Theme.chat_msgTextPaint, dp, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        return staticLayout.getHeight();
    }

    public boolean isVoiceTranscriptionOpen() {
        if (UserConfig.getInstance(this.currentAccount).isPremium() && this.messageOwner != null && (isVoice() || (isRoundVideo() && TranscribeButton.isVideoTranscriptionOpen(this)))) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message.voiceTranscriptionOpen && tLRPC$Message.voiceTranscription != null && (tLRPC$Message.voiceTranscriptionFinal || TranscribeButton.isTranscribing(this))) {
                return true;
            }
        }
        return false;
    }

    public void generateCaption() {
        if (this.caption != null || isRoundVideo()) {
            return;
        }
        if (hasExtendedMedia()) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            tLRPC$Message.message = tLRPC$Message.media.description;
        }
        if (isMediaEmpty() || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) || TextUtils.isEmpty(this.messageOwner.message)) {
            return;
        }
        boolean z = false;
        CharSequence replaceEmoji = Emoji.replaceEmoji(this.messageOwner.message, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
        this.caption = replaceEmoji;
        this.caption = replaceAnimatedEmoji(replaceEmoji, this.messageOwner.entities, Theme.chat_msgTextPaint.getFontMetricsInt());
        TLRPC$Message tLRPC$Message2 = this.messageOwner;
        if (!(tLRPC$Message2.send_state != 0 ? false : !tLRPC$Message2.entities.isEmpty()) && (this.eventId != 0 || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto_old) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto_layer68) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto_layer74) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument_old) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument_layer68) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument_layer74) || ((isOut() && this.messageOwner.send_state != 0) || this.messageOwner.id < 0))) {
            z = true;
        }
        if (z) {
            if (containsUrls(this.caption)) {
                try {
                    AndroidUtilities.addLinks((Spannable) this.caption, 5);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            addUrlsByPattern(isOutOwner(), this.caption, true, 0, 0, true);
        }
        addEntitiesToText(this.caption, z);
        if (isVideo()) {
            addUrlsByPattern(isOutOwner(), this.caption, true, 3, getDuration(), false);
        } else if (!isMusic() && !isVoice()) {
        } else {
            addUrlsByPattern(isOutOwner(), this.caption, true, 4, getDuration(), false);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x01f8  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01bb A[Catch: Exception -> 0x0214, TryCatch #0 {Exception -> 0x0214, blocks: (B:10:0x0011, B:12:0x0015, B:13:0x001d, B:14:0x0049, B:15:0x004c, B:17:0x0052, B:20:0x0061, B:24:0x0071, B:25:0x0073, B:31:0x008c, B:35:0x01fa, B:37:0x0204, B:39:0x0207, B:40:0x020d, B:44:0x00ae, B:47:0x00d4, B:48:0x00f5, B:49:0x0116, B:52:0x011e, B:55:0x012d, B:58:0x0136, B:69:0x0086, B:72:0x0147, B:75:0x0186, B:79:0x0199, B:82:0x01a8, B:84:0x01b2, B:87:0x01b6, B:89:0x01bb, B:94:0x01c7, B:95:0x01f4, B:96:0x01de, B:104:0x0024, B:106:0x0028, B:107:0x0030, B:108:0x0037, B:110:0x003b, B:111:0x0043), top: B:6:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01c5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void addUrlsByPattern(boolean z, CharSequence charSequence, boolean z2, int i, int i2, boolean z3) {
        Matcher matcher;
        String str;
        URLSpan[] uRLSpanArr;
        int i3;
        URLSpanNoUnderline uRLSpanNoUnderline;
        ClickableSpan[] clickableSpanArr;
        if (charSequence == null) {
            return;
        }
        int i4 = 1;
        try {
            if (i == 3 || i == 4) {
                if (videoTimeUrlPattern == null) {
                    videoTimeUrlPattern = Pattern.compile("\\b(?:(\\d{1,2}):)?(\\d{1,3}):([0-5][0-9])\\b(?: - |)([^\\n]*)");
                }
                matcher = videoTimeUrlPattern.matcher(charSequence);
            } else if (i == 1) {
                if (instagramUrlPattern == null) {
                    instagramUrlPattern = Pattern.compile("(^|\\s|\\()@[a-zA-Z\\d_.]{1,32}|(^|\\s|\\()#[\\w.]+");
                }
                matcher = instagramUrlPattern.matcher(charSequence);
            } else {
                if (urlPattern == null) {
                    urlPattern = Pattern.compile("(^|\\s)/[a-zA-Z@\\d_]{1,255}|(^|\\s|\\()@[a-zA-Z\\d_]{1,32}|(^|\\s|\\()#[^0-9][\\w.]+|(^|\\s)\\$[A-Z]{3,8}([ ,.]|$)");
                }
                matcher = urlPattern.matcher(charSequence);
            }
            Spannable spannable = (Spannable) charSequence;
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                if (i != 3 && i != 4) {
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
                    if (i == i4) {
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
                        uRLSpanNoUnderline = new URLSpanNoUnderline(charSequence.subSequence(start, end).toString());
                    } else if (z2) {
                        uRLSpanNoUnderline = new URLSpanBotCommand(charSequence.subSequence(start, end).toString(), z ? 1 : 0);
                    } else {
                        uRLSpanNoUnderline = null;
                        if (uRLSpanNoUnderline != null) {
                            if (z3 && (clickableSpanArr = (ClickableSpan[]) spannable.getSpans(start, end, ClickableSpan.class)) != null && clickableSpanArr.length > 0) {
                                spannable.removeSpan(clickableSpanArr[0]);
                            }
                            spannable.setSpan(uRLSpanNoUnderline, start, end, 0);
                        }
                        i4 = 1;
                    }
                    if (uRLSpanNoUnderline != null) {
                    }
                    i4 = 1;
                }
                matcher.groupCount();
                int start2 = matcher.start(i4);
                int end2 = matcher.end(i4);
                int start3 = matcher.start(2);
                int end3 = matcher.end(2);
                int start4 = matcher.start(3);
                int end4 = matcher.end(3);
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
                            i3 += intValue3 * 60 * 60;
                        }
                        if (i3 <= i2) {
                            if (i == 3) {
                                uRLSpanNoUnderline = new URLSpanNoUnderline("video?" + i3);
                            } else {
                                uRLSpanNoUnderline = new URLSpanNoUnderline("audio?" + i3);
                            }
                            uRLSpanNoUnderline.label = str;
                            if (uRLSpanNoUnderline != null) {
                            }
                        }
                    }
                    i4 = 1;
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
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static int[] getWebDocumentWidthAndHeight(TLRPC$WebDocument tLRPC$WebDocument) {
        if (tLRPC$WebDocument == null) {
            return null;
        }
        int size = tLRPC$WebDocument.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$WebDocument.attributes.get(i);
            if (!(tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) && !(tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
            }
            return new int[]{tLRPC$DocumentAttribute.w, tLRPC$DocumentAttribute.h};
        }
        return null;
    }

    public static int getWebDocumentDuration(TLRPC$WebDocument tLRPC$WebDocument) {
        if (tLRPC$WebDocument == null) {
            return 0;
        }
        int size = tLRPC$WebDocument.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$WebDocument.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                return tLRPC$DocumentAttribute.duration;
            }
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                return tLRPC$DocumentAttribute.duration;
            }
        }
        return 0;
    }

    public static int[] getInlineResultWidthAndHeight(TLRPC$BotInlineResult tLRPC$BotInlineResult) {
        int[] webDocumentWidthAndHeight = getWebDocumentWidthAndHeight(tLRPC$BotInlineResult.content);
        if (webDocumentWidthAndHeight == null) {
            int[] webDocumentWidthAndHeight2 = getWebDocumentWidthAndHeight(tLRPC$BotInlineResult.thumb);
            return webDocumentWidthAndHeight2 == null ? new int[]{0, 0} : webDocumentWidthAndHeight2;
        }
        return webDocumentWidthAndHeight;
    }

    public static int getInlineResultDuration(TLRPC$BotInlineResult tLRPC$BotInlineResult) {
        int webDocumentDuration = getWebDocumentDuration(tLRPC$BotInlineResult.content);
        return webDocumentDuration == 0 ? getWebDocumentDuration(tLRPC$BotInlineResult.thumb) : webDocumentDuration;
    }

    public boolean hasValidGroupId() {
        ArrayList<TLRPC$PhotoSize> arrayList;
        return getGroupId() != 0 && (((arrayList = this.photoThumbs) != null && !arrayList.isEmpty()) || isMusic() || isDocument());
    }

    public long getGroupIdForUse() {
        long j = this.localSentGroupId;
        return j != 0 ? j : this.messageOwner.grouped_id;
    }

    public long getGroupId() {
        long j = this.localGroupId;
        return j != 0 ? j : getGroupIdForUse();
    }

    public static void addLinks(boolean z, CharSequence charSequence) {
        addLinks(z, charSequence, true, false);
    }

    public static void addLinks(boolean z, CharSequence charSequence, boolean z2, boolean z3) {
        addLinks(z, charSequence, z2, z3, false);
    }

    public static void addLinks(boolean z, CharSequence charSequence, boolean z2, boolean z3, boolean z4) {
        if (!(charSequence instanceof Spannable) || !containsUrls(charSequence)) {
            return;
        }
        if (charSequence.length() < 1000) {
            try {
                AndroidUtilities.addLinks((Spannable) charSequence, 5, z4);
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            try {
                AndroidUtilities.addLinks((Spannable) charSequence, 1, z4);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        addUrlsByPattern(z, charSequence, z2, 0, 0, z3);
    }

    public void resetPlayingProgress() {
        this.audioProgress = 0.0f;
        this.audioProgressSec = 0;
        this.bufferedProgress = 0.0f;
    }

    private boolean addEntitiesToText(CharSequence charSequence, boolean z) {
        return addEntitiesToText(charSequence, false, z);
    }

    public boolean addEntitiesToText(CharSequence charSequence, boolean z, boolean z2) {
        if (charSequence == null) {
            return false;
        }
        if (this.isRestrictedMessage) {
            ArrayList arrayList = new ArrayList();
            TLRPC$TL_messageEntityItalic tLRPC$TL_messageEntityItalic = new TLRPC$TL_messageEntityItalic();
            tLRPC$TL_messageEntityItalic.offset = 0;
            tLRPC$TL_messageEntityItalic.length = charSequence.length();
            arrayList.add(tLRPC$TL_messageEntityItalic);
            return addEntitiesToText(charSequence, arrayList, isOutOwner(), true, z, z2);
        }
        return addEntitiesToText(charSequence, this.messageOwner.entities, isOutOwner(), true, z, z2);
    }

    public void replaceEmojiToLottieFrame(CharSequence charSequence, int[] iArr) {
        if (!(charSequence instanceof Spannable)) {
            return;
        }
        Spannable spannable = (Spannable) charSequence;
        Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) spannable.getSpans(0, spannable.length(), Emoji.EmojiSpan.class);
        AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannable.getSpans(0, spannable.length(), AnimatedEmojiSpan.class);
        if (emojiSpanArr == null) {
            return;
        }
        if (((iArr == null ? 0 : iArr[0]) - emojiSpanArr.length) - (animatedEmojiSpanArr == null ? 0 : animatedEmojiSpanArr.length) > 0) {
            return;
        }
        for (int i = 0; i < emojiSpanArr.length; i++) {
            TLRPC$Document emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(emojiSpanArr[i].emoji);
            if (emojiAnimatedSticker != null) {
                int spanStart = spannable.getSpanStart(emojiSpanArr[i]);
                int spanEnd = spannable.getSpanEnd(emojiSpanArr[i]);
                spannable.removeSpan(emojiSpanArr[i]);
                AnimatedEmojiSpan animatedEmojiSpan = new AnimatedEmojiSpan(emojiAnimatedSticker, emojiSpanArr[i].fontMetrics);
                animatedEmojiSpan.standard = true;
                spannable.setSpan(animatedEmojiSpan, spanStart, spanEnd, 33);
            }
        }
    }

    public static Spannable replaceAnimatedEmoji(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, Paint.FontMetricsInt fontMetricsInt) {
        AnimatedEmojiSpan animatedEmojiSpan;
        Spannable spannableString = charSequence instanceof Spannable ? (Spannable) charSequence : new SpannableString(charSequence);
        if (arrayList == null) {
            return spannableString;
        }
        Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) spannableString.getSpans(0, spannableString.length(), Emoji.EmojiSpan.class);
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC$MessageEntity tLRPC$MessageEntity = arrayList.get(i);
            if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji) {
                TLRPC$TL_messageEntityCustomEmoji tLRPC$TL_messageEntityCustomEmoji = (TLRPC$TL_messageEntityCustomEmoji) tLRPC$MessageEntity;
                for (int i2 = 0; i2 < emojiSpanArr.length; i2++) {
                    Emoji.EmojiSpan emojiSpan = emojiSpanArr[i2];
                    if (emojiSpan != null) {
                        int spanStart = spannableString.getSpanStart(emojiSpan);
                        int spanEnd = spannableString.getSpanEnd(emojiSpan);
                        if (tLRPC$TL_messageEntityCustomEmoji.offset == spanStart && tLRPC$TL_messageEntityCustomEmoji.length == spanEnd - spanStart) {
                            spannableString.removeSpan(emojiSpan);
                            emojiSpanArr[i2] = null;
                        }
                    }
                }
                if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length <= spannableString.length()) {
                    int i3 = tLRPC$MessageEntity.offset;
                    AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannableString.getSpans(i3, tLRPC$MessageEntity.length + i3, AnimatedEmojiSpan.class);
                    if (animatedEmojiSpanArr != null && animatedEmojiSpanArr.length > 0) {
                        for (AnimatedEmojiSpan animatedEmojiSpan2 : animatedEmojiSpanArr) {
                            spannableString.removeSpan(animatedEmojiSpan2);
                        }
                    }
                    if (tLRPC$TL_messageEntityCustomEmoji.document != null) {
                        animatedEmojiSpan = new AnimatedEmojiSpan(tLRPC$TL_messageEntityCustomEmoji.document, fontMetricsInt);
                    } else {
                        animatedEmojiSpan = new AnimatedEmojiSpan(tLRPC$TL_messageEntityCustomEmoji.document_id, fontMetricsInt);
                    }
                    int i4 = tLRPC$MessageEntity.offset;
                    spannableString.setSpan(animatedEmojiSpan, i4, tLRPC$MessageEntity.length + i4, 33);
                }
            }
        }
        return spannableString;
    }

    /* JADX WARN: Removed duplicated region for block: B:170:0x03e9  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x03fd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x017b  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x022f A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, boolean z, boolean z2, boolean z3, boolean z4) {
        String str;
        String str2;
        int i;
        boolean z5;
        int i2;
        int size;
        int i3;
        if (!(charSequence instanceof Spannable)) {
            return false;
        }
        Spannable spannable = (Spannable) charSequence;
        URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, charSequence.length(), URLSpan.class);
        boolean z6 = uRLSpanArr != null && uRLSpanArr.length > 0;
        if (arrayList.isEmpty()) {
            return z6;
        }
        byte b = z3 ? (byte) 2 : z ? (byte) 1 : (byte) 0;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList(arrayList);
        Collections.sort(arrayList3, MessageObject$$ExternalSyntheticLambda2.INSTANCE);
        int size2 = arrayList3.size();
        int i4 = 0;
        while (true) {
            str = null;
            if (i4 >= size2) {
                break;
            }
            TLRPC$MessageEntity tLRPC$MessageEntity = (TLRPC$MessageEntity) arrayList3.get(i4);
            if (tLRPC$MessageEntity.length > 0 && (i2 = tLRPC$MessageEntity.offset) >= 0 && i2 < charSequence.length()) {
                if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length > charSequence.length()) {
                    tLRPC$MessageEntity.length = charSequence.length() - tLRPC$MessageEntity.offset;
                }
                if ((!z4 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji)) && uRLSpanArr != null && uRLSpanArr.length > 0) {
                    for (int i5 = 0; i5 < uRLSpanArr.length; i5++) {
                        if (uRLSpanArr[i5] != null) {
                            int spanStart = spannable.getSpanStart(uRLSpanArr[i5]);
                            int spanEnd = spannable.getSpanEnd(uRLSpanArr[i5]);
                            int i6 = tLRPC$MessageEntity.offset;
                            if ((i6 <= spanStart && tLRPC$MessageEntity.length + i6 >= spanStart) || (i6 <= spanEnd && i6 + tLRPC$MessageEntity.length >= spanEnd)) {
                                spannable.removeSpan(uRLSpanArr[i5]);
                                uRLSpanArr[i5] = null;
                            }
                        }
                    }
                }
                if (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji)) {
                    TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                    int i7 = tLRPC$MessageEntity.offset;
                    textStyleRun.start = i7;
                    textStyleRun.end = i7 + tLRPC$MessageEntity.length;
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
                    } else {
                        if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) {
                            textStyleRun.flags = 2;
                        } else if ((tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre)) {
                            textStyleRun.flags = 4;
                        } else {
                            if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) {
                                if (z2) {
                                    textStyleRun.flags = 64;
                                    textStyleRun.urlEntity = tLRPC$MessageEntity;
                                }
                            } else if (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) {
                                if (z2) {
                                    textStyleRun.flags = 64;
                                    textStyleRun.urlEntity = tLRPC$MessageEntity;
                                }
                            } else if ((!z4 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl)) && (((!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUrl) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl)) || !Browser.isPassportUrl(tLRPC$MessageEntity.url)) && (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMention) || z2))) {
                                textStyleRun.flags = ConnectionsManager.RequestFlagNeedQuickAck;
                                textStyleRun.urlEntity = tLRPC$MessageEntity;
                            }
                            i4++;
                        }
                        size = arrayList2.size();
                        i3 = 0;
                        while (i3 < size) {
                            TextStyleSpan.TextStyleRun textStyleRun2 = (TextStyleSpan.TextStyleRun) arrayList2.get(i3);
                            if ((textStyleRun2.flags & 256) == 0 || textStyleRun.start < textStyleRun2.start || textStyleRun.end > textStyleRun2.end) {
                                int i8 = textStyleRun.start;
                                int i9 = textStyleRun2.start;
                                if (i8 > i9) {
                                    int i10 = textStyleRun2.end;
                                    if (i8 < i10) {
                                        if (textStyleRun.end < i10) {
                                            TextStyleSpan.TextStyleRun textStyleRun3 = new TextStyleSpan.TextStyleRun(textStyleRun);
                                            textStyleRun3.merge(textStyleRun2);
                                            int i11 = i3 + 1;
                                            arrayList2.add(i11, textStyleRun3);
                                            TextStyleSpan.TextStyleRun textStyleRun4 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                            textStyleRun4.start = textStyleRun.end;
                                            i3 = i11 + 1;
                                            size = size + 1 + 1;
                                            arrayList2.add(i3, textStyleRun4);
                                        } else {
                                            TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun(textStyleRun);
                                            textStyleRun5.merge(textStyleRun2);
                                            textStyleRun5.end = textStyleRun2.end;
                                            i3++;
                                            size++;
                                            arrayList2.add(i3, textStyleRun5);
                                        }
                                        int i12 = textStyleRun.start;
                                        textStyleRun.start = textStyleRun2.end;
                                        textStyleRun2.end = i12;
                                    }
                                } else {
                                    int i13 = textStyleRun.end;
                                    if (i9 < i13) {
                                        int i14 = textStyleRun2.end;
                                        if (i13 == i14) {
                                            textStyleRun2.merge(textStyleRun);
                                        } else if (i13 < i14) {
                                            TextStyleSpan.TextStyleRun textStyleRun6 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                            textStyleRun6.merge(textStyleRun);
                                            textStyleRun6.end = textStyleRun.end;
                                            i3++;
                                            size++;
                                            arrayList2.add(i3, textStyleRun6);
                                            textStyleRun2.start = textStyleRun.end;
                                        } else {
                                            TextStyleSpan.TextStyleRun textStyleRun7 = new TextStyleSpan.TextStyleRun(textStyleRun);
                                            textStyleRun7.start = textStyleRun2.end;
                                            i3++;
                                            size++;
                                            arrayList2.add(i3, textStyleRun7);
                                            textStyleRun2.merge(textStyleRun);
                                        }
                                        textStyleRun.end = i9;
                                    }
                                }
                            }
                            i3++;
                            size = size;
                        }
                        if (textStyleRun.start >= textStyleRun.end) {
                            arrayList2.add(textStyleRun);
                        }
                        i4++;
                    }
                    size = arrayList2.size();
                    i3 = 0;
                    while (i3 < size) {
                    }
                    if (textStyleRun.start >= textStyleRun.end) {
                    }
                    i4++;
                }
            }
            i4++;
        }
        int size3 = arrayList2.size();
        boolean z7 = z6;
        int i15 = 0;
        while (i15 < size3) {
            TextStyleSpan.TextStyleRun textStyleRun8 = (TextStyleSpan.TextStyleRun) arrayList2.get(i15);
            TLRPC$MessageEntity tLRPC$MessageEntity2 = textStyleRun8.urlEntity;
            if (tLRPC$MessageEntity2 != null) {
                int i16 = tLRPC$MessageEntity2.offset;
                str2 = TextUtils.substring(charSequence, i16, tLRPC$MessageEntity2.length + i16);
            } else {
                str2 = str;
            }
            TLRPC$MessageEntity tLRPC$MessageEntity3 = textStyleRun8.urlEntity;
            if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBotCommand) {
                spannable.setSpan(new URLSpanBotCommand(str2, b, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
            } else {
                if ((tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityHashtag) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMention) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityCashtag)) {
                    i = 33;
                    spannable.setSpan(new URLSpanNoUnderline(str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityEmail) {
                    spannable.setSpan(new URLSpanReplacement("mailto:" + str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                } else {
                    if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityUrl) {
                        if (!str2.toLowerCase().contains("://")) {
                            spannable.setSpan(new URLSpanBrowser("http://" + str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                        } else {
                            spannable.setSpan(new URLSpanBrowser(str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                        }
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBankCard) {
                        spannable.setSpan(new URLSpanNoUnderline("card:" + str2, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityPhone) {
                        String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str2);
                        if (str2.startsWith("+")) {
                            stripExceptNumbers = "+" + stripExceptNumbers;
                        }
                        spannable.setSpan(new URLSpanBrowser("tel:" + stripExceptNumbers, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityTextUrl) {
                        spannable.setSpan(new URLSpanReplacement(textStyleRun8.urlEntity.url, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMentionName) {
                        spannable.setSpan(new URLSpanUserMention("" + ((TLRPC$TL_messageEntityMentionName) textStyleRun8.urlEntity).user_id, b, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_inputMessageEntityMentionName) {
                        spannable.setSpan(new URLSpanUserMention("" + ((TLRPC$TL_inputMessageEntityMentionName) textStyleRun8.urlEntity).user_id.user_id, b, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else if ((textStyleRun8.flags & 4) != 0) {
                        i = 33;
                        spannable.setSpan(new URLSpanMono(spannable, textStyleRun8.start, textStyleRun8.end, b, textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                    } else {
                        i = 33;
                        spannable.setSpan(new TextStyleSpan(textStyleRun8), textStyleRun8.start, textStyleRun8.end, 33);
                        z5 = true;
                        if (!z5 && (textStyleRun8.flags & 256) != 0) {
                            spannable.setSpan(new TextStyleSpan(textStyleRun8), textStyleRun8.start, textStyleRun8.end, i);
                        }
                        i15++;
                        str = null;
                    }
                    z5 = false;
                    z7 = true;
                    i = 33;
                    if (!z5) {
                        spannable.setSpan(new TextStyleSpan(textStyleRun8), textStyleRun8.start, textStyleRun8.end, i);
                    }
                    i15++;
                    str = null;
                }
                z5 = false;
                if (!z5) {
                }
                i15++;
                str = null;
            }
            i = 33;
            z5 = false;
            if (!z5) {
            }
            i15++;
            str = null;
        }
        return z7;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$addEntitiesToText$2(TLRPC$MessageEntity tLRPC$MessageEntity, TLRPC$MessageEntity tLRPC$MessageEntity2) {
        int i = tLRPC$MessageEntity.offset;
        int i2 = tLRPC$MessageEntity2.offset;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    public boolean needDrawShareButton() {
        int i;
        if (!this.preview && !this.scheduled && this.eventId == 0) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message.noforwards) {
                return false;
            }
            if (tLRPC$Message.fwd_from != null && !isOutOwner() && this.messageOwner.fwd_from.saved_from_peer != null && getDialogId() == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                return true;
            }
            int i2 = this.type;
            if (i2 != 13 && i2 != 15 && i2 != 19) {
                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                if (tLRPC$MessageFwdHeader != null && (tLRPC$MessageFwdHeader.from_id instanceof TLRPC$TL_peerChannel) && !isOutOwner()) {
                    return true;
                }
                if (isFromUser()) {
                    if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaEmpty) || getMedia(this.messageOwner) == null || ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && !(getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage))) {
                        return false;
                    }
                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
                    if (user != null && user.bot && !hasExtendedMedia()) {
                        return true;
                    }
                    if (!isOut()) {
                        if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) || (((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice) && !hasExtendedMedia()) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage))) {
                            return true;
                        }
                        TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
                        TLRPC$Chat tLRPC$Chat = null;
                        if (tLRPC$Peer != null) {
                            long j = tLRPC$Peer.channel_id;
                            if (j != 0) {
                                tLRPC$Chat = getChat(null, null, j);
                            }
                        }
                        return ChatObject.isChannel(tLRPC$Chat) && tLRPC$Chat.megagroup && ChatObject.isPublic(tLRPC$Chat) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaContact) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeo);
                    }
                } else {
                    TLRPC$Message tLRPC$Message2 = this.messageOwner;
                    if ((tLRPC$Message2.from_id instanceof TLRPC$TL_peerChannel) || tLRPC$Message2.post) {
                        if ((getMedia(tLRPC$Message2) instanceof TLRPC$TL_messageMediaWebPage) && !isOutOwner()) {
                            return true;
                        }
                        if (isSupergroup()) {
                            return false;
                        }
                        TLRPC$Message tLRPC$Message3 = this.messageOwner;
                        if (tLRPC$Message3.peer_id.channel_id != 0 && ((tLRPC$Message3.via_bot_id == 0 && tLRPC$Message3.reply_to == null) || ((i = this.type) != 13 && i != 15))) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    public boolean isYouTubeVideo() {
        return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && !TextUtils.isEmpty(getMedia(this.messageOwner).webpage.embed_url) && "YouTube".equals(getMedia(this.messageOwner).webpage.site_name);
    }

    public int getMaxMessageTextWidth() {
        if (AndroidUtilities.isTablet() && this.eventId != 0) {
            this.generatedWithMinSize = AndroidUtilities.dp(530.0f);
        } else {
            this.generatedWithMinSize = AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : getParentWidth();
        }
        this.generatedWithDensity = AndroidUtilities.density;
        int i = 0;
        if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_background".equals(getMedia(this.messageOwner).webpage.type)) {
            try {
                Uri parse = Uri.parse(getMedia(this.messageOwner).webpage.url);
                String lastPathSegment = parse.getLastPathSegment();
                if (parse.getQueryParameter("bg_color") != null) {
                    i = AndroidUtilities.dp(220.0f);
                } else if (lastPathSegment.length() == 6 || (lastPathSegment.length() == 13 && lastPathSegment.charAt(6) == '-')) {
                    i = AndroidUtilities.dp(200.0f);
                }
            } catch (Exception unused) {
            }
        } else if (isAndroidTheme()) {
            i = AndroidUtilities.dp(200.0f);
        }
        if (i == 0) {
            int dp = this.generatedWithMinSize - AndroidUtilities.dp((!needDrawAvatarInternal() || isOutOwner() || this.messageOwner.isThreadMessage) ? 80.0f : 132.0f);
            if (needDrawShareButton() && !isOutOwner()) {
                dp -= AndroidUtilities.dp(10.0f);
            }
            i = getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame ? dp - AndroidUtilities.dp(10.0f) : dp;
        }
        int i2 = this.emojiOnlyCount;
        if (i2 >= 1) {
            int i3 = this.totalAnimatedEmojiCount;
            int i4 = 100;
            if (i3 > 100) {
                return i;
            }
            int i5 = i2 - i3;
            if (SharedConfig.getDevicePerformanceClass() < 2) {
                i4 = 50;
            }
            return i5 < i4 ? (hasValidReplyMessageObject() || isForwarded()) ? Math.min(i, (int) (this.generatedWithMinSize * 0.65f)) : i : i;
        }
        return i;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(21:(1:87)(1:288)|88|(3:90|(1:(1:(2:94|(1:96))(1:97))(1:98))|99)(5:195|(1:197)(19:200|201|202|(3:247|248|(22:250|251|252|253|254|255|256|257|258|259|260|(2:262|(1:264))(1:267)|265|266|211|212|213|(3:226|227|(1:229))|215|216|(3:218|219|220)(1:225)|221))|204|205|206|207|208|209|210|211|212|213|(0)|215|216|(0)(0)|221)|198|199|172)|100|(1:102)|103|104|105|106|(2:110|111)|117|118|119|(1:121)|122|(1:124)|125|(6:127|(11:129|130|131|132|133|134|(1:136)(1:156)|137|(1:139)(1:155)|(6:143|144|145|(1:150)|147|148)|149)|163|164|(2:166|(1:168))(3:173|(1:175)|176)|169)(3:177|(5:179|(1:181)|182|(1:184)(1:187)|185)(1:188)|186)|170|171|172) */
    /* JADX WARN: Can't wrap try/catch for region: R(7:200|(2:201|202)|(12:(3:247|248|(22:250|251|252|253|254|255|256|257|258|259|260|(2:262|(1:264))(1:267)|265|266|211|212|213|(3:226|227|(1:229))|215|216|(3:218|219|220)(1:225)|221))|208|209|210|211|212|213|(0)|215|216|(0)(0)|221)|204|205|206|207) */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x03d7, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x03d8, code lost:
        org.telegram.messenger.FileLog.e(r0);
        r0 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x03c4, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x03c5, code lost:
        r7 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x0529, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x052a, code lost:
        r25 = r4;
        r27 = r8;
        r4 = r9;
        r9 = r10;
        r20 = r11;
        r22 = r13;
        r23 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x007f, code lost:
        if ((getMedia(r0) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaUnsupported) == false) goto L302;
     */
    /* JADX WARN: Removed duplicated region for block: B:121:0x03e6  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x03eb  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x04c2  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0374  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0397  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x034f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0186  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0154 A[Catch: Exception -> 0x056e, TRY_LEAVE, TryCatch #0 {Exception -> 0x056e, blocks: (B:68:0x0118, B:70:0x0122, B:72:0x0140, B:74:0x0145, B:75:0x0148, B:294:0x0154), top: B:67:0x0118 }] */
    /* JADX WARN: Removed duplicated region for block: B:299:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0110  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0122 A[Catch: Exception -> 0x056e, TryCatch #0 {Exception -> 0x056e, blocks: (B:68:0x0118, B:70:0x0122, B:72:0x0140, B:74:0x0145, B:75:0x0148, B:294:0x0154), top: B:67:0x0118 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0183  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01a7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLayout(TLRPC$User tLRPC$User) {
        TLRPC$Message tLRPC$Message;
        boolean z;
        TextPaint textPaint;
        int i;
        int i2;
        Layout.Alignment alignment;
        float f;
        StaticLayout staticLayout;
        int lineCount;
        int ceil;
        int i3;
        int min;
        boolean z2;
        int i4;
        int i5;
        int i6;
        int i7;
        StaticLayout staticLayout2;
        TextPaint textPaint2;
        Layout.Alignment alignment2;
        SpannableStringBuilder valueOf;
        StaticLayout staticLayout3;
        StaticLayout staticLayout4;
        float f2;
        StaticLayout.Builder hyphenationFrequency;
        Layout.Alignment alignment3;
        TextLayoutBlock textLayoutBlock;
        int i8;
        int i9;
        int i10;
        StaticLayout staticLayout5;
        int i11;
        int i12;
        float f3;
        int ceil2;
        int i13;
        int i14;
        int i15;
        float f4;
        float f5;
        StaticLayout staticLayout6;
        float f6;
        float f7;
        float f8;
        int i16;
        int i17;
        MessageObject messageObject;
        int i18 = this.type;
        if ((i18 == 0 || i18 == 19) && this.messageOwner.peer_id != null && !TextUtils.isEmpty(this.messageText)) {
            generateLinkDescription();
            this.textLayoutBlocks = new ArrayList<>();
            this.textWidth = 0;
            int i19 = 1;
            try {
                if (!(this.messageOwner.send_state != 0 ? false : !tLRPC$Message.entities.isEmpty())) {
                    if (this.eventId == 0) {
                        TLRPC$Message tLRPC$Message2 = this.messageOwner;
                        if (!(tLRPC$Message2 instanceof TLRPC$TL_message_old) && !(tLRPC$Message2 instanceof TLRPC$TL_message_old2) && !(tLRPC$Message2 instanceof TLRPC$TL_message_old3) && !(tLRPC$Message2 instanceof TLRPC$TL_message_old4) && !(tLRPC$Message2 instanceof TLRPC$TL_messageForwarded_old) && !(tLRPC$Message2 instanceof TLRPC$TL_messageForwarded_old2) && !(tLRPC$Message2 instanceof TLRPC$TL_message_secret) && !(getMedia(tLRPC$Message2) instanceof TLRPC$TL_messageMediaInvoice) && (!isOut() || this.messageOwner.send_state == 0)) {
                            TLRPC$Message tLRPC$Message3 = this.messageOwner;
                            if (tLRPC$Message3.id >= 0) {
                            }
                        }
                    }
                    z = true;
                    if (z) {
                        addLinks(isOutOwner(), this.messageText, true, true);
                    }
                    if (!isYouTubeVideo() || ((messageObject = this.replyMessageObject) != null && messageObject.isYouTubeVideo())) {
                        addUrlsByPattern(isOutOwner(), this.messageText, false, 3, ConnectionsManager.DEFAULT_DATACENTER_ID, false);
                    } else {
                        MessageObject messageObject2 = this.replyMessageObject;
                        if (messageObject2 != null) {
                            if (messageObject2.isVideo()) {
                                addUrlsByPattern(isOutOwner(), this.messageText, false, 3, this.replyMessageObject.getDuration(), false);
                            } else if (this.replyMessageObject.isMusic() || this.replyMessageObject.isVoice()) {
                                addUrlsByPattern(isOutOwner(), this.messageText, false, 4, this.replyMessageObject.getDuration(), false);
                            }
                        }
                    }
                    boolean addEntitiesToText = addEntitiesToText(this.messageText, z);
                    int maxMessageTextWidth = getMaxMessageTextWidth();
                    if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame)) {
                        textPaint = Theme.chat_msgGameTextPaint;
                    } else {
                        textPaint = Theme.chat_msgTextPaint;
                    }
                    TextPaint textPaint3 = textPaint;
                    float f9 = 0.0f;
                    float f10 = this.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f;
                    Layout.Alignment alignment4 = Layout.Alignment.ALIGN_NORMAL;
                    i = Build.VERSION.SDK_INT;
                    if (i < 24) {
                        CharSequence charSequence = this.messageText;
                        StaticLayout.Builder alignment5 = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint3, maxMessageTextWidth).setLineSpacing(f10, 1.0f).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(alignment4);
                        if (this.emojiOnlyCount > 0) {
                            alignment5.setIncludePad(false);
                            if (i >= 28) {
                                alignment5.setUseLineSpacingFromFallbacks(false);
                            }
                        }
                        alignment = alignment4;
                        f = f10;
                        i2 = 24;
                        staticLayout = alignment5.build();
                    } else {
                        i2 = 24;
                        alignment = alignment4;
                        f = f10;
                        staticLayout = new StaticLayout(this.messageText, textPaint3, maxMessageTextWidth, alignment4, 1.0f, f10, false);
                    }
                    this.textHeight = staticLayout.getHeight();
                    this.linesCount = staticLayout.getLineCount();
                    int i20 = this.totalAnimatedEmojiCount;
                    int i21 = i20 < 50 ? 5 : 10;
                    boolean z3 = i < i2 && i20 < 50;
                    ceil = !z3 ? 1 : (int) Math.ceil(lineCount / i21);
                    int i22 = 0;
                    i3 = 0;
                    float f11 = 0.0f;
                    while (i3 < ceil) {
                        if (z3) {
                            min = this.linesCount;
                        } else {
                            min = Math.min(i21, this.linesCount - i22);
                        }
                        TextLayoutBlock textLayoutBlock2 = new TextLayoutBlock();
                        if (ceil == i19) {
                            textLayoutBlock2.textLayout = staticLayout;
                            textLayoutBlock2.textYOffset = f9;
                            textLayoutBlock2.charactersOffset = 0;
                            textLayoutBlock2.charactersEnd = staticLayout.getText().length();
                            int i23 = this.emojiOnlyCount;
                            if (i23 != 0) {
                                if (i23 == i19) {
                                    this.textHeight -= AndroidUtilities.dp(5.3f);
                                    textLayoutBlock2.textYOffset -= AndroidUtilities.dp(5.3f);
                                } else if (i23 == 2) {
                                    this.textHeight -= AndroidUtilities.dp(4.5f);
                                    textLayoutBlock2.textYOffset -= AndroidUtilities.dp(4.5f);
                                } else if (i23 == 3) {
                                    this.textHeight -= AndroidUtilities.dp(4.2f);
                                    textLayoutBlock2.textYOffset -= AndroidUtilities.dp(4.2f);
                                }
                            }
                            textLayoutBlock2.height = this.textHeight;
                            textLayoutBlock = textLayoutBlock2;
                            i8 = i22;
                            i11 = i3;
                            i6 = ceil;
                            i7 = i21;
                            staticLayout5 = staticLayout;
                            textPaint2 = textPaint3;
                            alignment3 = alignment;
                            i12 = min;
                        } else {
                            int lineStart = staticLayout.getLineStart(i22);
                            int lineEnd = staticLayout.getLineEnd((i22 + min) - i19);
                            if (lineEnd < lineStart) {
                                z2 = addEntitiesToText;
                                i4 = i22;
                                i5 = i3;
                                i6 = ceil;
                                i7 = i21;
                                staticLayout2 = staticLayout;
                                textPaint2 = textPaint3;
                                alignment2 = alignment;
                            } else {
                                textLayoutBlock2.charactersOffset = lineStart;
                                textLayoutBlock2.charactersEnd = lineEnd;
                                try {
                                    valueOf = SpannableStringBuilder.valueOf(this.messageText.subSequence(lineStart, lineEnd));
                                } catch (Exception e) {
                                    e = e;
                                    z2 = addEntitiesToText;
                                    i4 = i22;
                                    i5 = i3;
                                    i6 = ceil;
                                    i7 = i21;
                                    staticLayout2 = staticLayout;
                                    textPaint2 = textPaint3;
                                    alignment2 = alignment;
                                }
                                try {
                                    try {
                                        if (addEntitiesToText) {
                                            try {
                                                int i24 = Build.VERSION.SDK_INT;
                                                if (i24 >= i2) {
                                                    try {
                                                        staticLayout4 = staticLayout;
                                                    } catch (Exception e2) {
                                                        e = e2;
                                                        staticLayout4 = staticLayout;
                                                    }
                                                    try {
                                                        f2 = f;
                                                        try {
                                                            hyphenationFrequency = StaticLayout.Builder.obtain(valueOf, 0, valueOf.length(), textPaint3, maxMessageTextWidth + AndroidUtilities.dp(2.0f)).setLineSpacing(f2, 1.0f).setBreakStrategy(i19).setHyphenationFrequency(0);
                                                            alignment3 = alignment;
                                                        } catch (Exception e3) {
                                                            e = e3;
                                                            i4 = i22;
                                                            i5 = i3;
                                                            i6 = ceil;
                                                            i7 = i21;
                                                            f = f2;
                                                            textPaint2 = textPaint3;
                                                            alignment2 = alignment;
                                                            staticLayout2 = staticLayout4;
                                                            z2 = addEntitiesToText;
                                                            FileLog.e(e);
                                                            i22 = i4;
                                                            int i25 = i5 + 1;
                                                            ceil = i6;
                                                            i21 = i7;
                                                            textPaint3 = textPaint2;
                                                            addEntitiesToText = z2;
                                                            alignment = alignment2;
                                                            staticLayout = staticLayout2;
                                                            i2 = 24;
                                                            i19 = 1;
                                                            f9 = 0.0f;
                                                            i3 = i25;
                                                        }
                                                    } catch (Exception e4) {
                                                        e = e4;
                                                        i4 = i22;
                                                        i5 = i3;
                                                        i6 = ceil;
                                                        i7 = i21;
                                                        textPaint2 = textPaint3;
                                                        alignment2 = alignment;
                                                        staticLayout2 = staticLayout4;
                                                        z2 = addEntitiesToText;
                                                        FileLog.e(e);
                                                        i22 = i4;
                                                        int i252 = i5 + 1;
                                                        ceil = i6;
                                                        i21 = i7;
                                                        textPaint3 = textPaint2;
                                                        addEntitiesToText = z2;
                                                        alignment = alignment2;
                                                        staticLayout = staticLayout2;
                                                        i2 = 24;
                                                        i19 = 1;
                                                        f9 = 0.0f;
                                                        i3 = i252;
                                                    }
                                                    try {
                                                        StaticLayout.Builder alignment6 = hyphenationFrequency.setAlignment(alignment3);
                                                        if (this.emojiOnlyCount > 0) {
                                                            alignment6.setIncludePad(false);
                                                            if (i24 >= 28) {
                                                                alignment6.setUseLineSpacingFromFallbacks(false);
                                                            }
                                                        }
                                                        textLayoutBlock2.textLayout = alignment6.build();
                                                        textLayoutBlock = textLayoutBlock2;
                                                        i8 = i22;
                                                        i9 = i3;
                                                        i10 = ceil;
                                                        i7 = i21;
                                                        f = f2;
                                                        textPaint2 = textPaint3;
                                                        staticLayout5 = staticLayout4;
                                                        float lineTop = staticLayout5.getLineTop(i8);
                                                        textLayoutBlock.textYOffset = lineTop;
                                                        i11 = i9;
                                                        if (i11 != 0) {
                                                            try {
                                                                if (this.emojiOnlyCount <= 0) {
                                                                    textLayoutBlock.height = (int) (lineTop - f11);
                                                                }
                                                            } catch (Exception e5) {
                                                                e = e5;
                                                                z2 = addEntitiesToText;
                                                                alignment2 = alignment3;
                                                                staticLayout2 = staticLayout5;
                                                                i4 = i8;
                                                                i5 = i11;
                                                                i6 = i10;
                                                                FileLog.e(e);
                                                                i22 = i4;
                                                                int i2522 = i5 + 1;
                                                                ceil = i6;
                                                                i21 = i7;
                                                                textPaint3 = textPaint2;
                                                                addEntitiesToText = z2;
                                                                alignment = alignment2;
                                                                staticLayout = staticLayout2;
                                                                i2 = 24;
                                                                i19 = 1;
                                                                f9 = 0.0f;
                                                                i3 = i2522;
                                                            }
                                                        }
                                                        int i26 = textLayoutBlock.height;
                                                        StaticLayout staticLayout7 = textLayoutBlock.textLayout;
                                                        textLayoutBlock.height = Math.max(i26, staticLayout7.getLineBottom(staticLayout7.getLineCount() - 1));
                                                        float f12 = textLayoutBlock.textYOffset;
                                                        i6 = i10;
                                                        if (i11 != i6 - 1) {
                                                            i12 = Math.max(min, textLayoutBlock.textLayout.getLineCount());
                                                            try {
                                                                this.textHeight = Math.max(this.textHeight, (int) (textLayoutBlock.textYOffset + textLayoutBlock.textLayout.getHeight()));
                                                            } catch (Exception e6) {
                                                                FileLog.e(e6);
                                                            }
                                                        } else {
                                                            i12 = min;
                                                        }
                                                        f11 = f12;
                                                    } catch (Exception e7) {
                                                        e = e7;
                                                        alignment2 = alignment3;
                                                        i4 = i22;
                                                        i5 = i3;
                                                        i6 = ceil;
                                                        i7 = i21;
                                                        f = f2;
                                                        textPaint2 = textPaint3;
                                                        staticLayout2 = staticLayout4;
                                                        z2 = addEntitiesToText;
                                                        FileLog.e(e);
                                                        i22 = i4;
                                                        int i25222 = i5 + 1;
                                                        ceil = i6;
                                                        i21 = i7;
                                                        textPaint3 = textPaint2;
                                                        addEntitiesToText = z2;
                                                        alignment = alignment2;
                                                        staticLayout = staticLayout2;
                                                        i2 = 24;
                                                        i19 = 1;
                                                        f9 = 0.0f;
                                                        i3 = i25222;
                                                    }
                                                }
                                            } catch (Exception e8) {
                                                e = e8;
                                                staticLayout3 = staticLayout;
                                                i4 = i22;
                                                i5 = i3;
                                                i6 = ceil;
                                                i7 = i21;
                                                textPaint2 = textPaint3;
                                                alignment2 = alignment;
                                                staticLayout2 = staticLayout3;
                                                z2 = addEntitiesToText;
                                                FileLog.e(e);
                                                i22 = i4;
                                                int i252222 = i5 + 1;
                                                ceil = i6;
                                                i21 = i7;
                                                textPaint3 = textPaint2;
                                                addEntitiesToText = z2;
                                                alignment = alignment2;
                                                staticLayout = staticLayout2;
                                                i2 = 24;
                                                i19 = 1;
                                                f9 = 0.0f;
                                                i3 = i252222;
                                            }
                                        }
                                        float lineTop2 = staticLayout5.getLineTop(i8);
                                        textLayoutBlock.textYOffset = lineTop2;
                                        i11 = i9;
                                        if (i11 != 0) {
                                        }
                                        int i262 = textLayoutBlock.height;
                                        StaticLayout staticLayout72 = textLayoutBlock.textLayout;
                                        textLayoutBlock.height = Math.max(i262, staticLayout72.getLineBottom(staticLayout72.getLineCount() - 1));
                                        float f122 = textLayoutBlock.textYOffset;
                                        i6 = i10;
                                        if (i11 != i6 - 1) {
                                        }
                                        f11 = f122;
                                    } catch (Exception e9) {
                                        e = e9;
                                        z2 = addEntitiesToText;
                                        alignment2 = alignment3;
                                        staticLayout2 = staticLayout5;
                                        i4 = i8;
                                        i5 = i9;
                                        i6 = i10;
                                        FileLog.e(e);
                                        i22 = i4;
                                        int i2522222 = i5 + 1;
                                        ceil = i6;
                                        i21 = i7;
                                        textPaint3 = textPaint2;
                                        addEntitiesToText = z2;
                                        alignment = alignment2;
                                        staticLayout = staticLayout2;
                                        i2 = 24;
                                        i19 = 1;
                                        f9 = 0.0f;
                                        i3 = i2522222;
                                    }
                                    textLayoutBlock.textLayout = new StaticLayout(valueOf, 0, valueOf.length(), textPaint3, maxMessageTextWidth, alignment3, 1.0f, f3, false);
                                    i8 = i4;
                                    staticLayout5 = staticLayout2;
                                } catch (Exception e10) {
                                    e = e10;
                                    z2 = addEntitiesToText;
                                    alignment2 = alignment3;
                                    i5 = i9;
                                    i6 = i10;
                                    FileLog.e(e);
                                    i22 = i4;
                                    int i25222222 = i5 + 1;
                                    ceil = i6;
                                    i21 = i7;
                                    textPaint3 = textPaint2;
                                    addEntitiesToText = z2;
                                    alignment = alignment2;
                                    staticLayout = staticLayout2;
                                    i2 = 24;
                                    i19 = 1;
                                    f9 = 0.0f;
                                    i3 = i25222222;
                                }
                                staticLayout3 = staticLayout;
                                alignment3 = alignment;
                                f3 = f;
                                textLayoutBlock = textLayoutBlock2;
                                i4 = i22;
                                i9 = i3;
                                i10 = ceil;
                                i7 = i21;
                                staticLayout2 = staticLayout3;
                                f = f3;
                                textPaint2 = textPaint3;
                            }
                            i22 = i4;
                            int i252222222 = i5 + 1;
                            ceil = i6;
                            i21 = i7;
                            textPaint3 = textPaint2;
                            addEntitiesToText = z2;
                            alignment = alignment2;
                            staticLayout = staticLayout2;
                            i2 = 24;
                            i19 = 1;
                            f9 = 0.0f;
                            i3 = i252222222;
                        }
                        textLayoutBlock.spoilers.clear();
                        if (!this.isSpoilersRevealed) {
                            SpoilerEffect.addSpoilers(null, textLayoutBlock.textLayout, null, textLayoutBlock.spoilers);
                        }
                        this.textLayoutBlocks.add(textLayoutBlock);
                        float f13 = textLayoutBlock.textLayout.getLineLeft(i12 - 1);
                        float f14 = 0.0f;
                        if (i11 == 0 && f13 >= 0.0f) {
                            try {
                                this.textXOffset = f13;
                            } catch (Exception e11) {
                                e = e11;
                                if (i11 == 0) {
                                    this.textXOffset = f14;
                                }
                                FileLog.e(e);
                                f13 = 0.0f;
                                float f15 = textLayoutBlock.textLayout.getLineWidth(i12 - 1);
                                ceil2 = (int) Math.ceil(f15);
                                if (ceil2 > maxMessageTextWidth + 80) {
                                }
                                i13 = i6 - 1;
                                if (i11 == i13) {
                                }
                                float f16 = ceil2;
                                int i27 = i11;
                                int ceil3 = (int) Math.ceil(Math.max(f14, f13) + f16);
                                if (i12 <= 1) {
                                }
                                i22 = i14 + i15;
                                int i2522222222 = i5 + 1;
                                ceil = i6;
                                i21 = i7;
                                textPaint3 = textPaint2;
                                addEntitiesToText = z2;
                                alignment = alignment2;
                                staticLayout = staticLayout2;
                                i2 = 24;
                                i19 = 1;
                                f9 = 0.0f;
                                i3 = i2522222222;
                            }
                        }
                        float f152 = textLayoutBlock.textLayout.getLineWidth(i12 - 1);
                        ceil2 = (int) Math.ceil(f152);
                        if (ceil2 > maxMessageTextWidth + 80) {
                            ceil2 = maxMessageTextWidth;
                        }
                        i13 = i6 - 1;
                        if (i11 == i13) {
                            this.lastLineWidth = ceil2;
                        }
                        float f162 = ceil2;
                        int i272 = i11;
                        int ceil32 = (int) Math.ceil(Math.max(f14, f13) + f162);
                        if (i12 <= 1) {
                            int i28 = ceil2;
                            z2 = addEntitiesToText;
                            alignment2 = alignment3;
                            int i29 = ceil32;
                            float f17 = 0.0f;
                            int i30 = 0;
                            boolean z4 = false;
                            float f18 = 0.0f;
                            while (i30 < i12) {
                                try {
                                    f4 = textLayoutBlock.textLayout.getLineWidth(i30);
                                } catch (Exception e12) {
                                    FileLog.e(e12);
                                    f4 = 0.0f;
                                }
                                try {
                                    f5 = textLayoutBlock.textLayout.getLineLeft(i30);
                                } catch (Exception e13) {
                                    FileLog.e(e13);
                                    f5 = 0.0f;
                                }
                                float f19 = f5;
                                if (f4 > maxMessageTextWidth + 20) {
                                    staticLayout6 = staticLayout5;
                                    f8 = 0.0f;
                                    f6 = maxMessageTextWidth;
                                    f7 = 0.0f;
                                } else {
                                    staticLayout6 = staticLayout5;
                                    f6 = f4;
                                    f7 = f19;
                                    f8 = 0.0f;
                                }
                                if (f7 > f8) {
                                    i16 = i8;
                                    this.textXOffset = Math.min(this.textXOffset, f7);
                                    i17 = i12;
                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                    this.hasRtl = true;
                                } else {
                                    i16 = i8;
                                    i17 = i12;
                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                }
                                if (!z4 && f7 == 0.0f) {
                                    try {
                                        if (textLayoutBlock.textLayout.getParagraphDirection(i30) != 1) {
                                        }
                                    } catch (Exception unused) {
                                    }
                                    z4 = true;
                                }
                                f17 = Math.max(f17, f6);
                                float f20 = f7 + f6;
                                f18 = Math.max(f18, f20);
                                i28 = Math.max(i28, (int) Math.ceil(f6));
                                i29 = Math.max(i29, (int) Math.ceil(f20));
                                i30++;
                                i12 = i17;
                                i8 = i16;
                                staticLayout5 = staticLayout6;
                            }
                            staticLayout2 = staticLayout5;
                            i14 = i8;
                            i15 = i12;
                            if (z4) {
                                i5 = i272;
                                if (i5 == i13) {
                                    this.lastLineWidth = ceil32;
                                }
                            } else {
                                i5 = i272;
                                if (i5 == i13) {
                                    this.lastLineWidth = i28;
                                }
                                f18 = f17;
                            }
                            this.textWidth = Math.max(this.textWidth, (int) Math.ceil(f18));
                        } else {
                            z2 = addEntitiesToText;
                            alignment2 = alignment3;
                            staticLayout2 = staticLayout5;
                            i14 = i8;
                            i15 = i12;
                            i5 = i272;
                            if (f13 > 0.0f) {
                                float min2 = Math.min(this.textXOffset, f13);
                                this.textXOffset = min2;
                                if (min2 == 0.0f) {
                                    ceil2 = (int) (f162 + f13);
                                }
                                this.hasRtl = i6 != 1;
                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                            } else {
                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                            }
                            this.textWidth = Math.max(this.textWidth, Math.min(maxMessageTextWidth, ceil2));
                        }
                        i22 = i14 + i15;
                        int i25222222222 = i5 + 1;
                        ceil = i6;
                        i21 = i7;
                        textPaint3 = textPaint2;
                        addEntitiesToText = z2;
                        alignment = alignment2;
                        staticLayout = staticLayout2;
                        i2 = 24;
                        i19 = 1;
                        f9 = 0.0f;
                        i3 = i25222222222;
                    }
                    return;
                }
                i = Build.VERSION.SDK_INT;
                if (i < 24) {
                }
                this.textHeight = staticLayout.getHeight();
                this.linesCount = staticLayout.getLineCount();
                int i202 = this.totalAnimatedEmojiCount;
                if (i202 < 50) {
                }
                if (i < i2) {
                }
                if (!z3) {
                }
                int i222 = 0;
                i3 = 0;
                float f112 = 0.0f;
                while (i3 < ceil) {
                }
                return;
            } catch (Exception e14) {
                FileLog.e(e14);
                return;
            }
            z = false;
            if (z) {
            }
            if (!isYouTubeVideo()) {
            }
            addUrlsByPattern(isOutOwner(), this.messageText, false, 3, ConnectionsManager.DEFAULT_DATACENTER_ID, false);
            boolean addEntitiesToText2 = addEntitiesToText(this.messageText, z);
            int maxMessageTextWidth2 = getMaxMessageTextWidth();
            if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame)) {
            }
            TextPaint textPaint32 = textPaint;
            float f92 = 0.0f;
            if (this.totalAnimatedEmojiCount < 4) {
            }
            Layout.Alignment alignment42 = Layout.Alignment.ALIGN_NORMAL;
        }
    }

    public boolean isOut() {
        return this.messageOwner.out;
    }

    public boolean isOutOwner() {
        TLRPC$Peer tLRPC$Peer;
        if (this.preview) {
            return true;
        }
        TLRPC$Peer tLRPC$Peer2 = this.messageOwner.peer_id;
        TLRPC$Chat tLRPC$Chat = null;
        if (tLRPC$Peer2 != null) {
            long j = tLRPC$Peer2.channel_id;
            if (j != 0) {
                tLRPC$Chat = getChat(null, null, j);
            }
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.out) {
            TLRPC$Peer tLRPC$Peer3 = tLRPC$Message.from_id;
            if ((tLRPC$Peer3 instanceof TLRPC$TL_peerUser) || ((tLRPC$Peer3 instanceof TLRPC$TL_peerChannel) && (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup))) {
                TLRPC$Message tLRPC$Message2 = this.messageOwner;
                if (!tLRPC$Message2.post) {
                    if (tLRPC$Message2.fwd_from == null) {
                        return true;
                    }
                    long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    if (getDialogId() == clientUserId) {
                        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                        TLRPC$Peer tLRPC$Peer4 = tLRPC$MessageFwdHeader.from_id;
                        if ((tLRPC$Peer4 instanceof TLRPC$TL_peerUser) && tLRPC$Peer4.user_id == clientUserId && ((tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) == null || tLRPC$Peer.user_id == clientUserId)) {
                            return true;
                        }
                        TLRPC$Peer tLRPC$Peer5 = tLRPC$MessageFwdHeader.saved_from_peer;
                        return tLRPC$Peer5 != null && tLRPC$Peer5.user_id == clientUserId && (tLRPC$Peer4 == null || tLRPC$Peer4.user_id == clientUserId);
                    }
                    TLRPC$Peer tLRPC$Peer6 = this.messageOwner.fwd_from.saved_from_peer;
                    return tLRPC$Peer6 == null || tLRPC$Peer6.user_id == clientUserId;
                }
            }
        }
        return false;
    }

    public boolean needDrawAvatar() {
        if (this.customAvatarDrawable != null) {
            return true;
        }
        if (isSponsored() && (isFromChat() || this.sponsoredShowPeerPhoto)) {
            return true;
        }
        if (!isSponsored()) {
            if (isFromUser() || isFromGroup() || this.eventId != 0) {
                return true;
            }
            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
            if (tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.saved_from_peer != null) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needDrawAvatarInternal() {
        if (this.customAvatarDrawable != null) {
            return true;
        }
        if (!isSponsored()) {
            if ((isFromChat() && isFromUser()) || isFromGroup() || this.eventId != 0) {
                return true;
            }
            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
            if (tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.saved_from_peer != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isFromChat() {
        TLRPC$Peer tLRPC$Peer;
        if (getDialogId() == UserConfig.getInstance(this.currentAccount).clientUserId) {
            return true;
        }
        TLRPC$Peer tLRPC$Peer2 = this.messageOwner.peer_id;
        TLRPC$Chat tLRPC$Chat = null;
        if (tLRPC$Peer2 != null) {
            long j = tLRPC$Peer2.channel_id;
            if (j != 0) {
                tLRPC$Chat = getChat(null, null, j);
            }
        }
        if ((ChatObject.isChannel(tLRPC$Chat) && tLRPC$Chat.megagroup) || ((tLRPC$Peer = this.messageOwner.peer_id) != null && tLRPC$Peer.chat_id != 0)) {
            return true;
        }
        return (tLRPC$Peer == null || tLRPC$Peer.channel_id == 0 || tLRPC$Chat == null || !tLRPC$Chat.megagroup) ? false : true;
    }

    public static long getFromChatId(TLRPC$Message tLRPC$Message) {
        return getPeerId(tLRPC$Message.from_id);
    }

    public static long getPeerId(TLRPC$Peer tLRPC$Peer) {
        long j;
        if (tLRPC$Peer == null) {
            return 0L;
        }
        if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
            j = tLRPC$Peer.chat_id;
        } else if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
            j = tLRPC$Peer.channel_id;
        } else {
            return tLRPC$Peer.user_id;
        }
        return -j;
    }

    public static boolean peersEqual(TLRPC$Peer tLRPC$Peer, TLRPC$Peer tLRPC$Peer2) {
        if (tLRPC$Peer == null && tLRPC$Peer2 == null) {
            return true;
        }
        if (tLRPC$Peer != null && tLRPC$Peer2 != null) {
            return (!(tLRPC$Peer instanceof TLRPC$TL_peerChat) || !(tLRPC$Peer2 instanceof TLRPC$TL_peerChat)) ? (!(tLRPC$Peer instanceof TLRPC$TL_peerChannel) || !(tLRPC$Peer2 instanceof TLRPC$TL_peerChannel)) ? (tLRPC$Peer instanceof TLRPC$TL_peerUser) && (tLRPC$Peer2 instanceof TLRPC$TL_peerUser) && tLRPC$Peer.user_id == tLRPC$Peer2.user_id : tLRPC$Peer.channel_id == tLRPC$Peer2.channel_id : tLRPC$Peer.chat_id == tLRPC$Peer2.chat_id;
        }
        return false;
    }

    public long getFromChatId() {
        return getFromChatId(this.messageOwner);
    }

    public long getChatId() {
        TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
        if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
            return tLRPC$Peer.chat_id;
        }
        if (!(tLRPC$Peer instanceof TLRPC$TL_peerChannel)) {
            return 0L;
        }
        return tLRPC$Peer.channel_id;
    }

    public boolean isFromUser() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return (tLRPC$Message.from_id instanceof TLRPC$TL_peerUser) && !tLRPC$Message.post;
    }

    public boolean isFromGroup() {
        TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
        TLRPC$Chat tLRPC$Chat = null;
        if (tLRPC$Peer != null) {
            long j = tLRPC$Peer.channel_id;
            if (j != 0) {
                tLRPC$Chat = getChat(null, null, j);
            }
        }
        return (this.messageOwner.from_id instanceof TLRPC$TL_peerChannel) && ChatObject.isChannel(tLRPC$Chat) && tLRPC$Chat.megagroup;
    }

    public boolean isForwardedChannelPost() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
        if ((tLRPC$Peer instanceof TLRPC$TL_peerChannel) && (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) != null && tLRPC$MessageFwdHeader.channel_post != 0) {
            TLRPC$Peer tLRPC$Peer2 = tLRPC$MessageFwdHeader.saved_from_peer;
            if ((tLRPC$Peer2 instanceof TLRPC$TL_peerChannel) && tLRPC$Peer.channel_id == tLRPC$Peer2.channel_id) {
                return true;
            }
        }
        return false;
    }

    public boolean isUnread() {
        return this.messageOwner.unread;
    }

    public boolean isContentUnread() {
        return this.messageOwner.media_unread;
    }

    public void setIsRead() {
        this.messageOwner.unread = false;
    }

    public int getUnradFlags() {
        return getUnreadFlags(this.messageOwner);
    }

    public static int getUnreadFlags(TLRPC$Message tLRPC$Message) {
        int i = !tLRPC$Message.unread ? 1 : 0;
        return !tLRPC$Message.media_unread ? i | 2 : i;
    }

    public void setContentIsRead() {
        this.messageOwner.media_unread = false;
    }

    public int getId() {
        return this.messageOwner.id;
    }

    public int getRealId() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        int i = tLRPC$Message.realId;
        return i != 0 ? i : tLRPC$Message.id;
    }

    public static long getMessageSize(TLRPC$Message tLRPC$Message) {
        TLRPC$Document tLRPC$Document;
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            tLRPC$Document = getMedia(tLRPC$Message).webpage.document;
        } else if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGame) {
            tLRPC$Document = getMedia(tLRPC$Message).game.document;
        } else {
            tLRPC$Document = getMedia(tLRPC$Message) != null ? getMedia(tLRPC$Message).document : null;
        }
        if (tLRPC$Document != null) {
            return tLRPC$Document.size;
        }
        return 0L;
    }

    public long getSize() {
        return getMessageSize(this.messageOwner);
    }

    public static void fixMessagePeer(ArrayList<TLRPC$Message> arrayList, long j) {
        if (arrayList == null || arrayList.isEmpty() || j == 0) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC$Message tLRPC$Message = arrayList.get(i);
            if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
                TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                tLRPC$Message.peer_id = tLRPC$TL_peerChannel;
                tLRPC$TL_peerChannel.channel_id = j;
            }
        }
    }

    public long getChannelId() {
        return getChannelId(this.messageOwner);
    }

    public static long getChannelId(TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
        if (tLRPC$Peer != null) {
            return tLRPC$Peer.channel_id;
        }
        return 0L;
    }

    public static boolean shouldEncryptPhotoOrVideo(TLRPC$Message tLRPC$Message) {
        int i;
        return tLRPC$Message instanceof TLRPC$TL_message_secret ? ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isVideoMessage(tLRPC$Message)) && (i = tLRPC$Message.ttl) > 0 && i <= 60 : ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument)) && getMedia(tLRPC$Message).ttl_seconds != 0;
    }

    public boolean shouldEncryptPhotoOrVideo() {
        return shouldEncryptPhotoOrVideo(this.messageOwner);
    }

    public static boolean isSecretPhotoOrVideo(TLRPC$Message tLRPC$Message) {
        int i;
        if (tLRPC$Message instanceof TLRPC$TL_message_secret) {
            return ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isRoundVideoMessage(tLRPC$Message) || isVideoMessage(tLRPC$Message)) && (i = tLRPC$Message.ttl) > 0 && i <= 60;
        } else if (!(tLRPC$Message instanceof TLRPC$TL_message)) {
            return false;
        } else {
            return ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument)) && getMedia(tLRPC$Message).ttl_seconds != 0;
        }
    }

    public static boolean isSecretMedia(TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message instanceof TLRPC$TL_message_secret) {
            return ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isRoundVideoMessage(tLRPC$Message) || isVideoMessage(tLRPC$Message)) && getMedia(tLRPC$Message).ttl_seconds != 0;
        } else if (!(tLRPC$Message instanceof TLRPC$TL_message)) {
            return false;
        } else {
            return ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument)) && getMedia(tLRPC$Message).ttl_seconds != 0;
        }
    }

    public boolean needDrawBluredPreview() {
        if (hasExtendedMediaPreview()) {
            return true;
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (!(tLRPC$Message instanceof TLRPC$TL_message_secret)) {
            return (tLRPC$Message instanceof TLRPC$TL_message) && getMedia(tLRPC$Message) != null && getMedia(this.messageOwner).ttl_seconds != 0 && ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument));
        }
        int max = Math.max(tLRPC$Message.ttl, getMedia(tLRPC$Message).ttl_seconds);
        return max > 0 && ((((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || isVideo() || isGif()) && max <= 60) || isRoundVideo());
    }

    public boolean isSecretMedia() {
        int i;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message instanceof TLRPC$TL_message_secret ? (((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isGif()) && (i = this.messageOwner.ttl) > 0 && i <= 60) || isVoice() || isRoundVideo() || isVideo() : (tLRPC$Message instanceof TLRPC$TL_message) && getMedia(tLRPC$Message) != null && getMedia(this.messageOwner).ttl_seconds != 0 && ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument));
    }

    public static void setUnreadFlags(TLRPC$Message tLRPC$Message, int i) {
        boolean z = false;
        tLRPC$Message.unread = (i & 1) == 0;
        if ((i & 2) == 0) {
            z = true;
        }
        tLRPC$Message.media_unread = z;
    }

    public static boolean isUnread(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.unread;
    }

    public static boolean isContentUnread(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.media_unread;
    }

    public boolean isSavedFromMegagroup() {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        if (tLRPC$MessageFwdHeader == null || (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) == null || tLRPC$Peer.channel_id == 0) {
            return false;
        }
        return ChatObject.isMegagroup(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.saved_from_peer.channel_id)));
    }

    public static boolean isOut(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.out;
    }

    public long getDialogId() {
        return getDialogId(this.messageOwner);
    }

    public boolean canStreamVideo() {
        TLRPC$Document document = getDocument();
        if (document != null && !(document instanceof TLRPC$TL_documentEncrypted)) {
            if (SharedConfig.streamAllVideo) {
                return true;
            }
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    return tLRPC$DocumentAttribute.supports_streaming;
                }
            }
            if (SharedConfig.streamMkv && "video/x-matroska".equals(document.mime_type)) {
                return true;
            }
        }
        return false;
    }

    public static long getDialogId(TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer;
        if (tLRPC$Message.dialog_id == 0 && (tLRPC$Peer = tLRPC$Message.peer_id) != null) {
            long j = tLRPC$Peer.chat_id;
            if (j != 0) {
                tLRPC$Message.dialog_id = -j;
            } else {
                long j2 = tLRPC$Peer.channel_id;
                if (j2 != 0) {
                    tLRPC$Message.dialog_id = -j2;
                } else if (tLRPC$Message.from_id == null || isOut(tLRPC$Message)) {
                    tLRPC$Message.dialog_id = tLRPC$Message.peer_id.user_id;
                } else {
                    tLRPC$Message.dialog_id = tLRPC$Message.from_id.user_id;
                }
            }
        }
        return tLRPC$Message.dialog_id;
    }

    public boolean isSending() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.send_state == 1 && tLRPC$Message.id < 0;
    }

    public boolean isEditing() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.send_state == 3 && tLRPC$Message.id > 0;
    }

    public boolean isEditingMedia() {
        return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto ? getMedia(this.messageOwner).photo.id == 0 : (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) && getMedia(this.messageOwner).document.dc_id == 0;
    }

    public boolean isSendError() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return (tLRPC$Message.send_state == 2 && tLRPC$Message.id < 0) || (this.scheduled && tLRPC$Message.id > 0 && tLRPC$Message.date < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + (-60));
    }

    public boolean isSent() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.send_state == 0 || tLRPC$Message.id > 0;
    }

    public int getSecretTimeLeft() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        int i = tLRPC$Message.ttl;
        int i2 = tLRPC$Message.destroyTime;
        return i2 != 0 ? Math.max(0, i2 - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) : i;
    }

    public String getSecretTimeString() {
        if (!isSecretMedia()) {
            return null;
        }
        int secretTimeLeft = getSecretTimeLeft();
        if (secretTimeLeft < 60) {
            return secretTimeLeft + "s";
        }
        return (secretTimeLeft / 60) + "m";
    }

    public String getDocumentName() {
        return FileLoader.getDocumentFileName(getDocument());
    }

    public static boolean isWebM(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && "video/webm".equals(tLRPC$Document.mime_type);
    }

    public static boolean isVideoSticker(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && isVideoStickerDocument(tLRPC$Document);
    }

    public boolean isVideoSticker() {
        return getDocument() != null && isVideoStickerDocument(getDocument());
    }

    public static boolean isStickerDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                if (tLRPC$Document.attributes.get(i) instanceof TLRPC$TL_documentAttributeSticker) {
                    return "image/webp".equals(tLRPC$Document.mime_type) || "video/webm".equals(tLRPC$Document.mime_type);
                }
            }
        }
        return false;
    }

    public static boolean isVideoStickerDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeCustomEmoji)) {
                    return "video/webm".equals(tLRPC$Document.mime_type);
                }
            }
        }
        return false;
    }

    public static boolean isStickerHasSet(TLRPC$Document tLRPC$Document) {
        TLRPC$InputStickerSet tLRPC$InputStickerSet;
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) && (tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset) != null && !(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAnimatedStickerDocument(TLRPC$Document tLRPC$Document, boolean z) {
        if (tLRPC$Document != null && (("application/x-tgsticker".equals(tLRPC$Document.mime_type) && !tLRPC$Document.thumbs.isEmpty()) || "application/x-tgsdice".equals(tLRPC$Document.mime_type))) {
            if (z) {
                return true;
            }
            int size = tLRPC$Document.attributes.size();
            for (int i = 0; i < size; i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                    return tLRPC$DocumentAttribute.stickerset instanceof TLRPC$TL_inputStickerSetShortName;
                }
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeCustomEmoji) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canAutoplayAnimatedSticker(TLRPC$Document tLRPC$Document) {
        return (isAnimatedStickerDocument(tLRPC$Document, true) || isVideoStickerDocument(tLRPC$Document)) && SharedConfig.getDevicePerformanceClass() != 0;
    }

    public static boolean isMaskDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) && tLRPC$DocumentAttribute.mask) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isVoiceDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                    return tLRPC$DocumentAttribute.voice;
                }
            }
        }
        return false;
    }

    public static boolean isVoiceWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.equals("audio/ogg");
    }

    public static boolean isImageWebDocument(WebFile webFile) {
        return webFile != null && !isGifDocument(webFile) && webFile.mime_type.startsWith("image/");
    }

    public static boolean isVideoWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.startsWith("video/");
    }

    public static boolean isMusicDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                    return !tLRPC$DocumentAttribute.voice;
                }
            }
            if (!TextUtils.isEmpty(tLRPC$Document.mime_type)) {
                String lowerCase = tLRPC$Document.mime_type.toLowerCase();
                if (lowerCase.equals("audio/flac") || lowerCase.equals("audio/ogg") || lowerCase.equals("audio/opus") || lowerCase.equals("audio/x-opus+ogg") || (lowerCase.equals("application/octet-stream") && FileLoader.getDocumentFileName(tLRPC$Document).endsWith(".opus"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static TLRPC$VideoSize getDocumentVideoThumb(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null || tLRPC$Document.video_thumbs.isEmpty()) {
            return null;
        }
        return tLRPC$Document.video_thumbs.get(0);
    }

    public static boolean isVideoDocument(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return false;
        }
        boolean z = false;
        int i = 0;
        int i2 = 0;
        boolean z2 = false;
        for (int i3 = 0; i3 < tLRPC$Document.attributes.size(); i3++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i3);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                if (tLRPC$DocumentAttribute.round_message) {
                    return false;
                }
                i = tLRPC$DocumentAttribute.w;
                i2 = tLRPC$DocumentAttribute.h;
                z2 = true;
            } else if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAnimated) {
                z = true;
            }
        }
        if (z && (i > 1280 || i2 > 1280)) {
            z = false;
        }
        if (SharedConfig.streamMkv && !z2 && "video/x-matroska".equals(tLRPC$Document.mime_type)) {
            z2 = true;
        }
        return z2 && !z;
    }

    public TLRPC$Document getDocument() {
        TLRPC$Document tLRPC$Document = this.emojiAnimatedSticker;
        return tLRPC$Document != null ? tLRPC$Document : getDocument(this.messageOwner);
    }

    public static TLRPC$Document getDocument(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return getMedia(tLRPC$Message).webpage.document;
        }
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGame) {
            return getMedia(tLRPC$Message).game.document;
        }
        if (getMedia(tLRPC$Message) == null) {
            return null;
        }
        return getMedia(tLRPC$Message).document;
    }

    public static TLRPC$Photo getPhoto(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return getMedia(tLRPC$Message).webpage.photo;
        }
        if (getMedia(tLRPC$Message) == null) {
            return null;
        }
        return getMedia(tLRPC$Message).photo;
    }

    public static boolean isStickerMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) != null && isStickerDocument(getMedia(tLRPC$Message).document);
    }

    public static boolean isAnimatedStickerMessage(TLRPC$Message tLRPC$Message) {
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(tLRPC$Message.dialog_id);
        if ((!isEncryptedDialog || tLRPC$Message.stickerVerified == 1) && getMedia(tLRPC$Message) != null) {
            return isAnimatedStickerDocument(getMedia(tLRPC$Message).document, !isEncryptedDialog || tLRPC$Message.out);
        }
        return false;
    }

    public static boolean isLocationMessage(TLRPC$Message tLRPC$Message) {
        return (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGeo) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGeoLive) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaVenue);
    }

    public static boolean isMaskMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) != null && isMaskDocument(getMedia(tLRPC$Message).document);
    }

    public static boolean isMusicMessage(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return isMusicDocument(getMedia(tLRPC$Message).webpage.document);
        }
        return getMedia(tLRPC$Message) != null && isMusicDocument(getMedia(tLRPC$Message).document);
    }

    public static boolean isGifMessage(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return isGifDocument(getMedia(tLRPC$Message).webpage.document);
        }
        if (getMedia(tLRPC$Message) != null) {
            if (isGifDocument(getMedia(tLRPC$Message).document, tLRPC$Message.grouped_id != 0)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRoundVideoMessage(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return isRoundVideoDocument(getMedia(tLRPC$Message).webpage.document);
        }
        return getMedia(tLRPC$Message) != null && isRoundVideoDocument(getMedia(tLRPC$Message).document);
    }

    public static boolean isPhoto(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return (getMedia(tLRPC$Message).webpage.photo instanceof TLRPC$TL_photo) && !(getMedia(tLRPC$Message).webpage.document instanceof TLRPC$TL_document);
        }
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto;
    }

    public static boolean isVoiceMessage(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return isVoiceDocument(getMedia(tLRPC$Message).webpage.document);
        }
        return getMedia(tLRPC$Message) != null && isVoiceDocument(getMedia(tLRPC$Message).document);
    }

    public static boolean isNewGifMessage(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return isNewGifDocument(getMedia(tLRPC$Message).webpage.document);
        }
        return getMedia(tLRPC$Message) != null && isNewGifDocument(getMedia(tLRPC$Message).document);
    }

    public static boolean isLiveLocationMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGeoLive;
    }

    public static boolean isVideoMessage(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) == null || !isVideoSticker(getMedia(tLRPC$Message).document)) {
            if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
                return isVideoDocument(getMedia(tLRPC$Message).webpage.document);
            }
            return getMedia(tLRPC$Message) != null && isVideoDocument(getMedia(tLRPC$Message).document);
        }
        return false;
    }

    public static boolean isGameMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGame;
    }

    public static boolean isInvoiceMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaInvoice;
    }

    public static TLRPC$InputStickerSet getInputStickerSet(TLRPC$Message tLRPC$Message) {
        TLRPC$Document document = getDocument(tLRPC$Message);
        if (document != null) {
            return getInputStickerSet(document);
        }
        return null;
    }

    public static TLRPC$InputStickerSet getInputStickerSet(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return null;
        }
        int size = tLRPC$Document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeCustomEmoji)) {
                TLRPC$InputStickerSet tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset;
                if (!(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                    return tLRPC$InputStickerSet;
                }
                return null;
            }
        }
        return null;
    }

    public static String findAnimatedEmojiEmoticon(TLRPC$Document tLRPC$Document) {
        return findAnimatedEmojiEmoticon(tLRPC$Document, "😀");
    }

    public static String findAnimatedEmojiEmoticon(TLRPC$Document tLRPC$Document, String str) {
        if (tLRPC$Document == null) {
            return str;
        }
        int size = tLRPC$Document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeCustomEmoji) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker)) {
                return tLRPC$DocumentAttribute.alt;
            }
        }
        return str;
    }

    public static boolean isAnimatedEmoji(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return false;
        }
        int size = tLRPC$Document.attributes.size();
        for (int i = 0; i < size; i++) {
            if (tLRPC$Document.attributes.get(i) instanceof TLRPC$TL_documentAttributeCustomEmoji) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFreeEmoji(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return false;
        }
        int size = tLRPC$Document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeCustomEmoji) {
                return ((TLRPC$TL_documentAttributeCustomEmoji) tLRPC$DocumentAttribute).free;
            }
        }
        return false;
    }

    public static boolean isPremiumEmojiPack(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        TLRPC$StickerSet tLRPC$StickerSet;
        if ((tLRPC$TL_messages_stickerSet == null || (tLRPC$StickerSet = tLRPC$TL_messages_stickerSet.set) == null || tLRPC$StickerSet.emojis) && tLRPC$TL_messages_stickerSet != null && tLRPC$TL_messages_stickerSet.documents != null) {
            for (int i = 0; i < tLRPC$TL_messages_stickerSet.documents.size(); i++) {
                if (!isFreeEmoji(tLRPC$TL_messages_stickerSet.documents.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPremiumEmojiPack(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
        TLRPC$StickerSet tLRPC$StickerSet;
        if (tLRPC$StickerSetCovered == null || (tLRPC$StickerSet = tLRPC$StickerSetCovered.set) == null || tLRPC$StickerSet.emojis) {
            ArrayList<TLRPC$Document> arrayList = tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetFullCovered ? ((TLRPC$TL_stickerSetFullCovered) tLRPC$StickerSetCovered).documents : tLRPC$StickerSetCovered.covers;
            if (tLRPC$StickerSetCovered != null && arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!isFreeEmoji(arrayList.get(i))) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public static long getStickerSetId(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return -1L;
        }
        for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                TLRPC$InputStickerSet tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset;
                if (!(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                    return tLRPC$InputStickerSet.id;
                }
                return -1L;
            }
        }
        return -1L;
    }

    public static String getStickerSetName(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return null;
        }
        for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                TLRPC$InputStickerSet tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset;
                if (!(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                    return tLRPC$InputStickerSet.short_name;
                }
                return null;
            }
        }
        return null;
    }

    public String getStickerChar() {
        TLRPC$Document document = getDocument();
        if (document != null) {
            Iterator<TLRPC$DocumentAttribute> it = document.attributes.iterator();
            while (it.hasNext()) {
                TLRPC$DocumentAttribute next = it.next();
                if (next instanceof TLRPC$TL_documentAttributeSticker) {
                    return next.alt;
                }
            }
            return null;
        }
        return null;
    }

    public int getApproximateHeight() {
        int i;
        int i2;
        int min;
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        int min2;
        int i3 = this.type;
        int i4 = 0;
        if (i3 == 0) {
            int i5 = this.textHeight;
            if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage)) {
                i4 = AndroidUtilities.dp(100.0f);
            }
            int i6 = i5 + i4;
            return isReply() ? i6 + AndroidUtilities.dp(42.0f) : i6;
        } else if (i3 == 20) {
            return AndroidUtilities.getPhotoSize();
        } else {
            if (i3 == 2) {
                return AndroidUtilities.dp(72.0f);
            }
            if (i3 == 12) {
                return AndroidUtilities.dp(71.0f);
            }
            if (i3 == 9) {
                return AndroidUtilities.dp(100.0f);
            }
            if (i3 == 4) {
                return AndroidUtilities.dp(114.0f);
            }
            if (i3 == 14) {
                return AndroidUtilities.dp(82.0f);
            }
            if (i3 == 10) {
                return AndroidUtilities.dp(30.0f);
            }
            if (i3 == 11 || i3 == 18) {
                return AndroidUtilities.dp(50.0f);
            }
            if (i3 == 5) {
                return AndroidUtilities.roundMessageSize;
            }
            if (i3 == 19) {
                return this.textHeight + AndroidUtilities.dp(30.0f);
            }
            if (i3 == 13 || i3 == 15) {
                float f = AndroidUtilities.displaySize.y * 0.4f;
                if (AndroidUtilities.isTablet()) {
                    i = AndroidUtilities.getMinTabletSide();
                } else {
                    i = AndroidUtilities.displaySize.x;
                }
                float f2 = i * 0.5f;
                TLRPC$Document document = getDocument();
                if (document != null) {
                    int size = document.attributes.size();
                    for (int i7 = 0; i7 < size; i7++) {
                        TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i7);
                        if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) {
                            i4 = tLRPC$DocumentAttribute.w;
                            i2 = tLRPC$DocumentAttribute.h;
                            break;
                        }
                    }
                }
                i2 = 0;
                if (i4 == 0) {
                    i2 = (int) f;
                    i4 = AndroidUtilities.dp(100.0f) + i2;
                }
                float f3 = i2;
                if (f3 > f) {
                    i4 = (int) (i4 * (f / f3));
                    i2 = (int) f;
                }
                float f4 = i4;
                if (f4 > f2) {
                    i2 = (int) (i2 * (f2 / f4));
                }
                return i2 + AndroidUtilities.dp(14.0f);
            }
            if (AndroidUtilities.isTablet()) {
                min = AndroidUtilities.getMinTabletSide();
            } else {
                Point point = AndroidUtilities.displaySize;
                min = Math.min(point.x, point.y);
            }
            int i8 = (int) (min * 0.7f);
            int dp = AndroidUtilities.dp(100.0f) + i8;
            if (i8 > AndroidUtilities.getPhotoSize()) {
                i8 = AndroidUtilities.getPhotoSize();
            }
            if (dp > AndroidUtilities.getPhotoSize()) {
                dp = AndroidUtilities.getPhotoSize();
            }
            if (FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
                int i9 = (int) (closestPhotoSizeWithSize.h / (closestPhotoSizeWithSize.w / i8));
                if (i9 == 0) {
                    i9 = AndroidUtilities.dp(100.0f);
                }
                if (i9 <= dp) {
                    dp = i9 < AndroidUtilities.dp(120.0f) ? AndroidUtilities.dp(120.0f) : i9;
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
    }

    private int getParentWidth() {
        int i;
        return (!this.preview || (i = this.parentWidth) <= 0) ? AndroidUtilities.displaySize.x : i;
    }

    public String getStickerEmoji() {
        TLRPC$Document document = getDocument();
        if (document == null) {
            return null;
        }
        for (int i = 0; i < document.attributes.size(); i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
            if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeCustomEmoji)) {
                String str = tLRPC$DocumentAttribute.alt;
                if (str != null && str.length() > 0) {
                    return tLRPC$DocumentAttribute.alt;
                }
                return null;
            }
        }
        return null;
    }

    public boolean isVideoCall() {
        TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
        return (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) && tLRPC$MessageAction.video;
    }

    public boolean isAnimatedEmoji() {
        return (this.emojiAnimatedSticker == null && this.emojiAnimatedStickerId == null) ? false : true;
    }

    public boolean isAnimatedAnimatedEmoji() {
        return isAnimatedEmoji() && isAnimatedEmoji(getDocument());
    }

    public boolean isDice() {
        return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDice;
    }

    public String getDiceEmoji() {
        if (!isDice()) {
            return null;
        }
        TLRPC$TL_messageMediaDice tLRPC$TL_messageMediaDice = (TLRPC$TL_messageMediaDice) getMedia(this.messageOwner);
        return TextUtils.isEmpty(tLRPC$TL_messageMediaDice.emoticon) ? "🎲" : tLRPC$TL_messageMediaDice.emoticon.replace("️", "");
    }

    public int getDiceValue() {
        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDice) {
            return ((TLRPC$TL_messageMediaDice) getMedia(this.messageOwner)).value;
        }
        return -1;
    }

    public boolean isSticker() {
        int i = this.type;
        return i != 1000 ? i == 13 : isStickerDocument(getDocument()) || isVideoSticker(getDocument());
    }

    public boolean isAnimatedSticker() {
        int i = this.type;
        boolean z = false;
        if (i != 1000) {
            return i == 15;
        }
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(getDialogId());
        if (isEncryptedDialog && this.messageOwner.stickerVerified != 1) {
            return false;
        }
        if (this.emojiAnimatedStickerId != null && this.emojiAnimatedSticker == null) {
            return true;
        }
        TLRPC$Document document = getDocument();
        if (this.emojiAnimatedSticker != null || !isEncryptedDialog || isOut()) {
            z = true;
        }
        return isAnimatedStickerDocument(document, z);
    }

    public boolean isAnyKindOfSticker() {
        int i = this.type;
        return i == 13 || i == 15 || i == 19;
    }

    public boolean shouldDrawWithoutBackground() {
        int i = this.type;
        return i == 13 || i == 15 || i == 5 || i == 19;
    }

    public boolean isAnimatedEmojiStickers() {
        return this.type == 19;
    }

    public boolean isAnimatedEmojiStickerSingle() {
        return this.emojiAnimatedStickerId != null;
    }

    public boolean isLocation() {
        return isLocationMessage(this.messageOwner);
    }

    public boolean isMask() {
        return isMaskMessage(this.messageOwner);
    }

    public boolean isMusic() {
        return isMusicMessage(this.messageOwner) && !isVideo();
    }

    public boolean isDocument() {
        return getDocument() != null && !isVideo() && !isMusic() && !isVoice() && !isAnyKindOfSticker();
    }

    public boolean isVoice() {
        return isVoiceMessage(this.messageOwner);
    }

    public boolean isVideo() {
        return isVideoMessage(this.messageOwner);
    }

    public boolean isPhoto() {
        return isPhoto(this.messageOwner);
    }

    public boolean isLiveLocation() {
        return isLiveLocationMessage(this.messageOwner);
    }

    public boolean isExpiredLiveLocation(int i) {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.date + getMedia(tLRPC$Message).period <= i;
    }

    public boolean isGame() {
        return isGameMessage(this.messageOwner);
    }

    public boolean isInvoice() {
        return isInvoiceMessage(this.messageOwner);
    }

    public boolean isRoundVideo() {
        if (this.isRoundVideoCached == 0) {
            this.isRoundVideoCached = (this.type == 5 || isRoundVideoMessage(this.messageOwner)) ? 1 : 2;
        }
        return this.isRoundVideoCached == 1;
    }

    public boolean shouldAnimateSending() {
        return isSending() && (this.type == 5 || isVoice() || ((isAnyKindOfSticker() && this.sendAnimationData != null) || !(this.messageText == null || this.sendAnimationData == null)));
    }

    public boolean hasAttachedStickers() {
        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
            return getMedia(this.messageOwner).photo != null && getMedia(this.messageOwner).photo.has_stickers;
        } else if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument)) {
            return false;
        } else {
            return isDocumentHasAttachedStickers(getMedia(this.messageOwner).document);
        }
    }

    public static boolean isDocumentHasAttachedStickers(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document != null) {
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                if (tLRPC$Document.attributes.get(i) instanceof TLRPC$TL_documentAttributeHasStickers) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGif() {
        return isGifMessage(this.messageOwner);
    }

    public boolean isWebpageDocument() {
        return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage.document != null && !isGifDocument(getMedia(this.messageOwner).webpage.document);
    }

    public boolean isWebpage() {
        return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage;
    }

    public boolean isNewGif() {
        return getMedia(this.messageOwner) != null && isNewGifDocument(getDocument());
    }

    public boolean isAndroidTheme() {
        if (getMedia(this.messageOwner) != null && getMedia(this.messageOwner).webpage != null && !getMedia(this.messageOwner).webpage.attributes.isEmpty()) {
            int size = getMedia(this.messageOwner).webpage.attributes.size();
            for (int i = 0; i < size; i++) {
                TLRPC$TL_webPageAttributeTheme tLRPC$TL_webPageAttributeTheme = getMedia(this.messageOwner).webpage.attributes.get(i);
                ArrayList<TLRPC$Document> arrayList = tLRPC$TL_webPageAttributeTheme.documents;
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    if ("application/x-tgtheme-android".equals(arrayList.get(i2).mime_type)) {
                        return true;
                    }
                }
                if (tLRPC$TL_webPageAttributeTheme.settings != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getMusicTitle() {
        return getMusicTitle(true);
    }

    public String getMusicTitle(boolean z) {
        TLRPC$Document document = getDocument();
        if (document != null) {
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                    if (tLRPC$DocumentAttribute.voice) {
                        if (z) {
                            return LocaleController.formatDateAudio(this.messageOwner.date, true);
                        }
                        return null;
                    }
                    String str = tLRPC$DocumentAttribute.title;
                    if (str != null && str.length() != 0) {
                        return str;
                    }
                    String documentFileName = FileLoader.getDocumentFileName(document);
                    return (!TextUtils.isEmpty(documentFileName) || !z) ? documentFileName : LocaleController.getString("AudioUnknownTitle", R.string.AudioUnknownTitle);
                } else if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) && tLRPC$DocumentAttribute.round_message) {
                    return LocaleController.formatDateAudio(this.messageOwner.date, true);
                }
            }
            String documentFileName2 = FileLoader.getDocumentFileName(document);
            if (!TextUtils.isEmpty(documentFileName2)) {
                return documentFileName2;
            }
        }
        return LocaleController.getString("AudioUnknownTitle", R.string.AudioUnknownTitle);
    }

    public int getDuration() {
        TLRPC$Document document = getDocument();
        if (document == null) {
            return 0;
        }
        int i = this.audioPlayerDuration;
        if (i > 0) {
            return i;
        }
        for (int i2 = 0; i2 < document.attributes.size(); i2++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i2);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                return tLRPC$DocumentAttribute.duration;
            }
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                return tLRPC$DocumentAttribute.duration;
            }
        }
        return this.audioPlayerDuration;
    }

    public String getArtworkUrl(boolean z) {
        TLRPC$Document document = getDocument();
        if (document == null || "audio/ogg".equals(document.mime_type)) {
            return null;
        }
        int size = document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                if (tLRPC$DocumentAttribute.voice) {
                    return null;
                }
                String str = tLRPC$DocumentAttribute.performer;
                String str2 = tLRPC$DocumentAttribute.title;
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

    public String getMusicAuthor() {
        return getMusicAuthor(true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:81:0x003c, code lost:
        if (r4.round_message != false) goto L10;
     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0156 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String getMusicAuthor(boolean z) {
        TLRPC$User user;
        TLRPC$Chat chat;
        String str;
        TLRPC$Document document = getDocument();
        if (document != null) {
            boolean z2 = false;
            for (int i = 0; i < document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                    if (!tLRPC$DocumentAttribute.voice) {
                        String str2 = tLRPC$DocumentAttribute.performer;
                        return (!TextUtils.isEmpty(str2) || !z) ? str2 : LocaleController.getString("AudioUnknownArtist", R.string.AudioUnknownArtist);
                    }
                } else {
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    }
                    if (!z2) {
                        TLRPC$User tLRPC$User = null;
                        if (!z) {
                            return null;
                        }
                        if (!isOutOwner()) {
                            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                            if (tLRPC$MessageFwdHeader != null) {
                                TLRPC$Peer tLRPC$Peer = tLRPC$MessageFwdHeader.from_id;
                                if ((tLRPC$Peer instanceof TLRPC$TL_peerUser) && tLRPC$Peer.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                }
                            }
                            TLRPC$Message tLRPC$Message = this.messageOwner;
                            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2 = tLRPC$Message.fwd_from;
                            if (tLRPC$MessageFwdHeader2 != null && (tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerChannel)) {
                                chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.channel_id));
                            } else if (tLRPC$MessageFwdHeader2 != null && (tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerChat)) {
                                chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.chat_id));
                            } else {
                                if (tLRPC$MessageFwdHeader2 != null && (tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerUser)) {
                                    user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.fwd_from.from_id.user_id));
                                } else if (tLRPC$MessageFwdHeader2 != null && (str = tLRPC$MessageFwdHeader2.from_name) != null) {
                                    return str;
                                } else {
                                    TLRPC$Peer tLRPC$Peer2 = tLRPC$Message.from_id;
                                    if (tLRPC$Peer2 instanceof TLRPC$TL_peerChat) {
                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.chat_id));
                                    } else if (tLRPC$Peer2 instanceof TLRPC$TL_peerChannel) {
                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.channel_id));
                                    } else if (tLRPC$Peer2 == null && tLRPC$Message.peer_id.channel_id != 0) {
                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.channel_id));
                                    } else {
                                        user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
                                    }
                                }
                                TLRPC$User tLRPC$User2 = user;
                                chat = null;
                                tLRPC$User = tLRPC$User2;
                            }
                            if (tLRPC$User != null) {
                                return UserObject.getUserName(tLRPC$User);
                            }
                            if (chat != null) {
                                return chat.title;
                            }
                        }
                        return LocaleController.getString("FromYou", R.string.FromYou);
                    }
                }
                z2 = true;
                if (!z2) {
                }
            }
        }
        return LocaleController.getString("AudioUnknownArtist", R.string.AudioUnknownArtist);
    }

    public TLRPC$InputStickerSet getInputStickerSet() {
        return getInputStickerSet(this.messageOwner);
    }

    public boolean isForwarded() {
        return isForwardedMessage(this.messageOwner);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0020, code lost:
        if (r1.channel_id == r0.channel_id) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean needDrawForwarded() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if ((tLRPC$Message.flags & 4) != 0 && (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) != null && !tLRPC$MessageFwdHeader.imported) {
            TLRPC$Peer tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer;
            if (tLRPC$Peer != null) {
                TLRPC$Peer tLRPC$Peer2 = tLRPC$MessageFwdHeader.from_id;
                if (tLRPC$Peer2 instanceof TLRPC$TL_peerChannel) {
                }
            }
            if (UserConfig.getInstance(this.currentAccount).getClientUserId() != getDialogId()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForwardedMessage(TLRPC$Message tLRPC$Message) {
        return ((tLRPC$Message.flags & 4) == 0 || tLRPC$Message.fwd_from == null) ? false : true;
    }

    public boolean isReply() {
        TLRPC$Message tLRPC$Message;
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader;
        MessageObject messageObject = this.replyMessageObject;
        return ((messageObject != null && (messageObject.messageOwner instanceof TLRPC$TL_messageEmpty)) || (tLRPC$TL_messageReplyHeader = (tLRPC$Message = this.messageOwner).reply_to) == null || (tLRPC$TL_messageReplyHeader.reply_to_msg_id == 0 && tLRPC$TL_messageReplyHeader.reply_to_random_id == 0) || (tLRPC$Message.flags & 8) == 0) ? false : true;
    }

    public boolean isMediaEmpty() {
        return isMediaEmpty(this.messageOwner);
    }

    public boolean isMediaEmptyWebpage() {
        return isMediaEmptyWebpage(this.messageOwner);
    }

    public static boolean isMediaEmpty(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message == null || getMedia(tLRPC$Message) == null || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaEmpty) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage);
    }

    public static boolean isMediaEmptyWebpage(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message == null || getMedia(tLRPC$Message) == null || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaEmpty);
    }

    public boolean hasReplies() {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        return tLRPC$MessageReplies != null && tLRPC$MessageReplies.replies > 0;
    }

    public boolean canViewThread() {
        MessageObject messageObject;
        if (this.messageOwner.action != null) {
            return false;
        }
        return hasReplies() || !(((messageObject = this.replyMessageObject) == null || messageObject.messageOwner.replies == null) && getReplyTopMsgId() == 0);
    }

    public boolean isComments() {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        return tLRPC$MessageReplies != null && tLRPC$MessageReplies.comments;
    }

    public boolean isLinkedToChat(long j) {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        return tLRPC$MessageReplies != null && (j == 0 || tLRPC$MessageReplies.channel_id == j);
    }

    public int getRepliesCount() {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        if (tLRPC$MessageReplies != null) {
            return tLRPC$MessageReplies.replies;
        }
        return 0;
    }

    public boolean canEditMessage(TLRPC$Chat tLRPC$Chat) {
        return canEditMessage(this.currentAccount, this.messageOwner, tLRPC$Chat, this.scheduled);
    }

    public boolean canEditMessageScheduleTime(TLRPC$Chat tLRPC$Chat) {
        return canEditMessageScheduleTime(this.currentAccount, this.messageOwner, tLRPC$Chat);
    }

    public boolean canForwardMessage() {
        return !(this.messageOwner instanceof TLRPC$TL_message_secret) && !needDrawBluredPreview() && !isLiveLocation() && this.type != 16 && !isSponsored() && !this.messageOwner.noforwards;
    }

    public boolean canEditMedia() {
        if (isSecretMedia()) {
            return false;
        }
        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
            return true;
        }
        return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) && !isVoice() && !isSticker() && !isAnimatedSticker() && !isRoundVideo();
    }

    public boolean canEditMessageAnytime(TLRPC$Chat tLRPC$Chat) {
        return canEditMessageAnytime(this.currentAccount, this.messageOwner, tLRPC$Chat);
    }

    public static boolean canEditMessageAnytime(int i, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat) {
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2;
        if (tLRPC$Message != null && tLRPC$Message.peer_id != null && ((getMedia(tLRPC$Message) == null || (!isRoundVideoDocument(getMedia(tLRPC$Message).document) && !isStickerDocument(getMedia(tLRPC$Message).document) && !isAnimatedStickerDocument(getMedia(tLRPC$Message).document, true))) && (((tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty)) && !isForwardedMessage(tLRPC$Message) && tLRPC$Message.via_bot_id == 0 && tLRPC$Message.id >= 0))) {
            TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
            if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                long j = tLRPC$Peer.user_id;
                if (j == tLRPC$Message.peer_id.user_id && j == UserConfig.getInstance(i).getClientUserId() && !isLiveLocationMessage(tLRPC$Message)) {
                    return true;
                }
            }
            if (tLRPC$Chat == null && tLRPC$Message.peer_id.channel_id != 0 && (tLRPC$Chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(tLRPC$Message.peer_id.channel_id))) == null) {
                return false;
            }
            if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup && (tLRPC$Chat.creator || ((tLRPC$TL_chatAdminRights2 = tLRPC$Chat.admin_rights) != null && tLRPC$TL_chatAdminRights2.edit_messages))) {
                return true;
            }
            if (tLRPC$Message.out && tLRPC$Chat != null && tLRPC$Chat.megagroup && (tLRPC$Chat.creator || (((tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights) != null && tLRPC$TL_chatAdminRights.pin_messages) || ((tLRPC$TL_chatBannedRights = tLRPC$Chat.default_banned_rights) != null && !tLRPC$TL_chatBannedRights.pin_messages)))) {
                return true;
            }
        }
        return false;
    }

    public static boolean canEditMessageScheduleTime(int i, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat) {
        if (tLRPC$Chat == null && tLRPC$Message.peer_id.channel_id != 0 && (tLRPC$Chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Message.peer_id.channel_id))) == null) {
            return false;
        }
        if (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup || tLRPC$Chat.creator) {
            return true;
        }
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights;
        return tLRPC$TL_chatAdminRights != null && (tLRPC$TL_chatAdminRights.edit_messages || tLRPC$Message.out);
    }

    public static boolean canEditMessage(int i, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat, boolean z) {
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights3;
        if (!z || tLRPC$Message.date >= ConnectionsManager.getInstance(i).getCurrentTime() - 60) {
            if ((tLRPC$Chat == null || ((!tLRPC$Chat.left && !tLRPC$Chat.kicked) || (tLRPC$Chat.megagroup && tLRPC$Chat.has_link))) && tLRPC$Message != null && tLRPC$Message.peer_id != null && ((getMedia(tLRPC$Message) == null || (!isRoundVideoDocument(getMedia(tLRPC$Message).document) && !isStickerDocument(getMedia(tLRPC$Message).document) && !isAnimatedStickerDocument(getMedia(tLRPC$Message).document, true) && !isLocationMessage(tLRPC$Message))) && (((tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty)) && !isForwardedMessage(tLRPC$Message) && tLRPC$Message.via_bot_id == 0 && tLRPC$Message.id >= 0))) {
                TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
                if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                    long j = tLRPC$Peer.user_id;
                    if (j == tLRPC$Message.peer_id.user_id && j == UserConfig.getInstance(i).getClientUserId() && !isLiveLocationMessage(tLRPC$Message) && !(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaContact)) {
                        return true;
                    }
                }
                if (tLRPC$Chat == null && tLRPC$Message.peer_id.channel_id != 0 && (tLRPC$Chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Message.peer_id.channel_id))) == null) {
                    return false;
                }
                if (getMedia(tLRPC$Message) != null && !(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaEmpty) && !(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) && !(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument) && !(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage)) {
                    return false;
                }
                if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup && (tLRPC$Chat.creator || ((tLRPC$TL_chatAdminRights3 = tLRPC$Chat.admin_rights) != null && tLRPC$TL_chatAdminRights3.edit_messages))) {
                    return true;
                }
                if (tLRPC$Message.out && tLRPC$Chat != null && tLRPC$Chat.megagroup && (tLRPC$Chat.creator || (((tLRPC$TL_chatAdminRights2 = tLRPC$Chat.admin_rights) != null && tLRPC$TL_chatAdminRights2.pin_messages) || ((tLRPC$TL_chatBannedRights = tLRPC$Chat.default_banned_rights) != null && !tLRPC$TL_chatBannedRights.pin_messages)))) {
                    return true;
                }
                if (!z && Math.abs(tLRPC$Message.date - ConnectionsManager.getInstance(i).getCurrentTime()) > MessagesController.getInstance(i).maxEditTime) {
                    return false;
                }
                if (tLRPC$Message.peer_id.channel_id == 0) {
                    if (!tLRPC$Message.out) {
                        TLRPC$Peer tLRPC$Peer2 = tLRPC$Message.from_id;
                        if (!(tLRPC$Peer2 instanceof TLRPC$TL_peerUser) || tLRPC$Peer2.user_id != UserConfig.getInstance(i).getClientUserId()) {
                            return false;
                        }
                    }
                    return (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument) && !isStickerMessage(tLRPC$Message) && !isAnimatedStickerMessage(tLRPC$Message)) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaEmpty) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(tLRPC$Message) == null;
                } else if (((tLRPC$Chat != null && tLRPC$Chat.megagroup && tLRPC$Message.out) || (tLRPC$Chat != null && !tLRPC$Chat.megagroup && ((tLRPC$Chat.creator || ((tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights) != null && (tLRPC$TL_chatAdminRights.edit_messages || (tLRPC$Message.out && tLRPC$TL_chatAdminRights.post_messages)))) && tLRPC$Message.post))) && ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || (((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument) && !isStickerMessage(tLRPC$Message) && !isAnimatedStickerMessage(tLRPC$Message)) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaEmpty) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(tLRPC$Message) == null))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean canDeleteMessage(boolean z, TLRPC$Chat tLRPC$Chat) {
        return this.eventId == 0 && this.sponsoredId == null && canDeleteMessage(this.currentAccount, z, this.messageOwner, tLRPC$Chat);
    }

    public static boolean canDeleteMessage(int i, boolean z, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        if (tLRPC$Message == null) {
            return false;
        }
        if (ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat) && (tLRPC$Message.action instanceof TLRPC$TL_messageActionChatJoinedByRequest)) {
            return false;
        }
        if (tLRPC$Message.id < 0) {
            return true;
        }
        if (tLRPC$Chat == null && tLRPC$Message.peer_id.channel_id != 0) {
            tLRPC$Chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Message.peer_id.channel_id));
        }
        if (!ChatObject.isChannel(tLRPC$Chat)) {
            return z || isOut(tLRPC$Message) || !ChatObject.isChannel(tLRPC$Chat);
        } else if (z && !tLRPC$Chat.megagroup) {
            if (!tLRPC$Chat.creator) {
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2 = tLRPC$Chat.admin_rights;
                if (tLRPC$TL_chatAdminRights2 == null) {
                    return false;
                }
                if (!tLRPC$TL_chatAdminRights2.delete_messages && !tLRPC$Message.out) {
                    return false;
                }
            }
            return true;
        } else {
            boolean z2 = tLRPC$Message.out;
            if (z2 && (tLRPC$Message instanceof TLRPC$TL_messageService)) {
                return tLRPC$Message.id != 1 && ChatObject.canUserDoAdminAction(tLRPC$Chat, 13);
            }
            if (!z) {
                if (tLRPC$Message.id == 1) {
                    return false;
                }
                if (!tLRPC$Chat.creator && (((tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights) == null || (!tLRPC$TL_chatAdminRights.delete_messages && (!z2 || (!tLRPC$Chat.megagroup && !tLRPC$TL_chatAdminRights.post_messages)))) && (!tLRPC$Chat.megagroup || !z2))) {
                    return false;
                }
            }
            return true;
        }
    }

    public String getForwardedName() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        if (tLRPC$MessageFwdHeader != null) {
            TLRPC$Peer tLRPC$Peer = tLRPC$MessageFwdHeader.from_id;
            if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.channel_id));
                if (chat == null) {
                    return null;
                }
                return chat.title;
            } else if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.chat_id));
                if (chat2 == null) {
                    return null;
                }
                return chat2.title;
            } else if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.fwd_from.from_id.user_id));
                if (user == null) {
                    return null;
                }
                return UserObject.getUserName(user);
            } else {
                String str = tLRPC$MessageFwdHeader.from_name;
                if (str == null) {
                    return null;
                }
                return str;
            }
        }
        return null;
    }

    public int getReplyMsgId() {
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$TL_messageReplyHeader != null) {
            return tLRPC$TL_messageReplyHeader.reply_to_msg_id;
        }
        return 0;
    }

    public int getReplyTopMsgId() {
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$TL_messageReplyHeader != null) {
            return tLRPC$TL_messageReplyHeader.reply_to_top_id;
        }
        return 0;
    }

    public static long getReplyToDialogId(TLRPC$Message tLRPC$Message) {
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = tLRPC$Message.reply_to;
        if (tLRPC$TL_messageReplyHeader == null) {
            return 0L;
        }
        TLRPC$Peer tLRPC$Peer = tLRPC$TL_messageReplyHeader.reply_to_peer_id;
        if (tLRPC$Peer != null) {
            return getPeerId(tLRPC$Peer);
        }
        return getDialogId(tLRPC$Message);
    }

    public int getReplyAnyMsgId() {
        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$TL_messageReplyHeader != null) {
            int i = tLRPC$TL_messageReplyHeader.reply_to_top_id;
            return i != 0 ? i : tLRPC$TL_messageReplyHeader.reply_to_msg_id;
        }
        return 0;
    }

    public boolean isPrivateForward() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        return tLRPC$MessageFwdHeader != null && !TextUtils.isEmpty(tLRPC$MessageFwdHeader.from_name);
    }

    public boolean isImportedForward() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        return tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.imported;
    }

    public long getSenderId() {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
        if (tLRPC$MessageFwdHeader != null && (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) != null) {
            long j = tLRPC$Peer.user_id;
            if (j != 0) {
                TLRPC$Peer tLRPC$Peer2 = tLRPC$MessageFwdHeader.from_id;
                return tLRPC$Peer2 instanceof TLRPC$TL_peerUser ? tLRPC$Peer2.user_id : j;
            } else if (tLRPC$Peer.channel_id != 0) {
                if (isSavedFromMegagroup()) {
                    TLRPC$Peer tLRPC$Peer3 = this.messageOwner.fwd_from.from_id;
                    if (tLRPC$Peer3 instanceof TLRPC$TL_peerUser) {
                        return tLRPC$Peer3.user_id;
                    }
                }
                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2 = this.messageOwner.fwd_from;
                TLRPC$Peer tLRPC$Peer4 = tLRPC$MessageFwdHeader2.from_id;
                if (tLRPC$Peer4 instanceof TLRPC$TL_peerChannel) {
                    return -tLRPC$Peer4.channel_id;
                }
                if (tLRPC$Peer4 instanceof TLRPC$TL_peerChat) {
                    return -tLRPC$Peer4.chat_id;
                }
                return -tLRPC$MessageFwdHeader2.saved_from_peer.channel_id;
            } else {
                long j2 = tLRPC$Peer.chat_id;
                if (j2 != 0) {
                    TLRPC$Peer tLRPC$Peer5 = tLRPC$MessageFwdHeader.from_id;
                    if (tLRPC$Peer5 instanceof TLRPC$TL_peerUser) {
                        return tLRPC$Peer5.user_id;
                    }
                    if (tLRPC$Peer5 instanceof TLRPC$TL_peerChannel) {
                        return -tLRPC$Peer5.channel_id;
                    }
                    return tLRPC$Peer5 instanceof TLRPC$TL_peerChat ? -tLRPC$Peer5.chat_id : -j2;
                }
            }
        } else {
            TLRPC$Peer tLRPC$Peer6 = tLRPC$Message.from_id;
            if (tLRPC$Peer6 instanceof TLRPC$TL_peerUser) {
                return tLRPC$Peer6.user_id;
            }
            if (tLRPC$Peer6 instanceof TLRPC$TL_peerChannel) {
                return -tLRPC$Peer6.channel_id;
            }
            if (tLRPC$Peer6 instanceof TLRPC$TL_peerChat) {
                return -tLRPC$Peer6.chat_id;
            }
            if (tLRPC$Message.post) {
                return tLRPC$Message.peer_id.channel_id;
            }
        }
        return 0L;
    }

    public boolean isWallpaper() {
        return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_background".equals(getMedia(this.messageOwner).webpage.type);
    }

    public boolean isTheme() {
        return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_theme".equals(getMedia(this.messageOwner).webpage.type);
    }

    public int getMediaExistanceFlags() {
        boolean z = this.attachPathExists;
        return this.mediaExists ? (z ? 1 : 0) | 2 : z ? 1 : 0;
    }

    public void applyMediaExistanceFlags(int i) {
        if (i == -1) {
            checkMediaExistance();
            return;
        }
        boolean z = false;
        this.attachPathExists = (i & 1) != 0;
        if ((i & 2) != 0) {
            z = true;
        }
        this.mediaExists = z;
    }

    public void checkMediaExistance() {
        checkMediaExistance(true);
    }

    public void checkMediaExistance(boolean z) {
        int i;
        TLRPC$Photo tLRPC$Photo;
        this.attachPathExists = false;
        this.mediaExists = false;
        int i2 = this.type;
        if (i2 == 20) {
            TLRPC$TL_messageExtendedMediaPreview tLRPC$TL_messageExtendedMediaPreview = (TLRPC$TL_messageExtendedMediaPreview) this.messageOwner.media.extended_media;
            if (tLRPC$TL_messageExtendedMediaPreview.thumb != null) {
                File pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$TL_messageExtendedMediaPreview.thumb);
                if (!this.mediaExists) {
                    this.mediaExists = pathToAttach.exists() || (tLRPC$TL_messageExtendedMediaPreview.thumb instanceof TLRPC$TL_photoStrippedSize);
                }
            }
        } else if (i2 == 1 && FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
            File pathToMessage = FileLoader.getInstance(this.currentAccount).getPathToMessage(this.messageOwner, z);
            if (needDrawBluredPreview()) {
                this.mediaExists = new File(pathToMessage.getAbsolutePath() + ".enc").exists();
            }
            if (!this.mediaExists) {
                this.mediaExists = pathToMessage.exists();
            }
        }
        if ((!this.mediaExists && this.type == 8) || (i = this.type) == 3 || i == 9 || i == 2 || i == 14 || i == 5) {
            String str = this.messageOwner.attachPath;
            if (str != null && str.length() > 0) {
                this.attachPathExists = new File(this.messageOwner.attachPath).exists();
            }
            if (!this.attachPathExists) {
                File pathToMessage2 = FileLoader.getInstance(this.currentAccount).getPathToMessage(this.messageOwner, z);
                if (this.type == 3 && needDrawBluredPreview()) {
                    this.mediaExists = new File(pathToMessage2.getAbsolutePath() + ".enc").exists();
                }
                if (!this.mediaExists) {
                    this.mediaExists = pathToMessage2.exists();
                }
            }
        }
        if (!this.mediaExists) {
            TLRPC$Document document = getDocument();
            if (document != null) {
                if (isWallpaper()) {
                    this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, true, z).exists();
                    return;
                } else {
                    this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, false, z).exists();
                    return;
                }
            }
            int i3 = this.type;
            if (i3 == 0) {
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
                if (closestPhotoSizeWithSize == null) {
                    return;
                }
                this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, null, true, z).exists();
            } else if (i3 != 11 || (tLRPC$Photo = this.messageOwner.action.photo) == null || tLRPC$Photo.video_sizes.isEmpty()) {
            } else {
                this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$Photo.video_sizes.get(0), null, true, z).exists();
            }
        }
    }

    public void setQuery(String str) {
        String str2;
        int indexOf;
        if (TextUtils.isEmpty(str)) {
            this.highlightedWords = null;
            this.messageTrimmedToHighlight = null;
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        String lowerCase = str.trim().toLowerCase();
        String[] split = lowerCase.split("\\P{L}+");
        ArrayList arrayList2 = new ArrayList();
        if (!TextUtils.isEmpty(this.messageOwner.message)) {
            String lowerCase2 = this.messageOwner.message.trim().toLowerCase();
            if (lowerCase2.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
                handleFoundWords(arrayList, split);
                return;
            }
            arrayList2.addAll(Arrays.asList(lowerCase2.split("\\P{L}+")));
        }
        if (getDocument() != null) {
            String lowerCase3 = FileLoader.getDocumentFileName(getDocument()).toLowerCase();
            if (lowerCase3.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
            }
            arrayList2.addAll(Arrays.asList(lowerCase3.split("\\P{L}+")));
        }
        if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage)) {
            TLRPC$WebPage tLRPC$WebPage = getMedia(this.messageOwner).webpage;
            String str3 = tLRPC$WebPage.title;
            if (str3 == null) {
                str3 = tLRPC$WebPage.site_name;
            }
            if (str3 != null) {
                String lowerCase4 = str3.toLowerCase();
                if (lowerCase4.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                    arrayList.add(lowerCase);
                }
                arrayList2.addAll(Arrays.asList(lowerCase4.split("\\P{L}+")));
            }
        }
        String musicAuthor = getMusicAuthor();
        if (musicAuthor != null) {
            String lowerCase5 = musicAuthor.toLowerCase();
            if (lowerCase5.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
            }
            arrayList2.addAll(Arrays.asList(lowerCase5.split("\\P{L}+")));
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
        handleFoundWords(arrayList, split);
    }

    private void handleFoundWords(ArrayList<String> arrayList, String[] strArr) {
        boolean z;
        if (!arrayList.isEmpty()) {
            boolean z2 = false;
            for (int i = 0; i < arrayList.size(); i++) {
                int i2 = 0;
                while (true) {
                    if (i2 >= strArr.length) {
                        break;
                    } else if (arrayList.get(i).contains(strArr[i2])) {
                        z2 = true;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (z2) {
                    break;
                }
            }
            if (z2) {
                int i3 = 0;
                while (i3 < arrayList.size()) {
                    int i4 = 0;
                    while (true) {
                        if (i4 >= strArr.length) {
                            z = false;
                            break;
                        } else if (arrayList.get(i3).contains(strArr[i4])) {
                            z = true;
                            break;
                        } else {
                            i4++;
                        }
                    }
                    if (!z) {
                        arrayList.remove(i3);
                        i3--;
                    }
                    i3++;
                }
                if (arrayList.size() > 0) {
                    Collections.sort(arrayList, MessageObject$$ExternalSyntheticLambda1.INSTANCE);
                    arrayList.clear();
                    arrayList.add(arrayList.get(0));
                }
            }
            this.highlightedWords = arrayList;
            String str = this.messageOwner.message;
            if (str == null) {
                return;
            }
            String trim = str.replace('\n', ' ').replaceAll(" +", " ").trim();
            int length = trim.length();
            int indexOf = trim.toLowerCase().indexOf(arrayList.get(0));
            if (indexOf < 0) {
                indexOf = 0;
            }
            if (length > 200) {
                int max = Math.max(0, indexOf - 100);
                trim = trim.substring(max, Math.min(length, (indexOf - max) + indexOf + 100));
            }
            this.messageTrimmedToHighlight = trim;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$handleFoundWords$3(String str, String str2) {
        return str2.length() - str.length();
    }

    public void createMediaThumbs() {
        if (isVideo()) {
            TLRPC$Document document = getDocument();
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
            this.mediaThumb = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320), document);
            this.mediaSmallThumb = ImageLocation.getForDocument(closestPhotoSizeWithSize, document);
        } else if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || getMedia(this.messageOwner).photo == null || this.photoThumbs.isEmpty()) {
        } else {
            TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 50);
            this.mediaThumb = ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 320, false, closestPhotoSizeWithSize2, false), this.photoThumbsObject);
            this.mediaSmallThumb = ImageLocation.getForObject(closestPhotoSizeWithSize2, this.photoThumbsObject);
        }
    }

    public boolean hasHighlightedWords() {
        ArrayList<String> arrayList = this.highlightedWords;
        return arrayList != null && !arrayList.isEmpty();
    }

    public boolean equals(MessageObject messageObject) {
        return getId() == messageObject.getId() && getDialogId() == messageObject.getDialogId();
    }

    public boolean isReactionsAvailable() {
        return !isEditing() && !isSponsored() && isSent() && this.messageOwner.action == null;
    }

    public boolean selectReaction(ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.reactions == null) {
            tLRPC$Message.reactions = new TLRPC$TL_messageReactions();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        ArrayList arrayList = new ArrayList();
        TLRPC$ReactionCount tLRPC$ReactionCount = null;
        int i = 0;
        for (int i2 = 0; i2 < this.messageOwner.reactions.results.size(); i2++) {
            if (this.messageOwner.reactions.results.get(i2).chosen) {
                TLRPC$ReactionCount tLRPC$ReactionCount2 = this.messageOwner.reactions.results.get(i2);
                arrayList.add(tLRPC$ReactionCount2);
                int i3 = tLRPC$ReactionCount2.chosen_order;
                if (i3 > i) {
                    i = i3;
                }
            }
            TLRPC$Reaction tLRPC$Reaction = this.messageOwner.reactions.results.get(i2).reaction;
            if (tLRPC$Reaction instanceof TLRPC$TL_reactionEmoji) {
                String str = visibleReaction.emojicon;
                if (str != null) {
                    if (((TLRPC$TL_reactionEmoji) tLRPC$Reaction).emoticon.equals(str)) {
                        tLRPC$ReactionCount = this.messageOwner.reactions.results.get(i2);
                    }
                }
            }
            if (tLRPC$Reaction instanceof TLRPC$TL_reactionCustomEmoji) {
                long j = visibleReaction.documentId;
                if (j != 0 && ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction).document_id == j) {
                    tLRPC$ReactionCount = this.messageOwner.reactions.results.get(i2);
                }
            }
        }
        if (arrayList.isEmpty() || !arrayList.contains(tLRPC$ReactionCount) || !z) {
            int maxUserReactionsCount = MessagesController.getInstance(this.currentAccount).getMaxUserReactionsCount();
            if (!arrayList.isEmpty() && (arrayList.contains(tLRPC$ReactionCount) || z2)) {
                if (tLRPC$ReactionCount != null) {
                    tLRPC$ReactionCount.chosen = false;
                    int i4 = tLRPC$ReactionCount.count - 1;
                    tLRPC$ReactionCount.count = i4;
                    if (i4 <= 0) {
                        this.messageOwner.reactions.results.remove(tLRPC$ReactionCount);
                    }
                }
                if (this.messageOwner.reactions.can_see_list) {
                    int i5 = 0;
                    while (i5 < this.messageOwner.reactions.recent_reactions.size()) {
                        if (getPeerId(this.messageOwner.reactions.recent_reactions.get(i5).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId() && ReactionsUtils.compare(this.messageOwner.reactions.recent_reactions.get(i5).reaction, visibleReaction)) {
                            this.messageOwner.reactions.recent_reactions.remove(i5);
                            i5--;
                        }
                        i5++;
                    }
                }
                this.reactionsChanged = true;
                return false;
            }
            while (!arrayList.isEmpty() && arrayList.size() >= maxUserReactionsCount) {
                int i6 = 0;
                for (int i7 = 1; i7 < arrayList.size(); i7++) {
                    if (((TLRPC$ReactionCount) arrayList.get(i7)).chosen_order < ((TLRPC$ReactionCount) arrayList.get(i6)).chosen_order) {
                        i6 = i7;
                    }
                }
                TLRPC$ReactionCount tLRPC$ReactionCount3 = (TLRPC$ReactionCount) arrayList.get(i6);
                tLRPC$ReactionCount3.chosen = false;
                int i8 = tLRPC$ReactionCount3.count - 1;
                tLRPC$ReactionCount3.count = i8;
                if (i8 <= 0) {
                    this.messageOwner.reactions.results.remove(tLRPC$ReactionCount3);
                }
                arrayList.remove(tLRPC$ReactionCount3);
                if (this.messageOwner.reactions.can_see_list) {
                    int i9 = 0;
                    while (i9 < this.messageOwner.reactions.recent_reactions.size()) {
                        if (getPeerId(this.messageOwner.reactions.recent_reactions.get(i9).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId() && ReactionsUtils.compare(this.messageOwner.reactions.recent_reactions.get(i9).reaction, visibleReaction)) {
                            this.messageOwner.reactions.recent_reactions.remove(i9);
                            i9--;
                        }
                        i9++;
                    }
                }
            }
            if (tLRPC$ReactionCount == null) {
                tLRPC$ReactionCount = new TLRPC$TL_reactionCount();
                if (visibleReaction.emojicon != null) {
                    TLRPC$TL_reactionEmoji tLRPC$TL_reactionEmoji = new TLRPC$TL_reactionEmoji();
                    tLRPC$ReactionCount.reaction = tLRPC$TL_reactionEmoji;
                    tLRPC$TL_reactionEmoji.emoticon = visibleReaction.emojicon;
                    this.messageOwner.reactions.results.add(tLRPC$ReactionCount);
                } else {
                    TLRPC$TL_reactionCustomEmoji tLRPC$TL_reactionCustomEmoji = new TLRPC$TL_reactionCustomEmoji();
                    tLRPC$ReactionCount.reaction = tLRPC$TL_reactionCustomEmoji;
                    tLRPC$TL_reactionCustomEmoji.document_id = visibleReaction.documentId;
                    this.messageOwner.reactions.results.add(tLRPC$ReactionCount);
                }
            }
            tLRPC$ReactionCount.chosen = true;
            tLRPC$ReactionCount.count++;
            tLRPC$ReactionCount.chosen_order = i + 1;
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            if (tLRPC$Message2.reactions.can_see_list || (tLRPC$Message2.dialog_id > 0 && maxUserReactionsCount > 1)) {
                TLRPC$TL_messagePeerReaction tLRPC$TL_messagePeerReaction = new TLRPC$TL_messagePeerReaction();
                this.messageOwner.reactions.recent_reactions.add(0, tLRPC$TL_messagePeerReaction);
                TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                tLRPC$TL_messagePeerReaction.peer_id = tLRPC$TL_peerUser;
                tLRPC$TL_peerUser.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                if (visibleReaction.emojicon != null) {
                    TLRPC$TL_reactionEmoji tLRPC$TL_reactionEmoji2 = new TLRPC$TL_reactionEmoji();
                    tLRPC$TL_messagePeerReaction.reaction = tLRPC$TL_reactionEmoji2;
                    tLRPC$TL_reactionEmoji2.emoticon = visibleReaction.emojicon;
                } else {
                    TLRPC$TL_reactionCustomEmoji tLRPC$TL_reactionCustomEmoji2 = new TLRPC$TL_reactionCustomEmoji();
                    tLRPC$TL_messagePeerReaction.reaction = tLRPC$TL_reactionCustomEmoji2;
                    tLRPC$TL_reactionCustomEmoji2.document_id = visibleReaction.documentId;
                }
            }
            this.reactionsChanged = true;
            return true;
        }
        return true;
    }

    public boolean probablyRingtone() {
        if (getDocument() != null && RingtoneDataStore.ringtoneSupportedMimeType.contains(getDocument().mime_type) && getDocument().size < MessagesController.getInstance(this.currentAccount).ringtoneSizeMax * 2) {
            for (int i = 0; i < getDocument().attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = getDocument().attributes.get(i);
                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) && tLRPC$DocumentAttribute.duration < 60) {
                    return true;
                }
            }
        }
        return false;
    }

    public byte[] getWaveform() {
        if (getDocument() == null) {
            return null;
        }
        int i = 0;
        for (int i2 = 0; i2 < getDocument().attributes.size(); i2++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = getDocument().attributes.get(i2);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                byte[] bArr = tLRPC$DocumentAttribute.waveform;
                if (bArr == null || bArr.length == 0) {
                    MediaController.getInstance().generateWaveform(this);
                }
                return tLRPC$DocumentAttribute.waveform;
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
}
