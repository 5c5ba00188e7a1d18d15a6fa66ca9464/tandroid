package androidx.appcompat.app;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$color;
import androidx.appcompat.R$id;
import androidx.appcompat.R$layout;
import androidx.appcompat.R$style;
import androidx.appcompat.R$styleable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.StandaloneActionMode;
import androidx.appcompat.view.SupportActionModeWrapper;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.DecorContentParent;
import androidx.appcompat.widget.FitWindowsViewGroup;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.appcompat.widget.ViewStubCompat;
import androidx.appcompat.widget.ViewUtils;
import androidx.collection.SimpleArrayMap;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.KeyEventDispatcher;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.PopupWindowCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.lang.Thread;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MediaController;
import org.webrtc.MediaStreamTrack;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.Callback, LayoutInflater.Factory2 {
    private static final boolean IS_PRE_LOLLIPOP;
    private static final boolean sCanApplyOverrideConfiguration;
    private static final boolean sCanReturnDifferentContext;
    private static boolean sInstalledExceptionHandler;
    private static final SimpleArrayMap sLocalNightModes = new SimpleArrayMap();
    private static final int[] sWindowBackgroundStyleable;
    ActionBar mActionBar;
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    ActionMode mActionMode;
    PopupWindow mActionModePopup;
    ActionBarContextView mActionModeView;
    private int mActivityHandlesConfigFlags;
    private boolean mActivityHandlesConfigFlagsChecked;
    final AppCompatCallback mAppCompatCallback;
    private AppCompatViewInflater mAppCompatViewInflater;
    private AppCompatWindowCallback mAppCompatWindowCallback;
    private AutoNightModeManager mAutoBatteryNightModeManager;
    private AutoNightModeManager mAutoTimeNightModeManager;
    private OnBackInvokedCallback mBackCallback;
    private boolean mBaseContextAttached;
    private boolean mClosingActionMenu;
    final Context mContext;
    private boolean mCreated;
    private DecorContentParent mDecorContentParent;
    boolean mDestroyed;
    private OnBackInvokedDispatcher mDispatcher;
    private Configuration mEffectiveConfiguration;
    private boolean mEnableDefaultActionBarUp;
    ViewPropertyAnimatorCompat mFadeAnim;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    private boolean mHandleNativeActionModes;
    boolean mHasActionBar;
    final Object mHost;
    int mInvalidatePanelMenuFeatures;
    boolean mInvalidatePanelMenuPosted;
    private final Runnable mInvalidatePanelMenuRunnable;
    boolean mIsFloating;
    private LayoutIncludeDetector mLayoutIncludeDetector;
    private int mLocalNightMode;
    private boolean mLongPressBackDown;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    Runnable mShowActionModePopup;
    private View mStatusGuard;
    ViewGroup mSubDecor;
    private boolean mSubDecorInstalled;
    private Rect mTempRect1;
    private Rect mTempRect2;
    private int mThemeResId;
    private CharSequence mTitle;
    private TextView mTitleView;
    Window mWindow;
    boolean mWindowNoTitle;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
        ActionMenuPresenterCallback() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            AppCompatDelegateImpl.this.checkCloseActionMenu(menuBuilder);
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback windowCallback = AppCompatDelegateImpl.this.getWindowCallback();
            if (windowCallback == null) {
                return true;
            }
            windowCallback.onMenuOpened(108, menuBuilder);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
        private ActionMode.Callback mWrapped;

        public ActionModeCallbackWrapperV9(ActionMode.Callback callback) {
            this.mWrapped = callback;
        }

        @Override // androidx.appcompat.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override // androidx.appcompat.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override // androidx.appcompat.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (appCompatDelegateImpl.mActionModePopup != null) {
                appCompatDelegateImpl.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup);
            }
            AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
            if (appCompatDelegateImpl2.mActionModeView != null) {
                appCompatDelegateImpl2.endOnGoingFadeAnimation();
                AppCompatDelegateImpl appCompatDelegateImpl3 = AppCompatDelegateImpl.this;
                appCompatDelegateImpl3.mFadeAnim = ViewCompat.animate(appCompatDelegateImpl3.mActionModeView).alpha(0.0f);
                AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.ActionModeCallbackWrapperV9.1
                    @Override // androidx.core.view.ViewPropertyAnimatorListener
                    public void onAnimationEnd(View view) {
                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                        AppCompatDelegateImpl appCompatDelegateImpl4 = AppCompatDelegateImpl.this;
                        PopupWindow popupWindow = appCompatDelegateImpl4.mActionModePopup;
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        } else if (appCompatDelegateImpl4.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View) AppCompatDelegateImpl.this.mActionModeView.getParent());
                        }
                        AppCompatDelegateImpl.this.mActionModeView.killMode();
                        AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                        AppCompatDelegateImpl appCompatDelegateImpl5 = AppCompatDelegateImpl.this;
                        appCompatDelegateImpl5.mFadeAnim = null;
                        ViewCompat.requestApplyInsets(appCompatDelegateImpl5.mSubDecor);
                    }
                });
            }
            AppCompatDelegateImpl appCompatDelegateImpl4 = AppCompatDelegateImpl.this;
            AppCompatCallback appCompatCallback = appCompatDelegateImpl4.mAppCompatCallback;
            if (appCompatCallback != null) {
                appCompatCallback.onSupportActionModeFinished(appCompatDelegateImpl4.mActionMode);
            }
            AppCompatDelegateImpl appCompatDelegateImpl5 = AppCompatDelegateImpl.this;
            appCompatDelegateImpl5.mActionMode = null;
            ViewCompat.requestApplyInsets(appCompatDelegateImpl5.mSubDecor);
            AppCompatDelegateImpl.this.updateBackInvokedCallbackState();
        }

        @Override // androidx.appcompat.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            ViewCompat.requestApplyInsets(AppCompatDelegateImpl.this.mSubDecor);
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api17Impl {
        static void setLayoutDirection(Configuration configuration, Locale locale) {
            configuration.setLayoutDirection(locale);
        }

        static void setLocale(Configuration configuration, Locale locale) {
            configuration.setLocale(locale);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api21Impl {
        static boolean isPowerSaveMode(PowerManager powerManager) {
            boolean isPowerSaveMode;
            isPowerSaveMode = powerManager.isPowerSaveMode();
            return isPowerSaveMode;
        }

        static String toLanguageTag(Locale locale) {
            String languageTag;
            languageTag = locale.toLanguageTag();
            return languageTag;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api24Impl {
        static void generateConfigDelta_locale(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            LocaleList locales;
            LocaleList locales2;
            boolean equals;
            locales = configuration.getLocales();
            locales2 = configuration2.getLocales();
            equals = locales.equals(locales2);
            if (equals) {
                return;
            }
            configuration3.setLocales(locales2);
            configuration3.locale = configuration2.locale;
        }

        static LocaleListCompat getLocales(Configuration configuration) {
            LocaleList locales;
            String languageTags;
            locales = configuration.getLocales();
            languageTags = locales.toLanguageTags();
            return LocaleListCompat.forLanguageTags(languageTags);
        }

        public static void setDefaultLocales(LocaleListCompat localeListCompat) {
            LocaleList forLanguageTags;
            forLanguageTags = LocaleList.forLanguageTags(localeListCompat.toLanguageTags());
            LocaleList.setDefault(forLanguageTags);
        }

        static void setLocales(Configuration configuration, LocaleListCompat localeListCompat) {
            LocaleList forLanguageTags;
            forLanguageTags = LocaleList.forLanguageTags(localeListCompat.toLanguageTags());
            configuration.setLocales(forLanguageTags);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api33Impl {
        static OnBackInvokedDispatcher getOnBackInvokedDispatcher(Activity activity) {
            OnBackInvokedDispatcher onBackInvokedDispatcher;
            onBackInvokedDispatcher = activity.getOnBackInvokedDispatcher();
            return onBackInvokedDispatcher;
        }

        static OnBackInvokedCallback registerOnBackPressedCallback(Object obj, final AppCompatDelegateImpl appCompatDelegateImpl) {
            Objects.requireNonNull(appCompatDelegateImpl);
            OnBackInvokedCallback onBackInvokedCallback = new OnBackInvokedCallback() { // from class: androidx.appcompat.app.AppCompatDelegateImpl$Api33Impl$$ExternalSyntheticLambda5
                @Override // android.window.OnBackInvokedCallback
                public final void onBackInvoked() {
                    AppCompatDelegateImpl.this.onBackPressed();
                }
            };
            AppCompatDelegateImpl$Api33Impl$$ExternalSyntheticApiModelOutline1.m(obj).registerOnBackInvokedCallback(MediaController.VIDEO_BITRATE_480, onBackInvokedCallback);
            return onBackInvokedCallback;
        }

        static void unregisterOnBackInvokedCallback(Object obj, Object obj2) {
            AppCompatDelegateImpl$Api33Impl$$ExternalSyntheticApiModelOutline1.m(obj).unregisterOnBackInvokedCallback(AppCompatDelegateImpl$Api33Impl$$ExternalSyntheticApiModelOutline0.m(obj2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class AppCompatWindowCallback extends WindowCallbackWrapper {
        private boolean mDispatchKeyEventBypassEnabled;
        private boolean mOnContentChangedBypassEnabled;
        private boolean mOnPanelClosedBypassEnabled;

        AppCompatWindowCallback(Window.Callback callback) {
            super(callback);
        }

        public boolean bypassDispatchKeyEvent(Window.Callback callback, KeyEvent keyEvent) {
            try {
                this.mDispatchKeyEventBypassEnabled = true;
                return callback.dispatchKeyEvent(keyEvent);
            } finally {
                this.mDispatchKeyEventBypassEnabled = false;
            }
        }

        public void bypassOnContentChanged(Window.Callback callback) {
            try {
                this.mOnContentChangedBypassEnabled = true;
                callback.onContentChanged();
            } finally {
                this.mOnContentChangedBypassEnabled = false;
            }
        }

        public void bypassOnPanelClosed(Window.Callback callback, int i, Menu menu) {
            try {
                this.mOnPanelClosedBypassEnabled = true;
                callback.onPanelClosed(i, menu);
            } finally {
                this.mOnPanelClosedBypassEnabled = false;
            }
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return this.mDispatchKeyEventBypassEnabled ? getWrapped().dispatchKeyEvent(keyEvent) : AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return super.dispatchKeyShortcutEvent(keyEvent) || AppCompatDelegateImpl.this.onKeyShortcut(keyEvent.getKeyCode(), keyEvent);
        }

        @Override // android.view.Window.Callback
        public void onContentChanged() {
            if (this.mOnContentChangedBypassEnabled) {
                getWrapped().onContentChanged();
            }
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onCreatePanelMenu(int i, Menu menu) {
            if (i != 0 || (menu instanceof MenuBuilder)) {
                return super.onCreatePanelMenu(i, menu);
            }
            return false;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public View onCreatePanelView(int i) {
            return super.onCreatePanelView(i);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onMenuOpened(int i, Menu menu) {
            super.onMenuOpened(i, menu);
            AppCompatDelegateImpl.this.onMenuOpened(i);
            return true;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onPanelClosed(int i, Menu menu) {
            if (this.mOnPanelClosedBypassEnabled) {
                getWrapped().onPanelClosed(i, menu);
            } else {
                super.onPanelClosed(i, menu);
                AppCompatDelegateImpl.this.onPanelClosed(i);
            }
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onPreparePanel(int i, View view, Menu menu) {
            MenuBuilder menuBuilder = menu instanceof MenuBuilder ? (MenuBuilder) menu : null;
            if (i == 0 && menuBuilder == null) {
                return false;
            }
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(true);
            }
            boolean onPreparePanel = super.onPreparePanel(i, view, menu);
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(false);
            }
            return onPreparePanel;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onProvideKeyboardShortcuts(List list, Menu menu, int i) {
            MenuBuilder menuBuilder;
            PanelFeatureState panelState = AppCompatDelegateImpl.this.getPanelState(0, true);
            if (panelState == null || (menuBuilder = panelState.menu) == null) {
                super.onProvideKeyboardShortcuts(list, menu, i);
            } else {
                super.onProvideKeyboardShortcuts(list, menuBuilder, i);
            }
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            if (Build.VERSION.SDK_INT >= 23) {
                return null;
            }
            return AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() ? startAsSupportActionMode(callback) : super.onWindowStartingActionMode(callback);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
            return (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() && i == 0) ? startAsSupportActionMode(callback) : super.onWindowStartingActionMode(callback, i);
        }

        final android.view.ActionMode startAsSupportActionMode(ActionMode.Callback callback) {
            SupportActionModeWrapper.CallbackWrapper callbackWrapper = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImpl.this.mContext, callback);
            androidx.appcompat.view.ActionMode startSupportActionMode = AppCompatDelegateImpl.this.startSupportActionMode(callbackWrapper);
            if (startSupportActionMode != null) {
                return callbackWrapper.getActionModeWrapper(startSupportActionMode);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AutoBatteryNightModeManager extends AutoNightModeManager {
        private final PowerManager mPowerManager;

        AutoBatteryNightModeManager(Context context) {
            super();
            this.mPowerManager = (PowerManager) context.getApplicationContext().getSystemService("power");
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        IntentFilter createIntentFilterForBroadcastReceiver() {
            if (Build.VERSION.SDK_INT < 21) {
                return null;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
            return intentFilter;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public int getApplyableNightMode() {
            return (Build.VERSION.SDK_INT < 21 || !Api21Impl.isPowerSaveMode(this.mPowerManager)) ? 1 : 2;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public void onChange() {
            AppCompatDelegateImpl.this.applyDayNight();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public abstract class AutoNightModeManager {
        private BroadcastReceiver mReceiver;

        AutoNightModeManager() {
        }

        void cleanup() {
            BroadcastReceiver broadcastReceiver = this.mReceiver;
            if (broadcastReceiver != null) {
                try {
                    AppCompatDelegateImpl.this.mContext.unregisterReceiver(broadcastReceiver);
                } catch (IllegalArgumentException unused) {
                }
                this.mReceiver = null;
            }
        }

        abstract IntentFilter createIntentFilterForBroadcastReceiver();

        abstract int getApplyableNightMode();

        abstract void onChange();

        void setup() {
            cleanup();
            IntentFilter createIntentFilterForBroadcastReceiver = createIntentFilterForBroadcastReceiver();
            if (createIntentFilterForBroadcastReceiver == null || createIntentFilterForBroadcastReceiver.countActions() == 0) {
                return;
            }
            if (this.mReceiver == null) {
                this.mReceiver = new BroadcastReceiver() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        AutoNightModeManager.this.onChange();
                    }
                };
            }
            AppCompatDelegateImpl.this.mContext.registerReceiver(this.mReceiver, createIntentFilterForBroadcastReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AutoTimeNightModeManager extends AutoNightModeManager {
        private final TwilightManager mTwilightManager;

        AutoTimeNightModeManager(TwilightManager twilightManager) {
            super();
            this.mTwilightManager = twilightManager;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        IntentFilter createIntentFilterForBroadcastReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.TIME_TICK");
            return intentFilter;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public int getApplyableNightMode() {
            return this.mTwilightManager.isNight() ? 2 : 1;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public void onChange() {
            AppCompatDelegateImpl.this.applyDayNight();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ListMenuDecorView extends ContentFrameLayout {
        public ListMenuDecorView(Context context) {
            super(context);
        }

        private boolean isOutOfBounds(int i, int i2) {
            return i < -5 || i2 < -5 || i > getWidth() + 5 || i2 > getHeight() + 5;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0 || !isOutOfBounds((int) motionEvent.getX(), (int) motionEvent.getY())) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            AppCompatDelegateImpl.this.closePanel(0);
            return true;
        }

        @Override // android.view.View
        public void setBackgroundResource(int i) {
            setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), i));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static final class PanelFeatureState {
        int background;
        View createdPanelView;
        ViewGroup decorView;
        int featureId;
        Bundle frozenActionViewState;
        int gravity;
        boolean isHandled;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        Context listPresenterContext;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView = false;
        boolean refreshMenuContent;
        View shownPanelView;
        int windowAnimations;
        int x;
        int y;

        PanelFeatureState(int i) {
            this.featureId = i;
        }

        MenuView getListMenuView(MenuPresenter.Callback callback) {
            if (this.menu == null) {
                return null;
            }
            if (this.listMenuPresenter == null) {
                ListMenuPresenter listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R$layout.abc_list_menu_item_layout);
                this.listMenuPresenter = listMenuPresenter;
                listMenuPresenter.setCallback(callback);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }

        public boolean hasPanelItems() {
            if (this.shownPanelView == null) {
                return false;
            }
            return this.createdPanelView != null || this.listMenuPresenter.getAdapter().getCount() > 0;
        }

        void setMenu(MenuBuilder menuBuilder) {
            ListMenuPresenter listMenuPresenter;
            MenuBuilder menuBuilder2 = this.menu;
            if (menuBuilder == menuBuilder2) {
                return;
            }
            if (menuBuilder2 != null) {
                menuBuilder2.removeMenuPresenter(this.listMenuPresenter);
            }
            this.menu = menuBuilder;
            if (menuBuilder == null || (listMenuPresenter = this.listMenuPresenter) == null) {
                return;
            }
            menuBuilder.addMenuPresenter(listMenuPresenter);
        }

        void setStyle(Context context) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme newTheme = context.getResources().newTheme();
            newTheme.setTo(context.getTheme());
            newTheme.resolveAttribute(R$attr.actionBarPopupTheme, typedValue, true);
            int i = typedValue.resourceId;
            if (i != 0) {
                newTheme.applyStyle(i, true);
            }
            newTheme.resolveAttribute(R$attr.panelMenuListTheme, typedValue, true);
            int i2 = typedValue.resourceId;
            if (i2 == 0) {
                i2 = R$style.Theme_AppCompat_CompactMenu;
            }
            newTheme.applyStyle(i2, true);
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 0);
            contextThemeWrapper.getTheme().setTo(newTheme);
            this.listPresenterContext = contextThemeWrapper;
            TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(R$styleable.AppCompatTheme);
            this.background = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTheme_panelBackground, 0);
            this.windowAnimations = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            obtainStyledAttributes.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
        PanelMenuPresenterCallback() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            MenuBuilder rootMenu = menuBuilder.getRootMenu();
            boolean z2 = rootMenu != menuBuilder;
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (z2) {
                menuBuilder = rootMenu;
            }
            PanelFeatureState findMenuPanel = appCompatDelegateImpl.findMenuPanel(menuBuilder);
            if (findMenuPanel != null) {
                if (!z2) {
                    AppCompatDelegateImpl.this.closePanel(findMenuPanel, z);
                } else {
                    AppCompatDelegateImpl.this.callOnPanelClosed(findMenuPanel.featureId, findMenuPanel, rootMenu);
                    AppCompatDelegateImpl.this.closePanel(findMenuPanel, true);
                }
            }
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback windowCallback;
            if (menuBuilder != menuBuilder.getRootMenu()) {
                return true;
            }
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (!appCompatDelegateImpl.mHasActionBar || (windowCallback = appCompatDelegateImpl.getWindowCallback()) == null || AppCompatDelegateImpl.this.mDestroyed) {
                return true;
            }
            windowCallback.onMenuOpened(108, menuBuilder);
            return true;
        }
    }

    static {
        boolean z = Build.VERSION.SDK_INT < 21;
        IS_PRE_LOLLIPOP = z;
        sWindowBackgroundStyleable = new int[]{R.attr.windowBackground};
        sCanReturnDifferentContext = !"robolectric".equals(Build.FINGERPRINT);
        sCanApplyOverrideConfiguration = true;
        if (!z || sInstalledExceptionHandler) {
            return;
        }
        final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.1
            private boolean shouldWrapException(Throwable th) {
                String message;
                if (!(th instanceof Resources.NotFoundException) || (message = th.getMessage()) == null) {
                    return false;
                }
                return message.contains("drawable") || message.contains("Drawable");
            }

            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(Thread thread, Throwable th) {
                if (!shouldWrapException(th)) {
                    defaultUncaughtExceptionHandler.uncaughtException(thread, th);
                    return;
                }
                Resources.NotFoundException notFoundException = new Resources.NotFoundException(th.getMessage() + ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                notFoundException.initCause(th.getCause());
                notFoundException.setStackTrace(th.getStackTrace());
                defaultUncaughtExceptionHandler.uncaughtException(thread, notFoundException);
            }
        });
        sInstalledExceptionHandler = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatDelegateImpl(Dialog dialog, AppCompatCallback appCompatCallback) {
        this(dialog.getContext(), dialog.getWindow(), appCompatCallback, dialog);
    }

    private AppCompatDelegateImpl(Context context, Window window, AppCompatCallback appCompatCallback, Object obj) {
        this.mFadeAnim = null;
        this.mHandleNativeActionModes = true;
        this.mLocalNightMode = -100;
        this.mInvalidatePanelMenuRunnable = new Runnable() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.2
            @Override // java.lang.Runnable
            public void run() {
                AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                if ((appCompatDelegateImpl.mInvalidatePanelMenuFeatures & 1) != 0) {
                    appCompatDelegateImpl.doInvalidatePanelMenu(0);
                }
                AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
                if ((appCompatDelegateImpl2.mInvalidatePanelMenuFeatures & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) != 0) {
                    appCompatDelegateImpl2.doInvalidatePanelMenu(108);
                }
                AppCompatDelegateImpl appCompatDelegateImpl3 = AppCompatDelegateImpl.this;
                appCompatDelegateImpl3.mInvalidatePanelMenuPosted = false;
                appCompatDelegateImpl3.mInvalidatePanelMenuFeatures = 0;
            }
        };
        this.mContext = context;
        this.mAppCompatCallback = appCompatCallback;
        this.mHost = obj;
        if (this.mLocalNightMode == -100 && (obj instanceof Dialog)) {
            tryUnwrapContext();
        }
        if (this.mLocalNightMode == -100) {
            SimpleArrayMap simpleArrayMap = sLocalNightModes;
            Integer num = (Integer) simpleArrayMap.get(obj.getClass().getName());
            if (num != null) {
                this.mLocalNightMode = num.intValue();
                simpleArrayMap.remove(obj.getClass().getName());
            }
        }
        if (window != null) {
            attachToWindow(window);
        }
        AppCompatDrawableManager.preload();
    }

    private boolean applyApplicationSpecificConfig(boolean z) {
        return applyApplicationSpecificConfig(z, true);
    }

    private boolean applyApplicationSpecificConfig(boolean z, boolean z2) {
        if (this.mDestroyed) {
            return false;
        }
        int calculateNightMode = calculateNightMode();
        int mapNightMode = mapNightMode(this.mContext, calculateNightMode);
        LocaleListCompat calculateApplicationLocales = Build.VERSION.SDK_INT < 33 ? calculateApplicationLocales(this.mContext) : null;
        if (!z2 && calculateApplicationLocales != null) {
            calculateApplicationLocales = getConfigurationLocales(this.mContext.getResources().getConfiguration());
        }
        boolean updateAppConfiguration = updateAppConfiguration(mapNightMode, calculateApplicationLocales, z);
        if (calculateNightMode == 0) {
            getAutoTimeNightModeManager(this.mContext).setup();
        } else {
            AutoNightModeManager autoNightModeManager = this.mAutoTimeNightModeManager;
            if (autoNightModeManager != null) {
                autoNightModeManager.cleanup();
            }
        }
        if (calculateNightMode == 3) {
            getAutoBatteryNightModeManager(this.mContext).setup();
        } else {
            AutoNightModeManager autoNightModeManager2 = this.mAutoBatteryNightModeManager;
            if (autoNightModeManager2 != null) {
                autoNightModeManager2.cleanup();
            }
        }
        return updateAppConfiguration;
    }

    private void applyFixedSizeWindow() {
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) this.mSubDecor.findViewById(R.id.content);
        View decorView = this.mWindow.getDecorView();
        contentFrameLayout.setDecorPadding(decorView.getPaddingLeft(), decorView.getPaddingTop(), decorView.getPaddingRight(), decorView.getPaddingBottom());
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R$styleable.AppCompatTheme);
        obtainStyledAttributes.getValue(R$styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
        obtainStyledAttributes.getValue(R$styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
        int i = R$styleable.AppCompatTheme_windowFixedWidthMajor;
        if (obtainStyledAttributes.hasValue(i)) {
            obtainStyledAttributes.getValue(i, contentFrameLayout.getFixedWidthMajor());
        }
        int i2 = R$styleable.AppCompatTheme_windowFixedWidthMinor;
        if (obtainStyledAttributes.hasValue(i2)) {
            obtainStyledAttributes.getValue(i2, contentFrameLayout.getFixedWidthMinor());
        }
        int i3 = R$styleable.AppCompatTheme_windowFixedHeightMajor;
        if (obtainStyledAttributes.hasValue(i3)) {
            obtainStyledAttributes.getValue(i3, contentFrameLayout.getFixedHeightMajor());
        }
        int i4 = R$styleable.AppCompatTheme_windowFixedHeightMinor;
        if (obtainStyledAttributes.hasValue(i4)) {
            obtainStyledAttributes.getValue(i4, contentFrameLayout.getFixedHeightMinor());
        }
        obtainStyledAttributes.recycle();
        contentFrameLayout.requestLayout();
    }

    private void attachToWindow(Window window) {
        if (this.mWindow != null) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        Window.Callback callback = window.getCallback();
        if (callback instanceof AppCompatWindowCallback) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        AppCompatWindowCallback appCompatWindowCallback = new AppCompatWindowCallback(callback);
        this.mAppCompatWindowCallback = appCompatWindowCallback;
        window.setCallback(appCompatWindowCallback);
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mContext, (AttributeSet) null, sWindowBackgroundStyleable);
        Drawable drawableIfKnown = obtainStyledAttributes.getDrawableIfKnown(0);
        if (drawableIfKnown != null) {
            window.setBackgroundDrawable(drawableIfKnown);
        }
        obtainStyledAttributes.recycle();
        this.mWindow = window;
        if (Build.VERSION.SDK_INT < 33 || this.mDispatcher != null) {
            return;
        }
        setOnBackInvokedDispatcher(null);
    }

    private int calculateNightMode() {
        int i = this.mLocalNightMode;
        return i != -100 ? i : AppCompatDelegate.getDefaultNightMode();
    }

    private void cleanupAutoManagers() {
        AutoNightModeManager autoNightModeManager = this.mAutoTimeNightModeManager;
        if (autoNightModeManager != null) {
            autoNightModeManager.cleanup();
        }
        AutoNightModeManager autoNightModeManager2 = this.mAutoBatteryNightModeManager;
        if (autoNightModeManager2 != null) {
            autoNightModeManager2.cleanup();
        }
    }

    private Configuration createOverrideAppConfiguration(Context context, int i, LocaleListCompat localeListCompat, Configuration configuration, boolean z) {
        int i2 = i != 1 ? i != 2 ? z ? 0 : context.getApplicationContext().getResources().getConfiguration().uiMode & 48 : 32 : 16;
        Configuration configuration2 = new Configuration();
        configuration2.fontScale = 0.0f;
        if (configuration != null) {
            configuration2.setTo(configuration);
        }
        configuration2.uiMode = i2 | (configuration2.uiMode & (-49));
        if (localeListCompat != null) {
            setConfigurationLocales(configuration2, localeListCompat);
        }
        return configuration2;
    }

    private ViewGroup createSubDecor() {
        ViewGroup viewGroup;
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R$styleable.AppCompatTheme);
        int i = R$styleable.AppCompatTheme_windowActionBar;
        if (!obtainStyledAttributes.hasValue(i)) {
            obtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.AppCompatTheme_windowNoTitle, false)) {
            requestWindowFeature(1);
        } else if (obtainStyledAttributes.getBoolean(i, false)) {
            requestWindowFeature(108);
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            requestWindowFeature(109);
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            requestWindowFeature(10);
        }
        this.mIsFloating = obtainStyledAttributes.getBoolean(R$styleable.AppCompatTheme_android_windowIsFloating, false);
        obtainStyledAttributes.recycle();
        ensureWindow();
        this.mWindow.getDecorView();
        LayoutInflater from = LayoutInflater.from(this.mContext);
        if (this.mWindowNoTitle) {
            viewGroup = (ViewGroup) from.inflate(this.mOverlayActionMode ? R$layout.abc_screen_simple_overlay_action_mode : R$layout.abc_screen_simple, (ViewGroup) null);
        } else if (this.mIsFloating) {
            viewGroup = (ViewGroup) from.inflate(R$layout.abc_dialog_title_material, (ViewGroup) null);
            this.mOverlayActionBar = false;
            this.mHasActionBar = false;
        } else if (this.mHasActionBar) {
            TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R$attr.actionBarTheme, typedValue, true);
            viewGroup = (ViewGroup) LayoutInflater.from(typedValue.resourceId != 0 ? new ContextThemeWrapper(this.mContext, typedValue.resourceId) : this.mContext).inflate(R$layout.abc_screen_toolbar, (ViewGroup) null);
            DecorContentParent decorContentParent = (DecorContentParent) viewGroup.findViewById(R$id.decor_content_parent);
            this.mDecorContentParent = decorContentParent;
            decorContentParent.setWindowCallback(getWindowCallback());
            if (this.mOverlayActionBar) {
                this.mDecorContentParent.initFeature(109);
            }
            if (this.mFeatureProgress) {
                this.mDecorContentParent.initFeature(2);
            }
            if (this.mFeatureIndeterminateProgress) {
                this.mDecorContentParent.initFeature(5);
            }
        } else {
            viewGroup = null;
        }
        if (viewGroup == null) {
            throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.mHasActionBar + ", windowActionBarOverlay: " + this.mOverlayActionBar + ", android:windowIsFloating: " + this.mIsFloating + ", windowActionModeOverlay: " + this.mOverlayActionMode + ", windowNoTitle: " + this.mWindowNoTitle + " }");
        }
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.setOnApplyWindowInsetsListener(viewGroup, new OnApplyWindowInsetsListener() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.3
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
                    int updateStatusGuard = AppCompatDelegateImpl.this.updateStatusGuard(windowInsetsCompat, null);
                    if (systemWindowInsetTop != updateStatusGuard) {
                        windowInsetsCompat = windowInsetsCompat.replaceSystemWindowInsets(windowInsetsCompat.getSystemWindowInsetLeft(), updateStatusGuard, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                    }
                    return ViewCompat.onApplyWindowInsets(view, windowInsetsCompat);
                }
            });
        } else if (viewGroup instanceof FitWindowsViewGroup) {
            ((FitWindowsViewGroup) viewGroup).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.4
                @Override // androidx.appcompat.widget.FitWindowsViewGroup.OnFitSystemWindowsListener
                public void onFitSystemWindows(Rect rect) {
                    rect.top = AppCompatDelegateImpl.this.updateStatusGuard(null, rect);
                }
            });
        }
        if (this.mDecorContentParent == null) {
            this.mTitleView = (TextView) viewGroup.findViewById(R$id.title);
        }
        ViewUtils.makeOptionalFitsSystemWindows(viewGroup);
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) viewGroup.findViewById(R$id.action_bar_activity_content);
        ViewGroup viewGroup2 = (ViewGroup) this.mWindow.findViewById(R.id.content);
        if (viewGroup2 != null) {
            while (viewGroup2.getChildCount() > 0) {
                View childAt = viewGroup2.getChildAt(0);
                viewGroup2.removeViewAt(0);
                contentFrameLayout.addView(childAt);
            }
            viewGroup2.setId(-1);
            contentFrameLayout.setId(R.id.content);
            if (viewGroup2 instanceof FrameLayout) {
                ((FrameLayout) viewGroup2).setForeground(null);
            }
        }
        this.mWindow.setContentView(viewGroup);
        contentFrameLayout.setAttachListener(new ContentFrameLayout.OnAttachListener() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.5
            @Override // androidx.appcompat.widget.ContentFrameLayout.OnAttachListener
            public void onAttachedFromWindow() {
            }

            @Override // androidx.appcompat.widget.ContentFrameLayout.OnAttachListener
            public void onDetachedFromWindow() {
                AppCompatDelegateImpl.this.dismissPopups();
            }
        });
        return viewGroup;
    }

    private void ensureSubDecor() {
        if (this.mSubDecorInstalled) {
            return;
        }
        this.mSubDecor = createSubDecor();
        CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null) {
                decorContentParent.setWindowTitle(title);
            } else if (peekSupportActionBar() != null) {
                peekSupportActionBar().setWindowTitle(title);
            } else {
                TextView textView = this.mTitleView;
                if (textView != null) {
                    textView.setText(title);
                }
            }
        }
        applyFixedSizeWindow();
        onSubDecorInstalled(this.mSubDecor);
        this.mSubDecorInstalled = true;
        PanelFeatureState panelState = getPanelState(0, false);
        if (this.mDestroyed) {
            return;
        }
        if (panelState == null || panelState.menu == null) {
            invalidatePanelMenu(108);
        }
    }

    private void ensureWindow() {
        if (this.mWindow == null) {
            Object obj = this.mHost;
            if (obj instanceof Activity) {
                attachToWindow(((Activity) obj).getWindow());
            }
        }
        if (this.mWindow == null) {
            throw new IllegalStateException("We have not been given a Window");
        }
    }

    private int getActivityHandlesConfigChangesFlags(Context context) {
        if (!this.mActivityHandlesConfigFlagsChecked && (this.mHost instanceof Activity)) {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return 0;
            }
            try {
                int i = Build.VERSION.SDK_INT;
                ActivityInfo activityInfo = packageManager.getActivityInfo(new ComponentName(context, this.mHost.getClass()), i >= 29 ? 269221888 : i >= 24 ? 786432 : 0);
                if (activityInfo != null) {
                    this.mActivityHandlesConfigFlags = activityInfo.configChanges;
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", e);
                this.mActivityHandlesConfigFlags = 0;
            }
        }
        this.mActivityHandlesConfigFlagsChecked = true;
        return this.mActivityHandlesConfigFlags;
    }

    private AutoNightModeManager getAutoBatteryNightModeManager(Context context) {
        if (this.mAutoBatteryNightModeManager == null) {
            this.mAutoBatteryNightModeManager = new AutoBatteryNightModeManager(context);
        }
        return this.mAutoBatteryNightModeManager;
    }

    private AutoNightModeManager getAutoTimeNightModeManager(Context context) {
        if (this.mAutoTimeNightModeManager == null) {
            this.mAutoTimeNightModeManager = new AutoTimeNightModeManager(TwilightManager.getInstance(context));
        }
        return this.mAutoTimeNightModeManager;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void initWindowDecorActionBar() {
        WindowDecorActionBar windowDecorActionBar;
        ActionBar actionBar;
        ensureSubDecor();
        if (!this.mHasActionBar || this.mActionBar != null) {
            return;
        }
        Object obj = this.mHost;
        if (!(obj instanceof Activity)) {
            if (obj instanceof Dialog) {
                windowDecorActionBar = new WindowDecorActionBar((Dialog) this.mHost);
            }
            actionBar = this.mActionBar;
            if (actionBar == null) {
                actionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
                return;
            }
            return;
        }
        windowDecorActionBar = new WindowDecorActionBar((Activity) this.mHost, this.mOverlayActionBar);
        this.mActionBar = windowDecorActionBar;
        actionBar = this.mActionBar;
        if (actionBar == null) {
        }
    }

    private boolean initializePanelContent(PanelFeatureState panelFeatureState) {
        View view = panelFeatureState.createdPanelView;
        if (view != null) {
            panelFeatureState.shownPanelView = view;
            return true;
        }
        if (panelFeatureState.menu == null) {
            return false;
        }
        if (this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback();
        }
        View view2 = (View) panelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
        panelFeatureState.shownPanelView = view2;
        return view2 != null;
    }

    private boolean initializePanelDecor(PanelFeatureState panelFeatureState) {
        panelFeatureState.setStyle(getActionBarThemedContext());
        panelFeatureState.decorView = new ListMenuDecorView(panelFeatureState.listPresenterContext);
        panelFeatureState.gravity = 81;
        return true;
    }

    private boolean initializePanelMenu(PanelFeatureState panelFeatureState) {
        Resources.Theme theme;
        Context context = this.mContext;
        int i = panelFeatureState.featureId;
        if ((i == 0 || i == 108) && this.mDecorContentParent != null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme2 = context.getTheme();
            theme2.resolveAttribute(R$attr.actionBarTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme = context.getResources().newTheme();
                theme.setTo(theme2);
                theme.applyStyle(typedValue.resourceId, true);
                theme.resolveAttribute(R$attr.actionBarWidgetTheme, typedValue, true);
            } else {
                theme2.resolveAttribute(R$attr.actionBarWidgetTheme, typedValue, true);
                theme = null;
            }
            if (typedValue.resourceId != 0) {
                if (theme == null) {
                    theme = context.getResources().newTheme();
                    theme.setTo(theme2);
                }
                theme.applyStyle(typedValue.resourceId, true);
            }
            if (theme != null) {
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 0);
                contextThemeWrapper.getTheme().setTo(theme);
                context = contextThemeWrapper;
            }
        }
        MenuBuilder menuBuilder = new MenuBuilder(context);
        menuBuilder.setCallback(this);
        panelFeatureState.setMenu(menuBuilder);
        return true;
    }

    private void invalidatePanelMenu(int i) {
        this.mInvalidatePanelMenuFeatures = (1 << i) | this.mInvalidatePanelMenuFeatures;
        if (this.mInvalidatePanelMenuPosted) {
            return;
        }
        ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
        this.mInvalidatePanelMenuPosted = true;
    }

    private boolean onKeyDownPanel(int i, KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() != 0) {
            return false;
        }
        PanelFeatureState panelState = getPanelState(i, true);
        if (panelState.isOpen) {
            return false;
        }
        return preparePanel(panelState, keyEvent);
    }

    private boolean onKeyUpPanel(int i, KeyEvent keyEvent) {
        boolean z;
        DecorContentParent decorContentParent;
        if (this.mActionMode != null) {
            return false;
        }
        boolean z2 = true;
        PanelFeatureState panelState = getPanelState(i, true);
        if (i != 0 || (decorContentParent = this.mDecorContentParent) == null || !decorContentParent.canShowOverflowMenu() || ViewConfiguration.get(this.mContext).hasPermanentMenuKey()) {
            boolean z3 = panelState.isOpen;
            if (z3 || panelState.isHandled) {
                closePanel(panelState, true);
                z2 = z3;
            } else {
                if (panelState.isPrepared) {
                    if (panelState.refreshMenuContent) {
                        panelState.isPrepared = false;
                        z = preparePanel(panelState, keyEvent);
                    } else {
                        z = true;
                    }
                    if (z) {
                        openPanel(panelState, keyEvent);
                    }
                }
                z2 = false;
            }
        } else if (this.mDecorContentParent.isOverflowMenuShowing()) {
            z2 = this.mDecorContentParent.hideOverflowMenu();
        } else {
            if (!this.mDestroyed && preparePanel(panelState, keyEvent)) {
                z2 = this.mDecorContentParent.showOverflowMenu();
            }
            z2 = false;
        }
        if (z2) {
            AudioManager audioManager = (AudioManager) this.mContext.getApplicationContext().getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            if (audioManager != null) {
                audioManager.playSoundEffect(0);
            } else {
                Log.w("AppCompatDelegate", "Couldn't get audio manager");
            }
        }
        return z2;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openPanel(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        int i;
        ViewGroup.LayoutParams layoutParams;
        if (panelFeatureState.isOpen || this.mDestroyed) {
            return;
        }
        if (panelFeatureState.featureId == 0 && (this.mContext.getResources().getConfiguration().screenLayout & 15) == 4) {
            return;
        }
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null && !windowCallback.onMenuOpened(panelFeatureState.featureId, panelFeatureState.menu)) {
            closePanel(panelFeatureState, true);
            return;
        }
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        if (windowManager == null || !preparePanel(panelFeatureState, keyEvent)) {
            return;
        }
        ViewGroup viewGroup = panelFeatureState.decorView;
        if (viewGroup == null || panelFeatureState.refreshDecorView) {
            if (viewGroup == null) {
                if (!initializePanelDecor(panelFeatureState) || panelFeatureState.decorView == null) {
                    return;
                }
            } else if (panelFeatureState.refreshDecorView && viewGroup.getChildCount() > 0) {
                panelFeatureState.decorView.removeAllViews();
            }
            if (!initializePanelContent(panelFeatureState) || !panelFeatureState.hasPanelItems()) {
                panelFeatureState.refreshDecorView = true;
                return;
            }
            ViewGroup.LayoutParams layoutParams2 = panelFeatureState.shownPanelView.getLayoutParams();
            if (layoutParams2 == null) {
                layoutParams2 = new ViewGroup.LayoutParams(-2, -2);
            }
            panelFeatureState.decorView.setBackgroundResource(panelFeatureState.background);
            ViewParent parent = panelFeatureState.shownPanelView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(panelFeatureState.shownPanelView);
            }
            panelFeatureState.decorView.addView(panelFeatureState.shownPanelView, layoutParams2);
            if (!panelFeatureState.shownPanelView.hasFocus()) {
                panelFeatureState.shownPanelView.requestFocus();
            }
        } else {
            View view = panelFeatureState.createdPanelView;
            if (view != null && (layoutParams = view.getLayoutParams()) != null && layoutParams.width == -1) {
                i = -1;
                panelFeatureState.isHandled = false;
                WindowManager.LayoutParams layoutParams3 = new WindowManager.LayoutParams(i, -2, panelFeatureState.x, panelFeatureState.y, 1002, 8519680, -3);
                layoutParams3.gravity = panelFeatureState.gravity;
                layoutParams3.windowAnimations = panelFeatureState.windowAnimations;
                windowManager.addView(panelFeatureState.decorView, layoutParams3);
                panelFeatureState.isOpen = true;
                if (panelFeatureState.featureId != 0) {
                    updateBackInvokedCallbackState();
                    return;
                }
                return;
            }
        }
        i = -2;
        panelFeatureState.isHandled = false;
        WindowManager.LayoutParams layoutParams32 = new WindowManager.LayoutParams(i, -2, panelFeatureState.x, panelFeatureState.y, 1002, 8519680, -3);
        layoutParams32.gravity = panelFeatureState.gravity;
        layoutParams32.windowAnimations = panelFeatureState.windowAnimations;
        windowManager.addView(panelFeatureState.decorView, layoutParams32);
        panelFeatureState.isOpen = true;
        if (panelFeatureState.featureId != 0) {
        }
    }

    private boolean performPanelShortcut(PanelFeatureState panelFeatureState, int i, KeyEvent keyEvent, int i2) {
        MenuBuilder menuBuilder;
        boolean z = false;
        if (keyEvent.isSystem()) {
            return false;
        }
        if ((panelFeatureState.isPrepared || preparePanel(panelFeatureState, keyEvent)) && (menuBuilder = panelFeatureState.menu) != null) {
            z = menuBuilder.performShortcut(i, keyEvent, i2);
        }
        if (z && (i2 & 1) == 0 && this.mDecorContentParent == null) {
            closePanel(panelFeatureState, true);
        }
        return z;
    }

    private boolean preparePanel(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        DecorContentParent decorContentParent;
        DecorContentParent decorContentParent2;
        DecorContentParent decorContentParent3;
        if (this.mDestroyed) {
            return false;
        }
        if (panelFeatureState.isPrepared) {
            return true;
        }
        PanelFeatureState panelFeatureState2 = this.mPreparedPanel;
        if (panelFeatureState2 != null && panelFeatureState2 != panelFeatureState) {
            closePanel(panelFeatureState2, false);
        }
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null) {
            panelFeatureState.createdPanelView = windowCallback.onCreatePanelView(panelFeatureState.featureId);
        }
        int i = panelFeatureState.featureId;
        boolean z = i == 0 || i == 108;
        if (z && (decorContentParent3 = this.mDecorContentParent) != null) {
            decorContentParent3.setMenuPrepared();
        }
        if (panelFeatureState.createdPanelView == null) {
            if (z) {
                peekSupportActionBar();
            }
            MenuBuilder menuBuilder = panelFeatureState.menu;
            if (menuBuilder == null || panelFeatureState.refreshMenuContent) {
                if (menuBuilder == null && (!initializePanelMenu(panelFeatureState) || panelFeatureState.menu == null)) {
                    return false;
                }
                if (z && this.mDecorContentParent != null) {
                    if (this.mActionMenuPresenterCallback == null) {
                        this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
                    }
                    this.mDecorContentParent.setMenu(panelFeatureState.menu, this.mActionMenuPresenterCallback);
                }
                panelFeatureState.menu.stopDispatchingItemsChanged();
                if (!windowCallback.onCreatePanelMenu(panelFeatureState.featureId, panelFeatureState.menu)) {
                    panelFeatureState.setMenu(null);
                    if (z && (decorContentParent = this.mDecorContentParent) != null) {
                        decorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                    }
                    return false;
                }
                panelFeatureState.refreshMenuContent = false;
            }
            panelFeatureState.menu.stopDispatchingItemsChanged();
            Bundle bundle = panelFeatureState.frozenActionViewState;
            if (bundle != null) {
                panelFeatureState.menu.restoreActionViewStates(bundle);
                panelFeatureState.frozenActionViewState = null;
            }
            if (!windowCallback.onPreparePanel(0, panelFeatureState.createdPanelView, panelFeatureState.menu)) {
                if (z && (decorContentParent2 = this.mDecorContentParent) != null) {
                    decorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
                }
                panelFeatureState.menu.startDispatchingItemsChanged();
                return false;
            }
            boolean z2 = KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1;
            panelFeatureState.qwertyMode = z2;
            panelFeatureState.menu.setQwertyMode(z2);
            panelFeatureState.menu.startDispatchingItemsChanged();
        }
        panelFeatureState.isPrepared = true;
        panelFeatureState.isHandled = false;
        this.mPreparedPanel = panelFeatureState;
        return true;
    }

    private void reopenMenu(boolean z) {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent == null || !decorContentParent.canShowOverflowMenu() || (ViewConfiguration.get(this.mContext).hasPermanentMenuKey() && !this.mDecorContentParent.isOverflowMenuShowPending())) {
            PanelFeatureState panelState = getPanelState(0, true);
            panelState.refreshDecorView = true;
            closePanel(panelState, false);
            openPanel(panelState, null);
            return;
        }
        Window.Callback windowCallback = getWindowCallback();
        if (this.mDecorContentParent.isOverflowMenuShowing() && z) {
            this.mDecorContentParent.hideOverflowMenu();
            if (this.mDestroyed) {
                return;
            }
            windowCallback.onPanelClosed(108, getPanelState(0, true).menu);
            return;
        }
        if (windowCallback == null || this.mDestroyed) {
            return;
        }
        if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuRunnable.run();
        }
        PanelFeatureState panelState2 = getPanelState(0, true);
        MenuBuilder menuBuilder = panelState2.menu;
        if (menuBuilder == null || panelState2.refreshMenuContent || !windowCallback.onPreparePanel(0, panelState2.createdPanelView, menuBuilder)) {
            return;
        }
        windowCallback.onMenuOpened(108, panelState2.menu);
        this.mDecorContentParent.showOverflowMenu();
    }

    private int sanitizeWindowFeatureId(int i) {
        if (i == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        }
        if (i != 9) {
            return i;
        }
        Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
        return 109;
    }

    private boolean shouldInheritContext(ViewParent viewParent) {
        if (viewParent == null) {
            return false;
        }
        View decorView = this.mWindow.getDecorView();
        while (viewParent != null) {
            if (viewParent == decorView || !(viewParent instanceof View) || ViewCompat.isAttachedToWindow((View) viewParent)) {
                return false;
            }
            viewParent = viewParent.getParent();
        }
        return true;
    }

    private void throwFeatureRequestIfSubDecorInstalled() {
        if (this.mSubDecorInstalled) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }

    private AppCompatActivity tryUnwrapContext() {
        for (Context context = this.mContext; context != null && (context instanceof ContextWrapper); context = ((ContextWrapper) context).getBaseContext()) {
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateActivityConfiguration(Configuration configuration) {
        Activity activity = (Activity) this.mHost;
        if (activity instanceof LifecycleOwner) {
            if (!((LifecycleOwner) activity).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                return;
            }
        } else if (!this.mCreated || this.mDestroyed) {
            return;
        }
        activity.onConfigurationChanged(configuration);
    }

    private boolean updateAppConfiguration(int i, LocaleListCompat localeListCompat, boolean z) {
        boolean z2;
        Configuration createOverrideAppConfiguration = createOverrideAppConfiguration(this.mContext, i, localeListCompat, null, false);
        int activityHandlesConfigChangesFlags = getActivityHandlesConfigChangesFlags(this.mContext);
        Configuration configuration = this.mEffectiveConfiguration;
        if (configuration == null) {
            configuration = this.mContext.getResources().getConfiguration();
        }
        int i2 = configuration.uiMode & 48;
        int i3 = createOverrideAppConfiguration.uiMode & 48;
        LocaleListCompat configurationLocales = getConfigurationLocales(configuration);
        LocaleListCompat configurationLocales2 = localeListCompat == null ? null : getConfigurationLocales(createOverrideAppConfiguration);
        int i4 = i2 != i3 ? 512 : 0;
        if (configurationLocales2 != null && !configurationLocales.equals(configurationLocales2)) {
            i4 |= 8196;
        }
        boolean z3 = true;
        if (((activityHandlesConfigChangesFlags ^ (-1)) & i4) != 0 && z && this.mBaseContextAttached && (sCanReturnDifferentContext || this.mCreated)) {
            Object obj = this.mHost;
            if ((obj instanceof Activity) && !((Activity) obj).isChild()) {
                ActivityCompat.recreate((Activity) this.mHost);
                z2 = true;
                if (!z2 || i4 == 0) {
                    z3 = z2;
                } else {
                    updateResourcesConfiguration(i3, configurationLocales2, (i4 & activityHandlesConfigChangesFlags) == i4, null);
                }
                if (z3 && configurationLocales2 != null) {
                    setDefaultLocalesForLocaleList(getConfigurationLocales(this.mContext.getResources().getConfiguration()));
                }
                return z3;
            }
        }
        z2 = false;
        if (z2) {
        }
        z3 = z2;
        if (z3) {
            setDefaultLocalesForLocaleList(getConfigurationLocales(this.mContext.getResources().getConfiguration()));
        }
        return z3;
    }

    private void updateResourcesConfiguration(int i, LocaleListCompat localeListCompat, boolean z, Configuration configuration) {
        Resources resources = this.mContext.getResources();
        Configuration configuration2 = new Configuration(resources.getConfiguration());
        if (configuration != null) {
            configuration2.updateFrom(configuration);
        }
        configuration2.uiMode = i | (resources.getConfiguration().uiMode & (-49));
        if (localeListCompat != null) {
            setConfigurationLocales(configuration2, localeListCompat);
        }
        resources.updateConfiguration(configuration2, null);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 < 26) {
            ResourcesFlusher.flush(resources);
        }
        int i3 = this.mThemeResId;
        if (i3 != 0) {
            this.mContext.setTheme(i3);
            if (i2 >= 23) {
                this.mContext.getTheme().applyStyle(this.mThemeResId, true);
            }
        }
        if (z && (this.mHost instanceof Activity)) {
            updateActivityConfiguration(configuration2);
        }
    }

    private void updateStatusGuardColor(View view) {
        Context context;
        int i;
        if ((ViewCompat.getWindowSystemUiVisibility(view) & LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM) != 0) {
            context = this.mContext;
            i = R$color.abc_decor_view_status_guard_light;
        } else {
            context = this.mContext;
            i = R$color.abc_decor_view_status_guard;
        }
        view.setBackgroundColor(ContextCompat.getColor(context, i));
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ensureSubDecor();
        ((ViewGroup) this.mSubDecor.findViewById(R.id.content)).addView(view, layoutParams);
        this.mAppCompatWindowCallback.bypassOnContentChanged(this.mWindow.getCallback());
    }

    public boolean applyDayNight() {
        return applyApplicationSpecificConfig(true);
    }

    LocaleListCompat calculateApplicationLocales(Context context) {
        LocaleListCompat requestedAppLocales;
        int i = Build.VERSION.SDK_INT;
        if (i >= 33 || (requestedAppLocales = AppCompatDelegate.getRequestedAppLocales()) == null) {
            return null;
        }
        LocaleListCompat configurationLocales = getConfigurationLocales(context.getApplicationContext().getResources().getConfiguration());
        LocaleListCompat combineLocalesIfOverlayExists = i >= 24 ? LocaleOverlayHelper.combineLocalesIfOverlayExists(requestedAppLocales, configurationLocales) : requestedAppLocales.isEmpty() ? LocaleListCompat.getEmptyLocaleList() : LocaleListCompat.forLanguageTags(requestedAppLocales.get(0).toString());
        return combineLocalesIfOverlayExists.isEmpty() ? configurationLocales : combineLocalesIfOverlayExists;
    }

    void callOnPanelClosed(int i, PanelFeatureState panelFeatureState, Menu menu) {
        if (menu == null) {
            if (panelFeatureState == null && i >= 0) {
                PanelFeatureState[] panelFeatureStateArr = this.mPanels;
                if (i < panelFeatureStateArr.length) {
                    panelFeatureState = panelFeatureStateArr[i];
                }
            }
            if (panelFeatureState != null) {
                menu = panelFeatureState.menu;
            }
        }
        if ((panelFeatureState == null || panelFeatureState.isOpen) && !this.mDestroyed) {
            this.mAppCompatWindowCallback.bypassOnPanelClosed(this.mWindow.getCallback(), i, menu);
        }
    }

    void checkCloseActionMenu(MenuBuilder menuBuilder) {
        if (this.mClosingActionMenu) {
            return;
        }
        this.mClosingActionMenu = true;
        this.mDecorContentParent.dismissPopups();
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null && !this.mDestroyed) {
            windowCallback.onPanelClosed(108, menuBuilder);
        }
        this.mClosingActionMenu = false;
    }

    void closePanel(int i) {
        closePanel(getPanelState(i, true), true);
    }

    void closePanel(PanelFeatureState panelFeatureState, boolean z) {
        ViewGroup viewGroup;
        DecorContentParent decorContentParent;
        if (z && panelFeatureState.featureId == 0 && (decorContentParent = this.mDecorContentParent) != null && decorContentParent.isOverflowMenuShowing()) {
            checkCloseActionMenu(panelFeatureState.menu);
            return;
        }
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        if (windowManager != null && panelFeatureState.isOpen && (viewGroup = panelFeatureState.decorView) != null) {
            windowManager.removeView(viewGroup);
            if (z) {
                callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, null);
            }
        }
        panelFeatureState.isPrepared = false;
        panelFeatureState.isHandled = false;
        panelFeatureState.isOpen = false;
        panelFeatureState.shownPanelView = null;
        panelFeatureState.refreshDecorView = true;
        if (this.mPreparedPanel == panelFeatureState) {
            this.mPreparedPanel = null;
        }
        if (panelFeatureState.featureId == 0) {
            updateBackInvokedCallbackState();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public View createView(View view, String str, Context context, AttributeSet attributeSet) {
        boolean z;
        AppCompatViewInflater appCompatViewInflater;
        if (this.mAppCompatViewInflater == null) {
            String string = this.mContext.obtainStyledAttributes(R$styleable.AppCompatTheme).getString(R$styleable.AppCompatTheme_viewInflaterClass);
            if (string == null) {
                appCompatViewInflater = new AppCompatViewInflater();
            } else {
                try {
                    this.mAppCompatViewInflater = (AppCompatViewInflater) this.mContext.getClassLoader().loadClass(string).getDeclaredConstructor(null).newInstance(null);
                } catch (Throwable th) {
                    Log.i("AppCompatDelegate", "Failed to instantiate custom view inflater " + string + ". Falling back to default.", th);
                    appCompatViewInflater = new AppCompatViewInflater();
                }
            }
            this.mAppCompatViewInflater = appCompatViewInflater;
        }
        boolean z2 = IS_PRE_LOLLIPOP;
        boolean z3 = false;
        if (z2) {
            if (this.mLayoutIncludeDetector == null) {
                this.mLayoutIncludeDetector = new LayoutIncludeDetector();
            }
            if (this.mLayoutIncludeDetector.detect(attributeSet)) {
                z = true;
            } else {
                if (!(attributeSet instanceof XmlPullParser)) {
                    z3 = shouldInheritContext((ViewParent) view);
                } else if (((XmlPullParser) attributeSet).getDepth() > 1) {
                    z3 = true;
                }
                z = z3;
            }
        } else {
            z = false;
        }
        return this.mAppCompatViewInflater.createView(view, str, context, attributeSet, z, z2, true, VectorEnabledTintResources.shouldBeUsed());
    }

    void dismissPopups() {
        MenuBuilder menuBuilder;
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.dismissPopups();
        }
        if (this.mActionModePopup != null) {
            this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
            if (this.mActionModePopup.isShowing()) {
                try {
                    this.mActionModePopup.dismiss();
                } catch (IllegalArgumentException unused) {
                }
            }
            this.mActionModePopup = null;
        }
        endOnGoingFadeAnimation();
        PanelFeatureState panelState = getPanelState(0, false);
        if (panelState == null || (menuBuilder = panelState.menu) == null) {
            return;
        }
        menuBuilder.close();
    }

    boolean dispatchKeyEvent(KeyEvent keyEvent) {
        View decorView;
        Object obj = this.mHost;
        if (((obj instanceof KeyEventDispatcher.Component) || (obj instanceof AppCompatDialog)) && (decorView = this.mWindow.getDecorView()) != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 82 && this.mAppCompatWindowCallback.bypassDispatchKeyEvent(this.mWindow.getCallback(), keyEvent)) {
            return true;
        }
        int keyCode = keyEvent.getKeyCode();
        return keyEvent.getAction() == 0 ? onKeyDown(keyCode, keyEvent) : onKeyUp(keyCode, keyEvent);
    }

    void doInvalidatePanelMenu(int i) {
        PanelFeatureState panelState;
        PanelFeatureState panelState2 = getPanelState(i, true);
        if (panelState2.menu != null) {
            Bundle bundle = new Bundle();
            panelState2.menu.saveActionViewStates(bundle);
            if (bundle.size() > 0) {
                panelState2.frozenActionViewState = bundle;
            }
            panelState2.menu.stopDispatchingItemsChanged();
            panelState2.menu.clear();
        }
        panelState2.refreshMenuContent = true;
        panelState2.refreshDecorView = true;
        if ((i != 108 && i != 0) || this.mDecorContentParent == null || (panelState = getPanelState(0, false)) == null) {
            return;
        }
        panelState.isPrepared = false;
        preparePanel(panelState, null);
    }

    void endOnGoingFadeAnimation() {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mFadeAnim;
        if (viewPropertyAnimatorCompat != null) {
            viewPropertyAnimatorCompat.cancel();
        }
    }

    PanelFeatureState findMenuPanel(Menu menu) {
        PanelFeatureState[] panelFeatureStateArr = this.mPanels;
        int length = panelFeatureStateArr != null ? panelFeatureStateArr.length : 0;
        for (int i = 0; i < length; i++) {
            PanelFeatureState panelFeatureState = panelFeatureStateArr[i];
            if (panelFeatureState != null && panelFeatureState.menu == menu) {
                return panelFeatureState;
            }
        }
        return null;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public View findViewById(int i) {
        ensureSubDecor();
        return this.mWindow.findViewById(i);
    }

    final Context getActionBarThemedContext() {
        ActionBar supportActionBar = getSupportActionBar();
        Context themedContext = supportActionBar != null ? supportActionBar.getThemedContext() : null;
        return themedContext == null ? this.mContext : themedContext;
    }

    LocaleListCompat getConfigurationLocales(Configuration configuration) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 24) {
            return Api24Impl.getLocales(configuration);
        }
        Locale locale = configuration.locale;
        return i >= 21 ? LocaleListCompat.forLanguageTags(Api21Impl.toLanguageTag(locale)) : LocaleListCompat.create(locale);
    }

    protected PanelFeatureState getPanelState(int i, boolean z) {
        PanelFeatureState[] panelFeatureStateArr = this.mPanels;
        if (panelFeatureStateArr == null || panelFeatureStateArr.length <= i) {
            PanelFeatureState[] panelFeatureStateArr2 = new PanelFeatureState[i + 1];
            if (panelFeatureStateArr != null) {
                System.arraycopy(panelFeatureStateArr, 0, panelFeatureStateArr2, 0, panelFeatureStateArr.length);
            }
            this.mPanels = panelFeatureStateArr2;
            panelFeatureStateArr = panelFeatureStateArr2;
        }
        PanelFeatureState panelFeatureState = panelFeatureStateArr[i];
        if (panelFeatureState != null) {
            return panelFeatureState;
        }
        PanelFeatureState panelFeatureState2 = new PanelFeatureState(i);
        panelFeatureStateArr[i] = panelFeatureState2;
        return panelFeatureState2;
    }

    public ActionBar getSupportActionBar() {
        initWindowDecorActionBar();
        return this.mActionBar;
    }

    final CharSequence getTitle() {
        Object obj = this.mHost;
        return obj instanceof Activity ? ((Activity) obj).getTitle() : this.mTitle;
    }

    final Window.Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void installViewFactory() {
        LayoutInflater from = LayoutInflater.from(this.mContext);
        if (from.getFactory() == null) {
            LayoutInflaterCompat.setFactory2(from, this);
        } else {
            if (from.getFactory2() instanceof AppCompatDelegateImpl) {
                return;
            }
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void invalidateOptionsMenu() {
        if (peekSupportActionBar() == null || getSupportActionBar().invalidateOptionsMenu()) {
            return;
        }
        invalidatePanelMenu(0);
    }

    public boolean isHandleNativeActionModesEnabled() {
        return this.mHandleNativeActionModes;
    }

    int mapNightMode(Context context, int i) {
        AutoNightModeManager autoTimeNightModeManager;
        if (i == -100) {
            return -1;
        }
        if (i != -1) {
            if (i != 0) {
                if (i != 1 && i != 2) {
                    if (i != 3) {
                        throw new IllegalStateException("Unknown value set for night mode. Please use one of the MODE_NIGHT values from AppCompatDelegate.");
                    }
                    autoTimeNightModeManager = getAutoBatteryNightModeManager(context);
                }
            } else {
                if (Build.VERSION.SDK_INT >= 23 && ((UiModeManager) context.getApplicationContext().getSystemService("uimode")).getNightMode() == 0) {
                    return -1;
                }
                autoTimeNightModeManager = getAutoTimeNightModeManager(context);
            }
            return autoTimeNightModeManager.getApplyableNightMode();
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onBackPressed() {
        boolean z = this.mLongPressBackDown;
        this.mLongPressBackDown = false;
        PanelFeatureState panelState = getPanelState(0, false);
        if (panelState != null && panelState.isOpen) {
            if (!z) {
                closePanel(panelState, true);
            }
            return true;
        }
        androidx.appcompat.view.ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
            return true;
        }
        ActionBar supportActionBar = getSupportActionBar();
        return supportActionBar != null && supportActionBar.collapseActionView();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void onCreate(Bundle bundle) {
        String str;
        this.mBaseContextAttached = true;
        applyApplicationSpecificConfig(false);
        ensureWindow();
        Object obj = this.mHost;
        if (obj instanceof Activity) {
            try {
                str = NavUtils.getParentActivityName((Activity) obj);
            } catch (IllegalArgumentException unused) {
                str = null;
            }
            if (str != null) {
                ActionBar peekSupportActionBar = peekSupportActionBar();
                if (peekSupportActionBar == null) {
                    this.mEnableDefaultActionBarUp = true;
                } else {
                    peekSupportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
            AppCompatDelegate.addActiveDelegate(this);
        }
        this.mEffectiveConfiguration = new Configuration(this.mContext.getResources().getConfiguration());
        this.mCreated = true;
    }

    @Override // android.view.LayoutInflater.Factory2
    public final View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return createView(view, str, context, attributeSet);
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0058  */
    @Override // androidx.appcompat.app.AppCompatDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDestroy() {
        ActionBar actionBar;
        if (this.mHost instanceof Activity) {
            AppCompatDelegate.removeActivityDelegate(this);
        }
        if (this.mInvalidatePanelMenuPosted) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
        }
        this.mDestroyed = true;
        if (this.mLocalNightMode != -100) {
            Object obj = this.mHost;
            if ((obj instanceof Activity) && ((Activity) obj).isChangingConfigurations()) {
                sLocalNightModes.put(this.mHost.getClass().getName(), Integer.valueOf(this.mLocalNightMode));
                actionBar = this.mActionBar;
                if (actionBar != null) {
                    actionBar.onDestroy();
                }
                cleanupAutoManagers();
            }
        }
        sLocalNightModes.remove(this.mHost.getClass().getName());
        actionBar = this.mActionBar;
        if (actionBar != null) {
        }
        cleanupAutoManagers();
    }

    boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            this.mLongPressBackDown = (keyEvent.getFlags() & 128) != 0;
        } else if (i == 82) {
            onKeyDownPanel(0, keyEvent);
            return true;
        }
        return false;
    }

    boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && supportActionBar.onKeyShortcut(i, keyEvent)) {
            return true;
        }
        PanelFeatureState panelFeatureState = this.mPreparedPanel;
        if (panelFeatureState != null && performPanelShortcut(panelFeatureState, keyEvent.getKeyCode(), keyEvent, 1)) {
            PanelFeatureState panelFeatureState2 = this.mPreparedPanel;
            if (panelFeatureState2 != null) {
                panelFeatureState2.isHandled = true;
            }
            return true;
        }
        if (this.mPreparedPanel == null) {
            PanelFeatureState panelState = getPanelState(0, true);
            preparePanel(panelState, keyEvent);
            boolean performPanelShortcut = performPanelShortcut(panelState, keyEvent.getKeyCode(), keyEvent, 1);
            panelState.isPrepared = false;
            if (performPanelShortcut) {
                return true;
            }
        }
        return false;
    }

    boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 4) {
            if (i == 82) {
                onKeyUpPanel(0, keyEvent);
                return true;
            }
        } else if (onBackPressed()) {
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        PanelFeatureState findMenuPanel;
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback == null || this.mDestroyed || (findMenuPanel = findMenuPanel(menuBuilder.getRootMenu())) == null) {
            return false;
        }
        return windowCallback.onMenuItemSelected(findMenuPanel.featureId, menuItem);
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        reopenMenu(true);
    }

    void onMenuOpened(int i) {
        ActionBar supportActionBar;
        if (i != 108 || (supportActionBar = getSupportActionBar()) == null) {
            return;
        }
        supportActionBar.dispatchMenuVisibilityChanged(true);
    }

    void onPanelClosed(int i) {
        if (i == 108) {
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.dispatchMenuVisibilityChanged(false);
                return;
            }
            return;
        }
        if (i == 0) {
            PanelFeatureState panelState = getPanelState(i, true);
            if (panelState.isOpen) {
                closePanel(panelState, false);
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void onStop() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(false);
        }
    }

    void onSubDecorInstalled(ViewGroup viewGroup) {
    }

    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public boolean requestWindowFeature(int i) {
        int sanitizeWindowFeatureId = sanitizeWindowFeatureId(i);
        if (this.mWindowNoTitle && sanitizeWindowFeatureId == 108) {
            return false;
        }
        if (this.mHasActionBar && sanitizeWindowFeatureId == 1) {
            this.mHasActionBar = false;
        }
        if (sanitizeWindowFeatureId == 1) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mWindowNoTitle = true;
            return true;
        }
        if (sanitizeWindowFeatureId == 2) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureProgress = true;
            return true;
        }
        if (sanitizeWindowFeatureId == 5) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureIndeterminateProgress = true;
            return true;
        }
        if (sanitizeWindowFeatureId == 10) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mOverlayActionMode = true;
            return true;
        }
        if (sanitizeWindowFeatureId == 108) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mHasActionBar = true;
            return true;
        }
        if (sanitizeWindowFeatureId != 109) {
            return this.mWindow.requestFeature(sanitizeWindowFeatureId);
        }
        throwFeatureRequestIfSubDecorInstalled();
        this.mOverlayActionBar = true;
        return true;
    }

    void setConfigurationLocales(Configuration configuration, LocaleListCompat localeListCompat) {
        if (Build.VERSION.SDK_INT >= 24) {
            Api24Impl.setLocales(configuration, localeListCompat);
        } else {
            Api17Impl.setLocale(configuration, localeListCompat.get(0));
            Api17Impl.setLayoutDirection(configuration, localeListCompat.get(0));
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void setContentView(int i) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(i, viewGroup);
        this.mAppCompatWindowCallback.bypassOnContentChanged(this.mWindow.getCallback());
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void setContentView(View view) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mAppCompatWindowCallback.bypassOnContentChanged(this.mWindow.getCallback());
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        this.mAppCompatWindowCallback.bypassOnContentChanged(this.mWindow.getCallback());
    }

    void setDefaultLocalesForLocaleList(LocaleListCompat localeListCompat) {
        if (Build.VERSION.SDK_INT >= 24) {
            Api24Impl.setDefaultLocales(localeListCompat);
        } else {
            Locale.setDefault(localeListCompat.get(0));
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void setOnBackInvokedDispatcher(OnBackInvokedDispatcher onBackInvokedDispatcher) {
        OnBackInvokedCallback onBackInvokedCallback;
        super.setOnBackInvokedDispatcher(onBackInvokedDispatcher);
        OnBackInvokedDispatcher onBackInvokedDispatcher2 = this.mDispatcher;
        if (onBackInvokedDispatcher2 != null && (onBackInvokedCallback = this.mBackCallback) != null) {
            Api33Impl.unregisterOnBackInvokedCallback(onBackInvokedDispatcher2, onBackInvokedCallback);
            this.mBackCallback = null;
        }
        if (onBackInvokedDispatcher == null) {
            Object obj = this.mHost;
            if ((obj instanceof Activity) && ((Activity) obj).getWindow() != null) {
                onBackInvokedDispatcher = Api33Impl.getOnBackInvokedDispatcher((Activity) this.mHost);
            }
        }
        this.mDispatcher = onBackInvokedDispatcher;
        updateBackInvokedCallbackState();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public void setTheme(int i) {
        this.mThemeResId = i;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.setWindowTitle(charSequence);
            return;
        }
        if (peekSupportActionBar() != null) {
            peekSupportActionBar().setWindowTitle(charSequence);
            return;
        }
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    final boolean shouldAnimateActionModeView() {
        ViewGroup viewGroup;
        return this.mSubDecorInstalled && (viewGroup = this.mSubDecor) != null && ViewCompat.isLaidOut(viewGroup);
    }

    boolean shouldRegisterBackInvokedCallback() {
        if (this.mDispatcher == null) {
            return false;
        }
        PanelFeatureState panelState = getPanelState(0, false);
        return (panelState != null && panelState.isOpen) || this.mActionMode != null;
    }

    public androidx.appcompat.view.ActionMode startSupportActionMode(ActionMode.Callback callback) {
        AppCompatCallback appCompatCallback;
        if (callback == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }
        androidx.appcompat.view.ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        ActionModeCallbackWrapperV9 actionModeCallbackWrapperV9 = new ActionModeCallbackWrapperV9(callback);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            androidx.appcompat.view.ActionMode startActionMode = supportActionBar.startActionMode(actionModeCallbackWrapperV9);
            this.mActionMode = startActionMode;
            if (startActionMode != null && (appCompatCallback = this.mAppCompatCallback) != null) {
                appCompatCallback.onSupportActionModeStarted(startActionMode);
            }
        }
        if (this.mActionMode == null) {
            this.mActionMode = startSupportActionModeFromWindow(actionModeCallbackWrapperV9);
        }
        updateBackInvokedCallbackState();
        return this.mActionMode;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    androidx.appcompat.view.ActionMode startSupportActionModeFromWindow(ActionMode.Callback callback) {
        androidx.appcompat.view.ActionMode actionMode;
        Context context;
        androidx.appcompat.view.ActionMode actionMode2;
        AppCompatCallback appCompatCallback;
        endOnGoingFadeAnimation();
        androidx.appcompat.view.ActionMode actionMode3 = this.mActionMode;
        if (actionMode3 != null) {
            actionMode3.finish();
        }
        if (!(callback instanceof ActionModeCallbackWrapperV9)) {
            callback = new ActionModeCallbackWrapperV9(callback);
        }
        AppCompatCallback appCompatCallback2 = this.mAppCompatCallback;
        if (appCompatCallback2 != null && !this.mDestroyed) {
            try {
                actionMode = appCompatCallback2.onWindowStartingSupportActionMode(callback);
            } catch (AbstractMethodError unused) {
            }
            if (actionMode == null) {
                this.mActionMode = actionMode;
            } else {
                if (this.mActionModeView == null) {
                    if (this.mIsFloating) {
                        TypedValue typedValue = new TypedValue();
                        Resources.Theme theme = this.mContext.getTheme();
                        theme.resolveAttribute(R$attr.actionBarTheme, typedValue, true);
                        if (typedValue.resourceId != 0) {
                            Resources.Theme newTheme = this.mContext.getResources().newTheme();
                            newTheme.setTo(theme);
                            newTheme.applyStyle(typedValue.resourceId, true);
                            context = new ContextThemeWrapper(this.mContext, 0);
                            context.getTheme().setTo(newTheme);
                        } else {
                            context = this.mContext;
                        }
                        this.mActionModeView = new ActionBarContextView(context);
                        PopupWindow popupWindow = new PopupWindow(context, (AttributeSet) null, R$attr.actionModePopupWindowStyle);
                        this.mActionModePopup = popupWindow;
                        PopupWindowCompat.setWindowLayoutType(popupWindow, 2);
                        this.mActionModePopup.setContentView(this.mActionModeView);
                        this.mActionModePopup.setWidth(-1);
                        context.getTheme().resolveAttribute(R$attr.actionBarSize, typedValue, true);
                        this.mActionModeView.setContentHeight(TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics()));
                        this.mActionModePopup.setHeight(-2);
                        this.mShowActionModePopup = new Runnable() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.6
                            @Override // java.lang.Runnable
                            public void run() {
                                AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                                appCompatDelegateImpl.mActionModePopup.showAtLocation(appCompatDelegateImpl.mActionModeView, 55, 0, 0);
                                AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                                if (!AppCompatDelegateImpl.this.shouldAnimateActionModeView()) {
                                    AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                    AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                                } else {
                                    AppCompatDelegateImpl.this.mActionModeView.setAlpha(0.0f);
                                    AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
                                    appCompatDelegateImpl2.mFadeAnim = ViewCompat.animate(appCompatDelegateImpl2.mActionModeView).alpha(1.0f);
                                    AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.6.1
                                        @Override // androidx.core.view.ViewPropertyAnimatorListener
                                        public void onAnimationEnd(View view) {
                                            AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                            AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                                            AppCompatDelegateImpl.this.mFadeAnim = null;
                                        }

                                        @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
                                        public void onAnimationStart(View view) {
                                            AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                                        }
                                    });
                                }
                            }
                        };
                    } else {
                        ViewStubCompat viewStubCompat = (ViewStubCompat) this.mSubDecor.findViewById(R$id.action_mode_bar_stub);
                        if (viewStubCompat != null) {
                            viewStubCompat.setLayoutInflater(LayoutInflater.from(getActionBarThemedContext()));
                            this.mActionModeView = (ActionBarContextView) viewStubCompat.inflate();
                        }
                    }
                }
                if (this.mActionModeView != null) {
                    endOnGoingFadeAnimation();
                    this.mActionModeView.killMode();
                    StandaloneActionMode standaloneActionMode = new StandaloneActionMode(this.mActionModeView.getContext(), this.mActionModeView, callback, this.mActionModePopup == null);
                    if (callback.onCreateActionMode(standaloneActionMode, standaloneActionMode.getMenu())) {
                        standaloneActionMode.invalidate();
                        this.mActionModeView.initForMode(standaloneActionMode);
                        this.mActionMode = standaloneActionMode;
                        if (shouldAnimateActionModeView()) {
                            this.mActionModeView.setAlpha(0.0f);
                            ViewPropertyAnimatorCompat alpha = ViewCompat.animate(this.mActionModeView).alpha(1.0f);
                            this.mFadeAnim = alpha;
                            alpha.setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.7
                                @Override // androidx.core.view.ViewPropertyAnimatorListener
                                public void onAnimationEnd(View view) {
                                    AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                    AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                                    AppCompatDelegateImpl.this.mFadeAnim = null;
                                }

                                @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
                                public void onAnimationStart(View view) {
                                    AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                                    if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                                        ViewCompat.requestApplyInsets((View) AppCompatDelegateImpl.this.mActionModeView.getParent());
                                    }
                                }
                            });
                        } else {
                            this.mActionModeView.setAlpha(1.0f);
                            this.mActionModeView.setVisibility(0);
                            if (this.mActionModeView.getParent() instanceof View) {
                                ViewCompat.requestApplyInsets((View) this.mActionModeView.getParent());
                            }
                        }
                        if (this.mActionModePopup != null) {
                            this.mWindow.getDecorView().post(this.mShowActionModePopup);
                        }
                    } else {
                        this.mActionMode = null;
                    }
                }
            }
            actionMode2 = this.mActionMode;
            if (actionMode2 != null && (appCompatCallback = this.mAppCompatCallback) != null) {
                appCompatCallback.onSupportActionModeStarted(actionMode2);
            }
            updateBackInvokedCallbackState();
            return this.mActionMode;
        }
        actionMode = null;
        if (actionMode == null) {
        }
        actionMode2 = this.mActionMode;
        if (actionMode2 != null) {
            appCompatCallback.onSupportActionModeStarted(actionMode2);
        }
        updateBackInvokedCallbackState();
        return this.mActionMode;
    }

    void updateBackInvokedCallbackState() {
        OnBackInvokedCallback onBackInvokedCallback;
        if (Build.VERSION.SDK_INT >= 33) {
            boolean shouldRegisterBackInvokedCallback = shouldRegisterBackInvokedCallback();
            if (shouldRegisterBackInvokedCallback && this.mBackCallback == null) {
                this.mBackCallback = Api33Impl.registerOnBackPressedCallback(this.mDispatcher, this);
            } else {
                if (shouldRegisterBackInvokedCallback || (onBackInvokedCallback = this.mBackCallback) == null) {
                    return;
                }
                Api33Impl.unregisterOnBackInvokedCallback(this.mDispatcher, onBackInvokedCallback);
            }
        }
    }

    final int updateStatusGuard(WindowInsetsCompat windowInsetsCompat, Rect rect) {
        boolean z;
        boolean z2;
        int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : rect != null ? rect.top : 0;
        ActionBarContextView actionBarContextView = this.mActionModeView;
        if (actionBarContextView == null || !(actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            z = false;
        } else {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mActionModeView.getLayoutParams();
            if (this.mActionModeView.isShown()) {
                if (this.mTempRect1 == null) {
                    this.mTempRect1 = new Rect();
                    this.mTempRect2 = new Rect();
                }
                Rect rect2 = this.mTempRect1;
                Rect rect3 = this.mTempRect2;
                if (windowInsetsCompat == null) {
                    rect2.set(rect);
                } else {
                    rect2.set(windowInsetsCompat.getSystemWindowInsetLeft(), windowInsetsCompat.getSystemWindowInsetTop(), windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                }
                ViewUtils.computeFitSystemWindows(this.mSubDecor, rect2, rect3);
                int i = rect2.top;
                int i2 = rect2.left;
                int i3 = rect2.right;
                WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(this.mSubDecor);
                int systemWindowInsetLeft = rootWindowInsets == null ? 0 : rootWindowInsets.getSystemWindowInsetLeft();
                int systemWindowInsetRight = rootWindowInsets == null ? 0 : rootWindowInsets.getSystemWindowInsetRight();
                if (marginLayoutParams.topMargin == i && marginLayoutParams.leftMargin == i2 && marginLayoutParams.rightMargin == i3) {
                    z2 = false;
                } else {
                    marginLayoutParams.topMargin = i;
                    marginLayoutParams.leftMargin = i2;
                    marginLayoutParams.rightMargin = i3;
                    z2 = true;
                }
                if (i <= 0 || this.mStatusGuard != null) {
                    View view = this.mStatusGuard;
                    if (view != null) {
                        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                        int i4 = marginLayoutParams2.height;
                        int i5 = marginLayoutParams.topMargin;
                        if (i4 != i5 || marginLayoutParams2.leftMargin != systemWindowInsetLeft || marginLayoutParams2.rightMargin != systemWindowInsetRight) {
                            marginLayoutParams2.height = i5;
                            marginLayoutParams2.leftMargin = systemWindowInsetLeft;
                            marginLayoutParams2.rightMargin = systemWindowInsetRight;
                            this.mStatusGuard.setLayoutParams(marginLayoutParams2);
                        }
                    }
                } else {
                    View view2 = new View(this.mContext);
                    this.mStatusGuard = view2;
                    view2.setVisibility(8);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, marginLayoutParams.topMargin, 51);
                    layoutParams.leftMargin = systemWindowInsetLeft;
                    layoutParams.rightMargin = systemWindowInsetRight;
                    this.mSubDecor.addView(this.mStatusGuard, -1, layoutParams);
                }
                View view3 = this.mStatusGuard;
                r5 = view3 != null;
                if (r5 && view3.getVisibility() != 0) {
                    updateStatusGuardColor(this.mStatusGuard);
                }
                if (!this.mOverlayActionMode && r5) {
                    systemWindowInsetTop = 0;
                }
                z = r5;
                r5 = z2;
            } else if (marginLayoutParams.topMargin != 0) {
                marginLayoutParams.topMargin = 0;
                z = false;
            } else {
                z = false;
                r5 = false;
            }
            if (r5) {
                this.mActionModeView.setLayoutParams(marginLayoutParams);
            }
        }
        View view4 = this.mStatusGuard;
        if (view4 != null) {
            view4.setVisibility(z ? 0 : 8);
        }
        return systemWindowInsetTop;
    }
}
