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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.CodeHighlighting;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.ringtone.RingtoneDataStore;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotApp;
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
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$ExportedChatInvite;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$ForumTopic;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$InputQuickReplyShortcut;
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
import org.telegram.tgnet.TLRPC$MessageReplyHeader;
import org.telegram.tgnet.TLRPC$Page;
import org.telegram.tgnet.TLRPC$PageBlock;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$PollResults;
import org.telegram.tgnet.TLRPC$Reaction;
import org.telegram.tgnet.TLRPC$ReactionCount;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$SecureValueType;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEvent;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeAbout;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeColor;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeEmojiStatus;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeEmojiStickerSet;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeLinkedChat;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeLocation;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangePeerColor;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangePhoto;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeStickerSet;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeTheme;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeTitle;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeUsername;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeUsernames;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeWallpaper;
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
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleAntiSpam;
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
import org.telegram.tgnet.TLRPC$TL_emojiStatusEmpty;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_game;
import org.telegram.tgnet.TLRPC$TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC$TL_inputPeerChat;
import org.telegram.tgnet.TLRPC$TL_inputPeerSelf;
import org.telegram.tgnet.TLRPC$TL_inputPeerUser;
import org.telegram.tgnet.TLRPC$TL_inputQuickReplyShortcut;
import org.telegram.tgnet.TLRPC$TL_inputQuickReplyShortcutId;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageActionAttachMenuBotAllowed;
import org.telegram.tgnet.TLRPC$TL_messageActionBoostApply;
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
import org.telegram.tgnet.TLRPC$TL_messageActionGiftCode;
import org.telegram.tgnet.TLRPC$TL_messageActionGiftPremium;
import org.telegram.tgnet.TLRPC$TL_messageActionGiveawayLaunch;
import org.telegram.tgnet.TLRPC$TL_messageActionGiveawayResults;
import org.telegram.tgnet.TLRPC$TL_messageActionGroupCall;
import org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled;
import org.telegram.tgnet.TLRPC$TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall;
import org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation;
import org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC$TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC$TL_messageActionRequestedPeer;
import org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken;
import org.telegram.tgnet.TLRPC$TL_messageActionSecureValuesSent;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatWallPaper;
import org.telegram.tgnet.TLRPC$TL_messageActionSetMessagesTTL;
import org.telegram.tgnet.TLRPC$TL_messageActionSetSameChatWallPaper;
import org.telegram.tgnet.TLRPC$TL_messageActionSuggestProfilePhoto;
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
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto_layer68;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto_layer74;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto_old;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageMediaStory;
import org.telegram.tgnet.TLRPC$TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_messagePeerReaction;
import org.telegram.tgnet.TLRPC$TL_messageReactions;
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
import org.telegram.tgnet.TLRPC$TL_peerChannel_layer131;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerChat_layer131;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_peerUser_layer131;
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
import org.telegram.tgnet.TLRPC$TL_sponsoredWebPage;
import org.telegram.tgnet.TLRPC$TL_stickerPack;
import org.telegram.tgnet.TLRPC$TL_stickerSetFullCovered;
import org.telegram.tgnet.TLRPC$TL_textWithEntities;
import org.telegram.tgnet.TLRPC$TL_wallPaperNoFile;
import org.telegram.tgnet.TLRPC$TL_webPage;
import org.telegram.tgnet.TLRPC$TL_webPageAttributeStory;
import org.telegram.tgnet.TLRPC$TL_webPageAttributeTheme;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.tgnet.TLRPC$WebDocument;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.TLRPC$WebPageAttribute;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemDeleted;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Business.QuickRepliesController;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AvatarDrawable;
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
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.PeerColorActivity;
/* loaded from: classes3.dex */
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
    public static final int TYPE_GIVEAWAY = 26;
    public static final int TYPE_GIVEAWAY_RESULTS = 28;
    public static final int TYPE_JOINED_CHANNEL = 27;
    public static final int TYPE_LOADING = 6;
    public static final int TYPE_MUSIC = 14;
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
    public boolean animateComments;
    public int animatedEmojiCount;
    public boolean attachPathExists;
    public double attributeDuration;
    public int audioPlayerDuration;
    public float audioProgress;
    public int audioProgressMs;
    public int audioProgressSec;
    public StringBuilder botButtonsLayout;
    public String botStartParam;
    public float bufferedProgress;
    public boolean business;
    public Boolean cachedIsSupergroup;
    public boolean cancelEditing;
    public CharSequence caption;
    private boolean captionTranslated;
    private boolean channelJoined;
    public boolean channelJoinedExpanded;
    public ArrayList<TLRPC$TL_pollAnswer> checkedVotes;
    public int contentType;
    public int currentAccount;
    public TLRPC$TL_channelAdminLogEvent currentEvent;
    public Drawable customAvatarDrawable;
    public String customName;
    public String customReplyName;
    public String dateKey;
    public int dateKeyInt;
    public boolean deleted;
    public boolean deletedByThanos;
    public boolean drawServiceWithDefaultTypeface;
    public CharSequence editingMessage;
    public ArrayList<TLRPC$MessageEntity> editingMessageEntities;
    public boolean editingMessageSearchWebPage;
    public TLRPC$Document emojiAnimatedSticker;
    public String emojiAnimatedStickerColor;
    public Long emojiAnimatedStickerId;
    private boolean emojiAnimatedStickerLoading;
    public TLRPC$VideoSize emojiMarkup;
    public int emojiOnlyCount;
    public long eventId;
    public long extendedMediaLastCheckTime;
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
    public boolean isSpoilersRevealed;
    public boolean isStoryMentionPush;
    public boolean isStoryPush;
    public boolean isStoryPushHidden;
    public boolean isTopicMainMessage;
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
    public TLRPC$Message messageOwner;
    public CharSequence messageText;
    public CharSequence messageTextForReply;
    public CharSequence messageTextShort;
    public CharSequence messageTrimmedToHighlight;
    public String monthKey;
    public int overrideLinkColor;
    public long overrideLinkEmoji;
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
    public boolean previewForward;
    public String previousAttachPath;
    public TLRPC$MessageMedia previousMedia;
    public String previousMessage;
    public ArrayList<TLRPC$MessageEntity> previousMessageEntities;
    public boolean putInDownloadsStore;
    public String quick_reply_shortcut;
    private byte[] randomWaveform;
    public boolean reactionsChanged;
    public long reactionsLastCheckTime;
    public MessageObject replyMessageObject;
    public boolean replyTextEllipsized;
    public boolean replyTextRevealed;
    public TLRPC$TL_forumTopic replyToForumTopic;
    public boolean resendAsIs;
    public boolean revealingMediaSpoilers;
    public boolean scheduled;
    public boolean scheduledSent;
    private CharSequence secretOnceSpan;
    private CharSequence secretPlaySpan;
    public SendAnimationData sendAnimationData;
    public TLRPC$Peer sendAsPeer;
    public boolean settingAvatar;
    public boolean shouldRemoveVideoEditedInfo;
    private boolean spoiledLoginCode;
    public String sponsoredAdditionalInfo;
    public TLRPC$BotApp sponsoredBotApp;
    public String sponsoredButtonText;
    public int sponsoredChannelPost;
    public TLRPC$ChatInvite sponsoredChatInvite;
    public String sponsoredChatInviteHash;
    public byte[] sponsoredId;
    public String sponsoredInfo;
    public boolean sponsoredRecommended;
    public boolean sponsoredShowPeerPhoto;
    public TLRPC$TL_sponsoredWebPage sponsoredWebPage;
    public int stableId;
    public TL_stories$StoryItem storyItem;
    private TLRPC$WebPage storyMentionWebpage;
    public BitmapDrawable strippedThumb;
    public int textHeight;
    public ArrayList<TextLayoutBlock> textLayoutBlocks;
    public int textWidth;
    public float textXOffset;
    public Drawable[] topicIconDrawable;
    private int totalAnimatedEmojiCount;
    public boolean translated;
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

    /* loaded from: classes3.dex */
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

    public boolean shouldDrawReactionsInLayout() {
        return true;
    }

    public int getChatMode() {
        if (this.scheduled) {
            return 1;
        }
        return isQuickReply() ? 5 : 0;
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

    private static long getTopicId(MessageObject messageObject) {
        if (messageObject == null) {
            return 0L;
        }
        return getTopicId(messageObject.currentAccount, messageObject.messageOwner, false);
    }

    private static long getTopicId(int i, TLRPC$Message tLRPC$Message) {
        return getTopicId(i, tLRPC$Message, false);
    }

    public static long getTopicId(int i, TLRPC$Message tLRPC$Message, boolean z) {
        int i2;
        long clientUserId = UserConfig.getInstance(i).getClientUserId();
        if ((tLRPC$Message.flags & 1073741824) != 0 && DialogObject.getPeerDialogId(tLRPC$Message.peer_id) == clientUserId) {
            i2 = tLRPC$Message.quick_reply_shortcut_id;
        } else if (!z && i >= 0 && DialogObject.getPeerDialogId(tLRPC$Message.peer_id) == clientUserId) {
            return getSavedDialogId(clientUserId, tLRPC$Message);
        } else {
            TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicCreate) {
                i2 = tLRPC$Message.id;
            } else {
                TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = tLRPC$Message.reply_to;
                if (tLRPC$MessageReplyHeader == null || !tLRPC$MessageReplyHeader.forum_topic) {
                    return z ? 1L : 0L;
                } else if ((tLRPC$Message instanceof TLRPC$TL_messageService) && !(tLRPC$MessageAction instanceof TLRPC$TL_messageActionPinMessage)) {
                    int i3 = tLRPC$MessageReplyHeader.reply_to_msg_id;
                    if (i3 == 0) {
                        i3 = tLRPC$MessageReplyHeader.reply_to_top_id;
                    }
                    return i3;
                } else {
                    int i4 = tLRPC$MessageReplyHeader.reply_to_top_id;
                    if (i4 == 0) {
                        i4 = tLRPC$MessageReplyHeader.reply_to_msg_id;
                    }
                    return i4;
                }
            }
        }
        return i2;
    }

    public static boolean isTopicActionMessage(MessageObject messageObject) {
        TLRPC$Message tLRPC$Message;
        if (messageObject == null || (tLRPC$Message = messageObject.messageOwner) == null) {
            return false;
        }
        TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
        return (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicCreate) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicEdit);
    }

    public static boolean canCreateStripedThubms() {
        return SharedConfig.getDevicePerformanceClass() == 2;
    }

    public static void normalizeFlags(TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
        if (tLRPC$Peer == null) {
            tLRPC$Message.flags &= -257;
        }
        if (tLRPC$Peer == null) {
            tLRPC$Message.flags &= -5;
        }
        if (tLRPC$Message.reply_to == null) {
            tLRPC$Message.flags &= -9;
        }
        if (tLRPC$Message.media == null) {
            tLRPC$Message.flags &= -513;
        }
        if (tLRPC$Message.reply_markup == null) {
            tLRPC$Message.flags &= -65;
        }
        if (tLRPC$Message.replies == null) {
            tLRPC$Message.flags &= -8388609;
        }
        if (tLRPC$Message.reactions == null) {
            tLRPC$Message.flags &= -1048577;
        }
    }

    public static double getDocumentDuration(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return 0.0d;
        }
        int size = tLRPC$Document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                return tLRPC$DocumentAttribute.duration;
            }
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                return tLRPC$DocumentAttribute.duration;
            }
        }
        return 0.0d;
    }

    public boolean isWallpaperAction() {
        TLRPC$Message tLRPC$Message;
        return this.type == 22 || ((tLRPC$Message = this.messageOwner) != null && (tLRPC$Message.action instanceof TLRPC$TL_messageActionSetSameChatWallPaper));
    }

    public boolean isWallpaperForBoth() {
        TLRPC$Message tLRPC$Message;
        if (isWallpaperAction() && (tLRPC$Message = this.messageOwner) != null) {
            TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
            if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) && ((TLRPC$TL_messageActionSetChatWallPaper) tLRPC$MessageAction).for_both) {
                return true;
            }
        }
        return false;
    }

    public boolean isCurrentWallpaper() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$UserFull userFull;
        TLRPC$WallPaper tLRPC$WallPaper;
        return (!isWallpaperAction() || (tLRPC$Message = this.messageOwner) == null || (tLRPC$MessageAction = tLRPC$Message.action) == null || tLRPC$MessageAction.wallpaper == null || (userFull = MessagesController.getInstance(this.currentAccount).getUserFull(getDialogId())) == null || (tLRPC$WallPaper = userFull.wallpaper) == null || !userFull.wallpaper_overridden || this.messageOwner.action.wallpaper.id != tLRPC$WallPaper.id) ? false : true;
    }

    public int getEmojiOnlyCount() {
        return this.emojiOnlyCount;
    }

    public boolean hasMediaSpoilers() {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        return !this.isRepostPreview && (((tLRPC$MessageMedia = this.messageOwner.media) != null && tLRPC$MessageMedia.spoiler) || needDrawBluredPreview());
    }

    public boolean shouldDrawReactions() {
        return !this.isRepostPreview;
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
        if (z) {
            MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            TLRPC$Message tLRPC$Message = this.messageOwner;
            messagesStorage.markMessageReactionsAsRead(tLRPC$Message.dialog_id, getTopicId(this.currentAccount, tLRPC$Message), this.messageOwner.id, true);
        }
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
        TLRPC$MessageMedia tLRPC$MessageMedia;
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
        TLRPC$Message tLRPC$Message2 = this.messageOwner;
        TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
        tLRPC$Message2.replyStory = tLRPC$Message3.replyStory;
        TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message2.media;
        if (tLRPC$MessageMedia2 != null && (tLRPC$MessageMedia = tLRPC$Message3.media) != null) {
            tLRPC$MessageMedia2.storyItem = tLRPC$MessageMedia.storyItem;
        }
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
        if (this.messageOwner.reactions == null) {
            return arrayList;
        }
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (this.messageOwner.reactions.results.get(i).chosen) {
                arrayList.add(ReactionsLayoutInBubble.VisibleReaction.fromTLReaction(this.messageOwner.reactions.results.get(i).reaction));
            }
        }
        return arrayList;
    }

    public boolean isReplyToStory() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader;
        MessageObject messageObject = this.replyMessageObject;
        return ((messageObject != null && (messageObject.messageOwner instanceof TLRPC$TL_messageEmpty)) || (tLRPC$MessageReplyHeader = (tLRPC$Message = this.messageOwner).reply_to) == null || tLRPC$MessageReplyHeader.story_id == 0 || (tLRPC$Message.flags & 8) == 0) ? false : true;
    }

    public boolean isUnsupported() {
        return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaUnsupported;
    }

    public boolean isExpiredStory() {
        int i = this.type;
        return (i == 23 || i == 24) && (this.messageOwner.media.storyItem instanceof TL_stories$TL_storyItemDeleted);
    }

    /* loaded from: classes3.dex */
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
                if (z) {
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
                }
                return null;
            } catch (Throwable unused) {
                return null;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class TextLayoutBlock {
        public static final int FLAG_NOT_RTL = 2;
        public static final int FLAG_RTL = 1;
        public int charactersEnd;
        public int charactersOffset;
        public boolean code;
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
        public String language;
        public int languageHeight;
        public Text languageLayout;
        public boolean last;
        public float maxRight;
        public int padBottom;
        public int padTop;
        public boolean quote;
        public StaticLayout textLayout;
        public float textYOffset;
        public AtomicReference<Layout> spoilersPatchedTextLayout = new AtomicReference<>();
        public List<SpoilerEffect> spoilers = new ArrayList();

        public void layoutCode(String str, int i, boolean z) {
            boolean z2 = i >= 75 && !z;
            this.hasCodeCopyButton = z2;
            if (z2) {
                this.copyText = new Text(LocaleController.getString(R.string.CopyCode).toUpperCase(), SharedConfig.fontSize - 3, AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                Drawable mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.msg_copy).mutate();
                this.copyIcon = mutate;
                mutate.setColorFilter(new PorterDuffColorFilter(this.copyIconColor, PorterDuff.Mode.SRC_IN));
                this.copySelector = Theme.createRadSelectorDrawable(this.copySelectorColor, 0, 0, Math.min(5, SharedConfig.bubbleRadius), 0);
                this.copySeparator = new Paint(1);
            }
            if (TextUtils.isEmpty(str)) {
                this.language = null;
                this.languageLayout = null;
                return;
            }
            this.language = str;
            Text text = new Text(capitalizeLanguage(str), (SharedConfig.fontSize - 1) - (CodeHighlighting.getTextSizeDecrement(i) / 2), AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.languageLayout = text;
            this.languageHeight = ((int) (text.getTextSize() * 1.714f)) + AndroidUtilities.dp(4.0f);
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
                this.copySelector.draw(canvas);
                this.copySeparator.setColor(ColorUtils.setAlphaComponent(i2, 38));
                canvas.drawRect(AndroidUtilities.dp(10.0f) + rectF.left, (rectF.bottom - AndroidUtilities.dp(38.0f)) - AndroidUtilities.getShadowHeight(), rectF.right - AndroidUtilities.dp(6.66f), rectF.bottom - AndroidUtilities.dp(38.0f), this.copySeparator);
                float min = Math.min(rectF.width() - AndroidUtilities.dp(12.0f), (this.copyIcon.getIntrinsicWidth() * 0.8f) + AndroidUtilities.dp(5.0f) + this.copyText.getCurrentWidth());
                float centerX = rectF.centerX() - (min / 2.0f);
                float dp = rectF.bottom - (AndroidUtilities.dp(38.0f) / 2.0f);
                if (this.copyIconColor != i) {
                    Drawable drawable2 = this.copyIcon;
                    this.copyIconColor = i;
                    drawable2.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
                }
                this.copyIcon.setAlpha(i3);
                Drawable drawable3 = this.copyIcon;
                drawable3.setBounds((int) centerX, (int) (dp - ((drawable3.getIntrinsicHeight() * 0.8f) / 2.0f)), (int) ((this.copyIcon.getIntrinsicWidth() * 0.8f) + centerX), (int) (((this.copyIcon.getIntrinsicHeight() * 0.8f) / 2.0f) + dp));
                this.copyIcon.draw(canvas);
                this.copyText.ellipsize(((int) (min - ((this.copyIcon.getIntrinsicWidth() * 0.8f) + AndroidUtilities.dp(5.0f)))) + AndroidUtilities.dp(12.0f)).draw(canvas, centerX + (this.copyIcon.getIntrinsicWidth() * 0.8f) + AndroidUtilities.dp(5.0f), dp, i, f);
            }
        }

        private static String capitalizeLanguage(String str) {
            if (str == null) {
                return null;
            }
            String replaceAll = str.toLowerCase().replaceAll("\\W", "");
            replaceAll.hashCode();
            char c = 65535;
            switch (replaceAll.hashCode()) {
                case -1886433663:
                    if (replaceAll.equals("actionscript")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1408289185:
                    if (replaceAll.equals("aspnet")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1351281305:
                    if (replaceAll.equals("csharp")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1326485984:
                    if (replaceAll.equals("docker")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1317317732:
                    if (replaceAll.equals("dockerfile")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1125574399:
                    if (replaceAll.equals("kotlin")) {
                        c = 5;
                        break;
                    }
                    break;
                case -995396628:
                    if (replaceAll.equals("pascal")) {
                        c = 6;
                        break;
                    }
                    break;
                case -973197092:
                    if (replaceAll.equals("python")) {
                        c = 7;
                        break;
                    }
                    break;
                case -746790872:
                    if (replaceAll.equals("arduino")) {
                        c = '\b';
                        break;
                    }
                    break;
                case -522285947:
                    if (replaceAll.equals("typescript")) {
                        c = '\t';
                        break;
                    }
                    break;
                case R.styleable.AppCompatTheme_spinnerDropDownItemStyle /* 99 */:
                    if (replaceAll.equals("c")) {
                        c = '\n';
                        break;
                    }
                    break;
                case R.styleable.AppCompatTheme_tooltipForegroundColor /* 114 */:
                    if (replaceAll.equals("r")) {
                        c = 11;
                        break;
                    }
                    break;
                case 3184:
                    if (replaceAll.equals("cs")) {
                        c = '\f';
                        break;
                    }
                    break;
                case 3401:
                    if (replaceAll.equals("js")) {
                        c = '\r';
                        break;
                    }
                    break;
                case 3479:
                    if (replaceAll.equals("md")) {
                        c = 14;
                        break;
                    }
                    break;
                case 3593:
                    if (replaceAll.equals("py")) {
                        c = 15;
                        break;
                    }
                    break;
                case 3632:
                    if (replaceAll.equals("rb")) {
                        c = 16;
                        break;
                    }
                    break;
                case 3704:
                    if (replaceAll.equals("tl")) {
                        c = 17;
                        break;
                    }
                    break;
                case 3711:
                    if (replaceAll.equals("ts")) {
                        c = 18;
                        break;
                    }
                    break;
                case 96891:
                    if (replaceAll.equals("asm")) {
                        c = 19;
                        break;
                    }
                    break;
                case 98723:
                    if (replaceAll.equals("cpp")) {
                        c = 20;
                        break;
                    }
                    break;
                case 98819:
                    if (replaceAll.equals("css")) {
                        c = 21;
                        break;
                    }
                    break;
                case 98822:
                    if (replaceAll.equals("csv")) {
                        c = 22;
                        break;
                    }
                    break;
                case 104420:
                    if (replaceAll.equals("ini")) {
                        c = 23;
                        break;
                    }
                    break;
                case 105551:
                    if (replaceAll.equals("jsx")) {
                        c = 24;
                        break;
                    }
                    break;
                case 107512:
                    if (replaceAll.equals("lua")) {
                        c = 25;
                        break;
                    }
                    break;
                case 110968:
                    if (replaceAll.equals("php")) {
                        c = 26;
                        break;
                    }
                    break;
                case 114922:
                    if (replaceAll.equals("tlb")) {
                        c = 27;
                        break;
                    }
                    break;
                case 115161:
                    if (replaceAll.equals("tsx")) {
                        c = 28;
                        break;
                    }
                    break;
                case 118807:
                    if (replaceAll.equals("xml")) {
                        c = 29;
                        break;
                    }
                    break;
                case 119768:
                    if (replaceAll.equals("yml")) {
                        c = 30;
                        break;
                    }
                    break;
                case 3075967:
                    if (replaceAll.equals("dart")) {
                        c = 31;
                        break;
                    }
                    break;
                case 3142865:
                    if (replaceAll.equals("fift")) {
                        c = ' ';
                        break;
                    }
                    break;
                case 3154628:
                    if (replaceAll.equals("func")) {
                        c = '!';
                        break;
                    }
                    break;
                case 3175934:
                    if (replaceAll.equals("glsl")) {
                        c = '\"';
                        break;
                    }
                    break;
                case 3205725:
                    if (replaceAll.equals("hlsl")) {
                        c = '#';
                        break;
                    }
                    break;
                case 3213227:
                    if (replaceAll.equals("html")) {
                        c = '$';
                        break;
                    }
                    break;
                case 3213448:
                    if (replaceAll.equals("http")) {
                        c = '%';
                        break;
                    }
                    break;
                case 3254818:
                    if (replaceAll.equals("java")) {
                        c = '&';
                        break;
                    }
                    break;
                case 3271912:
                    if (replaceAll.equals("json")) {
                        c = '\'';
                        break;
                    }
                    break;
                case 3318169:
                    if (replaceAll.equals("less")) {
                        c = '(';
                        break;
                    }
                    break;
                case 3373901:
                    if (replaceAll.equals("nasm")) {
                        c = ')';
                        break;
                    }
                    break;
                case 3404364:
                    if (replaceAll.equals("objc")) {
                        c = '*';
                        break;
                    }
                    break;
                case 3511770:
                    if (replaceAll.equals("ruby")) {
                        c = '+';
                        break;
                    }
                    break;
                case 3512292:
                    if (replaceAll.equals("rust")) {
                        c = ',';
                        break;
                    }
                    break;
                case 3524784:
                    if (replaceAll.equals("scss")) {
                        c = '-';
                        break;
                    }
                    break;
                case 3561037:
                    if (replaceAll.equals("tl-b")) {
                        c = '.';
                        break;
                    }
                    break;
                case 3642020:
                    if (replaceAll.equals("wasm")) {
                        c = '/';
                        break;
                    }
                    break;
                case 3701415:
                    if (replaceAll.equals("yaml")) {
                        c = '0';
                        break;
                    }
                    break;
                case 94833107:
                    if (replaceAll.equals("cobol")) {
                        c = '1';
                        break;
                    }
                    break;
                case 101429325:
                    if (replaceAll.equals("json5")) {
                        c = '2';
                        break;
                    }
                    break;
                case 109854227:
                    if (replaceAll.equals("swift")) {
                        c = '3';
                        break;
                    }
                    break;
                case 188995949:
                    if (replaceAll.equals("javascript")) {
                        c = '4';
                        break;
                    }
                    break;
                case 213985633:
                    if (replaceAll.equals("autohotkey")) {
                        c = '5';
                        break;
                    }
                    break;
                case 246938863:
                    if (replaceAll.equals("markdown")) {
                        c = '6';
                        break;
                    }
                    break;
                case 1067478602:
                    if (replaceAll.equals("objectivec")) {
                        c = '7';
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return "ActionScript";
                case 1:
                    return "ASP.NET";
                case 2:
                case '\f':
                    return "C#";
                case 3:
                case 4:
                case 5:
                case 6:
                case '\b':
                case '\n':
                case 25:
                case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                case ' ':
                case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                    return capitalizeFirst(str);
                case 7:
                case 15:
                    return "Python";
                case '\t':
                case 18:
                    return "TypeScript";
                case 11:
                case 17:
                case 19:
                case 21:
                case 22:
                case 23:
                case 24:
                case 26:
                case 28:
                case 29:
                case R.styleable.AppCompatTheme_actionModeTheme /* 30 */:
                case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                    return str.toUpperCase();
                case '\r':
                case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                    return "JavaScript";
                case 14:
                case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                    return "Markdown";
                case 16:
                case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                    return "Ruby";
                case 20:
                    return "C++";
                case 27:
                case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                    return "TL-B";
                case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                    return "FunC";
                case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                    return "Objective-C";
                case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                    return "AutoHotKey";
                default:
                    return str;
            }
        }

        private static String capitalizeFirst(String str) {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }

        public boolean isRtl() {
            byte b = this.directionFlags;
            return (b & 1) != 0 && (b & 2) == 0;
        }
    }

    /* loaded from: classes3.dex */
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

    /* loaded from: classes3.dex */
    public static class GroupedMessages {
        public MessageObject captionMessage;
        public long groupId;
        public boolean hasCaption;
        public boolean hasSibling;
        public boolean isDocuments;
        public boolean reversed;
        public ArrayList<MessageObject> messages = new ArrayList<>();
        public ArrayList<GroupedMessagePosition> posArray = new ArrayList<>();
        public HashMap<MessageObject, GroupedMessagePosition> positions = new HashMap<>();
        public LongSparseArray<GroupedMessagePosition> positionsArray = new LongSparseArray<>();
        private int maxSizeWidth = 800;
        public final TransitionParams transitionParams = new TransitionParams();

        public GroupedMessagePosition getPosition(MessageObject messageObject) {
            if (messageObject == null) {
                return null;
            }
            GroupedMessagePosition groupedMessagePosition = this.positions.get(messageObject);
            return groupedMessagePosition == null ? this.positionsArray.get(messageObject.getId()) : groupedMessagePosition;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
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

        /* JADX WARN: Code restructure failed: missing block: B:40:0x00a3, code lost:
            if ((org.telegram.messenger.MessageObject.getMedia(r14.messageOwner) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaInvoice) == false) goto L82;
         */
        /* JADX WARN: Removed duplicated region for block: B:20:0x005f  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0062  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0065  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x00b9  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x00dc  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x00df  */
        /* JADX WARN: Removed duplicated region for block: B:64:0x00ec  */
        /* JADX WARN: Removed duplicated region for block: B:65:0x00f3  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x010e  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x0129  */
        /* JADX WARN: Removed duplicated region for block: B:83:0x0140  */
        /* JADX WARN: Removed duplicated region for block: B:86:0x0145  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x0148  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            int i;
            boolean z;
            byte b;
            int i2;
            float f;
            int i3;
            float f2;
            MessageObject messageObject;
            int i4;
            float f3;
            float f4;
            this.posArray.clear();
            this.positions.clear();
            this.positionsArray.clear();
            Boolean bool = null;
            this.captionMessage = null;
            this.maxSizeWidth = 800;
            int size = this.messages.size();
            if (size == 1) {
                this.captionMessage = this.messages.get(0);
            } else if (size >= 1) {
                StringBuilder sb = new StringBuilder();
                this.hasSibling = false;
                this.hasCaption = false;
                int i5 = this.reversed ? size - 1 : 0;
                boolean z2 = false;
                float f5 = 1.0f;
                boolean z3 = false;
                boolean z4 = false;
                boolean z5 = true;
                while (true) {
                    if (this.reversed) {
                        if (i5 < 0) {
                            break;
                        }
                        messageObject = this.messages.get(i5);
                        if (i5 != (!this.reversed ? size - 1 : 0)) {
                            messageObject.isOutOwnerCached = bool;
                            z4 = messageObject.isOutOwner();
                            if (!z4) {
                                TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
                                if (tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.saved_from_peer != null) {
                                    i4 = size;
                                } else if (tLRPC$Message.from_id instanceof TLRPC$TL_peerUser) {
                                    TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
                                    i4 = size;
                                    if (tLRPC$Peer.channel_id == 0) {
                                        if (tLRPC$Peer.chat_id == 0) {
                                            if (!(MessageObject.getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGame)) {
                                            }
                                        }
                                    }
                                }
                                z2 = true;
                                if (!messageObject.isMusic() || messageObject.isDocument()) {
                                    this.isDocuments = true;
                                }
                            }
                            i4 = size;
                            z2 = false;
                            if (!messageObject.isMusic()) {
                            }
                            this.isDocuments = true;
                        } else {
                            i4 = size;
                        }
                        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        GroupedMessagePosition groupedMessagePosition = new GroupedMessagePosition();
                        groupedMessagePosition.last = this.reversed ? i5 == i4 + (-1) : i5 == 0;
                        f3 = closestPhotoSizeWithSize != null ? 1.0f : closestPhotoSizeWithSize.w / closestPhotoSizeWithSize.h;
                        groupedMessagePosition.aspectRatio = f3;
                        if (f3 <= 1.2f) {
                            sb.append("w");
                        } else if (f3 < 0.8f) {
                            sb.append("n");
                        } else {
                            sb.append("q");
                        }
                        f4 = groupedMessagePosition.aspectRatio;
                        f5 += f4;
                        if (f4 > 2.0f) {
                            z3 = true;
                        }
                        this.positions.put(messageObject, groupedMessagePosition);
                        boolean z6 = z2;
                        this.positionsArray.put(messageObject.getId(), groupedMessagePosition);
                        this.posArray.add(groupedMessagePosition);
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
                        i5 = !this.reversed ? i5 - 1 : i5 + 1;
                        size = i4;
                        z2 = z6;
                    } else {
                        if (i5 >= size) {
                            break;
                        }
                        messageObject = this.messages.get(i5);
                        if (i5 != (!this.reversed ? size - 1 : 0)) {
                        }
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        GroupedMessagePosition groupedMessagePosition2 = new GroupedMessagePosition();
                        groupedMessagePosition2.last = this.reversed ? i5 == i4 + (-1) : i5 == 0;
                        if (closestPhotoSizeWithSize2 != null) {
                        }
                        groupedMessagePosition2.aspectRatio = f3;
                        if (f3 <= 1.2f) {
                        }
                        f4 = groupedMessagePosition2.aspectRatio;
                        f5 += f4;
                        if (f4 > 2.0f) {
                        }
                        this.positions.put(messageObject, groupedMessagePosition2);
                        boolean z62 = z2;
                        this.positionsArray.put(messageObject.getId(), groupedMessagePosition2);
                        this.posArray.add(groupedMessagePosition2);
                        if (messageObject.caption == null) {
                        }
                        if (!this.reversed) {
                        }
                        size = i4;
                        z2 = z62;
                    }
                }
                int i6 = size;
                int i7 = 3;
                if (this.isDocuments) {
                    for (int i8 = 0; i8 < i6; i8++) {
                        GroupedMessagePosition groupedMessagePosition3 = this.posArray.get(i8);
                        groupedMessagePosition3.flags = 3;
                        if (i8 == 0) {
                            groupedMessagePosition3.flags = 3 | 4;
                            groupedMessagePosition3.last = false;
                        } else if (i8 == i6 - 1) {
                            groupedMessagePosition3.flags = 3 | 8;
                            groupedMessagePosition3.last = true;
                        } else {
                            groupedMessagePosition3.last = false;
                        }
                        groupedMessagePosition3.edge = true;
                        groupedMessagePosition3.aspectRatio = 1.0f;
                        groupedMessagePosition3.minX = (byte) 0;
                        groupedMessagePosition3.maxX = (byte) 0;
                        byte b2 = (byte) i8;
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
                float f7 = f5 / i6;
                float dp4 = AndroidUtilities.dp(100.0f) / 814.0f;
                if (z3 || !(i6 == 2 || i6 == 3 || i6 == 4)) {
                    z = z4;
                    int size2 = this.posArray.size();
                    float[] fArr = new float[size2];
                    for (int i10 = 0; i10 < i6; i10++) {
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
                    int i18 = 1;
                    while (i18 < size2 - 2) {
                        int i19 = 1;
                        while (true) {
                            int i20 = size2 - i18;
                            if (i19 < i20) {
                                int i21 = 1;
                                while (true) {
                                    int i22 = i20 - i19;
                                    if (i21 < i22) {
                                        int i23 = i22 - i21;
                                        if (i18 <= i7 && i19 <= i7 && i21 <= i7 && i23 <= i7) {
                                            int i24 = i18 + i19;
                                            int i25 = i24 + i21;
                                            arrayList.add(new MessageGroupedLayoutAttempt(i18, i19, i21, i23, multiHeight(fArr, 0, i18), multiHeight(fArr, i18, i24), multiHeight(fArr, i24, i25), multiHeight(fArr, i25, size2)));
                                        }
                                        i21++;
                                        i7 = 3;
                                    }
                                }
                                i19++;
                                i7 = 3;
                            }
                        }
                        i18++;
                        i7 = 3;
                    }
                    float f8 = (this.maxSizeWidth / 3) * 4;
                    int i26 = 0;
                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                    float f9 = 0.0f;
                    while (i26 < arrayList.size()) {
                        MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = (MessageGroupedLayoutAttempt) arrayList.get(i26);
                        int i27 = 0;
                        float f10 = Float.MAX_VALUE;
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
                        f8 = f12;
                        arrayList = arrayList2;
                    }
                    if (messageGroupedLayoutAttempt == null) {
                        return;
                    }
                    int i28 = 0;
                    int i29 = 0;
                    byte b3 = 0;
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
                        GroupedMessagePosition groupedMessagePosition4 = null;
                        int i34 = i29;
                        int i35 = 0;
                        while (i35 < i30) {
                            int i36 = (int) (fArr[i34] * f13);
                            i33 -= i36;
                            float[] fArr3 = fArr;
                            GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(i34);
                            int i37 = b3;
                            int i38 = i28 == 0 ? 4 : 0;
                            if (i28 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                                i38 |= 8;
                            }
                            if (i35 == 0) {
                                i38 |= 1;
                                if (z) {
                                    groupedMessagePosition4 = groupedMessagePosition5;
                                }
                            }
                            if (i35 == i32) {
                                i38 |= 2;
                                if (!z) {
                                    i2 = i38;
                                    groupedMessagePosition4 = groupedMessagePosition5;
                                    groupedMessagePosition5.set(i35, i35, i28, i28, i36, Math.max(dp4, f13 / 814.0f), i2);
                                    i34++;
                                    i35++;
                                    fArr = fArr3;
                                    b3 = i37;
                                }
                            }
                            i2 = i38;
                            groupedMessagePosition5.set(i35, i35, i28, i28, i36, Math.max(dp4, f13 / 814.0f), i2);
                            i34++;
                            i35++;
                            fArr = fArr3;
                            b3 = i37;
                        }
                        groupedMessagePosition4.pw += i33;
                        groupedMessagePosition4.spanSize += i33;
                        i28++;
                        i29 = i34;
                        fArr = fArr;
                    }
                    b = b3;
                } else if (i6 == 2) {
                    GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(0);
                    GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(1);
                    String sb2 = sb.toString();
                    if (sb2.equals("ww")) {
                        z = z4;
                        double d = f6;
                        Double.isNaN(d);
                        if (f7 > d * 1.4d) {
                            float f14 = groupedMessagePosition6.aspectRatio;
                            float f15 = groupedMessagePosition7.aspectRatio;
                            if (f14 - f15 < 0.2d) {
                                int i39 = this.maxSizeWidth;
                                float round = Math.round(Math.min(i39 / f14, Math.min(i39 / f15, 407.0f))) / 814.0f;
                                groupedMessagePosition6.set(0, 0, 0, 0, this.maxSizeWidth, round, 7);
                                groupedMessagePosition7.set(0, 0, 1, 1, this.maxSizeWidth, round, 11);
                                i3 = 0;
                                b = i3;
                            }
                        }
                    } else {
                        z = z4;
                    }
                    if (sb2.equals("ww") || sb2.equals("qq")) {
                        int i40 = this.maxSizeWidth / 2;
                        float f16 = i40;
                        float round2 = Math.round(Math.min(f16 / groupedMessagePosition6.aspectRatio, Math.min(f16 / groupedMessagePosition7.aspectRatio, 814.0f))) / 814.0f;
                        groupedMessagePosition6.set(0, 0, 0, 0, i40, round2, 13);
                        groupedMessagePosition7.set(1, 1, 0, 0, i40, round2, 14);
                    } else {
                        int i41 = this.maxSizeWidth;
                        float f17 = groupedMessagePosition6.aspectRatio;
                        int max = (int) Math.max(i41 * 0.4f, Math.round((i41 / f17) / ((1.0f / f17) + (1.0f / groupedMessagePosition7.aspectRatio))));
                        int i42 = this.maxSizeWidth - max;
                        if (i42 < dp2) {
                            max -= dp2 - i42;
                            i42 = dp2;
                        }
                        float min = Math.min(814.0f, Math.round(Math.min(i42 / groupedMessagePosition6.aspectRatio, max / groupedMessagePosition7.aspectRatio))) / 814.0f;
                        groupedMessagePosition6.set(0, 0, 0, 0, i42, min, 13);
                        groupedMessagePosition7.set(1, 1, 0, 0, max, min, 14);
                    }
                    i3 = 1;
                    b = i3;
                } else {
                    z = z4;
                    if (i6 == 3) {
                        GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(0);
                        GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(1);
                        GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(2);
                        if (sb.charAt(0) == 'n') {
                            float f18 = groupedMessagePosition9.aspectRatio;
                            float min2 = Math.min(407.0f, Math.round((this.maxSizeWidth * f18) / (groupedMessagePosition10.aspectRatio + f18)));
                            int max2 = (int) Math.max(dp2, Math.min(this.maxSizeWidth * 0.5f, Math.round(Math.min(groupedMessagePosition10.aspectRatio * min2, groupedMessagePosition9.aspectRatio * f2))));
                            int round3 = Math.round(Math.min((groupedMessagePosition8.aspectRatio * 814.0f) + dp3, this.maxSizeWidth - max2));
                            groupedMessagePosition8.set(0, 0, 0, 1, round3, 1.0f, 13);
                            float f19 = (814.0f - min2) / 814.0f;
                            groupedMessagePosition9.set(1, 1, 0, 0, max2, f19, 6);
                            float f20 = min2 / 814.0f;
                            groupedMessagePosition10.set(0, 1, 1, 1, max2, f20, 10);
                            int i43 = this.maxSizeWidth;
                            groupedMessagePosition10.spanSize = i43;
                            groupedMessagePosition8.siblingHeights = new float[]{f20, f19};
                            if (z) {
                                groupedMessagePosition8.spanSize = i43 - max2;
                            } else {
                                groupedMessagePosition9.spanSize = i43 - round3;
                                groupedMessagePosition10.leftSpanOffset = round3;
                            }
                            this.hasSibling = true;
                        } else {
                            float round4 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition8.aspectRatio, 537.24005f)) / 814.0f;
                            groupedMessagePosition8.set(0, 1, 0, 0, this.maxSizeWidth, round4, 7);
                            int i44 = this.maxSizeWidth / 2;
                            float f21 = i44;
                            float min3 = Math.min(814.0f - round4, Math.round(Math.min(f21 / groupedMessagePosition9.aspectRatio, f21 / groupedMessagePosition10.aspectRatio))) / 814.0f;
                            if (min3 < dp4) {
                                min3 = dp4;
                            }
                            float f22 = min3;
                            groupedMessagePosition9.set(0, 0, 1, 1, i44, f22, 9);
                            groupedMessagePosition10.set(1, 1, 1, 1, i44, f22, 10);
                        }
                        b = 1;
                    } else {
                        GroupedMessagePosition groupedMessagePosition11 = this.posArray.get(0);
                        GroupedMessagePosition groupedMessagePosition12 = this.posArray.get(1);
                        GroupedMessagePosition groupedMessagePosition13 = this.posArray.get(2);
                        GroupedMessagePosition groupedMessagePosition14 = this.posArray.get(3);
                        if (sb.charAt(0) == 'w') {
                            float round5 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition11.aspectRatio, 537.24005f)) / 814.0f;
                            groupedMessagePosition11.set(0, 2, 0, 0, this.maxSizeWidth, round5, 7);
                            float round6 = Math.round(this.maxSizeWidth / ((groupedMessagePosition12.aspectRatio + groupedMessagePosition13.aspectRatio) + groupedMessagePosition14.aspectRatio));
                            float f23 = dp2;
                            int max3 = (int) Math.max(f23, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition12.aspectRatio * round6));
                            int max4 = (int) Math.max(Math.max(f23, this.maxSizeWidth * 0.33f), groupedMessagePosition14.aspectRatio * round6);
                            int i45 = (this.maxSizeWidth - max3) - max4;
                            if (i45 < AndroidUtilities.dp(58.0f)) {
                                int dp5 = AndroidUtilities.dp(58.0f) - i45;
                                i45 = AndroidUtilities.dp(58.0f);
                                int i46 = dp5 / 2;
                                max3 -= i46;
                                max4 -= dp5 - i46;
                            }
                            int i47 = max3;
                            float min4 = Math.min(814.0f - round5, round6) / 814.0f;
                            if (min4 < dp4) {
                                min4 = dp4;
                            }
                            float f24 = min4;
                            groupedMessagePosition12.set(0, 0, 1, 1, i47, f24, 9);
                            groupedMessagePosition13.set(1, 1, 1, 1, i45, f24, 8);
                            groupedMessagePosition14.set(2, 2, 1, 1, max4, f24, 10);
                            i3 = 2;
                            b = i3;
                        } else {
                            int max5 = Math.max(dp2, Math.round(814.0f / (((1.0f / groupedMessagePosition12.aspectRatio) + (1.0f / groupedMessagePosition13.aspectRatio)) + (1.0f / groupedMessagePosition14.aspectRatio))));
                            float f25 = dp;
                            float f26 = max5;
                            float min5 = Math.min(0.33f, Math.max(f25, f26 / groupedMessagePosition12.aspectRatio) / 814.0f);
                            float min6 = Math.min(0.33f, Math.max(f25, f26 / groupedMessagePosition13.aspectRatio) / 814.0f);
                            float f27 = (1.0f - min5) - min6;
                            int round7 = Math.round(Math.min((814.0f * groupedMessagePosition11.aspectRatio) + dp3, this.maxSizeWidth - max5));
                            groupedMessagePosition11.set(0, 0, 0, 2, round7, min5 + min6 + f27, 13);
                            groupedMessagePosition12.set(1, 1, 0, 0, max5, min5, 6);
                            groupedMessagePosition13.set(0, 1, 1, 1, max5, min6, 2);
                            groupedMessagePosition13.spanSize = this.maxSizeWidth;
                            groupedMessagePosition14.set(0, 1, 2, 2, max5, f27, 10);
                            int i48 = this.maxSizeWidth;
                            groupedMessagePosition14.spanSize = i48;
                            if (z) {
                                groupedMessagePosition11.spanSize = i48 - max5;
                            } else {
                                groupedMessagePosition12.spanSize = i48 - round7;
                                groupedMessagePosition13.leftSpanOffset = round7;
                                groupedMessagePosition14.leftSpanOffset = round7;
                            }
                            groupedMessagePosition11.siblingHeights = new float[]{min5, min6, f27};
                            this.hasSibling = true;
                            i3 = 1;
                            b = i3;
                        }
                    }
                }
                for (int i49 = 0; i49 < i6; i49++) {
                    GroupedMessagePosition groupedMessagePosition15 = this.posArray.get(i49);
                    if (z) {
                        if (groupedMessagePosition15.minX == 0) {
                            groupedMessagePosition15.spanSize += i;
                        }
                        if ((groupedMessagePosition15.flags & 2) != 0) {
                            groupedMessagePosition15.edge = true;
                        }
                    } else {
                        if (groupedMessagePosition15.maxX == b || (groupedMessagePosition15.flags & 2) != 0) {
                            groupedMessagePosition15.spanSize += i;
                        }
                        if ((groupedMessagePosition15.flags & 1) != 0) {
                            groupedMessagePosition15.edge = true;
                        }
                    }
                    MessageObject messageObject2 = this.messages.get(i49);
                    if (!z && messageObject2.needDrawAvatarInternal()) {
                        if (groupedMessagePosition15.edge) {
                            int i50 = groupedMessagePosition15.spanSize;
                            if (i50 != 1000) {
                                groupedMessagePosition15.spanSize = i50 + R.styleable.AppCompatTheme_textAppearanceSearchResultTitle;
                            }
                            groupedMessagePosition15.pw += R.styleable.AppCompatTheme_textAppearanceSearchResultTitle;
                        } else {
                            if ((groupedMessagePosition15.flags & 2) != 0) {
                                int i51 = groupedMessagePosition15.spanSize;
                                if (i51 != 1000) {
                                    groupedMessagePosition15.spanSize = i51 - 108;
                                } else {
                                    int i52 = groupedMessagePosition15.leftSpanOffset;
                                    if (i52 != 0) {
                                        groupedMessagePosition15.leftSpanOffset = i52 + R.styleable.AppCompatTheme_textAppearanceSearchResultTitle;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        public MessageObject findPrimaryMessageObject() {
            return findMessageWithFlags(this.reversed ? 10 : 5);
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

        /* loaded from: classes3.dex */
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
    }

    public MessageObject(int i, TL_stories$StoryItem tL_stories$StoryItem) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.overrideLinkColor = -1;
        this.overrideLinkEmoji = -1L;
        this.topicIconDrawable = new Drawable[1];
        this.spoiledLoginCode = false;
        this.translated = false;
        this.currentAccount = i;
        this.storyItem = tL_stories$StoryItem;
        if (tL_stories$StoryItem != null) {
            TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
            this.messageOwner = tLRPC$TL_message;
            tLRPC$TL_message.id = tL_stories$StoryItem.id;
            tLRPC$TL_message.date = tL_stories$StoryItem.date;
            tLRPC$TL_message.dialog_id = tL_stories$StoryItem.dialogId;
            tLRPC$TL_message.message = tL_stories$StoryItem.caption;
            tLRPC$TL_message.entities = tL_stories$StoryItem.entities;
            tLRPC$TL_message.media = tL_stories$StoryItem.media;
            tLRPC$TL_message.attachPath = tL_stories$StoryItem.attachPath;
        }
        this.photoThumbs = new ArrayList<>();
        this.photoThumbs2 = new ArrayList<>();
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, String str, String str2, String str3, boolean z, boolean z2, boolean z3, boolean z4) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.overrideLinkColor = -1;
        this.overrideLinkEmoji = -1L;
        this.topicIconDrawable = new Drawable[1];
        this.spoiledLoginCode = false;
        this.translated = false;
        this.localType = z ? 2 : 1;
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
        this(i, tLRPC$Message, null, null, null, longSparseArray, longSparseArray2, z, z2, 0L, false, false, false);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2, boolean z, boolean z2, boolean z3) {
        this(i, tLRPC$Message, null, null, null, longSparseArray, longSparseArray2, z, z2, 0L, false, false, z3);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, boolean z, boolean z2, long j) {
        this(i, tLRPC$Message, null, abstractMap, abstractMap2, null, null, z, z2, j);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2, boolean z, boolean z2, long j) {
        this(i, tLRPC$Message, messageObject, abstractMap, abstractMap2, longSparseArray, longSparseArray2, z, z2, j, false, false, false);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2, boolean z, boolean z2, long j, boolean z3, boolean z4, boolean z5) {
        TextPaint textPaint;
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.overrideLinkColor = -1;
        this.overrideLinkEmoji = -1L;
        this.topicIconDrawable = new Drawable[1];
        this.spoiledLoginCode = false;
        this.translated = false;
        Theme.createCommonMessageResources();
        this.isRepostPreview = z3;
        this.isRepostVideoPreview = z4;
        this.isSaved = z5 || getDialogId(tLRPC$Message) == UserConfig.getInstance(i).getClientUserId();
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
        if (z) {
            updateTranslation(false);
        }
        measureInlineBotButtons();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(this.messageOwner.date * 1000);
        int i2 = gregorianCalendar.get(6);
        int i3 = gregorianCalendar.get(1);
        int i4 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i2));
        this.dateKeyInt = (i4 * 10000) + i3 + (i2 * MediaController.VIDEO_BITRATE_480);
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

    protected void checkBigAnimatedEmoji() {
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
        } else if (isSticker()) {
            this.type = 13;
        } else if (isAnimatedSticker()) {
            this.type = 15;
        } else {
            this.type = 1000;
        }
    }

    private void createPathThumb() {
        TLRPC$Document document = getDocument();
        if (document == null) {
            return;
        }
        this.pathThumb = DocumentObject.getSvgThumb(document, Theme.key_chat_serviceBackground, 1.0f);
    }

    public void createStrippedThumb() {
        if (this.photoThumbs != null) {
            if ((canCreateStripedThubms() || hasExtendedMediaPreview()) && this.strippedThumb == null) {
                try {
                    String str = isRoundVideo() ? "br" : "b";
                    int size = this.photoThumbs.size();
                    for (int i = 0; i < size; i++) {
                        TLRPC$PhotoSize tLRPC$PhotoSize = this.photoThumbs.get(i);
                        if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                            this.strippedThumb = new BitmapDrawable(ApplicationLoader.applicationContext.getResources(), ImageLoader.getStrippedPhotoBitmap(tLRPC$PhotoSize.bytes, str));
                            return;
                        }
                    }
                } catch (Throwable th) {
                    FileLog.e(th);
                }
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
        TextPaint textPaint;
        if (num != null && num.intValue() >= 1 && this.messageOwner != null && !hasNonEmojiEntities()) {
            CharSequence charSequence = this.messageText;
            Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) ((Spannable) charSequence).getSpans(0, charSequence.length(), Emoji.EmojiSpan.class);
            CharSequence charSequence2 = this.messageText;
            AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spannable) charSequence2).getSpans(0, charSequence2.length(), AnimatedEmojiSpan.class);
            this.emojiOnlyCount = Math.max(num.intValue(), (emojiSpanArr == null ? 0 : emojiSpanArr.length) + (animatedEmojiSpanArr == null ? 0 : animatedEmojiSpanArr.length));
            this.totalAnimatedEmojiCount = animatedEmojiSpanArr == null ? 0 : animatedEmojiSpanArr.length;
            this.animatedEmojiCount = 0;
            if (animatedEmojiSpanArr != null) {
                for (AnimatedEmojiSpan animatedEmojiSpan : animatedEmojiSpanArr) {
                    if (!animatedEmojiSpan.standard) {
                        this.animatedEmojiCount++;
                    }
                }
            }
            int i = this.emojiOnlyCount;
            boolean z = (i - (emojiSpanArr == null ? 0 : emojiSpanArr.length)) - (animatedEmojiSpanArr == null ? 0 : animatedEmojiSpanArr.length) > 0;
            this.hasUnwrappedEmoji = z;
            if (i == 0 || z) {
                if (animatedEmojiSpanArr == null || animatedEmojiSpanArr.length <= 0) {
                    return;
                }
                for (int i2 = 0; i2 < animatedEmojiSpanArr.length; i2++) {
                    animatedEmojiSpanArr[i2].replaceFontMetrics(Theme.chat_msgTextPaint.getFontMetricsInt(), (int) (Theme.chat_msgTextPaint.getTextSize() + AndroidUtilities.dp(4.0f)), -1);
                    animatedEmojiSpanArr[i2].full = false;
                }
                return;
            }
            int i3 = this.animatedEmojiCount;
            boolean z2 = i == i3;
            int i4 = 2;
            switch (Math.max(i, i3)) {
                case 0:
                case 1:
                case 2:
                    TextPaint[] textPaintArr = Theme.chat_msgTextPaintEmoji;
                    textPaint = z2 ? textPaintArr[0] : textPaintArr[2];
                    i4 = 1;
                    break;
                case 3:
                    TextPaint[] textPaintArr2 = Theme.chat_msgTextPaintEmoji;
                    textPaint = z2 ? textPaintArr2[1] : textPaintArr2[3];
                    i4 = 1;
                    break;
                case 4:
                    TextPaint[] textPaintArr3 = Theme.chat_msgTextPaintEmoji;
                    textPaint = z2 ? textPaintArr3[2] : textPaintArr3[4];
                    i4 = 1;
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
                    int i5 = this.emojiOnlyCount > 9 ? 0 : -1;
                    textPaint = Theme.chat_msgTextPaintEmoji[5];
                    i4 = i5;
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
                animatedEmojiSpanArr[i6].replaceFontMetrics(textPaint.getFontMetricsInt(), textSize, i4);
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

    /* JADX WARN: Removed duplicated region for block: B:1005:0x1bbd  */
    /* JADX WARN: Removed duplicated region for block: B:1008:0x1c0e  */
    /* JADX WARN: Removed duplicated region for block: B:1010:0x1c11  */
    /* JADX WARN: Removed duplicated region for block: B:1022:0x1c93  */
    /* JADX WARN: Removed duplicated region for block: B:1026:0x1c9a  */
    /* JADX WARN: Removed duplicated region for block: B:1051:0x0505 A[EDGE_INSN: B:1051:0x0505->B:195:0x0505 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1061:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x04df  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x04ef A[LOOP:0: B:170:0x04ab->B:193:0x04ef, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:474:0x0cef  */
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
        TLRPC$Chat tLRPC$Chat2;
        StringBuilder sb;
        int i2;
        String str2;
        TLRPC$Chat tLRPC$Chat3;
        String str3;
        SpannableString spannableString;
        SpannableString spannableString2;
        SpannableString spannableString3;
        boolean z2;
        SpannableString spannableString4;
        int i3;
        String string2;
        int i4;
        String str4;
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
        int i5;
        String str5;
        int i6;
        String str6;
        int i7;
        String str7;
        int i8;
        String str8;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        String str9;
        TLObject chat4;
        String string3;
        StringBuilder sb2;
        boolean z3;
        char c4;
        String formatPluralString3;
        int i9;
        int i10;
        boolean z4;
        TLObject chat5;
        ArrayList<MessageObject> arrayList4;
        int[] iArr2;
        TextPaint textPaint;
        MessageObject messageObject = this;
        TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
        messageObject.type = 1000;
        messageObject.forceSeekTo = -1.0f;
        messageObject.overrideLinkColor = -1;
        messageObject.overrideLinkEmoji = -1L;
        messageObject.topicIconDrawable = new Drawable[1];
        messageObject.spoiledLoginCode = false;
        messageObject.translated = false;
        messageObject.currentEvent = tLRPC$TL_channelAdminLogEvent2;
        messageObject.currentAccount = i;
        TLRPC$User user2 = tLRPC$TL_channelAdminLogEvent2.user_id > 0 ? MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEvent2.user_id)) : null;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(tLRPC$TL_channelAdminLogEvent2.date * 1000);
        int i11 = gregorianCalendar.get(6);
        int i12 = gregorianCalendar.get(1);
        int i13 = gregorianCalendar.get(2);
        messageObject.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i12), Integer.valueOf(i13), Integer.valueOf(i11));
        messageObject.dateKeyInt = (i13 * 1000) + i12 + (i11 * 100000);
        messageObject.monthKey = String.format("%d_%02d", Integer.valueOf(i12), Integer.valueOf(i13));
        TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
        tLRPC$TL_peerChannel.channel_id = tLRPC$Chat.id;
        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction = tLRPC$TL_channelAdminLogEvent2.action;
        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTitle) {
            String str10 = ((TLRPC$TL_channelAdminLogEventActionChangeTitle) tLRPC$ChannelAdminLogEventAction).new_value;
            if (tLRPC$Chat.megagroup) {
                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedGroupTitle", R.string.EventLogEditedGroupTitle, str10), "un1", user2);
            } else {
                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedChannelTitle", R.string.EventLogEditedChannelTitle, str10), "un1", user2);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangePhoto) {
            TLRPC$TL_channelAdminLogEventActionChangePhoto tLRPC$TL_channelAdminLogEventActionChangePhoto = (TLRPC$TL_channelAdminLogEventActionChangePhoto) tLRPC$ChannelAdminLogEventAction;
            TLRPC$TL_messageService tLRPC$TL_messageService = new TLRPC$TL_messageService();
            messageObject.messageOwner = tLRPC$TL_messageService;
            if (tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo instanceof TLRPC$TL_photoEmpty) {
                tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatDeletePhoto();
                if (tLRPC$Chat.megagroup) {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedWGroupPhoto", R.string.EventLogRemovedWGroupPhoto), "un1", user2);
                } else {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedChannelPhoto", R.string.EventLogRemovedChannelPhoto), "un1", user2);
                }
            } else {
                tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatEditPhoto();
                messageObject.messageOwner.action.photo = tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo;
                if (tLRPC$Chat.megagroup) {
                    if (isVideoAvatar()) {
                        messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedGroupVideo", R.string.EventLogEditedGroupVideo), "un1", user2);
                    } else {
                        messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedGroupPhoto", R.string.EventLogEditedGroupPhoto), "un1", user2);
                    }
                } else if (isVideoAvatar()) {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedChannelVideo", R.string.EventLogEditedChannelVideo), "un1", user2);
                } else {
                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedChannelPhoto", R.string.EventLogEditedChannelPhoto), "un1", user2);
                }
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoin) {
            if (tLRPC$Chat.megagroup) {
                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogGroupJoined), "un1", user2);
            } else {
                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogChannelJoined), "un1", user2);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantLeave) {
            TLRPC$TL_messageService tLRPC$TL_messageService2 = new TLRPC$TL_messageService();
            messageObject.messageOwner = tLRPC$TL_messageService2;
            tLRPC$TL_messageService2.action = new TLRPC$TL_messageActionChatDeleteUser();
            messageObject.messageOwner.action.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
            if (tLRPC$Chat.megagroup) {
                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogLeftGroup", R.string.EventLogLeftGroup), "un1", user2);
            } else {
                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogLeftChannel", R.string.EventLogLeftChannel), "un1", user2);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantInvite) {
            TLRPC$TL_messageService tLRPC$TL_messageService3 = new TLRPC$TL_messageService();
            messageObject.messageOwner = tLRPC$TL_messageService3;
            tLRPC$TL_messageService3.action = new TLRPC$TL_messageActionChatAddUser();
            long peerId = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantInvite) tLRPC$ChannelAdminLogEventAction).participant.peer);
            if (peerId > 0) {
                chat5 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId));
            } else {
                chat5 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId));
            }
            TLRPC$Peer tLRPC$Peer2 = messageObject.messageOwner.from_id;
            if ((tLRPC$Peer2 instanceof TLRPC$TL_peerUser) && peerId == tLRPC$Peer2.user_id) {
                if (tLRPC$Chat.megagroup) {
                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogGroupJoined), "un1", user2);
                } else {
                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogChannelJoined), "un1", user2);
                }
            } else {
                CharSequence replaceWithLink = replaceWithLink(LocaleController.getString("EventLogAdded", R.string.EventLogAdded), "un2", chat5);
                messageObject.messageText = replaceWithLink;
                messageObject.messageText = replaceWithLink(replaceWithLink, "un1", user2);
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
                messageObject.messageOwner = new TLRPC$TL_message();
                long peerId2 = getPeerId(tLRPC$ChannelParticipant.peer);
                if (peerId2 > 0) {
                    user = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId2));
                } else {
                    user = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(-peerId2));
                }
                if (!(tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator) && (tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator)) {
                    String string4 = LocaleController.getString("EventLogChangedOwnership", R.string.EventLogChangedOwnership);
                    sb = new StringBuilder(String.format(string4, messageObject.getUserName(user, messageObject.messageOwner.entities, string4.indexOf("%1$s"))));
                    tLRPC$Chat2 = tLRPC$Chat;
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
                    StringBuilder sb3 = new StringBuilder(String.format(string, messageObject.getUserName(user, messageObject.messageOwner.entities, string.indexOf("%1$s"))));
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
                        tLRPC$Chat2 = tLRPC$Chat;
                        if (tLRPC$Chat2.megagroup) {
                            i2 = R.string.EventLogPromotedChangeGroupInfo;
                            str2 = "EventLogPromotedChangeGroupInfo";
                        } else {
                            i2 = R.string.EventLogPromotedChangeChannelInfo;
                            str2 = "EventLogPromotedChangeChannelInfo";
                        }
                        sb3.append(LocaleController.getString(str2, i2));
                    } else {
                        tLRPC$Chat2 = tLRPC$Chat;
                    }
                    if (!tLRPC$Chat2.megagroup) {
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
                    if (tLRPC$TL_chatAdminRights.post_stories != tLRPC$TL_chatAdminRights2.post_stories) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.post_stories ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedPostStories", R.string.EventLogPromotedPostStories));
                    }
                    if (tLRPC$TL_chatAdminRights.edit_stories != tLRPC$TL_chatAdminRights2.edit_stories) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.edit_stories ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedEditStories", R.string.EventLogPromotedEditStories));
                    }
                    if (tLRPC$TL_chatAdminRights.delete_stories != tLRPC$TL_chatAdminRights2.delete_stories) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.delete_stories ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedDeleteStories", R.string.EventLogPromotedDeleteStories));
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
                    if (tLRPC$Chat2.megagroup) {
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
                    if (tLRPC$Chat2.megagroup && tLRPC$TL_chatAdminRights.pin_messages != tLRPC$TL_chatAdminRights2.pin_messages) {
                        sb3.append('\n');
                        sb3.append(tLRPC$TL_chatAdminRights2.pin_messages ? c : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString("EventLogPromotedPinMessages", R.string.EventLogPromotedPinMessages));
                    }
                    sb = sb3;
                }
                messageObject.messageText = sb.toString();
            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) {
                TLRPC$TL_channelAdminLogEventActionDefaultBannedRights tLRPC$TL_channelAdminLogEventActionDefaultBannedRights = (TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) tLRPC$ChannelAdminLogEventAction;
                messageObject.messageOwner = new TLRPC$TL_message();
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
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (tLRPC$TL_chatBannedRights.send_stickers != tLRPC$TL_chatBannedRights2.send_stickers || tLRPC$TL_chatBannedRights.send_inline != tLRPC$TL_chatBannedRights2.send_inline || tLRPC$TL_chatBannedRights.send_gifs != tLRPC$TL_chatBannedRights2.send_gifs || tLRPC$TL_chatBannedRights.send_games != tLRPC$TL_chatBannedRights2.send_games) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_stickers ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendStickers", R.string.EventLogRestrictedSendStickers));
                }
                if (tLRPC$TL_chatBannedRights.send_media != tLRPC$TL_chatBannedRights2.send_media) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_media ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendMedia", R.string.EventLogRestrictedSendMedia));
                }
                if (tLRPC$TL_chatBannedRights.send_polls != tLRPC$TL_chatBannedRights2.send_polls) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.send_polls ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendPolls", R.string.EventLogRestrictedSendPolls));
                }
                if (tLRPC$TL_chatBannedRights.embed_links != tLRPC$TL_chatBannedRights2.embed_links) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.embed_links ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedSendEmbed", R.string.EventLogRestrictedSendEmbed));
                }
                if (tLRPC$TL_chatBannedRights.change_info != tLRPC$TL_chatBannedRights2.change_info) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.change_info ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedChangeInfo", R.string.EventLogRestrictedChangeInfo));
                }
                if (tLRPC$TL_chatBannedRights.invite_users != tLRPC$TL_chatBannedRights2.invite_users) {
                    if (!z4) {
                        sb4.append('\n');
                        z4 = true;
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.invite_users ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedInviteUsers", R.string.EventLogRestrictedInviteUsers));
                }
                if (tLRPC$TL_chatBannedRights.pin_messages != tLRPC$TL_chatBannedRights2.pin_messages) {
                    if (!z4) {
                        sb4.append('\n');
                    }
                    sb4.append('\n');
                    sb4.append(!tLRPC$TL_chatBannedRights2.pin_messages ? '+' : '-');
                    sb4.append(' ');
                    sb4.append(LocaleController.getString("EventLogRestrictedPinMessages", R.string.EventLogRestrictedPinMessages));
                }
                messageObject.messageText = sb4.toString();
            } else {
                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) {
                    TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2 = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                    messageObject.messageOwner = new TLRPC$TL_message();
                    long peerId3 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.peer);
                    if (peerId3 > 0) {
                        messageObject = this;
                        chat4 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId3));
                        str9 = ", ";
                    } else {
                        messageObject = this;
                        str9 = ", ";
                        chat4 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId3));
                    }
                    TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights3 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.banned_rights;
                    TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights4 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.new_participant.banned_rights;
                    if (tLRPC$Chat.megagroup && (tLRPC$TL_chatBannedRights4 == null || !tLRPC$TL_chatBannedRights4.view_messages || (tLRPC$TL_chatBannedRights3 != null && tLRPC$TL_chatBannedRights4.until_date != tLRPC$TL_chatBannedRights3.until_date))) {
                        if (tLRPC$TL_chatBannedRights4 != null && !AndroidUtilities.isBannedForever(tLRPC$TL_chatBannedRights4)) {
                            sb2 = new StringBuilder();
                            int i14 = tLRPC$TL_chatBannedRights4.until_date - tLRPC$TL_channelAdminLogEvent2.date;
                            int i15 = ((i14 / 60) / 60) / 24;
                            int i16 = i14 - (((i15 * 60) * 60) * 24);
                            int i17 = (i16 / 60) / 60;
                            int i18 = (i16 - ((i17 * 60) * 60)) / 60;
                            str = "";
                            int i19 = 0;
                            int i20 = 0;
                            while (i19 < 3) {
                                if (i19 == 0) {
                                    if (i15 != 0) {
                                        formatPluralString3 = LocaleController.formatPluralString("Days", i15, new Object[0]);
                                        i9 = i20 + 1;
                                        i10 = i9;
                                        if (formatPluralString3 != null) {
                                            if (sb2.length() > 0) {
                                                sb2.append(str9);
                                            }
                                            sb2.append(formatPluralString3);
                                        }
                                        if (i10 == 2) {
                                            break;
                                        }
                                        i19++;
                                        i20 = i10;
                                    }
                                    i10 = i20;
                                    formatPluralString3 = null;
                                    if (formatPluralString3 != null) {
                                    }
                                    if (i10 == 2) {
                                    }
                                } else {
                                    if (i19 == 1) {
                                        if (i17 != 0) {
                                            i9 = i20 + 1;
                                            formatPluralString3 = LocaleController.formatPluralString("Hours", i17, new Object[0]);
                                            i10 = i9;
                                            if (formatPluralString3 != null) {
                                            }
                                            if (i10 == 2) {
                                            }
                                        }
                                    } else if (i18 != 0) {
                                        formatPluralString3 = LocaleController.formatPluralString("Minutes", i18, new Object[0]);
                                        i9 = i20 + 1;
                                        i10 = i9;
                                        if (formatPluralString3 != null) {
                                        }
                                        if (i10 == 2) {
                                        }
                                    }
                                    i10 = i20;
                                    formatPluralString3 = null;
                                    if (formatPluralString3 != null) {
                                    }
                                    if (i10 == 2) {
                                    }
                                }
                            }
                        } else {
                            str = "";
                            sb2 = new StringBuilder(LocaleController.getString("UserRestrictionsUntilForever", R.string.UserRestrictionsUntilForever));
                        }
                        String string5 = LocaleController.getString("EventLogRestrictedUntil", R.string.EventLogRestrictedUntil);
                        StringBuilder sb5 = new StringBuilder(String.format(string5, messageObject.getUserName(chat4, messageObject.messageOwner.entities, string5.indexOf("%1$s")), sb2.toString()));
                        tLRPC$TL_chatBannedRights3 = tLRPC$TL_chatBannedRights3 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights3;
                        tLRPC$TL_chatBannedRights4 = tLRPC$TL_chatBannedRights4 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights4;
                        if (tLRPC$TL_chatBannedRights3.view_messages != tLRPC$TL_chatBannedRights4.view_messages) {
                            sb5.append('\n');
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.view_messages ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedReadMessages", R.string.EventLogRestrictedReadMessages));
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        if (tLRPC$TL_chatBannedRights3.send_messages != tLRPC$TL_chatBannedRights4.send_messages) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_messages ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendMessages", R.string.EventLogRestrictedSendMessages));
                        }
                        if (tLRPC$TL_chatBannedRights3.send_stickers != tLRPC$TL_chatBannedRights4.send_stickers || tLRPC$TL_chatBannedRights3.send_inline != tLRPC$TL_chatBannedRights4.send_inline || tLRPC$TL_chatBannedRights3.send_gifs != tLRPC$TL_chatBannedRights4.send_gifs || tLRPC$TL_chatBannedRights3.send_games != tLRPC$TL_chatBannedRights4.send_games) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_stickers ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendStickers", R.string.EventLogRestrictedSendStickers));
                        }
                        if (tLRPC$TL_chatBannedRights3.send_media != tLRPC$TL_chatBannedRights4.send_media) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_media ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendMedia", R.string.EventLogRestrictedSendMedia));
                        }
                        if (tLRPC$TL_chatBannedRights3.send_polls != tLRPC$TL_chatBannedRights4.send_polls) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.send_polls ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendPolls", R.string.EventLogRestrictedSendPolls));
                        }
                        if (tLRPC$TL_chatBannedRights3.embed_links != tLRPC$TL_chatBannedRights4.embed_links) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.embed_links ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedSendEmbed", R.string.EventLogRestrictedSendEmbed));
                        }
                        if (tLRPC$TL_chatBannedRights3.change_info != tLRPC$TL_chatBannedRights4.change_info) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.change_info ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedChangeInfo", R.string.EventLogRestrictedChangeInfo));
                        }
                        if (tLRPC$TL_chatBannedRights3.invite_users != tLRPC$TL_chatBannedRights4.invite_users) {
                            if (!z3) {
                                sb5.append('\n');
                                z3 = true;
                            }
                            sb5.append('\n');
                            sb5.append(!tLRPC$TL_chatBannedRights4.invite_users ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedInviteUsers", R.string.EventLogRestrictedInviteUsers));
                        }
                        if (tLRPC$TL_chatBannedRights3.pin_messages != tLRPC$TL_chatBannedRights4.pin_messages) {
                            if (z3) {
                                c4 = '\n';
                            } else {
                                c4 = '\n';
                                sb5.append('\n');
                            }
                            sb5.append(c4);
                            sb5.append(!tLRPC$TL_chatBannedRights4.pin_messages ? '+' : '-');
                            sb5.append(' ');
                            sb5.append(LocaleController.getString("EventLogRestrictedPinMessages", R.string.EventLogRestrictedPinMessages));
                        }
                        messageObject.messageText = sb5.toString();
                    } else {
                        str = "";
                        if (tLRPC$TL_chatBannedRights4 != null && (tLRPC$TL_chatBannedRights3 == null || tLRPC$TL_chatBannedRights4.view_messages)) {
                            string3 = LocaleController.getString("EventLogChannelRestricted", R.string.EventLogChannelRestricted);
                        } else {
                            string3 = LocaleController.getString("EventLogChannelUnrestricted", R.string.EventLogChannelUnrestricted);
                        }
                        messageObject.messageText = String.format(string3, messageObject.getUserName(chat4, messageObject.messageOwner.entities, string3.indexOf("%1$s")));
                    }
                } else {
                    str = "";
                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionUpdatePinned) {
                        TLRPC$TL_channelAdminLogEventActionUpdatePinned tLRPC$TL_channelAdminLogEventActionUpdatePinned = (TLRPC$TL_channelAdminLogEventActionUpdatePinned) tLRPC$ChannelAdminLogEventAction;
                        tLRPC$Message = tLRPC$TL_channelAdminLogEventActionUpdatePinned.message;
                        if (user2 != null && user2.id == 136817688 && (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) != null && (tLRPC$MessageFwdHeader.from_id instanceof TLRPC$TL_peerChannel)) {
                            TLRPC$Chat chat6 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(tLRPC$TL_channelAdminLogEventActionUpdatePinned.message.fwd_from.from_id.channel_id));
                            TLRPC$Message tLRPC$Message2 = tLRPC$TL_channelAdminLogEventActionUpdatePinned.message;
                            if ((tLRPC$Message2 instanceof TLRPC$TL_messageEmpty) || !tLRPC$Message2.pinned) {
                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", R.string.EventLogUnpinnedMessages), "un1", chat6);
                            } else {
                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogPinnedMessages", R.string.EventLogPinnedMessages), "un1", chat6);
                            }
                        } else if ((tLRPC$Message instanceof TLRPC$TL_messageEmpty) || !tLRPC$Message.pinned) {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", R.string.EventLogUnpinnedMessages), "un1", user2);
                        } else {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogPinnedMessages", R.string.EventLogPinnedMessages), "un1", user2);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStopPoll) {
                        tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionStopPoll) tLRPC$ChannelAdminLogEventAction).message;
                        if ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPoll) && ((TLRPC$TL_messageMediaPoll) getMedia(tLRPC$Message)).poll.quiz) {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogStopQuiz", R.string.EventLogStopQuiz), "un1", user2);
                        } else {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogStopPoll", R.string.EventLogStopPoll), "un1", user2);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSignatures) {
                        if (((TLRPC$TL_channelAdminLogEventActionToggleSignatures) tLRPC$ChannelAdminLogEventAction).new_value) {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOn", R.string.EventLogToggledSignaturesOn), "un1", user2);
                        } else {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOff", R.string.EventLogToggledSignaturesOff), "un1", user2);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleInvites) {
                        if (((TLRPC$TL_channelAdminLogEventActionToggleInvites) tLRPC$ChannelAdminLogEventAction).new_value) {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesOn", R.string.EventLogToggledInvitesOn), "un1", user2);
                        } else {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesOff", R.string.EventLogToggledInvitesOff), "un1", user2);
                        }
                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                        tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionDeleteMessage) tLRPC$ChannelAdminLogEventAction).message;
                        if (user2 != null && user2.id == MessagesController.getInstance(messageObject.currentAccount).telegramAntispamUserId) {
                            messageObject.messageText = LocaleController.getString("EventLogDeletedMessages", R.string.EventLogDeletedMessages).replace("un1", UserObject.getUserName(user2));
                        } else {
                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogDeletedMessages", R.string.EventLogDeletedMessages), "un1", user2);
                        }
                    } else {
                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) {
                            long j = ((TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) tLRPC$ChannelAdminLogEventAction).new_value;
                            long j2 = ((TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) tLRPC$ChannelAdminLogEventAction).prev_value;
                            tLRPC$Chat3 = tLRPC$Chat;
                            if (tLRPC$Chat3.megagroup) {
                                if (j == 0) {
                                    TLRPC$Chat chat7 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j2));
                                    CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString("EventLogRemovedLinkedChannel", R.string.EventLogRemovedLinkedChannel), "un1", user2);
                                    messageObject.messageText = replaceWithLink2;
                                    messageObject.messageText = replaceWithLink(replaceWithLink2, "un2", chat7);
                                } else {
                                    TLRPC$Chat chat8 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j));
                                    CharSequence replaceWithLink3 = replaceWithLink(LocaleController.getString("EventLogChangedLinkedChannel", R.string.EventLogChangedLinkedChannel), "un1", user2);
                                    messageObject.messageText = replaceWithLink3;
                                    messageObject.messageText = replaceWithLink(replaceWithLink3, "un2", chat8);
                                }
                            } else if (j == 0) {
                                TLRPC$Chat chat9 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j2));
                                CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString("EventLogRemovedLinkedGroup", R.string.EventLogRemovedLinkedGroup), "un1", user2);
                                messageObject.messageText = replaceWithLink4;
                                messageObject.messageText = replaceWithLink(replaceWithLink4, "un2", chat9);
                            } else {
                                TLRPC$Chat chat10 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j));
                                CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString("EventLogChangedLinkedGroup", R.string.EventLogChangedLinkedGroup), "un1", user2);
                                messageObject.messageText = replaceWithLink5;
                                messageObject.messageText = replaceWithLink(replaceWithLink5, "un2", chat10);
                            }
                        } else {
                            tLRPC$Chat3 = tLRPC$Chat;
                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) {
                                if (((TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) tLRPC$ChannelAdminLogEventAction).new_value) {
                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOff", R.string.EventLogToggledInvitesHistoryOff), "un1", user2);
                                } else {
                                    messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOn", R.string.EventLogToggledInvitesHistoryOn), "un1", user2);
                                }
                            } else {
                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAbout) {
                                    if (tLRPC$Chat3.megagroup) {
                                        i8 = R.string.EventLogEditedGroupDescription;
                                        str8 = "EventLogEditedGroupDescription";
                                    } else {
                                        i8 = R.string.EventLogEditedChannelDescription;
                                        str8 = "EventLogEditedChannelDescription";
                                    }
                                    messageObject.messageText = replaceWithLink(LocaleController.getString(str8, i8), "un1", user2);
                                    tLRPC$Message = new TLRPC$TL_message();
                                    tLRPC$Message.out = false;
                                    tLRPC$Message.unread = false;
                                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                                    tLRPC$Message.from_id = tLRPC$TL_peerUser;
                                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                    tLRPC$TL_peerUser.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                    tLRPC$Message.peer_id = tLRPC$TL_peerChannel;
                                    tLRPC$Message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                    TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction2 = tLRPC$TL_channelAdminLogEvent2.action;
                                    tLRPC$Message.message = ((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$ChannelAdminLogEventAction2).new_value;
                                    if (!TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$ChannelAdminLogEventAction2).prev_value)) {
                                        TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage = new TLRPC$TL_messageMediaWebPage();
                                        tLRPC$Message.media = tLRPC$TL_messageMediaWebPage;
                                        tLRPC$TL_messageMediaWebPage.webpage = new TLRPC$TL_webPage();
                                        TLRPC$WebPage tLRPC$WebPage2 = tLRPC$Message.media.webpage;
                                        tLRPC$WebPage2.flags = 10;
                                        str3 = str;
                                        tLRPC$WebPage2.display_url = str3;
                                        tLRPC$WebPage2.url = str3;
                                        tLRPC$WebPage2.site_name = LocaleController.getString("EventLogPreviousGroupDescription", R.string.EventLogPreviousGroupDescription);
                                        tLRPC$Message.media.webpage.description = ((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                    } else {
                                        str3 = str;
                                        tLRPC$Message.media = new TLRPC$TL_messageMediaEmpty();
                                    }
                                } else {
                                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                    str3 = str;
                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTheme) {
                                        if (tLRPC$Chat3.megagroup) {
                                            i7 = R.string.EventLogEditedGroupTheme;
                                            str7 = "EventLogEditedGroupTheme";
                                        } else {
                                            i7 = R.string.EventLogEditedChannelTheme;
                                            str7 = "EventLogEditedChannelTheme";
                                        }
                                        messageObject.messageText = replaceWithLink(LocaleController.getString(str7, i7), "un1", user2);
                                        tLRPC$Message = new TLRPC$TL_message();
                                        tLRPC$Message.out = false;
                                        tLRPC$Message.unread = false;
                                        TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                                        tLRPC$Message.from_id = tLRPC$TL_peerUser2;
                                        tLRPC$TL_peerUser2.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                        tLRPC$Message.peer_id = tLRPC$TL_peerChannel;
                                        tLRPC$Message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction3 = tLRPC$TL_channelAdminLogEvent2.action;
                                        tLRPC$Message.message = ((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$ChannelAdminLogEventAction3).new_value;
                                        if (!TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$ChannelAdminLogEventAction3).prev_value)) {
                                            TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage2 = new TLRPC$TL_messageMediaWebPage();
                                            tLRPC$Message.media = tLRPC$TL_messageMediaWebPage2;
                                            tLRPC$TL_messageMediaWebPage2.webpage = new TLRPC$TL_webPage();
                                            TLRPC$WebPage tLRPC$WebPage3 = tLRPC$Message.media.webpage;
                                            tLRPC$WebPage3.flags = 10;
                                            tLRPC$WebPage3.display_url = str3;
                                            tLRPC$WebPage3.url = str3;
                                            tLRPC$WebPage3.site_name = LocaleController.getString("EventLogPreviousGroupTheme", R.string.EventLogPreviousGroupTheme);
                                            tLRPC$Message.media.webpage.description = ((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                        } else {
                                            tLRPC$Message.media = new TLRPC$TL_messageMediaEmpty();
                                        }
                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeUsername) {
                                        String str11 = ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$ChannelAdminLogEventAction).new_value;
                                        if (!TextUtils.isEmpty(str11)) {
                                            if (tLRPC$Chat3.megagroup) {
                                                i6 = R.string.EventLogChangedGroupLink;
                                                str6 = "EventLogChangedGroupLink";
                                            } else {
                                                i6 = R.string.EventLogChangedChannelLink;
                                                str6 = "EventLogChangedChannelLink";
                                            }
                                            messageObject.messageText = replaceWithLink(LocaleController.getString(str6, i6), "un1", user2);
                                        } else {
                                            if (tLRPC$Chat3.megagroup) {
                                                i5 = R.string.EventLogRemovedGroupLink;
                                                str5 = "EventLogRemovedGroupLink";
                                            } else {
                                                i5 = R.string.EventLogRemovedChannelLink;
                                                str5 = "EventLogRemovedChannelLink";
                                            }
                                            messageObject.messageText = replaceWithLink(LocaleController.getString(str5, i5), "un1", user2);
                                        }
                                        TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                                        tLRPC$TL_message.out = false;
                                        tLRPC$TL_message.unread = false;
                                        TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                                        tLRPC$TL_message.from_id = tLRPC$TL_peerUser3;
                                        tLRPC$TL_peerUser3.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                        tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                        tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                        if (!TextUtils.isEmpty(str11)) {
                                            tLRPC$TL_message.message = "https://" + MessagesController.getInstance(messageObject.currentAccount).linkPrefix + "/" + str11;
                                        } else {
                                            tLRPC$TL_message.message = str3;
                                        }
                                        TLRPC$TL_messageEntityUrl tLRPC$TL_messageEntityUrl = new TLRPC$TL_messageEntityUrl();
                                        tLRPC$TL_messageEntityUrl.offset = 0;
                                        tLRPC$TL_messageEntityUrl.length = tLRPC$TL_message.message.length();
                                        tLRPC$TL_message.entities.add(tLRPC$TL_messageEntityUrl);
                                        if (!TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$TL_channelAdminLogEvent2.action).prev_value)) {
                                            TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage3 = new TLRPC$TL_messageMediaWebPage();
                                            tLRPC$TL_message.media = tLRPC$TL_messageMediaWebPage3;
                                            tLRPC$TL_messageMediaWebPage3.webpage = new TLRPC$TL_webPage();
                                            TLRPC$WebPage tLRPC$WebPage4 = tLRPC$TL_message.media.webpage;
                                            tLRPC$WebPage4.flags = 10;
                                            tLRPC$WebPage4.display_url = str3;
                                            tLRPC$WebPage4.url = str3;
                                            tLRPC$WebPage4.site_name = LocaleController.getString("EventLogPreviousLink", R.string.EventLogPreviousLink);
                                            tLRPC$TL_message.media.webpage.description = "https://" + MessagesController.getInstance(messageObject.currentAccount).linkPrefix + "/" + ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                        } else {
                                            tLRPC$TL_message.media = new TLRPC$TL_messageMediaEmpty();
                                        }
                                        tLRPC$Message = tLRPC$TL_message;
                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionEditMessage) {
                                        tLRPC$Message = new TLRPC$TL_message();
                                        tLRPC$Message.out = false;
                                        tLRPC$Message.unread = false;
                                        tLRPC$Message.peer_id = tLRPC$TL_peerChannel;
                                        tLRPC$Message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction4 = tLRPC$TL_channelAdminLogEvent2.action;
                                        TLRPC$Message tLRPC$Message3 = ((TLRPC$TL_channelAdminLogEventActionEditMessage) tLRPC$ChannelAdminLogEventAction4).new_message;
                                        TLRPC$Message tLRPC$Message4 = ((TLRPC$TL_channelAdminLogEventActionEditMessage) tLRPC$ChannelAdminLogEventAction4).prev_message;
                                        if (tLRPC$Message3 != null && (tLRPC$Peer = tLRPC$Message3.from_id) != null) {
                                            tLRPC$Message.from_id = tLRPC$Peer;
                                        } else {
                                            TLRPC$TL_peerUser tLRPC$TL_peerUser4 = new TLRPC$TL_peerUser();
                                            tLRPC$Message.from_id = tLRPC$TL_peerUser4;
                                            tLRPC$TL_peerUser4.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                        }
                                        if (getMedia(tLRPC$Message3) != null && !(getMedia(tLRPC$Message3) instanceof TLRPC$TL_messageMediaEmpty) && !(getMedia(tLRPC$Message3) instanceof TLRPC$TL_messageMediaWebPage)) {
                                            boolean z5 = !TextUtils.equals(tLRPC$Message3.message, tLRPC$Message4.message);
                                            if (((getMedia(tLRPC$Message3).getClass() == tLRPC$Message4.media.getClass() && (getMedia(tLRPC$Message3).photo == null || tLRPC$Message4.media.photo == null || getMedia(tLRPC$Message3).photo.id == tLRPC$Message4.media.photo.id) && (getMedia(tLRPC$Message3).document == null || tLRPC$Message4.media.document == null || getMedia(tLRPC$Message3).document.id == tLRPC$Message4.media.document.id)) ? false : true) && z5) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMediaCaption", R.string.EventLogEditedMediaCaption), "un1", user2);
                                            } else if (z5) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedCaption", R.string.EventLogEditedCaption), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMedia", R.string.EventLogEditedMedia), "un1", user2);
                                            }
                                            TLRPC$MessageMedia media = getMedia(tLRPC$Message3);
                                            tLRPC$Message.media = media;
                                            if (z5) {
                                                media.webpage = new TLRPC$TL_webPage();
                                                tLRPC$Message.media.webpage.site_name = LocaleController.getString("EventLogOriginalCaption", R.string.EventLogOriginalCaption);
                                                if (TextUtils.isEmpty(tLRPC$Message4.message)) {
                                                    tLRPC$Message.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", R.string.EventLogOriginalCaptionEmpty);
                                                } else {
                                                    tLRPC$Message.media.webpage.description = tLRPC$Message4.message;
                                                    arrayList2 = tLRPC$Message4.entities;
                                                    tLRPC$Message.reply_markup = tLRPC$Message3.reply_markup;
                                                    tLRPC$WebPage = tLRPC$Message.media.webpage;
                                                    if (tLRPC$WebPage != null) {
                                                    }
                                                    str = str3;
                                                    arrayList3 = arrayList2;
                                                    tLRPC$Chat2 = tLRPC$Chat3;
                                                }
                                            }
                                            arrayList2 = null;
                                            tLRPC$Message.reply_markup = tLRPC$Message3.reply_markup;
                                            tLRPC$WebPage = tLRPC$Message.media.webpage;
                                            if (tLRPC$WebPage != null) {
                                            }
                                            str = str3;
                                            arrayList3 = arrayList2;
                                            tLRPC$Chat2 = tLRPC$Chat3;
                                        } else {
                                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMessages", R.string.EventLogEditedMessages), "un1", user2);
                                            if (tLRPC$Message3.action instanceof TLRPC$TL_messageActionGroupCall) {
                                                tLRPC$Message3.media = new TLRPC$TL_messageMediaEmpty();
                                                tLRPC$Message = tLRPC$Message3;
                                            } else {
                                                tLRPC$Message.message = tLRPC$Message3.message;
                                                tLRPC$Message.entities = tLRPC$Message3.entities;
                                                TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage4 = new TLRPC$TL_messageMediaWebPage();
                                                tLRPC$Message.media = tLRPC$TL_messageMediaWebPage4;
                                                tLRPC$TL_messageMediaWebPage4.webpage = new TLRPC$TL_webPage();
                                                tLRPC$Message.media.webpage.site_name = LocaleController.getString("EventLogOriginalMessages", R.string.EventLogOriginalMessages);
                                                if (TextUtils.isEmpty(tLRPC$Message4.message)) {
                                                    tLRPC$Message.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", R.string.EventLogOriginalCaptionEmpty);
                                                } else {
                                                    tLRPC$Message.media.webpage.description = tLRPC$Message4.message;
                                                    arrayList2 = tLRPC$Message4.entities;
                                                    tLRPC$Message.reply_markup = tLRPC$Message3.reply_markup;
                                                    tLRPC$WebPage = tLRPC$Message.media.webpage;
                                                    if (tLRPC$WebPage != null) {
                                                        tLRPC$WebPage.flags = 10;
                                                        tLRPC$WebPage.display_url = str3;
                                                        tLRPC$WebPage.url = str3;
                                                    }
                                                    str = str3;
                                                    arrayList3 = arrayList2;
                                                    tLRPC$Chat2 = tLRPC$Chat3;
                                                }
                                            }
                                            arrayList2 = null;
                                            tLRPC$Message.reply_markup = tLRPC$Message3.reply_markup;
                                            tLRPC$WebPage = tLRPC$Message.media.webpage;
                                            if (tLRPC$WebPage != null) {
                                            }
                                            str = str3;
                                            arrayList3 = arrayList2;
                                            tLRPC$Chat2 = tLRPC$Chat3;
                                        }
                                        if (messageObject.messageOwner == null) {
                                            messageObject.messageOwner = new TLRPC$TL_messageService();
                                        }
                                        messageObject.messageOwner.message = messageObject.messageText.toString();
                                        messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                        TLRPC$Message tLRPC$Message5 = messageObject.messageOwner;
                                        tLRPC$Message5.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                        tLRPC$Message5.date = tLRPC$TL_channelAdminLogEvent2.date;
                                        int i21 = iArr[0];
                                        iArr[0] = i21 + 1;
                                        tLRPC$Message5.id = i21;
                                        messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                        tLRPC$Message5.out = false;
                                        tLRPC$Message5.peer_id = new TLRPC$TL_peerChannel();
                                        TLRPC$Message tLRPC$Message6 = messageObject.messageOwner;
                                        tLRPC$Message6.peer_id.channel_id = tLRPC$Chat2.id;
                                        tLRPC$Message6.unread = false;
                                        MediaController mediaController = MediaController.getInstance();
                                        messageObject.isOutOwnerCached = null;
                                        tLRPC$Message = tLRPC$Message instanceof TLRPC$TL_messageEmpty ? null : tLRPC$Message;
                                        if (tLRPC$Message != null) {
                                            tLRPC$Message.out = false;
                                            tLRPC$Message.realId = tLRPC$Message.id;
                                            int i22 = iArr[0];
                                            iArr[0] = i22 + 1;
                                            tLRPC$Message.id = i22;
                                            int i23 = tLRPC$Message.flags & (-9);
                                            tLRPC$Message.flags = i23;
                                            tLRPC$Message.reply_to = null;
                                            tLRPC$Message.flags = i23 & (-32769);
                                            MessageObject messageObject2 = new MessageObject(messageObject.currentAccount, tLRPC$Message, (AbstractMap<Long, TLRPC$User>) null, (AbstractMap<Long, TLRPC$Chat>) null, true, true, messageObject.eventId);
                                            messageObject2.currentEvent = tLRPC$TL_channelAdminLogEvent2;
                                            if (messageObject2.contentType >= 0) {
                                                if (mediaController.isPlayingMessage(messageObject2)) {
                                                    MessageObject playingMessageObject = mediaController.getPlayingMessageObject();
                                                    messageObject2.audioProgress = playingMessageObject.audioProgress;
                                                    messageObject2.audioProgressSec = playingMessageObject.audioProgressSec;
                                                }
                                                createDateArray(messageObject.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                                if (z) {
                                                    arrayList4 = arrayList;
                                                    arrayList4.add(0, messageObject2);
                                                } else {
                                                    arrayList4 = arrayList;
                                                    arrayList4.add(arrayList.size() - 1, messageObject2);
                                                }
                                            } else {
                                                arrayList4 = arrayList;
                                                messageObject.contentType = -1;
                                            }
                                            if (arrayList3 != null) {
                                                messageObject2.webPageDescriptionEntities = arrayList3;
                                                iArr2 = null;
                                                messageObject2.linkDescription = null;
                                                messageObject2.generateLinkDescription();
                                                if (messageObject.contentType < 0) {
                                                    createDateArray(messageObject.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                                    if (z) {
                                                        arrayList4.add(0, messageObject);
                                                    } else {
                                                        arrayList4.add(arrayList.size() - 1, messageObject);
                                                    }
                                                    if (messageObject.messageText == null) {
                                                        messageObject.messageText = str;
                                                    }
                                                    if (getMedia(messageObject.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                                                        textPaint = Theme.chat_msgGameTextPaint;
                                                    } else {
                                                        textPaint = Theme.chat_msgTextPaint;
                                                    }
                                                    int[] iArr3 = allowsBigEmoji() ? new int[1] : iArr2;
                                                    CharSequence replaceEmoji = Emoji.replaceEmoji(messageObject.messageText, textPaint.getFontMetricsInt(), false, iArr3);
                                                    messageObject.messageText = replaceEmoji;
                                                    Spannable replaceAnimatedEmoji = messageObject.replaceAnimatedEmoji(replaceEmoji, textPaint.getFontMetricsInt());
                                                    messageObject.messageText = replaceAnimatedEmoji;
                                                    if (iArr3 != null && iArr3[0] > 1) {
                                                        messageObject.replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr3);
                                                    }
                                                    messageObject.checkEmojiOnly(iArr3);
                                                    setType();
                                                    measureInlineBotButtons();
                                                    generateCaption();
                                                    if (mediaController.isPlayingMessage(messageObject)) {
                                                        MessageObject playingMessageObject2 = mediaController.getPlayingMessageObject();
                                                        messageObject.audioProgress = playingMessageObject2.audioProgress;
                                                        messageObject.audioProgressSec = playingMessageObject2.audioProgressSec;
                                                    }
                                                    messageObject.generateLayout(user2);
                                                    messageObject.layoutCreated = true;
                                                    messageObject.generateThumbs(false);
                                                    checkMediaExistance();
                                                    return;
                                                }
                                                return;
                                            }
                                        } else {
                                            arrayList4 = arrayList;
                                        }
                                        iArr2 = null;
                                        if (messageObject.contentType < 0) {
                                        }
                                    } else {
                                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeEmojiStickerSet) {
                                            TLRPC$InputStickerSet tLRPC$InputStickerSet = ((TLRPC$TL_channelAdminLogEventActionChangeEmojiStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                            TLRPC$InputStickerSet tLRPC$InputStickerSet2 = ((TLRPC$TL_channelAdminLogEventActionChangeEmojiStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                            if (tLRPC$InputStickerSet == null || (tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedEmojiPack", R.string.EventLogRemovedEmojiPack), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogChangedEmojiPack", R.string.EventLogChangedEmojiPack), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeStickerSet) {
                                            TLRPC$InputStickerSet tLRPC$InputStickerSet3 = ((TLRPC$TL_channelAdminLogEventActionChangeStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                            TLRPC$InputStickerSet tLRPC$InputStickerSet4 = ((TLRPC$TL_channelAdminLogEventActionChangeStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                            if (tLRPC$InputStickerSet3 == null || (tLRPC$InputStickerSet3 instanceof TLRPC$TL_inputStickerSetEmpty)) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedStickersSet", R.string.EventLogRemovedStickersSet), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogChangedStickersSet", R.string.EventLogChangedStickersSet), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLocation) {
                                            TLRPC$ChannelLocation tLRPC$ChannelLocation = ((TLRPC$TL_channelAdminLogEventActionChangeLocation) tLRPC$ChannelAdminLogEventAction).new_value;
                                            if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocationEmpty) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedLocation", R.string.EventLogRemovedLocation), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogChangedLocation", R.string.EventLogChangedLocation, ((TLRPC$TL_channelLocation) tLRPC$ChannelLocation).address), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSlowMode) {
                                            int i24 = ((TLRPC$TL_channelAdminLogEventActionToggleSlowMode) tLRPC$ChannelAdminLogEventAction).new_value;
                                            if (i24 == 0) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSlowmodeOff", R.string.EventLogToggledSlowmodeOff), "un1", user2);
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
                                                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogToggledSlowmodeOn", i25, objArr), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStartGroupCall) {
                                            if (ChatObject.isChannel(tLRPC$Chat) && (!tLRPC$Chat3.megagroup || tLRPC$Chat3.gigagroup)) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogStartedLiveStream", R.string.EventLogStartedLiveStream), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogStartedVoiceChat", R.string.EventLogStartedVoiceChat), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDiscardGroupCall) {
                                            if (ChatObject.isChannel(tLRPC$Chat) && (!tLRPC$Chat3.megagroup || tLRPC$Chat3.gigagroup)) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEndedLiveStream", R.string.EventLogEndedLiveStream), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogEndedVoiceChat", R.string.EventLogEndedVoiceChat), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantMute) {
                                            long peerId4 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantMute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                            if (peerId4 > 0) {
                                                chat3 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId4));
                                            } else {
                                                chat3 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId4));
                                            }
                                            CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString("EventLogVoiceChatMuted", R.string.EventLogVoiceChatMuted), "un1", user2);
                                            messageObject.messageText = replaceWithLink6;
                                            messageObject.messageText = replaceWithLink(replaceWithLink6, "un2", chat3);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantUnmute) {
                                            long peerId5 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantUnmute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                            if (peerId5 > 0) {
                                                chat2 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId5));
                                            } else {
                                                chat2 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId5));
                                            }
                                            CharSequence replaceWithLink7 = replaceWithLink(LocaleController.getString("EventLogVoiceChatUnmuted", R.string.EventLogVoiceChatUnmuted), "un1", user2);
                                            messageObject.messageText = replaceWithLink7;
                                            messageObject.messageText = replaceWithLink(replaceWithLink7, "un2", chat2);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) {
                                            if (((TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) tLRPC$ChannelAdminLogEventAction).join_muted) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogVoiceChatNotAllowedToSpeak", R.string.EventLogVoiceChatNotAllowedToSpeak), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogVoiceChatAllowedToSpeak", R.string.EventLogVoiceChatAllowedToSpeak), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) {
                                            if (((TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) tLRPC$ChannelAdminLogEventAction).via_chatlist) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("ActionInviteUserFolder", R.string.ActionInviteUserFolder), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("ActionInviteUser", R.string.ActionInviteUser), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleNoForwards) {
                                            TLRPC$TL_channelAdminLogEventActionToggleNoForwards tLRPC$TL_channelAdminLogEventActionToggleNoForwards = (TLRPC$TL_channelAdminLogEventActionToggleNoForwards) tLRPC$ChannelAdminLogEventAction;
                                            boolean z6 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat3.megagroup;
                                            if (tLRPC$TL_channelAdminLogEventActionToggleNoForwards.new_value) {
                                                if (z6) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("ActionForwardsRestrictedChannel", R.string.ActionForwardsRestrictedChannel), "un1", user2);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("ActionForwardsRestrictedGroup", R.string.ActionForwardsRestrictedGroup), "un1", user2);
                                                }
                                            } else if (z6) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("ActionForwardsEnabledChannel", R.string.ActionForwardsEnabledChannel), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString("ActionForwardsEnabledGroup", R.string.ActionForwardsEnabledGroup), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteDelete) {
                                            CharSequence replaceWithLink8 = replaceWithLink(LocaleController.formatString("ActionDeletedInviteLinkClickable", R.string.ActionDeletedInviteLinkClickable, new Object[0]), "un1", user2);
                                            messageObject.messageText = replaceWithLink8;
                                            messageObject.messageText = replaceWithLink(replaceWithLink8, "un2", ((TLRPC$TL_channelAdminLogEventActionExportedInviteDelete) tLRPC$ChannelAdminLogEventAction).invite);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke) {
                                            TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke = (TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke) tLRPC$ChannelAdminLogEventAction;
                                            CharSequence replaceWithLink9 = replaceWithLink(LocaleController.formatString("ActionRevokedInviteLinkClickable", R.string.ActionRevokedInviteLinkClickable, tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke.invite.link), "un1", user2);
                                            messageObject.messageText = replaceWithLink9;
                                            messageObject.messageText = replaceWithLink(replaceWithLink9, "un2", tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke.invite);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteEdit) {
                                            TLRPC$TL_channelAdminLogEventActionExportedInviteEdit tLRPC$TL_channelAdminLogEventActionExportedInviteEdit = (TLRPC$TL_channelAdminLogEventActionExportedInviteEdit) tLRPC$ChannelAdminLogEventAction;
                                            String str12 = tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite.link;
                                            if (str12 != null && str12.equals(tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite.link)) {
                                                messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkToSameClickable", R.string.ActionEditedInviteLinkToSameClickable, new Object[0]), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkClickable", R.string.ActionEditedInviteLinkClickable, new Object[0]), "un1", user2);
                                            }
                                            CharSequence replaceWithLink10 = replaceWithLink(messageObject.messageText, "un2", tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite);
                                            messageObject.messageText = replaceWithLink10;
                                            messageObject.messageText = replaceWithLink(replaceWithLink10, "un3", tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantVolume) {
                                            TLRPC$TL_channelAdminLogEventActionParticipantVolume tLRPC$TL_channelAdminLogEventActionParticipantVolume = (TLRPC$TL_channelAdminLogEventActionParticipantVolume) tLRPC$ChannelAdminLogEventAction;
                                            long peerId6 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant.peer);
                                            if (peerId6 > 0) {
                                                chat = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId6));
                                            } else {
                                                chat = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId6));
                                            }
                                            double participantVolume = ChatObject.getParticipantVolume(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant);
                                            Double.isNaN(participantVolume);
                                            double d = participantVolume / 100.0d;
                                            int i26 = R.string.ActionVolumeChanged;
                                            Object[] objArr2 = new Object[1];
                                            objArr2[0] = Integer.valueOf((int) (d > 0.0d ? Math.max(d, 1.0d) : 0.0d));
                                            CharSequence replaceWithLink11 = replaceWithLink(LocaleController.formatString("ActionVolumeChanged", i26, objArr2), "un1", user2);
                                            messageObject.messageText = replaceWithLink11;
                                            messageObject.messageText = replaceWithLink(replaceWithLink11, "un2", chat);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) {
                                            TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL = (TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) tLRPC$ChannelAdminLogEventAction;
                                            if (!tLRPC$Chat3.megagroup) {
                                                int i27 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                if (i27 != 0) {
                                                    messageObject.messageText = LocaleController.formatString("ActionTTLChannelChanged", R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i27));
                                                } else {
                                                    messageObject.messageText = LocaleController.getString("ActionTTLChannelDisabled", R.string.ActionTTLChannelDisabled);
                                                }
                                            } else {
                                                int i28 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                if (i28 == 0) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString("ActionTTLDisabled", R.string.ActionTTLDisabled), "un1", user2);
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
                                                    messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", i29, objArr3), "un1", user2);
                                                }
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) {
                                            TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest = (TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) tLRPC$ChannelAdminLogEventAction;
                                            TLRPC$ExportedChatInvite tLRPC$ExportedChatInvite = tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite;
                                            if (((tLRPC$ExportedChatInvite instanceof TLRPC$TL_chatInviteExported) && "https://t.me/+PublicChat".equals(((TLRPC$TL_chatInviteExported) tLRPC$ExportedChatInvite).link)) || (tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite instanceof TLRPC$TL_chatInvitePublicJoinRequests)) {
                                                CharSequence replaceWithLink12 = replaceWithLink(LocaleController.getString("JoinedViaRequestApproved", R.string.JoinedViaRequestApproved), "un1", user2);
                                                messageObject.messageText = replaceWithLink12;
                                                messageObject.messageText = replaceWithLink(replaceWithLink12, "un2", MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by)));
                                            } else {
                                                CharSequence replaceWithLink13 = replaceWithLink(LocaleController.getString("JoinedViaInviteLinkApproved", R.string.JoinedViaInviteLinkApproved), "un1", user2);
                                                messageObject.messageText = replaceWithLink13;
                                                CharSequence replaceWithLink14 = replaceWithLink(replaceWithLink13, "un2", tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite);
                                                messageObject.messageText = replaceWithLink14;
                                                messageObject.messageText = replaceWithLink(replaceWithLink14, "un3", MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by)));
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionSendMessage) {
                                            tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionSendMessage) tLRPC$ChannelAdminLogEventAction).message;
                                            messageObject.messageText = replaceWithLink(LocaleController.getString("EventLogSendMessages", R.string.EventLogSendMessages), "un1", user2);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) {
                                            TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions = (TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) tLRPC$ChannelAdminLogEventAction;
                                            boolean z7 = (tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.prev_value instanceof TLRPC$TL_chatReactionsSome) && (tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.new_value instanceof TLRPC$TL_chatReactionsSome);
                                            CharSequence stringFrom = messageObject.getStringFrom(tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.new_value);
                                            if (z7) {
                                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(replaceWithLink(LocaleController.formatString("ActionReactionsChangedList", R.string.ActionReactionsChangedList, "**new**"), "un1", user2));
                                                int indexOf = spannableStringBuilder.toString().indexOf("**new**");
                                                if (indexOf > 0) {
                                                    spannableStringBuilder.replace(indexOf, indexOf + 7, stringFrom);
                                                }
                                                messageObject.messageText = spannableStringBuilder;
                                            } else {
                                                CharSequence stringFrom2 = messageObject.getStringFrom(tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.prev_value);
                                                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(replaceWithLink(LocaleController.formatString("ActionReactionsChanged", R.string.ActionReactionsChanged, "**old**", "**new**"), "un1", user2));
                                                int indexOf2 = spannableStringBuilder2.toString().indexOf("**old**");
                                                if (indexOf2 > 0) {
                                                    spannableStringBuilder2.replace(indexOf2, indexOf2 + 7, stringFrom2);
                                                }
                                                int indexOf3 = spannableStringBuilder2.toString().indexOf("**new**");
                                                if (indexOf3 > 0) {
                                                    spannableStringBuilder2.replace(indexOf3, indexOf3 + 7, stringFrom);
                                                }
                                                messageObject.messageText = spannableStringBuilder2;
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeUsernames) {
                                            TLRPC$TL_channelAdminLogEventActionChangeUsernames tLRPC$TL_channelAdminLogEventActionChangeUsernames = (TLRPC$TL_channelAdminLogEventActionChangeUsernames) tLRPC$ChannelAdminLogEventAction;
                                            ArrayList<String> arrayList5 = tLRPC$TL_channelAdminLogEventActionChangeUsernames.prev_value;
                                            ArrayList<String> arrayList6 = tLRPC$TL_channelAdminLogEventActionChangeUsernames.new_value;
                                            messageObject.messageText = null;
                                            if (arrayList5 != null && arrayList6 != null) {
                                                if (arrayList6.size() + 1 == arrayList5.size()) {
                                                    String str13 = null;
                                                    int i30 = 0;
                                                    while (true) {
                                                        if (i30 >= arrayList5.size()) {
                                                            break;
                                                        }
                                                        String str14 = arrayList5.get(i30);
                                                        if (!arrayList6.contains(str14)) {
                                                            if (str13 != null) {
                                                                str13 = null;
                                                                break;
                                                            }
                                                            str13 = str14;
                                                        }
                                                        i30++;
                                                    }
                                                    if (str13 != null) {
                                                        messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogDeactivatedUsername", R.string.EventLogDeactivatedUsername, "@" + str13), "un1", user2);
                                                    }
                                                } else if (arrayList5.size() + 1 == arrayList6.size()) {
                                                    String str15 = null;
                                                    int i31 = 0;
                                                    while (true) {
                                                        if (i31 >= arrayList6.size()) {
                                                            break;
                                                        }
                                                        String str16 = arrayList6.get(i31);
                                                        if (!arrayList5.contains(str16)) {
                                                            if (str15 != null) {
                                                                str15 = null;
                                                                break;
                                                            }
                                                            str15 = str16;
                                                        }
                                                        i31++;
                                                    }
                                                    if (str15 != null) {
                                                        messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogActivatedUsername", R.string.EventLogActivatedUsername, "@" + str15), "un1", user2);
                                                    }
                                                }
                                            }
                                            if (messageObject.messageText == null) {
                                                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogChangeUsernames", R.string.EventLogChangeUsernames, messageObject.getUsernamesString(arrayList5), messageObject.getUsernamesString(arrayList6)), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleForum) {
                                            if (((TLRPC$TL_channelAdminLogEventActionToggleForum) tLRPC$ChannelAdminLogEventAction).new_value) {
                                                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogSwitchToForum", R.string.EventLogSwitchToForum, new Object[0]), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogSwitchToGroup", R.string.EventLogSwitchToGroup, new Object[0]), "un1", user2);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionCreateTopic) {
                                            CharSequence replaceWithLink15 = replaceWithLink(LocaleController.formatString("EventLogCreateTopic", R.string.EventLogCreateTopic, new Object[0]), "un1", user2);
                                            messageObject.messageText = replaceWithLink15;
                                            messageObject.messageText = replaceWithLink(replaceWithLink15, "un2", ((TLRPC$TL_channelAdminLogEventActionCreateTopic) tLRPC$ChannelAdminLogEventAction).topic);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionEditTopic) {
                                            TLRPC$TL_channelAdminLogEventActionEditTopic tLRPC$TL_channelAdminLogEventActionEditTopic = (TLRPC$TL_channelAdminLogEventActionEditTopic) tLRPC$ChannelAdminLogEventAction;
                                            TLRPC$ForumTopic tLRPC$ForumTopic = tLRPC$TL_channelAdminLogEventActionEditTopic.prev_topic;
                                            if (tLRPC$ForumTopic instanceof TLRPC$TL_forumTopic) {
                                                TLRPC$ForumTopic tLRPC$ForumTopic2 = tLRPC$TL_channelAdminLogEventActionEditTopic.new_topic;
                                                if ((tLRPC$ForumTopic2 instanceof TLRPC$TL_forumTopic) && ((TLRPC$TL_forumTopic) tLRPC$ForumTopic).hidden != ((TLRPC$TL_forumTopic) tLRPC$ForumTopic2).hidden) {
                                                    if (((TLRPC$TL_forumTopic) tLRPC$ForumTopic2).hidden) {
                                                        i4 = R.string.TopicHidden2;
                                                        str4 = "TopicHidden2";
                                                    } else {
                                                        i4 = R.string.TopicShown2;
                                                        str4 = "TopicShown2";
                                                    }
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(str4, i4), "%s", user2);
                                                }
                                            }
                                            CharSequence replaceWithLink16 = replaceWithLink(LocaleController.getString("EventLogEditTopic", R.string.EventLogEditTopic), "un1", user2);
                                            messageObject.messageText = replaceWithLink16;
                                            CharSequence replaceWithLink17 = replaceWithLink(replaceWithLink16, "un2", tLRPC$TL_channelAdminLogEventActionEditTopic.prev_topic);
                                            messageObject.messageText = replaceWithLink17;
                                            messageObject.messageText = replaceWithLink(replaceWithLink17, "un3", tLRPC$TL_channelAdminLogEventActionEditTopic.new_topic);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDeleteTopic) {
                                            CharSequence replaceWithLink18 = replaceWithLink(LocaleController.getString("EventLogDeleteTopic", R.string.EventLogDeleteTopic), "un1", user2);
                                            messageObject.messageText = replaceWithLink18;
                                            messageObject.messageText = replaceWithLink(replaceWithLink18, "un2", ((TLRPC$TL_channelAdminLogEventActionDeleteTopic) tLRPC$ChannelAdminLogEventAction).topic);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionPinTopic) {
                                            TLRPC$TL_channelAdminLogEventActionPinTopic tLRPC$TL_channelAdminLogEventActionPinTopic = (TLRPC$TL_channelAdminLogEventActionPinTopic) tLRPC$ChannelAdminLogEventAction;
                                            TLRPC$ForumTopic tLRPC$ForumTopic3 = tLRPC$TL_channelAdminLogEventActionPinTopic.new_topic;
                                            if ((tLRPC$ForumTopic3 instanceof TLRPC$TL_forumTopic) && ((TLRPC$TL_forumTopic) tLRPC$ForumTopic3).pinned) {
                                                CharSequence replaceWithLink19 = replaceWithLink(LocaleController.formatString("EventLogPinTopic", R.string.EventLogPinTopic, new Object[0]), "un1", user2);
                                                messageObject.messageText = replaceWithLink19;
                                                messageObject.messageText = replaceWithLink(replaceWithLink19, "un2", tLRPC$TL_channelAdminLogEventActionPinTopic.new_topic);
                                            } else {
                                                CharSequence replaceWithLink20 = replaceWithLink(LocaleController.formatString("EventLogUnpinTopic", R.string.EventLogUnpinTopic, new Object[0]), "un1", user2);
                                                messageObject.messageText = replaceWithLink20;
                                                messageObject.messageText = replaceWithLink(replaceWithLink20, "un2", tLRPC$TL_channelAdminLogEventActionPinTopic.new_topic);
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleAntiSpam) {
                                            if (((TLRPC$TL_channelAdminLogEventActionToggleAntiSpam) tLRPC$ChannelAdminLogEventAction).new_value) {
                                                string2 = LocaleController.getString("EventLogEnabledAntiSpam", R.string.EventLogEnabledAntiSpam);
                                            } else {
                                                string2 = LocaleController.getString("EventLogDisabledAntiSpam", R.string.EventLogDisabledAntiSpam);
                                            }
                                            messageObject.messageText = replaceWithLink(string2, "un1", user2);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeColor) {
                                            boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                            TLRPC$TL_channelAdminLogEventActionChangeColor tLRPC$TL_channelAdminLogEventActionChangeColor = (TLRPC$TL_channelAdminLogEventActionChangeColor) tLRPC$TL_channelAdminLogEvent2.action;
                                            messageObject.messageText = replaceWithLink(LocaleController.formatString(isChannelAndNotMegaGroup ? R.string.EventLogChangedColor : R.string.EventLogChangedColorGroup, AvatarDrawable.colorName(tLRPC$TL_channelAdminLogEventActionChangeColor.prev_value).toLowerCase(), AvatarDrawable.colorName(tLRPC$TL_channelAdminLogEventActionChangeColor.new_value).toLowerCase()), "un1", user2);
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangePeerColor) {
                                            boolean isChannelAndNotMegaGroup2 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                            TLRPC$TL_channelAdminLogEventActionChangePeerColor tLRPC$TL_channelAdminLogEventActionChangePeerColor = (TLRPC$TL_channelAdminLogEventActionChangePeerColor) tLRPC$TL_channelAdminLogEvent2.action;
                                            SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(LocaleController.getString(isChannelAndNotMegaGroup2 ? R.string.EventLogChangedPeerColorIcon : R.string.EventLogChangedPeerColorIconGroup));
                                            SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder();
                                            if ((tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.flags & 1) != 0) {
                                                spannableStringBuilder4.append((CharSequence) "c");
                                                str = str3;
                                                spannableStringBuilder4.setSpan(new PeerColorActivity.PeerColorSpan(false, messageObject.currentAccount, tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder4.length() - 1, spannableStringBuilder4.length(), 33);
                                            } else {
                                                str = str3;
                                            }
                                            if ((tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.flags & 2) != 0) {
                                                if (spannableStringBuilder4.length() > 0) {
                                                    spannableStringBuilder4.append((CharSequence) ", ");
                                                }
                                                spannableStringBuilder4.append((CharSequence) "e");
                                                spannableStringBuilder4.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder4.length() - 1, spannableStringBuilder4.length(), 33);
                                            }
                                            if (spannableStringBuilder4.length() == 0) {
                                                spannableStringBuilder4.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                            }
                                            SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder();
                                            if ((tLRPC$TL_channelAdminLogEventActionChangePeerColor.new_value.flags & 1) != 0) {
                                                spannableStringBuilder5.append((CharSequence) "c");
                                                spannableStringBuilder5.setSpan(new PeerColorActivity.PeerColorSpan(false, messageObject.currentAccount, tLRPC$TL_channelAdminLogEventActionChangePeerColor.new_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder5.length() - 1, spannableStringBuilder5.length(), 33);
                                            }
                                            if ((tLRPC$TL_channelAdminLogEventActionChangePeerColor.new_value.flags & 2) != 0) {
                                                if (spannableStringBuilder5.length() > 0) {
                                                    spannableStringBuilder5.append((CharSequence) ", ");
                                                }
                                                spannableStringBuilder5.append((CharSequence) "e");
                                                spannableStringBuilder5.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangePeerColor.new_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder5.length() - 1, spannableStringBuilder5.length(), 33);
                                            }
                                            if (spannableStringBuilder5.length() == 0) {
                                                spannableStringBuilder5.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                            }
                                            messageObject.messageText = replaceWithLink(AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", spannableStringBuilder3, spannableStringBuilder4), spannableStringBuilder5), "un1", user2);
                                        } else {
                                            str = str3;
                                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor) {
                                                boolean isChannelAndNotMegaGroup3 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor = (TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor) tLRPC$TL_channelAdminLogEvent.action;
                                                SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder(LocaleController.getString(isChannelAndNotMegaGroup3 ? R.string.EventLogChangedProfileColorIcon : R.string.EventLogChangedProfileColorIconGroup));
                                                SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder();
                                                if ((tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.flags & 1) != 0) {
                                                    spannableStringBuilder7.append((CharSequence) "c");
                                                    spannableStringBuilder7.setSpan(new PeerColorActivity.PeerColorSpan(true, messageObject.currentAccount, tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder7.length() - 1, spannableStringBuilder7.length(), 33);
                                                }
                                                if ((tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.flags & 2) != 0) {
                                                    if (spannableStringBuilder7.length() > 0) {
                                                        spannableStringBuilder7.append((CharSequence) ", ");
                                                    }
                                                    spannableStringBuilder7.append((CharSequence) "e");
                                                    spannableStringBuilder7.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder7.length() - 1, spannableStringBuilder7.length(), 33);
                                                }
                                                if (spannableStringBuilder7.length() == 0) {
                                                    spannableStringBuilder7.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                                }
                                                SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder();
                                                if ((tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.new_value.flags & 1) != 0) {
                                                    spannableStringBuilder8.append((CharSequence) "c");
                                                    spannableStringBuilder8.setSpan(new PeerColorActivity.PeerColorSpan(true, messageObject.currentAccount, tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.new_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder8.length() - 1, spannableStringBuilder8.length(), 33);
                                                }
                                                if ((tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.new_value.flags & 2) != 0) {
                                                    if (spannableStringBuilder8.length() > 0) {
                                                        spannableStringBuilder8.append((CharSequence) ", ");
                                                    }
                                                    spannableStringBuilder8.append((CharSequence) "e");
                                                    spannableStringBuilder8.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.new_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder8.length() - 1, spannableStringBuilder8.length(), 33);
                                                }
                                                if (spannableStringBuilder8.length() == 0) {
                                                    spannableStringBuilder8.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                                }
                                                messageObject.messageText = replaceWithLink(AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", spannableStringBuilder6, spannableStringBuilder7), spannableStringBuilder8), "un1", user2);
                                            } else {
                                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeEmojiStatus) {
                                                    boolean isChannelAndNotMegaGroup4 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                                    TLRPC$TL_channelAdminLogEventActionChangeEmojiStatus tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus = (TLRPC$TL_channelAdminLogEventActionChangeEmojiStatus) tLRPC$TL_channelAdminLogEvent2.action;
                                                    if (tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.prev_value instanceof TLRPC$TL_emojiStatusEmpty) {
                                                        spannableString3 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                        z2 = true;
                                                    } else {
                                                        spannableString3 = new SpannableString("e");
                                                        spannableString3.setSpan(new AnimatedEmojiSpan(DialogObject.getEmojiStatusDocumentId(tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.prev_value), Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                        z2 = false;
                                                    }
                                                    TLRPC$EmojiStatus tLRPC$EmojiStatus = tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.new_value;
                                                    boolean z8 = tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil;
                                                    if (tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusEmpty) {
                                                        spannableString4 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                    } else {
                                                        spannableString4 = new SpannableString("e");
                                                        spannableString4.setSpan(new AnimatedEmojiSpan(DialogObject.getEmojiStatusDocumentId(tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.new_value), Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                    }
                                                    if (z2) {
                                                        if (z8) {
                                                            i3 = isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFor : R.string.EventLogChangedEmojiStatusForGroup;
                                                        } else {
                                                            i3 = isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatus : R.string.EventLogChangedEmojiStatusGroup;
                                                        }
                                                    } else if (z8) {
                                                        i3 = isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFromFor : R.string.EventLogChangedEmojiStatusFromForGroup;
                                                    } else {
                                                        i3 = isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFrom : R.string.EventLogChangedEmojiStatusFromGroup;
                                                    }
                                                    SpannableStringBuilder replaceCharSequence = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", new SpannableStringBuilder(LocaleController.getString(i3)), spannableString3), spannableString4);
                                                    messageObject.messageText = replaceWithLink(z8 ? AndroidUtilities.replaceCharSequence("%3$s", replaceCharSequence, LocaleController.formatTTLString((int) ((DialogObject.getEmojiStatusUntil(tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.new_value) - tLRPC$TL_channelAdminLogEvent2.date) * 1.05f))) : replaceCharSequence, "un1", user2);
                                                } else {
                                                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeWallpaper) {
                                                        TLRPC$TL_channelAdminLogEventActionChangeWallpaper tLRPC$TL_channelAdminLogEventActionChangeWallpaper = (TLRPC$TL_channelAdminLogEventActionChangeWallpaper) tLRPC$ChannelAdminLogEventAction;
                                                        boolean isChannelAndNotMegaGroup5 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                        TLRPC$WallPaper tLRPC$WallPaper = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value;
                                                        if ((tLRPC$WallPaper instanceof TLRPC$TL_wallPaperNoFile) && tLRPC$WallPaper.id == 0 && tLRPC$WallPaper.settings == null) {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(isChannelAndNotMegaGroup5 ? R.string.EventLogRemovedWallpaper : R.string.EventLogRemovedWallpaperGroup), "un1", user2);
                                                        } else {
                                                            ArrayList<TLRPC$PhotoSize> arrayList7 = new ArrayList<>();
                                                            messageObject.photoThumbs = arrayList7;
                                                            TLRPC$Document tLRPC$Document = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                                                            if (tLRPC$Document != null) {
                                                                arrayList7.addAll(tLRPC$Document.thumbs);
                                                                messageObject.photoThumbsObject = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                                                            }
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(isChannelAndNotMegaGroup5 ? R.string.EventLogChangedWallpaper : R.string.EventLogChangedWallpaperGroup), "un1", user2);
                                                        }
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji) {
                                                        boolean isChannelAndNotMegaGroup6 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                        TLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji = (TLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji) tLRPC$TL_channelAdminLogEvent2.action;
                                                        messageObject.messageText = replaceWithLink(LocaleController.getString(isChannelAndNotMegaGroup6 ? R.string.EventLogChangedEmoji : R.string.EventLogChangedEmojiGroup), "un1", user2);
                                                        if (tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji.prev_value == 0) {
                                                            spannableString = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                        } else {
                                                            spannableString = new SpannableString("e");
                                                            spannableString.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji.prev_value, Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                        }
                                                        messageObject.messageText = AndroidUtilities.replaceCharSequence("%1$s", messageObject.messageText, spannableString);
                                                        if (tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji.new_value == 0) {
                                                            spannableString2 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                        } else {
                                                            spannableString2 = new SpannableString("e");
                                                            spannableString2.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji.new_value, Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                        }
                                                        messageObject.messageText = AndroidUtilities.replaceCharSequence("%2$s", messageObject.messageText, spannableString2);
                                                    } else {
                                                        messageObject.messageText = "unsupported " + tLRPC$TL_channelAdminLogEvent2.action;
                                                    }
                                                }
                                                tLRPC$Chat2 = tLRPC$Chat;
                                            }
                                        }
                                        tLRPC$Chat2 = tLRPC$Chat3;
                                        str = str3;
                                    }
                                }
                                tLRPC$Chat2 = tLRPC$Chat3;
                                str = str3;
                                arrayList3 = null;
                                if (messageObject.messageOwner == null) {
                                }
                                messageObject.messageOwner.message = messageObject.messageText.toString();
                                messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                TLRPC$Message tLRPC$Message52 = messageObject.messageOwner;
                                tLRPC$Message52.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                tLRPC$Message52.date = tLRPC$TL_channelAdminLogEvent2.date;
                                int i212 = iArr[0];
                                iArr[0] = i212 + 1;
                                tLRPC$Message52.id = i212;
                                messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                tLRPC$Message52.out = false;
                                tLRPC$Message52.peer_id = new TLRPC$TL_peerChannel();
                                TLRPC$Message tLRPC$Message62 = messageObject.messageOwner;
                                tLRPC$Message62.peer_id.channel_id = tLRPC$Chat2.id;
                                tLRPC$Message62.unread = false;
                                MediaController mediaController2 = MediaController.getInstance();
                                messageObject.isOutOwnerCached = null;
                                if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
                                }
                                if (tLRPC$Message != null) {
                                }
                                iArr2 = null;
                                if (messageObject.contentType < 0) {
                                }
                            }
                        }
                        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                        tLRPC$Chat2 = tLRPC$Chat3;
                    }
                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                    tLRPC$Chat2 = tLRPC$Chat;
                    arrayList3 = null;
                    if (messageObject.messageOwner == null) {
                    }
                    messageObject.messageOwner.message = messageObject.messageText.toString();
                    messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                    TLRPC$Message tLRPC$Message522 = messageObject.messageOwner;
                    tLRPC$Message522.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                    tLRPC$Message522.date = tLRPC$TL_channelAdminLogEvent2.date;
                    int i2122 = iArr[0];
                    iArr[0] = i2122 + 1;
                    tLRPC$Message522.id = i2122;
                    messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                    tLRPC$Message522.out = false;
                    tLRPC$Message522.peer_id = new TLRPC$TL_peerChannel();
                    TLRPC$Message tLRPC$Message622 = messageObject.messageOwner;
                    tLRPC$Message622.peer_id.channel_id = tLRPC$Chat2.id;
                    tLRPC$Message622.unread = false;
                    MediaController mediaController22 = MediaController.getInstance();
                    messageObject.isOutOwnerCached = null;
                    if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
                    }
                    if (tLRPC$Message != null) {
                    }
                    iArr2 = null;
                    if (messageObject.contentType < 0) {
                    }
                }
                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                tLRPC$Chat2 = tLRPC$Chat;
            }
            tLRPC$Message = null;
            arrayList3 = null;
            if (messageObject.messageOwner == null) {
            }
            messageObject.messageOwner.message = messageObject.messageText.toString();
            messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
            TLRPC$Message tLRPC$Message5222 = messageObject.messageOwner;
            tLRPC$Message5222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
            tLRPC$Message5222.date = tLRPC$TL_channelAdminLogEvent2.date;
            int i21222 = iArr[0];
            iArr[0] = i21222 + 1;
            tLRPC$Message5222.id = i21222;
            messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
            tLRPC$Message5222.out = false;
            tLRPC$Message5222.peer_id = new TLRPC$TL_peerChannel();
            TLRPC$Message tLRPC$Message6222 = messageObject.messageOwner;
            tLRPC$Message6222.peer_id.channel_id = tLRPC$Chat2.id;
            tLRPC$Message6222.unread = false;
            MediaController mediaController222 = MediaController.getInstance();
            messageObject.isOutOwnerCached = null;
            if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
            }
            if (tLRPC$Message != null) {
            }
            iArr2 = null;
            if (messageObject.contentType < 0) {
            }
        }
        tLRPC$Chat2 = tLRPC$Chat;
        str = "";
        tLRPC$Message = null;
        arrayList3 = null;
        if (messageObject.messageOwner == null) {
        }
        messageObject.messageOwner.message = messageObject.messageText.toString();
        messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
        TLRPC$Message tLRPC$Message52222 = messageObject.messageOwner;
        tLRPC$Message52222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
        tLRPC$Message52222.date = tLRPC$TL_channelAdminLogEvent2.date;
        int i212222 = iArr[0];
        iArr[0] = i212222 + 1;
        tLRPC$Message52222.id = i212222;
        messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
        tLRPC$Message52222.out = false;
        tLRPC$Message52222.peer_id = new TLRPC$TL_peerChannel();
        TLRPC$Message tLRPC$Message62222 = messageObject.messageOwner;
        tLRPC$Message62222.peer_id.channel_id = tLRPC$Chat2.id;
        tLRPC$Message62222.unread = false;
        MediaController mediaController2222 = MediaController.getInstance();
        messageObject.isOutOwnerCached = null;
        if (tLRPC$Message instanceof TLRPC$TL_messageEmpty) {
        }
        if (tLRPC$Message != null) {
        }
        iArr2 = null;
        if (messageObject.contentType < 0) {
        }
    }

    public void spoilLoginCode() {
        TLRPC$Message tLRPC$Message;
        if (this.spoiledLoginCode || this.messageText == null || (tLRPC$Message = this.messageOwner) == null || tLRPC$Message.entities == null) {
            return;
        }
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
        if ((tLRPC$Peer instanceof TLRPC$TL_peerUser) && tLRPC$Peer.user_id == 777000) {
            if (loginCodePattern == null) {
                loginCodePattern = Pattern.compile("[\\d\\-]{5,7}");
            }
            try {
                Matcher matcher = loginCodePattern.matcher(this.messageText);
                if (matcher.find()) {
                    TLRPC$TL_messageEntitySpoiler tLRPC$TL_messageEntitySpoiler = new TLRPC$TL_messageEntitySpoiler();
                    tLRPC$TL_messageEntitySpoiler.offset = matcher.start();
                    tLRPC$TL_messageEntitySpoiler.length = matcher.end() - tLRPC$TL_messageEntitySpoiler.offset;
                    this.messageOwner.entities.add(tLRPC$TL_messageEntitySpoiler);
                }
            } catch (Exception e) {
                FileLog.e((Throwable) e, false);
            }
            this.spoiledLoginCode = true;
        }
    }

    public boolean didSpoilLoginCode() {
        return this.spoiledLoginCode;
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
                    spannableStringBuilder.append((CharSequence) " ");
                }
                spannableStringBuilder.append(Emoji.replaceEmoji(ReactionsUtils.reactionToCharSequence(tLRPC$TL_chatReactionsSome.reactions.get(i)), null, false));
            }
            return spannableStringBuilder;
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
                publicUsername = UserObject.getPublicUsername(tLRPC$User);
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
        if (TextUtils.isEmpty(str2)) {
            return str;
        }
        if (i >= 0) {
            TLRPC$TL_messageEntityMentionName tLRPC$TL_messageEntityMentionName2 = new TLRPC$TL_messageEntityMentionName();
            tLRPC$TL_messageEntityMentionName2.user_id = j2;
            tLRPC$TL_messageEntityMentionName2.offset = i + str.length() + 2;
            tLRPC$TL_messageEntityMentionName2.length = str2.length() + 1;
            arrayList.add(tLRPC$TL_messageEntityMentionName2);
        }
        return String.format("%1$s (@%2$s)", str, str2);
    }

    public boolean updateTranslation() {
        return updateTranslation(false);
    }

    public boolean updateTranslation(boolean z) {
        TLRPC$Message tLRPC$Message;
        MessageObject messageObject = this.replyMessageObject;
        boolean z2 = messageObject != null && messageObject.updateTranslation(z);
        TranslateController translateController = MessagesController.getInstance(this.currentAccount).getTranslateController();
        if (TranslateController.isTranslatable(this) && translateController.isTranslatingDialog(getDialogId()) && (tLRPC$Message = this.messageOwner) != null && tLRPC$Message.translatedText != null && TextUtils.equals(translateController.getDialogTranslateTo(getDialogId()), this.messageOwner.translatedToLanguage)) {
            if (this.translated) {
                return z2;
            }
            this.translated = true;
            applyNewText(this.messageOwner.translatedText.text);
            generateCaption();
            return true;
        }
        TLRPC$Message tLRPC$Message2 = this.messageOwner;
        if (tLRPC$Message2 == null || !(z || this.translated)) {
            return z2;
        }
        this.translated = false;
        applyNewText(tLRPC$Message2.message);
        generateCaption();
        return true;
    }

    public void applyNewText() {
        this.translated = false;
        applyNewText(this.messageOwner.message);
    }

    public void applyNewText(CharSequence charSequence) {
        TextPaint textPaint;
        TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities;
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        TLRPC$User user = isFromUser() ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id)) : null;
        this.messageText = charSequence;
        ArrayList<TLRPC$MessageEntity> arrayList = (!this.translated || (tLRPC$TL_textWithEntities = this.messageOwner.translatedText) == null) ? this.messageOwner.entities : tLRPC$TL_textWithEntities.entities;
        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
            textPaint = Theme.chat_msgGameTextPaint;
        } else {
            textPaint = Theme.chat_msgTextPaint;
        }
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

    private boolean allowsBigEmoji() {
        TLRPC$Peer tLRPC$Peer;
        if (SharedConfig.allowBigEmoji) {
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
        return false;
    }

    public void generateGameMessageText(TLRPC$User tLRPC$User) {
        if (tLRPC$User == null && isFromUser()) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
        }
        TLRPC$TL_game tLRPC$TL_game = null;
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null && getMedia(messageObject) != null && getMedia(this.replyMessageObject).game != null) {
            tLRPC$TL_game = getMedia(this.replyMessageObject).game;
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
            if (!(tLRPC$Message instanceof TLRPC$TL_messageEmpty)) {
                TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear) && !(tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicCreate)) {
                    return true;
                }
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
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null && (getMedia(messageObject) instanceof TLRPC$TL_messageMediaInvoice)) {
            if (this.messageOwner.action.recurring_init) {
                this.messageText = LocaleController.formatString(R.string.PaymentSuccessfullyPaidRecurrent, str, firstName, getMedia(this.replyMessageObject).title);
            } else {
                this.messageText = LocaleController.formatString("PaymentSuccessfullyPaid", R.string.PaymentSuccessfullyPaid, str, firstName, getMedia(this.replyMessageObject).title);
            }
        } else if (this.messageOwner.action.recurring_init) {
            this.messageText = LocaleController.formatString(R.string.PaymentSuccessfullyPaidNoItemRecurrent, str, firstName);
        } else {
            this.messageText = LocaleController.formatString("PaymentSuccessfullyPaidNoItem", R.string.PaymentSuccessfullyPaidNoItem, str, firstName);
        }
    }

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
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string, "un1", tLRPC$User);
                    return;
                } else if (this.replyMessageObject.isVideo()) {
                    String string2 = LocaleController.getString("ActionPinnedVideo", R.string.ActionPinnedVideo);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string2, "un1", tLRPC$User);
                    return;
                } else if (this.replyMessageObject.isGif()) {
                    String string3 = LocaleController.getString("ActionPinnedGif", R.string.ActionPinnedGif);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string3, "un1", tLRPC$User);
                    return;
                } else if (this.replyMessageObject.isVoice()) {
                    String string4 = LocaleController.getString("ActionPinnedVoice", R.string.ActionPinnedVoice);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string4, "un1", tLRPC$User);
                    return;
                } else if (this.replyMessageObject.isRoundVideo()) {
                    String string5 = LocaleController.getString("ActionPinnedRound", R.string.ActionPinnedRound);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string5, "un1", tLRPC$User);
                    return;
                } else if ((this.replyMessageObject.isSticker() || this.replyMessageObject.isAnimatedSticker()) && !this.replyMessageObject.isAnimatedEmoji()) {
                    String string6 = LocaleController.getString("ActionPinnedSticker", R.string.ActionPinnedSticker);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string6, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaDocument) {
                    String string7 = LocaleController.getString("ActionPinnedFile", R.string.ActionPinnedFile);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string7, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaGeo) {
                    String string8 = LocaleController.getString("ActionPinnedGeo", R.string.ActionPinnedGeo);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string8, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaGeoLive) {
                    String string9 = LocaleController.getString("ActionPinnedGeoLive", R.string.ActionPinnedGeoLive);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string9, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaContact) {
                    String string10 = LocaleController.getString("ActionPinnedContact", R.string.ActionPinnedContact);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string10, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaPoll) {
                    if (((TLRPC$TL_messageMediaPoll) getMedia(this.replyMessageObject)).poll.quiz) {
                        String string11 = LocaleController.getString("ActionPinnedQuiz", R.string.ActionPinnedQuiz);
                        if (tLRPC$User == null) {
                            tLRPC$User = tLRPC$Chat;
                        }
                        this.messageText = replaceWithLink(string11, "un1", tLRPC$User);
                        return;
                    }
                    String string12 = LocaleController.getString("ActionPinnedPoll", R.string.ActionPinnedPoll);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string12, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaPhoto) {
                    String string13 = LocaleController.getString("ActionPinnedPhoto", R.string.ActionPinnedPhoto);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string13, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaGame) {
                    int i = R.string.ActionPinnedGame;
                    String formatString = LocaleController.formatString("ActionPinnedGame", i, "🎮 " + getMedia(this.replyMessageObject).game.title);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    CharSequence replaceWithLink = replaceWithLink(formatString, "un1", tLRPC$User);
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
                        SpannableStringBuilder replaceEmoji = Emoji.replaceEmoji(cloneSpans, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
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
                        SpannableStringBuilder formatSpannable = AndroidUtilities.formatSpannable(LocaleController.getString("ActionPinnedText", R.string.ActionPinnedText), replaceEmoji);
                        if (tLRPC$User == null) {
                            tLRPC$User = tLRPC$Chat;
                        }
                        this.messageText = replaceWithLink(formatSpannable, "un1", tLRPC$User);
                        return;
                    }
                    String string14 = LocaleController.getString("ActionPinnedNoText", R.string.ActionPinnedNoText);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string14, "un1", tLRPC$User);
                    return;
                }
            }
        }
        String string15 = LocaleController.getString("ActionPinnedNoText", R.string.ActionPinnedNoText);
        if (tLRPC$User == null) {
            tLRPC$User = tLRPC$Chat;
        }
        this.messageText = replaceWithLink(string15, "un1", tLRPC$User);
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
        tLRPC$Message.flags |= FileLoaderPriorityQueue.PRIORITY_VALUE_MAX;
    }

    public boolean hasReactions() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        return (tLRPC$TL_messageReactions == null || tLRPC$TL_messageReactions.results.isEmpty()) ? false : true;
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
        if ((tLRPC$PollResults.flags & 16) != 0) {
            TLRPC$PollResults tLRPC$PollResults5 = tLRPC$TL_messageMediaPoll.results;
            tLRPC$PollResults5.solution = tLRPC$PollResults.solution;
            tLRPC$PollResults5.solution_entities = tLRPC$PollResults.solution_entities;
            tLRPC$PollResults5.flags |= 16;
        }
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
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.animatedEmojiDocumentLoaded, this);
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
                if (chat != null) {
                    Boolean valueOf = Boolean.valueOf(chat.megagroup);
                    this.cachedIsSupergroup = valueOf;
                    return valueOf.booleanValue();
                }
                return false;
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
        VideoEditedInfo videoEditedInfo = this.videoEditedInfo;
        boolean z = videoEditedInfo != null && videoEditedInfo.notReadyYet;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.message != null) {
            if ((tLRPC$Message.id < 0 || isEditing()) && (hashMap = this.messageOwner.params) != null) {
                String str2 = hashMap.get("ve");
                if (str2 != null && (isVideo() || isNewGif() || isRoundVideo())) {
                    VideoEditedInfo videoEditedInfo2 = new VideoEditedInfo();
                    this.videoEditedInfo = videoEditedInfo2;
                    if (!videoEditedInfo2.parseString(str2)) {
                        this.videoEditedInfo = null;
                    } else {
                        this.videoEditedInfo.roundVideo = isRoundVideo();
                        this.videoEditedInfo.notReadyYet = z;
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
    }

    public boolean hasInlineBotButtons() {
        TLRPC$Message tLRPC$Message;
        if (!this.isRestrictedMessage && !this.isRepostPreview && (tLRPC$Message = this.messageOwner) != null) {
            TLRPC$ReplyMarkup tLRPC$ReplyMarkup = tLRPC$Message.reply_markup;
            if ((tLRPC$ReplyMarkup instanceof TLRPC$TL_replyInlineMarkup) && !tLRPC$ReplyMarkup.rows.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void measureInlineBotButtons() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
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
        if (!(this.messageOwner.reply_markup instanceof TLRPC$TL_replyInlineMarkup) || hasExtendedMedia() || this.messageOwner.reply_markup.rows == null) {
            return;
        }
        for (int i = 0; i < this.messageOwner.reply_markup.rows.size(); i++) {
            TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = this.messageOwner.reply_markup.rows.get(i);
            int size = tLRPC$TL_keyboardButtonRow.buttons.size();
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i3);
                StringBuilder sb2 = this.botButtonsLayout;
                sb2.append(i);
                sb2.append(i3);
                if ((tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonBuy) && (getMedia(this.messageOwner).flags & 4) != 0) {
                    replaceEmoji = LocaleController.getString("PaymentReceipt", R.string.PaymentReceipt);
                } else {
                    String str = tLRPC$KeyboardButton.text;
                    if (str == null) {
                        str = "";
                    }
                    replaceEmoji = Emoji.replaceEmoji((CharSequence) str, Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false);
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

    /* JADX WARN: Removed duplicated region for block: B:11:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x06d7  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x06da  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x06e1  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x06e4  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0712  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x071d  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x072a  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x0750  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x07b2  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x085a  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x0878  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x08d8  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x08ee  */
    /* JADX WARN: Removed duplicated region for block: B:387:0x0938  */
    /* JADX WARN: Removed duplicated region for block: B:388:0x0944  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x0b4a  */
    /* JADX WARN: Removed duplicated region for block: B:497:0x0bf5  */
    /* JADX WARN: Removed duplicated region for block: B:814:0x14f6  */
    /* JADX WARN: Removed duplicated region for block: B:828:0x153d  */
    /* JADX WARN: Removed duplicated region for block: B:829:0x1540  */
    /* JADX WARN: Removed duplicated region for block: B:961:0x18b5  */
    /* JADX WARN: Removed duplicated region for block: B:987:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateMessageText(AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2) {
        TLRPC$User tLRPC$User;
        TLRPC$Chat chat;
        TLRPC$Message tLRPC$Message;
        String str;
        String publicUsername;
        boolean isChannelAndNotMegaGroup;
        String str2;
        String string;
        String formatString;
        String formatString2;
        String publicUsername2;
        String str3;
        TLRPC$Chat tLRPC$Chat;
        String sb;
        TLRPC$Chat tLRPC$Chat2;
        long j;
        ArrayList arrayList;
        Iterator<TLRPC$Peer> it;
        String str4;
        TLRPC$Chat tLRPC$Chat3;
        TLRPC$Chat tLRPC$Chat4;
        TLRPC$Chat tLRPC$Chat5;
        TLRPC$Chat tLRPC$Chat6;
        ArrayList<TLRPC$VideoSize> arrayList2;
        TLRPC$Chat tLRPC$Chat7;
        TLRPC$TL_messageActionGiveawayResults tLRPC$TL_messageActionGiveawayResults;
        TLRPC$Chat tLRPC$Chat8;
        String str5;
        boolean z;
        TLRPC$Chat tLRPC$Chat9;
        TLObject chat2;
        TLObject chat3;
        String formatPluralString;
        TLRPC$Peer tLRPC$Peer = this.messageOwner.from_id;
        if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
            tLRPC$User = getUser(abstractMap, longSparseArray, tLRPC$Peer.user_id);
        } else if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
            chat = getChat(abstractMap2, longSparseArray2, tLRPC$Peer.channel_id);
            tLRPC$User = null;
            TLObject tLObject = tLRPC$User == null ? tLRPC$User : chat;
            this.drawServiceWithDefaultTypeface = false;
            this.channelJoined = false;
            tLRPC$Message = this.messageOwner;
            String str6 = "";
            if (!(tLRPC$Message instanceof TLRPC$TL_messageService)) {
                TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                if (tLRPC$MessageAction != null) {
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetSameChatWallPaper) {
                        this.contentType = 1;
                        this.type = 10;
                        TLRPC$TL_messageActionSetSameChatWallPaper tLRPC$TL_messageActionSetSameChatWallPaper = (TLRPC$TL_messageActionSetSameChatWallPaper) tLRPC$MessageAction;
                        TLRPC$User user = getUser(abstractMap, longSparseArray, isOutOwner() ? 0L : getDialogId());
                        ArrayList<TLRPC$PhotoSize> arrayList3 = new ArrayList<>();
                        this.photoThumbs = arrayList3;
                        TLRPC$Document tLRPC$Document = tLRPC$TL_messageActionSetSameChatWallPaper.wallpaper.document;
                        if (tLRPC$Document != null) {
                            arrayList3.addAll(tLRPC$Document.thumbs);
                            this.photoThumbsObject = tLRPC$TL_messageActionSetSameChatWallPaper.wallpaper.document;
                        }
                        if (user != null) {
                            if (user.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                this.messageText = LocaleController.formatString(R.string.ActionSetSameWallpaperForThisChatSelf, new Object[0]);
                            } else {
                                this.messageText = LocaleController.formatString(R.string.ActionSetSameWallpaperForThisChat, user.first_name);
                            }
                        } else if (chat != null) {
                            this.messageText = LocaleController.getString(ChatObject.isChannelAndNotMegaGroup(chat) ? R.string.ActionSetWallpaperForThisChannel : R.string.ActionSetWallpaperForThisGroup);
                        } else if (tLRPC$User != null) {
                            this.messageText = LocaleController.formatString(R.string.ActionSetWallpaperForThisGroupByUser, UserObject.getFirstName(tLRPC$User));
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                        this.contentType = 1;
                        TLRPC$TL_messageActionSetChatWallPaper tLRPC$TL_messageActionSetChatWallPaper = (TLRPC$TL_messageActionSetChatWallPaper) tLRPC$MessageAction;
                        this.type = 22;
                        ArrayList<TLRPC$PhotoSize> arrayList4 = new ArrayList<>();
                        this.photoThumbs = arrayList4;
                        TLRPC$Document tLRPC$Document2 = tLRPC$TL_messageActionSetChatWallPaper.wallpaper.document;
                        if (tLRPC$Document2 != null) {
                            arrayList4.addAll(tLRPC$Document2.thumbs);
                            this.photoThumbsObject = tLRPC$TL_messageActionSetChatWallPaper.wallpaper.document;
                        }
                        TLRPC$User user2 = getUser(abstractMap, longSparseArray, isOutOwner() ? 0L : getDialogId());
                        TLRPC$User user3 = getUser(abstractMap, longSparseArray, getDialogId());
                        if (user2 != null) {
                            if (user2.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                if (tLRPC$TL_messageActionSetChatWallPaper.same) {
                                    this.type = 10;
                                    this.messageText = LocaleController.formatString(R.string.ActionSetSameWallpaperForThisChatSelf, new Object[0]);
                                } else if (tLRPC$TL_messageActionSetChatWallPaper.for_both && user3 != null) {
                                    this.messageText = LocaleController.getString(R.string.ActionSetWallpaperForThisChatSelfBoth);
                                    SpannableString spannableString = new SpannableString(UserObject.getFirstName(user3));
                                    spannableString.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, spannableString.length(), 33);
                                    this.messageText = AndroidUtilities.replaceCharSequence("%s", this.messageText, spannableString);
                                } else {
                                    this.messageText = LocaleController.getString(R.string.ActionSetWallpaperForThisChatSelf);
                                }
                            } else {
                                SpannableString spannableString2 = new SpannableString(UserObject.getFirstName(user2));
                                spannableString2.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, spannableString2.length(), 33);
                                if (tLRPC$TL_messageActionSetChatWallPaper.same) {
                                    this.type = 10;
                                    this.messageText = LocaleController.getString(R.string.ActionSetSameWallpaperForThisChat);
                                } else if (tLRPC$TL_messageActionSetChatWallPaper.for_both) {
                                    this.messageText = LocaleController.getString(R.string.ActionSetWallpaperForThisChatBoth);
                                } else {
                                    this.messageText = LocaleController.getString(R.string.ActionSetWallpaperForThisChat);
                                }
                                this.messageText = AndroidUtilities.replaceCharSequence("%s", this.messageText, spannableString2);
                            }
                        } else if (chat != null) {
                            this.messageText = LocaleController.getString(ChatObject.isChannelAndNotMegaGroup(chat) ? R.string.ActionSetWallpaperForThisChannel : R.string.ActionSetWallpaperForThisGroup);
                        } else if (tLRPC$User != null) {
                            this.messageText = LocaleController.formatString(R.string.ActionSetWallpaperForThisGroupByUser, UserObject.getFirstName(tLRPC$User));
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGroupCallScheduled) {
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
                                this.messageText = replaceWithLink(LocaleController.formatString("ActionGroupCallEndedBy", R.string.ActionGroupCallEndedBy, formatPluralString), "un1", tLObject);
                            }
                        } else if ((tLRPC$Message.peer_id instanceof TLRPC$TL_peerChat) || isSupergroup()) {
                            if (isOut()) {
                                this.messageText = LocaleController.getString("ActionGroupCallStartedByYou", R.string.ActionGroupCallStartedByYou);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallStarted", R.string.ActionGroupCallStarted), "un1", tLObject);
                            }
                        } else {
                            this.messageText = LocaleController.getString("ActionChannelCallJustStarted", R.string.ActionChannelCallJustStarted);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionInviteToGroupCall) {
                        long j2 = tLRPC$MessageAction.user_id;
                        if (j2 == 0 && tLRPC$MessageAction.users.size() == 1) {
                            j2 = this.messageOwner.action.users.get(0).longValue();
                        }
                        if (j2 != 0) {
                            TLRPC$User user4 = getUser(abstractMap, longSparseArray, j2);
                            if (isOut()) {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallYouInvited", R.string.ActionGroupCallYouInvited), "un2", user4);
                            } else if (j2 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallInvitedYou", R.string.ActionGroupCallInvitedYou), "un1", tLObject);
                            } else {
                                CharSequence replaceWithLink = replaceWithLink(LocaleController.getString("ActionGroupCallInvited", R.string.ActionGroupCallInvited), "un2", user4);
                                this.messageText = replaceWithLink;
                                this.messageText = replaceWithLink(replaceWithLink, "un1", tLObject);
                            }
                        } else if (isOut()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallYouInvited", R.string.ActionGroupCallYouInvited), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                        } else {
                            CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString("ActionGroupCallInvited", R.string.ActionGroupCallInvited), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                            this.messageText = replaceWithLink2;
                            this.messageText = replaceWithLink(replaceWithLink2, "un1", tLObject);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                        TLRPC$TL_messageActionGeoProximityReached tLRPC$TL_messageActionGeoProximityReached = (TLRPC$TL_messageActionGeoProximityReached) tLRPC$MessageAction;
                        long peerId = getPeerId(tLRPC$TL_messageActionGeoProximityReached.from_id);
                        if (peerId > 0) {
                            chat2 = getUser(abstractMap, longSparseArray, peerId);
                        } else {
                            chat2 = getChat(abstractMap2, longSparseArray2, -peerId);
                        }
                        long peerId2 = getPeerId(tLRPC$TL_messageActionGeoProximityReached.to_id);
                        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                        if (peerId2 == clientUserId) {
                            this.messageText = replaceWithLink(LocaleController.formatString("ActionUserWithinRadius", R.string.ActionUserWithinRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un1", chat2);
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
                                this.messageText = replaceWithLink(replaceWithLink3, "un1", chat2);
                            }
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCustomAction) {
                        this.messageText = tLRPC$MessageAction.message;
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatCreate) {
                        if (isOut()) {
                            this.messageText = LocaleController.getString("ActionYouCreateGroup", R.string.ActionYouCreateGroup);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionCreateGroup", R.string.ActionCreateGroup), "un1", tLObject);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatDeleteUser) {
                        if (isFromUser()) {
                            TLRPC$Message tLRPC$Message2 = this.messageOwner;
                            if (tLRPC$Message2.action.user_id == tLRPC$Message2.from_id.user_id) {
                                if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouLeftUser", R.string.ActionYouLeftUser);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionLeftUser", R.string.ActionLeftUser), "un1", tLObject);
                                }
                            }
                        }
                        TLRPC$User user5 = getUser(abstractMap, longSparseArray, this.messageOwner.action.user_id);
                        if (isOut()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionYouKickUser", R.string.ActionYouKickUser), "un2", user5);
                        } else if (this.messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionKickUserYou", R.string.ActionKickUserYou), "un1", tLObject);
                        } else {
                            CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString("ActionKickUser", R.string.ActionKickUser), "un2", user5);
                            this.messageText = replaceWithLink4;
                            this.messageText = replaceWithLink(replaceWithLink4, "un1", tLObject);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatAddUser) {
                        long j3 = tLRPC$MessageAction.user_id;
                        if (j3 == 0 && tLRPC$MessageAction.users.size() == 1) {
                            j3 = this.messageOwner.action.users.get(0).longValue();
                        }
                        if (j3 != 0) {
                            TLRPC$User user6 = getUser(abstractMap, longSparseArray, j3);
                            long j4 = this.messageOwner.peer_id.channel_id;
                            TLRPC$Chat chat4 = j4 != 0 ? getChat(abstractMap2, longSparseArray2, j4) : null;
                            TLRPC$Peer tLRPC$Peer2 = this.messageOwner.from_id;
                            if (tLRPC$Peer2 != null && j3 == tLRPC$Peer2.user_id) {
                                if (ChatObject.isChannel(chat4) && !chat4.megagroup) {
                                    this.channelJoined = true;
                                    this.messageText = LocaleController.getString("ChannelJoined", R.string.ChannelJoined);
                                } else if (this.messageOwner.peer_id.channel_id != 0) {
                                    if (j3 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        this.messageText = LocaleController.getString("ChannelMegaJoined", R.string.ChannelMegaJoined);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelfMega", R.string.ActionAddUserSelfMega), "un1", tLObject);
                                    }
                                } else if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionAddUserSelfYou", R.string.ActionAddUserSelfYou);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelf", R.string.ActionAddUserSelf), "un1", tLObject);
                                }
                            } else if (isOut()) {
                                this.messageText = replaceWithLink(LocaleController.getString("ActionYouAddUser", R.string.ActionYouAddUser), "un2", user6);
                            } else if (j3 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                if (this.messageOwner.peer_id.channel_id != 0) {
                                    if (chat4 != null && chat4.megagroup) {
                                        this.messageText = replaceWithLink(LocaleController.getString("MegaAddedBy", R.string.MegaAddedBy), "un1", tLObject);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ChannelAddedBy", R.string.ChannelAddedBy), "un1", tLObject);
                                    }
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserYou", R.string.ActionAddUserYou), "un1", tLObject);
                                }
                            } else {
                                CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString("ActionAddUser", R.string.ActionAddUser), "un2", user6);
                                this.messageText = replaceWithLink5;
                                this.messageText = replaceWithLink(replaceWithLink5, "un1", tLObject);
                            }
                        } else if (isOut()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionYouAddUser", R.string.ActionYouAddUser), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                        } else {
                            CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString("ActionAddUser", R.string.ActionAddUser), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                            this.messageText = replaceWithLink6;
                            this.messageText = replaceWithLink(replaceWithLink6, "un1", tLObject);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatJoinedByLink) {
                        if (isOut()) {
                            this.messageText = LocaleController.getString("ActionInviteYou", R.string.ActionInviteYou);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionInviteUser", R.string.ActionInviteUser), "un1", tLObject);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiveawayLaunch) {
                        TLRPC$Peer tLRPC$Peer3 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer3 != null) {
                            long j5 = tLRPC$Peer3.channel_id;
                            if (j5 != 0) {
                                tLRPC$Chat9 = getChat(abstractMap2, longSparseArray2, j5);
                                int i5 = !ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat9) ? R.string.BoostingGiveawayJustStarted : R.string.BoostingGiveawayJustStartedGroup;
                                Object[] objArr = new Object[1];
                                objArr[0] = tLRPC$Chat9 == null ? tLRPC$Chat9.title : "";
                                this.messageText = LocaleController.formatString(i5, objArr);
                            }
                        }
                        tLRPC$Chat9 = null;
                        if (!ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat9)) {
                        }
                        Object[] objArr2 = new Object[1];
                        objArr2[0] = tLRPC$Chat9 == null ? tLRPC$Chat9.title : "";
                        this.messageText = LocaleController.formatString(i5, objArr2);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionBoostApply) {
                        TLRPC$Peer tLRPC$Peer4 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer4 != null) {
                            long j6 = tLRPC$Peer4.channel_id;
                            if (j6 != 0) {
                                tLRPC$Chat8 = getChat(abstractMap2, longSparseArray2, j6);
                                boolean isChannelAndNotMegaGroup2 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat8);
                                TLRPC$TL_messageActionBoostApply tLRPC$TL_messageActionBoostApply = (TLRPC$TL_messageActionBoostApply) this.messageOwner.action;
                                if (!(tLObject instanceof TLRPC$User)) {
                                    TLRPC$User tLRPC$User2 = (TLRPC$User) tLObject;
                                    z = UserObject.isUserSelf(tLRPC$User2);
                                    str5 = UserObject.getFirstName(tLRPC$User2);
                                } else {
                                    str5 = tLObject instanceof TLRPC$Chat ? ((TLRPC$Chat) tLObject).title : "";
                                    z = false;
                                }
                                if (!z) {
                                    int i6 = tLRPC$TL_messageActionBoostApply.boosts;
                                    if (i6 <= 1) {
                                        this.messageText = LocaleController.getString(isChannelAndNotMegaGroup2 ? R.string.BoostingBoostsChannelByYouServiceMsg : R.string.BoostingBoostsGroupByYouServiceMsg);
                                    } else {
                                        this.messageText = LocaleController.formatPluralString(isChannelAndNotMegaGroup2 ? "BoostingBoostsChannelByYouServiceMsgCount" : "BoostingBoostsGroupByYouServiceMsgCount", i6, new Object[0]);
                                    }
                                } else {
                                    int i7 = tLRPC$TL_messageActionBoostApply.boosts;
                                    if (i7 <= 1) {
                                        this.messageText = LocaleController.formatString(isChannelAndNotMegaGroup2 ? R.string.BoostingBoostsChannelByUserServiceMsg : R.string.BoostingBoostsGroupByUserServiceMsg, str5);
                                    } else {
                                        this.messageText = LocaleController.formatPluralString(isChannelAndNotMegaGroup2 ? "BoostingBoostsChannelByUserServiceMsgCount" : "BoostingBoostsGroupByUserServiceMsgCount", i7, str5);
                                    }
                                }
                            }
                        }
                        tLRPC$Chat8 = null;
                        boolean isChannelAndNotMegaGroup22 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat8);
                        TLRPC$TL_messageActionBoostApply tLRPC$TL_messageActionBoostApply2 = (TLRPC$TL_messageActionBoostApply) this.messageOwner.action;
                        if (!(tLObject instanceof TLRPC$User)) {
                        }
                        if (!z) {
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiveawayResults) {
                        TLRPC$Peer tLRPC$Peer5 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer5 != null) {
                            long j7 = tLRPC$Peer5.channel_id;
                            if (j7 != 0) {
                                tLRPC$Chat7 = getChat(abstractMap2, longSparseArray2, j7);
                                boolean isChannelAndNotMegaGroup3 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat7);
                                tLRPC$TL_messageActionGiveawayResults = (TLRPC$TL_messageActionGiveawayResults) this.messageOwner.action;
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                spannableStringBuilder.append((CharSequence) LocaleController.formatPluralString("BoostingGiveawayServiceWinnersSelected", tLRPC$TL_messageActionGiveawayResults.winners_count, new Object[0]));
                                if (tLRPC$TL_messageActionGiveawayResults.unclaimed_count > 0) {
                                    spannableStringBuilder.append((CharSequence) "\n");
                                    spannableStringBuilder.append((CharSequence) LocaleController.formatPluralString(isChannelAndNotMegaGroup3 ? "BoostingGiveawayServiceUndistributed" : "BoostingGiveawayServiceUndistributedGroup", tLRPC$TL_messageActionGiveawayResults.unclaimed_count, new Object[0]));
                                }
                                this.messageText = spannableStringBuilder;
                            }
                        }
                        tLRPC$Chat7 = null;
                        boolean isChannelAndNotMegaGroup32 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat7);
                        tLRPC$TL_messageActionGiveawayResults = (TLRPC$TL_messageActionGiveawayResults) this.messageOwner.action;
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                        spannableStringBuilder2.append((CharSequence) LocaleController.formatPluralString("BoostingGiveawayServiceWinnersSelected", tLRPC$TL_messageActionGiveawayResults.winners_count, new Object[0]));
                        if (tLRPC$TL_messageActionGiveawayResults.unclaimed_count > 0) {
                        }
                        this.messageText = spannableStringBuilder2;
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftCode) && ((TLRPC$TL_messageActionGiftCode) tLRPC$MessageAction).boost_peer != null) {
                        this.messageText = LocaleController.getString("BoostingReceivedGiftNoName", R.string.BoostingReceivedGiftNoName);
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftPremium) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftCode)) {
                        str = "";
                        if ((tLObject instanceof TLRPC$User) && ((TLRPC$User) tLObject).self) {
                            this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftOutbound)), "un1", getUser(abstractMap, longSparseArray, tLRPC$Message.peer_id.user_id));
                        } else {
                            this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), "un1", tLObject);
                        }
                        int indexOf = this.messageText.toString().indexOf("un2");
                        if (indexOf != -1) {
                            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(this.messageText);
                            BillingController billingController = BillingController.getInstance();
                            TLRPC$MessageAction tLRPC$MessageAction2 = this.messageOwner.action;
                            String formatCurrency = billingController.formatCurrency(tLRPC$MessageAction2.amount, tLRPC$MessageAction2.currency);
                            if ((this.messageOwner.action.flags & 1) != 0) {
                                StringBuilder sb2 = new StringBuilder();
                                double d = this.messageOwner.action.cryptoAmount;
                                double pow = Math.pow(10.0d, -9.0d);
                                Double.isNaN(d);
                                sb2.append(String.format("%.2f", Double.valueOf(d * pow)));
                                sb2.append(" ");
                                sb2.append(this.messageOwner.action.cryptoCurrency);
                                sb2.append(" (~ ");
                                sb2.append((Object) formatCurrency);
                                sb2.append(")");
                                formatCurrency = sb2.toString();
                            }
                            this.messageText = valueOf.replace(indexOf, indexOf + 3, (CharSequence) formatCurrency);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSuggestProfilePhoto) {
                        TLRPC$Photo tLRPC$Photo = tLRPC$MessageAction.photo;
                        if (tLRPC$Photo != null && (arrayList2 = tLRPC$Photo.video_sizes) != null && !arrayList2.isEmpty()) {
                            this.messageText = LocaleController.getString(R.string.ActionSuggestVideoShort);
                        } else {
                            this.messageText = LocaleController.getString(R.string.ActionSuggestPhotoShort);
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto) {
                        TLRPC$Peer tLRPC$Peer6 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer6 != null) {
                            long j8 = tLRPC$Peer6.channel_id;
                            if (j8 != 0) {
                                tLRPC$Chat6 = getChat(abstractMap2, longSparseArray2, j8);
                                if (!ChatObject.isChannel(tLRPC$Chat6) && !tLRPC$Chat6.megagroup) {
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
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionChangedVideo", R.string.ActionChangedVideo), "un1", tLObject);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionChangedPhoto", R.string.ActionChangedPhoto), "un1", tLObject);
                                }
                            }
                        }
                        tLRPC$Chat6 = null;
                        if (!ChatObject.isChannel(tLRPC$Chat6)) {
                        }
                        if (!isOut()) {
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditTitle) {
                        TLRPC$Peer tLRPC$Peer7 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer7 != null) {
                            long j9 = tLRPC$Peer7.channel_id;
                            if (j9 != 0) {
                                tLRPC$Chat5 = getChat(abstractMap2, longSparseArray2, j9);
                                if (!ChatObject.isChannel(tLRPC$Chat5) && !tLRPC$Chat5.megagroup) {
                                    this.messageText = LocaleController.getString("ActionChannelChangedTitle", R.string.ActionChannelChangedTitle).replace("un2", this.messageOwner.action.title);
                                } else if (!isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouChangedTitle", R.string.ActionYouChangedTitle).replace("un2", this.messageOwner.action.title);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionChangedTitle", R.string.ActionChangedTitle).replace("un2", this.messageOwner.action.title), "un1", tLObject);
                                }
                            }
                        }
                        tLRPC$Chat5 = null;
                        if (!ChatObject.isChannel(tLRPC$Chat5)) {
                        }
                        if (!isOut()) {
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatDeletePhoto) {
                        TLRPC$Peer tLRPC$Peer8 = tLRPC$Message.peer_id;
                        if (tLRPC$Peer8 != null) {
                            long j10 = tLRPC$Peer8.channel_id;
                            if (j10 != 0) {
                                tLRPC$Chat4 = getChat(abstractMap2, longSparseArray2, j10);
                                if (!ChatObject.isChannel(tLRPC$Chat4) && !tLRPC$Chat4.megagroup) {
                                    this.messageText = LocaleController.getString("ActionChannelRemovedPhoto", R.string.ActionChannelRemovedPhoto);
                                } else if (!isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouRemovedPhoto", R.string.ActionYouRemovedPhoto);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionRemovedPhoto", R.string.ActionRemovedPhoto), "un1", tLObject);
                                }
                            }
                        }
                        tLRPC$Chat4 = null;
                        if (!ChatObject.isChannel(tLRPC$Chat4)) {
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
                    } else {
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionRequestedPeer) {
                            ArrayList arrayList5 = new ArrayList();
                            Iterator<TLRPC$Peer> it2 = ((TLRPC$TL_messageActionRequestedPeer) this.messageOwner.action).peers.iterator();
                            int i8 = 0;
                            int i9 = 0;
                            int i10 = 0;
                            while (it2.hasNext()) {
                                TLRPC$Peer next = it2.next();
                                boolean z2 = next instanceof TLRPC$TL_peerUser;
                                if (z2) {
                                    arrayList = arrayList5;
                                    it = it2;
                                    TLRPC$User user7 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(next.user_id));
                                    TLRPC$User tLRPC$User3 = user7;
                                    if (user7 == null) {
                                        tLRPC$User3 = getUser(abstractMap, longSparseArray, next.user_id);
                                    }
                                    str4 = str6;
                                    tLRPC$Chat3 = tLRPC$User3;
                                } else {
                                    arrayList = arrayList5;
                                    it = it2;
                                    if (next instanceof TLRPC$TL_peerChat) {
                                        str4 = str6;
                                        TLRPC$Chat chat5 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(next.chat_id));
                                        tLRPC$Chat3 = chat5;
                                        if (chat5 == null) {
                                            tLRPC$Chat3 = getChat(abstractMap2, longSparseArray2, next.chat_id);
                                        }
                                    } else {
                                        str4 = str6;
                                        if (next instanceof TLRPC$TL_peerChannel) {
                                            TLRPC$Chat chat6 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(next.channel_id));
                                            tLRPC$Chat3 = chat6;
                                            if (chat6 == null) {
                                                tLRPC$Chat3 = getChat(abstractMap2, longSparseArray2, next.channel_id);
                                            }
                                        } else {
                                            tLRPC$Chat3 = null;
                                        }
                                    }
                                }
                                if (z2) {
                                    i8++;
                                } else if (next instanceof TLRPC$TL_peerChat) {
                                    i10++;
                                } else {
                                    i9++;
                                }
                                ArrayList arrayList6 = arrayList;
                                if (tLRPC$Chat3 != null) {
                                    arrayList6.add(tLRPC$Chat3);
                                }
                                arrayList5 = arrayList6;
                                it2 = it;
                                str6 = str4;
                            }
                            ArrayList arrayList7 = arrayList5;
                            str2 = str6;
                            if (i8 > 0 && i8 != arrayList7.size()) {
                                this.messageText = LocaleController.getPluralString("ActionRequestedPeerUserPlural", i8);
                            } else if (i9 > 0 && i9 != arrayList7.size()) {
                                this.messageText = LocaleController.getPluralString("ActionRequestedPeerChannelPlural", i9);
                            } else if (i10 > 0 && i10 != arrayList7.size()) {
                                this.messageText = LocaleController.getPluralString("ActionRequestedPeerChatPlural", i10);
                            } else {
                                SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder();
                                for (int i11 = 0; i11 < arrayList7.size(); i11++) {
                                    spannableStringBuilder3.append(replaceWithLink("un1", "un1", (TLObject) arrayList7.get(i11)));
                                    if (i11 < arrayList7.size() - 1) {
                                        spannableStringBuilder3.append((CharSequence) ", ");
                                    }
                                }
                                this.messageText = AndroidUtilities.replaceCharSequence("un1", LocaleController.getString(R.string.ActionRequestedPeer), spannableStringBuilder3);
                            }
                            TLRPC$User user8 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
                            if (user8 == null) {
                                user8 = getUser(abstractMap, longSparseArray, getDialogId());
                            }
                            this.messageText = replaceWithLink(this.messageText, "un2", user8);
                        } else {
                            str2 = "";
                            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) {
                                TLRPC$TL_messageActionSetMessagesTTL tLRPC$TL_messageActionSetMessagesTTL = (TLRPC$TL_messageActionSetMessagesTTL) tLRPC$MessageAction;
                                TLRPC$Peer tLRPC$Peer9 = tLRPC$Message.peer_id;
                                if (tLRPC$Peer9 != null) {
                                    long j11 = tLRPC$Peer9.channel_id;
                                    if (j11 != 0) {
                                        tLRPC$Chat2 = getChat(abstractMap2, longSparseArray2, j11);
                                        if (tLRPC$Chat2 == null && !tLRPC$Chat2.megagroup) {
                                            int i12 = tLRPC$TL_messageActionSetMessagesTTL.period;
                                            if (i12 != 0) {
                                                this.messageText = LocaleController.formatString("ActionTTLChannelChanged", R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i12));
                                            } else {
                                                this.messageText = LocaleController.getString("ActionTTLChannelDisabled", R.string.ActionTTLChannelDisabled);
                                            }
                                        } else {
                                            j = tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from;
                                            if (j == 0) {
                                                this.drawServiceWithDefaultTypeface = true;
                                                if (j == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                    this.messageText = AndroidUtilities.replaceTags(LocaleController.formatString("AutoDeleteGlobalActionFromYou", R.string.AutoDeleteGlobalActionFromYou, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period)));
                                                } else {
                                                    TLObject tLObject2 = longSparseArray != null ? longSparseArray.get(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from) : null;
                                                    if (tLObject2 == null && abstractMap != null) {
                                                        tLObject2 = abstractMap.get(Long.valueOf(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                    }
                                                    if (tLObject2 == null && abstractMap2 != null) {
                                                        tLObject2 = abstractMap2.get(Long.valueOf(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                    }
                                                    if (tLObject2 == null) {
                                                        if (tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from > 0) {
                                                            tLObject2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                        } else {
                                                            tLObject2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                        }
                                                    }
                                                    if (tLObject2 != null) {
                                                        tLObject = tLObject2;
                                                    }
                                                    this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.formatString("AutoDeleteGlobalAction", R.string.AutoDeleteGlobalAction, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period))), "un1", tLObject);
                                                }
                                            } else if (tLRPC$TL_messageActionSetMessagesTTL.period != 0) {
                                                if (isOut()) {
                                                    this.messageText = LocaleController.formatString("ActionTTLYouChanged", R.string.ActionTTLYouChanged, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period));
                                                } else {
                                                    this.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", R.string.ActionTTLChanged, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period)), "un1", tLObject);
                                                }
                                            } else if (isOut()) {
                                                this.messageText = LocaleController.getString("ActionTTLYouDisabled", R.string.ActionTTLYouDisabled);
                                            } else {
                                                this.messageText = replaceWithLink(LocaleController.getString("ActionTTLDisabled", R.string.ActionTTLDisabled), "un1", tLObject);
                                            }
                                        }
                                    }
                                }
                                tLRPC$Chat2 = null;
                                if (tLRPC$Chat2 == null) {
                                }
                                j = tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from;
                                if (j == 0) {
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                                long j12 = tLRPC$Message.date * 1000;
                                if (LocaleController.getInstance().formatterDay != null && LocaleController.getInstance().formatterYear != null) {
                                    sb = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(j12), LocaleController.getInstance().formatterDay.format(j12));
                                    str = str2;
                                } else {
                                    StringBuilder sb3 = new StringBuilder();
                                    str = str2;
                                    sb3.append(str);
                                    sb3.append(this.messageOwner.date);
                                    sb = sb3.toString();
                                }
                                TLRPC$User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                                if (currentUser == null) {
                                    currentUser = getUser(abstractMap, longSparseArray, this.messageOwner.peer_id.user_id);
                                }
                                String firstName = currentUser != null ? UserObject.getFirstName(currentUser) : str;
                                int i13 = R.string.NotificationUnrecognizedDevice;
                                TLRPC$MessageAction tLRPC$MessageAction3 = this.messageOwner.action;
                                this.messageText = LocaleController.formatString("NotificationUnrecognizedDevice", i13, firstName, sb, tLRPC$MessageAction3.title, tLRPC$MessageAction3.address);
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
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", R.string.ActionTakeScreenshoot), "un1", tLObject);
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
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", R.string.ActionTakeScreenshoot), "un1", tLObject);
                                    }
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCreatedBroadcastList) {
                                    this.messageText = LocaleController.formatString("YouCreatedBroadcastList", R.string.YouCreatedBroadcastList, new Object[0]);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChannelCreate) {
                                    TLRPC$Peer tLRPC$Peer10 = tLRPC$Message.peer_id;
                                    if (tLRPC$Peer10 != null) {
                                        long j13 = tLRPC$Peer10.channel_id;
                                        if (j13 != 0) {
                                            tLRPC$Chat = getChat(abstractMap2, longSparseArray2, j13);
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
                                    this.messageTextShort = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString("TopicWasCreatedAction", R.string.TopicWasCreatedAction), ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic, null, false));
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicEdit) {
                                    TLRPC$TL_messageActionTopicEdit tLRPC$TL_messageActionTopicEdit = (TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction;
                                    if (tLRPC$User != null) {
                                        str3 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                                    } else if (chat != null) {
                                        str3 = chat.title;
                                        tLRPC$User = chat;
                                    } else {
                                        str3 = null;
                                        tLRPC$User = null;
                                    }
                                    String trim = str3 != null ? str3.trim() : "DELETED";
                                    TLRPC$MessageAction tLRPC$MessageAction4 = this.messageOwner.action;
                                    int i14 = tLRPC$MessageAction4.flags;
                                    if ((i14 & 8) > 0) {
                                        if (((TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction4).hidden) {
                                            this.messageText = replaceWithLink(LocaleController.getString("TopicHidden2", R.string.TopicHidden2), "%s", tLRPC$User);
                                            this.messageTextShort = LocaleController.getString("TopicHidden", R.string.TopicHidden);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("TopicShown2", R.string.TopicShown2), "%s", tLRPC$User);
                                            this.messageTextShort = LocaleController.getString("TopicShown", R.string.TopicShown);
                                        }
                                    } else if ((i14 & 4) > 0) {
                                        if (((TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction4).closed) {
                                            this.messageText = replaceWithLink(LocaleController.getString("TopicClosed2", R.string.TopicClosed2), "%s", tLRPC$User);
                                            this.messageTextShort = LocaleController.getString("TopicClosed", R.string.TopicClosed);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("TopicRestarted2", R.string.TopicRestarted2), "%s", tLRPC$User);
                                            this.messageTextShort = LocaleController.getString("TopicRestarted", R.string.TopicRestarted);
                                        }
                                    } else if ((i14 & 2) != 0 && (i14 & 1) != 0) {
                                        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = new TLRPC$TL_forumTopic();
                                        tLRPC$TL_forumTopic2.icon_emoji_id = tLRPC$TL_messageActionTopicEdit.icon_emoji_id;
                                        tLRPC$TL_forumTopic2.title = tLRPC$TL_messageActionTopicEdit.title;
                                        tLRPC$TL_forumTopic2.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                        CharSequence topicSpannedName = ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic2, null, this.topicIconDrawable, false);
                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString("TopicChangeIconAndTitleTo", R.string.TopicChangeIconAndTitleTo), trim), topicSpannedName);
                                        this.messageTextShort = LocaleController.getString("TopicRenamed", R.string.TopicRenamed);
                                        this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString("TopicChangeIconAndTitleToInReply", R.string.TopicChangeIconAndTitleToInReply), topicSpannedName);
                                    } else if ((i14 & 2) != 0) {
                                        TLRPC$TL_forumTopic tLRPC$TL_forumTopic3 = new TLRPC$TL_forumTopic();
                                        tLRPC$TL_forumTopic3.icon_emoji_id = tLRPC$TL_messageActionTopicEdit.icon_emoji_id;
                                        tLRPC$TL_forumTopic3.title = str;
                                        tLRPC$TL_forumTopic3.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                        CharSequence topicSpannedName2 = ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic3, null, this.topicIconDrawable, false);
                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString("TopicIconChangedTo", R.string.TopicIconChangedTo), trim), topicSpannedName2);
                                        this.messageTextShort = LocaleController.getString("TopicIconChanged", R.string.TopicIconChanged);
                                        this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString("TopicIconChangedToInReply", R.string.TopicIconChangedToInReply), topicSpannedName2);
                                    } else if ((1 & i14) != 0) {
                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString("TopicRenamedTo", R.string.TopicRenamedTo), trim), tLRPC$TL_messageActionTopicEdit.title);
                                        this.messageTextShort = LocaleController.getString("TopicRenamed", R.string.TopicRenamed);
                                        this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString("TopicRenamedToInReply", R.string.TopicRenamedToInReply), tLRPC$TL_messageActionTopicEdit.title);
                                    }
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) {
                                    generateGameMessageText(tLRPC$User);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                                    TLRPC$TL_messageActionPhoneCall tLRPC$TL_messageActionPhoneCall = (TLRPC$TL_messageActionPhoneCall) tLRPC$MessageAction;
                                    boolean z3 = tLRPC$TL_messageActionPhoneCall.reason instanceof TLRPC$TL_phoneCallDiscardReasonMissed;
                                    if (isFromUser() && this.messageOwner.from_id.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        if (z3) {
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
                                    } else if (z3) {
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
                                    int i15 = tLRPC$TL_messageActionPhoneCall.duration;
                                    if (i15 > 0) {
                                        String formatCallDuration = LocaleController.formatCallDuration(i15);
                                        String formatString3 = LocaleController.formatString("CallMessageWithDuration", R.string.CallMessageWithDuration, this.messageText, formatCallDuration);
                                        this.messageText = formatString3;
                                        String charSequence = formatString3.toString();
                                        int indexOf2 = charSequence.indexOf(formatCallDuration);
                                        if (indexOf2 != -1) {
                                            SpannableString spannableString3 = new SpannableString(this.messageText);
                                            int length = formatCallDuration.length() + indexOf2;
                                            if (indexOf2 > 0 && charSequence.charAt(indexOf2 - 1) == '(') {
                                                indexOf2--;
                                            }
                                            if (length < charSequence.length() && charSequence.charAt(length) == ')') {
                                                length++;
                                            }
                                            spannableString3.setSpan(new TypefaceSpan(Typeface.DEFAULT), indexOf2, length, 0);
                                            this.messageText = spannableString3;
                                        }
                                    }
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent) {
                                    generatePaymentSentMessageText(getUser(abstractMap, longSparseArray, getDialogId()));
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionBotAllowed) {
                                    String str7 = ((TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction).domain;
                                    TLRPC$BotApp tLRPC$BotApp = ((TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction).app;
                                    if (((TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction).from_request) {
                                        this.messageText = LocaleController.getString(R.string.ActionBotAllowedWebapp);
                                    } else if (tLRPC$BotApp != null) {
                                        String str8 = tLRPC$BotApp.title;
                                        if (str8 == null) {
                                            str8 = str;
                                        }
                                        String string2 = LocaleController.getString("ActionBotAllowedApp", R.string.ActionBotAllowedApp);
                                        int indexOf3 = string2.indexOf("%1$s");
                                        SpannableString spannableString4 = new SpannableString(String.format(string2, str8));
                                        TLRPC$User user9 = getUser(abstractMap, longSparseArray, getDialogId());
                                        if (indexOf3 >= 0 && user9 != null && (publicUsername2 = UserObject.getPublicUsername(user9)) != null) {
                                            spannableString4.setSpan(new URLSpanNoUnderlineBold("https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername2 + "/" + tLRPC$BotApp.short_name), indexOf3, str8.length() + indexOf3, 33);
                                        }
                                        this.messageText = spannableString4;
                                    } else {
                                        if (str7 == null) {
                                            str7 = str;
                                        }
                                        String string3 = LocaleController.getString("ActionBotAllowed", R.string.ActionBotAllowed);
                                        int indexOf4 = string3.indexOf("%1$s");
                                        SpannableString spannableString5 = new SpannableString(String.format(string3, str7));
                                        if (indexOf4 >= 0 && !TextUtils.isEmpty(str7)) {
                                            spannableString5.setSpan(new URLSpanNoUnderlineBold("http://" + str7), indexOf4, str7.length() + indexOf4, 33);
                                        }
                                        this.messageText = spannableString5;
                                    }
                                } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionAttachMenuBotAllowed) || ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionBotAllowed) && ((TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction).attach_menu)) {
                                    this.messageText = LocaleController.getString(R.string.ActionAttachMenuBotAllowed);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSecureValuesSent) {
                                    TLRPC$TL_messageActionSecureValuesSent tLRPC$TL_messageActionSecureValuesSent = (TLRPC$TL_messageActionSecureValuesSent) tLRPC$MessageAction;
                                    StringBuilder sb4 = new StringBuilder();
                                    int size = tLRPC$TL_messageActionSecureValuesSent.types.size();
                                    for (int i16 = 0; i16 < size; i16++) {
                                        TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_messageActionSecureValuesSent.types.get(i16);
                                        if (sb4.length() > 0) {
                                            sb4.append(", ");
                                        }
                                        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentPhone", R.string.ActionBotDocumentPhone));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentEmail", R.string.ActionBotDocumentEmail));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentAddress", R.string.ActionBotDocumentAddress));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentIdentity", R.string.ActionBotDocumentIdentity));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentPassport", R.string.ActionBotDocumentPassport));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentDriverLicence", R.string.ActionBotDocumentDriverLicence));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentIdentityCard", R.string.ActionBotDocumentIdentityCard));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentUtilityBill", R.string.ActionBotDocumentUtilityBill));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentBankStatement", R.string.ActionBotDocumentBankStatement));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentRentalAgreement", R.string.ActionBotDocumentRentalAgreement));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentInternalPassport", R.string.ActionBotDocumentInternalPassport));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentPassportRegistration", R.string.ActionBotDocumentPassportRegistration));
                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                                            sb4.append(LocaleController.getString("ActionBotDocumentTemporaryRegistration", R.string.ActionBotDocumentTemporaryRegistration));
                                        }
                                    }
                                    TLRPC$Peer tLRPC$Peer11 = this.messageOwner.peer_id;
                                    this.messageText = LocaleController.formatString("ActionBotDocuments", R.string.ActionBotDocuments, UserObject.getFirstName(tLRPC$Peer11 != null ? getUser(abstractMap, longSparseArray, tLRPC$Peer11.user_id) : null), sb4.toString());
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionWebViewDataSent) {
                                    this.messageText = LocaleController.formatString("ActionBotWebViewData", R.string.ActionBotWebViewData, ((TLRPC$TL_messageActionWebViewDataSent) tLRPC$MessageAction).text);
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                                    String str9 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                                    String firstName2 = UserObject.getFirstName(tLRPC$User);
                                    boolean z4 = tLRPC$User == null && chat != null;
                                    if (z4) {
                                        firstName2 = chat.title;
                                    }
                                    boolean isUserSelf = UserObject.isUserSelf(tLRPC$User);
                                    if (TextUtils.isEmpty(str9)) {
                                        if (isUserSelf) {
                                            formatString2 = LocaleController.formatString("ChatThemeDisabledYou", R.string.ChatThemeDisabledYou, new Object[0]);
                                        } else {
                                            formatString2 = LocaleController.formatString(z4 ? R.string.ChannelThemeDisabled : R.string.ChatThemeDisabled, firstName2, str9);
                                        }
                                        this.messageText = formatString2;
                                    } else {
                                        if (isUserSelf) {
                                            formatString = LocaleController.formatString("ChatThemeChangedYou", R.string.ChatThemeChangedYou, str9);
                                        } else {
                                            formatString = LocaleController.formatString(z4 ? R.string.ChannelThemeChangedTo : R.string.ChatThemeChangedTo, firstName2, str9);
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
                                        this.messageText = replaceWithLink(LocaleController.getString("UserAcceptedToGroupAction", R.string.UserAcceptedToGroupAction), "un1", tLObject);
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
                    if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveaway) {
                        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                        if (tLRPC$MessageFwdHeader != null) {
                            TLRPC$Peer tLRPC$Peer12 = tLRPC$MessageFwdHeader.from_id;
                            if (tLRPC$Peer12 instanceof TLRPC$TL_peerChannel) {
                                isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(getChat(abstractMap2, longSparseArray2, tLRPC$Peer12.channel_id));
                                this.messageText = LocaleController.getString(!isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted);
                            }
                        }
                        isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(chat);
                        this.messageText = LocaleController.getString(!isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveawayResults) {
                        this.messageText = LocaleController.getString("BoostingGiveawayResults", R.string.BoostingGiveawayResults);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaStory) {
                        if (getMedia(this.messageOwner).via_mention) {
                            TLRPC$User user10 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getMedia(this.messageOwner).user_id));
                            String str10 = (user10 == null || (publicUsername = UserObject.getPublicUsername(user10)) == null) ? null : MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername + "/s/" + getMedia(this.messageOwner).id;
                            if (str10 != null) {
                                SpannableString spannableString6 = new SpannableString(str10);
                                this.messageText = spannableString6;
                                spannableString6.setSpan(new URLSpanReplacement("https://" + str10, new TextStyleSpan.TextStyleRun()), 0, this.messageText.length(), 33);
                            } else {
                                this.messageText = str;
                            }
                        } else {
                            this.messageText = LocaleController.getString("ForwardedStory", R.string.ForwardedStory);
                        }
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDice) {
                        this.messageText = getDiceEmoji();
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPoll) {
                        if (((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.quiz) {
                            this.messageText = LocaleController.getString("QuizPoll", R.string.QuizPoll);
                        } else {
                            this.messageText = LocaleController.getString("Poll", R.string.Poll);
                        }
                    } else if (isVoiceOnce()) {
                        this.messageText = LocaleController.getString(R.string.AttachOnceAudio);
                    } else if (isRoundOnce()) {
                        this.messageText = LocaleController.getString(R.string.AttachOnceRound);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                        if (getMedia(this.messageOwner).ttl_seconds != 0 && !(this.messageOwner instanceof TLRPC$TL_message_secret)) {
                            this.messageText = LocaleController.getString("AttachDestructingPhoto", R.string.AttachDestructingPhoto);
                        } else if (getGroupId() != 0) {
                            this.messageText = LocaleController.getString("Album", R.string.Album);
                        } else {
                            this.messageText = LocaleController.getString("AttachPhoto", R.string.AttachPhoto);
                        }
                    } else if (isVideo() || ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) && (((getDocument() instanceof TLRPC$TL_documentEmpty) || getDocument() == null) && getMedia(this.messageOwner).ttl_seconds != 0))) {
                        if (getMedia(this.messageOwner).ttl_seconds != 0) {
                            TLRPC$Message tLRPC$Message3 = this.messageOwner;
                            if (!(tLRPC$Message3 instanceof TLRPC$TL_message_secret)) {
                                if (getMedia(tLRPC$Message3).voice) {
                                    this.messageText = LocaleController.getString(R.string.AttachVoiceExpired);
                                } else if (getMedia(this.messageOwner).round) {
                                    this.messageText = LocaleController.getString(R.string.AttachRoundExpired);
                                } else {
                                    this.messageText = LocaleController.getString(R.string.AttachDestructingVideo);
                                }
                            }
                        }
                        this.messageText = LocaleController.getString("AttachVideo", R.string.AttachVideo);
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
                        this.messageText = LocaleController.getString(R.string.UnsupportedMedia2);
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
                    String str11 = this.messageOwner.message;
                    if (str11 != null) {
                        try {
                            if (str11.length() > 200) {
                                this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_LONG_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                            } else {
                                this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                            }
                        } catch (Throwable unused) {
                            this.messageText = this.messageOwner.message;
                        }
                    } else {
                        this.messageText = str11;
                    }
                }
            }
            if (this.messageText != null) {
                this.messageText = str;
                return;
            }
            return;
        } else {
            tLRPC$User = null;
        }
        chat = null;
        if (tLRPC$User == null) {
        }
        this.drawServiceWithDefaultTypeface = false;
        this.channelJoined = false;
        tLRPC$Message = this.messageOwner;
        String str62 = "";
        if (!(tLRPC$Message instanceof TLRPC$TL_messageService)) {
        }
        if (this.messageText != null) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x0108, code lost:
        if (r0 != null) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x010c, code lost:
        if (r6.ttl_seconds == 0) goto L77;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CharSequence getMediaTitle(TLRPC$MessageMedia tLRPC$MessageMedia) {
        String publicUsername;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveaway) {
            return LocaleController.getString("BoostingGiveaway", R.string.BoostingGiveaway);
        }
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveawayResults) {
            return LocaleController.getString("BoostingGiveawayResults", R.string.BoostingGiveawayResults);
        }
        String str = null;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaStory) {
            if (tLRPC$MessageMedia.via_mention) {
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$MessageMedia.user_id));
                if (user != null && (publicUsername = UserObject.getPublicUsername(user)) != null) {
                    str = MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername + "/s/" + tLRPC$MessageMedia.id;
                }
                if (str != null) {
                    SpannableString spannableString = new SpannableString(str);
                    spannableString.setSpan(new URLSpanReplacement("https://" + str, new TextStyleSpan.TextStyleRun()), 0, spannableString.length(), 33);
                    return spannableString;
                }
                return "";
            }
            return LocaleController.getString("ForwardedStory", R.string.ForwardedStory);
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDice) {
            return getDiceEmoji();
        } else {
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                if (((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll.quiz) {
                    return LocaleController.getString("QuizPoll", R.string.QuizPoll);
                }
                return LocaleController.getString("Poll", R.string.Poll);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                if (tLRPC$MessageMedia.ttl_seconds != 0 && !(this.messageOwner instanceof TLRPC$TL_message_secret)) {
                    return LocaleController.getString("AttachDestructingPhoto", R.string.AttachDestructingPhoto);
                }
                if (getGroupId() != 0) {
                    return LocaleController.getString("Album", R.string.Album);
                }
                return LocaleController.getString("AttachPhoto", R.string.AttachPhoto);
            } else {
                if (tLRPC$MessageMedia != null) {
                    if (!isVideoDocument(tLRPC$MessageMedia.document)) {
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                            TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
                            if (!(tLRPC$Document instanceof TLRPC$TL_documentEmpty)) {
                            }
                        }
                    }
                    if (tLRPC$MessageMedia.ttl_seconds != 0 && !(this.messageOwner instanceof TLRPC$TL_message_secret)) {
                        if (tLRPC$MessageMedia.voice) {
                            return LocaleController.getString(R.string.AttachVoiceExpired);
                        }
                        if (tLRPC$MessageMedia.round) {
                            return LocaleController.getString(R.string.AttachRoundExpired);
                        }
                        return LocaleController.getString(R.string.AttachDestructingVideo);
                    }
                    return LocaleController.getString("AttachVideo", R.string.AttachVideo);
                }
                if (tLRPC$MessageMedia != null && isVoiceDocument(tLRPC$MessageMedia.document)) {
                    return LocaleController.getString("AttachAudio", R.string.AttachAudio);
                }
                if (tLRPC$MessageMedia != null && isRoundVideoDocument(tLRPC$MessageMedia.document)) {
                    return LocaleController.getString("AttachRound", R.string.AttachRound);
                }
                if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue)) {
                    return LocaleController.getString("AttachLocation", R.string.AttachLocation);
                }
                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                    return LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                }
                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaContact) {
                    return LocaleController.getString("AttachContact", R.string.AttachContact);
                }
                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                    return this.messageOwner.message;
                }
                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                    return tLRPC$MessageMedia.description;
                }
                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaUnsupported) {
                    return LocaleController.getString(R.string.UnsupportedMedia2);
                }
                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                    if (isStickerDocument(tLRPC$MessageMedia.document) || isAnimatedStickerDocument(tLRPC$MessageMedia.document, true)) {
                        String stickerChar = getStickerChar();
                        return (stickerChar == null || stickerChar.length() <= 0) ? LocaleController.getString("AttachSticker", R.string.AttachSticker) : String.format("%s %s", stickerChar, LocaleController.getString("AttachSticker", R.string.AttachSticker));
                    } else if (isMusic()) {
                        return LocaleController.getString("AttachMusic", R.string.AttachMusic);
                    } else {
                        if (isGif()) {
                            return LocaleController.getString("AttachGif", R.string.AttachGif);
                        }
                        String documentFileName = FileLoader.getDocumentFileName(tLRPC$MessageMedia.document);
                        return !TextUtils.isEmpty(documentFileName) ? documentFileName : LocaleController.getString("AttachDocument", R.string.AttachDocument);
                    }
                }
                return null;
            }
        }
    }

    public static TLRPC$MessageMedia getMedia(MessageObject messageObject) {
        TLRPC$Message tLRPC$Message;
        if (messageObject == null || (tLRPC$Message = messageObject.messageOwner) == null) {
            return null;
        }
        return getMedia(tLRPC$Message);
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

    private boolean hasNonEmojiEntities() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message != null && tLRPC$Message.entities != null) {
            for (int i = 0; i < this.messageOwner.entities.size(); i++) {
                if (!(this.messageOwner.entities.get(i) instanceof TLRPC$TL_messageEntityCustomEmoji)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setType() {
        int i;
        int i2 = this.type;
        this.type = 1000;
        this.isRoundVideoCached = 0;
        if (this.channelJoined) {
            this.type = 27;
            SharedPreferences mainSettings = MessagesController.getInstance(this.currentAccount).getMainSettings();
            this.channelJoinedExpanded = mainSettings.getBoolean("c" + getDialogId() + "_rec", true);
        } else {
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
                } else if (isMediaEmpty(false) && !isDice() && !isSponsored() && this.emojiOnlyCount >= 1 && !this.hasUnwrappedEmoji && this.messageOwner != null && !hasNonEmojiEntities()) {
                    this.type = 19;
                } else if (isMediaEmpty()) {
                    this.type = 0;
                    if (TextUtils.isEmpty(this.messageText) && this.eventId == 0) {
                        this.messageText = LocaleController.getString("EventLogOriginalCaptionEmpty", R.string.EventLogOriginalCaptionEmpty);
                    }
                } else if (hasExtendedMediaPreview()) {
                    this.type = 20;
                } else if (getMedia(this.messageOwner).ttl_seconds != 0 && ((getMedia(this.messageOwner).photo instanceof TLRPC$TL_photoEmpty) || (getDocument() instanceof TLRPC$TL_documentEmpty) || (((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) && getDocument() == null) || this.forceExpired))) {
                    this.contentType = 1;
                    this.type = 10;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveaway) {
                    this.type = 26;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveawayResults) {
                    this.type = 28;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDice) {
                    this.type = 15;
                    if (getMedia(this.messageOwner).document == null) {
                        getMedia(this.messageOwner).document = new TLRPC$TL_document();
                        getMedia(this.messageOwner).document.file_reference = new byte[0];
                        getMedia(this.messageOwner).document.mime_type = "application/x-tgsdice";
                        getMedia(this.messageOwner).document.dc_id = Integer.MIN_VALUE;
                        getMedia(this.messageOwner).document.id = -2147483648L;
                        TLRPC$TL_documentAttributeImageSize tLRPC$TL_documentAttributeImageSize = new TLRPC$TL_documentAttributeImageSize();
                        tLRPC$TL_documentAttributeImageSize.w = LiteMode.FLAG_CALLS_ANIMATIONS;
                        tLRPC$TL_documentAttributeImageSize.h = LiteMode.FLAG_CALLS_ANIMATIONS;
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
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaStory) {
                    int i3 = getMedia(this.messageOwner).via_mention ? 24 : 23;
                    this.type = i3;
                    if (i3 == 24) {
                        this.contentType = 1;
                    }
                }
            } else {
                TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent = this.currentEvent;
                if (tLRPC$TL_channelAdminLogEvent != null) {
                    TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction = tLRPC$TL_channelAdminLogEvent.action;
                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeWallpaper) {
                        TLRPC$TL_channelAdminLogEventActionChangeWallpaper tLRPC$TL_channelAdminLogEventActionChangeWallpaper = (TLRPC$TL_channelAdminLogEventActionChangeWallpaper) tLRPC$ChannelAdminLogEventAction;
                        this.contentType = 1;
                        TLRPC$WallPaper tLRPC$WallPaper = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value;
                        if ((tLRPC$WallPaper instanceof TLRPC$TL_wallPaperNoFile) && tLRPC$WallPaper.id == 0 && tLRPC$WallPaper.settings == null) {
                            this.type = 10;
                        } else {
                            this.type = 22;
                            ArrayList<TLRPC$PhotoSize> arrayList = new ArrayList<>();
                            this.photoThumbs = arrayList;
                            TLRPC$Document tLRPC$Document = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                            if (tLRPC$Document != null) {
                                arrayList.addAll(tLRPC$Document.thumbs);
                                this.photoThumbsObject = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                            }
                        }
                    }
                }
                if (tLRPC$Message instanceof TLRPC$TL_messageService) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetSameChatWallPaper) {
                        this.contentType = 1;
                        this.type = 10;
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                        this.contentType = 1;
                        this.type = 22;
                        TLRPC$TL_messageActionSetChatWallPaper tLRPC$TL_messageActionSetChatWallPaper = (TLRPC$TL_messageActionSetChatWallPaper) tLRPC$MessageAction;
                        ArrayList<TLRPC$PhotoSize> arrayList2 = new ArrayList<>();
                        this.photoThumbs = arrayList2;
                        TLRPC$Document tLRPC$Document2 = tLRPC$TL_messageActionSetChatWallPaper.wallpaper.document;
                        if (tLRPC$Document2 != null) {
                            arrayList2.addAll(tLRPC$Document2.thumbs);
                            this.photoThumbsObject = tLRPC$TL_messageActionSetChatWallPaper.wallpaper.document;
                        }
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSuggestProfilePhoto) {
                        this.contentType = 1;
                        this.type = 21;
                        ArrayList<TLRPC$PhotoSize> arrayList3 = new ArrayList<>();
                        this.photoThumbs = arrayList3;
                        arrayList3.addAll(this.messageOwner.action.photo.sizes);
                        this.photoThumbsObject = this.messageOwner.action.photo;
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                        this.type = 0;
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftCode) && ((TLRPC$TL_messageActionGiftCode) tLRPC$MessageAction).boost_peer != null) {
                        this.contentType = 1;
                        this.type = 25;
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftPremium) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftCode)) {
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
        TLRPC$WebDocument tLRPC$WebDocument = ((TLRPC$TL_messageMediaInvoice) getMedia(this.messageOwner)).webPhoto;
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
                if (tLRPC$PhotoSize != null && !(tLRPC$PhotoSize instanceof TLRPC$TL_photoSizeEmpty) && (!(tLRPC$PhotoSize.location instanceof TLRPC$TL_fileLocationUnavailable) || tLRPC$PhotoSize.bytes != null)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canPreviewDocument(TLRPC$Document tLRPC$Document) {
        String str;
        if (tLRPC$Document != null && (str = tLRPC$Document.mime_type) != null) {
            if ((isDocumentHasThumb(tLRPC$Document) && (str.equalsIgnoreCase("image/png") || str.equalsIgnoreCase("image/jpg") || str.equalsIgnoreCase("image/jpeg"))) || (Build.VERSION.SDK_INT >= 26 && str.equalsIgnoreCase("image/heic"))) {
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
        ArrayList<TLRPC$PhotoSize> arrayList8;
        if (hasExtendedMediaPreview()) {
            TLRPC$TL_messageExtendedMediaPreview tLRPC$TL_messageExtendedMediaPreview = (TLRPC$TL_messageExtendedMediaPreview) this.messageOwner.media.extended_media;
            if (!z) {
                this.photoThumbs = new ArrayList<>(Collections.singletonList(tLRPC$TL_messageExtendedMediaPreview.thumb));
            } else {
                updatePhotoSizeLocations(this.photoThumbs, Collections.singletonList(tLRPC$TL_messageExtendedMediaPreview.thumb));
            }
            this.photoThumbsObject = this.messageOwner;
            if (this.strippedThumb == null) {
                createStrippedThumb();
                return;
            }
            return;
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message instanceof TLRPC$TL_messageService) {
            TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto) {
                TLRPC$Photo tLRPC$Photo = tLRPC$MessageAction.photo;
                if (!z) {
                    this.photoThumbs = new ArrayList<>(tLRPC$Photo.sizes);
                } else {
                    ArrayList<TLRPC$PhotoSize> arrayList9 = this.photoThumbs;
                    if (arrayList9 != null && !arrayList9.isEmpty()) {
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
                if (tLRPC$Photo.dc_id != 0 && (arrayList8 = this.photoThumbs) != null) {
                    int size = arrayList8.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        TLRPC$FileLocation tLRPC$FileLocation = this.photoThumbs.get(i3).location;
                        if (tLRPC$FileLocation != null) {
                            tLRPC$FileLocation.dc_id = tLRPC$Photo.dc_id;
                            tLRPC$FileLocation.file_reference = tLRPC$Photo.file_reference;
                        }
                    }
                }
                this.photoThumbsObject = this.messageOwner.action.photo;
            }
        } else if (this.emojiAnimatedSticker != null || this.emojiAnimatedStickerId != null) {
            if (TextUtils.isEmpty(this.emojiAnimatedStickerColor) && isDocumentHasThumb(this.emojiAnimatedSticker)) {
                if (!z || (arrayList = this.photoThumbs) == null) {
                    ArrayList<TLRPC$PhotoSize> arrayList10 = new ArrayList<>();
                    this.photoThumbs = arrayList10;
                    arrayList10.addAll(this.emojiAnimatedSticker.thumbs);
                } else if (!arrayList.isEmpty()) {
                    updatePhotoSizeLocations(this.photoThumbs, this.emojiAnimatedSticker.thumbs);
                }
                this.photoThumbsObject = this.emojiAnimatedSticker;
            }
        } else if (getMedia(tLRPC$Message) != null && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaEmpty)) {
            if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                TLRPC$Photo tLRPC$Photo2 = getMedia(this.messageOwner).photo;
                if (!z || ((arrayList7 = this.photoThumbs) != null && arrayList7.size() != tLRPC$Photo2.sizes.size())) {
                    this.photoThumbs = new ArrayList<>(tLRPC$Photo2.sizes);
                } else {
                    ArrayList<TLRPC$PhotoSize> arrayList11 = this.photoThumbs;
                    if (arrayList11 != null && !arrayList11.isEmpty()) {
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
                if (isDocumentHasThumb(document)) {
                    if (!z || (arrayList6 = this.photoThumbs) == null) {
                        ArrayList<TLRPC$PhotoSize> arrayList12 = new ArrayList<>();
                        this.photoThumbs = arrayList12;
                        arrayList12.addAll(document.thumbs);
                    } else if (!arrayList6.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs, document.thumbs);
                    }
                    this.photoThumbsObject = document;
                }
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                TLRPC$Document tLRPC$Document = getMedia(this.messageOwner).game.document;
                if (tLRPC$Document != null && isDocumentHasThumb(tLRPC$Document)) {
                    if (!z) {
                        ArrayList<TLRPC$PhotoSize> arrayList13 = new ArrayList<>();
                        this.photoThumbs = arrayList13;
                        arrayList13.addAll(tLRPC$Document.thumbs);
                    } else {
                        ArrayList<TLRPC$PhotoSize> arrayList14 = this.photoThumbs;
                        if (arrayList14 != null && !arrayList14.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document.thumbs);
                        }
                    }
                    this.photoThumbsObject = tLRPC$Document;
                }
                TLRPC$Photo tLRPC$Photo3 = getMedia(this.messageOwner).game.photo;
                if (tLRPC$Photo3 != null) {
                    if (!z || (arrayList5 = this.photoThumbs2) == null) {
                        this.photoThumbs2 = new ArrayList<>(tLRPC$Photo3.sizes);
                    } else if (!arrayList5.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs2, tLRPC$Photo3.sizes);
                    }
                    this.photoThumbsObject2 = tLRPC$Photo3;
                }
                if (this.photoThumbs != null || (arrayList4 = this.photoThumbs2) == null) {
                    return;
                }
                this.photoThumbs = arrayList4;
                this.photoThumbs2 = null;
                this.photoThumbsObject = this.photoThumbsObject2;
                this.photoThumbsObject2 = null;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) {
                TLRPC$Photo tLRPC$Photo4 = getMedia(this.messageOwner).webpage.photo;
                TLRPC$Document tLRPC$Document2 = getMedia(this.messageOwner).webpage.document;
                if (tLRPC$Photo4 != null) {
                    if (!z || (arrayList3 = this.photoThumbs) == null) {
                        this.photoThumbs = new ArrayList<>(tLRPC$Photo4.sizes);
                    } else if (!arrayList3.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs, tLRPC$Photo4.sizes);
                    }
                    this.photoThumbsObject = tLRPC$Photo4;
                } else if (tLRPC$Document2 != null && isDocumentHasThumb(tLRPC$Document2)) {
                    if (!z) {
                        ArrayList<TLRPC$PhotoSize> arrayList15 = new ArrayList<>();
                        this.photoThumbs = arrayList15;
                        arrayList15.addAll(tLRPC$Document2.thumbs);
                    } else {
                        ArrayList<TLRPC$PhotoSize> arrayList16 = this.photoThumbs;
                        if (arrayList16 != null && !arrayList16.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document2.thumbs);
                        }
                    }
                    this.photoThumbsObject = tLRPC$Document2;
                }
            }
        } else {
            TLRPC$TL_sponsoredWebPage tLRPC$TL_sponsoredWebPage = this.sponsoredWebPage;
            if (tLRPC$TL_sponsoredWebPage == null || tLRPC$TL_sponsoredWebPage.photo == null) {
                return;
            }
            if (!z || (arrayList2 = this.photoThumbs) == null) {
                this.photoThumbs = new ArrayList<>(this.sponsoredWebPage.photo.sizes);
            } else if (!arrayList2.isEmpty()) {
                updatePhotoSizeLocations(this.photoThumbs, this.sponsoredWebPage.photo.sizes);
            }
            this.photoThumbsObject = this.sponsoredWebPage.photo;
            if (this.strippedThumb == null) {
                createStrippedThumb();
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
                    charSequence2 = ForumUtilities.getTopicSpannedName((TLRPC$ForumTopic) tLObject, null, false);
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
            return (!(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(tLRPC$Message).webpage == null) ? "" : FileLoader.getAttachFileName(getMedia(tLRPC$Message).webpage.document);
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

    /* JADX WARN: Removed duplicated region for block: B:57:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:79:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLinkDescription() {
        boolean z;
        int i;
        int i2;
        TLRPC$TL_webPageAttributeStory tLRPC$TL_webPageAttributeStory;
        TL_stories$StoryItem tL_stories$StoryItem;
        if (this.linkDescription != null) {
            return;
        }
        TLRPC$WebPage tLRPC$WebPage = null;
        TLRPC$WebPage tLRPC$WebPage2 = this.storyMentionWebpage;
        if (tLRPC$WebPage2 != null) {
            tLRPC$WebPage = tLRPC$WebPage2;
        } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) {
            tLRPC$WebPage = ((TLRPC$TL_messageMediaWebPage) getMedia(this.messageOwner)).webpage;
        }
        if (tLRPC$WebPage != null) {
            for (int i3 = 0; i3 < tLRPC$WebPage.attributes.size(); i3++) {
                TLRPC$WebPageAttribute tLRPC$WebPageAttribute = tLRPC$WebPage.attributes.get(i3);
                if ((tLRPC$WebPageAttribute instanceof TLRPC$TL_webPageAttributeStory) && (tL_stories$StoryItem = (tLRPC$TL_webPageAttributeStory = (TLRPC$TL_webPageAttributeStory) tLRPC$WebPageAttribute).storyItem) != null && tL_stories$StoryItem.caption != null) {
                    this.linkDescription = new SpannableStringBuilder(tLRPC$TL_webPageAttributeStory.storyItem.caption);
                    this.webPageDescriptionEntities = tLRPC$TL_webPageAttributeStory.storyItem.entities;
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (this.linkDescription == null) {
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
                if (TextUtils.isEmpty(this.linkDescription)) {
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
            } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) && getMedia(this.messageOwner).game.description != null) {
                this.linkDescription = Spannable.Factory.getInstance().newSpannable(getMedia(this.messageOwner).game.description);
            } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice) && getMedia(this.messageOwner).description != null) {
                this.linkDescription = Spannable.Factory.getInstance().newSpannable(getMedia(this.messageOwner).description);
            }
        }
        i = 0;
        if (TextUtils.isEmpty(this.linkDescription)) {
        }
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
        return !TextUtils.isEmpty(str2) ? Emoji.replaceEmoji((CharSequence) str2, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false) : str2;
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
        if (this.messageOwner != null && (isVoice() || (isRoundVideo() && TranscribeButton.isVideoTranscriptionOpen(this)))) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message.voiceTranscriptionOpen && tLRPC$Message.voiceTranscription != null && (tLRPC$Message.voiceTranscriptionFinal || TranscribeButton.isTranscribing(this))) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateCaption() {
        boolean z;
        boolean z2;
        TL_stories$StoryItem tL_stories$StoryItem;
        if ((this.caption == null || this.translated != this.captionTranslated) && !isRoundVideo()) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            String str = tLRPC$Message.message;
            ArrayList<TLRPC$MessageEntity> arrayList = tLRPC$Message.entities;
            boolean z3 = true;
            if (this.type == 23) {
                TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
                if (tLRPC$MessageMedia != null && (tL_stories$StoryItem = tLRPC$MessageMedia.storyItem) != null) {
                    str = tL_stories$StoryItem.caption;
                    arrayList = tL_stories$StoryItem.entities;
                    z = true;
                    z2 = this.translated;
                    this.captionTranslated = z2;
                    if (z2) {
                        TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = this.messageOwner.translatedText;
                        String str2 = tLRPC$TL_textWithEntities.text;
                        arrayList = tLRPC$TL_textWithEntities.entities;
                        str = str2;
                    }
                    if (!isMediaEmpty() || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) || TextUtils.isEmpty(str)) {
                        return;
                    }
                    CharSequence replaceEmoji = Emoji.replaceEmoji((CharSequence) str, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    this.caption = replaceEmoji;
                    this.caption = replaceAnimatedEmoji(replaceEmoji, arrayList, Theme.chat_msgTextPaint.getFontMetricsInt(), false);
                    boolean z4 = this.messageOwner.send_state != 0 ? false : !arrayList.isEmpty();
                    if (!z && (z4 || (this.eventId == 0 && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto_old) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto_layer68) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto_layer74) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument_old) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument_layer68) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument_layer74) && ((!isOut() || this.messageOwner.send_state == 0) && this.messageOwner.id >= 0)))) {
                        z3 = false;
                    }
                    if (z3) {
                        if (containsUrls(this.caption)) {
                            try {
                                AndroidUtilities.addLinks((Spannable) this.caption, 5);
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                        addUrlsByPattern(isOutOwner(), this.caption, true, 0, 0, true);
                    }
                    addEntitiesToText(this.caption, z3);
                    if (isVideo()) {
                        addUrlsByPattern(isOutOwner(), this.caption, true, 3, (int) getDuration(), false);
                        return;
                    } else if (isMusic() || isVoice()) {
                        addUrlsByPattern(isOutOwner(), this.caption, true, 4, (int) getDuration(), false);
                        return;
                    } else {
                        return;
                    }
                }
                arrayList = new ArrayList<>();
                str = "";
            } else if (hasExtendedMedia()) {
                TLRPC$Message tLRPC$Message2 = this.messageOwner;
                str = tLRPC$Message2.media.description;
                tLRPC$Message2.message = str;
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

    /* JADX WARN: Removed duplicated region for block: B:89:0x01ce A[Catch: Exception -> 0x0228, TryCatch #0 {Exception -> 0x0228, blocks: (B:10:0x0011, B:12:0x0015, B:13:0x001d, B:22:0x0049, B:25:0x004e, B:26:0x0051, B:28:0x0057, B:32:0x0066, B:36:0x0076, B:37:0x0078, B:49:0x0091, B:99:0x020e, B:101:0x0218, B:103:0x021b, B:104:0x0221, B:50:0x00b3, B:53:0x00d8, B:54:0x00f9, B:55:0x011a, B:58:0x0122, B:62:0x0131, B:65:0x013d, B:67:0x0147, B:68:0x0150, B:46:0x008b, B:69:0x015a, B:72:0x0199, B:77:0x01ac, B:82:0x01bb, B:84:0x01c5, B:87:0x01c9, B:89:0x01ce, B:94:0x01da, B:96:0x0208, B:95:0x01f2, B:14:0x0024, B:16:0x0028, B:17:0x0030, B:18:0x0037, B:20:0x003b, B:21:0x0043), top: B:109:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01d7  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x020c  */
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
            if (charSequence instanceof Spannable) {
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
                        } else if (i != 2) {
                            if (charSequence.charAt(start) != '/') {
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
                            if (uRLSpanNoUnderline != null) {
                                if (z3 && (clickableSpanArr = (ClickableSpan[]) spannable.getSpans(start, end, ClickableSpan.class)) != null && clickableSpanArr.length > 0) {
                                    spannable.removeSpan(clickableSpanArr[0]);
                                }
                                spannable.setSpan(uRLSpanNoUnderline, start, end, 0);
                            }
                            i4 = 1;
                        } else if (charAt == '@') {
                            uRLSpanNoUnderline = new URLSpanNoUnderline("https://twitter.com/" + charSequence.subSequence(start + 1, end).toString());
                        } else {
                            uRLSpanNoUnderline = new URLSpanNoUnderline("https://twitter.com/hashtag/" + charSequence.subSequence(start + 1, end).toString());
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
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static int[] getWebDocumentWidthAndHeight(TLRPC$WebDocument tLRPC$WebDocument) {
        int i;
        if (tLRPC$WebDocument == null) {
            return null;
        }
        int size = tLRPC$WebDocument.attributes.size();
        while (i < size) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$WebDocument.attributes.get(i);
            i = ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) ? 0 : i + 1;
            return new int[]{tLRPC$DocumentAttribute.w, tLRPC$DocumentAttribute.h};
        }
        return null;
    }

    public static double getWebDocumentDuration(TLRPC$WebDocument tLRPC$WebDocument) {
        if (tLRPC$WebDocument == null) {
            return 0.0d;
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
        return 0.0d;
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
        int webDocumentDuration = (int) getWebDocumentDuration(tLRPC$BotInlineResult.content);
        return webDocumentDuration == 0 ? (int) getWebDocumentDuration(tLRPC$BotInlineResult.thumb) : webDocumentDuration;
    }

    public boolean hasValidGroupId() {
        ArrayList<TLRPC$PhotoSize> arrayList;
        return getGroupId() != 0 && (!((arrayList = this.photoThumbs) == null || arrayList.isEmpty()) || isMusic() || isDocument());
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
        if ((charSequence instanceof Spannable) && containsUrls(charSequence)) {
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
        ArrayList<TLRPC$MessageEntity> arrayList;
        if (charSequence == null) {
            return false;
        }
        if (this.isRestrictedMessage || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaUnsupported)) {
            ArrayList arrayList2 = new ArrayList();
            TLRPC$TL_messageEntityItalic tLRPC$TL_messageEntityItalic = new TLRPC$TL_messageEntityItalic();
            tLRPC$TL_messageEntityItalic.offset = 0;
            tLRPC$TL_messageEntityItalic.length = charSequence.length();
            arrayList2.add(tLRPC$TL_messageEntityItalic);
            return addEntitiesToText(charSequence, arrayList2, isOutOwner(), true, z, z2);
        }
        if (this.translated) {
            TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = this.messageOwner.translatedText;
            arrayList = tLRPC$TL_textWithEntities == null ? null : tLRPC$TL_textWithEntities.entities;
        } else {
            arrayList = this.messageOwner.entities;
        }
        return addEntitiesToText(charSequence, arrayList, isOutOwner(), true, z, z2);
    }

    public void replaceEmojiToLottieFrame(CharSequence charSequence, int[] iArr) {
        if (charSequence instanceof Spannable) {
            Spannable spannable = (Spannable) charSequence;
            Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) spannable.getSpans(0, spannable.length(), Emoji.EmojiSpan.class);
            AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannable.getSpans(0, spannable.length(), AnimatedEmojiSpan.class);
            if (emojiSpanArr != null) {
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
        }
    }

    public Spannable replaceAnimatedEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt) {
        TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities;
        return replaceAnimatedEmoji(charSequence, (!this.translated || (tLRPC$TL_textWithEntities = this.messageOwner.translatedText) == null) ? this.messageOwner.entities : tLRPC$TL_textWithEntities.entities, fontMetricsInt, false);
    }

    public static Spannable replaceAnimatedEmoji(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, Paint.FontMetricsInt fontMetricsInt) {
        return replaceAnimatedEmoji(charSequence, arrayList, fontMetricsInt, false);
    }

    public static Spannable replaceAnimatedEmoji(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, Paint.FontMetricsInt fontMetricsInt, boolean z) {
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
                        int i3 = tLRPC$TL_messageEntityCustomEmoji.offset;
                        if (AndroidUtilities.intersect1d(i3, tLRPC$TL_messageEntityCustomEmoji.length + i3, spanStart, spanEnd)) {
                            spannableString.removeSpan(emojiSpan);
                            emojiSpanArr[i2] = null;
                        }
                    }
                }
                if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length <= spannableString.length()) {
                    int i4 = tLRPC$MessageEntity.offset;
                    AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spannableString.getSpans(i4, tLRPC$MessageEntity.length + i4, AnimatedEmojiSpan.class);
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
                    animatedEmojiSpan.top = z;
                    int i5 = tLRPC$MessageEntity.offset;
                    spannableString.setSpan(animatedEmojiSpan, i5, tLRPC$MessageEntity.length + i5, 33);
                }
            }
        }
        return spannableString;
    }

    /* JADX WARN: Removed duplicated region for block: B:132:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x022d  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0407  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x041b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0232 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, boolean z, boolean z2, boolean z3, boolean z4) {
        String str;
        int i;
        String str2;
        TextStyleSpan.TextStyleRun textStyleRun;
        int i2;
        int i3;
        boolean z5;
        int i4;
        int size;
        int i5;
        if (charSequence instanceof Spannable) {
            Spannable spannable = (Spannable) charSequence;
            URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, charSequence.length(), URLSpan.class);
            boolean z6 = uRLSpanArr != null && uRLSpanArr.length > 0;
            if (arrayList == null || arrayList.isEmpty()) {
                return z6;
            }
            byte b = z3 ? (byte) 2 : z ? (byte) 1 : (byte) 0;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList(arrayList);
            Collections.sort(arrayList3, MessageObject$$ExternalSyntheticLambda2.INSTANCE);
            int size2 = arrayList3.size();
            int i6 = 0;
            while (true) {
                str = null;
                if (i6 >= size2) {
                    break;
                }
                TLRPC$MessageEntity tLRPC$MessageEntity = (TLRPC$MessageEntity) arrayList3.get(i6);
                if (tLRPC$MessageEntity.length > 0 && (i4 = tLRPC$MessageEntity.offset) >= 0 && i4 < charSequence.length()) {
                    if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length > charSequence.length()) {
                        tLRPC$MessageEntity.length = charSequence.length() - tLRPC$MessageEntity.offset;
                    }
                    if ((!z4 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji)) && uRLSpanArr != null && uRLSpanArr.length > 0) {
                        for (int i7 = 0; i7 < uRLSpanArr.length; i7++) {
                            if (uRLSpanArr[i7] != null) {
                                int spanStart = spannable.getSpanStart(uRLSpanArr[i7]);
                                int spanEnd = spannable.getSpanEnd(uRLSpanArr[i7]);
                                int i8 = tLRPC$MessageEntity.offset;
                                if ((i8 <= spanStart && tLRPC$MessageEntity.length + i8 >= spanStart) || (i8 <= spanEnd && i8 + tLRPC$MessageEntity.length >= spanEnd)) {
                                    spannable.removeSpan(uRLSpanArr[i7]);
                                    uRLSpanArr[i7] = null;
                                }
                            }
                        }
                    }
                    if (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre)) {
                        TextStyleSpan.TextStyleRun textStyleRun2 = new TextStyleSpan.TextStyleRun();
                        int i9 = tLRPC$MessageEntity.offset;
                        textStyleRun2.start = i9;
                        textStyleRun2.end = i9 + tLRPC$MessageEntity.length;
                        if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) {
                            textStyleRun2.flags = LiteMode.FLAG_CHAT_BLUR;
                        } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) {
                            textStyleRun2.flags = 8;
                        } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) {
                            textStyleRun2.flags = 16;
                        } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) {
                            textStyleRun2.flags = 1;
                        } else {
                            if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) {
                                textStyleRun2.flags = 2;
                            } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) {
                                textStyleRun2.flags = 4;
                            } else {
                                if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) {
                                    if (z2) {
                                        textStyleRun2.flags = 64;
                                        textStyleRun2.urlEntity = tLRPC$MessageEntity;
                                    }
                                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) {
                                    if (z2) {
                                        textStyleRun2.flags = 64;
                                        textStyleRun2.urlEntity = tLRPC$MessageEntity;
                                    }
                                } else if ((!z4 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl)) && (((!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUrl) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl)) || !Browser.isPassportUrl(tLRPC$MessageEntity.url)) && (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMention) || z2))) {
                                    textStyleRun2.flags = 128;
                                    textStyleRun2.urlEntity = tLRPC$MessageEntity;
                                    if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) {
                                        textStyleRun2.flags = 128 | 1024;
                                    }
                                }
                                i6++;
                            }
                            size = arrayList2.size();
                            i5 = 0;
                            while (i5 < size) {
                                TextStyleSpan.TextStyleRun textStyleRun3 = (TextStyleSpan.TextStyleRun) arrayList2.get(i5);
                                if ((textStyleRun3.flags & LiteMode.FLAG_CHAT_BLUR) == 0 || textStyleRun2.start < textStyleRun3.start || textStyleRun2.end > textStyleRun3.end) {
                                    int i10 = textStyleRun2.start;
                                    int i11 = textStyleRun3.start;
                                    if (i10 > i11) {
                                        int i12 = textStyleRun3.end;
                                        if (i10 < i12) {
                                            if (textStyleRun2.end < i12) {
                                                TextStyleSpan.TextStyleRun textStyleRun4 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                                textStyleRun4.merge(textStyleRun3);
                                                int i13 = i5 + 1;
                                                arrayList2.add(i13, textStyleRun4);
                                                TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun(textStyleRun3);
                                                textStyleRun5.start = textStyleRun2.end;
                                                i5 = i13 + 1;
                                                size = size + 1 + 1;
                                                arrayList2.add(i5, textStyleRun5);
                                            } else {
                                                TextStyleSpan.TextStyleRun textStyleRun6 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                                textStyleRun6.merge(textStyleRun3);
                                                textStyleRun6.end = textStyleRun3.end;
                                                i5++;
                                                size++;
                                                arrayList2.add(i5, textStyleRun6);
                                            }
                                            int i14 = textStyleRun2.start;
                                            textStyleRun2.start = textStyleRun3.end;
                                            textStyleRun3.end = i14;
                                        }
                                    } else {
                                        int i15 = textStyleRun2.end;
                                        if (i11 < i15) {
                                            int i16 = textStyleRun3.end;
                                            if (i15 == i16) {
                                                textStyleRun3.merge(textStyleRun2);
                                            } else if (i15 < i16) {
                                                TextStyleSpan.TextStyleRun textStyleRun7 = new TextStyleSpan.TextStyleRun(textStyleRun3);
                                                textStyleRun7.merge(textStyleRun2);
                                                textStyleRun7.end = textStyleRun2.end;
                                                i5++;
                                                size++;
                                                arrayList2.add(i5, textStyleRun7);
                                                textStyleRun3.start = textStyleRun2.end;
                                            } else {
                                                TextStyleSpan.TextStyleRun textStyleRun8 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                                textStyleRun8.start = textStyleRun3.end;
                                                i5++;
                                                size++;
                                                arrayList2.add(i5, textStyleRun8);
                                                textStyleRun3.merge(textStyleRun2);
                                            }
                                            textStyleRun2.end = i11;
                                        }
                                    }
                                }
                                i5++;
                            }
                            if (textStyleRun2.start >= textStyleRun2.end) {
                                arrayList2.add(textStyleRun2);
                            }
                            i6++;
                        }
                        size = arrayList2.size();
                        i5 = 0;
                        while (i5 < size) {
                        }
                        if (textStyleRun2.start >= textStyleRun2.end) {
                        }
                        i6++;
                    }
                }
                i6++;
            }
            int size3 = arrayList2.size();
            boolean z7 = z6;
            int i17 = 0;
            while (i17 < size3) {
                TextStyleSpan.TextStyleRun textStyleRun9 = (TextStyleSpan.TextStyleRun) arrayList2.get(i17);
                TLRPC$MessageEntity tLRPC$MessageEntity2 = textStyleRun9.urlEntity;
                if (tLRPC$MessageEntity2 != null) {
                    int i18 = tLRPC$MessageEntity2.offset;
                    str2 = TextUtils.substring(charSequence, i18, tLRPC$MessageEntity2.length + i18);
                } else {
                    str2 = str;
                }
                TLRPC$MessageEntity tLRPC$MessageEntity3 = textStyleRun9.urlEntity;
                if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBotCommand) {
                    spannable.setSpan(new URLSpanBotCommand(str2, b, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                } else {
                    if ((tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityHashtag) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMention) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityCashtag)) {
                        textStyleRun = textStyleRun9;
                        i2 = i17;
                        i3 = 33;
                        spannable.setSpan(new URLSpanNoUnderline(str2, textStyleRun), textStyleRun.start, textStyleRun.end, 33);
                    } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityEmail) {
                        spannable.setSpan(new URLSpanReplacement("mailto:" + str2, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                    } else {
                        if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityUrl) {
                            if (!str2.toLowerCase().contains("://")) {
                                str2 = "http://" + str2;
                            }
                            if (str2 != null) {
                                str2 = str2.replaceAll("∕|⁄|%E2%81%84|%E2%88%95", "/");
                            }
                            spannable.setSpan(new URLSpanBrowser(str2, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                        } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBankCard) {
                            spannable.setSpan(new URLSpanNoUnderline("card:" + str2, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                        } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityPhone) {
                            String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str2);
                            if (str2.startsWith("+")) {
                                stripExceptNumbers = "+" + stripExceptNumbers;
                            }
                            spannable.setSpan(new URLSpanBrowser("tel:" + stripExceptNumbers, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                        } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityTextUrl) {
                            String str3 = tLRPC$MessageEntity3.url;
                            if (str3 != null) {
                                str3 = str3.replaceAll("∕|⁄|%E2%81%84|%E2%88%95", "/");
                            }
                            spannable.setSpan(new URLSpanReplacement(str3, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                        } else {
                            if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMentionName) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("");
                                i2 = i17;
                                sb.append(((TLRPC$TL_messageEntityMentionName) textStyleRun9.urlEntity).user_id);
                                spannable.setSpan(new URLSpanUserMention(sb.toString(), b, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                            } else {
                                i2 = i17;
                                if (tLRPC$MessageEntity3 instanceof TLRPC$TL_inputMessageEntityMentionName) {
                                    spannable.setSpan(new URLSpanUserMention("" + ((TLRPC$TL_inputMessageEntityMentionName) textStyleRun9.urlEntity).user_id.user_id, b, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                                } else if ((textStyleRun9.flags & 4) != 0) {
                                    i3 = 33;
                                    textStyleRun = textStyleRun9;
                                    spannable.setSpan(new URLSpanMono(spannable, textStyleRun9.start, textStyleRun9.end, b, textStyleRun9), textStyleRun.start, textStyleRun.end, 33);
                                } else {
                                    textStyleRun = textStyleRun9;
                                    i3 = 33;
                                    spannable.setSpan(new TextStyleSpan(textStyleRun), textStyleRun.start, textStyleRun.end, 33);
                                    z5 = true;
                                    if (!z5 && (textStyleRun.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                                        spannable.setSpan(new TextStyleSpan(textStyleRun), textStyleRun.start, textStyleRun.end, i3);
                                    }
                                    i17 = i2 + 1;
                                    str = null;
                                }
                            }
                            textStyleRun = textStyleRun9;
                            i3 = 33;
                        }
                        textStyleRun = textStyleRun9;
                        i2 = i17;
                        z5 = false;
                        z7 = true;
                        i3 = 33;
                        if (!z5) {
                            spannable.setSpan(new TextStyleSpan(textStyleRun), textStyleRun.start, textStyleRun.end, i3);
                        }
                        i17 = i2 + 1;
                        str = null;
                    }
                    z5 = false;
                    if (!z5) {
                    }
                    i17 = i2 + 1;
                    str = null;
                }
                textStyleRun = textStyleRun9;
                i2 = i17;
                i3 = 33;
                z5 = false;
                if (!z5) {
                }
                i17 = i2 + 1;
                str = null;
            }
            int size4 = arrayList3.size();
            for (int i19 = 0; i19 < size4; i19++) {
                TLRPC$MessageEntity tLRPC$MessageEntity4 = (TLRPC$MessageEntity) arrayList3.get(i19);
                if (tLRPC$MessageEntity4.length > 0 && (i = tLRPC$MessageEntity4.offset) >= 0 && i < charSequence.length()) {
                    if (tLRPC$MessageEntity4.offset + tLRPC$MessageEntity4.length > charSequence.length()) {
                        tLRPC$MessageEntity4.length = charSequence.length() - tLRPC$MessageEntity4.offset;
                    }
                    if (tLRPC$MessageEntity4 instanceof TLRPC$TL_messageEntityBlockquote) {
                        int i20 = tLRPC$MessageEntity4.offset;
                        QuoteSpan.putQuote(spannable, i20, tLRPC$MessageEntity4.length + i20);
                    } else if (tLRPC$MessageEntity4 instanceof TLRPC$TL_messageEntityPre) {
                        int i21 = tLRPC$MessageEntity4.offset;
                        int i22 = tLRPC$MessageEntity4.length + i21;
                        spannable.setSpan(new CodeHighlighting.Span(true, 0, null, tLRPC$MessageEntity4.language, spannable.subSequence(i21, i22).toString()), i21, i22, 33);
                    }
                }
            }
            return z7;
        }
        return false;
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
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        if (this.isRepostPreview) {
            return false;
        }
        if (this.isSaved) {
            long j = UserConfig.getInstance(this.currentAccount).clientUserId;
            long savedDialogId = getSavedDialogId(j, this.messageOwner);
            if (savedDialogId == j || savedDialogId == UserObject.ANONYMOUS || (tLRPC$Message = this.messageOwner) == null || (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) == null) {
                return false;
            }
            return (tLRPC$MessageFwdHeader.from_id == null && tLRPC$MessageFwdHeader.saved_from_id == null) ? false : true;
        } else if (this.type == 27 || isSponsored() || this.hasCode || this.preview || this.scheduled || this.eventId != 0) {
            return false;
        } else {
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            if (tLRPC$Message2.noforwards) {
                return false;
            }
            if (tLRPC$Message2.fwd_from == null || isOutOwner() || this.messageOwner.fwd_from.saved_from_peer == null || getDialogId() != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                int i2 = this.type;
                if (i2 != 13 && i2 != 15 && i2 != 19) {
                    TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2 = this.messageOwner.fwd_from;
                    if (tLRPC$MessageFwdHeader2 != null && (tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerChannel) && !isOutOwner()) {
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
                                long j2 = tLRPC$Peer.channel_id;
                                if (j2 != 0) {
                                    tLRPC$Chat = getChat(null, null, j2);
                                }
                            }
                            return ChatObject.isChannel(tLRPC$Chat) && tLRPC$Chat.megagroup && ChatObject.isPublic(tLRPC$Chat) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaContact) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeo);
                        }
                    } else {
                        TLRPC$Message tLRPC$Message3 = this.messageOwner;
                        if ((tLRPC$Message3.from_id instanceof TLRPC$TL_peerChannel) || tLRPC$Message3.post) {
                            if ((getMedia(tLRPC$Message3) instanceof TLRPC$TL_messageMediaWebPage) && !isOutOwner()) {
                                return true;
                            }
                            if (isSupergroup()) {
                                return false;
                            }
                            TLRPC$Message tLRPC$Message4 = this.messageOwner;
                            if (tLRPC$Message4.peer_id.channel_id != 0 && ((tLRPC$Message4.via_bot_id == 0 && tLRPC$Message4.reply_to == null) || ((i = this.type) != 13 && i != 15))) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        }
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
        if (this.hasCode && !this.isSaved) {
            i = this.generatedWithMinSize - AndroidUtilities.dp(60.0f);
            if (needDrawAvatarInternal() && !isOutOwner() && !this.messageOwner.isThreadMessage) {
                i -= AndroidUtilities.dp(52.0f);
            }
        } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_background".equals(getMedia(this.messageOwner).webpage.type)) {
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
            int dp = this.generatedWithMinSize - AndroidUtilities.dp(80.0f);
            if (needDrawAvatarInternal() && !isOutOwner() && !this.messageOwner.isThreadMessage) {
                dp -= AndroidUtilities.dp(52.0f);
            }
            if (needDrawShareButton() && (this.isSaved || !isOutOwner())) {
                dp -= AndroidUtilities.dp((this.isSaved && isOutOwner()) ? 40.0f : 10.0f);
            }
            i = getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame ? dp - AndroidUtilities.dp(10.0f) : dp;
        }
        int i2 = this.emojiOnlyCount;
        if (i2 >= 1) {
            int i3 = this.totalAnimatedEmojiCount;
            if (i3 <= 100) {
                return i2 - i3 < (SharedConfig.getDevicePerformanceClass() < 2 ? 50 : 100) ? (hasValidReplyMessageObject() || isForwarded()) ? Math.min(i, (int) (this.generatedWithMinSize * 0.65f)) : i : i;
            }
            return i;
        }
        return i;
    }

    public void applyTimestampsHighlightForReplyMsg() {
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject == null) {
            return;
        }
        if (messageObject.isYouTubeVideo()) {
            addUrlsByPattern(isOutOwner(), this.messageText, false, 3, ConnectionsManager.DEFAULT_DATACENTER_ID, false);
        } else if (messageObject.isVideo()) {
            addUrlsByPattern(isOutOwner(), this.messageText, false, 3, (int) messageObject.getDuration(), false);
        } else if (messageObject.isMusic() || messageObject.isVoice()) {
            addUrlsByPattern(isOutOwner(), this.messageText, false, 4, (int) messageObject.getDuration(), false);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0060, code lost:
        if (r9.messageOwner.send_state == 0) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0066, code lost:
        if (r9.messageOwner.id >= 0) goto L35;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean applyEntities() {
        TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities;
        generateLinkDescription();
        ArrayList<TLRPC$MessageEntity> arrayList = (!this.translated || (tLRPC$TL_textWithEntities = this.messageOwner.translatedText) == null) ? this.messageOwner.entities : tLRPC$TL_textWithEntities.entities;
        spoilLoginCode();
        boolean z = false;
        if (!(this.messageOwner.send_state != 0 ? false : !arrayList.isEmpty())) {
            if (this.eventId == 0) {
                TLRPC$Message tLRPC$Message = this.messageOwner;
                if (!(tLRPC$Message instanceof TLRPC$TL_message_old)) {
                    if (!(tLRPC$Message instanceof TLRPC$TL_message_old2)) {
                        if (!(tLRPC$Message instanceof TLRPC$TL_message_old3)) {
                            if (!(tLRPC$Message instanceof TLRPC$TL_message_old4)) {
                                if (!(tLRPC$Message instanceof TLRPC$TL_messageForwarded_old)) {
                                    if (!(tLRPC$Message instanceof TLRPC$TL_messageForwarded_old2)) {
                                        if (!(tLRPC$Message instanceof TLRPC$TL_message_secret)) {
                                            if (!(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaInvoice)) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public static StaticLayout makeStaticLayout(CharSequence charSequence, TextPaint textPaint, int i, float f, float f2, boolean z) {
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 24) {
            boolean z2 = true;
            StaticLayout.Builder alignment = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, i).setLineSpacing(f2, f).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL);
            if (z) {
                alignment.setIncludePad(false);
                if (i2 >= 28) {
                    alignment.setUseLineSpacingFromFallbacks(false);
                }
            }
            StaticLayout build = alignment.build();
            int i3 = 0;
            while (true) {
                if (i3 >= build.getLineCount()) {
                    z2 = false;
                    break;
                } else if (build.getLineRight(i3) > i) {
                    break;
                } else {
                    i3++;
                }
            }
            if (z2) {
                StaticLayout.Builder alignment2 = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, i).setLineSpacing(f2, f).setBreakStrategy(0).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL);
                if (z) {
                    alignment2.setIncludePad(false);
                    if (Build.VERSION.SDK_INT >= 28) {
                        alignment2.setUseLineSpacingFromFallbacks(false);
                    }
                }
                return alignment2.build();
            }
            return build;
        }
        return new StaticLayout(charSequence, textPaint, i, Layout.Alignment.ALIGN_NORMAL, f, f2, false);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(16:258|259|260|261|262|(1:264)(11:294|(1:296)|266|267|268|(1:270)|271|(2:273|(3:275|(5:279|280|(1:285)|282|283)|284))(1:291)|290|(1:289)(6:277|279|280|(0)|282|283)|284)|265|266|267|268|(0)|271|(0)(0)|290|(0)(0)|284) */
    /* JADX WARN: Can't wrap try/catch for region: R(46:143|(1:145)(1:411)|146|(1:148)(1:410)|149|(1:151)|(1:153)|(1:409)(1:158)|159|(1:408)(1:166)|167|(2:169|(2:(1:391)|392)(1:172))(2:393|(7:395|(1:397)(1:407)|398|(1:400)(1:406)|401|(1:403)(1:405)|404))|173|(3:175|(1:177)(2:385|(1:387)(1:388))|178)(1:389)|179|(1:181)(2:381|(1:383)(29:384|183|(5:185|(7:191|(1:193)(1:203)|194|(1:196)(1:202)|197|(1:199)(1:201)|200)|204|(2:206|(1:(2:209|(1:211))(1:212))(1:213))|214)(3:355|(2:357|358)(9:359|360|361|(1:376)(1:365)|366|(1:368)(1:375)|369|(1:373)|374)|322)|215|216|217|(1:221)|222|223|224|225|(1:227)(17:344|(1:346)|229|(1:231)|232|(1:234)|235|(3:237|(8:239|240|241|242|243|244|246|247)|253)|254|(6:256|(16:258|259|260|261|262|(1:264)(11:294|(1:296)|266|267|268|(1:270)|271|(2:273|(3:275|(5:279|280|(1:285)|282|283)|284))(1:291)|290|(1:289)(6:277|279|280|(0)|282|283)|284)|265|266|267|268|(0)|271|(0)(0)|290|(0)(0)|284)|299|300|(1:(1:303))(2:(1:329)|330)|304)(3:331|(5:333|(1:335)(1:342)|336|(1:338)(1:341)|339)(1:343)|340)|305|(3:307|(1:309)(1:311)|310)|312|(1:327)(3:316|(1:318)(3:323|(1:325)|326)|319)|320|321|322)|228|229|(0)|232|(0)|235|(0)|254|(0)(0)|305|(0)|312|(1:314)|327|320|321|322))|182|183|(0)(0)|215|216|217|(2:219|221)|222|223|224|225|(0)(0)|228|229|(0)|232|(0)|235|(0)|254|(0)(0)|305|(0)|312|(0)|327|320|321|322|141) */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x050d, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x050e, code lost:
        if (r4 == 0) goto L353;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0510, code lost:
        r36.textXOffset = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0513, code lost:
        org.telegram.messenger.FileLog.e(r0);
        r14 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x0520, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0521, code lost:
        org.telegram.messenger.FileLog.e(r0);
        r0 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x05d4, code lost:
        r13 = 0.0f;
     */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0242 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0249  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x025d  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x026b  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x02c1  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x047d  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0529  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x0534  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x054d  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x0552  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x056a  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0598  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x05dc  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x05e3  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x05f8  */
    /* JADX WARN: Removed duplicated region for block: B:360:0x060d  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x066e  */
    /* JADX WARN: Removed duplicated region for block: B:389:0x06b7  */
    /* JADX WARN: Removed duplicated region for block: B:396:0x06e5  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x074a  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x061b A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:470:0x061b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x011f  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0139  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x014b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLayout(TLRPC$User tLRPC$User) {
        int dp;
        TextPaint textPaint;
        SpannableStringBuilder spannableStringBuilder;
        int dp2;
        boolean z;
        ArrayList arrayList;
        int i;
        boolean z2;
        int i2;
        int i3;
        TextPaint textPaint2;
        int i4;
        int dp3;
        boolean z3;
        TextPaint textPaint3;
        ArrayList arrayList2;
        CharSequence charSequence;
        int i5;
        SpannableString valueOf;
        int lineCount;
        float dp4;
        int ceil;
        int i6;
        float f;
        int i7;
        Text text;
        int i8;
        StaticLayout staticLayout;
        float f2;
        float f3;
        CharSequence charSequence2;
        int dp5;
        float f4;
        int i9;
        int i10;
        StaticLayout staticLayout2;
        float lineLeft;
        SpannableString spannableString;
        TextPaint textPaint4;
        int i11 = this.type;
        if ((i11 != 0 && i11 != 19 && i11 != 24) || this.messageOwner.peer_id == null || TextUtils.isEmpty(this.messageText)) {
            return;
        }
        applyEntities();
        TLRPC$Message tLRPC$Message = this.messageOwner;
        boolean z4 = false;
        boolean z5 = tLRPC$Message != null && tLRPC$Message.noforwards;
        if (!z5) {
            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()));
            z5 = chat != null && chat.noforwards;
        }
        boolean z6 = z5;
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
            if (this.hasSingleQuote) {
                dp = AndroidUtilities.dp(32.0f);
            } else {
                if (this.hasSingleCode) {
                    dp = AndroidUtilities.dp(15.0f);
                }
                if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame)) {
                    textPaint = Theme.chat_msgGameTextPaint;
                } else {
                    textPaint = Theme.chat_msgTextPaint;
                }
                CharSequence charSequence6 = this.messageText;
                StaticLayout makeStaticLayout = makeStaticLayout(charSequence6, textPaint, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f, this.emojiOnlyCount <= 0);
                spannableStringBuilder = charSequence6;
                if (this.isRepostPreview) {
                    int i12 = 22;
                    if (this.type != 0) {
                        i12 = hasValidGroupId() ? 7 : 12;
                    }
                    if (isWebpage()) {
                        i12 -= 8;
                    }
                    spannableStringBuilder = charSequence6;
                    if (makeStaticLayout.getLineCount() > i12) {
                        String string = LocaleController.getString(R.string.ReadMore);
                        int ceil2 = (int) Math.ceil(textPaint.measureText("… " + string) + AndroidUtilities.dp(1.0f));
                        float f5 = 0.0f;
                        for (int i13 = 0; i13 < i12; i13++) {
                            f5 = Math.max(f5, makeStaticLayout.getLineRight(i13));
                        }
                        int i14 = i12 - 1;
                        int lineStart = makeStaticLayout.getLineStart(i14);
                        int lineEnd = makeStaticLayout.getLineEnd(i14) - 1;
                        while (lineEnd >= lineStart && makeStaticLayout.getPrimaryHorizontal(lineEnd) >= f5 - ceil2) {
                            lineEnd--;
                        }
                        while (lineEnd >= lineStart && !Character.isWhitespace(charSequence6.charAt(lineEnd))) {
                            lineEnd--;
                        }
                        SpannableStringBuilder append = new SpannableStringBuilder(charSequence6.subSequence(0, lineEnd)).append((CharSequence) "… ").append((CharSequence) string);
                        append.setSpan(new CharacterStyle() { // from class: org.telegram.messenger.MessageObject.2
                            @Override // android.text.style.CharacterStyle
                            public void updateDrawState(TextPaint textPaint5) {
                                textPaint5.setColor(Theme.chat_msgTextPaint.linkColor);
                            }
                        }, append.length() - string.length(), append.length(), 33);
                        try {
                            makeStaticLayout = makeStaticLayout(append, textPaint, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount >= 4 ? -1.0f : 0.0f, this.emojiOnlyCount > 0);
                            spannableStringBuilder = append;
                        } catch (Exception e) {
                            FileLog.e(e);
                            return;
                        }
                    }
                }
                if (!this.hasSingleQuote) {
                    dp2 = AndroidUtilities.dp(32.0f);
                } else {
                    if (this.hasSingleCode) {
                        dp2 = AndroidUtilities.dp(15.0f);
                    }
                    int i15 = maxMessageTextWidth;
                    this.textHeight = 0;
                    int lineCount2 = makeStaticLayout.getLineCount();
                    int i16 = this.totalAnimatedEmojiCount;
                    int i17 = i16 >= 50 ? 5 : 10;
                    z = Build.VERSION.SDK_INT < 24 && i16 < 50;
                    int ceil3 = z ? 1 : (int) Math.ceil(lineCount2 / i17);
                    arrayList = new ArrayList();
                    if (!(spannableStringBuilder instanceof Spanned) && (this.hasQuote || this.hasCode)) {
                        cutIntoRanges(spannableStringBuilder, arrayList);
                    } else if (!z || ceil3 == 1) {
                        z4 = false;
                        arrayList.add(new TextRange(0, makeStaticLayout.getText().length()));
                    } else {
                        int i18 = 0;
                        int i19 = 0;
                        while (i18 < ceil3) {
                            int min = z ? lineCount2 : Math.min(i17, lineCount2 - i19);
                            int lineStart2 = makeStaticLayout.getLineStart(i19);
                            int i20 = min + i19;
                            int lineEnd2 = makeStaticLayout.getLineEnd(i20 - 1);
                            if (lineEnd2 >= lineStart2) {
                                arrayList.add(new TextRange(lineStart2, lineEnd2));
                                i19 = i20;
                            }
                            i18++;
                            z4 = false;
                        }
                    }
                    int size = arrayList.size();
                    this.hasCodeAtTop = z4;
                    this.hasCodeAtBottom = z4;
                    this.hasQuoteAtBottom = z4;
                    this.hasSingleQuote = z4;
                    this.hasSingleCode = z4;
                    i = 0;
                    float f6 = 0.0f;
                    float f7 = 0.0f;
                    CharSequence charSequence7 = spannableStringBuilder;
                    while (i < arrayList.size()) {
                        TextLayoutBlock textLayoutBlock = new TextLayoutBlock();
                        TextRange textRange = (TextRange) arrayList.get(i);
                        textLayoutBlock.code = textRange.code;
                        textLayoutBlock.quote = textRange.quote;
                        textLayoutBlock.first = i == 0;
                        boolean z7 = i == arrayList.size() - 1;
                        textLayoutBlock.last = z7;
                        boolean z8 = textLayoutBlock.first;
                        if (z8) {
                            this.hasCodeAtTop = textLayoutBlock.code;
                        }
                        if (z7) {
                            this.hasQuoteAtBottom = textLayoutBlock.quote;
                            this.hasCodeAtBottom = textLayoutBlock.code;
                        }
                        this.hasSingleQuote = z8 && z7 && textLayoutBlock.quote;
                        this.hasSingleCode = z8 && z7 && !textLayoutBlock.quote && textLayoutBlock.code;
                        if (textLayoutBlock.quote) {
                            if (z8 && z7) {
                                int dp6 = AndroidUtilities.dp(6.0f);
                                textLayoutBlock.padBottom = dp6;
                                textLayoutBlock.padTop = dp6;
                            } else {
                                textLayoutBlock.padTop = AndroidUtilities.dp(z8 ? 8.0f : 6.0f);
                                textLayoutBlock.padBottom = AndroidUtilities.dp(7.0f);
                            }
                        } else if (textLayoutBlock.code) {
                            textLayoutBlock.layoutCode(textRange.language, textRange.end - textRange.start, z6);
                            textLayoutBlock.padTop = AndroidUtilities.dp(4.0f) + textLayoutBlock.languageHeight + (textLayoutBlock.first ? 0 : AndroidUtilities.dp(5.0f));
                            textLayoutBlock.padBottom = AndroidUtilities.dp(4.0f) + (textLayoutBlock.last ? 0 : AndroidUtilities.dp(7.0f)) + (textLayoutBlock.hasCodeCopyButton ? AndroidUtilities.dp(38.0f) : 0);
                        }
                        if (textLayoutBlock.code) {
                            int i21 = textRange.end - textRange.start;
                            if (i21 > 220) {
                                textPaint4 = Theme.chat_msgTextCode3Paint;
                            } else if (i21 > 80) {
                                textPaint4 = Theme.chat_msgTextCode2Paint;
                            } else {
                                textPaint4 = Theme.chat_msgTextCodePaint;
                            }
                            textPaint2 = textPaint4;
                        } else {
                            textPaint2 = textPaint;
                        }
                        CharSequence subSequence = charSequence7.subSequence(textRange.start, textRange.end);
                        if (textLayoutBlock.quote) {
                            dp3 = i15 - AndroidUtilities.dp(24.0f);
                        } else if (textLayoutBlock.code) {
                            dp3 = i15 - AndroidUtilities.dp(15.0f);
                        } else {
                            i4 = i15;
                            if (size != 1) {
                                if (textLayoutBlock.code && !textLayoutBlock.quote && (makeStaticLayout.getText() instanceof Spannable)) {
                                    if (!TextUtils.isEmpty(textRange.language)) {
                                        spannableString = CodeHighlighting.getHighlighted(subSequence.toString(), textRange.language);
                                    } else {
                                        spannableString = new SpannableString(subSequence.toString());
                                    }
                                    makeStaticLayout = makeStaticLayout(spannableString, textPaint2, i4, 1.0f, this.totalAnimatedEmojiCount >= 4 ? -1.0f : 0.0f, this.emojiOnlyCount > 0);
                                }
                                textLayoutBlock.textLayout = makeStaticLayout;
                                textLayoutBlock.textYOffset = 0.0f;
                                textLayoutBlock.charactersOffset = 0;
                                textLayoutBlock.charactersEnd = makeStaticLayout.getText().length();
                                int height = makeStaticLayout.getHeight();
                                textLayoutBlock.height = height;
                                int i22 = textLayoutBlock.padTop + height + textLayoutBlock.padBottom;
                                this.textHeight = i22;
                                int i23 = this.emojiOnlyCount;
                                if (i23 != 0) {
                                    if (i23 == 1) {
                                        this.textHeight = i22 - AndroidUtilities.dp(5.3f);
                                        textLayoutBlock.textYOffset -= AndroidUtilities.dp(5.3f);
                                    } else if (i23 == 2) {
                                        this.textHeight = i22 - AndroidUtilities.dp(4.5f);
                                        textLayoutBlock.textYOffset -= AndroidUtilities.dp(4.5f);
                                    } else if (i23 == 3) {
                                        this.textHeight = i22 - AndroidUtilities.dp(4.2f);
                                        textLayoutBlock.textYOffset -= AndroidUtilities.dp(4.2f);
                                    }
                                }
                            } else {
                                int i24 = textRange.start;
                                int i25 = textRange.end;
                                if (i25 < i24) {
                                    z3 = z6;
                                    textPaint3 = textPaint;
                                    arrayList2 = arrayList;
                                    charSequence = charSequence7;
                                    i5 = i15;
                                } else {
                                    textLayoutBlock.charactersOffset = i24;
                                    textLayoutBlock.charactersEnd = i25;
                                    try {
                                        if (textLayoutBlock.code && !textLayoutBlock.quote) {
                                            valueOf = CodeHighlighting.getHighlighted(subSequence.toString(), textRange.language);
                                        } else {
                                            valueOf = SpannableString.valueOf(subSequence);
                                        }
                                        StaticLayout makeStaticLayout2 = makeStaticLayout(valueOf, textPaint2, i4, 1.0f, this.totalAnimatedEmojiCount >= 4 ? -1.0f : 0.0f, false);
                                        textLayoutBlock.textLayout = makeStaticLayout2;
                                        textLayoutBlock.textYOffset = f7;
                                        if (i != 0 && this.emojiOnlyCount <= 0) {
                                            textLayoutBlock.height = (int) (f7 - f6);
                                        }
                                        int height2 = makeStaticLayout2.getHeight();
                                        textLayoutBlock.height = height2;
                                        this.textHeight += textLayoutBlock.padTop + height2 + textLayoutBlock.padBottom;
                                        f6 = textLayoutBlock.textYOffset;
                                    } catch (Exception e2) {
                                        z3 = z6;
                                        textPaint3 = textPaint;
                                        arrayList2 = arrayList;
                                        charSequence = charSequence7;
                                        i5 = i15;
                                        FileLog.e(e2);
                                    }
                                }
                                i++;
                                i15 = i5;
                                z6 = z3;
                                textPaint = textPaint3;
                                arrayList = arrayList2;
                                charSequence7 = charSequence;
                            }
                            float f8 = f7 + textLayoutBlock.padTop + textLayoutBlock.height + textLayoutBlock.padBottom;
                            this.textLayoutBlocks.add(textLayoutBlock);
                            lineCount = textLayoutBlock.textLayout.getLineCount();
                            lineLeft = textLayoutBlock.textLayout.getLineLeft(lineCount - 1);
                            if (i == 0 && lineLeft >= 0.0f) {
                                this.textXOffset = lineLeft;
                            }
                            float f9 = lineLeft;
                            float f10 = textLayoutBlock.textLayout.getLineWidth(lineCount - 1);
                            if (!textLayoutBlock.quote) {
                                z3 = z6;
                                dp4 = AndroidUtilities.dp(32.0f);
                            } else {
                                z3 = z6;
                                if (textLayoutBlock.code) {
                                    dp4 = AndroidUtilities.dp(15.0f);
                                }
                                TextPaint textPaint5 = textPaint;
                                ceil = (int) Math.ceil(f10);
                                if (ceil > i15 + 80) {
                                    ceil = i15;
                                }
                                i6 = size - 1;
                                if (i == i6) {
                                    this.lastLineWidth = ceil;
                                }
                                float f11 = ceil;
                                float f12 = f6;
                                textPaint3 = textPaint5;
                                int ceil4 = (int) Math.ceil(f11 + Math.max(0.0f, f9));
                                if (textLayoutBlock.quote) {
                                    textLayoutBlock.maxRight = 0.0f;
                                    int i26 = 0;
                                    while (i26 < lineCount) {
                                        int i27 = ceil;
                                        try {
                                            staticLayout2 = makeStaticLayout;
                                            try {
                                                textLayoutBlock.maxRight = Math.max(textLayoutBlock.maxRight, textLayoutBlock.textLayout.getLineRight(i26));
                                            } catch (Exception unused) {
                                                textLayoutBlock.maxRight = this.textWidth;
                                                i26++;
                                                ceil = i27;
                                                makeStaticLayout = staticLayout2;
                                            }
                                        } catch (Exception unused2) {
                                            staticLayout2 = makeStaticLayout;
                                        }
                                        i26++;
                                        ceil = i27;
                                        makeStaticLayout = staticLayout2;
                                    }
                                }
                                int i28 = ceil;
                                StaticLayout staticLayout3 = makeStaticLayout;
                                if (lineCount > 1) {
                                    arrayList2 = arrayList;
                                    int i29 = i28;
                                    int i30 = 0;
                                    boolean z9 = false;
                                    float f13 = 0.0f;
                                    float f14 = 0.0f;
                                    int i31 = ceil4;
                                    CharSequence charSequence8 = charSequence7;
                                    while (i30 < lineCount) {
                                        int i32 = lineCount;
                                        try {
                                            f3 = textLayoutBlock.textLayout.getLineWidth(i30);
                                            f2 = f8;
                                        } catch (Exception unused3) {
                                            f2 = f8;
                                            f3 = 0.0f;
                                        }
                                        if (textLayoutBlock.quote) {
                                            charSequence2 = charSequence8;
                                            dp5 = AndroidUtilities.dp(32.0f);
                                        } else {
                                            charSequence2 = charSequence8;
                                            if (textLayoutBlock.code) {
                                                dp5 = AndroidUtilities.dp(15.0f);
                                            }
                                            f4 = textLayoutBlock.textLayout.getLineLeft(i30);
                                            if (f3 > i15 + 20) {
                                                f3 = i15;
                                                f4 = 0.0f;
                                            }
                                            if (f4 > 0.0f) {
                                                i9 = i15;
                                                if (textLayoutBlock.textLayout.getParagraphDirection(i30) != -1) {
                                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                                    i10 = 1;
                                                    if (z9 && f4 == 0.0f) {
                                                        try {
                                                            if (textLayoutBlock.textLayout.getParagraphDirection(i30) != i10) {
                                                            }
                                                        } catch (Exception unused4) {
                                                        }
                                                        z9 = true;
                                                    }
                                                    f14 = Math.max(f14, f3);
                                                    float f15 = f4 + f3;
                                                    float max = Math.max(f13, f15);
                                                    i29 = Math.max(i29, (int) Math.ceil(f3));
                                                    i31 = Math.max(i31, (int) Math.ceil(f15));
                                                    i30++;
                                                    z9 = z9;
                                                    f13 = max;
                                                    lineCount = i32;
                                                    f8 = f2;
                                                    charSequence8 = charSequence2;
                                                    i15 = i9;
                                                }
                                            } else {
                                                i9 = i15;
                                            }
                                            this.textXOffset = Math.min(this.textXOffset, f4);
                                            i10 = 1;
                                            textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                            this.hasRtl = true;
                                            if (z9) {
                                                if (textLayoutBlock.textLayout.getParagraphDirection(i30) != i10) {
                                                }
                                                z9 = true;
                                            }
                                            f14 = Math.max(f14, f3);
                                            float f152 = f4 + f3;
                                            float max2 = Math.max(f13, f152);
                                            i29 = Math.max(i29, (int) Math.ceil(f3));
                                            i31 = Math.max(i31, (int) Math.ceil(f152));
                                            i30++;
                                            z9 = z9;
                                            f13 = max2;
                                            lineCount = i32;
                                            f8 = f2;
                                            charSequence8 = charSequence2;
                                            i15 = i9;
                                        }
                                        f3 += dp5;
                                        f4 = textLayoutBlock.textLayout.getLineLeft(i30);
                                        if (f3 > i15 + 20) {
                                        }
                                        if (f4 > 0.0f) {
                                        }
                                        this.textXOffset = Math.min(this.textXOffset, f4);
                                        i10 = 1;
                                        textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                        this.hasRtl = true;
                                        if (z9) {
                                        }
                                        f14 = Math.max(f14, f3);
                                        float f1522 = f4 + f3;
                                        float max22 = Math.max(f13, f1522);
                                        i29 = Math.max(i29, (int) Math.ceil(f3));
                                        i31 = Math.max(i31, (int) Math.ceil(f1522));
                                        i30++;
                                        z9 = z9;
                                        f13 = max22;
                                        lineCount = i32;
                                        f8 = f2;
                                        charSequence8 = charSequence2;
                                        i15 = i9;
                                    }
                                    int i33 = i15;
                                    f = f8;
                                    charSequence = charSequence8;
                                    if (!z9) {
                                        if (i == i6) {
                                            this.lastLineWidth = i29;
                                        }
                                        f13 = f14;
                                    } else if (i == i6) {
                                        this.lastLineWidth = ceil4;
                                    }
                                    this.textWidth = Math.max(this.textWidth, (int) Math.ceil(f13));
                                    ceil4 = i31;
                                    i5 = i33;
                                } else {
                                    int i34 = i15;
                                    arrayList2 = arrayList;
                                    f = f8;
                                    charSequence = charSequence7;
                                    if (f9 > 0.0f) {
                                        float min2 = Math.min(this.textXOffset, f9);
                                        this.textXOffset = min2;
                                        i7 = min2 == 0.0f ? (int) (f11 + f9) : i28;
                                        this.hasRtl = size != 1;
                                        textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                    } else {
                                        textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                        i7 = i28;
                                    }
                                    i5 = i34;
                                    this.textWidth = Math.max(this.textWidth, Math.min(i5, i7));
                                }
                                text = textLayoutBlock.languageLayout;
                                if (text != null) {
                                    this.textWidth = (int) Math.max(this.textWidth, Math.min(text.getCurrentWidth() + AndroidUtilities.dp(15.0f), textLayoutBlock.textLayout == null ? 0.0f : staticLayout.getWidth()));
                                }
                                textLayoutBlock.spoilers.clear();
                                if (this.isSpoilersRevealed && !this.spoiledLoginCode) {
                                    if (textLayoutBlock.quote) {
                                        i8 = ceil4 - AndroidUtilities.dp(32.0f);
                                    } else {
                                        if (textLayoutBlock.code) {
                                            ceil4 -= AndroidUtilities.dp(15.0f);
                                        }
                                        i8 = ceil4;
                                    }
                                    SpoilerEffect.addSpoilers(null, textLayoutBlock.textLayout, -1, i8, null, textLayoutBlock.spoilers);
                                }
                                f6 = f12;
                                makeStaticLayout = staticLayout3;
                                f7 = f;
                                i++;
                                i15 = i5;
                                z6 = z3;
                                textPaint = textPaint3;
                                arrayList = arrayList2;
                                charSequence7 = charSequence;
                            }
                            f10 += dp4;
                            TextPaint textPaint52 = textPaint;
                            ceil = (int) Math.ceil(f10);
                            if (ceil > i15 + 80) {
                            }
                            i6 = size - 1;
                            if (i == i6) {
                            }
                            float f112 = ceil;
                            float f122 = f6;
                            textPaint3 = textPaint52;
                            int ceil42 = (int) Math.ceil(f112 + Math.max(0.0f, f9));
                            if (textLayoutBlock.quote) {
                            }
                            int i282 = ceil;
                            StaticLayout staticLayout32 = makeStaticLayout;
                            if (lineCount > 1) {
                            }
                            text = textLayoutBlock.languageLayout;
                            if (text != null) {
                            }
                            textLayoutBlock.spoilers.clear();
                            if (this.isSpoilersRevealed) {
                            }
                            f6 = f122;
                            makeStaticLayout = staticLayout32;
                            f7 = f;
                            i++;
                            i15 = i5;
                            z6 = z3;
                            textPaint = textPaint3;
                            arrayList = arrayList2;
                            charSequence7 = charSequence;
                        }
                        i4 = dp3;
                        if (size != 1) {
                        }
                        float f82 = f7 + textLayoutBlock.padTop + textLayoutBlock.height + textLayoutBlock.padBottom;
                        this.textLayoutBlocks.add(textLayoutBlock);
                        lineCount = textLayoutBlock.textLayout.getLineCount();
                        lineLeft = textLayoutBlock.textLayout.getLineLeft(lineCount - 1);
                        if (i == 0) {
                            this.textXOffset = lineLeft;
                        }
                        float f92 = lineLeft;
                        float f102 = textLayoutBlock.textLayout.getLineWidth(lineCount - 1);
                        if (!textLayoutBlock.quote) {
                        }
                        f102 += dp4;
                        TextPaint textPaint522 = textPaint;
                        ceil = (int) Math.ceil(f102);
                        if (ceil > i15 + 80) {
                        }
                        i6 = size - 1;
                        if (i == i6) {
                        }
                        float f1122 = ceil;
                        float f1222 = f6;
                        textPaint3 = textPaint522;
                        int ceil422 = (int) Math.ceil(f1122 + Math.max(0.0f, f92));
                        if (textLayoutBlock.quote) {
                        }
                        int i2822 = ceil;
                        StaticLayout staticLayout322 = makeStaticLayout;
                        if (lineCount > 1) {
                        }
                        text = textLayoutBlock.languageLayout;
                        if (text != null) {
                        }
                        textLayoutBlock.spoilers.clear();
                        if (this.isSpoilersRevealed) {
                        }
                        f6 = f1222;
                        makeStaticLayout = staticLayout322;
                        f7 = f;
                        i++;
                        i15 = i5;
                        z6 = z3;
                        textPaint = textPaint3;
                        arrayList = arrayList2;
                        charSequence7 = charSequence;
                    }
                    if (this.hasCode) {
                        int i35 = this.textWidth;
                        int i36 = this.generatedWithMinSize;
                        if (!needDrawAvatarInternal() || isOutOwner() || this.messageOwner.isThreadMessage) {
                            i2 = 80;
                            i3 = 0;
                        } else {
                            i3 = 52;
                            i2 = 80;
                        }
                        if (i35 > i36 - AndroidUtilities.dp(i2 + i3)) {
                            z2 = true;
                            this.hasWideCode = z2;
                            return;
                        }
                    }
                    z2 = false;
                    this.hasWideCode = z2;
                    return;
                }
                maxMessageTextWidth += dp2;
                int i152 = maxMessageTextWidth;
                this.textHeight = 0;
                int lineCount22 = makeStaticLayout.getLineCount();
                int i162 = this.totalAnimatedEmojiCount;
                if (i162 >= 50) {
                }
                if (Build.VERSION.SDK_INT < 24) {
                }
                if (z) {
                }
                arrayList = new ArrayList();
                if (!(spannableStringBuilder instanceof Spanned)) {
                }
                if (z) {
                }
                z4 = false;
                arrayList.add(new TextRange(0, makeStaticLayout.getText().length()));
                int size2 = arrayList.size();
                this.hasCodeAtTop = z4;
                this.hasCodeAtBottom = z4;
                this.hasQuoteAtBottom = z4;
                this.hasSingleQuote = z4;
                this.hasSingleCode = z4;
                i = 0;
                float f62 = 0.0f;
                float f72 = 0.0f;
                CharSequence charSequence72 = spannableStringBuilder;
                while (i < arrayList.size()) {
                }
                if (this.hasCode) {
                }
                z2 = false;
                this.hasWideCode = z2;
                return;
            }
            StaticLayout makeStaticLayout3 = makeStaticLayout(charSequence6, textPaint, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f, this.emojiOnlyCount <= 0);
            spannableStringBuilder = charSequence6;
            if (this.isRepostPreview) {
            }
            if (!this.hasSingleQuote) {
            }
            maxMessageTextWidth += dp2;
            int i1522 = maxMessageTextWidth;
            this.textHeight = 0;
            int lineCount222 = makeStaticLayout3.getLineCount();
            int i1622 = this.totalAnimatedEmojiCount;
            if (i1622 >= 50) {
            }
            if (Build.VERSION.SDK_INT < 24) {
            }
            if (z) {
            }
            arrayList = new ArrayList();
            if (!(spannableStringBuilder instanceof Spanned)) {
            }
            if (z) {
            }
            z4 = false;
            arrayList.add(new TextRange(0, makeStaticLayout3.getText().length()));
            int size22 = arrayList.size();
            this.hasCodeAtTop = z4;
            this.hasCodeAtBottom = z4;
            this.hasQuoteAtBottom = z4;
            this.hasSingleQuote = z4;
            this.hasSingleCode = z4;
            i = 0;
            float f622 = 0.0f;
            float f722 = 0.0f;
            CharSequence charSequence722 = spannableStringBuilder;
            while (i < arrayList.size()) {
            }
            if (this.hasCode) {
            }
            z2 = false;
            this.hasWideCode = z2;
            return;
        } catch (Exception e3) {
            FileLog.e(e3);
            return;
        }
        maxMessageTextWidth -= dp;
        if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame)) {
        }
        CharSequence charSequence62 = this.messageText;
    }

    /* loaded from: classes3.dex */
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
        public int textHeight;
        public final ArrayList<TextLayoutBlock> textLayoutBlocks = new ArrayList<>();
        public int textWidth;
        public float textXOffset;

        /* JADX WARN: Can't wrap try/catch for region: R(14:208|(3:209|210|211)|212|(1:214)(11:245|(1:247)|216|217|218|(1:220)(1:242)|221|(2:223|(3:225|(5:229|230|(1:235)|232|233)|234))(1:241)|240|(1:239)(6:227|229|230|(0)|232|233)|234)|215|216|217|218|(0)(0)|221|(0)(0)|240|(0)(0)|234) */
        /* JADX WARN: Can't wrap try/catch for region: R(44:115|(1:117)(1:353)|118|(1:120)(1:352)|121|(1:123)|(1:125)|(1:351)(1:130)|131|(2:133|(2:(1:334)|335)(1:136))(2:336|(7:338|(1:340)(1:350)|341|(1:343)(1:349)|344|(1:346)(1:348)|347))|137|(3:139|(1:141)(2:328|(1:330)(1:331))|142)(1:332)|143|(1:145)(1:(1:326)(28:327|147|(3:149|(3:155|(1:157)(1:159)|158)|160)(3:305|(2:307|308)(7:309|310|311|(1:320)(1:315)|316|(1:318)|319)|275)|161|(1:167)|168|169|170|(1:174)|175|176|177|178|179|(1:181)|182|(1:184)|185|(3:187|(8:189|190|191|192|193|194|196|197)|203)|204|(6:206|(16:208|209|210|211|212|(1:214)(11:245|(1:247)|216|217|218|(1:220)(1:242)|221|(2:223|(3:225|(5:229|230|(1:235)|232|233)|234))(1:241)|240|(1:239)(6:227|229|230|(0)|232|233)|234)|215|216|217|218|(0)(0)|221|(0)(0)|240|(0)(0)|234)|250|251|(1:(1:254))(2:(1:282)|283)|255)(3:284|(5:286|(1:288)(1:295)|289|(1:291)(1:294)|292)(1:296)|293)|256|(3:258|(1:260)(1:262)|261)|263|(1:280)(3:269|(1:271)(3:276|(1:278)|279)|272)|273|274|275))|146|147|(0)(0)|161|(3:163|165|167)|168|169|170|(2:172|174)|175|176|177|178|179|(0)|182|(0)|185|(0)|204|(0)(0)|256|(0)|263|(1:265)|280|273|274|275|113) */
        /* JADX WARN: Code restructure failed: missing block: B:235:0x048d, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:236:0x048e, code lost:
            if (r7 == 0) goto L303;
         */
        /* JADX WARN: Code restructure failed: missing block: B:237:0x0490, code lost:
            r32.textXOffset = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:238:0x0493, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r12 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:241:0x04a3, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:242:0x04a4, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r34 = r10;
            r15 = r11;
            r0 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:278:0x053a, code lost:
            r13 = 0.0f;
         */
        /* JADX WARN: Removed duplicated region for block: B:100:0x01e1  */
        /* JADX WARN: Removed duplicated region for block: B:105:0x01fd  */
        /* JADX WARN: Removed duplicated region for block: B:106:0x01ff  */
        /* JADX WARN: Removed duplicated region for block: B:108:0x0202  */
        /* JADX WARN: Removed duplicated region for block: B:109:0x0204  */
        /* JADX WARN: Removed duplicated region for block: B:132:0x0274  */
        /* JADX WARN: Removed duplicated region for block: B:195:0x0363  */
        /* JADX WARN: Removed duplicated region for block: B:207:0x03c8  */
        /* JADX WARN: Removed duplicated region for block: B:245:0x04b5  */
        /* JADX WARN: Removed duplicated region for block: B:248:0x04ba  */
        /* JADX WARN: Removed duplicated region for block: B:251:0x04d0  */
        /* JADX WARN: Removed duplicated region for block: B:263:0x04fe  */
        /* JADX WARN: Removed duplicated region for block: B:281:0x0542  */
        /* JADX WARN: Removed duplicated region for block: B:282:0x0547  */
        /* JADX WARN: Removed duplicated region for block: B:285:0x0551  */
        /* JADX WARN: Removed duplicated region for block: B:289:0x0566  */
        /* JADX WARN: Removed duplicated region for block: B:292:0x057b  */
        /* JADX WARN: Removed duplicated region for block: B:306:0x05da  */
        /* JADX WARN: Removed duplicated region for block: B:321:0x0623  */
        /* JADX WARN: Removed duplicated region for block: B:387:0x0589 A[ADDED_TO_REGION, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:389:0x0589 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:64:0x010a  */
        /* JADX WARN: Removed duplicated region for block: B:99:0x01da  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public TextLayoutBlocks(MessageObject messageObject, CharSequence charSequence, TextPaint textPaint, int i) {
            int i2;
            int dp;
            StaticLayout staticLayout;
            CharSequence charSequence2;
            boolean z;
            ArrayList arrayList;
            int i3;
            TextPaint textPaint2;
            int i4;
            int dp2;
            CharSequence charSequence3;
            ArrayList arrayList2;
            boolean z2;
            int i5;
            MessageObject messageObject2;
            SpannableString valueOf;
            int lineCount;
            int i6;
            float f;
            float f2;
            int i7;
            Text text;
            int dp3;
            StaticLayout staticLayout2;
            float f3;
            float f4;
            boolean z3;
            int dp4;
            float f5;
            float f6;
            float f7;
            int i8;
            int i9;
            StaticLayout staticLayout3;
            float lineLeft;
            SpannableString spannableString;
            TextPaint textPaint3;
            TLRPC$Message tLRPC$Message;
            MessageObject messageObject3 = messageObject;
            CharSequence charSequence4 = charSequence;
            this.text = charSequence4;
            this.textWidth = 0;
            boolean z4 = (messageObject3 == null || (tLRPC$Message = messageObject3.messageOwner) == null || !tLRPC$Message.noforwards) ? false : true;
            if (messageObject3 != null && !z4) {
                TLRPC$Chat chat = MessagesController.getInstance(messageObject3.currentAccount).getChat(Long.valueOf(-messageObject.getDialogId()));
                z4 = chat != null && chat.noforwards;
            }
            boolean z5 = z4;
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
            try {
                if (this.hasSingleQuote) {
                    dp = AndroidUtilities.dp(32.0f);
                } else if (this.hasSingleCode) {
                    dp = AndroidUtilities.dp(15.0f);
                } else {
                    i2 = i;
                    Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
                    StaticLayout makeStaticLayout = MessageObject.makeStaticLayout(charSequence, textPaint, i2, 1.0f, 0.0f, false);
                    SpannableStringBuilder spannableStringBuilder = charSequence4;
                    if (messageObject3 != null) {
                        spannableStringBuilder = charSequence4;
                        if (messageObject3.isRepostPreview) {
                            int i10 = messageObject3.type != 0 ? messageObject.hasValidGroupId() ? 7 : 12 : 22;
                            i10 = messageObject.isWebpage() ? i10 - 8 : i10;
                            spannableStringBuilder = charSequence4;
                            if (makeStaticLayout.getLineCount() > i10) {
                                String string = LocaleController.getString(R.string.ReadMore);
                                int ceil = (int) Math.ceil(textPaint.measureText("… " + string) + AndroidUtilities.dp(1.0f));
                                float f8 = 0.0f;
                                for (int i11 = 0; i11 < i10; i11++) {
                                    f8 = Math.max(f8, makeStaticLayout.getLineRight(i11));
                                }
                                int i12 = i10 - 1;
                                int lineStart = makeStaticLayout.getLineStart(i12);
                                int lineEnd = makeStaticLayout.getLineEnd(i12) - 1;
                                while (lineEnd >= lineStart && makeStaticLayout.getPrimaryHorizontal(lineEnd) >= f8 - ceil) {
                                    lineEnd--;
                                }
                                while (lineEnd >= lineStart && !Character.isWhitespace(charSequence4.charAt(lineEnd))) {
                                    lineEnd--;
                                }
                                SpannableStringBuilder append = new SpannableStringBuilder(charSequence4.subSequence(0, lineEnd)).append((CharSequence) "… ").append((CharSequence) string);
                                append.setSpan(new CharacterStyle() { // from class: org.telegram.messenger.MessageObject.TextLayoutBlocks.1
                                    @Override // android.text.style.CharacterStyle
                                    public void updateDrawState(TextPaint textPaint4) {
                                        textPaint4.setColor(Theme.chat_msgTextPaint.linkColor);
                                    }
                                }, append.length() - string.length(), append.length(), 33);
                                try {
                                    makeStaticLayout = MessageObject.makeStaticLayout(append, textPaint, i2, 1.0f, 0.0f, false);
                                    spannableStringBuilder = append;
                                } catch (Exception e) {
                                    FileLog.e(e);
                                    return;
                                }
                            }
                        }
                    }
                    staticLayout = makeStaticLayout;
                    charSequence2 = spannableStringBuilder;
                    if (!this.hasSingleQuote) {
                        i2 += AndroidUtilities.dp(32.0f);
                    } else if (this.hasSingleCode) {
                        i2 += AndroidUtilities.dp(15.0f);
                    }
                    int i13 = i2;
                    this.textHeight = 0;
                    int lineCount2 = staticLayout.getLineCount();
                    int i14 = 10;
                    z = Build.VERSION.SDK_INT < 24;
                    int ceil2 = !z ? 1 : (int) Math.ceil(lineCount2 / 10);
                    arrayList = new ArrayList();
                    if (!(charSequence2 instanceof Spanned) && (this.hasQuote || this.hasCode)) {
                        MessageObject.cutIntoRanges(charSequence2, arrayList);
                    } else if (!z || ceil2 == 1) {
                        arrayList.add(new TextRange(0, staticLayout.getText().length()));
                    } else {
                        int i15 = 0;
                        int i16 = 0;
                        while (i15 < ceil2) {
                            int min = Math.min(i14, lineCount2 - i16);
                            int lineStart2 = staticLayout.getLineStart(i16);
                            int i17 = min + i16;
                            int lineEnd2 = staticLayout.getLineEnd(i17 - 1);
                            if (lineEnd2 >= lineStart2) {
                                arrayList.add(new TextRange(lineStart2, lineEnd2));
                                i16 = i17;
                            }
                            i15++;
                            i14 = 10;
                        }
                    }
                    int size = arrayList.size();
                    this.hasCodeAtTop = false;
                    this.hasCodeAtBottom = false;
                    this.hasQuoteAtBottom = false;
                    this.hasSingleQuote = false;
                    StaticLayout staticLayout4 = staticLayout;
                    i3 = 0;
                    float f9 = 0.0f;
                    float f10 = 0.0f;
                    while (i3 < arrayList.size()) {
                        TextLayoutBlock textLayoutBlock = new TextLayoutBlock();
                        TextRange textRange = (TextRange) arrayList.get(i3);
                        textLayoutBlock.code = textRange.code;
                        textLayoutBlock.quote = textRange.quote;
                        textLayoutBlock.first = i3 == 0;
                        boolean z7 = i3 == arrayList.size() + (-1);
                        textLayoutBlock.last = z7;
                        boolean z8 = textLayoutBlock.first;
                        if (z8) {
                            this.hasCodeAtTop = textLayoutBlock.code;
                        }
                        if (z7) {
                            this.hasQuoteAtBottom = textLayoutBlock.quote;
                            this.hasCodeAtBottom = textLayoutBlock.code;
                        }
                        this.hasSingleQuote = z8 && z7 && textLayoutBlock.quote;
                        if (textLayoutBlock.quote) {
                            if (z8 && z7) {
                                int dp5 = AndroidUtilities.dp(6.0f);
                                textLayoutBlock.padBottom = dp5;
                                textLayoutBlock.padTop = dp5;
                            } else {
                                textLayoutBlock.padTop = AndroidUtilities.dp(z8 ? 8.0f : 6.0f);
                                textLayoutBlock.padBottom = AndroidUtilities.dp(7.0f);
                            }
                        } else if (textLayoutBlock.code) {
                            textLayoutBlock.layoutCode(textRange.language, textRange.end - textRange.start, z5);
                            textLayoutBlock.padTop = AndroidUtilities.dp(4.0f) + textLayoutBlock.languageHeight + (textLayoutBlock.first ? 0 : AndroidUtilities.dp(5.0f));
                            textLayoutBlock.padBottom = AndroidUtilities.dp(4.0f) + (textLayoutBlock.last ? 0 : AndroidUtilities.dp(7.0f)) + (textLayoutBlock.hasCodeCopyButton ? AndroidUtilities.dp(38.0f) : 0);
                        }
                        boolean z9 = textLayoutBlock.code;
                        if (z9) {
                            int i18 = textRange.end - textRange.start;
                            if (i18 > 220) {
                                textPaint3 = Theme.chat_msgTextCode3Paint;
                            } else if (i18 > 80) {
                                textPaint3 = Theme.chat_msgTextCode2Paint;
                            } else {
                                textPaint3 = Theme.chat_msgTextCodePaint;
                            }
                            textPaint2 = textPaint3;
                        } else {
                            textPaint2 = textPaint;
                        }
                        if (textLayoutBlock.quote) {
                            dp2 = i13 - AndroidUtilities.dp(32.0f);
                        } else if (z9) {
                            dp2 = i13 - AndroidUtilities.dp(15.0f);
                        } else {
                            i4 = i13;
                            if (size != 1) {
                                if (textLayoutBlock.code && !textLayoutBlock.quote && (staticLayout4.getText() instanceof Spannable)) {
                                    if (!TextUtils.isEmpty(textRange.language)) {
                                        spannableString = CodeHighlighting.getHighlighted(charSequence2.subSequence(textRange.start, textRange.end).toString(), textRange.language);
                                    } else {
                                        spannableString = new SpannableString(charSequence2.subSequence(textRange.start, textRange.end));
                                    }
                                    staticLayout4 = MessageObject.makeStaticLayout(spannableString, textPaint2, i4, 1.0f, 0.0f, false);
                                }
                                textLayoutBlock.textLayout = staticLayout4;
                                textLayoutBlock.textYOffset = 0.0f;
                                textLayoutBlock.charactersOffset = 0;
                                textLayoutBlock.charactersEnd = staticLayout4.getText().length();
                                int height = staticLayout4.getHeight();
                                textLayoutBlock.height = height;
                                this.textHeight = textLayoutBlock.padTop + height + textLayoutBlock.padBottom;
                            } else {
                                int i19 = textRange.start;
                                int i20 = textRange.end;
                                if (i20 < i19) {
                                    charSequence3 = charSequence2;
                                    arrayList2 = arrayList;
                                    z2 = z5;
                                    i5 = i13;
                                    messageObject2 = messageObject3;
                                } else {
                                    textLayoutBlock.charactersOffset = i19;
                                    textLayoutBlock.charactersEnd = i20;
                                    try {
                                        if (textLayoutBlock.code && !textLayoutBlock.quote) {
                                            valueOf = CodeHighlighting.getHighlighted(charSequence2.subSequence(i19, i20).toString(), textRange.language);
                                        } else {
                                            valueOf = SpannableString.valueOf(charSequence2.subSequence(i19, i20));
                                        }
                                        StaticLayout makeStaticLayout2 = MessageObject.makeStaticLayout(valueOf, textPaint2, i4, 1.0f, 0.0f, false);
                                        textLayoutBlock.textLayout = makeStaticLayout2;
                                        textLayoutBlock.textYOffset = f9;
                                        if (i3 != 0) {
                                            textLayoutBlock.height = (int) (f9 - f10);
                                        }
                                        int height2 = makeStaticLayout2.getHeight();
                                        textLayoutBlock.height = height2;
                                        this.textHeight += textLayoutBlock.padTop + height2 + textLayoutBlock.padBottom;
                                        f10 = textLayoutBlock.textYOffset;
                                    } catch (Exception e2) {
                                        charSequence3 = charSequence2;
                                        arrayList2 = arrayList;
                                        z2 = z5;
                                        i5 = i13;
                                        messageObject2 = messageObject3;
                                        FileLog.e(e2);
                                    }
                                }
                                i3++;
                                messageObject3 = messageObject2;
                                i13 = i5;
                                charSequence2 = charSequence3;
                                z5 = z2;
                                arrayList = arrayList2;
                            }
                            if (textLayoutBlock.code && (textLayoutBlock.textLayout.getText() instanceof Spannable) && TextUtils.isEmpty(textRange.language)) {
                                CodeHighlighting.highlight((Spannable) textLayoutBlock.textLayout.getText(), 0, textLayoutBlock.textLayout.getText().length(), textRange.language, 0, null, true);
                            }
                            float f11 = f9 + textLayoutBlock.padTop + textLayoutBlock.height + textLayoutBlock.padBottom;
                            this.textLayoutBlocks.add(textLayoutBlock);
                            lineCount = textLayoutBlock.textLayout.getLineCount();
                            lineLeft = textLayoutBlock.textLayout.getLineLeft(lineCount - 1);
                            if (i3 == 0 && lineLeft >= 0.0f) {
                                this.textXOffset = lineLeft;
                            }
                            float f12 = lineLeft;
                            float f13 = textLayoutBlock.textLayout.getLineWidth(lineCount - 1);
                            arrayList2 = arrayList;
                            boolean z10 = z5;
                            int ceil3 = (int) Math.ceil(f13);
                            ceil3 = ceil3 > i13 + 80 ? i13 : ceil3;
                            i6 = size - 1;
                            if (i3 == i6) {
                                this.lastLineWidth = ceil3;
                            }
                            float f14 = ceil3;
                            charSequence3 = charSequence2;
                            int ceil4 = (int) Math.ceil(f14 + Math.max(0.0f, f12));
                            if (textLayoutBlock.quote) {
                                textLayoutBlock.maxRight = 0.0f;
                                int i21 = 0;
                                while (i21 < lineCount) {
                                    int i22 = ceil3;
                                    try {
                                        staticLayout3 = staticLayout4;
                                        try {
                                            textLayoutBlock.maxRight = Math.max(textLayoutBlock.maxRight, textLayoutBlock.textLayout.getLineRight(i21));
                                        } catch (Exception unused) {
                                            textLayoutBlock.maxRight = this.textWidth;
                                            i21++;
                                            ceil3 = i22;
                                            staticLayout4 = staticLayout3;
                                        }
                                    } catch (Exception unused2) {
                                        staticLayout3 = staticLayout4;
                                    }
                                    i21++;
                                    ceil3 = i22;
                                    staticLayout4 = staticLayout3;
                                }
                            }
                            int i23 = ceil3;
                            StaticLayout staticLayout5 = staticLayout4;
                            if (lineCount <= 1) {
                                f = f11;
                                int i24 = i23;
                                float f15 = 0.0f;
                                float f16 = 0.0f;
                                int i25 = 0;
                                boolean z11 = false;
                                int i26 = ceil4;
                                while (i25 < lineCount) {
                                    int i27 = lineCount;
                                    try {
                                        f4 = textLayoutBlock.textLayout.getLineWidth(i25);
                                        f3 = f10;
                                    } catch (Exception unused3) {
                                        f3 = f10;
                                        f4 = 0.0f;
                                    }
                                    if (textLayoutBlock.quote) {
                                        z3 = z10;
                                        AndroidUtilities.dp(32.0f);
                                    } else {
                                        z3 = z10;
                                        dp4 = textLayoutBlock.code ? AndroidUtilities.dp(15.0f) : dp4;
                                        float f17 = textLayoutBlock.textLayout.getLineLeft(i25);
                                        if (f4 <= i13 + 20) {
                                            f6 = i13;
                                            f7 = 0.0f;
                                            f5 = 0.0f;
                                        } else {
                                            f5 = 0.0f;
                                            float f18 = f17;
                                            f6 = f4;
                                            f7 = f18;
                                        }
                                        if (f7 > f5) {
                                            i8 = i13;
                                            if (textLayoutBlock.textLayout.getParagraphDirection(i25) != -1) {
                                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                                i9 = 1;
                                                if (z11 && f7 == 0.0f) {
                                                    try {
                                                        if (textLayoutBlock.textLayout.getParagraphDirection(i25) != i9) {
                                                        }
                                                    } catch (Exception unused4) {
                                                    }
                                                    z11 = true;
                                                }
                                                f16 = Math.max(f16, f6);
                                                float f19 = f7 + f6;
                                                float max = Math.max(f15, f19);
                                                i24 = Math.max(i24, (int) Math.ceil(f6));
                                                i26 = Math.max(i26, (int) Math.ceil(f19));
                                                i25++;
                                                f15 = max;
                                                lineCount = i27;
                                                f10 = f3;
                                                z10 = z3;
                                                i13 = i8;
                                            }
                                        } else {
                                            i8 = i13;
                                        }
                                        this.textXOffset = Math.min(this.textXOffset, f7);
                                        i9 = 1;
                                        textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                        this.hasRtl = true;
                                        if (z11) {
                                            if (textLayoutBlock.textLayout.getParagraphDirection(i25) != i9) {
                                            }
                                            z11 = true;
                                        }
                                        f16 = Math.max(f16, f6);
                                        float f192 = f7 + f6;
                                        float max2 = Math.max(f15, f192);
                                        i24 = Math.max(i24, (int) Math.ceil(f6));
                                        i26 = Math.max(i26, (int) Math.ceil(f192));
                                        i25++;
                                        f15 = max2;
                                        lineCount = i27;
                                        f10 = f3;
                                        z10 = z3;
                                        i13 = i8;
                                    }
                                    f4 += dp4;
                                    float f172 = textLayoutBlock.textLayout.getLineLeft(i25);
                                    if (f4 <= i13 + 20) {
                                    }
                                    if (f7 > f5) {
                                    }
                                    this.textXOffset = Math.min(this.textXOffset, f7);
                                    i9 = 1;
                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                    this.hasRtl = true;
                                    if (z11) {
                                    }
                                    f16 = Math.max(f16, f6);
                                    float f1922 = f7 + f6;
                                    float max22 = Math.max(f15, f1922);
                                    i24 = Math.max(i24, (int) Math.ceil(f6));
                                    i26 = Math.max(i26, (int) Math.ceil(f1922));
                                    i25++;
                                    f15 = max22;
                                    lineCount = i27;
                                    f10 = f3;
                                    z10 = z3;
                                    i13 = i8;
                                }
                                int i28 = i13;
                                f2 = f10;
                                z2 = z10;
                                if (!z11) {
                                    if (i3 == i6) {
                                        this.lastLineWidth = i24;
                                    }
                                    f15 = f16;
                                } else if (i3 == i6) {
                                    this.lastLineWidth = ceil4;
                                }
                                this.textWidth = Math.max(this.textWidth, (int) Math.ceil(f15));
                                ceil4 = i26;
                                i5 = i28;
                            } else {
                                int i29 = i13;
                                f = f11;
                                f2 = f10;
                                z2 = z10;
                                if (f12 > 0.0f) {
                                    float min2 = Math.min(this.textXOffset, f12);
                                    this.textXOffset = min2;
                                    i7 = min2 == 0.0f ? (int) (f14 + f12) : i23;
                                    this.hasRtl = size != 1;
                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                } else {
                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                    i7 = i23;
                                }
                                i5 = i29;
                                this.textWidth = Math.max(this.textWidth, Math.min(i5, i7));
                            }
                            text = textLayoutBlock.languageLayout;
                            if (text != null) {
                                this.textWidth = (int) Math.max(this.textWidth, Math.min(text.getCurrentWidth() + AndroidUtilities.dp(15.0f), textLayoutBlock.textLayout == null ? 0.0f : staticLayout2.getWidth()));
                            }
                            messageObject2 = messageObject;
                            if (messageObject2 == null && !messageObject2.isSpoilersRevealed && !messageObject.spoiledLoginCode) {
                                if (textLayoutBlock.quote) {
                                    dp3 = ceil4 - AndroidUtilities.dp(32.0f);
                                } else {
                                    dp3 = textLayoutBlock.code ? ceil4 - AndroidUtilities.dp(15.0f) : ceil4;
                                }
                                SpoilerEffect.addSpoilers(null, textLayoutBlock.textLayout, -1, dp3, null, textLayoutBlock.spoilers);
                            }
                            staticLayout4 = staticLayout5;
                            f9 = f;
                            f10 = f2;
                            i3++;
                            messageObject3 = messageObject2;
                            i13 = i5;
                            charSequence2 = charSequence3;
                            z5 = z2;
                            arrayList = arrayList2;
                        }
                        i4 = dp2;
                        if (size != 1) {
                        }
                        if (textLayoutBlock.code) {
                            CodeHighlighting.highlight((Spannable) textLayoutBlock.textLayout.getText(), 0, textLayoutBlock.textLayout.getText().length(), textRange.language, 0, null, true);
                        }
                        float f112 = f9 + textLayoutBlock.padTop + textLayoutBlock.height + textLayoutBlock.padBottom;
                        this.textLayoutBlocks.add(textLayoutBlock);
                        lineCount = textLayoutBlock.textLayout.getLineCount();
                        lineLeft = textLayoutBlock.textLayout.getLineLeft(lineCount - 1);
                        if (i3 == 0) {
                            this.textXOffset = lineLeft;
                        }
                        float f122 = lineLeft;
                        float f132 = textLayoutBlock.textLayout.getLineWidth(lineCount - 1);
                        arrayList2 = arrayList;
                        boolean z102 = z5;
                        int ceil32 = (int) Math.ceil(f132);
                        if (ceil32 > i13 + 80) {
                        }
                        i6 = size - 1;
                        if (i3 == i6) {
                        }
                        float f142 = ceil32;
                        charSequence3 = charSequence2;
                        int ceil42 = (int) Math.ceil(f142 + Math.max(0.0f, f122));
                        if (textLayoutBlock.quote) {
                        }
                        int i232 = ceil32;
                        StaticLayout staticLayout52 = staticLayout4;
                        if (lineCount <= 1) {
                        }
                        text = textLayoutBlock.languageLayout;
                        if (text != null) {
                        }
                        messageObject2 = messageObject;
                        if (messageObject2 == null) {
                        }
                        staticLayout4 = staticLayout52;
                        f9 = f;
                        f10 = f2;
                        i3++;
                        messageObject3 = messageObject2;
                        i13 = i5;
                        charSequence2 = charSequence3;
                        z5 = z2;
                        arrayList = arrayList2;
                    }
                    return;
                }
                StaticLayout makeStaticLayout3 = MessageObject.makeStaticLayout(charSequence, textPaint, i2, 1.0f, 0.0f, false);
                SpannableStringBuilder spannableStringBuilder2 = charSequence4;
                if (messageObject3 != null) {
                }
                staticLayout = makeStaticLayout3;
                charSequence2 = spannableStringBuilder2;
                if (!this.hasSingleQuote) {
                }
                int i132 = i2;
                this.textHeight = 0;
                int lineCount22 = staticLayout.getLineCount();
                int i142 = 10;
                if (Build.VERSION.SDK_INT < 24) {
                }
                if (!z) {
                }
                arrayList = new ArrayList();
                if (!(charSequence2 instanceof Spanned)) {
                }
                if (z) {
                }
                arrayList.add(new TextRange(0, staticLayout.getText().length()));
                int size2 = arrayList.size();
                this.hasCodeAtTop = false;
                this.hasCodeAtBottom = false;
                this.hasQuoteAtBottom = false;
                this.hasSingleQuote = false;
                StaticLayout staticLayout42 = staticLayout;
                i3 = 0;
                float f92 = 0.0f;
                float f102 = 0.0f;
                while (i3 < arrayList.size()) {
                }
                return;
            } catch (Exception e3) {
                FileLog.e(e3);
                return;
            }
            i2 = i - dp;
            Layout.Alignment alignment2 = Layout.Alignment.ALIGN_NORMAL;
        }
    }

    public boolean isOut() {
        return this.messageOwner.out;
    }

    public boolean isOutOwner() {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Peer tLRPC$Peer2;
        boolean z = true;
        if (this.previewForward) {
            return true;
        }
        Boolean bool = this.isOutOwnerCached;
        if (bool != null) {
            return bool.booleanValue();
        }
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (this.isSaved || getDialogId() == clientUserId) {
            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
            if (tLRPC$MessageFwdHeader != null) {
                TLRPC$Peer tLRPC$Peer3 = tLRPC$MessageFwdHeader.from_id;
                if ((tLRPC$Peer3 == null || tLRPC$Peer3.user_id != clientUserId) && !tLRPC$MessageFwdHeader.saved_out) {
                    z = false;
                }
                Boolean valueOf = Boolean.valueOf(z);
                this.isOutOwnerCached = valueOf;
                return valueOf.booleanValue();
            }
            this.isOutOwnerCached = Boolean.TRUE;
            return true;
        }
        TLRPC$Peer tLRPC$Peer4 = this.messageOwner.peer_id;
        TLRPC$Chat tLRPC$Chat = null;
        if (tLRPC$Peer4 != null) {
            long j = tLRPC$Peer4.channel_id;
            if (j != 0) {
                tLRPC$Chat = getChat(null, null, j);
            }
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.out) {
            TLRPC$Peer tLRPC$Peer5 = tLRPC$Message.from_id;
            if ((tLRPC$Peer5 instanceof TLRPC$TL_peerUser) || ((tLRPC$Peer5 instanceof TLRPC$TL_peerChannel) && (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup))) {
                TLRPC$Message tLRPC$Message2 = this.messageOwner;
                if (!tLRPC$Message2.post) {
                    if (tLRPC$Message2.fwd_from == null) {
                        this.isOutOwnerCached = Boolean.TRUE;
                        return true;
                    } else if (getDialogId() == clientUserId) {
                        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2 = this.messageOwner.fwd_from;
                        TLRPC$Peer tLRPC$Peer6 = tLRPC$MessageFwdHeader2.from_id;
                        if ((!(tLRPC$Peer6 instanceof TLRPC$TL_peerUser) || tLRPC$Peer6.user_id != clientUserId || ((tLRPC$Peer2 = tLRPC$MessageFwdHeader2.saved_from_peer) != null && tLRPC$Peer2.user_id != clientUserId)) && ((tLRPC$Peer = tLRPC$MessageFwdHeader2.saved_from_peer) == null || tLRPC$Peer.user_id != clientUserId || (tLRPC$Peer6 != null && tLRPC$Peer6.user_id != clientUserId))) {
                            z = false;
                        }
                        Boolean valueOf2 = Boolean.valueOf(z);
                        this.isOutOwnerCached = valueOf2;
                        return valueOf2.booleanValue();
                    } else {
                        TLRPC$Peer tLRPC$Peer7 = this.messageOwner.fwd_from.saved_from_peer;
                        if (tLRPC$Peer7 != null && tLRPC$Peer7.user_id != clientUserId) {
                            z = false;
                        }
                        Boolean valueOf3 = Boolean.valueOf(z);
                        this.isOutOwnerCached = valueOf3;
                        return valueOf3.booleanValue();
                    }
                }
            }
        }
        this.isOutOwnerCached = Boolean.FALSE;
        return false;
    }

    public boolean needDrawAvatar() {
        if (this.isRepostPreview || this.isSaved || this.forceAvatar || this.customAvatarDrawable != null) {
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
        if (this.isRepostPreview || this.isSaved || this.forceAvatar || this.customAvatarDrawable != null) {
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
        if (!(ChatObject.isChannel(tLRPC$Chat) && tLRPC$Chat.megagroup) && ((tLRPC$Peer = this.messageOwner.peer_id) == null || tLRPC$Peer.chat_id == 0)) {
            return (tLRPC$Peer == null || tLRPC$Peer.channel_id == 0 || tLRPC$Chat == null || !tLRPC$Chat.megagroup) ? false : true;
        }
        return true;
    }

    public static long getFromChatId(TLRPC$Message tLRPC$Message) {
        return getPeerId(tLRPC$Message.from_id);
    }

    public static long getObjectPeerId(TLObject tLObject) {
        if (tLObject == null) {
            return 0L;
        }
        if (tLObject instanceof TLRPC$Chat) {
            return -((TLRPC$Chat) tLObject).id;
        }
        if (tLObject instanceof TLRPC$User) {
            return ((TLRPC$User) tLObject).id;
        }
        return 0L;
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

    public static boolean peersEqual(TLRPC$InputPeer tLRPC$InputPeer, TLRPC$InputPeer tLRPC$InputPeer2) {
        if (tLRPC$InputPeer == null && tLRPC$InputPeer2 == null) {
            return true;
        }
        if (tLRPC$InputPeer != null && tLRPC$InputPeer2 != null) {
            if ((tLRPC$InputPeer instanceof TLRPC$TL_inputPeerChat) && (tLRPC$InputPeer2 instanceof TLRPC$TL_inputPeerChat)) {
                return tLRPC$InputPeer.chat_id == tLRPC$InputPeer2.chat_id;
            } else if ((tLRPC$InputPeer instanceof TLRPC$TL_inputPeerChannel) && (tLRPC$InputPeer2 instanceof TLRPC$TL_inputPeerChannel)) {
                return tLRPC$InputPeer.channel_id == tLRPC$InputPeer2.channel_id;
            } else if ((tLRPC$InputPeer instanceof TLRPC$TL_inputPeerUser) && (tLRPC$InputPeer2 instanceof TLRPC$TL_inputPeerUser)) {
                return tLRPC$InputPeer.user_id == tLRPC$InputPeer2.user_id;
            } else if ((tLRPC$InputPeer instanceof TLRPC$TL_inputPeerSelf) && (tLRPC$InputPeer2 instanceof TLRPC$TL_inputPeerSelf)) {
                return true;
            }
        }
        return false;
    }

    public static boolean peersEqual(TLRPC$InputPeer tLRPC$InputPeer, TLRPC$Peer tLRPC$Peer) {
        if (tLRPC$InputPeer == null && tLRPC$Peer == null) {
            return true;
        }
        if (tLRPC$InputPeer != null && tLRPC$Peer != null) {
            return ((tLRPC$InputPeer instanceof TLRPC$TL_inputPeerChat) && (tLRPC$Peer instanceof TLRPC$TL_peerChat)) ? tLRPC$InputPeer.chat_id == tLRPC$Peer.chat_id : ((tLRPC$InputPeer instanceof TLRPC$TL_inputPeerChannel) && (tLRPC$Peer instanceof TLRPC$TL_peerChannel)) ? tLRPC$InputPeer.channel_id == tLRPC$Peer.channel_id : (tLRPC$InputPeer instanceof TLRPC$TL_inputPeerUser) && (tLRPC$Peer instanceof TLRPC$TL_peerUser) && tLRPC$InputPeer.user_id == tLRPC$Peer.user_id;
        }
        return false;
    }

    public static boolean peersEqual(TLRPC$Peer tLRPC$Peer, TLRPC$Peer tLRPC$Peer2) {
        if (tLRPC$Peer == null && tLRPC$Peer2 == null) {
            return true;
        }
        if (tLRPC$Peer != null && tLRPC$Peer2 != null) {
            return ((tLRPC$Peer instanceof TLRPC$TL_peerChat) && (tLRPC$Peer2 instanceof TLRPC$TL_peerChat)) ? tLRPC$Peer.chat_id == tLRPC$Peer2.chat_id : ((tLRPC$Peer instanceof TLRPC$TL_peerChannel) && (tLRPC$Peer2 instanceof TLRPC$TL_peerChannel)) ? tLRPC$Peer.channel_id == tLRPC$Peer2.channel_id : (tLRPC$Peer instanceof TLRPC$TL_peerUser) && (tLRPC$Peer2 instanceof TLRPC$TL_peerUser) && tLRPC$Peer.user_id == tLRPC$Peer2.user_id;
        }
        return false;
    }

    public static boolean peersEqual(TLRPC$Chat tLRPC$Chat, TLRPC$Peer tLRPC$Peer) {
        if (tLRPC$Chat == null && tLRPC$Peer == null) {
            return true;
        }
        if (tLRPC$Chat != null && tLRPC$Peer != null) {
            return (ChatObject.isChannel(tLRPC$Chat) && (tLRPC$Peer instanceof TLRPC$TL_peerChannel)) ? tLRPC$Chat.id == tLRPC$Peer.channel_id : !ChatObject.isChannel(tLRPC$Chat) && (tLRPC$Peer instanceof TLRPC$TL_peerChat) && tLRPC$Chat.id == tLRPC$Peer.chat_id;
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
        if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
            return tLRPC$Peer.channel_id;
        }
        return 0L;
    }

    public TLObject getFromPeerObject() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message != null) {
            TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
            if ((tLRPC$Peer instanceof TLRPC$TL_peerChannel_layer131) || (tLRPC$Peer instanceof TLRPC$TL_peerChannel)) {
                return MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.channel_id));
            }
            if ((tLRPC$Peer instanceof TLRPC$TL_peerUser_layer131) || (tLRPC$Peer instanceof TLRPC$TL_peerUser)) {
                return MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
            }
            if ((tLRPC$Peer instanceof TLRPC$TL_peerChat_layer131) || (tLRPC$Peer instanceof TLRPC$TL_peerChat)) {
                return MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.chat_id));
            }
            return null;
        }
        return null;
    }

    public static String getPeerObjectName(TLObject tLObject) {
        if (tLObject instanceof TLRPC$User) {
            return UserObject.getUserName((TLRPC$User) tLObject);
        }
        return tLObject instanceof TLRPC$Chat ? ((TLRPC$Chat) tLObject).title : "DELETED";
    }

    public boolean isFromUser() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return (tLRPC$Message.from_id instanceof TLRPC$TL_peerUser) && !tLRPC$Message.post;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isFromChannel() {
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Peer tLRPC$Peer2 = this.messageOwner.peer_id;
        TLRPC$Chat tLRPC$Chat2 = null;
        if (tLRPC$Peer2 != null) {
            long j = tLRPC$Peer2.channel_id;
            if (j != 0) {
                tLRPC$Chat = getChat(null, null, j);
                if ((this.messageOwner.peer_id instanceof TLRPC$TL_peerChannel) || !ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
                    tLRPC$Peer = this.messageOwner.from_id;
                    if (tLRPC$Peer != null) {
                        long j2 = tLRPC$Peer.channel_id;
                        if (j2 != 0) {
                            tLRPC$Chat2 = getChat(null, null, j2);
                        }
                    }
                    return (this.messageOwner.from_id instanceof TLRPC$TL_peerChannel) && ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat2);
                }
                return true;
            }
        }
        tLRPC$Chat = null;
        if (this.messageOwner.peer_id instanceof TLRPC$TL_peerChannel) {
        }
        tLRPC$Peer = this.messageOwner.from_id;
        if (tLRPC$Peer != null) {
        }
        if (this.messageOwner.from_id instanceof TLRPC$TL_peerChannel) {
            return false;
        }
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

    public static long getChatId(TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message == null) {
            return 0L;
        }
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
        if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
            return tLRPC$Peer.chat_id;
        }
        if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
            return tLRPC$Peer.channel_id;
        }
        return 0L;
    }

    public static boolean shouldEncryptPhotoOrVideo(int i, TLRPC$Message tLRPC$Message) {
        int i2;
        if (tLRPC$Message == null || tLRPC$Message.media == null || !((isVoiceDocument(getDocument(tLRPC$Message)) || isRoundVideoMessage(tLRPC$Message)) && tLRPC$Message.media.ttl_seconds == Integer.MAX_VALUE)) {
            return tLRPC$Message instanceof TLRPC$TL_message_secret ? ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isVideoMessage(tLRPC$Message)) && (i2 = tLRPC$Message.ttl) > 0 && i2 <= 60 : ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument)) && getMedia(tLRPC$Message).ttl_seconds != 0;
        }
        return true;
    }

    public boolean shouldEncryptPhotoOrVideo() {
        return shouldEncryptPhotoOrVideo(this.currentAccount, this.messageOwner);
    }

    public static boolean isSecretPhotoOrVideo(TLRPC$Message tLRPC$Message) {
        int i;
        if (tLRPC$Message instanceof TLRPC$TL_message_secret) {
            return ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isRoundVideoMessage(tLRPC$Message) || isVideoMessage(tLRPC$Message)) && (i = tLRPC$Message.ttl) > 0 && i <= 60;
        } else if (tLRPC$Message instanceof TLRPC$TL_message) {
            return ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument)) && getMedia(tLRPC$Message).ttl_seconds != 0;
        } else {
            return false;
        }
    }

    public static boolean isSecretMedia(TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message instanceof TLRPC$TL_message_secret) {
            return ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isRoundVideoMessage(tLRPC$Message) || isVideoMessage(tLRPC$Message)) && getMedia(tLRPC$Message).ttl_seconds != 0;
        } else if (tLRPC$Message instanceof TLRPC$TL_message) {
            return ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument)) && getMedia(tLRPC$Message).ttl_seconds != 0;
        } else {
            return false;
        }
    }

    public boolean needDrawBluredPreview() {
        if (this.isRepostPreview) {
            return false;
        }
        if (hasExtendedMediaPreview()) {
            return true;
        }
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message instanceof TLRPC$TL_message_secret) {
            int max = Math.max(tLRPC$Message.ttl, getMedia(tLRPC$Message).ttl_seconds);
            if (max > 0) {
                return (((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || isVideo() || isGif()) && max <= 60) || isRoundVideo();
            }
            return false;
        } else if (!(tLRPC$Message instanceof TLRPC$TL_message) || getMedia(tLRPC$Message) == null || getMedia(this.messageOwner).ttl_seconds == 0) {
            return false;
        } else {
            return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument);
        }
    }

    public boolean isSecretMedia() {
        int i;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message instanceof TLRPC$TL_message_secret ? (((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isGif()) && (i = this.messageOwner.ttl) > 0 && i <= 60) || isVoice() || isRoundVideo() || isVideo() : (tLRPC$Message instanceof TLRPC$TL_message) && getMedia(tLRPC$Message) != null && getMedia(this.messageOwner).ttl_seconds != 0 && ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument));
    }

    public static void setUnreadFlags(TLRPC$Message tLRPC$Message, int i) {
        tLRPC$Message.unread = (i & 1) == 0;
        tLRPC$Message.media_unread = (i & 2) == 0;
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

    public long getSavedDialogId() {
        return getSavedDialogId(UserConfig.getInstance(this.currentAccount).getClientUserId(), this.messageOwner);
    }

    public static long getSavedDialogId(long j, TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Peer tLRPC$Peer2 = tLRPC$Message.saved_peer_id;
        if (tLRPC$Peer2 != null) {
            long j2 = tLRPC$Peer2.chat_id;
            if (j2 != 0) {
                return -j2;
            }
            long j3 = tLRPC$Peer2.channel_id;
            return j3 != 0 ? -j3 : tLRPC$Peer2.user_id;
        } else if (tLRPC$Message.from_id.user_id == j) {
            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
            if (tLRPC$MessageFwdHeader == null || (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) == null) {
                return ((tLRPC$MessageFwdHeader == null || tLRPC$MessageFwdHeader.from_id == null) && tLRPC$MessageFwdHeader != null) ? UserObject.ANONYMOUS : j;
            }
            return DialogObject.getPeerDialogId(tLRPC$Peer);
        } else {
            return 0L;
        }
    }

    public static TLRPC$Peer getSavedDialogPeer(long j, TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Peer tLRPC$Peer2;
        TLRPC$Peer tLRPC$Peer3 = tLRPC$Message.saved_peer_id;
        if (tLRPC$Peer3 != null) {
            return tLRPC$Peer3;
        }
        TLRPC$Peer tLRPC$Peer4 = tLRPC$Message.peer_id;
        if (tLRPC$Peer4 == null || tLRPC$Peer4.user_id != j || (tLRPC$Peer = tLRPC$Message.from_id) == null || tLRPC$Peer.user_id != j) {
            return null;
        }
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
        if (tLRPC$MessageFwdHeader == null || (tLRPC$Peer2 = tLRPC$MessageFwdHeader.saved_from_peer) == null) {
            if (tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.from_id != null) {
                TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                tLRPC$TL_peerUser.user_id = j;
                return tLRPC$TL_peerUser;
            } else if (tLRPC$MessageFwdHeader != null) {
                TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                tLRPC$TL_peerUser2.user_id = UserObject.ANONYMOUS;
                return tLRPC$TL_peerUser2;
            } else {
                TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                tLRPC$TL_peerUser3.user_id = j;
                return tLRPC$TL_peerUser3;
            }
        }
        return tLRPC$Peer2;
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

    public CharSequence getSecretTimeString() {
        String str;
        if (isSecretMedia()) {
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
        return null;
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
        return (isAnimatedStickerDocument(tLRPC$Document, true) || isVideoStickerDocument(tLRPC$Document)) && LiteMode.isEnabled(1);
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
        return (webFile == null || isGifDocument(webFile) || !webFile.mime_type.startsWith("image/")) ? false : true;
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
        if (getMedia(tLRPC$Message) != null) {
            return getMedia(tLRPC$Message).document;
        }
        return null;
    }

    public static TLRPC$Photo getPhoto(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return getMedia(tLRPC$Message).webpage.photo;
        }
        if (getMedia(tLRPC$Message) != null) {
            return getMedia(tLRPC$Message).photo;
        }
        return null;
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
        if (!(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(tLRPC$Message).webpage == null) {
            return getMedia(tLRPC$Message) != null && isRoundVideoDocument(getMedia(tLRPC$Message).document);
        }
        return isRoundVideoDocument(getMedia(tLRPC$Message).webpage.document);
    }

    public static boolean isPhoto(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$Photo tLRPC$Photo;
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return (getMedia(tLRPC$Message).webpage.photo instanceof TLRPC$TL_photo) && !(getMedia(tLRPC$Message).webpage.document instanceof TLRPC$TL_document);
        } else if (tLRPC$Message != null && (tLRPC$MessageAction = tLRPC$Message.action) != null && (tLRPC$Photo = tLRPC$MessageAction.photo) != null) {
            return tLRPC$Photo instanceof TLRPC$TL_photo;
        } else {
            return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto;
        }
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
                if (tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty) {
                    return null;
                }
                return tLRPC$InputStickerSet;
            }
        }
        return null;
    }

    public static String findAnimatedEmojiEmoticon(TLRPC$Document tLRPC$Document) {
        return findAnimatedEmojiEmoticon(tLRPC$Document, "😀");
    }

    public static String findAnimatedEmojiEmoticon(TLRPC$Document tLRPC$Document, String str) {
        return findAnimatedEmojiEmoticon(tLRPC$Document, str, null);
    }

    public static String findAnimatedEmojiEmoticon(TLRPC$Document tLRPC$Document, String str, Integer num) {
        if (tLRPC$Document == null) {
            return str;
        }
        int size = tLRPC$Document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeCustomEmoji) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker)) {
                if (num != null) {
                    TLRPC$TL_messages_stickerSet stickerSet = MediaDataController.getInstance(num.intValue()).getStickerSet(tLRPC$DocumentAttribute.stickerset, true);
                    StringBuilder sb = new StringBuilder("");
                    if (stickerSet != null && stickerSet.packs != null) {
                        for (int i2 = 0; i2 < stickerSet.packs.size(); i2++) {
                            TLRPC$TL_stickerPack tLRPC$TL_stickerPack = stickerSet.packs.get(i2);
                            if (tLRPC$TL_stickerPack.documents.contains(Long.valueOf(tLRPC$Document.id))) {
                                sb.append(tLRPC$TL_stickerPack.emoticon);
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(sb)) {
                        return sb.toString();
                    }
                }
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

    public static boolean isTextColorEmoji(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return false;
        }
        getInputStickerSet(tLRPC$Document);
        int size = tLRPC$Document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeCustomEmoji) {
                TLRPC$InputStickerSet tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset;
                if ((tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetID) && tLRPC$InputStickerSet.id == 1269403972611866647L) {
                    return true;
                }
                return ((TLRPC$TL_documentAttributeCustomEmoji) tLRPC$DocumentAttribute).text_color;
            }
        }
        return false;
    }

    public static boolean isTextColorSet(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        TLRPC$StickerSet tLRPC$StickerSet;
        if (tLRPC$TL_messages_stickerSet != null && (tLRPC$StickerSet = tLRPC$TL_messages_stickerSet.set) != null) {
            if (tLRPC$StickerSet.text_color) {
                return true;
            }
            ArrayList<TLRPC$Document> arrayList = tLRPC$TL_messages_stickerSet.documents;
            if (arrayList != null && !arrayList.isEmpty()) {
                return isTextColorEmoji(tLRPC$TL_messages_stickerSet.documents.get(0));
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
                if (tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty) {
                    return -1L;
                }
                return tLRPC$InputStickerSet.id;
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
                if (tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty) {
                    return null;
                }
                return tLRPC$InputStickerSet.short_name;
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
            if (i3 == 11 || i3 == 18 || i3 == 25 || i3 == 21) {
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
                if (str == null || str.length() <= 0) {
                    return null;
                }
                return tLRPC$DocumentAttribute.alt;
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
        if (isDice()) {
            TLRPC$TL_messageMediaDice tLRPC$TL_messageMediaDice = (TLRPC$TL_messageMediaDice) getMedia(this.messageOwner);
            return TextUtils.isEmpty(tLRPC$TL_messageMediaDice.emoticon) ? "🎲" : tLRPC$TL_messageMediaDice.emoticon.replace("️", "");
        }
        return null;
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
        if (!isEncryptedDialog || this.messageOwner.stickerVerified == 1) {
            if (this.emojiAnimatedStickerId == null || this.emojiAnimatedSticker != null) {
                return isAnimatedStickerDocument(getDocument(), (this.emojiAnimatedSticker == null && isEncryptedDialog && !isOut()) ? true : true);
            }
            return true;
        }
        return false;
    }

    public boolean isAnyKindOfSticker() {
        int i = this.type;
        return i == 13 || i == 15 || i == 19;
    }

    public boolean shouldDrawWithoutBackground() {
        int i;
        return !isSponsored() && ((i = this.type) == 13 || i == 15 || i == 5 || i == 19 || isExpiredStory());
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
        return (!isMusicMessage(this.messageOwner) || isVideo() || isRoundVideo()) ? false : true;
    }

    public boolean isDocument() {
        return (getDocument() == null || isVideo() || isMusic() || isVoice() || isAnyKindOfSticker()) ? false : true;
    }

    public boolean isVoice() {
        return isVoiceMessage(this.messageOwner);
    }

    public boolean isVoiceOnce() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        return isVoice() && (tLRPC$Message = this.messageOwner) != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null && tLRPC$MessageMedia.ttl_seconds == Integer.MAX_VALUE;
    }

    public boolean isRoundOnce() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        return isRoundVideo() && (tLRPC$Message = this.messageOwner) != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null && tLRPC$MessageMedia.ttl_seconds == Integer.MAX_VALUE;
    }

    public boolean isVideo() {
        return isVideoMessage(this.messageOwner);
    }

    public boolean isVideoStory() {
        TL_stories$StoryItem tL_stories$StoryItem;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        TLRPC$MessageMedia media = getMedia(this.messageOwner);
        if (media == null || (tL_stories$StoryItem = media.storyItem) == null || (tLRPC$MessageMedia = tL_stories$StoryItem.media) == null) {
            return false;
        }
        return isVideoDocument(tLRPC$MessageMedia.document);
    }

    public boolean isPhoto() {
        return isPhoto(this.messageOwner);
    }

    public boolean isStoryMedia() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message != null && (tLRPC$Message.media instanceof TLRPC$TL_messageMediaStory);
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
        return this.wasJustSent && (this.type == 5 || isVoice() || ((isAnyKindOfSticker() && this.sendAnimationData != null) || !(this.messageText == null || this.sendAnimationData == null)));
    }

    public boolean hasAttachedStickers() {
        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
            return getMedia(this.messageOwner).photo != null && getMedia(this.messageOwner).photo.has_stickers;
        } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) {
            return isDocumentHasAttachedStickers(getMedia(this.messageOwner).document);
        } else {
            return false;
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
        return (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(this.messageOwner).webpage.document == null || isGifDocument(getMedia(this.messageOwner).webpage.document)) ? false : true;
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
                TLRPC$WebPageAttribute tLRPC$WebPageAttribute = getMedia(this.messageOwner).webpage.attributes.get(i);
                if (tLRPC$WebPageAttribute instanceof TLRPC$TL_webPageAttributeTheme) {
                    TLRPC$TL_webPageAttributeTheme tLRPC$TL_webPageAttributeTheme = (TLRPC$TL_webPageAttributeTheme) tLRPC$WebPageAttribute;
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
                    if (str == null || str.length() == 0) {
                        String documentFileName = FileLoader.getDocumentFileName(document);
                        return (TextUtils.isEmpty(documentFileName) && z) ? LocaleController.getString("AudioUnknownTitle", R.string.AudioUnknownTitle) : documentFileName;
                    }
                    return str;
                } else if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) && tLRPC$DocumentAttribute.round_message) {
                    if (isQuickReply()) {
                        int i2 = R.string.BusinessInReplies;
                        return LocaleController.formatString(i2, "/" + getQuickReplyDisplayName());
                    }
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

    public double getDuration() {
        TL_stories$StoryItem tL_stories$StoryItem;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        double d = this.attributeDuration;
        if (d > 0.0d) {
            return d;
        }
        TLRPC$Document document = getDocument();
        if (document == null && this.type == 23 && (tL_stories$StoryItem = getMedia(this.messageOwner).storyItem) != null && (tLRPC$MessageMedia = tL_stories$StoryItem.media) != null) {
            document = tLRPC$MessageMedia.document;
        }
        if (document == null) {
            return 0.0d;
        }
        int i = this.audioPlayerDuration;
        if (i > 0) {
            return i;
        }
        for (int i2 = 0; i2 < document.attributes.size(); i2++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i2);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                double d2 = tLRPC$DocumentAttribute.duration;
                this.attributeDuration = d2;
                return d2;
            } else if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                double d3 = tLRPC$DocumentAttribute.duration;
                this.attributeDuration = d3;
                return d3;
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

    /* JADX WARN: Code restructure failed: missing block: B:20:0x003c, code lost:
        if (r4.round_message != false) goto L10;
     */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0156 A[SYNTHETIC] */
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
                        return (TextUtils.isEmpty(str2) && z) ? LocaleController.getString("AudioUnknownArtist", R.string.AudioUnknownArtist) : str2;
                    }
                } else {
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    }
                    if (!z2) {
                        TLRPC$User tLRPC$User = null;
                        if (z) {
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
                        return null;
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

    /* JADX WARN: Code restructure failed: missing block: B:43:0x0080, code lost:
        if (r3.channel_id == r0.channel_id) goto L44;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean needDrawForwarded() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        if (this.type != 23 || isExpiredStory()) {
            if (this.isSaved) {
                TLRPC$Message tLRPC$Message = this.messageOwner;
                if (tLRPC$Message == null || tLRPC$Message.fwd_from == null) {
                    return false;
                }
                long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                long savedDialogId = getSavedDialogId(clientUserId, this.messageOwner);
                long peerDialogId = DialogObject.getPeerDialogId(this.messageOwner.fwd_from.saved_from_peer);
                if (peerDialogId >= 0) {
                    peerDialogId = DialogObject.getPeerDialogId(this.messageOwner.fwd_from.from_id);
                }
                return peerDialogId == 0 ? savedDialogId != UserObject.ANONYMOUS : (savedDialogId == peerDialogId || peerDialogId == clientUserId) ? false : true;
            }
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            if ((tLRPC$Message2.flags & 4) != 0 && (tLRPC$MessageFwdHeader = tLRPC$Message2.fwd_from) != null && !tLRPC$MessageFwdHeader.imported) {
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
        return true;
    }

    public static boolean isForwardedMessage(TLRPC$Message tLRPC$Message) {
        return ((tLRPC$Message.flags & 4) == 0 || tLRPC$Message.fwd_from == null) ? false : true;
    }

    public boolean isReply() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader;
        MessageObject messageObject = this.replyMessageObject;
        return ((messageObject != null && (messageObject.messageOwner instanceof TLRPC$TL_messageEmpty)) || (tLRPC$MessageReplyHeader = (tLRPC$Message = this.messageOwner).reply_to) == null || (tLRPC$MessageReplyHeader.reply_to_msg_id == 0 && tLRPC$MessageReplyHeader.reply_to_random_id == 0) || (tLRPC$Message.flags & 8) == 0) ? false : true;
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

    public static boolean isMediaEmpty(TLRPC$Message tLRPC$Message) {
        return isMediaEmpty(tLRPC$Message, true);
    }

    public static boolean isMediaEmpty(TLRPC$Message tLRPC$Message, boolean z) {
        return tLRPC$Message == null || getMedia(tLRPC$Message) == null || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaEmpty) || (z && (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage));
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
        return (isQuickReply() || (this.messageOwner instanceof TLRPC$TL_message_secret) || needDrawBluredPreview() || isLiveLocation() || this.type == 16 || isSponsored() || this.messageOwner.noforwards) ? false : true;
    }

    public boolean canEditMedia() {
        if (isSecretMedia()) {
            return false;
        }
        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
            return true;
        }
        return (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) || isVoice() || isSticker() || isAnimatedSticker() || isRoundVideo()) ? false : true;
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
                    return (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || !(!(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument) || isStickerMessage(tLRPC$Message) || isAnimatedStickerMessage(tLRPC$Message)) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaEmpty) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(tLRPC$Message) == null;
                } else if (((tLRPC$Chat != null && tLRPC$Chat.megagroup && tLRPC$Message.out) || (tLRPC$Chat != null && !tLRPC$Chat.megagroup && ((tLRPC$Chat.creator || ((tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights) != null && (tLRPC$TL_chatAdminRights.edit_messages || (tLRPC$Message.out && tLRPC$TL_chatAdminRights.post_messages)))) && tLRPC$Message.post))) && ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || (((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument) && !isStickerMessage(tLRPC$Message) && !isAnimatedStickerMessage(tLRPC$Message)) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaEmpty) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(tLRPC$Message) == null))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean canDeleteMessage(boolean z, TLRPC$Chat tLRPC$Chat) {
        TLRPC$Message tLRPC$Message;
        return (isStory() && (tLRPC$Message = this.messageOwner) != null && tLRPC$Message.dialog_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) || (this.eventId == 0 && this.sponsoredId == null && canDeleteMessage(this.currentAccount, z, this.messageOwner, tLRPC$Chat));
    }

    public static boolean canDeleteMessage(int i, boolean z, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat) {
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        TLRPC$Peer tLRPC$Peer;
        if (tLRPC$Message == null) {
            return false;
        }
        if (ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat) && (tLRPC$Message.action instanceof TLRPC$TL_messageActionChatJoinedByRequest)) {
            return false;
        }
        if (tLRPC$Message.id < 0) {
            return true;
        }
        if (tLRPC$Chat == null && (tLRPC$Peer = tLRPC$Message.peer_id) != null && tLRPC$Peer.channel_id != 0) {
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
                if (chat != null) {
                    return chat.title;
                }
                return null;
            } else if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.chat_id));
                if (chat2 != null) {
                    return chat2.title;
                }
                return null;
            } else if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.fwd_from.from_id.user_id));
                if (user != null) {
                    return UserObject.getUserName(user);
                }
                return null;
            } else {
                String str = tLRPC$MessageFwdHeader.from_name;
                if (str != null) {
                    return str;
                }
                return null;
            }
        }
        return null;
    }

    public int getReplyMsgId() {
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$MessageReplyHeader != null) {
            return tLRPC$MessageReplyHeader.reply_to_msg_id;
        }
        return 0;
    }

    public int getReplyTopMsgId() {
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$MessageReplyHeader != null) {
            return tLRPC$MessageReplyHeader.reply_to_top_id;
        }
        return 0;
    }

    public int getReplyTopMsgId(boolean z) {
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$MessageReplyHeader != null) {
            if (z && (tLRPC$MessageReplyHeader.flags & 2) > 0 && tLRPC$MessageReplyHeader.reply_to_top_id == 0) {
                return 1;
            }
            return tLRPC$MessageReplyHeader.reply_to_top_id;
        }
        return 0;
    }

    public static long getReplyToDialogId(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = tLRPC$Message.reply_to;
        if (tLRPC$MessageReplyHeader == null) {
            return 0L;
        }
        TLRPC$Peer tLRPC$Peer = tLRPC$MessageReplyHeader.reply_to_peer_id;
        if (tLRPC$Peer != null) {
            return getPeerId(tLRPC$Peer);
        }
        return getDialogId(tLRPC$Message);
    }

    public int getReplyAnyMsgId() {
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$MessageReplyHeader != null) {
            int i = tLRPC$MessageReplyHeader.reply_to_top_id;
            return i != 0 ? i : tLRPC$MessageReplyHeader.reply_to_msg_id;
        }
        return 0;
    }

    public boolean isPrivateForward() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        return (tLRPC$MessageFwdHeader == null || TextUtils.isEmpty(tLRPC$MessageFwdHeader.from_name)) ? false : true;
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
        this.attachPathExists = (i & 1) != 0;
        this.mediaExists = (i & 2) != 0;
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
                File pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$TL_messageExtendedMediaPreview.thumb, z);
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
                if ((this.type == 3 && needDrawBluredPreview()) || isVoiceOnce() || isRoundOnce()) {
                    this.mediaExists = new File(pathToMessage2.getAbsolutePath() + ".enc").exists();
                }
                if (!this.mediaExists) {
                    this.mediaExists = pathToMessage2.exists();
                }
            }
        }
        if (this.mediaExists) {
            return;
        }
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
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$MessageReplyHeader != null && !TextUtils.isEmpty(tLRPC$MessageReplyHeader.quote_text)) {
            String lowerCase2 = this.messageOwner.reply_to.quote_text.trim().toLowerCase();
            if (lowerCase2.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
                handleFoundWords(arrayList, split, true);
                return;
            }
            arrayList2.addAll(Arrays.asList(lowerCase2.split("\\P{L}+")));
        }
        if (!TextUtils.isEmpty(this.messageOwner.message)) {
            String lowerCase3 = this.messageOwner.message.trim().toLowerCase();
            if (lowerCase3.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
                handleFoundWords(arrayList, split, false);
                return;
            }
            arrayList2.addAll(Arrays.asList(lowerCase3.split("\\P{L}+")));
        }
        if (getDocument() != null) {
            String lowerCase4 = FileLoader.getDocumentFileName(getDocument()).toLowerCase();
            if (lowerCase4.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
            }
            arrayList2.addAll(Arrays.asList(lowerCase4.split("\\P{L}+")));
        }
        if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage)) {
            TLRPC$WebPage tLRPC$WebPage = getMedia(this.messageOwner).webpage;
            String str3 = tLRPC$WebPage.title;
            if (str3 == null) {
                str3 = tLRPC$WebPage.site_name;
            }
            if (str3 != null) {
                String lowerCase5 = str3.toLowerCase();
                if (lowerCase5.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                    arrayList.add(lowerCase);
                }
                arrayList2.addAll(Arrays.asList(lowerCase5.split("\\P{L}+")));
            }
        }
        String musicAuthor = getMusicAuthor();
        if (musicAuthor != null) {
            String lowerCase6 = musicAuthor.toLowerCase();
            if (lowerCase6.contains(lowerCase) && !arrayList.contains(lowerCase)) {
                arrayList.add(lowerCase);
            }
            arrayList2.addAll(Arrays.asList(lowerCase6.split("\\P{L}+")));
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
        handleFoundWords(arrayList, split, false);
    }

    private void handleFoundWords(ArrayList<String> arrayList, String[] strArr, boolean z) {
        CharSequence charSequence;
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader;
        boolean z2;
        if (arrayList.isEmpty()) {
            return;
        }
        boolean z3 = false;
        for (int i = 0; i < arrayList.size(); i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= strArr.length) {
                    break;
                } else if (arrayList.get(i).contains(strArr[i2])) {
                    z3 = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (z3) {
                break;
            }
        }
        if (z3) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                int i4 = 0;
                while (true) {
                    if (i4 >= strArr.length) {
                        z2 = false;
                        break;
                    } else if (arrayList.get(i3).contains(strArr[i4])) {
                        z2 = true;
                        break;
                    } else {
                        i4++;
                    }
                }
                if (!z2) {
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
        if (this.messageOwner.message != null) {
            applyEntities();
            if (!TextUtils.isEmpty(this.caption)) {
                charSequence = this.caption;
            } else {
                charSequence = this.messageText;
            }
            CharSequence replaceMultipleCharSequence = AndroidUtilities.replaceMultipleCharSequence("\n", charSequence, " ");
            if (z && (tLRPC$Message = this.messageOwner) != null && (tLRPC$MessageReplyHeader = tLRPC$Message.reply_to) != null && tLRPC$MessageReplyHeader.quote_text != null) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.messageOwner.reply_to.quote_text);
                addEntitiesToText(spannableStringBuilder, this.messageOwner.reply_to.quote_entities, isOutOwner(), false, false, false);
                SpannableString spannableString = new SpannableString("q ");
                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_quote);
                coloredImageSpan.setOverrideColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
                spannableString.setSpan(coloredImageSpan, 0, 1, 33);
                replaceMultipleCharSequence = new SpannableStringBuilder(spannableString).append((CharSequence) spannableStringBuilder).append('\n').append(replaceMultipleCharSequence);
            }
            String charSequence2 = replaceMultipleCharSequence.toString();
            int length = charSequence2.length();
            int indexOf = charSequence2.toLowerCase().indexOf(arrayList.get(0));
            if (indexOf < 0) {
                indexOf = 0;
            }
            if (length > 120) {
                float f = 120;
                int max = Math.max(0, indexOf - ((int) (0.1f * f)));
                replaceMultipleCharSequence = replaceMultipleCharSequence.subSequence(max, Math.min(length, (indexOf - max) + indexOf + ((int) (f * 0.9f))));
            }
            this.messageTrimmedToHighlight = replaceMultipleCharSequence;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$handleFoundWords$3(String str, String str2) {
        return str2.length() - str.length();
    }

    public void createMediaThumbs() {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        if (isStoryMedia()) {
            TL_stories$StoryItem tL_stories$StoryItem = getMedia(this.messageOwner).storyItem;
            if (tL_stories$StoryItem == null || (tLRPC$MessageMedia = tL_stories$StoryItem.media) == null) {
                return;
            }
            TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
            if (tLRPC$Document != null) {
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 50);
                this.mediaThumb = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 320, false, null, true), tLRPC$Document);
                this.mediaSmallThumb = ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$Document);
                return;
            }
            TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 50);
            this.mediaThumb = ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 320, false, closestPhotoSizeWithSize2, true), this.photoThumbsObject);
            this.mediaSmallThumb = ImageLocation.getForObject(closestPhotoSizeWithSize2, this.photoThumbsObject);
        } else if (isVideo()) {
            TLRPC$Document document = getDocument();
            TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
            this.mediaThumb = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320), document);
            this.mediaSmallThumb = ImageLocation.getForDocument(closestPhotoSizeWithSize3, document);
        } else if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || getMedia(this.messageOwner).photo == null || this.photoThumbs.isEmpty()) {
        } else {
            TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 50);
            this.mediaThumb = ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 320, false, closestPhotoSizeWithSize4, false), this.photoThumbsObject);
            this.mediaSmallThumb = ImageLocation.getForObject(closestPhotoSizeWithSize4, this.photoThumbsObject);
        }
    }

    public boolean hasHighlightedWords() {
        ArrayList<String> arrayList = this.highlightedWords;
        return (arrayList == null || arrayList.isEmpty()) ? false : true;
    }

    public boolean equals(MessageObject messageObject) {
        return messageObject != null && getId() == messageObject.getId() && getDialogId() == messageObject.getDialogId();
    }

    public boolean isReactionsAvailable() {
        return (isEditing() || isSponsored() || !isSent() || this.messageOwner.action != null || isExpiredStory()) ? false : true;
    }

    public boolean selectReaction(ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.reactions == null) {
            tLRPC$Message.reactions = new TLRPC$TL_messageReactions();
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            tLRPC$Message2.reactions.reactions_as_tags = getDialogId(tLRPC$Message2) == UserConfig.getInstance(this.currentAccount).getClientUserId();
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
        if (!arrayList.isEmpty() && arrayList.contains(tLRPC$ReactionCount) && z) {
            return true;
        }
        int maxUserReactionsCount = MessagesController.getInstance(this.currentAccount).getMaxUserReactionsCount();
        if (!arrayList.isEmpty() && arrayList.contains(tLRPC$ReactionCount)) {
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
            tLRPC$ReactionCount.reaction = visibleReaction.toTLReaction();
            this.messageOwner.reactions.results.add(tLRPC$ReactionCount);
        }
        tLRPC$ReactionCount.chosen = true;
        tLRPC$ReactionCount.count++;
        tLRPC$ReactionCount.chosen_order = i + 1;
        TLRPC$Message tLRPC$Message3 = this.messageOwner;
        if (tLRPC$Message3.reactions.can_see_list || (tLRPC$Message3.dialog_id > 0 && maxUserReactionsCount > 1)) {
            TLRPC$TL_messagePeerReaction tLRPC$TL_messagePeerReaction = new TLRPC$TL_messagePeerReaction();
            TLRPC$Message tLRPC$Message4 = this.messageOwner;
            if (tLRPC$Message4.isThreadMessage && tLRPC$Message4.fwd_from != null) {
                tLRPC$TL_messagePeerReaction.peer_id = MessagesController.getInstance(this.currentAccount).getSendAsSelectedPeer(getFromChatId());
            } else {
                tLRPC$TL_messagePeerReaction.peer_id = MessagesController.getInstance(this.currentAccount).getSendAsSelectedPeer(getDialogId());
            }
            this.messageOwner.reactions.recent_reactions.add(0, tLRPC$TL_messagePeerReaction);
            if (visibleReaction.emojicon != null) {
                TLRPC$TL_reactionEmoji tLRPC$TL_reactionEmoji = new TLRPC$TL_reactionEmoji();
                tLRPC$TL_messagePeerReaction.reaction = tLRPC$TL_reactionEmoji;
                tLRPC$TL_reactionEmoji.emoticon = visibleReaction.emojicon;
            } else {
                TLRPC$TL_reactionCustomEmoji tLRPC$TL_reactionCustomEmoji = new TLRPC$TL_reactionCustomEmoji();
                tLRPC$TL_messagePeerReaction.reaction = tLRPC$TL_reactionCustomEmoji;
                tLRPC$TL_reactionCustomEmoji.document_id = visibleReaction.documentId;
            }
        }
        this.reactionsChanged = true;
        return true;
    }

    public boolean probablyRingtone() {
        if (!isVoiceOnce() && getDocument() != null && RingtoneDataStore.ringtoneSupportedMimeType.contains(getDocument().mime_type) && getDocument().size < MessagesController.getInstance(this.currentAccount).ringtoneSizeMax * 2) {
            for (int i = 0; i < getDocument().attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = getDocument().attributes.get(i);
                if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) && tLRPC$DocumentAttribute.duration < 60.0d) {
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
        if (isRoundVideo()) {
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
        return null;
    }

    public boolean isStory() {
        return this.storyItem != null;
    }

    public TLRPC$WebPage getStoryMentionWebpage() {
        if (isStoryMention()) {
            TLRPC$WebPage tLRPC$WebPage = this.storyMentionWebpage;
            if (tLRPC$WebPage != null) {
                return tLRPC$WebPage;
            }
            TLRPC$TL_webPage tLRPC$TL_webPage = new TLRPC$TL_webPage();
            tLRPC$TL_webPage.type = "telegram_story";
            TLRPC$TL_webPageAttributeStory tLRPC$TL_webPageAttributeStory = new TLRPC$TL_webPageAttributeStory();
            tLRPC$TL_webPageAttributeStory.id = this.messageOwner.media.id;
            tLRPC$TL_webPageAttributeStory.peer = MessagesController.getInstance(this.currentAccount).getPeer(this.messageOwner.media.user_id);
            TL_stories$StoryItem tL_stories$StoryItem = this.messageOwner.media.storyItem;
            if (tL_stories$StoryItem != null) {
                tLRPC$TL_webPageAttributeStory.flags |= 1;
                tLRPC$TL_webPageAttributeStory.storyItem = tL_stories$StoryItem;
            }
            tLRPC$TL_webPage.attributes.add(tLRPC$TL_webPageAttributeStory);
            this.storyMentionWebpage = tLRPC$TL_webPage;
            return tLRPC$TL_webPage;
        }
        return null;
    }

    public boolean isStoryMention() {
        return this.type == 24 && !isExpiredStory();
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

    public boolean isAnyGift() {
        int i = this.type;
        return i == 18 || i == 25;
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

    public static CharSequence groupSpan() {
        if (groupSpan == null) {
            groupSpan = new SpannableStringBuilder(ImageLoader.AUTOPLAY_FILTER);
            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.msg_folders_groups);
            coloredImageSpan.setScale(0.7f, 0.7f);
            ((SpannableStringBuilder) groupSpan).setSpan(coloredImageSpan, 0, 1, 33);
        }
        return groupSpan;
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

    public static CharSequence peerNameWithIcon(int i, TLRPC$Peer tLRPC$Peer) {
        return peerNameWithIcon(i, tLRPC$Peer, !(tLRPC$Peer instanceof TLRPC$TL_peerUser));
    }

    public static CharSequence peerNameWithIcon(int i, TLRPC$Peer tLRPC$Peer, boolean z) {
        TLRPC$Chat chat;
        if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
            TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$Peer.user_id));
            if (user != null) {
                if (z) {
                    return new SpannableStringBuilder(userSpan()).append((CharSequence) " ").append((CharSequence) UserObject.getUserName(user));
                }
                return UserObject.getUserName(user);
            }
            return "";
        } else if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
            TLRPC$Chat chat2 = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Peer.chat_id));
            if (chat2 != null) {
                if (z) {
                    return new SpannableStringBuilder(ChatObject.isChannelAndNotMegaGroup(chat2) ? channelSpan() : groupSpan()).append((CharSequence) " ").append((CharSequence) chat2.title);
                }
                return chat2.title;
            }
            return "";
        } else if (!(tLRPC$Peer instanceof TLRPC$TL_peerChannel) || (chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Peer.channel_id))) == null) {
            return "";
        } else {
            if (z) {
                return new SpannableStringBuilder(ChatObject.isChannelAndNotMegaGroup(chat) ? channelSpan() : groupSpan()).append((CharSequence) " ").append((CharSequence) chat.title);
            }
            return chat.title;
        }
    }

    public static CharSequence peerNameWithIcon(int i, long j) {
        return peerNameWithIcon(i, j, false);
    }

    public static CharSequence peerNameWithIcon(int i, long j, boolean z) {
        if (j >= 0) {
            TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
            return user != null ? UserObject.getUserName(user) : "";
        }
        TLRPC$Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-j));
        if (chat != null) {
            return new SpannableStringBuilder(ChatObject.isChannelAndNotMegaGroup(chat) ? channelSpan() : groupSpan()).append((CharSequence) " ").append((CharSequence) chat.title);
        }
        return "";
    }

    public CharSequence getReplyQuoteNameWithIcon() {
        CharSequence charSequence;
        CharSequence spannableStringBuilder;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null) {
            return "";
        }
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = tLRPC$Message.reply_to;
        CharSequence charSequence2 = null;
        if (tLRPC$MessageReplyHeader == null) {
            if (DialogObject.isChatDialog(getDialogId())) {
                charSequence = peerNameWithIcon(this.currentAccount, getDialogId());
            } else {
                spannableStringBuilder = peerNameWithIcon(this.currentAccount, getDialogId());
                charSequence2 = spannableStringBuilder;
                charSequence = null;
            }
        } else {
            if (tLRPC$MessageReplyHeader.reply_from != null) {
                TLRPC$Peer tLRPC$Peer = tLRPC$MessageReplyHeader.reply_to_peer_id;
                boolean z = tLRPC$Peer == null || DialogObject.getPeerDialogId(tLRPC$Peer) != getDialogId();
                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.reply_to.reply_from;
                TLRPC$Peer tLRPC$Peer2 = tLRPC$MessageFwdHeader.from_id;
                if (tLRPC$Peer2 != null) {
                    if (tLRPC$Peer2 instanceof TLRPC$TL_peerUser) {
                        spannableStringBuilder = peerNameWithIcon(this.currentAccount, tLRPC$Peer2, z);
                        charSequence2 = spannableStringBuilder;
                        charSequence = null;
                    } else {
                        charSequence = peerNameWithIcon(this.currentAccount, tLRPC$Peer2, z);
                    }
                } else {
                    TLRPC$Peer tLRPC$Peer3 = tLRPC$MessageFwdHeader.saved_from_peer;
                    if (tLRPC$Peer3 != null) {
                        if (tLRPC$Peer3 instanceof TLRPC$TL_peerUser) {
                            spannableStringBuilder = peerNameWithIcon(this.currentAccount, tLRPC$Peer3, z);
                        } else {
                            charSequence = peerNameWithIcon(this.currentAccount, tLRPC$Peer3, z);
                        }
                    } else if (!TextUtils.isEmpty(tLRPC$MessageFwdHeader.from_name)) {
                        if (z) {
                            spannableStringBuilder = new SpannableStringBuilder(userSpan()).append((CharSequence) " ").append((CharSequence) this.messageOwner.reply_to.reply_from.from_name);
                        } else {
                            spannableStringBuilder = new SpannableStringBuilder(this.messageOwner.reply_to.reply_from.from_name);
                        }
                    }
                    charSequence2 = spannableStringBuilder;
                    charSequence = null;
                }
            }
            charSequence = null;
        }
        TLRPC$Peer tLRPC$Peer4 = this.messageOwner.reply_to.reply_to_peer_id;
        if (tLRPC$Peer4 != null && DialogObject.getPeerDialogId(tLRPC$Peer4) != getDialogId()) {
            TLRPC$Peer tLRPC$Peer5 = this.messageOwner.reply_to.reply_to_peer_id;
            if (tLRPC$Peer5 instanceof TLRPC$TL_peerUser) {
                charSequence2 = peerNameWithIcon(this.currentAccount, tLRPC$Peer5, true);
            } else {
                charSequence = peerNameWithIcon(this.currentAccount, tLRPC$Peer5);
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
        if (charSequence == null || charSequence2 == null) {
            return charSequence != null ? charSequence : charSequence2 != null ? charSequence2 : LocaleController.getString(R.string.Loading);
        }
        return new SpannableStringBuilder(charSequence2).append((CharSequence) " ").append(charSequence);
    }

    public boolean hasLinkMediaToMakeSmall() {
        boolean z = !this.isRestrictedMessage && (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage);
        TLRPC$WebPage tLRPC$WebPage = z ? getMedia(this.messageOwner).webpage : null;
        String str = tLRPC$WebPage != null ? tLRPC$WebPage.type : null;
        return z && !isGiveawayOrGiveawayResults() && tLRPC$WebPage != null && (tLRPC$WebPage.photo != null || isVideoDocument(tLRPC$WebPage.document)) && !((TextUtils.isEmpty(tLRPC$WebPage.description) && TextUtils.isEmpty(tLRPC$WebPage.title)) || ((isSponsored() && this.sponsoredWebPage == null && this.sponsoredChannelPost == 0) || "telegram_megagroup".equals(str) || "telegram_background".equals(str) || "telegram_voicechat".equals(str) || "telegram_livestream".equals(str) || "telegram_user".equals(str) || "telegram_story".equals(str) || "telegram_channel_boost".equals(str) || "telegram_group_boost".equals(str) || "telegram_chat".equals(str)));
    }

    public boolean isLinkMediaSmall() {
        TLRPC$WebPage tLRPC$WebPage = !this.isRestrictedMessage && (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage) ? getMedia(this.messageOwner).webpage : null;
        String str = tLRPC$WebPage != null ? tLRPC$WebPage.type : null;
        return !(tLRPC$WebPage != null && TextUtils.isEmpty(tLRPC$WebPage.description) && TextUtils.isEmpty(tLRPC$WebPage.title)) && ("app".equals(str) || "profile".equals(str) || "article".equals(str) || "telegram_bot".equals(str) || "telegram_user".equals(str) || "telegram_channel".equals(str) || "telegram_megagroup".equals(str) || "telegram_voicechat".equals(str) || "telegram_livestream".equals(str) || "telegram_channel_boost".equals(str) || "telegram_group_boost".equals(str) || "telegram_chat".equals(str));
    }

    /* loaded from: classes3.dex */
    public static class TextRange {
        public boolean code;
        public int end;
        public String language;
        public boolean quote;
        public int start;

        public TextRange(int i, int i2) {
            this.start = i;
            this.end = i2;
        }

        public TextRange(int i, int i2, boolean z, boolean z2, String str) {
            this.start = i;
            this.end = i2;
            this.quote = z;
            this.code = z2;
            this.language = str;
        }
    }

    public static void cutIntoRanges(CharSequence charSequence, ArrayList<TextRange> arrayList) {
        Iterator it;
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
        for (int i = 0; i < quoteStyleSpanArr.length; i++) {
            quoteStyleSpanArr[i].span.adaptLineHeight = false;
            int spanStart = spanned.getSpanStart(quoteStyleSpanArr[i]);
            int spanEnd = spanned.getSpanEnd(quoteStyleSpanArr[i]);
            treeSet.add(Integer.valueOf(spanStart));
            hashMap.put(Integer.valueOf(spanStart), Integer.valueOf((hashMap.containsKey(Integer.valueOf(spanStart)) ? ((Integer) hashMap.get(Integer.valueOf(spanStart))).intValue() : 0) | 1));
            treeSet.add(Integer.valueOf(spanEnd));
            hashMap.put(Integer.valueOf(spanEnd), Integer.valueOf((hashMap.containsKey(Integer.valueOf(spanEnd)) ? ((Integer) hashMap.get(Integer.valueOf(spanEnd))).intValue() : 0) | 2));
        }
        CodeHighlighting.Span[] spanArr = (CodeHighlighting.Span[]) spanned.getSpans(0, spanned.length(), CodeHighlighting.Span.class);
        for (int i2 = 0; i2 < spanArr.length; i2++) {
            int spanStart2 = spanned.getSpanStart(spanArr[i2]);
            int spanEnd2 = spanned.getSpanEnd(spanArr[i2]);
            treeSet.add(Integer.valueOf(spanStart2));
            hashMap.put(Integer.valueOf(spanStart2), Integer.valueOf((hashMap.containsKey(Integer.valueOf(spanStart2)) ? ((Integer) hashMap.get(Integer.valueOf(spanStart2))).intValue() : 0) | 4));
            treeSet.add(Integer.valueOf(spanEnd2));
            hashMap.put(Integer.valueOf(spanEnd2), Integer.valueOf((hashMap.containsKey(Integer.valueOf(spanEnd2)) ? ((Integer) hashMap.get(Integer.valueOf(spanEnd2))).intValue() : 0) | 8));
        }
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        for (Iterator it2 = treeSet.iterator(); it2.hasNext(); it2 = it) {
            int intValue = ((Integer) it2.next()).intValue();
            int intValue2 = ((Integer) hashMap.get(Integer.valueOf(intValue))).intValue();
            if (i6 != intValue) {
                int i7 = intValue - 1;
                if (i7 >= 0 && i7 < charSequence.length() && charSequence.charAt(i7) == '\n') {
                    intValue--;
                }
                int i8 = intValue;
                String str = null;
                if ((intValue2 & 8) != 0 && i5 < spanArr.length) {
                    str = spanArr[i5].lng;
                    i5++;
                }
                String str2 = str;
                it = it2;
                arrayList.add(new TextRange(i6, i8, i3 > 0, i4 > 0, str2));
                i6 = i8 + 1;
                if (i6 >= charSequence.length() || charSequence.charAt(i8) != '\n') {
                    i6 = i8;
                }
            } else {
                it = it2;
            }
            if ((intValue2 & 2) != 0) {
                i3--;
            }
            if ((intValue2 & 1) != 0) {
                i3++;
            }
            if ((intValue2 & 8) != 0) {
                i4--;
            }
            if ((intValue2 & 4) != 0) {
                i4++;
            }
        }
        if (i6 < charSequence.length()) {
            arrayList.add(new TextRange(i6, charSequence.length(), i3 > 0, i4 > 0, null));
        }
    }

    public void toggleChannelRecommendations() {
        expandChannelRecommendations(!this.channelJoinedExpanded);
    }

    public void expandChannelRecommendations(boolean z) {
        this.channelJoinedExpanded = z;
        MessagesController.getInstance(this.currentAccount).getMainSettings().edit().putBoolean("c" + getDialogId() + "_rec", z).apply();
    }

    public static int findQuoteStart(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return -1;
        }
        if (i == -1) {
            return str.indexOf(str2);
        }
        if (str2.length() + i >= str.length() || !str.startsWith(str2, i)) {
            int indexOf = str.indexOf(str2, i);
            int lastIndexOf = str.lastIndexOf(str2, i);
            return indexOf == -1 ? lastIndexOf : (lastIndexOf != -1 && indexOf - i >= i - lastIndexOf) ? lastIndexOf : indexOf;
        }
        return i;
    }

    public void applyQuickReply(String str, int i) {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null) {
            return;
        }
        if (i != 0) {
            tLRPC$Message.flags |= 1073741824;
            tLRPC$Message.quick_reply_shortcut_id = i;
            TLRPC$TL_inputQuickReplyShortcutId tLRPC$TL_inputQuickReplyShortcutId = new TLRPC$TL_inputQuickReplyShortcutId();
            tLRPC$TL_inputQuickReplyShortcutId.shortcut_id = i;
            this.messageOwner.quick_reply_shortcut = tLRPC$TL_inputQuickReplyShortcutId;
        } else if (str != null) {
            TLRPC$TL_inputQuickReplyShortcut tLRPC$TL_inputQuickReplyShortcut = new TLRPC$TL_inputQuickReplyShortcut();
            tLRPC$TL_inputQuickReplyShortcut.shortcut = str;
            this.messageOwner.quick_reply_shortcut = tLRPC$TL_inputQuickReplyShortcut;
        } else {
            tLRPC$Message.flags &= -1073741825;
            tLRPC$Message.quick_reply_shortcut_id = 0;
            tLRPC$Message.quick_reply_shortcut = null;
        }
    }

    public static int getQuickReplyId(TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message == null) {
            return 0;
        }
        if ((tLRPC$Message.flags & 1073741824) != 0) {
            return tLRPC$Message.quick_reply_shortcut_id;
        }
        TLRPC$InputQuickReplyShortcut tLRPC$InputQuickReplyShortcut = tLRPC$Message.quick_reply_shortcut;
        if (tLRPC$InputQuickReplyShortcut instanceof TLRPC$TL_inputQuickReplyShortcutId) {
            return ((TLRPC$TL_inputQuickReplyShortcutId) tLRPC$InputQuickReplyShortcut).shortcut_id;
        }
        return 0;
    }

    public static int getQuickReplyId(int i, TLRPC$Message tLRPC$Message) {
        QuickRepliesController.QuickReply findReply;
        if (tLRPC$Message == null) {
            return 0;
        }
        if ((tLRPC$Message.flags & 1073741824) != 0) {
            return tLRPC$Message.quick_reply_shortcut_id;
        }
        TLRPC$InputQuickReplyShortcut tLRPC$InputQuickReplyShortcut = tLRPC$Message.quick_reply_shortcut;
        if (tLRPC$InputQuickReplyShortcut instanceof TLRPC$TL_inputQuickReplyShortcutId) {
            return ((TLRPC$TL_inputQuickReplyShortcutId) tLRPC$InputQuickReplyShortcut).shortcut_id;
        }
        String quickReplyName = getQuickReplyName(tLRPC$Message);
        if (quickReplyName == null || (findReply = QuickRepliesController.getInstance(i).findReply(quickReplyName)) == null) {
            return 0;
        }
        return findReply.id;
    }

    public int getQuickReplyId() {
        return getQuickReplyId(this.messageOwner);
    }

    public static String getQuickReplyName(TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message == null) {
            return null;
        }
        TLRPC$InputQuickReplyShortcut tLRPC$InputQuickReplyShortcut = tLRPC$Message.quick_reply_shortcut;
        if (tLRPC$InputQuickReplyShortcut instanceof TLRPC$TL_inputQuickReplyShortcut) {
            return ((TLRPC$TL_inputQuickReplyShortcut) tLRPC$InputQuickReplyShortcut).shortcut;
        }
        return null;
    }

    public String getQuickReplyName() {
        return getQuickReplyName(this.messageOwner);
    }

    public String getQuickReplyDisplayName() {
        String quickReplyName = getQuickReplyName();
        if (quickReplyName != null) {
            return quickReplyName;
        }
        QuickRepliesController.QuickReply findReply = QuickRepliesController.getInstance(this.currentAccount).findReply(getQuickReplyId());
        return findReply != null ? findReply.name : "";
    }

    public static boolean isQuickReply(TLRPC$Message tLRPC$Message) {
        return (tLRPC$Message == null || ((tLRPC$Message.flags & 1073741824) == 0 && tLRPC$Message.quick_reply_shortcut == null)) ? false : true;
    }

    public boolean isQuickReply() {
        return isQuickReply(this.messageOwner);
    }
}
