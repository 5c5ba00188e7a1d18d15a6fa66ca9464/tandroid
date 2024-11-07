package org.telegram.ui;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.QRCodeBottomSheet;

/* loaded from: classes4.dex */
public class ProxySettingsActivity extends BaseFragment {
    private boolean addingNewProxy;
    private TextInfoPrivacyCell[] bottomCells;
    private ClipboardManager.OnPrimaryClipChangedListener clipChangedListener;
    private ClipboardManager clipboardManager;
    private SharedConfig.ProxyInfo currentProxyInfo;
    private int currentType;
    private ActionBarMenuItem doneItem;
    private HeaderCell headerCell;
    private boolean ignoreOnTextChange;
    private EditTextBoldCursor[] inputFields;
    private LinearLayout inputFieldsContainer;
    private LinearLayout linearLayout2;
    private TextSettingsCell pasteCell;
    private String[] pasteFields;
    private String pasteString;
    private int pasteType;
    private ScrollView scrollView;
    private ShadowSectionCell[] sectionCell;
    private TextSettingsCell shareCell;
    private ValueAnimator shareDoneAnimator;
    private boolean shareDoneEnabled;
    private float shareDoneProgress;
    private float[] shareDoneProgressAnimValues;
    private RadioCell[] typeCell;

    public ProxySettingsActivity() {
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCells = new TextInfoPrivacyCell[2];
        this.typeCell = new RadioCell[2];
        this.currentType = -1;
        this.pasteType = -1;
        this.shareDoneProgress = 1.0f;
        this.shareDoneProgressAnimValues = new float[2];
        this.shareDoneEnabled = true;
        this.clipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda2
            @Override // android.content.ClipboardManager.OnPrimaryClipChangedListener
            public final void onPrimaryClipChanged() {
                ProxySettingsActivity.this.updatePasteCell();
            }
        };
        this.currentProxyInfo = new SharedConfig.ProxyInfo("", 1080, "", "", "");
        this.addingNewProxy = true;
    }

    public ProxySettingsActivity(SharedConfig.ProxyInfo proxyInfo) {
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCells = new TextInfoPrivacyCell[2];
        this.typeCell = new RadioCell[2];
        this.currentType = -1;
        this.pasteType = -1;
        this.shareDoneProgress = 1.0f;
        this.shareDoneProgressAnimValues = new float[2];
        this.shareDoneEnabled = true;
        this.clipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda2
            @Override // android.content.ClipboardManager.OnPrimaryClipChangedListener
            public final void onPrimaryClipChanged() {
                ProxySettingsActivity.this.updatePasteCell();
            }
        };
        this.currentProxyInfo = proxyInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkShareDone(boolean z) {
        if (this.shareCell == null || this.doneItem == null) {
            return;
        }
        EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
        boolean z2 = false;
        EditTextBoldCursor editTextBoldCursor = editTextBoldCursorArr[0];
        if (editTextBoldCursor == null || editTextBoldCursorArr[1] == null) {
            return;
        }
        if (editTextBoldCursor.length() != 0 && Utilities.parseInt((CharSequence) this.inputFields[1].getText().toString()).intValue() != 0) {
            z2 = true;
        }
        setShareDoneEnabled(z2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view) {
        setProxyType(((Integer) view.getTag()).intValue(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$1(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            if (i != 6) {
                return false;
            }
            lambda$onBackPressed$321();
            return true;
        }
        int intValue = ((Integer) textView.getTag()).intValue() + 1;
        EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
        if (intValue < editTextBoldCursorArr.length) {
            editTextBoldCursorArr[intValue].requestFocus();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2() {
        AndroidUtilities.hideKeyboard(this.inputFieldsContainer.findFocus());
        for (int i = 0; i < this.pasteFields.length; i++) {
            int i2 = this.pasteType;
            if ((i2 != 0 || i == 4) && (i2 != 1 || i == 2 || i == 3)) {
                this.inputFields[i].setText((CharSequence) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        EditTextBoldCursor editTextBoldCursor;
        String str;
        if (this.pasteType == -1) {
            return;
        }
        int i = 0;
        while (true) {
            String[] strArr = this.pasteFields;
            if (i >= strArr.length) {
                EditTextBoldCursor editTextBoldCursor2 = this.inputFields[0];
                editTextBoldCursor2.setSelection(editTextBoldCursor2.length());
                setProxyType(this.pasteType, true, new Runnable() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProxySettingsActivity.this.lambda$createView$2();
                    }
                });
                return;
            }
            int i2 = this.pasteType;
            if ((i2 != 0 || i != 4) && (i2 != 1 || (i != 2 && i != 3))) {
                String str2 = strArr[i];
                if (str2 != null) {
                    try {
                        this.inputFields[i].setText(URLDecoder.decode(str2, "UTF-8"));
                    } catch (UnsupportedEncodingException unused) {
                        editTextBoldCursor = this.inputFields[i];
                        str = this.pasteFields[i];
                    }
                } else {
                    editTextBoldCursor = this.inputFields[i];
                    str = null;
                }
                editTextBoldCursor.setText(str);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00ce A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00cf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$4(Context context, View view) {
        String str;
        String encode;
        StringBuilder sb = new StringBuilder();
        String obj = this.inputFields[0].getText().toString();
        String obj2 = this.inputFields[3].getText().toString();
        String obj3 = this.inputFields[2].getText().toString();
        String obj4 = this.inputFields[1].getText().toString();
        String obj5 = this.inputFields[4].getText().toString();
        try {
            if (!TextUtils.isEmpty(obj)) {
                sb.append("server=");
                sb.append(URLEncoder.encode(obj, "UTF-8"));
            }
            if (!TextUtils.isEmpty(obj4)) {
                if (sb.length() != 0) {
                    sb.append("&");
                }
                sb.append("port=");
                sb.append(URLEncoder.encode(obj4, "UTF-8"));
            }
            if (this.currentType != 1) {
                str = "https://t.me/socks?";
                if (!TextUtils.isEmpty(obj3)) {
                    if (sb.length() != 0) {
                        sb.append("&");
                    }
                    sb.append("user=");
                    sb.append(URLEncoder.encode(obj3, "UTF-8"));
                }
                if (!TextUtils.isEmpty(obj2)) {
                    if (sb.length() != 0) {
                        sb.append("&");
                    }
                    sb.append("pass=");
                    encode = URLEncoder.encode(obj2, "UTF-8");
                }
                if (sb.length() != 0) {
                    return;
                }
                QRCodeBottomSheet qRCodeBottomSheet = new QRCodeBottomSheet(context, LocaleController.getString(R.string.ShareQrCode), str + sb.toString(), LocaleController.getString(R.string.QRCodeLinkHelpProxy), true);
                qRCodeBottomSheet.setCenterImage(SvgHelper.getBitmap(AndroidUtilities.readRes(R.raw.qr_dog), AndroidUtilities.dp(60.0f), AndroidUtilities.dp(60.0f), false));
                showDialog(qRCodeBottomSheet);
                return;
            }
            str = "https://t.me/proxy?";
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append("secret=");
            encode = URLEncoder.encode(obj5, "UTF-8");
            sb.append(encode);
            if (sb.length() != 0) {
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$6() {
        ValueAnimator valueAnimator;
        if (this.shareCell != null && ((valueAnimator = this.shareDoneAnimator) == null || !valueAnimator.isRunning())) {
            this.shareCell.setTextColor(Theme.getColor(this.shareDoneEnabled ? Theme.key_windowBackgroundWhiteBlueText4 : Theme.key_windowBackgroundWhiteGrayText2));
        }
        if (this.inputFields == null) {
            return;
        }
        int i = 0;
        while (true) {
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (i >= editTextBoldCursorArr.length) {
                return;
            }
            editTextBoldCursorArr[i].setLineColors(Theme.getColor(Theme.key_windowBackgroundWhiteInputField), Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated), Theme.getColor(Theme.key_text_RedRegular));
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setShareDoneEnabled$5(ValueAnimator valueAnimator) {
        this.shareDoneProgress = AndroidUtilities.lerp(this.shareDoneProgressAnimValues, valueAnimator.getAnimatedFraction());
        this.shareCell.setTextColor(ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2), Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4), this.shareDoneProgress));
        this.doneItem.setAlpha((this.shareDoneProgress / 2.0f) + 0.5f);
    }

    private void setProxyType(int i, boolean z) {
        setProxyType(i, z, null);
    }

    private void setProxyType(int i, boolean z, final Runnable runnable) {
        if (this.currentType != i) {
            this.currentType = i;
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 23) {
                TransitionManager.endTransitions(this.linearLayout2);
            }
            if (z && i2 >= 21) {
                TransitionSet duration = new TransitionSet().addTransition(new Fade(2)).addTransition(new ChangeBounds()).addTransition(new Fade(1)).setInterpolator((TimeInterpolator) CubicBezierInterpolator.DEFAULT).setDuration(250L);
                if (runnable != null) {
                    duration.addListener(new Transition.TransitionListener() { // from class: org.telegram.ui.ProxySettingsActivity.4
                        @Override // android.transition.Transition.TransitionListener
                        public void onTransitionCancel(Transition transition) {
                        }

                        @Override // android.transition.Transition.TransitionListener
                        public void onTransitionEnd(Transition transition) {
                            runnable.run();
                        }

                        @Override // android.transition.Transition.TransitionListener
                        public void onTransitionPause(Transition transition) {
                        }

                        @Override // android.transition.Transition.TransitionListener
                        public void onTransitionResume(Transition transition) {
                        }

                        @Override // android.transition.Transition.TransitionListener
                        public void onTransitionStart(Transition transition) {
                        }
                    });
                }
                TransitionManager.beginDelayedTransition(this.linearLayout2, duration);
            }
            int i3 = this.currentType;
            if (i3 == 0) {
                this.bottomCells[0].setVisibility(0);
                this.bottomCells[1].setVisibility(8);
                ((View) this.inputFields[4].getParent()).setVisibility(8);
                ((View) this.inputFields[3].getParent()).setVisibility(0);
                ((View) this.inputFields[2].getParent()).setVisibility(0);
            } else if (i3 == 1) {
                this.bottomCells[0].setVisibility(8);
                this.bottomCells[1].setVisibility(0);
                ((View) this.inputFields[4].getParent()).setVisibility(0);
                ((View) this.inputFields[3].getParent()).setVisibility(8);
                ((View) this.inputFields[2].getParent()).setVisibility(8);
            }
            this.typeCell[0].setChecked(this.currentType == 0, z);
            this.typeCell[1].setChecked(this.currentType == 1, z);
        }
    }

    private void setShareDoneEnabled(boolean z, boolean z2) {
        if (this.shareDoneEnabled != z) {
            ValueAnimator valueAnimator = this.shareDoneAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            } else if (z2) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.shareDoneAnimator = ofFloat;
                ofFloat.setDuration(200L);
                this.shareDoneAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda9
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        ProxySettingsActivity.this.lambda$setShareDoneEnabled$5(valueAnimator2);
                    }
                });
            }
            if (z2) {
                float[] fArr = this.shareDoneProgressAnimValues;
                fArr[0] = this.shareDoneProgress;
                fArr[1] = z ? 1.0f : 0.0f;
                this.shareDoneAnimator.start();
            } else {
                this.shareDoneProgress = z ? 1.0f : 0.0f;
                this.shareCell.setTextColor(Theme.getColor(z ? Theme.key_windowBackgroundWhiteBlueText4 : Theme.key_windowBackgroundWhiteGrayText2));
                this.doneItem.setAlpha(z ? 1.0f : 0.5f);
            }
            this.shareCell.setEnabled(z);
            this.doneItem.setEnabled(z);
            this.shareDoneEnabled = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002d A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updatePasteCell() {
        String str;
        char c;
        ClipData primaryClip = this.clipboardManager.getPrimaryClip();
        String[] strArr = null;
        if (primaryClip != null && primaryClip.getItemCount() > 0) {
            try {
                str = primaryClip.getItemAt(0).coerceToText(this.fragmentView.getContext()).toString();
            } catch (Exception unused) {
            }
            if (TextUtils.equals(str, this.pasteString)) {
                this.pasteType = -1;
                this.pasteString = str;
                this.pasteFields = new String[this.inputFields.length];
                if (str != null) {
                    String[] strArr2 = {"t.me/socks?", "tg://socks?"};
                    int i = 0;
                    while (true) {
                        if (i >= 2) {
                            break;
                        }
                        int indexOf = str.indexOf(strArr2[i]);
                        if (indexOf >= 0) {
                            this.pasteType = 0;
                            strArr = str.substring(indexOf + strArr2[i].length()).split("&");
                            break;
                        }
                        i++;
                    }
                    if (strArr == null) {
                        String[] strArr3 = {"t.me/proxy?", "tg://proxy?"};
                        int i2 = 0;
                        while (true) {
                            if (i2 >= 2) {
                                break;
                            }
                            int indexOf2 = str.indexOf(strArr3[i2]);
                            if (indexOf2 >= 0) {
                                this.pasteType = 1;
                                strArr = str.substring(indexOf2 + strArr3[i2].length()).split("&");
                                break;
                            }
                            i2++;
                        }
                    }
                    if (strArr != null) {
                        for (String str2 : strArr) {
                            String[] split = str2.split("=");
                            if (split.length == 2) {
                                String lowerCase = split[0].toLowerCase();
                                lowerCase.hashCode();
                                switch (lowerCase.hashCode()) {
                                    case -906277200:
                                        if (lowerCase.equals("secret")) {
                                            c = 0;
                                            break;
                                        }
                                        break;
                                    case -905826493:
                                        if (lowerCase.equals("server")) {
                                            c = 1;
                                            break;
                                        }
                                        break;
                                    case 3433489:
                                        if (lowerCase.equals("pass")) {
                                            c = 2;
                                            break;
                                        }
                                        break;
                                    case 3446913:
                                        if (lowerCase.equals("port")) {
                                            c = 3;
                                            break;
                                        }
                                        break;
                                    case 3599307:
                                        if (lowerCase.equals("user")) {
                                            c = 4;
                                            break;
                                        }
                                        break;
                                }
                                c = 65535;
                                switch (c) {
                                    case 0:
                                        if (this.pasteType == 1) {
                                            this.pasteFields[4] = split[1];
                                            break;
                                        } else {
                                            break;
                                        }
                                    case 1:
                                        this.pasteFields[0] = split[1];
                                        break;
                                    case 2:
                                        if (this.pasteType == 0) {
                                            this.pasteFields[3] = split[1];
                                            break;
                                        } else {
                                            break;
                                        }
                                    case 3:
                                        this.pasteFields[1] = split[1];
                                        break;
                                    case 4:
                                        if (this.pasteType == 0) {
                                            this.pasteFields[2] = split[1];
                                            break;
                                        } else {
                                            break;
                                        }
                                }
                            }
                        }
                    }
                }
                if (this.pasteType != -1) {
                    if (this.pasteCell.getVisibility() != 0) {
                        this.pasteCell.setVisibility(0);
                        this.sectionCell[2].setVisibility(0);
                        return;
                    }
                    return;
                }
                if (this.pasteCell.getVisibility() != 8) {
                    this.pasteCell.setVisibility(8);
                    this.sectionCell[2].setVisibility(8);
                    return;
                }
                return;
            }
            return;
        }
        str = null;
        if (TextUtils.equals(str, this.pasteString)) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x030e  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0313  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x02e0  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        EditTextBoldCursor editTextBoldCursor;
        TextWatcher textWatcher;
        EditTextBoldCursor editTextBoldCursor2;
        String str;
        this.actionBar.setTitle(LocaleController.getString(R.string.ProxyDetails));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.ProxySettingsActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                SharedConfig.ProxyInfo proxyInfo;
                boolean z;
                if (i != -1) {
                    if (i != 1 || ProxySettingsActivity.this.getParentActivity() == null) {
                        return;
                    }
                    ProxySettingsActivity.this.currentProxyInfo.address = ProxySettingsActivity.this.inputFields[0].getText().toString();
                    ProxySettingsActivity.this.currentProxyInfo.port = Utilities.parseInt((CharSequence) ProxySettingsActivity.this.inputFields[1].getText().toString()).intValue();
                    String str2 = "";
                    if (ProxySettingsActivity.this.currentType == 0) {
                        ProxySettingsActivity.this.currentProxyInfo.secret = "";
                        ProxySettingsActivity.this.currentProxyInfo.username = ProxySettingsActivity.this.inputFields[2].getText().toString();
                        proxyInfo = ProxySettingsActivity.this.currentProxyInfo;
                        str2 = ProxySettingsActivity.this.inputFields[3].getText().toString();
                    } else {
                        ProxySettingsActivity.this.currentProxyInfo.secret = ProxySettingsActivity.this.inputFields[4].getText().toString();
                        ProxySettingsActivity.this.currentProxyInfo.username = "";
                        proxyInfo = ProxySettingsActivity.this.currentProxyInfo;
                    }
                    proxyInfo.password = str2;
                    SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                    SharedPreferences.Editor edit = globalMainSettings.edit();
                    if (ProxySettingsActivity.this.addingNewProxy) {
                        SharedConfig.addProxy(ProxySettingsActivity.this.currentProxyInfo);
                        SharedConfig.currentProxy = ProxySettingsActivity.this.currentProxyInfo;
                        edit.putBoolean("proxy_enabled", true);
                        z = true;
                    } else {
                        boolean z2 = globalMainSettings.getBoolean("proxy_enabled", false);
                        SharedConfig.saveProxyList();
                        z = z2;
                    }
                    if (ProxySettingsActivity.this.addingNewProxy || SharedConfig.currentProxy == ProxySettingsActivity.this.currentProxyInfo) {
                        edit.putString("proxy_ip", ProxySettingsActivity.this.currentProxyInfo.address);
                        edit.putString("proxy_pass", ProxySettingsActivity.this.currentProxyInfo.password);
                        edit.putString("proxy_user", ProxySettingsActivity.this.currentProxyInfo.username);
                        edit.putInt("proxy_port", ProxySettingsActivity.this.currentProxyInfo.port);
                        edit.putString("proxy_secret", ProxySettingsActivity.this.currentProxyInfo.secret);
                        ConnectionsManager.setProxySettings(z, ProxySettingsActivity.this.currentProxyInfo.address, ProxySettingsActivity.this.currentProxyInfo.port, ProxySettingsActivity.this.currentProxyInfo.username, ProxySettingsActivity.this.currentProxyInfo.password, ProxySettingsActivity.this.currentProxyInfo.secret);
                    }
                    edit.commit();
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.proxySettingsChanged, new Object[0]);
                }
                ProxySettingsActivity.this.lambda$onBackPressed$321();
            }
        });
        ActionBarMenuItem addItemWithWidth = this.actionBar.createMenu().addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f));
        this.doneItem = addItemWithWidth;
        addItemWithWidth.setContentDescription(LocaleController.getString(R.string.Done));
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        ScrollView scrollView = new ScrollView(context);
        this.scrollView = scrollView;
        scrollView.setFillViewport(true);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor(Theme.key_actionBarDefault));
        frameLayout.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.linearLayout2 = linearLayout;
        linearLayout.setOrientation(1);
        this.scrollView.addView(this.linearLayout2, new FrameLayout.LayoutParams(-1, -2));
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ProxySettingsActivity.this.lambda$createView$0(view);
            }
        };
        int i = 0;
        while (i < 2) {
            this.typeCell[i] = new RadioCell(context);
            this.typeCell[i].setBackground(Theme.getSelectorDrawable(true));
            this.typeCell[i].setTag(Integer.valueOf(i));
            RadioCell[] radioCellArr = this.typeCell;
            if (i == 0) {
                radioCellArr[i].setText(LocaleController.getString(R.string.UseProxySocks5), i == this.currentType, true);
            } else {
                radioCellArr[i].setText(LocaleController.getString(R.string.UseProxyTelegram), i == this.currentType, false);
            }
            this.linearLayout2.addView(this.typeCell[i], LayoutHelper.createLinear(-1, 50));
            this.typeCell[i].setOnClickListener(onClickListener);
            i++;
        }
        this.sectionCell[0] = new ShadowSectionCell(context);
        this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
        LinearLayout linearLayout2 = new LinearLayout(context);
        this.inputFieldsContainer = linearLayout2;
        linearLayout2.setOrientation(1);
        this.inputFieldsContainer.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        if (Build.VERSION.SDK_INT >= 21) {
            this.inputFieldsContainer.setElevation(AndroidUtilities.dp(1.0f));
            this.inputFieldsContainer.setOutlineProvider(null);
        }
        this.linearLayout2.addView(this.inputFieldsContainer, LayoutHelper.createLinear(-1, -2));
        this.inputFields = new EditTextBoldCursor[5];
        int i2 = 0;
        for (int i3 = 5; i2 < i3; i3 = 5) {
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.inputFieldsContainer.addView(frameLayout2, LayoutHelper.createLinear(-1, 64));
            this.inputFields[i2] = new EditTextBoldCursor(context);
            this.inputFields[i2].setTag(Integer.valueOf(i2));
            this.inputFields[i2].setTextSize(1, 16.0f);
            this.inputFields[i2].setHintColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
            EditTextBoldCursor editTextBoldCursor3 = this.inputFields[i2];
            int i4 = Theme.key_windowBackgroundWhiteBlackText;
            editTextBoldCursor3.setTextColor(Theme.getColor(i4));
            this.inputFields[i2].setBackground(null);
            this.inputFields[i2].setCursorColor(Theme.getColor(i4));
            this.inputFields[i2].setCursorSize(AndroidUtilities.dp(20.0f));
            this.inputFields[i2].setCursorWidth(1.5f);
            this.inputFields[i2].setSingleLine(true);
            this.inputFields[i2].setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            this.inputFields[i2].setHeaderHintColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
            this.inputFields[i2].setTransformHintToHeader(true);
            this.inputFields[i2].setLineColors(Theme.getColor(Theme.key_windowBackgroundWhiteInputField), Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated), Theme.getColor(Theme.key_text_RedRegular));
            if (i2 == 0) {
                this.inputFields[i2].setInputType(524305);
                editTextBoldCursor = this.inputFields[i2];
                textWatcher = new TextWatcher() { // from class: org.telegram.ui.ProxySettingsActivity.2
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        ProxySettingsActivity.this.checkShareDone(true);
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                    }
                };
            } else {
                EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                if (i2 == 1) {
                    editTextBoldCursorArr[i2].setInputType(2);
                    editTextBoldCursor = this.inputFields[i2];
                    textWatcher = new TextWatcher() { // from class: org.telegram.ui.ProxySettingsActivity.3
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            if (ProxySettingsActivity.this.ignoreOnTextChange) {
                                return;
                            }
                            EditTextBoldCursor editTextBoldCursor4 = ProxySettingsActivity.this.inputFields[1];
                            int selectionStart = editTextBoldCursor4.getSelectionStart();
                            String obj = editTextBoldCursor4.getText().toString();
                            StringBuilder sb = new StringBuilder(obj.length());
                            int i5 = 0;
                            while (i5 < obj.length()) {
                                int i6 = i5 + 1;
                                String substring = obj.substring(i5, i6);
                                if ("0123456789".contains(substring)) {
                                    sb.append(substring);
                                }
                                i5 = i6;
                            }
                            ProxySettingsActivity.this.ignoreOnTextChange = true;
                            int intValue = Utilities.parseInt((CharSequence) sb.toString()).intValue();
                            if (intValue < 0 || intValue > 65535 || !obj.equals(sb.toString())) {
                                editTextBoldCursor4.setText(intValue < 0 ? "0" : intValue > 65535 ? "65535" : sb.toString());
                            } else if (selectionStart >= 0) {
                                editTextBoldCursor4.setSelection(Math.min(selectionStart, editTextBoldCursor4.length()));
                            }
                            ProxySettingsActivity.this.ignoreOnTextChange = false;
                            ProxySettingsActivity.this.checkShareDone(true);
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                        }
                    };
                } else {
                    if (i2 == 3) {
                        editTextBoldCursorArr[i2].setInputType(NotificationCenter.walletPendingTransactionsChanged);
                        this.inputFields[i2].setTypeface(Typeface.DEFAULT);
                        this.inputFields[i2].setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        editTextBoldCursorArr[i2].setInputType(524289);
                    }
                    this.inputFields[i2].setImeOptions(268435461);
                    if (i2 != 0) {
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.UseProxyAddress));
                        editTextBoldCursor2 = this.inputFields[i2];
                        str = this.currentProxyInfo.address;
                    } else if (i2 == 1) {
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.UseProxyPort));
                        editTextBoldCursor2 = this.inputFields[i2];
                        str = "" + this.currentProxyInfo.port;
                    } else if (i2 == 2) {
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.UseProxyUsername));
                        editTextBoldCursor2 = this.inputFields[i2];
                        str = this.currentProxyInfo.username;
                    } else if (i2 != 3) {
                        if (i2 == 4) {
                            this.inputFields[i2].setHintText(LocaleController.getString(R.string.UseProxySecret));
                            editTextBoldCursor2 = this.inputFields[i2];
                            str = this.currentProxyInfo.secret;
                        }
                        EditTextBoldCursor editTextBoldCursor4 = this.inputFields[i2];
                        editTextBoldCursor4.setSelection(editTextBoldCursor4.length());
                        this.inputFields[i2].setPadding(0, 0, 0, 0);
                        frameLayout2.addView(this.inputFields[i2], LayoutHelper.createFrame(-1, -1.0f, 51, 17.0f, i2 == 0 ? 12.0f : 0.0f, 17.0f, 0.0f));
                        this.inputFields[i2].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda4
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView, int i5, KeyEvent keyEvent) {
                                boolean lambda$createView$1;
                                lambda$createView$1 = ProxySettingsActivity.this.lambda$createView$1(textView, i5, keyEvent);
                                return lambda$createView$1;
                            }
                        });
                        i2++;
                    } else {
                        this.inputFields[i2].setHintText(LocaleController.getString(R.string.UseProxyPassword));
                        editTextBoldCursor2 = this.inputFields[i2];
                        str = this.currentProxyInfo.password;
                    }
                    editTextBoldCursor2.setText(str);
                    EditTextBoldCursor editTextBoldCursor42 = this.inputFields[i2];
                    editTextBoldCursor42.setSelection(editTextBoldCursor42.length());
                    this.inputFields[i2].setPadding(0, 0, 0, 0);
                    frameLayout2.addView(this.inputFields[i2], LayoutHelper.createFrame(-1, -1.0f, 51, 17.0f, i2 == 0 ? 12.0f : 0.0f, 17.0f, 0.0f));
                    this.inputFields[i2].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda4
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView, int i5, KeyEvent keyEvent) {
                            boolean lambda$createView$1;
                            lambda$createView$1 = ProxySettingsActivity.this.lambda$createView$1(textView, i5, keyEvent);
                            return lambda$createView$1;
                        }
                    });
                    i2++;
                }
            }
            editTextBoldCursor.addTextChangedListener(textWatcher);
            this.inputFields[i2].setImeOptions(268435461);
            if (i2 != 0) {
            }
            editTextBoldCursor2.setText(str);
            EditTextBoldCursor editTextBoldCursor422 = this.inputFields[i2];
            editTextBoldCursor422.setSelection(editTextBoldCursor422.length());
            this.inputFields[i2].setPadding(0, 0, 0, 0);
            frameLayout2.addView(this.inputFields[i2], LayoutHelper.createFrame(-1, -1.0f, 51, 17.0f, i2 == 0 ? 12.0f : 0.0f, 17.0f, 0.0f));
            this.inputFields[i2].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda4
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView, int i5, KeyEvent keyEvent) {
                    boolean lambda$createView$1;
                    lambda$createView$1 = ProxySettingsActivity.this.lambda$createView$1(textView, i5, keyEvent);
                    return lambda$createView$1;
                }
            });
            i2++;
        }
        for (int i5 = 0; i5 < 2; i5++) {
            this.bottomCells[i5] = new TextInfoPrivacyCell(context);
            this.bottomCells[i5].setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            if (i5 == 0) {
                this.bottomCells[i5].setText(LocaleController.getString(R.string.UseProxyInfo));
            } else {
                this.bottomCells[i5].setText(LocaleController.getString(R.string.UseProxyTelegramInfo) + "\n\n" + LocaleController.getString(R.string.UseProxyTelegramInfo2));
                this.bottomCells[i5].setVisibility(8);
            }
            this.linearLayout2.addView(this.bottomCells[i5], LayoutHelper.createLinear(-1, -2));
        }
        TextSettingsCell textSettingsCell = new TextSettingsCell(this.fragmentView.getContext());
        this.pasteCell = textSettingsCell;
        textSettingsCell.setBackground(Theme.getSelectorDrawable(true));
        this.pasteCell.setText(LocaleController.getString(R.string.PasteFromClipboard), false);
        TextSettingsCell textSettingsCell2 = this.pasteCell;
        int i6 = Theme.key_windowBackgroundWhiteBlueText4;
        textSettingsCell2.setTextColor(Theme.getColor(i6));
        this.pasteCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ProxySettingsActivity.this.lambda$createView$3(view);
            }
        });
        this.linearLayout2.addView(this.pasteCell, 0, LayoutHelper.createLinear(-1, -2));
        this.pasteCell.setVisibility(8);
        this.sectionCell[2] = new ShadowSectionCell(this.fragmentView.getContext());
        ShadowSectionCell shadowSectionCell = this.sectionCell[2];
        Context context2 = this.fragmentView.getContext();
        int i7 = R.drawable.greydivider_bottom;
        int i8 = Theme.key_windowBackgroundGrayShadow;
        shadowSectionCell.setBackground(Theme.getThemedDrawableByKey(context2, i7, i8));
        this.linearLayout2.addView(this.sectionCell[2], 1, LayoutHelper.createLinear(-1, -2));
        this.sectionCell[2].setVisibility(8);
        TextSettingsCell textSettingsCell3 = new TextSettingsCell(context);
        this.shareCell = textSettingsCell3;
        textSettingsCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.shareCell.setText(LocaleController.getString(R.string.ShareFile), false);
        this.shareCell.setTextColor(Theme.getColor(i6));
        this.linearLayout2.addView(this.shareCell, LayoutHelper.createLinear(-1, -2));
        this.shareCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ProxySettingsActivity.this.lambda$createView$4(context, view);
            }
        });
        this.sectionCell[1] = new ShadowSectionCell(context);
        this.sectionCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i7, i8));
        this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(-1, -2));
        this.clipboardManager = (ClipboardManager) context.getSystemService("clipboard");
        this.shareDoneEnabled = true;
        this.shareDoneProgress = 1.0f;
        checkShareDone(false);
        this.currentType = -1;
        setProxyType(!TextUtils.isEmpty(this.currentProxyInfo.secret) ? 1 : 0, false);
        this.pasteType = -1;
        this.pasteString = null;
        updatePasteCell();
        return this.fragmentView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ProxySettingsActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ProxySettingsActivity.this.lambda$getThemeDescriptions$6();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));
        LinearLayout linearLayout = this.inputFieldsContainer;
        int i3 = ThemeDescription.FLAG_BACKGROUND;
        int i4 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(linearLayout, i3, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.shareCell, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, i4));
        TextSettingsCell textSettingsCell = this.shareCell;
        int i5 = ThemeDescription.FLAG_SELECTORWHITE;
        int i6 = Theme.key_listSelector;
        arrayList.add(new ThemeDescription(textSettingsCell, i5, null, null, null, null, i6));
        int i7 = Theme.key_windowBackgroundWhiteBlueText4;
        arrayList.add(new ThemeDescription((View) null, 0, (Class[]) null, (String[]) null, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, i7));
        arrayList.add(new ThemeDescription((View) null, 0, (Class[]) null, (String[]) null, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.pasteCell, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.pasteCell, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.pasteCell, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i7));
        for (int i8 = 0; i8 < this.typeCell.length; i8++) {
            arrayList.add(new ThemeDescription(this.typeCell[i8], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(this.typeCell[i8], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
            arrayList.add(new ThemeDescription(this.typeCell[i8], 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(this.typeCell[i8], ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackground));
            arrayList.add(new ThemeDescription(this.typeCell[i8], ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackgroundChecked));
        }
        if (this.inputFields != null) {
            for (int i9 = 0; i9 < this.inputFields.length; i9++) {
                EditTextBoldCursor editTextBoldCursor = this.inputFields[i9];
                int i10 = ThemeDescription.FLAG_TEXTCOLOR;
                int i11 = Theme.key_windowBackgroundWhiteBlackText;
                arrayList.add(new ThemeDescription(editTextBoldCursor, i10, null, null, null, null, i11));
                arrayList.add(new ThemeDescription(this.inputFields[i9], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
                arrayList.add(new ThemeDescription(this.inputFields[i9], ThemeDescription.FLAG_HINTTEXTCOLOR | ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader));
                arrayList.add(new ThemeDescription(this.inputFields[i9], ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, i11));
                arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteInputField));
                arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteInputFieldActivated));
                arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_text_RedRegular));
            }
        } else {
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        }
        arrayList.add(new ThemeDescription(this.headerCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.headerCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        int i12 = 0;
        while (true) {
            ShadowSectionCell[] shadowSectionCellArr = this.sectionCell;
            if (i12 >= shadowSectionCellArr.length) {
                break;
            }
            if (shadowSectionCellArr[i12] != null) {
                arrayList.add(new ThemeDescription(this.sectionCell[i12], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
            }
            i12++;
        }
        for (int i13 = 0; i13 < this.bottomCells.length; i13++) {
            arrayList.add(new ThemeDescription(this.bottomCells[i13], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
            arrayList.add(new ThemeDescription(this.bottomCells[i13], 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
            arrayList.add(new ThemeDescription(this.bottomCells[i13], ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteLinkText));
        }
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        this.clipboardManager.removePrimaryClipChangedListener(this.clipChangedListener);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        this.clipboardManager.addPrimaryClipChangedListener(this.clipChangedListener);
        updatePasteCell();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z && !z2 && this.addingNewProxy) {
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
        }
    }
}
