package org.telegram.ui.Components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CacheControlActivity;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.SharedAudioCell;
import org.telegram.ui.Cells.SharedDocumentCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.FilteredSearchView;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class SearchDownloadsContainer extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    boolean checkingFilesExist;
    private final int currentAccount;
    StickerEmptyView emptyView;
    private boolean hasCurrentDownload;
    RecyclerItemsEnterAnimator itemsEnterAnimator;
    String lastQueryString;
    Runnable lastSearchRunnable;
    private final FlickerLoadingView loadingView;
    Activity parentActivity;
    BaseFragment parentFragment;
    public RecyclerListView recyclerListView;
    int rowCount;
    String searchQuery;
    FilteredSearchView.UiCallback uiCallback;
    DownloadsAdapter adapter = new DownloadsAdapter(this, null);
    ArrayList<MessageObject> currentLoadingFiles = new ArrayList<>();
    ArrayList<MessageObject> recentLoadingFiles = new ArrayList<>();
    ArrayList<MessageObject> currentLoadingFilesTmp = new ArrayList<>();
    ArrayList<MessageObject> recentLoadingFilesTmp = new ArrayList<>();
    int downloadingFilesHeader = -1;
    int downloadingFilesStartRow = -1;
    int downloadingFilesEndRow = -1;
    int recentFilesHeader = -1;
    int recentFilesStartRow = -1;
    int recentFilesEndRow = -1;
    private final FilteredSearchView.MessageHashId messageHashIdTmp = new FilteredSearchView.MessageHashId(0, 0);

    public SearchDownloadsContainer(BaseFragment baseFragment, int i) {
        super(baseFragment.getParentActivity());
        this.parentFragment = baseFragment;
        this.parentActivity = baseFragment.getParentActivity();
        this.currentAccount = i;
        BlurredRecyclerView blurredRecyclerView = new BlurredRecyclerView(getContext());
        this.recyclerListView = blurredRecyclerView;
        addView(blurredRecyclerView);
        this.recyclerListView.setLayoutManager(new AnonymousClass1(this, baseFragment.getParentActivity()));
        this.recyclerListView.setAdapter(this.adapter);
        this.recyclerListView.setOnScrollListener(new AnonymousClass2());
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.recyclerListView.setItemAnimator(defaultItemAnimator);
        this.recyclerListView.setOnItemClickListener(new SearchDownloadsContainer$$ExternalSyntheticLambda6(this));
        this.recyclerListView.setOnItemLongClickListener(new SearchDownloadsContainer$$ExternalSyntheticLambda7(this));
        this.itemsEnterAnimator = new RecyclerItemsEnterAnimator(this.recyclerListView, true);
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(getContext());
        this.loadingView = flickerLoadingView;
        addView(flickerLoadingView);
        flickerLoadingView.setUseHeaderOffset(true);
        flickerLoadingView.setViewType(3);
        flickerLoadingView.setVisibility(8);
        StickerEmptyView stickerEmptyView = new StickerEmptyView(getContext(), flickerLoadingView, 1);
        this.emptyView = stickerEmptyView;
        addView(stickerEmptyView);
        this.recyclerListView.setEmptyView(this.emptyView);
        FileLoader.getInstance(i).getCurrentLoadingFiles(this.currentLoadingFiles);
    }

    /* renamed from: org.telegram.ui.Components.SearchDownloadsContainer$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends LinearLayoutManager {
        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return true;
        }

        AnonymousClass1(SearchDownloadsContainer searchDownloadsContainer, Context context) {
            super(context);
        }
    }

    /* renamed from: org.telegram.ui.Components.SearchDownloadsContainer$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends RecyclerView.OnScrollListener {
        AnonymousClass2() {
            SearchDownloadsContainer.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                AndroidUtilities.hideKeyboard(SearchDownloadsContainer.this.parentActivity.getCurrentFocus());
            }
        }
    }

    public /* synthetic */ void lambda$new$0(View view, int i) {
        MessageObject message = this.adapter.getMessage(i);
        if (message == null) {
            return;
        }
        if (this.uiCallback.actionModeShowing()) {
            this.uiCallback.toggleItemSelection(message, view, 0);
            this.messageHashIdTmp.set(message.getId(), message.getDialogId());
            this.adapter.notifyItemChanged(i);
            return;
        }
        if (view instanceof Cell) {
            SharedDocumentCell sharedDocumentCell = ((Cell) view).sharedDocumentCell;
            MessageObject message2 = sharedDocumentCell.getMessage();
            TLRPC$Document document = message2.getDocument();
            if (sharedDocumentCell.isLoaded()) {
                if (message2.isRoundVideo() || message2.isVoice()) {
                    MediaController.getInstance().playMessage(message2);
                    return;
                } else if (message2.canPreviewDocument()) {
                    PhotoViewer.getInstance().setParentActivity(this.parentActivity);
                    ArrayList<MessageObject> arrayList = new ArrayList<>();
                    arrayList.add(message2);
                    PhotoViewer.getInstance().setParentActivity(this.parentActivity);
                    PhotoViewer.getInstance().openPhoto(arrayList, 0, 0L, 0L, new PhotoViewer.EmptyPhotoViewerProvider());
                    return;
                } else {
                    AndroidUtilities.openDocument(message2, this.parentActivity, this.parentFragment);
                }
            } else if (!sharedDocumentCell.isLoading()) {
                message.putInDownloadsStore = true;
                AccountInstance.getInstance(UserConfig.selectedAccount).getFileLoader().loadFile(document, message, 0, 0);
                sharedDocumentCell.updateFileExistIcon(true);
            } else {
                AccountInstance.getInstance(UserConfig.selectedAccount).getFileLoader().cancelLoadFile(document);
                sharedDocumentCell.updateFileExistIcon(true);
            }
            update(true);
        }
        if (!(view instanceof SharedAudioCell)) {
            return;
        }
        ((SharedAudioCell) view).didPressedButton();
    }

    public /* synthetic */ boolean lambda$new$1(View view, int i) {
        MessageObject message = this.adapter.getMessage(i);
        if (message != null) {
            if (!this.uiCallback.actionModeShowing()) {
                this.uiCallback.showActionMode();
            }
            if (!this.uiCallback.actionModeShowing()) {
                return true;
            }
            this.uiCallback.toggleItemSelection(message, view, 0);
            this.messageHashIdTmp.set(message.getId(), message.getDialogId());
            this.adapter.notifyItemChanged(i);
            return true;
        }
        return false;
    }

    private void checkFilesExist() {
        if (this.checkingFilesExist) {
            return;
        }
        this.checkingFilesExist = true;
        Utilities.searchQueue.postRunnable(new SearchDownloadsContainer$$ExternalSyntheticLambda2(this));
    }

    public /* synthetic */ void lambda$checkFilesExist$3() {
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        ArrayList<MessageObject> arrayList2 = new ArrayList<>();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        FileLoader.getInstance(this.currentAccount).getCurrentLoadingFiles(arrayList);
        FileLoader.getInstance(this.currentAccount).getRecentLoadingFiles(arrayList2);
        for (int i = 0; i < arrayList.size(); i++) {
            if (FileLoader.getInstance(this.currentAccount).getPathToMessage(arrayList.get(i).messageOwner).exists()) {
                arrayList3.add(arrayList.get(i));
            }
        }
        for (int i2 = 0; i2 < arrayList2.size(); i2++) {
            if (!FileLoader.getInstance(this.currentAccount).getPathToMessage(arrayList2.get(i2).messageOwner).exists()) {
                arrayList4.add(arrayList2.get(i2));
            }
        }
        AndroidUtilities.runOnUIThread(new SearchDownloadsContainer$$ExternalSyntheticLambda5(this, arrayList3, arrayList4));
    }

    public /* synthetic */ void lambda$checkFilesExist$2(ArrayList arrayList, ArrayList arrayList2) {
        for (int i = 0; i < arrayList.size(); i++) {
            DownloadController.getInstance(this.currentAccount).onDownloadComplete((MessageObject) arrayList.get(i));
        }
        if (!arrayList2.isEmpty()) {
            DownloadController.getInstance(this.currentAccount).deleteRecentFiles(arrayList2);
        }
        this.checkingFilesExist = false;
        update(true);
    }

    public void update(boolean z) {
        if (TextUtils.isEmpty(this.searchQuery) || isEmptyDownloads()) {
            if (this.rowCount == 0) {
                this.itemsEnterAnimator.showItemsAnimated(0);
            }
            if (this.checkingFilesExist) {
                this.currentLoadingFilesTmp.clear();
                this.recentLoadingFilesTmp.clear();
            }
            FileLoader.getInstance(this.currentAccount).getCurrentLoadingFiles(this.currentLoadingFilesTmp);
            FileLoader.getInstance(this.currentAccount).getRecentLoadingFiles(this.recentLoadingFilesTmp);
            for (int i = 0; i < this.currentLoadingFiles.size(); i++) {
                this.currentLoadingFiles.get(i).setQuery(null);
            }
            for (int i2 = 0; i2 < this.recentLoadingFiles.size(); i2++) {
                this.recentLoadingFiles.get(i2).setQuery(null);
            }
            this.lastQueryString = null;
            updateListInternal(z, this.currentLoadingFilesTmp, this.recentLoadingFilesTmp);
            if (this.rowCount == 0) {
                this.emptyView.showProgress(false, false);
                this.emptyView.title.setText(LocaleController.getString("SearchEmptyViewDownloads", 2131628097));
                this.emptyView.subtitle.setVisibility(8);
            }
            this.emptyView.setStickerType(9);
            return;
        }
        this.emptyView.setStickerType(1);
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        ArrayList<MessageObject> arrayList2 = new ArrayList<>();
        FileLoader.getInstance(this.currentAccount).getCurrentLoadingFiles(arrayList);
        FileLoader.getInstance(this.currentAccount).getRecentLoadingFiles(arrayList2);
        String lowerCase = this.searchQuery.toLowerCase();
        boolean equals = lowerCase.equals(this.lastQueryString);
        this.lastQueryString = lowerCase;
        Utilities.searchQueue.cancelRunnable(this.lastSearchRunnable);
        DispatchQueue dispatchQueue = Utilities.searchQueue;
        SearchDownloadsContainer$$ExternalSyntheticLambda4 searchDownloadsContainer$$ExternalSyntheticLambda4 = new SearchDownloadsContainer$$ExternalSyntheticLambda4(this, arrayList, lowerCase, arrayList2);
        this.lastSearchRunnable = searchDownloadsContainer$$ExternalSyntheticLambda4;
        dispatchQueue.postRunnable(searchDownloadsContainer$$ExternalSyntheticLambda4, equals ? 0L : 300L);
        this.recentLoadingFilesTmp.clear();
        this.currentLoadingFilesTmp.clear();
        if (equals) {
            return;
        }
        this.emptyView.showProgress(true, true);
        updateListInternal(z, this.currentLoadingFilesTmp, this.recentLoadingFilesTmp);
    }

    public /* synthetic */ void lambda$update$5(ArrayList arrayList, String str, ArrayList arrayList2) {
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            if (FileLoader.getDocumentFileName(((MessageObject) arrayList.get(i)).getDocument()).toLowerCase().contains(str)) {
                MessageObject messageObject = new MessageObject(this.currentAccount, ((MessageObject) arrayList.get(i)).messageOwner, false, false);
                messageObject.mediaExists = ((MessageObject) arrayList.get(i)).mediaExists;
                messageObject.setQuery(this.searchQuery);
                arrayList3.add(messageObject);
            }
        }
        for (int i2 = 0; i2 < arrayList2.size(); i2++) {
            if (FileLoader.getDocumentFileName(((MessageObject) arrayList2.get(i2)).getDocument()).toLowerCase().contains(str)) {
                MessageObject messageObject2 = new MessageObject(this.currentAccount, ((MessageObject) arrayList2.get(i2)).messageOwner, false, false);
                messageObject2.mediaExists = ((MessageObject) arrayList2.get(i2)).mediaExists;
                messageObject2.setQuery(this.searchQuery);
                arrayList4.add(messageObject2);
            }
        }
        AndroidUtilities.runOnUIThread(new SearchDownloadsContainer$$ExternalSyntheticLambda3(this, str, arrayList3, arrayList4));
    }

    public /* synthetic */ void lambda$update$4(String str, ArrayList arrayList, ArrayList arrayList2) {
        if (str.equals(this.lastQueryString)) {
            if (this.rowCount == 0) {
                this.itemsEnterAnimator.showItemsAnimated(0);
            }
            updateListInternal(true, arrayList, arrayList2);
            if (this.rowCount != 0) {
                return;
            }
            this.emptyView.showProgress(false, true);
            this.emptyView.title.setText(LocaleController.getString("SearchEmptyViewTitle2", 2131628105));
            this.emptyView.subtitle.setVisibility(0);
            this.emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", 2131628098));
        }
    }

    private boolean isEmptyDownloads() {
        return DownloadController.getInstance(this.currentAccount).downloadingFiles.isEmpty() && DownloadController.getInstance(this.currentAccount).recentDownloadingFiles.isEmpty();
    }

    private void updateListInternal(boolean z, ArrayList<MessageObject> arrayList, ArrayList<MessageObject> arrayList2) {
        RecyclerView.ViewHolder childViewHolder;
        if (z) {
            int i = this.downloadingFilesHeader;
            int i2 = this.downloadingFilesStartRow;
            int i3 = this.downloadingFilesEndRow;
            int i4 = this.recentFilesHeader;
            int i5 = this.recentFilesStartRow;
            int i6 = this.recentFilesEndRow;
            int i7 = this.rowCount;
            ArrayList arrayList3 = new ArrayList(this.currentLoadingFiles);
            ArrayList arrayList4 = new ArrayList(this.recentLoadingFiles);
            updateRows(arrayList, arrayList2);
            DiffUtil.calculateDiff(new AnonymousClass3(i7, i, i4, i2, i3, arrayList3, i5, i6, arrayList4)).dispatchUpdatesTo(this.adapter);
            for (int i8 = 0; i8 < this.recyclerListView.getChildCount(); i8++) {
                View childAt = this.recyclerListView.getChildAt(i8);
                int childAdapterPosition = this.recyclerListView.getChildAdapterPosition(childAt);
                if (childAdapterPosition >= 0 && (childViewHolder = this.recyclerListView.getChildViewHolder(childAt)) != null && !childViewHolder.shouldIgnore()) {
                    if (childAt instanceof GraySectionCell) {
                        this.adapter.onBindViewHolder(childViewHolder, childAdapterPosition);
                    } else if (childAt instanceof Cell) {
                        Cell cell = (Cell) childAt;
                        cell.sharedDocumentCell.updateFileExistIcon(true);
                        this.messageHashIdTmp.set(cell.sharedDocumentCell.getMessage().getId(), cell.sharedDocumentCell.getMessage().getDialogId());
                        cell.sharedDocumentCell.setChecked(this.uiCallback.isSelected(this.messageHashIdTmp), true);
                    }
                }
            }
            return;
        }
        updateRows(arrayList, arrayList2);
        this.adapter.notifyDataSetChanged();
    }

    /* renamed from: org.telegram.ui.Components.SearchDownloadsContainer$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends DiffUtil.Callback {
        final /* synthetic */ int val$oldDownloadingFilesEndRow;
        final /* synthetic */ int val$oldDownloadingFilesHeader;
        final /* synthetic */ int val$oldDownloadingFilesStartRow;
        final /* synthetic */ ArrayList val$oldDownloadingLoadingFiles;
        final /* synthetic */ int val$oldRecentFilesEndRow;
        final /* synthetic */ int val$oldRecentFilesHeader;
        final /* synthetic */ int val$oldRecentFilesStartRow;
        final /* synthetic */ ArrayList val$oldRecentLoadingFiles;
        final /* synthetic */ int val$oldRowCount;

        AnonymousClass3(int i, int i2, int i3, int i4, int i5, ArrayList arrayList, int i6, int i7, ArrayList arrayList2) {
            SearchDownloadsContainer.this = r1;
            this.val$oldRowCount = i;
            this.val$oldDownloadingFilesHeader = i2;
            this.val$oldRecentFilesHeader = i3;
            this.val$oldDownloadingFilesStartRow = i4;
            this.val$oldDownloadingFilesEndRow = i5;
            this.val$oldDownloadingLoadingFiles = arrayList;
            this.val$oldRecentFilesStartRow = i6;
            this.val$oldRecentFilesEndRow = i7;
            this.val$oldRecentLoadingFiles = arrayList2;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.val$oldRowCount;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return SearchDownloadsContainer.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            MessageObject messageObject;
            if (i >= 0 && i2 >= 0) {
                if (i == this.val$oldDownloadingFilesHeader && i2 == SearchDownloadsContainer.this.downloadingFilesHeader) {
                    return true;
                }
                if (i == this.val$oldRecentFilesHeader && i2 == SearchDownloadsContainer.this.recentFilesHeader) {
                    return true;
                }
            }
            int i3 = this.val$oldDownloadingFilesStartRow;
            MessageObject messageObject2 = null;
            if (i >= i3 && i < this.val$oldDownloadingFilesEndRow) {
                messageObject = (MessageObject) this.val$oldDownloadingLoadingFiles.get(i - i3);
            } else {
                int i4 = this.val$oldRecentFilesStartRow;
                messageObject = (i < i4 || i >= this.val$oldRecentFilesEndRow) ? null : (MessageObject) this.val$oldRecentLoadingFiles.get(i - i4);
            }
            SearchDownloadsContainer searchDownloadsContainer = SearchDownloadsContainer.this;
            int i5 = searchDownloadsContainer.downloadingFilesStartRow;
            if (i2 >= i5 && i2 < searchDownloadsContainer.downloadingFilesEndRow) {
                messageObject2 = searchDownloadsContainer.currentLoadingFiles.get(i2 - i5);
            } else {
                int i6 = searchDownloadsContainer.recentFilesStartRow;
                if (i2 >= i6 && i2 < searchDownloadsContainer.recentFilesEndRow) {
                    messageObject2 = searchDownloadsContainer.recentLoadingFiles.get(i2 - i6);
                }
            }
            return (messageObject2 == null || messageObject == null || messageObject2.getDocument().id != messageObject.getDocument().id) ? false : true;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            return areItemsTheSame(i, i2);
        }
    }

    private void updateRows(ArrayList<MessageObject> arrayList, ArrayList<MessageObject> arrayList2) {
        this.currentLoadingFiles.clear();
        this.currentLoadingFiles.addAll(arrayList);
        this.recentLoadingFiles.clear();
        this.recentLoadingFiles.addAll(arrayList2);
        int i = 0;
        this.rowCount = 0;
        this.downloadingFilesHeader = -1;
        this.downloadingFilesStartRow = -1;
        this.downloadingFilesEndRow = -1;
        this.recentFilesHeader = -1;
        this.recentFilesStartRow = -1;
        this.recentFilesEndRow = -1;
        this.hasCurrentDownload = false;
        if (!this.currentLoadingFiles.isEmpty()) {
            int i2 = this.rowCount;
            int i3 = i2 + 1;
            this.rowCount = i3;
            this.downloadingFilesHeader = i2;
            this.downloadingFilesStartRow = i3;
            int size = i3 + this.currentLoadingFiles.size();
            this.rowCount = size;
            this.downloadingFilesEndRow = size;
            while (true) {
                if (i >= this.currentLoadingFiles.size()) {
                    break;
                } else if (FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentLoadingFiles.get(i).getFileName())) {
                    this.hasCurrentDownload = true;
                    break;
                } else {
                    i++;
                }
            }
        }
        if (!this.recentLoadingFiles.isEmpty()) {
            int i4 = this.rowCount;
            int i5 = i4 + 1;
            this.rowCount = i5;
            this.recentFilesHeader = i4;
            this.recentFilesStartRow = i5;
            int size2 = i5 + this.recentLoadingFiles.size();
            this.rowCount = size2;
            this.recentFilesEndRow = size2;
        }
    }

    public void search(String str) {
        this.searchQuery = str;
        update(false);
    }

    /* loaded from: classes3.dex */
    public class DownloadsAdapter extends RecyclerListView.SelectionAdapter {
        private DownloadsAdapter() {
            SearchDownloadsContainer.this = r1;
        }

        /* synthetic */ DownloadsAdapter(SearchDownloadsContainer searchDownloadsContainer, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                view = new GraySectionCell(viewGroup.getContext());
            } else if (i == 1) {
                view = new Cell(SearchDownloadsContainer.this, viewGroup.getContext());
            } else {
                view = new AnonymousClass1(this, viewGroup.getContext());
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
        }

        /* renamed from: org.telegram.ui.Components.SearchDownloadsContainer$DownloadsAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends SharedAudioCell {
            AnonymousClass1(DownloadsAdapter downloadsAdapter, Context context) {
                super(context);
            }

            @Override // org.telegram.ui.Cells.SharedAudioCell
            public boolean needPlayMessage(MessageObject messageObject) {
                return MediaController.getInstance().playMessage(messageObject);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
                SearchDownloadsContainer searchDownloadsContainer = SearchDownloadsContainer.this;
                if (i == searchDownloadsContainer.downloadingFilesHeader) {
                    String string = LocaleController.getString("Downloading", 2131625534);
                    if (graySectionCell.getText().equals(string)) {
                        graySectionCell.setRightText(SearchDownloadsContainer.this.hasCurrentDownload ? LocaleController.getString("PauseAll", 2131627366) : LocaleController.getString("ResumeAll", 2131628035), SearchDownloadsContainer.this.hasCurrentDownload);
                        return;
                    } else {
                        graySectionCell.setText(string, SearchDownloadsContainer.this.hasCurrentDownload ? LocaleController.getString("PauseAll", 2131627366) : LocaleController.getString("ResumeAll", 2131628035), new AnonymousClass2());
                        return;
                    }
                } else if (i != searchDownloadsContainer.recentFilesHeader) {
                    return;
                } else {
                    graySectionCell.setText(LocaleController.getString("RecentlyDownloaded", 2131627864), LocaleController.getString("Settings", 2131628259), new AnonymousClass3());
                    return;
                }
            }
            MessageObject message = getMessage(i);
            if (message == null) {
                return;
            }
            boolean z = false;
            if (itemViewType != 1) {
                if (itemViewType != 2) {
                    return;
                }
                SharedAudioCell sharedAudioCell = (SharedAudioCell) viewHolder.itemView;
                sharedAudioCell.setMessageObject(message, true);
                int id = sharedAudioCell.getMessage() == null ? 0 : sharedAudioCell.getMessage().getId();
                SearchDownloadsContainer searchDownloadsContainer2 = SearchDownloadsContainer.this;
                boolean isSelected = searchDownloadsContainer2.uiCallback.isSelected(searchDownloadsContainer2.messageHashIdTmp);
                if (id == message.getId()) {
                    z = true;
                }
                sharedAudioCell.setChecked(isSelected, z);
                return;
            }
            Cell cell = (Cell) viewHolder.itemView;
            cell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            int id2 = cell.sharedDocumentCell.getMessage() == null ? 0 : cell.sharedDocumentCell.getMessage().getId();
            cell.sharedDocumentCell.setDocument(message, true);
            SearchDownloadsContainer.this.messageHashIdTmp.set(cell.sharedDocumentCell.getMessage().getId(), cell.sharedDocumentCell.getMessage().getDialogId());
            SharedDocumentCell sharedDocumentCell = cell.sharedDocumentCell;
            SearchDownloadsContainer searchDownloadsContainer3 = SearchDownloadsContainer.this;
            boolean isSelected2 = searchDownloadsContainer3.uiCallback.isSelected(searchDownloadsContainer3.messageHashIdTmp);
            if (id2 == message.getId()) {
                z = true;
            }
            sharedDocumentCell.setChecked(isSelected2, z);
        }

        /* renamed from: org.telegram.ui.Components.SearchDownloadsContainer$DownloadsAdapter$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 implements View.OnClickListener {
            AnonymousClass2() {
                DownloadsAdapter.this = r1;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                for (int i = 0; i < SearchDownloadsContainer.this.currentLoadingFiles.size(); i++) {
                    MessageObject messageObject = SearchDownloadsContainer.this.currentLoadingFiles.get(i);
                    if (SearchDownloadsContainer.this.hasCurrentDownload) {
                        AccountInstance.getInstance(UserConfig.selectedAccount).getFileLoader().cancelLoadFile(messageObject.getDocument());
                    } else {
                        AccountInstance.getInstance(UserConfig.selectedAccount).getFileLoader().loadFile(messageObject.getDocument(), messageObject, 0, 0);
                    }
                }
                SearchDownloadsContainer.this.update(true);
            }
        }

        /* renamed from: org.telegram.ui.Components.SearchDownloadsContainer$DownloadsAdapter$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 implements View.OnClickListener {
            AnonymousClass3() {
                DownloadsAdapter.this = r1;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SearchDownloadsContainer.this.showSettingsDialog();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            SearchDownloadsContainer searchDownloadsContainer = SearchDownloadsContainer.this;
            if (i == searchDownloadsContainer.downloadingFilesHeader || i == searchDownloadsContainer.recentFilesHeader) {
                return 0;
            }
            MessageObject message = getMessage(i);
            return (message != null && message.isMusic()) ? 2 : 1;
        }

        public MessageObject getMessage(int i) {
            SearchDownloadsContainer searchDownloadsContainer = SearchDownloadsContainer.this;
            int i2 = searchDownloadsContainer.downloadingFilesStartRow;
            if (i >= i2 && i < searchDownloadsContainer.downloadingFilesEndRow) {
                return searchDownloadsContainer.currentLoadingFiles.get(i - i2);
            }
            int i3 = searchDownloadsContainer.recentFilesStartRow;
            if (i >= i3 && i < searchDownloadsContainer.recentFilesEndRow) {
                return searchDownloadsContainer.recentLoadingFiles.get(i - i3);
            }
            return null;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return SearchDownloadsContainer.this.rowCount;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 1 || viewHolder.getItemViewType() == 2;
        }
    }

    public void showSettingsDialog() {
        if (this.parentFragment == null || this.parentActivity == null) {
            return;
        }
        BottomSheet bottomSheet = new BottomSheet(this.parentActivity, false);
        Activity parentActivity = this.parentFragment.getParentActivity();
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        StickerImageView stickerImageView = new StickerImageView(parentActivity, this.currentAccount);
        stickerImageView.setStickerNum(9);
        stickerImageView.getImageReceiver().setAutoRepeat(1);
        linearLayout.addView(stickerImageView, LayoutHelper.createLinear(144, 144, 1, 0, 16, 0, 0));
        TextView textView = new TextView(parentActivity);
        textView.setGravity(1);
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setTextSize(1, 24.0f);
        textView.setText(LocaleController.getString("DownloadedFiles", 2131625532));
        linearLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 0, 21.0f, 30.0f, 21.0f, 0.0f));
        TextView textView2 = new TextView(parentActivity);
        textView2.setGravity(1);
        textView2.setTextSize(1, 15.0f);
        textView2.setTextColor(Theme.getColor("dialogTextHint"));
        textView2.setText(LocaleController.formatString("DownloadedFilesMessage", 2131625533, new Object[0]));
        linearLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, 0, 21.0f, 15.0f, 21.0f, 16.0f));
        TextView textView3 = new TextView(parentActivity);
        textView3.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView3.setGravity(17);
        textView3.setTextSize(1, 14.0f);
        textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView3.setText(LocaleController.getString("ManageDeviceStorage", 2131626528));
        textView3.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        textView3.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("featuredStickers_addButton"), ColorUtils.setAlphaComponent(Theme.getColor("windowBackgroundWhite"), 120)));
        linearLayout.addView(textView3, LayoutHelper.createFrame(-1, 48.0f, 0, 16.0f, 15.0f, 16.0f, 16.0f));
        TextView textView4 = new TextView(parentActivity);
        textView4.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView4.setGravity(17);
        textView4.setTextSize(1, 14.0f);
        textView4.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView4.setText(LocaleController.getString("ClearDownloadsList", 2131625136));
        textView4.setTextColor(Theme.getColor("featuredStickers_addButton"));
        textView4.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, ColorUtils.setAlphaComponent(Theme.getColor("featuredStickers_addButton"), 120)));
        linearLayout.addView(textView4, LayoutHelper.createFrame(-1, 48.0f, 0, 16.0f, 0.0f, 16.0f, 16.0f));
        NestedScrollView nestedScrollView = new NestedScrollView(parentActivity);
        nestedScrollView.addView(linearLayout);
        bottomSheet.setCustomView(nestedScrollView);
        bottomSheet.show();
        if (Build.VERSION.SDK_INT >= 23) {
            AndroidUtilities.setLightStatusBar(bottomSheet.getWindow(), !Theme.isCurrentThemeDark());
            AndroidUtilities.setLightNavigationBar(bottomSheet.getWindow(), !Theme.isCurrentThemeDark());
        }
        textView3.setOnClickListener(new SearchDownloadsContainer$$ExternalSyntheticLambda0(this, bottomSheet));
        textView4.setOnClickListener(new SearchDownloadsContainer$$ExternalSyntheticLambda1(this, bottomSheet));
    }

    public /* synthetic */ void lambda$showSettingsDialog$6(BottomSheet bottomSheet, View view) {
        bottomSheet.dismiss();
        BaseFragment baseFragment = this.parentFragment;
        if (baseFragment != null) {
            baseFragment.presentFragment(new CacheControlActivity());
        }
    }

    public /* synthetic */ void lambda$showSettingsDialog$7(BottomSheet bottomSheet, View view) {
        bottomSheet.dismiss();
        DownloadController.getInstance(this.currentAccount).clearRecentDownloadedFiles();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.onDownloadingFilesChanged);
        if (getVisibility() == 0) {
            DownloadController.getInstance(this.currentAccount).clearUnviewedDownloads();
        }
        checkFilesExist();
        update(false);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.onDownloadingFilesChanged);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.onDownloadingFilesChanged) {
            if (getVisibility() == 0) {
                DownloadController.getInstance(this.currentAccount).clearUnviewedDownloads();
            }
            update(true);
        }
    }

    /* loaded from: classes3.dex */
    public class Cell extends FrameLayout {
        SharedDocumentCell sharedDocumentCell;

        public Cell(SearchDownloadsContainer searchDownloadsContainer, Context context) {
            super(context);
            SharedDocumentCell sharedDocumentCell = new SharedDocumentCell(context, 2);
            this.sharedDocumentCell = sharedDocumentCell;
            sharedDocumentCell.rightDateTextView.setVisibility(8);
            addView(this.sharedDocumentCell);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            this.sharedDocumentCell.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        }
    }

    public void setUiCallback(FilteredSearchView.UiCallback uiCallback) {
        this.uiCallback = uiCallback;
    }

    public void setKeyboardHeight(int i, boolean z) {
        this.emptyView.setKeyboardHeight(i, z);
    }
}
