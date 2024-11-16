package org.telegram.ui.web;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.ui.Components.UniversalRecyclerView;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.web.AddressBarList;
import org.telegram.ui.web.BookmarksFragment;
import org.telegram.ui.web.WebMetadataCache;

/* loaded from: classes5.dex */
public class BookmarksFragment extends UniversalFragment {
    private final Runnable closeToTabs;
    private ActionBarMenuItem gotoItem;
    private String query;
    private ActionBarMenuItem searchItem;
    public AddressBarList.BookmarksList searchList;
    private NumberTextView selectedCount;
    private final Utilities.Callback whenClicked;
    public AddressBarList.BookmarksList list = new AddressBarList.BookmarksList(this.currentAccount, new Runnable() { // from class: org.telegram.ui.web.BookmarksFragment$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            BookmarksFragment.this.updateWithOffset();
        }
    });
    public HashSet selected = new HashSet();
    private final HashSet addedUrls = new HashSet();

    class 1 extends ActionBar.ActionBarMenuOnItemClick {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onItemClick$0(View view) {
            if (view instanceof AddressBarList.BookmarkView) {
                ((AddressBarList.BookmarkView) view).setChecked(false);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (!((BaseFragment) BookmarksFragment.this).actionBar.isActionModeShowed()) {
                    BookmarksFragment.this.lambda$onBackPressed$321();
                    return;
                }
                ((BaseFragment) BookmarksFragment.this).actionBar.hideActionMode();
                BookmarksFragment.this.selected.clear();
                AndroidUtilities.forEachViews((RecyclerView) BookmarksFragment.this.listView, new Consumer() { // from class: org.telegram.ui.web.BookmarksFragment$1$$ExternalSyntheticLambda0
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        BookmarksFragment.1.lambda$onItemClick$0((View) obj);
                    }
                });
                return;
            }
            if (i == R.id.menu_delete) {
                BookmarksFragment.this.deleteSelectedMessages();
            } else if (i == R.id.menu_link) {
                BookmarksFragment.this.gotoMessage();
            }
        }
    }

    class 2 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        private Runnable applySearch = new Runnable() { // from class: org.telegram.ui.web.BookmarksFragment$2$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BookmarksFragment.2.this.lambda$$1();
            }
        };

        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$1() {
            AddressBarList.BookmarksList bookmarksList = BookmarksFragment.this.searchList;
            if (bookmarksList != null) {
                bookmarksList.load();
            }
        }

        private void scheduleSearch() {
            AndroidUtilities.cancelRunOnUIThread(this.applySearch);
            AndroidUtilities.runOnUIThread(this.applySearch, 500L);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            BookmarksFragment.this.query = null;
            AndroidUtilities.cancelRunOnUIThread(this.applySearch);
            AddressBarList.BookmarksList bookmarksList = BookmarksFragment.this.searchList;
            if (bookmarksList != null) {
                bookmarksList.detach();
                BookmarksFragment.this.searchList = null;
            }
            UniversalRecyclerView universalRecyclerView = BookmarksFragment.this.listView;
            if (universalRecyclerView != null) {
                universalRecyclerView.adapter.update(true);
                BookmarksFragment.this.listView.layoutManager.scrollToPositionWithOffset(0, 0);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            boolean z = !TextUtils.isEmpty(BookmarksFragment.this.query);
            String obj = editText.getText().toString();
            if (!TextUtils.equals(BookmarksFragment.this.query, obj)) {
                BookmarksFragment.this.query = obj;
                AddressBarList.BookmarksList bookmarksList = BookmarksFragment.this.searchList;
                if (bookmarksList != null) {
                    bookmarksList.detach();
                }
                BookmarksFragment bookmarksFragment = BookmarksFragment.this;
                int i = ((BaseFragment) bookmarksFragment).currentAccount;
                final BookmarksFragment bookmarksFragment2 = BookmarksFragment.this;
                bookmarksFragment.searchList = new AddressBarList.BookmarksList(i, obj, new Runnable() { // from class: org.telegram.ui.web.BookmarksFragment$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        BookmarksFragment.access$400(BookmarksFragment.this);
                    }
                });
                BookmarksFragment.this.searchList.attach();
                scheduleSearch();
            }
            UniversalRecyclerView universalRecyclerView = BookmarksFragment.this.listView;
            if (universalRecyclerView != null) {
                universalRecyclerView.adapter.update(true);
                if (z != (!TextUtils.isEmpty(obj))) {
                    BookmarksFragment.this.listView.layoutManager.scrollToPositionWithOffset(0, 0);
                }
            }
        }
    }

    public BookmarksFragment(Runnable runnable, Utilities.Callback callback) {
        this.closeToTabs = runnable;
        this.whenClicked = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void access$400(BookmarksFragment bookmarksFragment) {
        bookmarksFragment.updateWithOffset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$2(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteSelectedMessages$0(HashSet hashSet, DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).deleteMessages(new ArrayList<>(hashSet), null, null, UserConfig.getInstance(this.currentAccount).getClientUserId(), 0, true, 0);
        this.list.delete(new ArrayList(hashSet));
        AddressBarList.BookmarksList bookmarksList = this.searchList;
        if (bookmarksList != null) {
            bookmarksList.delete(new ArrayList(hashSet));
        }
        this.selected.clear();
        this.actionBar.hideActionMode();
        this.listView.adapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$gotoMessage$1(long j, int i) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment(ChatActivity.of(j, i));
        }
    }

    public static boolean matches(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        String lowerCase2 = str2.toLowerCase();
        if (!lowerCase.startsWith(lowerCase2)) {
            if (!lowerCase.contains(" " + lowerCase2)) {
                if (!lowerCase.contains("." + lowerCase2)) {
                    String translitSafe = AndroidUtilities.translitSafe(lowerCase);
                    String translitSafe2 = AndroidUtilities.translitSafe(lowerCase2);
                    if (!translitSafe.startsWith(translitSafe2)) {
                        if (!translitSafe.contains(" " + translitSafe2)) {
                            if (!translitSafe.contains("." + translitSafe2)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWithOffset() {
        int i;
        int i2 = -1;
        int i3 = 0;
        while (true) {
            if (i3 >= this.listView.getChildCount()) {
                i = 0;
                break;
            }
            View childAt = this.listView.getChildAt(i3);
            int childAdapterPosition = this.listView.getChildAdapterPosition(childAt);
            if (childAdapterPosition >= 0) {
                i = childAt.getTop();
                i2 = childAdapterPosition;
                break;
            } else {
                i3++;
                i2 = childAdapterPosition;
            }
        }
        this.listView.adapter.update(true);
        if (i2 >= 0) {
            this.listView.layoutManager.scrollToPositionWithOffset(i2, i);
        } else {
            this.listView.layoutManager.scrollToPositionWithOffset(0, 0);
        }
    }

    public void clickSelect(UItem uItem, View view) {
        AddressBarList.BookmarkView bookmarkView = (AddressBarList.BookmarkView) view;
        MessageObject messageObject = (MessageObject) uItem.object2;
        if (isSelected(messageObject)) {
            setSelected(messageObject, false);
            bookmarkView.setChecked(false);
        } else {
            setSelected(messageObject, true);
            bookmarkView.setChecked(true);
        }
        this.selectedCount.setNumber(this.selected.size(), true);
        if (this.selected.isEmpty()) {
            this.actionBar.hideActionMode();
        } else {
            this.actionBar.showActionMode();
        }
        AndroidUtilities.updateViewShow(this.gotoItem, this.selected.size() == 1, true, true);
    }

    @Override // org.telegram.ui.Components.UniversalFragment, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.fragmentView = super.createView(context);
        ActionBar actionBar = this.actionBar;
        int i = Theme.key_windowBackgroundWhite;
        actionBar.setBackgroundColor(getThemedColor(i));
        this.actionBar.setActionModeColor(Theme.getColor(i));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        ActionBar actionBar2 = this.actionBar;
        int i2 = Theme.key_windowBackgroundWhiteBlackText;
        actionBar2.setTitleColor(getThemedColor(i2));
        this.actionBar.setItemsBackgroundColor(getThemedColor(Theme.key_actionBarActionModeDefaultSelector), false);
        this.actionBar.setItemsColor(getThemedColor(i2), false);
        this.actionBar.setItemsColor(getThemedColor(i2), true);
        this.actionBar.setCastShadows(true);
        this.actionBar.setActionBarMenuOnItemClick(new 1());
        ActionBarMenu createActionMode = this.actionBar.createActionMode();
        NumberTextView numberTextView = new NumberTextView(createActionMode.getContext());
        this.selectedCount = numberTextView;
        numberTextView.setTextSize(18);
        this.selectedCount.setTypeface(AndroidUtilities.bold());
        this.selectedCount.setTextColor(getThemedColor(Theme.key_actionBarActionModeDefaultIcon));
        this.selectedCount.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.web.BookmarksFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createView$2;
                lambda$createView$2 = BookmarksFragment.lambda$createView$2(view, motionEvent);
                return lambda$createView$2;
            }
        });
        createActionMode.addView(this.selectedCount, LayoutHelper.createLinear(0, -1, 1.0f, 65, 0, 0, 0));
        this.gotoItem = createActionMode.addItemWithWidth(R.id.menu_link, R.drawable.msg_message, AndroidUtilities.dp(54.0f), LocaleController.getString(R.string.AccDescrGoToMessage));
        createActionMode.addItemWithWidth(R.id.menu_delete, R.drawable.msg_delete, AndroidUtilities.dp(54.0f), LocaleController.getString(R.string.Delete));
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search, getResourceProvider()).setIsSearchField(true).setActionBarMenuItemSearchListener(new 2());
        this.searchItem = actionBarMenuItemSearchListener;
        int i3 = R.string.Search;
        actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString(i3));
        this.searchItem.setContentDescription(LocaleController.getString(i3));
        EditTextBoldCursor searchField = this.searchItem.getSearchField();
        searchField.setTextColor(getThemedColor(i2));
        searchField.setHintTextColor(getThemedColor(Theme.key_player_time));
        searchField.setCursorColor(getThemedColor(i2));
        this.listView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.web.BookmarksFragment.3
            /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
                jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r1v10 org.telegram.ui.web.AddressBarList$BookmarksList, still in use, count: 2, list:
                  (r1v10 org.telegram.ui.web.AddressBarList$BookmarksList) from 0x0023: IF  (r1v10 org.telegram.ui.web.AddressBarList$BookmarksList) != (null org.telegram.ui.web.AddressBarList$BookmarksList)  -> B:6:0x001b A[HIDDEN]
                  (r1v10 org.telegram.ui.web.AddressBarList$BookmarksList) from 0x001b: PHI (r1v11 org.telegram.ui.web.AddressBarList$BookmarksList) = (r1v10 org.telegram.ui.web.AddressBarList$BookmarksList) binds: [B:8:0x0023] A[DONT_GENERATE, DONT_INLINE]
                	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
                	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
                	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:125)
                	at jadx.core.dex.visitors.regions.TernaryMod.processRegion(TernaryMod.java:62)
                	at jadx.core.dex.visitors.regions.TernaryMod.enterRegion(TernaryMod.java:45)
                	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:67)
                	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
                	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
                	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
                	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
                	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
                	at jadx.core.dex.visitors.regions.TernaryMod.process(TernaryMod.java:35)
                	at jadx.core.dex.visitors.regions.IfRegionVisitor.process(IfRegionVisitor.java:34)
                	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:30)
                */
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(androidx.recyclerview.widget.RecyclerView r1, int r2, int r3) {
                /*
                    r0 = this;
                    org.telegram.ui.web.BookmarksFragment r1 = org.telegram.ui.web.BookmarksFragment.this
                    org.telegram.ui.Components.UniversalRecyclerView r1 = r1.listView
                    r2 = 1
                    boolean r1 = r1.canScrollVertically(r2)
                    if (r1 != 0) goto L26
                    org.telegram.ui.web.BookmarksFragment r1 = org.telegram.ui.web.BookmarksFragment.this
                    java.lang.String r1 = org.telegram.ui.web.BookmarksFragment.access$200(r1)
                    boolean r1 = android.text.TextUtils.isEmpty(r1)
                    if (r1 == 0) goto L1f
                    org.telegram.ui.web.BookmarksFragment r1 = org.telegram.ui.web.BookmarksFragment.this
                    org.telegram.ui.web.AddressBarList$BookmarksList r1 = r1.list
                L1b:
                    r1.load()
                    goto L26
                L1f:
                    org.telegram.ui.web.BookmarksFragment r1 = org.telegram.ui.web.BookmarksFragment.this
                    org.telegram.ui.web.AddressBarList$BookmarksList r1 = r1.searchList
                    if (r1 == 0) goto L26
                    goto L1b
                L26:
                    org.telegram.ui.web.BookmarksFragment r1 = org.telegram.ui.web.BookmarksFragment.this
                    org.telegram.ui.Components.UniversalRecyclerView r2 = r1.listView
                    boolean r2 = r2.scrollingByUser
                    if (r2 == 0) goto L33
                    android.view.View r1 = r1.fragmentView
                    org.telegram.messenger.AndroidUtilities.hideKeyboard(r1)
                L33:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.web.BookmarksFragment.3.onScrolled(androidx.recyclerview.widget.RecyclerView, int, int):void");
            }
        });
        StickerEmptyView stickerEmptyView = new StickerEmptyView(context, null, 1);
        stickerEmptyView.title.setText(LocaleController.getString(R.string.WebNoBookmarks));
        stickerEmptyView.subtitle.setVisibility(8);
        stickerEmptyView.showProgress(false, false);
        stickerEmptyView.setAnimateLayoutChange(true);
        ((FrameLayout) this.fragmentView).addView(stickerEmptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setEmptyView(stickerEmptyView);
        return this.fragmentView;
    }

    public void deleteSelectedMessages() {
        HashSet hashSet = new HashSet();
        ArrayList arrayList = new ArrayList();
        final HashSet hashSet2 = new HashSet();
        Iterator it = this.selected.iterator();
        while (true) {
            MessageObject messageObject = null;
            if (!it.hasNext()) {
                break;
            }
            int intValue = ((Integer) it.next()).intValue();
            Iterator it2 = this.list.links.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                MessageObject messageObject2 = (MessageObject) it2.next();
                if (messageObject2 != null && messageObject2.getId() == intValue) {
                    messageObject = messageObject2;
                    break;
                }
            }
            AddressBarList.BookmarksList bookmarksList = this.searchList;
            if (bookmarksList != null && messageObject == null) {
                Iterator it3 = bookmarksList.links.iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        break;
                    }
                    MessageObject messageObject3 = (MessageObject) it3.next();
                    if (messageObject3 != null && messageObject3.getId() == intValue) {
                        messageObject = messageObject3;
                        break;
                    }
                }
            }
            if (messageObject != null) {
                arrayList.add(messageObject);
                hashSet2.add(Integer.valueOf(messageObject.getId()));
                hashSet.add(AddressBarList.getLink(messageObject));
            }
        }
        new AlertDialog.Builder(getContext(), getResourceProvider()).setTitle(LocaleController.formatPluralString("DeleteOptionsTitle", hashSet2.size(), new Object[0])).setMessage(LocaleController.getString(hashSet2.size() == 1 ? "AreYouSureUnsaveSingleMessage" : "AreYouSureUnsaveFewMessages")).setPositiveButton(LocaleController.getString(R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BookmarksFragment$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                BookmarksFragment.this.lambda$deleteSelectedMessages$0(hashSet2, dialogInterface, i);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).makeRed(-1).show();
    }

    /* JADX WARN: Code restructure failed: missing block: B:114:0x015a, code lost:
    
        if (r10.searchList.endReached == false) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0060, code lost:
    
        if (r10.list.endReached == false) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x015c, code lost:
    
        r11.add(org.telegram.ui.Components.UItem.asFlicker(r11.size(), 32));
        r11.add(org.telegram.ui.Components.UItem.asFlicker(r11.size(), 32));
        r11.add(org.telegram.ui.Components.UItem.asFlicker(r11.size(), 32));
     */
    @Override // org.telegram.ui.Components.UniversalFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        this.addedUrls.clear();
        if (TextUtils.isEmpty(this.query)) {
            Iterator it = this.list.links.iterator();
            while (it.hasNext()) {
                MessageObject messageObject = (MessageObject) it.next();
                String link = AddressBarList.getLink(messageObject);
                if (!TextUtils.isEmpty(link) && !link.startsWith("#") && !link.startsWith("$") && !link.startsWith("@")) {
                    this.addedUrls.add(link);
                    arrayList.add(AddressBarList.BookmarkView.Factory.as(messageObject, false).setChecked(isSelected(messageObject)));
                }
            }
        } else {
            Iterator it2 = this.list.links.iterator();
            while (it2.hasNext()) {
                MessageObject messageObject2 = (MessageObject) it2.next();
                String link2 = AddressBarList.getLink(messageObject2);
                if (!TextUtils.isEmpty(link2) && !link2.startsWith("#") && !link2.startsWith("$") && !link2.startsWith("@")) {
                    this.addedUrls.add(link2);
                    String hostAuthority = AndroidUtilities.getHostAuthority(link2, true);
                    WebMetadataCache.WebMetadata webMetadata = WebMetadataCache.getInstance().get(hostAuthority);
                    TLRPC.WebPage webPage = (messageObject2 == null || (message = messageObject2.messageOwner) == null || (messageMedia = message.media) == null) ? null : messageMedia.webpage;
                    String str = (webPage == null || TextUtils.isEmpty(webPage.site_name)) ? (webMetadata == null || TextUtils.isEmpty(webMetadata.sitename)) ? null : webMetadata.sitename : webPage.site_name;
                    String str2 = (webPage == null || TextUtils.isEmpty(webPage.title)) ? null : webPage.title;
                    if (matches(hostAuthority, this.query) || matches(str, this.query) || matches(str2, this.query)) {
                        arrayList.add(AddressBarList.BookmarkView.Factory.as(messageObject2, false, this.query).setChecked(isSelected(messageObject2)));
                    }
                }
            }
            Iterator it3 = this.searchList.links.iterator();
            while (it3.hasNext()) {
                MessageObject messageObject3 = (MessageObject) it3.next();
                String link3 = AddressBarList.getLink(messageObject3);
                if (!TextUtils.isEmpty(link3) && !link3.startsWith("#") && !link3.startsWith("$") && !link3.startsWith("@")) {
                    this.addedUrls.add(link3);
                    arrayList.add(AddressBarList.BookmarkView.Factory.as(messageObject3, false, this.query).setChecked(isSelected(messageObject3)));
                }
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        arrayList.add(UItem.asShadow(null));
    }

    @Override // org.telegram.ui.Components.UniversalFragment
    protected CharSequence getTitle() {
        return LocaleController.getString(R.string.WebBookmarks);
    }

    public void gotoMessage() {
        if (this.selected.size() != 1) {
            return;
        }
        final long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        final int intValue = ((Integer) this.selected.iterator().next()).intValue();
        lambda$onBackPressed$321();
        Runnable runnable = this.closeToTabs;
        if (runnable != null) {
            runnable.run();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BookmarksFragment$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BookmarksFragment.lambda$gotoMessage$1(clientUserId, intValue);
            }
        }, 80L);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return AndroidUtilities.computePerceivedBrightness(getThemedColor(Theme.key_windowBackgroundWhite)) > 0.721f;
    }

    public boolean isSelected(MessageObject messageObject) {
        return messageObject != null && this.selected.contains(Integer.valueOf(messageObject.getId()));
    }

    @Override // org.telegram.ui.Components.UniversalFragment
    protected void onClick(UItem uItem, View view, int i, float f, float f2) {
        if (uItem.instanceOf(AddressBarList.BookmarkView.Factory.class)) {
            if (this.actionBar.isActionModeShowed()) {
                clickSelect(uItem, view);
            } else {
                lambda$onBackPressed$321();
                this.whenClicked.run(AddressBarList.getLink((MessageObject) uItem.object2));
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        this.list.attach();
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        this.list.detach();
    }

    @Override // org.telegram.ui.Components.UniversalFragment
    protected boolean onLongClick(UItem uItem, View view, int i, float f, float f2) {
        if (!uItem.instanceOf(AddressBarList.BookmarkView.Factory.class)) {
            return false;
        }
        clickSelect(uItem, view);
        return true;
    }

    public void setSelected(MessageObject messageObject, boolean z) {
        if (messageObject == null) {
            return;
        }
        if (z) {
            this.selected.add(Integer.valueOf(messageObject.getId()));
        } else {
            this.selected.remove(Integer.valueOf(messageObject.getId()));
        }
    }
}
