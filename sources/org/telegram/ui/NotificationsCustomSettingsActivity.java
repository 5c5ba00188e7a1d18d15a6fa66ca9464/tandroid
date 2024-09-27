package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Dialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
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
import org.telegram.tgnet.TLRPC;
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
    private ArrayList autoExceptions;
    private int currentType;
    private EmptyTextProgressView emptyView;
    private ArrayList exceptions;
    private HashMap exceptionsDict;
    private int exceptionsEnd;
    private int exceptionsStart;
    boolean expanded;
    private final ArrayList items;
    private RecyclerListView listView;
    private final ArrayList oldItems;
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

    /* loaded from: classes4.dex */
    public class ExpandView extends TextCell {
        public ImageView imageView;

        public ExpandView(Context context) {
            super(context);
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.setColorFilter(new PorterDuffColorFilter(NotificationsCustomSettingsActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlueIcon), PorterDuff.Mode.SRC_IN));
            this.imageView.setImageResource(R.drawable.msg_expand);
            addView(this.imageView, LayoutHelper.createFrame(24, 24.0f, (LocaleController.isRTL ? 3 : 5) | 16, 17.0f, 0.0f, 17.0f, 0.0f));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Cells.TextCell, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int dp = LocaleController.isRTL ? AndroidUtilities.dp(17.0f) : (i3 - i) - AndroidUtilities.dp(41.0f);
            int dp2 = ((i4 - i2) - AndroidUtilities.dp(24.0f)) / 2;
            this.imageView.layout(dp, dp2, AndroidUtilities.dp(24.0f) + dp, AndroidUtilities.dp(24.0f) + dp2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Cells.TextCell, android.widget.FrameLayout, android.view.View
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.imageView.measure(i, i2);
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

        public static ItemInner asButton(int i, int i2, CharSequence charSequence) {
            ItemInner itemInner = new ItemInner(7);
            itemInner.id = i;
            itemInner.resId = i2;
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

        public static ItemInner asColor(CharSequence charSequence, int i) {
            ItemInner itemInner = new ItemInner(3);
            itemInner.text = charSequence;
            itemInner.color = i;
            return itemInner;
        }

        public static ItemInner asException(NotificationsSettingsActivity.NotificationException notificationException) {
            ItemInner itemInner = new ItemInner(2);
            itemInner.exception = notificationException;
            return itemInner;
        }

        public static ItemInner asExpand(CharSequence charSequence, boolean z) {
            ItemInner itemInner = new ItemInner(8);
            itemInner.text = charSequence;
            itemInner.resId = z ? 1 : 0;
            return itemInner;
        }

        public static ItemInner asHeader(CharSequence charSequence) {
            ItemInner itemInner = new ItemInner(0);
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

        public static ItemInner asShadow(int i, CharSequence charSequence) {
            ItemInner itemInner = new ItemInner(4);
            itemInner.id = i;
            itemInner.text = charSequence;
            return itemInner;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.ListView.AdapterWithDiffUtils.Item
        public boolean contentsEquals(AdapterWithDiffUtils.Item item) {
            if (this == item) {
                return true;
            }
            if (item == null || getClass() != item.getClass()) {
                return false;
            }
            ItemInner itemInner = (ItemInner) item;
            return this.id == itemInner.id && this.resId == itemInner.resId && this.color == itemInner.color && this.checked == itemInner.checked && Objects.equals(this.text, itemInner.text) && Objects.equals(this.text2, itemInner.text2) && this.exception == itemInner.exception;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ItemInner itemInner = (ItemInner) obj;
            return this.id == itemInner.id && this.color == itemInner.color && (this.viewType == 8 || (this.resId == itemInner.resId && Objects.equals(this.text, itemInner.text) && (this.viewType == 6 || Objects.equals(this.text2, itemInner.text2)))) && this.exception == itemInner.exception;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends AdapterWithDiffUtils {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return NotificationsCustomSettingsActivity.this.items.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i < 0 || i >= NotificationsCustomSettingsActivity.this.items.size()) {
                return 5;
            }
            return ((ItemInner) NotificationsCustomSettingsActivity.this.items.get(i)).viewType;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return (itemViewType == 0 || itemViewType == 4) ? false : true;
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
                    viewHolder.itemView.setBackground(Theme.getThemedDrawableByKey(this.mContext, !z ? R.drawable.greydivider_bottom : R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    return;
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
                    headerCell = new ExpandView(this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new RecyclerListView.Holder(headerCell);
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SearchAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private SearchAdapterHelper searchAdapterHelper;
        private ArrayList searchResult = new ArrayList();
        private ArrayList searchResultNames = new ArrayList();
        private Runnable searchRunnable;

        public SearchAdapter(Context context) {
            this.mContext = context;
            SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
            this.searchAdapterHelper = searchAdapterHelper;
            searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ boolean canApplySearchResults(int i) {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                    return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
                }

                @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
                public /* synthetic */ LongSparseArray getExcludeUsers() {
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

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:62:0x0169, code lost:
            if (r10[0].contains(" " + r15) == false) goto L41;
         */
        /* JADX WARN: Code restructure failed: missing block: B:69:0x0189, code lost:
            if (r6.contains(" " + r15) != false) goto L65;
         */
        /* JADX WARN: Removed duplicated region for block: B:52:0x0133  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0141  */
        /* JADX WARN: Removed duplicated region for block: B:86:0x01e0 A[LOOP:1: B:56:0x013f->B:86:0x01e0, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:96:0x01a1 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$processSearch$2(String str, ArrayList arrayList) {
            int i;
            String[] strArr;
            int i2;
            TLRPC.Chat chat;
            String translitString;
            String str2;
            int i3;
            int i4;
            char c;
            char c2;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList(), new ArrayList(), new ArrayList());
                return;
            }
            String translitString2 = LocaleController.getInstance().getTranslitString(lowerCase);
            translitString2 = (lowerCase.equals(translitString2) || translitString2.length() == 0) ? null : null;
            char c3 = 0;
            int i5 = (translitString2 != null ? 1 : 0) + 1;
            String[] strArr2 = new String[i5];
            strArr2[0] = lowerCase;
            if (translitString2 != null) {
                strArr2[1] = translitString2;
            }
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            String[] strArr3 = new String[2];
            int i6 = 0;
            while (i6 < arrayList.size()) {
                NotificationsSettingsActivity.NotificationException notificationException = (NotificationsSettingsActivity.NotificationException) arrayList.get(i6);
                if (DialogObject.isEncryptedDialog(notificationException.did)) {
                    TLRPC.EncryptedChat encryptedChat = NotificationsCustomSettingsActivity.this.getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(notificationException.did)));
                    if (encryptedChat != null) {
                        i = i5;
                        TLRPC.User user = NotificationsCustomSettingsActivity.this.getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                        if (user != null) {
                            strArr3[0] = ContactsController.formatName(user.first_name, user.last_name);
                            strArr3[1] = UserObject.getPublicUsername(user);
                        }
                    } else {
                        i = i5;
                        chat = null;
                        String str3 = strArr3[c3];
                        strArr3[c3] = str3.toLowerCase();
                        translitString = LocaleController.getInstance().getTranslitString(strArr3[c3]);
                        str2 = strArr3[c3];
                        if (str2 != null && str2.equals(translitString)) {
                            translitString = null;
                        }
                        i3 = i;
                        i4 = 0;
                        char c4 = 0;
                        while (i4 < i3) {
                            String str4 = strArr2[i4];
                            strArr = strArr2;
                            String str5 = strArr3[c3];
                            if (str5 != null) {
                                i2 = i3;
                                if (!str5.startsWith(str4)) {
                                }
                                c = 1;
                                c2 = 1;
                                if (c2 != 0) {
                                    if (c2 == c) {
                                        arrayList4.add(AndroidUtilities.generateSearchName(str3, null, str4));
                                    } else {
                                        arrayList4.add(AndroidUtilities.generateSearchName("@" + strArr3[c], null, "@" + str4));
                                    }
                                    arrayList3.add(notificationException);
                                    if (chat != null) {
                                        arrayList2.add(chat);
                                    }
                                    i6++;
                                    strArr2 = strArr;
                                    i5 = i2;
                                    c3 = 0;
                                } else {
                                    i4++;
                                    c4 = c2;
                                    strArr2 = strArr;
                                    i3 = i2;
                                    c3 = 0;
                                }
                            } else {
                                i2 = i3;
                            }
                            if (translitString != null) {
                                if (!translitString.startsWith(str4)) {
                                }
                                c = 1;
                                c2 = 1;
                                if (c2 != 0) {
                                }
                            }
                            c = 1;
                            String str6 = strArr3[1];
                            c2 = (str6 == null || !str6.startsWith(str4)) ? c4 : (char) 2;
                            if (c2 != 0) {
                            }
                        }
                        strArr = strArr2;
                        i2 = i3;
                        i6++;
                        strArr2 = strArr;
                        i5 = i2;
                        c3 = 0;
                    }
                } else {
                    i = i5;
                    if (DialogObject.isUserDialog(notificationException.did)) {
                        TLRPC.User user2 = NotificationsCustomSettingsActivity.this.getMessagesController().getUser(Long.valueOf(notificationException.did));
                        if (user2 != null && !user2.deleted) {
                            strArr3[0] = ContactsController.formatName(user2.first_name, user2.last_name);
                            strArr3[1] = UserObject.getPublicUsername(user2);
                            c3 = 0;
                            chat = user2;
                            String str32 = strArr3[c3];
                            strArr3[c3] = str32.toLowerCase();
                            translitString = LocaleController.getInstance().getTranslitString(strArr3[c3]);
                            str2 = strArr3[c3];
                            if (str2 != null) {
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
                    } else {
                        TLRPC.Chat chat2 = NotificationsCustomSettingsActivity.this.getMessagesController().getChat(Long.valueOf(-notificationException.did));
                        if (chat2 != null) {
                            if (!chat2.left && !chat2.kicked && chat2.migrated_to == null) {
                                c3 = 0;
                                strArr3[0] = chat2.title;
                                strArr3[1] = ChatObject.getPublicUsername(chat2);
                                chat = chat2;
                                String str322 = strArr3[c3];
                                strArr3[c3] = str322.toLowerCase();
                                translitString = LocaleController.getInstance().getTranslitString(strArr3[c3]);
                                str2 = strArr3[c3];
                                if (str2 != null) {
                                }
                                i3 = i;
                                i4 = 0;
                                char c422 = 0;
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
                    strArr2 = strArr;
                    i5 = i2;
                    c3 = 0;
                }
                c3 = 0;
                chat = null;
                String str3222 = strArr3[c3];
                strArr3[c3] = str3222.toLowerCase();
                translitString = LocaleController.getInstance().getTranslitString(strArr3[c3]);
                str2 = strArr3[c3];
                if (str2 != null) {
                }
                i3 = i;
                i4 = 0;
                char c4222 = 0;
                while (i4 < i3) {
                }
                strArr = strArr2;
                i2 = i3;
                i6++;
                strArr2 = strArr;
                i5 = i2;
                c3 = 0;
            }
            updateSearchResults(arrayList2, arrayList3, arrayList4);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSearch$3(final String str) {
            this.searchAdapterHelper.queryServerSearch(str, true, (NotificationsCustomSettingsActivity.this.currentType == 1 || NotificationsCustomSettingsActivity.this.currentType == 3) ? false : true, true, false, false, 0L, false, 0, 0);
            final ArrayList arrayList = new ArrayList(NotificationsCustomSettingsActivity.this.exceptions);
            Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.SearchAdapter.this.lambda$processSearch$2(str, arrayList);
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

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: processSearch */
        public void lambda$searchDialogs$1(final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.SearchAdapter.this.lambda$processSearch$3(str);
                }
            });
        }

        private void updateSearchResults(final ArrayList arrayList, final ArrayList arrayList2, final ArrayList arrayList3) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.SearchAdapter.this.lambda$updateSearchResults$4(arrayList2, arrayList3, arrayList);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.searchResult.size();
            ArrayList globalSearch = this.searchAdapterHelper.getGlobalSearch();
            return !globalSearch.isEmpty() ? size + globalSearch.size() + 1 : size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == this.searchResult.size() ? 1 : 0;
        }

        public Object getObject(int i) {
            ArrayList globalSearch;
            if (i < 0 || i >= this.searchResult.size()) {
                i -= this.searchResult.size() + 1;
                ArrayList globalSearch2 = this.searchAdapterHelper.getGlobalSearch();
                if (i < 0 || i >= globalSearch2.size()) {
                    return null;
                }
                globalSearch = this.searchAdapterHelper.getGlobalSearch();
            } else {
                globalSearch = this.searchResult;
            }
            return globalSearch.get(i);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
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
                userCell.setException((NotificationsSettingsActivity.NotificationException) this.searchResult.get(i), (CharSequence) this.searchResultNames.get(i), i != this.searchResult.size() - 1);
                userCell.setAddButtonVisible(false);
                return;
            }
            int size = i - (this.searchResult.size() + 1);
            ArrayList globalSearch = this.searchAdapterHelper.getGlobalSearch();
            userCell.setData((TLObject) globalSearch.get(size), null, LocaleController.getString("NotificationsOn", R.string.NotificationsOn), 0, size != globalSearch.size() - 1);
            userCell.setAddButtonVisible(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View userCell;
            if (i != 0) {
                userCell = new GraySectionCell(this.mContext);
            } else {
                userCell = new UserCell(this.mContext, 4, 0, false, true);
                userCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
            return new RecyclerListView.Holder(userCell);
        }

        public void searchDialogs(final String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (str != null) {
                DispatchQueue dispatchQueue = Utilities.searchQueue;
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$SearchAdapter$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationsCustomSettingsActivity.SearchAdapter.this.lambda$searchDialogs$1(str);
                    }
                };
                this.searchRunnable = runnable;
                dispatchQueue.postRunnable(runnable, 300L);
                return;
            }
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchAdapterHelper.mergeResults(null);
            this.searchAdapterHelper.queryServerSearch(null, true, (NotificationsCustomSettingsActivity.this.currentType == 1 || NotificationsCustomSettingsActivity.this.currentType == 3) ? false : true, true, false, false, 0L, false, 0, 0);
            notifyDataSetChanged();
        }
    }

    public NotificationsCustomSettingsActivity(int i, ArrayList arrayList, ArrayList arrayList2) {
        this(i, arrayList, arrayList2, false);
    }

    public NotificationsCustomSettingsActivity(int i, ArrayList arrayList, ArrayList arrayList2, boolean z) {
        this.showAutoExceptions = true;
        this.exceptionsDict = new HashMap();
        this.topicId = 0;
        this.vibrateLabels = new int[]{R.string.VibrationDefault, R.string.Short, R.string.VibrationDisabled, R.string.Long, R.string.OnlyIfSilent};
        this.popupOptions = new int[]{R.string.NoPopup, R.string.OnlyWhenScreenOn, R.string.OnlyWhenScreenOff, R.string.AlwaysShowPopup};
        int i2 = R.string.NotificationsPriorityHigh;
        int i3 = R.string.NotificationsPriorityUrgent;
        int i4 = R.string.NotificationsPriorityMedium;
        this.priorityOptions = new int[]{i2, i3, i3, i4, R.string.NotificationsPriorityLow, i4};
        this.oldItems = new ArrayList();
        this.items = new ArrayList();
        this.currentType = i;
        this.autoExceptions = arrayList2;
        this.exceptions = arrayList;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i5 = 0; i5 < size; i5++) {
                NotificationsSettingsActivity.NotificationException notificationException = (NotificationsSettingsActivity.NotificationException) this.exceptions.get(i5);
                this.exceptionsDict.put(Long.valueOf(notificationException.did), notificationException);
            }
        }
        ArrayList arrayList3 = this.autoExceptions;
        if (arrayList3 != null) {
            int size2 = arrayList3.size();
            for (int i6 = 0; i6 < size2; i6++) {
                NotificationsSettingsActivity.NotificationException notificationException2 = (NotificationsSettingsActivity.NotificationException) this.autoExceptions.get(i6);
                this.exceptionsDict.put(Long.valueOf(notificationException2.did), notificationException2);
            }
        }
        if (z) {
            loadExceptions();
        }
    }

    public static boolean areStoriesNotMuted(int i, long j) {
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(i);
        if (!notificationsSettings.contains(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j)) {
            return notificationsSettings.contains("EnableAllStories") ? notificationsSettings.getBoolean("EnableAllStories", true) : isTop5Peer(i, j);
        }
        return notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkRowsEnabled() {
        boolean isGlobalNotificationsEnabled;
        int i;
        ArrayList arrayList;
        if (this.exceptions.isEmpty() || this.currentType == 3) {
            int childCount = this.listView.getChildCount();
            ArrayList arrayList2 = new ArrayList();
            if (this.currentType == 3) {
                Boolean bool = this.storiesEnabled;
                isGlobalNotificationsEnabled = bool == null || bool.booleanValue() || !((arrayList = this.exceptions) == null || arrayList.isEmpty());
            } else {
                isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(this.currentType);
            }
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = this.listView.getChildAt(i2);
                RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.getChildViewHolder(childAt);
                int childAdapterPosition = this.listView.getChildAdapterPosition(childAt);
                ItemInner itemInner = (childAdapterPosition < 0 || childAdapterPosition >= this.items.size()) ? null : (ItemInner) this.items.get(childAdapterPosition);
                boolean z = (itemInner == null || !((i = itemInner.id) == 102 || i == 101 || i == 100)) ? isGlobalNotificationsEnabled : true;
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

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: deleteException */
    public void lambda$createView$6(NotificationsSettingsActivity.NotificationException notificationException, View view, int i) {
        String sharedPrefKey = NotificationsController.getSharedPrefKey(notificationException.did, 0L);
        SharedPreferences.Editor edit = getNotificationsSettings().edit();
        edit.remove(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey).commit();
        ArrayList arrayList = this.autoExceptions;
        if (arrayList != null) {
            arrayList.remove(notificationException);
        }
        ArrayList arrayList2 = this.exceptions;
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

    /* JADX WARN: Removed duplicated region for block: B:24:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int getLedColor() {
        SharedPreferences notificationsSettings;
        String str;
        int i;
        int i2 = this.currentType;
        int i3 = -16776961;
        if (i2 == 0) {
            notificationsSettings = getNotificationsSettings();
            str = "GroupLed";
        } else if (i2 == 1) {
            notificationsSettings = getNotificationsSettings();
            str = "MessagesLed";
        } else if (i2 == 2) {
            notificationsSettings = getNotificationsSettings();
            str = "ChannelLed";
        } else if (i2 != 3) {
            if (i2 == 4 || i2 == 5) {
                notificationsSettings = getNotificationsSettings();
                str = "ReactionsLed";
            }
            for (i = 0; i < 9; i++) {
                if (TextColorCell.colorsToSave[i] == i3) {
                    return TextColorCell.colors[i];
                }
            }
            return i3;
        } else {
            notificationsSettings = getNotificationsSettings();
            str = "StoriesLed";
        }
        i3 = notificationsSettings.getInt(str, -16776961);
        while (i < 9) {
        }
        return i3;
    }

    private String getPopupOption() {
        SharedPreferences notificationsSettings;
        String str;
        int i;
        int i2 = this.currentType;
        if (i2 == 0) {
            notificationsSettings = getNotificationsSettings();
            str = "popupGroup";
        } else if (i2 == 1) {
            notificationsSettings = getNotificationsSettings();
            str = "popupAll";
        } else if (i2 != 2) {
            i = 0;
            int[] iArr = this.popupOptions;
            return LocaleController.getString(iArr[Utilities.clamp(i, iArr.length - 1, 0)]);
        } else {
            notificationsSettings = getNotificationsSettings();
            str = "popupChannel";
        }
        i = notificationsSettings.getInt(str, 0);
        int[] iArr2 = this.popupOptions;
        return LocaleController.getString(iArr2[Utilities.clamp(i, iArr2.length - 1, 0)]);
    }

    private String getPriorityOption() {
        SharedPreferences notificationsSettings;
        String str;
        int i;
        int i2 = this.currentType;
        if (i2 == 0) {
            notificationsSettings = getNotificationsSettings();
            str = "priority_group";
        } else if (i2 == 1) {
            notificationsSettings = getNotificationsSettings();
            str = "priority_messages";
        } else if (i2 == 2) {
            notificationsSettings = getNotificationsSettings();
            str = "priority_channel";
        } else if (i2 == 3) {
            notificationsSettings = getNotificationsSettings();
            str = "priority_stories";
        } else if (i2 != 4 && i2 != 5) {
            i = 1;
            int[] iArr = this.priorityOptions;
            return LocaleController.getString(iArr[Utilities.clamp(i, iArr.length - 1, 0)]);
        } else {
            notificationsSettings = getNotificationsSettings();
            str = "priority_react";
        }
        i = notificationsSettings.getInt(str, 1);
        int[] iArr2 = this.priorityOptions;
        return LocaleController.getString(iArr2[Utilities.clamp(i, iArr2.length - 1, 0)]);
    }

    private String getSound() {
        String string;
        String str;
        SharedPreferences notificationsSettings = getNotificationsSettings();
        int i = R.string.SoundDefault;
        String string2 = LocaleController.getString("SoundDefault", i);
        int i2 = this.currentType;
        if (i2 == 0) {
            string = notificationsSettings.getString("GroupSound", string2);
            str = "GroupSoundDocId";
        } else if (i2 == 1) {
            string = notificationsSettings.getString("GlobalSound", string2);
            str = "GlobalSoundDocId";
        } else if (i2 == 3) {
            string = notificationsSettings.getString("StoriesSound", string2);
            str = "StoriesSoundDocId";
        } else if (i2 == 4 || i2 == 5) {
            string = notificationsSettings.getString("ReactionSound", string2);
            str = "ReactionSoundDocId";
        } else {
            string = notificationsSettings.getString("ChannelSound", string2);
            str = "ChannelDocId";
        }
        long j = notificationsSettings.getLong(str, 0L);
        if (j == 0) {
            return string.equals("NoSound") ? LocaleController.getString("NoSound", R.string.NoSound) : string.equals("Default") ? LocaleController.getString("SoundDefault", i) : string;
        }
        TLRPC.Document document = getMediaDataController().ringtoneDataStore.getDocument(j);
        return document == null ? LocaleController.getString("CustomSound", R.string.CustomSound) : NotificationsSoundActivity.trimTitle(document, FileLoader.getDocumentFileName(document));
    }

    private static boolean isTop5Peer(int i, long j) {
        ArrayList arrayList = new ArrayList(MediaDataController.getInstance(i).hints);
        Collections.sort(arrayList, Comparator$-CC.comparingDouble(new ToDoubleFunction() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda3
            @Override // j$.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                double d;
                d = ((TLRPC.TL_topPeer) obj).rating;
                return d;
            }
        }));
        int i2 = -1;
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            if (DialogObject.getPeerDialogId(((TLRPC.TL_topPeer) arrayList.get(i3)).peer) == j) {
                i2 = i3;
            }
        }
        return i2 >= 0 && i2 >= arrayList.size() + (-5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(NotificationsSettingsActivity.NotificationException notificationException, View view, int i) {
        updateMute(notificationException, view, i, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(int i) {
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(View view, int i) {
        if (!(view instanceof TextColorCell)) {
            updateRows(true);
            return;
        }
        if (i >= 0 && i < this.items.size()) {
            ((ItemInner) this.items.get(i)).color = getLedColor();
        }
        ((TextColorCell) view).setTextAndColor(LocaleController.getString("LedColor", R.string.LedColor), getLedColor(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(View view, int i) {
        if (!(view instanceof TextSettingsCell)) {
            updateRows(true);
            return;
        }
        if (i >= 0 && i < this.items.size()) {
            ((ItemInner) this.items.get(i)).text2 = getPopupOption();
        }
        TextSettingsCell textSettingsCell = (TextSettingsCell) view;
        textSettingsCell.setTextAndValue(LocaleController.getString("PopupNotification", R.string.PopupNotification), getPopupOption(), true, textSettingsCell.needDivider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13(View view, String str, int i) {
        if (!(view instanceof TextSettingsCell)) {
            updateRows(true);
            return;
        }
        String string = LocaleController.getString(this.vibrateLabels[Utilities.clamp(getNotificationsSettings().getInt(str, 0), this.vibrateLabels.length - 1, 0)]);
        if (i >= 0 && i < this.items.size()) {
            ((ItemInner) this.items.get(i)).text2 = string;
        }
        ((TextSettingsCell) view).setTextAndValue(LocaleController.getString("Vibrate", R.string.Vibrate), string, true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$14(View view, int i) {
        if (!(view instanceof TextSettingsCell)) {
            updateRows(true);
            return;
        }
        if (i >= 0 && i < this.items.size()) {
            ((ItemInner) this.items.get(i)).text2 = getPriorityOption();
        }
        TextSettingsCell textSettingsCell = (TextSettingsCell) view;
        textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", R.string.NotificationsImportance), getPriorityOption(), true, textSettingsCell.needDivider);
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
    /* JADX WARN: Removed duplicated region for block: B:190:0x0397  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x05a9 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:280:0x05aa  */
    /* JADX WARN: Removed duplicated region for block: B:294:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0114 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0115  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$17(Context context, final View view, final int i, float f, float f2) {
        final NotificationsSettingsActivity.NotificationException notificationException;
        final ArrayList arrayList;
        final boolean z;
        NotificationsSettingsActivity.NotificationException notificationException2;
        boolean z2;
        Dialog create;
        String str;
        boolean z3;
        final NotificationsSettingsActivity.NotificationException notificationException3;
        final boolean z4;
        final NotificationsSettingsActivity.NotificationException notificationException4;
        if (getParentActivity() == null) {
            return;
        }
        ItemInner itemInner = (this.listView.getAdapter() != this.adapter || i < 0 || i >= this.items.size()) ? null : (ItemInner) this.items.get(i);
        if (itemInner != null && itemInner.viewType == 8) {
            this.expanded = !this.expanded;
            updateRows(true);
            return;
        }
        int i2 = this.currentType;
        if (i2 == 3 && itemInner != null && (notificationException4 = itemInner.exception) != null) {
            ItemOptions.makeOptions(this, view).setGravity(3).addIf(notificationException4.notify <= 0 || notificationException4.auto, R.drawable.msg_mute, (CharSequence) LocaleController.getString(R.string.NotificationsStoryMute), false, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.this.lambda$createView$1(notificationException4, view, i);
                }
            }).addIf(notificationException4.notify > 0 || notificationException4.auto, R.drawable.msg_unmute, (CharSequence) LocaleController.getString(R.string.NotificationsStoryUnmute), false, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsCustomSettingsActivity.this.lambda$createView$2(notificationException4, view, i);
                }
            }).addIf(!notificationException4.auto, R.drawable.msg_delete, (CharSequence) LocaleController.getString("DeleteException", R.string.DeleteException), true, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda13
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
                if (!(object instanceof NotificationsSettingsActivity.NotificationException)) {
                    boolean z5 = object instanceof TLRPC.User;
                    long j = z5 ? ((TLRPC.User) object).id : -((TLRPC.Chat) object).id;
                    if (!this.exceptionsDict.containsKey(Long.valueOf(j))) {
                        NotificationsSettingsActivity.NotificationException notificationException5 = new NotificationsSettingsActivity.NotificationException();
                        notificationException5.story = true;
                        notificationException5.did = j;
                        notificationException5.did = z5 ? ((TLRPC.User) object).id : -((TLRPC.Chat) object).id;
                        notificationException3 = notificationException5;
                        z4 = true;
                        if (notificationException3 != null) {
                            return;
                        }
                        ItemOptions.makeOptions(this, view).setGravity(3).addIf(notificationException3.notify <= 0 || notificationException3.auto, R.drawable.msg_mute, (CharSequence) LocaleController.getString(R.string.NotificationsStoryMute), false, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda14
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$createView$4(notificationException3, view, z4);
                            }
                        }).addIf(notificationException3.notify > 0 || notificationException3.auto, R.drawable.msg_unmute, (CharSequence) LocaleController.getString(R.string.NotificationsStoryUnmute), false, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda15
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$createView$5(notificationException3, view, z4);
                            }
                        }).addIf((z4 || notificationException3.auto) ? false : true, R.drawable.msg_delete, (CharSequence) LocaleController.getString("DeleteException", R.string.DeleteException), true, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda16
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$createView$6(notificationException3, view, i);
                            }
                        }).show();
                        return;
                    }
                    object = this.exceptionsDict.get(Long.valueOf(j));
                }
                notificationException3 = (NotificationsSettingsActivity.NotificationException) object;
                z4 = false;
                if (notificationException3 != null) {
                }
            }
        }
        if (this.listView.getAdapter() != this.searchAdapter && (itemInner == null || itemInner.exception == null)) {
            if (itemInner == null) {
                return;
            }
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
                dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda17
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList2, CharSequence charSequence, boolean z6, boolean z7, int i5, TopicsFragment topicsFragment) {
                        boolean lambda$createView$8;
                        lambda$createView$8 = NotificationsCustomSettingsActivity.this.lambda$createView$8(dialogsActivity2, arrayList2, charSequence, z6, z7, i5, topicsFragment);
                        return lambda$createView$8;
                    }
                });
                presentFragment(dialogsActivity);
                return;
            } else if (i3 == 7) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("NotificationsDeleteAllExceptionTitle", R.string.NotificationsDeleteAllExceptionTitle));
                builder.setMessage(LocaleController.getString("NotificationsDeleteAllExceptionAlert", R.string.NotificationsDeleteAllExceptionAlert));
                builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda18
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        NotificationsCustomSettingsActivity.this.lambda$createView$9(dialogInterface, i5);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog create2 = builder.create();
                showDialog(create2);
                TextView textView = (TextView) create2.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                    return;
                }
                return;
            } else if (i3 == 100 || i3 == 101) {
                boolean isGlobalNotificationsEnabled = getNotificationsController().isGlobalNotificationsEnabled(this.currentType);
                TextCheckCell textCheckCell = (TextCheckCell) view;
                this.listView.findViewHolderForAdapterPosition(i);
                int i5 = this.currentType;
                if (i5 != 3) {
                    if (isGlobalNotificationsEnabled) {
                        AlertsCreator.showCustomNotificationsDialog(this, 0L, 0, i5, this.exceptions, this.autoExceptions, this.currentAccount, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda11
                            @Override // org.telegram.messenger.MessagesStorage.IntCallback
                            public final void run(int i6) {
                                NotificationsCustomSettingsActivity.this.lambda$createView$10(i6);
                            }
                        });
                        return;
                    }
                    getNotificationsController().setGlobalNotificationsEnabled(this.currentType, 0);
                    updateRows(true);
                    return;
                }
                SharedPreferences.Editor edit = getNotificationsSettings().edit();
                Boolean bool = this.storiesEnabled;
                boolean z6 = bool != null && bool.booleanValue();
                if (this.storiesAuto && z6) {
                    edit.remove("EnableAllStories");
                    this.storiesEnabled = null;
                } else {
                    boolean z7 = !z6;
                    edit.putBoolean("EnableAllStories", z7);
                    this.storiesEnabled = Boolean.valueOf(z7);
                }
                edit.apply();
                getNotificationsController().updateServerNotificationsSettings(this.currentType);
                updateRows(true);
                if (this.showAutoExceptions != (this.storiesEnabled == null)) {
                    toggleShowAutoExceptions();
                }
                checkRowsEnabled();
                return;
            } else if (i3 == 3) {
                if (view.isEnabled()) {
                    try {
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("type", this.currentType);
                        presentFragment(new NotificationsSoundActivity(bundle2, getResourceProvider()));
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        return;
                    }
                }
                return;
            } else {
                if (itemInner.viewType == 3) {
                    if (!view.isEnabled()) {
                        return;
                    }
                    create = AlertsCreator.createColorSelectDialog(getParentActivity(), 0L, 0, this.currentType, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda19
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsCustomSettingsActivity.this.lambda$createView$11(view, i);
                        }
                    });
                } else if (i3 == 2) {
                    if (!view.isEnabled()) {
                        return;
                    }
                    create = AlertsCreator.createPopupSelectDialog(getParentActivity(), this.currentType, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda20
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsCustomSettingsActivity.this.lambda$createView$12(view, i);
                        }
                    });
                } else if (i3 == 1) {
                    if (!view.isEnabled()) {
                        return;
                    }
                    int i6 = this.currentType;
                    final String str2 = i6 == 1 ? "vibrate_messages" : i6 == 0 ? "vibrate_group" : i6 == 3 ? "vibrate_stories" : (i6 == 4 || i6 == 5) ? "vibrate_react" : "vibrate_channel";
                    create = AlertsCreator.createVibrationSelectDialog(getParentActivity(), 0L, 0L, str2, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsCustomSettingsActivity.this.lambda$createView$13(view, str2, i);
                        }
                    });
                } else if (i3 == 4) {
                    if (!view.isEnabled()) {
                        return;
                    }
                    create = AlertsCreator.createPrioritySelectDialog(getParentActivity(), 0L, 0, this.currentType, new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsCustomSettingsActivity.this.lambda$createView$14(view, i);
                        }
                    });
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
                        return;
                    }
                    return;
                } else if (i3 == 0) {
                    if (!view.isEnabled()) {
                        return;
                    }
                    SharedPreferences notificationsSettings2 = getNotificationsSettings();
                    SharedPreferences.Editor edit3 = notificationsSettings2.edit();
                    int i7 = this.currentType;
                    if (i7 == 1) {
                        str = "EnablePreviewAll";
                    } else if (i7 == 0) {
                        str = "EnablePreviewGroup";
                    } else if (i7 == 3) {
                        z3 = !notificationsSettings2.getBoolean("EnableHideStoriesSenders", false);
                        edit3.putBoolean("EnableHideStoriesSenders", z3);
                        edit3.commit();
                        getNotificationsController().updateServerNotificationsSettings(this.currentType);
                        if (view instanceof TextCheckCell) {
                            return;
                        }
                        ((TextCheckCell) view).setChecked(!z3);
                        return;
                    } else {
                        str = (i7 == 4 || i7 == 5) ? "EnableReactionsPreview" : "EnablePreviewChannel";
                    }
                    z3 = notificationsSettings2.getBoolean(str, true);
                    edit3.putBoolean(str, !z3);
                    edit3.commit();
                    getNotificationsController().updateServerNotificationsSettings(this.currentType);
                    if (view instanceof TextCheckCell) {
                    }
                } else if (i3 != 103 && i3 != 104) {
                    return;
                } else {
                    boolean z8 = !LocaleController.isRTL ? f <= ((float) (view.getMeasuredWidth() - AndroidUtilities.dp(76.0f))) : f >= ((float) AndroidUtilities.dp(76.0f));
                    final SharedPreferences notificationsSettings3 = getNotificationsSettings();
                    if (z8) {
                        String str3 = itemInner.id == 103 ? "EnableReactionsMessages" : "EnableReactionsStories";
                        SharedPreferences.Editor edit4 = notificationsSettings3.edit();
                        edit4.putBoolean(str3, !notificationsSettings3.getBoolean(str3, true));
                        edit4.apply();
                        updateRows(true);
                        getNotificationsController().updateServerNotificationsSettings(this.currentType);
                        return;
                    }
                    final String str4 = itemInner.id == 103 ? "EnableReactionsMessagesContacts" : "EnableReactionsStoriesContacts";
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(1);
                    final boolean[] zArr = {notificationsSettings3.getBoolean(str4, false)};
                    final RadioColorCell[] radioColorCellArr = new RadioColorCell[2];
                    final int i8 = 0;
                    while (i8 < 2) {
                        RadioColorCell radioColorCell = new RadioColorCell(context, getResourceProvider());
                        radioColorCellArr[i8] = radioColorCell;
                        radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                        radioColorCellArr[i8].setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                        radioColorCellArr[i8].setTextAndValue(LocaleController.getString(i8 == 0 ? R.string.NotifyAboutReactionsFromEveryone : R.string.NotifyAboutReactionsFromContacts), i8 == 0 ? !zArr[0] : zArr[0]);
                        radioColorCellArr[i8].setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                        linearLayout.addView(radioColorCellArr[i8]);
                        radioColorCellArr[i8].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda9
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                NotificationsCustomSettingsActivity.lambda$createView$15(zArr, i8, radioColorCellArr, view2);
                            }
                        });
                        i8++;
                    }
                    create = new AlertDialog.Builder(getContext(), this.resourceProvider).setTitle(LocaleController.getString(R.string.NotifyAboutReactionsFrom)).setView(linearLayout).setNegativeButton(LocaleController.getString(R.string.Cancel), null).setPositiveButton(LocaleController.getString(R.string.Save), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda10
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i9) {
                            NotificationsCustomSettingsActivity.this.lambda$createView$16(notificationsSettings3, str4, zArr, dialogInterface, i9);
                        }
                    }).create();
                }
                showDialog(create);
                return;
            }
        }
        RecyclerView.Adapter adapter2 = this.listView.getAdapter();
        SearchAdapter searchAdapter2 = this.searchAdapter;
        if (adapter2 == searchAdapter2) {
            Object object2 = searchAdapter2.getObject(i);
            if (!(object2 instanceof NotificationsSettingsActivity.NotificationException)) {
                boolean z9 = object2 instanceof TLRPC.User;
                long j2 = z9 ? ((TLRPC.User) object2).id : -((TLRPC.Chat) object2).id;
                if (this.exceptionsDict.containsKey(Long.valueOf(j2))) {
                    notificationException2 = (NotificationsSettingsActivity.NotificationException) this.exceptionsDict.get(Long.valueOf(j2));
                    z2 = false;
                } else {
                    NotificationsSettingsActivity.NotificationException notificationException6 = new NotificationsSettingsActivity.NotificationException();
                    notificationException6.did = j2;
                    notificationException6.did = z9 ? ((TLRPC.User) object2).id : -((TLRPC.Chat) object2).id;
                    notificationException2 = notificationException6;
                    z2 = true;
                }
                notificationException = notificationException2;
                z = z2;
                arrayList = this.exceptions;
                if (notificationException != null) {
                    return;
                }
                final long j3 = notificationException.did;
                final boolean isGlobalNotificationsEnabled2 = NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(j3, false, false);
                ChatNotificationsPopupWrapper chatNotificationsPopupWrapper = new ChatNotificationsPopupWrapper(context, this.currentAccount, null, true, true, new ChatNotificationsPopupWrapper.Callback() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.4
                    /* JADX INFO: Access modifiers changed from: private */
                    public void setDefault() {
                        int indexOf;
                        if (z) {
                            return;
                        }
                        if (arrayList != NotificationsCustomSettingsActivity.this.exceptions && (indexOf = NotificationsCustomSettingsActivity.this.exceptions.indexOf(notificationException)) >= 0) {
                            NotificationsCustomSettingsActivity.this.exceptions.remove(indexOf);
                            NotificationsCustomSettingsActivity.this.exceptionsDict.remove(Long.valueOf(notificationException.did));
                        }
                        arrayList.remove(notificationException);
                        if (arrayList == NotificationsCustomSettingsActivity.this.exceptions) {
                            NotificationsCustomSettingsActivity.this.updateRows(true);
                            NotificationsCustomSettingsActivity.this.checkRowsEnabled();
                        } else {
                            NotificationsCustomSettingsActivity.this.updateRows(true);
                            NotificationsCustomSettingsActivity.this.searchAdapter.notifyItemChanged(i);
                        }
                        ((BaseFragment) NotificationsCustomSettingsActivity.this).actionBar.closeSearchField();
                    }

                    private void setNotDefault() {
                        SharedPreferences notificationsSettings4 = NotificationsCustomSettingsActivity.this.getNotificationsSettings();
                        NotificationsSettingsActivity.NotificationException notificationException7 = notificationException;
                        notificationException7.hasCustom = notificationsSettings4.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + notificationException.did, false);
                        NotificationsSettingsActivity.NotificationException notificationException8 = notificationException;
                        notificationException8.notify = notificationsSettings4.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + notificationException.did, 0);
                        if (notificationException.notify != 0) {
                            int i9 = notificationsSettings4.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + notificationException.did, -1);
                            if (i9 != -1) {
                                notificationException.muteUntil = i9;
                            }
                        }
                        if (z) {
                            NotificationsCustomSettingsActivity.this.exceptions.add(notificationException);
                            NotificationsCustomSettingsActivity.this.exceptionsDict.put(Long.valueOf(notificationException.did), notificationException);
                            NotificationsCustomSettingsActivity.this.updateRows(true);
                        } else {
                            NotificationsCustomSettingsActivity.this.listView.getAdapter().notifyItemChanged(i);
                        }
                        ((BaseFragment) NotificationsCustomSettingsActivity.this).actionBar.closeSearchField();
                    }

                    private void update() {
                        if (NotificationsCustomSettingsActivity.this.getMessagesController().isDialogMuted(j3, NotificationsCustomSettingsActivity.this.topicId) != isGlobalNotificationsEnabled2) {
                            setDefault();
                        } else {
                            setNotDefault();
                        }
                    }

                    @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                    public /* synthetic */ void dismiss() {
                        ChatNotificationsPopupWrapper.Callback.-CC.$default$dismiss(this);
                    }

                    @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                    public void muteFor(int i9) {
                        NotificationsCustomSettingsActivity notificationsCustomSettingsActivity;
                        Theme.ResourcesProvider resourceProvider;
                        int i10;
                        NotificationsCustomSettingsActivity notificationsCustomSettingsActivity2 = NotificationsCustomSettingsActivity.this;
                        if (i9 == 0) {
                            if (notificationsCustomSettingsActivity2.getMessagesController().isDialogMuted(j3, NotificationsCustomSettingsActivity.this.topicId)) {
                                toggleMute();
                            }
                            if (BulletinFactory.canShowBulletin(NotificationsCustomSettingsActivity.this)) {
                                notificationsCustomSettingsActivity = NotificationsCustomSettingsActivity.this;
                                resourceProvider = notificationsCustomSettingsActivity.getResourceProvider();
                                i10 = 4;
                                BulletinFactory.createMuteBulletin(notificationsCustomSettingsActivity, i10, i9, resourceProvider).show();
                            }
                        } else {
                            notificationsCustomSettingsActivity2.getNotificationsController().muteUntil(j3, NotificationsCustomSettingsActivity.this.topicId, i9);
                            if (BulletinFactory.canShowBulletin(NotificationsCustomSettingsActivity.this)) {
                                notificationsCustomSettingsActivity = NotificationsCustomSettingsActivity.this;
                                resourceProvider = notificationsCustomSettingsActivity.getResourceProvider();
                                i10 = 5;
                                BulletinFactory.createMuteBulletin(notificationsCustomSettingsActivity, i10, i9, resourceProvider).show();
                            }
                        }
                        update();
                    }

                    @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                    public /* synthetic */ void openExceptions() {
                        ChatNotificationsPopupWrapper.Callback.-CC.$default$openExceptions(this);
                    }

                    @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                    public void showCustomize() {
                        if (j3 != 0) {
                            Bundle bundle3 = new Bundle();
                            bundle3.putLong("dialog_id", j3);
                            ProfileNotificationsActivity profileNotificationsActivity = new ProfileNotificationsActivity(bundle3);
                            profileNotificationsActivity.setDelegate(new ProfileNotificationsActivity.ProfileNotificationsActivityDelegate() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.4.1
                                @Override // org.telegram.ui.ProfileNotificationsActivity.ProfileNotificationsActivityDelegate
                                public void didCreateNewException(NotificationsSettingsActivity.NotificationException notificationException7) {
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

                    @Override // org.telegram.ui.Components.ChatNotificationsPopupWrapper.Callback
                    public void toggleSound() {
                        String sharedPrefKey = NotificationsController.getSharedPrefKey(j3, NotificationsCustomSettingsActivity.this.topicId);
                        SharedPreferences notificationsSettings4 = MessagesController.getNotificationsSettings(((BaseFragment) NotificationsCustomSettingsActivity.this).currentAccount);
                        boolean z10 = notificationsSettings4.getBoolean("sound_enabled_" + sharedPrefKey, true);
                        boolean z11 = !z10 ? 1 : 0;
                        SharedPreferences.Editor edit5 = notificationsSettings4.edit();
                        edit5.putBoolean("sound_enabled_" + sharedPrefKey, z11).apply();
                        if (BulletinFactory.canShowBulletin(NotificationsCustomSettingsActivity.this)) {
                            NotificationsCustomSettingsActivity notificationsCustomSettingsActivity = NotificationsCustomSettingsActivity.this;
                            BulletinFactory.createSoundEnabledBulletin(notificationsCustomSettingsActivity, z10 ? 1 : 0, notificationsCustomSettingsActivity.getResourceProvider()).show();
                        }
                    }
                }, getResourceProvider());
                chatNotificationsPopupWrapper.lambda$update$11(j3, this.topicId, null);
                chatNotificationsPopupWrapper.showAsOptions(this, view, f, f2);
                return;
            }
            arrayList = this.searchAdapter.searchResult;
            notificationException = (NotificationsSettingsActivity.NotificationException) object2;
        } else {
            NotificationsSettingsActivity.NotificationException notificationException7 = itemInner.exception;
            if (notificationException7.auto) {
                return;
            }
            notificationException = notificationException7;
            arrayList = this.exceptions;
        }
        z = false;
        if (notificationException != null) {
        }
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
    public /* synthetic */ void lambda$createView$7(NotificationsSettingsActivity.NotificationException notificationException) {
        this.exceptions.add(0, notificationException);
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$8(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
        int i2 = 0;
        long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        if (this.currentType == 3) {
            ArrayList arrayList2 = this.autoExceptions;
            if (arrayList2 != null) {
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    if (((NotificationsSettingsActivity.NotificationException) it.next()).did == j) {
                        it.remove();
                    }
                }
            }
            ArrayList arrayList3 = this.exceptions;
            if (arrayList3 != null) {
                Iterator it2 = arrayList3.iterator();
                while (it2.hasNext()) {
                    if (((NotificationsSettingsActivity.NotificationException) it2.next()).did == j) {
                        it2.remove();
                    }
                }
            }
            NotificationsSettingsActivity.NotificationException notificationException = new NotificationsSettingsActivity.NotificationException();
            notificationException.did = j;
            notificationException.story = true;
            Boolean bool = this.storiesEnabled;
            if (bool != null && bool.booleanValue()) {
                i2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            notificationException.notify = i2;
            if (this.exceptions == null) {
                this.exceptions = new ArrayList();
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
    public /* synthetic */ void lambda$createView$9(DialogInterface dialogInterface, int i) {
        SharedPreferences.Editor edit = getNotificationsSettings().edit();
        int size = this.exceptions.size();
        for (int i2 = 0; i2 < size; i2++) {
            NotificationsSettingsActivity.NotificationException notificationException = (NotificationsSettingsActivity.NotificationException) this.exceptions.get(i2);
            if (this.currentType == 3) {
                edit.remove(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + notificationException.did);
            } else {
                SharedPreferences.Editor remove = edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + notificationException.did);
                remove.remove(NotificationsSettingsFacade.PROPERTY_CUSTOM + notificationException.did);
            }
            getMessagesStorage().setDialogFlags(notificationException.did, 0L);
            TLRPC.Dialog dialog = (TLRPC.Dialog) getMessagesController().dialogs_dict.get(notificationException.did);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
            }
        }
        edit.commit();
        int size2 = this.exceptions.size();
        for (int i3 = 0; i3 < size2; i3++) {
            getNotificationsController().updateServerNotificationsSettings(((NotificationsSettingsActivity.NotificationException) this.exceptions.get(i3)).did, this.topicId, false);
        }
        this.exceptions.clear();
        this.exceptionsDict.clear();
        updateRows(true);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
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
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0122, code lost:
        if (r6.deleted != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0149, code lost:
        if (r8.deleted != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x029c, code lost:
        if (r8.deleted != false) goto L127;
     */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0321  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0365  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x037f A[LOOP:5: B:162:0x037d->B:163:0x037f, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0399  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadExceptions$20(ArrayList arrayList) {
        ArrayList<TLRPC.User> arrayList2;
        boolean z;
        final ArrayList<TLRPC.Chat> arrayList3;
        ArrayList<TLRPC.User> arrayList4;
        ArrayList<TLRPC.EncryptedChat> arrayList5;
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
        Long parseLong;
        long longValue;
        final ArrayList arrayList10 = new ArrayList();
        ArrayList arrayList11 = new ArrayList();
        ArrayList arrayList12 = new ArrayList();
        ArrayList arrayList13 = new ArrayList();
        ArrayList arrayList14 = new ArrayList();
        android.util.LongSparseArray longSparseArray = new android.util.LongSparseArray();
        ArrayList<Long> arrayList15 = new ArrayList<>();
        ArrayList arrayList16 = new ArrayList();
        ArrayList arrayList17 = new ArrayList();
        ArrayList<TLRPC.User> arrayList18 = new ArrayList<>();
        ArrayList<TLRPC.Chat> arrayList19 = new ArrayList<>();
        ArrayList<TLRPC.EncryptedChat> arrayList20 = new ArrayList<>();
        long j = getUserConfig().clientUserId;
        SharedPreferences notificationsSettings = getNotificationsSettings();
        Map<String, ?> all = notificationsSettings.getAll();
        Iterator<Map.Entry<String, ?>> it = all.entrySet().iterator();
        while (true) {
            arrayList2 = arrayList18;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, ?> next = it.next();
            String key = next.getKey();
            ArrayList<TLRPC.EncryptedChat> arrayList21 = arrayList20;
            if (key.startsWith(NotificationsSettingsFacade.PROPERTY_NOTIFY)) {
                ArrayList arrayList22 = arrayList13;
                String replace = key.replace(NotificationsSettingsFacade.PROPERTY_NOTIFY, "");
                Long parseLong2 = Utilities.parseLong(replace);
                ArrayList arrayList23 = arrayList11;
                ArrayList arrayList24 = arrayList12;
                long longValue2 = parseLong2.longValue();
                if (longValue2 == 0 || longValue2 == j) {
                    arrayList11 = arrayList23;
                    arrayList18 = arrayList2;
                    arrayList20 = arrayList21;
                    arrayList13 = arrayList22;
                    arrayList12 = arrayList24;
                } else {
                    NotificationsSettingsActivity.NotificationException notificationException = new NotificationsSettingsActivity.NotificationException();
                    notificationException.did = longValue2;
                    long j2 = j;
                    notificationException.hasCustom = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + longValue2, false);
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
                        TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                        if (encryptedChat == null) {
                            arrayList17.add(Integer.valueOf(encryptedChatId));
                        } else {
                            TLRPC.User user = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                            if (user == null) {
                                arrayList15.add(Long.valueOf(encryptedChat.user_id));
                                longValue2 = encryptedChat.user_id;
                            }
                        }
                        longSparseArray.put(longValue2, notificationException);
                        arrayList10.add(notificationException);
                        arrayList11 = arrayList23;
                        arrayList18 = arrayList2;
                        arrayList20 = arrayList21;
                        arrayList13 = arrayList22;
                        arrayList12 = arrayList24;
                        j = j2;
                    } else {
                        if (DialogObject.isUserDialog(longValue2)) {
                            TLRPC.User user2 = getMessagesController().getUser(parseLong2);
                            if (user2 == null) {
                                arrayList15.add(parseLong2);
                                longSparseArray.put(longValue2, notificationException);
                                arrayList10.add(notificationException);
                            }
                        } else {
                            long j3 = -longValue2;
                            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(j3));
                            if (chat == null) {
                                arrayList16.add(Long.valueOf(j3));
                                longSparseArray.put(longValue2, notificationException);
                            } else if (!chat.left && !chat.kicked && chat.migrated_to == null) {
                                if (!ChatObject.isChannel(chat) || chat.megagroup) {
                                    arrayList23.add(notificationException);
                                } else {
                                    arrayList14.add(notificationException);
                                }
                            }
                        }
                        arrayList11 = arrayList23;
                        arrayList18 = arrayList2;
                        arrayList20 = arrayList21;
                        arrayList13 = arrayList22;
                        arrayList12 = arrayList24;
                        j = j2;
                    }
                }
            } else {
                arrayList18 = arrayList2;
                arrayList20 = arrayList21;
            }
        }
        ArrayList arrayList25 = arrayList12;
        ArrayList arrayList26 = arrayList13;
        ArrayList<TLRPC.EncryptedChat> arrayList27 = arrayList20;
        long j4 = j;
        final ArrayList arrayList28 = arrayList11;
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
                    parseLong = Utilities.parseLong(key2.substring(8));
                    longValue = parseLong.longValue();
                } catch (Exception unused) {
                    arrayList9 = arrayList25;
                }
                if (longValue != 0 && longValue != j4) {
                    NotificationsSettingsActivity.NotificationException notificationException2 = new NotificationsSettingsActivity.NotificationException();
                    notificationException2.did = longValue;
                    notificationException2.story = true;
                    notificationException2.notify = ((Boolean) next2.getValue()).booleanValue() ? 0 : ConnectionsManager.DEFAULT_DATACENTER_ID;
                    if (DialogObject.isUserDialog(longValue)) {
                        TLRPC.User user3 = getMessagesController().getUser(parseLong);
                        if (user3 == null) {
                            try {
                                arrayList15.add(parseLong);
                                longSparseArray.put(longValue, notificationException2);
                            } catch (Exception unused2) {
                                arrayList9 = arrayList25;
                            }
                        } else if (user3.deleted) {
                        }
                        arrayList9 = arrayList25;
                        try {
                            arrayList9.add(notificationException2);
                            hashSet.add(parseLong);
                        } catch (Exception unused3) {
                            arrayList25 = arrayList9;
                        }
                        arrayList25 = arrayList9;
                    }
                }
            }
        }
        final ArrayList arrayList29 = arrayList25;
        if (arrayList != null) {
            Collections.sort(arrayList, Comparator$-CC.comparingDouble(new ToDoubleFunction() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda4
                @Override // j$.util.function.ToDoubleFunction
                public final double applyAsDouble(Object obj) {
                    double d;
                    d = ((TLRPC.TL_topPeer) obj).rating;
                    return d;
                }
            }));
            int max = Math.max(0, arrayList.size() - 6);
            while (max < arrayList.size()) {
                long peerDialogId = DialogObject.getPeerDialogId(((TLRPC.TL_topPeer) arrayList.get(max)).peer);
                if (!hashSet.contains(Long.valueOf(peerDialogId))) {
                    NotificationsSettingsActivity.NotificationException notificationException3 = new NotificationsSettingsActivity.NotificationException();
                    notificationException3.did = peerDialogId;
                    notificationException3.story = z;
                    notificationException3.notify = 0;
                    notificationException3.auto = z;
                    if (DialogObject.isUserDialog(peerDialogId)) {
                        TLRPC.User user4 = getMessagesController().getUser(Long.valueOf(peerDialogId));
                        if (user4 == null) {
                            arrayList15.add(Long.valueOf(peerDialogId));
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
                if (arrayList17.isEmpty()) {
                    arrayList5 = arrayList27;
                } else {
                    try {
                        arrayList5 = arrayList27;
                    } catch (Exception e) {
                        e = e;
                        arrayList5 = arrayList27;
                        arrayList3 = arrayList19;
                        arrayList4 = arrayList2;
                        FileLog.e(e);
                        size = arrayList3.size();
                        i = 0;
                        while (i < size) {
                        }
                        arrayList6 = arrayList14;
                        size2 = arrayList4.size();
                        while (i2 < size2) {
                        }
                        size3 = arrayList5.size();
                        while (i3 < size3) {
                        }
                        size4 = longSparseArray.size();
                        while (i4 < size4) {
                        }
                        final ArrayList<TLRPC.User> arrayList31 = arrayList4;
                        final ArrayList<TLRPC.EncryptedChat> arrayList32 = arrayList5;
                        final ArrayList arrayList33 = arrayList6;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList31, arrayList3, arrayList32, arrayList10, arrayList28, arrayList29, arrayList30, arrayList33);
                            }
                        });
                    }
                    try {
                        getMessagesStorage().getEncryptedChatsInternal(TextUtils.join(",", arrayList17), arrayList5, arrayList15);
                    } catch (Exception e2) {
                        e = e2;
                        arrayList3 = arrayList19;
                        arrayList4 = arrayList2;
                        FileLog.e(e);
                        size = arrayList3.size();
                        i = 0;
                        while (i < size) {
                        }
                        arrayList6 = arrayList14;
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
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList312, arrayList3, arrayList322, arrayList10, arrayList28, arrayList29, arrayList30, arrayList332);
                            }
                        });
                    }
                }
                if (arrayList15.isEmpty()) {
                    arrayList4 = arrayList2;
                } else {
                    try {
                        arrayList4 = arrayList2;
                        try {
                            getMessagesStorage().getUsersInternal(arrayList15, arrayList4);
                        } catch (Exception e3) {
                            e = e3;
                            arrayList3 = arrayList19;
                            FileLog.e(e);
                            size = arrayList3.size();
                            i = 0;
                            while (i < size) {
                            }
                            arrayList6 = arrayList14;
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
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda5
                                @Override // java.lang.Runnable
                                public final void run() {
                                    NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList3122, arrayList3, arrayList3222, arrayList10, arrayList28, arrayList29, arrayList30, arrayList3322);
                                }
                            });
                        }
                    } catch (Exception e4) {
                        e = e4;
                        arrayList4 = arrayList2;
                        arrayList3 = arrayList19;
                        FileLog.e(e);
                        size = arrayList3.size();
                        i = 0;
                        while (i < size) {
                        }
                        arrayList6 = arrayList14;
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
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList31222, arrayList3, arrayList32222, arrayList10, arrayList28, arrayList29, arrayList30, arrayList33222);
                            }
                        });
                    }
                }
                if (arrayList16.isEmpty()) {
                    arrayList3 = arrayList19;
                } else {
                    MessagesStorage messagesStorage = getMessagesStorage();
                    String join = TextUtils.join(",", arrayList16);
                    arrayList3 = arrayList19;
                    try {
                        messagesStorage.getChatsInternal(join, arrayList3);
                    } catch (Exception e5) {
                        e = e5;
                        FileLog.e(e);
                        size = arrayList3.size();
                        i = 0;
                        while (i < size) {
                        }
                        arrayList6 = arrayList14;
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
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList312222, arrayList3, arrayList322222, arrayList10, arrayList28, arrayList29, arrayList30, arrayList332222);
                            }
                        });
                    }
                }
            } catch (Exception e6) {
                e = e6;
                arrayList3 = arrayList19;
                arrayList4 = arrayList2;
                arrayList5 = arrayList27;
            }
            size = arrayList3.size();
            i = 0;
            while (i < size) {
                TLRPC.Chat chat2 = arrayList3.get(i);
                if (chat2.left || chat2.kicked || chat2.migrated_to != null) {
                    arrayList7 = arrayList14;
                } else {
                    arrayList7 = arrayList14;
                    NotificationsSettingsActivity.NotificationException notificationException4 = (NotificationsSettingsActivity.NotificationException) longSparseArray.get(-chat2.id);
                    longSparseArray.remove(-chat2.id);
                    if (notificationException4 != null) {
                        if (!ChatObject.isChannel(chat2) || chat2.megagroup) {
                            arrayList28.add(notificationException4);
                        } else {
                            arrayList7.add(notificationException4);
                        }
                    }
                }
                i++;
                arrayList14 = arrayList7;
            }
            arrayList6 = arrayList14;
            size2 = arrayList4.size();
            for (i2 = 0; i2 < size2; i2++) {
                TLRPC.User user5 = arrayList4.get(i2);
                if (!user5.deleted) {
                    longSparseArray.remove(user5.id);
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
                    arrayList10.remove(longSparseArray.valueAt(i4));
                }
            }
        } else {
            arrayList6 = arrayList14;
            arrayList3 = arrayList19;
            arrayList4 = arrayList2;
            arrayList5 = arrayList27;
        }
        final ArrayList arrayList3122222 = arrayList4;
        final ArrayList arrayList3222222 = arrayList5;
        final ArrayList arrayList3322222 = arrayList6;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$19(arrayList3122222, arrayList3, arrayList3222222, arrayList10, arrayList28, arrayList29, arrayList30, arrayList3322222);
            }
        });
    }

    private void loadExceptions() {
        final ArrayList arrayList;
        if (this.currentType == 3) {
            MediaDataController.getInstance(this.currentAccount).loadHints(true);
            arrayList = new ArrayList(MediaDataController.getInstance(this.currentAccount).hints);
        } else {
            arrayList = null;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsCustomSettingsActivity.this.lambda$loadExceptions$20(arrayList);
            }
        });
    }

    private void updateMute(NotificationsSettingsActivity.NotificationException notificationException, View view, int i, boolean z, boolean z2) {
        StringBuilder sb;
        String sharedPrefKey = NotificationsController.getSharedPrefKey(notificationException.did, 0L);
        SharedPreferences.Editor edit = getNotificationsSettings().edit();
        boolean isTop5Peer = isTop5Peer(this.currentAccount, notificationException.did);
        notificationException.notify = z2 ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0;
        if (notificationException.auto) {
            notificationException.auto = false;
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey, !z2).commit();
            ArrayList arrayList = this.autoExceptions;
            if (arrayList != null) {
                arrayList.remove(notificationException);
            }
            if (this.exceptions == null) {
                this.exceptions = new ArrayList();
            }
            this.exceptions.add(0, notificationException);
        } else {
            if (isTop5Peer) {
                sb = new StringBuilder();
            } else {
                Boolean bool = this.storiesEnabled;
                if (!z2 ? bool == null || !bool.booleanValue() : bool != null && bool.booleanValue()) {
                    lambda$createView$6(notificationException, view, i);
                    return;
                }
                sb = new StringBuilder();
            }
            sb.append(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY);
            sb.append(sharedPrefKey);
            edit.putBoolean(sb.toString(), !z2).commit();
        }
        if (view instanceof UserCell) {
            UserCell userCell = (UserCell) view;
            userCell.setException(notificationException, null, userCell.needDivider);
        }
        getNotificationsController().updateServerNotificationsSettings(notificationException.did, 0L, false);
        updateRows(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0219  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x023e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateRows(boolean z) {
        ArrayList arrayList;
        ArrayList arrayList2;
        ItemInner asShadow;
        ArrayList arrayList3;
        String string;
        boolean z2;
        String str;
        boolean z3;
        ArrayList arrayList4;
        ItemInner asExpand;
        String str2;
        int i;
        int i2;
        Boolean bool;
        this.oldItems.clear();
        this.oldItems.addAll(this.items);
        this.items.clear();
        SharedPreferences notificationsSettings = getNotificationsSettings();
        if (this.currentType != -1) {
            this.items.add(ItemInner.asHeader(LocaleController.getString(R.string.NotifyMeAbout)));
            int i3 = this.currentType;
            if (i3 == 3) {
                this.items.add(ItemInner.asCheck(101, LocaleController.getString(R.string.NotifyMeAboutNewStories), notificationsSettings.getBoolean("EnableAllStories", false)));
                if (!notificationsSettings.getBoolean("EnableAllStories", false)) {
                    this.items.add(ItemInner.asCheck(102, LocaleController.getString(R.string.NotifyMeAboutImportantStories), this.storiesAuto && ((bool = this.storiesEnabled) == null || !bool.booleanValue())));
                }
                arrayList2 = this.items;
                asShadow = ItemInner.asShadow(-1, LocaleController.getString(R.string.StoryAutoExceptionsInfo));
            } else {
                if (i3 == 4 || i3 == 5) {
                    this.items.add(ItemInner.asCheck2(103, R.drawable.msg_markunread, LocaleController.getString(R.string.NotifyMeAboutMessagesReactions), LocaleController.getString(!notificationsSettings.getBoolean("EnableReactionsMessages", true) ? R.string.NotifyFromNobody : notificationsSettings.getBoolean("EnableReactionsMessagesContacts", false) ? R.string.NotifyFromContacts : R.string.NotifyFromEveryone), notificationsSettings.getBoolean("EnableReactionsMessages", true)));
                    this.items.add(ItemInner.asCheck2(104, R.drawable.msg_stories_saved, LocaleController.getString(R.string.NotifyMeAboutStoriesReactions), LocaleController.getString(!notificationsSettings.getBoolean("EnableReactionsStories", true) ? R.string.NotifyFromNobody : notificationsSettings.getBoolean("EnableReactionsStoriesContacts", false) ? R.string.NotifyFromContacts : R.string.NotifyFromEveryone), notificationsSettings.getBoolean("EnableReactionsStories", true)));
                } else {
                    this.items.add(ItemInner.asCheck(100, LocaleController.getString(i3 == 1 ? R.string.NotifyMeAboutPrivate : i3 == 0 ? R.string.NotifyMeAboutGroups : R.string.NotifyMeAboutChannels), getNotificationsController().isGlobalNotificationsEnabled(this.currentType)));
                }
                arrayList2 = this.items;
                asShadow = ItemInner.asShadow(-1, null);
            }
            arrayList2.add(asShadow);
            this.items.add(ItemInner.asHeader(LocaleController.getString(R.string.SETTINGS)));
            this.settingsStart = this.items.size() - 1;
            int i4 = this.currentType;
            if (i4 == 3) {
                arrayList3 = this.items;
                string = LocaleController.getString(R.string.NotificationShowSenderNames);
                z2 = !notificationsSettings.getBoolean("EnableHideStoriesSenders", false);
            } else if (i4 == 4 || i4 == 5) {
                arrayList3 = this.items;
                string = LocaleController.getString(R.string.NotificationShowSenderNames);
                z2 = notificationsSettings.getBoolean("EnableReactionsPreview", true);
            } else {
                if (i4 == 0) {
                    str = "EnablePreviewGroup";
                } else if (i4 == 1) {
                    str = "EnablePreviewAll";
                } else if (i4 != 2) {
                    z3 = false;
                    this.items.add(ItemInner.asCheck(0, LocaleController.getString(R.string.MessagePreview), z3));
                    this.items.add(ItemInner.asSetting(3, LocaleController.getString("Sound", R.string.Sound), getSound()));
                    if (this.expanded) {
                        this.items.add(ItemInner.asColor(LocaleController.getString("LedColor", R.string.LedColor), getLedColor()));
                        int i5 = this.currentType;
                        if (i5 == 0) {
                            str2 = "vibrate_group";
                        } else if (i5 == 1) {
                            str2 = "vibrate_messages";
                        } else if (i5 == 2) {
                            str2 = "vibrate_channel";
                        } else if (i5 == 3) {
                            str2 = "vibrate_stories";
                        } else if (i5 == 4 || i5 == 5) {
                            str2 = "vibrate_react";
                        } else {
                            i = 0;
                            ArrayList arrayList5 = this.items;
                            String string2 = LocaleController.getString("Vibrate", R.string.Vibrate);
                            int[] iArr = this.vibrateLabels;
                            arrayList5.add(ItemInner.asSetting(1, string2, LocaleController.getString(iArr[Utilities.clamp(i, iArr.length - 1, 0)])));
                            i2 = this.currentType;
                            if (i2 != 1 || i2 == 0) {
                                this.items.add(ItemInner.asSetting(2, LocaleController.getString("PopupNotification", R.string.PopupNotification), getPopupOption()));
                            }
                            if (Build.VERSION.SDK_INT >= 21) {
                                this.items.add(ItemInner.asSetting(4, LocaleController.getString("NotificationsImportance", R.string.NotificationsImportance), getPriorityOption()));
                            }
                            arrayList4 = this.items;
                            asExpand = ItemInner.asExpand(LocaleController.getString(R.string.NotifyLessOptions), false);
                        }
                        i = notificationsSettings.getInt(str2, 0);
                        ArrayList arrayList52 = this.items;
                        String string22 = LocaleController.getString("Vibrate", R.string.Vibrate);
                        int[] iArr2 = this.vibrateLabels;
                        arrayList52.add(ItemInner.asSetting(1, string22, LocaleController.getString(iArr2[Utilities.clamp(i, iArr2.length - 1, 0)])));
                        i2 = this.currentType;
                        if (i2 != 1) {
                        }
                        this.items.add(ItemInner.asSetting(2, LocaleController.getString("PopupNotification", R.string.PopupNotification), getPopupOption()));
                        if (Build.VERSION.SDK_INT >= 21) {
                        }
                        arrayList4 = this.items;
                        asExpand = ItemInner.asExpand(LocaleController.getString(R.string.NotifyLessOptions), false);
                    } else {
                        arrayList4 = this.items;
                        asExpand = ItemInner.asExpand(LocaleController.getString(R.string.NotifyMoreOptions), true);
                    }
                    arrayList4.add(asExpand);
                    this.settingsEnd = this.items.size() - 1;
                    this.items.add(ItemInner.asShadow(-2, null));
                } else {
                    str = "EnablePreviewChannel";
                }
                z3 = notificationsSettings.getBoolean(str, true);
                this.items.add(ItemInner.asCheck(0, LocaleController.getString(R.string.MessagePreview), z3));
                this.items.add(ItemInner.asSetting(3, LocaleController.getString("Sound", R.string.Sound), getSound()));
                if (this.expanded) {
                }
                arrayList4.add(asExpand);
                this.settingsEnd = this.items.size() - 1;
                this.items.add(ItemInner.asShadow(-2, null));
            }
            arrayList3.add(ItemInner.asCheck(0, string, z2));
            this.items.add(ItemInner.asSetting(3, LocaleController.getString("Sound", R.string.Sound), getSound()));
            if (this.expanded) {
            }
            arrayList4.add(asExpand);
            this.settingsEnd = this.items.size() - 1;
            this.items.add(ItemInner.asShadow(-2, null));
        }
        int i6 = this.currentType;
        if (i6 == 4 || i6 == 5) {
            this.exceptionsStart = -1;
            this.exceptionsEnd = -1;
        } else {
            if (i6 != -1) {
                this.items.add(ItemInner.asButton(6, R.drawable.msg_contact_add, LocaleController.getString("NotificationsAddAnException", R.string.NotificationsAddAnException)));
            }
            this.exceptionsStart = this.items.size() - 1;
            if (this.autoExceptions != null && this.showAutoExceptions) {
                for (int i7 = 0; i7 < this.autoExceptions.size(); i7++) {
                    this.items.add(ItemInner.asException((NotificationsSettingsActivity.NotificationException) this.autoExceptions.get(i7)));
                }
            }
            if (this.exceptions != null) {
                for (int i8 = 0; i8 < this.exceptions.size(); i8++) {
                    this.items.add(ItemInner.asException((NotificationsSettingsActivity.NotificationException) this.exceptions.get(i8)));
                }
            }
            this.exceptionsEnd = this.items.size() - 1;
            if (this.currentType != -1 || ((arrayList = this.exceptions) != null && !arrayList.isEmpty())) {
                this.items.add(ItemInner.asShadow(-3, null));
            }
            ArrayList arrayList6 = this.exceptions;
            if (arrayList6 != null && !arrayList6.isEmpty()) {
                this.items.add(ItemInner.asButton(7, 0, LocaleController.getString("NotificationsDeleteAllException", R.string.NotificationsDeleteAllException)));
            }
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
    public View createView(final Context context) {
        ActionBar actionBar;
        int i;
        String str;
        this.searching = false;
        this.searchWas = false;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == -1) {
            actionBar = this.actionBar;
            i = R.string.NotificationsExceptions;
            str = "NotificationsExceptions";
        } else {
            actionBar = this.actionBar;
            i = R.string.Notifications;
            str = "Notifications";
        }
        actionBar.setTitle(LocaleController.getString(str, i));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i2) {
                if (i2 == -1) {
                    NotificationsCustomSettingsActivity.this.finishFragment();
                }
            }
        });
        ArrayList arrayList = this.exceptions;
        if (arrayList != null && !arrayList.isEmpty()) {
            this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity.2
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
                public void onSearchExpand() {
                    NotificationsCustomSettingsActivity.this.searching = true;
                    NotificationsCustomSettingsActivity.this.emptyView.setShowAtCenter(true);
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
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView;
        emptyTextProgressView.setTextSize(18);
        this.emptyView.setText(LocaleController.getString("NoExceptions", R.string.NoExceptions));
        this.emptyView.showTextView();
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
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
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i2) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i2, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i2, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i2, float f, float f2) {
                NotificationsCustomSettingsActivity.this.lambda$createView$17(context, view, i2, f, f2);
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
            public void onScrollStateChanged(RecyclerView recyclerView, int i2) {
                if (i2 == 1) {
                    AndroidUtilities.hideKeyboard(NotificationsCustomSettingsActivity.this.getParentActivity().getCurrentFocus());
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i2, int i3) {
                super.onScrolled(recyclerView, i2, i3);
            }
        });
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i != NotificationCenter.notificationsSettingsUpdated) {
            if (i == NotificationCenter.reloadHints) {
                loadExceptions();
                return;
            }
            return;
        }
        ListAdapter listAdapter = this.adapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.NotificationsCustomSettingsActivity$$ExternalSyntheticLambda1
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

    /* JADX WARN: Code restructure failed: missing block: B:18:0x004a, code lost:
        if (r6 != null) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x004c, code lost:
        r7.putString(r2, r0);
        r7.putString(r1, r6.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0066, code lost:
        if (r6 != null) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0072, code lost:
        if (r6 != null) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x007e, code lost:
        if (r6 != null) goto L16;
     */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        String str;
        String str2;
        String str3;
        Ringtone ringtone;
        if (i2 == -1) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (uri == null || (ringtone = RingtoneManager.getRingtone(getParentActivity(), uri)) == null) {
                str = null;
            } else {
                str = uri.equals(Settings.System.DEFAULT_NOTIFICATION_URI) ? LocaleController.getString("SoundDefault", R.string.SoundDefault) : ringtone.getTitle(getParentActivity());
                ringtone.stop();
            }
            SharedPreferences.Editor edit = getNotificationsSettings().edit();
            int i3 = this.currentType;
            if (i3 == 1) {
                str2 = "GlobalSoundPath";
                str3 = "GlobalSound";
                if (str != null) {
                }
                edit.putString(str3, "NoSound");
                edit.putString(str2, "NoSound");
            } else if (i3 == 0) {
                str2 = "GroupSoundPath";
                str3 = "GroupSound";
                if (str != null) {
                }
                edit.putString(str3, "NoSound");
                edit.putString(str2, "NoSound");
            } else if (i3 == 2) {
                str2 = "ChannelSoundPath";
                str3 = "ChannelSound";
                if (str != null) {
                }
                edit.putString(str3, "NoSound");
                edit.putString(str2, "NoSound");
            } else if (i3 == 3) {
                str2 = "StoriesSoundPath";
                str3 = "StoriesSound";
                if (str != null) {
                }
                edit.putString(str3, "NoSound");
                edit.putString(str2, "NoSound");
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
    public void onBecomeFullyVisible() {
        super.onBecomeFullyVisible();
        updateRows(true);
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

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        getNotificationCenter().removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
        getNotificationCenter().removeObserver(this, NotificationCenter.reloadHints);
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

    public void toggleShowAutoExceptions() {
        if (this.listView == null || this.adapter == null) {
            return;
        }
        this.showAutoExceptions = !this.showAutoExceptions;
        updateRows(true);
    }
}
