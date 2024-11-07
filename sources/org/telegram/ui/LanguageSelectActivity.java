package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import j$.util.Collection$-EL;
import j$.util.function.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.TranslateController;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextRadioCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.TranslateAlert2;

/* loaded from: classes4.dex */
public class LanguageSelectActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private EmptyTextProgressView emptyView;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private ActionBarMenuItem searchItem;
    private ListAdapter searchListViewAdapter;
    private ArrayList searchResult;
    private boolean searchWas;
    private boolean searching;
    private ArrayList sortedLanguages;
    private int translateSettingsBackgroundHeight;
    private ArrayList unofficialLanguages;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private boolean search;

        public ListAdapter(Context context, boolean z) {
            this.mContext = context;
            this.search = z;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (this.search) {
                if (LanguageSelectActivity.this.searchResult == null) {
                    return 0;
                }
                return LanguageSelectActivity.this.searchResult.size();
            }
            int size = LanguageSelectActivity.this.sortedLanguages.size();
            if (size != 0) {
                size++;
            }
            if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty()) {
                size += LanguageSelectActivity.this.unofficialLanguages.size() + 1;
            }
            return (!LanguageSelectActivity.this.getMessagesController().premiumFeaturesBlocked() ? 1 : 0) + 4 + ((LanguageSelectActivity.this.getChatValue() || LanguageSelectActivity.this.getContextValue()) ? 1 : 0) + 1 + size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (this.search) {
                return 0;
            }
            int i2 = i - 1;
            if (i == 0) {
                return 3;
            }
            int i3 = i - 2;
            if (i2 == 0) {
                return 2;
            }
            if (!LanguageSelectActivity.this.getMessagesController().premiumFeaturesBlocked()) {
                int i4 = i - 3;
                if (i3 == 0) {
                    return 2;
                }
                i3 = i4;
            }
            if (LanguageSelectActivity.this.getChatValue() || LanguageSelectActivity.this.getContextValue()) {
                int i5 = i3 - 1;
                if (i3 == 0) {
                    return 4;
                }
                i3 = i5;
            }
            int i6 = i3 - 1;
            if (i3 == 0) {
                return 5;
            }
            int i7 = i3 - 2;
            if (i6 == 0) {
                return 5;
            }
            int i8 = i3 - 3;
            if (i7 == 0) {
                return 3;
            }
            return ((LanguageSelectActivity.this.unofficialLanguages.isEmpty() || !(i8 == LanguageSelectActivity.this.unofficialLanguages.size() || i8 == (LanguageSelectActivity.this.unofficialLanguages.size() + LanguageSelectActivity.this.sortedLanguages.size()) + 1)) && !(LanguageSelectActivity.this.unofficialLanguages.isEmpty() && i8 == LanguageSelectActivity.this.sortedLanguages.size())) ? 0 : 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 4 || itemViewType == 2;
        }

        /* JADX WARN: Code restructure failed: missing block: B:110:0x023a, code lost:
        
            if (r13 == (r11.this$0.searchResult.size() - 1)) goto L116;
         */
        /* JADX WARN: Code restructure failed: missing block: B:111:0x02b5, code lost:
        
            r13 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:124:0x02b3, code lost:
        
            r13 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:131:0x0270, code lost:
        
            if (r13 == (r11.this$0.unofficialLanguages.size() - 1)) goto L116;
         */
        /* JADX WARN: Code restructure failed: missing block: B:140:0x02b1, code lost:
        
            if (r13 == (r11.this$0.sortedLanguages.size() - 1)) goto L116;
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x0122, code lost:
        
            if (r12.getValueTextView().getPaint().measureText(r3) > java.lang.Math.min((org.telegram.messenger.AndroidUtilities.displaySize.x - org.telegram.messenger.AndroidUtilities.dp(34.0f)) / 2.0f, (org.telegram.messenger.AndroidUtilities.displaySize.x - org.telegram.messenger.AndroidUtilities.dp(84.0f)) - r12.getTextView().getPaint().measureText(r0))) goto L48;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
            Context context;
            int i2;
            String sb;
            int i3 = 0;
            int itemViewType = viewHolder.getItemViewType();
            LocaleController.LocaleInfo localeInfo = null;
            r4 = null;
            CharSequence charSequence = null;
            localeInfo = null;
            localeInfo = null;
            localeInfo = null;
            if (itemViewType == 0) {
                if (!this.search) {
                    i -= (7 - ((LanguageSelectActivity.this.getChatValue() || LanguageSelectActivity.this.getContextValue()) ? 0 : 1)) - (LanguageSelectActivity.this.getMessagesController().premiumFeaturesBlocked() ? 1 : 0);
                }
                TextRadioCell textRadioCell = (TextRadioCell) viewHolder.itemView;
                textRadioCell.updateRTL();
                if (this.search) {
                    if (i >= 0 && i < LanguageSelectActivity.this.searchResult.size()) {
                        localeInfo = (LocaleController.LocaleInfo) LanguageSelectActivity.this.searchResult.get(i);
                    }
                } else if (LanguageSelectActivity.this.unofficialLanguages.isEmpty() || i < 0 || i >= LanguageSelectActivity.this.unofficialLanguages.size()) {
                    if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty()) {
                        i -= LanguageSelectActivity.this.unofficialLanguages.size() + 1;
                    }
                    if (i >= 0 && i < LanguageSelectActivity.this.sortedLanguages.size()) {
                        localeInfo = (LocaleController.LocaleInfo) LanguageSelectActivity.this.sortedLanguages.get(i);
                    }
                } else {
                    localeInfo = (LocaleController.LocaleInfo) LanguageSelectActivity.this.unofficialLanguages.get(i);
                }
                if (localeInfo != null) {
                    textRadioCell.setTextAndValueAndCheck(localeInfo.isLocal() ? String.format("%1$s (%2$s)", localeInfo.name, LocaleController.getString(R.string.LanguageCustom)) : localeInfo.name, localeInfo.nameEnglish, false, false, !z);
                }
                textRadioCell.setChecked(localeInfo == LocaleController.getInstance().getCurrentLocaleInfo());
                return;
            }
            if (itemViewType == 1) {
                if (!this.search) {
                    i--;
                }
                ShadowSectionCell shadowSectionCell = (ShadowSectionCell) viewHolder.itemView;
                if (LanguageSelectActivity.this.unofficialLanguages.isEmpty() || i != LanguageSelectActivity.this.unofficialLanguages.size()) {
                    context = this.mContext;
                    i2 = R.drawable.greydivider_bottom;
                } else {
                    context = this.mContext;
                    i2 = R.drawable.greydivider;
                }
                shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i2, Theme.key_windowBackgroundGrayShadow));
                return;
            }
            if (itemViewType == 2) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                textCheckCell.updateRTL();
                if (i == 1) {
                    textCheckCell.setTextAndCheck(LocaleController.getString(R.string.ShowTranslateButton), LanguageSelectActivity.this.getContextValue(), true);
                } else {
                    if (i != 2) {
                        return;
                    }
                    String string = LocaleController.getString(R.string.ShowTranslateChatButton);
                    boolean chatValue = LanguageSelectActivity.this.getChatValue();
                    if (!LanguageSelectActivity.this.getContextValue() && !LanguageSelectActivity.this.getChatValue()) {
                        r1 = false;
                    }
                    textCheckCell.setTextAndCheck(string, chatValue, r1);
                    if (!LanguageSelectActivity.this.getUserConfig().isPremium()) {
                        i3 = R.drawable.permission_locked;
                    }
                }
                textCheckCell.setCheckBoxIcon(i3);
                return;
            }
            int i4 = 3;
            if (itemViewType == 3) {
                ((HeaderCell) viewHolder.itemView).setText(LocaleController.getString(i == 0 ? R.string.TranslateMessages : R.string.Language));
                return;
            }
            if (itemViewType != 4) {
                if (itemViewType != 5) {
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                textInfoPrivacyCell.updateRTL();
                if (!LanguageSelectActivity.this.getMessagesController().premiumFeaturesBlocked() && (LanguageSelectActivity.this.getContextValue() || LanguageSelectActivity.this.getChatValue())) {
                    i4 = 4;
                }
                if (i == i4) {
                    textInfoPrivacyCell.setText(LocaleController.getString(R.string.TranslateMessagesInfo1));
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    textInfoPrivacyCell.setTopPadding(11);
                    textInfoPrivacyCell.setBottomPadding(16);
                    return;
                }
                textInfoPrivacyCell.setTopPadding(0);
                textInfoPrivacyCell.setBottomPadding(16);
                textInfoPrivacyCell.setText(LocaleController.getString(R.string.TranslateMessagesInfo2));
                textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_top, Theme.key_windowBackgroundGrayShadow));
                return;
            }
            TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
            textSettingsCell.updateRTL();
            HashSet restrictedLanguages = RestrictedLanguagesSelectActivity.getRestrictedLanguages();
            String string2 = LocaleController.getString(R.string.DoNotTranslate);
            try {
                boolean[] zArr = new boolean[1];
                if (restrictedLanguages.size() == 0) {
                    sb = "";
                } else if (restrictedLanguages.size() == 1) {
                    sb = TranslateAlert2.capitalFirst(TranslateAlert2.languageName((String) restrictedLanguages.iterator().next(), zArr));
                } else {
                    Iterator it = restrictedLanguages.iterator();
                    StringBuilder sb2 = new StringBuilder();
                    boolean z2 = true;
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        if (!z2) {
                            sb2.append(", ");
                        }
                        String capitalFirst = TranslateAlert2.capitalFirst(TranslateAlert2.languageName(str, zArr));
                        if (capitalFirst != null) {
                            sb2.append(capitalFirst);
                            z2 = false;
                        }
                    }
                    sb = sb2.toString();
                }
                charSequence = sb;
            } catch (Exception unused) {
            }
            if (charSequence == null) {
                charSequence = String.format(LocaleController.getPluralString("Languages", restrictedLanguages.size()), Integer.valueOf(restrictedLanguages.size()));
            }
            textSettingsCell.setTextAndValue(string2, charSequence, true, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textRadioCell;
            if (i != 0) {
                if (i == 2) {
                    textRadioCell = new TextCheckCell(this.mContext);
                    textRadioCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                } else if (i == 3) {
                    textRadioCell = new HeaderCell(this.mContext);
                } else if (i != 4) {
                    textRadioCell = i != 5 ? new ShadowSectionCell(this.mContext) : new TextInfoPrivacyCell(this.mContext);
                } else {
                    textRadioCell = new TextSettingsCell(this.mContext);
                }
                return new RecyclerListView.Holder(textRadioCell);
            }
            textRadioCell = new TextRadioCell(this.mContext);
            textRadioCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            return new RecyclerListView.Holder(textRadioCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof TextRadioCell) {
                ((TextRadioCell) view).updateRTL();
            }
        }
    }

    private void fillLanguages() {
        final LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
        Comparator comparator = new Comparator() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$fillLanguages$8;
                lambda$fillLanguages$8 = LanguageSelectActivity.lambda$fillLanguages$8(LocaleController.LocaleInfo.this, (LocaleController.LocaleInfo) obj, (LocaleController.LocaleInfo) obj2);
                return lambda$fillLanguages$8;
            }
        };
        this.sortedLanguages = new ArrayList();
        this.unofficialLanguages = new ArrayList(LocaleController.getInstance().unofficialLanguages);
        ArrayList<LocaleController.LocaleInfo> arrayList = LocaleController.getInstance().languages;
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            LocaleController.LocaleInfo localeInfo = arrayList.get(i);
            (localeInfo.serverIndex != Integer.MAX_VALUE ? this.sortedLanguages : this.unofficialLanguages).add(localeInfo);
        }
        Collections.sort(this.sortedLanguages, comparator);
        Collections.sort(this.unofficialLanguages, comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getChatValue() {
        return getMessagesController().getTranslateController().isFeatureAvailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getContextValue() {
        return getMessagesController().getTranslateController().isContextTranslateEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0() {
        this.actionBar.closeSearchField();
        updateLanguage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(AlertDialog alertDialog, boolean z) {
        alertDialog.dismiss();
        if (z) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$createView$0();
            }
        }, 10L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(int i, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$3(String str, String str2) {
        return str2 != null && str2.equals(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x001c A[Catch: Exception -> 0x0015, TryCatch #0 {Exception -> 0x0015, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x000c, B:12:0x001c, B:13:0x007a, B:15:0x0080, B:20:0x008c, B:21:0x0098, B:23:0x00a0, B:25:0x00ae, B:27:0x00b2, B:29:0x00b5, B:34:0x00ba, B:36:0x00bf, B:39:0x00c6, B:44:0x0041, B:46:0x0048, B:48:0x0052, B:50:0x005d, B:52:0x00cd, B:54:0x00d1, B:56:0x00da, B:58:0x00e0, B:60:0x00e4, B:64:0x00ea, B:68:0x00f9, B:70:0x00ff, B:73:0x0108, B:76:0x0116, B:77:0x0118, B:78:0x0145, B:80:0x0149, B:83:0x0154, B:85:0x0160, B:86:0x0165, B:88:0x017b, B:89:0x0183, B:91:0x0196, B:93:0x019c, B:95:0x01a6, B:97:0x01ae, B:98:0x01b1, B:101:0x011d, B:104:0x0127, B:106:0x012f, B:107:0x0132, B:109:0x013a, B:110:0x0142), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x008c A[Catch: Exception -> 0x0015, TryCatch #0 {Exception -> 0x0015, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x000c, B:12:0x001c, B:13:0x007a, B:15:0x0080, B:20:0x008c, B:21:0x0098, B:23:0x00a0, B:25:0x00ae, B:27:0x00b2, B:29:0x00b5, B:34:0x00ba, B:36:0x00bf, B:39:0x00c6, B:44:0x0041, B:46:0x0048, B:48:0x0052, B:50:0x005d, B:52:0x00cd, B:54:0x00d1, B:56:0x00da, B:58:0x00e0, B:60:0x00e4, B:64:0x00ea, B:68:0x00f9, B:70:0x00ff, B:73:0x0108, B:76:0x0116, B:77:0x0118, B:78:0x0145, B:80:0x0149, B:83:0x0154, B:85:0x0160, B:86:0x0165, B:88:0x017b, B:89:0x0183, B:91:0x0196, B:93:0x019c, B:95:0x01a6, B:97:0x01ae, B:98:0x01b1, B:101:0x011d, B:104:0x0127, B:106:0x012f, B:107:0x0132, B:109:0x013a, B:110:0x0142), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$4(View view, int i) {
        ArrayList arrayList;
        boolean z;
        boolean z2;
        try {
            if (!(view instanceof TextCheckCell)) {
                if (view instanceof TextSettingsCell) {
                    presentFragment(new RestrictedLanguagesSelectActivity());
                    return;
                }
                if (getParentActivity() != null && this.parentLayout != null && (view instanceof TextRadioCell)) {
                    boolean z3 = this.listView.getAdapter() == this.searchListViewAdapter;
                    if (!z3) {
                        i -= (7 - ((getChatValue() || getContextValue()) ? 0 : 1)) - (getMessagesController().premiumFeaturesBlocked() ? 1 : 0);
                    }
                    if (z3) {
                        arrayList = this.searchResult;
                    } else if (this.unofficialLanguages.isEmpty() || i < 0 || i >= this.unofficialLanguages.size()) {
                        if (!this.unofficialLanguages.isEmpty()) {
                            i -= this.unofficialLanguages.size() + 1;
                        }
                        arrayList = this.sortedLanguages;
                    } else {
                        arrayList = this.unofficialLanguages;
                    }
                    LocaleController.LocaleInfo localeInfo = (LocaleController.LocaleInfo) arrayList.get(i);
                    if (localeInfo != null) {
                        LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
                        final boolean z4 = currentLocaleInfo == localeInfo;
                        final AlertDialog alertDialog = new AlertDialog(getContext(), 3);
                        if (!z4) {
                            alertDialog.showDelayed(500L);
                        }
                        final int applyLanguage = LocaleController.getInstance().applyLanguage(localeInfo, true, false, false, true, this.currentAccount, new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda6
                            @Override // java.lang.Runnable
                            public final void run() {
                                LanguageSelectActivity.this.lambda$createView$1(alertDialog, z4);
                            }
                        });
                        if (applyLanguage != 0) {
                            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda7
                                @Override // android.content.DialogInterface.OnCancelListener
                                public final void onCancel(DialogInterface dialogInterface) {
                                    LanguageSelectActivity.this.lambda$createView$2(applyLanguage, dialogInterface);
                                }
                            });
                        }
                        String str = localeInfo.pluralLangCode;
                        final String str2 = currentLocaleInfo.pluralLangCode;
                        HashSet restrictedLanguages = RestrictedLanguagesSelectActivity.getRestrictedLanguages();
                        HashSet hashSet = new HashSet(restrictedLanguages);
                        if (restrictedLanguages.contains(str2) && !restrictedLanguages.contains(str)) {
                            Collection$-EL.removeIf(hashSet, new Predicate() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda8
                                @Override // j$.util.function.Predicate
                                public /* synthetic */ Predicate and(Predicate predicate) {
                                    return Predicate.-CC.$default$and(this, predicate);
                                }

                                @Override // j$.util.function.Predicate
                                public /* synthetic */ Predicate negate() {
                                    return Predicate.-CC.$default$negate(this);
                                }

                                @Override // j$.util.function.Predicate
                                public /* synthetic */ Predicate or(Predicate predicate) {
                                    return Predicate.-CC.$default$or(this, predicate);
                                }

                                @Override // j$.util.function.Predicate
                                public final boolean test(Object obj) {
                                    boolean lambda$createView$3;
                                    lambda$createView$3 = LanguageSelectActivity.lambda$createView$3(str2, (String) obj);
                                    return lambda$createView$3;
                                }
                            });
                        }
                        if (str != null && !"null".equals(str)) {
                            hashSet.add(str);
                        }
                        RestrictedLanguagesSelectActivity.updateRestrictedLanguages(hashSet, Boolean.FALSE);
                        MessagesController.getInstance(this.currentAccount).getTranslateController().checkRestrictedLanguagesUpdate();
                        MessagesController.getInstance(this.currentAccount).getTranslateController().cleanup();
                        TranslateController.invalidateSuggestedLanguageCodes();
                        return;
                    }
                    return;
                }
                return;
            }
            if (!getContextValue() && !getChatValue()) {
                z = false;
                if (i != 1) {
                    boolean z5 = !getContextValue();
                    getMessagesController().getTranslateController().setContextTranslateEnabled(z5);
                    ((TextCheckCell) view).setChecked(z5);
                    NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateSearchSettings, new Object[0]);
                } else if (i == 2) {
                    boolean z6 = !getChatValue();
                    if (z6 && !getUserConfig().isPremium()) {
                        showDialog(new PremiumFeatureBottomSheet(this, 13, false));
                        return;
                    } else {
                        getMessagesController().getTranslateController().setChatTranslateEnabled(z6);
                        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateSearchSettings, new Object[0]);
                        ((TextCheckCell) view).setChecked(z6);
                    }
                }
                if (!getContextValue() && !getChatValue()) {
                    z2 = false;
                    if (z2 == z) {
                        int i2 = !getMessagesController().premiumFeaturesBlocked() ? 1 : 0;
                        int i3 = i2 + 1;
                        TextCheckCell textCheckCell = null;
                        for (int i4 = 0; i4 < this.listView.getChildCount(); i4++) {
                            View childAt = this.listView.getChildAt(i4);
                            if (this.listView.getChildAdapterPosition(childAt) == i3 && (childAt instanceof TextCheckCell)) {
                                textCheckCell = (TextCheckCell) childAt;
                            }
                        }
                        if (textCheckCell != null) {
                            textCheckCell.setDivider(z2);
                        }
                        if (z2) {
                            this.listAdapter.notifyItemInserted(i2 + 2);
                            return;
                        } else {
                            this.listAdapter.notifyItemRemoved(i2 + 2);
                            return;
                        }
                    }
                    return;
                }
                z2 = true;
                if (z2 == z) {
                }
            }
            z = true;
            if (i != 1) {
            }
            if (!getContextValue()) {
                z2 = false;
                if (z2 == z) {
                }
            }
            z2 = true;
            if (z2 == z) {
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(LocaleController.LocaleInfo localeInfo, DialogInterface dialogInterface, int i) {
        if (LocaleController.getInstance().deleteLanguage(localeInfo, this.currentAccount)) {
            fillLanguages();
            ArrayList arrayList = this.searchResult;
            if (arrayList != null) {
                arrayList.remove(localeInfo);
            }
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
            ListAdapter listAdapter2 = this.searchListViewAdapter;
            if (listAdapter2 != null) {
                listAdapter2.notifyDataSetChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$6(View view, int i) {
        ArrayList arrayList;
        try {
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (getParentActivity() != null && this.parentLayout != null && (view instanceof TextRadioCell)) {
            boolean z = this.listView.getAdapter() == this.searchListViewAdapter;
            if (!z) {
                i -= (7 - ((getChatValue() || getContextValue()) ? 0 : 1)) - (getMessagesController().premiumFeaturesBlocked() ? 1 : 0);
            }
            if (z) {
                arrayList = this.searchResult;
            } else if (this.unofficialLanguages.isEmpty() || i < 0 || i >= this.unofficialLanguages.size()) {
                if (!this.unofficialLanguages.isEmpty()) {
                    i -= this.unofficialLanguages.size() + 1;
                }
                arrayList = this.sortedLanguages;
            } else {
                arrayList = this.unofficialLanguages;
            }
            final LocaleController.LocaleInfo localeInfo = (LocaleController.LocaleInfo) arrayList.get(i);
            if (localeInfo != null && localeInfo.pathToFile != null && (!localeInfo.isRemote() || localeInfo.serverIndex == Integer.MAX_VALUE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString(R.string.DeleteLocalizationTitle));
                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("DeleteLocalizationText", R.string.DeleteLocalizationText, localeInfo.name)));
                builder.setPositiveButton(LocaleController.getString(R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda5
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        LanguageSelectActivity.this.lambda$createView$5(localeInfo, dialogInterface, i2);
                    }
                });
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                AlertDialog create = builder.create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                }
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$7() {
        this.listAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$fillLanguages$8(LocaleController.LocaleInfo localeInfo, LocaleController.LocaleInfo localeInfo2, LocaleController.LocaleInfo localeInfo3) {
        if (localeInfo2 == localeInfo) {
            return -1;
        }
        if (localeInfo3 == localeInfo) {
            return 1;
        }
        int i = localeInfo2.serverIndex;
        int i2 = localeInfo3.serverIndex;
        if (i == i2) {
            return localeInfo2.name.compareTo(localeInfo3.name);
        }
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBecomeFullyVisible$9() {
        if (this.isPaused) {
            return;
        }
        updateLanguage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSearch$10(String str) {
        if (str.trim().toLowerCase().length() == 0) {
            updateSearchResults(new ArrayList());
            return;
        }
        System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        int size = this.unofficialLanguages.size();
        for (int i = 0; i < size; i++) {
            LocaleController.LocaleInfo localeInfo = (LocaleController.LocaleInfo) this.unofficialLanguages.get(i);
            if (localeInfo.name.toLowerCase().startsWith(str) || localeInfo.nameEnglish.toLowerCase().startsWith(str)) {
                arrayList.add(localeInfo);
            }
        }
        int size2 = this.sortedLanguages.size();
        for (int i2 = 0; i2 < size2; i2++) {
            LocaleController.LocaleInfo localeInfo2 = (LocaleController.LocaleInfo) this.sortedLanguages.get(i2);
            if (localeInfo2.name.toLowerCase().startsWith(str) || localeInfo2.nameEnglish.toLowerCase().startsWith(str)) {
                arrayList.add(localeInfo2);
            }
        }
        updateSearchResults(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSearchResults$11(ArrayList arrayList) {
        this.searchResult = arrayList;
        this.searchListViewAdapter.notifyDataSetChanged();
    }

    private void processSearch(final String str) {
        Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$processSearch$10(str);
            }
        });
    }

    private void updateLanguage() {
        if (this.actionBar != null) {
            String string = LocaleController.getString(R.string.Language);
            if (!TextUtils.equals(this.actionBar.getTitle(), string)) {
                this.actionBar.setTitleAnimated(string, true, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
            }
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyItemRangeChanged(0, listAdapter.getItemCount());
        }
    }

    private void updateSearchResults(final ArrayList arrayList) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$updateSearchResults$11(arrayList);
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.searching = false;
        this.searchWas = false;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString(R.string.Language));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LanguageSelectActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    LanguageSelectActivity.this.lambda$onBackPressed$321();
                }
            }
        });
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.LanguageSelectActivity.2
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchCollapse() {
                LanguageSelectActivity.this.search(null);
                LanguageSelectActivity.this.searching = false;
                LanguageSelectActivity.this.searchWas = false;
                if (LanguageSelectActivity.this.listView != null) {
                    LanguageSelectActivity.this.emptyView.setVisibility(8);
                    LanguageSelectActivity.this.listView.setAdapter(LanguageSelectActivity.this.listAdapter);
                }
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchExpand() {
                LanguageSelectActivity.this.searching = true;
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onTextChanged(EditText editText) {
                RecyclerListView recyclerListView;
                ListAdapter listAdapter;
                String obj = editText.getText().toString();
                LanguageSelectActivity.this.search(obj);
                if (obj.length() != 0) {
                    LanguageSelectActivity.this.searchWas = true;
                    if (LanguageSelectActivity.this.listView == null) {
                        return;
                    }
                    recyclerListView = LanguageSelectActivity.this.listView;
                    listAdapter = LanguageSelectActivity.this.searchListViewAdapter;
                } else {
                    LanguageSelectActivity.this.searching = false;
                    LanguageSelectActivity.this.searchWas = false;
                    if (LanguageSelectActivity.this.listView == null) {
                        return;
                    }
                    LanguageSelectActivity.this.emptyView.setVisibility(8);
                    recyclerListView = LanguageSelectActivity.this.listView;
                    listAdapter = LanguageSelectActivity.this.listAdapter;
                }
                recyclerListView.setAdapter(listAdapter);
            }
        });
        this.searchItem = actionBarMenuItemSearchListener;
        actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString(R.string.Search));
        this.listAdapter = new ListAdapter(context, false);
        this.searchListViewAdapter = new ListAdapter(context, true);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout2 = (FrameLayout) this.fragmentView;
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView;
        emptyTextProgressView.setText(LocaleController.getString(R.string.NoResult));
        this.emptyView.showTextView();
        this.emptyView.setShowAtCenter(true);
        frameLayout2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.LanguageSelectActivity.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                if (getAdapter() == LanguageSelectActivity.this.listAdapter && getItemAnimator() != null && getItemAnimator().isRunning()) {
                    int color = Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider);
                    drawItemBackground(canvas, 0, LanguageSelectActivity.this.translateSettingsBackgroundHeight, color);
                    drawSectionBackground(canvas, 1, 2, color);
                }
                super.dispatchDraw(canvas);
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setEmptyView(this.emptyView);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.LanguageSelectActivity.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                LanguageSelectActivity.this.listView.invalidate();
                LanguageSelectActivity.this.listView.updateSelector();
            }
        };
        defaultItemAnimator.setDurations(400L);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.listView.setItemAnimator(defaultItemAnimator);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                LanguageSelectActivity.this.lambda$createView$4(view, i);
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i) {
                boolean lambda$createView$6;
                lambda$createView$6 = LanguageSelectActivity.this.lambda$createView$6(view, i);
                return lambda$createView$6;
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.LanguageSelectActivity.5
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                if (i == 1) {
                    AndroidUtilities.hideKeyboard(LanguageSelectActivity.this.getParentActivity().getCurrentFocus());
                }
            }
        });
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i != NotificationCenter.suggestedLangpack || this.listAdapter == null) {
            return;
        }
        fillLanguages();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$didReceivedNotification$7();
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{LanguageCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
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
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"checkImage"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_featuredStickers_addedIcon));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyVisible() {
        super.onBecomeFullyVisible();
        LocaleController.getInstance().checkForcePatchLangpack(this.currentAccount, new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$onBecomeFullyVisible$9();
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        fillLanguages();
        LocaleController.getInstance().loadRemoteLanguages(this.currentAccount, false);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void search(String str) {
        if (str != null) {
            processSearch(str);
            return;
        }
        this.searching = false;
        this.searchResult = null;
        if (this.listView != null) {
            this.emptyView.setVisibility(8);
            this.listView.setAdapter(this.listAdapter);
        }
    }
}
