package org.telegram.messenger;

import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.view.View;
import androidx.collection.LongSparseArray;
import androidx.core.net.MailTo;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.microsoft.appcenter.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
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
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes4.dex */
public class MessageObject {
    private static final int LINES_PER_BLOCK = 10;
    public static final int MESSAGE_SEND_STATE_EDITING = 3;
    public static final int MESSAGE_SEND_STATE_SENDING = 1;
    public static final int MESSAGE_SEND_STATE_SEND_ERROR = 2;
    public static final int MESSAGE_SEND_STATE_SENT = 0;
    public static final int POSITION_FLAG_BOTTOM = 8;
    public static final int POSITION_FLAG_LEFT = 1;
    public static final int POSITION_FLAG_RIGHT = 2;
    public static final int POSITION_FLAG_TOP = 4;
    public static final int TYPE_ANIMATED_STICKER = 15;
    public static final int TYPE_GEO = 4;
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_POLL = 17;
    public static final int TYPE_ROUND_VIDEO = 5;
    public static final int TYPE_STICKER = 13;
    public static final int TYPE_VIDEO = 3;
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
    public ArrayList<TLRPC.TL_pollAnswer> checkedVotes;
    public int contentType;
    public int currentAccount;
    public TLRPC.TL_channelAdminLogEvent currentEvent;
    public Drawable customAvatarDrawable;
    public String customName;
    public String customReplyName;
    public String dateKey;
    public boolean deleted;
    public CharSequence editingMessage;
    public ArrayList<TLRPC.MessageEntity> editingMessageEntities;
    public boolean editingMessageSearchWebPage;
    public TLRPC.Document emojiAnimatedSticker;
    public String emojiAnimatedStickerColor;
    private int emojiOnlyCount;
    public long eventId;
    public boolean forcePlayEffect;
    public float forceSeekTo;
    public boolean forceUpdate;
    private float generatedWithDensity;
    private int generatedWithMinSize;
    public float gifState;
    public boolean hadAnimationNotReadyLoading;
    public boolean hasRtl;
    public boolean hideSendersName;
    public ArrayList<String> highlightedWords;
    public boolean isDateObject;
    public boolean isDownloadingFile;
    public boolean isReactionPush;
    public boolean isRestrictedMessage;
    private int isRoundVideoCached;
    public boolean isSpoilersRevealed;
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
    public TLRPC.Message messageOwner;
    public CharSequence messageText;
    public String messageTrimmedToHighlight;
    public String monthKey;
    public int parentWidth;
    public SvgHelper.SvgDrawable pathThumb;
    public ArrayList<TLRPC.PhotoSize> photoThumbs;
    public ArrayList<TLRPC.PhotoSize> photoThumbs2;
    public TLObject photoThumbsObject;
    public TLObject photoThumbsObject2;
    public long pollLastCheckTime;
    public boolean pollVisibleOnScreen;
    public boolean preview;
    public String previousAttachPath;
    public TLRPC.MessageMedia previousMedia;
    public String previousMessage;
    public ArrayList<TLRPC.MessageEntity> previousMessageEntities;
    public boolean putInDownloadsStore;
    public boolean reactionsChanged;
    public long reactionsLastCheckTime;
    public MessageObject replyMessageObject;
    public boolean resendAsIs;
    public boolean scheduled;
    public SendAnimationData sendAnimationData;
    public TLRPC.Peer sendAsPeer;
    public boolean shouldRemoveVideoEditedInfo;
    public int sponsoredChannelPost;
    public TLRPC.ChatInvite sponsoredChatInvite;
    public String sponsoredChatInviteHash;
    public byte[] sponsoredId;
    public int stableId;
    public BitmapDrawable strippedThumb;
    public int textHeight;
    public ArrayList<TextLayoutBlock> textLayoutBlocks;
    public int textWidth;
    public float textXOffset;
    public int type;
    public boolean useCustomPhoto;
    public CharSequence vCardData;
    public VideoEditedInfo videoEditedInfo;
    public AtomicReference<WeakReference<View>> viewRef;
    public boolean viewsReloaded;
    public int wantedBotKeyboardWidth;
    public boolean wasJustSent;
    public boolean wasUnread;

    /* loaded from: classes4.dex */
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

    public static boolean hasUnreadReactions(TLRPC.Message message) {
        if (message == null) {
            return false;
        }
        return hasUnreadReactions(message.reactions);
    }

    public static boolean hasUnreadReactions(TLRPC.TL_messageReactions reactions) {
        if (reactions == null) {
            return false;
        }
        for (int i = 0; i < reactions.recent_reactions.size(); i++) {
            if (reactions.recent_reactions.get(i).unread) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPremiumSticker(TLRPC.Document document) {
        if (document == null || document.thumbs == null) {
            return false;
        }
        for (int i = 0; i < document.video_thumbs.size(); i++) {
            if ("f".equals(document.video_thumbs.get(i).type)) {
                return true;
            }
        }
        return false;
    }

    public int getEmojiOnlyCount() {
        return this.emojiOnlyCount;
    }

    public boolean shouldDrawReactionsInLayout() {
        return getDialogId() < 0;
    }

    public TLRPC.TL_messagePeerReaction getRandomUnreadReaction() {
        if (this.messageOwner.reactions == null || this.messageOwner.reactions.recent_reactions == null || this.messageOwner.reactions.recent_reactions.isEmpty()) {
            return null;
        }
        return this.messageOwner.reactions.recent_reactions.get(0);
    }

    public void markReactionsAsRead() {
        if (this.messageOwner.reactions == null || this.messageOwner.reactions.recent_reactions == null) {
            return;
        }
        boolean changed = false;
        for (int i = 0; i < this.messageOwner.reactions.recent_reactions.size(); i++) {
            if (this.messageOwner.reactions.recent_reactions.get(i).unread) {
                this.messageOwner.reactions.recent_reactions.get(i).unread = false;
                changed = true;
            }
        }
        if (changed) {
            MessagesStorage.getInstance(this.currentAccount).markMessageReactionsAsRead(this.messageOwner.dialog_id, this.messageOwner.id, true);
        }
    }

    public boolean isPremiumSticker() {
        if (this.messageOwner.media != null && this.messageOwner.media.nopremium) {
            return false;
        }
        return isPremiumSticker(getDocument());
    }

    public TLRPC.VideoSize getPremiumStickerAnimation() {
        return getPremiumStickerAnimation(getDocument());
    }

    public static TLRPC.VideoSize getPremiumStickerAnimation(TLRPC.Document document) {
        if (document == null || document.thumbs == null) {
            return null;
        }
        for (int i = 0; i < document.video_thumbs.size(); i++) {
            if ("f".equals(document.video_thumbs.get(i).type)) {
                return document.video_thumbs.get(i);
            }
        }
        return null;
    }

    /* loaded from: classes4.dex */
    public static class VCardData {
        private String company;
        private ArrayList<String> emails = new ArrayList<>();
        private ArrayList<String> phones = new ArrayList<>();

        public static CharSequence parse(String data) {
            boolean finished;
            byte[] bytes;
            try {
                BufferedReader bufferedReader = new BufferedReader(new StringReader(data));
                String pendingLine = null;
                boolean finished2 = false;
                VCardData currentData = null;
                while (true) {
                    String originalLine = bufferedReader.readLine();
                    String line = originalLine;
                    if (originalLine != null) {
                        if (!originalLine.startsWith("PHOTO")) {
                            if (originalLine.indexOf(58) >= 0) {
                                if (originalLine.startsWith("BEGIN:VCARD")) {
                                    currentData = new VCardData();
                                } else if (originalLine.startsWith("END:VCARD") && currentData != null) {
                                    finished2 = true;
                                }
                            }
                            if (pendingLine != null) {
                                line = pendingLine + line;
                                pendingLine = null;
                            }
                            int i = 0;
                            if (line.contains("=QUOTED-PRINTABLE") && line.endsWith("=")) {
                                pendingLine = line.substring(0, line.length() - 1);
                            } else {
                                int idx = line.indexOf(Constants.COMMON_SCHEMA_PREFIX_SEPARATOR);
                                String[] args = idx >= 0 ? new String[]{line.substring(0, idx), line.substring(idx + 1).trim()} : new String[]{line.trim()};
                                if (args.length < 2) {
                                    finished = finished2;
                                } else if (currentData == null) {
                                    finished = finished2;
                                } else if (args[0].startsWith("ORG")) {
                                    String nameEncoding = null;
                                    String nameCharset = null;
                                    String[] params = args[0].split(";");
                                    int length = params.length;
                                    while (i < length) {
                                        String param = params[i];
                                        String[] args2 = param.split("=");
                                        int idx2 = idx;
                                        boolean args22 = finished2;
                                        if (args2.length == 2) {
                                            if (args2[0].equals("CHARSET")) {
                                                nameCharset = args2[1];
                                            } else if (args2[0].equals("ENCODING")) {
                                                nameEncoding = args2[1];
                                            }
                                        }
                                        i++;
                                        idx = idx2;
                                        finished2 = args22;
                                    }
                                    finished = finished2;
                                    currentData.company = args[1];
                                    if (nameEncoding != null && nameEncoding.equalsIgnoreCase("QUOTED-PRINTABLE") && (bytes = AndroidUtilities.decodeQuotedPrintable(AndroidUtilities.getStringBytes(currentData.company))) != null && bytes.length != 0) {
                                        currentData.company = new String(bytes, nameCharset);
                                    }
                                    currentData.company = currentData.company.replace(';', ' ');
                                } else {
                                    finished = finished2;
                                    if (args[0].startsWith("TEL")) {
                                        if (args[1].length() > 0) {
                                            currentData.phones.add(args[1]);
                                        }
                                    } else if (args[0].startsWith("EMAIL")) {
                                        String email = args[1];
                                        if (email.length() > 0) {
                                            currentData.emails.add(email);
                                        }
                                    }
                                }
                                finished2 = finished;
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
                if (finished2) {
                    StringBuilder result = new StringBuilder();
                    for (int a = 0; a < currentData.phones.size(); a++) {
                        if (result.length() > 0) {
                            result.append('\n');
                        }
                        String phone = currentData.phones.get(a);
                        if (!phone.contains("#") && !phone.contains("*")) {
                            result.append(PhoneFormat.getInstance().format(phone));
                        }
                        result.append(phone);
                    }
                    for (int a2 = 0; a2 < currentData.emails.size(); a2++) {
                        if (result.length() > 0) {
                            result.append('\n');
                        }
                        result.append(PhoneFormat.getInstance().format(currentData.emails.get(a2)));
                    }
                    if (!TextUtils.isEmpty(currentData.company)) {
                        if (result.length() > 0) {
                            result.append('\n');
                        }
                        result.append(currentData.company);
                    }
                    return result;
                }
                return null;
            } catch (Throwable th) {
                return null;
            }
        }
    }

    /* loaded from: classes4.dex */
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

    /* loaded from: classes4.dex */
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

        public void set(int minX, int maxX, int minY, int maxY, int w, float h, int flags) {
            this.minX = (byte) minX;
            this.maxX = (byte) maxX;
            this.minY = (byte) minY;
            this.maxY = (byte) maxY;
            this.pw = w;
            this.spanSize = w;
            this.ph = h;
            this.flags = (byte) flags;
        }
    }

    /* loaded from: classes4.dex */
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

        /* loaded from: classes4.dex */
        public static class MessageGroupedLayoutAttempt {
            public float[] heights;
            public int[] lineCounts;

            public MessageGroupedLayoutAttempt(int i1, int i2, float f1, float f2) {
                this.lineCounts = new int[]{i1, i2};
                this.heights = new float[]{f1, f2};
            }

            public MessageGroupedLayoutAttempt(int i1, int i2, int i3, float f1, float f2, float f3) {
                this.lineCounts = new int[]{i1, i2, i3};
                this.heights = new float[]{f1, f2, f3};
            }

            public MessageGroupedLayoutAttempt(int i1, int i2, int i3, int i4, float f1, float f2, float f3, float f4) {
                this.lineCounts = new int[]{i1, i2, i3, i4};
                this.heights = new float[]{f1, f2, f3, f4};
            }
        }

        private float multiHeight(float[] array, int start, int end) {
            float sum = 0.0f;
            for (int a = start; a < end; a++) {
                sum += array[a];
            }
            int a2 = this.maxSizeWidth;
            return a2 / sum;
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x0083, code lost:
            if ((r12.messageOwner.media instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) == false) goto L28;
         */
        /* JADX WARN: Removed duplicated region for block: B:248:0x08f3  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            boolean isOut;
            int firstSpanAdditionalSize;
            int maxX;
            int count;
            int a;
            float averageAspectRatio;
            int count2;
            int maxX2;
            float maxSizeHeight;
            int minHeight;
            float maxHeight;
            int count3;
            int paddingsWidth;
            float maxSizeHeight2;
            int i;
            boolean z;
            float maxSizeHeight3;
            boolean needShare;
            this.posArray.clear();
            this.positions.clear();
            this.maxSizeWidth = 800;
            int firstSpanAdditionalSize2 = 200;
            int count4 = this.messages.size();
            if (count4 <= 1) {
                return;
            }
            float maxSizeHeight4 = 814.0f;
            StringBuilder proportions = new StringBuilder();
            float averageAspectRatio2 = 1.0f;
            boolean isOut2 = false;
            boolean forceCalc = false;
            boolean needShare2 = false;
            this.hasSibling = false;
            this.hasCaption = false;
            int a2 = 0;
            while (a2 < count4) {
                MessageObject messageObject = this.messages.get(a2);
                if (a2 != 0) {
                    maxSizeHeight3 = maxSizeHeight4;
                } else {
                    isOut2 = messageObject.isOutOwner();
                    if (!isOut2) {
                        if (messageObject.messageOwner.fwd_from != null && messageObject.messageOwner.fwd_from.saved_from_peer != null) {
                            maxSizeHeight3 = maxSizeHeight4;
                        } else if (messageObject.messageOwner.from_id instanceof TLRPC.TL_peerUser) {
                            maxSizeHeight3 = maxSizeHeight4;
                            if (messageObject.messageOwner.peer_id.channel_id == 0) {
                                if (messageObject.messageOwner.peer_id.chat_id == 0) {
                                    if (!(messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGame)) {
                                    }
                                }
                            }
                        }
                        needShare = true;
                        if (!messageObject.isMusic() || messageObject.isDocument()) {
                            this.isDocuments = true;
                        }
                        needShare2 = needShare;
                    }
                    maxSizeHeight3 = maxSizeHeight4;
                    needShare = false;
                    if (!messageObject.isMusic()) {
                    }
                    this.isDocuments = true;
                    needShare2 = needShare;
                }
                TLRPC.PhotoSize photoSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                GroupedMessagePosition position = new GroupedMessagePosition();
                position.last = a2 == count4 + (-1);
                position.aspectRatio = photoSize == null ? 1.0f : photoSize.w / photoSize.h;
                if (position.aspectRatio > 1.2f) {
                    proportions.append("w");
                } else if (position.aspectRatio < 0.8f) {
                    proportions.append("n");
                } else {
                    proportions.append("q");
                }
                averageAspectRatio2 += position.aspectRatio;
                if (position.aspectRatio > 2.0f) {
                    forceCalc = true;
                }
                this.positions.put(messageObject, position);
                this.posArray.add(position);
                if (messageObject.caption != null) {
                    this.hasCaption = true;
                }
                a2++;
                maxSizeHeight4 = maxSizeHeight3;
            }
            float maxSizeHeight5 = maxSizeHeight4;
            if (this.isDocuments) {
                for (int a3 = 0; a3 < count4; a3++) {
                    GroupedMessagePosition pos = this.posArray.get(a3);
                    pos.flags |= 3;
                    if (a3 == 0) {
                        pos.flags |= 4;
                        z = true;
                    } else if (a3 != count4 - 1) {
                        z = true;
                    } else {
                        pos.flags |= 8;
                        z = true;
                        pos.last = true;
                    }
                    pos.edge = z;
                    pos.aspectRatio = 1.0f;
                    pos.minX = (byte) 0;
                    pos.maxX = (byte) 0;
                    pos.minY = (byte) a3;
                    pos.maxY = (byte) a3;
                    pos.spanSize = 1000;
                    pos.pw = this.maxSizeWidth;
                    pos.ph = 100.0f;
                }
                return;
            }
            if (needShare2) {
                this.maxSizeWidth -= 50;
                firstSpanAdditionalSize2 = 200 + 50;
            }
            int minHeight2 = AndroidUtilities.dp(120.0f);
            int minWidth = (int) (AndroidUtilities.dp(120.0f) / (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) / this.maxSizeWidth));
            int i2 = this.maxSizeWidth;
            int paddingsWidth2 = (int) (AndroidUtilities.dp(40.0f) / (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) / i2));
            float maxAspectRatio = i2 / maxSizeHeight5;
            float averageAspectRatio3 = averageAspectRatio2 / count4;
            float minH = AndroidUtilities.dp(100.0f) / maxSizeHeight5;
            if (!forceCalc) {
                if (count4 != 2 && count4 != 3 && count4 != 4) {
                    count2 = count4;
                    averageAspectRatio = averageAspectRatio3;
                    isOut = isOut2;
                    maxX2 = 0;
                    maxSizeHeight = maxSizeHeight5;
                    firstSpanAdditionalSize = firstSpanAdditionalSize2;
                } else {
                    if (count4 == 2) {
                        GroupedMessagePosition position1 = this.posArray.get(0);
                        GroupedMessagePosition position2 = this.posArray.get(1);
                        String pString = proportions.toString();
                        if (pString.equals("ww")) {
                            paddingsWidth = paddingsWidth2;
                            double d = averageAspectRatio3;
                            isOut = isOut2;
                            double d2 = maxAspectRatio;
                            Double.isNaN(d2);
                            if (d > d2 * 1.4d && position1.aspectRatio - position2.aspectRatio < 0.2d) {
                                float height = Math.round(Math.min(this.maxSizeWidth / position1.aspectRatio, Math.min(this.maxSizeWidth / position2.aspectRatio, maxSizeHeight5 / 2.0f))) / maxSizeHeight5;
                                position1.set(0, 0, 0, 0, this.maxSizeWidth, height, 7);
                                position2.set(0, 0, 1, 1, this.maxSizeWidth, height, 11);
                                maxX = 0;
                                firstSpanAdditionalSize = firstSpanAdditionalSize2;
                                count = count4;
                            }
                        } else {
                            isOut = isOut2;
                            paddingsWidth = paddingsWidth2;
                        }
                        if (pString.equals("ww")) {
                            maxSizeHeight2 = maxSizeHeight5;
                        } else if (pString.equals("qq")) {
                            maxSizeHeight2 = maxSizeHeight5;
                        } else {
                            int secondWidth = (int) Math.max(this.maxSizeWidth * 0.4f, Math.round((i / position1.aspectRatio) / ((1.0f / position1.aspectRatio) + (1.0f / position2.aspectRatio))));
                            int firstWidth = this.maxSizeWidth - secondWidth;
                            if (firstWidth < minWidth) {
                                int diff = minWidth - firstWidth;
                                firstWidth = minWidth;
                                secondWidth -= diff;
                            }
                            float height2 = Math.min(maxSizeHeight5, Math.round(Math.min(firstWidth / position1.aspectRatio, secondWidth / position2.aspectRatio))) / maxSizeHeight5;
                            position1.set(0, 0, 0, 0, firstWidth, height2, 13);
                            position2.set(1, 1, 0, 0, secondWidth, height2, 14);
                            maxX = 1;
                            firstSpanAdditionalSize = firstSpanAdditionalSize2;
                            count = count4;
                        }
                        int width = this.maxSizeWidth / 2;
                        float height3 = Math.round(Math.min(width / position1.aspectRatio, Math.min(width / position2.aspectRatio, maxSizeHeight2))) / maxSizeHeight2;
                        position1.set(0, 0, 0, 0, width, height3, 13);
                        position2.set(1, 1, 0, 0, width, height3, 14);
                        maxX = 1;
                        firstSpanAdditionalSize = firstSpanAdditionalSize2;
                        count = count4;
                    } else {
                        isOut = isOut2;
                        if (count4 == 3) {
                            GroupedMessagePosition position12 = this.posArray.get(0);
                            GroupedMessagePosition position22 = this.posArray.get(1);
                            GroupedMessagePosition position3 = this.posArray.get(2);
                            if (proportions.charAt(0) == 'n') {
                                float thirdHeight = Math.min(maxSizeHeight5 * 0.5f, Math.round((position22.aspectRatio * this.maxSizeWidth) / (position3.aspectRatio + position22.aspectRatio)));
                                float secondHeight = maxSizeHeight5 - thirdHeight;
                                float maxAspectRatio2 = position3.aspectRatio;
                                firstSpanAdditionalSize = firstSpanAdditionalSize2;
                                int rightWidth = (int) Math.max(minWidth, Math.min(this.maxSizeWidth * 0.5f, Math.round(Math.min(maxAspectRatio2 * thirdHeight, position22.aspectRatio * secondHeight))));
                                int leftWidth = Math.round(Math.min((position12.aspectRatio * maxSizeHeight5) + paddingsWidth2, this.maxSizeWidth - rightWidth));
                                position12.set(0, 0, 0, 1, leftWidth, 1.0f, 13);
                                position22.set(1, 1, 0, 0, rightWidth, secondHeight / maxSizeHeight5, 6);
                                position3.set(0, 1, 1, 1, rightWidth, thirdHeight / maxSizeHeight5, 10);
                                position3.spanSize = this.maxSizeWidth;
                                count3 = count4;
                                position12.siblingHeights = new float[]{thirdHeight / maxSizeHeight5, secondHeight / maxSizeHeight5};
                                if (isOut) {
                                    position12.spanSize = this.maxSizeWidth - rightWidth;
                                } else {
                                    position22.spanSize = this.maxSizeWidth - leftWidth;
                                    position3.leftSpanOffset = leftWidth;
                                }
                                this.hasSibling = true;
                                maxX = 1;
                            } else {
                                firstSpanAdditionalSize = firstSpanAdditionalSize2;
                                count3 = count4;
                                int firstSpanAdditionalSize3 = this.maxSizeWidth;
                                float firstHeight = Math.round(Math.min(firstSpanAdditionalSize3 / position12.aspectRatio, maxSizeHeight5 * 0.66f)) / maxSizeHeight5;
                                position12.set(0, 1, 0, 0, this.maxSizeWidth, firstHeight, 7);
                                int width2 = this.maxSizeWidth / 2;
                                float secondHeight2 = Math.min(maxSizeHeight5 - firstHeight, Math.round(Math.min(width2 / position22.aspectRatio, width2 / position3.aspectRatio))) / maxSizeHeight5;
                                if (secondHeight2 < minH) {
                                    secondHeight2 = minH;
                                }
                                float f = secondHeight2;
                                position22.set(0, 0, 1, 1, width2, f, 9);
                                position3.set(1, 1, 1, 1, width2, f, 10);
                                maxX = 1;
                            }
                            count = count3;
                        } else {
                            firstSpanAdditionalSize = firstSpanAdditionalSize2;
                            GroupedMessagePosition position13 = this.posArray.get(0);
                            GroupedMessagePosition position23 = this.posArray.get(1);
                            GroupedMessagePosition position32 = this.posArray.get(2);
                            GroupedMessagePosition position4 = this.posArray.get(3);
                            if (proportions.charAt(0) == 'w') {
                                float h0 = Math.round(Math.min(this.maxSizeWidth / position13.aspectRatio, 0.66f * maxSizeHeight5)) / maxSizeHeight5;
                                position13.set(0, 2, 0, 0, this.maxSizeWidth, h0, 7);
                                float h = Math.round(this.maxSizeWidth / ((position23.aspectRatio + position32.aspectRatio) + position4.aspectRatio));
                                int w0 = (int) Math.max(minWidth, Math.min(this.maxSizeWidth * 0.4f, position23.aspectRatio * h));
                                int w2 = (int) Math.max(Math.max(minWidth, this.maxSizeWidth * 0.33f), position4.aspectRatio * h);
                                int w1 = (this.maxSizeWidth - w0) - w2;
                                if (w1 < AndroidUtilities.dp(58.0f)) {
                                    int diff2 = AndroidUtilities.dp(58.0f) - w1;
                                    w1 = AndroidUtilities.dp(58.0f);
                                    w0 -= diff2 / 2;
                                    w2 -= diff2 - (diff2 / 2);
                                }
                                float h2 = Math.min(maxSizeHeight5 - h0, h) / maxSizeHeight5;
                                if (h2 < minH) {
                                    h2 = minH;
                                }
                                float f2 = h2;
                                position23.set(0, 0, 1, 1, w0, f2, 9);
                                position32.set(1, 1, 1, 1, w1, f2, 8);
                                position4.set(2, 2, 1, 1, w2, f2, 10);
                                maxX = 2;
                            } else {
                                int w = Math.max(minWidth, Math.round(maxSizeHeight5 / (((1.0f / position23.aspectRatio) + (1.0f / position32.aspectRatio)) + (1.0f / position4.aspectRatio))));
                                float h02 = Math.min(0.33f, Math.max(minHeight2, w / position23.aspectRatio) / maxSizeHeight5);
                                float h1 = Math.min(0.33f, Math.max(minHeight2, w / position32.aspectRatio) / maxSizeHeight5);
                                float h22 = (1.0f - h02) - h1;
                                int w02 = Math.round(Math.min((position13.aspectRatio * maxSizeHeight5) + paddingsWidth2, this.maxSizeWidth - w));
                                position13.set(0, 0, 0, 2, w02, h02 + h1 + h22, 13);
                                position23.set(1, 1, 0, 0, w, h02, 6);
                                position32.set(0, 1, 1, 1, w, h1, 2);
                                position32.spanSize = this.maxSizeWidth;
                                position4.set(0, 1, 2, 2, w, h22, 10);
                                position4.spanSize = this.maxSizeWidth;
                                if (isOut) {
                                    position13.spanSize = this.maxSizeWidth - w;
                                } else {
                                    position23.spanSize = this.maxSizeWidth - w02;
                                    position32.leftSpanOffset = w02;
                                    position4.leftSpanOffset = w02;
                                }
                                position13.siblingHeights = new float[]{h02, h1, h22};
                                this.hasSibling = true;
                                maxX = 1;
                            }
                            count = count4;
                        }
                    }
                    for (a = 0; a < count; a++) {
                        GroupedMessagePosition pos2 = this.posArray.get(a);
                        if (isOut) {
                            if (pos2.minX == 0) {
                                pos2.spanSize += firstSpanAdditionalSize;
                            }
                            if ((pos2.flags & 2) != 0) {
                                pos2.edge = true;
                            }
                        } else {
                            if (pos2.maxX == maxX || (pos2.flags & 2) != 0) {
                                pos2.spanSize += firstSpanAdditionalSize;
                            }
                            if ((pos2.flags & 1) != 0) {
                                pos2.edge = true;
                            }
                        }
                        MessageObject messageObject2 = this.messages.get(a);
                        if (!isOut && messageObject2.needDrawAvatarInternal()) {
                            if (pos2.edge) {
                                if (pos2.spanSize != 1000) {
                                    pos2.spanSize += 108;
                                }
                                pos2.pw += 108;
                            } else if ((pos2.flags & 2) != 0) {
                                if (pos2.spanSize != 1000) {
                                    pos2.spanSize -= 108;
                                } else if (pos2.leftSpanOffset != 0) {
                                    pos2.leftSpanOffset += 108;
                                }
                            }
                        }
                    }
                }
            } else {
                count2 = count4;
                averageAspectRatio = averageAspectRatio3;
                isOut = isOut2;
                maxX2 = 0;
                maxSizeHeight = maxSizeHeight5;
                firstSpanAdditionalSize = firstSpanAdditionalSize2;
            }
            float[] croppedRatios = new float[this.posArray.size()];
            int a4 = 0;
            while (true) {
                count = count2;
                if (a4 >= count) {
                    break;
                }
                if (averageAspectRatio > 1.1f) {
                    croppedRatios[a4] = Math.max(1.0f, this.posArray.get(a4).aspectRatio);
                } else {
                    croppedRatios[a4] = Math.min(1.0f, this.posArray.get(a4).aspectRatio);
                }
                croppedRatios[a4] = Math.max(0.66667f, Math.min(1.7f, croppedRatios[a4]));
                a4++;
                count2 = count;
            }
            ArrayList<MessageGroupedLayoutAttempt> attempts = new ArrayList<>();
            for (int firstLine = 1; firstLine < croppedRatios.length; firstLine++) {
                int secondLine = croppedRatios.length - firstLine;
                if (firstLine <= 3 && secondLine <= 3) {
                    attempts.add(new MessageGroupedLayoutAttempt(firstLine, secondLine, multiHeight(croppedRatios, 0, firstLine), multiHeight(croppedRatios, firstLine, croppedRatios.length)));
                }
            }
            for (int firstLine2 = 1; firstLine2 < croppedRatios.length - 1; firstLine2++) {
                for (int secondLine2 = 1; secondLine2 < croppedRatios.length - firstLine2; secondLine2++) {
                    int thirdLine = (croppedRatios.length - firstLine2) - secondLine2;
                    if (firstLine2 <= 3) {
                        if (secondLine2 <= (averageAspectRatio < 0.85f ? 4 : 3) && thirdLine <= 3) {
                            attempts.add(new MessageGroupedLayoutAttempt(firstLine2, secondLine2, thirdLine, multiHeight(croppedRatios, 0, firstLine2), multiHeight(croppedRatios, firstLine2, firstLine2 + secondLine2), multiHeight(croppedRatios, firstLine2 + secondLine2, croppedRatios.length)));
                        }
                    }
                }
            }
            int firstLine3 = 1;
            while (firstLine3 < croppedRatios.length - 2) {
                for (int secondLine3 = 1; secondLine3 < croppedRatios.length - firstLine3; secondLine3++) {
                    for (int thirdLine2 = 1; thirdLine2 < (croppedRatios.length - firstLine3) - secondLine3; thirdLine2++) {
                        int fourthLine = ((croppedRatios.length - firstLine3) - secondLine3) - thirdLine2;
                        if (firstLine3 <= 3 && secondLine3 <= 3 && thirdLine2 <= 3 && fourthLine <= 3) {
                            attempts.add(new MessageGroupedLayoutAttempt(firstLine3, secondLine3, thirdLine2, fourthLine, multiHeight(croppedRatios, 0, firstLine3), multiHeight(croppedRatios, firstLine3, firstLine3 + secondLine3), multiHeight(croppedRatios, firstLine3 + secondLine3, firstLine3 + secondLine3 + thirdLine2), multiHeight(croppedRatios, firstLine3 + secondLine3 + thirdLine2, croppedRatios.length)));
                        }
                    }
                }
                firstLine3++;
            }
            MessageGroupedLayoutAttempt optimal = null;
            float optimalDiff = 0.0f;
            float maxHeight2 = (this.maxSizeWidth / 3) * 4;
            int a5 = 0;
            while (a5 < attempts.size()) {
                MessageGroupedLayoutAttempt attempt = attempts.get(a5);
                float height4 = 0.0f;
                float minLineHeight = Float.MAX_VALUE;
                ArrayList<MessageGroupedLayoutAttempt> attempts2 = attempts;
                int b = 0;
                while (true) {
                    minHeight = minHeight2;
                    if (b >= attempt.heights.length) {
                        break;
                    }
                    height4 += attempt.heights[b];
                    if (attempt.heights[b] < minLineHeight) {
                        minLineHeight = attempt.heights[b];
                    }
                    b++;
                    minHeight2 = minHeight;
                }
                float diff3 = Math.abs(height4 - maxHeight2);
                int firstLine4 = firstLine3;
                if (attempt.lineCounts.length <= 1) {
                    maxHeight = maxHeight2;
                } else {
                    maxHeight = maxHeight2;
                    if (attempt.lineCounts[0] <= attempt.lineCounts[1] && ((attempt.lineCounts.length <= 2 || attempt.lineCounts[1] <= attempt.lineCounts[2]) && (attempt.lineCounts.length <= 3 || attempt.lineCounts[2] <= attempt.lineCounts[3]))) {
                    }
                    diff3 *= 1.2f;
                }
                float maxHeight3 = minWidth;
                if (minLineHeight < maxHeight3) {
                    diff3 *= 1.5f;
                }
                if (optimal == null || diff3 < optimalDiff) {
                    optimal = attempt;
                    optimalDiff = diff3;
                }
                a5++;
                attempts = attempts2;
                minHeight2 = minHeight;
                firstLine3 = firstLine4;
                maxHeight2 = maxHeight;
            }
            if (optimal == null) {
                return;
            }
            int index = 0;
            float y = 0.0f;
            int i3 = 0;
            int maxX3 = maxX2;
            while (i3 < optimal.lineCounts.length) {
                int c = optimal.lineCounts[i3];
                float lineHeight = optimal.heights[i3];
                int spanLeft = this.maxSizeWidth;
                int index2 = index;
                int index3 = c - 1;
                int maxX4 = maxX3 == 1 ? 1 : 0;
                int maxX5 = Math.max(maxX4, index3);
                int k = 0;
                int minWidth2 = minWidth;
                GroupedMessagePosition posToFix = null;
                int spanLeft2 = spanLeft;
                int spanLeft3 = index2;
                int maxX6 = maxX5;
                while (k < c) {
                    float ratio = croppedRatios[spanLeft3];
                    float[] croppedRatios2 = croppedRatios;
                    int width3 = (int) (ratio * lineHeight);
                    spanLeft2 -= width3;
                    float optimalDiff2 = optimalDiff;
                    GroupedMessagePosition pos3 = this.posArray.get(spanLeft3);
                    int flags = 0;
                    if (i3 == 0) {
                        flags = 0 | 4;
                    }
                    int maxX7 = maxX6;
                    if (i3 == optimal.lineCounts.length - 1) {
                        flags |= 8;
                    }
                    if (k == 0) {
                        flags |= 1;
                        if (isOut) {
                            posToFix = pos3;
                        }
                    }
                    if (k == c - 1) {
                        flags |= 2;
                        if (!isOut) {
                            posToFix = pos3;
                        }
                    }
                    pos3.set(k, k, i3, i3, width3, Math.max(minH, lineHeight / maxSizeHeight), flags);
                    spanLeft3++;
                    k++;
                    croppedRatios = croppedRatios2;
                    optimalDiff = optimalDiff2;
                    maxX6 = maxX7;
                }
                boolean z2 = maxX6 == 1 ? 1 : 0;
                posToFix.pw += spanLeft2;
                posToFix.spanSize += spanLeft2;
                y += lineHeight;
                i3++;
                index = spanLeft3;
                minWidth = minWidth2;
                croppedRatios = croppedRatios;
                maxX3 = maxX6;
            }
            maxX = maxX3;
            while (a < count) {
            }
        }

        public MessageObject findPrimaryMessageObject() {
            return findMessageWithFlags(5);
        }

        public MessageObject findMessageWithFlags(int flags) {
            if (!this.messages.isEmpty() && this.positions.isEmpty()) {
                calculate();
            }
            for (int i = 0; i < this.messages.size(); i++) {
                MessageObject object = this.messages.get(i);
                GroupedMessagePosition position = this.positions.get(object);
                if (position != null && (position.flags & flags) == flags) {
                    return object;
                }
            }
            return null;
        }

        /* loaded from: classes4.dex */
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

    public MessageObject(int accountNum, TLRPC.Message message, String formattedMessage, String name, String userName, boolean localMessage, boolean isChannel, boolean supergroup, boolean edit) {
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.viewRef = new AtomicReference<>(null);
        this.localType = localMessage ? 2 : 1;
        this.currentAccount = accountNum;
        this.localName = name;
        this.localUserName = userName;
        this.messageText = formattedMessage;
        this.messageOwner = message;
        this.localChannel = isChannel;
        this.localSupergroup = supergroup;
        this.localEdit = edit;
    }

    public MessageObject(int accountNum, TLRPC.Message message, AbstractMap<Long, TLRPC.User> users, boolean generateLayout, boolean checkMediaExists) {
        this(accountNum, message, users, (AbstractMap<Long, TLRPC.Chat>) null, generateLayout, checkMediaExists);
    }

    public MessageObject(int accountNum, TLRPC.Message message, LongSparseArray<TLRPC.User> users, boolean generateLayout, boolean checkMediaExists) {
        this(accountNum, message, users, (LongSparseArray<TLRPC.Chat>) null, generateLayout, checkMediaExists);
    }

    public MessageObject(int accountNum, TLRPC.Message message, boolean generateLayout, boolean checkMediaExists) {
        this(accountNum, message, null, null, null, null, null, generateLayout, checkMediaExists, 0L);
    }

    public MessageObject(int accountNum, TLRPC.Message message, MessageObject replyToMessage, boolean generateLayout, boolean checkMediaExists) {
        this(accountNum, message, replyToMessage, null, null, null, null, generateLayout, checkMediaExists, 0L);
    }

    public MessageObject(int accountNum, TLRPC.Message message, AbstractMap<Long, TLRPC.User> users, AbstractMap<Long, TLRPC.Chat> chats, boolean generateLayout, boolean checkMediaExists) {
        this(accountNum, message, users, chats, generateLayout, checkMediaExists, 0L);
    }

    public MessageObject(int accountNum, TLRPC.Message message, LongSparseArray<TLRPC.User> users, LongSparseArray<TLRPC.Chat> chats, boolean generateLayout, boolean checkMediaExists) {
        this(accountNum, message, null, null, null, users, chats, generateLayout, checkMediaExists, 0L);
    }

    public MessageObject(int accountNum, TLRPC.Message message, AbstractMap<Long, TLRPC.User> users, AbstractMap<Long, TLRPC.Chat> chats, boolean generateLayout, boolean checkMediaExists, long eid) {
        this(accountNum, message, null, users, chats, null, null, generateLayout, checkMediaExists, eid);
    }

    public MessageObject(int accountNum, TLRPC.Message message, MessageObject replyToMessage, AbstractMap<Long, TLRPC.User> users, AbstractMap<Long, TLRPC.Chat> chats, LongSparseArray<TLRPC.User> sUsers, LongSparseArray<TLRPC.Chat> sChats, boolean generateLayout, boolean checkMediaExists, long eid) {
        TextPaint paint;
        CharSequence emoji;
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.viewRef = new AtomicReference<>(null);
        Theme.createCommonMessageResources();
        this.currentAccount = accountNum;
        this.messageOwner = message;
        this.replyMessageObject = replyToMessage;
        this.eventId = eid;
        this.wasUnread = !message.out && this.messageOwner.unread;
        if (message.replyMessage != null) {
            this.replyMessageObject = new MessageObject(this.currentAccount, message.replyMessage, null, users, chats, sUsers, sChats, false, checkMediaExists, eid);
        }
        TLRPC.User fromUser = null;
        fromUser = message.from_id instanceof TLRPC.TL_peerUser ? getUser(users, sUsers, message.from_id.user_id) : fromUser;
        updateMessageText(users, chats, sUsers, sChats);
        setType();
        measureInlineBotButtons();
        Calendar rightNow = new GregorianCalendar();
        rightNow.setTimeInMillis(this.messageOwner.date * 1000);
        int dateDay = rightNow.get(6);
        int dateYear = rightNow.get(1);
        int dateMonth = rightNow.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(dateYear), Integer.valueOf(dateMonth), Integer.valueOf(dateDay));
        this.monthKey = String.format("%d_%02d", Integer.valueOf(dateYear), Integer.valueOf(dateMonth));
        createMessageSendInfo();
        generateCaption();
        if (generateLayout) {
            if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                paint = Theme.chat_msgGameTextPaint;
            } else {
                paint = Theme.chat_msgTextPaint;
            }
            int[] emojiOnly = allowsBigEmoji() ? new int[1] : null;
            this.messageText = Emoji.replaceEmoji(this.messageText, paint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, emojiOnly, this.contentType == 0, this.viewRef);
            checkEmojiOnly(emojiOnly);
            this.emojiAnimatedSticker = null;
            if (this.emojiOnlyCount == 1 && !(message.media instanceof TLRPC.TL_messageMediaWebPage) && !(message.media instanceof TLRPC.TL_messageMediaInvoice) && message.entities.isEmpty() && (((message.media instanceof TLRPC.TL_messageMediaEmpty) || message.media == null) && this.messageOwner.grouped_id == 0)) {
                CharSequence emoji2 = this.messageText;
                int indexOf = TextUtils.indexOf(emoji2, "🏻");
                int index = indexOf;
                if (indexOf >= 0) {
                    this.emojiAnimatedStickerColor = "_c1";
                    emoji2 = emoji2.subSequence(0, index);
                } else {
                    int indexOf2 = TextUtils.indexOf(emoji2, "🏼");
                    index = indexOf2;
                    if (indexOf2 >= 0) {
                        this.emojiAnimatedStickerColor = "_c2";
                        emoji2 = emoji2.subSequence(0, index);
                    } else {
                        int indexOf3 = TextUtils.indexOf(emoji2, "🏽");
                        index = indexOf3;
                        if (indexOf3 >= 0) {
                            this.emojiAnimatedStickerColor = "_c3";
                            emoji2 = emoji2.subSequence(0, index);
                        } else {
                            int indexOf4 = TextUtils.indexOf(emoji2, "🏾");
                            index = indexOf4;
                            if (indexOf4 >= 0) {
                                this.emojiAnimatedStickerColor = "_c4";
                                emoji2 = emoji2.subSequence(0, index);
                            } else {
                                int indexOf5 = TextUtils.indexOf(emoji2, "🏿");
                                index = indexOf5;
                                if (indexOf5 >= 0) {
                                    this.emojiAnimatedStickerColor = "_c5";
                                    emoji2 = emoji2.subSequence(0, index);
                                } else {
                                    this.emojiAnimatedStickerColor = "";
                                }
                            }
                        }
                    }
                }
                if (!TextUtils.isEmpty(this.emojiAnimatedStickerColor) && index + 2 < this.messageText.length()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(emoji2.toString());
                    CharSequence charSequence = this.messageText;
                    sb.append(charSequence.subSequence(index + 2, charSequence.length()).toString());
                    emoji = sb.toString();
                } else {
                    emoji = emoji2;
                }
                if (TextUtils.isEmpty(this.emojiAnimatedStickerColor) || EmojiData.emojiColoredMap.contains(emoji.toString())) {
                    this.emojiAnimatedSticker = MediaDataController.getInstance(this.currentAccount).getEmojiAnimatedSticker(emoji);
                }
            }
            if (this.emojiAnimatedSticker != null) {
                this.type = 1000;
                if (isSticker()) {
                    this.type = 13;
                } else if (isAnimatedSticker()) {
                    this.type = 15;
                }
            } else {
                generateLayout(fromUser);
            }
            createPathThumb();
        }
        this.layoutCreated = generateLayout;
        generateThumbs(false);
        if (checkMediaExists) {
            checkMediaExistance();
        }
    }

    private void createPathThumb() {
        TLRPC.Document document = getDocument();
        if (document == null) {
            return;
        }
        this.pathThumb = DocumentObject.getSvgThumb(document, Theme.key_chat_serviceBackground, 1.0f);
    }

    public void createStrippedThumb() {
        if (this.photoThumbs == null || SharedConfig.getDevicePerformanceClass() != 2) {
            return;
        }
        try {
            int N = this.photoThumbs.size();
            for (int a = 0; a < N; a++) {
                TLRPC.PhotoSize photoSize = this.photoThumbs.get(a);
                if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                    this.strippedThumb = new BitmapDrawable(ImageLoader.getStrippedPhotoBitmap(photoSize.bytes, "b"));
                    return;
                }
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void createDateArray(int accountNum, TLRPC.TL_channelAdminLogEvent event, ArrayList<MessageObject> messageObjects, HashMap<String, ArrayList<MessageObject>> messagesByDays, boolean addToEnd) {
        ArrayList<MessageObject> dayArray = messagesByDays.get(this.dateKey);
        if (dayArray == null) {
            ArrayList<MessageObject> dayArray2 = new ArrayList<>();
            messagesByDays.put(this.dateKey, dayArray2);
            TLRPC.TL_message dateMsg = new TLRPC.TL_message();
            dateMsg.message = LocaleController.formatDateChat(event.date);
            dateMsg.id = 0;
            dateMsg.date = event.date;
            MessageObject dateObj = new MessageObject(accountNum, dateMsg, false, false);
            dateObj.type = 10;
            dateObj.contentType = 1;
            dateObj.isDateObject = true;
            if (addToEnd) {
                messageObjects.add(0, dateObj);
            } else {
                messageObjects.add(dateObj);
            }
        }
    }

    public void checkForScam() {
    }

    private void checkEmojiOnly(int[] emojiOnly) {
        int size;
        TextPaint emojiPaint;
        if (emojiOnly != null && emojiOnly[0] >= 1 && emojiOnly[0] <= 3) {
            switch (emojiOnly[0]) {
                case 1:
                    emojiPaint = Theme.chat_msgTextPaintOneEmoji;
                    int size2 = AndroidUtilities.dp(32.0f);
                    this.emojiOnlyCount = 1;
                    size = size2;
                    break;
                case 2:
                    emojiPaint = Theme.chat_msgTextPaintTwoEmoji;
                    size = AndroidUtilities.dp(28.0f);
                    this.emojiOnlyCount = 2;
                    break;
                default:
                    emojiPaint = Theme.chat_msgTextPaintThreeEmoji;
                    size = AndroidUtilities.dp(24.0f);
                    this.emojiOnlyCount = 3;
                    break;
            }
            CharSequence charSequence = this.messageText;
            Emoji.EmojiSpan[] spans = (Emoji.EmojiSpan[]) ((Spannable) charSequence).getSpans(0, charSequence.length(), Emoji.EmojiSpan.class);
            if (spans != null && spans.length > 0) {
                for (Emoji.EmojiSpan emojiSpan : spans) {
                    emojiSpan.replaceFontMetrics(emojiPaint.getFontMetricsInt(), size);
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:166:0x0505, code lost:
        if (r12.until_date != r11.until_date) goto L170;
     */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0592  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x05a4 A[LOOP:0: B:174:0x054c->B:196:0x05a4, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:748:0x16c6  */
    /* JADX WARN: Removed duplicated region for block: B:751:0x171f  */
    /* JADX WARN: Removed duplicated region for block: B:752:0x1722  */
    /* JADX WARN: Removed duplicated region for block: B:754:0x1725  */
    /* JADX WARN: Removed duplicated region for block: B:764:0x17a3  */
    /* JADX WARN: Removed duplicated region for block: B:767:0x17ae  */
    /* JADX WARN: Removed duplicated region for block: B:790:0x183d  */
    /* JADX WARN: Removed duplicated region for block: B:792:0x05b2 A[EDGE_INSN: B:792:0x05b2->B:198:0x05b2 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MessageObject(int accountNum, TLRPC.TL_channelAdminLogEvent event, ArrayList<MessageObject> messageObjects, HashMap<String, ArrayList<MessageObject>> messagesByDays, TLRPC.Chat chat, int[] mid, boolean addToEnd) {
        TLRPC.User fromUser;
        TLRPC.TL_peerChannel tL_peerChannel;
        String str;
        TLRPC.User fromUser2;
        TLRPC.Message message;
        TLRPC.Message message2;
        String str2;
        int[] iArr;
        ArrayList<MessageObject> arrayList;
        TextPaint paint;
        TLRPC.Message message3;
        TLRPC.ChannelParticipant prev_participant;
        TLRPC.ChannelParticipant new_participant;
        TLObject whoUser;
        StringBuilder rights;
        String str3;
        String str4;
        int i;
        String str5;
        TLRPC.TL_peerChannel tL_peerChannel2;
        char c;
        String time;
        TLObject object;
        TLObject object2;
        TLObject object3;
        char c2;
        String string;
        boolean changedCaption;
        boolean changedMedia;
        String str6;
        int i2;
        String str7;
        int i3;
        String str8;
        int i4;
        String str9;
        int i5;
        TLRPC.User fromUser3;
        TLObject whoUser2;
        String str10;
        StringBuilder bannedDuration;
        char c3;
        char c4;
        char c5;
        char c6;
        char c7;
        char c8;
        char c9;
        char c10;
        StringBuilder bannedDuration2;
        int count;
        String addStr;
        TLRPC.Chat chat2 = chat;
        this.type = 1000;
        this.forceSeekTo = -1.0f;
        this.viewRef = new AtomicReference<>(null);
        this.currentEvent = event;
        this.currentAccount = accountNum;
        if (event.user_id <= 0) {
            fromUser = null;
        } else {
            fromUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(event.user_id));
        }
        Calendar rightNow = new GregorianCalendar();
        rightNow.setTimeInMillis(event.date * 1000);
        int dateDay = rightNow.get(6);
        int dateYear = rightNow.get(1);
        int dateMonth = rightNow.get(2);
        this.dateKey = String.format("%d_%02d_%02d", Integer.valueOf(dateYear), Integer.valueOf(dateMonth), Integer.valueOf(dateDay));
        this.monthKey = String.format("%d_%02d", Integer.valueOf(dateYear), Integer.valueOf(dateMonth));
        TLRPC.TL_peerChannel tL_peerChannel3 = new TLRPC.TL_peerChannel();
        tL_peerChannel3.channel_id = chat2.id;
        if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangeTitle) {
            String title = ((TLRPC.TL_channelAdminLogEventActionChangeTitle) event.action).new_value;
            if (chat2.megagroup) {
                this.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedGroupTitle", org.telegram.messenger.beta.R.string.EventLogEditedGroupTitle, title), "un1", fromUser);
            } else {
                this.messageText = replaceWithLink(LocaleController.formatString("EventLogEditedChannelTitle", org.telegram.messenger.beta.R.string.EventLogEditedChannelTitle, title), "un1", fromUser);
            }
            message3 = null;
            str = "";
            tL_peerChannel = tL_peerChannel3;
            fromUser2 = fromUser;
        } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangePhoto) {
            TLRPC.TL_channelAdminLogEventActionChangePhoto action = (TLRPC.TL_channelAdminLogEventActionChangePhoto) event.action;
            this.messageOwner = new TLRPC.TL_messageService();
            if (action.new_photo instanceof TLRPC.TL_photoEmpty) {
                this.messageOwner.action = new TLRPC.TL_messageActionChatDeletePhoto();
                if (chat2.megagroup) {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedWGroupPhoto", org.telegram.messenger.beta.R.string.EventLogRemovedWGroupPhoto), "un1", fromUser);
                } else {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedChannelPhoto", org.telegram.messenger.beta.R.string.EventLogRemovedChannelPhoto), "un1", fromUser);
                }
            } else {
                this.messageOwner.action = new TLRPC.TL_messageActionChatEditPhoto();
                this.messageOwner.action.photo = action.new_photo;
                if (chat2.megagroup) {
                    if (isVideoAvatar()) {
                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedGroupVideo", org.telegram.messenger.beta.R.string.EventLogEditedGroupVideo), "un1", fromUser);
                    } else {
                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedGroupPhoto", org.telegram.messenger.beta.R.string.EventLogEditedGroupPhoto), "un1", fromUser);
                    }
                } else if (isVideoAvatar()) {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedChannelVideo", org.telegram.messenger.beta.R.string.EventLogEditedChannelVideo), "un1", fromUser);
                } else {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedChannelPhoto", org.telegram.messenger.beta.R.string.EventLogEditedChannelPhoto), "un1", fromUser);
                }
            }
            message3 = null;
            str = "";
            tL_peerChannel = tL_peerChannel3;
            fromUser2 = fromUser;
        } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantJoin) {
            if (chat2.megagroup) {
                this.messageText = replaceWithLink(LocaleController.getString("EventLogGroupJoined", org.telegram.messenger.beta.R.string.EventLogGroupJoined), "un1", fromUser);
                message3 = null;
                str = "";
                tL_peerChannel = tL_peerChannel3;
                fromUser2 = fromUser;
            } else {
                this.messageText = replaceWithLink(LocaleController.getString("EventLogChannelJoined", org.telegram.messenger.beta.R.string.EventLogChannelJoined), "un1", fromUser);
                message3 = null;
                str = "";
                tL_peerChannel = tL_peerChannel3;
                fromUser2 = fromUser;
            }
        } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantLeave) {
            TLRPC.TL_messageService tL_messageService = new TLRPC.TL_messageService();
            this.messageOwner = tL_messageService;
            tL_messageService.action = new TLRPC.TL_messageActionChatDeleteUser();
            this.messageOwner.action.user_id = event.user_id;
            if (chat2.megagroup) {
                this.messageText = replaceWithLink(LocaleController.getString("EventLogLeftGroup", org.telegram.messenger.beta.R.string.EventLogLeftGroup), "un1", fromUser);
                message3 = null;
                str = "";
                tL_peerChannel = tL_peerChannel3;
                fromUser2 = fromUser;
            } else {
                this.messageText = replaceWithLink(LocaleController.getString("EventLogLeftChannel", org.telegram.messenger.beta.R.string.EventLogLeftChannel), "un1", fromUser);
                message3 = null;
                str = "";
                tL_peerChannel = tL_peerChannel3;
                fromUser2 = fromUser;
            }
        } else if (!(event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantInvite)) {
            message3 = null;
            if (event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin) {
                fromUser2 = fromUser;
            } else if ((event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) && (((TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) event.action).prev_participant instanceof TLRPC.TL_channelParticipantAdmin) && (((TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) event.action).new_participant instanceof TLRPC.TL_channelParticipant)) {
                fromUser2 = fromUser;
            } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionDefaultBannedRights) {
                TLRPC.TL_channelAdminLogEventActionDefaultBannedRights bannedRights = (TLRPC.TL_channelAdminLogEventActionDefaultBannedRights) event.action;
                this.messageOwner = new TLRPC.TL_message();
                TLRPC.TL_chatBannedRights o = bannedRights.prev_banned_rights;
                TLRPC.TL_chatBannedRights n = bannedRights.new_banned_rights;
                StringBuilder rights2 = new StringBuilder(LocaleController.getString("EventLogDefaultPermissions", org.telegram.messenger.beta.R.string.EventLogDefaultPermissions));
                boolean added = false;
                o = o == null ? new TLRPC.TL_chatBannedRights() : o;
                n = n == null ? new TLRPC.TL_chatBannedRights() : n;
                if (o.send_messages != n.send_messages) {
                    rights2.append('\n');
                    added = true;
                    rights2.append('\n');
                    rights2.append(!n.send_messages ? '+' : '-');
                    rights2.append(' ');
                    rights2.append(LocaleController.getString("EventLogRestrictedSendMessages", org.telegram.messenger.beta.R.string.EventLogRestrictedSendMessages));
                }
                if (o.send_stickers != n.send_stickers || o.send_inline != n.send_inline || o.send_gifs != n.send_gifs || o.send_games != n.send_games) {
                    if (!added) {
                        rights2.append('\n');
                        added = true;
                    }
                    rights2.append('\n');
                    rights2.append(!n.send_stickers ? '+' : '-');
                    rights2.append(' ');
                    rights2.append(LocaleController.getString("EventLogRestrictedSendStickers", org.telegram.messenger.beta.R.string.EventLogRestrictedSendStickers));
                }
                if (o.send_media != n.send_media) {
                    if (!added) {
                        rights2.append('\n');
                        added = true;
                    }
                    rights2.append('\n');
                    rights2.append(!n.send_media ? '+' : '-');
                    rights2.append(' ');
                    rights2.append(LocaleController.getString("EventLogRestrictedSendMedia", org.telegram.messenger.beta.R.string.EventLogRestrictedSendMedia));
                }
                if (o.send_polls != n.send_polls) {
                    if (!added) {
                        rights2.append('\n');
                        added = true;
                    }
                    rights2.append('\n');
                    rights2.append(!n.send_polls ? '+' : '-');
                    rights2.append(' ');
                    rights2.append(LocaleController.getString("EventLogRestrictedSendPolls", org.telegram.messenger.beta.R.string.EventLogRestrictedSendPolls));
                }
                if (o.embed_links != n.embed_links) {
                    if (!added) {
                        rights2.append('\n');
                        added = true;
                    }
                    rights2.append('\n');
                    rights2.append(!n.embed_links ? '+' : '-');
                    rights2.append(' ');
                    rights2.append(LocaleController.getString("EventLogRestrictedSendEmbed", org.telegram.messenger.beta.R.string.EventLogRestrictedSendEmbed));
                }
                if (o.change_info != n.change_info) {
                    if (!added) {
                        rights2.append('\n');
                        added = true;
                    }
                    rights2.append('\n');
                    rights2.append(!n.change_info ? '+' : '-');
                    rights2.append(' ');
                    rights2.append(LocaleController.getString("EventLogRestrictedChangeInfo", org.telegram.messenger.beta.R.string.EventLogRestrictedChangeInfo));
                }
                if (o.invite_users != n.invite_users) {
                    if (!added) {
                        rights2.append('\n');
                        added = true;
                    }
                    rights2.append('\n');
                    rights2.append(!n.invite_users ? '+' : '-');
                    rights2.append(' ');
                    rights2.append(LocaleController.getString("EventLogRestrictedInviteUsers", org.telegram.messenger.beta.R.string.EventLogRestrictedInviteUsers));
                }
                if (o.pin_messages != n.pin_messages) {
                    if (!added) {
                        rights2.append('\n');
                    }
                    rights2.append('\n');
                    rights2.append(!n.pin_messages ? '+' : '-');
                    rights2.append(' ');
                    rights2.append(LocaleController.getString("EventLogRestrictedPinMessages", org.telegram.messenger.beta.R.string.EventLogRestrictedPinMessages));
                }
                this.messageText = rights2.toString();
                str = "";
                tL_peerChannel = tL_peerChannel3;
                fromUser2 = fromUser;
            } else if (!(event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleBan)) {
                str = "";
                tL_peerChannel = tL_peerChannel3;
                TLRPC.User fromUser4 = fromUser;
                if (event.action instanceof TLRPC.TL_channelAdminLogEventActionUpdatePinned) {
                    TLRPC.TL_channelAdminLogEventActionUpdatePinned action2 = (TLRPC.TL_channelAdminLogEventActionUpdatePinned) event.action;
                    TLRPC.Message message4 = action2.message;
                    if (fromUser4 != null) {
                        fromUser2 = fromUser4;
                        if (fromUser2.id == 136817688 && action2.message.fwd_from != null && (action2.message.fwd_from.from_id instanceof TLRPC.TL_peerChannel)) {
                            TLRPC.Chat channel = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(action2.message.fwd_from.from_id.channel_id));
                            if ((action2.message instanceof TLRPC.TL_messageEmpty) || !action2.message.pinned) {
                                this.messageText = replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", org.telegram.messenger.beta.R.string.EventLogUnpinnedMessages), "un1", channel);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.getString("EventLogPinnedMessages", org.telegram.messenger.beta.R.string.EventLogPinnedMessages), "un1", channel);
                            }
                            chat2 = chat;
                            message = message4;
                        }
                    } else {
                        fromUser2 = fromUser4;
                    }
                    if ((action2.message instanceof TLRPC.TL_messageEmpty) || !action2.message.pinned) {
                        this.messageText = replaceWithLink(LocaleController.getString("EventLogUnpinnedMessages", org.telegram.messenger.beta.R.string.EventLogUnpinnedMessages), "un1", fromUser2);
                    } else {
                        this.messageText = replaceWithLink(LocaleController.getString("EventLogPinnedMessages", org.telegram.messenger.beta.R.string.EventLogPinnedMessages), "un1", fromUser2);
                    }
                    chat2 = chat;
                    message = message4;
                } else {
                    fromUser2 = fromUser4;
                    if (event.action instanceof TLRPC.TL_channelAdminLogEventActionStopPoll) {
                        TLRPC.Message message5 = ((TLRPC.TL_channelAdminLogEventActionStopPoll) event.action).message;
                        if (!(message5.media instanceof TLRPC.TL_messageMediaPoll) || !((TLRPC.TL_messageMediaPoll) message5.media).poll.quiz) {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogStopPoll", org.telegram.messenger.beta.R.string.EventLogStopPoll), "un1", fromUser2);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogStopQuiz", org.telegram.messenger.beta.R.string.EventLogStopQuiz), "un1", fromUser2);
                        }
                        chat2 = chat;
                        message = message5;
                    } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionToggleSignatures) {
                        if (((TLRPC.TL_channelAdminLogEventActionToggleSignatures) event.action).new_value) {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOn", org.telegram.messenger.beta.R.string.EventLogToggledSignaturesOn), "un1", fromUser2);
                            chat2 = chat;
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSignaturesOff", org.telegram.messenger.beta.R.string.EventLogToggledSignaturesOff), "un1", fromUser2);
                            chat2 = chat;
                        }
                    } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionToggleInvites) {
                        if (((TLRPC.TL_channelAdminLogEventActionToggleInvites) event.action).new_value) {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesOn", org.telegram.messenger.beta.R.string.EventLogToggledInvitesOn), "un1", fromUser2);
                            chat2 = chat;
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesOff", org.telegram.messenger.beta.R.string.EventLogToggledInvitesOff), "un1", fromUser2);
                            chat2 = chat;
                        }
                    } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionDeleteMessage) {
                        message = ((TLRPC.TL_channelAdminLogEventActionDeleteMessage) event.action).message;
                        this.messageText = replaceWithLink(LocaleController.getString("EventLogDeletedMessages", org.telegram.messenger.beta.R.string.EventLogDeletedMessages), "un1", fromUser2);
                        chat2 = chat;
                    } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangeLinkedChat) {
                        long newChatId = ((TLRPC.TL_channelAdminLogEventActionChangeLinkedChat) event.action).new_value;
                        long oldChatId = ((TLRPC.TL_channelAdminLogEventActionChangeLinkedChat) event.action).prev_value;
                        chat2 = chat;
                        if (chat2.megagroup) {
                            if (newChatId == 0) {
                                TLRPC.Chat oldChat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(oldChatId));
                                CharSequence replaceWithLink = replaceWithLink(LocaleController.getString("EventLogRemovedLinkedChannel", org.telegram.messenger.beta.R.string.EventLogRemovedLinkedChannel), "un1", fromUser2);
                                this.messageText = replaceWithLink;
                                this.messageText = replaceWithLink(replaceWithLink, "un2", oldChat);
                            } else {
                                TLRPC.Chat newChat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(newChatId));
                                CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString("EventLogChangedLinkedChannel", org.telegram.messenger.beta.R.string.EventLogChangedLinkedChannel), "un1", fromUser2);
                                this.messageText = replaceWithLink2;
                                this.messageText = replaceWithLink(replaceWithLink2, "un2", newChat);
                            }
                        } else if (newChatId == 0) {
                            TLRPC.Chat oldChat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(oldChatId));
                            CharSequence replaceWithLink3 = replaceWithLink(LocaleController.getString("EventLogRemovedLinkedGroup", org.telegram.messenger.beta.R.string.EventLogRemovedLinkedGroup), "un1", fromUser2);
                            this.messageText = replaceWithLink3;
                            this.messageText = replaceWithLink(replaceWithLink3, "un2", oldChat2);
                        } else {
                            TLRPC.Chat newChat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(newChatId));
                            CharSequence replaceWithLink4 = replaceWithLink(LocaleController.getString("EventLogChangedLinkedGroup", org.telegram.messenger.beta.R.string.EventLogChangedLinkedGroup), "un1", fromUser2);
                            this.messageText = replaceWithLink4;
                            this.messageText = replaceWithLink(replaceWithLink4, "un2", newChat2);
                        }
                    } else {
                        chat2 = chat;
                        if (event.action instanceof TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden) {
                            if (((TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden) event.action).new_value) {
                                this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOff", org.telegram.messenger.beta.R.string.EventLogToggledInvitesHistoryOff), "un1", fromUser2);
                            } else {
                                this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledInvitesHistoryOn", org.telegram.messenger.beta.R.string.EventLogToggledInvitesHistoryOn), "un1", fromUser2);
                            }
                        } else {
                            if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangeAbout) {
                                if (chat2.megagroup) {
                                    i5 = org.telegram.messenger.beta.R.string.EventLogEditedGroupDescription;
                                    str9 = "EventLogEditedGroupDescription";
                                } else {
                                    i5 = org.telegram.messenger.beta.R.string.EventLogEditedChannelDescription;
                                    str9 = "EventLogEditedChannelDescription";
                                }
                                this.messageText = replaceWithLink(LocaleController.getString(str9, i5), "un1", fromUser2);
                                message = new TLRPC.TL_message();
                                message.out = false;
                                message.unread = false;
                                message.from_id = new TLRPC.TL_peerUser();
                                message.from_id.user_id = event.user_id;
                                tL_peerChannel2 = tL_peerChannel;
                                message.peer_id = tL_peerChannel2;
                                message.date = event.date;
                                message.message = ((TLRPC.TL_channelAdminLogEventActionChangeAbout) event.action).new_value;
                                if (!TextUtils.isEmpty(((TLRPC.TL_channelAdminLogEventActionChangeAbout) event.action).prev_value)) {
                                    message.media = new TLRPC.TL_messageMediaWebPage();
                                    message.media.webpage = new TLRPC.TL_webPage();
                                    message.media.webpage.flags = 10;
                                    str5 = str;
                                    message.media.webpage.display_url = str5;
                                    message.media.webpage.url = str5;
                                    message.media.webpage.site_name = LocaleController.getString("EventLogPreviousGroupDescription", org.telegram.messenger.beta.R.string.EventLogPreviousGroupDescription);
                                    message.media.webpage.description = ((TLRPC.TL_channelAdminLogEventActionChangeAbout) event.action).prev_value;
                                } else {
                                    str5 = str;
                                    message.media = new TLRPC.TL_messageMediaEmpty();
                                }
                            } else {
                                str5 = str;
                                tL_peerChannel2 = tL_peerChannel;
                                if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangeTheme) {
                                    if (chat2.megagroup) {
                                        i4 = org.telegram.messenger.beta.R.string.EventLogEditedGroupTheme;
                                        str8 = "EventLogEditedGroupTheme";
                                    } else {
                                        i4 = org.telegram.messenger.beta.R.string.EventLogEditedChannelTheme;
                                        str8 = "EventLogEditedChannelTheme";
                                    }
                                    this.messageText = replaceWithLink(LocaleController.getString(str8, i4), "un1", fromUser2);
                                    message = new TLRPC.TL_message();
                                    message.out = false;
                                    message.unread = false;
                                    message.from_id = new TLRPC.TL_peerUser();
                                    message.from_id.user_id = event.user_id;
                                    message.peer_id = tL_peerChannel2;
                                    message.date = event.date;
                                    message.message = ((TLRPC.TL_channelAdminLogEventActionChangeTheme) event.action).new_value;
                                    if (!TextUtils.isEmpty(((TLRPC.TL_channelAdminLogEventActionChangeTheme) event.action).prev_value)) {
                                        message.media = new TLRPC.TL_messageMediaWebPage();
                                        message.media.webpage = new TLRPC.TL_webPage();
                                        message.media.webpage.flags = 10;
                                        message.media.webpage.display_url = str5;
                                        message.media.webpage.url = str5;
                                        message.media.webpage.site_name = LocaleController.getString("EventLogPreviousGroupTheme", org.telegram.messenger.beta.R.string.EventLogPreviousGroupTheme);
                                        message.media.webpage.description = ((TLRPC.TL_channelAdminLogEventActionChangeTheme) event.action).prev_value;
                                    } else {
                                        message.media = new TLRPC.TL_messageMediaEmpty();
                                    }
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangeUsername) {
                                    String newLink = ((TLRPC.TL_channelAdminLogEventActionChangeUsername) event.action).new_value;
                                    if (!TextUtils.isEmpty(newLink)) {
                                        if (chat2.megagroup) {
                                            i3 = org.telegram.messenger.beta.R.string.EventLogChangedGroupLink;
                                            str7 = "EventLogChangedGroupLink";
                                        } else {
                                            i3 = org.telegram.messenger.beta.R.string.EventLogChangedChannelLink;
                                            str7 = "EventLogChangedChannelLink";
                                        }
                                        this.messageText = replaceWithLink(LocaleController.getString(str7, i3), "un1", fromUser2);
                                    } else {
                                        if (chat2.megagroup) {
                                            i2 = org.telegram.messenger.beta.R.string.EventLogRemovedGroupLink;
                                            str6 = "EventLogRemovedGroupLink";
                                        } else {
                                            i2 = org.telegram.messenger.beta.R.string.EventLogRemovedChannelLink;
                                            str6 = "EventLogRemovedChannelLink";
                                        }
                                        this.messageText = replaceWithLink(LocaleController.getString(str6, i2), "un1", fromUser2);
                                    }
                                    TLRPC.Message message6 = new TLRPC.TL_message();
                                    message6.out = false;
                                    message6.unread = false;
                                    message6.from_id = new TLRPC.TL_peerUser();
                                    message6.from_id.user_id = event.user_id;
                                    message6.peer_id = tL_peerChannel2;
                                    message6.date = event.date;
                                    if (!TextUtils.isEmpty(newLink)) {
                                        message6.message = "https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + newLink;
                                    } else {
                                        message6.message = str5;
                                    }
                                    TLRPC.TL_messageEntityUrl url = new TLRPC.TL_messageEntityUrl();
                                    url.offset = 0;
                                    url.length = message6.message.length();
                                    message6.entities.add(url);
                                    if (!TextUtils.isEmpty(((TLRPC.TL_channelAdminLogEventActionChangeUsername) event.action).prev_value)) {
                                        message6.media = new TLRPC.TL_messageMediaWebPage();
                                        message6.media.webpage = new TLRPC.TL_webPage();
                                        message6.media.webpage.flags = 10;
                                        message6.media.webpage.display_url = str5;
                                        message6.media.webpage.url = str5;
                                        message6.media.webpage.site_name = LocaleController.getString("EventLogPreviousLink", org.telegram.messenger.beta.R.string.EventLogPreviousLink);
                                        message6.media.webpage.description = "https://" + MessagesController.getInstance(this.currentAccount).linkPrefix + "/" + ((TLRPC.TL_channelAdminLogEventActionChangeUsername) event.action).prev_value;
                                    } else {
                                        message6.media = new TLRPC.TL_messageMediaEmpty();
                                    }
                                    message = message6;
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionEditMessage) {
                                    message = new TLRPC.TL_message();
                                    message.out = false;
                                    message.unread = false;
                                    message.peer_id = tL_peerChannel2;
                                    message.date = event.date;
                                    TLRPC.Message newMessage = ((TLRPC.TL_channelAdminLogEventActionEditMessage) event.action).new_message;
                                    TLRPC.Message oldMessage = ((TLRPC.TL_channelAdminLogEventActionEditMessage) event.action).prev_message;
                                    if (newMessage != null && newMessage.from_id != null) {
                                        message.from_id = newMessage.from_id;
                                    } else {
                                        message.from_id = new TLRPC.TL_peerUser();
                                        message.from_id.user_id = event.user_id;
                                    }
                                    if (newMessage.media == null || (newMessage.media instanceof TLRPC.TL_messageMediaEmpty) || (newMessage.media instanceof TLRPC.TL_messageMediaWebPage)) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMessages", org.telegram.messenger.beta.R.string.EventLogEditedMessages), "un1", fromUser2);
                                        if (newMessage.action instanceof TLRPC.TL_messageActionGroupCall) {
                                            message = newMessage;
                                            message.media = new TLRPC.TL_messageMediaEmpty();
                                        } else {
                                            message.message = newMessage.message;
                                            message.media = new TLRPC.TL_messageMediaWebPage();
                                            message.media.webpage = new TLRPC.TL_webPage();
                                            message.media.webpage.site_name = LocaleController.getString("EventLogOriginalMessages", org.telegram.messenger.beta.R.string.EventLogOriginalMessages);
                                            if (TextUtils.isEmpty(oldMessage.message)) {
                                                message.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", org.telegram.messenger.beta.R.string.EventLogOriginalCaptionEmpty);
                                            } else {
                                                message.media.webpage.description = oldMessage.message;
                                            }
                                        }
                                    } else {
                                        if (!TextUtils.equals(newMessage.message, oldMessage.message)) {
                                            changedCaption = true;
                                        } else {
                                            changedCaption = false;
                                        }
                                        if (newMessage.media.getClass() != oldMessage.media.getClass() || ((newMessage.media.photo != null && oldMessage.media.photo != null && newMessage.media.photo.id != oldMessage.media.photo.id) || (newMessage.media.document != null && oldMessage.media.document != null && newMessage.media.document.id != oldMessage.media.document.id))) {
                                            changedMedia = true;
                                        } else {
                                            changedMedia = false;
                                        }
                                        if (changedMedia && changedCaption) {
                                            this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMediaCaption", org.telegram.messenger.beta.R.string.EventLogEditedMediaCaption), "un1", fromUser2);
                                        } else if (changedCaption) {
                                            this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedCaption", org.telegram.messenger.beta.R.string.EventLogEditedCaption), "un1", fromUser2);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("EventLogEditedMedia", org.telegram.messenger.beta.R.string.EventLogEditedMedia), "un1", fromUser2);
                                        }
                                        message.media = newMessage.media;
                                        if (changedCaption) {
                                            message.media.webpage = new TLRPC.TL_webPage();
                                            message.media.webpage.site_name = LocaleController.getString("EventLogOriginalCaption", org.telegram.messenger.beta.R.string.EventLogOriginalCaption);
                                            if (TextUtils.isEmpty(oldMessage.message)) {
                                                message.media.webpage.description = LocaleController.getString("EventLogOriginalCaptionEmpty", org.telegram.messenger.beta.R.string.EventLogOriginalCaptionEmpty);
                                            } else {
                                                message.media.webpage.description = oldMessage.message;
                                            }
                                        }
                                    }
                                    message.reply_markup = newMessage.reply_markup;
                                    if (message.media.webpage != null) {
                                        message.media.webpage.flags = 10;
                                        message.media.webpage.display_url = str5;
                                        message.media.webpage.url = str5;
                                    }
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangeStickerSet) {
                                    TLRPC.InputStickerSet newStickerset = ((TLRPC.TL_channelAdminLogEventActionChangeStickerSet) event.action).new_stickerset;
                                    TLRPC.InputStickerSet inputStickerSet = ((TLRPC.TL_channelAdminLogEventActionChangeStickerSet) event.action).new_stickerset;
                                    if (newStickerset == null || (newStickerset instanceof TLRPC.TL_inputStickerSetEmpty)) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedStickersSet", org.telegram.messenger.beta.R.string.EventLogRemovedStickersSet), "un1", fromUser2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogChangedStickersSet", org.telegram.messenger.beta.R.string.EventLogChangedStickersSet), "un1", fromUser2);
                                    }
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangeLocation) {
                                    TLRPC.TL_channelAdminLogEventActionChangeLocation location = (TLRPC.TL_channelAdminLogEventActionChangeLocation) event.action;
                                    if (location.new_value instanceof TLRPC.TL_channelLocationEmpty) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogRemovedLocation", org.telegram.messenger.beta.R.string.EventLogRemovedLocation), "un1", fromUser2);
                                    } else {
                                        TLRPC.TL_channelLocation channelLocation = (TLRPC.TL_channelLocation) location.new_value;
                                        this.messageText = replaceWithLink(LocaleController.formatString("EventLogChangedLocation", org.telegram.messenger.beta.R.string.EventLogChangedLocation, channelLocation.address), "un1", fromUser2);
                                    }
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionToggleSlowMode) {
                                    TLRPC.TL_channelAdminLogEventActionToggleSlowMode slowMode = (TLRPC.TL_channelAdminLogEventActionToggleSlowMode) event.action;
                                    if (slowMode.new_value == 0) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogToggledSlowmodeOff", org.telegram.messenger.beta.R.string.EventLogToggledSlowmodeOff), "un1", fromUser2);
                                    } else {
                                        if (slowMode.new_value < 60) {
                                            c2 = 0;
                                            string = LocaleController.formatPluralString("Seconds", slowMode.new_value, new Object[0]);
                                        } else {
                                            c2 = 0;
                                            string = slowMode.new_value < 3600 ? LocaleController.formatPluralString("Minutes", slowMode.new_value / 60, new Object[0]) : LocaleController.formatPluralString("Hours", (slowMode.new_value / 60) / 60, new Object[0]);
                                        }
                                        Object[] objArr = new Object[1];
                                        objArr[c2] = string;
                                        this.messageText = replaceWithLink(LocaleController.formatString("EventLogToggledSlowmodeOn", org.telegram.messenger.beta.R.string.EventLogToggledSlowmodeOn, objArr), "un1", fromUser2);
                                    }
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionStartGroupCall) {
                                    if (!ChatObject.isChannel(chat) || (chat2.megagroup && !chat2.gigagroup)) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogStartedVoiceChat", org.telegram.messenger.beta.R.string.EventLogStartedVoiceChat), "un1", fromUser2);
                                        str = str5;
                                        tL_peerChannel = tL_peerChannel2;
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogStartedLiveStream", org.telegram.messenger.beta.R.string.EventLogStartedLiveStream), "un1", fromUser2);
                                        str = str5;
                                        tL_peerChannel = tL_peerChannel2;
                                    }
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionDiscardGroupCall) {
                                    if (!ChatObject.isChannel(chat) || (chat2.megagroup && !chat2.gigagroup)) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEndedVoiceChat", org.telegram.messenger.beta.R.string.EventLogEndedVoiceChat), "un1", fromUser2);
                                        str = str5;
                                        tL_peerChannel = tL_peerChannel2;
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogEndedLiveStream", org.telegram.messenger.beta.R.string.EventLogEndedLiveStream), "un1", fromUser2);
                                        str = str5;
                                        tL_peerChannel = tL_peerChannel2;
                                    }
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantMute) {
                                    long id = getPeerId(((TLRPC.TL_channelAdminLogEventActionParticipantMute) event.action).participant.peer);
                                    if (id > 0) {
                                        object3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(id));
                                    } else {
                                        object3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-id));
                                    }
                                    CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString("EventLogVoiceChatMuted", org.telegram.messenger.beta.R.string.EventLogVoiceChatMuted), "un1", fromUser2);
                                    this.messageText = replaceWithLink5;
                                    this.messageText = replaceWithLink(replaceWithLink5, "un2", object3);
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantUnmute) {
                                    long id2 = getPeerId(((TLRPC.TL_channelAdminLogEventActionParticipantUnmute) event.action).participant.peer);
                                    if (id2 > 0) {
                                        object2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(id2));
                                    } else {
                                        object2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-id2));
                                    }
                                    CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString("EventLogVoiceChatUnmuted", org.telegram.messenger.beta.R.string.EventLogVoiceChatUnmuted), "un1", fromUser2);
                                    this.messageText = replaceWithLink6;
                                    this.messageText = replaceWithLink(replaceWithLink6, "un2", object2);
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionToggleGroupCallSetting) {
                                    if (((TLRPC.TL_channelAdminLogEventActionToggleGroupCallSetting) event.action).join_muted) {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogVoiceChatNotAllowedToSpeak", org.telegram.messenger.beta.R.string.EventLogVoiceChatNotAllowedToSpeak), "un1", fromUser2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("EventLogVoiceChatAllowedToSpeak", org.telegram.messenger.beta.R.string.EventLogVoiceChatAllowedToSpeak), "un1", fromUser2);
                                    }
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantJoinByInvite) {
                                    TLRPC.TL_channelAdminLogEventActionParticipantJoinByInvite tL_channelAdminLogEventActionParticipantJoinByInvite = (TLRPC.TL_channelAdminLogEventActionParticipantJoinByInvite) event.action;
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionInviteUser", org.telegram.messenger.beta.R.string.ActionInviteUser), "un1", fromUser2);
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionToggleNoForwards) {
                                    TLRPC.TL_channelAdminLogEventActionToggleNoForwards action3 = (TLRPC.TL_channelAdminLogEventActionToggleNoForwards) event.action;
                                    boolean isChannel = ChatObject.isChannel(chat) && !chat2.megagroup;
                                    if (action3.new_value) {
                                        if (isChannel) {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionForwardsRestrictedChannel", org.telegram.messenger.beta.R.string.ActionForwardsRestrictedChannel), "un1", fromUser2);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionForwardsRestrictedGroup", org.telegram.messenger.beta.R.string.ActionForwardsRestrictedGroup), "un1", fromUser2);
                                        }
                                    } else if (isChannel) {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionForwardsEnabledChannel", org.telegram.messenger.beta.R.string.ActionForwardsEnabledChannel), "un1", fromUser2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionForwardsEnabledGroup", org.telegram.messenger.beta.R.string.ActionForwardsEnabledGroup), "un1", fromUser2);
                                    }
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionExportedInviteDelete) {
                                    CharSequence replaceWithLink7 = replaceWithLink(LocaleController.formatString("ActionDeletedInviteLinkClickable", org.telegram.messenger.beta.R.string.ActionDeletedInviteLinkClickable, new Object[0]), "un1", fromUser2);
                                    this.messageText = replaceWithLink7;
                                    this.messageText = replaceWithLink(replaceWithLink7, "un2", ((TLRPC.TL_channelAdminLogEventActionExportedInviteDelete) event.action).invite);
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionExportedInviteRevoke) {
                                    TLRPC.TL_channelAdminLogEventActionExportedInviteRevoke action4 = (TLRPC.TL_channelAdminLogEventActionExportedInviteRevoke) event.action;
                                    CharSequence replaceWithLink8 = replaceWithLink(LocaleController.formatString("ActionRevokedInviteLinkClickable", org.telegram.messenger.beta.R.string.ActionRevokedInviteLinkClickable, action4.invite.link), "un1", fromUser2);
                                    this.messageText = replaceWithLink8;
                                    this.messageText = replaceWithLink(replaceWithLink8, "un2", action4.invite);
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionExportedInviteEdit) {
                                    TLRPC.TL_channelAdminLogEventActionExportedInviteEdit action5 = (TLRPC.TL_channelAdminLogEventActionExportedInviteEdit) event.action;
                                    if (action5.prev_invite.link == null || !action5.prev_invite.link.equals(action5.new_invite.link)) {
                                        this.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkClickable", org.telegram.messenger.beta.R.string.ActionEditedInviteLinkClickable, new Object[0]), "un1", fromUser2);
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.formatString("ActionEditedInviteLinkToSameClickable", org.telegram.messenger.beta.R.string.ActionEditedInviteLinkToSameClickable, new Object[0]), "un1", fromUser2);
                                    }
                                    CharSequence replaceWithLink9 = replaceWithLink(this.messageText, "un2", action5.prev_invite);
                                    this.messageText = replaceWithLink9;
                                    this.messageText = replaceWithLink(replaceWithLink9, "un3", action5.new_invite);
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantVolume) {
                                    TLRPC.TL_channelAdminLogEventActionParticipantVolume action6 = (TLRPC.TL_channelAdminLogEventActionParticipantVolume) event.action;
                                    long id3 = getPeerId(action6.participant.peer);
                                    if (id3 > 0) {
                                        object = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(id3));
                                    } else {
                                        object = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-id3));
                                    }
                                    double participantVolume = ChatObject.getParticipantVolume(action6.participant);
                                    Double.isNaN(participantVolume);
                                    double vol = participantVolume / 100.0d;
                                    Object[] objArr2 = new Object[1];
                                    objArr2[0] = Integer.valueOf((int) (vol > FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE ? Math.max(vol, 1.0d) : FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE));
                                    CharSequence replaceWithLink10 = replaceWithLink(LocaleController.formatString("ActionVolumeChanged", org.telegram.messenger.beta.R.string.ActionVolumeChanged, objArr2), "un1", fromUser2);
                                    this.messageText = replaceWithLink10;
                                    this.messageText = replaceWithLink(replaceWithLink10, "un2", object);
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangeHistoryTTL) {
                                    TLRPC.TL_channelAdminLogEventActionChangeHistoryTTL action7 = (TLRPC.TL_channelAdminLogEventActionChangeHistoryTTL) event.action;
                                    if (!chat2.megagroup) {
                                        if (action7.new_value != 0) {
                                            this.messageText = LocaleController.formatString("ActionTTLChannelChanged", org.telegram.messenger.beta.R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(action7.new_value));
                                        } else {
                                            this.messageText = LocaleController.getString("ActionTTLChannelDisabled", org.telegram.messenger.beta.R.string.ActionTTLChannelDisabled);
                                        }
                                    } else if (action7.new_value == 0) {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionTTLDisabled", org.telegram.messenger.beta.R.string.ActionTTLDisabled), "un1", fromUser2);
                                    } else {
                                        if (action7.new_value > 86400) {
                                            c = 0;
                                            time = LocaleController.formatPluralString("Days", action7.new_value / 86400, new Object[0]);
                                        } else {
                                            c = 0;
                                            if (action7.new_value >= 3600) {
                                                time = LocaleController.formatPluralString("Hours", action7.new_value / 3600, new Object[0]);
                                            } else {
                                                time = action7.new_value >= 60 ? LocaleController.formatPluralString("Minutes", action7.new_value / 60, new Object[0]) : LocaleController.formatPluralString("Seconds", action7.new_value, new Object[0]);
                                            }
                                        }
                                        Object[] objArr3 = new Object[1];
                                        objArr3[c] = time;
                                        this.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", org.telegram.messenger.beta.R.string.ActionTTLChanged, objArr3), "un1", fromUser2);
                                    }
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantJoinByRequest) {
                                    TLRPC.TL_channelAdminLogEventActionParticipantJoinByRequest action8 = (TLRPC.TL_channelAdminLogEventActionParticipantJoinByRequest) event.action;
                                    if (((action8.invite instanceof TLRPC.TL_chatInviteExported) && "https://t.me/+PublicChat".equals(((TLRPC.TL_chatInviteExported) action8.invite).link)) || (action8.invite instanceof TLRPC.TL_chatInvitePublicJoinRequests)) {
                                        CharSequence replaceWithLink11 = replaceWithLink(LocaleController.getString("JoinedViaRequestApproved", org.telegram.messenger.beta.R.string.JoinedViaRequestApproved), "un1", fromUser2);
                                        this.messageText = replaceWithLink11;
                                        this.messageText = replaceWithLink(replaceWithLink11, "un2", MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(action8.approved_by)));
                                    } else {
                                        CharSequence replaceWithLink12 = replaceWithLink(LocaleController.getString("JoinedViaInviteLinkApproved", org.telegram.messenger.beta.R.string.JoinedViaInviteLinkApproved), "un1", fromUser2);
                                        this.messageText = replaceWithLink12;
                                        CharSequence replaceWithLink13 = replaceWithLink(replaceWithLink12, "un2", action8.invite);
                                        this.messageText = replaceWithLink13;
                                        this.messageText = replaceWithLink(replaceWithLink13, "un3", MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(action8.approved_by)));
                                    }
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionSendMessage) {
                                    message = ((TLRPC.TL_channelAdminLogEventActionSendMessage) event.action).message;
                                    this.messageText = replaceWithLink(LocaleController.getString("EventLogSendMessages", org.telegram.messenger.beta.R.string.EventLogSendMessages), "un1", fromUser2);
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else if (event.action instanceof TLRPC.TL_channelAdminLogEventActionChangeAvailableReactions) {
                                    String oldReactions = TextUtils.join(", ", ((TLRPC.TL_channelAdminLogEventActionChangeAvailableReactions) event.action).prev_value);
                                    String newReactions = TextUtils.join(", ", ((TLRPC.TL_channelAdminLogEventActionChangeAvailableReactions) event.action).new_value);
                                    this.messageText = replaceWithLink(LocaleController.formatString("ActionReactionsChanged", org.telegram.messenger.beta.R.string.ActionReactionsChanged, oldReactions, newReactions), "un1", fromUser2);
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                } else {
                                    this.messageText = "unsupported " + event.action;
                                    str = str5;
                                    tL_peerChannel = tL_peerChannel2;
                                }
                            }
                            str = str5;
                            tL_peerChannel = tL_peerChannel2;
                        }
                    }
                }
                if (this.messageOwner == null) {
                    this.messageOwner = new TLRPC.TL_messageService();
                }
                this.messageOwner.message = this.messageText.toString();
                this.messageOwner.from_id = new TLRPC.TL_peerUser();
                this.messageOwner.from_id.user_id = event.user_id;
                this.messageOwner.date = event.date;
                TLRPC.Message message7 = this.messageOwner;
                int i6 = mid[0];
                mid[0] = i6 + 1;
                message7.id = i6;
                this.eventId = event.id;
                this.messageOwner.out = false;
                this.messageOwner.peer_id = new TLRPC.TL_peerChannel();
                this.messageOwner.peer_id.channel_id = chat2.id;
                this.messageOwner.unread = false;
                MediaController mediaController = MediaController.getInstance();
                if (message instanceof TLRPC.TL_messageEmpty) {
                    message2 = message;
                } else {
                    message2 = null;
                }
                if (message2 != null) {
                    arrayList = messageObjects;
                    str2 = str;
                    iArr = null;
                } else {
                    message2.out = false;
                    int i7 = mid[0];
                    mid[0] = i7 + 1;
                    message2.id = i7;
                    message2.flags &= -9;
                    iArr = null;
                    message2.reply_to = null;
                    message2.flags &= -32769;
                    MessageObject messageObject = new MessageObject(this.currentAccount, message2, (AbstractMap<Long, TLRPC.User>) null, (AbstractMap<Long, TLRPC.Chat>) null, true, true, this.eventId);
                    if (messageObject.contentType >= 0) {
                        if (mediaController.isPlayingMessage(messageObject)) {
                            MessageObject player = mediaController.getPlayingMessageObject();
                            messageObject.audioProgress = player.audioProgress;
                            messageObject.audioProgressSec = player.audioProgressSec;
                        }
                        str2 = str;
                        createDateArray(this.currentAccount, event, messageObjects, messagesByDays, addToEnd);
                        if (addToEnd) {
                            arrayList = messageObjects;
                            arrayList.add(0, messageObject);
                        } else {
                            arrayList = messageObjects;
                            arrayList.add(messageObjects.size() - 1, messageObject);
                        }
                    } else {
                        arrayList = messageObjects;
                        str2 = str;
                        this.contentType = -1;
                    }
                }
                if (this.contentType >= 0) {
                    return;
                }
                ArrayList<MessageObject> arrayList2 = arrayList;
                createDateArray(this.currentAccount, event, messageObjects, messagesByDays, addToEnd);
                if (addToEnd) {
                    arrayList2.add(0, this);
                } else {
                    arrayList2.add(messageObjects.size() - 1, this);
                }
                if (this.messageText == null) {
                    this.messageText = str2;
                }
                setType();
                measureInlineBotButtons();
                generateCaption();
                if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                    paint = Theme.chat_msgGameTextPaint;
                } else {
                    paint = Theme.chat_msgTextPaint;
                }
                int[] emojiOnly = allowsBigEmoji() ? new int[1] : iArr;
                this.messageText = Emoji.replaceEmoji(this.messageText, paint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, emojiOnly, this.contentType == 0, this.viewRef);
                checkEmojiOnly(emojiOnly);
                if (mediaController.isPlayingMessage(this)) {
                    MessageObject player2 = mediaController.getPlayingMessageObject();
                    this.audioProgress = player2.audioProgress;
                    this.audioProgressSec = player2.audioProgressSec;
                }
                generateLayout(fromUser2);
                this.layoutCreated = true;
                generateThumbs(false);
                checkMediaExistance();
                return;
            } else {
                TLRPC.TL_channelAdminLogEventActionParticipantToggleBan action9 = (TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) event.action;
                this.messageOwner = new TLRPC.TL_message();
                long peerId = getPeerId(action9.prev_participant.peer);
                if (peerId > 0) {
                    whoUser2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId));
                    fromUser3 = fromUser;
                } else {
                    fromUser3 = fromUser;
                    whoUser2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId));
                }
                TLRPC.TL_chatBannedRights o2 = action9.prev_participant.banned_rights;
                TLRPC.TL_chatBannedRights n2 = action9.new_participant.banned_rights;
                if (!chat2.megagroup) {
                    str = "";
                    tL_peerChannel = tL_peerChannel3;
                } else {
                    if (n2 != null && n2.view_messages) {
                        if (o2 == null) {
                        }
                        str = "";
                        tL_peerChannel = tL_peerChannel3;
                    }
                    if (n2 == null || AndroidUtilities.isBannedForever(n2)) {
                        str = "";
                        tL_peerChannel = tL_peerChannel3;
                        bannedDuration = new StringBuilder(LocaleController.getString("UserRestrictionsUntilForever", org.telegram.messenger.beta.R.string.UserRestrictionsUntilForever));
                    } else {
                        StringBuilder bannedDuration3 = new StringBuilder();
                        int duration = n2.until_date - event.date;
                        int days = ((duration / 60) / 60) / 24;
                        int duration2 = duration - (((days * 60) * 60) * 24);
                        int hours = (duration2 / 60) / 60;
                        str = "";
                        int minutes = (duration2 - ((hours * 60) * 60)) / 60;
                        int count2 = 0;
                        int duration3 = 0;
                        while (true) {
                            tL_peerChannel = tL_peerChannel3;
                            if (duration3 >= 3) {
                                bannedDuration2 = bannedDuration3;
                                break;
                            } else if (duration3 == 0) {
                                if (days != 0) {
                                    addStr = LocaleController.formatPluralString("Days", days, new Object[0]);
                                    count = count2 + 1;
                                    if (addStr != null) {
                                        if (bannedDuration3.length() > 0) {
                                            bannedDuration3.append(", ");
                                        }
                                        bannedDuration3.append(addStr);
                                    }
                                    bannedDuration2 = bannedDuration3;
                                    if (count != 2) {
                                        break;
                                    }
                                    duration3++;
                                    count2 = count;
                                    bannedDuration3 = bannedDuration2;
                                    tL_peerChannel3 = tL_peerChannel;
                                }
                                addStr = null;
                                count = count2;
                                if (addStr != null) {
                                }
                                bannedDuration2 = bannedDuration3;
                                if (count != 2) {
                                }
                            } else if (duration3 == 1) {
                                if (hours != 0) {
                                    addStr = LocaleController.formatPluralString("Hours", hours, new Object[0]);
                                    count = count2 + 1;
                                    if (addStr != null) {
                                    }
                                    bannedDuration2 = bannedDuration3;
                                    if (count != 2) {
                                    }
                                } else {
                                    addStr = null;
                                    count = count2;
                                    if (addStr != null) {
                                    }
                                    bannedDuration2 = bannedDuration3;
                                    if (count != 2) {
                                    }
                                }
                            } else {
                                if (minutes != 0) {
                                    addStr = LocaleController.formatPluralString("Minutes", minutes, new Object[0]);
                                    count = count2 + 1;
                                    if (addStr != null) {
                                    }
                                    bannedDuration2 = bannedDuration3;
                                    if (count != 2) {
                                    }
                                }
                                addStr = null;
                                count = count2;
                                if (addStr != null) {
                                }
                                bannedDuration2 = bannedDuration3;
                                if (count != 2) {
                                }
                            }
                        }
                        bannedDuration = bannedDuration2;
                    }
                    String str11 = LocaleController.getString("EventLogRestrictedUntil", org.telegram.messenger.beta.R.string.EventLogRestrictedUntil);
                    int offset = str11.indexOf("%1$s");
                    StringBuilder rights3 = new StringBuilder(String.format(str11, getUserName(whoUser2, this.messageOwner.entities, offset), bannedDuration.toString()));
                    boolean added2 = false;
                    o2 = o2 == null ? new TLRPC.TL_chatBannedRights() : o2;
                    n2 = n2 == null ? new TLRPC.TL_chatBannedRights() : n2;
                    if (o2.view_messages != n2.view_messages) {
                        rights3.append('\n');
                        added2 = true;
                        rights3.append('\n');
                        rights3.append(!n2.view_messages ? '+' : '-');
                        rights3.append(' ');
                        rights3.append(LocaleController.getString("EventLogRestrictedReadMessages", org.telegram.messenger.beta.R.string.EventLogRestrictedReadMessages));
                    }
                    if (o2.send_messages != n2.send_messages) {
                        if (added2) {
                            c10 = '\n';
                        } else {
                            c10 = '\n';
                            rights3.append('\n');
                            added2 = true;
                        }
                        rights3.append(c10);
                        rights3.append(!n2.send_messages ? '+' : '-');
                        rights3.append(' ');
                        rights3.append(LocaleController.getString("EventLogRestrictedSendMessages", org.telegram.messenger.beta.R.string.EventLogRestrictedSendMessages));
                    }
                    if (o2.send_stickers != n2.send_stickers || o2.send_inline != n2.send_inline || o2.send_gifs != n2.send_gifs || o2.send_games != n2.send_games) {
                        if (added2) {
                            c9 = '\n';
                        } else {
                            c9 = '\n';
                            rights3.append('\n');
                            added2 = true;
                        }
                        rights3.append(c9);
                        rights3.append(!n2.send_stickers ? '+' : '-');
                        rights3.append(' ');
                        rights3.append(LocaleController.getString("EventLogRestrictedSendStickers", org.telegram.messenger.beta.R.string.EventLogRestrictedSendStickers));
                    }
                    if (o2.send_media != n2.send_media) {
                        if (added2) {
                            c8 = '\n';
                        } else {
                            c8 = '\n';
                            rights3.append('\n');
                            added2 = true;
                        }
                        rights3.append(c8);
                        rights3.append(!n2.send_media ? '+' : '-');
                        rights3.append(' ');
                        rights3.append(LocaleController.getString("EventLogRestrictedSendMedia", org.telegram.messenger.beta.R.string.EventLogRestrictedSendMedia));
                    }
                    if (o2.send_polls != n2.send_polls) {
                        if (added2) {
                            c7 = '\n';
                        } else {
                            c7 = '\n';
                            rights3.append('\n');
                            added2 = true;
                        }
                        rights3.append(c7);
                        rights3.append(!n2.send_polls ? '+' : '-');
                        rights3.append(' ');
                        rights3.append(LocaleController.getString("EventLogRestrictedSendPolls", org.telegram.messenger.beta.R.string.EventLogRestrictedSendPolls));
                    }
                    if (o2.embed_links != n2.embed_links) {
                        if (added2) {
                            c6 = '\n';
                        } else {
                            c6 = '\n';
                            rights3.append('\n');
                            added2 = true;
                        }
                        rights3.append(c6);
                        rights3.append(!n2.embed_links ? '+' : '-');
                        rights3.append(' ');
                        rights3.append(LocaleController.getString("EventLogRestrictedSendEmbed", org.telegram.messenger.beta.R.string.EventLogRestrictedSendEmbed));
                    }
                    if (o2.change_info != n2.change_info) {
                        if (added2) {
                            c5 = '\n';
                        } else {
                            c5 = '\n';
                            rights3.append('\n');
                            added2 = true;
                        }
                        rights3.append(c5);
                        rights3.append(!n2.change_info ? '+' : '-');
                        rights3.append(' ');
                        rights3.append(LocaleController.getString("EventLogRestrictedChangeInfo", org.telegram.messenger.beta.R.string.EventLogRestrictedChangeInfo));
                    }
                    if (o2.invite_users != n2.invite_users) {
                        if (added2) {
                            c4 = '\n';
                        } else {
                            c4 = '\n';
                            rights3.append('\n');
                            added2 = true;
                        }
                        rights3.append(c4);
                        rights3.append(!n2.invite_users ? '+' : '-');
                        rights3.append(' ');
                        rights3.append(LocaleController.getString("EventLogRestrictedInviteUsers", org.telegram.messenger.beta.R.string.EventLogRestrictedInviteUsers));
                    }
                    if (o2.pin_messages != n2.pin_messages) {
                        if (added2) {
                            c3 = '\n';
                        } else {
                            c3 = '\n';
                            rights3.append('\n');
                        }
                        rights3.append(c3);
                        rights3.append(!n2.pin_messages ? '+' : '-');
                        rights3.append(' ');
                        rights3.append(LocaleController.getString("EventLogRestrictedPinMessages", org.telegram.messenger.beta.R.string.EventLogRestrictedPinMessages));
                    }
                    this.messageText = rights3.toString();
                    chat2 = chat;
                    fromUser2 = fromUser3;
                }
                if (n2 != null && (o2 == null || n2.view_messages)) {
                    str10 = LocaleController.getString("EventLogChannelRestricted", org.telegram.messenger.beta.R.string.EventLogChannelRestricted);
                } else {
                    str10 = LocaleController.getString("EventLogChannelUnrestricted", org.telegram.messenger.beta.R.string.EventLogChannelUnrestricted);
                }
                int offset2 = str10.indexOf("%1$s");
                this.messageText = String.format(str10, getUserName(whoUser2, this.messageOwner.entities, offset2));
                chat2 = chat;
                fromUser2 = fromUser3;
            }
            if (event.action instanceof TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin) {
                TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin action10 = (TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin) event.action;
                prev_participant = action10.prev_participant;
                new_participant = action10.new_participant;
            } else {
                TLRPC.TL_channelAdminLogEventActionParticipantToggleBan action11 = (TLRPC.TL_channelAdminLogEventActionParticipantToggleBan) event.action;
                prev_participant = action11.prev_participant;
                new_participant = action11.new_participant;
            }
            this.messageOwner = new TLRPC.TL_message();
            long peerId2 = getPeerId(prev_participant.peer);
            if (peerId2 > 0) {
                whoUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId2));
            } else {
                whoUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(-peerId2));
            }
            if (!(prev_participant instanceof TLRPC.TL_channelParticipantCreator) && (new_participant instanceof TLRPC.TL_channelParticipantCreator)) {
                String str12 = LocaleController.getString("EventLogChangedOwnership", org.telegram.messenger.beta.R.string.EventLogChangedOwnership);
                int offset3 = str12.indexOf("%1$s");
                rights = new StringBuilder(String.format(str12, getUserName(whoUser, this.messageOwner.entities, offset3)));
                str = "";
                tL_peerChannel = tL_peerChannel3;
            } else {
                TLRPC.TL_chatAdminRights o3 = prev_participant.admin_rights;
                TLRPC.TL_chatAdminRights n3 = new_participant.admin_rights;
                o3 = o3 == null ? new TLRPC.TL_chatAdminRights() : o3;
                n3 = n3 == null ? new TLRPC.TL_chatAdminRights() : n3;
                if (n3.other) {
                    str3 = LocaleController.getString("EventLogPromotedNoRights", org.telegram.messenger.beta.R.string.EventLogPromotedNoRights);
                } else {
                    str3 = LocaleController.getString("EventLogPromoted", org.telegram.messenger.beta.R.string.EventLogPromoted);
                }
                int offset4 = str3.indexOf("%1$s");
                str = "";
                tL_peerChannel = tL_peerChannel3;
                StringBuilder rights4 = new StringBuilder(String.format(str3, getUserName(whoUser, this.messageOwner.entities, offset4)));
                rights4.append("\n");
                if (!TextUtils.equals(prev_participant.rank, new_participant.rank)) {
                    if (TextUtils.isEmpty(new_participant.rank)) {
                        rights4.append('\n');
                        rights4.append('-');
                        rights4.append(' ');
                        rights4.append(LocaleController.getString("EventLogPromotedRemovedTitle", org.telegram.messenger.beta.R.string.EventLogPromotedRemovedTitle));
                    } else {
                        rights4.append('\n');
                        rights4.append('+');
                        rights4.append(' ');
                        rights4.append(LocaleController.formatString("EventLogPromotedTitle", org.telegram.messenger.beta.R.string.EventLogPromotedTitle, new_participant.rank));
                    }
                }
                if (o3.change_info != n3.change_info) {
                    rights4.append('\n');
                    rights4.append(n3.change_info ? '+' : '-');
                    rights4.append(' ');
                    if (chat2.megagroup) {
                        i = org.telegram.messenger.beta.R.string.EventLogPromotedChangeGroupInfo;
                        str4 = "EventLogPromotedChangeGroupInfo";
                    } else {
                        i = org.telegram.messenger.beta.R.string.EventLogPromotedChangeChannelInfo;
                        str4 = "EventLogPromotedChangeChannelInfo";
                    }
                    rights4.append(LocaleController.getString(str4, i));
                }
                if (!chat2.megagroup) {
                    if (o3.post_messages != n3.post_messages) {
                        rights4.append('\n');
                        rights4.append(n3.post_messages ? '+' : '-');
                        rights4.append(' ');
                        rights4.append(LocaleController.getString("EventLogPromotedPostMessages", org.telegram.messenger.beta.R.string.EventLogPromotedPostMessages));
                    }
                    if (o3.edit_messages != n3.edit_messages) {
                        rights4.append('\n');
                        rights4.append(n3.edit_messages ? '+' : '-');
                        rights4.append(' ');
                        rights4.append(LocaleController.getString("EventLogPromotedEditMessages", org.telegram.messenger.beta.R.string.EventLogPromotedEditMessages));
                    }
                }
                if (o3.delete_messages != n3.delete_messages) {
                    rights4.append('\n');
                    rights4.append(n3.delete_messages ? '+' : '-');
                    rights4.append(' ');
                    rights4.append(LocaleController.getString("EventLogPromotedDeleteMessages", org.telegram.messenger.beta.R.string.EventLogPromotedDeleteMessages));
                }
                if (o3.add_admins != n3.add_admins) {
                    rights4.append('\n');
                    rights4.append(n3.add_admins ? '+' : '-');
                    rights4.append(' ');
                    rights4.append(LocaleController.getString("EventLogPromotedAddAdmins", org.telegram.messenger.beta.R.string.EventLogPromotedAddAdmins));
                }
                if (o3.anonymous != n3.anonymous) {
                    rights4.append('\n');
                    rights4.append(n3.anonymous ? '+' : '-');
                    rights4.append(' ');
                    rights4.append(LocaleController.getString("EventLogPromotedSendAnonymously", org.telegram.messenger.beta.R.string.EventLogPromotedSendAnonymously));
                }
                if (chat2.megagroup) {
                    if (o3.ban_users != n3.ban_users) {
                        rights4.append('\n');
                        rights4.append(n3.ban_users ? '+' : '-');
                        rights4.append(' ');
                        rights4.append(LocaleController.getString("EventLogPromotedBanUsers", org.telegram.messenger.beta.R.string.EventLogPromotedBanUsers));
                    }
                    if (o3.manage_call != n3.manage_call) {
                        rights4.append('\n');
                        rights4.append(n3.manage_call ? '+' : '-');
                        rights4.append(' ');
                        rights4.append(LocaleController.getString("EventLogPromotedManageCall", org.telegram.messenger.beta.R.string.EventLogPromotedManageCall));
                    }
                }
                if (o3.invite_users != n3.invite_users) {
                    rights4.append('\n');
                    rights4.append(n3.invite_users ? '+' : '-');
                    rights4.append(' ');
                    rights4.append(LocaleController.getString("EventLogPromotedAddUsers", org.telegram.messenger.beta.R.string.EventLogPromotedAddUsers));
                }
                if (chat2.megagroup && o3.pin_messages != n3.pin_messages) {
                    rights4.append('\n');
                    rights4.append(n3.pin_messages ? '+' : '-');
                    rights4.append(' ');
                    rights4.append(LocaleController.getString("EventLogPromotedPinMessages", org.telegram.messenger.beta.R.string.EventLogPromotedPinMessages));
                }
                rights = rights4;
            }
            this.messageText = rights.toString();
        } else {
            TLRPC.TL_messageService tL_messageService2 = new TLRPC.TL_messageService();
            this.messageOwner = tL_messageService2;
            message3 = null;
            tL_messageService2.action = new TLRPC.TL_messageActionChatAddUser();
            long peerId3 = getPeerId(((TLRPC.TL_channelAdminLogEventActionParticipantInvite) event.action).participant.peer);
            TLObject whoUser3 = peerId3 > 0 ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerId3)) : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerId3));
            if ((this.messageOwner.from_id instanceof TLRPC.TL_peerUser) && peerId3 == this.messageOwner.from_id.user_id) {
                if (chat2.megagroup) {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogGroupJoined", org.telegram.messenger.beta.R.string.EventLogGroupJoined), "un1", fromUser);
                } else {
                    this.messageText = replaceWithLink(LocaleController.getString("EventLogChannelJoined", org.telegram.messenger.beta.R.string.EventLogChannelJoined), "un1", fromUser);
                }
            } else {
                CharSequence replaceWithLink14 = replaceWithLink(LocaleController.getString("EventLogAdded", org.telegram.messenger.beta.R.string.EventLogAdded), "un2", whoUser3);
                this.messageText = replaceWithLink14;
                this.messageText = replaceWithLink(replaceWithLink14, "un1", fromUser);
            }
            str = "";
            tL_peerChannel = tL_peerChannel3;
            fromUser2 = fromUser;
        }
        message = message3;
        if (this.messageOwner == null) {
        }
        this.messageOwner.message = this.messageText.toString();
        this.messageOwner.from_id = new TLRPC.TL_peerUser();
        this.messageOwner.from_id.user_id = event.user_id;
        this.messageOwner.date = event.date;
        TLRPC.Message message72 = this.messageOwner;
        int i62 = mid[0];
        mid[0] = i62 + 1;
        message72.id = i62;
        this.eventId = event.id;
        this.messageOwner.out = false;
        this.messageOwner.peer_id = new TLRPC.TL_peerChannel();
        this.messageOwner.peer_id.channel_id = chat2.id;
        this.messageOwner.unread = false;
        MediaController mediaController2 = MediaController.getInstance();
        if (message instanceof TLRPC.TL_messageEmpty) {
        }
        if (message2 != null) {
        }
        if (this.contentType >= 0) {
        }
    }

    private String getUserName(TLObject object, ArrayList<TLRPC.MessageEntity> entities, int offset) {
        long id;
        String name;
        String name2;
        String name3;
        if (object == null) {
            name2 = "";
            name = null;
            id = 0;
        } else if (object instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) object;
            if (user.deleted) {
                name3 = LocaleController.getString("HiddenName", org.telegram.messenger.beta.R.string.HiddenName);
            } else {
                name3 = ContactsController.formatName(user.first_name, user.last_name);
            }
            String username = user.username;
            long id2 = user.id;
            name2 = name3;
            name = username;
            id = id2;
        } else {
            TLRPC.Chat chat = (TLRPC.Chat) object;
            String name4 = chat.title;
            String username2 = chat.username;
            name2 = name4;
            name = username2;
            id = -chat.id;
        }
        if (offset >= 0) {
            TLRPC.TL_messageEntityMentionName entity = new TLRPC.TL_messageEntityMentionName();
            entity.user_id = id;
            entity.offset = offset;
            entity.length = name2.length();
            entities.add(entity);
        }
        if (!TextUtils.isEmpty(name)) {
            if (offset >= 0) {
                TLRPC.TL_messageEntityMentionName entity2 = new TLRPC.TL_messageEntityMentionName();
                entity2.user_id = id;
                entity2.offset = name2.length() + offset + 2;
                entity2.length = name.length() + 1;
                entities.add(entity2);
            }
            return String.format("%1$s (@%2$s)", name2, name);
        }
        return name2;
    }

    public void applyNewText() {
        applyNewText(this.messageOwner.message);
    }

    public void applyNewText(CharSequence text) {
        TextPaint paint;
        if (TextUtils.isEmpty(text)) {
            return;
        }
        TLRPC.User fromUser = null;
        if (isFromUser()) {
            fromUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
        }
        this.messageText = text;
        if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
            paint = Theme.chat_msgGameTextPaint;
        } else {
            paint = Theme.chat_msgTextPaint;
        }
        int[] emojiOnly = allowsBigEmoji() ? new int[1] : null;
        this.messageText = Emoji.replaceEmoji(this.messageText, paint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, emojiOnly, this.contentType == 0, this.viewRef);
        checkEmojiOnly(emojiOnly);
        generateLayout(fromUser);
    }

    private boolean allowsBigEmoji() {
        if (!SharedConfig.allowBigEmoji) {
            return false;
        }
        TLRPC.Message message = this.messageOwner;
        if (message == null || message.peer_id == null || (this.messageOwner.peer_id.channel_id == 0 && this.messageOwner.peer_id.chat_id == 0)) {
            return true;
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        int i = (this.messageOwner.peer_id.channel_id > 0L ? 1 : (this.messageOwner.peer_id.channel_id == 0L ? 0 : -1));
        TLRPC.Peer peer = this.messageOwner.peer_id;
        TLRPC.Chat chat = messagesController.getChat(Long.valueOf(i != 0 ? peer.channel_id : peer.chat_id));
        return (chat != null && chat.gigagroup) || !ChatObject.isActionBanned(chat, 8) || ChatObject.hasAdminRights(chat);
    }

    public void generateGameMessageText(TLRPC.User fromUser) {
        if (fromUser == null && isFromUser()) {
            fromUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
        }
        TLRPC.TL_game game = null;
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null && messageObject.messageOwner.media != null && this.replyMessageObject.messageOwner.media.game != null) {
            game = this.replyMessageObject.messageOwner.media.game;
        }
        if (game == null) {
            if (fromUser == null || fromUser.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScored", org.telegram.messenger.beta.R.string.ActionUserScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", fromUser);
                return;
            } else {
                this.messageText = LocaleController.formatString("ActionYouScored", org.telegram.messenger.beta.R.string.ActionYouScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
                return;
            }
        }
        if (fromUser == null || fromUser.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScoredInGame", org.telegram.messenger.beta.R.string.ActionUserScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0])), "un1", fromUser);
        } else {
            this.messageText = LocaleController.formatString("ActionYouScoredInGame", org.telegram.messenger.beta.R.string.ActionYouScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score, new Object[0]));
        }
        this.messageText = replaceWithLink(this.messageText, "un2", game);
    }

    public boolean hasValidReplyMessageObject() {
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            if (!(message instanceof TLRPC.TL_messageEmpty) && !(message.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                return true;
            }
        }
        return false;
    }

    public void generatePaymentSentMessageText(TLRPC.User fromUser) {
        String name;
        String currency;
        if (fromUser == null) {
            fromUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(getDialogId()));
        }
        if (fromUser != null) {
            name = UserObject.getFirstName(fromUser);
        } else {
            name = "";
        }
        try {
            currency = LocaleController.getInstance().formatCurrencyString(this.messageOwner.action.total_amount, this.messageOwner.action.currency);
        } catch (Exception e) {
            FileLog.e(e);
            currency = "<error>";
        }
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null && (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaInvoice)) {
            if (this.messageOwner.action.recurring_init) {
                this.messageText = LocaleController.formatString(org.telegram.messenger.beta.R.string.PaymentSuccessfullyPaidRecurrent, currency, name, this.replyMessageObject.messageOwner.media.title);
            } else {
                this.messageText = LocaleController.formatString("PaymentSuccessfullyPaid", org.telegram.messenger.beta.R.string.PaymentSuccessfullyPaid, currency, name, this.replyMessageObject.messageOwner.media.title);
            }
        } else if (this.messageOwner.action.recurring_init) {
            this.messageText = LocaleController.formatString(org.telegram.messenger.beta.R.string.PaymentSuccessfullyPaidNoItemRecurrent, currency, name);
        } else {
            this.messageText = LocaleController.formatString("PaymentSuccessfullyPaidNoItem", org.telegram.messenger.beta.R.string.PaymentSuccessfullyPaidNoItem, currency, name);
        }
    }

    public void generatePinMessageText(TLRPC.User fromUser, TLRPC.Chat chat) {
        if (fromUser == null && chat == 0) {
            if (isFromUser()) {
                fromUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
            }
            if (fromUser == null) {
                if (this.messageOwner.peer_id instanceof TLRPC.TL_peerChannel) {
                    chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.channel_id));
                } else if (this.messageOwner.peer_id instanceof TLRPC.TL_peerChat) {
                    chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.chat_id));
                }
            }
        }
        MessageObject messageObject = this.replyMessageObject;
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            if (!(message instanceof TLRPC.TL_messageEmpty) && !(message.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                if (!this.replyMessageObject.isMusic()) {
                    if (!this.replyMessageObject.isVideo()) {
                        if (!this.replyMessageObject.isGif()) {
                            if (!this.replyMessageObject.isVoice()) {
                                if (!this.replyMessageObject.isRoundVideo()) {
                                    if ((!this.replyMessageObject.isSticker() && !this.replyMessageObject.isAnimatedSticker()) || this.replyMessageObject.isAnimatedEmoji()) {
                                        if (!(this.replyMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaDocument)) {
                                            if (!(this.replyMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeo)) {
                                                if (!(this.replyMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive)) {
                                                    if (!(this.replyMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaContact)) {
                                                        if (this.replyMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                                            if (((TLRPC.TL_messageMediaPoll) this.replyMessageObject.messageOwner.media).poll.quiz) {
                                                                this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedQuiz", org.telegram.messenger.beta.R.string.ActionPinnedQuiz), "un1", fromUser != null ? fromUser : chat);
                                                                return;
                                                            } else {
                                                                this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedPoll", org.telegram.messenger.beta.R.string.ActionPinnedPoll), "un1", fromUser != null ? fromUser : chat);
                                                                return;
                                                            }
                                                        } else if (!(this.replyMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto)) {
                                                            if (this.replyMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                                                CharSequence replaceWithLink = replaceWithLink(LocaleController.formatString("ActionPinnedGame", org.telegram.messenger.beta.R.string.ActionPinnedGame, "🎮 " + this.replyMessageObject.messageOwner.media.game.title), "un1", fromUser != null ? fromUser : chat);
                                                                this.messageText = replaceWithLink;
                                                                this.messageText = Emoji.replaceEmoji(replaceWithLink, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
                                                                return;
                                                            }
                                                            CharSequence charSequence = this.replyMessageObject.messageText;
                                                            if (charSequence != null && charSequence.length() > 0) {
                                                                CharSequence mess = this.replyMessageObject.messageText;
                                                                if (mess.length() > 20) {
                                                                    mess = ((Object) mess.subSequence(0, 20)) + "...";
                                                                }
                                                                CharSequence mess2 = Emoji.replaceEmoji(mess, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
                                                                MediaDataController.addTextStyleRuns(this.replyMessageObject, (Spannable) mess2);
                                                                this.messageText = replaceWithLink(AndroidUtilities.formatSpannable(LocaleController.getString("ActionPinnedText", org.telegram.messenger.beta.R.string.ActionPinnedText), mess2), "un1", fromUser != null ? fromUser : chat);
                                                                return;
                                                            }
                                                            this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedNoText", org.telegram.messenger.beta.R.string.ActionPinnedNoText), "un1", fromUser != null ? fromUser : chat);
                                                            return;
                                                        } else {
                                                            this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedPhoto", org.telegram.messenger.beta.R.string.ActionPinnedPhoto), "un1", fromUser != null ? fromUser : chat);
                                                            return;
                                                        }
                                                    }
                                                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedContact", org.telegram.messenger.beta.R.string.ActionPinnedContact), "un1", fromUser != null ? fromUser : chat);
                                                    return;
                                                }
                                                this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedGeoLive", org.telegram.messenger.beta.R.string.ActionPinnedGeoLive), "un1", fromUser != null ? fromUser : chat);
                                                return;
                                            }
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedGeo", org.telegram.messenger.beta.R.string.ActionPinnedGeo), "un1", fromUser != null ? fromUser : chat);
                                            return;
                                        }
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedFile", org.telegram.messenger.beta.R.string.ActionPinnedFile), "un1", fromUser != null ? fromUser : chat);
                                        return;
                                    }
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedSticker", org.telegram.messenger.beta.R.string.ActionPinnedSticker), "un1", fromUser != null ? fromUser : chat);
                                    return;
                                }
                                this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedRound", org.telegram.messenger.beta.R.string.ActionPinnedRound), "un1", fromUser != null ? fromUser : chat);
                                return;
                            }
                            this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedVoice", org.telegram.messenger.beta.R.string.ActionPinnedVoice), "un1", fromUser != null ? fromUser : chat);
                            return;
                        }
                        this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedGif", org.telegram.messenger.beta.R.string.ActionPinnedGif), "un1", fromUser != null ? fromUser : chat);
                        return;
                    }
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedVideo", org.telegram.messenger.beta.R.string.ActionPinnedVideo), "un1", fromUser != null ? fromUser : chat);
                    return;
                }
                this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedMusic", org.telegram.messenger.beta.R.string.ActionPinnedMusic), "un1", fromUser != null ? fromUser : chat);
                return;
            }
        }
        this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedNoText", org.telegram.messenger.beta.R.string.ActionPinnedNoText), "un1", fromUser != null ? fromUser : chat);
    }

    public static void updateReactions(TLRPC.Message message, TLRPC.TL_messageReactions reactions) {
        if (message == null || reactions == null) {
            return;
        }
        if (reactions.min && message.reactions != null) {
            int a = 0;
            int N = message.reactions.results.size();
            while (true) {
                if (a >= N) {
                    break;
                }
                TLRPC.TL_reactionCount reaction = message.reactions.results.get(a);
                if (!reaction.chosen) {
                    a++;
                } else {
                    int b = 0;
                    int N2 = reactions.results.size();
                    while (true) {
                        if (b >= N2) {
                            break;
                        }
                        TLRPC.TL_reactionCount newReaction = reactions.results.get(b);
                        if (!reaction.reaction.equals(newReaction.reaction)) {
                            b++;
                        } else {
                            newReaction.chosen = true;
                            break;
                        }
                    }
                }
            }
        }
        message.reactions = reactions;
        message.flags |= 1048576;
    }

    public boolean hasReactions() {
        return this.messageOwner.reactions != null && !this.messageOwner.reactions.results.isEmpty();
    }

    public static void updatePollResults(TLRPC.TL_messageMediaPoll media, TLRPC.PollResults results) {
        if (media == null || results == null) {
            return;
        }
        if ((results.flags & 2) != 0) {
            ArrayList<byte[]> chosen = null;
            byte[] correct = null;
            if (results.min && media.results.results != null) {
                int N2 = media.results.results.size();
                for (int b = 0; b < N2; b++) {
                    TLRPC.TL_pollAnswerVoters answerVoters = media.results.results.get(b);
                    if (answerVoters.chosen) {
                        if (chosen == null) {
                            chosen = new ArrayList<>();
                        }
                        chosen.add(answerVoters.option);
                    }
                    if (answerVoters.correct) {
                        correct = answerVoters.option;
                    }
                }
            }
            media.results.results = results.results;
            if (chosen != null || correct != null) {
                int N22 = media.results.results.size();
                for (int b2 = 0; b2 < N22; b2++) {
                    TLRPC.TL_pollAnswerVoters answerVoters2 = media.results.results.get(b2);
                    if (chosen != null) {
                        int a = 0;
                        int N = chosen.size();
                        while (true) {
                            if (a < N) {
                                if (!Arrays.equals(answerVoters2.option, chosen.get(a))) {
                                    a++;
                                } else {
                                    answerVoters2.chosen = true;
                                    chosen.remove(a);
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        if (chosen.isEmpty()) {
                            chosen = null;
                        }
                    }
                    if (correct != null && Arrays.equals(answerVoters2.option, correct)) {
                        answerVoters2.correct = true;
                        correct = null;
                    }
                    if (chosen == null && correct == null) {
                        break;
                    }
                }
            }
            media.results.flags |= 2;
        }
        if ((results.flags & 4) != 0) {
            media.results.total_voters = results.total_voters;
            media.results.flags |= 4;
        }
        if ((results.flags & 8) != 0) {
            media.results.recent_voters = results.recent_voters;
            media.results.flags |= 8;
        }
        if ((results.flags & 16) != 0) {
            media.results.solution = results.solution;
            media.results.solution_entities = results.solution_entities;
            media.results.flags |= 16;
        }
    }

    public boolean isPollClosed() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC.TL_messageMediaPoll) this.messageOwner.media).poll.closed;
    }

    public boolean isQuiz() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC.TL_messageMediaPoll) this.messageOwner.media).poll.quiz;
    }

    public boolean isPublicPoll() {
        if (this.type != 17) {
            return false;
        }
        return ((TLRPC.TL_messageMediaPoll) this.messageOwner.media).poll.public_voters;
    }

    public boolean isPoll() {
        return this.type == 17;
    }

    public boolean canUnvote() {
        if (this.type != 17) {
            return false;
        }
        TLRPC.TL_messageMediaPoll mediaPoll = (TLRPC.TL_messageMediaPoll) this.messageOwner.media;
        if (mediaPoll.results == null || mediaPoll.results.results.isEmpty() || mediaPoll.poll.quiz) {
            return false;
        }
        int N = mediaPoll.results.results.size();
        for (int a = 0; a < N; a++) {
            TLRPC.TL_pollAnswerVoters answer = mediaPoll.results.results.get(a);
            if (answer.chosen) {
                return true;
            }
        }
        return false;
    }

    public boolean isVoted() {
        if (this.type != 17) {
            return false;
        }
        TLRPC.TL_messageMediaPoll mediaPoll = (TLRPC.TL_messageMediaPoll) this.messageOwner.media;
        if (mediaPoll.results == null || mediaPoll.results.results.isEmpty()) {
            return false;
        }
        int N = mediaPoll.results.results.size();
        for (int a = 0; a < N; a++) {
            TLRPC.TL_pollAnswerVoters answer = mediaPoll.results.results.get(a);
            if (answer.chosen) {
                return true;
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
        return ((TLRPC.TL_messageMediaPoll) this.messageOwner.media).poll.id;
    }

    private TLRPC.Photo getPhotoWithId(TLRPC.WebPage webPage, long id) {
        if (webPage == null || webPage.cached_page == null) {
            return null;
        }
        if (webPage.photo != null && webPage.photo.id == id) {
            return webPage.photo;
        }
        for (int a = 0; a < webPage.cached_page.photos.size(); a++) {
            TLRPC.Photo photo = webPage.cached_page.photos.get(a);
            if (photo.id == id) {
                return photo;
            }
        }
        return null;
    }

    private TLRPC.Document getDocumentWithId(TLRPC.WebPage webPage, long id) {
        if (webPage == null || webPage.cached_page == null) {
            return null;
        }
        if (webPage.document != null && webPage.document.id == id) {
            return webPage.document;
        }
        for (int a = 0; a < webPage.cached_page.documents.size(); a++) {
            TLRPC.Document document = webPage.cached_page.documents.get(a);
            if (document.id == id) {
                return document;
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
        if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
            TLRPC.Chat chat = getChat(null, null, this.messageOwner.peer_id.channel_id);
            if (chat == null) {
                return false;
            }
            Boolean valueOf = Boolean.valueOf(chat.megagroup);
            this.cachedIsSupergroup = valueOf;
            return valueOf.booleanValue();
        }
        this.cachedIsSupergroup = false;
        return false;
    }

    private MessageObject getMessageObjectForBlock(TLRPC.WebPage webPage, TLRPC.PageBlock pageBlock) {
        TLRPC.TL_message message = null;
        if (pageBlock instanceof TLRPC.TL_pageBlockPhoto) {
            TLRPC.TL_pageBlockPhoto pageBlockPhoto = (TLRPC.TL_pageBlockPhoto) pageBlock;
            TLRPC.Photo photo = getPhotoWithId(webPage, pageBlockPhoto.photo_id);
            if (photo == webPage.photo) {
                return this;
            }
            message = new TLRPC.TL_message();
            message.media = new TLRPC.TL_messageMediaPhoto();
            message.media.photo = photo;
        } else if (pageBlock instanceof TLRPC.TL_pageBlockVideo) {
            TLRPC.TL_pageBlockVideo pageBlockVideo = (TLRPC.TL_pageBlockVideo) pageBlock;
            TLRPC.Document document = getDocumentWithId(webPage, pageBlockVideo.video_id);
            if (document == webPage.document) {
                return this;
            }
            message = new TLRPC.TL_message();
            message.media = new TLRPC.TL_messageMediaDocument();
            message.media.document = getDocumentWithId(webPage, pageBlockVideo.video_id);
        }
        message.message = "";
        message.realId = getId();
        message.id = Utilities.random.nextInt();
        message.date = this.messageOwner.date;
        message.peer_id = this.messageOwner.peer_id;
        message.out = this.messageOwner.out;
        message.from_id = this.messageOwner.from_id;
        return new MessageObject(this.currentAccount, message, false, true);
    }

    public ArrayList<MessageObject> getWebPagePhotos(ArrayList<MessageObject> array, ArrayList<TLRPC.PageBlock> blocksToSearch) {
        ArrayList<MessageObject> messageObjects = array == null ? new ArrayList<>() : array;
        if (this.messageOwner.media == null || this.messageOwner.media.webpage == null) {
            return messageObjects;
        }
        TLRPC.WebPage webPage = this.messageOwner.media.webpage;
        if (webPage.cached_page == null) {
            return messageObjects;
        }
        ArrayList<TLRPC.PageBlock> blocks = blocksToSearch == null ? webPage.cached_page.blocks : blocksToSearch;
        for (int a = 0; a < blocks.size(); a++) {
            TLRPC.PageBlock block = blocks.get(a);
            if (block instanceof TLRPC.TL_pageBlockSlideshow) {
                TLRPC.TL_pageBlockSlideshow slideshow = (TLRPC.TL_pageBlockSlideshow) block;
                for (int b = 0; b < slideshow.items.size(); b++) {
                    messageObjects.add(getMessageObjectForBlock(webPage, slideshow.items.get(b)));
                }
            } else if (block instanceof TLRPC.TL_pageBlockCollage) {
                TLRPC.TL_pageBlockCollage slideshow2 = (TLRPC.TL_pageBlockCollage) block;
                for (int b2 = 0; b2 < slideshow2.items.size(); b2++) {
                    messageObjects.add(getMessageObjectForBlock(webPage, slideshow2.items.get(b2)));
                }
            }
        }
        return messageObjects;
    }

    public void createMessageSendInfo() {
        String param;
        if (this.messageOwner.message != null) {
            if ((this.messageOwner.id < 0 || isEditing()) && this.messageOwner.params != null) {
                String param2 = this.messageOwner.params.get("ve");
                if (param2 != null && (isVideo() || isNewGif() || isRoundVideo())) {
                    VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
                    this.videoEditedInfo = videoEditedInfo;
                    if (!videoEditedInfo.parseString(param2)) {
                        this.videoEditedInfo = null;
                    } else {
                        this.videoEditedInfo.roundVideo = isRoundVideo();
                    }
                }
                if (this.messageOwner.send_state == 3 && (param = this.messageOwner.params.get("prevMedia")) != null) {
                    SerializedData serializedData = new SerializedData(Base64.decode(param, 0));
                    int constructor = serializedData.readInt32(false);
                    this.previousMedia = TLRPC.MessageMedia.TLdeserialize(serializedData, constructor, false);
                    this.previousMessage = serializedData.readString(false);
                    this.previousAttachPath = serializedData.readString(false);
                    int count = serializedData.readInt32(false);
                    this.previousMessageEntities = new ArrayList<>(count);
                    for (int a = 0; a < count; a++) {
                        int constructor2 = serializedData.readInt32(false);
                        TLRPC.MessageEntity entity = TLRPC.MessageEntity.TLdeserialize(serializedData, constructor2, false);
                        this.previousMessageEntities.add(entity);
                    }
                    serializedData.cleanup();
                }
            }
        }
    }

    public void measureInlineBotButtons() {
        CharSequence text;
        float width;
        if (!this.isRestrictedMessage) {
            this.wantedBotKeyboardWidth = 0;
            if ((this.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) || (this.messageOwner.reactions != null && !this.messageOwner.reactions.results.isEmpty())) {
                Theme.createCommonMessageResources();
                StringBuilder sb = this.botButtonsLayout;
                if (sb == null) {
                    this.botButtonsLayout = new StringBuilder();
                } else {
                    sb.setLength(0);
                }
            }
            float f = 2000.0f;
            float f2 = 15.0f;
            if (this.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) {
                int a = 0;
                while (a < this.messageOwner.reply_markup.rows.size()) {
                    TLRPC.TL_keyboardButtonRow row = this.messageOwner.reply_markup.rows.get(a);
                    int maxButtonSize = 0;
                    int size = row.buttons.size();
                    int b = 0;
                    while (b < size) {
                        TLRPC.KeyboardButton button = row.buttons.get(b);
                        StringBuilder sb2 = this.botButtonsLayout;
                        sb2.append(a);
                        sb2.append(b);
                        if ((button instanceof TLRPC.TL_keyboardButtonBuy) && (this.messageOwner.media.flags & 4) != 0) {
                            text = LocaleController.getString("PaymentReceipt", org.telegram.messenger.beta.R.string.PaymentReceipt);
                        } else {
                            String str = button.text;
                            if (str == null) {
                                str = "";
                            }
                            text = Emoji.replaceEmoji(str, Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(f2), false, this.contentType == 0, this.viewRef);
                        }
                        StaticLayout staticLayout = new StaticLayout(text, Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        if (staticLayout.getLineCount() > 0) {
                            float width2 = staticLayout.getLineWidth(0);
                            float left = staticLayout.getLineLeft(0);
                            if (left >= width2) {
                                width = width2;
                            } else {
                                width = width2 - left;
                            }
                            maxButtonSize = Math.max(maxButtonSize, ((int) Math.ceil(width)) + AndroidUtilities.dp(4.0f));
                        }
                        b++;
                        f = 2000.0f;
                        f2 = 15.0f;
                    }
                    this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, ((AndroidUtilities.dp(12.0f) + maxButtonSize) * size) + (AndroidUtilities.dp(5.0f) * (size - 1)));
                    a++;
                    f = 2000.0f;
                    f2 = 15.0f;
                }
            } else if (this.messageOwner.reactions != null) {
                int size2 = this.messageOwner.reactions.results.size();
                for (int a2 = 0; a2 < size2; a2++) {
                    TLRPC.TL_reactionCount reactionCount = this.messageOwner.reactions.results.get(a2);
                    int maxButtonSize2 = 0;
                    StringBuilder sb3 = this.botButtonsLayout;
                    sb3.append(0);
                    sb3.append(a2);
                    CharSequence text2 = Emoji.replaceEmoji(String.format("%d %s", Integer.valueOf(reactionCount.count), reactionCount.reaction), Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false, this.contentType == 0, this.viewRef);
                    StaticLayout staticLayout2 = new StaticLayout(text2, Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(2000.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    if (staticLayout2.getLineCount() > 0) {
                        float width3 = staticLayout2.getLineWidth(0);
                        float left2 = staticLayout2.getLineLeft(0);
                        if (left2 < width3) {
                            width3 -= left2;
                        }
                        maxButtonSize2 = Math.max(0, ((int) Math.ceil(width3)) + AndroidUtilities.dp(4.0f));
                    }
                    this.wantedBotKeyboardWidth = Math.max(this.wantedBotKeyboardWidth, ((AndroidUtilities.dp(12.0f) + maxButtonSize2) * size2) + (AndroidUtilities.dp(5.0f) * (size2 - 1)));
                }
            }
        }
    }

    public boolean isVideoAvatar() {
        return (this.messageOwner.action == null || this.messageOwner.action.photo == null || this.messageOwner.action.photo.video_sizes.isEmpty()) ? false : true;
    }

    public boolean isFcmMessage() {
        return this.localType != 0;
    }

    private TLRPC.User getUser(AbstractMap<Long, TLRPC.User> users, LongSparseArray<TLRPC.User> sUsers, long userId) {
        TLRPC.User user = null;
        if (users != null) {
            TLRPC.User user2 = users.get(Long.valueOf(userId));
            user = user2;
        } else if (sUsers != null) {
            TLRPC.User user3 = sUsers.get(userId);
            user = user3;
        }
        if (user == null) {
            TLRPC.User user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(userId));
            return user4;
        }
        return user;
    }

    private TLRPC.Chat getChat(AbstractMap<Long, TLRPC.Chat> chats, LongSparseArray<TLRPC.Chat> sChats, long chatId) {
        TLRPC.Chat chat = null;
        if (chats != null) {
            TLRPC.Chat chat2 = chats.get(Long.valueOf(chatId));
            chat = chat2;
        } else if (sChats != null) {
            TLRPC.Chat chat3 = sChats.get(chatId);
            chat = chat3;
        }
        if (chat == null) {
            TLRPC.Chat chat4 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(chatId));
            return chat4;
        }
        return chat;
    }

    private void updateMessageText(AbstractMap<Long, TLRPC.User> users, AbstractMap<Long, TLRPC.Chat> chats, LongSparseArray<TLRPC.User> sUsers, LongSparseArray<TLRPC.Chat> sChats) {
        TLRPC.User user;
        TLRPC.User fromUser;
        TLRPC.User fromUser2;
        String str;
        TLRPC.Chat chat;
        String date;
        long singleUserId;
        TLRPC.Chat chat2;
        String str2;
        TLObject from;
        String str3;
        TLRPC.User user2;
        TLObject to;
        long singleUserId2;
        String time;
        if (this.messageOwner.from_id instanceof TLRPC.TL_peerUser) {
            fromUser = getUser(users, sUsers, this.messageOwner.from_id.user_id);
            user = null;
        } else if (this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) {
            TLRPC.Chat fromChat = getChat(chats, sChats, this.messageOwner.from_id.channel_id);
            fromUser = null;
            user = fromChat;
        } else {
            fromUser = null;
            user = null;
        }
        TLRPC.User user3 = fromUser != null ? fromUser : user;
        TLRPC.Message message = this.messageOwner;
        String str4 = "";
        if (!(message instanceof TLRPC.TL_messageService)) {
            this.isRestrictedMessage = false;
            String restrictionReason = MessagesController.getRestrictionReason(message.restriction_reason);
            if (!TextUtils.isEmpty(restrictionReason)) {
                this.messageText = restrictionReason;
                this.isRestrictedMessage = true;
            } else if (!isMediaEmpty()) {
                if (this.messageOwner.media instanceof TLRPC.TL_messageMediaDice) {
                    this.messageText = getDiceEmoji();
                } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                    if (((TLRPC.TL_messageMediaPoll) this.messageOwner.media).poll.quiz) {
                        this.messageText = LocaleController.getString("QuizPoll", org.telegram.messenger.beta.R.string.QuizPoll);
                    } else {
                        this.messageText = LocaleController.getString("Poll", org.telegram.messenger.beta.R.string.Poll);
                    }
                } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                    if (this.messageOwner.media.ttl_seconds == 0 || (this.messageOwner instanceof TLRPC.TL_message_secret)) {
                        this.messageText = LocaleController.getString("AttachPhoto", org.telegram.messenger.beta.R.string.AttachPhoto);
                    } else {
                        this.messageText = LocaleController.getString("AttachDestructingPhoto", org.telegram.messenger.beta.R.string.AttachDestructingPhoto);
                    }
                } else if (isVideo() || ((this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) && (getDocument() instanceof TLRPC.TL_documentEmpty) && this.messageOwner.media.ttl_seconds != 0)) {
                    if (this.messageOwner.media.ttl_seconds == 0 || (this.messageOwner instanceof TLRPC.TL_message_secret)) {
                        this.messageText = LocaleController.getString("AttachVideo", org.telegram.messenger.beta.R.string.AttachVideo);
                    } else {
                        this.messageText = LocaleController.getString("AttachDestructingVideo", org.telegram.messenger.beta.R.string.AttachDestructingVideo);
                    }
                } else if (isVoice()) {
                    this.messageText = LocaleController.getString("AttachAudio", org.telegram.messenger.beta.R.string.AttachAudio);
                } else if (isRoundVideo()) {
                    this.messageText = LocaleController.getString("AttachRound", org.telegram.messenger.beta.R.string.AttachRound);
                } else if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaGeo) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaVenue)) {
                    this.messageText = LocaleController.getString("AttachLocation", org.telegram.messenger.beta.R.string.AttachLocation);
                } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive) {
                    this.messageText = LocaleController.getString("AttachLiveLocation", org.telegram.messenger.beta.R.string.AttachLiveLocation);
                } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaContact) {
                    this.messageText = LocaleController.getString("AttachContact", org.telegram.messenger.beta.R.string.AttachContact);
                    if (!TextUtils.isEmpty(this.messageOwner.media.vcard)) {
                        this.vCardData = VCardData.parse(this.messageOwner.media.vcard);
                    }
                } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                    this.messageText = this.messageOwner.message;
                } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaInvoice) {
                    this.messageText = this.messageOwner.media.description;
                } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaUnsupported) {
                    this.messageText = LocaleController.getString("UnsupportedMedia", org.telegram.messenger.beta.R.string.UnsupportedMedia);
                } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                    if (isSticker() || isAnimatedStickerDocument(getDocument(), true)) {
                        String sch = getStickerChar();
                        if (sch == null || sch.length() <= 0) {
                            this.messageText = LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
                        } else {
                            this.messageText = String.format("%s %s", sch, LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker));
                        }
                    } else if (isMusic()) {
                        this.messageText = LocaleController.getString("AttachMusic", org.telegram.messenger.beta.R.string.AttachMusic);
                    } else if (isGif()) {
                        this.messageText = LocaleController.getString("AttachGif", org.telegram.messenger.beta.R.string.AttachGif);
                    } else {
                        String name = FileLoader.getDocumentFileName(getDocument());
                        if (TextUtils.isEmpty(name)) {
                            this.messageText = LocaleController.getString("AttachDocument", org.telegram.messenger.beta.R.string.AttachDocument);
                        } else {
                            this.messageText = name;
                        }
                    }
                }
            } else if (this.messageOwner.message == null) {
                this.messageText = this.messageOwner.message;
            } else {
                try {
                    if (this.messageOwner.message.length() > 200) {
                        this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_LONG_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                    } else {
                        this.messageText = AndroidUtilities.BAD_CHARS_MESSAGE_PATTERN.matcher(this.messageOwner.message).replaceAll("\u200c");
                    }
                } catch (Throwable th) {
                    this.messageText = this.messageOwner.message;
                }
            }
        } else if (message.action != null) {
            if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionGroupCallScheduled)) {
                if (this.messageOwner.action instanceof TLRPC.TL_messageActionGroupCall) {
                    if (this.messageOwner.action.duration != 0) {
                        int days = this.messageOwner.action.duration / 86400;
                        if (days > 0) {
                            time = LocaleController.formatPluralString("Days", days, new Object[0]);
                        } else {
                            int hours = this.messageOwner.action.duration / 3600;
                            if (hours > 0) {
                                time = LocaleController.formatPluralString("Hours", hours, new Object[0]);
                            } else {
                                int minutes = this.messageOwner.action.duration / 60;
                                if (minutes > 0) {
                                    time = LocaleController.formatPluralString("Minutes", minutes, new Object[0]);
                                } else {
                                    time = LocaleController.formatPluralString("Seconds", this.messageOwner.action.duration, new Object[0]);
                                }
                            }
                        }
                        if (!(this.messageOwner.peer_id instanceof TLRPC.TL_peerChat) && !isSupergroup()) {
                            this.messageText = LocaleController.formatString("ActionChannelCallEnded", org.telegram.messenger.beta.R.string.ActionChannelCallEnded, time);
                        } else if (isOut()) {
                            this.messageText = LocaleController.formatString("ActionGroupCallEndedByYou", org.telegram.messenger.beta.R.string.ActionGroupCallEndedByYou, time);
                        } else {
                            this.messageText = replaceWithLink(LocaleController.formatString("ActionGroupCallEndedBy", org.telegram.messenger.beta.R.string.ActionGroupCallEndedBy, time), "un1", user3);
                        }
                    } else if (!(this.messageOwner.peer_id instanceof TLRPC.TL_peerChat) && !isSupergroup()) {
                        this.messageText = LocaleController.getString("ActionChannelCallJustStarted", org.telegram.messenger.beta.R.string.ActionChannelCallJustStarted);
                    } else if (isOut()) {
                        this.messageText = LocaleController.getString("ActionGroupCallStartedByYou", org.telegram.messenger.beta.R.string.ActionGroupCallStartedByYou);
                    } else {
                        this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallStarted", org.telegram.messenger.beta.R.string.ActionGroupCallStarted), "un1", user3);
                    }
                } else if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionInviteToGroupCall)) {
                    if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionGeoProximityReached)) {
                        TLRPC.User fromUser3 = fromUser;
                        if (this.messageOwner.action instanceof TLRPC.TL_messageActionCustomAction) {
                            this.messageText = this.messageOwner.action.message;
                            str4 = str4;
                        } else if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionChatCreate)) {
                            if (this.messageOwner.action instanceof TLRPC.TL_messageActionChatDeleteUser) {
                                if (!isFromUser() || this.messageOwner.action.user_id != this.messageOwner.from_id.user_id) {
                                    TLRPC.User whoUser = getUser(users, sUsers, this.messageOwner.action.user_id);
                                    if (isOut()) {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionYouKickUser", org.telegram.messenger.beta.R.string.ActionYouKickUser), "un2", whoUser);
                                    } else if (this.messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionKickUserYou", org.telegram.messenger.beta.R.string.ActionKickUserYou), "un1", user3);
                                    } else {
                                        CharSequence replaceWithLink = replaceWithLink(LocaleController.getString("ActionKickUser", org.telegram.messenger.beta.R.string.ActionKickUser), "un2", whoUser);
                                        this.messageText = replaceWithLink;
                                        this.messageText = replaceWithLink(replaceWithLink, "un1", user3);
                                    }
                                    str4 = str4;
                                } else if (isOut()) {
                                    this.messageText = LocaleController.getString("ActionYouLeftUser", org.telegram.messenger.beta.R.string.ActionYouLeftUser);
                                    str4 = str4;
                                } else {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionLeftUser", org.telegram.messenger.beta.R.string.ActionLeftUser), "un1", user3);
                                    str4 = str4;
                                }
                            } else if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionChatAddUser)) {
                                if (this.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                                    if (isOut()) {
                                        this.messageText = LocaleController.getString("ActionInviteYou", org.telegram.messenger.beta.R.string.ActionInviteYou);
                                        str4 = str4;
                                    } else {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionInviteUser", org.telegram.messenger.beta.R.string.ActionInviteUser), "un1", user3);
                                        str4 = str4;
                                    }
                                } else {
                                    TLRPC.Chat chat3 = null;
                                    if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionChatEditPhoto)) {
                                        if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionChatEditTitle)) {
                                            if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionChatDeletePhoto)) {
                                                if (this.messageOwner.action instanceof TLRPC.TL_messageActionTTLChange) {
                                                    if (this.messageOwner.action.ttl != 0) {
                                                        if (isOut()) {
                                                            this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", org.telegram.messenger.beta.R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(this.messageOwner.action.ttl));
                                                            str4 = str4;
                                                        } else {
                                                            this.messageText = LocaleController.formatString("MessageLifetimeChanged", org.telegram.messenger.beta.R.string.MessageLifetimeChanged, UserObject.getFirstName(fromUser3), LocaleController.formatTTLString(this.messageOwner.action.ttl));
                                                            str4 = str4;
                                                        }
                                                    } else if (isOut()) {
                                                        this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", org.telegram.messenger.beta.R.string.MessageLifetimeYouRemoved);
                                                        str4 = str4;
                                                    } else {
                                                        this.messageText = LocaleController.formatString("MessageLifetimeRemoved", org.telegram.messenger.beta.R.string.MessageLifetimeRemoved, UserObject.getFirstName(fromUser3));
                                                        str4 = str4;
                                                    }
                                                } else if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionSetMessagesTTL)) {
                                                    if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionLoginUnknownLocation)) {
                                                        str4 = str4;
                                                        if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionUserJoined)) {
                                                            if (this.messageOwner.action instanceof TLRPC.TL_messageActionContactSignUp) {
                                                                fromUser2 = fromUser3;
                                                            } else if (this.messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                                                                this.messageText = LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.beta.R.string.NotificationContactNewPhoto, UserObject.getUserName(fromUser3));
                                                            } else if (this.messageOwner.action instanceof TLRPC.TL_messageEncryptedAction) {
                                                                if (this.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                                                                    if (isOut()) {
                                                                        this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", org.telegram.messenger.beta.R.string.ActionTakeScreenshootYou, new Object[0]);
                                                                    } else {
                                                                        this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", org.telegram.messenger.beta.R.string.ActionTakeScreenshoot), "un1", user3);
                                                                    }
                                                                } else if (this.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                                                                    TLRPC.TL_decryptedMessageActionSetMessageTTL action = (TLRPC.TL_decryptedMessageActionSetMessageTTL) this.messageOwner.action.encryptedAction;
                                                                    if (action.ttl_seconds != 0) {
                                                                        if (isOut()) {
                                                                            this.messageText = LocaleController.formatString("MessageLifetimeChangedOutgoing", org.telegram.messenger.beta.R.string.MessageLifetimeChangedOutgoing, LocaleController.formatTTLString(action.ttl_seconds));
                                                                        } else {
                                                                            this.messageText = LocaleController.formatString("MessageLifetimeChanged", org.telegram.messenger.beta.R.string.MessageLifetimeChanged, UserObject.getFirstName(fromUser3), LocaleController.formatTTLString(action.ttl_seconds));
                                                                        }
                                                                    } else if (isOut()) {
                                                                        this.messageText = LocaleController.getString("MessageLifetimeYouRemoved", org.telegram.messenger.beta.R.string.MessageLifetimeYouRemoved);
                                                                    } else {
                                                                        this.messageText = LocaleController.formatString("MessageLifetimeRemoved", org.telegram.messenger.beta.R.string.MessageLifetimeRemoved, UserObject.getFirstName(fromUser3));
                                                                    }
                                                                }
                                                            } else if (this.messageOwner.action instanceof TLRPC.TL_messageActionScreenshotTaken) {
                                                                if (isOut()) {
                                                                    this.messageText = LocaleController.formatString("ActionTakeScreenshootYou", org.telegram.messenger.beta.R.string.ActionTakeScreenshootYou, new Object[0]);
                                                                } else {
                                                                    this.messageText = replaceWithLink(LocaleController.getString("ActionTakeScreenshoot", org.telegram.messenger.beta.R.string.ActionTakeScreenshoot), "un1", user3);
                                                                }
                                                            } else if (this.messageOwner.action instanceof TLRPC.TL_messageActionCreatedBroadcastList) {
                                                                this.messageText = LocaleController.formatString("YouCreatedBroadcastList", org.telegram.messenger.beta.R.string.YouCreatedBroadcastList, new Object[0]);
                                                            } else if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionChannelCreate)) {
                                                                if (this.messageOwner.action instanceof TLRPC.TL_messageActionChatMigrateTo) {
                                                                    this.messageText = LocaleController.getString("ActionMigrateFromGroup", org.telegram.messenger.beta.R.string.ActionMigrateFromGroup);
                                                                } else if (this.messageOwner.action instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                                                    this.messageText = LocaleController.getString("ActionMigrateFromGroup", org.telegram.messenger.beta.R.string.ActionMigrateFromGroup);
                                                                } else if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionPinMessage)) {
                                                                    if (this.messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear) {
                                                                        this.messageText = LocaleController.getString("HistoryCleared", org.telegram.messenger.beta.R.string.HistoryCleared);
                                                                    } else if (this.messageOwner.action instanceof TLRPC.TL_messageActionGameScore) {
                                                                        generateGameMessageText(fromUser3);
                                                                    } else if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionPhoneCall)) {
                                                                        if (this.messageOwner.action instanceof TLRPC.TL_messageActionPaymentSent) {
                                                                            TLRPC.User user4 = getUser(users, sUsers, getDialogId());
                                                                            generatePaymentSentMessageText(user4);
                                                                        } else if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionBotAllowed)) {
                                                                            if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionSecureValuesSent)) {
                                                                                if (!(this.messageOwner.action instanceof TLRPC.TL_messageActionWebViewDataSent)) {
                                                                                    if (this.messageOwner.action instanceof TLRPC.TL_messageActionSetChatTheme) {
                                                                                        String emoticon = ((TLRPC.TL_messageActionSetChatTheme) this.messageOwner.action).emoticon;
                                                                                        String userName = UserObject.getFirstName(fromUser3);
                                                                                        boolean isUserSelf = UserObject.isUserSelf(fromUser3);
                                                                                        if (TextUtils.isEmpty(emoticon)) {
                                                                                            this.messageText = isUserSelf ? LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.beta.R.string.ChatThemeDisabledYou, new Object[0]) : LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.beta.R.string.ChatThemeDisabled, userName, emoticon);
                                                                                        } else {
                                                                                            this.messageText = isUserSelf ? LocaleController.formatString("ChatThemeChangedYou", org.telegram.messenger.beta.R.string.ChatThemeChangedYou, emoticon) : LocaleController.formatString("ChatThemeChangedTo", org.telegram.messenger.beta.R.string.ChatThemeChangedTo, userName, emoticon);
                                                                                        }
                                                                                    } else if (this.messageOwner.action instanceof TLRPC.TL_messageActionChatJoinedByRequest) {
                                                                                        if (UserObject.isUserSelf(fromUser3)) {
                                                                                            boolean isChannel = ChatObject.isChannelAndNotMegaGroup(this.messageOwner.peer_id.channel_id, this.currentAccount);
                                                                                            if (isChannel) {
                                                                                                str = LocaleController.getString("RequestToJoinChannelApproved", org.telegram.messenger.beta.R.string.RequestToJoinChannelApproved);
                                                                                            } else {
                                                                                                str = LocaleController.getString("RequestToJoinGroupApproved", org.telegram.messenger.beta.R.string.RequestToJoinGroupApproved);
                                                                                            }
                                                                                            this.messageText = str;
                                                                                        } else {
                                                                                            this.messageText = replaceWithLink(LocaleController.getString("UserAcceptedToGroupAction", org.telegram.messenger.beta.R.string.UserAcceptedToGroupAction), "un1", user3);
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    TLRPC.TL_messageActionWebViewDataSent dataSent = (TLRPC.TL_messageActionWebViewDataSent) this.messageOwner.action;
                                                                                    this.messageText = LocaleController.formatString("ActionBotWebViewData", org.telegram.messenger.beta.R.string.ActionBotWebViewData, dataSent.text);
                                                                                }
                                                                            } else {
                                                                                TLRPC.TL_messageActionSecureValuesSent valuesSent = (TLRPC.TL_messageActionSecureValuesSent) this.messageOwner.action;
                                                                                StringBuilder str5 = new StringBuilder();
                                                                                int size = valuesSent.types.size();
                                                                                for (int a = 0; a < size; a++) {
                                                                                    TLRPC.SecureValueType type = valuesSent.types.get(a);
                                                                                    if (str5.length() > 0) {
                                                                                        str5.append(", ");
                                                                                    }
                                                                                    if (type instanceof TLRPC.TL_secureValueTypePhone) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentPhone", org.telegram.messenger.beta.R.string.ActionBotDocumentPhone));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypeEmail) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentEmail", org.telegram.messenger.beta.R.string.ActionBotDocumentEmail));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypeAddress) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentAddress", org.telegram.messenger.beta.R.string.ActionBotDocumentAddress));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypePersonalDetails) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentIdentity", org.telegram.messenger.beta.R.string.ActionBotDocumentIdentity));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypePassport) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentPassport", org.telegram.messenger.beta.R.string.ActionBotDocumentPassport));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypeDriverLicense) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentDriverLicence", org.telegram.messenger.beta.R.string.ActionBotDocumentDriverLicence));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypeIdentityCard) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentIdentityCard", org.telegram.messenger.beta.R.string.ActionBotDocumentIdentityCard));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypeUtilityBill) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentUtilityBill", org.telegram.messenger.beta.R.string.ActionBotDocumentUtilityBill));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypeBankStatement) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentBankStatement", org.telegram.messenger.beta.R.string.ActionBotDocumentBankStatement));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypeRentalAgreement) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentRentalAgreement", org.telegram.messenger.beta.R.string.ActionBotDocumentRentalAgreement));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypeInternalPassport) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentInternalPassport", org.telegram.messenger.beta.R.string.ActionBotDocumentInternalPassport));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypePassportRegistration) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentPassportRegistration", org.telegram.messenger.beta.R.string.ActionBotDocumentPassportRegistration));
                                                                                    } else if (type instanceof TLRPC.TL_secureValueTypeTemporaryRegistration) {
                                                                                        str5.append(LocaleController.getString("ActionBotDocumentTemporaryRegistration", org.telegram.messenger.beta.R.string.ActionBotDocumentTemporaryRegistration));
                                                                                    }
                                                                                }
                                                                                TLRPC.User user5 = null;
                                                                                if (this.messageOwner.peer_id != null) {
                                                                                    user5 = getUser(users, sUsers, this.messageOwner.peer_id.user_id);
                                                                                }
                                                                                this.messageText = LocaleController.formatString("ActionBotDocuments", org.telegram.messenger.beta.R.string.ActionBotDocuments, UserObject.getFirstName(user5), str5.toString());
                                                                            }
                                                                        } else {
                                                                            String domain = ((TLRPC.TL_messageActionBotAllowed) this.messageOwner.action).domain;
                                                                            String text = LocaleController.getString("ActionBotAllowed", org.telegram.messenger.beta.R.string.ActionBotAllowed);
                                                                            int start = text.indexOf("%1$s");
                                                                            SpannableString str6 = new SpannableString(String.format(text, domain));
                                                                            if (start >= 0) {
                                                                                str6.setSpan(new URLSpanNoUnderlineBold("http://" + domain), start, domain.length() + start, 33);
                                                                            }
                                                                            this.messageText = str6;
                                                                        }
                                                                    } else {
                                                                        TLRPC.TL_messageActionPhoneCall call = (TLRPC.TL_messageActionPhoneCall) this.messageOwner.action;
                                                                        boolean isMissed = call.reason instanceof TLRPC.TL_phoneCallDiscardReasonMissed;
                                                                        if (isFromUser() && this.messageOwner.from_id.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                            if (isMissed) {
                                                                                if (call.video) {
                                                                                    this.messageText = LocaleController.getString("CallMessageVideoOutgoingMissed", org.telegram.messenger.beta.R.string.CallMessageVideoOutgoingMissed);
                                                                                } else {
                                                                                    this.messageText = LocaleController.getString("CallMessageOutgoingMissed", org.telegram.messenger.beta.R.string.CallMessageOutgoingMissed);
                                                                                }
                                                                            } else if (call.video) {
                                                                                this.messageText = LocaleController.getString("CallMessageVideoOutgoing", org.telegram.messenger.beta.R.string.CallMessageVideoOutgoing);
                                                                            } else {
                                                                                this.messageText = LocaleController.getString("CallMessageOutgoing", org.telegram.messenger.beta.R.string.CallMessageOutgoing);
                                                                            }
                                                                        } else if (isMissed) {
                                                                            if (call.video) {
                                                                                this.messageText = LocaleController.getString("CallMessageVideoIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageVideoIncomingMissed);
                                                                            } else {
                                                                                this.messageText = LocaleController.getString("CallMessageIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageIncomingMissed);
                                                                            }
                                                                        } else if (call.reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy) {
                                                                            if (call.video) {
                                                                                this.messageText = LocaleController.getString("CallMessageVideoIncomingDeclined", org.telegram.messenger.beta.R.string.CallMessageVideoIncomingDeclined);
                                                                            } else {
                                                                                this.messageText = LocaleController.getString("CallMessageIncomingDeclined", org.telegram.messenger.beta.R.string.CallMessageIncomingDeclined);
                                                                            }
                                                                        } else if (call.video) {
                                                                            this.messageText = LocaleController.getString("CallMessageVideoIncoming", org.telegram.messenger.beta.R.string.CallMessageVideoIncoming);
                                                                        } else {
                                                                            this.messageText = LocaleController.getString("CallMessageIncoming", org.telegram.messenger.beta.R.string.CallMessageIncoming);
                                                                        }
                                                                        if (call.duration > 0) {
                                                                            String duration = LocaleController.formatCallDuration(call.duration);
                                                                            String formatString = LocaleController.formatString("CallMessageWithDuration", org.telegram.messenger.beta.R.string.CallMessageWithDuration, this.messageText, duration);
                                                                            this.messageText = formatString;
                                                                            String _messageText = formatString.toString();
                                                                            int start2 = _messageText.indexOf(duration);
                                                                            if (start2 != -1) {
                                                                                SpannableString sp = new SpannableString(this.messageText);
                                                                                int end = duration.length() + start2;
                                                                                if (start2 > 0 && _messageText.charAt(start2 - 1) == '(') {
                                                                                    start2--;
                                                                                }
                                                                                if (end < _messageText.length() && _messageText.charAt(end) == ')') {
                                                                                    end++;
                                                                                }
                                                                                sp.setSpan(new TypefaceSpan(Typeface.DEFAULT), start2, end, 0);
                                                                                this.messageText = sp;
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    if (fromUser3 == null) {
                                                                        chat = getChat(chats, sChats, this.messageOwner.peer_id.channel_id);
                                                                    } else {
                                                                        chat = null;
                                                                    }
                                                                    generatePinMessageText(fromUser3, chat);
                                                                }
                                                            } else {
                                                                if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
                                                                    chat3 = getChat(chats, sChats, this.messageOwner.peer_id.channel_id);
                                                                }
                                                                TLRPC.Chat chat4 = chat3;
                                                                if (!ChatObject.isChannel(chat4) || !chat4.megagroup) {
                                                                    this.messageText = LocaleController.getString("ActionCreateChannel", org.telegram.messenger.beta.R.string.ActionCreateChannel);
                                                                } else {
                                                                    this.messageText = LocaleController.getString("ActionCreateMega", org.telegram.messenger.beta.R.string.ActionCreateMega);
                                                                }
                                                            }
                                                        } else {
                                                            fromUser2 = fromUser3;
                                                        }
                                                        this.messageText = LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.beta.R.string.NotificationContactJoined, UserObject.getUserName(fromUser2));
                                                    } else {
                                                        long time2 = this.messageOwner.date * 1000;
                                                        if (LocaleController.getInstance().formatterDay != null && LocaleController.getInstance().formatterYear != null) {
                                                            date = LocaleController.formatString("formatDateAtTime", org.telegram.messenger.beta.R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(time2), LocaleController.getInstance().formatterDay.format(time2));
                                                            str4 = str4;
                                                        } else {
                                                            StringBuilder sb = new StringBuilder();
                                                            str4 = str4;
                                                            sb.append(str4);
                                                            sb.append(this.messageOwner.date);
                                                            date = sb.toString();
                                                        }
                                                        TLRPC.User to_user = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                                                        if (to_user == null) {
                                                            to_user = getUser(users, sUsers, this.messageOwner.peer_id.user_id);
                                                        }
                                                        this.messageText = LocaleController.formatString("NotificationUnrecognizedDevice", org.telegram.messenger.beta.R.string.NotificationUnrecognizedDevice, to_user != null ? UserObject.getFirstName(to_user) : str4, date, this.messageOwner.action.title, this.messageOwner.action.address);
                                                    }
                                                } else {
                                                    TLRPC.TL_messageActionSetMessagesTTL action2 = (TLRPC.TL_messageActionSetMessagesTTL) this.messageOwner.action;
                                                    if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
                                                        chat3 = getChat(chats, sChats, this.messageOwner.peer_id.channel_id);
                                                    }
                                                    TLRPC.Chat chat5 = chat3;
                                                    if (chat5 != null && !chat5.megagroup) {
                                                        if (action2.period != 0) {
                                                            this.messageText = LocaleController.formatString("ActionTTLChannelChanged", org.telegram.messenger.beta.R.string.ActionTTLChannelChanged, LocaleController.formatTTLString(action2.period));
                                                        } else {
                                                            this.messageText = LocaleController.getString("ActionTTLChannelDisabled", org.telegram.messenger.beta.R.string.ActionTTLChannelDisabled);
                                                        }
                                                    } else if (action2.period != 0) {
                                                        if (isOut()) {
                                                            this.messageText = LocaleController.formatString("ActionTTLYouChanged", org.telegram.messenger.beta.R.string.ActionTTLYouChanged, LocaleController.formatTTLString(action2.period));
                                                        } else {
                                                            this.messageText = replaceWithLink(LocaleController.formatString("ActionTTLChanged", org.telegram.messenger.beta.R.string.ActionTTLChanged, LocaleController.formatTTLString(action2.period)), "un1", user3);
                                                        }
                                                    } else if (isOut()) {
                                                        this.messageText = LocaleController.getString("ActionTTLYouDisabled", org.telegram.messenger.beta.R.string.ActionTTLYouDisabled);
                                                    } else {
                                                        this.messageText = replaceWithLink(LocaleController.getString("ActionTTLDisabled", org.telegram.messenger.beta.R.string.ActionTTLDisabled), "un1", user3);
                                                    }
                                                    str4 = str4;
                                                }
                                            } else {
                                                if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
                                                    chat3 = getChat(chats, sChats, this.messageOwner.peer_id.channel_id);
                                                }
                                                TLRPC.Chat chat6 = chat3;
                                                if (ChatObject.isChannel(chat6) && !chat6.megagroup) {
                                                    this.messageText = LocaleController.getString("ActionChannelRemovedPhoto", org.telegram.messenger.beta.R.string.ActionChannelRemovedPhoto);
                                                } else if (isOut()) {
                                                    this.messageText = LocaleController.getString("ActionYouRemovedPhoto", org.telegram.messenger.beta.R.string.ActionYouRemovedPhoto);
                                                } else {
                                                    this.messageText = replaceWithLink(LocaleController.getString("ActionRemovedPhoto", org.telegram.messenger.beta.R.string.ActionRemovedPhoto), "un1", user3);
                                                }
                                                str4 = str4;
                                            }
                                        } else {
                                            if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
                                                chat3 = getChat(chats, sChats, this.messageOwner.peer_id.channel_id);
                                            }
                                            TLRPC.Chat chat7 = chat3;
                                            if (ChatObject.isChannel(chat7) && !chat7.megagroup) {
                                                this.messageText = LocaleController.getString("ActionChannelChangedTitle", org.telegram.messenger.beta.R.string.ActionChannelChangedTitle).replace("un2", this.messageOwner.action.title);
                                            } else if (isOut()) {
                                                this.messageText = LocaleController.getString("ActionYouChangedTitle", org.telegram.messenger.beta.R.string.ActionYouChangedTitle).replace("un2", this.messageOwner.action.title);
                                            } else {
                                                this.messageText = replaceWithLink(LocaleController.getString("ActionChangedTitle", org.telegram.messenger.beta.R.string.ActionChangedTitle).replace("un2", this.messageOwner.action.title), "un1", user3);
                                            }
                                            str4 = str4;
                                        }
                                    } else {
                                        if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
                                            chat3 = getChat(chats, sChats, this.messageOwner.peer_id.channel_id);
                                        }
                                        TLRPC.Chat chat8 = chat3;
                                        if (ChatObject.isChannel(chat8) && !chat8.megagroup) {
                                            if (isVideoAvatar()) {
                                                this.messageText = LocaleController.getString("ActionChannelChangedVideo", org.telegram.messenger.beta.R.string.ActionChannelChangedVideo);
                                            } else {
                                                this.messageText = LocaleController.getString("ActionChannelChangedPhoto", org.telegram.messenger.beta.R.string.ActionChannelChangedPhoto);
                                            }
                                        } else if (isOut()) {
                                            if (isVideoAvatar()) {
                                                this.messageText = LocaleController.getString("ActionYouChangedVideo", org.telegram.messenger.beta.R.string.ActionYouChangedVideo);
                                            } else {
                                                this.messageText = LocaleController.getString("ActionYouChangedPhoto", org.telegram.messenger.beta.R.string.ActionYouChangedPhoto);
                                            }
                                        } else if (isVideoAvatar()) {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionChangedVideo", org.telegram.messenger.beta.R.string.ActionChangedVideo), "un1", user3);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionChangedPhoto", org.telegram.messenger.beta.R.string.ActionChangedPhoto), "un1", user3);
                                        }
                                        str4 = str4;
                                    }
                                }
                            } else {
                                long singleUserId3 = this.messageOwner.action.user_id;
                                if (singleUserId3 == 0 && this.messageOwner.action.users.size() == 1) {
                                    singleUserId = this.messageOwner.action.users.get(0).longValue();
                                } else {
                                    singleUserId = singleUserId3;
                                }
                                if (singleUserId != 0) {
                                    TLRPC.User whoUser2 = getUser(users, sUsers, singleUserId);
                                    if (this.messageOwner.peer_id.channel_id != 0) {
                                        TLRPC.Chat chat9 = getChat(chats, sChats, this.messageOwner.peer_id.channel_id);
                                        chat2 = chat9;
                                    } else {
                                        chat2 = null;
                                    }
                                    if (this.messageOwner.from_id != null && singleUserId == this.messageOwner.from_id.user_id) {
                                        if (ChatObject.isChannel(chat2) && !chat2.megagroup) {
                                            this.messageText = LocaleController.getString("ChannelJoined", org.telegram.messenger.beta.R.string.ChannelJoined);
                                        } else if (this.messageOwner.peer_id.channel_id != 0) {
                                            if (singleUserId == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                this.messageText = LocaleController.getString("ChannelMegaJoined", org.telegram.messenger.beta.R.string.ChannelMegaJoined);
                                            } else {
                                                this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelfMega", org.telegram.messenger.beta.R.string.ActionAddUserSelfMega), "un1", user3);
                                            }
                                        } else if (isOut()) {
                                            this.messageText = LocaleController.getString("ActionAddUserSelfYou", org.telegram.messenger.beta.R.string.ActionAddUserSelfYou);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserSelf", org.telegram.messenger.beta.R.string.ActionAddUserSelf), "un1", user3);
                                        }
                                    } else if (isOut()) {
                                        this.messageText = replaceWithLink(LocaleController.getString("ActionYouAddUser", org.telegram.messenger.beta.R.string.ActionYouAddUser), "un2", whoUser2);
                                    } else if (singleUserId == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                        if (this.messageOwner.peer_id.channel_id == 0) {
                                            this.messageText = replaceWithLink(LocaleController.getString("ActionAddUserYou", org.telegram.messenger.beta.R.string.ActionAddUserYou), "un1", user3);
                                        } else if (chat2 == null || !chat2.megagroup) {
                                            this.messageText = replaceWithLink(LocaleController.getString("ChannelAddedBy", org.telegram.messenger.beta.R.string.ChannelAddedBy), "un1", user3);
                                        } else {
                                            this.messageText = replaceWithLink(LocaleController.getString("MegaAddedBy", org.telegram.messenger.beta.R.string.MegaAddedBy), "un1", user3);
                                        }
                                    } else {
                                        CharSequence replaceWithLink2 = replaceWithLink(LocaleController.getString("ActionAddUser", org.telegram.messenger.beta.R.string.ActionAddUser), "un2", whoUser2);
                                        this.messageText = replaceWithLink2;
                                        this.messageText = replaceWithLink(replaceWithLink2, "un1", user3);
                                    }
                                } else if (isOut()) {
                                    this.messageText = replaceWithLink(LocaleController.getString("ActionYouAddUser", org.telegram.messenger.beta.R.string.ActionYouAddUser), "un2", this.messageOwner.action.users, users, sUsers);
                                } else {
                                    CharSequence replaceWithLink3 = replaceWithLink(LocaleController.getString("ActionAddUser", org.telegram.messenger.beta.R.string.ActionAddUser), "un2", this.messageOwner.action.users, users, sUsers);
                                    this.messageText = replaceWithLink3;
                                    this.messageText = replaceWithLink(replaceWithLink3, "un1", user3);
                                }
                                str4 = str4;
                            }
                        } else if (isOut()) {
                            this.messageText = LocaleController.getString("ActionYouCreateGroup", org.telegram.messenger.beta.R.string.ActionYouCreateGroup);
                            str4 = str4;
                        } else {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionCreateGroup", org.telegram.messenger.beta.R.string.ActionCreateGroup), "un1", user3);
                            str4 = str4;
                        }
                    } else {
                        TLRPC.TL_messageActionGeoProximityReached action3 = (TLRPC.TL_messageActionGeoProximityReached) this.messageOwner.action;
                        long fromId = getPeerId(action3.from_id);
                        if (fromId > 0) {
                            str2 = "un2";
                            from = getUser(users, sUsers, fromId);
                        } else {
                            str2 = "un2";
                            from = getChat(chats, sChats, -fromId);
                        }
                        long toId = getPeerId(action3.to_id);
                        long selfUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                        if (toId == selfUserId) {
                            str3 = str4;
                            this.messageText = replaceWithLink(LocaleController.formatString("ActionUserWithinRadius", org.telegram.messenger.beta.R.string.ActionUserWithinRadius, LocaleController.formatDistance(action3.distance, 2)), "un1", from);
                            user2 = user3;
                        } else {
                            str3 = str4;
                            if (toId > 0) {
                                to = getUser(users, sUsers, toId);
                                user2 = user3;
                            } else {
                                user2 = user3;
                                to = getChat(chats, sChats, -toId);
                            }
                            if (fromId == selfUserId) {
                                this.messageText = replaceWithLink(LocaleController.formatString("ActionUserWithinYouRadius", org.telegram.messenger.beta.R.string.ActionUserWithinYouRadius, LocaleController.formatDistance(action3.distance, 2)), "un1", to);
                            } else {
                                CharSequence replaceWithLink4 = replaceWithLink(LocaleController.formatString("ActionUserWithinOtherRadius", org.telegram.messenger.beta.R.string.ActionUserWithinOtherRadius, LocaleController.formatDistance(action3.distance, 2)), str2, to);
                                this.messageText = replaceWithLink4;
                                this.messageText = replaceWithLink(replaceWithLink4, "un1", from);
                            }
                        }
                        str4 = str3;
                    }
                } else {
                    long singleUserId4 = this.messageOwner.action.user_id;
                    if (singleUserId4 == 0 && this.messageOwner.action.users.size() == 1) {
                        singleUserId2 = this.messageOwner.action.users.get(0).longValue();
                    } else {
                        singleUserId2 = singleUserId4;
                    }
                    if (singleUserId2 != 0) {
                        TLRPC.User whoUser3 = getUser(users, sUsers, singleUserId2);
                        if (isOut()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallYouInvited", org.telegram.messenger.beta.R.string.ActionGroupCallYouInvited), "un2", whoUser3);
                        } else if (singleUserId2 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallInvitedYou", org.telegram.messenger.beta.R.string.ActionGroupCallInvitedYou), "un1", user3);
                        } else {
                            CharSequence replaceWithLink5 = replaceWithLink(LocaleController.getString("ActionGroupCallInvited", org.telegram.messenger.beta.R.string.ActionGroupCallInvited), "un2", whoUser3);
                            this.messageText = replaceWithLink5;
                            this.messageText = replaceWithLink(replaceWithLink5, "un1", user3);
                        }
                    } else if (isOut()) {
                        this.messageText = replaceWithLink(LocaleController.getString("ActionGroupCallYouInvited", org.telegram.messenger.beta.R.string.ActionGroupCallYouInvited), "un2", this.messageOwner.action.users, users, sUsers);
                    } else {
                        CharSequence replaceWithLink6 = replaceWithLink(LocaleController.getString("ActionGroupCallInvited", org.telegram.messenger.beta.R.string.ActionGroupCallInvited), "un2", this.messageOwner.action.users, users, sUsers);
                        this.messageText = replaceWithLink6;
                        this.messageText = replaceWithLink(replaceWithLink6, "un1", user3);
                    }
                }
            } else {
                TLRPC.TL_messageActionGroupCallScheduled action4 = (TLRPC.TL_messageActionGroupCallScheduled) this.messageOwner.action;
                if ((this.messageOwner.peer_id instanceof TLRPC.TL_peerChat) || isSupergroup()) {
                    this.messageText = LocaleController.formatString("ActionGroupCallScheduled", org.telegram.messenger.beta.R.string.ActionGroupCallScheduled, LocaleController.formatStartsTime(action4.schedule_date, 3, false));
                } else {
                    this.messageText = LocaleController.formatString("ActionChannelCallScheduled", org.telegram.messenger.beta.R.string.ActionChannelCallScheduled, LocaleController.formatStartsTime(action4.schedule_date, 3, false));
                }
            }
        }
        if (this.messageText == null) {
            this.messageText = str4;
        }
    }

    public void setType() {
        int oldType = this.type;
        this.type = 1000;
        this.isRoundVideoCached = 0;
        TLRPC.Message message = this.messageOwner;
        if ((message instanceof TLRPC.TL_message) || (message instanceof TLRPC.TL_messageForwarded_old2)) {
            if (this.isRestrictedMessage) {
                this.type = 0;
            } else if (this.emojiAnimatedSticker != null) {
                if (isSticker()) {
                    this.type = 13;
                } else {
                    this.type = 15;
                }
            } else if (isMediaEmpty()) {
                this.type = 0;
                if (TextUtils.isEmpty(this.messageText) && this.eventId == 0) {
                    this.messageText = "Empty message";
                }
            } else if (this.messageOwner.media.ttl_seconds != 0 && ((this.messageOwner.media.photo instanceof TLRPC.TL_photoEmpty) || (getDocument() instanceof TLRPC.TL_documentEmpty))) {
                this.contentType = 1;
                this.type = 10;
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaDice) {
                this.type = 15;
                if (this.messageOwner.media.document == null) {
                    this.messageOwner.media.document = new TLRPC.TL_document();
                    this.messageOwner.media.document.file_reference = new byte[0];
                    this.messageOwner.media.document.mime_type = "application/x-tgsdice";
                    this.messageOwner.media.document.dc_id = Integer.MIN_VALUE;
                    this.messageOwner.media.document.id = -2147483648L;
                    TLRPC.TL_documentAttributeImageSize attributeImageSize = new TLRPC.TL_documentAttributeImageSize();
                    attributeImageSize.w = 512;
                    attributeImageSize.h = 512;
                    this.messageOwner.media.document.attributes.add(attributeImageSize);
                }
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                this.type = 1;
            } else if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaGeo) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaVenue) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaGeoLive)) {
                this.type = 4;
            } else if (isRoundVideo()) {
                this.type = 5;
            } else if (isVideo()) {
                this.type = 3;
            } else if (isVoice()) {
                this.type = 2;
            } else if (isMusic()) {
                this.type = 14;
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaContact) {
                this.type = 12;
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                this.type = 17;
                this.checkedVotes = new ArrayList<>();
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaUnsupported) {
                this.type = 0;
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                TLRPC.Document document = getDocument();
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
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                this.type = 0;
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaInvoice) {
                this.type = 0;
            }
        } else if (message instanceof TLRPC.TL_messageService) {
            if (message.action instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                this.type = 0;
            } else if ((this.messageOwner.action instanceof TLRPC.TL_messageActionChatEditPhoto) || (this.messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto)) {
                this.contentType = 1;
                this.type = 11;
            } else if (this.messageOwner.action instanceof TLRPC.TL_messageEncryptedAction) {
                if ((this.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) || (this.messageOwner.action.encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL)) {
                    this.contentType = 1;
                    this.type = 10;
                } else {
                    this.contentType = -1;
                    this.type = -1;
                }
            } else if (this.messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear) {
                this.contentType = -1;
                this.type = -1;
            } else if (this.messageOwner.action instanceof TLRPC.TL_messageActionPhoneCall) {
                this.type = 16;
            } else {
                this.contentType = 1;
                this.type = 10;
            }
        }
        if (oldType != 1000 && oldType != this.type) {
            updateMessageText(MessagesController.getInstance(this.currentAccount).getUsers(), MessagesController.getInstance(this.currentAccount).getChats(), null, null);
            generateThumbs(false);
        }
    }

    public boolean checkLayout() {
        CharSequence charSequence;
        TextPaint paint;
        if (this.type != 0 || this.messageOwner.peer_id == null || (charSequence = this.messageText) == null || charSequence.length() == 0) {
            return false;
        }
        if (this.layoutCreated) {
            int newMinSize = AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : AndroidUtilities.displaySize.x;
            if (Math.abs(this.generatedWithMinSize - newMinSize) > AndroidUtilities.dp(52.0f) || this.generatedWithDensity != AndroidUtilities.density) {
                this.layoutCreated = false;
            }
        }
        if (this.layoutCreated) {
            return false;
        }
        this.layoutCreated = true;
        TLRPC.User fromUser = null;
        if (isFromUser()) {
            fromUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
        }
        if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
            paint = Theme.chat_msgGameTextPaint;
        } else {
            paint = Theme.chat_msgTextPaint;
        }
        int[] emojiOnly = allowsBigEmoji() ? new int[1] : null;
        this.messageText = Emoji.replaceEmoji(this.messageText, paint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, emojiOnly, this.contentType == 0, this.viewRef);
        checkEmojiOnly(emojiOnly);
        generateLayout(fromUser);
        return true;
    }

    public void resetLayout() {
        this.layoutCreated = false;
    }

    public String getMimeType() {
        TLRPC.Document document = getDocument();
        if (document != null) {
            return document.mime_type;
        }
        if (!(this.messageOwner.media instanceof TLRPC.TL_messageMediaInvoice)) {
            return this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto ? "image/jpeg" : (!(this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) || this.messageOwner.media.webpage.photo == null) ? "" : "image/jpeg";
        }
        TLRPC.WebDocument photo = ((TLRPC.TL_messageMediaInvoice) this.messageOwner.media).photo;
        if (photo != null) {
            return photo.mime_type;
        }
        return "";
    }

    public boolean canPreviewDocument() {
        return canPreviewDocument(getDocument());
    }

    public static boolean isAnimatedStickerDocument(TLRPC.Document document) {
        return document != null && document.mime_type.equals(MimeTypes.VIDEO_WEBM);
    }

    public static boolean isGifDocument(WebFile document) {
        return document != null && (document.mime_type.equals("image/gif") || isNewGifDocument(document));
    }

    public static boolean isGifDocument(TLRPC.Document document) {
        return isGifDocument(document, false);
    }

    public static boolean isGifDocument(TLRPC.Document document, boolean hasGroup) {
        return (document == null || document.mime_type == null || ((!document.mime_type.equals("image/gif") || hasGroup) && !isNewGifDocument(document))) ? false : true;
    }

    public static boolean isDocumentHasThumb(TLRPC.Document document) {
        if (document == null || document.thumbs.isEmpty()) {
            return false;
        }
        int N = document.thumbs.size();
        for (int a = 0; a < N; a++) {
            TLRPC.PhotoSize photoSize = document.thumbs.get(a);
            if (photoSize != null && !(photoSize instanceof TLRPC.TL_photoSizeEmpty) && !(photoSize.location instanceof TLRPC.TL_fileLocationUnavailable)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canPreviewDocument(TLRPC.Document document) {
        if (document != null && document.mime_type != null) {
            String mime = document.mime_type.toLowerCase();
            if ((isDocumentHasThumb(document) && (mime.equals("image/png") || mime.equals("image/jpg") || mime.equals("image/jpeg"))) || (Build.VERSION.SDK_INT >= 26 && mime.equals("image/heic"))) {
                for (int a = 0; a < document.attributes.size(); a++) {
                    TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                    if (attribute instanceof TLRPC.TL_documentAttributeImageSize) {
                        TLRPC.TL_documentAttributeImageSize size = (TLRPC.TL_documentAttributeImageSize) attribute;
                        return size.w < 6000 && size.h < 6000;
                    }
                }
            } else if (BuildVars.DEBUG_PRIVATE_VERSION) {
                String fileName = FileLoader.getDocumentFileName(document);
                if ((fileName.startsWith("tg_secret_sticker") && fileName.endsWith("json")) || fileName.endsWith(".svg")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isRoundVideoDocument(TLRPC.Document document) {
        if (document != null && MimeTypes.VIDEO_MP4.equals(document.mime_type)) {
            int width = 0;
            int height = 0;
            boolean round = false;
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (attribute instanceof TLRPC.TL_documentAttributeVideo) {
                    width = attribute.w;
                    height = attribute.h;
                    round = attribute.round_message;
                }
            }
            if (round && width <= 1280 && height <= 1280) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isNewGifDocument(WebFile document) {
        if (document != null && MimeTypes.VIDEO_MP4.equals(document.mime_type)) {
            int width = 0;
            int height = 0;
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (!(attribute instanceof TLRPC.TL_documentAttributeAnimated) && (attribute instanceof TLRPC.TL_documentAttributeVideo)) {
                    width = attribute.w;
                    height = attribute.h;
                }
            }
            if (width <= 1280 && height <= 1280) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isNewGifDocument(TLRPC.Document document) {
        if (document != null && MimeTypes.VIDEO_MP4.equals(document.mime_type)) {
            int width = 0;
            int height = 0;
            boolean animated = false;
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (attribute instanceof TLRPC.TL_documentAttributeAnimated) {
                    animated = true;
                } else if (attribute instanceof TLRPC.TL_documentAttributeVideo) {
                    width = attribute.w;
                    height = attribute.h;
                }
            }
            if (animated && width <= 1280 && height <= 1280) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isSystemSignUp(MessageObject message) {
        if (message != null) {
            TLRPC.Message message2 = message.messageOwner;
            if ((message2 instanceof TLRPC.TL_messageService) && (((TLRPC.TL_messageService) message2).action instanceof TLRPC.TL_messageActionContactSignUp)) {
                return true;
            }
        }
        return false;
    }

    public void generateThumbs(boolean update) {
        ArrayList<TLRPC.PhotoSize> arrayList;
        ArrayList<TLRPC.PhotoSize> arrayList2;
        ArrayList<TLRPC.PhotoSize> arrayList3;
        ArrayList<TLRPC.PhotoSize> arrayList4;
        ArrayList<TLRPC.PhotoSize> arrayList5;
        ArrayList<TLRPC.PhotoSize> arrayList6;
        ArrayList<TLRPC.PhotoSize> arrayList7;
        TLRPC.Message message = this.messageOwner;
        if (message instanceof TLRPC.TL_messageService) {
            if (message.action instanceof TLRPC.TL_messageActionChatEditPhoto) {
                TLRPC.Photo photo = this.messageOwner.action.photo;
                if (!update) {
                    this.photoThumbs = new ArrayList<>(photo.sizes);
                } else {
                    ArrayList<TLRPC.PhotoSize> arrayList8 = this.photoThumbs;
                    if (arrayList8 != null && !arrayList8.isEmpty()) {
                        for (int a = 0; a < this.photoThumbs.size(); a++) {
                            TLRPC.PhotoSize photoObject = this.photoThumbs.get(a);
                            int b = 0;
                            while (true) {
                                if (b < photo.sizes.size()) {
                                    TLRPC.PhotoSize size = photo.sizes.get(b);
                                    if ((size instanceof TLRPC.TL_photoSizeEmpty) || !size.type.equals(photoObject.type)) {
                                        b++;
                                    } else {
                                        photoObject.location = size.location;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                int a2 = photo.dc_id;
                if (a2 != 0 && (arrayList7 = this.photoThumbs) != null) {
                    int N = arrayList7.size();
                    for (int a3 = 0; a3 < N; a3++) {
                        TLRPC.FileLocation location = this.photoThumbs.get(a3).location;
                        if (location != null) {
                            location.dc_id = photo.dc_id;
                            location.file_reference = photo.file_reference;
                        }
                    }
                }
                this.photoThumbsObject = this.messageOwner.action.photo;
            }
        } else if (this.emojiAnimatedSticker != null) {
            if (TextUtils.isEmpty(this.emojiAnimatedStickerColor) && isDocumentHasThumb(this.emojiAnimatedSticker)) {
                if (!update || (arrayList6 = this.photoThumbs) == null) {
                    ArrayList<TLRPC.PhotoSize> arrayList9 = new ArrayList<>();
                    this.photoThumbs = arrayList9;
                    arrayList9.addAll(this.emojiAnimatedSticker.thumbs);
                } else if (!arrayList6.isEmpty()) {
                    updatePhotoSizeLocations(this.photoThumbs, this.emojiAnimatedSticker.thumbs);
                }
                this.photoThumbsObject = this.emojiAnimatedSticker;
            }
        } else if (message.media != null && !(this.messageOwner.media instanceof TLRPC.TL_messageMediaEmpty)) {
            if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                TLRPC.Photo photo2 = this.messageOwner.media.photo;
                if (!update || ((arrayList5 = this.photoThumbs) != null && arrayList5.size() != photo2.sizes.size())) {
                    this.photoThumbs = new ArrayList<>(photo2.sizes);
                } else {
                    ArrayList<TLRPC.PhotoSize> arrayList10 = this.photoThumbs;
                    if (arrayList10 != null && !arrayList10.isEmpty()) {
                        for (int a4 = 0; a4 < this.photoThumbs.size(); a4++) {
                            TLRPC.PhotoSize photoObject2 = this.photoThumbs.get(a4);
                            if (photoObject2 != null) {
                                int b2 = 0;
                                while (true) {
                                    if (b2 >= photo2.sizes.size()) {
                                        break;
                                    }
                                    TLRPC.PhotoSize size2 = photo2.sizes.get(b2);
                                    if (size2 != null && !(size2 instanceof TLRPC.TL_photoSizeEmpty)) {
                                        if (size2.type.equals(photoObject2.type)) {
                                            photoObject2.location = size2.location;
                                            break;
                                        } else if ("s".equals(photoObject2.type) && (size2 instanceof TLRPC.TL_photoStrippedSize)) {
                                            this.photoThumbs.set(a4, size2);
                                            break;
                                        }
                                    }
                                    b2++;
                                }
                            }
                        }
                    }
                }
                this.photoThumbsObject = this.messageOwner.media.photo;
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
                TLRPC.Document document = getDocument();
                if (isDocumentHasThumb(document)) {
                    if (!update || (arrayList4 = this.photoThumbs) == null) {
                        ArrayList<TLRPC.PhotoSize> arrayList11 = new ArrayList<>();
                        this.photoThumbs = arrayList11;
                        arrayList11.addAll(document.thumbs);
                    } else if (!arrayList4.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs, document.thumbs);
                    }
                    this.photoThumbsObject = document;
                }
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                TLRPC.Document document2 = this.messageOwner.media.game.document;
                if (document2 != null && isDocumentHasThumb(document2)) {
                    if (!update) {
                        ArrayList<TLRPC.PhotoSize> arrayList12 = new ArrayList<>();
                        this.photoThumbs = arrayList12;
                        arrayList12.addAll(document2.thumbs);
                    } else {
                        ArrayList<TLRPC.PhotoSize> arrayList13 = this.photoThumbs;
                        if (arrayList13 != null && !arrayList13.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, document2.thumbs);
                        }
                    }
                    this.photoThumbsObject = document2;
                }
                TLRPC.Photo photo3 = this.messageOwner.media.game.photo;
                if (photo3 != null) {
                    if (!update || (arrayList3 = this.photoThumbs2) == null) {
                        this.photoThumbs2 = new ArrayList<>(photo3.sizes);
                    } else if (!arrayList3.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs2, photo3.sizes);
                    }
                    this.photoThumbsObject2 = photo3;
                }
                if (this.photoThumbs == null && (arrayList2 = this.photoThumbs2) != null) {
                    this.photoThumbs = arrayList2;
                    this.photoThumbs2 = null;
                    this.photoThumbsObject = this.photoThumbsObject2;
                    this.photoThumbsObject2 = null;
                }
            } else if (this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) {
                TLRPC.Photo photo4 = this.messageOwner.media.webpage.photo;
                TLRPC.Document document3 = this.messageOwner.media.webpage.document;
                if (photo4 != null) {
                    if (!update || (arrayList = this.photoThumbs) == null) {
                        this.photoThumbs = new ArrayList<>(photo4.sizes);
                    } else if (!arrayList.isEmpty()) {
                        updatePhotoSizeLocations(this.photoThumbs, photo4.sizes);
                    }
                    this.photoThumbsObject = photo4;
                } else if (document3 != null && isDocumentHasThumb(document3)) {
                    if (!update) {
                        ArrayList<TLRPC.PhotoSize> arrayList14 = new ArrayList<>();
                        this.photoThumbs = arrayList14;
                        arrayList14.addAll(document3.thumbs);
                    } else {
                        ArrayList<TLRPC.PhotoSize> arrayList15 = this.photoThumbs;
                        if (arrayList15 != null && !arrayList15.isEmpty()) {
                            updatePhotoSizeLocations(this.photoThumbs, document3.thumbs);
                        }
                    }
                    this.photoThumbsObject = document3;
                }
            }
        }
    }

    private static void updatePhotoSizeLocations(ArrayList<TLRPC.PhotoSize> o, ArrayList<TLRPC.PhotoSize> n) {
        int N = o.size();
        for (int a = 0; a < N; a++) {
            TLRPC.PhotoSize photoObject = o.get(a);
            if (photoObject != null) {
                int b = 0;
                int N2 = n.size();
                while (true) {
                    if (b < N2) {
                        TLRPC.PhotoSize size = n.get(b);
                        if ((size instanceof TLRPC.TL_photoSizeEmpty) || (size instanceof TLRPC.TL_photoCachedSize) || size == null || !size.type.equals(photoObject.type)) {
                            b++;
                        } else {
                            photoObject.location = size.location;
                            break;
                        }
                    }
                }
            }
        }
    }

    public CharSequence replaceWithLink(CharSequence source, String param, ArrayList<Long> uids, AbstractMap<Long, TLRPC.User> usersDict, LongSparseArray<TLRPC.User> sUsersDict) {
        if (TextUtils.indexOf(source, param) >= 0) {
            SpannableStringBuilder names = new SpannableStringBuilder("");
            for (int a = 0; a < uids.size(); a++) {
                TLRPC.User user = null;
                if (usersDict != null) {
                    TLRPC.User user2 = usersDict.get(uids.get(a));
                    user = user2;
                } else if (sUsersDict != null) {
                    TLRPC.User user3 = sUsersDict.get(uids.get(a).longValue());
                    user = user3;
                }
                if (user == null) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(uids.get(a));
                }
                if (user != null) {
                    String name = UserObject.getUserName(user);
                    int start = names.length();
                    if (names.length() != 0) {
                        names.append((CharSequence) ", ");
                    }
                    names.append((CharSequence) name);
                    names.setSpan(new URLSpanNoUnderlineBold("" + user.id), start, name.length() + start, 33);
                }
            }
            return TextUtils.replace(source, new String[]{param}, new CharSequence[]{names});
        }
        return source;
    }

    public static CharSequence replaceWithLink(CharSequence source, String param, TLObject object) {
        String id;
        String name;
        int start = TextUtils.indexOf(source, param);
        if (start >= 0) {
            TLObject spanObject = null;
            if (object instanceof TLRPC.User) {
                name = UserObject.getUserName((TLRPC.User) object);
                id = "" + ((TLRPC.User) object).id;
            } else if (object instanceof TLRPC.Chat) {
                name = ((TLRPC.Chat) object).title;
                id = "" + (-((TLRPC.Chat) object).id);
            } else if (object instanceof TLRPC.TL_game) {
                TLRPC.TL_game game = (TLRPC.TL_game) object;
                String name2 = game.title;
                id = "game";
                name = name2;
            } else if (object instanceof TLRPC.TL_chatInviteExported) {
                TLRPC.TL_chatInviteExported invite = (TLRPC.TL_chatInviteExported) object;
                String name3 = invite.link;
                spanObject = invite;
                name = name3;
                id = "invite";
            } else {
                name = "";
                id = "0";
            }
            String name4 = name.replace('\n', ' ');
            SpannableStringBuilder builder = new SpannableStringBuilder(TextUtils.replace(source, new String[]{param}, new String[]{name4}));
            URLSpanNoUnderlineBold span = new URLSpanNoUnderlineBold("" + id);
            span.setObject(spanObject);
            builder.setSpan(span, start, name4.length() + start, 33);
            return builder;
        }
        return source;
    }

    public String getExtension() {
        String fileName = getFileName();
        int idx = fileName.lastIndexOf(46);
        String ext = null;
        if (idx != -1) {
            ext = fileName.substring(idx + 1);
        }
        if (ext == null || ext.length() == 0) {
            ext = getDocument().mime_type;
        }
        if (ext == null) {
            ext = "";
        }
        return ext.toUpperCase();
    }

    public String getFileName() {
        return getFileName(this.messageOwner);
    }

    public static String getFileName(TLRPC.Message messageOwner) {
        TLRPC.PhotoSize sizeFull;
        if (messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
            return FileLoader.getAttachFileName(getDocument(messageOwner));
        }
        if (messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
            ArrayList<TLRPC.PhotoSize> sizes = messageOwner.media.photo.sizes;
            if (sizes.size() > 0 && (sizeFull = FileLoader.getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize())) != null) {
                return FileLoader.getAttachFileName(sizeFull);
            }
            return "";
        } else if (messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) {
            return FileLoader.getAttachFileName(messageOwner.media.webpage.document);
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
        if (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) {
            return 3;
        }
        if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
            return 0;
        }
        return 4;
    }

    private static boolean containsUrls(CharSequence message) {
        if (message == null || message.length() < 2 || message.length() > 20480) {
            return false;
        }
        int length = message.length();
        int digitsInRow = 0;
        int schemeSequence = 0;
        int dotSequence = 0;
        char lastChar = 0;
        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);
            if (c >= '0' && c <= '9') {
                digitsInRow++;
                if (digitsInRow >= 6) {
                    return true;
                }
                schemeSequence = 0;
                dotSequence = 0;
            } else if (c == ' ' || digitsInRow <= 0) {
                digitsInRow = 0;
            }
            if (((c == '@' || c == '#' || c == '/' || c == '$') && i == 0) || (i != 0 && (message.charAt(i - 1) == ' ' || message.charAt(i - 1) == '\n'))) {
                return true;
            }
            if (c == ':') {
                if (schemeSequence == 0) {
                    schemeSequence = 1;
                } else {
                    schemeSequence = 0;
                }
            } else if (c == '/') {
                if (schemeSequence == 2) {
                    return true;
                }
                if (schemeSequence == 1) {
                    schemeSequence++;
                } else {
                    schemeSequence = 0;
                }
            } else if (c == '.') {
                if (dotSequence == 0 && lastChar != ' ') {
                    dotSequence++;
                } else {
                    dotSequence = 0;
                }
            } else if (c != ' ' && lastChar == '.' && dotSequence == 1) {
                return true;
            } else {
                dotSequence = 0;
            }
            lastChar = c;
        }
        return false;
    }

    public void generateLinkDescription() {
        if (this.linkDescription != null) {
            return;
        }
        int hashtagsType = 0;
        if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && (this.messageOwner.media.webpage instanceof TLRPC.TL_webPage) && this.messageOwner.media.webpage.description != null) {
            this.linkDescription = Spannable.Factory.getInstance().newSpannable(this.messageOwner.media.webpage.description);
            String siteName = this.messageOwner.media.webpage.site_name;
            if (siteName != null) {
                siteName = siteName.toLowerCase();
            }
            if ("instagram".equals(siteName)) {
                hashtagsType = 1;
            } else if ("twitter".equals(siteName)) {
                hashtagsType = 2;
            }
        } else if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) && this.messageOwner.media.game.description != null) {
            this.linkDescription = Spannable.Factory.getInstance().newSpannable(this.messageOwner.media.game.description);
        } else if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaInvoice) && this.messageOwner.media.description != null) {
            this.linkDescription = Spannable.Factory.getInstance().newSpannable(this.messageOwner.media.description);
        }
        if (!TextUtils.isEmpty(this.linkDescription)) {
            if (containsUrls(this.linkDescription)) {
                try {
                    AndroidUtilities.addLinks((Spannable) this.linkDescription, 1);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            CharSequence replaceEmoji = Emoji.replaceEmoji(this.linkDescription, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
            this.linkDescription = replaceEmoji;
            if (hashtagsType != 0) {
                if (!(replaceEmoji instanceof Spannable)) {
                    this.linkDescription = new SpannableStringBuilder(this.linkDescription);
                }
                addUrlsByPattern(isOutOwner(), this.linkDescription, false, hashtagsType, 0, false);
            }
        }
    }

    public CharSequence getVoiceTranscription() {
        TLRPC.Message message = this.messageOwner;
        if (message == null || message.voiceTranscription == null) {
            return null;
        }
        if (TextUtils.isEmpty(this.messageOwner.voiceTranscription)) {
            SpannableString ssb = new SpannableString(LocaleController.getString("NoWordsRecognized", org.telegram.messenger.beta.R.string.NoWordsRecognized));
            ssb.setSpan(new CharacterStyle() { // from class: org.telegram.messenger.MessageObject.1
                @Override // android.text.style.CharacterStyle
                public void updateDrawState(TextPaint textPaint) {
                    textPaint.setTextSize(textPaint.getTextSize() * 0.8f);
                    textPaint.setColor(Theme.chat_timePaint.getColor());
                }
            }, 0, ssb.length(), 33);
            return ssb;
        }
        CharSequence text = this.messageOwner.voiceTranscription;
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        return Emoji.replaceEmoji(text, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
    }

    public float measureVoiceTranscriptionHeight() {
        StaticLayout captionLayout;
        CharSequence voiceTranscription = getVoiceTranscription();
        if (voiceTranscription == null) {
            return 0.0f;
        }
        int width = AndroidUtilities.displaySize.x - AndroidUtilities.dp(needDrawAvatar() ? 147.0f : 95.0f);
        if (Build.VERSION.SDK_INT >= 24) {
            captionLayout = StaticLayout.Builder.obtain(voiceTranscription, 0, voiceTranscription.length(), Theme.chat_msgTextPaint, width).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
        } else {
            captionLayout = new StaticLayout(voiceTranscription, Theme.chat_msgTextPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        return captionLayout.getHeight();
    }

    public boolean isVoiceTranscriptionOpen() {
        TLRPC.Message message;
        return isVoice() && (message = this.messageOwner) != null && message.voiceTranscriptionOpen && this.messageOwner.voiceTranscription != null && (this.messageOwner.voiceTranscriptionFinal || TranscribeButton.isTranscribing(this)) && UserConfig.getInstance(this.currentAccount).isPremium();
    }

    public void generateCaption() {
        boolean hasEntities;
        if (this.caption == null && !isRoundVideo() && !isMediaEmpty() && !(this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) && !TextUtils.isEmpty(this.messageOwner.message)) {
            boolean z = false;
            this.caption = Emoji.replaceEmoji(this.messageOwner.message, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false, this.contentType == 0, this.viewRef);
            if (this.messageOwner.send_state != 0) {
                hasEntities = false;
            } else {
                hasEntities = !this.messageOwner.entities.isEmpty();
            }
            if (!hasEntities && (this.eventId != 0 || (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto_old) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto_layer68) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto_layer74) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument_old) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument_layer68) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument_layer74) || ((isOut() && this.messageOwner.send_state != 0) || this.messageOwner.id < 0))) {
                z = true;
            }
            boolean useManualParse = z;
            if (useManualParse) {
                if (containsUrls(this.caption)) {
                    try {
                        AndroidUtilities.addLinks((Spannable) this.caption, 5);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                addUrlsByPattern(isOutOwner(), this.caption, true, 0, 0, true);
            }
            addEntitiesToText(this.caption, useManualParse);
            if (isVideo()) {
                addUrlsByPattern(isOutOwner(), this.caption, true, 3, getDuration(), false);
            } else if (isMusic() || isVoice()) {
                addUrlsByPattern(isOutOwner(), this.caption, true, 4, getDuration(), false);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x01f8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x01f1 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01e0 A[Catch: Exception -> 0x0256, TryCatch #0 {Exception -> 0x0256, blocks: (B:9:0x0011, B:11:0x0015, B:12:0x001d, B:13:0x0024, B:15:0x0028, B:16:0x0030, B:17:0x0037, B:19:0x003b, B:20:0x0043, B:21:0x0049, B:22:0x004c, B:24:0x0052, B:28:0x0062, B:32:0x0072, B:33:0x0074, B:42:0x0089, B:45:0x008f, B:46:0x00b2, B:49:0x00d9, B:50:0x00fc, B:51:0x011f, B:54:0x0127, B:58:0x0136, B:59:0x013c, B:60:0x014c, B:63:0x0198, B:70:0x01b0, B:77:0x01c4, B:79:0x01d0, B:82:0x01da, B:84:0x01e0, B:91:0x01fd, B:92:0x0215, B:93:0x022c, B:96:0x0235, B:98:0x023f, B:100:0x0242, B:101:0x0248), top: B:106:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0233  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void addUrlsByPattern(boolean isOut, CharSequence charSequence, boolean botCommands, int patternType, int duration, boolean check) {
        Matcher matcher;
        ClickableSpan[] spans;
        String str;
        int e4;
        URLSpan[] spans2;
        int seconds;
        CharSequence charSequence2 = charSequence;
        int i = patternType;
        if (charSequence2 == null) {
            return;
        }
        int i2 = 4;
        int e42 = 3;
        int e3 = 1;
        try {
            if (i == 3 || i == 4) {
                if (videoTimeUrlPattern == null) {
                    videoTimeUrlPattern = Pattern.compile("\\b(?:(\\d{1,2}):)?(\\d{1,3}):([0-5][0-9])\\b([^\\n]*)");
                }
                matcher = videoTimeUrlPattern.matcher(charSequence2);
            } else if (i == 1) {
                if (instagramUrlPattern == null) {
                    instagramUrlPattern = Pattern.compile("(^|\\s|\\()@[a-zA-Z\\d_.]{1,32}|(^|\\s|\\()#[\\w.]+");
                }
                matcher = instagramUrlPattern.matcher(charSequence2);
            } else {
                if (urlPattern == null) {
                    urlPattern = Pattern.compile("(^|\\s)/[a-zA-Z@\\d_]{1,255}|(^|\\s|\\()@[a-zA-Z\\d_]{1,32}|(^|\\s|\\()#[^0-9][\\w.]+|(^|\\s)\\$[A-Z]{3,8}([ ,.]|$)");
                }
                matcher = urlPattern.matcher(charSequence2);
            }
            Spannable spannable = (Spannable) charSequence2;
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                URLSpanNoUnderline url = null;
                if (i != e42 && i != i2) {
                    char ch = charSequence2.charAt(start);
                    if (i != 0) {
                        if (ch != '@' && ch != '#') {
                            start++;
                        }
                        ch = charSequence2.charAt(start);
                        if (ch != '@' && ch != '#') {
                        }
                    } else if (ch != '@' && ch != '#' && ch != '/' && ch != '$') {
                        start++;
                    }
                    if (i == e3) {
                        if (ch == '@') {
                            url = new URLSpanNoUnderline("https://instagram.com/" + charSequence2.subSequence(start + 1, end).toString());
                        } else {
                            url = new URLSpanNoUnderline("https://www.instagram.com/explore/tags/" + charSequence2.subSequence(start + 1, end).toString());
                        }
                    } else if (i != 2) {
                        if (charSequence2.charAt(start) == '/') {
                            if (botCommands) {
                                url = new URLSpanBotCommand(charSequence2.subSequence(start, end).toString(), isOut ? 1 : 0);
                            }
                        } else {
                            url = new URLSpanNoUnderline(charSequence2.subSequence(start, end).toString());
                        }
                    } else if (ch == '@') {
                        url = new URLSpanNoUnderline("https://twitter.com/" + charSequence2.subSequence(start + 1, end).toString());
                    } else {
                        url = new URLSpanNoUnderline("https://twitter.com/hashtag/" + charSequence2.subSequence(start + 1, end).toString());
                    }
                    if (url != null) {
                        if (check && (spans = (ClickableSpan[]) spannable.getSpans(start, end, ClickableSpan.class)) != null && spans.length > 0) {
                            spannable.removeSpan(spans[0]);
                        }
                        spannable.setSpan(url, start, end, 0);
                    }
                    charSequence2 = charSequence;
                    i = patternType;
                    i2 = 4;
                    e42 = 3;
                    e3 = 1;
                }
                matcher.groupCount();
                int s1 = matcher.start(e3);
                int e1 = matcher.end(e3);
                int s2 = matcher.start(2);
                int e2 = matcher.end(2);
                int s3 = matcher.start(e42);
                int e32 = matcher.end(e42);
                int s4 = matcher.start(i2);
                int e43 = matcher.end(i2);
                int minutes = Utilities.parseInt(charSequence2.subSequence(s2, e2)).intValue();
                int seconds2 = Utilities.parseInt(charSequence2.subSequence(s3, e32)).intValue();
                int hours = (s1 < 0 || e1 < 0) ? -1 : Utilities.parseInt(charSequence2.subSequence(s1, e1)).intValue();
                if (s4 >= 0) {
                    e4 = e43;
                    if (e4 >= 0) {
                        str = charSequence2.subSequence(s4, e4).toString();
                        String label = str;
                        if (s4 < 0 || e4 >= 0) {
                            end = e32;
                        }
                        spans2 = (URLSpan[]) spannable.getSpans(start, end, URLSpan.class);
                        if (spans2 == null && spans2.length > 0) {
                            charSequence2 = charSequence;
                            i2 = 4;
                            e42 = 3;
                            e3 = 1;
                        } else {
                            int seconds3 = seconds2 + (minutes * 60);
                            if (hours > 0) {
                                seconds = seconds3;
                            } else {
                                seconds = seconds3 + (hours * 60 * 60);
                            }
                            if (seconds <= duration) {
                                charSequence2 = charSequence;
                                i2 = 4;
                                e42 = 3;
                                e3 = 1;
                            } else {
                                if (i == 3) {
                                    url = new URLSpanNoUnderline("video?" + seconds);
                                } else {
                                    URLSpanNoUnderline url2 = new URLSpanNoUnderline("audio?" + seconds);
                                    url = url2;
                                }
                                url.label = label;
                                if (url != null) {
                                }
                                charSequence2 = charSequence;
                                i = patternType;
                                i2 = 4;
                                e42 = 3;
                                e3 = 1;
                            }
                        }
                    }
                } else {
                    e4 = e43;
                }
                str = null;
                String label2 = str;
                if (s4 < 0) {
                }
                end = e32;
                spans2 = (URLSpan[]) spannable.getSpans(start, end, URLSpan.class);
                if (spans2 == null) {
                }
                int seconds32 = seconds2 + (minutes * 60);
                if (hours > 0) {
                }
                if (seconds <= duration) {
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static int[] getWebDocumentWidthAndHeight(TLRPC.WebDocument document) {
        if (document == null) {
            return null;
        }
        int size = document.attributes.size();
        for (int a = 0; a < size; a++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(a);
            if (!(attribute instanceof TLRPC.TL_documentAttributeImageSize) && !(attribute instanceof TLRPC.TL_documentAttributeVideo)) {
            }
            return new int[]{attribute.w, attribute.h};
        }
        return null;
    }

    public static int getWebDocumentDuration(TLRPC.WebDocument document) {
        if (document == null) {
            return 0;
        }
        int size = document.attributes.size();
        for (int a = 0; a < size; a++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(a);
            if (attribute instanceof TLRPC.TL_documentAttributeVideo) {
                return attribute.duration;
            }
            if (attribute instanceof TLRPC.TL_documentAttributeAudio) {
                return attribute.duration;
            }
        }
        return 0;
    }

    public static int[] getInlineResultWidthAndHeight(TLRPC.BotInlineResult inlineResult) {
        int[] result = getWebDocumentWidthAndHeight(inlineResult.content);
        if (result == null) {
            int[] result2 = getWebDocumentWidthAndHeight(inlineResult.thumb);
            if (result2 == null) {
                return new int[]{0, 0};
            }
            return result2;
        }
        return result;
    }

    public static int getInlineResultDuration(TLRPC.BotInlineResult inlineResult) {
        int result = getWebDocumentDuration(inlineResult.content);
        if (result == 0) {
            return getWebDocumentDuration(inlineResult.thumb);
        }
        return result;
    }

    public boolean hasValidGroupId() {
        ArrayList<TLRPC.PhotoSize> arrayList;
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

    public static void addLinks(boolean isOut, CharSequence messageText) {
        addLinks(isOut, messageText, true, false);
    }

    public static void addLinks(boolean isOut, CharSequence messageText, boolean botCommands, boolean check) {
        addLinks(isOut, messageText, botCommands, check, false);
    }

    public static void addLinks(boolean isOut, CharSequence messageText, boolean botCommands, boolean check, boolean internalOnly) {
        if ((messageText instanceof Spannable) && containsUrls(messageText)) {
            if (messageText.length() < 1000) {
                try {
                    AndroidUtilities.addLinks((Spannable) messageText, 5, internalOnly);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } else {
                try {
                    AndroidUtilities.addLinks((Spannable) messageText, 1, internalOnly);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            addUrlsByPattern(isOut, messageText, botCommands, 0, 0, check);
        }
    }

    public void resetPlayingProgress() {
        this.audioProgress = 0.0f;
        this.audioProgressSec = 0;
        this.bufferedProgress = 0.0f;
    }

    private boolean addEntitiesToText(CharSequence text, boolean useManualParse) {
        return addEntitiesToText(text, false, useManualParse);
    }

    public boolean addEntitiesToText(CharSequence text, boolean photoViewer, boolean useManualParse) {
        if (text == null) {
            return false;
        }
        if (this.isRestrictedMessage) {
            ArrayList<TLRPC.MessageEntity> entities = new ArrayList<>();
            TLRPC.TL_messageEntityItalic entityItalic = new TLRPC.TL_messageEntityItalic();
            entityItalic.offset = 0;
            entityItalic.length = text.length();
            entities.add(entityItalic);
            return addEntitiesToText(text, entities, isOutOwner(), true, photoViewer, useManualParse);
        }
        return addEntitiesToText(text, this.messageOwner.entities, isOutOwner(), true, photoViewer, useManualParse);
    }

    public static boolean addEntitiesToText(CharSequence text, ArrayList<TLRPC.MessageEntity> entities, boolean out, boolean usernames, boolean photoViewer, boolean useManualParse) {
        byte t;
        int count;
        int count2;
        String url;
        int i;
        int N;
        URLSpan[] spans;
        boolean hasUrls;
        int N2;
        CharSequence charSequence = text;
        boolean hasUrls2 = false;
        if (!(charSequence instanceof Spannable)) {
            return false;
        }
        Spannable spannable = (Spannable) charSequence;
        URLSpan[] spans2 = (URLSpan[]) spannable.getSpans(0, text.length(), URLSpan.class);
        if (spans2 != null && spans2.length > 0) {
            hasUrls2 = true;
        }
        if (entities.isEmpty()) {
            return hasUrls2;
        }
        if (photoViewer) {
            t = 2;
        } else if (out) {
            t = 1;
        } else {
            t = 0;
        }
        ArrayList<TextStyleSpan.TextStyleRun> runs = new ArrayList<>();
        ArrayList<TLRPC.MessageEntity> entitiesCopy = new ArrayList<>(entities);
        Collections.sort(entitiesCopy, MessageObject$$ExternalSyntheticLambda1.INSTANCE);
        int a = 0;
        int N3 = entitiesCopy.size();
        while (a < N3) {
            TLRPC.MessageEntity entity = entitiesCopy.get(a);
            if (entity.length <= 0 || entity.offset < 0) {
                hasUrls = hasUrls2;
                N = N3;
                spans = spans2;
            } else if (entity.offset >= text.length()) {
                hasUrls = hasUrls2;
                N = N3;
                spans = spans2;
            } else {
                if (entity.offset + entity.length > text.length()) {
                    entity.length = text.length() - entity.offset;
                }
                if ((!useManualParse || (entity instanceof TLRPC.TL_messageEntityBold) || (entity instanceof TLRPC.TL_messageEntityItalic) || (entity instanceof TLRPC.TL_messageEntityStrike) || (entity instanceof TLRPC.TL_messageEntityUnderline) || (entity instanceof TLRPC.TL_messageEntityBlockquote) || (entity instanceof TLRPC.TL_messageEntityCode) || (entity instanceof TLRPC.TL_messageEntityPre) || (entity instanceof TLRPC.TL_messageEntityMentionName) || (entity instanceof TLRPC.TL_inputMessageEntityMentionName) || (entity instanceof TLRPC.TL_messageEntityTextUrl) || (entity instanceof TLRPC.TL_messageEntitySpoiler)) && spans2 != null && spans2.length > 0) {
                    for (int b = 0; b < spans2.length; b++) {
                        if (spans2[b] != null) {
                            int start = spannable.getSpanStart(spans2[b]);
                            int end = spannable.getSpanEnd(spans2[b]);
                            if ((entity.offset <= start && entity.offset + entity.length >= start) || (entity.offset <= end && entity.offset + entity.length >= end)) {
                                spannable.removeSpan(spans2[b]);
                                spans2[b] = null;
                            }
                        }
                    }
                }
                TextStyleSpan.TextStyleRun newRun = new TextStyleSpan.TextStyleRun();
                newRun.start = entity.offset;
                newRun.end = newRun.start + entity.length;
                if (entity instanceof TLRPC.TL_messageEntitySpoiler) {
                    newRun.flags = 256;
                } else if (entity instanceof TLRPC.TL_messageEntityStrike) {
                    newRun.flags = 8;
                } else if (entity instanceof TLRPC.TL_messageEntityUnderline) {
                    newRun.flags = 16;
                } else if (entity instanceof TLRPC.TL_messageEntityBlockquote) {
                    newRun.flags = 32;
                } else if (entity instanceof TLRPC.TL_messageEntityBold) {
                    newRun.flags = 1;
                } else if (entity instanceof TLRPC.TL_messageEntityItalic) {
                    newRun.flags = 2;
                } else if ((entity instanceof TLRPC.TL_messageEntityCode) || (entity instanceof TLRPC.TL_messageEntityPre)) {
                    newRun.flags = 4;
                } else if (entity instanceof TLRPC.TL_messageEntityMentionName) {
                    if (!usernames) {
                        hasUrls = hasUrls2;
                        N = N3;
                        spans = spans2;
                    } else {
                        newRun.flags = 64;
                        newRun.urlEntity = entity;
                    }
                } else if (entity instanceof TLRPC.TL_inputMessageEntityMentionName) {
                    if (!usernames) {
                        hasUrls = hasUrls2;
                        N = N3;
                        spans = spans2;
                    } else {
                        newRun.flags = 64;
                        newRun.urlEntity = entity;
                    }
                } else if (useManualParse && !(entity instanceof TLRPC.TL_messageEntityTextUrl)) {
                    hasUrls = hasUrls2;
                    N = N3;
                    spans = spans2;
                } else if (((entity instanceof TLRPC.TL_messageEntityUrl) || (entity instanceof TLRPC.TL_messageEntityTextUrl)) && Browser.isPassportUrl(entity.url)) {
                    hasUrls = hasUrls2;
                    N = N3;
                    spans = spans2;
                } else if ((entity instanceof TLRPC.TL_messageEntityMention) && !usernames) {
                    hasUrls = hasUrls2;
                    N = N3;
                    spans = spans2;
                } else {
                    newRun.flags = 128;
                    newRun.urlEntity = entity;
                }
                int b2 = 0;
                int N22 = runs.size();
                while (b2 < N22) {
                    TextStyleSpan.TextStyleRun run = runs.get(b2);
                    boolean hasUrls3 = hasUrls2;
                    URLSpan[] spans3 = spans2;
                    if ((run.flags & 256) == 0 || newRun.start < run.start || newRun.end > run.end) {
                        if (newRun.start > run.start) {
                            if (newRun.start < run.end) {
                                if (newRun.end < run.end) {
                                    TextStyleSpan.TextStyleRun r = new TextStyleSpan.TextStyleRun(newRun);
                                    r.merge(run);
                                    int b3 = b2 + 1;
                                    runs.add(b3, r);
                                    TextStyleSpan.TextStyleRun r2 = new TextStyleSpan.TextStyleRun(run);
                                    r2.start = newRun.end;
                                    b2 = b3 + 1;
                                    N22 = N22 + 1 + 1;
                                    runs.add(b2, r2);
                                } else {
                                    TextStyleSpan.TextStyleRun r3 = new TextStyleSpan.TextStyleRun(newRun);
                                    r3.merge(run);
                                    r3.end = run.end;
                                    b2++;
                                    N22++;
                                    runs.add(b2, r3);
                                }
                                int temp = newRun.start;
                                newRun.start = run.end;
                                run.end = temp;
                                N2 = N3;
                                b2++;
                                hasUrls2 = hasUrls3;
                                spans2 = spans3;
                                N3 = N2;
                            }
                        } else if (run.start < newRun.end) {
                            int temp2 = run.start;
                            int i2 = newRun.end;
                            N2 = N3;
                            int N4 = run.end;
                            if (i2 == N4) {
                                run.merge(newRun);
                            } else if (newRun.end < run.end) {
                                TextStyleSpan.TextStyleRun r4 = new TextStyleSpan.TextStyleRun(run);
                                r4.merge(newRun);
                                r4.end = newRun.end;
                                b2++;
                                N22++;
                                runs.add(b2, r4);
                                run.start = newRun.end;
                            } else {
                                TextStyleSpan.TextStyleRun r5 = new TextStyleSpan.TextStyleRun(newRun);
                                r5.start = run.end;
                                b2++;
                                N22++;
                                runs.add(b2, r5);
                                run.merge(newRun);
                            }
                            newRun.end = temp2;
                            b2++;
                            hasUrls2 = hasUrls3;
                            spans2 = spans3;
                            N3 = N2;
                        }
                    }
                    N2 = N3;
                    b2++;
                    hasUrls2 = hasUrls3;
                    spans2 = spans3;
                    N3 = N2;
                }
                hasUrls = hasUrls2;
                N = N3;
                spans = spans2;
                int N5 = newRun.start;
                if (N5 < newRun.end) {
                    runs.add(newRun);
                }
            }
            a++;
            hasUrls2 = hasUrls;
            spans2 = spans;
            N3 = N;
        }
        boolean hasUrls4 = hasUrls2;
        String str = null;
        int count3 = runs.size();
        int a2 = 0;
        while (a2 < count3) {
            TextStyleSpan.TextStyleRun run2 = runs.get(a2);
            boolean setRun = false;
            String url2 = run2.urlEntity != null ? TextUtils.substring(charSequence, run2.urlEntity.offset, run2.urlEntity.offset + run2.urlEntity.length) : str;
            if (run2.urlEntity instanceof TLRPC.TL_messageEntityBotCommand) {
                spannable.setSpan(new URLSpanBotCommand(url2, t, run2), run2.start, run2.end, 33);
                count = count3;
                count2 = 256;
            } else {
                if ((run2.urlEntity instanceof TLRPC.TL_messageEntityHashtag) || (run2.urlEntity instanceof TLRPC.TL_messageEntityMention)) {
                    count = count3;
                    url = url2;
                    count2 = 256;
                    i = 33;
                } else if (run2.urlEntity instanceof TLRPC.TL_messageEntityCashtag) {
                    count = count3;
                    url = url2;
                    count2 = 256;
                    i = 33;
                } else if (run2.urlEntity instanceof TLRPC.TL_messageEntityEmail) {
                    spannable.setSpan(new URLSpanReplacement(MailTo.MAILTO_SCHEME + url2, run2), run2.start, run2.end, 33);
                    count = count3;
                    count2 = 256;
                } else if (run2.urlEntity instanceof TLRPC.TL_messageEntityUrl) {
                    String lowerCase = url2.toLowerCase();
                    if (!lowerCase.contains("://")) {
                        spannable.setSpan(new URLSpanBrowser("http://" + url2, run2), run2.start, run2.end, 33);
                    } else {
                        spannable.setSpan(new URLSpanBrowser(url2, run2), run2.start, run2.end, 33);
                    }
                    count = count3;
                    hasUrls4 = true;
                    count2 = 256;
                } else if (run2.urlEntity instanceof TLRPC.TL_messageEntityBankCard) {
                    spannable.setSpan(new URLSpanNoUnderline("card:" + url2, run2), run2.start, run2.end, 33);
                    count = count3;
                    hasUrls4 = true;
                    count2 = 256;
                } else if (run2.urlEntity instanceof TLRPC.TL_messageEntityPhone) {
                    String tel = PhoneFormat.stripExceptNumbers(url2);
                    if (url2.startsWith("+")) {
                        tel = "+" + tel;
                    }
                    spannable.setSpan(new URLSpanBrowser("tel:" + tel, run2), run2.start, run2.end, 33);
                    count = count3;
                    hasUrls4 = true;
                    count2 = 256;
                } else if (run2.urlEntity instanceof TLRPC.TL_messageEntityTextUrl) {
                    spannable.setSpan(new URLSpanReplacement(run2.urlEntity.url, run2), run2.start, run2.end, 33);
                    count = count3;
                    count2 = 256;
                } else if (run2.urlEntity instanceof TLRPC.TL_messageEntityMentionName) {
                    spannable.setSpan(new URLSpanUserMention("" + ((TLRPC.TL_messageEntityMentionName) run2.urlEntity).user_id, t, run2), run2.start, run2.end, 33);
                    count = count3;
                    count2 = 256;
                } else if (run2.urlEntity instanceof TLRPC.TL_inputMessageEntityMentionName) {
                    spannable.setSpan(new URLSpanUserMention("" + ((TLRPC.TL_inputMessageEntityMentionName) run2.urlEntity).user_id.user_id, t, run2), run2.start, run2.end, 33);
                    count = count3;
                    count2 = 256;
                } else if ((run2.flags & 4) != 0) {
                    count = count3;
                    count2 = 256;
                    spannable.setSpan(new URLSpanMono(spannable, run2.start, run2.end, t == 1 ? (byte) 1 : (byte) 0, run2), run2.start, run2.end, 33);
                } else {
                    count = count3;
                    count2 = 256;
                    setRun = true;
                    spannable.setSpan(new TextStyleSpan(run2), run2.start, run2.end, 33);
                }
                spannable.setSpan(new URLSpanNoUnderline(url, run2), run2.start, run2.end, i);
            }
            if (!setRun && (run2.flags & count2) != 0) {
                spannable.setSpan(new TextStyleSpan(run2), run2.start, run2.end, 33);
            }
            a2++;
            str = null;
            charSequence = text;
            count3 = count;
        }
        return hasUrls4;
    }

    public static /* synthetic */ int lambda$addEntitiesToText$0(TLRPC.MessageEntity o1, TLRPC.MessageEntity o2) {
        if (o1.offset > o2.offset) {
            return 1;
        }
        if (o1.offset < o2.offset) {
            return -1;
        }
        return 0;
    }

    public boolean needDrawShareButton() {
        int i;
        if (!this.preview && !this.scheduled && this.eventId == 0 && !this.messageOwner.noforwards) {
            if (this.messageOwner.fwd_from != null && !isOutOwner() && this.messageOwner.fwd_from.saved_from_peer != null && getDialogId() == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                return true;
            }
            int i2 = this.type;
            if (i2 == 13 || i2 == 15) {
                return false;
            }
            if (this.messageOwner.fwd_from != null && (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChannel) && !isOutOwner()) {
                return true;
            }
            if (isFromUser()) {
                if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaEmpty) || this.messageOwner.media == null || ((this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && !(this.messageOwner.media.webpage instanceof TLRPC.TL_webPage))) {
                    return false;
                }
                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
                if (user != null && user.bot) {
                    return true;
                }
                if (!isOut()) {
                    if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaInvoice)) {
                        return true;
                    }
                    TLRPC.Chat chat = null;
                    if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
                        chat = getChat(null, null, this.messageOwner.peer_id.channel_id);
                    }
                    TLRPC.Chat chat2 = chat;
                    return ChatObject.isChannel(chat2) && chat2.megagroup && chat2.username != null && chat2.username.length() > 0 && !(this.messageOwner.media instanceof TLRPC.TL_messageMediaContact) && !(this.messageOwner.media instanceof TLRPC.TL_messageMediaGeo);
                }
            } else if (((this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) || this.messageOwner.post) && !isSupergroup() && this.messageOwner.peer_id.channel_id != 0 && ((this.messageOwner.via_bot_id == 0 && this.messageOwner.reply_to == null) || ((i = this.type) != 13 && i != 15))) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isYouTubeVideo() {
        return (this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && this.messageOwner.media.webpage != null && !TextUtils.isEmpty(this.messageOwner.media.webpage.embed_url) && "YouTube".equals(this.messageOwner.media.webpage.site_name);
    }

    public int getMaxMessageTextWidth() {
        int maxWidth;
        int maxWidth2 = 0;
        if (AndroidUtilities.isTablet() && this.eventId != 0) {
            this.generatedWithMinSize = AndroidUtilities.dp(530.0f);
        } else {
            this.generatedWithMinSize = AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : getParentWidth();
        }
        this.generatedWithDensity = AndroidUtilities.density;
        if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && this.messageOwner.media.webpage != null && "telegram_background".equals(this.messageOwner.media.webpage.type)) {
            try {
                Uri uri = Uri.parse(this.messageOwner.media.webpage.url);
                String segment = uri.getLastPathSegment();
                if (uri.getQueryParameter("bg_color") != null) {
                    maxWidth2 = AndroidUtilities.dp(220.0f);
                } else if (segment.length() == 6 || (segment.length() == 13 && segment.charAt(6) == '-')) {
                    maxWidth2 = AndroidUtilities.dp(200.0f);
                }
            } catch (Exception e) {
            }
        } else if (isAndroidTheme()) {
            maxWidth2 = AndroidUtilities.dp(200.0f);
        }
        if (maxWidth2 == 0) {
            int maxWidth3 = this.generatedWithMinSize - AndroidUtilities.dp((!needDrawAvatarInternal() || isOutOwner() || this.messageOwner.isThreadMessage) ? 80.0f : 132.0f);
            if (needDrawShareButton() && !isOutOwner()) {
                maxWidth = maxWidth3 - AndroidUtilities.dp(10.0f);
            } else {
                maxWidth = maxWidth3;
            }
            if (this.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                return maxWidth - AndroidUtilities.dp(10.0f);
            }
            return maxWidth;
        }
        return maxWidth2;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(23:82|(1:84)(1:85)|86|(3:88|(1:90)|94)(5:95|(1:97)(22:98|254|99|(3:258|101|(14:103|104|270|105|106|117|266|118|119|(2:236|121)|244|124|125|(3:127|264|128)(1:131)))|111|268|112|113|240|114|115|252|116|117|266|118|119|(0)|244|124|125|(0)(0))|224|273|225)|132|(1:134)|135|256|136|(2:138|(2:248|140))(1:142)|143|238|150|151|154|(1:156)|157|(1:159)|160|(6:162|(11:250|164|165|246|168|171|(1:173)(1:174)|175|(1:177)(1:178)|(4:262|182|183|(2:185|280)(1:279))|188)|275|189|(2:191|(1:193))(2:194|(1:196))|197)(3:198|(5:200|(1:202)|203|(1:205)(1:206)|207)(1:208)|209)|210|274|225) */
    /* JADX WARN: Can't wrap try/catch for region: R(7:(4:(9:(3:258|101|(14:103|104|270|105|106|117|266|118|119|(2:236|121)|244|124|125|(3:127|264|128)(1:131)))|266|118|119|(0)|244|124|125|(0)(0))|252|116|117)|268|112|113|240|114|115) */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x039a, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x039b, code lost:
        r12 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x03ae, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x03af, code lost:
        r14 = 0.0f;
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x054e, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x054f, code lost:
        r16 = r3;
        r20 = r4;
        r21 = r5;
        r22 = r6;
        r9 = r29;
        r10 = r31;
        r12 = r33;
        r3 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x006f, code lost:
        if (r34.messageOwner.send_state == 0) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x007d, code lost:
        if ((r34.messageOwner.media instanceof org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported) == false) goto L40;
     */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0346  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x036b  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x03c0  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x03c5  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x03dd  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x04c3  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x010e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0307 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x013c A[Catch: Exception -> 0x05b9, TRY_ENTER, TRY_LEAVE, TryCatch #13 {Exception -> 0x05b9, blocks: (B:65:0x0108, B:72:0x013c), top: B:260:0x0108 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0181  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void generateLayout(TLRPC.User fromUser) {
        boolean hasEntities;
        boolean z;
        boolean useManualParse;
        TextPaint paint;
        Exception e;
        TextPaint paint2;
        int i;
        StaticLayout textLayout;
        int blocksCount;
        int a;
        int currentBlockLinesCount;
        StaticLayout textLayout2;
        boolean hasUrls;
        boolean useManualParse2;
        boolean hasEntities2;
        TextPaint paint3;
        int linesOffset;
        int currentBlockLinesCount2;
        int a2;
        int linesMaxWidth;
        int maxWidth;
        int a3;
        int linesOffset2;
        TextLayoutBlock block;
        TextPaint paint4;
        StaticLayout textLayout3;
        float lastLeft;
        int linesMaxWidth2;
        int linesOffset3;
        int currentBlockLinesCount3;
        float lineWidth;
        float lineLeft;
        int linesOffset4;
        float lastLine;
        float lastLine2;
        float lineWidth2;
        int currentBlockLinesCount4;
        int linesOffset5;
        Exception e2;
        int currentBlockLinesCount5;
        int a4;
        int blocksCount2;
        StaticLayout textLayout4;
        int linesOffset6;
        MessageObject messageObject;
        if (this.type != 0 || this.messageOwner.peer_id == null || TextUtils.isEmpty(this.messageText)) {
            return;
        }
        generateLinkDescription();
        this.textLayoutBlocks = new ArrayList<>();
        this.textWidth = 0;
        int endCharacter = 1;
        if (this.messageOwner.send_state != 0) {
            hasEntities = false;
        } else {
            hasEntities = !this.messageOwner.entities.isEmpty();
        }
        try {
            if (!hasEntities) {
                if (this.eventId == 0) {
                    TLRPC.Message message = this.messageOwner;
                    if (!(message instanceof TLRPC.TL_message_old)) {
                        if (!(message instanceof TLRPC.TL_message_old2)) {
                            if (!(message instanceof TLRPC.TL_message_old3)) {
                                if (!(message instanceof TLRPC.TL_message_old4)) {
                                    if (!(message instanceof TLRPC.TL_messageForwarded_old)) {
                                        if (!(message instanceof TLRPC.TL_messageForwarded_old2)) {
                                            if (!(message instanceof TLRPC.TL_message_secret)) {
                                                if (!(message.media instanceof TLRPC.TL_messageMediaInvoice)) {
                                                    if (isOut()) {
                                                    }
                                                    if (this.messageOwner.id >= 0) {
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
                useManualParse = z;
                if (useManualParse) {
                    addLinks(isOutOwner(), this.messageText, true, true);
                }
                if (!isYouTubeVideo() || ((messageObject = this.replyMessageObject) != null && messageObject.isYouTubeVideo())) {
                    addUrlsByPattern(isOutOwner(), this.messageText, false, 3, Integer.MAX_VALUE, false);
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
                boolean hasUrls2 = addEntitiesToText(this.messageText, useManualParse);
                int maxWidth2 = getMaxMessageTextWidth();
                if (!(this.messageOwner.media instanceof TLRPC.TL_messageMediaGame)) {
                    paint = Theme.chat_msgGameTextPaint;
                } else {
                    paint = Theme.chat_msgTextPaint;
                }
                if (Build.VERSION.SDK_INT < 24) {
                    try {
                        CharSequence charSequence = this.messageText;
                        paint2 = paint;
                        i = 24;
                        textLayout = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), paint, maxWidth2).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
                    } catch (Exception e3) {
                        e = e3;
                        FileLog.e(e);
                        return;
                    }
                } else {
                    i = 24;
                    paint2 = paint;
                    try {
                        textLayout = new StaticLayout(this.messageText, paint, maxWidth2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    } catch (Exception e4) {
                        e = e4;
                        FileLog.e(e);
                        return;
                    }
                }
                this.textHeight = textLayout.getHeight();
                this.linesCount = textLayout.getLineCount();
                if (Build.VERSION.SDK_INT < i) {
                    blocksCount = 1;
                } else {
                    int blocksCount3 = this.linesCount;
                    blocksCount = (int) Math.ceil(blocksCount3 / 10.0f);
                }
                int maxWidth3 = 0;
                float prevOffset = 0.0f;
                a = 0;
                while (a < blocksCount) {
                    if (Build.VERSION.SDK_INT >= i) {
                        currentBlockLinesCount = this.linesCount;
                    } else {
                        currentBlockLinesCount = Math.min(10, this.linesCount - maxWidth3);
                    }
                    TextLayoutBlock block2 = new TextLayoutBlock();
                    if (blocksCount == endCharacter) {
                        block2.textLayout = textLayout;
                        block2.textYOffset = 0.0f;
                        block2.charactersOffset = 0;
                        block2.charactersEnd = textLayout.getText().length();
                        int i2 = this.emojiOnlyCount;
                        if (i2 != 0) {
                            switch (i2) {
                                case 1:
                                    this.textHeight -= AndroidUtilities.dp(5.3f);
                                    block2.textYOffset -= AndroidUtilities.dp(5.3f);
                                    break;
                                case 2:
                                    this.textHeight -= AndroidUtilities.dp(4.5f);
                                    block2.textYOffset -= AndroidUtilities.dp(4.5f);
                                    break;
                                case 3:
                                    this.textHeight -= AndroidUtilities.dp(4.2f);
                                    block2.textYOffset -= AndroidUtilities.dp(4.2f);
                                    break;
                            }
                        }
                        block2.height = this.textHeight;
                        block = block2;
                        a2 = a;
                        linesOffset2 = maxWidth3;
                        textLayout3 = textLayout;
                        maxWidth = maxWidth2;
                        paint4 = paint2;
                        a3 = currentBlockLinesCount;
                        currentBlockLinesCount2 = blocksCount;
                    } else {
                        int startCharacter = textLayout.getLineStart(maxWidth3);
                        int endCharacter2 = textLayout.getLineEnd((maxWidth3 + currentBlockLinesCount) - endCharacter);
                        if (endCharacter2 < startCharacter) {
                            hasEntities2 = hasEntities;
                            useManualParse2 = useManualParse;
                            hasUrls = hasUrls2;
                            a2 = a;
                            linesOffset5 = maxWidth3;
                            currentBlockLinesCount2 = blocksCount;
                            textLayout2 = textLayout;
                            linesOffset = maxWidth2;
                            paint3 = paint2;
                            endCharacter = 1;
                        } else {
                            block2.charactersOffset = startCharacter;
                            block2.charactersEnd = endCharacter2;
                            try {
                                SpannableStringBuilder sb = SpannableStringBuilder.valueOf(this.messageText.subSequence(startCharacter, endCharacter2));
                                try {
                                    try {
                                        try {
                                            try {
                                                if (hasUrls2) {
                                                    try {
                                                        if (Build.VERSION.SDK_INT >= i) {
                                                            StaticLayout textLayout5 = textLayout;
                                                            paint4 = paint2;
                                                            try {
                                                                block2.textLayout = StaticLayout.Builder.obtain(sb, 0, sb.length(), paint4, maxWidth2 + AndroidUtilities.dp(2.0f)).setBreakStrategy(1).setHyphenationFrequency(0).setAlignment(Layout.Alignment.ALIGN_NORMAL).build();
                                                                block = block2;
                                                                currentBlockLinesCount5 = currentBlockLinesCount;
                                                                a4 = a;
                                                                linesOffset5 = maxWidth3;
                                                                blocksCount2 = blocksCount;
                                                                maxWidth = maxWidth2;
                                                                textLayout2 = textLayout5;
                                                                linesOffset2 = linesOffset5;
                                                                textLayout3 = textLayout2;
                                                                block.textYOffset = textLayout3.getLineTop(linesOffset2);
                                                                a2 = a4;
                                                                if (a2 != 0) {
                                                                    try {
                                                                        block.height = (int) (block.textYOffset - prevOffset);
                                                                    } catch (Exception e5) {
                                                                        e2 = e5;
                                                                        textLayout2 = textLayout3;
                                                                        paint3 = paint4;
                                                                        hasEntities2 = hasEntities;
                                                                        useManualParse2 = useManualParse;
                                                                        hasUrls = hasUrls2;
                                                                        linesOffset5 = linesOffset2;
                                                                        currentBlockLinesCount2 = blocksCount2;
                                                                        linesOffset = maxWidth;
                                                                        endCharacter = 1;
                                                                        FileLog.e(e2);
                                                                        linesMaxWidth = linesOffset5;
                                                                        a = a2 + 1;
                                                                        blocksCount = currentBlockLinesCount2;
                                                                        maxWidth2 = linesOffset;
                                                                        paint2 = paint3;
                                                                        hasEntities = hasEntities2;
                                                                        useManualParse = useManualParse2;
                                                                        hasUrls2 = hasUrls;
                                                                        textLayout = textLayout2;
                                                                        i = 24;
                                                                        maxWidth3 = linesMaxWidth;
                                                                    }
                                                                }
                                                                block.height = Math.max(block.height, block.textLayout.getLineBottom(block.textLayout.getLineCount() - 1));
                                                                prevOffset = block.textYOffset;
                                                                currentBlockLinesCount2 = blocksCount2;
                                                                if (a2 == currentBlockLinesCount2 - 1) {
                                                                    a3 = currentBlockLinesCount5;
                                                                } else {
                                                                    a3 = Math.max(currentBlockLinesCount5, block.textLayout.getLineCount());
                                                                    try {
                                                                        this.textHeight = Math.max(this.textHeight, (int) (block.textYOffset + block.textLayout.getHeight()));
                                                                    } catch (Exception e6) {
                                                                        FileLog.e(e6);
                                                                    }
                                                                }
                                                            } catch (Exception e7) {
                                                                e2 = e7;
                                                                paint3 = paint4;
                                                                useManualParse2 = useManualParse;
                                                                hasUrls = hasUrls2;
                                                                a2 = a;
                                                                linesOffset5 = maxWidth3;
                                                                linesOffset = maxWidth2;
                                                                textLayout2 = textLayout5;
                                                                endCharacter = 1;
                                                                hasEntities2 = hasEntities;
                                                                currentBlockLinesCount2 = blocksCount;
                                                                FileLog.e(e2);
                                                                linesMaxWidth = linesOffset5;
                                                                a = a2 + 1;
                                                                blocksCount = currentBlockLinesCount2;
                                                                maxWidth2 = linesOffset;
                                                                paint2 = paint3;
                                                                hasEntities = hasEntities2;
                                                                useManualParse = useManualParse2;
                                                                hasUrls2 = hasUrls;
                                                                textLayout = textLayout2;
                                                                i = 24;
                                                                maxWidth3 = linesMaxWidth;
                                                            }
                                                        }
                                                    } catch (Exception e8) {
                                                        e2 = e8;
                                                        useManualParse2 = useManualParse;
                                                        hasUrls = hasUrls2;
                                                        a2 = a;
                                                        linesOffset5 = maxWidth3;
                                                        linesOffset = maxWidth2;
                                                        paint3 = paint2;
                                                        textLayout2 = textLayout;
                                                        endCharacter = 1;
                                                        hasEntities2 = hasEntities;
                                                        currentBlockLinesCount2 = blocksCount;
                                                    }
                                                }
                                                block.height = Math.max(block.height, block.textLayout.getLineBottom(block.textLayout.getLineCount() - 1));
                                                prevOffset = block.textYOffset;
                                                currentBlockLinesCount2 = blocksCount2;
                                                if (a2 == currentBlockLinesCount2 - 1) {
                                                }
                                            } catch (Exception e9) {
                                                e2 = e9;
                                                textLayout2 = textLayout3;
                                                paint3 = paint4;
                                                hasEntities2 = hasEntities;
                                                useManualParse2 = useManualParse;
                                                hasUrls = hasUrls2;
                                                linesOffset5 = linesOffset2;
                                                currentBlockLinesCount2 = blocksCount2;
                                                linesOffset = maxWidth;
                                                endCharacter = 1;
                                                FileLog.e(e2);
                                                linesMaxWidth = linesOffset5;
                                                a = a2 + 1;
                                                blocksCount = currentBlockLinesCount2;
                                                maxWidth2 = linesOffset;
                                                paint2 = paint3;
                                                hasEntities = hasEntities2;
                                                useManualParse = useManualParse2;
                                                hasUrls2 = hasUrls;
                                                textLayout = textLayout2;
                                                i = 24;
                                                maxWidth3 = linesMaxWidth;
                                            }
                                            block.textYOffset = textLayout3.getLineTop(linesOffset2);
                                            a2 = a4;
                                            if (a2 != 0) {
                                            }
                                        } catch (Exception e10) {
                                            e2 = e10;
                                            textLayout2 = textLayout3;
                                            paint3 = paint4;
                                            hasEntities2 = hasEntities;
                                            useManualParse2 = useManualParse;
                                            hasUrls = hasUrls2;
                                            linesOffset5 = linesOffset2;
                                            a2 = a4;
                                            currentBlockLinesCount2 = blocksCount2;
                                            linesOffset = maxWidth;
                                            endCharacter = 1;
                                        }
                                        block.textLayout = new StaticLayout(sb, 0, sb.length(), paint4, linesOffset6, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                        linesOffset2 = linesOffset5;
                                        textLayout3 = textLayout2;
                                    } catch (Exception e11) {
                                        e2 = e11;
                                        paint3 = paint4;
                                        hasEntities2 = hasEntities;
                                        useManualParse2 = useManualParse;
                                        hasUrls = hasUrls2;
                                        a2 = a4;
                                        currentBlockLinesCount2 = blocksCount2;
                                        linesOffset = maxWidth;
                                        endCharacter = 1;
                                        FileLog.e(e2);
                                        linesMaxWidth = linesOffset5;
                                        a = a2 + 1;
                                        blocksCount = currentBlockLinesCount2;
                                        maxWidth2 = linesOffset;
                                        paint2 = paint3;
                                        hasEntities = hasEntities2;
                                        useManualParse = useManualParse2;
                                        hasUrls2 = hasUrls;
                                        textLayout = textLayout2;
                                        i = 24;
                                        maxWidth3 = linesMaxWidth;
                                    }
                                    currentBlockLinesCount5 = currentBlockLinesCount;
                                    a4 = a;
                                    linesOffset5 = maxWidth3;
                                    linesOffset6 = maxWidth2;
                                    blocksCount2 = blocksCount;
                                    textLayout2 = textLayout4;
                                    maxWidth = maxWidth2;
                                    block = block2;
                                } catch (Exception e12) {
                                    e2 = e12;
                                    paint3 = paint4;
                                    useManualParse2 = useManualParse;
                                    hasUrls = hasUrls2;
                                    a2 = a;
                                    linesOffset5 = maxWidth3;
                                    linesOffset = maxWidth2;
                                    textLayout2 = textLayout4;
                                    endCharacter = 1;
                                    hasEntities2 = hasEntities;
                                    currentBlockLinesCount2 = blocksCount;
                                }
                                textLayout4 = textLayout;
                                paint4 = paint2;
                            } catch (Exception e13) {
                                e2 = e13;
                                hasEntities2 = hasEntities;
                                useManualParse2 = useManualParse;
                                hasUrls = hasUrls2;
                                a2 = a;
                                linesOffset5 = maxWidth3;
                                textLayout2 = textLayout;
                                linesOffset = maxWidth2;
                                paint3 = paint2;
                                endCharacter = 1;
                                currentBlockLinesCount2 = blocksCount;
                            }
                        }
                        linesMaxWidth = linesOffset5;
                        a = a2 + 1;
                        blocksCount = currentBlockLinesCount2;
                        maxWidth2 = linesOffset;
                        paint2 = paint3;
                        hasEntities = hasEntities2;
                        useManualParse = useManualParse2;
                        hasUrls2 = hasUrls;
                        textLayout = textLayout2;
                        i = 24;
                        maxWidth3 = linesMaxWidth;
                    }
                    block.spoilers.clear();
                    if (!this.isSpoilersRevealed) {
                        SpoilerEffect.addSpoilers(null, block.textLayout, null, block.spoilers);
                    }
                    this.textLayoutBlocks.add(block);
                    float lastLeft2 = block.textLayout.getLineLeft(a3 - 1);
                    if (a2 == 0) {
                        float f = 0.0f;
                        if (lastLeft2 >= 0.0f) {
                            try {
                                this.textXOffset = lastLeft2;
                            } catch (Exception e14) {
                                Exception e15 = e14;
                                lastLeft = 0.0f;
                                if (a2 == 0) {
                                    this.textXOffset = f;
                                }
                                FileLog.e(e15);
                                float lastLine3 = block.textLayout.getLineWidth(a3 - 1);
                                float lastLeft3 = lastLeft;
                                linesMaxWidth2 = (int) Math.ceil(lastLine3);
                                linesOffset = maxWidth;
                                if (linesMaxWidth2 > linesOffset + 80) {
                                }
                                if (a2 == currentBlockLinesCount2 - 1) {
                                }
                                textLayout2 = textLayout3;
                                paint3 = paint4;
                                int linesMaxWidthWithLeft = (int) Math.ceil(linesMaxWidth2 + Math.max(0.0f, lastLeft3));
                                if (a3 <= 1) {
                                }
                                linesMaxWidth = linesOffset3 + currentBlockLinesCount3;
                                a = a2 + 1;
                                blocksCount = currentBlockLinesCount2;
                                maxWidth2 = linesOffset;
                                paint2 = paint3;
                                hasEntities = hasEntities2;
                                useManualParse = useManualParse2;
                                hasUrls2 = hasUrls;
                                textLayout = textLayout2;
                                i = 24;
                                maxWidth3 = linesMaxWidth;
                            }
                        }
                    }
                    lastLeft = lastLeft2;
                    float lastLine32 = block.textLayout.getLineWidth(a3 - 1);
                    float lastLeft32 = lastLeft;
                    linesMaxWidth2 = (int) Math.ceil(lastLine32);
                    linesOffset = maxWidth;
                    if (linesMaxWidth2 > linesOffset + 80) {
                        linesMaxWidth2 = linesOffset;
                    }
                    if (a2 == currentBlockLinesCount2 - 1) {
                        this.lastLineWidth = linesMaxWidth2;
                    }
                    textLayout2 = textLayout3;
                    paint3 = paint4;
                    int linesMaxWidthWithLeft2 = (int) Math.ceil(linesMaxWidth2 + Math.max(0.0f, lastLeft32));
                    if (a3 <= 1) {
                        hasUrls = hasUrls2;
                        int n = 0;
                        useManualParse2 = useManualParse;
                        float textRealMaxWidthWithLeft = 0.0f;
                        hasEntities2 = hasEntities;
                        float textRealMaxWidth = 0.0f;
                        boolean hasNonRTL = false;
                        int linesMaxWidthWithLeft3 = linesMaxWidthWithLeft2;
                        int linesMaxWidth3 = linesMaxWidth2;
                        while (n < a3) {
                            try {
                                lineWidth = block.textLayout.getLineWidth(n);
                            } catch (Exception e16) {
                                FileLog.e(e16);
                                lineWidth = 0.0f;
                            }
                            try {
                                lineLeft = block.textLayout.getLineLeft(n);
                            } catch (Exception e17) {
                                FileLog.e(e17);
                                lineLeft = 0.0f;
                            }
                            float lineLeft2 = lineLeft;
                            if (lineWidth <= linesOffset + 20) {
                                linesOffset4 = linesOffset2;
                                lastLine = lastLine32;
                                lineWidth2 = lineWidth;
                                lastLine2 = lineLeft2;
                            } else {
                                linesOffset4 = linesOffset2;
                                lastLine = lastLine32;
                                lastLine2 = 0.0f;
                                lineWidth2 = linesOffset;
                            }
                            if (lastLine2 > 0.0f) {
                                this.textXOffset = Math.min(this.textXOffset, lastLine2);
                                currentBlockLinesCount4 = a3;
                                block.directionFlags = (byte) (block.directionFlags | 1);
                                this.hasRtl = true;
                            } else {
                                currentBlockLinesCount4 = a3;
                                block.directionFlags = (byte) (block.directionFlags | 2);
                            }
                            if (!hasNonRTL && lastLine2 == 0.0f) {
                                try {
                                    if (block.textLayout.getParagraphDirection(n) == 1) {
                                        hasNonRTL = true;
                                    }
                                } catch (Exception e18) {
                                    hasNonRTL = true;
                                }
                            }
                            float textRealMaxWidth2 = Math.max(textRealMaxWidth, lineWidth2);
                            float textRealMaxWidthWithLeft2 = Math.max(textRealMaxWidthWithLeft, lineWidth2 + lastLine2);
                            linesMaxWidth3 = Math.max(linesMaxWidth3, (int) Math.ceil(lineWidth2));
                            linesMaxWidthWithLeft3 = Math.max(linesMaxWidthWithLeft3, (int) Math.ceil(lineWidth2 + lastLine2));
                            n++;
                            textRealMaxWidth = textRealMaxWidth2;
                            textRealMaxWidthWithLeft = textRealMaxWidthWithLeft2;
                            a3 = currentBlockLinesCount4;
                            lastLine32 = lastLine;
                            linesOffset2 = linesOffset4;
                        }
                        linesOffset3 = linesOffset2;
                        currentBlockLinesCount3 = a3;
                        if (hasNonRTL) {
                            textRealMaxWidth = textRealMaxWidthWithLeft;
                            if (a2 == currentBlockLinesCount2 - 1) {
                                this.lastLineWidth = linesMaxWidthWithLeft2;
                            }
                        } else if (a2 == currentBlockLinesCount2 - 1) {
                            this.lastLineWidth = linesMaxWidth3;
                        }
                        this.textWidth = Math.max(this.textWidth, (int) Math.ceil(textRealMaxWidth));
                        endCharacter = 1;
                    } else {
                        hasEntities2 = hasEntities;
                        useManualParse2 = useManualParse;
                        hasUrls = hasUrls2;
                        linesOffset3 = linesOffset2;
                        currentBlockLinesCount3 = a3;
                        if (lastLeft32 > 0.0f) {
                            float min = Math.min(this.textXOffset, lastLeft32);
                            this.textXOffset = min;
                            if (min == 0.0f) {
                                linesMaxWidth2 = (int) (linesMaxWidth2 + lastLeft32);
                            }
                            endCharacter = 1;
                            this.hasRtl = currentBlockLinesCount2 != 1;
                            block.directionFlags = (byte) (block.directionFlags | 1);
                        } else {
                            endCharacter = 1;
                            block.directionFlags = (byte) (block.directionFlags | 2);
                        }
                        this.textWidth = Math.max(this.textWidth, Math.min(linesOffset, linesMaxWidth2));
                    }
                    linesMaxWidth = linesOffset3 + currentBlockLinesCount3;
                    a = a2 + 1;
                    blocksCount = currentBlockLinesCount2;
                    maxWidth2 = linesOffset;
                    paint2 = paint3;
                    hasEntities = hasEntities2;
                    useManualParse = useManualParse2;
                    hasUrls2 = hasUrls;
                    textLayout = textLayout2;
                    i = 24;
                    maxWidth3 = linesMaxWidth;
                }
                return;
            }
            if (Build.VERSION.SDK_INT < 24) {
            }
            this.textHeight = textLayout.getHeight();
            this.linesCount = textLayout.getLineCount();
            if (Build.VERSION.SDK_INT < i) {
            }
            int maxWidth32 = 0;
            float prevOffset2 = 0.0f;
            a = 0;
            while (a < blocksCount) {
            }
            return;
        } catch (Exception e19) {
            e = e19;
        }
        z = false;
        useManualParse = z;
        if (useManualParse) {
        }
        if (!isYouTubeVideo()) {
        }
        addUrlsByPattern(isOutOwner(), this.messageText, false, 3, Integer.MAX_VALUE, false);
        boolean hasUrls22 = addEntitiesToText(this.messageText, useManualParse);
        int maxWidth22 = getMaxMessageTextWidth();
        if (!(this.messageOwner.media instanceof TLRPC.TL_messageMediaGame)) {
        }
    }

    public boolean isOut() {
        return this.messageOwner.out;
    }

    public boolean isOutOwner() {
        if (this.preview) {
            return true;
        }
        TLRPC.Chat chat = null;
        if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
            chat = getChat(null, null, this.messageOwner.peer_id.channel_id);
        }
        TLRPC.Chat chat2 = chat;
        if (!this.messageOwner.out || ((!(this.messageOwner.from_id instanceof TLRPC.TL_peerUser) && (!(this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) || (ChatObject.isChannel(chat2) && !chat2.megagroup))) || this.messageOwner.post)) {
            return false;
        }
        if (this.messageOwner.fwd_from == null) {
            return true;
        }
        long selfUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (getDialogId() != selfUserId) {
            return this.messageOwner.fwd_from.saved_from_peer == null || this.messageOwner.fwd_from.saved_from_peer.user_id == selfUserId;
        } else if ((this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerUser) && this.messageOwner.fwd_from.from_id.user_id == selfUserId && (this.messageOwner.fwd_from.saved_from_peer == null || this.messageOwner.fwd_from.saved_from_peer.user_id == selfUserId)) {
            return true;
        } else {
            return this.messageOwner.fwd_from.saved_from_peer != null && this.messageOwner.fwd_from.saved_from_peer.user_id == selfUserId && (this.messageOwner.fwd_from.from_id == null || this.messageOwner.fwd_from.from_id.user_id == selfUserId);
        }
    }

    public boolean needDrawAvatar() {
        if (this.customAvatarDrawable != null) {
            return true;
        }
        if (isSponsored() && isFromChat()) {
            return true;
        }
        if (!isSponsored()) {
            if (isFromUser() || isFromGroup() || this.eventId != 0) {
                return true;
            }
            if (this.messageOwner.fwd_from != null && this.messageOwner.fwd_from.saved_from_peer != null) {
                return true;
            }
        }
        return false;
    }

    public boolean needDrawAvatarInternal() {
        if (this.customAvatarDrawable != null) {
            return true;
        }
        if (!isSponsored()) {
            if ((isFromChat() && isFromUser()) || isFromGroup() || this.eventId != 0) {
                return true;
            }
            if (this.messageOwner.fwd_from != null && this.messageOwner.fwd_from.saved_from_peer != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isFromChat() {
        if (getDialogId() == UserConfig.getInstance(this.currentAccount).clientUserId) {
            return true;
        }
        TLRPC.Chat chat = null;
        if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
            chat = getChat(null, null, this.messageOwner.peer_id.channel_id);
        }
        TLRPC.Chat chat2 = chat;
        if ((ChatObject.isChannel(chat2) && chat2.megagroup) || (this.messageOwner.peer_id != null && this.messageOwner.peer_id.chat_id != 0)) {
            return true;
        }
        return (this.messageOwner.peer_id == null || this.messageOwner.peer_id.channel_id == 0 || chat2 == null || !chat2.megagroup) ? false : true;
    }

    public static long getFromChatId(TLRPC.Message message) {
        return getPeerId(message.from_id);
    }

    public static long getPeerId(TLRPC.Peer peer) {
        if (peer == null) {
            return 0L;
        }
        if (peer instanceof TLRPC.TL_peerChat) {
            return -peer.chat_id;
        }
        if (peer instanceof TLRPC.TL_peerChannel) {
            return -peer.channel_id;
        }
        return peer.user_id;
    }

    public long getFromChatId() {
        return getFromChatId(this.messageOwner);
    }

    public long getChatId() {
        if (this.messageOwner.peer_id instanceof TLRPC.TL_peerChat) {
            return this.messageOwner.peer_id.chat_id;
        }
        if (this.messageOwner.peer_id instanceof TLRPC.TL_peerChannel) {
            return this.messageOwner.peer_id.channel_id;
        }
        return 0L;
    }

    public boolean isFromUser() {
        return (this.messageOwner.from_id instanceof TLRPC.TL_peerUser) && !this.messageOwner.post;
    }

    public boolean isFromGroup() {
        TLRPC.Chat chat = null;
        if (this.messageOwner.peer_id != null && this.messageOwner.peer_id.channel_id != 0) {
            chat = getChat(null, null, this.messageOwner.peer_id.channel_id);
        }
        TLRPC.Chat chat2 = chat;
        return (this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) && ChatObject.isChannel(chat2) && chat2.megagroup;
    }

    public boolean isForwardedChannelPost() {
        return (this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) && this.messageOwner.fwd_from != null && this.messageOwner.fwd_from.channel_post != 0 && (this.messageOwner.fwd_from.saved_from_peer instanceof TLRPC.TL_peerChannel) && this.messageOwner.from_id.channel_id == this.messageOwner.fwd_from.saved_from_peer.channel_id;
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

    public static int getUnreadFlags(TLRPC.Message message) {
        int flags = 0;
        if (!message.unread) {
            flags = 0 | 1;
        }
        if (!message.media_unread) {
            return flags | 2;
        }
        return flags;
    }

    public void setContentIsRead() {
        this.messageOwner.media_unread = false;
    }

    public int getId() {
        return this.messageOwner.id;
    }

    public int getRealId() {
        return this.messageOwner.realId != 0 ? this.messageOwner.realId : this.messageOwner.id;
    }

    public static long getMessageSize(TLRPC.Message message) {
        TLRPC.Document document;
        if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
            document = message.media.webpage.document;
        } else if (message.media instanceof TLRPC.TL_messageMediaGame) {
            document = message.media.game.document;
        } else {
            document = message.media != null ? message.media.document : null;
        }
        if (document != null) {
            return document.size;
        }
        return 0L;
    }

    public long getSize() {
        return getMessageSize(this.messageOwner);
    }

    public static void fixMessagePeer(ArrayList<TLRPC.Message> messages, long channelId) {
        if (messages == null || messages.isEmpty() || channelId == 0) {
            return;
        }
        for (int a = 0; a < messages.size(); a++) {
            TLRPC.Message message = messages.get(a);
            if (message instanceof TLRPC.TL_messageEmpty) {
                message.peer_id = new TLRPC.TL_peerChannel();
                message.peer_id.channel_id = channelId;
            }
        }
    }

    public long getChannelId() {
        return getChannelId(this.messageOwner);
    }

    public static long getChannelId(TLRPC.Message message) {
        if (message.peer_id != null) {
            return message.peer_id.channel_id;
        }
        return 0L;
    }

    public static boolean shouldEncryptPhotoOrVideo(TLRPC.Message message) {
        return message instanceof TLRPC.TL_message_secret ? ((message.media instanceof TLRPC.TL_messageMediaPhoto) || isVideoMessage(message)) && message.ttl > 0 && message.ttl <= 60 : ((message.media instanceof TLRPC.TL_messageMediaPhoto) || (message.media instanceof TLRPC.TL_messageMediaDocument)) && message.media.ttl_seconds != 0;
    }

    public boolean shouldEncryptPhotoOrVideo() {
        return shouldEncryptPhotoOrVideo(this.messageOwner);
    }

    public static boolean isSecretPhotoOrVideo(TLRPC.Message message) {
        if (message instanceof TLRPC.TL_message_secret) {
            return ((message.media instanceof TLRPC.TL_messageMediaPhoto) || isRoundVideoMessage(message) || isVideoMessage(message)) && message.ttl > 0 && message.ttl <= 60;
        } else if (!(message instanceof TLRPC.TL_message)) {
            return false;
        } else {
            return ((message.media instanceof TLRPC.TL_messageMediaPhoto) || (message.media instanceof TLRPC.TL_messageMediaDocument)) && message.media.ttl_seconds != 0;
        }
    }

    public static boolean isSecretMedia(TLRPC.Message message) {
        if (message instanceof TLRPC.TL_message_secret) {
            return ((message.media instanceof TLRPC.TL_messageMediaPhoto) || isRoundVideoMessage(message) || isVideoMessage(message)) && message.media.ttl_seconds != 0;
        } else if (!(message instanceof TLRPC.TL_message)) {
            return false;
        } else {
            return ((message.media instanceof TLRPC.TL_messageMediaPhoto) || (message.media instanceof TLRPC.TL_messageMediaDocument)) && message.media.ttl_seconds != 0;
        }
    }

    public boolean needDrawBluredPreview() {
        TLRPC.Message message = this.messageOwner;
        if (!(message instanceof TLRPC.TL_message_secret)) {
            return (message instanceof TLRPC.TL_message) && message.media != null && this.messageOwner.media.ttl_seconds != 0 && ((this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument));
        }
        int ttl = Math.max(message.ttl, this.messageOwner.media.ttl_seconds);
        return ttl > 0 && ((((this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) || isVideo() || isGif()) && ttl <= 60) || isRoundVideo());
    }

    public boolean isSecretMedia() {
        TLRPC.Message message = this.messageOwner;
        return message instanceof TLRPC.TL_message_secret ? (((message.media instanceof TLRPC.TL_messageMediaPhoto) || isGif()) && this.messageOwner.ttl > 0 && this.messageOwner.ttl <= 60) || isVoice() || isRoundVideo() || isVideo() : (message instanceof TLRPC.TL_message) && message.media != null && this.messageOwner.media.ttl_seconds != 0 && ((this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) || (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument));
    }

    public static void setUnreadFlags(TLRPC.Message message, int flag) {
        boolean z = false;
        message.unread = (flag & 1) == 0;
        if ((flag & 2) == 0) {
            z = true;
        }
        message.media_unread = z;
    }

    public static boolean isUnread(TLRPC.Message message) {
        return message.unread;
    }

    public static boolean isContentUnread(TLRPC.Message message) {
        return message.media_unread;
    }

    public boolean isSavedFromMegagroup() {
        if (this.messageOwner.fwd_from != null && this.messageOwner.fwd_from.saved_from_peer != null && this.messageOwner.fwd_from.saved_from_peer.channel_id != 0) {
            TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.saved_from_peer.channel_id));
            return ChatObject.isMegagroup(chat);
        }
        return false;
    }

    public static boolean isOut(TLRPC.Message message) {
        return message.out;
    }

    public long getDialogId() {
        return getDialogId(this.messageOwner);
    }

    public boolean canStreamVideo() {
        TLRPC.Document document = getDocument();
        if (document == null || (document instanceof TLRPC.TL_documentEncrypted)) {
            return false;
        }
        if (SharedConfig.streamAllVideo) {
            return true;
        }
        for (int a = 0; a < document.attributes.size(); a++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(a);
            if (attribute instanceof TLRPC.TL_documentAttributeVideo) {
                return attribute.supports_streaming;
            }
        }
        return SharedConfig.streamMkv && "video/x-matroska".equals(document.mime_type);
    }

    public static long getDialogId(TLRPC.Message message) {
        if (message.dialog_id == 0 && message.peer_id != null) {
            if (message.peer_id.chat_id != 0) {
                message.dialog_id = -message.peer_id.chat_id;
            } else if (message.peer_id.channel_id != 0) {
                message.dialog_id = -message.peer_id.channel_id;
            } else if (message.from_id == null || isOut(message)) {
                message.dialog_id = message.peer_id.user_id;
            } else {
                message.dialog_id = message.from_id.user_id;
            }
        }
        return message.dialog_id;
    }

    public boolean isSending() {
        return this.messageOwner.send_state == 1 && this.messageOwner.id < 0;
    }

    public boolean isEditing() {
        return this.messageOwner.send_state == 3 && this.messageOwner.id > 0;
    }

    public boolean isEditingMedia() {
        return this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto ? this.messageOwner.media.photo.id == 0 : (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) && this.messageOwner.media.document.dc_id == 0;
    }

    public boolean isSendError() {
        return (this.messageOwner.send_state == 2 && this.messageOwner.id < 0) || (this.scheduled && this.messageOwner.id > 0 && this.messageOwner.date < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + (-60));
    }

    public boolean isSent() {
        return this.messageOwner.send_state == 0 || this.messageOwner.id > 0;
    }

    public int getSecretTimeLeft() {
        int secondsLeft = this.messageOwner.ttl;
        if (this.messageOwner.destroyTime != 0) {
            int secondsLeft2 = Math.max(0, this.messageOwner.destroyTime - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
            return secondsLeft2;
        }
        return secondsLeft;
    }

    public String getSecretTimeString() {
        if (!isSecretMedia()) {
            return null;
        }
        int secondsLeft = getSecretTimeLeft();
        if (secondsLeft < 60) {
            String str = secondsLeft + "s";
            return str;
        }
        String str2 = (secondsLeft / 60) + "m";
        return str2;
    }

    public String getDocumentName() {
        return FileLoader.getDocumentFileName(getDocument());
    }

    public static boolean isWebM(TLRPC.Document document) {
        return document != null && MimeTypes.VIDEO_WEBM.equals(document.mime_type);
    }

    public static boolean isVideoSticker(TLRPC.Document document) {
        return document != null && isVideoStickerDocument(document);
    }

    public boolean isVideoSticker() {
        return getDocument() != null && isVideoStickerDocument(getDocument());
    }

    public static boolean isStickerDocument(TLRPC.Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (attribute instanceof TLRPC.TL_documentAttributeSticker) {
                    return "image/webp".equals(document.mime_type) || MimeTypes.VIDEO_WEBM.equals(document.mime_type);
                }
            }
        }
        return false;
    }

    public static boolean isVideoStickerDocument(TLRPC.Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (attribute instanceof TLRPC.TL_documentAttributeSticker) {
                    return MimeTypes.VIDEO_WEBM.equals(document.mime_type);
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isStickerHasSet(TLRPC.Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if ((attribute instanceof TLRPC.TL_documentAttributeSticker) && attribute.stickerset != null && !(attribute.stickerset instanceof TLRPC.TL_inputStickerSetEmpty)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isAnimatedStickerDocument(TLRPC.Document document, boolean allowWithoutSet) {
        if (document != null) {
            if (("application/x-tgsticker".equals(document.mime_type) && !document.thumbs.isEmpty()) || "application/x-tgsdice".equals(document.mime_type)) {
                if (allowWithoutSet) {
                    return true;
                }
                int N = document.attributes.size();
                for (int a = 0; a < N; a++) {
                    TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                    if (attribute instanceof TLRPC.TL_documentAttributeSticker) {
                        return attribute.stickerset instanceof TLRPC.TL_inputStickerSetShortName;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public static boolean canAutoplayAnimatedSticker(TLRPC.Document document) {
        return (isAnimatedStickerDocument(document, true) || isVideoStickerDocument(document)) && SharedConfig.getDevicePerformanceClass() != 0;
    }

    public static boolean isMaskDocument(TLRPC.Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if ((attribute instanceof TLRPC.TL_documentAttributeSticker) && attribute.mask) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isVoiceDocument(TLRPC.Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (attribute instanceof TLRPC.TL_documentAttributeAudio) {
                    return attribute.voice;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isVoiceWebDocument(WebFile webDocument) {
        return webDocument != null && webDocument.mime_type.equals("audio/ogg");
    }

    public static boolean isImageWebDocument(WebFile webDocument) {
        return webDocument != null && !isGifDocument(webDocument) && webDocument.mime_type.startsWith("image/");
    }

    public static boolean isVideoWebDocument(WebFile webDocument) {
        return webDocument != null && webDocument.mime_type.startsWith("video/");
    }

    public static boolean isMusicDocument(TLRPC.Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (attribute instanceof TLRPC.TL_documentAttributeAudio) {
                    return true ^ attribute.voice;
                }
            }
            if (!TextUtils.isEmpty(document.mime_type)) {
                String mime = document.mime_type.toLowerCase();
                if (mime.equals(MimeTypes.AUDIO_FLAC) || mime.equals("audio/ogg") || mime.equals(MimeTypes.AUDIO_OPUS) || mime.equals("audio/x-opus+ogg")) {
                    return true;
                }
                return mime.equals("application/octet-stream") && FileLoader.getDocumentFileName(document).endsWith(".opus");
            }
            return false;
        }
        return false;
    }

    public static TLRPC.VideoSize getDocumentVideoThumb(TLRPC.Document document) {
        if (document == null || document.video_thumbs.isEmpty()) {
            return null;
        }
        return document.video_thumbs.get(0);
    }

    public static boolean isVideoDocument(TLRPC.Document document) {
        if (document == null) {
            return false;
        }
        boolean isAnimated = false;
        boolean isVideo = false;
        int width = 0;
        int height = 0;
        for (int a = 0; a < document.attributes.size(); a++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(a);
            if (attribute instanceof TLRPC.TL_documentAttributeVideo) {
                if (attribute.round_message) {
                    return false;
                }
                isVideo = true;
                width = attribute.w;
                height = attribute.h;
            } else if (attribute instanceof TLRPC.TL_documentAttributeAnimated) {
                isAnimated = true;
            }
        }
        if (isAnimated && (width > 1280 || height > 1280)) {
            isAnimated = false;
        }
        if (SharedConfig.streamMkv && !isVideo && "video/x-matroska".equals(document.mime_type)) {
            isVideo = true;
        }
        return isVideo && !isAnimated;
    }

    public TLRPC.Document getDocument() {
        TLRPC.Document document = this.emojiAnimatedSticker;
        if (document != null) {
            return document;
        }
        return getDocument(this.messageOwner);
    }

    public static TLRPC.Document getDocument(TLRPC.Message message) {
        if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
            return message.media.webpage.document;
        }
        if (message.media instanceof TLRPC.TL_messageMediaGame) {
            return message.media.game.document;
        }
        if (message.media == null) {
            return null;
        }
        return message.media.document;
    }

    public static TLRPC.Photo getPhoto(TLRPC.Message message) {
        if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
            return message.media.webpage.photo;
        }
        if (message.media == null) {
            return null;
        }
        return message.media.photo;
    }

    public static boolean isStickerMessage(TLRPC.Message message) {
        return message.media != null && isStickerDocument(message.media.document);
    }

    public static boolean isAnimatedStickerMessage(TLRPC.Message message) {
        boolean isSecretChat = DialogObject.isEncryptedDialog(message.dialog_id);
        if ((!isSecretChat || message.stickerVerified == 1) && message.media != null) {
            return isAnimatedStickerDocument(message.media.document, !isSecretChat || message.out);
        }
        return false;
    }

    public static boolean isLocationMessage(TLRPC.Message message) {
        return (message.media instanceof TLRPC.TL_messageMediaGeo) || (message.media instanceof TLRPC.TL_messageMediaGeoLive) || (message.media instanceof TLRPC.TL_messageMediaVenue);
    }

    public static boolean isMaskMessage(TLRPC.Message message) {
        return message.media != null && isMaskDocument(message.media.document);
    }

    public static boolean isMusicMessage(TLRPC.Message message) {
        if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
            return isMusicDocument(message.media.webpage.document);
        }
        return message.media != null && isMusicDocument(message.media.document);
    }

    public static boolean isGifMessage(TLRPC.Message message) {
        if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
            return isGifDocument(message.media.webpage.document);
        }
        if (message.media != null) {
            if (isGifDocument(message.media.document, message.grouped_id != 0)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRoundVideoMessage(TLRPC.Message message) {
        if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
            return isRoundVideoDocument(message.media.webpage.document);
        }
        return message.media != null && isRoundVideoDocument(message.media.document);
    }

    public static boolean isPhoto(TLRPC.Message message) {
        if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
            return (message.media.webpage.photo instanceof TLRPC.TL_photo) && !(message.media.webpage.document instanceof TLRPC.TL_document);
        }
        return message.media instanceof TLRPC.TL_messageMediaPhoto;
    }

    public static boolean isVoiceMessage(TLRPC.Message message) {
        if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
            return isVoiceDocument(message.media.webpage.document);
        }
        return message.media != null && isVoiceDocument(message.media.document);
    }

    public static boolean isNewGifMessage(TLRPC.Message message) {
        if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
            return isNewGifDocument(message.media.webpage.document);
        }
        return message.media != null && isNewGifDocument(message.media.document);
    }

    public static boolean isLiveLocationMessage(TLRPC.Message message) {
        return message.media instanceof TLRPC.TL_messageMediaGeoLive;
    }

    public static boolean isVideoMessage(TLRPC.Message message) {
        if (message.media == null || !isVideoSticker(message.media.document)) {
            if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
                return isVideoDocument(message.media.webpage.document);
            }
            return message.media != null && isVideoDocument(message.media.document);
        }
        return false;
    }

    public static boolean isGameMessage(TLRPC.Message message) {
        return message.media instanceof TLRPC.TL_messageMediaGame;
    }

    public static boolean isInvoiceMessage(TLRPC.Message message) {
        return message.media instanceof TLRPC.TL_messageMediaInvoice;
    }

    public static TLRPC.InputStickerSet getInputStickerSet(TLRPC.Message message) {
        TLRPC.Document document = getDocument(message);
        if (document != null) {
            return getInputStickerSet(document);
        }
        return null;
    }

    public static TLRPC.InputStickerSet getInputStickerSet(TLRPC.Document document) {
        if (document == null) {
            return null;
        }
        int N = document.attributes.size();
        for (int a = 0; a < N; a++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(a);
            if (attribute instanceof TLRPC.TL_documentAttributeSticker) {
                if (attribute.stickerset instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return null;
                } else {
                    return attribute.stickerset;
                }
            }
        }
        return null;
    }

    public static long getStickerSetId(TLRPC.Document document) {
        if (document == null) {
            return -1L;
        }
        for (int a = 0; a < document.attributes.size(); a++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(a);
            if (attribute instanceof TLRPC.TL_documentAttributeSticker) {
                if (attribute.stickerset instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return -1L;
                } else {
                    return attribute.stickerset.id;
                }
            }
        }
        return -1L;
    }

    public static String getStickerSetName(TLRPC.Document document) {
        if (document == null) {
            return null;
        }
        for (int a = 0; a < document.attributes.size(); a++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(a);
            if (attribute instanceof TLRPC.TL_documentAttributeSticker) {
                if (attribute.stickerset instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return null;
                } else {
                    return attribute.stickerset.short_name;
                }
            }
        }
        return null;
    }

    public String getStickerChar() {
        TLRPC.Document document = getDocument();
        if (document != null) {
            Iterator<TLRPC.DocumentAttribute> it = document.attributes.iterator();
            while (it.hasNext()) {
                TLRPC.DocumentAttribute attribute = it.next();
                if (attribute instanceof TLRPC.TL_documentAttributeSticker) {
                    return attribute.alt;
                }
            }
            return null;
        }
        return null;
    }

    public int getApproximateHeight() {
        float maxWidth;
        int photoWidth;
        int i = this.type;
        if (i == 0) {
            int height = this.textHeight + ((!(this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) || !(this.messageOwner.media.webpage instanceof TLRPC.TL_webPage)) ? 0 : AndroidUtilities.dp(100.0f));
            if (isReply()) {
                return height + AndroidUtilities.dp(42.0f);
            }
            return height;
        } else if (i == 2) {
            return AndroidUtilities.dp(72.0f);
        } else {
            if (i == 12) {
                return AndroidUtilities.dp(71.0f);
            }
            if (i == 9) {
                return AndroidUtilities.dp(100.0f);
            }
            if (i == 4) {
                return AndroidUtilities.dp(114.0f);
            }
            if (i == 14) {
                return AndroidUtilities.dp(82.0f);
            }
            if (i == 10) {
                return AndroidUtilities.dp(30.0f);
            }
            if (i == 11) {
                return AndroidUtilities.dp(50.0f);
            }
            if (i == 5) {
                return AndroidUtilities.roundMessageSize;
            }
            if (i == 13 || i == 15) {
                float maxHeight = AndroidUtilities.displaySize.y * 0.4f;
                if (AndroidUtilities.isTablet()) {
                    maxWidth = AndroidUtilities.getMinTabletSide() * 0.5f;
                } else {
                    maxWidth = AndroidUtilities.displaySize.x * 0.5f;
                }
                int photoHeight = 0;
                int photoWidth2 = 0;
                TLRPC.Document document = getDocument();
                int a = 0;
                int N = document.attributes.size();
                while (true) {
                    if (a >= N) {
                        break;
                    }
                    TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                    if (!(attribute instanceof TLRPC.TL_documentAttributeImageSize)) {
                        a++;
                    } else {
                        photoWidth2 = attribute.w;
                        photoHeight = attribute.h;
                        break;
                    }
                }
                if (photoWidth2 == 0) {
                    photoHeight = (int) maxHeight;
                    photoWidth2 = photoHeight + AndroidUtilities.dp(100.0f);
                }
                if (photoHeight > maxHeight) {
                    photoWidth2 = (int) (photoWidth2 * (maxHeight / photoHeight));
                    photoHeight = (int) maxHeight;
                }
                if (photoWidth2 > maxWidth) {
                    photoHeight = (int) (photoHeight * (maxWidth / photoWidth2));
                }
                return AndroidUtilities.dp(14.0f) + photoHeight;
            }
            if (AndroidUtilities.isTablet()) {
                photoWidth = (int) (AndroidUtilities.getMinTabletSide() * 0.7f);
            } else {
                photoWidth = (int) (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) * 0.7f);
            }
            int photoHeight2 = AndroidUtilities.dp(100.0f) + photoWidth;
            if (photoWidth > AndroidUtilities.getPhotoSize()) {
                photoWidth = AndroidUtilities.getPhotoSize();
            }
            if (photoHeight2 > AndroidUtilities.getPhotoSize()) {
                photoHeight2 = AndroidUtilities.getPhotoSize();
            }
            TLRPC.PhotoSize currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
            if (currentPhotoObject != null) {
                float scale = currentPhotoObject.w / photoWidth;
                int h = (int) (currentPhotoObject.h / scale);
                if (h == 0) {
                    h = AndroidUtilities.dp(100.0f);
                }
                if (h > photoHeight2) {
                    h = photoHeight2;
                } else if (h < AndroidUtilities.dp(120.0f)) {
                    h = AndroidUtilities.dp(120.0f);
                }
                if (needDrawBluredPreview()) {
                    if (AndroidUtilities.isTablet()) {
                        h = (int) (AndroidUtilities.getMinTabletSide() * 0.5f);
                    } else {
                        h = (int) (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) * 0.5f);
                    }
                }
                photoHeight2 = h;
            }
            return AndroidUtilities.dp(14.0f) + photoHeight2;
        }
    }

    private int getParentWidth() {
        int i;
        return (!this.preview || (i = this.parentWidth) <= 0) ? AndroidUtilities.displaySize.x : i;
    }

    public String getStickerEmoji() {
        TLRPC.Document document = getDocument();
        if (document == null) {
            return null;
        }
        for (int a = 0; a < document.attributes.size(); a++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(a);
            if (attribute instanceof TLRPC.TL_documentAttributeSticker) {
                if (attribute.alt == null || attribute.alt.length() <= 0) {
                    return null;
                } else {
                    return attribute.alt;
                }
            }
        }
        return null;
    }

    public boolean isVideoCall() {
        return (this.messageOwner.action instanceof TLRPC.TL_messageActionPhoneCall) && this.messageOwner.action.video;
    }

    public boolean isAnimatedEmoji() {
        return this.emojiAnimatedSticker != null;
    }

    public boolean isDice() {
        return this.messageOwner.media instanceof TLRPC.TL_messageMediaDice;
    }

    public String getDiceEmoji() {
        if (!isDice()) {
            return null;
        }
        TLRPC.TL_messageMediaDice messageMediaDice = (TLRPC.TL_messageMediaDice) this.messageOwner.media;
        if (TextUtils.isEmpty(messageMediaDice.emoticon)) {
            return "🎲";
        }
        return messageMediaDice.emoticon.replace("️", "");
    }

    public int getDiceValue() {
        if (this.messageOwner.media instanceof TLRPC.TL_messageMediaDice) {
            return ((TLRPC.TL_messageMediaDice) this.messageOwner.media).value;
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
        boolean isSecretChat = DialogObject.isEncryptedDialog(getDialogId());
        if (isSecretChat && this.messageOwner.stickerVerified != 1) {
            return false;
        }
        TLRPC.Document document = getDocument();
        if (this.emojiAnimatedSticker != null || !isSecretChat || isOut()) {
            z = true;
        }
        return isAnimatedStickerDocument(document, z);
    }

    public boolean isAnyKindOfSticker() {
        int i = this.type;
        return i == 13 || i == 15;
    }

    public boolean shouldDrawWithoutBackground() {
        int i = this.type;
        return i == 13 || i == 15 || i == 5;
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

    public boolean isExpiredLiveLocation(int date) {
        return this.messageOwner.date + this.messageOwner.media.period <= date;
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
        if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
            return this.messageOwner.media.photo != null && this.messageOwner.media.photo.has_stickers;
        } else if (!(this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument)) {
            return false;
        } else {
            return isDocumentHasAttachedStickers(this.messageOwner.media.document);
        }
    }

    public static boolean isDocumentHasAttachedStickers(TLRPC.Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (attribute instanceof TLRPC.TL_documentAttributeHasStickers) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean isGif() {
        return isGifMessage(this.messageOwner);
    }

    public boolean isWebpageDocument() {
        return (this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && this.messageOwner.media.webpage.document != null && !isGifDocument(this.messageOwner.media.webpage.document);
    }

    public boolean isWebpage() {
        return this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage;
    }

    public boolean isNewGif() {
        return this.messageOwner.media != null && isNewGifDocument(getDocument());
    }

    public boolean isAndroidTheme() {
        if (this.messageOwner.media != null && this.messageOwner.media.webpage != null && !this.messageOwner.media.webpage.attributes.isEmpty()) {
            int N2 = this.messageOwner.media.webpage.attributes.size();
            for (int b = 0; b < N2; b++) {
                TLRPC.TL_webPageAttributeTheme attribute = this.messageOwner.media.webpage.attributes.get(b);
                ArrayList<TLRPC.Document> documents = attribute.documents;
                int N = documents.size();
                for (int a = 0; a < N; a++) {
                    TLRPC.Document document = documents.get(a);
                    if ("application/x-tgtheme-android".equals(document.mime_type)) {
                        return true;
                    }
                }
                if (attribute.settings != null) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public String getMusicTitle() {
        return getMusicTitle(true);
    }

    public String getMusicTitle(boolean unknown) {
        TLRPC.Document document = getDocument();
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (attribute instanceof TLRPC.TL_documentAttributeAudio) {
                    if (attribute.voice) {
                        if (!unknown) {
                            return null;
                        }
                        return LocaleController.formatDateAudio(this.messageOwner.date, true);
                    }
                    String title = attribute.title;
                    if (title == null || title.length() == 0) {
                        String title2 = FileLoader.getDocumentFileName(document);
                        if (TextUtils.isEmpty(title2) && unknown) {
                            return LocaleController.getString("AudioUnknownTitle", org.telegram.messenger.beta.R.string.AudioUnknownTitle);
                        }
                        return title2;
                    }
                    return title;
                } else if ((attribute instanceof TLRPC.TL_documentAttributeVideo) && attribute.round_message) {
                    return LocaleController.formatDateAudio(this.messageOwner.date, true);
                }
            }
            String fileName = FileLoader.getDocumentFileName(document);
            if (!TextUtils.isEmpty(fileName)) {
                return fileName;
            }
        }
        return LocaleController.getString("AudioUnknownTitle", org.telegram.messenger.beta.R.string.AudioUnknownTitle);
    }

    public int getDuration() {
        TLRPC.Document document = getDocument();
        if (document == null) {
            return 0;
        }
        int i = this.audioPlayerDuration;
        if (i > 0) {
            return i;
        }
        for (int a = 0; a < document.attributes.size(); a++) {
            TLRPC.DocumentAttribute attribute = document.attributes.get(a);
            if (attribute instanceof TLRPC.TL_documentAttributeAudio) {
                return attribute.duration;
            }
            if (attribute instanceof TLRPC.TL_documentAttributeVideo) {
                return attribute.duration;
            }
        }
        int a2 = this.audioPlayerDuration;
        return a2;
    }

    public String getArtworkUrl(boolean small) {
        TLRPC.Document document = getDocument();
        String str = null;
        if (document != null) {
            if ("audio/ogg".equals(document.mime_type)) {
                return null;
            }
            int N = document.attributes.size();
            for (int i = 0; i < N; i++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(i);
                if (attribute instanceof TLRPC.TL_documentAttributeAudio) {
                    if (attribute.voice) {
                        return str;
                    }
                    String performer = attribute.performer;
                    String title = attribute.title;
                    if (!TextUtils.isEmpty(performer)) {
                        int a = 0;
                        while (true) {
                            String[] strArr = excludeWords;
                            if (a >= strArr.length) {
                                break;
                            }
                            performer = performer.replace(strArr[a], " ");
                            a++;
                        }
                    }
                    if (TextUtils.isEmpty(performer) && TextUtils.isEmpty(title)) {
                        return str;
                    }
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("athumb://itunes.apple.com/search?term=");
                        sb.append(URLEncoder.encode(performer + " - " + title, "UTF-8"));
                        sb.append("&entity=song&limit=4");
                        sb.append(small ? "&s=1" : "");
                        return sb.toString();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return str;
    }

    public String getMusicAuthor() {
        return getMusicAuthor(true);
    }

    public String getMusicAuthor(boolean unknown) {
        TLRPC.Document document = getDocument();
        if (document != null) {
            boolean isVoice = false;
            for (int a = 0; a < document.attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                if (attribute instanceof TLRPC.TL_documentAttributeAudio) {
                    if (attribute.voice) {
                        isVoice = true;
                    } else {
                        String performer = attribute.performer;
                        if (TextUtils.isEmpty(performer) && unknown) {
                            return LocaleController.getString("AudioUnknownArtist", org.telegram.messenger.beta.R.string.AudioUnknownArtist);
                        }
                        return performer;
                    }
                } else if ((attribute instanceof TLRPC.TL_documentAttributeVideo) && attribute.round_message) {
                    isVoice = true;
                }
                if (isVoice) {
                    if (!unknown) {
                        return null;
                    }
                    if (isOutOwner() || (this.messageOwner.fwd_from != null && (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerUser) && this.messageOwner.fwd_from.from_id.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId())) {
                        return LocaleController.getString("FromYou", org.telegram.messenger.beta.R.string.FromYou);
                    }
                    TLRPC.User user = null;
                    TLRPC.Chat chat = null;
                    if (this.messageOwner.fwd_from != null && (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChannel)) {
                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.channel_id));
                    } else if (this.messageOwner.fwd_from != null && (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChat)) {
                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.chat_id));
                    } else if (this.messageOwner.fwd_from != null && (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerUser)) {
                        user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.fwd_from.from_id.user_id));
                    } else if (this.messageOwner.fwd_from != null && this.messageOwner.fwd_from.from_name != null) {
                        return this.messageOwner.fwd_from.from_name;
                    } else {
                        if (this.messageOwner.from_id instanceof TLRPC.TL_peerChat) {
                            chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.chat_id));
                        } else if (this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) {
                            chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.from_id.channel_id));
                        } else if (this.messageOwner.from_id == null && this.messageOwner.peer_id.channel_id != 0) {
                            chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.peer_id.channel_id));
                        } else {
                            user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.from_id.user_id));
                        }
                    }
                    if (user != null) {
                        return UserObject.getUserName(user);
                    }
                    if (chat != null) {
                        return chat.title;
                    }
                }
            }
        }
        return LocaleController.getString("AudioUnknownArtist", org.telegram.messenger.beta.R.string.AudioUnknownArtist);
    }

    public TLRPC.InputStickerSet getInputStickerSet() {
        return getInputStickerSet(this.messageOwner);
    }

    public boolean isForwarded() {
        return isForwardedMessage(this.messageOwner);
    }

    public boolean needDrawForwarded() {
        return (this.messageOwner.flags & 4) != 0 && this.messageOwner.fwd_from != null && !this.messageOwner.fwd_from.imported && (this.messageOwner.fwd_from.saved_from_peer == null || !(this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChannel) || this.messageOwner.fwd_from.saved_from_peer.channel_id != this.messageOwner.fwd_from.from_id.channel_id) && UserConfig.getInstance(this.currentAccount).getClientUserId() != getDialogId();
    }

    public static boolean isForwardedMessage(TLRPC.Message message) {
        return ((message.flags & 4) == 0 || message.fwd_from == null) ? false : true;
    }

    public boolean isReply() {
        MessageObject messageObject = this.replyMessageObject;
        return ((messageObject != null && (messageObject.messageOwner instanceof TLRPC.TL_messageEmpty)) || this.messageOwner.reply_to == null || (this.messageOwner.reply_to.reply_to_msg_id == 0 && this.messageOwner.reply_to.reply_to_random_id == 0) || (this.messageOwner.flags & 8) == 0) ? false : true;
    }

    public boolean isMediaEmpty() {
        return isMediaEmpty(this.messageOwner);
    }

    public boolean isMediaEmptyWebpage() {
        return isMediaEmptyWebpage(this.messageOwner);
    }

    public static boolean isMediaEmpty(TLRPC.Message message) {
        return message == null || message.media == null || (message.media instanceof TLRPC.TL_messageMediaEmpty) || (message.media instanceof TLRPC.TL_messageMediaWebPage);
    }

    public static boolean isMediaEmptyWebpage(TLRPC.Message message) {
        return message == null || message.media == null || (message.media instanceof TLRPC.TL_messageMediaEmpty);
    }

    public boolean hasReplies() {
        return this.messageOwner.replies != null && this.messageOwner.replies.replies > 0;
    }

    public boolean canViewThread() {
        MessageObject messageObject;
        if (this.messageOwner.action != null) {
            return false;
        }
        return hasReplies() || !(((messageObject = this.replyMessageObject) == null || messageObject.messageOwner.replies == null) && getReplyTopMsgId() == 0);
    }

    public boolean isComments() {
        return this.messageOwner.replies != null && this.messageOwner.replies.comments;
    }

    public boolean isLinkedToChat(long chatId) {
        return this.messageOwner.replies != null && (chatId == 0 || this.messageOwner.replies.channel_id == chatId);
    }

    public int getRepliesCount() {
        if (this.messageOwner.replies != null) {
            return this.messageOwner.replies.replies;
        }
        return 0;
    }

    public boolean canEditMessage(TLRPC.Chat chat) {
        return canEditMessage(this.currentAccount, this.messageOwner, chat, this.scheduled);
    }

    public boolean canEditMessageScheduleTime(TLRPC.Chat chat) {
        return canEditMessageScheduleTime(this.currentAccount, this.messageOwner, chat);
    }

    public boolean canForwardMessage() {
        return !(this.messageOwner instanceof TLRPC.TL_message_secret) && !needDrawBluredPreview() && !isLiveLocation() && this.type != 16 && !isSponsored() && !this.messageOwner.noforwards;
    }

    public boolean canEditMedia() {
        if (isSecretMedia()) {
            return false;
        }
        if (this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
            return true;
        }
        return (this.messageOwner.media instanceof TLRPC.TL_messageMediaDocument) && !isVoice() && !isSticker() && !isAnimatedSticker() && !isRoundVideo();
    }

    public boolean canEditMessageAnytime(TLRPC.Chat chat) {
        return canEditMessageAnytime(this.currentAccount, this.messageOwner, chat);
    }

    public static boolean canEditMessageAnytime(int currentAccount, TLRPC.Message message, TLRPC.Chat chat) {
        if (message == null || message.peer_id == null || ((message.media != null && (isRoundVideoDocument(message.media.document) || isStickerDocument(message.media.document) || isAnimatedStickerDocument(message.media.document, true))) || ((message.action != null && !(message.action instanceof TLRPC.TL_messageActionEmpty)) || isForwardedMessage(message) || message.via_bot_id != 0 || message.id < 0))) {
            return false;
        }
        if ((message.from_id instanceof TLRPC.TL_peerUser) && message.from_id.user_id == message.peer_id.user_id && message.from_id.user_id == UserConfig.getInstance(currentAccount).getClientUserId() && !isLiveLocationMessage(message)) {
            return true;
        }
        if (chat == null && message.peer_id.channel_id != 0 && (chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Long.valueOf(message.peer_id.channel_id))) == null) {
            return false;
        }
        if (ChatObject.isChannel(chat) && !chat.megagroup && (chat.creator || (chat.admin_rights != null && chat.admin_rights.edit_messages))) {
            return true;
        }
        return message.out && chat != null && chat.megagroup && (chat.creator || ((chat.admin_rights != null && chat.admin_rights.pin_messages) || (chat.default_banned_rights != null && !chat.default_banned_rights.pin_messages)));
    }

    public static boolean canEditMessageScheduleTime(int currentAccount, TLRPC.Message message, TLRPC.Chat chat) {
        if (chat == null && message.peer_id.channel_id != 0 && (chat = MessagesController.getInstance(currentAccount).getChat(Long.valueOf(message.peer_id.channel_id))) == null) {
            return false;
        }
        if (!ChatObject.isChannel(chat) || chat.megagroup || chat.creator) {
            return true;
        }
        return chat.admin_rights != null && (chat.admin_rights.edit_messages || message.out);
    }

    public static boolean canEditMessage(int currentAccount, TLRPC.Message message, TLRPC.Chat chat, boolean scheduled) {
        if (scheduled && message.date < ConnectionsManager.getInstance(currentAccount).getCurrentTime() - 60) {
            return false;
        }
        if ((chat != null && ((chat.left || chat.kicked) && (!chat.megagroup || !chat.has_link))) || message == null || message.peer_id == null || ((message.media != null && (isRoundVideoDocument(message.media.document) || isStickerDocument(message.media.document) || isAnimatedStickerDocument(message.media.document, true) || isLocationMessage(message))) || ((message.action != null && !(message.action instanceof TLRPC.TL_messageActionEmpty)) || isForwardedMessage(message) || message.via_bot_id != 0 || message.id < 0))) {
            return false;
        }
        if ((message.from_id instanceof TLRPC.TL_peerUser) && message.from_id.user_id == message.peer_id.user_id && message.from_id.user_id == UserConfig.getInstance(currentAccount).getClientUserId() && !isLiveLocationMessage(message) && !(message.media instanceof TLRPC.TL_messageMediaContact)) {
            return true;
        }
        if (chat == null && message.peer_id.channel_id != 0 && (chat = MessagesController.getInstance(currentAccount).getChat(Long.valueOf(message.peer_id.channel_id))) == null) {
            return false;
        }
        if (message.media != null && !(message.media instanceof TLRPC.TL_messageMediaEmpty) && !(message.media instanceof TLRPC.TL_messageMediaPhoto) && !(message.media instanceof TLRPC.TL_messageMediaDocument) && !(message.media instanceof TLRPC.TL_messageMediaWebPage)) {
            return false;
        }
        if (ChatObject.isChannel(chat) && !chat.megagroup && (chat.creator || (chat.admin_rights != null && chat.admin_rights.edit_messages))) {
            return true;
        }
        if (message.out && chat != null && chat.megagroup && (chat.creator || ((chat.admin_rights != null && chat.admin_rights.pin_messages) || (chat.default_banned_rights != null && !chat.default_banned_rights.pin_messages)))) {
            return true;
        }
        if (!scheduled && Math.abs(message.date - ConnectionsManager.getInstance(currentAccount).getCurrentTime()) > MessagesController.getInstance(currentAccount).maxEditTime) {
            return false;
        }
        if (message.peer_id.channel_id != 0) {
            return ((chat != null && chat.megagroup && message.out) || (chat != null && !chat.megagroup && ((chat.creator || (chat.admin_rights != null && (chat.admin_rights.edit_messages || (message.out && chat.admin_rights.post_messages)))) && message.post))) && ((message.media instanceof TLRPC.TL_messageMediaPhoto) || (((message.media instanceof TLRPC.TL_messageMediaDocument) && !isStickerMessage(message) && !isAnimatedStickerMessage(message)) || (message.media instanceof TLRPC.TL_messageMediaEmpty) || (message.media instanceof TLRPC.TL_messageMediaWebPage) || message.media == null));
        } else if (!message.out && (!(message.from_id instanceof TLRPC.TL_peerUser) || message.from_id.user_id != UserConfig.getInstance(currentAccount).getClientUserId())) {
            return false;
        } else {
            return (message.media instanceof TLRPC.TL_messageMediaPhoto) || ((message.media instanceof TLRPC.TL_messageMediaDocument) && !isStickerMessage(message) && !isAnimatedStickerMessage(message)) || (message.media instanceof TLRPC.TL_messageMediaEmpty) || (message.media instanceof TLRPC.TL_messageMediaWebPage) || message.media == null;
        }
    }

    public boolean canDeleteMessage(boolean inScheduleMode, TLRPC.Chat chat) {
        return this.eventId == 0 && this.sponsoredId == null && canDeleteMessage(this.currentAccount, inScheduleMode, this.messageOwner, chat);
    }

    public static boolean canDeleteMessage(int currentAccount, boolean inScheduleMode, TLRPC.Message message, TLRPC.Chat chat) {
        if (message == null) {
            return false;
        }
        if (ChatObject.isChannelAndNotMegaGroup(chat) && (message.action instanceof TLRPC.TL_messageActionChatJoinedByRequest)) {
            return false;
        }
        if (message.id < 0) {
            return true;
        }
        if (chat == null && message.peer_id.channel_id != 0) {
            chat = MessagesController.getInstance(currentAccount).getChat(Long.valueOf(message.peer_id.channel_id));
        }
        if (!ChatObject.isChannel(chat)) {
            return inScheduleMode || isOut(message) || !ChatObject.isChannel(chat);
        } else if (inScheduleMode && !chat.megagroup) {
            if (!chat.creator) {
                if (chat.admin_rights == null) {
                    return false;
                }
                if (!chat.admin_rights.delete_messages && !message.out) {
                    return false;
                }
            }
            return true;
        } else if (message.out && (message instanceof TLRPC.TL_messageService)) {
            return message.id != 1 && ChatObject.canUserDoAdminAction(chat, 13);
        } else {
            if (!inScheduleMode) {
                if (message.id == 1) {
                    return false;
                }
                if (!chat.creator && ((chat.admin_rights == null || (!chat.admin_rights.delete_messages && (!message.out || (!chat.megagroup && !chat.admin_rights.post_messages)))) && (!chat.megagroup || !message.out))) {
                    return false;
                }
            }
            return true;
        }
    }

    public String getForwardedName() {
        if (this.messageOwner.fwd_from != null) {
            if (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChannel) {
                TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.channel_id));
                if (chat != null) {
                    return chat.title;
                }
                return null;
            } else if (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChat) {
                TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.messageOwner.fwd_from.from_id.chat_id));
                if (chat2 != null) {
                    return chat2.title;
                }
                return null;
            } else if (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerUser) {
                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.messageOwner.fwd_from.from_id.user_id));
                if (user != null) {
                    return UserObject.getUserName(user);
                }
                return null;
            } else if (this.messageOwner.fwd_from.from_name != null) {
                return this.messageOwner.fwd_from.from_name;
            } else {
                return null;
            }
        }
        return null;
    }

    public int getReplyMsgId() {
        if (this.messageOwner.reply_to != null) {
            return this.messageOwner.reply_to.reply_to_msg_id;
        }
        return 0;
    }

    public int getReplyTopMsgId() {
        if (this.messageOwner.reply_to != null) {
            return this.messageOwner.reply_to.reply_to_top_id;
        }
        return 0;
    }

    public static long getReplyToDialogId(TLRPC.Message message) {
        if (message.reply_to == null) {
            return 0L;
        }
        if (message.reply_to.reply_to_peer_id != null) {
            return getPeerId(message.reply_to.reply_to_peer_id);
        }
        return getDialogId(message);
    }

    public int getReplyAnyMsgId() {
        if (this.messageOwner.reply_to != null) {
            if (this.messageOwner.reply_to.reply_to_top_id != 0) {
                return this.messageOwner.reply_to.reply_to_top_id;
            }
            return this.messageOwner.reply_to.reply_to_msg_id;
        }
        return 0;
    }

    public boolean isPrivateForward() {
        return this.messageOwner.fwd_from != null && !TextUtils.isEmpty(this.messageOwner.fwd_from.from_name);
    }

    public boolean isImportedForward() {
        return this.messageOwner.fwd_from != null && this.messageOwner.fwd_from.imported;
    }

    public long getSenderId() {
        if (this.messageOwner.fwd_from != null && this.messageOwner.fwd_from.saved_from_peer != null) {
            if (this.messageOwner.fwd_from.saved_from_peer.user_id != 0) {
                if (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerUser) {
                    return this.messageOwner.fwd_from.from_id.user_id;
                }
                return this.messageOwner.fwd_from.saved_from_peer.user_id;
            } else if (this.messageOwner.fwd_from.saved_from_peer.channel_id != 0) {
                if (isSavedFromMegagroup() && (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerUser)) {
                    return this.messageOwner.fwd_from.from_id.user_id;
                }
                if (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChannel) {
                    return -this.messageOwner.fwd_from.from_id.channel_id;
                }
                if (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChat) {
                    return -this.messageOwner.fwd_from.from_id.chat_id;
                }
                return -this.messageOwner.fwd_from.saved_from_peer.channel_id;
            } else if (this.messageOwner.fwd_from.saved_from_peer.chat_id != 0) {
                if (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerUser) {
                    return this.messageOwner.fwd_from.from_id.user_id;
                }
                if (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChannel) {
                    return -this.messageOwner.fwd_from.from_id.channel_id;
                }
                if (this.messageOwner.fwd_from.from_id instanceof TLRPC.TL_peerChat) {
                    return -this.messageOwner.fwd_from.from_id.chat_id;
                }
                return -this.messageOwner.fwd_from.saved_from_peer.chat_id;
            }
        } else if (this.messageOwner.from_id instanceof TLRPC.TL_peerUser) {
            return this.messageOwner.from_id.user_id;
        } else {
            if (this.messageOwner.from_id instanceof TLRPC.TL_peerChannel) {
                return -this.messageOwner.from_id.channel_id;
            }
            if (this.messageOwner.from_id instanceof TLRPC.TL_peerChat) {
                return -this.messageOwner.from_id.chat_id;
            }
            if (this.messageOwner.post) {
                return this.messageOwner.peer_id.channel_id;
            }
        }
        return 0L;
    }

    public boolean isWallpaper() {
        return (this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && this.messageOwner.media.webpage != null && "telegram_background".equals(this.messageOwner.media.webpage.type);
    }

    public boolean isTheme() {
        return (this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && this.messageOwner.media.webpage != null && "telegram_theme".equals(this.messageOwner.media.webpage.type);
    }

    public int getMediaExistanceFlags() {
        int flags = 0;
        if (this.attachPathExists) {
            flags = 0 | 1;
        }
        if (this.mediaExists) {
            return flags | 2;
        }
        return flags;
    }

    public void applyMediaExistanceFlags(int flags) {
        if (flags == -1) {
            checkMediaExistance();
            return;
        }
        boolean z = false;
        this.attachPathExists = (flags & 1) != 0;
        if ((flags & 2) != 0) {
            z = true;
        }
        this.mediaExists = z;
    }

    public void checkMediaExistance() {
        checkMediaExistance(true);
    }

    public void checkMediaExistance(boolean useFileDatabaseQueue) {
        TLRPC.Photo photo;
        int i;
        this.attachPathExists = false;
        this.mediaExists = false;
        if (this.type == 1 && FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize()) != null) {
            File file = FileLoader.getInstance(this.currentAccount).getPathToMessage(this.messageOwner, useFileDatabaseQueue);
            if (needDrawBluredPreview()) {
                this.mediaExists = new File(file.getAbsolutePath() + ".enc").exists();
            }
            if (!this.mediaExists) {
                this.mediaExists = file.exists();
            }
        }
        if ((!this.mediaExists && this.type == 8) || (i = this.type) == 3 || i == 9 || i == 2 || i == 14 || i == 5) {
            if (this.messageOwner.attachPath != null && this.messageOwner.attachPath.length() > 0) {
                File f = new File(this.messageOwner.attachPath);
                this.attachPathExists = f.exists();
            }
            if (!this.attachPathExists) {
                File file2 = FileLoader.getInstance(this.currentAccount).getPathToMessage(this.messageOwner, useFileDatabaseQueue);
                if (this.type == 3 && needDrawBluredPreview()) {
                    this.mediaExists = new File(file2.getAbsolutePath() + ".enc").exists();
                }
                if (!this.mediaExists) {
                    this.mediaExists = file2.exists();
                }
            }
        }
        if (!this.mediaExists) {
            TLRPC.Document document = getDocument();
            if (document != null) {
                if (!isWallpaper()) {
                    this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, false, useFileDatabaseQueue).exists();
                    return;
                } else {
                    this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(document, null, true, useFileDatabaseQueue).exists();
                    return;
                }
            }
            int i2 = this.type;
            if (i2 == 0) {
                TLRPC.PhotoSize currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
                if (currentPhotoObject != null) {
                    this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(currentPhotoObject, null, true, useFileDatabaseQueue).exists();
                }
            } else if (i2 == 11 && (photo = this.messageOwner.action.photo) != null && !photo.video_sizes.isEmpty()) {
                this.mediaExists = FileLoader.getInstance(this.currentAccount).getPathToAttach(photo.video_sizes.get(0), null, true, useFileDatabaseQueue).exists();
            }
        }
    }

    public void setQuery(String query) {
        String query2;
        if (TextUtils.isEmpty(query)) {
            this.highlightedWords = null;
            this.messageTrimmedToHighlight = null;
            return;
        }
        ArrayList<String> foundWords = new ArrayList<>();
        String query3 = query.trim().toLowerCase();
        String[] queryWord = query3.split("\\P{L}+");
        ArrayList<String> searchForWords = new ArrayList<>();
        if (!TextUtils.isEmpty(this.messageOwner.message)) {
            String message = this.messageOwner.message.trim().toLowerCase();
            if (message.contains(query3) && !foundWords.contains(query3)) {
                foundWords.add(query3);
                handleFoundWords(foundWords, queryWord);
                return;
            }
            String[] words = message.split("\\P{L}+");
            searchForWords.addAll(Arrays.asList(words));
        }
        if (getDocument() != null) {
            String fileName = FileLoader.getDocumentFileName(getDocument()).toLowerCase();
            if (fileName.contains(query3) && !foundWords.contains(query3)) {
                foundWords.add(query3);
            }
            String[] words2 = fileName.split("\\P{L}+");
            searchForWords.addAll(Arrays.asList(words2));
        }
        if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) && (this.messageOwner.media.webpage instanceof TLRPC.TL_webPage)) {
            TLRPC.WebPage webPage = this.messageOwner.media.webpage;
            String title = webPage.title;
            if (title == null) {
                title = webPage.site_name;
            }
            if (title != null) {
                String title2 = title.toLowerCase();
                if (title2.contains(query3) && !foundWords.contains(query3)) {
                    foundWords.add(query3);
                }
                String[] words3 = title2.split("\\P{L}+");
                searchForWords.addAll(Arrays.asList(words3));
            }
        }
        String musicAuthor = getMusicAuthor();
        if (musicAuthor != null) {
            String musicAuthor2 = musicAuthor.toLowerCase();
            if (musicAuthor2.contains(query3) && !foundWords.contains(query3)) {
                foundWords.add(query3);
            }
            String[] words4 = musicAuthor2.split("\\P{L}+");
            searchForWords.addAll(Arrays.asList(words4));
        }
        int k = 0;
        while (k < queryWord.length) {
            String currentQuery = queryWord[k];
            if (currentQuery.length() >= 2) {
                int i = 0;
                while (i < searchForWords.size()) {
                    if (foundWords.contains(searchForWords.get(i))) {
                        query2 = query3;
                    } else {
                        String word = searchForWords.get(i);
                        int startIndex = word.indexOf(currentQuery.charAt(0));
                        if (startIndex < 0) {
                            query2 = query3;
                        } else {
                            int l = Math.max(currentQuery.length(), word.length());
                            if (startIndex != 0) {
                                word = word.substring(startIndex);
                            }
                            int min = Math.min(currentQuery.length(), word.length());
                            int count = 0;
                            int j = 0;
                            while (true) {
                                if (j >= min) {
                                    query2 = query3;
                                    break;
                                }
                                query2 = query3;
                                if (word.charAt(j) != currentQuery.charAt(j)) {
                                    break;
                                }
                                count++;
                                j++;
                                query3 = query2;
                            }
                            if (count / l >= 0.5d) {
                                foundWords.add(searchForWords.get(i));
                            }
                        }
                    }
                    i++;
                    query3 = query2;
                }
            }
            k++;
            query3 = query3;
        }
        handleFoundWords(foundWords, queryWord);
    }

    private void handleFoundWords(ArrayList<String> foundWords, String[] queryWord) {
        if (!foundWords.isEmpty()) {
            boolean foundExactly = false;
            for (int i = 0; i < foundWords.size(); i++) {
                int j = 0;
                while (true) {
                    if (j < queryWord.length) {
                        if (!foundWords.get(i).contains(queryWord[j])) {
                            j++;
                        } else {
                            foundExactly = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (foundExactly) {
                    break;
                }
            }
            if (foundExactly) {
                int i2 = 0;
                while (i2 < foundWords.size()) {
                    boolean findMatch = false;
                    int j2 = 0;
                    while (true) {
                        if (j2 < queryWord.length) {
                            if (!foundWords.get(i2).contains(queryWord[j2])) {
                                j2++;
                            } else {
                                findMatch = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!findMatch) {
                        foundWords.remove(i2);
                        i2--;
                    }
                    i2++;
                }
                int i3 = foundWords.size();
                if (i3 > 0) {
                    Collections.sort(foundWords, MessageObject$$ExternalSyntheticLambda0.INSTANCE);
                    String s = foundWords.get(0);
                    foundWords.clear();
                    foundWords.add(s);
                }
            }
            this.highlightedWords = foundWords;
            if (this.messageOwner.message != null) {
                String str = this.messageOwner.message.replace('\n', ' ').replaceAll(" +", " ").trim();
                int lastIndex = str.length();
                int startHighlightedIndex = str.toLowerCase().indexOf(foundWords.get(0));
                if (startHighlightedIndex < 0) {
                    startHighlightedIndex = 0;
                }
                if (lastIndex > 200) {
                    int newStart = Math.max(0, startHighlightedIndex - (200 / 2));
                    str = str.substring(newStart, Math.min(lastIndex, (startHighlightedIndex - newStart) + startHighlightedIndex + (200 / 2)));
                }
                this.messageTrimmedToHighlight = str;
            }
        }
    }

    public static /* synthetic */ int lambda$handleFoundWords$1(String s, String s1) {
        return s1.length() - s.length();
    }

    public void createMediaThumbs() {
        if (isVideo()) {
            TLRPC.Document document = getDocument();
            TLRPC.PhotoSize thumb = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
            TLRPC.PhotoSize qualityThumb = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, GroupCallActivity.TABLET_LIST_SIZE);
            this.mediaThumb = ImageLocation.getForDocument(qualityThumb, document);
            this.mediaSmallThumb = ImageLocation.getForDocument(thumb, document);
        } else if ((this.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && this.messageOwner.media.photo != null && !this.photoThumbs.isEmpty()) {
            TLRPC.PhotoSize currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, 50);
            TLRPC.PhotoSize currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, GroupCallActivity.TABLET_LIST_SIZE, false, currentPhotoObjectThumb, false);
            this.mediaThumb = ImageLocation.getForObject(currentPhotoObject, this.photoThumbsObject);
            this.mediaSmallThumb = ImageLocation.getForObject(currentPhotoObjectThumb, this.photoThumbsObject);
        }
    }

    public boolean hasHighlightedWords() {
        ArrayList<String> arrayList = this.highlightedWords;
        return arrayList != null && !arrayList.isEmpty();
    }

    public boolean equals(MessageObject obj) {
        return getId() == obj.getId() && getDialogId() == obj.getDialogId();
    }

    public boolean isReactionsAvailable() {
        return !isEditing() && !isSponsored() && isSent() && this.messageOwner.action == null;
    }

    public boolean selectReaction(String reaction, boolean big, boolean fromDoubleTap) {
        if (this.messageOwner.reactions == null) {
            this.messageOwner.reactions = new TLRPC.TL_messageReactions();
            this.messageOwner.reactions.can_see_list = isFromGroup() || isFromUser();
        }
        TLRPC.TL_reactionCount choosenReaction = null;
        TLRPC.TL_reactionCount newReaction = null;
        for (int i = 0; i < this.messageOwner.reactions.results.size(); i++) {
            if (this.messageOwner.reactions.results.get(i).chosen) {
                TLRPC.TL_reactionCount choosenReaction2 = this.messageOwner.reactions.results.get(i);
                choosenReaction = choosenReaction2;
            }
            if (this.messageOwner.reactions.results.get(i).reaction.equals(reaction)) {
                TLRPC.TL_reactionCount newReaction2 = this.messageOwner.reactions.results.get(i);
                newReaction = newReaction2;
            }
        }
        if (choosenReaction == null || choosenReaction != newReaction || !big) {
            if (choosenReaction != null && (choosenReaction == newReaction || fromDoubleTap)) {
                choosenReaction.chosen = false;
                choosenReaction.count--;
                if (choosenReaction.count <= 0) {
                    this.messageOwner.reactions.results.remove(choosenReaction);
                }
                if (this.messageOwner.reactions.can_see_list) {
                    int i2 = 0;
                    while (i2 < this.messageOwner.reactions.recent_reactions.size()) {
                        if (getPeerId(this.messageOwner.reactions.recent_reactions.get(i2).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            this.messageOwner.reactions.recent_reactions.remove(i2);
                            i2--;
                        }
                        i2++;
                    }
                }
                this.reactionsChanged = true;
                return false;
            }
            if (choosenReaction != null) {
                choosenReaction.chosen = false;
                choosenReaction.count--;
                if (choosenReaction.count <= 0) {
                    this.messageOwner.reactions.results.remove(choosenReaction);
                }
                if (this.messageOwner.reactions.can_see_list) {
                    int i3 = 0;
                    while (i3 < this.messageOwner.reactions.recent_reactions.size()) {
                        if (getPeerId(this.messageOwner.reactions.recent_reactions.get(i3).peer_id) == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            this.messageOwner.reactions.recent_reactions.remove(i3);
                            i3--;
                        }
                        i3++;
                    }
                }
            }
            if (newReaction == null) {
                newReaction = new TLRPC.TL_reactionCount();
                newReaction.reaction = reaction;
                this.messageOwner.reactions.results.add(newReaction);
            }
            newReaction.chosen = true;
            newReaction.count++;
            if (this.messageOwner.reactions.can_see_list) {
                TLRPC.TL_messagePeerReaction action = new TLRPC.TL_messagePeerReaction();
                this.messageOwner.reactions.recent_reactions.add(0, action);
                action.peer_id = new TLRPC.TL_peerUser();
                action.peer_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                action.reaction = reaction;
            }
            this.reactionsChanged = true;
            return true;
        }
        return true;
    }

    public boolean probablyRingtone() {
        if (getDocument() != null && RingtoneDataStore.ringtoneSupportedMimeType.contains(getDocument().mime_type) && getDocument().size < MessagesController.getInstance(this.currentAccount).ringtoneSizeMax * 2) {
            for (int a = 0; a < getDocument().attributes.size(); a++) {
                TLRPC.DocumentAttribute attribute = getDocument().attributes.get(a);
                if ((attribute instanceof TLRPC.TL_documentAttributeAudio) && attribute.duration < 60) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
