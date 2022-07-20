package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Property;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$PeerLocated;
import org.telegram.tgnet.TLRPC$TL_channels_getAdminedPublicChannels;
import org.telegram.tgnet.TLRPC$TL_contacts_getLocated;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputGeoPoint;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerLocated;
import org.telegram.tgnet.TLRPC$TL_peerSelfLocated;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_updatePeerLocated;
import org.telegram.tgnet.TLRPC$TL_updates;
import org.telegram.tgnet.TLRPC$Update;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ShareLocationDrawable;
import org.telegram.ui.Components.UndoView;
/* loaded from: classes3.dex */
public class PeopleNearbyActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, LocationController.LocationFetchCallback {
    private AnimatorSet actionBarAnimator;
    private View actionBarBackground;
    private boolean canCreateGroup;
    private int chatsCreateRow;
    private int chatsEndRow;
    private int chatsHeaderRow;
    private int chatsSectionRow;
    private int chatsStartRow;
    private Runnable checkExpiredRunnable;
    private boolean checkingCanCreate;
    private String currentGroupCreateAddress;
    private String currentGroupCreateDisplayAddress;
    private Location currentGroupCreateLocation;
    private boolean expanded;
    private boolean firstLoaded;
    private ActionIntroActivity groupCreateActivity;
    private int helpRow;
    private int helpSectionRow;
    private DefaultItemAnimator itemAnimator;
    private Location lastLoadedLocation;
    private long lastLoadedLocationTime;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private AlertDialog loadingDialog;
    private int reqId;
    private int rowCount;
    private int showMeRow;
    private int showMoreRow;
    private AnimatorSet showProgressAnimation;
    private Runnable showProgressRunnable;
    private boolean showingLoadingProgress;
    private boolean showingMe;
    private UndoView undoView;
    private int usersEndRow;
    private int usersHeaderRow;
    private int usersSectionRow;
    private int usersStartRow;
    private ArrayList<View> animatingViews = new ArrayList<>();
    private Runnable shortPollRunnable = new AnonymousClass1();
    private int[] location = new int[2];
    private ArrayList<TLRPC$TL_peerLocated> users = new ArrayList<>(getLocationController().getCachedNearbyUsers());
    private ArrayList<TLRPC$TL_peerLocated> chats = new ArrayList<>(getLocationController().getCachedNearbyChats());

    /* renamed from: org.telegram.ui.PeopleNearbyActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            PeopleNearbyActivity.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (PeopleNearbyActivity.this.shortPollRunnable != null) {
                PeopleNearbyActivity.this.sendRequest(true, 0);
                AndroidUtilities.cancelRunOnUIThread(PeopleNearbyActivity.this.shortPollRunnable);
                AndroidUtilities.runOnUIThread(PeopleNearbyActivity.this.shortPollRunnable, 25000L);
            }
        }
    }

    public PeopleNearbyActivity() {
        checkForExpiredLocations(false);
        updateRows(null);
    }

    private void updateRows(DiffCallback diffCallback) {
        int i;
        this.rowCount = 0;
        this.usersStartRow = -1;
        this.usersEndRow = -1;
        this.showMoreRow = -1;
        this.chatsStartRow = -1;
        this.chatsEndRow = -1;
        this.chatsCreateRow = -1;
        this.showMeRow = -1;
        int i2 = 0 + 1;
        this.rowCount = i2;
        this.helpRow = 0;
        int i3 = i2 + 1;
        this.rowCount = i3;
        this.helpSectionRow = i2;
        int i4 = i3 + 1;
        this.rowCount = i4;
        this.usersHeaderRow = i3;
        this.rowCount = i4 + 1;
        this.showMeRow = i4;
        if (!this.users.isEmpty()) {
            if (this.expanded) {
                i = this.users.size();
            } else {
                i = Math.min(5, this.users.size());
            }
            int i5 = this.rowCount;
            this.usersStartRow = i5;
            int i6 = i5 + i;
            this.rowCount = i6;
            this.usersEndRow = i6;
            if (i != this.users.size()) {
                int i7 = this.rowCount;
                this.rowCount = i7 + 1;
                this.showMoreRow = i7;
            }
        }
        int i8 = this.rowCount;
        int i9 = i8 + 1;
        this.rowCount = i9;
        this.usersSectionRow = i8;
        int i10 = i9 + 1;
        this.rowCount = i10;
        this.chatsHeaderRow = i9;
        this.rowCount = i10 + 1;
        this.chatsCreateRow = i10;
        if (!this.chats.isEmpty()) {
            int i11 = this.rowCount;
            this.chatsStartRow = i11;
            int size = i11 + this.chats.size();
            this.rowCount = size;
            this.chatsEndRow = size;
        }
        int i12 = this.rowCount;
        this.rowCount = i12 + 1;
        this.chatsSectionRow = i12;
        if (this.listViewAdapter != null) {
            if (diffCallback == null) {
                this.listView.setItemAnimator(null);
                this.listViewAdapter.notifyDataSetChanged();
                return;
            }
            this.listView.setItemAnimator(this.itemAnimator);
            diffCallback.fillPositions(diffCallback.newPositionToItem);
            DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this.listViewAdapter);
        }
    }

    /* loaded from: classes3.dex */
    public class DiffCallback extends DiffUtil.Callback {
        SparseIntArray newPositionToItem;
        private final ArrayList<TLRPC$TL_peerLocated> oldChats;
        int oldChatsEndRow;
        int oldChatsStartRow;
        SparseIntArray oldPositionToItem;
        int oldRowCount;
        private final ArrayList<TLRPC$TL_peerLocated> oldUsers;
        int oldUsersEndRow;
        int oldUsersStartRow;

        private DiffCallback() {
            PeopleNearbyActivity.this = r1;
            this.oldPositionToItem = new SparseIntArray();
            this.newPositionToItem = new SparseIntArray();
            this.oldUsers = new ArrayList<>();
            this.oldChats = new ArrayList<>();
        }

        /* synthetic */ DiffCallback(PeopleNearbyActivity peopleNearbyActivity, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.oldRowCount;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return PeopleNearbyActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            int i3;
            int i4;
            if (i2 >= PeopleNearbyActivity.this.usersStartRow && i2 < PeopleNearbyActivity.this.usersEndRow && i >= (i4 = this.oldUsersStartRow) && i < this.oldUsersEndRow) {
                return MessageObject.getPeerId(this.oldUsers.get(i - i4).peer) == MessageObject.getPeerId(((TLRPC$TL_peerLocated) PeopleNearbyActivity.this.users.get(i2 - PeopleNearbyActivity.this.usersStartRow)).peer);
            } else if (i2 >= PeopleNearbyActivity.this.chatsStartRow && i2 < PeopleNearbyActivity.this.chatsEndRow && i >= (i3 = this.oldChatsStartRow) && i < this.oldChatsEndRow) {
                return MessageObject.getPeerId(this.oldChats.get(i - i3).peer) == MessageObject.getPeerId(((TLRPC$TL_peerLocated) PeopleNearbyActivity.this.chats.get(i2 - PeopleNearbyActivity.this.chatsStartRow)).peer);
            } else {
                int i5 = this.oldPositionToItem.get(i, -1);
                return i5 == this.newPositionToItem.get(i2, -1) && i5 >= 0;
            }
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            return areItemsTheSame(i, i2);
        }

        public void fillPositions(SparseIntArray sparseIntArray) {
            sparseIntArray.clear();
            put(1, PeopleNearbyActivity.this.helpRow, sparseIntArray);
            put(2, PeopleNearbyActivity.this.helpSectionRow, sparseIntArray);
            put(3, PeopleNearbyActivity.this.usersHeaderRow, sparseIntArray);
            put(4, PeopleNearbyActivity.this.showMoreRow, sparseIntArray);
            put(5, PeopleNearbyActivity.this.usersSectionRow, sparseIntArray);
            put(6, PeopleNearbyActivity.this.chatsHeaderRow, sparseIntArray);
            put(7, PeopleNearbyActivity.this.chatsCreateRow, sparseIntArray);
            put(8, PeopleNearbyActivity.this.chatsSectionRow, sparseIntArray);
            put(9, PeopleNearbyActivity.this.showMeRow, sparseIntArray);
        }

        public void saveCurrentState() {
            this.oldRowCount = PeopleNearbyActivity.this.rowCount;
            this.oldUsersStartRow = PeopleNearbyActivity.this.usersStartRow;
            this.oldUsersEndRow = PeopleNearbyActivity.this.usersEndRow;
            this.oldChatsStartRow = PeopleNearbyActivity.this.chatsStartRow;
            this.oldChatsEndRow = PeopleNearbyActivity.this.chatsEndRow;
            this.oldUsers.addAll(PeopleNearbyActivity.this.users);
            this.oldChats.addAll(PeopleNearbyActivity.this.chats);
            fillPositions(this.oldPositionToItem);
        }

        private void put(int i, int i2, SparseIntArray sparseIntArray) {
            if (i2 >= 0) {
                sparseIntArray.put(i2, i);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.newLocationAvailable);
        getNotificationCenter().addObserver(this, NotificationCenter.newPeopleNearbyAvailable);
        getNotificationCenter().addObserver(this, NotificationCenter.needDeleteDialog);
        checkCanCreateGroup();
        sendRequest(false, 0);
        AndroidUtilities.runOnUIThread(this.shortPollRunnable, 25000L);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.newLocationAvailable);
        getNotificationCenter().removeObserver(this, NotificationCenter.newPeopleNearbyAvailable);
        getNotificationCenter().removeObserver(this, NotificationCenter.needDeleteDialog);
        Runnable runnable = this.shortPollRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.shortPollRunnable = null;
        }
        Runnable runnable2 = this.checkExpiredRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.checkExpiredRunnable = null;
        }
        Runnable runnable3 = this.showProgressRunnable;
        if (runnable3 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable3);
            this.showProgressRunnable = null;
        }
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setBackgroundDrawable(null);
        this.actionBar.setTitleColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.actionBar.setItemsColor(Theme.getColor("windowBackgroundWhiteBlackText"), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor("listSelectorSDK21"), false);
        this.actionBar.setCastShadows(false);
        this.actionBar.setAddToContainer(false);
        int i = 1;
        this.actionBar.setOccupyStatusBar(Build.VERSION.SDK_INT >= 21 && !AndroidUtilities.isTablet());
        this.actionBar.setTitle(LocaleController.getString("PeopleNearby", 2131627453));
        this.actionBar.getTitleTextView().setAlpha(0.0f);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass2());
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        this.fragmentView = anonymousClass3;
        anonymousClass3.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        this.fragmentView.setTag("windowBackgroundGray");
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setGlowColor(0);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        RecyclerListView recyclerListView3 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listViewAdapter = listAdapter;
        recyclerListView3.setAdapter(listAdapter);
        RecyclerListView recyclerListView4 = this.listView;
        if (!LocaleController.isRTL) {
            i = 2;
        }
        recyclerListView4.setVerticalScrollbarPosition(i);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.itemAnimator = new AnonymousClass4(this);
        this.listView.setOnItemClickListener(new PeopleNearbyActivity$$ExternalSyntheticLambda10(this));
        this.listView.setOnScrollListener(new AnonymousClass5());
        AnonymousClass6 anonymousClass6 = new AnonymousClass6(context);
        this.actionBarBackground = anonymousClass6;
        anonymousClass6.setAlpha(0.0f);
        frameLayout.addView(this.actionBarBackground, LayoutHelper.createFrame(-1, -2.0f));
        frameLayout.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        UndoView undoView = new UndoView(context);
        this.undoView = undoView;
        frameLayout.addView(undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        updateRows(null);
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.PeopleNearbyActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass2() {
            PeopleNearbyActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                PeopleNearbyActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.PeopleNearbyActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            PeopleNearbyActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            ((FrameLayout.LayoutParams) PeopleNearbyActivity.this.actionBarBackground.getLayoutParams()).height = ActionBar.getCurrentActionBarHeight() + (((BaseFragment) PeopleNearbyActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + AndroidUtilities.dp(3.0f);
            super.onMeasure(i, i2);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            PeopleNearbyActivity.this.checkScroll(false);
        }
    }

    /* renamed from: org.telegram.ui.PeopleNearbyActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends DefaultItemAnimator {
        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected long getAddAnimationDelay(long j, long j2, long j3) {
            return j;
        }

        AnonymousClass4(PeopleNearbyActivity peopleNearbyActivity) {
        }
    }

    public /* synthetic */ void lambda$createView$2(View view, int i) {
        long j;
        if (getParentActivity() == null) {
            return;
        }
        int i2 = this.usersStartRow;
        if (i >= i2 && i < this.usersEndRow) {
            if (!(view instanceof ManageChatUserCell)) {
                return;
            }
            TLRPC$TL_peerLocated tLRPC$TL_peerLocated = this.users.get(i - i2);
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", tLRPC$TL_peerLocated.peer.user_id);
            if (((ManageChatUserCell) view).hasAvatarSet()) {
                bundle.putBoolean("expandPhoto", true);
            }
            bundle.putInt("nearby_distance", tLRPC$TL_peerLocated.distance);
            MessagesController.getInstance(this.currentAccount).ensureMessagesLoaded(tLRPC$TL_peerLocated.peer.user_id, 0, null);
            presentFragment(new ProfileActivity(bundle));
            return;
        }
        int i3 = this.chatsStartRow;
        if (i >= i3 && i < this.chatsEndRow) {
            Bundle bundle2 = new Bundle();
            TLRPC$Peer tLRPC$Peer = this.chats.get(i - i3).peer;
            if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                j = tLRPC$Peer.chat_id;
            } else {
                j = tLRPC$Peer.channel_id;
            }
            bundle2.putLong("chat_id", j);
            presentFragment(new ChatActivity(bundle2));
        } else if (i == this.chatsCreateRow) {
            if (this.checkingCanCreate || this.currentGroupCreateAddress == null) {
                AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
                this.loadingDialog = alertDialog;
                alertDialog.setOnCancelListener(new PeopleNearbyActivity$$ExternalSyntheticLambda0(this));
                this.loadingDialog.show();
                return;
            }
            openGroupCreate();
        } else if (i == this.showMeRow) {
            UserConfig userConfig = getUserConfig();
            if (this.showingMe) {
                userConfig.sharingMyLocationUntil = 0;
                userConfig.saveConfig(false);
                sendRequest(false, 2);
                updateRows(null);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("MakeMyselfVisibleTitle", 2131626523));
                builder.setMessage(LocaleController.getString("MakeMyselfVisibleInfo", 2131626522));
                builder.setPositiveButton(LocaleController.getString("OK", 2131627075), new PeopleNearbyActivity$$ExternalSyntheticLambda1(this, userConfig));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                showDialog(builder.create());
            }
            userConfig.saveConfig(false);
        } else if (i != this.showMoreRow) {
        } else {
            this.expanded = true;
            DiffCallback diffCallback = new DiffCallback(this, null);
            diffCallback.saveCurrentState();
            updateRows(diffCallback);
        }
    }

    public /* synthetic */ void lambda$createView$0(DialogInterface dialogInterface) {
        this.loadingDialog = null;
    }

    public /* synthetic */ void lambda$createView$1(UserConfig userConfig, DialogInterface dialogInterface, int i) {
        userConfig.sharingMyLocationUntil = Integer.MAX_VALUE;
        userConfig.saveConfig(false);
        sendRequest(false, 1);
        updateRows(null);
    }

    /* renamed from: org.telegram.ui.PeopleNearbyActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends RecyclerView.OnScrollListener {
        AnonymousClass5() {
            PeopleNearbyActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            PeopleNearbyActivity.this.checkScroll(true);
        }
    }

    /* renamed from: org.telegram.ui.PeopleNearbyActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends View {
        private Paint paint = new Paint();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            PeopleNearbyActivity.this = r1;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.paint.setColor(Theme.getColor("windowBackgroundWhite"));
            int measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(3.0f);
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), measuredHeight, this.paint);
            ((BaseFragment) PeopleNearbyActivity.this).parentLayout.drawHeaderShadow(canvas, measuredHeight);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkScroll(boolean z) {
        boolean z2;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        int findFirstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
        if (findFirstVisibleItemPosition == 0 && (findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition)) != null) {
            HintInnerCell hintInnerCell = (HintInnerCell) findViewHolderForAdapterPosition.itemView;
            hintInnerCell.titleTextView.getLocationOnScreen(this.location);
            if (this.location[1] + hintInnerCell.titleTextView.getMeasuredHeight() >= this.actionBar.getBottom()) {
                z2 = false;
                if (z2 != (this.actionBarBackground.getTag() != null)) {
                    return;
                }
                this.actionBarBackground.setTag(z2 ? null : 1);
                AnimatorSet animatorSet = this.actionBarAnimator;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.actionBarAnimator = null;
                }
                float f = 1.0f;
                if (z) {
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    this.actionBarAnimator = animatorSet2;
                    Animator[] animatorArr = new Animator[2];
                    View view = this.actionBarBackground;
                    Property property = View.ALPHA;
                    float[] fArr = new float[1];
                    fArr[0] = z2 ? 1.0f : 0.0f;
                    animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
                    SimpleTextView titleTextView = this.actionBar.getTitleTextView();
                    Property property2 = View.ALPHA;
                    float[] fArr2 = new float[1];
                    if (!z2) {
                        f = 0.0f;
                    }
                    fArr2[0] = f;
                    animatorArr[1] = ObjectAnimator.ofFloat(titleTextView, property2, fArr2);
                    animatorSet2.playTogether(animatorArr);
                    this.actionBarAnimator.setDuration(150L);
                    this.actionBarAnimator.addListener(new AnonymousClass7());
                    this.actionBarAnimator.start();
                    return;
                }
                this.actionBarBackground.setAlpha(z2 ? 1.0f : 0.0f);
                SimpleTextView titleTextView2 = this.actionBar.getTitleTextView();
                if (!z2) {
                    f = 0.0f;
                }
                titleTextView2.setAlpha(f);
                return;
            }
        }
        z2 = true;
        if (z2 != (this.actionBarBackground.getTag() != null)) {
        }
    }

    /* renamed from: org.telegram.ui.PeopleNearbyActivity$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        AnonymousClass7() {
            PeopleNearbyActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (animator.equals(PeopleNearbyActivity.this.actionBarAnimator)) {
                PeopleNearbyActivity.this.actionBarAnimator = null;
            }
        }
    }

    private void openGroupCreate() {
        if (!this.canCreateGroup) {
            AlertsCreator.showSimpleAlert(this, LocaleController.getString("YourLocatedChannelsTooMuch", 2131629289));
            return;
        }
        ActionIntroActivity actionIntroActivity = new ActionIntroActivity(2);
        this.groupCreateActivity = actionIntroActivity;
        actionIntroActivity.setGroupCreateAddress(this.currentGroupCreateAddress, this.currentGroupCreateDisplayAddress, this.currentGroupCreateLocation);
        presentFragment(this.groupCreateActivity);
    }

    private void checkCanCreateGroup() {
        if (this.checkingCanCreate) {
            return;
        }
        this.checkingCanCreate = true;
        TLRPC$TL_channels_getAdminedPublicChannels tLRPC$TL_channels_getAdminedPublicChannels = new TLRPC$TL_channels_getAdminedPublicChannels();
        tLRPC$TL_channels_getAdminedPublicChannels.by_location = true;
        tLRPC$TL_channels_getAdminedPublicChannels.check_limit = true;
        getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tLRPC$TL_channels_getAdminedPublicChannels, new PeopleNearbyActivity$$ExternalSyntheticLambda7(this)), this.classGuid);
    }

    public /* synthetic */ void lambda$checkCanCreateGroup$4(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PeopleNearbyActivity$$ExternalSyntheticLambda6(this, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$checkCanCreateGroup$3(TLRPC$TL_error tLRPC$TL_error) {
        this.canCreateGroup = tLRPC$TL_error == null;
        this.checkingCanCreate = false;
        AlertDialog alertDialog = this.loadingDialog;
        if (alertDialog == null || this.currentGroupCreateAddress == null) {
            return;
        }
        try {
            alertDialog.dismiss();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        this.loadingDialog = null;
        openGroupCreate();
    }

    private void showLoadingProgress(boolean z) {
        if (this.showingLoadingProgress == z) {
            return;
        }
        this.showingLoadingProgress = z;
        AnimatorSet animatorSet = this.showProgressAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.showProgressAnimation = null;
        }
        if (this.listView == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int childCount = this.listView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt instanceof HeaderCellProgress) {
                HeaderCellProgress headerCellProgress = (HeaderCellProgress) childAt;
                this.animatingViews.add(headerCellProgress);
                RadialProgressView radialProgressView = headerCellProgress.progressView;
                Property property = View.ALPHA;
                float[] fArr = new float[1];
                fArr[0] = z ? 1.0f : 0.0f;
                arrayList.add(ObjectAnimator.ofFloat(radialProgressView, property, fArr));
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.showProgressAnimation = animatorSet2;
        animatorSet2.playTogether(arrayList);
        this.showProgressAnimation.addListener(new AnonymousClass8());
        this.showProgressAnimation.setDuration(180L);
        this.showProgressAnimation.start();
    }

    /* renamed from: org.telegram.ui.PeopleNearbyActivity$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        AnonymousClass8() {
            PeopleNearbyActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            PeopleNearbyActivity.this.showProgressAnimation = null;
            PeopleNearbyActivity.this.animatingViews.clear();
        }
    }

    public void sendRequest(boolean z, int i) {
        Location location;
        if (!this.firstLoaded) {
            PeopleNearbyActivity$$ExternalSyntheticLambda3 peopleNearbyActivity$$ExternalSyntheticLambda3 = new PeopleNearbyActivity$$ExternalSyntheticLambda3(this);
            this.showProgressRunnable = peopleNearbyActivity$$ExternalSyntheticLambda3;
            AndroidUtilities.runOnUIThread(peopleNearbyActivity$$ExternalSyntheticLambda3, 1000L);
            this.firstLoaded = true;
        }
        Location lastKnownLocation = getLocationController().getLastKnownLocation();
        if (lastKnownLocation == null) {
            return;
        }
        this.currentGroupCreateLocation = lastKnownLocation;
        int i2 = 0;
        if (!z && (location = this.lastLoadedLocation) != null) {
            float distanceTo = location.distanceTo(lastKnownLocation);
            if (BuildVars.DEBUG_VERSION) {
                FileLog.d("located distance = " + distanceTo);
            }
            if (i == 0 && (SystemClock.elapsedRealtime() - this.lastLoadedLocationTime < 3000 || this.lastLoadedLocation.distanceTo(lastKnownLocation) <= 20.0f)) {
                return;
            }
            if (this.reqId != 0) {
                getConnectionsManager().cancelRequest(this.reqId, true);
                this.reqId = 0;
            }
        }
        if (this.reqId != 0) {
            return;
        }
        this.lastLoadedLocation = lastKnownLocation;
        this.lastLoadedLocationTime = SystemClock.elapsedRealtime();
        LocationController.fetchLocationAddress(this.currentGroupCreateLocation, this);
        TLRPC$TL_contacts_getLocated tLRPC$TL_contacts_getLocated = new TLRPC$TL_contacts_getLocated();
        TLRPC$TL_inputGeoPoint tLRPC$TL_inputGeoPoint = new TLRPC$TL_inputGeoPoint();
        tLRPC$TL_contacts_getLocated.geo_point = tLRPC$TL_inputGeoPoint;
        tLRPC$TL_inputGeoPoint.lat = lastKnownLocation.getLatitude();
        tLRPC$TL_contacts_getLocated.geo_point._long = lastKnownLocation.getLongitude();
        if (i != 0) {
            tLRPC$TL_contacts_getLocated.flags |= 1;
            if (i == 1) {
                i2 = Integer.MAX_VALUE;
            }
            tLRPC$TL_contacts_getLocated.self_expires = i2;
        }
        this.reqId = getConnectionsManager().sendRequest(tLRPC$TL_contacts_getLocated, new PeopleNearbyActivity$$ExternalSyntheticLambda8(this, i));
        getConnectionsManager().bindRequestToGuid(this.reqId, this.classGuid);
    }

    public /* synthetic */ void lambda$sendRequest$5() {
        showLoadingProgress(true);
        this.showProgressRunnable = null;
    }

    public /* synthetic */ void lambda$sendRequest$7(int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PeopleNearbyActivity$$ExternalSyntheticLambda4(this, i, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$sendRequest$6(int i, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        boolean z;
        this.reqId = 0;
        Runnable runnable = this.showProgressRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.showProgressRunnable = null;
        }
        showLoadingProgress(false);
        UserConfig userConfig = getUserConfig();
        if (i != 1 || tLRPC$TL_error == null) {
            z = false;
        } else {
            userConfig.sharingMyLocationUntil = 0;
            updateRows(null);
            z = true;
        }
        if (tLObject != null && i != 2) {
            TLRPC$TL_updates tLRPC$TL_updates = (TLRPC$TL_updates) tLObject;
            getMessagesController().putUsers(tLRPC$TL_updates.users, false);
            getMessagesController().putChats(tLRPC$TL_updates.chats, false);
            DiffCallback diffCallback = new DiffCallback(this, null);
            diffCallback.saveCurrentState();
            this.users.clear();
            this.chats.clear();
            if (userConfig.sharingMyLocationUntil != 0) {
                userConfig.lastMyLocationShareTime = (int) (System.currentTimeMillis() / 1000);
                z = true;
            }
            int size = tLRPC$TL_updates.updates.size();
            boolean z2 = false;
            for (int i2 = 0; i2 < size; i2++) {
                TLRPC$Update tLRPC$Update = tLRPC$TL_updates.updates.get(i2);
                if (tLRPC$Update instanceof TLRPC$TL_updatePeerLocated) {
                    TLRPC$TL_updatePeerLocated tLRPC$TL_updatePeerLocated = (TLRPC$TL_updatePeerLocated) tLRPC$Update;
                    int size2 = tLRPC$TL_updatePeerLocated.peers.size();
                    for (int i3 = 0; i3 < size2; i3++) {
                        TLRPC$PeerLocated tLRPC$PeerLocated = tLRPC$TL_updatePeerLocated.peers.get(i3);
                        if (tLRPC$PeerLocated instanceof TLRPC$TL_peerLocated) {
                            TLRPC$TL_peerLocated tLRPC$TL_peerLocated = (TLRPC$TL_peerLocated) tLRPC$PeerLocated;
                            if (tLRPC$TL_peerLocated.peer instanceof TLRPC$TL_peerUser) {
                                this.users.add(tLRPC$TL_peerLocated);
                            } else {
                                this.chats.add(tLRPC$TL_peerLocated);
                            }
                        } else if (tLRPC$PeerLocated instanceof TLRPC$TL_peerSelfLocated) {
                            int i4 = userConfig.sharingMyLocationUntil;
                            int i5 = ((TLRPC$TL_peerSelfLocated) tLRPC$PeerLocated).expires;
                            if (i4 != i5) {
                                userConfig.sharingMyLocationUntil = i5;
                                z = true;
                            }
                            z2 = true;
                        }
                    }
                }
            }
            if (!z2 && userConfig.sharingMyLocationUntil != 0) {
                userConfig.sharingMyLocationUntil = 0;
                z = true;
            }
            checkForExpiredLocations(true);
            updateRows(diffCallback);
        }
        if (z) {
            userConfig.saveConfig(false);
        }
        Runnable runnable2 = this.shortPollRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            AndroidUtilities.runOnUIThread(this.shortPollRunnable, 25000L);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listViewAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        getLocationController().startLocationLookupForPeopleNearby(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
        getLocationController().startLocationLookupForPeopleNearby(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        super.onBecomeFullyHidden();
        UndoView undoView = this.undoView;
        if (undoView != null) {
            undoView.hide(true, 0);
        }
    }

    @Override // org.telegram.messenger.LocationController.LocationFetchCallback
    public void onLocationAddressAvailable(String str, String str2, Location location) {
        this.currentGroupCreateAddress = str;
        this.currentGroupCreateDisplayAddress = str2;
        this.currentGroupCreateLocation = location;
        ActionIntroActivity actionIntroActivity = this.groupCreateActivity;
        if (actionIntroActivity != null) {
            actionIntroActivity.setGroupCreateAddress(str, str2, location);
        }
        AlertDialog alertDialog = this.loadingDialog;
        if (alertDialog == null || this.checkingCanCreate) {
            return;
        }
        try {
            alertDialog.dismiss();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        this.loadingDialog = null;
        openGroupCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyVisible() {
        super.onBecomeFullyVisible();
        this.groupCreateActivity = null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0061, code lost:
        if (r13 != r5.peer.user_id) goto L23;
     */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ArrayList<TLRPC$TL_peerLocated> arrayList;
        ArrayList<TLRPC$TL_peerLocated> arrayList2;
        if (i == NotificationCenter.newLocationAvailable) {
            sendRequest(false, 0);
        } else if (i == NotificationCenter.newPeopleNearbyAvailable) {
            TLRPC$TL_updatePeerLocated tLRPC$TL_updatePeerLocated = (TLRPC$TL_updatePeerLocated) objArr[0];
            DiffCallback diffCallback = new DiffCallback(this, null);
            diffCallback.saveCurrentState();
            int size = tLRPC$TL_updatePeerLocated.peers.size();
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC$PeerLocated tLRPC$PeerLocated = tLRPC$TL_updatePeerLocated.peers.get(i3);
                if (tLRPC$PeerLocated instanceof TLRPC$TL_peerLocated) {
                    TLRPC$TL_peerLocated tLRPC$TL_peerLocated = (TLRPC$TL_peerLocated) tLRPC$PeerLocated;
                    if (tLRPC$TL_peerLocated.peer instanceof TLRPC$TL_peerUser) {
                        arrayList = this.users;
                    } else {
                        arrayList = this.chats;
                    }
                    int size2 = arrayList.size();
                    boolean z = false;
                    for (int i4 = 0; i4 < size2; i4++) {
                        TLRPC$Peer tLRPC$Peer = arrayList.get(i4).peer;
                        long j = tLRPC$Peer.user_id;
                        if (j != 0) {
                            arrayList2 = arrayList;
                        } else {
                            arrayList2 = arrayList;
                        }
                        long j2 = tLRPC$Peer.chat_id;
                        if (j2 == 0 || j2 != tLRPC$TL_peerLocated.peer.chat_id) {
                            long j3 = tLRPC$Peer.channel_id;
                            if (j3 == 0 || j3 != tLRPC$TL_peerLocated.peer.channel_id) {
                                arrayList = arrayList2;
                            }
                        }
                        arrayList = arrayList2;
                        arrayList.set(i4, tLRPC$TL_peerLocated);
                        z = true;
                    }
                    if (!z) {
                        arrayList.add(tLRPC$TL_peerLocated);
                    }
                }
            }
            checkForExpiredLocations(true);
            updateRows(diffCallback);
        } else if (i == NotificationCenter.needDeleteDialog && this.fragmentView != null && !this.isPaused) {
            long longValue = ((Long) objArr[0]).longValue();
            TLRPC$User tLRPC$User = (TLRPC$User) objArr[1];
            PeopleNearbyActivity$$ExternalSyntheticLambda5 peopleNearbyActivity$$ExternalSyntheticLambda5 = new PeopleNearbyActivity$$ExternalSyntheticLambda5(this, (TLRPC$Chat) objArr[2], longValue, ((Boolean) objArr[3]).booleanValue());
            UndoView undoView = this.undoView;
            if (undoView != null) {
                undoView.showWithAction(longValue, 1, (Runnable) peopleNearbyActivity$$ExternalSyntheticLambda5);
            } else {
                peopleNearbyActivity$$ExternalSyntheticLambda5.run();
            }
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$8(TLRPC$Chat tLRPC$Chat, long j, boolean z) {
        if (tLRPC$Chat != null) {
            if (ChatObject.isNotInChat(tLRPC$Chat)) {
                getMessagesController().deleteDialog(j, 0, z);
                return;
            } else {
                getMessagesController().deleteParticipantFromChat(-j, getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId())), null, null, z, z);
                return;
            }
        }
        getMessagesController().deleteDialog(j, 0, z);
    }

    private void checkForExpiredLocations(boolean z) {
        Runnable runnable = this.checkExpiredRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.checkExpiredRunnable = null;
        }
        int currentTime = getConnectionsManager().getCurrentTime();
        DiffCallback diffCallback = null;
        int i = 0;
        boolean z2 = false;
        int i2 = Integer.MAX_VALUE;
        while (i < 2) {
            ArrayList<TLRPC$TL_peerLocated> arrayList = i == 0 ? this.users : this.chats;
            int size = arrayList.size();
            int i3 = 0;
            while (i3 < size) {
                int i4 = arrayList.get(i3).expires;
                if (i4 <= currentTime) {
                    if (diffCallback == null) {
                        diffCallback = new DiffCallback(this, null);
                        diffCallback.saveCurrentState();
                    }
                    arrayList.remove(i3);
                    i3--;
                    size--;
                    z2 = true;
                } else {
                    i2 = Math.min(i2, i4);
                }
                i3++;
            }
            i++;
        }
        if (z2 && this.listViewAdapter != null) {
            updateRows(diffCallback);
        }
        if (z2 || z) {
            getLocationController().setCachedNearbyUsersAndChats(this.users, this.chats);
        }
        if (i2 != Integer.MAX_VALUE) {
            PeopleNearbyActivity$$ExternalSyntheticLambda2 peopleNearbyActivity$$ExternalSyntheticLambda2 = new PeopleNearbyActivity$$ExternalSyntheticLambda2(this);
            this.checkExpiredRunnable = peopleNearbyActivity$$ExternalSyntheticLambda2;
            AndroidUtilities.runOnUIThread(peopleNearbyActivity$$ExternalSyntheticLambda2, (i2 - currentTime) * 1000);
        }
    }

    public /* synthetic */ void lambda$checkForExpiredLocations$9() {
        this.checkExpiredRunnable = null;
        checkForExpiredLocations(false);
    }

    /* loaded from: classes3.dex */
    public static class HeaderCellProgress extends HeaderCell {
        private RadialProgressView progressView;

        public HeaderCellProgress(Context context) {
            super(context);
            setClipChildren(false);
            RadialProgressView radialProgressView = new RadialProgressView(context);
            this.progressView = radialProgressView;
            radialProgressView.setSize(AndroidUtilities.dp(14.0f));
            this.progressView.setStrokeWidth(2.0f);
            this.progressView.setAlpha(0.0f);
            this.progressView.setProgressColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
            RadialProgressView radialProgressView2 = this.progressView;
            boolean z = LocaleController.isRTL;
            addView(radialProgressView2, LayoutHelper.createFrame(50, 40.0f, (z ? 3 : 5) | 48, z ? 2.0f : 0.0f, 3.0f, z ? 0.0f : 2.0f, 0.0f));
        }
    }

    /* loaded from: classes3.dex */
    public class HintInnerCell extends FrameLayout {
        private ImageView imageView;
        private TextView messageTextView;
        private TextView titleTextView;

        public HintInnerCell(PeopleNearbyActivity peopleNearbyActivity, Context context) {
            super(context);
            int currentActionBarHeight = ((int) ((ActionBar.getCurrentActionBarHeight() + (((BaseFragment) peopleNearbyActivity).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0)) / AndroidUtilities.density)) - 44;
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setBackgroundDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(74.0f), Theme.getColor("chats_archiveBackground")));
            this.imageView.setImageDrawable(new ShareLocationDrawable(context, 2));
            this.imageView.setScaleType(ImageView.ScaleType.CENTER);
            addView(this.imageView, LayoutHelper.createFrame(74, 74.0f, 49, 0.0f, currentActionBarHeight + 27, 0.0f, 0.0f));
            TextView textView = new TextView(context);
            this.titleTextView = textView;
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.titleTextView.setTextSize(1, 24.0f);
            this.titleTextView.setGravity(17);
            this.titleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("PeopleNearby", 2131627453, new Object[0])));
            addView(this.titleTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 17.0f, currentActionBarHeight + 120, 17.0f, 27.0f));
            TextView textView2 = new TextView(context);
            this.messageTextView = textView2;
            textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
            this.messageTextView.setTextSize(1, 15.0f);
            this.messageTextView.setGravity(17);
            this.messageTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("PeopleNearbyInfo2", 2131627460, new Object[0])));
            addView(this.messageTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 40.0f, currentActionBarHeight + 161, 40.0f, 27.0f));
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            PeopleNearbyActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 2;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PeopleNearbyActivity.this.rowCount;
        }

        /* renamed from: org.telegram.ui.PeopleNearbyActivity$ListAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends TextView {
            AnonymousClass1(ListAdapter listAdapter, Context context) {
                super(context);
            }

            @Override // android.widget.TextView, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(67.0f), 1073741824));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            HintInnerCell hintInnerCell;
            if (i == 0) {
                ManageChatUserCell manageChatUserCell = new ManageChatUserCell(this.mContext, 6, 2, false);
                manageChatUserCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                hintInnerCell = manageChatUserCell;
            } else if (i == 1) {
                hintInnerCell = new ShadowSectionCell(this.mContext);
            } else if (i == 2) {
                ManageChatTextCell manageChatTextCell = new ManageChatTextCell(this.mContext);
                manageChatTextCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                hintInnerCell = manageChatTextCell;
            } else if (i == 3) {
                HeaderCellProgress headerCellProgress = new HeaderCellProgress(this.mContext);
                headerCellProgress.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                hintInnerCell = headerCellProgress;
            } else if (i == 4) {
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, this.mContext);
                anonymousClass1.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                anonymousClass1.setPadding(0, 0, AndroidUtilities.dp(3.0f), 0);
                anonymousClass1.setTextSize(1, 14.0f);
                anonymousClass1.setGravity(17);
                anonymousClass1.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
                hintInnerCell = anonymousClass1;
            } else {
                HintInnerCell hintInnerCell2 = new HintInnerCell(PeopleNearbyActivity.this, this.mContext);
                hintInnerCell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                hintInnerCell = hintInnerCell2;
            }
            hintInnerCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(hintInnerCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 3 || PeopleNearbyActivity.this.animatingViews.contains(viewHolder.itemView)) {
                return;
            }
            ((HeaderCellProgress) viewHolder.itemView).progressView.setAlpha(PeopleNearbyActivity.this.showingLoadingProgress ? 1.0f : 0.0f);
        }

        private String formatDistance(TLRPC$TL_peerLocated tLRPC$TL_peerLocated) {
            return LocaleController.formatDistance(tLRPC$TL_peerLocated.distance, 0);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            long j;
            int itemViewType = viewHolder.getItemViewType();
            boolean z = false;
            if (itemViewType == 0) {
                ManageChatUserCell manageChatUserCell = (ManageChatUserCell) viewHolder.itemView;
                manageChatUserCell.setTag(Integer.valueOf(i));
                if (i < PeopleNearbyActivity.this.usersStartRow || i >= PeopleNearbyActivity.this.usersEndRow) {
                    if (i < PeopleNearbyActivity.this.chatsStartRow || i >= PeopleNearbyActivity.this.chatsEndRow) {
                        return;
                    }
                    int i2 = i - PeopleNearbyActivity.this.chatsStartRow;
                    TLRPC$TL_peerLocated tLRPC$TL_peerLocated = (TLRPC$TL_peerLocated) PeopleNearbyActivity.this.chats.get(i2);
                    TLRPC$Peer tLRPC$Peer = tLRPC$TL_peerLocated.peer;
                    if (tLRPC$Peer instanceof TLRPC$TL_peerChat) {
                        j = tLRPC$Peer.chat_id;
                    } else {
                        j = tLRPC$Peer.channel_id;
                    }
                    TLRPC$Chat chat = PeopleNearbyActivity.this.getMessagesController().getChat(Long.valueOf(j));
                    if (chat == null) {
                        return;
                    }
                    String formatDistance = formatDistance(tLRPC$TL_peerLocated);
                    int i3 = chat.participants_count;
                    if (i3 != 0) {
                        formatDistance = String.format("%1$s, %2$s", formatDistance, LocaleController.formatPluralString("Members", i3, new Object[0]));
                    }
                    if (i2 != PeopleNearbyActivity.this.chats.size() - 1) {
                        z = true;
                    }
                    manageChatUserCell.setData(chat, null, formatDistance, z);
                    return;
                }
                TLRPC$TL_peerLocated tLRPC$TL_peerLocated2 = (TLRPC$TL_peerLocated) PeopleNearbyActivity.this.users.get(i - PeopleNearbyActivity.this.usersStartRow);
                TLRPC$User user = PeopleNearbyActivity.this.getMessagesController().getUser(Long.valueOf(tLRPC$TL_peerLocated2.peer.user_id));
                if (user == null) {
                    return;
                }
                String formatDistance2 = formatDistance(tLRPC$TL_peerLocated2);
                if (PeopleNearbyActivity.this.showMoreRow != -1 || i != PeopleNearbyActivity.this.usersEndRow - 1) {
                    z = true;
                }
                manageChatUserCell.setData(user, null, formatDistance2, z);
            } else if (itemViewType == 1) {
                ShadowSectionCell shadowSectionCell = (ShadowSectionCell) viewHolder.itemView;
                if (i != PeopleNearbyActivity.this.usersSectionRow) {
                    if (i != PeopleNearbyActivity.this.chatsSectionRow) {
                        if (i != PeopleNearbyActivity.this.helpSectionRow) {
                            return;
                        }
                        shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                        return;
                    }
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                    return;
                }
                shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
            } else if (itemViewType != 2) {
                if (itemViewType != 3) {
                    return;
                }
                HeaderCellProgress headerCellProgress = (HeaderCellProgress) viewHolder.itemView;
                if (i != PeopleNearbyActivity.this.usersHeaderRow) {
                    if (i != PeopleNearbyActivity.this.chatsHeaderRow) {
                        return;
                    }
                    headerCellProgress.setText(LocaleController.getString("ChatsNearbyHeader", 2131625066));
                    return;
                }
                headerCellProgress.setText(LocaleController.getString("PeopleNearbyHeader", 2131627459));
            } else {
                ManageChatTextCell manageChatTextCell = (ManageChatTextCell) viewHolder.itemView;
                manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                if (i != PeopleNearbyActivity.this.chatsCreateRow) {
                    if (i != PeopleNearbyActivity.this.showMeRow) {
                        if (i != PeopleNearbyActivity.this.showMoreRow) {
                            return;
                        }
                        manageChatTextCell.setText(LocaleController.formatPluralString("ShowVotes", PeopleNearbyActivity.this.users.size() - 5, new Object[0]), null, 2131165259, false);
                        return;
                    }
                    PeopleNearbyActivity peopleNearbyActivity = PeopleNearbyActivity.this;
                    if (peopleNearbyActivity.showingMe = peopleNearbyActivity.getUserConfig().sharingMyLocationUntil > PeopleNearbyActivity.this.getConnectionsManager().getCurrentTime()) {
                        String string = LocaleController.getString("StopShowingMe", 2131628478);
                        if (PeopleNearbyActivity.this.usersStartRow != -1) {
                            z = true;
                        }
                        manageChatTextCell.setText(string, null, 2131165824, z);
                        manageChatTextCell.setColors("windowBackgroundWhiteRedText5", "windowBackgroundWhiteRedText5");
                        return;
                    }
                    String string2 = LocaleController.getString("MakeMyselfVisible", 2131626521);
                    if (PeopleNearbyActivity.this.usersStartRow != -1) {
                        z = true;
                    }
                    manageChatTextCell.setText(string2, null, 2131165820, z);
                    return;
                }
                String string3 = LocaleController.getString("NearbyCreateGroup", 2131626756);
                if (PeopleNearbyActivity.this.chatsStartRow != -1) {
                    z = true;
                }
                manageChatTextCell.setText(string3, null, 2131165751, z);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof ManageChatUserCell) {
                ((ManageChatUserCell) view).recycle();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == PeopleNearbyActivity.this.helpRow) {
                return 5;
            }
            if (i == PeopleNearbyActivity.this.chatsCreateRow || i == PeopleNearbyActivity.this.showMeRow || i == PeopleNearbyActivity.this.showMoreRow) {
                return 2;
            }
            if (i == PeopleNearbyActivity.this.usersHeaderRow || i == PeopleNearbyActivity.this.chatsHeaderRow) {
                return 3;
            }
            return (i == PeopleNearbyActivity.this.usersSectionRow || i == PeopleNearbyActivity.this.chatsSectionRow || i == PeopleNearbyActivity.this.helpSectionRow) ? 1 : 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(Theme.getColor("windowBackgroundWhite", null, true)) > 0.699999988079071d;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        PeopleNearbyActivity$$ExternalSyntheticLambda9 peopleNearbyActivity$$ExternalSyntheticLambda9 = new PeopleNearbyActivity$$ExternalSyntheticLambda9(this);
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ManageChatUserCell.class, ManageChatTextCell.class, HeaderCell.class, TextView.class, HintInnerCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.actionBarBackground, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{HeaderCellProgress.class}, new String[]{"progressView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, peopleNearbyActivity$$ExternalSyntheticLambda9, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, peopleNearbyActivity$$ExternalSyntheticLambda9, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, peopleNearbyActivity$$ExternalSyntheticLambda9, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, peopleNearbyActivity$$ExternalSyntheticLambda9, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, peopleNearbyActivity$$ExternalSyntheticLambda9, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, peopleNearbyActivity$$ExternalSyntheticLambda9, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, peopleNearbyActivity$$ExternalSyntheticLambda9, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, peopleNearbyActivity$$ExternalSyntheticLambda9, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, peopleNearbyActivity$$ExternalSyntheticLambda9, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{HintInnerCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_archiveBackground"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HintInnerCell.class}, new String[]{"messageTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_message"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueButton"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText5"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText5"));
        arrayList.add(new ThemeDescription(this.undoView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "undo_background"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"subinfoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        arrayList.add(new ThemeDescription(this.undoView, 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$10() {
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
