package org.telegram.messenger;

import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.URLSpan;
import android.util.LongSparseArray;
import android.util.SparseBooleanArray;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.MessagePreviewView;

/* loaded from: classes3.dex */
public class MessagePreviewParams {
    public CharacterStyle currentLink;
    public Messages forwardMessages;
    public boolean hasCaption;
    public boolean hasMedia;
    public boolean hasSecretMessages;
    public boolean hasSenders;
    public boolean hideCaption;
    public boolean hideForwardSendersName;
    public boolean isSecret;
    public boolean isVideo;
    public TLRPC.WebPage linkMedia;
    public Messages linkMessage;
    public boolean multipleUsers;
    public boolean noforwards;
    private MessagePreviewView previewView;
    public ChatActivity.ReplyQuote quote;
    public int quoteEnd;
    public int quoteStart;
    public Messages replyMessage;
    public boolean singleLink;
    public TLRPC.WebPage webpage;
    public boolean webpagePhoto;
    public boolean webpageSmall;
    public boolean webpageTop;
    public boolean willSeeSenders;

    public class Messages {
        private long dialogId;
        public LongSparseArray<MessageObject.GroupedMessages> groupedMessagesMap;
        public boolean hasSpoilers;
        public boolean hasText;
        public ArrayList<MessageObject> messages;
        private Boolean out;
        public ArrayList<TLRPC.TL_pollAnswerVoters> pollChosenAnswers;
        public ArrayList<MessageObject> previewMessages;
        public SparseBooleanArray selectedIds;
        private int type;

        public Messages(Boolean bool, int i, ArrayList<MessageObject> arrayList, long j, SparseBooleanArray sparseBooleanArray) {
            this.groupedMessagesMap = new LongSparseArray<>();
            this.previewMessages = new ArrayList<>();
            this.selectedIds = new SparseBooleanArray();
            this.pollChosenAnswers = new ArrayList<>();
            this.out = bool;
            this.type = i;
            this.dialogId = j;
            this.messages = arrayList;
            if (sparseBooleanArray != null) {
                this.selectedIds = sparseBooleanArray;
            }
            int i2 = 0;
            int i3 = 0;
            while (i3 < arrayList.size()) {
                MessageObject messageObject = arrayList.get(i3);
                if (i == 0 && sparseBooleanArray == null) {
                    this.selectedIds.put(messageObject.getId(), true);
                }
                MessageObject previewMessage = MessagePreviewParams.this.toPreviewMessage(messageObject, bool, i);
                if (!this.hasSpoilers) {
                    Iterator<TLRPC.MessageEntity> it = previewMessage.messageOwner.entities.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (it.next() instanceof TLRPC.TL_messageEntitySpoiler) {
                                this.hasSpoilers = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                previewMessage.messageOwner.dialog_id = j;
                if (previewMessage.getGroupId() != 0) {
                    MessageObject.GroupedMessages groupedMessages = this.groupedMessagesMap.get(previewMessage.getGroupId(), null);
                    if (groupedMessages == null) {
                        groupedMessages = new MessageObject.GroupedMessages();
                        this.groupedMessagesMap.put(previewMessage.getGroupId(), groupedMessages);
                    }
                    groupedMessages.messages.add(previewMessage);
                }
                this.previewMessages.add(i2, previewMessage);
                if (messageObject.isPoll()) {
                    TLRPC.TL_messageMediaPoll tL_messageMediaPoll = (TLRPC.TL_messageMediaPoll) messageObject.messageOwner.media;
                    PreviewMediaPoll previewMediaPoll = new PreviewMediaPoll();
                    previewMediaPoll.poll = tL_messageMediaPoll.poll;
                    previewMediaPoll.provider = tL_messageMediaPoll.provider;
                    TLRPC.TL_pollResults tL_pollResults = new TLRPC.TL_pollResults();
                    previewMediaPoll.results = tL_pollResults;
                    int i4 = tL_messageMediaPoll.results.total_voters;
                    tL_pollResults.total_voters = i4;
                    previewMediaPoll.totalVotersCached = i4;
                    previewMessage.messageOwner.media = previewMediaPoll;
                    if (messageObject.canUnvote()) {
                        int size = tL_messageMediaPoll.results.results.size();
                        for (int i5 = 0; i5 < size; i5++) {
                            TLRPC.TL_pollAnswerVoters tL_pollAnswerVoters = tL_messageMediaPoll.results.results.get(i5);
                            if (tL_pollAnswerVoters.chosen) {
                                TLRPC.TL_pollAnswerVoters tL_pollAnswerVoters2 = new TLRPC.TL_pollAnswerVoters();
                                tL_pollAnswerVoters2.chosen = tL_pollAnswerVoters.chosen;
                                tL_pollAnswerVoters2.correct = tL_pollAnswerVoters.correct;
                                tL_pollAnswerVoters2.flags = tL_pollAnswerVoters.flags;
                                tL_pollAnswerVoters2.option = tL_pollAnswerVoters.option;
                                tL_pollAnswerVoters2.voters = tL_pollAnswerVoters.voters;
                                this.pollChosenAnswers.add(tL_pollAnswerVoters2);
                                previewMediaPoll.results.results.add(tL_pollAnswerVoters2);
                            } else {
                                previewMediaPoll.results.results.add(tL_pollAnswerVoters);
                            }
                        }
                    }
                }
                i3++;
                i2 = 0;
            }
            for (int i6 = 0; i6 < this.groupedMessagesMap.size(); i6++) {
                this.groupedMessagesMap.valueAt(i6).calculate();
            }
            LongSparseArray<MessageObject.GroupedMessages> longSparseArray = this.groupedMessagesMap;
            if (longSparseArray != null && longSparseArray.size() > 0) {
                this.hasText = this.groupedMessagesMap.valueAt(0).findCaptionMessageObject() != null;
            } else if (arrayList.size() == 1) {
                int i7 = arrayList.get(0).type;
                this.hasText = !TextUtils.isEmpty((i7 == 0 || i7 == 19) ? r1.messageText : r1.caption);
            }
        }

        public Messages(MessagePreviewParams messagePreviewParams, Boolean bool, int i, MessageObject messageObject) {
            this(bool, i, MessagePreviewParams.singletonArrayList(messageObject), messageObject.getDialogId(), null);
        }

        public Messages(MessagePreviewParams messagePreviewParams, Boolean bool, int i, MessageObject messageObject, long j) {
            this(bool, i, MessagePreviewParams.singletonArrayList(messageObject), j, null);
        }

        public Messages checkEdits(ArrayList<MessageObject> arrayList) {
            ArrayList<MessageObject> arrayList2 = this.messages;
            if (arrayList2 != null && arrayList2.size() <= 1 && arrayList != null) {
                boolean z = false;
                for (int i = 0; i < this.messages.size(); i++) {
                    MessageObject messageObject = this.messages.get(i);
                    if (messageObject != null) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= arrayList.size()) {
                                break;
                            }
                            MessageObject messageObject2 = arrayList.get(i2);
                            if (messageObject2 != null && messageObject.getId() == messageObject2.getId() && messageObject.getDialogId() == messageObject2.getDialogId()) {
                                this.messages.set(i, messageObject2);
                                z = true;
                                break;
                            }
                            i2++;
                        }
                    }
                }
                if (z) {
                    return MessagePreviewParams.this.new Messages(this.out, this.type, this.messages, this.dialogId, null);
                }
            }
            return null;
        }

        public void getSelectedMessages(ArrayList<MessageObject> arrayList) {
            arrayList.clear();
            for (int i = 0; i < this.messages.size(); i++) {
                MessageObject messageObject = this.messages.get(i);
                if (this.selectedIds.get(messageObject.getId(), false)) {
                    arrayList.add(messageObject);
                }
            }
        }
    }

    public static class PreviewMediaPoll extends TLRPC.TL_messageMediaPoll {
        public int totalVotersCached;
    }

    public MessagePreviewParams(boolean z, boolean z2) {
        this.isSecret = z;
        this.noforwards = z || z2;
    }

    public static boolean areUrlsEqual(String str, String str2) {
        if (str == null || str2 == null) {
            return str == null;
        }
        Uri parse = Uri.parse(str);
        Uri parse2 = Uri.parse(str2);
        if (parse == parse2) {
            return true;
        }
        if (parse != null && parse2 != null && parse.getHost() != null && parse.getHost().equalsIgnoreCase(parse2.getHost()) && parse.getPort() == parse2.getPort() && normalizePath(parse.getPath()).equals(normalizePath(parse2.getPath()))) {
            if (parse.getQuery() == null) {
                if (parse2.getQuery() == null) {
                    return true;
                }
            } else if (parse.getQuery().equals(parse2.getQuery())) {
                return true;
            }
        }
        return false;
    }

    private static String normalizePath(String str) {
        if (str == null) {
            return "";
        }
        if (str.endsWith("/")) {
            return str;
        }
        return str + "/";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ArrayList<MessageObject> singletonArrayList(MessageObject messageObject) {
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        arrayList.add(messageObject);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00ae, code lost:
    
        if (r12.isDice() == false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00b0, code lost:
    
        r11.hasSenders = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00b3, code lost:
    
        r11.willSeeSenders = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00d9, code lost:
    
        if (r12.isDice() == false) goto L29;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MessageObject toPreviewMessage(MessageObject messageObject, Boolean bool, final int i) {
        TLRPC.MessageFwdHeader messageFwdHeader;
        MessageObject messageObject2;
        TLRPC.TL_message tL_message = new TLRPC.TL_message();
        tL_message.date = i != 1 ? ConnectionsManager.getInstance(messageObject.currentAccount).getCurrentTime() : messageObject.messageOwner.date;
        TLRPC.Message message = messageObject.messageOwner;
        tL_message.id = message.id;
        tL_message.grouped_id = message.grouped_id;
        tL_message.peer_id = message.peer_id;
        tL_message.from_id = message.from_id;
        tL_message.message = message.message;
        tL_message.media = message.media;
        tL_message.action = message.action;
        tL_message.edit_date = 0;
        ArrayList<TLRPC.MessageEntity> arrayList = message.entities;
        if (arrayList != null) {
            tL_message.entities.addAll(arrayList);
        }
        boolean booleanValue = bool == null ? messageObject.messageOwner.out : bool.booleanValue();
        tL_message.out = booleanValue;
        if (booleanValue) {
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            tL_message.from_id = tL_peerUser;
            tL_peerUser.user_id = UserConfig.getInstance(messageObject.currentAccount).getClientUserId();
        }
        tL_message.unread = false;
        TLRPC.Message message2 = messageObject.messageOwner;
        tL_message.via_bot_id = message2.via_bot_id;
        tL_message.reply_markup = message2.reply_markup;
        tL_message.post = message2.post;
        tL_message.legacy = message2.legacy;
        tL_message.restriction_reason = message2.restriction_reason;
        TLRPC.Message message3 = message2.replyMessage;
        tL_message.replyMessage = message3;
        if (message3 == null && (messageObject2 = messageObject.replyMessageObject) != null) {
            tL_message.replyMessage = messageObject2.messageOwner;
        }
        tL_message.reply_to = message2.reply_to;
        tL_message.invert_media = message2.invert_media;
        if (i == 0) {
            long clientUserId = UserConfig.getInstance(messageObject.currentAccount).getClientUserId();
            if (!this.isSecret) {
                TLRPC.Message message4 = messageObject.messageOwner;
                messageFwdHeader = message4.fwd_from;
                if (messageFwdHeader == null) {
                    long j = message4.from_id.user_id;
                    if (j == 0 || message4.dialog_id != clientUserId || j != clientUserId) {
                        messageFwdHeader = new TLRPC.TL_messageFwdHeader();
                        messageFwdHeader.from_id = messageObject.messageOwner.from_id;
                    }
                }
                if (messageFwdHeader != null) {
                    tL_message.fwd_from = messageFwdHeader;
                    tL_message.flags |= 4;
                }
            }
            messageFwdHeader = null;
            if (messageFwdHeader != null) {
            }
        }
        MessageObject messageObject3 = new MessageObject(messageObject.currentAccount, tL_message, true, false) { // from class: org.telegram.messenger.MessagePreviewParams.1
            @Override // org.telegram.messenger.MessageObject
            public void generateLayout(TLRPC.User user) {
                super.generateLayout(user);
                if (i == 2) {
                    MessagePreviewParams.this.checkCurrentLink(this);
                }
            }

            @Override // org.telegram.messenger.MessageObject
            public boolean needDrawForwarded() {
                if (MessagePreviewParams.this.hideForwardSendersName) {
                    return false;
                }
                return super.needDrawForwarded();
            }
        };
        messageObject3.previewForward = i == 0;
        messageObject3.preview = true;
        return messageObject3;
    }

    public void attach(MessagePreviewView messagePreviewView) {
        this.previewView = messagePreviewView;
    }

    public void checkCurrentLink(MessageObject messageObject) {
        TLRPC.WebPage webPage;
        this.currentLink = null;
        if (messageObject != null) {
            CharSequence charSequence = messageObject.messageText;
            if (!(charSequence instanceof Spanned) || (webPage = this.webpage) == null || webPage.url == null) {
                return;
            }
            Spanned spanned = (Spanned) charSequence;
            URLSpan[] uRLSpanArr = (URLSpan[]) spanned.getSpans(0, spanned.length(), URLSpan.class);
            for (int i = 0; i < uRLSpanArr.length; i++) {
                if (areUrlsEqual(uRLSpanArr[i].getURL(), this.webpage.url)) {
                    this.currentLink = uRLSpanArr[i];
                    return;
                }
            }
        }
    }

    public void checkEdits(ArrayList<MessageObject> arrayList) {
        boolean z;
        MessagePreviewView messagePreviewView;
        Messages checkEdits;
        Messages checkEdits2;
        Messages checkEdits3;
        Messages messages = this.forwardMessages;
        boolean z2 = true;
        if (messages == null || (checkEdits3 = messages.checkEdits(arrayList)) == null) {
            z = false;
        } else {
            this.forwardMessages = checkEdits3;
            z = true;
        }
        Messages messages2 = this.replyMessage;
        if (messages2 != null && (checkEdits2 = messages2.checkEdits(arrayList)) != null) {
            this.replyMessage = checkEdits2;
            z = true;
        }
        Messages messages3 = this.linkMessage;
        if (messages3 == null || (checkEdits = messages3.checkEdits(arrayList)) == null) {
            z2 = z;
        } else {
            this.linkMessage = checkEdits;
        }
        if (!z2 || (messagePreviewView = this.previewView) == null) {
            return;
        }
        messagePreviewView.updateAll();
    }

    public boolean hasLink(CharSequence charSequence, String str) {
        if (str != null) {
            try {
                SpannableString valueOf = SpannableString.valueOf(charSequence);
                try {
                    AndroidUtilities.addLinksSafe(valueOf, 1, false, true);
                } catch (Exception e) {
                    FileLog.e(e);
                }
                for (URLSpan uRLSpan : (URLSpan[]) valueOf.getSpans(0, valueOf.length(), URLSpan.class)) {
                    if (areUrlsEqual(uRLSpan.getURL(), str)) {
                        return true;
                    }
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        return false;
    }

    public boolean isEmpty() {
        Messages messages;
        Messages messages2;
        ArrayList<MessageObject> arrayList;
        ArrayList<MessageObject> arrayList2;
        ArrayList<MessageObject> arrayList3;
        Messages messages3 = this.forwardMessages;
        return (messages3 == null || (arrayList3 = messages3.messages) == null || arrayList3.isEmpty()) && ((messages = this.replyMessage) == null || (arrayList2 = messages.messages) == null || arrayList2.isEmpty()) && ((messages2 = this.linkMessage) == null || (arrayList = messages2.messages) == null || arrayList.isEmpty());
    }

    public void updateForward(ArrayList<MessageObject> arrayList, long j) {
        long j2;
        TLRPC.MessageFwdHeader messageFwdHeader;
        this.hasCaption = false;
        this.hasSenders = false;
        this.isSecret = DialogObject.isEncryptedDialog(j);
        this.multipleUsers = false;
        if (arrayList == null) {
            this.forwardMessages = null;
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = arrayList.get(i);
            if (!TextUtils.isEmpty(messageObject.caption)) {
                this.hasCaption = true;
            }
            if (!this.isSecret && (messageFwdHeader = messageObject.messageOwner.fwd_from) != null && messageFwdHeader.from_id == null && !arrayList2.contains(messageFwdHeader.from_name)) {
                arrayList2.add(messageFwdHeader.from_name);
            }
        }
        Boolean bool = Boolean.TRUE;
        Messages messages = this.forwardMessages;
        Messages messages2 = new Messages(bool, 0, arrayList, j, messages != null ? messages.selectedIds : null);
        this.forwardMessages = messages2;
        if (messages2.messages.isEmpty()) {
            this.forwardMessages = null;
        }
        ArrayList arrayList3 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            MessageObject messageObject2 = arrayList.get(i2);
            if (messageObject2.isFromUser()) {
                j2 = messageObject2.messageOwner.from_id.user_id;
            } else {
                TLRPC.Chat chat = MessagesController.getInstance(messageObject2.currentAccount).getChat(Long.valueOf(messageObject2.messageOwner.peer_id.channel_id));
                j2 = -((ChatObject.isChannel(chat) && chat.megagroup && messageObject2.isForwardedChannelPost()) ? messageObject2.messageOwner.fwd_from.from_id : messageObject2.messageOwner.peer_id).channel_id;
            }
            if (!arrayList3.contains(Long.valueOf(j2))) {
                arrayList3.add(Long.valueOf(j2));
            }
        }
        if (arrayList3.size() + arrayList2.size() > 1) {
            this.multipleUsers = true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x0131, code lost:
    
        if (r9.length > 1) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0156, code lost:
    
        r9 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0153, code lost:
    
        if (r9.length > 1) goto L64;
     */
    /* JADX WARN: Removed duplicated region for block: B:11:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0185  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateLink(int i, TLRPC.WebPage webPage, CharSequence charSequence, MessageObject messageObject, ChatActivity.ReplyQuote replyQuote, MessageObject messageObject2) {
        boolean z;
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        TLRPC.Message message2;
        TLRPC.MessageMedia messageMedia2;
        TLRPC.Message message3;
        MessagePreviewView messagePreviewView;
        boolean z2 = false;
        this.hasMedia = false;
        this.isVideo = false;
        this.singleLink = true;
        boolean z3 = this.webpage != webPage;
        this.webpage = webPage;
        if (!TextUtils.isEmpty(charSequence) || this.webpage != null) {
            if (charSequence == null) {
                charSequence = "";
            }
            Messages messages = this.linkMessage;
            boolean z4 = messages == null || z3;
            if (messages == null && messageObject2 != null && (message3 = messageObject2.messageOwner) != null) {
                this.webpageTop = message3.invert_media;
                TLRPC.MessageMedia messageMedia3 = message3.media;
                if (messageMedia3 != null && messageMedia3.force_small_media) {
                    this.webpageSmall = true;
                }
            }
            TLRPC.TL_message tL_message = new TLRPC.TL_message();
            CharSequence[] charSequenceArr = {new SpannableStringBuilder(AndroidUtilities.getTrimmedString(charSequence))};
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            tL_message.peer_id = tL_peerUser;
            tL_peerUser.user_id = UserConfig.getInstance(i).getClientUserId();
            TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
            tL_message.from_id = tL_peerUser2;
            tL_peerUser2.user_id = UserConfig.getInstance(i).getClientUserId();
            tL_message.entities = MediaDataController.getInstance(i).getEntities(charSequenceArr, true);
            tL_message.message = charSequenceArr[0].toString();
            tL_message.invert_media = this.webpageTop;
            if (webPage != null) {
                tL_message.flags |= 512;
                TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage = new TLRPC.TL_messageMediaWebPage();
                tL_message.media = tL_messageMediaWebPage;
                tL_messageMediaWebPage.webpage = webPage;
                boolean z5 = this.webpageSmall;
                tL_messageMediaWebPage.force_large_media = !z5;
                tL_messageMediaWebPage.force_small_media = z5;
                this.hasMedia = webPage.photo != null;
                this.isVideo = MessageObject.isVideoDocument(webPage.document);
            } else {
                this.hasMedia = false;
            }
            tL_message.out = true;
            tL_message.unread = false;
            if (messageObject != null) {
                tL_message.replyMessage = messageObject.messageOwner;
                TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
                tL_message.reply_to = tL_messageReplyHeader;
                if (replyQuote != null) {
                    tL_messageReplyHeader.quote_text = replyQuote.getText();
                    TLRPC.MessageReplyHeader messageReplyHeader = tL_message.reply_to;
                    messageReplyHeader.flags |= 64;
                    messageReplyHeader.quote_entities = replyQuote.getEntities();
                    TLRPC.MessageReplyHeader messageReplyHeader2 = tL_message.reply_to;
                    if (messageReplyHeader2.quote_entities != null) {
                        messageReplyHeader2.flags |= 128;
                    }
                }
            }
            Messages messages2 = new Messages(this, Boolean.TRUE, 2, new MessageObject(i, tL_message, true, false));
            this.linkMessage = messages2;
            if (!messages2.messages.isEmpty()) {
                MessageObject messageObject3 = this.linkMessage.messages.get(0);
                CharSequence charSequence2 = messageObject3.messageText;
                if (!(charSequence2 instanceof Spanned) || TextUtils.isEmpty(charSequence2)) {
                    CharSequence charSequence3 = messageObject3.caption;
                    if ((charSequence3 instanceof Spanned) && !TextUtils.isEmpty(charSequence3)) {
                        URLSpan[] uRLSpanArr = (URLSpan[]) ((Spanned) messageObject3.messageText).getSpans(0, messageObject3.caption.length(), URLSpan.class);
                        if (uRLSpanArr != null) {
                        }
                        z = true;
                    }
                    this.hasMedia = messageObject3.hasLinkMediaToMakeSmall();
                    if (!z4 && messageObject2 != null && (message2 = messageObject2.messageOwner) != null && (messageMedia2 = message2.media) != null) {
                        if (messageMedia2.force_small_media || (messageObject3.isLinkMediaSmall() && !messageObject2.messageOwner.media.force_large_media)) {
                            z2 = true;
                        }
                        this.webpageSmall = z2;
                    } else if (z4) {
                        this.webpageSmall = messageObject3.isLinkMediaSmall();
                    }
                    message = messageObject3.messageOwner;
                    if (message != null && (messageMedia = message.media) != null) {
                        boolean z6 = this.webpageSmall;
                        messageMedia.force_large_media = !z6;
                        messageMedia.force_small_media = z6;
                    }
                    messagePreviewView = this.previewView;
                    if (messagePreviewView != null) {
                        messagePreviewView.updateLink();
                        return;
                    }
                    return;
                }
                CharSequence charSequence4 = messageObject3.messageText;
                URLSpan[] uRLSpanArr2 = (URLSpan[]) ((Spanned) charSequence4).getSpans(0, charSequence4.length(), URLSpan.class);
                if (uRLSpanArr2 != null) {
                }
                z = true;
                this.singleLink = z;
                this.hasMedia = messageObject3.hasLinkMediaToMakeSmall();
                if (!z4) {
                }
                if (z4) {
                }
                message = messageObject3.messageOwner;
                if (message != null) {
                    boolean z62 = this.webpageSmall;
                    messageMedia.force_large_media = !z62;
                    messageMedia.force_small_media = z62;
                }
                messagePreviewView = this.previewView;
                if (messagePreviewView != null) {
                }
            }
        }
        this.linkMessage = null;
        messagePreviewView = this.previewView;
        if (messagePreviewView != null) {
        }
    }

    public void updateLinkInvertMedia(boolean z) {
        this.webpageTop = z;
    }

    public void updateReply(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages, long j, ChatActivity.ReplyQuote replyQuote) {
        ChatActivity.ReplyQuote replyQuote2;
        int i;
        MessageObject messageObject2 = messageObject;
        if (this.isSecret || messageObject2 == null || (i = messageObject2.type) == 10 || i == 11 || i == 22 || i == 21 || i == 18 || i == 25 || i == 16) {
            messageObject2 = null;
            replyQuote2 = null;
        } else {
            replyQuote2 = replyQuote;
        }
        this.hasSecretMessages = messageObject2 != null && (messageObject2.isVoiceOnce() || messageObject2.isRoundOnce() || messageObject2.type == 30);
        if (messageObject2 == null && replyQuote2 == null) {
            this.replyMessage = null;
            this.quote = null;
            return;
        }
        if (groupedMessages != null) {
            this.replyMessage = new Messages(null, 1, groupedMessages.messages, j, null);
        } else {
            if (messageObject2 == null) {
                messageObject2 = replyQuote2.message;
            }
            this.replyMessage = new Messages(this, null, 1, messageObject2, j);
        }
        if (this.replyMessage.messages.isEmpty()) {
            this.replyMessage = null;
            return;
        }
        this.quote = replyQuote2;
        if (replyQuote2 != null) {
            this.quoteStart = replyQuote2.start;
            this.quoteEnd = replyQuote2.end;
        }
    }
}
