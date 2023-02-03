package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import j$.util.Collection$-EL;
import j$.util.function.Predicate;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Timer;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.TranslateController;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckbox2Cell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.TranslateAlert2;
/* loaded from: classes3.dex */
public class RestrictedLanguagesSelectActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private EmptyTextProgressView emptyView;
    private HashSet<String> firstSelectedLanguages;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private SharedPreferences preferences;
    private ListAdapter searchListViewAdapter;
    private ArrayList<LocaleController.LocaleInfo> searchResult;
    private Timer searchTimer;
    private boolean searchWas;
    private boolean searching;
    private HashSet<String> selectedLanguages;
    private ArrayList<LocaleController.LocaleInfo> sortedLanguages;

    public static HashSet<String> getRestrictedLanguages() {
        return new HashSet<>(MessagesController.getGlobalMainSettings().getStringSet("translate_button_restricted_languages", new HashSet(Arrays.asList(LocaleController.getInstance().getCurrentLocaleInfo().pluralLangCode))));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        this.preferences = MessagesController.getGlobalMainSettings();
        this.firstSelectedLanguages = getRestrictedLanguages();
        this.selectedLanguages = getRestrictedLanguages();
        fillLanguages();
        LocaleController.getInstance().loadRemoteLanguages(this.currentAccount);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        return super.onFragmentCreate();
    }

    private void rebind(int i) {
        int adapterPosition;
        for (int i2 = 0; i2 < this.listView.getChildCount(); i2++) {
            RecyclerView.ViewHolder childViewHolder = this.listView.getChildViewHolder(this.listView.getChildAt(i2));
            if (childViewHolder != null && (adapterPosition = childViewHolder.getAdapterPosition()) != -1 && adapterPosition == i) {
                this.listAdapter.onBindViewHolder(childViewHolder, i);
                return;
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
    }

    public static boolean toggleLanguage(String str, boolean z) {
        if (str == null) {
            return false;
        }
        LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
        HashSet<String> restrictedLanguages = getRestrictedLanguages();
        if (str.equals(currentLocaleInfo.pluralLangCode) && z) {
            return false;
        }
        if (!z) {
            restrictedLanguages.remove(str);
        } else {
            restrictedLanguages.add(str);
        }
        if (restrictedLanguages.size() == 1 && restrictedLanguages.contains(currentLocaleInfo.pluralLangCode)) {
            MessagesController.getGlobalMainSettings().edit().remove("translate_button_restricted_languages").commit();
        } else {
            MessagesController.getGlobalMainSettings().edit().putStringSet("translate_button_restricted_languages", restrictedLanguages).commit();
        }
        TranslateController.invalidateSuggestedLanguageCodes();
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("DoNotTranslate", R.string.DoNotTranslate));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    RestrictedLanguagesSelectActivity.this.finishFragment();
                }
            }
        });
        this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity.2
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchExpand() {
                RestrictedLanguagesSelectActivity.this.searching = true;
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchCollapse() {
                RestrictedLanguagesSelectActivity.this.search(null);
                RestrictedLanguagesSelectActivity.this.searching = false;
                RestrictedLanguagesSelectActivity.this.searchWas = false;
                if (RestrictedLanguagesSelectActivity.this.listView != null) {
                    RestrictedLanguagesSelectActivity.this.emptyView.setVisibility(8);
                    RestrictedLanguagesSelectActivity.this.listView.setAdapter(RestrictedLanguagesSelectActivity.this.listAdapter);
                }
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onTextChanged(EditText editText) {
                String obj = editText.getText().toString();
                RestrictedLanguagesSelectActivity.this.search(obj);
                if (obj.length() != 0) {
                    RestrictedLanguagesSelectActivity.this.searchWas = true;
                    if (RestrictedLanguagesSelectActivity.this.listView != null) {
                        RestrictedLanguagesSelectActivity.this.listView.setAdapter(RestrictedLanguagesSelectActivity.this.searchListViewAdapter);
                        return;
                    }
                    return;
                }
                RestrictedLanguagesSelectActivity.this.searching = false;
                RestrictedLanguagesSelectActivity.this.searchWas = false;
                if (RestrictedLanguagesSelectActivity.this.listView != null) {
                    RestrictedLanguagesSelectActivity.this.emptyView.setVisibility(8);
                    RestrictedLanguagesSelectActivity.this.listView.setAdapter(RestrictedLanguagesSelectActivity.this.listAdapter);
                }
            }
        }).setSearchFieldHint(LocaleController.getString("Search", R.string.Search));
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
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setEmptyView(this.emptyView);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i) {
                RestrictedLanguagesSelectActivity.this.lambda$createView$1(view, i);
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda11
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i) {
                boolean lambda$createView$3;
                lambda$createView$3 = RestrictedLanguagesSelectActivity.this.lambda$createView$3(view, i);
                return lambda$createView$3;
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity.3
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                if (i == 1) {
                    AndroidUtilities.hideKeyboard(RestrictedLanguagesSelectActivity.this.getParentActivity().getCurrentFocus());
                }
            }
        });
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view, int i) {
        LocaleController.LocaleInfo localeInfo;
        if (getParentActivity() == null || this.parentLayout == null || !(view instanceof TextCheckbox2Cell)) {
            return;
        }
        boolean z = this.listView.getAdapter() == this.searchListViewAdapter;
        int i2 = !z ? i - 1 : i;
        if (z) {
            localeInfo = this.searchResult.get(i2);
        } else {
            localeInfo = this.sortedLanguages.get(i2);
        }
        if (localeInfo != null) {
            LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
            final String str = localeInfo.pluralLangCode;
            boolean contains = this.selectedLanguages.contains(str);
            if (str != null && str.equals(currentLocaleInfo.pluralLangCode) && contains) {
                AndroidUtilities.shakeViewSpring(view);
                BotWebViewVibrationEffect.APP_ERROR.vibrate();
                return;
            }
            if (contains) {
                Collection$-EL.removeIf(this.selectedLanguages, new Predicate() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda3
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
                        boolean lambda$createView$0;
                        lambda$createView$0 = RestrictedLanguagesSelectActivity.lambda$createView$0(str, (String) obj);
                        return lambda$createView$0;
                    }
                });
            } else {
                this.selectedLanguages.add(str);
            }
            if (this.selectedLanguages.size() == 1 && this.selectedLanguages.contains(currentLocaleInfo.pluralLangCode)) {
                this.preferences.edit().remove("translate_button_restricted_languages").remove("translate_button_restricted_languages_changed").apply();
            } else {
                this.preferences.edit().putStringSet("translate_button_restricted_languages", this.selectedLanguages).putBoolean("translate_button_restricted_languages_changed", true).apply();
            }
            rebind(i);
            MessagesController.getInstance(this.currentAccount).getTranslateController().checkRestrictedLanguagesUpdate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$0(String str, String str2) {
        return str2 != null && str2.equals(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$3(View view, int i) {
        final LocaleController.LocaleInfo localeInfo;
        if (getParentActivity() != null && this.parentLayout != null && (view instanceof TextCheckbox2Cell)) {
            boolean z = this.listView.getAdapter() == this.searchListViewAdapter;
            if (!z) {
                i--;
            }
            if (z) {
                localeInfo = this.searchResult.get(i);
            } else {
                localeInfo = this.sortedLanguages.get(i);
            }
            if (localeInfo != null && localeInfo.pathToFile != null && (!localeInfo.isRemote() || localeInfo.serverIndex == Integer.MAX_VALUE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("DeleteLocalizationTitle", R.string.DeleteLocalizationTitle));
                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("DeleteLocalizationText", R.string.DeleteLocalizationText, localeInfo.name)));
                builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        RestrictedLanguagesSelectActivity.this.lambda$createView$2(localeInfo, dialogInterface, i2);
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
    public /* synthetic */ void lambda$createView$2(LocaleController.LocaleInfo localeInfo, DialogInterface dialogInterface, int i) {
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
        this.listAdapter.notifyDataSetChanged();
    }

    private void fillLanguages() {
        final LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
        Comparator comparator = new Comparator() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$fillLanguages$4;
                lambda$fillLanguages$4 = RestrictedLanguagesSelectActivity.lambda$fillLanguages$4(LocaleController.LocaleInfo.this, (LocaleController.LocaleInfo) obj, (LocaleController.LocaleInfo) obj2);
                return lambda$fillLanguages$4;
            }
        };
        this.sortedLanguages = new ArrayList<>();
        ArrayList<LocaleController.LocaleInfo> arrayList = LocaleController.getInstance().languages;
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            LocaleController.LocaleInfo localeInfo = arrayList.get(i);
            if (localeInfo != null && localeInfo.serverIndex != Integer.MAX_VALUE && !"en_raw".equals(localeInfo.shortName) && (localeInfo == currentLocaleInfo || !this.firstSelectedLanguages.contains(localeInfo.pluralLangCode))) {
                this.sortedLanguages.add(localeInfo);
            }
        }
        Collections.sort(this.sortedLanguages, comparator);
        int size2 = arrayList.size();
        for (int i2 = 0; i2 < size2; i2++) {
            LocaleController.LocaleInfo localeInfo2 = arrayList.get(i2);
            if (localeInfo2 != null && localeInfo2.serverIndex != Integer.MAX_VALUE && !"en_raw".equals(localeInfo2.shortName) && localeInfo2 != currentLocaleInfo && this.firstSelectedLanguages.contains(localeInfo2.pluralLangCode)) {
                this.sortedLanguages.add(1, localeInfo2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$fillLanguages$4(LocaleController.LocaleInfo localeInfo, LocaleController.LocaleInfo localeInfo2, LocaleController.LocaleInfo localeInfo3) {
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
            this.searchResult = null;
            return;
        }
        try {
            Timer timer = this.searchTimer;
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        processSearch(str);
    }

    private void processSearch(String str) {
        if (str.trim().toLowerCase().length() == 0) {
            updateSearchResults(new ArrayList<>());
            return;
        }
        System.currentTimeMillis();
        ArrayList<LocaleController.LocaleInfo> arrayList = new ArrayList<>();
        int size = this.sortedLanguages.size();
        for (int i = 0; i < size; i++) {
            LocaleController.LocaleInfo localeInfo = this.sortedLanguages.get(i);
            if (localeInfo.name.toLowerCase().startsWith(str) || localeInfo.nameEnglish.toLowerCase().startsWith(str)) {
                arrayList.add(localeInfo);
            }
        }
        updateSearchResults(arrayList);
    }

    private void updateSearchResults(final ArrayList<LocaleController.LocaleInfo> arrayList) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RestrictedLanguagesSelectActivity.this.lambda$updateSearchResults$5(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSearchResults$5(ArrayList arrayList) {
        this.searchResult = arrayList;
        this.searchListViewAdapter.notifyDataSetChanged();
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
                if (RestrictedLanguagesSelectActivity.this.searchResult == null) {
                    return 0;
                }
                return RestrictedLanguagesSelectActivity.this.searchResult.size();
            }
            return RestrictedLanguagesSelectActivity.this.sortedLanguages.size() + 1;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextCheckbox2Cell textCheckbox2Cell;
            View view;
            if (i == 0) {
                TextCheckbox2Cell textCheckbox2Cell2 = new TextCheckbox2Cell(this.mContext);
                textCheckbox2Cell2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                textCheckbox2Cell = textCheckbox2Cell2;
            } else if (i == 2) {
                HeaderCell headerCell = new HeaderCell(this.mContext);
                headerCell.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                headerCell.setText(LocaleController.getString("ChooseLanguages", R.string.ChooseLanguages));
                textCheckbox2Cell = headerCell;
            } else {
                view = new ShadowSectionCell(this.mContext);
                return new RecyclerListView.Holder(view);
            }
            view = textCheckbox2Cell;
            return new RecyclerListView.Holder(view);
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x0052, code lost:
            if (r9 == (r7.this$0.searchResult.size() - 1)) goto L19;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0054, code lost:
            r9 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x007c, code lost:
            if (r9 == (r7.this$0.sortedLanguages.size() - 1)) goto L19;
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
                    return;
                }
                ((ShadowSectionCell) viewHolder.itemView).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, "windowBackgroundGrayShadow"));
                return;
            }
            boolean z2 = this.search;
            if (!z2) {
                i--;
            }
            TextCheckbox2Cell textCheckbox2Cell = (TextCheckbox2Cell) viewHolder.itemView;
            LocaleController.LocaleInfo localeInfo = null;
            if (z2) {
                if (i >= 0 && i < RestrictedLanguagesSelectActivity.this.searchResult.size()) {
                    localeInfo = (LocaleController.LocaleInfo) RestrictedLanguagesSelectActivity.this.searchResult.get(i);
                }
            } else {
                if (i >= 0 && i < RestrictedLanguagesSelectActivity.this.sortedLanguages.size()) {
                    localeInfo = (LocaleController.LocaleInfo) RestrictedLanguagesSelectActivity.this.sortedLanguages.get(i);
                }
                z = false;
            }
            if (localeInfo == null) {
                return;
            }
            boolean contains = RestrictedLanguagesSelectActivity.this.selectedLanguages.contains(localeInfo.pluralLangCode);
            if (localeInfo.isLocal()) {
                textCheckbox2Cell.setTextAndValue(String.format("%1$s (%2$s)", localeInfo.name, LocaleController.getString("LanguageCustom", R.string.LanguageCustom)), localeInfo.nameEnglish, false, !z);
            } else {
                textCheckbox2Cell.setTextAndValue(localeInfo.name, localeInfo.nameEnglish, false, !z);
            }
            textCheckbox2Cell.setChecked(contains);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (!this.search) {
                i--;
            }
            return i == -1 ? 2 : 0;
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

    public static void cleanup() {
        MessagesController.getGlobalMainSettings().edit().remove("translate_button_restricted_languages_changed").remove("translate_button_restricted_languages_version").remove("translate_button_restricted_languages").apply();
        checkRestrictedLanguages(false);
    }

    public static void checkRestrictedLanguages(boolean z) {
        boolean z2 = MessagesController.getGlobalMainSettings().getBoolean("translate_button_restricted_languages_changed", false);
        if (MessagesController.getGlobalMainSettings().getInt("translate_button_restricted_languages_version", 0) != 2 || (z && !z2)) {
            getExtendedDoNotTranslate(RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda9.INSTANCE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkRestrictedLanguages$6(HashSet hashSet) {
        String str = LocaleController.getInstance().getCurrentLocaleInfo().pluralLangCode;
        hashSet.addAll(getRestrictedLanguages());
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        if (hashSet.size() == 1 && TextUtils.equals((CharSequence) hashSet.iterator().next(), str)) {
            edit.remove("translate_button_restricted_languages");
        } else {
            edit.putStringSet("translate_button_restricted_languages", hashSet);
        }
        edit.putInt("translate_button_restricted_languages_version", 2).apply();
        for (int i = 0; i < 4; i++) {
            try {
                MessagesController.getInstance(i).getTranslateController().checkRestrictedLanguagesUpdate();
            } catch (Exception unused) {
            }
        }
    }

    public static void getExtendedDoNotTranslate(final Utilities.Callback<HashSet<String>> callback) {
        if (callback == null) {
            return;
        }
        final HashSet hashSet = new HashSet();
        final HashMap hashMap = new HashMap();
        final HashMap hashMap2 = new HashMap();
        Utilities.doCallbacks(new Utilities.Callback() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda6
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                RestrictedLanguagesSelectActivity.lambda$getExtendedDoNotTranslate$7(hashSet, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                RestrictedLanguagesSelectActivity.lambda$getExtendedDoNotTranslate$8(hashSet, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda4
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                RestrictedLanguagesSelectActivity.lambda$getExtendedDoNotTranslate$9(hashMap, hashMap2, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda5
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                RestrictedLanguagesSelectActivity.lambda$getExtendedDoNotTranslate$10(hashSet, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda8
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                Runnable runnable = (Runnable) obj;
                Utilities.Callback.this.run(hashSet);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getExtendedDoNotTranslate$7(HashSet hashSet, Runnable runnable) {
        try {
            String str = LocaleController.getInstance().getCurrentLocaleInfo().pluralLangCode;
            if (TranslateAlert2.languageName(str) != null) {
                hashSet.add(str);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getExtendedDoNotTranslate$8(HashSet hashSet, Runnable runnable) {
        try {
            String language = Resources.getSystem().getConfiguration().locale.getLanguage();
            if (TranslateAlert2.languageName(language) != null) {
                hashSet.add(language);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getExtendedDoNotTranslate$9(HashMap hashMap, HashMap hashMap2, Runnable runnable) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt")));
            ArrayList arrayList = new ArrayList();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split(";");
                if (split.length >= 3) {
                    hashMap.put(split[2], split[1]);
                    if (hashMap2.containsKey(split[0]) && !"7".equals(split[0])) {
                        arrayList.add(split[0]);
                        hashMap2.remove(split[0]);
                    } else if (!arrayList.contains(split[0])) {
                        hashMap2.put(split[0], split[1]);
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getExtendedDoNotTranslate$10(HashSet hashSet, Runnable runnable) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) ApplicationLoader.applicationContext.getSystemService("input_method");
            for (InputMethodInfo inputMethodInfo : inputMethodManager.getEnabledInputMethodList()) {
                for (InputMethodSubtype inputMethodSubtype : inputMethodManager.getEnabledInputMethodSubtypeList(inputMethodInfo, true)) {
                    if ("keyboard".equals(inputMethodSubtype.getMode())) {
                        String locale = inputMethodSubtype.getLocale();
                        if (locale != null && locale.contains("_")) {
                            locale = locale.split("_")[0];
                        }
                        if (TranslateAlert2.languageName(locale) != null) {
                            hashSet.add(locale);
                        }
                    }
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        runnable.run();
    }
}
