package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_account_authorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getAuthorizationForm;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_password;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
/* loaded from: classes3.dex */
public class ExternalActionActivity extends Activity implements ActionBarLayout.ActionBarLayoutDelegate {
    protected ActionBarLayout actionBarLayout;
    protected SizeNotifierFrameLayout backgroundTablet;
    protected DrawerLayoutContainer drawerLayoutContainer;
    private boolean finished;
    protected ActionBarLayout layersActionBarLayout;
    private Runnable lockRunnable;
    private Intent passcodeSaveIntent;
    private int passcodeSaveIntentAccount;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private int passcodeSaveIntentState;
    private PasscodeView passcodeView;
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList<>();
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList<>();

    public static /* synthetic */ void lambda$onCreate$1(View view) {
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needAddFragmentToStack(BaseFragment baseFragment, ActionBarLayout actionBarLayout) {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needPresentFragment(BaseFragment baseFragment, boolean z, boolean z2, ActionBarLayout actionBarLayout) {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean onPreIme() {
        return false;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        ApplicationLoader.postInitApplication();
        requestWindowFeature(1);
        setTheme(2131689489);
        getWindow().setBackgroundDrawableResource(2131166187);
        if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
            try {
                getWindow().setFlags(8192, 8192);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        super.onCreate(bundle);
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.appLocked) {
            SharedConfig.lastPauseTime = (int) (SystemClock.elapsedRealtime() / 1000);
        }
        AndroidUtilities.fillStatusBarHeight(this);
        Theme.createDialogsResources(this);
        Theme.createChatResources(this, false);
        this.actionBarLayout = new ActionBarLayout(this);
        DrawerLayoutContainer drawerLayoutContainer = new DrawerLayoutContainer(this);
        this.drawerLayoutContainer = drawerLayoutContainer;
        drawerLayoutContainer.setAllowOpenDrawer(false, false);
        setContentView(this.drawerLayoutContainer, new ViewGroup.LayoutParams(-1, -1));
        if (AndroidUtilities.isTablet()) {
            getWindow().setSoftInputMode(16);
            RelativeLayout relativeLayout = new RelativeLayout(this);
            this.drawerLayoutContainer.addView(relativeLayout);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) relativeLayout.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -1;
            relativeLayout.setLayoutParams(layoutParams);
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, this);
            this.backgroundTablet = anonymousClass1;
            anonymousClass1.setOccupyStatusBar(false);
            this.backgroundTablet.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
            relativeLayout.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
            relativeLayout.addView(this.actionBarLayout, LayoutHelper.createRelative(-1, -1));
            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.setBackgroundColor(2130706432);
            relativeLayout.addView(frameLayout, LayoutHelper.createRelative(-1, -1));
            frameLayout.setOnTouchListener(new ExternalActionActivity$$ExternalSyntheticLambda4(this));
            frameLayout.setOnClickListener(ExternalActionActivity$$ExternalSyntheticLambda3.INSTANCE);
            ActionBarLayout actionBarLayout = new ActionBarLayout(this);
            this.layersActionBarLayout = actionBarLayout;
            actionBarLayout.setRemoveActionBarExtraHeight(true);
            this.layersActionBarLayout.setBackgroundView(frameLayout);
            this.layersActionBarLayout.setUseAlphaAnimations(true);
            this.layersActionBarLayout.setBackgroundResource(2131165291);
            relativeLayout.addView(this.layersActionBarLayout, LayoutHelper.createRelative(530, AndroidUtilities.isSmallTablet() ? 528 : 700));
            this.layersActionBarLayout.init(layerFragmentsStack);
            this.layersActionBarLayout.setDelegate(this);
            this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        } else {
            RelativeLayout relativeLayout2 = new RelativeLayout(this);
            this.drawerLayoutContainer.addView(relativeLayout2, LayoutHelper.createFrame(-1, -1.0f));
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, this);
            this.backgroundTablet = anonymousClass2;
            anonymousClass2.setOccupyStatusBar(false);
            this.backgroundTablet.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
            relativeLayout2.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
            relativeLayout2.addView(this.actionBarLayout, LayoutHelper.createRelative(-1, -1));
        }
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.init(mainFragmentsStack);
        this.actionBarLayout.setDelegate(this);
        PasscodeView passcodeView = new PasscodeView(this);
        this.passcodeView = passcodeView;
        this.drawerLayoutContainer.addView(passcodeView, LayoutHelper.createFrame(-1, -1.0f));
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, this);
        this.actionBarLayout.removeAllFragments();
        ActionBarLayout actionBarLayout2 = this.layersActionBarLayout;
        if (actionBarLayout2 != null) {
            actionBarLayout2.removeAllFragments();
        }
        handleIntent(getIntent(), false, bundle != null, false, UserConfig.selectedAccount, 0);
        needLayout();
    }

    /* renamed from: org.telegram.ui.ExternalActionActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends SizeNotifierFrameLayout {
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        protected boolean isActionBarVisible() {
            return false;
        }

        AnonymousClass1(ExternalActionActivity externalActionActivity, Context context) {
            super(context);
        }
    }

    public /* synthetic */ boolean lambda$onCreate$0(View view, MotionEvent motionEvent) {
        if (!this.actionBarLayout.fragmentsStack.isEmpty() && motionEvent.getAction() == 1) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int[] iArr = new int[2];
            this.layersActionBarLayout.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            if (!this.layersActionBarLayout.checkTransitionAnimation() && (x <= i || x >= i + this.layersActionBarLayout.getWidth() || y <= i2 || y >= i2 + this.layersActionBarLayout.getHeight())) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
                        actionBarLayout.removeFragmentFromStack(actionBarLayout.fragmentsStack.get(0));
                    }
                    this.layersActionBarLayout.closeLastFragment(true);
                }
                return true;
            }
        }
        return false;
    }

    /* renamed from: org.telegram.ui.ExternalActionActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends SizeNotifierFrameLayout {
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        protected boolean isActionBarVisible() {
            return false;
        }

        AnonymousClass2(ExternalActionActivity externalActionActivity, Context context) {
            super(context);
        }
    }

    public void showPasscodeActivity() {
        if (this.passcodeView == null) {
            return;
        }
        SharedConfig.appLocked = true;
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        this.passcodeView.onShow(true, false);
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.passcodeView.setDelegate(new ExternalActionActivity$$ExternalSyntheticLambda10(this));
    }

    public /* synthetic */ void lambda$showPasscodeActivity$2() {
        SharedConfig.isWaitingForPasscodeEnter = false;
        Intent intent = this.passcodeSaveIntent;
        if (intent != null) {
            handleIntent(intent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true, this.passcodeSaveIntentAccount, this.passcodeSaveIntentState);
            this.passcodeSaveIntent = null;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        this.actionBarLayout.showLastFragment();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
        }
    }

    public void onFinishLogin() {
        handleIntent(this.passcodeSaveIntent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true, this.passcodeSaveIntentAccount, this.passcodeSaveIntentState);
        this.actionBarLayout.removeAllFragments();
        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.removeAllFragments();
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.backgroundTablet;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.setVisibility(0);
        }
    }

    public boolean checkPasscode(Intent intent, boolean z, boolean z2, boolean z3, int i, int i2) {
        if (z3 || (!AndroidUtilities.needShowPasscode(true) && !SharedConfig.isWaitingForPasscodeEnter)) {
            return true;
        }
        showPasscodeActivity();
        this.passcodeSaveIntent = intent;
        this.passcodeSaveIntentIsNew = z;
        this.passcodeSaveIntentIsRestore = z2;
        this.passcodeSaveIntentAccount = i;
        this.passcodeSaveIntentState = i2;
        UserConfig.getInstance(i).saveConfig(false);
        return false;
    }

    protected boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3, int i, int i2) {
        if (!checkPasscode(intent, z, z2, z3, i, i2)) {
            return false;
        }
        if ("org.telegram.passport.AUTHORIZE".equals(intent.getAction())) {
            if (i2 == 0) {
                int activatedAccountsCount = UserConfig.getActivatedAccountsCount();
                if (activatedAccountsCount == 0) {
                    this.passcodeSaveIntent = intent;
                    this.passcodeSaveIntentIsNew = z;
                    this.passcodeSaveIntentIsRestore = z2;
                    this.passcodeSaveIntentAccount = i;
                    this.passcodeSaveIntentState = i2;
                    LoginActivity loginActivity = new LoginActivity();
                    if (AndroidUtilities.isTablet()) {
                        this.layersActionBarLayout.addFragmentToStack(loginActivity);
                    } else {
                        this.actionBarLayout.addFragmentToStack(loginActivity);
                    }
                    if (!AndroidUtilities.isTablet()) {
                        this.backgroundTablet.setVisibility(8);
                    }
                    this.actionBarLayout.showLastFragment();
                    if (AndroidUtilities.isTablet()) {
                        this.layersActionBarLayout.showLastFragment();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(LocaleController.getString("AppName", 2131624375));
                    builder.setMessage(LocaleController.getString("PleaseLoginPassport", 2131627568));
                    builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
                    builder.show();
                    return true;
                } else if (activatedAccountsCount >= 2) {
                    AlertDialog createAccountSelectDialog = AlertsCreator.createAccountSelectDialog(this, new ExternalActionActivity$$ExternalSyntheticLambda9(this, i, intent, z, z2, z3));
                    createAccountSelectDialog.show();
                    createAccountSelectDialog.setCanceledOnTouchOutside(false);
                    createAccountSelectDialog.setOnDismissListener(new ExternalActionActivity$$ExternalSyntheticLambda1(this));
                    return true;
                }
            }
            long longExtra = intent.getLongExtra("bot_id", intent.getIntExtra("bot_id", 0));
            String stringExtra = intent.getStringExtra("nonce");
            String stringExtra2 = intent.getStringExtra("payload");
            TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm = new TLRPC$TL_account_getAuthorizationForm();
            tLRPC$TL_account_getAuthorizationForm.bot_id = longExtra;
            tLRPC$TL_account_getAuthorizationForm.scope = intent.getStringExtra("scope");
            tLRPC$TL_account_getAuthorizationForm.public_key = intent.getStringExtra("public_key");
            if (longExtra == 0 || ((TextUtils.isEmpty(stringExtra2) && TextUtils.isEmpty(stringExtra)) || TextUtils.isEmpty(tLRPC$TL_account_getAuthorizationForm.scope) || TextUtils.isEmpty(tLRPC$TL_account_getAuthorizationForm.public_key))) {
                finish();
                return false;
            }
            int[] iArr = {0};
            AlertDialog alertDialog = new AlertDialog(this, 3);
            alertDialog.setOnCancelListener(new ExternalActionActivity$$ExternalSyntheticLambda0(i, iArr));
            alertDialog.show();
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getAuthorizationForm, new ExternalActionActivity$$ExternalSyntheticLambda8(this, iArr, i, alertDialog, tLRPC$TL_account_getAuthorizationForm, stringExtra2, stringExtra), 10);
        } else {
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    this.layersActionBarLayout.addFragmentToStack(new CacheControlActivity());
                }
            } else if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                this.actionBarLayout.addFragmentToStack(new CacheControlActivity());
            }
            if (!AndroidUtilities.isTablet()) {
                this.backgroundTablet.setVisibility(8);
            }
            this.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.showLastFragment();
            }
            intent.setAction(null);
        }
        return false;
    }

    public /* synthetic */ void lambda$handleIntent$3(int i, Intent intent, boolean z, boolean z2, boolean z3, int i2) {
        if (i2 != i) {
            switchToAccount(i2);
        }
        handleIntent(intent, z, z2, z3, i2, 1);
    }

    public /* synthetic */ void lambda$handleIntent$4(DialogInterface dialogInterface) {
        setResult(0);
        finish();
    }

    public static /* synthetic */ void lambda$handleIntent$5(int i, int[] iArr, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
    }

    public /* synthetic */ void lambda$handleIntent$10(int[] iArr, int i, AlertDialog alertDialog, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm = (TLRPC$TL_account_authorizationForm) tLObject;
        if (tLRPC$TL_account_authorizationForm != null) {
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(new TLRPC$TL_account_getPassword(), new ExternalActionActivity$$ExternalSyntheticLambda7(this, alertDialog, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2));
            return;
        }
        AndroidUtilities.runOnUIThread(new ExternalActionActivity$$ExternalSyntheticLambda6(this, alertDialog, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$handleIntent$7(AlertDialog alertDialog, int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ExternalActionActivity$$ExternalSyntheticLambda5(this, alertDialog, tLObject, i, tLRPC$TL_account_authorizationForm, tLRPC$TL_account_getAuthorizationForm, str, str2));
    }

    public /* synthetic */ void lambda$handleIntent$6(AlertDialog alertDialog, TLObject tLObject, int i, TLRPC$TL_account_authorizationForm tLRPC$TL_account_authorizationForm, TLRPC$TL_account_getAuthorizationForm tLRPC$TL_account_getAuthorizationForm, String str, String str2) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            MessagesController.getInstance(i).putUsers(tLRPC$TL_account_authorizationForm.users, false);
            PassportActivity passportActivity = new PassportActivity(5, tLRPC$TL_account_getAuthorizationForm.bot_id, tLRPC$TL_account_getAuthorizationForm.scope, tLRPC$TL_account_getAuthorizationForm.public_key, str, str2, (String) null, tLRPC$TL_account_authorizationForm, (TLRPC$TL_account_password) tLObject);
            passportActivity.setNeedActivityResult(true);
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.addFragmentToStack(passportActivity);
            } else {
                this.actionBarLayout.addFragmentToStack(passportActivity);
            }
            if (!AndroidUtilities.isTablet()) {
                this.backgroundTablet.setVisibility(8);
            }
            this.actionBarLayout.showLastFragment();
            if (!AndroidUtilities.isTablet()) {
                return;
            }
            this.layersActionBarLayout.showLastFragment();
        }
    }

    public /* synthetic */ void lambda$handleIntent$9(AlertDialog alertDialog, TLRPC$TL_error tLRPC$TL_error) {
        try {
            alertDialog.dismiss();
            if ("APP_VERSION_OUTDATED".equals(tLRPC$TL_error.text)) {
                AlertDialog showUpdateAppAlert = AlertsCreator.showUpdateAppAlert(this, LocaleController.getString("UpdateAppAlert", 2131628758), true);
                if (showUpdateAppAlert != null) {
                    showUpdateAppAlert.setOnDismissListener(new ExternalActionActivity$$ExternalSyntheticLambda2(this, tLRPC$TL_error));
                } else {
                    setResult(1, new Intent().putExtra("error", tLRPC$TL_error.text));
                    finish();
                }
            } else {
                if (!"BOT_INVALID".equals(tLRPC$TL_error.text) && !"PUBLIC_KEY_REQUIRED".equals(tLRPC$TL_error.text) && !"PUBLIC_KEY_INVALID".equals(tLRPC$TL_error.text) && !"SCOPE_EMPTY".equals(tLRPC$TL_error.text) && !"PAYLOAD_EMPTY".equals(tLRPC$TL_error.text)) {
                    setResult(0);
                    finish();
                }
                setResult(1, new Intent().putExtra("error", tLRPC$TL_error.text));
                finish();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$handleIntent$8(TLRPC$TL_error tLRPC$TL_error, DialogInterface dialogInterface) {
        setResult(1, new Intent().putExtra("error", tLRPC$TL_error.text));
        finish();
    }

    public void switchToAccount(int i) {
        int i2 = UserConfig.selectedAccount;
        if (i == i2) {
            return;
        }
        ConnectionsManager.getInstance(i2).setAppPaused(true, false);
        UserConfig.selectedAccount = i;
        UserConfig.getInstance(0).saveConfig(false);
        if (ApplicationLoader.mainInterfacePaused) {
            return;
        }
        ConnectionsManager.getInstance(UserConfig.selectedAccount).setAppPaused(false, false);
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false, UserConfig.selectedAccount, 0);
    }

    private void onFinish() {
        if (this.finished) {
            return;
        }
        Runnable runnable = this.lockRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.lockRunnable = null;
        }
        this.finished = true;
    }

    public void needLayout() {
        if (AndroidUtilities.isTablet()) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.layersActionBarLayout.getLayoutParams();
            layoutParams.leftMargin = (AndroidUtilities.displaySize.x - layoutParams.width) / 2;
            int i = Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
            layoutParams.topMargin = i + (((AndroidUtilities.displaySize.y - layoutParams.height) - i) / 2);
            this.layersActionBarLayout.setLayoutParams(layoutParams);
            if (!AndroidUtilities.isSmallTablet() || getResources().getConfiguration().orientation == 2) {
                int i2 = (AndroidUtilities.displaySize.x / 100) * 35;
                if (i2 < AndroidUtilities.dp(320.0f)) {
                    i2 = AndroidUtilities.dp(320.0f);
                }
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.actionBarLayout.getLayoutParams();
                layoutParams2.width = i2;
                layoutParams2.height = -1;
                this.actionBarLayout.setLayoutParams(layoutParams2);
                if (!AndroidUtilities.isSmallTablet() || this.actionBarLayout.fragmentsStack.size() != 2) {
                    return;
                }
                this.actionBarLayout.fragmentsStack.get(1).onPause();
                this.actionBarLayout.fragmentsStack.remove(1);
                this.actionBarLayout.showLastFragment();
                return;
            }
            RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.actionBarLayout.getLayoutParams();
            layoutParams3.width = -1;
            layoutParams3.height = -1;
            this.actionBarLayout.setLayoutParams(layoutParams3);
        }
    }

    public void fixLayout() {
        ActionBarLayout actionBarLayout;
        if (AndroidUtilities.isTablet() && (actionBarLayout = this.actionBarLayout) != null) {
            actionBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new AnonymousClass3());
        }
    }

    /* renamed from: org.telegram.ui.ExternalActionActivity$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements ViewTreeObserver.OnGlobalLayoutListener {
        AnonymousClass3() {
            ExternalActionActivity.this = r1;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            ExternalActionActivity.this.needLayout();
            ActionBarLayout actionBarLayout = ExternalActionActivity.this.actionBarLayout;
            if (actionBarLayout != null) {
                actionBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onPause();
        }
        ApplicationLoader.externalInterfacePaused = true;
        onPasscodePause();
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView != null) {
            passcodeView.onPause();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        onFinish();
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.actionBarLayout.onResume();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onResume();
        }
        ApplicationLoader.externalInterfacePaused = false;
        onPasscodeResume();
        if (this.passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (!AndroidUtilities.isTablet()) {
                return;
            }
            this.layersActionBarLayout.onResume();
            return;
        }
        this.actionBarLayout.dismissDialogs();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.dismissDialogs();
        }
        this.passcodeView.onResume();
    }

    private void onPasscodePause() {
        Runnable runnable = this.lockRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.lockRunnable = null;
        }
        if (SharedConfig.passcodeHash.length() != 0) {
            SharedConfig.lastPauseTime = (int) (SystemClock.elapsedRealtime() / 1000);
            AnonymousClass4 anonymousClass4 = new AnonymousClass4();
            this.lockRunnable = anonymousClass4;
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(anonymousClass4, 1000L);
            } else {
                int i = SharedConfig.autoLockIn;
                if (i != 0) {
                    AndroidUtilities.runOnUIThread(anonymousClass4, (i * 1000) + 1000);
                }
            }
        } else {
            SharedConfig.lastPauseTime = 0;
        }
        SharedConfig.saveConfig();
    }

    /* renamed from: org.telegram.ui.ExternalActionActivity$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 implements Runnable {
        AnonymousClass4() {
            ExternalActionActivity.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ExternalActionActivity.this.lockRunnable == this) {
                if (AndroidUtilities.needShowPasscode(true)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("lock app");
                    }
                    ExternalActionActivity.this.showPasscodeActivity();
                } else if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("didn't pass lock check");
                }
                ExternalActionActivity.this.lockRunnable = null;
            }
        }
    }

    private void onPasscodeResume() {
        Runnable runnable = this.lockRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.lockRunnable = null;
        }
        if (AndroidUtilities.needShowPasscode(true)) {
            showPasscodeActivity();
        }
        if (SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            SharedConfig.saveConfig();
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        AndroidUtilities.checkDisplaySize(this, configuration);
        super.onConfigurationChanged(configuration);
        fixLayout();
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (this.passcodeView.getVisibility() == 0) {
            finish();
        } else if (PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        } else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
        } else if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
                this.layersActionBarLayout.onBackPressed();
            } else {
                this.actionBarLayout.onBackPressed();
            }
        } else {
            this.actionBarLayout.onBackPressed();
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        this.actionBarLayout.onLowMemory();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onLowMemory();
        }
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needCloseLastFragment(ActionBarLayout actionBarLayout) {
        if (AndroidUtilities.isTablet()) {
            if (actionBarLayout == this.actionBarLayout && actionBarLayout.fragmentsStack.size() <= 1) {
                onFinish();
                finish();
                return false;
            } else if (actionBarLayout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                onFinish();
                finish();
                return false;
            }
        } else if (actionBarLayout.fragmentsStack.size() <= 1) {
            onFinish();
            finish();
            return false;
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public void onRebuildAllFragments(ActionBarLayout actionBarLayout, boolean z) {
        if (!AndroidUtilities.isTablet() || actionBarLayout != this.layersActionBarLayout) {
            return;
        }
        this.actionBarLayout.rebuildAllFragmentViews(z, z);
    }
}
