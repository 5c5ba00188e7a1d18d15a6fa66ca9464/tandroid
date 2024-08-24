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

    public static void addPhoneLinks(CharSequence charSequence) {
    }

    public void checkForScam() {
    }

    public float getProgress() {
        return 0.0f;
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
        return (!this.isRepostPreview && (((tLRPC$MessageMedia = this.messageOwner.media) != null && tLRPC$MessageMedia.spoiler) || needDrawBluredPreview())) || isHiddenSensitive();
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
                    TLRPC$RestrictionReason tLRPC$RestrictionReason = this.messageOwner.restriction_reason.get(i);
                    if ("sensitive".equals(tLRPC$RestrictionReason.reason) && ("all".equals(tLRPC$RestrictionReason.platform) || (((!ApplicationLoader.isStandaloneBuild() && !BuildVars.isBetaApp()) || BuildVars.DEBUG_PRIVATE_VERSION) && "android".equals(tLRPC$RestrictionReason.platform)))) {
                        this.isSensitiveCached = Boolean.TRUE;
                        return true;
                    }
                }
            }
            if (getDialogId() < 0 && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-getDialogId()))) != null && chat.restriction_reason != null) {
                for (int i2 = 0; i2 < chat.restriction_reason.size(); i2++) {
                    TLRPC$RestrictionReason tLRPC$RestrictionReason2 = chat.restriction_reason.get(i2);
                    if ("sensitive".equals(tLRPC$RestrictionReason2.reason) && ("all".equals(tLRPC$RestrictionReason2.platform) || (((!ApplicationLoader.isStandaloneBuild() && !BuildVars.isBetaApp()) || BuildVars.DEBUG_PRIVATE_VERSION) && "android".equals(tLRPC$RestrictionReason2.platform)))) {
                        this.isSensitiveCached = Boolean.TRUE;
                        return true;
                    }
                }
            }
            this.isSensitiveCached = Boolean.FALSE;
            return false;
        }
        return false;
    }

    public boolean isHiddenSensitive() {
        return isSensitive() && !MessagesController.getInstance(this.currentAccount).showSensitiveContent();
    }

    public boolean canBeSensitive() {
        int i;
        return (this.messageOwner == null || ((i = this.type) != 1 && i != 3 && i != 9 && i != 8 && i != 5) || this.sendPreview || this.isRepostPreview || isOutOwner() || this.messageOwner.send_state != 0) ? false : true;
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
                arrayList.add(ReactionsLayoutInBubble.VisibleReaction.fromTL(this.messageOwner.reactions.results.get(i).reaction));
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
            String[] strArr;
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
                                if (indexOf >= 0) {
                                    strArr = new String[]{readLine.substring(0, indexOf), readLine.substring(indexOf + 1).trim()};
                                } else {
                                    strArr = new String[]{readLine.trim()};
                                }
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

    public static boolean expandedQuotesEquals(HashSet<Integer> hashSet, HashSet<Integer> hashSet2) {
        if (hashSet == null && hashSet2 == null) {
            return true;
        }
        return (hashSet == null ? 0 : hashSet.size()) == (hashSet2 == null ? 0 : hashSet2.size()) && hashSet != null && hashSet.equals(hashSet2);
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

        public int height() {
            return (this.quoteCollapse && collapsed()) ? this.collapsedHeight : this.height;
        }

        public int height(ChatMessageCell.TransitionParams transitionParams) {
            if (!this.quoteCollapse) {
                return this.height;
            }
            return AndroidUtilities.lerp(this.height, this.collapsedHeight, collapsed(transitionParams));
        }

        public float collapsed(ChatMessageCell.TransitionParams transitionParams) {
            boolean collapsed;
            if (transitionParams.animateExpandedQuotes) {
                HashSet<Integer> hashSet = transitionParams.animateExpandedQuotesFrom;
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

        /* JADX WARN: Code restructure failed: missing block: B:250:0x07b6, code lost:
            if (r15[2] > r15[3]) goto L303;
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x00ac, code lost:
            if ((org.telegram.messenger.MessageObject.getMedia(r15.messageOwner) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaInvoice) == false) goto L87;
         */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0068  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x006b  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x006e  */
        /* JADX WARN: Removed duplicated region for block: B:292:0x0870  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00c2  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x00ef  */
        /* JADX WARN: Removed duplicated region for block: B:67:0x00f2  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x00ff  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0106  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0121  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x013c  */
        /* JADX WARN: Removed duplicated region for block: B:89:0x0153  */
        /* JADX WARN: Removed duplicated region for block: B:92:0x0158  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x0161  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            int i;
            byte b;
            int i2;
            int i3;
            float f;
            int i4;
            int i5;
            MessageObject messageObject;
            int i6;
            TLRPC$Message tLRPC$Message;
            float f2;
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
                int i7 = this.reversed ? size - 1 : 0;
                boolean z = false;
                float f4 = 1.0f;
                boolean z2 = false;
                boolean z3 = false;
                boolean z4 = true;
                while (true) {
                    if (this.reversed) {
                        if (i7 < 0) {
                            break;
                        }
                        messageObject = this.messages.get(i7);
                        if (i7 != (!this.reversed ? size - 1 : 0)) {
                            messageObject.isOutOwnerCached = bool2;
                            z3 = messageObject.isOutOwner();
                            if (!z3) {
                                TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message2.fwd_from;
                                if (tLRPC$MessageFwdHeader != null && tLRPC$MessageFwdHeader.saved_from_peer != null) {
                                    i6 = size;
                                } else if (tLRPC$Message2.from_id instanceof TLRPC$TL_peerUser) {
                                    TLRPC$Peer tLRPC$Peer = tLRPC$Message2.peer_id;
                                    i6 = size;
                                    if (tLRPC$Peer.channel_id == 0) {
                                        if (tLRPC$Peer.chat_id == 0) {
                                            if (!(MessageObject.getMedia(tLRPC$Message2) instanceof TLRPC$TL_messageMediaGame)) {
                                            }
                                        }
                                    }
                                }
                                z = true;
                                if (!messageObject.isMusic() || messageObject.isDocument()) {
                                    this.isDocuments = true;
                                }
                            }
                            i6 = size;
                            z = false;
                            if (!messageObject.isMusic()) {
                            }
                            this.isDocuments = true;
                        } else {
                            i6 = size;
                        }
                        tLRPC$Message = messageObject.messageOwner;
                        if (tLRPC$Message != null && tLRPC$Message.invert_media) {
                            this.captionAbove = true;
                        }
                        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        GroupedMessagePosition groupedMessagePosition = new GroupedMessagePosition();
                        groupedMessagePosition.last = this.reversed ? i7 == i6 + (-1) : i7 == 0;
                        f2 = closestPhotoSizeWithSize != null ? 1.0f : closestPhotoSizeWithSize.w / closestPhotoSizeWithSize.h;
                        groupedMessagePosition.aspectRatio = f2;
                        if (f2 <= 1.2f) {
                            sb.append("w");
                        } else if (f2 < 0.8f) {
                            sb.append("n");
                        } else {
                            sb.append("q");
                        }
                        f3 = groupedMessagePosition.aspectRatio;
                        f4 += f3;
                        if (f3 > 2.0f) {
                            z2 = true;
                        }
                        this.positions.put(messageObject, groupedMessagePosition);
                        boolean z5 = z;
                        this.positionsArray.put(messageObject.getId(), groupedMessagePosition);
                        this.posArray.add(groupedMessagePosition);
                        if (messageObject.caption == null) {
                            if (z4 && this.captionMessage == null) {
                                this.captionMessage = messageObject;
                                bool = null;
                                z4 = false;
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
                        i7 = !this.reversed ? i7 - 1 : i7 + 1;
                        bool2 = bool;
                        size = i6;
                        z = z5;
                    } else {
                        if (i7 >= size) {
                            break;
                        }
                        messageObject = this.messages.get(i7);
                        if (i7 != (!this.reversed ? size - 1 : 0)) {
                        }
                        tLRPC$Message = messageObject.messageOwner;
                        if (tLRPC$Message != null) {
                            this.captionAbove = true;
                        }
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        GroupedMessagePosition groupedMessagePosition2 = new GroupedMessagePosition();
                        groupedMessagePosition2.last = this.reversed ? i7 == i6 + (-1) : i7 == 0;
                        if (closestPhotoSizeWithSize2 != null) {
                        }
                        groupedMessagePosition2.aspectRatio = f2;
                        if (f2 <= 1.2f) {
                        }
                        f3 = groupedMessagePosition2.aspectRatio;
                        f4 += f3;
                        if (f3 > 2.0f) {
                        }
                        this.positions.put(messageObject, groupedMessagePosition2);
                        boolean z52 = z;
                        this.positionsArray.put(messageObject.getId(), groupedMessagePosition2);
                        this.posArray.add(groupedMessagePosition2);
                        if (messageObject.caption == null) {
                        }
                        if (!this.reversed) {
                        }
                        bool2 = bool;
                        size = i6;
                        z = z52;
                    }
                }
                int i8 = size;
                if (this.isDocuments) {
                    for (int i9 = 0; i9 < i8; i9++) {
                        GroupedMessagePosition groupedMessagePosition3 = this.posArray.get(i9);
                        groupedMessagePosition3.flags = 3;
                        if (i9 == 0) {
                            groupedMessagePosition3.flags = 7;
                            groupedMessagePosition3.last = false;
                        } else if (i9 == i8 - 1) {
                            groupedMessagePosition3.flags = 11;
                            groupedMessagePosition3.last = true;
                        } else {
                            groupedMessagePosition3.last = false;
                        }
                        groupedMessagePosition3.edge = true;
                        groupedMessagePosition3.aspectRatio = 1.0f;
                        groupedMessagePosition3.minX = (byte) 0;
                        groupedMessagePosition3.maxX = (byte) 0;
                        byte b2 = (byte) i9;
                        groupedMessagePosition3.minY = b2;
                        groupedMessagePosition3.maxY = b2;
                        groupedMessagePosition3.spanSize = 1000;
                        groupedMessagePosition3.pw = this.maxSizeWidth;
                        groupedMessagePosition3.ph = 100.0f;
                    }
                    return;
                }
                if (z) {
                    this.maxSizeWidth -= 50;
                    i = NotificationCenter.playerDidStartPlaying;
                } else {
                    i = NotificationCenter.storyQualityUpdate;
                }
                int dp = AndroidUtilities.dp(120.0f);
                Point point = AndroidUtilities.displaySize;
                int dp2 = (int) (AndroidUtilities.dp(120.0f) / (Math.min(point.x, point.y) / this.maxSizeWidth));
                Point point2 = AndroidUtilities.displaySize;
                float f5 = this.maxSizeWidth;
                int dp3 = (int) (AndroidUtilities.dp(40.0f) / (Math.min(point2.x, point2.y) / f5));
                float f6 = f5 / 814.0f;
                float f7 = f4 / i8;
                float dp4 = AndroidUtilities.dp(100.0f) / 814.0f;
                if (i8 != 1) {
                    if (z2 || !(i8 == 2 || i8 == 3 || i8 == 4)) {
                        int size2 = this.posArray.size();
                        float[] fArr = new float[size2];
                        for (int i10 = 0; i10 < i8; i10++) {
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
                            int i27 = 0;
                            float f10 = Float.MAX_VALUE;
                            float f11 = 0.0f;
                            while (true) {
                                float[] fArr2 = messageGroupedLayoutAttempt2.heights;
                                if (i27 >= fArr2.length) {
                                    break;
                                }
                                float f12 = fArr2[i27];
                                f11 += f12;
                                if (f12 < f10) {
                                    f10 = f12;
                                }
                                i27++;
                            }
                            float abs = Math.abs(f11 - f8);
                            int[] iArr = messageGroupedLayoutAttempt2.lineCounts;
                            float f13 = f8;
                            ArrayList arrayList2 = arrayList;
                            if (iArr.length > 1) {
                                int i28 = iArr[0];
                                int i29 = iArr[1];
                                if (i28 <= i29 && (iArr.length <= 2 || i29 <= iArr[2])) {
                                    if (iArr.length > 3) {
                                    }
                                }
                                abs *= 1.2f;
                            }
                            if (f10 < dp2) {
                                abs *= 1.5f;
                            }
                            if (messageGroupedLayoutAttempt == null || abs < f9) {
                                f9 = abs;
                                messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                            }
                            i26++;
                            f8 = f13;
                            arrayList = arrayList2;
                        }
                        if (messageGroupedLayoutAttempt == null) {
                            return;
                        }
                        int i30 = 0;
                        int i31 = 0;
                        byte b3 = 0;
                        while (true) {
                            int[] iArr2 = messageGroupedLayoutAttempt.lineCounts;
                            if (i31 >= iArr2.length) {
                                break;
                            }
                            int i32 = iArr2[i31];
                            float f14 = messageGroupedLayoutAttempt.heights[i31];
                            int i33 = this.maxSizeWidth;
                            int i34 = i32 - 1;
                            int max = Math.max((int) b3, i34);
                            int i35 = i33;
                            int i36 = 0;
                            GroupedMessagePosition groupedMessagePosition4 = null;
                            while (i36 < i32) {
                                float[] fArr3 = fArr;
                                int i37 = (int) (fArr[i30] * f14);
                                i35 -= i37;
                                int i38 = max;
                                GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(i30);
                                int i39 = i32;
                                int i40 = i31 == 0 ? 4 : 0;
                                if (i31 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                                    i40 |= 8;
                                }
                                if (i36 == 0) {
                                    i40 |= 1;
                                    if (z3) {
                                        groupedMessagePosition4 = groupedMessagePosition5;
                                    }
                                }
                                if (i36 == i34) {
                                    i40 |= 2;
                                    if (!z3) {
                                        i2 = i40;
                                        groupedMessagePosition4 = groupedMessagePosition5;
                                        groupedMessagePosition5.set(i36, i36, i31, i31, i37, Math.max(dp4, f14 / 814.0f), i2);
                                        i30++;
                                        i36++;
                                        max = i38;
                                        fArr = fArr3;
                                        i32 = i39;
                                    }
                                }
                                i2 = i40;
                                groupedMessagePosition5.set(i36, i36, i31, i31, i37, Math.max(dp4, f14 / 814.0f), i2);
                                i30++;
                                i36++;
                                max = i38;
                                fArr = fArr3;
                                i32 = i39;
                            }
                            groupedMessagePosition4.pw += i35;
                            groupedMessagePosition4.spanSize += i35;
                            i31++;
                            b3 = max;
                            fArr = fArr;
                        }
                        b = b3;
                    } else {
                        if (i8 == 2) {
                            GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(0);
                            GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(1);
                            String sb2 = sb.toString();
                            if (sb2.equals("ww")) {
                                double d = f6;
                                Double.isNaN(d);
                                if (f7 > d * 1.4d) {
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
                                int i41 = this.maxSizeWidth / 2;
                                float f18 = i41;
                                float round2 = Math.round(Math.min(f18 / groupedMessagePosition6.aspectRatio, Math.min(f18 / groupedMessagePosition7.aspectRatio, 814.0f))) / 814.0f;
                                groupedMessagePosition6.set(0, 0, 0, 0, i41, round2, 13);
                                groupedMessagePosition7.set(1, 1, 0, 0, i41, round2, 14);
                            } else {
                                float f19 = this.maxSizeWidth;
                                float f20 = groupedMessagePosition6.aspectRatio;
                                int max2 = (int) Math.max(0.4f * f19, Math.round((f19 / f20) / ((1.0f / f20) + (1.0f / groupedMessagePosition7.aspectRatio))));
                                int i42 = this.maxSizeWidth - max2;
                                if (i42 < dp2) {
                                    max2 -= dp2 - i42;
                                } else {
                                    dp2 = i42;
                                }
                                float min = Math.min(814.0f, Math.round(Math.min(dp2 / groupedMessagePosition6.aspectRatio, max2 / groupedMessagePosition7.aspectRatio))) / 814.0f;
                                groupedMessagePosition6.set(0, 0, 0, 0, dp2, min, 13);
                                groupedMessagePosition7.set(1, 1, 0, 0, max2, min, 14);
                            }
                        } else if (i8 == 3) {
                            GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(0);
                            GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(1);
                            GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(2);
                            if (sb.charAt(0) == 'n') {
                                float f21 = groupedMessagePosition9.aspectRatio;
                                float min2 = Math.min(407.0f, Math.round((this.maxSizeWidth * f21) / (groupedMessagePosition10.aspectRatio + f21)));
                                int max3 = (int) Math.max(dp2, Math.min(this.maxSizeWidth * 0.5f, Math.round(Math.min(groupedMessagePosition10.aspectRatio * min2, groupedMessagePosition9.aspectRatio * f))));
                                int round3 = Math.round(Math.min((groupedMessagePosition8.aspectRatio * 814.0f) + dp3, this.maxSizeWidth - max3));
                                groupedMessagePosition8.set(0, 0, 0, 1, round3, 1.0f, 13);
                                float f22 = (814.0f - min2) / 814.0f;
                                groupedMessagePosition9.set(1, 1, 0, 0, max3, f22, 6);
                                float f23 = min2 / 814.0f;
                                groupedMessagePosition10.set(0, 1, 1, 1, max3, f23, 10);
                                int i43 = this.maxSizeWidth;
                                groupedMessagePosition10.spanSize = i43;
                                groupedMessagePosition8.siblingHeights = new float[]{f23, f22};
                                if (z3) {
                                    groupedMessagePosition8.spanSize = i43 - max3;
                                } else {
                                    groupedMessagePosition9.spanSize = i43 - round3;
                                    groupedMessagePosition10.leftSpanOffset = round3;
                                }
                                this.hasSibling = true;
                            } else {
                                float round4 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition8.aspectRatio, 537.24005f)) / 814.0f;
                                groupedMessagePosition8.set(0, 1, 0, 0, this.maxSizeWidth, round4, 7);
                                int i44 = this.maxSizeWidth / 2;
                                float f24 = 814.0f - round4;
                                float f25 = i44;
                                float min3 = Math.min(f24, Math.round(Math.min(f25 / groupedMessagePosition9.aspectRatio, f25 / groupedMessagePosition10.aspectRatio))) / 814.0f;
                                if (min3 >= dp4) {
                                    dp4 = min3;
                                }
                                float f26 = dp4;
                                groupedMessagePosition9.set(0, 0, 1, 1, i44, f26, 9);
                                groupedMessagePosition10.set(1, 1, 1, 1, i44, f26, 10);
                            }
                        } else {
                            GroupedMessagePosition groupedMessagePosition11 = this.posArray.get(0);
                            GroupedMessagePosition groupedMessagePosition12 = this.posArray.get(1);
                            GroupedMessagePosition groupedMessagePosition13 = this.posArray.get(2);
                            GroupedMessagePosition groupedMessagePosition14 = this.posArray.get(3);
                            if (sb.charAt(0) == 'w') {
                                float round5 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition11.aspectRatio, 537.24005f)) / 814.0f;
                                groupedMessagePosition11.set(0, 2, 0, 0, this.maxSizeWidth, round5, 7);
                                float round6 = Math.round(this.maxSizeWidth / ((groupedMessagePosition12.aspectRatio + groupedMessagePosition13.aspectRatio) + groupedMessagePosition14.aspectRatio));
                                float f27 = dp2;
                                int max4 = (int) Math.max(f27, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition12.aspectRatio * round6));
                                int max5 = (int) Math.max(Math.max(f27, this.maxSizeWidth * 0.33f), groupedMessagePosition14.aspectRatio * round6);
                                int i45 = (this.maxSizeWidth - max4) - max5;
                                if (i45 < AndroidUtilities.dp(58.0f)) {
                                    int dp5 = AndroidUtilities.dp(58.0f) - i45;
                                    i45 = AndroidUtilities.dp(58.0f);
                                    int i46 = dp5 / 2;
                                    max4 -= i46;
                                    max5 -= dp5 - i46;
                                }
                                int i47 = max4;
                                float min4 = Math.min(814.0f - round5, round6) / 814.0f;
                                if (min4 >= dp4) {
                                    dp4 = min4;
                                }
                                float f28 = dp4;
                                groupedMessagePosition12.set(0, 0, 1, 1, i47, f28, 9);
                                groupedMessagePosition13.set(1, 1, 1, 1, i45, f28, 8);
                                groupedMessagePosition14.set(2, 2, 1, 1, max5, f28, 10);
                                b = 2;
                            } else {
                                int max6 = Math.max(dp2, Math.round(814.0f / (((1.0f / groupedMessagePosition12.aspectRatio) + (1.0f / groupedMessagePosition13.aspectRatio)) + (1.0f / groupedMessagePosition14.aspectRatio))));
                                float f29 = dp;
                                float f30 = max6;
                                float min5 = Math.min(0.33f, Math.max(f29, f30 / groupedMessagePosition12.aspectRatio) / 814.0f);
                                float min6 = Math.min(0.33f, Math.max(f29, f30 / groupedMessagePosition13.aspectRatio) / 814.0f);
                                float f31 = (1.0f - min5) - min6;
                                int round7 = Math.round(Math.min((groupedMessagePosition11.aspectRatio * 814.0f) + dp3, this.maxSizeWidth - max6));
                                groupedMessagePosition11.set(0, 0, 0, 2, round7, min5 + min6 + f31, 13);
                                groupedMessagePosition12.set(1, 1, 0, 0, max6, min5, 6);
                                groupedMessagePosition13.set(0, 1, 1, 1, max6, min6, 2);
                                groupedMessagePosition13.spanSize = this.maxSizeWidth;
                                groupedMessagePosition14.set(0, 1, 2, 2, max6, f31, 10);
                                int i48 = this.maxSizeWidth;
                                groupedMessagePosition14.spanSize = i48;
                                if (z3) {
                                    groupedMessagePosition11.spanSize = i48 - max6;
                                } else {
                                    groupedMessagePosition12.spanSize = i48 - round7;
                                    groupedMessagePosition13.leftSpanOffset = round7;
                                    groupedMessagePosition14.leftSpanOffset = round7;
                                }
                                groupedMessagePosition11.siblingHeights = new float[]{min5, min6, f31};
                                this.hasSibling = true;
                            }
                        }
                        b = 1;
                    }
                    i4 = 0;
                    while (i4 < i8) {
                        GroupedMessagePosition groupedMessagePosition15 = this.posArray.get(i4);
                        if (z3) {
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
                        MessageObject messageObject2 = this.messages.get(i4);
                        if (!z3 && messageObject2.needDrawAvatarInternal()) {
                            if (groupedMessagePosition15.edge) {
                                int i49 = groupedMessagePosition15.spanSize;
                                if (i49 != 1000) {
                                    groupedMessagePosition15.spanSize = i49 + 108;
                                }
                                groupedMessagePosition15.pw += 108;
                                i5 = 1;
                                i4 += i5;
                            } else if ((groupedMessagePosition15.flags & 2) != 0) {
                                int i50 = groupedMessagePosition15.spanSize;
                                if (i50 != 1000) {
                                    groupedMessagePosition15.spanSize = i50 - 108;
                                } else {
                                    int i51 = groupedMessagePosition15.leftSpanOffset;
                                    if (i51 != 0) {
                                        groupedMessagePosition15.leftSpanOffset = i51 + 108;
                                    }
                                }
                                i5 = 1;
                                i4 += i5;
                            }
                        }
                        i5 = 1;
                        i4 += i5;
                    }
                }
                GroupedMessagePosition groupedMessagePosition16 = this.posArray.get(0);
                float f32 = this.maxSizeWidth / groupedMessagePosition16.aspectRatio;
                groupedMessagePosition16.set(0, 0, 0, 0, this.maxSizeWidth, Math.round(Math.min(f32, Math.min(f32, 407.0f))) / 814.0f, 15);
                b = 0;
                i4 = 0;
                while (i4 < i8) {
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
        this(i, tLRPC$Message, messageObject, abstractMap, abstractMap2, longSparseArray, longSparseArray2, z, z2, j, z3, z4, z5, 0);
    }

    public MessageObject(int i, TLRPC$Message tLRPC$Message, MessageObject messageObject, AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2, boolean z, boolean z2, long j, boolean z3, boolean z4, boolean z5, int i2) {
        TextPaint textPaint;
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
                        long j = ((TLRPC$TL_messageEntityCustomEmoji) this.messageOwner.entities.get(0)).document_id;
                        this.emojiAnimatedStickerId = Long.valueOf(j);
                        TLRPC$Document findDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, j);
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
                    textPaint = z2 ? Theme.chat_msgTextPaintEmoji[0] : Theme.chat_msgTextPaintEmoji[2];
                    i4 = 1;
                    break;
                case 3:
                    textPaint = z2 ? Theme.chat_msgTextPaintEmoji[1] : Theme.chat_msgTextPaintEmoji[3];
                    i4 = 1;
                    break;
                case 4:
                    textPaint = z2 ? Theme.chat_msgTextPaintEmoji[2] : Theme.chat_msgTextPaintEmoji[4];
                    i4 = 1;
                    break;
                case 5:
                    if (z2) {
                        textPaint = Theme.chat_msgTextPaintEmoji[3];
                        break;
                    } else {
                        textPaint = Theme.chat_msgTextPaintEmoji[5];
                        break;
                    }
                case 6:
                    if (z2) {
                        textPaint = Theme.chat_msgTextPaintEmoji[4];
                        break;
                    } else {
                        textPaint = Theme.chat_msgTextPaintEmoji[5];
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

    public TextPaint getTextPaint() {
        if (this.emojiOnlyCount >= 1 && this.messageOwner != null && !hasNonEmojiEntities()) {
            int i = this.emojiOnlyCount;
            int i2 = this.animatedEmojiCount;
            boolean z = i == i2;
            switch (Math.max(i, i2)) {
                case 0:
                case 1:
                case 2:
                    return z ? Theme.chat_msgTextPaintEmoji[0] : Theme.chat_msgTextPaintEmoji[2];
                case 3:
                    return z ? Theme.chat_msgTextPaintEmoji[1] : Theme.chat_msgTextPaintEmoji[3];
                case 4:
                    return z ? Theme.chat_msgTextPaintEmoji[2] : Theme.chat_msgTextPaintEmoji[4];
                case 5:
                    return z ? Theme.chat_msgTextPaintEmoji[3] : Theme.chat_msgTextPaintEmoji[5];
                case 6:
                    return z ? Theme.chat_msgTextPaintEmoji[4] : Theme.chat_msgTextPaintEmoji[5];
                default:
                    return Theme.chat_msgTextPaintEmoji[5];
            }
        }
        return Theme.chat_msgTextPaint;
    }

    /* JADX WARN: Code restructure failed: missing block: B:66:0x0296, code lost:
        if ((r4.new_participant instanceof org.telegram.tgnet.TLRPC$TL_channelParticipant) != false) goto L122;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1052:0x1b7f  */
    /* JADX WARN: Removed duplicated region for block: B:1055:0x1bd1  */
    /* JADX WARN: Removed duplicated region for block: B:1057:0x1bd4  */
    /* JADX WARN: Removed duplicated region for block: B:1073:0x1c68  */
    /* JADX WARN: Removed duplicated region for block: B:1076:0x1c73 A[ADDED_TO_REGION, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:1077:0x1c74  */
    /* JADX WARN: Removed duplicated region for block: B:1081:0x1c89  */
    /* JADX WARN: Removed duplicated region for block: B:1082:0x1c8d  */
    /* JADX WARN: Removed duplicated region for block: B:1085:0x1c99  */
    /* JADX WARN: Removed duplicated region for block: B:1088:0x1ca7  */
    /* JADX WARN: Removed duplicated region for block: B:1089:0x1caa  */
    /* JADX WARN: Removed duplicated region for block: B:1092:0x1cb2  */
    /* JADX WARN: Removed duplicated region for block: B:1093:0x1cb5  */
    /* JADX WARN: Removed duplicated region for block: B:1101:0x1ce7  */
    /* JADX WARN: Removed duplicated region for block: B:1105:0x0545 A[EDGE_INSN: B:1105:0x0545->B:211:0x0545 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0519  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0529 A[LOOP:0: B:184:0x04cc->B:209:0x0529, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:505:0x0d01  */
    /* JADX WARN: Type inference failed for: r12v83 */
    /* JADX WARN: Type inference failed for: r12v84 */
    /* JADX WARN: Type inference failed for: r12v86 */
    /* JADX WARN: Type inference failed for: r12v87, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r12v89 */
    /* JADX WARN: Type inference failed for: r12v90 */
    /* JADX WARN: Type inference failed for: r13v29 */
    /* JADX WARN: Type inference failed for: r13v30, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r13v31 */
    /* JADX WARN: Type inference failed for: r13v32 */
    /* JADX WARN: Type inference failed for: r13v33 */
    /* JADX WARN: Type inference failed for: r13v35 */
    /* JADX WARN: Type inference failed for: r14v10 */
    /* JADX WARN: Type inference failed for: r14v11 */
    /* JADX WARN: Type inference failed for: r14v6 */
    /* JADX WARN: Type inference failed for: r14v7, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r14v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MessageObject(int i, TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent, ArrayList<MessageObject> arrayList, HashMap<String, ArrayList<MessageObject>> hashMap, TLRPC$Chat tLRPC$Chat, int[] iArr, boolean z) {
        boolean z2;
        String str;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant;
        TLRPC$ChannelParticipant tLRPC$ChannelParticipant2;
        TLRPC$User user;
        String string;
        StringBuilder sb;
        char c;
        TLRPC$Chat tLRPC$Chat2;
        String str2;
        TLRPC$Chat tLRPC$Chat3;
        SpannableString spannableString;
        SpannableString spannableString2;
        SpannableString spannableString3;
        boolean z3;
        SpannableString spannableString4;
        int i2;
        String string2;
        TLRPC$TL_message tLRPC$TL_message;
        char c2;
        String formatPluralString;
        TLObject chat;
        TLObject chat2;
        TLObject chat3;
        char c3;
        String formatPluralString2;
        ArrayList<TLRPC$MessageEntity> arrayList2;
        ArrayList<TLRPC$MessageEntity> arrayList3;
        TLRPC$WebPage tLRPC$WebPage;
        ArrayList<TLRPC$MessageEntity> arrayList4;
        TLRPC$Peer tLRPC$Peer;
        String str3;
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLObject chat4;
        String string3;
        StringBuilder sb2;
        boolean z4;
        char c4;
        int i3;
        String str4;
        String formatPluralString3;
        int i4;
        boolean z5;
        boolean z6;
        TLObject chat5;
        MediaController mediaController;
        ArrayList<MessageObject> arrayList5;
        ?? r12;
        ?? r13;
        int i5;
        TextPaint textPaint;
        int[] iArr2;
        Spannable replaceAnimatedEmoji;
        int i6;
        MessageObject messageObject = this;
        TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
        messageObject.type = 1000;
        messageObject.forceSeekTo = -1.0f;
        messageObject.actionDeleteGroupEventId = -1L;
        messageObject.overrideLinkColor = -1;
        messageObject.overrideLinkEmoji = -1L;
        messageObject.topicIconDrawable = new Drawable[1];
        messageObject.spoiledLoginCode = false;
        messageObject.translated = false;
        messageObject.currentEvent = tLRPC$TL_channelAdminLogEvent2;
        messageObject.currentAccount = i;
        TLRPC$User user2 = tLRPC$TL_channelAdminLogEvent2.user_id > 0 ? MessagesController.getInstance(i).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEvent2.user_id)) : null;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        String str5 = "Minutes";
        gregorianCalendar.setTimeInMillis(tLRPC$TL_channelAdminLogEvent2.date * 1000);
        int i7 = gregorianCalendar.get(6);
        int i8 = gregorianCalendar.get(1);
        int i9 = gregorianCalendar.get(2);
        messageObject.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(i8), Integer.valueOf(i9), Integer.valueOf(i7));
        messageObject.dateKeyInt = (i9 * 1000) + i8 + (i7 * 100000);
        messageObject.monthKey = String.format("%d_%02d", Integer.valueOf(i8), Integer.valueOf(i9));
        TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
        tLRPC$TL_peerChannel.channel_id = tLRPC$Chat.id;
        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction = tLRPC$TL_channelAdminLogEvent2.action;
        String str6 = "";
        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTitle) {
            String str7 = ((TLRPC$TL_channelAdminLogEventActionChangeTitle) tLRPC$ChannelAdminLogEventAction).new_value;
            if (tLRPC$Chat.megagroup) {
                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedGroupTitle", R.string.EventLogEditedGroupTitle, str7), "un1", user2);
            } else {
                messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedChannelTitle", R.string.EventLogEditedChannelTitle, str7), "un1", user2);
            }
        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangePhoto) {
            TLRPC$TL_channelAdminLogEventActionChangePhoto tLRPC$TL_channelAdminLogEventActionChangePhoto = (TLRPC$TL_channelAdminLogEventActionChangePhoto) tLRPC$ChannelAdminLogEventAction;
            TLRPC$TL_messageService tLRPC$TL_messageService = new TLRPC$TL_messageService();
            messageObject.messageOwner = tLRPC$TL_messageService;
            if (tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo instanceof TLRPC$TL_photoEmpty) {
                tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatDeletePhoto();
                if (tLRPC$Chat.megagroup) {
                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogRemovedWGroupPhoto), "un1", user2);
                } else {
                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogRemovedChannelPhoto), "un1", user2);
                }
            } else {
                tLRPC$TL_messageService.action = new TLRPC$TL_messageActionChatEditPhoto();
                messageObject.messageOwner.action.photo = tLRPC$TL_channelAdminLogEventActionChangePhoto.new_photo;
                if (tLRPC$Chat.megagroup) {
                    if (isVideoAvatar()) {
                        messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedGroupVideo), "un1", user2);
                    } else {
                        messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedGroupPhoto), "un1", user2);
                    }
                } else if (isVideoAvatar()) {
                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedChannelVideo), "un1", user2);
                } else {
                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedChannelPhoto), "un1", user2);
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
                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogLeftGroup), "un1", user2);
            } else {
                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogLeftChannel), "un1", user2);
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
                CharSequence replaceWithLink = replaceWithLink(LocaleController.getString(R.string.EventLogAdded), "un2", chat5);
                messageObject.messageText = replaceWithLink;
                messageObject.messageText = replaceWithLink(replaceWithLink, "un1", user2);
            }
        } else {
            boolean z7 = tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin;
            if (z7) {
                z2 = z7;
            } else {
                boolean z8 = tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantToggleBan;
                if (z8) {
                    z2 = z7;
                    TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                    str2 = "/";
                    if (tLRPC$TL_channelAdminLogEventActionParticipantToggleBan.prev_participant instanceof TLRPC$TL_channelParticipantAdmin) {
                    }
                } else {
                    str2 = "/";
                }
                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) {
                    TLRPC$TL_channelAdminLogEventActionDefaultBannedRights tLRPC$TL_channelAdminLogEventActionDefaultBannedRights = (TLRPC$TL_channelAdminLogEventActionDefaultBannedRights) tLRPC$ChannelAdminLogEventAction;
                    TLRPC$TL_message tLRPC$TL_message2 = new TLRPC$TL_message();
                    messageObject.messageOwner = tLRPC$TL_message2;
                    tLRPC$TL_message2.realId = -1;
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
                    if (tLRPC$TL_chatBannedRights.send_stickers == tLRPC$TL_chatBannedRights2.send_stickers && tLRPC$TL_chatBannedRights.send_inline == tLRPC$TL_chatBannedRights2.send_inline && tLRPC$TL_chatBannedRights.send_gifs == tLRPC$TL_chatBannedRights2.send_gifs && tLRPC$TL_chatBannedRights.send_games == tLRPC$TL_chatBannedRights2.send_games) {
                        z6 = z5;
                    } else {
                        if (z5) {
                            z6 = z5;
                        } else {
                            sb3.append('\n');
                            z6 = true;
                        }
                        sb3.append('\n');
                        sb3.append(!tLRPC$TL_chatBannedRights2.send_stickers ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString(R.string.EventLogRestrictedSendStickers));
                    }
                    if (tLRPC$TL_chatBannedRights.send_media != tLRPC$TL_chatBannedRights2.send_media) {
                        if (!z6) {
                            sb3.append('\n');
                            z6 = true;
                        }
                        sb3.append('\n');
                        sb3.append(!tLRPC$TL_chatBannedRights2.send_media ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString(R.string.EventLogRestrictedSendMedia));
                    }
                    if (tLRPC$TL_chatBannedRights.send_polls != tLRPC$TL_chatBannedRights2.send_polls) {
                        if (!z6) {
                            sb3.append('\n');
                            z6 = true;
                        }
                        sb3.append('\n');
                        sb3.append(!tLRPC$TL_chatBannedRights2.send_polls ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString(R.string.EventLogRestrictedSendPolls));
                    }
                    if (tLRPC$TL_chatBannedRights.embed_links != tLRPC$TL_chatBannedRights2.embed_links) {
                        if (!z6) {
                            sb3.append('\n');
                            z6 = true;
                        }
                        sb3.append('\n');
                        sb3.append(!tLRPC$TL_chatBannedRights2.embed_links ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString(R.string.EventLogRestrictedSendEmbed));
                    }
                    if (tLRPC$TL_chatBannedRights.change_info != tLRPC$TL_chatBannedRights2.change_info) {
                        if (!z6) {
                            sb3.append('\n');
                            z6 = true;
                        }
                        sb3.append('\n');
                        sb3.append(!tLRPC$TL_chatBannedRights2.change_info ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString(R.string.EventLogRestrictedChangeInfo));
                    }
                    if (tLRPC$TL_chatBannedRights.invite_users != tLRPC$TL_chatBannedRights2.invite_users) {
                        if (!z6) {
                            sb3.append('\n');
                            z6 = true;
                        }
                        sb3.append('\n');
                        sb3.append(!tLRPC$TL_chatBannedRights2.invite_users ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString(R.string.EventLogRestrictedInviteUsers));
                    }
                    if (tLRPC$TL_chatBannedRights.pin_messages != tLRPC$TL_chatBannedRights2.pin_messages) {
                        if (!z6) {
                            sb3.append('\n');
                        }
                        sb3.append('\n');
                        sb3.append(!tLRPC$TL_chatBannedRights2.pin_messages ? '+' : '-');
                        sb3.append(' ');
                        sb3.append(LocaleController.getString(R.string.EventLogRestrictedPinMessages));
                    }
                    messageObject.messageText = sb3.toString();
                } else {
                    if (z8) {
                        TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2 = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                        TLRPC$TL_message tLRPC$TL_message3 = new TLRPC$TL_message();
                        messageObject.messageOwner = tLRPC$TL_message3;
                        tLRPC$TL_message3.realId = -1;
                        long peerId2 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.peer);
                        if (peerId2 > 0) {
                            chat4 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId2));
                        } else {
                            chat4 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId2));
                        }
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights3 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.prev_participant.banned_rights;
                        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights4 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan2.new_participant.banned_rights;
                        if (tLRPC$Chat.megagroup && (tLRPC$TL_chatBannedRights4 == null || !tLRPC$TL_chatBannedRights4.view_messages || (tLRPC$TL_chatBannedRights3 != null && tLRPC$TL_chatBannedRights4.until_date != tLRPC$TL_chatBannedRights3.until_date))) {
                            if (tLRPC$TL_chatBannedRights4 != null && !AndroidUtilities.isBannedForever(tLRPC$TL_chatBannedRights4)) {
                                sb2 = new StringBuilder();
                                int i10 = tLRPC$TL_chatBannedRights4.until_date - tLRPC$TL_channelAdminLogEvent2.date;
                                int i11 = ((i10 / 60) / 60) / 24;
                                int i12 = i10 - (86400 * i11);
                                str = "";
                                int i13 = (i12 / 60) / 60;
                                int i14 = (i12 - (i13 * 3600)) / 60;
                                int i15 = 3;
                                int i16 = 0;
                                int i17 = 0;
                                while (i16 < i15) {
                                    if (i16 == 0) {
                                        if (i11 != 0) {
                                            i17++;
                                            i3 = i16;
                                            formatPluralString3 = LocaleController.formatPluralString("Days", i11, new Object[0]);
                                            str4 = str5;
                                            i4 = i17;
                                            int i18 = i14;
                                            if (formatPluralString3 != null) {
                                                if (sb2.length() > 0) {
                                                    sb2.append(", ");
                                                }
                                                sb2.append(formatPluralString3);
                                            }
                                            if (i4 != 2) {
                                                break;
                                            }
                                            str5 = str4;
                                            i16 = i3 + 1;
                                            i15 = 3;
                                            i17 = i4;
                                            i14 = i18;
                                        }
                                        i3 = i16;
                                        str4 = str5;
                                        formatPluralString3 = null;
                                        i4 = i17;
                                        int i182 = i14;
                                        if (formatPluralString3 != null) {
                                        }
                                        if (i4 != 2) {
                                        }
                                    } else if (i16 == 1) {
                                        if (i13 != 0) {
                                            int i19 = i16;
                                            formatPluralString3 = LocaleController.formatPluralString("Hours", i13, new Object[0]);
                                            i17++;
                                            i3 = i19;
                                            str4 = str5;
                                            i4 = i17;
                                            int i1822 = i14;
                                            if (formatPluralString3 != null) {
                                            }
                                            if (i4 != 2) {
                                            }
                                        }
                                        i3 = i16;
                                        str4 = str5;
                                        formatPluralString3 = null;
                                        i4 = i17;
                                        int i18222 = i14;
                                        if (formatPluralString3 != null) {
                                        }
                                        if (i4 != 2) {
                                        }
                                    } else {
                                        int i20 = i16;
                                        if (i14 != 0) {
                                            str4 = str5;
                                            i17++;
                                            i3 = i20;
                                            formatPluralString3 = LocaleController.formatPluralString(str4, i14, new Object[0]);
                                            i4 = i17;
                                            int i182222 = i14;
                                            if (formatPluralString3 != null) {
                                            }
                                            if (i4 != 2) {
                                            }
                                        } else {
                                            i3 = i20;
                                            str4 = str5;
                                            formatPluralString3 = null;
                                            i4 = i17;
                                            int i1822222 = i14;
                                            if (formatPluralString3 != null) {
                                            }
                                            if (i4 != 2) {
                                            }
                                        }
                                    }
                                }
                            } else {
                                str = "";
                                sb2 = new StringBuilder(LocaleController.getString(R.string.UserRestrictionsUntilForever));
                            }
                            String string4 = LocaleController.getString(R.string.EventLogRestrictedUntil);
                            StringBuilder sb4 = new StringBuilder(String.format(string4, messageObject.getUserName(chat4, messageObject.messageOwner.entities, string4.indexOf("%1$s")), sb2.toString()));
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
                                    c4 = '\n';
                                } else {
                                    c4 = '\n';
                                    sb4.append('\n');
                                }
                                sb4.append(c4);
                                sb4.append(!tLRPC$TL_chatBannedRights4.pin_messages ? '+' : '-');
                                sb4.append(' ');
                                sb4.append(LocaleController.getString(R.string.EventLogRestrictedPinMessages));
                            }
                            messageObject.messageText = sb4.toString();
                        } else {
                            str = "";
                            if (tLRPC$TL_chatBannedRights4 != null && (tLRPC$TL_chatBannedRights3 == null || tLRPC$TL_chatBannedRights4.view_messages)) {
                                string3 = LocaleController.getString(R.string.EventLogChannelRestricted);
                            } else {
                                string3 = LocaleController.getString(R.string.EventLogChannelUnrestricted);
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
                                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogUnpinnedMessages), "un1", chat6);
                                } else {
                                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogPinnedMessages), "un1", chat6);
                                }
                            } else if ((tLRPC$Message instanceof TLRPC$TL_messageEmpty) || !tLRPC$Message.pinned) {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogUnpinnedMessages), "un1", user2);
                            } else {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogPinnedMessages), "un1", user2);
                            }
                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStopPoll) {
                            tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionStopPoll) tLRPC$ChannelAdminLogEventAction).message;
                            if ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPoll) && ((TLRPC$TL_messageMediaPoll) getMedia(tLRPC$Message)).poll.quiz) {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogStopQuiz), "un1", user2);
                            } else {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogStopPoll), "un1", user2);
                            }
                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSignatures) {
                            if (((TLRPC$TL_channelAdminLogEventActionToggleSignatures) tLRPC$ChannelAdminLogEventAction).new_value) {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogToggledSignaturesOn), "un1", user2);
                            } else {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogToggledSignaturesOff), "un1", user2);
                            }
                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSignatureProfiles) {
                            if (((TLRPC$TL_channelAdminLogEventActionToggleSignatureProfiles) tLRPC$ChannelAdminLogEventAction).value) {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogToggledSignaturesProfilesOn), "un1", user2);
                            } else {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogToggledSignaturesProfilesOff), "un1", user2);
                            }
                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleInvites) {
                            if (((TLRPC$TL_channelAdminLogEventActionToggleInvites) tLRPC$ChannelAdminLogEventAction).new_value) {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogToggledInvitesOn), "un1", user2);
                            } else {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogToggledInvitesOff), "un1", user2);
                            }
                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                            tLRPC$Message = ((TLRPC$TL_channelAdminLogEventActionDeleteMessage) tLRPC$ChannelAdminLogEventAction).message;
                            if (user2 != null && user2.id == MessagesController.getInstance(messageObject.currentAccount).telegramAntispamUserId) {
                                messageObject.messageText = LocaleController.getString(R.string.EventLogDeletedMessages).replace("un1", UserObject.getUserName(user2));
                            } else {
                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogDeletedMessages), "un1", user2);
                            }
                        } else {
                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) {
                                TLRPC$TL_channelAdminLogEventActionChangeLinkedChat tLRPC$TL_channelAdminLogEventActionChangeLinkedChat = (TLRPC$TL_channelAdminLogEventActionChangeLinkedChat) tLRPC$ChannelAdminLogEventAction;
                                long j = tLRPC$TL_channelAdminLogEventActionChangeLinkedChat.new_value;
                                long j2 = tLRPC$TL_channelAdminLogEventActionChangeLinkedChat.prev_value;
                                tLRPC$Chat3 = tLRPC$Chat;
                                if (tLRPC$Chat3.megagroup) {
                                    if (j == 0) {
                                        TLRPC$Chat chat7 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j2));
                                        CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString(R.string.EventLogRemovedLinkedChannel), "un1", user2);
                                        messageObject.messageText = replaceWithLink2;
                                        messageObject.messageText = replaceWithLink(replaceWithLink2, "un2", chat7);
                                    } else {
                                        TLRPC$Chat chat8 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j));
                                        CharSequence replaceWithLink3 = replaceWithLink(LocaleController.getString(R.string.EventLogChangedLinkedChannel), "un1", user2);
                                        messageObject.messageText = replaceWithLink3;
                                        messageObject.messageText = replaceWithLink(replaceWithLink3, "un2", chat8);
                                    }
                                } else if (j == 0) {
                                    TLRPC$Chat chat9 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j2));
                                    CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString(R.string.EventLogRemovedLinkedGroup), "un1", user2);
                                    messageObject.messageText = replaceWithLink4;
                                    messageObject.messageText = replaceWithLink(replaceWithLink4, "un2", chat9);
                                } else {
                                    TLRPC$Chat chat10 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(j));
                                    CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString(R.string.EventLogChangedLinkedGroup), "un1", user2);
                                    messageObject.messageText = replaceWithLink5;
                                    messageObject.messageText = replaceWithLink(replaceWithLink5, "un2", chat10);
                                }
                            } else {
                                tLRPC$Chat3 = tLRPC$Chat;
                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) {
                                    if (((TLRPC$TL_channelAdminLogEventActionTogglePreHistoryHidden) tLRPC$ChannelAdminLogEventAction).new_value) {
                                        messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogToggledInvitesHistoryOff), "un1", user2);
                                    } else {
                                        messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogToggledInvitesHistoryOn), "un1", user2);
                                    }
                                } else {
                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAbout) {
                                        messageObject.messageText = replaceWithLink(LocaleController.getString(tLRPC$Chat3.megagroup ? R.string.EventLogEditedGroupDescription : R.string.EventLogEditedChannelDescription), "un1", user2);
                                        tLRPC$TL_message = new TLRPC$TL_message();
                                        tLRPC$TL_message.out = false;
                                        tLRPC$TL_message.unread = false;
                                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                                        tLRPC$TL_message.from_id = tLRPC$TL_peerUser;
                                        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                        tLRPC$TL_peerUser.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                        tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                        tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                        TLRPC$TL_channelAdminLogEventActionChangeAbout tLRPC$TL_channelAdminLogEventActionChangeAbout = (TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$TL_channelAdminLogEvent2.action;
                                        tLRPC$TL_message.message = tLRPC$TL_channelAdminLogEventActionChangeAbout.new_value;
                                        if (!TextUtils.isEmpty(tLRPC$TL_channelAdminLogEventActionChangeAbout.prev_value)) {
                                            TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage = new TLRPC$TL_messageMediaWebPage();
                                            tLRPC$TL_message.media = tLRPC$TL_messageMediaWebPage;
                                            tLRPC$TL_messageMediaWebPage.webpage = new TLRPC$TL_webPage();
                                            TLRPC$WebPage tLRPC$WebPage2 = tLRPC$TL_message.media.webpage;
                                            tLRPC$WebPage2.flags = 10;
                                            str6 = str;
                                            tLRPC$WebPage2.display_url = str6;
                                            tLRPC$WebPage2.url = str6;
                                            tLRPC$WebPage2.site_name = LocaleController.getString(R.string.EventLogPreviousGroupDescription);
                                            tLRPC$TL_message.media.webpage.description = ((TLRPC$TL_channelAdminLogEventActionChangeAbout) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                        } else {
                                            str6 = str;
                                            tLRPC$TL_message.media = new TLRPC$TL_messageMediaEmpty();
                                        }
                                    } else {
                                        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                                        str6 = str;
                                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeTheme) {
                                            messageObject.messageText = replaceWithLink(LocaleController.getString(tLRPC$Chat3.megagroup ? R.string.EventLogEditedGroupTheme : R.string.EventLogEditedChannelTheme), "un1", user2);
                                            tLRPC$TL_message = new TLRPC$TL_message();
                                            tLRPC$TL_message.out = false;
                                            tLRPC$TL_message.unread = false;
                                            TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                                            tLRPC$TL_message.from_id = tLRPC$TL_peerUser2;
                                            tLRPC$TL_peerUser2.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                            tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                            tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                            TLRPC$TL_channelAdminLogEventActionChangeTheme tLRPC$TL_channelAdminLogEventActionChangeTheme = (TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$TL_channelAdminLogEvent2.action;
                                            tLRPC$TL_message.message = tLRPC$TL_channelAdminLogEventActionChangeTheme.new_value;
                                            if (!TextUtils.isEmpty(tLRPC$TL_channelAdminLogEventActionChangeTheme.prev_value)) {
                                                TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage2 = new TLRPC$TL_messageMediaWebPage();
                                                tLRPC$TL_message.media = tLRPC$TL_messageMediaWebPage2;
                                                tLRPC$TL_messageMediaWebPage2.webpage = new TLRPC$TL_webPage();
                                                TLRPC$WebPage tLRPC$WebPage3 = tLRPC$TL_message.media.webpage;
                                                tLRPC$WebPage3.flags = 10;
                                                tLRPC$WebPage3.display_url = str6;
                                                tLRPC$WebPage3.url = str6;
                                                tLRPC$WebPage3.site_name = LocaleController.getString(R.string.EventLogPreviousGroupTheme);
                                                tLRPC$TL_message.media.webpage.description = ((TLRPC$TL_channelAdminLogEventActionChangeTheme) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                            } else {
                                                tLRPC$TL_message.media = new TLRPC$TL_messageMediaEmpty();
                                            }
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeUsername) {
                                            String str8 = ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$ChannelAdminLogEventAction).new_value;
                                            if (TextUtils.isEmpty(str8)) {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString(tLRPC$Chat3.megagroup ? R.string.EventLogRemovedGroupLink : R.string.EventLogRemovedChannelLink), "un1", user2);
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString(tLRPC$Chat3.megagroup ? R.string.EventLogChangedGroupLink : R.string.EventLogChangedChannelLink), "un1", user2);
                                            }
                                            tLRPC$TL_message = new TLRPC$TL_message();
                                            tLRPC$TL_message.out = false;
                                            tLRPC$TL_message.unread = false;
                                            TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                                            tLRPC$TL_message.from_id = tLRPC$TL_peerUser3;
                                            tLRPC$TL_peerUser3.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                            tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                            tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                            if (!TextUtils.isEmpty(str8)) {
                                                StringBuilder sb5 = new StringBuilder();
                                                sb5.append("https://");
                                                sb5.append(MessagesController.getInstance(messageObject.currentAccount).linkPrefix);
                                                str3 = str2;
                                                sb5.append(str3);
                                                sb5.append(str8);
                                                tLRPC$TL_message.message = sb5.toString();
                                            } else {
                                                str3 = str2;
                                                tLRPC$TL_message.message = str6;
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
                                                tLRPC$WebPage4.display_url = str6;
                                                tLRPC$WebPage4.url = str6;
                                                tLRPC$WebPage4.site_name = LocaleController.getString(R.string.EventLogPreviousLink);
                                                tLRPC$TL_message.media.webpage.description = "https://" + MessagesController.getInstance(messageObject.currentAccount).linkPrefix + str3 + ((TLRPC$TL_channelAdminLogEventActionChangeUsername) tLRPC$TL_channelAdminLogEvent2.action).prev_value;
                                            } else {
                                                tLRPC$TL_message.media = new TLRPC$TL_messageMediaEmpty();
                                            }
                                            tLRPC$Chat2 = tLRPC$Chat;
                                            str = str6;
                                            arrayList4 = null;
                                            if (messageObject.messageOwner == null) {
                                                messageObject.messageOwner = new TLRPC$TL_messageService();
                                            }
                                            messageObject.messageOwner.message = messageObject.messageText.toString();
                                            messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                            TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
                                            tLRPC$Message3.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                            tLRPC$Message3.date = tLRPC$TL_channelAdminLogEvent2.date;
                                            int i21 = iArr[0];
                                            iArr[0] = i21 + 1;
                                            tLRPC$Message3.id = i21;
                                            messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                            tLRPC$Message3.out = false;
                                            tLRPC$Message3.peer_id = new TLRPC$TL_peerChannel();
                                            TLRPC$Message tLRPC$Message4 = messageObject.messageOwner;
                                            tLRPC$Message4.peer_id.channel_id = tLRPC$Chat2.id;
                                            tLRPC$Message4.unread = false;
                                            mediaController = MediaController.getInstance();
                                            messageObject.isOutOwnerCached = null;
                                            tLRPC$TL_message = tLRPC$TL_message instanceof TLRPC$TL_messageEmpty ? null : tLRPC$TL_message;
                                            if (tLRPC$TL_message != null) {
                                                tLRPC$TL_message.out = false;
                                                tLRPC$TL_message.realId = tLRPC$TL_message.id;
                                                int i22 = iArr[0];
                                                iArr[0] = i22 + 1;
                                                tLRPC$TL_message.id = i22;
                                                tLRPC$TL_message.flags &= -32769;
                                                tLRPC$TL_message.dialog_id = -tLRPC$Chat2.id;
                                                if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                                    i6 = tLRPC$TL_message.date;
                                                    tLRPC$TL_message.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                } else {
                                                    i6 = 0;
                                                }
                                                MessageObject messageObject2 = new MessageObject(messageObject.currentAccount, tLRPC$TL_message, (AbstractMap<Long, TLRPC$User>) null, (AbstractMap<Long, TLRPC$Chat>) null, true, true, messageObject.eventId);
                                                messageObject2.realDate = i6;
                                                messageObject2.currentEvent = tLRPC$TL_channelAdminLogEvent2;
                                                if (messageObject2.contentType >= 0) {
                                                    if (mediaController.isPlayingMessage(messageObject2)) {
                                                        MessageObject playingMessageObject = mediaController.getPlayingMessageObject();
                                                        messageObject2.audioProgress = playingMessageObject.audioProgress;
                                                        messageObject2.audioProgressSec = playingMessageObject.audioProgressSec;
                                                    }
                                                    Object obj = null;
                                                    r13 = 0;
                                                    r13 = 0;
                                                    i5 = 1;
                                                    i5 = 1;
                                                    createDateArray(messageObject.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                                    if (z) {
                                                        arrayList5 = arrayList;
                                                        arrayList5.add(0, messageObject2);
                                                        r12 = obj;
                                                    } else {
                                                        arrayList5 = arrayList;
                                                        arrayList5.add(arrayList.size() - 1, messageObject2);
                                                        r12 = obj;
                                                    }
                                                } else {
                                                    arrayList5 = arrayList;
                                                    r12 = 0;
                                                    r13 = 0;
                                                    i5 = 1;
                                                    messageObject.contentType = -1;
                                                }
                                                if (arrayList4 != null) {
                                                    messageObject2.webPageDescriptionEntities = arrayList4;
                                                    messageObject2.linkDescription = r12;
                                                    messageObject2.generateLinkDescription();
                                                }
                                            } else {
                                                arrayList5 = arrayList;
                                                r12 = 0;
                                                r13 = 0;
                                                i5 = 1;
                                            }
                                            if (!(tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) && messageObject.contentType >= 0) {
                                                createDateArray(messageObject.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                                if (!z) {
                                                    arrayList5.add(r13, messageObject);
                                                } else {
                                                    arrayList5.add(arrayList.size() - i5, messageObject);
                                                }
                                                if (messageObject.messageText == null) {
                                                    messageObject.messageText = str;
                                                }
                                                if (!(getMedia(messageObject.messageOwner) instanceof TLRPC$TL_messageMediaGame)) {
                                                    textPaint = Theme.chat_msgGameTextPaint;
                                                } else {
                                                    textPaint = Theme.chat_msgTextPaint;
                                                }
                                                iArr2 = !allowsBigEmoji() ? new int[i5] : r12;
                                                CharSequence replaceEmoji = Emoji.replaceEmoji(messageObject.messageText, textPaint.getFontMetricsInt(), (boolean) r13, iArr2);
                                                messageObject.messageText = replaceEmoji;
                                                replaceAnimatedEmoji = messageObject.replaceAnimatedEmoji(replaceEmoji, textPaint.getFontMetricsInt());
                                                messageObject.messageText = replaceAnimatedEmoji;
                                                if (iArr2 != null && iArr2[r13] > i5) {
                                                    messageObject.replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr2);
                                                }
                                                messageObject.checkEmojiOnly(iArr2);
                                                setType();
                                                measureInlineBotButtons();
                                                generateCaption();
                                                if (mediaController.isPlayingMessage(messageObject)) {
                                                    MessageObject playingMessageObject2 = mediaController.getPlayingMessageObject();
                                                    messageObject.audioProgress = playingMessageObject2.audioProgress;
                                                    messageObject.audioProgressSec = playingMessageObject2.audioProgressSec;
                                                }
                                                messageObject.generateLayout(user2);
                                                messageObject.layoutCreated = i5;
                                                messageObject.generateThumbs(r13);
                                                checkMediaExistance();
                                                return;
                                            }
                                            return;
                                        } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionEditMessage) {
                                            TLRPC$TL_message tLRPC$TL_message4 = new TLRPC$TL_message();
                                            tLRPC$TL_message4.out = false;
                                            tLRPC$TL_message4.unread = false;
                                            tLRPC$TL_message4.peer_id = tLRPC$TL_peerChannel;
                                            tLRPC$TL_message4.date = tLRPC$TL_channelAdminLogEvent2.date;
                                            TLRPC$TL_channelAdminLogEventActionEditMessage tLRPC$TL_channelAdminLogEventActionEditMessage = (TLRPC$TL_channelAdminLogEventActionEditMessage) tLRPC$TL_channelAdminLogEvent2.action;
                                            TLRPC$Message tLRPC$Message5 = tLRPC$TL_channelAdminLogEventActionEditMessage.new_message;
                                            TLRPC$Message tLRPC$Message6 = tLRPC$TL_channelAdminLogEventActionEditMessage.prev_message;
                                            if (tLRPC$Message6 != null) {
                                                tLRPC$TL_message4.reply_to = tLRPC$Message6.reply_to;
                                                tLRPC$TL_message4.id = tLRPC$Message6.id;
                                            } else if (tLRPC$Message5 != null) {
                                                tLRPC$TL_message4.reply_to = tLRPC$Message5.reply_to;
                                                tLRPC$TL_message4.id = tLRPC$Message5.id;
                                            }
                                            if (tLRPC$Message5 != null && (tLRPC$Peer = tLRPC$Message5.from_id) != null) {
                                                tLRPC$TL_message4.from_id = tLRPC$Peer;
                                            } else {
                                                TLRPC$TL_peerUser tLRPC$TL_peerUser4 = new TLRPC$TL_peerUser();
                                                tLRPC$TL_message4.from_id = tLRPC$TL_peerUser4;
                                                tLRPC$TL_peerUser4.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                            }
                                            if (getMedia(tLRPC$Message5) != null && !(getMedia(tLRPC$Message5) instanceof TLRPC$TL_messageMediaEmpty) && !(getMedia(tLRPC$Message5) instanceof TLRPC$TL_messageMediaWebPage)) {
                                                boolean z9 = !TextUtils.equals(tLRPC$Message5.message, tLRPC$Message6.message);
                                                if (!(getMedia(tLRPC$Message5).getClass() == tLRPC$Message6.media.getClass() && ((getMedia(tLRPC$Message5).photo == null || tLRPC$Message6.media.photo == null || getMedia(tLRPC$Message5).photo.id == tLRPC$Message6.media.photo.id) && (getMedia(tLRPC$Message5).document == null || tLRPC$Message6.media.document == null || getMedia(tLRPC$Message5).document.id == tLRPC$Message6.media.document.id))) && z9) {
                                                    messageObject = this;
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedMediaCaption), "un1", user2);
                                                } else {
                                                    messageObject = this;
                                                    if (z9) {
                                                        messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedCaption), "un1", user2);
                                                    } else {
                                                        messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedMedia), "un1", user2);
                                                    }
                                                }
                                                TLRPC$MessageMedia media = getMedia(tLRPC$Message5);
                                                tLRPC$TL_message4.media = media;
                                                if (z9) {
                                                    media.webpage = new TLRPC$TL_webPage();
                                                    tLRPC$TL_message4.media.webpage.site_name = LocaleController.getString(R.string.EventLogOriginalCaption);
                                                    if (TextUtils.isEmpty(tLRPC$Message6.message)) {
                                                        tLRPC$TL_message4.media.webpage.description = LocaleController.getString(R.string.EventLogOriginalCaptionEmpty);
                                                    } else {
                                                        tLRPC$TL_message4.media.webpage.description = tLRPC$Message6.message;
                                                        arrayList2 = tLRPC$Message6.entities;
                                                        ArrayList<TLRPC$MessageEntity> arrayList6 = arrayList2;
                                                        tLRPC$TL_message = tLRPC$TL_message4;
                                                        arrayList3 = arrayList6;
                                                        tLRPC$TL_message.reply_markup = tLRPC$Message5.reply_markup;
                                                        tLRPC$WebPage = tLRPC$TL_message.media.webpage;
                                                        if (tLRPC$WebPage != null) {
                                                        }
                                                        tLRPC$Chat2 = tLRPC$Chat;
                                                        arrayList4 = arrayList3;
                                                        str = str6;
                                                    }
                                                }
                                                tLRPC$TL_message = tLRPC$TL_message4;
                                                arrayList3 = null;
                                                tLRPC$TL_message.reply_markup = tLRPC$Message5.reply_markup;
                                                tLRPC$WebPage = tLRPC$TL_message.media.webpage;
                                                if (tLRPC$WebPage != null) {
                                                }
                                                tLRPC$Chat2 = tLRPC$Chat;
                                                arrayList4 = arrayList3;
                                                str = str6;
                                            } else {
                                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEditedMessages), "un1", user2);
                                                if (tLRPC$Message5.action instanceof TLRPC$TL_messageActionGroupCall) {
                                                    tLRPC$Message5.media = new TLRPC$TL_messageMediaEmpty();
                                                    tLRPC$TL_message = tLRPC$Message5;
                                                    arrayList3 = null;
                                                    tLRPC$TL_message.reply_markup = tLRPC$Message5.reply_markup;
                                                    tLRPC$WebPage = tLRPC$TL_message.media.webpage;
                                                    if (tLRPC$WebPage != null) {
                                                        tLRPC$WebPage.flags = 10;
                                                        tLRPC$WebPage.display_url = str6;
                                                        tLRPC$WebPage.url = str6;
                                                    }
                                                    tLRPC$Chat2 = tLRPC$Chat;
                                                    arrayList4 = arrayList3;
                                                    str = str6;
                                                } else {
                                                    tLRPC$TL_message4.message = tLRPC$Message5.message;
                                                    tLRPC$TL_message4.entities = tLRPC$Message5.entities;
                                                    TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage4 = new TLRPC$TL_messageMediaWebPage();
                                                    tLRPC$TL_message4.media = tLRPC$TL_messageMediaWebPage4;
                                                    tLRPC$TL_messageMediaWebPage4.webpage = new TLRPC$TL_webPage();
                                                    tLRPC$TL_message4.media.webpage.site_name = LocaleController.getString(R.string.EventLogOriginalMessages);
                                                    if (TextUtils.isEmpty(tLRPC$Message6.message)) {
                                                        tLRPC$TL_message4.media.webpage.description = LocaleController.getString(R.string.EventLogOriginalCaptionEmpty);
                                                        tLRPC$TL_message = tLRPC$TL_message4;
                                                        arrayList3 = null;
                                                        tLRPC$TL_message.reply_markup = tLRPC$Message5.reply_markup;
                                                        tLRPC$WebPage = tLRPC$TL_message.media.webpage;
                                                        if (tLRPC$WebPage != null) {
                                                        }
                                                        tLRPC$Chat2 = tLRPC$Chat;
                                                        arrayList4 = arrayList3;
                                                        str = str6;
                                                    } else {
                                                        tLRPC$TL_message4.media.webpage.description = tLRPC$Message6.message;
                                                        arrayList2 = tLRPC$Message6.entities;
                                                        ArrayList<TLRPC$MessageEntity> arrayList62 = arrayList2;
                                                        tLRPC$TL_message = tLRPC$TL_message4;
                                                        arrayList3 = arrayList62;
                                                        tLRPC$TL_message.reply_markup = tLRPC$Message5.reply_markup;
                                                        tLRPC$WebPage = tLRPC$TL_message.media.webpage;
                                                        if (tLRPC$WebPage != null) {
                                                        }
                                                        tLRPC$Chat2 = tLRPC$Chat;
                                                        arrayList4 = arrayList3;
                                                        str = str6;
                                                    }
                                                }
                                            }
                                            if (messageObject.messageOwner == null) {
                                            }
                                            messageObject.messageOwner.message = messageObject.messageText.toString();
                                            messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                            TLRPC$Message tLRPC$Message32 = messageObject.messageOwner;
                                            tLRPC$Message32.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                            tLRPC$Message32.date = tLRPC$TL_channelAdminLogEvent2.date;
                                            int i212 = iArr[0];
                                            iArr[0] = i212 + 1;
                                            tLRPC$Message32.id = i212;
                                            messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                            tLRPC$Message32.out = false;
                                            tLRPC$Message32.peer_id = new TLRPC$TL_peerChannel();
                                            TLRPC$Message tLRPC$Message42 = messageObject.messageOwner;
                                            tLRPC$Message42.peer_id.channel_id = tLRPC$Chat2.id;
                                            tLRPC$Message42.unread = false;
                                            mediaController = MediaController.getInstance();
                                            messageObject.isOutOwnerCached = null;
                                            if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                            }
                                            if (tLRPC$TL_message != null) {
                                            }
                                            if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                                return;
                                            }
                                            createDateArray(messageObject.currentAccount, tLRPC$TL_channelAdminLogEvent, arrayList, hashMap, z);
                                            if (!z) {
                                            }
                                            if (messageObject.messageText == null) {
                                            }
                                            if (!(getMedia(messageObject.messageOwner) instanceof TLRPC$TL_messageMediaGame)) {
                                            }
                                            if (!allowsBigEmoji()) {
                                            }
                                            CharSequence replaceEmoji2 = Emoji.replaceEmoji(messageObject.messageText, textPaint.getFontMetricsInt(), (boolean) r13, iArr2);
                                            messageObject.messageText = replaceEmoji2;
                                            replaceAnimatedEmoji = messageObject.replaceAnimatedEmoji(replaceEmoji2, textPaint.getFontMetricsInt());
                                            messageObject.messageText = replaceAnimatedEmoji;
                                            if (iArr2 != null) {
                                                messageObject.replaceEmojiToLottieFrame(replaceAnimatedEmoji, iArr2);
                                            }
                                            messageObject.checkEmojiOnly(iArr2);
                                            setType();
                                            measureInlineBotButtons();
                                            generateCaption();
                                            if (mediaController.isPlayingMessage(messageObject)) {
                                            }
                                            messageObject.generateLayout(user2);
                                            messageObject.layoutCreated = i5;
                                            messageObject.generateThumbs(r13);
                                            checkMediaExistance();
                                            return;
                                        } else {
                                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeEmojiStickerSet) {
                                                TLRPC$InputStickerSet tLRPC$InputStickerSet = ((TLRPC$TL_channelAdminLogEventActionChangeEmojiStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                                if (tLRPC$InputStickerSet == null || (tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogRemovedEmojiPack), "un1", user2);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogChangedEmojiPack), "un1", user2);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeStickerSet) {
                                                TLRPC$InputStickerSet tLRPC$InputStickerSet2 = ((TLRPC$TL_channelAdminLogEventActionChangeStickerSet) tLRPC$ChannelAdminLogEventAction).new_stickerset;
                                                if (tLRPC$InputStickerSet2 == null || (tLRPC$InputStickerSet2 instanceof TLRPC$TL_inputStickerSetEmpty)) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogRemovedStickersSet), "un1", user2);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogChangedStickersSet), "un1", user2);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeLocation) {
                                                TLRPC$ChannelLocation tLRPC$ChannelLocation = ((TLRPC$TL_channelAdminLogEventActionChangeLocation) tLRPC$ChannelAdminLogEventAction).new_value;
                                                if (tLRPC$ChannelLocation instanceof TLRPC$TL_channelLocationEmpty) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogRemovedLocation), "un1", user2);
                                                } else {
                                                    messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogChangedLocation", R.string.EventLogChangedLocation, ((TLRPC$TL_channelLocation) tLRPC$ChannelLocation).address), "un1", user2);
                                                }
                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleSlowMode) {
                                                int i23 = ((TLRPC$TL_channelAdminLogEventActionToggleSlowMode) tLRPC$ChannelAdminLogEventAction).new_value;
                                                if (i23 == 0) {
                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogToggledSlowmodeOff), "un1", user2);
                                                } else {
                                                    if (i23 < 60) {
                                                        c3 = 0;
                                                        formatPluralString2 = LocaleController.formatPluralString("Seconds", i23, new Object[0]);
                                                    } else {
                                                        c3 = 0;
                                                        if (i23 < 3600) {
                                                            formatPluralString2 = LocaleController.formatPluralString(str5, i23 / 60, new Object[0]);
                                                        } else {
                                                            formatPluralString2 = LocaleController.formatPluralString("Hours", (i23 / 60) / 60, new Object[0]);
                                                        }
                                                    }
                                                    int i24 = R.string.EventLogToggledSlowmodeOn;
                                                    Object[] objArr = new Object[1];
                                                    objArr[c3] = formatPluralString2;
                                                    messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogToggledSlowmodeOn", i24, objArr), "un1", user2);
                                                }
                                            } else {
                                                if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionStartGroupCall) {
                                                    tLRPC$Chat3 = tLRPC$Chat;
                                                    if (ChatObject.isChannel(tLRPC$Chat) && (!tLRPC$Chat3.megagroup || tLRPC$Chat3.gigagroup)) {
                                                        messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogStartedLiveStream), "un1", user2);
                                                    } else {
                                                        messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogStartedVoiceChat), "un1", user2);
                                                    }
                                                } else {
                                                    tLRPC$Chat3 = tLRPC$Chat;
                                                    if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDiscardGroupCall) {
                                                        if (ChatObject.isChannel(tLRPC$Chat) && (!tLRPC$Chat3.megagroup || tLRPC$Chat3.gigagroup)) {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEndedLiveStream), "un1", user2);
                                                        } else {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogEndedVoiceChat), "un1", user2);
                                                        }
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantMute) {
                                                        long peerId3 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantMute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                                        if (peerId3 > 0) {
                                                            chat3 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId3));
                                                        } else {
                                                            chat3 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId3));
                                                        }
                                                        CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString(R.string.EventLogVoiceChatMuted), "un1", user2);
                                                        messageObject.messageText = replaceWithLink6;
                                                        messageObject.messageText = replaceWithLink(replaceWithLink6, "un2", chat3);
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantUnmute) {
                                                        long peerId4 = getPeerId(((TLRPC$TL_channelAdminLogEventActionParticipantUnmute) tLRPC$ChannelAdminLogEventAction).participant.peer);
                                                        if (peerId4 > 0) {
                                                            chat2 = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId4));
                                                        } else {
                                                            chat2 = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId4));
                                                        }
                                                        CharSequence replaceWithLink7 = replaceWithLink(LocaleController.getString(R.string.EventLogVoiceChatUnmuted), "un1", user2);
                                                        messageObject.messageText = replaceWithLink7;
                                                        messageObject.messageText = replaceWithLink(replaceWithLink7, "un2", chat2);
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) {
                                                        if (((TLRPC$TL_channelAdminLogEventActionToggleGroupCallSetting) tLRPC$ChannelAdminLogEventAction).join_muted) {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogVoiceChatNotAllowedToSpeak), "un1", user2);
                                                        } else {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogVoiceChatAllowedToSpeak), "un1", user2);
                                                        }
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) {
                                                        TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite = (TLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite) tLRPC$ChannelAdminLogEventAction;
                                                        if (tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite.via_chatlist) {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat) ? R.string.ActionInviteChannelUserFolder : R.string.ActionInviteUserFolder), "un1", user2);
                                                        } else {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat) ? R.string.ActionInviteChannelUser : R.string.ActionInviteUser), "un1", user2);
                                                        }
                                                        TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported = tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite.invite;
                                                        if (tLRPC$TL_chatInviteExported != null && !TextUtils.isEmpty(tLRPC$TL_chatInviteExported.link)) {
                                                            messageObject.messageText = TextUtils.concat(messageObject.messageText, " ", tLRPC$TL_channelAdminLogEventActionParticipantJoinByInvite.invite.link);
                                                        }
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionToggleNoForwards) {
                                                        TLRPC$TL_channelAdminLogEventActionToggleNoForwards tLRPC$TL_channelAdminLogEventActionToggleNoForwards = (TLRPC$TL_channelAdminLogEventActionToggleNoForwards) tLRPC$ChannelAdminLogEventAction;
                                                        boolean z10 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat3.megagroup;
                                                        if (tLRPC$TL_channelAdminLogEventActionToggleNoForwards.new_value) {
                                                            if (z10) {
                                                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.ActionForwardsRestrictedChannel), "un1", user2);
                                                            } else {
                                                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.ActionForwardsRestrictedGroup), "un1", user2);
                                                            }
                                                        } else if (z10) {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.ActionForwardsEnabledChannel), "un1", user2);
                                                        } else {
                                                            messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.ActionForwardsEnabledGroup), "un1", user2);
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
                                                        String str9 = tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite.link;
                                                        if (str9 != null && str9.equals(tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite.link)) {
                                                            messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkToSameClickable", R.string.ActionEditedInviteLinkToSameClickable, new Object[0]), "un1", user2);
                                                        } else {
                                                            messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkClickable", R.string.ActionEditedInviteLinkClickable, new Object[0]), "un1", user2);
                                                        }
                                                        CharSequence replaceWithLink10 = replaceWithLink(messageObject.messageText, "un2", tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.prev_invite);
                                                        messageObject.messageText = replaceWithLink10;
                                                        messageObject.messageText = replaceWithLink(replaceWithLink10, "un3", tLRPC$TL_channelAdminLogEventActionExportedInviteEdit.new_invite);
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantVolume) {
                                                        TLRPC$TL_channelAdminLogEventActionParticipantVolume tLRPC$TL_channelAdminLogEventActionParticipantVolume = (TLRPC$TL_channelAdminLogEventActionParticipantVolume) tLRPC$ChannelAdminLogEventAction;
                                                        long peerId5 = getPeerId(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant.peer);
                                                        if (peerId5 > 0) {
                                                            chat = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId5));
                                                        } else {
                                                            chat = MessagesController.getInstance(messageObject.currentAccount).getChat(Long.valueOf(-peerId5));
                                                        }
                                                        double participantVolume = ChatObject.getParticipantVolume(tLRPC$TL_channelAdminLogEventActionParticipantVolume.participant);
                                                        Double.isNaN(participantVolume);
                                                        double d = participantVolume / 100.0d;
                                                        CharSequence replaceWithLink11 = replaceWithLink(LocaleController.formatString("ActionVolumeChanged", R.string.ActionVolumeChanged, Integer.valueOf((int) (d > 0.0d ? Math.max(d, 1.0d) : 0.0d))), "un1", user2);
                                                        messageObject.messageText = replaceWithLink11;
                                                        messageObject.messageText = replaceWithLink(replaceWithLink11, "un2", chat);
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) {
                                                        TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL = (TLRPC$TL_channelAdminLogEventActionChangeHistoryTTL) tLRPC$ChannelAdminLogEventAction;
                                                        if (!tLRPC$Chat3.megagroup) {
                                                            int i25 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                            if (i25 != 0) {
                                                                messageObject.messageText = LocaleController.formatString("ActionTTLChannelChanged", R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i25));
                                                            } else {
                                                                messageObject.messageText = LocaleController.getString(R.string.ActionTTLChannelDisabled);
                                                            }
                                                        } else {
                                                            int i26 = tLRPC$TL_channelAdminLogEventActionChangeHistoryTTL.new_value;
                                                            if (i26 == 0) {
                                                                messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.ActionTTLDisabled), "un1", user2);
                                                            } else {
                                                                if (i26 > 86400) {
                                                                    c2 = 0;
                                                                    formatPluralString = LocaleController.formatPluralString("Days", i26 / 86400, new Object[0]);
                                                                } else {
                                                                    c2 = 0;
                                                                    if (i26 >= 3600) {
                                                                        formatPluralString = LocaleController.formatPluralString("Hours", i26 / 3600, new Object[0]);
                                                                    } else if (i26 >= 60) {
                                                                        formatPluralString = LocaleController.formatPluralString(str5, i26 / 60, new Object[0]);
                                                                    } else {
                                                                        formatPluralString = LocaleController.formatPluralString("Seconds", i26, new Object[0]);
                                                                    }
                                                                }
                                                                int i27 = R.string.ActionTTLChanged;
                                                                Object[] objArr2 = new Object[1];
                                                                objArr2[c2] = formatPluralString;
                                                                messageObject.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", i27, objArr2), "un1", user2);
                                                            }
                                                        }
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) {
                                                        TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest = (TLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest) tLRPC$ChannelAdminLogEventAction;
                                                        TLRPC$ExportedChatInvite tLRPC$ExportedChatInvite = tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite;
                                                        if (((tLRPC$ExportedChatInvite instanceof TLRPC$TL_chatInviteExported) && "https://t.me/+PublicChat".equals(((TLRPC$TL_chatInviteExported) tLRPC$ExportedChatInvite).link)) || (tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite instanceof TLRPC$TL_chatInvitePublicJoinRequests)) {
                                                            CharSequence replaceWithLink12 = replaceWithLink(LocaleController.getString(R.string.JoinedViaRequestApproved), "un1", user2);
                                                            messageObject.messageText = replaceWithLink12;
                                                            messageObject.messageText = replaceWithLink(replaceWithLink12, "un2", MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by)));
                                                        } else {
                                                            CharSequence replaceWithLink13 = replaceWithLink(LocaleController.getString(R.string.JoinedViaInviteLinkApproved), "un1", user2);
                                                            messageObject.messageText = replaceWithLink13;
                                                            CharSequence replaceWithLink14 = replaceWithLink(replaceWithLink13, "un2", tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.invite);
                                                            messageObject.messageText = replaceWithLink14;
                                                            messageObject.messageText = replaceWithLink(replaceWithLink14, "un3", MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(tLRPC$TL_channelAdminLogEventActionParticipantJoinByRequest.approved_by)));
                                                        }
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionSendMessage) {
                                                        TLRPC$Message tLRPC$Message7 = ((TLRPC$TL_channelAdminLogEventActionSendMessage) tLRPC$ChannelAdminLogEventAction).message;
                                                        messageObject.messageText = replaceWithLink(LocaleController.getString(R.string.EventLogSendMessages), "un1", user2);
                                                        tLRPC$TL_message = tLRPC$Message7;
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) {
                                                        TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions = (TLRPC$TL_channelAdminLogEventActionChangeAvailableReactions) tLRPC$ChannelAdminLogEventAction;
                                                        boolean z11 = (tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.prev_value instanceof TLRPC$TL_chatReactionsSome) && (tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.new_value instanceof TLRPC$TL_chatReactionsSome);
                                                        CharSequence stringFrom = messageObject.getStringFrom(tLRPC$TL_channelAdminLogEventActionChangeAvailableReactions.new_value);
                                                        if (z11) {
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
                                                        ArrayList<String> arrayList7 = tLRPC$TL_channelAdminLogEventActionChangeUsernames.prev_value;
                                                        ArrayList<String> arrayList8 = tLRPC$TL_channelAdminLogEventActionChangeUsernames.new_value;
                                                        messageObject.messageText = null;
                                                        if (arrayList7 != null && arrayList8 != null) {
                                                            if (arrayList8.size() + 1 == arrayList7.size()) {
                                                                String str10 = null;
                                                                int i28 = 0;
                                                                while (true) {
                                                                    if (i28 >= arrayList7.size()) {
                                                                        break;
                                                                    }
                                                                    String str11 = arrayList7.get(i28);
                                                                    if (!arrayList8.contains(str11)) {
                                                                        if (str10 != null) {
                                                                            str10 = null;
                                                                            break;
                                                                        }
                                                                        str10 = str11;
                                                                    }
                                                                    i28++;
                                                                }
                                                                if (str10 != null) {
                                                                    messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogDeactivatedUsername", R.string.EventLogDeactivatedUsername, "@" + str10), "un1", user2);
                                                                }
                                                            } else if (arrayList7.size() + 1 == arrayList8.size()) {
                                                                String str12 = null;
                                                                int i29 = 0;
                                                                while (true) {
                                                                    if (i29 >= arrayList8.size()) {
                                                                        break;
                                                                    }
                                                                    String str13 = arrayList8.get(i29);
                                                                    if (!arrayList7.contains(str13)) {
                                                                        if (str12 != null) {
                                                                            str12 = null;
                                                                            break;
                                                                        }
                                                                        str12 = str13;
                                                                    }
                                                                    i29++;
                                                                }
                                                                if (str12 != null) {
                                                                    messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogActivatedUsername", R.string.EventLogActivatedUsername, "@" + str12), "un1", user2);
                                                                }
                                                            }
                                                        }
                                                        if (messageObject.messageText == null) {
                                                            messageObject.messageText = replaceWithLink(LocaleController.formatString("EventLogChangeUsernames", R.string.EventLogChangeUsernames, messageObject.getUsernamesString(arrayList7), messageObject.getUsernamesString(arrayList8)), "un1", user2);
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
                                                            if (tLRPC$ForumTopic2 instanceof TLRPC$TL_forumTopic) {
                                                                boolean z12 = ((TLRPC$TL_forumTopic) tLRPC$ForumTopic).hidden;
                                                                boolean z13 = ((TLRPC$TL_forumTopic) tLRPC$ForumTopic2).hidden;
                                                                if (z12 != z13) {
                                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(z13 ? R.string.TopicHidden2 : R.string.TopicShown2), "%s", user2);
                                                                }
                                                            }
                                                        }
                                                        CharSequence replaceWithLink16 = replaceWithLink(LocaleController.getString(R.string.EventLogEditTopic), "un1", user2);
                                                        messageObject.messageText = replaceWithLink16;
                                                        CharSequence replaceWithLink17 = replaceWithLink(replaceWithLink16, "un2", tLRPC$TL_channelAdminLogEventActionEditTopic.prev_topic);
                                                        messageObject.messageText = replaceWithLink17;
                                                        messageObject.messageText = replaceWithLink(replaceWithLink17, "un3", tLRPC$TL_channelAdminLogEventActionEditTopic.new_topic);
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionDeleteTopic) {
                                                        CharSequence replaceWithLink18 = replaceWithLink(LocaleController.getString(R.string.EventLogDeleteTopic), "un1", user2);
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
                                                            string2 = LocaleController.getString(R.string.EventLogEnabledAntiSpam);
                                                        } else {
                                                            string2 = LocaleController.getString(R.string.EventLogDisabledAntiSpam);
                                                        }
                                                        messageObject.messageText = replaceWithLink(string2, "un1", user2);
                                                    } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeColor) {
                                                        boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                        TLRPC$TL_channelAdminLogEventActionChangeColor tLRPC$TL_channelAdminLogEventActionChangeColor = (TLRPC$TL_channelAdminLogEventActionChangeColor) tLRPC$TL_channelAdminLogEvent2.action;
                                                        messageObject.messageText = replaceWithLink(LocaleController.formatString(isChannelAndNotMegaGroup ? R.string.EventLogChangedColor : R.string.EventLogChangedColorGroup, AvatarDrawable.colorName(tLRPC$TL_channelAdminLogEventActionChangeColor.prev_value).toLowerCase(), AvatarDrawable.colorName(tLRPC$TL_channelAdminLogEventActionChangeColor.new_value).toLowerCase()), "un1", user2);
                                                    } else {
                                                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangePeerColor) {
                                                            boolean isChannelAndNotMegaGroup2 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                            TLRPC$TL_channelAdminLogEventActionChangePeerColor tLRPC$TL_channelAdminLogEventActionChangePeerColor = (TLRPC$TL_channelAdminLogEventActionChangePeerColor) tLRPC$TL_channelAdminLogEvent2.action;
                                                            SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(LocaleController.getString(isChannelAndNotMegaGroup2 ? R.string.EventLogChangedPeerColorIcon : R.string.EventLogChangedPeerColorIconGroup));
                                                            SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder();
                                                            if ((tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.flags & 1) != 0) {
                                                                spannableStringBuilder4.append((CharSequence) "c");
                                                                str = str6;
                                                                spannableStringBuilder4.setSpan(new PeerColorActivity.PeerColorSpan(false, messageObject.currentAccount, tLRPC$TL_channelAdminLogEventActionChangePeerColor.prev_value.color).setSize(AndroidUtilities.dp(18.0f)), spannableStringBuilder4.length() - 1, spannableStringBuilder4.length(), 33);
                                                            } else {
                                                                str = str6;
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
                                                            str = str6;
                                                            if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor) {
                                                                boolean isChannelAndNotMegaGroup3 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                                TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor tLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor = (TLRPC$TL_channelAdminLogEventActionChangeProfilePeerColor) tLRPC$TL_channelAdminLogEvent2.action;
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
                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeEmojiStatus) {
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
                                                                boolean z14 = tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil;
                                                                if (tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusEmpty) {
                                                                    spannableString4 = new SpannableString(LocaleController.getString(R.string.EventLogEmojiNone));
                                                                } else {
                                                                    spannableString4 = new SpannableString("e");
                                                                    spannableString4.setSpan(new AnimatedEmojiSpan(DialogObject.getEmojiStatusDocumentId(tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.new_value), Theme.chat_actionTextPaint.getFontMetricsInt()), 0, 1, 33);
                                                                }
                                                                if (z3) {
                                                                    if (z14) {
                                                                        i2 = isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFor : R.string.EventLogChangedEmojiStatusForGroup;
                                                                    } else {
                                                                        i2 = isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatus : R.string.EventLogChangedEmojiStatusGroup;
                                                                    }
                                                                } else if (z14) {
                                                                    i2 = isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFromFor : R.string.EventLogChangedEmojiStatusFromForGroup;
                                                                } else {
                                                                    i2 = isChannelAndNotMegaGroup4 ? R.string.EventLogChangedEmojiStatusFrom : R.string.EventLogChangedEmojiStatusFromGroup;
                                                                }
                                                                SpannableStringBuilder replaceCharSequence = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", new SpannableStringBuilder(LocaleController.getString(i2)), spannableString3), spannableString4);
                                                                messageObject.messageText = replaceWithLink(z14 ? AndroidUtilities.replaceCharSequence("%3$s", replaceCharSequence, LocaleController.formatTTLString((int) ((DialogObject.getEmojiStatusUntil(tLRPC$TL_channelAdminLogEventActionChangeEmojiStatus.new_value) - tLRPC$TL_channelAdminLogEvent2.date) * 1.05f))) : replaceCharSequence, "un1", user2);
                                                            } else if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeWallpaper) {
                                                                TLRPC$TL_channelAdminLogEventActionChangeWallpaper tLRPC$TL_channelAdminLogEventActionChangeWallpaper = (TLRPC$TL_channelAdminLogEventActionChangeWallpaper) tLRPC$ChannelAdminLogEventAction;
                                                                boolean isChannelAndNotMegaGroup5 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
                                                                TLRPC$WallPaper tLRPC$WallPaper = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value;
                                                                if ((tLRPC$WallPaper instanceof TLRPC$TL_wallPaperNoFile) && tLRPC$WallPaper.id == 0 && tLRPC$WallPaper.settings == null) {
                                                                    messageObject.messageText = replaceWithLink(LocaleController.getString(isChannelAndNotMegaGroup5 ? R.string.EventLogRemovedWallpaper : R.string.EventLogRemovedWallpaperGroup), "un1", user2);
                                                                } else {
                                                                    ArrayList<TLRPC$PhotoSize> arrayList9 = new ArrayList<>();
                                                                    messageObject.photoThumbs = arrayList9;
                                                                    TLRPC$Document tLRPC$Document = tLRPC$TL_channelAdminLogEventActionChangeWallpaper.new_value.document;
                                                                    if (tLRPC$Document != null) {
                                                                        arrayList9.addAll(tLRPC$Document.thumbs);
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
                                                        arrayList4 = null;
                                                        tLRPC$TL_message = null;
                                                        if (messageObject.messageOwner == null) {
                                                        }
                                                        messageObject.messageOwner.message = messageObject.messageText.toString();
                                                        messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                                        TLRPC$Message tLRPC$Message322 = messageObject.messageOwner;
                                                        tLRPC$Message322.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                        tLRPC$Message322.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                        int i2122 = iArr[0];
                                                        iArr[0] = i2122 + 1;
                                                        tLRPC$Message322.id = i2122;
                                                        messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                                        tLRPC$Message322.out = false;
                                                        tLRPC$Message322.peer_id = new TLRPC$TL_peerChannel();
                                                        TLRPC$Message tLRPC$Message422 = messageObject.messageOwner;
                                                        tLRPC$Message422.peer_id.channel_id = tLRPC$Chat2.id;
                                                        tLRPC$Message422.unread = false;
                                                        mediaController = MediaController.getInstance();
                                                        messageObject.isOutOwnerCached = null;
                                                        if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                                        }
                                                        if (tLRPC$TL_message != null) {
                                                        }
                                                        if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                                        }
                                                    }
                                                }
                                                tLRPC$Chat2 = tLRPC$Chat3;
                                                str = str6;
                                                arrayList4 = null;
                                                tLRPC$TL_message = null;
                                                if (messageObject.messageOwner == null) {
                                                }
                                                messageObject.messageOwner.message = messageObject.messageText.toString();
                                                messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                                TLRPC$Message tLRPC$Message3222 = messageObject.messageOwner;
                                                tLRPC$Message3222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                                tLRPC$Message3222.date = tLRPC$TL_channelAdminLogEvent2.date;
                                                int i21222 = iArr[0];
                                                iArr[0] = i21222 + 1;
                                                tLRPC$Message3222.id = i21222;
                                                messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                                tLRPC$Message3222.out = false;
                                                tLRPC$Message3222.peer_id = new TLRPC$TL_peerChannel();
                                                TLRPC$Message tLRPC$Message4222 = messageObject.messageOwner;
                                                tLRPC$Message4222.peer_id.channel_id = tLRPC$Chat2.id;
                                                tLRPC$Message4222.unread = false;
                                                mediaController = MediaController.getInstance();
                                                messageObject.isOutOwnerCached = null;
                                                if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                                }
                                                if (tLRPC$TL_message != null) {
                                                }
                                                if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                                }
                                            }
                                            tLRPC$Chat2 = tLRPC$Chat;
                                            str = str6;
                                            arrayList4 = null;
                                            tLRPC$TL_message = null;
                                            if (messageObject.messageOwner == null) {
                                            }
                                            messageObject.messageOwner.message = messageObject.messageText.toString();
                                            messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                            TLRPC$Message tLRPC$Message32222 = messageObject.messageOwner;
                                            tLRPC$Message32222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                            tLRPC$Message32222.date = tLRPC$TL_channelAdminLogEvent2.date;
                                            int i212222 = iArr[0];
                                            iArr[0] = i212222 + 1;
                                            tLRPC$Message32222.id = i212222;
                                            messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                            tLRPC$Message32222.out = false;
                                            tLRPC$Message32222.peer_id = new TLRPC$TL_peerChannel();
                                            TLRPC$Message tLRPC$Message42222 = messageObject.messageOwner;
                                            tLRPC$Message42222.peer_id.channel_id = tLRPC$Chat2.id;
                                            tLRPC$Message42222.unread = false;
                                            mediaController = MediaController.getInstance();
                                            messageObject.isOutOwnerCached = null;
                                            if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                            }
                                            if (tLRPC$TL_message != null) {
                                            }
                                            if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                            }
                                        }
                                    }
                                    tLRPC$Chat2 = tLRPC$Chat3;
                                    str = str6;
                                    arrayList4 = null;
                                    if (messageObject.messageOwner == null) {
                                    }
                                    messageObject.messageOwner.message = messageObject.messageText.toString();
                                    messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                                    TLRPC$Message tLRPC$Message322222 = messageObject.messageOwner;
                                    tLRPC$Message322222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                                    tLRPC$Message322222.date = tLRPC$TL_channelAdminLogEvent2.date;
                                    int i2122222 = iArr[0];
                                    iArr[0] = i2122222 + 1;
                                    tLRPC$Message322222.id = i2122222;
                                    messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                                    tLRPC$Message322222.out = false;
                                    tLRPC$Message322222.peer_id = new TLRPC$TL_peerChannel();
                                    TLRPC$Message tLRPC$Message422222 = messageObject.messageOwner;
                                    tLRPC$Message422222.peer_id.channel_id = tLRPC$Chat2.id;
                                    tLRPC$Message422222.unread = false;
                                    mediaController = MediaController.getInstance();
                                    messageObject.isOutOwnerCached = null;
                                    if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                                    }
                                    if (tLRPC$TL_message != null) {
                                    }
                                    if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                                    }
                                }
                            }
                            tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                            tLRPC$Chat2 = tLRPC$Chat3;
                            arrayList4 = null;
                            tLRPC$TL_message = null;
                            if (messageObject.messageOwner == null) {
                            }
                            messageObject.messageOwner.message = messageObject.messageText.toString();
                            messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                            TLRPC$Message tLRPC$Message3222222 = messageObject.messageOwner;
                            tLRPC$Message3222222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                            tLRPC$Message3222222.date = tLRPC$TL_channelAdminLogEvent2.date;
                            int i21222222 = iArr[0];
                            iArr[0] = i21222222 + 1;
                            tLRPC$Message3222222.id = i21222222;
                            messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                            tLRPC$Message3222222.out = false;
                            tLRPC$Message3222222.peer_id = new TLRPC$TL_peerChannel();
                            TLRPC$Message tLRPC$Message4222222 = messageObject.messageOwner;
                            tLRPC$Message4222222.peer_id.channel_id = tLRPC$Chat2.id;
                            tLRPC$Message4222222.unread = false;
                            mediaController = MediaController.getInstance();
                            messageObject.isOutOwnerCached = null;
                            if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                            }
                            if (tLRPC$TL_message != null) {
                            }
                            if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                            }
                        }
                        tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                        tLRPC$Chat2 = tLRPC$Chat;
                        tLRPC$TL_message = tLRPC$Message;
                        arrayList4 = null;
                        if (messageObject.messageOwner == null) {
                        }
                        messageObject.messageOwner.message = messageObject.messageText.toString();
                        messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                        TLRPC$Message tLRPC$Message32222222 = messageObject.messageOwner;
                        tLRPC$Message32222222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                        tLRPC$Message32222222.date = tLRPC$TL_channelAdminLogEvent2.date;
                        int i212222222 = iArr[0];
                        iArr[0] = i212222222 + 1;
                        tLRPC$Message32222222.id = i212222222;
                        messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                        tLRPC$Message32222222.out = false;
                        tLRPC$Message32222222.peer_id = new TLRPC$TL_peerChannel();
                        TLRPC$Message tLRPC$Message42222222 = messageObject.messageOwner;
                        tLRPC$Message42222222.peer_id.channel_id = tLRPC$Chat2.id;
                        tLRPC$Message42222222.unread = false;
                        mediaController = MediaController.getInstance();
                        messageObject.isOutOwnerCached = null;
                        if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                        }
                        if (tLRPC$TL_message != null) {
                        }
                        if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                        }
                    }
                    tLRPC$TL_channelAdminLogEvent2 = tLRPC$TL_channelAdminLogEvent;
                    tLRPC$Chat2 = tLRPC$Chat;
                    arrayList4 = null;
                    tLRPC$TL_message = null;
                    if (messageObject.messageOwner == null) {
                    }
                    messageObject.messageOwner.message = messageObject.messageText.toString();
                    messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
                    TLRPC$Message tLRPC$Message322222222 = messageObject.messageOwner;
                    tLRPC$Message322222222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
                    tLRPC$Message322222222.date = tLRPC$TL_channelAdminLogEvent2.date;
                    int i2122222222 = iArr[0];
                    iArr[0] = i2122222222 + 1;
                    tLRPC$Message322222222.id = i2122222222;
                    messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
                    tLRPC$Message322222222.out = false;
                    tLRPC$Message322222222.peer_id = new TLRPC$TL_peerChannel();
                    TLRPC$Message tLRPC$Message422222222 = messageObject.messageOwner;
                    tLRPC$Message422222222.peer_id.channel_id = tLRPC$Chat2.id;
                    tLRPC$Message422222222.unread = false;
                    mediaController = MediaController.getInstance();
                    messageObject.isOutOwnerCached = null;
                    if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
                    }
                    if (tLRPC$TL_message != null) {
                    }
                    if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
                    }
                }
            }
            str = "";
            if (z2) {
                TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin = (TLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin) tLRPC$ChannelAdminLogEventAction;
                tLRPC$ChannelParticipant = tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin.prev_participant;
                tLRPC$ChannelParticipant2 = tLRPC$TL_channelAdminLogEventActionParticipantToggleAdmin.new_participant;
            } else {
                TLRPC$TL_channelAdminLogEventActionParticipantToggleBan tLRPC$TL_channelAdminLogEventActionParticipantToggleBan3 = (TLRPC$TL_channelAdminLogEventActionParticipantToggleBan) tLRPC$ChannelAdminLogEventAction;
                tLRPC$ChannelParticipant = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan3.prev_participant;
                tLRPC$ChannelParticipant2 = tLRPC$TL_channelAdminLogEventActionParticipantToggleBan3.new_participant;
            }
            TLRPC$TL_message tLRPC$TL_message5 = new TLRPC$TL_message();
            messageObject.messageOwner = tLRPC$TL_message5;
            tLRPC$TL_message5.realId = -1;
            long peerId6 = getPeerId(tLRPC$ChannelParticipant.peer);
            if (peerId6 > 0) {
                user = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(peerId6));
            } else {
                user = MessagesController.getInstance(messageObject.currentAccount).getUser(Long.valueOf(-peerId6));
            }
            if (!(tLRPC$ChannelParticipant instanceof TLRPC$TL_channelParticipantCreator) && (tLRPC$ChannelParticipant2 instanceof TLRPC$TL_channelParticipantCreator)) {
                String string5 = LocaleController.getString(R.string.EventLogChangedOwnership);
                sb = new StringBuilder(String.format(string5, messageObject.getUserName(user, messageObject.messageOwner.entities, string5.indexOf("%1$s"))));
                tLRPC$Chat2 = tLRPC$Chat;
            } else {
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$ChannelParticipant.admin_rights;
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights2 = tLRPC$ChannelParticipant2.admin_rights;
                tLRPC$TL_chatAdminRights = tLRPC$TL_chatAdminRights == null ? new TLRPC$TL_chatAdminRights() : tLRPC$TL_chatAdminRights;
                tLRPC$TL_chatAdminRights2 = tLRPC$TL_chatAdminRights2 == null ? new TLRPC$TL_chatAdminRights() : tLRPC$TL_chatAdminRights2;
                if (tLRPC$TL_chatAdminRights2.other) {
                    string = LocaleController.getString(R.string.EventLogPromotedNoRights);
                } else {
                    string = LocaleController.getString(R.string.EventLogPromoted);
                }
                sb = new StringBuilder(String.format(string, messageObject.getUserName(user, messageObject.messageOwner.entities, string.indexOf("%1$s"))));
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
            }
            messageObject.messageText = sb.toString();
            arrayList4 = null;
            tLRPC$TL_message = null;
            if (messageObject.messageOwner == null) {
            }
            messageObject.messageOwner.message = messageObject.messageText.toString();
            messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
            TLRPC$Message tLRPC$Message3222222222 = messageObject.messageOwner;
            tLRPC$Message3222222222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
            tLRPC$Message3222222222.date = tLRPC$TL_channelAdminLogEvent2.date;
            int i21222222222 = iArr[0];
            iArr[0] = i21222222222 + 1;
            tLRPC$Message3222222222.id = i21222222222;
            messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
            tLRPC$Message3222222222.out = false;
            tLRPC$Message3222222222.peer_id = new TLRPC$TL_peerChannel();
            TLRPC$Message tLRPC$Message4222222222 = messageObject.messageOwner;
            tLRPC$Message4222222222.peer_id.channel_id = tLRPC$Chat2.id;
            tLRPC$Message4222222222.unread = false;
            mediaController = MediaController.getInstance();
            messageObject.isOutOwnerCached = null;
            if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
            }
            if (tLRPC$TL_message != null) {
            }
            if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
            }
        }
        tLRPC$Chat2 = tLRPC$Chat;
        str = str6;
        arrayList4 = null;
        tLRPC$TL_message = null;
        if (messageObject.messageOwner == null) {
        }
        messageObject.messageOwner.message = messageObject.messageText.toString();
        messageObject.messageOwner.from_id = new TLRPC$TL_peerUser();
        TLRPC$Message tLRPC$Message32222222222 = messageObject.messageOwner;
        tLRPC$Message32222222222.from_id.user_id = tLRPC$TL_channelAdminLogEvent2.user_id;
        tLRPC$Message32222222222.date = tLRPC$TL_channelAdminLogEvent2.date;
        int i212222222222 = iArr[0];
        iArr[0] = i212222222222 + 1;
        tLRPC$Message32222222222.id = i212222222222;
        messageObject.eventId = tLRPC$TL_channelAdminLogEvent2.id;
        tLRPC$Message32222222222.out = false;
        tLRPC$Message32222222222.peer_id = new TLRPC$TL_peerChannel();
        TLRPC$Message tLRPC$Message42222222222 = messageObject.messageOwner;
        tLRPC$Message42222222222.peer_id.channel_id = tLRPC$Chat2.id;
        tLRPC$Message42222222222.unread = false;
        mediaController = MediaController.getInstance();
        messageObject.isOutOwnerCached = null;
        if (tLRPC$TL_message instanceof TLRPC$TL_messageEmpty) {
        }
        if (tLRPC$TL_message != null) {
        }
        if (tLRPC$TL_channelAdminLogEvent2.action instanceof TLRPC$TL_channelAdminLogEventActionDeleteMessage) {
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
            return LocaleController.getString(R.string.AllReactions);
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
        return LocaleController.getString(R.string.NoReactions);
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
                if (tLRPC$User.deleted) {
                    str = LocaleController.getString(R.string.HiddenName);
                } else {
                    str = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                }
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

    public boolean updateTranslation() {
        return updateTranslation(false);
    }

    public boolean updateTranslation(boolean z) {
        TLRPC$Message tLRPC$Message;
        MessageObject messageObject = this.replyMessageObject;
        boolean z2 = messageObject != null && messageObject.updateTranslation(z);
        TranslateController translateController = MessagesController.getInstance(this.currentAccount).getTranslateController();
        if (TranslateController.isTranslatable(this) && translateController.isTranslatingDialog(getDialogId()) && !translateController.isTranslateDialogHidden(getDialogId()) && (tLRPC$Message = this.messageOwner) != null && tLRPC$Message.translatedText != null && TextUtils.equals(translateController.getDialogTranslateTo(getDialogId()), this.messageOwner.translatedToLanguage)) {
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
        MessageObject messageObject = this.replyMessageObject;
        TLRPC$TL_game tLRPC$TL_game = (messageObject == null || getMedia(messageObject) == null || getMedia(this.replyMessageObject).game == null) ? null : getMedia(this.replyMessageObject).game;
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
        String str2;
        if (tLRPC$User == null) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
        }
        if (tLRPC$User != null) {
            str = UserObject.getFirstName(tLRPC$User);
        } else {
            str = "";
        }
        try {
            if ("XTR".equals(this.messageOwner.action.currency)) {
                str2 = "XTR " + this.messageOwner.action.total_amount;
            } else {
                LocaleController localeController = LocaleController.getInstance();
                TLRPC$MessageAction tLRPC$MessageAction = this.messageOwner.action;
                str2 = localeController.formatCurrencyString(tLRPC$MessageAction.total_amount, tLRPC$MessageAction.currency);
            }
        } catch (Exception e) {
            FileLog.e(e);
            str2 = "<error>";
        }
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null && (getMedia(messageObject) instanceof TLRPC$TL_messageMediaInvoice)) {
            if (this.messageOwner.action.recurring_init) {
                this.messageText = LocaleController.formatString(R.string.PaymentSuccessfullyPaidRecurrent, str2, str, getMedia(this.replyMessageObject).title);
            } else {
                this.messageText = LocaleController.formatString("PaymentSuccessfullyPaid", R.string.PaymentSuccessfullyPaid, str2, str, getMedia(this.replyMessageObject).title);
            }
        } else if (this.messageOwner.action.recurring_init) {
            this.messageText = LocaleController.formatString(R.string.PaymentSuccessfullyPaidNoItemRecurrent, str2, str);
        } else {
            this.messageText = LocaleController.formatString("PaymentSuccessfullyPaidNoItem", R.string.PaymentSuccessfullyPaidNoItem, str2, str);
        }
        this.messageText = StarsIntroActivity.replaceStars(this.messageText);
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
                    String string = LocaleController.getString(R.string.ActionPinnedMusic);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string, "un1", tLRPC$User);
                    return;
                } else if (this.replyMessageObject.isVideo()) {
                    String string2 = LocaleController.getString(R.string.ActionPinnedVideo);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string2, "un1", tLRPC$User);
                    return;
                } else if (this.replyMessageObject.isGif()) {
                    String string3 = LocaleController.getString(R.string.ActionPinnedGif);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string3, "un1", tLRPC$User);
                    return;
                } else if (this.replyMessageObject.isVoice()) {
                    String string4 = LocaleController.getString(R.string.ActionPinnedVoice);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string4, "un1", tLRPC$User);
                    return;
                } else if (this.replyMessageObject.isRoundVideo()) {
                    String string5 = LocaleController.getString(R.string.ActionPinnedRound);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string5, "un1", tLRPC$User);
                    return;
                } else if ((this.replyMessageObject.isSticker() || this.replyMessageObject.isAnimatedSticker()) && !this.replyMessageObject.isAnimatedEmoji()) {
                    String string6 = LocaleController.getString(R.string.ActionPinnedSticker);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string6, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaDocument) {
                    String string7 = LocaleController.getString(R.string.ActionPinnedFile);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string7, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaGeo) {
                    String string8 = LocaleController.getString(R.string.ActionPinnedGeo);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string8, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaGeoLive) {
                    String string9 = LocaleController.getString(R.string.ActionPinnedGeoLive);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string9, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaContact) {
                    String string10 = LocaleController.getString(R.string.ActionPinnedContact);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string10, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaPoll) {
                    if (((TLRPC$TL_messageMediaPoll) getMedia(this.replyMessageObject)).poll.quiz) {
                        String string11 = LocaleController.getString(R.string.ActionPinnedQuiz);
                        if (tLRPC$User == null) {
                            tLRPC$User = tLRPC$Chat;
                        }
                        this.messageText = replaceWithLink(string11, "un1", tLRPC$User);
                        return;
                    }
                    String string12 = LocaleController.getString(R.string.ActionPinnedPoll);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string12, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaPhoto) {
                    String string13 = LocaleController.getString(R.string.ActionPinnedPhoto);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string13, "un1", tLRPC$User);
                    return;
                } else if (getMedia(this.replyMessageObject) instanceof TLRPC$TL_messageMediaPaidMedia) {
                    this.messageText = LocaleController.formatPluralString("NotificationPinnedPaidMedia", (int) ((TLRPC$TL_messageMediaPaidMedia) getMedia(this.replyMessageObject)).stars_amount, tLRPC$Chat != 0 ? tLRPC$Chat.title : UserObject.getUserName(tLRPC$User));
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
                        SpannableStringBuilder replaceEmoji = Emoji.replaceEmoji(cloneSpans, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), true);
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
                        SpannableStringBuilder formatSpannable = AndroidUtilities.formatSpannable(LocaleController.getString(R.string.ActionPinnedText), replaceEmoji);
                        if (tLRPC$User == null) {
                            tLRPC$User = tLRPC$Chat;
                        }
                        this.messageText = replaceWithLink(formatSpannable, "un1", tLRPC$User);
                        return;
                    }
                    String string14 = LocaleController.getString(R.string.ActionPinnedNoText);
                    if (tLRPC$User == null) {
                        tLRPC$User = tLRPC$Chat;
                    }
                    this.messageText = replaceWithLink(string14, "un1", tLRPC$User);
                    return;
                }
            }
        }
        String string15 = LocaleController.getString(R.string.ActionPinnedNoText);
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
        FileLog.d("msg#" + tLRPC$Message.id + " updateReactions out=" + tLRPC$Message.out);
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
        AnimatedEmojiDrawable.getDocumentFetcher(this.currentAccount).fetchDocument(this.emojiAnimatedStickerId.longValue(), new AnimatedEmojiDrawable.ReceivedDocument() { // from class: org.telegram.messenger.MessageObject$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.ReceivedDocument
            public final void run(TLRPC$Document tLRPC$Document) {
                MessageObject.this.lambda$loadAnimatedEmojiDocument$1(tLRPC$Document);
            }
        });
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
                if (str2 != null && (isVideo() || isNewGif() || isRoundVideo() || isVideoSticker() || isPaidVideo(getMedia(this)))) {
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

    public static boolean isPaidVideo(TLRPC$MessageMedia tLRPC$MessageMedia) {
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) && tLRPC$MessageMedia.extended_media.size() == 1 && isExtendedVideo(tLRPC$MessageMedia.extended_media.get(0));
    }

    public static boolean isExtendedVideo(TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia) {
        if (!(tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia)) {
            return (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) && (((TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia).flags & 4) != 0;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media;
        return (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && isVideoDocument(tLRPC$MessageMedia.document);
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
                    replaceEmoji = LocaleController.getString(R.string.PaymentReceipt);
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

    /* JADX WARN: Code restructure failed: missing block: B:909:0x176c, code lost:
        if ((((org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) r4).flags & 4) != 0) goto L909;
     */
    /* JADX WARN: Removed duplicated region for block: B:1024:0x19e5  */
    /* JADX WARN: Removed duplicated region for block: B:1038:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x071b  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x071e  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0722  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x0726  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x0758  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0763  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x0772  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x0799  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x07fb  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x099c  */
    /* JADX WARN: Removed duplicated region for block: B:380:0x09b6  */
    /* JADX WARN: Removed duplicated region for block: B:399:0x0a14  */
    /* JADX WARN: Removed duplicated region for block: B:400:0x0a28  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x0a6e  */
    /* JADX WARN: Removed duplicated region for block: B:417:0x0a78  */
    /* JADX WARN: Removed duplicated region for block: B:502:0x0c6b  */
    /* JADX WARN: Removed duplicated region for block: B:523:0x0d0f  */
    /* JADX WARN: Removed duplicated region for block: B:843:0x15ae  */
    /* JADX WARN: Removed duplicated region for block: B:859:0x160b  */
    /* JADX WARN: Removed duplicated region for block: B:860:0x160e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateMessageText(AbstractMap<Long, TLRPC$User> abstractMap, AbstractMap<Long, TLRPC$Chat> abstractMap2, LongSparseArray<TLRPC$User> longSparseArray, LongSparseArray<TLRPC$Chat> longSparseArray2) {
        TLRPC$User tLRPC$User;
        TLRPC$Chat chat;
        TLRPC$Message tLRPC$Message;
        String str;
        char c;
        String formatPluralString;
        String publicUsername;
        boolean isChannelAndNotMegaGroup;
        int i;
        SpannableStringBuilder spannableStringBuilder;
        String string;
        String formatString;
        String formatString2;
        String publicUsername2;
        String str2;
        String str3;
        TLRPC$User tLRPC$User2;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat tLRPC$Chat2;
        String sb;
        TLRPC$Chat tLRPC$Chat3;
        long j;
        String str4;
        TLObject tLObject;
        TLRPC$Chat tLRPC$Chat4;
        TLRPC$Chat tLRPC$Chat5;
        TLRPC$Chat tLRPC$Chat6;
        ArrayList<TLRPC$VideoSize> arrayList;
        int i2;
        SpannableStringBuilder spannableStringBuilder2;
        TLRPC$Chat tLRPC$Chat7;
        TLRPC$TL_messageActionGiveawayResults tLRPC$TL_messageActionGiveawayResults;
        TLRPC$Chat tLRPC$Chat8;
        String str5;
        boolean z;
        TLRPC$Chat tLRPC$Chat9;
        String str6;
        TLRPC$TL_messageActionPaymentRefunded tLRPC$TL_messageActionPaymentRefunded;
        TLObject chat2;
        TLObject chat3;
        TLObject chat4;
        String formatPluralString2;
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
            if (!(tLRPC$Message instanceof TLRPC$TL_messageService)) {
                TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                if (tLRPC$MessageAction != null) {
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetSameChatWallPaper) {
                        this.contentType = 1;
                        this.type = 10;
                        TLRPC$TL_messageActionSetSameChatWallPaper tLRPC$TL_messageActionSetSameChatWallPaper = (TLRPC$TL_messageActionSetSameChatWallPaper) tLRPC$MessageAction;
                        TLRPC$User user = getUser(abstractMap, longSparseArray, isOutOwner() ? 0L : getDialogId());
                        ArrayList<TLRPC$PhotoSize> arrayList2 = new ArrayList<>();
                        this.photoThumbs = arrayList2;
                        TLRPC$Document tLRPC$Document = tLRPC$TL_messageActionSetSameChatWallPaper.wallpaper.document;
                        if (tLRPC$Document != null) {
                            arrayList2.addAll(tLRPC$Document.thumbs);
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
                    } else {
                        String str7 = ", ";
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                            this.contentType = 1;
                            TLRPC$TL_messageActionSetChatWallPaper tLRPC$TL_messageActionSetChatWallPaper = (TLRPC$TL_messageActionSetChatWallPaper) tLRPC$MessageAction;
                            this.type = 22;
                            ArrayList<TLRPC$PhotoSize> arrayList3 = new ArrayList<>();
                            this.photoThumbs = arrayList3;
                            TLRPC$Document tLRPC$Document2 = tLRPC$TL_messageActionSetChatWallPaper.wallpaper.document;
                            if (tLRPC$Document2 != null) {
                                arrayList3.addAll(tLRPC$Document2.thumbs);
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
                                        spannableString.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString.length(), 33);
                                        this.messageText = AndroidUtilities.replaceCharSequence("%s", this.messageText, spannableString);
                                    } else {
                                        this.messageText = LocaleController.getString(R.string.ActionSetWallpaperForThisChatSelf);
                                    }
                                } else {
                                    SpannableString spannableString2 = new SpannableString(UserObject.getFirstName(user2));
                                    spannableString2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, spannableString2.length(), 33);
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
                            int i3 = tLRPC$MessageAction.duration;
                            if (i3 != 0) {
                                int i4 = i3 / 86400;
                                if (i4 > 0) {
                                    formatPluralString2 = LocaleController.formatPluralString("Days", i4, new Object[0]);
                                } else {
                                    int i5 = i3 / 3600;
                                    if (i5 > 0) {
                                        formatPluralString2 = LocaleController.formatPluralString("Hours", i5, new Object[0]);
                                    } else {
                                        int i6 = i3 / 60;
                                        if (i6 > 0) {
                                            formatPluralString2 = LocaleController.formatPluralString("Minutes", i6, new Object[0]);
                                        } else {
                                            formatPluralString2 = LocaleController.formatPluralString("Seconds", i3, new Object[0]);
                                        }
                                    }
                                }
                                if (!(this.messageOwner.peer_id instanceof TLRPC$TL_peerChat) && !isSupergroup()) {
                                    this.messageText = LocaleController.formatString("ActionChannelCallEnded", R.string.ActionChannelCallEnded, formatPluralString2);
                                } else if (isOut()) {
                                    this.messageText = LocaleController.formatString("ActionGroupCallEndedByYou", R.string.ActionGroupCallEndedByYou, formatPluralString2);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.formatString("ActionGroupCallEndedBy", R.string.ActionGroupCallEndedBy, formatPluralString2), "un1", tLObject2);
                                }
                            } else if ((tLRPC$Message.peer_id instanceof TLRPC$TL_peerChat) || isSupergroup()) {
                                if (isOut()) {
                                    this.messageText = LocaleController.getString(R.string.ActionGroupCallStartedByYou);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionGroupCallStarted), "un1", tLObject2);
                                }
                            } else {
                                this.messageText = LocaleController.getString(R.string.ActionChannelCallJustStarted);
                            }
                        } else {
                            TLRPC$Chat tLRPC$Chat10 = chat;
                            String str8 = "un2";
                            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionInviteToGroupCall) {
                                long j2 = tLRPC$MessageAction.user_id;
                                if (j2 == 0 && tLRPC$MessageAction.users.size() == 1) {
                                    j2 = this.messageOwner.action.users.get(0).longValue();
                                }
                                if (j2 != 0) {
                                    TLRPC$User user4 = getUser(abstractMap, longSparseArray, j2);
                                    if (isOut()) {
                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionGroupCallYouInvited), "un2", user4);
                                    } else if (j2 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionGroupCallInvitedYou), "un1", tLObject2);
                                    } else {
                                        CharSequence replaceWithLink = replaceWithLink(LocaleController.getString(R.string.ActionGroupCallInvited), "un2", user4);
                                        this.messageText = replaceWithLink;
                                        this.messageText = replaceWithLink(replaceWithLink, "un1", tLObject2);
                                    }
                                } else {
                                    if (isOut()) {
                                        str6 = "";
                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionGroupCallYouInvited), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                    } else {
                                        str6 = "";
                                        CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString(R.string.ActionGroupCallInvited), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                        this.messageText = replaceWithLink2;
                                        this.messageText = replaceWithLink(replaceWithLink2, "un1", tLObject2);
                                    }
                                    str = str6;
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                                TLRPC$TL_messageActionGeoProximityReached tLRPC$TL_messageActionGeoProximityReached = (TLRPC$TL_messageActionGeoProximityReached) tLRPC$MessageAction;
                                long peerId = getPeerId(tLRPC$TL_messageActionGeoProximityReached.from_id);
                                if (peerId > 0) {
                                    chat3 = getUser(abstractMap, longSparseArray, peerId);
                                } else {
                                    chat3 = getChat(abstractMap2, longSparseArray2, -peerId);
                                }
                                long peerId2 = getPeerId(tLRPC$TL_messageActionGeoProximityReached.to_id);
                                long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                if (peerId2 == clientUserId) {
                                    this.messageText = replaceWithLink(LocaleController.formatString("ActionUserWithinRadius", R.string.ActionUserWithinRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un1", chat3);
                                } else {
                                    if (peerId2 > 0) {
                                        chat4 = getUser(abstractMap, longSparseArray, peerId2);
                                    } else {
                                        chat4 = getChat(abstractMap2, longSparseArray2, -peerId2);
                                    }
                                    if (peerId == clientUserId) {
                                        this.messageText = replaceWithLink(LocaleController.formatString("ActionUserWithinYouRadius", R.string.ActionUserWithinYouRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un1", chat4);
                                    } else {
                                        CharSequence replaceWithLink3 = replaceWithLink(LocaleController.formatString("ActionUserWithinOtherRadius", R.string.ActionUserWithinOtherRadius, LocaleController.formatDistance(tLRPC$TL_messageActionGeoProximityReached.distance, 2)), "un2", chat4);
                                        this.messageText = replaceWithLink3;
                                        this.messageText = replaceWithLink(replaceWithLink3, "un1", chat3);
                                    }
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCustomAction) {
                                this.messageText = tLRPC$MessageAction.message;
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatCreate) {
                                if (isOut()) {
                                    this.messageText = LocaleController.getString(R.string.ActionYouCreateGroup);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionCreateGroup), "un1", tLObject2);
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatDeleteUser) {
                                if (isFromUser()) {
                                    TLRPC$Message tLRPC$Message2 = this.messageOwner;
                                    if (tLRPC$Message2.action.user_id == tLRPC$Message2.from_id.user_id) {
                                        if (isOut()) {
                                            this.messageText = LocaleController.getString(R.string.ActionYouLeftUser);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionLeftUser), "un1", tLObject2);
                                        }
                                    }
                                }
                                TLRPC$User user5 = getUser(abstractMap, longSparseArray, this.messageOwner.action.user_id);
                                if (isOut()) {
                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionYouKickUser), "un2", user5);
                                } else if (this.messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionKickUserYou), "un1", tLObject2);
                                } else {
                                    CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString(R.string.ActionKickUser), "un2", user5);
                                    this.messageText = replaceWithLink4;
                                    this.messageText = replaceWithLink(replaceWithLink4, "un1", tLObject2);
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentRefunded) {
                                long peerDialogId = DialogObject.getPeerDialogId(((TLRPC$TL_messageActionPaymentRefunded) tLRPC$MessageAction).peer);
                                if (peerDialogId >= 0) {
                                    chat2 = getUser(abstractMap, longSparseArray, peerDialogId);
                                } else {
                                    chat2 = getChat(abstractMap2, longSparseArray2, -peerDialogId);
                                }
                                this.messageText = StarsIntroActivity.replaceStars(replaceWithLink(LocaleController.formatString(R.string.ActionRefunded, tLRPC$TL_messageActionPaymentRefunded.currency + " " + LocaleController.formatNumber(tLRPC$TL_messageActionPaymentRefunded.total_amount, ',')), "un1", chat2));
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatAddUser) {
                                long j3 = tLRPC$MessageAction.user_id;
                                if (j3 == 0 && tLRPC$MessageAction.users.size() == 1) {
                                    j3 = this.messageOwner.action.users.get(0).longValue();
                                }
                                if (j3 != 0) {
                                    TLRPC$User user6 = getUser(abstractMap, longSparseArray, j3);
                                    long j4 = this.messageOwner.peer_id.channel_id;
                                    TLRPC$Chat chat5 = j4 != 0 ? getChat(abstractMap2, longSparseArray2, j4) : null;
                                    TLRPC$Peer tLRPC$Peer2 = this.messageOwner.from_id;
                                    if (tLRPC$Peer2 != null && j3 == tLRPC$Peer2.user_id) {
                                        if (ChatObject.isChannel(chat5) && !chat5.megagroup) {
                                            this.channelJoined = true;
                                            this.messageText = LocaleController.getString(R.string.ChannelJoined);
                                        } else if (this.messageOwner.peer_id.channel_id != 0) {
                                            if (j3 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                this.messageText = LocaleController.getString(R.string.ChannelMegaJoined);
                                            } else {
                                                this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionAddUserSelfMega), "un1", tLObject2);
                                            }
                                        } else if (isOut()) {
                                            this.messageText = LocaleController.getString(R.string.ActionAddUserSelfYou);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionAddUserSelf), "un1", tLObject2);
                                        }
                                    } else if (isOut()) {
                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionYouAddUser), "un2", user6);
                                    } else if (j3 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        if (this.messageOwner.peer_id.channel_id != 0) {
                                            if (chat5 != null && chat5.megagroup) {
                                                this.messageText = replaceWithLink(LocaleController.getString(R.string.MegaAddedBy), "un1", tLObject2);
                                            } else {
                                                this.messageText = replaceWithLink(LocaleController.getString(R.string.ChannelAddedBy), "un1", tLObject2);
                                            }
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionAddUserYou), "un1", tLObject2);
                                        }
                                    } else {
                                        CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString(R.string.ActionAddUser), "un2", user6);
                                        this.messageText = replaceWithLink5;
                                        this.messageText = replaceWithLink(replaceWithLink5, "un1", tLObject2);
                                    }
                                } else {
                                    if (isOut()) {
                                        str6 = "";
                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionYouAddUser), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                    } else {
                                        str6 = "";
                                        CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString(R.string.ActionAddUser), "un2", this.messageOwner.action.users, abstractMap, longSparseArray);
                                        this.messageText = replaceWithLink6;
                                        this.messageText = replaceWithLink(replaceWithLink6, "un1", tLObject2);
                                    }
                                    str = str6;
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatJoinedByLink) {
                                if (isOut()) {
                                    this.messageText = LocaleController.getString(R.string.ActionInviteYou);
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionInviteUser), "un1", tLObject2);
                                }
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiveawayLaunch) {
                                TLRPC$Peer tLRPC$Peer3 = tLRPC$Message.peer_id;
                                if (tLRPC$Peer3 != null) {
                                    long j5 = tLRPC$Peer3.channel_id;
                                    if (j5 != 0) {
                                        tLRPC$Chat9 = getChat(abstractMap2, longSparseArray2, j5);
                                        this.messageText = LocaleController.formatString(!ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat9) ? R.string.BoostingGiveawayJustStarted : R.string.BoostingGiveawayJustStartedGroup, tLRPC$Chat9 == null ? tLRPC$Chat9.title : "");
                                    }
                                }
                                tLRPC$Chat9 = null;
                                this.messageText = LocaleController.formatString(!ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat9) ? R.string.BoostingGiveawayJustStarted : R.string.BoostingGiveawayJustStartedGroup, tLRPC$Chat9 == null ? tLRPC$Chat9.title : "");
                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionBoostApply) {
                                TLRPC$Peer tLRPC$Peer4 = tLRPC$Message.peer_id;
                                if (tLRPC$Peer4 != null) {
                                    long j6 = tLRPC$Peer4.channel_id;
                                    if (j6 != 0) {
                                        tLRPC$Chat8 = getChat(abstractMap2, longSparseArray2, j6);
                                        boolean isChannelAndNotMegaGroup2 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat8);
                                        TLRPC$TL_messageActionBoostApply tLRPC$TL_messageActionBoostApply = (TLRPC$TL_messageActionBoostApply) this.messageOwner.action;
                                        if (!(tLObject2 instanceof TLRPC$User)) {
                                            TLRPC$User tLRPC$User3 = (TLRPC$User) tLObject2;
                                            z = UserObject.isUserSelf(tLRPC$User3);
                                            str5 = UserObject.getFirstName(tLRPC$User3);
                                        } else {
                                            str5 = tLObject2 instanceof TLRPC$Chat ? ((TLRPC$Chat) tLObject2).title : "";
                                            z = false;
                                        }
                                        if (!z) {
                                            int i7 = tLRPC$TL_messageActionBoostApply.boosts;
                                            if (i7 <= 1) {
                                                this.messageText = LocaleController.getString(isChannelAndNotMegaGroup2 ? R.string.BoostingBoostsChannelByYouServiceMsg : R.string.BoostingBoostsGroupByYouServiceMsg);
                                            } else {
                                                this.messageText = LocaleController.formatPluralString(isChannelAndNotMegaGroup2 ? "BoostingBoostsChannelByYouServiceMsgCount" : "BoostingBoostsGroupByYouServiceMsgCount", i7, new Object[0]);
                                            }
                                        } else {
                                            int i8 = tLRPC$TL_messageActionBoostApply.boosts;
                                            if (i8 <= 1) {
                                                this.messageText = LocaleController.formatString(isChannelAndNotMegaGroup2 ? R.string.BoostingBoostsChannelByUserServiceMsg : R.string.BoostingBoostsGroupByUserServiceMsg, str5);
                                            } else {
                                                this.messageText = LocaleController.formatPluralString(isChannelAndNotMegaGroup2 ? "BoostingBoostsChannelByUserServiceMsgCount" : "BoostingBoostsGroupByUserServiceMsgCount", i8, str5);
                                            }
                                        }
                                    }
                                }
                                tLRPC$Chat8 = null;
                                boolean isChannelAndNotMegaGroup22 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat8);
                                TLRPC$TL_messageActionBoostApply tLRPC$TL_messageActionBoostApply2 = (TLRPC$TL_messageActionBoostApply) this.messageOwner.action;
                                if (!(tLObject2 instanceof TLRPC$User)) {
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
                                        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder();
                                        spannableStringBuilder3.append((CharSequence) LocaleController.formatPluralString("BoostingGiveawayServiceWinnersSelected", tLRPC$TL_messageActionGiveawayResults.winners_count, new Object[0]));
                                        if (tLRPC$TL_messageActionGiveawayResults.unclaimed_count > 0) {
                                            spannableStringBuilder3.append((CharSequence) "\n");
                                            spannableStringBuilder3.append((CharSequence) LocaleController.formatPluralString(isChannelAndNotMegaGroup3 ? "BoostingGiveawayServiceUndistributed" : "BoostingGiveawayServiceUndistributedGroup", tLRPC$TL_messageActionGiveawayResults.unclaimed_count, new Object[0]));
                                        }
                                        this.messageText = spannableStringBuilder3;
                                    }
                                }
                                tLRPC$Chat7 = null;
                                boolean isChannelAndNotMegaGroup32 = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat7);
                                tLRPC$TL_messageActionGiveawayResults = (TLRPC$TL_messageActionGiveawayResults) this.messageOwner.action;
                                SpannableStringBuilder spannableStringBuilder32 = new SpannableStringBuilder();
                                spannableStringBuilder32.append((CharSequence) LocaleController.formatPluralString("BoostingGiveawayServiceWinnersSelected", tLRPC$TL_messageActionGiveawayResults.winners_count, new Object[0]));
                                if (tLRPC$TL_messageActionGiveawayResults.unclaimed_count > 0) {
                                }
                                this.messageText = spannableStringBuilder32;
                            } else {
                                TLRPC$User tLRPC$User4 = tLRPC$User;
                                if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftStars) {
                                    boolean z2 = tLObject2 instanceof TLRPC$User;
                                    if (z2 && ((TLRPC$User) tLObject2).self) {
                                        this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftOutbound)), "un1", getUser(abstractMap, longSparseArray, tLRPC$Message.peer_id.user_id));
                                    } else if (z2 && UserObject.isService(((TLRPC$User) tLObject2).id)) {
                                        this.messageText = TextUtils.replace(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), new String[]{"un1"}, new CharSequence[]{LocaleController.getString(R.string.StarsTransactionUnknown)});
                                    } else {
                                        this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), "un1", tLObject2);
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
                                            i2 = indexOf;
                                            spannableStringBuilder2 = valueOf;
                                            double pow = Math.pow(10.0d, -9.0d);
                                            Double.isNaN(d);
                                            sb2.append(String.format("%.2f", Double.valueOf(d * pow)));
                                            sb2.append(" ");
                                            sb2.append(this.messageOwner.action.cryptoCurrency);
                                            sb2.append(" (~ ");
                                            sb2.append((Object) formatCurrency);
                                            sb2.append(")");
                                            formatCurrency = sb2.toString();
                                        } else {
                                            i2 = indexOf;
                                            spannableStringBuilder2 = valueOf;
                                        }
                                        this.messageText = spannableStringBuilder2.replace(i2, i2 + 3, (CharSequence) formatCurrency);
                                    }
                                } else {
                                    boolean z3 = tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftCode;
                                    if (z3 && ((TLRPC$TL_messageActionGiftCode) tLRPC$MessageAction).boost_peer != null) {
                                        this.messageText = LocaleController.getString(R.string.BoostingReceivedGiftNoName);
                                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftPremium) || z3) {
                                        str = "";
                                        if ((tLObject2 instanceof TLRPC$User) && ((TLRPC$User) tLObject2).self) {
                                            this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftOutbound)), "un1", getUser(abstractMap, longSparseArray, tLRPC$Message.peer_id.user_id));
                                        } else {
                                            this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ActionGiftInbound)), "un1", tLObject2);
                                        }
                                        int indexOf2 = this.messageText.toString().indexOf("un2");
                                        if (indexOf2 != -1) {
                                            SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(this.messageText);
                                            BillingController billingController2 = BillingController.getInstance();
                                            TLRPC$MessageAction tLRPC$MessageAction3 = this.messageOwner.action;
                                            String formatCurrency2 = billingController2.formatCurrency(tLRPC$MessageAction3.amount, tLRPC$MessageAction3.currency);
                                            if ((this.messageOwner.action.flags & 1) != 0) {
                                                StringBuilder sb3 = new StringBuilder();
                                                double d2 = this.messageOwner.action.cryptoAmount;
                                                i = indexOf2;
                                                spannableStringBuilder = valueOf2;
                                                double pow2 = Math.pow(10.0d, -9.0d);
                                                Double.isNaN(d2);
                                                sb3.append(String.format("%.2f", Double.valueOf(d2 * pow2)));
                                                sb3.append(" ");
                                                sb3.append(this.messageOwner.action.cryptoCurrency);
                                                sb3.append(" (~ ");
                                                sb3.append((Object) formatCurrency2);
                                                sb3.append(")");
                                                formatCurrency2 = sb3.toString();
                                            } else {
                                                i = indexOf2;
                                                spannableStringBuilder = valueOf2;
                                            }
                                            this.messageText = spannableStringBuilder.replace(i, i + 3, (CharSequence) formatCurrency2);
                                        }
                                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSuggestProfilePhoto) {
                                        TLRPC$Photo tLRPC$Photo = tLRPC$MessageAction.photo;
                                        if (tLRPC$Photo != null && (arrayList = tLRPC$Photo.video_sizes) != null && !arrayList.isEmpty()) {
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
                                                        this.messageText = LocaleController.getString(R.string.ActionChannelChangedVideo);
                                                    } else {
                                                        this.messageText = LocaleController.getString(R.string.ActionChannelChangedPhoto);
                                                    }
                                                } else if (!isOut()) {
                                                    if (isVideoAvatar()) {
                                                        this.messageText = LocaleController.getString(R.string.ActionYouChangedVideo);
                                                    } else {
                                                        this.messageText = LocaleController.getString(R.string.ActionYouChangedPhoto);
                                                    }
                                                } else if (isVideoAvatar()) {
                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionChangedVideo), "un1", tLObject2);
                                                } else {
                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionChangedPhoto), "un1", tLObject2);
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
                                                    this.messageText = LocaleController.getString(R.string.ActionChannelChangedTitle).replace("un2", this.messageOwner.action.title);
                                                } else if (!isOut()) {
                                                    this.messageText = LocaleController.getString(R.string.ActionYouChangedTitle).replace("un2", this.messageOwner.action.title);
                                                } else {
                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionChangedTitle).replace("un2", this.messageOwner.action.title), "un1", tLObject2);
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
                                                    this.messageText = LocaleController.getString(R.string.ActionChannelRemovedPhoto);
                                                } else if (!isOut()) {
                                                    this.messageText = LocaleController.getString(R.string.ActionYouRemovedPhoto);
                                                } else {
                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionRemovedPhoto), "un1", tLObject2);
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
                                                this.messageText = LocaleController.formatString("MessageLifetimeChanged", R.string.MessageLifetimeChanged, UserObject.getFirstName(tLRPC$User4), LocaleController.formatTTLString(this.messageOwner.action.ttl));
                                            }
                                        } else if (isOut()) {
                                            this.messageText = LocaleController.getString(R.string.MessageLifetimeYouRemoved);
                                        } else {
                                            this.messageText = LocaleController.formatString("MessageLifetimeRemoved", R.string.MessageLifetimeRemoved, UserObject.getFirstName(tLRPC$User4));
                                        }
                                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionRequestedPeer) {
                                        ArrayList arrayList4 = new ArrayList();
                                        int i9 = 0;
                                        int i10 = 0;
                                        int i11 = 0;
                                        for (TLRPC$Peer tLRPC$Peer9 : ((TLRPC$TL_messageActionRequestedPeer) this.messageOwner.action).peers) {
                                            boolean z4 = tLRPC$Peer9 instanceof TLRPC$TL_peerUser;
                                            if (z4) {
                                                str4 = str8;
                                                tLObject = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$Peer9.user_id));
                                                if (tLObject == null) {
                                                    tLObject = getUser(abstractMap, longSparseArray, tLRPC$Peer9.user_id);
                                                }
                                            } else {
                                                str4 = str8;
                                                if (tLRPC$Peer9 instanceof TLRPC$TL_peerChat) {
                                                    tLObject = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$Peer9.chat_id));
                                                    if (tLObject == null) {
                                                        tLObject = getChat(abstractMap2, longSparseArray2, tLRPC$Peer9.chat_id);
                                                    }
                                                } else if (tLRPC$Peer9 instanceof TLRPC$TL_peerChannel) {
                                                    tLObject = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$Peer9.channel_id));
                                                    if (tLObject == null) {
                                                        tLObject = getChat(abstractMap2, longSparseArray2, tLRPC$Peer9.channel_id);
                                                    }
                                                } else {
                                                    tLObject = null;
                                                }
                                            }
                                            if (z4) {
                                                i9++;
                                            } else if (tLRPC$Peer9 instanceof TLRPC$TL_peerChat) {
                                                i11++;
                                            } else {
                                                i10++;
                                            }
                                            if (tLObject != null) {
                                                arrayList4.add(tLObject);
                                            }
                                            str8 = str4;
                                        }
                                        String str9 = str8;
                                        if (i9 > 0 && i9 != arrayList4.size()) {
                                            this.messageText = LocaleController.getPluralString("ActionRequestedPeerUserPlural", i9);
                                        } else if (i10 > 0 && i10 != arrayList4.size()) {
                                            this.messageText = LocaleController.getPluralString("ActionRequestedPeerChannelPlural", i10);
                                        } else if (i11 > 0 && i11 != arrayList4.size()) {
                                            this.messageText = LocaleController.getPluralString("ActionRequestedPeerChatPlural", i11);
                                        } else {
                                            SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder();
                                            int i12 = 0;
                                            while (i12 < arrayList4.size()) {
                                                spannableStringBuilder4.append(replaceWithLink("un1", "un1", (TLObject) arrayList4.get(i12)));
                                                String str10 = str7;
                                                if (i12 < arrayList4.size() - 1) {
                                                    spannableStringBuilder4.append((CharSequence) str10);
                                                }
                                                i12++;
                                                str7 = str10;
                                            }
                                            this.messageText = AndroidUtilities.replaceCharSequence("un1", LocaleController.getString(R.string.ActionRequestedPeer), spannableStringBuilder4);
                                        }
                                        TLRPC$User user7 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
                                        if (user7 == null) {
                                            user7 = getUser(abstractMap, longSparseArray, getDialogId());
                                        }
                                        this.messageText = replaceWithLink(this.messageText, str9, user7);
                                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) {
                                        TLRPC$TL_messageActionSetMessagesTTL tLRPC$TL_messageActionSetMessagesTTL = (TLRPC$TL_messageActionSetMessagesTTL) tLRPC$MessageAction;
                                        TLRPC$Peer tLRPC$Peer10 = tLRPC$Message.peer_id;
                                        if (tLRPC$Peer10 != null) {
                                            long j11 = tLRPC$Peer10.channel_id;
                                            if (j11 != 0) {
                                                tLRPC$Chat3 = getChat(abstractMap2, longSparseArray2, j11);
                                                if (tLRPC$Chat3 == null && !tLRPC$Chat3.megagroup) {
                                                    int i13 = tLRPC$TL_messageActionSetMessagesTTL.period;
                                                    if (i13 != 0) {
                                                        this.messageText = LocaleController.formatString("ActionTTLChannelChanged", R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(i13));
                                                    } else {
                                                        this.messageText = LocaleController.getString(R.string.ActionTTLChannelDisabled);
                                                    }
                                                } else {
                                                    j = tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from;
                                                    if (j == 0) {
                                                        this.drawServiceWithDefaultTypeface = true;
                                                        if (j == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                            this.messageText = AndroidUtilities.replaceTags(LocaleController.formatString("AutoDeleteGlobalActionFromYou", R.string.AutoDeleteGlobalActionFromYou, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period)));
                                                        } else {
                                                            TLObject tLObject3 = longSparseArray != null ? longSparseArray.get(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from) : null;
                                                            if (tLObject3 == null && abstractMap != null) {
                                                                tLObject3 = abstractMap.get(Long.valueOf(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                            }
                                                            if (tLObject3 == null && abstractMap2 != null) {
                                                                tLObject3 = abstractMap2.get(Long.valueOf(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                            }
                                                            if (tLObject3 == null) {
                                                                if (tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from > 0) {
                                                                    tLObject3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                                } else {
                                                                    tLObject3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from));
                                                                }
                                                            }
                                                            if (tLObject3 != null) {
                                                                tLObject2 = tLObject3;
                                                            }
                                                            this.messageText = replaceWithLink(AndroidUtilities.replaceTags(LocaleController.formatString("AutoDeleteGlobalAction", R.string.AutoDeleteGlobalAction, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period))), "un1", tLObject2);
                                                        }
                                                    } else if (tLRPC$TL_messageActionSetMessagesTTL.period != 0) {
                                                        if (isOut()) {
                                                            this.messageText = LocaleController.formatString("ActionTTLYouChanged", R.string.ActionTTLYouChanged, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period));
                                                        } else {
                                                            this.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", R.string.ActionTTLChanged, LocaleController.formatTTLString(tLRPC$TL_messageActionSetMessagesTTL.period)), "un1", tLObject2);
                                                        }
                                                    } else if (isOut()) {
                                                        this.messageText = LocaleController.getString(R.string.ActionTTLYouDisabled);
                                                    } else {
                                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionTTLDisabled), "un1", tLObject2);
                                                    }
                                                }
                                            }
                                        }
                                        tLRPC$Chat3 = null;
                                        if (tLRPC$Chat3 == null) {
                                        }
                                        j = tLRPC$TL_messageActionSetMessagesTTL.auto_setting_from;
                                        if (j == 0) {
                                        }
                                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                                        long j12 = tLRPC$Message.date * 1000;
                                        if (LocaleController.getInstance().getFormatterDay() != null && LocaleController.getInstance().getFormatterYear() != null) {
                                            sb = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(j12), LocaleController.getInstance().getFormatterDay().format(j12));
                                            str = "";
                                        } else {
                                            StringBuilder sb4 = new StringBuilder();
                                            str = "";
                                            sb4.append(str);
                                            sb4.append(this.messageOwner.date);
                                            sb = sb4.toString();
                                        }
                                        TLRPC$User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                                        if (currentUser == null) {
                                            currentUser = getUser(abstractMap, longSparseArray, this.messageOwner.peer_id.user_id);
                                        }
                                        String firstName = currentUser != null ? UserObject.getFirstName(currentUser) : str;
                                        int i14 = R.string.NotificationUnrecognizedDevice;
                                        TLRPC$MessageAction tLRPC$MessageAction4 = this.messageOwner.action;
                                        this.messageText = LocaleController.formatString("NotificationUnrecognizedDevice", i14, firstName, sb, tLRPC$MessageAction4.title, tLRPC$MessageAction4.address);
                                    } else {
                                        str = "";
                                        if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                                            this.messageText = LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, UserObject.getUserName(tLRPC$User4));
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                                            this.messageText = LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, UserObject.getUserName(tLRPC$User4));
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageEncryptedAction) {
                                            TLRPC$DecryptedMessageAction tLRPC$DecryptedMessageAction = tLRPC$MessageAction.encryptedAction;
                                            if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionScreenshotMessages) {
                                                if (isOut()) {
                                                    this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", R.string.ActionTakeScreenshootYou, new Object[0]);
                                                } else {
                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionTakeScreenshoot), "un1", tLObject2);
                                                }
                                            } else if (tLRPC$DecryptedMessageAction instanceof TLRPC$TL_decryptedMessageActionSetMessageTTL) {
                                                TLRPC$TL_decryptedMessageActionSetMessageTTL tLRPC$TL_decryptedMessageActionSetMessageTTL = (TLRPC$TL_decryptedMessageActionSetMessageTTL) tLRPC$DecryptedMessageAction;
                                                if (tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds != 0) {
                                                    if (isOut()) {
                                                        this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                                    } else {
                                                        this.messageText = LocaleController.formatString("MessageLifetimeChanged", R.string.MessageLifetimeChanged, UserObject.getFirstName(tLRPC$User4), LocaleController.formatTTLString(tLRPC$TL_decryptedMessageActionSetMessageTTL.ttl_seconds));
                                                    }
                                                } else if (isOut()) {
                                                    this.messageText = LocaleController.getString(R.string.MessageLifetimeYouRemoved);
                                                } else {
                                                    this.messageText = LocaleController.formatString("MessageLifetimeRemoved", R.string.MessageLifetimeRemoved, UserObject.getFirstName(tLRPC$User4));
                                                }
                                            }
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionScreenshotTaken) {
                                            if (isOut()) {
                                                this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", R.string.ActionTakeScreenshootYou, new Object[0]);
                                            } else {
                                                this.messageText = replaceWithLink(LocaleController.getString(R.string.ActionTakeScreenshoot), "un1", tLObject2);
                                            }
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionCreatedBroadcastList) {
                                            this.messageText = LocaleController.formatString("YouCreatedBroadcastList", R.string.YouCreatedBroadcastList, new Object[0]);
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChannelCreate) {
                                            TLRPC$Peer tLRPC$Peer11 = tLRPC$Message.peer_id;
                                            if (tLRPC$Peer11 != null) {
                                                long j13 = tLRPC$Peer11.channel_id;
                                                if (j13 != 0) {
                                                    tLRPC$Chat2 = getChat(abstractMap2, longSparseArray2, j13);
                                                    if (!ChatObject.isChannel(tLRPC$Chat2) && tLRPC$Chat2.megagroup) {
                                                        this.messageText = LocaleController.getString(R.string.ActionCreateMega);
                                                    } else {
                                                        this.messageText = LocaleController.getString(R.string.ActionCreateChannel);
                                                    }
                                                }
                                            }
                                            tLRPC$Chat2 = null;
                                            if (!ChatObject.isChannel(tLRPC$Chat2)) {
                                            }
                                            this.messageText = LocaleController.getString(R.string.ActionCreateChannel);
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatMigrateTo) {
                                            this.messageText = LocaleController.getString(R.string.ActionMigrateFromGroup);
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChannelMigrateFrom) {
                                            this.messageText = LocaleController.getString(R.string.ActionMigrateFromGroup);
                                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPinMessage) {
                                            if (tLRPC$User4 == null) {
                                                tLRPC$Chat = getChat(abstractMap2, longSparseArray2, tLRPC$Message.peer_id.channel_id);
                                                tLRPC$User2 = tLRPC$User4;
                                            } else {
                                                tLRPC$User2 = tLRPC$User4;
                                                tLRPC$Chat = null;
                                            }
                                            generatePinMessageText(tLRPC$User2, tLRPC$Chat);
                                        } else {
                                            TLRPC$User tLRPC$User5 = tLRPC$User4;
                                            if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionHistoryClear) {
                                                this.messageText = LocaleController.getString(R.string.HistoryCleared);
                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicCreate) {
                                                this.messageText = LocaleController.getString(R.string.TopicCreated);
                                                TLRPC$TL_messageActionTopicCreate tLRPC$TL_messageActionTopicCreate = (TLRPC$TL_messageActionTopicCreate) this.messageOwner.action;
                                                TLRPC$TL_forumTopic tLRPC$TL_forumTopic = new TLRPC$TL_forumTopic();
                                                tLRPC$TL_forumTopic.icon_emoji_id = tLRPC$TL_messageActionTopicCreate.icon_emoji_id;
                                                tLRPC$TL_forumTopic.title = tLRPC$TL_messageActionTopicCreate.title;
                                                tLRPC$TL_forumTopic.icon_color = tLRPC$TL_messageActionTopicCreate.icon_color;
                                                this.messageTextShort = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString(R.string.TopicWasCreatedAction), ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic, null, false));
                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionTopicEdit) {
                                                TLRPC$TL_messageActionTopicEdit tLRPC$TL_messageActionTopicEdit = (TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction;
                                                if (tLRPC$User5 != null) {
                                                    str2 = ContactsController.formatName(tLRPC$User5.first_name, tLRPC$User5.last_name);
                                                } else if (tLRPC$Chat10 != null) {
                                                    tLRPC$User5 = tLRPC$Chat10;
                                                    str2 = tLRPC$Chat10.title;
                                                } else {
                                                    str2 = null;
                                                    tLRPC$User5 = null;
                                                }
                                                if (str2 != null) {
                                                    str3 = str2.trim();
                                                } else {
                                                    str3 = "DELETED";
                                                }
                                                TLRPC$MessageAction tLRPC$MessageAction5 = this.messageOwner.action;
                                                int i15 = tLRPC$MessageAction5.flags;
                                                if ((i15 & 8) > 0) {
                                                    if (((TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction5).hidden) {
                                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicHidden2), "%s", tLRPC$User5);
                                                        this.messageTextShort = LocaleController.getString(R.string.TopicHidden);
                                                    } else {
                                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicShown2), "%s", tLRPC$User5);
                                                        this.messageTextShort = LocaleController.getString(R.string.TopicShown);
                                                    }
                                                } else if ((4 & i15) <= 0) {
                                                    int i16 = 2 & i15;
                                                    if (i16 != 0 && (i15 & 1) != 0) {
                                                        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = new TLRPC$TL_forumTopic();
                                                        tLRPC$TL_forumTopic2.icon_emoji_id = tLRPC$TL_messageActionTopicEdit.icon_emoji_id;
                                                        tLRPC$TL_forumTopic2.title = tLRPC$TL_messageActionTopicEdit.title;
                                                        tLRPC$TL_forumTopic2.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                                        CharSequence topicSpannedName = ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic2, null, this.topicIconDrawable, false);
                                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString(R.string.TopicChangeIconAndTitleTo), str3), topicSpannedName);
                                                        this.messageTextShort = LocaleController.getString(R.string.TopicRenamed);
                                                        this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString(R.string.TopicChangeIconAndTitleToInReply), topicSpannedName);
                                                    } else if (i16 != 0) {
                                                        TLRPC$TL_forumTopic tLRPC$TL_forumTopic3 = new TLRPC$TL_forumTopic();
                                                        tLRPC$TL_forumTopic3.icon_emoji_id = tLRPC$TL_messageActionTopicEdit.icon_emoji_id;
                                                        tLRPC$TL_forumTopic3.title = str;
                                                        tLRPC$TL_forumTopic3.icon_color = ForumBubbleDrawable.serverSupportedColor[0];
                                                        CharSequence topicSpannedName2 = ForumUtilities.getTopicSpannedName(tLRPC$TL_forumTopic3, null, this.topicIconDrawable, false);
                                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString(R.string.TopicIconChangedTo), str3), topicSpannedName2);
                                                        this.messageTextShort = LocaleController.getString(R.string.TopicIconChanged);
                                                        this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString(R.string.TopicIconChangedToInReply), topicSpannedName2);
                                                    } else if ((1 & i15) != 0) {
                                                        this.messageText = AndroidUtilities.replaceCharSequence("%2$s", AndroidUtilities.replaceCharSequence("%1$s", LocaleController.getString(R.string.TopicRenamedTo), str3), tLRPC$TL_messageActionTopicEdit.title);
                                                        this.messageTextShort = LocaleController.getString(R.string.TopicRenamed);
                                                        this.messageTextForReply = AndroidUtilities.replaceCharSequence("%s", LocaleController.getString(R.string.TopicRenamedToInReply), tLRPC$TL_messageActionTopicEdit.title);
                                                    }
                                                } else if (((TLRPC$TL_messageActionTopicEdit) tLRPC$MessageAction5).closed) {
                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicClosed2), "%s", tLRPC$User5);
                                                    this.messageTextShort = LocaleController.getString(R.string.TopicClosed);
                                                } else {
                                                    this.messageText = replaceWithLink(LocaleController.getString(R.string.TopicRestarted2), "%s", tLRPC$User5);
                                                    this.messageTextShort = LocaleController.getString(R.string.TopicRestarted);
                                                }
                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) {
                                                generateGameMessageText(tLRPC$User5);
                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                                                TLRPC$TL_messageActionPhoneCall tLRPC$TL_messageActionPhoneCall = (TLRPC$TL_messageActionPhoneCall) tLRPC$MessageAction;
                                                boolean z5 = tLRPC$TL_messageActionPhoneCall.reason instanceof TLRPC$TL_phoneCallDiscardReasonMissed;
                                                if (isFromUser() && this.messageOwner.from_id.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                    if (z5) {
                                                        if (tLRPC$TL_messageActionPhoneCall.video) {
                                                            this.messageText = LocaleController.getString(R.string.CallMessageVideoOutgoingMissed);
                                                        } else {
                                                            this.messageText = LocaleController.getString(R.string.CallMessageOutgoingMissed);
                                                        }
                                                    } else if (tLRPC$TL_messageActionPhoneCall.video) {
                                                        this.messageText = LocaleController.getString(R.string.CallMessageVideoOutgoing);
                                                    } else {
                                                        this.messageText = LocaleController.getString(R.string.CallMessageOutgoing);
                                                    }
                                                } else if (z5) {
                                                    if (tLRPC$TL_messageActionPhoneCall.video) {
                                                        this.messageText = LocaleController.getString(R.string.CallMessageVideoIncomingMissed);
                                                    } else {
                                                        this.messageText = LocaleController.getString(R.string.CallMessageIncomingMissed);
                                                    }
                                                } else if (tLRPC$TL_messageActionPhoneCall.reason instanceof TLRPC$TL_phoneCallDiscardReasonBusy) {
                                                    if (tLRPC$TL_messageActionPhoneCall.video) {
                                                        this.messageText = LocaleController.getString(R.string.CallMessageVideoIncomingDeclined);
                                                    } else {
                                                        this.messageText = LocaleController.getString(R.string.CallMessageIncomingDeclined);
                                                    }
                                                } else if (tLRPC$TL_messageActionPhoneCall.video) {
                                                    this.messageText = LocaleController.getString(R.string.CallMessageVideoIncoming);
                                                } else {
                                                    this.messageText = LocaleController.getString(R.string.CallMessageIncoming);
                                                }
                                                int i17 = tLRPC$TL_messageActionPhoneCall.duration;
                                                if (i17 > 0) {
                                                    String formatCallDuration = LocaleController.formatCallDuration(i17);
                                                    String formatString3 = LocaleController.formatString("CallMessageWithDuration", R.string.CallMessageWithDuration, this.messageText, formatCallDuration);
                                                    this.messageText = formatString3;
                                                    String charSequence = formatString3.toString();
                                                    int indexOf3 = charSequence.indexOf(formatCallDuration);
                                                    if (indexOf3 != -1) {
                                                        SpannableString spannableString3 = new SpannableString(this.messageText);
                                                        int length = formatCallDuration.length() + indexOf3;
                                                        if (indexOf3 > 0 && charSequence.charAt(indexOf3 - 1) == '(') {
                                                            indexOf3--;
                                                        }
                                                        if (length < charSequence.length() && charSequence.charAt(length) == ')') {
                                                            length++;
                                                        }
                                                        spannableString3.setSpan(new TypefaceSpan(Typeface.DEFAULT), indexOf3, length, 0);
                                                        this.messageText = spannableString3;
                                                    }
                                                }
                                            } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent) {
                                                generatePaymentSentMessageText(getUser(abstractMap, longSparseArray, getDialogId()));
                                            } else {
                                                boolean z6 = tLRPC$MessageAction instanceof TLRPC$TL_messageActionBotAllowed;
                                                if (z6) {
                                                    TLRPC$TL_messageActionBotAllowed tLRPC$TL_messageActionBotAllowed = (TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction;
                                                    String str11 = tLRPC$TL_messageActionBotAllowed.domain;
                                                    TLRPC$BotApp tLRPC$BotApp = tLRPC$TL_messageActionBotAllowed.app;
                                                    if (tLRPC$TL_messageActionBotAllowed.from_request) {
                                                        this.messageText = LocaleController.getString(R.string.ActionBotAllowedWebapp);
                                                    } else if (tLRPC$BotApp != null) {
                                                        String str12 = tLRPC$BotApp.title;
                                                        if (str12 == null) {
                                                            str12 = str;
                                                        }
                                                        String string2 = LocaleController.getString(R.string.ActionBotAllowedApp);
                                                        int indexOf4 = string2.indexOf("%1$s");
                                                        SpannableString spannableString4 = new SpannableString(String.format(string2, str12));
                                                        TLRPC$User user8 = getUser(abstractMap, longSparseArray, getDialogId());
                                                        if (indexOf4 >= 0 && user8 != null && (publicUsername2 = UserObject.getPublicUsername(user8)) != null) {
                                                            spannableString4.setSpan(new URLSpanNoUnderlineBold("https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername2 + "/" + tLRPC$BotApp.short_name), indexOf4, str12.length() + indexOf4, 33);
                                                        }
                                                        this.messageText = spannableString4;
                                                    } else {
                                                        if (str11 == null) {
                                                            str11 = str;
                                                        }
                                                        String string3 = LocaleController.getString(R.string.ActionBotAllowed);
                                                        int indexOf5 = string3.indexOf("%1$s");
                                                        SpannableString spannableString5 = new SpannableString(String.format(string3, str11));
                                                        if (indexOf5 >= 0 && !TextUtils.isEmpty(str11)) {
                                                            spannableString5.setSpan(new URLSpanNoUnderlineBold("http://" + str11), indexOf5, str11.length() + indexOf5, 33);
                                                        }
                                                        this.messageText = spannableString5;
                                                    }
                                                } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionAttachMenuBotAllowed) || (z6 && ((TLRPC$TL_messageActionBotAllowed) tLRPC$MessageAction).attach_menu)) {
                                                    this.messageText = LocaleController.getString(R.string.ActionAttachMenuBotAllowed);
                                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSecureValuesSent) {
                                                    TLRPC$TL_messageActionSecureValuesSent tLRPC$TL_messageActionSecureValuesSent = (TLRPC$TL_messageActionSecureValuesSent) tLRPC$MessageAction;
                                                    StringBuilder sb5 = new StringBuilder();
                                                    int size = tLRPC$TL_messageActionSecureValuesSent.types.size();
                                                    for (int i18 = 0; i18 < size; i18++) {
                                                        TLRPC$SecureValueType tLRPC$SecureValueType = tLRPC$TL_messageActionSecureValuesSent.types.get(i18);
                                                        if (sb5.length() > 0) {
                                                            sb5.append(str7);
                                                        }
                                                        if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePhone) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentPhone));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeEmail) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentEmail));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeAddress) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentAddress));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePersonalDetails) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentIdentity));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassport) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentPassport));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeDriverLicense) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentDriverLicence));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeIdentityCard) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentIdentityCard));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeUtilityBill) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentUtilityBill));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeBankStatement) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentBankStatement));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeRentalAgreement) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentRentalAgreement));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeInternalPassport) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentInternalPassport));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypePassportRegistration) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentPassportRegistration));
                                                        } else if (tLRPC$SecureValueType instanceof TLRPC$TL_secureValueTypeTemporaryRegistration) {
                                                            sb5.append(LocaleController.getString(R.string.ActionBotDocumentTemporaryRegistration));
                                                        }
                                                    }
                                                    TLRPC$Peer tLRPC$Peer12 = this.messageOwner.peer_id;
                                                    this.messageText = LocaleController.formatString("ActionBotDocuments", R.string.ActionBotDocuments, UserObject.getFirstName(tLRPC$Peer12 != null ? getUser(abstractMap, longSparseArray, tLRPC$Peer12.user_id) : null), sb5.toString());
                                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionWebViewDataSent) {
                                                    this.messageText = LocaleController.formatString("ActionBotWebViewData", R.string.ActionBotWebViewData, ((TLRPC$TL_messageActionWebViewDataSent) tLRPC$MessageAction).text);
                                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                                                    String str13 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                                                    String firstName2 = UserObject.getFirstName(tLRPC$User5);
                                                    boolean z7 = tLRPC$User5 == null && tLRPC$Chat10 != null;
                                                    if (z7) {
                                                        firstName2 = tLRPC$Chat10.title;
                                                    }
                                                    boolean isUserSelf = UserObject.isUserSelf(tLRPC$User5);
                                                    if (TextUtils.isEmpty(str13)) {
                                                        if (isUserSelf) {
                                                            formatString2 = LocaleController.formatString("ChatThemeDisabledYou", R.string.ChatThemeDisabledYou, new Object[0]);
                                                        } else {
                                                            formatString2 = LocaleController.formatString(z7 ? R.string.ChannelThemeDisabled : R.string.ChatThemeDisabled, firstName2, str13);
                                                        }
                                                        this.messageText = formatString2;
                                                    } else {
                                                        if (isUserSelf) {
                                                            formatString = LocaleController.formatString("ChatThemeChangedYou", R.string.ChatThemeChangedYou, str13);
                                                        } else {
                                                            formatString = LocaleController.formatString(z7 ? R.string.ChannelThemeChangedTo : R.string.ChatThemeChangedTo, firstName2, str13);
                                                        }
                                                        this.messageText = formatString;
                                                    }
                                                } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatJoinedByRequest) {
                                                    if (UserObject.isUserSelf(tLRPC$User5)) {
                                                        if (ChatObject.isChannelAndNotMegaGroup(this.messageOwner.peer_id.channel_id, this.currentAccount)) {
                                                            string = LocaleController.getString(R.string.RequestToJoinChannelApproved);
                                                        } else {
                                                            string = LocaleController.getString(R.string.RequestToJoinGroupApproved);
                                                        }
                                                        this.messageText = string;
                                                    } else {
                                                        this.messageText = replaceWithLink(LocaleController.getString(R.string.UserAcceptedToGroupAction), "un1", tLObject2);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    str = "";
                } else {
                    str = "";
                }
            } else {
                TLRPC$Chat tLRPC$Chat11 = chat;
                str = "";
                this.isRestrictedMessage = false;
                String restrictionReason = MessagesController.getInstance(this.currentAccount).getRestrictionReason(this.messageOwner.restriction_reason);
                if (!TextUtils.isEmpty(restrictionReason)) {
                    this.messageText = restrictionReason;
                    this.isRestrictedMessage = true;
                } else if (!isMediaEmpty() && !isSponsored()) {
                    if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveaway) {
                        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = this.messageOwner.fwd_from;
                        if (tLRPC$MessageFwdHeader != null) {
                            TLRPC$Peer tLRPC$Peer13 = tLRPC$MessageFwdHeader.from_id;
                            if (tLRPC$Peer13 instanceof TLRPC$TL_peerChannel) {
                                isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(getChat(abstractMap2, longSparseArray2, tLRPC$Peer13.channel_id));
                                this.messageText = LocaleController.getString(!isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted);
                            }
                        }
                        isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat11);
                        this.messageText = LocaleController.getString(!isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGiveawayResults) {
                        this.messageText = LocaleController.getString(R.string.BoostingGiveawayResults);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaStory) {
                        if (getMedia(this.messageOwner).via_mention) {
                            TLRPC$User user9 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getMedia(this.messageOwner).user_id));
                            String str14 = (user9 == null || (publicUsername = UserObject.getPublicUsername(user9)) == null) ? null : MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + publicUsername + "/s/" + getMedia(this.messageOwner).id;
                            if (str14 != null) {
                                SpannableString spannableString6 = new SpannableString(str14);
                                this.messageText = spannableString6;
                                spannableString6.setSpan(new URLSpanReplacement("https://" + str14, new TextStyleSpan.TextStyleRun()), 0, this.messageText.length(), 33);
                            } else {
                                this.messageText = str;
                            }
                        } else {
                            this.messageText = LocaleController.getString(R.string.ForwardedStory);
                        }
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaDice) {
                        this.messageText = getDiceEmoji();
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPoll) {
                        if (((TLRPC$TL_messageMediaPoll) getMedia(this.messageOwner)).poll.quiz) {
                            this.messageText = LocaleController.getString(R.string.QuizPoll);
                        } else {
                            this.messageText = LocaleController.getString(R.string.Poll);
                        }
                    } else if (isVoiceOnce()) {
                        this.messageText = LocaleController.getString(R.string.AttachOnceAudio);
                    } else if (isRoundOnce()) {
                        this.messageText = LocaleController.getString(R.string.AttachOnceRound);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPaidMedia) {
                        TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) getMedia(this.messageOwner);
                        int size2 = tLRPC$TL_messageMediaPaidMedia.extended_media.size();
                        boolean z8 = false;
                        for (int i19 = 0; i19 < size2; i19++) {
                            TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$TL_messageMediaPaidMedia.extended_media.get(i19);
                            if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                                TLRPC$MessageMedia tLRPC$MessageMedia = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media;
                                z8 = (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && isVideoDocument(tLRPC$MessageMedia.document);
                            } else if (!(tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview)) {
                            }
                            if (z8) {
                                break;
                            }
                        }
                        if (size2 == 1) {
                            formatPluralString = LocaleController.getString(z8 ? R.string.AttachVideo : R.string.AttachPhoto);
                            c = 0;
                        } else {
                            String str15 = z8 ? "Media" : "Photos";
                            c = 0;
                            formatPluralString = LocaleController.formatPluralString(str15, size2, new Object[0]);
                        }
                        this.messageText = formatPluralString;
                        int i20 = R.string.AttachPaidMedia;
                        Object[] objArr = new Object[1];
                        objArr[c] = formatPluralString;
                        this.messageText = StarsIntroActivity.replaceStars(LocaleController.formatString(i20, objArr));
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                        if (getMedia(this.messageOwner).ttl_seconds != 0 && !(this.messageOwner instanceof TLRPC$TL_message_secret)) {
                            this.messageText = LocaleController.getString(R.string.AttachDestructingPhoto);
                        } else if (getGroupId() != 0) {
                            this.messageText = LocaleController.getString(R.string.Album);
                        } else {
                            this.messageText = LocaleController.getString(R.string.AttachPhoto);
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
                        this.messageText = LocaleController.getString(R.string.AttachVideo);
                    } else if (isVoice()) {
                        this.messageText = LocaleController.getString(R.string.AttachAudio);
                    } else if (isRoundVideo()) {
                        this.messageText = LocaleController.getString(R.string.AttachRound);
                    } else if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeo) || (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaVenue)) {
                        this.messageText = LocaleController.getString(R.string.AttachLocation);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGeoLive) {
                        this.messageText = LocaleController.getString(R.string.AttachLiveLocation);
                    } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaContact) {
                        this.messageText = LocaleController.getString(R.string.AttachContact);
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
                                this.messageText = LocaleController.getString(R.string.AttachSticker);
                            } else {
                                this.messageText = String.format("%s %s", stickerChar, LocaleController.getString(R.string.AttachSticker));
                            }
                        } else if (isMusic()) {
                            this.messageText = LocaleController.getString(R.string.AttachMusic);
                        } else if (isGif()) {
                            this.messageText = LocaleController.getString(R.string.AttachGif);
                        } else {
                            String documentFileName = FileLoader.getDocumentFileName(getDocument());
                            if (!TextUtils.isEmpty(documentFileName)) {
                                this.messageText = documentFileName;
                            } else {
                                this.messageText = LocaleController.getString(R.string.AttachDocument);
                            }
                        }
                    }
                } else {
                    String str16 = this.messageOwner.message;
                    if (str16 != null) {
                        try {
                            if (str16.length() > 200) {
                                this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_LONG_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                            } else {
                                this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                            }
                        } catch (Throwable unused) {
                            this.messageText = this.messageOwner.message;
                        }
                    } else {
                        this.messageText = str16;
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
        if (!(tLRPC$Message instanceof TLRPC$TL_messageService)) {
        }
        if (this.messageText != null) {
        }
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
        String formatPluralString;
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
                if (((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll.quiz) {
                    return LocaleController.getString(R.string.QuizPoll);
                }
                return LocaleController.getString(R.string.Poll);
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
                if (size == 1) {
                    formatPluralString = LocaleController.getString(z ? R.string.AttachVideo : R.string.AttachPhoto);
                } else {
                    formatPluralString = LocaleController.formatPluralString(z ? "Media" : "Photos", size, new Object[0]);
                }
                return StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.AttachPaidMedia, formatPluralString));
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                if (tLRPC$MessageMedia.ttl_seconds != 0 && !(this.messageOwner instanceof TLRPC$TL_message_secret)) {
                    return LocaleController.getString(R.string.AttachDestructingPhoto);
                }
                if (getGroupId() != 0) {
                    return LocaleController.getString(R.string.Album);
                }
                return LocaleController.getString(R.string.AttachPhoto);
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
                    return LocaleController.getString(R.string.AttachVideo);
                }
                if (tLRPC$MessageMedia != null && isVoiceDocument(tLRPC$MessageMedia.document)) {
                    return LocaleController.getString(R.string.AttachAudio);
                }
                if (tLRPC$MessageMedia != null && isRoundVideoDocument(tLRPC$MessageMedia.document)) {
                    return LocaleController.getString(R.string.AttachRound);
                }
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
        }
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
        if (tLRPC$MessageMedia != null && !(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) && !tLRPC$MessageMedia.extended_media.isEmpty() && (tLRPC$Message.media.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMedia)) {
            return ((TLRPC$TL_messageExtendedMedia) tLRPC$Message.media.extended_media.get(0)).media;
        }
        return tLRPC$Message.media;
    }

    public boolean hasRevealedExtendedMedia() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) || tLRPC$MessageMedia.extended_media.isEmpty() || !(this.messageOwner.media.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMedia)) ? false : true;
    }

    public boolean hasExtendedMedia() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) || tLRPC$MessageMedia.extended_media.isEmpty()) ? false : true;
    }

    public boolean hasPaidMediaPreview() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return tLRPC$MessageMedia != null && (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) && !tLRPC$MessageMedia.extended_media.isEmpty() && (this.messageOwner.media.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMediaPreview);
    }

    public boolean hasExtendedMediaPreview() {
        TLRPC$MessageMedia tLRPC$MessageMedia = this.messageOwner.media;
        return (tLRPC$MessageMedia == null || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) || tLRPC$MessageMedia.extended_media.isEmpty() || !(this.messageOwner.media.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMediaPreview)) ? false : true;
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
        if (isSponsored()) {
            this.type = 0;
        } else if (this.channelJoined) {
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
                } else if (tLRPC$Message.media instanceof TLRPC$TL_messageMediaPaidMedia) {
                    this.type = 29;
                } else if (isMediaEmpty(false) && !isDice() && !isSponsored() && this.emojiOnlyCount >= 1 && !this.hasUnwrappedEmoji && this.messageOwner != null && !hasNonEmojiEntities()) {
                    this.type = 19;
                } else if (isMediaEmpty()) {
                    this.type = 0;
                    if (TextUtils.isEmpty(this.messageText) && this.eventId == 0) {
                        this.messageText = LocaleController.getString(R.string.EventLogOriginalCaptionEmpty);
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
                    } else {
                        boolean z = tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftCode;
                        if (z && ((TLRPC$TL_messageActionGiftCode) tLRPC$MessageAction).boost_peer != null) {
                            this.contentType = 1;
                            this.type = 25;
                        } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftPremium) || z) {
                            this.contentType = 1;
                            this.type = 18;
                        } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftStars) {
                            this.contentType = 1;
                            this.type = 30;
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
        if (tLRPC$WebDocument != null) {
            return tLRPC$WebDocument.mime_type;
        }
        return "";
    }

    public boolean canPreviewDocument() {
        return canPreviewDocument(getDocument());
    }

    public static boolean isAnimatedStickerDocument(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && tLRPC$Document.mime_type.equals("video/webm");
    }

    public static boolean isStaticStickerDocument(TLRPC$Document tLRPC$Document) {
        return tLRPC$Document != null && tLRPC$Document.mime_type.equals("image/webp");
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
        ArrayList<TLRPC$PhotoSize> arrayList9;
        if (hasExtendedMediaPreview()) {
            TLRPC$TL_messageExtendedMediaPreview tLRPC$TL_messageExtendedMediaPreview = (TLRPC$TL_messageExtendedMediaPreview) this.messageOwner.media.extended_media.get(0);
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
                    ArrayList<TLRPC$PhotoSize> arrayList10 = this.photoThumbs;
                    if (arrayList10 != null && !arrayList10.isEmpty()) {
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
                if (tLRPC$Photo.dc_id != 0 && (arrayList9 = this.photoThumbs) != null) {
                    int size = arrayList9.size();
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
                    ArrayList<TLRPC$PhotoSize> arrayList11 = new ArrayList<>();
                    this.photoThumbs = arrayList11;
                    arrayList11.addAll(this.emojiAnimatedSticker.thumbs);
                } else if (!arrayList.isEmpty()) {
                    updatePhotoSizeLocations(this.photoThumbs, this.emojiAnimatedSticker.thumbs);
                }
                this.photoThumbsObject = this.emojiAnimatedSticker;
            }
        } else if (getMedia(tLRPC$Message) != null && !(getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaEmpty)) {
            if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) {
                TLRPC$Photo tLRPC$Photo2 = getMedia(this.messageOwner).photo;
                if (!z || ((arrayList8 = this.photoThumbs) != null && arrayList8.size() != tLRPC$Photo2.sizes.size())) {
                    this.photoThumbs = new ArrayList<>(tLRPC$Photo2.sizes);
                } else {
                    ArrayList<TLRPC$PhotoSize> arrayList12 = this.photoThumbs;
                    if (arrayList12 != null && !arrayList12.isEmpty()) {
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
                    if (!z || (arrayList7 = this.photoThumbs) == null) {
                        ArrayList<TLRPC$PhotoSize> arrayList13 = new ArrayList<>();
                        this.photoThumbs = arrayList13;
                        arrayList13.addAll(document.thumbs);
                    } else if (!arrayList7.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs, document.thumbs);
                    }
                    this.photoThumbsObject = document;
                }
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaGame) {
                TLRPC$Document tLRPC$Document = getMedia(this.messageOwner).game.document;
                if (tLRPC$Document != null && isDocumentHasThumb(tLRPC$Document)) {
                    if (!z) {
                        ArrayList<TLRPC$PhotoSize> arrayList14 = new ArrayList<>();
                        this.photoThumbs = arrayList14;
                        arrayList14.addAll(tLRPC$Document.thumbs);
                    } else {
                        ArrayList<TLRPC$PhotoSize> arrayList15 = this.photoThumbs;
                        if (arrayList15 != null && !arrayList15.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document.thumbs);
                        }
                    }
                    this.photoThumbsObject = tLRPC$Document;
                }
                TLRPC$Photo tLRPC$Photo3 = getMedia(this.messageOwner).game.photo;
                if (tLRPC$Photo3 != null) {
                    if (!z || (arrayList6 = this.photoThumbs2) == null) {
                        this.photoThumbs2 = new ArrayList<>(tLRPC$Photo3.sizes);
                    } else if (!arrayList6.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs2, tLRPC$Photo3.sizes);
                    }
                    this.photoThumbsObject2 = tLRPC$Photo3;
                }
                if (this.photoThumbs != null || (arrayList5 = this.photoThumbs2) == null) {
                    return;
                }
                this.photoThumbs = arrayList5;
                this.photoThumbs2 = null;
                this.photoThumbsObject = this.photoThumbsObject2;
                this.photoThumbsObject2 = null;
            } else if (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) {
                TLRPC$Photo tLRPC$Photo4 = getMedia(this.messageOwner).webpage.photo;
                TLRPC$Document tLRPC$Document2 = getMedia(this.messageOwner).webpage.document;
                if (tLRPC$Photo4 != null) {
                    if (!z || (arrayList4 = this.photoThumbs) == null) {
                        this.photoThumbs = new ArrayList<>(tLRPC$Photo4.sizes);
                    } else if (!arrayList4.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs, tLRPC$Photo4.sizes);
                    }
                    this.photoThumbsObject = tLRPC$Photo4;
                } else if (tLRPC$Document2 != null && isDocumentHasThumb(tLRPC$Document2)) {
                    if (!z) {
                        ArrayList<TLRPC$PhotoSize> arrayList16 = new ArrayList<>();
                        this.photoThumbs = arrayList16;
                        arrayList16.addAll(tLRPC$Document2.thumbs);
                    } else {
                        ArrayList<TLRPC$PhotoSize> arrayList17 = this.photoThumbs;
                        if (arrayList17 != null && !arrayList17.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document2.thumbs);
                        }
                    }
                    this.photoThumbsObject = tLRPC$Document2;
                }
            }
        } else {
            TLRPC$MessageMedia tLRPC$MessageMedia = this.sponsoredMedia;
            if (tLRPC$MessageMedia != null) {
                TLRPC$Photo tLRPC$Photo5 = tLRPC$MessageMedia.photo;
                TLRPC$Document tLRPC$Document3 = tLRPC$MessageMedia.document;
                if (tLRPC$Photo5 != null) {
                    if (!z || (arrayList3 = this.photoThumbs) == null) {
                        this.photoThumbs = new ArrayList<>(tLRPC$Photo5.sizes);
                    } else if (!arrayList3.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs, tLRPC$Photo5.sizes);
                    }
                    this.photoThumbsObject = tLRPC$Photo5;
                } else if (tLRPC$Document3 != null && isDocumentHasThumb(tLRPC$Document3)) {
                    if (!z) {
                        ArrayList<TLRPC$PhotoSize> arrayList18 = new ArrayList<>();
                        this.photoThumbs = arrayList18;
                        arrayList18.addAll(tLRPC$Document3.thumbs);
                    } else {
                        ArrayList<TLRPC$PhotoSize> arrayList19 = this.photoThumbs;
                        if (arrayList19 != null && !arrayList19.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, tLRPC$Document3.thumbs);
                        }
                    }
                    this.photoThumbsObject = tLRPC$Document3;
                }
            } else if (this.sponsoredPhoto != null) {
                if (!z || (arrayList2 = this.photoThumbs) == null) {
                    this.photoThumbs = new ArrayList<>(this.sponsoredPhoto.sizes);
                } else if (!arrayList2.isEmpty()) {
                    updatePhotoSizeLocations(this.photoThumbs, this.sponsoredPhoto.sizes);
                }
                this.photoThumbsObject = this.sponsoredPhoto;
                if (this.strippedThumb == null) {
                    createStrippedThumb();
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
        TLRPC$User tLRPC$User;
        if (TextUtils.indexOf(charSequence, str) >= 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("");
            for (int i = 0; i < arrayList.size(); i++) {
                if (abstractMap != null) {
                    tLRPC$User = abstractMap.get(arrayList.get(i));
                } else {
                    tLRPC$User = longSparseArray != null ? longSparseArray.get(arrayList.get(i).longValue()) : null;
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
        TLRPC$Chat tLRPC$Chat;
        TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported;
        TLRPC$User tLRPC$User;
        int indexOf = TextUtils.indexOf(charSequence, str);
        if (indexOf >= 0) {
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
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{charSequence2}));
                URLSpanNoUnderlineBold uRLSpanNoUnderlineBold = new URLSpanNoUnderlineBold("" + str3);
                uRLSpanNoUnderlineBold.setObject(tLRPC$TL_chatInviteExported);
                spannableStringBuilder.setSpan(uRLSpanNoUnderlineBold, indexOf, charSequence2.length() + indexOf, 33);
                return spannableStringBuilder;
            }
            str3 = str2;
            tLRPC$TL_chatInviteExported = null;
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(TextUtils.replace(charSequence, new String[]{str}, new CharSequence[]{charSequence2}));
            URLSpanNoUnderlineBold uRLSpanNoUnderlineBold2 = new URLSpanNoUnderlineBold("" + str3);
            uRLSpanNoUnderlineBold2.setObject(tLRPC$TL_chatInviteExported);
            spannableStringBuilder2.setSpan(uRLSpanNoUnderlineBold2, indexOf, charSequence2.length() + indexOf, 33);
            return spannableStringBuilder2;
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
        if (getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPhoto) {
            ArrayList<TLRPC$PhotoSize> arrayList = getMedia(tLRPC$Message).photo.sizes;
            if (arrayList.size() > 0 && (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) != null) {
                return FileLoader.getAttachFileName(closestPhotoSizeWithSize);
            }
            return "";
        } else if ((getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(tLRPC$Message).webpage != null) {
            return FileLoader.getAttachFileName(getMedia(tLRPC$Message).webpage.document);
        } else {
            return "";
        }
    }

    public static String getFileName(TLRPC$MessageMedia tLRPC$MessageMedia) {
        TLRPC$WebPage tLRPC$WebPage;
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            return FileLoader.getAttachFileName(tLRPC$MessageMedia.document);
        }
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            ArrayList<TLRPC$PhotoSize> arrayList = tLRPC$MessageMedia.photo.sizes;
            if (arrayList.size() > 0 && (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize())) != null) {
                return FileLoader.getAttachFileName(closestPhotoSizeWithSize);
            }
            return "";
        } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && (tLRPC$WebPage = tLRPC$MessageMedia.webpage) != null) {
            return FileLoader.getAttachFileName(tLRPC$WebPage.document);
        } else {
            return "";
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

    /* JADX WARN: Removed duplicated region for block: B:56:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:78:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLinkDescription() {
        boolean z;
        int i;
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
                TLRPC$WebPageAttribute tLRPC$WebPageAttribute = tLRPC$WebPage.attributes.get(i2);
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
                    i = 1;
                } else if ("twitter".equals(str)) {
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
        String str2 = this.messageOwner.voiceTranscription;
        return !TextUtils.isEmpty(str2) ? Emoji.replaceEmoji((CharSequence) str2, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false) : str2;
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

    public boolean isVoiceTranscriptionOpen() {
        if (this.messageOwner != null && (isVoice() || (isRoundVideo() && TranscribeButton.isVideoTranscriptionOpen(this)))) {
            TLRPC$Message tLRPC$Message = this.messageOwner;
            if (tLRPC$Message.voiceTranscriptionOpen && tLRPC$Message.voiceTranscription != null && (tLRPC$Message.voiceTranscriptionFinal || TranscribeButton.isTranscribing(this))) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
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

    /* JADX WARN: Removed duplicated region for block: B:89:0x01dc A[Catch: Exception -> 0x001e, TryCatch #0 {Exception -> 0x001e, blocks: (B:10:0x0011, B:12:0x0015, B:15:0x0021, B:24:0x004d, B:27:0x0052, B:28:0x0055, B:30:0x005b, B:34:0x006c, B:38:0x007c, B:39:0x007e, B:51:0x0097, B:101:0x021c, B:103:0x0226, B:105:0x0229, B:106:0x022e, B:55:0x00be, B:58:0x00e3, B:59:0x0104, B:60:0x0125, B:63:0x012d, B:65:0x0146, B:67:0x0152, B:68:0x015b, B:48:0x0091, B:69:0x0165, B:72:0x01a4, B:77:0x01b7, B:82:0x01c6, B:84:0x01d0, B:87:0x01d7, B:89:0x01dc, B:95:0x01e7, B:97:0x0215, B:96:0x01ff, B:16:0x0028, B:18:0x002c, B:19:0x0034, B:20:0x003b, B:22:0x003f, B:23:0x0047), top: B:110:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void addUrlsByPattern(boolean z, CharSequence charSequence, boolean z2, int i, int i2, boolean z3) {
        Matcher matcher;
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

    public static int[] getWebDocumentWidthAndHeight(TLRPC$WebDocument tLRPC$WebDocument) {
        if (tLRPC$WebDocument == null) {
            return null;
        }
        int size = tLRPC$WebDocument.attributes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$WebDocument.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeImageSize) {
                return new int[]{tLRPC$DocumentAttribute.w, tLRPC$DocumentAttribute.h};
            }
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                return new int[]{tLRPC$DocumentAttribute.w, tLRPC$DocumentAttribute.h};
            }
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
            try {
                AndroidUtilities.addLinksSafe((Spannable) charSequence, 1, z4, false);
            } catch (Exception e) {
                FileLog.e(e);
            }
            addPhoneLinks(charSequence);
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

    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, boolean z, boolean z2, boolean z3, boolean z4) {
        return addEntitiesToText(charSequence, arrayList, z, z2, z3, z4, 0);
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x0191  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0439  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x044d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0245 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean addEntitiesToText(CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, boolean z, boolean z2, boolean z3, boolean z4, int i) {
        int i2;
        String str;
        TextStyleSpan.TextStyleRun textStyleRun;
        boolean z5;
        boolean z6;
        int i3;
        int i4;
        int size;
        int i5;
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
            int i6 = 0;
            while (i6 < size2) {
                TLRPC$MessageEntity tLRPC$MessageEntity = (TLRPC$MessageEntity) arrayList3.get(i6);
                if (tLRPC$MessageEntity.length > 0 && (i4 = tLRPC$MessageEntity.offset) >= 0 && i4 < charSequence.length()) {
                    if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length > charSequence.length()) {
                        tLRPC$MessageEntity.length = charSequence.length() - tLRPC$MessageEntity.offset;
                    }
                    if ((!z4 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji)) && uRLSpanArr != null && uRLSpanArr.length > 0) {
                        for (int i7 = 0; i7 < uRLSpanArr.length; i7++) {
                            URLSpan uRLSpan = uRLSpanArr[i7];
                            if (uRLSpan != null) {
                                int spanStart = spannable.getSpanStart(uRLSpan);
                                int spanEnd = spannable.getSpanEnd(uRLSpanArr[i7]);
                                int i8 = tLRPC$MessageEntity.offset;
                                if ((i8 <= spanStart && tLRPC$MessageEntity.length + i8 >= spanStart) || (i8 <= spanEnd && i8 + tLRPC$MessageEntity.length >= spanEnd)) {
                                    spannable.removeSpan(uRLSpanArr[i7]);
                                    uRLSpanArr[i7] = null;
                                }
                            }
                        }
                    }
                    if ((i != 1 || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityHashtag)) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) && !(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre)) {
                        TextStyleSpan.TextStyleRun textStyleRun2 = new TextStyleSpan.TextStyleRun();
                        int i9 = tLRPC$MessageEntity.offset;
                        textStyleRun2.start = i9;
                        textStyleRun2.end = i9 + tLRPC$MessageEntity.length;
                        if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) {
                            textStyleRun2.flags = 256;
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
                                        textStyleRun2.flags = 1152;
                                    }
                                }
                                i6++;
                                size2 = i3;
                            }
                            size = arrayList2.size();
                            i5 = 0;
                            while (i5 < size) {
                                TextStyleSpan.TextStyleRun textStyleRun3 = (TextStyleSpan.TextStyleRun) arrayList2.get(i5);
                                int i10 = size2;
                                if ((textStyleRun3.flags & 256) == 0 || textStyleRun2.start < textStyleRun3.start || textStyleRun2.end > textStyleRun3.end) {
                                    int i11 = textStyleRun2.start;
                                    int i12 = textStyleRun3.start;
                                    if (i11 > i12) {
                                        int i13 = textStyleRun3.end;
                                        if (i11 < i13) {
                                            if (textStyleRun2.end < i13) {
                                                TextStyleSpan.TextStyleRun textStyleRun4 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                                textStyleRun4.merge(textStyleRun3);
                                                arrayList2.add(i5 + 1, textStyleRun4);
                                                TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun(textStyleRun3);
                                                textStyleRun5.start = textStyleRun2.end;
                                                i5 += 2;
                                                size += 2;
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
                                        if (i12 < i15) {
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
                                            textStyleRun2.end = i12;
                                        }
                                    }
                                }
                                i5++;
                                size2 = i10;
                            }
                            i3 = size2;
                            if (textStyleRun2.start >= textStyleRun2.end) {
                                arrayList2.add(textStyleRun2);
                            }
                            i6++;
                            size2 = i3;
                        }
                        size = arrayList2.size();
                        i5 = 0;
                        while (i5 < size) {
                        }
                        i3 = size2;
                        if (textStyleRun2.start >= textStyleRun2.end) {
                        }
                        i6++;
                        size2 = i3;
                    }
                }
                i3 = size2;
                i6++;
                size2 = i3;
            }
            int size3 = arrayList2.size();
            boolean z8 = z7;
            int i17 = 0;
            while (i17 < size3) {
                TextStyleSpan.TextStyleRun textStyleRun9 = (TextStyleSpan.TextStyleRun) arrayList2.get(i17);
                if (i != 1 || (textStyleRun9.urlEntity instanceof TLRPC$TL_messageEntityHashtag)) {
                    TLRPC$MessageEntity tLRPC$MessageEntity2 = textStyleRun9.urlEntity;
                    if (tLRPC$MessageEntity2 != null) {
                        int i18 = tLRPC$MessageEntity2.offset;
                        str = TextUtils.substring(charSequence2, i18, tLRPC$MessageEntity2.length + i18);
                    } else {
                        str = null;
                    }
                    TLRPC$MessageEntity tLRPC$MessageEntity3 = textStyleRun9.urlEntity;
                    if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBotCommand) {
                        spannable.setSpan(new URLSpanBotCommand(str, b, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                    } else {
                        if ((tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityHashtag) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMention) || (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityCashtag)) {
                            textStyleRun = textStyleRun9;
                            spannable.setSpan(new URLSpanNoUnderline(str, textStyleRun), textStyleRun.start, textStyleRun.end, 33);
                        } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityEmail) {
                            spannable.setSpan(new URLSpanReplacement("mailto:" + str, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                        } else {
                            if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityUrl) {
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
                                    spannable.setSpan(new URLSpanBrowser(str, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                                    textStyleRun = textStyleRun9;
                                    z5 = true;
                                    z6 = false;
                                    if (z6 && (textStyleRun.flags & 256) != 0) {
                                        spannable.setSpan(new TextStyleSpan(textStyleRun), textStyleRun.start, textStyleRun.end, 33);
                                    }
                                    z8 = z5;
                                }
                            } else {
                                if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityBankCard) {
                                    spannable.setSpan(new URLSpanNoUnderline("card:" + str, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityPhone) {
                                    String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str);
                                    if (str.startsWith("+")) {
                                        stripExceptNumbers = "+" + stripExceptNumbers;
                                    }
                                    spannable.setSpan(new URLSpanNoUnderline("tel:" + stripExceptNumbers, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityTextUrl) {
                                    String str2 = tLRPC$MessageEntity3.url;
                                    if (str2 != null) {
                                        str2 = str2.replaceAll("∕|⁄|%E2%81%84|%E2%88%95", "/");
                                    }
                                    if (!Browser.isTonsitePunycode(str2)) {
                                        spannable.setSpan(new URLSpanReplacement(str2, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                                    }
                                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_messageEntityMentionName) {
                                    spannable.setSpan(new URLSpanUserMention("" + ((TLRPC$TL_messageEntityMentionName) textStyleRun9.urlEntity).user_id, b, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                                } else if (tLRPC$MessageEntity3 instanceof TLRPC$TL_inputMessageEntityMentionName) {
                                    spannable.setSpan(new URLSpanUserMention("" + ((TLRPC$TL_inputMessageEntityMentionName) textStyleRun9.urlEntity).user_id.user_id, b, textStyleRun9), textStyleRun9.start, textStyleRun9.end, 33);
                                } else if ((textStyleRun9.flags & 4) != 0) {
                                    textStyleRun = textStyleRun9;
                                    spannable.setSpan(new URLSpanMono(spannable, textStyleRun9.start, textStyleRun9.end, b, textStyleRun9), textStyleRun.start, textStyleRun.end, 33);
                                } else {
                                    textStyleRun = textStyleRun9;
                                    spannable.setSpan(new TextStyleSpan(textStyleRun), textStyleRun.start, textStyleRun.end, 33);
                                    z5 = z8;
                                    z6 = true;
                                    if (z6) {
                                        spannable.setSpan(new TextStyleSpan(textStyleRun), textStyleRun.start, textStyleRun.end, 33);
                                    }
                                    z8 = z5;
                                }
                                textStyleRun = textStyleRun9;
                                z5 = true;
                                z6 = false;
                                if (z6) {
                                }
                                z8 = z5;
                            }
                            i17++;
                            charSequence2 = charSequence;
                        }
                        z5 = z8;
                        z6 = false;
                        if (z6) {
                        }
                        z8 = z5;
                        i17++;
                        charSequence2 = charSequence;
                    }
                    textStyleRun = textStyleRun9;
                    z5 = z8;
                    z6 = false;
                    if (z6) {
                    }
                    z8 = z5;
                    i17++;
                    charSequence2 = charSequence;
                }
                i17++;
                charSequence2 = charSequence;
            }
            int size4 = arrayList3.size();
            for (int i19 = 0; i19 < size4; i19++) {
                TLRPC$MessageEntity tLRPC$MessageEntity4 = (TLRPC$MessageEntity) arrayList3.get(i19);
                if (tLRPC$MessageEntity4.length > 0 && (i2 = tLRPC$MessageEntity4.offset) >= 0 && i2 < charSequence.length()) {
                    if (tLRPC$MessageEntity4.offset + tLRPC$MessageEntity4.length > charSequence.length()) {
                        tLRPC$MessageEntity4.length = charSequence.length() - tLRPC$MessageEntity4.offset;
                    }
                    if (tLRPC$MessageEntity4 instanceof TLRPC$TL_messageEntityBlockquote) {
                        int i20 = tLRPC$MessageEntity4.offset;
                        QuoteSpan.putQuote(spannable, i20, tLRPC$MessageEntity4.length + i20, tLRPC$MessageEntity4.collapsed);
                    } else if (tLRPC$MessageEntity4 instanceof TLRPC$TL_messageEntityPre) {
                        int i21 = tLRPC$MessageEntity4.offset;
                        int i22 = tLRPC$MessageEntity4.length + i21;
                        spannable.setSpan(new CodeHighlighting.Span(true, 0, null, tLRPC$MessageEntity4.language, spannable.subSequence(i21, i22).toString()), i21, i22, 33);
                    }
                }
            }
            return z8;
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

    public boolean isYouTubeVideo() {
        return (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && getMedia(this.messageOwner).webpage != null && !TextUtils.isEmpty(getMedia(this.messageOwner).webpage.embed_url) && "YouTube".equals(getMedia(this.messageOwner).webpage.site_name);
    }

    public int getMaxMessageTextWidth() {
        int dp;
        Uri parse;
        String lastPathSegment;
        if (AndroidUtilities.isTablet() && this.eventId != 0) {
            this.generatedWithMinSize = AndroidUtilities.dp(530.0f);
        } else {
            this.generatedWithMinSize = AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : getParentWidth();
        }
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

    /* JADX WARN: Can't wrap try/catch for region: R(16:266|267|268|269|270|(1:272)(11:302|(1:304)|274|275|276|(1:278)|279|(2:281|(3:283|(5:287|288|(1:293)|290|291)|292))(1:299)|298|(1:297)(6:285|287|288|(0)|290|291)|292)|273|274|275|276|(0)|279|(0)(0)|298|(0)(0)|292) */
    /* JADX WARN: Can't wrap try/catch for region: R(49:144|(1:146)|147|(1:149)(1:418)|150|(1:152)(1:417)|153|(1:155)|(1:157)|(1:416)(1:162)|163|(1:415)(1:170)|171|(2:173|(2:(1:398)|399)(1:176))(2:400|(7:402|(1:404)(1:414)|405|(1:407)(1:413)|408|(1:410)(1:412)|411))|177|(3:179|(1:181)(1:(1:394)(1:395))|182)(1:396)|183|(1:185)(2:389|(1:391)(1:392))|186|(5:188|(1:365)(8:194|(1:196)(1:364)|197|198|(1:200)(1:363)|201|(1:203)(1:362)|204)|205|(2:207|(2:209|(2:211|(1:213))(1:214))(1:215))|216)(3:366|(2:368|369)(8:370|371|372|(1:383)(1:376)|377|378|(1:380)(1:382)|381)|331)|217|218|219|220|(2:224|225)|358|231|232|233|(1:235)(18:352|(1:354)|237|238|(1:240)|241|(1:243)|244|(3:246|(7:248|249|250|251|252|254|255)|261)|262|(6:264|(16:266|267|268|269|270|(1:272)(11:302|(1:304)|274|275|276|(1:278)|279|(2:281|(3:283|(5:287|288|(1:293)|290|291)|292))(1:299)|298|(1:297)(6:285|287|288|(0)|290|291)|292)|273|274|275|276|(0)|279|(0)(0)|298|(0)(0)|292)|307|308|(2:(1:311)|312)(1:(1:338))|313)(3:339|(5:341|(1:343)(1:350)|344|(1:346)(1:349)|347)(1:351)|348)|314|(3:316|(1:318)(1:320)|319)|321|(1:336)(3:325|(1:327)(3:332|(1:334)|335)|328)|329|330|331)|236|237|238|(0)|241|(0)|244|(0)|262|(0)(0)|314|(0)|321|(1:323)|336|329|330|331|142) */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0524, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0525, code lost:
        r11 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0537, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x0538, code lost:
        org.telegram.messenger.FileLog.e(r0);
        r13 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x05e2, code lost:
        r9 = 0.0f;
     */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0240 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0247  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0249  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x02ca  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x0540  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x054b  */
    /* JADX WARN: Removed duplicated region for block: B:326:0x0560  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0565  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x057d  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x05a5  */
    /* JADX WARN: Removed duplicated region for block: B:361:0x05eb  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x05f2  */
    /* JADX WARN: Removed duplicated region for block: B:368:0x0607  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x061c  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x0679  */
    /* JADX WARN: Removed duplicated region for block: B:400:0x06c3  */
    /* JADX WARN: Removed duplicated region for block: B:407:0x06f1  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x0743  */
    /* JADX WARN: Removed duplicated region for block: B:480:0x062a A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:482:0x062a A[SYNTHETIC] */
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
        TextPaint textPaint;
        SpannableStringBuilder spannableStringBuilder;
        int i;
        int dp2;
        boolean z2;
        ArrayList arrayList;
        int i2;
        boolean z3;
        int i3;
        boolean z4;
        TextPaint textPaint2;
        int dp3;
        boolean z5;
        TextPaint textPaint3;
        ArrayList arrayList2;
        CharSequence charSequence;
        SpannableString valueOf;
        float f;
        int dp4;
        int ceil;
        int i4;
        int i5;
        StaticLayout staticLayout;
        int i6;
        Text text;
        int i7;
        StaticLayout staticLayout2;
        StaticLayout staticLayout3;
        float f2;
        CharSequence charSequence2;
        int dp5;
        float f3;
        int i8;
        int i9;
        int i10;
        SpannableString spannableString;
        TextPaint textPaint4;
        int i11;
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
                    if (this.type != 0) {
                        i11 = hasValidGroupId() ? 7 : 12;
                    } else {
                        i11 = 22;
                    }
                    if (isWebpage()) {
                        i11 -= 8;
                    }
                    spannableStringBuilder = charSequence6;
                    if (makeStaticLayout.getLineCount() > i11) {
                        String string = LocaleController.getString(R.string.ReadMore);
                        int ceil2 = (int) Math.ceil(textPaint.measureText("… " + string) + AndroidUtilities.dp(1.0f));
                        float f4 = 0.0f;
                        for (int i13 = 0; i13 < i11; i13++) {
                            f4 = Math.max(f4, makeStaticLayout.getLineRight(i13));
                        }
                        int i14 = i11 - 1;
                        int lineStart = makeStaticLayout.getLineStart(i14);
                        int lineEnd = makeStaticLayout.getLineEnd(i14) - 1;
                        while (lineEnd >= lineStart && makeStaticLayout.getPrimaryHorizontal(lineEnd) >= f4 - ceil2) {
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
                } else if (this.hasSingleCode) {
                    dp2 = AndroidUtilities.dp(15.0f);
                } else {
                    i = maxMessageTextWidth;
                    int lineCount = makeStaticLayout.getLineCount();
                    int i15 = this.totalAnimatedEmojiCount;
                    int i16 = i15 >= 50 ? 5 : 10;
                    z2 = Build.VERSION.SDK_INT < 24 && i15 < 50;
                    int ceil3 = z2 ? 1 : (int) Math.ceil(lineCount / i16);
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
                        int i17 = i2;
                        i3 = 0;
                        CharSequence charSequence7 = spannableStringBuilder;
                        while (i3 < arrayList.size()) {
                            TextLayoutBlock textLayoutBlock = new TextLayoutBlock();
                            TextRange textRange = (TextRange) arrayList.get(i3);
                            textLayoutBlock.code = textRange.code;
                            textLayoutBlock.quote = textRange.quote;
                            boolean z7 = textRange.collapse;
                            textLayoutBlock.quoteCollapse = z7;
                            if (z7) {
                                textLayoutBlock.messageObject = this;
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
                            this.hasSingleCode = z9 && z8 && !textLayoutBlock.quote && textLayoutBlock.code;
                            if (textLayoutBlock.quote) {
                                if (z9 && z8) {
                                    int dp6 = AndroidUtilities.dp(6.0f);
                                    textLayoutBlock.padBottom = dp6;
                                    textLayoutBlock.padTop = dp6;
                                } else {
                                    textLayoutBlock.padTop = AndroidUtilities.dp(z9 ? 8.0f : 6.0f);
                                    textLayoutBlock.padBottom = AndroidUtilities.dp(7.0f);
                                }
                            } else if (textLayoutBlock.code) {
                                textLayoutBlock.layoutCode(textRange.language, textRange.end - textRange.start, z);
                                textLayoutBlock.padTop = AndroidUtilities.dp(4.0f) + textLayoutBlock.languageHeight + (textLayoutBlock.first ? 0 : AndroidUtilities.dp(5.0f));
                                textLayoutBlock.padBottom = AndroidUtilities.dp(4.0f) + (textLayoutBlock.last ? 0 : AndroidUtilities.dp(7.0f)) + (textLayoutBlock.hasCodeCopyButton ? AndroidUtilities.dp(38.0f) : 0);
                            }
                            if (textLayoutBlock.code) {
                                int i18 = textRange.end - textRange.start;
                                if (i18 > 220) {
                                    textPaint4 = Theme.chat_msgTextCode3Paint;
                                } else if (i18 > 80) {
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
                                dp3 = i - AndroidUtilities.dp(24.0f);
                            } else {
                                dp3 = textLayoutBlock.code ? i - AndroidUtilities.dp(15.0f) : i;
                            }
                            if (size == 1) {
                                if (textLayoutBlock.code && !textLayoutBlock.quote && (makeStaticLayout.getText() instanceof Spannable)) {
                                    if (!TextUtils.isEmpty(textRange.language)) {
                                        spannableString = CodeHighlighting.getHighlighted(subSequence.toString(), textRange.language);
                                    } else {
                                        spannableString = new SpannableString(subSequence.toString());
                                    }
                                    SpannableString spannableString2 = spannableString;
                                    textLayoutBlock.originalWidth = dp3;
                                    makeStaticLayout = makeStaticLayout(spannableString2, textPaint2, dp3, 1.0f, this.totalAnimatedEmojiCount >= 4 ? -1.0f : 0.0f, this.emojiOnlyCount > 0);
                                    i17 = dp3;
                                } else {
                                    textLayoutBlock.originalWidth = i17;
                                }
                                textLayoutBlock.textLayout = makeStaticLayout;
                                textLayoutBlock.charactersOffset = 0;
                                textLayoutBlock.charactersEnd = makeStaticLayout.getText().length();
                                textLayoutBlock.height = makeStaticLayout.getHeight();
                                textLayoutBlock.collapsedHeight = (int) Math.min(textPaint.getTextSize() * 1.4f * 3.0f, textLayoutBlock.height);
                                int i19 = this.emojiOnlyCount;
                                if (i19 != 0) {
                                    if (i19 == 1) {
                                        textLayoutBlock.padTop -= AndroidUtilities.dp(5.3f);
                                    } else if (i19 == 2) {
                                        textLayoutBlock.padTop -= AndroidUtilities.dp(4.5f);
                                    } else if (i19 == 3) {
                                        textLayoutBlock.padTop -= AndroidUtilities.dp(4.2f);
                                    }
                                }
                            } else {
                                int i20 = textRange.start;
                                int i21 = textRange.end;
                                if (i21 < i20) {
                                    z5 = z;
                                    textPaint3 = textPaint;
                                    arrayList2 = arrayList;
                                    charSequence = charSequence7;
                                } else {
                                    textLayoutBlock.charactersOffset = i20;
                                    textLayoutBlock.charactersEnd = i21;
                                    try {
                                        if (textLayoutBlock.code && !textLayoutBlock.quote) {
                                            valueOf = CodeHighlighting.getHighlighted(subSequence.toString(), textRange.language);
                                        } else {
                                            valueOf = SpannableString.valueOf(subSequence);
                                        }
                                        SpannableString spannableString3 = valueOf;
                                        textLayoutBlock.originalWidth = dp3;
                                        StaticLayout makeStaticLayout2 = makeStaticLayout(spannableString3, textPaint2, dp3, 1.0f, this.totalAnimatedEmojiCount >= 4 ? -1.0f : 0.0f, false);
                                        textLayoutBlock.textLayout = makeStaticLayout2;
                                        textLayoutBlock.height = makeStaticLayout2.getHeight();
                                        textLayoutBlock.collapsedHeight = (int) Math.min(textPaint.getTextSize() * 1.4f * 3.0f, textLayoutBlock.height);
                                    } catch (Exception e2) {
                                        z5 = z;
                                        textPaint3 = textPaint;
                                        arrayList2 = arrayList;
                                        charSequence = charSequence7;
                                        FileLog.e(e2);
                                    }
                                }
                                i3++;
                                arrayList = arrayList2;
                                z = z5;
                                textPaint = textPaint3;
                                charSequence7 = charSequence;
                            }
                            this.textLayoutBlocks.add(textLayoutBlock);
                            int lineCount2 = textLayoutBlock.textLayout.getLineCount();
                            float lineLeft = textLayoutBlock.textLayout.getLineLeft(lineCount2 - 1);
                            float f5 = 0.0f;
                            if (i3 == 0 && lineLeft >= 0.0f) {
                                try {
                                    this.textXOffset = lineLeft;
                                } catch (Exception e3) {
                                    e = e3;
                                    if (i3 == 0) {
                                        this.textXOffset = f5;
                                    }
                                    FileLog.e(e);
                                    f = 0.0f;
                                    float f6 = textLayoutBlock.textLayout.getLineWidth(lineCount2 - 1);
                                    if (!textLayoutBlock.quote) {
                                    }
                                    f6 += dp4;
                                    arrayList2 = arrayList;
                                    ceil = (int) Math.ceil(f6);
                                    if (ceil > i + 80) {
                                    }
                                    i4 = size - 1;
                                    if (i3 == i4) {
                                    }
                                    float f7 = ceil;
                                    z5 = z;
                                    textPaint3 = textPaint;
                                    int ceil4 = (int) Math.ceil(f7 + Math.max(0.0f, f));
                                    if (textLayoutBlock.quote) {
                                    }
                                    int i22 = ceil;
                                    if (lineCount2 > 1) {
                                    }
                                    text = textLayoutBlock.languageLayout;
                                    if (text != null) {
                                    }
                                    textLayoutBlock.spoilers.clear();
                                    if (this.isSpoilersRevealed) {
                                    }
                                    i17 = i5;
                                    makeStaticLayout = staticLayout;
                                    i3++;
                                    arrayList = arrayList2;
                                    z = z5;
                                    textPaint = textPaint3;
                                    charSequence7 = charSequence;
                                }
                            }
                            f = lineLeft;
                            float f62 = textLayoutBlock.textLayout.getLineWidth(lineCount2 - 1);
                            if (!textLayoutBlock.quote) {
                                dp4 = AndroidUtilities.dp(32.0f);
                            } else {
                                if (textLayoutBlock.code) {
                                    dp4 = AndroidUtilities.dp(15.0f);
                                }
                                arrayList2 = arrayList;
                                ceil = (int) Math.ceil(f62);
                                if (ceil > i + 80) {
                                    ceil = i;
                                }
                                i4 = size - 1;
                                if (i3 == i4) {
                                    this.lastLineWidth = ceil;
                                }
                                float f72 = ceil;
                                z5 = z;
                                textPaint3 = textPaint;
                                int ceil42 = (int) Math.ceil(f72 + Math.max(0.0f, f));
                                if (textLayoutBlock.quote) {
                                    textLayoutBlock.maxRight = 0.0f;
                                    int i23 = 0;
                                    while (i23 < lineCount2) {
                                        try {
                                            i10 = ceil;
                                            try {
                                                textLayoutBlock.maxRight = Math.max(textLayoutBlock.maxRight, textLayoutBlock.textLayout.getLineRight(i23));
                                            } catch (Exception unused) {
                                                textLayoutBlock.maxRight = this.textWidth;
                                                i23++;
                                                ceil = i10;
                                            }
                                        } catch (Exception unused2) {
                                            i10 = ceil;
                                        }
                                        i23++;
                                        ceil = i10;
                                    }
                                }
                                int i222 = ceil;
                                if (lineCount2 > 1) {
                                    i5 = i17;
                                    int i24 = ceil42;
                                    int i25 = i222;
                                    int i26 = 0;
                                    float f8 = 0.0f;
                                    boolean z10 = false;
                                    float f9 = 0.0f;
                                    CharSequence charSequence8 = charSequence7;
                                    while (i26 < lineCount2) {
                                        int i27 = lineCount2;
                                        try {
                                            f2 = textLayoutBlock.textLayout.getLineWidth(i26);
                                            staticLayout3 = makeStaticLayout;
                                        } catch (Exception unused3) {
                                            staticLayout3 = makeStaticLayout;
                                            f2 = 0.0f;
                                        }
                                        if (textLayoutBlock.quote) {
                                            charSequence2 = charSequence8;
                                            dp5 = AndroidUtilities.dp(32.0f);
                                        } else {
                                            charSequence2 = charSequence8;
                                            if (textLayoutBlock.code) {
                                                dp5 = AndroidUtilities.dp(15.0f);
                                            }
                                            f3 = textLayoutBlock.textLayout.getLineLeft(i26);
                                            if (f2 > i + 20) {
                                                f2 = i;
                                                f3 = 0.0f;
                                            }
                                            if (f3 > 0.0f) {
                                                i8 = i;
                                                if (textLayoutBlock.textLayout.getParagraphDirection(i26) != -1) {
                                                    textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                                    i9 = 1;
                                                    if (z10 && f3 == 0.0f) {
                                                        try {
                                                            if (textLayoutBlock.textLayout.getParagraphDirection(i26) != i9) {
                                                            }
                                                        } catch (Exception unused4) {
                                                        }
                                                        z10 = true;
                                                    }
                                                    f8 = Math.max(f8, f2);
                                                    float f10 = f3 + f2;
                                                    f9 = Math.max(f9, f10);
                                                    i25 = Math.max(i25, (int) Math.ceil(f2));
                                                    i24 = Math.max(i24, (int) Math.ceil(f10));
                                                    i26++;
                                                    lineCount2 = i27;
                                                    makeStaticLayout = staticLayout3;
                                                    charSequence8 = charSequence2;
                                                    i = i8;
                                                }
                                            } else {
                                                i8 = i;
                                            }
                                            this.textXOffset = Math.min(this.textXOffset, f3);
                                            i9 = 1;
                                            textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                            this.hasRtl = true;
                                            if (z10) {
                                                if (textLayoutBlock.textLayout.getParagraphDirection(i26) != i9) {
                                                }
                                                z10 = true;
                                            }
                                            f8 = Math.max(f8, f2);
                                            float f102 = f3 + f2;
                                            f9 = Math.max(f9, f102);
                                            i25 = Math.max(i25, (int) Math.ceil(f2));
                                            i24 = Math.max(i24, (int) Math.ceil(f102));
                                            i26++;
                                            lineCount2 = i27;
                                            makeStaticLayout = staticLayout3;
                                            charSequence8 = charSequence2;
                                            i = i8;
                                        }
                                        f2 += dp5;
                                        f3 = textLayoutBlock.textLayout.getLineLeft(i26);
                                        if (f2 > i + 20) {
                                        }
                                        if (f3 > 0.0f) {
                                        }
                                        this.textXOffset = Math.min(this.textXOffset, f3);
                                        i9 = 1;
                                        textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                        this.hasRtl = true;
                                        if (z10) {
                                        }
                                        f8 = Math.max(f8, f2);
                                        float f1022 = f3 + f2;
                                        f9 = Math.max(f9, f1022);
                                        i25 = Math.max(i25, (int) Math.ceil(f2));
                                        i24 = Math.max(i24, (int) Math.ceil(f1022));
                                        i26++;
                                        lineCount2 = i27;
                                        makeStaticLayout = staticLayout3;
                                        charSequence8 = charSequence2;
                                        i = i8;
                                    }
                                    int i28 = i;
                                    staticLayout = makeStaticLayout;
                                    charSequence = charSequence8;
                                    if (z10) {
                                        if (i3 == i4) {
                                            this.lastLineWidth = ceil42;
                                        }
                                        f8 = f9;
                                    } else if (i3 == i4) {
                                        this.lastLineWidth = i25;
                                    }
                                    this.textWidth = Math.max(this.textWidth, (int) Math.ceil(f8));
                                    ceil42 = i24;
                                    i = i28;
                                } else {
                                    i5 = i17;
                                    int i29 = i;
                                    staticLayout = makeStaticLayout;
                                    charSequence = charSequence7;
                                    if (f > 0.0f) {
                                        float min = Math.min(this.textXOffset, f);
                                        this.textXOffset = min;
                                        i6 = min == 0.0f ? (int) (f72 + f) : i222;
                                        this.hasRtl = size != 1;
                                        textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 1);
                                    } else {
                                        textLayoutBlock.directionFlags = (byte) (textLayoutBlock.directionFlags | 2);
                                        i6 = i222;
                                    }
                                    i = i29;
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
                                i17 = i5;
                                makeStaticLayout = staticLayout;
                                i3++;
                                arrayList = arrayList2;
                                z = z5;
                                textPaint = textPaint3;
                                charSequence7 = charSequence;
                            }
                            f62 += dp4;
                            arrayList2 = arrayList;
                            ceil = (int) Math.ceil(f62);
                            if (ceil > i + 80) {
                            }
                            i4 = size - 1;
                            if (i3 == i4) {
                            }
                            float f722 = ceil;
                            z5 = z;
                            textPaint3 = textPaint;
                            int ceil422 = (int) Math.ceil(f722 + Math.max(0.0f, f));
                            if (textLayoutBlock.quote) {
                            }
                            int i2222 = ceil;
                            if (lineCount2 > 1) {
                            }
                            text = textLayoutBlock.languageLayout;
                            if (text != null) {
                            }
                            textLayoutBlock.spoilers.clear();
                            if (this.isSpoilersRevealed) {
                            }
                            i17 = i5;
                            makeStaticLayout = staticLayout;
                            i3++;
                            arrayList = arrayList2;
                            z = z5;
                            textPaint = textPaint3;
                            charSequence7 = charSequence;
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
                        int i30 = 0;
                        int i31 = 0;
                        while (i30 < ceil3) {
                            int min2 = z2 ? lineCount : Math.min(i16, lineCount - i31);
                            int lineStart2 = makeStaticLayout.getLineStart(i31);
                            int i32 = min2 + i31;
                            int i33 = maxMessageTextWidth;
                            int lineEnd2 = makeStaticLayout.getLineEnd(i32 - 1);
                            if (lineEnd2 >= lineStart2) {
                                arrayList.add(new TextRange(lineStart2, lineEnd2));
                                i31 = i32;
                            }
                            i30++;
                            maxMessageTextWidth = i33;
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
                    int i172 = i2;
                    i3 = 0;
                    CharSequence charSequence72 = spannableStringBuilder;
                    while (i3 < arrayList.size()) {
                    }
                    if (this.hasCode) {
                    }
                    z4 = false;
                    this.hasWideCode = z4;
                    this.factCheckText = null;
                    return;
                }
                i = dp2 + maxMessageTextWidth;
                int lineCount3 = makeStaticLayout.getLineCount();
                int i152 = this.totalAnimatedEmojiCount;
                if (i152 >= 50) {
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
                int i1722 = i2;
                i3 = 0;
                CharSequence charSequence722 = spannableStringBuilder;
                while (i3 < arrayList.size()) {
                }
                if (this.hasCode) {
                }
                z4 = false;
                this.hasWideCode = z4;
                this.factCheckText = null;
                return;
            }
            StaticLayout makeStaticLayout3 = makeStaticLayout(charSequence6, textPaint, maxMessageTextWidth, 1.0f, this.totalAnimatedEmojiCount < 4 ? -1.0f : 0.0f, this.emojiOnlyCount <= 0);
            spannableStringBuilder = charSequence6;
            if (this.isRepostPreview) {
            }
            if (!this.hasSingleQuote) {
            }
            i = dp2 + maxMessageTextWidth;
            int lineCount32 = makeStaticLayout3.getLineCount();
            int i1522 = this.totalAnimatedEmojiCount;
            if (i1522 >= 50) {
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
            int i17222 = i2;
            i3 = 0;
            CharSequence charSequence7222 = spannableStringBuilder;
            while (i3 < arrayList.size()) {
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
        CharSequence charSequence62 = this.messageText;
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

        public void bounceFrom(TextLayoutBlocks textLayoutBlocks) {
            if (textLayoutBlocks == null) {
                return;
            }
            for (int i = 0; i < Math.min(this.textLayoutBlocks.size(), textLayoutBlocks.textLayoutBlocks.size()); i++) {
                this.textLayoutBlocks.get(i).collapsedBounce = textLayoutBlocks.textLayoutBlocks.get(i).collapsedBounce;
            }
        }

        /* JADX WARN: Can't wrap try/catch for region: R(16:210|211|212|213|214|(1:216)(11:246|(1:248)|218|219|220|(1:222)|223|(2:225|(3:227|(5:231|232|(1:237)|234|235)|236))(1:243)|242|(1:241)(6:229|231|232|(0)|234|235)|236)|217|218|219|220|(0)|223|(0)(0)|242|(0)(0)|236) */
        /* JADX WARN: Can't wrap try/catch for region: R(43:117|(1:119)|120|(1:122)(1:355)|123|(1:125)(1:354)|126|(1:128)|(1:130)|(1:353)(1:135)|136|(2:138|(2:(1:336)|337)(1:141))(2:338|(7:340|(1:342)(1:352)|343|(1:345)(1:351)|346|(1:348)(1:350)|349))|142|(3:144|(1:146)(2:330|(1:332)(1:333))|147)(1:334)|148|(1:150)(1:(1:328)(1:329))|151|(3:153|(1:307)(4:159|(1:161)(1:306)|162|163)|164)(3:308|(2:310|311)(6:312|313|314|(1:321)(1:318)|319|320)|277)|165|(1:171)|172|173|174|(1:178)|179|180|181|182|(1:184)|185|(1:187)|188|(3:190|(7:192|193|194|195|196|198|199)|205)|206|(6:208|(16:210|211|212|213|214|(1:216)(11:246|(1:248)|218|219|220|(1:222)|223|(2:225|(3:227|(5:231|232|(1:237)|234|235)|236))(1:243)|242|(1:241)(6:229|231|232|(0)|234|235)|236)|217|218|219|220|(0)|223|(0)(0)|242|(0)(0)|236)|251|252|(2:(1:255)|256)(1:(1:284))|257)(3:285|(5:287|(1:289)(1:296)|290|(1:292)(1:295)|293)(1:297)|294)|258|(3:260|(1:262)(1:264)|263)|265|(1:282)(3:271|(1:273)(3:278|(1:280)|281)|274)|275|276|277|115) */
        /* JADX WARN: Code restructure failed: missing block: B:243:0x04ae, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:246:0x04b2, code lost:
            if (r8 == 0) goto L304;
         */
        /* JADX WARN: Code restructure failed: missing block: B:247:0x04b4, code lost:
            r31.textXOffset = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:248:0x04b7, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r12 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:251:0x04c4, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:252:0x04c5, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r0 = 0.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:287:0x0554, code lost:
            r2 = 0.0f;
         */
        /* JADX WARN: Removed duplicated region for block: B:101:0x01d8  */
        /* JADX WARN: Removed duplicated region for block: B:102:0x01df  */
        /* JADX WARN: Removed duplicated region for block: B:108:0x01f8  */
        /* JADX WARN: Removed duplicated region for block: B:109:0x01fa  */
        /* JADX WARN: Removed duplicated region for block: B:112:0x01ff  */
        /* JADX WARN: Removed duplicated region for block: B:113:0x0201  */
        /* JADX WARN: Removed duplicated region for block: B:136:0x0271  */
        /* JADX WARN: Removed duplicated region for block: B:290:0x055d  */
        /* JADX WARN: Removed duplicated region for block: B:293:0x0564  */
        /* JADX WARN: Removed duplicated region for block: B:297:0x0579  */
        /* JADX WARN: Removed duplicated region for block: B:300:0x058e  */
        /* JADX WARN: Removed duplicated region for block: B:394:0x059c A[ADDED_TO_REGION, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:396:0x059c A[SYNTHETIC] */
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
            int dp2;
            boolean z2;
            ArrayList arrayList;
            int i3;
            TextPaint textPaint2;
            int dp3;
            CharSequence charSequence3;
            int i4;
            ArrayList arrayList2;
            boolean z3;
            MessageObject messageObject2;
            SpannableString valueOf;
            int i5;
            int dp4;
            StaticLayout staticLayout2;
            boolean z4;
            float f;
            float f2;
            float f3;
            int i6;
            int i7;
            int i8;
            SpannableString spannableString;
            TextPaint textPaint3;
            int i9;
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
                } else if (this.hasSingleCode) {
                    dp = AndroidUtilities.dp(15.0f);
                } else {
                    i2 = i;
                    StaticLayout makeStaticLayout = MessageObject.makeStaticLayout(charSequence, textPaint, i2, 1.0f, 0.0f, false);
                    SpannableStringBuilder spannableStringBuilder = charSequence4;
                    if (messageObject3 != null) {
                        spannableStringBuilder = charSequence4;
                        if (messageObject3.isRepostPreview) {
                            if (messageObject3.type != 0) {
                                i9 = messageObject.hasValidGroupId() ? 7 : 12;
                            } else {
                                i9 = 22;
                            }
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
                        dp2 = i2 + AndroidUtilities.dp(32.0f);
                    } else {
                        dp2 = this.hasSingleCode ? i2 + AndroidUtilities.dp(15.0f) : i2;
                    }
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
                                int dp5 = AndroidUtilities.dp(6.0f);
                                textLayoutBlock.padBottom = dp5;
                                textLayoutBlock.padTop = dp5;
                            } else {
                                textLayoutBlock.padTop = AndroidUtilities.dp(z9 ? 8.0f : 6.0f);
                                textLayoutBlock.padBottom = AndroidUtilities.dp(7.0f);
                            }
                        } else if (textLayoutBlock.code) {
                            textLayoutBlock.layoutCode(textRange.language, textRange.end - textRange.start, z);
                            textLayoutBlock.padTop = AndroidUtilities.dp(4.0f) + textLayoutBlock.languageHeight + (textLayoutBlock.first ? 0 : AndroidUtilities.dp(5.0f));
                            textLayoutBlock.padBottom = AndroidUtilities.dp(4.0f) + (textLayoutBlock.last ? 0 : AndroidUtilities.dp(7.0f)) + (textLayoutBlock.hasCodeCopyButton ? AndroidUtilities.dp(38.0f) : 0);
                        }
                        boolean z10 = textLayoutBlock.code;
                        if (z10) {
                            int i17 = textRange.end - textRange.start;
                            if (i17 > 220) {
                                textPaint3 = Theme.chat_msgTextCode3Paint;
                            } else if (i17 > 80) {
                                textPaint3 = Theme.chat_msgTextCode2Paint;
                            } else {
                                textPaint3 = Theme.chat_msgTextCodePaint;
                            }
                            textPaint2 = textPaint3;
                        } else {
                            textPaint2 = textPaint;
                        }
                        if (textLayoutBlock.quote) {
                            dp3 = dp2 - AndroidUtilities.dp(f4);
                        } else {
                            dp3 = z10 ? dp2 - AndroidUtilities.dp(15.0f) : dp2;
                        }
                        if (size == 1) {
                            if (textLayoutBlock.code && !textLayoutBlock.quote && (staticLayout3.getText() instanceof Spannable)) {
                                if (!TextUtils.isEmpty(textRange.language)) {
                                    spannableString = CodeHighlighting.getHighlighted(charSequence2.subSequence(textRange.start, textRange.end).toString(), textRange.language);
                                } else {
                                    spannableString = new SpannableString(charSequence2.subSequence(textRange.start, textRange.end));
                                }
                                SpannableString spannableString2 = spannableString;
                                textLayoutBlock.originalWidth = dp3;
                                staticLayout3 = MessageObject.makeStaticLayout(spannableString2, textPaint2, dp3, 1.0f, 0.0f, false);
                                i16 = dp3;
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
                                i4 = dp2;
                                arrayList2 = arrayList;
                                z3 = z;
                                messageObject2 = messageObject3;
                            } else {
                                textLayoutBlock.charactersOffset = i18;
                                textLayoutBlock.charactersEnd = i19;
                                try {
                                    if (textLayoutBlock.code && !textLayoutBlock.quote) {
                                        valueOf = CodeHighlighting.getHighlighted(charSequence2.subSequence(i18, i19).toString(), textRange.language);
                                    } else {
                                        valueOf = SpannableString.valueOf(charSequence2.subSequence(i18, i19));
                                    }
                                    SpannableString spannableString3 = valueOf;
                                    textLayoutBlock.originalWidth = dp3;
                                    StaticLayout makeStaticLayout2 = MessageObject.makeStaticLayout(spannableString3, textPaint2, dp3, 1.0f, 0.0f, false);
                                    textLayoutBlock.textLayout = makeStaticLayout2;
                                    textLayoutBlock.height = makeStaticLayout2.getHeight();
                                    textLayoutBlock.collapsedHeight = (int) Math.min(textPaint.getTextSize() * 1.4f * 3.0f, textLayoutBlock.height);
                                } catch (Exception e2) {
                                    charSequence3 = charSequence2;
                                    i4 = dp2;
                                    arrayList2 = arrayList;
                                    z3 = z;
                                    messageObject2 = messageObject3;
                                    FileLog.e(e2);
                                }
                            }
                            i3++;
                            dp2 = i4;
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
                        ceil3 = ceil3 > dp2 + 80 ? dp2 : ceil3;
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
                                    if (f > dp2 + 20) {
                                        f = dp2;
                                        f3 = 0.0f;
                                    }
                                    if (f3 > 0.0f) {
                                        i6 = dp2;
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
                                            dp2 = i6;
                                        }
                                    } else {
                                        i6 = dp2;
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
                                    dp2 = i6;
                                }
                                f += f2;
                                f3 = textLayoutBlock.textLayout.getLineLeft(i25);
                                if (f > dp2 + 20) {
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
                                dp2 = i6;
                            }
                            int i28 = dp2;
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
                            int i29 = dp2;
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
                            if (textLayoutBlock.quote) {
                                dp4 = ceil4 - AndroidUtilities.dp(32.0f);
                            } else {
                                dp4 = textLayoutBlock.code ? ceil4 - AndroidUtilities.dp(15.0f) : ceil4;
                            }
                            SpoilerEffect.addSpoilers(null, textLayoutBlock.textLayout, -1, dp4, null, textLayoutBlock.spoilers);
                        }
                        staticLayout3 = staticLayout4;
                        i16 = i21;
                        i3++;
                        dp2 = i4;
                        messageObject3 = messageObject2;
                        charSequence2 = charSequence3;
                        arrayList = arrayList2;
                        z = z3;
                        f4 = 32.0f;
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
                this.isOutOwnerCached = Boolean.valueOf(z);
                return z;
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
            if ((tLRPC$Peer5 instanceof TLRPC$TL_peerUser) || ((tLRPC$Peer5 instanceof TLRPC$TL_peerChannel) && !ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat))) {
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
                        this.isOutOwnerCached = Boolean.valueOf(z);
                        return z;
                    } else {
                        TLRPC$Peer tLRPC$Peer7 = this.messageOwner.fwd_from.saved_from_peer;
                        if (tLRPC$Peer7 != null && tLRPC$Peer7.user_id != clientUserId) {
                            z = false;
                        }
                        this.isOutOwnerCached = Boolean.valueOf(z);
                        return z;
                    }
                }
            }
        }
        this.isOutOwnerCached = Boolean.FALSE;
        return false;
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

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needDrawAvatarInternal() {
        if (this.isRepostPreview || this.isSaved || this.forceAvatar || this.customAvatarDrawable != null || this.searchType != 0) {
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

    public static String getPeerObjectName(TLObject tLObject) {
        if (tLObject instanceof TLRPC$User) {
            return UserObject.getUserName((TLRPC$User) tLObject);
        }
        if (tLObject instanceof TLRPC$Chat) {
            return ((TLRPC$Chat) tLObject).title;
        }
        return "DELETED";
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
        return getMediaSize(getMedia(tLRPC$Message));
    }

    public static long getMediaSize(TLRPC$MessageMedia tLRPC$MessageMedia) {
        TLRPC$Document tLRPC$Document;
        TLRPC$WebPage tLRPC$WebPage;
        if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) && (tLRPC$WebPage = tLRPC$MessageMedia.webpage) != null) {
            tLRPC$Document = tLRPC$WebPage.document;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
            tLRPC$Document = tLRPC$MessageMedia.game.document;
        } else {
            tLRPC$Document = tLRPC$MessageMedia != null ? tLRPC$MessageMedia.document : null;
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
        if ((tLRPC$Message == null || tLRPC$Message.media == null || !((isVoiceDocument(getDocument(tLRPC$Message)) || isRoundVideoMessage(tLRPC$Message)) && tLRPC$Message.media.ttl_seconds == Integer.MAX_VALUE)) && !(getMedia(tLRPC$Message) instanceof TLRPC$TL_messageMediaPaidMedia)) {
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

    public TLRPC$Document getDocument() {
        TLRPC$Document tLRPC$Document = this.emojiAnimatedSticker;
        return tLRPC$Document != null ? tLRPC$Document : getDocument(this.messageOwner);
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
            return isGifDocument(getMedia(tLRPC$Message).document, (tLRPC$Message.grouped_id > 0L ? 1 : (tLRPC$Message.grouped_id == 0L ? 0 : -1)) != 0);
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
                            TLRPC$TL_stickerPack tLRPC$TL_stickerPack = stickerSet.packs.get(i2);
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
            int textHeight = textHeight();
            if ((getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage)) {
                i4 = AndroidUtilities.dp(100.0f);
            }
            int i5 = textHeight + i4;
            return isReply() ? i5 + AndroidUtilities.dp(42.0f) : i5;
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
            if (i3 == 11 || i3 == 18 || i3 == 30 || i3 == 25 || i3 == 21) {
                return AndroidUtilities.dp(50.0f);
            }
            if (i3 == 5) {
                return AndroidUtilities.roundMessageSize;
            }
            if (i3 == 19) {
                return textHeight() + AndroidUtilities.dp(30.0f);
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
                    for (int i6 = 0; i6 < size; i6++) {
                        TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i6);
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
            int i7 = (int) (min * 0.7f);
            int dp = AndroidUtilities.dp(100.0f) + i7;
            if (i7 > AndroidUtilities.getPhotoSize()) {
                i7 = AndroidUtilities.getPhotoSize();
            }
            if (dp > AndroidUtilities.getPhotoSize()) {
                dp = AndroidUtilities.getPhotoSize();
            }
            if (FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
                int i8 = (int) (closestPhotoSizeWithSize.h / (closestPhotoSizeWithSize.w / i7));
                if (i8 == 0) {
                    i8 = AndroidUtilities.dp(100.0f);
                }
                if (i8 <= dp) {
                    dp = i8 < AndroidUtilities.dp(120.0f) ? AndroidUtilities.dp(120.0f) : i8;
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
            if (TextUtils.isEmpty(tLRPC$TL_messageMediaDice.emoticon)) {
                return "🎲";
            }
            return tLRPC$TL_messageMediaDice.emoticon.replace("️", "");
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

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0038, code lost:
        if (r3.round_message != false) goto L10;
     */
    /* JADX WARN: Removed duplicated region for block: B:23:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0150 A[SYNTHETIC] */
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
                        return (TextUtils.isEmpty(str2) && z) ? LocaleController.getString(R.string.AudioUnknownArtist) : str2;
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
                            return LocaleController.getString(R.string.FromYou);
                        }
                        return null;
                    }
                }
                z2 = true;
                if (!z2) {
                }
            }
        }
        return LocaleController.getString(R.string.AudioUnknownArtist);
    }

    public TLRPC$InputStickerSet getInputStickerSet() {
        return getInputStickerSet(this.messageOwner);
    }

    public boolean isForwarded() {
        return isForwardedMessage(this.messageOwner);
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
            TLRPC$TL_messageExtendedMediaPreview tLRPC$TL_messageExtendedMediaPreview = (TLRPC$TL_messageExtendedMediaPreview) this.messageOwner.media.extended_media.get(0);
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

    public boolean isPaidReactionChosen() {
        if (this.messageOwner.reactions == null) {
            return false;
        }
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (this.messageOwner.reactions.results.get(i).reaction instanceof TLRPC$TL_reactionPaid) {
                return this.messageOwner.reactions.results.get(i).chosen;
            }
        }
        return false;
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

    public Boolean isMyPaidReactionAnonymous() {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList<TLRPC$MessageReactor> arrayList;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (tLRPC$TL_messageReactions = tLRPC$Message.reactions) == null || (arrayList = tLRPC$TL_messageReactions.top_reactors) == null) {
            return null;
        }
        Iterator<TLRPC$MessageReactor> it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC$MessageReactor next = it.next();
            if (next != null && next.my) {
                return Boolean.valueOf(next.anonymous);
            }
        }
        return null;
    }

    public static Boolean isMyPaidReactionAnonymous(TLRPC$MessageReactions tLRPC$MessageReactions) {
        ArrayList<TLRPC$MessageReactor> arrayList;
        if (tLRPC$MessageReactions == null || (arrayList = tLRPC$MessageReactions.top_reactors) == null) {
            return null;
        }
        Iterator<TLRPC$MessageReactor> it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC$MessageReactor next = it.next();
            if (next != null && next.my) {
                return Boolean.valueOf(next.anonymous);
            }
        }
        return null;
    }

    public void setMyPaidReactionAnonymous(boolean z) {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList<TLRPC$MessageReactor> arrayList;
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || (tLRPC$TL_messageReactions = tLRPC$Message.reactions) == null || (arrayList = tLRPC$TL_messageReactions.top_reactors) == null) {
            return;
        }
        Iterator<TLRPC$MessageReactor> it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC$MessageReactor next = it.next();
            if (next != null && next.my) {
                next.anonymous = z;
            }
        }
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
            if (this.messageOwner.reactions.results.get(i).reaction instanceof TLRPC$TL_reactionPaid) {
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
            if (this.messageOwner.reactions.results.get(i).reaction instanceof TLRPC$TL_reactionPaid) {
                tLRPC$ReactionCount = this.messageOwner.reactions.results.get(i);
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

    public static void addPaidReactions(int i, TLRPC$MessageReactions tLRPC$MessageReactions, int i2, boolean z, boolean z2) {
        TLRPC$MessageReactor tLRPC$MessageReactor = null;
        TLRPC$ReactionCount tLRPC$ReactionCount = null;
        for (int i3 = 0; i3 < tLRPC$MessageReactions.results.size(); i3++) {
            if (tLRPC$MessageReactions.results.get(i3).reaction instanceof TLRPC$TL_reactionPaid) {
                tLRPC$ReactionCount = tLRPC$MessageReactions.results.get(i3);
            }
        }
        int i4 = 0;
        while (true) {
            if (i4 >= tLRPC$MessageReactions.top_reactors.size()) {
                break;
            } else if (tLRPC$MessageReactions.top_reactors.get(i4).my) {
                tLRPC$MessageReactor = tLRPC$MessageReactions.top_reactors.get(i4);
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

    public boolean selectReaction(ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
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
            TLRPC$ReactionCount tLRPC$ReactionCount2 = this.messageOwner.reactions.results.get(i3);
            if (tLRPC$ReactionCount2.chosen && !(tLRPC$ReactionCount2.reaction instanceof TLRPC$TL_reactionPaid)) {
                arrayList.add(tLRPC$ReactionCount2);
                int i4 = tLRPC$ReactionCount2.chosen_order;
                if (i4 > i2) {
                    i2 = i4;
                }
            }
            TLRPC$Reaction tLRPC$Reaction = this.messageOwner.reactions.results.get(i3).reaction;
            if (tLRPC$Reaction instanceof TLRPC$TL_reactionEmoji) {
                String str = visibleReaction.emojicon;
                if (str != null) {
                    if (((TLRPC$TL_reactionEmoji) tLRPC$Reaction).emoticon.equals(str)) {
                        tLRPC$ReactionCount = this.messageOwner.reactions.results.get(i3);
                    }
                }
            }
            if (tLRPC$Reaction instanceof TLRPC$TL_reactionCustomEmoji) {
                long j = visibleReaction.documentId;
                if (j != 0 && ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction).document_id == j) {
                    tLRPC$ReactionCount = this.messageOwner.reactions.results.get(i3);
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
                    if (getPeerId(this.messageOwner.reactions.recent_reactions.get(i10).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId() && ReactionsUtils.compare(this.messageOwner.reactions.recent_reactions.get(i10).reaction, visibleReaction)) {
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
                Iterator<TLRPC$ReactionCount> it = tLRPC$TL_messageReactions.results.iterator();
                i = 0;
                while (it.hasNext()) {
                    if (!(it.next().reaction instanceof TLRPC$TL_reactionPaid)) {
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
            if (tLRPC$Message5.isThreadMessage && tLRPC$Message5.fwd_from != null) {
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

    public boolean isBotPreview() {
        return this.storyItem instanceof StoriesController.BotPreview;
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
        return i == 30 || i == 18 || i == 25;
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
            if (user != null) {
                return AndroidUtilities.removeDiacritics(UserObject.getUserName(user));
            }
            return "";
        }
        TLRPC$Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-j));
        if (chat != null) {
            return new SpannableStringBuilder(ChatObject.isChannelAndNotMegaGroup(chat) ? channelSpan() : groupSpan()).append((CharSequence) " ").append((CharSequence) AndroidUtilities.removeDiacritics(chat.title));
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
        if (!z || isGiveawayOrGiveawayResults() || tLRPC$WebPage == null) {
            return false;
        }
        if (tLRPC$WebPage.photo != null || isVideoDocument(tLRPC$WebPage.document)) {
            return ((TextUtils.isEmpty(tLRPC$WebPage.description) && TextUtils.isEmpty(tLRPC$WebPage.title)) || isSponsored() || "telegram_megagroup".equals(str) || "telegram_background".equals(str) || "telegram_voicechat".equals(str) || "telegram_videochat".equals(str) || "telegram_livestream".equals(str) || "telegram_user".equals(str) || "telegram_story".equals(str) || "telegram_channel_boost".equals(str) || "telegram_group_boost".equals(str) || "telegram_chat".equals(str)) ? false : true;
        }
        return false;
    }

    public boolean isLinkMediaSmall() {
        TLRPC$WebPage tLRPC$WebPage = (!this.isRestrictedMessage && (getMedia(this.messageOwner) instanceof TLRPC$TL_messageMediaWebPage) && (getMedia(this.messageOwner).webpage instanceof TLRPC$TL_webPage)) ? getMedia(this.messageOwner).webpage : null;
        String str = tLRPC$WebPage != null ? tLRPC$WebPage.type : null;
        return !(tLRPC$WebPage != null && TextUtils.isEmpty(tLRPC$WebPage.description) && TextUtils.isEmpty(tLRPC$WebPage.title)) && ("app".equals(str) || "profile".equals(str) || "article".equals(str) || "telegram_bot".equals(str) || "telegram_user".equals(str) || "telegram_channel".equals(str) || "telegram_megagroup".equals(str) || "telegram_voicechat".equals(str) || "telegram_videochat".equals(str) || "telegram_livestream".equals(str) || "telegram_channel_boost".equals(str) || "telegram_group_boost".equals(str) || "telegram_chat".equals(str));
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
        if (findReply != null) {
            return findReply.name;
        }
        return "";
    }

    public static boolean isQuickReply(TLRPC$Message tLRPC$Message) {
        return (tLRPC$Message == null || ((tLRPC$Message.flags & 1073741824) == 0 && tLRPC$Message.quick_reply_shortcut == null)) ? false : true;
    }

    public boolean isQuickReply() {
        return isQuickReply(this.messageOwner);
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

    public boolean isFactCheckable() {
        int i;
        return getId() >= 0 && !isSponsored() && ((i = this.type) == 0 || i == 2 || i == 1 || i == 3 || i == 8 || i == 9);
    }

    public boolean hasEntitiesFromServer() {
        TLRPC$Message tLRPC$Message = this.messageOwner;
        if (tLRPC$Message == null || tLRPC$Message.entities == null) {
            return false;
        }
        for (int i = 0; i < this.messageOwner.entities.size(); i++) {
            TLRPC$MessageEntity tLRPC$MessageEntity = this.messageOwner.entities.get(i);
            if ((tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPhone) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBankCard)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlbumSingle() {
        return getMedia(this) instanceof TLRPC$TL_messageMediaPaidMedia;
    }
}
