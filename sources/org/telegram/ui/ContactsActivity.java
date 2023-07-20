package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.ContactsAdapter;
import org.telegram.ui.Adapters.SearchAdapter;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerEmptyView;
import org.telegram.ui.ContactsActivity;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
/* loaded from: classes3.dex */
public class ContactsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private boolean allowBots;
    private boolean allowSelf;
    private boolean allowUsernameSearch;
    private int animationIndex;
    private boolean askAboutContacts;
    private AnimatorSet bounceIconAnimator;
    private long channelId;
    private long chatId;
    private boolean checkPermission;
    private boolean createSecretChat;
    private boolean creatingChat;
    private ContactsActivityDelegate delegate;
    private boolean destroyAfterSelect;
    private boolean disableSections;
    private StickerEmptyView emptyView;
    private RLottieImageView floatingButton;
    private FrameLayout floatingButtonContainer;
    private boolean floatingHidden;
    private AccelerateDecelerateInterpolator floatingInterpolator;
    private boolean hasGps;
    private LongSparseArray<TLRPC$User> ignoreUsers;
    private String initialSearchString;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ContactsAdapter listViewAdapter;
    private boolean needFinishFragment;
    private boolean needForwardCount;
    private boolean needPhonebook;
    private boolean onlyUsers;
    private AlertDialog permissionDialog;
    private long permissionRequestTime;
    private int prevPosition;
    private int prevTop;
    private boolean resetDelegate;
    private boolean returnAsResult;
    boolean scheduled;
    private boolean scrollUpdated;
    private SearchAdapter searchListViewAdapter;
    private boolean searchWas;
    private boolean searching;
    private String selectAlertString;
    private boolean sortByName;
    Runnable sortContactsRunnable;
    private ActionBarMenuItem sortItem;

    /* loaded from: classes3.dex */
    public interface ContactsActivityDelegate {
        void didSelectContact(TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity);
    }

    public ContactsActivity(Bundle bundle) {
        super(bundle);
        this.floatingInterpolator = new AccelerateDecelerateInterpolator();
        this.allowSelf = true;
        this.allowBots = true;
        this.needForwardCount = true;
        this.needFinishFragment = true;
        this.resetDelegate = true;
        this.selectAlertString = null;
        this.allowUsernameSearch = true;
        this.askAboutContacts = true;
        this.checkPermission = true;
        this.animationIndex = -1;
        this.sortContactsRunnable = new Runnable() { // from class: org.telegram.ui.ContactsActivity.13
            @Override // java.lang.Runnable
            public void run() {
                ContactsActivity.this.listViewAdapter.sortOnlineContacts();
                ContactsActivity.this.scheduled = false;
            }
        };
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.encryptedChatCreated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        this.checkPermission = UserConfig.getInstance(this.currentAccount).syncContacts;
        Bundle bundle = this.arguments;
        if (bundle != null) {
            this.onlyUsers = bundle.getBoolean("onlyUsers", false);
            this.destroyAfterSelect = this.arguments.getBoolean("destroyAfterSelect", false);
            this.returnAsResult = this.arguments.getBoolean("returnAsResult", false);
            this.createSecretChat = this.arguments.getBoolean("createSecretChat", false);
            this.selectAlertString = this.arguments.getString("selectAlertString");
            this.allowUsernameSearch = this.arguments.getBoolean("allowUsernameSearch", true);
            this.needForwardCount = this.arguments.getBoolean("needForwardCount", true);
            this.allowBots = this.arguments.getBoolean("allowBots", true);
            this.allowSelf = this.arguments.getBoolean("allowSelf", true);
            this.channelId = this.arguments.getLong("channelId", 0L);
            this.needFinishFragment = this.arguments.getBoolean("needFinishFragment", true);
            this.chatId = this.arguments.getLong("chat_id", 0L);
            this.disableSections = this.arguments.getBoolean("disableSections", false);
            this.resetDelegate = this.arguments.getBoolean("resetDelegate", false);
        } else {
            this.needPhonebook = true;
        }
        if (!this.createSecretChat && !this.returnAsResult) {
            this.sortByName = SharedConfig.sortContactsByName;
        }
        getContactsController().checkInviteText();
        getContactsController().reloadContactsStatusesMaybe();
        MessagesController.getInstance(this.currentAccount).getStoriesController().loadHiddenStories();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.encryptedChatCreated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        this.delegate = null;
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
        getNotificationCenter().onAnimationFinish(this.animationIndex);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationProgress(boolean z, float f) {
        super.onTransitionAnimationProgress(z, f);
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(17:1|(2:3|(1:5)(2:95|(1:97)(1:98)))(1:99)|6|(3:10|(1:12)(1:14)|13)|15|(1:17)(2:86|(2:88|(1:93)(1:92))(11:94|19|20|21|(2:23|(1:25)(1:82))(1:83)|26|(20:30|(1:32)(1:75)|33|(1:35)(1:74)|36|(1:38)(1:73)|39|(1:41)(1:72)|42|(1:44)(1:71)|45|(1:47)|48|(3:50|(1:52)(1:65)|53)(3:66|(1:68)(1:70)|69)|54|(1:56)|57|(1:59)(1:64)|(1:61)(1:63)|62)|76|(1:78)|79|80))|18|19|20|21|(0)(0)|26|(21:28|30|(0)(0)|33|(0)(0)|36|(0)(0)|39|(0)(0)|42|(0)(0)|45|(0)|48|(0)(0)|54|(0)|57|(0)(0)|(0)(0)|62)|76|(0)|79|80) */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x011a, code lost:
        r25.hasGps = false;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:39:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x022d  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0233  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x023f  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0249  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x024c  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0252  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0255  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0290  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x02e6  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x02f3  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0373  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0376  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x037a  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x037d  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0397  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        final int i;
        int i2;
        FrameLayout frameLayout;
        String str;
        int i3;
        this.searching = false;
        this.searchWas = false;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.destroyAfterSelect) {
            if (this.returnAsResult) {
                this.actionBar.setTitle(LocaleController.getString("SelectContact", R.string.SelectContact));
            } else if (this.createSecretChat) {
                this.actionBar.setTitle(LocaleController.getString("NewSecretChat", R.string.NewSecretChat));
            } else {
                this.actionBar.setTitle(LocaleController.getString("NewMessageTitle", R.string.NewMessageTitle));
            }
        } else {
            this.actionBar.setTitle(LocaleController.getString("Contacts", R.string.Contacts));
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.ContactsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i4) {
                if (i4 == -1) {
                    ContactsActivity.this.finishFragment();
                    return;
                }
                if (i4 == 1) {
                    SharedConfig.toggleSortContactsByName();
                    ContactsActivity.this.sortByName = SharedConfig.sortContactsByName;
                    ContactsActivity.this.listViewAdapter.setSortType(ContactsActivity.this.sortByName ? 1 : 2, false);
                    ContactsActivity.this.sortItem.setIcon(ContactsActivity.this.sortByName ? R.drawable.msg_contacts_time : R.drawable.msg_contacts_name);
                }
            }
        });
        ActionBarMenu createMenu = this.actionBar.createMenu();
        ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.ContactsActivity.2
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchExpand() {
                ContactsActivity.this.searching = true;
                if (ContactsActivity.this.floatingButtonContainer != null) {
                    ContactsActivity.this.floatingButtonContainer.setVisibility(8);
                }
                if (ContactsActivity.this.sortItem != null) {
                    ContactsActivity.this.sortItem.setVisibility(8);
                }
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchCollapse() {
                ContactsActivity.this.searchListViewAdapter.searchDialogs(null);
                ContactsActivity.this.searching = false;
                ContactsActivity.this.searchWas = false;
                ContactsActivity.this.listView.setAdapter(ContactsActivity.this.listViewAdapter);
                ContactsActivity.this.listView.setSectionsType(1);
                ContactsActivity.this.listViewAdapter.notifyDataSetChanged();
                ContactsActivity.this.listView.setFastScrollVisible(true);
                ContactsActivity.this.listView.setVerticalScrollBarEnabled(false);
                ContactsActivity.this.listView.getFastScroll().topOffset = AndroidUtilities.dp(90.0f);
                if (ContactsActivity.this.floatingButtonContainer != null) {
                    ContactsActivity.this.floatingButtonContainer.setVisibility(0);
                    ContactsActivity.this.floatingHidden = true;
                    ContactsActivity.this.floatingButtonContainer.setTranslationY(AndroidUtilities.dp(100.0f));
                    ContactsActivity.this.hideFloatingButton(false);
                }
                if (ContactsActivity.this.sortItem != null) {
                    ContactsActivity.this.sortItem.setVisibility(0);
                }
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onTextChanged(EditText editText) {
                if (ContactsActivity.this.searchListViewAdapter == null) {
                    return;
                }
                String obj = editText.getText().toString();
                if (obj.length() != 0) {
                    ContactsActivity.this.searchWas = true;
                    if (ContactsActivity.this.listView != null) {
                        ContactsActivity.this.listView.setAdapter(ContactsActivity.this.searchListViewAdapter);
                        ContactsActivity.this.listView.setSectionsType(0);
                        ContactsActivity.this.searchListViewAdapter.notifyDataSetChanged();
                        ContactsActivity.this.listView.setFastScrollVisible(false);
                        ContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
                    }
                    ContactsActivity.this.emptyView.showProgress(true, true);
                    ContactsActivity.this.searchListViewAdapter.searchDialogs(obj);
                } else if (ContactsActivity.this.listView != null) {
                    ContactsActivity.this.listView.setAdapter(ContactsActivity.this.listViewAdapter);
                    ContactsActivity.this.listView.setSectionsType(1);
                }
            }
        });
        int i4 = R.string.Search;
        actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString("Search", i4));
        actionBarMenuItemSearchListener.setContentDescription(LocaleController.getString("Search", i4));
        if (!this.createSecretChat && !this.returnAsResult) {
            ActionBarMenuItem addItem = createMenu.addItem(1, this.sortByName ? R.drawable.msg_contacts_time : R.drawable.msg_contacts_name);
            this.sortItem = addItem;
            addItem.setContentDescription(LocaleController.getString("AccDescrContactSorting", R.string.AccDescrContactSorting));
        }
        this.searchListViewAdapter = new SearchAdapter(context, this.ignoreUsers, this.allowUsernameSearch, false, false, this.allowBots, this.allowSelf, true, 0) { // from class: org.telegram.ui.ContactsActivity.3
            @Override // org.telegram.ui.Adapters.SearchAdapter
            protected void onSearchProgressChanged() {
                if (!searchInProgress() && getItemCount() == 0) {
                    ContactsActivity.this.emptyView.showProgress(false, true);
                }
                ContactsActivity.this.showItemsAnimated();
            }
        };
        if (this.chatId != 0) {
            i2 = ChatObject.canUserDoAdminAction(getMessagesController().getChat(Long.valueOf(this.chatId)), 3);
        } else if (this.channelId != 0) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.channelId));
            i2 = (!ChatObject.canUserDoAdminAction(chat, 3) || ChatObject.isPublic(chat)) ? 0 : 2;
        } else {
            i = 0;
            this.hasGps = ApplicationLoader.applicationContext.getPackageManager().hasSystemFeature("android.hardware.location.gps");
            ContactsAdapter contactsAdapter = new ContactsAdapter(context, this, this.onlyUsers ? 1 : 0, this.needPhonebook, this.ignoreUsers, i, this.hasGps) { // from class: org.telegram.ui.ContactsActivity.4
                @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
                public void notifyDataSetChanged() {
                    super.notifyDataSetChanged();
                    if (ContactsActivity.this.listView == null || ContactsActivity.this.listView.getAdapter() != this) {
                        return;
                    }
                    int itemCount = super.getItemCount();
                    if (ContactsActivity.this.needPhonebook) {
                        ContactsActivity.this.listView.setFastScrollVisible(itemCount != 2);
                    } else {
                        ContactsActivity.this.listView.setFastScrollVisible(itemCount != 0);
                    }
                }
            };
            this.listViewAdapter = contactsAdapter;
            contactsAdapter.setSortType(this.sortItem == null ? this.sortByName ? 1 : 2 : 0, false);
            this.listViewAdapter.setDisableSections(this.disableSections);
            FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.ContactsActivity.5
                Paint actionBarPaint = new Paint();

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    this.actionBarPaint.setColor(Theme.getColor(Theme.key_actionBarDefault));
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), ((BaseFragment) ContactsActivity.this).actionBar.getMeasuredHeight(), this.actionBarPaint);
                    ((BaseFragment) ContactsActivity.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) ContactsActivity.this).actionBar.getMeasuredHeight());
                    super.dispatchDraw(canvas);
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i5, int i6) {
                    measureChildWithMargins(((BaseFragment) ContactsActivity.this).actionBar, i5, 0, i6, 0);
                    ((ViewGroup.MarginLayoutParams) ContactsActivity.this.emptyView.getLayoutParams()).topMargin = ((BaseFragment) ContactsActivity.this).actionBar.getMeasuredHeight();
                    ((ViewGroup.MarginLayoutParams) ContactsActivity.this.listView.getLayoutParams()).topMargin = ((BaseFragment) ContactsActivity.this).actionBar.getMeasuredHeight();
                    super.onMeasure(i5, i6);
                }

                @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
                protected void onLayout(boolean z, int i5, int i6, int i7, int i8) {
                    super.onLayout(z, i5, i6, i7, i8);
                    if (ContactsActivity.this.listView.getAdapter() == ContactsActivity.this.listViewAdapter) {
                        if (ContactsActivity.this.emptyView.getVisibility() == 0) {
                            ContactsActivity.this.emptyView.setTranslationY(AndroidUtilities.dp(74.0f));
                            return;
                        }
                        return;
                    }
                    ContactsActivity.this.emptyView.setTranslationY(AndroidUtilities.dp(0.0f));
                }
            };
            this.fragmentView = frameLayout2;
            frameLayout = frameLayout2;
            FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
            flickerLoadingView.setViewType(6);
            flickerLoadingView.showDate(false);
            StickerEmptyView stickerEmptyView = new StickerEmptyView(context, flickerLoadingView, 1);
            this.emptyView = stickerEmptyView;
            stickerEmptyView.addView(flickerLoadingView, 0);
            this.emptyView.setAnimateLayoutChange(true);
            this.emptyView.showProgress(true, false);
            this.emptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
            this.emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", R.string.SearchEmptyViewFilteredSubtitle2));
            frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
            this.listView = new RecyclerListView(context) { // from class: org.telegram.ui.ContactsActivity.6
                @Override // android.view.View
                public void setPadding(int i5, int i6, int i7, int i8) {
                    super.setPadding(i5, i6, i7, i8);
                    if (ContactsActivity.this.emptyView != null) {
                        ContactsActivity.this.emptyView.setPadding(i5, i6, i7, i8);
                    }
                }
            };
            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            defaultItemAnimator.setDelayAnimations(false);
            defaultItemAnimator.setDurations(150L);
            defaultItemAnimator.setSupportsChangeAnimations(false);
            this.listView.setItemAnimator(defaultItemAnimator);
            this.listView.setSectionsType(1);
            this.listView.setVerticalScrollBarEnabled(false);
            this.listView.setFastScrollEnabled(0);
            RecyclerListView recyclerListView = this.listView;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
            this.layoutManager = linearLayoutManager;
            recyclerListView.setLayoutManager(linearLayoutManager);
            this.listView.setAdapter(this.listViewAdapter);
            frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
            this.listView.setEmptyView(this.emptyView);
            this.listView.setAnimateEmptyView(true, 0);
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda9
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ boolean hasDoubleTap(View view, int i5) {
                    return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i5);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ void onDoubleTap(View view, int i5, float f, float f2) {
                    RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i5, f, f2);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public final void onItemClick(View view, int i5, float f, float f2) {
                    ContactsActivity.this.lambda$createView$1(i, view, i5, f, f2);
                }
            });
            this.listView.setOnItemLongClickListener(new 7());
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ContactsActivity.8
                private boolean scrollingManually;

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i5) {
                    if (i5 == 1) {
                        if (ContactsActivity.this.searching && ContactsActivity.this.searchWas) {
                            AndroidUtilities.hideKeyboard(ContactsActivity.this.getParentActivity().getCurrentFocus());
                        }
                        this.scrollingManually = true;
                        return;
                    }
                    this.scrollingManually = false;
                }

                /* JADX WARN: Code restructure failed: missing block: B:17:0x004f, code lost:
                    if (java.lang.Math.abs(r0) > 1) goto L25;
                 */
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void onScrolled(RecyclerView recyclerView, int i5, int i6) {
                    boolean z;
                    super.onScrolled(recyclerView, i5, i6);
                    if (ContactsActivity.this.floatingButtonContainer == null || ContactsActivity.this.floatingButtonContainer.getVisibility() == 8) {
                        return;
                    }
                    int findFirstVisibleItemPosition = ContactsActivity.this.layoutManager.findFirstVisibleItemPosition();
                    View childAt = recyclerView.getChildAt(0);
                    int top = childAt != null ? childAt.getTop() : 0;
                    if (ContactsActivity.this.prevPosition == findFirstVisibleItemPosition) {
                        int i7 = ContactsActivity.this.prevTop - top;
                        z = top < ContactsActivity.this.prevTop;
                    } else {
                        z = findFirstVisibleItemPosition > ContactsActivity.this.prevPosition;
                    }
                    r6 = true;
                    if (r6 && ContactsActivity.this.scrollUpdated && (z || this.scrollingManually)) {
                        ContactsActivity.this.hideFloatingButton(z);
                    }
                    ContactsActivity.this.prevPosition = findFirstVisibleItemPosition;
                    ContactsActivity.this.prevTop = top;
                    ContactsActivity.this.scrollUpdated = true;
                }
            });
            if (!this.createSecretChat && !this.returnAsResult) {
                FrameLayout frameLayout3 = new FrameLayout(context);
                this.floatingButtonContainer = frameLayout3;
                i3 = Build.VERSION.SDK_INT;
                int i5 = (i3 < 21 ? 56 : 60) + 20;
                float f = (i3 < 21 ? 56 : 60) + 20;
                boolean z = LocaleController.isRTL;
                frameLayout.addView(frameLayout3, LayoutHelper.createFrame(i5, f, (!z ? 3 : 5) | 80, !z ? 4.0f : 0.0f, 0.0f, !z ? 0.0f : 4.0f, 0.0f));
                this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ContactsActivity.this.lambda$createView$2(view);
                    }
                });
                RLottieImageView rLottieImageView = new RLottieImageView(context);
                this.floatingButton = rLottieImageView;
                rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
                Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
                if (i3 < 21) {
                    Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
                    mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                    CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
                    combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    createSimpleSelectorCircleDrawable = combinedDrawable;
                }
                this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
                this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
                boolean z2 = MessagesController.getGlobalMainSettings().getBoolean("view_animations", true);
                if (!getMessagesController().storiesEnabled()) {
                    this.floatingButton.setAnimation(z2 ? R.raw.write_contacts_fab_icon_camera : R.raw.write_contacts_fab_icon_reverse_camera, 56, 56);
                } else {
                    this.floatingButton.setAnimation(z2 ? R.raw.write_contacts_fab_icon : R.raw.write_contacts_fab_icon_reverse, 52, 52);
                }
                this.floatingButtonContainer.setContentDescription(LocaleController.getString("CreateNewContact", R.string.CreateNewContact));
                if (i3 >= 21) {
                    StateListAnimator stateListAnimator = new StateListAnimator();
                    RLottieImageView rLottieImageView2 = this.floatingButton;
                    Property property = View.TRANSLATION_Z;
                    stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(rLottieImageView2, property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                    stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButton, property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                    this.floatingButton.setStateListAnimator(stateListAnimator);
                    this.floatingButton.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.ContactsActivity.10
                        @Override // android.view.ViewOutlineProvider
                        @SuppressLint({"NewApi"})
                        public void getOutline(View view, Outline outline) {
                            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        }
                    });
                }
                this.floatingButtonContainer.addView(this.floatingButton, LayoutHelper.createFrame(i3 < 21 ? 56 : 60, i3 < 21 ? 56 : 60, 51, 10.0f, 6.0f, 10.0f, 0.0f));
            }
            str = this.initialSearchString;
            if (str != null) {
                this.actionBar.openSearchField(str, false);
                this.initialSearchString = null;
            }
            ((FrameLayout) this.fragmentView).addView(this.actionBar);
            this.listViewAdapter.setStories(getMessagesController().storiesController.getHiddenList(), false);
            return this.fragmentView;
        }
        i = i2;
        this.hasGps = ApplicationLoader.applicationContext.getPackageManager().hasSystemFeature("android.hardware.location.gps");
        ContactsAdapter contactsAdapter2 = new ContactsAdapter(context, this, this.onlyUsers ? 1 : 0, this.needPhonebook, this.ignoreUsers, i, this.hasGps) { // from class: org.telegram.ui.ContactsActivity.4
            @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                if (ContactsActivity.this.listView == null || ContactsActivity.this.listView.getAdapter() != this) {
                    return;
                }
                int itemCount = super.getItemCount();
                if (ContactsActivity.this.needPhonebook) {
                    ContactsActivity.this.listView.setFastScrollVisible(itemCount != 2);
                } else {
                    ContactsActivity.this.listView.setFastScrollVisible(itemCount != 0);
                }
            }
        };
        this.listViewAdapter = contactsAdapter2;
        contactsAdapter2.setSortType(this.sortItem == null ? this.sortByName ? 1 : 2 : 0, false);
        this.listViewAdapter.setDisableSections(this.disableSections);
        FrameLayout frameLayout22 = new FrameLayout(context) { // from class: org.telegram.ui.ContactsActivity.5
            Paint actionBarPaint = new Paint();

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                this.actionBarPaint.setColor(Theme.getColor(Theme.key_actionBarDefault));
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), ((BaseFragment) ContactsActivity.this).actionBar.getMeasuredHeight(), this.actionBarPaint);
                ((BaseFragment) ContactsActivity.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) ContactsActivity.this).actionBar.getMeasuredHeight());
                super.dispatchDraw(canvas);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i52, int i6) {
                measureChildWithMargins(((BaseFragment) ContactsActivity.this).actionBar, i52, 0, i6, 0);
                ((ViewGroup.MarginLayoutParams) ContactsActivity.this.emptyView.getLayoutParams()).topMargin = ((BaseFragment) ContactsActivity.this).actionBar.getMeasuredHeight();
                ((ViewGroup.MarginLayoutParams) ContactsActivity.this.listView.getLayoutParams()).topMargin = ((BaseFragment) ContactsActivity.this).actionBar.getMeasuredHeight();
                super.onMeasure(i52, i6);
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z3, int i52, int i6, int i7, int i8) {
                super.onLayout(z3, i52, i6, i7, i8);
                if (ContactsActivity.this.listView.getAdapter() == ContactsActivity.this.listViewAdapter) {
                    if (ContactsActivity.this.emptyView.getVisibility() == 0) {
                        ContactsActivity.this.emptyView.setTranslationY(AndroidUtilities.dp(74.0f));
                        return;
                    }
                    return;
                }
                ContactsActivity.this.emptyView.setTranslationY(AndroidUtilities.dp(0.0f));
            }
        };
        this.fragmentView = frameLayout22;
        frameLayout = frameLayout22;
        FlickerLoadingView flickerLoadingView2 = new FlickerLoadingView(context);
        flickerLoadingView2.setViewType(6);
        flickerLoadingView2.showDate(false);
        StickerEmptyView stickerEmptyView2 = new StickerEmptyView(context, flickerLoadingView2, 1);
        this.emptyView = stickerEmptyView2;
        stickerEmptyView2.addView(flickerLoadingView2, 0);
        this.emptyView.setAnimateLayoutChange(true);
        this.emptyView.showProgress(true, false);
        this.emptyView.title.setText(LocaleController.getString("NoResult", R.string.NoResult));
        this.emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", R.string.SearchEmptyViewFilteredSubtitle2));
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView = new RecyclerListView(context) { // from class: org.telegram.ui.ContactsActivity.6
            @Override // android.view.View
            public void setPadding(int i52, int i6, int i7, int i8) {
                super.setPadding(i52, i6, i7, i8);
                if (ContactsActivity.this.emptyView != null) {
                    ContactsActivity.this.emptyView.setPadding(i52, i6, i7, i8);
                }
            }
        };
        DefaultItemAnimator defaultItemAnimator2 = new DefaultItemAnimator();
        defaultItemAnimator2.setDelayAnimations(false);
        defaultItemAnimator2.setDurations(150L);
        defaultItemAnimator2.setSupportsChangeAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator2);
        this.listView.setSectionsType(1);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setFastScrollEnabled(0);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager2;
        recyclerListView2.setLayoutManager(linearLayoutManager2);
        this.listView.setAdapter(this.listViewAdapter);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setEmptyView(this.emptyView);
        this.listView.setAnimateEmptyView(true, 0);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda9
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i52) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i52);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i52, float f2, float f22) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i52, f2, f22);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i52, float f2, float f22) {
                ContactsActivity.this.lambda$createView$1(i, view, i52, f2, f22);
            }
        });
        this.listView.setOnItemLongClickListener(new 7());
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ContactsActivity.8
            private boolean scrollingManually;

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i52) {
                if (i52 == 1) {
                    if (ContactsActivity.this.searching && ContactsActivity.this.searchWas) {
                        AndroidUtilities.hideKeyboard(ContactsActivity.this.getParentActivity().getCurrentFocus());
                    }
                    this.scrollingManually = true;
                    return;
                }
                this.scrollingManually = false;
            }

            /* JADX WARN: Code restructure failed: missing block: B:17:0x004f, code lost:
                if (java.lang.Math.abs(r0) > 1) goto L25;
             */
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onScrolled(RecyclerView recyclerView, int i52, int i6) {
                boolean z3;
                super.onScrolled(recyclerView, i52, i6);
                if (ContactsActivity.this.floatingButtonContainer == null || ContactsActivity.this.floatingButtonContainer.getVisibility() == 8) {
                    return;
                }
                int findFirstVisibleItemPosition = ContactsActivity.this.layoutManager.findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                int top = childAt != null ? childAt.getTop() : 0;
                if (ContactsActivity.this.prevPosition == findFirstVisibleItemPosition) {
                    int i7 = ContactsActivity.this.prevTop - top;
                    z3 = top < ContactsActivity.this.prevTop;
                } else {
                    z3 = findFirstVisibleItemPosition > ContactsActivity.this.prevPosition;
                }
                r6 = true;
                if (r6 && ContactsActivity.this.scrollUpdated && (z3 || this.scrollingManually)) {
                    ContactsActivity.this.hideFloatingButton(z3);
                }
                ContactsActivity.this.prevPosition = findFirstVisibleItemPosition;
                ContactsActivity.this.prevTop = top;
                ContactsActivity.this.scrollUpdated = true;
            }
        });
        if (!this.createSecretChat) {
            FrameLayout frameLayout32 = new FrameLayout(context);
            this.floatingButtonContainer = frameLayout32;
            i3 = Build.VERSION.SDK_INT;
            int i52 = (i3 < 21 ? 56 : 60) + 20;
            float f2 = (i3 < 21 ? 56 : 60) + 20;
            boolean z3 = LocaleController.isRTL;
            frameLayout.addView(frameLayout32, LayoutHelper.createFrame(i52, f2, (!z3 ? 3 : 5) | 80, !z3 ? 4.0f : 0.0f, 0.0f, !z3 ? 0.0f : 4.0f, 0.0f));
            this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ContactsActivity.this.lambda$createView$2(view);
                }
            });
            RLottieImageView rLottieImageView3 = new RLottieImageView(context);
            this.floatingButton = rLottieImageView3;
            rLottieImageView3.setScaleType(ImageView.ScaleType.CENTER);
            Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
            if (i3 < 21) {
            }
            this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable2);
            this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
            boolean z22 = MessagesController.getGlobalMainSettings().getBoolean("view_animations", true);
            if (!getMessagesController().storiesEnabled()) {
            }
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("CreateNewContact", R.string.CreateNewContact));
            if (i3 >= 21) {
            }
            this.floatingButtonContainer.addView(this.floatingButton, LayoutHelper.createFrame(i3 < 21 ? 56 : 60, i3 < 21 ? 56 : 60, 51, 10.0f, 6.0f, 10.0f, 0.0f));
        }
        str = this.initialSearchString;
        if (str != null) {
        }
        ((FrameLayout) this.fragmentView).addView(this.actionBar);
        this.listViewAdapter.setStories(getMessagesController().storiesController.getHiddenList(), false);
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(int i, View view, int i2, float f, float f2) {
        Activity parentActivity;
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        SearchAdapter searchAdapter = this.searchListViewAdapter;
        boolean z = true;
        if (adapter == searchAdapter) {
            Object item = searchAdapter.getItem(i2);
            if (item instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) item;
                if (this.searchListViewAdapter.isGlobalSearch(i2)) {
                    ArrayList<TLRPC$User> arrayList = new ArrayList<>();
                    arrayList.add(tLRPC$User);
                    getMessagesController().putUsers(arrayList, false);
                    MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(arrayList, null, false, true);
                }
                if (this.returnAsResult) {
                    LongSparseArray<TLRPC$User> longSparseArray = this.ignoreUsers;
                    if (longSparseArray == null || longSparseArray.indexOfKey(tLRPC$User.id) < 0) {
                        didSelectResult(tLRPC$User, true, null);
                        return;
                    }
                    return;
                } else if (this.createSecretChat) {
                    if (tLRPC$User.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        return;
                    }
                    this.creatingChat = true;
                    SecretChatHelper.getInstance(this.currentAccount).startSecretChat(getParentActivity(), tLRPC$User);
                    return;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong("user_id", tLRPC$User.id);
                    if (getMessagesController().checkCanOpenChat(bundle, this)) {
                        presentFragment(new ChatActivity(bundle), true);
                        return;
                    }
                    return;
                }
            } else if (item instanceof String) {
                String str = (String) item;
                if (str.equals("section")) {
                    return;
                }
                NewContactBottomSheet newContactBottomSheet = new NewContactBottomSheet(this, getContext());
                newContactBottomSheet.setInitialPhoneNumber(str, true);
                newContactBottomSheet.show();
                return;
            } else if (item instanceof ContactsController.Contact) {
                ContactsController.Contact contact = (ContactsController.Contact) item;
                AlertsCreator.createContactInviteDialog(this, contact.first_name, contact.last_name, contact.phones.get(0));
                return;
            } else {
                return;
            }
        }
        int sectionForPosition = this.listViewAdapter.getSectionForPosition(i2);
        int positionInSectionForPosition = this.listViewAdapter.getPositionInSectionForPosition(i2);
        if (positionInSectionForPosition < 0 || sectionForPosition < 0) {
            return;
        }
        ContactsAdapter contactsAdapter = this.listViewAdapter;
        boolean z2 = contactsAdapter.hasStories;
        if (z2 && sectionForPosition == 1) {
            if (view instanceof UserCell) {
                getOrCreateStoryViewer().open(getContext(), ((UserCell) view).getDialogId(), StoriesListPlaceProvider.of(this.listView));
                return;
            }
            return;
        }
        if (z2 && sectionForPosition > 1) {
            sectionForPosition--;
        }
        if ((!this.onlyUsers || i != 0) && sectionForPosition == 0) {
            if (this.needPhonebook) {
                if (positionInSectionForPosition == 0) {
                    presentFragment(new InviteContactsActivity());
                    return;
                } else if (positionInSectionForPosition == 1 && this.hasGps) {
                    int i3 = Build.VERSION.SDK_INT;
                    if (i3 >= 23 && (parentActivity = getParentActivity()) != null && parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                        presentFragment(new ActionIntroActivity(1));
                        return;
                    }
                    if (i3 >= 28) {
                        z = ((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isLocationEnabled();
                    } else if (i3 >= 19) {
                        try {
                            z = Settings.Secure.getInt(ApplicationLoader.applicationContext.getContentResolver(), "location_mode", 0) != 0;
                        } catch (Throwable th) {
                            FileLog.e(th);
                        }
                    }
                    if (!z) {
                        presentFragment(new ActionIntroActivity(4));
                        return;
                    } else {
                        presentFragment(new PeopleNearbyActivity());
                        return;
                    }
                } else {
                    return;
                }
            } else if (i != 0) {
                if (positionInSectionForPosition == 0) {
                    long j = this.chatId;
                    if (j == 0) {
                        j = this.channelId;
                    }
                    presentFragment(new GroupInviteActivity(j));
                    return;
                }
                return;
            } else if (positionInSectionForPosition == 0) {
                presentFragment(new GroupCreateActivity(new Bundle()), false);
                return;
            } else if (positionInSectionForPosition == 1) {
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("onlyUsers", true);
                bundle2.putBoolean("destroyAfterSelect", true);
                bundle2.putBoolean("createSecretChat", true);
                bundle2.putBoolean("allowBots", false);
                bundle2.putBoolean("allowSelf", false);
                presentFragment(new ContactsActivity(bundle2), false);
                return;
            } else if (positionInSectionForPosition == 2) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if (!BuildVars.DEBUG_VERSION && globalMainSettings.getBoolean("channel_intro", false)) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putInt("step", 0);
                    presentFragment(new ChannelCreateActivity(bundle3));
                    return;
                }
                presentFragment(new ActionIntroActivity(0));
                globalMainSettings.edit().putBoolean("channel_intro", true).commit();
                return;
            } else {
                return;
            }
        }
        Object item2 = this.listViewAdapter.getItem(contactsAdapter.getSectionForPosition(i2), this.listViewAdapter.getPositionInSectionForPosition(i2));
        if (item2 instanceof TLRPC$User) {
            TLRPC$User tLRPC$User2 = (TLRPC$User) item2;
            if (this.returnAsResult) {
                LongSparseArray<TLRPC$User> longSparseArray2 = this.ignoreUsers;
                if (longSparseArray2 == null || longSparseArray2.indexOfKey(tLRPC$User2.id) < 0) {
                    didSelectResult(tLRPC$User2, true, null);
                }
            } else if (this.createSecretChat) {
                this.creatingChat = true;
                SecretChatHelper.getInstance(this.currentAccount).startSecretChat(getParentActivity(), tLRPC$User2);
            } else {
                Bundle bundle4 = new Bundle();
                bundle4.putLong("user_id", tLRPC$User2.id);
                if (getMessagesController().checkCanOpenChat(bundle4, this)) {
                    presentFragment(new ChatActivity(bundle4), true);
                }
            }
        } else if (item2 instanceof ContactsController.Contact) {
            ContactsController.Contact contact2 = (ContactsController.Contact) item2;
            final String str2 = !contact2.phones.isEmpty() ? contact2.phones.get(0) : null;
            if (str2 == null || getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setMessage(LocaleController.getString("InviteUser", R.string.InviteUser));
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    ContactsActivity.this.lambda$createView$0(str2, dialogInterface, i4);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder.create());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(String str, DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", str, null));
            intent.putExtra("sms_body", ContactsController.getInstance(this.currentAccount).getInviteText(1));
            getParentActivity().startActivityForResult(intent, 500);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 7 implements RecyclerListView.OnItemLongClickListener {
        7() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
        public boolean onItemClick(View view, int i) {
            int sectionForPosition = ContactsActivity.this.listViewAdapter.getSectionForPosition(i);
            int positionInSectionForPosition = ContactsActivity.this.listViewAdapter.getPositionInSectionForPosition(i);
            if (Bulletin.getVisibleBulletin() != null) {
                Bulletin.getVisibleBulletin().hide();
            }
            if (positionInSectionForPosition < 0 || sectionForPosition < 0) {
                return false;
            }
            if (ContactsActivity.this.listViewAdapter.hasStories && sectionForPosition == 1 && (view instanceof UserCell)) {
                final long dialogId = ((UserCell) view).getDialogId();
                final TLRPC$User user = MessagesController.getInstance(((BaseFragment) ContactsActivity.this).currentAccount).getUser(Long.valueOf(dialogId));
                final String sharedPrefKey = NotificationsController.getSharedPrefKey(dialogId, 0);
                boolean z = !NotificationsCustomSettingsActivity.isStoriesNotMuted(((BaseFragment) ContactsActivity.this).currentAccount, dialogId);
                ItemOptions addIf = ItemOptions.makeOptions(ContactsActivity.this, view).setScrimViewBackground(Theme.createRoundRectDrawable(0, 0, Theme.getColor(Theme.key_windowBackgroundWhite))).add(R.drawable.msg_discussion, LocaleController.getString("SendMessage", R.string.SendMessage), new Runnable() { // from class: org.telegram.ui.ContactsActivity$7$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsActivity.7.this.lambda$onItemClick$0(dialogId);
                    }
                }).add(R.drawable.msg_openprofile, LocaleController.getString("OpenProfile", R.string.OpenProfile), new Runnable() { // from class: org.telegram.ui.ContactsActivity$7$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsActivity.7.this.lambda$onItemClick$1(dialogId);
                    }
                }).addIf(!z, R.drawable.msg_mute, LocaleController.getString("NotificationsStoryMute", R.string.NotificationsStoryMute), new Runnable() { // from class: org.telegram.ui.ContactsActivity$7$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsActivity.7.this.lambda$onItemClick$2(sharedPrefKey, dialogId, user);
                    }
                }).addIf(z, R.drawable.msg_unmute, LocaleController.getString("NotificationsStoryUnmute", R.string.NotificationsStoryUnmute), new Runnable() { // from class: org.telegram.ui.ContactsActivity$7$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsActivity.7.this.lambda$onItemClick$3(sharedPrefKey, dialogId, user);
                    }
                });
                addIf.add(R.drawable.msg_viewintopic, LocaleController.getString("ShowInChats", R.string.ShowInChats), new Runnable() { // from class: org.telegram.ui.ContactsActivity$7$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContactsActivity.7.this.lambda$onItemClick$6(dialogId, user);
                    }
                });
                addIf.setGravity(5).show();
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(long j) {
            ContactsActivity.this.presentFragment(ChatActivity.of(j));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$1(long j) {
            ContactsActivity.this.presentFragment(ProfileActivity.of(j));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$2(String str, long j, TLRPC$User tLRPC$User) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(((BaseFragment) ContactsActivity.this).currentAccount).edit();
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, false).apply();
            ContactsActivity.this.getNotificationsController().updateServerNotificationsSettings(j, 0);
            String trim = tLRPC$User == null ? "" : tLRPC$User.first_name.trim();
            int indexOf = trim.indexOf(" ");
            if (indexOf > 0) {
                trim = trim.substring(0, indexOf);
            }
            BulletinFactory.of(ContactsActivity.this).createUsersBulletin(Arrays.asList(tLRPC$User), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryMutedHint", R.string.NotificationsStoryMutedHint, trim))).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$3(String str, long j, TLRPC$User tLRPC$User) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(((BaseFragment) ContactsActivity.this).currentAccount).edit();
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, true).apply();
            ContactsActivity.this.getNotificationsController().updateServerNotificationsSettings(j, 0);
            String trim = tLRPC$User == null ? "" : tLRPC$User.first_name.trim();
            int indexOf = trim.indexOf(" ");
            if (indexOf > 0) {
                trim = trim.substring(0, indexOf);
            }
            BulletinFactory.of(ContactsActivity.this).createUsersBulletin(Arrays.asList(tLRPC$User), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryUnmutedHint", R.string.NotificationsStoryUnmutedHint, trim))).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$6(final long j, TLRPC$User tLRPC$User) {
            ContactsActivity.this.getMessagesController().getStoriesController().toggleHidden(j, false, false, true);
            BulletinFactory.UndoObject undoObject = new BulletinFactory.UndoObject();
            undoObject.onUndo = new Runnable() { // from class: org.telegram.ui.ContactsActivity$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsActivity.7.this.lambda$onItemClick$4(j);
                }
            };
            undoObject.onAction = new Runnable() { // from class: org.telegram.ui.ContactsActivity$7$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ContactsActivity.7.this.lambda$onItemClick$5(j);
                }
            };
            BulletinFactory.global().createUsersBulletin(Arrays.asList(tLRPC$User), AndroidUtilities.replaceTags(LocaleController.formatString("StoriesMovedToDialogs", R.string.StoriesMovedToDialogs, ContactsController.formatName(tLRPC$User.first_name, null, 20))), null, undoObject).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$4(long j) {
            ContactsActivity.this.getMessagesController().getStoriesController().toggleHidden(j, true, false, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$5(long j) {
            ContactsActivity.this.getMessagesController().getStoriesController().toggleHidden(j, false, true, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(View view) {
        AndroidUtilities.requestAdjustNothing(getParentActivity(), getClassGuid());
        new NewContactBottomSheet(this, getContext()) { // from class: org.telegram.ui.ContactsActivity.9
            @Override // org.telegram.ui.ActionBar.BottomSheet
            public void dismissInternal() {
                super.dismissInternal();
                AndroidUtilities.requestAdjustResize(ContactsActivity.this.getParentActivity(), this.classGuid);
            }
        }.show();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ActionBar createActionBar(Context context) {
        ActionBar createActionBar = super.createActionBar(context);
        createActionBar.setBackground(null);
        createActionBar.setAddToContainer(false);
        return createActionBar;
    }

    private void didSelectResult(final TLRPC$User tLRPC$User, boolean z, final String str) {
        final EditTextBoldCursor editTextBoldCursor;
        if (z && this.selectAlertString != null) {
            if (getParentActivity() == null) {
                return;
            }
            if (tLRPC$User.bot) {
                if (tLRPC$User.bot_nochats) {
                    try {
                        BulletinFactory.of(this).createErrorBulletin(LocaleController.getString("BotCantJoinGroups", R.string.BotCantJoinGroups)).show();
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        return;
                    }
                } else if (this.channelId != 0) {
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.channelId));
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    if (ChatObject.canAddAdmins(chat)) {
                        builder.setTitle(LocaleController.getString("AddBotAdminAlert", R.string.AddBotAdminAlert));
                        builder.setMessage(LocaleController.getString("AddBotAsAdmin", R.string.AddBotAsAdmin));
                        builder.setPositiveButton(LocaleController.getString("AddAsAdmin", R.string.AddAsAdmin), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda3
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                ContactsActivity.this.lambda$didSelectResult$3(tLRPC$User, str, dialogInterface, i);
                            }
                        });
                        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    } else {
                        builder.setMessage(LocaleController.getString("CantAddBotAsAdmin", R.string.CantAddBotAsAdmin));
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                    }
                    showDialog(builder.create());
                    return;
                }
            }
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
            builder2.setTitle(LocaleController.getString("AppName", R.string.AppName));
            String formatStringSimple = LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(tLRPC$User));
            if (tLRPC$User.bot || !this.needForwardCount) {
                editTextBoldCursor = null;
            } else {
                formatStringSimple = String.format("%s\n\n%s", formatStringSimple, LocaleController.getString("AddToTheGroupForwardCount", R.string.AddToTheGroupForwardCount));
                editTextBoldCursor = new EditTextBoldCursor(getParentActivity());
                editTextBoldCursor.setTextSize(1, 18.0f);
                editTextBoldCursor.setText("50");
                editTextBoldCursor.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                editTextBoldCursor.setGravity(17);
                editTextBoldCursor.setInputType(2);
                editTextBoldCursor.setImeOptions(6);
                editTextBoldCursor.setBackgroundDrawable(Theme.createEditTextDrawable(getParentActivity(), true));
                editTextBoldCursor.addTextChangedListener(new TextWatcher(this) { // from class: org.telegram.ui.ContactsActivity.11
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        try {
                            String obj = editable.toString();
                            if (obj.length() != 0) {
                                int intValue = Utilities.parseInt((CharSequence) obj).intValue();
                                if (intValue < 0) {
                                    editTextBoldCursor.setText("0");
                                    EditText editText = editTextBoldCursor;
                                    editText.setSelection(editText.length());
                                } else if (intValue > 300) {
                                    editTextBoldCursor.setText("300");
                                    EditText editText2 = editTextBoldCursor;
                                    editText2.setSelection(editText2.length());
                                } else {
                                    if (!obj.equals("" + intValue)) {
                                        EditText editText3 = editTextBoldCursor;
                                        editText3.setText("" + intValue);
                                        EditText editText4 = editTextBoldCursor;
                                        editText4.setSelection(editText4.length());
                                    }
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                    }
                });
                builder2.setView(editTextBoldCursor);
            }
            builder2.setMessage(formatStringSimple);
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ContactsActivity.this.lambda$didSelectResult$4(tLRPC$User, editTextBoldCursor, dialogInterface, i);
                }
            });
            builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder2.create());
            if (editTextBoldCursor != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) editTextBoldCursor.getLayoutParams();
                if (marginLayoutParams != null) {
                    if (marginLayoutParams instanceof FrameLayout.LayoutParams) {
                        ((FrameLayout.LayoutParams) marginLayoutParams).gravity = 1;
                    }
                    int dp = AndroidUtilities.dp(24.0f);
                    marginLayoutParams.leftMargin = dp;
                    marginLayoutParams.rightMargin = dp;
                    marginLayoutParams.height = AndroidUtilities.dp(36.0f);
                    editTextBoldCursor.setLayoutParams(marginLayoutParams);
                }
                editTextBoldCursor.setSelection(editTextBoldCursor.getText().length());
                return;
            }
            return;
        }
        ContactsActivityDelegate contactsActivityDelegate = this.delegate;
        if (contactsActivityDelegate != null) {
            contactsActivityDelegate.didSelectContact(tLRPC$User, str, this);
            if (this.resetDelegate) {
                this.delegate = null;
            }
        }
        if (this.needFinishFragment) {
            finishFragment();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$3(TLRPC$User tLRPC$User, String str, DialogInterface dialogInterface, int i) {
        ContactsActivityDelegate contactsActivityDelegate = this.delegate;
        if (contactsActivityDelegate != null) {
            contactsActivityDelegate.didSelectContact(tLRPC$User, str, this);
            this.delegate = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$4(TLRPC$User tLRPC$User, EditText editText, DialogInterface dialogInterface, int i) {
        didSelectResult(tLRPC$User, false, editText != null ? editText.getText().toString() : "0");
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        Activity parentActivity;
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        ContactsAdapter contactsAdapter = this.listViewAdapter;
        if (contactsAdapter != null) {
            contactsAdapter.notifyDataSetChanged();
        }
        if (!this.checkPermission || Build.VERSION.SDK_INT < 23 || (parentActivity = getParentActivity()) == null) {
            return;
        }
        this.checkPermission = false;
        if (parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
            if (parentActivity.shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
                AlertDialog create = AlertsCreator.createContactsPermissionDialog(parentActivity, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda7
                    @Override // org.telegram.messenger.MessagesStorage.IntCallback
                    public final void run(int i) {
                        ContactsActivity.this.lambda$onResume$5(i);
                    }
                }).create();
                this.permissionDialog = create;
                showDialog(create);
                return;
            }
            askForPermissons(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$5(int i) {
        this.askAboutContacts = i != 0;
        if (i == 0) {
            return;
        }
        askForPermissons(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RecyclerListView getListView() {
        return this.listView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        FrameLayout frameLayout = this.floatingButtonContainer;
        if (frameLayout != null) {
            frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.ContactsActivity.12
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    ContactsActivity.this.floatingButtonContainer.setTranslationY(ContactsActivity.this.floatingHidden ? AndroidUtilities.dp(100.0f) : 0);
                    ContactsActivity.this.floatingButtonContainer.setClickable(!ContactsActivity.this.floatingHidden);
                    if (ContactsActivity.this.floatingButtonContainer != null) {
                        ContactsActivity.this.floatingButtonContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        super.onDialogDismiss(dialog);
        AlertDialog alertDialog = this.permissionDialog;
        if (alertDialog == null || dialog != alertDialog || getParentActivity() == null || !this.askAboutContacts) {
            return;
        }
        askForPermissons(false);
    }

    @TargetApi(23)
    private void askForPermissons(boolean z) {
        Activity parentActivity = getParentActivity();
        if (parentActivity == null || !UserConfig.getInstance(this.currentAccount).syncContacts || parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            return;
        }
        if (z && this.askAboutContacts) {
            showDialog(AlertsCreator.createContactsPermissionDialog(parentActivity, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.MessagesStorage.IntCallback
                public final void run(int i) {
                    ContactsActivity.this.lambda$askForPermissons$6(i);
                }
            }).create());
            return;
        }
        this.permissionRequestTime = SystemClock.elapsedRealtime();
        ArrayList arrayList = new ArrayList();
        arrayList.add("android.permission.READ_CONTACTS");
        arrayList.add("android.permission.WRITE_CONTACTS");
        arrayList.add("android.permission.GET_ACCOUNTS");
        try {
            parentActivity.requestPermissions((String[]) arrayList.toArray(new String[0]), 1);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$askForPermissons$6(int i) {
        this.askAboutContacts = i != 0;
        if (i == 0) {
            return;
        }
        askForPermissons(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        if (i == 1) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (iArr.length > i2 && "android.permission.READ_CONTACTS".equals(strArr[i2])) {
                    if (iArr[i2] == 0) {
                        ContactsController.getInstance(this.currentAccount).forceImportContacts();
                        return;
                    }
                    SharedPreferences.Editor edit = MessagesController.getGlobalNotificationsSettings().edit();
                    this.askAboutContacts = false;
                    edit.putBoolean("askAboutContacts", false).commit();
                    if (SystemClock.elapsedRealtime() - this.permissionRequestTime < 200) {
                        try {
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", ApplicationLoader.applicationContext.getPackageName(), null));
                            getParentActivity().startActivity(intent);
                            return;
                        } catch (Exception e) {
                            FileLog.e(e);
                            return;
                        }
                    }
                    return;
                }
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.closeSearchField();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.storiesUpdated) {
            this.listViewAdapter.setStories(getMessagesController().getStoriesController().getHiddenList(), true);
            MessagesController.getInstance(this.currentAccount).getStoriesController().loadHiddenStories();
        } else if (i == NotificationCenter.contactsDidLoad) {
            ContactsAdapter contactsAdapter = this.listViewAdapter;
            if (contactsAdapter != null) {
                if (!this.sortByName) {
                    contactsAdapter.setSortType(2, true);
                }
                this.listViewAdapter.notifyDataSetChanged();
            }
        } else if (i == NotificationCenter.updateInterfaces) {
            int intValue = ((Integer) objArr[0]).intValue();
            if ((MessagesController.UPDATE_MASK_AVATAR & intValue) != 0 || (MessagesController.UPDATE_MASK_NAME & intValue) != 0 || (MessagesController.UPDATE_MASK_STATUS & intValue) != 0) {
                updateVisibleRows(intValue);
            }
            if ((intValue & MessagesController.UPDATE_MASK_STATUS) == 0 || this.sortByName || this.listViewAdapter == null) {
                return;
            }
            scheduleSort();
        } else if (i == NotificationCenter.encryptedChatCreated) {
            if (this.createSecretChat && this.creatingChat) {
                Bundle bundle = new Bundle();
                bundle.putInt("enc_id", ((TLRPC$EncryptedChat) objArr[0]).id);
                NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                presentFragment(new ChatActivity(bundle), true);
            }
        } else if (i != NotificationCenter.closeChats || this.creatingChat) {
        } else {
            removeSelfFromStack(true);
        }
    }

    private void scheduleSort() {
        if (this.scheduled) {
            return;
        }
        this.scheduled = true;
        AndroidUtilities.cancelRunOnUIThread(this.sortContactsRunnable);
        AndroidUtilities.runOnUIThread(this.sortContactsRunnable, 5000L);
    }

    private void updateVisibleRows(int i) {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = this.listView.getChildAt(i2);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(i);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideFloatingButton(boolean z) {
        if (this.floatingHidden == z) {
            return;
        }
        this.floatingHidden = z;
        AnimatorSet animatorSet = new AnimatorSet();
        Animator[] animatorArr = new Animator[1];
        FrameLayout frameLayout = this.floatingButtonContainer;
        Property property = View.TRANSLATION_Y;
        float[] fArr = new float[1];
        fArr[0] = this.floatingHidden ? AndroidUtilities.dp(100.0f) : 0;
        animatorArr[0] = ObjectAnimator.ofFloat(frameLayout, property, fArr);
        animatorSet.playTogether(animatorArr);
        animatorSet.setDuration(300L);
        animatorSet.setInterpolator(this.floatingInterpolator);
        this.floatingButtonContainer.setClickable(!z);
        animatorSet.start();
    }

    public void setDelegate(ContactsActivityDelegate contactsActivityDelegate) {
        this.delegate = contactsActivityDelegate;
    }

    public void setInitialSearchString(String str) {
        this.initialSearchString = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showItemsAnimated() {
        LinearLayoutManager linearLayoutManager = this.layoutManager;
        final int findLastVisibleItemPosition = linearLayoutManager == null ? 0 : linearLayoutManager.findLastVisibleItemPosition();
        this.listView.invalidate();
        this.listView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.ContactsActivity.14
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                ContactsActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
                int childCount = ContactsActivity.this.listView.getChildCount();
                AnimatorSet animatorSet = new AnimatorSet();
                for (int i = 0; i < childCount; i++) {
                    View childAt = ContactsActivity.this.listView.getChildAt(i);
                    if (ContactsActivity.this.listView.getChildAdapterPosition(childAt) > findLastVisibleItemPosition) {
                        childAt.setAlpha(0.0f);
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(childAt, View.ALPHA, 0.0f, 1.0f);
                        ofFloat.setStartDelay((int) ((Math.min(ContactsActivity.this.listView.getMeasuredHeight(), Math.max(0, childAt.getTop())) / ContactsActivity.this.listView.getMeasuredHeight()) * 100.0f));
                        ofFloat.setDuration(200L);
                        animatorSet.playTogether(ofFloat);
                    }
                }
                animatorSet.start();
                return true;
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public AnimatorSet onCustomTransitionAnimation(final boolean z, final Runnable runnable) {
        final ValueAnimator ofFloat;
        float[] fArr = {0.0f, 1.0f};
        if (z) {
            // fill-array-data instruction
            fArr[0] = 1.0f;
            fArr[1] = 0.0f;
            ofFloat = ValueAnimator.ofFloat(fArr);
        } else {
            ofFloat = ValueAnimator.ofFloat(fArr);
        }
        final ViewGroup viewGroup = (ViewGroup) this.fragmentView.getParent();
        BaseFragment baseFragment = this.parentLayout.getFragmentStack().size() > 1 ? this.parentLayout.getFragmentStack().get(this.parentLayout.getFragmentStack().size() - 2) : null;
        DialogsActivity dialogsActivity = baseFragment instanceof DialogsActivity ? (DialogsActivity) baseFragment : null;
        if (dialogsActivity == null) {
            return null;
        }
        final boolean z2 = dialogsActivity.storiesEnabled;
        final RLottieImageView floatingButton = dialogsActivity.getFloatingButton();
        View view = floatingButton.getParent() != null ? (View) floatingButton.getParent() : null;
        if (this.floatingButtonContainer == null || view == null || floatingButton.getVisibility() != 0 || Math.abs(view.getTranslationY()) > AndroidUtilities.dp(4.0f) || Math.abs(this.floatingButtonContainer.getTranslationY()) > AndroidUtilities.dp(4.0f)) {
            if (z2) {
                this.floatingButton.setAnimation(R.raw.write_contacts_fab_icon_camera, 56, 56);
            } else {
                this.floatingButton.setAnimation(R.raw.write_contacts_fab_icon, 52, 52);
            }
            this.floatingButton.getAnimatedDrawable().setCurrentFrame(this.floatingButton.getAnimatedDrawable().getFramesCount() - 1);
            return null;
        }
        view.setVisibility(8);
        if (z) {
            viewGroup.setAlpha(0.0f);
        }
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ContactsActivity.lambda$onCustomTransitionAnimation$7(ofFloat, viewGroup, valueAnimator);
            }
        });
        FrameLayout frameLayout = this.floatingButtonContainer;
        if (frameLayout != null) {
            ((ViewGroup) this.fragmentView).removeView(frameLayout);
            this.parentLayout.getOverlayContainerView().addView(this.floatingButtonContainer);
        }
        ofFloat.setDuration(150L);
        ofFloat.setInterpolator(new DecelerateInterpolator(1.5f));
        final AnimatorSet animatorSet = new AnimatorSet();
        final View view2 = view;
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ContactsActivity.15
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ContactsActivity.this.floatingButtonContainer != null) {
                    if (ContactsActivity.this.floatingButtonContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) ContactsActivity.this.floatingButtonContainer.getParent()).removeView(ContactsActivity.this.floatingButtonContainer);
                    }
                    ((ViewGroup) ((BaseFragment) ContactsActivity.this).fragmentView).addView(ContactsActivity.this.floatingButtonContainer);
                    view2.setVisibility(0);
                    if (!z) {
                        if (z2) {
                            floatingButton.setAnimation(R.raw.write_contacts_fab_icon_reverse_camera, 56, 56);
                        } else {
                            floatingButton.setAnimation(R.raw.write_contacts_fab_icon_reverse, 52, 52);
                        }
                        floatingButton.getAnimatedDrawable().setCurrentFrame(ContactsActivity.this.floatingButton.getAnimatedDrawable().getCurrentFrame());
                        floatingButton.playAnimation();
                    }
                }
                runnable.run();
            }
        });
        animatorSet.playTogether(ofFloat);
        final View view3 = view;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ContactsActivity.this.lambda$onCustomTransitionAnimation$8(animatorSet, z, z2, view3);
            }
        }, 50L);
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCustomTransitionAnimation$7(ValueAnimator valueAnimator, ViewGroup viewGroup, ValueAnimator valueAnimator2) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        viewGroup.setTranslationX(AndroidUtilities.dp(48.0f) * floatValue);
        viewGroup.setAlpha(1.0f - floatValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCustomTransitionAnimation$8(AnimatorSet animatorSet, boolean z, boolean z2, final View view) {
        this.animationIndex = getNotificationCenter().setAnimationInProgress(this.animationIndex, new int[]{NotificationCenter.diceStickersDidLoad}, false);
        animatorSet.start();
        if (z) {
            if (z2) {
                this.floatingButton.setAnimation(R.raw.write_contacts_fab_icon_camera, 56, 56);
            } else {
                this.floatingButton.setAnimation(R.raw.write_contacts_fab_icon, 52, 52);
            }
        } else if (z2) {
            this.floatingButton.setAnimation(R.raw.write_contacts_fab_icon_reverse_camera, 56, 56);
        } else {
            this.floatingButton.setAnimation(R.raw.write_contacts_fab_icon_reverse, 52, 52);
        }
        this.floatingButton.playAnimation();
        AnimatorSet animatorSet2 = this.bounceIconAnimator;
        if (animatorSet2 != null) {
            animatorSet2.cancel();
        }
        this.bounceIconAnimator = new AnimatorSet();
        float duration = (float) this.floatingButton.getAnimatedDrawable().getDuration();
        long j = 0;
        int i = 4;
        if (z) {
            for (int i2 = 0; i2 < 6; i2++) {
                AnimatorSet animatorSet3 = new AnimatorSet();
                if (i2 == 0) {
                    animatorSet3.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 1.0f, 0.9f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 1.0f, 0.9f), ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 0.9f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 0.9f));
                    animatorSet3.setDuration(0.12765957f * duration);
                    animatorSet3.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                } else if (i2 == 1) {
                    animatorSet3.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 0.9f, 1.06f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 0.9f, 1.06f), ObjectAnimator.ofFloat(view, View.SCALE_X, 0.9f, 1.06f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.9f, 1.06f));
                    animatorSet3.setDuration(0.3617021f * duration);
                    animatorSet3.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                } else if (i2 == 2) {
                    animatorSet3.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 1.06f, 0.9f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 1.06f, 0.9f), ObjectAnimator.ofFloat(view, View.SCALE_X, 1.06f, 0.9f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.06f, 0.9f));
                    animatorSet3.setDuration(0.21276596f * duration);
                    animatorSet3.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                } else if (i2 == 3) {
                    animatorSet3.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 0.9f, 1.03f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 0.9f, 1.03f), ObjectAnimator.ofFloat(view, View.SCALE_X, 0.9f, 1.03f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.9f, 1.03f));
                    animatorSet3.setDuration(duration * 0.10638298f);
                    animatorSet3.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                } else if (i2 == 4) {
                    animatorSet3.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 1.03f, 0.98f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 1.03f, 0.98f), ObjectAnimator.ofFloat(view, View.SCALE_X, 1.03f, 0.98f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.03f, 0.98f));
                    animatorSet3.setDuration(duration * 0.10638298f);
                    animatorSet3.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                } else {
                    animatorSet3.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 0.98f, 1.0f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 0.98f, 1.0f), ObjectAnimator.ofFloat(view, View.SCALE_X, 0.98f, 1.0f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.98f, 1.0f));
                    animatorSet3.setDuration(0.08510638f * duration);
                    animatorSet3.setInterpolator(CubicBezierInterpolator.EASE_IN);
                }
                animatorSet3.setStartDelay(j);
                j += animatorSet3.getDuration();
                this.bounceIconAnimator.playTogether(animatorSet3);
            }
        } else {
            for (int i3 = 0; i3 < 5; i3++) {
                AnimatorSet animatorSet4 = new AnimatorSet();
                if (i3 == 0) {
                    Animator[] animatorArr = new Animator[i];
                    animatorArr[0] = ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 1.0f, 0.9f);
                    animatorArr[1] = ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 1.0f, 0.9f);
                    animatorArr[2] = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 0.9f);
                    animatorArr[3] = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 0.9f);
                    animatorSet4.playTogether(animatorArr);
                    animatorSet4.setDuration(0.19444445f * duration);
                    animatorSet4.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                } else if (i3 == 1) {
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 0.9f, 1.06f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 0.9f, 1.06f), ObjectAnimator.ofFloat(view, View.SCALE_X, 0.9f, 1.06f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.9f, 1.06f));
                    animatorSet4.setDuration(0.22222222f * duration);
                    animatorSet4.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                } else if (i3 == 2) {
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 1.06f, 0.92f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 1.06f, 0.92f), ObjectAnimator.ofFloat(view, View.SCALE_X, 1.06f, 0.92f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.06f, 0.92f));
                    animatorSet4.setDuration(0.19444445f * duration);
                    animatorSet4.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                } else if (i3 == 3) {
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 0.92f, 1.02f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 0.92f, 1.02f), ObjectAnimator.ofFloat(view, View.SCALE_X, 0.92f, 1.02f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.92f, 1.02f));
                    animatorSet4.setDuration(0.25f * duration);
                    animatorSet4.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                } else {
                    i = 4;
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_X, 1.02f, 1.0f), ObjectAnimator.ofFloat(this.floatingButton, View.SCALE_Y, 1.02f, 1.0f), ObjectAnimator.ofFloat(view, View.SCALE_X, 1.02f, 1.0f), ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.02f, 1.0f));
                    animatorSet4.setDuration(duration * 0.10638298f);
                    animatorSet4.setInterpolator(CubicBezierInterpolator.EASE_IN);
                    animatorSet4.setStartDelay(j);
                    j += animatorSet4.getDuration();
                    this.bounceIconAnimator.playTogether(animatorSet4);
                }
                i = 4;
                animatorSet4.setStartDelay(j);
                j += animatorSet4.getDuration();
                this.bounceIconAnimator.playTogether(animatorSet4);
            }
        }
        this.bounceIconAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ContactsActivity.16
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ContactsActivity.this.floatingButton.setScaleX(1.0f);
                ContactsActivity.this.floatingButton.setScaleY(1.0f);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                ContactsActivity.this.bounceIconAnimator = null;
                ContactsActivity.this.getNotificationCenter().onAnimationFinish(ContactsActivity.this.animationIndex);
            }
        });
        this.bounceIconAnimator.start();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ContactsActivity$$ExternalSyntheticLambda8
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ContactsActivity.this.lambda$getThemeDescriptions$9();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SECTIONS, new Class[]{LetterSectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollActive));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollInactive));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollText));
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlueText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, Theme.avatarDrawables, null, Theme.key_avatar_text));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueText2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayIcon));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionIcon));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chats_actionBackground));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_chats_actionPressedBackground));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_graySectionText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, Theme.key_graySection));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedCheckDrawable}, null, Theme.key_chats_verifiedCheck));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedDrawable}, null, Theme.key_chats_verifiedBackground));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_offlinePaint, null, null, Theme.key_windowBackgroundWhiteGrayText3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_onlinePaint, null, null, Theme.key_windowBackgroundWhiteBlueText3));
        TextPaint[] textPaintArr = Theme.dialogs_namePaint;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr[0], textPaintArr[1], Theme.dialogs_searchNamePaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chats_name));
        TextPaint[] textPaintArr2 = Theme.dialogs_nameEncryptedPaint;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr2[0], textPaintArr2[1], Theme.dialogs_searchNameEncryptedPaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_chats_secretName));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$9() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(0);
                } else if (childAt instanceof ProfileSearchCell) {
                    ((ProfileSearchCell) childAt).update(0);
                }
            }
        }
    }
}
