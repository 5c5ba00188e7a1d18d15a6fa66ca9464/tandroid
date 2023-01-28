package org.telegram.messenger;

import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseBooleanArray;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageEntitySpoiler;
import org.telegram.tgnet.TLRPC$TL_messageFwdHeader;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_pollAnswerVoters;
import org.telegram.tgnet.TLRPC$TL_pollResults;
/* loaded from: classes.dex */
public class ForwardingMessagesParams {
    public boolean hasCaption;
    public boolean hasSenders;
    public boolean hasSpoilers;
    public boolean hideCaption;
    public boolean hideForwardSendersName;
    public boolean isSecret;
    public ArrayList<MessageObject> messages;
    public boolean multiplyUsers;
    public boolean willSeeSenders;
    public LongSparseArray<MessageObject.GroupedMessages> groupedMessagesMap = new LongSparseArray<>();
    public ArrayList<MessageObject> previewMessages = new ArrayList<>();
    public SparseBooleanArray selectedIds = new SparseBooleanArray();
    public ArrayList<TLRPC$TL_pollAnswerVoters> pollChoosenAnswers = new ArrayList<>();

    /* JADX WARN: Removed duplicated region for block: B:45:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x014a  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01ed A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r8v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ForwardingMessagesParams(ArrayList<MessageObject> arrayList, long j) {
        long j2;
        long j3;
        ArrayList arrayList2;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        MessageObject messageObject;
        this.messages = arrayList;
        ?? r8 = 0;
        this.hasCaption = false;
        this.hasSenders = false;
        this.isSecret = DialogObject.isEncryptedDialog(j);
        this.hasSpoilers = false;
        ArrayList arrayList3 = new ArrayList();
        int i = 0;
        while (i < arrayList.size()) {
            MessageObject messageObject2 = arrayList.get(i);
            if (!TextUtils.isEmpty(messageObject2.caption)) {
                this.hasCaption = true;
            }
            this.selectedIds.put(messageObject2.getId(), true);
            TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
            TLRPC$Message tLRPC$Message = messageObject2.messageOwner;
            tLRPC$TL_message.id = tLRPC$Message.id;
            tLRPC$TL_message.grouped_id = tLRPC$Message.grouped_id;
            tLRPC$TL_message.peer_id = tLRPC$Message.peer_id;
            tLRPC$TL_message.from_id = tLRPC$Message.from_id;
            tLRPC$TL_message.message = tLRPC$Message.message;
            tLRPC$TL_message.media = tLRPC$Message.media;
            tLRPC$TL_message.action = tLRPC$Message.action;
            tLRPC$TL_message.edit_date = r8;
            ArrayList<TLRPC$MessageEntity> arrayList4 = tLRPC$Message.entities;
            if (arrayList4 != null) {
                tLRPC$TL_message.entities.addAll(arrayList4);
                if (!this.hasSpoilers) {
                    Iterator<TLRPC$MessageEntity> it = tLRPC$TL_message.entities.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (it.next() instanceof TLRPC$TL_messageEntitySpoiler) {
                                this.hasSpoilers = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
            tLRPC$TL_message.out = true;
            tLRPC$TL_message.unread = r8;
            TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
            tLRPC$TL_message.via_bot_id = tLRPC$Message2.via_bot_id;
            tLRPC$TL_message.reply_markup = tLRPC$Message2.reply_markup;
            tLRPC$TL_message.post = tLRPC$Message2.post;
            tLRPC$TL_message.legacy = tLRPC$Message2.legacy;
            tLRPC$TL_message.restriction_reason = tLRPC$Message2.restriction_reason;
            tLRPC$TL_message.replyMessage = tLRPC$Message2.replyMessage;
            long j4 = UserConfig.getInstance(messageObject2.currentAccount).clientUserId;
            if (this.isSecret) {
                arrayList2 = arrayList3;
            } else {
                TLRPC$Message tLRPC$Message3 = messageObject2.messageOwner;
                tLRPC$MessageFwdHeader = tLRPC$Message3.fwd_from;
                if (tLRPC$MessageFwdHeader != null) {
                    if (!messageObject2.isDice()) {
                        this.hasSenders = true;
                    } else {
                        this.willSeeSenders = true;
                    }
                    if (tLRPC$MessageFwdHeader.from_id == null && !arrayList3.contains(tLRPC$MessageFwdHeader.from_name)) {
                        arrayList3.add(tLRPC$MessageFwdHeader.from_name);
                    }
                    arrayList2 = arrayList3;
                } else {
                    long j5 = tLRPC$Message3.from_id.user_id;
                    arrayList2 = arrayList3;
                    if (j5 == 0 || tLRPC$Message3.dialog_id != j4 || j5 != j4) {
                        tLRPC$MessageFwdHeader = new TLRPC$TL_messageFwdHeader();
                        tLRPC$MessageFwdHeader.from_id = messageObject2.messageOwner.from_id;
                        if (!messageObject2.isDice()) {
                            this.hasSenders = true;
                        } else {
                            this.willSeeSenders = true;
                        }
                    }
                }
                if (tLRPC$MessageFwdHeader != null) {
                    tLRPC$TL_message.fwd_from = tLRPC$MessageFwdHeader;
                    tLRPC$TL_message.flags |= 4;
                }
                tLRPC$TL_message.dialog_id = j;
                messageObject = new MessageObject(messageObject2.currentAccount, tLRPC$TL_message, true, false) { // from class: org.telegram.messenger.ForwardingMessagesParams.1
                    @Override // org.telegram.messenger.MessageObject
                    public boolean needDrawForwarded() {
                        if (ForwardingMessagesParams.this.hideForwardSendersName) {
                            return false;
                        }
                        return super.needDrawForwarded();
                    }
                };
                messageObject.preview = true;
                if (messageObject.getGroupId() != 0) {
                    MessageObject.GroupedMessages groupedMessages = this.groupedMessagesMap.get(messageObject.getGroupId(), null);
                    if (groupedMessages == null) {
                        groupedMessages = new MessageObject.GroupedMessages();
                        this.groupedMessagesMap.put(messageObject.getGroupId(), groupedMessages);
                    }
                    groupedMessages.messages.add(messageObject);
                }
                this.previewMessages.add(0, messageObject);
                if (!messageObject2.isPoll()) {
                    TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) messageObject2.messageOwner.media;
                    PreviewMediaPoll previewMediaPoll = new PreviewMediaPoll();
                    previewMediaPoll.poll = tLRPC$TL_messageMediaPoll.poll;
                    previewMediaPoll.provider = tLRPC$TL_messageMediaPoll.provider;
                    TLRPC$TL_pollResults tLRPC$TL_pollResults = new TLRPC$TL_pollResults();
                    previewMediaPoll.results = tLRPC$TL_pollResults;
                    int i2 = tLRPC$TL_messageMediaPoll.results.total_voters;
                    tLRPC$TL_pollResults.total_voters = i2;
                    previewMediaPoll.totalVotersCached = i2;
                    messageObject.messageOwner.media = previewMediaPoll;
                    if (messageObject2.canUnvote()) {
                        int size = tLRPC$TL_messageMediaPoll.results.results.size();
                        for (int i3 = 0; i3 < size; i3++) {
                            TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters = tLRPC$TL_messageMediaPoll.results.results.get(i3);
                            if (tLRPC$TL_pollAnswerVoters.chosen) {
                                TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters2 = new TLRPC$TL_pollAnswerVoters();
                                tLRPC$TL_pollAnswerVoters2.chosen = tLRPC$TL_pollAnswerVoters.chosen;
                                tLRPC$TL_pollAnswerVoters2.correct = tLRPC$TL_pollAnswerVoters.correct;
                                tLRPC$TL_pollAnswerVoters2.flags = tLRPC$TL_pollAnswerVoters.flags;
                                tLRPC$TL_pollAnswerVoters2.option = tLRPC$TL_pollAnswerVoters.option;
                                tLRPC$TL_pollAnswerVoters2.voters = tLRPC$TL_pollAnswerVoters.voters;
                                this.pollChoosenAnswers.add(tLRPC$TL_pollAnswerVoters2);
                                previewMediaPoll.results.results.add(tLRPC$TL_pollAnswerVoters2);
                            } else {
                                previewMediaPoll.results.results.add(tLRPC$TL_pollAnswerVoters);
                            }
                        }
                    }
                }
                i++;
                arrayList3 = arrayList2;
                r8 = 0;
            }
            tLRPC$MessageFwdHeader = null;
            if (tLRPC$MessageFwdHeader != null) {
            }
            tLRPC$TL_message.dialog_id = j;
            messageObject = new MessageObject(messageObject2.currentAccount, tLRPC$TL_message, true, false) { // from class: org.telegram.messenger.ForwardingMessagesParams.1
                @Override // org.telegram.messenger.MessageObject
                public boolean needDrawForwarded() {
                    if (ForwardingMessagesParams.this.hideForwardSendersName) {
                        return false;
                    }
                    return super.needDrawForwarded();
                }
            };
            messageObject.preview = true;
            if (messageObject.getGroupId() != 0) {
            }
            this.previewMessages.add(0, messageObject);
            if (!messageObject2.isPoll()) {
            }
            i++;
            arrayList3 = arrayList2;
            r8 = 0;
        }
        ArrayList arrayList5 = arrayList3;
        ArrayList arrayList6 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            MessageObject messageObject3 = arrayList.get(i4);
            if (messageObject3.isFromUser()) {
                j3 = messageObject3.messageOwner.from_id.user_id;
            } else {
                TLRPC$Chat chat = MessagesController.getInstance(messageObject3.currentAccount).getChat(Long.valueOf(messageObject3.messageOwner.peer_id.channel_id));
                if (ChatObject.isChannel(chat) && chat.megagroup && messageObject3.isForwardedChannelPost()) {
                    j2 = messageObject3.messageOwner.fwd_from.from_id.channel_id;
                } else {
                    j2 = messageObject3.messageOwner.peer_id.channel_id;
                }
                j3 = -j2;
            }
            if (!arrayList6.contains(Long.valueOf(j3))) {
                arrayList6.add(Long.valueOf(j3));
            }
        }
        if (arrayList6.size() + arrayList5.size() > 1) {
            this.multiplyUsers = true;
        }
        for (int i5 = 0; i5 < this.groupedMessagesMap.size(); i5++) {
            this.groupedMessagesMap.valueAt(i5).calculate();
        }
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

    /* loaded from: classes.dex */
    public class PreviewMediaPoll extends TLRPC$TL_messageMediaPoll {
        public int totalVotersCached;

        public PreviewMediaPoll() {
        }
    }
}
