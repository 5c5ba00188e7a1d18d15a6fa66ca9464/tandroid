package org.telegram.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_messages_reorderStickerSets;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_stickerSetFullCovered;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.FeaturedStickerSetCell2;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.StickerSetCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ReorderingBulletinLayout;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.TrendingStickersAlert;
import org.telegram.ui.Components.TrendingStickersLayout;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.StickersActivity;
/* loaded from: classes3.dex */
public class StickersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int activeReorderingRequests;
    private int archivedInfoRow;
    private int archivedRow;
    private int currentType;
    private ActionBarMenuItem deleteMenuItem;
    private ArrayList<TLRPC$TL_messages_stickerSet> emojiPacks;
    private int emojiPacksRow;
    private int featuredStickersEndRow;
    private int featuredStickersHeaderRow;
    private int featuredStickersShadowRow;
    private int featuredStickersShowMoreRow;
    private int featuredStickersStartRow;
    private boolean isListeningForFeaturedUpdate;
    private DefaultItemAnimator itemAnimator;
    private ItemTouchHelper itemTouchHelper;
    private int largeEmojiRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int loopInfoRow;
    private int loopRow;
    private int masksInfoRow;
    private int masksRow;
    private boolean needReorder;
    private int reactionsDoubleTapRow;
    private int rowCount;
    private NumberTextView selectedCountTextView;
    private int stickersBotInfo;
    private int stickersEndRow;
    private int stickersHeaderRow;
    private int stickersShadowRow;
    private int stickersStartRow;
    private int suggestRow;
    private TrendingStickersAlert trendingStickersAlert;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$0(View view, MotionEvent motionEvent) {
        return true;
    }

    /* loaded from: classes3.dex */
    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }

        public TouchHelperCallback() {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return StickersActivity.this.listAdapter.hasSelected() && StickersActivity.this.currentType != 5;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 0) {
                return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
            }
            return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                return false;
            }
            StickersActivity.this.listAdapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i == 0) {
                StickersActivity.this.sendReorder();
            } else {
                StickersActivity.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }
    }

    public StickersActivity(int i) {
        this.currentType = i;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        MediaDataController.getInstance(this.currentAccount).checkStickers(this.currentType);
        int i = this.currentType;
        if (i == 0) {
            MediaDataController.getInstance(this.currentAccount).checkFeaturedStickers();
            MediaDataController.getInstance(this.currentAccount).checkStickers(1);
            MediaDataController.getInstance(this.currentAccount).checkStickers(5);
        } else if (i == 6) {
            MediaDataController.getInstance(this.currentAccount).checkFeaturedEmoji();
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredEmojiDidLoad);
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.archivedStickersCountDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        updateRows();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.currentType == 6) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredEmojiDidLoad);
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.archivedStickersCountDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    @SuppressLint({"ClickableViewAccessibility"})
    public View createView(final Context context) {
        boolean z;
        this.actionBar.setBackButtonDrawable(new BackDrawable(false));
        this.actionBar.setAllowOverlayTitle(true);
        int i = this.currentType;
        if (i == 0) {
            this.actionBar.setTitle(LocaleController.getString("StickersName", R.string.StickersName));
        } else if (i == 1) {
            this.actionBar.setTitle(LocaleController.getString("Masks", R.string.Masks));
        } else if (i == 5) {
            this.actionBar.setTitle(LocaleController.getString("Emoji", R.string.Emoji));
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.StickersActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i2) {
                if (i2 == -1) {
                    if (!StickersActivity.this.onBackPressed()) {
                        return;
                    }
                    StickersActivity.this.finishFragment();
                } else if (i2 != 0 && i2 != 1 && i2 != 2) {
                } else {
                    if (!StickersActivity.this.needReorder) {
                        if (StickersActivity.this.activeReorderingRequests != 0) {
                            return;
                        }
                        StickersActivity.this.listAdapter.processSelectionMenu(i2);
                        return;
                    }
                    StickersActivity.this.sendReorder();
                }
            }
        });
        ActionBarMenu createActionMode = this.actionBar.createActionMode();
        NumberTextView numberTextView = new NumberTextView(createActionMode.getContext());
        this.selectedCountTextView = numberTextView;
        numberTextView.setTextSize(18);
        this.selectedCountTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.selectedCountTextView.setTextColor(Theme.getColor("actionBarActionModeDefaultIcon"));
        createActionMode.addView(this.selectedCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 72, 0, 0, 0));
        this.selectedCountTextView.setOnTouchListener(StickersActivity$$ExternalSyntheticLambda1.INSTANCE);
        createActionMode.addItemWithWidth(2, R.drawable.msg_share, AndroidUtilities.dp(54.0f));
        if (this.currentType != 5) {
            createActionMode.addItemWithWidth(0, R.drawable.msg_archive, AndroidUtilities.dp(54.0f));
        }
        this.deleteMenuItem = createActionMode.addItemWithWidth(1, R.drawable.msg_delete, AndroidUtilities.dp(54.0f));
        ArrayList<TLRPC$TL_messages_stickerSet> arrayList = new ArrayList<>(MessagesController.getInstance(this.currentAccount).filterPremiumStickers(MediaDataController.getInstance(this.currentAccount).getStickerSets(this.currentType)));
        ArrayList<TLRPC$StickerSetCovered> featuredEmojiSets = this.currentType == 5 ? MediaDataController.getInstance(this.currentAccount).getFeaturedEmojiSets() : MediaDataController.getInstance(this.currentAccount).getFeaturedStickerSets();
        if (this.currentType == 5) {
            this.emojiPacks = new ArrayList<>();
            if (!UserConfig.getInstance(this.currentAccount).isPremium()) {
                int i2 = 0;
                while (i2 < arrayList.size()) {
                    if (arrayList.get(i2) != null && !MessageObject.isPremiumEmojiPack(arrayList.get(i2))) {
                        this.emojiPacks.add(arrayList.get(i2));
                        arrayList.remove(i2);
                        i2--;
                    }
                    i2++;
                }
            }
            this.emojiPacks.addAll(arrayList);
            for (int i3 = 0; i3 < featuredEmojiSets.size(); i3++) {
                int i4 = 0;
                while (true) {
                    if (i4 >= this.emojiPacks.size()) {
                        z = false;
                        break;
                    } else if (featuredEmojiSets.get(i3).set.id == this.emojiPacks.get(i4).set.id) {
                        z = true;
                        break;
                    } else {
                        i4++;
                    }
                }
                if (!z) {
                    this.emojiPacks.add(convertFeatured(featuredEmojiSets.get(i3)));
                }
            }
            arrayList = this.emojiPacks;
            featuredEmojiSets = new ArrayList<>();
        }
        this.listAdapter = new ListAdapter(context, arrayList, featuredEmojiSets);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setFocusable(true);
        this.listView.setTag(7);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) { // from class: org.telegram.ui.StickersActivity.2
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.LinearLayoutManager
            public void calculateExtraLayoutSpace(RecyclerView.State state, int[] iArr) {
                iArr[1] = StickersActivity.this.listView.getHeight();
            }
        };
        this.layoutManager = linearLayoutManager;
        linearLayoutManager.setOrientation(1);
        this.listView.setLayoutManager(this.layoutManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
        this.itemTouchHelper = itemTouchHelper;
        itemTouchHelper.attachToRecyclerView(this.listView);
        DefaultItemAnimator defaultItemAnimator = (DefaultItemAnimator) this.listView.getItemAnimator();
        this.itemAnimator = defaultItemAnimator;
        defaultItemAnimator.setSupportsChangeAnimations(false);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda4
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i5) {
                StickersActivity.this.lambda$createView$2(context, view, i5);
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i5) {
                boolean lambda$createView$3;
                lambda$createView$3 = StickersActivity.this.lambda$createView$3(view, i5);
                return lambda$createView$3;
            }
        });
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(Context context, View view, int i) {
        if (i >= this.featuredStickersStartRow && i < this.featuredStickersEndRow && getParentActivity() != null) {
            TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
            TLRPC$StickerSet tLRPC$StickerSet = ((TLRPC$StickerSetCovered) this.listAdapter.featuredStickerSets.get(i - this.featuredStickersStartRow)).set;
            tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSet.id;
            tLRPC$TL_inputStickerSetID.access_hash = tLRPC$StickerSet.access_hash;
            if (this.currentType == 5) {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(tLRPC$TL_inputStickerSetID);
                showDialog(new EmojiPacksAlert(this, getParentActivity(), getResourceProvider(), arrayList));
                return;
            }
            showDialog(new StickersAlert(getParentActivity(), this, tLRPC$TL_inputStickerSetID, (TLRPC$TL_messages_stickerSet) null, (StickersAlert.StickersAlertDelegate) null));
            return;
        }
        if (i == this.featuredStickersShowMoreRow) {
            if (this.currentType == 5) {
                ArrayList arrayList2 = new ArrayList();
                ArrayList<TLRPC$StickerSetCovered> featuredEmojiSets = MediaDataController.getInstance(this.currentAccount).getFeaturedEmojiSets();
                for (int i2 = 0; featuredEmojiSets != null && i2 < featuredEmojiSets.size(); i2++) {
                    TLRPC$StickerSetCovered tLRPC$StickerSetCovered = featuredEmojiSets.get(i2);
                    if (tLRPC$StickerSetCovered != null && tLRPC$StickerSetCovered.set != null) {
                        TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID2 = new TLRPC$TL_inputStickerSetID();
                        TLRPC$StickerSet tLRPC$StickerSet2 = tLRPC$StickerSetCovered.set;
                        tLRPC$TL_inputStickerSetID2.id = tLRPC$StickerSet2.id;
                        tLRPC$TL_inputStickerSetID2.access_hash = tLRPC$StickerSet2.access_hash;
                        arrayList2.add(tLRPC$TL_inputStickerSetID2);
                    }
                }
                MediaDataController.getInstance(this.currentAccount).markFeaturedStickersAsRead(true, true);
                showDialog(new EmojiPacksAlert(this, getParentActivity(), getResourceProvider(), arrayList2));
                return;
            }
            TrendingStickersAlert trendingStickersAlert = new TrendingStickersAlert(context, this, new TrendingStickersLayout(context, new TrendingStickersLayout.Delegate() { // from class: org.telegram.ui.StickersActivity.3
                @Override // org.telegram.ui.Components.TrendingStickersLayout.Delegate
                public void onStickerSetAdd(TLRPC$StickerSetCovered tLRPC$StickerSetCovered2, boolean z) {
                    MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), tLRPC$StickerSetCovered2, 2, StickersActivity.this, false, false);
                }

                @Override // org.telegram.ui.Components.TrendingStickersLayout.Delegate
                public void onStickerSetRemove(TLRPC$StickerSetCovered tLRPC$StickerSetCovered2) {
                    MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), tLRPC$StickerSetCovered2, 0, StickersActivity.this, false, false);
                }
            }), null);
            this.trendingStickersAlert = trendingStickersAlert;
            trendingStickersAlert.show();
        } else if (i >= this.stickersStartRow && i < this.stickersEndRow && getParentActivity() != null) {
            if (this.listAdapter.hasSelected()) {
                this.listAdapter.toggleSelected(i);
                return;
            }
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) this.listAdapter.stickerSets.get(i - this.stickersStartRow);
            ArrayList<TLRPC$Document> arrayList3 = tLRPC$TL_messages_stickerSet.documents;
            if (arrayList3 == null || arrayList3.isEmpty()) {
                return;
            }
            TLRPC$StickerSet tLRPC$StickerSet3 = tLRPC$TL_messages_stickerSet.set;
            if (tLRPC$StickerSet3 != null && tLRPC$StickerSet3.emojis) {
                ArrayList arrayList4 = new ArrayList();
                TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID3 = new TLRPC$TL_inputStickerSetID();
                TLRPC$StickerSet tLRPC$StickerSet4 = tLRPC$TL_messages_stickerSet.set;
                tLRPC$TL_inputStickerSetID3.id = tLRPC$StickerSet4.id;
                tLRPC$TL_inputStickerSetID3.access_hash = tLRPC$StickerSet4.access_hash;
                arrayList4.add(tLRPC$TL_inputStickerSetID3);
                showDialog(new EmojiPacksAlert(this, getParentActivity(), getResourceProvider(), arrayList4));
                return;
            }
            showDialog(new StickersAlert(getParentActivity(), this, (TLRPC$InputStickerSet) null, tLRPC$TL_messages_stickerSet, (StickersAlert.StickersAlertDelegate) null));
        } else if (i == this.archivedRow) {
            presentFragment(new ArchivedStickersActivity(this.currentType));
        } else if (i == this.masksRow) {
            presentFragment(new StickersActivity(1));
        } else if (i == this.emojiPacksRow) {
            presentFragment(new StickersActivity(5));
        } else if (i == this.suggestRow) {
            if (getParentActivity() == null) {
                return;
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("SuggestStickers", R.string.SuggestStickers));
            String[] strArr = {LocaleController.getString("SuggestStickersAll", R.string.SuggestStickersAll), LocaleController.getString("SuggestStickersInstalled", R.string.SuggestStickersInstalled), LocaleController.getString("SuggestStickersNone", R.string.SuggestStickersNone)};
            LinearLayout linearLayout = new LinearLayout(getParentActivity());
            linearLayout.setOrientation(1);
            builder.setView(linearLayout);
            int i3 = 0;
            while (i3 < 3) {
                RadioColorCell radioColorCell = new RadioColorCell(getParentActivity());
                radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                radioColorCell.setTag(Integer.valueOf(i3));
                radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
                radioColorCell.setTextAndValue(strArr[i3], SharedConfig.suggestStickers == i3);
                linearLayout.addView(radioColorCell);
                radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        StickersActivity.this.lambda$createView$1(builder, view2);
                    }
                });
                i3++;
            }
            showDialog(builder.create());
        } else if (i == this.loopRow) {
            SharedConfig.toggleLoopStickers();
            this.listAdapter.notifyItemChanged(this.loopRow, 0);
        } else if (i == this.largeEmojiRow) {
            SharedConfig.toggleBigEmoji();
            ((TextCheckCell) view).setChecked(SharedConfig.allowBigEmoji);
        } else if (i == this.reactionsDoubleTapRow) {
            presentFragment(new ReactionsDoubleTapManageActivity());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(AlertDialog.Builder builder, View view) {
        SharedConfig.setSuggestStickers(((Integer) view.getTag()).intValue());
        this.listAdapter.notifyItemChanged(this.suggestRow);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$3(View view, int i) {
        if (this.listAdapter.hasSelected() || i < this.stickersStartRow || i >= this.stickersEndRow) {
            return false;
        }
        this.listAdapter.toggleSelected(i);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (this.listAdapter.hasSelected()) {
            this.listAdapter.clearSelected();
            return false;
        }
        return super.onBackPressed();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.stickersDidLoad) {
            int intValue = ((Integer) objArr[0]).intValue();
            int i3 = this.currentType;
            if (intValue == i3) {
                this.listAdapter.loadingFeaturedStickerSets.clear();
                updateRows();
            } else if (i3 != 0 || intValue != 1) {
            } else {
                this.listAdapter.notifyItemChanged(this.masksRow);
            }
        } else if (i == NotificationCenter.featuredStickersDidLoad || i == NotificationCenter.featuredEmojiDidLoad) {
            updateRows();
        } else if (i == NotificationCenter.archivedStickersCountDidLoad) {
            if (((Integer) objArr[0]).intValue() != this.currentType) {
                return;
            }
            updateRows();
        } else {
            int i4 = NotificationCenter.currentUserPremiumStatusChanged;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendReorder() {
        if (!this.needReorder) {
            return;
        }
        MediaDataController.getInstance(this.currentAccount).calcNewHash(this.currentType);
        this.needReorder = false;
        this.activeReorderingRequests++;
        TLRPC$TL_messages_reorderStickerSets tLRPC$TL_messages_reorderStickerSets = new TLRPC$TL_messages_reorderStickerSets();
        int i = this.currentType;
        tLRPC$TL_messages_reorderStickerSets.masks = i == 1;
        tLRPC$TL_messages_reorderStickerSets.emojis = i == 5;
        for (int i2 = 0; i2 < this.listAdapter.stickerSets.size(); i2++) {
            tLRPC$TL_messages_reorderStickerSets.order.add(Long.valueOf(((TLRPC$TL_messages_stickerSet) this.listAdapter.stickerSets.get(i2)).set.id));
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_reorderStickerSets, new RequestDelegate() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda3
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                StickersActivity.this.lambda$sendReorder$5(tLObject, tLRPC$TL_error);
            }
        });
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, Integer.valueOf(this.currentType));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendReorder$4() {
        this.activeReorderingRequests--;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendReorder$5(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                StickersActivity.this.lambda$sendReorder$4();
            }
        });
    }

    private void updateRows() {
        boolean z;
        DiffUtil.DiffResult diffResult;
        int i;
        int i2;
        MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
        final ArrayList arrayList = new ArrayList(MessagesController.getInstance(this.currentAccount).filterPremiumStickers(mediaDataController.getStickerSets(this.currentType)));
        List featuredEmojiSets = this.currentType == 5 ? mediaDataController.getFeaturedEmojiSets() : mediaDataController.getFeaturedStickerSets();
        if (featuredEmojiSets.size() <= 3 || this.currentType == 5) {
            z = false;
        } else {
            featuredEmojiSets = featuredEmojiSets.subList(0, 3);
            z = true;
        }
        final ArrayList arrayList2 = new ArrayList(featuredEmojiSets);
        if (this.currentType == 5) {
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                checkPack((TLRPC$TL_messages_stickerSet) arrayList.get(i3));
            }
            for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                checkPack((TLRPC$StickerSetCovered) arrayList2.get(i4));
            }
            arrayList2.clear();
            arrayList.clear();
            ArrayList<TLRPC$TL_messages_stickerSet> arrayList3 = this.emojiPacks;
            if (arrayList3 != null) {
                arrayList.addAll(arrayList3);
            }
        }
        DiffUtil.DiffResult diffResult2 = null;
        if (this.listAdapter != null) {
            if (!this.isPaused) {
                diffResult2 = DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: org.telegram.ui.StickersActivity.4
                    List<TLRPC$TL_messages_stickerSet> oldList;

                    {
                        this.oldList = StickersActivity.this.listAdapter.stickerSets;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getOldListSize() {
                        return this.oldList.size();
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getNewListSize() {
                        return arrayList.size();
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areItemsTheSame(int i5, int i6) {
                        return this.oldList.get(i5).set.id == ((TLRPC$TL_messages_stickerSet) arrayList.get(i6)).set.id;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areContentsTheSame(int i5, int i6) {
                        TLRPC$StickerSet tLRPC$StickerSet = this.oldList.get(i5).set;
                        TLRPC$StickerSet tLRPC$StickerSet2 = ((TLRPC$TL_messages_stickerSet) arrayList.get(i6)).set;
                        return TextUtils.equals(tLRPC$StickerSet.title, tLRPC$StickerSet2.title) && tLRPC$StickerSet.count == tLRPC$StickerSet2.count;
                    }
                });
                diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: org.telegram.ui.StickersActivity.5
                    List<TLRPC$StickerSetCovered> oldList;

                    {
                        this.oldList = StickersActivity.this.listAdapter.featuredStickerSets;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getOldListSize() {
                        return this.oldList.size();
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getNewListSize() {
                        return arrayList2.size();
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areItemsTheSame(int i5, int i6) {
                        return this.oldList.get(i5).set.id == ((TLRPC$StickerSetCovered) arrayList2.get(i6)).set.id;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areContentsTheSame(int i5, int i6) {
                        TLRPC$StickerSet tLRPC$StickerSet = this.oldList.get(i5).set;
                        TLRPC$StickerSet tLRPC$StickerSet2 = ((TLRPC$StickerSetCovered) arrayList2.get(i6)).set;
                        return TextUtils.equals(tLRPC$StickerSet.title, tLRPC$StickerSet2.title) && tLRPC$StickerSet.count == tLRPC$StickerSet2.count && tLRPC$StickerSet.installed == tLRPC$StickerSet2.installed;
                    }
                });
            } else {
                diffResult = null;
            }
            this.listAdapter.setStickerSets(arrayList);
            this.listAdapter.setFeaturedStickerSets(arrayList2);
        } else {
            diffResult = null;
        }
        this.rowCount = 0;
        int i5 = this.currentType;
        if (i5 == 0) {
            int i6 = 0 + 1;
            this.rowCount = i6;
            this.suggestRow = 0;
            int i7 = i6 + 1;
            this.rowCount = i7;
            this.largeEmojiRow = i6;
            int i8 = i7 + 1;
            this.rowCount = i8;
            this.loopRow = i7;
            this.rowCount = i8 + 1;
            this.loopInfoRow = i8;
        } else {
            this.suggestRow = -1;
            this.largeEmojiRow = -1;
            this.loopRow = -1;
            this.loopInfoRow = -1;
        }
        if (i5 == 0) {
            int i9 = this.rowCount;
            int i10 = i9 + 1;
            this.rowCount = i10;
            this.masksRow = i9;
            this.rowCount = i10 + 1;
            this.emojiPacksRow = i10;
        } else {
            this.masksRow = -1;
            this.emojiPacksRow = -1;
        }
        int archivedStickersCount = mediaDataController.getArchivedStickersCount(i5);
        int i11 = 2;
        if (archivedStickersCount != 0 && (i2 = this.currentType) != 5) {
            boolean z2 = this.archivedRow == -1;
            int i12 = this.rowCount;
            int i13 = i12 + 1;
            this.rowCount = i13;
            this.archivedRow = i12;
            if (i2 == 1) {
                this.rowCount = i13 + 1;
            } else {
                i13 = -1;
            }
            this.archivedInfoRow = i13;
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null && z2) {
                if (i13 == -1) {
                    i11 = 1;
                }
                listAdapter.notifyItemRangeInserted(i12, i11);
            }
        } else {
            int i14 = this.archivedRow;
            int i15 = this.archivedInfoRow;
            this.archivedRow = -1;
            this.archivedInfoRow = -1;
            ListAdapter listAdapter2 = this.listAdapter;
            if (listAdapter2 != null && i14 != -1) {
                if (i15 == -1) {
                    i11 = 1;
                }
                listAdapter2.notifyItemRangeRemoved(i14, i11);
            }
        }
        if (this.currentType == 0) {
            int i16 = this.rowCount;
            int i17 = i16 + 1;
            this.rowCount = i17;
            this.reactionsDoubleTapRow = i16;
            this.rowCount = i17 + 1;
            this.stickersBotInfo = i17;
        } else {
            this.reactionsDoubleTapRow = -1;
            this.stickersBotInfo = -1;
        }
        if (!arrayList2.isEmpty() && ((i = this.currentType) == 0 || i == 5)) {
            int i18 = this.rowCount;
            int i19 = i18 + 1;
            this.rowCount = i19;
            this.featuredStickersHeaderRow = i18;
            this.featuredStickersStartRow = i19;
            int size = i19 + arrayList2.size();
            this.rowCount = size;
            this.featuredStickersEndRow = size;
            if (z) {
                this.rowCount = size + 1;
                this.featuredStickersShowMoreRow = size;
            } else {
                this.featuredStickersShowMoreRow = -1;
            }
            int i20 = this.rowCount;
            this.rowCount = i20 + 1;
            this.featuredStickersShadowRow = i20;
        } else {
            this.featuredStickersHeaderRow = -1;
            this.featuredStickersStartRow = -1;
            this.featuredStickersEndRow = -1;
            this.featuredStickersShowMoreRow = -1;
            this.featuredStickersShadowRow = -1;
        }
        int size2 = arrayList.size();
        if (size2 > 0) {
            if (this.featuredStickersHeaderRow != -1) {
                int i21 = this.rowCount;
                this.rowCount = i21 + 1;
                this.stickersHeaderRow = i21;
            } else {
                this.stickersHeaderRow = -1;
            }
            int i22 = this.rowCount;
            this.stickersStartRow = i22;
            int i23 = i22 + size2;
            this.rowCount = i23;
            this.stickersEndRow = i23;
            if (this.currentType != 1) {
                this.rowCount = i23 + 1;
                this.stickersShadowRow = i23;
                this.masksInfoRow = -1;
            } else {
                this.rowCount = i23 + 1;
                this.masksInfoRow = i23;
                this.stickersShadowRow = -1;
            }
        } else {
            this.stickersHeaderRow = -1;
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
            this.stickersShadowRow = -1;
            this.masksInfoRow = -1;
        }
        ListAdapter listAdapter3 = this.listAdapter;
        if (listAdapter3 != null) {
            if (diffResult2 != null) {
                final int i24 = this.stickersStartRow;
                if (i24 < 0) {
                    i24 = this.rowCount;
                }
                listAdapter3.notifyItemRangeChanged(0, i24);
                diffResult2.dispatchUpdatesTo(new ListUpdateCallback() { // from class: org.telegram.ui.StickersActivity.6
                    @Override // androidx.recyclerview.widget.ListUpdateCallback
                    public void onInserted(int i25, int i26) {
                        StickersActivity.this.listAdapter.notifyItemRangeInserted(i24 + i25, i26);
                    }

                    @Override // androidx.recyclerview.widget.ListUpdateCallback
                    public void onRemoved(int i25, int i26) {
                        StickersActivity.this.listAdapter.notifyItemRangeRemoved(i24 + i25, i26);
                    }

                    @Override // androidx.recyclerview.widget.ListUpdateCallback
                    public void onMoved(int i25, int i26) {
                        if (StickersActivity.this.currentType == 5) {
                            ListAdapter listAdapter4 = StickersActivity.this.listAdapter;
                            int i27 = i24;
                            listAdapter4.notifyItemMoved(i25 + i27, i27 + i26);
                        }
                    }

                    @Override // androidx.recyclerview.widget.ListUpdateCallback
                    public void onChanged(int i25, int i26, Object obj) {
                        StickersActivity.this.listAdapter.notifyItemRangeChanged(i24 + i25, i26);
                    }
                });
            }
            if (diffResult == null) {
                return;
            }
            final int i25 = this.featuredStickersStartRow;
            if (i25 < 0) {
                i25 = this.rowCount;
            }
            this.listAdapter.notifyItemRangeChanged(0, i25);
            diffResult.dispatchUpdatesTo(new ListUpdateCallback() { // from class: org.telegram.ui.StickersActivity.7
                @Override // androidx.recyclerview.widget.ListUpdateCallback
                public void onMoved(int i26, int i27) {
                }

                @Override // androidx.recyclerview.widget.ListUpdateCallback
                public void onInserted(int i26, int i27) {
                    StickersActivity.this.listAdapter.notifyItemRangeInserted(i25 + i26, i27);
                }

                @Override // androidx.recyclerview.widget.ListUpdateCallback
                public void onRemoved(int i26, int i27) {
                    StickersActivity.this.listAdapter.notifyItemRangeRemoved(i25 + i26, i27);
                }

                @Override // androidx.recyclerview.widget.ListUpdateCallback
                public void onChanged(int i26, int i27, Object obj) {
                    StickersActivity.this.listAdapter.notifyItemRangeChanged(i25 + i26, i27);
                }
            });
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    @SuppressLint({"NotifyDataSetChanged"})
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private final LongSparseArray<Boolean> selectedItems = new LongSparseArray<>();
        private final List<TLRPC$TL_messages_stickerSet> stickerSets = new ArrayList();
        private final List<TLRPC$StickerSetCovered> featuredStickerSets = new ArrayList();
        private final List<Long> loadingFeaturedStickerSets = new ArrayList();

        public ListAdapter(Context context, List<TLRPC$TL_messages_stickerSet> list, List<TLRPC$StickerSetCovered> list2) {
            this.mContext = context;
            setStickerSets(list);
            if (list2.size() > 3) {
                setFeaturedStickerSets(list2.subList(0, 3));
            } else {
                setFeaturedStickerSets(list2);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @SuppressLint({"NotifyDataSetChanged"})
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (StickersActivity.this.isListeningForFeaturedUpdate) {
                StickersActivity.this.isListeningForFeaturedUpdate = false;
            }
        }

        public void setStickerSets(List<TLRPC$TL_messages_stickerSet> list) {
            this.stickerSets.clear();
            this.stickerSets.addAll(list);
        }

        public void setFeaturedStickerSets(List<TLRPC$StickerSetCovered> list) {
            this.featuredStickerSets.clear();
            this.featuredStickerSets.addAll(list);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return StickersActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            if (i < StickersActivity.this.featuredStickersStartRow || i >= StickersActivity.this.featuredStickersEndRow) {
                return (i < StickersActivity.this.stickersStartRow || i >= StickersActivity.this.stickersEndRow) ? i : this.stickerSets.get(i - StickersActivity.this.stickersStartRow).set.id;
            }
            return this.featuredStickerSets.get(i - StickersActivity.this.featuredStickersStartRow).set.id;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processSelectionMenu(final int i) {
            String string;
            TextView textView;
            int i2 = 0;
            if (i == 2) {
                StringBuilder sb = new StringBuilder();
                int size = this.stickerSets.size();
                while (i2 < size) {
                    TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.stickerSets.get(i2);
                    if (this.selectedItems.get(tLRPC$TL_messages_stickerSet.set.id, Boolean.FALSE).booleanValue()) {
                        if (sb.length() != 0) {
                            sb.append("\n");
                        }
                        sb.append(StickersActivity.this.getLinkForSet(tLRPC$TL_messages_stickerSet));
                    }
                    i2++;
                }
                String sb2 = sb.toString();
                ShareAlert createShareAlert = ShareAlert.createShareAlert(((BaseFragment) StickersActivity.this).fragmentView.getContext(), null, sb2, false, sb2, false);
                createShareAlert.setDelegate(new ShareAlert.ShareAlertDelegate() { // from class: org.telegram.ui.StickersActivity.ListAdapter.1
                    @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                    public void didShare() {
                        ListAdapter.this.clearSelected();
                    }

                    @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                    public boolean didCopy() {
                        ListAdapter.this.clearSelected();
                        return true;
                    }
                });
                createShareAlert.show();
            } else if (i == 0 || i == 1) {
                final ArrayList arrayList = new ArrayList(this.selectedItems.size());
                int size2 = this.stickerSets.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    TLRPC$StickerSet tLRPC$StickerSet = this.stickerSets.get(i3).set;
                    if (this.selectedItems.get(tLRPC$StickerSet.id, Boolean.FALSE).booleanValue()) {
                        arrayList.add(tLRPC$StickerSet);
                    }
                }
                int size3 = arrayList.size();
                if (size3 == 0) {
                    return;
                }
                if (size3 == 1) {
                    int size4 = this.stickerSets.size();
                    while (true) {
                        if (i2 >= size4) {
                            break;
                        }
                        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet2 = this.stickerSets.get(i2);
                        if (this.selectedItems.get(tLRPC$TL_messages_stickerSet2.set.id, Boolean.FALSE).booleanValue()) {
                            processSelectionOption(i, tLRPC$TL_messages_stickerSet2);
                            break;
                        }
                        i2++;
                    }
                    StickersActivity.this.listAdapter.clearSelected();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(StickersActivity.this.getParentActivity());
                if (i == 1) {
                    builder.setTitle(LocaleController.formatString("DeleteStickerSetsAlertTitle", R.string.DeleteStickerSetsAlertTitle, LocaleController.formatPluralString("StickerSets", size3, new Object[0])));
                    builder.setMessage(LocaleController.formatString("DeleteStickersAlertMessage", R.string.DeleteStickersAlertMessage, Integer.valueOf(size3)));
                    string = LocaleController.getString("Delete", R.string.Delete);
                } else {
                    builder.setTitle(LocaleController.formatString("ArchiveStickerSetsAlertTitle", R.string.ArchiveStickerSetsAlertTitle, LocaleController.formatPluralString("StickerSets", size3, new Object[0])));
                    builder.setMessage(LocaleController.formatString("ArchiveStickersAlertMessage", R.string.ArchiveStickersAlertMessage, Integer.valueOf(size3)));
                    string = LocaleController.getString("Archive", R.string.Archive);
                }
                builder.setPositiveButton(string, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        StickersActivity.ListAdapter.this.lambda$processSelectionMenu$0(arrayList, i, dialogInterface, i4);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog create = builder.create();
                StickersActivity.this.showDialog(create);
                if (i != 1 || (textView = (TextView) create.getButton(-1)) == null) {
                    return;
                }
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSelectionMenu$0(ArrayList arrayList, int i, DialogInterface dialogInterface, int i2) {
            StickersActivity.this.listAdapter.clearSelected();
            MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSets(arrayList, StickersActivity.this.currentType, i == 1 ? 0 : 1, StickersActivity.this, true);
        }

        private void processSelectionOption(int i, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
            int indexOf;
            if (i == 0) {
                MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), tLRPC$TL_messages_stickerSet, !tLRPC$TL_messages_stickerSet.set.archived ? 1 : 2, StickersActivity.this, true, true);
            } else if (i == 1) {
                MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), tLRPC$TL_messages_stickerSet, 0, StickersActivity.this, true, true);
            } else if (i == 2) {
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", StickersActivity.this.getLinkForSet(tLRPC$TL_messages_stickerSet));
                    StickersActivity.this.getParentActivity().startActivityForResult(Intent.createChooser(intent, LocaleController.getString("StickersShare", R.string.StickersShare)), 500);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } else if (i != 3) {
                if (i != 4 || (indexOf = this.stickerSets.indexOf(tLRPC$TL_messages_stickerSet)) < 0) {
                    return;
                }
                StickersActivity.this.listAdapter.toggleSelected(StickersActivity.this.stickersStartRow + indexOf);
            } else {
                try {
                    Locale locale = Locale.US;
                    StringBuilder sb = new StringBuilder();
                    sb.append("https://");
                    sb.append(MessagesController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).linkPrefix);
                    sb.append("/");
                    sb.append(tLRPC$TL_messages_stickerSet.set.emojis ? "addemoji" : "addstickers");
                    sb.append("/%s");
                    ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", String.format(locale, sb.toString(), tLRPC$TL_messages_stickerSet.set.short_name)));
                    BulletinFactory.createCopyLinkBulletin(StickersActivity.this).show();
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:144:0x03c8, code lost:
            if (r5 == false) goto L145;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC$StickerSet tLRPC$StickerSet;
            boolean z;
            String string;
            TLRPC$TL_availableReaction tLRPC$TL_availableReaction;
            boolean z2 = false;
            int i2 = 1;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    StickerSetCell stickerSetCell = (StickerSetCell) viewHolder.itemView;
                    int i3 = i - StickersActivity.this.stickersStartRow;
                    TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.stickerSets.get(i3);
                    boolean z3 = (stickerSetCell.getStickersSet() == null && tLRPC$TL_messages_stickerSet == null) || !(stickerSetCell.getStickersSet() == null || tLRPC$TL_messages_stickerSet == null || stickerSetCell.getStickersSet().set.id != tLRPC$TL_messages_stickerSet.set.id);
                    stickerSetCell.setStickersSet(tLRPC$TL_messages_stickerSet, i3 != this.stickerSets.size() - 1);
                    stickerSetCell.setChecked(this.selectedItems.get(getItemId(i), Boolean.FALSE).booleanValue(), false);
                    stickerSetCell.setReorderable(hasSelected(), false);
                    if (tLRPC$TL_messages_stickerSet == null || (tLRPC$StickerSet = tLRPC$TL_messages_stickerSet.set) == null || !tLRPC$StickerSet.emojis) {
                        return;
                    }
                    boolean isStickerPackInstalled = StickersActivity.this.getMediaDataController().isStickerPackInstalled(tLRPC$TL_messages_stickerSet.set.id);
                    boolean z4 = !UserConfig.getInstance(((BaseFragment) StickersActivity.this).currentAccount).isPremium();
                    if (z4) {
                        int i4 = 0;
                        while (true) {
                            if (i4 >= tLRPC$TL_messages_stickerSet.documents.size()) {
                                z = false;
                                break;
                            } else if (!MessageObject.isFreeEmoji(tLRPC$TL_messages_stickerSet.documents.get(i4))) {
                                z = true;
                                break;
                            } else {
                                i4++;
                            }
                        }
                    }
                    z2 = z4;
                    if (!z2) {
                        i2 = isStickerPackInstalled ? 4 : 3;
                    } else if (isStickerPackInstalled && !tLRPC$TL_messages_stickerSet.set.official) {
                        i2 = 2;
                    }
                    stickerSetCell.updateButtonState(i2, z3);
                    return;
                case 1:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (i != StickersActivity.this.stickersBotInfo) {
                        if (i == StickersActivity.this.archivedInfoRow) {
                            if (StickersActivity.this.currentType == 0) {
                                textInfoPrivacyCell.setText(LocaleController.getString("ArchivedStickersInfo", R.string.ArchivedStickersInfo));
                                return;
                            } else {
                                textInfoPrivacyCell.setText(LocaleController.getString("ArchivedMasksInfo", R.string.ArchivedMasksInfo));
                                return;
                            }
                        } else if (i != StickersActivity.this.loopInfoRow) {
                            if (i != StickersActivity.this.masksInfoRow) {
                                return;
                            }
                            textInfoPrivacyCell.setText(LocaleController.getString("MasksInfo", R.string.MasksInfo));
                            return;
                        } else {
                            textInfoPrivacyCell.setText(LocaleController.getString("LoopAnimatedStickersInfo", R.string.LoopAnimatedStickersInfo));
                            return;
                        }
                    }
                    textInfoPrivacyCell.setText(addStickersBotSpan(LocaleController.getString("StickersBotInfo", R.string.StickersBotInfo)));
                    return;
                case 2:
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    if (i == StickersActivity.this.featuredStickersShowMoreRow) {
                        textCell.setColors("windowBackgroundWhiteBlueText4", "windowBackgroundWhiteBlueText4");
                        if (StickersActivity.this.currentType == 5) {
                            textCell.setTextAndIcon(LocaleController.getString((int) R.string.ShowMoreEmojiPacks), R.drawable.msg_trending, false);
                            return;
                        } else {
                            textCell.setTextAndIcon(LocaleController.getString((int) R.string.ShowMoreStickers), R.drawable.msg_trending, false);
                            return;
                        }
                    }
                    textCell.imageView.setTranslationX(0.0f);
                    textCell.setColors("windowBackgroundWhiteGrayIcon", "windowBackgroundWhiteBlackText");
                    String str = "";
                    if (i == StickersActivity.this.archivedRow) {
                        int archivedStickersCount = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).getArchivedStickersCount(StickersActivity.this.currentType);
                        if (archivedStickersCount > 0) {
                            str = Integer.toString(archivedStickersCount);
                        }
                        if (StickersActivity.this.currentType != 0) {
                            if (StickersActivity.this.currentType == 5) {
                                textCell.setTextAndValue(LocaleController.getString("ArchivedEmojiPacks", R.string.ArchivedEmojiPacks), str, true);
                                return;
                            } else {
                                textCell.setTextAndValue(LocaleController.getString("ArchivedMasks", R.string.ArchivedMasks), str, true);
                                return;
                            }
                        }
                        textCell.setTextAndValueAndIcon(LocaleController.getString((int) R.string.ArchivedStickers), str, R.drawable.msg_archived_stickers, true);
                        return;
                    } else if (i == StickersActivity.this.masksRow) {
                        MediaDataController mediaDataController = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount);
                        int size = MessagesController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).filterPremiumStickers(mediaDataController.getStickerSets(1)).size() + mediaDataController.getArchivedStickersCount(1);
                        String string2 = LocaleController.getString("Masks", R.string.Masks);
                        if (size > 0) {
                            str = Integer.toString(size);
                        }
                        textCell.setTextAndValueAndIcon(string2, str, R.drawable.msg_mask, true);
                        return;
                    } else if (i != StickersActivity.this.emojiPacksRow) {
                        if (i != StickersActivity.this.suggestRow) {
                            return;
                        }
                        int i5 = SharedConfig.suggestStickers;
                        if (i5 == 0) {
                            string = LocaleController.getString("SuggestStickersAll", R.string.SuggestStickersAll);
                        } else if (i5 == 1) {
                            string = LocaleController.getString("SuggestStickersInstalled", R.string.SuggestStickersInstalled);
                        } else {
                            string = LocaleController.getString("SuggestStickersNone", R.string.SuggestStickersNone);
                        }
                        textCell.setTextAndValue(LocaleController.getString("SuggestStickers", R.string.SuggestStickers), string, true);
                        return;
                    } else {
                        int size2 = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).getStickerSets(5).size();
                        textCell.imageView.setTranslationX(-AndroidUtilities.dp(2.0f));
                        String string3 = LocaleController.getString("Emoji", R.string.Emoji);
                        if (size2 > 0) {
                            str = Integer.toString(size2);
                        }
                        textCell.setTextAndValueAndIcon(string3, str, R.drawable.smiles_tab_smiles, true);
                        return;
                    }
                case 3:
                    if (i != StickersActivity.this.stickersShadowRow) {
                        return;
                    }
                    viewHolder.itemView.setBackground(Theme.getThemedDrawable(this.mContext, (int) R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                    return;
                case 4:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    if (i != StickersActivity.this.loopRow) {
                        if (i != StickersActivity.this.largeEmojiRow) {
                            return;
                        }
                        textCheckCell.setTextAndCheck(LocaleController.getString((int) R.string.LargeEmoji), SharedConfig.allowBigEmoji, true);
                        return;
                    }
                    textCheckCell.setTextAndCheck(LocaleController.getString((int) R.string.LoopAnimatedStickers), SharedConfig.loopStickers, true);
                    return;
                case 5:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    textSettingsCell.setText(LocaleController.getString((int) R.string.DoubleTapSetting), false);
                    textSettingsCell.setIcon(R.drawable.msg_reactions2);
                    String doubleTapReaction = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).getDoubleTapReaction();
                    if (doubleTapReaction == null || (tLRPC$TL_availableReaction = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).getReactionsMap().get(doubleTapReaction)) == null) {
                        return;
                    }
                    textSettingsCell.getValueBackupImageView().getImageReceiver().setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.center_icon), "100_100_lastframe", DocumentObject.getSvgThumb(tLRPC$TL_availableReaction.static_icon.thumbs, "windowBackgroundGray", 1.0f), "webp", tLRPC$TL_availableReaction, 1);
                    return;
                case 6:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i == StickersActivity.this.featuredStickersHeaderRow) {
                        headerCell.setText(LocaleController.getString(StickersActivity.this.currentType == 5 ? R.string.FeaturedEmojiPacks : R.string.FeaturedStickers));
                        return;
                    } else if (i != StickersActivity.this.stickersHeaderRow) {
                        return;
                    } else {
                        headerCell.setText(LocaleController.getString(StickersActivity.this.currentType == 5 ? R.string.ChooseStickerMyEmojiPacks : R.string.ChooseStickerMyStickerSets));
                        return;
                    }
                case 7:
                    FeaturedStickerSetCell2 featuredStickerSetCell2 = (FeaturedStickerSetCell2) viewHolder.itemView;
                    TLRPC$StickerSetCovered tLRPC$StickerSetCovered = this.featuredStickerSets.get(i - StickersActivity.this.featuredStickersStartRow);
                    if (StickersActivity.this.isListeningForFeaturedUpdate || (featuredStickerSetCell2.getStickerSet() != null && featuredStickerSetCell2.getStickerSet().set.id == tLRPC$StickerSetCovered.set.id)) {
                        z2 = true;
                    }
                    featuredStickerSetCell2.setStickersSet(tLRPC$StickerSetCovered, true, false, false, z2);
                    featuredStickerSetCell2.setDrawProgress(this.loadingFeaturedStickerSets.contains(Long.valueOf(tLRPC$StickerSetCovered.set.id)), z2);
                    featuredStickerSetCell2.setAddOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StickersActivity.ListAdapter.this.lambda$onBindViewHolder$1(view);
                        }
                    });
                    return;
                default:
                    return;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$1(View view) {
            FeaturedStickerSetCell2 featuredStickerSetCell2 = (FeaturedStickerSetCell2) view.getParent();
            TLRPC$StickerSetCovered stickerSet = featuredStickerSetCell2.getStickerSet();
            if (this.loadingFeaturedStickerSets.contains(Long.valueOf(stickerSet.set.id))) {
                return;
            }
            StickersActivity.this.isListeningForFeaturedUpdate = true;
            this.loadingFeaturedStickerSets.add(Long.valueOf(stickerSet.set.id));
            featuredStickerSetCell2.setDrawProgress(true, true);
            if (featuredStickerSetCell2.isInstalled()) {
                MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), stickerSet, 0, StickersActivity.this, false, false);
            } else {
                MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), stickerSet, 2, StickersActivity.this, false, false);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List list) {
            if (list.isEmpty()) {
                onBindViewHolder(viewHolder, i);
                return;
            }
            int itemViewType = viewHolder.getItemViewType();
            boolean z = false;
            if (itemViewType != 0) {
                if (itemViewType == 4) {
                    if (!list.contains(0) || i != StickersActivity.this.loopRow) {
                        return;
                    }
                    ((TextCheckCell) viewHolder.itemView).setChecked(SharedConfig.loopStickers);
                } else if (itemViewType != 7 || !list.contains(4) || i < StickersActivity.this.featuredStickersStartRow || i > StickersActivity.this.featuredStickersEndRow) {
                } else {
                    ((FeaturedStickerSetCell2) viewHolder.itemView).setStickersSet(this.featuredStickerSets.get(i - StickersActivity.this.featuredStickersStartRow), true, false, false, true);
                }
            } else if (i < StickersActivity.this.stickersStartRow || i >= StickersActivity.this.stickersEndRow) {
            } else {
                StickerSetCell stickerSetCell = (StickerSetCell) viewHolder.itemView;
                if (list.contains(1)) {
                    stickerSetCell.setChecked(this.selectedItems.get(getItemId(i), Boolean.FALSE).booleanValue());
                }
                if (list.contains(2)) {
                    stickerSetCell.setReorderable(hasSelected());
                }
                if (!list.contains(3)) {
                    return;
                }
                if (i - StickersActivity.this.stickersStartRow != this.stickerSets.size() - 1) {
                    z = true;
                }
                stickerSetCell.setNeedDivider(z);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 7 || itemViewType == 2 || itemViewType == 4 || itemViewType == 5;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$2(StickerSetCell stickerSetCell, View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                StickersActivity.this.itemTouchHelper.startDrag(StickersActivity.this.listView.getChildViewHolder(stickerSetCell));
                return false;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$4(View view) {
            int[] iArr;
            final int[] iArr2;
            CharSequence[] charSequenceArr;
            final TLRPC$TL_messages_stickerSet stickersSet = ((StickerSetCell) view.getParent()).getStickersSet();
            AlertDialog.Builder builder = new AlertDialog.Builder(StickersActivity.this.getParentActivity());
            builder.setTitle(stickersSet.set.title);
            if (stickersSet.set.official) {
                iArr2 = new int[]{0, 4};
                charSequenceArr = new CharSequence[]{LocaleController.getString("StickersHide", R.string.StickersHide), LocaleController.getString("StickersReorder", R.string.StickersReorder)};
                iArr = new int[]{R.drawable.msg_archive, R.drawable.msg_reorder};
            } else {
                CharSequence[] charSequenceArr2 = {LocaleController.getString("StickersHide", R.string.StickersHide), LocaleController.getString("StickersCopy", R.string.StickersCopy), LocaleController.getString("StickersReorder", R.string.StickersReorder), LocaleController.getString("StickersShare", R.string.StickersShare), LocaleController.getString("StickersRemove", R.string.StickersRemove)};
                iArr = new int[]{R.drawable.msg_archive, R.drawable.msg_link, R.drawable.msg_reorder, R.drawable.msg_share, R.drawable.msg_delete};
                iArr2 = new int[]{0, 3, 4, 2, 1};
                charSequenceArr = charSequenceArr2;
            }
            builder.setItems(charSequenceArr, iArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$3(iArr2, stickersSet, dialogInterface, i);
                }
            });
            AlertDialog create = builder.create();
            StickersActivity.this.showDialog(create);
            if (iArr2[iArr2.length - 1] == 1) {
                create.setItemColor(charSequenceArr.length - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$3(int[] iArr, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, DialogInterface dialogInterface, int i) {
            processSelectionOption(iArr[i], tLRPC$TL_messages_stickerSet);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @SuppressLint({"ClickableViewAccessibility"})
        /* renamed from: onCreateViewHolder */
        public RecyclerView.ViewHolder mo1741onCreateViewHolder(ViewGroup viewGroup, int i) {
            FeaturedStickerSetCell2 featuredStickerSetCell2;
            if (i == 0) {
                final StickerSetCell stickerSetCell = new StickerSetCell(this.mContext, 1) { // from class: org.telegram.ui.StickersActivity.ListAdapter.2
                    @Override // org.telegram.ui.Cells.StickerSetCell
                    protected void onPremiumButtonClick() {
                        StickersActivity.this.showDialog(new PremiumFeatureBottomSheet(StickersActivity.this, 11, false));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r2v0 */
                    /* JADX WARN: Type inference failed for: r2v1, types: [org.telegram.tgnet.TLRPC$StickerSetCovered] */
                    /* JADX WARN: Type inference failed for: r2v3, types: [org.telegram.tgnet.TLRPC$StickerSetCovered] */
                    @Override // org.telegram.ui.Cells.StickerSetCell
                    protected void onAddButtonClick() {
                        TLRPC$TL_messages_stickerSet stickersSet = getStickersSet();
                        if (stickersSet == null || stickersSet.set == null) {
                            return;
                        }
                        ArrayList<TLRPC$StickerSetCovered> featuredEmojiSets = StickersActivity.this.getMediaDataController().getFeaturedEmojiSets();
                        TLRPC$StickerSetCovered tLRPC$StickerSetCovered = 0;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= featuredEmojiSets.size()) {
                                break;
                            } else if (stickersSet.set.id == featuredEmojiSets.get(i2).set.id) {
                                tLRPC$StickerSetCovered = featuredEmojiSets.get(i2);
                                break;
                            } else {
                                i2++;
                            }
                        }
                        if (tLRPC$StickerSetCovered != 0) {
                            if (ListAdapter.this.loadingFeaturedStickerSets.contains(Long.valueOf(tLRPC$StickerSetCovered.set.id))) {
                                return;
                            }
                            ListAdapter.this.loadingFeaturedStickerSets.add(Long.valueOf(tLRPC$StickerSetCovered.set.id));
                        }
                        MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), tLRPC$StickerSetCovered == 0 ? stickersSet : tLRPC$StickerSetCovered, 2, StickersActivity.this, false, false);
                    }

                    @Override // org.telegram.ui.Cells.StickerSetCell
                    protected void onRemoveButtonClick() {
                        MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), getStickersSet(), 0, StickersActivity.this, false, false);
                    }
                };
                stickerSetCell.setBackgroundColor(StickersActivity.this.getThemedColor("windowBackgroundWhite"));
                stickerSetCell.setOnReorderButtonTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        boolean lambda$onCreateViewHolder$2;
                        lambda$onCreateViewHolder$2 = StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$2(stickerSetCell, view, motionEvent);
                        return lambda$onCreateViewHolder$2;
                    }
                });
                stickerSetCell.setOnOptionsClick(new View.OnClickListener() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$4(view);
                    }
                });
                featuredStickerSetCell2 = stickerSetCell;
            } else if (i == 1) {
                View textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.mContext, (int) R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                featuredStickerSetCell2 = textInfoPrivacyCell;
            } else if (i == 2) {
                View textCell = new TextCell(this.mContext);
                textCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                featuredStickerSetCell2 = textCell;
            } else if (i == 3) {
                featuredStickerSetCell2 = new ShadowSectionCell(this.mContext);
            } else if (i == 5) {
                View textSettingsCell = new TextSettingsCell(this.mContext);
                textSettingsCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                featuredStickerSetCell2 = textSettingsCell;
            } else if (i == 6) {
                View headerCell = new HeaderCell(this.mContext);
                headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                featuredStickerSetCell2 = headerCell;
            } else if (i == 7) {
                FeaturedStickerSetCell2 featuredStickerSetCell22 = new FeaturedStickerSetCell2(this.mContext, StickersActivity.this.getResourceProvider());
                featuredStickerSetCell22.setBackgroundColor(StickersActivity.this.getThemedColor("windowBackgroundWhite"));
                featuredStickerSetCell22.getTextView().setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                featuredStickerSetCell2 = featuredStickerSetCell22;
            } else {
                View textCheckCell = new TextCheckCell(this.mContext);
                textCheckCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                featuredStickerSetCell2 = textCheckCell;
            }
            featuredStickerSetCell2.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(featuredStickerSetCell2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i < StickersActivity.this.featuredStickersStartRow || i >= StickersActivity.this.featuredStickersEndRow) {
                if (i >= StickersActivity.this.stickersStartRow && i < StickersActivity.this.stickersEndRow) {
                    return 0;
                }
                if (i == StickersActivity.this.stickersBotInfo || i == StickersActivity.this.archivedInfoRow || i == StickersActivity.this.loopInfoRow || i == StickersActivity.this.masksInfoRow) {
                    return 1;
                }
                if (i == StickersActivity.this.archivedRow || i == StickersActivity.this.masksRow || i == StickersActivity.this.emojiPacksRow || i == StickersActivity.this.suggestRow || i == StickersActivity.this.featuredStickersShowMoreRow) {
                    return 2;
                }
                if (i == StickersActivity.this.stickersShadowRow || i == StickersActivity.this.featuredStickersShadowRow) {
                    return 3;
                }
                if (i == StickersActivity.this.loopRow || i == StickersActivity.this.largeEmojiRow) {
                    return 4;
                }
                if (i == StickersActivity.this.reactionsDoubleTapRow) {
                    return 5;
                }
                return (i == StickersActivity.this.featuredStickersHeaderRow || i == StickersActivity.this.stickersHeaderRow) ? 6 : 0;
            }
            return 7;
        }

        public void swapElements(int i, int i2) {
            if (i != i2) {
                StickersActivity.this.needReorder = true;
            }
            MediaDataController mediaDataController = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount);
            int i3 = i - StickersActivity.this.stickersStartRow;
            int i4 = i2 - StickersActivity.this.stickersStartRow;
            swapListElements(this.stickerSets, i3, i4);
            swapListElements(mediaDataController.getStickerSets(StickersActivity.this.currentType), i3, i4);
            notifyItemMoved(i, i2);
            if (i == StickersActivity.this.stickersEndRow - 1 || i2 == StickersActivity.this.stickersEndRow - 1) {
                notifyItemRangeChanged(i, 3);
                notifyItemRangeChanged(i2, 3);
            }
        }

        private void swapListElements(List<TLRPC$TL_messages_stickerSet> list, int i, int i2) {
            list.set(i, list.get(i2));
            list.set(i2, list.get(i));
        }

        public void toggleSelected(int i) {
            long itemId = getItemId(i);
            LongSparseArray<Boolean> longSparseArray = this.selectedItems;
            longSparseArray.put(itemId, Boolean.valueOf(!longSparseArray.get(itemId, Boolean.FALSE).booleanValue()));
            notifyItemChanged(i, 1);
            checkActionMode();
        }

        public void clearSelected() {
            this.selectedItems.clear();
            notifyStickersItemsChanged(1);
            checkActionMode();
        }

        public boolean hasSelected() {
            return this.selectedItems.indexOfValue(Boolean.TRUE) != -1;
        }

        public int getSelectedCount() {
            int size = this.selectedItems.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                if (this.selectedItems.valueAt(i2).booleanValue()) {
                    i++;
                }
            }
            return i;
        }

        private void checkActionMode() {
            int selectedCount = StickersActivity.this.listAdapter.getSelectedCount();
            boolean isActionModeShowed = ((BaseFragment) StickersActivity.this).actionBar.isActionModeShowed();
            if (selectedCount <= 0) {
                if (!isActionModeShowed) {
                    return;
                }
                ((BaseFragment) StickersActivity.this).actionBar.hideActionMode();
                notifyStickersItemsChanged(2);
                return;
            }
            checkActionModeIcons();
            StickersActivity.this.selectedCountTextView.setNumber(selectedCount, isActionModeShowed);
            if (isActionModeShowed) {
                return;
            }
            ((BaseFragment) StickersActivity.this).actionBar.showActionMode();
            notifyStickersItemsChanged(2);
            if (SharedConfig.stickersReorderingHintUsed) {
                return;
            }
            SharedConfig.setStickersReorderingHintUsed(true);
            Bulletin.make(((BaseFragment) StickersActivity.this).parentLayout, new ReorderingBulletinLayout(this.mContext, LocaleController.getString("StickersReorderHint", R.string.StickersReorderHint), null), 3250).show();
        }

        private void checkActionModeIcons() {
            boolean z;
            if (hasSelected()) {
                int size = this.stickerSets.size();
                int i = 0;
                for (int i2 = 0; i2 < size; i2++) {
                    if (this.stickerSets.get(i2).set.emojis || (this.selectedItems.get(this.stickerSets.get(i2).set.id, Boolean.FALSE).booleanValue() && this.stickerSets.get(i2).set.official)) {
                        z = false;
                        break;
                    }
                }
                z = true;
                if (!z) {
                    i = 8;
                }
                if (StickersActivity.this.deleteMenuItem.getVisibility() == i) {
                    return;
                }
                StickersActivity.this.deleteMenuItem.setVisibility(i);
            }
        }

        private void notifyStickersItemsChanged(Object obj) {
            notifyItemRangeChanged(StickersActivity.this.stickersStartRow, StickersActivity.this.stickersEndRow - StickersActivity.this.stickersStartRow, obj);
        }

        private CharSequence addStickersBotSpan(String str) {
            int indexOf = str.indexOf("@stickers");
            if (indexOf != -1) {
                try {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
                    spannableStringBuilder.setSpan(new URLSpanNoUnderline("@stickers") { // from class: org.telegram.ui.StickersActivity.ListAdapter.3
                        @Override // org.telegram.ui.Components.URLSpanNoUnderline, android.text.style.URLSpan, android.text.style.ClickableSpan
                        public void onClick(View view) {
                            MessagesController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).openByUserName("stickers", StickersActivity.this, 3);
                        }
                    }, indexOf, indexOf + 9, 18);
                    return spannableStringBuilder;
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            return str;
        }
    }

    private void checkPack(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        if (tLRPC$TL_messages_stickerSet == null) {
            return;
        }
        if (this.emojiPacks == null) {
            ArrayList<TLRPC$TL_messages_stickerSet> arrayList = new ArrayList<>();
            this.emojiPacks = arrayList;
            arrayList.add(tLRPC$TL_messages_stickerSet);
            return;
        }
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= this.emojiPacks.size()) {
                break;
            } else if (this.emojiPacks.get(i).set.id == tLRPC$TL_messages_stickerSet.set.id) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (z) {
            return;
        }
        this.emojiPacks.add(tLRPC$TL_messages_stickerSet);
    }

    private void checkPack(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
        if (tLRPC$StickerSetCovered == null) {
            return;
        }
        if (this.emojiPacks == null) {
            ArrayList<TLRPC$TL_messages_stickerSet> arrayList = new ArrayList<>();
            this.emojiPacks = arrayList;
            arrayList.add(convertFeatured(tLRPC$StickerSetCovered));
            return;
        }
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= this.emojiPacks.size()) {
                break;
            } else if (this.emojiPacks.get(i).set.id == tLRPC$StickerSetCovered.set.id) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (z) {
            return;
        }
        this.emojiPacks.add(convertFeatured(tLRPC$StickerSetCovered));
    }

    private TLRPC$TL_messages_stickerSet convertFeatured(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
        if (tLRPC$StickerSetCovered == null) {
            return null;
        }
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = new TLRPC$TL_messages_stickerSet();
        tLRPC$TL_messages_stickerSet.set = tLRPC$StickerSetCovered.set;
        if (tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetFullCovered) {
            TLRPC$TL_stickerSetFullCovered tLRPC$TL_stickerSetFullCovered = (TLRPC$TL_stickerSetFullCovered) tLRPC$StickerSetCovered;
            tLRPC$TL_messages_stickerSet.documents = tLRPC$TL_stickerSetFullCovered.documents;
            tLRPC$TL_messages_stickerSet.packs = tLRPC$TL_stickerSetFullCovered.packs;
        } else {
            tLRPC$TL_messages_stickerSet.documents = tLRPC$StickerSetCovered.covers;
        }
        return tLRPC$TL_messages_stickerSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLinkForSet(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        Locale locale = Locale.US;
        StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
        sb.append("/");
        sb.append(tLRPC$TL_messages_stickerSet.set.emojis ? "addemoji" : "addstickers");
        sb.append("/%s");
        return String.format(locale, sb.toString(), tLRPC$TL_messages_stickerSet.set.short_name);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{StickerSetCell.class, TextSettingsCell.class, TextCheckCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_ITEMSCOLOR, null, null, null, null, "actionBarActionModeDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_BACKGROUND, null, null, null, null, "actionBarActionModeDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_TOPBACKGROUND, null, null, null, null, "actionBarActionModeDefaultTop"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, null, null, null, null, "actionBarActionModeDefaultSelector"));
        arrayList.add(new ThemeDescription(this.selectedCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "actionBarActionModeDefaultIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
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
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"reorderButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "stickers_menu"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{StickerSetCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{StickerSetCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkboxCheck"));
        TrendingStickersAlert trendingStickersAlert = this.trendingStickersAlert;
        if (trendingStickersAlert != null) {
            arrayList.addAll(trendingStickersAlert.getThemeDescriptions());
        }
        return arrayList;
    }
}
