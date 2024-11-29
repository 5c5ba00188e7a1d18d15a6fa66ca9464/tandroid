package org.telegram.ui;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BillingController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EditTextCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.Stories.recorder.KeyboardNotifier;

/* loaded from: classes4.dex */
public class LinkEditActivity extends BaseFragment {
    private TextCheckCell approveCell;
    private TextInfoPrivacyCell approveHintCell;
    private FrameLayout buttonLayout;
    private TextView buttonTextView;
    private Callback callback;
    private final long chatId;
    private TextView createTextView;
    int currentInviteDate;
    private TextInfoPrivacyCell divider;
    private TextInfoPrivacyCell dividerName;
    private TextInfoPrivacyCell dividerUses;
    private boolean finished;
    private boolean ignoreSet;
    TLRPC.TL_chatInviteExported inviteToEdit;
    boolean loading;
    private EditText nameEditText;
    AlertDialog progressDialog;
    private TextSettingsCell revokeLink;
    boolean scrollToEnd;
    boolean scrollToStart;
    private ScrollView scrollView;
    private TextCheckCell subCell;
    private EditTextCell subEditPriceCell;
    private TextInfoPrivacyCell subInfoCell;
    private TextView subPriceView;
    private SlideChooseView timeChooseView;
    private TextView timeEditText;
    private HeaderCell timeHeaderCell;
    private int type;
    private SlideChooseView usesChooseView;
    private EditText usesEditText;
    private HeaderCell usesHeaderCell;
    private int shakeDp = -3;
    private boolean firstLayout = true;
    private ArrayList dispalyedDates = new ArrayList();
    private final int[] defaultDates = {3600, 86400, 604800};
    private ArrayList dispalyedUses = new ArrayList();
    private final int[] defaultUses = {1, 10, 100};

    public interface Callback {
        void onLinkCreated(TLObject tLObject);

        void onLinkEdited(TLRPC.TL_chatInviteExported tL_chatInviteExported, TLObject tLObject);

        void onLinkRemoved(TLRPC.TL_chatInviteExported tL_chatInviteExported);

        void revokeLink(TLRPC.TL_chatInviteExported tL_chatInviteExported);
    }

    public LinkEditActivity(int i, long j) {
        this.type = i;
        this.chatId = j;
    }

    private void chooseDate(int i) {
        long j = i;
        this.timeEditText.setText(LocaleController.formatDateAudio(j, false));
        int currentTime = i - getConnectionsManager().getCurrentTime();
        this.dispalyedDates.clear();
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        while (true) {
            int[] iArr = this.defaultDates;
            if (i2 >= iArr.length) {
                break;
            }
            if (!z && currentTime < iArr[i2]) {
                this.dispalyedDates.add(Integer.valueOf(currentTime));
                i3 = i2;
                z = true;
            }
            this.dispalyedDates.add(Integer.valueOf(this.defaultDates[i2]));
            i2++;
        }
        if (!z) {
            this.dispalyedDates.add(Integer.valueOf(currentTime));
            i3 = this.defaultDates.length;
        }
        int size = this.dispalyedDates.size();
        int i4 = size + 1;
        String[] strArr = new String[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            if (i5 == size) {
                strArr[i5] = LocaleController.getString(R.string.NoLimit);
            } else if (((Integer) this.dispalyedDates.get(i5)).intValue() == this.defaultDates[0]) {
                strArr[i5] = LocaleController.formatPluralString("Hours", 1, new Object[0]);
            } else if (((Integer) this.dispalyedDates.get(i5)).intValue() == this.defaultDates[1]) {
                strArr[i5] = LocaleController.formatPluralString("Days", 1, new Object[0]);
            } else if (((Integer) this.dispalyedDates.get(i5)).intValue() == this.defaultDates[2]) {
                strArr[i5] = LocaleController.formatPluralString("Weeks", 1, new Object[0]);
            } else {
                long j2 = currentTime;
                if (j2 < 86400) {
                    strArr[i5] = LocaleController.getString(R.string.MessageScheduleToday);
                } else {
                    LocaleController localeController = LocaleController.getInstance();
                    if (j2 < 31449600) {
                        strArr[i5] = localeController.getFormatterScheduleDay().format(j * 1000);
                    } else {
                        strArr[i5] = localeController.getFormatterYear().format(j * 1000);
                    }
                }
            }
        }
        this.timeChooseView.setOptions(i3, strArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void chooseUses(int i) {
        int i2;
        this.dispalyedUses.clear();
        int i3 = 0;
        boolean z = false;
        int i4 = 0;
        while (true) {
            int[] iArr = this.defaultUses;
            if (i3 >= iArr.length) {
                break;
            }
            if (!z && i <= (i2 = iArr[i3])) {
                if (i != i2) {
                    this.dispalyedUses.add(Integer.valueOf(i));
                }
                z = true;
                i4 = i3;
            }
            this.dispalyedUses.add(Integer.valueOf(this.defaultUses[i3]));
            i3++;
        }
        if (!z) {
            this.dispalyedUses.add(Integer.valueOf(i));
            i4 = this.defaultUses.length;
        }
        int size = this.dispalyedUses.size();
        int i5 = size + 1;
        String[] strArr = new String[i5];
        for (int i6 = 0; i6 < i5; i6++) {
            if (i6 == size) {
                strArr[i6] = LocaleController.getString(R.string.NoLimit);
            } else {
                strArr[i6] = ((Integer) this.dispalyedUses.get(i6)).toString();
            }
        }
        this.usesChooseView.setOptions(i4, strArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(View view) {
        TextCheckCell textCheckCell = this.subCell;
        if (textCheckCell != null && textCheckCell.isChecked()) {
            TextCheckCell textCheckCell2 = this.subCell;
            int i = -this.shakeDp;
            this.shakeDp = i;
            AndroidUtilities.shakeViewSpring(textCheckCell2, i);
            return;
        }
        TextCheckCell textCheckCell3 = (TextCheckCell) view;
        boolean isChecked = textCheckCell3.isChecked();
        boolean z = !isChecked;
        textCheckCell3.setBackgroundColorAnimated(z, Theme.getColor(z ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
        textCheckCell3.setChecked(z);
        setUsesVisible(isChecked);
        this.firstLayout = true;
        if (this.subCell != null) {
            if (textCheckCell3.isChecked()) {
                this.subCell.setChecked(false);
                this.subCell.setCheckBoxIcon(R.drawable.permission_locked);
                this.subEditPriceCell.setVisibility(8);
            } else if (this.inviteToEdit == null) {
                this.subCell.setCheckBoxIcon(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1() {
        this.subEditPriceCell.editText.requestFocus();
        AndroidUtilities.showKeyboard(this.subEditPriceCell.editText);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString(R.string.RevokeAlert));
        builder.setTitle(LocaleController.getString(R.string.RevokeLink));
        builder.setPositiveButton(LocaleController.getString(R.string.RevokeButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda13
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                LinkEditActivity.this.lambda$createView$9(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$11(Integer num) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2() {
        this.subEditPriceCell.editText.clearFocus();
        AndroidUtilities.hideKeyboard(this.subEditPriceCell.editText);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(Runnable[] runnableArr, View view) {
        if (this.inviteToEdit != null) {
            return;
        }
        if (this.approveCell.isChecked()) {
            TextCheckCell textCheckCell = this.approveCell;
            int i = -this.shakeDp;
            this.shakeDp = i;
            AndroidUtilities.shakeViewSpring(textCheckCell, i);
            return;
        }
        TextCheckCell textCheckCell2 = (TextCheckCell) view;
        textCheckCell2.setChecked(!textCheckCell2.isChecked());
        this.subEditPriceCell.setVisibility(textCheckCell2.isChecked() ? 0 : 8);
        AndroidUtilities.cancelRunOnUIThread(runnableArr[0]);
        if (!textCheckCell2.isChecked()) {
            this.approveCell.setCheckBoxIcon(0);
            this.approveHintCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescription));
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    LinkEditActivity.this.lambda$createView$2();
                }
            };
            runnableArr[0] = runnable;
            AndroidUtilities.runOnUIThread(runnable);
            return;
        }
        this.approveCell.setChecked(false);
        this.approveCell.setCheckBoxIcon(R.drawable.permission_locked);
        this.approveHintCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescriptionFrozen));
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                LinkEditActivity.this.lambda$createView$1();
            }
        };
        runnableArr[0] = runnable2;
        AndroidUtilities.runOnUIThread(runnable2, 60L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4() {
        Browser.openUrl(getContext(), LocaleController.getString(R.string.RequireMonthlyFeeInfoLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(boolean z, int i) {
        chooseDate(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(Context context, View view) {
        AlertsCreator.createDatePickerDialog(context, LocaleController.getString(R.string.ExpireAfter), LocaleController.getString(R.string.SetTimeLimit), -1L, new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda12
            @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
            public final void didSelectDate(boolean z, int i) {
                LinkEditActivity.this.lambda$createView$5(z, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(int i) {
        TextView textView;
        String str;
        if (i < this.dispalyedDates.size()) {
            long intValue = ((Integer) this.dispalyedDates.get(i)).intValue() + getConnectionsManager().getCurrentTime();
            textView = this.timeEditText;
            str = LocaleController.formatDateAudio(intValue, false);
        } else {
            textView = this.timeEditText;
            str = "";
        }
        textView.setText(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(int i) {
        this.usesEditText.clearFocus();
        this.ignoreSet = true;
        if (i < this.dispalyedUses.size()) {
            this.usesEditText.setText(((Integer) this.dispalyedUses.get(i)).toString());
        } else {
            this.usesEditText.setText("");
        }
        this.ignoreSet = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(DialogInterface dialogInterface, int i) {
        this.callback.revokeLink(this.inviteToEdit);
        lambda$onBackPressed$321();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$16() {
        TextInfoPrivacyCell textInfoPrivacyCell = this.dividerUses;
        if (textInfoPrivacyCell != null) {
            Context context = textInfoPrivacyCell.getContext();
            TextInfoPrivacyCell textInfoPrivacyCell2 = this.dividerUses;
            int i = R.drawable.greydivider_bottom;
            int i2 = Theme.key_windowBackgroundGrayShadow;
            textInfoPrivacyCell2.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i, i2));
            this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i2));
            this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
            EditText editText = this.usesEditText;
            int i3 = Theme.key_windowBackgroundWhiteBlackText;
            editText.setTextColor(Theme.getColor(i3));
            EditText editText2 = this.usesEditText;
            int i4 = Theme.key_windowBackgroundWhiteGrayText;
            editText2.setHintTextColor(Theme.getColor(i4));
            this.timeEditText.setTextColor(Theme.getColor(i3));
            this.timeEditText.setHintTextColor(Theme.getColor(i4));
            this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
            TextSettingsCell textSettingsCell = this.revokeLink;
            if (textSettingsCell != null) {
                textSettingsCell.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
            }
            this.createTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
            this.dividerName.setBackground(Theme.getThemedDrawableByKey(context, i, i2));
            this.nameEditText.setTextColor(Theme.getColor(i3));
            this.nameEditText.setHintTextColor(Theme.getColor(i4));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateClicked$12(TLRPC.TL_error tL_error, TLObject tLObject) {
        this.loading = false;
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (tL_error != null) {
            AlertsCreator.showSimpleAlert(this, tL_error.text);
            return;
        }
        Callback callback = this.callback;
        if (callback != null) {
            callback.onLinkCreated(tLObject);
        }
        lambda$onBackPressed$321();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateClicked$13(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                LinkEditActivity.this.lambda$onCreateClicked$12(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateClicked$14(TLRPC.TL_error tL_error, TLObject tLObject) {
        this.loading = false;
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (tL_error != null) {
            AlertsCreator.showSimpleAlert(this, tL_error.text);
            return;
        }
        if (tLObject instanceof TLRPC.TL_messages_exportedChatInvite) {
            this.inviteToEdit = (TLRPC.TL_chatInviteExported) ((TLRPC.TL_messages_exportedChatInvite) tLObject).invite;
        }
        Callback callback = this.callback;
        if (callback != null) {
            callback.onLinkEdited(this.inviteToEdit, tLObject);
        }
        lambda$onBackPressed$321();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateClicked$15(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                LinkEditActivity.this.lambda$onCreateClicked$14(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01fb  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0255  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onCreateClicked(View view) {
        long j;
        int i;
        boolean z;
        boolean z2;
        TextCheckCell textCheckCell;
        String obj;
        RequestDelegate requestDelegate;
        TLRPC.TL_messages_editExportedChatInvite tL_messages_editExportedChatInvite;
        ConnectionsManager connectionsManager;
        if (this.loading) {
            return;
        }
        int selectedIndex = this.timeChooseView.getSelectedIndex();
        if (selectedIndex < this.dispalyedDates.size() && ((Integer) this.dispalyedDates.get(selectedIndex)).intValue() < 0) {
            AndroidUtilities.shakeView(this.timeEditText);
            Vibrator vibrator = (Vibrator) this.timeEditText.getContext().getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
                return;
            }
            return;
        }
        TextCheckCell textCheckCell2 = this.subCell;
        if (textCheckCell2 != null && textCheckCell2.isChecked()) {
            try {
                j = Long.parseLong(this.subEditPriceCell.editText.getText().toString());
            } catch (Exception e) {
                FileLog.e(e);
            }
            i = this.type;
            if (i != 0) {
                AlertDialog alertDialog = this.progressDialog;
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                this.loading = true;
                AlertDialog alertDialog2 = new AlertDialog(getParentActivity(), 3);
                this.progressDialog = alertDialog2;
                alertDialog2.showDelayed(500L);
                TLRPC.TL_messages_exportChatInvite tL_messages_exportChatInvite = new TLRPC.TL_messages_exportChatInvite();
                tL_messages_exportChatInvite.peer = getMessagesController().getInputPeer(-this.chatId);
                tL_messages_exportChatInvite.legacy_revoke_permanent = false;
                int selectedIndex2 = this.timeChooseView.getSelectedIndex();
                tL_messages_exportChatInvite.flags |= 1;
                if (selectedIndex2 < this.dispalyedDates.size()) {
                    tL_messages_exportChatInvite.expire_date = ((Integer) this.dispalyedDates.get(selectedIndex2)).intValue() + getConnectionsManager().getCurrentTime();
                } else {
                    tL_messages_exportChatInvite.expire_date = 0;
                }
                int selectedIndex3 = this.usesChooseView.getSelectedIndex();
                tL_messages_exportChatInvite.flags |= 2;
                if (selectedIndex3 < this.dispalyedUses.size()) {
                    tL_messages_exportChatInvite.usage_limit = ((Integer) this.dispalyedUses.get(selectedIndex3)).intValue();
                } else {
                    tL_messages_exportChatInvite.usage_limit = 0;
                }
                TextCheckCell textCheckCell3 = this.approveCell;
                boolean z3 = textCheckCell3 != null && textCheckCell3.isChecked();
                tL_messages_exportChatInvite.request_needed = z3;
                if (z3) {
                    tL_messages_exportChatInvite.usage_limit = 0;
                }
                String obj2 = this.nameEditText.getText().toString();
                tL_messages_exportChatInvite.title = obj2;
                if (!TextUtils.isEmpty(obj2)) {
                    tL_messages_exportChatInvite.flags |= 16;
                }
                if (j > 0) {
                    tL_messages_exportChatInvite.flags |= 32;
                    TL_stars.TL_starsSubscriptionPricing tL_starsSubscriptionPricing = new TL_stars.TL_starsSubscriptionPricing();
                    tL_messages_exportChatInvite.subscription_pricing = tL_starsSubscriptionPricing;
                    tL_starsSubscriptionPricing.period = getConnectionsManager().isTestBackend() ? NotificationCenter.uploadStoryProgress : 2592000;
                    tL_messages_exportChatInvite.subscription_pricing.amount = j;
                }
                ConnectionsManager connectionsManager2 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda14
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        LinkEditActivity.this.lambda$onCreateClicked$13(tLObject, tL_error);
                    }
                };
                connectionsManager = connectionsManager2;
                tL_messages_editExportedChatInvite = tL_messages_exportChatInvite;
            } else {
                if (i != 1) {
                    return;
                }
                AlertDialog alertDialog3 = this.progressDialog;
                if (alertDialog3 != null) {
                    alertDialog3.dismiss();
                }
                TLRPC.TL_messages_editExportedChatInvite tL_messages_editExportedChatInvite2 = new TLRPC.TL_messages_editExportedChatInvite();
                tL_messages_editExportedChatInvite2.link = this.inviteToEdit.link;
                tL_messages_editExportedChatInvite2.revoked = false;
                tL_messages_editExportedChatInvite2.peer = getMessagesController().getInputPeer(-this.chatId);
                int selectedIndex4 = this.timeChooseView.getSelectedIndex();
                if (selectedIndex4 < this.dispalyedDates.size()) {
                    if (this.currentInviteDate != ((Integer) this.dispalyedDates.get(selectedIndex4)).intValue()) {
                        tL_messages_editExportedChatInvite2.flags |= 1;
                        tL_messages_editExportedChatInvite2.expire_date = ((Integer) this.dispalyedDates.get(selectedIndex4)).intValue() + getConnectionsManager().getCurrentTime();
                        z = true;
                    }
                    z = false;
                } else {
                    if (this.currentInviteDate != 0) {
                        tL_messages_editExportedChatInvite2.flags |= 1;
                        tL_messages_editExportedChatInvite2.expire_date = 0;
                        z = true;
                    }
                    z = false;
                }
                int selectedIndex5 = this.usesChooseView.getSelectedIndex();
                if (selectedIndex5 < this.dispalyedUses.size()) {
                    int intValue = ((Integer) this.dispalyedUses.get(selectedIndex5)).intValue();
                    if (this.inviteToEdit.usage_limit != intValue) {
                        tL_messages_editExportedChatInvite2.flags |= 2;
                        tL_messages_editExportedChatInvite2.usage_limit = intValue;
                        z = true;
                    }
                    z2 = this.inviteToEdit.request_needed;
                    textCheckCell = this.approveCell;
                    if (z2 != (textCheckCell == null && textCheckCell.isChecked())) {
                        tL_messages_editExportedChatInvite2.flags |= 8;
                        TextCheckCell textCheckCell4 = this.approveCell;
                        boolean z4 = textCheckCell4 != null && textCheckCell4.isChecked();
                        tL_messages_editExportedChatInvite2.request_needed = z4;
                        if (z4) {
                            tL_messages_editExportedChatInvite2.flags |= 2;
                            tL_messages_editExportedChatInvite2.usage_limit = 0;
                        }
                        z = true;
                    }
                    obj = this.nameEditText.getText().toString();
                    if (!TextUtils.equals(this.inviteToEdit.title, obj)) {
                        tL_messages_editExportedChatInvite2.title = obj;
                        tL_messages_editExportedChatInvite2.flags |= 16;
                        z = true;
                    }
                    if (z) {
                        lambda$onBackPressed$321();
                        return;
                    }
                    this.loading = true;
                    AlertDialog alertDialog4 = new AlertDialog(getParentActivity(), 3);
                    this.progressDialog = alertDialog4;
                    alertDialog4.showDelayed(500L);
                    ConnectionsManager connectionsManager3 = getConnectionsManager();
                    requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda15
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LinkEditActivity.this.lambda$onCreateClicked$15(tLObject, tL_error);
                        }
                    };
                    connectionsManager = connectionsManager3;
                    tL_messages_editExportedChatInvite = tL_messages_editExportedChatInvite2;
                } else {
                    if (this.inviteToEdit.usage_limit != 0) {
                        tL_messages_editExportedChatInvite2.flags |= 2;
                        tL_messages_editExportedChatInvite2.usage_limit = 0;
                        z = true;
                    }
                    z2 = this.inviteToEdit.request_needed;
                    textCheckCell = this.approveCell;
                    if (z2 != (textCheckCell == null && textCheckCell.isChecked())) {
                    }
                    obj = this.nameEditText.getText().toString();
                    if (!TextUtils.equals(this.inviteToEdit.title, obj)) {
                    }
                    if (z) {
                    }
                }
            }
            connectionsManager.sendRequest(tL_messages_editExportedChatInvite, requestDelegate);
        }
        j = 0;
        i = this.type;
        if (i != 0) {
        }
        connectionsManager.sendRequest(tL_messages_editExportedChatInvite, requestDelegate);
    }

    private void resetDates() {
        this.dispalyedDates.clear();
        int i = 0;
        while (true) {
            int[] iArr = this.defaultDates;
            if (i >= iArr.length) {
                this.timeChooseView.setOptions(3, LocaleController.formatPluralString("Hours", 1, new Object[0]), LocaleController.formatPluralString("Days", 1, new Object[0]), LocaleController.formatPluralString("Weeks", 1, new Object[0]), LocaleController.getString(R.string.NoLimit));
                return;
            } else {
                this.dispalyedDates.add(Integer.valueOf(iArr[i]));
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetUses() {
        this.dispalyedUses.clear();
        int i = 0;
        while (true) {
            int[] iArr = this.defaultUses;
            if (i >= iArr.length) {
                this.usesChooseView.setOptions(3, "1", "10", "100", LocaleController.getString(R.string.NoLimit));
                return;
            } else {
                this.dispalyedUses.add(Integer.valueOf(iArr[i]));
                i++;
            }
        }
    }

    private void setUsesVisible(boolean z) {
        this.usesHeaderCell.setVisibility(z ? 0 : 8);
        this.usesChooseView.setVisibility(z ? 0 : 8);
        this.usesEditText.setVisibility(z ? 0 : 8);
        this.dividerUses.setVisibility(z ? 0 : 8);
        this.divider.setBackground(Theme.getThemedDrawableByKey(getParentActivity(), z ? R.drawable.greydivider : R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x013f  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0512  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0202  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x024a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x028e  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x033d  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0347  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0291  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0059  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        ActionBar actionBar;
        int i;
        int i2;
        TextView textView;
        int i3;
        int i4;
        TextView textView2;
        int i5;
        TLRPC.Chat chat;
        TLRPC.TL_chatInviteExported tL_chatInviteExported;
        int i6;
        int i7;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        int i8 = this.type;
        if (i8 != 0) {
            if (i8 == 1) {
                actionBar = this.actionBar;
                i = R.string.EditLink;
            }
            this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LinkEditActivity.1
                @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
                public void onItemClick(int i9) {
                    if (i9 == -1) {
                        LinkEditActivity.this.lambda$onBackPressed$321();
                        AndroidUtilities.hideKeyboard(LinkEditActivity.this.usesEditText);
                    }
                }
            });
            TextView textView3 = new TextView(context);
            this.createTextView = textView3;
            textView3.setEllipsize(TextUtils.TruncateAt.END);
            this.createTextView.setGravity(16);
            this.createTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LinkEditActivity.this.onCreateClicked(view);
                }
            });
            this.createTextView.setSingleLine();
            i2 = this.type;
            if (i2 == 0) {
                if (i2 == 1) {
                    textView = this.createTextView;
                    i3 = R.string.SaveLinkHeader;
                }
                this.createTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
                this.createTextView.setTextSize(1, 14.0f);
                this.createTextView.setTypeface(AndroidUtilities.bold());
                this.createTextView.setPadding(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f));
                this.actionBar.addView(this.createTextView, LayoutHelper.createFrame(-2, -2.0f, 8388629, 0.0f, this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight / AndroidUtilities.dp(2.0f) : 0, 0.0f, 0.0f));
                this.scrollView = new ScrollView(context);
                SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.LinkEditActivity.2
                    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
                    protected void dispatchDraw(Canvas canvas) {
                        super.dispatchDraw(canvas);
                        LinkEditActivity linkEditActivity = LinkEditActivity.this;
                        if (linkEditActivity.scrollToEnd) {
                            linkEditActivity.scrollToEnd = false;
                            linkEditActivity.scrollView.smoothScrollTo(0, Math.max(0, LinkEditActivity.this.scrollView.getChildAt(0).getMeasuredHeight() - LinkEditActivity.this.scrollView.getMeasuredHeight()));
                        } else if (linkEditActivity.scrollToStart) {
                            linkEditActivity.scrollToStart = false;
                            linkEditActivity.scrollView.smoothScrollTo(0, 0);
                        }
                    }

                    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
                    protected void onLayout(boolean z, int i9, int i10, int i11, int i12) {
                        int scrollY = LinkEditActivity.this.scrollView.getScrollY();
                        super.onLayout(z, i9, i10, i11, i12);
                        if (scrollY != LinkEditActivity.this.scrollView.getScrollY()) {
                            LinkEditActivity linkEditActivity = LinkEditActivity.this;
                            if (linkEditActivity.scrollToEnd) {
                                return;
                            }
                            linkEditActivity.scrollView.setTranslationY(LinkEditActivity.this.scrollView.getScrollY() - scrollY);
                            LinkEditActivity.this.scrollView.animate().cancel();
                            LinkEditActivity.this.scrollView.animate().translationY(0.0f).setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator).start();
                        }
                    }

                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i9, int i10) {
                        super.onMeasure(i9, i10);
                        measureKeyboardHeight();
                        int i11 = this.keyboardHeight;
                        if (i11 != 0 && i11 < AndroidUtilities.dp(20.0f)) {
                            LinkEditActivity.this.usesEditText.clearFocus();
                            LinkEditActivity.this.nameEditText.clearFocus();
                        }
                        LinkEditActivity.this.buttonLayout.setVisibility(this.keyboardHeight > AndroidUtilities.dp(20.0f) ? 8 : 0);
                    }
                };
                this.fragmentView = sizeNotifierFrameLayout;
                LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.LinkEditActivity.3
                    @Override // android.view.ViewGroup, android.view.View
                    protected void dispatchDraw(Canvas canvas) {
                        super.dispatchDraw(canvas);
                        LinkEditActivity.this.firstLayout = false;
                    }

                    @Override // android.widget.LinearLayout, android.view.View
                    protected void onMeasure(int i9, int i10) {
                        super.onMeasure(i9, i10);
                    }
                };
                LayoutTransition layoutTransition = new LayoutTransition();
                layoutTransition.setDuration(420L);
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
                layoutTransition.setInterpolator(2, cubicBezierInterpolator);
                layoutTransition.setInterpolator(0, cubicBezierInterpolator);
                layoutTransition.setInterpolator(4, cubicBezierInterpolator);
                layoutTransition.setInterpolator(1, cubicBezierInterpolator);
                layoutTransition.setInterpolator(3, cubicBezierInterpolator);
                linearLayout.setLayoutTransition(layoutTransition);
                linearLayout.setOrientation(1);
                linearLayout.setPadding(0, 0, 0, AndroidUtilities.dp(79.0f));
                this.scrollView.addView(linearLayout);
                TextView textView4 = new TextView(context);
                this.buttonTextView = textView4;
                textView4.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
                this.buttonTextView.setGravity(17);
                this.buttonTextView.setTextSize(1, 14.0f);
                this.buttonTextView.setTypeface(AndroidUtilities.bold());
                i4 = this.type;
                if (i4 != 0) {
                    if (i4 == 1) {
                        textView2 = this.buttonTextView;
                        i5 = R.string.SaveLink;
                    }
                    chat = getMessagesController().getChat(Long.valueOf(this.chatId));
                    if (chat != null || chat.username == null) {
                        TextCheckCell textCheckCell = new TextCheckCell(context) { // from class: org.telegram.ui.LinkEditActivity.4
                            @Override // org.telegram.ui.Cells.TextCheckCell, android.view.View
                            protected void onDraw(Canvas canvas) {
                                canvas.save();
                                canvas.clipRect(0, 0, getWidth(), getHeight());
                                super.onDraw(canvas);
                                canvas.restore();
                            }
                        };
                        this.approveCell = textCheckCell;
                        int i9 = Theme.key_windowBackgroundUnchecked;
                        textCheckCell.setBackgroundColor(Theme.getColor(i9));
                        this.approveCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
                        this.approveCell.setDrawCheckRipple(true);
                        this.approveCell.setHeight(56);
                        this.approveCell.setTag(Integer.valueOf(i9));
                        this.approveCell.setTextAndCheck(LocaleController.getString(R.string.ApproveNewMembers), false, false);
                        this.approveCell.setTypeface(AndroidUtilities.bold());
                        this.approveCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                LinkEditActivity.this.lambda$createView$0(view);
                            }
                        });
                        linearLayout.addView(this.approveCell, LayoutHelper.createLinear(-1, 56));
                        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(context);
                        this.approveHintCell = textInfoPrivacyCell;
                        textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        this.approveHintCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescription));
                        linearLayout.addView(this.approveHintCell);
                        TLRPC.ChatFull chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(this.chatId);
                        if ((this.inviteToEdit == null && ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chatId))) && chatFull != null && chatFull.paid_media_allowed) || ((tL_chatInviteExported = this.inviteToEdit) != null && tL_chatInviteExported.subscription_pricing != null)) {
                            TextCheckCell textCheckCell2 = new TextCheckCell(context);
                            this.subCell = textCheckCell2;
                            int i10 = Theme.key_windowBackgroundWhite;
                            textCheckCell2.setBackgroundColor(Theme.getColor(i10));
                            this.subCell.setDrawCheckRipple(true);
                            this.subCell.setTextAndCheck(LocaleController.getString(R.string.RequireMonthlyFee), false, true);
                            if (this.inviteToEdit != null) {
                                this.subCell.setCheckBoxIcon(R.drawable.permission_locked);
                                this.subCell.setEnabled(false);
                            }
                            final Runnable[] runnableArr = new Runnable[1];
                            this.subCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda3
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    LinkEditActivity.this.lambda$createView$3(runnableArr, view);
                                }
                            });
                            linearLayout.addView(this.subCell, LayoutHelper.createLinear(-1, 48));
                            TextView textView5 = new TextView(context);
                            this.subPriceView = textView5;
                            textView5.setTextSize(1, 16.0f);
                            this.subPriceView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
                            EditTextCell editTextCell = new EditTextCell(context, LocaleController.getString(getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceHintTest5Minutes : R.string.RequireMonthlyFeePriceHint), false, false, -1, this.resourceProvider) { // from class: org.telegram.ui.LinkEditActivity.5
                                private boolean ignoreTextChanged;

                                @Override // org.telegram.ui.Cells.EditTextCell
                                protected void onTextChanged(CharSequence charSequence) {
                                    super.onTextChanged(charSequence);
                                    if (this.ignoreTextChanged) {
                                        return;
                                    }
                                    if (TextUtils.isEmpty(charSequence)) {
                                        LinkEditActivity.this.subPriceView.setText("");
                                        return;
                                    }
                                    try {
                                        long parseLong = Long.parseLong(charSequence.toString());
                                        if (parseLong > LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax) {
                                            this.ignoreTextChanged = true;
                                            parseLong = LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax;
                                            setText(Long.toString(parseLong));
                                            this.ignoreTextChanged = false;
                                        }
                                        TextView textView6 = LinkEditActivity.this.subPriceView;
                                        int i11 = LinkEditActivity.this.getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceTest5Minutes : R.string.RequireMonthlyFeePrice;
                                        BillingController billingController = BillingController.getInstance();
                                        double d = parseLong;
                                        Double.isNaN(d);
                                        double d2 = d / 1000.0d;
                                        double d3 = MessagesController.getInstance(((BaseFragment) LinkEditActivity.this).currentAccount).starsUsdWithdrawRate1000;
                                        Double.isNaN(d3);
                                        textView6.setText(LocaleController.formatString(i11, billingController.formatCurrency((long) (d2 * d3), "USD")));
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                    }
                                }
                            };
                            this.subEditPriceCell = editTextCell;
                            editTextCell.editText.setInputType(2);
                            this.subEditPriceCell.editText.setRawInputType(2);
                            this.subEditPriceCell.setBackgroundColor(getThemedColor(i10));
                            this.subEditPriceCell.hideKeyboardOnEnter();
                            this.subEditPriceCell.addView(this.subPriceView, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 19.0f, 0.0f));
                            ImageView leftDrawable = this.subEditPriceCell.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate());
                            leftDrawable.setScaleX(0.83f);
                            leftDrawable.setScaleY(0.83f);
                            leftDrawable.setTranslationY(AndroidUtilities.dp(-1.0f));
                            leftDrawable.setTranslationX(AndroidUtilities.dp(1.0f));
                            i6 = -1;
                            linearLayout.addView(this.subEditPriceCell, LayoutHelper.createLinear(-1, 48));
                            this.subEditPriceCell.setVisibility(8);
                            TextInfoPrivacyCell textInfoPrivacyCell2 = new TextInfoPrivacyCell(context);
                            this.subInfoCell = textInfoPrivacyCell2;
                            textInfoPrivacyCell2.setText(this.inviteToEdit != null ? LocaleController.getString(R.string.RequireMonthlyFeeInfoFrozen) : AndroidUtilities.withLearnMore(LocaleController.getString(R.string.RequireMonthlyFeeInfo), new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda4
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LinkEditActivity.this.lambda$createView$4();
                                }
                            }));
                            i7 = -2;
                            linearLayout.addView(this.subInfoCell, LayoutHelper.createLinear(-1, -2));
                            HeaderCell headerCell = new HeaderCell(context);
                            this.timeHeaderCell = headerCell;
                            headerCell.setText(LocaleController.getString(R.string.LimitByPeriod));
                            linearLayout.addView(this.timeHeaderCell);
                            SlideChooseView slideChooseView = new SlideChooseView(context);
                            this.timeChooseView = slideChooseView;
                            linearLayout.addView(slideChooseView);
                            TextView textView6 = new TextView(context);
                            this.timeEditText = textView6;
                            textView6.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                            this.timeEditText.setGravity(16);
                            this.timeEditText.setTextSize(1, 16.0f);
                            this.timeEditText.setHint(LocaleController.getString(R.string.TimeLimitHint));
                            this.timeEditText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda5
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    LinkEditActivity.this.lambda$createView$6(context, view);
                                }
                            });
                            this.timeChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda6
                                @Override // org.telegram.ui.Components.SlideChooseView.Callback
                                public final void onOptionSelected(int i11) {
                                    LinkEditActivity.this.lambda$createView$7(i11);
                                }

                                @Override // org.telegram.ui.Components.SlideChooseView.Callback
                                public /* synthetic */ void onTouchEnd() {
                                    SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                                }
                            });
                            resetDates();
                            linearLayout.addView(this.timeEditText, LayoutHelper.createLinear(i6, 50));
                            TextInfoPrivacyCell textInfoPrivacyCell3 = new TextInfoPrivacyCell(context);
                            this.divider = textInfoPrivacyCell3;
                            textInfoPrivacyCell3.setText(LocaleController.getString(R.string.TimeLimitHelp));
                            linearLayout.addView(this.divider);
                            HeaderCell headerCell2 = new HeaderCell(context);
                            this.usesHeaderCell = headerCell2;
                            headerCell2.setText(LocaleController.getString(R.string.LimitNumberOfUses));
                            linearLayout.addView(this.usesHeaderCell);
                            SlideChooseView slideChooseView2 = new SlideChooseView(context);
                            this.usesChooseView = slideChooseView2;
                            slideChooseView2.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda7
                                @Override // org.telegram.ui.Components.SlideChooseView.Callback
                                public final void onOptionSelected(int i11) {
                                    LinkEditActivity.this.lambda$createView$8(i11);
                                }

                                @Override // org.telegram.ui.Components.SlideChooseView.Callback
                                public /* synthetic */ void onTouchEnd() {
                                    SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                                }
                            });
                            resetUses();
                            linearLayout.addView(this.usesChooseView);
                            EditText editText = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.6
                                @Override // android.widget.TextView, android.view.View
                                public boolean onTouchEvent(MotionEvent motionEvent) {
                                    if (motionEvent.getAction() == 1) {
                                        setCursorVisible(true);
                                    }
                                    return super.onTouchEvent(motionEvent);
                                }
                            };
                            this.usesEditText = editText;
                            editText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                            this.usesEditText.setGravity(16);
                            this.usesEditText.setTextSize(1, 16.0f);
                            this.usesEditText.setHint(LocaleController.getString(R.string.UsesLimitHint));
                            this.usesEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                            this.usesEditText.setInputType(2);
                            this.usesEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.7
                                @Override // android.text.TextWatcher
                                public void afterTextChanged(Editable editable) {
                                    if (LinkEditActivity.this.ignoreSet) {
                                        return;
                                    }
                                    if (editable.toString().equals("0")) {
                                        LinkEditActivity.this.usesEditText.setText("");
                                        return;
                                    }
                                    try {
                                        int parseInt = Integer.parseInt(editable.toString());
                                        if (parseInt > 100000) {
                                            LinkEditActivity.this.resetUses();
                                        } else {
                                            LinkEditActivity.this.chooseUses(parseInt);
                                        }
                                    } catch (NumberFormatException unused) {
                                        LinkEditActivity.this.resetUses();
                                    }
                                }

                                @Override // android.text.TextWatcher
                                public void beforeTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                                }

                                @Override // android.text.TextWatcher
                                public void onTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                                }
                            });
                            linearLayout.addView(this.usesEditText, LayoutHelper.createLinear(i6, 50));
                            TextInfoPrivacyCell textInfoPrivacyCell4 = new TextInfoPrivacyCell(context);
                            this.dividerUses = textInfoPrivacyCell4;
                            textInfoPrivacyCell4.setText(LocaleController.getString(R.string.UsesLimitHelp));
                            linearLayout.addView(this.dividerUses);
                            EditText editText2 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.8
                                @Override // android.widget.TextView, android.view.View
                                public boolean onTouchEvent(MotionEvent motionEvent) {
                                    if (motionEvent.getAction() == 1) {
                                        setCursorVisible(true);
                                    }
                                    return super.onTouchEvent(motionEvent);
                                }
                            };
                            this.nameEditText = editText2;
                            editText2.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.9
                                @Override // android.text.TextWatcher
                                public void afterTextChanged(Editable editable) {
                                    Emoji.replaceEmoji((CharSequence) editable, LinkEditActivity.this.nameEditText.getPaint().getFontMetricsInt(), (int) LinkEditActivity.this.nameEditText.getPaint().getTextSize(), false);
                                }

                                @Override // android.text.TextWatcher
                                public void beforeTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                                }

                                @Override // android.text.TextWatcher
                                public void onTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                                }
                            });
                            this.nameEditText.setCursorVisible(false);
                            this.nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                            this.nameEditText.setGravity(16);
                            this.nameEditText.setHint(LocaleController.getString(R.string.LinkNameHint));
                            EditText editText3 = this.nameEditText;
                            int i11 = Theme.key_windowBackgroundWhiteGrayText;
                            editText3.setHintTextColor(Theme.getColor(i11));
                            this.nameEditText.setLines(1);
                            this.nameEditText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                            this.nameEditText.setSingleLine();
                            EditText editText4 = this.nameEditText;
                            int i12 = Theme.key_windowBackgroundWhiteBlackText;
                            editText4.setTextColor(Theme.getColor(i12));
                            this.nameEditText.setTextSize(1, 16.0f);
                            linearLayout.addView(this.nameEditText, LayoutHelper.createLinear(i6, 50));
                            TextInfoPrivacyCell textInfoPrivacyCell5 = new TextInfoPrivacyCell(context);
                            this.dividerName = textInfoPrivacyCell5;
                            int i13 = R.drawable.greydivider_bottom;
                            int i14 = Theme.key_windowBackgroundGrayShadow;
                            textInfoPrivacyCell5.setBackground(Theme.getThemedDrawableByKey(context, i13, i14));
                            this.dividerName.setText(LocaleController.getString(R.string.LinkNameHelp));
                            linearLayout.addView(this.dividerName);
                            if (this.type == 1) {
                                TextSettingsCell textSettingsCell = new TextSettingsCell(context);
                                this.revokeLink = textSettingsCell;
                                textSettingsCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                                this.revokeLink.setText(LocaleController.getString(R.string.RevokeLink), false);
                                this.revokeLink.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
                                this.revokeLink.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda8
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view) {
                                        LinkEditActivity.this.lambda$createView$10(view);
                                    }
                                });
                                linearLayout.addView(this.revokeLink);
                            }
                            sizeNotifierFrameLayout.addView(this.scrollView, LayoutHelper.createFrame(i6, -1.0f));
                            FrameLayout frameLayout = new FrameLayout(context);
                            this.buttonLayout = frameLayout;
                            int i15 = Theme.key_windowBackgroundGray;
                            frameLayout.setBackgroundColor(getThemedColor(i15));
                            new KeyboardNotifier(sizeNotifierFrameLayout, new Utilities.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda9
                                @Override // org.telegram.messenger.Utilities.Callback
                                public final void run(Object obj) {
                                    LinkEditActivity.lambda$createView$11((Integer) obj);
                                }
                            });
                            this.buttonLayout.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 87, 16.0f, 15.0f, 16.0f, 16.0f));
                            sizeNotifierFrameLayout.addView(this.buttonLayout, LayoutHelper.createFrame(i6, i7, 80));
                            HeaderCell headerCell3 = this.timeHeaderCell;
                            int i16 = Theme.key_windowBackgroundWhite;
                            headerCell3.setBackgroundColor(Theme.getColor(i16));
                            this.timeChooseView.setBackgroundColor(Theme.getColor(i16));
                            this.timeEditText.setBackgroundColor(Theme.getColor(i16));
                            this.usesHeaderCell.setBackgroundColor(Theme.getColor(i16));
                            this.usesChooseView.setBackgroundColor(Theme.getColor(i16));
                            this.usesEditText.setBackgroundColor(Theme.getColor(i16));
                            this.nameEditText.setBackgroundColor(Theme.getColor(i16));
                            sizeNotifierFrameLayout.setBackgroundColor(Theme.getColor(i15));
                            this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda1
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    LinkEditActivity.this.onCreateClicked(view);
                                }
                            });
                            this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
                            this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i13, i14));
                            this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i14));
                            this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
                            this.usesEditText.setTextColor(Theme.getColor(i12));
                            this.usesEditText.setHintTextColor(Theme.getColor(i11));
                            this.timeEditText.setTextColor(Theme.getColor(i12));
                            this.timeEditText.setHintTextColor(Theme.getColor(i11));
                            this.usesEditText.setCursorVisible(false);
                            setInviteToEdit(this.inviteToEdit);
                            sizeNotifierFrameLayout.setClipChildren(false);
                            this.scrollView.setClipChildren(false);
                            linearLayout.setClipChildren(false);
                            return sizeNotifierFrameLayout;
                        }
                    }
                    i6 = -1;
                    i7 = -2;
                    HeaderCell headerCell4 = new HeaderCell(context);
                    this.timeHeaderCell = headerCell4;
                    headerCell4.setText(LocaleController.getString(R.string.LimitByPeriod));
                    linearLayout.addView(this.timeHeaderCell);
                    SlideChooseView slideChooseView3 = new SlideChooseView(context);
                    this.timeChooseView = slideChooseView3;
                    linearLayout.addView(slideChooseView3);
                    TextView textView62 = new TextView(context);
                    this.timeEditText = textView62;
                    textView62.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                    this.timeEditText.setGravity(16);
                    this.timeEditText.setTextSize(1, 16.0f);
                    this.timeEditText.setHint(LocaleController.getString(R.string.TimeLimitHint));
                    this.timeEditText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LinkEditActivity.this.lambda$createView$6(context, view);
                        }
                    });
                    this.timeChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda6
                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public final void onOptionSelected(int i112) {
                            LinkEditActivity.this.lambda$createView$7(i112);
                        }

                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public /* synthetic */ void onTouchEnd() {
                            SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                        }
                    });
                    resetDates();
                    linearLayout.addView(this.timeEditText, LayoutHelper.createLinear(i6, 50));
                    TextInfoPrivacyCell textInfoPrivacyCell32 = new TextInfoPrivacyCell(context);
                    this.divider = textInfoPrivacyCell32;
                    textInfoPrivacyCell32.setText(LocaleController.getString(R.string.TimeLimitHelp));
                    linearLayout.addView(this.divider);
                    HeaderCell headerCell22 = new HeaderCell(context);
                    this.usesHeaderCell = headerCell22;
                    headerCell22.setText(LocaleController.getString(R.string.LimitNumberOfUses));
                    linearLayout.addView(this.usesHeaderCell);
                    SlideChooseView slideChooseView22 = new SlideChooseView(context);
                    this.usesChooseView = slideChooseView22;
                    slideChooseView22.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda7
                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public final void onOptionSelected(int i112) {
                            LinkEditActivity.this.lambda$createView$8(i112);
                        }

                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public /* synthetic */ void onTouchEnd() {
                            SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                        }
                    });
                    resetUses();
                    linearLayout.addView(this.usesChooseView);
                    EditText editText5 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.6
                        @Override // android.widget.TextView, android.view.View
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            if (motionEvent.getAction() == 1) {
                                setCursorVisible(true);
                            }
                            return super.onTouchEvent(motionEvent);
                        }
                    };
                    this.usesEditText = editText5;
                    editText5.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                    this.usesEditText.setGravity(16);
                    this.usesEditText.setTextSize(1, 16.0f);
                    this.usesEditText.setHint(LocaleController.getString(R.string.UsesLimitHint));
                    this.usesEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    this.usesEditText.setInputType(2);
                    this.usesEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.7
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            if (LinkEditActivity.this.ignoreSet) {
                                return;
                            }
                            if (editable.toString().equals("0")) {
                                LinkEditActivity.this.usesEditText.setText("");
                                return;
                            }
                            try {
                                int parseInt = Integer.parseInt(editable.toString());
                                if (parseInt > 100000) {
                                    LinkEditActivity.this.resetUses();
                                } else {
                                    LinkEditActivity.this.chooseUses(parseInt);
                                }
                            } catch (NumberFormatException unused) {
                                LinkEditActivity.this.resetUses();
                            }
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i112, int i122, int i132) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i112, int i122, int i132) {
                        }
                    });
                    linearLayout.addView(this.usesEditText, LayoutHelper.createLinear(i6, 50));
                    TextInfoPrivacyCell textInfoPrivacyCell42 = new TextInfoPrivacyCell(context);
                    this.dividerUses = textInfoPrivacyCell42;
                    textInfoPrivacyCell42.setText(LocaleController.getString(R.string.UsesLimitHelp));
                    linearLayout.addView(this.dividerUses);
                    EditText editText22 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.8
                        @Override // android.widget.TextView, android.view.View
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            if (motionEvent.getAction() == 1) {
                                setCursorVisible(true);
                            }
                            return super.onTouchEvent(motionEvent);
                        }
                    };
                    this.nameEditText = editText22;
                    editText22.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.9
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            Emoji.replaceEmoji((CharSequence) editable, LinkEditActivity.this.nameEditText.getPaint().getFontMetricsInt(), (int) LinkEditActivity.this.nameEditText.getPaint().getTextSize(), false);
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i112, int i122, int i132) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i112, int i122, int i132) {
                        }
                    });
                    this.nameEditText.setCursorVisible(false);
                    this.nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                    this.nameEditText.setGravity(16);
                    this.nameEditText.setHint(LocaleController.getString(R.string.LinkNameHint));
                    EditText editText32 = this.nameEditText;
                    int i112 = Theme.key_windowBackgroundWhiteGrayText;
                    editText32.setHintTextColor(Theme.getColor(i112));
                    this.nameEditText.setLines(1);
                    this.nameEditText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                    this.nameEditText.setSingleLine();
                    EditText editText42 = this.nameEditText;
                    int i122 = Theme.key_windowBackgroundWhiteBlackText;
                    editText42.setTextColor(Theme.getColor(i122));
                    this.nameEditText.setTextSize(1, 16.0f);
                    linearLayout.addView(this.nameEditText, LayoutHelper.createLinear(i6, 50));
                    TextInfoPrivacyCell textInfoPrivacyCell52 = new TextInfoPrivacyCell(context);
                    this.dividerName = textInfoPrivacyCell52;
                    int i132 = R.drawable.greydivider_bottom;
                    int i142 = Theme.key_windowBackgroundGrayShadow;
                    textInfoPrivacyCell52.setBackground(Theme.getThemedDrawableByKey(context, i132, i142));
                    this.dividerName.setText(LocaleController.getString(R.string.LinkNameHelp));
                    linearLayout.addView(this.dividerName);
                    if (this.type == 1) {
                    }
                    sizeNotifierFrameLayout.addView(this.scrollView, LayoutHelper.createFrame(i6, -1.0f));
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    this.buttonLayout = frameLayout2;
                    int i152 = Theme.key_windowBackgroundGray;
                    frameLayout2.setBackgroundColor(getThemedColor(i152));
                    new KeyboardNotifier(sizeNotifierFrameLayout, new Utilities.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda9
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            LinkEditActivity.lambda$createView$11((Integer) obj);
                        }
                    });
                    this.buttonLayout.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 87, 16.0f, 15.0f, 16.0f, 16.0f));
                    sizeNotifierFrameLayout.addView(this.buttonLayout, LayoutHelper.createFrame(i6, i7, 80));
                    HeaderCell headerCell32 = this.timeHeaderCell;
                    int i162 = Theme.key_windowBackgroundWhite;
                    headerCell32.setBackgroundColor(Theme.getColor(i162));
                    this.timeChooseView.setBackgroundColor(Theme.getColor(i162));
                    this.timeEditText.setBackgroundColor(Theme.getColor(i162));
                    this.usesHeaderCell.setBackgroundColor(Theme.getColor(i162));
                    this.usesChooseView.setBackgroundColor(Theme.getColor(i162));
                    this.usesEditText.setBackgroundColor(Theme.getColor(i162));
                    this.nameEditText.setBackgroundColor(Theme.getColor(i162));
                    sizeNotifierFrameLayout.setBackgroundColor(Theme.getColor(i152));
                    this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LinkEditActivity.this.onCreateClicked(view);
                        }
                    });
                    this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
                    this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i132, i142));
                    this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i142));
                    this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
                    this.usesEditText.setTextColor(Theme.getColor(i122));
                    this.usesEditText.setHintTextColor(Theme.getColor(i112));
                    this.timeEditText.setTextColor(Theme.getColor(i122));
                    this.timeEditText.setHintTextColor(Theme.getColor(i112));
                    this.usesEditText.setCursorVisible(false);
                    setInviteToEdit(this.inviteToEdit);
                    sizeNotifierFrameLayout.setClipChildren(false);
                    this.scrollView.setClipChildren(false);
                    linearLayout.setClipChildren(false);
                    return sizeNotifierFrameLayout;
                }
                textView2 = this.buttonTextView;
                i5 = R.string.CreateLink;
                textView2.setText(LocaleController.getString(i5));
                chat = getMessagesController().getChat(Long.valueOf(this.chatId));
                if (chat != null) {
                }
                TextCheckCell textCheckCell3 = new TextCheckCell(context) { // from class: org.telegram.ui.LinkEditActivity.4
                    @Override // org.telegram.ui.Cells.TextCheckCell, android.view.View
                    protected void onDraw(Canvas canvas) {
                        canvas.save();
                        canvas.clipRect(0, 0, getWidth(), getHeight());
                        super.onDraw(canvas);
                        canvas.restore();
                    }
                };
                this.approveCell = textCheckCell3;
                int i92 = Theme.key_windowBackgroundUnchecked;
                textCheckCell3.setBackgroundColor(Theme.getColor(i92));
                this.approveCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
                this.approveCell.setDrawCheckRipple(true);
                this.approveCell.setHeight(56);
                this.approveCell.setTag(Integer.valueOf(i92));
                this.approveCell.setTextAndCheck(LocaleController.getString(R.string.ApproveNewMembers), false, false);
                this.approveCell.setTypeface(AndroidUtilities.bold());
                this.approveCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LinkEditActivity.this.lambda$createView$0(view);
                    }
                });
                linearLayout.addView(this.approveCell, LayoutHelper.createLinear(-1, 56));
                TextInfoPrivacyCell textInfoPrivacyCell6 = new TextInfoPrivacyCell(context);
                this.approveHintCell = textInfoPrivacyCell6;
                textInfoPrivacyCell6.setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                this.approveHintCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescription));
                linearLayout.addView(this.approveHintCell);
                TLRPC.ChatFull chatFull2 = MessagesController.getInstance(this.currentAccount).getChatFull(this.chatId);
                if (this.inviteToEdit == null) {
                    TextCheckCell textCheckCell22 = new TextCheckCell(context);
                    this.subCell = textCheckCell22;
                    int i102 = Theme.key_windowBackgroundWhite;
                    textCheckCell22.setBackgroundColor(Theme.getColor(i102));
                    this.subCell.setDrawCheckRipple(true);
                    this.subCell.setTextAndCheck(LocaleController.getString(R.string.RequireMonthlyFee), false, true);
                    if (this.inviteToEdit != null) {
                    }
                    final Runnable[] runnableArr2 = new Runnable[1];
                    this.subCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LinkEditActivity.this.lambda$createView$3(runnableArr2, view);
                        }
                    });
                    linearLayout.addView(this.subCell, LayoutHelper.createLinear(-1, 48));
                    TextView textView52 = new TextView(context);
                    this.subPriceView = textView52;
                    textView52.setTextSize(1, 16.0f);
                    this.subPriceView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
                    EditTextCell editTextCell2 = new EditTextCell(context, LocaleController.getString(getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceHintTest5Minutes : R.string.RequireMonthlyFeePriceHint), false, false, -1, this.resourceProvider) { // from class: org.telegram.ui.LinkEditActivity.5
                        private boolean ignoreTextChanged;

                        @Override // org.telegram.ui.Cells.EditTextCell
                        protected void onTextChanged(CharSequence charSequence) {
                            super.onTextChanged(charSequence);
                            if (this.ignoreTextChanged) {
                                return;
                            }
                            if (TextUtils.isEmpty(charSequence)) {
                                LinkEditActivity.this.subPriceView.setText("");
                                return;
                            }
                            try {
                                long parseLong = Long.parseLong(charSequence.toString());
                                if (parseLong > LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax) {
                                    this.ignoreTextChanged = true;
                                    parseLong = LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax;
                                    setText(Long.toString(parseLong));
                                    this.ignoreTextChanged = false;
                                }
                                TextView textView63 = LinkEditActivity.this.subPriceView;
                                int i113 = LinkEditActivity.this.getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceTest5Minutes : R.string.RequireMonthlyFeePrice;
                                BillingController billingController = BillingController.getInstance();
                                double d = parseLong;
                                Double.isNaN(d);
                                double d2 = d / 1000.0d;
                                double d3 = MessagesController.getInstance(((BaseFragment) LinkEditActivity.this).currentAccount).starsUsdWithdrawRate1000;
                                Double.isNaN(d3);
                                textView63.setText(LocaleController.formatString(i113, billingController.formatCurrency((long) (d2 * d3), "USD")));
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                    };
                    this.subEditPriceCell = editTextCell2;
                    editTextCell2.editText.setInputType(2);
                    this.subEditPriceCell.editText.setRawInputType(2);
                    this.subEditPriceCell.setBackgroundColor(getThemedColor(i102));
                    this.subEditPriceCell.hideKeyboardOnEnter();
                    this.subEditPriceCell.addView(this.subPriceView, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 19.0f, 0.0f));
                    ImageView leftDrawable2 = this.subEditPriceCell.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate());
                    leftDrawable2.setScaleX(0.83f);
                    leftDrawable2.setScaleY(0.83f);
                    leftDrawable2.setTranslationY(AndroidUtilities.dp(-1.0f));
                    leftDrawable2.setTranslationX(AndroidUtilities.dp(1.0f));
                    i6 = -1;
                    linearLayout.addView(this.subEditPriceCell, LayoutHelper.createLinear(-1, 48));
                    this.subEditPriceCell.setVisibility(8);
                    TextInfoPrivacyCell textInfoPrivacyCell22 = new TextInfoPrivacyCell(context);
                    this.subInfoCell = textInfoPrivacyCell22;
                    textInfoPrivacyCell22.setText(this.inviteToEdit != null ? LocaleController.getString(R.string.RequireMonthlyFeeInfoFrozen) : AndroidUtilities.withLearnMore(LocaleController.getString(R.string.RequireMonthlyFeeInfo), new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            LinkEditActivity.this.lambda$createView$4();
                        }
                    }));
                    i7 = -2;
                    linearLayout.addView(this.subInfoCell, LayoutHelper.createLinear(-1, -2));
                    HeaderCell headerCell42 = new HeaderCell(context);
                    this.timeHeaderCell = headerCell42;
                    headerCell42.setText(LocaleController.getString(R.string.LimitByPeriod));
                    linearLayout.addView(this.timeHeaderCell);
                    SlideChooseView slideChooseView32 = new SlideChooseView(context);
                    this.timeChooseView = slideChooseView32;
                    linearLayout.addView(slideChooseView32);
                    TextView textView622 = new TextView(context);
                    this.timeEditText = textView622;
                    textView622.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                    this.timeEditText.setGravity(16);
                    this.timeEditText.setTextSize(1, 16.0f);
                    this.timeEditText.setHint(LocaleController.getString(R.string.TimeLimitHint));
                    this.timeEditText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LinkEditActivity.this.lambda$createView$6(context, view);
                        }
                    });
                    this.timeChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda6
                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public final void onOptionSelected(int i1122) {
                            LinkEditActivity.this.lambda$createView$7(i1122);
                        }

                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public /* synthetic */ void onTouchEnd() {
                            SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                        }
                    });
                    resetDates();
                    linearLayout.addView(this.timeEditText, LayoutHelper.createLinear(i6, 50));
                    TextInfoPrivacyCell textInfoPrivacyCell322 = new TextInfoPrivacyCell(context);
                    this.divider = textInfoPrivacyCell322;
                    textInfoPrivacyCell322.setText(LocaleController.getString(R.string.TimeLimitHelp));
                    linearLayout.addView(this.divider);
                    HeaderCell headerCell222 = new HeaderCell(context);
                    this.usesHeaderCell = headerCell222;
                    headerCell222.setText(LocaleController.getString(R.string.LimitNumberOfUses));
                    linearLayout.addView(this.usesHeaderCell);
                    SlideChooseView slideChooseView222 = new SlideChooseView(context);
                    this.usesChooseView = slideChooseView222;
                    slideChooseView222.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda7
                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public final void onOptionSelected(int i1122) {
                            LinkEditActivity.this.lambda$createView$8(i1122);
                        }

                        @Override // org.telegram.ui.Components.SlideChooseView.Callback
                        public /* synthetic */ void onTouchEnd() {
                            SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                        }
                    });
                    resetUses();
                    linearLayout.addView(this.usesChooseView);
                    EditText editText52 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.6
                        @Override // android.widget.TextView, android.view.View
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            if (motionEvent.getAction() == 1) {
                                setCursorVisible(true);
                            }
                            return super.onTouchEvent(motionEvent);
                        }
                    };
                    this.usesEditText = editText52;
                    editText52.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                    this.usesEditText.setGravity(16);
                    this.usesEditText.setTextSize(1, 16.0f);
                    this.usesEditText.setHint(LocaleController.getString(R.string.UsesLimitHint));
                    this.usesEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    this.usesEditText.setInputType(2);
                    this.usesEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.7
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            if (LinkEditActivity.this.ignoreSet) {
                                return;
                            }
                            if (editable.toString().equals("0")) {
                                LinkEditActivity.this.usesEditText.setText("");
                                return;
                            }
                            try {
                                int parseInt = Integer.parseInt(editable.toString());
                                if (parseInt > 100000) {
                                    LinkEditActivity.this.resetUses();
                                } else {
                                    LinkEditActivity.this.chooseUses(parseInt);
                                }
                            } catch (NumberFormatException unused) {
                                LinkEditActivity.this.resetUses();
                            }
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i1122, int i1222, int i1322) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i1122, int i1222, int i1322) {
                        }
                    });
                    linearLayout.addView(this.usesEditText, LayoutHelper.createLinear(i6, 50));
                    TextInfoPrivacyCell textInfoPrivacyCell422 = new TextInfoPrivacyCell(context);
                    this.dividerUses = textInfoPrivacyCell422;
                    textInfoPrivacyCell422.setText(LocaleController.getString(R.string.UsesLimitHelp));
                    linearLayout.addView(this.dividerUses);
                    EditText editText222 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.8
                        @Override // android.widget.TextView, android.view.View
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            if (motionEvent.getAction() == 1) {
                                setCursorVisible(true);
                            }
                            return super.onTouchEvent(motionEvent);
                        }
                    };
                    this.nameEditText = editText222;
                    editText222.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.9
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            Emoji.replaceEmoji((CharSequence) editable, LinkEditActivity.this.nameEditText.getPaint().getFontMetricsInt(), (int) LinkEditActivity.this.nameEditText.getPaint().getTextSize(), false);
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i1122, int i1222, int i1322) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i1122, int i1222, int i1322) {
                        }
                    });
                    this.nameEditText.setCursorVisible(false);
                    this.nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                    this.nameEditText.setGravity(16);
                    this.nameEditText.setHint(LocaleController.getString(R.string.LinkNameHint));
                    EditText editText322 = this.nameEditText;
                    int i1122 = Theme.key_windowBackgroundWhiteGrayText;
                    editText322.setHintTextColor(Theme.getColor(i1122));
                    this.nameEditText.setLines(1);
                    this.nameEditText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                    this.nameEditText.setSingleLine();
                    EditText editText422 = this.nameEditText;
                    int i1222 = Theme.key_windowBackgroundWhiteBlackText;
                    editText422.setTextColor(Theme.getColor(i1222));
                    this.nameEditText.setTextSize(1, 16.0f);
                    linearLayout.addView(this.nameEditText, LayoutHelper.createLinear(i6, 50));
                    TextInfoPrivacyCell textInfoPrivacyCell522 = new TextInfoPrivacyCell(context);
                    this.dividerName = textInfoPrivacyCell522;
                    int i1322 = R.drawable.greydivider_bottom;
                    int i1422 = Theme.key_windowBackgroundGrayShadow;
                    textInfoPrivacyCell522.setBackground(Theme.getThemedDrawableByKey(context, i1322, i1422));
                    this.dividerName.setText(LocaleController.getString(R.string.LinkNameHelp));
                    linearLayout.addView(this.dividerName);
                    if (this.type == 1) {
                    }
                    sizeNotifierFrameLayout.addView(this.scrollView, LayoutHelper.createFrame(i6, -1.0f));
                    FrameLayout frameLayout22 = new FrameLayout(context);
                    this.buttonLayout = frameLayout22;
                    int i1522 = Theme.key_windowBackgroundGray;
                    frameLayout22.setBackgroundColor(getThemedColor(i1522));
                    new KeyboardNotifier(sizeNotifierFrameLayout, new Utilities.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda9
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            LinkEditActivity.lambda$createView$11((Integer) obj);
                        }
                    });
                    this.buttonLayout.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 87, 16.0f, 15.0f, 16.0f, 16.0f));
                    sizeNotifierFrameLayout.addView(this.buttonLayout, LayoutHelper.createFrame(i6, i7, 80));
                    HeaderCell headerCell322 = this.timeHeaderCell;
                    int i1622 = Theme.key_windowBackgroundWhite;
                    headerCell322.setBackgroundColor(Theme.getColor(i1622));
                    this.timeChooseView.setBackgroundColor(Theme.getColor(i1622));
                    this.timeEditText.setBackgroundColor(Theme.getColor(i1622));
                    this.usesHeaderCell.setBackgroundColor(Theme.getColor(i1622));
                    this.usesChooseView.setBackgroundColor(Theme.getColor(i1622));
                    this.usesEditText.setBackgroundColor(Theme.getColor(i1622));
                    this.nameEditText.setBackgroundColor(Theme.getColor(i1622));
                    sizeNotifierFrameLayout.setBackgroundColor(Theme.getColor(i1522));
                    this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LinkEditActivity.this.onCreateClicked(view);
                        }
                    });
                    this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
                    this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i1322, i1422));
                    this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i1422));
                    this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
                    this.usesEditText.setTextColor(Theme.getColor(i1222));
                    this.usesEditText.setHintTextColor(Theme.getColor(i1122));
                    this.timeEditText.setTextColor(Theme.getColor(i1222));
                    this.timeEditText.setHintTextColor(Theme.getColor(i1122));
                    this.usesEditText.setCursorVisible(false);
                    setInviteToEdit(this.inviteToEdit);
                    sizeNotifierFrameLayout.setClipChildren(false);
                    this.scrollView.setClipChildren(false);
                    linearLayout.setClipChildren(false);
                    return sizeNotifierFrameLayout;
                }
                TextCheckCell textCheckCell222 = new TextCheckCell(context);
                this.subCell = textCheckCell222;
                int i1022 = Theme.key_windowBackgroundWhite;
                textCheckCell222.setBackgroundColor(Theme.getColor(i1022));
                this.subCell.setDrawCheckRipple(true);
                this.subCell.setTextAndCheck(LocaleController.getString(R.string.RequireMonthlyFee), false, true);
                if (this.inviteToEdit != null) {
                }
                final Runnable[] runnableArr22 = new Runnable[1];
                this.subCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LinkEditActivity.this.lambda$createView$3(runnableArr22, view);
                    }
                });
                linearLayout.addView(this.subCell, LayoutHelper.createLinear(-1, 48));
                TextView textView522 = new TextView(context);
                this.subPriceView = textView522;
                textView522.setTextSize(1, 16.0f);
                this.subPriceView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
                EditTextCell editTextCell22 = new EditTextCell(context, LocaleController.getString(getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceHintTest5Minutes : R.string.RequireMonthlyFeePriceHint), false, false, -1, this.resourceProvider) { // from class: org.telegram.ui.LinkEditActivity.5
                    private boolean ignoreTextChanged;

                    @Override // org.telegram.ui.Cells.EditTextCell
                    protected void onTextChanged(CharSequence charSequence) {
                        super.onTextChanged(charSequence);
                        if (this.ignoreTextChanged) {
                            return;
                        }
                        if (TextUtils.isEmpty(charSequence)) {
                            LinkEditActivity.this.subPriceView.setText("");
                            return;
                        }
                        try {
                            long parseLong = Long.parseLong(charSequence.toString());
                            if (parseLong > LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax) {
                                this.ignoreTextChanged = true;
                                parseLong = LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax;
                                setText(Long.toString(parseLong));
                                this.ignoreTextChanged = false;
                            }
                            TextView textView63 = LinkEditActivity.this.subPriceView;
                            int i113 = LinkEditActivity.this.getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceTest5Minutes : R.string.RequireMonthlyFeePrice;
                            BillingController billingController = BillingController.getInstance();
                            double d = parseLong;
                            Double.isNaN(d);
                            double d2 = d / 1000.0d;
                            double d3 = MessagesController.getInstance(((BaseFragment) LinkEditActivity.this).currentAccount).starsUsdWithdrawRate1000;
                            Double.isNaN(d3);
                            textView63.setText(LocaleController.formatString(i113, billingController.formatCurrency((long) (d2 * d3), "USD")));
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                };
                this.subEditPriceCell = editTextCell22;
                editTextCell22.editText.setInputType(2);
                this.subEditPriceCell.editText.setRawInputType(2);
                this.subEditPriceCell.setBackgroundColor(getThemedColor(i1022));
                this.subEditPriceCell.hideKeyboardOnEnter();
                this.subEditPriceCell.addView(this.subPriceView, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 19.0f, 0.0f));
                ImageView leftDrawable22 = this.subEditPriceCell.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate());
                leftDrawable22.setScaleX(0.83f);
                leftDrawable22.setScaleY(0.83f);
                leftDrawable22.setTranslationY(AndroidUtilities.dp(-1.0f));
                leftDrawable22.setTranslationX(AndroidUtilities.dp(1.0f));
                i6 = -1;
                linearLayout.addView(this.subEditPriceCell, LayoutHelper.createLinear(-1, 48));
                this.subEditPriceCell.setVisibility(8);
                TextInfoPrivacyCell textInfoPrivacyCell222 = new TextInfoPrivacyCell(context);
                this.subInfoCell = textInfoPrivacyCell222;
                textInfoPrivacyCell222.setText(this.inviteToEdit != null ? LocaleController.getString(R.string.RequireMonthlyFeeInfoFrozen) : AndroidUtilities.withLearnMore(LocaleController.getString(R.string.RequireMonthlyFeeInfo), new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        LinkEditActivity.this.lambda$createView$4();
                    }
                }));
                i7 = -2;
                linearLayout.addView(this.subInfoCell, LayoutHelper.createLinear(-1, -2));
                HeaderCell headerCell422 = new HeaderCell(context);
                this.timeHeaderCell = headerCell422;
                headerCell422.setText(LocaleController.getString(R.string.LimitByPeriod));
                linearLayout.addView(this.timeHeaderCell);
                SlideChooseView slideChooseView322 = new SlideChooseView(context);
                this.timeChooseView = slideChooseView322;
                linearLayout.addView(slideChooseView322);
                TextView textView6222 = new TextView(context);
                this.timeEditText = textView6222;
                textView6222.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                this.timeEditText.setGravity(16);
                this.timeEditText.setTextSize(1, 16.0f);
                this.timeEditText.setHint(LocaleController.getString(R.string.TimeLimitHint));
                this.timeEditText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LinkEditActivity.this.lambda$createView$6(context, view);
                    }
                });
                this.timeChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda6
                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public final void onOptionSelected(int i11222) {
                        LinkEditActivity.this.lambda$createView$7(i11222);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public /* synthetic */ void onTouchEnd() {
                        SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                    }
                });
                resetDates();
                linearLayout.addView(this.timeEditText, LayoutHelper.createLinear(i6, 50));
                TextInfoPrivacyCell textInfoPrivacyCell3222 = new TextInfoPrivacyCell(context);
                this.divider = textInfoPrivacyCell3222;
                textInfoPrivacyCell3222.setText(LocaleController.getString(R.string.TimeLimitHelp));
                linearLayout.addView(this.divider);
                HeaderCell headerCell2222 = new HeaderCell(context);
                this.usesHeaderCell = headerCell2222;
                headerCell2222.setText(LocaleController.getString(R.string.LimitNumberOfUses));
                linearLayout.addView(this.usesHeaderCell);
                SlideChooseView slideChooseView2222 = new SlideChooseView(context);
                this.usesChooseView = slideChooseView2222;
                slideChooseView2222.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda7
                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public final void onOptionSelected(int i11222) {
                        LinkEditActivity.this.lambda$createView$8(i11222);
                    }

                    @Override // org.telegram.ui.Components.SlideChooseView.Callback
                    public /* synthetic */ void onTouchEnd() {
                        SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                    }
                });
                resetUses();
                linearLayout.addView(this.usesChooseView);
                EditText editText522 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.6
                    @Override // android.widget.TextView, android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        if (motionEvent.getAction() == 1) {
                            setCursorVisible(true);
                        }
                        return super.onTouchEvent(motionEvent);
                    }
                };
                this.usesEditText = editText522;
                editText522.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                this.usesEditText.setGravity(16);
                this.usesEditText.setTextSize(1, 16.0f);
                this.usesEditText.setHint(LocaleController.getString(R.string.UsesLimitHint));
                this.usesEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                this.usesEditText.setInputType(2);
                this.usesEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.7
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        if (LinkEditActivity.this.ignoreSet) {
                            return;
                        }
                        if (editable.toString().equals("0")) {
                            LinkEditActivity.this.usesEditText.setText("");
                            return;
                        }
                        try {
                            int parseInt = Integer.parseInt(editable.toString());
                            if (parseInt > 100000) {
                                LinkEditActivity.this.resetUses();
                            } else {
                                LinkEditActivity.this.chooseUses(parseInt);
                            }
                        } catch (NumberFormatException unused) {
                            LinkEditActivity.this.resetUses();
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i11222, int i12222, int i13222) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i11222, int i12222, int i13222) {
                    }
                });
                linearLayout.addView(this.usesEditText, LayoutHelper.createLinear(i6, 50));
                TextInfoPrivacyCell textInfoPrivacyCell4222 = new TextInfoPrivacyCell(context);
                this.dividerUses = textInfoPrivacyCell4222;
                textInfoPrivacyCell4222.setText(LocaleController.getString(R.string.UsesLimitHelp));
                linearLayout.addView(this.dividerUses);
                EditText editText2222 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.8
                    @Override // android.widget.TextView, android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        if (motionEvent.getAction() == 1) {
                            setCursorVisible(true);
                        }
                        return super.onTouchEvent(motionEvent);
                    }
                };
                this.nameEditText = editText2222;
                editText2222.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.9
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        Emoji.replaceEmoji((CharSequence) editable, LinkEditActivity.this.nameEditText.getPaint().getFontMetricsInt(), (int) LinkEditActivity.this.nameEditText.getPaint().getTextSize(), false);
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i11222, int i12222, int i13222) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i11222, int i12222, int i13222) {
                    }
                });
                this.nameEditText.setCursorVisible(false);
                this.nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                this.nameEditText.setGravity(16);
                this.nameEditText.setHint(LocaleController.getString(R.string.LinkNameHint));
                EditText editText3222 = this.nameEditText;
                int i11222 = Theme.key_windowBackgroundWhiteGrayText;
                editText3222.setHintTextColor(Theme.getColor(i11222));
                this.nameEditText.setLines(1);
                this.nameEditText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
                this.nameEditText.setSingleLine();
                EditText editText4222 = this.nameEditText;
                int i12222 = Theme.key_windowBackgroundWhiteBlackText;
                editText4222.setTextColor(Theme.getColor(i12222));
                this.nameEditText.setTextSize(1, 16.0f);
                linearLayout.addView(this.nameEditText, LayoutHelper.createLinear(i6, 50));
                TextInfoPrivacyCell textInfoPrivacyCell5222 = new TextInfoPrivacyCell(context);
                this.dividerName = textInfoPrivacyCell5222;
                int i13222 = R.drawable.greydivider_bottom;
                int i14222 = Theme.key_windowBackgroundGrayShadow;
                textInfoPrivacyCell5222.setBackground(Theme.getThemedDrawableByKey(context, i13222, i14222));
                this.dividerName.setText(LocaleController.getString(R.string.LinkNameHelp));
                linearLayout.addView(this.dividerName);
                if (this.type == 1) {
                }
                sizeNotifierFrameLayout.addView(this.scrollView, LayoutHelper.createFrame(i6, -1.0f));
                FrameLayout frameLayout222 = new FrameLayout(context);
                this.buttonLayout = frameLayout222;
                int i15222 = Theme.key_windowBackgroundGray;
                frameLayout222.setBackgroundColor(getThemedColor(i15222));
                new KeyboardNotifier(sizeNotifierFrameLayout, new Utilities.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda9
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        LinkEditActivity.lambda$createView$11((Integer) obj);
                    }
                });
                this.buttonLayout.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 87, 16.0f, 15.0f, 16.0f, 16.0f));
                sizeNotifierFrameLayout.addView(this.buttonLayout, LayoutHelper.createFrame(i6, i7, 80));
                HeaderCell headerCell3222 = this.timeHeaderCell;
                int i16222 = Theme.key_windowBackgroundWhite;
                headerCell3222.setBackgroundColor(Theme.getColor(i16222));
                this.timeChooseView.setBackgroundColor(Theme.getColor(i16222));
                this.timeEditText.setBackgroundColor(Theme.getColor(i16222));
                this.usesHeaderCell.setBackgroundColor(Theme.getColor(i16222));
                this.usesChooseView.setBackgroundColor(Theme.getColor(i16222));
                this.usesEditText.setBackgroundColor(Theme.getColor(i16222));
                this.nameEditText.setBackgroundColor(Theme.getColor(i16222));
                sizeNotifierFrameLayout.setBackgroundColor(Theme.getColor(i15222));
                this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LinkEditActivity.this.onCreateClicked(view);
                    }
                });
                this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
                this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i13222, i14222));
                this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i14222));
                this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
                this.usesEditText.setTextColor(Theme.getColor(i12222));
                this.usesEditText.setHintTextColor(Theme.getColor(i11222));
                this.timeEditText.setTextColor(Theme.getColor(i12222));
                this.timeEditText.setHintTextColor(Theme.getColor(i11222));
                this.usesEditText.setCursorVisible(false);
                setInviteToEdit(this.inviteToEdit);
                sizeNotifierFrameLayout.setClipChildren(false);
                this.scrollView.setClipChildren(false);
                linearLayout.setClipChildren(false);
                return sizeNotifierFrameLayout;
            }
            textView = this.createTextView;
            i3 = R.string.CreateLinkHeader;
            textView.setText(LocaleController.getString(i3));
            this.createTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
            this.createTextView.setTextSize(1, 14.0f);
            this.createTextView.setTypeface(AndroidUtilities.bold());
            this.createTextView.setPadding(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f));
            this.actionBar.addView(this.createTextView, LayoutHelper.createFrame(-2, -2.0f, 8388629, 0.0f, this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight / AndroidUtilities.dp(2.0f) : 0, 0.0f, 0.0f));
            this.scrollView = new ScrollView(context);
            SizeNotifierFrameLayout sizeNotifierFrameLayout2 = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.LinkEditActivity.2
                @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    super.dispatchDraw(canvas);
                    LinkEditActivity linkEditActivity = LinkEditActivity.this;
                    if (linkEditActivity.scrollToEnd) {
                        linkEditActivity.scrollToEnd = false;
                        linkEditActivity.scrollView.smoothScrollTo(0, Math.max(0, LinkEditActivity.this.scrollView.getChildAt(0).getMeasuredHeight() - LinkEditActivity.this.scrollView.getMeasuredHeight()));
                    } else if (linkEditActivity.scrollToStart) {
                        linkEditActivity.scrollToStart = false;
                        linkEditActivity.scrollView.smoothScrollTo(0, 0);
                    }
                }

                @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
                protected void onLayout(boolean z, int i93, int i103, int i113, int i123) {
                    int scrollY = LinkEditActivity.this.scrollView.getScrollY();
                    super.onLayout(z, i93, i103, i113, i123);
                    if (scrollY != LinkEditActivity.this.scrollView.getScrollY()) {
                        LinkEditActivity linkEditActivity = LinkEditActivity.this;
                        if (linkEditActivity.scrollToEnd) {
                            return;
                        }
                        linkEditActivity.scrollView.setTranslationY(LinkEditActivity.this.scrollView.getScrollY() - scrollY);
                        LinkEditActivity.this.scrollView.animate().cancel();
                        LinkEditActivity.this.scrollView.animate().translationY(0.0f).setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator).start();
                    }
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i93, int i103) {
                    super.onMeasure(i93, i103);
                    measureKeyboardHeight();
                    int i113 = this.keyboardHeight;
                    if (i113 != 0 && i113 < AndroidUtilities.dp(20.0f)) {
                        LinkEditActivity.this.usesEditText.clearFocus();
                        LinkEditActivity.this.nameEditText.clearFocus();
                    }
                    LinkEditActivity.this.buttonLayout.setVisibility(this.keyboardHeight > AndroidUtilities.dp(20.0f) ? 8 : 0);
                }
            };
            this.fragmentView = sizeNotifierFrameLayout2;
            LinearLayout linearLayout2 = new LinearLayout(context) { // from class: org.telegram.ui.LinkEditActivity.3
                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    super.dispatchDraw(canvas);
                    LinkEditActivity.this.firstLayout = false;
                }

                @Override // android.widget.LinearLayout, android.view.View
                protected void onMeasure(int i93, int i103) {
                    super.onMeasure(i93, i103);
                }
            };
            LayoutTransition layoutTransition2 = new LayoutTransition();
            layoutTransition2.setDuration(420L);
            CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.EASE_OUT_QUINT;
            layoutTransition2.setInterpolator(2, cubicBezierInterpolator2);
            layoutTransition2.setInterpolator(0, cubicBezierInterpolator2);
            layoutTransition2.setInterpolator(4, cubicBezierInterpolator2);
            layoutTransition2.setInterpolator(1, cubicBezierInterpolator2);
            layoutTransition2.setInterpolator(3, cubicBezierInterpolator2);
            linearLayout2.setLayoutTransition(layoutTransition2);
            linearLayout2.setOrientation(1);
            linearLayout2.setPadding(0, 0, 0, AndroidUtilities.dp(79.0f));
            this.scrollView.addView(linearLayout2);
            TextView textView42 = new TextView(context);
            this.buttonTextView = textView42;
            textView42.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
            this.buttonTextView.setGravity(17);
            this.buttonTextView.setTextSize(1, 14.0f);
            this.buttonTextView.setTypeface(AndroidUtilities.bold());
            i4 = this.type;
            if (i4 != 0) {
            }
            textView2.setText(LocaleController.getString(i5));
            chat = getMessagesController().getChat(Long.valueOf(this.chatId));
            if (chat != null) {
            }
            TextCheckCell textCheckCell32 = new TextCheckCell(context) { // from class: org.telegram.ui.LinkEditActivity.4
                @Override // org.telegram.ui.Cells.TextCheckCell, android.view.View
                protected void onDraw(Canvas canvas) {
                    canvas.save();
                    canvas.clipRect(0, 0, getWidth(), getHeight());
                    super.onDraw(canvas);
                    canvas.restore();
                }
            };
            this.approveCell = textCheckCell32;
            int i922 = Theme.key_windowBackgroundUnchecked;
            textCheckCell32.setBackgroundColor(Theme.getColor(i922));
            this.approveCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
            this.approveCell.setDrawCheckRipple(true);
            this.approveCell.setHeight(56);
            this.approveCell.setTag(Integer.valueOf(i922));
            this.approveCell.setTextAndCheck(LocaleController.getString(R.string.ApproveNewMembers), false, false);
            this.approveCell.setTypeface(AndroidUtilities.bold());
            this.approveCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LinkEditActivity.this.lambda$createView$0(view);
                }
            });
            linearLayout2.addView(this.approveCell, LayoutHelper.createLinear(-1, 56));
            TextInfoPrivacyCell textInfoPrivacyCell62 = new TextInfoPrivacyCell(context);
            this.approveHintCell = textInfoPrivacyCell62;
            textInfoPrivacyCell62.setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
            this.approveHintCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescription));
            linearLayout2.addView(this.approveHintCell);
            TLRPC.ChatFull chatFull22 = MessagesController.getInstance(this.currentAccount).getChatFull(this.chatId);
            if (this.inviteToEdit == null) {
            }
            TextCheckCell textCheckCell2222 = new TextCheckCell(context);
            this.subCell = textCheckCell2222;
            int i10222 = Theme.key_windowBackgroundWhite;
            textCheckCell2222.setBackgroundColor(Theme.getColor(i10222));
            this.subCell.setDrawCheckRipple(true);
            this.subCell.setTextAndCheck(LocaleController.getString(R.string.RequireMonthlyFee), false, true);
            if (this.inviteToEdit != null) {
            }
            final Runnable[] runnableArr222 = new Runnable[1];
            this.subCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LinkEditActivity.this.lambda$createView$3(runnableArr222, view);
                }
            });
            linearLayout2.addView(this.subCell, LayoutHelper.createLinear(-1, 48));
            TextView textView5222 = new TextView(context);
            this.subPriceView = textView5222;
            textView5222.setTextSize(1, 16.0f);
            this.subPriceView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
            EditTextCell editTextCell222 = new EditTextCell(context, LocaleController.getString(getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceHintTest5Minutes : R.string.RequireMonthlyFeePriceHint), false, false, -1, this.resourceProvider) { // from class: org.telegram.ui.LinkEditActivity.5
                private boolean ignoreTextChanged;

                @Override // org.telegram.ui.Cells.EditTextCell
                protected void onTextChanged(CharSequence charSequence) {
                    super.onTextChanged(charSequence);
                    if (this.ignoreTextChanged) {
                        return;
                    }
                    if (TextUtils.isEmpty(charSequence)) {
                        LinkEditActivity.this.subPriceView.setText("");
                        return;
                    }
                    try {
                        long parseLong = Long.parseLong(charSequence.toString());
                        if (parseLong > LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax) {
                            this.ignoreTextChanged = true;
                            parseLong = LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax;
                            setText(Long.toString(parseLong));
                            this.ignoreTextChanged = false;
                        }
                        TextView textView63 = LinkEditActivity.this.subPriceView;
                        int i113 = LinkEditActivity.this.getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceTest5Minutes : R.string.RequireMonthlyFeePrice;
                        BillingController billingController = BillingController.getInstance();
                        double d = parseLong;
                        Double.isNaN(d);
                        double d2 = d / 1000.0d;
                        double d3 = MessagesController.getInstance(((BaseFragment) LinkEditActivity.this).currentAccount).starsUsdWithdrawRate1000;
                        Double.isNaN(d3);
                        textView63.setText(LocaleController.formatString(i113, billingController.formatCurrency((long) (d2 * d3), "USD")));
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            };
            this.subEditPriceCell = editTextCell222;
            editTextCell222.editText.setInputType(2);
            this.subEditPriceCell.editText.setRawInputType(2);
            this.subEditPriceCell.setBackgroundColor(getThemedColor(i10222));
            this.subEditPriceCell.hideKeyboardOnEnter();
            this.subEditPriceCell.addView(this.subPriceView, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 19.0f, 0.0f));
            ImageView leftDrawable222 = this.subEditPriceCell.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate());
            leftDrawable222.setScaleX(0.83f);
            leftDrawable222.setScaleY(0.83f);
            leftDrawable222.setTranslationY(AndroidUtilities.dp(-1.0f));
            leftDrawable222.setTranslationX(AndroidUtilities.dp(1.0f));
            i6 = -1;
            linearLayout2.addView(this.subEditPriceCell, LayoutHelper.createLinear(-1, 48));
            this.subEditPriceCell.setVisibility(8);
            TextInfoPrivacyCell textInfoPrivacyCell2222 = new TextInfoPrivacyCell(context);
            this.subInfoCell = textInfoPrivacyCell2222;
            textInfoPrivacyCell2222.setText(this.inviteToEdit != null ? LocaleController.getString(R.string.RequireMonthlyFeeInfoFrozen) : AndroidUtilities.withLearnMore(LocaleController.getString(R.string.RequireMonthlyFeeInfo), new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    LinkEditActivity.this.lambda$createView$4();
                }
            }));
            i7 = -2;
            linearLayout2.addView(this.subInfoCell, LayoutHelper.createLinear(-1, -2));
            HeaderCell headerCell4222 = new HeaderCell(context);
            this.timeHeaderCell = headerCell4222;
            headerCell4222.setText(LocaleController.getString(R.string.LimitByPeriod));
            linearLayout2.addView(this.timeHeaderCell);
            SlideChooseView slideChooseView3222 = new SlideChooseView(context);
            this.timeChooseView = slideChooseView3222;
            linearLayout2.addView(slideChooseView3222);
            TextView textView62222 = new TextView(context);
            this.timeEditText = textView62222;
            textView62222.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
            this.timeEditText.setGravity(16);
            this.timeEditText.setTextSize(1, 16.0f);
            this.timeEditText.setHint(LocaleController.getString(R.string.TimeLimitHint));
            this.timeEditText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LinkEditActivity.this.lambda$createView$6(context, view);
                }
            });
            this.timeChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda6
                @Override // org.telegram.ui.Components.SlideChooseView.Callback
                public final void onOptionSelected(int i112222) {
                    LinkEditActivity.this.lambda$createView$7(i112222);
                }

                @Override // org.telegram.ui.Components.SlideChooseView.Callback
                public /* synthetic */ void onTouchEnd() {
                    SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                }
            });
            resetDates();
            linearLayout2.addView(this.timeEditText, LayoutHelper.createLinear(i6, 50));
            TextInfoPrivacyCell textInfoPrivacyCell32222 = new TextInfoPrivacyCell(context);
            this.divider = textInfoPrivacyCell32222;
            textInfoPrivacyCell32222.setText(LocaleController.getString(R.string.TimeLimitHelp));
            linearLayout2.addView(this.divider);
            HeaderCell headerCell22222 = new HeaderCell(context);
            this.usesHeaderCell = headerCell22222;
            headerCell22222.setText(LocaleController.getString(R.string.LimitNumberOfUses));
            linearLayout2.addView(this.usesHeaderCell);
            SlideChooseView slideChooseView22222 = new SlideChooseView(context);
            this.usesChooseView = slideChooseView22222;
            slideChooseView22222.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda7
                @Override // org.telegram.ui.Components.SlideChooseView.Callback
                public final void onOptionSelected(int i112222) {
                    LinkEditActivity.this.lambda$createView$8(i112222);
                }

                @Override // org.telegram.ui.Components.SlideChooseView.Callback
                public /* synthetic */ void onTouchEnd() {
                    SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
                }
            });
            resetUses();
            linearLayout2.addView(this.usesChooseView);
            EditText editText5222 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.6
                @Override // android.widget.TextView, android.view.View
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 1) {
                        setCursorVisible(true);
                    }
                    return super.onTouchEvent(motionEvent);
                }
            };
            this.usesEditText = editText5222;
            editText5222.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
            this.usesEditText.setGravity(16);
            this.usesEditText.setTextSize(1, 16.0f);
            this.usesEditText.setHint(LocaleController.getString(R.string.UsesLimitHint));
            this.usesEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            this.usesEditText.setInputType(2);
            this.usesEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.7
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    if (LinkEditActivity.this.ignoreSet) {
                        return;
                    }
                    if (editable.toString().equals("0")) {
                        LinkEditActivity.this.usesEditText.setText("");
                        return;
                    }
                    try {
                        int parseInt = Integer.parseInt(editable.toString());
                        if (parseInt > 100000) {
                            LinkEditActivity.this.resetUses();
                        } else {
                            LinkEditActivity.this.chooseUses(parseInt);
                        }
                    } catch (NumberFormatException unused) {
                        LinkEditActivity.this.resetUses();
                    }
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i112222, int i122222, int i132222) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i112222, int i122222, int i132222) {
                }
            });
            linearLayout2.addView(this.usesEditText, LayoutHelper.createLinear(i6, 50));
            TextInfoPrivacyCell textInfoPrivacyCell42222 = new TextInfoPrivacyCell(context);
            this.dividerUses = textInfoPrivacyCell42222;
            textInfoPrivacyCell42222.setText(LocaleController.getString(R.string.UsesLimitHelp));
            linearLayout2.addView(this.dividerUses);
            EditText editText22222 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.8
                @Override // android.widget.TextView, android.view.View
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 1) {
                        setCursorVisible(true);
                    }
                    return super.onTouchEvent(motionEvent);
                }
            };
            this.nameEditText = editText22222;
            editText22222.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.9
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    Emoji.replaceEmoji((CharSequence) editable, LinkEditActivity.this.nameEditText.getPaint().getFontMetricsInt(), (int) LinkEditActivity.this.nameEditText.getPaint().getTextSize(), false);
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i112222, int i122222, int i132222) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i112222, int i122222, int i132222) {
                }
            });
            this.nameEditText.setCursorVisible(false);
            this.nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
            this.nameEditText.setGravity(16);
            this.nameEditText.setHint(LocaleController.getString(R.string.LinkNameHint));
            EditText editText32222 = this.nameEditText;
            int i112222 = Theme.key_windowBackgroundWhiteGrayText;
            editText32222.setHintTextColor(Theme.getColor(i112222));
            this.nameEditText.setLines(1);
            this.nameEditText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
            this.nameEditText.setSingleLine();
            EditText editText42222 = this.nameEditText;
            int i122222 = Theme.key_windowBackgroundWhiteBlackText;
            editText42222.setTextColor(Theme.getColor(i122222));
            this.nameEditText.setTextSize(1, 16.0f);
            linearLayout2.addView(this.nameEditText, LayoutHelper.createLinear(i6, 50));
            TextInfoPrivacyCell textInfoPrivacyCell52222 = new TextInfoPrivacyCell(context);
            this.dividerName = textInfoPrivacyCell52222;
            int i132222 = R.drawable.greydivider_bottom;
            int i142222 = Theme.key_windowBackgroundGrayShadow;
            textInfoPrivacyCell52222.setBackground(Theme.getThemedDrawableByKey(context, i132222, i142222));
            this.dividerName.setText(LocaleController.getString(R.string.LinkNameHelp));
            linearLayout2.addView(this.dividerName);
            if (this.type == 1) {
            }
            sizeNotifierFrameLayout2.addView(this.scrollView, LayoutHelper.createFrame(i6, -1.0f));
            FrameLayout frameLayout2222 = new FrameLayout(context);
            this.buttonLayout = frameLayout2222;
            int i152222 = Theme.key_windowBackgroundGray;
            frameLayout2222.setBackgroundColor(getThemedColor(i152222));
            new KeyboardNotifier(sizeNotifierFrameLayout2, new Utilities.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda9
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    LinkEditActivity.lambda$createView$11((Integer) obj);
                }
            });
            this.buttonLayout.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 87, 16.0f, 15.0f, 16.0f, 16.0f));
            sizeNotifierFrameLayout2.addView(this.buttonLayout, LayoutHelper.createFrame(i6, i7, 80));
            HeaderCell headerCell32222 = this.timeHeaderCell;
            int i162222 = Theme.key_windowBackgroundWhite;
            headerCell32222.setBackgroundColor(Theme.getColor(i162222));
            this.timeChooseView.setBackgroundColor(Theme.getColor(i162222));
            this.timeEditText.setBackgroundColor(Theme.getColor(i162222));
            this.usesHeaderCell.setBackgroundColor(Theme.getColor(i162222));
            this.usesChooseView.setBackgroundColor(Theme.getColor(i162222));
            this.usesEditText.setBackgroundColor(Theme.getColor(i162222));
            this.nameEditText.setBackgroundColor(Theme.getColor(i162222));
            sizeNotifierFrameLayout2.setBackgroundColor(Theme.getColor(i152222));
            this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LinkEditActivity.this.onCreateClicked(view);
                }
            });
            this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
            this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i132222, i142222));
            this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i142222));
            this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
            this.usesEditText.setTextColor(Theme.getColor(i122222));
            this.usesEditText.setHintTextColor(Theme.getColor(i112222));
            this.timeEditText.setTextColor(Theme.getColor(i122222));
            this.timeEditText.setHintTextColor(Theme.getColor(i112222));
            this.usesEditText.setCursorVisible(false);
            setInviteToEdit(this.inviteToEdit);
            sizeNotifierFrameLayout2.setClipChildren(false);
            this.scrollView.setClipChildren(false);
            linearLayout2.setClipChildren(false);
            return sizeNotifierFrameLayout2;
        }
        actionBar = this.actionBar;
        i = R.string.NewLink;
        actionBar.setTitle(LocaleController.getString(i));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LinkEditActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i93) {
                if (i93 == -1) {
                    LinkEditActivity.this.lambda$onBackPressed$321();
                    AndroidUtilities.hideKeyboard(LinkEditActivity.this.usesEditText);
                }
            }
        });
        TextView textView32 = new TextView(context);
        this.createTextView = textView32;
        textView32.setEllipsize(TextUtils.TruncateAt.END);
        this.createTextView.setGravity(16);
        this.createTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LinkEditActivity.this.onCreateClicked(view);
            }
        });
        this.createTextView.setSingleLine();
        i2 = this.type;
        if (i2 == 0) {
        }
        textView.setText(LocaleController.getString(i3));
        this.createTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
        this.createTextView.setTextSize(1, 14.0f);
        this.createTextView.setTypeface(AndroidUtilities.bold());
        this.createTextView.setPadding(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(8.0f));
        this.actionBar.addView(this.createTextView, LayoutHelper.createFrame(-2, -2.0f, 8388629, 0.0f, this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight / AndroidUtilities.dp(2.0f) : 0, 0.0f, 0.0f));
        this.scrollView = new ScrollView(context);
        SizeNotifierFrameLayout sizeNotifierFrameLayout22 = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.LinkEditActivity.2
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                LinkEditActivity linkEditActivity = LinkEditActivity.this;
                if (linkEditActivity.scrollToEnd) {
                    linkEditActivity.scrollToEnd = false;
                    linkEditActivity.scrollView.smoothScrollTo(0, Math.max(0, LinkEditActivity.this.scrollView.getChildAt(0).getMeasuredHeight() - LinkEditActivity.this.scrollView.getMeasuredHeight()));
                } else if (linkEditActivity.scrollToStart) {
                    linkEditActivity.scrollToStart = false;
                    linkEditActivity.scrollView.smoothScrollTo(0, 0);
                }
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i93, int i103, int i113, int i123) {
                int scrollY = LinkEditActivity.this.scrollView.getScrollY();
                super.onLayout(z, i93, i103, i113, i123);
                if (scrollY != LinkEditActivity.this.scrollView.getScrollY()) {
                    LinkEditActivity linkEditActivity = LinkEditActivity.this;
                    if (linkEditActivity.scrollToEnd) {
                        return;
                    }
                    linkEditActivity.scrollView.setTranslationY(LinkEditActivity.this.scrollView.getScrollY() - scrollY);
                    LinkEditActivity.this.scrollView.animate().cancel();
                    LinkEditActivity.this.scrollView.animate().translationY(0.0f).setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator).start();
                }
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i93, int i103) {
                super.onMeasure(i93, i103);
                measureKeyboardHeight();
                int i113 = this.keyboardHeight;
                if (i113 != 0 && i113 < AndroidUtilities.dp(20.0f)) {
                    LinkEditActivity.this.usesEditText.clearFocus();
                    LinkEditActivity.this.nameEditText.clearFocus();
                }
                LinkEditActivity.this.buttonLayout.setVisibility(this.keyboardHeight > AndroidUtilities.dp(20.0f) ? 8 : 0);
            }
        };
        this.fragmentView = sizeNotifierFrameLayout22;
        LinearLayout linearLayout22 = new LinearLayout(context) { // from class: org.telegram.ui.LinkEditActivity.3
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                LinkEditActivity.this.firstLayout = false;
            }

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i93, int i103) {
                super.onMeasure(i93, i103);
            }
        };
        LayoutTransition layoutTransition22 = new LayoutTransition();
        layoutTransition22.setDuration(420L);
        CubicBezierInterpolator cubicBezierInterpolator22 = CubicBezierInterpolator.EASE_OUT_QUINT;
        layoutTransition22.setInterpolator(2, cubicBezierInterpolator22);
        layoutTransition22.setInterpolator(0, cubicBezierInterpolator22);
        layoutTransition22.setInterpolator(4, cubicBezierInterpolator22);
        layoutTransition22.setInterpolator(1, cubicBezierInterpolator22);
        layoutTransition22.setInterpolator(3, cubicBezierInterpolator22);
        linearLayout22.setLayoutTransition(layoutTransition22);
        linearLayout22.setOrientation(1);
        linearLayout22.setPadding(0, 0, 0, AndroidUtilities.dp(79.0f));
        this.scrollView.addView(linearLayout22);
        TextView textView422 = new TextView(context);
        this.buttonTextView = textView422;
        textView422.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        this.buttonTextView.setGravity(17);
        this.buttonTextView.setTextSize(1, 14.0f);
        this.buttonTextView.setTypeface(AndroidUtilities.bold());
        i4 = this.type;
        if (i4 != 0) {
        }
        textView2.setText(LocaleController.getString(i5));
        chat = getMessagesController().getChat(Long.valueOf(this.chatId));
        if (chat != null) {
        }
        TextCheckCell textCheckCell322 = new TextCheckCell(context) { // from class: org.telegram.ui.LinkEditActivity.4
            @Override // org.telegram.ui.Cells.TextCheckCell, android.view.View
            protected void onDraw(Canvas canvas) {
                canvas.save();
                canvas.clipRect(0, 0, getWidth(), getHeight());
                super.onDraw(canvas);
                canvas.restore();
            }
        };
        this.approveCell = textCheckCell322;
        int i9222 = Theme.key_windowBackgroundUnchecked;
        textCheckCell322.setBackgroundColor(Theme.getColor(i9222));
        this.approveCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
        this.approveCell.setDrawCheckRipple(true);
        this.approveCell.setHeight(56);
        this.approveCell.setTag(Integer.valueOf(i9222));
        this.approveCell.setTextAndCheck(LocaleController.getString(R.string.ApproveNewMembers), false, false);
        this.approveCell.setTypeface(AndroidUtilities.bold());
        this.approveCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LinkEditActivity.this.lambda$createView$0(view);
            }
        });
        linearLayout22.addView(this.approveCell, LayoutHelper.createLinear(-1, 56));
        TextInfoPrivacyCell textInfoPrivacyCell622 = new TextInfoPrivacyCell(context);
        this.approveHintCell = textInfoPrivacyCell622;
        textInfoPrivacyCell622.setBackground(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
        this.approveHintCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescription));
        linearLayout22.addView(this.approveHintCell);
        TLRPC.ChatFull chatFull222 = MessagesController.getInstance(this.currentAccount).getChatFull(this.chatId);
        if (this.inviteToEdit == null) {
        }
        TextCheckCell textCheckCell22222 = new TextCheckCell(context);
        this.subCell = textCheckCell22222;
        int i102222 = Theme.key_windowBackgroundWhite;
        textCheckCell22222.setBackgroundColor(Theme.getColor(i102222));
        this.subCell.setDrawCheckRipple(true);
        this.subCell.setTextAndCheck(LocaleController.getString(R.string.RequireMonthlyFee), false, true);
        if (this.inviteToEdit != null) {
        }
        final Runnable[] runnableArr2222 = new Runnable[1];
        this.subCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LinkEditActivity.this.lambda$createView$3(runnableArr2222, view);
            }
        });
        linearLayout22.addView(this.subCell, LayoutHelper.createLinear(-1, 48));
        TextView textView52222 = new TextView(context);
        this.subPriceView = textView52222;
        textView52222.setTextSize(1, 16.0f);
        this.subPriceView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        EditTextCell editTextCell2222 = new EditTextCell(context, LocaleController.getString(getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceHintTest5Minutes : R.string.RequireMonthlyFeePriceHint), false, false, -1, this.resourceProvider) { // from class: org.telegram.ui.LinkEditActivity.5
            private boolean ignoreTextChanged;

            @Override // org.telegram.ui.Cells.EditTextCell
            protected void onTextChanged(CharSequence charSequence) {
                super.onTextChanged(charSequence);
                if (this.ignoreTextChanged) {
                    return;
                }
                if (TextUtils.isEmpty(charSequence)) {
                    LinkEditActivity.this.subPriceView.setText("");
                    return;
                }
                try {
                    long parseLong = Long.parseLong(charSequence.toString());
                    if (parseLong > LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax) {
                        this.ignoreTextChanged = true;
                        parseLong = LinkEditActivity.this.getMessagesController().starsSubscriptionAmountMax;
                        setText(Long.toString(parseLong));
                        this.ignoreTextChanged = false;
                    }
                    TextView textView63 = LinkEditActivity.this.subPriceView;
                    int i113 = LinkEditActivity.this.getConnectionsManager().isTestBackend() ? R.string.RequireMonthlyFeePriceTest5Minutes : R.string.RequireMonthlyFeePrice;
                    BillingController billingController = BillingController.getInstance();
                    double d = parseLong;
                    Double.isNaN(d);
                    double d2 = d / 1000.0d;
                    double d3 = MessagesController.getInstance(((BaseFragment) LinkEditActivity.this).currentAccount).starsUsdWithdrawRate1000;
                    Double.isNaN(d3);
                    textView63.setText(LocaleController.formatString(i113, billingController.formatCurrency((long) (d2 * d3), "USD")));
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        };
        this.subEditPriceCell = editTextCell2222;
        editTextCell2222.editText.setInputType(2);
        this.subEditPriceCell.editText.setRawInputType(2);
        this.subEditPriceCell.setBackgroundColor(getThemedColor(i102222));
        this.subEditPriceCell.hideKeyboardOnEnter();
        this.subEditPriceCell.addView(this.subPriceView, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 19.0f, 0.0f));
        ImageView leftDrawable2222 = this.subEditPriceCell.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate());
        leftDrawable2222.setScaleX(0.83f);
        leftDrawable2222.setScaleY(0.83f);
        leftDrawable2222.setTranslationY(AndroidUtilities.dp(-1.0f));
        leftDrawable2222.setTranslationX(AndroidUtilities.dp(1.0f));
        i6 = -1;
        linearLayout22.addView(this.subEditPriceCell, LayoutHelper.createLinear(-1, 48));
        this.subEditPriceCell.setVisibility(8);
        TextInfoPrivacyCell textInfoPrivacyCell22222 = new TextInfoPrivacyCell(context);
        this.subInfoCell = textInfoPrivacyCell22222;
        textInfoPrivacyCell22222.setText(this.inviteToEdit != null ? LocaleController.getString(R.string.RequireMonthlyFeeInfoFrozen) : AndroidUtilities.withLearnMore(LocaleController.getString(R.string.RequireMonthlyFeeInfo), new Runnable() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                LinkEditActivity.this.lambda$createView$4();
            }
        }));
        i7 = -2;
        linearLayout22.addView(this.subInfoCell, LayoutHelper.createLinear(-1, -2));
        HeaderCell headerCell42222 = new HeaderCell(context);
        this.timeHeaderCell = headerCell42222;
        headerCell42222.setText(LocaleController.getString(R.string.LimitByPeriod));
        linearLayout22.addView(this.timeHeaderCell);
        SlideChooseView slideChooseView32222 = new SlideChooseView(context);
        this.timeChooseView = slideChooseView32222;
        linearLayout22.addView(slideChooseView32222);
        TextView textView622222 = new TextView(context);
        this.timeEditText = textView622222;
        textView622222.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
        this.timeEditText.setGravity(16);
        this.timeEditText.setTextSize(1, 16.0f);
        this.timeEditText.setHint(LocaleController.getString(R.string.TimeLimitHint));
        this.timeEditText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LinkEditActivity.this.lambda$createView$6(context, view);
            }
        });
        this.timeChooseView.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public final void onOptionSelected(int i1122222) {
                LinkEditActivity.this.lambda$createView$7(i1122222);
            }

            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public /* synthetic */ void onTouchEnd() {
                SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
            }
        });
        resetDates();
        linearLayout22.addView(this.timeEditText, LayoutHelper.createLinear(i6, 50));
        TextInfoPrivacyCell textInfoPrivacyCell322222 = new TextInfoPrivacyCell(context);
        this.divider = textInfoPrivacyCell322222;
        textInfoPrivacyCell322222.setText(LocaleController.getString(R.string.TimeLimitHelp));
        linearLayout22.addView(this.divider);
        HeaderCell headerCell222222 = new HeaderCell(context);
        this.usesHeaderCell = headerCell222222;
        headerCell222222.setText(LocaleController.getString(R.string.LimitNumberOfUses));
        linearLayout22.addView(this.usesHeaderCell);
        SlideChooseView slideChooseView222222 = new SlideChooseView(context);
        this.usesChooseView = slideChooseView222222;
        slideChooseView222222.setCallback(new SlideChooseView.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public final void onOptionSelected(int i1122222) {
                LinkEditActivity.this.lambda$createView$8(i1122222);
            }

            @Override // org.telegram.ui.Components.SlideChooseView.Callback
            public /* synthetic */ void onTouchEnd() {
                SlideChooseView.Callback.-CC.$default$onTouchEnd(this);
            }
        });
        resetUses();
        linearLayout22.addView(this.usesChooseView);
        EditText editText52222 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.6
            @Override // android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    setCursorVisible(true);
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.usesEditText = editText52222;
        editText52222.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
        this.usesEditText.setGravity(16);
        this.usesEditText.setTextSize(1, 16.0f);
        this.usesEditText.setHint(LocaleController.getString(R.string.UsesLimitHint));
        this.usesEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        this.usesEditText.setInputType(2);
        this.usesEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.7
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (LinkEditActivity.this.ignoreSet) {
                    return;
                }
                if (editable.toString().equals("0")) {
                    LinkEditActivity.this.usesEditText.setText("");
                    return;
                }
                try {
                    int parseInt = Integer.parseInt(editable.toString());
                    if (parseInt > 100000) {
                        LinkEditActivity.this.resetUses();
                    } else {
                        LinkEditActivity.this.chooseUses(parseInt);
                    }
                } catch (NumberFormatException unused) {
                    LinkEditActivity.this.resetUses();
                }
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i1122222, int i1222222, int i1322222) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i1122222, int i1222222, int i1322222) {
            }
        });
        linearLayout22.addView(this.usesEditText, LayoutHelper.createLinear(i6, 50));
        TextInfoPrivacyCell textInfoPrivacyCell422222 = new TextInfoPrivacyCell(context);
        this.dividerUses = textInfoPrivacyCell422222;
        textInfoPrivacyCell422222.setText(LocaleController.getString(R.string.UsesLimitHelp));
        linearLayout22.addView(this.dividerUses);
        EditText editText222222 = new EditText(context) { // from class: org.telegram.ui.LinkEditActivity.8
            @Override // android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    setCursorVisible(true);
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.nameEditText = editText222222;
        editText222222.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LinkEditActivity.9
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                Emoji.replaceEmoji((CharSequence) editable, LinkEditActivity.this.nameEditText.getPaint().getFontMetricsInt(), (int) LinkEditActivity.this.nameEditText.getPaint().getTextSize(), false);
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i1122222, int i1222222, int i1322222) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i1122222, int i1222222, int i1322222) {
            }
        });
        this.nameEditText.setCursorVisible(false);
        this.nameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        this.nameEditText.setGravity(16);
        this.nameEditText.setHint(LocaleController.getString(R.string.LinkNameHint));
        EditText editText322222 = this.nameEditText;
        int i1122222 = Theme.key_windowBackgroundWhiteGrayText;
        editText322222.setHintTextColor(Theme.getColor(i1122222));
        this.nameEditText.setLines(1);
        this.nameEditText.setPadding(AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f), 0);
        this.nameEditText.setSingleLine();
        EditText editText422222 = this.nameEditText;
        int i1222222 = Theme.key_windowBackgroundWhiteBlackText;
        editText422222.setTextColor(Theme.getColor(i1222222));
        this.nameEditText.setTextSize(1, 16.0f);
        linearLayout22.addView(this.nameEditText, LayoutHelper.createLinear(i6, 50));
        TextInfoPrivacyCell textInfoPrivacyCell522222 = new TextInfoPrivacyCell(context);
        this.dividerName = textInfoPrivacyCell522222;
        int i1322222 = R.drawable.greydivider_bottom;
        int i1422222 = Theme.key_windowBackgroundGrayShadow;
        textInfoPrivacyCell522222.setBackground(Theme.getThemedDrawableByKey(context, i1322222, i1422222));
        this.dividerName.setText(LocaleController.getString(R.string.LinkNameHelp));
        linearLayout22.addView(this.dividerName);
        if (this.type == 1) {
        }
        sizeNotifierFrameLayout22.addView(this.scrollView, LayoutHelper.createFrame(i6, -1.0f));
        FrameLayout frameLayout22222 = new FrameLayout(context);
        this.buttonLayout = frameLayout22222;
        int i1522222 = Theme.key_windowBackgroundGray;
        frameLayout22222.setBackgroundColor(getThemedColor(i1522222));
        new KeyboardNotifier(sizeNotifierFrameLayout22, new Utilities.Callback() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda9
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                LinkEditActivity.lambda$createView$11((Integer) obj);
            }
        });
        this.buttonLayout.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 87, 16.0f, 15.0f, 16.0f, 16.0f));
        sizeNotifierFrameLayout22.addView(this.buttonLayout, LayoutHelper.createFrame(i6, i7, 80));
        HeaderCell headerCell322222 = this.timeHeaderCell;
        int i1622222 = Theme.key_windowBackgroundWhite;
        headerCell322222.setBackgroundColor(Theme.getColor(i1622222));
        this.timeChooseView.setBackgroundColor(Theme.getColor(i1622222));
        this.timeEditText.setBackgroundColor(Theme.getColor(i1622222));
        this.usesHeaderCell.setBackgroundColor(Theme.getColor(i1622222));
        this.usesChooseView.setBackgroundColor(Theme.getColor(i1622222));
        this.usesEditText.setBackgroundColor(Theme.getColor(i1622222));
        this.nameEditText.setBackgroundColor(Theme.getColor(i1622222));
        sizeNotifierFrameLayout22.setBackgroundColor(Theme.getColor(i1522222));
        this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LinkEditActivity.this.onCreateClicked(view);
            }
        });
        this.buttonTextView.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
        this.dividerUses.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i1322222, i1422222));
        this.divider.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, i1422222));
        this.buttonTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
        this.usesEditText.setTextColor(Theme.getColor(i1222222));
        this.usesEditText.setHintTextColor(Theme.getColor(i1122222));
        this.timeEditText.setTextColor(Theme.getColor(i1222222));
        this.timeEditText.setHintTextColor(Theme.getColor(i1122222));
        this.usesEditText.setCursorVisible(false);
        setInviteToEdit(this.inviteToEdit);
        sizeNotifierFrameLayout22.setClipChildren(false);
        this.scrollView.setClipChildren(false);
        linearLayout22.setClipChildren(false);
        return sizeNotifierFrameLayout22;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    /* renamed from: finishFragment */
    public void lambda$onBackPressed$321() {
        this.scrollView.getLayoutParams().height = this.scrollView.getHeight();
        this.finished = true;
        super.lambda$onBackPressed$321();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.LinkEditActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                LinkEditActivity.this.lambda$getThemeDescriptions$16();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList arrayList = new ArrayList();
        int i = Theme.key_windowBackgroundWhiteBlueHeader;
        arrayList.add(new ThemeDescription(this.timeHeaderCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        arrayList.add(new ThemeDescription(this.usesHeaderCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i));
        HeaderCell headerCell = this.timeHeaderCell;
        int i2 = ThemeDescription.FLAG_BACKGROUND;
        int i3 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(headerCell, i2, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.usesHeaderCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.timeChooseView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.usesChooseView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.timeEditText, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.usesEditText, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.revokeLink, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i3));
        int i4 = Theme.key_windowBackgroundWhiteGrayText4;
        arrayList.add(new ThemeDescription(this.divider, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.dividerUses, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.dividerName, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_featuredStickers_addButton));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_featuredStickers_addButtonPressed));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_featuredStickers_buttonText));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_text_RedRegular));
        return arrayList;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setInviteToEdit(TLRPC.TL_chatInviteExported tL_chatInviteExported) {
        this.inviteToEdit = tL_chatInviteExported;
        if (this.fragmentView == null || tL_chatInviteExported == null) {
            return;
        }
        int i = tL_chatInviteExported.expire_date;
        if (i > 0) {
            chooseDate(i);
            this.currentInviteDate = ((Integer) this.dispalyedDates.get(this.timeChooseView.getSelectedIndex())).intValue();
        } else {
            this.currentInviteDate = 0;
        }
        int i2 = tL_chatInviteExported.usage_limit;
        if (i2 > 0) {
            chooseUses(i2);
            this.usesEditText.setText(Integer.toString(tL_chatInviteExported.usage_limit));
        }
        TextCheckCell textCheckCell = this.approveCell;
        if (textCheckCell != null) {
            textCheckCell.setBackgroundColor(Theme.getColor(tL_chatInviteExported.request_needed ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
            this.approveCell.setChecked(tL_chatInviteExported.request_needed);
        }
        setUsesVisible(!tL_chatInviteExported.request_needed);
        if (!TextUtils.isEmpty(tL_chatInviteExported.title)) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tL_chatInviteExported.title);
            Emoji.replaceEmoji((CharSequence) spannableStringBuilder, this.nameEditText.getPaint().getFontMetricsInt(), (int) this.nameEditText.getPaint().getTextSize(), false);
            this.nameEditText.setText(spannableStringBuilder);
        }
        TextCheckCell textCheckCell2 = this.subCell;
        if (textCheckCell2 != null) {
            textCheckCell2.setChecked(tL_chatInviteExported.subscription_pricing != null);
        }
        if (tL_chatInviteExported.subscription_pricing != null) {
            TextCheckCell textCheckCell3 = this.approveCell;
            if (textCheckCell3 != null) {
                textCheckCell3.setChecked(false);
                this.approveCell.setCheckBoxIcon(R.drawable.permission_locked);
            }
            TextInfoPrivacyCell textInfoPrivacyCell = this.approveHintCell;
            if (textInfoPrivacyCell != null) {
                textInfoPrivacyCell.setText(LocaleController.getString(R.string.ApproveNewMembersDescriptionFrozen));
            }
        }
        EditTextCell editTextCell = this.subEditPriceCell;
        if (editTextCell != null) {
            editTextCell.setVisibility(tL_chatInviteExported.subscription_pricing != null ? 0 : 8);
            this.subEditPriceCell.setText(Long.toString(tL_chatInviteExported.subscription_pricing.amount));
            this.subEditPriceCell.editText.setClickable(false);
            this.subEditPriceCell.editText.setFocusable(false);
            this.subEditPriceCell.editText.setFocusableInTouchMode(false);
            this.subEditPriceCell.editText.setLongClickable(false);
        }
    }
}
