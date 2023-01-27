package org.telegram.messenger;

import android.text.TextUtils;
import android.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.telegram.messenger.LanguageDetector;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_togglePeerTranslations;
import org.telegram.tgnet.TLRPC$TL_messages_translateResult;
import org.telegram.tgnet.TLRPC$TL_messages_translateText;
import org.telegram.tgnet.TLRPC$TL_textWithEntities;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.ui.RestrictedLanguagesSelectActivity;
/* loaded from: classes.dex */
public class TranslateController extends BaseController {
    public static final String FAILED_TRANSLATION = "{f4il}";
    private static final int GROUPING_TRANSLATIONS_TIMEOUT = 200;
    private static final int MAX_MESSAGES_PER_REQUEST = 20;
    private static final int MAX_SYMBOLS_PER_REQUEST = 25000;
    private static final float REQUIRED_PERCENTAGE_MESSAGES_TRANSLATABLE = 0.6f;
    private static final int REQUIRED_TOTAL_MESSAGES_CHECKED = 8;
    public static final String UNKNOWN_LANGUAGE = "und";
    private final HashMap<Long, String> detectedDialogLanguage;
    private final Set<Long> forceNotTranslatableDialogs;
    private final HashMap<Long, HashMap<Integer, MessageObject>> keptReplyMessageObjects;
    private final Set<Integer> loadingTranslations;
    private ArrayList<Integer> pendingLanguageChecks;
    private final HashMap<Long, ArrayList<PendingTranslation>> pendingTranslations;
    private final HashMap<Long, Pair<Set<Integer>, Set<Integer>>> translatableDialogMessages;
    private final Set<Long> translatableDialogs;
    private final HashMap<Long, String> translateDialogLanguage;
    private final Set<Long> translatingDialogs;

    public TranslateController(int i) {
        super(i);
        this.translatingDialogs = new HashSet();
        this.translatableDialogs = new HashSet();
        this.translatableDialogMessages = new HashMap<>();
        this.translateDialogLanguage = new HashMap<>();
        this.detectedDialogLanguage = new HashMap<>();
        this.keptReplyMessageObjects = new HashMap<>();
        this.forceNotTranslatableDialogs = new HashSet();
        this.pendingLanguageChecks = new ArrayList<>();
        this.loadingTranslations = new HashSet();
        this.pendingTranslations = new HashMap<>();
    }

    public boolean isFeatureAvailable() {
        return UserConfig.getInstance(this.currentAccount).isPremium() && isChatTranslateEnabled();
    }

    public boolean isChatTranslateEnabled() {
        return MessagesController.getMainSettings(this.currentAccount).getBoolean("translate_chat_button", true);
    }

    public boolean isContextTranslateEnabled() {
        return MessagesController.getMainSettings(this.currentAccount).getBoolean("translate_button", MessagesController.getGlobalMainSettings().getBoolean("translate_button", false));
    }

    public void setContextTranslateEnabled(boolean z) {
        MessagesController.getMainSettings(this.currentAccount).edit().putBoolean("translate_button", z).apply();
    }

    public static boolean isTranslatable(MessageObject messageObject) {
        int i;
        return (messageObject == null || messageObject.messageOwner == null || messageObject.isOutOwner() || ((i = messageObject.type) != 0 && i != 3 && i != 1 && i != 2 && i != 9 && i != 14) || TextUtils.isEmpty(messageObject.messageOwner.message)) ? false : true;
    }

    public boolean isDialogTranslatable(long j) {
        return isFeatureAvailable() && !DialogObject.isEncryptedDialog(j) && getUserConfig().getClientUserId() != j && this.translatableDialogs.contains(Long.valueOf(j));
    }

    public boolean isDialogForceNotTranslatable(long j) {
        if (this.forceNotTranslatableDialogs.contains(Long.valueOf(j))) {
            return true;
        }
        TLRPC$ChatFull chatFull = getMessagesController().getChatFull(-j);
        if (chatFull != null) {
            return chatFull.translations_disabled;
        }
        TLRPC$UserFull userFull = getMessagesController().getUserFull(j);
        if (userFull != null) {
            return userFull.translations_disabled;
        }
        return false;
    }

    public boolean isTranslatingDialog(long j) {
        return isFeatureAvailable() && this.translatingDialogs.contains(Long.valueOf(j));
    }

    public void toggleTranslatingDialog(long j) {
        toggleTranslatingDialog(j, !isTranslatingDialog(j));
    }

    public void toggleTranslatingDialog(long j, boolean z) {
        boolean isTranslatingDialog = isTranslatingDialog(j);
        if (z && !isTranslatingDialog) {
            this.translatingDialogs.add(Long.valueOf(j));
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogTranslate, Long.valueOf(j), Boolean.TRUE);
        } else if (z || !isTranslatingDialog) {
        } else {
            this.translatingDialogs.remove(Long.valueOf(j));
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogTranslate, Long.valueOf(j), Boolean.FALSE);
            cancelTranslations(j);
        }
    }

    private int hash(MessageObject messageObject) {
        if (messageObject == null) {
            return 0;
        }
        return Objects.hash(Long.valueOf(messageObject.getDialogId()), Integer.valueOf(messageObject.getId()));
    }

    private String currentLanguage() {
        String str = LocaleController.getInstance().getCurrentLocaleInfo().pluralLangCode;
        return str != null ? str.split("_")[0] : str;
    }

    public String getDialogTranslateTo(long j) {
        String str = this.translateDialogLanguage.get(Long.valueOf(j));
        return str == null ? currentLanguage() : str;
    }

    public void setDialogTranslateTo(final long j, final String str) {
        if (TextUtils.equals(getDialogTranslateTo(j), str)) {
            return;
        }
        if (isTranslatingDialog(j)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    TranslateController.this.lambda$setDialogTranslateTo$0(j, str);
                }
            }, 150L);
        } else {
            synchronized (this) {
                this.translateDialogLanguage.put(Long.valueOf(j), str);
            }
        }
        cancelTranslations(j);
        synchronized (this) {
            this.translatingDialogs.remove(Long.valueOf(j));
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogTranslate, Long.valueOf(j), Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDialogTranslateTo$0(long j, String str) {
        synchronized (this) {
            this.translateDialogLanguage.put(Long.valueOf(j), str);
            this.translatingDialogs.add(Long.valueOf(j));
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogTranslate, Long.valueOf(j), Boolean.TRUE);
    }

    public void setForceDoNotTranslateDialog(long j, boolean z) {
        setForceDoNotTranslateDialog(j, z, false);
    }

    public void setForceDoNotTranslateDialog(long j, boolean z, boolean z2) {
        boolean z3;
        TLRPC$TL_messages_togglePeerTranslations tLRPC$TL_messages_togglePeerTranslations = new TLRPC$TL_messages_togglePeerTranslations();
        tLRPC$TL_messages_togglePeerTranslations.peer = getMessagesController().getInputPeer(j);
        tLRPC$TL_messages_togglePeerTranslations.disabled = z;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_togglePeerTranslations, null);
        TLRPC$ChatFull chatFull = getMessagesController().getChatFull(-j);
        if (chatFull != null) {
            chatFull.translations_disabled = z;
            getMessagesStorage().updateChatInfo(chatFull, true);
        }
        TLRPC$UserFull userFull = getMessagesController().getUserFull(j);
        if (userFull != null) {
            userFull.translations_disabled = z;
            getMessagesStorage().updateUserInfo(userFull, true);
        }
        synchronized (this) {
            this.translatableDialogMessages.remove(Long.valueOf(j));
            if (z) {
                this.forceNotTranslatableDialogs.add(Long.valueOf(j));
                cancelTranslations(j);
                if (isDialogTranslatable(j)) {
                    this.translatingDialogs.remove(Long.valueOf(j));
                } else {
                    z3 = false;
                }
            } else {
                this.forceNotTranslatableDialogs.remove(Long.valueOf(j));
            }
            z3 = true;
        }
        if (!z3 || z2) {
            return;
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogTranslate, Long.valueOf(j), Boolean.FALSE);
    }

    public void checkRestrictedLanguagesUpdate() {
        synchronized (this) {
            this.translatableDialogMessages.clear();
            HashSet<String> restrictedLanguages = RestrictedLanguagesSelectActivity.getRestrictedLanguages();
            for (Long l : this.translatableDialogs) {
                long longValue = l.longValue();
                String str = this.detectedDialogLanguage.get(Long.valueOf(longValue));
                if (str != null && restrictedLanguages.contains(str)) {
                    cancelTranslations(longValue);
                    this.translatingDialogs.remove(Long.valueOf(longValue));
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogTranslate, Long.valueOf(longValue), Boolean.FALSE);
                }
            }
            this.translatableDialogs.clear();
        }
    }

    public String getDialogDetectedLanguage(long j) {
        return this.detectedDialogLanguage.get(Long.valueOf(j));
    }

    public void checkTranslation(MessageObject messageObject, boolean z) {
        checkTranslation(messageObject, z, false);
    }

    private void checkTranslation(MessageObject messageObject, boolean z, final boolean z2) {
        MessageObject findReplyMessageObject;
        MessageObject messageObject2;
        if (!isFeatureAvailable() || messageObject == null || messageObject.messageOwner == null) {
            return;
        }
        final long dialogId = messageObject.getDialogId();
        if (!z2 && (messageObject2 = messageObject.replyMessageObject) != null) {
            checkTranslation(messageObject2, z, true);
        }
        if (isTranslatable(messageObject)) {
            if (!isTranslatingDialog(dialogId)) {
                checkLanguage(messageObject);
                return;
            }
            String dialogTranslateTo = getDialogTranslateTo(dialogId);
            if (!z2) {
                TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                if ((tLRPC$Message.translatedText == null || !dialogTranslateTo.equals(tLRPC$Message.translatedToLanguage)) && (findReplyMessageObject = findReplyMessageObject(dialogId, messageObject.getId())) != null) {
                    TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                    TLRPC$Message tLRPC$Message3 = findReplyMessageObject.messageOwner;
                    tLRPC$Message2.translatedToLanguage = tLRPC$Message3.translatedToLanguage;
                    tLRPC$Message2.translatedText = tLRPC$Message3.translatedText;
                    messageObject = findReplyMessageObject;
                }
            }
            if (z && isTranslatingDialog(dialogId)) {
                TLRPC$Message tLRPC$Message4 = messageObject.messageOwner;
                if (tLRPC$Message4.translatedText == null || !dialogTranslateTo.equals(tLRPC$Message4.translatedToLanguage)) {
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageTranslating, messageObject);
                    final MessageObject messageObject3 = messageObject;
                    pushToTranslate(messageObject, dialogTranslateTo, new Utilities.Callback2() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda8
                        @Override // org.telegram.messenger.Utilities.Callback2
                        public final void run(Object obj, Object obj2) {
                            TranslateController.this.lambda$checkTranslation$1(messageObject3, z2, dialogId, (TLRPC$TL_textWithEntities) obj, (String) obj2);
                        }
                    });
                } else if (z2) {
                    keepReplyMessage(messageObject);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkTranslation$1(MessageObject messageObject, boolean z, long j, TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities, String str) {
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        tLRPC$Message.translatedToLanguage = str;
        tLRPC$Message.translatedText = tLRPC$TL_textWithEntities;
        if (z) {
            keepReplyMessage(messageObject);
        }
        getMessagesStorage().updateMessageCustomParams(j, messageObject.messageOwner);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageTranslated, messageObject);
    }

    public void cleanup() {
        cancelAllTranslations();
        this.translatingDialogs.clear();
        this.translatableDialogs.clear();
        this.translatableDialogMessages.clear();
        this.translateDialogLanguage.clear();
        this.detectedDialogLanguage.clear();
        this.keptReplyMessageObjects.clear();
        this.forceNotTranslatableDialogs.clear();
        this.loadingTranslations.clear();
    }

    private void checkLanguage(final MessageObject messageObject) {
        TLRPC$Message tLRPC$Message;
        if (LanguageDetector.hasSupport() && isTranslatable(messageObject) && (tLRPC$Message = messageObject.messageOwner) != null && !TextUtils.isEmpty(tLRPC$Message.message)) {
            if (messageObject.messageOwner.originalLanguage != null) {
                checkDialogTranslatable(messageObject);
                return;
            }
            final long dialogId = messageObject.getDialogId();
            final int hash = hash(messageObject);
            if (isDialogTranslatable(dialogId) || isDialogForceNotTranslatable(dialogId) || this.pendingLanguageChecks.contains(Integer.valueOf(hash))) {
                return;
            }
            this.pendingLanguageChecks.add(Integer.valueOf(hash));
            LanguageDetector.detectLanguage(messageObject.messageOwner.message, new LanguageDetector.StringCallback() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda7
                @Override // org.telegram.messenger.LanguageDetector.StringCallback
                public final void run(String str) {
                    TranslateController.this.lambda$checkLanguage$3(messageObject, dialogId, hash, str);
                }
            }, new LanguageDetector.ExceptionCallback() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.LanguageDetector.ExceptionCallback
                public final void run(Exception exc) {
                    TranslateController.this.lambda$checkLanguage$5(messageObject, dialogId, hash, exc);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkLanguage$3(final MessageObject messageObject, final long j, final int i, final String str) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                TranslateController.this.lambda$checkLanguage$2(str, messageObject, j, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkLanguage$2(String str, MessageObject messageObject, long j, int i) {
        if (str == null) {
            str = UNKNOWN_LANGUAGE;
        }
        messageObject.messageOwner.originalLanguage = str;
        getMessagesStorage().updateMessageCustomParams(j, messageObject.messageOwner);
        this.pendingLanguageChecks.remove(Integer.valueOf(i));
        checkDialogTranslatable(messageObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkLanguage$5(final MessageObject messageObject, final long j, final int i, Exception exc) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                TranslateController.this.lambda$checkLanguage$4(messageObject, j, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkLanguage$4(MessageObject messageObject, long j, int i) {
        messageObject.messageOwner.originalLanguage = UNKNOWN_LANGUAGE;
        getMessagesStorage().updateMessageCustomParams(j, messageObject.messageOwner);
        this.pendingLanguageChecks.remove(Integer.valueOf(i));
    }

    private void checkDialogTranslatable(MessageObject messageObject) {
        String str;
        if (messageObject == null || messageObject.messageOwner == null) {
            return;
        }
        final long dialogId = messageObject.getDialogId();
        String str2 = messageObject.messageOwner.originalLanguage;
        if (str2 != null && !UNKNOWN_LANGUAGE.equals(str2)) {
            this.detectedDialogLanguage.put(Long.valueOf(dialogId), messageObject.messageOwner.originalLanguage);
        }
        Pair<Set<Integer>, Set<Integer>> pair = this.translatableDialogMessages.get(Long.valueOf(dialogId));
        if (pair == null) {
            HashMap<Long, Pair<Set<Integer>, Set<Integer>>> hashMap = this.translatableDialogMessages;
            Long valueOf = Long.valueOf(dialogId);
            Pair<Set<Integer>, Set<Integer>> pair2 = new Pair<>(new HashSet(), new HashSet());
            hashMap.put(valueOf, pair2);
            pair = pair2;
        }
        ((Set) (isTranslatable(messageObject) && (str = messageObject.messageOwner.originalLanguage) != null && !UNKNOWN_LANGUAGE.equals(str) && !RestrictedLanguagesSelectActivity.getRestrictedLanguages().contains(messageObject.messageOwner.originalLanguage) && !TextUtils.equals(getDialogTranslateTo(dialogId), messageObject.messageOwner.originalLanguage) ? pair.first : pair.second)).add(Integer.valueOf(messageObject.getId()));
        int size = ((Set) pair.first).size();
        int size2 = ((Set) pair.second).size() + size;
        if (size2 < 8 || size / size2 < REQUIRED_PERCENTAGE_MESSAGES_TRANSLATABLE) {
            return;
        }
        this.translatableDialogs.add(Long.valueOf(dialogId));
        this.translatableDialogMessages.remove(Long.valueOf(dialogId));
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TranslateController.this.lambda$checkDialogTranslatable$6(dialogId);
            }
        }, 450L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDialogTranslatable$6(long j) {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogIsTranslatable, Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PendingTranslation {
        ArrayList<Utilities.Callback2<TLRPC$TL_textWithEntities, String>> callbacks;
        String language;
        ArrayList<Integer> messageIds;
        int reqId;
        Runnable runnable;
        int symbolsCount;

        private PendingTranslation() {
            this.messageIds = new ArrayList<>();
            this.callbacks = new ArrayList<>();
            this.reqId = -1;
        }
    }

    private void pushToTranslate(MessageObject messageObject, String str, Utilities.Callback2<TLRPC$TL_textWithEntities, String> callback2) {
        final PendingTranslation pendingTranslation;
        String str2;
        if (messageObject == null || callback2 == null) {
            return;
        }
        final long dialogId = messageObject.getDialogId();
        synchronized (this) {
            ArrayList<PendingTranslation> arrayList = this.pendingTranslations.get(Long.valueOf(dialogId));
            if (arrayList == null) {
                HashMap<Long, ArrayList<PendingTranslation>> hashMap = this.pendingTranslations;
                Long valueOf = Long.valueOf(dialogId);
                ArrayList<PendingTranslation> arrayList2 = new ArrayList<>();
                hashMap.put(valueOf, arrayList2);
                arrayList = arrayList2;
            }
            if (arrayList.isEmpty()) {
                pendingTranslation = new PendingTranslation();
                arrayList.add(pendingTranslation);
            } else {
                pendingTranslation = arrayList.get(arrayList.size() - 1);
            }
            if (pendingTranslation.messageIds.contains(Integer.valueOf(messageObject.getId()))) {
                return;
            }
            int i = 0;
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if (tLRPC$Message != null && (str2 = tLRPC$Message.message) != null) {
                i = str2.length();
            } else {
                CharSequence charSequence = messageObject.caption;
                if (charSequence != null) {
                    i = charSequence.length();
                } else {
                    CharSequence charSequence2 = messageObject.messageText;
                    if (charSequence2 != null) {
                        i = charSequence2.length();
                    }
                }
            }
            if (pendingTranslation.symbolsCount + i >= MAX_SYMBOLS_PER_REQUEST || pendingTranslation.messageIds.size() + 1 >= 20) {
                pendingTranslation = new PendingTranslation();
                arrayList.add(pendingTranslation);
            }
            Runnable runnable = pendingTranslation.runnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            this.loadingTranslations.add(Integer.valueOf(messageObject.getId()));
            pendingTranslation.messageIds.add(Integer.valueOf(messageObject.getId()));
            pendingTranslation.callbacks.add(callback2);
            pendingTranslation.language = str;
            pendingTranslation.symbolsCount += i;
            Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    TranslateController.this.lambda$pushToTranslate$9(dialogId, pendingTranslation);
                }
            };
            pendingTranslation.runnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, 200L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pushToTranslate$9(long j, final PendingTranslation pendingTranslation) {
        synchronized (this) {
            ArrayList<PendingTranslation> arrayList = this.pendingTranslations.get(Long.valueOf(j));
            if (arrayList != null) {
                arrayList.remove(pendingTranslation);
                if (arrayList.isEmpty()) {
                    this.pendingTranslations.remove(Long.valueOf(j));
                }
            }
        }
        TLRPC$TL_messages_translateText tLRPC$TL_messages_translateText = new TLRPC$TL_messages_translateText();
        tLRPC$TL_messages_translateText.flags |= 1;
        tLRPC$TL_messages_translateText.peer = getMessagesController().getInputPeer(j);
        tLRPC$TL_messages_translateText.id = pendingTranslation.messageIds;
        tLRPC$TL_messages_translateText.to_lang = pendingTranslation.language;
        int sendRequest = getConnectionsManager().sendRequest(tLRPC$TL_messages_translateText, new RequestDelegate() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda9
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TranslateController.this.lambda$pushToTranslate$8(pendingTranslation, tLObject, tLRPC$TL_error);
            }
        });
        synchronized (this) {
            pendingTranslation.reqId = sendRequest;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pushToTranslate$8(final PendingTranslation pendingTranslation, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.TranslateController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                TranslateController.this.lambda$pushToTranslate$7(pendingTranslation, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pushToTranslate$7(PendingTranslation pendingTranslation, TLObject tLObject) {
        ArrayList<Integer> arrayList;
        ArrayList<Utilities.Callback2<TLRPC$TL_textWithEntities, String>> arrayList2;
        synchronized (this) {
            arrayList = pendingTranslation.messageIds;
            arrayList2 = pendingTranslation.callbacks;
        }
        if (tLObject instanceof TLRPC$TL_messages_translateResult) {
            ArrayList<TLRPC$TL_textWithEntities> arrayList3 = ((TLRPC$TL_messages_translateResult) tLObject).result;
            int min = Math.min(arrayList2.size(), arrayList3.size());
            for (int i = 0; i < min; i++) {
                arrayList2.get(i).run(arrayList3.get(i), pendingTranslation.language);
            }
        } else {
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                arrayList2.get(i2).run(null, pendingTranslation.language);
            }
        }
        synchronized (this) {
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                this.loadingTranslations.remove(arrayList.get(i3));
            }
        }
    }

    public boolean isTranslating(MessageObject messageObject) {
        boolean z;
        synchronized (this) {
            if (messageObject != null) {
                try {
                    z = (isTranslatingDialog(messageObject.getDialogId()) && this.loadingTranslations.contains(Integer.valueOf(messageObject.getId()))) ? true : true;
                } finally {
                }
            }
            z = false;
        }
        return z;
    }

    public boolean isTranslating(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages) {
        if (messageObject != null && isTranslatingDialog(messageObject.getDialogId())) {
            synchronized (this) {
                if (this.loadingTranslations.contains(Integer.valueOf(messageObject.getId()))) {
                    return true;
                }
                if (groupedMessages != null) {
                    Iterator<MessageObject> it = groupedMessages.messages.iterator();
                    while (it.hasNext()) {
                        if (this.loadingTranslations.contains(Integer.valueOf(it.next().getId()))) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    public void cancelAllTranslations() {
        synchronized (this) {
            for (ArrayList<PendingTranslation> arrayList : this.pendingTranslations.values()) {
                if (arrayList != null) {
                    Iterator<PendingTranslation> it = arrayList.iterator();
                    while (it.hasNext()) {
                        PendingTranslation next = it.next();
                        AndroidUtilities.cancelRunOnUIThread(next.runnable);
                        if (next.reqId != -1) {
                            getConnectionsManager().cancelRequest(next.reqId, true);
                            Iterator<Integer> it2 = next.messageIds.iterator();
                            while (it2.hasNext()) {
                                this.loadingTranslations.remove(it2.next());
                            }
                        }
                    }
                }
            }
        }
    }

    public void cancelTranslations(long j) {
        synchronized (this) {
            ArrayList<PendingTranslation> arrayList = this.pendingTranslations.get(Long.valueOf(j));
            if (arrayList != null) {
                Iterator<PendingTranslation> it = arrayList.iterator();
                while (it.hasNext()) {
                    PendingTranslation next = it.next();
                    AndroidUtilities.cancelRunOnUIThread(next.runnable);
                    if (next.reqId != -1) {
                        getConnectionsManager().cancelRequest(next.reqId, true);
                        Iterator<Integer> it2 = next.messageIds.iterator();
                        while (it2.hasNext()) {
                            this.loadingTranslations.remove(it2.next());
                        }
                    }
                }
                this.pendingTranslations.remove(Long.valueOf(j));
            }
        }
    }

    private void keepReplyMessage(MessageObject messageObject) {
        if (messageObject == null) {
            return;
        }
        HashMap<Integer, MessageObject> hashMap = this.keptReplyMessageObjects.get(Long.valueOf(messageObject.getDialogId()));
        if (hashMap == null) {
            HashMap<Long, HashMap<Integer, MessageObject>> hashMap2 = this.keptReplyMessageObjects;
            Long valueOf = Long.valueOf(messageObject.getDialogId());
            HashMap<Integer, MessageObject> hashMap3 = new HashMap<>();
            hashMap2.put(valueOf, hashMap3);
            hashMap = hashMap3;
        }
        hashMap.put(Integer.valueOf(messageObject.getId()), messageObject);
    }

    public MessageObject findReplyMessageObject(long j, int i) {
        HashMap<Integer, MessageObject> hashMap = this.keptReplyMessageObjects.get(Long.valueOf(j));
        if (hashMap == null) {
            return null;
        }
        return hashMap.get(Integer.valueOf(i));
    }

    private void clearAllKeptReplyMessages(long j) {
        this.keptReplyMessageObjects.remove(Long.valueOf(j));
    }
}
