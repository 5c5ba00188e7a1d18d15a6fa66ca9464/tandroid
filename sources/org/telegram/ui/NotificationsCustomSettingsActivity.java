package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import j$.util.Comparator$-CC;
import j$.util.function.ToDoubleFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC$TL_topPeer;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatNotificationsPopupWrapper;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.NotificationsCustomSettingsActivity;
import org.telegram.ui.NotificationsSettingsActivity;
import org.telegram.ui.ProfileNotificationsActivity;
/* loaded from: classes4.dex */
public class NotificationsCustomSettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private ListAdapter adapter;
    private AnimatorSet animatorSet;
    private ArrayList<NotificationsSettingsActivity.NotificationException> autoExceptions;
    private int currentType;
    private EmptyTextProgressView emptyView;
    private ArrayList<NotificationsSettingsActivity.NotificationException> exceptions;
    private HashMap<Long, NotificationsSettingsActivity.NotificationException> exceptionsDict;
    private int exceptionsEnd;
    private int exceptionsStart;
    boolean expanded;
    private final ArrayList<ItemInner> items;
    private RecyclerListView listView;
    private final ArrayList<ItemInner> oldItems;
    private final int[] popupOptions;
    private final int[] priorityOptions;
    private SearchAdapter searchAdapter;
    private boolean searchWas;
    private boolean searching;
    private int settingsEnd;
    private int settingsStart;
    private boolean showAutoExceptions;
    private boolean storiesAuto;
    private Boolean storiesEnabled;
    int topicId;
    private final int[] vibrateLabels;

    public void toggleShowAutoExceptions() {
        if (this.listView == null || this.adapter == null) {
            return;
        }
        this.showAutoExceptions = !this.showAutoExceptions;
        updateRows(true);
    }

    public NotificationsCustomSettingsActivity(int i, ArrayList<NotificationsSettingsActivity.NotificationException> arrayList, ArrayList<NotificationsSettingsActivity.NotificationException> arrayList2) {
        this(i, arrayList, arrayList2, false);
    }

    public NotificationsCustomSettingsActivity(int i, ArrayList<NotificationsSettingsActivity.NotificationException> arrayList, ArrayList<NotificationsSettingsActivity.NotificationException> arrayList2, boolean z) {
        this.showAutoExceptions = true;
        this.exceptionsDict = new HashMap<>();
        this.topicId = 0;
        this.vibrateLabels = new int[]{R.string.VibrationDefault, R.string.Short, R.string.VibrationDisabled, R.string.Long, R.string.OnlyIfSilent};
        this.popupOptions = new int[]{R.string.NoPopup, R.string.OnlyWhenScreenOn, R.string.OnlyWhenScreenOff, R.string.AlwaysShowPopup};
        int i2 = R.string.NotificationsPriorityUrgent;
        int i3 = R.string.NotificationsPriorityMedium;
        this.priorityOptions = new int[]{R.string.NotificationsPriorityHigh, i2, i2, i3, R.string.NotificationsPriorityLow, i3};
        this.oldItems = new ArrayList<>();
        this.items = new ArrayList<>();
        this.currentType = i;
        this.autoExceptions = arrayList2;
        this.exceptions = arrayList;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i4 = 0; i4 < size; i4++) {
                NotificationsSettingsActivity.NotificationException notificationException = this.exceptions.get(i4);
                this.exceptionsDict.put(Long.valueOf(notificationException.did), notificationException);
            }
        }
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList3 = this.autoExceptions;
        if (arrayList3 != null) {
            int size2 = arrayList3.size();
            for (int i5 = 0; i5 < size2; i5++) {
                NotificationsSettingsActivity.NotificationException notificationException2 = this.autoExceptions.get(i5);
                this.exceptionsDict.put(Long.valueOf(notificationException2.did), notificationException2);
            }
        }
        if (z) {
            loadExceptions();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        if (this.currentType == 3) {
            if (getNotificationsSettings().contains("EnableAllStories")) {
                this.storiesEnabled = Boolean.valueOf(getNotificationsSettings().getBoolean("EnableAllStories", true));
                this.storiesAuto = false;
                this.showAutoExceptions = false;
            } else {
                this.storiesEnabled = null;
                this.storiesAuto = true;
                this.showAutoExceptions = true;
            }
        }
        updateRows(true);
        return super.onFragmentCreate();
    }

    private static boolean isTop5Peer(int i, long j) {
        ArrayList arrayList = new ArrayList(MediaDataController.getInstance(i).hints);
        Collections.sort(arrayList, Comparator$-CC.comparingDouble(new ToDoubleFunction() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda15
            @Override // j$.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                double d;
                d = ((TLRPC$TL_topPeer) obj).rating;
                return d;
            }
        }));
        int i2 = -1;
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            if (DialogObject.getPeerDialogId(((TLRPC$TL_topPeer) arrayList.get(i3)).peer) == j) {
                i2 = i3;
            }
        }
        return i2 >= 0 && i2 >= arrayList.size() + (-5);
    }

    public static boolean areStoriesNotMuted(int i, long j) {
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(i);
        if (notificationsSettings.contains(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j)) {
            return notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j, true);
        } else if (notificationsSettings.contains("EnableAllStories")) {
            return notificationsSettings.getBoolean("EnableAllStories", true);
        } else {
            return isTop5Peer(i, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: deleteException */
    public void lambda$createView$6(NotificationsSettingsActivity.NotificationException notificationException, View view, int i) {
        String sharedPrefKey = NotificationsController.getSharedPrefKey(notificationException.did, 0L);
        SharedPreferences.Editor edit = getNotificationsSettings().edit();
        edit.remove(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey).commit();
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList = this.autoExceptions;
        if (arrayList != null) {
            arrayList.remove(notificationException);
        }
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList2 = this.exceptions;
        if (arrayList2 != null) {
            arrayList2.remove(notificationException);
        }
        if (isTop5Peer(this.currentAccount, notificationException.did)) {
            notificationException.auto = true;
            notificationException.notify = 0;
            this.autoExceptions.add(notificationException);
        }
        if (view instanceof UserCell) {
            UserCell userCell = (UserCell) view;
            userCell.setException(notificationException, null, userCell.needDivider);
        }
        getNotificationsController().updateServerNotificationsSettings(notificationException.did, 0L, false);
        updateRows(true);
    }

    private void updateMute(NotificationsSettingsActivity.NotificationException notificationException, View view, int i, boolean z, boolean z2) {
        String sharedPrefKey = NotificationsController.getSharedPrefKey(notificationException.did, 0L);
        SharedPreferences.Editor edit = getNotificationsSettings().edit();
        boolean isTop5Peer = isTop5Peer(this.currentAccount, notificationException.did);
        notificationException.notify = z2 ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0;
        if (notificationException.auto) {
            notificationException.auto = false;
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey, !z2).commit();
            ArrayList<NotificationsSettingsActivity.NotificationException> arrayList = this.autoExceptions;
            if (arrayList != null) {
                arrayList.remove(notificationException);
            }
            if (this.exceptions == null) {
                this.exceptions = new ArrayList<>();
            }
            this.exceptions.add(0, notificationException);
        } else if (isTop5Peer) {
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey, !z2).commit();
        } else {
            Boolean bool = this.storiesEnabled;
            if (!z2 ? !(bool == null || !bool.booleanValue()) : !(bool != null && bool.booleanValue())) {
                lambda$createView$6(notificationException, view, i);
                return;
            }
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey, !z2).commit();
        }
        if (view instanceof UserCell) {
            UserCell userCell = (UserCell) view;
            userCell.setException(notificationException, null, userCell.needDivider);
        }
        getNotificationsController().updateServerNotificationsSettings(notificationException.did, 0L, false);
        updateRows(true);
    }

    private int getLedColor() {
        int i = this.currentType;
        int i2 = -16776961;
        if (i == 0) {
            i2 = getNotificationsSettings().getInt("GroupLed", -16776961);
        } else if (i == 1) {
            i2 = getNotificationsSettings().getInt("MessagesLed", -16776961);
        } else if (i == 2) {
            i2 = getNotificationsSettings().getInt("ChannelLed", -16776961);
        } else if (i == 3) {
            i2 = getNotificationsSettings().getInt("StoriesLed", -16776961);
        } else if (i == 4 || i == 5) {
            i2 = getNotificationsSettings().getInt("ReactionsLed", -16776961);
        }
        for (int i3 = 0; i3 < 9; i3++) {
            if (TextColorCell.colorsToSave[i3] == i2) {
                return TextColorCell.colors[i3];
            }
        }
        return i2;
    }

    private String getPopupOption() {
        int i;
        int i2 = this.currentType;
        if (i2 == 0) {
            i = getNotificationsSettings().getInt("popupGroup", 0);
        } else if (i2 == 1) {
            i = getNotificationsSettings().getInt("popupAll", 0);
        } else {
            i = i2 != 2 ? 0 : getNotificationsSettings().getInt("popupChannel", 0);
        }
        int[] iArr = this.popupOptions;
        return LocaleController.getString(iArr[Utilities.clamp(i, iArr.length - 1, 0)]);
    }

    private String getSound() {
        String string;
        long j;
        SharedPreferences notificationsSettings = getNotificationsSettings();
        int i = R.string.SoundDefault;
        String string2 = LocaleController.getString("SoundDefault", i);
        int i2 = this.currentType;
        if (i2 == 0) {
            string = notificationsSettings.getString("GroupSound", string2);
            j = notificationsSettings.getLong("GroupSoundDocId", 0L);
        } else if (i2 == 1) {
            string = notificationsSettings.getString("GlobalSound", string2);
            j = notificationsSettings.getLong("GlobalSoundDocId", 0L);
        } else if (i2 == 3) {
            string = notificationsSettings.getString("StoriesSound", string2);
            j = notificationsSettings.getLong("StoriesSoundDocId", 0L);
        } else {
            string = notificationsSettings.getString("ChannelSound", string2);
            j = notificationsSettings.getLong("ChannelDocId", 0L);
        }
        if (j != 0) {
            TLRPC$Document document = getMediaDataController().ringtoneDataStore.getDocument(j);
            if (document == null) {
                return LocaleController.getString("CustomSound", R.string.CustomSound);
            }
            return NotificationsSoundActivity.trimTitle(document, FileLoader.getDocumentFileName(document));
        } else if (string.equals("NoSound")) {
            return LocaleController.getString("NoSound", R.string.NoSound);
        } else {
            return string.equals("Default") ? LocaleController.getString("SoundDefault", i) : string;
        }
    }

    private String getPriorityOption() {
        int i;
        int i2 = this.currentType;
        if (i2 == 0) {
            i = getNotificationsSettings().getInt("priority_group", 1);
        } else if (i2 == 1) {
            i = getNotificationsSettings().getInt("priority_messages", 1);
        } else if (i2 == 2) {
            i = getNotificationsSettings().getInt("priority_channel", 1);
        } else if (i2 == 3) {
            i = getNotificationsSettings().getInt("priority_stories", 1);
        } else {
            i = (i2 == 4 || i2 == 5) ? getNotificationsSettings().getInt("priority_react", 1) : 1;
        }
        int[] iArr = this.priorityOptions;
        return LocaleController.getString(iArr[Utilities.clamp(i, iArr.length - 1, 0)]);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        this.searching = false;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == -1) {
            this.actionBar.setTitle(LocaleController.getString("NotificationsExceptions", R.string.NotificationsExceptions));
        } else {
            this.actionBar.setTitle(LocaleController.getString("Notifications", R.string.Notifications));
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    NotificationsCustomSettingsActivity.this.finishFragment();
                }
            }
        });
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList = this.exceptions;
        if (arrayList != null && !arrayList.isEmpty()) {
            this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.2
                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onSearchExpand() {
                    NotificationsCustomSettingsActivity.this.searching = true;
                    NotificationsCustomSettingsActivity.this.emptyView.setShowAtCenter(true);
                }

                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onSearchCollapse() {
                    NotificationsCustomSettingsActivity.this.searchAdapter.searchDialogs(null);
                    NotificationsCustomSettingsActivity.this.searching = false;
                    NotificationsCustomSettingsActivity.this.searchWas = false;
                    NotificationsCustomSettingsActivity.this.emptyView.setText(LocaleController.getString("NoExceptions", R.string.NoExceptions));
                    NotificationsCustomSettingsActivity.this.listView.setAdapter(NotificationsCustomSettingsActivity.this.adapter);
                    NotificationsCustomSettingsActivity.this.adapter.notifyDataSetChanged();
                    NotificationsCustomSettingsActivity.this.listView.setFastScrollVisible(true);
                    NotificationsCustomSettingsActivity.this.listView.setVerticalScrollBarEnabled(false);
                    NotificationsCustomSettingsActivity.this.emptyView.setShowAtCenter(false);
                }

                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                public void onTextChanged(EditText editText) {
                    if (NotificationsCustomSettingsActivity.this.searchAdapter == null) {
                        return;
                    }
                    String obj = editText.getText().toString();
                    if (obj.length() != 0) {
                        NotificationsCustomSettingsActivity.this.searchWas = true;
                        if (NotificationsCustomSettingsActivity.this.listView != null) {
                            NotificationsCustomSettingsActivity.this.emptyView.setText(LocaleController.getString("NoResult", R.string.NoResult));
                            NotificationsCustomSettingsActivity.this.emptyView.showProgress();
                            NotificationsCustomSettingsActivity.this.listView.setAdapter(NotificationsCustomSettingsActivity.this.searchAdapter);
                            NotificationsCustomSettingsActivity.this.searchAdapter.notifyDataSetChanged();
                            NotificationsCustomSettingsActivity.this.listView.setFastScrollVisible(false);
                            NotificationsCustomSettingsActivity.this.listView.setVerticalScrollBarEnabled(true);
                        }
                    }
                    NotificationsCustomSettingsActivity.this.searchAdapter.searchDialogs(obj);
                }
            }).setSearchFieldHint(LocaleController.getString("Search", R.string.Search));
        }
        this.searchAdapter = new SearchAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView;
        emptyTextProgressView.setTextSize(18);
        this.emptyView.setText(LocaleController.getString("NoExceptions", R.string.NoExceptions));
        this.emptyView.showTextView();
        frameLayout2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                if (NotificationsCustomSettingsActivity.this.currentType != -1) {
                    if (NotificationsCustomSettingsActivity.this.exceptionsStart >= 0) {
                        drawSectionBackground(canvas, NotificationsCustomSettingsActivity.this.exceptionsStart, NotificationsCustomSettingsActivity.this.exceptionsEnd, getThemedColor(Theme.key_windowBackgroundWhite));
                    }
                    if (NotificationsCustomSettingsActivity.this.currentType != 4 && NotificationsCustomSettingsActivity.this.currentType != 5) {
                        drawSectionBackground(canvas, NotificationsCustomSettingsActivity.this.settingsStart, NotificationsCustomSettingsActivity.this.settingsEnd, getThemedColor(Theme.key_windowBackgroundWhite));
                    }
                }
                super.dispatchDraw(canvas);
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setEmptyView(this.emptyView);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda19
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i, float f, float f2) {
                NotificationsCustomSettingsActivity.this.lambda$createView$17(context, view, i, f, f2);
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                NotificationsCustomSettingsActivity.this.listView.invalidate();
            }
        };
        defaultItemAnimator.setAddDuration(150L);
        defaultItemAnimator.setMoveDuration(350L);
        defaultItemAnimator.setChangeDuration(0L);
        defaultItemAnimator.setRemoveDuration(0L);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setMoveInterpolator(new OvershootInterpolator(1.1f));
        defaultItemAnimator.setTranslationInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator);
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.6
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                if (i == 1) {
                    AndroidUtilities.hideKeyboard(NotificationsCustomSettingsActivity.this.getParentActivity().getCurrentFocus());
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
            }
        });
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0117 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0118  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$17(Context context, final View view, final int i, float f, float f2) {
        final NotificationsSettingsActivity.NotificationException notificationException;
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList;
        boolean z;
        long j;
        NotificationsSettingsActivity.NotificationException notificationException2;
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList2;
        boolean z2;
        long j2;
        final NotificationsSettingsActivity.NotificationException notificationException3;
        final boolean z3;
        final NotificationsSettingsActivity.NotificationException notificationException4;
        if (getParentActivity() == null) {
            return;
        }
        ItemInner itemInner = (this.listView.getAdapter() != this.adapter || i < 0 || i >= this.items.size()) ? null : this.items.get(i);
        if (itemInner != null && itemInner.viewType == 8) {
            this.expanded = !this.expanded;
            updateRows(true);
            return;
        }
        int i2 = this.currentType;
        if (i2 == 3 && itemInner != null && (notificationException4 = itemInner.exception) != null) {
            ItemOptions.makeOptions(this, view).setGravity(3).addIf(notificationException4.notify <= 0 || notificationException4.auto, R.drawable.msg_mute, LocaleController.getString(R.string.NotificationsStoryMute), false, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.this.lambda$createView$1(notificationException4, view, i);
                }
            }).addIf(notificationException4.notify > 0 || notificationException4.auto, R.drawable.msg_unmute, LocaleController.getString(R.string.NotificationsStoryUnmute), false, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.this.lambda$createView$2(notificationException4, view, i);
                }
            }).addIf(!notificationException4.auto, R.drawable.msg_delete, LocaleController.getString("DeleteException", R.string.DeleteException), true, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.this.lambda$createView$3(notificationException4, view, i);
                }
            }).show();
            return;
        }
        if (i2 == 3) {
            RecyclerView.Adapter adapter = this.listView.getAdapter();
            SearchAdapter searchAdapter = this.searchAdapter;
            if (adapter == searchAdapter) {
                Object object = searchAdapter.getObject(i);
                if (object instanceof NotificationsSettingsActivity.NotificationException) {
                    notificationException3 = (NotificationsSettingsActivity.NotificationException) object;
                } else {
                    boolean z4 = object instanceof TLRPC$User;
                    if (z4) {
                        j2 = ((TLRPC$User) object).id;
                    } else {
                        j2 = -((TLRPC$Chat) object).id;
                    }
                    if (this.exceptionsDict.containsKey(Long.valueOf(j2))) {
                        notificationException3 = this.exceptionsDict.get(Long.valueOf(j2));
                    } else {
                        NotificationsSettingsActivity.NotificationException notificationException5 = new NotificationsSettingsActivity.NotificationException();
                        notificationException5.story = true;
                        notificationException5.did = j2;
                        if (z4) {
                            notificationException5.did = ((TLRPC$User) object).id;
                        } else {
                            notificationException5.did = -((TLRPC$Chat) object).id;
                        }
                        notificationException3 = notificationException5;
                        z3 = true;
                        if (notificationException3 != null) {
                            return;
                        }
                        ItemOptions.makeOptions(this, view).setGravity(3).addIf(notificationException3.notify <= 0 || notificationException3.auto, R.drawable.msg_mute, LocaleController.getString(R.string.NotificationsStoryMute), false, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda14
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$createView$4(notificationException3, view, z3);
                            }
                        }).addIf(notificationException3.notify > 0 || notificationException3.auto, R.drawable.msg_unmute, LocaleController.getString(R.string.NotificationsStoryUnmute), false, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda13
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$createView$5(notificationException3, view, z3);
                            }
                        }).addIf((z3 || notificationException3.auto) ? false : true, R.drawable.msg_delete, LocaleController.getString("DeleteException", R.string.DeleteException), true, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda11
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$createView$6(notificationException3, view, i);
                            }
                        }).show();
                        return;
                    }
                }
                z3 = false;
                if (notificationException3 != null) {
                }
            }
        }
        if (this.listView.getAdapter() == this.searchAdapter || !(itemInner == null || itemInner.exception == null)) {
            RecyclerView.Adapter adapter2 = this.listView.getAdapter();
            SearchAdapter searchAdapter2 = this.searchAdapter;
            if (adapter2 == searchAdapter2) {
                Object object2 = searchAdapter2.getObject(i);
                if (object2 instanceof NotificationsSettingsActivity.NotificationException) {
                    notificationException2 = (NotificationsSettingsActivity.NotificationException) object2;
                    arrayList2 = this.searchAdapter.searchResult;
                    r2 = false;
                } else {
                    boolean z5 = object2 instanceof TLRPC$User;
                    if (z5) {
                        j = ((TLRPC$User) object2).id;
                    } else {
                        j = -((TLRPC$Chat) object2).id;
                    }
                    if (this.exceptionsDict.containsKey(Long.valueOf(j))) {
                        notificationException2 = this.exceptionsDict.get(Long.valueOf(j));
                        r2 = false;
                    } else {
                        NotificationsSettingsActivity.NotificationException notificationException6 = new NotificationsSettingsActivity.NotificationException();
                        notificationException6.did = j;
                        if (z5) {
                            notificationException6.did = ((TLRPC$User) object2).id;
                        } else {
                            notificationException6.did = -((TLRPC$Chat) object2).id;
                        }
                        notificationException2 = notificationException6;
                    }
                    arrayList2 = this.exceptions;
                }
                notificationException = notificationException2;
                z = r2;
                arrayList = arrayList2;
            } else {
                NotificationsSettingsActivity.NotificationException notificationException7 = itemInner.exception;
                if (notificationException7.auto) {
                    return;
                }
                notificationException = notificationException7;
                arrayList = this.exceptions;
                z = false;
            }
            if (notificationException == null) {
                return;
            }
            final long j3 = notificationException.did;
            final boolean isGlobalNotificationsEnabled = NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(j3, false, false);
            final boolean z6 = z;
            final ArrayList<NotificationsSettingsActivity.NotificationException> arrayList3 = arrayList;
            ChatNotificationsPopupWrapper chatNotificationsPopupWrapper = new ChatNotificationsPopupWrapper(context, this.currentAccount, null, true, true, new ChatNotificationsPopupWrapper.Callback() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.4
                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public /* synthetic */ void dismiss() {
                    ChatNotificationsPopupWrapper.Callback.-CC.$default$dismiss(this);
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public /* synthetic */ void openExceptions() {
                    ChatNotificationsPopupWrapper.Callback.-CC.$default$openExceptions(this);
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public void toggleSound() {
                    String sharedPrefKey = NotificationsController.getSharedPrefKey(j3, NotificationsCustomSettingsActivity.this.topicId);
                    SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsCustomSettingsActivity.this).currentAccount);
                    boolean z7 = !notificationsSettings.getBoolean("sound_enabled_" + sharedPrefKey, true);
                    notificationsSettings.edit().putBoolean("sound_enabled_" + sharedPrefKey, z7).apply();
                    if (BulletinFactory.canShowBulletin(NotificationsCustomSettingsActivity.this)) {
                        NotificationsCustomSettingsActivity notificationsCustomSettingsActivity = NotificationsCustomSettingsActivity.this;
                        BulletinFactory.createSoundEnabledBulletin(notificationsCustomSettingsActivity, !z7, notificationsCustomSettingsActivity.getResourceProvider()).show();
                    }
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public void muteFor(int i3) {
                    if (i3 != 0) {
                        NotificationsCustomSettingsActivity.this.getNotificationsController().muteUntil(j3, NotificationsCustomSettingsActivity.this.topicId, i3);
                        if (BulletinFactory.canShowBulletin(NotificationsCustomSettingsActivity.this)) {
                            NotificationsCustomSettingsActivity notificationsCustomSettingsActivity = NotificationsCustomSettingsActivity.this;
                            BulletinFactory.createMuteBulletin(notificationsCustomSettingsActivity, 5, i3, notificationsCustomSettingsActivity.getResourceProvider()).show();
                        }
                    } else {
                        if (NotificationsCustomSettingsActivity.this.getMessagesController().isDialogMuted(j3, NotificationsCustomSettingsActivity.this.topicId)) {
                            toggleMute();
                        }
                        if (BulletinFactory.canShowBulletin(NotificationsCustomSettingsActivity.this)) {
                            NotificationsCustomSettingsActivity notificationsCustomSettingsActivity2 = NotificationsCustomSettingsActivity.this;
                            BulletinFactory.createMuteBulletin(notificationsCustomSettingsActivity2, 4, i3, notificationsCustomSettingsActivity2.getResourceProvider()).show();
                        }
                    }
                    update();
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public void showCustomize() {
                    if (j3 != 0) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("dialog_id", j3);
                        ProfileNotificationsActivity profileNotificationsActivity = new ProfileNotificationsActivity(bundle);
                        profileNotificationsActivity.setDelegate(new ProfileNotificationsActivity.ProfileNotificationsActivityDelegate() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.4.1
                            @Override // org.telegram.ui.ProfileNotificationsActivity.ProfileNotificationsActivityDelegate
                            public void didCreateNewException(NotificationsSettingsActivity.NotificationException notificationException8) {
                            }

                            @Override // org.telegram.ui.ProfileNotificationsActivity.ProfileNotificationsActivityDelegate
                            public void didRemoveException(long j4) {
                                setDefault();
                            }
                        });
                        NotificationsCustomSettingsActivity.this.presentFragment(profileNotificationsActivity);
                    }
                }

                @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                public void toggleMute() {
                    NotificationsCustomSettingsActivity.this.getNotificationsController().muteDialog(j3, NotificationsCustomSettingsActivity.this.topicId, !NotificationsCustomSettingsActivity.this.getMessagesController().isDialogMuted(j3, NotificationsCustomSettingsActivity.this.topicId));
                    NotificationsCustomSettingsActivity notificationsCustomSettingsActivity = NotificationsCustomSettingsActivity.this;
                    BulletinFactory.createMuteBulletin(notificationsCustomSettingsActivity, notificationsCustomSettingsActivity.getMessagesController().isDialogMuted(j3, NotificationsCustomSettingsActivity.this.topicId), null).show();
                    update();
                }

                private void update() {
                    if (NotificationsCustomSettingsActivity.this.getMessagesController().isDialogMuted(j3, NotificationsCustomSettingsActivity.this.topicId) != isGlobalNotificationsEnabled) {
                        setDefault();
                    } else {
                        setNotDefault();
                    }
                }

                private void setNotDefault() {
                    SharedPreferences notificationsSettings = NotificationsCustomSettingsActivity.this.getNotificationsSettings();
                    NotificationsSettingsActivity.NotificationException notificationException8 = notificationException;
                    notificationException8.hasCustom = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + notificationException.did, false);
                    NotificationsSettingsActivity.NotificationException notificationException9 = notificationException;
                    notificationException9.notify = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + notificationException.did, 0);
                    if (notificationException.notify != 0) {
                        int i3 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + notificationException.did, -1);
                        if (i3 != -1) {
                            notificationException.muteUntil = i3;
                        }
                    }
                    if (z6) {
                        NotificationsCustomSettingsActivity.this.exceptions.add(notificationException);
                        NotificationsCustomSettingsActivity.this.exceptionsDict.put(Long.valueOf(notificationException.did), notificationException);
                        NotificationsCustomSettingsActivity.this.updateRows(true);
                    } else {
                        NotificationsCustomSettingsActivity.this.listView.getAdapter().notifyItemChanged(i);
                    }
                    ((BaseFragment) NotificationsCustomSettingsActivity.this).actionBar.closeSearchField();
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void setDefault() {
                    int indexOf;
                    if (z6) {
                        return;
                    }
                    if (arrayList3 != NotificationsCustomSettingsActivity.this.exceptions && (indexOf = NotificationsCustomSettingsActivity.this.exceptions.indexOf(notificationException)) >= 0) {
                        NotificationsCustomSettingsActivity.this.exceptions.remove(indexOf);
                        NotificationsCustomSettingsActivity.this.exceptionsDict.remove(Long.valueOf(notificationException.did));
                    }
                    arrayList3.remove(notificationException);
                    if (arrayList3 == NotificationsCustomSettingsActivity.this.exceptions) {
                        NotificationsCustomSettingsActivity.this.updateRows(true);
                        NotificationsCustomSettingsActivity.this.checkRowsEnabled();
                    } else {
                        NotificationsCustomSettingsActivity.this.updateRows(true);
                        NotificationsCustomSettingsActivity.this.searchAdapter.notifyItemChanged(i);
                    }
                    ((BaseFragment) NotificationsCustomSettingsActivity.this).actionBar.closeSearchField();
                }
            }, getResourceProvider());
            chatNotificationsPopupWrapper.lambda$update$11(j3, this.topicId, null);
            chatNotificationsPopupWrapper.showAsOptions(this, view, f, f2);
        } else if (itemInner == null) {
        } else {
            int i3 = itemInner.id;
            if (i3 == 6) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("onlySelect", true);
                bundle.putBoolean("checkCanWrite", false);
                int i4 = this.currentType;
                if (i4 == 0) {
                    bundle.putInt("dialogsType", 6);
                } else if (i4 == 2) {
                    bundle.putInt("dialogsType", 5);
                } else {
                    bundle.putInt("dialogsType", 4);
                }
                DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda20
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList4, CharSequence charSequence, boolean z7, TopicsFragment topicsFragment) {
                        boolean lambda$createView$8;
                        lambda$createView$8 = NotificationsCustomSettingsActivity.this.lambda$createView$8(dialogsActivity2, arrayList4, charSequence, z7, topicsFragment);
                        return lambda$createView$8;
                    }
                });
                presentFragment(dialogsActivity);
            } else if (i3 == 7) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("NotificationsDeleteAllExceptionTitle", R.string.NotificationsDeleteAllExceptionTitle));
                builder.setMessage(LocaleController.getString("NotificationsDeleteAllExceptionAlert", R.string.NotificationsDeleteAllExceptionAlert));
                builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        NotificationsCustomSettingsActivity.this.lambda$createView$9(dialogInterface, i5);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog create = builder.create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                }
            } else if (i3 == 100 || i3 == 101) {
                boolean isGlobalNotificationsEnabled2 = getNotificationsController().isGlobalNotificationsEnabled(this.currentType);
                TextCheckCell textCheckCell = (TextCheckCell) view;
                this.listView.findViewHolderForAdapterPosition(i);
                int i5 = this.currentType;
                if (i5 != 3) {
                    if (!isGlobalNotificationsEnabled2) {
                        getNotificationsController().setGlobalNotificationsEnabled(this.currentType, 0);
                        updateRows(true);
                        return;
                    }
                    AlertsCreator.showCustomNotificationsDialog(this, 0L, 0, i5, this.exceptions, this.autoExceptions, this.currentAccount, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda17
                        @Override // org.telegram.messenger.MessagesStorage.IntCallback
                        public final void run(int i6) {
                            NotificationsCustomSettingsActivity.this.lambda$createView$10(i6);
                        }
                    });
                    return;
                }
                SharedPreferences.Editor edit = getNotificationsSettings().edit();
                Boolean bool = this.storiesEnabled;
                boolean z7 = bool != null && bool.booleanValue();
                if (this.storiesAuto && z7) {
                    edit.remove("EnableAllStories");
                    this.storiesEnabled = null;
                } else {
                    edit.putBoolean("EnableAllStories", !z7);
                    this.storiesEnabled = Boolean.valueOf(!z7);
                }
                edit.apply();
                getNotificationsController().updateServerNotificationsSettings(this.currentType);
                updateRows(true);
                if (this.showAutoExceptions != (this.storiesEnabled == null)) {
                    toggleShowAutoExceptions();
                }
                checkRowsEnabled();
            } else if (i3 == 3) {
                if (view.isEnabled()) {
                    try {
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("type", this.currentType);
                        presentFragment(new NotificationsSoundActivity(bundle2, getResourceProvider()));
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            } else if (itemInner.viewType == 3) {
                if (view.isEnabled()) {
                    showDialog(AlertsCreator.createColorSelectDialog(getParentActivity(), 0L, 0, this.currentType, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsCustomSettingsActivity.this.lambda$createView$11(view, i);
                        }
                    }));
                }
            } else if (i3 == 2) {
                if (view.isEnabled()) {
                    showDialog(AlertsCreator.createPopupSelectDialog(getParentActivity(), this.currentType, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsCustomSettingsActivity.this.lambda$createView$12(view, i);
                        }
                    }));
                }
            } else if (i3 == 1) {
                if (view.isEnabled()) {
                    int i6 = this.currentType;
                    final String str = i6 == 1 ? "vibrate_messages" : i6 == 0 ? "vibrate_group" : i6 == 3 ? "vibrate_stories" : (i6 == 4 || i6 == 5) ? "vibrate_react" : "vibrate_channel";
                    showDialog(AlertsCreator.createVibrationSelectDialog(getParentActivity(), 0L, 0L, str, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsCustomSettingsActivity.this.lambda$createView$13(view, str, i);
                        }
                    }));
                }
            } else if (i3 == 4) {
                if (view.isEnabled()) {
                    showDialog(AlertsCreator.createPrioritySelectDialog(getParentActivity(), 0L, 0, this.currentType, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsCustomSettingsActivity.this.lambda$createView$14(view, i);
                        }
                    }));
                }
            } else if (i3 == 102) {
                if (view.isEnabled()) {
                    SharedPreferences notificationsSettings = getNotificationsSettings();
                    if (notificationsSettings.getBoolean("EnableAllStories", false)) {
                        return;
                    }
                    SharedPreferences.Editor edit2 = notificationsSettings.edit();
                    if (this.storiesEnabled != null) {
                        edit2.remove("EnableAllStories");
                        this.storiesEnabled = null;
                        this.storiesAuto = true;
                        itemInner.checked = true;
                    } else {
                        edit2.putBoolean("EnableAllStories", false);
                        this.storiesEnabled = Boolean.FALSE;
                        this.storiesAuto = false;
                        itemInner.checked = false;
                    }
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(this.storiesAuto);
                    }
                    edit2.commit();
                    if (this.storiesAuto != this.showAutoExceptions) {
                        toggleShowAutoExceptions();
                    }
                    getNotificationsController().updateServerNotificationsSettings(this.currentType);
                    checkRowsEnabled();
                }
            } else if (i3 == 0) {
                if (view.isEnabled()) {
                    SharedPreferences notificationsSettings2 = getNotificationsSettings();
                    SharedPreferences.Editor edit3 = notificationsSettings2.edit();
                    int i7 = this.currentType;
                    if (i7 == 1) {
                        z2 = notificationsSettings2.getBoolean("EnablePreviewAll", true);
                        edit3.putBoolean("EnablePreviewAll", !z2);
                    } else if (i7 == 0) {
                        z2 = notificationsSettings2.getBoolean("EnablePreviewGroup", true);
                        edit3.putBoolean("EnablePreviewGroup", !z2);
                    } else if (i7 == 3) {
                        z2 = !notificationsSettings2.getBoolean("EnableHideStoriesSenders", false);
                        edit3.putBoolean("EnableHideStoriesSenders", z2);
                    } else if (i7 == 4 || i7 == 5) {
                        z2 = notificationsSettings2.getBoolean("EnableReactionsPreview", true);
                        edit3.putBoolean("EnableReactionsPreview", !z2);
                    } else {
                        z2 = notificationsSettings2.getBoolean("EnablePreviewChannel", true);
                        edit3.putBoolean("EnablePreviewChannel", !z2);
                    }
                    edit3.commit();
                    getNotificationsController().updateServerNotificationsSettings(this.currentType);
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!z2);
                    }
                }
            } else if (i3 == 103 || i3 == 104) {
                boolean z8 = !LocaleController.isRTL ? f <= ((float) (view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) : f >= ((float) AndroidUtilities.dp(76.0f));
                final SharedPreferences notificationsSettings3 = getNotificationsSettings();
                if (z8) {
                    String str2 = itemInner.id == 103 ? "EnableReactionsMessages" : "EnableReactionsStories";
                    SharedPreferences.Editor edit4 = notificationsSettings3.edit();
                    edit4.putBoolean(str2, !notificationsSettings3.getBoolean(str2, true));
                    edit4.apply();
                    updateRows(true);
                    getNotificationsController().updateServerNotificationsSettings(this.currentType);
                    return;
                }
                final String str3 = itemInner.id == 103 ? "EnableReactionsMessagesContacts" : "EnableReactionsStoriesContacts";
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(1);
                final boolean[] zArr = {notificationsSettings3.getBoolean(str3, false)};
                final RadioColorCell[] radioColorCellArr = new RadioColorCell[2];
                final int i8 = 0;
                while (i8 < 2) {
                    radioColorCellArr[i8] = new RadioColorCell(context, getResourceProvider());
                    radioColorCellArr[i8].setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                    radioColorCellArr[i8].setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                    radioColorCellArr[i8].setTextAndValue(LocaleController.getString(i8 == 0 ? R.string.NotifyAboutReactionsFromEveryone : R.string.NotifyAboutReactionsFromContacts), i8 == 0 ? !zArr[0] : zArr[0]);
                    radioColorCellArr[i8].setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                    linearLayout.addView(radioColorCellArr[i8]);
                    radioColorCellArr[i8].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            NotificationsCustomSettingsActivity.lambda$createView$15(zArr, i8, radioColorCellArr, view2);
                        }
                    });
                    i8++;
                }
                showDialog(new AlertDialog.Builder(getContext(), this.resourceProvider).setTitle(LocaleController.getString(R.string.NotifyAboutReactionsFrom)).setView(linearLayout).setNegativeButton(LocaleController.getString(R.string.Cancel), null).setPositiveButton(LocaleController.getString(R.string.Save), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i9) {
                        NotificationsCustomSettingsActivity.this.lambda$createView$16(notificationsSettings3, str3, zArr, dialogInterface, i9);
                    }
                }).create());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(NotificationsSettingsActivity.NotificationException notificationException, View view, int i) {
        updateMute(notificationException, view, i, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(NotificationsSettingsActivity.NotificationException notificationException, View view, int i) {
        updateMute(notificationException, view, i, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(NotificationsSettingsActivity.NotificationException notificationException, View view, boolean z) {
        this.actionBar.closeSearchField();
        updateMute(notificationException, view, -1, z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(NotificationsSettingsActivity.NotificationException notificationException, View view, boolean z) {
        this.actionBar.closeSearchField();
        updateMute(notificationException, view, -1, z, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$8(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
        int i = 0;
        long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        if (this.currentType == 3) {
            ArrayList<NotificationsSettingsActivity.NotificationException> arrayList2 = this.autoExceptions;
            if (arrayList2 != null) {
                Iterator<NotificationsSettingsActivity.NotificationException> it = arrayList2.iterator();
                while (it.hasNext()) {
                    if (it.next().did == j) {
                        it.remove();
                    }
                }
            }
            ArrayList<NotificationsSettingsActivity.NotificationException> arrayList3 = this.exceptions;
            if (arrayList3 != null) {
                Iterator<NotificationsSettingsActivity.NotificationException> it2 = arrayList3.iterator();
                while (it2.hasNext()) {
                    if (it2.next().did == j) {
                        it2.remove();
                    }
                }
            }
            NotificationsSettingsActivity.NotificationException notificationException = new NotificationsSettingsActivity.NotificationException();
            notificationException.did = j;
            notificationException.story = true;
            Boolean bool = this.storiesEnabled;
            if (bool != null && bool.booleanValue()) {
                i = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            notificationException.notify = i;
            if (this.exceptions == null) {
                this.exceptions = new ArrayList<>();
            }
            this.exceptions.add(notificationException);
            updateRows(true);
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong("dialog_id", j);
            bundle.putBoolean("exception", true);
            ProfileNotificationsActivity profileNotificationsActivity = new ProfileNotificationsActivity(bundle, getResourceProvider());
            profileNotificationsActivity.setDelegate(new ProfileNotificationsActivity.ProfileNotificationsActivityDelegate() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda21
                @Override // org.telegram.ui.ProfileNotificationsActivity.ProfileNotificationsActivityDelegate
                public final void didCreateNewException(NotificationsSettingsActivity.NotificationException notificationException2) {
                    NotificationsCustomSettingsActivity.this.lambda$createView$7(notificationException2);
                }

                @Override // org.telegram.ui.ProfileNotificationsActivity.ProfileNotificationsActivityDelegate
                public /* synthetic */ void didRemoveException(long j2) {
                    ProfileNotificationsActivity.ProfileNotificationsActivityDelegate.-CC.$default$didRemoveException(this, j2);
                }
            });
            presentFragment(profileNotificationsActivity, true);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(NotificationsSettingsActivity.NotificationException notificationException) {
        this.exceptions.add(0, notificationException);
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(DialogInterface dialogInterface, int i) {
        SharedPreferences.Editor edit = getNotificationsSettings().edit();
        int size = this.exceptions.size();
        for (int i2 = 0; i2 < size; i2++) {
            NotificationsSettingsActivity.NotificationException notificationException = this.exceptions.get(i2);
            if (this.currentType == 3) {
                edit.remove(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + notificationException.did);
            } else {
                SharedPreferences.Editor remove = edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + notificationException.did);
                remove.remove(NotificationsSettingsFacade.PROPERTY_CUSTOM + notificationException.did);
            }
            getMessagesStorage().setDialogFlags(notificationException.did, 0L);
            TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(notificationException.did);
            if (tLRPC$Dialog != null) {
                tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
            }
        }
        edit.commit();
        int size2 = this.exceptions.size();
        for (int i3 = 0; i3 < size2; i3++) {
            getNotificationsController().updateServerNotificationsSettings(this.exceptions.get(i3).did, this.topicId, false);
        }
        this.exceptions.clear();
        this.exceptionsDict.clear();
        updateRows(true);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(int i) {
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(View view, int i) {
        if (view instanceof TextColorCell) {
            if (i >= 0 && i < this.items.size()) {
                this.items.get(i).color = getLedColor();
            }
            ((TextColorCell) view).setTextAndColor(LocaleController.getString("LedColor", R.string.LedColor), getLedColor(), true);
            return;
        }
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(View view, int i) {
        if (view instanceof TextSettingsCell) {
            if (i >= 0 && i < this.items.size()) {
                this.items.get(i).text2 = getPopupOption();
            }
            TextSettingsCell textSettingsCell = (TextSettingsCell) view;
            textSettingsCell.setTextAndValue(LocaleController.getString("PopupNotification", R.string.PopupNotification), getPopupOption(), true, textSettingsCell.needDivider);
            return;
        }
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13(View view, String str, int i) {
        if (view instanceof TextSettingsCell) {
            String string = LocaleController.getString(this.vibrateLabels[Utilities.clamp(getNotificationsSettings().getInt(str, 0), this.vibrateLabels.length - 1, 0)]);
            if (i >= 0 && i < this.items.size()) {
                this.items.get(i).text2 = string;
            }
            ((TextSettingsCell) view).setTextAndValue(LocaleController.getString("Vibrate", R.string.Vibrate), string, true, true);
            return;
        }
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$14(View view, int i) {
        if (view instanceof TextSettingsCell) {
            if (i >= 0 && i < this.items.size()) {
                this.items.get(i).text2 = getPriorityOption();
            }
            TextSettingsCell textSettingsCell = (TextSettingsCell) view;
            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", R.string.NotificationsImportance), getPriorityOption(), true, textSettingsCell.needDivider);
            return;
        }
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$15(boolean[] zArr, int i, RadioColorCell[] radioColorCellArr, View view) {
        zArr[0] = i == 1;
        int i2 = 0;
        while (i2 < radioColorCellArr.length) {
            radioColorCellArr[i2].setChecked(zArr[0] == (i2 == 1), true);
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$16(SharedPreferences sharedPreferences, String str, boolean[] zArr, DialogInterface dialogInterface, int i) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(str, zArr[0]);
        edit.apply();
        updateRows(true);
        getNotificationsController().updateServerNotificationsSettings(this.currentType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkRowsEnabled() {
        boolean isGlobalNotificationsEnabled;
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList;
        if (this.exceptions.isEmpty() || this.currentType == 3) {
            int childCount = this.listView.getChildCount();
            ArrayList<Animator> arrayList2 = new ArrayList<>();
            if (this.currentType == 3) {
                Boolean bool = this.storiesEnabled;
                isGlobalNotificationsEnabled = bool == null || bool.booleanValue() || !((arrayList = this.exceptions) == null || arrayList.isEmpty());
            } else {
                isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(this.currentType);
            }
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.getChildViewHolder(childAt);
                int childAdapterPosition = this.listView.getChildAdapterPosition(childAt);
                ItemInner itemInner = null;
                if (childAdapterPosition >= 0 && childAdapterPosition < this.items.size()) {
                    itemInner = this.items.get(childAdapterPosition);
                }
                boolean z = (itemInner == null || itemInner.id != 102) ? isGlobalNotificationsEnabled : true;
                int itemViewType = holder.getItemViewType();
                if (itemViewType == 0) {
                    ((HeaderCell) holder.itemView).setEnabled(z, arrayList2);
                } else if (itemViewType == 1) {
                    ((TextCheckCell) holder.itemView).setEnabled(z, arrayList2);
                } else if (itemViewType == 3) {
                    ((TextColorCell) holder.itemView).setEnabled(z, arrayList2);
                } else if (itemViewType == 5) {
                    ((TextSettingsCell) holder.itemView).setEnabled(z, arrayList2);
                }
            }
            if (arrayList2.isEmpty()) {
                return;
            }
            AnimatorSet animatorSet = this.animatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.playTogether(arrayList2);
            this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(NotificationsCustomSettingsActivity.this.animatorSet)) {
                        NotificationsCustomSettingsActivity.this.animatorSet = null;
                    }
                }
            });
            this.animatorSet.setDuration(150L);
            this.animatorSet.start();
        }
    }

    private void loadExceptions() {
        final ArrayList arrayList;
        if (this.currentType == 3) {
            MediaDataController.getInstance(this.currentAccount).loadHints(true);
            arrayList = new ArrayList(MediaDataController.getInstance(this.currentAccount).hints);
        } else {
            arrayList = null;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$20(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't wrap try/catch for region: R(8:80|(2:94|95)(2:82|(2:93|90)(1:84))|85|86|87|88|89|90) */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x02a4, code lost:
        if (r8.deleted != false) goto L122;
     */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0331  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0376  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x0390 A[LOOP:5: B:163:0x038e->B:164:0x0390, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x03aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadExceptions$20(ArrayList arrayList) {
        ArrayList<TLRPC$User> arrayList2;
        boolean z;
        final ArrayList<TLRPC$Chat> arrayList3;
        ArrayList<TLRPC$User> arrayList4;
        ArrayList<TLRPC$EncryptedChat> arrayList5;
        int size;
        int i;
        ArrayList arrayList6;
        int size2;
        int i2;
        int size3;
        int i3;
        int size4;
        int i4;
        ArrayList arrayList7;
        ArrayList arrayList8;
        ArrayList arrayList9;
        long longValue;
        ArrayList arrayList10;
        ArrayList arrayList11;
        ArrayList arrayList12;
        long j;
        final ArrayList arrayList13 = new ArrayList();
        ArrayList arrayList14 = new ArrayList();
        ArrayList arrayList15 = new ArrayList();
        ArrayList arrayList16 = new ArrayList();
        ArrayList arrayList17 = new ArrayList();
        LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList<Long> arrayList18 = new ArrayList<>();
        ArrayList arrayList19 = new ArrayList();
        ArrayList arrayList20 = new ArrayList();
        ArrayList<TLRPC$User> arrayList21 = new ArrayList<>();
        ArrayList<TLRPC$Chat> arrayList22 = new ArrayList<>();
        ArrayList<TLRPC$EncryptedChat> arrayList23 = new ArrayList<>();
        long j2 = getUserConfig().clientUserId;
        SharedPreferences notificationsSettings = getNotificationsSettings();
        Map<String, ?> all = notificationsSettings.getAll();
        Iterator<Map.Entry<String, ?>> it = all.entrySet().iterator();
        while (true) {
            arrayList2 = arrayList21;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, ?> next = it.next();
            String key = next.getKey();
            ArrayList<TLRPC$EncryptedChat> arrayList24 = arrayList23;
            if (key.startsWith(NotificationsSettingsFacade.PROPERTY_NOTIFY)) {
                arrayList12 = arrayList16;
                String replace = key.replace(NotificationsSettingsFacade.PROPERTY_NOTIFY, "");
                arrayList10 = arrayList14;
                arrayList11 = arrayList15;
                long longValue2 = Utilities.parseLong(replace).longValue();
                if (longValue2 != 0 && longValue2 != j2) {
                    NotificationsSettingsActivity.NotificationException notificationException = new NotificationsSettingsActivity.NotificationException();
                    notificationException.did = longValue2;
                    StringBuilder sb = new StringBuilder();
                    j = j2;
                    sb.append(NotificationsSettingsFacade.PROPERTY_CUSTOM);
                    sb.append(longValue2);
                    notificationException.hasCustom = notificationsSettings.getBoolean(sb.toString(), false);
                    int intValue = ((Integer) next.getValue()).intValue();
                    notificationException.notify = intValue;
                    if (intValue != 0) {
                        Integer num = (Integer) all.get(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + replace);
                        if (num != null) {
                            notificationException.muteUntil = num.intValue();
                        }
                    }
                    if (DialogObject.isEncryptedDialog(longValue2)) {
                        int encryptedChatId = DialogObject.getEncryptedChatId(longValue2);
                        TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                        if (encryptedChat == null) {
                            arrayList20.add(Integer.valueOf(encryptedChatId));
                            longSparseArray.put(longValue2, notificationException);
                        } else {
                            TLRPC$User user = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                            if (user == null) {
                                arrayList18.add(Long.valueOf(encryptedChat.user_id));
                                longSparseArray.put(encryptedChat.user_id, notificationException);
                            } else if (user.deleted) {
                            }
                        }
                        arrayList13.add(notificationException);
                    } else if (DialogObject.isUserDialog(longValue2)) {
                        TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(longValue2));
                        if (user2 == null) {
                            arrayList18.add(Long.valueOf(longValue2));
                            longSparseArray.put(longValue2, notificationException);
                        } else if (user2.deleted) {
                        }
                        arrayList13.add(notificationException);
                    } else {
                        long j3 = -longValue2;
                        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(j3));
                        if (chat == null) {
                            arrayList19.add(Long.valueOf(j3));
                            longSparseArray.put(longValue2, notificationException);
                        } else if (!chat.left && !chat.kicked && chat.migrated_to == null) {
                            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                                arrayList17.add(notificationException);
                            } else {
                                arrayList10.add(notificationException);
                            }
                        }
                    }
                    arrayList14 = arrayList10;
                    arrayList21 = arrayList2;
                    arrayList23 = arrayList24;
                    arrayList16 = arrayList12;
                    arrayList15 = arrayList11;
                    j2 = j;
                }
            } else {
                arrayList10 = arrayList14;
                arrayList11 = arrayList15;
                arrayList12 = arrayList16;
            }
            j = j2;
            arrayList14 = arrayList10;
            arrayList21 = arrayList2;
            arrayList23 = arrayList24;
            arrayList16 = arrayList12;
            arrayList15 = arrayList11;
            j2 = j;
        }
        ArrayList arrayList25 = arrayList15;
        ArrayList arrayList26 = arrayList16;
        ArrayList<TLRPC$EncryptedChat> arrayList27 = arrayList23;
        long j4 = j2;
        final ArrayList arrayList28 = arrayList14;
        HashSet hashSet = new HashSet();
        Iterator<Map.Entry<String, ?>> it2 = all.entrySet().iterator();
        while (true) {
            z = true;
            if (!it2.hasNext()) {
                break;
            }
            Map.Entry<String, ?> next2 = it2.next();
            String key2 = next2.getKey();
            if (key2.startsWith(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY)) {
                try {
                    longValue = Utilities.parseLong(key2.substring(8)).longValue();
                } catch (Exception unused) {
                }
                if (longValue != 0 && longValue != j4) {
                    NotificationsSettingsActivity.NotificationException notificationException2 = new NotificationsSettingsActivity.NotificationException();
                    notificationException2.did = longValue;
                    notificationException2.story = true;
                    notificationException2.notify = ((Boolean) next2.getValue()).booleanValue() ? 0 : ConnectionsManager.DEFAULT_DATACENTER_ID;
                    if (DialogObject.isUserDialog(longValue)) {
                        TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(longValue));
                        if (user3 == null) {
                            arrayList18.add(Long.valueOf(longValue));
                            longSparseArray.put(longValue, notificationException2);
                        } else if (user3.deleted) {
                        }
                        arrayList9 = arrayList25;
                        arrayList9.add(notificationException2);
                        hashSet.add(Long.valueOf(longValue));
                        arrayList25 = arrayList9;
                    }
                }
            }
            arrayList9 = arrayList25;
            arrayList25 = arrayList9;
        }
        final ArrayList arrayList29 = arrayList25;
        if (arrayList != null) {
            Collections.sort(arrayList, Comparator$-CC.comparingDouble(new ToDoubleFunction() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda16
                @Override // j$.util.function.ToDoubleFunction
                public final double applyAsDouble(Object obj) {
                    double d;
                    d = ((TLRPC$TL_topPeer) obj).rating;
                    return d;
                }
            }));
            int max = Math.max(0, arrayList.size() - 6);
            while (max < arrayList.size()) {
                long peerDialogId = DialogObject.getPeerDialogId(((TLRPC$TL_topPeer) arrayList.get(max)).peer);
                if (!hashSet.contains(Long.valueOf(peerDialogId))) {
                    NotificationsSettingsActivity.NotificationException notificationException3 = new NotificationsSettingsActivity.NotificationException();
                    notificationException3.did = peerDialogId;
                    notificationException3.story = z;
                    notificationException3.notify = 0;
                    notificationException3.auto = z;
                    if (DialogObject.isUserDialog(peerDialogId)) {
                        TLRPC$User user4 = getMessagesController().getUser(Long.valueOf(peerDialogId));
                        if (user4 == null) {
                            arrayList18.add(Long.valueOf(peerDialogId));
                            longSparseArray.put(peerDialogId, notificationException3);
                        }
                        arrayList8 = arrayList26;
                        arrayList8.add(0, notificationException3);
                        hashSet.add(Long.valueOf(peerDialogId));
                        max++;
                        arrayList26 = arrayList8;
                        z = true;
                    }
                }
                arrayList8 = arrayList26;
                max++;
                arrayList26 = arrayList8;
                z = true;
            }
        }
        final ArrayList arrayList30 = arrayList26;
        if (longSparseArray.size() != 0) {
            try {
                if (arrayList20.isEmpty()) {
                    arrayList5 = arrayList27;
                } else {
                    try {
                        arrayList5 = arrayList27;
                    } catch (Exception e) {
                        e = e;
                        arrayList5 = arrayList27;
                        arrayList3 = arrayList22;
                        arrayList4 = arrayList2;
                        FileLog.e(e);
                        size = arrayList3.size();
                        i = 0;
                        while (i < size) {
                        }
                        arrayList6 = arrayList17;
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList<TLRPC$User> arrayList31 = arrayList4;
                        final ArrayList<TLRPC$EncryptedChat> arrayList32 = arrayList5;
                        final ArrayList arrayList33 = arrayList6;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList31, arrayList3, arrayList32, arrayList13, arrayList28, arrayList29, arrayList30, arrayList33);
                            }
                        });
                    }
                    try {
                        getMessagesStorage().getEncryptedChatsInternal(TextUtils.join(",", arrayList20), arrayList5, arrayList18);
                    } catch (Exception e2) {
                        e = e2;
                        arrayList3 = arrayList22;
                        arrayList4 = arrayList2;
                        FileLog.e(e);
                        size = arrayList3.size();
                        i = 0;
                        while (i < size) {
                        }
                        arrayList6 = arrayList17;
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList arrayList312 = arrayList4;
                        final ArrayList arrayList322 = arrayList5;
                        final ArrayList arrayList332 = arrayList6;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList312, arrayList3, arrayList322, arrayList13, arrayList28, arrayList29, arrayList30, arrayList332);
                            }
                        });
                    }
                }
                if (arrayList18.isEmpty()) {
                    arrayList4 = arrayList2;
                } else {
                    try {
                        arrayList4 = arrayList2;
                    } catch (Exception e3) {
                        e = e3;
                        arrayList4 = arrayList2;
                        arrayList3 = arrayList22;
                        FileLog.e(e);
                        size = arrayList3.size();
                        i = 0;
                        while (i < size) {
                        }
                        arrayList6 = arrayList17;
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList arrayList3122 = arrayList4;
                        final ArrayList arrayList3222 = arrayList5;
                        final ArrayList arrayList3322 = arrayList6;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList3122, arrayList3, arrayList3222, arrayList13, arrayList28, arrayList29, arrayList30, arrayList3322);
                            }
                        });
                    }
                    try {
                        getMessagesStorage().getUsersInternal(TextUtils.join(",", arrayList18), arrayList4);
                    } catch (Exception e4) {
                        e = e4;
                        arrayList3 = arrayList22;
                        FileLog.e(e);
                        size = arrayList3.size();
                        i = 0;
                        while (i < size) {
                        }
                        arrayList6 = arrayList17;
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList arrayList31222 = arrayList4;
                        final ArrayList arrayList32222 = arrayList5;
                        final ArrayList arrayList33222 = arrayList6;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList31222, arrayList3, arrayList32222, arrayList13, arrayList28, arrayList29, arrayList30, arrayList33222);
                            }
                        });
                    }
                }
                if (arrayList19.isEmpty()) {
                    arrayList3 = arrayList22;
                } else {
                    MessagesStorage messagesStorage = getMessagesStorage();
                    String join = TextUtils.join(",", arrayList19);
                    arrayList3 = arrayList22;
                    try {
                        messagesStorage.getChatsInternal(join, arrayList3);
                    } catch (Exception e5) {
                        e = e5;
                        FileLog.e(e);
                        size = arrayList3.size();
                        i = 0;
                        while (i < size) {
                        }
                        arrayList6 = arrayList17;
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList arrayList312222 = arrayList4;
                        final ArrayList arrayList322222 = arrayList5;
                        final ArrayList arrayList332222 = arrayList6;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList312222, arrayList3, arrayList322222, arrayList13, arrayList28, arrayList29, arrayList30, arrayList332222);
                            }
                        });
                    }
                }
            } catch (Exception e6) {
                e = e6;
                arrayList3 = arrayList22;
                arrayList4 = arrayList2;
                arrayList5 = arrayList27;
            }
            size = arrayList3.size();
            i = 0;
            while (i < size) {
                TLRPC$Chat tLRPC$Chat = arrayList3.get(i);
                if (tLRPC$Chat.left || tLRPC$Chat.kicked || tLRPC$Chat.migrated_to != null) {
                    arrayList7 = arrayList17;
                } else {
                    arrayList7 = arrayList17;
                    NotificationsSettingsActivity.NotificationException notificationException4 = (NotificationsSettingsActivity.NotificationException) longSparseArray.get(-tLRPC$Chat.id);
                    longSparseArray.remove(-tLRPC$Chat.id);
                    if (notificationException4 != null) {
                        if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                            arrayList7.add(notificationException4);
                        } else {
                            arrayList28.add(notificationException4);
                        }
                    }
                }
                i++;
                arrayList17 = arrayList7;
            }
            arrayList6 = arrayList17;
            size2 = arrayList4.size();
            for (i2 = 0; i2 < size2; i2++) {
                TLRPC$User tLRPC$User = arrayList4.get(i2);
                if (!tLRPC$User.deleted) {
                    longSparseArray.remove(tLRPC$User.id);
                }
            }
            size3 = arrayList5.size();
            for (i3 = 0; i3 < size3; i3++) {
                longSparseArray.remove(DialogObject.makeEncryptedDialogId(arrayList5.get(i3).id));
            }
            size4 = longSparseArray.size();
            for (i4 = 0; i4 < size4; i4++) {
                if (DialogObject.isChatDialog(longSparseArray.keyAt(i4))) {
                    arrayList28.remove(longSparseArray.valueAt(i4));
                    arrayList6.remove(longSparseArray.valueAt(i4));
                } else {
                    arrayList13.remove(longSparseArray.valueAt(i4));
                }
            }
        } else {
            arrayList6 = arrayList17;
            arrayList3 = arrayList22;
            arrayList4 = arrayList2;
            arrayList5 = arrayList27;
        }
        final ArrayList arrayList3122222 = arrayList4;
        final ArrayList arrayList3222222 = arrayList5;
        final ArrayList arrayList3322222 = arrayList6;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList3122222, arrayList3, arrayList3222222, arrayList13, arrayList28, arrayList29, arrayList30, arrayList3322222);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadExceptions$19(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5, ArrayList arrayList6, ArrayList arrayList7, ArrayList arrayList8) {
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        getMessagesController().putEncryptedChats(arrayList3, true);
        int i = this.currentType;
        if (i == 1) {
            this.exceptions = arrayList4;
        } else if (i == 0) {
            this.exceptions = arrayList5;
        } else if (i == 3) {
            this.exceptions = arrayList6;
            this.autoExceptions = arrayList7;
        } else {
            this.exceptions = arrayList8;
        }
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows(boolean z) {
        ArrayList<NotificationsSettingsActivity.NotificationException> arrayList;
        int i;
        int i2;
        int i3;
        boolean z2;
        int i4;
        Boolean bool;
        this.oldItems.clear();
        this.oldItems.addAll(this.items);
        this.items.clear();
        SharedPreferences notificationsSettings = getNotificationsSettings();
        if (this.currentType != -1) {
            this.items.add(ItemInner.asHeader(LocaleController.getString(R.string.NotifyMeAbout)));
            int i5 = this.currentType;
            if (i5 == 3) {
                this.items.add(ItemInner.asCheck(101, LocaleController.getString(R.string.NotifyMeAboutNewStories), notificationsSettings.getBoolean("EnableAllStories", false)));
                if (!notificationsSettings.getBoolean("EnableAllStories", false)) {
                    this.items.add(ItemInner.asCheck(R.styleable.AppCompatTheme_textAppearanceLargePopupMenu, LocaleController.getString(R.string.NotifyMeAboutImportantStories), this.storiesAuto && ((bool = this.storiesEnabled) == null || !bool.booleanValue())));
                }
                this.items.add(ItemInner.asShadow(-1, LocaleController.getString(R.string.StoryAutoExceptionsInfo)));
            } else if (i5 == 4 || i5 == 5) {
                ArrayList<ItemInner> arrayList2 = this.items;
                int i6 = R.drawable.msg_markunread;
                String string = LocaleController.getString(R.string.NotifyMeAboutMessagesReactions);
                if (!notificationsSettings.getBoolean("EnableReactionsMessages", true)) {
                    i = R.string.NotifyFromNobody;
                } else if (notificationsSettings.getBoolean("EnableReactionsMessagesContacts", false)) {
                    i = R.string.NotifyFromContacts;
                } else {
                    i = R.string.NotifyFromEveryone;
                }
                arrayList2.add(ItemInner.asCheck2(R.styleable.AppCompatTheme_textAppearanceListItem, i6, string, LocaleController.getString(i), notificationsSettings.getBoolean("EnableReactionsMessages", true)));
                ArrayList<ItemInner> arrayList3 = this.items;
                int i7 = R.drawable.msg_stories_saved;
                String string2 = LocaleController.getString(R.string.NotifyMeAboutStoriesReactions);
                if (!notificationsSettings.getBoolean("EnableReactionsStories", true)) {
                    i2 = R.string.NotifyFromNobody;
                } else if (notificationsSettings.getBoolean("EnableReactionsStoriesContacts", false)) {
                    i2 = R.string.NotifyFromContacts;
                } else {
                    i2 = R.string.NotifyFromEveryone;
                }
                arrayList3.add(ItemInner.asCheck2(R.styleable.AppCompatTheme_textAppearanceListItemSecondary, i7, string2, LocaleController.getString(i2), notificationsSettings.getBoolean("EnableReactionsStories", true)));
                this.items.add(ItemInner.asShadow(-1, null));
            } else {
                if (i5 == 1) {
                    i3 = R.string.NotifyMeAboutPrivate;
                } else if (i5 == 0) {
                    i3 = R.string.NotifyMeAboutGroups;
                } else {
                    i3 = R.string.NotifyMeAboutChannels;
                }
                this.items.add(ItemInner.asCheck(100, LocaleController.getString(i3), getNotificationsController().isGlobalNotificationsEnabled(this.currentType)));
                this.items.add(ItemInner.asShadow(-1, null));
            }
            this.items.add(ItemInner.asHeader(LocaleController.getString(R.string.SETTINGS)));
            this.settingsStart = this.items.size() - 1;
            int i8 = this.currentType;
            if (i8 == 3) {
                this.items.add(ItemInner.asCheck(0, LocaleController.getString(R.string.NotificationShowSenderNames), !notificationsSettings.getBoolean("EnableHideStoriesSenders", false)));
            } else if (i8 == 4 || i8 == 5) {
                this.items.add(ItemInner.asCheck(0, LocaleController.getString(R.string.NotificationShowSenderNames), notificationsSettings.getBoolean("EnableReactionsPreview", true)));
            } else {
                if (i8 == 0) {
                    z2 = notificationsSettings.getBoolean("EnablePreviewGroup", true);
                } else if (i8 == 1) {
                    z2 = notificationsSettings.getBoolean("EnablePreviewAll", true);
                } else {
                    z2 = i8 != 2 ? false : notificationsSettings.getBoolean("EnablePreviewChannel", true);
                }
                this.items.add(ItemInner.asCheck(0, LocaleController.getString(R.string.MessagePreview), z2));
            }
            this.items.add(ItemInner.asSetting(3, LocaleController.getString("Sound", R.string.Sound), getSound()));
            if (this.expanded) {
                this.items.add(ItemInner.asColor(LocaleController.getString("LedColor", R.string.LedColor), getLedColor()));
                int i9 = this.currentType;
                if (i9 == 0) {
                    i4 = notificationsSettings.getInt("vibrate_group", 0);
                } else if (i9 == 1) {
                    i4 = notificationsSettings.getInt("vibrate_messages", 0);
                } else if (i9 == 2) {
                    i4 = notificationsSettings.getInt("vibrate_channel", 0);
                } else if (i9 == 3) {
                    i4 = notificationsSettings.getInt("vibrate_stories", 0);
                } else {
                    i4 = (i9 == 4 || i9 == 5) ? notificationsSettings.getInt("vibrate_react", 0) : 0;
                }
                ArrayList<ItemInner> arrayList4 = this.items;
                String string3 = LocaleController.getString("Vibrate", R.string.Vibrate);
                int[] iArr = this.vibrateLabels;
                arrayList4.add(ItemInner.asSetting(1, string3, LocaleController.getString(iArr[Utilities.clamp(i4, iArr.length - 1, 0)])));
                int i10 = this.currentType;
                if (i10 == 1 || i10 == 0) {
                    this.items.add(ItemInner.asSetting(2, LocaleController.getString("PopupNotification", R.string.PopupNotification), getPopupOption()));
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    this.items.add(ItemInner.asSetting(4, LocaleController.getString("NotificationsImportance", R.string.NotificationsImportance), getPriorityOption()));
                }
                this.items.add(ItemInner.asExpand(LocaleController.getString(R.string.NotifyLessOptions), false));
            } else {
                this.items.add(ItemInner.asExpand(LocaleController.getString(R.string.NotifyMoreOptions), true));
            }
            this.settingsEnd = this.items.size() - 1;
            this.items.add(ItemInner.asShadow(-2, null));
        }
        int i11 = this.currentType;
        if (i11 != 4 && i11 != 5) {
            if (i11 != -1) {
                this.items.add(ItemInner.asButton(6, R.drawable.msg_contact_add, LocaleController.getString("NotificationsAddAnException", R.string.NotificationsAddAnException)));
            }
            this.exceptionsStart = this.items.size() - 1;
            if (this.autoExceptions != null && this.showAutoExceptions) {
                for (int i12 = 0; i12 < this.autoExceptions.size(); i12++) {
                    this.items.add(ItemInner.asException(this.autoExceptions.get(i12)));
                }
            }
            if (this.exceptions != null) {
                for (int i13 = 0; i13 < this.exceptions.size(); i13++) {
                    this.items.add(ItemInner.asException(this.exceptions.get(i13)));
                }
            }
            this.exceptionsEnd = this.items.size() - 1;
            if (this.currentType != -1 || ((arrayList = this.exceptions) != null && !arrayList.isEmpty())) {
                this.items.add(ItemInner.asShadow(-3, null));
            }
            ArrayList<NotificationsSettingsActivity.NotificationException> arrayList5 = this.exceptions;
            if (arrayList5 != null && !arrayList5.isEmpty()) {
                this.items.add(ItemInner.asButton(7, 0, LocaleController.getString("NotificationsDeleteAllException", R.string.NotificationsDeleteAllException)));
            }
        } else {
            this.exceptionsStart = -1;
            this.exceptionsEnd = -1;
        }
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            if (z) {
                listAdapter.setItems(this.oldItems, this.items);
            } else {
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyVisible() {
        super.onBecomeFullyVisible();
        updateRows(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        Ringtone ringtone;
        if (i2 == -1) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String str = null;
            if (uri != null && (ringtone = RingtoneManager.getRingtone(getParentActivity(), uri)) != null) {
                if (uri.equals(Settings.System.DEFAULT_NOTIFICATION_URI)) {
                    str = LocaleController.getString("SoundDefault", R.string.SoundDefault);
                } else {
                    str = ringtone.getTitle(getParentActivity());
                }
                ringtone.stop();
            }
            SharedPreferences.Editor edit = getNotificationsSettings().edit();
            int i3 = this.currentType;
            if (i3 == 1) {
                if (str != null && uri != null) {
                    edit.putString("GlobalSound", str);
                    edit.putString("GlobalSoundPath", uri.toString());
                } else {
                    edit.putString("GlobalSound", "NoSound");
                    edit.putString("GlobalSoundPath", "NoSound");
                }
            } else if (i3 == 0) {
                if (str != null && uri != null) {
                    edit.putString("GroupSound", str);
                    edit.putString("GroupSoundPath", uri.toString());
                } else {
                    edit.putString("GroupSound", "NoSound");
                    edit.putString("GroupSoundPath", "NoSound");
                }
            } else if (i3 == 2) {
                if (str != null && uri != null) {
                    edit.putString("ChannelSound", str);
                    edit.putString("ChannelSoundPath", uri.toString());
                } else {
                    edit.putString("ChannelSound", "NoSound");
                    edit.putString("ChannelSoundPath", "NoSound");
                }
            } else if (i3 == 3) {
                if (str != null && uri != null) {
                    edit.putString("StoriesSound", str);
                    edit.putString("StoriesSoundPath", uri.toString());
                } else {
                    edit.putString("StoriesSound", "NoSound");
                    edit.putString("StoriesSoundPath", "NoSound");
                }
            }
            getNotificationsController().deleteNotificationChannelGlobal(this.currentType);
            edit.commit();
            getNotificationsController().updateServerNotificationsSettings(this.currentType);
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(i);
            if (findViewHolderForAdapterPosition != null) {
                this.adapter.onBindViewHolder(findViewHolderForAdapterPosition, i);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        getNotificationCenter().addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        getNotificationCenter().addObserver(this, NotificationCenter.reloadHints);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        getNotificationCenter().removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
        getNotificationCenter().removeObserver(this, NotificationCenter.reloadHints);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.notificationsSettingsUpdated) {
            ListAdapter listAdapter = this.adapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
        } else if (i == NotificationCenter.reloadHints) {
            loadExceptions();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private SearchAdapterHelper searchAdapterHelper;
        private ArrayList<NotificationsSettingsActivity.NotificationException> searchResult = new ArrayList<>();
        private ArrayList<CharSequence> searchResultNames = new ArrayList<>();
        private Runnable searchRunnable;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public SearchAdapter(Context context) {
            this.mContext = context;
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda4
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ boolean canApplySearchResults(int i) {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ androidx.collection.LongSparseArray getExcludeCallParticipants() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ androidx.collection.LongSparseArray getExcludeUsers() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public final void onDataSetChanged(int i) {
                    NotificationsCustomSettingsActivity.SearchAdapter.this.lambda$new$0(i);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                    SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$onSetHashtags(this, arrayList, hashMap);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int i) {
            if (this.searchRunnable == null && !this.searchAdapterHelper.isSearchInProgress()) {
                NotificationsCustomSettingsActivity.this.emptyView.showTextView();
            }
            notifyDataSetChanged();
        }

        public void searchDialogs(final String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (str == null) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                this.searchAdapterHelper.mergeResults(null);
                this.searchAdapterHelper.queryServerSearch(null, true, (NotificationsCustomSettingsActivity.this.currentType == 1 || NotificationsCustomSettingsActivity.this.currentType == 3) ? false : true, true, false, false, 0L, false, 0, 0);
                notifyDataSetChanged();
                return;
            }
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.SearchAdapter.this.lambda$searchDialogs$1(str);
                }
            };
            this.searchRunnable = runnable;
            dispatchQueue.postRunnable(runnable, 300L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: processSearch */
        public void lambda$searchDialogs$1(final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.SearchAdapter.this.lambda$processSearch$3(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSearch$3(final String str) {
            this.searchAdapterHelper.queryServerSearch(str, true, (NotificationsCustomSettingsActivity.this.currentType == 1 || NotificationsCustomSettingsActivity.this.currentType == 3) ? false : true, true, false, false, 0L, false, 0, 0);
            final ArrayList arrayList = new ArrayList(NotificationsCustomSettingsActivity.this.exceptions);
            Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.SearchAdapter.this.lambda$processSearch$2(str, arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:62:0x0170, code lost:
            if (r10[r5].contains(" " + r15) == false) goto L40;
         */
        /* JADX WARN: Code restructure failed: missing block: B:69:0x0190, code lost:
            if (r6.contains(" " + r15) != false) goto L63;
         */
        /* JADX WARN: Removed duplicated region for block: B:52:0x0137  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0147  */
        /* JADX WARN: Removed duplicated region for block: B:84:0x01e7 A[LOOP:1: B:56:0x0145->B:84:0x01e7, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:94:0x01a8 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$2(String str, ArrayList arrayList) {
            int i;
            char c;
            String[] strArr;
            int i2;
            TLRPC$User tLRPC$User;
            String translitString;
            int i3;
            int i4;
            char c2;
            char c3;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                return;
            }
            String translitString2 = LocaleController.getInstance().getTranslitString(lowerCase);
            translitString2 = (lowerCase.equals(translitString2) || translitString2.length() == 0) ? null : null;
            int i5 = (translitString2 != null ? 1 : 0) + 1;
            String[] strArr2 = new String[i5];
            strArr2[0] = lowerCase;
            if (translitString2 != null) {
                strArr2[1] = translitString2;
            }
            ArrayList<Object> arrayList2 = new ArrayList<>();
            ArrayList<NotificationsSettingsActivity.NotificationException> arrayList3 = new ArrayList<>();
            ArrayList<CharSequence> arrayList4 = new ArrayList<>();
            String[] strArr3 = new String[2];
            int i6 = 0;
            while (i6 < arrayList.size()) {
                NotificationsSettingsActivity.NotificationException notificationException = (NotificationsSettingsActivity.NotificationException) arrayList.get(i6);
                if (DialogObject.isEncryptedDialog(notificationException.did)) {
                    TLRPC$EncryptedChat encryptedChat = NotificationsCustomSettingsActivity.this.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(notificationException.did)));
                    if (encryptedChat != null) {
                        i = i5;
                        TLRPC$User user = NotificationsCustomSettingsActivity.this.getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                        if (user != null) {
                            strArr3[0] = ContactsController.formatName(user.first_name, user.last_name);
                            strArr3[1] = UserObject.getPublicUsername(user);
                        }
                    } else {
                        i = i5;
                    }
                } else {
                    i = i5;
                    if (DialogObject.isUserDialog(notificationException.did)) {
                        TLRPC$User user2 = NotificationsCustomSettingsActivity.this.getMessagesController().getUser(Long.valueOf(notificationException.did));
                        if (user2 != null && !user2.deleted) {
                            strArr3[0] = ContactsController.formatName(user2.first_name, user2.last_name);
                            strArr3[1] = UserObject.getPublicUsername(user2);
                            c = 0;
                            tLRPC$User = user2;
                            String str2 = strArr3[c];
                            strArr3[c] = strArr3[c].toLowerCase();
                            translitString = LocaleController.getInstance().getTranslitString(strArr3[c]);
                            if (strArr3[c] != null && strArr3[c].equals(translitString)) {
                                translitString = null;
                            }
                            i3 = i;
                            i4 = 0;
                            char c4 = 0;
                            while (i4 < i3) {
                                String str3 = strArr2[i4];
                                strArr = strArr2;
                                if (strArr3[c] != null) {
                                    i2 = i3;
                                    if (!strArr3[c].startsWith(str3)) {
                                    }
                                    c2 = 1;
                                    c3 = 1;
                                    if (c3 != 0) {
                                        if (c3 == c2) {
                                            arrayList4.add(AndroidUtilities.generateSearchName(str2, null, str3));
                                        } else {
                                            arrayList4.add(AndroidUtilities.generateSearchName("@" + strArr3[c2], null, "@" + str3));
                                        }
                                        arrayList3.add(notificationException);
                                        if (tLRPC$User != null) {
                                            arrayList2.add(tLRPC$User);
                                        }
                                    } else {
                                        i4++;
                                        c4 = c3;
                                        i3 = i2;
                                        strArr2 = strArr;
                                        c = 0;
                                    }
                                } else {
                                    i2 = i3;
                                }
                                if (translitString != null) {
                                    if (!translitString.startsWith(str3)) {
                                    }
                                    c2 = 1;
                                    c3 = 1;
                                    if (c3 != 0) {
                                    }
                                }
                                c2 = 1;
                                c3 = (strArr3[1] == null || !strArr3[1].startsWith(str3)) ? c4 : (char) 2;
                                if (c3 != 0) {
                                }
                            }
                            strArr = strArr2;
                            i2 = i3;
                        }
                        strArr = strArr2;
                        i2 = i;
                    } else {
                        TLRPC$Chat chat = NotificationsCustomSettingsActivity.this.getMessagesController().getChat(Long.valueOf(-notificationException.did));
                        if (chat != null) {
                            if (!chat.left && !chat.kicked && chat.migrated_to == null) {
                                c = 0;
                                strArr3[0] = chat.title;
                                strArr3[1] = ChatObject.getPublicUsername(chat);
                                tLRPC$User = chat;
                                String str22 = strArr3[c];
                                strArr3[c] = strArr3[c].toLowerCase();
                                translitString = LocaleController.getInstance().getTranslitString(strArr3[c]);
                                if (strArr3[c] != null) {
                                    translitString = null;
                                }
                                i3 = i;
                                i4 = 0;
                                char c42 = 0;
                                while (i4 < i3) {
                                }
                                strArr = strArr2;
                                i2 = i3;
                            }
                            strArr = strArr2;
                            i2 = i;
                        }
                    }
                    i6++;
                    i5 = i2;
                    strArr2 = strArr;
                }
                c = 0;
                tLRPC$User = null;
                String str222 = strArr3[c];
                strArr3[c] = strArr3[c].toLowerCase();
                translitString = LocaleController.getInstance().getTranslitString(strArr3[c]);
                if (strArr3[c] != null) {
                }
                i3 = i;
                i4 = 0;
                char c422 = 0;
                while (i4 < i3) {
                }
                strArr = strArr2;
                i2 = i3;
                i6++;
                i5 = i2;
                strArr2 = strArr;
            }
            updateSearchResults(arrayList2, arrayList3, arrayList4);
        }

        private void updateSearchResults(final ArrayList<Object> arrayList, final ArrayList<NotificationsSettingsActivity.NotificationException> arrayList2, final ArrayList<CharSequence> arrayList3) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.SearchAdapter.this.lambda$updateSearchResults$4(arrayList2, arrayList3, arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$4(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
            if (NotificationsCustomSettingsActivity.this.searching) {
                this.searchRunnable = null;
                this.searchResult = arrayList;
                this.searchResultNames = arrayList2;
                this.searchAdapterHelper.mergeResults(arrayList3);
                if (NotificationsCustomSettingsActivity.this.searching && !this.searchAdapterHelper.isSearchInProgress()) {
                    NotificationsCustomSettingsActivity.this.emptyView.showTextView();
                }
                notifyDataSetChanged();
            }
        }

        public Object getObject(int i) {
            if (i >= 0 && i < this.searchResult.size()) {
                return this.searchResult.get(i);
            }
            int size = i - (this.searchResult.size() + 1);
            ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            if (size < 0 || size >= globalSearch.size()) {
                return null;
            }
            return this.searchAdapterHelper.getGlobalSearch().get(size);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.searchResult.size();
            ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            return !globalSearch.isEmpty() ? size + globalSearch.size() + 1 : size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View userCell;
            if (i == 0) {
                userCell = new UserCell(this.mContext, 4, 0, false, true);
                userCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else {
                userCell = new GraySectionCell(this.mContext);
            }
            return new RecyclerListView.Holder(userCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    return;
                }
                ((GraySectionCell) viewHolder.itemView).setText(LocaleController.getString("AddToExceptions", R.string.AddToExceptions));
                return;
            }
            UserCell userCell = (UserCell) viewHolder.itemView;
            if (i < this.searchResult.size()) {
                userCell.setException(this.searchResult.get(i), this.searchResultNames.get(i), i != this.searchResult.size() - 1);
                userCell.setAddButtonVisible(false);
                return;
            }
            int size = i - (this.searchResult.size() + 1);
            ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            userCell.setData(globalSearch.get(size), null, LocaleController.getString("NotificationsOn", R.string.NotificationsOn), 0, size != globalSearch.size() - 1);
            userCell.setAddButtonVisible(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == this.searchResult.size() ? 1 : 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ItemInner extends AdapterWithDiffUtils.Item {
        public boolean checked;
        public int color;
        public NotificationsSettingsActivity.NotificationException exception;
        public int id;
        public int resId;
        public CharSequence text;
        public CharSequence text2;

        private ItemInner(int i) {
            super(i, true);
        }

        public static ItemInner asHeader(CharSequence charSequence) {
            ItemInner itemInner = new ItemInner(0);
            itemInner.text = charSequence;
            return itemInner;
        }

        public static ItemInner asCheck(int i, CharSequence charSequence, boolean z) {
            ItemInner itemInner = new ItemInner(1);
            itemInner.id = i;
            itemInner.text = charSequence;
            itemInner.checked = z;
            return itemInner;
        }

        public static ItemInner asCheck2(int i, int i2, CharSequence charSequence, CharSequence charSequence2, boolean z) {
            ItemInner itemInner = new ItemInner(6);
            itemInner.id = i;
            itemInner.resId = i2;
            itemInner.text = charSequence;
            itemInner.text2 = charSequence2;
            itemInner.checked = z;
            return itemInner;
        }

        public static ItemInner asException(NotificationsSettingsActivity.NotificationException notificationException) {
            ItemInner itemInner = new ItemInner(2);
            itemInner.exception = notificationException;
            return itemInner;
        }

        public static ItemInner asColor(CharSequence charSequence, int i) {
            ItemInner itemInner = new ItemInner(3);
            itemInner.text = charSequence;
            itemInner.color = i;
            return itemInner;
        }

        public static ItemInner asShadow(int i, CharSequence charSequence) {
            ItemInner itemInner = new ItemInner(4);
            itemInner.id = i;
            itemInner.text = charSequence;
            return itemInner;
        }

        public static ItemInner asSetting(int i, CharSequence charSequence, CharSequence charSequence2) {
            ItemInner itemInner = new ItemInner(5);
            itemInner.id = i;
            itemInner.text = charSequence;
            itemInner.text2 = charSequence2;
            return itemInner;
        }

        public static ItemInner asButton(int i, int i2, CharSequence charSequence) {
            ItemInner itemInner = new ItemInner(7);
            itemInner.id = i;
            itemInner.resId = i2;
            itemInner.text = charSequence;
            return itemInner;
        }

        public static ItemInner asExpand(CharSequence charSequence, boolean z) {
            ItemInner itemInner = new ItemInner(8);
            itemInner.text = charSequence;
            itemInner.resId = z ? 1 : 0;
            return itemInner;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || ItemInner.class != obj.getClass()) {
                return false;
            }
            ItemInner itemInner = (ItemInner) obj;
            return this.id == itemInner.id && this.color == itemInner.color && (this.viewType == 8 || (this.resId == itemInner.resId && Objects.equals(this.text, itemInner.text) && (this.viewType == 6 || Objects.equals(this.text2, itemInner.text2)))) && this.exception == itemInner.exception;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.ListView.AdapterWithDiffUtils.Item
        public boolean contentsEquals(AdapterWithDiffUtils.Item item) {
            if (this == item) {
                return true;
            }
            if (item == null || ItemInner.class != item.getClass()) {
                return false;
            }
            ItemInner itemInner = (ItemInner) item;
            return this.id == itemInner.id && this.resId == itemInner.resId && this.color == itemInner.color && this.checked == itemInner.checked && Objects.equals(this.text, itemInner.text) && Objects.equals(this.text2, itemInner.text2) && this.exception == itemInner.exception;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends AdapterWithDiffUtils {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return (itemViewType == 0 || itemViewType == 4) ? false : true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return NotificationsCustomSettingsActivity.this.items.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View headerCell;
            switch (i) {
                case 0:
                    headerCell = new HeaderCell(this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    headerCell = new TextCheckCell(this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 2:
                    headerCell = new UserCell(this.mContext, 6, 0, false);
                    headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    headerCell = new TextColorCell(this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    headerCell = new TextInfoPrivacyCell(this.mContext);
                    break;
                case 5:
                    headerCell = new TextSettingsCell(this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 6:
                    headerCell = new NotificationsCheckCell(this.mContext, 21, 64, true, ((BaseFragment) NotificationsCustomSettingsActivity.this).resourceProvider);
                    headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 7:
                default:
                    headerCell = new TextCell(this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 8:
                    headerCell = new ExpandView(NotificationsCustomSettingsActivity.this, this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new RecyclerListView.Holder(headerCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (i < 0 || i >= NotificationsCustomSettingsActivity.this.items.size()) {
                return;
            }
            ItemInner itemInner = (ItemInner) NotificationsCustomSettingsActivity.this.items.get(i);
            int i2 = i + 1;
            boolean z = i2 < NotificationsCustomSettingsActivity.this.items.size() && ((ItemInner) NotificationsCustomSettingsActivity.this.items.get(i2)).viewType != 4;
            switch (viewHolder.getItemViewType()) {
                case 0:
                    ((HeaderCell) viewHolder.itemView).setText(itemInner.text);
                    return;
                case 1:
                    ((TextCheckCell) viewHolder.itemView).setTextAndCheck("" + ((Object) itemInner.text), itemInner.checked, z);
                    return;
                case 2:
                    ((UserCell) viewHolder.itemView).setException(itemInner.exception, null, z);
                    return;
                case 3:
                    ((TextColorCell) viewHolder.itemView).setTextAndColor("" + ((Object) itemInner.text), itemInner.color, z);
                    return;
                case 4:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    if (itemInner.text == null) {
                        textInfoPrivacyCell.setFixedSize(12);
                        textInfoPrivacyCell.setText(null);
                    } else {
                        textInfoPrivacyCell.setFixedSize(0);
                        textInfoPrivacyCell.setText(itemInner.text);
                    }
                    if (!z) {
                        viewHolder.itemView.setBackground(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else {
                        viewHolder.itemView.setBackground(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        return;
                    }
                case 5:
                    ((TextSettingsCell) viewHolder.itemView).setTextAndValue(itemInner.text, itemInner.text2, z);
                    return;
                case 6:
                    NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
                    notificationsCheckCell.setDrawLine(true);
                    notificationsCheckCell.setChecked(itemInner.checked);
                    notificationsCheckCell.setTextAndValueAndIconAndCheck(itemInner.text, itemInner.text2, itemInner.resId, itemInner.checked, 0, false, z, true);
                    return;
                case 7:
                    TextCell textCell = (TextCell) viewHolder.itemView;
                    if (itemInner.resId == 0) {
                        textCell.setColors(-1, Theme.key_text_RedRegular);
                        textCell.setText("" + ((Object) itemInner.text), z);
                        return;
                    }
                    textCell.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
                    textCell.setTextAndIcon("" + ((Object) itemInner.text), itemInner.resId, z);
                    return;
                case 8:
                    ExpandView expandView = (ExpandView) viewHolder.itemView;
                    expandView.setColors(Theme.key_windowBackgroundWhiteBlueIcon, Theme.key_windowBackgroundWhiteBlueButton);
                    expandView.set(itemInner.text, itemInner.resId == 1, z);
                    return;
                default:
                    return;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (NotificationsCustomSettingsActivity.this.currentType == 3 || (NotificationsCustomSettingsActivity.this.exceptions != null && NotificationsCustomSettingsActivity.this.exceptions.isEmpty())) {
                boolean isGlobalNotificationsEnabled = NotificationsCustomSettingsActivity.this.currentType == 3 ? NotificationsCustomSettingsActivity.this.storiesEnabled == null || NotificationsCustomSettingsActivity.this.storiesEnabled.booleanValue() || !(NotificationsCustomSettingsActivity.this.exceptions == null || NotificationsCustomSettingsActivity.this.exceptions.isEmpty()) : NotificationsCustomSettingsActivity.this.getNotificationsController().isGlobalNotificationsEnabled(NotificationsCustomSettingsActivity.this.currentType);
                int adapterPosition = viewHolder.getAdapterPosition();
                ItemInner itemInner = (adapterPosition < 0 || adapterPosition >= NotificationsCustomSettingsActivity.this.items.size()) ? null : (ItemInner) NotificationsCustomSettingsActivity.this.items.get(adapterPosition);
                if (itemInner == null || itemInner.id != 102) {
                    int itemViewType = viewHolder.getItemViewType();
                    if (itemViewType == 0) {
                        ((HeaderCell) viewHolder.itemView).setEnabled(isGlobalNotificationsEnabled, null);
                    } else if (itemViewType == 1) {
                        ((TextCheckCell) viewHolder.itemView).setEnabled(isGlobalNotificationsEnabled, null);
                    } else if (itemViewType == 3) {
                        ((TextColorCell) viewHolder.itemView).setEnabled(isGlobalNotificationsEnabled, null);
                    } else if (itemViewType != 5) {
                    } else {
                        ((TextSettingsCell) viewHolder.itemView).setEnabled(isGlobalNotificationsEnabled, null);
                    }
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i < 0 || i >= NotificationsCustomSettingsActivity.this.items.size()) {
                return 5;
            }
            return ((ItemInner) NotificationsCustomSettingsActivity.this.items.get(i)).viewType;
        }
    }

    /* loaded from: classes4.dex */
    public class ExpandView extends TextCell {
        public ImageView imageView;

        public ExpandView(NotificationsCustomSettingsActivity notificationsCustomSettingsActivity, Context context) {
            super(context);
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.setColorFilter(new PorterDuffColorFilter(notificationsCustomSettingsActivity.getThemedColor(Theme.key_windowBackgroundWhiteBlueIcon), PorterDuff.Mode.SRC_IN));
            this.imageView.setImageResource(R.drawable.msg_expand);
            addView(this.imageView, LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 3 : 5) | 16, 17.0f, 0.0f, 17.0f, 0.0f));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Cells.TextCell, android.widget.FrameLayout, android.view.View
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.imageView.measure(i, i2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Cells.TextCell, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int dp = LocaleController.isRTL ? AndroidUtilities.dp(17.0f) : (i3 - i) - AndroidUtilities.dp(41.0f);
            int dp2 = ((i4 - i2) - AndroidUtilities.dp(24.0f)) / 2;
            this.imageView.layout(dp, dp2, AndroidUtilities.dp(24.0f) + dp, AndroidUtilities.dp(24.0f) + dp2);
        }

        public void set(CharSequence charSequence, boolean z, boolean z2) {
            setArrow(z, true);
            setText(charSequence, z2);
        }

        public void setArrow(boolean z, boolean z2) {
            if (z2) {
                this.imageView.animate().rotation(z ? 0.0f : 180.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).setDuration(340L).start();
            } else {
                this.imageView.setRotation(z ? 0.0f : 180.0f);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda18
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                NotificationsCustomSettingsActivity.this.lambda$getThemeDescriptions$21();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCheckCell.class, TextColorCell.class, TextSettingsCell.class, UserCell.class, NotificationsCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        int i4 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        int i5 = Theme.key_switchTrack;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        int i6 = Theme.key_switchTrackChecked;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayIcon));
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
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_graySectionText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, Theme.key_graySection));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextColorCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteValueText));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueButton));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_text_RedRegular));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueIcon));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$21() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(0);
                }
            }
        }
    }
}
