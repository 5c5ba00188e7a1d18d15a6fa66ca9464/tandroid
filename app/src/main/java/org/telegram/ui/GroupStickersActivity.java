package org.telegram.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.core.util.ObjectsCompat$$ExternalSyntheticBackport0;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_channels_setStickers;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC$TL_messages_foundStickerSets;
import org.telegram.tgnet.TLRPC$TL_messages_searchStickerSets;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.StickerSetCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
/* loaded from: classes3.dex */
public class GroupStickersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private long chatId;
    private FrameLayout emptyFrameView;
    private StickerEmptyView emptyView;
    private int headerRow;
    private TLRPC$ChatFull info;
    private int infoRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private FlickerLoadingView loadingView;
    private boolean removeStickerSet;
    private int rowCount;
    private SearchAdapter searchAdapter;
    private ActionBarMenuItem searchItem;
    private boolean searching;
    private TLRPC$TL_messages_stickerSet selectedStickerSet;
    private int selectedStickerSetIndex = -1;
    private int stickersEndRow;
    private int stickersStartRow;

    public GroupStickersActivity(long j) {
        this.chatId = j;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        MediaDataController.getInstance(this.currentAccount).checkStickers(0);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupStickersDidLoad);
        updateRows();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupStickersDidLoad);
        if (this.selectedStickerSet != null || this.removeStickerSet) {
            saveStickerSet();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("GroupStickers", 2131626151));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        ActionBarMenuItem addItem = this.actionBar.createMenu().addItem(0, 2131165456);
        this.searchItem = addItem;
        addItem.setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass2());
        this.searchItem.setSearchFieldHint(LocaleController.getString(2131628154));
        this.listAdapter = new ListAdapter(context);
        this.searchAdapter = new SearchAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        this.listView = new RecyclerListView(context);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setSupportsChangeAnimations(true);
        this.listView.setItemAnimator(defaultItemAnimator);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        this.layoutManager = linearLayoutManager;
        linearLayoutManager.setOrientation(1);
        this.listView.setLayoutManager(this.layoutManager);
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.emptyFrameView = frameLayout3;
        frameLayout3.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context, getResourceProvider());
        this.loadingView = flickerLoadingView;
        flickerLoadingView.setViewType(19);
        this.loadingView.setIsSingleCell(true);
        this.loadingView.setItemsCount((int) Math.ceil(AndroidUtilities.displaySize.y / AndroidUtilities.dpf2(58.0f)));
        this.emptyFrameView.addView(this.loadingView, LayoutHelper.createFrame(-1, -1.0f));
        StickerEmptyView stickerEmptyView = new StickerEmptyView(context, this.loadingView, 1);
        this.emptyView = stickerEmptyView;
        VerticalPositionAutoAnimator.attach(stickerEmptyView);
        this.emptyFrameView.addView(this.emptyView);
        frameLayout2.addView(this.emptyFrameView);
        this.emptyFrameView.setVisibility(8);
        this.listView.setEmptyView(this.emptyFrameView);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new GroupStickersActivity$$ExternalSyntheticLambda2(this));
        this.listView.setOnScrollListener(new AnonymousClass3());
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.GroupStickersActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            GroupStickersActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                GroupStickersActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.GroupStickersActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
        }

        AnonymousClass2() {
            GroupStickersActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            if (GroupStickersActivity.this.searching) {
                GroupStickersActivity.this.searchAdapter.onSearchStickers(null);
                GroupStickersActivity.this.searching = false;
                GroupStickersActivity.this.listView.setAdapter(GroupStickersActivity.this.listAdapter);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            String obj = editText.getText().toString();
            GroupStickersActivity.this.searchAdapter.onSearchStickers(obj);
            boolean z = !TextUtils.isEmpty(obj);
            if (z != GroupStickersActivity.this.searching) {
                GroupStickersActivity.this.searching = z;
                if (GroupStickersActivity.this.listView == null) {
                    return;
                }
                GroupStickersActivity.this.listView.setAdapter(GroupStickersActivity.this.searching ? GroupStickersActivity.this.searchAdapter : GroupStickersActivity.this.listAdapter);
            }
        }
    }

    public /* synthetic */ void lambda$createView$0(View view, int i) {
        if (getParentActivity() == null) {
            return;
        }
        if (!this.searching) {
            if (i < this.stickersStartRow || i >= this.stickersEndRow) {
                return;
            }
            onStickerSetClicked(view, MediaDataController.getInstance(this.currentAccount).getStickerSets(0).get(i - this.stickersStartRow), false);
        } else if (i > this.searchAdapter.searchEntries.size()) {
            onStickerSetClicked(view, (TLRPC$TL_messages_stickerSet) this.searchAdapter.localSearchEntries.get((i - this.searchAdapter.searchEntries.size()) - 1), false);
        } else if (i == this.searchAdapter.searchEntries.size()) {
        } else {
            onStickerSetClicked(view, (TLRPC$TL_messages_stickerSet) this.searchAdapter.searchEntries.get(i), true);
        }
    }

    /* renamed from: org.telegram.ui.GroupStickersActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends RecyclerView.OnScrollListener {
        AnonymousClass3() {
            GroupStickersActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                AndroidUtilities.hideKeyboard(GroupStickersActivity.this.getParentActivity().getCurrentFocus());
            }
        }
    }

    private void onStickerSetClicked(View view, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, boolean z) {
        TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName;
        if (z) {
            TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName2 = new TLRPC$TL_inputStickerSetShortName();
            tLRPC$TL_inputStickerSetShortName2.short_name = tLRPC$TL_messages_stickerSet.set.short_name;
            tLRPC$TL_inputStickerSetShortName = tLRPC$TL_inputStickerSetShortName2;
        } else {
            tLRPC$TL_inputStickerSetShortName = null;
        }
        StickersAlert stickersAlert = new StickersAlert(getParentActivity(), this, tLRPC$TL_inputStickerSetShortName, !z ? tLRPC$TL_messages_stickerSet : null, (StickersAlert.StickersAlertDelegate) null);
        stickersAlert.setCustomButtonDelegate(new AnonymousClass4(((StickerSetCell) view).isChecked(), tLRPC$TL_messages_stickerSet));
        stickersAlert.show();
    }

    /* renamed from: org.telegram.ui.GroupStickersActivity$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 implements StickersAlert.StickersAlertCustomButtonDelegate {
        final /* synthetic */ boolean val$isSelected;
        final /* synthetic */ TLRPC$TL_messages_stickerSet val$stickerSet;

        AnonymousClass4(boolean z, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
            GroupStickersActivity.this = r1;
            this.val$isSelected = z;
            this.val$stickerSet = tLRPC$TL_messages_stickerSet;
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertCustomButtonDelegate
        public String getCustomButtonTextColorKey() {
            return this.val$isSelected ? "dialogTextRed" : "featuredStickers_buttonText";
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertCustomButtonDelegate
        public String getCustomButtonRippleColorKey() {
            if (!this.val$isSelected) {
                return "featuredStickers_addButtonPressed";
            }
            return null;
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertCustomButtonDelegate
        public String getCustomButtonColorKey() {
            if (!this.val$isSelected) {
                return "featuredStickers_addButton";
            }
            return null;
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertCustomButtonDelegate
        public String getCustomButtonText() {
            return LocaleController.getString(this.val$isSelected ? 2131627965 : 2131628301);
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertCustomButtonDelegate
        public boolean onCustomButtonPressed() {
            boolean z;
            boolean z2;
            int findFirstVisibleItemPosition = GroupStickersActivity.this.layoutManager.findFirstVisibleItemPosition();
            RecyclerListView.Holder holder = (RecyclerListView.Holder) GroupStickersActivity.this.listView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
            int top = holder != null ? holder.itemView.getTop() : Integer.MAX_VALUE;
            int i = GroupStickersActivity.this.selectedStickerSetIndex;
            if (this.val$isSelected) {
                GroupStickersActivity.this.selectedStickerSet = null;
                GroupStickersActivity.this.removeStickerSet = true;
            } else {
                GroupStickersActivity.this.selectedStickerSet = this.val$stickerSet;
                GroupStickersActivity.this.removeStickerSet = false;
            }
            GroupStickersActivity.this.updateSelectedStickerSetIndex();
            if (i != -1) {
                if (!GroupStickersActivity.this.searching) {
                    for (int i2 = 0; i2 < GroupStickersActivity.this.listView.getChildCount(); i2++) {
                        View childAt = GroupStickersActivity.this.listView.getChildAt(i2);
                        if (GroupStickersActivity.this.listView.getChildViewHolder(childAt).getAdapterPosition() == GroupStickersActivity.this.stickersStartRow + i) {
                            ((StickerSetCell) childAt).setChecked(false, true);
                            z2 = true;
                            break;
                        }
                    }
                }
                z2 = false;
                if (!z2) {
                    GroupStickersActivity.this.listAdapter.notifyItemChanged(i);
                }
            }
            if (GroupStickersActivity.this.selectedStickerSetIndex != -1) {
                if (!GroupStickersActivity.this.searching) {
                    for (int i3 = 0; i3 < GroupStickersActivity.this.listView.getChildCount(); i3++) {
                        View childAt2 = GroupStickersActivity.this.listView.getChildAt(i3);
                        if (GroupStickersActivity.this.listView.getChildViewHolder(childAt2).getAdapterPosition() == GroupStickersActivity.this.stickersStartRow + GroupStickersActivity.this.selectedStickerSetIndex) {
                            ((StickerSetCell) childAt2).setChecked(true, true);
                            z = true;
                            break;
                        }
                    }
                }
                z = false;
                if (!z) {
                    GroupStickersActivity.this.listAdapter.notifyItemChanged(GroupStickersActivity.this.selectedStickerSetIndex);
                }
            }
            if (top != Integer.MAX_VALUE) {
                GroupStickersActivity.this.layoutManager.scrollToPositionWithOffset(findFirstVisibleItemPosition + 1, top);
            }
            if (GroupStickersActivity.this.searching) {
                GroupStickersActivity.this.searchItem.setSearchFieldText("", false);
                ((BaseFragment) GroupStickersActivity.this).actionBar.closeSearchField(true);
            }
            return true;
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC$StickerSet tLRPC$StickerSet;
        if (i == NotificationCenter.stickersDidLoad) {
            if (((Integer) objArr[0]).intValue() != 0) {
                return;
            }
            updateRows();
        } else if (i == NotificationCenter.chatInfoDidLoad) {
            TLRPC$ChatFull tLRPC$ChatFull = (TLRPC$ChatFull) objArr[0];
            if (tLRPC$ChatFull.id != this.chatId) {
                return;
            }
            if (this.info == null && tLRPC$ChatFull.stickerset != null) {
                this.selectedStickerSet = MediaDataController.getInstance(this.currentAccount).getGroupStickerSetById(tLRPC$ChatFull.stickerset);
            }
            this.info = tLRPC$ChatFull;
            updateRows();
        } else if (i != NotificationCenter.groupStickersDidLoad) {
        } else {
            long longValue = ((Long) objArr[0]).longValue();
            TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
            if (tLRPC$ChatFull2 == null || (tLRPC$StickerSet = tLRPC$ChatFull2.stickerset) == null || tLRPC$StickerSet.id != longValue) {
                return;
            }
            updateRows();
        }
    }

    public void setInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.info = tLRPC$ChatFull;
        if (tLRPC$ChatFull == null || tLRPC$ChatFull.stickerset == null) {
            return;
        }
        this.selectedStickerSet = MediaDataController.getInstance(this.currentAccount).getGroupStickerSetById(this.info.stickerset);
    }

    private void saveStickerSet() {
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet;
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        if (tLRPC$ChatFull != null) {
            TLRPC$StickerSet tLRPC$StickerSet = tLRPC$ChatFull.stickerset;
            if (tLRPC$StickerSet != null && (tLRPC$TL_messages_stickerSet = this.selectedStickerSet) != null && tLRPC$TL_messages_stickerSet.set.id == tLRPC$StickerSet.id) {
                return;
            }
            if (tLRPC$StickerSet == null && this.selectedStickerSet == null) {
                return;
            }
            TLRPC$TL_channels_setStickers tLRPC$TL_channels_setStickers = new TLRPC$TL_channels_setStickers();
            tLRPC$TL_channels_setStickers.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(this.chatId);
            if (this.removeStickerSet) {
                tLRPC$TL_channels_setStickers.stickerset = new TLRPC$TL_inputStickerSetEmpty();
            } else {
                SharedPreferences.Editor edit = MessagesController.getEmojiSettings(this.currentAccount).edit();
                edit.remove("group_hide_stickers_" + this.info.id).apply();
                TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
                tLRPC$TL_channels_setStickers.stickerset = tLRPC$TL_inputStickerSetID;
                TLRPC$StickerSet tLRPC$StickerSet2 = this.selectedStickerSet.set;
                tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSet2.id;
                tLRPC$TL_inputStickerSetID.access_hash = tLRPC$StickerSet2.access_hash;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_channels_setStickers, new GroupStickersActivity$$ExternalSyntheticLambda1(this));
        }
    }

    public /* synthetic */ void lambda$saveStickerSet$2(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new GroupStickersActivity$$ExternalSyntheticLambda0(this, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$saveStickerSet$1(TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.selectedStickerSet;
            if (tLRPC$TL_messages_stickerSet == null) {
                this.info.stickerset = null;
            } else {
                this.info.stickerset = tLRPC$TL_messages_stickerSet.set;
                MediaDataController.getInstance(this.currentAccount).putGroupStickerSet(this.selectedStickerSet);
            }
            updateSelectedStickerSetIndex();
            TLRPC$ChatFull tLRPC$ChatFull = this.info;
            if (tLRPC$ChatFull.stickerset == null) {
                tLRPC$ChatFull.flags |= 256;
            } else {
                tLRPC$ChatFull.flags &= -257;
            }
            MessagesStorage.getInstance(this.currentAccount).updateChatInfo(this.info, false);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, this.info, 0, Boolean.TRUE, Boolean.FALSE);
            finishFragment();
        } else if (getParentActivity() == null) {
        } else {
            Toast.makeText(getParentActivity(), LocaleController.getString("ErrorOccurred", 2131625695) + "\n" + tLRPC$TL_error.text, 0).show();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x002d A[LOOP:0: B:15:0x002d->B:20:0x0044, LOOP_START, PHI: r1 
      PHI: (r1v1 int) = (r1v0 int), (r1v2 int) binds: [B:14:0x002b, B:20:0x0044] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0047 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateSelectedStickerSetIndex() {
        long j;
        TLRPC$StickerSet tLRPC$StickerSet;
        ArrayList<TLRPC$TL_messages_stickerSet> stickerSets = MediaDataController.getInstance(this.currentAccount).getStickerSets(0);
        this.selectedStickerSetIndex = -1;
        if (!this.removeStickerSet) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.selectedStickerSet;
            if (tLRPC$TL_messages_stickerSet != null) {
                j = tLRPC$TL_messages_stickerSet.set.id;
            } else {
                TLRPC$ChatFull tLRPC$ChatFull = this.info;
                if (tLRPC$ChatFull != null && (tLRPC$StickerSet = tLRPC$ChatFull.stickerset) != null) {
                    j = tLRPC$StickerSet.id;
                }
            }
            if (j != 0) {
                return;
            }
            for (int i = 0; i < stickerSets.size(); i++) {
                if (stickerSets.get(i).set.id == j) {
                    this.selectedStickerSetIndex = i;
                    return;
                }
            }
            return;
        }
        j = 0;
        if (j != 0) {
        }
    }

    @SuppressLint({"NotifyDataSetChanged"})
    private void updateRows() {
        this.rowCount = 0;
        ArrayList<TLRPC$TL_messages_stickerSet> stickerSets = MediaDataController.getInstance(this.currentAccount).getStickerSets(0);
        if (!stickerSets.isEmpty()) {
            int i = this.rowCount;
            int i2 = i + 1;
            this.rowCount = i2;
            this.headerRow = i;
            this.stickersStartRow = i2;
            this.stickersEndRow = i2 + stickerSets.size();
            this.rowCount += stickerSets.size();
        } else {
            this.headerRow = -1;
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
        }
        int i3 = this.rowCount;
        this.rowCount = i3 + 1;
        this.infoRow = i3;
        updateSelectedStickerSetIndex();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes3.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private Runnable lastCallback;
        private String lastQuery;
        private Context mContext;
        private int reqId;
        private List<TLRPC$TL_messages_stickerSet> searchEntries = new ArrayList();
        private List<TLRPC$TL_messages_stickerSet> localSearchEntries = new ArrayList();

        public SearchAdapter(Context context) {
            GroupStickersActivity.this = r1;
            this.mContext = context;
            setHasStableIds(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            if (getItemViewType(i) == 0) {
                List<TLRPC$TL_messages_stickerSet> list = i > this.searchEntries.size() ? this.localSearchEntries : this.searchEntries;
                if (i > this.searchEntries.size()) {
                    i = (i - this.searchEntries.size()) - 1;
                }
                return list.get(i).set.id;
            }
            return -1L;
        }

        @SuppressLint({"NotifyDataSetChanged"})
        public void onSearchStickers(String str) {
            if (this.reqId != 0) {
                GroupStickersActivity.this.getConnectionsManager().cancelRequest(this.reqId, true);
                this.reqId = 0;
            }
            Runnable runnable = this.lastCallback;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.lastCallback = null;
            }
            this.lastQuery = null;
            int itemCount = getItemCount();
            if (itemCount > 0) {
                this.searchEntries.clear();
                this.localSearchEntries.clear();
                notifyItemRangeRemoved(0, itemCount);
            }
            if (TextUtils.isEmpty(str)) {
                GroupStickersActivity.this.emptyView.setVisibility(8);
                GroupStickersActivity.this.emptyView.showProgress(false, true);
                return;
            }
            if (GroupStickersActivity.this.emptyView.getVisibility() != 0) {
                GroupStickersActivity.this.emptyView.setVisibility(0);
                GroupStickersActivity.this.emptyView.showProgress(true, false);
            } else {
                GroupStickersActivity.this.emptyView.showProgress(true, true);
            }
            GroupStickersActivity$SearchAdapter$$ExternalSyntheticLambda0 groupStickersActivity$SearchAdapter$$ExternalSyntheticLambda0 = new GroupStickersActivity$SearchAdapter$$ExternalSyntheticLambda0(this, str);
            this.lastCallback = groupStickersActivity$SearchAdapter$$ExternalSyntheticLambda0;
            AndroidUtilities.runOnUIThread(groupStickersActivity$SearchAdapter$$ExternalSyntheticLambda0, 300L);
        }

        public /* synthetic */ void lambda$onSearchStickers$2(String str) {
            this.lastQuery = str;
            TLRPC$TL_messages_searchStickerSets tLRPC$TL_messages_searchStickerSets = new TLRPC$TL_messages_searchStickerSets();
            tLRPC$TL_messages_searchStickerSets.q = str;
            this.reqId = GroupStickersActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_messages_searchStickerSets, new GroupStickersActivity$SearchAdapter$$ExternalSyntheticLambda2(this, tLRPC$TL_messages_searchStickerSets, str), 66);
        }

        public /* synthetic */ void lambda$onSearchStickers$1(TLRPC$TL_messages_searchStickerSets tLRPC$TL_messages_searchStickerSets, String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            if (ObjectsCompat$$ExternalSyntheticBackport0.m(this.lastQuery, tLRPC$TL_messages_searchStickerSets.q) && (tLObject instanceof TLRPC$TL_messages_foundStickerSets)) {
                ArrayList arrayList = new ArrayList();
                Iterator<TLRPC$StickerSetCovered> it = ((TLRPC$TL_messages_foundStickerSets) tLObject).sets.iterator();
                while (it.hasNext()) {
                    TLRPC$StickerSetCovered next = it.next();
                    TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = new TLRPC$TL_messages_stickerSet();
                    tLRPC$TL_messages_stickerSet.set = next.set;
                    tLRPC$TL_messages_stickerSet.documents = next.covers;
                    arrayList.add(tLRPC$TL_messages_stickerSet);
                }
                String trim = str.toLowerCase(Locale.ROOT).trim();
                ArrayList arrayList2 = new ArrayList();
                Iterator<TLRPC$TL_messages_stickerSet> it2 = MediaDataController.getInstance(((BaseFragment) GroupStickersActivity.this).currentAccount).getStickerSets(0).iterator();
                while (it2.hasNext()) {
                    TLRPC$TL_messages_stickerSet next2 = it2.next();
                    String str2 = next2.set.short_name;
                    Locale locale = Locale.ROOT;
                    if (str2.toLowerCase(locale).contains(trim) || next2.set.title.toLowerCase(locale).contains(trim)) {
                        arrayList2.add(next2);
                    }
                }
                AndroidUtilities.runOnUIThread(new GroupStickersActivity$SearchAdapter$$ExternalSyntheticLambda1(this, arrayList, arrayList2, str));
            }
        }

        public /* synthetic */ void lambda$onSearchStickers$0(List list, List list2, String str) {
            this.searchEntries = list;
            this.localSearchEntries = list2;
            notifyDataSetChanged();
            GroupStickersActivity.this.emptyView.title.setVisibility(8);
            GroupStickersActivity.this.emptyView.subtitle.setText(LocaleController.formatString(2131625129, str));
            GroupStickersActivity.this.emptyView.showProgress(false, true);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            StickerSetCell stickerSetCell;
            if (i == 0) {
                StickerSetCell stickerSetCell2 = new StickerSetCell(this.mContext, 3);
                stickerSetCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                stickerSetCell = stickerSetCell2;
            } else {
                HeaderCell headerCell = new HeaderCell(this.mContext, "windowBackgroundWhiteGrayText4", 21, 0, 0, false, GroupStickersActivity.this.getResourceProvider());
                headerCell.setBackground(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                headerCell.setText(LocaleController.getString(2131625128));
                stickerSetCell = headerCell;
            }
            stickerSetCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(stickerSetCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (getItemViewType(i) != 0) {
                return;
            }
            boolean z = true;
            boolean z2 = i > this.searchEntries.size();
            List<TLRPC$TL_messages_stickerSet> list = z2 ? this.localSearchEntries : this.searchEntries;
            if (z2) {
                i = (i - this.searchEntries.size()) - 1;
            }
            StickerSetCell stickerSetCell = (StickerSetCell) viewHolder.itemView;
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = list.get(i);
            stickerSetCell.setStickersSet(tLRPC$TL_messages_stickerSet, i != list.size() - 1, !z2);
            String str = this.lastQuery;
            stickerSetCell.setSearchQuery(tLRPC$TL_messages_stickerSet, str != null ? str.toLowerCase(Locale.ROOT) : "", GroupStickersActivity.this.getResourceProvider());
            if (tLRPC$TL_messages_stickerSet.set.id != (GroupStickersActivity.this.selectedStickerSet != null ? GroupStickersActivity.this.selectedStickerSet.set.id : (GroupStickersActivity.this.info == null || GroupStickersActivity.this.info.stickerset == null) ? 0L : GroupStickersActivity.this.info.stickerset.id)) {
                z = false;
            }
            stickerSetCell.setChecked(z, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return this.searchEntries.size() == i ? 1 : 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.searchEntries.size() + this.localSearchEntries.size() + (!this.localSearchEntries.isEmpty() ? 1 : 0);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return getItemViewType(viewHolder.getAdapterPosition()) == 0;
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            GroupStickersActivity.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return GroupStickersActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            boolean z = true;
            if (itemViewType == 0) {
                ArrayList<TLRPC$TL_messages_stickerSet> stickerSets = MediaDataController.getInstance(((BaseFragment) GroupStickersActivity.this).currentAccount).getStickerSets(0);
                int i2 = i - GroupStickersActivity.this.stickersStartRow;
                StickerSetCell stickerSetCell = (StickerSetCell) viewHolder.itemView;
                TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSets.get(i2);
                stickerSetCell.setStickersSet(stickerSets.get(i2), i2 != stickerSets.size() - 1);
                if (tLRPC$TL_messages_stickerSet.set.id != (GroupStickersActivity.this.selectedStickerSet != null ? GroupStickersActivity.this.selectedStickerSet.set.id : (GroupStickersActivity.this.info == null || GroupStickersActivity.this.info.stickerset == null) ? 0L : GroupStickersActivity.this.info.stickerset.id)) {
                    z = false;
                }
                stickerSetCell.setChecked(z, false);
            } else if (itemViewType != 1) {
                if (itemViewType != 4) {
                    return;
                }
                ((HeaderCell) viewHolder.itemView).setText(LocaleController.getString(2131625131));
            } else if (i != GroupStickersActivity.this.infoRow) {
            } else {
                String string = LocaleController.getString("ChooseStickerSetMy", 2131625132);
                int indexOf = string.indexOf("@stickers");
                if (indexOf != -1) {
                    try {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
                        spannableStringBuilder.setSpan(new AnonymousClass1("@stickers"), indexOf, indexOf + 9, 18);
                        ((TextInfoPrivacyCell) viewHolder.itemView).setText(spannableStringBuilder);
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        ((TextInfoPrivacyCell) viewHolder.itemView).setText(string);
                        return;
                    }
                }
                ((TextInfoPrivacyCell) viewHolder.itemView).setText(string);
            }
        }

        /* renamed from: org.telegram.ui.GroupStickersActivity$ListAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends URLSpanNoUnderline {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(String str) {
                super(str);
                ListAdapter.this = r1;
            }

            @Override // org.telegram.ui.Components.URLSpanNoUnderline, android.text.style.URLSpan, android.text.style.ClickableSpan
            public void onClick(View view) {
                MessagesController.getInstance(((BaseFragment) GroupStickersActivity.this).currentAccount).openByUserName("stickers", GroupStickersActivity.this, 1);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                view = new StickerSetCell(this.mContext, 3);
                view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else if (i == 1) {
                view = new TextInfoPrivacyCell(this.mContext);
                view.setBackground(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
            } else {
                view = new HeaderCell(this.mContext);
                view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i < GroupStickersActivity.this.stickersStartRow || i >= GroupStickersActivity.this.stickersEndRow) {
                if (i == GroupStickersActivity.this.headerRow) {
                    return 4;
                }
                return i == GroupStickersActivity.this.infoRow ? 1 : 0;
            }
            return 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{StickerSetCell.class, TextSettingsCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteLinkText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "stickers_menuSelector"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "stickers_menu"));
        return arrayList;
    }
}
