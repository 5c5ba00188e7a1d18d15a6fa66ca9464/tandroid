package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.GroupCreateSectionCell;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.GroupCreateDividerItemDecoration;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PermanentLinkBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
import org.telegram.ui.GroupCreateActivity;

/* loaded from: classes4.dex */
public class GroupCreateActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, View.OnClickListener {
    private GroupCreateAdapter adapter;
    private boolean addToGroup;
    private ArrayList allSpans;
    private boolean allowMiniapps;
    private boolean allowPremium;
    private long channelId;
    private int chatAddType;
    private long chatId;
    private int chatType;
    private int containerHeight;
    private AnimatorSet currentAnimation;
    private GroupCreateSpan currentDeletingSpan;
    private AnimatorSet currentDoneButtonAnimation;
    private GroupCreateActivityDelegate delegate;
    private ContactsAddActivityDelegate delegate2;
    private boolean doneButtonVisible;
    private EditTextBoldCursor editText;
    private StickerEmptyView emptyView;
    private int fieldY;
    private ImageView floatingButton;
    private boolean forImport;
    private boolean ignoreScrollEvent;
    private LongSparseArray ignoreUsers;
    private TLRPC.ChatFull info;
    private boolean isAlwaysShare;
    private boolean isNeverShare;
    private GroupCreateDividerItemDecoration itemDecoration;
    private RecyclerListView listView;
    private int maxCount;
    int maxSize;
    private int measuredContainerHeight;
    private ScrollView scrollView;
    private boolean searchWas;
    private boolean searching;
    private LongSparseArray selectedContacts;
    private GroupCreateSpan selectedMiniapps;
    private GroupCreateSpan selectedPremium;
    private PermanentLinkBottomSheet sharedLinkBottomSheet;
    private int shiftDp;
    private SpansContainer spansContainer;
    private ArrayList toSelectIds;
    private boolean toSelectMiniapps;
    private boolean toSelectPremium;

    public static class Comparator implements java.util.Comparator {
        /* JADX INFO: Access modifiers changed from: private */
        public static String getName(TLObject tLObject) {
            if (!(tLObject instanceof TLRPC.User)) {
                return tLObject instanceof TLRPC.Chat ? ((TLRPC.Chat) tLObject).title : "";
            }
            TLRPC.User user = (TLRPC.User) tLObject;
            return ContactsController.formatName(user.first_name, user.last_name);
        }

        @Override // java.util.Comparator
        public int compare(TLObject tLObject, TLObject tLObject2) {
            return getName(tLObject).compareTo(getName(tLObject2));
        }
    }

    public interface ContactsAddActivityDelegate {

        public abstract /* synthetic */ class -CC {
            public static void $default$needAddBot(ContactsAddActivityDelegate contactsAddActivityDelegate, TLRPC.User user) {
            }
        }

        void didSelectUsers(ArrayList arrayList, int i);

        void needAddBot(TLRPC.User user);
    }

    public interface GroupCreateActivityDelegate {
        void didSelectUsers(boolean z, boolean z2, ArrayList arrayList);
    }

    public class GroupCreateAdapter extends RecyclerListView.FastScrollAdapter {
        private Context context;
        private int currentItemsCount;
        private int firstSectionRow;
        private int inviteViaLink;
        private int miniappsRow;
        private int noContactsStubRow;
        private int premiumRow;
        private SearchAdapterHelper searchAdapterHelper;
        private Runnable searchRunnable;
        private boolean searching;
        private int userTypesHeaderRow;
        private int usersStartRow;
        private ArrayList searchResult = new ArrayList();
        private ArrayList searchResultNames = new ArrayList();
        private ArrayList contacts = new ArrayList();

        public GroupCreateAdapter(Context context) {
            TLRPC.Chat chat;
            this.context = context;
            ArrayList<TLRPC.TL_contact> arrayList = GroupCreateActivity.this.getContactsController().contacts;
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC.User user = GroupCreateActivity.this.getMessagesController().getUser(Long.valueOf(arrayList.get(i).user_id));
                if (user != null && !user.self && !user.deleted) {
                    this.contacts.add(user);
                }
            }
            if (GroupCreateActivity.this.isNeverShare || GroupCreateActivity.this.isAlwaysShare) {
                ArrayList<TLRPC.Dialog> allDialogs = GroupCreateActivity.this.getMessagesController().getAllDialogs();
                int size = allDialogs.size();
                for (int i2 = 0; i2 < size; i2++) {
                    TLRPC.Dialog dialog = allDialogs.get(i2);
                    if (DialogObject.isChatDialog(dialog.id) && (chat = GroupCreateActivity.this.getMessagesController().getChat(Long.valueOf(-dialog.id))) != null && chat.migrated_to == null && (!ChatObject.isChannel(chat) || chat.megagroup)) {
                        this.contacts.add(chat);
                    }
                }
                Collections.sort(this.contacts, new Comparator());
                TLObject tLObject = null;
                int i3 = 0;
                while (i3 < this.contacts.size()) {
                    TLObject tLObject2 = (TLObject) this.contacts.get(i3);
                    if (tLObject == null || !firstLetter(Comparator.getName(tLObject)).equals(firstLetter(Comparator.getName(tLObject2)))) {
                        this.contacts.add(i3, new Letter(firstLetter(Comparator.getName(tLObject2))));
                    }
                    i3++;
                    tLObject = tLObject2;
                }
            }
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(false);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.GroupCreateActivity$GroupCreateAdapter$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ boolean canApplySearchResults(int i4) {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i4);
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
                public final void onDataSetChanged(int i4) {
                    GroupCreateActivity.GroupCreateAdapter.this.lambda$new$0(i4);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ void onSetHashtags(ArrayList arrayList2, HashMap hashMap) {
                    SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList2, hashMap);
                }
            });
        }

        private String firstLetter(String str) {
            return TextUtils.isEmpty(str) ? "" : str.substring(0, 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int i) {
            GroupCreateActivity.this.showItemsAnimated(this.currentItemsCount);
            if (this.searchRunnable == null && !this.searchAdapterHelper.isSearchInProgress() && getItemCount() == 0) {
                GroupCreateActivity.this.emptyView.showProgress(false, true);
            }
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$1(View view) {
            GroupCreateActivity.this.selectedPremium = null;
            GroupCreateActivity.this.selectedContacts.clear();
            GroupCreateActivity.this.spansContainer.removeAllSpans(true);
            GroupCreateActivity.this.checkVisibleRows();
            GroupCreateActivity.this.updateEditTextHint();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x00d0, code lost:
        
            if (r13.contains(" " + r3) != false) goto L46;
         */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0131 A[LOOP:1: B:26:0x0094->B:41:0x0131, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00e0 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$searchDialogs$2(String str) {
            String str2;
            String publicUsername;
            Object obj;
            CharSequence generateSearchName;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList(), new ArrayList());
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
            int i = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < this.contacts.size(); i2++) {
                TLObject tLObject = (TLObject) this.contacts.get(i2);
                boolean z = tLObject instanceof TLRPC.User;
                if (z) {
                    TLRPC.User user = (TLRPC.User) tLObject;
                    str2 = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                    publicUsername = UserObject.getPublicUsername(user);
                } else {
                    if (tLObject instanceof TLRPC.Chat) {
                        TLRPC.Chat chat = (TLRPC.Chat) tLObject;
                        str2 = chat.title;
                        publicUsername = ChatObject.getPublicUsername(chat);
                    }
                }
                String translitString2 = LocaleController.getInstance().getTranslitString(str2);
                if (str2.equals(translitString2)) {
                    translitString2 = null;
                }
                char c = 0;
                for (int i3 = 0; i3 < i; i3++) {
                    String str3 = strArr[i3];
                    if (!str2.startsWith(str3)) {
                        if (!str2.contains(" " + str3)) {
                            if (translitString2 != null) {
                                if (!translitString2.startsWith(str3)) {
                                }
                            }
                            if (publicUsername != null && publicUsername.startsWith(str3)) {
                                c = 2;
                            }
                            if (c == 0) {
                                if (c == 1) {
                                    if (z) {
                                        TLRPC.User user2 = (TLRPC.User) tLObject;
                                        generateSearchName = AndroidUtilities.generateSearchName(user2.first_name, user2.last_name, str3);
                                    } else {
                                        if (tLObject instanceof TLRPC.Chat) {
                                            generateSearchName = AndroidUtilities.generateSearchName(((TLRPC.Chat) tLObject).title, null, str3);
                                        }
                                        obj = null;
                                    }
                                    arrayList2.add(generateSearchName);
                                    obj = null;
                                } else {
                                    obj = null;
                                    arrayList2.add(AndroidUtilities.generateSearchName("@" + publicUsername, null, "@" + str3));
                                }
                                arrayList.add(tLObject);
                            }
                        }
                    }
                    c = 1;
                    if (c == 0) {
                    }
                }
            }
            updateSearchResults(arrayList, arrayList2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchDialogs$3(final String str) {
            this.searchAdapterHelper.queryServerSearch(str, true, GroupCreateActivity.this.isAlwaysShare || GroupCreateActivity.this.isNeverShare, true, false, false, 0L, false, 0, 0);
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.GroupCreateActivity$GroupCreateAdapter$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCreateActivity.GroupCreateAdapter.this.lambda$searchDialogs$2(str);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$searchDialogs$4(final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCreateActivity$GroupCreateAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCreateActivity.GroupCreateAdapter.this.lambda$searchDialogs$3(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$5(ArrayList arrayList, ArrayList arrayList2) {
            if (this.searching) {
                this.searchRunnable = null;
                this.searchResult = arrayList;
                this.searchResultNames = arrayList2;
                this.searchAdapterHelper.mergeResults(arrayList);
                GroupCreateActivity.this.showItemsAnimated(this.currentItemsCount);
                notifyDataSetChanged();
                if (this.searching && !this.searchAdapterHelper.isSearchInProgress() && getItemCount() == 0) {
                    GroupCreateActivity.this.emptyView.showProgress(false, true);
                }
            }
        }

        private void updateSearchResults(final ArrayList arrayList, final ArrayList arrayList2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.GroupCreateActivity$GroupCreateAdapter$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCreateActivity.GroupCreateAdapter.this.lambda$updateSearchResults$5(arrayList, arrayList2);
                }
            });
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:15:0x0065  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x00c0  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x00c9  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public int getItemCount() {
            int i;
            int size;
            this.noContactsStubRow = -1;
            this.userTypesHeaderRow = -1;
            this.firstSectionRow = -1;
            this.premiumRow = -1;
            this.miniappsRow = -1;
            if (this.searching) {
                int size2 = this.searchResult.size();
                int size3 = this.searchAdapterHelper.getLocalServerSearch().size();
                int size4 = this.searchAdapterHelper.getGlobalSearch().size();
                int i2 = size2 + size3;
                if (size4 != 0) {
                    i2 += size4 + 1;
                }
                this.currentItemsCount = i2;
                return i2;
            }
            int i3 = 2;
            if (GroupCreateActivity.this.allowPremium) {
                this.firstSectionRow = 0;
                this.userTypesHeaderRow = 0;
                this.premiumRow = 1;
            } else {
                boolean z = GroupCreateActivity.this.allowMiniapps;
                this.firstSectionRow = 0;
                if (!z) {
                    i = 0;
                    this.usersStartRow = i;
                    size = i + this.contacts.size();
                    if (GroupCreateActivity.this.addToGroup) {
                        if (GroupCreateActivity.this.chatId != 0) {
                            i3 = ChatObject.canUserDoAdminAction(GroupCreateActivity.this.getMessagesController().getChat(Long.valueOf(GroupCreateActivity.this.chatId)), 3);
                        } else if (GroupCreateActivity.this.channelId != 0) {
                            TLRPC.Chat chat = GroupCreateActivity.this.getMessagesController().getChat(Long.valueOf(GroupCreateActivity.this.channelId));
                            if (!ChatObject.canUserDoAdminAction(chat, 3) || ChatObject.isPublic(chat)) {
                                i3 = 0;
                            }
                        } else {
                            this.inviteViaLink = 0;
                            if (this.inviteViaLink != 0) {
                                this.usersStartRow++;
                                size++;
                            }
                        }
                        this.inviteViaLink = i3;
                        if (this.inviteViaLink != 0) {
                        }
                    }
                    if (size == 0) {
                        this.noContactsStubRow = 0;
                        size++;
                    }
                    this.currentItemsCount = size;
                    return size;
                }
                this.userTypesHeaderRow = 0;
                this.miniappsRow = 1;
            }
            i = 2;
            this.usersStartRow = i;
            size = i + this.contacts.size();
            if (GroupCreateActivity.this.addToGroup) {
            }
            if (size == 0) {
            }
            this.currentItemsCount = size;
            return size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (this.searching) {
                return i == this.searchResult.size() + this.searchAdapterHelper.getLocalServerSearch().size() ? 0 : 1;
            }
            if (i == this.userTypesHeaderRow) {
                return 0;
            }
            if (i != this.premiumRow && i != this.miniappsRow) {
                if (this.inviteViaLink != 0 && i == 0) {
                    return 2;
                }
                if (this.noContactsStubRow == i) {
                    return 3;
                }
                int i2 = i - this.usersStartRow;
                if (i2 >= 0 && i2 < this.contacts.size() && (this.contacts.get(i - this.usersStartRow) instanceof Letter)) {
                    return 0;
                }
            }
            return 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public String getLetter(int i) {
            String str;
            String str2;
            if (this.searching || i < this.usersStartRow) {
                return null;
            }
            int size = this.contacts.size();
            int i2 = this.usersStartRow;
            if (i >= size + i2) {
                return null;
            }
            TLObject tLObject = (TLObject) this.contacts.get(i - i2);
            if (tLObject instanceof Letter) {
                return ((Letter) tLObject).letter;
            }
            if (tLObject instanceof TLRPC.User) {
                TLRPC.User user = (TLRPC.User) tLObject;
                str = user.first_name;
                str2 = user.last_name;
            } else {
                str = ((TLRPC.Chat) tLObject).title;
                str2 = "";
            }
            if (LocaleController.nameDisplayOrder == 1) {
                if (!TextUtils.isEmpty(str)) {
                    return str.substring(0, 1).toUpperCase();
                }
                if (!TextUtils.isEmpty(str2)) {
                    return str2.substring(0, 1).toUpperCase();
                }
            } else {
                if (!TextUtils.isEmpty(str2)) {
                    return str2.substring(0, 1).toUpperCase();
                }
                if (!TextUtils.isEmpty(str)) {
                    return str.substring(0, 1).toUpperCase();
                }
            }
            return "";
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr) {
            iArr[0] = (int) (getItemCount() * f);
            iArr[1] = 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 0) {
                return false;
            }
            if (GroupCreateActivity.this.ignoreUsers != null) {
                View view = viewHolder.itemView;
                if (view instanceof GroupCreateUserCell) {
                    Object object = ((GroupCreateUserCell) view).getObject();
                    return !(object instanceof TLRPC.User) || GroupCreateActivity.this.ignoreUsers.indexOfKey(((TLRPC.User) object).id) < 0;
                }
            }
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            GroupCreateActivity.this.updateEditTextHint();
        }

        /* JADX WARN: Code restructure failed: missing block: B:33:0x00c4, code lost:
        
            if (r12.toString().startsWith("@" + r5) != false) goto L84;
         */
        /* JADX WARN: Removed duplicated region for block: B:112:0x01e7  */
        /* JADX WARN: Removed duplicated region for block: B:120:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0084  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x015d  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0171  */
        /* JADX WARN: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:52:0x0162  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String upperCase;
            int i2;
            TLObject tLObject;
            CharSequence charSequence;
            long j;
            Object obj;
            String publicUsername;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
                if (this.searching) {
                    i2 = R.string.GlobalSearch;
                } else {
                    if (i != this.userTypesHeaderRow) {
                        int i3 = i - this.usersStartRow;
                        if (i3 >= 0 && i3 < this.contacts.size()) {
                            TLObject tLObject2 = (TLObject) this.contacts.get(i - this.usersStartRow);
                            if (tLObject2 instanceof Letter) {
                                upperCase = ((Letter) tLObject2).letter.toUpperCase();
                                graySectionCell.setText(upperCase);
                            }
                        }
                        if (i != this.firstSectionRow) {
                            graySectionCell.setRightText((GroupCreateActivity.this.selectedPremium == null && GroupCreateActivity.this.selectedContacts.isEmpty()) ? "" : LocaleController.getString(R.string.DeselectAll), true, new View.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$GroupCreateAdapter$$ExternalSyntheticLambda0
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    GroupCreateActivity.GroupCreateAdapter.this.lambda$onBindViewHolder$1(view);
                                }
                            });
                            return;
                        }
                        return;
                    }
                    i2 = R.string.PrivacyUserTypes;
                }
                upperCase = LocaleController.getString(i2);
                graySectionCell.setText(upperCase);
                if (i != this.firstSectionRow) {
                }
            } else {
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        return;
                    }
                    ((TextCell) viewHolder.itemView).setTextAndIcon((CharSequence) LocaleController.getString(this.inviteViaLink == 2 ? R.string.ChannelInviteViaLink : R.string.InviteToGroupByLink), R.drawable.msg_link2, false);
                    return;
                }
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
                CharSequence charSequence2 = null;
                if (this.searching) {
                    int size = this.searchResult.size();
                    int size2 = this.searchAdapterHelper.getGlobalSearch().size();
                    int size3 = this.searchAdapterHelper.getLocalServerSearch().size();
                    if (i >= 0 && i < size) {
                        obj = this.searchResult.get(i);
                    } else if (i >= size && i < size3 + size) {
                        obj = this.searchAdapterHelper.getLocalServerSearch().get(i - size);
                    } else if (i <= size + size3 || i > size2 + size + size3) {
                        tLObject = null;
                        if (tLObject != null) {
                            if (tLObject instanceof TLRPC.User) {
                                publicUsername = ((TLRPC.User) tLObject).username;
                            } else if (!(tLObject instanceof TLRPC.Chat)) {
                                return;
                            } else {
                                publicUsername = ChatObject.getPublicUsername((TLRPC.Chat) tLObject);
                            }
                            if (i < size) {
                                charSequence = (CharSequence) this.searchResultNames.get(i);
                                if (charSequence != null && !TextUtils.isEmpty(publicUsername)) {
                                }
                                charSequence2 = charSequence;
                                charSequence = null;
                            } else if (i > size && !TextUtils.isEmpty(publicUsername)) {
                                String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
                                if (lastFoundUsername.startsWith("@")) {
                                    lastFoundUsername = lastFoundUsername.substring(1);
                                }
                                try {
                                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                    spannableStringBuilder.append((CharSequence) "@");
                                    spannableStringBuilder.append((CharSequence) publicUsername);
                                    int indexOfIgnoreCase = AndroidUtilities.indexOfIgnoreCase(publicUsername, lastFoundUsername);
                                    if (indexOfIgnoreCase != -1) {
                                        int length = lastFoundUsername.length();
                                        if (indexOfIgnoreCase == 0) {
                                            length++;
                                        } else {
                                            indexOfIgnoreCase++;
                                        }
                                        spannableStringBuilder.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4)), indexOfIgnoreCase, length + indexOfIgnoreCase, 33);
                                    }
                                    charSequence = spannableStringBuilder;
                                } catch (Exception unused) {
                                    charSequence = publicUsername;
                                }
                            }
                            groupCreateUserCell.setObject(tLObject, charSequence2, charSequence);
                            j = !(tLObject instanceof TLRPC.User) ? ((TLRPC.User) tLObject).id : tLObject instanceof TLRPC.Chat ? -((TLRPC.Chat) tLObject).id : 0L;
                            if (j == 0) {
                                if (GroupCreateActivity.this.ignoreUsers == null || GroupCreateActivity.this.ignoreUsers.indexOfKey(j) < 0) {
                                    groupCreateUserCell.setChecked(GroupCreateActivity.this.selectedContacts.indexOfKey(j) >= 0, false);
                                    groupCreateUserCell.setCheckBoxEnabled(true);
                                    return;
                                } else {
                                    groupCreateUserCell.setChecked(true, false);
                                    groupCreateUserCell.setCheckBoxEnabled(false);
                                    return;
                                }
                            }
                            return;
                        }
                    } else {
                        obj = this.searchAdapterHelper.getGlobalSearch().get(((i - size) - size3) - 1);
                    }
                    tLObject = (TLObject) obj;
                    if (tLObject != null) {
                    }
                } else if (i == this.premiumRow) {
                    groupCreateUserCell.setPremium();
                    groupCreateUserCell.setChecked(GroupCreateActivity.this.selectedPremium != null, false);
                    return;
                } else {
                    if (i == this.miniappsRow) {
                        groupCreateUserCell.setMiniapps();
                        groupCreateUserCell.setChecked(GroupCreateActivity.this.selectedMiniapps != null, false);
                        return;
                    }
                    tLObject = (TLObject) this.contacts.get(i - this.usersStartRow);
                }
                charSequence = null;
                groupCreateUserCell.setObject(tLObject, charSequence2, charSequence);
                if (!(tLObject instanceof TLRPC.User)) {
                }
                if (j == 0) {
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View graySectionCell;
            GroupCreateUserCell groupCreateUserCell;
            if (i != 0) {
                int i2 = 0;
                if (i == 1) {
                    groupCreateUserCell = new GroupCreateUserCell(this.context, 1, 0, false);
                } else if (i != 3) {
                    graySectionCell = new TextCell(this.context);
                } else {
                    StickerEmptyView stickerEmptyView = new StickerEmptyView(this.context, null, i2) { // from class: org.telegram.ui.GroupCreateActivity.GroupCreateAdapter.1
                        @Override // org.telegram.ui.Components.StickerEmptyView, android.view.ViewGroup, android.view.View
                        protected void onAttachedToWindow() {
                            super.onAttachedToWindow();
                            this.stickerView.getImageReceiver().startAnimation();
                        }
                    };
                    stickerEmptyView.setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
                    stickerEmptyView.subtitle.setVisibility(8);
                    stickerEmptyView.title.setText(LocaleController.getString(R.string.NoContacts));
                    stickerEmptyView.setAnimateLayoutChange(true);
                    groupCreateUserCell = stickerEmptyView;
                }
                graySectionCell = groupCreateUserCell;
            } else {
                graySectionCell = new GraySectionCell(this.context);
            }
            return new RecyclerListView.Holder(graySectionCell);
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
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchAdapterHelper.mergeResults(null);
            this.searchAdapterHelper.queryServerSearch(null, true, GroupCreateActivity.this.isAlwaysShare || GroupCreateActivity.this.isNeverShare, false, false, false, 0L, false, 0, 0);
            notifyDataSetChanged();
            if (TextUtils.isEmpty(str)) {
                return;
            }
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.GroupCreateActivity$GroupCreateAdapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    GroupCreateActivity.GroupCreateAdapter.this.lambda$searchDialogs$4(str);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable, 300L);
        }

        public void setSearching(boolean z) {
            if (this.searching == z) {
                return;
            }
            this.searching = z;
            notifyDataSetChanged();
        }
    }

    private static class Letter extends TLRPC.TL_contact {
        public final String letter;

        public Letter(String str) {
            this.letter = str;
        }
    }

    private class SpansContainer extends ViewGroup {
        private View addingSpan;
        private int animationIndex;
        private boolean animationStarted;
        private ArrayList animators;
        private final ArrayList removingSpans;

        public SpansContainer(Context context) {
            super(context);
            this.animators = new ArrayList();
            this.removingSpans = new ArrayList();
            this.animationIndex = -1;
        }

        public void addSpan(GroupCreateSpan groupCreateSpan) {
            GroupCreateActivity.this.allSpans.add(groupCreateSpan);
            if (!groupCreateSpan.isFlag) {
                GroupCreateActivity.this.selectedContacts.put(groupCreateSpan.getUid(), groupCreateSpan);
            }
            GroupCreateActivity.this.editText.setHintVisible(false, TextUtils.isEmpty(GroupCreateActivity.this.editText.getText()));
            if (GroupCreateActivity.this.currentAnimation != null && GroupCreateActivity.this.currentAnimation.isRunning()) {
                GroupCreateActivity.this.currentAnimation.setupEndValues();
                GroupCreateActivity.this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            GroupCreateActivity.this.currentAnimation = new AnimatorSet();
            GroupCreateActivity.this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCreateActivity.SpansContainer.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SpansContainer.this.addingSpan = null;
                    GroupCreateActivity.this.currentAnimation = null;
                    SpansContainer.this.animationStarted = false;
                    GroupCreateActivity.this.editText.setAllowDrawCursor(true);
                }
            });
            GroupCreateActivity.this.currentAnimation.setDuration(150L);
            this.addingSpan = groupCreateSpan;
            this.animators.clear();
            this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, (Property<View, Float>) View.SCALE_X, 0.01f, 1.0f));
            this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, (Property<View, Float>) View.SCALE_Y, 0.01f, 1.0f));
            this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, (Property<View, Float>) View.ALPHA, 0.0f, 1.0f));
            addView(groupCreateSpan);
        }

        public void endAnimation() {
            if (GroupCreateActivity.this.currentAnimation == null || !GroupCreateActivity.this.currentAnimation.isRunning()) {
                return;
            }
            GroupCreateActivity.this.currentAnimation.setupEndValues();
            GroupCreateActivity.this.currentAnimation.cancel();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:31:0x00e0  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            int min;
            boolean z;
            int i3;
            int i4;
            char c;
            int size = View.MeasureSpec.getSize(i);
            int dp = size - AndroidUtilities.dp(26.0f);
            int dp2 = AndroidUtilities.dp(10.0f);
            int dp3 = AndroidUtilities.dp(10.0f);
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            for (int childCount = getChildCount(); i5 < childCount; childCount = i3) {
                View childAt = getChildAt(i5);
                if (childAt instanceof GroupCreateSpan) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
                    boolean contains = this.removingSpans.contains(childAt);
                    if (!contains && childAt.getMeasuredWidth() + i6 > dp) {
                        dp2 += childAt.getMeasuredHeight() + AndroidUtilities.dp(8.0f);
                        i6 = 0;
                    }
                    if (childAt.getMeasuredWidth() + i7 > dp) {
                        dp3 += childAt.getMeasuredHeight() + AndroidUtilities.dp(8.0f);
                        i7 = 0;
                    }
                    int dp4 = AndroidUtilities.dp(13.0f) + i6;
                    if (!this.animationStarted) {
                        if (contains) {
                            childAt.setTranslationX(AndroidUtilities.dp(13.0f) + i7);
                            childAt.setTranslationY(dp3);
                        } else {
                            if (this.removingSpans.isEmpty()) {
                                i3 = childCount;
                                childAt.setTranslationX(dp4);
                                childAt.setTranslationY(dp2);
                            } else {
                                float f = dp4;
                                if (childAt.getTranslationX() != f) {
                                    i3 = childCount;
                                    i4 = 1;
                                    c = 0;
                                    this.animators.add(ObjectAnimator.ofFloat(childAt, "translationX", f));
                                } else {
                                    i3 = childCount;
                                    i4 = 1;
                                    c = 0;
                                }
                                float f2 = dp2;
                                if (childAt.getTranslationY() != f2) {
                                    ArrayList arrayList = this.animators;
                                    float[] fArr = new float[i4];
                                    fArr[c] = f2;
                                    arrayList.add(ObjectAnimator.ofFloat(childAt, "translationY", fArr));
                                }
                            }
                            if (!contains) {
                                i6 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                            }
                            i7 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                        }
                    }
                    i3 = childCount;
                    if (!contains) {
                    }
                    i7 += childAt.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                } else {
                    i3 = childCount;
                }
                i5++;
            }
            if (AndroidUtilities.isTablet()) {
                min = AndroidUtilities.dp(372.0f);
            } else {
                Point point = AndroidUtilities.displaySize;
                min = Math.min(point.x, point.y) - AndroidUtilities.dp(158.0f);
            }
            int i8 = min / 3;
            if (dp - i6 < i8) {
                dp2 += AndroidUtilities.dp(40.0f);
                i6 = 0;
            }
            if (dp - i7 < i8) {
                dp3 += AndroidUtilities.dp(40.0f);
            }
            GroupCreateActivity.this.editText.measure(View.MeasureSpec.makeMeasureSpec(dp - i6, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
            if (!this.animationStarted) {
                int dp5 = dp3 + AndroidUtilities.dp(42.0f);
                int dp6 = i6 + AndroidUtilities.dp(16.0f);
                GroupCreateActivity.this.fieldY = dp2;
                if (GroupCreateActivity.this.currentAnimation != null) {
                    int dp7 = dp2 + AndroidUtilities.dp(42.0f);
                    if (GroupCreateActivity.this.containerHeight != dp7) {
                        this.animators.add(ObjectAnimator.ofInt(GroupCreateActivity.this, "containerHeight", dp7));
                    }
                    GroupCreateActivity groupCreateActivity = GroupCreateActivity.this;
                    groupCreateActivity.measuredContainerHeight = Math.max(groupCreateActivity.containerHeight, dp7);
                    float f3 = dp6;
                    if (GroupCreateActivity.this.editText.getTranslationX() != f3) {
                        this.animators.add(ObjectAnimator.ofFloat(GroupCreateActivity.this.editText, "translationX", f3));
                    }
                    if (GroupCreateActivity.this.editText.getTranslationY() != GroupCreateActivity.this.fieldY) {
                        z = false;
                        this.animators.add(ObjectAnimator.ofFloat(GroupCreateActivity.this.editText, "translationY", GroupCreateActivity.this.fieldY));
                    } else {
                        z = false;
                    }
                    GroupCreateActivity.this.editText.setAllowDrawCursor(z);
                    GroupCreateActivity.this.currentAnimation.playTogether(this.animators);
                    GroupCreateActivity.this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCreateActivity.SpansContainer.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            GroupCreateActivity.this.getNotificationCenter().onAnimationFinish(SpansContainer.this.animationIndex);
                            SpansContainer.this.requestLayout();
                        }
                    });
                    this.animationIndex = GroupCreateActivity.this.getNotificationCenter().setAnimationInProgress(this.animationIndex, null);
                    GroupCreateActivity.this.currentAnimation.start();
                    this.animationStarted = true;
                } else {
                    GroupCreateActivity groupCreateActivity2 = GroupCreateActivity.this;
                    groupCreateActivity2.measuredContainerHeight = groupCreateActivity2.containerHeight = dp5;
                    GroupCreateActivity.this.editText.setTranslationX(dp6);
                    GroupCreateActivity.this.editText.setTranslationY(GroupCreateActivity.this.fieldY);
                }
            } else if (GroupCreateActivity.this.currentAnimation != null && !GroupCreateActivity.this.ignoreScrollEvent && this.removingSpans.isEmpty()) {
                GroupCreateActivity.this.editText.bringPointIntoView(GroupCreateActivity.this.editText.getSelectionStart());
            }
            setMeasuredDimension(size, GroupCreateActivity.this.measuredContainerHeight);
            GroupCreateActivity.this.listView.setTranslationY(0.0f);
        }

        public void removeAllSpans(boolean z) {
            GroupCreateActivity.this.ignoreScrollEvent = true;
            final ArrayList arrayList = new ArrayList(GroupCreateActivity.this.allSpans);
            GroupCreateActivity.this.allSpans.clear();
            this.removingSpans.clear();
            this.removingSpans.addAll(arrayList);
            for (int i = 0; i < arrayList.size(); i++) {
                ((GroupCreateSpan) arrayList.get(i)).setOnClickListener(null);
            }
            endAnimation();
            if (z) {
                this.animationStarted = false;
                GroupCreateActivity.this.currentAnimation = new AnimatorSet();
                GroupCreateActivity.this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCreateActivity.SpansContainer.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            SpansContainer.this.removeView((View) arrayList.get(i2));
                        }
                        SpansContainer.this.removingSpans.clear();
                        GroupCreateActivity.this.currentAnimation = null;
                        SpansContainer.this.animationStarted = false;
                        GroupCreateActivity.this.editText.setAllowDrawCursor(true);
                        if (GroupCreateActivity.this.allSpans.isEmpty()) {
                            GroupCreateActivity.this.editText.setHintVisible(true, true);
                        }
                    }
                });
                this.animators.clear();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    GroupCreateSpan groupCreateSpan = (GroupCreateSpan) arrayList.get(i2);
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_X, 1.0f, 0.01f));
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_Y, 1.0f, 0.01f));
                    this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.ALPHA, 1.0f, 0.0f));
                }
            } else {
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    removeView((View) arrayList.get(i3));
                }
                this.removingSpans.clear();
                GroupCreateActivity.this.currentAnimation = null;
                this.animationStarted = false;
                GroupCreateActivity.this.editText.setAllowDrawCursor(true);
                if (GroupCreateActivity.this.allSpans.isEmpty()) {
                    GroupCreateActivity.this.editText.setHintVisible(true, true);
                }
            }
            requestLayout();
        }

        public void removeSpan(final GroupCreateSpan groupCreateSpan) {
            GroupCreateActivity.this.ignoreScrollEvent = true;
            if (!groupCreateSpan.isFlag) {
                GroupCreateActivity.this.selectedContacts.remove(groupCreateSpan.getUid());
            }
            if (groupCreateSpan == GroupCreateActivity.this.selectedPremium) {
                GroupCreateActivity.this.selectedPremium = null;
            }
            if (groupCreateSpan == GroupCreateActivity.this.selectedMiniapps) {
                GroupCreateActivity.this.selectedMiniapps = null;
            }
            GroupCreateActivity.this.allSpans.remove(groupCreateSpan);
            groupCreateSpan.setOnClickListener(null);
            if (GroupCreateActivity.this.currentAnimation != null) {
                GroupCreateActivity.this.currentAnimation.setupEndValues();
                GroupCreateActivity.this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            GroupCreateActivity.this.currentAnimation = new AnimatorSet();
            GroupCreateActivity.this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCreateActivity.SpansContainer.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SpansContainer.this.removeView(groupCreateSpan);
                    SpansContainer.this.removingSpans.clear();
                    GroupCreateActivity.this.currentAnimation = null;
                    SpansContainer.this.animationStarted = false;
                    GroupCreateActivity.this.editText.setAllowDrawCursor(true);
                    if (GroupCreateActivity.this.allSpans.isEmpty()) {
                        GroupCreateActivity.this.editText.setHintVisible(true, true);
                    }
                }
            });
            GroupCreateActivity.this.currentAnimation.setDuration(150L);
            this.removingSpans.clear();
            this.removingSpans.add(groupCreateSpan);
            this.animators.clear();
            this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_X, 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.SCALE_Y, 1.0f, 0.01f));
            this.animators.add(ObjectAnimator.ofFloat(groupCreateSpan, (Property<GroupCreateSpan, Float>) View.ALPHA, 1.0f, 0.0f));
            requestLayout();
        }
    }

    public GroupCreateActivity(Bundle bundle) {
        super(bundle);
        this.maxCount = getMessagesController().maxMegagroupCount;
        this.chatType = 0;
        this.selectedContacts = new LongSparseArray();
        this.allSpans = new ArrayList();
        this.shiftDp = -4;
        this.chatType = bundle.getInt("chatType", 0);
        this.forImport = bundle.getBoolean("forImport", false);
        this.isAlwaysShare = bundle.getBoolean("isAlwaysShare", false);
        this.isNeverShare = bundle.getBoolean("isNeverShare", false);
        this.addToGroup = bundle.getBoolean("addToGroup", false);
        this.chatAddType = bundle.getInt("chatAddType", 0);
        this.allowPremium = bundle.getBoolean("allowPremium", false);
        this.allowMiniapps = bundle.getBoolean("allowMiniapps", false);
        this.chatId = bundle.getLong("chatId");
        this.channelId = bundle.getLong("channelId");
        if (this.isAlwaysShare || this.isNeverShare || this.addToGroup) {
            this.maxCount = 0;
        } else {
            this.maxCount = this.chatType == 0 ? getMessagesController().maxMegagroupCount : getMessagesController().maxBroadcastCount;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0071, code lost:
    
        if (r11.selectedContacts.indexOfKey(r9) >= 0) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0073, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0075, code lost:
    
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0041, code lost:
    
        if (r11.selectedPremium != null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0052, code lost:
    
        if (r11.selectedMiniapps != null) goto L35;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkVisibleRows() {
        long j;
        boolean z;
        int childCount = this.listView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt instanceof GroupCreateUserCell) {
                GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) childAt;
                Object object = groupCreateUserCell.getObject();
                if (object instanceof TLRPC.User) {
                    j = ((TLRPC.User) object).id;
                } else if (object instanceof TLRPC.Chat) {
                    j = -((TLRPC.Chat) object).id;
                } else {
                    boolean z2 = object instanceof String;
                    if (!z2 || !"premium".equalsIgnoreCase((String) object)) {
                        if (!z2 || !"miniapps".equalsIgnoreCase((String) object)) {
                            j = 0;
                        }
                    }
                    groupCreateUserCell.setChecked(z, true);
                    groupCreateUserCell.setCheckBoxEnabled(true);
                }
                if (j != 0) {
                    LongSparseArray longSparseArray = this.ignoreUsers;
                    if (longSparseArray != null && longSparseArray.indexOfKey(j) >= 0) {
                        groupCreateUserCell.setChecked(true, false);
                        groupCreateUserCell.setCheckBoxEnabled(false);
                    }
                }
            } else if ((childAt instanceof GraySectionCell) && this.listView.getChildAdapterPosition(childAt) == this.adapter.firstSectionRow) {
                ((GraySectionCell) childAt).setRightText((this.selectedPremium == null && this.selectedContacts.isEmpty()) ? "" : LocaleController.getString(R.string.DeselectAll), true, new View.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        GroupCreateActivity.this.lambda$checkVisibleRows$6(view);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeSearch() {
        this.searching = false;
        this.searchWas = false;
        this.itemDecoration.setSearching(false);
        this.adapter.setSearching(false);
        this.adapter.searchDialogs(null);
        this.listView.setFastScrollVisible(true);
        this.listView.setVerticalScrollBarEnabled(false);
        showItemsAnimated(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkVisibleRows$6(View view) {
        this.selectedPremium = null;
        this.selectedContacts.clear();
        this.spansContainer.removeAllSpans(true);
        checkVisibleRows();
        updateEditTextHint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view) {
        this.editText.clearFocus();
        this.editText.requestFocus();
        AndroidUtilities.showKeyboard(this.editText);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$1(TextView textView, int i, KeyEvent keyEvent) {
        return i == 6 && onDonePressed(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(TLRPC.User user, DialogInterface dialogInterface, int i) {
        this.delegate2.needAddBot(user);
        if (this.editText.length() > 0) {
            this.editText.setText((CharSequence) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(Context context, View view, int i) {
        long j;
        Dialog create;
        if (i == 0 && this.adapter.inviteViaLink != 0 && !this.adapter.searching) {
            PermanentLinkBottomSheet permanentLinkBottomSheet = new PermanentLinkBottomSheet(context, false, this, this.info, this.chatId, this.channelId != 0);
            this.sharedLinkBottomSheet = permanentLinkBottomSheet;
            showDialog(permanentLinkBottomSheet);
            return;
        }
        if (view instanceof GroupCreateUserCell) {
            GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) view;
            if (groupCreateUserCell.currentPremium) {
                GroupCreateSpan groupCreateSpan = this.selectedPremium;
                if (groupCreateSpan == null) {
                    GroupCreateSpan groupCreateSpan2 = new GroupCreateSpan(this.editText.getContext(), "premium");
                    this.selectedPremium = groupCreateSpan2;
                    this.spansContainer.addSpan(groupCreateSpan2);
                    this.selectedPremium.setOnClickListener(this);
                } else {
                    this.spansContainer.removeSpan(groupCreateSpan);
                    this.selectedPremium = null;
                }
                checkVisibleRows();
                return;
            }
            if (groupCreateUserCell.currentMiniapps) {
                GroupCreateSpan groupCreateSpan3 = this.selectedMiniapps;
                if (groupCreateSpan3 == null) {
                    GroupCreateSpan groupCreateSpan4 = new GroupCreateSpan(this.editText.getContext(), "miniapps");
                    this.selectedMiniapps = groupCreateSpan4;
                    this.spansContainer.addSpan(groupCreateSpan4);
                    this.selectedMiniapps.setOnClickListener(this);
                } else {
                    this.spansContainer.removeSpan(groupCreateSpan3);
                    this.selectedMiniapps = null;
                }
                checkVisibleRows();
                return;
            }
            Object object = groupCreateUserCell.getObject();
            boolean z = object instanceof TLRPC.User;
            if (z) {
                j = ((TLRPC.User) object).id;
            } else if (!(object instanceof TLRPC.Chat)) {
                return;
            } else {
                j = -((TLRPC.Chat) object).id;
            }
            LongSparseArray longSparseArray = this.ignoreUsers;
            if (longSparseArray == null || longSparseArray.indexOfKey(j) < 0) {
                if (groupCreateUserCell.isBlocked()) {
                    showPremiumBlockedToast(groupCreateUserCell, j);
                    return;
                }
                if (this.selectedContacts.indexOfKey(j) < 0) {
                    if (this.maxCount == 0 || this.selectedContacts.size() != this.maxCount) {
                        if (this.chatType == 0 && this.selectedContacts.size() == getMessagesController().maxGroupCount) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                            builder.setTitle(LocaleController.getString(R.string.AppName));
                            builder.setMessage(LocaleController.getString(R.string.SoftUserLimitAlert));
                            builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
                            create = builder.create();
                        } else {
                            if (z) {
                                final TLRPC.User user = (TLRPC.User) object;
                                if (this.addToGroup && user.bot) {
                                    long j2 = this.channelId;
                                    if (j2 == 0 && user.bot_nochats) {
                                        try {
                                            BulletinFactory.of(this).createErrorBulletin(LocaleController.getString(R.string.BotCantJoinGroups)).show();
                                            return;
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                            return;
                                        }
                                    }
                                    if (j2 != 0) {
                                        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(this.channelId));
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                                        if (ChatObject.canAddAdmins(chat)) {
                                            builder2.setTitle(LocaleController.getString(R.string.AddBotAdminAlert));
                                            builder2.setMessage(LocaleController.getString(R.string.AddBotAsAdmin));
                                            builder2.setPositiveButton(LocaleController.getString(R.string.AddAsAdmin), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda6
                                                @Override // android.content.DialogInterface.OnClickListener
                                                public final void onClick(DialogInterface dialogInterface, int i2) {
                                                    GroupCreateActivity.this.lambda$createView$2(user, dialogInterface, i2);
                                                }
                                            });
                                            builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                                        } else {
                                            builder2.setMessage(LocaleController.getString(R.string.CantAddBotAsAdmin));
                                            builder2.setPositiveButton(LocaleController.getString(R.string.OK), null);
                                        }
                                        create = builder2.create();
                                    }
                                }
                                getMessagesController().putUser(user, true ^ this.searching);
                            } else if (object instanceof TLRPC.Chat) {
                                getMessagesController().putChat((TLRPC.Chat) object, true ^ this.searching);
                            }
                            GroupCreateSpan groupCreateSpan5 = new GroupCreateSpan(this.editText.getContext(), object);
                            this.spansContainer.addSpan(groupCreateSpan5);
                            groupCreateSpan5.setOnClickListener(this);
                        }
                        showDialog(create);
                        return;
                    }
                    return;
                }
                this.spansContainer.removeSpan((GroupCreateSpan) this.selectedContacts.get(j));
                updateHint();
                if (this.searching || this.searchWas) {
                    AndroidUtilities.showKeyboard(this.editText);
                } else {
                    checkVisibleRows();
                }
                if (this.editText.length() > 0) {
                    this.editText.setText((CharSequence) null);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        onDonePressed(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$9() {
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
    public static /* synthetic */ void lambda$onDonePressed$7(CheckBoxCell[] checkBoxCellArr, View view) {
        checkBoxCellArr[0].setChecked(!r1.isChecked(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDonePressed$8(CheckBoxCell[] checkBoxCellArr, DialogInterface dialogInterface, int i) {
        int i2 = 0;
        CheckBoxCell checkBoxCell = checkBoxCellArr[0];
        if (checkBoxCell != null && checkBoxCell.isChecked()) {
            i2 = 100;
        }
        onAddToGroupDone(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$5() {
        presentFragment(new PremiumPreviewFragment("noncontacts"));
    }

    private void onAddToGroupDone(int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.selectedContacts.size(); i2++) {
            arrayList.add(getMessagesController().getUser(Long.valueOf(this.selectedContacts.keyAt(i2))));
        }
        ContactsAddActivityDelegate contactsAddActivityDelegate = this.delegate2;
        if (contactsAddActivityDelegate != null) {
            contactsAddActivityDelegate.didSelectUsers(arrayList, i);
        }
        lambda$onBackPressed$321();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onDonePressed(boolean z) {
        SpannableStringBuilder replaceTags;
        if (this.selectedContacts.size() == 0 && this.chatType != 2 && this.addToGroup) {
            return false;
        }
        if (z && this.addToGroup) {
            if (getParentActivity() == null) {
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.formatPluralString("AddManyMembersAlertTitle", this.selectedContacts.size(), new Object[0]));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.selectedContacts.size(); i++) {
                TLRPC.User user = getMessagesController().getUser(Long.valueOf(this.selectedContacts.keyAt(i)));
                if (user != null) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append("**");
                    sb.append(ContactsController.formatName(user.first_name, user.last_name));
                    sb.append("**");
                }
            }
            MessagesController messagesController = getMessagesController();
            long j = this.chatId;
            if (j == 0) {
                j = this.channelId;
            }
            TLRPC.Chat chat = messagesController.getChat(Long.valueOf(j));
            if (this.selectedContacts.size() > 5) {
                replaceTags = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatPluralString("AddManyMembersAlertNamesText", this.selectedContacts.size(), chat == null ? "" : chat.title)));
                String format = String.format("%d", Integer.valueOf(this.selectedContacts.size()));
                int indexOf = TextUtils.indexOf(replaceTags, format);
                if (indexOf >= 0) {
                    replaceTags.setSpan(new TypefaceSpan(AndroidUtilities.bold()), indexOf, format.length() + indexOf, 33);
                }
            } else {
                replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", R.string.AddMembersAlertNamesText, sb, chat == null ? "" : chat.title));
            }
            builder.setMessage(replaceTags);
            final CheckBoxCell[] checkBoxCellArr = new CheckBoxCell[1];
            if (!ChatObject.isChannel(chat)) {
                LinearLayout linearLayout = new LinearLayout(getParentActivity());
                linearLayout.setOrientation(1);
                CheckBoxCell checkBoxCell = new CheckBoxCell(getParentActivity(), 1, this.resourceProvider);
                checkBoxCellArr[0] = checkBoxCell;
                checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                checkBoxCellArr[0].setMultiline(true);
                if (this.selectedContacts.size() == 1) {
                    checkBoxCellArr[0].setText(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AddOneMemberForwardMessages, UserObject.getFirstName(getMessagesController().getUser(Long.valueOf(this.selectedContacts.keyAt(0)))))), "", true, false);
                } else {
                    checkBoxCellArr[0].setText(LocaleController.getString(R.string.AddMembersForwardMessages), "", true, false);
                }
                checkBoxCellArr[0].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                linearLayout.addView(checkBoxCellArr[0], LayoutHelper.createLinear(-1, -2));
                checkBoxCellArr[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda7
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        GroupCreateActivity.lambda$onDonePressed$7(checkBoxCellArr, view);
                    }
                });
                builder.setView(linearLayout);
            }
            builder.setPositiveButton(LocaleController.getString(R.string.Add), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda8
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    GroupCreateActivity.this.lambda$onDonePressed$8(checkBoxCellArr, dialogInterface, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            showDialog(builder.create());
        } else if (this.chatType == 2) {
            ArrayList<TLRPC.InputUser> arrayList = new ArrayList<>();
            for (int i2 = 0; i2 < this.selectedContacts.size(); i2++) {
                TLRPC.InputUser inputUser = getMessagesController().getInputUser(getMessagesController().getUser(Long.valueOf(this.selectedContacts.keyAt(i2))));
                if (inputUser != null) {
                    arrayList.add(inputUser);
                }
            }
            getMessagesController().addUsersToChannel(this.chatId, arrayList, null);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", this.chatId);
            bundle.putBoolean("just_created_chat", true);
            presentFragment(new ChatActivity(bundle), true);
        } else {
            if (!this.doneButtonVisible) {
                return false;
            }
            if (this.addToGroup) {
                onAddToGroupDone(0);
            } else {
                ArrayList arrayList2 = new ArrayList();
                for (int i3 = 0; i3 < this.selectedContacts.size(); i3++) {
                    arrayList2.add(Long.valueOf(this.selectedContacts.keyAt(i3)));
                }
                if (this.isAlwaysShare || this.isNeverShare) {
                    GroupCreateActivityDelegate groupCreateActivityDelegate = this.delegate;
                    if (groupCreateActivityDelegate != null) {
                        groupCreateActivityDelegate.didSelectUsers(this.selectedPremium != null, this.selectedMiniapps != null, arrayList2);
                    }
                    lambda$onBackPressed$321();
                } else {
                    Bundle bundle2 = new Bundle();
                    int size = arrayList2.size();
                    long[] jArr = new long[size];
                    for (int i4 = 0; i4 < size; i4++) {
                        jArr[i4] = ((Long) arrayList2.get(i4)).longValue();
                    }
                    bundle2.putLongArray("result", jArr);
                    bundle2.putInt("chatType", this.chatType);
                    bundle2.putBoolean("forImport", this.forImport);
                    presentFragment(new GroupCreateFinalActivity(bundle2));
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showItemsAnimated(final int i) {
        if (this.isPaused) {
            return;
        }
        this.listView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.GroupCreateActivity.10
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                GroupCreateActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
                int childCount = GroupCreateActivity.this.listView.getChildCount();
                AnimatorSet animatorSet = new AnimatorSet();
                for (int i2 = 0; i2 < childCount; i2++) {
                    View childAt = GroupCreateActivity.this.listView.getChildAt(i2);
                    if (GroupCreateActivity.this.listView.getChildAdapterPosition(childAt) >= i) {
                        childAt.setAlpha(0.0f);
                        int min = (int) ((Math.min(GroupCreateActivity.this.listView.getMeasuredHeight(), Math.max(0, childAt.getTop())) / GroupCreateActivity.this.listView.getMeasuredHeight()) * 100.0f);
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(childAt, (Property<View, Float>) View.ALPHA, 0.0f, 1.0f);
                        ofFloat.setStartDelay(min);
                        ofFloat.setDuration(200L);
                        animatorSet.playTogether(ofFloat);
                    }
                }
                animatorSet.start();
                return true;
            }
        });
    }

    private void showPremiumBlockedToast(View view, long j) {
        int i = -this.shiftDp;
        this.shiftDp = i;
        AndroidUtilities.shakeViewSpring(view, i);
        BotWebViewVibrationEffect.APP_ERROR.vibrate();
        String userName = j >= 0 ? UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j))) : "";
        (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() ? BulletinFactory.of(this).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedNonPremium, userName))) : BulletinFactory.of(this).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedNonPremium, userName)), LocaleController.getString(R.string.UserBlockedNonPremiumButton), new Runnable() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                GroupCreateActivity.this.lambda$showPremiumBlockedToast$5();
            }
        })).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEditTextHint() {
        int i;
        GroupCreateAdapter groupCreateAdapter;
        EditTextBoldCursor editTextBoldCursor = this.editText;
        if (editTextBoldCursor == null) {
            return;
        }
        if (this.chatType == 2) {
            i = R.string.AddMutual;
        } else if (this.addToGroup || ((groupCreateAdapter = this.adapter) != null && groupCreateAdapter.noContactsStubRow == 0)) {
            editTextBoldCursor = this.editText;
            i = R.string.SearchForPeople;
        } else if (this.isAlwaysShare || this.isNeverShare) {
            editTextBoldCursor = this.editText;
            i = R.string.SearchForPeopleAndGroups;
        } else {
            editTextBoldCursor = this.editText;
            i = R.string.SendMessageTo;
        }
        editTextBoldCursor.setHintText(LocaleController.getString(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHint() {
        ActionBar actionBar;
        String formatString;
        if (!this.isAlwaysShare && !this.isNeverShare && !this.addToGroup) {
            if (this.chatType == 2) {
                actionBar = this.actionBar;
                formatString = LocaleController.formatPluralString("Members", this.selectedContacts.size(), new Object[0]);
            } else if (this.selectedContacts.size() == 0) {
                actionBar = this.actionBar;
                formatString = LocaleController.formatString("MembersCountZero", R.string.MembersCountZero, LocaleController.formatPluralString("Members", this.maxCount, new Object[0]));
            } else {
                this.actionBar.setSubtitle(String.format(LocaleController.getPluralString("MembersCountSelected", this.selectedContacts.size()), Integer.valueOf(this.selectedContacts.size()), Integer.valueOf(this.maxCount)));
            }
            actionBar.setSubtitle(formatString);
        }
        if (this.chatType == 2 || !this.addToGroup) {
            return;
        }
        if (this.doneButtonVisible && this.allSpans.isEmpty()) {
            AnimatorSet animatorSet = this.currentDoneButtonAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.currentDoneButtonAnimation = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.ALPHA, 0.0f));
            this.currentDoneButtonAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.GroupCreateActivity.11
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    GroupCreateActivity.this.floatingButton.setVisibility(4);
                }
            });
            this.currentDoneButtonAnimation.setDuration(180L);
            this.currentDoneButtonAnimation.start();
            this.doneButtonVisible = false;
            return;
        }
        if (this.doneButtonVisible || this.allSpans.isEmpty()) {
            return;
        }
        AnimatorSet animatorSet3 = this.currentDoneButtonAnimation;
        if (animatorSet3 != null) {
            animatorSet3.cancel();
        }
        this.currentDoneButtonAnimation = new AnimatorSet();
        this.floatingButton.setVisibility(0);
        this.currentDoneButtonAnimation.playTogether(ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) View.ALPHA, 1.0f));
        this.currentDoneButtonAnimation.setDuration(180L);
        this.currentDoneButtonAnimation.start();
        this.doneButtonVisible = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0151  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x018f  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0293  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x02b5  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0327  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x01f7  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0153  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        ActionBar actionBar;
        int i;
        ArrayList arrayList;
        int i2;
        this.searching = false;
        this.searchWas = false;
        this.allSpans.clear();
        this.selectedContacts.clear();
        this.currentDeletingSpan = null;
        if (this.chatType == 2) {
            this.doneButtonVisible = true;
        } else {
            this.doneButtonVisible = !this.addToGroup;
        }
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        int i3 = this.chatType;
        if (i3 != 2) {
            if (this.addToGroup) {
                if (this.channelId == 0) {
                    actionBar = this.actionBar;
                    i = R.string.GroupAddMembers;
                }
            } else if (this.isAlwaysShare) {
                int i4 = this.chatAddType;
                if (i4 == 2) {
                    actionBar = this.actionBar;
                    i = R.string.FilterAlwaysShow;
                } else if (i4 == 1) {
                    actionBar = this.actionBar;
                    i = R.string.AlwaysAllow;
                } else {
                    actionBar = this.actionBar;
                    i = R.string.AlwaysShareWithTitle;
                }
            } else {
                if (!this.isNeverShare) {
                    this.actionBar.setTitle(LocaleController.getString(i3 == 0 ? R.string.NewGroup : R.string.NewBroadcastList));
                    this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.GroupCreateActivity.1
                        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
                        public void onItemClick(int i5) {
                            if (i5 == -1) {
                                GroupCreateActivity.this.lambda$onBackPressed$321();
                            } else if (i5 == 1) {
                                GroupCreateActivity.this.onDonePressed(true);
                            }
                        }
                    });
                    ViewGroup viewGroup = new ViewGroup(context) { // from class: org.telegram.ui.GroupCreateActivity.2
                        private VerticalPositionAutoAnimator verticalPositionAutoAnimator;

                        @Override // android.view.ViewGroup, android.view.View
                        protected void dispatchDraw(Canvas canvas) {
                            super.dispatchDraw(canvas);
                            INavigationLayout iNavigationLayout = ((BaseFragment) GroupCreateActivity.this).parentLayout;
                            GroupCreateActivity groupCreateActivity = GroupCreateActivity.this;
                            iNavigationLayout.drawHeaderShadow(canvas, Math.min(groupCreateActivity.maxSize, (groupCreateActivity.measuredContainerHeight + GroupCreateActivity.this.containerHeight) - GroupCreateActivity.this.measuredContainerHeight));
                        }

                        @Override // android.view.ViewGroup
                        protected boolean drawChild(Canvas canvas, View view, long j) {
                            int left;
                            int top;
                            int right;
                            int min;
                            if (view == GroupCreateActivity.this.listView) {
                                canvas.save();
                                left = view.getLeft();
                                GroupCreateActivity groupCreateActivity = GroupCreateActivity.this;
                                top = Math.min(groupCreateActivity.maxSize, (groupCreateActivity.measuredContainerHeight + GroupCreateActivity.this.containerHeight) - GroupCreateActivity.this.measuredContainerHeight);
                                right = view.getRight();
                                min = view.getBottom();
                            } else {
                                if (view != GroupCreateActivity.this.scrollView) {
                                    return super.drawChild(canvas, view, j);
                                }
                                canvas.save();
                                left = view.getLeft();
                                top = view.getTop();
                                right = view.getRight();
                                GroupCreateActivity groupCreateActivity2 = GroupCreateActivity.this;
                                min = Math.min(groupCreateActivity2.maxSize, (groupCreateActivity2.measuredContainerHeight + GroupCreateActivity.this.containerHeight) - GroupCreateActivity.this.measuredContainerHeight);
                            }
                            canvas.clipRect(left, top, right, min);
                            boolean drawChild = super.drawChild(canvas, view, j);
                            canvas.restore();
                            return drawChild;
                        }

                        @Override // android.view.ViewGroup, android.view.View
                        protected void onAttachedToWindow() {
                            super.onAttachedToWindow();
                            VerticalPositionAutoAnimator verticalPositionAutoAnimator = this.verticalPositionAutoAnimator;
                            if (verticalPositionAutoAnimator != null) {
                                verticalPositionAutoAnimator.ignoreNextLayout();
                            }
                        }

                        @Override // android.view.ViewGroup, android.view.View
                        protected void onLayout(boolean z, int i5, int i6, int i7, int i8) {
                            GroupCreateActivity.this.scrollView.layout(0, 0, GroupCreateActivity.this.scrollView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight());
                            GroupCreateActivity.this.listView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.listView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.listView.getMeasuredHeight());
                            GroupCreateActivity.this.emptyView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.emptyView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.emptyView.getMeasuredHeight());
                            if (GroupCreateActivity.this.floatingButton != null) {
                                int dp = LocaleController.isRTL ? AndroidUtilities.dp(14.0f) : ((i7 - i5) - AndroidUtilities.dp(14.0f)) - GroupCreateActivity.this.floatingButton.getMeasuredWidth();
                                int dp2 = ((i8 - i6) - AndroidUtilities.dp(14.0f)) - GroupCreateActivity.this.floatingButton.getMeasuredHeight();
                                GroupCreateActivity.this.floatingButton.layout(dp, dp2, GroupCreateActivity.this.floatingButton.getMeasuredWidth() + dp, GroupCreateActivity.this.floatingButton.getMeasuredHeight() + dp2);
                            }
                        }

                        @Override // android.view.View
                        protected void onMeasure(int i5, int i6) {
                            GroupCreateActivity groupCreateActivity;
                            int dp;
                            int size = View.MeasureSpec.getSize(i5);
                            int size2 = View.MeasureSpec.getSize(i6);
                            setMeasuredDimension(size, size2);
                            if (AndroidUtilities.isTablet() || size2 > size) {
                                groupCreateActivity = GroupCreateActivity.this;
                                dp = AndroidUtilities.dp(144.0f);
                            } else {
                                groupCreateActivity = GroupCreateActivity.this;
                                dp = AndroidUtilities.dp(56.0f);
                            }
                            groupCreateActivity.maxSize = dp;
                            GroupCreateActivity.this.scrollView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(GroupCreateActivity.this.maxSize, Integer.MIN_VALUE));
                            GroupCreateActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                            GroupCreateActivity.this.emptyView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                            if (GroupCreateActivity.this.floatingButton != null) {
                                int dp2 = AndroidUtilities.dp(Build.VERSION.SDK_INT < 21 ? 60.0f : 56.0f);
                                GroupCreateActivity.this.floatingButton.measure(View.MeasureSpec.makeMeasureSpec(dp2, 1073741824), View.MeasureSpec.makeMeasureSpec(dp2, 1073741824));
                            }
                        }

                        @Override // android.view.ViewGroup
                        public void onViewAdded(View view) {
                            if (view == GroupCreateActivity.this.floatingButton && this.verticalPositionAutoAnimator == null) {
                                this.verticalPositionAutoAnimator = VerticalPositionAutoAnimator.attach(view);
                            }
                        }
                    };
                    this.fragmentView = viewGroup;
                    viewGroup.setFocusableInTouchMode(true);
                    viewGroup.setDescendantFocusability(131072);
                    ScrollView scrollView = new ScrollView(context) { // from class: org.telegram.ui.GroupCreateActivity.3
                        @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
                        public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
                            if (GroupCreateActivity.this.ignoreScrollEvent) {
                                GroupCreateActivity.this.ignoreScrollEvent = false;
                                return false;
                            }
                            rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
                            rect.top += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(20.0f);
                            rect.bottom += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(50.0f);
                            return super.requestChildRectangleOnScreen(view, rect, z);
                        }
                    };
                    this.scrollView = scrollView;
                    scrollView.setClipChildren(false);
                    viewGroup.setClipChildren(false);
                    this.scrollView.setVerticalScrollBarEnabled(false);
                    AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor(Theme.key_windowBackgroundWhite));
                    viewGroup.addView(this.scrollView);
                    SpansContainer spansContainer = new SpansContainer(context);
                    this.spansContainer = spansContainer;
                    this.scrollView.addView(spansContainer, LayoutHelper.createFrame(-1, -2.0f));
                    this.spansContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            GroupCreateActivity.this.lambda$createView$0(view);
                        }
                    });
                    EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context) { // from class: org.telegram.ui.GroupCreateActivity.4
                        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            if (GroupCreateActivity.this.currentDeletingSpan != null) {
                                GroupCreateActivity.this.currentDeletingSpan.cancelDeleteAnimation();
                                GroupCreateActivity.this.currentDeletingSpan = null;
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
                    this.editText.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
                    this.spansContainer.addView(this.editText);
                    updateEditTextHint();
                    this.editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: org.telegram.ui.GroupCreateActivity.5
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
                    this.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda1
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView, int i5, KeyEvent keyEvent) {
                            boolean lambda$createView$1;
                            lambda$createView$1 = GroupCreateActivity.this.lambda$createView$1(textView, i5, keyEvent);
                            return lambda$createView$1;
                        }
                    });
                    this.editText.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.GroupCreateActivity.6
                        private boolean wasEmpty;

                        @Override // android.view.View.OnKeyListener
                        public boolean onKey(View view, int i5, KeyEvent keyEvent) {
                            if (i5 == 67) {
                                if (keyEvent.getAction() == 0) {
                                    this.wasEmpty = GroupCreateActivity.this.editText.length() == 0;
                                } else if (keyEvent.getAction() == 1 && this.wasEmpty && !GroupCreateActivity.this.allSpans.isEmpty()) {
                                    GroupCreateActivity.this.spansContainer.removeSpan((GroupCreateSpan) GroupCreateActivity.this.allSpans.get(GroupCreateActivity.this.allSpans.size() - 1));
                                    GroupCreateActivity.this.updateHint();
                                    GroupCreateActivity.this.checkVisibleRows();
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    this.editText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.GroupCreateActivity.7
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            if (GroupCreateActivity.this.editText.length() == 0) {
                                GroupCreateActivity.this.closeSearch();
                                return;
                            }
                            if (!GroupCreateActivity.this.adapter.searching) {
                                GroupCreateActivity.this.searching = true;
                                GroupCreateActivity.this.searchWas = true;
                                GroupCreateActivity.this.adapter.setSearching(true);
                                GroupCreateActivity.this.itemDecoration.setSearching(true);
                                GroupCreateActivity.this.listView.setFastScrollVisible(false);
                                GroupCreateActivity.this.listView.setVerticalScrollBarEnabled(true);
                            }
                            GroupCreateActivity.this.adapter.searchDialogs(GroupCreateActivity.this.editText.getText().toString());
                            GroupCreateActivity.this.emptyView.showProgress(true, false);
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                        }
                    });
                    arrayList = this.toSelectIds;
                    if (arrayList != null) {
                        select(arrayList, this.toSelectPremium, this.toSelectMiniapps);
                    }
                    FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
                    flickerLoadingView.setViewType(6);
                    flickerLoadingView.showDate(false);
                    StickerEmptyView stickerEmptyView = new StickerEmptyView(context, flickerLoadingView, 1);
                    this.emptyView = stickerEmptyView;
                    stickerEmptyView.addView(flickerLoadingView);
                    this.emptyView.showProgress(true, false);
                    this.emptyView.title.setText(LocaleController.getString(R.string.NoResult));
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
                    RecyclerListView recyclerListView3 = this.listView;
                    GroupCreateDividerItemDecoration groupCreateDividerItemDecoration = new GroupCreateDividerItemDecoration();
                    this.itemDecoration = groupCreateDividerItemDecoration;
                    recyclerListView3.addItemDecoration(groupCreateDividerItemDecoration);
                    viewGroup.addView(this.listView);
                    this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda2
                        @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                        public final void onItemClick(View view, int i5) {
                            GroupCreateActivity.this.lambda$createView$3(context, view, i5);
                        }
                    });
                    this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.GroupCreateActivity.8
                        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                        public void onScrollStateChanged(RecyclerView recyclerView, int i5) {
                            if (i5 == 1) {
                                GroupCreateActivity.this.editText.hideActionMode();
                                AndroidUtilities.hideKeyboard(GroupCreateActivity.this.editText);
                            }
                        }
                    });
                    this.listView.setAnimateEmptyView(true, 0);
                    ImageView imageView = new ImageView(context);
                    this.floatingButton = imageView;
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
                    i2 = Build.VERSION.SDK_INT;
                    if (i2 < 21) {
                        Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
                        mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                        CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
                        combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        createSimpleSelectorCircleDrawable = combinedDrawable;
                    }
                    this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
                    this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
                    if (!this.isNeverShare || this.isAlwaysShare || this.addToGroup) {
                        this.floatingButton.setImageResource(R.drawable.floating_check);
                    } else {
                        BackDrawable backDrawable = new BackDrawable(false);
                        backDrawable.setArrowRotation(NotificationCenter.updateBotMenuButton);
                        this.floatingButton.setImageDrawable(backDrawable);
                    }
                    if (i2 >= 21) {
                        StateListAnimator stateListAnimator = new StateListAnimator();
                        stateListAnimator.addState(new int[]{android.R.attr.state_pressed}, ObjectAnimator.ofFloat(this.floatingButton, "translationZ", AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButton, "translationZ", AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                        this.floatingButton.setStateListAnimator(stateListAnimator);
                        this.floatingButton.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.GroupCreateActivity.9
                            @Override // android.view.ViewOutlineProvider
                            public void getOutline(View view, Outline outline) {
                                outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                            }
                        });
                    }
                    viewGroup.addView(this.floatingButton);
                    this.floatingButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            GroupCreateActivity.this.lambda$createView$4(view);
                        }
                    });
                    if (!this.doneButtonVisible) {
                        this.floatingButton.setVisibility(4);
                        this.floatingButton.setScaleX(0.0f);
                        this.floatingButton.setScaleY(0.0f);
                        this.floatingButton.setAlpha(0.0f);
                    }
                    this.floatingButton.setContentDescription(LocaleController.getString(R.string.Next));
                    updateHint();
                    return this.fragmentView;
                }
                int i5 = this.chatAddType;
                if (i5 == 2) {
                    actionBar = this.actionBar;
                    i = R.string.FilterNeverShow;
                } else if (i5 == 1) {
                    actionBar = this.actionBar;
                    i = R.string.NeverAllow;
                } else {
                    actionBar = this.actionBar;
                    i = R.string.NeverShareWithTitle;
                }
            }
            actionBar.setTitle(LocaleController.getString(i));
            this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.GroupCreateActivity.1
                @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
                public void onItemClick(int i52) {
                    if (i52 == -1) {
                        GroupCreateActivity.this.lambda$onBackPressed$321();
                    } else if (i52 == 1) {
                        GroupCreateActivity.this.onDonePressed(true);
                    }
                }
            });
            ViewGroup viewGroup2 = new ViewGroup(context) { // from class: org.telegram.ui.GroupCreateActivity.2
                private VerticalPositionAutoAnimator verticalPositionAutoAnimator;

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    super.dispatchDraw(canvas);
                    INavigationLayout iNavigationLayout = ((BaseFragment) GroupCreateActivity.this).parentLayout;
                    GroupCreateActivity groupCreateActivity = GroupCreateActivity.this;
                    iNavigationLayout.drawHeaderShadow(canvas, Math.min(groupCreateActivity.maxSize, (groupCreateActivity.measuredContainerHeight + GroupCreateActivity.this.containerHeight) - GroupCreateActivity.this.measuredContainerHeight));
                }

                @Override // android.view.ViewGroup
                protected boolean drawChild(Canvas canvas, View view, long j) {
                    int left;
                    int top;
                    int right;
                    int min;
                    if (view == GroupCreateActivity.this.listView) {
                        canvas.save();
                        left = view.getLeft();
                        GroupCreateActivity groupCreateActivity = GroupCreateActivity.this;
                        top = Math.min(groupCreateActivity.maxSize, (groupCreateActivity.measuredContainerHeight + GroupCreateActivity.this.containerHeight) - GroupCreateActivity.this.measuredContainerHeight);
                        right = view.getRight();
                        min = view.getBottom();
                    } else {
                        if (view != GroupCreateActivity.this.scrollView) {
                            return super.drawChild(canvas, view, j);
                        }
                        canvas.save();
                        left = view.getLeft();
                        top = view.getTop();
                        right = view.getRight();
                        GroupCreateActivity groupCreateActivity2 = GroupCreateActivity.this;
                        min = Math.min(groupCreateActivity2.maxSize, (groupCreateActivity2.measuredContainerHeight + GroupCreateActivity.this.containerHeight) - GroupCreateActivity.this.measuredContainerHeight);
                    }
                    canvas.clipRect(left, top, right, min);
                    boolean drawChild = super.drawChild(canvas, view, j);
                    canvas.restore();
                    return drawChild;
                }

                @Override // android.view.ViewGroup, android.view.View
                protected void onAttachedToWindow() {
                    super.onAttachedToWindow();
                    VerticalPositionAutoAnimator verticalPositionAutoAnimator = this.verticalPositionAutoAnimator;
                    if (verticalPositionAutoAnimator != null) {
                        verticalPositionAutoAnimator.ignoreNextLayout();
                    }
                }

                @Override // android.view.ViewGroup, android.view.View
                protected void onLayout(boolean z, int i52, int i6, int i7, int i8) {
                    GroupCreateActivity.this.scrollView.layout(0, 0, GroupCreateActivity.this.scrollView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight());
                    GroupCreateActivity.this.listView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.listView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.listView.getMeasuredHeight());
                    GroupCreateActivity.this.emptyView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.emptyView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.emptyView.getMeasuredHeight());
                    if (GroupCreateActivity.this.floatingButton != null) {
                        int dp = LocaleController.isRTL ? AndroidUtilities.dp(14.0f) : ((i7 - i52) - AndroidUtilities.dp(14.0f)) - GroupCreateActivity.this.floatingButton.getMeasuredWidth();
                        int dp2 = ((i8 - i6) - AndroidUtilities.dp(14.0f)) - GroupCreateActivity.this.floatingButton.getMeasuredHeight();
                        GroupCreateActivity.this.floatingButton.layout(dp, dp2, GroupCreateActivity.this.floatingButton.getMeasuredWidth() + dp, GroupCreateActivity.this.floatingButton.getMeasuredHeight() + dp2);
                    }
                }

                @Override // android.view.View
                protected void onMeasure(int i52, int i6) {
                    GroupCreateActivity groupCreateActivity;
                    int dp;
                    int size = View.MeasureSpec.getSize(i52);
                    int size2 = View.MeasureSpec.getSize(i6);
                    setMeasuredDimension(size, size2);
                    if (AndroidUtilities.isTablet() || size2 > size) {
                        groupCreateActivity = GroupCreateActivity.this;
                        dp = AndroidUtilities.dp(144.0f);
                    } else {
                        groupCreateActivity = GroupCreateActivity.this;
                        dp = AndroidUtilities.dp(56.0f);
                    }
                    groupCreateActivity.maxSize = dp;
                    GroupCreateActivity.this.scrollView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(GroupCreateActivity.this.maxSize, Integer.MIN_VALUE));
                    GroupCreateActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                    GroupCreateActivity.this.emptyView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                    if (GroupCreateActivity.this.floatingButton != null) {
                        int dp2 = AndroidUtilities.dp(Build.VERSION.SDK_INT < 21 ? 60.0f : 56.0f);
                        GroupCreateActivity.this.floatingButton.measure(View.MeasureSpec.makeMeasureSpec(dp2, 1073741824), View.MeasureSpec.makeMeasureSpec(dp2, 1073741824));
                    }
                }

                @Override // android.view.ViewGroup
                public void onViewAdded(View view) {
                    if (view == GroupCreateActivity.this.floatingButton && this.verticalPositionAutoAnimator == null) {
                        this.verticalPositionAutoAnimator = VerticalPositionAutoAnimator.attach(view);
                    }
                }
            };
            this.fragmentView = viewGroup2;
            viewGroup2.setFocusableInTouchMode(true);
            viewGroup2.setDescendantFocusability(131072);
            ScrollView scrollView2 = new ScrollView(context) { // from class: org.telegram.ui.GroupCreateActivity.3
                @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
                public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
                    if (GroupCreateActivity.this.ignoreScrollEvent) {
                        GroupCreateActivity.this.ignoreScrollEvent = false;
                        return false;
                    }
                    rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
                    rect.top += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(20.0f);
                    rect.bottom += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(50.0f);
                    return super.requestChildRectangleOnScreen(view, rect, z);
                }
            };
            this.scrollView = scrollView2;
            scrollView2.setClipChildren(false);
            viewGroup2.setClipChildren(false);
            this.scrollView.setVerticalScrollBarEnabled(false);
            AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor(Theme.key_windowBackgroundWhite));
            viewGroup2.addView(this.scrollView);
            SpansContainer spansContainer2 = new SpansContainer(context);
            this.spansContainer = spansContainer2;
            this.scrollView.addView(spansContainer2, LayoutHelper.createFrame(-1, -2.0f));
            this.spansContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCreateActivity.this.lambda$createView$0(view);
                }
            });
            EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context) { // from class: org.telegram.ui.GroupCreateActivity.4
                @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    if (GroupCreateActivity.this.currentDeletingSpan != null) {
                        GroupCreateActivity.this.currentDeletingSpan.cancelDeleteAnimation();
                        GroupCreateActivity.this.currentDeletingSpan = null;
                    }
                    if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
                        clearFocus();
                        requestFocus();
                    }
                    return super.onTouchEvent(motionEvent);
                }
            };
            this.editText = editTextBoldCursor2;
            editTextBoldCursor2.setTextSize(1, 16.0f);
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
            this.editText.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            this.spansContainer.addView(this.editText);
            updateEditTextHint();
            this.editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: org.telegram.ui.GroupCreateActivity.5
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
            this.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda1
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView, int i52, KeyEvent keyEvent) {
                    boolean lambda$createView$1;
                    lambda$createView$1 = GroupCreateActivity.this.lambda$createView$1(textView, i52, keyEvent);
                    return lambda$createView$1;
                }
            });
            this.editText.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.GroupCreateActivity.6
                private boolean wasEmpty;

                @Override // android.view.View.OnKeyListener
                public boolean onKey(View view, int i52, KeyEvent keyEvent) {
                    if (i52 == 67) {
                        if (keyEvent.getAction() == 0) {
                            this.wasEmpty = GroupCreateActivity.this.editText.length() == 0;
                        } else if (keyEvent.getAction() == 1 && this.wasEmpty && !GroupCreateActivity.this.allSpans.isEmpty()) {
                            GroupCreateActivity.this.spansContainer.removeSpan((GroupCreateSpan) GroupCreateActivity.this.allSpans.get(GroupCreateActivity.this.allSpans.size() - 1));
                            GroupCreateActivity.this.updateHint();
                            GroupCreateActivity.this.checkVisibleRows();
                            return true;
                        }
                    }
                    return false;
                }
            });
            this.editText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.GroupCreateActivity.7
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    if (GroupCreateActivity.this.editText.length() == 0) {
                        GroupCreateActivity.this.closeSearch();
                        return;
                    }
                    if (!GroupCreateActivity.this.adapter.searching) {
                        GroupCreateActivity.this.searching = true;
                        GroupCreateActivity.this.searchWas = true;
                        GroupCreateActivity.this.adapter.setSearching(true);
                        GroupCreateActivity.this.itemDecoration.setSearching(true);
                        GroupCreateActivity.this.listView.setFastScrollVisible(false);
                        GroupCreateActivity.this.listView.setVerticalScrollBarEnabled(true);
                    }
                    GroupCreateActivity.this.adapter.searchDialogs(GroupCreateActivity.this.editText.getText().toString());
                    GroupCreateActivity.this.emptyView.showProgress(true, false);
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i52, int i6, int i7) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i52, int i6, int i7) {
                }
            });
            arrayList = this.toSelectIds;
            if (arrayList != null) {
            }
            FlickerLoadingView flickerLoadingView2 = new FlickerLoadingView(context);
            flickerLoadingView2.setViewType(6);
            flickerLoadingView2.showDate(false);
            StickerEmptyView stickerEmptyView2 = new StickerEmptyView(context, flickerLoadingView2, 1);
            this.emptyView = stickerEmptyView2;
            stickerEmptyView2.addView(flickerLoadingView2);
            this.emptyView.showProgress(true, false);
            this.emptyView.title.setText(LocaleController.getString(R.string.NoResult));
            viewGroup2.addView(this.emptyView);
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context, 1, false);
            RecyclerListView recyclerListView4 = new RecyclerListView(context);
            this.listView = recyclerListView4;
            recyclerListView4.setFastScrollEnabled(0);
            this.listView.setEmptyView(this.emptyView);
            RecyclerListView recyclerListView22 = this.listView;
            GroupCreateAdapter groupCreateAdapter2 = new GroupCreateAdapter(context);
            this.adapter = groupCreateAdapter2;
            recyclerListView22.setAdapter(groupCreateAdapter2);
            this.listView.setLayoutManager(linearLayoutManager2);
            this.listView.setVerticalScrollBarEnabled(false);
            this.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
            RecyclerListView recyclerListView32 = this.listView;
            GroupCreateDividerItemDecoration groupCreateDividerItemDecoration2 = new GroupCreateDividerItemDecoration();
            this.itemDecoration = groupCreateDividerItemDecoration2;
            recyclerListView32.addItemDecoration(groupCreateDividerItemDecoration2);
            viewGroup2.addView(this.listView);
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i52) {
                    GroupCreateActivity.this.lambda$createView$3(context, view, i52);
                }
            });
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.GroupCreateActivity.8
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i52) {
                    if (i52 == 1) {
                        GroupCreateActivity.this.editText.hideActionMode();
                        AndroidUtilities.hideKeyboard(GroupCreateActivity.this.editText);
                    }
                }
            });
            this.listView.setAnimateEmptyView(true, 0);
            ImageView imageView2 = new ImageView(context);
            this.floatingButton = imageView2;
            imageView2.setScaleType(ImageView.ScaleType.CENTER);
            Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
            i2 = Build.VERSION.SDK_INT;
            if (i2 < 21) {
            }
            this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable2);
            this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
            if (this.isNeverShare) {
            }
            this.floatingButton.setImageResource(R.drawable.floating_check);
            if (i2 >= 21) {
            }
            viewGroup2.addView(this.floatingButton);
            this.floatingButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupCreateActivity.this.lambda$createView$4(view);
                }
            });
            if (!this.doneButtonVisible) {
            }
            this.floatingButton.setContentDescription(LocaleController.getString(R.string.Next));
            updateHint();
            return this.fragmentView;
        }
        actionBar = this.actionBar;
        i = R.string.ChannelAddSubscribers;
        actionBar.setTitle(LocaleController.getString(i));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.GroupCreateActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i52) {
                if (i52 == -1) {
                    GroupCreateActivity.this.lambda$onBackPressed$321();
                } else if (i52 == 1) {
                    GroupCreateActivity.this.onDonePressed(true);
                }
            }
        });
        ViewGroup viewGroup22 = new ViewGroup(context) { // from class: org.telegram.ui.GroupCreateActivity.2
            private VerticalPositionAutoAnimator verticalPositionAutoAnimator;

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                INavigationLayout iNavigationLayout = ((BaseFragment) GroupCreateActivity.this).parentLayout;
                GroupCreateActivity groupCreateActivity = GroupCreateActivity.this;
                iNavigationLayout.drawHeaderShadow(canvas, Math.min(groupCreateActivity.maxSize, (groupCreateActivity.measuredContainerHeight + GroupCreateActivity.this.containerHeight) - GroupCreateActivity.this.measuredContainerHeight));
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                int left;
                int top;
                int right;
                int min;
                if (view == GroupCreateActivity.this.listView) {
                    canvas.save();
                    left = view.getLeft();
                    GroupCreateActivity groupCreateActivity = GroupCreateActivity.this;
                    top = Math.min(groupCreateActivity.maxSize, (groupCreateActivity.measuredContainerHeight + GroupCreateActivity.this.containerHeight) - GroupCreateActivity.this.measuredContainerHeight);
                    right = view.getRight();
                    min = view.getBottom();
                } else {
                    if (view != GroupCreateActivity.this.scrollView) {
                        return super.drawChild(canvas, view, j);
                    }
                    canvas.save();
                    left = view.getLeft();
                    top = view.getTop();
                    right = view.getRight();
                    GroupCreateActivity groupCreateActivity2 = GroupCreateActivity.this;
                    min = Math.min(groupCreateActivity2.maxSize, (groupCreateActivity2.measuredContainerHeight + GroupCreateActivity.this.containerHeight) - GroupCreateActivity.this.measuredContainerHeight);
                }
                canvas.clipRect(left, top, right, min);
                boolean drawChild = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild;
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                VerticalPositionAutoAnimator verticalPositionAutoAnimator = this.verticalPositionAutoAnimator;
                if (verticalPositionAutoAnimator != null) {
                    verticalPositionAutoAnimator.ignoreNextLayout();
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i52, int i6, int i7, int i8) {
                GroupCreateActivity.this.scrollView.layout(0, 0, GroupCreateActivity.this.scrollView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight());
                GroupCreateActivity.this.listView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.listView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.listView.getMeasuredHeight());
                GroupCreateActivity.this.emptyView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.emptyView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.emptyView.getMeasuredHeight());
                if (GroupCreateActivity.this.floatingButton != null) {
                    int dp = LocaleController.isRTL ? AndroidUtilities.dp(14.0f) : ((i7 - i52) - AndroidUtilities.dp(14.0f)) - GroupCreateActivity.this.floatingButton.getMeasuredWidth();
                    int dp2 = ((i8 - i6) - AndroidUtilities.dp(14.0f)) - GroupCreateActivity.this.floatingButton.getMeasuredHeight();
                    GroupCreateActivity.this.floatingButton.layout(dp, dp2, GroupCreateActivity.this.floatingButton.getMeasuredWidth() + dp, GroupCreateActivity.this.floatingButton.getMeasuredHeight() + dp2);
                }
            }

            @Override // android.view.View
            protected void onMeasure(int i52, int i6) {
                GroupCreateActivity groupCreateActivity;
                int dp;
                int size = View.MeasureSpec.getSize(i52);
                int size2 = View.MeasureSpec.getSize(i6);
                setMeasuredDimension(size, size2);
                if (AndroidUtilities.isTablet() || size2 > size) {
                    groupCreateActivity = GroupCreateActivity.this;
                    dp = AndroidUtilities.dp(144.0f);
                } else {
                    groupCreateActivity = GroupCreateActivity.this;
                    dp = AndroidUtilities.dp(56.0f);
                }
                groupCreateActivity.maxSize = dp;
                GroupCreateActivity.this.scrollView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(GroupCreateActivity.this.maxSize, Integer.MIN_VALUE));
                GroupCreateActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                GroupCreateActivity.this.emptyView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                if (GroupCreateActivity.this.floatingButton != null) {
                    int dp2 = AndroidUtilities.dp(Build.VERSION.SDK_INT < 21 ? 60.0f : 56.0f);
                    GroupCreateActivity.this.floatingButton.measure(View.MeasureSpec.makeMeasureSpec(dp2, 1073741824), View.MeasureSpec.makeMeasureSpec(dp2, 1073741824));
                }
            }

            @Override // android.view.ViewGroup
            public void onViewAdded(View view) {
                if (view == GroupCreateActivity.this.floatingButton && this.verticalPositionAutoAnimator == null) {
                    this.verticalPositionAutoAnimator = VerticalPositionAutoAnimator.attach(view);
                }
            }
        };
        this.fragmentView = viewGroup22;
        viewGroup22.setFocusableInTouchMode(true);
        viewGroup22.setDescendantFocusability(131072);
        ScrollView scrollView22 = new ScrollView(context) { // from class: org.telegram.ui.GroupCreateActivity.3
            @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
            public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
                if (GroupCreateActivity.this.ignoreScrollEvent) {
                    GroupCreateActivity.this.ignoreScrollEvent = false;
                    return false;
                }
                rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
                rect.top += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(20.0f);
                rect.bottom += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(50.0f);
                return super.requestChildRectangleOnScreen(view, rect, z);
            }
        };
        this.scrollView = scrollView22;
        scrollView22.setClipChildren(false);
        viewGroup22.setClipChildren(false);
        this.scrollView.setVerticalScrollBarEnabled(false);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor(Theme.key_windowBackgroundWhite));
        viewGroup22.addView(this.scrollView);
        SpansContainer spansContainer22 = new SpansContainer(context);
        this.spansContainer = spansContainer22;
        this.scrollView.addView(spansContainer22, LayoutHelper.createFrame(-1, -2.0f));
        this.spansContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCreateActivity.this.lambda$createView$0(view);
            }
        });
        EditTextBoldCursor editTextBoldCursor22 = new EditTextBoldCursor(context) { // from class: org.telegram.ui.GroupCreateActivity.4
            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (GroupCreateActivity.this.currentDeletingSpan != null) {
                    GroupCreateActivity.this.currentDeletingSpan.cancelDeleteAnimation();
                    GroupCreateActivity.this.currentDeletingSpan = null;
                }
                if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
                    clearFocus();
                    requestFocus();
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.editText = editTextBoldCursor22;
        editTextBoldCursor22.setTextSize(1, 16.0f);
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
        this.editText.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        this.spansContainer.addView(this.editText);
        updateEditTextHint();
        this.editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: org.telegram.ui.GroupCreateActivity.5
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
        this.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda1
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i52, KeyEvent keyEvent) {
                boolean lambda$createView$1;
                lambda$createView$1 = GroupCreateActivity.this.lambda$createView$1(textView, i52, keyEvent);
                return lambda$createView$1;
            }
        });
        this.editText.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.GroupCreateActivity.6
            private boolean wasEmpty;

            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int i52, KeyEvent keyEvent) {
                if (i52 == 67) {
                    if (keyEvent.getAction() == 0) {
                        this.wasEmpty = GroupCreateActivity.this.editText.length() == 0;
                    } else if (keyEvent.getAction() == 1 && this.wasEmpty && !GroupCreateActivity.this.allSpans.isEmpty()) {
                        GroupCreateActivity.this.spansContainer.removeSpan((GroupCreateSpan) GroupCreateActivity.this.allSpans.get(GroupCreateActivity.this.allSpans.size() - 1));
                        GroupCreateActivity.this.updateHint();
                        GroupCreateActivity.this.checkVisibleRows();
                        return true;
                    }
                }
                return false;
            }
        });
        this.editText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.GroupCreateActivity.7
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (GroupCreateActivity.this.editText.length() == 0) {
                    GroupCreateActivity.this.closeSearch();
                    return;
                }
                if (!GroupCreateActivity.this.adapter.searching) {
                    GroupCreateActivity.this.searching = true;
                    GroupCreateActivity.this.searchWas = true;
                    GroupCreateActivity.this.adapter.setSearching(true);
                    GroupCreateActivity.this.itemDecoration.setSearching(true);
                    GroupCreateActivity.this.listView.setFastScrollVisible(false);
                    GroupCreateActivity.this.listView.setVerticalScrollBarEnabled(true);
                }
                GroupCreateActivity.this.adapter.searchDialogs(GroupCreateActivity.this.editText.getText().toString());
                GroupCreateActivity.this.emptyView.showProgress(true, false);
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i52, int i6, int i7) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i52, int i6, int i7) {
            }
        });
        arrayList = this.toSelectIds;
        if (arrayList != null) {
        }
        FlickerLoadingView flickerLoadingView22 = new FlickerLoadingView(context);
        flickerLoadingView22.setViewType(6);
        flickerLoadingView22.showDate(false);
        StickerEmptyView stickerEmptyView22 = new StickerEmptyView(context, flickerLoadingView22, 1);
        this.emptyView = stickerEmptyView22;
        stickerEmptyView22.addView(flickerLoadingView22);
        this.emptyView.showProgress(true, false);
        this.emptyView.title.setText(LocaleController.getString(R.string.NoResult));
        viewGroup22.addView(this.emptyView);
        LinearLayoutManager linearLayoutManager22 = new LinearLayoutManager(context, 1, false);
        RecyclerListView recyclerListView42 = new RecyclerListView(context);
        this.listView = recyclerListView42;
        recyclerListView42.setFastScrollEnabled(0);
        this.listView.setEmptyView(this.emptyView);
        RecyclerListView recyclerListView222 = this.listView;
        GroupCreateAdapter groupCreateAdapter22 = new GroupCreateAdapter(context);
        this.adapter = groupCreateAdapter22;
        recyclerListView222.setAdapter(groupCreateAdapter22);
        this.listView.setLayoutManager(linearLayoutManager22);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        RecyclerListView recyclerListView322 = this.listView;
        GroupCreateDividerItemDecoration groupCreateDividerItemDecoration22 = new GroupCreateDividerItemDecoration();
        this.itemDecoration = groupCreateDividerItemDecoration22;
        recyclerListView322.addItemDecoration(groupCreateDividerItemDecoration22);
        viewGroup22.addView(this.listView);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i52) {
                GroupCreateActivity.this.lambda$createView$3(context, view, i52);
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.GroupCreateActivity.8
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i52) {
                if (i52 == 1) {
                    GroupCreateActivity.this.editText.hideActionMode();
                    AndroidUtilities.hideKeyboard(GroupCreateActivity.this.editText);
                }
            }
        });
        this.listView.setAnimateEmptyView(true, 0);
        ImageView imageView22 = new ImageView(context);
        this.floatingButton = imageView22;
        imageView22.setScaleType(ImageView.ScaleType.CENTER);
        Drawable createSimpleSelectorCircleDrawable22 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
        i2 = Build.VERSION.SDK_INT;
        if (i2 < 21) {
        }
        this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable22);
        this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
        if (this.isNeverShare) {
        }
        this.floatingButton.setImageResource(R.drawable.floating_check);
        if (i2 >= 21) {
        }
        viewGroup22.addView(this.floatingButton);
        this.floatingButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupCreateActivity.this.lambda$createView$4(view);
            }
        });
        if (!this.doneButtonVisible) {
        }
        this.floatingButton.setContentDescription(LocaleController.getString(R.string.Next));
        updateHint();
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.contactsDidLoad) {
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
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.GroupCreateActivity$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                GroupCreateActivity.this.lambda$getThemeDescriptions$9();
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
        EditTextBoldCursor editTextBoldCursor = this.editText;
        int i5 = ThemeDescription.FLAG_TEXTCOLOR;
        int i6 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(editTextBoldCursor, i5, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_groupcreate_hintText));
        arrayList.add(new ThemeDescription(this.editText, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, Theme.key_groupcreate_cursor));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GroupCreateSectionCell.class}, null, null, null, Theme.key_graySection));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GroupCreateSectionCell.class}, new String[]{"drawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_groupcreate_sectionShadow));
        int i7 = Theme.key_groupcreate_sectionText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateSectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_checkbox));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_checkboxDisabled));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_checkboxCheck));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{GroupCreateUserCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText));
        int i8 = Theme.key_windowBackgroundWhiteGrayText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{GroupCreateUserCell.class}, new String[]{"statusTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GroupCreateUserCell.class}, null, Theme.avatarDrawables, null, Theme.key_avatar_text));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        int i9 = Theme.key_avatar_backgroundBlue;
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, i9));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_groupcreate_spanBackground));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_groupcreate_spanText));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_groupcreate_spanDelete));
        arrayList.add(new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.emptyView.title, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.emptyView.subtitle, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i8));
        PermanentLinkBottomSheet permanentLinkBottomSheet = this.sharedLinkBottomSheet;
        if (permanentLinkBottomSheet != null) {
            arrayList.addAll(permanentLinkBottomSheet.getThemeDescriptions());
        }
        return arrayList;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        GroupCreateSpan groupCreateSpan = (GroupCreateSpan) view;
        if (groupCreateSpan.isDeleting()) {
            this.currentDeletingSpan = null;
            this.spansContainer.removeSpan(groupCreateSpan);
            updateHint();
            checkVisibleRows();
            return;
        }
        GroupCreateSpan groupCreateSpan2 = this.currentDeletingSpan;
        if (groupCreateSpan2 != null) {
            groupCreateSpan2.cancelDeleteAnimation();
        }
        this.currentDeletingSpan = groupCreateSpan;
        groupCreateSpan.startDeleteAnimation();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        getNotificationCenter().addObserver(this, NotificationCenter.contactsDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.updateInterfaces);
        getNotificationCenter().addObserver(this, NotificationCenter.chatDidCreated);
        getUserConfig().loadGlobalTTl();
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.contactsDidLoad);
        getNotificationCenter().removeObserver(this, NotificationCenter.updateInterfaces);
        getNotificationCenter().removeObserver(this, NotificationCenter.chatDidCreated);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
    }

    public void select(ArrayList arrayList, boolean z, boolean z2) {
        GroupCreateSpan groupCreateSpan;
        GroupCreateSpan groupCreateSpan2;
        SpansContainer spansContainer = this.spansContainer;
        if (spansContainer == null) {
            this.toSelectIds = arrayList;
            this.toSelectPremium = z;
            this.toSelectMiniapps = z2;
            return;
        }
        if (z && this.selectedPremium == null) {
            GroupCreateSpan groupCreateSpan3 = new GroupCreateSpan(getContext(), "premium");
            this.selectedPremium = groupCreateSpan3;
            this.spansContainer.addSpan(groupCreateSpan3);
            this.selectedPremium.setOnClickListener(this);
        } else if (!z && (groupCreateSpan = this.selectedPremium) != null) {
            spansContainer.removeSpan(groupCreateSpan);
            this.selectedPremium = null;
        }
        if (z2 && this.selectedMiniapps == null) {
            GroupCreateSpan groupCreateSpan4 = new GroupCreateSpan(getContext(), "miniapps");
            this.selectedMiniapps = groupCreateSpan4;
            this.spansContainer.addSpan(groupCreateSpan4);
            this.selectedMiniapps.setOnClickListener(this);
        } else if (!z2 && (groupCreateSpan2 = this.selectedMiniapps) != null) {
            this.spansContainer.removeSpan(groupCreateSpan2);
            this.selectedMiniapps = null;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Long l = (Long) it.next();
            long longValue = l.longValue();
            Object chat = longValue < 0 ? getMessagesController().getChat(Long.valueOf(-longValue)) : getMessagesController().getUser(l);
            if (chat != null) {
                GroupCreateSpan groupCreateSpan5 = new GroupCreateSpan(getContext(), chat);
                this.spansContainer.addSpan(groupCreateSpan5);
                groupCreateSpan5.setOnClickListener(this);
            }
        }
        this.spansContainer.endAnimation();
        AndroidUtilities.updateVisibleRows(this.listView);
    }

    public void setDelegate(GroupCreateActivityDelegate groupCreateActivityDelegate) {
        this.delegate = groupCreateActivityDelegate;
    }

    public void setDelegate2(ContactsAddActivityDelegate contactsAddActivityDelegate) {
        this.delegate2 = contactsAddActivityDelegate;
    }

    public void setIgnoreUsers(LongSparseArray longSparseArray) {
        this.ignoreUsers = longSparseArray;
    }

    public void setInfo(TLRPC.ChatFull chatFull) {
        this.info = chatFull;
    }
}
