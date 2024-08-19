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
    private ArrayList<LocaleController.LocaleInfo> searchResult;
    private boolean searchWas;
    private boolean searching;
    private ArrayList<LocaleController.LocaleInfo> sortedLanguages;
    private int translateSettingsBackgroundHeight;
    private ArrayList<LocaleController.LocaleInfo> unofficialLanguages;

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
                    LanguageSelectActivity.this.finishFragment();
                }
            }
        });
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.LanguageSelectActivity.2
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchExpand() {
                LanguageSelectActivity.this.searching = true;
            }

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
            public void onTextChanged(EditText editText) {
                String obj = editText.getText().toString();
                LanguageSelectActivity.this.search(obj);
                if (obj.length() != 0) {
                    LanguageSelectActivity.this.searchWas = true;
                    if (LanguageSelectActivity.this.listView != null) {
                        LanguageSelectActivity.this.listView.setAdapter(LanguageSelectActivity.this.searchListViewAdapter);
                        return;
                    }
                    return;
                }
                LanguageSelectActivity.this.searching = false;
                LanguageSelectActivity.this.searchWas = false;
                if (LanguageSelectActivity.this.listView != null) {
                    LanguageSelectActivity.this.emptyView.setVisibility(8);
                    LanguageSelectActivity.this.listView.setAdapter(LanguageSelectActivity.this.listAdapter);
                }
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

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:111:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x001c A[Catch: Exception -> 0x0015, TryCatch #0 {Exception -> 0x0015, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x000c, B:15:0x001c, B:24:0x007a, B:26:0x0080, B:32:0x008c, B:33:0x0098, B:35:0x00a0, B:37:0x00ae, B:39:0x00b2, B:40:0x00b5, B:42:0x00ba, B:44:0x00bf, B:45:0x00c6, B:17:0x0041, B:19:0x0048, B:21:0x0052, B:23:0x005d, B:47:0x00cd, B:49:0x00d1, B:51:0x00da, B:53:0x00e0, B:55:0x00e4, B:58:0x00ea, B:63:0x00f9, B:65:0x00ff, B:69:0x0108, B:71:0x0116, B:83:0x0154, B:86:0x015f, B:88:0x016b, B:89:0x0170, B:91:0x0186, B:92:0x018e, B:94:0x01a1, B:96:0x01a7, B:98:0x01b1, B:100:0x01b9, B:101:0x01bc, B:72:0x011f, B:75:0x0129, B:77:0x0131, B:78:0x013a, B:80:0x0142, B:81:0x014a), top: B:106:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008c A[Catch: Exception -> 0x0015, TryCatch #0 {Exception -> 0x0015, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x000c, B:15:0x001c, B:24:0x007a, B:26:0x0080, B:32:0x008c, B:33:0x0098, B:35:0x00a0, B:37:0x00ae, B:39:0x00b2, B:40:0x00b5, B:42:0x00ba, B:44:0x00bf, B:45:0x00c6, B:17:0x0041, B:19:0x0048, B:21:0x0052, B:23:0x005d, B:47:0x00cd, B:49:0x00d1, B:51:0x00da, B:53:0x00e0, B:55:0x00e4, B:58:0x00ea, B:63:0x00f9, B:65:0x00ff, B:69:0x0108, B:71:0x0116, B:83:0x0154, B:86:0x015f, B:88:0x016b, B:89:0x0170, B:91:0x0186, B:92:0x018e, B:94:0x01a1, B:96:0x01a7, B:98:0x01b1, B:100:0x01b9, B:101:0x01bc, B:72:0x011f, B:75:0x0129, B:77:0x0131, B:78:0x013a, B:80:0x0142, B:81:0x014a), top: B:106:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$4(View view, int i) {
        LocaleController.LocaleInfo localeInfo;
        boolean z;
        boolean z2;
        try {
            if (view instanceof TextCheckCell) {
                if (!getContextValue() && !getChatValue()) {
                    z = false;
                    if (i != 1) {
                        boolean z3 = !getContextValue();
                        getMessagesController().getTranslateController().setContextTranslateEnabled(z3);
                        ((TextCheckCell) view).setChecked(z3);
                        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateSearchSettings, new Object[0]);
                    } else if (i == 2) {
                        boolean z4 = !getChatValue();
                        if (z4 && !getUserConfig().isPremium()) {
                            showDialog(new PremiumFeatureBottomSheet(this, 13, false));
                            return;
                        }
                        getMessagesController().getTranslateController().setChatTranslateEnabled(z4);
                        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateSearchSettings, new Object[0]);
                        ((TextCheckCell) view).setChecked(z4);
                    }
                    if (!getContextValue() && !getChatValue()) {
                        z2 = false;
                        if (z2 == z) {
                            int i2 = !getMessagesController().premiumFeaturesBlocked();
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
            } else if (view instanceof TextSettingsCell) {
                presentFragment(new RestrictedLanguagesSelectActivity());
            } else if (getParentActivity() != null && this.parentLayout != null && (view instanceof TextRadioCell)) {
                boolean z5 = this.listView.getAdapter() == this.searchListViewAdapter;
                if (!z5) {
                    i -= (7 - ((getChatValue() || getContextValue()) ? 0 : 1)) - (getMessagesController().premiumFeaturesBlocked() ? 1 : 0);
                }
                if (z5) {
                    localeInfo = this.searchResult.get(i);
                } else if (!this.unofficialLanguages.isEmpty() && i >= 0 && i < this.unofficialLanguages.size()) {
                    localeInfo = this.unofficialLanguages.get(i);
                } else {
                    if (!this.unofficialLanguages.isEmpty()) {
                        i -= this.unofficialLanguages.size() + 1;
                    }
                    localeInfo = this.sortedLanguages.get(i);
                }
                if (localeInfo != null) {
                    LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
                    final boolean z6 = currentLocaleInfo == localeInfo;
                    final AlertDialog alertDialog = new AlertDialog(getContext(), 3);
                    if (!z6) {
                        alertDialog.showDelayed(500L);
                    }
                    final int applyLanguage = LocaleController.getInstance().applyLanguage(localeInfo, true, false, false, true, this.currentAccount, new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            LanguageSelectActivity.this.lambda$createView$1(alertDialog, z6);
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
                    HashSet<String> restrictedLanguages = RestrictedLanguagesSelectActivity.getRestrictedLanguages();
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
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
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
    public /* synthetic */ void lambda$createView$0() {
        this.actionBar.closeSearchField();
        updateLanguage();
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
    public /* synthetic */ boolean lambda$createView$6(View view, int i) {
        final LocaleController.LocaleInfo localeInfo;
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
                localeInfo = this.searchResult.get(i);
            } else if (!this.unofficialLanguages.isEmpty() && i >= 0 && i < this.unofficialLanguages.size()) {
                localeInfo = this.unofficialLanguages.get(i);
            } else {
                if (!this.unofficialLanguages.isEmpty()) {
                    i -= this.unofficialLanguages.size() + 1;
                }
                localeInfo = this.sortedLanguages.get(i);
            }
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
    public /* synthetic */ void lambda$createView$5(LocaleController.LocaleInfo localeInfo, DialogInterface dialogInterface, int i) {
        if (LocaleController.getInstance().deleteLanguage(localeInfo, this.currentAccount)) {
            fillLanguages();
            ArrayList<LocaleController.LocaleInfo> arrayList = this.searchResult;
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$7() {
        this.listAdapter.notifyDataSetChanged();
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
        this.sortedLanguages = new ArrayList<>();
        this.unofficialLanguages = new ArrayList<>(LocaleController.getInstance().unofficialLanguages);
        ArrayList<LocaleController.LocaleInfo> arrayList = LocaleController.getInstance().languages;
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            LocaleController.LocaleInfo localeInfo = arrayList.get(i);
            if (localeInfo.serverIndex != Integer.MAX_VALUE) {
                this.sortedLanguages.add(localeInfo);
            } else {
                this.unofficialLanguages.add(localeInfo);
            }
        }
        Collections.sort(this.sortedLanguages, comparator);
        Collections.sort(this.unofficialLanguages, comparator);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBecomeFullyVisible$9() {
        if (this.isPaused) {
            return;
        }
        updateLanguage();
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
        if (str == null) {
            this.searching = false;
            this.searchResult = null;
            if (this.listView != null) {
                this.emptyView.setVisibility(8);
                this.listView.setAdapter(this.listAdapter);
                return;
            }
            return;
        }
        processSearch(str);
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

    private void processSearch(final String str) {
        Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$processSearch$10(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSearch$10(String str) {
        if (str.trim().toLowerCase().length() == 0) {
            updateSearchResults(new ArrayList<>());
            return;
        }
        System.currentTimeMillis();
        ArrayList<LocaleController.LocaleInfo> arrayList = new ArrayList<>();
        int size = this.unofficialLanguages.size();
        for (int i = 0; i < size; i++) {
            LocaleController.LocaleInfo localeInfo = this.unofficialLanguages.get(i);
            if (localeInfo.name.toLowerCase().startsWith(str) || localeInfo.nameEnglish.toLowerCase().startsWith(str)) {
                arrayList.add(localeInfo);
            }
        }
        int size2 = this.sortedLanguages.size();
        for (int i2 = 0; i2 < size2; i2++) {
            LocaleController.LocaleInfo localeInfo2 = this.sortedLanguages.get(i2);
            if (localeInfo2.name.toLowerCase().startsWith(str) || localeInfo2.nameEnglish.toLowerCase().startsWith(str)) {
                arrayList.add(localeInfo2);
            }
        }
        updateSearchResults(arrayList);
    }

    private void updateSearchResults(final ArrayList<LocaleController.LocaleInfo> arrayList) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$updateSearchResults$11(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSearchResults$11(ArrayList arrayList) {
        this.searchResult = arrayList;
        this.searchListViewAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getContextValue() {
        return getMessagesController().getTranslateController().isContextTranslateEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getChatValue() {
        return getMessagesController().getTranslateController().isFeatureAvailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private boolean search;

        public ListAdapter(Context context, boolean z) {
            this.mContext = context;
            this.search = z;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 4 || itemViewType == 2;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int i = 0;
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
            return (!LanguageSelectActivity.this.getMessagesController().premiumFeaturesBlocked()) + 4 + ((LanguageSelectActivity.this.getChatValue() || LanguageSelectActivity.this.getContextValue()) ? 1 : 1) + 1 + size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textRadioCell;
            if (i == 0) {
                textRadioCell = new TextRadioCell(this.mContext);
                textRadioCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (i == 2) {
                textRadioCell = new TextCheckCell(this.mContext);
                textRadioCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (i == 3) {
                textRadioCell = new HeaderCell(this.mContext);
                textRadioCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (i == 4) {
                textRadioCell = new TextSettingsCell(this.mContext);
                textRadioCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (i == 5) {
                textRadioCell = new TextInfoPrivacyCell(this.mContext);
            } else {
                textRadioCell = new ShadowSectionCell(this.mContext);
            }
            return new RecyclerListView.Holder(textRadioCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof TextRadioCell) {
                ((TextRadioCell) view).updateRTL();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:104:0x027e, code lost:
            if (r13 == (r11.this$0.unofficialLanguages.size() - 1)) goto L123;
         */
        /* JADX WARN: Code restructure failed: missing block: B:114:0x02bf, code lost:
            if (r13 == (r11.this$0.sortedLanguages.size() - 1)) goto L123;
         */
        /* JADX WARN: Code restructure failed: missing block: B:115:0x02c1, code lost:
            r13 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:116:0x02c3, code lost:
            r13 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x0122, code lost:
            if (r12.getValueTextView().getPaint().measureText(r3) > java.lang.Math.min((org.telegram.messenger.AndroidUtilities.displaySize.x - org.telegram.messenger.AndroidUtilities.dp(34.0f)) / 2.0f, (org.telegram.messenger.AndroidUtilities.displaySize.x - org.telegram.messenger.AndroidUtilities.dp(84.0f)) - r12.getTextView().getPaint().measureText(r0))) goto L32;
         */
        /* JADX WARN: Code restructure failed: missing block: B:96:0x0248, code lost:
            if (r13 == (r11.this$0.searchResult.size() - 1)) goto L123;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r12v10, types: [org.telegram.ui.Cells.TextSettingsCell] */
        /* JADX WARN: Type inference failed for: r4v12 */
        /* JADX WARN: Type inference failed for: r4v13, types: [java.lang.CharSequence] */
        /* JADX WARN: Type inference failed for: r4v16, types: [java.lang.String] */
        /* JADX WARN: Type inference failed for: r4v17 */
        /* JADX WARN: Type inference failed for: r4v3, types: [org.telegram.messenger.LocaleController$LocaleInfo] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
            String sb;
            int itemViewType = viewHolder.getItemViewType();
            LocaleController.LocaleInfo localeInfo = 0;
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
                    if (localeInfo.isLocal()) {
                        textRadioCell.setTextAndValueAndCheck(String.format("%1$s (%2$s)", localeInfo.name, LocaleController.getString(R.string.LanguageCustom)), localeInfo.nameEnglish, false, false, !z);
                    } else {
                        textRadioCell.setTextAndValueAndCheck(localeInfo.name, localeInfo.nameEnglish, false, false, !z);
                    }
                }
                textRadioCell.setChecked(localeInfo == LocaleController.getInstance().getCurrentLocaleInfo());
            } else if (itemViewType == 1) {
                if (!this.search) {
                    i--;
                }
                ShadowSectionCell shadowSectionCell = (ShadowSectionCell) viewHolder.itemView;
                if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty() && i == LanguageSelectActivity.this.unofficialLanguages.size()) {
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                } else {
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                }
            } else if (itemViewType == 2) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                textCheckCell.updateRTL();
                if (i == 1) {
                    textCheckCell.setTextAndCheck(LocaleController.getString(R.string.ShowTranslateButton), LanguageSelectActivity.this.getContextValue(), true);
                    textCheckCell.setCheckBoxIcon(0);
                } else if (i == 2) {
                    String string = LocaleController.getString(R.string.ShowTranslateChatButton);
                    boolean chatValue = LanguageSelectActivity.this.getChatValue();
                    if (!LanguageSelectActivity.this.getContextValue() && !LanguageSelectActivity.this.getChatValue()) {
                        r1 = false;
                    }
                    textCheckCell.setTextAndCheck(string, chatValue, r1);
                    textCheckCell.setCheckBoxIcon(LanguageSelectActivity.this.getUserConfig().isPremium() ? 0 : R.drawable.permission_locked);
                }
            } else {
                int i2 = 3;
                if (itemViewType == 3) {
                    ((HeaderCell) viewHolder.itemView).setText(LocaleController.getString(i == 0 ? R.string.TranslateMessages : R.string.Language));
                } else if (itemViewType != 4) {
                    if (itemViewType != 5) {
                        return;
                    }
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                    textInfoPrivacyCell.updateRTL();
                    if (!LanguageSelectActivity.this.getMessagesController().premiumFeaturesBlocked() && (LanguageSelectActivity.this.getContextValue() || LanguageSelectActivity.this.getChatValue())) {
                        i2 = 4;
                    }
                    if (i == i2) {
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
                } else {
                    ?? r12 = (TextSettingsCell) viewHolder.itemView;
                    r12.updateRTL();
                    HashSet<String> restrictedLanguages = RestrictedLanguagesSelectActivity.getRestrictedLanguages();
                    String string2 = LocaleController.getString(R.string.DoNotTranslate);
                    try {
                        boolean[] zArr = new boolean[1];
                        if (restrictedLanguages.size() == 0) {
                            sb = "";
                        } else if (restrictedLanguages.size() == 1) {
                            sb = TranslateAlert2.capitalFirst(TranslateAlert2.languageName(restrictedLanguages.iterator().next(), zArr));
                        } else {
                            Iterator<String> it = restrictedLanguages.iterator();
                            StringBuilder sb2 = new StringBuilder();
                            boolean z2 = true;
                            while (it.hasNext()) {
                                String next = it.next();
                                if (!z2) {
                                    sb2.append(", ");
                                }
                                String capitalFirst = TranslateAlert2.capitalFirst(TranslateAlert2.languageName(next, zArr));
                                if (capitalFirst != null) {
                                    sb2.append(capitalFirst);
                                    z2 = false;
                                }
                            }
                            sb = sb2.toString();
                        }
                        localeInfo = sb;
                    } catch (Exception unused) {
                    }
                    if (localeInfo == 0) {
                        localeInfo = String.format(LocaleController.getPluralString("Languages", restrictedLanguages.size()), Integer.valueOf(restrictedLanguages.size()));
                    }
                    r12.setTextAndValue(string2, localeInfo, true, false);
                }
            }
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
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
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
}
