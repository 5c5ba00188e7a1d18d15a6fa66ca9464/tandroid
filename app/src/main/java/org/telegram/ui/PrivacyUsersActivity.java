package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.ContactsActivity;
/* loaded from: classes3.dex */
public class PrivacyUsersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, ContactsActivity.ContactsActivityDelegate {
    private int blockUserDetailRow;
    private int blockUserRow;
    private boolean blockedUsersActivity;
    private int currentType;
    private PrivacyActivityDelegate delegate;
    private EmptyTextProgressView emptyView;
    private boolean isAlwaysShare;
    private boolean isGroup;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private int rowCount;
    private ArrayList<Long> uidArray;
    private int usersDetailRow;
    private int usersEndRow;
    private int usersHeaderRow;
    private int usersStartRow;

    /* loaded from: classes3.dex */
    public interface PrivacyActivityDelegate {
        void didUpdateUserList(ArrayList<Long> arrayList, boolean z);
    }

    public PrivacyUsersActivity() {
        this.currentType = 1;
        this.blockedUsersActivity = true;
    }

    public PrivacyUsersActivity(int i, ArrayList<Long> arrayList, boolean z, boolean z2) {
        this.uidArray = arrayList;
        this.isAlwaysShare = z2;
        this.isGroup = z;
        this.blockedUsersActivity = false;
        this.currentType = i;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        if (this.currentType == 1) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.blockedUsersDidLoad);
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        if (this.currentType == 1) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.blockedUsersDidLoad);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        int i = this.currentType;
        int i2 = 2;
        if (i == 1) {
            this.actionBar.setTitle(LocaleController.getString("BlockedUsers", 2131624701));
        } else if (i == 2) {
            if (this.isAlwaysShare) {
                this.actionBar.setTitle(LocaleController.getString("FilterAlwaysShow", 2131625872));
            } else {
                this.actionBar.setTitle(LocaleController.getString("FilterNeverShow", 2131625910));
            }
        } else if (this.isGroup) {
            if (this.isAlwaysShare) {
                this.actionBar.setTitle(LocaleController.getString("AlwaysAllow", 2131624342));
            } else {
                this.actionBar.setTitle(LocaleController.getString("NeverAllow", 2131626820));
            }
        } else if (this.isAlwaysShare) {
            this.actionBar.setTitle(LocaleController.getString("AlwaysShareWithTitle", 2131624344));
        } else {
            this.actionBar.setTitle(LocaleController.getString("NeverShareWithTitle", 2131626822));
        }
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView;
        if (this.currentType == 1) {
            emptyTextProgressView.setText(LocaleController.getString("NoBlocked", 2131626859));
        } else {
            emptyTextProgressView.setText(LocaleController.getString("NoContacts", 2131626867));
        }
        frameLayout2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setEmptyView(this.emptyView);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        this.listView.setVerticalScrollBarEnabled(false);
        RecyclerListView recyclerListView3 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listViewAdapter = listAdapter;
        recyclerListView3.setAdapter(listAdapter);
        RecyclerListView recyclerListView4 = this.listView;
        if (LocaleController.isRTL) {
            i2 = 1;
        }
        recyclerListView4.setVerticalScrollbarPosition(i2);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new PrivacyUsersActivity$$ExternalSyntheticLambda2(this));
        this.listView.setOnItemLongClickListener(new PrivacyUsersActivity$$ExternalSyntheticLambda3(this));
        if (this.currentType == 1) {
            this.listView.setOnScrollListener(new AnonymousClass2());
            if (getMessagesController().totalBlockedCount < 0) {
                this.emptyView.showProgress();
            } else {
                this.emptyView.showTextView();
            }
        }
        updateRows();
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.PrivacyUsersActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            PrivacyUsersActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                PrivacyUsersActivity.this.finishFragment();
            }
        }
    }

    public /* synthetic */ void lambda$createView$1(View view, int i) {
        if (i == this.blockUserRow) {
            if (this.currentType == 1) {
                presentFragment(new DialogOrContactPickerActivity());
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(this.isAlwaysShare ? "isAlwaysShare" : "isNeverShare", true);
            if (this.isGroup) {
                bundle.putInt("chatAddType", 1);
            } else if (this.currentType == 2) {
                bundle.putInt("chatAddType", 2);
            }
            GroupCreateActivity groupCreateActivity = new GroupCreateActivity(bundle);
            groupCreateActivity.setDelegate(new PrivacyUsersActivity$$ExternalSyntheticLambda4(this));
            presentFragment(groupCreateActivity);
        } else if (i < this.usersStartRow || i >= this.usersEndRow) {
        } else {
            if (this.currentType == 1) {
                Bundle bundle2 = new Bundle();
                bundle2.putLong("user_id", getMessagesController().blockePeers.keyAt(i - this.usersStartRow));
                presentFragment(new ProfileActivity(bundle2));
                return;
            }
            Bundle bundle3 = new Bundle();
            long longValue = this.uidArray.get(i - this.usersStartRow).longValue();
            if (DialogObject.isUserDialog(longValue)) {
                bundle3.putLong("user_id", longValue);
            } else {
                bundle3.putLong("chat_id", -longValue);
            }
            presentFragment(new ProfileActivity(bundle3));
        }
    }

    public /* synthetic */ void lambda$createView$0(ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Long l = (Long) it.next();
            if (!this.uidArray.contains(l)) {
                this.uidArray.add(l);
            }
        }
        updateRows();
        PrivacyActivityDelegate privacyActivityDelegate = this.delegate;
        if (privacyActivityDelegate != null) {
            privacyActivityDelegate.didUpdateUserList(this.uidArray, true);
        }
    }

    public /* synthetic */ boolean lambda$createView$2(View view, int i) {
        int i2 = this.usersStartRow;
        if (i < i2 || i >= this.usersEndRow) {
            return false;
        }
        if (this.currentType == 1) {
            showUnblockAlert(Long.valueOf(getMessagesController().blockePeers.keyAt(i - this.usersStartRow)));
        } else {
            showUnblockAlert(this.uidArray.get(i - i2));
        }
        return true;
    }

    /* renamed from: org.telegram.ui.PrivacyUsersActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends RecyclerView.OnScrollListener {
        AnonymousClass2() {
            PrivacyUsersActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (PrivacyUsersActivity.this.getMessagesController().blockedEndReached) {
                return;
            }
            int abs = Math.abs(PrivacyUsersActivity.this.layoutManager.findLastVisibleItemPosition() - PrivacyUsersActivity.this.layoutManager.findFirstVisibleItemPosition()) + 1;
            int itemCount = recyclerView.getAdapter().getItemCount();
            if (abs <= 0 || PrivacyUsersActivity.this.layoutManager.findLastVisibleItemPosition() < itemCount - 10) {
                return;
            }
            PrivacyUsersActivity.this.getMessagesController().getBlockedPeers(false);
        }
    }

    public void setDelegate(PrivacyActivityDelegate privacyActivityDelegate) {
        this.delegate = privacyActivityDelegate;
    }

    public void showUnblockAlert(Long l) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setItems(this.currentType == 1 ? new CharSequence[]{LocaleController.getString("Unblock", 2131628793)} : new CharSequence[]{LocaleController.getString("Delete", 2131625384)}, new PrivacyUsersActivity$$ExternalSyntheticLambda0(this, l));
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$showUnblockAlert$3(Long l, DialogInterface dialogInterface, int i) {
        if (i == 0) {
            if (this.currentType == 1) {
                getMessagesController().unblockPeer(l.longValue());
                return;
            }
            this.uidArray.remove(l);
            updateRows();
            PrivacyActivityDelegate privacyActivityDelegate = this.delegate;
            if (privacyActivityDelegate != null) {
                privacyActivityDelegate.didUpdateUserList(this.uidArray, false);
            }
            if (!this.uidArray.isEmpty()) {
                return;
            }
            finishFragment();
        }
    }

    private void updateRows() {
        int i;
        this.rowCount = 0;
        if (!this.blockedUsersActivity || getMessagesController().totalBlockedCount >= 0) {
            int i2 = this.rowCount;
            int i3 = i2 + 1;
            this.rowCount = i3;
            this.blockUserRow = i2;
            this.rowCount = i3 + 1;
            this.blockUserDetailRow = i3;
            if (this.currentType == 1) {
                i = getMessagesController().blockePeers.size();
            } else {
                i = this.uidArray.size();
            }
            if (i != 0) {
                int i4 = this.rowCount;
                int i5 = i4 + 1;
                this.rowCount = i5;
                this.usersHeaderRow = i4;
                this.usersStartRow = i5;
                int i6 = i5 + i;
                this.rowCount = i6;
                this.usersEndRow = i6;
                this.rowCount = i6 + 1;
                this.usersDetailRow = i6;
            } else {
                this.usersHeaderRow = -1;
                this.usersStartRow = -1;
                this.usersEndRow = -1;
                this.usersDetailRow = -1;
            }
        }
        ListAdapter listAdapter = this.listViewAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.updateInterfaces) {
            int intValue = ((Integer) objArr[0]).intValue();
            if ((MessagesController.UPDATE_MASK_AVATAR & intValue) == 0 && (MessagesController.UPDATE_MASK_NAME & intValue) == 0) {
                return;
            }
            updateVisibleRows(intValue);
        } else if (i != NotificationCenter.blockedUsersDidLoad) {
        } else {
            this.emptyView.showTextView();
            updateRows();
        }
    }

    private void updateVisibleRows(int i) {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView == null) {
            return;
        }
        int childCount = recyclerListView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = this.listView.getChildAt(i2);
            if (childAt instanceof ManageChatUserCell) {
                ((ManageChatUserCell) childAt).update(i);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listViewAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.ContactsActivity.ContactsActivityDelegate
    public void didSelectContact(TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity) {
        if (tLRPC$User == null) {
            return;
        }
        getMessagesController().blockPeer(tLRPC$User.id);
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            PrivacyUsersActivity.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PrivacyUsersActivity.this.rowCount;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 2;
        }

        public /* synthetic */ boolean lambda$onCreateViewHolder$0(ManageChatUserCell manageChatUserCell, boolean z) {
            if (z) {
                PrivacyUsersActivity.this.showUnblockAlert((Long) manageChatUserCell.getTag());
                return true;
            }
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            HeaderCell headerCell;
            if (i == 0) {
                ManageChatUserCell manageChatUserCell = new ManageChatUserCell(this.mContext, 7, 6, true);
                manageChatUserCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                manageChatUserCell.setDelegate(new PrivacyUsersActivity$ListAdapter$$ExternalSyntheticLambda0(this));
                headerCell = manageChatUserCell;
            } else if (i == 1) {
                headerCell = new TextInfoPrivacyCell(this.mContext);
            } else if (i == 2) {
                ManageChatTextCell manageChatTextCell = new ManageChatTextCell(this.mContext);
                manageChatTextCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                headerCell = manageChatTextCell;
            } else {
                HeaderCell headerCell2 = new HeaderCell(this.mContext, "windowBackgroundWhiteBlueHeader", 21, 11, false);
                headerCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                headerCell2.setHeight(43);
                headerCell = headerCell2;
            }
            return new RecyclerListView.Holder(headerCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String str;
            String str2;
            int itemViewType = viewHolder.getItemViewType();
            boolean z = false;
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i == PrivacyUsersActivity.this.blockUserDetailRow) {
                        if (PrivacyUsersActivity.this.currentType == 1) {
                            textInfoPrivacyCell.setText(LocaleController.getString("BlockedUsersInfo", 2131624708));
                        } else {
                            textInfoPrivacyCell.setText(null);
                        }
                        if (PrivacyUsersActivity.this.usersStartRow == -1) {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                            return;
                        } else {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                            return;
                        }
                    } else if (i != PrivacyUsersActivity.this.usersDetailRow) {
                        return;
                    } else {
                        textInfoPrivacyCell.setText("");
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                        return;
                    }
                } else if (itemViewType == 2) {
                    ManageChatTextCell manageChatTextCell = (ManageChatTextCell) viewHolder.itemView;
                    manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                    if (PrivacyUsersActivity.this.currentType == 1) {
                        manageChatTextCell.setText(LocaleController.getString("BlockUser", 2131624690), null, 2131165690, false);
                        return;
                    } else {
                        manageChatTextCell.setText(LocaleController.getString("PrivacyAddAnException", 2131627750), null, 2131165690, false);
                        return;
                    }
                } else if (itemViewType != 3) {
                    return;
                } else {
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i != PrivacyUsersActivity.this.usersHeaderRow) {
                        return;
                    }
                    if (PrivacyUsersActivity.this.currentType == 1) {
                        headerCell.setText(LocaleController.formatPluralString("BlockedUsersCount", PrivacyUsersActivity.this.getMessagesController().totalBlockedCount, new Object[0]));
                        return;
                    } else {
                        headerCell.setText(LocaleController.getString("PrivacyExceptions", 2131627757));
                        return;
                    }
                }
            }
            ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
            long keyAt = PrivacyUsersActivity.this.currentType == 1 ? PrivacyUsersActivity.this.getMessagesController().blockePeers.keyAt(i - PrivacyUsersActivity.this.usersStartRow) : ((Long) PrivacyUsersActivity.this.uidArray.get(i - PrivacyUsersActivity.this.usersStartRow)).longValue();
            manageChatUserCell.setTag(Long.valueOf(keyAt));
            if (keyAt > 0) {
                TLRPC$User user = PrivacyUsersActivity.this.getMessagesController().getUser(Long.valueOf(keyAt));
                if (user == null) {
                    return;
                }
                if (user.bot) {
                    str2 = LocaleController.getString("Bot", 2131624715).substring(0, 1).toUpperCase() + LocaleController.getString("Bot", 2131624715).substring(1);
                } else {
                    String str3 = user.phone;
                    if (str3 != null && str3.length() != 0) {
                        str2 = PhoneFormat.getInstance().format("+" + user.phone);
                    } else {
                        str2 = LocaleController.getString("NumberUnknown", 2131627126);
                    }
                }
                if (i != PrivacyUsersActivity.this.usersEndRow - 1) {
                    z = true;
                }
                manageChatUserCell.setData(user, null, str2, z);
                return;
            }
            TLRPC$Chat chat = PrivacyUsersActivity.this.getMessagesController().getChat(Long.valueOf(-keyAt));
            if (chat == null) {
                return;
            }
            int i2 = chat.participants_count;
            if (i2 != 0) {
                str = LocaleController.formatPluralString("Members", i2, new Object[0]);
            } else if (chat.has_geo) {
                str = LocaleController.getString("MegaLocation", 2131626632);
            } else if (TextUtils.isEmpty(chat.username)) {
                str = LocaleController.getString("MegaPrivate", 2131626633);
            } else {
                str = LocaleController.getString("MegaPublic", 2131626636);
            }
            if (i != PrivacyUsersActivity.this.usersEndRow - 1) {
                z = true;
            }
            manageChatUserCell.setData(chat, null, str, z);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == PrivacyUsersActivity.this.usersHeaderRow) {
                return 3;
            }
            if (i == PrivacyUsersActivity.this.blockUserRow) {
                return 2;
            }
            return (i == PrivacyUsersActivity.this.blockUserDetailRow || i == PrivacyUsersActivity.this.usersDetailRow) ? 1 : 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        PrivacyUsersActivity$$ExternalSyntheticLambda1 privacyUsersActivity$$ExternalSyntheticLambda1 = new PrivacyUsersActivity$$ExternalSyntheticLambda1(this);
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ManageChatUserCell.class, ManageChatTextCell.class, HeaderCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, privacyUsersActivity$$ExternalSyntheticLambda1, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, privacyUsersActivity$$ExternalSyntheticLambda1, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, privacyUsersActivity$$ExternalSyntheticLambda1, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, privacyUsersActivity$$ExternalSyntheticLambda1, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, privacyUsersActivity$$ExternalSyntheticLambda1, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, privacyUsersActivity$$ExternalSyntheticLambda1, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, privacyUsersActivity$$ExternalSyntheticLambda1, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, privacyUsersActivity$$ExternalSyntheticLambda1, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, privacyUsersActivity$$ExternalSyntheticLambda1, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueButton"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueIcon"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$4() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof ManageChatUserCell) {
                    ((ManageChatUserCell) childAt).update(0);
                }
            }
        }
    }
}
