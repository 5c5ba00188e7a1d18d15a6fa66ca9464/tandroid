package org.telegram.ui;

import android.app.Activity;
import android.app.Dialog;
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
import java.util.Collections;
import java.util.Comparator;
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
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
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
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.ItemOptions;
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

/* loaded from: classes4.dex */
public class StickersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int activeReorderingRequests;
    private ActionBarMenuItem archiveMenuItem;
    private int archivedInfoRow;
    private int archivedRow;
    private int currentType;
    private ActionBarMenuItem deleteMenuItem;
    private int dynamicPackOrder;
    private int dynamicPackOrderInfo;
    private int emojiPacksRow;
    private int featuredRow;
    private int featuredStickersEndRow;
    private int featuredStickersHeaderRow;
    private int featuredStickersShadowRow;
    private int featuredStickersShowMoreRow;
    private int featuredStickersStartRow;
    ArrayList frozenEmojiPacks;
    private boolean isListeningForFeaturedUpdate;
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
    private ActionBarMenuItem shareMenuItem;
    private int stickersBotInfo;
    private int stickersEndRow;
    private int stickersHeaderRow;
    private int stickersSettingsRow;
    private int stickersShadowRow;
    private int stickersStartRow;
    private int suggestAnimatedEmojiInfoRow;
    private int suggestAnimatedEmojiRow;
    private int suggestRow;
    private TrendingStickersAlert trendingStickersAlert;
    private boolean updateSuggestStickers;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private final LongSparseArray selectedItems = new LongSparseArray();
        private final List stickerSets = new ArrayList();
        private final List featuredStickerSets = new ArrayList();
        private final List loadingFeaturedStickerSets = new ArrayList();

        public ListAdapter(Context context, List list, List list2) {
            this.mContext = context;
            setStickerSets(list);
            if (list2.size() > 3) {
                setFeaturedStickerSets(list2.subList(0, 3));
            } else {
                setFeaturedStickerSets(list2);
            }
        }

        private CharSequence addStickersBotSpan(String str) {
            String str2 = "@stickers";
            int indexOf = str.indexOf("@stickers");
            if (indexOf != -1) {
                try {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
                    spannableStringBuilder.setSpan(new URLSpanNoUnderline(str2) { // from class: org.telegram.ui.StickersActivity.ListAdapter.4
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

        private void checkActionMode() {
            int selectedCount = StickersActivity.this.listAdapter.getSelectedCount();
            boolean isActionModeShowed = ((BaseFragment) StickersActivity.this).actionBar.isActionModeShowed();
            if (selectedCount <= 0) {
                if (isActionModeShowed) {
                    ((BaseFragment) StickersActivity.this).actionBar.hideActionMode();
                    notifyStickersItemsChanged(2);
                    return;
                }
                return;
            }
            checkActionModeIcons();
            StickersActivity.this.selectedCountTextView.setNumber(selectedCount, isActionModeShowed);
            if (isActionModeShowed) {
                return;
            }
            ((BaseFragment) StickersActivity.this).actionBar.showActionMode();
            notifyStickersItemsChanged(2);
            if (SharedConfig.stickersReorderingHintUsed || StickersActivity.this.currentType == 5) {
                return;
            }
            SharedConfig.setStickersReorderingHintUsed(true);
            Bulletin.make(((BaseFragment) StickersActivity.this).parentLayout.getLastFragment(), new ReorderingBulletinLayout(this.mContext, LocaleController.getString(R.string.StickersReorderHint), null), 3250).show();
        }

        private void checkActionModeIcons() {
            if (hasSelected()) {
                int size = this.stickerSets.size();
                int i = 0;
                int i2 = 0;
                while (true) {
                    if (i2 < size) {
                        if (((Boolean) this.selectedItems.get(((TLRPC.TL_messages_stickerSet) this.stickerSets.get(i2)).set.id, Boolean.FALSE)).booleanValue() && ((TLRPC.TL_messages_stickerSet) this.stickerSets.get(i2)).set.official && !((TLRPC.TL_messages_stickerSet) this.stickerSets.get(i2)).set.emojis) {
                            i = 8;
                            break;
                        }
                        i2++;
                    } else {
                        break;
                    }
                }
                if (StickersActivity.this.deleteMenuItem.getVisibility() != i) {
                    StickersActivity.this.deleteMenuItem.setVisibility(i);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$1(View view) {
            MediaDataController mediaDataController;
            Activity parentActivity;
            StickersActivity stickersActivity;
            boolean z;
            boolean z2;
            int i;
            FeaturedStickerSetCell2 featuredStickerSetCell2 = (FeaturedStickerSetCell2) view.getParent();
            TLRPC.StickerSetCovered stickerSet = featuredStickerSetCell2.getStickerSet();
            if (this.loadingFeaturedStickerSets.contains(Long.valueOf(stickerSet.set.id))) {
                return;
            }
            StickersActivity.this.isListeningForFeaturedUpdate = true;
            this.loadingFeaturedStickerSets.add(Long.valueOf(stickerSet.set.id));
            featuredStickerSetCell2.setDrawProgress(true, true);
            if (featuredStickerSetCell2.isInstalled()) {
                mediaDataController = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount);
                parentActivity = StickersActivity.this.getParentActivity();
                stickersActivity = StickersActivity.this;
                z = false;
                z2 = false;
                i = 0;
            } else {
                mediaDataController = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount);
                parentActivity = StickersActivity.this.getParentActivity();
                stickersActivity = StickersActivity.this;
                z = false;
                z2 = false;
                i = 2;
            }
            mediaDataController.toggleStickerSet(parentActivity, stickerSet, i, stickersActivity, z, z2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$2(StickerSetCell stickerSetCell, View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            StickersActivity.this.itemTouchHelper.startDrag(StickersActivity.this.listView.getChildViewHolder(stickerSetCell));
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$3(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
            processSelectionOption(0, tL_messages_stickerSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$4(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
            processSelectionOption(4, tL_messages_stickerSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$5(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
            processSelectionOption(3, tL_messages_stickerSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$6(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
            processSelectionOption(4, tL_messages_stickerSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$7(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
            processSelectionOption(2, tL_messages_stickerSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$8(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
            processSelectionOption(1, tL_messages_stickerSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$9(View view) {
            StickerSetCell stickerSetCell = (StickerSetCell) view.getParent();
            final TLRPC.TL_messages_stickerSet stickersSet = stickerSetCell.getStickersSet();
            ItemOptions makeOptions = ItemOptions.makeOptions(StickersActivity.this, stickerSetCell);
            makeOptions.add(R.drawable.msg_archive, LocaleController.getString(R.string.StickersHide), new Runnable() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$3(stickersSet);
                }
            });
            if (stickersSet.set.official) {
                makeOptions.add(R.drawable.msg_reorder, LocaleController.getString(R.string.StickersReorder), new Runnable() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$4(stickersSet);
                    }
                });
            } else {
                makeOptions.add(R.drawable.msg_link, LocaleController.getString(R.string.StickersCopy), new Runnable() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$5(stickersSet);
                    }
                });
                makeOptions.add(R.drawable.msg_reorder, LocaleController.getString(R.string.StickersReorder), new Runnable() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$6(stickersSet);
                    }
                });
                makeOptions.add(R.drawable.msg_share, LocaleController.getString(R.string.StickersShare), new Runnable() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$7(stickersSet);
                    }
                });
                makeOptions.add(R.drawable.msg_delete, (CharSequence) LocaleController.getString(R.string.StickersRemove), true, new Runnable() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$8(stickersSet);
                    }
                });
            }
            makeOptions.setMinWidth(NotificationCenter.storiesSendAsUpdate);
            makeOptions.show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSelectionMenu$0(ArrayList arrayList, int i, DialogInterface dialogInterface, int i2) {
            StickersActivity.this.listAdapter.clearSelected();
            MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSets(arrayList, StickersActivity.this.currentType, i == 1 ? 0 : 1, StickersActivity.this, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ int lambda$swapElements$10(TLRPC.TL_messages_stickerSet tL_messages_stickerSet, TLRPC.TL_messages_stickerSet tL_messages_stickerSet2) {
            int indexOf = this.stickerSets.indexOf(tL_messages_stickerSet);
            int indexOf2 = this.stickerSets.indexOf(tL_messages_stickerSet2);
            if (indexOf < 0 || indexOf2 < 0) {
                return 0;
            }
            return indexOf - indexOf2;
        }

        private void notifyStickersItemsChanged(Object obj) {
            notifyItemRangeChanged(StickersActivity.this.stickersStartRow, StickersActivity.this.stickersEndRow - StickersActivity.this.stickersStartRow, obj);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processSelectionMenu(final int i) {
            int i2;
            TextView textView;
            int i3 = 0;
            if (i == 2) {
                StringBuilder sb = new StringBuilder();
                int size = this.stickerSets.size();
                while (i3 < size) {
                    TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.stickerSets.get(i3);
                    if (((Boolean) this.selectedItems.get(tL_messages_stickerSet.set.id, Boolean.FALSE)).booleanValue()) {
                        if (sb.length() != 0) {
                            sb.append("\n");
                        }
                        sb.append(StickersActivity.this.getLinkForSet(tL_messages_stickerSet));
                    }
                    i3++;
                }
                String sb2 = sb.toString();
                ShareAlert createShareAlert = ShareAlert.createShareAlert(StickersActivity.this.fragmentView.getContext(), null, sb2, false, sb2, false);
                createShareAlert.setDelegate(new ShareAlert.ShareAlertDelegate() { // from class: org.telegram.ui.StickersActivity.ListAdapter.1
                    @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                    public boolean didCopy() {
                        ListAdapter.this.clearSelected();
                        return true;
                    }

                    @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                    public void didShare() {
                        ListAdapter.this.clearSelected();
                    }
                });
                createShareAlert.show();
                return;
            }
            if (i == 0 || i == 1) {
                final ArrayList arrayList = new ArrayList(this.selectedItems.size());
                int size2 = this.stickerSets.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    TLRPC.StickerSet stickerSet = ((TLRPC.TL_messages_stickerSet) this.stickerSets.get(i4)).set;
                    if (((Boolean) this.selectedItems.get(stickerSet.id, Boolean.FALSE)).booleanValue()) {
                        arrayList.add(stickerSet);
                    }
                }
                int size3 = arrayList.size();
                if (size3 != 0) {
                    if (size3 == 1) {
                        int size4 = this.stickerSets.size();
                        while (true) {
                            if (i3 >= size4) {
                                break;
                            }
                            TLRPC.TL_messages_stickerSet tL_messages_stickerSet2 = (TLRPC.TL_messages_stickerSet) this.stickerSets.get(i3);
                            if (((Boolean) this.selectedItems.get(tL_messages_stickerSet2.set.id, Boolean.FALSE)).booleanValue()) {
                                processSelectionOption(i, tL_messages_stickerSet2);
                                break;
                            }
                            i3++;
                        }
                        StickersActivity.this.listAdapter.clearSelected();
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(StickersActivity.this.getParentActivity());
                    if (i == 1) {
                        builder.setTitle(LocaleController.formatString("DeleteStickerSetsAlertTitle", R.string.DeleteStickerSetsAlertTitle, LocaleController.formatPluralString("StickerSets", size3, new Object[0])));
                        builder.setMessage(LocaleController.formatString("DeleteStickersAlertMessage", R.string.DeleteStickersAlertMessage, Integer.valueOf(size3)));
                        i2 = R.string.Delete;
                    } else {
                        builder.setTitle(LocaleController.formatString("ArchiveStickerSetsAlertTitle", R.string.ArchiveStickerSetsAlertTitle, LocaleController.formatPluralString("StickerSets", size3, new Object[0])));
                        builder.setMessage(LocaleController.formatString("ArchiveStickersAlertMessage", R.string.ArchiveStickersAlertMessage, Integer.valueOf(size3)));
                        i2 = R.string.Archive;
                    }
                    builder.setPositiveButton(LocaleController.getString(i2), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i5) {
                            StickersActivity.ListAdapter.this.lambda$processSelectionMenu$0(arrayList, i, dialogInterface, i5);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                    AlertDialog create = builder.create();
                    StickersActivity.this.showDialog(create);
                    if (i != 1 || (textView = (TextView) create.getButton(-1)) == null) {
                        return;
                    }
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                }
            }
        }

        private void processSelectionOption(int i, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
            int indexOf;
            if (i == 0) {
                MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), tL_messages_stickerSet, !tL_messages_stickerSet.set.archived ? 1 : 2, StickersActivity.this, true, true);
                return;
            }
            if (i == 1) {
                MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), tL_messages_stickerSet, 0, StickersActivity.this, true, true);
                return;
            }
            try {
                if (i == 2) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", StickersActivity.this.getLinkForSet(tL_messages_stickerSet));
                    StickersActivity.this.getParentActivity().startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.StickersShare)), 500);
                } else {
                    if (i != 3) {
                        if (i != 4 || (indexOf = this.stickerSets.indexOf(tL_messages_stickerSet)) < 0) {
                            return;
                        }
                        StickersActivity.this.listAdapter.toggleSelected(StickersActivity.this.stickersStartRow + indexOf);
                        return;
                    }
                    Locale locale = Locale.US;
                    StringBuilder sb = new StringBuilder();
                    sb.append("https://");
                    sb.append(MessagesController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).linkPrefix);
                    sb.append("/");
                    sb.append(tL_messages_stickerSet.set.emojis ? "addemoji" : "addstickers");
                    sb.append("/%s");
                    ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", String.format(locale, sb.toString(), tL_messages_stickerSet.set.short_name)));
                    BulletinFactory.createCopyLinkBulletin(StickersActivity.this).show();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        private void swapListElements(List list, int i, int i2) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) list.get(i);
            list.set(i, (TLRPC.TL_messages_stickerSet) list.get(i2));
            list.set(i2, tL_messages_stickerSet);
        }

        public void clearSelected() {
            this.selectedItems.clear();
            notifyStickersItemsChanged(1);
            checkActionMode();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return StickersActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            TLRPC.StickerSet stickerSet;
            if (i >= StickersActivity.this.featuredStickersStartRow && i < StickersActivity.this.featuredStickersEndRow) {
                stickerSet = ((TLRPC.StickerSetCovered) this.featuredStickerSets.get(i - StickersActivity.this.featuredStickersStartRow)).set;
            } else {
                if (i < StickersActivity.this.stickersStartRow || i >= StickersActivity.this.stickersEndRow) {
                    return i;
                }
                stickerSet = ((TLRPC.TL_messages_stickerSet) this.stickerSets.get(i - StickersActivity.this.stickersStartRow)).set;
            }
            return stickerSet.id;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i >= StickersActivity.this.featuredStickersStartRow && i < StickersActivity.this.featuredStickersEndRow) {
                return 7;
            }
            if (i >= StickersActivity.this.stickersStartRow && i < StickersActivity.this.stickersEndRow) {
                return 0;
            }
            if (i == StickersActivity.this.stickersBotInfo || i == StickersActivity.this.archivedInfoRow || i == StickersActivity.this.loopInfoRow || i == StickersActivity.this.suggestAnimatedEmojiInfoRow || i == StickersActivity.this.masksInfoRow || i == StickersActivity.this.dynamicPackOrderInfo) {
                return 1;
            }
            if (i == StickersActivity.this.archivedRow || i == StickersActivity.this.masksRow || i == StickersActivity.this.featuredRow || i == StickersActivity.this.emojiPacksRow || i == StickersActivity.this.suggestRow || i == StickersActivity.this.featuredStickersShowMoreRow) {
                return 2;
            }
            if (i == StickersActivity.this.stickersShadowRow || i == StickersActivity.this.featuredStickersShadowRow) {
                return 3;
            }
            if (i == StickersActivity.this.loopRow || i == StickersActivity.this.largeEmojiRow || i == StickersActivity.this.suggestAnimatedEmojiRow || i == StickersActivity.this.dynamicPackOrder) {
                return 4;
            }
            if (i == StickersActivity.this.reactionsDoubleTapRow) {
                return 5;
            }
            return (i == StickersActivity.this.featuredStickersHeaderRow || i == StickersActivity.this.stickersHeaderRow || i == StickersActivity.this.stickersSettingsRow) ? 6 : 0;
        }

        public int getSelectedCount() {
            int size = this.selectedItems.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                if (((Boolean) this.selectedItems.valueAt(i2)).booleanValue()) {
                    i++;
                }
            }
            return i;
        }

        public boolean hasSelected() {
            return this.selectedItems.indexOfValue(Boolean.TRUE) != -1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 7 || itemViewType == 2 || itemViewType == 4 || itemViewType == 5;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (StickersActivity.this.isListeningForFeaturedUpdate) {
                StickersActivity.this.isListeningForFeaturedUpdate = false;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:184:0x042c  */
        /* JADX WARN: Removed duplicated region for block: B:190:0x0437  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC.StickerSet stickerSet;
            CharSequence string;
            int i2;
            String string2;
            String num;
            int i3;
            String string3;
            int i4;
            String string4;
            boolean z;
            String string5;
            boolean z2;
            int i5;
            int i6 = 2;
            boolean z3 = false;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    StickerSetCell stickerSetCell = (StickerSetCell) viewHolder.itemView;
                    int i7 = i - StickersActivity.this.stickersStartRow;
                    TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.stickerSets.get(i7);
                    boolean z4 = (stickerSetCell.getStickersSet() == null && tL_messages_stickerSet == null) || !(stickerSetCell.getStickersSet() == null || tL_messages_stickerSet == null || stickerSetCell.getStickersSet().set.id != tL_messages_stickerSet.set.id);
                    stickerSetCell.setStickersSet(tL_messages_stickerSet, i7 != this.stickerSets.size() - 1);
                    stickerSetCell.setChecked(((Boolean) this.selectedItems.get(getItemId(i), Boolean.FALSE)).booleanValue(), false);
                    stickerSetCell.setReorderable(hasSelected(), false);
                    if (tL_messages_stickerSet == null || (stickerSet = tL_messages_stickerSet.set) == null || !stickerSet.emojis) {
                        return;
                    }
                    boolean isStickerPackInstalled = StickersActivity.this.getMediaDataController().isStickerPackInstalled(tL_messages_stickerSet.set.id);
                    boolean z5 = !UserConfig.getInstance(((BaseFragment) StickersActivity.this).currentAccount).isPremium();
                    if (z5) {
                        for (int i8 = 0; i8 < tL_messages_stickerSet.documents.size(); i8++) {
                            if (MessageObject.isFreeEmoji(tL_messages_stickerSet.documents.get(i8))) {
                            }
                        }
                        if (z3) {
                            i6 = isStickerPackInstalled ? 4 : 3;
                        } else if (!isStickerPackInstalled || tL_messages_stickerSet.set.official) {
                            i6 = 1;
                        }
                        stickerSetCell.updateButtonState(i6, z4);
                        return;
                    }
                    z3 = z5;
                    if (z3) {
                    }
                    stickerSetCell.updateButtonState(i6, z4);
                    return;
                case 1:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    textInfoPrivacyCell.setFixedSize(0);
                    if (i == StickersActivity.this.stickersBotInfo) {
                        string = addStickersBotSpan(LocaleController.getString(StickersActivity.this.currentType == 5 ? R.string.EmojiBotInfo : R.string.StickersBotInfo));
                    } else {
                        if (i == StickersActivity.this.archivedInfoRow) {
                            i2 = StickersActivity.this.currentType == 0 ? R.string.ArchivedStickersInfo : R.string.ArchivedMasksInfo;
                        } else if (i == StickersActivity.this.loopInfoRow) {
                            textInfoPrivacyCell.setText(null);
                            textInfoPrivacyCell.setFixedSize(12);
                            return;
                        } else if (i == StickersActivity.this.suggestAnimatedEmojiInfoRow) {
                            i2 = R.string.SuggestAnimatedEmojiInfo;
                        } else if (i == StickersActivity.this.masksInfoRow) {
                            i2 = R.string.MasksInfo;
                        } else if (i != StickersActivity.this.dynamicPackOrderInfo) {
                            return;
                        } else {
                            string = LocaleController.getString("DynamicPackOrderInfo");
                        }
                        string = LocaleController.getString(i2);
                    }
                    textInfoPrivacyCell.setText(string);
                    return;
                case 2:
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    if (i == StickersActivity.this.featuredStickersShowMoreRow) {
                        int i9 = Theme.key_windowBackgroundWhiteBlueText4;
                        textCell.setColors(i9, i9);
                        textCell.setTextAndIcon((CharSequence) LocaleController.getString(StickersActivity.this.currentType == 5 ? R.string.ShowMoreEmojiPacks : R.string.ShowMoreStickers), R.drawable.msg2_trending, false);
                        return;
                    }
                    textCell.imageView.setTranslationX(0.0f);
                    textCell.textView.setTranslationX(0.0f);
                    textCell.setColors(Theme.key_windowBackgroundWhiteGrayIcon, Theme.key_windowBackgroundWhiteBlackText);
                    if (i == StickersActivity.this.archivedRow) {
                        int archivedStickersCount = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).getArchivedStickersCount(StickersActivity.this.currentType);
                        num = archivedStickersCount > 0 ? Integer.toString(archivedStickersCount) : "";
                        if (StickersActivity.this.currentType != 0) {
                            textCell.setTextAndValue(LocaleController.getString(StickersActivity.this.currentType == 5 ? R.string.ArchivedEmojiPacks : R.string.ArchivedMasks), num, false, true);
                            return;
                        } else {
                            string3 = LocaleController.getString(R.string.ArchivedStickers);
                            i4 = R.drawable.msg2_archived_stickers;
                        }
                    } else {
                        if (i != StickersActivity.this.masksRow) {
                            if (i == StickersActivity.this.featuredRow) {
                                List featuredSets = StickersActivity.this.getFeaturedSets();
                                string2 = LocaleController.getString(R.string.FeaturedStickers);
                                num = featuredSets != null ? "" + featuredSets.size() : "";
                                i3 = R.drawable.msg2_trending;
                            } else {
                                if (i != StickersActivity.this.emojiPacksRow) {
                                    if (i == StickersActivity.this.suggestRow) {
                                        int i10 = SharedConfig.suggestStickers;
                                        String string6 = LocaleController.getString(i10 != 0 ? i10 != 1 ? R.string.SuggestStickersNone : R.string.SuggestStickersInstalled : R.string.SuggestStickersAll);
                                        if (!LocaleController.isRTL) {
                                            textCell.textView.setTranslationX(AndroidUtilities.dp(-2.0f));
                                        }
                                        textCell.setTextAndValue(LocaleController.getString(R.string.SuggestStickers), string6, StickersActivity.this.updateSuggestStickers, true);
                                        StickersActivity.this.updateSuggestStickers = false;
                                        return;
                                    }
                                    return;
                                }
                                int size = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).getStickerSets(5).size();
                                textCell.imageView.setTranslationX(-AndroidUtilities.dp(2.0f));
                                string2 = LocaleController.getString(R.string.Emoji);
                                num = size > 0 ? Integer.toString(size) : "";
                                i3 = R.drawable.msg2_smile_status;
                            }
                            textCell.setTextAndValueAndIcon((CharSequence) string2, (CharSequence) num, i3, true);
                            return;
                        }
                        MediaDataController mediaDataController = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount);
                        int size2 = MessagesController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).filterPremiumStickers(mediaDataController.getStickerSets(1)).size() + mediaDataController.getArchivedStickersCount(1);
                        string3 = LocaleController.getString(R.string.Masks);
                        num = size2 > 0 ? Integer.toString(size2) : "";
                        i4 = R.drawable.msg_mask;
                    }
                    textCell.setTextAndValueAndIcon((CharSequence) string3, (CharSequence) num, i4, true);
                    return;
                case 3:
                    if (i == StickersActivity.this.stickersShadowRow) {
                        viewHolder.itemView.setBackground(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        return;
                    }
                    return;
                case 4:
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                    if (i == StickersActivity.this.loopRow) {
                        string5 = LocaleController.getString(R.string.LoopAnimatedStickers);
                        z2 = SharedConfig.loopStickers();
                    } else {
                        if (i != StickersActivity.this.largeEmojiRow) {
                            if (i == StickersActivity.this.suggestAnimatedEmojiRow) {
                                string4 = LocaleController.getString(R.string.SuggestAnimatedEmoji);
                                z = SharedConfig.suggestAnimatedEmoji;
                            } else {
                                if (i != StickersActivity.this.dynamicPackOrder) {
                                    return;
                                }
                                string4 = LocaleController.getString("DynamicPackOrder");
                                z = SharedConfig.updateStickersOrderOnSend;
                            }
                            textCheckCell.setTextAndCheck(string4, z, false);
                            return;
                        }
                        string5 = LocaleController.getString(R.string.LargeEmoji);
                        z2 = SharedConfig.allowBigEmoji;
                    }
                    textCheckCell.setTextAndCheck(string5, z2, true);
                    return;
                case 5:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                    textSettingsCell.setText(LocaleController.getString(R.string.DoubleTapSetting), false);
                    textSettingsCell.setIcon(R.drawable.msg2_reactions2);
                    String doubleTapReaction = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).getDoubleTapReaction();
                    if (doubleTapReaction != null) {
                        if (doubleTapReaction.startsWith("animated_")) {
                            try {
                                AnimatedEmojiDrawable make = AnimatedEmojiDrawable.make(((BaseFragment) StickersActivity.this).currentAccount, 2, Long.parseLong(doubleTapReaction.substring(9)));
                                make.addView(textSettingsCell.getValueBackupImageView());
                                textSettingsCell.getValueBackupImageView().setImageDrawable(make);
                                return;
                            } catch (Exception unused) {
                                return;
                            }
                        }
                        TLRPC.TL_availableReaction tL_availableReaction = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).getReactionsMap().get(doubleTapReaction);
                        if (tL_availableReaction != null) {
                            textSettingsCell.getValueBackupImageView().getImageReceiver().setImage(ImageLocation.getForDocument(tL_availableReaction.center_icon), "100_100_lastreactframe", DocumentObject.getSvgThumb(tL_availableReaction.static_icon.thumbs, Theme.key_windowBackgroundGray, 1.0f), "webp", tL_availableReaction, 1);
                            return;
                        }
                        return;
                    }
                    return;
                case 6:
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    if (i == StickersActivity.this.featuredStickersHeaderRow) {
                        i5 = StickersActivity.this.currentType == 5 ? R.string.FeaturedEmojiPacks : R.string.FeaturedStickers;
                    } else if (i == StickersActivity.this.stickersHeaderRow) {
                        i5 = StickersActivity.this.currentType == 5 ? R.string.ChooseStickerMyEmojiPacks : R.string.ChooseStickerMyStickerSets;
                    } else if (i != StickersActivity.this.stickersSettingsRow) {
                        return;
                    } else {
                        i5 = R.string.StickersSettings;
                    }
                    headerCell.setText(LocaleController.getString(i5));
                    return;
                case 7:
                    FeaturedStickerSetCell2 featuredStickerSetCell2 = (FeaturedStickerSetCell2) viewHolder.itemView;
                    TLRPC.StickerSetCovered stickerSetCovered = (TLRPC.StickerSetCovered) this.featuredStickerSets.get(i - StickersActivity.this.featuredStickersStartRow);
                    if (StickersActivity.this.isListeningForFeaturedUpdate || (featuredStickerSetCell2.getStickerSet() != null && featuredStickerSetCell2.getStickerSet().set.id == stickerSetCovered.set.id)) {
                        z3 = true;
                    }
                    featuredStickerSetCell2.setStickersSet(stickerSetCovered, true, false, false, z3);
                    featuredStickerSetCell2.setDrawProgress(this.loadingFeaturedStickerSets.contains(Long.valueOf(stickerSetCovered.set.id)), z3);
                    featuredStickerSetCell2.setAddOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda1
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

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List list) {
            if (list.isEmpty()) {
                onBindViewHolder(viewHolder, i);
                return;
            }
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                if (i < StickersActivity.this.stickersStartRow || i >= StickersActivity.this.stickersEndRow) {
                    return;
                }
                StickerSetCell stickerSetCell = (StickerSetCell) viewHolder.itemView;
                if (list.contains(1)) {
                    stickerSetCell.setChecked(((Boolean) this.selectedItems.get(getItemId(i), Boolean.FALSE)).booleanValue());
                }
                if (list.contains(2)) {
                    stickerSetCell.setReorderable(hasSelected());
                }
                if (list.contains(3)) {
                    stickerSetCell.setNeedDivider(i - StickersActivity.this.stickersStartRow != this.stickerSets.size() - 1);
                    return;
                }
                return;
            }
            if (itemViewType == 4) {
                if (list.contains(0) && i == StickersActivity.this.loopRow) {
                    ((TextCheckCell) viewHolder.itemView).setChecked(SharedConfig.loopStickers());
                    return;
                }
                return;
            }
            if (itemViewType == 7 && list.contains(4) && i >= StickersActivity.this.featuredStickersStartRow && i <= StickersActivity.this.featuredStickersEndRow) {
                ((FeaturedStickerSetCell2) viewHolder.itemView).setStickersSet((TLRPC.StickerSetCovered) this.featuredStickerSets.get(i - StickersActivity.this.featuredStickersStartRow), true, false, false, true);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            View view2;
            int i2 = 1;
            if (i == 0) {
                final StickerSetCell stickerSetCell = new StickerSetCell(this.mContext, i2) { // from class: org.telegram.ui.StickersActivity.ListAdapter.3
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r1v5 */
                    /* JADX WARN: Type inference failed for: r1v6, types: [org.telegram.tgnet.TLRPC$StickerSetCovered] */
                    /* JADX WARN: Type inference failed for: r1v9 */
                    @Override // org.telegram.ui.Cells.StickerSetCell
                    protected void onAddButtonClick() {
                        ?? r1;
                        TLRPC.TL_messages_stickerSet stickersSet = getStickersSet();
                        if (stickersSet == null || stickersSet.set == null) {
                            return;
                        }
                        ArrayList<TLRPC.StickerSetCovered> featuredEmojiSets = StickersActivity.this.getMediaDataController().getFeaturedEmojiSets();
                        int i3 = 0;
                        while (true) {
                            if (i3 >= featuredEmojiSets.size()) {
                                r1 = 0;
                                break;
                            } else {
                                if (stickersSet.set.id == featuredEmojiSets.get(i3).set.id) {
                                    r1 = featuredEmojiSets.get(i3);
                                    break;
                                }
                                i3++;
                            }
                        }
                        if (r1 != 0) {
                            if (ListAdapter.this.loadingFeaturedStickerSets.contains(Long.valueOf(r1.set.id))) {
                                return;
                            } else {
                                ListAdapter.this.loadingFeaturedStickerSets.add(Long.valueOf(r1.set.id));
                            }
                        }
                        MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), r1 == 0 ? stickersSet : r1, 2, StickersActivity.this, false, false);
                    }

                    @Override // org.telegram.ui.Cells.StickerSetCell
                    protected void onPremiumButtonClick() {
                        StickersActivity.this.showDialog(new PremiumFeatureBottomSheet(StickersActivity.this, 11, false));
                    }

                    @Override // org.telegram.ui.Cells.StickerSetCell
                    protected void onRemoveButtonClick() {
                        MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), getStickersSet(), 0, StickersActivity.this, false, true);
                    }
                };
                stickerSetCell.setBackgroundColor(StickersActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                stickerSetCell.setOnReorderButtonTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view3, MotionEvent motionEvent) {
                        boolean lambda$onCreateViewHolder$2;
                        lambda$onCreateViewHolder$2 = StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$2(stickerSetCell, view3, motionEvent);
                        return lambda$onCreateViewHolder$2;
                    }
                });
                stickerSetCell.setOnOptionsClick(new View.OnClickListener() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        StickersActivity.ListAdapter.this.lambda$onCreateViewHolder$9(view3);
                    }
                });
                view = stickerSetCell;
            } else if (i != 1) {
                if (i == 2) {
                    view2 = new TextCell(this.mContext);
                } else if (i == 3) {
                    view = new ShadowSectionCell(this.mContext);
                } else if (i == 5) {
                    view2 = new TextSettingsCell(this.mContext);
                } else if (i == 6) {
                    view2 = new HeaderCell(this.mContext);
                } else if (i != 7) {
                    view2 = new TextCheckCell(this.mContext);
                } else {
                    FeaturedStickerSetCell2 featuredStickerSetCell2 = new FeaturedStickerSetCell2(this.mContext, StickersActivity.this.getResourceProvider()) { // from class: org.telegram.ui.StickersActivity.ListAdapter.2
                        @Override // org.telegram.ui.Cells.FeaturedStickerSetCell2
                        protected void onPremiumButtonClick() {
                            StickersActivity.this.showDialog(new PremiumFeatureBottomSheet(StickersActivity.this, 11, false));
                        }
                    };
                    featuredStickerSetCell2.setBackgroundColor(StickersActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                    featuredStickerSetCell2.getTextView().setTypeface(AndroidUtilities.bold());
                    view = featuredStickerSetCell2;
                }
                view2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                view = view2;
            } else {
                View textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                view = textInfoPrivacyCell;
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
        }

        public void setFeaturedStickerSets(List list) {
            this.featuredStickerSets.clear();
            this.featuredStickerSets.addAll(list);
        }

        public void setStickerSets(List list) {
            this.stickerSets.clear();
            this.stickerSets.addAll(list);
        }

        public void swapElements(int i, int i2) {
            if (i != i2) {
                StickersActivity.this.needReorder = true;
            }
            MediaDataController mediaDataController = MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount);
            swapListElements(this.stickerSets, i - StickersActivity.this.stickersStartRow, i2 - StickersActivity.this.stickersStartRow);
            Collections.sort(mediaDataController.getStickerSets(StickersActivity.this.currentType), new Comparator() { // from class: org.telegram.ui.StickersActivity$ListAdapter$$ExternalSyntheticLambda4
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$swapElements$10;
                    lambda$swapElements$10 = StickersActivity.ListAdapter.this.lambda$swapElements$10((TLRPC.TL_messages_stickerSet) obj, (TLRPC.TL_messages_stickerSet) obj2);
                    return lambda$swapElements$10;
                }
            });
            notifyItemMoved(i, i2);
            if (i == StickersActivity.this.stickersEndRow - 1 || i2 == StickersActivity.this.stickersEndRow - 1) {
                notifyItemRangeChanged(i, 3);
                notifyItemRangeChanged(i2, 3);
            }
        }

        public void toggleSelected(int i) {
            long itemId = getItemId(i);
            this.selectedItems.put(itemId, Boolean.valueOf(!((Boolean) r2.get(itemId, Boolean.FALSE)).booleanValue()));
            notifyItemChanged(i, 1);
            checkActionMode();
        }
    }

    /* loaded from: classes4.dex */
    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        public TouchHelperCallback() {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 0 ? ItemTouchHelper.Callback.makeMovementFlags(0, 0) : ItemTouchHelper.Callback.makeMovementFlags(3, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return StickersActivity.this.listAdapter.hasSelected();
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
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
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            StickersActivity stickersActivity = StickersActivity.this;
            if (i == 0) {
                stickersActivity.sendReorder();
            } else {
                stickersActivity.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }
    }

    public StickersActivity(int i, ArrayList arrayList) {
        this.currentType = i;
        this.frozenEmojiPacks = arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List getFeaturedSets() {
        MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
        if (this.currentType != 5) {
            return mediaDataController.getFeaturedStickerSets();
        }
        ArrayList arrayList = new ArrayList(mediaDataController.getFeaturedEmojiSets());
        int i = 0;
        while (i < arrayList.size()) {
            if (arrayList.get(i) == null || mediaDataController.isStickerPackInstalled(((TLRPC.StickerSetCovered) arrayList.get(i)).set.id, false)) {
                arrayList.remove(i);
                i--;
            }
            i++;
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLinkForSet(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        Locale locale = Locale.US;
        StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
        sb.append("/");
        sb.append(tL_messages_stickerSet.set.emojis ? "addemoji" : "addstickers");
        sb.append("/%s");
        return String.format(locale, sb.toString(), tL_messages_stickerSet.set.short_name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$0(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(AlertDialog.Builder builder, View view) {
        SharedConfig.setSuggestStickers(((Integer) view.getTag()).intValue());
        this.updateSuggestStickers = true;
        this.listAdapter.notifyItemChanged(this.suggestRow);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(Context context, View view, int i) {
        EmojiPacksAlert emojiPacksAlert;
        TextCheckCell textCheckCell;
        boolean z;
        BaseFragment reactionsDoubleTapManageActivity;
        Dialog create;
        if (i >= this.featuredStickersStartRow && i < this.featuredStickersEndRow && getParentActivity() != null) {
            TLRPC.StickerSetCovered stickerSetCovered = (TLRPC.StickerSetCovered) this.listAdapter.featuredStickerSets.get(i - this.featuredStickersStartRow);
            TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
            TLRPC.StickerSet stickerSet = stickerSetCovered.set;
            tL_inputStickerSetID.id = stickerSet.id;
            tL_inputStickerSetID.access_hash = stickerSet.access_hash;
            if (this.currentType != 5) {
                create = new StickersAlert(getParentActivity(), this, tL_inputStickerSetID, null, null, false);
                showDialog(create);
                return;
            } else {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(tL_inputStickerSetID);
                emojiPacksAlert = new EmojiPacksAlert(this, getParentActivity(), getResourceProvider(), arrayList);
                showDialog(emojiPacksAlert);
            }
        }
        if (i != this.featuredStickersShowMoreRow && i != this.featuredRow) {
            if (i < this.stickersStartRow || i >= this.stickersEndRow || getParentActivity() == null) {
                if (i == this.archivedRow) {
                    reactionsDoubleTapManageActivity = new ArchivedStickersActivity(this.currentType);
                } else if (i == this.masksRow) {
                    reactionsDoubleTapManageActivity = new StickersActivity(1, null);
                } else if (i == this.emojiPacksRow) {
                    reactionsDoubleTapManageActivity = new StickersActivity(5, null);
                } else {
                    if (i != this.suggestRow) {
                        if (i == this.loopRow) {
                            SharedConfig.toggleLoopStickers();
                            this.listAdapter.notifyItemChanged(this.loopRow, 0);
                            return;
                        }
                        if (i == this.largeEmojiRow) {
                            SharedConfig.toggleBigEmoji();
                            textCheckCell = (TextCheckCell) view;
                            z = SharedConfig.allowBigEmoji;
                        } else if (i == this.suggestAnimatedEmojiRow) {
                            SharedConfig.toggleSuggestAnimatedEmoji();
                            textCheckCell = (TextCheckCell) view;
                            z = SharedConfig.suggestAnimatedEmoji;
                        } else if (i == this.reactionsDoubleTapRow) {
                            reactionsDoubleTapManageActivity = new ReactionsDoubleTapManageActivity();
                        } else {
                            if (i != this.dynamicPackOrder) {
                                return;
                            }
                            SharedConfig.toggleUpdateStickersOrderOnSend();
                            textCheckCell = (TextCheckCell) view;
                            z = SharedConfig.updateStickersOrderOnSend;
                        }
                        textCheckCell.setChecked(z);
                        return;
                    }
                    if (getParentActivity() == null) {
                        return;
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setTitle(LocaleController.getString(R.string.SuggestStickers));
                    String[] strArr = {LocaleController.getString(R.string.SuggestStickersAll), LocaleController.getString(R.string.SuggestStickersInstalled), LocaleController.getString(R.string.SuggestStickersNone)};
                    LinearLayout linearLayout = new LinearLayout(getParentActivity());
                    linearLayout.setOrientation(1);
                    builder.setView(linearLayout);
                    int i2 = 0;
                    while (i2 < 3) {
                        RadioColorCell radioColorCell = new RadioColorCell(getParentActivity());
                        radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                        radioColorCell.setTag(Integer.valueOf(i2));
                        radioColorCell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                        radioColorCell.setTextAndValue(strArr[i2], SharedConfig.suggestStickers == i2);
                        radioColorCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                        linearLayout.addView(radioColorCell);
                        radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda4
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                StickersActivity.this.lambda$createView$1(builder, view2);
                            }
                        });
                        i2++;
                    }
                    create = builder.create();
                }
                presentFragment(reactionsDoubleTapManageActivity);
                return;
            }
            if (this.listAdapter.hasSelected()) {
                this.listAdapter.toggleSelected(i);
                return;
            }
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.listAdapter.stickerSets.get(i - this.stickersStartRow);
            ArrayList<TLRPC.Document> arrayList2 = tL_messages_stickerSet.documents;
            if (arrayList2 == null || arrayList2.isEmpty()) {
                return;
            }
            TLRPC.StickerSet stickerSet2 = tL_messages_stickerSet.set;
            if (stickerSet2 == null || !stickerSet2.emojis) {
                create = new StickersAlert(getParentActivity(), this, null, tL_messages_stickerSet, null, false);
            } else {
                ArrayList arrayList3 = new ArrayList();
                TLRPC.TL_inputStickerSetID tL_inputStickerSetID2 = new TLRPC.TL_inputStickerSetID();
                TLRPC.StickerSet stickerSet3 = tL_messages_stickerSet.set;
                tL_inputStickerSetID2.id = stickerSet3.id;
                tL_inputStickerSetID2.access_hash = stickerSet3.access_hash;
                arrayList3.add(tL_inputStickerSetID2);
                emojiPacksAlert = new EmojiPacksAlert(this, getParentActivity(), getResourceProvider(), arrayList3);
            }
            showDialog(create);
            return;
        }
        if (this.currentType != 5) {
            TrendingStickersAlert trendingStickersAlert = new TrendingStickersAlert(context, this, new TrendingStickersLayout(context, new TrendingStickersLayout.Delegate() { // from class: org.telegram.ui.StickersActivity.5
                @Override // org.telegram.ui.Components.TrendingStickersLayout.Delegate
                public void onStickerSetAdd(TLRPC.StickerSetCovered stickerSetCovered2, boolean z2) {
                    MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), stickerSetCovered2, 2, StickersActivity.this, false, false);
                }

                @Override // org.telegram.ui.Components.TrendingStickersLayout.Delegate
                public void onStickerSetRemove(TLRPC.StickerSetCovered stickerSetCovered2) {
                    MediaDataController.getInstance(((BaseFragment) StickersActivity.this).currentAccount).toggleStickerSet(StickersActivity.this.getParentActivity(), stickerSetCovered2, 0, StickersActivity.this, false, false);
                }
            }), null);
            this.trendingStickersAlert = trendingStickersAlert;
            trendingStickersAlert.show();
            return;
        }
        ArrayList arrayList4 = new ArrayList();
        List featuredSets = getFeaturedSets();
        if (featuredSets != null) {
            for (int i3 = 0; i3 < featuredSets.size(); i3++) {
                TLRPC.StickerSetCovered stickerSetCovered2 = (TLRPC.StickerSetCovered) featuredSets.get(i3);
                if (stickerSetCovered2 != null && stickerSetCovered2.set != null) {
                    TLRPC.TL_inputStickerSetID tL_inputStickerSetID3 = new TLRPC.TL_inputStickerSetID();
                    TLRPC.StickerSet stickerSet4 = stickerSetCovered2.set;
                    tL_inputStickerSetID3.id = stickerSet4.id;
                    tL_inputStickerSetID3.access_hash = stickerSet4.access_hash;
                    arrayList4.add(tL_inputStickerSetID3);
                }
            }
        }
        MediaDataController.getInstance(this.currentAccount).markFeaturedStickersAsRead(true, true);
        emojiPacksAlert = new EmojiPacksAlert(this, getParentActivity(), getResourceProvider(), arrayList4);
        showDialog(emojiPacksAlert);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$3(View view, int i) {
        if (this.listAdapter.hasSelected() || i < this.stickersStartRow || i >= this.stickersEndRow) {
            return false;
        }
        this.listAdapter.toggleSelected(i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendReorder$4() {
        this.activeReorderingRequests--;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendReorder$5(TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                StickersActivity.this.lambda$sendReorder$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendReorder() {
        if (this.needReorder) {
            MediaDataController.getInstance(this.currentAccount).calcNewHash(this.currentType);
            this.needReorder = false;
            this.activeReorderingRequests++;
            TLRPC.TL_messages_reorderStickerSets tL_messages_reorderStickerSets = new TLRPC.TL_messages_reorderStickerSets();
            int i = this.currentType;
            tL_messages_reorderStickerSets.masks = i == 1;
            tL_messages_reorderStickerSets.emojis = i == 5;
            for (int i2 = 0; i2 < this.listAdapter.stickerSets.size(); i2++) {
                tL_messages_reorderStickerSets.order.add(Long.valueOf(((TLRPC.TL_messages_stickerSet) this.listAdapter.stickerSets.get(i2)).set.id));
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_reorderStickerSets, new RequestDelegate() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    StickersActivity.this.lambda$sendReorder$5(tLObject, tL_error);
                }
            });
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(this.currentType), Boolean.TRUE);
            if (!SharedConfig.updateStickersOrderOnSend || this.dynamicPackOrder == -1) {
                return;
            }
            SharedConfig.toggleUpdateStickersOrderOnSend();
            BulletinFactory.of(this).createSimpleBulletin(R.raw.filter_reorder, LocaleController.getString(R.string.DynamicPackOrderOff), LocaleController.getString(R.string.DynamicPackOrderOffInfo)).show();
            for (int i3 = 0; i3 < this.listView.getChildCount(); i3++) {
                View childAt = this.listView.getChildAt(i3);
                if (this.listView.getChildAdapterPosition(childAt) == this.dynamicPackOrder && (childAt instanceof TextCheckCell)) {
                    ((TextCheckCell) childAt).setChecked(SharedConfig.updateStickersOrderOnSend);
                    return;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:103:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0200  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x021d  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0233  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateRows(boolean z) {
        final ArrayList arrayList;
        boolean z2;
        DiffUtil.DiffResult diffResult;
        ListAdapter listAdapter;
        MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
        if (this.currentType == 5) {
            if (z || this.frozenEmojiPacks == null) {
                this.frozenEmojiPacks = new ArrayList(MessagesController.getInstance(this.currentAccount).filterPremiumStickers(mediaDataController.getStickerSets(this.currentType)));
            }
            arrayList = this.frozenEmojiPacks;
        } else {
            arrayList = new ArrayList(MessagesController.getInstance(this.currentAccount).filterPremiumStickers(mediaDataController.getStickerSets(this.currentType)));
        }
        List featuredSets = getFeaturedSets();
        if (featuredSets.size() > 3) {
            featuredSets = featuredSets.subList(0, 3);
            z2 = true;
        } else {
            z2 = false;
        }
        final ArrayList arrayList2 = new ArrayList(featuredSets);
        if (this.currentType == 5) {
            boolean isPremium = UserConfig.getInstance(this.currentAccount).isPremium();
            if (!isPremium) {
                int i = 0;
                while (true) {
                    if (i >= arrayList.size()) {
                        break;
                    }
                    if (!MessageObject.isPremiumEmojiPack((TLRPC.TL_messages_stickerSet) arrayList.get(i))) {
                        isPremium = true;
                        break;
                    }
                    i++;
                }
            }
            if (!isPremium) {
                for (int i2 = 0; i2 < featuredSets.size() && MessageObject.isPremiumEmojiPack((TLRPC.StickerSetCovered) featuredSets.get(i2)); i2++) {
                }
            }
        }
        DiffUtil.DiffResult diffResult2 = null;
        if (this.listAdapter != null) {
            if (this.isPaused) {
                diffResult = null;
            } else {
                diffResult2 = DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: org.telegram.ui.StickersActivity.6
                    List oldList;

                    {
                        this.oldList = StickersActivity.this.listAdapter.stickerSets;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areContentsTheSame(int i3, int i4) {
                        TLRPC.StickerSet stickerSet = ((TLRPC.TL_messages_stickerSet) this.oldList.get(i3)).set;
                        TLRPC.StickerSet stickerSet2 = ((TLRPC.TL_messages_stickerSet) arrayList.get(i4)).set;
                        return TextUtils.equals(stickerSet.title, stickerSet2.title) && stickerSet.count == stickerSet2.count;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areItemsTheSame(int i3, int i4) {
                        return ((TLRPC.TL_messages_stickerSet) this.oldList.get(i3)).set.id == ((TLRPC.TL_messages_stickerSet) arrayList.get(i4)).set.id;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getNewListSize() {
                        return arrayList.size();
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getOldListSize() {
                        return this.oldList.size();
                    }
                });
                diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() { // from class: org.telegram.ui.StickersActivity.7
                    List oldList;

                    {
                        this.oldList = StickersActivity.this.listAdapter.featuredStickerSets;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areContentsTheSame(int i3, int i4) {
                        TLRPC.StickerSet stickerSet = ((TLRPC.StickerSetCovered) this.oldList.get(i3)).set;
                        TLRPC.StickerSet stickerSet2 = ((TLRPC.StickerSetCovered) arrayList2.get(i4)).set;
                        return TextUtils.equals(stickerSet.title, stickerSet2.title) && stickerSet.count == stickerSet2.count && stickerSet.installed == stickerSet2.installed;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public boolean areItemsTheSame(int i3, int i4) {
                        return ((TLRPC.StickerSetCovered) this.oldList.get(i3)).set.id == ((TLRPC.StickerSetCovered) arrayList2.get(i4)).set.id;
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getNewListSize() {
                        return arrayList2.size();
                    }

                    @Override // androidx.recyclerview.widget.DiffUtil.Callback
                    public int getOldListSize() {
                        return this.oldList.size();
                    }
                });
            }
            this.listAdapter.setStickerSets(arrayList);
            this.listAdapter.setFeaturedStickerSets(arrayList2);
        } else {
            diffResult = null;
        }
        this.rowCount = 0;
        this.archivedRow = -1;
        this.loopRow = -1;
        this.loopInfoRow = -1;
        int i3 = this.currentType;
        if (i3 == 0) {
            this.rowCount = 1;
            this.featuredRow = 0;
            this.masksRow = -1;
            if (mediaDataController.getArchivedStickersCount(i3) != 0) {
                boolean z3 = this.archivedRow == -1;
                int i4 = this.rowCount;
                this.rowCount = i4 + 1;
                this.archivedRow = i4;
                ListAdapter listAdapter2 = this.listAdapter;
                if (listAdapter2 != null && z3) {
                    listAdapter2.notifyItemRangeInserted(i4, 1);
                }
            }
            this.archivedInfoRow = -1;
            int i5 = this.rowCount;
            this.rowCount = i5 + 1;
            this.emojiPacksRow = i5;
        } else {
            this.featuredRow = -1;
            this.masksRow = -1;
            this.emojiPacksRow = -1;
            if (mediaDataController.getArchivedStickersCount(i3) != 0) {
                boolean z4 = this.archivedRow == -1;
                int i6 = this.rowCount;
                int i7 = i6 + 1;
                this.rowCount = i7;
                this.archivedRow = i6;
                if (this.currentType == 1) {
                    this.rowCount = i6 + 2;
                } else {
                    i7 = -1;
                }
                this.archivedInfoRow = i7;
                ListAdapter listAdapter3 = this.listAdapter;
                if (listAdapter3 != null && z4) {
                    listAdapter3.notifyItemRangeInserted(i6, i7 != -1 ? 2 : 1);
                }
            } else {
                int i8 = this.archivedRow;
                int i9 = this.archivedInfoRow;
                this.archivedRow = -1;
                this.archivedInfoRow = -1;
                ListAdapter listAdapter4 = this.listAdapter;
                if (listAdapter4 != null && i8 != -1) {
                    listAdapter4.notifyItemRangeRemoved(i8, i9 != -1 ? 2 : 1);
                }
            }
        }
        int i10 = this.currentType;
        if (i10 == 5) {
            int i11 = this.rowCount;
            this.suggestAnimatedEmojiRow = i11;
            this.rowCount = i11 + 2;
            this.suggestAnimatedEmojiInfoRow = i11 + 1;
        } else {
            this.suggestAnimatedEmojiRow = -1;
            this.suggestAnimatedEmojiInfoRow = -1;
        }
        if (i10 == 0) {
            int i12 = this.rowCount;
            this.rowCount = i12 + 1;
            this.reactionsDoubleTapRow = i12;
        } else {
            this.reactionsDoubleTapRow = -1;
        }
        this.stickersBotInfo = -1;
        if (i10 == 0) {
            int i13 = this.rowCount;
            this.rowCount = i13 + 1;
            this.stickersBotInfo = i13;
        }
        this.featuredStickersHeaderRow = -1;
        this.featuredStickersStartRow = -1;
        this.featuredStickersEndRow = -1;
        this.featuredStickersShowMoreRow = -1;
        this.featuredStickersShadowRow = -1;
        if (i10 == 0) {
            int i14 = this.rowCount;
            this.stickersSettingsRow = i14;
            this.suggestRow = i14 + 1;
            this.largeEmojiRow = i14 + 2;
            this.dynamicPackOrder = i14 + 3;
            this.rowCount = i14 + 5;
            this.dynamicPackOrderInfo = i14 + 4;
        } else {
            this.stickersSettingsRow = -1;
            this.suggestRow = -1;
            this.largeEmojiRow = -1;
            this.dynamicPackOrder = -1;
            this.dynamicPackOrderInfo = -1;
        }
        int size = arrayList.size();
        if (size > 0) {
            if (this.currentType == 5 || (!arrayList2.isEmpty() && this.currentType == 0)) {
                int i15 = this.rowCount;
                this.rowCount = i15 + 1;
                this.stickersHeaderRow = i15;
            } else {
                this.stickersHeaderRow = -1;
            }
            int i16 = this.rowCount;
            this.stickersStartRow = i16;
            int i17 = i16 + size;
            this.rowCount = i17;
            this.stickersEndRow = i17;
            int i18 = this.currentType;
            if (i18 != 1 && i18 != 5) {
                this.rowCount = i17 + 1;
                this.stickersShadowRow = i17;
                this.masksInfoRow = -1;
                if (!arrayList2.isEmpty()) {
                    if (size > 0) {
                    }
                    int i19 = this.rowCount;
                    int i20 = i19 + 1;
                    this.rowCount = i20;
                    this.featuredStickersHeaderRow = i19;
                    this.featuredStickersStartRow = i20;
                    int size2 = i20 + arrayList2.size();
                    this.rowCount = size2;
                    this.featuredStickersEndRow = size2;
                    if (z2) {
                    }
                }
                if (this.currentType == 5) {
                }
                listAdapter = this.listAdapter;
                if (listAdapter != null) {
                }
            } else if (i18 == 1) {
                this.rowCount = i17 + 1;
                this.masksInfoRow = i17;
                this.stickersShadowRow = -1;
                if (!arrayList2.isEmpty() && this.currentType == 5) {
                    if (size > 0) {
                        int i21 = this.rowCount;
                        this.rowCount = i21 + 1;
                        this.featuredStickersShadowRow = i21;
                    }
                    int i192 = this.rowCount;
                    int i202 = i192 + 1;
                    this.rowCount = i202;
                    this.featuredStickersHeaderRow = i192;
                    this.featuredStickersStartRow = i202;
                    int size22 = i202 + arrayList2.size();
                    this.rowCount = size22;
                    this.featuredStickersEndRow = size22;
                    if (z2) {
                        this.rowCount = size22 + 1;
                        this.featuredStickersShowMoreRow = size22;
                    }
                }
                if (this.currentType == 5) {
                    int i22 = this.rowCount;
                    this.rowCount = i22 + 1;
                    this.stickersBotInfo = i22;
                }
                listAdapter = this.listAdapter;
                if (listAdapter != null) {
                    if (diffResult2 != null) {
                        final int i23 = this.stickersStartRow;
                        if (i23 < 0) {
                            i23 = this.rowCount;
                        }
                        listAdapter.notifyItemRangeChanged(0, i23);
                        diffResult2.dispatchUpdatesTo(new ListUpdateCallback() { // from class: org.telegram.ui.StickersActivity.8
                            @Override // androidx.recyclerview.widget.ListUpdateCallback
                            public void onChanged(int i24, int i25, Object obj) {
                                StickersActivity.this.listAdapter.notifyItemRangeChanged(i23 + i24, i25);
                            }

                            @Override // androidx.recyclerview.widget.ListUpdateCallback
                            public void onInserted(int i24, int i25) {
                                StickersActivity.this.listAdapter.notifyItemRangeInserted(i23 + i24, i25);
                            }

                            @Override // androidx.recyclerview.widget.ListUpdateCallback
                            public void onMoved(int i24, int i25) {
                                if (StickersActivity.this.currentType == 5) {
                                    ListAdapter listAdapter5 = StickersActivity.this.listAdapter;
                                    int i26 = i23;
                                    listAdapter5.notifyItemMoved(i24 + i26, i26 + i25);
                                }
                            }

                            @Override // androidx.recyclerview.widget.ListUpdateCallback
                            public void onRemoved(int i24, int i25) {
                                StickersActivity.this.listAdapter.notifyItemRangeRemoved(i23 + i24, i25);
                            }
                        });
                    }
                    if (diffResult != null) {
                        final int i24 = this.featuredStickersStartRow;
                        if (i24 < 0) {
                            i24 = this.rowCount;
                        }
                        this.listAdapter.notifyItemRangeChanged(0, i24);
                        diffResult.dispatchUpdatesTo(new ListUpdateCallback() { // from class: org.telegram.ui.StickersActivity.9
                            @Override // androidx.recyclerview.widget.ListUpdateCallback
                            public void onChanged(int i25, int i26, Object obj) {
                                StickersActivity.this.listAdapter.notifyItemRangeChanged(i24 + i25, i26);
                            }

                            @Override // androidx.recyclerview.widget.ListUpdateCallback
                            public void onInserted(int i25, int i26) {
                                StickersActivity.this.listAdapter.notifyItemRangeInserted(i24 + i25, i26);
                            }

                            @Override // androidx.recyclerview.widget.ListUpdateCallback
                            public void onMoved(int i25, int i26) {
                            }

                            @Override // androidx.recyclerview.widget.ListUpdateCallback
                            public void onRemoved(int i25, int i26) {
                                StickersActivity.this.listAdapter.notifyItemRangeRemoved(i24 + i25, i26);
                            }
                        });
                        return;
                    }
                    return;
                }
                return;
            }
        } else {
            this.stickersHeaderRow = -1;
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
        }
        this.stickersShadowRow = -1;
        this.masksInfoRow = -1;
        if (!arrayList2.isEmpty()) {
        }
        if (this.currentType == 5) {
        }
        listAdapter = this.listAdapter;
        if (listAdapter != null) {
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        ActionBar actionBar;
        int i;
        this.actionBar.setBackButtonDrawable(new BackDrawable(false));
        this.actionBar.setAllowOverlayTitle(true);
        int i2 = this.currentType;
        if (i2 == 0) {
            actionBar = this.actionBar;
            i = R.string.StickersName;
        } else {
            if (i2 != 1) {
                if (i2 == 5) {
                    actionBar = this.actionBar;
                    i = R.string.Emoji;
                }
                this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.StickersActivity.1
                    @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
                    public void onItemClick(int i3) {
                        if (i3 == -1) {
                            if (StickersActivity.this.onBackPressed()) {
                                StickersActivity.this.lambda$onBackPressed$319();
                            }
                        } else if (i3 == 0 || i3 == 1 || i3 == 2) {
                            if (StickersActivity.this.needReorder) {
                                StickersActivity.this.sendReorder();
                            } else if (StickersActivity.this.activeReorderingRequests == 0) {
                                StickersActivity.this.listAdapter.processSelectionMenu(i3);
                            }
                        }
                    }
                });
                ActionBarMenu createActionMode = this.actionBar.createActionMode();
                NumberTextView numberTextView = new NumberTextView(createActionMode.getContext());
                this.selectedCountTextView = numberTextView;
                numberTextView.setTextSize(18);
                this.selectedCountTextView.setTypeface(AndroidUtilities.bold());
                this.selectedCountTextView.setTextColor(Theme.getColor(Theme.key_actionBarActionModeDefaultIcon));
                createActionMode.addView(this.selectedCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 72, 0, 0, 0));
                this.selectedCountTextView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        boolean lambda$createView$0;
                        lambda$createView$0 = StickersActivity.lambda$createView$0(view, motionEvent);
                        return lambda$createView$0;
                    }
                });
                this.shareMenuItem = createActionMode.addItemWithWidth(2, R.drawable.msg_share, AndroidUtilities.dp(54.0f));
                this.archiveMenuItem = createActionMode.addItemWithWidth(0, R.drawable.msg_archive, AndroidUtilities.dp(54.0f));
                this.deleteMenuItem = createActionMode.addItemWithWidth(1, R.drawable.msg_delete, AndroidUtilities.dp(54.0f));
                if (this.currentType == 5 || (r0 = this.frozenEmojiPacks) == null) {
                    ArrayList arrayList = new ArrayList(MessagesController.getInstance(this.currentAccount).filterPremiumStickers(MediaDataController.getInstance(this.currentAccount).getStickerSets(this.currentType)));
                }
                this.listAdapter = new ListAdapter(context, arrayList, getFeaturedSets());
                FrameLayout frameLayout = new FrameLayout(context);
                this.fragmentView = frameLayout;
                frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
                RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.StickersActivity.2
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                    public void dispatchDraw(Canvas canvas) {
                        if (((BaseFragment) StickersActivity.this).actionBar.isActionModeShowed()) {
                            drawSectionBackground(canvas, StickersActivity.this.stickersHeaderRow, StickersActivity.this.stickersEndRow - 1, getThemedColor(Theme.key_windowBackgroundWhite));
                        }
                        super.dispatchDraw(canvas);
                    }
                };
                this.listView = recyclerListView;
                recyclerListView.setFocusable(true);
                this.listView.setTag(7);
                DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.StickersActivity.3
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // androidx.recyclerview.widget.DefaultItemAnimator
                    public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                        super.onMoveAnimationUpdate(viewHolder);
                        StickersActivity.this.listView.invalidate();
                    }
                };
                defaultItemAnimator.setMoveDuration(350L);
                defaultItemAnimator.setSupportsChangeAnimations(false);
                defaultItemAnimator.setMoveInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.listView.setItemAnimator(defaultItemAnimator);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) { // from class: org.telegram.ui.StickersActivity.4
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // androidx.recyclerview.widget.LinearLayoutManager
                    public void calculateExtraLayoutSpace(RecyclerView.State state, int[] iArr) {
                        iArr[1] = StickersActivity.this.listView.getHeight();
                    }

                    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                    public boolean supportsPredictiveItemAnimations() {
                        return false;
                    }
                };
                this.layoutManager = linearLayoutManager;
                linearLayoutManager.setOrientation(1);
                this.listView.setLayoutManager(this.layoutManager);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
                this.itemTouchHelper = itemTouchHelper;
                itemTouchHelper.attachToRecyclerView(this.listView);
                frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
                this.listView.setAdapter(this.listAdapter);
                this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda1
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i3) {
                        StickersActivity.this.lambda$createView$2(context, view, i3);
                    }
                });
                this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda2
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
                    public final boolean onItemClick(View view, int i3) {
                        boolean lambda$createView$3;
                        lambda$createView$3 = StickersActivity.this.lambda$createView$3(view, i3);
                        return lambda$createView$3;
                    }
                });
                return this.fragmentView;
            }
            actionBar = this.actionBar;
            i = R.string.Masks;
        }
        actionBar.setTitle(LocaleController.getString(i));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.StickersActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i3) {
                if (i3 == -1) {
                    if (StickersActivity.this.onBackPressed()) {
                        StickersActivity.this.lambda$onBackPressed$319();
                    }
                } else if (i3 == 0 || i3 == 1 || i3 == 2) {
                    if (StickersActivity.this.needReorder) {
                        StickersActivity.this.sendReorder();
                    } else if (StickersActivity.this.activeReorderingRequests == 0) {
                        StickersActivity.this.listAdapter.processSelectionMenu(i3);
                    }
                }
            }
        });
        ActionBarMenu createActionMode2 = this.actionBar.createActionMode();
        NumberTextView numberTextView2 = new NumberTextView(createActionMode2.getContext());
        this.selectedCountTextView = numberTextView2;
        numberTextView2.setTextSize(18);
        this.selectedCountTextView.setTypeface(AndroidUtilities.bold());
        this.selectedCountTextView.setTextColor(Theme.getColor(Theme.key_actionBarActionModeDefaultIcon));
        createActionMode2.addView(this.selectedCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 72, 0, 0, 0));
        this.selectedCountTextView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createView$0;
                lambda$createView$0 = StickersActivity.lambda$createView$0(view, motionEvent);
                return lambda$createView$0;
            }
        });
        this.shareMenuItem = createActionMode2.addItemWithWidth(2, R.drawable.msg_share, AndroidUtilities.dp(54.0f));
        this.archiveMenuItem = createActionMode2.addItemWithWidth(0, R.drawable.msg_archive, AndroidUtilities.dp(54.0f));
        this.deleteMenuItem = createActionMode2.addItemWithWidth(1, R.drawable.msg_delete, AndroidUtilities.dp(54.0f));
        if (this.currentType == 5) {
        }
        ArrayList arrayList2 = new ArrayList(MessagesController.getInstance(this.currentAccount).filterPremiumStickers(MediaDataController.getInstance(this.currentAccount).getStickerSets(this.currentType)));
        this.listAdapter = new ListAdapter(context, arrayList2, getFeaturedSets());
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.fragmentView = frameLayout2;
        frameLayout2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        RecyclerListView recyclerListView2 = new RecyclerListView(context) { // from class: org.telegram.ui.StickersActivity.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                if (((BaseFragment) StickersActivity.this).actionBar.isActionModeShowed()) {
                    drawSectionBackground(canvas, StickersActivity.this.stickersHeaderRow, StickersActivity.this.stickersEndRow - 1, getThemedColor(Theme.key_windowBackgroundWhite));
                }
                super.dispatchDraw(canvas);
            }
        };
        this.listView = recyclerListView2;
        recyclerListView2.setFocusable(true);
        this.listView.setTag(7);
        DefaultItemAnimator defaultItemAnimator2 = new DefaultItemAnimator() { // from class: org.telegram.ui.StickersActivity.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                super.onMoveAnimationUpdate(viewHolder);
                StickersActivity.this.listView.invalidate();
            }
        };
        defaultItemAnimator2.setMoveDuration(350L);
        defaultItemAnimator2.setSupportsChangeAnimations(false);
        defaultItemAnimator2.setMoveInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.listView.setItemAnimator(defaultItemAnimator2);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context) { // from class: org.telegram.ui.StickersActivity.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.LinearLayoutManager
            public void calculateExtraLayoutSpace(RecyclerView.State state, int[] iArr) {
                iArr[1] = StickersActivity.this.listView.getHeight();
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager = linearLayoutManager2;
        linearLayoutManager2.setOrientation(1);
        this.listView.setLayoutManager(this.layoutManager);
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(new TouchHelperCallback());
        this.itemTouchHelper = itemTouchHelper2;
        itemTouchHelper2.attachToRecyclerView(this.listView);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i3) {
                StickersActivity.this.lambda$createView$2(context, view, i3);
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.StickersActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i3) {
                boolean lambda$createView$3;
                lambda$createView$3 = StickersActivity.this.lambda$createView$3(view, i3);
                return lambda$createView$3;
            }
        });
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i != NotificationCenter.stickersDidLoad) {
            if (i == NotificationCenter.featuredStickersDidLoad || i == NotificationCenter.featuredEmojiDidLoad || (i == NotificationCenter.archivedStickersCountDidLoad && ((Integer) objArr[0]).intValue() == this.currentType)) {
                updateRows(false);
                return;
            }
            return;
        }
        int intValue = ((Integer) objArr[0]).intValue();
        int i3 = this.currentType;
        if (intValue == i3) {
            this.listAdapter.loadingFeaturedStickerSets.clear();
            updateRows(((Boolean) objArr[1]).booleanValue());
        } else if (i3 == 0 && intValue == 1) {
            this.listAdapter.notifyItemChanged(this.masksRow);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        int i = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{StickerSetCell.class, TextSettingsCell.class, TextCheckCell.class}, null, null, null, i));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i2 = ThemeDescription.FLAG_BACKGROUND;
        int i3 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i2, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        ActionBar actionBar2 = this.actionBar;
        int i4 = ThemeDescription.FLAG_AB_AM_ITEMSCOLOR;
        int i5 = Theme.key_actionBarActionModeDefaultIcon;
        arrayList.add(new ThemeDescription(actionBar2, i4, null, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_BACKGROUND, null, null, null, null, Theme.key_actionBarActionModeDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_TOPBACKGROUND, null, null, null, null, Theme.key_actionBarActionModeDefaultTop));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarActionModeDefaultSelector));
        arrayList.add(new ThemeDescription(this.selectedCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        int i6 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrack));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackChecked));
        int i7 = Theme.key_windowBackgroundGrayShadow;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteLinkText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_stickers_menuSelector));
        int i8 = Theme.key_stickers_menu;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"reorderButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i8));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{StickerSetCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{StickerSetCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_checkboxCheck));
        TrendingStickersAlert trendingStickersAlert = this.trendingStickersAlert;
        if (trendingStickersAlert != null) {
            arrayList.addAll(trendingStickersAlert.getThemeDescriptions());
        }
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (!this.listAdapter.hasSelected()) {
            return super.onBackPressed();
        }
        this.listAdapter.clearSelected();
        return false;
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
        updateRows(false);
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
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }
}
