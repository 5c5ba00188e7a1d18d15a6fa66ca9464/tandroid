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
import org.telegram.tgnet.TLRPC$BotApp;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$ChannelAdminLogEventAction;
import org.telegram.tgnet.TLRPC$ChannelLocation;
import org.telegram.tgnet.TLRPC$ChannelParticipant;
import org.telegram.tgnet.TLRPC$Chat;
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
import org.telegram.tgnet.TLRPC$MessageReactions;
import org.telegram.tgnet.TLRPC$MessageReactor;
import org.telegram.tgnet.TLRPC$MessageReplies;
import org.telegram.tgnet.TLRPC$MessageReplyHeader;
import org.telegram.tgnet.TLRPC$Page;
import org.telegram.tgnet.TLRPC$PageBlock;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$PollAnswer;
import org.telegram.tgnet.TLRPC$PollResults;
import org.telegram.tgnet.TLRPC$Reaction;
import org.telegram.tgnet.TLRPC$ReactionCount;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$RestrictionReason;
import org.telegram.tgnet.TLRPC$SecureValueType;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_availableEffect;
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
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantSubExtend;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantToggleBan;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantUnmute;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionParticipantVolume;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionPinTopic;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionStartGroupCall;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionStopPoll;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleAntiSpam;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleForum;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleInvites;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleNoForwards;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleSignatureProfiles;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleSignatures;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionToggleSlowMode;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionUpdatePinned;
import org.telegram.tgnet.TLRPC$TL_channelLocation;
import org.telegram.tgnet.TLRPC$TL_channelLocationEmpty;
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
import org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC$TL_documentAttributeHasStickers;
import org.telegram.tgnet.TLRPC$TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_documentEncrypted;
import org.telegram.tgnet.TLRPC$TL_emojiStatusEmpty;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
import org.telegram.tgnet.TLRPC$TL_factCheck;
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
import org.telegram.tgnet.TLRPC$TL_messageActionGiftStars;
import org.telegram.tgnet.TLRPC$TL_messageActionGiveawayLaunch;
import org.telegram.tgnet.TLRPC$TL_messageActionGiveawayResults;
import org.telegram.tgnet.TLRPC$TL_messageActionGroupCall;
import org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled;
import org.telegram.tgnet.TLRPC$TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall;
import org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation;
import org.telegram.tgnet.TLRPC$TL_messageActionPaymentRefunded;
import org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC$TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC$TL_messageActionPrizeStars;
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
import org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia;
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
import org.telegram.tgnet.TLRPC$TL_messageReactor;
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
import org.telegram.tgnet.TLRPC$TL_peerColor;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_peerUser_layer131;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonBusy;
import org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonMissed;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photoCachedSize;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC$TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC$TL_pollAnswerVoters;
import org.telegram.tgnet.TLRPC$TL_reactionCount;
import org.telegram.tgnet.TLRPC$TL_reactionCustomEmoji;
import org.telegram.tgnet.TLRPC$TL_reactionEmoji;
import org.telegram.tgnet.TLRPC$TL_reactionPaid;
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
    public ArrayList<TLRPC$PollAnswer> checkedVotes;
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
    public boolean notime;
    public int overrideLinkColor;
    public long overrideLinkEmoji;
    public StoriesController.StoriesList parentStoriesList;
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
    public int realDate;
    public MessageObject replyMessageObject;
    public boolean replyTextEllipsized;
    public boolean replyTextRevealed;
    public TLRPC$TL_forumTopic replyToForumTopic;
    public boolean resendAsIs;
    public boolean revealingMediaSpoilers;
    public boolean scheduled;
    public boolean scheduledSent;
    public int searchType;
    private CharSequence secretOnceSpan;
    private CharSequence secretPlaySpan;
    public SendAnimationData sendAnimationData;
    public TLRPC$Peer sendAsPeer;
    public boolean sendPreview;
    public MediaController.PhotoEntry sendPreviewEntry;
    public boolean settingAvatar;
    public boolean shouldRemoveVideoEditedInfo;
    private boolean spoiledLoginCode;
    public String sponsoredAdditionalInfo;
    public String sponsoredButtonText;
    public boolean sponsoredCanReport;
    public TLRPC$TL_peerColor sponsoredColor;
    public byte[] sponsoredId;
    public String sponsoredInfo;
    public TLRPC$MessageMedia sponsoredMedia;
    public TLRPC$Photo sponsoredPhoto;
    public boolean sponsoredRecommended;
    public String sponsoredTitle;
    public String sponsoredUrl;
    public int stableId;
    public TL_stories$StoryItem storyItem;
    private TLRPC$WebPage storyMentionWebpage;
    public BitmapDrawable strippedThumb;
    public ArrayList<TextLayoutBlock> textLayoutBlocks;
    public int textWidth;
    public float textXOffset;
    public Drawable[] topicIconDrawable;
    public int totalAnimatedEmojiCount;
    public boolean translated;
    public int type;
    public StoriesController.UploadingStory uploadingStory;
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

    /* loaded from: classes3.dex */
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

        private float multiHeight(float[] fArr, int i, int i2) {
            float f = 0.0f;
            while (i < i2) {
                f += fArr[i];
                i++;
            }
            return this.maxSizeWidth / f;
        }

        /* JADX WARN: Code restructure failed: missing block: B:252:0x07a8, code lost:
            if (r15[2] > r15[3]) goto L304;
         */
        /* JADX WARN: Code restructure failed: missing block: B:308:0x0890, code lost:
            if ((r1.flags & 1) != 0) goto L127;
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x00ac, code lost:
            if ((org.telegram.messenger.MessageObject.getMedia(r15.messageOwner) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaInvoice) == false) goto L88;
         */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0068  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x006b  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x006e  */
        /* JADX WARN: Removed duplicated region for block: B:294:0x0862  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00c2  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x00ef  */
        /* JADX WARN: Removed duplicated region for block: B:67:0x00f2  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x00ff  */
        /* JADX WARN: Removed duplicated region for block: B:72:0x0106  */
        /* JADX WARN: Removed duplicated region for block: B:78:0x011c  */
        /* JADX WARN: Removed duplicated region for block: B:81:0x0137  */
        /* JADX WARN: Removed duplicated region for block: B:90:0x014e  */
        /* JADX WARN: Removed duplicated region for block: B:93:0x0153  */
        /* JADX WARN: Removed duplicated region for block: B:95:0x015c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            int i;
            byte b;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            float f;
            int i7;
            int i8;
            int i9;
            GroupedMessagePosition groupedMessagePosition;
            float f2;
            int i10;
            boolean z;
            int i11;
            MessageObject messageObject;
            int i12;
            TLRPC$Message tLRPC$Message;
            float f3;
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
            } else if (size >= 1) {
                StringBuilder sb = new StringBuilder();
                this.hasSibling = false;
                this.hasCaption = false;
                this.captionAbove = false;
                int i13 = this.reversed ? size - 1 : 0;
                boolean z2 = false;
                float f4 = 1.0f;
                boolean z3 = false;
                boolean z4 = false;
                boolean z5 = true;
                while (true) {
                    if (this.reversed) {
                        if (i13 < 0) {
                            break;
                        }
                        messageObject = this.messages.get(i13);
                        if (i13 != (!this.reversed ? size - 1 : 0)) {
                            messageObject.isOutOwnerCached = bool2;
                            z4 = messageObject.isOutOwner();
                            if (!z4) {
                                TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message2.fwd_from;
                                if (tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.saved_from_peer != null) {
                                    i12 = size;
                                } else if (tLRPC$Message2.from_id instanceof TLRPC$TL_peerUser) {
                                    TLRPC$Peer tLRPC$Peer = tLRPC$Message2.peer_id;
                                    i12 = size;
                                    if (tLRPC$Peer.channel_id == 0) {
                                        if (tLRPC$Peer.chat_id == 0) {
                                            if (!(MessageObject.getMedia(tLRPC$Message2) instanceof TLRPC$TL_messageMediaGame)) {
                                            }
                                        }
                                    }
                                }
                                z2 = true;
                                if (!messageObject.isMusic() || messageObject.isDocument()) {
                                    this.isDocuments = true;
                                }
                            }
                            i12 = size;
                            z2 = false;
                            if (!messageObject.isMusic()) {
                            }
                            this.isDocuments = true;
                        } else {
                            i12 = size;
                        }
                        tLRPC$Message = messageObject.messageOwner;
                        if (tLRPC$Message != null && tLRPC$Message.invert_media) {
                            this.captionAbove = true;
                        }
                        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        GroupedMessagePosition groupedMessagePosition2 = new GroupedMessagePosition();
                        groupedMessagePosition2.last = this.reversed ? i13 == i12 + (-1) : i13 == 0;
                        float f5 = closestPhotoSizeWithSize != null ? 1.0f : closestPhotoSizeWithSize.w / closestPhotoSizeWithSize.h;
                        groupedMessagePosition2.aspectRatio = f5;
                        sb.append(f5 <= 1.2f ? "w" : f5 < 0.8f ? "n" : "q");
                        f3 = groupedMessagePosition2.aspectRatio;
                        f4 += f3;
                        if (f3 > 2.0f) {
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
                        i13 = !this.reversed ? i13 - 1 : i13 + 1;
                        bool2 = bool;
                        size = i12;
                        z2 = z6;
                    } else {
                        if (i13 >= size) {
                            break;
                        }
                        messageObject = this.messages.get(i13);
                        if (i13 != (!this.reversed ? size - 1 : 0)) {
                        }
                        tLRPC$Message = messageObject.messageOwner;
                        if (tLRPC$Message != null) {
                            this.captionAbove = true;
                        }
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        GroupedMessagePosition groupedMessagePosition22 = new GroupedMessagePosition();
                        groupedMessagePosition22.last = this.reversed ? i13 == i12 + (-1) : i13 == 0;
                        if (closestPhotoSizeWithSize2 != null) {
                        }
                        groupedMessagePosition22.aspectRatio = f5;
                        sb.append(f5 <= 1.2f ? "w" : f5 < 0.8f ? "n" : "q");
                        f3 = groupedMessagePosition22.aspectRatio;
                        f4 += f3;
                        if (f3 > 2.0f) {
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
                        size = i12;
                        z2 = z62;
                    }
                }
                int i14 = size;
                if (this.isDocuments) {
                    for (int i15 = 0; i15 < i14; i15++) {
                        GroupedMessagePosition groupedMessagePosition3 = this.posArray.get(i15);
                        groupedMessagePosition3.flags = 3;
                        if (i15 == 0) {
                            groupedMessagePosition3.flags = 7;
                        } else if (i15 == i14 - 1) {
                            groupedMessagePosition3.flags = 11;
                            groupedMessagePosition3.last = true;
                            groupedMessagePosition3.edge = true;
                            groupedMessagePosition3.aspectRatio = 1.0f;
                            groupedMessagePosition3.minX = (byte) 0;
                            groupedMessagePosition3.maxX = (byte) 0;
                            byte b2 = (byte) i15;
                            groupedMessagePosition3.minY = b2;
                            groupedMessagePosition3.maxY = b2;
                            groupedMessagePosition3.spanSize = 1000;
                            groupedMessagePosition3.pw = this.maxSizeWidth;
                            groupedMessagePosition3.ph = 100.0f;
                        }
                        groupedMessagePosition3.last = false;
                        groupedMessagePosition3.edge = true;
                        groupedMessagePosition3.aspectRatio = 1.0f;
                        groupedMessagePosition3.minX = (byte) 0;
                        groupedMessagePosition3.maxX = (byte) 0;
                        byte b22 = (byte) i15;
                        groupedMessagePosition3.minY = b22;
                        groupedMessagePosition3.maxY = b22;
                        groupedMessagePosition3.spanSize = 1000;
                        groupedMessagePosition3.pw = this.maxSizeWidth;
                        groupedMessagePosition3.ph = 100.0f;
                    }
                    return;
                }
                if (z2) {
                    this.maxSizeWidth -= 50;
                    i = NotificationCenter.notificationsCountUpdated;
                } else {
                    i = NotificationCenter.storyQualityUpdate;
                }
                int dp = AndroidUtilities.dp(120.0f);
                Point point = AndroidUtilities.displaySize;
                int dp2 = (int) (AndroidUtilities.dp(120.0f) / (Math.min(point.x, point.y) / this.maxSizeWidth));
                Point point2 = AndroidUtilities.displaySize;
                float f6 = this.maxSizeWidth;
                int dp3 = (int) (AndroidUtilities.dp(40.0f) / (Math.min(point2.x, point2.y) / f6));
                float f7 = f6 / 814.0f;
                float f8 = f4 / i14;
                float dp4 = AndroidUtilities.dp(100.0f) / 814.0f;
                if (i14 != 1) {
                    if (z3 || !(i14 == 2 || i14 == 3 || i14 == 4)) {
                        int size2 = this.posArray.size();
                        float[] fArr = new float[size2];
                        for (int i16 = 0; i16 < i14; i16++) {
                            if (f8 > 1.1f) {
                                fArr[i16] = Math.max(1.0f, this.posArray.get(i16).aspectRatio);
                            } else {
                                fArr[i16] = Math.min(1.0f, this.posArray.get(i16).aspectRatio);
                            }
                            fArr[i16] = Math.max(0.66667f, Math.min(1.7f, fArr[i16]));
                        }
                        ArrayList arrayList = new ArrayList();
                        for (int i17 = 1; i17 < size2; i17++) {
                            int i18 = size2 - i17;
                            if (i17 <= 3 && i18 <= 3) {
                                arrayList.add(new MessageGroupedLayoutAttempt(i17, i18, multiHeight(fArr, 0, i17), multiHeight(fArr, i17, size2)));
                            }
                        }
                        for (int i19 = 1; i19 < size2 - 1; i19++) {
                            int i20 = 1;
                            while (true) {
                                int i21 = size2 - i19;
                                if (i20 < i21) {
                                    int i22 = i21 - i20;
                                    if (i19 <= 3) {
                                        if (i20 <= (f8 < 0.85f ? 4 : 3) && i22 <= 3) {
                                            int i23 = i19 + i20;
                                            arrayList.add(new MessageGroupedLayoutAttempt(i19, i20, i22, multiHeight(fArr, 0, i19), multiHeight(fArr, i19, i23), multiHeight(fArr, i23, size2)));
                                        }
                                    }
                                    i20++;
                                }
                            }
                        }
                        for (int i24 = 1; i24 < size2 - 2; i24++) {
                            int i25 = 1;
                            while (true) {
                                int i26 = size2 - i24;
                                if (i25 < i26) {
                                    int i27 = 1;
                                    while (true) {
                                        int i28 = i26 - i25;
                                        if (i27 < i28) {
                                            int i29 = i28 - i27;
                                            if (i24 > 3 || i25 > 3 || i27 > 3 || i29 > 3) {
                                                i3 = i26;
                                            } else {
                                                int i30 = i24 + i25;
                                                i3 = i26;
                                                int i31 = i30 + i27;
                                                arrayList.add(new MessageGroupedLayoutAttempt(i24, i25, i27, i29, multiHeight(fArr, 0, i24), multiHeight(fArr, i24, i30), multiHeight(fArr, i30, i31), multiHeight(fArr, i31, size2)));
                                            }
                                            i27++;
                                            i26 = i3;
                                        }
                                    }
                                    i25++;
                                }
                            }
                        }
                        float f9 = (this.maxSizeWidth / 3) * 4;
                        int i32 = 0;
                        MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                        float f10 = 0.0f;
                        while (i32 < arrayList.size()) {
                            MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = (MessageGroupedLayoutAttempt) arrayList.get(i32);
                            int i33 = 0;
                            float f11 = Float.MAX_VALUE;
                            float f12 = 0.0f;
                            while (true) {
                                float[] fArr2 = messageGroupedLayoutAttempt2.heights;
                                if (i33 >= fArr2.length) {
                                    break;
                                }
                                float f13 = fArr2[i33];
                                f12 += f13;
                                if (f13 < f11) {
                                    f11 = f13;
                                }
                                i33++;
                            }
                            float abs = Math.abs(f12 - f9);
                            int[] iArr = messageGroupedLayoutAttempt2.lineCounts;
                            float f14 = f9;
                            ArrayList arrayList2 = arrayList;
                            if (iArr.length > 1) {
                                int i34 = iArr[0];
                                int i35 = iArr[1];
                                if (i34 <= i35 && (iArr.length <= 2 || i35 <= iArr[2])) {
                                    if (iArr.length > 3) {
                                    }
                                }
                                abs *= 1.2f;
                            }
                            if (f11 < dp2) {
                                abs *= 1.5f;
                            }
                            if (messageGroupedLayoutAttempt == null || abs < f10) {
                                f10 = abs;
                                messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                            }
                            i32++;
                            f9 = f14;
                            arrayList = arrayList2;
                        }
                        if (messageGroupedLayoutAttempt == null) {
                            return;
                        }
                        int i36 = 0;
                        int i37 = 0;
                        byte b3 = 0;
                        while (true) {
                            int[] iArr2 = messageGroupedLayoutAttempt.lineCounts;
                            if (i37 >= iArr2.length) {
                                break;
                            }
                            int i38 = iArr2[i37];
                            float f15 = messageGroupedLayoutAttempt.heights[i37];
                            int i39 = this.maxSizeWidth;
                            int i40 = i38 - 1;
                            int max = Math.max((int) b3, i40);
                            int i41 = i39;
                            int i42 = 0;
                            GroupedMessagePosition groupedMessagePosition4 = null;
                            while (i42 < i38) {
                                float[] fArr3 = fArr;
                                int i43 = (int) (fArr[i36] * f15);
                                i41 -= i43;
                                int i44 = max;
                                GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(i36);
                                int i45 = i38;
                                int i46 = i37 == 0 ? 4 : 0;
                                if (i37 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                                    i46 |= 8;
                                }
                                if (i42 == 0) {
                                    i46 |= 1;
                                    if (z4) {
                                        groupedMessagePosition4 = groupedMessagePosition5;
                                    }
                                }
                                if (i42 == i40) {
                                    i46 |= 2;
                                    if (!z4) {
                                        i2 = i46;
                                        groupedMessagePosition4 = groupedMessagePosition5;
                                        groupedMessagePosition5.set(i42, i42, i37, i37, i43, Math.max(dp4, f15 / 814.0f), i2);
                                        i36++;
                                        i42++;
                                        max = i44;
                                        fArr = fArr3;
                                        i38 = i45;
                                    }
                                }
                                i2 = i46;
                                groupedMessagePosition5.set(i42, i42, i37, i37, i43, Math.max(dp4, f15 / 814.0f), i2);
                                i36++;
                                i42++;
                                max = i44;
                                fArr = fArr3;
                                i38 = i45;
                            }
                            groupedMessagePosition4.pw += i41;
                            groupedMessagePosition4.spanSize += i41;
                            i37++;
                            b3 = max;
                            fArr = fArr;
                        }
                        b = b3;
                    } else {
                        if (i14 == 2) {
                            GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(0);
                            GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(1);
                            String sb2 = sb.toString();
                            if (sb2.equals("ww")) {
                                double d = f7;
                                Double.isNaN(d);
                                if (f8 > d * 1.4d) {
                                    float f16 = groupedMessagePosition6.aspectRatio;
                                    float f17 = groupedMessagePosition7.aspectRatio;
                                    if (f16 - f17 < 0.2d) {
                                        float f18 = this.maxSizeWidth;
                                        float round = Math.round(Math.min(f18 / f16, Math.min(f18 / f17, 407.0f))) / 814.0f;
                                        groupedMessagePosition6.set(0, 0, 0, 0, this.maxSizeWidth, round, 7);
                                        groupedMessagePosition7.set(0, 0, 1, 1, this.maxSizeWidth, round, 11);
                                    }
                                }
                            }
                            if (sb2.equals("ww") || sb2.equals("qq")) {
                                int i47 = this.maxSizeWidth / 2;
                                float f19 = i47;
                                i4 = 0;
                                i5 = 0;
                                i6 = i47;
                                f = Math.round(Math.min(f19 / groupedMessagePosition6.aspectRatio, Math.min(f19 / groupedMessagePosition7.aspectRatio, 814.0f))) / 814.0f;
                                groupedMessagePosition6.set(0, 0, 0, 0, i6, f, 13);
                                i7 = 14;
                                i8 = 1;
                                i9 = 1;
                                groupedMessagePosition = groupedMessagePosition7;
                            } else {
                                float f20 = this.maxSizeWidth;
                                float f21 = groupedMessagePosition6.aspectRatio;
                                int max2 = (int) Math.max(0.4f * f20, Math.round((f20 / f21) / ((1.0f / f21) + (1.0f / groupedMessagePosition7.aspectRatio))));
                                int i48 = this.maxSizeWidth - max2;
                                if (i48 < dp2) {
                                    max2 -= dp2 - i48;
                                } else {
                                    dp2 = i48;
                                }
                                i4 = 0;
                                i5 = 0;
                                f = Math.min(814.0f, Math.round(Math.min(dp2 / groupedMessagePosition6.aspectRatio, max2 / groupedMessagePosition7.aspectRatio))) / 814.0f;
                                groupedMessagePosition6.set(0, 0, 0, 0, dp2, f, 13);
                                i7 = 14;
                                i8 = 1;
                                i9 = 1;
                                groupedMessagePosition = groupedMessagePosition7;
                                i6 = max2;
                            }
                        } else {
                            if (i14 == 3) {
                                GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(0);
                                GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(1);
                                GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(2);
                                if (sb.charAt(0) == 'n') {
                                    float f22 = groupedMessagePosition9.aspectRatio;
                                    float min = Math.min(407.0f, Math.round((this.maxSizeWidth * f22) / (groupedMessagePosition10.aspectRatio + f22)));
                                    int max3 = (int) Math.max(dp2, Math.min(this.maxSizeWidth * 0.5f, Math.round(Math.min(groupedMessagePosition10.aspectRatio * min, groupedMessagePosition9.aspectRatio * f2))));
                                    int round2 = Math.round(Math.min((groupedMessagePosition8.aspectRatio * 814.0f) + dp3, this.maxSizeWidth - max3));
                                    groupedMessagePosition8.set(0, 0, 0, 1, round2, 1.0f, 13);
                                    float f23 = (814.0f - min) / 814.0f;
                                    groupedMessagePosition9.set(1, 1, 0, 0, max3, f23, 6);
                                    float f24 = min / 814.0f;
                                    groupedMessagePosition10.set(0, 1, 1, 1, max3, f24, 10);
                                    int i49 = this.maxSizeWidth;
                                    groupedMessagePosition10.spanSize = i49;
                                    groupedMessagePosition8.siblingHeights = new float[]{f24, f23};
                                    if (z4) {
                                        groupedMessagePosition8.spanSize = i49 - max3;
                                    } else {
                                        groupedMessagePosition9.spanSize = i49 - round2;
                                        groupedMessagePosition10.leftSpanOffset = round2;
                                    }
                                    this.hasSibling = true;
                                } else {
                                    float round3 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition8.aspectRatio, 537.24005f)) / 814.0f;
                                    groupedMessagePosition8.set(0, 1, 0, 0, this.maxSizeWidth, round3, 7);
                                    int i50 = this.maxSizeWidth / 2;
                                    float f25 = 814.0f - round3;
                                    float f26 = i50;
                                    float min2 = Math.min(f25, Math.round(Math.min(f26 / groupedMessagePosition9.aspectRatio, f26 / groupedMessagePosition10.aspectRatio))) / 814.0f;
                                    if (min2 >= dp4) {
                                        dp4 = min2;
                                    }
                                    i4 = 1;
                                    i5 = 1;
                                    i6 = i50;
                                    f = dp4;
                                    groupedMessagePosition9.set(0, 0, 1, 1, i6, f, 9);
                                    i7 = 10;
                                    i8 = 1;
                                    i9 = 1;
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
                                    float f27 = dp2;
                                    int max4 = (int) Math.max(f27, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition12.aspectRatio * round5));
                                    int max5 = (int) Math.max(Math.max(f27, this.maxSizeWidth * 0.33f), groupedMessagePosition14.aspectRatio * round5);
                                    int i51 = (this.maxSizeWidth - max4) - max5;
                                    if (i51 < AndroidUtilities.dp(58.0f)) {
                                        int dp5 = AndroidUtilities.dp(58.0f) - i51;
                                        i51 = AndroidUtilities.dp(58.0f);
                                        int i52 = dp5 / 2;
                                        max4 -= i52;
                                        max5 -= dp5 - i52;
                                    }
                                    int i53 = max4;
                                    float min3 = Math.min(814.0f - round4, round5) / 814.0f;
                                    if (min3 >= dp4) {
                                        dp4 = min3;
                                    }
                                    float f28 = dp4;
                                    groupedMessagePosition12.set(0, 0, 1, 1, i53, f28, 9);
                                    groupedMessagePosition13.set(1, 1, 1, 1, i51, f28, 8);
                                    groupedMessagePosition14.set(2, 2, 1, 1, max5, f28, 10);
                                    b = 2;
                                } else {
                                    int max6 = Math.max(dp2, Math.round(814.0f / (((1.0f / groupedMessagePosition12.aspectRatio) + (1.0f / groupedMessagePosition13.aspectRatio)) + (1.0f / groupedMessagePosition14.aspectRatio))));
                                    float f29 = dp;
                                    float f30 = max6;
                                    float min4 = Math.min(0.33f, Math.max(f29, f30 / groupedMessagePosition12.aspectRatio) / 814.0f);
                                    float min5 = Math.min(0.33f, Math.max(f29, f30 / groupedMessagePosition13.aspectRatio) / 814.0f);
                                    float f31 = (1.0f - min4) - min5;
                                    int round6 = Math.round(Math.min((groupedMessagePosition11.aspectRatio * 814.0f) + dp3, this.maxSizeWidth - max6));
                                    groupedMessagePosition11.set(0, 0, 0, 2, round6, min4 + min5 + f31, 13);
                                    groupedMessagePosition12.set(1, 1, 0, 0, max6, min4, 6);
                                    groupedMessagePosition13.set(0, 1, 1, 1, max6, min5, 2);
                                    groupedMessagePosition13.spanSize = this.maxSizeWidth;
                                    groupedMessagePosition14.set(0, 1, 2, 2, max6, f31, 10);
                                    int i54 = this.maxSizeWidth;
                                    groupedMessagePosition14.spanSize = i54;
                                    if (z4) {
                                        groupedMessagePosition11.spanSize = i54 - max6;
                                    } else {
                                        groupedMessagePosition12.spanSize = i54 - round6;
                                        groupedMessagePosition13.leftSpanOffset = round6;
                                        groupedMessagePosition14.leftSpanOffset = round6;
                                    }
                                    groupedMessagePosition11.siblingHeights = new float[]{min4, min5, f31};
                                    this.hasSibling = true;
                                }
                            }
                            b = 1;
                        }
                        groupedMessagePosition.set(i8, i9, i5, i4, i6, f, i7);
                        b = 1;
                    }
                    i10 = 0;
                    while (i10 < i14) {
                        GroupedMessagePosition groupedMessagePosition15 = this.posArray.get(i10);
                        if (z4) {
                            if (groupedMessagePosition15.minX == 0) {
                                groupedMessagePosition15.spanSize += i;
                            }
                            if ((groupedMessagePosition15.flags & 2) != 0) {
                                z = true;
                                groupedMessagePosition15.edge = z;
                            }
                            MessageObject messageObject2 = this.messages.get(i10);
                            if (!z4 && messageObject2.needDrawAvatarInternal()) {
                                if (groupedMessagePosition15.edge) {
                                    int i55 = groupedMessagePosition15.spanSize;
                                    if (i55 != 1000) {
                                        groupedMessagePosition15.spanSize = i55 + 108;
                                    }
                                    groupedMessagePosition15.pw += 108;
                                    i11 = 1;
                                    i10 += i11;
                                } else if ((groupedMessagePosition15.flags & 2) != 0) {
                                    int i56 = groupedMessagePosition15.spanSize;
                                    if (i56 != 1000) {
                                        groupedMessagePosition15.spanSize = i56 - 108;
                                    } else {
                                        int i57 = groupedMessagePosition15.leftSpanOffset;
                                        if (i57 != 0) {
                                            groupedMessagePosition15.leftSpanOffset = i57 + 108;
                                        }
                                    }
                                    i11 = 1;
                                    i10 += i11;
                                }
                            }
                            i11 = 1;
                            i10 += i11;
                        } else {
                            if (groupedMessagePosition15.maxX == b || (groupedMessagePosition15.flags & 2) != 0) {
                                groupedMessagePosition15.spanSize += i;
                            }
                            z = true;
                        }
                    }
                }
                GroupedMessagePosition groupedMessagePosition16 = this.posArray.get(0);
                float f32 = this.maxSizeWidth / groupedMessagePosition16.aspectRatio;
                groupedMessagePosition16.set(0, 0, 0, 0, this.maxSizeWidth, Math.round(Math.min(f32, Math.min(f32, 407.0f))) / 814.0f, 15);
                b = 0;
                i10 = 0;
                while (i10 < i14) {
                }
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

    /* loaded from: classes3.dex */
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

    /* loaded from: classes3.dex */
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
                case 99:
                    if (replaceAll.equals("c")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 114:
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
                case 3304:
                    if (replaceAll.equals("go")) {
                        c = '\r';
                        break;
                    }
                    break;
                case 3401:
                    if (replaceAll.equals("js")) {
                        c = 14;
                        break;
                    }
                    break;
                case 3479:
                    if (replaceAll.equals("md")) {
                        c = 15;
                        break;
                    }
                    break;
                case 3593:
                    if (replaceAll.equals("py")) {
                        c = 16;
                        break;
                    }
                    break;
                case 3632:
                    if (replaceAll.equals("rb")) {
                        c = 17;
                        break;
                    }
                    break;
                case 3704:
                    if (replaceAll.equals("tl")) {
                        c = 18;
                        break;
                    }
                    break;
                case 3711:
                    if (replaceAll.equals("ts")) {
                        c = 19;
                        break;
                    }
                    break;
                case 96891:
                    if (replaceAll.equals("asm")) {
                        c = 20;
                        break;
                    }
                    break;
                case 98723:
                    if (replaceAll.equals("cpp")) {
                        c = 21;
                        break;
                    }
                    break;
                case 98819:
                    if (replaceAll.equals("css")) {
                        c = 22;
                        break;
                    }
                    break;
                case 98822:
                    if (replaceAll.equals("csv")) {
                        c = 23;
                        break;
                    }
                    break;
                case 104420:
                    if (replaceAll.equals("ini")) {
                        c = 24;
                        break;
                    }
                    break;
                case 105551:
                    if (replaceAll.equals("jsx")) {
                        c = 25;
                        break;
                    }
                    break;
                case 107512:
                    if (replaceAll.equals("lua")) {
                        c = 26;
                        break;
                    }
                    break;
                case 110968:
                    if (replaceAll.equals("php")) {
                        c = 27;
                        break;
                    }
                    break;
                case 114922:
                    if (replaceAll.equals("tlb")) {
                        c = 28;
                        break;
                    }
                    break;
                case 115161:
                    if (replaceAll.equals("tsx")) {
                        c = 29;
                        break;
                    }
                    break;
                case 118807:
                    if (replaceAll.equals("xml")) {
                        c = 30;
                        break;
                    }
                    break;
                case 119768:
                    if (replaceAll.equals("yml")) {
                        c = 31;
                        break;
                    }
                    break;
                case 3075967:
                    if (replaceAll.equals("dart")) {
                        c = ' ';
                        break;
                    }
                    break;
                case 3142865:
                    if (replaceAll.equals("fift")) {
                        c = '!';
                        break;
                    }
                    break;
                case 3154628:
                    if (replaceAll.equals("func")) {
                        c = '\"';
                        break;
                    }
                    break;
                case 3175934:
                    if (replaceAll.equals("glsl")) {
                        c = '#';
                        break;
                    }
                    break;
                case 3205725:
                    if (replaceAll.equals("hlsl")) {
                        c = '$';
                        break;
                    }
                    break;
                case 3213227:
                    if (replaceAll.equals("html")) {
                        c = '%';
                        break;
                    }
                    break;
                case 3213448:
                    if (replaceAll.equals("http")) {
                        c = '&';
                        break;
                    }
                    break;
                case 3254818:
                    if (replaceAll.equals("java")) {
                        c = '\'';
                        break;
                    }
                    break;
                case 3271912:
                    if (replaceAll.equals("json")) {
                        c = '(';
                        break;
                    }
                    break;
                case 3318169:
                    if (replaceAll.equals("less")) {
                        c = ')';
                        break;
                    }
                    break;
                case 3373901:
                    if (replaceAll.equals("nasm")) {
                        c = '*';
                        break;
                    }
                    break;
                case 3404364:
                    if (replaceAll.equals("objc")) {
                        c = '+';
                        break;
                    }
                    break;
                case 3511770:
                    if (replaceAll.equals("ruby")) {
                        c = ',';
                        break;
                    }
                    break;
                case 3512292:
                    if (replaceAll.equals("rust")) {
                        c = '-';
                        break;
                    }
                    break;
                case 3524784:
                    if (replaceAll.equals("scss")) {
                        c = '.';
                        break;
                    }
                    break;
                case 3561037:
                    if (replaceAll.equals("tl-b")) {
                        c = '/';
                        break;
                    }
                    break;
                case 3642020:
                    if (replaceAll.equals("wasm")) {
                        c = '0';
                        break;
                    }
                    break;
                case 3701415:
                    if (replaceAll.equals("yaml")) {
                        c = '1';
                        break;
                    }
                    break;
                case 94833107:
                    if (replaceAll.equals("cobol")) {
                        c = '2';
                        break;
                    }
                    break;
                case 101429325:
                    if (replaceAll.equals("json5")) {
                        c = '3';
                        break;
                    }
                    break;
                case 109854227:
                    if (replaceAll.equals("swift")) {
                        c = '4';
                        break;
                    }
                    break;
                case 188995949:
                    if (replaceAll.equals("javascript")) {
                        c = '5';
                        break;
                    }
                    break;
                case 213985633:
                    if (replaceAll.equals("autohotkey")) {
                        c = '6';
                        break;
                    }
                    break;
                case 246938863:
                    if (replaceAll.equals("markdown")) {
                        c = '7';
                        break;
                    }
                    break;
                case 1067478602:
                    if (replaceAll.equals("objectivec")) {
                        c = '8';
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
                case '\r':
                case 26:
                case ' ':
                case '!':
                case '\'':
                case '-':
                case '4':
                    return capitalizeFirst(str);
                case 7:
                case 16:
                    return "Python";
                case '\t':
                case 19:
                    return "TypeScript";
                case 11:
                case 18:
                case 20:
                case 22:
                case 23:
                case 24:
                case 25:
                case 27:
                case 29:
                case 30:
                case 31:
                case '#':
                case '$':
                case '%':
                case '&':
                case '(':
                case ')':
                case '*':
                case '.':
                case '0':
                case '1':
                case '2':
                case '3':
                    return str.toUpperCase();
                case 14:
                case '5':
                    return "JavaScript";
                case 15:
                case '7':
                    return "Markdown";
                case 17:
                case ',':
                    return "Ruby";
                case 21:
                    return "C++";
                case 28:
                case '/':
                    return "TL-B";
                case '\"':
                    return "FunC";
                case '+':
                case '8':
                    return "Objective-C";
                case '6':
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
                return;
            }
            this.language = str;
            Text text = new Text(capitalizeLanguage(str), (SharedConfig.fontSize - 1) - (CodeHighlighting.getTextSizeDecrement(i) / 2), AndroidUtilities.bold());
            this.languageLayout = text;
            this.languageHeight = ((int) (text.getTextSize() * 1.714f)) + AndroidUtilities.dp(4.0f);
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
        public final ArrayList<TextLayoutBlock> textLayoutBlocks = new ArrayList<>();
        public int textWidth;
        public float textXOffset;

        /* JADX WARN: Can't wrap try/catch for region: R(16:210|211|212|213|214|(1:216)(11:246|(1:248)|218|219|220|(1:222)|223|(2:225|(3:227|(5:231|232|(1:237)|234|235)|236))(1:243)|242|(1:241)(6:229|231|232|(0)|234|235)|236)|217|218|219|220|(0)|223|(0)(0)|242|(0)(0)|236) */
        /* JADX WARN: Can't wrap try/catch for region: R(43:117|(1:119)|120|(1:122)(1:356)|123|(1:125)(1:355)|126|(1:128)|(1:130)|(1:354)(1:135)|136|(2:138|(3:(1:336)|337|338)(1:141))(2:339|(8:341|(1:343)(1:353)|344|(1:346)(1:352)|347|(1:349)(1:351)|350|338))|142|(3:144|(1:146)(2:330|(1:332)(1:333))|147)(1:334)|148|(1:150)(1:(1:328)(1:329))|151|(3:153|(1:307)(4:159|(1:161)(1:306)|162|163)|164)(3:308|(2:310|311)(6:312|313|314|(1:321)(1:318)|319|320)|277)|165|(1:171)|172|173|174|(1:178)|179|180|181|182|(1:184)|185|(1:187)|188|(3:190|(7:192|193|194|195|196|198|199)|205)|206|(6:208|(16:210|211|212|213|214|(1:216)(11:246|(1:248)|218|219|220|(1:222)|223|(2:225|(3:227|(5:231|232|(1:237)|234|235)|236))(1:243)|242|(1:241)(6:229|231|232|(0)|234|235)|236)|217|218|219|220|(0)|223|(0)(0)|242|(0)(0)|236)|251|252|(2:(1:255)|256)(1:(1:284))|257)(3:285|(5:287|(1:289)(1:296)|290|(1:292)(1:295)|293)(1:297)|294)|258|(3:260|(1:262)(1:264)|263)|265|(1:282)(3:271|(1:273)(3:278|(1:280)|281)|274)|275|276|277|115) */
        /* JADX WARN: Code restructure failed: missing block: B:244:0x04ad, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:247:0x04b1, code lost:
            if (r8 == 0) goto L304;
         */
        /* JADX WARN: Code restructure failed: missing block: B:248:0x04b3, code lost:
            r31.textXOffset = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:249:0x04b6, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r12 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:252:0x04c3, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:253:0x04c4, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r0 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:288:0x0553, code lost:
            r2 = 0.0f;
         */
        /* JADX WARN: Removed duplicated region for block: B:101:0x01d8  */
        /* JADX WARN: Removed duplicated region for block: B:102:0x01df  */
        /* JADX WARN: Removed duplicated region for block: B:108:0x01f8  */
        /* JADX WARN: Removed duplicated region for block: B:109:0x01fa  */
        /* JADX WARN: Removed duplicated region for block: B:112:0x01ff  */
        /* JADX WARN: Removed duplicated region for block: B:113:0x0201  */
        /* JADX WARN: Removed duplicated region for block: B:136:0x0271  */
        /* JADX WARN: Removed duplicated region for block: B:291:0x055c  */
        /* JADX WARN: Removed duplicated region for block: B:294:0x0563  */
        /* JADX WARN: Removed duplicated region for block: B:298:0x0578  */
        /* JADX WARN: Removed duplicated region for block: B:301:0x058d  */
        /* JADX WARN: Removed duplicated region for block: B:395:0x059b A[ADDED_TO_REGION, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:397:0x059b A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:65:0x0107  */
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
            StaticLayout staticLayout2;
            boolean z4;
            float f;
            float f2;
            float f3;
            int i6;
            int i7;
            int i8;
            TLRPC$Message tLRPC$Message;
            MessageObject messageObject3 = messageObject;
            CharSequence charSequence4 = charSequence;
            this.text = charSequence4;
            this.textWidth = 0;
            boolean z5 = (messageObject3 == null || (tLRPC$Message = messageObject3.messageOwner) == null || !tLRPC$Message.noforwards) ? false : true;
            if (messageObject3 == null || z5) {
                z = z5;
            } else {
                TLRPC$Chat chat = MessagesController.getInstance(messageObject3.currentAccount).getChat(Long.valueOf(-messageObject.getDialogId()));
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
            float f4 = 32.0f;
            try {
                if (this.hasSingleQuote) {
                    dp = AndroidUtilities.dp(32.0f);
                } else if (!this.hasSingleCode) {
                    i2 = i;
                    StaticLayout makeStaticLayout = MessageObject.makeStaticLayout(charSequence, textPaint, i2, 1.0f, 0.0f, false);
                    SpannableStringBuilder spannableStringBuilder = charSequence4;
                    if (messageObject3 != null) {
                        spannableStringBuilder = charSequence4;
                        if (messageObject3.isRepostPreview) {
                            int i9 = messageObject3.type != 0 ? messageObject.hasValidGroupId() ? 7 : 12 : 22;
                            i9 = messageObject.isWebpage() ? i9 - 8 : i9;
                            spannableStringBuilder = charSequence4;
                            if (makeStaticLayout.getLineCount() > i9) {
                                String string = LocaleController.getString(R.string.ReadMore);
                                int ceil = (int) Math.ceil(textPaint.measureText("… " + string) + AndroidUtilities.dp(1.0f));
                                float f5 = 0.0f;
                                for (int i10 = 0; i10 < i9; i10++) {
                                    f5 = Math.max(f5, makeStaticLayout.getLineRight(i10));
                                }
                                int i11 = i9 - 1;
                                int lineStart = makeStaticLayout.getLineStart(i11);
                                int lineEnd = makeStaticLayout.getLineEnd(i11) - 1;
                                while (lineEnd >= lineStart && makeStaticLayout.getPrimaryHorizontal(lineEnd) >= f5 - ceil) {
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
                    int dp3 = !this.hasSingleQuote ? i2 + AndroidUtilities.dp(32.0f) : this.hasSingleCode ? i2 + AndroidUtilities.dp(15.0f) : i2;
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
                    StaticLayout staticLayout3 = staticLayout;
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
                        boolean z10 = textLayoutBlock.code;
                        if (z10) {
                            int i17 = textRange.end - textRange.start;
                            textPaint2 = i17 > 220 ? Theme.chat_msgTextCode3Paint : i17 > 80 ? Theme.chat_msgTextCode2Paint : Theme.chat_msgTextCodePaint;
                        } else {
                            textPaint2 = textPaint;
                        }
                        int dp5 = textLayoutBlock.quote ? dp3 - AndroidUtilities.dp(f4) : z10 ? dp3 - AndroidUtilities.dp(15.0f) : dp3;
                        if (size == 1) {
                            if (textLayoutBlock.code && !textLayoutBlock.quote && (staticLayout3.getText() instanceof Spannable)) {
                                SpannableString highlighted = !TextUtils.isEmpty(textRange.language) ? CodeHighlighting.getHighlighted(charSequence2.subSequence(textRange.start, textRange.end).toString(), textRange.language) : new SpannableString(charSequence2.subSequence(textRange.start, textRange.end));
                                textLayoutBlock.originalWidth = dp5;
                                staticLayout3 = MessageObject.makeStaticLayout(highlighted, textPaint2, dp5, 1.0f, 0.0f, false);
                                i16 = dp5;
                            } else {
                                textLayoutBlock.originalWidth = i16;
                            }
                            textLayoutBlock.textLayout = staticLayout3;
                            textLayoutBlock.charactersOffset = 0;
                            textLayoutBlock.charactersEnd = staticLayout3.getText().length();
                            textLayoutBlock.height = staticLayout3.getHeight();
                            textLayoutBlock.collapsedHeight = (int) Math.min(textPaint.getTextSize() * 1.4f * 3.0f, textLayoutBlock.height);
                        } else {
                            int i18 = textRange.start;
                            int i19 = textRange.end;
                            if (i19 < i18) {
                                charSequence3 = charSequence2;
                                i4 = dp3;
                                arrayList2 = arrayList;
                                z3 = z;
                                messageObject2 = messageObject3;
                            } else {
                                textLayoutBlock.charactersOffset = i18;
                                textLayoutBlock.charactersEnd = i19;
                                try {
                                    SpannableString valueOf = (!textLayoutBlock.code || textLayoutBlock.quote) ? SpannableString.valueOf(charSequence2.subSequence(i18, i19)) : CodeHighlighting.getHighlighted(charSequence2.subSequence(i18, i19).toString(), textRange.language);
                                    textLayoutBlock.originalWidth = dp5;
                                    StaticLayout makeStaticLayout2 = MessageObject.makeStaticLayout(valueOf, textPaint2, dp5, 1.0f, 0.0f, false);
                                    textLayoutBlock.textLayout = makeStaticLayout2;
                                    textLayoutBlock.height = makeStaticLayout2.getHeight();
                                    textLayoutBlock.collapsedHeight = (int) Math.min(textPaint.getTextSize() * 1.4f * 3.0f, textLayoutBlock.height);
                                } catch (Exception e2) {
                                    charSequence3 = charSequence2;
                                    i4 = dp3;
                                    arrayList2 = arrayList;
                                    z3 = z;
                                    messageObject2 = messageObject3;
                                    FileLog.e(e2);
                                }
                            }
                            i3++;
                            dp3 = i4;
                            messageObject3 = messageObject2;
                            charSequence2 = charSequence3;
                            arrayList = arrayList2;
                            z = z3;
                            f4 = 32.0f;
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
                        float f6 = lineLeft;
                        float f7 = textLayoutBlock.textLayout.getLineWidth(lineCount2 - 1);
                        int ceil3 = (int) Math.ceil(f7);
                        ceil3 = ceil3 > dp3 + 80 ? dp3 : ceil3;
                        int i20 = size - 1;
                        if (i3 == i20) {
                            this.lastLineWidth = ceil3;
                        }
                        float f8 = ceil3;
                        charSequence3 = charSequence2;
                        StaticLayout staticLayout4 = staticLayout3;
                        int i21 = i16;
                        int ceil4 = (int) Math.ceil(f8 + Math.max(0.0f, f6));
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
                            float f9 = 0.0f;
                            float f10 = 0.0f;
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
                                    AndroidUtilities.dp(32.0f);
                                } else {
                                    f2 = textLayoutBlock.code ? AndroidUtilities.dp(15.0f) : f2;
                                    f3 = textLayoutBlock.textLayout.getLineLeft(i25);
                                    if (f > dp3 + 20) {
                                        f = dp3;
                                        f3 = 0.0f;
                                    }
                                    if (f3 > 0.0f) {
                                        i6 = dp3;
                                        if (textLayoutBlock.textLayout.getParagraphDirection(i25) != -1) {
                                            textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                            i7 = 1;
                                            if (z11 && f3 == 0.0f) {
                                                try {
                                                    if (textLayoutBlock.textLayout.getParagraphDirection(i25) != i7) {
                                                    }
                                                } catch (Exception unused4) {
                                                }
                                                z11 = true;
                                            }
                                            f10 = Math.max(f10, f);
                                            float f11 = f3 + f;
                                            float max = Math.max(f9, f11);
                                            i24 = Math.max(i24, (int) Math.ceil(f));
                                            i26 = Math.max(i26, (int) Math.ceil(f11));
                                            i25++;
                                            z11 = z11;
                                            f9 = max;
                                            lineCount2 = i27;
                                            z = z4;
                                            dp3 = i6;
                                        }
                                    } else {
                                        i6 = dp3;
                                    }
                                    this.textXOffset = Math.min(this.textXOffset, f3);
                                    i7 = 1;
                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                    this.hasRtl = true;
                                    if (z11) {
                                        if (textLayoutBlock.textLayout.getParagraphDirection(i25) != i7) {
                                        }
                                        z11 = true;
                                    }
                                    f10 = Math.max(f10, f);
                                    float f112 = f3 + f;
                                    float max2 = Math.max(f9, f112);
                                    i24 = Math.max(i24, (int) Math.ceil(f));
                                    i26 = Math.max(i26, (int) Math.ceil(f112));
                                    i25++;
                                    z11 = z11;
                                    f9 = max2;
                                    lineCount2 = i27;
                                    z = z4;
                                    dp3 = i6;
                                }
                                f += f2;
                                f3 = textLayoutBlock.textLayout.getLineLeft(i25);
                                if (f > dp3 + 20) {
                                }
                                if (f3 > 0.0f) {
                                }
                                this.textXOffset = Math.min(this.textXOffset, f3);
                                i7 = 1;
                                textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                this.hasRtl = true;
                                if (z11) {
                                }
                                f10 = Math.max(f10, f);
                                float f1122 = f3 + f;
                                float max22 = Math.max(f9, f1122);
                                i24 = Math.max(i24, (int) Math.ceil(f));
                                i26 = Math.max(i26, (int) Math.ceil(f1122));
                                i25++;
                                z11 = z11;
                                f9 = max22;
                                lineCount2 = i27;
                                z = z4;
                                dp3 = i6;
                            }
                            int i28 = dp3;
                            z3 = z;
                            if (z11) {
                                if (i3 == i20) {
                                    this.lastLineWidth = ceil4;
                                }
                                f10 = f9;
                            } else if (i3 == i20) {
                                this.lastLineWidth = i24;
                            }
                            this.textWidth = Math.max(this.textWidth, (int) Math.ceil(f10));
                            ceil4 = i26;
                            i4 = i28;
                        } else {
                            int i29 = dp3;
                            arrayList2 = arrayList;
                            z3 = z;
                            if (f6 > 0.0f) {
                                float min2 = Math.min(this.textXOffset, f6);
                                this.textXOffset = min2;
                                i5 = min2 == 0.0f ? (int) (f8 + f6) : i23;
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
                            this.textWidth = (int) Math.max(this.textWidth, Math.min(text.getCurrentWidth() + AndroidUtilities.dp(15.0f), textLayoutBlock.textLayout == null ? 0.0f : staticLayout2.getWidth()));
                        }
                        messageObject2 = messageObject;
                        if (messageObject2 != null && !messageObject2.isSpoilersRevealed && !messageObject.spoiledLoginCode) {
                            SpoilerEffect.addSpoilers(null, textLayoutBlock.textLayout, -1, textLayoutBlock.quote ? ceil4 - AndroidUtilities.dp(32.0f) : textLayoutBlock.code ? ceil4 - AndroidUtilities.dp(15.0f) : ceil4, null, textLayoutBlock.spoilers);
                        }
                        staticLayout3 = staticLayout4;
                        i16 = i21;
                        i3++;
                        dp3 = i4;
                        messageObject3 = messageObject2;
                        charSequence2 = charSequence3;
                        arrayList = arrayList2;
                        z = z3;
                        f4 = 32.0f;
                    }
                    return;
                } else {
                    dp = AndroidUtilities.dp(15.0f);
                }
                StaticLayout makeStaticLayout3 = MessageObject.makeStaticLayout(charSequence, textPaint, i2, 1.0f, 0.0f, false);
                SpannableStringBuilder spannableStringBuilder2 = charSequence4;
                if (messageObject3 != null) {
                }
                staticLayout = makeStaticLayout3;
                charSequence2 = spannableStringBuilder2;
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
                StaticLayout staticLayout32 = staticLayout;
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

    /* loaded from: classes3.dex */
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
                if (z) {
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
                }
                return null;
            } catch (Throwable unused) {
                return null;
            }
        }
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2) {
        this(i, tLRPC$Message, null, null, null, longSparseArray, longSparseArray2, z, z2, 0L, false, false, false);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2, boolean z3) {
        this(i, tLRPC$Message, null, null, null, longSparseArray, longSparseArray2, z, z2, 0L, false, false, z3);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, LongSparseArray longSparseArray, boolean z, boolean z2) {
        this(i, tLRPC$Message, longSparseArray, (LongSparseArray) null, z, z2);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, String str, String str2, String str3, boolean z, boolean z2, boolean z3, boolean z4) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.actionDeleteGroupEventId = -1L;
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

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, boolean z, boolean z2) {
        this(i, tLRPC$Message, abstractMap, abstractMap2, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, boolean z, boolean z2, long j) {
        this(i, tLRPC$Message, null, abstractMap, abstractMap2, null, null, z, z2, j);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, AbstractMap<Long, TLRPC$User> abstractMap, boolean z, boolean z2) {
        this(i, tLRPC$Message, abstractMap, (AbstractMap<Long, TLRPC$Chat>) null, z, z2);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2, long j) {
        this(i, tLRPC$Message, messageObject, abstractMap, abstractMap2, longSparseArray, longSparseArray2, z, z2, j, false, false, false);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2, long j, boolean z3, boolean z4, boolean z5) {
        this(i, tLRPC$Message, messageObject, abstractMap, abstractMap2, longSparseArray, longSparseArray2, z, z2, j, z3, z4, z5, 0);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2, long j, boolean z3, boolean z4, boolean z5, int i2) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.actionDeleteGroupEventId = -1L;
        this.overrideLinkColor = -1;
        this.overrideLinkEmoji = -1L;
        this.topicIconDrawable = new Drawable[1];
        this.spoiledLoginCode = false;
        this.translated = false;
        Theme.createCommonMessageResources();
        this.isRepostPreview = z3;
        this.isRepostVideoPreview = z4;
        this.isSaved = z5 || getDialogId(tLRPC$Message) == UserConfig.getInstance(i).getClientUserId();
        this.searchType = i2;
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
        int i3 = gregorianCalendar.get(6);
        int i4 = gregorianCalendar.get(1);
        int i5 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i3));
        this.dateKeyInt = (i5 * 10000) + i4 + (i3 * MediaController.VIDEO_BITRATE_480);
        this.monthKey = String.format("%d_%02d", Integer.valueOf(i4), Integer.valueOf(i5));
        createMessageSendInfo();
        generateCaption();
        if (z) {
            TextPaint textPaint = getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
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

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, boolean z, boolean z2) {
        this(i, tLRPC$Message, messageObject, null, null, null, null, z, z2, 0L);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, boolean z, boolean z2) {
        this(i, tLRPC$Message, null, null, null, null, null, z, z2, 0L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0164, code lost:
        if (r40.megagroup != false) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0b16, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0b5d, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0b5f, code lost:
        r0.media.webpage.description = org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.EventLogOriginalCaptionEmpty);
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0b6c, code lost:
        r0.media.webpage.description = r1.message;
        r1 = r1.entities;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01df, code lost:
        if (r40.megagroup != false) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01e1, code lost:
        r1 = org.telegram.messenger.R.string.EventLogGroupJoined;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x01e5, code lost:
        r1 = org.telegram.messenger.R.string.EventLogChannelJoined;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0216, code lost:
        if ((r12.new_participant instanceof org.telegram.tgnet.TLRPC$TL_channelParticipant) != false) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:828:0x12be, code lost:
        if (r2.length() == 0) goto L1017;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1070:0x18a5  */
    /* JADX WARN: Removed duplicated region for block: B:1073:0x18f7  */
    /* JADX WARN: Removed duplicated region for block: B:1075:0x18fa  */
    /* JADX WARN: Removed duplicated region for block: B:1092:0x198a  */
    /* JADX WARN: Removed duplicated region for block: B:1095:0x1995 A[ADDED_TO_REGION, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:1096:0x1996  */
    /* JADX WARN: Removed duplicated region for block: B:1100:0x19ab  */
    /* JADX WARN: Removed duplicated region for block: B:1101:0x19af  */
    /* JADX WARN: Removed duplicated region for block: B:1104:0x19bb  */
    /* JADX WARN: Removed duplicated region for block: B:1107:0x19c7  */
    /* JADX WARN: Removed duplicated region for block: B:1108:0x19ca  */
    /* JADX WARN: Removed duplicated region for block: B:1111:0x19d2  */
    /* JADX WARN: Removed duplicated region for block: B:1112:0x19d5  */
    /* JADX WARN: Removed duplicated region for block: B:1120:0x1a07  */
    /* JADX WARN: Removed duplicated region for block: B:499:0x0b2b  */
    /* JADX WARN: Removed duplicated region for block: B:501:0x0b35  */
    /* JADX WARN: Type inference failed for: r12v30 */
    /* JADX WARN: Type inference failed for: r12v31, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r12v36 */
    /* JADX WARN: Type inference failed for: r12v37 */
    /* JADX WARN: Type inference failed for: r14v39 */
    /* JADX WARN: Type inference failed for: r14v45 */
    /* JADX WARN: Type inference failed for: r14v46 */
    /* JADX WARN: Type inference failed for: r1v67, types: [java.lang.String, java.util.ArrayList] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MessageObject(int i, TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent, ArrayList<MessageObject> arrayList, HashMap<String, ArrayList<MessageObject>> hashMap, TLRPC$Chat tLRPC$Chat, int[] iArr, boolean z) {
        String str;
        TLRPC$User tLRPC$User;
        boolean z2;
        TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent2;
        TLRPC$User tLRPC$User2;
        TLRPC$Chat tLRPC$Chat2;
        CharSequence charSequence;
        TLRPC$Message tLRPC$Message;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant2;
        MessagesController messagesController;
        StringBuilder sb;
        char c;
        String str2;
        String str3;
        String str4;
        String str5;
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
        CharSequence replaceWithLink2;
        TLObject tLObject;
        String formatString;
        SpannableStringBuilder spannableStringBuilder6;
        TLRPC$Message tLRPC$Message2;
        char c2;
        String formatPluralString;
        int i3;
        TLRPC$Chat user;
        CharSequence charSequence2;
        TLObject tLObject2;
        TLRPC$TL_message tLRPC$TL_message;
        String user2;
        int i4;
        int i5;
        ArrayList<TLRPC$MessageEntity> arrayList2;
        ArrayList<TLRPC$MessageEntity> arrayList3;
        TLRPC$Peer tLRPC$Peer;
        String str6;
        TLRPC$TL_message tLRPC$TL_message2;
        TLRPC$TL_messageMediaEmpty tLRPC$TL_messageMediaEmpty;
        TLRPC$WebPage tLRPC$WebPage;
        String str7;
        TLRPC$Chat chat;
        int i6;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$User tLRPC$User3;
        String str8;
        StringBuilder sb2;
        boolean z4;
        char c3;
        int i7;
        String formatPluralString2;
        int i8;
        int i9;
        boolean z5;
        MediaController mediaController;
        ArrayList<MessageObject> arrayList4;
        int[] iArr2;
        ?? r12;
        int i10;
        int[] iArr3;
        Spannable replaceAnimatedEmoji;
        int i11;
        boolean z6;
        boolean z7;
        SpannableStringBuilder spannableStringBuilder7;
        TLRPC$Chat tLRPC$Chat3;
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.actionDeleteGroupEventId = -1L;
        this.overrideLinkColor = -1;
        this.overrideLinkEmoji = -1L;
        this.topicIconDrawable = new Drawable[1];
        this.spoiledLoginCode = false;
        this.translated = false;
        this.currentEvent = tLRPC$TL_channelAdminLogEvent;
        this.currentAccount = i;
        if (tLRPC$TL_channelAdminLogEvent.user_id > 0) {
            str = "%2$s";
            tLRPC$User = MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEvent.user_id));
        } else {
            str = "%2$s";
            tLRPC$User = null;
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(tLRPC$TL_channelAdminLogEvent.date * 1000);
        int i12 = gregorianCalendar.get(6);
        int i13 = gregorianCalendar.get(1);
        int i14 = gregorianCalendar.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i13), Integer.valueOf(i14), Integer.valueOf(i12));
        this.dateKeyInt = i13 + (i14 * 1000) + (i12 * 100000);
        this.monthKey = String.format("%d_%02d", Integer.valueOf(i13), Integer.valueOf(i14));
        TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
        tLRPC$TL_peerChannel.channel_id = tLRPC$Chat.id;
        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction = tLRPC$TL_channelAdminLogEvent.action;
        String str9 = "";
        String str10 = str;
        String str11 = "un1";
        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTitle) {
            String str12 = ((TLRPC$TL_channelAdminLogEventActionChangeTitle) tLRPC$ChannelAdminLogEventAction).new_value;
            string = tLRPC$Chat.megagroup ? LocaleController.formatString("EventLogEditedGroupTitle", R.string.EventLogEditedGroupTitle, str12) : LocaleController.formatString("EventLogEditedChannelTitle", R.string.EventLogEditedChannelTitle, str12);
        } else {
            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangePhoto) {
                TLRPC$TL_channelAdminLogEventActionChangePhoto tLRPC$TL_channelAdminLogEventActionChangePhoto = (TLRPC$TL_channelAdminLogEventActionChangePhoto) tLRPC$ChannelAdminLogEventAction;
                TLRPC$TL_messageService tLRPC$TL_messageService = new TLRPC$TL_messageService();
                this.messageOwner = tLRPC$TL_messageService;
                if (tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo instanceof TLRPC$TL_photoEmpty) {
                    tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatDeletePhoto();
                    i3 = tLRPC$Chat.megagroup ? R.string.EventLogRemovedWGroupPhoto : R.string.EventLogRemovedChannelPhoto;
                } else {
                    tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatEditPhoto();
                    this.messageOwner.action.photo = tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo;
                    i3 = tLRPC$Chat.megagroup ? isVideoAvatar() ? R.string.EventLogEditedGroupVideo : R.string.EventLogEditedGroupPhoto : isVideoAvatar() ? R.string.EventLogEditedChannelVideo : R.string.EventLogEditedChannelPhoto;
                }
            } else if (!(tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoin)) {
                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantLeave) {
                    TLRPC$TL_messageService tLRPC$TL_messageService2 = new TLRPC$TL_messageService();
                    this.messageOwner = tLRPC$TL_messageService2;
                    tLRPC$TL_messageService2.action = new TLRPC$TL_messageActionChatDeleteUser();
                    this.messageOwner.action.user_id = tLRPC$TL_channelAdminLogEvent.user_id;
                    i3 = tLRPC$Chat.megagroup ? R.string.EventLogLeftGroup : R.string.EventLogLeftChannel;
                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantInvite) {
                    TLRPC$TL_messageService tLRPC$TL_messageService3 = new TLRPC$TL_messageService();
                    this.messageOwner = tLRPC$TL_messageService3;
                    tLRPC$TL_messageService3.action = new TLRPC$TL_messageActionChatAddUser();
                    long peerId = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantInvite) tLRPC$ChannelAdminLogEventAction).participant.peer);
                    TLObject user3 = peerId > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId));
                    TLRPC$Peer tLRPC$Peer2 = this.messageOwner.from_id;
                    if (!(tLRPC$Peer2 instanceof TLRPC$TL_peerUser) || peerId != tLRPC$Peer2.user_id) {
                        string = replaceWithLink(LocaleController.getString(R.string.EventLogAdded), "un2", user3);
                        this.messageText = string;
                    }
                } else {
                    boolean z8 = tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin;
                    if (z8) {
                        z2 = z8;
                    } else {
                        boolean z9 = tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleBan;
                        if (z9) {
                            z2 = z8;
                            TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                            str2 = "/";
                            if (tLRPC$TL_channelAdminLogEventActionParticipantToggleBan.prev_participant instanceof TLRPC$TL_channelParticipantAdmin) {
                            }
                        } else {
                            str2 = "/";
                        }
                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) {
                            TLRPC$TL_channelAdminLogEventActionDefaultBannedRights tLRPC$TL_channelAdminLogEventActionDefaultBannedRights = (TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) tLRPC$ChannelAdminLogEventAction;
                            TLRPC$TL_message tLRPC$TL_message3 = new TLRPC$TL_message();
                            this.messageOwner = tLRPC$TL_message3;
                            tLRPC$TL_message3.realId = -1;
                            TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights = tLRPC$TL_channelAdminLogEventActionDefaultBannedRights.prev_banned_rights;
                            TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights2 = tLRPC$TL_channelAdminLogEventActionDefaultBannedRights.new_banned_rights;
                            StringBuilder sb3 = new StringBuilder(LocaleController.getString(R.string.EventLogDefaultPermissions));
                            tLRPC$TL_chatBannedRights = tLRPC$TL_chatBannedRights == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights;
                            tLRPC$TL_chatBannedRights2 = tLRPC$TL_chatBannedRights2 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights2;
                            if (tLRPC$TL_chatBannedRights.send_messages != tLRPC$TL_chatBannedRights2.send_messages) {
                                sb3.append('\n');
                                sb3.append('\n');
                                sb3.append(!tLRPC$TL_chatBannedRights2.send_messages ? '+' : '-');
                                sb3.append(' ');
                                sb3.append(LocaleController.getString(R.string.EventLogRestrictedSendMessages));
                                z5 = true;
                            } else {
                                z5 = false;
                            }
                            if (tLRPC$TL_chatBannedRights.send_stickers != tLRPC$TL_chatBannedRights2.send_stickers || tLRPC$TL_chatBannedRights.send_inline != tLRPC$TL_chatBannedRights2.send_inline || tLRPC$TL_chatBannedRights.send_gifs != tLRPC$TL_chatBannedRights2.send_gifs || tLRPC$TL_chatBannedRights.send_games != tLRPC$TL_chatBannedRights2.send_games) {
                                if (!z5) {
                                    sb3.append('\n');
                                    z5 = true;
                                }
                                sb3.append('\n');
                                sb3.append(!tLRPC$TL_chatBannedRights2.send_stickers ? '+' : '-');
                                sb3.append(' ');
                                sb3.append(LocaleController.getString(R.string.EventLogRestrictedSendStickers));
                            }
                            if (tLRPC$TL_chatBannedRights.send_media != tLRPC$TL_chatBannedRights2.send_media) {
                                if (!z5) {
                                    sb3.append('\n');
                                    z5 = true;
                                }
                                sb3.append('\n');
                                sb3.append(!tLRPC$TL_chatBannedRights2.send_media ? '+' : '-');
                                sb3.append(' ');
                                sb3.append(LocaleController.getString(R.string.EventLogRestrictedSendMedia));
                            }
                            if (tLRPC$TL_chatBannedRights.send_polls != tLRPC$TL_chatBannedRights2.send_polls) {
                                if (!z5) {
                                    sb3.append('\n');
                                    z5 = true;
                                }
                                sb3.append('\n');
                                sb3.append(!tLRPC$TL_chatBannedRights2.send_polls ? '+' : '-');
                                sb3.append(' ');
                                sb3.append(LocaleController.getString(R.string.EventLogRestrictedSendPolls));
                            }
                            if (tLRPC$TL_chatBannedRights.embed_links != tLRPC$TL_chatBannedRights2.embed_links) {
                                if (!z5) {
                                    sb3.append('\n');
                                    z5 = true;
                                }
                                sb3.append('\n');
                                sb3.append(!tLRPC$TL_chatBannedRights2.embed_links ? '+' : '-');
                                sb3.append(' ');
                                sb3.append(LocaleController.getString(R.string.EventLogRestrictedSendEmbed));
                            }
                            if (tLRPC$TL_chatBannedRights.change_info != tLRPC$TL_chatBannedRights2.change_info) {
                                if (!z5) {
                                    sb3.append('\n');
                                    z5 = true;
                                }
                                sb3.append('\n');
                                sb3.append(!tLRPC$TL_chatBannedRights2.change_info ? '+' : '-');
                                sb3.append(' ');
                                sb3.append(LocaleController.getString(R.string.EventLogRestrictedChangeInfo));
                            }
                            if (tLRPC$TL_chatBannedRights.invite_users != tLRPC$TL_chatBannedRights2.invite_users) {
                                if (!z5) {
                                    sb3.append('\n');
                                    z5 = true;
                                }
                                sb3.append('\n');
                                sb3.append(!tLRPC$TL_chatBannedRights2.invite_users ? '+' : '-');
                                sb3.append(' ');
                                sb3.append(LocaleController.getString(R.string.EventLogRestrictedInviteUsers));
                            }
                            if (tLRPC$TL_chatBannedRights.pin_messages != tLRPC$TL_chatBannedRights2.pin_messages) {
                                if (!z5) {
                                    sb3.append('\n');
                                }
                                sb3.append('\n');
                                sb3.append(!tLRPC$TL_chatBannedRights2.pin_messages ? '+' : '-');
                                sb3.append(' ');
                                sb3.append(LocaleController.getString(R.string.EventLogRestrictedPinMessages));
                            }
                            charSequence = sb3.toString();
                            tLRPC$Chat2 = tLRPC$Chat;
                            tLRPC$Message = null;
                            TLRPC$User tLRPC$User4 = tLRPC$User;
                            tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                            tLRPC$User2 = tLRPC$User4;
                        } else {
                            if (z9) {
                                TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2 = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                                TLRPC$TL_message tLRPC$TL_message4 = new TLRPC$TL_message();
                                this.messageOwner = tLRPC$TL_message4;
                                tLRPC$TL_message4.realId = -1;
                                long peerId2 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.peer);
                                TLObject user4 = peerId2 > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId2)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId2));
                                TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights3 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.banned_rights;
                                TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights4 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.new_participant.banned_rights;
                                if (!tLRPC$Chat.megagroup || (tLRPC$TL_chatBannedRights4 != null && tLRPC$TL_chatBannedRights4.view_messages && (tLRPC$TL_chatBannedRights3 == null || tLRPC$TL_chatBannedRights4.until_date == tLRPC$TL_chatBannedRights3.until_date))) {
                                    tLRPC$User3 = tLRPC$User;
                                    str8 = "";
                                    String string2 = LocaleController.getString((tLRPC$TL_chatBannedRights4 == null || !(tLRPC$TL_chatBannedRights3 == null || tLRPC$TL_chatBannedRights4.view_messages)) ? R.string.EventLogChannelUnrestricted : R.string.EventLogChannelRestricted);
                                    charSequence = String.format(string2, getUserName(user4, this.messageOwner.entities, string2.indexOf("%1$s")));
                                } else {
                                    if (tLRPC$TL_chatBannedRights4 != null && !AndroidUtilities.isBannedForever(tLRPC$TL_chatBannedRights4)) {
                                        sb2 = new StringBuilder();
                                        int i15 = tLRPC$TL_chatBannedRights4.until_date - tLRPC$TL_channelAdminLogEvent.date;
                                        int i16 = ((i15 / 60) / 60) / 24;
                                        int i17 = i15 - (86400 * i16);
                                        str8 = "";
                                        int i18 = (i17 / 60) / 60;
                                        int i19 = (i17 - (i18 * 3600)) / 60;
                                        int i20 = 3;
                                        int i21 = 0;
                                        int i22 = 0;
                                        while (true) {
                                            if (i21 >= i20) {
                                                tLRPC$User3 = tLRPC$User;
                                                break;
                                            }
                                            tLRPC$User3 = tLRPC$User;
                                            if (i21 != 0) {
                                                i7 = i16;
                                                if (i21 == 1) {
                                                    if (i18 != 0) {
                                                        formatPluralString2 = LocaleController.formatPluralString("Hours", i18, new Object[0]);
                                                        i8 = i22 + 1;
                                                        i9 = i8;
                                                    }
                                                    i9 = i22;
                                                    formatPluralString2 = null;
                                                } else {
                                                    if (i19 != 0) {
                                                        formatPluralString2 = LocaleController.formatPluralString("Minutes", i19, new Object[0]);
                                                        i8 = i22 + 1;
                                                        i9 = i8;
                                                    }
                                                    i9 = i22;
                                                    formatPluralString2 = null;
                                                }
                                            } else if (i16 != 0) {
                                                i8 = i22 + 1;
                                                i7 = i16;
                                                formatPluralString2 = LocaleController.formatPluralString("Days", i16, new Object[0]);
                                                i9 = i8;
                                            } else {
                                                i7 = i16;
                                                i9 = i22;
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
                                            i21++;
                                            i16 = i7;
                                            i22 = i9;
                                            tLRPC$User = tLRPC$User3;
                                            i20 = 3;
                                        }
                                    } else {
                                        tLRPC$User3 = tLRPC$User;
                                        str8 = "";
                                        sb2 = new StringBuilder(LocaleController.getString(R.string.UserRestrictionsUntilForever));
                                    }
                                    String string3 = LocaleController.getString(R.string.EventLogRestrictedUntil);
                                    StringBuilder sb4 = new StringBuilder(String.format(string3, getUserName(user4, this.messageOwner.entities, string3.indexOf("%1$s")), sb2.toString()));
                                    tLRPC$TL_chatBannedRights3 = tLRPC$TL_chatBannedRights3 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights3;
                                    tLRPC$TL_chatBannedRights4 = tLRPC$TL_chatBannedRights4 == null ? new TLRPC$TL_chatBannedRights() : tLRPC$TL_chatBannedRights4;
                                    if (tLRPC$TL_chatBannedRights3.view_messages != tLRPC$TL_chatBannedRights4.view_messages) {
                                        sb4.append('\n');
                                        sb4.append('\n');
                                        sb4.append(!tLRPC$TL_chatBannedRights4.view_messages ? '+' : '-');
                                        sb4.append(' ');
                                        sb4.append(LocaleController.getString(R.string.EventLogRestrictedReadMessages));
                                        z4 = true;
                                    } else {
                                        z4 = false;
                                    }
                                    if (tLRPC$TL_chatBannedRights3.send_messages != tLRPC$TL_chatBannedRights4.send_messages) {
                                        if (!z4) {
                                            sb4.append('\n');
                                            z4 = true;
                                        }
                                        sb4.append('\n');
                                        sb4.append(!tLRPC$TL_chatBannedRights4.send_messages ? '+' : '-');
                                        sb4.append(' ');
                                        sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendMessages));
                                    }
                                    if (tLRPC$TL_chatBannedRights3.send_stickers != tLRPC$TL_chatBannedRights4.send_stickers || tLRPC$TL_chatBannedRights3.send_inline != tLRPC$TL_chatBannedRights4.send_inline || tLRPC$TL_chatBannedRights3.send_gifs != tLRPC$TL_chatBannedRights4.send_gifs || tLRPC$TL_chatBannedRights3.send_games != tLRPC$TL_chatBannedRights4.send_games) {
                                        if (!z4) {
                                            sb4.append('\n');
                                            z4 = true;
                                        }
                                        sb4.append('\n');
                                        sb4.append(!tLRPC$TL_chatBannedRights4.send_stickers ? '+' : '-');
                                        sb4.append(' ');
                                        sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendStickers));
                                    }
                                    if (tLRPC$TL_chatBannedRights3.send_media != tLRPC$TL_chatBannedRights4.send_media) {
                                        if (!z4) {
                                            sb4.append('\n');
                                            z4 = true;
                                        }
                                        sb4.append('\n');
                                        sb4.append(!tLRPC$TL_chatBannedRights4.send_media ? '+' : '-');
                                        sb4.append(' ');
                                        sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendMedia));
                                    }
                                    if (tLRPC$TL_chatBannedRights3.send_polls != tLRPC$TL_chatBannedRights4.send_polls) {
                                        if (!z4) {
                                            sb4.append('\n');
                                            z4 = true;
                                        }
                                        sb4.append('\n');
                                        sb4.append(!tLRPC$TL_chatBannedRights4.send_polls ? '+' : '-');
                                        sb4.append(' ');
                                        sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendPolls));
                                    }
                                    if (tLRPC$TL_chatBannedRights3.embed_links != tLRPC$TL_chatBannedRights4.embed_links) {
                                        if (!z4) {
                                            sb4.append('\n');
                                            z4 = true;
                                        }
                                        sb4.append('\n');
                                        sb4.append(!tLRPC$TL_chatBannedRights4.embed_links ? '+' : '-');
                                        sb4.append(' ');
                                        sb4.append(LocaleController.getString(R.string.EventLogRestrictedSendEmbed));
                                    }
                                    if (tLRPC$TL_chatBannedRights3.change_info != tLRPC$TL_chatBannedRights4.change_info) {
                                        if (!z4) {
                                            sb4.append('\n');
                                            z4 = true;
                                        }
                                        sb4.append('\n');
                                        sb4.append(!tLRPC$TL_chatBannedRights4.change_info ? '+' : '-');
                                        sb4.append(' ');
                                        sb4.append(LocaleController.getString(R.string.EventLogRestrictedChangeInfo));
                                    }
                                    if (tLRPC$TL_chatBannedRights3.invite_users != tLRPC$TL_chatBannedRights4.invite_users) {
                                        if (!z4) {
                                            sb4.append('\n');
                                            z4 = true;
                                        }
                                        sb4.append('\n');
                                        sb4.append(!tLRPC$TL_chatBannedRights4.invite_users ? '+' : '-');
                                        sb4.append(' ');
                                        sb4.append(LocaleController.getString(R.string.EventLogRestrictedInviteUsers));
                                    }
                                    if (tLRPC$TL_chatBannedRights3.pin_messages != tLRPC$TL_chatBannedRights4.pin_messages) {
                                        if (z4) {
                                            c3 = '\n';
                                        } else {
                                            c3 = '\n';
                                            sb4.append('\n');
                                        }
                                        sb4.append(c3);
                                        sb4.append(!tLRPC$TL_chatBannedRights4.pin_messages ? '+' : '-');
                                        sb4.append(' ');
                                        sb4.append(LocaleController.getString(R.string.EventLogRestrictedPinMessages));
                                    }
                                    charSequence = sb4.toString();
                                }
                                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                tLRPC$Chat2 = tLRPC$Chat;
                                str9 = str8;
                                tLRPC$User2 = tLRPC$User3;
                            } else {
                                TLRPC$User tLRPC$User5 = tLRPC$User;
                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionUpdatePinned) {
                                    TLRPC$TL_channelAdminLogEventActionUpdatePinned tLRPC$TL_channelAdminLogEventActionUpdatePinned = (TLRPC$TL_channelAdminLogEventActionUpdatePinned) tLRPC$ChannelAdminLogEventAction;
                                    tLRPC$Message = tLRPC$TL_channelAdminLogEventActionUpdatePinned.message;
                                    tLRPC$User2 = tLRPC$User5;
                                    if (tLRPC$User5 == null || tLRPC$User2.id != 136817688 || (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) == null || !(tLRPC$MessageFwdHeader.from_id instanceof TLRPC$TL_peerChannel)) {
                                        i3 = ((tLRPC$Message instanceof TLRPC$TL_messageEmpty) || !tLRPC$Message.pinned) ? R.string.EventLogUnpinnedMessages : R.string.EventLogPinnedMessages;
                                        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                        str9 = "";
                                        string = LocaleController.getString(i3);
                                        str5 = "un1";
                                        charSequence2 = string;
                                        tLRPC$Chat3 = tLRPC$User2;
                                        str11 = str5;
                                        charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
                                        tLRPC$Chat2 = tLRPC$Chat;
                                    } else {
                                        TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$TL_channelAdminLogEventActionUpdatePinned.message.fwd_from.from_id.channel_id));
                                        TLRPC$Message tLRPC$Message3 = tLRPC$TL_channelAdminLogEventActionUpdatePinned.message;
                                        charSequence2 = LocaleController.getString(((tLRPC$Message3 instanceof TLRPC$TL_messageEmpty) || !tLRPC$Message3.pinned) ? R.string.EventLogUnpinnedMessages : R.string.EventLogPinnedMessages);
                                        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                        str9 = "";
                                        tLRPC$Chat3 = chat2;
                                        charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
                                        tLRPC$Chat2 = tLRPC$Chat;
                                    }
                                } else {
                                    tLRPC$User2 = tLRPC$User5;
                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStopPoll) {
                                        tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionStopPoll) tLRPC$ChannelAdminLogEventAction).message;
                                        i3 = ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPoll) && ((TLRPC$TL_messageMediaPoll) getMedia(tLRPC$Message)).poll.quiz) ? R.string.EventLogStopQuiz : R.string.EventLogStopPoll;
                                    } else {
                                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSignatures) {
                                            i3 = ((TLRPC$TL_channelAdminLogEventActionToggleSignatures) tLRPC$ChannelAdminLogEventAction).new_value ? R.string.EventLogToggledSignaturesOn : R.string.EventLogToggledSignaturesOff;
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantSubExtend) {
                                            i3 = R.string.EventLogSubExtend;
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSignatureProfiles) {
                                            i3 = ((TLRPC$TL_channelAdminLogEventActionToggleSignatureProfiles) tLRPC$ChannelAdminLogEventAction).value ? R.string.EventLogToggledSignaturesProfilesOn : R.string.EventLogToggledSignaturesProfilesOff;
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleInvites) {
                                            i3 = ((TLRPC$TL_channelAdminLogEventActionToggleInvites) tLRPC$ChannelAdminLogEventAction).new_value ? R.string.EventLogToggledInvitesOn : R.string.EventLogToggledInvitesOff;
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                            tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionDeleteMessage) tLRPC$ChannelAdminLogEventAction).message;
                                            if (tLRPC$User2 == null || tLRPC$User2.id != MessagesController.getInstance(this.currentAccount).telegramAntispamUserId) {
                                                i3 = R.string.EventLogDeletedMessages;
                                            } else {
                                                charSequence = LocaleController.getString(R.string.EventLogDeletedMessages).replace("un1", UserObject.getUserName(tLRPC$User2));
                                                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                                tLRPC$Chat2 = tLRPC$Chat;
                                                str9 = "";
                                            }
                                        } else {
                                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) {
                                                TLRPC$TL_channelAdminLogEventActionChangeLinkedChat tLRPC$TL_channelAdminLogEventActionChangeLinkedChat = (TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) tLRPC$ChannelAdminLogEventAction;
                                                long j = tLRPC$TL_channelAdminLogEventActionChangeLinkedChat.new_value;
                                                long j2 = tLRPC$TL_channelAdminLogEventActionChangeLinkedChat.prev_value;
                                                if (tLRPC$Chat.megagroup) {
                                                    if (j == 0) {
                                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
                                                        i6 = R.string.EventLogRemovedLinkedChannel;
                                                    } else {
                                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                                                        i6 = R.string.EventLogChangedLinkedChannel;
                                                    }
                                                } else if (j == 0) {
                                                    chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
                                                    i6 = R.string.EventLogRemovedLinkedGroup;
                                                } else {
                                                    chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                                                    i6 = R.string.EventLogChangedLinkedGroup;
                                                }
                                                charSequence2 = replaceWithLink(LocaleController.getString(i6), "un1", tLRPC$User2);
                                                this.messageText = charSequence2;
                                                str11 = chat;
                                                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                                str9 = "";
                                                user = chat;
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) {
                                                i3 = ((TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) tLRPC$ChannelAdminLogEventAction).new_value ? R.string.EventLogToggledInvitesHistoryOff : R.string.EventLogToggledInvitesHistoryOn;
                                                tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                                str9 = "";
                                                tLRPC$Message = null;
                                                string = LocaleController.getString(i3);
                                                str5 = "un1";
                                                charSequence2 = string;
                                                tLRPC$Chat3 = tLRPC$User2;
                                                str11 = str5;
                                                charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
                                                tLRPC$Chat2 = tLRPC$Chat;
                                            } else {
                                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAbout) {
                                                    this.messageText = replaceWithLink(LocaleController.getString(tLRPC$Chat.megagroup ? R.string.EventLogEditedGroupDescription : R.string.EventLogEditedChannelDescription), "un1", tLRPC$User2);
                                                    tLRPC$TL_message2 = new TLRPC$TL_message();
                                                    tLRPC$TL_message2.out = false;
                                                    tLRPC$TL_message2.unread = false;
                                                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                                                    tLRPC$TL_message2.from_id = tLRPC$TL_peerUser;
                                                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                                    tLRPC$TL_peerUser.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                    tLRPC$TL_message2.peer_id = tLRPC$TL_peerChannel;
                                                    tLRPC$TL_message2.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                    TLRPC$TL_channelAdminLogEventActionChangeAbout tLRPC$TL_channelAdminLogEventActionChangeAbout = (TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$TL_channelAdminLogEvent2.action;
                                                    tLRPC$TL_message2.message = tLRPC$TL_channelAdminLogEventActionChangeAbout.new_value;
                                                    if (TextUtils.isEmpty(tLRPC$TL_channelAdminLogEventActionChangeAbout.prev_value)) {
                                                        str9 = "";
                                                        tLRPC$TL_messageMediaEmpty = new TLRPC$TL_messageMediaEmpty();
                                                        tLRPC$TL_message2.media = tLRPC$TL_messageMediaEmpty;
                                                        tLRPC$TL_message = tLRPC$TL_message2;
                                                    } else {
                                                        TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage = new TLRPC$TL_messageMediaWebPage();
                                                        tLRPC$TL_message2.media = tLRPC$TL_messageMediaWebPage;
                                                        tLRPC$TL_messageMediaWebPage.webpage = new TLRPC$TL_webPage();
                                                        TLRPC$WebPage tLRPC$WebPage2 = tLRPC$TL_message2.media.webpage;
                                                        tLRPC$WebPage2.flags = 10;
                                                        str9 = "";
                                                        tLRPC$WebPage2.display_url = str9;
                                                        tLRPC$WebPage2.url = str9;
                                                        tLRPC$WebPage2.site_name = LocaleController.getString(R.string.EventLogPreviousGroupDescription);
                                                        tLRPC$WebPage = tLRPC$TL_message2.media.webpage;
                                                        str7 = ((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                                        tLRPC$WebPage.description = str7;
                                                        tLRPC$TL_message = tLRPC$TL_message2;
                                                    }
                                                } else {
                                                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                                    str9 = "";
                                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTheme) {
                                                        this.messageText = replaceWithLink(LocaleController.getString(tLRPC$Chat.megagroup ? R.string.EventLogEditedGroupTheme : R.string.EventLogEditedChannelTheme), "un1", tLRPC$User2);
                                                        tLRPC$TL_message2 = new TLRPC$TL_message();
                                                        tLRPC$TL_message2.out = false;
                                                        tLRPC$TL_message2.unread = false;
                                                        TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                                                        tLRPC$TL_message2.from_id = tLRPC$TL_peerUser2;
                                                        tLRPC$TL_peerUser2.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                        tLRPC$TL_message2.peer_id = tLRPC$TL_peerChannel;
                                                        tLRPC$TL_message2.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                        TLRPC$TL_channelAdminLogEventActionChangeTheme tLRPC$TL_channelAdminLogEventActionChangeTheme = (TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$TL_channelAdminLogEvent2.action;
                                                        tLRPC$TL_message2.message = tLRPC$TL_channelAdminLogEventActionChangeTheme.new_value;
                                                        if (TextUtils.isEmpty(tLRPC$TL_channelAdminLogEventActionChangeTheme.prev_value)) {
                                                            tLRPC$TL_messageMediaEmpty = new TLRPC$TL_messageMediaEmpty();
                                                            tLRPC$TL_message2.media = tLRPC$TL_messageMediaEmpty;
                                                            tLRPC$TL_message = tLRPC$TL_message2;
                                                        } else {
                                                            TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage2 = new TLRPC$TL_messageMediaWebPage();
                                                            tLRPC$TL_message2.media = tLRPC$TL_messageMediaWebPage2;
                                                            tLRPC$TL_messageMediaWebPage2.webpage = new TLRPC$TL_webPage();
                                                            TLRPC$WebPage tLRPC$WebPage3 = tLRPC$TL_message2.media.webpage;
                                                            tLRPC$WebPage3.flags = 10;
                                                            tLRPC$WebPage3.display_url = str9;
                                                            tLRPC$WebPage3.url = str9;
                                                            tLRPC$WebPage3.site_name = LocaleController.getString(R.string.EventLogPreviousGroupTheme);
                                                            tLRPC$WebPage = tLRPC$TL_message2.media.webpage;
                                                            str7 = ((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                                            tLRPC$WebPage.description = str7;
                                                            tLRPC$TL_message = tLRPC$TL_message2;
                                                        }
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeUsername) {
                                                        String str13 = ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$ChannelAdminLogEventAction).new_value;
                                                        this.messageText = replaceWithLink(LocaleController.getString(!TextUtils.isEmpty(str13) ? tLRPC$Chat.megagroup ? R.string.EventLogChangedGroupLink : R.string.EventLogChangedChannelLink : tLRPC$Chat.megagroup ? R.string.EventLogRemovedGroupLink : R.string.EventLogRemovedChannelLink), "un1", tLRPC$User2);
                                                        TLRPC$TL_message tLRPC$TL_message5 = new TLRPC$TL_message();
                                                        tLRPC$TL_message5.out = false;
                                                        tLRPC$TL_message5.unread = false;
                                                        TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                                                        tLRPC$TL_message5.from_id = tLRPC$TL_peerUser3;
                                                        tLRPC$TL_peerUser3.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                        tLRPC$TL_message5.peer_id = tLRPC$TL_peerChannel;
                                                        tLRPC$TL_message5.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                        if (TextUtils.isEmpty(str13)) {
                                                            str6 = str2;
                                                            tLRPC$TL_message5.message = str9;
                                                        } else {
                                                            StringBuilder sb5 = new StringBuilder();
                                                            sb5.append("https://");
                                                            sb5.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
                                                            str6 = str2;
                                                            sb5.append(str6);
                                                            sb5.append(str13);
                                                            tLRPC$TL_message5.message = sb5.toString();
                                                        }
                                                        TLRPC$TL_messageEntityUrl tLRPC$TL_messageEntityUrl = new TLRPC$TL_messageEntityUrl();
                                                        tLRPC$TL_messageEntityUrl.offset = 0;
                                                        tLRPC$TL_messageEntityUrl.length = tLRPC$TL_message5.message.length();
                                                        tLRPC$TL_message5.entities.add(tLRPC$TL_messageEntityUrl);
                                                        if (TextUtils.isEmpty(((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$TL_channelAdminLogEvent2.action).prev_value)) {
                                                            tLRPC$TL_message5.media = new TLRPC$TL_messageMediaEmpty();
                                                        } else {
                                                            TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage3 = new TLRPC$TL_messageMediaWebPage();
                                                            tLRPC$TL_message5.media = tLRPC$TL_messageMediaWebPage3;
                                                            tLRPC$TL_messageMediaWebPage3.webpage = new TLRPC$TL_webPage();
                                                            TLRPC$WebPage tLRPC$WebPage4 = tLRPC$TL_message5.media.webpage;
                                                            tLRPC$WebPage4.flags = 10;
                                                            tLRPC$WebPage4.display_url = str9;
                                                            tLRPC$WebPage4.url = str9;
                                                            tLRPC$WebPage4.site_name = LocaleController.getString(R.string.EventLogPreviousLink);
                                                            tLRPC$TL_message5.media.webpage.description = "https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + str6 + ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                                        }
                                                        tLRPC$TL_message = tLRPC$TL_message5;
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionEditMessage) {
                                                        tLRPC$TL_message = new TLRPC$TL_message();
                                                        tLRPC$TL_message.out = false;
                                                        tLRPC$TL_message.unread = false;
                                                        tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                                        tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                        TLRPC$TL_channelAdminLogEventActionEditMessage tLRPC$TL_channelAdminLogEventActionEditMessage = (TLRPC$TL_channelAdminLogEventActionEditMessage) tLRPC$TL_channelAdminLogEvent2.action;
                                                        TLRPC$Message tLRPC$Message4 = tLRPC$TL_channelAdminLogEventActionEditMessage.new_message;
                                                        TLRPC$Message tLRPC$Message5 = tLRPC$TL_channelAdminLogEventActionEditMessage.prev_message;
                                                        if (tLRPC$Message5 == null) {
                                                            if (tLRPC$Message4 != null) {
                                                                tLRPC$TL_message.reply_to = tLRPC$Message4.reply_to;
                                                                i5 = tLRPC$Message4.id;
                                                            }
                                                            if (tLRPC$Message4 != null || (tLRPC$Peer = tLRPC$Message4.from_id) == null) {
                                                                TLRPC$TL_peerUser tLRPC$TL_peerUser4 = new TLRPC$TL_peerUser();
                                                                tLRPC$TL_message.from_id = tLRPC$TL_peerUser4;
                                                                tLRPC$TL_peerUser4.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                            } else {
                                                                tLRPC$TL_message.from_id = tLRPC$Peer;
                                                            }
                                                            if (getMedia(tLRPC$Message4) != null || (getMedia(tLRPC$Message4) instanceof TLRPC$TL_messageMediaEmpty) || (getMedia(tLRPC$Message4) instanceof TLRPC$TL_messageMediaWebPage)) {
                                                                this.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedMessages), "un1", tLRPC$User2);
                                                                if (tLRPC$Message4.action instanceof TLRPC$TL_messageActionGroupCall) {
                                                                    tLRPC$TL_message.message = tLRPC$Message4.message;
                                                                    tLRPC$TL_message.entities = tLRPC$Message4.entities;
                                                                    TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage4 = new TLRPC$TL_messageMediaWebPage();
                                                                    tLRPC$TL_message.media = tLRPC$TL_messageMediaWebPage4;
                                                                    tLRPC$TL_messageMediaWebPage4.webpage = new TLRPC$TL_webPage();
                                                                    tLRPC$TL_message.media.webpage.site_name = LocaleController.getString(R.string.EventLogOriginalMessages);
                                                                } else {
                                                                    tLRPC$Message4.media = new TLRPC$TL_messageMediaEmpty();
                                                                    tLRPC$TL_message = tLRPC$Message4;
                                                                    arrayList2 = null;
                                                                }
                                                            } else {
                                                                boolean z10 = !TextUtils.equals(tLRPC$Message4.message, tLRPC$Message5.message);
                                                                this.messageText = replaceWithLink(LocaleController.getString((!(getMedia(tLRPC$Message4).getClass() == tLRPC$Message5.media.getClass() && ((getMedia(tLRPC$Message4).photo == null || tLRPC$Message5.media.photo == null || getMedia(tLRPC$Message4).photo.id == tLRPC$Message5.media.photo.id) && (getMedia(tLRPC$Message4).document == null || tLRPC$Message5.media.document == null || getMedia(tLRPC$Message4).document.id == tLRPC$Message5.media.document.id))) && z10) ? R.string.EventLogEditedMediaCaption : z10 ? R.string.EventLogEditedCaption : R.string.EventLogEditedMedia), "un1", tLRPC$User2);
                                                                TLRPC$MessageMedia media = getMedia(tLRPC$Message4);
                                                                tLRPC$TL_message.media = media;
                                                                if (z10) {
                                                                    media.webpage = new TLRPC$TL_webPage();
                                                                    tLRPC$TL_message.media.webpage.site_name = LocaleController.getString(R.string.EventLogOriginalCaption);
                                                                }
                                                                arrayList2 = null;
                                                            }
                                                            tLRPC$TL_message.reply_markup = tLRPC$Message4.reply_markup;
                                                            TLRPC$WebPage tLRPC$WebPage5 = tLRPC$TL_message.media.webpage;
                                                            if (tLRPC$WebPage5 != null) {
                                                                tLRPC$WebPage5.flags = 10;
                                                                tLRPC$WebPage5.display_url = str9;
                                                                tLRPC$WebPage5.url = str9;
                                                            }
                                                            arrayList3 = arrayList2;
                                                            tLRPC$Chat2 = tLRPC$Chat;
                                                            if (this.messageOwner == null) {
                                                                this.messageOwner = new TLRPC$TL_messageService();
                                                            }
                                                            this.messageOwner.message = this.messageText.toString();
                                                            this.messageOwner.from_id = new TLRPC$TL_peerUser();
                                                            TLRPC$Message tLRPC$Message6 = this.messageOwner;
                                                            tLRPC$Message6.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                            tLRPC$Message6.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                            int i23 = iArr[0];
                                                            iArr[0] = i23 + 1;
                                                            tLRPC$Message6.id = i23;
                                                            this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                                            tLRPC$Message6.out = false;
                                                            tLRPC$Message6.peer_id = new TLRPC$TL_peerChannel();
                                                            TLRPC$Message tLRPC$Message7 = this.messageOwner;
                                                            tLRPC$Message7.peer_id.channel_id = tLRPC$Chat2.id;
                                                            tLRPC$Message7.unread = false;
                                                            mediaController = MediaController.getInstance();
                                                            this.isOutOwnerCached = null;
                                                            tLRPC$TL_message = tLRPC$TL_message instanceof TLRPC$TL_messageEmpty ? null : tLRPC$TL_message;
                                                            if (tLRPC$TL_message != null) {
                                                                tLRPC$TL_message.out = false;
                                                                tLRPC$TL_message.realId = tLRPC$TL_message.id;
                                                                int i24 = iArr[0];
                                                                iArr[0] = i24 + 1;
                                                                tLRPC$TL_message.id = i24;
                                                                tLRPC$TL_message.flags &= -32769;
                                                                tLRPC$TL_message.dialog_id = -tLRPC$Chat2.id;
                                                                if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                                                    i11 = tLRPC$TL_message.date;
                                                                    tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                                } else {
                                                                    i11 = 0;
                                                                }
                                                                MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$TL_message, (AbstractMap<Long, TLRPC$User>) null, (AbstractMap<Long, TLRPC$Chat>) null, true, true, this.eventId);
                                                                messageObject.realDate = i11;
                                                                messageObject.currentEvent = tLRPC$TL_channelAdminLogEvent2;
                                                                if (messageObject.contentType >= 0) {
                                                                    if (mediaController.isPlayingMessage(messageObject)) {
                                                                        MessageObject playingMessageObject = mediaController.getPlayingMessageObject();
                                                                        messageObject.audioProgress = playingMessageObject.audioProgress;
                                                                        messageObject.audioProgressSec = playingMessageObject.audioProgressSec;
                                                                    }
                                                                    z6 = false;
                                                                    z6 = false;
                                                                    z7 = true;
                                                                    z7 = true;
                                                                    createDateArray(this.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                                                    arrayList4 = arrayList;
                                                                    if (z) {
                                                                        arrayList4.add(0, messageObject);
                                                                    } else {
                                                                        arrayList4.add(arrayList.size() - 1, messageObject);
                                                                    }
                                                                } else {
                                                                    arrayList4 = arrayList;
                                                                    z6 = false;
                                                                    z7 = true;
                                                                    this.contentType = -1;
                                                                }
                                                                if (arrayList3 != null) {
                                                                    messageObject.webPageDescriptionEntities = arrayList3;
                                                                    iArr2 = null;
                                                                    messageObject.linkDescription = null;
                                                                    messageObject.generateLinkDescription();
                                                                    r12 = z6;
                                                                    i10 = z7;
                                                                } else {
                                                                    iArr2 = null;
                                                                    r12 = z6;
                                                                    i10 = z7;
                                                                }
                                                            } else {
                                                                arrayList4 = arrayList;
                                                                iArr2 = null;
                                                                r12 = 0;
                                                                i10 = 1;
                                                            }
                                                            if (!(tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) && this.contentType >= 0) {
                                                                createDateArray(this.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                                                if (z) {
                                                                    arrayList4.add(arrayList.size() - i10, this);
                                                                } else {
                                                                    arrayList4.add(r12, this);
                                                                }
                                                                if (this.messageText == null) {
                                                                    this.messageText = str9;
                                                                }
                                                                TextPaint textPaint = !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
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
                                                                generateLayout(tLRPC$User2);
                                                                this.layoutCreated = i10;
                                                                generateThumbs(r12);
                                                                checkMediaExistance();
                                                                return;
                                                            }
                                                            return;
                                                        }
                                                        tLRPC$TL_message.reply_to = tLRPC$Message5.reply_to;
                                                        i5 = tLRPC$Message5.id;
                                                        tLRPC$TL_message.id = i5;
                                                        if (tLRPC$Message4 != null) {
                                                        }
                                                        TLRPC$TL_peerUser tLRPC$TL_peerUser42 = new TLRPC$TL_peerUser();
                                                        tLRPC$TL_message.from_id = tLRPC$TL_peerUser42;
                                                        tLRPC$TL_peerUser42.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                        if (getMedia(tLRPC$Message4) != null) {
                                                        }
                                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedMessages), "un1", tLRPC$User2);
                                                        if (tLRPC$Message4.action instanceof TLRPC$TL_messageActionGroupCall) {
                                                        }
                                                    } else {
                                                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeEmojiStickerSet) {
                                                            TLRPC$InputStickerSet tLRPC$InputStickerSet = ((TLRPC$TL_channelAdminLogEventActionChangeEmojiStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                                            i3 = (tLRPC$InputStickerSet == null || (tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) ? R.string.EventLogRemovedEmojiPack : R.string.EventLogChangedEmojiPack;
                                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeStickerSet) {
                                                            TLRPC$InputStickerSet tLRPC$InputStickerSet2 = ((TLRPC$TL_channelAdminLogEventActionChangeStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                                            i3 = (tLRPC$InputStickerSet2 == null || (tLRPC$InputStickerSet2 instanceof TLRPC$TL_inputStickerSetEmpty)) ? R.string.EventLogRemovedStickersSet : R.string.EventLogChangedStickersSet;
                                                        } else {
                                                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLocation) {
                                                                TLRPC$ChannelLocation tLRPC$ChannelLocation = ((TLRPC$TL_channelAdminLogEventActionChangeLocation) tLRPC$ChannelAdminLogEventAction).new_value;
                                                                if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocationEmpty) {
                                                                    i3 = R.string.EventLogRemovedLocation;
                                                                } else {
                                                                    string = LocaleController.formatString("EventLogChangedLocation", R.string.EventLogChangedLocation, ((TLRPC$TL_channelLocation) tLRPC$ChannelLocation).address);
                                                                    str5 = "un1";
                                                                    tLRPC$Message = null;
                                                                }
                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSlowMode) {
                                                                int i25 = ((TLRPC$TL_channelAdminLogEventActionToggleSlowMode) tLRPC$ChannelAdminLogEventAction).new_value;
                                                                if (i25 == 0) {
                                                                    i3 = R.string.EventLogToggledSlowmodeOff;
                                                                } else {
                                                                    string = LocaleController.formatString("EventLogToggledSlowmodeOn", R.string.EventLogToggledSlowmodeOn, i25 < 60 ? LocaleController.formatPluralString("un1", i25, new Object[0]) : i25 < 3600 ? LocaleController.formatPluralString("Minutes", i25 / 60, new Object[0]) : LocaleController.formatPluralString("Hours", (i25 / 60) / 60, new Object[0]));
                                                                    str5 = "un1";
                                                                    tLRPC$Message = null;
                                                                }
                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStartGroupCall) {
                                                                i3 = (!ChatObject.isChannel(tLRPC$Chat) || (tLRPC$Chat.megagroup && !tLRPC$Chat.gigagroup)) ? R.string.EventLogStartedVoiceChat : R.string.EventLogStartedLiveStream;
                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDiscardGroupCall) {
                                                                i3 = (!ChatObject.isChannel(tLRPC$Chat) || (tLRPC$Chat.megagroup && !tLRPC$Chat.gigagroup)) ? R.string.EventLogEndedVoiceChat : R.string.EventLogEndedLiveStream;
                                                            } else {
                                                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantMute) {
                                                                    long peerId3 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantMute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                                                    user2 = peerId3 > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId3)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId3));
                                                                    i4 = R.string.EventLogVoiceChatMuted;
                                                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantUnmute) {
                                                                    long peerId4 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantUnmute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                                                    user2 = peerId4 > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId4)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId4));
                                                                    i4 = R.string.EventLogVoiceChatUnmuted;
                                                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) {
                                                                    i3 = ((TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) tLRPC$ChannelAdminLogEventAction).join_muted ? R.string.EventLogVoiceChatNotAllowedToSpeak : R.string.EventLogVoiceChatAllowedToSpeak;
                                                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) {
                                                                    TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite = (TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) tLRPC$ChannelAdminLogEventAction;
                                                                    this.messageText = replaceWithLink(LocaleController.getString(tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite.via_chatlist ? ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat) ? R.string.ActionInviteChannelUserFolder : R.string.ActionInviteUserFolder : ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat) ? R.string.ActionInviteChannelUser : R.string.ActionInviteUser), "un1", tLRPC$User2);
                                                                    TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite.invite;
                                                                    if (tLRPC$TL_chatInviteExported != null && !TextUtils.isEmpty(tLRPC$TL_chatInviteExported.link)) {
                                                                        charSequence = TextUtils.concat(this.messageText, " ", tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite.invite.link);
                                                                        tLRPC$Chat2 = tLRPC$Chat;
                                                                    }
                                                                    tLRPC$Chat2 = tLRPC$Chat;
                                                                    tLRPC$TL_message = null;
                                                                    arrayList3 = null;
                                                                    if (this.messageOwner == null) {
                                                                    }
                                                                    this.messageOwner.message = this.messageText.toString();
                                                                    this.messageOwner.from_id = new TLRPC$TL_peerUser();
                                                                    TLRPC$Message tLRPC$Message62 = this.messageOwner;
                                                                    tLRPC$Message62.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                                    tLRPC$Message62.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                                    int i232 = iArr[0];
                                                                    iArr[0] = i232 + 1;
                                                                    tLRPC$Message62.id = i232;
                                                                    this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                                                    tLRPC$Message62.out = false;
                                                                    tLRPC$Message62.peer_id = new TLRPC$TL_peerChannel();
                                                                    TLRPC$Message tLRPC$Message72 = this.messageOwner;
                                                                    tLRPC$Message72.peer_id.channel_id = tLRPC$Chat2.id;
                                                                    tLRPC$Message72.unread = false;
                                                                    mediaController = MediaController.getInstance();
                                                                    this.isOutOwnerCached = null;
                                                                    if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                                                    }
                                                                    if (tLRPC$TL_message != null) {
                                                                    }
                                                                    if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                                                        return;
                                                                    }
                                                                    createDateArray(this.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                                                    if (z) {
                                                                    }
                                                                    if (this.messageText == null) {
                                                                    }
                                                                    if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame)) {
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
                                                                    generateLayout(tLRPC$User2);
                                                                    this.layoutCreated = i10;
                                                                    generateThumbs(r12);
                                                                    checkMediaExistance();
                                                                    return;
                                                                } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleNoForwards) {
                                                                    TLRPC$TL_channelAdminLogEventActionToggleNoForwards tLRPC$TL_channelAdminLogEventActionToggleNoForwards = (TLRPC$TL_channelAdminLogEventActionToggleNoForwards) tLRPC$ChannelAdminLogEventAction;
                                                                    boolean z11 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup;
                                                                    i3 = tLRPC$TL_channelAdminLogEventActionToggleNoForwards.new_value ? z11 ? R.string.ActionForwardsRestrictedChannel : R.string.ActionForwardsRestrictedGroup : z11 ? R.string.ActionForwardsEnabledChannel : R.string.ActionForwardsEnabledGroup;
                                                                } else {
                                                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteDelete) {
                                                                        replaceWithLink = replaceWithLink(LocaleController.formatString("ActionDeletedInviteLinkClickable", R.string.ActionDeletedInviteLinkClickable, new Object[0]), "un1", tLRPC$User2);
                                                                        this.messageText = replaceWithLink;
                                                                        TLObject tLObject3 = ((TLRPC$TL_channelAdminLogEventActionExportedInviteDelete) tLRPC$ChannelAdminLogEventAction).invite;
                                                                        str3 = tLObject3;
                                                                        tLObject2 = tLObject3;
                                                                    } else {
                                                                        str3 = "%1$s";
                                                                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke) {
                                                                            TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke = (TLRPC$TL_channelAdminLogEventActionExportedInviteRevoke) tLRPC$ChannelAdminLogEventAction;
                                                                            replaceWithLink = replaceWithLink(LocaleController.formatString("ActionRevokedInviteLinkClickable", R.string.ActionRevokedInviteLinkClickable, tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke.invite.link), "un1", tLRPC$User2);
                                                                            this.messageText = replaceWithLink;
                                                                            tLObject2 = tLRPC$TL_channelAdminLogEventActionExportedInviteRevoke.invite;
                                                                        } else {
                                                                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionExportedInviteEdit) {
                                                                                TLRPC$TL_channelAdminLogEventActionExportedInviteEdit tLRPC$TL_channelAdminLogEventActionExportedInviteEdit = (TLRPC$TL_channelAdminLogEventActionExportedInviteEdit) tLRPC$ChannelAdminLogEventAction;
                                                                                String str14 = tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite.link;
                                                                                this.messageText = replaceWithLink((str14 == null || !str14.equals(tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite.link)) ? LocaleController.formatString("ActionEditedInviteLinkClickable", R.string.ActionEditedInviteLinkClickable, new Object[0]) : LocaleController.formatString("ActionEditedInviteLinkToSameClickable", R.string.ActionEditedInviteLinkToSameClickable, new Object[0]), "un1", tLRPC$User2);
                                                                                replaceWithLink2 = replaceWithLink(this.messageText, str3, tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite);
                                                                                this.messageText = replaceWithLink2;
                                                                                tLObject = tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite;
                                                                                str4 = str9;
                                                                            } else {
                                                                                str4 = str9;
                                                                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantVolume) {
                                                                                    TLRPC$TL_channelAdminLogEventActionParticipantVolume tLRPC$TL_channelAdminLogEventActionParticipantVolume = (TLRPC$TL_channelAdminLogEventActionParticipantVolume) tLRPC$ChannelAdminLogEventAction;
                                                                                    long peerId5 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant.peer);
                                                                                    user = peerId5 > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId5)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId5));
                                                                                    double participantVolume = ChatObject.getParticipantVolume(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant);
                                                                                    Double.isNaN(participantVolume);
                                                                                    double d = participantVolume / 100.0d;
                                                                                    CharSequence replaceWithLink3 = replaceWithLink(LocaleController.formatString("ActionVolumeChanged", R.string.ActionVolumeChanged, Integer.valueOf((int) (d > 0.0d ? Math.max(d, 1.0d) : 0.0d))), "un1", tLRPC$User2);
                                                                                    this.messageText = replaceWithLink3;
                                                                                    str11 = str3;
                                                                                    charSequence2 = replaceWithLink3;
                                                                                } else {
                                                                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) {
                                                                                        TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL = (TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) tLRPC$ChannelAdminLogEventAction;
                                                                                        if (tLRPC$Chat.megagroup) {
                                                                                            int i26 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                                                            if (i26 == 0) {
                                                                                                i3 = R.string.ActionTTLDisabled;
                                                                                            } else {
                                                                                                if (i26 > 86400) {
                                                                                                    c2 = 0;
                                                                                                    formatPluralString = LocaleController.formatPluralString("Days", i26 / 86400, new Object[0]);
                                                                                                } else {
                                                                                                    c2 = 0;
                                                                                                    formatPluralString = i26 >= 3600 ? LocaleController.formatPluralString("Hours", i26 / 3600, new Object[0]) : i26 >= 60 ? LocaleController.formatPluralString("Minutes", i26 / 60, new Object[0]) : LocaleController.formatPluralString("un1", i26, new Object[0]);
                                                                                                }
                                                                                                int i27 = R.string.ActionTTLChanged;
                                                                                                Object[] objArr = new Object[1];
                                                                                                objArr[c2] = formatPluralString;
                                                                                                string = LocaleController.formatString("ActionTTLChanged", i27, objArr);
                                                                                                str5 = "un1";
                                                                                                tLRPC$Message = null;
                                                                                            }
                                                                                        } else {
                                                                                            int i28 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                                                            charSequence = i28 != 0 ? LocaleController.formatString("ActionTTLChannelChanged", R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i28)) : LocaleController.getString(R.string.ActionTTLChannelDisabled);
                                                                                            tLRPC$Chat2 = tLRPC$Chat;
                                                                                        }
                                                                                    } else {
                                                                                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) {
                                                                                            TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest = (TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) tLRPC$ChannelAdminLogEventAction;
                                                                                            TLRPC$ExportedChatInvite tLRPC$ExportedChatInvite = tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite;
                                                                                            if (((tLRPC$ExportedChatInvite instanceof TLRPC$TL_chatInviteExported) && "https://t.me/+PublicChat".equals(((TLRPC$TL_chatInviteExported) tLRPC$ExportedChatInvite).link)) || (tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite instanceof TLRPC$TL_chatInvitePublicJoinRequests)) {
                                                                                                replaceWithLink = replaceWithLink(LocaleController.getString(R.string.JoinedViaRequestApproved), "un1", tLRPC$User2);
                                                                                                this.messageText = replaceWithLink;
                                                                                                tLObject2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by));
                                                                                            } else {
                                                                                                CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString(R.string.JoinedViaInviteLinkApproved), "un1", tLRPC$User2);
                                                                                                this.messageText = replaceWithLink4;
                                                                                                replaceWithLink2 = replaceWithLink(replaceWithLink4, str3, tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite);
                                                                                                this.messageText = replaceWithLink2;
                                                                                                tLObject = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by));
                                                                                            }
                                                                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) {
                                                                                            TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions = (TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) tLRPC$ChannelAdminLogEventAction;
                                                                                            boolean z12 = (tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.prev_value instanceof TLRPC$TL_chatReactionsSome) && (tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.new_value instanceof TLRPC$TL_chatReactionsSome);
                                                                                            CharSequence stringFrom = getStringFrom(tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.new_value);
                                                                                            if (z12) {
                                                                                                spannableStringBuilder6 = new SpannableStringBuilder(replaceWithLink(LocaleController.formatString("ActionReactionsChangedList", R.string.ActionReactionsChangedList, "**new**"), "un1", tLRPC$User2));
                                                                                                int indexOf = spannableStringBuilder6.toString().indexOf("**new**");
                                                                                                if (indexOf > 0) {
                                                                                                    spannableStringBuilder6.replace(indexOf, indexOf + 7, stringFrom);
                                                                                                }
                                                                                            } else {
                                                                                                CharSequence stringFrom2 = getStringFrom(tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.prev_value);
                                                                                                SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder(replaceWithLink(LocaleController.formatString("ActionReactionsChanged", R.string.ActionReactionsChanged, "**old**", "**new**"), "un1", tLRPC$User2));
                                                                                                int indexOf2 = spannableStringBuilder8.toString().indexOf("**old**");
                                                                                                if (indexOf2 > 0) {
                                                                                                    spannableStringBuilder8.replace(indexOf2, indexOf2 + 7, stringFrom2);
                                                                                                }
                                                                                                int indexOf3 = spannableStringBuilder8.toString().indexOf("**new**");
                                                                                                if (indexOf3 > 0) {
                                                                                                    spannableStringBuilder8.replace(indexOf3, indexOf3 + 7, stringFrom);
                                                                                                }
                                                                                                spannableStringBuilder6 = spannableStringBuilder8;
                                                                                            }
                                                                                            tLRPC$Chat2 = tLRPC$Chat;
                                                                                            tLRPC$Message2 = null;
                                                                                            spannableStringBuilder7 = spannableStringBuilder6;
                                                                                        } else {
                                                                                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeUsernames) {
                                                                                                TLRPC$TL_channelAdminLogEventActionChangeUsernames tLRPC$TL_channelAdminLogEventActionChangeUsernames = (TLRPC$TL_channelAdminLogEventActionChangeUsernames) tLRPC$ChannelAdminLogEventAction;
                                                                                                ArrayList arrayList5 = tLRPC$TL_channelAdminLogEventActionChangeUsernames.prev_value;
                                                                                                ?? r1 = tLRPC$TL_channelAdminLogEventActionChangeUsernames.new_value;
                                                                                                this.messageText = null;
                                                                                                if (arrayList5 != null && r1 != 0) {
                                                                                                    if (r1.size() + 1 == arrayList5.size()) {
                                                                                                        int i29 = 0;
                                                                                                        String str15 = null;
                                                                                                        while (true) {
                                                                                                            if (i29 >= arrayList5.size()) {
                                                                                                                break;
                                                                                                            }
                                                                                                            String str16 = (String) arrayList5.get(i29);
                                                                                                            if (!r1.contains(str16)) {
                                                                                                                if (str15 != null) {
                                                                                                                    str15 = null;
                                                                                                                    break;
                                                                                                                }
                                                                                                                str15 = str16;
                                                                                                            }
                                                                                                            i29++;
                                                                                                        }
                                                                                                        if (str15 != null) {
                                                                                                            formatString = LocaleController.formatString("EventLogDeactivatedUsername", R.string.EventLogDeactivatedUsername, ((String) r1) + str15);
                                                                                                            this.messageText = replaceWithLink(formatString, "un1", tLRPC$User2);
                                                                                                        }
                                                                                                    } else if (arrayList5.size() + 1 == r1.size()) {
                                                                                                        int i30 = 0;
                                                                                                        String str17 = null;
                                                                                                        while (true) {
                                                                                                            if (i30 >= r1.size()) {
                                                                                                                break;
                                                                                                            }
                                                                                                            String str18 = (String) r1.get(i30);
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
                                                                                                            formatString = LocaleController.formatString("EventLogActivatedUsername", R.string.EventLogActivatedUsername, ((String) r1) + str17);
                                                                                                            this.messageText = replaceWithLink(formatString, "un1", tLRPC$User2);
                                                                                                        }
                                                                                                    }
                                                                                                    charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
                                                                                                    tLRPC$Chat2 = tLRPC$Chat;
                                                                                                }
                                                                                                if (this.messageText == null) {
                                                                                                    string = LocaleController.formatString("EventLogChangeUsernames", R.string.EventLogChangeUsernames, getUsernamesString(arrayList5), getUsernamesString(r1));
                                                                                                }
                                                                                                tLRPC$Chat2 = tLRPC$Chat;
                                                                                                tLRPC$TL_message = null;
                                                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleForum) {
                                                                                                string = ((TLRPC$TL_channelAdminLogEventActionToggleForum) tLRPC$ChannelAdminLogEventAction).new_value ? LocaleController.formatString("EventLogSwitchToForum", R.string.EventLogSwitchToForum, new Object[0]) : LocaleController.formatString("EventLogSwitchToGroup", R.string.EventLogSwitchToGroup, new Object[0]);
                                                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionCreateTopic) {
                                                                                                replaceWithLink = replaceWithLink(LocaleController.formatString("EventLogCreateTopic", R.string.EventLogCreateTopic, new Object[0]), "un1", tLRPC$User2);
                                                                                                this.messageText = replaceWithLink;
                                                                                                tLObject2 = ((TLRPC$TL_channelAdminLogEventActionCreateTopic) tLRPC$ChannelAdminLogEventAction).topic;
                                                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionEditTopic) {
                                                                                                TLRPC$TL_channelAdminLogEventActionEditTopic tLRPC$TL_channelAdminLogEventActionEditTopic = (TLRPC$TL_channelAdminLogEventActionEditTopic) tLRPC$ChannelAdminLogEventAction;
                                                                                                TLRPC$ForumTopic tLRPC$ForumTopic = tLRPC$TL_channelAdminLogEventActionEditTopic.prev_topic;
                                                                                                if (tLRPC$ForumTopic instanceof TLRPC$TL_forumTopic) {
                                                                                                    TLRPC$ForumTopic tLRPC$ForumTopic2 = tLRPC$TL_channelAdminLogEventActionEditTopic.new_topic;
                                                                                                    if (tLRPC$ForumTopic2 instanceof TLRPC$TL_forumTopic) {
                                                                                                        boolean z13 = ((TLRPC$TL_forumTopic) tLRPC$ForumTopic).hidden;
                                                                                                        boolean z14 = ((TLRPC$TL_forumTopic) tLRPC$ForumTopic2).hidden;
                                                                                                        if (z13 != z14) {
                                                                                                            charSequence = replaceWithLink(LocaleController.getString(z14 ? R.string.TopicHidden2 : R.string.TopicShown2), "%s", tLRPC$User2);
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString(R.string.EventLogEditTopic), "un1", tLRPC$User2);
                                                                                                this.messageText = replaceWithLink5;
                                                                                                replaceWithLink2 = replaceWithLink(replaceWithLink5, str3, tLRPC$TL_channelAdminLogEventActionEditTopic.prev_topic);
                                                                                                this.messageText = replaceWithLink2;
                                                                                                tLObject = tLRPC$TL_channelAdminLogEventActionEditTopic.new_topic;
                                                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDeleteTopic) {
                                                                                                replaceWithLink = replaceWithLink(LocaleController.getString(R.string.EventLogDeleteTopic), "un1", tLRPC$User2);
                                                                                                this.messageText = replaceWithLink;
                                                                                                tLObject2 = ((TLRPC$TL_channelAdminLogEventActionDeleteTopic) tLRPC$ChannelAdminLogEventAction).topic;
                                                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionPinTopic) {
                                                                                                TLRPC$TL_channelAdminLogEventActionPinTopic tLRPC$TL_channelAdminLogEventActionPinTopic = (TLRPC$TL_channelAdminLogEventActionPinTopic) tLRPC$ChannelAdminLogEventAction;
                                                                                                TLRPC$ForumTopic tLRPC$ForumTopic3 = tLRPC$TL_channelAdminLogEventActionPinTopic.new_topic;
                                                                                                if ((tLRPC$ForumTopic3 instanceof TLRPC$TL_forumTopic) && ((TLRPC$TL_forumTopic) tLRPC$ForumTopic3).pinned) {
                                                                                                    CharSequence replaceWithLink6 = replaceWithLink(LocaleController.formatString("EventLogPinTopic", R.string.EventLogPinTopic, new Object[0]), "un1", tLRPC$User2);
                                                                                                    this.messageText = replaceWithLink6;
                                                                                                    charSequence = replaceWithLink(replaceWithLink6, str3, tLRPC$TL_channelAdminLogEventActionPinTopic.new_topic);
                                                                                                } else {
                                                                                                    replaceWithLink = replaceWithLink(LocaleController.formatString("EventLogUnpinTopic", R.string.EventLogUnpinTopic, new Object[0]), "un1", tLRPC$User2);
                                                                                                    this.messageText = replaceWithLink;
                                                                                                    tLObject2 = tLRPC$TL_channelAdminLogEventActionPinTopic.new_topic;
                                                                                                }
                                                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleAntiSpam) {
                                                                                                charSequence = replaceWithLink(LocaleController.getString(((TLRPC$TL_channelAdminLogEventActionToggleAntiSpam) tLRPC$ChannelAdminLogEventAction).new_value ? R.string.EventLogEnabledAntiSpam : R.string.EventLogDisabledAntiSpam), "un1", tLRPC$User2);
                                                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeColor) {
                                                                                                boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                                                                TLRPC$TL_channelAdminLogEventActionChangeColor tLRPC$TL_channelAdminLogEventActionChangeColor = (TLRPC$TL_channelAdminLogEventActionChangeColor) tLRPC$TL_channelAdminLogEvent2.action;
                                                                                                string = LocaleController.formatString(isChannelAndNotMegaGroup ? R.string.EventLogChangedColor : R.string.EventLogChangedColorGroup, AvatarDrawable.colorName(tLRPC$TL_channelAdminLogEventActionChangeColor.prev_value).toLowerCase(), AvatarDrawable.colorName(tLRPC$TL_channelAdminLogEventActionChangeColor.new_value).toLowerCase());
                                                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangePeerColor) {
                                                                                                boolean isChannelAndNotMegaGroup2 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                                                                TLRPC$TL_channelAdminLogEventActionChangePeerColor tLRPC$TL_channelAdminLogEventActionChangePeerColor = (TLRPC$TL_channelAdminLogEventActionChangePeerColor) tLRPC$TL_channelAdminLogEvent2.action;
                                                                                                SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder(LocaleController.getString(isChannelAndNotMegaGroup2 ? R.string.EventLogChangedPeerColorIcon : R.string.EventLogChangedPeerColorIconGroup));
                                                                                                spannableStringBuilder = new SpannableStringBuilder();
                                                                                                if ((tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.flags & 1) != 0) {
                                                                                                    spannableStringBuilder.append((CharSequence) "c");
                                                                                                    spannableStringBuilder5 = spannableStringBuilder9;
                                                                                                    spannableStringBuilder.setSpan(new PeerColorActivity.PeerColorSpan(false, this.currentAccount, tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
                                                                                                } else {
                                                                                                    spannableStringBuilder5 = spannableStringBuilder9;
                                                                                                }
                                                                                                if ((tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.flags & 2) != 0) {
                                                                                                    if (spannableStringBuilder.length() > 0) {
                                                                                                        spannableStringBuilder.append((CharSequence) ", ");
                                                                                                    }
                                                                                                    spannableStringBuilder.append((CharSequence) "e");
                                                                                                    spannableStringBuilder.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
                                                                                                }
                                                                                                if (spannableStringBuilder.length() == 0) {
                                                                                                    spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                }
                                                                                                spannableStringBuilder3 = new SpannableStringBuilder();
                                                                                                if ((tLRPC$TL_channelAdminLogEventActionChangePeerColor.new_value.flags & 1) != 0) {
                                                                                                    spannableStringBuilder3.append((CharSequence) "c");
                                                                                                    spannableStringBuilder3.setSpan(new PeerColorActivity.PeerColorSpan(false, this.currentAccount, tLRPC$TL_channelAdminLogEventActionChangePeerColor.new_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder3.length() - 1, spannableStringBuilder3.length(), 33);
                                                                                                }
                                                                                                if ((tLRPC$TL_channelAdminLogEventActionChangePeerColor.new_value.flags & 2) != 0) {
                                                                                                    if (spannableStringBuilder3.length() > 0) {
                                                                                                        spannableStringBuilder3.append((CharSequence) ", ");
                                                                                                    }
                                                                                                    spannableStringBuilder3.append((CharSequence) "e");
                                                                                                    spannableStringBuilder3.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangePeerColor.new_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder3.length() - 1, spannableStringBuilder3.length(), 33);
                                                                                                }
                                                                                                spannableStringBuilder4 = spannableStringBuilder5;
                                                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor) {
                                                                                                boolean isChannelAndNotMegaGroup3 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                                                                TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor = (TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor) tLRPC$TL_channelAdminLogEvent2.action;
                                                                                                SpannableStringBuilder spannableStringBuilder10 = new SpannableStringBuilder(LocaleController.getString(isChannelAndNotMegaGroup3 ? R.string.EventLogChangedProfileColorIcon : R.string.EventLogChangedProfileColorIconGroup));
                                                                                                spannableStringBuilder = new SpannableStringBuilder();
                                                                                                if ((tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.flags & 1) != 0) {
                                                                                                    spannableStringBuilder.append((CharSequence) "c");
                                                                                                    spannableStringBuilder2 = spannableStringBuilder10;
                                                                                                    spannableStringBuilder.setSpan(new PeerColorActivity.PeerColorSpan(true, this.currentAccount, tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
                                                                                                } else {
                                                                                                    spannableStringBuilder2 = spannableStringBuilder10;
                                                                                                }
                                                                                                if ((tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.flags & 2) != 0) {
                                                                                                    if (spannableStringBuilder.length() > 0) {
                                                                                                        spannableStringBuilder.append((CharSequence) ", ");
                                                                                                    }
                                                                                                    spannableStringBuilder.append((CharSequence) "e");
                                                                                                    spannableStringBuilder.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.prev_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
                                                                                                }
                                                                                                if (spannableStringBuilder.length() == 0) {
                                                                                                    spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                }
                                                                                                SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder();
                                                                                                if ((tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.new_value.flags & 1) != 0) {
                                                                                                    spannableStringBuilder11.append((CharSequence) "c");
                                                                                                    spannableStringBuilder11.setSpan(new PeerColorActivity.PeerColorSpan(true, this.currentAccount, tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.new_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder11.length() - 1, spannableStringBuilder11.length(), 33);
                                                                                                }
                                                                                                if ((tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.new_value.flags & 2) != 0) {
                                                                                                    if (spannableStringBuilder11.length() > 0) {
                                                                                                        spannableStringBuilder11.append((CharSequence) ", ");
                                                                                                    }
                                                                                                    spannableStringBuilder11.append((CharSequence) "e");
                                                                                                    spannableStringBuilder11.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor.new_value.background_emoji_id, Theme.chat_actionTextPaint.getFontMetricsInt()), spannableStringBuilder11.length() - 1, spannableStringBuilder11.length(), 33);
                                                                                                }
                                                                                                if (spannableStringBuilder11.length() == 0) {
                                                                                                    spannableStringBuilder3 = spannableStringBuilder11;
                                                                                                    spannableStringBuilder4 = spannableStringBuilder2;
                                                                                                    spannableStringBuilder3.append((CharSequence) LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                    string = AndroidUtilities.replaceCharSequence(str10, AndroidUtilities.replaceCharSequence("%1$s", spannableStringBuilder4, spannableStringBuilder), spannableStringBuilder3);
                                                                                                } else {
                                                                                                    spannableStringBuilder3 = spannableStringBuilder11;
                                                                                                    spannableStringBuilder4 = spannableStringBuilder2;
                                                                                                    string = AndroidUtilities.replaceCharSequence(str10, AndroidUtilities.replaceCharSequence("%1$s", spannableStringBuilder4, spannableStringBuilder), spannableStringBuilder3);
                                                                                                }
                                                                                            } else {
                                                                                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeEmojiStatus) {
                                                                                                    boolean isChannelAndNotMegaGroup4 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                                                                    TLRPC$TL_channelAdminLogEventActionChangeEmojiStatus tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus = (TLRPC$TL_channelAdminLogEventActionChangeEmojiStatus) tLRPC$TL_channelAdminLogEvent2.action;
                                                                                                    if (tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.prev_value instanceof TLRPC$TL_emojiStatusEmpty) {
                                                                                                        spannableString3 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                        z3 = true;
                                                                                                    } else {
                                                                                                        spannableString3 = new SpannableString("e");
                                                                                                        spannableString3.setSpan(new AnimatedEmojiSpan(DialogObject.getEmojiStatusDocumentId(tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.prev_value), Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                                                                        z3 = false;
                                                                                                    }
                                                                                                    TLRPC$EmojiStatus tLRPC$EmojiStatus = tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.new_value;
                                                                                                    boolean z15 = tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil;
                                                                                                    if (tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusEmpty) {
                                                                                                        spannableString4 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                        str5 = "un1";
                                                                                                    } else {
                                                                                                        SpannableString spannableString5 = new SpannableString("e");
                                                                                                        str5 = "un1";
                                                                                                        spannableString5.setSpan(new AnimatedEmojiSpan(DialogObject.getEmojiStatusDocumentId(tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.new_value), Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                                                                        spannableString4 = spannableString5;
                                                                                                    }
                                                                                                    SpannableStringBuilder replaceCharSequence = AndroidUtilities.replaceCharSequence(str10, AndroidUtilities.replaceCharSequence("%1$s", new SpannableStringBuilder(LocaleController.getString(z3 ? z15 ? isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFor : R.string.EventLogChangedEmojiStatusForGroup : isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatus : R.string.EventLogChangedEmojiStatusGroup : z15 ? isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFromFor : R.string.EventLogChangedEmojiStatusFromForGroup : isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFrom : R.string.EventLogChangedEmojiStatusFromGroup)), spannableString3), spannableString4);
                                                                                                    string = z15 ? AndroidUtilities.replaceCharSequence("%3$s", replaceCharSequence, LocaleController.formatTTLString((int) ((DialogObject.getEmojiStatusUntil(tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.new_value) - tLRPC$TL_channelAdminLogEvent2.date) * 1.05f))) : replaceCharSequence;
                                                                                                } else {
                                                                                                    str5 = "un1";
                                                                                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeWallpaper) {
                                                                                                        TLRPC$TL_channelAdminLogEventActionChangeWallpaper tLRPC$TL_channelAdminLogEventActionChangeWallpaper = (TLRPC$TL_channelAdminLogEventActionChangeWallpaper) tLRPC$ChannelAdminLogEventAction;
                                                                                                        boolean isChannelAndNotMegaGroup5 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                                                                        TLRPC$WallPaper tLRPC$WallPaper = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value;
                                                                                                        if ((tLRPC$WallPaper instanceof TLRPC$TL_wallPaperNoFile) && tLRPC$WallPaper.id == 0 && tLRPC$WallPaper.settings == null) {
                                                                                                            i2 = isChannelAndNotMegaGroup5 ? R.string.EventLogRemovedWallpaper : R.string.EventLogRemovedWallpaperGroup;
                                                                                                        } else {
                                                                                                            ArrayList<TLRPC$PhotoSize> arrayList6 = new ArrayList<>();
                                                                                                            this.photoThumbs = arrayList6;
                                                                                                            TLRPC$Document tLRPC$Document = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                                                                                                            if (tLRPC$Document != null) {
                                                                                                                arrayList6.addAll(tLRPC$Document.thumbs);
                                                                                                                this.photoThumbsObject = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                                                                                                            }
                                                                                                            i2 = isChannelAndNotMegaGroup5 ? R.string.EventLogChangedWallpaper : R.string.EventLogChangedWallpaperGroup;
                                                                                                        }
                                                                                                        string = LocaleController.getString(i2);
                                                                                                    } else {
                                                                                                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji) {
                                                                                                            boolean isChannelAndNotMegaGroup6 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                                                                            TLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji = (TLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji) tLRPC$TL_channelAdminLogEvent2.action;
                                                                                                            this.messageText = replaceWithLink(LocaleController.getString(isChannelAndNotMegaGroup6 ? R.string.EventLogChangedEmoji : R.string.EventLogChangedEmojiGroup), str5, tLRPC$User2);
                                                                                                            if (tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji.prev_value == 0) {
                                                                                                                spannableString = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                            } else {
                                                                                                                spannableString = new SpannableString("e");
                                                                                                                spannableString.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji.prev_value, Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                                                                            }
                                                                                                            this.messageText = AndroidUtilities.replaceCharSequence("%1$s", this.messageText, spannableString);
                                                                                                            if (tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji.new_value == 0) {
                                                                                                                spannableString2 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                                                                            } else {
                                                                                                                spannableString2 = new SpannableString("e");
                                                                                                                spannableString2.setSpan(new AnimatedEmojiSpan(tLRPC$TL_channelAdminLogEventActionChangeBackgroundEmoji.new_value, Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                                                                            }
                                                                                                            charSequence = AndroidUtilities.replaceCharSequence(str10, this.messageText, spannableString2);
                                                                                                        } else {
                                                                                                            charSequence = "unsupported " + tLRPC$TL_channelAdminLogEvent2.action;
                                                                                                        }
                                                                                                        tLRPC$Chat2 = tLRPC$Chat;
                                                                                                    }
                                                                                                }
                                                                                                tLRPC$Message = null;
                                                                                            }
                                                                                            str5 = "un1";
                                                                                            tLRPC$Message = null;
                                                                                        }
                                                                                        tLRPC$Chat2 = tLRPC$Chat;
                                                                                    }
                                                                                    arrayList3 = null;
                                                                                    if (this.messageOwner == null) {
                                                                                    }
                                                                                    this.messageOwner.message = this.messageText.toString();
                                                                                    this.messageOwner.from_id = new TLRPC$TL_peerUser();
                                                                                    TLRPC$Message tLRPC$Message622 = this.messageOwner;
                                                                                    tLRPC$Message622.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                                                    tLRPC$Message622.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                                                    int i2322 = iArr[0];
                                                                                    iArr[0] = i2322 + 1;
                                                                                    tLRPC$Message622.id = i2322;
                                                                                    this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                                                                    tLRPC$Message622.out = false;
                                                                                    tLRPC$Message622.peer_id = new TLRPC$TL_peerChannel();
                                                                                    TLRPC$Message tLRPC$Message722 = this.messageOwner;
                                                                                    tLRPC$Message722.peer_id.channel_id = tLRPC$Chat2.id;
                                                                                    tLRPC$Message722.unread = false;
                                                                                    mediaController = MediaController.getInstance();
                                                                                    this.isOutOwnerCached = null;
                                                                                    if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                                                                    }
                                                                                    if (tLRPC$TL_message != null) {
                                                                                    }
                                                                                    if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                                                                    }
                                                                                }
                                                                            }
                                                                            charSequence = replaceWithLink(replaceWithLink2, str4, tLObject);
                                                                            tLRPC$Chat2 = tLRPC$Chat;
                                                                        }
                                                                    }
                                                                    charSequence = replaceWithLink(replaceWithLink, str3, tLObject2);
                                                                    tLRPC$Chat2 = tLRPC$Chat;
                                                                }
                                                                charSequence2 = replaceWithLink(LocaleController.getString(i4), "un1", tLRPC$User2);
                                                                this.messageText = charSequence2;
                                                                str11 = user2;
                                                                user = user2;
                                                            }
                                                            charSequence2 = string;
                                                            tLRPC$Chat3 = tLRPC$User2;
                                                            str11 = str5;
                                                            charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
                                                            tLRPC$Chat2 = tLRPC$Chat;
                                                        }
                                                        tLRPC$Message = null;
                                                        string = LocaleController.getString(i3);
                                                        str5 = "un1";
                                                        charSequence2 = string;
                                                        tLRPC$Chat3 = tLRPC$User2;
                                                        str11 = str5;
                                                        charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
                                                        tLRPC$Chat2 = tLRPC$Chat;
                                                    }
                                                }
                                                tLRPC$Chat2 = tLRPC$Chat;
                                                arrayList3 = null;
                                                if (this.messageOwner == null) {
                                                }
                                                this.messageOwner.message = this.messageText.toString();
                                                this.messageOwner.from_id = new TLRPC$TL_peerUser();
                                                TLRPC$Message tLRPC$Message6222 = this.messageOwner;
                                                tLRPC$Message6222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                tLRPC$Message6222.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                int i23222 = iArr[0];
                                                iArr[0] = i23222 + 1;
                                                tLRPC$Message6222.id = i23222;
                                                this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                                tLRPC$Message6222.out = false;
                                                tLRPC$Message6222.peer_id = new TLRPC$TL_peerChannel();
                                                TLRPC$Message tLRPC$Message7222 = this.messageOwner;
                                                tLRPC$Message7222.peer_id.channel_id = tLRPC$Chat2.id;
                                                tLRPC$Message7222.unread = false;
                                                mediaController = MediaController.getInstance();
                                                this.isOutOwnerCached = null;
                                                if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                                }
                                                if (tLRPC$TL_message != null) {
                                                }
                                                if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                                }
                                            }
                                            tLRPC$Message = null;
                                            tLRPC$Chat3 = user;
                                            charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
                                            tLRPC$Chat2 = tLRPC$Chat;
                                        }
                                        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                        str9 = "";
                                        tLRPC$Message = null;
                                        string = LocaleController.getString(i3);
                                        str5 = "un1";
                                        charSequence2 = string;
                                        tLRPC$Chat3 = tLRPC$User2;
                                        str11 = str5;
                                        charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
                                        tLRPC$Chat2 = tLRPC$Chat;
                                    }
                                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                    str9 = "";
                                    string = LocaleController.getString(i3);
                                    str5 = "un1";
                                    charSequence2 = string;
                                    tLRPC$Chat3 = tLRPC$User2;
                                    str11 = str5;
                                    charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
                                    tLRPC$Chat2 = tLRPC$Chat;
                                }
                                this.messageText = spannableStringBuilder7;
                                tLRPC$TL_message = tLRPC$Message2;
                                arrayList3 = null;
                                if (this.messageOwner == null) {
                                }
                                this.messageOwner.message = this.messageText.toString();
                                this.messageOwner.from_id = new TLRPC$TL_peerUser();
                                TLRPC$Message tLRPC$Message62222 = this.messageOwner;
                                tLRPC$Message62222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                tLRPC$Message62222.date = tLRPC$TL_channelAdminLogEvent2.date;
                                int i232222 = iArr[0];
                                iArr[0] = i232222 + 1;
                                tLRPC$Message62222.id = i232222;
                                this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                tLRPC$Message62222.out = false;
                                tLRPC$Message62222.peer_id = new TLRPC$TL_peerChannel();
                                TLRPC$Message tLRPC$Message72222 = this.messageOwner;
                                tLRPC$Message72222.peer_id.channel_id = tLRPC$Chat2.id;
                                tLRPC$Message72222.unread = false;
                                mediaController = MediaController.getInstance();
                                this.isOutOwnerCached = null;
                                if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                }
                                if (tLRPC$TL_message != null) {
                                }
                                if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                }
                            }
                            tLRPC$Message = null;
                        }
                        CharSequence charSequence3 = charSequence;
                        tLRPC$Message2 = tLRPC$Message;
                        spannableStringBuilder7 = charSequence3;
                        this.messageText = spannableStringBuilder7;
                        tLRPC$TL_message = tLRPC$Message2;
                        arrayList3 = null;
                        if (this.messageOwner == null) {
                        }
                        this.messageOwner.message = this.messageText.toString();
                        this.messageOwner.from_id = new TLRPC$TL_peerUser();
                        TLRPC$Message tLRPC$Message622222 = this.messageOwner;
                        tLRPC$Message622222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                        tLRPC$Message622222.date = tLRPC$TL_channelAdminLogEvent2.date;
                        int i2322222 = iArr[0];
                        iArr[0] = i2322222 + 1;
                        tLRPC$Message622222.id = i2322222;
                        this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                        tLRPC$Message622222.out = false;
                        tLRPC$Message622222.peer_id = new TLRPC$TL_peerChannel();
                        TLRPC$Message tLRPC$Message722222 = this.messageOwner;
                        tLRPC$Message722222.peer_id.channel_id = tLRPC$Chat2.id;
                        tLRPC$Message722222.unread = false;
                        mediaController = MediaController.getInstance();
                        this.isOutOwnerCached = null;
                        if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                        }
                        if (tLRPC$TL_message != null) {
                        }
                        if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                        }
                    }
                    TLRPC$User tLRPC$User6 = tLRPC$User;
                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                    tLRPC$User2 = tLRPC$User6;
                    if (z2) {
                        TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin = (TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin) tLRPC$ChannelAdminLogEventAction;
                        tLRPC$ChannelParticipant = tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin.prev_participant;
                        tLRPC$ChannelParticipant2 = tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin.new_participant;
                    } else {
                        TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan3 = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                        tLRPC$ChannelParticipant = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan3.prev_participant;
                        tLRPC$ChannelParticipant2 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan3.new_participant;
                    }
                    TLRPC$TL_message tLRPC$TL_message6 = new TLRPC$TL_message();
                    this.messageOwner = tLRPC$TL_message6;
                    tLRPC$TL_message6.realId = -1;
                    long peerId6 = getPeerId(tLRPC$ChannelParticipant.peer);
                    if (peerId6 > 0) {
                        messagesController = MessagesController.getInstance(this.currentAccount);
                    } else {
                        messagesController = MessagesController.getInstance(this.currentAccount);
                        peerId6 = -peerId6;
                    }
                    TLRPC$User user5 = messagesController.getUser(Long.valueOf(peerId6));
                    if ((tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator) || !(tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator)) {
                        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$ChannelParticipant.admin_rights;
                        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2 = tLRPC$ChannelParticipant2.admin_rights;
                        tLRPC$TL_chatAdminRights = tLRPC$TL_chatAdminRights == null ? new TLRPC$TL_chatAdminRights() : tLRPC$TL_chatAdminRights;
                        tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights2 == null ? new TLRPC$TL_chatAdminRights() : tLRPC$TL_chatAdminRights2;
                        String string4 = LocaleController.getString(tLRPC$TL_chatAdminRights2.other ? R.string.EventLogPromotedNoRights : R.string.EventLogPromoted);
                        sb = new StringBuilder(String.format(string4, getUserName(user5, this.messageOwner.entities, string4.indexOf("%1$s"))));
                        sb.append("\n");
                        if (TextUtils.equals(tLRPC$ChannelParticipant.rank, tLRPC$ChannelParticipant2.rank)) {
                            c = '+';
                        } else if (TextUtils.isEmpty(tLRPC$ChannelParticipant2.rank)) {
                            sb.append('\n');
                            sb.append('-');
                            sb.append(' ');
                            sb.append(LocaleController.getString(R.string.EventLogPromotedRemovedTitle));
                            c = '+';
                        } else {
                            sb.append('\n');
                            c = '+';
                            sb.append('+');
                            sb.append(' ');
                            sb.append(LocaleController.formatString("EventLogPromotedTitle", R.string.EventLogPromotedTitle, tLRPC$ChannelParticipant2.rank));
                        }
                        if (tLRPC$TL_chatAdminRights.change_info != tLRPC$TL_chatAdminRights2.change_info) {
                            sb.append('\n');
                            sb.append(tLRPC$TL_chatAdminRights2.change_info ? '+' : '-');
                            sb.append(' ');
                            tLRPC$Chat2 = tLRPC$Chat;
                            sb.append(LocaleController.getString(tLRPC$Chat2.megagroup ? R.string.EventLogPromotedChangeGroupInfo : R.string.EventLogPromotedChangeChannelInfo));
                        } else {
                            tLRPC$Chat2 = tLRPC$Chat;
                        }
                        if (!tLRPC$Chat2.megagroup) {
                            if (tLRPC$TL_chatAdminRights.post_messages != tLRPC$TL_chatAdminRights2.post_messages) {
                                sb.append('\n');
                                sb.append(tLRPC$TL_chatAdminRights2.post_messages ? '+' : '-');
                                sb.append(' ');
                                sb.append(LocaleController.getString(R.string.EventLogPromotedPostMessages));
                            }
                            if (tLRPC$TL_chatAdminRights.edit_messages != tLRPC$TL_chatAdminRights2.edit_messages) {
                                sb.append('\n');
                                sb.append(tLRPC$TL_chatAdminRights2.edit_messages ? '+' : '-');
                                sb.append(' ');
                                sb.append(LocaleController.getString(R.string.EventLogPromotedEditMessages));
                            }
                        }
                        if (tLRPC$TL_chatAdminRights.post_stories != tLRPC$TL_chatAdminRights2.post_stories) {
                            sb.append('\n');
                            sb.append(tLRPC$TL_chatAdminRights2.post_stories ? '+' : '-');
                            sb.append(' ');
                            sb.append(LocaleController.getString(R.string.EventLogPromotedPostStories));
                        }
                        if (tLRPC$TL_chatAdminRights.edit_stories != tLRPC$TL_chatAdminRights2.edit_stories) {
                            sb.append('\n');
                            sb.append(tLRPC$TL_chatAdminRights2.edit_stories ? '+' : '-');
                            sb.append(' ');
                            sb.append(LocaleController.getString(R.string.EventLogPromotedEditStories));
                        }
                        if (tLRPC$TL_chatAdminRights.delete_stories != tLRPC$TL_chatAdminRights2.delete_stories) {
                            sb.append('\n');
                            sb.append(tLRPC$TL_chatAdminRights2.delete_stories ? '+' : '-');
                            sb.append(' ');
                            sb.append(LocaleController.getString(R.string.EventLogPromotedDeleteStories));
                        }
                        if (tLRPC$TL_chatAdminRights.delete_messages != tLRPC$TL_chatAdminRights2.delete_messages) {
                            sb.append('\n');
                            sb.append(tLRPC$TL_chatAdminRights2.delete_messages ? '+' : '-');
                            sb.append(' ');
                            sb.append(LocaleController.getString(R.string.EventLogPromotedDeleteMessages));
                        }
                        if (tLRPC$TL_chatAdminRights.add_admins != tLRPC$TL_chatAdminRights2.add_admins) {
                            sb.append('\n');
                            sb.append(tLRPC$TL_chatAdminRights2.add_admins ? '+' : '-');
                            sb.append(' ');
                            sb.append(LocaleController.getString(R.string.EventLogPromotedAddAdmins));
                        }
                        if (tLRPC$TL_chatAdminRights.anonymous != tLRPC$TL_chatAdminRights2.anonymous) {
                            sb.append('\n');
                            sb.append(tLRPC$TL_chatAdminRights2.anonymous ? '+' : '-');
                            sb.append(' ');
                            sb.append(LocaleController.getString(R.string.EventLogPromotedSendAnonymously));
                        }
                        if (tLRPC$Chat2.megagroup) {
                            if (tLRPC$TL_chatAdminRights.ban_users != tLRPC$TL_chatAdminRights2.ban_users) {
                                sb.append('\n');
                                sb.append(tLRPC$TL_chatAdminRights2.ban_users ? '+' : '-');
                                sb.append(' ');
                                sb.append(LocaleController.getString(R.string.EventLogPromotedBanUsers));
                            }
                            if (tLRPC$TL_chatAdminRights.manage_call != tLRPC$TL_chatAdminRights2.manage_call) {
                                sb.append('\n');
                                sb.append(tLRPC$TL_chatAdminRights2.manage_call ? '+' : '-');
                                sb.append(' ');
                                sb.append(LocaleController.getString(R.string.EventLogPromotedManageCall));
                            }
                        }
                        if (tLRPC$TL_chatAdminRights.invite_users != tLRPC$TL_chatAdminRights2.invite_users) {
                            sb.append('\n');
                            sb.append(tLRPC$TL_chatAdminRights2.invite_users ? '+' : '-');
                            sb.append(' ');
                            sb.append(LocaleController.getString(R.string.EventLogPromotedAddUsers));
                        }
                        if (tLRPC$Chat2.megagroup && tLRPC$TL_chatAdminRights.pin_messages != tLRPC$TL_chatAdminRights2.pin_messages) {
                            sb.append('\n');
                            sb.append(tLRPC$TL_chatAdminRights2.pin_messages ? c : '-');
                            sb.append(' ');
                            sb.append(LocaleController.getString(R.string.EventLogPromotedPinMessages));
                        }
                    } else {
                        String string5 = LocaleController.getString(R.string.EventLogChangedOwnership);
                        sb = new StringBuilder(String.format(string5, getUserName(user5, this.messageOwner.entities, string5.indexOf("%1$s"))));
                        tLRPC$Chat2 = tLRPC$Chat;
                    }
                    charSequence = sb.toString();
                    tLRPC$Message = null;
                    CharSequence charSequence32 = charSequence;
                    tLRPC$Message2 = tLRPC$Message;
                    spannableStringBuilder7 = charSequence32;
                    this.messageText = spannableStringBuilder7;
                    tLRPC$TL_message = tLRPC$Message2;
                    arrayList3 = null;
                    if (this.messageOwner == null) {
                    }
                    this.messageOwner.message = this.messageText.toString();
                    this.messageOwner.from_id = new TLRPC$TL_peerUser();
                    TLRPC$Message tLRPC$Message6222222 = this.messageOwner;
                    tLRPC$Message6222222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                    tLRPC$Message6222222.date = tLRPC$TL_channelAdminLogEvent2.date;
                    int i23222222 = iArr[0];
                    iArr[0] = i23222222 + 1;
                    tLRPC$Message6222222.id = i23222222;
                    this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                    tLRPC$Message6222222.out = false;
                    tLRPC$Message6222222.peer_id = new TLRPC$TL_peerChannel();
                    TLRPC$Message tLRPC$Message7222222 = this.messageOwner;
                    tLRPC$Message7222222.peer_id.channel_id = tLRPC$Chat2.id;
                    tLRPC$Message7222222.unread = false;
                    mediaController = MediaController.getInstance();
                    this.isOutOwnerCached = null;
                    if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                    }
                    if (tLRPC$TL_message != null) {
                    }
                    if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                    }
                }
            }
            tLRPC$Message = null;
            TLRPC$User tLRPC$User7 = tLRPC$User;
            tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
            tLRPC$User2 = tLRPC$User7;
            string = LocaleController.getString(i3);
            str5 = "un1";
            charSequence2 = string;
            tLRPC$Chat3 = tLRPC$User2;
            str11 = str5;
            charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
            tLRPC$Chat2 = tLRPC$Chat;
            CharSequence charSequence322 = charSequence;
            tLRPC$Message2 = tLRPC$Message;
            spannableStringBuilder7 = charSequence322;
            this.messageText = spannableStringBuilder7;
            tLRPC$TL_message = tLRPC$Message2;
            arrayList3 = null;
            if (this.messageOwner == null) {
            }
            this.messageOwner.message = this.messageText.toString();
            this.messageOwner.from_id = new TLRPC$TL_peerUser();
            TLRPC$Message tLRPC$Message62222222 = this.messageOwner;
            tLRPC$Message62222222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
            tLRPC$Message62222222.date = tLRPC$TL_channelAdminLogEvent2.date;
            int i232222222 = iArr[0];
            iArr[0] = i232222222 + 1;
            tLRPC$Message62222222.id = i232222222;
            this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
            tLRPC$Message62222222.out = false;
            tLRPC$Message62222222.peer_id = new TLRPC$TL_peerChannel();
            TLRPC$Message tLRPC$Message72222222 = this.messageOwner;
            tLRPC$Message72222222.peer_id.channel_id = tLRPC$Chat2.id;
            tLRPC$Message72222222.unread = false;
            mediaController = MediaController.getInstance();
            this.isOutOwnerCached = null;
            if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
            }
            if (tLRPC$TL_message != null) {
            }
            if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
            }
        }
        str5 = "un1";
        tLRPC$Message = null;
        TLRPC$User tLRPC$User8 = tLRPC$User;
        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
        tLRPC$User2 = tLRPC$User8;
        charSequence2 = string;
        tLRPC$Chat3 = tLRPC$User2;
        str11 = str5;
        charSequence = replaceWithLink(charSequence2, str11, tLRPC$Chat3);
        tLRPC$Chat2 = tLRPC$Chat;
        CharSequence charSequence3222 = charSequence;
        tLRPC$Message2 = tLRPC$Message;
        spannableStringBuilder7 = charSequence3222;
        this.messageText = spannableStringBuilder7;
        tLRPC$TL_message = tLRPC$Message2;
        arrayList3 = null;
        if (this.messageOwner == null) {
        }
        this.messageOwner.message = this.messageText.toString();
        this.messageOwner.from_id = new TLRPC$TL_peerUser();
        TLRPC$Message tLRPC$Message622222222 = this.messageOwner;
        tLRPC$Message622222222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
        tLRPC$Message622222222.date = tLRPC$TL_channelAdminLogEvent2.date;
        int i2322222222 = iArr[0];
        iArr[0] = i2322222222 + 1;
        tLRPC$Message622222222.id = i2322222222;
        this.eventId = tLRPC$TL_channelAdminLogEvent2.id;
        tLRPC$Message622222222.out = false;
        tLRPC$Message622222222.peer_id = new TLRPC$TL_peerChannel();
        TLRPC$Message tLRPC$Message722222222 = this.messageOwner;
        tLRPC$Message722222222.peer_id.channel_id = tLRPC$Chat2.id;
        tLRPC$Message722222222.unread = false;
        mediaController = MediaController.getInstance();
        this.isOutOwnerCached = null;
        if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
        }
        if (tLRPC$TL_message != null) {
        }
        if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
        }
    }

    public MessageObject(int i, TL_stories$StoryItem tL_stories$StoryItem) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.actionDeleteGroupEventId = -1L;
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
            tLRPC$TL_message.id = tL_stories$StoryItem.messageId;
            tLRPC$TL_message.realId = tL_stories$StoryItem.id;
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

    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, boolean z, boolean z2, boolean z3, boolean z4) {
        return addEntitiesToText(charSequence, arrayList, z, z2, z3, z4, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:110:0x013c, code lost:
        if (r22 == false) goto L161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0144, code lost:
        if (r22 == false) goto L161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0148, code lost:
        r4.flags = 64;
        r4.urlEntity = r9;
     */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0186  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0233  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0419 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:278:0x0236 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, boolean z, boolean z2, boolean z3, boolean z4, int i) {
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
        if (charSequence2 instanceof Spannable) {
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
                    lambda$addEntitiesToText$2 = MessageObject.lambda$addEntitiesToText$2((TLRPC$MessageEntity) obj, (TLRPC$MessageEntity) obj2);
                    return lambda$addEntitiesToText$2;
                }
            });
            int size2 = arrayList3.size();
            int i7 = 0;
            while (i7 < size2) {
                TLRPC$MessageEntity tLRPC$MessageEntity = (TLRPC$MessageEntity) arrayList3.get(i7);
                if (tLRPC$MessageEntity.length > 0 && (i4 = tLRPC$MessageEntity.offset) >= 0 && i4 < charSequence.length()) {
                    if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length > charSequence.length()) {
                        tLRPC$MessageEntity.length = charSequence.length() - tLRPC$MessageEntity.offset;
                    }
                    if ((!z4 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji)) && uRLSpanArr != null && uRLSpanArr.length > 0) {
                        for (int i8 = 0; i8 < uRLSpanArr.length; i8++) {
                            URLSpan uRLSpan = uRLSpanArr[i8];
                            if (uRLSpan != null) {
                                int spanStart = spannable.getSpanStart(uRLSpan);
                                int spanEnd = spannable.getSpanEnd(uRLSpanArr[i8]);
                                int i9 = tLRPC$MessageEntity.offset;
                                if ((i9 <= spanStart && tLRPC$MessageEntity.length + i9 >= spanStart) || (i9 <= spanEnd && i9 + tLRPC$MessageEntity.length >= spanEnd)) {
                                    spannable.removeSpan(uRLSpanArr[i8]);
                                    uRLSpanArr[i8] = null;
                                }
                            }
                        }
                    }
                    if ((i != 1 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityHashtag)) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre)) {
                        TextStyleSpan.TextStyleRun textStyleRun3 = new TextStyleSpan.TextStyleRun();
                        int i10 = tLRPC$MessageEntity.offset;
                        textStyleRun3.start = i10;
                        textStyleRun3.end = i10 + tLRPC$MessageEntity.length;
                        if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) {
                            i5 = 256;
                        } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) {
                            i5 = 8;
                        } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) {
                            i5 = 16;
                        } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) {
                            i5 = 1;
                        } else {
                            if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) {
                                textStyleRun3.flags = 2;
                            } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) {
                                textStyleRun3.flags = 4;
                            } else if (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName)) {
                                if (!(tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName)) {
                                    if ((!z4 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl)) && (((!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUrl) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl)) || !Browser.isPassportUrl(tLRPC$MessageEntity.url)) && (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMention) || z2))) {
                                        textStyleRun3.flags = 128;
                                        textStyleRun3.urlEntity = tLRPC$MessageEntity;
                                        if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) {
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
                if (i != 1 || (textStyleRun8.urlEntity instanceof TLRPC$TL_messageEntityHashtag)) {
                    TLRPC$MessageEntity tLRPC$MessageEntity2 = textStyleRun8.urlEntity;
                    if (tLRPC$MessageEntity2 != null) {
                        int i19 = tLRPC$MessageEntity2.offset;
                        str = TextUtils.substring(charSequence2, i19, tLRPC$MessageEntity2.length + i19);
                    } else {
                        str = null;
                    }
                    TLRPC$MessageEntity tLRPC$MessageEntity3 = textStyleRun8.urlEntity;
                    if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBotCommand) {
                        uRLSpanReplacement = new URLSpanBotCommand(str, b, textStyleRun8);
                    } else {
                        if ((tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityHashtag) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMention) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityCashtag)) {
                            textStyleRun = textStyleRun8;
                            uRLSpanNoUnderline = new URLSpanNoUnderline(str, textStyleRun);
                        } else {
                            if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityEmail) {
                                uRLSpanUserMention = new URLSpanReplacement("mailto:" + str, textStyleRun8);
                            } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityUrl) {
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
                                if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBankCard) {
                                    uRLSpanUserMention = new URLSpanNoUnderline("card:" + str, textStyleRun8);
                                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityPhone) {
                                    String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str);
                                    if (str.startsWith("+")) {
                                        stripExceptNumbers = "+" + stripExceptNumbers;
                                    }
                                    uRLSpanUserMention = new URLSpanNoUnderline("tel:" + stripExceptNumbers, textStyleRun8);
                                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityTextUrl) {
                                    String str2 = tLRPC$MessageEntity3.url;
                                    if (str2 != null) {
                                        str2 = str2.replaceAll("∕|⁄|%E2%81%84|%E2%88%95", "/");
                                    }
                                    if (!Browser.isTonsitePunycode(str2)) {
                                        uRLSpanReplacement = new URLSpanReplacement(str2, textStyleRun8);
                                    }
                                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMentionName) {
                                    uRLSpanUserMention = new URLSpanUserMention("" + ((TLRPC$TL_messageEntityMentionName) textStyleRun8.urlEntity).user_id, b, textStyleRun8);
                                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_inputMessageEntityMentionName) {
                                    uRLSpanUserMention = new URLSpanUserMention("" + ((TLRPC$TL_inputMessageEntityMentionName) textStyleRun8.urlEntity).user_id.user_id, b, textStyleRun8);
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
                TLRPC$MessageEntity tLRPC$MessageEntity4 = (TLRPC$MessageEntity) arrayList3.get(i20);
                if (tLRPC$MessageEntity4.length > 0 && (i2 = tLRPC$MessageEntity4.offset) >= 0 && i2 < charSequence.length()) {
                    if (tLRPC$MessageEntity4.offset + tLRPC$MessageEntity4.length > charSequence.length()) {
                        tLRPC$MessageEntity4.length = charSequence.length() - tLRPC$MessageEntity4.offset;
                    }
                    if (tLRPC$MessageEntity4 instanceof TLRPC$TL_messageEntityBlockquote) {
                        int i21 = tLRPC$MessageEntity4.offset;
                        QuoteSpan.putQuote(spannable, i21, tLRPC$MessageEntity4.length + i21, tLRPC$MessageEntity4.collapsed);
                    } else if (tLRPC$MessageEntity4 instanceof TLRPC$TL_messageEntityPre) {
                        int i22 = tLRPC$MessageEntity4.offset;
                        int i23 = tLRPC$MessageEntity4.length + i22;
                        spannable.setSpan(new CodeHighlighting.Span(true, 0, null, tLRPC$MessageEntity4.language, spannable.subSequence(i22, i23).toString()), i22, i23, 33);
                    }
                }
            }
            return z8;
        }
        return false;
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

    public static void addPaidReactions(int i, TLRPC$MessageReactions tLRPC$MessageReactions, int i2, boolean z, boolean z2) {
        TLRPC$MessageReactor tLRPC$MessageReactor = null;
        TLRPC$ReactionCount tLRPC$ReactionCount = null;
        for (int i3 = 0; i3 < tLRPC$MessageReactions.results.size(); i3++) {
            if (((TLRPC$ReactionCount) tLRPC$MessageReactions.results.get(i3)).reaction instanceof TLRPC$TL_reactionPaid) {
                tLRPC$ReactionCount = (TLRPC$ReactionCount) tLRPC$MessageReactions.results.get(i3);
            }
        }
        int i4 = 0;
        while (true) {
            if (i4 >= tLRPC$MessageReactions.top_reactors.size()) {
                break;
            } else if (((TLRPC$MessageReactor) tLRPC$MessageReactions.top_reactors.get(i4)).my) {
                tLRPC$MessageReactor = (TLRPC$MessageReactor) tLRPC$MessageReactions.top_reactors.get(i4);
                break;
            } else {
                i4++;
            }
        }
        if (tLRPC$ReactionCount == null && i2 > 0) {
            tLRPC$ReactionCount = new TLRPC$TL_reactionCount();
            tLRPC$ReactionCount.reaction = new TLRPC$TL_reactionPaid();
            tLRPC$MessageReactions.results.add(0, tLRPC$ReactionCount);
        }
        if (tLRPC$ReactionCount != null) {
            tLRPC$ReactionCount.chosen = z2;
            int max = Math.max(0, tLRPC$ReactionCount.count + i2);
            tLRPC$ReactionCount.count = max;
            if (max <= 0) {
                tLRPC$MessageReactions.results.remove(tLRPC$ReactionCount);
            }
        }
        if (tLRPC$MessageReactor == null && i2 > 0) {
            tLRPC$MessageReactor = new TLRPC$TL_messageReactor();
            tLRPC$MessageReactor.my = true;
            tLRPC$MessageReactor.peer_id = MessagesController.getInstance(i).getPeer(UserConfig.getInstance(i).getClientUserId());
            tLRPC$MessageReactions.top_reactors.add(tLRPC$MessageReactor);
        }
        if (tLRPC$MessageReactor != null) {
            int max2 = Math.max(0, tLRPC$MessageReactor.count + i2);
            tLRPC$MessageReactor.count = max2;
            tLRPC$MessageReactor.anonymous = z;
            if (max2 <= 0) {
                tLRPC$MessageReactions.top_reactors.remove(tLRPC$MessageReactor);
            }
        }
    }

    public static void addPhoneLinks(CharSequence charSequence) {
    }

    /* JADX WARN: Removed duplicated region for block: B:89:0x01d4 A[Catch: Exception -> 0x001e, TryCatch #0 {Exception -> 0x001e, blocks: (B:10:0x0011, B:12:0x0015, B:15:0x0021, B:24:0x0041, B:27:0x004a, B:28:0x004d, B:30:0x0053, B:34:0x0064, B:38:0x0074, B:39:0x0076, B:51:0x008f, B:101:0x0214, B:103:0x021e, B:105:0x0221, B:106:0x0226, B:55:0x00b6, B:58:0x00db, B:59:0x00fc, B:60:0x011d, B:63:0x0125, B:65:0x013e, B:67:0x014a, B:68:0x0153, B:48:0x0089, B:69:0x015d, B:72:0x019c, B:77:0x01af, B:82:0x01be, B:84:0x01c8, B:87:0x01cf, B:89:0x01d4, B:95:0x01df, B:97:0x020d, B:96:0x01f7, B:16:0x0024, B:18:0x0028, B:19:0x0030, B:20:0x0033, B:22:0x0037, B:23:0x003f), top: B:110:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01dc  */
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
                    urlPattern = Pattern.compile("(^|\\s)/[a-zA-Z@\\d_]{1,255}|(^|\\s|\\()@[a-zA-Z\\d_]{1,32}|(^|\\s|\\()#[^0-9][\\w.]+|(^|\\s)\\$[A-Z]{3,8}([ ,.]|$)");
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
        ArrayList arrayList = (!this.translated || (tLRPC$TL_textWithEntities = this.messageOwner.translatedText) == null) ? this.messageOwner.entities : tLRPC$TL_textWithEntities.entities;
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

    public static boolean canAutoplayAnimatedSticker(TLRPC$Document tLRPC$Document) {
        return (isAnimatedStickerDocument(tLRPC$Document, true) || isVideoStickerDocument(tLRPC$Document)) && LiteMode.isEnabled(1);
    }

    public static boolean canCreateStripedThubms() {
        return SharedConfig.getDevicePerformanceClass() == 2;
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

    public static boolean canEditMessage(int i, TLRPC$Message tLRPC$Message, TLRPC$Chat tLRPC$Chat, boolean z) {
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights3;
        if (!z || tLRPC$Message.date >= ConnectionsManager.getInstance(i).getCurrentTime() - 60) {
            if (tLRPC$Chat == null || (!(tLRPC$Chat.left || tLRPC$Chat.kicked) || (tLRPC$Chat.megagroup && tLRPC$Chat.has_link))) {
                TLRPC$MessageMedia media = getMedia(tLRPC$Message);
                if (tLRPC$Message != null && tLRPC$Message.peer_id != null && ((media == null || (!isRoundVideoDocument(media.document) && !isStickerDocument(media.document) && !isAnimatedStickerDocument(media.document, true) && !isLocationMessage(tLRPC$Message))) && (((tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty)) && !isForwardedMessage(tLRPC$Message) && tLRPC$Message.via_bot_id == 0 && tLRPC$Message.id >= 0))) {
                    TLRPC$Peer tLRPC$Peer = tLRPC$Message.from_id;
                    if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                        long j = tLRPC$Peer.user_id;
                        if (j == tLRPC$Message.peer_id.user_id && j == UserConfig.getInstance(i).getClientUserId() && !isLiveLocationMessage(tLRPC$Message) && !(media instanceof TLRPC$TL_messageMediaContact)) {
                            return true;
                        }
                    }
                    if (tLRPC$Chat == null && tLRPC$Message.peer_id.channel_id != 0 && (tLRPC$Chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Message.peer_id.channel_id))) == null) {
                        return false;
                    }
                    if (media != null && !(media instanceof TLRPC$TL_messageMediaEmpty) && !(media instanceof TLRPC$TL_messageMediaPhoto) && !(media instanceof TLRPC$TL_messageMediaDocument) && !(media instanceof TLRPC$TL_messageMediaWebPage) && !(media instanceof TLRPC$TL_messageMediaPaidMedia)) {
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
                        return (media instanceof TLRPC$TL_messageMediaPhoto) || !(!(media instanceof TLRPC$TL_messageMediaDocument) || isStickerMessage(tLRPC$Message) || isAnimatedStickerMessage(tLRPC$Message)) || (media instanceof TLRPC$TL_messageMediaEmpty) || (media instanceof TLRPC$TL_messageMediaWebPage) || (media instanceof TLRPC$TL_messageMediaPaidMedia) || media == null;
                    } else if (((tLRPC$Chat != null && tLRPC$Chat.megagroup && tLRPC$Message.out) || (tLRPC$Chat != null && !tLRPC$Chat.megagroup && ((tLRPC$Chat.creator || ((tLRPC$TL_chatAdminRights = tLRPC$Chat.admin_rights) != null && (tLRPC$TL_chatAdminRights.edit_messages || (tLRPC$Message.out && tLRPC$TL_chatAdminRights.post_messages)))) && tLRPC$Message.post))) && ((media instanceof TLRPC$TL_messageMediaPhoto) || (((media instanceof TLRPC$TL_messageMediaDocument) && !isStickerMessage(tLRPC$Message) && !isAnimatedStickerMessage(tLRPC$Message)) || (media instanceof TLRPC$TL_messageMediaEmpty) || (media instanceof TLRPC$TL_messageMediaWebPage) || (media instanceof TLRPC$TL_messageMediaPaidMedia) || media == null))) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
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

    private void createPathThumb() {
        TLRPC$Document document = getDocument();
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
                            TLRPC$TL_stickerPack tLRPC$TL_stickerPack = (TLRPC$TL_stickerPack) stickerSet.packs.get(i2);
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

    public static ArrayList<String> findStickerEmoticons(TLRPC$Document tLRPC$Document, Integer num) {
        if (tLRPC$Document == null) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        int size = tLRPC$Document.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeCustomEmoji) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker)) {
                if (num != null) {
                    TLRPC$TL_messages_stickerSet stickerSet = MediaDataController.getInstance(num.intValue()).getStickerSet(tLRPC$DocumentAttribute.stickerset, true);
                    if (stickerSet != null && stickerSet.packs != null) {
                        for (int i2 = 0; i2 < stickerSet.packs.size(); i2++) {
                            TLRPC$TL_stickerPack tLRPC$TL_stickerPack = (TLRPC$TL_stickerPack) stickerSet.packs.get(i2);
                            if (tLRPC$TL_stickerPack.documents.contains(Long.valueOf(tLRPC$Document.id)) && Emoji.getEmojiDrawable(tLRPC$TL_stickerPack.emoticon) != null) {
                                arrayList.add(tLRPC$TL_stickerPack.emoticon);
                            }
                        }
                    }
                    if (!arrayList.isEmpty()) {
                        return arrayList;
                    }
                }
                if (!TextUtils.isEmpty(tLRPC$DocumentAttribute.alt) && Emoji.getEmojiDrawable(tLRPC$DocumentAttribute.alt) != null) {
                    arrayList.add(tLRPC$DocumentAttribute.alt);
                    return arrayList;
                }
            }
        }
        return null;
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

    public static long getChannelId(TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
        if (tLRPC$Peer != null) {
            return tLRPC$Peer.channel_id;
        }
        return 0L;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0017  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TLRPC$Chat getChat(AbstractMap<Long, TLRPC$Chat> abstractMap, LongSparseArray longSparseArray, long j) {
        TLRPC$Chat tLRPC$Chat;
        Object obj;
        if (abstractMap != null) {
            obj = abstractMap.get(Long.valueOf(j));
        } else if (longSparseArray == null) {
            tLRPC$Chat = null;
            return tLRPC$Chat != null ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j)) : tLRPC$Chat;
        } else {
            obj = longSparseArray.get(j);
        }
        tLRPC$Chat = (TLRPC$Chat) obj;
        if (tLRPC$Chat != null) {
        }
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

    public static long getDialogId(TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer;
        long j;
        if (tLRPC$Message.dialog_id == 0 && (tLRPC$Peer = tLRPC$Message.peer_id) != null) {
            long j2 = tLRPC$Peer.chat_id;
            if (j2 != 0) {
                j = -j2;
            } else {
                long j3 = tLRPC$Peer.channel_id;
                if (j3 != 0) {
                    j = -j3;
                } else {
                    j = ((tLRPC$Message.from_id == null || isOut(tLRPC$Message)) ? tLRPC$Message.peer_id : tLRPC$Message.from_id).user_id;
                }
            }
            tLRPC$Message.dialog_id = j;
        }
        return tLRPC$Message.dialog_id;
    }

    public static TLRPC$Document getDocument(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        TLRPC$Document tLRPC$Document;
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return getMedia(tLRPC$Message).webpage.document;
        }
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGame) {
            return getMedia(tLRPC$Message).game.document;
        }
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaStory) {
            TL_stories$StoryItem tL_stories$StoryItem = ((TLRPC$TL_messageMediaStory) getMedia(tLRPC$Message)).storyItem;
            if (tL_stories$StoryItem != null && (tLRPC$MessageMedia = tL_stories$StoryItem.media) != null && (tLRPC$Document = tLRPC$MessageMedia.document) != null) {
                return tLRPC$Document;
            }
        } else if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPaidMedia) {
            TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) getMedia(tLRPC$Message);
            if (tLRPC$TL_messageMediaPaidMedia.extended_media.size() == 1 && (tLRPC$TL_messageMediaPaidMedia.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMedia)) {
                return ((TLRPC$TL_messageExtendedMedia) tLRPC$TL_messageMediaPaidMedia.extended_media.get(0)).media.document;
            }
        }
        if (getMedia(tLRPC$Message) != null) {
            return getMedia(tLRPC$Message).document;
        }
        return null;
    }

    public static double getDocumentDuration(TLRPC$Document tLRPC$Document) {
        int i;
        if (tLRPC$Document == null) {
            return 0.0d;
        }
        int size = tLRPC$Document.attributes.size();
        while (i < size) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            i = ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio)) ? 0 : i + 1;
            return tLRPC$DocumentAttribute.duration;
        }
        return 0.0d;
    }

    public static TLRPC$VideoSize getDocumentVideoThumb(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null || tLRPC$Document.video_thumbs.isEmpty()) {
            return null;
        }
        return tLRPC$Document.video_thumbs.get(0);
    }

    private TLRPC$Document getDocumentWithId(TLRPC$WebPage tLRPC$WebPage, long j) {
        if (tLRPC$WebPage != null && tLRPC$WebPage.cached_page != null) {
            TLRPC$Document tLRPC$Document = tLRPC$WebPage.document;
            if (tLRPC$Document != null && tLRPC$Document.id == j) {
                return tLRPC$Document;
            }
            for (int i = 0; i < tLRPC$WebPage.cached_page.documents.size(); i++) {
                TLRPC$Document tLRPC$Document2 = (TLRPC$Document) tLRPC$WebPage.cached_page.documents.get(i);
                if (tLRPC$Document2.id == j) {
                    return tLRPC$Document2;
                }
            }
        }
        return null;
    }

    public static String getFileName(TLRPC$Message tLRPC$Message) {
        TLRPC$Document tLRPC$Document;
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument) {
            tLRPC$Document = getDocument(tLRPC$Message);
        } else if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) {
            ArrayList arrayList = getMedia(tLRPC$Message).photo.sizes;
            return (arrayList.size() <= 0 || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) == null) ? "" : FileLoader.getAttachFileName(closestPhotoSizeWithSize);
        } else if (!(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(tLRPC$Message).webpage == null) {
            return "";
        } else {
            tLRPC$Document = getMedia(tLRPC$Message).webpage.document;
        }
        return FileLoader.getAttachFileName(tLRPC$Document);
    }

    public static String getFileName(TLRPC$MessageMedia tLRPC$MessageMedia) {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$Document tLRPC$Document;
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            tLRPC$Document = tLRPC$MessageMedia.document;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            ArrayList arrayList = tLRPC$MessageMedia.photo.sizes;
            return (arrayList.size() <= 0 || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) == null) ? "" : FileLoader.getAttachFileName(closestPhotoSizeWithSize);
        } else if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) || (tLRPC$WebPage = tLRPC$MessageMedia.webpage) == null) {
            return "";
        } else {
            tLRPC$Document = tLRPC$WebPage.document;
        }
        return FileLoader.getAttachFileName(tLRPC$Document);
    }

    public static long getFromChatId(TLRPC$Message tLRPC$Message) {
        return getPeerId(tLRPC$Message.from_id);
    }

    public static int getInlineResultDuration(TLRPC$BotInlineResult tLRPC$BotInlineResult) {
        int webDocumentDuration = (int) getWebDocumentDuration(tLRPC$BotInlineResult.content);
        return webDocumentDuration == 0 ? (int) getWebDocumentDuration(tLRPC$BotInlineResult.thumb) : webDocumentDuration;
    }

    public static int[] getInlineResultWidthAndHeight(TLRPC$BotInlineResult tLRPC$BotInlineResult) {
        int[] webDocumentWidthAndHeight = getWebDocumentWidthAndHeight(tLRPC$BotInlineResult.content);
        if (webDocumentWidthAndHeight == null) {
            int[] webDocumentWidthAndHeight2 = getWebDocumentWidthAndHeight(tLRPC$BotInlineResult.thumb);
            return webDocumentWidthAndHeight2 == null ? new int[]{0, 0} : webDocumentWidthAndHeight2;
        }
        return webDocumentWidthAndHeight;
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

    public static TLRPC$InputStickerSet getInputStickerSet(TLRPC$Message tLRPC$Message) {
        TLRPC$Document document = getDocument(tLRPC$Message);
        if (document != null) {
            return getInputStickerSet(document);
        }
        return null;
    }

    public static TLRPC$MessageMedia getMedia(MessageObject messageObject) {
        TLRPC$Message tLRPC$Message;
        if (messageObject == null || (tLRPC$Message = messageObject.messageOwner) == null) {
            return null;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.sponsoredMedia;
        return tLRPC$MessageMedia != null ? tLRPC$MessageMedia : getMedia(tLRPC$Message);
    }

    public static TLRPC$MessageMedia getMedia(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        return (tLRPC$MessageMedia == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) || tLRPC$MessageMedia.extended_media.isEmpty() || !(tLRPC$Message.media.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMedia)) ? tLRPC$Message.media : ((TLRPC$TL_messageExtendedMedia) tLRPC$Message.media.extended_media.get(0)).media;
    }

    public static long getMediaSize(TLRPC$MessageMedia tLRPC$MessageMedia) {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$Document tLRPC$Document = (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) || (tLRPC$WebPage = tLRPC$MessageMedia.webpage) == null) ? tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame ? tLRPC$MessageMedia.game.document : tLRPC$MessageMedia != null ? tLRPC$MessageMedia.document : null : tLRPC$WebPage.document;
        if (tLRPC$Document != null) {
            return tLRPC$Document.size;
        }
        return 0L;
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

    public static long getMessageSize(TLRPC$Message tLRPC$Message) {
        return getMediaSize(getMedia(tLRPC$Message));
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

    private int getParentWidth() {
        int i;
        return (!this.preview || (i = this.parentWidth) <= 0) ? AndroidUtilities.displaySize.x : i;
    }

    public static long getPeerId(TLRPC$Peer tLRPC$Peer) {
        long j;
        if (tLRPC$Peer == null) {
            return 0L;
        }
        if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
            j = tLRPC$Peer.chat_id;
        } else if (!(tLRPC$Peer instanceof TLRPC$TL_peerChannel)) {
            return tLRPC$Peer.user_id;
        } else {
            j = tLRPC$Peer.channel_id;
        }
        return -j;
    }

    public static String getPeerObjectName(TLObject tLObject) {
        return tLObject instanceof TLRPC$User ? UserObject.getUserName((TLRPC$User) tLObject) : tLObject instanceof TLRPC$Chat ? ((TLRPC$Chat) tLObject).title : "DELETED";
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

    private TLRPC$Photo getPhotoWithId(TLRPC$WebPage tLRPC$WebPage, long j) {
        if (tLRPC$WebPage != null && tLRPC$WebPage.cached_page != null) {
            TLRPC$Photo tLRPC$Photo = tLRPC$WebPage.photo;
            if (tLRPC$Photo != null && tLRPC$Photo.id == j) {
                return tLRPC$Photo;
            }
            for (int i = 0; i < tLRPC$WebPage.cached_page.photos.size(); i++) {
                TLRPC$Photo tLRPC$Photo2 = (TLRPC$Photo) tLRPC$WebPage.cached_page.photos.get(i);
                if (tLRPC$Photo2.id == j) {
                    return tLRPC$Photo2;
                }
            }
        }
        return null;
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

    public static long getReplyToDialogId(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = tLRPC$Message.reply_to;
        if (tLRPC$MessageReplyHeader == null) {
            return 0L;
        }
        TLRPC$Peer tLRPC$Peer = tLRPC$MessageReplyHeader.reply_to_peer_id;
        return tLRPC$Peer != null ? getPeerId(tLRPC$Peer) : getDialogId(tLRPC$Message);
    }

    public static long getSavedDialogId(long j, TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Peer tLRPC$Peer2 = tLRPC$Message.saved_peer_id;
        if (tLRPC$Peer2 == null) {
            if (tLRPC$Message.from_id.user_id == j) {
                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
                return (tLRPC$MessageFwdHeader == null || (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) == null) ? ((tLRPC$MessageFwdHeader == null || tLRPC$MessageFwdHeader.from_id == null) && tLRPC$MessageFwdHeader != null) ? UserObject.ANONYMOUS : j : DialogObject.getPeerDialogId(tLRPC$Peer);
            }
            return 0L;
        }
        long j2 = tLRPC$Peer2.chat_id;
        if (j2 != 0) {
            return -j2;
        }
        long j3 = tLRPC$Peer2.channel_id;
        return j3 != 0 ? -j3 : tLRPC$Peer2.user_id;
    }

    public static TLRPC$Peer getSavedDialogPeer(long j, TLRPC$Message tLRPC$Message) {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$TL_peerUser tLRPC$TL_peerUser;
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
                tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
            } else if (tLRPC$MessageFwdHeader != null) {
                TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                tLRPC$TL_peerUser2.user_id = UserObject.ANONYMOUS;
                return tLRPC$TL_peerUser2;
            } else {
                tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
            }
            tLRPC$TL_peerUser.user_id = j;
            return tLRPC$TL_peerUser;
        }
        return tLRPC$Peer2;
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

    private CharSequence getStringFrom(TLRPC$ChatReactions tLRPC$ChatReactions) {
        int i;
        if (tLRPC$ChatReactions instanceof TLRPC$TL_chatReactionsAll) {
            i = R.string.AllReactions;
        } else if (tLRPC$ChatReactions instanceof TLRPC$TL_chatReactionsSome) {
            TLRPC$TL_chatReactionsSome tLRPC$TL_chatReactionsSome = (TLRPC$TL_chatReactionsSome) tLRPC$ChatReactions;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (int i2 = 0; i2 < tLRPC$TL_chatReactionsSome.reactions.size(); i2++) {
                if (i2 != 0) {
                    spannableStringBuilder.append((CharSequence) " ");
                }
                spannableStringBuilder.append(Emoji.replaceEmoji(ReactionsUtils.reactionToCharSequence((TLRPC$Reaction) tLRPC$TL_chatReactionsSome.reactions.get(i2)), null, false));
            }
            return spannableStringBuilder;
        } else {
            i = R.string.NoReactions;
        }
        return LocaleController.getString(i);
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
            if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicCreate)) {
                TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = tLRPC$Message.reply_to;
                if (tLRPC$MessageReplyHeader == null || !tLRPC$MessageReplyHeader.forum_topic) {
                    return z ? 1L : 0L;
                } else if (!(tLRPC$Message instanceof TLRPC$TL_messageService) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPinMessage)) {
                    int i3 = tLRPC$MessageReplyHeader.reply_to_top_id;
                    if (i3 == 0) {
                        i3 = tLRPC$MessageReplyHeader.reply_to_msg_id;
                    }
                    return i3;
                } else {
                    int i4 = tLRPC$MessageReplyHeader.reply_to_msg_id;
                    if (i4 == 0) {
                        i4 = tLRPC$MessageReplyHeader.reply_to_top_id;
                    }
                    return i4;
                }
            }
            i2 = tLRPC$Message.id;
        }
        return i2;
    }

    private static long getTopicId(MessageObject messageObject) {
        if (messageObject == null) {
            return 0L;
        }
        return getTopicId(messageObject.currentAccount, messageObject.messageOwner, false);
    }

    public static int getUnreadFlags(TLRPC$Message tLRPC$Message) {
        int i = !tLRPC$Message.unread ? 1 : 0;
        return !tLRPC$Message.media_unread ? i | 2 : i;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0017  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TLRPC$User getUser(AbstractMap<Long, TLRPC$User> abstractMap, LongSparseArray longSparseArray, long j) {
        TLRPC$User tLRPC$User;
        Object obj;
        if (abstractMap != null) {
            obj = abstractMap.get(Long.valueOf(j));
        } else if (longSparseArray == null) {
            tLRPC$User = null;
            return tLRPC$User != null ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j)) : tLRPC$User;
        } else {
            obj = longSparseArray.get(j);
        }
        tLRPC$User = (TLRPC$User) obj;
        if (tLRPC$User != null) {
        }
    }

    private String getUserName(TLObject tLObject, ArrayList<TLRPC$MessageEntity> arrayList, int i) {
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
            if (tLObject instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                str = tLRPC$User.deleted ? LocaleController.getString(R.string.HiddenName) : ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                publicUsername = UserObject.getPublicUsername(tLRPC$User);
                j = tLRPC$User.id;
            } else {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLObject;
                str = tLRPC$Chat.title;
                publicUsername = ChatObject.getPublicUsername(tLRPC$Chat);
                j = -tLRPC$Chat.id;
            }
            str2 = str;
            str3 = publicUsername;
            j2 = j;
        }
        if (i >= 0) {
            TLRPC$TL_messageEntityMentionName tLRPC$TL_messageEntityMentionName = new TLRPC$TL_messageEntityMentionName();
            tLRPC$TL_messageEntityMentionName.user_id = j2;
            tLRPC$TL_messageEntityMentionName.offset = i;
            tLRPC$TL_messageEntityMentionName.length = str2.length();
            arrayList.add(tLRPC$TL_messageEntityMentionName);
        }
        if (TextUtils.isEmpty(str3)) {
            return str2;
        }
        if (i >= 0) {
            TLRPC$TL_messageEntityMentionName tLRPC$TL_messageEntityMentionName2 = new TLRPC$TL_messageEntityMentionName();
            tLRPC$TL_messageEntityMentionName2.user_id = j2;
            tLRPC$TL_messageEntityMentionName2.offset = i + str2.length() + 2;
            tLRPC$TL_messageEntityMentionName2.length = str3.length() + 1;
            arrayList.add(tLRPC$TL_messageEntityMentionName2);
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

    public static double getWebDocumentDuration(TLRPC$WebDocument tLRPC$WebDocument) {
        int i;
        if (tLRPC$WebDocument == null) {
            return 0.0d;
        }
        int size = tLRPC$WebDocument.attributes.size();
        while (i < size) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = (TLRPC$DocumentAttribute) tLRPC$WebDocument.attributes.get(i);
            i = ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio)) ? 0 : i + 1;
            return tLRPC$DocumentAttribute.duration;
        }
        return 0.0d;
    }

    public static int[] getWebDocumentWidthAndHeight(TLRPC$WebDocument tLRPC$WebDocument) {
        if (tLRPC$WebDocument == null) {
            return null;
        }
        int size = tLRPC$WebDocument.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = (TLRPC$DocumentAttribute) tLRPC$WebDocument.attributes.get(i);
            if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) || (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo)) {
                return new int[]{tLRPC$DocumentAttribute.w, tLRPC$DocumentAttribute.h};
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
                Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda9
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$handleFoundWords$3;
                        lambda$handleFoundWords$3 = MessageObject.lambda$handleFoundWords$3((String) obj, (String) obj2);
                        return lambda$handleFoundWords$3;
                    }
                });
                arrayList.clear();
                arrayList.add(arrayList.get(0));
            }
        }
        this.highlightedWords = arrayList;
        if (this.messageOwner.message != null) {
            applyEntities();
            CharSequence replaceMultipleCharSequence = AndroidUtilities.replaceMultipleCharSequence("\n", !TextUtils.isEmpty(this.caption) ? this.caption : this.messageText, " ");
            if (z && (tLRPC$Message = this.messageOwner) != null && (tLRPC$MessageReplyHeader = tLRPC$Message.reply_to) != null && tLRPC$MessageReplyHeader.quote_text != null) {
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
            if (length > 120) {
                float f = 120;
                int max = Math.max(0, indexOf - ((int) (0.1f * f)));
                replaceMultipleCharSequence = replaceMultipleCharSequence.subSequence(max, Math.min(length, (indexOf - max) + indexOf + ((int) (f * 0.9f))));
            }
            this.messageTrimmedToHighlight = replaceMultipleCharSequence;
        }
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
            if (((TLRPC$MessagePeerReaction) tLRPC$TL_messageReactions.recent_reactions.get(i)).unread) {
                return true;
            }
        }
        return false;
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

    public static boolean isAnimatedStickerDocument(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && tLRPC$Document.mime_type.equals("video/webm");
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

    public static boolean isAnimatedStickerMessage(TLRPC$Message tLRPC$Message) {
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(tLRPC$Message.dialog_id);
        if ((!isEncryptedDialog || tLRPC$Message.stickerVerified == 1) && getMedia(tLRPC$Message) != null) {
            return isAnimatedStickerDocument(getMedia(tLRPC$Message).document, !isEncryptedDialog || tLRPC$Message.out);
        }
        return false;
    }

    public static boolean isContentUnread(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.media_unread;
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

    public static boolean isExtendedVideo(TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia) {
        if (!(tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia)) {
            return (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) && (((TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia).flags & 4) != 0;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media;
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && isVideoDocument(tLRPC$MessageMedia.document);
    }

    public static boolean isForwardedMessage(TLRPC$Message tLRPC$Message) {
        return ((tLRPC$Message.flags & 4) == 0 || tLRPC$Message.fwd_from == null) ? false : true;
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

    public static boolean isGameMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGame;
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

    public static boolean isGifMessage(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) {
            return isGifDocument(getMedia(tLRPC$Message).webpage.document);
        }
        if (getMedia(tLRPC$Message) != null) {
            return isGifDocument(getMedia(tLRPC$Message).document, (tLRPC$Message.grouped_id > 0L ? 1 : (tLRPC$Message.grouped_id == 0L ? 0 : -1)) != 0);
        }
        return false;
    }

    public static boolean isImageWebDocument(WebFile webFile) {
        return (webFile == null || isGifDocument(webFile) || !webFile.mime_type.startsWith("image/")) ? false : true;
    }

    public static boolean isInvoiceMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaInvoice;
    }

    public static boolean isLiveLocationMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGeoLive;
    }

    public static boolean isLocationMessage(TLRPC$Message tLRPC$Message) {
        return (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGeo) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaGeoLive) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaVenue);
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

    public static boolean isMaskMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) != null && isMaskDocument(getMedia(tLRPC$Message).document);
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

    public static boolean isMusicMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage ? isMusicDocument(getMedia(tLRPC$Message).webpage.document) : getMedia(tLRPC$Message) != null && isMusicDocument(getMedia(tLRPC$Message).document);
    }

    public static Boolean isMyPaidReactionAnonymous(TLRPC$MessageReactions tLRPC$MessageReactions) {
        ArrayList arrayList;
        if (tLRPC$MessageReactions == null || (arrayList = tLRPC$MessageReactions.top_reactors) == null) {
            return null;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC$MessageReactor tLRPC$MessageReactor = (TLRPC$MessageReactor) it.next();
            if (tLRPC$MessageReactor != null && tLRPC$MessageReactor.my) {
                return Boolean.valueOf(tLRPC$MessageReactor.anonymous);
            }
        }
        return null;
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

    public static boolean isNewGifMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage ? isNewGifDocument(getMedia(tLRPC$Message).webpage.document) : getMedia(tLRPC$Message) != null && isNewGifDocument(getMedia(tLRPC$Message).document);
    }

    public static boolean isOut(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.out;
    }

    public static boolean isPaidVideo(TLRPC$MessageMedia tLRPC$MessageMedia) {
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) && tLRPC$MessageMedia.extended_media.size() == 1 && isExtendedVideo(tLRPC$MessageMedia.extended_media.get(0));
    }

    public static boolean isPhoto(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$Photo tLRPC$Photo;
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage ? (getMedia(tLRPC$Message).webpage.photo instanceof TLRPC$TL_photo) && !(getMedia(tLRPC$Message).webpage.document instanceof TLRPC$TL_document) : (tLRPC$Message == null || (tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$Photo = tLRPC$MessageAction.photo) == null) ? getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto : tLRPC$Photo instanceof TLRPC$TL_photo;
    }

    public static boolean isPremiumEmojiPack(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
        TLRPC$StickerSet tLRPC$StickerSet;
        if (tLRPC$StickerSetCovered == null || (tLRPC$StickerSet = tLRPC$StickerSetCovered.set) == null || tLRPC$StickerSet.emojis) {
            ArrayList arrayList = tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetFullCovered ? ((TLRPC$TL_stickerSetFullCovered) tLRPC$StickerSetCovered).documents : tLRPC$StickerSetCovered.covers;
            if (tLRPC$StickerSetCovered != null && arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!isFreeEmoji((TLRPC$Document) arrayList.get(i))) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isPremiumEmojiPack(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        TLRPC$StickerSet tLRPC$StickerSet;
        if ((tLRPC$TL_messages_stickerSet == null || (tLRPC$StickerSet = tLRPC$TL_messages_stickerSet.set) == null || tLRPC$StickerSet.emojis) && tLRPC$TL_messages_stickerSet != null && tLRPC$TL_messages_stickerSet.documents != null) {
            for (int i = 0; i < tLRPC$TL_messages_stickerSet.documents.size(); i++) {
                if (!isFreeEmoji((TLRPC$Document) tLRPC$TL_messages_stickerSet.documents.get(i))) {
                    return true;
                }
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

    public static boolean isQuickReply(TLRPC$Message tLRPC$Message) {
        return (tLRPC$Message == null || ((tLRPC$Message.flags & 1073741824) == 0 && tLRPC$Message.quick_reply_shortcut == null)) ? false : true;
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

    public static boolean isRoundVideoMessage(TLRPC$Message tLRPC$Message) {
        return (!(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(tLRPC$Message).webpage == null) ? getMedia(tLRPC$Message) != null && isRoundVideoDocument(getMedia(tLRPC$Message).document) : isRoundVideoDocument(getMedia(tLRPC$Message).webpage.document);
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

    public static boolean isStaticStickerDocument(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && tLRPC$Document.mime_type.equals("image/webp");
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

    public static boolean isStickerMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) != null && isStickerDocument(getMedia(tLRPC$Message).document);
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
            ArrayList arrayList = tLRPC$TL_messages_stickerSet.documents;
            if (arrayList != null && !arrayList.isEmpty()) {
                return isTextColorEmoji((TLRPC$Document) tLRPC$TL_messages_stickerSet.documents.get(0));
            }
        }
        return false;
    }

    public static boolean isTopicActionMessage(MessageObject messageObject) {
        TLRPC$Message tLRPC$Message;
        if (messageObject == null || (tLRPC$Message = messageObject.messageOwner) == null) {
            return false;
        }
        TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
        return (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicCreate) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicEdit);
    }

    public static boolean isUnread(TLRPC$Message tLRPC$Message) {
        return tLRPC$Message.unread;
    }

    public static boolean isVideoDocument(TLRPC$Document tLRPC$Document) {
        int lastIndexOf;
        if (tLRPC$Document == null) {
            return false;
        }
        String str = null;
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
            } else if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeFilename) {
                str = tLRPC$DocumentAttribute.file_name;
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
                    return false;
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

    public static boolean isVideoMessage(TLRPC$Message tLRPC$Message) {
        if (getMedia(tLRPC$Message) == null || !isVideoSticker(getMedia(tLRPC$Message).document)) {
            return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage ? isVideoDocument(getMedia(tLRPC$Message).webpage.document) : getMedia(tLRPC$Message) != null && isVideoDocument(getMedia(tLRPC$Message).document);
        }
        return false;
    }

    public static boolean isVideoSticker(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && isVideoStickerDocument(tLRPC$Document);
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

    public static boolean isVideoWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.startsWith("video/");
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

    public static boolean isVoiceMessage(TLRPC$Message tLRPC$Message) {
        return getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage ? isVoiceDocument(getMedia(tLRPC$Message).webpage.document) : getMedia(tLRPC$Message) != null && isVoiceDocument(getMedia(tLRPC$Message).document);
    }

    public static boolean isVoiceWebDocument(WebFile webFile) {
        return webFile != null && webFile.mime_type.equals("audio/ogg");
    }

    public static boolean isWebM(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && "video/webm".equals(tLRPC$Document.mime_type);
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$handleFoundWords$3(String str, String str2) {
        return str2.length() - str.length();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAnimatedEmojiDocument$0(TLRPC$Document tLRPC$Document) {
        this.emojiAnimatedSticker = tLRPC$Document;
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.animatedEmojiDocumentLoaded, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAnimatedEmojiDocument$1(final TLRPC$Document tLRPC$Document) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                MessageObject.this.lambda$loadAnimatedEmojiDocument$0(tLRPC$Document);
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
        if (i2 >= 24) {
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
        return new StaticLayout(charSequence, textPaint, i, Layout.Alignment.ALIGN_NORMAL, f, f2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needDrawAvatarInternal() {
        TLRPC$Chat chat;
        if (this.isRepostPreview || this.isSaved || this.forceAvatar || this.customAvatarDrawable != null || this.searchType != 0) {
            return true;
        }
        boolean z = getDialogId() < 0 && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()))) != null && chat.signature_profiles;
        if (!isSponsored()) {
            if ((isFromChat() && isFromUser()) || isFromGroup() || z || this.eventId != 0) {
                return true;
            }
            TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
            if (tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.saved_from_peer != null) {
                return true;
            }
        }
        return false;
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

    public static CharSequence peerNameWithIcon(int i, long j) {
        return peerNameWithIcon(i, j, false);
    }

    public static CharSequence peerNameWithIcon(int i, long j, boolean z) {
        int i2 = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        MessagesController messagesController = MessagesController.getInstance(i);
        if (i2 >= 0) {
            TLRPC$User user = messagesController.getUser(Long.valueOf(j));
            return user != null ? AndroidUtilities.removeDiacritics(UserObject.getUserName(user)) : "";
        }
        TLRPC$Chat chat = messagesController.getChat(Long.valueOf(-j));
        if (chat != null) {
            return new SpannableStringBuilder(ChatObject.isChannelAndNotMegaGroup(chat) ? channelSpan() : groupSpan()).append((CharSequence) " ").append((CharSequence) AndroidUtilities.removeDiacritics(chat.title));
        }
        return "";
    }

    public static CharSequence peerNameWithIcon(int i, TLRPC$Peer tLRPC$Peer) {
        return peerNameWithIcon(i, tLRPC$Peer, !(tLRPC$Peer instanceof TLRPC$TL_peerUser));
    }

    public static CharSequence peerNameWithIcon(int i, TLRPC$Peer tLRPC$Peer, boolean z) {
        TLRPC$Chat chat;
        SpannableStringBuilder spannableStringBuilder;
        SpannableStringBuilder append;
        String str;
        if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
            TLRPC$User user = MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$Peer.user_id));
            if (user == null) {
                return "";
            }
            if (!z) {
                return UserObject.getUserName(user);
            }
            append = new SpannableStringBuilder(userSpan()).append((CharSequence) " ");
            str = UserObject.getUserName(user);
        } else {
            if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Peer.chat_id));
                if (chat == null) {
                    return "";
                }
                if (!z) {
                    return chat.title;
                }
                spannableStringBuilder = new SpannableStringBuilder(ChatObject.isChannelAndNotMegaGroup(chat) ? channelSpan() : groupSpan());
            } else if (!(tLRPC$Peer instanceof TLRPC$TL_peerChannel) || (chat = MessagesController.getInstance(i).getChat(Long.valueOf(tLRPC$Peer.channel_id))) == null) {
                return "";
            } else {
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

    public static boolean peersEqual(TLRPC$Chat tLRPC$Chat, TLRPC$Peer tLRPC$Peer) {
        if (tLRPC$Chat == null && tLRPC$Peer == null) {
            return true;
        }
        if (tLRPC$Chat != null && tLRPC$Peer != null) {
            return (ChatObject.isChannel(tLRPC$Chat) && (tLRPC$Peer instanceof TLRPC$TL_peerChannel)) ? tLRPC$Chat.id == tLRPC$Peer.channel_id : !ChatObject.isChannel(tLRPC$Chat) && (tLRPC$Peer instanceof TLRPC$TL_peerChat) && tLRPC$Chat.id == tLRPC$Peer.chat_id;
        }
        return false;
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

    public static Spannable replaceAnimatedEmoji(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, Paint.FontMetricsInt fontMetricsInt) {
        return replaceAnimatedEmoji(charSequence, arrayList, fontMetricsInt, false);
    }

    public static Spannable replaceAnimatedEmoji(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, Paint.FontMetricsInt fontMetricsInt, boolean z) {
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
                        for (AnimatedEmojiSpan animatedEmojiSpan : animatedEmojiSpanArr) {
                            spannableString.removeSpan(animatedEmojiSpan);
                        }
                    }
                    AnimatedEmojiSpan animatedEmojiSpan2 = tLRPC$TL_messageEntityCustomEmoji.document != null ? new AnimatedEmojiSpan(tLRPC$TL_messageEntityCustomEmoji.document, fontMetricsInt) : new AnimatedEmojiSpan(tLRPC$TL_messageEntityCustomEmoji.document_id, fontMetricsInt);
                    animatedEmojiSpan2.top = z;
                    int i5 = tLRPC$MessageEntity.offset;
                    spannableString.setSpan(animatedEmojiSpan2, i5, tLRPC$MessageEntity.length + i5, 33);
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
        TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported;
        int indexOf = TextUtils.indexOf(charSequence, str);
        if (indexOf >= 0) {
            if (tLObject instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                charSequence2 = UserObject.getUserName(tLRPC$User).replace('\n', ' ');
                sb = new StringBuilder();
                sb.append("");
                j = tLRPC$User.id;
            } else if (!(tLObject instanceof TLRPC$Chat)) {
                if (!(tLObject instanceof TLRPC$TL_game)) {
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
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{charSequence2}));
                    URLSpanNoUnderlineBold uRLSpanNoUnderlineBold = new URLSpanNoUnderlineBold("" + str3);
                    uRLSpanNoUnderlineBold.setObject(tLRPC$TL_chatInviteExported);
                    spannableStringBuilder.setSpan(uRLSpanNoUnderlineBold, indexOf, charSequence2.length() + indexOf, 33);
                    return spannableStringBuilder;
                }
                charSequence2 = ((TLRPC$TL_game) tLObject).title.replace('\n', ' ');
                str2 = "game";
                str3 = str2;
                tLRPC$TL_chatInviteExported = null;
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{charSequence2}));
                URLSpanNoUnderlineBold uRLSpanNoUnderlineBold2 = new URLSpanNoUnderlineBold("" + str3);
                uRLSpanNoUnderlineBold2.setObject(tLRPC$TL_chatInviteExported);
                spannableStringBuilder2.setSpan(uRLSpanNoUnderlineBold2, indexOf, charSequence2.length() + indexOf, 33);
                return spannableStringBuilder2;
            } else {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) tLObject;
                charSequence2 = tLRPC$Chat.title.replace('\n', ' ');
                sb = new StringBuilder();
                sb.append("");
                j = -tLRPC$Chat.id;
            }
            sb.append(j);
            str2 = sb.toString();
            str3 = str2;
            tLRPC$TL_chatInviteExported = null;
            SpannableStringBuilder spannableStringBuilder22 = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{charSequence2}));
            URLSpanNoUnderlineBold uRLSpanNoUnderlineBold22 = new URLSpanNoUnderlineBold("" + str3);
            uRLSpanNoUnderlineBold22.setObject(tLRPC$TL_chatInviteExported);
            spannableStringBuilder22.setSpan(uRLSpanNoUnderlineBold22, indexOf, charSequence2.length() + indexOf, 33);
            return spannableStringBuilder22;
        }
        return charSequence;
    }

    public static void setUnreadFlags(TLRPC$Message tLRPC$Message, int i) {
        tLRPC$Message.unread = (i & 1) == 0;
        tLRPC$Message.media_unread = (i & 2) == 0;
    }

    public static boolean shouldEncryptPhotoOrVideo(int i, TLRPC$Message tLRPC$Message) {
        int i2;
        if ((tLRPC$Message == null || tLRPC$Message.media == null || !((isVoiceDocument(getDocument(tLRPC$Message)) || isRoundVideoMessage(tLRPC$Message)) && tLRPC$Message.media.ttl_seconds == Integer.MAX_VALUE)) && !(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPaidMedia)) {
            return tLRPC$Message instanceof TLRPC$TL_message_secret ? ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isVideoMessage(tLRPC$Message)) && (i2 = tLRPC$Message.ttl) > 0 && i2 <= 60 : ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaDocument)) && getMedia(tLRPC$Message).ttl_seconds != 0;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x00b7, code lost:
        if (org.telegram.messenger.ChatObject.isChannelAndNotMegaGroup(r10) != false) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b9, code lost:
        r0 = org.telegram.messenger.R.string.ActionSetWallpaperForThisChannel;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00bc, code lost:
        r0 = org.telegram.messenger.R.string.ActionSetWallpaperForThisGroup;
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x0c40, code lost:
        if (isOut() != false) goto L611;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x0caa, code lost:
        if (isOut() != false) goto L611;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x0cac, code lost:
        r0 = org.telegram.messenger.R.string.ActionTakeScreenshootYou;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x0caf, code lost:
        r0 = org.telegram.messenger.R.string.ActionTakeScreenshoot;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01bd, code lost:
        if (org.telegram.messenger.ChatObject.isChannelAndNotMegaGroup(r10) != false) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:953:0x13c1, code lost:
        if ((((org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) r4).flags & 4) != 0) goto L953;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1071:0x15c0  */
    /* JADX WARN: Removed duplicated region for block: B:1087:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0597  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x05b4  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x05fb  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0609  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0629  */
    /* JADX WARN: Removed duplicated region for block: B:331:0x067a  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x06a0  */
    /* JADX WARN: Removed duplicated region for block: B:407:0x0836  */
    /* JADX WARN: Removed duplicated region for block: B:411:0x0844  */
    /* JADX WARN: Removed duplicated region for block: B:433:0x088e  */
    /* JADX WARN: Removed duplicated region for block: B:434:0x0891  */
    /* JADX WARN: Removed duplicated region for block: B:451:0x08ce  */
    /* JADX WARN: Removed duplicated region for block: B:452:0x08d2  */
    /* JADX WARN: Removed duplicated region for block: B:540:0x0aa0  */
    /* JADX WARN: Removed duplicated region for block: B:561:0x0b34  */
    /* JADX WARN: Removed duplicated region for block: B:887:0x1235  */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v173, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r2v19, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r2v39 */
    /* JADX WARN: Type inference failed for: r2v57 */
    /* JADX WARN: Type inference failed for: r2v64 */
    /* JADX WARN: Type inference failed for: r2v75 */
    /* JADX WARN: Type inference failed for: r9v55 */
    /* JADX WARN: Type inference failed for: r9v56 */
    /* JADX WARN: Type inference failed for: r9v57 */
    /* JADX WARN: Type inference failed for: r9v58 */
    /* JADX WARN: Type inference failed for: r9v59 */
    /* JADX WARN: Type inference failed for: r9v60, types: [org.telegram.tgnet.TLRPC$Chat] */
    /* JADX WARN: Type inference failed for: r9v61, types: [org.telegram.tgnet.TLRPC$User] */
    /* JADX WARN: Type inference failed for: r9v63, types: [org.telegram.tgnet.TLObject] */
    /* JADX WARN: Type inference failed for: r9v65, types: [org.telegram.tgnet.TLObject] */
    /* JADX WARN: Type inference failed for: r9v67, types: [org.telegram.tgnet.TLObject] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateMessageText(AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray longSparseArray, LongSparseArray longSparseArray2) {
        TLRPC$User tLRPC$User;
        TLRPC$Chat chat;
        TLRPC$Message tLRPC$Message;
        String str;
        Pattern pattern;
        String str2;
        int i;
        char c;
        String formatPluralString;
        ?? r2;
        String publicUsername;
        String str3;
        CharSequence charSequence;
        int indexOf;
        SpannableStringBuilder valueOf;
        String formatCurrency;
        StringBuilder sb;
        String format;
        String str4;
        String str5;
        int i2;
        String str6;
        int i3;
        String publicUsername2;
        String str7;
        String string;
        CharSequence charSequence2;
        int i4;
        int i5;
        CharSequence string2;
        TLRPC$Chat tLRPC$Chat;
        String sb2;
        TLRPC$Chat tLRPC$Chat2;
        long j;
        CharSequence formatString;
        CharSequence replaceCharSequence;
        TLObject tLObject;
        String str8;
        String str9;
        Object obj;
        long j2;
        TLRPC$Chat tLRPC$Chat3;
        TLRPC$Chat tLRPC$Chat4;
        int i6;
        TLRPC$Chat tLRPC$Chat5;
        ArrayList arrayList;
        TLObject chat2;
        CharSequence replaceTags;
        TLRPC$Chat tLRPC$Chat6;
        TLRPC$TL_messageActionGiveawayResults tLRPC$TL_messageActionGiveawayResults;
        String formatPluralString2;
        TLRPC$Chat tLRPC$Chat7;
        String str10;
        boolean z;
        TLRPC$Chat tLRPC$Chat8;
        int i7;
        int i8;
        int i9;
        int i10;
        TLRPC$TL_messageActionPaymentRefunded tLRPC$TL_messageActionPaymentRefunded;
        CharSequence charSequence3;
        String formatPluralString3;
        int i11;
        CharSequence charSequence4;
        TLRPC$Peer tLRPC$Peer = this.messageOwner.from_id;
        if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
            tLRPC$User = getUser(abstractMap, longSparseArray, tLRPC$Peer.user_id);
        } else if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
            chat = getChat(abstractMap2, longSparseArray2, tLRPC$Peer.channel_id);
            tLRPC$User = null;
            TLObject tLObject2 = tLRPC$User == null ? tLRPC$User : chat;
            this.drawServiceWithDefaultTypeface = false;
            this.channelJoined = false;
            tLRPC$Message = this.messageOwner;
            if (tLRPC$Message instanceof TLRPC$TL_messageService) {
                str = "";
                TLRPC$Chat tLRPC$Chat9 = chat;
                this.isRestrictedMessage = false;
                String restrictionReason = MessagesController.getInstance(this.currentAccount).getRestrictionReason(this.messageOwner.restriction_reason);
                if (!TextUtils.isEmpty(restrictionReason)) {
                    this.messageText = restrictionReason;
                    this.isRestrictedMessage = true;
                    str3 = str;
                } else if (isMediaEmpty() || isSponsored()) {
                    String str11 = this.messageOwner.message;
                    charSequence = str11;
                    if (str11 != null) {
                        try {
                            if (str11.length() > 200) {
                                pattern = AndroidUtilities.BAD_CHARS_MESSAGE_LONG_PATTERN;
                                str2 = this.messageOwner.message;
                            } else {
                                pattern = AndroidUtilities.BAD_CHARS_MESSAGE_PATTERN;
                                str2 = this.messageOwner.message;
                            }
                            this.messageText = pattern.matcher(str2).replaceAll("\u200c");
                            str3 = str;
                        } catch (Throwable unused) {
                        }
                    }
                    r2 = charSequence;
                } else {
                    if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveaway) {
                        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                        if (tLRPC$MessageFwdHeader != null) {
                            TLRPC$Peer tLRPC$Peer2 = tLRPC$MessageFwdHeader.from_id;
                            if (tLRPC$Peer2 instanceof TLRPC$TL_peerChannel) {
                                tLRPC$Chat9 = getChat(abstractMap2, longSparseArray2, tLRPC$Peer2.channel_id);
                            }
                        }
                        if (ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat9)) {
                            i = R.string.BoostingGiveawayChannelStarted;
                            str = str;
                        } else {
                            i = R.string.BoostingGiveawayGroupStarted;
                            str = str;
                        }
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveawayResults) {
                        i = R.string.BoostingGiveawayResults;
                        str = str;
                    } else {
                        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaStory) {
                            if (getMedia(this.messageOwner).via_mention) {
                                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getMedia(this.messageOwner).user_id));
                                String str12 = (user == null || (publicUsername = UserObject.getPublicUsername(user)) == null) ? null : MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername + "/s/" + getMedia(this.messageOwner).id;
                                if (str12 != null) {
                                    SpannableString spannableString = new SpannableString(str12);
                                    this.messageText = spannableString;
                                    spannableString.setSpan(new URLSpanReplacement("https://" + str12, new TextStyleSpan.TextStyleRun()), 0, this.messageText.length(), 33);
                                    str3 = str;
                                } else {
                                    r2 = str;
                                }
                            } else {
                                i = R.string.ForwardedStory;
                                str = str;
                            }
                        } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDice) {
                            charSequence = getDiceEmoji();
                        } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPoll) {
                            if (((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.quiz) {
                                i = R.string.QuizPoll;
                                str = str;
                            } else {
                                i = R.string.Poll;
                                str = str;
                            }
                        } else if (isVoiceOnce()) {
                            i = R.string.AttachOnceAudio;
                            str = str;
                        } else if (isRoundOnce()) {
                            i = R.string.AttachOnceRound;
                            str = str;
                        } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPaidMedia) {
                            TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) getMedia(this.messageOwner);
                            int size = tLRPC$TL_messageMediaPaidMedia.extended_media.size();
                            boolean z2 = false;
                            for (int i12 = 0; i12 < size; i12++) {
                                TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$TL_messageMediaPaidMedia.extended_media.get(i12);
                                if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                                    TLRPC$MessageMedia tLRPC$MessageMedia = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media;
                                    z2 = (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && isVideoDocument(tLRPC$MessageMedia.document);
                                } else if (!(tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview)) {
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
                        } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                            if (getMedia(this.messageOwner).ttl_seconds != 0 && !(this.messageOwner instanceof TLRPC$TL_message_secret)) {
                                i = R.string.AttachDestructingPhoto;
                                str = str;
                            } else if (getGroupId() != 0) {
                                i = R.string.Album;
                                str = str;
                            } else {
                                i = R.string.AttachPhoto;
                                str = str;
                            }
                        } else if (isVideo() || ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) && (((getDocument() instanceof TLRPC$TL_documentEmpty) || getDocument() == null) && getMedia(this.messageOwner).ttl_seconds != 0))) {
                            if (getMedia(this.messageOwner).ttl_seconds != 0) {
                                TLRPC$Message tLRPC$Message2 = this.messageOwner;
                                if (!(tLRPC$Message2 instanceof TLRPC$TL_message_secret)) {
                                    if (getMedia(tLRPC$Message2).voice) {
                                        i = R.string.AttachVoiceExpired;
                                        str = str;
                                    } else if (getMedia(this.messageOwner).round) {
                                        i = R.string.AttachRoundExpired;
                                        str = str;
                                    } else {
                                        i = R.string.AttachDestructingVideo;
                                        str = str;
                                    }
                                }
                            }
                            i = R.string.AttachVideo;
                            str = str;
                        } else if (isVoice()) {
                            i = R.string.AttachAudio;
                            str = str;
                        } else if (isRoundVideo()) {
                            i = R.string.AttachRound;
                            str = str;
                        } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeo) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaVenue)) {
                            i = R.string.AttachLocation;
                            str = str;
                        } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeoLive) {
                            i = R.string.AttachLiveLocation;
                            str = str;
                        } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaContact) {
                            this.messageText = LocaleController.getString(R.string.AttachContact);
                            str3 = str;
                            if (!TextUtils.isEmpty(getMedia(this.messageOwner).vcard)) {
                                this.vCardData = VCardData.parse(getMedia(this.messageOwner).vcard);
                                str3 = str;
                            }
                        } else {
                            if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame)) {
                                if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice) {
                                    charSequence = getMedia(this.messageOwner).description;
                                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaUnsupported) {
                                    i = R.string.UnsupportedMedia2;
                                    str = str;
                                } else {
                                    str3 = str;
                                    if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) {
                                        if (isSticker() || isAnimatedStickerDocument(getDocument(), true)) {
                                            String stickerChar = getStickerChar();
                                            if (stickerChar == null || stickerChar.length() <= 0) {
                                                i = R.string.AttachSticker;
                                                str = str;
                                            } else {
                                                charSequence = String.format("%s %s", stickerChar, LocaleController.getString(R.string.AttachSticker));
                                            }
                                        } else if (isMusic()) {
                                            i = R.string.AttachMusic;
                                            str = str;
                                        } else if (isGif()) {
                                            i = R.string.AttachGif;
                                            str = str;
                                        } else {
                                            CharSequence documentFileName = FileLoader.getDocumentFileName(getDocument());
                                            boolean isEmpty = TextUtils.isEmpty(documentFileName);
                                            charSequence = documentFileName;
                                            if (isEmpty) {
                                                i = R.string.AttachDocument;
                                                str = str;
                                            }
                                        }
                                    }
                                }
                            }
                            charSequence = this.messageOwner.message;
                        }
                        r2 = charSequence;
                    }
                    charSequence = LocaleController.getString(i);
                    r2 = charSequence;
                }
                if (this.messageText == null) {
                }
            } else {
                TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                if (tLRPC$MessageAction != null) {
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetSameChatWallPaper)) {
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                            this.contentType = 1;
                            TLRPC$TL_messageActionSetChatWallPaper tLRPC$TL_messageActionSetChatWallPaper = (TLRPC$TL_messageActionSetChatWallPaper) tLRPC$MessageAction;
                            this.type = 22;
                            ArrayList<TLRPC$PhotoSize> arrayList2 = new ArrayList<>();
                            this.photoThumbs = arrayList2;
                            TLRPC$Document tLRPC$Document = tLRPC$TL_messageActionSetChatWallPaper.wallpaper.document;
                            if (tLRPC$Document != null) {
                                arrayList2.addAll(tLRPC$Document.thumbs);
                                this.photoThumbsObject = tLRPC$TL_messageActionSetChatWallPaper.wallpaper.document;
                            }
                            TLRPC$User user2 = getUser(abstractMap, longSparseArray, isOutOwner() ? 0L : getDialogId());
                            TLRPC$User user3 = getUser(abstractMap, longSparseArray, getDialogId());
                            if (user2 != null) {
                                if (user2.id != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                    SpannableString spannableString2 = new SpannableString(UserObject.getFirstName(user2));
                                    spannableString2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString2.length(), 33);
                                    if (tLRPC$TL_messageActionSetChatWallPaper.same) {
                                        this.type = 10;
                                        i11 = R.string.ActionSetSameWallpaperForThisChat;
                                    } else {
                                        i11 = tLRPC$TL_messageActionSetChatWallPaper.for_both ? R.string.ActionSetWallpaperForThisChatBoth : R.string.ActionSetWallpaperForThisChat;
                                    }
                                    this.messageText = LocaleController.getString(i11);
                                    charSequence4 = AndroidUtilities.replaceCharSequence("%s", this.messageText, spannableString2);
                                } else if (tLRPC$TL_messageActionSetChatWallPaper.same) {
                                    this.type = 10;
                                    charSequence4 = LocaleController.formatString(R.string.ActionSetSameWallpaperForThisChatSelf, new Object[0]);
                                } else if (!tLRPC$TL_messageActionSetChatWallPaper.for_both || user3 == null) {
                                    i = R.string.ActionSetWallpaperForThisChatSelf;
                                    str = "";
                                    charSequence = LocaleController.getString(i);
                                    r2 = charSequence;
                                } else {
                                    this.messageText = LocaleController.getString(R.string.ActionSetWallpaperForThisChatSelfBoth);
                                    SpannableString spannableString3 = new SpannableString(UserObject.getFirstName(user3));
                                    spannableString3.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString3.length(), 33);
                                    charSequence4 = AndroidUtilities.replaceCharSequence("%s", this.messageText, spannableString3);
                                }
                            } else if (chat == null) {
                                if (tLRPC$User != null) {
                                    charSequence4 = LocaleController.formatString(R.string.ActionSetWallpaperForThisGroupByUser, UserObject.getFirstName(tLRPC$User));
                                }
                            }
                            str = "";
                            charSequence = charSequence4;
                            r2 = charSequence;
                        } else {
                            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGroupCallScheduled) {
                                TLRPC$TL_messageActionGroupCallScheduled tLRPC$TL_messageActionGroupCallScheduled = (TLRPC$TL_messageActionGroupCallScheduled) tLRPC$MessageAction;
                                charSequence4 = ((tLRPC$Message.peer_id instanceof TLRPC$TL_peerChat) || isSupergroup()) ? LocaleController.formatString(R.string.ActionGroupCallScheduled, LocaleController.formatStartsTime(tLRPC$TL_messageActionGroupCallScheduled.schedule_date, 3, false)) : LocaleController.formatString(R.string.ActionChannelCallScheduled, LocaleController.formatStartsTime(tLRPC$TL_messageActionGroupCallScheduled.schedule_date, 3, false));
                            } else {
                                String str13 = "un1";
                                if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGroupCall) {
                                    int i14 = tLRPC$MessageAction.duration;
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
                                        if (!(this.messageOwner.peer_id instanceof TLRPC$TL_peerChat) && !isSupergroup()) {
                                            charSequence4 = LocaleController.formatString(R.string.ActionChannelCallEnded, formatPluralString3);
                                        } else if (isOut()) {
                                            charSequence4 = LocaleController.formatString(R.string.ActionGroupCallEndedByYou, formatPluralString3);
                                        } else {
                                            formatString = LocaleController.formatString(R.string.ActionGroupCallEndedBy, formatPluralString3);
                                            str4 = "un1";
                                            str5 = "";
                                            str13 = str4;
                                            tLObject = tLObject2;
                                            charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                            str = str5;
                                            charSequence = charSequence3;
                                        }
                                    } else {
                                        if (!(tLRPC$Message.peer_id instanceof TLRPC$TL_peerChat) && !isSupergroup()) {
                                            i = R.string.ActionChannelCallJustStarted;
                                        } else if (isOut()) {
                                            i = R.string.ActionGroupCallStartedByYou;
                                        } else {
                                            i2 = R.string.ActionGroupCallStarted;
                                            str4 = "un1";
                                            str5 = "";
                                            formatString = LocaleController.getString(i2);
                                            str13 = str4;
                                            tLObject = tLObject2;
                                            charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                            str = str5;
                                            charSequence = charSequence3;
                                        }
                                        str = "";
                                        charSequence = LocaleController.getString(i);
                                    }
                                } else {
                                    TLRPC$Chat tLRPC$Chat10 = chat;
                                    String str14 = "un2";
                                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionInviteToGroupCall) {
                                        long j3 = tLRPC$MessageAction.user_id;
                                        if (j3 == 0 && tLRPC$MessageAction.users.size() == 1) {
                                            j3 = ((Long) this.messageOwner.action.users.get(0)).longValue();
                                        }
                                        if (j3 != 0) {
                                            tLObject = getUser(abstractMap, longSparseArray, j3);
                                            if (isOut()) {
                                                i10 = R.string.ActionGroupCallYouInvited;
                                                formatString = LocaleController.getString(i10);
                                                str13 = "un2";
                                                str5 = "";
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            } else if (j3 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                i2 = R.string.ActionGroupCallInvitedYou;
                                                str4 = "un1";
                                                str5 = "";
                                                formatString = LocaleController.getString(i2);
                                                str13 = str4;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            } else {
                                                i9 = R.string.ActionGroupCallInvited;
                                                formatString = replaceWithLink(LocaleController.getString(i9), "un2", tLObject);
                                                this.messageText = formatString;
                                                str4 = "un1";
                                                str5 = "";
                                                str13 = str4;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            }
                                        } else if (isOut()) {
                                            i8 = R.string.ActionGroupCallYouInvited;
                                            charSequence4 = replaceWithLink(LocaleController.getString(i8), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                        } else {
                                            i7 = R.string.ActionGroupCallInvited;
                                            formatString = replaceWithLink(LocaleController.getString(i7), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                            this.messageText = formatString;
                                            str4 = "un1";
                                            str5 = "";
                                            str13 = str4;
                                            tLObject = tLObject2;
                                            charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                            str = str5;
                                            charSequence = charSequence3;
                                        }
                                    } else {
                                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                                            TLRPC$TL_messageActionGeoProximityReached tLRPC$TL_messageActionGeoProximityReached = (TLRPC$TL_messageActionGeoProximityReached) tLRPC$MessageAction;
                                            long peerId = getPeerId(tLRPC$TL_messageActionGeoProximityReached.from_id);
                                            TLObject user4 = peerId > 0 ? getUser(abstractMap, longSparseArray, peerId) : getChat(abstractMap2, longSparseArray2, -peerId);
                                            long peerId2 = getPeerId(tLRPC$TL_messageActionGeoProximityReached.to_id);
                                            long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                            if (peerId2 == clientUserId) {
                                                formatString = LocaleController.formatString(R.string.ActionUserWithinRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2));
                                            } else {
                                                chat2 = peerId2 > 0 ? getUser(abstractMap, longSparseArray, peerId2) : getChat(abstractMap2, longSparseArray2, -peerId2);
                                                if (peerId == clientUserId) {
                                                    replaceTags = LocaleController.formatString(R.string.ActionUserWithinYouRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2));
                                                } else {
                                                    formatString = replaceWithLink(LocaleController.formatString(R.string.ActionUserWithinOtherRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un2", chat2);
                                                    this.messageText = formatString;
                                                }
                                            }
                                            tLObject = user4;
                                            str5 = "";
                                            charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                            str = str5;
                                            charSequence = charSequence3;
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCustomAction) {
                                            charSequence4 = tLRPC$MessageAction.message;
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatCreate) {
                                            if (isOut()) {
                                                i = R.string.ActionYouCreateGroup;
                                                str = "";
                                                charSequence = LocaleController.getString(i);
                                            } else {
                                                i2 = R.string.ActionCreateGroup;
                                                str4 = "un1";
                                                str5 = "";
                                                formatString = LocaleController.getString(i2);
                                                str13 = str4;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            }
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatDeleteUser) {
                                            if (isFromUser()) {
                                                TLRPC$Message tLRPC$Message3 = this.messageOwner;
                                                if (tLRPC$Message3.action.user_id == tLRPC$Message3.from_id.user_id) {
                                                    if (isOut()) {
                                                        i = R.string.ActionYouLeftUser;
                                                        str = "";
                                                        charSequence = LocaleController.getString(i);
                                                    } else {
                                                        i2 = R.string.ActionLeftUser;
                                                        str4 = "un1";
                                                        str5 = "";
                                                        formatString = LocaleController.getString(i2);
                                                        str13 = str4;
                                                        tLObject = tLObject2;
                                                        charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                        str = str5;
                                                        charSequence = charSequence3;
                                                    }
                                                }
                                            }
                                            tLObject = getUser(abstractMap, longSparseArray, this.messageOwner.action.user_id);
                                            if (isOut()) {
                                                i10 = R.string.ActionYouKickUser;
                                                formatString = LocaleController.getString(i10);
                                                str13 = "un2";
                                                str5 = "";
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            } else if (this.messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                i2 = R.string.ActionKickUserYou;
                                                str4 = "un1";
                                                str5 = "";
                                                formatString = LocaleController.getString(i2);
                                                str13 = str4;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            } else {
                                                i9 = R.string.ActionKickUser;
                                                formatString = replaceWithLink(LocaleController.getString(i9), "un2", tLObject);
                                                this.messageText = formatString;
                                                str4 = "un1";
                                                str5 = "";
                                                str13 = str4;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            }
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentRefunded) {
                                            long peerDialogId = DialogObject.getPeerDialogId(((TLRPC$TL_messageActionPaymentRefunded) tLRPC$MessageAction).peer);
                                            charSequence4 = StarsIntroActivity.replaceStars(replaceWithLink(LocaleController.formatString(R.string.ActionRefunded, tLRPC$TL_messageActionPaymentRefunded.currency + " " + LocaleController.formatNumber(tLRPC$TL_messageActionPaymentRefunded.total_amount, ',')), "un1", peerDialogId >= 0 ? getUser(abstractMap, longSparseArray, peerDialogId) : getChat(abstractMap2, longSparseArray2, -peerDialogId)));
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatAddUser) {
                                            long j4 = tLRPC$MessageAction.user_id;
                                            if (j4 == 0 && tLRPC$MessageAction.users.size() == 1) {
                                                j4 = ((Long) this.messageOwner.action.users.get(0)).longValue();
                                            }
                                            if (j4 != 0) {
                                                tLObject = getUser(abstractMap, longSparseArray, j4);
                                                long j5 = this.messageOwner.peer_id.channel_id;
                                                TLRPC$Chat chat3 = j5 != 0 ? getChat(abstractMap2, longSparseArray2, j5) : null;
                                                TLRPC$Peer tLRPC$Peer3 = this.messageOwner.from_id;
                                                if (tLRPC$Peer3 != null && j4 == tLRPC$Peer3.user_id) {
                                                    if (ChatObject.isChannel(chat3) && !chat3.megagroup) {
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
                                                    str = "";
                                                    charSequence = LocaleController.getString(i);
                                                } else if (isOut()) {
                                                    i10 = R.string.ActionYouAddUser;
                                                    formatString = LocaleController.getString(i10);
                                                    str13 = "un2";
                                                    str5 = "";
                                                    charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                    str = str5;
                                                    charSequence = charSequence3;
                                                } else if (j4 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                    i2 = this.messageOwner.peer_id.channel_id != 0 ? (chat3 == null || !chat3.megagroup) ? R.string.ChannelAddedBy : R.string.MegaAddedBy : R.string.ActionAddUserYou;
                                                } else {
                                                    i9 = R.string.ActionAddUser;
                                                    formatString = replaceWithLink(LocaleController.getString(i9), "un2", tLObject);
                                                    this.messageText = formatString;
                                                    str4 = "un1";
                                                    str5 = "";
                                                    str13 = str4;
                                                    tLObject = tLObject2;
                                                    charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                    str = str5;
                                                    charSequence = charSequence3;
                                                }
                                                str4 = "un1";
                                                str5 = "";
                                                formatString = LocaleController.getString(i2);
                                                str13 = str4;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            } else if (isOut()) {
                                                i8 = R.string.ActionYouAddUser;
                                                charSequence4 = replaceWithLink(LocaleController.getString(i8), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                            } else {
                                                i7 = R.string.ActionAddUser;
                                                formatString = replaceWithLink(LocaleController.getString(i7), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                                this.messageText = formatString;
                                                str4 = "un1";
                                                str5 = "";
                                                str13 = str4;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            }
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatJoinedByLink) {
                                            if (isOut()) {
                                                i = R.string.ActionInviteYou;
                                                str = "";
                                                charSequence = LocaleController.getString(i);
                                            } else {
                                                i2 = R.string.ActionInviteUser;
                                                str4 = "un1";
                                                str5 = "";
                                                formatString = LocaleController.getString(i2);
                                                str13 = str4;
                                                tLObject = tLObject2;
                                                charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                str = str5;
                                                charSequence = charSequence3;
                                            }
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiveawayLaunch) {
                                            TLRPC$TL_messageActionGiveawayLaunch tLRPC$TL_messageActionGiveawayLaunch = (TLRPC$TL_messageActionGiveawayLaunch) tLRPC$MessageAction;
                                            TLRPC$Peer tLRPC$Peer4 = tLRPC$Message.peer_id;
                                            if (tLRPC$Peer4 != null) {
                                                long j6 = tLRPC$Peer4.channel_id;
                                                if (j6 != 0) {
                                                    tLRPC$Chat8 = getChat(abstractMap2, longSparseArray2, j6);
                                                    boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat8);
                                                    charSequence4 = (tLRPC$TL_messageActionGiveawayLaunch.flags & 1) == 0 ? LocaleController.formatPluralStringComma(isChannelAndNotMegaGroup ? "BoostingStarsGiveawayJustStarted" : "BoostingStarsGiveawayJustStartedGroup", (int) tLRPC$TL_messageActionGiveawayLaunch.stars, tLRPC$Chat8 != null ? tLRPC$Chat8.title : "") : LocaleController.formatString(isChannelAndNotMegaGroup ? R.string.BoostingGiveawayJustStarted : R.string.BoostingGiveawayJustStartedGroup, tLRPC$Chat8 != null ? tLRPC$Chat8.title : "");
                                                }
                                            }
                                            tLRPC$Chat8 = null;
                                            boolean isChannelAndNotMegaGroup2 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat8);
                                            if ((tLRPC$TL_messageActionGiveawayLaunch.flags & 1) == 0) {
                                            }
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionBoostApply) {
                                            TLRPC$Peer tLRPC$Peer5 = tLRPC$Message.peer_id;
                                            if (tLRPC$Peer5 != null) {
                                                long j7 = tLRPC$Peer5.channel_id;
                                                if (j7 != 0) {
                                                    tLRPC$Chat7 = getChat(abstractMap2, longSparseArray2, j7);
                                                    boolean isChannelAndNotMegaGroup3 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat7);
                                                    TLRPC$TL_messageActionBoostApply tLRPC$TL_messageActionBoostApply = (TLRPC$TL_messageActionBoostApply) this.messageOwner.action;
                                                    if (tLObject2 instanceof TLRPC$User) {
                                                        str10 = tLObject2 instanceof TLRPC$Chat ? ((TLRPC$Chat) tLObject2).title : "";
                                                        z = false;
                                                    } else {
                                                        TLRPC$User tLRPC$User2 = (TLRPC$User) tLObject2;
                                                        z = UserObject.isUserSelf(tLRPC$User2);
                                                        str10 = UserObject.getFirstName(tLRPC$User2);
                                                    }
                                                    if (z) {
                                                        int i18 = tLRPC$TL_messageActionBoostApply.boosts;
                                                        charSequence4 = i18 <= 1 ? LocaleController.formatString(isChannelAndNotMegaGroup3 ? R.string.BoostingBoostsChannelByUserServiceMsg : R.string.BoostingBoostsGroupByUserServiceMsg, str10) : LocaleController.formatPluralString(isChannelAndNotMegaGroup3 ? "BoostingBoostsChannelByUserServiceMsgCount" : "BoostingBoostsGroupByUserServiceMsgCount", i18, str10);
                                                    } else {
                                                        int i19 = tLRPC$TL_messageActionBoostApply.boosts;
                                                        if (i19 <= 1) {
                                                            int i20 = isChannelAndNotMegaGroup3 ? R.string.BoostingBoostsChannelByYouServiceMsg : R.string.BoostingBoostsGroupByYouServiceMsg;
                                                            charSequence4 = LocaleController.getString(i20);
                                                        } else {
                                                            charSequence4 = LocaleController.formatPluralString(isChannelAndNotMegaGroup3 ? "BoostingBoostsChannelByYouServiceMsgCount" : "BoostingBoostsGroupByYouServiceMsgCount", i19, new Object[0]);
                                                        }
                                                    }
                                                }
                                            }
                                            tLRPC$Chat7 = null;
                                            boolean isChannelAndNotMegaGroup32 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat7);
                                            TLRPC$TL_messageActionBoostApply tLRPC$TL_messageActionBoostApply2 = (TLRPC$TL_messageActionBoostApply) this.messageOwner.action;
                                            if (tLObject2 instanceof TLRPC$User) {
                                            }
                                            if (z) {
                                            }
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiveawayResults) {
                                            TLRPC$Peer tLRPC$Peer6 = tLRPC$Message.peer_id;
                                            if (tLRPC$Peer6 != null) {
                                                long j8 = tLRPC$Peer6.channel_id;
                                                if (j8 != 0) {
                                                    tLRPC$Chat6 = getChat(abstractMap2, longSparseArray2, j8);
                                                    boolean isChannelAndNotMegaGroup4 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat6);
                                                    tLRPC$TL_messageActionGiveawayResults = (TLRPC$TL_messageActionGiveawayResults) this.messageOwner.action;
                                                    r2 = new SpannableStringBuilder();
                                                    if (tLRPC$TL_messageActionGiveawayResults.stars) {
                                                        r2.append(LocaleController.formatPluralString("BoostingGiveawayServiceWinnersSelected", tLRPC$TL_messageActionGiveawayResults.winners_count, new Object[0]));
                                                        if (tLRPC$TL_messageActionGiveawayResults.unclaimed_count > 0) {
                                                            r2.append("\n");
                                                            formatPluralString2 = LocaleController.formatPluralString(isChannelAndNotMegaGroup4 ? "BoostingGiveawayServiceUndistributed" : "BoostingGiveawayServiceUndistributedGroup", tLRPC$TL_messageActionGiveawayResults.unclaimed_count, new Object[0]);
                                                            r2.append(formatPluralString2);
                                                        }
                                                        str = "";
                                                    } else {
                                                        r2.append(LocaleController.formatPluralStringComma("BoostingStarsGiveawayServiceWinnersSelected", tLRPC$TL_messageActionGiveawayResults.winners_count));
                                                        if (tLRPC$TL_messageActionGiveawayResults.unclaimed_count > 0) {
                                                            r2.append("\n");
                                                            formatPluralString2 = LocaleController.formatPluralString(isChannelAndNotMegaGroup4 ? "BoostingStarsGiveawayServiceUndistributed" : "BoostingStarsGiveawayServiceUndistributedGroup", tLRPC$TL_messageActionGiveawayResults.unclaimed_count, new Object[0]);
                                                            r2.append(formatPluralString2);
                                                        }
                                                        str = "";
                                                    }
                                                }
                                            }
                                            tLRPC$Chat6 = null;
                                            boolean isChannelAndNotMegaGroup42 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat6);
                                            tLRPC$TL_messageActionGiveawayResults = (TLRPC$TL_messageActionGiveawayResults) this.messageOwner.action;
                                            r2 = new SpannableStringBuilder();
                                            if (tLRPC$TL_messageActionGiveawayResults.stars) {
                                            }
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPrizeStars) {
                                            TLRPC$TL_messageActionPrizeStars tLRPC$TL_messageActionPrizeStars = (TLRPC$TL_messageActionPrizeStars) tLRPC$MessageAction;
                                            chat2 = getChat(abstractMap2, longSparseArray2, -DialogObject.getPeerDialogId(tLRPC$TL_messageActionPrizeStars.boost_peer));
                                            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("ActionStarGiveawayPrize", (int) tLRPC$TL_messageActionPrizeStars.stars));
                                        } else {
                                            str = "";
                                            String str15 = ", ";
                                            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftStars) {
                                                boolean z3 = tLObject2 instanceof TLRPC$User;
                                                this.messageText = (z3 && ((TLRPC$User) tLObject2).self) ? replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftOutbound)), "un1", getUser(abstractMap, longSparseArray, tLRPC$Message.peer_id.user_id)) : (z3 && UserObject.isService(((TLRPC$User) tLObject2).id)) ? TextUtils.replace(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), new String[]{"un1"}, new CharSequence[]{LocaleController.getString(R.string.StarsTransactionUnknown)}) : replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), "un1", tLObject2);
                                                indexOf = this.messageText.toString().indexOf("un2");
                                                str3 = str;
                                                if (indexOf != -1) {
                                                    valueOf = SpannableStringBuilder.valueOf(this.messageText);
                                                    BillingController billingController = BillingController.getInstance();
                                                    TLRPC$MessageAction tLRPC$MessageAction2 = this.messageOwner.action;
                                                    formatCurrency = billingController.formatCurrency(tLRPC$MessageAction2.amount, tLRPC$MessageAction2.currency);
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
                                                }
                                            } else {
                                                boolean z4 = tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftCode;
                                                if (z4 && ((TLRPC$TL_messageActionGiftCode) tLRPC$MessageAction).boost_peer != null) {
                                                    i = R.string.BoostingReceivedGiftNoName;
                                                    str = str;
                                                } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftPremium) || z4) {
                                                    this.messageText = ((tLObject2 instanceof TLRPC$User) && ((TLRPC$User) tLObject2).self) ? replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftOutbound)), "un1", getUser(abstractMap, longSparseArray, tLRPC$Message.peer_id.user_id)) : replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), "un1", tLObject2);
                                                    indexOf = this.messageText.toString().indexOf("un2");
                                                    str3 = str;
                                                    if (indexOf != -1) {
                                                        valueOf = SpannableStringBuilder.valueOf(this.messageText);
                                                        BillingController billingController2 = BillingController.getInstance();
                                                        TLRPC$MessageAction tLRPC$MessageAction3 = this.messageOwner.action;
                                                        formatCurrency = billingController2.formatCurrency(tLRPC$MessageAction3.amount, tLRPC$MessageAction3.currency);
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
                                                    }
                                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSuggestProfilePhoto) {
                                                    TLRPC$Photo tLRPC$Photo = tLRPC$MessageAction.photo;
                                                    if (tLRPC$Photo == null || (arrayList = tLRPC$Photo.video_sizes) == null || arrayList.isEmpty()) {
                                                        i = R.string.ActionSuggestPhotoShort;
                                                        str = str;
                                                    } else {
                                                        i = R.string.ActionSuggestVideoShort;
                                                        str = str;
                                                    }
                                                } else {
                                                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto) {
                                                        TLRPC$Peer tLRPC$Peer7 = tLRPC$Message.peer_id;
                                                        if (tLRPC$Peer7 != null) {
                                                            long j9 = tLRPC$Peer7.channel_id;
                                                            if (j9 != 0) {
                                                                tLRPC$Chat5 = getChat(abstractMap2, longSparseArray2, j9);
                                                                if (ChatObject.isChannel(tLRPC$Chat5) || tLRPC$Chat5.megagroup) {
                                                                    if (isOut()) {
                                                                        i2 = isVideoAvatar() ? R.string.ActionChangedVideo : R.string.ActionChangedPhoto;
                                                                        str4 = "un1";
                                                                        str5 = str;
                                                                        formatString = LocaleController.getString(i2);
                                                                        str13 = str4;
                                                                        tLObject = tLObject2;
                                                                    } else if (isVideoAvatar()) {
                                                                        i = R.string.ActionYouChangedVideo;
                                                                        str = str;
                                                                    } else {
                                                                        i = R.string.ActionYouChangedPhoto;
                                                                        str = str;
                                                                    }
                                                                } else if (isVideoAvatar()) {
                                                                    i = R.string.ActionChannelChangedVideo;
                                                                    str = str;
                                                                } else {
                                                                    i = R.string.ActionChannelChangedPhoto;
                                                                    str = str;
                                                                }
                                                            }
                                                        }
                                                        tLRPC$Chat5 = null;
                                                        if (ChatObject.isChannel(tLRPC$Chat5)) {
                                                        }
                                                        if (isOut()) {
                                                        }
                                                    } else {
                                                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditTitle) {
                                                            TLRPC$Peer tLRPC$Peer8 = tLRPC$Message.peer_id;
                                                            if (tLRPC$Peer8 != null) {
                                                                long j10 = tLRPC$Peer8.channel_id;
                                                                if (j10 != 0) {
                                                                    tLRPC$Chat4 = getChat(abstractMap2, longSparseArray2, j10);
                                                                    if (!ChatObject.isChannel(tLRPC$Chat4) && !tLRPC$Chat4.megagroup) {
                                                                        i6 = R.string.ActionChannelChangedTitle;
                                                                    } else if (isOut()) {
                                                                        formatString = LocaleController.getString(R.string.ActionChangedTitle).replace("un2", this.messageOwner.action.title);
                                                                        str4 = "un1";
                                                                        str5 = str;
                                                                        str13 = str4;
                                                                        tLObject = tLObject2;
                                                                    } else {
                                                                        i6 = R.string.ActionYouChangedTitle;
                                                                    }
                                                                    charSequence = LocaleController.getString(i6).replace("un2", this.messageOwner.action.title);
                                                                }
                                                            }
                                                            tLRPC$Chat4 = null;
                                                            if (!ChatObject.isChannel(tLRPC$Chat4)) {
                                                            }
                                                            if (isOut()) {
                                                            }
                                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatDeletePhoto) {
                                                            TLRPC$Peer tLRPC$Peer9 = tLRPC$Message.peer_id;
                                                            if (tLRPC$Peer9 != null) {
                                                                long j11 = tLRPC$Peer9.channel_id;
                                                                if (j11 != 0) {
                                                                    tLRPC$Chat3 = getChat(abstractMap2, longSparseArray2, j11);
                                                                    if (!ChatObject.isChannel(tLRPC$Chat3) && !tLRPC$Chat3.megagroup) {
                                                                        i = R.string.ActionChannelRemovedPhoto;
                                                                        str = str;
                                                                    } else if (isOut()) {
                                                                        i2 = R.string.ActionRemovedPhoto;
                                                                        str4 = "un1";
                                                                        str5 = str;
                                                                        formatString = LocaleController.getString(i2);
                                                                        str13 = str4;
                                                                        tLObject = tLObject2;
                                                                    } else {
                                                                        i = R.string.ActionYouRemovedPhoto;
                                                                        str = str;
                                                                    }
                                                                }
                                                            }
                                                            tLRPC$Chat3 = null;
                                                            if (!ChatObject.isChannel(tLRPC$Chat3)) {
                                                            }
                                                            if (isOut()) {
                                                            }
                                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTTLChange) {
                                                            if (tLRPC$MessageAction.ttl != 0) {
                                                                charSequence = isOut() ? LocaleController.formatString(R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(this.messageOwner.action.ttl)) : LocaleController.formatString(R.string.MessageLifetimeChanged, UserObject.getFirstName(tLRPC$User), LocaleController.formatTTLString(this.messageOwner.action.ttl));
                                                            } else if (isOut()) {
                                                                str5 = str;
                                                                i = R.string.MessageLifetimeYouRemoved;
                                                                str = str5;
                                                            } else {
                                                                charSequence = LocaleController.formatString(R.string.MessageLifetimeRemoved, UserObject.getFirstName(tLRPC$User));
                                                            }
                                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionRequestedPeer) {
                                                            ArrayList arrayList3 = new ArrayList();
                                                            int i21 = 0;
                                                            int i22 = 0;
                                                            int i23 = 0;
                                                            for (TLRPC$Peer tLRPC$Peer10 : ((TLRPC$TL_messageActionRequestedPeer) this.messageOwner.action).peers) {
                                                                boolean z5 = tLRPC$Peer10 instanceof TLRPC$TL_peerUser;
                                                                if (z5) {
                                                                    str8 = str14;
                                                                    str9 = str13;
                                                                    obj = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$Peer10.user_id));
                                                                    if (obj == null) {
                                                                        obj = getUser(abstractMap, longSparseArray, tLRPC$Peer10.user_id);
                                                                    }
                                                                } else {
                                                                    str8 = str14;
                                                                    str9 = str13;
                                                                    if (tLRPC$Peer10 instanceof TLRPC$TL_peerChat) {
                                                                        obj = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$Peer10.chat_id));
                                                                        if (obj == null) {
                                                                            j2 = tLRPC$Peer10.chat_id;
                                                                            obj = getChat(abstractMap2, longSparseArray2, j2);
                                                                        }
                                                                    } else if (tLRPC$Peer10 instanceof TLRPC$TL_peerChannel) {
                                                                        obj = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$Peer10.channel_id));
                                                                        if (obj == null) {
                                                                            j2 = tLRPC$Peer10.channel_id;
                                                                            obj = getChat(abstractMap2, longSparseArray2, j2);
                                                                        }
                                                                    } else {
                                                                        obj = null;
                                                                    }
                                                                }
                                                                if (z5) {
                                                                    i21++;
                                                                } else if (tLRPC$Peer10 instanceof TLRPC$TL_peerChat) {
                                                                    i23++;
                                                                } else {
                                                                    i22++;
                                                                }
                                                                if (obj != null) {
                                                                    arrayList3.add(obj);
                                                                }
                                                                str13 = str9;
                                                                str14 = str8;
                                                            }
                                                            String str16 = str14;
                                                            String str17 = str13;
                                                            if (i21 > 0 && i21 != arrayList3.size()) {
                                                                replaceCharSequence = LocaleController.getPluralString("ActionRequestedPeerUserPlural", i21);
                                                            } else if (i22 > 0 && i22 != arrayList3.size()) {
                                                                replaceCharSequence = LocaleController.getPluralString("ActionRequestedPeerChannelPlural", i22);
                                                            } else if (i23 <= 0 || i23 == arrayList3.size()) {
                                                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                                                int i24 = 0;
                                                                while (i24 < arrayList3.size()) {
                                                                    String str18 = str17;
                                                                    spannableStringBuilder.append(replaceWithLink(str18, str18, (TLObject) arrayList3.get(i24)));
                                                                    String str19 = str15;
                                                                    if (i24 < arrayList3.size() - 1) {
                                                                        spannableStringBuilder.append((CharSequence) str19);
                                                                    }
                                                                    i24++;
                                                                    str15 = str19;
                                                                    str17 = str18;
                                                                }
                                                                replaceCharSequence = AndroidUtilities.replaceCharSequence(str17, LocaleController.getString(R.string.ActionRequestedPeer), spannableStringBuilder);
                                                            } else {
                                                                replaceCharSequence = LocaleController.getPluralString("ActionRequestedPeerChatPlural", i23);
                                                            }
                                                            this.messageText = replaceCharSequence;
                                                            TLObject user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
                                                            if (user5 == null) {
                                                                user5 = getUser(abstractMap, longSparseArray, getDialogId());
                                                            }
                                                            tLObject = user5;
                                                            formatString = this.messageText;
                                                            str5 = str;
                                                            str13 = str16;
                                                        } else {
                                                            str4 = "un1";
                                                            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) {
                                                                TLRPC$TL_messageActionSetMessagesTTL tLRPC$TL_messageActionSetMessagesTTL = (TLRPC$TL_messageActionSetMessagesTTL) tLRPC$MessageAction;
                                                                TLRPC$Peer tLRPC$Peer11 = tLRPC$Message.peer_id;
                                                                if (tLRPC$Peer11 != null) {
                                                                    long j12 = tLRPC$Peer11.channel_id;
                                                                    if (j12 != 0) {
                                                                        tLRPC$Chat2 = getChat(abstractMap2, longSparseArray2, j12);
                                                                        if (tLRPC$Chat2 != null || tLRPC$Chat2.megagroup) {
                                                                            j = tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from;
                                                                            if (j == 0) {
                                                                                this.drawServiceWithDefaultTypeface = true;
                                                                                if (j == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                                                    charSequence = AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AutoDeleteGlobalActionFromYou, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period)));
                                                                                } else {
                                                                                    TLRPC$Chat tLRPC$Chat11 = longSparseArray != null ? (TLObject) longSparseArray.get(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from) : null;
                                                                                    if (tLRPC$Chat11 == null && abstractMap != null) {
                                                                                        tLRPC$Chat11 = abstractMap.get(Long.valueOf(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                                                    }
                                                                                    if (tLRPC$Chat11 == null && abstractMap2 != null) {
                                                                                        tLRPC$Chat11 = abstractMap2.get(Long.valueOf(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                                                    }
                                                                                    if (tLRPC$Chat11 == null) {
                                                                                        int i25 = (tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from > 0L ? 1 : (tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from == 0L ? 0 : -1));
                                                                                        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                        tLRPC$Chat11 = i25 > 0 ? messagesController.getUser(Long.valueOf(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from)) : messagesController.getChat(Long.valueOf(-tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                                                    }
                                                                                    if (tLRPC$Chat11 != null) {
                                                                                        tLObject2 = tLRPC$Chat11;
                                                                                    }
                                                                                    formatString = AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AutoDeleteGlobalAction, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period)));
                                                                                    str5 = str;
                                                                                    str13 = str4;
                                                                                    tLObject = tLObject2;
                                                                                }
                                                                            } else if (tLRPC$TL_messageActionSetMessagesTTL.period != 0) {
                                                                                if (isOut()) {
                                                                                    charSequence = LocaleController.formatString(R.string.ActionTTLYouChanged, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period));
                                                                                } else {
                                                                                    formatString = LocaleController.formatString(R.string.ActionTTLChanged, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period));
                                                                                    str5 = str;
                                                                                    str13 = str4;
                                                                                    tLObject = tLObject2;
                                                                                }
                                                                            } else if (isOut()) {
                                                                                i = R.string.ActionTTLYouDisabled;
                                                                                str = str;
                                                                            } else {
                                                                                i2 = R.string.ActionTTLDisabled;
                                                                                str5 = str;
                                                                                formatString = LocaleController.getString(i2);
                                                                                str13 = str4;
                                                                                tLObject = tLObject2;
                                                                            }
                                                                        } else {
                                                                            int i26 = tLRPC$TL_messageActionSetMessagesTTL.period;
                                                                            if (i26 != 0) {
                                                                                charSequence = LocaleController.formatString(R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i26));
                                                                            } else {
                                                                                i = R.string.ActionTTLChannelDisabled;
                                                                                str = str;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                tLRPC$Chat2 = null;
                                                                if (tLRPC$Chat2 != null) {
                                                                }
                                                                j = tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from;
                                                                if (j == 0) {
                                                                }
                                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                                                                long j13 = tLRPC$Message.date * 1000;
                                                                if (LocaleController.getInstance().getFormatterDay() == null || LocaleController.getInstance().getFormatterYear() == null) {
                                                                    StringBuilder sb3 = new StringBuilder();
                                                                    str5 = str;
                                                                    sb3.append(str5);
                                                                    sb3.append(this.messageOwner.date);
                                                                    sb2 = sb3.toString();
                                                                } else {
                                                                    sb2 = LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(j13), LocaleController.getInstance().getFormatterDay().format(j13));
                                                                    str5 = str;
                                                                }
                                                                TLRPC$User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                                                                if (currentUser == null) {
                                                                    currentUser = getUser(abstractMap, longSparseArray, this.messageOwner.peer_id.user_id);
                                                                }
                                                                String firstName = currentUser != null ? UserObject.getFirstName(currentUser) : str5;
                                                                int i27 = R.string.NotificationUnrecognizedDevice;
                                                                TLRPC$MessageAction tLRPC$MessageAction4 = this.messageOwner.action;
                                                                charSequence3 = LocaleController.formatString(i27, firstName, sb2, tLRPC$MessageAction4.title, tLRPC$MessageAction4.address);
                                                            } else {
                                                                str5 = str;
                                                                if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                                                                    charSequence3 = LocaleController.formatString(R.string.NotificationContactJoined, UserObject.getUserName(tLRPC$User));
                                                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                                                                    charSequence3 = LocaleController.formatString(R.string.NotificationContactNewPhoto, UserObject.getUserName(tLRPC$User));
                                                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
                                                                    TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
                                                                    if (!(tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages)) {
                                                                        if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL) {
                                                                            TLRPC$TL_decryptedMessageActionSetMessageTTL tLRPC$TL_decryptedMessageActionSetMessageTTL = (TLRPC$TL_decryptedMessageActionSetMessageTTL) tLRPC$DecryptedMessageAction;
                                                                            if (tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds != 0) {
                                                                                charSequence3 = isOut() ? LocaleController.formatString(R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds)) : LocaleController.formatString(R.string.MessageLifetimeChanged, UserObject.getFirstName(tLRPC$User), LocaleController.formatTTLString(tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                                                            } else {
                                                                                if (!isOut()) {
                                                                                    charSequence3 = LocaleController.formatString(R.string.MessageLifetimeRemoved, UserObject.getFirstName(tLRPC$User));
                                                                                }
                                                                                i = R.string.MessageLifetimeYouRemoved;
                                                                                str = str5;
                                                                            }
                                                                        }
                                                                        str3 = str5;
                                                                    }
                                                                } else if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionScreenshotTaken)) {
                                                                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCreatedBroadcastList) {
                                                                        i = R.string.YouCreatedBroadcastList;
                                                                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChannelCreate) {
                                                                        TLRPC$Peer tLRPC$Peer12 = tLRPC$Message.peer_id;
                                                                        if (tLRPC$Peer12 != null) {
                                                                            long j14 = tLRPC$Peer12.channel_id;
                                                                            if (j14 != 0) {
                                                                                tLRPC$Chat = getChat(abstractMap2, longSparseArray2, j14);
                                                                                i = (ChatObject.isChannel(tLRPC$Chat) || !tLRPC$Chat.megagroup) ? R.string.ActionCreateChannel : R.string.ActionCreateMega;
                                                                            }
                                                                        }
                                                                        tLRPC$Chat = null;
                                                                        if (ChatObject.isChannel(tLRPC$Chat)) {
                                                                        }
                                                                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatMigrateTo) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                        i = R.string.ActionMigrateFromGroup;
                                                                    } else {
                                                                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPinMessage) {
                                                                            generatePinMessageText(tLRPC$User, tLRPC$User == null ? getChat(abstractMap2, longSparseArray2, tLRPC$Message.peer_id.channel_id) : null);
                                                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear) {
                                                                            i = R.string.HistoryCleared;
                                                                        } else {
                                                                            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicCreate) {
                                                                                this.messageText = LocaleController.getString(R.string.TopicCreated);
                                                                                TLRPC$TL_messageActionTopicCreate tLRPC$TL_messageActionTopicCreate = (TLRPC$TL_messageActionTopicCreate) this.messageOwner.action;
                                                                                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = new TLRPC$TL_forumTopic();
                                                                                tLRPC$TL_forumTopic.icon_emoji_id = tLRPC$TL_messageActionTopicCreate.icon_emoji_id;
                                                                                tLRPC$TL_forumTopic.title = tLRPC$TL_messageActionTopicCreate.title;
                                                                                tLRPC$TL_forumTopic.icon_color = tLRPC$TL_messageActionTopicCreate.icon_color;
                                                                                string2 = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString(R.string.TopicWasCreatedAction), ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic, null, false));
                                                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicEdit) {
                                                                                TLRPC$TL_messageActionTopicEdit tLRPC$TL_messageActionTopicEdit = (TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction;
                                                                                if (tLRPC$User != null) {
                                                                                    str7 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                                                                                } else if (tLRPC$Chat10 != null) {
                                                                                    tLRPC$User = tLRPC$Chat10;
                                                                                    str7 = tLRPC$Chat10.title;
                                                                                } else {
                                                                                    str7 = null;
                                                                                    tLRPC$User = null;
                                                                                }
                                                                                String trim = str7 != null ? str7.trim() : "DELETED";
                                                                                TLRPC$MessageAction tLRPC$MessageAction5 = this.messageOwner.action;
                                                                                int i28 = tLRPC$MessageAction5.flags;
                                                                                if ((i28 & 8) > 0) {
                                                                                    if (((TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction5).hidden) {
                                                                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicHidden2), "%s", tLRPC$User);
                                                                                        i5 = R.string.TopicHidden;
                                                                                    } else {
                                                                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicShown2), "%s", tLRPC$User);
                                                                                        i5 = R.string.TopicShown;
                                                                                    }
                                                                                } else if ((4 & i28) <= 0) {
                                                                                    int i29 = 2 & i28;
                                                                                    if (i29 != 0 && (i28 & 1) != 0) {
                                                                                        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = new TLRPC$TL_forumTopic();
                                                                                        tLRPC$TL_forumTopic2.icon_emoji_id = tLRPC$TL_messageActionTopicEdit.icon_emoji_id;
                                                                                        tLRPC$TL_forumTopic2.title = tLRPC$TL_messageActionTopicEdit.title;
                                                                                        tLRPC$TL_forumTopic2.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                                                                        charSequence2 = ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic2, null, this.topicIconDrawable, false);
                                                                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString(R.string.TopicChangeIconAndTitleTo), trim), charSequence2);
                                                                                        this.messageTextShort = LocaleController.getString(R.string.TopicRenamed);
                                                                                        i4 = R.string.TopicChangeIconAndTitleToInReply;
                                                                                    } else if (i29 != 0) {
                                                                                        TLRPC$TL_forumTopic tLRPC$TL_forumTopic3 = new TLRPC$TL_forumTopic();
                                                                                        tLRPC$TL_forumTopic3.icon_emoji_id = tLRPC$TL_messageActionTopicEdit.icon_emoji_id;
                                                                                        tLRPC$TL_forumTopic3.title = str5;
                                                                                        tLRPC$TL_forumTopic3.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                                                                        charSequence2 = ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic3, null, this.topicIconDrawable, false);
                                                                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString(R.string.TopicIconChangedTo), trim), charSequence2);
                                                                                        this.messageTextShort = LocaleController.getString(R.string.TopicIconChanged);
                                                                                        i4 = R.string.TopicIconChangedToInReply;
                                                                                    } else if ((1 & i28) != 0) {
                                                                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString(R.string.TopicRenamedTo), trim), tLRPC$TL_messageActionTopicEdit.title);
                                                                                        this.messageTextShort = LocaleController.getString(R.string.TopicRenamed);
                                                                                        string = LocaleController.getString(R.string.TopicRenamedToInReply);
                                                                                        charSequence2 = tLRPC$TL_messageActionTopicEdit.title;
                                                                                        this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", string, charSequence2);
                                                                                    }
                                                                                    string = LocaleController.getString(i4);
                                                                                    this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", string, charSequence2);
                                                                                } else if (((TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction5).closed) {
                                                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicClosed2), "%s", tLRPC$User);
                                                                                    i5 = R.string.TopicClosed;
                                                                                } else {
                                                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicRestarted2), "%s", tLRPC$User);
                                                                                    i5 = R.string.TopicRestarted;
                                                                                }
                                                                                string2 = LocaleController.getString(i5);
                                                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) {
                                                                                generateGameMessageText(tLRPC$User);
                                                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                                                                                TLRPC$TL_messageActionPhoneCall tLRPC$TL_messageActionPhoneCall = (TLRPC$TL_messageActionPhoneCall) tLRPC$MessageAction;
                                                                                boolean z6 = tLRPC$TL_messageActionPhoneCall.reason instanceof TLRPC$TL_phoneCallDiscardReasonMissed;
                                                                                this.messageText = LocaleController.getString((isFromUser() && this.messageOwner.from_id.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) ? z6 ? tLRPC$TL_messageActionPhoneCall.video ? R.string.CallMessageVideoOutgoingMissed : R.string.CallMessageOutgoingMissed : tLRPC$TL_messageActionPhoneCall.video ? R.string.CallMessageVideoOutgoing : R.string.CallMessageOutgoing : z6 ? tLRPC$TL_messageActionPhoneCall.video ? R.string.CallMessageVideoIncomingMissed : R.string.CallMessageIncomingMissed : tLRPC$TL_messageActionPhoneCall.reason instanceof TLRPC$TL_phoneCallDiscardReasonBusy ? tLRPC$TL_messageActionPhoneCall.video ? R.string.CallMessageVideoIncomingDeclined : R.string.CallMessageIncomingDeclined : tLRPC$TL_messageActionPhoneCall.video ? R.string.CallMessageVideoIncoming : R.string.CallMessageIncoming);
                                                                                int i30 = tLRPC$TL_messageActionPhoneCall.duration;
                                                                                if (i30 > 0) {
                                                                                    String formatCallDuration = LocaleController.formatCallDuration(i30);
                                                                                    String formatString2 = LocaleController.formatString(R.string.CallMessageWithDuration, this.messageText, formatCallDuration);
                                                                                    this.messageText = formatString2;
                                                                                    String charSequence5 = formatString2.toString();
                                                                                    int indexOf2 = charSequence5.indexOf(formatCallDuration);
                                                                                    if (indexOf2 != -1) {
                                                                                        SpannableString spannableString4 = new SpannableString(this.messageText);
                                                                                        int length = formatCallDuration.length() + indexOf2;
                                                                                        if (indexOf2 > 0 && charSequence5.charAt(indexOf2 - 1) == '(') {
                                                                                            indexOf2--;
                                                                                        }
                                                                                        if (length < charSequence5.length() && charSequence5.charAt(length) == ')') {
                                                                                            length++;
                                                                                        }
                                                                                        spannableString4.setSpan(new TypefaceSpan(Typeface.DEFAULT), indexOf2, length, 0);
                                                                                        str = str5;
                                                                                        r2 = spannableString4;
                                                                                    }
                                                                                }
                                                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent) {
                                                                                generatePaymentSentMessageText(getUser(abstractMap, longSparseArray, getDialogId()));
                                                                            } else {
                                                                                boolean z7 = tLRPC$MessageAction instanceof TLRPC$TL_messageActionBotAllowed;
                                                                                if (z7) {
                                                                                    TLRPC$TL_messageActionBotAllowed tLRPC$TL_messageActionBotAllowed = (TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction;
                                                                                    String str20 = tLRPC$TL_messageActionBotAllowed.domain;
                                                                                    TLRPC$BotApp tLRPC$BotApp = tLRPC$TL_messageActionBotAllowed.app;
                                                                                    if (tLRPC$TL_messageActionBotAllowed.from_request) {
                                                                                        i = R.string.ActionBotAllowedWebapp;
                                                                                    } else if (tLRPC$BotApp != null) {
                                                                                        String str21 = tLRPC$BotApp.title;
                                                                                        if (str21 == null) {
                                                                                            str21 = str5;
                                                                                        }
                                                                                        String string3 = LocaleController.getString(R.string.ActionBotAllowedApp);
                                                                                        int indexOf3 = string3.indexOf("%1$s");
                                                                                        SpannableString spannableString5 = new SpannableString(String.format(string3, str21));
                                                                                        TLRPC$User user6 = getUser(abstractMap, longSparseArray, getDialogId());
                                                                                        if (indexOf3 >= 0 && user6 != null && (publicUsername2 = UserObject.getPublicUsername(user6)) != null) {
                                                                                            spannableString5.setSpan(new URLSpanNoUnderlineBold("https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername2 + "/" + tLRPC$BotApp.short_name), indexOf3, str21.length() + indexOf3, 33);
                                                                                        }
                                                                                        str = str5;
                                                                                        r2 = spannableString5;
                                                                                    } else {
                                                                                        if (str20 == null) {
                                                                                            str20 = str5;
                                                                                        }
                                                                                        String string4 = LocaleController.getString(R.string.ActionBotAllowed);
                                                                                        int indexOf4 = string4.indexOf("%1$s");
                                                                                        SpannableString spannableString6 = new SpannableString(String.format(string4, str20));
                                                                                        if (indexOf4 >= 0 && !TextUtils.isEmpty(str20)) {
                                                                                            spannableString6.setSpan(new URLSpanNoUnderlineBold("http://" + str20), indexOf4, str20.length() + indexOf4, 33);
                                                                                        }
                                                                                        str = str5;
                                                                                        r2 = spannableString6;
                                                                                    }
                                                                                } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionAttachMenuBotAllowed) || (z7 && ((TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction).attach_menu)) {
                                                                                    i = R.string.ActionAttachMenuBotAllowed;
                                                                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSecureValuesSent) {
                                                                                    TLRPC$TL_messageActionSecureValuesSent tLRPC$TL_messageActionSecureValuesSent = (TLRPC$TL_messageActionSecureValuesSent) tLRPC$MessageAction;
                                                                                    StringBuilder sb4 = new StringBuilder();
                                                                                    int size2 = tLRPC$TL_messageActionSecureValuesSent.types.size();
                                                                                    for (int i31 = 0; i31 < size2; i31++) {
                                                                                        TLRPC$SecureValueType tLRPC$SecureValueType = (TLRPC$SecureValueType) tLRPC$TL_messageActionSecureValuesSent.types.get(i31);
                                                                                        if (sb4.length() > 0) {
                                                                                            sb4.append(str15);
                                                                                        }
                                                                                        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
                                                                                            i3 = R.string.ActionBotDocumentPhone;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) {
                                                                                            i3 = R.string.ActionBotDocumentEmail;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                                                                                            i3 = R.string.ActionBotDocumentAddress;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                                                                                            i3 = R.string.ActionBotDocumentIdentity;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
                                                                                            i3 = R.string.ActionBotDocumentPassport;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                                                                                            i3 = R.string.ActionBotDocumentDriverLicence;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                                                                                            i3 = R.string.ActionBotDocumentIdentityCard;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                                                                                            i3 = R.string.ActionBotDocumentUtilityBill;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) {
                                                                                            i3 = R.string.ActionBotDocumentBankStatement;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                                                                                            i3 = R.string.ActionBotDocumentRentalAgreement;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                                                                                            i3 = R.string.ActionBotDocumentInternalPassport;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                                                                                            i3 = R.string.ActionBotDocumentPassportRegistration;
                                                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                                                                                            i3 = R.string.ActionBotDocumentTemporaryRegistration;
                                                                                        }
                                                                                        sb4.append(LocaleController.getString(i3));
                                                                                    }
                                                                                    TLRPC$Peer tLRPC$Peer13 = this.messageOwner.peer_id;
                                                                                    charSequence3 = LocaleController.formatString("ActionBotDocuments", R.string.ActionBotDocuments, UserObject.getFirstName(tLRPC$Peer13 != null ? getUser(abstractMap, longSparseArray, tLRPC$Peer13.user_id) : null), sb4.toString());
                                                                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionWebViewDataSent) {
                                                                                    charSequence3 = LocaleController.formatString("ActionBotWebViewData", R.string.ActionBotWebViewData, ((TLRPC$TL_messageActionWebViewDataSent) tLRPC$MessageAction).text);
                                                                                } else {
                                                                                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                                                                                        String str22 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                                                                                        String firstName2 = UserObject.getFirstName(tLRPC$User);
                                                                                        boolean z8 = tLRPC$User == null && tLRPC$Chat10 != null;
                                                                                        if (z8) {
                                                                                            firstName2 = tLRPC$Chat10.title;
                                                                                        }
                                                                                        boolean isUserSelf = UserObject.isUserSelf(tLRPC$User);
                                                                                        if (TextUtils.isEmpty(str22)) {
                                                                                            str6 = isUserSelf ? LocaleController.formatString("ChatThemeDisabledYou", R.string.ChatThemeDisabledYou, new Object[0]) : LocaleController.formatString(z8 ? R.string.ChannelThemeDisabled : R.string.ChatThemeDisabled, firstName2, str22);
                                                                                        } else if (isUserSelf) {
                                                                                            str6 = LocaleController.formatString("ChatThemeChangedYou", R.string.ChatThemeChangedYou, str22);
                                                                                        } else {
                                                                                            str6 = LocaleController.formatString(z8 ? R.string.ChannelThemeChangedTo : R.string.ChatThemeChangedTo, firstName2, str22);
                                                                                        }
                                                                                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatJoinedByRequest) {
                                                                                        if (UserObject.isUserSelf(tLRPC$User)) {
                                                                                            str6 = LocaleController.getString(ChatObject.isChannelAndNotMegaGroup(this.messageOwner.peer_id.channel_id, this.currentAccount) ? R.string.RequestToJoinChannelApproved : R.string.RequestToJoinGroupApproved);
                                                                                        } else {
                                                                                            i2 = R.string.UserAcceptedToGroupAction;
                                                                                            formatString = LocaleController.getString(i2);
                                                                                            str13 = str4;
                                                                                            tLObject = tLObject2;
                                                                                        }
                                                                                    }
                                                                                    r2 = str6;
                                                                                    str = str5;
                                                                                }
                                                                            }
                                                                            this.messageTextShort = string2;
                                                                        }
                                                                        str3 = str5;
                                                                    }
                                                                    str = str5;
                                                                }
                                                            }
                                                        }
                                                        str = str5;
                                                        charSequence = charSequence3;
                                                    }
                                                    charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                                    str = str5;
                                                    charSequence = charSequence3;
                                                }
                                                charSequence = LocaleController.getString(i);
                                            }
                                        }
                                        tLObject = chat2;
                                        formatString = replaceTags;
                                        str5 = "";
                                        charSequence3 = replaceWithLink(formatString, str13, tLObject);
                                        str = str5;
                                        charSequence = charSequence3;
                                    }
                                }
                                r2 = charSequence;
                            }
                            str = "";
                            charSequence = charSequence4;
                            r2 = charSequence;
                        }
                        if (this.messageText == null) {
                            this.messageText = str3;
                            return;
                        }
                        return;
                    }
                    this.contentType = 1;
                    this.type = 10;
                    TLRPC$TL_messageActionSetSameChatWallPaper tLRPC$TL_messageActionSetSameChatWallPaper = (TLRPC$TL_messageActionSetSameChatWallPaper) tLRPC$MessageAction;
                    TLRPC$User user7 = getUser(abstractMap, longSparseArray, isOutOwner() ? 0L : getDialogId());
                    ArrayList<TLRPC$PhotoSize> arrayList4 = new ArrayList<>();
                    this.photoThumbs = arrayList4;
                    TLRPC$Document tLRPC$Document2 = tLRPC$TL_messageActionSetSameChatWallPaper.wallpaper.document;
                    if (tLRPC$Document2 != null) {
                        arrayList4.addAll(tLRPC$Document2.thumbs);
                        this.photoThumbsObject = tLRPC$TL_messageActionSetSameChatWallPaper.wallpaper.document;
                    }
                    if (user7 != null) {
                        charSequence4 = user7.id == UserConfig.getInstance(this.currentAccount).clientUserId ? LocaleController.formatString(R.string.ActionSetSameWallpaperForThisChatSelf, new Object[0]) : LocaleController.formatString(R.string.ActionSetSameWallpaperForThisChat, user7.first_name);
                    } else if (chat == null) {
                        if (tLRPC$User != null) {
                            charSequence4 = LocaleController.formatString(R.string.ActionSetWallpaperForThisGroupByUser, UserObject.getFirstName(tLRPC$User));
                        }
                    }
                    str = "";
                    charSequence = charSequence4;
                    r2 = charSequence;
                }
                str3 = "";
                if (this.messageText == null) {
                }
            }
            this.messageText = r2;
            str3 = str;
            if (this.messageText == null) {
            }
        } else {
            tLRPC$User = null;
        }
        chat = null;
        if (tLRPC$User == null) {
        }
        this.drawServiceWithDefaultTypeface = false;
        this.channelJoined = false;
        tLRPC$Message = this.messageOwner;
        if (tLRPC$Message instanceof TLRPC$TL_messageService) {
        }
        this.messageText = r2;
        str3 = str;
        if (this.messageText == null) {
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

    public static void updatePollResults(TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll, TLRPC$PollResults tLRPC$PollResults) {
        ArrayList arrayList;
        byte[] bArr;
        ArrayList arrayList2;
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
                    TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters = (TLRPC$TL_pollAnswerVoters) tLRPC$TL_messageMediaPoll.results.results.get(i);
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
            ArrayList arrayList3 = tLRPC$PollResults.results;
            tLRPC$PollResults2.results = arrayList3;
            if (arrayList != null || bArr != null) {
                int size2 = arrayList3.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters2 = (TLRPC$TL_pollAnswerVoters) tLRPC$TL_messageMediaPoll.results.results.get(i2);
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

    public static void updateReactions(TLRPC$Message tLRPC$Message, TLRPC$TL_messageReactions tLRPC$TL_messageReactions) {
        if (tLRPC$Message == null || tLRPC$TL_messageReactions == null) {
            return;
        }
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions2 = tLRPC$Message.reactions;
        if (tLRPC$TL_messageReactions2 != null) {
            int size = tLRPC$TL_messageReactions2.results.size();
            boolean z = false;
            for (int i = 0; i < size; i++) {
                TLRPC$ReactionCount tLRPC$ReactionCount = (TLRPC$ReactionCount) tLRPC$Message.reactions.results.get(i);
                int size2 = tLRPC$TL_messageReactions.results.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    TLRPC$ReactionCount tLRPC$ReactionCount2 = (TLRPC$ReactionCount) tLRPC$TL_messageReactions.results.get(i2);
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
        FileLog.d("msg#" + tLRPC$Message.id + " updateReactions out=" + tLRPC$Message.out);
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
        ArrayList arrayList;
        if (charSequence == null) {
            return false;
        }
        if (!this.isRestrictedMessage && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaUnsupported)) {
            if (this.translated) {
                TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = this.messageOwner.translatedText;
                arrayList = tLRPC$TL_textWithEntities == null ? null : tLRPC$TL_textWithEntities.entities;
            } else {
                arrayList = this.messageOwner.entities;
            }
            return addEntitiesToText(charSequence, arrayList, isOutOwner(), true, z, z2);
        }
        ArrayList arrayList2 = new ArrayList();
        TLRPC$TL_messageEntityItalic tLRPC$TL_messageEntityItalic = new TLRPC$TL_messageEntityItalic();
        tLRPC$TL_messageEntityItalic.offset = 0;
        tLRPC$TL_messageEntityItalic.length = charSequence.length();
        arrayList2.add(tLRPC$TL_messageEntityItalic);
        return addEntitiesToText(charSequence, arrayList2, isOutOwner(), true, z, z2);
    }

    public void addPaidReactions(int i, boolean z, boolean z2) {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.reactions == null) {
            tLRPC$Message.reactions = new TLRPC$TL_messageReactions();
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            TLRPC$TL_messageReactions tLRPC$TL_messageReactions = tLRPC$Message2.reactions;
            long dialogId = getDialogId(tLRPC$Message2);
            boolean z3 = false;
            tLRPC$TL_messageReactions.reactions_as_tags = dialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
            this.messageOwner.reactions.can_see_list = (isFromGroup() || isFromUser()) ? true : true;
        }
        addPaidReactions(this.currentAccount, this.messageOwner.reactions, i, z2, z);
    }

    public void applyMediaExistanceFlags(int i) {
        if (i == -1) {
            checkMediaExistance();
            return;
        }
        this.attachPathExists = (i & 1) != 0;
        this.mediaExists = (i & 2) != 0;
    }

    public void applyNewText() {
        this.translated = false;
        applyNewText(this.messageOwner.message);
    }

    public void applyNewText(CharSequence charSequence) {
        TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities;
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        TLRPC$User user = isFromUser() ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id)) : null;
        this.messageText = charSequence;
        ArrayList arrayList = (!this.translated || (tLRPC$TL_textWithEntities = this.messageOwner.translatedText) == null) ? this.messageOwner.entities : tLRPC$TL_textWithEntities.entities;
        TextPaint textPaint = getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
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
        } else if (!messageObject.isVideo()) {
            if (messageObject.isMusic() || messageObject.isVoice()) {
                addUrlsByPattern(isOutOwner(), this.messageText, false, 4, (int) messageObject.getDuration(), false);
                return;
            }
            return;
        } else {
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

    public boolean canDeleteMessage(boolean z, TLRPC$Chat tLRPC$Chat) {
        TLRPC$Message tLRPC$Message;
        return (isStory() && (tLRPC$Message = this.messageOwner) != null && tLRPC$Message.dialog_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) || (this.eventId == 0 && this.sponsoredId == null && canDeleteMessage(this.currentAccount, z, this.messageOwner, tLRPC$Chat));
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

    public boolean canEditMessage(TLRPC$Chat tLRPC$Chat) {
        return canEditMessage(this.currentAccount, this.messageOwner, tLRPC$Chat, this.scheduled);
    }

    public boolean canEditMessageAnytime(TLRPC$Chat tLRPC$Chat) {
        return canEditMessageAnytime(this.currentAccount, this.messageOwner, tLRPC$Chat);
    }

    public boolean canEditMessageScheduleTime(TLRPC$Chat tLRPC$Chat) {
        return canEditMessageScheduleTime(this.currentAccount, this.messageOwner, tLRPC$Chat);
    }

    public boolean canForwardMessage() {
        return (isQuickReply() || (this.messageOwner instanceof TLRPC$TL_message_secret) || needDrawBluredPreview() || isLiveLocation() || this.type == 16 || isSponsored() || this.messageOwner.noforwards) ? false : true;
    }

    public boolean canPreviewDocument() {
        return canPreviewDocument(getDocument());
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

    public boolean canUnvote() {
        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll;
        TLRPC$PollResults tLRPC$PollResults;
        if (this.type == 17 && (tLRPC$PollResults = (tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).results) != null && !tLRPC$PollResults.results.isEmpty() && !tLRPC$TL_messageMediaPoll.poll.quiz) {
            int size = tLRPC$TL_messageMediaPoll.results.results.size();
            for (int i = 0; i < size; i++) {
                if (((TLRPC$TL_pollAnswerVoters) tLRPC$TL_messageMediaPoll.results.results.get(i)).chosen) {
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
        if (this.emojiOnlyCount == 1 && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice) && ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaEmpty) || getMedia(this.messageOwner) == null)) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message.grouped_id == 0) {
                if (tLRPC$Message.entities.isEmpty()) {
                    String str2 = this.messageText;
                    int indexOf = TextUtils.indexOf(str2, "🏻");
                    if (indexOf >= 0) {
                        str = "_c1";
                    } else {
                        indexOf = TextUtils.indexOf(str2, "🏼");
                        if (indexOf >= 0) {
                            str = "_c2";
                        } else {
                            indexOf = TextUtils.indexOf(str2, "🏽");
                            if (indexOf >= 0) {
                                str = "_c3";
                            } else {
                                indexOf = TextUtils.indexOf(str2, "🏾");
                                if (indexOf >= 0) {
                                    str = "_c4";
                                } else {
                                    indexOf = TextUtils.indexOf(str2, "🏿");
                                    if (indexOf >= 0) {
                                        str = "_c5";
                                    } else {
                                        this.emojiAnimatedStickerColor = "";
                                        if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) && (i = indexOf + 2) < this.messageText.length()) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(str2.toString());
                                            CharSequence charSequence = this.messageText;
                                            sb.append(charSequence.subSequence(i, charSequence.length()).toString());
                                            str2 = sb.toString();
                                        }
                                        if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) || EmojiData.emojiColoredMap.contains(str2.toString())) {
                                            this.emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(str2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.emojiAnimatedStickerColor = str;
                    str2 = str2.subSequence(0, indexOf);
                    if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2.toString());
                        CharSequence charSequence2 = this.messageText;
                        sb2.append(charSequence2.subSequence(i, charSequence2.length()).toString());
                        str2 = sb2.toString();
                    }
                    if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor)) {
                    }
                    this.emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(str2);
                } else if (this.messageOwner.entities.size() == 1 && (this.messageOwner.entities.get(0) instanceof TLRPC$TL_messageEntityCustomEmoji)) {
                    try {
                        long j = ((TLRPC$TL_messageEntityCustomEmoji) this.messageOwner.entities.get(0)).document_id;
                        this.emojiAnimatedStickerId = Long.valueOf(j);
                        TLRPC$Document findDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, j);
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
                TextPaint textPaint = getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
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
        TLRPC$Photo tLRPC$Photo;
        File pathToAttach;
        this.attachPathExists = false;
        this.mediaExists = false;
        int i2 = this.type;
        if (i2 == 20) {
            TLRPC$TL_messageExtendedMediaPreview tLRPC$TL_messageExtendedMediaPreview = (TLRPC$TL_messageExtendedMediaPreview) this.messageOwner.media.extended_media.get(0);
            if (tLRPC$TL_messageExtendedMediaPreview.thumb != null) {
                File pathToAttach2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$TL_messageExtendedMediaPreview.thumb, z);
                if (!this.mediaExists) {
                    exists = pathToAttach2.exists() || (tLRPC$TL_messageExtendedMediaPreview.thumb instanceof TLRPC$TL_photoStrippedSize);
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
        if (this.mediaExists) {
            return;
        }
        TLObject document = getDocument();
        if (document != null) {
            if (!isWallpaper()) {
                pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, false, z);
            }
            pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, true, z);
        } else {
            int i3 = this.type;
            if (i3 == 0) {
                document = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
                if (document == null) {
                    return;
                }
                pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, true, z);
            } else if (i3 != 11 || (tLRPC$Photo = this.messageOwner.action.photo) == null || tLRPC$Photo.video_sizes.isEmpty()) {
                return;
            } else {
                pathToAttach = FileLoader.getInstance(this.currentAccount).getPathToAttach((TLObject) tLRPC$Photo.video_sizes.get(0), null, true, z);
            }
        }
        this.mediaExists = pathToAttach.exists();
    }

    public void copyStableParams(MessageObject messageObject) {
        ArrayList<TextLayoutBlock> arrayList;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        ArrayList arrayList2;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        this.stableId = messageObject.stableId;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        tLRPC$Message.premiumEffectWasPlayed = messageObject.messageOwner.premiumEffectWasPlayed;
        this.forcePlayEffect = messageObject.forcePlayEffect;
        this.wasJustSent = messageObject.wasJustSent;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions2 = tLRPC$Message.reactions;
        if (tLRPC$TL_messageReactions2 != null && (arrayList2 = tLRPC$TL_messageReactions2.results) != null && !arrayList2.isEmpty() && (tLRPC$TL_messageReactions = messageObject.messageOwner.reactions) != null && tLRPC$TL_messageReactions.results != null) {
            for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
                TLRPC$ReactionCount tLRPC$ReactionCount = (TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i);
                for (int i2 = 0; i2 < messageObject.messageOwner.reactions.results.size(); i2++) {
                    TLRPC$ReactionCount tLRPC$ReactionCount2 = (TLRPC$ReactionCount) messageObject.messageOwner.reactions.results.get(i2);
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

    public void createMediaThumbs() {
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        TLRPC$PhotoSize closestPhotoSizeWithSize2;
        ImageLocation forDocument;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        if (isStoryMedia()) {
            TL_stories$StoryItem tL_stories$StoryItem = getMedia(this.messageOwner).storyItem;
            if (tL_stories$StoryItem == null || (tLRPC$MessageMedia = tL_stories$StoryItem.media) == null) {
                return;
            }
            TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
            if (tLRPC$Document != null) {
                TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 50);
                this.mediaThumb = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 320, false, null, true), tLRPC$Document);
                forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize3, tLRPC$Document);
            } else {
                closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 50);
                closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 320, false, closestPhotoSizeWithSize, true);
                this.mediaThumb = ImageLocation.getForObject(closestPhotoSizeWithSize2, this.photoThumbsObject);
                forDocument = ImageLocation.getForObject(closestPhotoSizeWithSize, this.photoThumbsObject);
            }
        } else if (isVideo()) {
            TLRPC$Document document = getDocument();
            TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
            this.mediaThumb = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320), document);
            forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize4, document);
        } else if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || getMedia(this.messageOwner).photo == null || this.photoThumbs.isEmpty()) {
            return;
        } else {
            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 50);
            closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 320, false, closestPhotoSizeWithSize, false);
            this.mediaThumb = ImageLocation.getForObject(closestPhotoSizeWithSize2, this.photoThumbsObject);
            forDocument = ImageLocation.getForObject(closestPhotoSizeWithSize, this.photoThumbsObject);
        }
        this.mediaSmallThumb = forDocument;
    }

    public void createMessageSendInfo() {
        HashMap hashMap;
        String str;
        VideoEditedInfo videoEditedInfo = this.videoEditedInfo;
        boolean z = videoEditedInfo != null && videoEditedInfo.notReadyYet;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.message != null) {
            if ((tLRPC$Message.id < 0 || isEditing()) && (hashMap = this.messageOwner.params) != null) {
                String str2 = (String) hashMap.get("ve");
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
                TLRPC$Message tLRPC$Message2 = this.messageOwner;
                if (tLRPC$Message2.send_state != 3 || (str = (String) tLRPC$Message2.params.get("prevMedia")) == null) {
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

    public boolean didSpoilLoginCode() {
        return this.spoiledLoginCode;
    }

    public boolean doesPaidReactionExist() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.reactions == null) {
            tLRPC$Message.reactions = new TLRPC$TL_messageReactions();
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            tLRPC$Message2.reactions.reactions_as_tags = getDialogId(tLRPC$Message2) == UserConfig.getInstance(this.currentAccount).getClientUserId();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (((TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i)).reaction instanceof TLRPC$TL_reactionPaid) {
                return true;
            }
        }
        return false;
    }

    public boolean ensurePaidReactionsExist(boolean z) {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.reactions == null) {
            tLRPC$Message.reactions = new TLRPC$TL_messageReactions();
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            tLRPC$Message2.reactions.reactions_as_tags = getDialogId(tLRPC$Message2) == UserConfig.getInstance(this.currentAccount).getClientUserId();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        TLRPC$ReactionCount tLRPC$ReactionCount = null;
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (((TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i)).reaction instanceof TLRPC$TL_reactionPaid) {
                tLRPC$ReactionCount = (TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i);
            }
        }
        if (tLRPC$ReactionCount == null) {
            TLRPC$TL_reactionCount tLRPC$TL_reactionCount = new TLRPC$TL_reactionCount();
            tLRPC$TL_reactionCount.reaction = new TLRPC$TL_reactionPaid();
            tLRPC$TL_reactionCount.count = 1;
            tLRPC$TL_reactionCount.chosen = z;
            this.messageOwner.reactions.results.add(0, tLRPC$TL_reactionCount);
            return true;
        }
        return false;
    }

    public boolean equals(MessageObject messageObject) {
        return messageObject != null && getId() == messageObject.getId() && getDialogId() == messageObject.getDialogId();
    }

    public void expandChannelRecommendations(boolean z) {
        this.channelJoinedExpanded = z;
        MessagesController.getInstance(this.currentAccount).getMainSettings().edit().putBoolean("c" + getDialogId() + "_rec", z).apply();
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
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
        TL_stories$StoryItem tL_stories$StoryItem;
        if ((this.caption == null || this.translated != this.captionTranslated) && !isRoundVideo()) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            String str = tLRPC$Message.message;
            ArrayList arrayList = tLRPC$Message.entities;
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
                    boolean z3 = z || (!(this.messageOwner.send_state != 0 ? false : arrayList.isEmpty() ^ true) && (this.eventId != 0 || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto_old) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto_layer68) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto_layer74) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument_old) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument_layer68) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument_layer74) || ((isOut() && this.messageOwner.send_state != 0) || this.messageOwner.id < 0)));
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
                    } else if (!isMusic() && !isVoice()) {
                        return;
                    } else {
                        isOutOwner = isOutOwner();
                        charSequence = this.caption;
                        duration = (int) getDuration();
                        i = 4;
                    }
                    addUrlsByPattern(isOutOwner, charSequence, true, i, duration, false);
                    return;
                }
                arrayList = new ArrayList();
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

    public void generateGameMessageText(TLRPC$User tLRPC$User) {
        CharSequence replaceWithLink;
        if (tLRPC$User == null && isFromUser()) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
        }
        MessageObject messageObject = this.replyMessageObject;
        TLRPC$TL_game tLRPC$TL_game = (messageObject == null || getMedia(messageObject) == null || getMedia(this.replyMessageObject).game == null) ? null : getMedia(this.replyMessageObject).game;
        if (tLRPC$TL_game == null) {
            replaceWithLink = (tLRPC$User == null || tLRPC$User.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) ? replaceWithLink(LocaleController.formatString("ActionUserScored", R.string.ActionUserScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", tLRPC$User) : LocaleController.formatString("ActionYouScored", R.string.ActionYouScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
        } else {
            this.messageText = (tLRPC$User == null || tLRPC$User.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) ? replaceWithLink(LocaleController.formatString("ActionUserScoredInGame", R.string.ActionUserScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", tLRPC$User) : LocaleController.formatString("ActionYouScoredInGame", R.string.ActionYouScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
            replaceWithLink = replaceWithLink(this.messageText, "un2", tLRPC$TL_game);
        }
        this.messageText = replaceWithLink;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:266|(3:267|268|269)|270|(1:272)(11:302|(1:304)|274|275|276|(1:278)|279|(2:281|(3:283|(5:287|288|(1:293)|290|291)|292))(1:299)|298|(1:297)(6:285|287|288|(0)|290|291)|292)|273|274|275|276|(0)|279|(0)(0)|298|(0)(0)|292) */
    /* JADX WARN: Can't wrap try/catch for region: R(48:144|(1:146)|147|(1:149)(1:419)|150|(1:152)(1:418)|153|(1:155)|(1:157)|(1:417)(1:162)|163|(1:416)(1:170)|171|(2:173|(3:(1:398)|399|400)(1:176))(2:401|(8:403|(1:405)(1:415)|406|(1:408)(1:414)|409|(1:411)(1:413)|412|400))|177|(3:179|(1:181)(1:(1:394)(1:395))|182)(1:396)|183|(1:185)(2:389|(1:391)(1:392))|186|(5:188|(1:365)(8:194|(1:196)(1:364)|197|198|(1:200)(1:363)|201|(1:203)(1:362)|204)|205|(3:207|(2:209|(2:211|(1:213))(1:215))(1:216)|214)|217)(3:366|(2:368|369)(8:370|371|372|(1:383)(1:376)|377|378|(1:380)(1:382)|381)|331)|218|219|220|221|(2:225|226)|358|232|233|234|(1:236)(17:352|(1:354)|238|(1:240)|241|(1:243)|244|(3:246|(7:248|249|250|251|252|254|255)|261)|262|(6:264|(16:266|267|268|269|270|(1:272)(11:302|(1:304)|274|275|276|(1:278)|279|(2:281|(3:283|(5:287|288|(1:293)|290|291)|292))(1:299)|298|(1:297)(6:285|287|288|(0)|290|291)|292)|273|274|275|276|(0)|279|(0)(0)|298|(0)(0)|292)|307|308|(2:(1:311)|312)(1:(1:338))|313)(3:339|(5:341|(1:343)(1:350)|344|(1:346)(1:349)|347)(1:351)|348)|314|(3:316|(1:318)(1:320)|319)|321|(1:336)(3:325|(1:327)(3:332|(1:334)|335)|328)|329|330|331)|237|238|(0)|241|(0)|244|(0)|262|(0)(0)|314|(0)|321|(1:323)|336|329|330|331|142) */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x053d, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x053e, code lost:
        r11 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x0550, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0551, code lost:
        org.telegram.messenger.FileLog.e(r0);
        r0 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x05f6, code lost:
        r13 = 0.0f;
     */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x023c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0243  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0245  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0257  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x02b3  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x02b6  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x02f1  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0559  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x0562  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x0578  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x057d  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x0597  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x05bf  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x05ff  */
    /* JADX WARN: Removed duplicated region for block: B:373:0x0606  */
    /* JADX WARN: Removed duplicated region for block: B:377:0x061b  */
    /* JADX WARN: Removed duplicated region for block: B:380:0x0630  */
    /* JADX WARN: Removed duplicated region for block: B:394:0x068d  */
    /* JADX WARN: Removed duplicated region for block: B:409:0x06d7  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x0705  */
    /* JADX WARN: Removed duplicated region for block: B:432:0x0755  */
    /* JADX WARN: Removed duplicated region for block: B:489:0x063e A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:491:0x063e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x014a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLayout(TLRPC$User tLRPC$User) {
        boolean z;
        int dp;
        SpannableStringBuilder spannableStringBuilder;
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
        CharSequence charSequence;
        float f2;
        float dp3;
        int ceil;
        int i5;
        StaticLayout staticLayout;
        int i6;
        Text text;
        int i7;
        StaticLayout staticLayout2;
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
        TLRPC$Message tLRPC$Message = this.messageOwner;
        boolean z6 = tLRPC$Message != null && tLRPC$Message.noforwards;
        if (z6) {
            z = z6;
        } else {
            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()));
            z = chat != null && chat.noforwards;
        }
        this.textLayoutBlocks = new ArrayList<>();
        this.textWidth = 0;
        CharSequence charSequence2 = this.messageText;
        this.hasCode = (charSequence2 instanceof Spanned) && ((CodeHighlighting.Span[]) ((Spanned) charSequence2).getSpans(0, charSequence2.length(), CodeHighlighting.Span.class)).length > 0;
        CharSequence charSequence3 = this.messageText;
        this.hasQuote = (charSequence3 instanceof Spanned) && ((QuoteSpan.QuoteStyleSpan[]) ((Spanned) charSequence3).getSpans(0, charSequence3.length(), QuoteSpan.QuoteStyleSpan.class)).length > 0;
        this.hasSingleQuote = false;
        this.hasSingleCode = false;
        CharSequence charSequence4 = this.messageText;
        if (charSequence4 instanceof Spanned) {
            Spanned spanned = (Spanned) charSequence4;
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
                TextPaint textPaint3 = !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) ? Theme.chat_msgGameTextPaint : Theme.chat_msgTextPaint;
                CharSequence charSequence5 = this.messageText;
                StaticLayout makeStaticLayout = makeStaticLayout(charSequence5, textPaint3, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f, this.emojiOnlyCount <= 0);
                spannableStringBuilder = charSequence5;
                if (this.isRepostPreview) {
                    int i13 = this.type != 0 ? hasValidGroupId() ? 7 : 12 : 22;
                    if (isWebpage()) {
                        i13 -= 8;
                    }
                    spannableStringBuilder = charSequence5;
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
                        while (lineEnd >= lineStart && !Character.isWhitespace(charSequence5.charAt(lineEnd))) {
                            lineEnd--;
                        }
                        SpannableStringBuilder append = new SpannableStringBuilder(charSequence5.subSequence(0, lineEnd)).append((CharSequence) "… ").append((CharSequence) string);
                        append.setSpan(new CharacterStyle() { // from class: org.telegram.messenger.MessageObject.2
                            @Override // android.text.style.CharacterStyle
                            public void updateDrawState(TextPaint textPaint4) {
                                textPaint4.setColor(Theme.chat_msgTextPaint.linkColor);
                            }
                        }, append.length() - string.length(), append.length(), 33);
                        try {
                            makeStaticLayout = makeStaticLayout(append, textPaint3, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount >= 4 ? -1.0f : 0.0f, this.emojiOnlyCount > 0);
                            spannableStringBuilder = append;
                        } catch (Exception e) {
                            FileLog.e(e);
                            return;
                        }
                    }
                }
                if (!this.hasSingleQuote) {
                    f = 32.0f;
                } else if (!this.hasSingleCode) {
                    i = maxMessageTextWidth;
                    int lineCount = makeStaticLayout.getLineCount();
                    int i16 = this.totalAnimatedEmojiCount;
                    int i17 = i16 >= 50 ? 5 : 10;
                    z2 = Build.VERSION.SDK_INT < 24 && i16 < 50;
                    int ceil3 = z2 ? 1 : (int) Math.ceil(lineCount / i17);
                    arrayList = new ArrayList();
                    if (!(spannableStringBuilder instanceof Spanned) && (this.hasQuote || this.hasCode)) {
                        cutIntoRanges(spannableStringBuilder, arrayList);
                    } else if (!z2 || ceil3 == 1) {
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
                        CharSequence charSequence6 = spannableStringBuilder;
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
                            CharSequence subSequence = charSequence6.subSequence(textRange.start, textRange.end);
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
                                    charSequence = charSequence6;
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
                                        charSequence = charSequence6;
                                        FileLog.e(e2);
                                    }
                                }
                                i4++;
                                z = z5;
                                textPaint3 = textPaint2;
                                arrayList = arrayList2;
                                charSequence6 = charSequence;
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
                                    charSequence6 = charSequence;
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
                                    CharSequence charSequence7 = charSequence6;
                                    while (i27 < lineCount2) {
                                        int i29 = lineCount2;
                                        try {
                                            f3 = textLayoutBlock.textLayout.getLineWidth(i27);
                                            arrayList3 = arrayList;
                                        } catch (Exception unused3) {
                                            arrayList3 = arrayList;
                                            f3 = 0.0f;
                                        }
                                        CharSequence charSequence8 = charSequence7;
                                        if (textLayoutBlock.quote) {
                                            f5 = 32.0f;
                                        } else {
                                            f5 = textLayoutBlock.code ? 15.0f : 15.0f;
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
                                                    charSequence7 = charSequence8;
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
                                            charSequence7 = charSequence8;
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
                                        charSequence7 = charSequence8;
                                        i = i8;
                                    }
                                    int i30 = i;
                                    arrayList2 = arrayList;
                                    charSequence = charSequence7;
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
                                    charSequence = charSequence6;
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
                                    this.textWidth = (int) Math.max(this.textWidth, Math.min(text.getCurrentWidth() + AndroidUtilities.dp(15.0f), textLayoutBlock.textLayout == null ? 0.0f : staticLayout2.getWidth()));
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
                                charSequence6 = charSequence;
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
                            charSequence6 = charSequence;
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
                    } else {
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
                    CharSequence charSequence62 = spannableStringBuilder;
                    while (i4 < arrayList.size()) {
                    }
                    if (this.hasCode) {
                    }
                    z4 = false;
                    this.hasWideCode = z4;
                    this.factCheckText = null;
                    return;
                } else {
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
                if (!(spannableStringBuilder instanceof Spanned)) {
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
                CharSequence charSequence622 = spannableStringBuilder;
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
            StaticLayout makeStaticLayout3 = makeStaticLayout(charSequence5, textPaint3, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f, this.emojiOnlyCount <= 0);
            spannableStringBuilder = charSequence5;
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
            if (!(spannableStringBuilder instanceof Spanned)) {
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
            CharSequence charSequence6222 = spannableStringBuilder;
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
        if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame)) {
        }
        CharSequence charSequence52 = this.messageText;
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:79:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLinkDescription() {
        boolean z;
        int i;
        Spannable.Factory factory;
        String str;
        TLRPC$TL_webPageAttributeStory tLRPC$TL_webPageAttributeStory;
        TL_stories$StoryItem tL_stories$StoryItem;
        if (this.linkDescription != null) {
            return;
        }
        TLRPC$WebPage tLRPC$WebPage = this.storyMentionWebpage;
        if (tLRPC$WebPage == null) {
            tLRPC$WebPage = getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage ? ((TLRPC$TL_messageMediaWebPage) getMedia(this.messageOwner)).webpage : null;
        }
        if (tLRPC$WebPage != null) {
            for (int i2 = 0; i2 < tLRPC$WebPage.attributes.size(); i2++) {
                TLRPC$WebPageAttribute tLRPC$WebPageAttribute = (TLRPC$WebPageAttribute) tLRPC$WebPage.attributes.get(i2);
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
            }
            if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) && getMedia(this.messageOwner).game.description != null) {
                factory = Spannable.Factory.getInstance();
                str = getMedia(this.messageOwner).game.description;
            } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice) && getMedia(this.messageOwner).description != null) {
                factory = Spannable.Factory.getInstance();
                str = getMedia(this.messageOwner).description;
            }
            this.linkDescription = factory.newSpannable(str);
        }
        i = 0;
        if (TextUtils.isEmpty(this.linkDescription)) {
        }
    }

    public void generatePaymentSentMessageText(TLRPC$User tLRPC$User) {
        String str;
        if (tLRPC$User == null) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
        }
        String firstName = tLRPC$User != null ? UserObject.getFirstName(tLRPC$User) : "";
        try {
            if ("XTR".equals(this.messageOwner.action.currency)) {
                str = "XTR " + this.messageOwner.action.total_amount;
            } else {
                LocaleController localeController = LocaleController.getInstance();
                TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
                str = localeController.formatCurrencyString(tLRPC$MessageAction.total_amount, tLRPC$MessageAction.currency);
            }
        } catch (Exception e) {
            FileLog.e(e);
            str = "<error>";
        }
        MessageObject messageObject = this.replyMessageObject;
        this.messageText = (messageObject == null || !(getMedia(messageObject) instanceof TLRPC$TL_messageMediaInvoice)) ? this.messageOwner.action.recurring_init ? LocaleController.formatString(R.string.PaymentSuccessfullyPaidNoItemRecurrent, str, firstName) : LocaleController.formatString("PaymentSuccessfullyPaidNoItem", R.string.PaymentSuccessfullyPaidNoItem, str, firstName) : this.messageOwner.action.recurring_init ? LocaleController.formatString(R.string.PaymentSuccessfullyPaidRecurrent, str, firstName, getMedia(this.replyMessageObject).title) : LocaleController.formatString("PaymentSuccessfullyPaid", R.string.PaymentSuccessfullyPaid, str, firstName, getMedia(this.replyMessageObject).title);
        this.messageText = StarsIntroActivity.replaceStars(this.messageText);
    }

    /* JADX WARN: Code restructure failed: missing block: B:128:0x025b, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x0264, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x026d, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0270, code lost:
        r8 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0271, code lost:
        r8 = replaceWithLink(r0, "un1", r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0071, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0083, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0095, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00a7, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b9, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00db, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00ef, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0103, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0117, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x012b, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x014d, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0157, code lost:
        if (r8 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x016b, code lost:
        if (r8 != null) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generatePinMessageText(TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat) {
        CharSequence string;
        CharSequence charSequence;
        boolean z;
        MessagesController messagesController;
        long j;
        if (tLRPC$User == null && tLRPC$Chat == 0) {
            if (isFromUser()) {
                tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
            }
            if (tLRPC$User == null) {
                TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
                if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                    messagesController = MessagesController.getInstance(this.currentAccount);
                    j = this.messageOwner.peer_id.channel_id;
                } else if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                    messagesController = MessagesController.getInstance(this.currentAccount);
                    j = this.messageOwner.peer_id.chat_id;
                }
                tLRPC$Chat = messagesController.getChat(Long.valueOf(j));
            }
        }
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if (!(tLRPC$Message instanceof TLRPC$TL_messageEmpty) && !(tLRPC$Message.action instanceof TLRPC$TL_messageActionHistoryClear)) {
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
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaDocument) {
                    string = LocaleController.getString(R.string.ActionPinnedFile);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaGeo) {
                    string = LocaleController.getString(R.string.ActionPinnedGeo);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaGeoLive) {
                    string = LocaleController.getString(R.string.ActionPinnedGeoLive);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaContact) {
                    string = LocaleController.getString(R.string.ActionPinnedContact);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaPoll) {
                    if (((TLRPC$TL_messageMediaPoll) getMedia(this.replyMessageObject)).poll.quiz) {
                        string = LocaleController.getString(R.string.ActionPinnedQuiz);
                    } else {
                        string = LocaleController.getString(R.string.ActionPinnedPoll);
                    }
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaPhoto) {
                    string = LocaleController.getString(R.string.ActionPinnedPhoto);
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaPaidMedia) {
                    charSequence = LocaleController.formatPluralString("NotificationPinnedPaidMedia", (int) ((TLRPC$TL_messageMediaPaidMedia) getMedia(this.replyMessageObject)).stars_amount, tLRPC$Chat != 0 ? tLRPC$Chat.title : UserObject.getUserName(tLRPC$User));
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaGame) {
                    int i = R.string.ActionPinnedGame;
                    String formatString = LocaleController.formatString("ActionPinnedGame", i, "🎮 " + getMedia(this.replyMessageObject).game.title);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    CharSequence replaceWithLink = replaceWithLink(formatString, "un1", tLRPC$User);
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
                        SpannableStringBuilder spannableStringBuilder = replaceEmoji;
                        if (z) {
                            boolean z2 = replaceEmoji instanceof SpannableStringBuilder;
                            spannableStringBuilder = replaceEmoji;
                            if (z2) {
                                ((SpannableStringBuilder) replaceEmoji).append((CharSequence) "...");
                                spannableStringBuilder = replaceEmoji;
                            } else if (replaceEmoji != null) {
                                spannableStringBuilder = new SpannableStringBuilder(replaceEmoji).append((CharSequence) "...");
                            }
                        }
                        string = AndroidUtilities.formatSpannable(LocaleController.getString(R.string.ActionPinnedText), spannableStringBuilder);
                    }
                }
                this.messageText = charSequence;
                return;
            }
        }
        string = LocaleController.getString(R.string.ActionPinnedNoText);
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:0x0198, code lost:
        if (r8.isEmpty() == false) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x02d2, code lost:
        if (r8.isEmpty() == false) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x02d4, code lost:
        r8 = r7.photoThumbs;
        r1 = r0.thumbs;
        r0 = r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateThumbs(boolean z) {
        TLObject tLObject;
        ArrayList<TLRPC$PhotoSize> arrayList;
        ArrayList<TLRPC$PhotoSize> arrayList2;
        ArrayList<TLRPC$PhotoSize> arrayList3;
        ArrayList<TLRPC$PhotoSize> arrayList4;
        ArrayList<TLRPC$PhotoSize> arrayList5;
        ArrayList<TLRPC$PhotoSize> arrayList6;
        TLRPC$Photo tLRPC$Photo;
        TLRPC$Photo tLRPC$Photo2;
        TLRPC$Document tLRPC$Document;
        TLRPC$Document tLRPC$Document2;
        TLRPC$Document tLRPC$Document3;
        ArrayList<TLRPC$PhotoSize> arrayList7;
        ArrayList<TLRPC$PhotoSize> arrayList8;
        ArrayList<TLRPC$PhotoSize> arrayList9;
        ArrayList<TLRPC$PhotoSize> arrayList10;
        ArrayList<TLRPC$PhotoSize> arrayList11;
        if (!hasExtendedMediaPreview()) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message instanceof TLRPC$TL_messageService) {
                TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto)) {
                    return;
                }
                TLRPC$Photo tLRPC$Photo3 = tLRPC$MessageAction.photo;
                if (z) {
                    ArrayList<TLRPC$PhotoSize> arrayList12 = this.photoThumbs;
                    if (arrayList12 != null && !arrayList12.isEmpty()) {
                        for (int i = 0; i < this.photoThumbs.size(); i++) {
                            TLRPC$PhotoSize tLRPC$PhotoSize = this.photoThumbs.get(i);
                            int i2 = 0;
                            while (true) {
                                if (i2 < tLRPC$Photo3.sizes.size()) {
                                    TLRPC$PhotoSize tLRPC$PhotoSize2 = (TLRPC$PhotoSize) tLRPC$Photo3.sizes.get(i2);
                                    if (!(tLRPC$PhotoSize2 instanceof TLRPC$TL_photoSizeEmpty) && tLRPC$PhotoSize2.type.equals(tLRPC$PhotoSize.type)) {
                                        tLRPC$PhotoSize.location = tLRPC$PhotoSize2.location;
                                        break;
                                    }
                                    i2++;
                                }
                            }
                        }
                    }
                } else {
                    this.photoThumbs = new ArrayList<>(tLRPC$Photo3.sizes);
                }
                if (tLRPC$Photo3.dc_id != 0 && (arrayList11 = this.photoThumbs) != null) {
                    int size = arrayList11.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        TLRPC$FileLocation tLRPC$FileLocation = this.photoThumbs.get(i3).location;
                        if (tLRPC$FileLocation != null) {
                            tLRPC$FileLocation.dc_id = tLRPC$Photo3.dc_id;
                            tLRPC$FileLocation.file_reference = tLRPC$Photo3.file_reference;
                        }
                    }
                }
                tLObject = this.messageOwner.action.photo;
            } else if (this.emojiAnimatedSticker == null && this.emojiAnimatedStickerId == null) {
                if (getMedia(tLRPC$Message) == null || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaEmpty)) {
                    TLRPC$MessageMedia tLRPC$MessageMedia = this.sponsoredMedia;
                    if (tLRPC$MessageMedia != null) {
                        TLRPC$Photo tLRPC$Photo4 = tLRPC$MessageMedia.photo;
                        TLRPC$Document tLRPC$Document4 = tLRPC$MessageMedia.document;
                        if (tLRPC$Photo4 != null) {
                            if (!z || (arrayList4 = this.photoThumbs) == null) {
                                this.photoThumbs = new ArrayList<>(tLRPC$Photo4.sizes);
                                tLRPC$Photo2 = tLRPC$Photo4;
                            } else {
                                tLRPC$Photo2 = tLRPC$Photo4;
                                if (!arrayList4.isEmpty()) {
                                    arrayList5 = this.photoThumbs;
                                    arrayList6 = tLRPC$Photo4.sizes;
                                    tLRPC$Photo = tLRPC$Photo4;
                                    updatePhotoSizeLocations(arrayList5, arrayList6);
                                    tLRPC$Photo2 = tLRPC$Photo;
                                }
                            }
                            this.photoThumbsObject = tLRPC$Photo2;
                            return;
                        } else if (tLRPC$Document4 == null || !isDocumentHasThumb(tLRPC$Document4)) {
                            return;
                        } else {
                            if (z) {
                                ArrayList<TLRPC$PhotoSize> arrayList13 = this.photoThumbs;
                                tLRPC$Document3 = tLRPC$Document4;
                                if (arrayList13 != null) {
                                    tLRPC$Document3 = tLRPC$Document4;
                                    tLRPC$Document2 = tLRPC$Document4;
                                }
                            } else {
                                arrayList3 = new ArrayList<>();
                                tLRPC$Document = tLRPC$Document4;
                                this.photoThumbs = arrayList3;
                                arrayList3.addAll(tLRPC$Document.thumbs);
                                tLRPC$Document3 = tLRPC$Document;
                            }
                        }
                    } else if (this.sponsoredPhoto == null) {
                        return;
                    } else {
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
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                    TLRPC$Photo tLRPC$Photo5 = getMedia(this.messageOwner).photo;
                    if (z && ((arrayList10 = this.photoThumbs) == null || arrayList10.size() == tLRPC$Photo5.sizes.size())) {
                        ArrayList<TLRPC$PhotoSize> arrayList14 = this.photoThumbs;
                        if (arrayList14 != null && !arrayList14.isEmpty()) {
                            for (int i4 = 0; i4 < this.photoThumbs.size(); i4++) {
                                TLRPC$PhotoSize tLRPC$PhotoSize3 = this.photoThumbs.get(i4);
                                if (tLRPC$PhotoSize3 != null) {
                                    int i5 = 0;
                                    while (true) {
                                        if (i5 >= tLRPC$Photo5.sizes.size()) {
                                            break;
                                        }
                                        TLRPC$PhotoSize tLRPC$PhotoSize4 = (TLRPC$PhotoSize) tLRPC$Photo5.sizes.get(i5);
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
                    } else {
                        this.photoThumbs = new ArrayList<>(tLRPC$Photo5.sizes);
                    }
                    tLObject = getMedia(this.messageOwner).photo;
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) {
                    TLRPC$Document document = getDocument();
                    if (!isDocumentHasThumb(document)) {
                        return;
                    }
                    if (!z || (r8 = this.photoThumbs) == null) {
                        arrayList3 = new ArrayList<>();
                        tLRPC$Document = document;
                        this.photoThumbs = arrayList3;
                        arrayList3.addAll(tLRPC$Document.thumbs);
                        tLRPC$Document3 = tLRPC$Document;
                    } else {
                        tLRPC$Document3 = document;
                        tLRPC$Document2 = document;
                    }
                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                    TLRPC$Document tLRPC$Document5 = getMedia(this.messageOwner).game.document;
                    if (tLRPC$Document5 != null && isDocumentHasThumb(tLRPC$Document5)) {
                        if (z) {
                            ArrayList<TLRPC$PhotoSize> arrayList15 = this.photoThumbs;
                            if (arrayList15 != null && !arrayList15.isEmpty()) {
                                updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document5.thumbs);
                            }
                        } else {
                            ArrayList<TLRPC$PhotoSize> arrayList16 = new ArrayList<>();
                            this.photoThumbs = arrayList16;
                            arrayList16.addAll(tLRPC$Document5.thumbs);
                        }
                        this.photoThumbsObject = tLRPC$Document5;
                    }
                    TLRPC$Photo tLRPC$Photo6 = getMedia(this.messageOwner).game.photo;
                    if (tLRPC$Photo6 != null) {
                        if (!z || (arrayList9 = this.photoThumbs2) == null) {
                            this.photoThumbs2 = new ArrayList<>(tLRPC$Photo6.sizes);
                        } else if (!arrayList9.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs2, tLRPC$Photo6.sizes);
                        }
                        this.photoThumbsObject2 = tLRPC$Photo6;
                    }
                    if (this.photoThumbs != null || (arrayList8 = this.photoThumbs2) == null) {
                        return;
                    }
                    this.photoThumbs = arrayList8;
                    this.photoThumbs2 = null;
                    this.photoThumbsObject = this.photoThumbsObject2;
                    this.photoThumbsObject2 = null;
                    return;
                } else if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage)) {
                    return;
                } else {
                    TLRPC$Photo tLRPC$Photo7 = getMedia(this.messageOwner).webpage.photo;
                    TLRPC$Document tLRPC$Document6 = getMedia(this.messageOwner).webpage.document;
                    if (tLRPC$Photo7 == 0) {
                        if (tLRPC$Document6 == 0 || !isDocumentHasThumb(tLRPC$Document6)) {
                            return;
                        }
                        if (z) {
                            ArrayList<TLRPC$PhotoSize> arrayList17 = this.photoThumbs;
                            tLRPC$Photo2 = tLRPC$Document6;
                            if (arrayList17 != null) {
                                tLRPC$Photo2 = tLRPC$Document6;
                                if (!arrayList17.isEmpty()) {
                                    arrayList5 = this.photoThumbs;
                                    arrayList6 = tLRPC$Document6.thumbs;
                                    tLRPC$Photo = tLRPC$Document6;
                                    updatePhotoSizeLocations(arrayList5, arrayList6);
                                    tLRPC$Photo2 = tLRPC$Photo;
                                }
                            }
                        } else {
                            ArrayList<TLRPC$PhotoSize> arrayList18 = new ArrayList<>();
                            this.photoThumbs = arrayList18;
                            arrayList18.addAll(tLRPC$Document6.thumbs);
                            tLRPC$Photo2 = tLRPC$Document6;
                        }
                        this.photoThumbsObject = tLRPC$Photo2;
                        return;
                    } else if (!z || (arrayList7 = this.photoThumbs) == null) {
                        this.photoThumbs = new ArrayList<>(tLRPC$Photo7.sizes);
                        tLRPC$Document3 = tLRPC$Photo7;
                    } else {
                        tLRPC$Document3 = tLRPC$Photo7;
                        if (!arrayList7.isEmpty()) {
                            ArrayList<TLRPC$PhotoSize> arrayList19 = this.photoThumbs;
                            ArrayList<TLRPC$PhotoSize> arrayList20 = tLRPC$Photo7.sizes;
                            TLRPC$Document tLRPC$Document7 = tLRPC$Photo7;
                            updatePhotoSizeLocations(arrayList19, arrayList20);
                            tLRPC$Document3 = tLRPC$Document7;
                        }
                    }
                }
                this.photoThumbsObject = tLRPC$Document3;
                return;
            } else if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) || !isDocumentHasThumb(this.emojiAnimatedSticker)) {
                return;
            } else {
                if (!z || (arrayList = this.photoThumbs) == null) {
                    ArrayList<TLRPC$PhotoSize> arrayList21 = new ArrayList<>();
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
        TLRPC$TL_messageExtendedMediaPreview tLRPC$TL_messageExtendedMediaPreview = (TLRPC$TL_messageExtendedMediaPreview) this.messageOwner.media.extended_media.get(0);
        if (z) {
            updatePhotoSizeLocations(this.photoThumbs, Collections.singletonList(tLRPC$TL_messageExtendedMediaPreview.thumb));
        } else {
            this.photoThumbs = new ArrayList<>(Collections.singletonList(tLRPC$TL_messageExtendedMediaPreview.thumb));
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
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        int min2;
        int i2 = this.type;
        int i3 = 0;
        if (i2 == 0) {
            int textHeight = textHeight();
            if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage)) {
                i3 = AndroidUtilities.dp(100.0f);
            }
            int i4 = textHeight + i3;
            return isReply() ? i4 + AndroidUtilities.dp(42.0f) : i4;
        } else if (i2 == 20) {
            return AndroidUtilities.getPhotoSize();
        } else {
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
                    int i6 = (int) (closestPhotoSizeWithSize.h / (closestPhotoSizeWithSize.w / i5));
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
            TLRPC$Document document = getDocument();
            if (document != null) {
                int size = document.attributes.size();
                for (int i7 = 0; i7 < size; i7++) {
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i7);
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) {
                        i3 = tLRPC$DocumentAttribute.w;
                        i = tLRPC$DocumentAttribute.h;
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

    public long getChannelId() {
        return getChannelId(this.messageOwner);
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
            if (((TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i)).chosen) {
                arrayList.add(ReactionsLayoutInBubble.VisibleReaction.fromTL(((TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i)).reaction));
            }
        }
        return arrayList;
    }

    public long getDialogId() {
        return getDialogId(this.messageOwner);
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

    public TLRPC$Document getDocument() {
        TLRPC$Document tLRPC$Document = this.emojiAnimatedSticker;
        return tLRPC$Document != null ? tLRPC$Document : getDocument(this.messageOwner);
    }

    public String getDocumentName() {
        return FileLoader.getDocumentFileName(getDocument());
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

    public TLRPC$TL_availableEffect getEffect() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (tLRPC$Message.flags2 & 4) == 0) {
            return null;
        }
        return MessagesController.getInstance(this.currentAccount).getEffect(this.messageOwner.effect);
    }

    public long getEffectId() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (tLRPC$Message.flags2 & 4) == 0) {
            return 0L;
        }
        return tLRPC$Message.effect;
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

    public TLRPC$TL_factCheck getFactCheck() {
        return FactCheckController.getInstance(this.currentAccount).getFactCheck(this);
    }

    public CharSequence getFactCheckText() {
        if (isFactCheckable()) {
            TLRPC$TL_factCheck factCheck = getFactCheck();
            if (factCheck == null || factCheck.text == null) {
                this.factCheckText = null;
                return null;
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(factCheck.text.text);
            addEntitiesToText(spannableStringBuilder, factCheck.text.entities, isOutOwner(), false, false, false);
            this.factCheckText = spannableStringBuilder;
            return spannableStringBuilder;
        }
        return null;
    }

    public String getFileName() {
        return getFileName(this.messageOwner);
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

    public long getFromChatId() {
        return getFromChatId(this.messageOwner);
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

    public TLRPC$InputStickerSet getInputStickerSet() {
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
        } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_background".equals(getMedia(this.messageOwner).webpage.type)) {
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
            if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                dp -= AndroidUtilities.dp(10.0f);
            }
        }
        int i = this.emojiOnlyCount;
        if (i >= 1) {
            int i2 = this.totalAnimatedEmojiCount;
            if (i2 <= 100) {
                return i - i2 < (SharedConfig.getDevicePerformanceClass() < 2 ? 50 : 100) ? (hasValidReplyMessageObject() || isForwarded()) ? Math.min(dp, (int) (this.generatedWithMinSize * 0.65f)) : dp : dp;
            }
            return dp;
        }
        return dp;
    }

    public int getMediaExistanceFlags() {
        boolean z = this.attachPathExists;
        return this.mediaExists ? (z ? 1 : 0) | 2 : z ? 1 : 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x00e2, code lost:
        if (isVideoDocument(r4.document) != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00ef, code lost:
        if ((((org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) r5).flags & 4) != 0) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00f1, code lost:
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0164, code lost:
        if (r2 != null) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0168, code lost:
        if (r8.ttl_seconds == 0) goto L108;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CharSequence getMediaTitle(TLRPC$MessageMedia tLRPC$MessageMedia) {
        String publicUsername;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveaway) {
            return LocaleController.getString(R.string.BoostingGiveaway);
        }
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveawayResults) {
            return LocaleController.getString(R.string.BoostingGiveawayResults);
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
            return LocaleController.getString(R.string.ForwardedStory);
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDice) {
            return getDiceEmoji();
        } else {
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                return ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll.quiz ? LocaleController.getString(R.string.QuizPoll) : LocaleController.getString(R.string.Poll);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) {
                TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia;
                int size = tLRPC$TL_messageMediaPaidMedia.extended_media.size();
                boolean z = false;
                for (int i = 0; i < size; i++) {
                    TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$TL_messageMediaPaidMedia.extended_media.get(i);
                    if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                        TLRPC$MessageMedia tLRPC$MessageMedia2 = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media;
                        if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) {
                        }
                        z = false;
                    } else if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) {
                    }
                    if (z) {
                        break;
                    }
                }
                return StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.AttachPaidMedia, size == 1 ? LocaleController.getString(z ? R.string.AttachVideo : R.string.AttachPhoto) : LocaleController.formatPluralString(z ? "Media" : "Photos", size, new Object[0])));
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                return (tLRPC$MessageMedia.ttl_seconds == 0 || (this.messageOwner instanceof TLRPC$TL_message_secret)) ? getGroupId() != 0 ? LocaleController.getString(R.string.Album) : LocaleController.getString(R.string.AttachPhoto) : LocaleController.getString(R.string.AttachDestructingPhoto);
            } else {
                if (tLRPC$MessageMedia != null) {
                    if (!isVideoDocument(tLRPC$MessageMedia.document)) {
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                            TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
                            if (!(tLRPC$Document instanceof TLRPC$TL_documentEmpty)) {
                            }
                        }
                    }
                    return (tLRPC$MessageMedia.ttl_seconds == 0 || (this.messageOwner instanceof TLRPC$TL_message_secret)) ? LocaleController.getString(R.string.AttachVideo) : tLRPC$MessageMedia.voice ? LocaleController.getString(R.string.AttachVoiceExpired) : tLRPC$MessageMedia.round ? LocaleController.getString(R.string.AttachRoundExpired) : LocaleController.getString(R.string.AttachDestructingVideo);
                }
                if (tLRPC$MessageMedia == null || !isVoiceDocument(tLRPC$MessageMedia.document)) {
                    if (tLRPC$MessageMedia == null || !isRoundVideoDocument(tLRPC$MessageMedia.document)) {
                        if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue)) {
                            return LocaleController.getString(R.string.AttachLocation);
                        }
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                            return LocaleController.getString(R.string.AttachLiveLocation);
                        }
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaContact) {
                            return LocaleController.getString(R.string.AttachContact);
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
                                return (stickerChar == null || stickerChar.length() <= 0) ? LocaleController.getString(R.string.AttachSticker) : String.format("%s %s", stickerChar, LocaleController.getString(R.string.AttachSticker));
                            } else if (isMusic()) {
                                return LocaleController.getString(R.string.AttachMusic);
                            } else {
                                if (isGif()) {
                                    return LocaleController.getString(R.string.AttachGif);
                                }
                                String documentFileName = FileLoader.getDocumentFileName(tLRPC$MessageMedia.document);
                                return !TextUtils.isEmpty(documentFileName) ? documentFileName : LocaleController.getString(R.string.AttachDocument);
                            }
                        }
                        return null;
                    }
                    return LocaleController.getString(R.string.AttachRound);
                }
                return LocaleController.getString(R.string.AttachAudio);
            }
        }
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

    public String getMusicAuthor() {
        return getMusicAuthor(true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0038, code lost:
        if (r3.round_message != false) goto L10;
     */
    /* JADX WARN: Removed duplicated region for block: B:23:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x010e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x011f A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String getMusicAuthor(boolean z) {
        int i;
        MessagesController messagesController;
        TLRPC$Peer tLRPC$Peer;
        MessagesController messagesController2;
        TLRPC$Peer tLRPC$Peer2;
        TLRPC$Peer tLRPC$Peer3;
        String str;
        TLRPC$Chat tLRPC$Chat;
        long j;
        TLRPC$Document document = getDocument();
        if (document != null) {
            boolean z2 = false;
            for (int i2 = 0; i2 < document.attributes.size(); i2++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i2);
                if (!(tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio)) {
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    }
                    if (!z2) {
                        TLRPC$User tLRPC$User = null;
                        if (z) {
                            if (!isOutOwner()) {
                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                                if (tLRPC$MessageFwdHeader != null) {
                                    TLRPC$Peer tLRPC$Peer4 = tLRPC$MessageFwdHeader.from_id;
                                    if ((tLRPC$Peer4 instanceof TLRPC$TL_peerUser) && tLRPC$Peer4.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    }
                                }
                                TLRPC$Message tLRPC$Message = this.messageOwner;
                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2 = tLRPC$Message.fwd_from;
                                if (tLRPC$MessageFwdHeader2 == null || !(tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerChannel)) {
                                    if (tLRPC$MessageFwdHeader2 == null || !(tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerChat)) {
                                        if (tLRPC$MessageFwdHeader2 != null && (tLRPC$MessageFwdHeader2.from_id instanceof TLRPC$TL_peerUser)) {
                                            messagesController = MessagesController.getInstance(this.currentAccount);
                                            tLRPC$Peer = this.messageOwner.fwd_from.from_id;
                                        } else if (tLRPC$MessageFwdHeader2 != null && (str = tLRPC$MessageFwdHeader2.from_name) != null) {
                                            return str;
                                        } else {
                                            TLRPC$Peer tLRPC$Peer5 = tLRPC$Message.from_id;
                                            if (tLRPC$Peer5 instanceof TLRPC$TL_peerChat) {
                                                messagesController2 = MessagesController.getInstance(this.currentAccount);
                                                tLRPC$Peer3 = this.messageOwner.from_id;
                                            } else if (tLRPC$Peer5 instanceof TLRPC$TL_peerChannel) {
                                                messagesController2 = MessagesController.getInstance(this.currentAccount);
                                                tLRPC$Peer2 = this.messageOwner.from_id;
                                            } else if (tLRPC$Peer5 != null || tLRPC$Message.peer_id.channel_id == 0) {
                                                messagesController = MessagesController.getInstance(this.currentAccount);
                                                tLRPC$Peer = this.messageOwner.from_id;
                                            } else {
                                                messagesController2 = MessagesController.getInstance(this.currentAccount);
                                                tLRPC$Peer2 = this.messageOwner.peer_id;
                                            }
                                        }
                                        TLRPC$User user = messagesController.getUser(Long.valueOf(tLRPC$Peer.user_id));
                                        tLRPC$Chat = null;
                                        tLRPC$User = user;
                                        if (tLRPC$User != null) {
                                            return UserObject.getUserName(tLRPC$User);
                                        }
                                        if (tLRPC$Chat != null) {
                                            return tLRPC$Chat.title;
                                        }
                                    } else {
                                        messagesController2 = MessagesController.getInstance(this.currentAccount);
                                        tLRPC$Peer3 = this.messageOwner.fwd_from.from_id;
                                    }
                                    j = tLRPC$Peer3.chat_id;
                                    tLRPC$Chat = messagesController2.getChat(Long.valueOf(j));
                                    if (tLRPC$User != null) {
                                    }
                                } else {
                                    messagesController2 = MessagesController.getInstance(this.currentAccount);
                                    tLRPC$Peer2 = this.messageOwner.fwd_from.from_id;
                                }
                                j = tLRPC$Peer2.channel_id;
                                tLRPC$Chat = messagesController2.getChat(Long.valueOf(j));
                                if (tLRPC$User != null) {
                                }
                            }
                            i = R.string.FromYou;
                            break;
                        }
                        return null;
                    }
                } else if (!tLRPC$DocumentAttribute.voice) {
                    String str2 = tLRPC$DocumentAttribute.performer;
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
                        return (TextUtils.isEmpty(documentFileName) && z) ? LocaleController.getString(R.string.AudioUnknownTitle) : documentFileName;
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
        return LocaleController.getString(R.string.AudioUnknownTitle);
    }

    public TLObject getPeerObject() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message != null) {
            TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
            if ((tLRPC$Peer instanceof TLRPC$TL_peerChannel_layer131) || (tLRPC$Peer instanceof TLRPC$TL_peerChannel)) {
                return MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.channel_id));
            }
            if ((tLRPC$Peer instanceof TLRPC$TL_peerUser_layer131) || (tLRPC$Peer instanceof TLRPC$TL_peerUser)) {
                return MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.peer_id.user_id));
            }
            if ((tLRPC$Peer instanceof TLRPC$TL_peerChat_layer131) || (tLRPC$Peer instanceof TLRPC$TL_peerChat)) {
                return MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.chat_id));
            }
            return null;
        }
        return null;
    }

    public long getPollId() {
        if (this.type != 17) {
            return 0L;
        }
        return ((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.id;
    }

    public TLRPC$VideoSize getPremiumStickerAnimation() {
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

    public TLRPC$MessagePeerReaction getRandomUnreadReaction() {
        ArrayList arrayList;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        if (tLRPC$TL_messageReactions == null || (arrayList = tLRPC$TL_messageReactions.recent_reactions) == null || arrayList.isEmpty()) {
            return null;
        }
        return (TLRPC$MessagePeerReaction) this.messageOwner.reactions.recent_reactions.get(0);
    }

    public int getRealId() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        int i = tLRPC$Message.realId;
        return i != 0 ? i : tLRPC$Message.id;
    }

    public int getRepliesCount() {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        if (tLRPC$MessageReplies != null) {
            return tLRPC$MessageReplies.replies;
        }
        return 0;
    }

    public int getReplyAnyMsgId() {
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$MessageReplyHeader != null) {
            int i = tLRPC$MessageReplyHeader.reply_to_top_id;
            return i != 0 ? i : tLRPC$MessageReplyHeader.reply_to_msg_id;
        }
        return 0;
    }

    public int getReplyMsgId() {
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = this.messageOwner.reply_to;
        if (tLRPC$MessageReplyHeader != null) {
            return tLRPC$MessageReplyHeader.reply_to_msg_id;
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0058, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_peerUser) != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0061, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_peerUser) != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0063, code lost:
        r0 = peerNameWithIcon(r10.currentAccount, r5, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006a, code lost:
        r0 = peerNameWithIcon(r10.currentAccount, r5, r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CharSequence getReplyQuoteNameWithIcon() {
        CharSequence charSequence;
        CharSequence append;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null) {
            return "";
        }
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = tLRPC$Message.reply_to;
        CharSequence charSequence2 = null;
        if (tLRPC$MessageReplyHeader != null) {
            if (tLRPC$MessageReplyHeader.reply_from != null) {
                TLRPC$Peer tLRPC$Peer = tLRPC$MessageReplyHeader.reply_to_peer_id;
                boolean z = tLRPC$Peer == null || DialogObject.getPeerDialogId(tLRPC$Peer) != getDialogId();
                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.reply_to.reply_from;
                TLRPC$Peer tLRPC$Peer2 = tLRPC$MessageFwdHeader.from_id;
                if (tLRPC$Peer2 == null) {
                    tLRPC$Peer2 = tLRPC$MessageFwdHeader.saved_from_peer;
                    if (tLRPC$Peer2 == null) {
                        if (!TextUtils.isEmpty(tLRPC$MessageFwdHeader.from_name)) {
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
        TLRPC$Peer tLRPC$Peer3 = this.messageOwner.reply_to.reply_to_peer_id;
        if (tLRPC$Peer3 != null && DialogObject.getPeerDialogId(tLRPC$Peer3) != getDialogId()) {
            TLRPC$Peer tLRPC$Peer4 = this.messageOwner.reply_to.reply_to_peer_id;
            if (tLRPC$Peer4 instanceof TLRPC$TL_peerUser) {
                charSequence2 = peerNameWithIcon(this.currentAccount, tLRPC$Peer4, true);
            } else {
                charSequence = peerNameWithIcon(this.currentAccount, tLRPC$Peer4);
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

    public long getSavedDialogId() {
        return getSavedDialogId(UserConfig.getInstance(this.currentAccount).getClientUserId(), this.messageOwner);
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

    public long getSenderId() {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
        if (tLRPC$MessageFwdHeader == null || (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) == null) {
            TLRPC$Peer tLRPC$Peer2 = tLRPC$Message.from_id;
            if (tLRPC$Peer2 instanceof TLRPC$TL_peerUser) {
                return tLRPC$Peer2.user_id;
            }
            if (tLRPC$Peer2 instanceof TLRPC$TL_peerChannel) {
                return -tLRPC$Peer2.channel_id;
            }
            if (tLRPC$Peer2 instanceof TLRPC$TL_peerChat) {
                return -tLRPC$Peer2.chat_id;
            }
            if (tLRPC$Message.post) {
                return tLRPC$Message.peer_id.channel_id;
            }
        } else {
            long j = tLRPC$Peer.user_id;
            if (j != 0) {
                TLRPC$Peer tLRPC$Peer3 = tLRPC$MessageFwdHeader.from_id;
                return tLRPC$Peer3 instanceof TLRPC$TL_peerUser ? tLRPC$Peer3.user_id : j;
            } else if (tLRPC$Peer.channel_id != 0) {
                if (isSavedFromMegagroup()) {
                    TLRPC$Peer tLRPC$Peer4 = this.messageOwner.fwd_from.from_id;
                    if (tLRPC$Peer4 instanceof TLRPC$TL_peerUser) {
                        return tLRPC$Peer4.user_id;
                    }
                }
                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2 = this.messageOwner.fwd_from;
                TLRPC$Peer tLRPC$Peer5 = tLRPC$MessageFwdHeader2.from_id;
                return tLRPC$Peer5 instanceof TLRPC$TL_peerChannel ? -tLRPC$Peer5.channel_id : tLRPC$Peer5 instanceof TLRPC$TL_peerChat ? -tLRPC$Peer5.chat_id : -tLRPC$MessageFwdHeader2.saved_from_peer.channel_id;
            } else {
                long j2 = tLRPC$Peer.chat_id;
                if (j2 != 0) {
                    TLRPC$Peer tLRPC$Peer6 = tLRPC$MessageFwdHeader.from_id;
                    return tLRPC$Peer6 instanceof TLRPC$TL_peerUser ? tLRPC$Peer6.user_id : tLRPC$Peer6 instanceof TLRPC$TL_peerChannel ? -tLRPC$Peer6.channel_id : tLRPC$Peer6 instanceof TLRPC$TL_peerChat ? -tLRPC$Peer6.chat_id : -j2;
                }
            }
        }
        return 0L;
    }

    public long getSize() {
        return getMessageSize(this.messageOwner);
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

    public int getUnradFlags() {
        return getUnreadFlags(this.messageOwner);
    }

    public CharSequence getVoiceTranscription() {
        String str;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (str = tLRPC$Message.voiceTranscription) == null) {
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
                    arrayList.add(getMessageObjectForBlock(tLRPC$WebPage, (TLRPC$PageBlock) tLRPC$TL_pageBlockSlideshow.items.get(i2)));
                }
            } else if (tLRPC$PageBlock instanceof TLRPC$TL_pageBlockCollage) {
                TLRPC$TL_pageBlockCollage tLRPC$TL_pageBlockCollage = (TLRPC$TL_pageBlockCollage) tLRPC$PageBlock;
                for (int i3 = 0; i3 < tLRPC$TL_pageBlockCollage.items.size(); i3++) {
                    arrayList.add(getMessageObjectForBlock(tLRPC$WebPage, (TLRPC$PageBlock) tLRPC$TL_pageBlockCollage.items.get(i3)));
                }
            }
        }
        return arrayList;
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

    public boolean hasEntitiesFromServer() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || tLRPC$Message.entities == null) {
            return false;
        }
        for (int i = 0; i < this.messageOwner.entities.size(); i++) {
            TLRPC$MessageEntity tLRPC$MessageEntity = (TLRPC$MessageEntity) this.messageOwner.entities.get(i);
            if ((tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPhone) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBankCard)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasExtendedMedia() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) || tLRPC$MessageMedia.extended_media.isEmpty()) ? false : true;
    }

    public boolean hasExtendedMediaPreview() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) || tLRPC$MessageMedia.extended_media.isEmpty() || !(this.messageOwner.media.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMediaPreview)) ? false : true;
    }

    public boolean hasHighlightedWords() {
        ArrayList<String> arrayList = this.highlightedWords;
        return (arrayList == null || arrayList.isEmpty()) ? false : true;
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

    public boolean hasLinkMediaToMakeSmall() {
        boolean z = !this.isRestrictedMessage && (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage);
        TLRPC$WebPage tLRPC$WebPage = z ? getMedia(this.messageOwner).webpage : null;
        String str = tLRPC$WebPage != null ? tLRPC$WebPage.type : null;
        if (!z || isGiveawayOrGiveawayResults() || tLRPC$WebPage == null) {
            return false;
        }
        if (tLRPC$WebPage.photo != null || isVideoDocument(tLRPC$WebPage.document)) {
            return ((TextUtils.isEmpty(tLRPC$WebPage.description) && TextUtils.isEmpty(tLRPC$WebPage.title)) || isSponsored() || "telegram_megagroup".equals(str) || "telegram_background".equals(str) || "telegram_voicechat".equals(str) || "telegram_videochat".equals(str) || "telegram_livestream".equals(str) || "telegram_user".equals(str) || "telegram_story".equals(str) || "telegram_channel_boost".equals(str) || "telegram_group_boost".equals(str) || "telegram_chat".equals(str)) ? false : true;
        }
        return false;
    }

    public boolean hasMediaSpoilers() {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        return (!this.isRepostPreview && (((tLRPC$MessageMedia = this.messageOwner.media) != null && tLRPC$MessageMedia.spoiler) || needDrawBluredPreview())) || isHiddenSensitive();
    }

    public boolean hasPaidMediaPreview() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return tLRPC$MessageMedia != null && (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) && !tLRPC$MessageMedia.extended_media.isEmpty() && (this.messageOwner.media.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMediaPreview);
    }

    public boolean hasReaction(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        if (hasReactions() && visibleReaction != null) {
            for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
                if (visibleReaction.isSame(((TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i)).reaction)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasReactions() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        return (tLRPC$TL_messageReactions == null || tLRPC$TL_messageReactions.results.isEmpty()) ? false : true;
    }

    public boolean hasReplies() {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        return tLRPC$MessageReplies != null && tLRPC$MessageReplies.replies > 0;
    }

    public boolean hasRevealedExtendedMedia() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) || tLRPC$MessageMedia.extended_media.isEmpty() || !(this.messageOwner.media.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMedia)) ? false : true;
    }

    public boolean hasValidGroupId() {
        int i;
        if (getGroupId() != 0) {
            ArrayList<TLRPC$PhotoSize> arrayList = this.photoThumbs;
            if (arrayList != null && !arrayList.isEmpty()) {
                return true;
            }
            if ((this.sendPreview && ((i = this.type) == 3 || i == 1)) || isMusic() || isDocument()) {
                return true;
            }
        }
        return false;
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

    public boolean isAlbumSingle() {
        return getMedia(this) instanceof TLRPC$TL_messageMediaPaidMedia;
    }

    public boolean isAndroidTheme() {
        if (getMedia(this.messageOwner) != null && getMedia(this.messageOwner).webpage != null && !getMedia(this.messageOwner).webpage.attributes.isEmpty()) {
            int size = getMedia(this.messageOwner).webpage.attributes.size();
            for (int i = 0; i < size; i++) {
                TLRPC$WebPageAttribute tLRPC$WebPageAttribute = (TLRPC$WebPageAttribute) getMedia(this.messageOwner).webpage.attributes.get(i);
                if (tLRPC$WebPageAttribute instanceof TLRPC$TL_webPageAttributeTheme) {
                    TLRPC$TL_webPageAttributeTheme tLRPC$TL_webPageAttributeTheme = (TLRPC$TL_webPageAttributeTheme) tLRPC$WebPageAttribute;
                    ArrayList arrayList = tLRPC$TL_webPageAttributeTheme.documents;
                    int size2 = arrayList.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        if ("application/x-tgtheme-android".equals(((TLRPC$Document) arrayList.get(i2)).mime_type)) {
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
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        return tLRPC$MessageReplies != null && tLRPC$MessageReplies.comments;
    }

    public boolean isContentUnread() {
        return this.messageOwner.media_unread;
    }

    public boolean isCurrentWallpaper() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$UserFull userFull;
        TLRPC$WallPaper tLRPC$WallPaper;
        return (!isWallpaperAction() || (tLRPC$Message = this.messageOwner) == null || (tLRPC$MessageAction = tLRPC$Message.action) == null || tLRPC$MessageAction.wallpaper == null || (userFull = MessagesController.getInstance(this.currentAccount).getUserFull(getDialogId())) == null || (tLRPC$WallPaper = userFull.wallpaper) == null || !userFull.wallpaper_overridden || this.messageOwner.action.wallpaper.id != tLRPC$WallPaper.id) ? false : true;
    }

    public boolean isDice() {
        return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDice;
    }

    public boolean isDocument() {
        return (getDocument() == null || isVideo() || isMusic() || isVoice() || isAnyKindOfSticker()) ? false : true;
    }

    public boolean isEditing() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.send_state == 3 && tLRPC$Message.id > 0;
    }

    public boolean isEditingMedia() {
        return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto ? getMedia(this.messageOwner).photo.id == 0 : (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) && getMedia(this.messageOwner).document.dc_id == 0;
    }

    public boolean isExpiredLiveLocation(int i) {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.date + getMedia(tLRPC$Message).period <= i;
    }

    public boolean isExpiredStory() {
        int i = this.type;
        return (i == 23 || i == 24) && (this.messageOwner.media.storyItem instanceof TL_stories$TL_storyItemDeleted);
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

    public boolean isFromUser() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return (tLRPC$Message.from_id instanceof TLRPC$TL_peerUser) && !tLRPC$Message.post;
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
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        return tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.imported;
    }

    public boolean isInvoice() {
        return isInvoiceMessage(this.messageOwner);
    }

    public boolean isLinkMediaSmall() {
        TLRPC$WebPage tLRPC$WebPage = (!this.isRestrictedMessage && (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage)) ? getMedia(this.messageOwner).webpage : null;
        String str = tLRPC$WebPage != null ? tLRPC$WebPage.type : null;
        return !(tLRPC$WebPage != null && TextUtils.isEmpty(tLRPC$WebPage.description) && TextUtils.isEmpty(tLRPC$WebPage.title)) && ("app".equals(str) || "profile".equals(str) || "article".equals(str) || "telegram_bot".equals(str) || "telegram_user".equals(str) || "telegram_channel".equals(str) || "telegram_megagroup".equals(str) || "telegram_voicechat".equals(str) || "telegram_videochat".equals(str) || "telegram_livestream".equals(str) || "telegram_channel_boost".equals(str) || "telegram_group_boost".equals(str) || "telegram_chat".equals(str));
    }

    public boolean isLinkedToChat(long j) {
        TLRPC$MessageReplies tLRPC$MessageReplies = this.messageOwner.replies;
        return tLRPC$MessageReplies != null && (j == 0 || tLRPC$MessageReplies.channel_id == j);
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
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList arrayList;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (tLRPC$TL_messageReactions = tLRPC$Message.reactions) == null || (arrayList = tLRPC$TL_messageReactions.top_reactors) == null) {
            return null;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC$MessageReactor tLRPC$MessageReactor = (TLRPC$MessageReactor) it.next();
            if (tLRPC$MessageReactor != null && tLRPC$MessageReactor.my) {
                return Boolean.valueOf(tLRPC$MessageReactor.anonymous);
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

    /* JADX WARN: Code restructure failed: missing block: B:44:0x0081, code lost:
        if (r6.user_id == r2) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0093, code lost:
        if (r5.user_id == r2) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00a8, code lost:
        if (r0.user_id != r2) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00be, code lost:
        if (r5.user_id == r2) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00c2, code lost:
        if (r0.saved_out != false) goto L43;
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
            TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
            TLRPC$Chat tLRPC$Chat = null;
            if (tLRPC$Peer != null) {
                long j = tLRPC$Peer.channel_id;
                if (j != 0) {
                    tLRPC$Chat = getChat(null, null, j);
                }
            }
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message.out) {
                TLRPC$Peer tLRPC$Peer2 = tLRPC$Message.from_id;
                if ((tLRPC$Peer2 instanceof TLRPC$TL_peerUser) || ((tLRPC$Peer2 instanceof TLRPC$TL_peerChannel) && !ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat))) {
                    TLRPC$Message tLRPC$Message2 = this.messageOwner;
                    if (!tLRPC$Message2.post) {
                        if (tLRPC$Message2.fwd_from != null) {
                            if (getDialogId() == clientUserId) {
                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                                TLRPC$Peer tLRPC$Peer3 = tLRPC$MessageFwdHeader.from_id;
                                if (tLRPC$Peer3 instanceof TLRPC$TL_peerUser) {
                                    if (tLRPC$Peer3.user_id == clientUserId) {
                                        TLRPC$Peer tLRPC$Peer4 = tLRPC$MessageFwdHeader.saved_from_peer;
                                        if (tLRPC$Peer4 != null) {
                                        }
                                        valueOf = Boolean.valueOf(z);
                                    }
                                }
                                TLRPC$Peer tLRPC$Peer5 = tLRPC$MessageFwdHeader.saved_from_peer;
                                if (tLRPC$Peer5 != null) {
                                    if (tLRPC$Peer5.user_id == clientUserId) {
                                        if (tLRPC$Peer3 != null) {
                                        }
                                        valueOf = Boolean.valueOf(z);
                                    }
                                }
                                z = false;
                                valueOf = Boolean.valueOf(z);
                            } else {
                                TLRPC$Peer tLRPC$Peer6 = this.messageOwner.fwd_from.saved_from_peer;
                                if (tLRPC$Peer6 != null) {
                                }
                                valueOf = Boolean.valueOf(z);
                            }
                        }
                        valueOf = Boolean.TRUE;
                    }
                }
            }
            this.isOutOwnerCached = Boolean.FALSE;
            return false;
        }
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2 = this.messageOwner.fwd_from;
        if (tLRPC$MessageFwdHeader2 != null) {
            TLRPC$Peer tLRPC$Peer7 = tLRPC$MessageFwdHeader2.from_id;
            if (tLRPC$Peer7 != null) {
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
            if (((TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i)).reaction instanceof TLRPC$TL_reactionPaid) {
                return ((TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i)).chosen;
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
        return ((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.closed;
    }

    public boolean isPremiumSticker() {
        if (getMedia(this.messageOwner) == null || !getMedia(this.messageOwner).nopremium) {
            return isPremiumSticker(getDocument());
        }
        return false;
    }

    public boolean isPrivateForward() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        return (tLRPC$MessageFwdHeader == null || TextUtils.isEmpty(tLRPC$MessageFwdHeader.from_name)) ? false : true;
    }

    public boolean isPublicPoll() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.public_voters;
    }

    public boolean isQuickReply() {
        return isQuickReply(this.messageOwner);
    }

    public boolean isQuiz() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.quiz;
    }

    public boolean isReactionsAvailable() {
        return (isEditing() || isSponsored() || !isSent() || this.messageOwner.action != null || isExpiredStory()) ? false : true;
    }

    public boolean isReply() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader;
        MessageObject messageObject = this.replyMessageObject;
        return ((messageObject != null && (messageObject.messageOwner instanceof TLRPC$TL_messageEmpty)) || (tLRPC$MessageReplyHeader = (tLRPC$Message = this.messageOwner).reply_to) == null || (tLRPC$MessageReplyHeader.reply_to_msg_id == 0 && tLRPC$MessageReplyHeader.reply_to_random_id == 0) || (tLRPC$Message.flags & 8) == 0) ? false : true;
    }

    public boolean isReplyToStory() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader;
        MessageObject messageObject = this.replyMessageObject;
        return ((messageObject != null && (messageObject.messageOwner instanceof TLRPC$TL_messageEmpty)) || (tLRPC$MessageReplyHeader = (tLRPC$Message = this.messageOwner).reply_to) == null || tLRPC$MessageReplyHeader.story_id == 0 || (tLRPC$Message.flags & 8) == 0) ? false : true;
    }

    public boolean isRoundOnce() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        return isRoundVideo() && (tLRPC$Message = this.messageOwner) != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null && tLRPC$MessageMedia.ttl_seconds == Integer.MAX_VALUE;
    }

    public boolean isRoundVideo() {
        if (this.isRoundVideoCached == 0) {
            this.isRoundVideoCached = (this.type == 5 || isRoundVideoMessage(this.messageOwner)) ? 1 : 2;
        }
        return this.isRoundVideoCached == 1;
    }

    public boolean isSavedFromMegagroup() {
        TLRPC$Peer tLRPC$Peer;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
        if (tLRPC$MessageFwdHeader == null || (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) == null || tLRPC$Peer.channel_id == 0) {
            return false;
        }
        return ChatObject.isMegagroup(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.saved_from_peer.channel_id)));
    }

    public boolean isSecretMedia() {
        int i;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message instanceof TLRPC$TL_message_secret ? (((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) || isGif()) && (i = this.messageOwner.ttl) > 0 && i <= 60) || isVoice() || isRoundVideo() || isVideo() : (tLRPC$Message instanceof TLRPC$TL_message) && getMedia(tLRPC$Message) != null && getMedia(this.messageOwner).ttl_seconds != 0 && ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument));
    }

    public boolean isSendError() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return (tLRPC$Message.send_state == 2 && tLRPC$Message.id < 0) || (this.scheduled && tLRPC$Message.id > 0 && tLRPC$Message.date < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + (-60));
    }

    public boolean isSending() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.send_state == 1 && tLRPC$Message.id < 0;
    }

    public boolean isSensitive() {
        TLRPC$Chat chat;
        Boolean bool = this.isSensitiveCached;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (this.messageOwner != null && canBeSensitive()) {
            if (!this.messageOwner.restriction_reason.isEmpty()) {
                for (int i = 0; i < this.messageOwner.restriction_reason.size(); i++) {
                    TLRPC$RestrictionReason tLRPC$RestrictionReason = (TLRPC$RestrictionReason) this.messageOwner.restriction_reason.get(i);
                    if ("sensitive".equals(tLRPC$RestrictionReason.reason) && ("all".equals(tLRPC$RestrictionReason.platform) || (((!ApplicationLoader.isStandaloneBuild() && !BuildVars.isBetaApp()) || BuildVars.DEBUG_PRIVATE_VERSION) && "android".equals(tLRPC$RestrictionReason.platform)))) {
                        break;
                    }
                }
            }
            if (getDialogId() < 0 && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()))) != null && chat.restriction_reason != null) {
                for (int i2 = 0; i2 < chat.restriction_reason.size(); i2++) {
                    TLRPC$RestrictionReason tLRPC$RestrictionReason2 = (TLRPC$RestrictionReason) chat.restriction_reason.get(i2);
                    if (!"sensitive".equals(tLRPC$RestrictionReason2.reason) || (!"all".equals(tLRPC$RestrictionReason2.platform) && (((ApplicationLoader.isStandaloneBuild() || BuildVars.isBetaApp()) && !BuildVars.DEBUG_PRIVATE_VERSION) || !"android".equals(tLRPC$RestrictionReason2.platform)))) {
                    }
                    this.isSensitiveCached = Boolean.TRUE;
                    return true;
                }
            }
            this.isSensitiveCached = Boolean.FALSE;
            return false;
        }
        return false;
    }

    public boolean isSent() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message.send_state == 0 || tLRPC$Message.id > 0;
    }

    public boolean isSponsored() {
        return this.sponsoredId != null;
    }

    public boolean isSticker() {
        int i = this.type;
        return i != 1000 ? i == 13 : isStickerDocument(getDocument()) || isVideoSticker(getDocument());
    }

    public boolean isStory() {
        return this.storyItem != null;
    }

    public boolean isStoryMedia() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        return tLRPC$Message != null && (tLRPC$Message.media instanceof TLRPC$TL_messageMediaStory);
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
        TLRPC$Peer tLRPC$Peer = this.messageOwner.peer_id;
        if (tLRPC$Peer != null) {
            long j = tLRPC$Peer.channel_id;
            if (j != 0) {
                TLRPC$Chat chat = getChat(null, null, j);
                if (chat != null) {
                    boolean z = chat.megagroup;
                    this.cachedIsSupergroup = Boolean.valueOf(z);
                    return z;
                }
                return false;
            }
        }
        this.cachedIsSupergroup = Boolean.FALSE;
        return false;
    }

    public boolean isTheme() {
        return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_theme".equals(getMedia(this.messageOwner).webpage.type);
    }

    public boolean isUnread() {
        return this.messageOwner.unread;
    }

    public boolean isUnsupported() {
        return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaUnsupported;
    }

    public boolean isVideo() {
        return isVideoMessage(this.messageOwner);
    }

    public boolean isVideoAvatar() {
        TLRPC$Photo tLRPC$Photo;
        TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
        return (tLRPC$MessageAction == null || (tLRPC$Photo = tLRPC$MessageAction.photo) == null || tLRPC$Photo.video_sizes.isEmpty()) ? false : true;
    }

    public boolean isVideoCall() {
        TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
        return (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) && tLRPC$MessageAction.video;
    }

    public boolean isVideoSticker() {
        return getDocument() != null && isVideoStickerDocument(getDocument());
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

    public boolean isVoice() {
        return isVoiceMessage(this.messageOwner);
    }

    public boolean isVoiceOnce() {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        return isVoice() && (tLRPC$Message = this.messageOwner) != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null && tLRPC$MessageMedia.ttl_seconds == Integer.MAX_VALUE;
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

    public boolean isVoted() {
        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll;
        TLRPC$PollResults tLRPC$PollResults;
        if (this.type == 17 && (tLRPC$PollResults = (tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).results) != null && !tLRPC$PollResults.results.isEmpty()) {
            int size = tLRPC$TL_messageMediaPoll.results.results.size();
            for (int i = 0; i < size; i++) {
                if (((TLRPC$TL_pollAnswerVoters) tLRPC$TL_messageMediaPoll.results.results.get(i)).chosen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWallpaper() {
        return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && "telegram_background".equals(getMedia(this.messageOwner).webpage.type);
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

    public boolean isWebpage() {
        return getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage;
    }

    public boolean isWebpageDocument() {
        return (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) || getMedia(this.messageOwner).webpage.document == null || isGifDocument(getMedia(this.messageOwner).webpage.document)) ? false : true;
    }

    public boolean isYouTubeVideo() {
        return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && !TextUtils.isEmpty(getMedia(this.messageOwner).webpage.embed_url) && "YouTube".equals(getMedia(this.messageOwner).webpage.site_name);
    }

    public void loadAnimatedEmojiDocument() {
        if (this.emojiAnimatedSticker != null || this.emojiAnimatedStickerId == null || this.emojiAnimatedStickerLoading) {
            return;
        }
        this.emojiAnimatedStickerLoading = true;
        AnimatedEmojiDrawable.getDocumentFetcher(this.currentAccount).fetchDocument(this.emojiAnimatedStickerId.longValue(), new AnimatedEmojiDrawable.ReceivedDocument() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.ReceivedDocument
            public final void run(TLRPC$Document tLRPC$Document) {
                MessageObject.this.lambda$loadAnimatedEmojiDocument$1(tLRPC$Document);
            }
        });
    }

    public void markReactionsAsRead() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions = this.messageOwner.reactions;
        if (tLRPC$TL_messageReactions == null || tLRPC$TL_messageReactions.recent_reactions == null) {
            return;
        }
        boolean z = false;
        for (int i = 0; i < this.messageOwner.reactions.recent_reactions.size(); i++) {
            if (((TLRPC$MessagePeerReaction) this.messageOwner.reactions.recent_reactions.get(i)).unread) {
                ((TLRPC$MessagePeerReaction) this.messageOwner.reactions.recent_reactions.get(i)).unread = false;
                z = true;
            }
        }
        if (z) {
            MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            TLRPC$Message tLRPC$Message = this.messageOwner;
            messagesStorage.markMessageReactionsAsRead(tLRPC$Message.dialog_id, getTopicId(this.currentAccount, tLRPC$Message), this.messageOwner.id, true);
        }
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
            TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = (TLRPC$TL_keyboardButtonRow) this.messageOwner.reply_markup.rows.get(i);
            int size = tLRPC$TL_keyboardButtonRow.buttons.size();
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC$KeyboardButton tLRPC$KeyboardButton = (TLRPC$KeyboardButton) tLRPC$TL_keyboardButtonRow.buttons.get(i3);
                StringBuilder sb2 = this.botButtonsLayout;
                sb2.append(i);
                sb2.append(i3);
                if (!(tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonBuy) || (getMedia(this.messageOwner).flags & 4) == 0) {
                    String str = tLRPC$KeyboardButton.text;
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

    public boolean needDrawAvatar() {
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$Chat chat;
        if (this.type == 27) {
            return false;
        }
        if (this.isRepostPreview || this.isSaved || this.forceAvatar || this.customAvatarDrawable != null || this.searchType != 0) {
            return true;
        }
        boolean z = getDialogId() < 0 && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()))) != null && chat.signature_profiles;
        if (isSponsored()) {
            return false;
        }
        return isFromUser() || isFromGroup() || z || this.eventId != 0 || !((tLRPC$MessageFwdHeader = this.messageOwner.fwd_from) == null || tLRPC$MessageFwdHeader.saved_from_peer == null);
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

    /* JADX WARN: Code restructure failed: missing block: B:47:0x008e, code lost:
        if (r3.channel_id == r0.channel_id) goto L35;
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
                return peerDialogId == 0 ? savedDialogId >= 0 && savedDialogId != UserObject.ANONYMOUS : (savedDialogId == peerDialogId || peerDialogId == clientUserId) ? false : true;
            }
            if (((this.messageOwner.flags & 4) != 0 || (getMedia(this) instanceof TLRPC$TL_messageMediaPaidMedia)) && (tLRPC$MessageFwdHeader = this.messageOwner.fwd_from) != null && !tLRPC$MessageFwdHeader.imported) {
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
            if (this.searchType == 2) {
                return true;
            }
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
                        TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
                        if (user != null && user.bot && ("reviews_bot".equals(UserObject.getPublicUsername(user)) || "ReviewInsightsBot".equals(UserObject.getPublicUsername(user)))) {
                            return true;
                        }
                        if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaEmpty) || getMedia(this.messageOwner) == null || ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && !(getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage))) {
                            return false;
                        }
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

    public Spannable replaceAnimatedEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt) {
        TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities;
        return replaceAnimatedEmoji(charSequence, (!this.translated || (tLRPC$TL_textWithEntities = this.messageOwner.translatedText) == null) ? this.messageOwner.entities : tLRPC$TL_textWithEntities.entities, fontMetricsInt, false);
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
                    TLRPC$Document emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(charSequence2);
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

    /* JADX WARN: Removed duplicated region for block: B:15:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0086 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CharSequence replaceWithLink(CharSequence charSequence, String str, ArrayList<Long> arrayList, AbstractMap<Long, TLRPC$User> abstractMap, LongSparseArray longSparseArray) {
        TLRPC$User tLRPC$User;
        Object obj;
        if (TextUtils.indexOf(charSequence, str) >= 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("");
            for (int i = 0; i < arrayList.size(); i++) {
                if (abstractMap != null) {
                    obj = abstractMap.get(arrayList.get(i));
                } else if (longSparseArray != null) {
                    obj = longSparseArray.get(arrayList.get(i).longValue());
                } else {
                    tLRPC$User = null;
                    if (tLRPC$User == null) {
                        tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(arrayList.get(i));
                    }
                    if (tLRPC$User == null) {
                        String userName = UserObject.getUserName(tLRPC$User);
                        int length = spannableStringBuilder.length();
                        if (spannableStringBuilder.length() != 0) {
                            spannableStringBuilder.append((CharSequence) ", ");
                        }
                        spannableStringBuilder.append((CharSequence) userName);
                        spannableStringBuilder.setSpan(new URLSpanNoUnderlineBold("" + tLRPC$User.id), length, userName.length() + length, 33);
                    }
                }
                tLRPC$User = (TLRPC$User) obj;
                if (tLRPC$User == null) {
                }
                if (tLRPC$User == null) {
                }
            }
            return TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{spannableStringBuilder});
        }
        return charSequence;
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
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message.reactions == null) {
            tLRPC$Message.reactions = new TLRPC$TL_messageReactions();
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            tLRPC$Message2.reactions.reactions_as_tags = getDialogId(tLRPC$Message2) == UserConfig.getInstance(this.currentAccount).getClientUserId();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        ArrayList arrayList = new ArrayList();
        TLRPC$ReactionCount tLRPC$ReactionCount = null;
        int i2 = 0;
        for (int i3 = 0; i3 < this.messageOwner.reactions.results.size(); i3++) {
            TLRPC$ReactionCount tLRPC$ReactionCount2 = (TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i3);
            if (tLRPC$ReactionCount2.chosen && !(tLRPC$ReactionCount2.reaction instanceof TLRPC$TL_reactionPaid)) {
                arrayList.add(tLRPC$ReactionCount2);
                int i4 = tLRPC$ReactionCount2.chosen_order;
                if (i4 > i2) {
                    i2 = i4;
                }
            }
            TLRPC$Reaction tLRPC$Reaction = ((TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i3)).reaction;
            if (tLRPC$Reaction instanceof TLRPC$TL_reactionEmoji) {
                String str = visibleReaction.emojicon;
                if (str != null) {
                    if (((TLRPC$TL_reactionEmoji) tLRPC$Reaction).emoticon.equals(str)) {
                        tLRPC$ReactionCount = (TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i3);
                    }
                }
            }
            if (tLRPC$Reaction instanceof TLRPC$TL_reactionCustomEmoji) {
                long j = visibleReaction.documentId;
                if (j != 0 && ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction).document_id == j) {
                    tLRPC$ReactionCount = (TLRPC$ReactionCount) this.messageOwner.reactions.results.get(i3);
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
                int i5 = tLRPC$ReactionCount.count - 1;
                tLRPC$ReactionCount.count = i5;
                if (i5 <= 0) {
                    this.messageOwner.reactions.results.remove(tLRPC$ReactionCount);
                }
            }
            if (this.messageOwner.reactions.can_see_list) {
                int i6 = 0;
                while (i6 < this.messageOwner.reactions.recent_reactions.size()) {
                    if (getPeerId(((TLRPC$MessagePeerReaction) this.messageOwner.reactions.recent_reactions.get(i6)).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId() && ReactionsUtils.compare(((TLRPC$MessagePeerReaction) this.messageOwner.reactions.recent_reactions.get(i6)).reaction, visibleReaction)) {
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
                if (!(((TLRPC$ReactionCount) arrayList.get(i8)).reaction instanceof TLRPC$TL_reactionPaid) && ((TLRPC$ReactionCount) arrayList.get(i8)).chosen_order < ((TLRPC$ReactionCount) arrayList.get(i7)).chosen_order) {
                    i7 = i8;
                }
            }
            TLRPC$ReactionCount tLRPC$ReactionCount3 = (TLRPC$ReactionCount) arrayList.get(i7);
            tLRPC$ReactionCount3.chosen = false;
            int i9 = tLRPC$ReactionCount3.count - 1;
            tLRPC$ReactionCount3.count = i9;
            if (i9 <= 0) {
                this.messageOwner.reactions.results.remove(tLRPC$ReactionCount3);
            }
            arrayList.remove(tLRPC$ReactionCount3);
            if (this.messageOwner.reactions.can_see_list) {
                int i10 = 0;
                while (i10 < this.messageOwner.reactions.recent_reactions.size()) {
                    if (getPeerId(((TLRPC$MessagePeerReaction) this.messageOwner.reactions.recent_reactions.get(i10)).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId() && ReactionsUtils.compare(((TLRPC$MessagePeerReaction) this.messageOwner.reactions.recent_reactions.get(i10)).reaction, visibleReaction)) {
                        this.messageOwner.reactions.recent_reactions.remove(i10);
                        i10--;
                    }
                    i10++;
                }
            }
        }
        if (tLRPC$ReactionCount == null) {
            int chatMaxUniqReactions = MessagesController.getInstance(this.currentAccount).getChatMaxUniqReactions(getDialogId());
            TLRPC$Message tLRPC$Message3 = this.messageOwner;
            if (tLRPC$Message3 == null || (tLRPC$TL_messageReactions = tLRPC$Message3.reactions) == null) {
                i = 0;
            } else {
                Iterator it = tLRPC$TL_messageReactions.results.iterator();
                i = 0;
                while (it.hasNext()) {
                    if (!(((TLRPC$ReactionCount) it.next()).reaction instanceof TLRPC$TL_reactionPaid)) {
                        i++;
                    }
                }
            }
            if (i + 1 > chatMaxUniqReactions) {
                return false;
            }
            tLRPC$ReactionCount = new TLRPC$TL_reactionCount();
            tLRPC$ReactionCount.reaction = visibleReaction.toTLReaction();
            this.messageOwner.reactions.results.add(tLRPC$ReactionCount);
        }
        tLRPC$ReactionCount.chosen = true;
        tLRPC$ReactionCount.count++;
        tLRPC$ReactionCount.chosen_order = i2 + 1;
        TLRPC$Message tLRPC$Message4 = this.messageOwner;
        if (tLRPC$Message4.reactions.can_see_list || (tLRPC$Message4.dialog_id > 0 && maxUserReactionsCount > 1)) {
            TLRPC$TL_messagePeerReaction tLRPC$TL_messagePeerReaction = new TLRPC$TL_messagePeerReaction();
            TLRPC$Message tLRPC$Message5 = this.messageOwner;
            if (!tLRPC$Message5.isThreadMessage || tLRPC$Message5.fwd_from == null) {
                messagesController = MessagesController.getInstance(this.currentAccount);
                dialogId = getDialogId();
            } else {
                messagesController = MessagesController.getInstance(this.currentAccount);
                dialogId = getFromChatId();
            }
            tLRPC$TL_messagePeerReaction.peer_id = messagesController.getSendAsSelectedPeer(dialogId);
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

    public void setContentIsRead() {
        this.messageOwner.media_unread = false;
    }

    public void setIsRead() {
        this.messageOwner.unread = false;
    }

    public void setMyPaidReactionAnonymous(boolean z) {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList arrayList;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (tLRPC$TL_messageReactions = tLRPC$Message.reactions) == null || (arrayList = tLRPC$TL_messageReactions.top_reactors) == null) {
            return;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC$MessageReactor tLRPC$MessageReactor = (TLRPC$MessageReactor) it.next();
            if (tLRPC$MessageReactor != null && tLRPC$MessageReactor.my) {
                tLRPC$MessageReactor.anonymous = z;
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

    /* JADX WARN: Code restructure failed: missing block: B:184:0x0320, code lost:
        if (isAnimatedSticker() != false) goto L181;
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x0365, code lost:
        if (isSticker() != false) goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x036a, code lost:
        r5 = 15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0122, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_decryptedMessageActionSetMessageTTL) == false) goto L73;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setType() {
        int i;
        int i2;
        TLObject tLObject;
        TLRPC$WallPaper tLRPC$WallPaper;
        int i3 = this.type;
        this.type = 1000;
        this.isRoundVideoCached = 0;
        if (!isSponsored()) {
            i = 1;
            if (this.channelJoined) {
                this.type = 27;
                SharedPreferences mainSettings = MessagesController.getInstance(this.currentAccount).getMainSettings();
                this.channelJoinedExpanded = mainSettings.getBoolean("c" + getDialogId() + "_rec", true);
            } else {
                TLRPC$Message tLRPC$Message = this.messageOwner;
                if (!(tLRPC$Message instanceof TLRPC$TL_message) && !(tLRPC$Message instanceof TLRPC$TL_messageForwarded_old2)) {
                    TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent = this.currentEvent;
                    if (tLRPC$TL_channelAdminLogEvent != null) {
                        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction = tLRPC$TL_channelAdminLogEvent.action;
                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeWallpaper) {
                            TLRPC$TL_channelAdminLogEventActionChangeWallpaper tLRPC$TL_channelAdminLogEventActionChangeWallpaper = (TLRPC$TL_channelAdminLogEventActionChangeWallpaper) tLRPC$ChannelAdminLogEventAction;
                            this.contentType = 1;
                            TLRPC$WallPaper tLRPC$WallPaper2 = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value;
                            if (!(tLRPC$WallPaper2 instanceof TLRPC$TL_wallPaperNoFile) || tLRPC$WallPaper2.id != 0 || tLRPC$WallPaper2.settings != null) {
                                this.type = 22;
                                ArrayList<TLRPC$PhotoSize> arrayList = new ArrayList<>();
                                this.photoThumbs = arrayList;
                                TLRPC$Document tLRPC$Document = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                                if (tLRPC$Document != null) {
                                    arrayList.addAll(tLRPC$Document.thumbs);
                                    tLRPC$WallPaper = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value;
                                    tLObject = tLRPC$WallPaper.document;
                                    this.photoThumbsObject = tLObject;
                                }
                            }
                            i = 10;
                            this.type = i;
                        }
                    }
                    if (tLRPC$Message instanceof TLRPC$TL_messageService) {
                        TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                        if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetSameChatWallPaper)) {
                            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                                this.contentType = 1;
                                this.type = 22;
                                TLRPC$TL_messageActionSetChatWallPaper tLRPC$TL_messageActionSetChatWallPaper = (TLRPC$TL_messageActionSetChatWallPaper) tLRPC$MessageAction;
                                ArrayList<TLRPC$PhotoSize> arrayList2 = new ArrayList<>();
                                this.photoThumbs = arrayList2;
                                TLRPC$Document tLRPC$Document2 = tLRPC$TL_messageActionSetChatWallPaper.wallpaper.document;
                                if (tLRPC$Document2 != null) {
                                    arrayList2.addAll(tLRPC$Document2.thumbs);
                                    tLRPC$WallPaper = tLRPC$TL_messageActionSetChatWallPaper.wallpaper;
                                    tLObject = tLRPC$WallPaper.document;
                                    this.photoThumbsObject = tLObject;
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSuggestProfilePhoto) {
                                this.contentType = 1;
                                this.type = 21;
                                ArrayList<TLRPC$PhotoSize> arrayList3 = new ArrayList<>();
                                this.photoThumbs = arrayList3;
                                arrayList3.addAll(this.messageOwner.action.photo.sizes);
                                tLObject = this.messageOwner.action.photo;
                                this.photoThumbsObject = tLObject;
                            } else if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation)) {
                                boolean z = tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftCode;
                                if (z && ((TLRPC$TL_messageActionGiftCode) tLRPC$MessageAction).boost_peer != null) {
                                    this.contentType = 1;
                                    i = 25;
                                } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftPremium) || z) {
                                    this.contentType = 1;
                                    i = 18;
                                } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftStars) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPrizeStars)) {
                                    this.contentType = 1;
                                    i = 30;
                                } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto)) {
                                    this.contentType = 1;
                                    i = 11;
                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
                                    TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
                                    if (!(tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages)) {
                                    }
                                } else {
                                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear)) {
                                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
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
                        if (tLRPC$Message.media instanceof TLRPC$TL_messageMediaPaidMedia) {
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
                            if (getMedia(this.messageOwner).ttl_seconds == 0 || (!(getMedia(this.messageOwner).photo instanceof TLRPC$TL_photoEmpty) && !(getDocument() instanceof TLRPC$TL_documentEmpty) && ((!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) || getDocument() != null) && !this.forceExpired))) {
                                if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveaway) {
                                    i = 26;
                                } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveawayResults) {
                                    i = 28;
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
                                } else if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto)) {
                                    if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeo) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaVenue) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeoLive)) {
                                        i = 4;
                                    } else if (isRoundVideo()) {
                                        i = 5;
                                    } else if (isVideo()) {
                                        i = 3;
                                    } else if (isVoice()) {
                                        i = 2;
                                    } else if (isMusic()) {
                                        i = 14;
                                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaContact) {
                                        i = 12;
                                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPoll) {
                                        this.type = 17;
                                        this.checkedVotes = new ArrayList<>();
                                    } else if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaUnsupported)) {
                                        if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDocument) {
                                            TLRPC$Document document = getDocument();
                                            if (document != null && document.mime_type != null) {
                                                if (isGifDocument(document, hasValidGroupId())) {
                                                    i = 8;
                                                } else {
                                                    if (!isSticker()) {
                                                    }
                                                    i = 13;
                                                }
                                            }
                                            i = 9;
                                        } else if (!(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaInvoice)) {
                                            if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaStory) {
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

    public boolean updateTranslation() {
        return updateTranslation(false);
    }

    public boolean updateTranslation(boolean z) {
        String str;
        TLRPC$Message tLRPC$Message;
        MessageObject messageObject = this.replyMessageObject;
        boolean z2 = messageObject != null && messageObject.updateTranslation(z);
        TranslateController translateController = MessagesController.getInstance(this.currentAccount).getTranslateController();
        if (!TranslateController.isTranslatable(this) || !translateController.isTranslatingDialog(getDialogId()) || translateController.isTranslateDialogHidden(getDialogId()) || (tLRPC$Message = this.messageOwner) == null || tLRPC$Message.translatedText == null || !TextUtils.equals(translateController.getDialogTranslateTo(getDialogId()), this.messageOwner.translatedToLanguage)) {
            TLRPC$Message tLRPC$Message2 = this.messageOwner;
            if (tLRPC$Message2 == null || !(z || this.translated)) {
                return z2;
            }
            this.translated = false;
            str = tLRPC$Message2.message;
        } else if (this.translated) {
            return z2;
        } else {
            this.translated = true;
            str = this.messageOwner.translatedText.text;
        }
        applyNewText(str);
        generateCaption();
        return true;
    }
}
