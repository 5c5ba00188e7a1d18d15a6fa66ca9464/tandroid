package org.telegram.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import j$.util.Comparator$-CC;
import j$.util.function.Function;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedPhoneNumberEditText;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.OutlineEditText;
import org.telegram.ui.Components.OutlineTextContainerView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.CountrySelectActivity;
import org.telegram.ui.NewContactBottomSheet;

/* loaded from: classes4.dex */
public class NewContactBottomSheet extends BottomSheet implements AdapterView.OnItemSelectedListener {
    int classGuid;
    private View codeDividerView;
    private AnimatedPhoneNumberEditText codeField;
    private HashMap codesMap;
    private LinearLayout contentLayout;
    private ArrayList countriesArray;
    private String countryCodeForHint;
    private TextView countryFlag;
    private TextView doneButton;
    private FrameLayout doneButtonContainer;
    private boolean donePressed;
    private ContextProgressView editDoneItemProgress;
    private OutlineEditText firstNameField;
    private boolean ignoreOnPhoneChange;
    private boolean ignoreOnTextChange;
    private boolean ignoreSelection;
    private String initialFirstName;
    private String initialLastName;
    private String initialPhoneNumber;
    private boolean initialPhoneNumberWithCountryCode;
    private OutlineEditText lastNameField;
    BaseFragment parentFragment;
    private AnimatedPhoneNumberEditText phoneField;
    private HashMap phoneFormatMap;
    private OutlineTextContainerView phoneOutlineView;
    private TextView plusTextView;
    private RadialProgressView progressView;
    private int wasCountryHintIndex;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 1 extends TextView {
        final NotificationCenter.NotificationCenterDelegate delegate;

        1(Context context) {
            super(context);
            this.delegate = new NotificationCenter.NotificationCenterDelegate() { // from class: org.telegram.ui.NewContactBottomSheet$1$$ExternalSyntheticLambda0
                @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
                public final void didReceivedNotification(int i, int i2, Object[] objArr) {
                    NewContactBottomSheet.1.this.lambda$$0(i, i2, objArr);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$0(int i, int i2, Object[] objArr) {
            invalidate();
        }

        @Override // android.widget.TextView, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            NotificationCenter.getGlobalInstance().addObserver(this.delegate, NotificationCenter.emojiLoaded);
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            NotificationCenter.getGlobalInstance().removeObserver(this.delegate, NotificationCenter.emojiLoaded);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 2 implements CountrySelectActivity.CountrySelectActivityDelegate {
        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectCountry$0() {
            AndroidUtilities.showKeyboard(NewContactBottomSheet.this.phoneField);
        }

        @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
        public void didSelectCountry(CountrySelectActivity.Country country) {
            NewContactBottomSheet.this.selectCountry(country);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NewContactBottomSheet$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NewContactBottomSheet.2.this.lambda$didSelectCountry$0();
                }
            }, 300L);
            NewContactBottomSheet.this.phoneField.requestFocus();
            NewContactBottomSheet.this.phoneField.setSelection(NewContactBottomSheet.this.phoneField.length());
        }
    }

    public NewContactBottomSheet(BaseFragment baseFragment, Context context) {
        super(context, true);
        this.countriesArray = new ArrayList();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        fixNavigationBar();
        this.waitingKeyboard = true;
        this.smoothKeyboardAnimationEnabled = true;
        this.classGuid = ConnectionsManager.generateClassGuid();
        this.parentFragment = baseFragment;
        setCustomView(createView(getContext()));
        setTitle(LocaleController.getString(R.string.NewContactTitle), true);
    }

    private void doOnDone() {
        BaseFragment baseFragment;
        if (this.donePressed || (baseFragment = this.parentFragment) == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (this.firstNameField.getEditText().length() == 0) {
            Vibrator vibrator = (Vibrator) this.parentFragment.getParentActivity().getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
            }
            AndroidUtilities.shakeView(this.firstNameField);
            return;
        }
        if (this.codeField.length() == 0) {
            Vibrator vibrator2 = (Vibrator) this.parentFragment.getParentActivity().getSystemService("vibrator");
            if (vibrator2 != null) {
                vibrator2.vibrate(200L);
            }
            AndroidUtilities.shakeView(this.codeField);
            return;
        }
        if (this.phoneField.length() == 0) {
            Vibrator vibrator3 = (Vibrator) this.parentFragment.getParentActivity().getSystemService("vibrator");
            if (vibrator3 != null) {
                vibrator3.vibrate(200L);
            }
            AndroidUtilities.shakeView(this.phoneField);
            return;
        }
        this.donePressed = true;
        showEditDoneProgress(true, true);
        final TLRPC.TL_contacts_importContacts tL_contacts_importContacts = new TLRPC.TL_contacts_importContacts();
        final TLRPC.TL_inputPhoneContact tL_inputPhoneContact = new TLRPC.TL_inputPhoneContact();
        tL_inputPhoneContact.first_name = this.firstNameField.getEditText().getText().toString();
        tL_inputPhoneContact.last_name = this.lastNameField.getEditText().getText().toString();
        tL_inputPhoneContact.phone = "+" + this.codeField.getText().toString() + this.phoneField.getText().toString();
        tL_contacts_importContacts.contacts.add(tL_inputPhoneContact);
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_contacts_importContacts, new RequestDelegate() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda10
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                NewContactBottomSheet.this.lambda$doOnDone$9(tL_inputPhoneContact, tL_contacts_importContacts, tLObject, tL_error);
            }
        }, 2), this.classGuid);
    }

    public static String getPhoneNumber(Context context, TLRPC.User user, String str, boolean z) {
        StringBuilder sb;
        HashMap hashMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split(";");
                hashMap.put(split[0], split[2]);
            }
            bufferedReader.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (str.startsWith("+")) {
            return str;
        }
        if (!z && user != null && !TextUtils.isEmpty(user.phone)) {
            String str2 = user.phone;
            for (int i = 4; i >= 1; i--) {
                String substring = str2.substring(0, i);
                if (((String) hashMap.get(substring)) != null) {
                    sb = new StringBuilder();
                    sb.append("+");
                    sb.append(substring);
                }
            }
            return str;
        }
        sb = new StringBuilder();
        sb.append("+");
        sb.append(str);
        return sb.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x008d, code lost:
    
        if (r7 == (-1)) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void invalidateCountryHint() {
        int i;
        String str = this.countryCodeForHint;
        String replace = this.phoneField.getText() != null ? this.phoneField.getText().toString().replace(" ", "") : "";
        if (this.phoneFormatMap.get(str) == null || ((List) this.phoneFormatMap.get(str)).isEmpty()) {
            if (this.wasCountryHintIndex != -1) {
                int selectionStart = this.phoneField.getSelectionStart();
                int selectionEnd = this.phoneField.getSelectionEnd();
                this.phoneField.setHintText((String) null);
                this.phoneField.setSelection(selectionStart, selectionEnd);
                this.wasCountryHintIndex = -1;
                return;
            }
            return;
        }
        List list = (List) this.phoneFormatMap.get(str);
        int i2 = 0;
        if (!replace.isEmpty()) {
            i = 0;
            while (i < list.size()) {
                if (replace.startsWith(((String) list.get(i)).replace(" ", "").replace("X", "").replace("0", ""))) {
                    break;
                } else {
                    i++;
                }
            }
        }
        i = -1;
        if (i == -1) {
            for (int i3 = 0; i3 < list.size(); i3++) {
                String str2 = (String) list.get(i3);
                if (str2.startsWith("X") || str2.startsWith("0")) {
                    i = i3;
                    break;
                }
            }
        }
        i2 = i;
        if (this.wasCountryHintIndex != i2) {
            String str3 = (String) ((List) this.phoneFormatMap.get(str)).get(i2);
            int selectionStart2 = this.phoneField.getSelectionStart();
            int selectionEnd2 = this.phoneField.getSelectionEnd();
            this.phoneField.setHintText(str3 != null ? str3.replace('X', '0') : null);
            this.phoneField.setSelection(selectionStart2, selectionEnd2);
            this.wasCountryHintIndex = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$0(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$1(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            return false;
        }
        this.lastNameField.requestFocus();
        this.lastNameField.getEditText().setSelection(this.lastNameField.getEditText().length());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$2(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            return false;
        }
        this.codeField.requestFocus();
        AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.codeField;
        animatedPhoneNumberEditText.setSelection(animatedPhoneNumberEditText.length());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        CountrySelectActivity countrySelectActivity = new CountrySelectActivity(true);
        countrySelectActivity.setCountrySelectActivityDelegate(new 2());
        this.parentFragment.showAsSheet(countrySelectActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$4(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            return false;
        }
        this.phoneField.requestFocus();
        AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.phoneField;
        animatedPhoneNumberEditText.setSelection(animatedPhoneNumberEditText.length());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$5(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            return false;
        }
        this.doneButtonContainer.callOnClick();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(View view) {
        doOnDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$11() {
        AndroidUtilities.hideKeyboard(this.contentLayout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doOnDone$8(TLRPC.TL_contacts_importedContacts tL_contacts_importedContacts, TLRPC.TL_inputPhoneContact tL_inputPhoneContact, TLRPC.TL_error tL_error, TLRPC.TL_contacts_importContacts tL_contacts_importContacts) {
        this.donePressed = false;
        if (tL_contacts_importedContacts == null) {
            showEditDoneProgress(false, true);
            AlertsCreator.processError(this.currentAccount, tL_error, this.parentFragment, tL_contacts_importContacts, new Object[0]);
        } else if (!tL_contacts_importedContacts.users.isEmpty()) {
            MessagesController.getInstance(this.currentAccount).putUsers(tL_contacts_importedContacts.users, false);
            MessagesController.getInstance(this.currentAccount).openChatOrProfileWith(tL_contacts_importedContacts.users.get(0), null, this.parentFragment, 1, false);
            dismiss();
        } else {
            if (this.parentFragment.getParentActivity() == null) {
                return;
            }
            showEditDoneProgress(false, true);
            AlertsCreator.createContactInviteDialog(this.parentFragment, tL_inputPhoneContact.first_name, tL_inputPhoneContact.last_name, tL_inputPhoneContact.phone);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doOnDone$9(final TLRPC.TL_inputPhoneContact tL_inputPhoneContact, final TLRPC.TL_contacts_importContacts tL_contacts_importContacts, TLObject tLObject, final TLRPC.TL_error tL_error) {
        final TLRPC.TL_contacts_importedContacts tL_contacts_importedContacts = (TLRPC.TL_contacts_importedContacts) tLObject;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                NewContactBottomSheet.this.lambda$doOnDone$8(tL_contacts_importedContacts, tL_inputPhoneContact, tL_error, tL_contacts_importContacts);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$show$10() {
        AndroidUtilities.showKeyboard(this.firstNameField.getEditText());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCountryButtonText(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            ViewPropertyAnimator animate = this.countryFlag.animate();
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            animate.setInterpolator(cubicBezierInterpolator).translationY(AndroidUtilities.dp(30.0f)).setDuration(150L);
            this.plusTextView.animate().setInterpolator(cubicBezierInterpolator).translationX(-AndroidUtilities.dp(30.0f)).setDuration(150L);
            this.codeField.animate().setInterpolator(cubicBezierInterpolator).translationX(-AndroidUtilities.dp(30.0f)).setDuration(150L);
            return;
        }
        this.countryFlag.animate().setInterpolator(AndroidUtilities.overshootInterpolator).translationY(0.0f).setDuration(350L).start();
        ViewPropertyAnimator animate2 = this.plusTextView.animate();
        CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.DEFAULT;
        animate2.setInterpolator(cubicBezierInterpolator2).translationX(0.0f).setDuration(150L);
        this.codeField.animate().setInterpolator(cubicBezierInterpolator2).translationX(0.0f).setDuration(150L);
        this.countryFlag.setText(charSequence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCountryHint(String str, CountrySelectActivity.Country country) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        String languageFlag = LocaleController.getLanguageFlag(country.shortname);
        if (languageFlag != null) {
            spannableStringBuilder.append((CharSequence) languageFlag);
        }
        setCountryButtonText(Emoji.replaceEmoji((CharSequence) spannableStringBuilder, this.countryFlag.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
        this.countryCodeForHint = str;
        this.wasCountryHintIndex = -1;
        invalidateCountryHint();
    }

    private void showEditDoneProgress(boolean z, boolean z2) {
        AndroidUtilities.updateViewVisibilityAnimated(this.doneButton, !z, 0.5f, z2);
        AndroidUtilities.updateViewVisibilityAnimated(this.progressView, z, 0.5f, z2);
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x03f1  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0410  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x040d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x041f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        String str;
        String str2;
        int i;
        CountrySelectActivity.Country country;
        TelephonyManager telephonyManager;
        AnimatedPhoneNumberEditText animatedPhoneNumberEditText;
        String str3;
        ContextProgressView contextProgressView = new ContextProgressView(context, 1);
        this.editDoneItemProgress = contextProgressView;
        contextProgressView.setVisibility(4);
        ScrollView scrollView = new ScrollView(context);
        LinearLayout linearLayout = new LinearLayout(context);
        this.contentLayout = linearLayout;
        linearLayout.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.contentLayout.setOrientation(1);
        scrollView.addView(this.contentLayout, LayoutHelper.createScroll(-1, -2, 51));
        this.contentLayout.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda2
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createView$0;
                lambda$createView$0 = NewContactBottomSheet.lambda$createView$0(view, motionEvent);
                return lambda$createView$0;
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        this.contentLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 0.0f));
        OutlineEditText outlineEditText = new OutlineEditText(context);
        this.firstNameField = outlineEditText;
        outlineEditText.getEditText().setInputType(49152);
        this.firstNameField.getEditText().setImeOptions(5);
        this.firstNameField.setHint(LocaleController.getString(R.string.FirstName));
        if (this.initialFirstName != null) {
            this.firstNameField.getEditText().setText(this.initialFirstName);
            this.initialFirstName = null;
        }
        frameLayout.addView(this.firstNameField, LayoutHelper.createFrame(-1, 58.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        this.firstNameField.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda3
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i2, KeyEvent keyEvent) {
                boolean lambda$createView$1;
                lambda$createView$1 = NewContactBottomSheet.this.lambda$createView$1(textView, i2, keyEvent);
                return lambda$createView$1;
            }
        });
        OutlineEditText outlineEditText2 = new OutlineEditText(context);
        this.lastNameField = outlineEditText2;
        outlineEditText2.setBackground(null);
        this.lastNameField.getEditText().setInputType(49152);
        this.lastNameField.getEditText().setImeOptions(5);
        this.lastNameField.setHint(LocaleController.getString(R.string.LastName));
        if (this.initialLastName != null) {
            this.lastNameField.getEditText().setText(this.initialLastName);
            this.initialLastName = null;
        }
        frameLayout.addView(this.lastNameField, LayoutHelper.createFrame(-1, 58.0f, 51, 0.0f, 68.0f, 0.0f, 0.0f));
        this.lastNameField.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda4
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i2, KeyEvent keyEvent) {
                boolean lambda$createView$2;
                lambda$createView$2 = NewContactBottomSheet.this.lambda$createView$2(textView, i2, keyEvent);
                return lambda$createView$2;
            }
        });
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
        this.phoneOutlineView = outlineTextContainerView;
        outlineTextContainerView.addView(linearLayout2, LayoutHelper.createFrame(-1, -2.0f, 16, 4.0f, 8.0f, 16.0f, 8.0f));
        OutlineTextContainerView outlineTextContainerView2 = this.phoneOutlineView;
        int i2 = R.string.PhoneNumber;
        outlineTextContainerView2.setText(LocaleController.getString(i2));
        this.contentLayout.addView(this.phoneOutlineView, LayoutHelper.createLinear(-1, 58, 0.0f, 12.0f, 0.0f, 8.0f));
        FrameLayout frameLayout2 = new FrameLayout(context);
        1 r12 = new 1(context);
        this.countryFlag = r12;
        r12.setTextSize(1, 16.0f);
        this.countryFlag.setFocusable(false);
        this.countryFlag.setGravity(17);
        frameLayout2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NewContactBottomSheet.this.lambda$createView$3(view);
            }
        });
        frameLayout2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, Theme.getColor(Theme.key_listSelector)));
        frameLayout2.addView(this.countryFlag, LayoutHelper.createFrame(-1, -2, 16));
        linearLayout2.addView(frameLayout2, LayoutHelper.createLinear(42, -1));
        TextView textView = new TextView(context);
        this.plusTextView = textView;
        textView.setText("+");
        this.plusTextView.setTextSize(1, 16.0f);
        this.plusTextView.setFocusable(false);
        linearLayout2.addView(this.plusTextView, LayoutHelper.createLinear(-2, -2));
        AnimatedPhoneNumberEditText animatedPhoneNumberEditText2 = new AnimatedPhoneNumberEditText(context) { // from class: org.telegram.ui.NewContactBottomSheet.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public void onFocusChanged(boolean z, int i3, Rect rect) {
                super.onFocusChanged(z, i3, rect);
                NewContactBottomSheet.this.phoneOutlineView.animateSelection((z || NewContactBottomSheet.this.phoneField.isFocused()) ? 1.0f : 0.0f);
            }
        };
        this.codeField = animatedPhoneNumberEditText2;
        int i3 = Theme.key_windowBackgroundWhiteBlackText;
        animatedPhoneNumberEditText2.setTextColor(Theme.getColor(i3));
        this.codeField.setInputType(3);
        this.codeField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.codeField.setCursorWidth(1.5f);
        this.codeField.setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
        this.codeField.setTextSize(1, 16.0f);
        this.codeField.setMaxLines(1);
        this.codeField.setGravity(19);
        this.codeField.setImeOptions(268435461);
        this.codeField.setBackground(null);
        this.codeField.setContentDescription(LocaleController.getString(R.string.LoginAccessibilityCountryCode));
        linearLayout2.addView(this.codeField, LayoutHelper.createLinear(55, 36, -9.0f, 0.0f, 0.0f, 0.0f));
        this.codeField.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.NewContactBottomSheet.4
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                String str4;
                boolean z;
                CountrySelectActivity.Country country2;
                CountrySelectActivity.Country country3;
                if (NewContactBottomSheet.this.ignoreOnTextChange) {
                    return;
                }
                NewContactBottomSheet.this.ignoreOnTextChange = true;
                String stripExceptNumbers = PhoneFormat.stripExceptNumbers(NewContactBottomSheet.this.codeField.getText().toString());
                NewContactBottomSheet.this.codeField.setText(stripExceptNumbers);
                if (stripExceptNumbers.length() == 0) {
                    NewContactBottomSheet.this.setCountryButtonText(null);
                    NewContactBottomSheet.this.phoneField.setHintText((String) null);
                } else {
                    int i4 = 4;
                    if (stripExceptNumbers.length() > 4) {
                        while (true) {
                            if (i4 < 1) {
                                str4 = null;
                                z = false;
                                break;
                            }
                            String substring = stripExceptNumbers.substring(0, i4);
                            List list = (List) NewContactBottomSheet.this.codesMap.get(substring);
                            if (list == null) {
                                country3 = null;
                            } else if (list.size() > 1) {
                                String string = MessagesController.getGlobalMainSettings().getString("phone_code_last_matched_" + substring, null);
                                country3 = (CountrySelectActivity.Country) list.get(list.size() - 1);
                                if (string != null) {
                                    Iterator it = NewContactBottomSheet.this.countriesArray.iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            break;
                                        }
                                        CountrySelectActivity.Country country4 = (CountrySelectActivity.Country) it.next();
                                        if (Objects.equals(country4.shortname, string)) {
                                            country3 = country4;
                                            break;
                                        }
                                    }
                                }
                            } else {
                                country3 = (CountrySelectActivity.Country) list.get(0);
                            }
                            if (country3 != null) {
                                String str5 = stripExceptNumbers.substring(i4) + NewContactBottomSheet.this.phoneField.getText().toString();
                                NewContactBottomSheet.this.codeField.setText(substring);
                                z = true;
                                str4 = str5;
                                stripExceptNumbers = substring;
                                break;
                            }
                            i4--;
                        }
                        if (!z) {
                            str4 = stripExceptNumbers.substring(1) + NewContactBottomSheet.this.phoneField.getText().toString();
                            AnimatedPhoneNumberEditText animatedPhoneNumberEditText3 = NewContactBottomSheet.this.codeField;
                            stripExceptNumbers = stripExceptNumbers.substring(0, 1);
                            animatedPhoneNumberEditText3.setText(stripExceptNumbers);
                        }
                    } else {
                        str4 = null;
                        z = false;
                    }
                    Iterator it2 = NewContactBottomSheet.this.countriesArray.iterator();
                    CountrySelectActivity.Country country5 = null;
                    int i5 = 0;
                    while (it2.hasNext()) {
                        CountrySelectActivity.Country country6 = (CountrySelectActivity.Country) it2.next();
                        if (country6.code.startsWith(stripExceptNumbers)) {
                            i5++;
                            if (country6.code.equals(stripExceptNumbers)) {
                                country5 = country6;
                            }
                        }
                    }
                    if (i5 == 1 && country5 != null && str4 == null) {
                        str4 = stripExceptNumbers.substring(country5.code.length()) + NewContactBottomSheet.this.phoneField.getText().toString();
                        AnimatedPhoneNumberEditText animatedPhoneNumberEditText4 = NewContactBottomSheet.this.codeField;
                        String str6 = country5.code;
                        animatedPhoneNumberEditText4.setText(str6);
                        stripExceptNumbers = str6;
                    }
                    List list2 = (List) NewContactBottomSheet.this.codesMap.get(stripExceptNumbers);
                    if (list2 == null) {
                        country2 = null;
                    } else if (list2.size() > 1) {
                        String string2 = MessagesController.getGlobalMainSettings().getString("phone_code_last_matched_" + stripExceptNumbers, null);
                        country2 = (CountrySelectActivity.Country) list2.get(list2.size() - 1);
                        if (string2 != null) {
                            Iterator it3 = NewContactBottomSheet.this.countriesArray.iterator();
                            while (true) {
                                if (!it3.hasNext()) {
                                    break;
                                }
                                CountrySelectActivity.Country country7 = (CountrySelectActivity.Country) it3.next();
                                if (Objects.equals(country7.shortname, string2)) {
                                    country2 = country7;
                                    break;
                                }
                            }
                        }
                    } else {
                        country2 = (CountrySelectActivity.Country) list2.get(0);
                    }
                    if (country2 != null) {
                        NewContactBottomSheet.this.ignoreSelection = true;
                        NewContactBottomSheet.this.setCountryHint(stripExceptNumbers, country2);
                    } else {
                        NewContactBottomSheet.this.setCountryButtonText(null);
                        NewContactBottomSheet.this.phoneField.setHintText((String) null);
                    }
                    if (!z) {
                        NewContactBottomSheet.this.codeField.setSelection(NewContactBottomSheet.this.codeField.getText().length());
                    }
                    if (str4 != null && str4.length() != 0) {
                        NewContactBottomSheet.this.phoneField.requestFocus();
                        NewContactBottomSheet.this.phoneField.setText(str4);
                        NewContactBottomSheet.this.phoneField.setSelection(NewContactBottomSheet.this.phoneField.length());
                    }
                }
                NewContactBottomSheet.this.ignoreOnTextChange = false;
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
            }
        });
        this.codeField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda6
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView2, int i4, KeyEvent keyEvent) {
                boolean lambda$createView$4;
                lambda$createView$4 = NewContactBottomSheet.this.lambda$createView$4(textView2, i4, keyEvent);
                return lambda$createView$4;
            }
        });
        this.codeDividerView = new View(context);
        LinearLayout.LayoutParams createLinear = LayoutHelper.createLinear(0, -1, 4.0f, 8.0f, 12.0f, 8.0f);
        createLinear.width = Math.max(2, AndroidUtilities.dp(0.5f));
        linearLayout2.addView(this.codeDividerView, createLinear);
        AnimatedPhoneNumberEditText animatedPhoneNumberEditText3 = new AnimatedPhoneNumberEditText(context) { // from class: org.telegram.ui.NewContactBottomSheet.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public void onFocusChanged(boolean z, int i4, Rect rect) {
                super.onFocusChanged(z, i4, rect);
                NewContactBottomSheet.this.phoneOutlineView.animateSelection((z || NewContactBottomSheet.this.codeField.isFocused()) ? 1.0f : 0.0f);
            }

            @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
            public boolean onKeyDown(int i4, KeyEvent keyEvent) {
                if (i4 == 67 && NewContactBottomSheet.this.phoneField.length() == 0) {
                    NewContactBottomSheet.this.codeField.requestFocus();
                    NewContactBottomSheet.this.codeField.setSelection(NewContactBottomSheet.this.codeField.length());
                    NewContactBottomSheet.this.codeField.dispatchKeyEvent(keyEvent);
                }
                return super.onKeyDown(i4, keyEvent);
            }
        };
        this.phoneField = animatedPhoneNumberEditText3;
        animatedPhoneNumberEditText3.setTextColor(Theme.getColor(i3));
        this.phoneField.setInputType(3);
        this.phoneField.setPadding(0, 0, 0, 0);
        this.phoneField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.phoneField.setCursorWidth(1.5f);
        this.phoneField.setTextSize(1, 16.0f);
        this.phoneField.setMaxLines(1);
        this.phoneField.setGravity(19);
        this.phoneField.setImeOptions(268435461);
        this.phoneField.setBackground(null);
        this.phoneField.setContentDescription(LocaleController.getString(i2));
        linearLayout2.addView(this.phoneField, LayoutHelper.createFrame(-1, 36.0f));
        this.phoneField.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.NewContactBottomSheet.6
            private int actionPosition;
            private int characterAction = -1;

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                int i4;
                int i5;
                if (NewContactBottomSheet.this.ignoreOnPhoneChange) {
                    return;
                }
                int selectionStart = NewContactBottomSheet.this.phoneField.getSelectionStart();
                String obj = NewContactBottomSheet.this.phoneField.getText().toString();
                if (this.characterAction == 3) {
                    obj = obj.substring(0, this.actionPosition) + obj.substring(this.actionPosition + 1);
                    selectionStart--;
                }
                StringBuilder sb = new StringBuilder(obj.length());
                int i6 = 0;
                while (i6 < obj.length()) {
                    int i7 = i6 + 1;
                    String substring = obj.substring(i6, i7);
                    if ("0123456789".contains(substring)) {
                        sb.append(substring);
                    }
                    i6 = i7;
                }
                NewContactBottomSheet.this.ignoreOnPhoneChange = true;
                String hintText = NewContactBottomSheet.this.phoneField.getHintText();
                if (hintText != null) {
                    int i8 = 0;
                    while (true) {
                        if (i8 >= sb.length()) {
                            break;
                        }
                        if (i8 < hintText.length()) {
                            if (hintText.charAt(i8) == ' ') {
                                sb.insert(i8, ' ');
                                i8++;
                                if (selectionStart == i8 && (i5 = this.characterAction) != 2 && i5 != 3) {
                                    selectionStart++;
                                }
                            }
                            i8++;
                        } else {
                            sb.insert(i8, ' ');
                            if (selectionStart == i8 + 1 && (i4 = this.characterAction) != 2 && i4 != 3) {
                                selectionStart++;
                            }
                        }
                    }
                }
                editable.replace(0, editable.length(), sb);
                if (selectionStart >= 0) {
                    NewContactBottomSheet.this.phoneField.setSelection(Math.min(selectionStart, NewContactBottomSheet.this.phoneField.length()));
                }
                NewContactBottomSheet.this.phoneField.onTextChange();
                NewContactBottomSheet.this.ignoreOnPhoneChange = false;
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                int i7;
                if (i5 == 0 && i6 == 1) {
                    this.characterAction = 1;
                    return;
                }
                if (i5 != 1 || i6 != 0) {
                    i7 = -1;
                } else {
                    if (charSequence.charAt(i4) == ' ' && i4 > 0) {
                        this.characterAction = 3;
                        this.actionPosition = i4 - 1;
                        return;
                    }
                    i7 = 2;
                }
                this.characterAction = i7;
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
            }
        });
        this.phoneField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda7
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView2, int i4, KeyEvent keyEvent) {
                boolean lambda$createView$5;
                lambda$createView$5 = NewContactBottomSheet.this.lambda$createView$5(textView2, i4, keyEvent);
                return lambda$createView$5;
            }
        });
        HashMap hashMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt")));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split(";");
                CountrySelectActivity.Country country2 = new CountrySelectActivity.Country();
                country2.name = split[2];
                country2.code = split[0];
                country2.shortname = split[1];
                this.countriesArray.add(0, country2);
                List list = (List) this.codesMap.get(split[0]);
                if (list == null) {
                    HashMap hashMap2 = this.codesMap;
                    String str4 = split[0];
                    ArrayList arrayList = new ArrayList();
                    hashMap2.put(str4, arrayList);
                    list = arrayList;
                }
                list.add(country2);
                if (split.length > 3) {
                    this.phoneFormatMap.put(split[0], Collections.singletonList(split[3]));
                }
                hashMap.put(split[1], split[2]);
            }
            bufferedReader.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
        Collections.sort(this.countriesArray, Comparator$-CC.comparing(new Function() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda8
            @Override // j$.util.function.Function
            public /* synthetic */ Function andThen(Function function) {
                return Function.-CC.$default$andThen(this, function);
            }

            @Override // j$.util.function.Function
            public final Object apply(Object obj) {
                String str5;
                str5 = ((CountrySelectActivity.Country) obj).name;
                return str5;
            }

            @Override // j$.util.function.Function
            public /* synthetic */ Function compose(Function function) {
                return Function.-CC.$default$compose(this, function);
            }
        }));
        if (TextUtils.isEmpty(this.initialPhoneNumber)) {
            try {
                telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            if (telephonyManager != null) {
                str = telephonyManager.getSimCountryIso().toUpperCase();
                if (str != null && (str2 = (String) hashMap.get(str)) != null) {
                    i = 0;
                    while (true) {
                        if (i < this.countriesArray.size()) {
                            country = null;
                            break;
                        }
                        if (Objects.equals(((CountrySelectActivity.Country) this.countriesArray.get(i)).name, str2)) {
                            country = (CountrySelectActivity.Country) this.countriesArray.get(i);
                            break;
                        }
                        i++;
                    }
                    if (country != null) {
                        this.codeField.setText(country.code);
                    }
                }
                if (this.codeField.length() == 0) {
                    this.phoneField.setHintText((String) null);
                }
            }
            str = null;
            if (str != null) {
                i = 0;
                while (true) {
                    if (i < this.countriesArray.size()) {
                    }
                    i++;
                }
                if (country != null) {
                }
            }
            if (this.codeField.length() == 0) {
            }
        } else {
            TLRPC.User currentUser = this.parentFragment.getUserConfig().getCurrentUser();
            if (this.initialPhoneNumber.startsWith("+")) {
                animatedPhoneNumberEditText = this.codeField;
                str3 = this.initialPhoneNumber.substring(1);
            } else {
                if (this.initialPhoneNumberWithCountryCode || currentUser == null || TextUtils.isEmpty(currentUser.phone)) {
                    animatedPhoneNumberEditText = this.codeField;
                } else {
                    String str5 = currentUser.phone;
                    int i4 = 4;
                    while (true) {
                        if (i4 < 1) {
                            break;
                        }
                        String substring = str5.substring(0, i4);
                        if (((List) this.codesMap.get(substring)) != null) {
                            this.codeField.setText(substring);
                            break;
                        }
                        i4--;
                    }
                    animatedPhoneNumberEditText = this.phoneField;
                }
                str3 = this.initialPhoneNumber;
            }
            animatedPhoneNumberEditText.setText(str3);
            this.initialPhoneNumber = null;
        }
        this.doneButtonContainer = new FrameLayout(getContext());
        TextView textView2 = new TextView(context);
        this.doneButton = textView2;
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        this.doneButton.setGravity(17);
        this.doneButton.setLines(1);
        this.doneButton.setSingleLine(true);
        this.doneButton.setText(LocaleController.getString(R.string.CreateContact));
        TextView textView3 = this.doneButton;
        BaseFragment baseFragment = this.parentFragment;
        int i5 = Theme.key_featuredStickers_buttonText;
        textView3.setTextColor(baseFragment.getThemedColor(i5));
        this.doneButton.setTextSize(1, 15.0f);
        this.doneButton.setTypeface(AndroidUtilities.bold());
        RadialProgressView radialProgressView = new RadialProgressView(context);
        this.progressView = radialProgressView;
        radialProgressView.setSize(AndroidUtilities.dp(20.0f));
        this.progressView.setProgressColor(this.parentFragment.getThemedColor(i5));
        this.doneButtonContainer.addView(this.doneButton, LayoutHelper.createFrame(-1, -1.0f));
        this.doneButtonContainer.addView(this.progressView, LayoutHelper.createFrame(40, 40, 17));
        this.contentLayout.addView(this.doneButtonContainer, LayoutHelper.createLinear(-1, 48, 0, 0, 16, 0, 16));
        AndroidUtilities.updateViewVisibilityAnimated(this.doneButton, true, 1.0f, false);
        AndroidUtilities.updateViewVisibilityAnimated(this.progressView, false, 1.0f, false);
        this.doneButtonContainer.setBackground(Theme.AdaptiveRipple.filledRect(this.parentFragment.getThemedColor(Theme.key_featuredStickers_addButton), 6.0f));
        this.doneButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NewContactBottomSheet.this.lambda$createView$7(view);
            }
        });
        this.plusTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.codeDividerView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhiteInputField));
        return scrollView;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss() {
        super.dismiss();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NewContactBottomSheet.this.lambda$dismiss$11();
            }
        }, 50L);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        OutlineEditText outlineEditText = this.firstNameField;
        int i = ThemeDescription.FLAG_TEXTCOLOR;
        int i2 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(outlineEditText, i, null, null, null, null, i2));
        OutlineEditText outlineEditText2 = this.firstNameField;
        int i3 = ThemeDescription.FLAG_HINTTEXTCOLOR;
        int i4 = Theme.key_windowBackgroundWhiteHintText;
        arrayList.add(new ThemeDescription(outlineEditText2, i3, null, null, null, null, i4));
        OutlineEditText outlineEditText3 = this.firstNameField;
        int i5 = ThemeDescription.FLAG_BACKGROUNDFILTER;
        int i6 = Theme.key_windowBackgroundWhiteInputField;
        arrayList.add(new ThemeDescription(outlineEditText3, i5, null, null, null, null, i6));
        OutlineEditText outlineEditText4 = this.firstNameField;
        int i7 = ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE;
        int i8 = Theme.key_windowBackgroundWhiteInputFieldActivated;
        arrayList.add(new ThemeDescription(outlineEditText4, i7, null, null, null, null, i8));
        arrayList.add(new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i8));
        arrayList.add(new ThemeDescription(this.codeField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.codeField, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i8));
        arrayList.add(new ThemeDescription(this.phoneField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.phoneField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.phoneField, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, i8));
        arrayList.add(new ThemeDescription(this.editDoneItemProgress, 0, null, null, null, null, Theme.key_contextProgressInner2));
        arrayList.add(new ThemeDescription(this.editDoneItemProgress, 0, null, null, null, null, Theme.key_contextProgressOuter2));
        return arrayList;
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
        if (this.ignoreSelection) {
            this.ignoreSelection = false;
            return;
        }
        this.ignoreOnTextChange = true;
        this.codeField.setText(((CountrySelectActivity.Country) this.countriesArray.get(i)).code);
        this.ignoreOnTextChange = false;
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView adapterView) {
    }

    public void selectCountry(CountrySelectActivity.Country country) {
        this.ignoreOnTextChange = true;
        String str = country.code;
        this.codeField.setText(str);
        setCountryHint(str, country);
        this.ignoreOnTextChange = false;
    }

    public void setInitialName(String str, String str2) {
        OutlineEditText outlineEditText = this.firstNameField;
        if (outlineEditText != null) {
            outlineEditText.getEditText().setText(str);
        } else {
            this.initialFirstName = str;
        }
        OutlineEditText outlineEditText2 = this.lastNameField;
        if (outlineEditText2 != null) {
            outlineEditText2.getEditText().setText(str2);
        } else {
            this.initialLastName = str2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0071, code lost:
    
        if (r5.initialPhoneNumber.startsWith("0") != false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00af, code lost:
    
        r5.initialPhoneNumber = r5.initialPhoneNumber.substring(1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00ad, code lost:
    
        if (r5.initialPhoneNumber.startsWith("0") != false) goto L37;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public NewContactBottomSheet setInitialPhoneNumber(String str, boolean z) {
        AnimatedPhoneNumberEditText animatedPhoneNumberEditText;
        String str2;
        String country;
        Object systemService;
        this.initialPhoneNumber = str;
        this.initialPhoneNumberWithCountryCode = z;
        if (!TextUtils.isEmpty(str)) {
            TLRPC.User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
            if (this.initialPhoneNumber.startsWith("+")) {
                animatedPhoneNumberEditText = this.codeField;
                str2 = this.initialPhoneNumber.substring(1);
            } else {
                if (this.initialPhoneNumberWithCountryCode || currentUser == null || TextUtils.isEmpty(currentUser.phone)) {
                    animatedPhoneNumberEditText = this.codeField;
                } else {
                    String str3 = currentUser.phone;
                    int i = 4;
                    while (true) {
                        if (i >= 1) {
                            List list = (List) this.codesMap.get(str3.substring(0, i));
                            if (list == null || list.size() <= 0) {
                                i--;
                            } else {
                                String str4 = ((CountrySelectActivity.Country) list.get(0)).code;
                                this.codeField.setText(str4);
                                if (str4.endsWith("0")) {
                                }
                            }
                        } else if (Build.VERSION.SDK_INT >= 23) {
                            Context context = ApplicationLoader.applicationContext;
                            if (context != null) {
                                systemService = context.getSystemService((Class<Object>) TelephonyManager.class);
                                country = ((TelephonyManager) systemService).getSimCountryIso().toUpperCase(Locale.US);
                            } else {
                                country = Locale.getDefault().getCountry();
                            }
                            this.codeField.setText(country);
                            if (country.endsWith("0")) {
                            }
                        }
                    }
                    animatedPhoneNumberEditText = this.phoneField;
                }
                str2 = this.initialPhoneNumber;
            }
            animatedPhoneNumberEditText.setText(str2);
            this.initialPhoneNumber = null;
        }
        return this;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        super.show();
        this.firstNameField.getEditText().requestFocus();
        this.firstNameField.getEditText().setSelection(this.firstNameField.getEditText().length());
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.NewContactBottomSheet$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                NewContactBottomSheet.this.lambda$show$10();
            }
        }, 50L);
    }
}
