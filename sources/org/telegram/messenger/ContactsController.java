package org.telegram.messenger;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import j$.util.concurrent.ConcurrentHashMap;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.ContactsController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.Bulletin;

/* loaded from: classes.dex */
public class ContactsController extends BaseController {
    private static volatile ContactsController[] Instance = new ContactsController[4];
    public static final int PRIVACY_RULES_TYPE_ADDED_BY_PHONE = 7;
    public static final int PRIVACY_RULES_TYPE_BIO = 9;
    public static final int PRIVACY_RULES_TYPE_BIRTHDAY = 11;
    public static final int PRIVACY_RULES_TYPE_CALLS = 2;
    public static final int PRIVACY_RULES_TYPE_COUNT = 13;
    public static final int PRIVACY_RULES_TYPE_FORWARDS = 5;
    public static final int PRIVACY_RULES_TYPE_GIFTS = 12;
    public static final int PRIVACY_RULES_TYPE_INVITE = 1;
    public static final int PRIVACY_RULES_TYPE_LASTSEEN = 0;
    public static final int PRIVACY_RULES_TYPE_MESSAGES = 10;
    public static final int PRIVACY_RULES_TYPE_P2P = 3;
    public static final int PRIVACY_RULES_TYPE_PHONE = 6;
    public static final int PRIVACY_RULES_TYPE_PHOTO = 4;
    public static final int PRIVACY_RULES_TYPE_VOICE_MESSAGES = 8;
    private static Collator cachedCollator;
    private static Locale cachedCollatorLocale;
    private ArrayList<TLRPC.PrivacyRule> addedByPhonePrivacyRules;
    private ArrayList<TLRPC.PrivacyRule> bioPrivacyRules;
    private ArrayList<TLRPC.PrivacyRule> birthdayPrivacyRules;
    private ArrayList<TLRPC.PrivacyRule> callPrivacyRules;
    private int completedRequestsCount;
    public ArrayList<TLRPC.TL_contact> contacts;
    public HashMap<String, Contact> contactsBook;
    private boolean contactsBookLoaded;
    public HashMap<String, Contact> contactsBookSPhones;
    public HashMap<String, TLRPC.TL_contact> contactsByPhone;
    public HashMap<String, TLRPC.TL_contact> contactsByShortPhone;
    public ConcurrentHashMap<Long, TLRPC.TL_contact> contactsDict;
    public boolean contactsLoaded;
    private boolean contactsSyncInProgress;
    private ArrayList<Long> delayedContactsUpdate;
    private int deleteAccountTTL;
    public boolean doneLoadingContacts;
    private ArrayList<TLRPC.PrivacyRule> forwardsPrivacyRules;
    private ArrayList<TLRPC.PrivacyRule> giftsPrivacyRules;
    private TLRPC.TL_globalPrivacySettings globalPrivacySettings;
    private ArrayList<TLRPC.PrivacyRule> groupPrivacyRules;
    private boolean ignoreChanges;
    private String inviteLink;
    private String lastContactsVersions;
    private ArrayList<TLRPC.PrivacyRule> lastseenPrivacyRules;
    private final Object loadContactsSync;
    private boolean loadingContacts;
    private int loadingDeleteInfo;
    private int loadingGlobalSettings;
    private int[] loadingPrivacyInfo;
    private boolean migratingContacts;
    private final Object observerLock;
    private ArrayList<TLRPC.PrivacyRule> p2pPrivacyRules;
    public HashMap<String, Contact> phoneBookByShortPhones;
    public ArrayList<Contact> phoneBookContacts;
    public ArrayList<String> phoneBookSectionsArray;
    public HashMap<String, ArrayList<Object>> phoneBookSectionsDict;
    private ArrayList<TLRPC.PrivacyRule> phonePrivacyRules;
    private ArrayList<TLRPC.PrivacyRule> profilePhotoPrivacyRules;
    private String[] projectionNames;
    private String[] projectionPhones;
    private HashMap<String, String> sectionsToReplace;
    public ArrayList<String> sortedUsersMutualSectionsArray;
    public ArrayList<String> sortedUsersSectionsArray;
    private Account systemAccount;
    private boolean updatingInviteLink;
    public HashMap<String, ArrayList<TLRPC.TL_contact>> usersMutualSectionsDict;
    public HashMap<String, ArrayList<TLRPC.TL_contact>> usersSectionsDict;
    private ArrayList<TLRPC.PrivacyRule> voiceMessagesRules;

    /* loaded from: classes3.dex */
    public static class Contact {
        public int contact_id;
        public String first_name;
        public int imported;
        public boolean isGoodProvider;
        public String key;
        public String last_name;
        public boolean namesFilled;
        public String provider;
        public TLRPC.User user;
        public ArrayList<String> phones = new ArrayList<>(4);
        public ArrayList<String> phoneTypes = new ArrayList<>(4);
        public ArrayList<String> shortPhones = new ArrayList<>(4);
        public ArrayList<Integer> phoneDeleted = new ArrayList<>(4);

        public static String getLetter(String str, String str2) {
            return !TextUtils.isEmpty(str) ? str.substring(0, 1) : !TextUtils.isEmpty(str2) ? str2.substring(0, 1) : "#";
        }

        public String getLetter() {
            return getLetter(this.first_name, this.last_name);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    class MyContentObserver extends ContentObserver {
        private Runnable checkRunnable;

        public MyContentObserver() {
            super(null);
            this.checkRunnable = new Runnable() { // from class: org.telegram.messenger.ContactsController$MyContentObserver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.MyContentObserver.lambda$new$0();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$new$0() {
            for (int i = 0; i < 4; i++) {
                if (UserConfig.getInstance(i).isClientActivated()) {
                    ConnectionsManager.getInstance(i).resumeNetworkMaybe();
                    ContactsController.getInstance(i).checkContacts();
                }
            }
        }

        @Override // android.database.ContentObserver
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            synchronized (ContactsController.this.observerLock) {
                try {
                    if (ContactsController.this.ignoreChanges) {
                        return;
                    }
                    Utilities.globalQueue.cancelRunnable(this.checkRunnable);
                    Utilities.globalQueue.postRunnable(this.checkRunnable, 500L);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    private class PhoneBookContact {
        String id;
        String lookup_key;
        String name;
        String phone;

        private PhoneBookContact() {
        }
    }

    public ContactsController(int i) {
        super(i);
        this.loadContactsSync = new Object();
        this.observerLock = new Object();
        this.lastContactsVersions = "";
        this.delayedContactsUpdate = new ArrayList<>();
        this.sectionsToReplace = new HashMap<>();
        this.loadingPrivacyInfo = new int[13];
        this.projectionPhones = new String[]{"lookup", "data1", "data2", "data3", "display_name", "account_type"};
        this.projectionNames = new String[]{"lookup", "data2", "data3", "data5"};
        this.contactsBook = new HashMap<>();
        this.contactsBookSPhones = new HashMap<>();
        this.phoneBookContacts = new ArrayList<>();
        this.phoneBookSectionsDict = new HashMap<>();
        this.phoneBookSectionsArray = new ArrayList<>();
        this.phoneBookByShortPhones = new HashMap<>();
        this.contacts = new ArrayList<>();
        this.contactsDict = new ConcurrentHashMap<>(20, 1.0f, 2);
        this.usersSectionsDict = new HashMap<>();
        this.sortedUsersSectionsArray = new ArrayList<>();
        this.usersMutualSectionsDict = new HashMap<>();
        this.sortedUsersMutualSectionsArray = new ArrayList<>();
        this.contactsByPhone = new HashMap<>();
        this.contactsByShortPhone = new HashMap<>();
        if (MessagesController.getMainSettings(this.currentAccount).getBoolean("needGetStatuses", false)) {
            reloadContactsStatuses();
        }
        this.sectionsToReplace.put("À", "A");
        this.sectionsToReplace.put("Á", "A");
        this.sectionsToReplace.put("Ä", "A");
        this.sectionsToReplace.put("Ù", "U");
        this.sectionsToReplace.put("Ú", "U");
        this.sectionsToReplace.put("Ü", "U");
        this.sectionsToReplace.put("Ì", "I");
        this.sectionsToReplace.put("Í", "I");
        this.sectionsToReplace.put("Ï", "I");
        this.sectionsToReplace.put("È", "E");
        this.sectionsToReplace.put("É", "E");
        this.sectionsToReplace.put("Ê", "E");
        this.sectionsToReplace.put("Ë", "E");
        this.sectionsToReplace.put("Ò", "O");
        this.sectionsToReplace.put("Ó", "O");
        this.sectionsToReplace.put("Ö", "O");
        this.sectionsToReplace.put("Ç", "C");
        this.sectionsToReplace.put("Ñ", "N");
        this.sectionsToReplace.put("Ÿ", "Y");
        this.sectionsToReplace.put("Ý", "Y");
        this.sectionsToReplace.put("Ţ", "Y");
        if (i == 0) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda56
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$new$0();
                }
            });
        }
    }

    private void applyContactToPhoneBook(ArrayList<ContentProviderOperation> arrayList, TLRPC.User user) {
        String str;
        if (user == null) {
            return;
        }
        int size = arrayList.size();
        ContentProviderOperation.Builder newInsert = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
        newInsert.withValue("account_name", this.systemAccount.name);
        newInsert.withValue("account_type", this.systemAccount.type);
        newInsert.withValue("sync1", TextUtils.isEmpty(user.phone) ? "" : user.phone);
        newInsert.withValue("sync2", Long.valueOf(user.id));
        arrayList.add(newInsert.build());
        Uri uri = ContactsContract.Data.CONTENT_URI;
        ContentProviderOperation.Builder newInsert2 = ContentProviderOperation.newInsert(uri);
        newInsert2.withValueBackReference("raw_contact_id", size);
        newInsert2.withValue("mimetype", "vnd.android.cursor.item/name");
        newInsert2.withValue("data2", user.first_name);
        newInsert2.withValue("data3", user.last_name);
        arrayList.add(newInsert2.build());
        if (TextUtils.isEmpty(user.phone)) {
            str = formatName(user.first_name, user.last_name);
        } else {
            str = "+" + user.phone;
        }
        ContentProviderOperation.Builder newInsert3 = ContentProviderOperation.newInsert(uri);
        newInsert3.withValueBackReference("raw_contact_id", size);
        newInsert3.withValue("mimetype", "vnd.android.cursor.item/vnd.org.telegram.messenger.android.profile");
        newInsert3.withValue("data1", Long.valueOf(user.id));
        newInsert3.withValue("data2", "Telegram Profile");
        newInsert3.withValue("data3", LocaleController.formatString("ContactShortcutMessage", R.string.ContactShortcutMessage, str));
        newInsert3.withValue("data4", Long.valueOf(user.id));
        arrayList.add(newInsert3.build());
        ContentProviderOperation.Builder newInsert4 = ContentProviderOperation.newInsert(uri);
        newInsert4.withValueBackReference("raw_contact_id", size);
        newInsert4.withValue("mimetype", "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call");
        newInsert4.withValue("data1", Long.valueOf(user.id));
        newInsert4.withValue("data2", "Telegram Voice Call");
        newInsert4.withValue("data3", LocaleController.formatString("ContactShortcutVoiceCall", R.string.ContactShortcutVoiceCall, str));
        newInsert4.withValue("data4", Long.valueOf(user.id));
        arrayList.add(newInsert4.build());
        ContentProviderOperation.Builder newInsert5 = ContentProviderOperation.newInsert(uri);
        newInsert5.withValueBackReference("raw_contact_id", size);
        newInsert5.withValue("mimetype", "vnd.android.cursor.item/vnd.org.telegram.messenger.android.call.video");
        newInsert5.withValue("data1", Long.valueOf(user.id));
        newInsert5.withValue("data2", "Telegram Video Call");
        newInsert5.withValue("data3", LocaleController.formatString("ContactShortcutVideoCall", R.string.ContactShortcutVideoCall, str));
        newInsert5.withValue("data4", Long.valueOf(user.id));
        arrayList.add(newInsert5.build());
    }

    private void applyContactsUpdates(ArrayList<Long> arrayList, ConcurrentHashMap<Long, TLRPC.User> concurrentHashMap, final ArrayList<TLRPC.TL_contact> arrayList2, final ArrayList<Long> arrayList3) {
        int indexOf;
        int indexOf2;
        if (arrayList2 == null || arrayList3 == null) {
            arrayList2 = new ArrayList<>();
            arrayList3 = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                Long l = arrayList.get(i);
                if (l.longValue() > 0) {
                    TLRPC.TL_contact tL_contact = new TLRPC.TL_contact();
                    tL_contact.user_id = l.longValue();
                    arrayList2.add(tL_contact);
                } else if (l.longValue() < 0) {
                    arrayList3.add(Long.valueOf(-l.longValue()));
                }
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("process update - contacts add = " + arrayList2.size() + " delete = " + arrayList3.size());
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int i2 = 0;
        boolean z = false;
        while (true) {
            if (i2 >= arrayList2.size()) {
                break;
            }
            TLRPC.TL_contact tL_contact2 = arrayList2.get(i2);
            TLRPC.User user = concurrentHashMap != null ? concurrentHashMap.get(Long.valueOf(tL_contact2.user_id)) : null;
            if (user == null) {
                user = getMessagesController().getUser(Long.valueOf(tL_contact2.user_id));
            } else {
                getMessagesController().putUser(user, true);
            }
            if (user == null || TextUtils.isEmpty(user.phone)) {
                z = true;
            } else {
                Contact contact = this.contactsBookSPhones.get(user.phone);
                if (contact != null && (indexOf2 = contact.shortPhones.indexOf(user.phone)) != -1) {
                    contact.phoneDeleted.set(indexOf2, 0);
                }
                if (sb.length() != 0) {
                    sb.append(",");
                }
                sb.append(user.phone);
            }
            i2++;
        }
        for (int i3 = 0; i3 < arrayList3.size(); i3++) {
            final Long l2 = arrayList3.get(i3);
            Utilities.phoneBookQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$applyContactsUpdates$46(l2);
                }
            });
            TLRPC.User user2 = concurrentHashMap != null ? concurrentHashMap.get(l2) : null;
            if (user2 == null) {
                user2 = getMessagesController().getUser(l2);
            } else {
                getMessagesController().putUser(user2, true);
            }
            if (user2 == null) {
                z = true;
            } else if (!TextUtils.isEmpty(user2.phone)) {
                Contact contact2 = this.contactsBookSPhones.get(user2.phone);
                if (contact2 != null && (indexOf = contact2.shortPhones.indexOf(user2.phone)) != -1) {
                    contact2.phoneDeleted.set(indexOf, 1);
                }
                if (sb2.length() != 0) {
                    sb2.append(",");
                }
                sb2.append(user2.phone);
            }
        }
        if (sb.length() != 0 || sb2.length() != 0) {
            getMessagesStorage().applyPhoneBookUpdates(sb.toString(), sb2.toString());
        }
        if (z) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$applyContactsUpdates$47();
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$applyContactsUpdates$48(arrayList2, arrayList3);
                }
            });
        }
    }

    private void buildContactsSectionsArrays(boolean z) {
        final Collator localeCollator = getLocaleCollator();
        if (z) {
            Collections.sort(this.contacts, new Comparator() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda18
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$buildContactsSectionsArrays$43;
                    lambda$buildContactsSectionsArrays$43 = ContactsController.this.lambda$buildContactsSectionsArrays$43(localeCollator, (TLRPC.TL_contact) obj, (TLRPC.TL_contact) obj2);
                    return lambda$buildContactsSectionsArrays$43;
                }
            });
        }
        HashMap<String, ArrayList<TLRPC.TL_contact>> hashMap = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < this.contacts.size(); i++) {
            TLRPC.TL_contact tL_contact = this.contacts.get(i);
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(tL_contact.user_id));
            if (user != null) {
                String firstName = UserObject.getFirstName(user);
                if (firstName.length() > 1) {
                    firstName = firstName.substring(0, 1);
                }
                String upperCase = firstName.length() == 0 ? "#" : firstName.toUpperCase();
                String str = this.sectionsToReplace.get(upperCase);
                if (str != null) {
                    upperCase = str;
                }
                ArrayList<TLRPC.TL_contact> arrayList2 = hashMap.get(upperCase);
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList<>();
                    hashMap.put(upperCase, arrayList2);
                    arrayList.add(upperCase);
                }
                arrayList2.add(tL_contact);
            }
        }
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda19
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$buildContactsSectionsArrays$44;
                lambda$buildContactsSectionsArrays$44 = ContactsController.lambda$buildContactsSectionsArrays$44(localeCollator, (String) obj, (String) obj2);
                return lambda$buildContactsSectionsArrays$44;
            }
        });
        this.usersSectionsDict = hashMap;
        this.sortedUsersSectionsArray = arrayList;
    }

    private boolean checkContactsInternal() {
        boolean z = false;
        try {
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (!hasContactsPermission()) {
            return false;
        }
        try {
            Cursor query = ApplicationLoader.applicationContext.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, new String[]{"version"}, null, null, null);
            if (query != null) {
                try {
                    StringBuilder sb = new StringBuilder();
                    while (query.moveToNext()) {
                        sb.append(query.getString(query.getColumnIndex("version")));
                    }
                    String sb2 = sb.toString();
                    if (this.lastContactsVersions.length() != 0 && !this.lastContactsVersions.equals(sb2)) {
                        z = true;
                    }
                    this.lastContactsVersions = sb2;
                } finally {
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        return z;
    }

    private void deleteContactFromPhoneBook(long j) {
        if (hasContactsPermission()) {
            synchronized (this.observerLock) {
                this.ignoreChanges = true;
            }
            try {
                ApplicationLoader.applicationContext.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").appendQueryParameter("account_name", this.systemAccount.name).appendQueryParameter("account_type", this.systemAccount.type).build(), "sync2 = " + j, null);
            } catch (Exception e) {
                FileLog.e((Throwable) e, false);
            }
            synchronized (this.observerLock) {
                this.ignoreChanges = false;
            }
        }
    }

    public static String formatName(String str, String str2) {
        return formatName(str, str2, 0);
    }

    public static String formatName(String str, String str2, int i) {
        char charAt;
        if (str != null) {
            str = str.trim();
        }
        if (str != null && str2 == null && i > 0 && str.contains(" ")) {
            int indexOf = str.indexOf(" ");
            String substring = str.substring(indexOf + 1);
            str = str.substring(0, indexOf);
            str2 = substring;
        }
        if (str2 != null) {
            str2 = str2.trim();
        }
        StringBuilder sb = new StringBuilder((str != null ? str.length() : 0) + (str2 != null ? str2.length() : 0) + 1);
        if (LocaleController.nameDisplayOrder != 1) {
            if (str2 == null || str2.length() <= 0) {
                if (str != null && str.length() > 0) {
                    if (i > 0 && str.length() > i + 2) {
                        return str.substring(0, i) + "…";
                    }
                    sb.append(str);
                }
            } else {
                if (i > 0 && str2.length() > i + 2) {
                    return str2.substring(0, i) + "…";
                }
                sb.append(str2);
                if (str != null && str.length() > 0) {
                    sb.append(" ");
                    if (i > 0 && sb.length() + str.length() > i) {
                        charAt = str.charAt(0);
                        sb.append(charAt);
                    }
                    sb.append(str);
                }
            }
            return sb.toString();
        }
        if (str == null || str.length() <= 0) {
            if (str2 != null && str2.length() > 0) {
                if (i > 0 && str2.length() > i + 2) {
                    return str2.substring(0, i) + "…";
                }
                sb.append(str2);
            }
            return sb.toString();
        }
        if (i > 0 && str.length() > i + 2) {
            return str.substring(0, i) + "…";
        }
        sb.append(str);
        if (str2 != null && str2.length() > 0) {
            sb.append(" ");
            if (i > 0 && sb.length() + str2.length() > i) {
                charAt = str2.charAt(0);
                sb.append(charAt);
            }
            sb.append(str2);
        }
        return sb.toString();
    }

    public static String formatName(TLObject tLObject) {
        return tLObject instanceof TLRPC.User ? formatName((TLRPC.User) tLObject) : tLObject instanceof TLRPC.Chat ? ((TLRPC.Chat) tLObject).title : LocaleController.getString(R.string.HiddenName);
    }

    public static String formatName(TLRPC.User user) {
        return user == null ? "" : formatName(user.first_name, user.last_name, 0);
    }

    private long getContactsHash(ArrayList<TLRPC.TL_contact> arrayList) {
        ArrayList arrayList2 = new ArrayList(arrayList);
        Collections.sort(arrayList2, new Comparator() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda51
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$getContactsHash$26;
                lambda$getContactsHash$26 = ContactsController.lambda$getContactsHash$26((TLRPC.TL_contact) obj, (TLRPC.TL_contact) obj2);
                return lambda$getContactsHash$26;
            }
        });
        int size = arrayList2.size();
        long j = 0;
        int i = -1;
        while (i < size) {
            j = MediaDataController.calcHash(j, i == -1 ? getUserConfig().contactsSavedCount : ((TLRPC.TL_contact) arrayList2.get(i)).user_id);
            i++;
        }
        return j;
    }

    public static ContactsController getInstance(int i) {
        ContactsController contactsController = Instance[i];
        if (contactsController == null) {
            synchronized (ContactsController.class) {
                try {
                    contactsController = Instance[i];
                    if (contactsController == null) {
                        ContactsController[] contactsControllerArr = Instance;
                        ContactsController contactsController2 = new ContactsController(i);
                        contactsControllerArr[i] = contactsController2;
                        contactsController = contactsController2;
                    }
                } finally {
                }
            }
        }
        return contactsController;
    }

    public static Collator getLocaleCollator() {
        if (cachedCollator == null || cachedCollatorLocale != Locale.getDefault()) {
            try {
                Locale locale = Locale.getDefault();
                cachedCollatorLocale = locale;
                Collator collator = Collator.getInstance(locale);
                cachedCollator = collator;
                collator.setStrength(1);
            } catch (Exception e) {
                FileLog.e((Throwable) e, true);
            }
        }
        if (cachedCollator == null) {
            try {
                Collator collator2 = Collator.getInstance();
                cachedCollator = collator2;
                collator2.setStrength(1);
            } catch (Exception e2) {
                FileLog.e((Throwable) e2, true);
            }
        }
        if (cachedCollator == null) {
            cachedCollator = new Collator() { // from class: org.telegram.messenger.ContactsController.1
                @Override // java.text.Collator
                public int compare(String str, String str2) {
                    if (str == null || str2 == null) {
                        return 0;
                    }
                    return str.compareTo(str2);
                }

                @Override // java.text.Collator
                public CollationKey getCollationKey(String str) {
                    return null;
                }

                @Override // java.text.Collator
                public int hashCode() {
                    return 0;
                }
            };
        }
        return cachedCollator;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x002c, code lost:
    
        if (r0.getCount() == 0) goto L20;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean hasContactsPermission() {
        int checkSelfPermission;
        if (Build.VERSION.SDK_INT >= 23) {
            checkSelfPermission = ApplicationLoader.applicationContext.checkSelfPermission("android.permission.READ_CONTACTS");
            return checkSelfPermission == 0;
        }
        Cursor cursor = null;
        try {
            cursor = ApplicationLoader.applicationContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, this.projectionPhones, null, null, null);
            if (cursor != null) {
            }
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            return false;
        } catch (Throwable th) {
            try {
                FileLog.e(th);
            } finally {
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                }
            }
        }
        return true;
    }

    private boolean hasContactsWritePermission() {
        int checkSelfPermission;
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        checkSelfPermission = ApplicationLoader.applicationContext.checkSelfPermission("android.permission.WRITE_CONTACTS");
        return checkSelfPermission == 0;
    }

    private boolean isNotValidNameString(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        int length = str.length();
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt >= '0' && charAt <= '9') {
                i++;
            }
        }
        return i > 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addContact$50(TLRPC.User user) {
        addContactToPhoneBook(user, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addContact$51(TLRPC.Updates updates, TLRPC.User user) {
        Contact contact;
        boolean z = false;
        for (int i = 0; i < updates.users.size(); i++) {
            TLRPC.User user2 = updates.users.get(i);
            if (user2.contact && (contact = this.contactsBookSPhones.get(user2.phone)) != null) {
                String letter = contact.getLetter();
                String letter2 = Contact.getLetter(user.first_name, user.last_name);
                if (contact.user == null) {
                    contact.user = user;
                    if (!letter.equals(letter2)) {
                        ArrayList<Object> arrayList = this.phoneBookSectionsDict.get(letter2);
                        if (arrayList == null) {
                            arrayList = new ArrayList<>();
                            this.phoneBookSectionsDict.put(letter2, arrayList);
                            this.phoneBookSectionsArray.add(letter2);
                        }
                        arrayList.add(contact);
                        ArrayList<Object> arrayList2 = this.phoneBookSectionsDict.get(letter);
                        if (arrayList2 != null) {
                            Iterator<Object> it = arrayList2.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                Object next = it.next();
                                if (next instanceof Contact) {
                                    Contact contact2 = (Contact) next;
                                    if (contact2.contact_id == contact.contact_id) {
                                        if (arrayList2.remove(contact2) && arrayList2.isEmpty()) {
                                            this.phoneBookSectionsDict.remove(letter);
                                            this.phoneBookSectionsArray.remove(letter);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    z = true;
                }
            }
            if (user2.contact && this.contactsDict.get(Long.valueOf(user2.id)) == null) {
                TLRPC.TL_contact tL_contact = new TLRPC.TL_contact();
                tL_contact.user_id = user2.id;
                this.contacts.add(tL_contact);
                this.contactsDict.put(Long.valueOf(tL_contact.user_id), tL_contact);
            }
        }
        buildContactsSectionsArrays(true);
        if (z) {
            mergePhonebookAndTelegramContacts(this.phoneBookSectionsDict, this.phoneBookSectionsArray, this.phoneBookByShortPhones, false);
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addContact$52(final TLRPC.User user, TLObject tLObject, TLRPC.TL_error tL_error) {
        int indexOf;
        if (tL_error != null) {
            return;
        }
        final TLRPC.Updates updates = (TLRPC.Updates) tLObject;
        TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
        if (userProfilePhoto != null && userProfilePhoto.personal) {
            for (int i = 0; i < updates.users.size(); i++) {
                if (updates.users.get(i).id == user.id) {
                    updates.users.get(i).photo = user.photo;
                }
            }
        }
        getMessagesController().processUpdates(updates, false);
        for (int i2 = 0; i2 < updates.users.size(); i2++) {
            final TLRPC.User user2 = updates.users.get(i2);
            if (user2.id == user.id) {
                Utilities.phoneBookQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda20
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsController.this.lambda$addContact$50(user2);
                    }
                });
                TLRPC.TL_contact tL_contact = new TLRPC.TL_contact();
                tL_contact.user_id = user2.id;
                ArrayList<TLRPC.TL_contact> arrayList = new ArrayList<>();
                arrayList.add(tL_contact);
                getMessagesStorage().putContacts(arrayList, false);
                if (!TextUtils.isEmpty(user2.phone)) {
                    formatName(user2.first_name, user2.last_name);
                    getMessagesStorage().applyPhoneBookUpdates(user2.phone, "");
                    Contact contact = this.contactsBookSPhones.get(user2.phone);
                    if (contact != null && (indexOf = contact.shortPhones.indexOf(user2.phone)) != -1) {
                        contact.phoneDeleted.set(indexOf, 0);
                    }
                }
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$addContact$51(updates, user);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyContactsUpdates$46(Long l) {
        deleteContactFromPhoneBook(l.longValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyContactsUpdates$47() {
        loadContacts(false, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyContactsUpdates$48(ArrayList arrayList, ArrayList arrayList2) {
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC.TL_contact tL_contact = (TLRPC.TL_contact) arrayList.get(i);
            if (this.contactsDict.get(Long.valueOf(tL_contact.user_id)) == null) {
                this.contacts.add(tL_contact);
                this.contactsDict.put(Long.valueOf(tL_contact.user_id), tL_contact);
            }
        }
        for (int i2 = 0; i2 < arrayList2.size(); i2++) {
            Long l = (Long) arrayList2.get(i2);
            TLRPC.TL_contact tL_contact2 = this.contactsDict.get(l);
            if (tL_contact2 != null) {
                this.contacts.remove(tL_contact2);
                this.contactsDict.remove(l);
            }
        }
        if (!arrayList.isEmpty()) {
            updateUnregisteredContacts();
            performWriteContactsToPhoneBook();
        }
        performSyncPhoneBook(getContactsCopy(this.contactsBook), false, false, false, false, true, false);
        buildContactsSectionsArrays(!arrayList.isEmpty());
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$buildContactsSectionsArrays$43(Collator collator, TLRPC.TL_contact tL_contact, TLRPC.TL_contact tL_contact2) {
        return collator.compare(UserObject.getFirstName(getMessagesController().getUser(Long.valueOf(tL_contact.user_id))), UserObject.getFirstName(getMessagesController().getUser(Long.valueOf(tL_contact2.user_id))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$buildContactsSectionsArrays$44(Collator collator, String str, String str2) {
        char charAt = str.charAt(0);
        char charAt2 = str2.charAt(0);
        if (charAt == '#') {
            return 1;
        }
        if (charAt2 == '#') {
            return -1;
        }
        return collator.compare(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAppAccount$4() {
        AccountManager accountManager = AccountManager.get(ApplicationLoader.applicationContext);
        try {
            Account[] accountsByType = accountManager.getAccountsByType(BuildConfig.LIBRARY_PACKAGE_NAME);
            for (int i = 0; i < accountsByType.length; i++) {
                Account account = accountsByType[i];
                int i2 = 0;
                while (true) {
                    if (i2 < 4) {
                        TLRPC.User currentUser = UserConfig.getInstance(i2).getCurrentUser();
                        if (currentUser != null) {
                            if (account.name.equals("" + currentUser.id)) {
                                if (i2 == this.currentAccount) {
                                    this.systemAccount = account;
                                }
                            }
                        }
                        i2++;
                    } else {
                        try {
                            accountManager.removeAccount(accountsByType[i], null, null);
                            break;
                        } catch (Exception unused) {
                        }
                    }
                }
            }
        } catch (Throwable unused2) {
        }
        if (getUserConfig().isClientActivated()) {
            readContacts();
            if (this.systemAccount == null) {
                try {
                    Account account2 = new Account("" + getUserConfig().getClientUserId(), BuildConfig.LIBRARY_PACKAGE_NAME);
                    this.systemAccount = account2;
                    accountManager.addAccountExplicitly(account2, "", null);
                } catch (Exception unused3) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkContacts$5() {
        if (checkContactsInternal()) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("detected contacts change");
            }
            performSyncPhoneBook(getContactsCopy(this.contactsBook), true, false, true, false, true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkInviteText$2(TLRPC.TL_help_inviteText tL_help_inviteText) {
        this.updatingInviteLink = false;
        SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
        String str = tL_help_inviteText.message;
        this.inviteLink = str;
        edit.putString("invitelink", str);
        edit.putInt("invitelinktime", (int) (System.currentTimeMillis() / 1000));
        edit.commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkInviteText$3(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject != null) {
            final TLRPC.TL_help_inviteText tL_help_inviteText = (TLRPC.TL_help_inviteText) tLObject;
            if (tL_help_inviteText.message.length() != 0) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda25
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsController.this.lambda$checkInviteText$2(tL_help_inviteText);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanup$1() {
        this.migratingContacts = false;
        this.completedRequestsCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteAllContacts$8(Runnable runnable) {
        AccountManager accountManager = AccountManager.get(ApplicationLoader.applicationContext);
        try {
            Account[] accountsByType = accountManager.getAccountsByType(BuildConfig.LIBRARY_PACKAGE_NAME);
            this.systemAccount = null;
            for (Account account : accountsByType) {
                int i = 0;
                while (true) {
                    if (i >= 4) {
                        break;
                    }
                    TLRPC.User currentUser = UserConfig.getInstance(i).getCurrentUser();
                    if (currentUser != null) {
                        if (account.name.equals("" + currentUser.id)) {
                            accountManager.removeAccount(account, null, null);
                            break;
                        }
                    }
                    i++;
                }
            }
        } catch (Throwable unused) {
        }
        try {
            Account account2 = new Account("" + getUserConfig().getClientUserId(), BuildConfig.LIBRARY_PACKAGE_NAME);
            this.systemAccount = account2;
            accountManager.addAccountExplicitly(account2, "", null);
        } catch (Exception unused2) {
        }
        getMessagesStorage().putCachedPhoneBook(new HashMap<>(), false, true);
        getMessagesStorage().putContacts(new ArrayList<>(), true);
        this.phoneBookContacts.clear();
        this.contacts.clear();
        this.contactsDict.clear();
        this.usersSectionsDict.clear();
        this.usersMutualSectionsDict.clear();
        this.sortedUsersSectionsArray.clear();
        this.phoneBookSectionsDict.clear();
        this.phoneBookSectionsArray.clear();
        this.phoneBookByShortPhones.clear();
        this.delayedContactsUpdate.clear();
        this.sortedUsersMutualSectionsArray.clear();
        this.contactsByPhone.clear();
        this.contactsByShortPhone.clear();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
        loadContacts(false, 0L);
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteAllContacts$9(final Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            AndroidUtilities.runOnUIThread(runnable);
            return;
        }
        this.contactsBookSPhones.clear();
        this.contactsBook.clear();
        this.completedRequestsCount = 0;
        this.migratingContacts = false;
        this.contactsSyncInProgress = false;
        this.contactsLoaded = false;
        this.loadingContacts = false;
        this.contactsBookLoaded = false;
        this.lastContactsVersions = "";
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$deleteAllContacts$8(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteContact$55(ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            deleteContactFromPhoneBook(((TLRPC.User) it.next()).id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteContact$56(ArrayList arrayList, boolean z, String str) {
        Iterator it = arrayList.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            TLRPC.User user = (TLRPC.User) it.next();
            TLRPC.TL_contact tL_contact = this.contactsDict.get(Long.valueOf(user.id));
            if (tL_contact != null) {
                this.contacts.remove(tL_contact);
                this.contactsDict.remove(Long.valueOf(user.id));
                z2 = true;
            }
        }
        if (z2) {
            buildContactsSectionsArrays(false);
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_NAME));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
        if (z) {
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 1, LocaleController.formatString("DeletedFromYourContacts", R.string.DeletedFromYourContacts, str));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteContact$57(ArrayList arrayList, final ArrayList arrayList2, final boolean z, final String str, TLObject tLObject, TLRPC.TL_error tL_error) {
        int indexOf;
        if (tL_error != null) {
            return;
        }
        getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        getMessagesStorage().deleteContacts(arrayList);
        Utilities.phoneBookQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$deleteContact$55(arrayList2);
            }
        });
        for (int i = 0; i < arrayList2.size(); i++) {
            TLRPC.User user = (TLRPC.User) arrayList2.get(i);
            if (!TextUtils.isEmpty(user.phone)) {
                getMessagesStorage().applyPhoneBookUpdates(user.phone, "");
                Contact contact = this.contactsBookSPhones.get(user.phone);
                if (contact != null && (indexOf = contact.shortPhones.indexOf(user.phone)) != -1) {
                    contact.phoneDeleted.set(indexOf, 1);
                }
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$deleteContact$56(arrayList2, z, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteContactsUndoable$53(HashMap hashMap) {
        for (Map.Entry entry : hashMap.entrySet()) {
            TLRPC.User user = (TLRPC.User) entry.getKey();
            TLRPC.TL_contact tL_contact = (TLRPC.TL_contact) entry.getValue();
            user.contact = true;
            this.contacts.add(tL_contact);
            this.contactsDict.put(Long.valueOf(user.id), tL_contact);
        }
        buildContactsSectionsArrays(true);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_NAME));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteContactsUndoable$54(ArrayList arrayList) {
        deleteContact(arrayList, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forceImportContacts$6() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("force import contacts");
        }
        performSyncPhoneBook(new HashMap<>(), true, true, true, true, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getContactsHash$26(TLRPC.TL_contact tL_contact, TLRPC.TL_contact tL_contact2) {
        long j = tL_contact.user_id;
        long j2 = tL_contact2.user_id;
        if (j > j2) {
            return 1;
        }
        return j < j2 ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadContacts$27() {
        synchronized (this.loadContactsSync) {
            this.loadingContacts = false;
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadContacts$28(long j, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            TLRPC.contacts_Contacts contacts_contacts = (TLRPC.contacts_Contacts) tLObject;
            if (j == 0 || !(contacts_contacts instanceof TLRPC.TL_contacts_contactsNotModified)) {
                getUserConfig().contactsSavedCount = contacts_contacts.saved_count;
                getUserConfig().saveConfig(false);
                processLoadedContacts(contacts_contacts.contacts, contacts_contacts.users, 0);
                return;
            }
            this.contactsLoaded = true;
            if (!this.delayedContactsUpdate.isEmpty() && this.contactsBookLoaded) {
                applyContactsUpdates(this.delayedContactsUpdate, null, null, null);
                this.delayedContactsUpdate.clear();
            }
            getUserConfig().lastContactsSyncTime = (int) (System.currentTimeMillis() / 1000);
            getUserConfig().saveConfig(false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$loadContacts$27();
                }
            });
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("load contacts don't change");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGlobalPrivacySetting$60(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            this.globalPrivacySettings = (TLRPC.TL_globalPrivacySettings) tLObject;
            this.loadingGlobalSettings = 2;
        } else {
            this.loadingGlobalSettings = 0;
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.privacyRulesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGlobalPrivacySetting$61(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$loadGlobalPrivacySetting$60(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPrivacySettings$62(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            this.deleteAccountTTL = ((TLRPC.TL_accountDaysTTL) tLObject).days;
            this.loadingDeleteInfo = 2;
        } else {
            this.loadingDeleteInfo = 0;
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.privacyRulesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPrivacySettings$63(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$loadPrivacySettings$62(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPrivacySettings$64(TLRPC.TL_error tL_error, TLObject tLObject, int i) {
        if (tL_error == null) {
            TL_account.privacyRules privacyrules = (TL_account.privacyRules) tLObject;
            getMessagesController().putUsers(privacyrules.users, false);
            getMessagesController().putChats(privacyrules.chats, false);
            ArrayList<TLRPC.PrivacyRule> arrayList = privacyrules.rules;
            switch (i) {
                case 0:
                    this.lastseenPrivacyRules = arrayList;
                    break;
                case 1:
                    this.groupPrivacyRules = arrayList;
                    break;
                case 2:
                    this.callPrivacyRules = arrayList;
                    break;
                case 3:
                    this.p2pPrivacyRules = arrayList;
                    break;
                case 4:
                    this.profilePhotoPrivacyRules = arrayList;
                    break;
                case 5:
                    this.forwardsPrivacyRules = arrayList;
                    break;
                case 6:
                    this.phonePrivacyRules = arrayList;
                    break;
                case 7:
                case 10:
                default:
                    this.addedByPhonePrivacyRules = arrayList;
                    break;
                case 8:
                    this.voiceMessagesRules = arrayList;
                    break;
                case 9:
                    this.bioPrivacyRules = arrayList;
                    break;
                case 11:
                    this.birthdayPrivacyRules = arrayList;
                    break;
                case 12:
                    this.giftsPrivacyRules = arrayList;
                    break;
            }
            this.loadingPrivacyInfo[i] = 2;
        } else {
            this.loadingPrivacyInfo[i] = 0;
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.privacyRulesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPrivacySettings$65(final int i, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$loadPrivacySettings$64(tL_error, tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$markAsContacted$49(String str) {
        Uri parse = Uri.parse(str);
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_time_contacted", Long.valueOf(System.currentTimeMillis()));
        ApplicationLoader.applicationContext.getContentResolver().update(parse, contentValues, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x002e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ int lambda$mergePhonebookAndTelegramContacts$38(Collator collator, Object obj, Object obj2) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6 = "";
        if (!(obj instanceof TLRPC.User)) {
            if (obj instanceof Contact) {
                Contact contact = (Contact) obj;
                TLRPC.User user = contact.user;
                if (user != null) {
                    str = formatName(user.first_name, user.last_name);
                } else {
                    str2 = contact.first_name;
                    str3 = contact.last_name;
                }
            } else {
                str = "";
            }
            if (obj2 instanceof TLRPC.User) {
                if (obj2 instanceof Contact) {
                    Contact contact2 = (Contact) obj2;
                    TLRPC.User user2 = contact2.user;
                    if (user2 != null) {
                        str6 = formatName(user2.first_name, user2.last_name);
                    } else {
                        str4 = contact2.first_name;
                        str5 = contact2.last_name;
                    }
                }
                return collator.compare(str, str6);
            }
            TLRPC.User user3 = (TLRPC.User) obj2;
            str4 = user3.first_name;
            str5 = user3.last_name;
            str6 = formatName(str4, str5);
            return collator.compare(str, str6);
        }
        TLRPC.User user4 = (TLRPC.User) obj;
        str2 = user4.first_name;
        str3 = user4.last_name;
        str = formatName(str2, str3);
        if (obj2 instanceof TLRPC.User) {
        }
        str6 = formatName(str4, str5);
        return collator.compare(str, str6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$mergePhonebookAndTelegramContacts$39(Collator collator, String str, String str2) {
        char charAt = str.charAt(0);
        char charAt2 = str2.charAt(0);
        if (charAt == '#') {
            return 1;
        }
        if (charAt2 == '#') {
            return -1;
        }
        return collator.compare(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$mergePhonebookAndTelegramContacts$40(ArrayList arrayList, HashMap hashMap, HashMap hashMap2) {
        this.phoneBookSectionsArray = arrayList;
        this.phoneBookByShortPhones = hashMap;
        this.phoneBookSectionsDict = hashMap2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$mergePhonebookAndTelegramContacts$41(boolean z, ArrayList arrayList, final HashMap hashMap, final HashMap hashMap2, final ArrayList arrayList2) {
        if (z) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                TLRPC.User user = getMessagesController().getUser(Long.valueOf(((TLRPC.TL_contact) arrayList.get(i)).user_id));
                if (user != null && !TextUtils.isEmpty(user.phone)) {
                    Contact contact = (Contact) hashMap.get(user.phone.substring(Math.max(0, r3.length() - 7)));
                    if (contact == null) {
                        String letter = Contact.getLetter(user.first_name, user.last_name);
                        ArrayList arrayList3 = (ArrayList) hashMap2.get(letter);
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                            hashMap2.put(letter, arrayList3);
                            arrayList2.add(letter);
                        }
                        arrayList3.add(user);
                    } else if (contact.user == null) {
                        contact.user = user;
                    }
                }
            }
        }
        final Collator localeCollator = getLocaleCollator();
        Iterator it = hashMap2.values().iterator();
        while (it.hasNext()) {
            Collections.sort((ArrayList) it.next(), new Comparator() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda26
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$mergePhonebookAndTelegramContacts$38;
                    lambda$mergePhonebookAndTelegramContacts$38 = ContactsController.lambda$mergePhonebookAndTelegramContacts$38(localeCollator, obj, obj2);
                    return lambda$mergePhonebookAndTelegramContacts$38;
                }
            });
        }
        Collections.sort(arrayList2, new Comparator() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda27
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$mergePhonebookAndTelegramContacts$39;
                lambda$mergePhonebookAndTelegramContacts$39 = ContactsController.lambda$mergePhonebookAndTelegramContacts$39(localeCollator, (String) obj, (String) obj2);
                return lambda$mergePhonebookAndTelegramContacts$39;
            }
        });
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$mergePhonebookAndTelegramContacts$40(arrayList2, hashMap, hashMap2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$migratePhoneBookToV7$12(SparseArray sparseArray) {
        if (this.migratingContacts) {
            return;
        }
        this.migratingContacts = true;
        HashMap<String, Contact> hashMap = new HashMap<>();
        HashMap<String, Contact> readContactsFromPhoneBook = readContactsFromPhoneBook();
        HashMap hashMap2 = new HashMap();
        Iterator<Map.Entry<String, Contact>> it = readContactsFromPhoneBook.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Contact value = it.next().getValue();
            for (int i = 0; i < value.shortPhones.size(); i++) {
                hashMap2.put(value.shortPhones.get(i), value.key);
            }
        }
        for (int i2 = 0; i2 < sparseArray.size(); i2++) {
            Contact contact = (Contact) sparseArray.valueAt(i2);
            int i3 = 0;
            while (true) {
                if (i3 >= contact.shortPhones.size()) {
                    break;
                }
                String str = (String) hashMap2.get(contact.shortPhones.get(i3));
                if (str != null) {
                    contact.key = str;
                    hashMap.put(str, contact);
                    break;
                }
                i3++;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("migrated contacts " + hashMap.size() + " of " + sparseArray.size());
        }
        getMessagesStorage().putCachedPhoneBook(hashMap, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        try {
            if (hasContactsPermission()) {
                ApplicationLoader.applicationContext.getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new MyContentObserver());
            }
        } catch (Throwable unused) {
        }
    }

    private /* synthetic */ void lambda$performSyncPhoneBook$13(HashMap hashMap) {
        ArrayList<TLRPC.User> arrayList = new ArrayList<>();
        if (hashMap != null && !hashMap.isEmpty()) {
            try {
                HashMap hashMap2 = new HashMap();
                for (int i = 0; i < this.contacts.size(); i++) {
                    TLRPC.User user = getMessagesController().getUser(Long.valueOf(this.contacts.get(i).user_id));
                    if (user != null && !TextUtils.isEmpty(user.phone)) {
                        hashMap2.put(user.phone, user);
                    }
                }
                Iterator it = hashMap.entrySet().iterator();
                while (it.hasNext()) {
                    Contact contact = (Contact) ((Map.Entry) it.next()).getValue();
                    int i2 = 0;
                    boolean z = false;
                    while (i2 < contact.shortPhones.size()) {
                        TLRPC.User user2 = (TLRPC.User) hashMap2.get(contact.shortPhones.get(i2));
                        if (user2 != null) {
                            arrayList.add(user2);
                            contact.shortPhones.remove(i2);
                            i2--;
                            z = true;
                        }
                        i2++;
                    }
                    if (z) {
                        contact.shortPhones.size();
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        deleteContact(arrayList, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$14(int i, HashMap hashMap, boolean z, boolean z2) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.hasNewContactsToImport, Integer.valueOf(i), hashMap, Boolean.valueOf(z), Boolean.valueOf(z2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$15(HashMap hashMap, ArrayList arrayList, HashMap hashMap2) {
        lambda$performSyncPhoneBook$23(hashMap, arrayList, hashMap2);
        updateUnregisteredContacts();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsImported, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$16(HashMap hashMap, HashMap hashMap2, boolean z, final HashMap hashMap3, final ArrayList arrayList, final HashMap hashMap4) {
        this.contactsBookSPhones = hashMap;
        this.contactsBook = hashMap2;
        this.contactsSyncInProgress = false;
        this.contactsBookLoaded = true;
        if (z) {
            this.contactsLoaded = true;
        }
        if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded) {
            applyContactsUpdates(this.delayedContactsUpdate, null, null, null);
            this.delayedContactsUpdate.clear();
        }
        getMessagesStorage().putCachedPhoneBook(hashMap2, false, false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$performSyncPhoneBook$15(hashMap3, arrayList, hashMap4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$17(HashMap hashMap, ArrayList arrayList, HashMap hashMap2) {
        lambda$performSyncPhoneBook$23(hashMap, arrayList, hashMap2);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsImported, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$18() {
        getMessagesStorage().getCachedPhoneBook(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$19(HashMap hashMap, HashMap hashMap2, boolean z, final HashMap hashMap3, final ArrayList arrayList, final HashMap hashMap4, boolean[] zArr) {
        this.contactsBookSPhones = hashMap;
        this.contactsBook = hashMap2;
        this.contactsSyncInProgress = false;
        this.contactsBookLoaded = true;
        if (z) {
            this.contactsLoaded = true;
        }
        if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded) {
            applyContactsUpdates(this.delayedContactsUpdate, null, null, null);
            this.delayedContactsUpdate.clear();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$performSyncPhoneBook$17(hashMap3, arrayList, hashMap4);
            }
        });
        if (zArr[0]) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$performSyncPhoneBook$18();
                }
            }, 300000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$20(HashMap hashMap, SparseArray sparseArray, final boolean[] zArr, final HashMap hashMap2, TLRPC.TL_contacts_importContacts tL_contacts_importContacts, int i, final HashMap hashMap3, final boolean z, final HashMap hashMap4, final ArrayList arrayList, final HashMap hashMap5, TLObject tLObject, TLRPC.TL_error tL_error) {
        this.completedRequestsCount++;
        if (tL_error == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("contacts imported");
            }
            TLRPC.TL_contacts_importedContacts tL_contacts_importedContacts = (TLRPC.TL_contacts_importedContacts) tLObject;
            if (!tL_contacts_importedContacts.retry_contacts.isEmpty()) {
                for (int i2 = 0; i2 < tL_contacts_importedContacts.retry_contacts.size(); i2++) {
                    hashMap.remove(sparseArray.get((int) tL_contacts_importedContacts.retry_contacts.get(i2).longValue()));
                }
                zArr[0] = true;
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("result has retry contacts");
                }
            }
            for (int i3 = 0; i3 < tL_contacts_importedContacts.popular_invites.size(); i3++) {
                TLRPC.TL_popularContact tL_popularContact = tL_contacts_importedContacts.popular_invites.get(i3);
                Contact contact = (Contact) hashMap2.get(sparseArray.get((int) tL_popularContact.client_id));
                if (contact != null) {
                    contact.imported = tL_popularContact.importers;
                }
            }
            getMessagesStorage().putUsersAndChats(tL_contacts_importedContacts.users, null, true, true);
            ArrayList<TLRPC.TL_contact> arrayList2 = new ArrayList<>();
            for (int i4 = 0; i4 < tL_contacts_importedContacts.imported.size(); i4++) {
                TLRPC.TL_contact tL_contact = new TLRPC.TL_contact();
                tL_contact.user_id = tL_contacts_importedContacts.imported.get(i4).user_id;
                arrayList2.add(tL_contact);
            }
            processLoadedContacts(arrayList2, tL_contacts_importedContacts.users, 2);
        } else {
            for (int i5 = 0; i5 < tL_contacts_importContacts.contacts.size(); i5++) {
                hashMap.remove(sparseArray.get((int) tL_contacts_importContacts.contacts.get(i5).client_id));
            }
            zArr[0] = true;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("import contacts error " + tL_error.text);
            }
        }
        if (this.completedRequestsCount == i) {
            if (!hashMap.isEmpty()) {
                getMessagesStorage().putCachedPhoneBook(hashMap, false, false);
            }
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda57
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$performSyncPhoneBook$19(hashMap3, hashMap2, z, hashMap4, arrayList, hashMap5, zArr);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$21(HashMap hashMap, ArrayList arrayList, HashMap hashMap2) {
        lambda$performSyncPhoneBook$23(hashMap, arrayList, hashMap2);
        updateUnregisteredContacts();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsImported, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$22(HashMap hashMap, HashMap hashMap2, boolean z, final HashMap hashMap3, final ArrayList arrayList, final HashMap hashMap4) {
        this.contactsBookSPhones = hashMap;
        this.contactsBook = hashMap2;
        this.contactsSyncInProgress = false;
        this.contactsBookLoaded = true;
        if (z) {
            this.contactsLoaded = true;
        }
        if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded) {
            applyContactsUpdates(this.delayedContactsUpdate, null, null, null);
            this.delayedContactsUpdate.clear();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$performSyncPhoneBook$21(hashMap3, arrayList, hashMap4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSyncPhoneBook$24(HashMap hashMap, HashMap hashMap2, boolean z, final HashMap hashMap3, final ArrayList arrayList, final HashMap hashMap4) {
        this.contactsBookSPhones = hashMap;
        this.contactsBook = hashMap2;
        this.contactsSyncInProgress = false;
        this.contactsBookLoaded = true;
        if (z) {
            this.contactsLoaded = true;
        }
        if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded) {
            applyContactsUpdates(this.delayedContactsUpdate, null, null, null);
            this.delayedContactsUpdate.clear();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$performSyncPhoneBook$23(hashMap3, arrayList, hashMap4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x030f, code lost:
    
        if (r11.intValue() == 1) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0350, code lost:
    
        if (r2 != null) goto L140;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x014a, code lost:
    
        if (r2.last_name.equals(r8.last_name) == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0135, code lost:
    
        if (r2.first_name.equals(r8.first_name) != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x014c, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x022f, code lost:
    
        if (r5.equals(r8.last_name) != false) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x023f, code lost:
    
        if (android.text.TextUtils.isEmpty(r8.last_name) != false) goto L105;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0279  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x04d9  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0505  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0517  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01f2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$performSyncPhoneBook$25(final HashMap hashMap, final boolean z, boolean z2, final boolean z3, boolean z4, boolean z5, boolean z6) {
        ArrayList arrayList;
        HashMap hashMap2;
        HashMap hashMap3;
        ContactsController contactsController;
        HashMap<String, Contact> hashMap4;
        int i;
        int i2;
        HashMap<String, Contact> hashMap5;
        int i3;
        final int i4;
        HashMap<String, Contact> hashMap6;
        Contact contact;
        HashMap hashMap7;
        String str;
        ArrayList arrayList2;
        HashMap hashMap8;
        HashMap hashMap9;
        HashMap<String, Contact> hashMap10;
        int i5;
        int indexOf;
        ArrayList arrayList3;
        int i6;
        HashMap hashMap11;
        int i7;
        boolean z7;
        HashMap<String, Contact> hashMap12;
        ContactsController contactsController2 = this;
        HashMap hashMap13 = new HashMap();
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Contact contact2 = (Contact) ((Map.Entry) it.next()).getValue();
            for (int i8 = 0; i8 < contact2.shortPhones.size(); i8++) {
                hashMap13.put(contact2.shortPhones.get(i8), contact2);
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start read contacts from phone");
        }
        if (!z) {
            checkContactsInternal();
        }
        HashMap<String, Contact> readContactsFromPhoneBook = readContactsFromPhoneBook();
        HashMap hashMap14 = new HashMap();
        HashMap hashMap15 = new HashMap();
        ArrayList arrayList4 = new ArrayList();
        Iterator<Map.Entry<String, Contact>> it2 = readContactsFromPhoneBook.entrySet().iterator();
        while (it2.hasNext()) {
            Contact value = it2.next().getValue();
            int size = value.shortPhones.size();
            for (int i9 = 0; i9 < size; i9++) {
                hashMap15.put(value.shortPhones.get(i9).substring(Math.max(0, r6.length() - 7)), value);
            }
            String letter = value.getLetter();
            ArrayList arrayList5 = (ArrayList) hashMap14.get(letter);
            if (arrayList5 == null) {
                arrayList5 = new ArrayList();
                hashMap14.put(letter, arrayList5);
                arrayList4.add(letter);
            }
            arrayList5.add(value);
        }
        final HashMap hashMap16 = new HashMap();
        int size2 = hashMap.size();
        ArrayList arrayList6 = new ArrayList();
        String str2 = "";
        if (hashMap.isEmpty()) {
            arrayList = arrayList4;
            hashMap2 = hashMap15;
            hashMap3 = hashMap14;
            HashMap<String, Contact> hashMap17 = readContactsFromPhoneBook;
            if (z2) {
                int i10 = 0;
                for (Map.Entry<String, Contact> entry : hashMap17.entrySet()) {
                    Contact value2 = entry.getValue();
                    entry.getKey();
                    int i11 = 0;
                    while (i11 < value2.phones.size()) {
                        if (!z4) {
                            String str3 = value2.shortPhones.get(i11);
                            String substring = str3.substring(Math.max(0, str3.length() - 7));
                            TLRPC.TL_contact tL_contact = this.contactsByPhone.get(str3);
                            if (tL_contact != null) {
                                TLRPC.User user = getMessagesController().getUser(Long.valueOf(tL_contact.user_id));
                                if (user != null) {
                                    i10++;
                                    String str4 = user.first_name;
                                    if (str4 == null) {
                                        str4 = "";
                                    }
                                    String str5 = user.last_name;
                                    if (str5 == null) {
                                        str5 = "";
                                    }
                                    if ((str4.equals(value2.first_name) && str5.equals(value2.last_name)) || (TextUtils.isEmpty(value2.first_name) && TextUtils.isEmpty(value2.last_name))) {
                                        hashMap5 = hashMap17;
                                        i3 = 1;
                                        i11 += i3;
                                        hashMap17 = hashMap5;
                                    }
                                }
                            } else if (this.contactsByShortPhone.containsKey(substring)) {
                                i10++;
                            }
                        }
                        TLRPC.TL_inputPhoneContact tL_inputPhoneContact = new TLRPC.TL_inputPhoneContact();
                        hashMap5 = hashMap17;
                        tL_inputPhoneContact.client_id = value2.contact_id | (i11 << 32);
                        tL_inputPhoneContact.first_name = value2.first_name;
                        tL_inputPhoneContact.last_name = value2.last_name;
                        tL_inputPhoneContact.phone = value2.phones.get(i11);
                        arrayList6.add(tL_inputPhoneContact);
                        i3 = 1;
                        i11 += i3;
                        hashMap17 = hashMap5;
                    }
                }
                contactsController = this;
                hashMap4 = hashMap17;
                i = i10;
            } else {
                contactsController = this;
                hashMap4 = hashMap17;
                i = 0;
            }
            i2 = 0;
        } else {
            Iterator<Map.Entry<String, Contact>> it3 = readContactsFromPhoneBook.entrySet().iterator();
            i2 = 0;
            int i12 = 0;
            while (it3.hasNext()) {
                Map.Entry<String, Contact> next = it3.next();
                String key = next.getKey();
                Contact value3 = next.getValue();
                Contact contact3 = (Contact) hashMap.get(key);
                Iterator<Map.Entry<String, Contact>> it4 = it3;
                if (contact3 == null) {
                    for (int i13 = 0; i13 < value3.shortPhones.size(); i13++) {
                        contact = (Contact) hashMap13.get(value3.shortPhones.get(i13));
                        if (contact != null) {
                            key = contact.key;
                            break;
                        }
                    }
                }
                contact = contact3;
                if (contact != null) {
                    value3.imported = contact.imported;
                }
                if (contact != null) {
                    if (TextUtils.isEmpty(value3.first_name)) {
                        hashMap7 = hashMap13;
                    } else {
                        hashMap7 = hashMap13;
                    }
                    if (!TextUtils.isEmpty(value3.last_name)) {
                    }
                } else {
                    hashMap7 = hashMap13;
                }
                boolean z8 = false;
                if (contact == null || z8) {
                    str = str2;
                    arrayList2 = arrayList4;
                    hashMap8 = hashMap15;
                    hashMap9 = hashMap14;
                    hashMap10 = readContactsFromPhoneBook;
                    int i14 = 0;
                    while (i14 < value3.phones.size()) {
                        String str6 = value3.shortPhones.get(i14);
                        str6.substring(Math.max(0, str6.length() - 7));
                        hashMap16.put(str6, value3);
                        if (contact != null && (indexOf = contact.shortPhones.indexOf(str6)) != -1) {
                            Integer num = contact.phoneDeleted.get(indexOf);
                            value3.phoneDeleted.set(i14, num);
                            i5 = 1;
                        }
                        i5 = 1;
                        if (z2) {
                            if (!z8) {
                                if (contactsController2.contactsByPhone.containsKey(str6)) {
                                    i12++;
                                } else {
                                    i2 += i5;
                                }
                            }
                            TLRPC.TL_inputPhoneContact tL_inputPhoneContact2 = new TLRPC.TL_inputPhoneContact();
                            tL_inputPhoneContact2.client_id = (i14 << 32) | value3.contact_id;
                            tL_inputPhoneContact2.first_name = value3.first_name;
                            tL_inputPhoneContact2.last_name = value3.last_name;
                            tL_inputPhoneContact2.phone = value3.phones.get(i14);
                            arrayList6.add(tL_inputPhoneContact2);
                        }
                        i14++;
                        contactsController2 = this;
                    }
                } else {
                    int i15 = 0;
                    while (i15 < value3.phones.size()) {
                        String str7 = value3.shortPhones.get(i15);
                        String str8 = str2;
                        HashMap hashMap18 = hashMap15;
                        String substring2 = str7.substring(Math.max(0, str7.length() - 7));
                        hashMap16.put(str7, value3);
                        int indexOf2 = contact.shortPhones.indexOf(str7);
                        if (z2) {
                            i6 = indexOf2;
                            TLRPC.TL_contact tL_contact2 = contactsController2.contactsByPhone.get(str7);
                            arrayList3 = arrayList4;
                            if (tL_contact2 != null) {
                                hashMap11 = hashMap14;
                                TLRPC.User user2 = getMessagesController().getUser(Long.valueOf(tL_contact2.user_id));
                                if (user2 != null) {
                                    i12++;
                                    if (TextUtils.isEmpty(user2.first_name) && TextUtils.isEmpty(user2.last_name) && (!TextUtils.isEmpty(value3.first_name) || !TextUtils.isEmpty(value3.last_name))) {
                                        z7 = true;
                                        i7 = -1;
                                        if (i7 != -1) {
                                            if (z2) {
                                                if (!z7) {
                                                    TLRPC.TL_contact tL_contact3 = contactsController2.contactsByPhone.get(str7);
                                                    if (tL_contact3 != null) {
                                                        TLRPC.User user3 = getMessagesController().getUser(Long.valueOf(tL_contact3.user_id));
                                                        if (user3 != null) {
                                                            i12++;
                                                            String str9 = user3.first_name;
                                                            if (str9 == null) {
                                                                str9 = str8;
                                                            }
                                                            String str10 = user3.last_name;
                                                            if (str10 == null) {
                                                                str10 = str8;
                                                            }
                                                            if (str9.equals(value3.first_name)) {
                                                            }
                                                            if (TextUtils.isEmpty(value3.first_name)) {
                                                            }
                                                        } else {
                                                            i2++;
                                                        }
                                                    } else if (contactsController2.contactsByShortPhone.containsKey(substring2)) {
                                                        i12++;
                                                    }
                                                }
                                                TLRPC.TL_inputPhoneContact tL_inputPhoneContact3 = new TLRPC.TL_inputPhoneContact();
                                                hashMap12 = readContactsFromPhoneBook;
                                                tL_inputPhoneContact3.client_id = value3.contact_id | (i15 << 32);
                                                tL_inputPhoneContact3.first_name = value3.first_name;
                                                tL_inputPhoneContact3.last_name = value3.last_name;
                                                tL_inputPhoneContact3.phone = value3.phones.get(i15);
                                                arrayList6.add(tL_inputPhoneContact3);
                                            }
                                            hashMap12 = readContactsFromPhoneBook;
                                        } else {
                                            hashMap12 = readContactsFromPhoneBook;
                                            value3.phoneDeleted.set(i15, contact.phoneDeleted.get(i7));
                                            contact.phones.remove(i7);
                                            contact.shortPhones.remove(i7);
                                            contact.phoneDeleted.remove(i7);
                                            contact.phoneTypes.remove(i7);
                                        }
                                        i15++;
                                        hashMap15 = hashMap18;
                                        str2 = str8;
                                        readContactsFromPhoneBook = hashMap12;
                                        arrayList4 = arrayList3;
                                        hashMap14 = hashMap11;
                                    }
                                }
                            } else {
                                hashMap11 = hashMap14;
                                if (contactsController2.contactsByShortPhone.containsKey(substring2)) {
                                    i12++;
                                }
                            }
                        } else {
                            arrayList3 = arrayList4;
                            i6 = indexOf2;
                            hashMap11 = hashMap14;
                        }
                        i7 = i6;
                        z7 = false;
                        if (i7 != -1) {
                        }
                        i15++;
                        hashMap15 = hashMap18;
                        str2 = str8;
                        readContactsFromPhoneBook = hashMap12;
                        arrayList4 = arrayList3;
                        hashMap14 = hashMap11;
                    }
                    str = str2;
                    arrayList2 = arrayList4;
                    hashMap8 = hashMap15;
                    hashMap9 = hashMap14;
                    hashMap10 = readContactsFromPhoneBook;
                    if (!contact.phones.isEmpty()) {
                        it3 = it4;
                        hashMap13 = hashMap7;
                        hashMap15 = hashMap8;
                        str2 = str;
                        readContactsFromPhoneBook = hashMap10;
                        arrayList4 = arrayList2;
                        hashMap14 = hashMap9;
                    }
                }
                hashMap.remove(key);
                contactsController2 = this;
                it3 = it4;
                hashMap13 = hashMap7;
                hashMap15 = hashMap8;
                str2 = str;
                readContactsFromPhoneBook = hashMap10;
                arrayList4 = arrayList2;
                hashMap14 = hashMap9;
            }
            arrayList = arrayList4;
            hashMap2 = hashMap15;
            hashMap3 = hashMap14;
            HashMap<String, Contact> hashMap19 = readContactsFromPhoneBook;
            if (!z3 && hashMap.isEmpty() && arrayList6.isEmpty() && size2 == hashMap19.size()) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("contacts not changed!");
                    return;
                }
                return;
            }
            if (!z2 || hashMap.isEmpty() || hashMap19.isEmpty() || !arrayList6.isEmpty()) {
                hashMap6 = hashMap19;
            } else {
                hashMap6 = hashMap19;
                getMessagesStorage().putCachedPhoneBook(hashMap6, false, false);
            }
            contactsController = this;
            hashMap4 = hashMap6;
            i = i12;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("done processing contacts");
        }
        if (!z2) {
            final HashMap<String, Contact> hashMap20 = hashMap4;
            final ArrayList arrayList7 = arrayList;
            final HashMap hashMap21 = hashMap3;
            final HashMap hashMap22 = hashMap2;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$performSyncPhoneBook$24(hashMap16, hashMap20, z3, hashMap21, arrayList7, hashMap22);
                }
            });
            if (hashMap20.isEmpty()) {
                return;
            }
            getMessagesStorage().putCachedPhoneBook(hashMap20, false, false);
            return;
        }
        if (arrayList6.isEmpty()) {
            final HashMap<String, Contact> hashMap23 = hashMap4;
            final ArrayList arrayList8 = arrayList;
            final HashMap hashMap24 = hashMap3;
            final HashMap hashMap25 = hashMap2;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$performSyncPhoneBook$22(hashMap16, hashMap23, z3, hashMap24, arrayList8, hashMap25);
                }
            });
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.e("start import contacts");
        }
        if (z5 && i2 != 0) {
            if (i2 >= 30) {
                i4 = 1;
            } else if (z3 && size2 == 0 && contactsController.contactsByPhone.size() - i > (contactsController.contactsByPhone.size() / 3) * 2) {
                i4 = 2;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("new phone book contacts " + i2 + " serverContactsInPhonebook " + i + " totalContacts " + contactsController.contactsByPhone.size());
            }
            if (i4 == 0) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsController.this.lambda$performSyncPhoneBook$14(i4, hashMap, z3, z);
                    }
                });
                return;
            }
            if (z6) {
                final HashMap<String, Contact> hashMap26 = hashMap4;
                final HashMap hashMap27 = hashMap3;
                final ArrayList arrayList9 = arrayList;
                final HashMap hashMap28 = hashMap2;
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsController.this.lambda$performSyncPhoneBook$16(hashMap16, hashMap26, z3, hashMap27, arrayList9, hashMap28);
                    }
                });
                return;
            }
            boolean[] zArr = {false};
            HashMap<String, Contact> hashMap29 = hashMap4;
            final HashMap hashMap30 = new HashMap(hashMap29);
            SparseArray sparseArray = new SparseArray();
            Iterator it5 = hashMap30.entrySet().iterator();
            while (it5.hasNext()) {
                Contact contact4 = (Contact) ((Map.Entry) it5.next()).getValue();
                sparseArray.put(contact4.contact_id, contact4.key);
            }
            contactsController.completedRequestsCount = 0;
            double size3 = arrayList6.size();
            Double.isNaN(size3);
            int ceil = (int) Math.ceil(size3 / 500.0d);
            int i16 = 0;
            while (i16 < ceil) {
                final TLRPC.TL_contacts_importContacts tL_contacts_importContacts = new TLRPC.TL_contacts_importContacts();
                int i17 = i16 * 500;
                tL_contacts_importContacts.contacts = new ArrayList<>(arrayList6.subList(i17, Math.min(i17 + 500, arrayList6.size())));
                final SparseArray sparseArray2 = sparseArray;
                final boolean[] zArr2 = zArr;
                final HashMap<String, Contact> hashMap31 = hashMap29;
                ArrayList arrayList10 = arrayList6;
                final int i18 = ceil;
                int i19 = i16;
                final HashMap hashMap32 = hashMap16;
                int i20 = ceil;
                final ArrayList arrayList11 = arrayList;
                final HashMap hashMap33 = hashMap3;
                HashMap<String, Contact> hashMap34 = hashMap29;
                final HashMap hashMap35 = hashMap2;
                getConnectionsManager().sendRequest(tL_contacts_importContacts, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda10
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        ContactsController.this.lambda$performSyncPhoneBook$20(hashMap30, sparseArray2, zArr2, hashMap31, tL_contacts_importContacts, i18, hashMap32, z3, hashMap33, arrayList11, hashMap35, tLObject, tL_error);
                    }
                }, 6);
                i16 = i19 + 1;
                hashMap29 = hashMap34;
                zArr = zArr;
                arrayList6 = arrayList10;
                hashMap16 = hashMap16;
                ceil = i20;
                sparseArray = sparseArray;
                arrayList = arrayList11;
            }
            return;
        }
        i4 = 0;
        if (BuildVars.LOGS_ENABLED) {
        }
        if (i4 == 0) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedContacts$29() {
        this.doneLoadingContacts = true;
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$processLoadedContacts$30(LongSparseArray longSparseArray, Collator collator, TLRPC.TL_contact tL_contact, TLRPC.TL_contact tL_contact2) {
        return collator.compare(UserObject.getFirstName((TLRPC.User) longSparseArray.get(tL_contact.user_id)), UserObject.getFirstName((TLRPC.User) longSparseArray.get(tL_contact2.user_id)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$processLoadedContacts$31(Collator collator, String str, String str2) {
        char charAt = str.charAt(0);
        char charAt2 = str2.charAt(0);
        if (charAt == '#') {
            return 1;
        }
        if (charAt2 == '#') {
            return -1;
        }
        return collator.compare(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$processLoadedContacts$32(Collator collator, String str, String str2) {
        char charAt = str.charAt(0);
        char charAt2 = str2.charAt(0);
        if (charAt == '#') {
            return 1;
        }
        if (charAt2 == '#') {
            return -1;
        }
        return collator.compare(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedContacts$33(ArrayList arrayList, ConcurrentHashMap concurrentHashMap, HashMap hashMap, HashMap hashMap2, ArrayList arrayList2, ArrayList arrayList3, int i, boolean z, boolean z2) {
        this.contacts = arrayList;
        this.contactsDict = concurrentHashMap;
        this.usersSectionsDict = hashMap;
        this.usersMutualSectionsDict = hashMap2;
        this.sortedUsersSectionsArray = arrayList2;
        this.sortedUsersMutualSectionsArray = arrayList3;
        this.doneLoadingContacts = true;
        if (i != 2) {
            synchronized (this.loadContactsSync) {
                this.loadingContacts = false;
            }
        }
        performWriteContactsToPhoneBook();
        updateUnregisteredContacts();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
        if (i == 1 || z) {
            reloadContactsStatusesMaybe(false);
        } else {
            saveContactsLoadTime();
        }
        if (z2) {
            loadContacts(false, 0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedContacts$34(HashMap hashMap, HashMap hashMap2) {
        this.contactsByPhone = hashMap;
        this.contactsByShortPhone = hashMap2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedContacts$35(final HashMap hashMap, final HashMap hashMap2) {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda58
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$processLoadedContacts$34(hashMap, hashMap2);
            }
        });
        if (this.contactsSyncInProgress) {
            return;
        }
        this.contactsSyncInProgress = true;
        getMessagesStorage().getCachedPhoneBook(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedContacts$36(final int i, final ArrayList arrayList, ArrayList arrayList2, LongSparseArray longSparseArray, final boolean z) {
        HashMap hashMap;
        final HashMap hashMap2;
        int i2;
        int i3;
        HashMap hashMap3;
        ArrayList arrayList3;
        ArrayList arrayList4 = arrayList;
        final LongSparseArray longSparseArray2 = longSparseArray;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("done loading contacts");
        }
        if (i == 1 && (arrayList.isEmpty() || Math.abs((System.currentTimeMillis() / 1000) - getUserConfig().lastContactsSyncTime) >= 86400)) {
            loadContacts(false, getContactsHash(arrayList4));
            if (arrayList.isEmpty()) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda40
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsController.this.lambda$processLoadedContacts$29();
                    }
                });
                return;
            }
        }
        if (i == 0) {
            getUserConfig().lastContactsSyncTime = (int) (System.currentTimeMillis() / 1000);
            getUserConfig().saveConfig(false);
        }
        int i4 = 0;
        final boolean z2 = false;
        while (i4 < arrayList.size()) {
            TLRPC.TL_contact tL_contact = arrayList4.get(i4);
            if (MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tL_contact.user_id)) == null && tL_contact.user_id != getUserConfig().getClientUserId()) {
                arrayList4.remove(i4);
                i4--;
                z2 = true;
            }
            i4++;
        }
        if (i != 1) {
            getMessagesStorage().putUsersAndChats(arrayList2, null, true, true);
            getMessagesStorage().putContacts(arrayList4, i != 2);
        }
        final Collator localeCollator = getLocaleCollator();
        Collections.sort(arrayList4, new Comparator() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda41
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$processLoadedContacts$30;
                lambda$processLoadedContacts$30 = ContactsController.lambda$processLoadedContacts$30(LongSparseArray.this, localeCollator, (TLRPC.TL_contact) obj, (TLRPC.TL_contact) obj2);
                return lambda$processLoadedContacts$30;
            }
        });
        final ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(20, 1.0f, 2);
        final HashMap hashMap4 = new HashMap();
        final HashMap hashMap5 = new HashMap();
        final ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        if (this.contactsBookLoaded) {
            hashMap = null;
            hashMap2 = null;
        } else {
            hashMap2 = new HashMap();
            hashMap = new HashMap();
        }
        int i5 = 0;
        while (i5 < arrayList.size()) {
            TLRPC.TL_contact tL_contact2 = arrayList4.get(i5);
            TLRPC.User user = (TLRPC.User) longSparseArray2.get(tL_contact2.user_id);
            if (user == null) {
                hashMap3 = hashMap;
                i2 = i5;
                arrayList3 = arrayList6;
            } else {
                i2 = i5;
                ArrayList arrayList7 = arrayList6;
                concurrentHashMap.put(Long.valueOf(tL_contact2.user_id), tL_contact2);
                if (hashMap2 == null || TextUtils.isEmpty(user.phone)) {
                    i3 = 0;
                } else {
                    hashMap2.put(user.phone, tL_contact2);
                    i3 = 0;
                    hashMap.put(user.phone.substring(Math.max(0, r13.length() - 7)), tL_contact2);
                }
                String firstName = UserObject.getFirstName(user);
                hashMap3 = hashMap;
                if (firstName.length() > 1) {
                    firstName = firstName.substring(i3, 1);
                }
                String upperCase = firstName.length() == 0 ? "#" : firstName.toUpperCase();
                String str = this.sectionsToReplace.get(upperCase);
                if (str != null) {
                    upperCase = str;
                }
                ArrayList arrayList8 = (ArrayList) hashMap4.get(upperCase);
                if (arrayList8 == null) {
                    arrayList8 = new ArrayList();
                    hashMap4.put(upperCase, arrayList8);
                    arrayList5.add(upperCase);
                }
                arrayList8.add(tL_contact2);
                if (user.mutual_contact) {
                    ArrayList arrayList9 = (ArrayList) hashMap5.get(upperCase);
                    if (arrayList9 == null) {
                        arrayList9 = new ArrayList();
                        hashMap5.put(upperCase, arrayList9);
                        arrayList3 = arrayList7;
                        arrayList3.add(upperCase);
                    } else {
                        arrayList3 = arrayList7;
                    }
                    arrayList9.add(tL_contact2);
                } else {
                    arrayList3 = arrayList7;
                }
            }
            int i6 = i2 + 1;
            arrayList4 = arrayList;
            longSparseArray2 = longSparseArray;
            arrayList6 = arrayList3;
            hashMap = hashMap3;
            i5 = i6;
        }
        final HashMap hashMap6 = hashMap;
        final ArrayList arrayList10 = arrayList6;
        Collections.sort(arrayList5, new Comparator() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda42
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$processLoadedContacts$31;
                lambda$processLoadedContacts$31 = ContactsController.lambda$processLoadedContacts$31(localeCollator, (String) obj, (String) obj2);
                return lambda$processLoadedContacts$31;
            }
        });
        Collections.sort(arrayList10, new Comparator() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda43
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$processLoadedContacts$32;
                lambda$processLoadedContacts$32 = ContactsController.lambda$processLoadedContacts$32(localeCollator, (String) obj, (String) obj2);
                return lambda$processLoadedContacts$32;
            }
        });
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$processLoadedContacts$33(arrayList, concurrentHashMap, hashMap4, hashMap5, arrayList5, arrayList10, i, z, z2);
            }
        });
        if (!this.delayedContactsUpdate.isEmpty() && this.contactsLoaded && this.contactsBookLoaded) {
            applyContactsUpdates(this.delayedContactsUpdate, null, null, null);
            this.delayedContactsUpdate.clear();
        }
        if (hashMap2 != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$processLoadedContacts$35(hashMap2, hashMap6);
                }
            });
        } else {
            this.contactsLoaded = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedContacts$37(final ArrayList arrayList, final int i, final ArrayList arrayList2) {
        getMessagesController().putUsers(arrayList, i == 1);
        final LongSparseArray longSparseArray = new LongSparseArray();
        final boolean isEmpty = arrayList2.isEmpty();
        if (i == 2 && !this.contacts.isEmpty()) {
            int i2 = 0;
            while (i2 < arrayList2.size()) {
                if (this.contactsDict.get(Long.valueOf(((TLRPC.TL_contact) arrayList2.get(i2)).user_id)) != null) {
                    arrayList2.remove(i2);
                    i2--;
                }
                i2++;
            }
            arrayList2.addAll(this.contacts);
        }
        for (int i3 = 0; i3 < arrayList2.size(); i3++) {
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(((TLRPC.TL_contact) arrayList2.get(i3)).user_id));
            if (user != null) {
                longSparseArray.put(user.id, user);
            }
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$processLoadedContacts$36(i, arrayList2, arrayList, longSparseArray, isEmpty);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$readContacts$11() {
        if (this.contacts.isEmpty() && !this.contactsLoaded) {
            loadContacts(true, 0L);
            return;
        }
        synchronized (this.loadContactsSync) {
            this.loadingContacts = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0070  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$reloadContactsStatuses$58(SharedPreferences.Editor editor, TLObject tLObject) {
        int i;
        TLRPC.User user;
        editor.remove("needGetStatuses").commit();
        Vector vector = (Vector) tLObject;
        if (!vector.objects.isEmpty()) {
            ArrayList<TLRPC.User> arrayList = new ArrayList<>();
            Iterator it = vector.objects.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                TLRPC.TL_user tL_user = new TLRPC.TL_user();
                TLRPC.TL_contactStatus tL_contactStatus = (TLRPC.TL_contactStatus) next;
                if (tL_contactStatus != null) {
                    TLRPC.UserStatus userStatus = tL_contactStatus.status;
                    if (userStatus instanceof TLRPC.TL_userStatusRecently) {
                        i = userStatus.by_me ? -1000 : -100;
                    } else if (userStatus instanceof TLRPC.TL_userStatusLastWeek) {
                        i = userStatus.by_me ? -1001 : -101;
                    } else {
                        if (userStatus instanceof TLRPC.TL_userStatusLastMonth) {
                            i = userStatus.by_me ? -1002 : -102;
                        }
                        user = getMessagesController().getUser(Long.valueOf(tL_contactStatus.user_id));
                        if (user != null) {
                            user.status = tL_contactStatus.status;
                        }
                        tL_user.status = tL_contactStatus.status;
                        arrayList.add(tL_user);
                    }
                    userStatus.expires = i;
                    user = getMessagesController().getUser(Long.valueOf(tL_contactStatus.user_id));
                    if (user != null) {
                    }
                    tL_user.status = tL_contactStatus.status;
                    arrayList.add(tL_user);
                }
            }
            getMessagesStorage().updateUsers(arrayList, true, true, true);
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_STATUS));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadContactsStatuses$59(final SharedPreferences.Editor editor, final TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof Vector) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$reloadContactsStatuses$58(editor, tLObject);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$resetImportedContacts$10(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$syncPhoneBookByAlert$7(HashMap hashMap, boolean z, boolean z2, boolean z3) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("sync contacts by alert");
        }
        performSyncPhoneBook(hashMap, true, z, z2, false, false, z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$updateUnregisteredContacts$42(Collator collator, Contact contact, Contact contact2) {
        String str = contact.first_name;
        if (str.length() == 0) {
            str = contact.last_name;
        }
        String str2 = contact2.first_name;
        if (str2.length() == 0) {
            str2 = contact2.last_name;
        }
        return collator.compare(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: mergePhonebookAndTelegramContacts, reason: merged with bridge method [inline-methods] */
    public void lambda$performSyncPhoneBook$23(HashMap<String, ArrayList<Object>> hashMap, ArrayList<String> arrayList, HashMap<String, Contact> hashMap2) {
        mergePhonebookAndTelegramContacts(hashMap, arrayList, hashMap2, true);
    }

    private void mergePhonebookAndTelegramContacts(final HashMap<String, ArrayList<Object>> hashMap, final ArrayList<String> arrayList, final HashMap<String, Contact> hashMap2, final boolean z) {
        final ArrayList arrayList2 = new ArrayList(this.contacts);
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$mergePhonebookAndTelegramContacts$41(z, arrayList2, hashMap2, hashMap, arrayList);
            }
        });
    }

    private void performWriteContactsToPhoneBook() {
        final ArrayList arrayList = new ArrayList(this.contacts);
        Utilities.phoneBookQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$performWriteContactsToPhoneBook$45(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0102, code lost:
    
        if (r3 != null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x010e, code lost:
    
        org.telegram.messenger.FileLog.d("performWriteContactsToPhoneBookInternal " + (java.lang.System.currentTimeMillis() - r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0127, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x010b, code lost:
    
        r3.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0109, code lost:
    
        if (r3 == null) goto L53;
     */
    /* renamed from: performWriteContactsToPhoneBookInternal, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void lambda$performWriteContactsToPhoneBook$45(ArrayList<TLRPC.TL_contact> arrayList) {
        long currentTimeMillis = System.currentTimeMillis();
        Cursor cursor = null;
        try {
            try {
                Account account = this.systemAccount;
                if (hasContactsPermission() && account != null && hasContactsWritePermission()) {
                    SharedPreferences mainSettings = MessagesController.getMainSettings(this.currentAccount);
                    boolean z = !mainSettings.getBoolean("contacts_updated_v7", false);
                    if (z) {
                        mainSettings.edit().putBoolean("contacts_updated_v7", true).commit();
                    }
                    ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                    Cursor query = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, new String[]{"_id", "sync2"}, null, null, null);
                    try {
                        LongSparseArray longSparseArray = new LongSparseArray();
                        if (query != null) {
                            while (query.moveToNext()) {
                                longSparseArray.put(query.getLong(1), Long.valueOf(query.getLong(0)));
                            }
                            query.close();
                            FileLog.d("performWriteContactsToPhoneBookInternal contacts array " + arrayList.size() + " " + z + " bookContactsSize=" + longSparseArray.size() + " currentAccount=" + this.currentAccount);
                            ArrayList<ContentProviderOperation> arrayList2 = null;
                            for (int i = 0; i < arrayList.size(); i++) {
                                TLRPC.TL_contact tL_contact = arrayList.get(i);
                                if (z || longSparseArray.indexOfKey(tL_contact.user_id) < 0) {
                                    if (arrayList2 == null) {
                                        arrayList2 = new ArrayList<>();
                                    }
                                    applyContactToPhoneBook(arrayList2, getMessagesController().getUser(Long.valueOf(tL_contact.user_id)));
                                    if (arrayList2.size() > 450) {
                                        contentResolver.applyBatch("com.android.contacts", arrayList2);
                                        arrayList2.clear();
                                    }
                                }
                            }
                            if (arrayList2 != null && !arrayList2.isEmpty()) {
                                contentResolver.applyBatch("com.android.contacts", arrayList2);
                                arrayList2.clear();
                            }
                        } else {
                            cursor = query;
                        }
                    } catch (Exception e) {
                        e = e;
                        cursor = query;
                        FileLog.e(e);
                    } catch (Throwable th) {
                        th = th;
                        cursor = query;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private void reloadContactsStatuses() {
        saveContactsLoadTime();
        getMessagesController().clearFullUsers();
        final SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
        edit.putBoolean("needGetStatuses", true).commit();
        getConnectionsManager().sendRequest(new TLRPC.TL_contacts_getStatuses(), new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda59
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ContactsController.this.lambda$reloadContactsStatuses$59(edit, tLObject, tL_error);
            }
        });
    }

    private void saveContactsLoadTime() {
        try {
            MessagesController.getMainSettings(this.currentAccount).edit().putLong("lastReloadStatusTime", System.currentTimeMillis()).commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void updateUnregisteredContacts() {
        HashMap hashMap = new HashMap();
        int size = this.contacts.size();
        for (int i = 0; i < size; i++) {
            TLRPC.TL_contact tL_contact = this.contacts.get(i);
            TLRPC.User user = getMessagesController().getUser(Long.valueOf(tL_contact.user_id));
            if (user != null && !TextUtils.isEmpty(user.phone)) {
                hashMap.put(user.phone, tL_contact);
            }
        }
        ArrayList<Contact> arrayList = new ArrayList<>();
        Iterator<Map.Entry<String, Contact>> it = this.contactsBook.entrySet().iterator();
        while (it.hasNext()) {
            Contact value = it.next().getValue();
            int i2 = 0;
            while (true) {
                if (i2 >= value.phones.size()) {
                    arrayList.add(value);
                    break;
                } else if (!hashMap.containsKey(value.shortPhones.get(i2)) && value.phoneDeleted.get(i2).intValue() != 1) {
                    i2++;
                }
            }
        }
        final Collator localeCollator = getLocaleCollator();
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda52
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$updateUnregisteredContacts$42;
                lambda$updateUnregisteredContacts$42 = ContactsController.lambda$updateUnregisteredContacts$42(localeCollator, (ContactsController.Contact) obj, (ContactsController.Contact) obj2);
                return lambda$updateUnregisteredContacts$42;
            }
        });
        this.phoneBookContacts = arrayList;
    }

    public void addContact(final TLRPC.User user, boolean z) {
        String str;
        if (user == null) {
            return;
        }
        TLRPC.TL_contacts_addContact tL_contacts_addContact = new TLRPC.TL_contacts_addContact();
        tL_contacts_addContact.id = getMessagesController().getInputUser(user);
        tL_contacts_addContact.first_name = user.first_name;
        tL_contacts_addContact.last_name = user.last_name;
        String str2 = user.phone;
        tL_contacts_addContact.phone = str2;
        tL_contacts_addContact.add_phone_privacy_exception = z;
        if (str2 != null) {
            if (str2.length() > 0 && !tL_contacts_addContact.phone.startsWith("+")) {
                str = "+" + tL_contacts_addContact.phone;
            }
            getConnectionsManager().sendRequest(tL_contacts_addContact, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda53
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ContactsController.this.lambda$addContact$52(user, tLObject, tL_error);
                }
            }, 6);
        }
        str = "";
        tL_contacts_addContact.phone = str;
        getConnectionsManager().sendRequest(tL_contacts_addContact, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda53
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ContactsController.this.lambda$addContact$52(user, tLObject, tL_error);
            }
        }, 6);
    }

    public long addContactToPhoneBook(TLRPC.User user, boolean z) {
        Uri uri;
        long j = -1;
        if (this.systemAccount == null || user == null || !hasContactsWritePermission()) {
            return -1L;
        }
        synchronized (this.observerLock) {
            this.ignoreChanges = true;
        }
        ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
        if (z) {
            try {
                contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").appendQueryParameter("account_name", this.systemAccount.name).appendQueryParameter("account_type", this.systemAccount.type).build(), "sync2 = " + user.id, null);
            } catch (Exception unused) {
            }
        }
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        applyContactToPhoneBook(arrayList, user);
        try {
            ContentProviderResult[] applyBatch = contentResolver.applyBatch("com.android.contacts", arrayList);
            if (applyBatch != null && applyBatch.length > 0 && (uri = applyBatch[0].uri) != null) {
                j = Long.parseLong(uri.getLastPathSegment());
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        synchronized (this.observerLock) {
            this.ignoreChanges = false;
        }
        return j;
    }

    public void checkAppAccount() {
        this.systemAccount = null;
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$checkAppAccount$4();
            }
        });
    }

    public void checkContacts() {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$checkContacts$5();
            }
        });
    }

    public void checkInviteText() {
        SharedPreferences mainSettings = MessagesController.getMainSettings(this.currentAccount);
        this.inviteLink = mainSettings.getString("invitelink", null);
        int i = mainSettings.getInt("invitelinktime", 0);
        if (this.updatingInviteLink) {
            return;
        }
        if (this.inviteLink == null || Math.abs((System.currentTimeMillis() / 1000) - i) >= 86400) {
            this.updatingInviteLink = true;
            getConnectionsManager().sendRequest(new TLRPC.TL_help_getInviteText(), new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda17
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ContactsController.this.lambda$checkInviteText$3(tLObject, tL_error);
                }
            }, 2);
        }
    }

    public void cleanup() {
        this.contactsBook.clear();
        this.contactsBookSPhones.clear();
        this.phoneBookContacts.clear();
        this.contacts.clear();
        this.contactsDict.clear();
        this.usersSectionsDict.clear();
        this.usersMutualSectionsDict.clear();
        this.sortedUsersSectionsArray.clear();
        this.sortedUsersMutualSectionsArray.clear();
        this.delayedContactsUpdate.clear();
        this.contactsByPhone.clear();
        this.contactsByShortPhone.clear();
        this.phoneBookSectionsDict.clear();
        this.phoneBookSectionsArray.clear();
        this.phoneBookByShortPhones.clear();
        this.loadingContacts = false;
        this.contactsSyncInProgress = false;
        this.doneLoadingContacts = false;
        this.contactsLoaded = false;
        this.contactsBookLoaded = false;
        this.lastContactsVersions = "";
        this.loadingGlobalSettings = 0;
        this.loadingDeleteInfo = 0;
        this.deleteAccountTTL = 0;
        Arrays.fill(this.loadingPrivacyInfo, 0);
        this.lastseenPrivacyRules = null;
        this.groupPrivacyRules = null;
        this.callPrivacyRules = null;
        this.p2pPrivacyRules = null;
        this.profilePhotoPrivacyRules = null;
        this.bioPrivacyRules = null;
        this.birthdayPrivacyRules = null;
        this.giftsPrivacyRules = null;
        this.forwardsPrivacyRules = null;
        this.phonePrivacyRules = null;
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$cleanup$1();
            }
        });
    }

    public void createOrUpdateConnectionServiceContact(long j, String str, String str2) {
        String str3;
        int parseInt;
        Cursor cursor;
        ArrayList<ContentProviderOperation> arrayList;
        ContentProviderOperation.Builder withValue;
        if (hasContactsPermission()) {
            try {
                ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                ArrayList<ContentProviderOperation> arrayList2 = new ArrayList<>();
                Uri build = ContactsContract.Groups.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build();
                Uri build2 = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build();
                Account account = this.systemAccount;
                Cursor query = contentResolver.query(build, new String[]{"_id"}, "title=? AND account_type=? AND account_name=?", new String[]{"TelegramConnectionService", account.type, account.name}, null);
                if (query == null || !query.moveToFirst()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("account_type", this.systemAccount.type);
                    contentValues.put("account_name", this.systemAccount.name);
                    str3 = "account_type";
                    contentValues.put("group_visible", (Integer) 0);
                    contentValues.put("group_is_read_only", (Integer) 1);
                    contentValues.put("title", "TelegramConnectionService");
                    parseInt = Integer.parseInt(contentResolver.insert(build, contentValues).getLastPathSegment());
                } else {
                    parseInt = query.getInt(0);
                    str3 = "account_type";
                }
                if (query != null) {
                    query.close();
                }
                Uri uri = ContactsContract.Data.CONTENT_URI;
                String str4 = str3;
                Cursor query2 = contentResolver.query(uri, new String[]{"raw_contact_id"}, "mimetype=? AND data1=?", new String[]{"vnd.android.cursor.item/group_membership", parseInt + ""}, null);
                int size = arrayList2.size();
                int i = parseInt;
                if (query2 == null || !query2.moveToFirst()) {
                    cursor = query2;
                    arrayList = arrayList2;
                    arrayList.add(ContentProviderOperation.newInsert(build2).withValue(str4, this.systemAccount.type).withValue("account_name", this.systemAccount.name).withValue("raw_contact_is_read_only", 1).withValue("aggregation_mode", 3).build());
                    arrayList.add(ContentProviderOperation.newInsert(uri).withValueBackReference("raw_contact_id", size).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data2", str).withValue("data3", str2).build());
                    arrayList.add(ContentProviderOperation.newInsert(uri).withValueBackReference("raw_contact_id", size).withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data1", "+99084" + j).build());
                    withValue = ContentProviderOperation.newInsert(uri).withValueBackReference("raw_contact_id", size).withValue("mimetype", "vnd.android.cursor.item/group_membership").withValue("data1", Integer.valueOf(i));
                } else {
                    int i2 = query2.getInt(0);
                    cursor = query2;
                    arrayList = arrayList2;
                    arrayList.add(ContentProviderOperation.newUpdate(build2).withSelection("_id=?", new String[]{i2 + ""}).withValue("deleted", 0).build());
                    arrayList.add(ContentProviderOperation.newUpdate(uri).withSelection("raw_contact_id=? AND mimetype=?", new String[]{i2 + "", "vnd.android.cursor.item/phone_v2"}).withValue("data1", "+99084" + j).build());
                    withValue = ContentProviderOperation.newUpdate(uri).withSelection("raw_contact_id=? AND mimetype=?", new String[]{i2 + "", "vnd.android.cursor.item/name"}).withValue("data2", str).withValue("data3", str2);
                }
                arrayList.add(withValue.build());
                if (cursor != null) {
                    cursor.close();
                }
                contentResolver.applyBatch("com.android.contacts", arrayList);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public void deleteAllContacts(final Runnable runnable) {
        resetImportedContacts();
        TLRPC.TL_contacts_deleteContacts tL_contacts_deleteContacts = new TLRPC.TL_contacts_deleteContacts();
        int size = this.contacts.size();
        for (int i = 0; i < size; i++) {
            tL_contacts_deleteContacts.id.add(getMessagesController().getInputUser(this.contacts.get(i).user_id));
        }
        getConnectionsManager().sendRequest(tL_contacts_deleteContacts, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda6
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ContactsController.this.lambda$deleteAllContacts$9(runnable, tLObject, tL_error);
            }
        });
    }

    public void deleteConnectionServiceContact() {
        if (hasContactsPermission()) {
            try {
                ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                Account account = this.systemAccount;
                Cursor query = contentResolver.query(ContactsContract.Groups.CONTENT_URI, new String[]{"_id"}, "title=? AND account_type=? AND account_name=?", new String[]{"TelegramConnectionService", account.type, account.name}, null);
                if (query == null || !query.moveToFirst()) {
                    if (query != null) {
                        query.close();
                        return;
                    }
                    return;
                }
                int i = query.getInt(0);
                query.close();
                Cursor query2 = contentResolver.query(ContactsContract.Data.CONTENT_URI, new String[]{"raw_contact_id"}, "mimetype=? AND data1=?", new String[]{"vnd.android.cursor.item/group_membership", i + ""}, null);
                if (query2 == null || !query2.moveToFirst()) {
                    if (query2 != null) {
                        query2.close();
                        return;
                    }
                    return;
                }
                int i2 = query2.getInt(0);
                query2.close();
                contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, "_id=?", new String[]{i2 + ""});
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public void deleteContact(final ArrayList<TLRPC.User> arrayList, final boolean z) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        TLRPC.TL_contacts_deleteContacts tL_contacts_deleteContacts = new TLRPC.TL_contacts_deleteContacts();
        final ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC.User user = arrayList.get(i);
            getMessagesController().getStoriesController().removeContact(user.id);
            TLRPC.InputUser inputUser = getMessagesController().getInputUser(user);
            if (inputUser != null) {
                user.contact = false;
                arrayList2.add(Long.valueOf(user.id));
                tL_contacts_deleteContacts.id.add(inputUser);
            }
        }
        final String str = arrayList.get(0).first_name;
        getConnectionsManager().sendRequest(tL_contacts_deleteContacts, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda36
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ContactsController.this.lambda$deleteContact$57(arrayList2, arrayList, z, str, tLObject, tL_error);
            }
        });
    }

    public void deleteContactsUndoable(Context context, BaseFragment baseFragment, final ArrayList<TLRPC.User> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        final HashMap hashMap = new HashMap();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC.User user = arrayList.get(i);
            TLRPC.TL_contact tL_contact = this.contactsDict.get(Long.valueOf(user.id));
            user.contact = false;
            this.contacts.remove(tL_contact);
            this.contactsDict.remove(Long.valueOf(user.id));
            hashMap.put(user, tL_contact);
        }
        buildContactsSectionsArrays(false);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_NAME));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.contactsDidLoad, new Object[0]);
        Bulletin.SimpleLayout simpleLayout = new Bulletin.SimpleLayout(context, baseFragment.getResourceProvider());
        simpleLayout.setTimer();
        simpleLayout.textView.setText(LocaleController.formatPluralString("ContactsDeletedUndo", hashMap.size(), new Object[0]));
        Bulletin.UndoButton undoButton = new Bulletin.UndoButton(context, true, true, baseFragment.getResourceProvider());
        undoButton.setUndoAction(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda63
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$deleteContactsUndoable$53(hashMap);
            }
        });
        undoButton.setDelayedAction(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$deleteContactsUndoable$54(arrayList);
            }
        });
        simpleLayout.setButton(undoButton);
        Bulletin.make(baseFragment, simpleLayout, 5000).show();
    }

    public void deleteUnknownAppAccounts() {
        try {
            this.systemAccount = null;
            AccountManager accountManager = AccountManager.get(ApplicationLoader.applicationContext);
            Account[] accountsByType = accountManager.getAccountsByType(BuildConfig.LIBRARY_PACKAGE_NAME);
            for (int i = 0; i < accountsByType.length; i++) {
                Account account = accountsByType[i];
                int i2 = 0;
                while (true) {
                    if (i2 < 4) {
                        TLRPC.User currentUser = UserConfig.getInstance(i2).getCurrentUser();
                        if (currentUser != null) {
                            if (account.name.equals("" + currentUser.id)) {
                                break;
                            }
                        }
                        i2++;
                    } else {
                        try {
                            accountManager.removeAccount(accountsByType[i], null, null);
                            break;
                        } catch (Exception unused) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forceImportContacts() {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$forceImportContacts$6();
            }
        });
    }

    public HashMap<String, Contact> getContactsCopy(HashMap<String, Contact> hashMap) {
        HashMap<String, Contact> hashMap2 = new HashMap<>();
        for (Map.Entry<String, Contact> entry : hashMap.entrySet()) {
            Contact contact = new Contact();
            Contact value = entry.getValue();
            contact.phoneDeleted.addAll(value.phoneDeleted);
            contact.phones.addAll(value.phones);
            contact.phoneTypes.addAll(value.phoneTypes);
            contact.shortPhones.addAll(value.shortPhones);
            contact.first_name = value.first_name;
            contact.last_name = value.last_name;
            contact.contact_id = value.contact_id;
            String str = value.key;
            contact.key = str;
            hashMap2.put(str, contact);
        }
        return hashMap2;
    }

    public int getDeleteAccountTTL() {
        return this.deleteAccountTTL;
    }

    public TLRPC.TL_globalPrivacySettings getGlobalPrivacySettings() {
        return this.globalPrivacySettings;
    }

    public String getInviteText(int i) {
        String str = this.inviteLink;
        if (str == null) {
            str = "https://telegram.org/dl";
        }
        if (i <= 1) {
            return LocaleController.formatString("InviteText2", R.string.InviteText2, str);
        }
        try {
            return String.format(LocaleController.getPluralString("InviteTextNum", i), Integer.valueOf(i), str);
        } catch (Exception unused) {
            return LocaleController.formatString("InviteText2", R.string.InviteText2, str);
        }
    }

    public boolean getLoadingDeleteInfo() {
        return this.loadingDeleteInfo != 2;
    }

    public boolean getLoadingGlobalSettings() {
        return this.loadingGlobalSettings != 2;
    }

    public boolean getLoadingPrivacyInfo(int i) {
        return this.loadingPrivacyInfo[i] != 2;
    }

    public ArrayList<TLRPC.PrivacyRule> getPrivacyRules(int i) {
        switch (i) {
            case 0:
                return this.lastseenPrivacyRules;
            case 1:
                return this.groupPrivacyRules;
            case 2:
                return this.callPrivacyRules;
            case 3:
                return this.p2pPrivacyRules;
            case 4:
                return this.profilePhotoPrivacyRules;
            case 5:
                return this.forwardsPrivacyRules;
            case 6:
                return this.phonePrivacyRules;
            case 7:
                return this.addedByPhonePrivacyRules;
            case 8:
                return this.voiceMessagesRules;
            case 9:
                return this.bioPrivacyRules;
            case 10:
            default:
                return null;
            case 11:
                return this.birthdayPrivacyRules;
            case 12:
                return this.giftsPrivacyRules;
        }
    }

    public boolean isContact(long j) {
        return this.contactsDict.get(Long.valueOf(j)) != null;
    }

    public boolean isLoadingContacts() {
        boolean z;
        synchronized (this.loadContactsSync) {
            z = this.loadingContacts;
        }
        return z;
    }

    public void loadContacts(boolean z, final long j) {
        synchronized (this.loadContactsSync) {
            this.loadingContacts = true;
        }
        if (z) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("load contacts from cache");
            }
            getMessagesStorage().getContacts();
        } else {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("load contacts from server");
            }
            TLRPC.TL_contacts_getContacts tL_contacts_getContacts = new TLRPC.TL_contacts_getContacts();
            tL_contacts_getContacts.hash = j;
            getConnectionsManager().sendRequest(tL_contacts_getContacts, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda23
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ContactsController.this.lambda$loadContacts$28(j, tLObject, tL_error);
                }
            });
        }
    }

    public void loadGlobalPrivacySetting() {
        if (this.loadingGlobalSettings == 0) {
            this.loadingGlobalSettings = 1;
            getConnectionsManager().sendRequest(new TL_account.getGlobalPrivacySettings(), new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda29
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ContactsController.this.lambda$loadGlobalPrivacySetting$61(tLObject, tL_error);
                }
            });
        }
    }

    public void loadPrivacySettings() {
        TLRPC.InputPrivacyKey tL_inputPrivacyKeyStatusTimestamp;
        if (this.loadingDeleteInfo == 0) {
            this.loadingDeleteInfo = 1;
            getConnectionsManager().sendRequest(new TL_account.getAccountTTL(), new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda13
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ContactsController.this.lambda$loadPrivacySettings$63(tLObject, tL_error);
                }
            });
        }
        loadGlobalPrivacySetting();
        final int i = 0;
        while (true) {
            int[] iArr = this.loadingPrivacyInfo;
            if (i >= iArr.length) {
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.privacyRulesUpdated, new Object[0]);
                return;
            }
            if (iArr[i] == 0) {
                iArr[i] = 1;
                TL_account.getPrivacy getprivacy = new TL_account.getPrivacy();
                switch (i) {
                    case 0:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyStatusTimestamp();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 1:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyChatInvite();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 2:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyPhoneCall();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 3:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyPhoneP2P();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 4:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyProfilePhoto();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 5:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyForwards();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 6:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyPhoneNumber();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 7:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyAddedByPhone();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 8:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyVoiceMessages();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 9:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyAbout();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 11:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyBirthday();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                    case 12:
                        tL_inputPrivacyKeyStatusTimestamp = new TLRPC.TL_inputPrivacyKeyStarGiftsAutoSave();
                        getprivacy.key = tL_inputPrivacyKeyStatusTimestamp;
                        getConnectionsManager().sendRequest(getprivacy, new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda14
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                ContactsController.this.lambda$loadPrivacySettings$65(i, tLObject, tL_error);
                            }
                        });
                        break;
                }
            }
            i++;
        }
    }

    protected void markAsContacted(final String str) {
        if (str == null) {
            return;
        }
        Utilities.phoneBookQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda60
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.lambda$markAsContacted$49(str);
            }
        });
    }

    protected void migratePhoneBookToV7(final SparseArray<Contact> sparseArray) {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$migratePhoneBookToV7$12(sparseArray);
            }
        });
    }

    protected void performSyncPhoneBook(final HashMap<String, Contact> hashMap, final boolean z, final boolean z2, final boolean z3, final boolean z4, final boolean z5, final boolean z6) {
        if (z2 || this.contactsBookLoaded) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsController.this.lambda$performSyncPhoneBook$25(hashMap, z3, z, z2, z4, z5, z6);
                }
            });
        }
    }

    public void processContactsUpdates(ArrayList<Long> arrayList, ConcurrentHashMap<Long, TLRPC.User> concurrentHashMap) {
        int indexOf;
        ArrayList<TLRPC.TL_contact> arrayList2 = new ArrayList<>();
        ArrayList<Long> arrayList3 = new ArrayList<>();
        Iterator<Long> it = arrayList.iterator();
        while (it.hasNext()) {
            Long next = it.next();
            if (next.longValue() > 0) {
                TLRPC.TL_contact tL_contact = new TLRPC.TL_contact();
                tL_contact.user_id = next.longValue();
                arrayList2.add(tL_contact);
                if (!this.delayedContactsUpdate.isEmpty() && (indexOf = this.delayedContactsUpdate.indexOf(Long.valueOf(-next.longValue()))) != -1) {
                    this.delayedContactsUpdate.remove(indexOf);
                }
            } else if (next.longValue() < 0) {
                arrayList3.add(Long.valueOf(-next.longValue()));
                if (!this.delayedContactsUpdate.isEmpty() && (indexOf = this.delayedContactsUpdate.indexOf(Long.valueOf(-next.longValue()))) != -1) {
                    this.delayedContactsUpdate.remove(indexOf);
                }
            }
        }
        if (!arrayList3.isEmpty()) {
            getMessagesStorage().deleteContacts(arrayList3);
        }
        if (!arrayList2.isEmpty()) {
            getMessagesStorage().putContacts(arrayList2, false);
        }
        if (this.contactsLoaded && this.contactsBookLoaded) {
            applyContactsUpdates(arrayList, concurrentHashMap, arrayList2, arrayList3);
            return;
        }
        this.delayedContactsUpdate.addAll(arrayList);
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("delay update - contacts add = " + arrayList2.size() + " delete = " + arrayList3.size());
        }
    }

    public void processLoadedContacts(final ArrayList<TLRPC.TL_contact> arrayList, final ArrayList<TLRPC.User> arrayList2, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$processLoadedContacts$37(arrayList2, i, arrayList);
            }
        });
    }

    public void readContacts() {
        synchronized (this.loadContactsSync) {
            try {
                if (this.loadingContacts) {
                    return;
                }
                this.loadingContacts = true;
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda61
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsController.this.lambda$readContacts$11();
                    }
                });
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:104:0x054e A[Catch: all -> 0x0552, TRY_LEAVE, TryCatch #9 {all -> 0x0552, blocks: (B:102:0x0549, B:104:0x054e), top: B:101:0x0549 }] */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0565  */
    /* JADX WARN: Removed duplicated region for block: B:111:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0557 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v36, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r0v50 */
    /* JADX WARN: Type inference failed for: r0v56 */
    /* JADX WARN: Type inference failed for: r15v1, types: [java.lang.CharSequence, java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r4v12, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r4v19, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r4v3, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r4v30, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v12 */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean, int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public HashMap<String, Contact> readContactsFromPhoneBook() {
        Cursor cursor;
        HashMap<String, Contact> hashMap;
        Exception e;
        HashMap<String, Contact> hashMap2;
        Cursor cursor2;
        ContentResolver contentResolver;
        String str;
        long j;
        String str2;
        boolean z;
        int i;
        int i2;
        int i3;
        Cursor cursor3;
        String str3;
        String str4;
        String str5;
        long j2;
        StringBuilder sb;
        ContentResolver contentResolver2;
        ArrayList<String> arrayList;
        int i4;
        String string;
        String trim;
        ?? r0;
        Cursor cursor4;
        HashMap hashMap3;
        String str6;
        if (!getUserConfig().syncContacts) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("contacts sync disabled");
            }
            return new HashMap<>();
        }
        if (!hasContactsPermission()) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("app has no contacts permissions");
            }
            return new HashMap<>();
        }
        try {
            StringBuilder sb2 = new StringBuilder();
            ContentResolver contentResolver3 = ApplicationLoader.applicationContext.getContentResolver();
            HashMap hashMap4 = new HashMap();
            ArrayList arrayList2 = new ArrayList();
            ?? query = contentResolver3.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, this.projectionPhones, null, null, null);
            try {
                long currentTimeMillis = System.currentTimeMillis();
                String str7 = "+";
                String str8 = "";
                ?? r5 = 1;
                if (query != 0) {
                    try {
                        int count = query.getCount();
                        if (count > 0) {
                            hashMap2 = new HashMap<>(count);
                            i3 = 1;
                            while (query.moveToNext()) {
                                try {
                                    String string2 = query.getString(r5);
                                    String string3 = query.getString(5);
                                    if (string3 == null) {
                                        string3 = str8;
                                    }
                                    boolean z2 = string3.indexOf(".sim") != 0;
                                    if (!TextUtils.isEmpty(string2)) {
                                        ?? stripExceptNumbers = PhoneFormat.stripExceptNumbers(string2, r5);
                                        if (!TextUtils.isEmpty(stripExceptNumbers)) {
                                            if (stripExceptNumbers.startsWith(str7)) {
                                                str3 = str8;
                                                str4 = str7;
                                                str5 = stripExceptNumbers.substring(r5);
                                            } else {
                                                str3 = str8;
                                                str4 = str7;
                                                str5 = stripExceptNumbers;
                                            }
                                            String string4 = query.getString(0);
                                            sb2.setLength(0);
                                            DatabaseUtils.appendEscapedSQLString(sb2, string4);
                                            String sb3 = sb2.toString();
                                            j2 = currentTimeMillis;
                                            Contact contact = (Contact) hashMap4.get(str5);
                                            if (contact != null) {
                                                if (!contact.isGoodProvider && !string3.equals(contact.provider)) {
                                                    sb2.setLength(0);
                                                    DatabaseUtils.appendEscapedSQLString(sb2, contact.key);
                                                    arrayList2.remove(sb2.toString());
                                                    arrayList2.add(sb3);
                                                    contact.key = string4;
                                                    contact.isGoodProvider = z2;
                                                    contact.provider = string3;
                                                }
                                                str8 = str3;
                                                str7 = str4;
                                                currentTimeMillis = j2;
                                                r5 = 1;
                                            } else {
                                                if (!arrayList2.contains(sb3)) {
                                                    arrayList2.add(sb3);
                                                }
                                                int i5 = query.getInt(2);
                                                Contact contact2 = hashMap2.get(string4);
                                                if (contact2 == null) {
                                                    contact2 = new Contact();
                                                    String string5 = query.getString(4);
                                                    String trim2 = string5 == null ? str3 : string5.trim();
                                                    if (isNotValidNameString(trim2)) {
                                                        contact2.first_name = trim2;
                                                        sb = sb2;
                                                        contentResolver2 = contentResolver3;
                                                    } else {
                                                        sb = sb2;
                                                        int lastIndexOf = trim2.lastIndexOf(32);
                                                        contentResolver2 = contentResolver3;
                                                        if (lastIndexOf != -1) {
                                                            contact2.first_name = trim2.substring(0, lastIndexOf).trim();
                                                            trim = trim2.substring(lastIndexOf + 1).trim();
                                                            contact2.last_name = trim;
                                                            contact2.provider = string3;
                                                            contact2.isGoodProvider = z2;
                                                            contact2.key = string4;
                                                            contact2.contact_id = i3;
                                                            hashMap2.put(string4, contact2);
                                                            i3++;
                                                        } else {
                                                            contact2.first_name = trim2;
                                                        }
                                                    }
                                                    trim = str3;
                                                    contact2.last_name = trim;
                                                    contact2.provider = string3;
                                                    contact2.isGoodProvider = z2;
                                                    contact2.key = string4;
                                                    contact2.contact_id = i3;
                                                    hashMap2.put(string4, contact2);
                                                    i3++;
                                                } else {
                                                    sb = sb2;
                                                    contentResolver2 = contentResolver3;
                                                }
                                                contact2.shortPhones.add(str5);
                                                contact2.phones.add(stripExceptNumbers);
                                                contact2.phoneDeleted.add(0);
                                                if (i5 == 0) {
                                                    String string6 = query.getString(3);
                                                    ArrayList<String> arrayList3 = contact2.phoneTypes;
                                                    if (string6 == null) {
                                                        string6 = LocaleController.getString(R.string.PhoneMobile);
                                                    }
                                                    arrayList3.add(string6);
                                                } else {
                                                    if (i5 == 1) {
                                                        arrayList = contact2.phoneTypes;
                                                        string = LocaleController.getString(R.string.PhoneHome);
                                                    } else if (i5 == 2) {
                                                        arrayList = contact2.phoneTypes;
                                                        string = LocaleController.getString(R.string.PhoneMobile);
                                                    } else {
                                                        if (i5 == 3) {
                                                            arrayList = contact2.phoneTypes;
                                                            i4 = R.string.PhoneWork;
                                                        } else if (i5 == 12) {
                                                            arrayList = contact2.phoneTypes;
                                                            i4 = R.string.PhoneMain;
                                                        } else {
                                                            arrayList = contact2.phoneTypes;
                                                            i4 = R.string.PhoneOther;
                                                        }
                                                        string = LocaleController.getString(i4);
                                                        arrayList.add(string);
                                                    }
                                                    arrayList.add(string);
                                                }
                                                hashMap4.put(str5, contact2);
                                                str8 = str3;
                                                str7 = str4;
                                                sb2 = sb;
                                                currentTimeMillis = j2;
                                                contentResolver3 = contentResolver2;
                                                r5 = 1;
                                            }
                                        }
                                    }
                                    str3 = str8;
                                    j2 = currentTimeMillis;
                                    str4 = str7;
                                    str8 = str3;
                                    str7 = str4;
                                    currentTimeMillis = j2;
                                    r5 = 1;
                                } catch (Throwable th) {
                                    th = th;
                                    cursor2 = query;
                                    try {
                                        FileLog.e(th);
                                        if (hashMap2 != null) {
                                        }
                                        if (cursor2 != null) {
                                        }
                                        hashMap = hashMap2;
                                        if (hashMap == null) {
                                        }
                                    } finally {
                                    }
                                }
                            }
                            contentResolver = contentResolver3;
                            str = str8;
                            j = currentTimeMillis;
                            str2 = str7;
                            z = true;
                            i = 0;
                            i2 = 3;
                        } else {
                            contentResolver = contentResolver3;
                            str = "";
                            j = currentTimeMillis;
                            str2 = "+";
                            z = true;
                            i = 0;
                            i2 = 3;
                            i3 = 1;
                            hashMap2 = null;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        cursor = query;
                        hashMap = null;
                    }
                    try {
                        query.close();
                    } catch (Exception unused) {
                    } catch (Throwable th3) {
                        th = th3;
                        cursor = query;
                        hashMap = hashMap2;
                        cursor2 = cursor;
                        hashMap2 = hashMap;
                        FileLog.e(th);
                        if (hashMap2 != null) {
                        }
                        if (cursor2 != null) {
                        }
                        hashMap = hashMap2;
                        if (hashMap == null) {
                        }
                    }
                    hashMap = hashMap2;
                    cursor3 = null;
                    r0 = z;
                } else {
                    contentResolver = contentResolver3;
                    str = "";
                    j = currentTimeMillis;
                    str2 = "+";
                    r0 = 1;
                    i = 0;
                    i2 = 3;
                    cursor3 = query;
                    i3 = 1;
                    hashMap = null;
                }
                try {
                    String join = TextUtils.join(",", arrayList2);
                    String str9 = str;
                    ?? query2 = contentResolver.query(ContactsContract.Data.CONTENT_URI, this.projectionNames, "lookup IN (" + join + ") AND mimetype = 'vnd.android.cursor.item/name'", null, null);
                    if (query2 != 0) {
                        while (query2.moveToNext()) {
                            try {
                                String string7 = query2.getString(i);
                                String string8 = query2.getString(r0);
                                String string9 = query2.getString(2);
                                String string10 = query2.getString(i2);
                                Contact contact3 = hashMap != null ? hashMap.get(string7) : null;
                                if (contact3 != null && !contact3.namesFilled) {
                                    if (contact3.isGoodProvider) {
                                        if (string8 != null) {
                                            contact3.first_name = string8;
                                        } else {
                                            contact3.first_name = str9;
                                        }
                                        if (string9 != null) {
                                            contact3.last_name = string9;
                                        } else {
                                            contact3.last_name = str9;
                                        }
                                        if (!TextUtils.isEmpty(string10)) {
                                            if (TextUtils.isEmpty(contact3.first_name)) {
                                                contact3.first_name = string10;
                                            } else {
                                                contact3.first_name += " " + string10;
                                            }
                                        }
                                    } else if ((!isNotValidNameString(string8) && (contact3.first_name.contains(string8) || string8.contains(contact3.first_name))) || (!isNotValidNameString(string9) && (contact3.last_name.contains(string9) || string8.contains(contact3.last_name)))) {
                                        if (string8 != null) {
                                            contact3.first_name = string8;
                                        } else {
                                            contact3.first_name = str9;
                                        }
                                        if (!TextUtils.isEmpty(string10)) {
                                            if (TextUtils.isEmpty(contact3.first_name)) {
                                                contact3.first_name = string10;
                                            } else {
                                                contact3.first_name += " " + string10;
                                            }
                                        }
                                        if (string9 != null) {
                                            contact3.last_name = string9;
                                        } else {
                                            contact3.last_name = str9;
                                        }
                                    }
                                    contact3.namesFilled = r0;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                cursor = query2;
                                cursor2 = cursor;
                                hashMap2 = hashMap;
                                FileLog.e(th);
                                if (hashMap2 != null) {
                                }
                                if (cursor2 != null) {
                                }
                                hashMap = hashMap2;
                                if (hashMap == null) {
                                }
                            }
                        }
                        try {
                            query2.close();
                        } catch (Exception unused2) {
                        }
                        cursor4 = null;
                    } else {
                        cursor4 = query2;
                    }
                    try {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("loading contacts 1 query time = ");
                        sb4.append(System.currentTimeMillis() - j);
                        sb4.append(" contactsSize = ");
                        sb4.append(hashMap == null ? 0 : hashMap.size());
                        FileLog.d(sb4.toString());
                        long currentTimeMillis2 = System.currentTimeMillis();
                        HashMap hashMap5 = new HashMap();
                        ArrayList arrayList4 = new ArrayList();
                        HashMap hashMap6 = hashMap5;
                        ?? query3 = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id", "lookup", "display_name"}, "has_phone_number = ?", new String[]{"0"}, null);
                        if (query3 != 0) {
                            while (query3.moveToNext()) {
                                PhoneBookContact phoneBookContact = new PhoneBookContact();
                                phoneBookContact.id = query3.getString(i);
                                phoneBookContact.lookup_key = query3.getString(r0);
                                phoneBookContact.name = query3.getString(2);
                                if (hashMap != null && hashMap.get(phoneBookContact.lookup_key) != null) {
                                }
                                if (!TextUtils.isEmpty(phoneBookContact.name)) {
                                    HashMap hashMap7 = hashMap6;
                                    hashMap7.put(phoneBookContact.id, phoneBookContact);
                                    arrayList4.add(phoneBookContact.id);
                                    hashMap6 = hashMap7;
                                }
                            }
                            hashMap3 = hashMap6;
                            query3.close();
                        } else {
                            hashMap3 = hashMap6;
                        }
                        FileLog.d("loading contacts 2 query time = " + (System.currentTimeMillis() - currentTimeMillis2) + " phoneBookConacts size = " + arrayList4.size());
                        long currentTimeMillis3 = System.currentTimeMillis();
                        if (arrayList4.isEmpty()) {
                            cursor = cursor4;
                        } else {
                            Pattern compile = Pattern.compile(".*(\\+[0-9 \\-]+).*");
                            HashMap hashMap8 = hashMap3;
                            query2 = contentResolver.query(ContactsContract.Data.CONTENT_URI, new String[]{"contact_id", "data1", "data2", "data3", "data4"}, "contact_id IN (" + TextUtils.join(", ", arrayList4) + ")", null, null);
                            if (query2 != 0) {
                                int i6 = i3;
                                HashMap<String, Contact> hashMap9 = hashMap;
                                while (query2.moveToNext()) {
                                    try {
                                        PhoneBookContact phoneBookContact2 = (PhoneBookContact) hashMap8.get(query2.getString(i));
                                        if (phoneBookContact2 != null) {
                                            String[] strArr = {query2.getString(1), query2.getString(2), query2.getString(3), query2.getString(4)};
                                            int i7 = 0;
                                            while (true) {
                                                if (i7 >= 4) {
                                                    str6 = str2;
                                                    break;
                                                }
                                                String str10 = strArr[i7];
                                                if (str10 != null) {
                                                    Matcher matcher = compile.matcher(str10);
                                                    if (matcher.matches()) {
                                                        phoneBookContact2.phone = matcher.group(1).replace(" ", str9).replace("-", str9);
                                                    }
                                                    String str11 = phoneBookContact2.phone;
                                                    if (str11 != null) {
                                                        str6 = str2;
                                                        if (str11.startsWith(str6)) {
                                                            str11 = phoneBookContact2.phone.substring(1);
                                                        }
                                                        Contact contact4 = new Contact();
                                                        contact4.first_name = phoneBookContact2.name;
                                                        contact4.last_name = str9;
                                                        int i8 = i6 + 1;
                                                        contact4.contact_id = i6;
                                                        contact4.key = phoneBookContact2.lookup_key;
                                                        contact4.phones.add(phoneBookContact2.phone);
                                                        contact4.shortPhones.add(str11);
                                                        contact4.phoneDeleted.add(Integer.valueOf(i));
                                                        contact4.phoneTypes.add(LocaleController.getString(R.string.PhoneOther));
                                                        if (hashMap9 == null) {
                                                            hashMap9 = new HashMap<>();
                                                        }
                                                        hashMap9.put(phoneBookContact2.lookup_key, contact4);
                                                        i6 = i8;
                                                    }
                                                }
                                                i7++;
                                                str2 = str2;
                                            }
                                        } else {
                                            str6 = str2;
                                        }
                                        str2 = str6;
                                    } catch (Throwable th5) {
                                        th = th5;
                                        hashMap = hashMap9;
                                        cursor = query2;
                                        cursor2 = cursor;
                                        hashMap2 = hashMap;
                                        FileLog.e(th);
                                        if (hashMap2 != null) {
                                            hashMap2.clear();
                                        }
                                        if (cursor2 != null) {
                                            try {
                                                cursor2.close();
                                            } catch (Exception e2) {
                                                e = e2;
                                                hashMap = hashMap2;
                                                FileLog.e(e);
                                                if (hashMap == null) {
                                                }
                                            }
                                        }
                                        hashMap = hashMap2;
                                        if (hashMap == null) {
                                        }
                                    }
                                }
                                query2.close();
                                hashMap = hashMap9;
                            }
                            cursor = query2;
                        }
                        try {
                            FileLog.d("loading contacts 3 query time = " + (System.currentTimeMillis() - currentTimeMillis3));
                            if (cursor != null) {
                                try {
                                    cursor.close();
                                } catch (Exception e3) {
                                    e = e3;
                                    FileLog.e(e);
                                    if (hashMap == null) {
                                    }
                                }
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            cursor2 = cursor;
                            hashMap2 = hashMap;
                            FileLog.e(th);
                            if (hashMap2 != null) {
                            }
                            if (cursor2 != null) {
                            }
                            hashMap = hashMap2;
                            if (hashMap == null) {
                            }
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        cursor = cursor4;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    cursor = cursor3;
                }
            } catch (Throwable th9) {
                th = th9;
                cursor = query;
                hashMap = null;
            }
        } catch (Throwable th10) {
            th = th10;
            cursor = null;
            hashMap = null;
        }
        return hashMap == null ? hashMap : new HashMap<>();
    }

    public void reloadContactsStatusesMaybe(boolean z) {
        try {
            if (MessagesController.getMainSettings(this.currentAccount).getLong("lastReloadStatusTime", 0L) < System.currentTimeMillis() - 10800000 || z) {
                reloadContactsStatuses();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void resetImportedContacts() {
        getConnectionsManager().sendRequest(new TLRPC.TL_contacts_resetSaved(), new RequestDelegate() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda47
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ContactsController.lambda$resetImportedContacts$10(tLObject, tL_error);
            }
        });
    }

    public void setDeleteAccountTTL(int i) {
        this.deleteAccountTTL = i;
    }

    public void setPrivacyRules(ArrayList<TLRPC.PrivacyRule> arrayList, int i) {
        switch (i) {
            case 0:
                this.lastseenPrivacyRules = arrayList;
                break;
            case 1:
                this.groupPrivacyRules = arrayList;
                break;
            case 2:
                this.callPrivacyRules = arrayList;
                break;
            case 3:
                this.p2pPrivacyRules = arrayList;
                break;
            case 4:
                this.profilePhotoPrivacyRules = arrayList;
                break;
            case 5:
                this.forwardsPrivacyRules = arrayList;
                break;
            case 6:
                this.phonePrivacyRules = arrayList;
                break;
            case 7:
                this.addedByPhonePrivacyRules = arrayList;
                break;
            case 8:
                this.voiceMessagesRules = arrayList;
                break;
            case 9:
                this.bioPrivacyRules = arrayList;
                break;
            case 11:
                this.birthdayPrivacyRules = arrayList;
                break;
            case 12:
                this.giftsPrivacyRules = arrayList;
                break;
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.privacyRulesUpdated, new Object[0]);
        reloadContactsStatuses();
    }

    public void syncPhoneBookByAlert(final HashMap<String, Contact> hashMap, final boolean z, final boolean z2, final boolean z3) {
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ContactsController$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                ContactsController.this.lambda$syncPhoneBookByAlert$7(hashMap, z, z2, z3);
            }
        });
    }
}
