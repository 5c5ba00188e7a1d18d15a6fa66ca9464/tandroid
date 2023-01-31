package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LanguageDetector;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
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
import org.telegram.ui.LanguageSelectActivity;
/* loaded from: classes3.dex */
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
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("Language", R.string.Language));
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
        actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString("Search", R.string.Search));
        this.listAdapter = new ListAdapter(context, false);
        this.searchListViewAdapter = new ListAdapter(context, true);
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        FrameLayout frameLayout2 = (FrameLayout) this.fragmentView;
        EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView;
        emptyTextProgressView.setText(LocaleController.getString("NoResult", R.string.NoResult));
        this.emptyView.showTextView();
        this.emptyView.setShowAtCenter(true);
        frameLayout2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.LanguageSelectActivity.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                if (getAdapter() == LanguageSelectActivity.this.listAdapter && getItemAnimator() != null && getItemAnimator().isRunning()) {
                    int color = Theme.getColor("windowBackgroundWhite", this.resourcesProvider);
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
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.listView.setItemAnimator(defaultItemAnimator);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda8
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                LanguageSelectActivity.this.lambda$createView$3(view, i);
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda9
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i) {
                boolean lambda$createView$5;
                lambda$createView$5 = LanguageSelectActivity.this.lambda$createView$5(view, i);
                return lambda$createView$5;
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
    public /* synthetic */ void lambda$createView$3(View view, int i) {
        LocaleController.LocaleInfo localeInfo;
        try {
            if (getParentActivity() != null && this.parentLayout != null && (view instanceof TextRadioCell)) {
                boolean z = this.listView.getAdapter() == this.searchListViewAdapter;
                if (!z) {
                    i -= 2;
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
                if (localeInfo != null) {
                    LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
                    final boolean z2 = currentLocaleInfo == localeInfo;
                    final AlertDialog alertDialog = new AlertDialog(getContext(), 3);
                    final int applyLanguage = LocaleController.getInstance().applyLanguage(localeInfo, true, false, false, true, this.currentAccount, new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            LanguageSelectActivity.this.lambda$createView$0(alertDialog, z2);
                        }
                    });
                    if (applyLanguage != 0) {
                        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda0
                            @Override // android.content.DialogInterface.OnCancelListener
                            public final void onCancel(DialogInterface dialogInterface) {
                                LanguageSelectActivity.this.lambda$createView$1(applyLanguage, dialogInterface);
                            }
                        });
                    }
                    String str = localeInfo.pluralLangCode;
                    final String str2 = currentLocaleInfo.pluralLangCode;
                    SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                    HashSet<String> restrictedLanguages = RestrictedLanguagesSelectActivity.getRestrictedLanguages();
                    HashSet hashSet = new HashSet(restrictedLanguages);
                    if (restrictedLanguages.contains(str2) && !restrictedLanguages.contains(str)) {
                        Collection$-EL.removeIf(hashSet, new Predicate() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda7
                            @Override // j$.util.function.Predicate
                            public /* synthetic */ Predicate and(Predicate predicate) {
                                return Objects.requireNonNull(predicate);
                            }

                            @Override // j$.util.function.Predicate
                            public /* synthetic */ Predicate negate() {
                                return Predicate.-CC.$default$negate(this);
                            }

                            @Override // j$.util.function.Predicate
                            public /* synthetic */ Predicate or(Predicate predicate) {
                                return Objects.requireNonNull(predicate);
                            }

                            @Override // j$.util.function.Predicate
                            public final boolean test(Object obj) {
                                boolean lambda$createView$2;
                                lambda$createView$2 = LanguageSelectActivity.lambda$createView$2(str2, (String) obj);
                                return lambda$createView$2;
                            }
                        });
                        hashSet.add(str);
                    }
                    globalMainSettings.edit().putStringSet("translate_button_restricted_languages", hashSet).apply();
                    MessagesController.getInstance(this.currentAccount).getTranslateController().checkRestrictedLanguagesUpdate();
                    MessagesController.getInstance(this.currentAccount).getTranslateController().cleanup();
                    if (z2) {
                        return;
                    }
                    alertDialog.showDelayed(500L);
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(AlertDialog alertDialog, boolean z) {
        alertDialog.dismiss();
        if (z) {
            return;
        }
        this.actionBar.closeSearchField();
        updateLanguage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(int i, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$2(String str, String str2) {
        return str2 != null && str2.equals(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$5(View view, int i) {
        final LocaleController.LocaleInfo localeInfo;
        try {
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (getParentActivity() != null && this.parentLayout != null && (view instanceof TextRadioCell)) {
            boolean z = this.listView.getAdapter() == this.searchListViewAdapter;
            if (!z) {
                i -= 2;
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
                builder.setTitle(LocaleController.getString("DeleteLocalizationTitle", R.string.DeleteLocalizationTitle));
                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("DeleteLocalizationText", R.string.DeleteLocalizationText, localeInfo.name)));
                builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        LanguageSelectActivity.this.lambda$createView$4(localeInfo, dialogInterface, i2);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                AlertDialog create = builder.create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor("dialogTextRed2"));
                }
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(LocaleController.LocaleInfo localeInfo, DialogInterface dialogInterface, int i) {
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$didReceivedNotification$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$6() {
        this.listAdapter.notifyDataSetChanged();
    }

    private void fillLanguages() {
        final LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
        Comparator comparator = new Comparator() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda6
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$fillLanguages$7;
                lambda$fillLanguages$7 = LanguageSelectActivity.lambda$fillLanguages$7(LocaleController.LocaleInfo.this, (LocaleController.LocaleInfo) obj, (LocaleController.LocaleInfo) obj2);
                return lambda$fillLanguages$7;
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
    public static /* synthetic */ int lambda$fillLanguages$7(LocaleController.LocaleInfo localeInfo, LocaleController.LocaleInfo localeInfo2, LocaleController.LocaleInfo localeInfo3) {
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
        ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.setTitleAnimated(LocaleController.getString("Language", R.string.Language), true, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
        }
        if (this.listView != null) {
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                View childAt = this.listView.getChildAt(i);
                if ((childAt instanceof TranslateSettings) || (childAt instanceof HeaderCell)) {
                    this.listAdapter.notifyItemChanged(this.listView.getChildAdapterPosition(childAt));
                } else {
                    this.listAdapter.onBindViewHolder(this.listView.getChildViewHolder(childAt), this.listView.getChildAdapterPosition(childAt));
                }
            }
        }
    }

    private void processSearch(final String str) {
        Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$processSearch$8(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSearch$8(String str) {
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LanguageSelectActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                LanguageSelectActivity.this.lambda$updateSearchResults$9(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSearchResults$9(ArrayList arrayList) {
        this.searchResult = arrayList;
        this.searchListViewAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TranslateSettings extends LinearLayout {
        private Drawable bottomShadow;
        private TextSettingsCell doNotTranslateCell;
        private ValueAnimator doNotTranslateCellAnimation;
        private HeaderCell header;
        private TextInfoPrivacyCell info;
        private TextInfoPrivacyCell info2;
        private SharedPreferences preferences;
        private TextCheckCell showButtonCheck;
        private TextCheckCell showChatButtonCheck;
        private Drawable topShadow;

        public TranslateSettings(Context context) {
            super(context);
            this.doNotTranslateCellAnimation = null;
            setFocusable(false);
            setOrientation(1);
            this.topShadow = Theme.getThemedDrawable(context, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow");
            this.bottomShadow = Theme.getThemedDrawable(context, R.drawable.greydivider_top, "windowBackgroundGrayShadow");
            this.preferences = MessagesController.getMainSettings(((BaseFragment) LanguageSelectActivity.this).currentAccount);
            HeaderCell headerCell = new HeaderCell(context);
            this.header = headerCell;
            headerCell.setFocusable(true);
            this.header.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            HeaderCell headerCell2 = this.header;
            int i = R.string.TranslateMessages;
            headerCell2.setText(LocaleController.getString("TranslateMessages", i));
            this.header.setContentDescription(LocaleController.getString("TranslateMessages", i));
            addView(this.header, LayoutHelper.createLinear(-1, -2));
            boolean value = getValue();
            TextCheckCell textCheckCell = new TextCheckCell(context);
            this.showButtonCheck = textCheckCell;
            textCheckCell.setBackground(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("windowBackgroundWhite"), Theme.getColor("listSelectorSDK21")));
            this.showButtonCheck.setTextAndCheck(LocaleController.getString("ShowTranslateButton", R.string.ShowTranslateButton), getContextValue(), true);
            this.showButtonCheck.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LanguageSelectActivity$TranslateSettings$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LanguageSelectActivity.TranslateSettings.this.lambda$new$0(view);
                }
            });
            addView(this.showButtonCheck, LayoutHelper.createLinear(-1, -2));
            TextCheckCell textCheckCell2 = new TextCheckCell(context);
            this.showChatButtonCheck = textCheckCell2;
            textCheckCell2.setBackground(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("windowBackgroundWhite"), Theme.getColor("listSelectorSDK21")));
            this.showChatButtonCheck.setTextAndCheck(LocaleController.getString("ShowTranslateChatButton", R.string.ShowTranslateChatButton), getChatValue(), value);
            this.showChatButtonCheck.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LanguageSelectActivity$TranslateSettings$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LanguageSelectActivity.TranslateSettings.this.lambda$new$1(view);
                }
            });
            this.showChatButtonCheck.setCheckBoxIcon(!LanguageSelectActivity.this.getUserConfig().isPremium() ? R.drawable.permission_locked : 0);
            addView(this.showChatButtonCheck, LayoutHelper.createLinear(-1, -2));
            TextSettingsCell textSettingsCell = new TextSettingsCell(context);
            this.doNotTranslateCell = textSettingsCell;
            textSettingsCell.setBackground(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("windowBackgroundWhite"), Theme.getColor("listSelectorSDK21")));
            this.doNotTranslateCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LanguageSelectActivity$TranslateSettings$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LanguageSelectActivity.TranslateSettings.this.lambda$new$2(view);
                }
            });
            this.doNotTranslateCell.setClickable(value && LanguageDetector.hasSupport());
            this.doNotTranslateCell.setAlpha((value && LanguageDetector.hasSupport()) ? 1.0f : 0.0f);
            addView(this.doNotTranslateCell, LayoutHelper.createLinear(-1, -2));
            TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
            this.info = textInfoPrivacyCell;
            textInfoPrivacyCell.setTopPadding(11);
            this.info.setBottomPadding(16);
            this.info.setFocusable(true);
            TextInfoPrivacyCell textInfoPrivacyCell2 = this.info;
            int i2 = R.string.TranslateMessagesInfo1;
            textInfoPrivacyCell2.setText(LocaleController.getString("TranslateMessagesInfo1", i2));
            this.info.setContentDescription(LocaleController.getString("TranslateMessagesInfo1", i2));
            addView(this.info, LayoutHelper.createLinear(-1, -2));
            TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
            this.info2 = textInfoPrivacyCell3;
            textInfoPrivacyCell3.setTopPadding(0);
            this.info2.setBottomPadding(16);
            this.info2.setFocusable(true);
            TextInfoPrivacyCell textInfoPrivacyCell4 = this.info2;
            int i3 = R.string.TranslateMessagesInfo2;
            textInfoPrivacyCell4.setText(LocaleController.getString("TranslateMessagesInfo2", i3));
            this.info2.setContentDescription(LocaleController.getString("TranslateMessagesInfo2", i3));
            this.info2.setAlpha(value ? 0.0f : 1.0f);
            addView(this.info2, LayoutHelper.createLinear(-1, -2));
            updateHeight();
            update();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            LanguageSelectActivity.this.getMessagesController().getTranslateController().setContextTranslateEnabled(!getContextValue());
            NotificationCenter.getInstance(((BaseFragment) LanguageSelectActivity.this).currentAccount).postNotificationName(NotificationCenter.updateSearchSettings, new Object[0]);
            update();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view) {
            if (!LanguageSelectActivity.this.getUserConfig().isPremium()) {
                LanguageSelectActivity.this.showDialog(new PremiumFeatureBottomSheet(LanguageSelectActivity.this, 13, false));
                return;
            }
            this.preferences.edit().putBoolean("translate_chat_button", !getChatValue()).apply();
            NotificationCenter.getInstance(((BaseFragment) LanguageSelectActivity.this).currentAccount).postNotificationName(NotificationCenter.updateSearchSettings, new Object[0]);
            update();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(View view) {
            LanguageSelectActivity.this.presentFragment(new RestrictedLanguagesSelectActivity());
            update();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            float max = Math.max(this.showChatButtonCheck.getY() + this.showChatButtonCheck.getHeight(), this.doNotTranslateCell.getY() + (this.doNotTranslateCell.getHeight() * this.doNotTranslateCell.getAlpha()));
            this.topShadow.setBounds(0, (int) max, getWidth(), (int) (max + AndroidUtilities.dp(12.0f)));
            this.topShadow.draw(canvas);
            float height = getHeight();
            this.bottomShadow.setBounds(0, (int) (height - AndroidUtilities.dp(12.0f)), getWidth(), (int) height);
            this.bottomShadow.draw(canvas);
        }

        public void updateTranslations() {
            this.header.setText(LocaleController.getString("TranslateMessages", R.string.TranslateMessages));
            this.showButtonCheck.setTextAndCheck(LocaleController.getString("ShowTranslateButton", R.string.ShowTranslateButton), getContextValue(), true);
            this.showChatButtonCheck.setTextAndCheck(LocaleController.getString("ShowTranslateChatButton", R.string.ShowTranslateChatButton), getChatValue(), getValue());
            this.showChatButtonCheck.setCheckBoxIcon(!LanguageSelectActivity.this.getUserConfig().isPremium() ? R.drawable.permission_locked : 0);
            this.showButtonCheck.updateRTL();
            this.doNotTranslateCell.updateRTL();
            this.info.setText(LocaleController.getString("TranslateMessagesInfo1", R.string.TranslateMessagesInfo1));
            this.info.getTextView().setGravity(LocaleController.isRTL ? 5 : 3);
            this.info2.setText(LocaleController.getString("TranslateMessagesInfo2", R.string.TranslateMessagesInfo2));
            this.info2.getTextView().setGravity(LocaleController.isRTL ? 5 : 3);
            update();
            updateHeight();
        }

        private boolean getContextValue() {
            return LanguageSelectActivity.this.getMessagesController().getTranslateController().isContextTranslateEnabled();
        }

        private boolean getChatValue() {
            return LanguageSelectActivity.this.getUserConfig().isPremium() && this.preferences.getBoolean("translate_chat_button", true);
        }

        private boolean getValue() {
            return getContextValue() || getChatValue();
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x00c2, code lost:
            if (r11.doNotTranslateCell.getValueTextView().getPaint().measureText(r6) > java.lang.Math.min((org.telegram.messenger.AndroidUtilities.displaySize.x - org.telegram.messenger.AndroidUtilities.dp(34.0f)) / 2.0f, (org.telegram.messenger.AndroidUtilities.displaySize.x - org.telegram.messenger.AndroidUtilities.dp(84.0f)) - r11.doNotTranslateCell.getTextView().getPaint().measureText(r4))) goto L14;
         */
        /* JADX WARN: Removed duplicated region for block: B:30:0x00ca  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0103  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x0106  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x0133  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void update() {
            String sb;
            boolean z = getValue() && LanguageDetector.hasSupport();
            this.showButtonCheck.setChecked(getContextValue());
            this.showChatButtonCheck.setChecked(getChatValue());
            ValueAnimator valueAnimator = this.doNotTranslateCellAnimation;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.showChatButtonCheck.setDivider(z);
            HashSet<String> restrictedLanguages = RestrictedLanguagesSelectActivity.getRestrictedLanguages();
            String string = LocaleController.getString("DoNotTranslate", R.string.DoNotTranslate);
            String str = null;
            try {
                if (restrictedLanguages.size() == 1) {
                    str = TranslateAlert2.capitalFirst(TranslateAlert2.languageName(restrictedLanguages.iterator().next()));
                } else {
                    Iterator<String> it = restrictedLanguages.iterator();
                    StringBuilder sb2 = new StringBuilder();
                    boolean z2 = true;
                    while (it.hasNext()) {
                        String next = it.next();
                        if (!z2) {
                            sb2.append(", ");
                        }
                        sb2.append(TranslateAlert2.capitalFirst(TranslateAlert2.languageName(next)));
                        z2 = false;
                    }
                    sb = sb2.toString();
                }
            } catch (Exception unused) {
            }
            if (str == null) {
                str = String.format(LocaleController.getPluralString("Languages", restrictedLanguages.size()), Integer.valueOf(restrictedLanguages.size()));
            }
            this.doNotTranslateCell.setTextAndValue(string, str, false, false);
            this.doNotTranslateCell.setClickable(z);
            this.info2.setVisibility(0);
            float[] fArr = new float[2];
            fArr[0] = this.doNotTranslateCell.getAlpha();
            fArr[1] = !z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.doNotTranslateCellAnimation = ofFloat;
            ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.doNotTranslateCellAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LanguageSelectActivity$TranslateSettings$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    LanguageSelectActivity.TranslateSettings.this.lambda$update$3(valueAnimator2);
                }
            });
            this.doNotTranslateCellAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LanguageSelectActivity.TranslateSettings.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (TranslateSettings.this.doNotTranslateCell.getAlpha() > 0.5d) {
                        TranslateSettings.this.info2.setVisibility(8);
                    } else {
                        TranslateSettings.this.info2.setVisibility(0);
                    }
                    TranslateSettings translateSettings = TranslateSettings.this;
                    LanguageSelectActivity.this.translateSettingsBackgroundHeight = translateSettings.header.getMeasuredHeight() + TranslateSettings.this.showButtonCheck.getMeasuredHeight() + ((int) (TranslateSettings.this.doNotTranslateCell.getAlpha() * TranslateSettings.this.doNotTranslateCell.getMeasuredHeight()));
                    TranslateSettings.this.invalidate();
                }
            });
            this.doNotTranslateCellAnimation.setDuration(Math.abs(this.doNotTranslateCell.getAlpha() - (z ? 1.0f : 0.0f)) * 200.0f);
            this.doNotTranslateCellAnimation.start();
            str = sb;
            if (str == null) {
            }
            this.doNotTranslateCell.setTextAndValue(string, str, false, false);
            this.doNotTranslateCell.setClickable(z);
            this.info2.setVisibility(0);
            float[] fArr2 = new float[2];
            fArr2[0] = this.doNotTranslateCell.getAlpha();
            fArr2[1] = !z ? 1.0f : 0.0f;
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(fArr2);
            this.doNotTranslateCellAnimation = ofFloat2;
            ofFloat2.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.doNotTranslateCellAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LanguageSelectActivity$TranslateSettings$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    LanguageSelectActivity.TranslateSettings.this.lambda$update$3(valueAnimator2);
                }
            });
            this.doNotTranslateCellAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LanguageSelectActivity.TranslateSettings.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (TranslateSettings.this.doNotTranslateCell.getAlpha() > 0.5d) {
                        TranslateSettings.this.info2.setVisibility(8);
                    } else {
                        TranslateSettings.this.info2.setVisibility(0);
                    }
                    TranslateSettings translateSettings = TranslateSettings.this;
                    LanguageSelectActivity.this.translateSettingsBackgroundHeight = translateSettings.header.getMeasuredHeight() + TranslateSettings.this.showButtonCheck.getMeasuredHeight() + ((int) (TranslateSettings.this.doNotTranslateCell.getAlpha() * TranslateSettings.this.doNotTranslateCell.getMeasuredHeight()));
                    TranslateSettings.this.invalidate();
                }
            });
            this.doNotTranslateCellAnimation.setDuration(Math.abs(this.doNotTranslateCell.getAlpha() - (z ? 1.0f : 0.0f)) * 200.0f);
            this.doNotTranslateCellAnimation.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$update$3(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.doNotTranslateCell.setAlpha(floatValue);
            float f = 1.0f - floatValue;
            this.doNotTranslateCell.setTranslationY((-AndroidUtilities.dp(8.0f)) * f);
            this.info.setTranslationY((-this.doNotTranslateCell.getHeight()) * f);
            this.info2.setAlpha(f);
            this.info2.setTranslationY((-this.doNotTranslateCell.getHeight()) * f);
            LanguageSelectActivity.this.translateSettingsBackgroundHeight = this.header.getMeasuredHeight() + this.showButtonCheck.getMeasuredHeight() + ((int) (this.doNotTranslateCell.getAlpha() * this.doNotTranslateCell.getMeasuredHeight()));
            invalidate();
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            updateHeight();
        }

        @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            updateHeight();
            super.onLayout(z, i, i2, i3, i4);
        }

        void updateHeight() {
            this.header.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, 1073741824), 0);
            this.showButtonCheck.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, 1073741824), 0);
            this.showChatButtonCheck.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, 1073741824), 0);
            this.doNotTranslateCell.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, 1073741824), 0);
            this.info.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, 1073741824), 0);
            this.info2.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x, 1073741824), 0);
            int height = LanguageSelectActivity.this.searching ? 0 : height();
            if (getLayoutParams() == null) {
                setLayoutParams(new RecyclerView.LayoutParams(-1, height));
            } else if (getLayoutParams().height != height) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
                ((ViewGroup.MarginLayoutParams) layoutParams).height = height;
                setLayoutParams(layoutParams);
            }
            invalidate();
        }

        int height() {
            return Math.max(AndroidUtilities.dp(40.0f), this.header.getMeasuredHeight()) + Math.max(AndroidUtilities.dp(50.0f), this.showButtonCheck.getMeasuredHeight()) + Math.max(AndroidUtilities.dp(50.0f), this.showChatButtonCheck.getMeasuredHeight()) + Math.max(Math.max(AndroidUtilities.dp(50.0f), this.doNotTranslateCell.getMeasuredHeight()), this.info2.getMeasuredHeight() <= 0 ? AndroidUtilities.dp(51.0f) : this.info2.getMeasuredHeight()) + (this.info.getMeasuredHeight() <= 0 ? AndroidUtilities.dp(62.0f) : this.info.getMeasuredHeight());
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            updateHeight();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            update();
            updateHeight();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private boolean search;

        public ListAdapter(Context context, boolean z) {
            this.mContext = context;
            this.search = z;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
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
            return size + 2;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            FrameLayout textRadioCell;
            View view;
            if (i == 0) {
                textRadioCell = new TextRadioCell(this.mContext);
                textRadioCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else {
                if (i == 2) {
                    view = new TranslateSettings(this.mContext);
                } else if (i == 3) {
                    textRadioCell = new HeaderCell(this.mContext);
                    textRadioCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                } else {
                    view = new ShadowSectionCell(this.mContext);
                }
                return new RecyclerListView.Holder(view);
            }
            view = textRadioCell;
            return new RecyclerListView.Holder(view);
        }

        /* JADX WARN: Code restructure failed: missing block: B:33:0x00aa, code lost:
            if (r12 == (r10.this$0.searchResult.size() - 1)) goto L38;
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x00ac, code lost:
            r12 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x00ae, code lost:
            r12 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:43:0x00e4, code lost:
            if (r12 == (r10.this$0.unofficialLanguages.size() - 1)) goto L38;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x0125, code lost:
            if (r12 == (r10.this$0.sortedLanguages.size() - 1)) goto L38;
         */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType != 3) {
                            return;
                        }
                        ((HeaderCell) viewHolder.itemView).setText(LocaleController.getString("Language", R.string.Language));
                        return;
                    }
                    TranslateSettings translateSettings = (TranslateSettings) viewHolder.itemView;
                    translateSettings.setVisibility(LanguageSelectActivity.this.searching ? 8 : 0);
                    translateSettings.updateTranslations();
                    return;
                }
                if (!this.search) {
                    i--;
                }
                ShadowSectionCell shadowSectionCell = (ShadowSectionCell) viewHolder.itemView;
                if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty() && i == LanguageSelectActivity.this.unofficialLanguages.size()) {
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, "windowBackgroundGrayShadow"));
                    return;
                } else {
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                    return;
                }
            }
            boolean z2 = this.search;
            if (!z2) {
                i -= 2;
            }
            TextRadioCell textRadioCell = (TextRadioCell) viewHolder.itemView;
            LocaleController.LocaleInfo localeInfo = null;
            if (z2) {
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
            LocaleController.LocaleInfo localeInfo2 = localeInfo;
            if (localeInfo2 != null) {
                if (localeInfo2.isLocal()) {
                    textRadioCell.setTextAndValueAndCheck(String.format("%1$s (%2$s)", localeInfo2.name, LocaleController.getString("LanguageCustom", R.string.LanguageCustom)), localeInfo2.nameEnglish, false, false, !z);
                } else {
                    textRadioCell.setTextAndValueAndCheck(localeInfo2.name, localeInfo2.nameEnglish, false, false, !z);
                }
            }
            textRadioCell.setChecked(localeInfo2 == LocaleController.getInstance().getCurrentLocaleInfo());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            boolean z = this.search;
            if (!z) {
                i -= 2;
            }
            if (i == -2) {
                return 2;
            }
            if (i == -1) {
                return 3;
            }
            if (z) {
                return 0;
            }
            return ((LanguageSelectActivity.this.unofficialLanguages.isEmpty() || !(i == LanguageSelectActivity.this.unofficialLanguages.size() || i == (LanguageSelectActivity.this.unofficialLanguages.size() + LanguageSelectActivity.this.sortedLanguages.size()) + 1)) && !(LanguageSelectActivity.this.unofficialLanguages.isEmpty() && i == LanguageSelectActivity.this.sortedLanguages.size())) ? 0 : 1;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{LanguageCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"textView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText3"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"checkImage"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "featuredStickers_addedIcon"));
        return arrayList;
    }
}
