package org.telegram.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.FeaturedStickerSetCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickersAlert;
/* loaded from: classes3.dex */
public class FeaturedStickersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int rowCount;
    private int stickersEndRow;
    private int stickersShadowRow;
    private int stickersStartRow;
    private ArrayList<Long> unreadStickers = null;
    private LongSparseArray<TLRPC$StickerSetCovered> installingStickerSets = new LongSparseArray<>();

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        MediaDataController.getInstance(this.currentAccount).checkFeaturedStickers();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        ArrayList<Long> unreadStickerSets = MediaDataController.getInstance(this.currentAccount).getUnreadStickerSets();
        if (unreadStickerSets != null) {
            this.unreadStickers = new ArrayList<>(unreadStickerSets);
        }
        updateRows();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("FeaturedStickers", 2131625801));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setFocusable(true);
        this.listView.setTag(14);
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, context);
        this.layoutManager = anonymousClass2;
        anonymousClass2.setOrientation(1);
        this.listView.setLayoutManager(this.layoutManager);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new FeaturedStickersActivity$$ExternalSyntheticLambda0(this));
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.FeaturedStickersActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            FeaturedStickersActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                FeaturedStickersActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.FeaturedStickersActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends LinearLayoutManager {
        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        AnonymousClass2(FeaturedStickersActivity featuredStickersActivity, Context context) {
            super(context);
        }
    }

    public /* synthetic */ void lambda$createView$0(View view, int i) {
        TLRPC$InputStickerSet tLRPC$InputStickerSet;
        if (i < this.stickersStartRow || i >= this.stickersEndRow || getParentActivity() == null) {
            return;
        }
        TLRPC$StickerSetCovered tLRPC$StickerSetCovered = MediaDataController.getInstance(this.currentAccount).getFeaturedStickerSets().get(i);
        if (tLRPC$StickerSetCovered.set.id != 0) {
            tLRPC$InputStickerSet = new TLRPC$TL_inputStickerSetID();
            tLRPC$InputStickerSet.id = tLRPC$StickerSetCovered.set.id;
        } else {
            tLRPC$InputStickerSet = new TLRPC$TL_inputStickerSetShortName();
            tLRPC$InputStickerSet.short_name = tLRPC$StickerSetCovered.set.short_name;
        }
        TLRPC$InputStickerSet tLRPC$InputStickerSet2 = tLRPC$InputStickerSet;
        tLRPC$InputStickerSet2.access_hash = tLRPC$StickerSetCovered.set.access_hash;
        StickersAlert stickersAlert = new StickersAlert(getParentActivity(), this, tLRPC$InputStickerSet2, (TLRPC$TL_messages_stickerSet) null, (StickersAlert.StickersAlertDelegate) null);
        stickersAlert.setInstallDelegate(new AnonymousClass3(view, tLRPC$StickerSetCovered));
        showDialog(stickersAlert);
    }

    /* renamed from: org.telegram.ui.FeaturedStickersActivity$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements StickersAlert.StickersAlertInstallDelegate {
        final /* synthetic */ TLRPC$StickerSetCovered val$stickerSet;
        final /* synthetic */ View val$view;

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertInstallDelegate
        public void onStickerSetUninstalled() {
        }

        AnonymousClass3(View view, TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
            FeaturedStickersActivity.this = r1;
            this.val$view = view;
            this.val$stickerSet = tLRPC$StickerSetCovered;
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertInstallDelegate
        public void onStickerSetInstalled() {
            ((FeaturedStickerSetCell) this.val$view).setDrawProgress(true, true);
            LongSparseArray longSparseArray = FeaturedStickersActivity.this.installingStickerSets;
            TLRPC$StickerSetCovered tLRPC$StickerSetCovered = this.val$stickerSet;
            longSparseArray.put(tLRPC$StickerSetCovered.set.id, tLRPC$StickerSetCovered);
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.featuredStickersDidLoad) {
            if (this.unreadStickers == null) {
                this.unreadStickers = MediaDataController.getInstance(this.currentAccount).getUnreadStickerSets();
            }
            updateRows();
        } else if (i != NotificationCenter.stickersDidLoad) {
        } else {
            updateVisibleTrendingSets();
        }
    }

    private void updateVisibleTrendingSets() {
        int findFirstVisibleItemPosition;
        int findLastVisibleItemPosition;
        LinearLayoutManager linearLayoutManager = this.layoutManager;
        if (linearLayoutManager == null || (findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()) == -1 || (findLastVisibleItemPosition = this.layoutManager.findLastVisibleItemPosition()) == -1) {
            return;
        }
        this.listAdapter.notifyItemRangeChanged(findFirstVisibleItemPosition, (findLastVisibleItemPosition - findFirstVisibleItemPosition) + 1);
    }

    private void updateRows() {
        this.rowCount = 0;
        ArrayList<TLRPC$StickerSetCovered> featuredStickerSets = MediaDataController.getInstance(this.currentAccount).getFeaturedStickerSets();
        if (!featuredStickerSets.isEmpty()) {
            int i = this.rowCount;
            this.stickersStartRow = i;
            this.stickersEndRow = i + featuredStickerSets.size();
            int size = this.rowCount + featuredStickerSets.size();
            this.rowCount = size;
            this.rowCount = size + 1;
            this.stickersShadowRow = size;
        } else {
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
            this.stickersShadowRow = -1;
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        MediaDataController.getInstance(this.currentAccount).markFaturedStickersAsRead(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            FeaturedStickersActivity.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return FeaturedStickersActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (getItemViewType(i) == 0) {
                ArrayList<TLRPC$StickerSetCovered> featuredStickerSets = MediaDataController.getInstance(((BaseFragment) FeaturedStickersActivity.this).currentAccount).getFeaturedStickerSets();
                FeaturedStickerSetCell featuredStickerSetCell = (FeaturedStickerSetCell) viewHolder.itemView;
                featuredStickerSetCell.setTag(Integer.valueOf(i));
                TLRPC$StickerSetCovered tLRPC$StickerSetCovered = featuredStickerSets.get(i);
                boolean z = true;
                featuredStickerSetCell.setStickersSet(tLRPC$StickerSetCovered, i != featuredStickerSets.size() - 1, FeaturedStickersActivity.this.unreadStickers != null && FeaturedStickersActivity.this.unreadStickers.contains(Long.valueOf(tLRPC$StickerSetCovered.set.id)));
                if (FeaturedStickersActivity.this.installingStickerSets.indexOfKey(tLRPC$StickerSetCovered.set.id) < 0) {
                    z = false;
                }
                if (z && featuredStickerSetCell.isInstalled()) {
                    FeaturedStickersActivity.this.installingStickerSets.remove(tLRPC$StickerSetCovered.set.id);
                    z = false;
                }
                featuredStickerSetCell.setDrawProgress(z, false);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            FeaturedStickerSetCell featuredStickerSetCell = (FeaturedStickerSetCell) view.getParent();
            TLRPC$StickerSetCovered stickerSet = featuredStickerSetCell.getStickerSet();
            if (FeaturedStickersActivity.this.installingStickerSets.indexOfKey(stickerSet.set.id) >= 0) {
                return;
            }
            FeaturedStickersActivity.this.installingStickerSets.put(stickerSet.set.id, stickerSet);
            MediaDataController.getInstance(((BaseFragment) FeaturedStickersActivity.this).currentAccount).toggleStickerSet(FeaturedStickersActivity.this.getParentActivity(), stickerSet, 2, FeaturedStickersActivity.this, false, false);
            featuredStickerSetCell.setDrawProgress(true, true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextInfoPrivacyCell textInfoPrivacyCell;
            if (i == 0) {
                FeaturedStickerSetCell featuredStickerSetCell = new FeaturedStickerSetCell(this.mContext);
                featuredStickerSetCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                featuredStickerSetCell.setAddOnClickListener(new FeaturedStickersActivity$ListAdapter$$ExternalSyntheticLambda0(this));
                textInfoPrivacyCell = featuredStickerSetCell;
            } else {
                TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                textInfoPrivacyCell = textInfoPrivacyCell2;
            }
            textInfoPrivacyCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(textInfoPrivacyCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return ((i < FeaturedStickersActivity.this.stickersStartRow || i >= FeaturedStickersActivity.this.stickersEndRow) && i == FeaturedStickersActivity.this.stickersShadowRow) ? 1 : 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{FeaturedStickerSetCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_buttonProgress"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"addButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_buttonText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"checkImage"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addedIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{FeaturedStickerSetCell.class}, new String[]{"addButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addButton"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{FeaturedStickerSetCell.class}, new String[]{"addButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addButtonPressed"));
        return arrayList;
    }
}
