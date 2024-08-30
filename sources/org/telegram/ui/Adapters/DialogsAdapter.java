package org.telegram.ui.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$RecentMeUrl;
import org.telegram.tgnet.TLRPC$RequestPeerType;
import org.telegram.tgnet.TLRPC$TL_contact;
import org.telegram.tgnet.TLRPC$TL_requestPeerTypeBroadcast;
import org.telegram.tgnet.TLRPC$TL_requestPeerTypeChat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.tgnet.tl.TL_chatlists$TL_chatlists_chatlistUpdates;
import org.telegram.tgnet.tl.TL_stories$PeerStories;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.DialogMeUrlCell;
import org.telegram.ui.Cells.DialogsEmptyCell;
import org.telegram.ui.Cells.DialogsHintCell;
import org.telegram.ui.Cells.DialogsRequestedEmptyCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.RequestPeerRequirementsCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.ArchiveHelp;
import org.telegram.ui.Components.BlurredRecyclerView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.PullForegroundDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
/* loaded from: classes4.dex */
public class DialogsAdapter extends RecyclerListView.SelectionAdapter implements DialogCell.DialogCellDelegate {
    private static final boolean ALLOW_UPDATE_IN_BACKGROUND = BuildVars.DEBUG_PRIVATE_VERSION;
    private Drawable arrowDrawable;
    private boolean collapsedView;
    private int currentAccount;
    private int currentCount;
    private int dialogsCount;
    private boolean dialogsListFrozen;
    private int dialogsType;
    private int folderId;
    private boolean forceShowEmptyCell;
    private boolean forceUpdatingContacts;
    private boolean hasChatlistHint;
    private boolean hasHints;
    boolean isCalculatingDiff;
    public boolean isEmpty;
    private boolean isOnlySelect;
    private boolean isReordering;
    private boolean isTransitionSupport;
    private long lastSortTime;
    private Context mContext;
    private ArrayList onlineContacts;
    private long openedDialogId;
    private DialogsActivity parentFragment;
    private DialogsPreloader preloader;
    private PullForegroundDrawable pullForegroundDrawable;
    RecyclerListView recyclerListView;
    private TLRPC$RequestPeerType requestPeerType;
    private ArrayList selectedDialogs;
    boolean updateListPending;
    private boolean firstUpdate = true;
    ArrayList itemInternals = new ArrayList();
    ArrayList oldItems = new ArrayList();
    int stableIdPointer = 10;
    LongSparseIntArray dialogsStableIds = new LongSparseIntArray();
    public int lastDialogsEmptyType = -1;

    /* loaded from: classes4.dex */
    public static class DialogsPreloader {
        int currentRequestCount;
        int networkRequestCount;
        boolean resumed;
        private final int MAX_REQUEST_COUNT = 4;
        private final int MAX_NETWORK_REQUEST_COUNT = 6;
        private final int NETWORK_REQUESTS_RESET_TIME = 60000;
        HashSet dialogsReadyMap = new HashSet();
        HashSet preloadedErrorMap = new HashSet();
        HashSet loadingDialogs = new HashSet();
        ArrayList preloadDialogsPool = new ArrayList();
        Runnable clearNetworkRequestCount = new Runnable() { // from class: org.telegram.ui.Adapters.DialogsAdapter$DialogsPreloader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DialogsAdapter.DialogsPreloader.this.lambda$new$0();
            }
        };

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public class 1 implements MessagesController.MessagesLoadedCallback {
            final /* synthetic */ long val$dialog_id;

            1(long j) {
                this.val$dialog_id = j;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onError$1(long j) {
                DialogsPreloader dialogsPreloader;
                if (DialogsPreloader.this.loadingDialogs.remove(Long.valueOf(j))) {
                    DialogsPreloader.this.preloadedErrorMap.add(Long.valueOf(j));
                    dialogsPreloader.currentRequestCount--;
                    DialogsPreloader.this.start();
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onMessagesLoaded$0(boolean z, long j) {
                DialogsPreloader dialogsPreloader;
                if (!z) {
                    DialogsPreloader dialogsPreloader2 = DialogsPreloader.this;
                    int i = dialogsPreloader2.networkRequestCount + 1;
                    dialogsPreloader2.networkRequestCount = i;
                    if (i >= 6) {
                        AndroidUtilities.cancelRunOnUIThread(dialogsPreloader2.clearNetworkRequestCount);
                        AndroidUtilities.runOnUIThread(DialogsPreloader.this.clearNetworkRequestCount, 60000L);
                    }
                }
                if (DialogsPreloader.this.loadingDialogs.remove(Long.valueOf(j))) {
                    DialogsPreloader.this.dialogsReadyMap.add(Long.valueOf(j));
                    DialogsPreloader.this.updateList();
                    dialogsPreloader.currentRequestCount--;
                    DialogsPreloader.this.start();
                }
            }

            @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
            public void onError() {
                final long j = this.val$dialog_id;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsAdapter$DialogsPreloader$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsAdapter.DialogsPreloader.1.this.lambda$onError$1(j);
                    }
                });
            }

            @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
            public void onMessagesLoaded(final boolean z) {
                final long j = this.val$dialog_id;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsAdapter$DialogsPreloader$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsAdapter.DialogsPreloader.1.this.lambda$onMessagesLoaded$0(z, j);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            this.networkRequestCount = 0;
            start();
        }

        private boolean preloadIsAvilable() {
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void start() {
            if (!preloadIsAvilable() || !this.resumed || this.preloadDialogsPool.isEmpty() || this.currentRequestCount >= 4 || this.networkRequestCount > 6) {
                return;
            }
            Long l = (Long) this.preloadDialogsPool.remove(0);
            long longValue = l.longValue();
            this.currentRequestCount++;
            this.loadingDialogs.add(l);
            MessagesController.getInstance(UserConfig.selectedAccount).ensureMessagesLoaded(longValue, 0, new 1(longValue));
        }

        public void add(long j) {
            if (isReady(j) || this.preloadedErrorMap.contains(Long.valueOf(j)) || this.loadingDialogs.contains(Long.valueOf(j)) || this.preloadDialogsPool.contains(Long.valueOf(j))) {
                return;
            }
            this.preloadDialogsPool.add(Long.valueOf(j));
            start();
        }

        public void clear() {
            this.dialogsReadyMap.clear();
            this.preloadedErrorMap.clear();
            this.loadingDialogs.clear();
            this.preloadDialogsPool.clear();
            this.currentRequestCount = 0;
            this.networkRequestCount = 0;
            AndroidUtilities.cancelRunOnUIThread(this.clearNetworkRequestCount);
            updateList();
        }

        public boolean isReady(long j) {
            return this.dialogsReadyMap.contains(Long.valueOf(j));
        }

        public void pause() {
            this.resumed = false;
        }

        public void remove(long j) {
            this.preloadDialogsPool.remove(Long.valueOf(j));
        }

        public void resume() {
            this.resumed = true;
            start();
        }

        public void updateList() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ItemInternal extends AdapterWithDiffUtils.Item {
        TL_chatlists$TL_chatlists_chatlistUpdates chatlistUpdates;
        TLRPC$TL_contact contact;
        TLRPC$Dialog dialog;
        private int emptyType;
        private boolean isFolder;
        boolean isForumCell;
        private boolean pinned;
        TLRPC$RecentMeUrl recentMeUrl;
        private final int stableId;

        public ItemInternal(int i) {
            super(i, true);
            this.emptyType = i;
            if (i == 10) {
                this.stableId = 1;
            } else if (this.viewType == 19) {
                this.stableId = 5;
            } else {
                int i2 = DialogsAdapter.this.stableIdPointer;
                DialogsAdapter.this.stableIdPointer = i2 + 1;
                this.stableId = i2;
            }
        }

        public ItemInternal(int i, int i2) {
            super(i, true);
            this.emptyType = i2;
            int i3 = DialogsAdapter.this.stableIdPointer;
            DialogsAdapter.this.stableIdPointer = i3 + 1;
            this.stableId = i3;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0037  */
        /* JADX WARN: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public ItemInternal(int i, TLRPC$Dialog tLRPC$Dialog) {
            super(i, true);
            int i2;
            boolean z = true;
            this.dialog = tLRPC$Dialog;
            if (tLRPC$Dialog != null) {
                i2 = DialogsAdapter.this.dialogsStableIds.get(tLRPC$Dialog.id, -1);
                if (i2 < 0) {
                    int i3 = DialogsAdapter.this.stableIdPointer;
                    DialogsAdapter.this.stableIdPointer = i3 + 1;
                    this.stableId = i3;
                    DialogsAdapter.this.dialogsStableIds.put(tLRPC$Dialog.id, i3);
                    if (tLRPC$Dialog == null) {
                        if (DialogsAdapter.this.dialogsType == 7 || DialogsAdapter.this.dialogsType == 8) {
                            MessagesController.DialogFilter dialogFilter = MessagesController.getInstance(DialogsAdapter.this.currentAccount).selectedDialogFilter[DialogsAdapter.this.dialogsType == 8 ? (char) 1 : (char) 0];
                            this.pinned = (dialogFilter == null || dialogFilter.pinnedDialogs.indexOfKey(tLRPC$Dialog.id) < 0) ? false : false;
                        } else {
                            this.pinned = tLRPC$Dialog.pinned;
                        }
                        this.isFolder = tLRPC$Dialog.isFolder;
                        this.isForumCell = MessagesController.getInstance(DialogsAdapter.this.currentAccount).isForum(tLRPC$Dialog.id);
                        return;
                    }
                    return;
                }
            } else if (i == 19) {
                i2 = 5;
            } else {
                i2 = DialogsAdapter.this.stableIdPointer;
                DialogsAdapter.this.stableIdPointer = i2 + 1;
            }
            this.stableId = i2;
            if (tLRPC$Dialog == null) {
            }
        }

        public ItemInternal(int i, TLRPC$RecentMeUrl tLRPC$RecentMeUrl) {
            super(i, true);
            this.recentMeUrl = tLRPC$RecentMeUrl;
            int i2 = DialogsAdapter.this.stableIdPointer;
            DialogsAdapter.this.stableIdPointer = i2 + 1;
            this.stableId = i2;
        }

        public ItemInternal(int i, TLRPC$TL_contact tLRPC$TL_contact) {
            super(i, true);
            int i2;
            this.contact = tLRPC$TL_contact;
            if (tLRPC$TL_contact != null) {
                i2 = DialogsAdapter.this.dialogsStableIds.get(tLRPC$TL_contact.user_id, -1);
                if (i2 <= 0) {
                    int i3 = DialogsAdapter.this.stableIdPointer;
                    DialogsAdapter.this.stableIdPointer = i3 + 1;
                    this.stableId = i3;
                    DialogsAdapter.this.dialogsStableIds.put(this.contact.user_id, i3);
                    return;
                }
            } else {
                i2 = DialogsAdapter.this.stableIdPointer;
                DialogsAdapter.this.stableIdPointer = i2 + 1;
            }
            this.stableId = i2;
        }

        public ItemInternal(TL_chatlists$TL_chatlists_chatlistUpdates tL_chatlists$TL_chatlists_chatlistUpdates) {
            super(17, true);
            this.chatlistUpdates = tL_chatlists$TL_chatlists_chatlistUpdates;
            int i = DialogsAdapter.this.stableIdPointer;
            DialogsAdapter.this.stableIdPointer = i + 1;
            this.stableId = i;
        }

        boolean compare(ItemInternal itemInternal) {
            TLRPC$TL_contact tLRPC$TL_contact;
            String str;
            TLRPC$Dialog tLRPC$Dialog;
            TLRPC$Dialog tLRPC$Dialog2;
            int i = this.viewType;
            if (i != itemInternal.viewType) {
                return false;
            }
            if (i == 0) {
                TLRPC$Dialog tLRPC$Dialog3 = this.dialog;
                return tLRPC$Dialog3 != null && (tLRPC$Dialog2 = itemInternal.dialog) != null && tLRPC$Dialog3.id == tLRPC$Dialog2.id && this.isFolder == itemInternal.isFolder && this.isForumCell == itemInternal.isForumCell && this.pinned == itemInternal.pinned;
            } else if (i == 14) {
                TLRPC$Dialog tLRPC$Dialog4 = this.dialog;
                return tLRPC$Dialog4 != null && (tLRPC$Dialog = itemInternal.dialog) != null && tLRPC$Dialog4.id == tLRPC$Dialog.id && tLRPC$Dialog4.isFolder == tLRPC$Dialog.isFolder;
            } else if (i == 4) {
                TLRPC$RecentMeUrl tLRPC$RecentMeUrl = this.recentMeUrl;
                return (tLRPC$RecentMeUrl == null || itemInternal.recentMeUrl == null || (str = tLRPC$RecentMeUrl.url) == null || !str.equals(str)) ? false : true;
            } else if (i != 6) {
                return i == 5 ? this.emptyType == itemInternal.emptyType : i != 10;
            } else {
                TLRPC$TL_contact tLRPC$TL_contact2 = this.contact;
                return (tLRPC$TL_contact2 == null || (tLRPC$TL_contact = itemInternal.contact) == null || tLRPC$TL_contact2.user_id != tLRPC$TL_contact.user_id) ? false : true;
            }
        }

        public int hashCode() {
            return Objects.hash(this.dialog, this.recentMeUrl, this.contact);
        }
    }

    /* loaded from: classes4.dex */
    public class LastEmptyView extends FrameLayout {
        public boolean moving;

        public LastEmptyView(Context context) {
            super(context);
        }

        /* JADX WARN: Code restructure failed: missing block: B:79:0x0170, code lost:
            if ((getParent() instanceof org.telegram.ui.DialogsActivity.DialogsRecyclerView) != false) goto L86;
         */
        /* JADX WARN: Code restructure failed: missing block: B:81:0x0173, code lost:
            if (r6 != false) goto L88;
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x019f, code lost:
            if ((getParent() instanceof org.telegram.ui.DialogsActivity.DialogsRecyclerView) != false) goto L86;
         */
        /* JADX WARN: Code restructure failed: missing block: B:94:0x01a1, code lost:
            r13 = r13 - ((org.telegram.ui.DialogsActivity.DialogsRecyclerView) getParent()).additionalPadding;
         */
        /* JADX WARN: Code restructure failed: missing block: B:95:0x01ab, code lost:
            if (r6 != false) goto L88;
         */
        /* JADX WARN: Code restructure failed: missing block: B:96:0x01ad, code lost:
            r13 = r13 - r7;
         */
        @Override // android.widget.FrameLayout, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            int i3;
            int size = DialogsAdapter.this.itemInternals.size();
            boolean z = DialogsAdapter.this.folderId == 0 && DialogsAdapter.this.dialogsType == 0 && MessagesController.getInstance(DialogsAdapter.this.currentAccount).dialogs_dict.get(DialogObject.makeFolderDialogId(1)) != null;
            View view = (View) getParent();
            int i4 = view instanceof BlurredRecyclerView ? ((BlurredRecyclerView) view).blurTopPadding : 0;
            boolean z2 = DialogsAdapter.this.collapsedView;
            int paddingTop = view.getPaddingTop() - i4;
            if (DialogsAdapter.this.folderId == 1 && size == 1 && ((ItemInternal) DialogsAdapter.this.itemInternals.get(0)).viewType == 19) {
                i3 = View.MeasureSpec.getSize(i2);
                if (i3 == 0) {
                    i3 = view.getMeasuredHeight();
                }
                if (i3 == 0) {
                    i3 = (AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                }
                if (DialogsAdapter.this.parentFragment.hasStories) {
                    i3 += AndroidUtilities.dp(81.0f);
                }
            } else {
                if (size != 0 && (paddingTop != 0 || z)) {
                    int size2 = View.MeasureSpec.getSize(i2);
                    if (size2 == 0) {
                        size2 = view.getMeasuredHeight();
                    }
                    if (size2 == 0) {
                        size2 = (AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                    }
                    int i5 = size2 - i4;
                    int dp = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f);
                    int i6 = 0;
                    for (int i7 = 0; i7 < size; i7++) {
                        if (((ItemInternal) DialogsAdapter.this.itemInternals.get(i7)).viewType == 0) {
                            if (((ItemInternal) DialogsAdapter.this.itemInternals.get(i7)).isForumCell && !z2) {
                                i6 += AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 86.0f : 91.0f);
                            }
                            i6 += dp;
                        } else {
                            if (((ItemInternal) DialogsAdapter.this.itemInternals.get(i7)).viewType != 1) {
                            }
                            i6 += dp;
                        }
                    }
                    int i8 = i6 + (size - 1);
                    if (DialogsAdapter.this.onlineContacts != null) {
                        i8 += (DialogsAdapter.this.onlineContacts.size() * AndroidUtilities.dp(58.0f)) + (DialogsAdapter.this.onlineContacts.size() - 1) + AndroidUtilities.dp(52.0f);
                    }
                    int i9 = z ? dp + 1 : 0;
                    if (i8 < i5) {
                        i3 = (i5 - i8) + i9;
                        if (paddingTop != 0) {
                            i3 -= AndroidUtilities.statusBarHeight;
                            if (DialogsAdapter.this.parentFragment.hasStories && !z2 && !DialogsAdapter.this.isTransitionSupport) {
                                i3 -= ActionBar.getCurrentActionBarHeight();
                            }
                        }
                    } else {
                        int i10 = i8 - i5;
                        if (i10 < i9) {
                            i3 = i9 - i10;
                            if (paddingTop != 0) {
                                i3 -= AndroidUtilities.statusBarHeight;
                                if (DialogsAdapter.this.parentFragment.hasStories && !z2 && !DialogsAdapter.this.isTransitionSupport) {
                                    i3 -= ActionBar.getCurrentActionBarHeight();
                                }
                            }
                        }
                    }
                }
                i3 = 0;
            }
            int i11 = i3 >= 0 ? i3 : 0;
            if (DialogsAdapter.this.isTransitionSupport) {
                i11 += AndroidUtilities.dp(1000.0f);
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(i11, 1073741824));
        }
    }

    public DialogsAdapter(DialogsActivity dialogsActivity, Context context, int i, int i2, boolean z, ArrayList arrayList, int i3, TLRPC$RequestPeerType tLRPC$RequestPeerType) {
        boolean z2 = true;
        this.mContext = context;
        this.parentFragment = dialogsActivity;
        this.dialogsType = i;
        this.folderId = i2;
        this.isOnlySelect = z;
        this.hasHints = (i2 == 0 && i == 0 && !z) ? false : false;
        this.selectedDialogs = arrayList;
        this.currentAccount = i3;
        if (i2 == 0) {
            this.preloader = new DialogsPreloader();
        }
        this.requestPeerType = tLRPC$RequestPeerType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$4() {
        this.parentFragment.setScrollDisabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$5(Float f) {
        this.parentFragment.setContactsAlpha(f.floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateViewHolder$3(View view) {
        MessagesController.getInstance(this.currentAccount).hintDialogs.clear();
        MessagesController.getGlobalMainSettings().edit().remove("installReferer").commit();
        notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002b  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x003d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0048 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0053 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x005c A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ int lambda$sortOnlineContacts$0(MessagesController messagesController, int i, TLRPC$TL_contact tLRPC$TL_contact, TLRPC$TL_contact tLRPC$TL_contact2) {
        int i2;
        int i3;
        TLRPC$User user = messagesController.getUser(Long.valueOf(tLRPC$TL_contact2.user_id));
        TLRPC$User user2 = messagesController.getUser(Long.valueOf(tLRPC$TL_contact.user_id));
        if (user != null) {
            if (user.self) {
                i2 = i + 50000;
            } else {
                TLRPC$UserStatus tLRPC$UserStatus = user.status;
                if (tLRPC$UserStatus != null) {
                    i2 = tLRPC$UserStatus.expires;
                }
            }
            if (user2 != null) {
                if (user2.self) {
                    i3 = i + 50000;
                } else {
                    TLRPC$UserStatus tLRPC$UserStatus2 = user2.status;
                    if (tLRPC$UserStatus2 != null) {
                        i3 = tLRPC$UserStatus2.expires;
                    }
                }
                if (i2 <= 0 && i3 > 0) {
                    if (i2 > i3) {
                        return 1;
                    }
                    return i2 < i3 ? -1 : 0;
                } else if (i2 >= 0 && i3 < 0) {
                    if (i2 > i3) {
                        return 1;
                    }
                    return i2 < i3 ? -1 : 0;
                } else if ((i2 < 0 || i3 <= 0) && (i2 != 0 || i3 == 0)) {
                    return (i3 >= 0 || i2 != 0) ? 1 : 0;
                } else {
                    return -1;
                }
            }
            i3 = 0;
            if (i2 <= 0) {
            }
            if (i2 >= 0) {
            }
            if (i2 < 0) {
            }
            if (i3 >= 0) {
            }
        }
        i2 = 0;
        if (user2 != null) {
        }
        i3 = 0;
        if (i2 <= 0) {
        }
        if (i2 >= 0) {
        }
        if (i2 < 0) {
        }
        if (i3 >= 0) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateList$1(Runnable runnable, ArrayList arrayList, DiffUtil.DiffResult diffResult) {
        if (this.isCalculatingDiff) {
            this.isCalculatingDiff = false;
            if (runnable != null) {
                runnable.run();
            }
            this.itemInternals = arrayList;
            diffResult.dispatchUpdatesTo(this);
            if (this.updateListPending) {
                this.updateListPending = false;
                updateList(runnable);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateList$2(DiffUtil.Callback callback, final Runnable runnable, final ArrayList arrayList) {
        final DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(callback);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsAdapter$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                DialogsAdapter.this.lambda$updateList$1(runnable, arrayList, calculateDiff);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x0280  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0296  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0343 A[LOOP:2: B:159:0x0343->B:168:0x036a, LOOP_START, PHI: r4 
      PHI: (r4v2 int) = (r4v1 int), (r4v4 int) binds: [B:158:0x0341, B:168:0x036a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:169:0x036c A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateItemList() {
        MessagesController.DialogFilter dialogFilter;
        ArrayList arrayList;
        ItemInternal itemInternal;
        ArrayList arrayList2;
        ItemInternal itemInternal2;
        ArrayList arrayList3;
        ItemInternal itemInternal3;
        int i;
        ArrayList arrayList4;
        ItemInternal itemInternal4;
        boolean z;
        ArrayList arrayList5;
        ItemInternal itemInternal5;
        TLRPC$RequestPeerType tLRPC$RequestPeerType;
        TLRPC$Dialog tLRPC$Dialog;
        ArrayList arrayList6;
        ItemInternal itemInternal6;
        int i2;
        ArrayList arrayList7;
        ItemInternal itemInternal7;
        ArrayList arrayList8;
        ItemInternal itemInternal8;
        int i3;
        this.itemInternals.clear();
        updateHasHints();
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ArrayList dialogsArray = this.parentFragment.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, this.dialogsListFrozen);
        if (dialogsArray == null) {
            dialogsArray = new ArrayList();
        }
        int size = dialogsArray.size();
        this.dialogsCount = size;
        int i4 = 0;
        this.isEmpty = false;
        if (size == 0 && this.parentFragment.isArchive()) {
            arrayList = this.itemInternals;
            itemInternal = new ItemInternal(19);
        } else {
            if (!this.hasHints && this.dialogsType == 0 && (i3 = this.folderId) == 0 && messagesController.isDialogsEndReached(i3) && !this.forceUpdatingContacts) {
                if (messagesController.getAllFoldersDialogsCount() <= 10 && ContactsController.getInstance(this.currentAccount).doneLoadingContacts && !ContactsController.getInstance(this.currentAccount).contacts.isEmpty()) {
                    this.onlineContacts = new ArrayList(ContactsController.getInstance(this.currentAccount).contacts);
                    long j = UserConfig.getInstance(this.currentAccount).clientUserId;
                    int size2 = this.onlineContacts.size();
                    int i5 = 0;
                    while (i5 < size2) {
                        long j2 = ((TLRPC$TL_contact) this.onlineContacts.get(i5)).user_id;
                        if (j2 == j || messagesController.dialogs_dict.get(j2) != null) {
                            this.onlineContacts.remove(i5);
                            i5--;
                            size2--;
                        }
                        i5++;
                    }
                    if (!this.onlineContacts.isEmpty()) {
                        sortOnlineContacts(false);
                    }
                }
                this.onlineContacts = null;
            }
            this.hasChatlistHint = false;
            int i6 = this.dialogsType;
            if ((i6 == 7 || i6 == 8) && (dialogFilter = messagesController.selectedDialogFilter[i6 - 7]) != null && dialogFilter.isChatlist()) {
                messagesController.checkChatlistFolderUpdate(dialogFilter.id, false);
                TL_chatlists$TL_chatlists_chatlistUpdates chatlistFolderUpdates = messagesController.getChatlistFolderUpdates(dialogFilter.id);
                if (chatlistFolderUpdates != null && chatlistFolderUpdates.missing_peers.size() > 0) {
                    this.hasChatlistHint = true;
                    this.itemInternals.add(new ItemInternal(chatlistFolderUpdates));
                }
            }
            if (this.requestPeerType != null) {
                this.itemInternals.add(new ItemInternal(15));
            }
            if (this.collapsedView || this.isTransitionSupport) {
                for (int i7 = 0; i7 < dialogsArray.size(); i7++) {
                    if (this.dialogsType == 2 && (dialogsArray.get(i7) instanceof DialogsActivity.DialogsHeader)) {
                        arrayList2 = this.itemInternals;
                        itemInternal2 = new ItemInternal(14, (TLRPC$Dialog) dialogsArray.get(i7));
                    } else {
                        arrayList2 = this.itemInternals;
                        itemInternal2 = new ItemInternal(0, (TLRPC$Dialog) dialogsArray.get(i7));
                    }
                    arrayList2.add(itemInternal2);
                }
                arrayList = this.itemInternals;
                itemInternal = new ItemInternal(10);
            } else {
                if (this.dialogsCount == 0 && this.forceUpdatingContacts) {
                    this.isEmpty = true;
                    if (this.requestPeerType != null) {
                        arrayList8 = this.itemInternals;
                        itemInternal8 = new ItemInternal(16);
                    } else {
                        arrayList8 = this.itemInternals;
                        itemInternal8 = new ItemInternal(5, dialogsEmptyType());
                    }
                    arrayList8.add(itemInternal8);
                    this.itemInternals.add(new ItemInternal(8));
                    this.itemInternals.add(new ItemInternal(7));
                    arrayList3 = this.itemInternals;
                    itemInternal3 = new ItemInternal(13);
                } else {
                    ArrayList arrayList9 = this.onlineContacts;
                    if (arrayList9 != null && !arrayList9.isEmpty() && (i = this.dialogsType) != 7 && i != 8) {
                        if (this.dialogsCount == 0) {
                            this.isEmpty = true;
                            if (this.requestPeerType != null) {
                                arrayList5 = this.itemInternals;
                                itemInternal5 = new ItemInternal(16);
                            } else {
                                arrayList5 = this.itemInternals;
                                itemInternal5 = new ItemInternal(5, dialogsEmptyType());
                            }
                            arrayList5.add(itemInternal5);
                            this.itemInternals.add(new ItemInternal(8));
                            arrayList4 = this.itemInternals;
                            itemInternal4 = new ItemInternal(7);
                        } else {
                            for (int i8 = 0; i8 < dialogsArray.size(); i8++) {
                                this.itemInternals.add(new ItemInternal(0, (TLRPC$Dialog) dialogsArray.get(i8)));
                            }
                            this.itemInternals.add(new ItemInternal(8));
                            arrayList4 = this.itemInternals;
                            itemInternal4 = new ItemInternal(7);
                        }
                        arrayList4.add(itemInternal4);
                        for (int i9 = 0; i9 < this.onlineContacts.size(); i9++) {
                            this.itemInternals.add(new ItemInternal(6, (TLRPC$TL_contact) this.onlineContacts.get(i9)));
                        }
                        this.itemInternals.add(new ItemInternal(10));
                        z = true;
                        tLRPC$RequestPeerType = this.requestPeerType;
                        if (!(tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast)) {
                        }
                        this.itemInternals.add(new ItemInternal(12));
                        if (!z) {
                        }
                        if (messagesController.hiddenUndoChats.isEmpty()) {
                        }
                    } else if (this.hasHints) {
                        int size3 = MessagesController.getInstance(this.currentAccount).hintDialogs.size();
                        this.itemInternals.add(new ItemInternal(2));
                        for (int i10 = 0; i10 < size3; i10++) {
                            this.itemInternals.add(new ItemInternal(4, MessagesController.getInstance(this.currentAccount).hintDialogs.get(i10)));
                        }
                        arrayList3 = this.itemInternals;
                        itemInternal3 = new ItemInternal(3);
                    } else {
                        int i11 = this.dialogsType;
                        if (i11 != 11 && i11 != 13) {
                            if (i11 == 12) {
                                arrayList3 = this.itemInternals;
                                itemInternal3 = new ItemInternal(7);
                            }
                            z = false;
                            tLRPC$RequestPeerType = this.requestPeerType;
                            if ((!(tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast) || (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeChat)) && this.dialogsCount > 0) {
                                this.itemInternals.add(new ItemInternal(12));
                            }
                            if (!z) {
                                for (int i12 = 0; i12 < dialogsArray.size(); i12++) {
                                    if (this.dialogsType == 2 && (dialogsArray.get(i12) instanceof DialogsActivity.DialogsHeader)) {
                                        arrayList7 = this.itemInternals;
                                        itemInternal7 = new ItemInternal(14, (TLRPC$Dialog) dialogsArray.get(i12));
                                    } else {
                                        arrayList7 = this.itemInternals;
                                        itemInternal7 = new ItemInternal(0, (TLRPC$Dialog) dialogsArray.get(i12));
                                    }
                                    arrayList7.add(itemInternal7);
                                }
                                if (this.forceShowEmptyCell || (i2 = this.dialogsType) == 7 || i2 == 8 || MessagesController.getInstance(this.currentAccount).isDialogsEndReached(this.folderId)) {
                                    int i13 = this.dialogsCount;
                                    if (i13 == 0) {
                                        this.isEmpty = true;
                                        if (this.requestPeerType != null) {
                                            arrayList6 = this.itemInternals;
                                            itemInternal6 = new ItemInternal(16);
                                        } else {
                                            arrayList6 = this.itemInternals;
                                            itemInternal6 = new ItemInternal(5, dialogsEmptyType());
                                        }
                                    } else {
                                        if (this.folderId == 0 && i13 > 10 && this.dialogsType == 0) {
                                            this.itemInternals.add(new ItemInternal(11));
                                        }
                                        arrayList6 = this.itemInternals;
                                        itemInternal6 = new ItemInternal(10);
                                    }
                                } else {
                                    if (this.dialogsCount != 0) {
                                        this.itemInternals.add(new ItemInternal(1));
                                    }
                                    arrayList6 = this.itemInternals;
                                    itemInternal6 = new ItemInternal(10);
                                }
                                arrayList6.add(itemInternal6);
                            }
                            if (messagesController.hiddenUndoChats.isEmpty()) {
                                return;
                            }
                            while (i4 < this.itemInternals.size()) {
                                ItemInternal itemInternal9 = (ItemInternal) this.itemInternals.get(i4);
                                if (itemInternal9.viewType == 0 && (tLRPC$Dialog = itemInternal9.dialog) != null && messagesController.isHiddenByUndo(tLRPC$Dialog.id)) {
                                    this.itemInternals.remove(i4);
                                    i4--;
                                }
                                i4++;
                            }
                            return;
                        }
                        this.itemInternals.add(new ItemInternal(7));
                        arrayList3 = this.itemInternals;
                        itemInternal3 = new ItemInternal(12);
                    }
                }
                arrayList3.add(itemInternal3);
                z = false;
                tLRPC$RequestPeerType = this.requestPeerType;
                if (!(tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast)) {
                }
                this.itemInternals.add(new ItemInternal(12));
                if (!z) {
                }
                if (messagesController.hiddenUndoChats.isEmpty()) {
                }
            }
        }
        arrayList.add(itemInternal);
    }

    @Override // org.telegram.ui.Cells.DialogCell.DialogCellDelegate
    public boolean canClickButtonInside() {
        return this.selectedDialogs.isEmpty();
    }

    public int dialogsEmptyType() {
        int i = this.dialogsType;
        if (i == 7 || i == 8) {
            return MessagesController.getInstance(this.currentAccount).isDialogsEndReached(this.folderId) ? 2 : 3;
        } else if (this.folderId == 1) {
            return 2;
        } else {
            return this.onlineContacts != null ? 1 : 0;
        }
    }

    public void didDatabaseCleared() {
        DialogsPreloader dialogsPreloader = this.preloader;
        if (dialogsPreloader != null) {
            dialogsPreloader.clear();
        }
    }

    public int findDialogPosition(long j) {
        for (int i = 0; i < this.itemInternals.size(); i++) {
            if (((ItemInternal) this.itemInternals.get(i)).dialog != null && ((ItemInternal) this.itemInternals.get(i)).dialog.id == j) {
                return i;
            }
        }
        return -1;
    }

    public int fixPosition(int i) {
        if (this.hasChatlistHint) {
            i--;
        }
        if (this.hasHints) {
            i -= MessagesController.getInstance(this.currentAccount).hintDialogs.size() + 2;
        }
        int i2 = this.dialogsType;
        return (i2 == 11 || i2 == 13) ? i - 2 : i2 == 12 ? i - 1 : i;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002b  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int fixScrollGap(RecyclerListView recyclerListView, int i, int i2, boolean z, boolean z2, boolean z3, boolean z4) {
        float f;
        getItemCount();
        int dp = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f);
        recyclerListView.getPaddingTop();
        int paddingTop = ((recyclerListView.getPaddingTop() + i2) - (i * dp)) - i;
        if (!z2) {
            f = z3 ? 44.0f : 44.0f;
            if (z) {
                paddingTop += dp;
            }
            int paddingTop2 = recyclerListView.getPaddingTop();
            return paddingTop <= paddingTop2 ? (i2 + paddingTop2) - paddingTop : i2;
        }
        f = 81.0f;
        AndroidUtilities.dp(f);
        if (z) {
        }
        int paddingTop22 = recyclerListView.getPaddingTop();
        if (paddingTop <= paddingTop22) {
        }
    }

    public ViewPager getArchiveHintCellPager() {
        return null;
    }

    public TL_chatlists$TL_chatlists_chatlistUpdates getChatlistUpdate() {
        ItemInternal itemInternal = (ItemInternal) this.itemInternals.get(0);
        if (itemInternal == null || itemInternal.viewType != 17) {
            return null;
        }
        return itemInternal.chatlistUpdates;
    }

    public int getCurrentCount() {
        return this.currentCount;
    }

    public int getDialogsCount() {
        return this.dialogsCount;
    }

    public boolean getDialogsListIsFrozen() {
        return this.dialogsListFrozen;
    }

    public int getDialogsType() {
        return this.dialogsType;
    }

    public TLObject getItem(int i) {
        if (i >= 0 && i < this.itemInternals.size()) {
            if (((ItemInternal) this.itemInternals.get(i)).dialog != null) {
                return ((ItemInternal) this.itemInternals.get(i)).dialog;
            }
            if (((ItemInternal) this.itemInternals.get(i)).contact != null) {
                return MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(((ItemInternal) this.itemInternals.get(i)).contact.user_id));
            }
            if (((ItemInternal) this.itemInternals.get(i)).recentMeUrl != null) {
                return ((ItemInternal) this.itemInternals.get(i)).recentMeUrl;
            }
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int size = this.itemInternals.size();
        this.currentCount = size;
        return size;
    }

    public int getItemHeight(int i) {
        int dp = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f);
        if (((ItemInternal) this.itemInternals.get(i)).viewType == 0) {
            if (!((ItemInternal) this.itemInternals.get(i)).isForumCell || this.collapsedView) {
                return dp;
            }
            return AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 86.0f : 91.0f);
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return ((ItemInternal) this.itemInternals.get(i)).stableId;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return ((ItemInternal) this.itemInternals.get(i)).viewType;
    }

    public boolean isDataSetChanged() {
        return true;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        return (itemViewType == 1 || itemViewType == 5 || itemViewType == 3 || itemViewType == 8 || itemViewType == 7 || itemViewType == 10 || itemViewType == 11 || itemViewType == 13 || itemViewType == 15 || itemViewType == 16 || itemViewType == 18 || itemViewType == 19) ? false : true;
    }

    public void moveDialogs(RecyclerListView recyclerListView, int i, int i2) {
        ArrayList dialogsArray = this.parentFragment.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, false);
        int fixPosition = fixPosition(i);
        int fixPosition2 = fixPosition(i2);
        TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) dialogsArray.get(fixPosition);
        TLRPC$Dialog tLRPC$Dialog2 = (TLRPC$Dialog) dialogsArray.get(fixPosition2);
        int i3 = this.dialogsType;
        if (i3 == 7 || i3 == 8) {
            MessagesController.DialogFilter dialogFilter = MessagesController.getInstance(this.currentAccount).selectedDialogFilter[this.dialogsType == 8 ? (char) 1 : (char) 0];
            int i4 = dialogFilter.pinnedDialogs.get(tLRPC$Dialog.id);
            dialogFilter.pinnedDialogs.put(tLRPC$Dialog.id, dialogFilter.pinnedDialogs.get(tLRPC$Dialog2.id));
            dialogFilter.pinnedDialogs.put(tLRPC$Dialog2.id, i4);
        } else {
            int i5 = tLRPC$Dialog.pinnedNum;
            tLRPC$Dialog.pinnedNum = tLRPC$Dialog2.pinnedNum;
            tLRPC$Dialog2.pinnedNum = i5;
        }
        Collections.swap(dialogsArray, fixPosition, fixPosition2);
        updateList(null);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void notifyDataSetChanged() {
        if (this.isCalculatingDiff) {
            this.itemInternals = new ArrayList();
        }
        this.isCalculatingDiff = false;
        updateItemList();
        super.notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void notifyItemMoved(int i, int i2) {
        super.notifyItemMoved(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onArchiveSettingsClick() {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TLRPC$Chat tLRPC$Chat;
        String str;
        TLRPC$User tLRPC$User;
        String str2;
        String str3;
        int i2;
        int i3;
        String string;
        String str4;
        TLRPC$Chat chat;
        HeaderCell headerCell;
        int i4;
        String string2;
        int i5;
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == 0) {
            TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) getItem(i);
            TLRPC$Dialog tLRPC$Dialog2 = (TLRPC$Dialog) getItem(i + 1);
            int i6 = this.dialogsType;
            if (i6 == 2 || i6 == 15) {
                ProfileSearchCell profileSearchCell = (ProfileSearchCell) viewHolder.itemView;
                long dialogId = profileSearchCell.getDialogId();
                if (tLRPC$Dialog.id != 0) {
                    tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog.id));
                    if (tLRPC$Chat != null && tLRPC$Chat.migrated_to != null && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$Chat.migrated_to.channel_id))) != null) {
                        tLRPC$Chat = chat;
                    }
                } else {
                    tLRPC$Chat = null;
                }
                if (tLRPC$Chat != null) {
                    String str5 = tLRPC$Chat.title;
                    if (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup) {
                        i2 = tLRPC$Chat.participants_count;
                        if (i2 != 0) {
                            str4 = "Members";
                            string = LocaleController.formatPluralStringComma(str4, i2);
                        } else if (tLRPC$Chat.has_geo) {
                            string = LocaleController.getString(R.string.MegaLocation);
                        } else {
                            i3 = !ChatObject.isPublic(tLRPC$Chat) ? R.string.MegaPrivate : R.string.MegaPublic;
                            string = LocaleController.getString(i3).toLowerCase();
                        }
                    } else {
                        i2 = tLRPC$Chat.participants_count;
                        if (i2 != 0) {
                            str4 = "Subscribers";
                            string = LocaleController.formatPluralStringComma(str4, i2);
                        } else {
                            i3 = !ChatObject.isPublic(tLRPC$Chat) ? R.string.ChannelPrivate : R.string.ChannelPublic;
                            string = LocaleController.getString(i3).toLowerCase();
                        }
                    }
                    str3 = string;
                    str2 = str5;
                    tLRPC$User = tLRPC$Chat;
                } else {
                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$Dialog.id));
                    str = "";
                    if (user != null) {
                        String userName = UserObject.getUserName(user);
                        str = UserObject.isReplyUser(user) ? "" : user.bot ? LocaleController.getString(R.string.Bot) : LocaleController.formatUserStatus(this.currentAccount, user);
                        tLRPC$User = user;
                        str2 = userName;
                    } else {
                        tLRPC$User = null;
                        str2 = null;
                    }
                    str3 = str;
                }
                profileSearchCell.useSeparator = tLRPC$Dialog2 != null;
                profileSearchCell.setData(tLRPC$User, null, str2, str3, false, false);
                profileSearchCell.setChecked(this.selectedDialogs.contains(Long.valueOf(profileSearchCell.getDialogId())), dialogId == profileSearchCell.getDialogId());
            } else {
                DialogCell dialogCell = (DialogCell) viewHolder.itemView;
                dialogCell.useSeparator = tLRPC$Dialog2 != null;
                dialogCell.fullSeparator = (!tLRPC$Dialog.pinned || tLRPC$Dialog2 == null || tLRPC$Dialog2.pinned) ? false : true;
                if (i6 == 0 && AndroidUtilities.isTablet()) {
                    dialogCell.setDialogSelected(tLRPC$Dialog.id == this.openedDialogId);
                }
                dialogCell.setChecked(this.selectedDialogs.contains(Long.valueOf(tLRPC$Dialog.id)), false);
                dialogCell.setDialog(tLRPC$Dialog, this.dialogsType, this.folderId);
                dialogCell.checkHeight();
                boolean z = dialogCell.collapsed;
                boolean z2 = this.collapsedView;
                if (z != z2) {
                    dialogCell.collapsed = z2;
                    dialogCell.requestLayout();
                }
                DialogsPreloader dialogsPreloader = this.preloader;
                if (dialogsPreloader != null && i < 10) {
                    dialogsPreloader.add(tLRPC$Dialog.id);
                }
            }
        } else if (itemViewType == 4) {
            ((DialogMeUrlCell) viewHolder.itemView).setRecentMeUrl((TLRPC$RecentMeUrl) getItem(i));
        } else if (itemViewType == 5) {
            DialogsEmptyCell dialogsEmptyCell = (DialogsEmptyCell) viewHolder.itemView;
            int i7 = this.lastDialogsEmptyType;
            int dialogsEmptyType = dialogsEmptyType();
            this.lastDialogsEmptyType = dialogsEmptyType;
            dialogsEmptyCell.setType(dialogsEmptyType, this.isOnlySelect);
            int i8 = this.dialogsType;
            if (i8 != 7 && i8 != 8) {
                dialogsEmptyCell.setOnUtyanAnimationEndListener(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsAdapter$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsAdapter.this.lambda$onBindViewHolder$4();
                    }
                });
                dialogsEmptyCell.setOnUtyanAnimationUpdateListener(new Consumer() { // from class: org.telegram.ui.Adapters.DialogsAdapter$$ExternalSyntheticLambda2
                    @Override // androidx.core.util.Consumer
                    public final void accept(Object obj) {
                        DialogsAdapter.this.lambda$onBindViewHolder$5((Float) obj);
                    }
                });
                if (!dialogsEmptyCell.isUtyanAnimationTriggered() && this.dialogsCount == 0) {
                    this.parentFragment.setContactsAlpha(0.0f);
                    this.parentFragment.setScrollDisabled(true);
                }
                if (this.onlineContacts == null || i7 != 0) {
                    if (this.forceUpdatingContacts) {
                        if (this.dialogsCount == 0) {
                            dialogsEmptyCell.startUtyanCollapseAnimation(false);
                        }
                    } else if (dialogsEmptyCell.isUtyanAnimationTriggered() && this.lastDialogsEmptyType == 0) {
                        dialogsEmptyCell.startUtyanExpandAnimation();
                    }
                } else if (!dialogsEmptyCell.isUtyanAnimationTriggered()) {
                    dialogsEmptyCell.startUtyanCollapseAnimation(true);
                }
            }
        } else if (itemViewType != 6) {
            if (itemViewType == 7) {
                headerCell = (HeaderCell) viewHolder.itemView;
                int i9 = this.dialogsType;
                i4 = (i9 == 11 || i9 == 12 || i9 == 13) ? i == 0 ? R.string.ImportHeader : R.string.ImportHeaderContacts : (this.dialogsCount == 0 && this.forceUpdatingContacts) ? R.string.ConnectingYourContacts : R.string.YourContacts;
            } else if (itemViewType == 11) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                textInfoPrivacyCell.setText(LocaleController.getString(R.string.TapOnThePencil));
                if (this.arrowDrawable == null) {
                    Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.arrow_newchat);
                    this.arrowDrawable = drawable;
                    drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4), PorterDuff.Mode.MULTIPLY));
                }
                TextView textView = textInfoPrivacyCell.getTextView();
                textView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                DialogsActivity dialogsActivity = this.parentFragment;
                textView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (dialogsActivity == null || !dialogsActivity.storiesEnabled) ? this.arrowDrawable : null, (Drawable) null);
                textView.getLayoutParams().width = -2;
            } else if (itemViewType != 12) {
                switch (itemViewType) {
                    case 14:
                        headerCell = (HeaderCell) viewHolder.itemView;
                        headerCell.setTextSize(14.0f);
                        headerCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
                        headerCell.setBackgroundColor(Theme.getColor(Theme.key_graySection));
                        int i10 = ((DialogsActivity.DialogsHeader) getItem(i)).headerType;
                        if (i10 == 0) {
                            i4 = R.string.MyChannels;
                            break;
                        } else if (i10 == 1) {
                            i4 = R.string.MyGroups;
                            break;
                        } else if (i10 == 2) {
                            i4 = R.string.FilterGroups;
                            break;
                        }
                        break;
                    case 15:
                        ((RequestPeerRequirementsCell) viewHolder.itemView).set(this.requestPeerType);
                        break;
                    case 16:
                        ((DialogsRequestedEmptyCell) viewHolder.itemView).set(this.requestPeerType);
                        break;
                    case 17:
                        DialogsHintCell dialogsHintCell = (DialogsHintCell) viewHolder.itemView;
                        TL_chatlists$TL_chatlists_chatlistUpdates tL_chatlists$TL_chatlists_chatlistUpdates = ((ItemInternal) this.itemInternals.get(i)).chatlistUpdates;
                        if (tL_chatlists$TL_chatlists_chatlistUpdates != null) {
                            int size = tL_chatlists$TL_chatlists_chatlistUpdates.missing_peers.size();
                            dialogsHintCell.setText(AndroidUtilities.replaceSingleTag(LocaleController.formatPluralString("FolderUpdatesTitle", size, new Object[0]), Theme.key_windowBackgroundWhiteValueText, 0, null), LocaleController.formatPluralString("FolderUpdatesSubtitle", size, new Object[0]));
                            break;
                        }
                        break;
                }
            } else {
                View view = viewHolder.itemView;
                if (!(view instanceof TextCell)) {
                    return;
                }
                TextCell textCell = (TextCell) view;
                int i11 = Theme.key_windowBackgroundWhiteBlueText4;
                textCell.setColors(i11, i11);
                TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
                if (tLRPC$RequestPeerType != null) {
                    if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast) {
                        string2 = LocaleController.getString(R.string.CreateChannelForThis);
                        i5 = R.drawable.msg_channel_create;
                    } else {
                        string2 = LocaleController.getString(R.string.CreateGroupForThis);
                        i5 = R.drawable.msg_groups_create;
                    }
                    textCell.setTextAndIcon((CharSequence) string2, i5, true);
                } else {
                    textCell.setTextAndIcon(LocaleController.getString(R.string.CreateGroupForImport), R.drawable.msg_groups_create, this.dialogsCount != 0);
                }
                textCell.setIsInDialogs();
                textCell.setOffsetFromImage(75);
            }
            headerCell.setText(LocaleController.getString(i4));
        } else {
            ((UserCell) viewHolder.itemView).setData((TLRPC$User) getItem(i), null, null, 0);
        }
        if (i >= this.dialogsCount + 1) {
            viewHolder.itemView.setAlpha(1.0f);
        }
    }

    @Override // org.telegram.ui.Cells.DialogCell.DialogCellDelegate
    public void onButtonClicked(DialogCell dialogCell) {
    }

    @Override // org.telegram.ui.Cells.DialogCell.DialogCellDelegate
    public void onButtonLongPress(DialogCell dialogCell) {
    }

    public void onCreateGroupForThisClick() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x0201, code lost:
        if (r19.dialogsType == 15) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0203, code lost:
        r5.setBackgroundColor(org.telegram.ui.ActionBar.Theme.getColor(org.telegram.ui.ActionBar.Theme.key_windowBackgroundWhite));
        r5 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0016, code lost:
        if (r19.dialogsType == 15) goto L14;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [org.telegram.ui.Components.FlickerLoadingView] */
    /* JADX WARN: Type inference failed for: r4v15 */
    /* JADX WARN: Type inference failed for: r4v16, types: [android.view.View] */
    /* JADX WARN: Type inference failed for: r4v2, types: [org.telegram.ui.Cells.HeaderCell, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r4v29 */
    /* JADX WARN: Type inference failed for: r4v30 */
    /* JADX WARN: Type inference failed for: r5v35, types: [org.telegram.ui.Adapters.DialogsAdapter$5] */
    /* JADX WARN: Type inference failed for: r5v4, types: [org.telegram.ui.Cells.DialogCell] */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewGroup viewGroup2;
        ?? flickerLoadingView;
        View view;
        ViewGroup.LayoutParams createFrame;
        CombinedDrawable combinedDrawable;
        HeaderCell headerCell;
        ViewGroup viewGroup3;
        ViewGroup viewGroup4;
        switch (i) {
            case 0:
                int i2 = this.dialogsType;
                if (i2 == 2 || i2 == 15) {
                    viewGroup2 = new ProfileSearchCell(this.mContext);
                } else {
                    ?? dialogCell = new DialogCell(this.parentFragment, this.mContext, true, false, this.currentAccount, null);
                    dialogCell.setArchivedPullAnimation(this.pullForegroundDrawable);
                    dialogCell.setPreloader(this.preloader);
                    dialogCell.setDialogCellDelegate(this);
                    dialogCell.setIsTransitionSupport(this.isTransitionSupport);
                    viewGroup2 = dialogCell;
                }
                headerCell = viewGroup2;
                viewGroup4 = viewGroup2;
                break;
            case 1:
            case 13:
                flickerLoadingView = new FlickerLoadingView(this.mContext);
                flickerLoadingView.setIsSingleCell(true);
                int i3 = i == 13 ? 18 : 7;
                flickerLoadingView.setViewType(i3);
                if (i3 == 18) {
                    flickerLoadingView.setIgnoreHeightCheck(true);
                }
                if (i == 13) {
                    flickerLoadingView.setItemsCount((int) ((AndroidUtilities.displaySize.y * 0.5f) / AndroidUtilities.dp(64.0f)));
                }
                headerCell = flickerLoadingView;
                break;
            case 2:
                flickerLoadingView = new HeaderCell(this.mContext);
                flickerLoadingView.setText(LocaleController.getString(R.string.RecentlyViewed));
                TextView textView = new TextView(this.mContext);
                textView.setTextSize(1, 15.0f);
                textView.setTypeface(AndroidUtilities.bold());
                textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
                textView.setText(LocaleController.getString(R.string.RecentlyViewedHide));
                textView.setGravity((LocaleController.isRTL ? 3 : 5) | 16);
                flickerLoadingView.addView(textView, LayoutHelper.createFrame(-1, -1.0f, (LocaleController.isRTL ? 3 : 5) | 48, 17.0f, 15.0f, 17.0f, 0.0f));
                textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Adapters.DialogsAdapter$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        DialogsAdapter.this.lambda$onCreateViewHolder$3(view2);
                    }
                });
                headerCell = flickerLoadingView;
                break;
            case 3:
                ViewGroup viewGroup5 = new FrameLayout(this.mContext) { // from class: org.telegram.ui.Adapters.DialogsAdapter.2
                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i4, int i5) {
                        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i4), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(12.0f), 1073741824));
                    }
                };
                viewGroup5.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
                view = new View(this.mContext);
                view.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                createFrame = LayoutHelper.createFrame(-1, -1.0f);
                viewGroup3 = viewGroup5;
                viewGroup3.addView(view, createFrame);
                headerCell = viewGroup3;
                break;
            case 4:
                headerCell = new DialogMeUrlCell(this.mContext);
                break;
            case 5:
                headerCell = new DialogsEmptyCell(this.mContext);
                break;
            case 6:
                headerCell = new UserCell(this.mContext, 8, 0, false);
                break;
            case 7:
                ViewGroup headerCell2 = new HeaderCell(this.mContext);
                headerCell2.setPadding(0, 0, 0, AndroidUtilities.dp(12.0f));
                headerCell = headerCell2;
                break;
            case 8:
                ShadowSectionCell shadowSectionCell = new ShadowSectionCell(this.mContext);
                combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                flickerLoadingView = shadowSectionCell;
                combinedDrawable.setFullsize(true);
                flickerLoadingView.setBackgroundDrawable(combinedDrawable);
                headerCell = flickerLoadingView;
                break;
            case 9:
            case 12:
            default:
                ViewGroup textCell = new TextCell(this.mContext);
                headerCell = textCell;
                viewGroup4 = textCell;
                break;
            case 10:
                headerCell = new LastEmptyView(this.mContext);
                break;
            case 11:
                TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext) { // from class: org.telegram.ui.Adapters.DialogsAdapter.4
                    private long lastUpdateTime;
                    private float moveProgress;
                    private int movement;
                    private int originalX;
                    private int originalY;

                    @Override // org.telegram.ui.Cells.TextInfoPrivacyCell
                    protected void afterTextDraw() {
                        if (DialogsAdapter.this.arrowDrawable != null) {
                            Rect bounds = DialogsAdapter.this.arrowDrawable.getBounds();
                            Drawable drawable = DialogsAdapter.this.arrowDrawable;
                            int i4 = this.originalX;
                            drawable.setBounds(i4, this.originalY, bounds.width() + i4, this.originalY + bounds.height());
                        }
                    }

                    @Override // org.telegram.ui.Cells.TextInfoPrivacyCell
                    protected void onTextDraw() {
                        if (DialogsAdapter.this.arrowDrawable != null) {
                            Rect bounds = DialogsAdapter.this.arrowDrawable.getBounds();
                            int dp = (int) (this.moveProgress * AndroidUtilities.dp(3.0f));
                            this.originalX = bounds.left;
                            this.originalY = bounds.top;
                            DialogsAdapter.this.arrowDrawable.setBounds(this.originalX + dp, this.originalY + AndroidUtilities.dp(1.0f), this.originalX + dp + bounds.width(), this.originalY + AndroidUtilities.dp(1.0f) + bounds.height());
                            long elapsedRealtime = SystemClock.elapsedRealtime();
                            long j = elapsedRealtime - this.lastUpdateTime;
                            if (j > 17) {
                                j = 17;
                            }
                            this.lastUpdateTime = elapsedRealtime;
                            if (this.movement == 0) {
                                float f = this.moveProgress + (((float) j) / 664.0f);
                                this.moveProgress = f;
                                if (f >= 1.0f) {
                                    this.movement = 1;
                                    this.moveProgress = 1.0f;
                                }
                            } else {
                                float f2 = this.moveProgress - (((float) j) / 664.0f);
                                this.moveProgress = f2;
                                if (f2 <= 0.0f) {
                                    this.movement = 0;
                                    this.moveProgress = 0.0f;
                                }
                            }
                            getTextView().invalidate();
                        }
                    }
                };
                combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                flickerLoadingView = textInfoPrivacyCell;
                combinedDrawable.setFullsize(true);
                flickerLoadingView.setBackgroundDrawable(combinedDrawable);
                headerCell = flickerLoadingView;
                break;
            case 14:
                HeaderCell headerCell3 = new HeaderCell(this.mContext, Theme.key_graySectionText, 16, 0, false);
                headerCell3.setHeight(32);
                headerCell3.setClickable(false);
                headerCell = headerCell3;
                break;
            case 15:
                headerCell = new RequestPeerRequirementsCell(this.mContext);
                break;
            case 16:
                headerCell = new DialogsRequestedEmptyCell(this.mContext) { // from class: org.telegram.ui.Adapters.DialogsAdapter.3
                    @Override // org.telegram.ui.Cells.DialogsRequestedEmptyCell
                    protected void onButtonClick() {
                        DialogsAdapter.this.onCreateGroupForThisClick();
                    }
                };
                break;
            case 17:
                headerCell = new DialogsHintCell(this.mContext, null);
                break;
            case 18:
                headerCell = new View(this.mContext) { // from class: org.telegram.ui.Adapters.DialogsAdapter.5
                    @Override // android.view.View
                    protected void onMeasure(int i4, int i5) {
                        super.onMeasure(i4, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(81.0f), 1073741824));
                    }
                };
                break;
            case 19:
                ViewGroup lastEmptyView = new LastEmptyView(this.mContext);
                view = new ArchiveHelp(this.mContext, this.currentAccount, null, new Runnable() { // from class: org.telegram.ui.Adapters.DialogsAdapter$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsAdapter.this.onArchiveSettingsClick();
                    }
                }, null);
                createFrame = LayoutHelper.createFrame(-1, -1.0f, 17, 0.0f, -40.0f, 0.0f, 0.0f);
                viewGroup3 = lastEmptyView;
                viewGroup3.addView(view, createFrame);
                headerCell = viewGroup3;
                break;
        }
        headerCell.setLayoutParams(new RecyclerView.LayoutParams(-1, (i == 5 || i == 19) ? -1 : -2));
        return new RecyclerListView.Holder(headerCell);
    }

    public void onReorderStateChanged(boolean z) {
        this.isReordering = z;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        if (view instanceof DialogCell) {
            DialogCell dialogCell = (DialogCell) view;
            dialogCell.onReorderStateChanged(this.isReordering, false);
            dialogCell.checkCurrentDialogIndex(this.dialogsListFrozen);
            dialogCell.setChecked(this.selectedDialogs.contains(Long.valueOf(dialogCell.getDialogId())), false);
        }
    }

    @Override // org.telegram.ui.Cells.DialogCell.DialogCellDelegate
    public void openHiddenStories() {
        StoriesController storiesController = MessagesController.getInstance(this.currentAccount).getStoriesController();
        if (storiesController.getHiddenList().isEmpty()) {
            return;
        }
        boolean z = storiesController.getUnreadState(DialogObject.getPeerDialogId(((TL_stories$PeerStories) storiesController.getHiddenList().get(0)).peer)) != 0;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < storiesController.getHiddenList().size(); i++) {
            long peerDialogId = DialogObject.getPeerDialogId(((TL_stories$PeerStories) storiesController.getHiddenList().get(i)).peer);
            if (!z || storiesController.getUnreadState(peerDialogId) != 0) {
                arrayList.add(Long.valueOf(peerDialogId));
            }
        }
        this.parentFragment.getOrCreateStoryViewer().open(this.mContext, null, arrayList, 0, null, null, StoriesListPlaceProvider.of(this.recyclerListView, true), false);
    }

    @Override // org.telegram.ui.Cells.DialogCell.DialogCellDelegate
    public void openStory(DialogCell dialogCell, Runnable runnable) {
        MessagesController.getInstance(this.currentAccount);
        if (MessagesController.getInstance(this.currentAccount).getStoriesController().hasStories(dialogCell.getDialogId())) {
            this.parentFragment.getOrCreateStoryViewer().doOnAnimationReady(runnable);
            this.parentFragment.getOrCreateStoryViewer().open(this.parentFragment.getContext(), dialogCell.getDialogId(), StoriesListPlaceProvider.of((RecyclerListView) dialogCell.getParent()));
        }
    }

    public void pause() {
        DialogsPreloader dialogsPreloader = this.preloader;
        if (dialogsPreloader != null) {
            dialogsPreloader.pause();
        }
    }

    public void resume() {
        DialogsPreloader dialogsPreloader = this.preloader;
        if (dialogsPreloader != null) {
            dialogsPreloader.resume();
        }
    }

    public void setArchivedPullDrawable(PullForegroundDrawable pullForegroundDrawable) {
        this.pullForegroundDrawable = pullForegroundDrawable;
    }

    public void setCollapsedView(boolean z, RecyclerListView recyclerListView) {
        this.collapsedView = z;
        for (int i = 0; i < recyclerListView.getChildCount(); i++) {
            if (recyclerListView.getChildAt(i) instanceof DialogCell) {
                ((DialogCell) recyclerListView.getChildAt(i)).collapsed = z;
            }
        }
        for (int i2 = 0; i2 < recyclerListView.getCachedChildCount(); i2++) {
            if (recyclerListView.getCachedChildAt(i2) instanceof DialogCell) {
                ((DialogCell) recyclerListView.getCachedChildAt(i2)).collapsed = z;
            }
        }
        for (int i3 = 0; i3 < recyclerListView.getHiddenChildCount(); i3++) {
            if (recyclerListView.getHiddenChildAt(i3) instanceof DialogCell) {
                ((DialogCell) recyclerListView.getHiddenChildAt(i3)).collapsed = z;
            }
        }
        for (int i4 = 0; i4 < recyclerListView.getAttachedScrapChildCount(); i4++) {
            if (recyclerListView.getAttachedScrapChildAt(i4) instanceof DialogCell) {
                ((DialogCell) recyclerListView.getAttachedScrapChildAt(i4)).collapsed = z;
            }
        }
    }

    public void setDialogsListFrozen(boolean z) {
        this.dialogsListFrozen = z;
    }

    public void setDialogsType(int i) {
        this.dialogsType = i;
        notifyDataSetChanged();
    }

    public void setForceShowEmptyCell(boolean z) {
        this.forceShowEmptyCell = z;
    }

    public void setForceUpdatingContacts(boolean z) {
        this.forceUpdatingContacts = z;
    }

    public void setIsTransitionSupport() {
        this.isTransitionSupport = true;
    }

    public void setOpenedDialogId(long j) {
        this.openedDialogId = j;
    }

    public void setRecyclerListView(RecyclerListView recyclerListView) {
        this.recyclerListView = recyclerListView;
    }

    @Override // org.telegram.ui.Cells.DialogCell.DialogCellDelegate
    public void showChatPreview(DialogCell dialogCell) {
        this.parentFragment.showChatPreview(dialogCell);
    }

    public void sortOnlineContacts(boolean z) {
        if (this.onlineContacts != null) {
            if (!z || SystemClock.elapsedRealtime() - this.lastSortTime >= 2000) {
                this.lastSortTime = SystemClock.elapsedRealtime();
                try {
                    final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                    final MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                    Collections.sort(this.onlineContacts, new Comparator() { // from class: org.telegram.ui.Adapters.DialogsAdapter$$ExternalSyntheticLambda0
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            int lambda$sortOnlineContacts$0;
                            lambda$sortOnlineContacts$0 = DialogsAdapter.lambda$sortOnlineContacts$0(MessagesController.this, currentTime, (TLRPC$TL_contact) obj, (TLRPC$TL_contact) obj2);
                            return lambda$sortOnlineContacts$0;
                        }
                    });
                    if (z) {
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public void updateHasHints() {
        this.hasHints = this.folderId == 0 && this.dialogsType == 0 && !this.isOnlySelect && !MessagesController.getInstance(this.currentAccount).hintDialogs.isEmpty();
    }

    public void updateList(final Runnable runnable) {
        if (this.isCalculatingDiff) {
            this.updateListPending = true;
            return;
        }
        this.isCalculatingDiff = true;
        ArrayList arrayList = new ArrayList();
        this.oldItems = arrayList;
        arrayList.addAll(this.itemInternals);
        updateItemList();
        final ArrayList arrayList2 = new ArrayList(this.itemInternals);
        this.itemInternals = this.oldItems;
        final DiffUtil.Callback callback = new DiffUtil.Callback() { // from class: org.telegram.ui.Adapters.DialogsAdapter.1
            @Override // androidx.recyclerview.widget.DiffUtil.Callback
            public boolean areContentsTheSame(int i, int i2) {
                return ((ItemInternal) DialogsAdapter.this.oldItems.get(i)).viewType == ((ItemInternal) arrayList2.get(i2)).viewType;
            }

            @Override // androidx.recyclerview.widget.DiffUtil.Callback
            public boolean areItemsTheSame(int i, int i2) {
                return ((ItemInternal) DialogsAdapter.this.oldItems.get(i)).compare((ItemInternal) arrayList2.get(i2));
            }

            @Override // androidx.recyclerview.widget.DiffUtil.Callback
            public int getNewListSize() {
                return arrayList2.size();
            }

            @Override // androidx.recyclerview.widget.DiffUtil.Callback
            public int getOldListSize() {
                return DialogsAdapter.this.oldItems.size();
            }
        };
        if (this.itemInternals.size() >= 50 && ALLOW_UPDATE_IN_BACKGROUND) {
            Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.DialogsAdapter$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsAdapter.this.lambda$updateList$2(callback, runnable, arrayList2);
                }
            });
            return;
        }
        DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(callback);
        this.isCalculatingDiff = false;
        if (runnable != null) {
            runnable.run();
        }
        this.itemInternals = arrayList2;
        calculateDiff.dispatchUpdatesTo(this);
    }
}
