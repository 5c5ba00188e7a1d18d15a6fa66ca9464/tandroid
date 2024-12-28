package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Property;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.ScrollView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Components.AnimatedAvatarContainer;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.UsersSelectActivity;

/* loaded from: classes4.dex */
public class UsersSelectActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, View.OnClickListener {
    private GroupCreateAdapter adapter;
    private ArrayList allSpans;
    public boolean allowSelf;
    AnimatedAvatarContainer animatedAvatarContainer;
    private int containerHeight;
    private GroupCreateSpan currentDeletingSpan;
    private FilterUsersActivityDelegate delegate;
    public boolean doNotNewChats;
    private EditTextBoldCursor editText;
    private StickerEmptyView emptyView;
    private int fieldY;
    private int filterFlags;
    private ImageView floatingButton;
    private boolean ignoreScrollEvent;
    private ArrayList initialIds;
    private boolean isInclude;
    private RecyclerListView listView;
    public boolean noChatTypes;
    private FlickerLoadingView progressView;
    private ScrollView scrollView;
    private boolean searchWas;
    private boolean searching;
    private LongSparseArray selectedContacts;
    private int selectedCount;
    private SpansContainer spansContainer;
    private int ttlPeriod;
    private int type;

    public interface FilterUsersActivityDelegate {
        void didSelectChats(ArrayList arrayList, int i);
    }

    public class GroupCreateAdapter extends RecyclerListView.FastScrollAdapter {
        private Context context;
        private SearchAdapterHelper searchAdapterHelper;
        private Runnable searchRunnable;
        private boolean searching;
        private final int usersStartRow;
        private ArrayList searchResult = new ArrayList();
        private ArrayList searchResultNames = new ArrayList();
        private ArrayList contacts = new ArrayList();

        /* JADX WARN: Removed duplicated region for block: B:12:0x0054  */
        /* JADX WARN: Removed duplicated region for block: B:15:0x0067  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x00cb  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x0056  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x004d  */
        /* JADX WARN: Removed duplicated region for block: B:9:0x004b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public GroupCreateAdapter(Context context) {
            int size;
            int i;
            boolean z;
            int i2;
            this.context = context;
            if (!UsersSelectActivity.this.noChatTypes) {
                if (UsersSelectActivity.this.type == 2) {
                    i2 = (!UsersSelectActivity.this.doNotNewChats ? 1 : 0) + 5;
                } else if (UsersSelectActivity.this.type == 0) {
                    if (!UsersSelectActivity.this.isInclude) {
                        this.usersStartRow = 5;
                        boolean z2 = UsersSelectActivity.this.type != 2;
                        boolean z3 = UsersSelectActivity.this.type != 2;
                        ArrayList<TLRPC.Dialog> allDialogs = UsersSelectActivity.this.getMessagesController().getAllDialogs();
                        size = allDialogs.size();
                        z = false;
                        for (i = 0; i < size; i++) {
                            TLRPC.Dialog dialog = allDialogs.get(i);
                            if (!DialogObject.isEncryptedDialog(dialog.id)) {
                                if (DialogObject.isUserDialog(dialog.id)) {
                                    TLRPC.User user = UsersSelectActivity.this.getMessagesController().getUser(Long.valueOf(dialog.id));
                                    if (user != null && ((UsersSelectActivity.this.allowSelf || !UserObject.isUserSelf(user)) && (!user.bot || z2))) {
                                        this.contacts.add(user);
                                        if (UserObject.isUserSelf(user)) {
                                            z = true;
                                        }
                                    }
                                } else {
                                    TLRPC.Chat chat = UsersSelectActivity.this.getMessagesController().getChat(Long.valueOf(-dialog.id));
                                    if (z3 && chat != null) {
                                        this.contacts.add(chat);
                                    }
                                }
                            }
                        }
                        if (!z && UsersSelectActivity.this.allowSelf) {
                            this.contacts.add(0, UsersSelectActivity.this.getMessagesController().getUser(Long.valueOf(UsersSelectActivity.this.getUserConfig().clientUserId)));
                        }
                        SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(false);
                        this.searchAdapterHelper = searchAdapterHelper;
                        searchAdapterHelper.setAllowGlobalResults(false);
                        this.searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.UsersSelectActivity$GroupCreateAdapter$$ExternalSyntheticLambda0
                            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                            public /* synthetic */ boolean canApplySearchResults(int i3) {
                                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i3);
                            }

                            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                            public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
                            }

                            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                            public /* synthetic */ LongSparseArray getExcludeUsers() {
                                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
                            }

                            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                            public final void onDataSetChanged(int i3) {
                                UsersSelectActivity.GroupCreateAdapter.this.lambda$new$0(i3);
                            }

                            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                            public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                                SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList, hashMap);
                            }
                        });
                    }
                    i2 = 7;
                }
                this.usersStartRow = i2;
                if (UsersSelectActivity.this.type != 2) {
                }
                if (UsersSelectActivity.this.type != 2) {
                }
                ArrayList<TLRPC.Dialog> allDialogs2 = UsersSelectActivity.this.getMessagesController().getAllDialogs();
                size = allDialogs2.size();
                z = false;
                while (i < size) {
                }
                if (!z) {
                    this.contacts.add(0, UsersSelectActivity.this.getMessagesController().getUser(Long.valueOf(UsersSelectActivity.this.getUserConfig().clientUserId)));
                }
                SearchAdapterHelper searchAdapterHelper2 = new SearchAdapterHelper(false);
                this.searchAdapterHelper = searchAdapterHelper2;
                searchAdapterHelper2.setAllowGlobalResults(false);
                this.searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.UsersSelectActivity$GroupCreateAdapter$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                    public /* synthetic */ boolean canApplySearchResults(int i3) {
                        return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i3);
                    }

                    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                    public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                        return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
                    }

                    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                    public /* synthetic */ LongSparseArray getExcludeUsers() {
                        return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
                    }

                    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                    public final void onDataSetChanged(int i3) {
                        UsersSelectActivity.GroupCreateAdapter.this.lambda$new$0(i3);
                    }

                    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                    public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                        SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList, hashMap);
                    }
                });
            }
            this.usersStartRow = 0;
            if (UsersSelectActivity.this.type != 2) {
            }
            if (UsersSelectActivity.this.type != 2) {
            }
            ArrayList<TLRPC.Dialog> allDialogs22 = UsersSelectActivity.this.getMessagesController().getAllDialogs();
            size = allDialogs22.size();
            z = false;
            while (i < size) {
            }
            if (!z) {
            }
            SearchAdapterHelper searchAdapterHelper22 = new SearchAdapterHelper(false);
            this.searchAdapterHelper = searchAdapterHelper22;
            searchAdapterHelper22.setAllowGlobalResults(false);
            this.searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.UsersSelectActivity$GroupCreateAdapter$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ boolean canApplySearchResults(int i3) {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i3);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeUsers() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public final void onDataSetChanged(int i3) {
                    UsersSelectActivity.GroupCreateAdapter.this.lambda$new$0(i3);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                    SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList, hashMap);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int i) {
            if (this.searchRunnable == null && !this.searchAdapterHelper.isSearchInProgress()) {
                UsersSelectActivity.this.emptyView.showProgress(false);
            }
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:76:0x00b2, code lost:
        
            if (r20 == false) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:78:0x00c2, code lost:
        
            if (r21 == false) goto L30;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$searchDialogs$1(String str, boolean z, boolean z2) {
            String str2;
            int i;
            String str3;
            CharSequence generateSearchName;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList(), new ArrayList());
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            String str4 = null;
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
            char c = 0;
            char c2 = 1;
            int i2 = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i2];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int i3 = 0;
            while (i3 < this.contacts.size()) {
                TLObject tLObject = (TLObject) this.contacts.get(i3);
                String[] strArr2 = new String[3];
                boolean z3 = tLObject instanceof TLRPC.User;
                if (z3) {
                    TLRPC.User user = (TLRPC.User) tLObject;
                    strArr2[c] = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                    str2 = UserObject.getPublicUsername(user);
                    if (UserObject.isReplyUser(user)) {
                        strArr2[2] = LocaleController.getString(R.string.RepliesTitle).toLowerCase();
                    } else if (UserObject.isUserSelf(user)) {
                        if (UsersSelectActivity.this.allowSelf) {
                            strArr2[2] = LocaleController.getString(R.string.SavedMessages).toLowerCase();
                        }
                        i = i2;
                        str3 = str4;
                    } else if (user.bot) {
                    }
                    String translitString2 = LocaleController.getInstance().getTranslitString(strArr2[c]);
                    strArr2[c2] = translitString2;
                    if (strArr2[c].equals(translitString2)) {
                        strArr2[c2] = str4;
                    }
                    int i4 = 0;
                    char c3 = 0;
                    while (i4 < i2) {
                        String str5 = strArr[i4];
                        int i5 = 0;
                        while (i5 < 3) {
                            String str6 = strArr2[i5];
                            if (str6 != null) {
                                if (str6.startsWith(str5)) {
                                    i = i2;
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    i = i2;
                                    sb.append(" ");
                                    sb.append(str5);
                                    if (str6.contains(sb.toString())) {
                                    }
                                }
                                c3 = 1;
                                break;
                            }
                            i = i2;
                            i5++;
                            i2 = i;
                        }
                        i = i2;
                        if (c3 == 0 && str2 != null && str2.toLowerCase().startsWith(str5)) {
                            c3 = 2;
                        }
                        if (c3 != 0) {
                            if (c3 == 1) {
                                if (z3) {
                                    TLRPC.User user2 = (TLRPC.User) tLObject;
                                    generateSearchName = AndroidUtilities.generateSearchName(user2.first_name, user2.last_name, str5);
                                } else {
                                    generateSearchName = AndroidUtilities.generateSearchName(((TLRPC.Chat) tLObject).title, null, str5);
                                }
                                arrayList2.add(generateSearchName);
                                str3 = null;
                            } else {
                                str3 = null;
                                arrayList2.add(AndroidUtilities.generateSearchName("@" + str2, null, "@" + str5));
                            }
                            arrayList.add(tLObject);
                        } else {
                            i4++;
                            str4 = null;
                            i2 = i;
                        }
                    }
                    i = i2;
                    str3 = str4;
                } else {
                    TLRPC.Chat chat = (TLRPC.Chat) tLObject;
                    strArr2[c] = chat.title.toLowerCase();
                    str2 = chat.username;
                }
                i3++;
                str4 = str3;
                i2 = i;
                c = 0;
                c2 = 1;
            }
            updateSearchResults(arrayList, arrayList2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchDialogs$2(final String str, final boolean z, final boolean z2) {
            this.searchAdapterHelper.queryServerSearch(str, true, z, z, UsersSelectActivity.this.allowSelf, false, 0L, false, 0, 0);
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.UsersSelectActivity$GroupCreateAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    UsersSelectActivity.GroupCreateAdapter.this.lambda$searchDialogs$1(str, z2, z);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchDialogs$3(final String str, final boolean z, final boolean z2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.UsersSelectActivity$GroupCreateAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    UsersSelectActivity.GroupCreateAdapter.this.lambda$searchDialogs$2(str, z, z2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$4(ArrayList arrayList, ArrayList arrayList2) {
            if (this.searching) {
                this.searchRunnable = null;
                this.searchResult = arrayList;
                this.searchResultNames = arrayList2;
                this.searchAdapterHelper.mergeResults(arrayList);
                if (this.searching && !this.searchAdapterHelper.isSearchInProgress()) {
                    UsersSelectActivity.this.emptyView.showProgress(false);
                }
                notifyDataSetChanged();
            }
        }

        private void updateSearchResults(final ArrayList arrayList, final ArrayList arrayList2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.UsersSelectActivity$GroupCreateAdapter$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    UsersSelectActivity.GroupCreateAdapter.this.lambda$updateSearchResults$4(arrayList, arrayList2);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int i;
            int size;
            if (this.searching) {
                i = this.searchResult.size();
                size = this.searchAdapterHelper.getLocalServerSearch().size() + this.searchAdapterHelper.getGlobalSearch().size();
            } else {
                UsersSelectActivity usersSelectActivity = UsersSelectActivity.this;
                if (!usersSelectActivity.noChatTypes) {
                    if (usersSelectActivity.type == 2) {
                        i = (!UsersSelectActivity.this.doNotNewChats ? 1 : 0) + 3;
                    } else if (UsersSelectActivity.this.type == 0) {
                        i = UsersSelectActivity.this.isInclude ? 7 : 5;
                    }
                    size = this.contacts.size();
                }
                i = 0;
                size = this.contacts.size();
            }
            return i + size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (this.searching) {
                return 1;
            }
            UsersSelectActivity usersSelectActivity = UsersSelectActivity.this;
            if (usersSelectActivity.noChatTypes) {
                if (i == 0) {
                    return 2;
                }
            } else if (usersSelectActivity.type == 2) {
                if (i == 0 || i == (!UsersSelectActivity.this.doNotNewChats ? 1 : 0) + 4) {
                    return 2;
                }
            } else if (UsersSelectActivity.this.type == 0) {
                if (UsersSelectActivity.this.isInclude) {
                    if (i == 0 || i == 6) {
                        return 2;
                    }
                } else if (i == 0 || i == 4) {
                    return 2;
                }
            }
            return 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public String getLetter(int i) {
            return null;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr) {
            iArr[0] = (int) (getItemCount() * f);
            iArr[1] = 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 1;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:35:0x01f6  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x0212  */
        /* JADX WARN: Removed duplicated region for block: B:64:0x02ee  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x0300  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0312  */
        /* JADX WARN: Removed duplicated region for block: B:77:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:78:0x0303  */
        /* JADX WARN: Removed duplicated region for block: B:79:0x02f4  */
        /* JADX WARN: Removed duplicated region for block: B:84:0x01fc  */
        /* JADX WARN: Type inference failed for: r7v0 */
        /* JADX WARN: Type inference failed for: r7v1 */
        /* JADX WARN: Type inference failed for: r7v10 */
        /* JADX WARN: Type inference failed for: r7v11 */
        /* JADX WARN: Type inference failed for: r7v12 */
        /* JADX WARN: Type inference failed for: r7v13 */
        /* JADX WARN: Type inference failed for: r7v14 */
        /* JADX WARN: Type inference failed for: r7v15 */
        /* JADX WARN: Type inference failed for: r7v2 */
        /* JADX WARN: Type inference failed for: r7v9, types: [android.text.SpannableStringBuilder] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            Object obj;
            String string;
            CharSequence charSequence;
            long j;
            boolean z;
            SpannableStringBuilder spannableStringBuilder;
            boolean z2;
            SpannableStringBuilder spannableStringBuilder2;
            SpannableStringBuilder spannableStringBuilder3;
            int itemViewType = viewHolder.getItemViewType();
            int i2 = 2;
            if (itemViewType != 1) {
                if (itemViewType != 2) {
                    return;
                }
                ((GraySectionCell) viewHolder.itemView).setText(LocaleController.getString((i != 0 || UsersSelectActivity.this.noChatTypes) ? R.string.FilterChats : R.string.FilterChatTypes));
                return;
            }
            GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
            ?? r7 = 0;
            r7 = 0;
            r7 = 0;
            r7 = 0;
            if (this.searching) {
                int size = this.searchResult.size();
                int size2 = this.searchAdapterHelper.getGlobalSearch().size();
                int size3 = this.searchAdapterHelper.getLocalServerSearch().size();
                obj = (i < 0 || i >= size) ? (i < size || i >= size3 + size) ? (i <= size + size3 || i >= (size2 + size) + size3) ? null : this.searchAdapterHelper.getGlobalSearch().get((i - size) - size3) : this.searchAdapterHelper.getLocalServerSearch().get(i - size) : this.searchResult.get(i);
                if (obj != null) {
                    String publicUsername = obj instanceof TLRPC.User ? ((TLRPC.User) obj).username : ChatObject.getPublicUsername((TLRPC.Chat) obj);
                    if (i < size) {
                        charSequence = (CharSequence) this.searchResultNames.get(i);
                        if (charSequence != null && !TextUtils.isEmpty(publicUsername)) {
                            if (charSequence.toString().startsWith("@" + publicUsername)) {
                                r7 = charSequence;
                                charSequence = null;
                            }
                        }
                    } else if (i > size && !TextUtils.isEmpty(publicUsername)) {
                        String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
                        if (lastFoundUsername.startsWith("@")) {
                            lastFoundUsername = lastFoundUsername.substring(1);
                        }
                        try {
                            SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder();
                            spannableStringBuilder4.append((CharSequence) "@");
                            spannableStringBuilder4.append((CharSequence) publicUsername);
                            int indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(publicUsername, lastFoundUsername);
                            if (indexOfIgnoreCase != -1) {
                                int length = lastFoundUsername.length();
                                if (indexOfIgnoreCase == 0) {
                                    length++;
                                } else {
                                    indexOfIgnoreCase++;
                                }
                                spannableStringBuilder4.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4)), indexOfIgnoreCase, length + indexOfIgnoreCase, 33);
                            }
                            charSequence = null;
                            r7 = spannableStringBuilder4;
                        } catch (Exception unused) {
                            charSequence = null;
                            r7 = publicUsername;
                        }
                    }
                    j = !(obj instanceof TLRPC.User) ? ((TLRPC.User) obj).id : obj instanceof TLRPC.Chat ? -((TLRPC.Chat) obj).id : 0L;
                    if (UsersSelectActivity.this.type != 2) {
                        if (UsersSelectActivity.this.type != 0) {
                            int i3 = UsersSelectActivity.this.getMessagesController().dialogs_dict.get(j) != null ? ((TLRPC.Dialog) UsersSelectActivity.this.getMessagesController().dialogs_dict.get(j)).ttl_period : 0;
                            if (i3 > 0) {
                                SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder();
                                spannableStringBuilder5.append((CharSequence) "d");
                                spannableStringBuilder5.setSpan(new ColoredImageSpan(R.drawable.msg_mini_fireon), 0, 1, 0);
                                spannableStringBuilder5.append((CharSequence) LocaleController.formatString(R.string.AutoDeleteAfter, LocaleController.formatTTLString(i3)).toLowerCase());
                                z = true;
                                spannableStringBuilder3 = spannableStringBuilder5;
                            } else {
                                SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder();
                                spannableStringBuilder6.append((CharSequence) "d");
                                spannableStringBuilder6.setSpan(new ColoredImageSpan(R.drawable.msg_mini_fireoff), 0, 1, 0);
                                spannableStringBuilder6.append((CharSequence) LocaleController.getString(R.string.AutoDeleteDisabled));
                                spannableStringBuilder3 = spannableStringBuilder6;
                                z = false;
                            }
                            spannableStringBuilder2 = spannableStringBuilder3;
                            if (obj instanceof TLRPC.Chat) {
                                z2 = ChatObject.canUserDoAdminAction((TLRPC.Chat) obj, 13);
                                spannableStringBuilder = spannableStringBuilder3;
                                groupCreateUserCell.setAlpha(!z2 ? 1.0f : 0.5f);
                                groupCreateUserCell.setObject(obj, charSequence, spannableStringBuilder);
                                groupCreateUserCell.getStatusTextView().setTextColor(Theme.getColor(!z ? Theme.key_windowBackgroundWhiteBlueText : Theme.key_windowBackgroundWhiteGrayText));
                                if (j == 0) {
                                    groupCreateUserCell.setChecked(UsersSelectActivity.this.selectedContacts.indexOfKey(j) >= 0, false);
                                    groupCreateUserCell.setCheckBoxEnabled(true);
                                    return;
                                }
                                return;
                            }
                            z2 = true;
                            spannableStringBuilder = spannableStringBuilder2;
                            groupCreateUserCell.setAlpha(!z2 ? 1.0f : 0.5f);
                            groupCreateUserCell.setObject(obj, charSequence, spannableStringBuilder);
                            groupCreateUserCell.getStatusTextView().setTextColor(Theme.getColor(!z ? Theme.key_windowBackgroundWhiteBlueText : Theme.key_windowBackgroundWhiteGrayText));
                            if (j == 0) {
                            }
                        } else if (!this.searching) {
                            Paint.FontMetricsInt fontMetricsInt = groupCreateUserCell.getStatusTextView().getPaint().getFontMetricsInt();
                            r7 = new SpannableStringBuilder();
                            ArrayList<MessagesController.DialogFilter> arrayList = UsersSelectActivity.this.getMessagesController().dialogFilters;
                            int size4 = arrayList.size();
                            for (int i4 = 0; i4 < size4; i4++) {
                                MessagesController.DialogFilter dialogFilter = arrayList.get(i4);
                                if (dialogFilter.includesDialog(UsersSelectActivity.this.getAccountInstance(), j)) {
                                    if (r7.length() > 0) {
                                        r7.append(", ");
                                    }
                                    r7.append(MessageObject.replaceAnimatedEmoji(Emoji.replaceEmoji(new SpannableStringBuilder(dialogFilter.name), fontMetricsInt, false), dialogFilter.entities, fontMetricsInt));
                                }
                            }
                        }
                    }
                    z = false;
                    spannableStringBuilder2 = r7;
                    z2 = true;
                    spannableStringBuilder = spannableStringBuilder2;
                    groupCreateUserCell.setAlpha(!z2 ? 1.0f : 0.5f);
                    groupCreateUserCell.setObject(obj, charSequence, spannableStringBuilder);
                    groupCreateUserCell.getStatusTextView().setTextColor(Theme.getColor(!z ? Theme.key_windowBackgroundWhiteBlueText : Theme.key_windowBackgroundWhiteGrayText));
                    if (j == 0) {
                    }
                }
            } else {
                int i5 = this.usersStartRow;
                if (i < i5) {
                    String str = "non_contacts";
                    if (UsersSelectActivity.this.type == 2) {
                        if (i == 1) {
                            string = LocaleController.getString(R.string.FilterExistingChats);
                            str = "existing_chats";
                            i2 = 1;
                        } else if (i == 2 && !UsersSelectActivity.this.doNotNewChats) {
                            string = LocaleController.getString(R.string.FilterNewChats);
                            str = "new_chats";
                        } else if (i == (!UsersSelectActivity.this.doNotNewChats ? 1 : 0) + 2) {
                            string = LocaleController.getString(R.string.FilterContacts);
                            str = "contacts";
                            i2 = 4;
                        } else {
                            string = LocaleController.getString(R.string.FilterNonContacts);
                            i2 = 8;
                        }
                    } else if (UsersSelectActivity.this.isInclude) {
                        if (i == 1) {
                            string = LocaleController.getString(R.string.FilterContacts);
                            i2 = MessagesController.DIALOG_FILTER_FLAG_CONTACTS;
                            str = "contacts";
                        } else if (i == 2) {
                            string = LocaleController.getString(R.string.FilterNonContacts);
                            i2 = MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS;
                        } else if (i == 3) {
                            string = LocaleController.getString(R.string.FilterGroups);
                            i2 = MessagesController.DIALOG_FILTER_FLAG_GROUPS;
                            str = "groups";
                        } else if (i == 4) {
                            string = LocaleController.getString(R.string.FilterChannels);
                            i2 = MessagesController.DIALOG_FILTER_FLAG_CHANNELS;
                            str = "channels";
                        } else {
                            string = LocaleController.getString(R.string.FilterBots);
                            i2 = MessagesController.DIALOG_FILTER_FLAG_BOTS;
                            str = "bots";
                        }
                    } else if (i == 1) {
                        string = LocaleController.getString(R.string.FilterMuted);
                        i2 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED;
                        str = "muted";
                    } else if (i == 2) {
                        string = LocaleController.getString(R.string.FilterRead);
                        i2 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ;
                        str = "read";
                    } else {
                        string = LocaleController.getString(R.string.FilterArchived);
                        i2 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED;
                        str = "archived";
                    }
                    groupCreateUserCell.setObject(str, string, null);
                    groupCreateUserCell.setChecked((UsersSelectActivity.this.filterFlags & i2) == i2, false);
                    groupCreateUserCell.setCheckBoxEnabled(true);
                    return;
                }
                obj = this.contacts.get(i - i5);
            }
            charSequence = null;
            if (!(obj instanceof TLRPC.User)) {
            }
            if (UsersSelectActivity.this.type != 2) {
            }
            z = false;
            spannableStringBuilder2 = r7;
            z2 = true;
            spannableStringBuilder = spannableStringBuilder2;
            groupCreateUserCell.setAlpha(!z2 ? 1.0f : 0.5f);
            groupCreateUserCell.setObject(obj, charSequence, spannableStringBuilder);
            groupCreateUserCell.getStatusTextView().setTextColor(Theme.getColor(!z ? Theme.key_windowBackgroundWhiteBlueText : Theme.key_windowBackgroundWhiteGrayText));
            if (j == 0) {
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(i != 1 ? new GraySectionCell(this.context) : new GroupCreateUserCell(this.context, 1, 0, true));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof GroupCreateUserCell) {
                ((GroupCreateUserCell) view).recycle();
            }
        }

        public void searchDialogs(final String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            final boolean z = UsersSelectActivity.this.type != 2;
            final boolean z2 = UsersSelectActivity.this.type != 2;
            if (str != null) {
                DispatchQueue dispatchQueue = Utilities.searchQueue;
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.UsersSelectActivity$GroupCreateAdapter$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        UsersSelectActivity.GroupCreateAdapter.this.lambda$searchDialogs$3(str, z2, z);
                    }
                };
                this.searchRunnable = runnable;
                dispatchQueue.postRunnable(runnable, 300L);
                return;
            }
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchAdapterHelper.mergeResults(null);
            this.searchAdapterHelper.queryServerSearch(null, true, false, false, false, false, 0L, false, 0, 0);
            notifyDataSetChanged();
        }

        public void setSearching(boolean z) {
            if (this.searching == z) {
                return;
            }
            this.searching = z;
            notifyDataSetChanged();
        }
    }

    private static class ItemDecoration extends RecyclerView.ItemDecoration {
        private boolean single;
        private int skipRows;

        private ItemDecoration() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            super.getItemOffsets(rect, view, recyclerView, state);
            rect.top = 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            int width = recyclerView.getWidth();
            int childCount = recyclerView.getChildCount() - (!this.single ? 1 : 0);
            int i = 0;
            while (i < childCount) {
                View childAt = recyclerView.getChildAt(i);
                View childAt2 = i < childCount + (-1) ? recyclerView.getChildAt(i + 1) : null;
                if (recyclerView.getChildAdapterPosition(childAt) >= this.skipRows && !(childAt instanceof GraySectionCell) && !(childAt2 instanceof GraySectionCell)) {
                    float bottom = childAt.getBottom();
                    canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(72.0f), bottom, width - (LocaleController.isRTL ? AndroidUtilities.dp(72.0f) : 0), bottom, Theme.dividerPaint);
                }
                i++;
            }
        }
    }

    private class SpansContainer extends ViewGroup {
        private View addingSpan;
        private boolean animationStarted;
        private ArrayList animators;
        private AnimatorSet currentAnimation;
        private View removingSpan;

        public SpansContainer(Context context) {
            super(context);
            this.animators = new ArrayList();
        }

        public void addSpan(GroupCreateSpan groupCreateSpan, boolean z) {
            UsersSelectActivity.this.allSpans.add(groupCreateSpan);
            long uid = groupCreateSpan.getUid();
            if (uid > -9223372036854775801L) {
                UsersSelectActivity.access$508(UsersSelectActivity.this);
            }
            UsersSelectActivity.this.selectedContacts.put(uid, groupCreateSpan);
            UsersSelectActivity.this.editText.setHintVisible(false, TextUtils.isEmpty(UsersSelectActivity.this.editText.getText()));
            AnimatorSet animatorSet = this.currentAnimation;
            if (animatorSet != null && animatorSet.isRunning()) {
                this.currentAnimation.setupEndValues();
                this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            if (z) {
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.currentAnimation = animatorSet2;
                animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.UsersSelectActivity.SpansContainer.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        SpansContainer.this.addingSpan = null;
                        SpansContainer.this.currentAnimation = null;
                        SpansContainer.this.animationStarted = false;
                        UsersSelectActivity.this.editText.setAllowDrawCursor(true);
                    }
                });
                this.currentAnimation.setDuration(150L);
                this.addingSpan = groupCreateSpan;
                this.animators.clear();
                this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, (Property<View, Float>) View.SCALE_X, 0.01f, 1.0f));
                this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, (Property<View, Float>) View.SCALE_Y, 0.01f, 1.0f));
                this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, (Property<View, Float>) View.ALPHA, 0.0f, 1.0f));
            }
            addView(groupCreateSpan);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            }
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            int min;
            float f;
            int childCount = getChildCount();
            int size = View.MeasureSpec.getSize(i);
            int dp = size - AndroidUtilities.dp(26.0f);
            int dp2 = AndroidUtilities.dp(10.0f);
            int dp3 = AndroidUtilities.dp(10.0f);
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                if (childAt instanceof GroupCreateSpan) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
                    if (childAt != this.removingSpan && childAt.getMeasuredWidth() + i3 > dp) {
                        dp2 += childAt.getMeasuredHeight() + AndroidUtilities.dp(8.0f);
                        i3 = 0;
                    }
                    if (childAt.getMeasuredWidth() + i4 > dp) {
                        dp3 += childAt.getMeasuredHeight() + AndroidUtilities.dp(8.0f);
                        i4 = 0;
                    }
                    int dp4 = AndroidUtilities.dp(13.0f) + i3;
                    if (!this.animationStarted) {
                        View view = this.removingSpan;
                        if (childAt == view) {
                            childAt.setTranslationX(AndroidUtilities.dp(13.0f) + i4);
                            f = dp3;
                        } else if (view != null) {
                            float f2 = dp4;
                            if (childAt.getTranslationX() != f2) {
                                this.animators.add(ObjectAnimator.ofFloat(childAt, (Property<View, Float>) View.TRANSLATION_X, f2));
                            }
                            float f3 = dp2;
                            if (childAt.getTranslationY() != f3) {
                                this.animators.add(ObjectAnimator.ofFloat(childAt, (Property<View, Float>) View.TRANSLATION_Y, f3));
                            }
                        } else {
                            childAt.setTranslationX(dp4);
                            f = dp2;
                        }
                        childAt.setTranslationY(f);
                    }
                    if (childAt != this.removingSpan) {
                        i3 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                    }
                    i4 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                }
            }
            if (AndroidUtilities.isTablet()) {
                min = AndroidUtilities.dp(372.0f);
            } else {
                Point point = AndroidUtilities.displaySize;
                min = Math.min(point.x, point.y) - AndroidUtilities.dp(158.0f);
            }
            int i6 = min / 3;
            if (dp - i3 < i6) {
                dp2 += AndroidUtilities.dp(40.0f);
                i3 = 0;
            }
            if (dp - i4 < i6) {
                dp3 += AndroidUtilities.dp(40.0f);
            }
            UsersSelectActivity.this.editText.measure(View.MeasureSpec.makeMeasureSpec(dp - i3, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
            if (!this.animationStarted) {
                int dp5 = dp3 + AndroidUtilities.dp(42.0f);
                int dp6 = i3 + AndroidUtilities.dp(16.0f);
                UsersSelectActivity.this.fieldY = dp2;
                if (this.currentAnimation != null) {
                    int dp7 = dp2 + AndroidUtilities.dp(42.0f);
                    if (UsersSelectActivity.this.containerHeight != dp7) {
                        this.animators.add(ObjectAnimator.ofInt(UsersSelectActivity.this, "containerHeight", dp7));
                    }
                    float f4 = dp6;
                    if (UsersSelectActivity.this.editText.getTranslationX() != f4) {
                        this.animators.add(ObjectAnimator.ofFloat(UsersSelectActivity.this.editText, (Property<EditTextBoldCursor, Float>) View.TRANSLATION_X, f4));
                    }
                    if (UsersSelectActivity.this.editText.getTranslationY() != UsersSelectActivity.this.fieldY) {
                        this.animators.add(ObjectAnimator.ofFloat(UsersSelectActivity.this.editText, (Property<EditTextBoldCursor, Float>) View.TRANSLATION_Y, UsersSelectActivity.this.fieldY));
                    }
                    UsersSelectActivity.this.editText.setAllowDrawCursor(false);
                    this.currentAnimation.playTogether(this.animators);
                    this.currentAnimation.start();
                    this.animationStarted = true;
                } else {
                    UsersSelectActivity.this.containerHeight = dp5;
                    UsersSelectActivity.this.editText.setTranslationX(dp6);
                    UsersSelectActivity.this.editText.setTranslationY(UsersSelectActivity.this.fieldY);
                }
            } else if (this.currentAnimation != null && !UsersSelectActivity.this.ignoreScrollEvent && this.removingSpan == null) {
                UsersSelectActivity.this.editText.bringPointIntoView(UsersSelectActivity.this.editText.getSelectionStart());
            }
            setMeasuredDimension(size, UsersSelectActivity.this.containerHeight);
        }

        public void removeSpan(final GroupCreateSpan groupCreateSpan) {
            UsersSelectActivity.this.ignoreScrollEvent = true;
            long uid = groupCreateSpan.getUid();
            if (uid > -9223372036854775801L) {
                UsersSelectActivity.access$510(UsersSelectActivity.this);
            }
            UsersSelectActivity.this.selectedContacts.remove(uid);
            UsersSelectActivity.this.allSpans.remove(groupCreateSpan);
            groupCreateSpan.setOnClickListener(null);
            AnimatorSet animatorSet = this.currentAnimation;
            if (animatorSet != null) {
                animatorSet.setupEndValues();
                this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.currentAnimation = animatorSet2;
            animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.UsersSelectActivity.SpansContainer.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SpansContainer.this.removeView(groupCreateSpan);
                    SpansContainer.this.removingSpan = null;
                    SpansContainer.this.currentAnimation = null;
                    SpansContainer.this.animationStarted = false;
                    UsersSelectActivity.this.editText.setAllowDrawCursor(true);
                    if (UsersSelectActivity.this.allSpans.isEmpty()) {
                        UsersSelectActivity.this.editText.setHintVisible(true, true);
                    }
                }
            });
            this.currentAnimation.setDuration(150L);
            this.removingSpan = groupCreateSpan;
            this.animators.clear();
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, (Property<View, Float>) View.SCALE_X, 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, (Property<View, Float>) View.SCALE_Y, 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, (Property<View, Float>) View.ALPHA, 1.0f, 0.0f));
            requestLayout();
        }
    }

    public UsersSelectActivity(int i) {
        this.selectedContacts = new LongSparseArray();
        this.allSpans = new ArrayList();
        this.type = i;
        this.allowSelf = i != 1;
    }

    public UsersSelectActivity(boolean z, ArrayList arrayList, int i) {
        this.selectedContacts = new LongSparseArray();
        this.allSpans = new ArrayList();
        this.isInclude = z;
        this.filterFlags = i;
        this.initialIds = arrayList;
        this.type = 0;
        this.allowSelf = true;
    }

    static /* synthetic */ int access$2172(UsersSelectActivity usersSelectActivity, int i) {
        int i2 = i & usersSelectActivity.filterFlags;
        usersSelectActivity.filterFlags = i2;
        return i2;
    }

    static /* synthetic */ int access$508(UsersSelectActivity usersSelectActivity) {
        int i = usersSelectActivity.selectedCount;
        usersSelectActivity.selectedCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$510(UsersSelectActivity usersSelectActivity) {
        int i = usersSelectActivity.selectedCount;
        usersSelectActivity.selectedCount = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void checkVisibleRows() {
        long j;
        char c;
        int childCount = this.listView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt instanceof GroupCreateUserCell) {
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) childAt;
                Object object = groupCreateUserCell.getObject();
                if (object instanceof String) {
                    String str = (String) object;
                    switch (str.hashCode()) {
                        case -1716307998:
                            if (str.equals("archived")) {
                                c = '\t';
                                break;
                            }
                            c = 65535;
                            break;
                        case -1237460524:
                            if (str.equals("groups")) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case -1197490811:
                            if (str.equals("non_contacts")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case -567451565:
                            if (str.equals("contacts")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case -268161860:
                            if (str.equals("new_chats")) {
                                c = '\b';
                                break;
                            }
                            c = 65535;
                            break;
                        case 3029900:
                            if (str.equals("bots")) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case 3496342:
                            if (str.equals("read")) {
                                c = 6;
                                break;
                            }
                            c = 65535;
                            break;
                        case 104264043:
                            if (str.equals("muted")) {
                                c = 5;
                                break;
                            }
                            c = 65535;
                            break;
                        case 151051367:
                            if (str.equals("existing_chats")) {
                                c = 7;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1432626128:
                            if (str.equals("channels")) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                            j = Long.MIN_VALUE;
                            break;
                        case 1:
                            j = -9223372036854775807L;
                            break;
                        case 2:
                            j = -9223372036854775806L;
                            break;
                        case 3:
                            j = -9223372036854775805L;
                            break;
                        case 4:
                            j = -9223372036854775804L;
                            break;
                        case 5:
                            j = -9223372036854775803L;
                            break;
                        case 6:
                            j = -9223372036854775802L;
                            break;
                        case 7:
                        case '\b':
                            j = -9223372036854775800L;
                            break;
                        default:
                            j = -9223372036854775801L;
                            break;
                    }
                } else {
                    j = object instanceof TLRPC.User ? ((TLRPC.User) object).id : object instanceof TLRPC.Chat ? -((TLRPC.Chat) object).id : 0L;
                }
                if (j != 0) {
                    groupCreateUserCell.setChecked(this.selectedContacts.indexOfKey(j) >= 0, true);
                    groupCreateUserCell.setCheckBoxEnabled(true);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeSearch() {
        this.searching = false;
        this.searchWas = false;
        this.adapter.setSearching(false);
        this.adapter.searchDialogs(null);
        this.listView.setFastScrollVisible(true);
        this.listView.setVerticalScrollBarEnabled(false);
        this.emptyView.title.setText(LocaleController.getString(R.string.NoContacts));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view) {
        this.editText.clearFocus();
        this.editText.requestFocus();
        AndroidUtilities.showKeyboard(this.editText);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$1(Context context, View view, int i) {
        long j;
        if (view instanceof GroupCreateUserCell) {
            GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) view;
            Object object = groupCreateUserCell.getObject();
            boolean z = object instanceof String;
            if (z) {
                j = -9223372036854775807L;
                int i2 = 4;
                if (this.type == 2) {
                    if (i == 1) {
                        j = -9223372036854775800L;
                        i2 = 1;
                    } else if (i != 2 || this.doNotNewChats) {
                        if (i != (!this.doNotNewChats ? 1 : 0) + 2) {
                            i2 = 8;
                        }
                        j = Long.MIN_VALUE;
                    } else {
                        j = -9223372036854775799L;
                        i2 = 2;
                    }
                    this.filterFlags = !groupCreateUserCell.isChecked() ? this.filterFlags & (i2 ^ (-1)) : this.filterFlags | i2;
                } else {
                    if (this.isInclude) {
                        if (i == 1) {
                            i2 = MessagesController.DIALOG_FILTER_FLAG_CONTACTS;
                            j = Long.MIN_VALUE;
                        } else if (i == 2) {
                            i2 = MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS;
                        } else if (i == 3) {
                            i2 = MessagesController.DIALOG_FILTER_FLAG_GROUPS;
                            j = -9223372036854775806L;
                        } else if (i == 4) {
                            i2 = MessagesController.DIALOG_FILTER_FLAG_CHANNELS;
                            j = -9223372036854775805L;
                        } else {
                            i2 = MessagesController.DIALOG_FILTER_FLAG_BOTS;
                            j = -9223372036854775804L;
                        }
                    } else if (i == 1) {
                        i2 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED;
                        j = -9223372036854775803L;
                    } else if (i == 2) {
                        i2 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ;
                        j = -9223372036854775802L;
                    } else {
                        i2 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED;
                        j = -9223372036854775801L;
                    }
                    this.filterFlags = !groupCreateUserCell.isChecked() ? this.filterFlags & (i2 ^ (-1)) : this.filterFlags | i2;
                }
            } else if (object instanceof TLRPC.User) {
                j = ((TLRPC.User) object).id;
            } else {
                if (!(object instanceof TLRPC.Chat)) {
                    return;
                }
                TLRPC.Chat chat = (TLRPC.Chat) object;
                j = -chat.id;
                if (this.type == 1 && !ChatObject.canUserDoAdminAction(chat, 13)) {
                    BulletinFactory.of(this).createErrorBulletin(LocaleController.getString(R.string.NeedAdminRightForSetAutoDeleteTimer)).show();
                    return;
                }
            }
            boolean z2 = this.selectedContacts.indexOfKey(j) >= 0;
            if (z2) {
                this.spansContainer.removeSpan((GroupCreateSpan) this.selectedContacts.get(j));
            } else if ((!z && !getUserConfig().isPremium() && this.selectedCount >= MessagesController.getInstance(this.currentAccount).dialogFiltersChatsLimitDefault) || this.selectedCount >= MessagesController.getInstance(this.currentAccount).dialogFiltersChatsLimitPremium) {
                LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(this, context, 4, this.currentAccount, null);
                limitReachedBottomSheet.setCurrentValue(this.selectedCount);
                showDialog(limitReachedBottomSheet);
                return;
            } else {
                if (object instanceof TLRPC.User) {
                    MessagesController.getInstance(this.currentAccount).putUser((TLRPC.User) object, !this.searching);
                } else if (object instanceof TLRPC.Chat) {
                    MessagesController.getInstance(this.currentAccount).putChat((TLRPC.Chat) object, !this.searching);
                }
                GroupCreateSpan groupCreateSpan = new GroupCreateSpan(this.editText.getContext(), object);
                this.spansContainer.addSpan(groupCreateSpan, true);
                groupCreateSpan.setOnClickListener(this);
            }
            updateHint();
            if (this.searching || this.searchWas) {
                AndroidUtilities.showKeyboard(this.editText);
            } else {
                groupCreateUserCell.setChecked(!z2, true);
            }
            if (this.editText.length() > 0) {
                this.editText.setText((CharSequence) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(View view) {
        onDonePressed(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$3() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof GroupCreateUserCell) {
                    ((GroupCreateUserCell) childAt).update(0);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onDonePressed(boolean z) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.selectedContacts.size(); i++) {
            if (this.selectedContacts.keyAt(i) > -9223372036854775799L) {
                arrayList.add(Long.valueOf(this.selectedContacts.keyAt(i)));
            }
        }
        FilterUsersActivityDelegate filterUsersActivityDelegate = this.delegate;
        if (filterUsersActivityDelegate != null) {
            filterUsersActivityDelegate.didSelectChats(arrayList, this.filterFlags);
        }
        lambda$onBackPressed$321();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHint() {
        AnimatedTextView subtitleTextView;
        int i;
        AnimatedTextView subtitleTextView2;
        int i2;
        int i3 = this.type;
        if (i3 == 0) {
            int i4 = getUserConfig().isPremium() ? getMessagesController().dialogFiltersChatsLimitPremium : getMessagesController().dialogFiltersChatsLimitDefault;
            int i5 = this.selectedCount;
            if (i5 == 0) {
                this.actionBar.setSubtitle(LocaleController.formatString("MembersCountZero", R.string.MembersCountZero, LocaleController.formatPluralString("Chats", i4, new Object[0])));
                return;
            } else {
                this.actionBar.setSubtitle(String.format(LocaleController.getPluralString("MembersCountSelected", i5), Integer.valueOf(this.selectedCount), Integer.valueOf(i4)));
                return;
            }
        }
        if (i3 == 1) {
            this.actionBar.setTitle("");
            this.actionBar.setSubtitle("");
            if (this.selectedCount == 0) {
                this.animatedAvatarContainer.getTitle().setText(LocaleController.getString(R.string.SelectChats), true);
                if (this.ttlPeriod > 0) {
                    subtitleTextView2 = this.animatedAvatarContainer.getSubtitleTextView();
                    i2 = R.string.SelectChatsForAutoDelete;
                } else {
                    subtitleTextView2 = this.animatedAvatarContainer.getSubtitleTextView();
                    i2 = R.string.SelectChatsForDisableAutoDelete;
                }
                subtitleTextView2.setText(LocaleController.getString(i2), true);
                return;
            }
            AnimatedTextView title = this.animatedAvatarContainer.getTitle();
            int i6 = this.selectedCount;
            title.setText(LocaleController.formatPluralString("Chats", i6, Integer.valueOf(i6)));
            if (this.ttlPeriod > 0) {
                subtitleTextView = this.animatedAvatarContainer.getSubtitleTextView();
                i = R.string.SelectChatsForAutoDelete2;
            } else {
                subtitleTextView = this.animatedAvatarContainer.getSubtitleTextView();
                i = R.string.SelectChatsForDisableAutoDelete2;
            }
            subtitleTextView.setText(LocaleController.getString(i));
        }
    }

    public UsersSelectActivity asPrivateChats() {
        this.type = 2;
        this.allowSelf = false;
        return this;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        ActionBar actionBar;
        int i;
        int i2;
        Property property;
        this.searching = false;
        this.searchWas = false;
        this.allSpans.clear();
        this.selectedContacts.clear();
        this.currentDeletingSpan = null;
        int i3 = 1;
        if (this.type == 1) {
            AnimatedAvatarContainer animatedAvatarContainer = new AnimatedAvatarContainer(getContext());
            this.animatedAvatarContainer = animatedAvatarContainer;
            ActionBar actionBar2 = this.actionBar;
            boolean z = LocaleController.isRTL;
            actionBar2.addView(animatedAvatarContainer, LayoutHelper.createFrame(-1, -1.0f, 0, z ? 0.0f : 64.0f, 0.0f, z ? 64.0f : 0.0f, 0.0f));
            this.actionBar.setAllowOverlayTitle(false);
        }
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        int i4 = this.type;
        if (i4 == 0 || i4 == 2) {
            if (this.isInclude) {
                actionBar = this.actionBar;
                i = R.string.FilterAlwaysShow;
            } else {
                actionBar = this.actionBar;
                i = R.string.FilterNeverShow;
            }
            actionBar.setTitle(LocaleController.getString(i));
        } else if (i4 == 1) {
            updateHint();
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.UsersSelectActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i5) {
                if (i5 == -1) {
                    UsersSelectActivity.this.lambda$onBackPressed$321();
                } else if (i5 == 1) {
                    UsersSelectActivity.this.onDonePressed(true);
                }
            }
        });
        ViewGroup viewGroup = new ViewGroup(context) { // from class: org.telegram.ui.UsersSelectActivity.2
            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                boolean drawChild = super.drawChild(canvas, view, j);
                if (view == UsersSelectActivity.this.listView || view == UsersSelectActivity.this.emptyView) {
                    ((BaseFragment) UsersSelectActivity.this).parentLayout.drawHeaderShadow(canvas, UsersSelectActivity.this.scrollView.getMeasuredHeight());
                }
                return drawChild;
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i5, int i6, int i7, int i8) {
                UsersSelectActivity.this.scrollView.layout(0, 0, UsersSelectActivity.this.scrollView.getMeasuredWidth(), UsersSelectActivity.this.scrollView.getMeasuredHeight());
                UsersSelectActivity.this.listView.layout(0, UsersSelectActivity.this.scrollView.getMeasuredHeight(), UsersSelectActivity.this.listView.getMeasuredWidth(), UsersSelectActivity.this.scrollView.getMeasuredHeight() + UsersSelectActivity.this.listView.getMeasuredHeight());
                UsersSelectActivity.this.emptyView.layout(0, UsersSelectActivity.this.scrollView.getMeasuredHeight(), UsersSelectActivity.this.emptyView.getMeasuredWidth(), UsersSelectActivity.this.scrollView.getMeasuredHeight() + UsersSelectActivity.this.emptyView.getMeasuredHeight());
                UsersSelectActivity.this.progressView.layout(0, UsersSelectActivity.this.scrollView.getMeasuredHeight(), UsersSelectActivity.this.emptyView.getMeasuredWidth(), UsersSelectActivity.this.scrollView.getMeasuredHeight() + UsersSelectActivity.this.progressView.getMeasuredHeight());
                if (UsersSelectActivity.this.floatingButton != null) {
                    int dp = LocaleController.isRTL ? AndroidUtilities.dp(14.0f) : ((i7 - i5) - AndroidUtilities.dp(14.0f)) - UsersSelectActivity.this.floatingButton.getMeasuredWidth();
                    int dp2 = ((i8 - i6) - AndroidUtilities.dp(14.0f)) - UsersSelectActivity.this.floatingButton.getMeasuredHeight();
                    UsersSelectActivity.this.floatingButton.layout(dp, dp2, UsersSelectActivity.this.floatingButton.getMeasuredWidth() + dp, UsersSelectActivity.this.floatingButton.getMeasuredHeight() + dp2);
                }
            }

            @Override // android.view.View
            protected void onMeasure(int i5, int i6) {
                int size = View.MeasureSpec.getSize(i5);
                int size2 = View.MeasureSpec.getSize(i6);
                setMeasuredDimension(size, size2);
                UsersSelectActivity.this.scrollView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((AndroidUtilities.isTablet() || size2 > size) ? AndroidUtilities.dp(144.0f) : AndroidUtilities.dp(56.0f), Integer.MIN_VALUE));
                UsersSelectActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - UsersSelectActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                UsersSelectActivity.this.emptyView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - UsersSelectActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                UsersSelectActivity.this.progressView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - UsersSelectActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                if (UsersSelectActivity.this.floatingButton != null) {
                    int dp = AndroidUtilities.dp(Build.VERSION.SDK_INT < 21 ? 60.0f : 56.0f);
                    UsersSelectActivity.this.floatingButton.measure(View.MeasureSpec.makeMeasureSpec(dp, 1073741824), View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
                }
            }
        };
        this.fragmentView = viewGroup;
        ScrollView scrollView = new ScrollView(context) { // from class: org.telegram.ui.UsersSelectActivity.3
            @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
            public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z2) {
                if (UsersSelectActivity.this.ignoreScrollEvent) {
                    UsersSelectActivity.this.ignoreScrollEvent = false;
                    return false;
                }
                rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
                rect.top += UsersSelectActivity.this.fieldY + AndroidUtilities.dp(20.0f);
                rect.bottom += UsersSelectActivity.this.fieldY + AndroidUtilities.dp(50.0f);
                return super.requestChildRectangleOnScreen(view, rect, z2);
            }
        };
        this.scrollView = scrollView;
        scrollView.setVerticalScrollBarEnabled(false);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor(Theme.key_windowBackgroundWhite));
        viewGroup.addView(this.scrollView);
        SpansContainer spansContainer = new SpansContainer(context);
        this.spansContainer = spansContainer;
        this.scrollView.addView(spansContainer, LayoutHelper.createFrame(-1, -2.0f));
        this.spansContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.UsersSelectActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                UsersSelectActivity.this.lambda$createView$0(view);
            }
        });
        EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context) { // from class: org.telegram.ui.UsersSelectActivity.4
            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (UsersSelectActivity.this.currentDeletingSpan != null) {
                    UsersSelectActivity.this.currentDeletingSpan.cancelDeleteAnimation();
                    UsersSelectActivity.this.currentDeletingSpan = null;
                }
                if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
                    clearFocus();
                    requestFocus();
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.editText = editTextBoldCursor;
        editTextBoldCursor.setTextSize(1, 16.0f);
        this.editText.setHintColor(Theme.getColor(Theme.key_groupcreate_hintText));
        this.editText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.editText.setCursorColor(Theme.getColor(Theme.key_groupcreate_cursor));
        this.editText.setCursorWidth(1.5f);
        this.editText.setInputType(655536);
        this.editText.setSingleLine(true);
        this.editText.setBackgroundDrawable(null);
        this.editText.setVerticalScrollBarEnabled(false);
        this.editText.setHorizontalScrollBarEnabled(false);
        this.editText.setTextIsSelectable(false);
        this.editText.setPadding(0, 0, 0, 0);
        this.editText.setImeOptions(268435462);
        int i5 = 5;
        this.editText.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        this.spansContainer.addView(this.editText);
        this.editText.setHintText(LocaleController.getString(R.string.SearchForPeopleAndGroups));
        this.editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: org.telegram.ui.UsersSelectActivity.5
            @Override // android.view.ActionMode.Callback
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override // android.view.ActionMode.Callback
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override // android.view.ActionMode.Callback
            public void onDestroyActionMode(ActionMode actionMode) {
            }

            @Override // android.view.ActionMode.Callback
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
        });
        this.editText.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.UsersSelectActivity.6
            private boolean wasEmpty;

            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int i6, KeyEvent keyEvent) {
                UsersSelectActivity usersSelectActivity;
                int i7;
                int i8;
                if (i6 == 67) {
                    if (keyEvent.getAction() == 0) {
                        this.wasEmpty = UsersSelectActivity.this.editText.length() == 0;
                    } else if (keyEvent.getAction() == 1 && this.wasEmpty && !UsersSelectActivity.this.allSpans.isEmpty()) {
                        GroupCreateSpan groupCreateSpan = (GroupCreateSpan) UsersSelectActivity.this.allSpans.get(UsersSelectActivity.this.allSpans.size() - 1);
                        UsersSelectActivity.this.spansContainer.removeSpan(groupCreateSpan);
                        int i9 = UsersSelectActivity.this.type;
                        long uid = groupCreateSpan.getUid();
                        if (i9 == 2) {
                            if (uid == -9223372036854775800L) {
                                usersSelectActivity = UsersSelectActivity.this;
                                i8 = -2;
                            } else if (groupCreateSpan.getUid() == -9223372036854775799L) {
                                usersSelectActivity = UsersSelectActivity.this;
                                i8 = -3;
                            } else {
                                if (groupCreateSpan.getUid() != Long.MIN_VALUE) {
                                    if (groupCreateSpan.getUid() == -9223372036854775807L) {
                                        usersSelectActivity = UsersSelectActivity.this;
                                        i8 = -9;
                                    }
                                    UsersSelectActivity.this.updateHint();
                                    UsersSelectActivity.this.checkVisibleRows();
                                    return true;
                                }
                                usersSelectActivity = UsersSelectActivity.this;
                                i8 = -5;
                            }
                            UsersSelectActivity.access$2172(usersSelectActivity, i8);
                            UsersSelectActivity.this.updateHint();
                            UsersSelectActivity.this.checkVisibleRows();
                            return true;
                        }
                        if (uid == Long.MIN_VALUE) {
                            usersSelectActivity = UsersSelectActivity.this;
                            i7 = MessagesController.DIALOG_FILTER_FLAG_CONTACTS;
                        } else if (groupCreateSpan.getUid() == -9223372036854775807L) {
                            usersSelectActivity = UsersSelectActivity.this;
                            i7 = MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS;
                        } else if (groupCreateSpan.getUid() == -9223372036854775806L) {
                            usersSelectActivity = UsersSelectActivity.this;
                            i7 = MessagesController.DIALOG_FILTER_FLAG_GROUPS;
                        } else if (groupCreateSpan.getUid() == -9223372036854775805L) {
                            usersSelectActivity = UsersSelectActivity.this;
                            i7 = MessagesController.DIALOG_FILTER_FLAG_CHANNELS;
                        } else if (groupCreateSpan.getUid() == -9223372036854775804L) {
                            usersSelectActivity = UsersSelectActivity.this;
                            i7 = MessagesController.DIALOG_FILTER_FLAG_BOTS;
                        } else if (groupCreateSpan.getUid() == -9223372036854775803L) {
                            usersSelectActivity = UsersSelectActivity.this;
                            i7 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED;
                        } else {
                            if (groupCreateSpan.getUid() != -9223372036854775802L) {
                                if (groupCreateSpan.getUid() == -9223372036854775801L) {
                                    usersSelectActivity = UsersSelectActivity.this;
                                    i7 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED;
                                }
                                UsersSelectActivity.this.updateHint();
                                UsersSelectActivity.this.checkVisibleRows();
                                return true;
                            }
                            usersSelectActivity = UsersSelectActivity.this;
                            i7 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ;
                        }
                        i8 = i7 ^ (-1);
                        UsersSelectActivity.access$2172(usersSelectActivity, i8);
                        UsersSelectActivity.this.updateHint();
                        UsersSelectActivity.this.checkVisibleRows();
                        return true;
                    }
                }
                return false;
            }
        });
        this.editText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.UsersSelectActivity.7
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (UsersSelectActivity.this.editText.length() == 0) {
                    UsersSelectActivity.this.closeSearch();
                    return;
                }
                if (!UsersSelectActivity.this.adapter.searching) {
                    UsersSelectActivity.this.searching = true;
                    UsersSelectActivity.this.searchWas = true;
                    UsersSelectActivity.this.adapter.setSearching(true);
                    UsersSelectActivity.this.listView.setFastScrollVisible(false);
                    UsersSelectActivity.this.listView.setVerticalScrollBarEnabled(true);
                    UsersSelectActivity.this.emptyView.title.setText(LocaleController.getString(R.string.NoResult));
                }
                UsersSelectActivity.this.emptyView.showProgress(true);
                UsersSelectActivity.this.adapter.searchDialogs(UsersSelectActivity.this.editText.getText().toString());
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i6, int i7, int i8) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i6, int i7, int i8) {
            }
        });
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
        this.progressView = flickerLoadingView;
        flickerLoadingView.setViewType(10);
        this.progressView.showDate(false);
        this.progressView.setItemsCount(3);
        FlickerLoadingView flickerLoadingView2 = this.progressView;
        int i6 = Theme.key_actionBarDefaultSubmenuBackground;
        int i7 = Theme.key_listSelector;
        flickerLoadingView2.setColors(i6, i7, i7);
        viewGroup.addView(this.progressView);
        StickerEmptyView stickerEmptyView = new StickerEmptyView(context, this.progressView, i3) { // from class: org.telegram.ui.UsersSelectActivity.8
            @Override // org.telegram.ui.Components.StickerEmptyView, android.view.View
            public void setVisibility(int i8) {
                super.setVisibility(i8);
                if (i8 != 0) {
                    showProgress(false, false);
                }
            }
        };
        this.emptyView = stickerEmptyView;
        stickerEmptyView.showProgress(ContactsController.getInstance(this.currentAccount).isLoadingContacts());
        this.emptyView.title.setText(LocaleController.getString(R.string.NoContacts));
        viewGroup.addView(this.emptyView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setFastScrollEnabled(0);
        this.listView.setEmptyView(this.emptyView);
        RecyclerListView recyclerListView2 = this.listView;
        GroupCreateAdapter groupCreateAdapter = new GroupCreateAdapter(context);
        this.adapter = groupCreateAdapter;
        recyclerListView2.setAdapter(groupCreateAdapter);
        this.listView.setLayoutManager(linearLayoutManager);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        this.listView.addItemDecoration(new ItemDecoration());
        viewGroup.addView(this.listView);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.UsersSelectActivity$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i8) {
                UsersSelectActivity.this.lambda$createView$1(context, view, i8);
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.UsersSelectActivity.9
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i8) {
                if (i8 == 1) {
                    AndroidUtilities.hideKeyboard(UsersSelectActivity.this.editText);
                }
            }
        });
        ImageView imageView = new ImageView(context);
        this.floatingButton = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 21) {
            Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
        this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
        this.floatingButton.setImageResource(R.drawable.floating_check);
        if (i8 >= 21) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            int[] iArr = {android.R.attr.state_pressed};
            ImageView imageView2 = this.floatingButton;
            property = View.TRANSLATION_Z;
            stateListAnimator.addState(iArr, ObjectAnimator.ofFloat(imageView2, (Property<ImageView, Float>) property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
            this.floatingButton.setStateListAnimator(stateListAnimator);
            this.floatingButton.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.UsersSelectActivity.10
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        viewGroup.addView(this.floatingButton);
        this.floatingButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.UsersSelectActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                UsersSelectActivity.this.lambda$createView$2(view);
            }
        });
        this.floatingButton.setContentDescription(LocaleController.getString(R.string.Next));
        if (this.isInclude) {
            i2 = 1;
        } else {
            i2 = 1;
            i5 = 3;
        }
        while (i2 <= i5) {
            String str = "non_contacts";
            int i9 = 4;
            if (this.type == 2) {
                if (i2 == 1) {
                    str = "existing_chats";
                    i9 = 1;
                } else if (i2 != 2 || this.doNotNewChats) {
                    if (i2 != (!this.doNotNewChats ? 1 : 0) + 2) {
                        i9 = 8;
                    }
                    str = "contacts";
                } else {
                    str = "new_chats";
                    i9 = 2;
                }
            } else if (this.isInclude) {
                if (i2 == 1) {
                    i9 = MessagesController.DIALOG_FILTER_FLAG_CONTACTS;
                    str = "contacts";
                } else if (i2 == 2) {
                    i9 = MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS;
                } else if (i2 == 3) {
                    i9 = MessagesController.DIALOG_FILTER_FLAG_GROUPS;
                    str = "groups";
                } else if (i2 == 4) {
                    i9 = MessagesController.DIALOG_FILTER_FLAG_CHANNELS;
                    str = "channels";
                } else {
                    i9 = MessagesController.DIALOG_FILTER_FLAG_BOTS;
                    str = "bots";
                }
            } else if (i2 == 1) {
                i9 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED;
                str = "muted";
            } else if (i2 == 2) {
                i9 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ;
                str = "read";
            } else {
                i9 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED;
                str = "archived";
            }
            if ((this.filterFlags & i9) != 0) {
                GroupCreateSpan groupCreateSpan = new GroupCreateSpan(this.editText.getContext(), str);
                this.spansContainer.addSpan(groupCreateSpan, false);
                groupCreateSpan.setOnClickListener(this);
            }
            i2++;
        }
        ArrayList arrayList = this.initialIds;
        if (arrayList != null && !arrayList.isEmpty()) {
            int size = this.initialIds.size();
            for (int i10 = 0; i10 < size; i10++) {
                Long l = (Long) this.initialIds.get(i10);
                Object user = l.longValue() > 0 ? getMessagesController().getUser(l) : getMessagesController().getChat(Long.valueOf(-l.longValue()));
                if (user != null) {
                    GroupCreateSpan groupCreateSpan2 = new GroupCreateSpan(this.editText.getContext(), user);
                    this.spansContainer.addSpan(groupCreateSpan2, false);
                    groupCreateSpan2.setOnClickListener(this);
                }
            }
        }
        updateHint();
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.contactsDidLoad) {
            StickerEmptyView stickerEmptyView = this.emptyView;
            if (stickerEmptyView != null) {
                stickerEmptyView.showProgress(false);
            }
            GroupCreateAdapter groupCreateAdapter = this.adapter;
            if (groupCreateAdapter != null) {
                groupCreateAdapter.notifyDataSetChanged();
                return;
            }
            return;
        }
        if (i != NotificationCenter.updateInterfaces) {
            if (i == NotificationCenter.chatDidCreated) {
                removeSelfFromStack();
            }
        } else if (this.listView != null) {
            int intValue = ((Integer) objArr[0]).intValue();
            int childCount = this.listView.getChildCount();
            if ((MessagesController.UPDATE_MASK_AVATAR & intValue) == 0 && (MessagesController.UPDATE_MASK_NAME & intValue) == 0 && (MessagesController.UPDATE_MASK_STATUS & intValue) == 0) {
                return;
            }
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = this.listView.getChildAt(i3);
                if (childAt instanceof GroupCreateUserCell) {
                    ((GroupCreateUserCell) childAt).update(intValue);
                }
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.UsersSelectActivity$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                UsersSelectActivity.this.lambda$getThemeDescriptions$3();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        View view = this.fragmentView;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(view, i, null, null, null, null, i2));
        ActionBar actionBar = this.actionBar;
        int i3 = ThemeDescription.FLAG_BACKGROUND;
        int i4 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i3, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollActive));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollInactive));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_groupcreate_hintText));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, Theme.key_groupcreate_cursor));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_graySectionText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, Theme.key_graySection));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_groupcreate_sectionText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_checkbox));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_checkboxDisabled));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_checkboxCheck));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{GroupCreateUserCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{GroupCreateUserCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GroupCreateUserCell.class}, null, Theme.avatarDrawables, null, Theme.key_avatar_text));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        int i5 = Theme.key_avatar_backgroundBlue;
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, i5));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_groupcreate_spanBackground));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_groupcreate_spanText));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_groupcreate_spanDelete));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, i5));
        return arrayList;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int i;
        int i2;
        int i3;
        GroupCreateSpan groupCreateSpan = (GroupCreateSpan) view;
        if (!groupCreateSpan.isDeleting()) {
            GroupCreateSpan groupCreateSpan2 = this.currentDeletingSpan;
            if (groupCreateSpan2 != null) {
                groupCreateSpan2.cancelDeleteAnimation();
            }
            this.currentDeletingSpan = groupCreateSpan;
            groupCreateSpan.startDeleteAnimation();
            return;
        }
        this.currentDeletingSpan = null;
        this.spansContainer.removeSpan(groupCreateSpan);
        if (this.type == 2) {
            if (groupCreateSpan.getUid() == -9223372036854775800L) {
                i3 = this.filterFlags & (-2);
            } else if (groupCreateSpan.getUid() == -9223372036854775799L) {
                i3 = this.filterFlags & (-3);
            } else {
                if (groupCreateSpan.getUid() != Long.MIN_VALUE) {
                    if (groupCreateSpan.getUid() == -9223372036854775807L) {
                        i3 = this.filterFlags & (-9);
                    }
                    updateHint();
                    checkVisibleRows();
                }
                i3 = this.filterFlags & (-5);
            }
            this.filterFlags = i3;
            updateHint();
            checkVisibleRows();
        }
        if (groupCreateSpan.getUid() == Long.MIN_VALUE) {
            i = this.filterFlags;
            i2 = MessagesController.DIALOG_FILTER_FLAG_CONTACTS;
        } else if (groupCreateSpan.getUid() == -9223372036854775807L) {
            i = this.filterFlags;
            i2 = MessagesController.DIALOG_FILTER_FLAG_NON_CONTACTS;
        } else if (groupCreateSpan.getUid() == -9223372036854775806L) {
            i = this.filterFlags;
            i2 = MessagesController.DIALOG_FILTER_FLAG_GROUPS;
        } else if (groupCreateSpan.getUid() == -9223372036854775805L) {
            i = this.filterFlags;
            i2 = MessagesController.DIALOG_FILTER_FLAG_CHANNELS;
        } else if (groupCreateSpan.getUid() == -9223372036854775804L) {
            i = this.filterFlags;
            i2 = MessagesController.DIALOG_FILTER_FLAG_BOTS;
        } else if (groupCreateSpan.getUid() == -9223372036854775803L) {
            i = this.filterFlags;
            i2 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_MUTED;
        } else {
            if (groupCreateSpan.getUid() != -9223372036854775802L) {
                if (groupCreateSpan.getUid() == -9223372036854775801L) {
                    i = this.filterFlags;
                    i2 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED;
                }
                updateHint();
                checkVisibleRows();
            }
            i = this.filterFlags;
            i2 = MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ;
        }
        i3 = i & (i2 ^ (-1));
        this.filterFlags = i3;
        updateHint();
        checkVisibleRows();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatDidCreated);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatDidCreated);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        EditTextBoldCursor editTextBoldCursor = this.editText;
        if (editTextBoldCursor != null) {
            editTextBoldCursor.requestFocus();
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
    }

    public void setDelegate(FilterUsersActivityDelegate filterUsersActivityDelegate) {
        this.delegate = filterUsersActivityDelegate;
    }

    public void setTtlPeriod(int i) {
        this.ttlPeriod = i;
    }
}
