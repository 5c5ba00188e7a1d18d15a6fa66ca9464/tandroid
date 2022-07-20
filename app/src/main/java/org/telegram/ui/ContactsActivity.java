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
import android.text.TextUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
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
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerEmptyView;
/* loaded from: classes3.dex */
public class ContactsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private AnimatorSet bounceIconAnimator;
    private long channelId;
    private long chatId;
    private boolean createSecretChat;
    private boolean creatingChat;
    private ContactsActivityDelegate delegate;
    private boolean destroyAfterSelect;
    private boolean disableSections;
    private StickerEmptyView emptyView;
    private RLottieImageView floatingButton;
    private FrameLayout floatingButtonContainer;
    private boolean floatingHidden;
    private boolean hasGps;
    private LongSparseArray<TLRPC$User> ignoreUsers;
    private String initialSearchString;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ContactsAdapter listViewAdapter;
    private boolean needPhonebook;
    private boolean onlyUsers;
    private AlertDialog permissionDialog;
    private long permissionRequestTime;
    private int prevPosition;
    private int prevTop;
    private boolean returnAsResult;
    private boolean scrollUpdated;
    private SearchAdapter searchListViewAdapter;
    private boolean searchWas;
    private boolean searching;
    private boolean sortByName;
    private ActionBarMenuItem sortItem;
    private AccelerateDecelerateInterpolator floatingInterpolator = new AccelerateDecelerateInterpolator();
    private boolean allowSelf = true;
    private boolean allowBots = true;
    private boolean needForwardCount = true;
    private boolean needFinishFragment = true;
    private boolean resetDelegate = true;
    private String selectAlertString = null;
    private boolean allowUsernameSearch = true;
    private boolean askAboutContacts = true;
    private boolean checkPermission = true;
    private int animationIndex = -1;

    /* loaded from: classes3.dex */
    public interface ContactsActivityDelegate {
        void didSelectContact(TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity);
    }

    public ContactsActivity(Bundle bundle) {
        super(bundle);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
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
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
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

    /* JADX WARN: Can't wrap try/catch for region: R(17:2|(2:4|(1:6)(2:7|(1:9)(1:10)))(1:11)|12|(3:16|(1:18)(1:19)|20)|21|(1:23)(2:25|(2:27|(1:32)(1:31))(11:33|89|34|36|(2:38|(1:40)(1:41))(1:42)|43|(20:47|(1:49)(1:50)|51|(1:53)(1:54)|55|(1:57)|58|(1:60)(1:61)|62|(1:64)(1:65)|66|(1:68)|69|(1:71)(1:72)|73|(1:75)|76|(1:78)(1:79)|(1:81)(1:82)|83)|84|(1:86)|87|88))|24|89|34|36|(0)(0)|43|(21:45|47|(0)(0)|51|(0)(0)|55|(0)|58|(0)(0)|62|(0)(0)|66|(0)|69|(0)(0)|73|(0)|76|(0)(0)|(0)(0)|83)|84|(0)|87|88) */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0126, code lost:
        r23.hasGps = false;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0141  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x020e  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0217  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x022c  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0235  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0273  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x02c8  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x02e0  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0343  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0346  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x034a  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x034d  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0367  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        int i;
        AnonymousClass5 anonymousClass5;
        String str;
        int i2;
        boolean z;
        int i3;
        this.searching = false;
        this.searchWas = false;
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.destroyAfterSelect) {
            if (this.returnAsResult) {
                this.actionBar.setTitle(LocaleController.getString("SelectContact", 2131628163));
            } else if (this.createSecretChat) {
                this.actionBar.setTitle(LocaleController.getString("NewSecretChat", 2131626793));
            } else {
                this.actionBar.setTitle(LocaleController.getString("NewMessageTitle", 2131626783));
            }
        } else {
            this.actionBar.setTitle(LocaleController.getString("Contacts", 2131625242));
        }
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        ActionBarMenu createMenu = this.actionBar.createMenu();
        ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass2());
        actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString("Search", 2131628092));
        actionBarMenuItemSearchListener.setContentDescription(LocaleController.getString("Search", 2131628092));
        if (!this.createSecretChat && !this.returnAsResult) {
            ActionBarMenuItem addItem = createMenu.addItem(1, this.sortByName ? 2131165696 : 2131165694);
            this.sortItem = addItem;
            addItem.setContentDescription(LocaleController.getString("AccDescrContactSorting", 2131623979));
        }
        this.searchListViewAdapter = new AnonymousClass3(context, this.ignoreUsers, this.allowUsernameSearch, false, false, this.allowBots, this.allowSelf, true, 0);
        int i4 = 3;
        if (this.chatId != 0) {
            i3 = ChatObject.canUserDoAdminAction(getMessagesController().getChat(Long.valueOf(this.chatId)), 3);
        } else if (this.channelId != 0) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.channelId));
            i3 = (!ChatObject.canUserDoAdminAction(chat, 3) || !TextUtils.isEmpty(chat.username)) ? 0 : 2;
        } else {
            i = 0;
            this.hasGps = ApplicationLoader.applicationContext.getPackageManager().hasSystemFeature("android.hardware.location.gps");
            AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, this.onlyUsers ? 1 : 0, this.needPhonebook, this.ignoreUsers, i, this.hasGps);
            this.listViewAdapter = anonymousClass4;
            anonymousClass4.setSortType(this.sortItem == null ? this.sortByName ? 1 : 2 : 0, false);
            this.listViewAdapter.setDisableSections(this.disableSections);
            AnonymousClass5 anonymousClass52 = new AnonymousClass5(context);
            this.fragmentView = anonymousClass52;
            anonymousClass5 = anonymousClass52;
            FlickerLoadingView flickerLoadingView = new FlickerLoadingView(context);
            flickerLoadingView.setViewType(6);
            flickerLoadingView.showDate(false);
            StickerEmptyView stickerEmptyView = new StickerEmptyView(context, flickerLoadingView, 1);
            this.emptyView = stickerEmptyView;
            stickerEmptyView.addView(flickerLoadingView, 0);
            this.emptyView.setAnimateLayoutChange(true);
            this.emptyView.showProgress(true, false);
            this.emptyView.title.setText(LocaleController.getString("NoResult", 2131626858));
            this.emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", 2131628098));
            anonymousClass5.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
            AnonymousClass6 anonymousClass6 = new AnonymousClass6(context);
            this.listView = anonymousClass6;
            anonymousClass6.setSectionsType(1);
            this.listView.setVerticalScrollBarEnabled(false);
            this.listView.setFastScrollEnabled(0);
            RecyclerListView recyclerListView = this.listView;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
            this.layoutManager = linearLayoutManager;
            recyclerListView.setLayoutManager(linearLayoutManager);
            this.listView.setAdapter(this.listViewAdapter);
            anonymousClass5.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
            this.listView.setEmptyView(this.emptyView);
            this.listView.setAnimateEmptyView(true, 0);
            this.listView.setOnItemClickListener(new ContactsActivity$$ExternalSyntheticLambda9(this, i));
            this.listView.setOnScrollListener(new AnonymousClass7());
            if (!this.createSecretChat && !this.returnAsResult) {
                FrameLayout frameLayout = new FrameLayout(context);
                this.floatingButtonContainer = frameLayout;
                i2 = Build.VERSION.SDK_INT;
                int i5 = (i2 < 21 ? 56 : 60) + 20;
                float f = (i2 < 21 ? 56 : 60) + 20;
                z = LocaleController.isRTL;
                if (!z) {
                    i4 = 5;
                }
                anonymousClass5.addView(frameLayout, LayoutHelper.createFrame(i5, f, i4 | 80, !z ? 4.0f : 0.0f, 0.0f, !z ? 0.0f : 4.0f, 0.0f));
                this.floatingButtonContainer.setOnClickListener(new ContactsActivity$$ExternalSyntheticLambda4(this));
                RLottieImageView rLottieImageView = new RLottieImageView(context);
                this.floatingButton = rLottieImageView;
                rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
                Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
                if (i2 < 21) {
                    Drawable mutate = context.getResources().getDrawable(2131165414).mutate();
                    mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                    CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
                    combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    createSimpleSelectorCircleDrawable = combinedDrawable;
                }
                this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
                this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), PorterDuff.Mode.MULTIPLY));
                this.floatingButton.setAnimation(!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true) ? 2131558603 : 2131558604, 52, 52);
                this.floatingButtonContainer.setContentDescription(LocaleController.getString("CreateNewContact", 2131625274));
                if (i2 >= 21) {
                    StateListAnimator stateListAnimator = new StateListAnimator();
                    RLottieImageView rLottieImageView2 = this.floatingButton;
                    Property property = View.TRANSLATION_Z;
                    stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(rLottieImageView2, property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                    stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButton, property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                    this.floatingButton.setStateListAnimator(stateListAnimator);
                    this.floatingButton.setOutlineProvider(new AnonymousClass8(this));
                }
                this.floatingButtonContainer.addView(this.floatingButton, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56 : 60, 51, 10.0f, 6.0f, 10.0f, 0.0f));
            }
            str = this.initialSearchString;
            if (str != null) {
                this.actionBar.openSearchField(str, false);
                this.initialSearchString = null;
            }
            return this.fragmentView;
        }
        i = i3;
        this.hasGps = ApplicationLoader.applicationContext.getPackageManager().hasSystemFeature("android.hardware.location.gps");
        AnonymousClass4 anonymousClass42 = new AnonymousClass4(context, this.onlyUsers ? 1 : 0, this.needPhonebook, this.ignoreUsers, i, this.hasGps);
        this.listViewAdapter = anonymousClass42;
        anonymousClass42.setSortType(this.sortItem == null ? this.sortByName ? 1 : 2 : 0, false);
        this.listViewAdapter.setDisableSections(this.disableSections);
        AnonymousClass5 anonymousClass522 = new AnonymousClass5(context);
        this.fragmentView = anonymousClass522;
        anonymousClass5 = anonymousClass522;
        FlickerLoadingView flickerLoadingView2 = new FlickerLoadingView(context);
        flickerLoadingView2.setViewType(6);
        flickerLoadingView2.showDate(false);
        StickerEmptyView stickerEmptyView2 = new StickerEmptyView(context, flickerLoadingView2, 1);
        this.emptyView = stickerEmptyView2;
        stickerEmptyView2.addView(flickerLoadingView2, 0);
        this.emptyView.setAnimateLayoutChange(true);
        this.emptyView.showProgress(true, false);
        this.emptyView.title.setText(LocaleController.getString("NoResult", 2131626858));
        this.emptyView.subtitle.setText(LocaleController.getString("SearchEmptyViewFilteredSubtitle2", 2131628098));
        anonymousClass5.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        AnonymousClass6 anonymousClass62 = new AnonymousClass6(context);
        this.listView = anonymousClass62;
        anonymousClass62.setSectionsType(1);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setFastScrollEnabled(0);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager2;
        recyclerListView2.setLayoutManager(linearLayoutManager2);
        this.listView.setAdapter(this.listViewAdapter);
        anonymousClass5.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setEmptyView(this.emptyView);
        this.listView.setAnimateEmptyView(true, 0);
        this.listView.setOnItemClickListener(new ContactsActivity$$ExternalSyntheticLambda9(this, i));
        this.listView.setOnScrollListener(new AnonymousClass7());
        if (!this.createSecretChat) {
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.floatingButtonContainer = frameLayout2;
            i2 = Build.VERSION.SDK_INT;
            int i52 = (i2 < 21 ? 56 : 60) + 20;
            float f2 = (i2 < 21 ? 56 : 60) + 20;
            z = LocaleController.isRTL;
            if (!z) {
            }
            anonymousClass5.addView(frameLayout2, LayoutHelper.createFrame(i52, f2, i4 | 80, !z ? 4.0f : 0.0f, 0.0f, !z ? 0.0f : 4.0f, 0.0f));
            this.floatingButtonContainer.setOnClickListener(new ContactsActivity$$ExternalSyntheticLambda4(this));
            RLottieImageView rLottieImageView3 = new RLottieImageView(context);
            this.floatingButton = rLottieImageView3;
            rLottieImageView3.setScaleType(ImageView.ScaleType.CENTER);
            Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
            if (i2 < 21) {
            }
            this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable2);
            this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), PorterDuff.Mode.MULTIPLY));
            this.floatingButton.setAnimation(!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true) ? 2131558603 : 2131558604, 52, 52);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("CreateNewContact", 2131625274));
            if (i2 >= 21) {
            }
            this.floatingButtonContainer.addView(this.floatingButton, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56 : 60, 51, 10.0f, 6.0f, 10.0f, 0.0f));
        }
        str = this.initialSearchString;
        if (str != null) {
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ContactsActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ContactsActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                ContactsActivity.this.finishFragment();
                return;
            }
            int i2 = 1;
            if (i != 1) {
                return;
            }
            SharedConfig.toggleSortContactsByName();
            ContactsActivity.this.sortByName = SharedConfig.sortContactsByName;
            ContactsAdapter contactsAdapter = ContactsActivity.this.listViewAdapter;
            if (!ContactsActivity.this.sortByName) {
                i2 = 2;
            }
            contactsAdapter.setSortType(i2, false);
            ContactsActivity.this.sortItem.setIcon(ContactsActivity.this.sortByName ? 2131165696 : 2131165694);
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass2() {
            ContactsActivity.this = r1;
        }

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
            } else if (ContactsActivity.this.listView == null) {
            } else {
                ContactsActivity.this.listView.setAdapter(ContactsActivity.this.listViewAdapter);
                ContactsActivity.this.listView.setSectionsType(1);
            }
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends SearchAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, LongSparseArray longSparseArray, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i) {
            super(context, longSparseArray, z, z2, z3, z4, z5, z6, i);
            ContactsActivity.this = r12;
        }

        @Override // org.telegram.ui.Adapters.SearchAdapter
        protected void onSearchProgressChanged() {
            if (!searchInProgress() && getItemCount() == 0) {
                ContactsActivity.this.emptyView.showProgress(false, true);
            }
            ContactsActivity.this.showItemsAnimated();
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends ContactsAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, int i, boolean z, LongSparseArray longSparseArray, int i2, boolean z2) {
            super(context, i, z, longSparseArray, i2, z2);
            ContactsActivity.this = r8;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (ContactsActivity.this.listView == null || ContactsActivity.this.listView.getAdapter() != this) {
                return;
            }
            int itemCount = super.getItemCount();
            boolean z = true;
            if (ContactsActivity.this.needPhonebook) {
                RecyclerListView recyclerListView = ContactsActivity.this.listView;
                if (itemCount == 2) {
                    z = false;
                }
                recyclerListView.setFastScrollVisible(z);
                return;
            }
            RecyclerListView recyclerListView2 = ContactsActivity.this.listView;
            if (itemCount == 0) {
                z = false;
            }
            recyclerListView2.setFastScrollVisible(z);
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context) {
            super(context);
            ContactsActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (ContactsActivity.this.listView.getAdapter() == ContactsActivity.this.listViewAdapter) {
                if (ContactsActivity.this.emptyView.getVisibility() != 0) {
                    return;
                }
                ContactsActivity.this.emptyView.setTranslationY(AndroidUtilities.dp(74.0f));
                return;
            }
            ContactsActivity.this.emptyView.setTranslationY(AndroidUtilities.dp(0.0f));
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            ContactsActivity.this = r1;
        }

        @Override // android.view.View
        public void setPadding(int i, int i2, int i3, int i4) {
            super.setPadding(i, i2, i3, i4);
            if (ContactsActivity.this.emptyView != null) {
                ContactsActivity.this.emptyView.setPadding(i, i2, i3, i4);
            }
        }
    }

    public /* synthetic */ void lambda$createView$1(int i, View view, int i2) {
        Activity parentActivity;
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        SearchAdapter searchAdapter = this.searchListViewAdapter;
        boolean z = false;
        boolean z2 = true;
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
                    if (longSparseArray != null && longSparseArray.indexOfKey(tLRPC$User.id) >= 0) {
                        return;
                    }
                    didSelectResult(tLRPC$User, true, null);
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
                    if (!getMessagesController().checkCanOpenChat(bundle, this)) {
                        return;
                    }
                    presentFragment(new ChatActivity(bundle), true);
                    return;
                }
            } else if (!(item instanceof String)) {
                return;
            } else {
                String str = (String) item;
                if (str.equals("section")) {
                    return;
                }
                NewContactActivity newContactActivity = new NewContactActivity();
                newContactActivity.setInitialPhoneNumber(str, true);
                presentFragment(newContactActivity);
                return;
            }
        }
        int sectionForPosition = this.listViewAdapter.getSectionForPosition(i2);
        int positionInSectionForPosition = this.listViewAdapter.getPositionInSectionForPosition(i2);
        if (positionInSectionForPosition < 0 || sectionForPosition < 0) {
            return;
        }
        if ((!this.onlyUsers || i != 0) && sectionForPosition == 0) {
            if (this.needPhonebook) {
                if (positionInSectionForPosition == 0) {
                    presentFragment(new InviteContactsActivity());
                    return;
                } else if (positionInSectionForPosition != 1 || !this.hasGps) {
                    return;
                } else {
                    int i3 = Build.VERSION.SDK_INT;
                    if (i3 >= 23 && (parentActivity = getParentActivity()) != null && parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                        presentFragment(new ActionIntroActivity(1));
                        return;
                    }
                    if (i3 >= 28) {
                        z2 = ((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isLocationEnabled();
                    } else if (i3 >= 19) {
                        try {
                            if (Settings.Secure.getInt(ApplicationLoader.applicationContext.getContentResolver(), "location_mode", 0) != 0) {
                                z = true;
                            }
                            z2 = z;
                        } catch (Throwable th) {
                            FileLog.e(th);
                        }
                    }
                    if (!z2) {
                        presentFragment(new ActionIntroActivity(4));
                        return;
                    } else {
                        presentFragment(new PeopleNearbyActivity());
                        return;
                    }
                }
            } else if (i != 0) {
                if (positionInSectionForPosition != 0) {
                    return;
                }
                long j = this.chatId;
                if (j == 0) {
                    j = this.channelId;
                }
                presentFragment(new GroupInviteActivity(j));
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
            } else if (positionInSectionForPosition != 2) {
                return;
            } else {
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
            }
        }
        Object item2 = this.listViewAdapter.getItem(sectionForPosition, positionInSectionForPosition);
        if (item2 instanceof TLRPC$User) {
            TLRPC$User tLRPC$User2 = (TLRPC$User) item2;
            if (this.returnAsResult) {
                LongSparseArray<TLRPC$User> longSparseArray2 = this.ignoreUsers;
                if (longSparseArray2 != null && longSparseArray2.indexOfKey(tLRPC$User2.id) >= 0) {
                    return;
                }
                didSelectResult(tLRPC$User2, true, null);
            } else if (this.createSecretChat) {
                this.creatingChat = true;
                SecretChatHelper.getInstance(this.currentAccount).startSecretChat(getParentActivity(), tLRPC$User2);
            } else {
                Bundle bundle4 = new Bundle();
                bundle4.putLong("user_id", tLRPC$User2.id);
                if (!getMessagesController().checkCanOpenChat(bundle4, this)) {
                    return;
                }
                presentFragment(new ChatActivity(bundle4), true);
            }
        } else if (!(item2 instanceof ContactsController.Contact)) {
        } else {
            ContactsController.Contact contact = (ContactsController.Contact) item2;
            String str2 = !contact.phones.isEmpty() ? contact.phones.get(0) : null;
            if (str2 == null || getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setMessage(LocaleController.getString("InviteUser", 2131626291));
            builder.setTitle(LocaleController.getString("AppName", 2131624375));
            builder.setPositiveButton(LocaleController.getString("OK", 2131627075), new ContactsActivity$$ExternalSyntheticLambda1(this, str2));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            showDialog(builder.create());
        }
    }

    public /* synthetic */ void lambda$createView$0(String str, DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", str, null));
            intent.putExtra("sms_body", ContactsController.getInstance(this.currentAccount).getInviteText(1));
            getParentActivity().startActivityForResult(intent, 500);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 extends RecyclerView.OnScrollListener {
        private boolean scrollingManually;

        AnonymousClass7() {
            ContactsActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                if (ContactsActivity.this.searching && ContactsActivity.this.searchWas) {
                    AndroidUtilities.hideKeyboard(ContactsActivity.this.getParentActivity().getCurrentFocus());
                }
                this.scrollingManually = true;
                return;
            }
            this.scrollingManually = false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x004f, code lost:
            if (java.lang.Math.abs(r0) > 1) goto L22;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            boolean z;
            super.onScrolled(recyclerView, i, i2);
            if (ContactsActivity.this.floatingButtonContainer == null || ContactsActivity.this.floatingButtonContainer.getVisibility() == 8) {
                return;
            }
            int findFirstVisibleItemPosition = ContactsActivity.this.layoutManager.findFirstVisibleItemPosition();
            boolean z2 = false;
            View childAt = recyclerView.getChildAt(0);
            int top = childAt != null ? childAt.getTop() : 0;
            if (ContactsActivity.this.prevPosition == findFirstVisibleItemPosition) {
                int i3 = ContactsActivity.this.prevTop - top;
                z = top < ContactsActivity.this.prevTop;
            } else {
                if (findFirstVisibleItemPosition > ContactsActivity.this.prevPosition) {
                    z2 = true;
                }
                z = z2;
            }
            z2 = true;
            if (z2 && ContactsActivity.this.scrollUpdated && (z || this.scrollingManually)) {
                ContactsActivity.this.hideFloatingButton(z);
            }
            ContactsActivity.this.prevPosition = findFirstVisibleItemPosition;
            ContactsActivity.this.prevTop = top;
            ContactsActivity.this.scrollUpdated = true;
        }
    }

    public /* synthetic */ void lambda$createView$2(View view) {
        presentFragment(new NewContactActivity());
    }

    /* renamed from: org.telegram.ui.ContactsActivity$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 extends ViewOutlineProvider {
        AnonymousClass8(ContactsActivity contactsActivity) {
        }

        @Override // android.view.ViewOutlineProvider
        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
    }

    private void didSelectResult(TLRPC$User tLRPC$User, boolean z, String str) {
        EditTextBoldCursor editTextBoldCursor;
        if (z && this.selectAlertString != null) {
            if (getParentActivity() == null) {
                return;
            }
            if (tLRPC$User.bot) {
                if (tLRPC$User.bot_nochats) {
                    try {
                        BulletinFactory.of(this).createErrorBulletin(LocaleController.getString("BotCantJoinGroups", 2131624709)).show();
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        return;
                    }
                } else if (this.channelId != 0) {
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(this.channelId));
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    if (ChatObject.canAddAdmins(chat)) {
                        builder.setTitle(LocaleController.getString("AppName", 2131624375));
                        builder.setMessage(LocaleController.getString("AddBotAsAdmin", 2131624252));
                        builder.setPositiveButton(LocaleController.getString("MakeAdmin", 2131626520), new ContactsActivity$$ExternalSyntheticLambda3(this, tLRPC$User, str));
                        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                    } else {
                        builder.setMessage(LocaleController.getString("CantAddBotAsAdmin", 2131624841));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
                    }
                    showDialog(builder.create());
                    return;
                }
            }
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
            builder2.setTitle(LocaleController.getString("AppName", 2131624375));
            String formatStringSimple = LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(tLRPC$User));
            if (tLRPC$User.bot || !this.needForwardCount) {
                editTextBoldCursor = null;
            } else {
                formatStringSimple = String.format("%s\n\n%s", formatStringSimple, LocaleController.getString("AddToTheGroupForwardCount", 2131624300));
                editTextBoldCursor = new EditTextBoldCursor(getParentActivity());
                editTextBoldCursor.setTextSize(1, 18.0f);
                editTextBoldCursor.setText("50");
                editTextBoldCursor.setTextColor(Theme.getColor("dialogTextBlack"));
                editTextBoldCursor.setGravity(17);
                editTextBoldCursor.setInputType(2);
                editTextBoldCursor.setImeOptions(6);
                editTextBoldCursor.setBackgroundDrawable(Theme.createEditTextDrawable(getParentActivity(), true));
                editTextBoldCursor.addTextChangedListener(new AnonymousClass9(this, editTextBoldCursor));
                builder2.setView(editTextBoldCursor);
            }
            builder2.setMessage(formatStringSimple);
            builder2.setPositiveButton(LocaleController.getString("OK", 2131627075), new ContactsActivity$$ExternalSyntheticLambda2(this, tLRPC$User, editTextBoldCursor));
            builder2.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
            showDialog(builder2.create());
            if (editTextBoldCursor == null) {
                return;
            }
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
        ContactsActivityDelegate contactsActivityDelegate = this.delegate;
        if (contactsActivityDelegate != null) {
            contactsActivityDelegate.didSelectContact(tLRPC$User, str, this);
            if (this.resetDelegate) {
                this.delegate = null;
            }
        }
        if (!this.needFinishFragment) {
            return;
        }
        finishFragment();
    }

    public /* synthetic */ void lambda$didSelectResult$3(TLRPC$User tLRPC$User, String str, DialogInterface dialogInterface, int i) {
        ContactsActivityDelegate contactsActivityDelegate = this.delegate;
        if (contactsActivityDelegate != null) {
            contactsActivityDelegate.didSelectContact(tLRPC$User, str, this);
            this.delegate = null;
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 implements TextWatcher {
        final /* synthetic */ EditText val$editTextFinal;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass9(ContactsActivity contactsActivity, EditText editText) {
            this.val$editTextFinal = editText;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            try {
                String obj = editable.toString();
                if (obj.length() != 0) {
                    int intValue = Utilities.parseInt((CharSequence) obj).intValue();
                    if (intValue < 0) {
                        this.val$editTextFinal.setText("0");
                        EditText editText = this.val$editTextFinal;
                        editText.setSelection(editText.length());
                    } else if (intValue > 300) {
                        this.val$editTextFinal.setText("300");
                        EditText editText2 = this.val$editTextFinal;
                        editText2.setSelection(editText2.length());
                    } else {
                        if (!obj.equals("" + intValue)) {
                            EditText editText3 = this.val$editTextFinal;
                            editText3.setText("" + intValue);
                            EditText editText4 = this.val$editTextFinal;
                            editText4.setSelection(editText4.length());
                        }
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

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
        if (parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            return;
        }
        if (parentActivity.shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
            AlertDialog create = AlertsCreator.createContactsPermissionDialog(parentActivity, new ContactsActivity$$ExternalSyntheticLambda7(this)).create();
            this.permissionDialog = create;
            showDialog(create);
            return;
        }
        askForPermissons(true);
    }

    public /* synthetic */ void lambda$onResume$5(int i) {
        this.askAboutContacts = i != 0;
        if (i == 0) {
            return;
        }
        askForPermissons(false);
    }

    public RecyclerListView getListView() {
        return this.listView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        FrameLayout frameLayout = this.floatingButtonContainer;
        if (frameLayout != null) {
            frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new AnonymousClass10());
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$10 */
    /* loaded from: classes3.dex */
    class AnonymousClass10 implements ViewTreeObserver.OnGlobalLayoutListener {
        AnonymousClass10() {
            ContactsActivity.this = r1;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            ContactsActivity.this.floatingButtonContainer.setTranslationY(ContactsActivity.this.floatingHidden ? AndroidUtilities.dp(100.0f) : 0);
            ContactsActivity.this.floatingButtonContainer.setClickable(!ContactsActivity.this.floatingHidden);
            if (ContactsActivity.this.floatingButtonContainer != null) {
                ContactsActivity.this.floatingButtonContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

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
            showDialog(AlertsCreator.createContactsPermissionDialog(parentActivity, new ContactsActivity$$ExternalSyntheticLambda6(this)).create());
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
                    if (SystemClock.elapsedRealtime() - this.permissionRequestTime >= 200) {
                        return;
                    }
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
        ContactsAdapter contactsAdapter;
        if (i == NotificationCenter.contactsDidLoad) {
            ContactsAdapter contactsAdapter2 = this.listViewAdapter;
            if (contactsAdapter2 == null) {
                return;
            }
            if (!this.sortByName) {
                contactsAdapter2.setSortType(2, true);
            }
            this.listViewAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.updateInterfaces) {
            int intValue = ((Integer) objArr[0]).intValue();
            if ((MessagesController.UPDATE_MASK_AVATAR & intValue) != 0 || (MessagesController.UPDATE_MASK_NAME & intValue) != 0 || (MessagesController.UPDATE_MASK_STATUS & intValue) != 0) {
                updateVisibleRows(intValue);
            }
            if ((intValue & MessagesController.UPDATE_MASK_STATUS) == 0 || this.sortByName || (contactsAdapter = this.listViewAdapter) == null) {
                return;
            }
            contactsAdapter.sortOnlineContacts();
        } else if (i == NotificationCenter.encryptedChatCreated) {
            if (!this.createSecretChat || !this.creatingChat) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("enc_id", ((TLRPC$EncryptedChat) objArr[0]).id);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            presentFragment(new ChatActivity(bundle), true);
        } else if (i != NotificationCenter.closeChats || this.creatingChat) {
        } else {
            removeSelfFromStack();
        }
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

    public void showItemsAnimated() {
        LinearLayoutManager linearLayoutManager = this.layoutManager;
        int findLastVisibleItemPosition = linearLayoutManager == null ? 0 : linearLayoutManager.findLastVisibleItemPosition();
        this.listView.invalidate();
        this.listView.getViewTreeObserver().addOnPreDrawListener(new AnonymousClass11(findLastVisibleItemPosition));
    }

    /* renamed from: org.telegram.ui.ContactsActivity$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ int val$from;

        AnonymousClass11(int i) {
            ContactsActivity.this = r1;
            this.val$from = i;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ContactsActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
            int childCount = ContactsActivity.this.listView.getChildCount();
            AnimatorSet animatorSet = new AnimatorSet();
            for (int i = 0; i < childCount; i++) {
                View childAt = ContactsActivity.this.listView.getChildAt(i);
                if (ContactsActivity.this.listView.getChildAdapterPosition(childAt) > this.val$from) {
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
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public AnimatorSet onCustomTransitionAnimation(boolean z, Runnable runnable) {
        ValueAnimator valueAnimator;
        BaseFragment baseFragment;
        float[] fArr = {0.0f, 1.0f};
        if (z) {
            // fill-array-data instruction
            fArr[0] = 1.0f;
            fArr[1] = 0.0f;
            valueAnimator = ValueAnimator.ofFloat(fArr);
        } else {
            valueAnimator = ValueAnimator.ofFloat(fArr);
        }
        ViewGroup viewGroup = (ViewGroup) this.fragmentView.getParent();
        if (this.parentLayout.fragmentsStack.size() > 1) {
            ArrayList<BaseFragment> arrayList = this.parentLayout.fragmentsStack;
            baseFragment = arrayList.get(arrayList.size() - 2);
        } else {
            baseFragment = null;
        }
        DialogsActivity dialogsActivity = baseFragment instanceof DialogsActivity ? (DialogsActivity) baseFragment : null;
        if (dialogsActivity == null) {
            return null;
        }
        RLottieImageView floatingButton = dialogsActivity.getFloatingButton();
        View view = floatingButton.getParent() != null ? (View) floatingButton.getParent() : null;
        if (this.floatingButtonContainer == null || view == null || floatingButton.getVisibility() != 0 || Math.abs(view.getTranslationY()) > AndroidUtilities.dp(4.0f) || Math.abs(this.floatingButtonContainer.getTranslationY()) > AndroidUtilities.dp(4.0f)) {
            return null;
        }
        view.setVisibility(8);
        if (z) {
            viewGroup.setAlpha(0.0f);
        }
        valueAnimator.addUpdateListener(new ContactsActivity$$ExternalSyntheticLambda0(valueAnimator, viewGroup));
        FrameLayout frameLayout = this.floatingButtonContainer;
        if (frameLayout != null) {
            ((ViewGroup) this.fragmentView).removeView(frameLayout);
            ((FrameLayout) viewGroup.getParent()).addView(this.floatingButtonContainer);
        }
        valueAnimator.setDuration(150L);
        valueAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnonymousClass12(view, z, floatingButton, runnable));
        animatorSet.playTogether(valueAnimator);
        AndroidUtilities.runOnUIThread(new ContactsActivity$$ExternalSyntheticLambda5(this, animatorSet, z, view), 50L);
        return animatorSet;
    }

    public static /* synthetic */ void lambda$onCustomTransitionAnimation$7(ValueAnimator valueAnimator, ViewGroup viewGroup, ValueAnimator valueAnimator2) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        viewGroup.setTranslationX(AndroidUtilities.dp(48.0f) * floatValue);
        viewGroup.setAlpha(1.0f - floatValue);
    }

    /* renamed from: org.telegram.ui.ContactsActivity$12 */
    /* loaded from: classes3.dex */
    class AnonymousClass12 extends AnimatorListenerAdapter {
        final /* synthetic */ Runnable val$callback;
        final /* synthetic */ boolean val$isOpen;
        final /* synthetic */ RLottieImageView val$previousFab;
        final /* synthetic */ View val$previousFabContainer;

        AnonymousClass12(View view, boolean z, RLottieImageView rLottieImageView, Runnable runnable) {
            ContactsActivity.this = r1;
            this.val$previousFabContainer = view;
            this.val$isOpen = z;
            this.val$previousFab = rLottieImageView;
            this.val$callback = runnable;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ContactsActivity.this.floatingButtonContainer != null) {
                if (ContactsActivity.this.floatingButtonContainer.getParent() instanceof ViewGroup) {
                    ((ViewGroup) ContactsActivity.this.floatingButtonContainer.getParent()).removeView(ContactsActivity.this.floatingButtonContainer);
                }
                ((ViewGroup) ((BaseFragment) ContactsActivity.this).fragmentView).addView(ContactsActivity.this.floatingButtonContainer);
                this.val$previousFabContainer.setVisibility(0);
                if (!this.val$isOpen) {
                    this.val$previousFab.setAnimation(2131558604, 52, 52);
                    this.val$previousFab.getAnimatedDrawable().setCurrentFrame(ContactsActivity.this.floatingButton.getAnimatedDrawable().getCurrentFrame());
                    this.val$previousFab.playAnimation();
                }
            }
            this.val$callback.run();
        }
    }

    public /* synthetic */ void lambda$onCustomTransitionAnimation$8(AnimatorSet animatorSet, boolean z, View view) {
        this.animationIndex = getNotificationCenter().setAnimationInProgress(this.animationIndex, new int[]{NotificationCenter.diceStickersDidLoad}, false);
        animatorSet.start();
        if (z) {
            this.floatingButton.setAnimation(2131558603, 52, 52);
            this.floatingButton.playAnimation();
        } else {
            this.floatingButton.setAnimation(2131558604, 52, 52);
            this.floatingButton.playAnimation();
        }
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
        this.bounceIconAnimator.addListener(new AnonymousClass13(view));
        this.bounceIconAnimator.start();
    }

    /* renamed from: org.telegram.ui.ContactsActivity$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 extends AnimatorListenerAdapter {
        final /* synthetic */ View val$previousFabContainer;

        AnonymousClass13(View view) {
            ContactsActivity.this = r1;
            this.val$previousFabContainer = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ContactsActivity.this.floatingButton.setScaleX(1.0f);
            ContactsActivity.this.floatingButton.setScaleY(1.0f);
            this.val$previousFabContainer.setScaleX(1.0f);
            this.val$previousFabContainer.setScaleY(1.0f);
            ContactsActivity.this.bounceIconAnimator = null;
            ContactsActivity.this.getNotificationCenter().onAnimationFinish(ContactsActivity.this.animationIndex);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ContactsActivity$$ExternalSyntheticLambda8 contactsActivity$$ExternalSyntheticLambda8 = new ContactsActivity$$ExternalSyntheticLambda8(this);
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SECTIONS, new Class[]{LetterSectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollActive"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollInactive"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, contactsActivity$$ExternalSyntheticLambda8, "windowBackgroundWhiteGrayText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, contactsActivity$$ExternalSyntheticLambda8, "windowBackgroundWhiteBlueText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, contactsActivity$$ExternalSyntheticLambda8, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, contactsActivity$$ExternalSyntheticLambda8, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, contactsActivity$$ExternalSyntheticLambda8, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, contactsActivity$$ExternalSyntheticLambda8, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, contactsActivity$$ExternalSyntheticLambda8, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, contactsActivity$$ExternalSyntheticLambda8, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, contactsActivity$$ExternalSyntheticLambda8, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayIcon"));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionIcon"));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "chats_actionBackground"));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "chats_actionPressedBackground"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "key_graySectionText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, "graySection"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedCheckDrawable}, null, "chats_verifiedCheck"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedDrawable}, null, "chats_verifiedBackground"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_offlinePaint, null, null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_onlinePaint, null, null, "windowBackgroundWhiteBlueText3"));
        TextPaint[] textPaintArr = Theme.dialogs_namePaint;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr[0], textPaintArr[1], Theme.dialogs_searchNamePaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_name"));
        TextPaint[] textPaintArr2 = Theme.dialogs_nameEncryptedPaint;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr2[0], textPaintArr2[1], Theme.dialogs_searchNameEncryptedPaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_secretName"));
        return arrayList;
    }

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
