package org.telegram.ui.Adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes4.dex */
public abstract class SearchAdapter extends RecyclerListView.SelectionAdapter {
    private ArrayList allUnregistredContacts;
    private boolean allowBots;
    private boolean allowChats;
    private boolean allowPhoneNumbers;
    private boolean allowSelf;
    private boolean allowUsernameSearch;
    private long channelId;
    private LongSparseArray ignoreUsers;
    private String lastQuery;
    private Context mContext;
    private boolean onlyMutual;
    private SearchAdapterHelper searchAdapterHelper;
    private boolean searchInProgress;
    private int searchPointer;
    private int searchReqId;
    private Timer searchTimer;
    private LongSparseArray selectedUsers;
    int unregistredContactsHeaderRow;
    private boolean useUserCell;
    private ArrayList searchResult = new ArrayList();
    private ArrayList searchResultNames = new ArrayList();
    private ArrayList unregistredContacts = new ArrayList();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ContactEntry {
        ContactsController.Contact contact;
        String q1;
        String q2;

        private ContactEntry() {
        }
    }

    public SearchAdapter(Context context, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i) {
        this.mContext = context;
        this.ignoreUsers = longSparseArray;
        this.selectedUsers = longSparseArray2;
        this.onlyMutual = z2;
        this.allowUsernameSearch = z;
        this.allowChats = z3;
        this.allowBots = z4;
        this.channelId = i;
        this.allowSelf = z5;
        this.allowPhoneNumbers = z6;
        SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
        this.searchAdapterHelper = searchAdapterHelper;
        searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.Adapters.SearchAdapter.1
            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ boolean canApplySearchResults(int i2) {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i2);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public LongSparseArray getExcludeUsers() {
                return SearchAdapter.this.ignoreUsers;
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public void onDataSetChanged(int i2) {
                SearchAdapter.this.notifyDataSetChanged();
                if (i2 != 0) {
                    SearchAdapter.this.onSearchProgressChanged();
                }
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList, hashMap);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSearch$0(String str, int i, ArrayList arrayList, int i2) {
        LongSparseArray longSparseArray;
        int i3;
        String[] strArr;
        String[] strArr2;
        String str2;
        String str3;
        String lowerCase = str.trim().toLowerCase();
        if (lowerCase.length() == 0) {
            updateSearchResults(i, new ArrayList(), new ArrayList(), this.unregistredContacts);
            return;
        }
        String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
        translitString = (lowerCase.equals(translitString) || translitString.length() == 0) ? null : null;
        int i4 = (translitString != null ? 1 : 0) + 1;
        String[] strArr3 = new String[i4];
        strArr3[0] = lowerCase;
        if (translitString != null) {
            strArr3[1] = translitString;
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        int i5 = 0;
        while (i5 < arrayList.size()) {
            TLRPC.TL_contact tL_contact = (TLRPC.TL_contact) arrayList.get(i5);
            int i6 = i4;
            TLRPC.User user = MessagesController.getInstance(i2).getUser(Long.valueOf(tL_contact.user_id));
            if ((this.allowSelf || !user.self) && ((!this.onlyMutual || user.mutual_contact) && ((longSparseArray = this.ignoreUsers) == null || longSparseArray.indexOfKey(tL_contact.user_id) < 0))) {
                String[] strArr4 = new String[3];
                strArr4[0] = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                String translitString2 = LocaleController.getInstance().getTranslitString(strArr4[0]);
                strArr4[1] = translitString2;
                if (strArr4[0].equals(translitString2)) {
                    strArr4[1] = null;
                }
                if (UserObject.isReplyUser(user)) {
                    strArr4[2] = LocaleController.getString(R.string.RepliesTitle).toLowerCase();
                } else if (user.self) {
                    strArr4[2] = LocaleController.getString(R.string.SavedMessages).toLowerCase();
                }
                int i7 = i6;
                int i8 = 0;
                char c = 0;
                while (true) {
                    i3 = i7;
                    if (i8 >= i7) {
                        strArr = strArr3;
                        break;
                    }
                    String str4 = strArr3[i8];
                    strArr = strArr3;
                    int i9 = 0;
                    while (i9 < 3) {
                        String str5 = strArr4[i9];
                        if (str5 != null) {
                            if (str5.startsWith(str4)) {
                                strArr2 = strArr4;
                            } else {
                                strArr2 = strArr4;
                                if (str5.contains(" " + str4)) {
                                }
                            }
                            c = 1;
                            break;
                        }
                        strArr2 = strArr4;
                        i9++;
                        strArr4 = strArr2;
                    }
                    strArr2 = strArr4;
                    String publicUsername = UserObject.getPublicUsername(user);
                    if (c == 0 && publicUsername != null && publicUsername.startsWith(str4)) {
                        c = 2;
                    }
                    if (c != 0) {
                        if (c == 1) {
                            str2 = user.first_name;
                            str3 = user.last_name;
                        } else {
                            str2 = "@" + UserObject.getPublicUsername(user);
                            str4 = "@" + str4;
                            str3 = null;
                        }
                        arrayList3.add(AndroidUtilities.generateSearchName(str2, str3, str4));
                        arrayList2.add(user);
                    } else {
                        i8++;
                        i7 = i3;
                        strArr3 = strArr;
                        strArr4 = strArr2;
                    }
                }
            } else {
                strArr = strArr3;
                i3 = i6;
            }
            i5++;
            i4 = i3;
            strArr3 = strArr;
        }
        if (this.allUnregistredContacts == null) {
            this.allUnregistredContacts = new ArrayList();
            Iterator<ContactsController.Contact> it = ContactsController.getInstance(i2).phoneBookContacts.iterator();
            while (it.hasNext()) {
                ContactsController.Contact next = it.next();
                ContactEntry contactEntry = new ContactEntry();
                contactEntry.contact = next;
                contactEntry.q1 = (next.first_name + " " + next.last_name).toLowerCase();
                contactEntry.q2 = (next.last_name + " " + next.first_name).toLowerCase();
                this.allUnregistredContacts.add(contactEntry);
            }
        }
        for (int i10 = 0; i10 < this.allUnregistredContacts.size(); i10++) {
            ContactEntry contactEntry2 = (ContactEntry) this.allUnregistredContacts.get(i10);
            if ((translitString != null && (contactEntry2.q1.toLowerCase().contains(translitString) || contactEntry2.q1.toLowerCase().contains(translitString))) || contactEntry2.q1.toLowerCase().contains(lowerCase) || contactEntry2.q1.toLowerCase().contains(lowerCase)) {
                arrayList4.add(contactEntry2.contact);
            }
        }
        updateSearchResults(i, arrayList2, arrayList3, arrayList4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSearch$1(final String str) {
        this.lastQuery = str;
        if (this.allowUsernameSearch) {
            this.searchAdapterHelper.queryServerSearch(str, true, this.allowChats, this.allowBots, this.allowSelf, false, this.channelId, this.allowPhoneNumbers, -1, 1);
        }
        final int i = UserConfig.selectedAccount;
        final ArrayList arrayList = new ArrayList(ContactsController.getInstance(i).contacts);
        this.searchInProgress = true;
        final int i2 = this.searchPointer;
        this.searchPointer = i2 + 1;
        this.searchReqId = i2;
        Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.SearchAdapter$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SearchAdapter.this.lambda$processSearch$0(str, i2, arrayList, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSearchResults$2(int i, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
        if (i == this.searchReqId) {
            this.searchResult = arrayList;
            this.searchResultNames = arrayList2;
            this.unregistredContacts = arrayList3;
            this.searchAdapterHelper.mergeResults(arrayList);
            this.searchInProgress = false;
            notifyDataSetChanged();
            onSearchProgressChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSearch(final String str) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.SearchAdapter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SearchAdapter.this.lambda$processSearch$1(str);
            }
        });
    }

    private void updateSearchResults(final int i, final ArrayList arrayList, final ArrayList arrayList2, final ArrayList arrayList3) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.SearchAdapter$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SearchAdapter.this.lambda$updateSearchResults$2(i, arrayList, arrayList2, arrayList3);
            }
        });
    }

    public Object getItem(int i) {
        ArrayList globalSearch;
        int size = this.searchResult.size();
        int size2 = this.unregistredContacts.size();
        int size3 = this.searchAdapterHelper.getGlobalSearch().size();
        int size4 = this.searchAdapterHelper.getPhoneSearch().size();
        if (i < 0 || i >= size) {
            i -= size;
            if (size2 > 0) {
                if (i == 0) {
                    return null;
                }
                if (i <= 0 || i > size2) {
                    i -= size2 + 1;
                } else {
                    globalSearch = this.unregistredContacts;
                    i--;
                }
            }
            if (i < 0 || i >= size4) {
                i -= size4;
                if (i <= 0 || i > size3) {
                    return null;
                }
                globalSearch = this.searchAdapterHelper.getGlobalSearch();
                i--;
            } else {
                globalSearch = this.searchAdapterHelper.getPhoneSearch();
            }
        } else {
            globalSearch = this.searchResult;
        }
        return globalSearch.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        this.unregistredContactsHeaderRow = -1;
        int size = this.searchResult.size();
        if (!this.unregistredContacts.isEmpty()) {
            this.unregistredContactsHeaderRow = size;
            size += this.unregistredContacts.size() + 1;
        }
        int size2 = this.searchAdapterHelper.getGlobalSearch().size();
        if (size2 != 0) {
            size += size2 + 1;
        }
        int size3 = this.searchAdapterHelper.getPhoneSearch().size();
        return size3 != 0 ? size + size3 : size;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        Object item = getItem(i);
        if (item == null) {
            return 1;
        }
        return item instanceof String ? "section".equals((String) item) ? 1 : 2 : item instanceof ContactsController.Contact ? 3 : 0;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        return itemViewType == 0 || itemViewType == 2 || itemViewType == 3;
    }

    public boolean isGlobalSearch(int i) {
        int size = this.searchResult.size();
        int size2 = this.unregistredContacts.size();
        int size3 = this.searchAdapterHelper.getGlobalSearch().size();
        int size4 = this.searchAdapterHelper.getPhoneSearch().size();
        if (i < 0 || i >= size) {
            if (i <= size || i >= size + size2 + 1) {
                return (i <= (size + size2) + 1 || i >= ((size + size4) + size2) + 1) && i > ((size + size4) + size2) + 1 && i <= (((size3 + size4) + size) + size2) + 1;
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01b2  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01c5  */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r2v31, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r8v1, types: [android.text.SpannableStringBuilder] */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        long j;
        String str;
        long j2;
        boolean z;
        ?? r2;
        String str2;
        boolean z2;
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType != 0) {
            if (itemViewType == 1) {
                ((GraySectionCell) viewHolder.itemView).setText(LocaleController.getString(i == this.unregistredContactsHeaderRow ? R.string.InviteToTelegramShort : getItem(i) == null ? R.string.GlobalSearch : R.string.PhoneNumberSearch));
                return;
            } else if (itemViewType == 2) {
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(-1, Theme.key_windowBackgroundWhiteBlueText2);
                textCell.setText(LocaleController.formatString("AddContactByPhone", R.string.AddContactByPhone, PhoneFormat.getInstance().format("+" + ((String) getItem(i)))), false);
                return;
            } else if (itemViewType != 3) {
                return;
            } else {
                ProfileSearchCell profileSearchCell = (ProfileSearchCell) viewHolder.itemView;
                ContactsController.Contact contact = (ContactsController.Contact) getItem(i);
                profileSearchCell.useSeparator = getItem(i + 1) instanceof ContactsController.Contact;
                profileSearchCell.setData(contact, null, ContactsController.formatName(contact.first_name, contact.last_name), PhoneFormat.getInstance().format("+" + contact.shortPhones.get(0)), false, false);
                return;
            }
        }
        TLObject tLObject = (TLObject) getItem(i);
        if (tLObject == null) {
            return;
        }
        String str3 = null;
        if (tLObject instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) tLObject;
            str = UserObject.getPublicUsername(user);
            if (str != null && this.lastQuery != null && !str.toLowerCase().contains(this.lastQuery.toLowerCase()) && user.usernames != null) {
                for (int i2 = 0; i2 < user.usernames.size(); i2++) {
                    TLRPC.TL_username tL_username = user.usernames.get(i2);
                    if (tL_username != null && tL_username.active && tL_username.username.toLowerCase().contains(this.lastQuery.toLowerCase())) {
                        str = tL_username.username;
                    }
                }
            }
            long j3 = user.id;
            z = user.self;
            j2 = j3;
        } else {
            if (tLObject instanceof TLRPC.Chat) {
                TLRPC.Chat chat = (TLRPC.Chat) tLObject;
                str = ChatObject.getPublicUsername(chat);
                j = chat.id;
            } else {
                j = 0;
                str = null;
            }
            j2 = j;
            z = false;
        }
        if (i < this.searchResult.size()) {
            r2 = (CharSequence) this.searchResultNames.get(i);
            if (r2 != null && str != null && str.length() > 0) {
                if (r2.toString().startsWith("@" + str)) {
                    str2 = r2;
                    z2 = this.useUserCell;
                    View view = viewHolder.itemView;
                    if (z2) {
                        UserCell userCell = (UserCell) view;
                        userCell.setData(tLObject, str3, str2, 0);
                        userCell.setChecked(this.selectedUsers.indexOfKey(j2) >= 0, false);
                        return;
                    }
                    ProfileSearchCell profileSearchCell2 = (ProfileSearchCell) view;
                    profileSearchCell2.setData(tLObject, null, z ? LocaleController.getString(R.string.SavedMessages) : str3, str2, false, z);
                    profileSearchCell2.useSeparator = (i == getItemCount() - 1 || i == this.searchResult.size() - 1) ? false : true;
                    profileSearchCell2.setChecked(this.selectedUsers.indexOfKey(j2) >= 0, false);
                    return;
                }
            }
        } else if (i <= this.searchResult.size() || str == null) {
            r2 = null;
        } else {
            String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
            if (lastFoundUsername != null && lastFoundUsername.startsWith("@")) {
                lastFoundUsername = lastFoundUsername.substring(1);
            }
            try {
                ?? spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder.append("@");
                spannableStringBuilder.append(str);
                str2 = spannableStringBuilder;
                if (lastFoundUsername != null) {
                    int indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(str, lastFoundUsername);
                    str2 = spannableStringBuilder;
                    if (indexOfIgnoreCase != -1) {
                        int length = lastFoundUsername.length();
                        if (indexOfIgnoreCase == 0) {
                            length++;
                        } else {
                            indexOfIgnoreCase++;
                        }
                        spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_windowBackgroundWhiteBlueText4), indexOfIgnoreCase, length + indexOfIgnoreCase, 33);
                        str2 = spannableStringBuilder;
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
                str2 = str;
            }
            z2 = this.useUserCell;
            View view2 = viewHolder.itemView;
            if (z2) {
            }
        }
        str2 = null;
        str3 = r2;
        z2 = this.useUserCell;
        View view22 = viewHolder.itemView;
        if (z2) {
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerListView.Holder(i != 0 ? i != 1 ? i != 3 ? new TextCell(this.mContext, 16, false) : new ProfileSearchCell(this.mContext) : new GraySectionCell(this.mContext) : this.useUserCell ? new UserCell(this.mContext, 1, 1, false) : new ProfileSearchCell(this.mContext));
    }

    protected abstract void onSearchProgressChanged();

    public void searchDialogs(final String str) {
        try {
            Timer timer = this.searchTimer;
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.searchResult.clear();
        this.unregistredContacts.clear();
        this.searchResultNames.clear();
        if (this.allowUsernameSearch) {
            this.searchAdapterHelper.queryServerSearch(null, true, this.allowChats, this.allowBots, this.allowSelf, false, this.channelId, this.allowPhoneNumbers, 0, 0);
        }
        notifyDataSetChanged();
        if (TextUtils.isEmpty(str)) {
            return;
        }
        Timer timer2 = new Timer();
        this.searchTimer = timer2;
        timer2.schedule(new TimerTask() { // from class: org.telegram.ui.Adapters.SearchAdapter.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                try {
                    SearchAdapter.this.searchTimer.cancel();
                    SearchAdapter.this.searchTimer = null;
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                SearchAdapter.this.processSearch(str);
            }
        }, 200L, 300L);
    }

    public boolean searchInProgress() {
        return this.searchInProgress || this.searchAdapterHelper.isSearchInProgress();
    }
}
