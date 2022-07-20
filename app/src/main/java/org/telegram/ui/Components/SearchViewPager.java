package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.MenuDrawable;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
import org.telegram.ui.Adapters.FiltersView;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.HashtagSearchCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.SharedAudioCell;
import org.telegram.ui.Cells.SharedDocumentCell;
import org.telegram.ui.Cells.SharedLinkCell;
import org.telegram.ui.Cells.SharedPhotoVideoCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ViewPagerFixed;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.FilteredSearchView;
/* loaded from: classes3.dex */
public class SearchViewPager extends ViewPagerFixed implements FilteredSearchView.UiCallback {
    private boolean attached;
    ChatPreviewDelegate chatPreviewDelegate;
    private ActionBarMenuItem deleteItem;
    public DialogsSearchAdapter dialogsSearchAdapter;
    public StickerEmptyView emptyView;
    private FilteredSearchView.Delegate filteredSearchViewDelegate;
    private final int folderId;
    private ActionBarMenuItem forwardItem;
    SizeNotifierFrameLayout fragmentView;
    private ActionBarMenuItem gotoItem;
    private boolean isActionModeShowed;
    private DefaultItemAnimator itemAnimator;
    private RecyclerItemsEnterAnimator itemsEnterAnimator;
    private int keyboardSize;
    private boolean lastSearchScrolledToTop;
    String lastSearchString;
    private FilteredSearchView noMediaFiltersSearchView;
    BaseFragment parent;
    public FrameLayout searchContainer;
    private LinearLayoutManager searchLayoutManager;
    public RecyclerListView searchListView;
    private NumberTextView selectedMessagesCountTextView;
    private boolean showOnlyDialogsAdapter;
    private final ViewPagerAdapter viewPagerAdapter;
    private HashMap<FilteredSearchView.MessageHashId, MessageObject> selectedFiles = new HashMap<>();
    private ArrayList<FiltersView.MediaFilterData> currentSearchFilters = new ArrayList<>();
    int currentAccount = UserConfig.selectedAccount;
    int animateFromCount = 0;

    /* loaded from: classes3.dex */
    public interface ChatPreviewDelegate {
        void finish();

        void move(float f);

        void startChatPreview(RecyclerListView recyclerListView, DialogCell dialogCell);
    }

    public static /* synthetic */ boolean lambda$showActionMode$0(View view, MotionEvent motionEvent) {
        return true;
    }

    public SearchViewPager(Context context, DialogsActivity dialogsActivity, int i, int i2, int i3, ChatPreviewDelegate chatPreviewDelegate) {
        super(context);
        this.folderId = i3;
        this.parent = dialogsActivity;
        this.chatPreviewDelegate = chatPreviewDelegate;
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        this.itemAnimator = defaultItemAnimator;
        defaultItemAnimator.setAddDuration(150L);
        this.itemAnimator.setMoveDuration(350L);
        this.itemAnimator.setChangeDuration(0L);
        this.itemAnimator.setRemoveDuration(0L);
        this.itemAnimator.setMoveInterpolator(new OvershootInterpolator(1.1f));
        this.itemAnimator.setTranslationInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.dialogsSearchAdapter = new AnonymousClass1(context, i, i2, this.itemAnimator);
        this.fragmentView = (SizeNotifierFrameLayout) dialogsActivity.getFragmentView();
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.searchListView = anonymousClass2;
        anonymousClass2.setItemAnimator(this.itemAnimator);
        this.searchListView.setPivotY(0.0f);
        this.searchListView.setAdapter(this.dialogsSearchAdapter);
        this.searchListView.setVerticalScrollBarEnabled(true);
        this.searchListView.setInstantClick(true);
        this.searchListView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        RecyclerListView recyclerListView = this.searchListView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.searchLayoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        this.searchListView.setAnimateEmptyView(true, 0);
        this.searchListView.setOnScrollListener(new AnonymousClass3(dialogsActivity));
        FilteredSearchView filteredSearchView = new FilteredSearchView(this.parent);
        this.noMediaFiltersSearchView = filteredSearchView;
        filteredSearchView.setUiCallback(this);
        this.noMediaFiltersSearchView.setVisibility(8);
        this.noMediaFiltersSearchView.setChatPreviewDelegate(chatPreviewDelegate);
        this.searchContainer = new FrameLayout(context);
        FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
        flickerLoadingView.setViewType(1);
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, flickerLoadingView, 1);
        this.emptyView = anonymousClass4;
        anonymousClass4.title.setText(LocaleController.getString("NoResult", 2131626910));
        this.emptyView.subtitle.setVisibility(8);
        this.emptyView.setVisibility(8);
        this.emptyView.addView(flickerLoadingView, 0);
        this.emptyView.showProgress(true, false);
        this.searchContainer.addView(this.emptyView);
        this.searchContainer.addView(this.searchListView);
        this.searchContainer.addView(this.noMediaFiltersSearchView);
        this.searchListView.setEmptyView(this.emptyView);
        this.searchListView.addOnScrollListener(new AnonymousClass5());
        this.itemsEnterAnimator = new RecyclerItemsEnterAnimator(this.searchListView, true);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        this.viewPagerAdapter = viewPagerAdapter;
        setAdapter(viewPagerAdapter);
    }

    /* renamed from: org.telegram.ui.Components.SearchViewPager$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends DialogsSearchAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, int i, int i2, DefaultItemAnimator defaultItemAnimator) {
            super(context, i, i2, defaultItemAnimator);
            SearchViewPager.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            RecyclerListView recyclerListView;
            int currentItemCount = getCurrentItemCount();
            super.notifyDataSetChanged();
            if (!SearchViewPager.this.lastSearchScrolledToTop && (recyclerListView = SearchViewPager.this.searchListView) != null) {
                recyclerListView.scrollToPosition(0);
                SearchViewPager.this.lastSearchScrolledToTop = true;
            }
            if (getItemCount() != 0 || currentItemCount == 0 || isSearching()) {
                return;
            }
            SearchViewPager.this.emptyView.showProgress(false, false);
        }
    }

    /* renamed from: org.telegram.ui.Components.SearchViewPager$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends BlurredRecyclerView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            SearchViewPager.this = r1;
        }

        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            SearchViewPager searchViewPager = SearchViewPager.this;
            if (searchViewPager.dialogsSearchAdapter != null && searchViewPager.itemAnimator != null && SearchViewPager.this.searchLayoutManager != null && SearchViewPager.this.dialogsSearchAdapter.showMoreAnimation) {
                canvas.save();
                invalidate();
                int itemCount = SearchViewPager.this.dialogsSearchAdapter.getItemCount() - 1;
                int i = 0;
                while (true) {
                    if (i >= getChildCount()) {
                        break;
                    }
                    View childAt = getChildAt(i);
                    if (getChildAdapterPosition(childAt) == itemCount) {
                        canvas.clipRect(0.0f, 0.0f, getWidth(), childAt.getBottom() + childAt.getTranslationY());
                        break;
                    }
                    i++;
                }
            }
            super.dispatchDraw(canvas);
            SearchViewPager searchViewPager2 = SearchViewPager.this;
            if (searchViewPager2.dialogsSearchAdapter != null && searchViewPager2.itemAnimator != null && SearchViewPager.this.searchLayoutManager != null && SearchViewPager.this.dialogsSearchAdapter.showMoreAnimation) {
                canvas.restore();
            }
            DialogsSearchAdapter dialogsSearchAdapter = SearchViewPager.this.dialogsSearchAdapter;
            if (dialogsSearchAdapter == null || dialogsSearchAdapter.showMoreHeader == null) {
                return;
            }
            canvas.save();
            canvas.translate(SearchViewPager.this.dialogsSearchAdapter.showMoreHeader.getLeft(), SearchViewPager.this.dialogsSearchAdapter.showMoreHeader.getTop() + SearchViewPager.this.dialogsSearchAdapter.showMoreHeader.getTranslationY());
            SearchViewPager.this.dialogsSearchAdapter.showMoreHeader.draw(canvas);
            canvas.restore();
        }
    }

    /* renamed from: org.telegram.ui.Components.SearchViewPager$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends RecyclerView.OnScrollListener {
        final /* synthetic */ DialogsActivity val$fragment;

        AnonymousClass3(DialogsActivity dialogsActivity) {
            SearchViewPager.this = r1;
            this.val$fragment = dialogsActivity;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                AndroidUtilities.hideKeyboard(this.val$fragment.getParentActivity().getCurrentFocus());
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            int abs = Math.abs(SearchViewPager.this.searchLayoutManager.findLastVisibleItemPosition() - SearchViewPager.this.searchLayoutManager.findFirstVisibleItemPosition()) + 1;
            int itemCount = recyclerView.getAdapter().getItemCount();
            if (abs > 0 && SearchViewPager.this.searchLayoutManager.findLastVisibleItemPosition() == itemCount - 1 && !SearchViewPager.this.dialogsSearchAdapter.isMessagesSearchEndReached()) {
                SearchViewPager.this.dialogsSearchAdapter.loadMoreSearchMessages();
            }
            SearchViewPager.this.fragmentView.invalidateBlur();
        }
    }

    /* renamed from: org.telegram.ui.Components.SearchViewPager$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends StickerEmptyView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, View view, int i) {
            super(context, view, i);
            SearchViewPager.this = r1;
        }

        @Override // org.telegram.ui.Components.StickerEmptyView, android.view.View
        public void setVisibility(int i) {
            if (SearchViewPager.this.noMediaFiltersSearchView.getTag() != null) {
                super.setVisibility(8);
            } else {
                super.setVisibility(i);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.SearchViewPager$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends RecyclerView.OnScrollListener {
        AnonymousClass5() {
            SearchViewPager.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            SearchViewPager.this.fragmentView.invalidateBlur();
        }
    }

    public void onTextChanged(String str) {
        View currentView = getCurrentView();
        boolean z = true;
        boolean z2 = !this.attached;
        if (!TextUtils.isEmpty(this.lastSearchString)) {
            z = z2;
        }
        this.lastSearchString = str;
        search(currentView, getCurrentPosition(), str, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void search(View view, int i, String str, boolean z) {
        boolean z2;
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < this.currentSearchFilters.size(); i3++) {
            FiltersView.MediaFilterData mediaFilterData = this.currentSearchFilters.get(i3);
            int i4 = mediaFilterData.filterType;
            if (i4 == 4) {
                TLObject tLObject = mediaFilterData.chat;
                if (tLObject instanceof TLRPC$User) {
                    j = ((TLRPC$User) tLObject).id;
                } else if (tLObject instanceof TLRPC$Chat) {
                    j = -((TLRPC$Chat) tLObject).id;
                }
            } else if (i4 == 6) {
                FiltersView.DateData dateData = mediaFilterData.dateData;
                long j4 = dateData.minDate;
                long j5 = dateData.maxDate;
                j2 = j4;
                j3 = j5;
            } else if (i4 == 7) {
                i2 = 1;
            }
        }
        if (view == this.searchContainer) {
            if (j == 0 && j2 == 0 && j3 == 0) {
                this.lastSearchScrolledToTop = false;
                this.dialogsSearchAdapter.searchDialogs(str, i2);
                this.dialogsSearchAdapter.setFiltersDelegate(this.filteredSearchViewDelegate, false);
                this.noMediaFiltersSearchView.animate().setListener(null).cancel();
                this.noMediaFiltersSearchView.setDelegate(null, false);
                if (z) {
                    this.emptyView.showProgress(!this.dialogsSearchAdapter.isSearching(), false);
                    this.emptyView.showProgress(this.dialogsSearchAdapter.isSearching(), false);
                } else if (!this.dialogsSearchAdapter.hasRecentSearch()) {
                    this.emptyView.showProgress(this.dialogsSearchAdapter.isSearching(), true);
                }
                if (z) {
                    this.noMediaFiltersSearchView.setVisibility(8);
                } else if (this.noMediaFiltersSearchView.getVisibility() != 8) {
                    this.noMediaFiltersSearchView.animate().alpha(0.0f).setListener(new AnonymousClass6()).setDuration(150L).start();
                }
                this.noMediaFiltersSearchView.setTag(null);
            } else {
                this.noMediaFiltersSearchView.setTag(1);
                this.noMediaFiltersSearchView.setDelegate(this.filteredSearchViewDelegate, false);
                this.noMediaFiltersSearchView.animate().setListener(null).cancel();
                if (z) {
                    this.noMediaFiltersSearchView.setVisibility(0);
                    this.noMediaFiltersSearchView.setAlpha(1.0f);
                    z2 = z;
                } else {
                    if (this.noMediaFiltersSearchView.getVisibility() != 0) {
                        this.noMediaFiltersSearchView.setVisibility(0);
                        this.noMediaFiltersSearchView.setAlpha(0.0f);
                        z2 = true;
                    } else {
                        z2 = z;
                    }
                    this.noMediaFiltersSearchView.animate().alpha(1.0f).setDuration(150L).start();
                }
                this.noMediaFiltersSearchView.search(j, j2, j3, null, i2, str, z2);
                this.emptyView.setVisibility(8);
            }
            this.emptyView.setKeyboardHeight(this.keyboardSize, false);
            this.noMediaFiltersSearchView.setKeyboardHeight(this.keyboardSize, false);
        } else if (view instanceof FilteredSearchView) {
            FilteredSearchView filteredSearchView = (FilteredSearchView) view;
            filteredSearchView.setKeyboardHeight(this.keyboardSize, false);
            filteredSearchView.search(j, j2, j3, FiltersView.filters[this.viewPagerAdapter.items.get(i).filterIndex], i2, str, z);
        } else if (view instanceof SearchDownloadsContainer) {
            SearchDownloadsContainer searchDownloadsContainer = (SearchDownloadsContainer) view;
            searchDownloadsContainer.setKeyboardHeight(this.keyboardSize, false);
            searchDownloadsContainer.search(str);
        }
    }

    /* renamed from: org.telegram.ui.Components.SearchViewPager$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends AnimatorListenerAdapter {
        AnonymousClass6() {
            SearchViewPager.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            SearchViewPager.this.noMediaFiltersSearchView.setVisibility(8);
        }
    }

    public void onResume() {
        DialogsSearchAdapter dialogsSearchAdapter = this.dialogsSearchAdapter;
        if (dialogsSearchAdapter != null) {
            dialogsSearchAdapter.notifyDataSetChanged();
        }
    }

    public void removeSearchFilter(FiltersView.MediaFilterData mediaFilterData) {
        this.currentSearchFilters.remove(mediaFilterData);
    }

    public ArrayList<FiltersView.MediaFilterData> getCurrentSearchFilters() {
        return this.currentSearchFilters;
    }

    public void clear() {
        this.currentSearchFilters.clear();
    }

    public void setFilteredSearchViewDelegate(FilteredSearchView.Delegate delegate) {
        this.filteredSearchViewDelegate = delegate;
    }

    private void showActionMode(boolean z) {
        if (this.isActionModeShowed == z) {
            return;
        }
        if (z && this.parent.getActionBar().isActionModeShowed()) {
            return;
        }
        if (z && !this.parent.getActionBar().actionModeIsExist("search_view_pager")) {
            ActionBarMenu createActionMode = this.parent.getActionBar().createActionMode(true, "search_view_pager");
            NumberTextView numberTextView = new NumberTextView(createActionMode.getContext());
            this.selectedMessagesCountTextView = numberTextView;
            numberTextView.setTextSize(18);
            this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.selectedMessagesCountTextView.setTextColor(Theme.getColor("actionBarActionModeDefaultIcon"));
            createActionMode.addView(this.selectedMessagesCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 72, 0, 0, 0));
            this.selectedMessagesCountTextView.setOnTouchListener(SearchViewPager$$ExternalSyntheticLambda2.INSTANCE);
            this.gotoItem = createActionMode.addItemWithWidth(200, 2131165801, AndroidUtilities.dp(54.0f), LocaleController.getString("AccDescrGoToMessage", 2131623988));
            this.forwardItem = createActionMode.addItemWithWidth(201, 2131165741, AndroidUtilities.dp(54.0f), LocaleController.getString("Forward", 2131625981));
            this.deleteItem = createActionMode.addItemWithWidth(202, 2131165702, AndroidUtilities.dp(54.0f), LocaleController.getString("Delete", 2131625384));
        }
        if (this.parent.getActionBar().getBackButton().getDrawable() instanceof MenuDrawable) {
            this.parent.getActionBar().setBackButtonDrawable(new BackDrawable(false));
        }
        this.isActionModeShowed = z;
        if (z) {
            AndroidUtilities.hideKeyboard(this.parent.getParentActivity().getCurrentFocus());
            this.parent.getActionBar().showActionMode();
            this.selectedMessagesCountTextView.setNumber(this.selectedFiles.size(), false);
            this.gotoItem.setVisibility(0);
            this.forwardItem.setVisibility(0);
            this.deleteItem.setVisibility(0);
            return;
        }
        this.parent.getActionBar().hideActionMode();
        this.selectedFiles.clear();
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof FilteredSearchView) {
                ((FilteredSearchView) getChildAt(i)).update();
            }
            if (getChildAt(i) instanceof SearchDownloadsContainer) {
                ((SearchDownloadsContainer) getChildAt(i)).update(true);
            }
        }
        FilteredSearchView filteredSearchView = this.noMediaFiltersSearchView;
        if (filteredSearchView != null) {
            filteredSearchView.update();
        }
        int size = this.viewsByType.size();
        for (int i2 = 0; i2 < size; i2++) {
            View valueAt = this.viewsByType.valueAt(i2);
            if (valueAt instanceof FilteredSearchView) {
                ((FilteredSearchView) valueAt).update();
            }
        }
    }

    public void onActionBarItemClick(int i) {
        if (i != 202) {
            if (i == 200) {
                if (this.selectedFiles.size() != 1) {
                    return;
                }
                goToMessage(this.selectedFiles.values().iterator().next());
                return;
            } else if (i != 201) {
                return;
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlySelect", true);
                bundle.putInt("dialogsType", 3);
                DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                dialogsActivity.setDelegate(new SearchViewPager$$ExternalSyntheticLambda3(this));
                this.parent.presentFragment(dialogsActivity);
                return;
            }
        }
        BaseFragment baseFragment = this.parent;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.selectedFiles.values());
        AlertDialog.Builder builder = new AlertDialog.Builder(this.parent.getParentActivity());
        builder.setTitle(LocaleController.formatPluralString("RemoveDocumentsTitle", this.selectedFiles.size(), new Object[0]));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatPluralString("RemoveDocumentsMessage", this.selectedFiles.size(), new Object[0]))).append((CharSequence) "\n\n").append((CharSequence) LocaleController.getString("RemoveDocumentsAlertMessage", 2131627951));
        builder.setMessage(spannableStringBuilder);
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), SearchViewPager$$ExternalSyntheticLambda1.INSTANCE);
        builder.setPositiveButton(LocaleController.getString("Delete", 2131625384), new SearchViewPager$$ExternalSyntheticLambda0(this, arrayList));
        TextView textView = (TextView) builder.show().getButton(-1);
        if (textView == null) {
            return;
        }
        textView.setTextColor(Theme.getColor("dialogTextRed2"));
    }

    public /* synthetic */ void lambda$onActionBarItemClick$2(ArrayList arrayList, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        this.parent.getDownloadController().deleteRecentFiles(arrayList);
        hideActionMode();
    }

    public /* synthetic */ void lambda$onActionBarItemClick$3(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        ArrayList<MessageObject> arrayList2 = new ArrayList<>();
        for (FilteredSearchView.MessageHashId messageHashId : this.selectedFiles.keySet()) {
            arrayList2.add(this.selectedFiles.get(messageHashId));
        }
        this.selectedFiles.clear();
        showActionMode(false);
        if (arrayList.size() > 1 || ((Long) arrayList.get(0)).longValue() == AccountInstance.getInstance(this.currentAccount).getUserConfig().getClientUserId() || charSequence != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                long longValue = ((Long) arrayList.get(i)).longValue();
                if (charSequence != null) {
                    AccountInstance.getInstance(this.currentAccount).getSendMessagesHelper().sendMessage(charSequence.toString(), longValue, null, null, null, true, null, null, null, true, 0, null);
                }
                AccountInstance.getInstance(this.currentAccount).getSendMessagesHelper().sendMessage(arrayList2, longValue, false, false, true, 0);
            }
            dialogsActivity.finishFragment();
            return;
        }
        long longValue2 = ((Long) arrayList.get(0)).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(longValue2)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue2));
        } else {
            if (DialogObject.isUserDialog(longValue2)) {
                bundle.putLong("user_id", longValue2);
            } else {
                bundle.putLong("chat_id", -longValue2);
            }
            if (!AccountInstance.getInstance(this.currentAccount).getMessagesController().checkCanOpenChat(bundle, dialogsActivity)) {
                return;
            }
        }
        ChatActivity chatActivity = new ChatActivity(bundle);
        dialogsActivity.presentFragment(chatActivity, true);
        chatActivity.showFieldPanelForForward(true, arrayList2);
    }

    @Override // org.telegram.ui.FilteredSearchView.UiCallback
    public void goToMessage(MessageObject messageObject) {
        Bundle bundle = new Bundle();
        long dialogId = messageObject.getDialogId();
        if (DialogObject.isEncryptedDialog(dialogId)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(dialogId));
        } else if (DialogObject.isUserDialog(dialogId)) {
            bundle.putLong("user_id", dialogId);
        } else {
            TLRPC$Chat chat = AccountInstance.getInstance(this.currentAccount).getMessagesController().getChat(Long.valueOf(-dialogId));
            if (chat != null && chat.migrated_to != null) {
                bundle.putLong("migrated_to", dialogId);
                dialogId = -chat.migrated_to.channel_id;
            }
            bundle.putLong("chat_id", -dialogId);
        }
        bundle.putInt("message_id", messageObject.getId());
        this.parent.presentFragment(new ChatActivity(bundle));
        showActionMode(false);
    }

    public int getFolderId() {
        return this.folderId;
    }

    @Override // org.telegram.ui.FilteredSearchView.UiCallback
    public boolean actionModeShowing() {
        return this.isActionModeShowed;
    }

    public void hideActionMode() {
        showActionMode(false);
    }

    @Override // org.telegram.ui.FilteredSearchView.UiCallback
    public void toggleItemSelection(MessageObject messageObject, View view, int i) {
        boolean z;
        FilteredSearchView.MessageHashId messageHashId = new FilteredSearchView.MessageHashId(messageObject.getId(), messageObject.getDialogId());
        if (this.selectedFiles.containsKey(messageHashId)) {
            this.selectedFiles.remove(messageHashId);
        } else if (this.selectedFiles.size() >= 100) {
            return;
        } else {
            this.selectedFiles.put(messageHashId, messageObject);
        }
        int i2 = 0;
        if (this.selectedFiles.size() == 0) {
            showActionMode(false);
        } else {
            this.selectedMessagesCountTextView.setNumber(this.selectedFiles.size(), true);
            ActionBarMenuItem actionBarMenuItem = this.gotoItem;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.setVisibility(this.selectedFiles.size() == 1 ? 0 : 8);
            }
            if (this.deleteItem != null) {
                Iterator<FilteredSearchView.MessageHashId> it = this.selectedFiles.keySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = true;
                        break;
                    }
                    if (!this.selectedFiles.get(it.next()).isDownloadingFile) {
                        z = false;
                        break;
                    }
                }
                ActionBarMenuItem actionBarMenuItem2 = this.deleteItem;
                if (!z) {
                    i2 = 8;
                }
                actionBarMenuItem2.setVisibility(i2);
            }
        }
        if (view instanceof SharedDocumentCell) {
            ((SharedDocumentCell) view).setChecked(this.selectedFiles.containsKey(messageHashId), true);
        } else if (view instanceof SharedPhotoVideoCell) {
            ((SharedPhotoVideoCell) view).setChecked(i, this.selectedFiles.containsKey(messageHashId), true);
        } else if (view instanceof SharedLinkCell) {
            ((SharedLinkCell) view).setChecked(this.selectedFiles.containsKey(messageHashId), true);
        } else if (view instanceof SharedAudioCell) {
            ((SharedAudioCell) view).setChecked(this.selectedFiles.containsKey(messageHashId), true);
        } else if (view instanceof ContextLinkCell) {
            ((ContextLinkCell) view).setChecked(this.selectedFiles.containsKey(messageHashId), true);
        } else if (!(view instanceof DialogCell)) {
        } else {
            ((DialogCell) view).setChecked(this.selectedFiles.containsKey(messageHashId), true);
        }
    }

    @Override // org.telegram.ui.FilteredSearchView.UiCallback
    public boolean isSelected(FilteredSearchView.MessageHashId messageHashId) {
        return this.selectedFiles.containsKey(messageHashId);
    }

    @Override // org.telegram.ui.FilteredSearchView.UiCallback
    public void showActionMode() {
        showActionMode(true);
    }

    @Override // org.telegram.ui.Components.ViewPagerFixed
    protected void onItemSelected(View view, View view2, int i, int i2) {
        boolean z = true;
        if (i == 0) {
            if (this.noMediaFiltersSearchView.getVisibility() == 0) {
                this.noMediaFiltersSearchView.setDelegate(this.filteredSearchViewDelegate, false);
                this.dialogsSearchAdapter.setFiltersDelegate(null, false);
            } else {
                this.noMediaFiltersSearchView.setDelegate(null, false);
                this.dialogsSearchAdapter.setFiltersDelegate(this.filteredSearchViewDelegate, true);
            }
        } else if (view instanceof FilteredSearchView) {
            if (i2 != 0 || this.noMediaFiltersSearchView.getVisibility() == 0) {
                z = false;
            }
            ((FilteredSearchView) view).setDelegate(this.filteredSearchViewDelegate, z);
        }
        if (view2 instanceof FilteredSearchView) {
            ((FilteredSearchView) view2).setDelegate(null, false);
            return;
        }
        this.dialogsSearchAdapter.setFiltersDelegate(null, false);
        this.noMediaFiltersSearchView.setDelegate(null, false);
    }

    public void getThemeDescriptions(ArrayList<ThemeDescription> arrayList) {
        for (int i = 0; i < this.searchListView.getChildCount(); i++) {
            View childAt = this.searchListView.getChildAt(i);
            if ((childAt instanceof ProfileSearchCell) || (childAt instanceof DialogCell) || (childAt instanceof HashtagSearchCell)) {
                arrayList.add(new ThemeDescription(childAt, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
            }
        }
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if (getChildAt(i2) instanceof FilteredSearchView) {
                arrayList.addAll(((FilteredSearchView) getChildAt(i2)).getThemeDescriptions());
            }
        }
        int size = this.viewsByType.size();
        for (int i3 = 0; i3 < size; i3++) {
            View valueAt = this.viewsByType.valueAt(i3);
            if (valueAt instanceof FilteredSearchView) {
                arrayList.addAll(((FilteredSearchView) valueAt).getThemeDescriptions());
            }
        }
        FilteredSearchView filteredSearchView = this.noMediaFiltersSearchView;
        if (filteredSearchView != null) {
            arrayList.addAll(filteredSearchView.getThemeDescriptions());
        }
        arrayList.add(new ThemeDescription(this.emptyView.title, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.emptyView.subtitle, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText"));
    }

    public void updateColors() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof FilteredSearchView) {
                RecyclerListView recyclerListView = ((FilteredSearchView) getChildAt(i)).recyclerListView;
                int childCount = recyclerListView.getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    View childAt = recyclerListView.getChildAt(i2);
                    if (childAt instanceof DialogCell) {
                        ((DialogCell) childAt).update(0);
                    }
                }
            }
        }
        int size = this.viewsByType.size();
        for (int i3 = 0; i3 < size; i3++) {
            View valueAt = this.viewsByType.valueAt(i3);
            if (valueAt instanceof FilteredSearchView) {
                RecyclerListView recyclerListView2 = ((FilteredSearchView) valueAt).recyclerListView;
                int childCount2 = recyclerListView2.getChildCount();
                for (int i4 = 0; i4 < childCount2; i4++) {
                    View childAt2 = recyclerListView2.getChildAt(i4);
                    if (childAt2 instanceof DialogCell) {
                        ((DialogCell) childAt2).update(0);
                    }
                }
            }
        }
        FilteredSearchView filteredSearchView = this.noMediaFiltersSearchView;
        if (filteredSearchView != null) {
            RecyclerListView recyclerListView3 = filteredSearchView.recyclerListView;
            int childCount3 = recyclerListView3.getChildCount();
            for (int i5 = 0; i5 < childCount3; i5++) {
                View childAt3 = recyclerListView3.getChildAt(i5);
                if (childAt3 instanceof DialogCell) {
                    ((DialogCell) childAt3).update(0);
                }
            }
        }
    }

    public void reset() {
        setPosition(0);
        if (this.dialogsSearchAdapter.getItemCount() > 0) {
            this.searchLayoutManager.scrollToPositionWithOffset(0, 0);
        }
        this.viewsByType.clear();
    }

    @Override // org.telegram.ui.Components.ViewPagerFixed
    public void setPosition(int i) {
        if (i < 0) {
            return;
        }
        super.setPosition(i);
        this.viewsByType.clear();
        ViewPagerFixed.TabsView tabsView = this.tabsView;
        if (tabsView != null) {
            tabsView.selectTabWithId(i, 1.0f);
        }
        invalidate();
    }

    public void setKeyboardHeight(int i) {
        this.keyboardSize = i;
        boolean z = getVisibility() == 0 && getAlpha() > 0.0f;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if (getChildAt(i2) instanceof FilteredSearchView) {
                ((FilteredSearchView) getChildAt(i2)).setKeyboardHeight(i, z);
            } else if (getChildAt(i2) == this.searchContainer) {
                this.emptyView.setKeyboardHeight(i, z);
                this.noMediaFiltersSearchView.setKeyboardHeight(i, z);
            } else if (getChildAt(i2) instanceof SearchDownloadsContainer) {
                ((SearchDownloadsContainer) getChildAt(i2)).setKeyboardHeight(i, z);
            }
        }
    }

    public void showOnlyDialogsAdapter(boolean z) {
        this.showOnlyDialogsAdapter = z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x007c, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r7, r11.currentAccount) != false) goto L27;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void messagesDeleted(long j, ArrayList<Integer> arrayList) {
        int i;
        int size = this.viewsByType.size();
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            View valueAt = this.viewsByType.valueAt(i3);
            if (valueAt instanceof FilteredSearchView) {
                ((FilteredSearchView) valueAt).messagesDeleted(j, arrayList);
            }
        }
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            if (getChildAt(i4) instanceof FilteredSearchView) {
                ((FilteredSearchView) getChildAt(i4)).messagesDeleted(j, arrayList);
            }
        }
        this.noMediaFiltersSearchView.messagesDeleted(j, arrayList);
        if (!this.selectedFiles.isEmpty()) {
            ArrayList arrayList2 = null;
            ArrayList arrayList3 = new ArrayList(this.selectedFiles.keySet());
            for (int i5 = 0; i5 < arrayList3.size(); i5++) {
                FilteredSearchView.MessageHashId messageHashId = (FilteredSearchView.MessageHashId) arrayList3.get(i5);
                MessageObject messageObject = this.selectedFiles.get(messageHashId);
                if (messageObject != null) {
                    long dialogId = messageObject.getDialogId();
                    if (dialogId < 0) {
                        i = (int) (-dialogId);
                    }
                    i = 0;
                    if (i == j) {
                        for (int i6 = 0; i6 < arrayList.size(); i6++) {
                            if (messageObject.getId() == arrayList.get(i6).intValue()) {
                                arrayList2 = new ArrayList();
                                arrayList2.add(messageHashId);
                            }
                        }
                    }
                }
            }
            if (arrayList2 == null) {
                return;
            }
            int size2 = arrayList2.size();
            for (int i7 = 0; i7 < size2; i7++) {
                this.selectedFiles.remove(arrayList2.get(i7));
            }
            this.selectedMessagesCountTextView.setNumber(this.selectedFiles.size(), true);
            ActionBarMenuItem actionBarMenuItem = this.gotoItem;
            if (actionBarMenuItem == null) {
                return;
            }
            if (this.selectedFiles.size() != 1) {
                i2 = 8;
            }
            actionBarMenuItem.setVisibility(i2);
        }
    }

    public void runResultsEnterAnimation() {
        RecyclerItemsEnterAnimator recyclerItemsEnterAnimator = this.itemsEnterAnimator;
        int i = this.animateFromCount;
        recyclerItemsEnterAnimator.showItemsAnimated(i > 0 ? i + 1 : 0);
        this.animateFromCount = this.dialogsSearchAdapter.getItemCount();
    }

    public ViewPagerFixed.TabsView getTabsView() {
        return this.tabsView;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attached = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attached = false;
    }

    @Override // org.telegram.ui.Components.ViewPagerFixed
    protected void invalidateBlur() {
        this.fragmentView.invalidateBlur();
    }

    public void cancelEnterAnimation() {
        this.itemsEnterAnimator.cancel();
        this.searchListView.invalidate();
        this.animateFromCount = 0;
    }

    public void showDownloads() {
        setPosition(2);
    }

    public int getPositionForType(int i) {
        for (int i2 = 0; i2 < this.viewPagerAdapter.items.size(); i2++) {
            if (this.viewPagerAdapter.items.get(i2).type == 2 && this.viewPagerAdapter.items.get(i2).filterIndex == i) {
                return i2;
            }
        }
        return -1;
    }

    /* loaded from: classes3.dex */
    public class ViewPagerAdapter extends ViewPagerFixed.Adapter {
        ArrayList<Item> items;

        public ViewPagerAdapter() {
            SearchViewPager.this = r5;
            ArrayList<Item> arrayList = new ArrayList<>();
            this.items = arrayList;
            arrayList.add(new Item(this, 0, null));
            if (!r5.showOnlyDialogsAdapter) {
                Item item = new Item(this, 2, null);
                item.filterIndex = 0;
                this.items.add(item);
                this.items.add(new Item(this, 1, null));
                Item item2 = new Item(this, 2, null);
                item2.filterIndex = 1;
                this.items.add(item2);
                Item item3 = new Item(this, 2, null);
                item3.filterIndex = 2;
                this.items.add(item3);
                Item item4 = new Item(this, 2, null);
                item4.filterIndex = 3;
                this.items.add(item4);
                Item item5 = new Item(this, 2, null);
                item5.filterIndex = 4;
                this.items.add(item5);
            }
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public String getItemTitle(int i) {
            if (this.items.get(i).type == 0) {
                return LocaleController.getString("SearchAllChatsShort", 2131628155);
            }
            if (this.items.get(i).type == 1) {
                return LocaleController.getString("DownloadsTabs", 2131625551);
            }
            return FiltersView.filters[this.items.get(i).filterIndex].title;
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public int getItemCount() {
            return this.items.size();
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public View createView(int i) {
            if (i == 1) {
                return SearchViewPager.this.searchContainer;
            }
            if (i == 2) {
                SearchViewPager searchViewPager = SearchViewPager.this;
                SearchDownloadsContainer searchDownloadsContainer = new SearchDownloadsContainer(searchViewPager.parent, searchViewPager.currentAccount);
                searchDownloadsContainer.recyclerListView.addOnScrollListener(new AnonymousClass1());
                searchDownloadsContainer.setUiCallback(SearchViewPager.this);
                return searchDownloadsContainer;
            }
            FilteredSearchView filteredSearchView = new FilteredSearchView(SearchViewPager.this.parent);
            filteredSearchView.setChatPreviewDelegate(SearchViewPager.this.chatPreviewDelegate);
            filteredSearchView.setUiCallback(SearchViewPager.this);
            filteredSearchView.recyclerListView.addOnScrollListener(new AnonymousClass2());
            return filteredSearchView;
        }

        /* renamed from: org.telegram.ui.Components.SearchViewPager$ViewPagerAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends RecyclerView.OnScrollListener {
            AnonymousClass1() {
                ViewPagerAdapter.this = r1;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                SearchViewPager.this.fragmentView.invalidateBlur();
            }
        }

        /* renamed from: org.telegram.ui.Components.SearchViewPager$ViewPagerAdapter$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 extends RecyclerView.OnScrollListener {
            AnonymousClass2() {
                ViewPagerAdapter.this = r1;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                SearchViewPager.this.fragmentView.invalidateBlur();
            }
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public int getItemViewType(int i) {
            if (this.items.get(i).type == 0) {
                return 1;
            }
            if (this.items.get(i).type != 1) {
                return this.items.get(i).type + i;
            }
            return 2;
        }

        @Override // org.telegram.ui.Components.ViewPagerFixed.Adapter
        public void bindView(View view, int i, int i2) {
            SearchViewPager searchViewPager = SearchViewPager.this;
            searchViewPager.search(view, i, searchViewPager.lastSearchString, true);
        }

        /* loaded from: classes3.dex */
        public class Item {
            int filterIndex;
            private final int type;

            /* synthetic */ Item(ViewPagerAdapter viewPagerAdapter, int i, AnonymousClass1 anonymousClass1) {
                this(viewPagerAdapter, i);
            }

            private Item(ViewPagerAdapter viewPagerAdapter, int i) {
                this.type = i;
            }
        }
    }
}
