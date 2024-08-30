package org.telegram.ui.web;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.ui.Components.UniversalRecyclerView;
import org.telegram.ui.web.BrowserHistory;
import org.telegram.ui.web.WebMetadataCache;
/* loaded from: classes.dex */
public class WebBrowserSettings extends UniversalFragment implements NotificationCenter.NotificationCenterDelegate {
    private Drawable addIcon;
    private long cacheSize;
    private long cookiesSize;
    private long historySize;
    private Utilities.Callback whenHistoryClicked;

    /* loaded from: classes.dex */
    public static class WebsiteView extends FrameLayout {
        private ArrayList domains;
        public final ImageView imageView;
        private boolean needDivider;
        public final ImageView optionsView;
        public final TextView subtitleView;
        public final TextView titleView;

        /* loaded from: classes.dex */
        public static class Factory extends UItem.UItemFactory {
            static {
                UItem.UItemFactory.setup(new Factory());
            }

            public static UItem as(ArrayList arrayList, String str, Bitmap bitmap) {
                UItem ofFactory = UItem.ofFactory(Factory.class);
                ofFactory.text = str;
                ofFactory.object = bitmap;
                ofFactory.object2 = arrayList;
                return ofFactory;
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public void bindView(View view, UItem uItem, boolean z) {
                WebsiteView websiteView = (WebsiteView) view;
                CharSequence charSequence = uItem.text;
                ArrayList arrayList = (ArrayList) uItem.object2;
                Object obj = uItem.object;
                websiteView.set(charSequence, arrayList, obj instanceof Bitmap ? (Bitmap) obj : null, z);
            }

            @Override // org.telegram.ui.Components.UItem.UItemFactory
            public WebsiteView createView(Context context, int i, int i2, Theme.ResourcesProvider resourcesProvider) {
                return new WebsiteView(context);
            }
        }

        public WebsiteView(Context context) {
            super(context);
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            addView(imageView, LayoutHelper.createFrame(28, 28.0f, 19, 18.0f, 0.0f, 0.0f, 0.0f));
            TextView textView = new TextView(context);
            this.titleView = textView;
            textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            textView.setTextSize(1, 16.0f);
            textView.setTypeface(AndroidUtilities.bold());
            textView.setMaxLines(1);
            TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
            textView.setEllipsize(truncateAt);
            addView(textView, LayoutHelper.createFrame(-1, -2.0f, 55, 68.0f, 7.0f, 54.0f, 0.0f));
            TextView textView2 = new TextView(context) { // from class: org.telegram.ui.web.WebBrowserSettings.WebsiteView.1
                @Override // android.widget.TextView, android.view.View
                protected void onMeasure(int i, int i2) {
                    super.onMeasure(i, i2);
                    WebsiteView.this.subtitleView.setPivotY(getMeasuredHeight() / 2.0f);
                }
            };
            this.subtitleView = textView2;
            textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            textView2.setTextSize(1, 13.0f);
            textView2.setMaxLines(1);
            textView2.setEllipsize(truncateAt);
            textView2.setPivotX(0.0f);
            addView(textView2, LayoutHelper.createFrame(-1, -2.0f, 55, 68.0f, 30.0f, 54.0f, 0.0f));
            ImageView imageView2 = new ImageView(context);
            this.optionsView = imageView2;
            imageView2.setScaleType(ImageView.ScaleType.CENTER);
            imageView2.setImageResource(R.drawable.ic_ab_other);
            imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.SRC_IN));
            addView(imageView2, LayoutHelper.createFrame(32, 32.0f, 21, 0.0f, 0.0f, 18.0f, 0.0f));
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            canvas.drawRect(AndroidUtilities.dp(64.0f), getHeight() - 1, getWidth(), getHeight(), Theme.dividerPaint);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f), 1073741824));
        }

        public void set(CharSequence charSequence, ArrayList arrayList, Bitmap bitmap, boolean z) {
            TextView textView;
            float f;
            this.titleView.setText(charSequence);
            StringBuilder sb = new StringBuilder();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(str);
            }
            this.subtitleView.setText(sb);
            if (TextUtils.isEmpty(charSequence)) {
                this.subtitleView.setTranslationY(-AndroidUtilities.dp(14.0f));
                textView = this.subtitleView;
                f = 1.3f;
            } else {
                this.subtitleView.setTranslationY(0.0f);
                textView = this.subtitleView;
                f = 1.0f;
            }
            textView.setScaleX(f);
            this.subtitleView.setScaleY(f);
            this.domains = arrayList;
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = (arrayList.isEmpty() || TextUtils.isEmpty((CharSequence) arrayList.get(0))) ? "" : (String) arrayList.get(0);
            }
            String charSequence2 = charSequence.toString();
            if (bitmap != null) {
                this.imageView.setImageBitmap(bitmap);
            } else {
                CombinedDrawable combinedDrawable = new CombinedDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.multAlpha(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText), 0.1f)), new Drawable(charSequence2) { // from class: org.telegram.ui.web.WebBrowserSettings.WebsiteView.2
                    private final Text text;
                    final /* synthetic */ String val$s;

                    {
                        this.val$s = charSequence2;
                        this.text = new Text(charSequence2.substring(0, !charSequence2.isEmpty()), 14.0f, AndroidUtilities.bold());
                    }

                    @Override // android.graphics.drawable.Drawable
                    public void draw(Canvas canvas) {
                        this.text.draw(canvas, getBounds().centerX() - (this.text.getCurrentWidth() / 2.0f), getBounds().centerY(), Theme.getColor(Theme.key_windowBackgroundWhiteBlackText), 1.0f);
                    }

                    @Override // android.graphics.drawable.Drawable
                    public int getOpacity() {
                        return -2;
                    }

                    @Override // android.graphics.drawable.Drawable
                    public void setAlpha(int i) {
                    }

                    @Override // android.graphics.drawable.Drawable
                    public void setColorFilter(ColorFilter colorFilter) {
                    }
                });
                combinedDrawable.setCustomSize(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f));
                this.imageView.setImageDrawable(combinedDrawable);
            }
            if (this.needDivider != z) {
                invalidate();
            }
            this.needDivider = z;
            setWillNotDraw(!z);
        }
    }

    public WebBrowserSettings(Utilities.Callback callback) {
        this.whenHistoryClicked = callback;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x003c, code lost:
        if (r6 != false) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean deleteDirectory(File file, Boolean bool) {
        boolean z;
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                z = true;
                for (File file2 : listFiles) {
                    if ((bool == null || bool.booleanValue() == file2.getName().startsWith("Cookies")) && !deleteDirectory(file2, bool)) {
                        z = false;
                    }
                }
            } else {
                z = true;
            }
        } else if (bool != null && bool.booleanValue() != file.getName().startsWith("Cookies")) {
            return false;
        }
        file.delete();
        return true;
    }

    private static long getDirectorySize(File file, Boolean bool) {
        long j = 0;
        if (file == null || !file.exists()) {
            return 0L;
        }
        if (!file.isDirectory()) {
            if (bool == null || bool.booleanValue() == file.getName().startsWith("Cookies")) {
                return file.length();
            }
            return 0L;
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                j += getDirectorySize(file2, bool);
            }
            return j;
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSizes$0(ArrayList arrayList) {
        this.historySize = arrayList.size();
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView == null || universalRecyclerView.adapter == null || !universalRecyclerView.isAttachedToWindow()) {
            return;
        }
        this.listView.adapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSizes$1(long j, long j2) {
        this.cacheSize = j;
        this.cookiesSize = j2;
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView == null || universalRecyclerView.adapter == null || !universalRecyclerView.isAttachedToWindow()) {
            return;
        }
        this.listView.adapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSizes$2() {
        File databasePath = ApplicationLoader.applicationContext.getDatabasePath("webview.db");
        long length = (databasePath == null || !databasePath.exists()) ? 0L : databasePath.length();
        File databasePath2 = ApplicationLoader.applicationContext.getDatabasePath("webviewCache.db");
        if (databasePath2 != null && databasePath2.exists()) {
            length += databasePath2.length();
        }
        File file = new File(ApplicationLoader.applicationContext.getApplicationInfo().dataDir, "app_webview");
        if (file.exists()) {
            length += getDirectorySize(file, Boolean.FALSE);
        }
        File file2 = new File(ApplicationLoader.applicationContext.getApplicationInfo().dataDir, "cache/WebView");
        if (file2.exists()) {
            length += getDirectorySize(file2, null);
        }
        final long j = length;
        File file3 = new File(ApplicationLoader.applicationContext.getApplicationInfo().dataDir, "app_webview");
        final long directorySize = file3.exists() ? getDirectorySize(file3, Boolean.TRUE) : 0L;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                WebBrowserSettings.this.lambda$loadSizes$1(j, directorySize);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$10(Runnable runnable, AlertDialog alertDialog, String str, String str2, Bitmap bitmap) {
        AndroidUtilities.cancelRunOnUIThread(runnable);
        alertDialog.dismissUnless(800L);
        if (WebMetadataCache.getInstance().get(str) != null) {
            this.listView.adapter.update(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$11(EditTextBoldCursor editTextBoldCursor, final AlertDialog[] alertDialogArr) {
        String obj = editTextBoldCursor.getText().toString();
        Uri parse = Uri.parse(obj);
        if (parse == null || parse.getHost() == null) {
            parse = Uri.parse("https://" + obj);
        }
        if (parse == null || parse.getHost() == null) {
            AndroidUtilities.shakeView(editTextBoldCursor);
            return;
        }
        final String lowerCase = parse.getHost().toLowerCase();
        if (lowerCase.startsWith("www.")) {
            lowerCase = lowerCase.substring(4);
        }
        RestrictedDomainsList.getInstance().setRestricted(true, lowerCase);
        WebMetadataCache.WebMetadata webMetadata = WebMetadataCache.getInstance().get(lowerCase);
        if (webMetadata != null && !TextUtils.isEmpty(webMetadata.sitename) && webMetadata.favicon != null) {
            AlertDialog alertDialog = alertDialogArr[0];
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            this.listView.adapter.update(true);
            return;
        }
        final AlertDialog alertDialog2 = new AlertDialog(getContext(), 3);
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                WebBrowserSettings.this.lambda$onClick$9(alertDialogArr, alertDialog2);
            }
        };
        AndroidUtilities.runOnUIThread(runnable, 5000L);
        alertDialog2.showDelayed(300L);
        WebMetadataCache.retrieveFaviconAndSitename("https://" + obj + "/", new Utilities.Callback2() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda15
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj2, Object obj3) {
                WebBrowserSettings.this.lambda$onClick$10(runnable, alertDialog2, lowerCase, (String) obj2, (Bitmap) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onClick$15(EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface) {
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$3(DialogInterface dialogInterface, int i) {
        ApplicationLoader.applicationContext.deleteDatabase("webview.db");
        ApplicationLoader.applicationContext.deleteDatabase("webviewCache.db");
        WebStorage.getInstance().deleteAllData();
        try {
            WebView webView = new WebView(getContext());
            webView.clearCache(true);
            webView.clearHistory();
            webView.destroy();
        } catch (Exception unused) {
        }
        try {
            File file = new File(ApplicationLoader.applicationContext.getApplicationInfo().dataDir, "app_webview");
            if (file.exists()) {
                deleteDirectory(file, Boolean.FALSE);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            File file2 = new File(ApplicationLoader.applicationContext.getApplicationInfo().dataDir, "cache/WebView");
            if (file2.exists()) {
                deleteDirectory(file2, null);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        WebMetadataCache.getInstance().clear();
        loadSizes();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$4(DialogInterface dialogInterface, int i) {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.removeAllCookies(null);
            cookieManager.flush();
        }
        try {
            File file = new File(ApplicationLoader.applicationContext.getApplicationInfo().dataDir, "app_webview");
            if (file.exists()) {
                deleteDirectory(file, Boolean.TRUE);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        loadSizes();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$5(DialogInterface dialogInterface, int i) {
        BrowserHistory.clearHistory();
        this.historySize = 0L;
        this.listView.adapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$6(HistoryFragment[] historyFragmentArr, BrowserHistory.Entry entry) {
        historyFragmentArr[0].finishFragment();
        if (this.whenHistoryClicked == null) {
            Browser.openUrl(getContext(), entry.url);
            return;
        }
        finishFragment();
        this.whenHistoryClicked.run(entry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$7(ArrayList arrayList) {
        RestrictedDomainsList.getInstance().setRestricted(false, (String[]) arrayList.toArray(new String[0]));
        this.listView.adapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onClick$8(int i, View view, AtomicReference atomicReference, View view2) {
        SharedConfig.setSearchEngineType(i);
        ((TextCell) view).setValue(SearchEngine.getCurrent().name, true);
        ((Dialog) atomicReference.get()).dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$9(AlertDialog[] alertDialogArr, AlertDialog alertDialog) {
        alertDialogArr[0].dismiss();
        alertDialog.dismissUnless(800L);
        this.listView.adapter.update(true);
    }

    private void loadSizes() {
        ArrayList history = BrowserHistory.getHistory(new Utilities.Callback() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                WebBrowserSettings.this.lambda$loadSizes$0((ArrayList) obj);
            }
        });
        if (history != null) {
            this.historySize = history.size();
            UniversalRecyclerView universalRecyclerView = this.listView;
            if (universalRecyclerView != null && universalRecyclerView.adapter != null && universalRecyclerView.isAttachedToWindow()) {
                this.listView.adapter.update(true);
            }
        }
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                WebBrowserSettings.this.lambda$loadSizes$2();
            }
        });
    }

    @Override // org.telegram.ui.Components.UniversalFragment, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        Drawable mutate = context.getResources().getDrawable(R.drawable.poll_add_circle).mutate();
        Drawable mutate2 = context.getResources().getDrawable(R.drawable.poll_add_plus).mutate();
        int themedColor = getThemedColor(Theme.key_switchTrackChecked);
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        mutate.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
        mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_checkboxCheck), mode));
        this.addIcon = new CombinedDrawable(mutate, mutate2) { // from class: org.telegram.ui.web.WebBrowserSettings.1
            {
                this.translateX = AndroidUtilities.dp(2.0f);
            }

            @Override // org.telegram.ui.Components.CombinedDrawable, android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }
        };
        return super.createView(context);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        UniversalRecyclerView universalRecyclerView;
        if (i != NotificationCenter.webViewResolved || (universalRecyclerView = this.listView) == null) {
            return;
        }
        universalRecyclerView.adapter.update(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0119  */
    @Override // org.telegram.ui.Components.UniversalFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fillItems(ArrayList arrayList, UniversalAdapter universalAdapter) {
        String str;
        arrayList.add(UItem.asRippleCheck(1, LocaleController.getString(R.string.BrowserSettingsEnable)).setChecked(SharedConfig.inappBrowser));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.BrowserSettingsEnableInfo)));
        int i = R.drawable.menu_clear_cookies;
        String string = LocaleController.getString(R.string.BrowserSettingsCookiesClear);
        long j = this.cookiesSize;
        arrayList.add(UItem.asButton(3, i, string, j > 0 ? AndroidUtilities.formatFileSize(j) : ""));
        int i2 = R.drawable.menu_clear_cache;
        String string2 = LocaleController.getString(R.string.BrowserSettingsCacheClear);
        long j2 = this.cacheSize;
        arrayList.add(UItem.asButton(2, i2, string2, j2 > 0 ? AndroidUtilities.formatFileSize(j2) : ""));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.BrowserSettingsCookiesInfo)));
        if (this.historySize > 0) {
            arrayList.add(UItem.asButton(9, R.drawable.menu_clear_recent, LocaleController.getString(R.string.BrowserSettingsHistoryShow)));
            arrayList.add(UItem.asButton(7, i2, LocaleController.getString(R.string.BrowserSettingsHistoryClear), LocaleController.formatPluralStringComma("BrowserSettingsHistoryPages", (int) this.historySize, ',')));
            arrayList.add(UItem.asShadow(null));
        }
        arrayList.add(UItem.asHeader(LocaleController.getString(R.string.BrowserSettingsNeverOpenInTitle)));
        arrayList.add(UItem.asButton(4, this.addIcon, LocaleController.getString(R.string.BrowserSettingsNeverOpenInAdd)).accent());
        RestrictedDomainsList.getInstance().load();
        ArrayList arrayList2 = RestrictedDomainsList.getInstance().restrictedDomains;
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            ArrayList arrayList3 = (ArrayList) it.next();
            Iterator it2 = arrayList3.iterator();
            WebMetadataCache.WebMetadata webMetadata = null;
            while (it2.hasNext()) {
                webMetadata = WebMetadataCache.getInstance().get((String) it2.next());
                if (webMetadata != null) {
                    break;
                }
            }
            if (webMetadata != null) {
                if (!TextUtils.isEmpty(webMetadata.sitename)) {
                    str = webMetadata.sitename;
                } else if (!TextUtils.isEmpty(webMetadata.title)) {
                    str = webMetadata.title;
                }
                arrayList.add(WebsiteView.Factory.as(arrayList3, str, webMetadata != null ? null : webMetadata.favicon));
            }
            str = "";
            arrayList.add(WebsiteView.Factory.as(arrayList3, str, webMetadata != null ? null : webMetadata.favicon));
        }
        if (!arrayList2.isEmpty()) {
            arrayList.add(UItem.asButton(5, R.drawable.msg_clearcache, LocaleController.getString(R.string.BrowserSettingsNeverOpenInClearList)).red());
        }
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.BrowserSettingsNeverOpenInInfo)));
        arrayList.add(UItem.asButton(6, R.drawable.msg_search, LocaleController.getString(R.string.SearchEngine), SearchEngine.getCurrent().name));
        arrayList.add(UItem.asShadow(LocaleController.getString(R.string.BrowserSettingsSearchEngineInfo)));
        if (!SharedConfig.inappBrowser) {
            arrayList.add(UItem.asHeader(LocaleController.getString(R.string.BrowserSettingsCustomTabsTitle)));
            arrayList.add(UItem.asRadio(10, LocaleController.getString(R.string.BrowserSettingsCustomTabs)).setChecked(SharedConfig.customTabs));
            arrayList.add(UItem.asRadio(11, LocaleController.getString(R.string.BrowserSettingsNoCustomTabs)).setChecked(true ^ SharedConfig.customTabs));
            arrayList.add(UItem.asShadow(LocaleController.getString(R.string.BrowserSettingsNoCustomTabsInfo)));
        }
        if (BuildVars.DEBUG_PRIVATE_VERSION) {
            arrayList.add(UItem.asCheck(12, "adaptable colors").setChecked(SharedConfig.adaptableColorInBrowser));
        }
    }

    @Override // org.telegram.ui.Components.UniversalFragment
    protected CharSequence getTitle() {
        return LocaleController.getString(R.string.BrowserSettingsTitle);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return super.isLightStatusBar();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.UniversalFragment
    public void onClick(UItem uItem, final View view, int i, float f, float f2) {
        AlertDialog.Builder message;
        String string;
        DialogInterface.OnClickListener onClickListener;
        int i2 = uItem.id;
        if (i2 == 12) {
            SharedConfig.toggleBrowserAdaptableColors();
            ((TextCheckCell) view).setChecked(SharedConfig.adaptableColorInBrowser);
            return;
        }
        if (i2 == 1) {
            SharedConfig.toggleInappBrowser();
            TextCheckCell textCheckCell = (TextCheckCell) view;
            textCheckCell.setChecked(SharedConfig.inappBrowser);
            boolean z = SharedConfig.inappBrowser;
            textCheckCell.setBackgroundColorAnimated(z, Theme.getColor(z ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
        } else if (i2 == 10) {
            SharedConfig.toggleCustomTabs(true);
        } else if (i2 != 11) {
            String str = "";
            if (i2 == 2) {
                AlertDialog.Builder title = new AlertDialog.Builder(getContext(), getResourceProvider()).setTitle(LocaleController.getString(R.string.BrowserSettingsCacheClear));
                int i3 = R.string.BrowserSettingsCacheClearText;
                if (this.cacheSize != 0) {
                    str = " (" + AndroidUtilities.formatFileSize(this.cacheSize) + ")";
                }
                message = title.setMessage(LocaleController.formatString(i3, str));
                string = LocaleController.getString(R.string.Clear);
                onClickListener = new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda3
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        WebBrowserSettings.this.lambda$onClick$3(dialogInterface, i4);
                    }
                };
            } else if (i2 == 3) {
                AlertDialog.Builder title2 = new AlertDialog.Builder(getContext(), getResourceProvider()).setTitle(LocaleController.getString(R.string.BrowserSettingsCookiesClear));
                int i4 = R.string.BrowserSettingsCookiesClearText;
                if (this.cookiesSize != 0) {
                    str = " (" + AndroidUtilities.formatFileSize(this.cookiesSize) + ")";
                }
                message = title2.setMessage(LocaleController.formatString(i4, str));
                string = LocaleController.getString(R.string.Clear);
                onClickListener = new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda5
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        WebBrowserSettings.this.lambda$onClick$4(dialogInterface, i5);
                    }
                };
            } else if (i2 == 7) {
                Iterator it = BrowserHistory.getHistory().iterator();
                long j = Long.MAX_VALUE;
                while (it.hasNext()) {
                    j = Math.min(j, ((BrowserHistory.Entry) it.next()).time);
                }
                message = new AlertDialog.Builder(getContext(), getResourceProvider()).setTitle(LocaleController.getString(R.string.BrowserSettingsHistoryClear)).setMessage(LocaleController.formatString(R.string.BrowserSettingsHistoryClearText, LocaleController.formatDateChat(j / 1000)));
                string = LocaleController.getString(R.string.Clear);
                onClickListener = new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda6
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i5) {
                        WebBrowserSettings.this.lambda$onClick$5(dialogInterface, i5);
                    }
                };
            } else if (i2 == 9) {
                final HistoryFragment[] historyFragmentArr = {null};
                HistoryFragment historyFragment = new HistoryFragment(null, new Utilities.Callback() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda7
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        WebBrowserSettings.this.lambda$onClick$6(historyFragmentArr, (BrowserHistory.Entry) obj);
                    }
                });
                historyFragmentArr[0] = historyFragment;
                presentFragment(historyFragment);
                return;
            } else if (i2 != 5) {
                if (uItem.instanceOf(WebsiteView.Factory.class)) {
                    WebsiteView websiteView = (WebsiteView) view;
                    final ArrayList arrayList = websiteView.domains;
                    ItemOptions.makeOptions((ViewGroup) this.fragmentView, websiteView).add(R.drawable.menu_delete_old, LocaleController.getString(R.string.Remove), new Runnable() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            WebBrowserSettings.this.lambda$onClick$7(arrayList);
                        }
                    }).show();
                    return;
                }
                int i5 = uItem.id;
                if (i5 == 6) {
                    if (getParentActivity() == null) {
                        return;
                    }
                    final AtomicReference atomicReference = new AtomicReference();
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setOrientation(1);
                    ArrayList searchEngines = SearchEngine.getSearchEngines();
                    int size = searchEngines.size();
                    CharSequence[] charSequenceArr = new CharSequence[size];
                    final int i6 = 0;
                    while (i6 < size) {
                        charSequenceArr[i6] = ((SearchEngine) searchEngines.get(i6)).name;
                        RadioColorCell radioColorCell = new RadioColorCell(getParentActivity());
                        radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                        radioColorCell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
                        radioColorCell.setTextAndValue(charSequenceArr[i6], i6 == SharedConfig.searchEngineType);
                        radioColorCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
                        linearLayout.addView(radioColorCell);
                        radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda9
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                WebBrowserSettings.lambda$onClick$8(i6, view, atomicReference, view2);
                            }
                        });
                        i6++;
                    }
                    AlertDialog create = new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString(R.string.SearchEngine)).setView(linearLayout).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create();
                    atomicReference.set(create);
                    showDialog(create);
                    return;
                } else if (i5 == 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), getResourceProvider());
                    builder.setTitle(LocaleController.getString(R.string.BrowserSettingsAddTitle));
                    LinearLayout linearLayout2 = new LinearLayout(getContext());
                    linearLayout2.setOrientation(1);
                    TextView textView = new TextView(getContext());
                    int i7 = Theme.key_dialogTextBlack;
                    textView.setTextColor(Theme.getColor(i7, getResourceProvider()));
                    textView.setTextSize(1, 16.0f);
                    textView.setText(LocaleController.getString(R.string.BrowserSettingsAddText));
                    linearLayout2.addView(textView, LayoutHelper.createLinear(-1, -2, 24.0f, 5.0f, 24.0f, 12.0f));
                    final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(getContext()) { // from class: org.telegram.ui.web.WebBrowserSettings.2
                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
                        public void onMeasure(int i8, int i9) {
                            super.onMeasure(i8, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(36.0f), 1073741824));
                        }
                    };
                    final Runnable runnable = new Runnable() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            WebBrowserSettings.this.lambda$onClick$11(editTextBoldCursor, r3);
                        }
                    };
                    editTextBoldCursor.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.web.WebBrowserSettings.3
                        @Override // android.widget.TextView.OnEditorActionListener
                        public boolean onEditorAction(TextView textView2, int i8, KeyEvent keyEvent) {
                            if (i8 == 6) {
                                runnable.run();
                                return true;
                            }
                            return false;
                        }
                    });
                    editTextBoldCursor.setTextSize(1, 18.0f);
                    editTextBoldCursor.setText("");
                    editTextBoldCursor.setTextColor(Theme.getColor(i7, getResourceProvider()));
                    editTextBoldCursor.setHintColor(Theme.getColor(Theme.key_groupcreate_hintText, getResourceProvider()));
                    editTextBoldCursor.setHintText(LocaleController.getString(R.string.BrowserSettingsAddHint));
                    editTextBoldCursor.setSingleLine(true);
                    editTextBoldCursor.setFocusable(true);
                    editTextBoldCursor.setInputType(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM);
                    editTextBoldCursor.setLineColors(Theme.getColor(Theme.key_windowBackgroundWhiteInputField, getResourceProvider()), Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated, getResourceProvider()), Theme.getColor(Theme.key_text_RedRegular, getResourceProvider()));
                    editTextBoldCursor.setImeOptions(6);
                    editTextBoldCursor.setBackgroundDrawable(null);
                    editTextBoldCursor.setPadding(0, 0, AndroidUtilities.dp(42.0f), 0);
                    linearLayout2.addView(editTextBoldCursor, LayoutHelper.createLinear(-1, -2, 24.0f, 0.0f, 24.0f, 10.0f));
                    builder.setView(linearLayout2);
                    builder.setWidth(AndroidUtilities.dp(292.0f));
                    builder.setPositiveButton(LocaleController.getString(R.string.Done), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda11
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i8) {
                            runnable.run();
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda12
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i8) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog create2 = builder.create();
                    final AlertDialog[] alertDialogArr = {create2};
                    create2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda13
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            AndroidUtilities.hideKeyboard(EditTextBoldCursor.this);
                        }
                    });
                    alertDialogArr[0].setOnShowListener(new DialogInterface.OnShowListener() { // from class: org.telegram.ui.web.WebBrowserSettings$$ExternalSyntheticLambda4
                        @Override // android.content.DialogInterface.OnShowListener
                        public final void onShow(DialogInterface dialogInterface) {
                            WebBrowserSettings.lambda$onClick$15(EditTextBoldCursor.this, dialogInterface);
                        }
                    });
                    alertDialogArr[0].setDismissDialogByButtons(false);
                    alertDialogArr[0].show();
                    return;
                } else {
                    return;
                }
            } else {
                RestrictedDomainsList.getInstance().restrictedDomains.clear();
                RestrictedDomainsList.getInstance().scheduleSave();
            }
            message.setPositiveButton(string, onClickListener).setNegativeButton(LocaleController.getString(R.string.Cancel), null).makeRed(-1).show();
            return;
        } else {
            SharedConfig.toggleCustomTabs(false);
        }
        this.listView.adapter.update(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        loadSizes();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.webViewResolved);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.webViewResolved);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.UniversalFragment
    public boolean onLongClick(UItem uItem, View view, int i, float f, float f2) {
        return false;
    }
}
