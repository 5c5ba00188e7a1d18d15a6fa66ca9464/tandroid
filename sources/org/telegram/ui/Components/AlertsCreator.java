package org.telegram.ui.Components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.util.Consumer;
import j$.time.YearMonth;
import j$.util.Collection$-EL;
import j$.util.function.LongFunction;
import j$.util.function.Predicate;
import j$.util.function.ToLongFunction;
import j$.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.OneUIUtilities;
import org.telegram.messenger.R;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CacheControlActivity;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ChatActivity$$ExternalSyntheticLambda294;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.LanguageSelectActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LoginActivity;
import org.telegram.ui.NotificationsCustomSettingsActivity;
import org.telegram.ui.PrivacyControlActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.ProfileNotificationsActivity;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.ThemePreviewActivity;
import org.telegram.ui.TooManyCommunitiesActivity;

/* loaded from: classes.dex */
public abstract class AlertsCreator {

    /* loaded from: classes3.dex */
    public interface AccountSelectDelegate {
        void didSelectAccount(int i);
    }

    /* loaded from: classes3.dex */
    public interface BlockDialogCallback {
        void run(boolean z, boolean z2);
    }

    /* loaded from: classes3.dex */
    public interface DatePickerDelegate {
        void didSelectDate(int i, int i2, int i3);
    }

    /* loaded from: classes3.dex */
    public static class ScheduleDatePickerColors {
        public final int backgroundColor;
        public final int buttonBackgroundColor;
        public final int buttonBackgroundPressedColor;
        public final int buttonTextColor;
        public final int iconColor;
        public final int iconSelectorColor;
        public final int subMenuBackgroundColor;
        public final int subMenuSelectorColor;
        public final int subMenuTextColor;
        public final int textColor;

        private ScheduleDatePickerColors() {
            this((Theme.ResourcesProvider) null);
        }

        public ScheduleDatePickerColors(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
            this(i, i2, i3, i4, i5, i6, i7, Theme.getColor(Theme.key_featuredStickers_buttonText), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed));
        }

        public ScheduleDatePickerColors(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            this.textColor = i;
            this.backgroundColor = i2;
            this.iconColor = i3;
            this.iconSelectorColor = i4;
            this.subMenuTextColor = i5;
            this.subMenuBackgroundColor = i6;
            this.subMenuSelectorColor = i7;
            this.buttonTextColor = i8;
            this.buttonBackgroundColor = i9;
            this.buttonBackgroundPressedColor = i10;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public ScheduleDatePickerColors(Theme.ResourcesProvider resourcesProvider) {
            this(r2, r3, r4, r5, r6, r7, r8, r9, resourcesProvider != null ? resourcesProvider.getColorOrDefault(r0) : Theme.getColor(r0), resourcesProvider != null ? resourcesProvider.getColorOrDefault(Theme.key_featuredStickers_addButtonPressed) : Theme.getColor(Theme.key_featuredStickers_addButtonPressed));
            int i = Theme.key_dialogTextBlack;
            int colorOrDefault = resourcesProvider != null ? resourcesProvider.getColorOrDefault(i) : Theme.getColor(i);
            int i2 = Theme.key_dialogBackground;
            int colorOrDefault2 = resourcesProvider != null ? resourcesProvider.getColorOrDefault(i2) : Theme.getColor(i2);
            int i3 = Theme.key_sheet_other;
            int colorOrDefault3 = resourcesProvider != null ? resourcesProvider.getColorOrDefault(i3) : Theme.getColor(i3);
            int i4 = Theme.key_player_actionBarSelector;
            int colorOrDefault4 = resourcesProvider != null ? resourcesProvider.getColorOrDefault(i4) : Theme.getColor(i4);
            int i5 = Theme.key_actionBarDefaultSubmenuItem;
            int colorOrDefault5 = resourcesProvider != null ? resourcesProvider.getColorOrDefault(i5) : Theme.getColor(i5);
            int i6 = Theme.key_actionBarDefaultSubmenuBackground;
            int colorOrDefault6 = resourcesProvider != null ? resourcesProvider.getColorOrDefault(i6) : Theme.getColor(i6);
            int i7 = Theme.key_listSelector;
            int colorOrDefault7 = resourcesProvider != null ? resourcesProvider.getColorOrDefault(i7) : Theme.getColor(i7);
            int i8 = Theme.key_featuredStickers_buttonText;
            int colorOrDefault8 = resourcesProvider != null ? resourcesProvider.getColorOrDefault(i8) : Theme.getColor(i8);
            int i9 = Theme.key_featuredStickers_addButton;
        }
    }

    /* loaded from: classes3.dex */
    public interface ScheduleDatePickerDelegate {
        void didSelectDate(boolean z, int i);
    }

    /* loaded from: classes3.dex */
    public interface SoundFrequencyDelegate {
        void didSelectValues(int i, int i2);
    }

    /* loaded from: classes3.dex */
    public interface StatusUntilDatePickerDelegate {
        void didSelectDate(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkCalendarDate(long j, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i = 1;
        int i2 = calendar.get(1);
        int i3 = calendar.get(2);
        int i4 = calendar.get(5);
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i5 = calendar.get(1);
        int i6 = calendar.get(2);
        int i7 = calendar.get(5);
        numberPicker3.setMaxValue(i5);
        numberPicker3.setMinValue(i2);
        int value = numberPicker3.getValue();
        numberPicker2.setMaxValue(value == i5 ? i6 : 11);
        numberPicker2.setMinValue(value == i2 ? i3 : 0);
        int value2 = numberPicker2.getValue();
        calendar.set(1, value);
        calendar.set(2, value2);
        int actualMaximum = calendar.getActualMaximum(5);
        if (value == i5 && value2 == i6) {
            actualMaximum = Math.min(i7, actualMaximum);
        }
        numberPicker.setMaxValue(actualMaximum);
        if (value == i2 && value2 == i3) {
            i = i4;
        }
        numberPicker.setMinValue(i);
    }

    private static boolean checkInternalBotApp(String str) {
        return Uri.parse(str).getPath().matches("^/\\w*/[^\\d]*(?:\\?startapp=.*?|)$");
    }

    private static void checkPickerDate(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i = 1;
        int i2 = calendar.get(1);
        int i3 = calendar.get(2);
        int i4 = calendar.get(5);
        numberPicker3.setMinValue(i2);
        int value = numberPicker3.getValue();
        numberPicker2.setMinValue(value == i2 ? i3 : 0);
        int value2 = numberPicker2.getValue();
        if (value == i2 && value2 == i3) {
            i = i4;
        }
        numberPicker.setMinValue(i);
    }

    public static void checkRestrictedInviteUsers(final int i, final TLRPC.Chat chat, TLRPC.TL_messages_invitedUsers tL_messages_invitedUsers) {
        TLRPC.User user;
        if (tL_messages_invitedUsers == null || tL_messages_invitedUsers.missing_invitees.isEmpty() || chat == null) {
            return;
        }
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        Iterator<TLRPC.TL_missingInvitee> it = tL_messages_invitedUsers.missing_invitees.iterator();
        while (it.hasNext()) {
            TLRPC.TL_missingInvitee next = it.next();
            if (tL_messages_invitedUsers.updates != null) {
                for (int i2 = 0; i2 < tL_messages_invitedUsers.updates.users.size(); i2++) {
                    user = tL_messages_invitedUsers.updates.users.get(i2);
                    if (user.id == next.user_id) {
                        break;
                    }
                }
            }
            user = null;
            if (user == null) {
                user = MessagesController.getInstance(i).getUser(Long.valueOf(next.user_id));
            }
            if (user != null) {
                arrayList.add(user);
                if (next.premium_required_for_pm) {
                    arrayList2.add(Long.valueOf(user.id));
                }
                if (next.premium_would_allow_invite) {
                    arrayList3.add(Long.valueOf(user.id));
                }
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.lambda$checkRestrictedInviteUsers$53(i, chat, arrayList, arrayList2, arrayList3);
            }
        }, 200L);
    }

    public static boolean checkScheduleDate(TextView textView, TextView textView2, int i, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        return checkScheduleDate(textView, textView2, 0L, i, numberPicker, numberPicker2, numberPicker3);
    }

    public static boolean checkScheduleDate(TextView textView, TextView textView2, long j, int i, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        int i2;
        long j2;
        int i3;
        int i4;
        int i5;
        int i6;
        char c;
        String formatPluralString;
        String formatString;
        int i7;
        int i8;
        int value = numberPicker.getValue();
        int value2 = numberPicker2.getValue();
        int value3 = numberPicker3.getValue();
        Calendar calendar = Calendar.getInstance();
        long currentTimeMillis = System.currentTimeMillis();
        calendar.setTimeInMillis(currentTimeMillis);
        int i9 = calendar.get(1);
        calendar.get(6);
        if (j > 0) {
            i2 = i9;
            calendar.setTimeInMillis(currentTimeMillis + (j * 1000));
            calendar.set(11, 23);
            calendar.set(12, 59);
            calendar.set(13, 59);
            i4 = 7;
            j2 = calendar.getTimeInMillis();
            i3 = 23;
            i5 = 59;
        } else {
            i2 = i9;
            j2 = j;
            i3 = 0;
            i4 = 0;
            i5 = 0;
        }
        int i10 = i4;
        long j3 = currentTimeMillis + 60000;
        calendar.setTimeInMillis(j3);
        int i11 = calendar.get(11);
        int i12 = calendar.get(12);
        int i13 = i3;
        calendar.setTimeInMillis(System.currentTimeMillis() + (value * 86400000));
        calendar.set(11, value2);
        calendar.set(12, value3);
        long timeInMillis = calendar.getTimeInMillis();
        calendar.setTimeInMillis(timeInMillis);
        numberPicker.setMinValue(0);
        long j4 = j2;
        if (j4 > 0) {
            i6 = i10;
            numberPicker.setMaxValue(i6);
        } else {
            i6 = i10;
        }
        int value4 = numberPicker.getValue();
        numberPicker2.setMinValue(value4 == 0 ? i11 : 0);
        if (j4 > 0) {
            numberPicker2.setMaxValue(value4 == i6 ? i13 : 23);
        }
        int value5 = numberPicker2.getValue();
        numberPicker3.setMinValue((value4 == 0 && value5 == i11) ? i12 : 0);
        if (j4 > 0) {
            numberPicker3.setMaxValue((value4 == i6 && value5 == i13) ? i5 : 59);
        }
        int value6 = numberPicker3.getValue();
        if (timeInMillis <= j3) {
            calendar.setTimeInMillis(j3);
        } else if (j4 > 0 && timeInMillis > j4) {
            calendar.setTimeInMillis(j4);
        }
        int i14 = calendar.get(1);
        calendar.setTimeInMillis(System.currentTimeMillis() + (value4 * 86400000));
        calendar.set(11, value5);
        calendar.set(12, value6);
        long timeInMillis2 = calendar.getTimeInMillis();
        if (textView != null) {
            if (value4 == 0) {
                i7 = 1;
                i8 = 0;
            } else if (i2 == i14) {
                i7 = 1;
                i8 = 1;
            } else {
                i7 = 1;
                i8 = 2;
            }
            if (i == i7) {
                i8 += 3;
            } else if (i == 2) {
                i8 += 6;
            } else if (i == 3) {
                i8 += 9;
            }
            textView.setText(LocaleController.getInstance().getFormatterScheduleSend(i8).format(timeInMillis2));
        }
        if (textView2 != null) {
            int i15 = (int) ((timeInMillis2 - currentTimeMillis) / 1000);
            if (i15 > 86400) {
                c = 0;
                formatPluralString = LocaleController.formatPluralString("DaysSchedule", Math.round(i15 / 86400.0f), new Object[0]);
            } else {
                c = 0;
                formatPluralString = i15 >= 3600 ? LocaleController.formatPluralString("HoursSchedule", Math.round(i15 / 3600.0f), new Object[0]) : i15 >= 60 ? LocaleController.formatPluralString("MinutesSchedule", Math.round(i15 / 60.0f), new Object[0]) : LocaleController.formatPluralString("SecondsSchedule", i15, new Object[0]);
            }
            if (textView2.getTag() != null) {
                int i16 = R.string.VoipChannelScheduleInfo;
                Object[] objArr = new Object[1];
                objArr[c] = formatPluralString;
                formatString = LocaleController.formatString("VoipChannelScheduleInfo", i16, objArr);
            } else {
                int i17 = R.string.VoipGroupScheduleInfo;
                Object[] objArr2 = new Object[1];
                objArr2[c] = formatPluralString;
                formatString = LocaleController.formatString("VoipGroupScheduleInfo", i17, objArr2);
            }
            textView2.setText(formatString);
        }
        return timeInMillis - currentTimeMillis > 60000;
    }

    public static boolean checkSlowMode(Context context, int i, long j, boolean z) {
        TLRPC.Chat chat;
        if (!DialogObject.isChatDialog(j) || (chat = MessagesController.getInstance(i).getChat(Long.valueOf(-j))) == null || !chat.slowmode_enabled || ChatObject.hasAdminRights(chat)) {
            return false;
        }
        if (!z) {
            TLRPC.ChatFull chatFull = MessagesController.getInstance(i).getChatFull(chat.id);
            if (chatFull == null) {
                chatFull = MessagesStorage.getInstance(i).loadChatInfo(chat.id, ChatObject.isChannel(chat), new CountDownLatch(1), false, false);
            }
            if (chatFull != null && chatFull.slowmode_next_send_date >= ConnectionsManager.getInstance(i).getCurrentTime()) {
                z = true;
            }
        }
        if (!z) {
            return false;
        }
        createSimpleAlert(context, chat.title, LocaleController.getString(R.string.SlowmodeSendError)).show();
        return true;
    }

    public static AlertDialog createAccountSelectDialog(Activity activity, final AccountSelectDelegate accountSelectDelegate) {
        if (UserConfig.getActivatedAccountsCount() < 2) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final Runnable dismissRunnable = builder.getDismissRunnable();
        final AlertDialog[] alertDialogArr = new AlertDialog[1];
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        for (int i = 0; i < 4; i++) {
            if (UserConfig.getInstance(i).getCurrentUser() != null) {
                AccountSelectCell accountSelectCell = new AccountSelectCell(activity, false);
                accountSelectCell.setAccount(i, false);
                accountSelectCell.setPadding(AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f), 0);
                accountSelectCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                linearLayout.addView(accountSelectCell, LayoutHelper.createLinear(-1, 50));
                accountSelectCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda12
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AlertsCreator.lambda$createAccountSelectDialog$149(alertDialogArr, dismissRunnable, accountSelectDelegate, view);
                    }
                });
            }
        }
        builder.setTitle(LocaleController.getString(R.string.SelectAccount));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog create = builder.create();
        alertDialogArr[0] = create;
        return create;
    }

    public static Dialog createApkRestrictedDialog(final Context context, Theme.ResourcesProvider resourcesProvider) {
        return new AlertDialog.Builder(context, resourcesProvider).setMessage(LocaleController.getString(R.string.ApkRestricted)).setTopAnimation(R.raw.permission_request_apk, 72, false, Theme.getColor(Theme.key_dialogTopBackground)).setPositiveButton(LocaleController.getString(R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda10
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createApkRestrictedDialog$6(context, dialogInterface, i);
            }
        }).setNegativeButton(LocaleController.getString(R.string.ContactsPermissionAlertNotNow), null).create();
    }

    public static BottomSheet.Builder createAutoDeleteDatePickerDialog(Context context, int i, Theme.ResourcesProvider resourcesProvider, final ScheduleDatePickerDelegate scheduleDatePickerDelegate) {
        boolean z = false;
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors(resourcesProvider);
        final BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        final int[] iArr = {0, 1440, 2880, 4320, 5760, 7200, 8640, 10080, 20160, 30240, 44640, 89280, 133920, 178560, 223200, 267840, 525600};
        final NumberPicker numberPicker = new NumberPicker(context, resourcesProvider) { // from class: org.telegram.ui.Components.AlertsCreator.37
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i2) {
                int i3 = iArr[i2];
                return i3 == 0 ? LocaleController.getString(R.string.AutoDeleteNever) : i3 < 10080 ? LocaleController.formatPluralString("Days", i3 / 1440, new Object[0]) : i3 < 44640 ? LocaleController.formatPluralString("Weeks", i3 / 1440, new Object[0]) : i3 < 525600 ? LocaleController.formatPluralString("Months", i3 / 10080, new Object[0]) : LocaleController.formatPluralString("Years", ((i3 * 5) / 31) * 1440, new Object[0]);
            }
        };
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(16);
        numberPicker.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker.setValue(0);
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda70
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i2) {
                String lambda$createAutoDeleteDatePickerDialog$107;
                lambda$createAutoDeleteDatePickerDialog$107 = AlertsCreator.lambda$createAutoDeleteDatePickerDialog$107(iArr, i2);
                return lambda$createAutoDeleteDatePickerDialog$107;
            }
        });
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.38
            boolean ignoreLayout = false;

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                this.ignoreLayout = true;
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i4 = point.x > point.y ? 3 : 5;
                numberPicker.setItemCount(i4);
                numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i4;
                this.ignoreLayout = false;
                super.onMeasure(i2, i3);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        boolean z2 = true;
        linearLayout.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString(R.string.AutoDeleteAfteTitle));
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda71
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createAutoDeleteDatePickerDialog$108;
                lambda$createAutoDeleteDatePickerDialog$108 = AlertsCreator.lambda$createAutoDeleteDatePickerDialog$108(view, motionEvent);
                return lambda$createAutoDeleteDatePickerDialog$108;
            }
        });
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setWeightSum(1.0f);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        final AnimatedTextView animatedTextView = new AnimatedTextView(context, z2, z2, z) { // from class: org.telegram.ui.Components.AlertsCreator.39
            @Override // android.view.View
            public CharSequence getAccessibilityClassName() {
                return Button.class.getName();
            }
        };
        linearLayout2.addView(numberPicker, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 1.0f));
        animatedTextView.setPadding(0, 0, 0, 0);
        animatedTextView.setGravity(17);
        animatedTextView.setTextColor(scheduleDatePickerColors.buttonTextColor);
        animatedTextView.setTextSize(AndroidUtilities.dp(14.0f));
        animatedTextView.setTypeface(AndroidUtilities.bold());
        animatedTextView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), scheduleDatePickerColors.buttonBackgroundColor, scheduleDatePickerColors.buttonBackgroundPressedColor));
        linearLayout.addView(animatedTextView, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        animatedTextView.setText(LocaleController.getString(R.string.DisableAutoDeleteTimer));
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda72
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker2, int i2, int i3) {
                AlertsCreator.lambda$createAutoDeleteDatePickerDialog$109(AnimatedTextView.this, numberPicker2, i2, i3);
            }
        });
        animatedTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda73
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createAutoDeleteDatePickerDialog$110(iArr, numberPicker, scheduleDatePickerDelegate, builder, view);
            }
        });
        builder.setCustomView(linearLayout);
        BottomSheet show = builder.show();
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    public static Dialog createBackgroundActivityDialog(final Context context) {
        return new AlertDialog.Builder(context).setTitle(LocaleController.getString(R.string.AllowBackgroundActivity)).setMessage(AndroidUtilities.replaceTags(LocaleController.getString(OneUIUtilities.isOneUI() ? Build.VERSION.SDK_INT >= 31 ? R.string.AllowBackgroundActivityInfoOneUIAboveS : R.string.AllowBackgroundActivityInfoOneUIBelowS : R.string.AllowBackgroundActivityInfo))).setTopAnimation(R.raw.permission_request_apk, 72, false, Theme.getColor(Theme.key_dialogTopBackground)).setPositiveButton(LocaleController.getString(R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createBackgroundActivityDialog$1(context, dialogInterface, i);
            }
        }).setNegativeButton(LocaleController.getString(R.string.ContactsPermissionAlertNotNow), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                SharedConfig.BackgroundActivityPrefs.increaseDismissedCount();
            }
        }).create();
    }

    public static AlertDialog.Builder createBackgroundLocationPermissionDialog(final Activity activity, TLRPC.User user, final Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        if (activity == null || Build.VERSION.SDK_INT < 29) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, resourcesProvider);
        String readRes = AndroidUtilities.readRes(Theme.getCurrentTheme().isDark() ? R.raw.permission_map_dark : R.raw.permission_map);
        String readRes2 = AndroidUtilities.readRes(Theme.getCurrentTheme().isDark() ? R.raw.permission_pin_dark : R.raw.permission_pin);
        FrameLayout frameLayout = new FrameLayout(activity);
        frameLayout.setClipToOutline(true);
        frameLayout.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.AlertsCreator.49
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
            }
        });
        View view = new View(activity);
        view.setBackground(SvgHelper.getDrawable(readRes));
        frameLayout.addView(view, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        View view2 = new View(activity);
        view2.setBackground(SvgHelper.getDrawable(readRes2));
        frameLayout.addView(view2, LayoutHelper.createFrame(60, 82.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
        BackupImageView backupImageView = new BackupImageView(activity);
        backupImageView.setRoundRadius(AndroidUtilities.dp(26.0f));
        backupImageView.setForUserOrChat(user, new AvatarDrawable(user));
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(52, 52.0f, 17, 0.0f, 0.0f, 0.0f, 11.0f));
        builder.setTopView(frameLayout);
        builder.setTopViewAspectRatio(0.37820512f);
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString(R.string.PermissionBackgroundLocation)));
        builder.setPositiveButton(LocaleController.getString(R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda59
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createBackgroundLocationPermissionDialog$137(activity, dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda60
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                runnable.run();
            }
        });
        return builder;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0213  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BottomSheet.Builder createBirthdayPickerDialog(Context context, String str, String str2, TLRPC.TL_birthday tL_birthday, final Utilities.Callback callback, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        LinearLayout linearLayout;
        int i;
        if (context == null) {
            return null;
        }
        final BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        final NumberPicker numberPicker = new NumberPicker(context, resourcesProvider);
        numberPicker.setTextOffset(AndroidUtilities.dp(10.0f));
        numberPicker.setItemCount(5);
        final NumberPicker numberPicker2 = new NumberPicker(context, resourcesProvider);
        numberPicker2.setItemCount(5);
        numberPicker2.setTextOffset(-AndroidUtilities.dp(10.0f));
        final NumberPicker numberPicker3 = new NumberPicker(context, resourcesProvider);
        numberPicker3.setItemCount(5);
        numberPicker3.setTextOffset(-AndroidUtilities.dp(24.0f));
        LinearLayout linearLayout2 = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.31
            boolean ignoreLayout = false;

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                this.ignoreLayout = true;
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i4 = point.x > point.y ? 3 : 5;
                numberPicker.setItemCount(i4);
                numberPicker2.setItemCount(i4);
                numberPicker3.setItemCount(i4);
                numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i4;
                numberPicker2.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i4;
                numberPicker3.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i4;
                this.ignoreLayout = false;
                super.onMeasure(i2, i3);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        linearLayout2.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(str);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda28
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createBirthdayPickerDialog$91;
                lambda$createBirthdayPickerDialog$91 = AlertsCreator.lambda$createBirthdayPickerDialog$91(view, motionEvent);
                return lambda$createBirthdayPickerDialog$91;
            }
        });
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setGravity(17);
        linearLayout3.setOrientation(0);
        linearLayout3.setWeightSum(1.0f);
        linearLayout2.addView(linearLayout3, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        Calendar calendar = Calendar.getInstance();
        int i2 = calendar.get(1) - 149;
        calendar.setTimeInMillis(System.currentTimeMillis());
        final int i3 = calendar.get(5);
        final int i4 = calendar.get(2);
        final int i5 = calendar.get(1);
        final int i6 = i5 + 1;
        final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.lambda$createBirthdayPickerDialog$92(NumberPicker.this, i6, numberPicker, numberPicker2, i5, i4, i3);
            }
        };
        System.currentTimeMillis();
        TextView textView2 = new TextView(context) { // from class: org.telegram.ui.Components.AlertsCreator.32
            @Override // android.widget.TextView, android.view.View
            public CharSequence getAccessibilityClassName() {
                return Button.class.getName();
            }
        };
        linearLayout3.addView(numberPicker, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.25f));
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(31);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda30
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i7) {
                String lambda$createBirthdayPickerDialog$93;
                lambda$createBirthdayPickerDialog$93 = AlertsCreator.lambda$createBirthdayPickerDialog$93(i7);
                return lambda$createBirthdayPickerDialog$93;
            }
        });
        NumberPicker.OnScrollListener onScrollListener = new NumberPicker.OnScrollListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda31
            @Override // org.telegram.ui.Components.NumberPicker.OnScrollListener
            public final void onScrollStateChange(NumberPicker numberPicker4, int i7) {
                AlertsCreator.lambda$createBirthdayPickerDialog$94(runnable2, numberPicker4, i7);
            }
        };
        numberPicker.setOnScrollListener(onScrollListener);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(11);
        numberPicker2.setWrapSelectorWheel(false);
        linearLayout3.addView(numberPicker2, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.5f));
        numberPicker2.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda32
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i7) {
                String lambda$createBirthdayPickerDialog$95;
                lambda$createBirthdayPickerDialog$95 = AlertsCreator.lambda$createBirthdayPickerDialog$95(i7);
                return lambda$createBirthdayPickerDialog$95;
            }
        });
        numberPicker2.setOnScrollListener(onScrollListener);
        numberPicker3.setMinValue(i2);
        numberPicker3.setMaxValue(i6);
        numberPicker3.setWrapSelectorWheel(false);
        numberPicker3.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda33
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i7) {
                String lambda$createBirthdayPickerDialog$96;
                lambda$createBirthdayPickerDialog$96 = AlertsCreator.lambda$createBirthdayPickerDialog$96(i6, i7);
                return lambda$createBirthdayPickerDialog$96;
            }
        });
        linearLayout3.addView(numberPicker3, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.25f));
        numberPicker3.setOnScrollListener(onScrollListener);
        if (tL_birthday != null) {
            numberPicker.setValue(tL_birthday.day);
            numberPicker2.setValue(tL_birthday.month - 1);
            if ((tL_birthday.flags & 1) != 0) {
                numberPicker3.setValue(tL_birthday.year);
                runnable2.run();
                if (runnable == null) {
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    final LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context);
                    linksTextView.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
                    linksTextView.setTextSize(1, 13.0f);
                    linksTextView.setTextColor(Theme.getColor(Theme.key_dialogTextGray2, resourcesProvider));
                    linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
                    i = 17;
                    linksTextView.setGravity(17);
                    frameLayout2.addView(linksTextView, LayoutHelper.createFrame(-2, -2, 17));
                    linearLayout = linearLayout2;
                    linearLayout.addView(frameLayout2, LayoutHelper.createLinear(-1, -2));
                    final int i7 = UserConfig.selectedAccount;
                    final Runnable runnable3 = new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda34
                        @Override // java.lang.Runnable
                        public final void run() {
                            AlertsCreator.lambda$createBirthdayPickerDialog$98(i7, linksTextView);
                        }
                    };
                    runnable3.run();
                    NotificationCenter.getInstance(i7).listen(frameLayout2, NotificationCenter.privacyRulesUpdated, new Utilities.Callback() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda35
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            runnable3.run();
                        }
                    });
                    ContactsController.getInstance(i7).loadPrivacySettings();
                } else {
                    linearLayout = linearLayout2;
                    i = 17;
                }
                textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
                textView2.setGravity(i);
                textView2.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
                textView2.setTextSize(1, 14.0f);
                textView2.setTypeface(AndroidUtilities.bold());
                textView2.setText(str2);
                textView2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider), Theme.getColor(Theme.key_featuredStickers_addButtonPressed, resourcesProvider)));
                linearLayout.addView(textView2, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
                textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda36
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AlertsCreator.lambda$createBirthdayPickerDialog$100(NumberPicker.this, numberPicker2, numberPicker3, i6, builder, callback, view);
                    }
                });
                builder.setCustomView(linearLayout);
                return builder;
            }
        } else {
            numberPicker.setValue(calendar.get(5));
            numberPicker2.setValue(calendar.get(2));
        }
        numberPicker3.setValue(i6);
        runnable2.run();
        if (runnable == null) {
        }
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView2.setGravity(i);
        textView2.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setText(str2);
        textView2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider), Theme.getColor(Theme.key_featuredStickers_addButtonPressed, resourcesProvider)));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda36
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createBirthdayPickerDialog$100(NumberPicker.this, numberPicker2, numberPicker3, i6, builder, callback, view);
            }
        });
        builder.setCustomView(linearLayout);
        return builder;
    }

    public static void createBlockDialogAlert(BaseFragment baseFragment, int i, boolean z, TLRPC.User user, final BlockDialogCallback blockDialogCallback) {
        String string;
        String formatString;
        CheckBoxCell checkBoxCell;
        int i2;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (i == 1 && user == null) {
            return;
        }
        Activity parentActivity = baseFragment.getParentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        CheckBoxCell[] checkBoxCellArr = new CheckBoxCell[2];
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        builder.setView(linearLayout);
        if (i == 1) {
            String formatName = ContactsController.formatName(user.first_name, user.last_name);
            builder.setTitle(LocaleController.formatString("BlockUserTitle", R.string.BlockUserTitle, formatName));
            string = LocaleController.getString(R.string.BlockUser);
            formatString = LocaleController.formatString("BlockUserMessage", R.string.BlockUserMessage, formatName);
        } else {
            builder.setTitle(LocaleController.formatString("BlockUserTitle", R.string.BlockUserTitle, LocaleController.formatPluralString("UsersCountTitle", i, new Object[0])));
            string = LocaleController.getString(R.string.BlockUsers);
            formatString = LocaleController.formatString("BlockUsersMessage", R.string.BlockUsersMessage, LocaleController.formatPluralString("UsersCount", i, new Object[0]));
        }
        builder.setMessage(AndroidUtilities.replaceTags(formatString));
        final boolean[] zArr = {true, true};
        for (final int i3 = 0; i3 < 2; i3++) {
            if (i3 != 0 || z) {
                CheckBoxCell checkBoxCell2 = new CheckBoxCell(parentActivity, 1);
                checkBoxCellArr[i3] = checkBoxCell2;
                checkBoxCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                if (i3 == 0) {
                    checkBoxCell = checkBoxCellArr[i3];
                    i2 = R.string.ReportSpamTitle;
                } else {
                    checkBoxCell = checkBoxCellArr[i3];
                    i2 = i == 1 ? R.string.DeleteThisChatBothSides : R.string.DeleteTheseChatsBothSides;
                }
                checkBoxCell.setText(LocaleController.getString(i2), "", true, false);
                checkBoxCellArr[i3].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                linearLayout.addView(checkBoxCellArr[i3], LayoutHelper.createLinear(-1, 48));
                checkBoxCellArr[i3].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda68
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AlertsCreator.lambda$createBlockDialogAlert$54(zArr, i3, view);
                    }
                });
            }
        }
        builder.setPositiveButton(string, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda69
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i4) {
                AlertsCreator.lambda$createBlockDialogAlert$55(AlertsCreator.BlockDialogCallback.this, zArr, dialogInterface, i4);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog create = builder.create();
        baseFragment.showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
        }
    }

    public static void createBotLaunchAlert(final BaseFragment baseFragment, final AtomicBoolean atomicBoolean, final TLRPC.User user, final Runnable runnable) {
        float f;
        int dp;
        if (baseFragment == null) {
            return;
        }
        final Context context = baseFragment.getContext();
        final CheckBoxCell[] checkBoxCellArr = new CheckBoxCell[1];
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context) { // from class: org.telegram.ui.Components.AlertsCreator.5
            @Override // org.telegram.ui.Components.LinkSpanDrawable.LinksTextView, android.widget.TextView
            public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                super.setText(Emoji.replaceEmoji(charSequence, getPaint().getFontMetricsInt(), false), bufferType);
            }
        };
        NotificationCenter.listenEmojiLoading(linksTextView);
        linksTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn));
        linksTextView.setTextSize(1, 16.0f);
        linksTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.6
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, i2);
                if (checkBoxCellArr[0] != null) {
                    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + checkBoxCellArr[0].getMeasuredHeight() + AndroidUtilities.dp(7.0f));
                }
            }
        };
        builder.setCustomViewOffset(6);
        builder.setView(frameLayout);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(18.0f));
        BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, 22.0f, 5.0f, 22.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        simpleTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
        simpleTextView.setTextSize(20);
        simpleTextView.setTypeface(AndroidUtilities.bold());
        simpleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        simpleTextView.setEllipsizeByGradient(true);
        simpleTextView.setText(user.first_name);
        if (user.verified) {
            Drawable mutate = context.getResources().getDrawable(R.drawable.verified_area).mutate();
            int color = Theme.getColor(Theme.key_chats_verifiedBackground);
            PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
            mutate.setColorFilter(new PorterDuffColorFilter(color, mode));
            Drawable mutate2 = context.getResources().getDrawable(R.drawable.verified_check).mutate();
            mutate2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_verifiedCheck), mode));
            simpleTextView.setRightDrawable(new CombinedDrawable(mutate, mutate2));
        }
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue));
        textView.setTextSize(1, 14.0f);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda119
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createBotLaunchAlert$30(TLRPC.User.this, baseFragment, builder, view);
            }
        });
        SpannableString valueOf = SpannableString.valueOf(LocaleController.getString(R.string.MoreAboutThisBot) + "  ");
        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.attach_arrow_right);
        coloredImageSpan.setTopOffset(1);
        coloredImageSpan.setSize(AndroidUtilities.dp(10.0f));
        valueOf.setSpan(coloredImageSpan, valueOf.length() - 1, valueOf.length(), 33);
        textView.setText(valueOf);
        boolean z = LocaleController.isRTL;
        frameLayout.addView(simpleTextView, LayoutHelper.createFrame(-1, -2.0f, (z ? 5 : 3) | 48, z ? 21 : 76, 0.0f, z ? 76 : 21, 0.0f));
        boolean z2 = LocaleController.isRTL;
        frameLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, (z2 ? 5 : 3) | 48, z2 ? 21 : 76, 24.0f, z2 ? 76 : 21, 0.0f));
        frameLayout.addView(linksTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 57.0f, 24.0f, 1.0f));
        if (atomicBoolean != null) {
            atomicBoolean.set(true);
            CheckBoxCell checkBoxCell = new CheckBoxCell(context, 1, baseFragment.getResourceProvider());
            checkBoxCellArr[0] = checkBoxCell;
            checkBoxCell.allowMultiline();
            checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
            checkBoxCellArr[0].setText(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.OpenUrlOption2, UserObject.getUserName(user))), "", true, false);
            CheckBoxCell checkBoxCell2 = checkBoxCellArr[0];
            if (LocaleController.isRTL) {
                f = 16.0f;
                dp = AndroidUtilities.dp(16.0f);
            } else {
                f = 16.0f;
                dp = AndroidUtilities.dp(8.0f);
            }
            checkBoxCell2.setPadding(dp, 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(f), 0);
            checkBoxCellArr[0].setChecked(true, false);
            frameLayout.addView(checkBoxCellArr[0], LayoutHelper.createFrame(-1, 48.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
            checkBoxCellArr[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda120
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AlertsCreator.lambda$createBotLaunchAlert$31(atomicBoolean, view);
                }
            });
        }
        if (UserObject.isReplyUser(user)) {
            avatarDrawable.setScaleSize(0.8f);
            avatarDrawable.setAvatarType(12);
            backupImageView.setImage((ImageLocation) null, (String) null, avatarDrawable, user);
        } else {
            avatarDrawable.setScaleSize(1.0f);
            avatarDrawable.setInfo(baseFragment.getCurrentAccount(), user);
            backupImageView.setForUserOrChat(user, avatarDrawable);
        }
        builder.setPositiveButton(LocaleController.getString(R.string.Start), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda121
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                runnable.run();
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        final AlertDialog create = builder.create();
        baseFragment.showDialog(create);
        linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.BotWebViewStartPermission2), new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda122
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.lambda$createBotLaunchAlert$33(AlertDialog.this, context);
            }
        }));
    }

    public static void createBotLaunchAlert(final BaseFragment baseFragment, final TLRPC.User user, final Runnable runnable, final Runnable runnable2) {
        final Context context = baseFragment.getContext();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context) { // from class: org.telegram.ui.Components.AlertsCreator.4
            @Override // org.telegram.ui.Components.LinkSpanDrawable.LinksTextView, android.widget.TextView
            public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                super.setText(Emoji.replaceEmoji(charSequence, getPaint().getFontMetricsInt(), false), bufferType);
            }
        };
        NotificationCenter.listenEmojiLoading(linksTextView);
        linksTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn));
        linksTextView.setTextSize(1, 16.0f);
        linksTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        FrameLayout frameLayout = new FrameLayout(context);
        builder.setCustomViewOffset(6);
        builder.setView(frameLayout);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(18.0f));
        BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, 22.0f, 5.0f, 22.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        simpleTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
        simpleTextView.setTextSize(20);
        simpleTextView.setTypeface(AndroidUtilities.bold());
        simpleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        simpleTextView.setEllipsizeByGradient(true);
        simpleTextView.setText(user.first_name);
        if (user.verified) {
            Drawable mutate = context.getResources().getDrawable(R.drawable.verified_area).mutate();
            int color = Theme.getColor(Theme.key_chats_verifiedBackground);
            PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
            mutate.setColorFilter(new PorterDuffColorFilter(color, mode));
            Drawable mutate2 = context.getResources().getDrawable(R.drawable.verified_check).mutate();
            mutate2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_verifiedCheck), mode));
            simpleTextView.setRightDrawable(new CombinedDrawable(mutate, mutate2));
        }
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue));
        textView.setTextSize(1, 14.0f);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda62
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createBotLaunchAlert$26(TLRPC.User.this, baseFragment, builder, view);
            }
        });
        SpannableString valueOf = SpannableString.valueOf(LocaleController.getString(R.string.MoreAboutThisBot) + "  ");
        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.attach_arrow_right);
        coloredImageSpan.setTopOffset(1);
        coloredImageSpan.setSize(AndroidUtilities.dp(10.0f));
        valueOf.setSpan(coloredImageSpan, valueOf.length() - 1, valueOf.length(), 33);
        textView.setText(valueOf);
        boolean z = LocaleController.isRTL;
        frameLayout.addView(simpleTextView, LayoutHelper.createFrame(-1, -2.0f, (z ? 5 : 3) | 48, z ? 21 : 76, 0.0f, z ? 76 : 21, 0.0f));
        boolean z2 = LocaleController.isRTL;
        frameLayout.addView(textView, LayoutHelper.createFrame(-1, -2.0f, (z2 ? 5 : 3) | 48, z2 ? 21 : 76, 24.0f, z2 ? 76 : 21, 0.0f));
        frameLayout.addView(linksTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 57.0f, 24.0f, 1.0f));
        if (UserObject.isReplyUser(user)) {
            avatarDrawable.setScaleSize(0.8f);
            avatarDrawable.setAvatarType(12);
            backupImageView.setImage((ImageLocation) null, (String) null, avatarDrawable, user);
        } else {
            avatarDrawable.setScaleSize(1.0f);
            avatarDrawable.setInfo(baseFragment.getCurrentAccount(), user);
            backupImageView.setForUserOrChat(user, avatarDrawable);
        }
        builder.setPositiveButton(LocaleController.getString(R.string.Start), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda63
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createBotLaunchAlert$27(runnable, dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        final AlertDialog create = builder.create();
        baseFragment.showDialog(create, false, new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda64
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AlertsCreator.lambda$createBotLaunchAlert$28(runnable2, dialogInterface);
            }
        });
        linksTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.BotWebViewStartPermission2), new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda65
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.lambda$createBotLaunchAlert$29(AlertDialog.this, context);
            }
        }));
    }

    public static BottomSheet.Builder createCalendarPickerDialog(Context context, final long j, final MessagesStorage.IntCallback intCallback, Theme.ResourcesProvider resourcesProvider) {
        if (context == null) {
            return null;
        }
        final BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        final NumberPicker numberPicker = new NumberPicker(context, resourcesProvider);
        numberPicker.setTextOffset(AndroidUtilities.dp(10.0f));
        numberPicker.setItemCount(5);
        final NumberPicker numberPicker2 = new NumberPicker(context, resourcesProvider);
        numberPicker2.setItemCount(5);
        numberPicker2.setTextOffset(-AndroidUtilities.dp(10.0f));
        final NumberPicker numberPicker3 = new NumberPicker(context, resourcesProvider);
        numberPicker3.setItemCount(5);
        numberPicker3.setTextOffset(-AndroidUtilities.dp(24.0f));
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.47
            boolean ignoreLayout = false;

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                this.ignoreLayout = true;
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i3 = point.x > point.y ? 3 : 5;
                numberPicker.setItemCount(i3);
                numberPicker2.setItemCount(i3);
                numberPicker3.setItemCount(i3);
                numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                numberPicker2.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                numberPicker3.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                this.ignoreLayout = false;
                super.onMeasure(i, i2);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        linearLayout.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString(R.string.ChooseDate));
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda77
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createCalendarPickerDialog$121;
                lambda$createCalendarPickerDialog$121 = AlertsCreator.lambda$createCalendarPickerDialog$121(view, motionEvent);
                return lambda$createCalendarPickerDialog$121;
            }
        });
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setWeightSum(1.0f);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        System.currentTimeMillis();
        TextView textView2 = new TextView(context) { // from class: org.telegram.ui.Components.AlertsCreator.48
            @Override // android.widget.TextView, android.view.View
            public CharSequence getAccessibilityClassName() {
                return Button.class.getName();
            }
        };
        linearLayout2.addView(numberPicker, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.25f));
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(31);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda78
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i) {
                String lambda$createCalendarPickerDialog$122;
                lambda$createCalendarPickerDialog$122 = AlertsCreator.lambda$createCalendarPickerDialog$122(i);
                return lambda$createCalendarPickerDialog$122;
            }
        });
        NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda79
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker4, int i, int i2) {
                AlertsCreator.checkCalendarDate(j, numberPicker, numberPicker2, numberPicker3);
            }
        };
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(11);
        numberPicker2.setWrapSelectorWheel(false);
        linearLayout2.addView(numberPicker2, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.5f));
        numberPicker2.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda80
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i) {
                String lambda$createCalendarPickerDialog$124;
                lambda$createCalendarPickerDialog$124 = AlertsCreator.lambda$createCalendarPickerDialog$124(i);
                return lambda$createCalendarPickerDialog$124;
            }
        });
        numberPicker2.setOnValueChangedListener(onValueChangeListener);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i = calendar.get(1);
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i2 = calendar.get(1);
        numberPicker3.setMinValue(i);
        numberPicker3.setMaxValue(i2);
        numberPicker3.setWrapSelectorWheel(false);
        numberPicker3.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda81
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i3) {
                String lambda$createCalendarPickerDialog$125;
                lambda$createCalendarPickerDialog$125 = AlertsCreator.lambda$createCalendarPickerDialog$125(i3);
                return lambda$createCalendarPickerDialog$125;
            }
        });
        linearLayout2.addView(numberPicker3, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.25f));
        numberPicker3.setOnValueChangedListener(onValueChangeListener);
        numberPicker.setValue(31);
        numberPicker2.setValue(12);
        numberPicker3.setValue(i2);
        checkCalendarDate(j, numberPicker, numberPicker2, numberPicker3);
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView2.setGravity(17);
        textView2.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setText(LocaleController.getString(R.string.JumpToDate));
        textView2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider), Theme.getColor(Theme.key_featuredStickers_addButtonPressed, resourcesProvider)));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda82
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createCalendarPickerDialog$126(j, numberPicker, numberPicker2, numberPicker3, calendar, intCallback, builder, view);
            }
        });
        builder.setCustomView(linearLayout);
        return builder;
    }

    public static void createCallDialogAlert(final BaseFragment baseFragment, final TLRPC.User user, final boolean z) {
        String string;
        String formatString;
        if (baseFragment == null || baseFragment.getParentActivity() == null || user == null || UserObject.isDeleted(user) || UserConfig.getInstance(baseFragment.getCurrentAccount()).getClientUserId() == user.id) {
            return;
        }
        baseFragment.getCurrentAccount();
        Activity parentActivity = baseFragment.getParentActivity();
        FrameLayout frameLayout = new FrameLayout(parentActivity);
        if (z) {
            string = LocaleController.getString(R.string.VideoCallAlertTitle);
            formatString = LocaleController.formatString("VideoCallAlert", R.string.VideoCallAlert, UserObject.getUserName(user));
        } else {
            string = LocaleController.getString(R.string.CallAlertTitle);
            formatString = LocaleController.formatString("CallAlert", R.string.CallAlert, UserObject.getUserName(user));
        }
        TextView textView = new TextView(parentActivity) { // from class: org.telegram.ui.Components.AlertsCreator.12
            @Override // android.widget.TextView
            public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                super.setText(Emoji.replaceEmoji(charSequence, getPaint().getFontMetricsInt(), false), bufferType);
            }
        };
        NotificationCenter.listenEmojiLoading(textView);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        textView.setTextSize(1, 16.0f);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        textView.setText(AndroidUtilities.replaceTags(formatString));
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        avatarDrawable.setScaleSize(1.0f);
        avatarDrawable.setInfo(baseFragment.getCurrentAccount(), user);
        BackupImageView backupImageView = new BackupImageView(parentActivity);
        backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        backupImageView.setForUserOrChat(user, avatarDrawable);
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, 22.0f, 5.0f, 22.0f, 0.0f));
        TextView textView2 = new TextView(parentActivity);
        textView2.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setLines(1);
        textView2.setMaxLines(1);
        textView2.setSingleLine(true);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        textView2.setText(string);
        boolean z2 = LocaleController.isRTL;
        frameLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, (z2 ? 5 : 3) | 48, z2 ? 21 : 76, 11.0f, z2 ? 76 : 21, 0.0f));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 57.0f, 24.0f, 9.0f));
        baseFragment.showDialog(new AlertDialog.Builder(parentActivity).setView(frameLayout).setPositiveButton(LocaleController.getString(R.string.Call), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda11
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createCallDialogAlert$42(BaseFragment.this, user, z, dialogInterface, i);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).create());
    }

    public static void createChangeBioAlert(String str, final long j, final Context context, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString(j > 0 ? R.string.UserBio : R.string.DescriptionPlaceholder));
        builder.setMessage(LocaleController.getString(j > 0 ? R.string.VoipGroupBioEditAlertText : R.string.DescriptionInfo));
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setClipChildren(false);
        if (j < 0) {
            long j2 = -j;
            if (MessagesController.getInstance(i).getChatFull(j2) == null) {
                MessagesController.getInstance(i).loadFullChat(j2, ConnectionsManager.generateClassGuid(), true);
            }
        }
        final NumberTextView numberTextView = new NumberTextView(context);
        final EditText editText = new EditText(context);
        int i2 = Theme.key_voipgroup_actionBarItems;
        editText.setTextColor(Theme.getColor(i2));
        editText.setHint(LocaleController.getString(j > 0 ? R.string.UserBio : R.string.DescriptionPlaceholder));
        editText.setTextSize(1, 16.0f);
        editText.setBackground(Theme.createEditTextDrawable(context, true));
        editText.setMaxLines(4);
        editText.setRawInputType(147457);
        editText.setImeOptions(6);
        final int i3 = j > 0 ? 70 : NotificationCenter.notificationsCountUpdated;
        editText.setFilters(new InputFilter[]{new CodepointsLengthInputFilter(i3) { // from class: org.telegram.ui.Components.AlertsCreator.13
            @Override // org.telegram.ui.Components.CodepointsLengthInputFilter, android.text.InputFilter
            public CharSequence filter(CharSequence charSequence, int i4, int i5, Spanned spanned, int i6, int i7) {
                CharSequence filter = super.filter(charSequence, i4, i5, spanned, i6, i7);
                if (filter != null && charSequence != null && filter.length() != charSequence.length()) {
                    Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
                    if (vibrator != null) {
                        vibrator.vibrate(200L);
                    }
                    AndroidUtilities.shakeView(numberTextView);
                }
                return filter;
            }
        }});
        numberTextView.setCenterAlign(true);
        numberTextView.setTextSize(15);
        numberTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4));
        numberTextView.setImportantForAccessibility(2);
        frameLayout.addView(numberTextView, LayoutHelper.createFrame(20, 20.0f, LocaleController.isRTL ? 3 : 5, 0.0f, 14.0f, 21.0f, 0.0f));
        editText.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(24.0f) : 0, AndroidUtilities.dp(8.0f), LocaleController.isRTL ? 0 : AndroidUtilities.dp(24.0f), AndroidUtilities.dp(8.0f));
        editText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Components.AlertsCreator.14
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                int codePointCount = i3 - Character.codePointCount(editable, 0, editable.length());
                if (codePointCount >= 30) {
                    AndroidUtilities.updateViewVisibilityAnimated(numberTextView, false);
                    return;
                }
                NumberTextView numberTextView2 = numberTextView;
                numberTextView2.setNumber(codePointCount, numberTextView2.getVisibility() == 0);
                AndroidUtilities.updateViewVisibilityAnimated(numberTextView, true);
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
            }
        });
        AndroidUtilities.updateViewVisibilityAnimated(numberTextView, false, 0.0f, false);
        editText.setText(str);
        editText.setSelection(editText.getText().toString().length());
        builder.setView(frameLayout);
        final DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda134
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i4) {
                AlertsCreator.lambda$createChangeBioAlert$44(j, i, editText, dialogInterface, i4);
            }
        };
        builder.setPositiveButton(LocaleController.getString(R.string.Save), onClickListener);
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda135
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AndroidUtilities.hideKeyboard(editText);
            }
        });
        frameLayout.addView(editText, LayoutHelper.createFrame(-1, -2.0f, 0, 23.0f, 12.0f, 23.0f, 21.0f));
        editText.requestFocus();
        AndroidUtilities.showKeyboard(editText);
        final AlertDialog create = builder.create();
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda136
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i4, KeyEvent keyEvent) {
                boolean lambda$createChangeBioAlert$46;
                lambda$createChangeBioAlert$46 = AlertsCreator.lambda$createChangeBioAlert$46(j, create, onClickListener, textView, i4, keyEvent);
                return lambda$createChangeBioAlert$46;
            }
        });
        create.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_dialogBackground));
        create.show();
        create.setTextColor(Theme.getColor(i2));
    }

    public static void createChangeNameAlert(final long j, Context context, final int i) {
        String str;
        String str2;
        final EditText editText;
        if (DialogObject.isUserDialog(j)) {
            TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
            str = user.first_name;
            str2 = user.last_name;
        } else {
            str = MessagesController.getInstance(i).getChat(Long.valueOf(-j)).title;
            str2 = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString(j > 0 ? R.string.VoipEditName : R.string.VoipEditTitle));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        final EditText editText2 = new EditText(context);
        int i2 = Theme.key_voipgroup_actionBarItems;
        editText2.setTextColor(Theme.getColor(i2));
        editText2.setTextSize(1, 16.0f);
        editText2.setMaxLines(1);
        editText2.setLines(1);
        editText2.setSingleLine(true);
        editText2.setGravity(LocaleController.isRTL ? 5 : 3);
        editText2.setInputType(49152);
        editText2.setImeOptions(j > 0 ? 5 : 6);
        editText2.setHint(LocaleController.getString(j > 0 ? R.string.FirstName : R.string.VoipEditTitleHint));
        editText2.setBackground(Theme.createEditTextDrawable(context, true));
        editText2.setPadding(0, AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f));
        editText2.requestFocus();
        if (j > 0) {
            editText = new EditText(context);
            editText.setTextColor(Theme.getColor(i2));
            editText.setTextSize(1, 16.0f);
            editText.setMaxLines(1);
            editText.setLines(1);
            editText.setSingleLine(true);
            editText.setGravity(LocaleController.isRTL ? 5 : 3);
            editText.setInputType(49152);
            editText.setImeOptions(6);
            editText.setHint(LocaleController.getString(R.string.LastName));
            editText.setBackground(Theme.createEditTextDrawable(context, true));
            editText.setPadding(0, AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f));
        } else {
            editText = null;
        }
        AndroidUtilities.showKeyboard(editText2);
        linearLayout.addView(editText2, LayoutHelper.createLinear(-1, -2, 0, 23, 12, 23, 21));
        if (editText != null) {
            linearLayout.addView(editText, LayoutHelper.createLinear(-1, -2, 0, 23, 12, 23, 21));
        }
        editText2.setText(str);
        editText2.setSelection(editText2.getText().toString().length());
        if (editText != null) {
            editText.setText(str2);
            editText.setSelection(editText.getText().toString().length());
        }
        builder.setView(linearLayout);
        final EditText editText3 = editText;
        final DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda125
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                AlertsCreator.lambda$createChangeNameAlert$48(editText2, j, i, editText3, dialogInterface, i3);
            }
        };
        builder.setPositiveButton(LocaleController.getString(R.string.Save), onClickListener);
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda126
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AlertsCreator.lambda$createChangeNameAlert$49(editText2, editText, dialogInterface);
            }
        });
        final AlertDialog create = builder.create();
        create.setBackgroundColor(Theme.getColor(Theme.key_voipgroup_dialogBackground));
        create.show();
        create.setTextColor(Theme.getColor(i2));
        TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda127
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i3, KeyEvent keyEvent) {
                boolean lambda$createChangeNameAlert$50;
                lambda$createChangeNameAlert$50 = AlertsCreator.lambda$createChangeNameAlert$50(AlertDialog.this, onClickListener, textView, i3, keyEvent);
                return lambda$createChangeNameAlert$50;
            }
        };
        if (editText != null) {
            editText.setOnEditorActionListener(onEditorActionListener);
        } else {
            editText2.setOnEditorActionListener(onEditorActionListener);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x0156, code lost:
    
        if (r12 != org.telegram.messenger.UserObject.VERIFY) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0168, code lost:
    
        r5 = new org.telegram.ui.Cells.CheckBoxCell(r8, 1, r32);
        r7[0] = r5;
        r5.setBackgroundDrawable(org.telegram.ui.ActionBar.Theme.getSelectorDrawable(false));
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0179, code lost:
    
        if (r29 == null) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x017b, code lost:
    
        r7[0].setText(org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.DeleteMessagesOptionAlsoChat), "", false, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x019c, code lost:
    
        r2 = r7[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x01a2, code lost:
    
        if (org.telegram.messenger.LocaleController.isRTL == false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x01a4, code lost:
    
        r4 = 16.0f;
        r6 = org.telegram.messenger.AndroidUtilities.dp(16.0f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x01b3, code lost:
    
        if (org.telegram.messenger.LocaleController.isRTL == false) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x01b5, code lost:
    
        r4 = org.telegram.messenger.AndroidUtilities.dp(8.0f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x01c0, code lost:
    
        r2.setPadding(r6, 0, r4, 0);
        r14.addView(r7[0], org.telegram.ui.Components.LayoutHelper.createFrame(-1, 48.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
        r7[0].setChecked(false, false);
        r7[0].setOnClickListener(new org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda66(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01bb, code lost:
    
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01ab, code lost:
    
        r4 = 16.0f;
        r6 = org.telegram.messenger.AndroidUtilities.dp(8.0f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0187, code lost:
    
        r7[0].setText(org.telegram.messenger.LocaleController.formatString("DeleteMessagesOptionAlso", org.telegram.messenger.R.string.DeleteMessagesOptionAlso, org.telegram.messenger.UserObject.getFirstName(r28)), "", false, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0166, code lost:
    
        if (org.telegram.messenger.ChatObject.isChannelAndNotMegaGroup(r29) == false) goto L60;
     */
    /* JADX WARN: Removed duplicated region for block: B:35:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:62:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x015a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void createClearDaysDialogAlert(BaseFragment baseFragment, int i, TLRPC.User user, TLRPC.Chat chat, boolean z, final MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider) {
        int i2;
        CharSequence string;
        final boolean[] zArr;
        CharSequence string2;
        TextView textView;
        String formatString;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (user == null && chat == null) {
            return;
        }
        int currentAccount = baseFragment.getCurrentAccount();
        Activity parentActivity = baseFragment.getParentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, resourcesProvider);
        long clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
        final CheckBoxCell[] checkBoxCellArr = new CheckBoxCell[1];
        TextView textView2 = new TextView(parentActivity) { // from class: org.telegram.ui.Components.AlertsCreator.10
            @Override // android.widget.TextView
            public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                super.setText(Emoji.replaceEmoji(charSequence, getPaint().getFontMetricsInt(), false), bufferType);
            }
        };
        NotificationCenter.listenEmojiLoading(textView2);
        textView2.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        textView2.setTextSize(1, 16.0f);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        FrameLayout frameLayout = new FrameLayout(parentActivity) { // from class: org.telegram.ui.Components.AlertsCreator.11
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i3, int i4) {
                super.onMeasure(i3, i4);
                if (checkBoxCellArr[0] != null) {
                    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + checkBoxCellArr[0].getMeasuredHeight());
                }
            }
        };
        builder.setView(frameLayout);
        TextView textView3 = new TextView(parentActivity);
        textView3.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
        textView3.setTextSize(1, 20.0f);
        textView3.setTypeface(AndroidUtilities.bold());
        textView3.setLines(1);
        textView3.setMaxLines(1);
        textView3.setSingleLine(true);
        textView3.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView3.setEllipsize(TextUtils.TruncateAt.END);
        frameLayout.addView(textView3, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 11.0f, 24.0f, 0.0f));
        frameLayout.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 48.0f, 24.0f, 18.0f));
        if (i == -1) {
            textView3.setText(LocaleController.formatString("ClearHistory", R.string.ClearHistory, new Object[0]));
            if (user != null) {
                formatString = LocaleController.formatString("AreYouSureClearHistoryWithUser", R.string.AreYouSureClearHistoryWithUser, UserObject.getUserName(user));
            } else if (z) {
                formatString = ChatObject.isChannelAndNotMegaGroup(chat) ? LocaleController.formatString("AreYouSureClearHistoryWithChannel", R.string.AreYouSureClearHistoryWithChannel, chat.title) : LocaleController.formatString("AreYouSureClearHistoryWithChat", R.string.AreYouSureClearHistoryWithChat, chat.title);
            } else {
                i2 = chat.megagroup ? R.string.AreYouSureClearHistoryGroup : R.string.AreYouSureClearHistoryChannel;
            }
            string = AndroidUtilities.replaceTags(formatString);
            textView2.setText(string);
            zArr = new boolean[]{false};
            if (chat != null && z && ChatObject.isPublic(chat)) {
                zArr[0] = true;
            }
            if (user != null) {
                long j = user.id;
                if (j != clientUserId) {
                }
            }
            if (chat != null) {
                if (z) {
                    if (!ChatObject.isPublic(chat)) {
                    }
                }
            }
            string2 = LocaleController.getString(R.string.Delete);
            if (chat != null && z && ChatObject.isPublic(chat) && !ChatObject.isChannelAndNotMegaGroup(chat)) {
                string2 = LocaleController.getString(R.string.ClearForAll);
            }
            builder.setPositiveButton(string2, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda67
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    AlertsCreator.lambda$createClearDaysDialogAlert$41(MessagesStorage.BooleanCallback.this, zArr, dialogInterface, i3);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            AlertDialog create = builder.create();
            baseFragment.showDialog(create);
            textView = (TextView) create.getButton(-1);
            if (textView == null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                return;
            }
            return;
        }
        textView3.setText(LocaleController.formatPluralString("DeleteDays", i, new Object[0]));
        i2 = R.string.DeleteHistoryByDaysMessage;
        string = LocaleController.getString(i2);
        textView2.setText(string);
        zArr = new boolean[]{false};
        if (chat != null) {
            zArr[0] = true;
        }
        if (user != null) {
        }
        if (chat != null) {
        }
        string2 = LocaleController.getString(R.string.Delete);
        if (chat != null) {
            string2 = LocaleController.getString(R.string.ClearForAll);
        }
        builder.setPositiveButton(string2, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda67
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                AlertsCreator.lambda$createClearDaysDialogAlert$41(MessagesStorage.BooleanCallback.this, zArr, dialogInterface, i3);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog create2 = builder.create();
        baseFragment.showDialog(create2);
        textView = (TextView) create2.getButton(-1);
        if (textView == null) {
        }
    }

    public static void createClearOrDeleteDialogAlert(BaseFragment baseFragment, boolean z, TLRPC.Chat chat, TLRPC.User user, boolean z2, boolean z3, MessagesStorage.BooleanCallback booleanCallback) {
        createClearOrDeleteDialogAlert(baseFragment, z, false, false, chat, user, z2, false, z3, booleanCallback, null);
    }

    public static void createClearOrDeleteDialogAlert(BaseFragment baseFragment, boolean z, TLRPC.Chat chat, TLRPC.User user, boolean z2, boolean z3, boolean z4, MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider) {
        createClearOrDeleteDialogAlert(baseFragment, z, chat != null && chat.creator, false, chat, user, z2, z3, z4, booleanCallback, resourcesProvider);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r1v31 boolean, still in use, count: 2, list:
          (r1v31 boolean) from 0x0284: IF  (r1v31 boolean) != false  -> B:193:0x0286 A[HIDDEN]
          (r1v31 boolean) from 0x0286: PHI (r1v34 boolean) = (r1v31 boolean), (r1v48 boolean) binds: [B:223:0x0284, B:192:0x0275] A[DONT_GENERATE, DONT_INLINE]
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:114)
        	at jadx.core.dex.visitors.regions.TernaryMod.processRegion(TernaryMod.java:62)
        	at jadx.core.dex.visitors.regions.TernaryMod.visitRegion(TernaryMod.java:53)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:77)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:82)
        */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:103:0x04a6  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x051e  */
    /* JADX WARN: Removed duplicated region for block: B:109:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x04ae  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0399  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x036b  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0377  */
    /* JADX WARN: Type inference failed for: r1v77 */
    /* JADX WARN: Type inference failed for: r1v80, types: [org.telegram.messenger.ImageLocation, java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v82 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void createClearOrDeleteDialogAlert(final org.telegram.ui.ActionBar.BaseFragment r29, final boolean r30, final boolean r31, final boolean r32, final org.telegram.tgnet.TLRPC.Chat r33, final org.telegram.tgnet.TLRPC.User r34, final boolean r35, final boolean r36, final boolean r37, final org.telegram.messenger.MessagesStorage.BooleanCallback r38, final org.telegram.ui.ActionBar.Theme.ResourcesProvider r39) {
        /*
            Method dump skipped, instructions count: 1320
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.AlertsCreator.createClearOrDeleteDialogAlert(org.telegram.ui.ActionBar.BaseFragment, boolean, boolean, boolean, org.telegram.tgnet.TLRPC$Chat, org.telegram.tgnet.TLRPC$User, boolean, boolean, boolean, org.telegram.messenger.MessagesStorage$BooleanCallback, org.telegram.ui.ActionBar.Theme$ResourcesProvider):void");
    }

    public static void createClearOrDeleteDialogsAlert(BaseFragment baseFragment, boolean z, boolean z2, int i, int i2, boolean z3, final MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider) {
        int i3;
        String str;
        int i4;
        String str2;
        int currentAccount = baseFragment.getCurrentAccount();
        Activity parentActivity = baseFragment.getParentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, resourcesProvider);
        UserConfig.getInstance(currentAccount).getClientUserId();
        final CheckBoxCell[] checkBoxCellArr = new CheckBoxCell[1];
        final boolean[] zArr = new boolean[1];
        TextView textView = new TextView(parentActivity);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        textView.setTextSize(1, 16.0f);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        FrameLayout frameLayout = new FrameLayout(parentActivity) { // from class: org.telegram.ui.Components.AlertsCreator.9
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i5, int i6) {
                super.onMeasure(i5, i6);
                if (checkBoxCellArr[0] != null) {
                    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + checkBoxCellArr[0].getMeasuredHeight() + AndroidUtilities.dp(7.0f));
                }
            }
        };
        builder.setCustomViewOffset(6);
        builder.setView(frameLayout);
        TextView textView2 = new TextView(parentActivity);
        textView2.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setLines(1);
        textView2.setMaxLines(1);
        textView2.setSingleLine(true);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        if (z2) {
            if (z3) {
                CheckBoxCell checkBoxCell = new CheckBoxCell(parentActivity, 1, resourcesProvider);
                checkBoxCellArr[0] = checkBoxCell;
                checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                checkBoxCellArr[0].setText(LocaleController.getString(R.string.DeleteMessagesForBothSidesWherePossible), "", false, false);
                checkBoxCellArr[0].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                frameLayout.addView(checkBoxCellArr[0], LayoutHelper.createFrame(-1, 48.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
                checkBoxCellArr[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda53
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AlertsCreator.lambda$createClearOrDeleteDialogsAlert$38(zArr, view);
                    }
                });
            }
            textView2.setText(LocaleController.formatString("DeleteFewChatsTitle", R.string.DeleteFewChatsTitle, LocaleController.formatPluralString("ChatsSelected", i2, new Object[0])));
            i3 = R.string.AreYouSureDeleteFewChats;
            str = "AreYouSureDeleteFewChats";
        } else if (i != 0) {
            textView2.setText(LocaleController.formatString("ClearCacheFewChatsTitle", R.string.ClearCacheFewChatsTitle, LocaleController.formatPluralString("ChatsSelectedClearCache", i2, new Object[0])));
            i3 = R.string.AreYouSureClearHistoryCacheFewChats;
            str = "AreYouSureClearHistoryCacheFewChats";
        } else {
            textView2.setText(LocaleController.formatString("ClearFewChatsTitle", R.string.ClearFewChatsTitle, LocaleController.formatPluralString("ChatsSelectedClear", i2, new Object[0])));
            i3 = R.string.AreYouSureClearHistoryFewChats;
            str = "AreYouSureClearHistoryFewChats";
        }
        textView.setText(LocaleController.getString(str, i3));
        frameLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 11.0f, 24.0f, 0.0f));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 57.0f, 24.0f, 1.0f));
        if (z2) {
            i4 = R.string.Delete;
            str2 = "Delete";
        } else if (i != 0) {
            i4 = R.string.ClearHistoryCache;
            str2 = "ClearHistoryCache";
        } else {
            i4 = R.string.ClearHistory;
            str2 = "ClearHistory";
        }
        builder.setPositiveButton(LocaleController.getString(str2, i4), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda54
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i5) {
                AlertsCreator.lambda$createClearOrDeleteDialogsAlert$39(MessagesStorage.BooleanCallback.this, zArr, dialogInterface, i5);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        AlertDialog create = builder.create();
        baseFragment.showDialog(create);
        TextView textView3 = (TextView) create.getButton(-1);
        if (textView3 != null) {
            textView3.setTextColor(Theme.getColor(Theme.key_text_RedBold));
        }
    }

    public static Dialog createColorSelectDialog(Activity activity, long j, int i, int i2, Runnable runnable) {
        return createColorSelectDialog(activity, j, i, i2, runnable, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x004d, code lost:
    
        if (org.telegram.messenger.DialogObject.isChatDialog(r26) != false) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Dialog createColorSelectDialog(Activity activity, final long j, final long j2, final int i, final Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        String str;
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final String sharedPrefKey = NotificationsController.getSharedPrefKey(j, j2);
        if (j != 0) {
            if (notificationsSettings.contains("color_" + sharedPrefKey)) {
                str = "color_" + sharedPrefKey;
            }
        } else {
            if (i != 1) {
                if (i != 0) {
                    str = i == 3 ? "StoriesLed" : (i == 5 || i == 4) ? "ReactionsLed" : "ChannelLed";
                }
                str = "GroupLed";
            }
            str = "MessagesLed";
        }
        int i2 = notificationsSettings.getInt(str, -16776961);
        final LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        String[] strArr = {LocaleController.getString(R.string.ColorRed), LocaleController.getString(R.string.ColorOrange), LocaleController.getString(R.string.ColorYellow), LocaleController.getString(R.string.ColorGreen), LocaleController.getString(R.string.ColorCyan), LocaleController.getString(R.string.ColorBlue), LocaleController.getString(R.string.ColorViolet), LocaleController.getString(R.string.ColorPink), LocaleController.getString(R.string.ColorWhite)};
        final int[] iArr = {i2};
        for (int i3 = 0; i3 < 9; i3++) {
            RadioColorCell radioColorCell = new RadioColorCell(activity, resourcesProvider);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag(Integer.valueOf(i3));
            int i4 = TextColorCell.colors[i3];
            radioColorCell.setCheckColor(i4, i4);
            radioColorCell.setTextAndValue(strArr[i3], i2 == TextColorCell.colorsToSave[i3]);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda110
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AlertsCreator.lambda$createColorSelectDialog$130(linearLayout, iArr, view);
                }
            });
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.LedColor));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString(R.string.Set), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda111
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i5) {
                AlertsCreator.lambda$createColorSelectDialog$131(j, sharedPrefKey, iArr, j2, i, runnable, dialogInterface, i5);
            }
        });
        builder.setNeutralButton(LocaleController.getString(R.string.LedDisabled), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda112
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i5) {
                AlertsCreator.lambda$createColorSelectDialog$132(j, i, runnable, dialogInterface, i5);
            }
        });
        if (j != 0) {
            builder.setNegativeButton(LocaleController.getString(R.string.Default), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda113
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i5) {
                    AlertsCreator.lambda$createColorSelectDialog$133(sharedPrefKey, runnable, dialogInterface, i5);
                }
            });
        }
        return builder.create();
    }

    public static void createContactInviteDialog(final BaseFragment baseFragment, String str, String str2, final String str3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.ContactNotRegisteredTitle));
        builder.setMessage(LocaleController.formatString("ContactNotRegistered", R.string.ContactNotRegistered, ContactsController.formatName(str, str2)));
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString(R.string.Invite), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda27
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createContactInviteDialog$52(str3, baseFragment, dialogInterface, i);
            }
        });
        baseFragment.showDialog(builder.create());
    }

    public static AlertDialog.Builder createContactsPermissionDialog(Activity activity, final MessagesStorage.IntCallback intCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTopAnimation(R.raw.permission_request_contacts, 72, false, Theme.getColor(Theme.key_dialogTopBackground));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString(R.string.ContactsPermissionAlert)));
        builder.setPositiveButton(LocaleController.getString(R.string.ContactsPermissionAlertContinue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda22
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MessagesStorage.IntCallback.this.run(1);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.ContactsPermissionAlertNotNow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda23
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MessagesStorage.IntCallback.this.run(0);
            }
        });
        return builder;
    }

    public static AlertDialog.Builder createDatePickerDialog(Context context, int i, int i2, int i3, int i4, int i5, int i6, String str, final boolean z, final DatePickerDelegate datePickerDelegate) {
        if (context == null) {
            return null;
        }
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        final NumberPicker numberPicker = new NumberPicker(context);
        final NumberPicker numberPicker2 = new NumberPicker(context);
        final NumberPicker numberPicker3 = new NumberPicker(context);
        linearLayout.addView(numberPicker2, LayoutHelper.createLinear(0, -2, 0.3f));
        numberPicker2.setOnScrollListener(new NumberPicker.OnScrollListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda137
            @Override // org.telegram.ui.Components.NumberPicker.OnScrollListener
            public final void onScrollStateChange(NumberPicker numberPicker4, int i7) {
                AlertsCreator.lambda$createDatePickerDialog$69(z, numberPicker2, numberPicker, numberPicker3, numberPicker4, i7);
            }
        });
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(11);
        linearLayout.addView(numberPicker, LayoutHelper.createLinear(0, -2, 0.3f));
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda138
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i7) {
                String lambda$createDatePickerDialog$70;
                lambda$createDatePickerDialog$70 = AlertsCreator.lambda$createDatePickerDialog$70(i7);
                return lambda$createDatePickerDialog$70;
            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda139
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker4, int i7, int i8) {
                AlertsCreator.updateDayPicker(NumberPicker.this, numberPicker, numberPicker3);
            }
        });
        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda140
            @Override // org.telegram.ui.Components.NumberPicker.OnScrollListener
            public final void onScrollStateChange(NumberPicker numberPicker4, int i7) {
                AlertsCreator.lambda$createDatePickerDialog$72(z, numberPicker2, numberPicker, numberPicker3, numberPicker4, i7);
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i7 = calendar.get(1);
        numberPicker3.setMinValue(i7 + i);
        numberPicker3.setMaxValue(i7 + i2);
        numberPicker3.setValue(i7 + i3);
        linearLayout.addView(numberPicker3, LayoutHelper.createLinear(0, -2, 0.4f));
        numberPicker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda141
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker4, int i8, int i9) {
                AlertsCreator.updateDayPicker(NumberPicker.this, numberPicker, numberPicker3);
            }
        });
        numberPicker3.setOnScrollListener(new NumberPicker.OnScrollListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda142
            @Override // org.telegram.ui.Components.NumberPicker.OnScrollListener
            public final void onScrollStateChange(NumberPicker numberPicker4, int i8) {
                AlertsCreator.lambda$createDatePickerDialog$74(z, numberPicker2, numberPicker, numberPicker3, numberPicker4, i8);
            }
        });
        updateDayPicker(numberPicker2, numberPicker, numberPicker3);
        if (z) {
            checkPickerDate(numberPicker2, numberPicker, numberPicker3);
        }
        if (i4 != -1) {
            numberPicker2.setValue(i4);
            numberPicker.setValue(i5);
            numberPicker3.setValue(i6);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString(R.string.Set), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda143
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i8) {
                AlertsCreator.lambda$createDatePickerDialog$75(z, numberPicker2, numberPicker, numberPicker3, datePickerDelegate, dialogInterface, i8);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        return builder;
    }

    public static BottomSheet.Builder createDatePickerDialog(Context context, String str, String str2, long j, final ScheduleDatePickerDelegate scheduleDatePickerDelegate) {
        LinearLayout linearLayout;
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors();
        final BottomSheet.Builder builder = new BottomSheet.Builder(context, false);
        builder.setApplyBottomPadding(false);
        final NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker.setTextOffset(AndroidUtilities.dp(10.0f));
        numberPicker.setItemCount(5);
        final NumberPicker numberPicker2 = new NumberPicker(context) { // from class: org.telegram.ui.Components.AlertsCreator.27
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i) {
                return LocaleController.formatPluralString("Hours", i, new Object[0]);
            }
        };
        numberPicker2.setItemCount(5);
        numberPicker2.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker2.setTextOffset(-AndroidUtilities.dp(10.0f));
        final NumberPicker numberPicker3 = new NumberPicker(context) { // from class: org.telegram.ui.Components.AlertsCreator.28
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i) {
                return LocaleController.formatPluralString("Minutes", i, new Object[0]);
            }
        };
        numberPicker3.setItemCount(5);
        numberPicker3.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker3.setTextOffset(-AndroidUtilities.dp(34.0f));
        LinearLayout linearLayout2 = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.29
            boolean ignoreLayout = false;

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                this.ignoreLayout = true;
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i3 = point.x > point.y ? 3 : 5;
                numberPicker.setItemCount(i3);
                numberPicker2.setItemCount(i3);
                numberPicker3.setItemCount(i3);
                numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                numberPicker2.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                numberPicker3.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                this.ignoreLayout = false;
                super.onMeasure(i, i2);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        linearLayout2.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(str);
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda144
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createDatePickerDialog$85;
                lambda$createDatePickerDialog$85 = AlertsCreator.lambda$createDatePickerDialog$85(view, motionEvent);
                return lambda$createDatePickerDialog$85;
            }
        });
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setOrientation(0);
        linearLayout3.setWeightSum(1.0f);
        linearLayout2.addView(linearLayout3, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        final long currentTimeMillis = System.currentTimeMillis();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        final int i = calendar.get(1);
        TextView textView2 = new TextView(context) { // from class: org.telegram.ui.Components.AlertsCreator.30
            @Override // android.widget.TextView, android.view.View
            public CharSequence getAccessibilityClassName() {
                return Button.class.getName();
            }
        };
        linearLayout3.addView(numberPicker, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.5f));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(365);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda145
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i2) {
                String lambda$createDatePickerDialog$86;
                lambda$createDatePickerDialog$86 = AlertsCreator.lambda$createDatePickerDialog$86(currentTimeMillis, calendar, i, i2);
                return lambda$createDatePickerDialog$86;
            }
        });
        NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda146
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker4, int i2, int i3) {
                AlertsCreator.checkScheduleDate(null, null, 0, NumberPicker.this, numberPicker2, numberPicker3);
            }
        };
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(23);
        linearLayout3.addView(numberPicker2, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.2f));
        numberPicker2.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda147
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i2) {
                String lambda$createDatePickerDialog$88;
                lambda$createDatePickerDialog$88 = AlertsCreator.lambda$createDatePickerDialog$88(i2);
                return lambda$createDatePickerDialog$88;
            }
        });
        numberPicker2.setOnValueChangedListener(onValueChangeListener);
        numberPicker3.setMinValue(0);
        numberPicker3.setMaxValue(59);
        numberPicker3.setValue(0);
        numberPicker3.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda148
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i2) {
                String lambda$createDatePickerDialog$89;
                lambda$createDatePickerDialog$89 = AlertsCreator.lambda$createDatePickerDialog$89(i2);
                return lambda$createDatePickerDialog$89;
            }
        });
        linearLayout3.addView(numberPicker3, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.3f));
        numberPicker3.setOnValueChangedListener(onValueChangeListener);
        if (j <= 0 || j == 2147483646) {
            linearLayout = linearLayout2;
        } else {
            long j2 = 1000 * j;
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            calendar.set(11, 0);
            linearLayout = linearLayout2;
            int timeInMillis = (int) ((j2 - calendar.getTimeInMillis()) / 86400000);
            calendar.setTimeInMillis(j2);
            if (timeInMillis >= 0) {
                numberPicker3.setValue(calendar.get(12));
                numberPicker2.setValue(calendar.get(11));
                numberPicker.setValue(timeInMillis);
            }
        }
        checkScheduleDate(null, null, 0, numberPicker, numberPicker2, numberPicker3);
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView2.setGravity(17);
        textView2.setTextColor(scheduleDatePickerColors.buttonTextColor);
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), scheduleDatePickerColors.buttonBackgroundColor, scheduleDatePickerColors.buttonBackgroundPressedColor));
        textView2.setText(str2);
        LinearLayout linearLayout4 = linearLayout;
        linearLayout4.addView(textView2, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda149
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createDatePickerDialog$90(NumberPicker.this, numberPicker2, numberPicker3, calendar, scheduleDatePickerDelegate, builder, view);
            }
        });
        builder.setCustomView(linearLayout4);
        BottomSheet show = builder.show();
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    /* JADX WARN: Code restructure failed: missing block: B:176:0x05a3, code lost:
    
        if (r10 == r1) goto L302;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x05f6, code lost:
    
        r0 = org.telegram.messenger.R.string.AreYouSureDeleteFewMessages;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x05f3, code lost:
    
        r0 = org.telegram.messenger.R.string.AreYouSureDeleteSingleMessage;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x05f1, code lost:
    
        if (r10 == r1) goto L302;
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0579  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0601  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0699 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x06f8  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x070a  */
    /* JADX WARN: Removed duplicated region for block: B:132:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x06c7  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x06ca  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x063b  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0586  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void createDeleteMessagesAlert(final BaseFragment baseFragment, final TLRPC.User user, final TLRPC.Chat chat, final TLRPC.EncryptedChat encryptedChat, final TLRPC.ChatFull chatFull, final long j, final MessageObject messageObject, final SparseArray[] sparseArrayArr, final MessageObject.GroupedMessages groupedMessages, final int i, final int i2, TLRPC.ChannelParticipant[] channelParticipantArr, final Runnable runnable, final Runnable runnable2, final Theme.ResourcesProvider resourcesProvider) {
        Activity parentActivity;
        boolean z;
        int i3;
        int i4;
        int i5;
        long j2;
        int i6;
        boolean z2;
        boolean z3;
        TLRPC.Chat chat2;
        int i7;
        float f;
        int dp;
        int i8;
        TLRPC.MessageAction messageAction;
        AlertDialog.Builder builder;
        int i9;
        int i10;
        CharSequence string;
        int i11;
        CharSequence replaceTags;
        String str;
        boolean z4;
        TextView textView;
        TextView textView2;
        int i12;
        float f2;
        int dp2;
        TLRPC.MessageAction messageAction2;
        boolean z5 = i2 == 1;
        boolean z6 = i2 == 3;
        if (baseFragment == null) {
            return;
        }
        if ((user == null && chat == null && encryptedChat == null) || (parentActivity = baseFragment.getParentActivity()) == null) {
            return;
        }
        final int currentAccount = baseFragment.getCurrentAccount();
        AlertDialog.Builder builder2 = new AlertDialog.Builder(parentActivity, resourcesProvider);
        builder2.setDimAlpha(runnable2 != null ? 0.5f : 0.6f);
        int size = groupedMessages != null ? groupedMessages.messages.size() : messageObject != null ? 1 : sparseArrayArr[0].size() + sparseArrayArr[1].size();
        long makeEncryptedDialogId = encryptedChat != null ? DialogObject.makeEncryptedDialogId(encryptedChat.id) : user != null ? user.id : -chat.id;
        int currentTime = ConnectionsManager.getInstance(currentAccount).getCurrentTime();
        if (messageObject == null) {
            int i13 = 0;
            z = false;
            for (int i14 = 2; i13 < i14; i14 = 2) {
                for (int i15 = 0; i15 < sparseArrayArr[i13].size(); i15++) {
                    MessageObject messageObject2 = (MessageObject) sparseArrayArr[i13].valueAt(i15);
                    if (!messageObject2.isDice()) {
                        i4 = 1;
                    } else if (Math.abs(currentTime - messageObject2.messageOwner.date) > 86400) {
                        i4 = 1;
                    }
                    z = true;
                }
                i4 = 1;
                i13 += i4;
            }
            i3 = 1;
        } else if (!messageObject.isDice() || Math.abs(currentTime - messageObject.messageOwner.date) > 86400) {
            i3 = 1;
            z = true;
        } else {
            i3 = 1;
            z = false;
        }
        final boolean[] zArr = new boolean[i3];
        boolean z7 = user != null && MessagesController.getInstance(currentAccount).canRevokePmInbox;
        MessagesController messagesController = MessagesController.getInstance(currentAccount);
        int i16 = user != null ? messagesController.revokeTimePmLimit : messagesController.revokeTimeLimit;
        boolean z8 = encryptedChat == null && user != null && z7 && i16 == Integer.MAX_VALUE;
        if (chat == null || !chat.megagroup || z5 || z6) {
            i5 = currentAccount;
            if (z5 || z6 || ChatObject.isChannel(chat) || encryptedChat != null) {
                j2 = makeEncryptedDialogId;
                i6 = 0;
                z2 = false;
            } else {
                if (user == null || user.id == UserConfig.getInstance(i5).getClientUserId() || (user.bot && !user.support)) {
                    chat2 = chat;
                    if (chat2 == null) {
                        j2 = makeEncryptedDialogId;
                        i7 = 0;
                        z2 = false;
                        if (i7 > 0 || !z || (user != null && UserObject.isDeleted(user))) {
                            i6 = i7;
                        } else {
                            FrameLayout frameLayout = new FrameLayout(parentActivity);
                            CheckBoxCell checkBoxCell = new CheckBoxCell(parentActivity, 1, resourcesProvider);
                            checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            checkBoxCell.setText(z8 ? LocaleController.formatString("DeleteMessagesOptionAlso", R.string.DeleteMessagesOptionAlso, UserObject.getFirstName(user)) : LocaleController.getString((chat2 == null || !(z2 || i7 == size)) ? R.string.DeleteMessagesOption : R.string.DeleteForAll), "", false, false);
                            if (LocaleController.isRTL) {
                                dp = AndroidUtilities.dp(16.0f);
                                f = 8.0f;
                            } else {
                                f = 8.0f;
                                dp = AndroidUtilities.dp(8.0f);
                            }
                            checkBoxCell.setPadding(dp, 0, LocaleController.isRTL ? AndroidUtilities.dp(f) : AndroidUtilities.dp(16.0f), 0);
                            frameLayout.addView(checkBoxCell, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                            checkBoxCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda88
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    AlertsCreator.lambda$createDeleteMessagesAlert$158(zArr, view);
                                }
                            });
                            builder2.setView(frameLayout);
                            builder2.setCustomViewOffset(9);
                            i6 = i7;
                            z3 = true;
                        }
                    }
                } else {
                    chat2 = chat;
                }
                if (messageObject != null) {
                    i7 = (messageObject.isSendError() || !((messageAction = messageObject.messageOwner.action) == null || (messageAction instanceof TLRPC.TL_messageActionEmpty) || (messageAction instanceof TLRPC.TL_messageActionPhoneCall) || (messageAction instanceof TLRPC.TL_messageActionPinMessage) || (messageAction instanceof TLRPC.TL_messageActionGeoProximityReached) || (messageAction instanceof TLRPC.TL_messageActionSetChatTheme)) || (!(messageObject.isOut() || z7 || ChatObject.hasAdminRights(chat)) || currentTime - messageObject.messageOwner.date > i16)) ? 0 : 1;
                    j2 = makeEncryptedDialogId;
                    z2 = !messageObject.isOut();
                } else {
                    int i17 = 0;
                    z2 = false;
                    for (int i18 = 1; i18 >= 0; i18--) {
                        int i19 = 0;
                        while (i19 < sparseArrayArr[i18].size()) {
                            MessageObject messageObject3 = (MessageObject) sparseArrayArr[i18].valueAt(i19);
                            long j3 = makeEncryptedDialogId;
                            TLRPC.MessageAction messageAction3 = messageObject3.messageOwner.action;
                            if ((messageAction3 == null || (messageAction3 instanceof TLRPC.TL_messageActionEmpty) || (messageAction3 instanceof TLRPC.TL_messageActionPhoneCall) || (messageAction3 instanceof TLRPC.TL_messageActionPinMessage) || (messageAction3 instanceof TLRPC.TL_messageActionGeoProximityReached)) && ((messageObject3.isOut() || z7 || (chat2 != null && ChatObject.canBlockUsers(chat))) && currentTime - messageObject3.messageOwner.date <= i16)) {
                                i8 = 1;
                                i17++;
                                if (!z2 && !messageObject3.isOut()) {
                                    z2 = true;
                                }
                            } else {
                                i8 = 1;
                            }
                            i19 += i8;
                            makeEncryptedDialogId = j3;
                        }
                    }
                    j2 = makeEncryptedDialogId;
                    i7 = i17;
                }
                if (i7 > 0) {
                }
                i6 = i7;
            }
            z3 = false;
        } else {
            ArrayList arrayList = new ArrayList();
            if (messageObject != null) {
                TLRPC.MessageAction messageAction4 = messageObject.messageOwner.action;
                if (messageAction4 == null || (messageAction4 instanceof TLRPC.TL_messageActionEmpty) || (messageAction4 instanceof TLRPC.TL_messageActionChatDeleteUser) || (messageAction4 instanceof TLRPC.TL_messageActionChatJoinedByLink) || (messageAction4 instanceof TLRPC.TL_messageActionChatAddUser)) {
                    if (groupedMessages != null) {
                        arrayList.addAll(groupedMessages.messages);
                    } else {
                        arrayList.add(messageObject);
                    }
                }
                i12 = (!messageObject.isSendError() && messageObject.getDialogId() == j && ((messageAction2 = messageObject.messageOwner.action) == null || (messageAction2 instanceof TLRPC.TL_messageActionEmpty)) && messageObject.isOut() && currentTime - messageObject.messageOwner.date <= i16) ? 1 : 0;
            } else {
                i12 = 0;
                for (int i20 = 1; i20 >= 0; i20--) {
                    int i21 = 0;
                    while (i21 < sparseArrayArr[i20].size()) {
                        MessageObject messageObject4 = (MessageObject) sparseArrayArr[i20].valueAt(i21);
                        int i22 = 1;
                        if (i20 == 1 && messageObject4.isOut()) {
                            TLRPC.Message message = messageObject4.messageOwner;
                            if (message.action != null || currentTime - message.date > i16) {
                                i22 = 1;
                            } else {
                                i22 = 1;
                                i12++;
                            }
                        }
                        arrayList.add(messageObject4);
                        i21 += i22;
                    }
                }
            }
            final long clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
            ArrayList arrayList2 = (ArrayList) Collection$-EL.stream(arrayList).mapToLong(new ToLongFunction() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda87
                @Override // j$.util.function.ToLongFunction
                public final long applyAsLong(Object obj) {
                    return ((MessageObject) obj).getFromChatId();
                }
            }).distinct().mapToObj(new LongFunction() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda91
                @Override // j$.util.function.LongFunction
                public final Object apply(long j4) {
                    TLObject lambda$createDeleteMessagesAlert$150;
                    lambda$createDeleteMessagesAlert$150 = AlertsCreator.lambda$createDeleteMessagesAlert$150(currentAccount, j4);
                    return lambda$createDeleteMessagesAlert$150;
                }
            }).filter(new Predicate() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda92
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
                    return AlertsCreator$$ExternalSyntheticBackport1.m((TLObject) obj);
                }
            }).filter(new Predicate() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda93
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
                    boolean lambda$createDeleteMessagesAlert$151;
                    lambda$createDeleteMessagesAlert$151 = AlertsCreator.lambda$createDeleteMessagesAlert$151(clientUserId, (TLObject) obj);
                    return lambda$createDeleteMessagesAlert$151;
                }
            }).collect(Collectors.toCollection(new ChatActivity$$ExternalSyntheticLambda294()));
            if (!arrayList2.isEmpty()) {
                if (channelParticipantArr != null) {
                    DeleteMessagesBottomSheet deleteMessagesBottomSheet = new DeleteMessagesBottomSheet(baseFragment, chat, arrayList, arrayList2, channelParticipantArr, j, i, i2, runnable);
                    if (runnable2 != null) {
                        deleteMessagesBottomSheet.setOnHideListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda96
                            @Override // android.content.DialogInterface.OnDismissListener
                            public final void onDismiss(DialogInterface dialogInterface) {
                                runnable2.run();
                            }
                        });
                    }
                    deleteMessagesBottomSheet.show();
                    return;
                }
                AlertDialog[] alertDialogArr = {new AlertDialog(parentActivity, 3)};
                int size2 = arrayList2.size();
                TLRPC.ChannelParticipant[] channelParticipantArr2 = new TLRPC.ChannelParticipant[size2];
                int[] iArr = new int[size2];
                int[] iArr2 = new int[1];
                int i23 = 0;
                while (i23 < size2) {
                    TLRPC.TL_channels_getParticipant tL_channels_getParticipant = new TLRPC.TL_channels_getParticipant();
                    tL_channels_getParticipant.channel = MessagesController.getInputChannel(chat);
                    tL_channels_getParticipant.participant = MessagesController.getInputPeer((TLObject) arrayList2.get(i23));
                    ConnectionsManager connectionsManager = ConnectionsManager.getInstance(currentAccount);
                    final int[] iArr3 = iArr2;
                    int i24 = currentAccount;
                    final int[] iArr4 = iArr;
                    final int i25 = i23;
                    final TLRPC.ChannelParticipant[] channelParticipantArr3 = channelParticipantArr2;
                    final int i26 = size2;
                    final AlertDialog[] alertDialogArr2 = alertDialogArr;
                    int i27 = i23;
                    int[] iArr5 = iArr;
                    iArr5[i27] = connectionsManager.sendRequest(tL_channels_getParticipant, new RequestDelegate() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda94
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            AlertsCreator.lambda$createDeleteMessagesAlert$153(iArr3, iArr4, i25, channelParticipantArr3, i26, alertDialogArr2, baseFragment, user, chat, encryptedChat, chatFull, j, messageObject, sparseArrayArr, groupedMessages, i, i2, runnable, runnable2, resourcesProvider, tLObject, tL_error);
                        }
                    });
                    i23 = i27 + 1;
                    iArr2 = iArr2;
                    iArr = iArr5;
                    arrayList2 = arrayList2;
                    channelParticipantArr2 = channelParticipantArr2;
                    size2 = size2;
                    alertDialogArr = alertDialogArr;
                    currentAccount = i24;
                }
                final int i28 = currentAccount;
                final int[] iArr6 = iArr;
                final AlertDialog[] alertDialogArr3 = alertDialogArr;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda95
                    @Override // java.lang.Runnable
                    public final void run() {
                        AlertsCreator.lambda$createDeleteMessagesAlert$155(alertDialogArr3, iArr6, i28, runnable2, baseFragment);
                    }
                }, 1000L);
                return;
            }
            i5 = currentAccount;
            if (i12 <= 0 || !z) {
                j2 = makeEncryptedDialogId;
                i6 = i12;
                z2 = false;
                z3 = false;
            } else {
                FrameLayout frameLayout2 = new FrameLayout(parentActivity);
                CheckBoxCell checkBoxCell2 = new CheckBoxCell(parentActivity, 1, resourcesProvider);
                checkBoxCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                checkBoxCell2.setText(LocaleController.getString(R.string.DeleteMessagesOption), "", false, false);
                if (LocaleController.isRTL) {
                    dp2 = AndroidUtilities.dp(16.0f);
                    f2 = 8.0f;
                } else {
                    f2 = 8.0f;
                    dp2 = AndroidUtilities.dp(8.0f);
                }
                checkBoxCell2.setPadding(dp2, 0, LocaleController.isRTL ? AndroidUtilities.dp(f2) : AndroidUtilities.dp(16.0f), 0);
                frameLayout2.addView(checkBoxCell2, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                checkBoxCell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda97
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AlertsCreator.lambda$createDeleteMessagesAlert$157(zArr, view);
                    }
                });
                builder2.setView(frameLayout2);
                builder2.setCustomViewOffset(9);
                j2 = makeEncryptedDialogId;
                i6 = i12;
                z2 = false;
                z3 = true;
            }
        }
        final long j4 = j2;
        final boolean z9 = z6;
        final int i29 = i5;
        int i30 = size;
        int i31 = i6;
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda89
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i32) {
                AlertsCreator.lambda$createDeleteMessagesAlert$159(j4, z9, i29, messageObject, groupedMessages, encryptedChat, j, i, zArr, i2, sparseArrayArr, runnable, dialogInterface, i32);
            }
        };
        if (z6) {
            i9 = 1;
            if (i30 == 1) {
                builder = builder2;
                builder.setTitle(LocaleController.getString(R.string.UnsaveSingleMessagesTitle));
                i10 = 0;
                if (!z6) {
                    i11 = i30 == i9 ? R.string.AreYouSureUnsaveSingleMessage : R.string.AreYouSureUnsaveFewMessages;
                } else if (chat == null || !z2) {
                    if (z3 && !z8 && i31 != i30) {
                        if (chat != null) {
                            int i32 = R.string.DeleteMessagesTextGroup;
                            Object[] objArr = new Object[i9];
                            objArr[i10] = LocaleController.formatPluralString("messages", i31, new Object[i10]);
                            replaceTags = LocaleController.formatString("DeleteMessagesTextGroup", i32, objArr);
                        } else {
                            int i33 = R.string.DeleteMessagesText;
                            String formatPluralString = LocaleController.formatPluralString("messages", i31, new Object[i10]);
                            String firstName = UserObject.getFirstName(user);
                            Object[] objArr2 = new Object[2];
                            objArr2[i10] = formatPluralString;
                            objArr2[i9] = firstName;
                            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("DeleteMessagesText", i33, objArr2));
                        }
                        builder.setMessage(replaceTags);
                        if (messageObject == null) {
                            z4 = messageObject.isGiveaway() && !messageObject.isForwarded();
                            if (z4) {
                                long j5 = ((TLRPC.TL_messageMediaGiveaway) messageObject.messageOwner.media).until_date * 1000;
                                str = LocaleController.getInstance().getFormatterGiveawayMonthDayYear().format(new Date(j5));
                                if (System.currentTimeMillis() < j5) {
                                    z4 = true;
                                }
                                z4 = false;
                            } else {
                                str = null;
                            }
                        } else {
                            str = null;
                            if (i30 == i9) {
                                int i34 = 1;
                                boolean z10 = false;
                                while (i34 >= 0) {
                                    boolean z11 = z10;
                                    for (int i35 = 0; i35 < sparseArrayArr[i34].size(); i35 += i9) {
                                        MessageObject messageObject5 = (MessageObject) sparseArrayArr[i34].valueAt(i35);
                                        boolean z12 = messageObject5.isGiveaway() && !messageObject5.isForwarded();
                                        if (z12) {
                                            long j6 = ((TLRPC.TL_messageMediaGiveaway) messageObject5.messageOwner.media).until_date * 1000;
                                            str = LocaleController.getInstance().getFormatterGiveawayMonthDayYear().format(new Date(j6));
                                            z11 = System.currentTimeMillis() < j6;
                                        } else {
                                            z11 = z12;
                                        }
                                    }
                                    i34--;
                                    z10 = z11;
                                }
                                z4 = z10;
                            }
                            z4 = false;
                        }
                        if (z4 || z6) {
                            builder.setPositiveButton(LocaleController.getString(!z6 ? R.string.Remove : R.string.Delete), onClickListener);
                        } else {
                            builder.setTitle(LocaleController.getString(R.string.BoostingGiveawayDeleteMsgTitle));
                            int i36 = R.string.BoostingGiveawayDeleteMsgText;
                            Object[] objArr3 = new Object[i9];
                            objArr3[i10] = str;
                            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BoostingGiveawayDeleteMsgText", i36, objArr3)));
                            builder.setNeutralButton(LocaleController.getString(R.string.Delete), onClickListener);
                        }
                        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                        builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda90
                            @Override // android.content.DialogInterface.OnDismissListener
                            public final void onDismiss(DialogInterface dialogInterface) {
                                AlertsCreator.lambda$createDeleteMessagesAlert$160(runnable2, dialogInterface);
                            }
                        });
                        AlertDialog create = builder.create();
                        baseFragment.showDialog(create);
                        textView = (TextView) create.getButton(-1);
                        if (textView != null) {
                            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                        }
                        textView2 = (TextView) create.getButton(-3);
                        if (textView2 == null) {
                            create.getButtonsLayout().setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(0.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(12.0f));
                            ((ViewGroup.MarginLayoutParams) create.getButtonsLayout().getLayoutParams()).topMargin = AndroidUtilities.dp(-8.0f);
                            textView2.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                            return;
                        }
                        return;
                    }
                    if (chat != null && chat.megagroup && !z5) {
                        i11 = i30 == i9 ? R.string.AreYouSureDeleteSingleMessageMega : R.string.AreYouSureDeleteFewMessagesMega;
                    }
                } else if (z3 && i31 != i30) {
                    int i37 = R.string.DeleteMessagesTextGroupPart;
                    Object[] objArr4 = new Object[i9];
                    objArr4[i10] = LocaleController.formatPluralString("messages", i31, new Object[i10]);
                    replaceTags = LocaleController.formatString(i37, objArr4);
                    builder.setMessage(replaceTags);
                    if (messageObject == null) {
                    }
                    if (z4) {
                    }
                    builder.setPositiveButton(LocaleController.getString(!z6 ? R.string.Remove : R.string.Delete), onClickListener);
                    builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                    builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda90
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            AlertsCreator.lambda$createDeleteMessagesAlert$160(runnable2, dialogInterface);
                        }
                    });
                    AlertDialog create2 = builder.create();
                    baseFragment.showDialog(create2);
                    textView = (TextView) create2.getButton(-1);
                    if (textView != null) {
                    }
                    textView2 = (TextView) create2.getButton(-3);
                    if (textView2 == null) {
                    }
                }
                replaceTags = LocaleController.getString(i11);
                builder.setMessage(replaceTags);
                if (messageObject == null) {
                }
                if (z4) {
                }
                builder.setPositiveButton(LocaleController.getString(!z6 ? R.string.Remove : R.string.Delete), onClickListener);
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda90
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        AlertsCreator.lambda$createDeleteMessagesAlert$160(runnable2, dialogInterface);
                    }
                });
                AlertDialog create22 = builder.create();
                baseFragment.showDialog(create22);
                textView = (TextView) create22.getButton(-1);
                if (textView != null) {
                }
                textView2 = (TextView) create22.getButton(-3);
                if (textView2 == null) {
                }
            } else {
                builder = builder2;
                i10 = 0;
                string = LocaleController.formatString(R.string.UnsaveMessagesTitle, LocaleController.formatPluralString("messages", i30, new Object[0]));
            }
        } else {
            builder = builder2;
            i9 = 1;
            i10 = 0;
            string = i30 == 1 ? LocaleController.getString(R.string.DeleteSingleMessagesTitle) : LocaleController.formatString(R.string.DeleteMessagesTitle, LocaleController.formatPluralString("messages", i30, new Object[0]));
        }
        builder.setTitle(string);
        if (!z6) {
        }
        replaceTags = LocaleController.getString(i11);
        builder.setMessage(replaceTags);
        if (messageObject == null) {
        }
        if (z4) {
        }
        builder.setPositiveButton(LocaleController.getString(!z6 ? R.string.Remove : R.string.Delete), onClickListener);
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda90
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AlertsCreator.lambda$createDeleteMessagesAlert$160(runnable2, dialogInterface);
            }
        });
        AlertDialog create222 = builder.create();
        baseFragment.showDialog(create222);
        textView = (TextView) create222.getButton(-1);
        if (textView != null) {
        }
        textView2 = (TextView) create222.getButton(-3);
        if (textView2 == null) {
        }
    }

    public static AlertDialog.Builder createDrawOverlayGroupCallPermissionDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String readRes = AndroidUtilities.readRes(R.raw.pip_voice_request);
        final GroupCallPipButton groupCallPipButton = new GroupCallPipButton(context, 0, true);
        groupCallPipButton.setImportantForAccessibility(2);
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.52
            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                groupCallPipButton.setTranslationY((getMeasuredHeight() * 0.28f) - (groupCallPipButton.getMeasuredWidth() / 2.0f));
                groupCallPipButton.setTranslationX((getMeasuredWidth() * 0.82f) - (groupCallPipButton.getMeasuredWidth() / 2.0f));
            }
        };
        frameLayout.setBackground(new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{-15128003, -15118002}));
        frameLayout.setClipToOutline(true);
        frameLayout.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.AlertsCreator.53
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + AndroidUtilities.dp(6.0f), AndroidUtilities.dpf2(6.0f));
            }
        });
        View view = new View(context);
        view.setBackground(new BitmapDrawable(SvgHelper.getBitmap(readRes, AndroidUtilities.dp(320.0f), AndroidUtilities.dp(184.61539f), false)));
        frameLayout.addView(view, LayoutHelper.createFrame(-1, -1.0f, 0, -1.0f, -1.0f, -1.0f, -1.0f));
        frameLayout.addView(groupCallPipButton, LayoutHelper.createFrame(117, 117.0f));
        builder.setTopView(frameLayout);
        builder.setTitle(LocaleController.getString(R.string.PermissionDrawAboveOtherAppsGroupCallTitle));
        builder.setMessage(LocaleController.getString(R.string.PermissionDrawAboveOtherAppsGroupCall));
        builder.setPositiveButton(LocaleController.getString(R.string.Enable), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda57
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createDrawOverlayGroupCallPermissionDialog$140(context, dialogInterface, i);
            }
        });
        builder.notDrawBackgroundOnTopView(true);
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setTopViewAspectRatio(0.5769231f);
        return builder;
    }

    public static AlertDialog.Builder createDrawOverlayPermissionDialog(final Activity activity, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String readRes = AndroidUtilities.readRes(R.raw.pip_video_request);
        FrameLayout frameLayout = new FrameLayout(activity);
        frameLayout.setBackground(new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{-14535089, -14527894}));
        frameLayout.setClipToOutline(true);
        frameLayout.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.AlertsCreator.51
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + AndroidUtilities.dp(6.0f), AndroidUtilities.dpf2(6.0f));
            }
        });
        View view = new View(activity);
        view.setBackground(new BitmapDrawable(SvgHelper.getBitmap(readRes, AndroidUtilities.dp(320.0f), AndroidUtilities.dp(161.36752f), false)));
        frameLayout.addView(view, LayoutHelper.createFrame(-1, -1.0f, 0, -1.0f, -1.0f, -1.0f, -1.0f));
        builder.setTopView(frameLayout);
        builder.setTitle(LocaleController.getString(R.string.PermissionDrawAboveOtherAppsTitle));
        builder.setMessage(LocaleController.getString(R.string.PermissionDrawAboveOtherApps));
        builder.setPositiveButton(LocaleController.getString(R.string.Enable), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda58
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createDrawOverlayPermissionDialog$139(activity, dialogInterface, i);
            }
        });
        builder.notDrawBackgroundOnTopView(true);
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), onClickListener);
        builder.setTopViewAspectRatio(0.50427353f);
        return builder;
    }

    public static Dialog createForgotPasscodeDialog(Context context) {
        return new AlertDialog.Builder(context).setTitle(LocaleController.getString(R.string.ForgotPasscode)).setMessage(LocaleController.getString(R.string.ForgotPasscodeInfo)).setPositiveButton(LocaleController.getString(R.string.Close), null).create();
    }

    public static Dialog createFreeSpaceDialog(final LaunchActivity launchActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(launchActivity);
        builder.setTitle(LocaleController.getString(R.string.LowDiskSpaceTitle));
        builder.setMessage(LocaleController.getString(R.string.LowDiskSpaceMessage2));
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString(R.string.LowDiskSpaceButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda61
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createFreeSpaceDialog$143(LaunchActivity.this, dialogInterface, i);
            }
        });
        return builder.create();
    }

    public static AlertDialog.Builder createGigagroupConvertAlert(Activity activity, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String readRes = AndroidUtilities.readRes(R.raw.gigagroup);
        FrameLayout frameLayout = new FrameLayout(activity);
        if (Build.VERSION.SDK_INT >= 21) {
            frameLayout.setClipToOutline(true);
            frameLayout.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.AlertsCreator.50
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
                }
            });
        }
        View view = new View(activity);
        view.setBackground(new BitmapDrawable(SvgHelper.getBitmap(readRes, AndroidUtilities.dp(320.0f), AndroidUtilities.dp(127.17949f), false)));
        frameLayout.addView(view, LayoutHelper.createFrame(-1, -1.0f, 0, -1.0f, -1.0f, -1.0f, -1.0f));
        builder.setTopView(frameLayout);
        builder.setTopViewAspectRatio(0.3974359f);
        builder.setTitle(LocaleController.getString(R.string.GigagroupAlertTitle));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString(R.string.GigagroupAlertText)));
        builder.setPositiveButton(LocaleController.getString(R.string.GigagroupAlertLearnMore), onClickListener);
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), onClickListener2);
        return builder;
    }

    public static void createImportDialogAlert(BaseFragment baseFragment, String str, String str2, TLRPC.User user, TLRPC.Chat chat, final Runnable runnable) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (chat == null && user == null) {
            return;
        }
        int currentAccount = baseFragment.getCurrentAccount();
        Activity parentActivity = baseFragment.getParentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        long clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
        TextView textView = new TextView(parentActivity);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        textView.setTextSize(1, 16.0f);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        FrameLayout frameLayout = new FrameLayout(parentActivity);
        builder.setView(frameLayout);
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        BackupImageView backupImageView = new BackupImageView(parentActivity);
        backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        frameLayout.addView(backupImageView, LayoutHelper.createFrame(40, 40.0f, (LocaleController.isRTL ? 5 : 3) | 48, 22.0f, 5.0f, 22.0f, 0.0f));
        TextView textView2 = new TextView(parentActivity);
        textView2.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setLines(1);
        textView2.setMaxLines(1);
        textView2.setSingleLine(true);
        textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        textView2.setText(LocaleController.getString(R.string.ImportMessages));
        boolean z = LocaleController.isRTL;
        frameLayout.addView(textView2, LayoutHelper.createFrame(-1, -2.0f, (z ? 5 : 3) | 48, z ? 21 : 76, 11.0f, z ? 76 : 21, 0.0f));
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, 24.0f, 57.0f, 24.0f, 9.0f));
        if (user != null) {
            if (UserObject.isReplyUser(user)) {
                avatarDrawable.setScaleSize(0.8f);
                avatarDrawable.setAvatarType(12);
            } else if (user.id == clientUserId) {
                avatarDrawable.setScaleSize(0.8f);
                avatarDrawable.setAvatarType(1);
            } else {
                avatarDrawable.setScaleSize(1.0f);
                avatarDrawable.setInfo(currentAccount, user);
                backupImageView.setForUserOrChat(user, avatarDrawable);
            }
            backupImageView.setImage((ImageLocation) null, (String) null, avatarDrawable, user);
        } else {
            avatarDrawable.setInfo(currentAccount, chat);
            backupImageView.setForUserOrChat(chat, avatarDrawable);
        }
        textView.setText(AndroidUtilities.replaceTags(str2));
        builder.setPositiveButton(LocaleController.getString(R.string.Import), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda151
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createImportDialogAlert$25(runnable, dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        baseFragment.showDialog(builder.create());
    }

    public static AlertDialog.Builder createLanguageAlert(final LaunchActivity launchActivity, final TLRPC.TL_langPackLanguage tL_langPackLanguage) {
        String formatString;
        int i;
        int i2;
        if (tL_langPackLanguage == null) {
            return null;
        }
        tL_langPackLanguage.lang_code = tL_langPackLanguage.lang_code.replace('-', '_').toLowerCase();
        tL_langPackLanguage.plural_code = tL_langPackLanguage.plural_code.replace('-', '_').toLowerCase();
        String str = tL_langPackLanguage.base_lang_code;
        if (str != null) {
            tL_langPackLanguage.base_lang_code = str.replace('-', '_').toLowerCase();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(launchActivity);
        if (LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals(tL_langPackLanguage.lang_code)) {
            builder.setTitle(LocaleController.getString(R.string.Language));
            formatString = LocaleController.formatString("LanguageSame", R.string.LanguageSame, tL_langPackLanguage.name);
            builder.setNegativeButton(LocaleController.getString(R.string.OK), null);
            builder.setNeutralButton(LocaleController.getString(R.string.SETTINGS), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda55
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    AlertsCreator.lambda$createLanguageAlert$8(LaunchActivity.this, dialogInterface, i3);
                }
            });
        } else {
            if (tL_langPackLanguage.strings_count == 0) {
                builder.setTitle(LocaleController.getString(R.string.LanguageUnknownTitle));
                formatString = LocaleController.formatString("LanguageUnknownCustomAlert", R.string.LanguageUnknownCustomAlert, tL_langPackLanguage.name);
                i = R.string.OK;
            } else {
                builder.setTitle(LocaleController.getString(R.string.LanguageTitle));
                formatString = tL_langPackLanguage.official ? LocaleController.formatString("LanguageAlert", R.string.LanguageAlert, tL_langPackLanguage.name, Integer.valueOf((int) Math.ceil((tL_langPackLanguage.translated_count / tL_langPackLanguage.strings_count) * 100.0f))) : LocaleController.formatString("LanguageCustomAlert", R.string.LanguageCustomAlert, tL_langPackLanguage.name, Integer.valueOf((int) Math.ceil((tL_langPackLanguage.translated_count / tL_langPackLanguage.strings_count) * 100.0f)));
                builder.setPositiveButton(LocaleController.getString(R.string.Change), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda56
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        AlertsCreator.lambda$createLanguageAlert$9(TLRPC.TL_langPackLanguage.this, launchActivity, dialogInterface, i3);
                    }
                });
                i = R.string.Cancel;
            }
            builder.setNegativeButton(LocaleController.getString(i), null);
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(formatString));
        int indexOf = TextUtils.indexOf((CharSequence) spannableStringBuilder, '[');
        if (indexOf != -1) {
            int i3 = indexOf + 1;
            i2 = TextUtils.indexOf((CharSequence) spannableStringBuilder, ']', i3);
            if (i2 != -1) {
                spannableStringBuilder.delete(i2, i2 + 1);
                spannableStringBuilder.delete(indexOf, i3);
            }
        } else {
            i2 = -1;
        }
        if (indexOf != -1 && i2 != -1) {
            spannableStringBuilder.setSpan(new URLSpanNoUnderline(tL_langPackLanguage.translations_url) { // from class: org.telegram.ui.Components.AlertsCreator.1
                @Override // org.telegram.ui.Components.URLSpanNoUnderline, android.text.style.URLSpan, android.text.style.ClickableSpan
                public void onClick(View view) {
                    builder.getDismissRunnable().run();
                    super.onClick(view);
                }
            }, indexOf, i2 - 1, 33);
        }
        TextView textView = new TextView(launchActivity);
        textView.setText(spannableStringBuilder);
        textView.setTextSize(1, 16.0f);
        textView.setLinkTextColor(Theme.getColor(Theme.key_dialogTextLink));
        textView.setHighlightColor(Theme.getColor(Theme.key_dialogLinkSelection));
        textView.setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
        textView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        builder.setView(textView);
        return builder;
    }

    public static Dialog createLocationRequiredDialog(final Context context, boolean z) {
        return new AlertDialog.Builder(context).setMessage(AndroidUtilities.replaceTags(LocaleController.getString(R.string.PermissionNoLocationFriends))).setTopAnimation(R.raw.permission_request_location, 72, false, Theme.getColor(Theme.key_dialogTopBackground)).setPositiveButton(LocaleController.getString(R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda123
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.lambda$createLocationRequiredDialog$0(context, dialogInterface, i);
            }
        }).setNegativeButton(LocaleController.getString(R.string.ContactsPermissionAlertNotNow), null).create();
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x005e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Dialog createLocationUpdateDialog(Activity activity, boolean z, TLRPC.User user, final MessagesStorage.IntCallback intCallback, Theme.ResourcesProvider resourcesProvider) {
        int i;
        String formatString;
        int i2;
        final int[] iArr = new int[1];
        String[] strArr = {LocaleController.getString(R.string.SendLiveLocationFor15m), LocaleController.getString(R.string.SendLiveLocationFor1h), LocaleController.getString(R.string.SendLiveLocationFor8h), LocaleController.getString(R.string.SendLiveLocationForever)};
        final LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, 0, 0, AndroidUtilities.dp(4.0f));
        TextView textView = new TextView(activity);
        if (z) {
            i = R.string.LiveLocationAlertExpandMessage;
        } else {
            if (user != null) {
                formatString = LocaleController.formatString(R.string.LiveLocationAlertPrivate, UserObject.getFirstName(user));
                textView.setText(formatString);
                int i3 = Theme.key_dialogTextBlack;
                textView.setTextColor(resourcesProvider == null ? resourcesProvider.getColorOrDefault(i3) : Theme.getColor(i3));
                textView.setTextSize(1, 16.0f);
                textView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
                linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, !z ? 4 : 0, 24, 8));
                i2 = 0;
                while (i2 < 4) {
                    RadioColorCell radioColorCell = new RadioColorCell(activity, resourcesProvider);
                    radioColorCell.heightDp = 42;
                    radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                    radioColorCell.setTag(Integer.valueOf(i2));
                    int i4 = Theme.key_radioBackground;
                    int colorOrDefault = resourcesProvider != null ? resourcesProvider.getColorOrDefault(i4) : Theme.getColor(i4);
                    int i5 = Theme.key_dialogRadioBackgroundChecked;
                    radioColorCell.setCheckColor(colorOrDefault, resourcesProvider != null ? resourcesProvider.getColorOrDefault(i5) : Theme.getColor(i5));
                    radioColorCell.setTextAndValue(strArr[i2], iArr[0] == i2);
                    linearLayout.addView(radioColorCell);
                    radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda37
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            AlertsCreator.lambda$createLocationUpdateDialog$135(iArr, linearLayout, view);
                        }
                    });
                    i2++;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(activity, resourcesProvider);
                if (z) {
                    builder.setTopImage(new ShareLocationDrawable(activity, 0), resourcesProvider != null ? resourcesProvider.getColorOrDefault(Theme.key_dialogTopBackground) : Theme.getColor(Theme.key_dialogTopBackground));
                } else {
                    builder.setTitle(LocaleController.getString(R.string.LiveLocationAlertExpandTitle));
                }
                builder.setView(linearLayout);
                builder.setPositiveButton(LocaleController.getString(R.string.ShareFile), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda38
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i6) {
                        AlertsCreator.lambda$createLocationUpdateDialog$136(iArr, intCallback, dialogInterface, i6);
                    }
                });
                builder.setNeutralButton(LocaleController.getString(R.string.Cancel), null);
                return builder.create();
            }
            i = R.string.LiveLocationAlertGroup;
        }
        formatString = LocaleController.getString(i);
        textView.setText(formatString);
        int i32 = Theme.key_dialogTextBlack;
        textView.setTextColor(resourcesProvider == null ? resourcesProvider.getColorOrDefault(i32) : Theme.getColor(i32));
        textView.setTextSize(1, 16.0f);
        textView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
        linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, !z ? 4 : 0, 24, 8));
        i2 = 0;
        while (i2 < 4) {
        }
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity, resourcesProvider);
        if (z) {
        }
        builder2.setView(linearLayout);
        builder2.setPositiveButton(LocaleController.getString(R.string.ShareFile), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda38
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                AlertsCreator.lambda$createLocationUpdateDialog$136(iArr, intCallback, dialogInterface, i6);
            }
        });
        builder2.setNeutralButton(LocaleController.getString(R.string.Cancel), null);
        return builder2.create();
    }

    public static BottomSheet createMuteAlert(final BaseFragment baseFragment, final long j, final long j2, final Theme.ResourcesProvider resourcesProvider) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(baseFragment.getParentActivity(), false, resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.Notifications), true);
        int i = R.string.MuteFor;
        builder.setItems(new CharSequence[]{LocaleController.formatString("MuteFor", i, LocaleController.formatPluralString("Hours", 1, new Object[0])), LocaleController.formatString("MuteFor", i, LocaleController.formatPluralString("Hours", 8, new Object[0])), LocaleController.formatString("MuteFor", i, LocaleController.formatPluralString("Days", 2, new Object[0])), LocaleController.getString(R.string.MuteDisable)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda76
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                AlertsCreator.lambda$createMuteAlert$127(j, j2, baseFragment, resourcesProvider, dialogInterface, i2);
            }
        });
        return builder.create();
    }

    public static BottomSheet createMuteAlert(final BaseFragment baseFragment, final ArrayList arrayList, final int i, final Theme.ResourcesProvider resourcesProvider) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(baseFragment.getParentActivity(), false, resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.Notifications), true);
        int i2 = R.string.MuteFor;
        builder.setItems(new CharSequence[]{LocaleController.formatString("MuteFor", i2, LocaleController.formatPluralString("Hours", 1, new Object[0])), LocaleController.formatString("MuteFor", i2, LocaleController.formatPluralString("Hours", 8, new Object[0])), LocaleController.formatString("MuteFor", i2, LocaleController.formatPluralString("Days", 2, new Object[0])), LocaleController.getString(R.string.MuteDisable)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda52
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                AlertsCreator.lambda$createMuteAlert$128(arrayList, i, baseFragment, resourcesProvider, dialogInterface, i3);
            }
        });
        return builder.create();
    }

    public static BottomSheet.Builder createMuteForPickerDialog(Context context, Theme.ResourcesProvider resourcesProvider, final ScheduleDatePickerDelegate scheduleDatePickerDelegate) {
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors(resourcesProvider);
        final BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        final int[] iArr = {30, 60, 120, NotificationCenter.updateBotMenuButton, 480, 1440, 2880, 4320, 5760, 7200, 8640, 10080, 20160, 30240, 44640, 89280, 133920, 178560, 223200, 267840, 525600};
        final NumberPicker numberPicker = new NumberPicker(context, resourcesProvider) { // from class: org.telegram.ui.Components.AlertsCreator.44
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i) {
                int i2 = iArr[i];
                return i2 == 0 ? LocaleController.getString(R.string.MuteNever) : i2 < 60 ? LocaleController.formatPluralString("Minutes", i2, new Object[0]) : i2 < 1440 ? LocaleController.formatPluralString("Hours", i2 / 60, new Object[0]) : i2 < 10080 ? LocaleController.formatPluralString("Days", i2 / 1440, new Object[0]) : i2 < 44640 ? LocaleController.formatPluralString("Weeks", i2 / 10080, new Object[0]) : i2 < 525600 ? LocaleController.formatPluralString("Months", i2 / 44640, new Object[0]) : LocaleController.formatPluralString("Years", i2 / 525600, new Object[0]);
            }
        };
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);
        numberPicker.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker.setValue(0);
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda103
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i) {
                String lambda$createMuteForPickerDialog$117;
                lambda$createMuteForPickerDialog$117 = AlertsCreator.lambda$createMuteForPickerDialog$117(iArr, i);
                return lambda$createMuteForPickerDialog$117;
            }
        });
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.45
            boolean ignoreLayout = false;

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                this.ignoreLayout = true;
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i3 = point.x > point.y ? 3 : 5;
                numberPicker.setItemCount(i3);
                numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                this.ignoreLayout = false;
                super.onMeasure(i, i2);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        linearLayout.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString(R.string.MuteForAlert));
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda104
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createMuteForPickerDialog$118;
                lambda$createMuteForPickerDialog$118 = AlertsCreator.lambda$createMuteForPickerDialog$118(view, motionEvent);
                return lambda$createMuteForPickerDialog$118;
            }
        });
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setWeightSum(1.0f);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        TextView textView2 = new TextView(context) { // from class: org.telegram.ui.Components.AlertsCreator.46
            @Override // android.widget.TextView, android.view.View
            public CharSequence getAccessibilityClassName() {
                return Button.class.getName();
            }
        };
        linearLayout2.addView(numberPicker, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 1.0f));
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda105
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker2, int i, int i2) {
                AlertsCreator.lambda$createMuteForPickerDialog$119(numberPicker2, i, i2);
            }
        });
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView2.setGravity(17);
        textView2.setTextColor(scheduleDatePickerColors.buttonTextColor);
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), scheduleDatePickerColors.buttonBackgroundColor, scheduleDatePickerColors.buttonBackgroundPressedColor));
        textView2.setText(LocaleController.getString(R.string.AutoDeleteConfirm));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda106
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createMuteForPickerDialog$120(iArr, numberPicker, scheduleDatePickerDelegate, builder, view);
            }
        });
        builder.setCustomView(linearLayout);
        BottomSheet show = builder.show();
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    public static AlertDialog.Builder createNoAccessAlert(Context context, String str, String str2, Theme.ResourcesProvider resourcesProvider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);
        HashMap hashMap = new HashMap();
        int i = Theme.key_dialogTopBackground;
        hashMap.put("info1.**", Integer.valueOf(Theme.getColor(i, resourcesProvider)));
        hashMap.put("info2.**", Integer.valueOf(Theme.getColor(i, resourcesProvider)));
        builder.setTopAnimation(R.raw.not_available, 52, false, Theme.getColor(i, resourcesProvider), hashMap);
        builder.setTopAnimationIsNew(true);
        builder.setPositiveButton(LocaleController.getString(R.string.Close), null);
        builder.setMessage(str2);
        return builder;
    }

    public static Dialog createPopupSelectDialog(Activity activity, final int i, final Runnable runnable) {
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] iArr = new int[1];
        if (i == 1) {
            iArr[0] = notificationsSettings.getInt("popupAll", 0);
        } else if (i == 0) {
            iArr[0] = notificationsSettings.getInt("popupGroup", 0);
        } else {
            iArr[0] = notificationsSettings.getInt("popupChannel", 0);
        }
        String[] strArr = {LocaleController.getString(R.string.NoPopup), LocaleController.getString(R.string.OnlyWhenScreenOn), LocaleController.getString(R.string.OnlyWhenScreenOff), LocaleController.getString(R.string.AlwaysShowPopup)};
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        int i2 = 0;
        while (i2 < 4) {
            RadioColorCell radioColorCell = new RadioColorCell(activity);
            radioColorCell.setTag(Integer.valueOf(i2));
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            radioColorCell.setTextAndValue(strArr[i2], iArr[0] == i2);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda107
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AlertsCreator.lambda$createPopupSelectDialog$145(iArr, i, builder, runnable, view);
                }
            });
            i2++;
        }
        builder.setTitle(LocaleController.getString(R.string.PopupNotification));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString(R.string.Cancel), null);
        return builder.create();
    }

    public static Dialog createPrioritySelectDialog(Activity activity, long j, int i, int i2, Runnable runnable) {
        return createPrioritySelectDialog(activity, j, i, i2, runnable, null);
    }

    public static Dialog createPrioritySelectDialog(Activity activity, final long j, final long j2, final int i, final Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        String[] strArr;
        Activity activity2 = activity;
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        int[] iArr = new int[1];
        int i2 = 0;
        if (j != 0) {
            int i3 = notificationsSettings.getInt("priority_" + j, 3);
            iArr[0] = i3;
            if (i3 == 3) {
                iArr[0] = 0;
            } else if (i3 == 4) {
                iArr[0] = 1;
            } else if (i3 == 5) {
                iArr[0] = 2;
            } else if (i3 == 0) {
                iArr[0] = 3;
            } else {
                iArr[0] = 4;
            }
            strArr = new String[]{LocaleController.getString(R.string.NotificationsPrioritySettings), LocaleController.getString(R.string.NotificationsPriorityLow), LocaleController.getString(R.string.NotificationsPriorityMedium), LocaleController.getString(R.string.NotificationsPriorityHigh), LocaleController.getString(R.string.NotificationsPriorityUrgent)};
        } else {
            if (i == 1) {
                iArr[0] = notificationsSettings.getInt("priority_messages", 1);
            } else if (i == 0) {
                iArr[0] = notificationsSettings.getInt("priority_group", 1);
            } else if (i == 2) {
                iArr[0] = notificationsSettings.getInt("priority_channel", 1);
            } else if (i == 3) {
                iArr[0] = notificationsSettings.getInt("priority_stories", 1);
            } else if (i == 4 || i == 5) {
                iArr[0] = notificationsSettings.getInt("priority_react", 1);
            }
            int i4 = iArr[0];
            if (i4 == 4) {
                iArr[0] = 0;
            } else if (i4 == 5) {
                iArr[0] = 1;
            } else if (i4 == 0) {
                iArr[0] = 2;
            } else {
                iArr[0] = 3;
            }
            strArr = new String[]{LocaleController.getString(R.string.NotificationsPriorityLow), LocaleController.getString(R.string.NotificationsPriorityMedium), LocaleController.getString(R.string.NotificationsPriorityHigh), LocaleController.getString(R.string.NotificationsPriorityUrgent)};
        }
        String[] strArr2 = strArr;
        LinearLayout linearLayout = new LinearLayout(activity2);
        linearLayout.setOrientation(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity2, resourcesProvider);
        int i5 = 0;
        while (i5 < strArr2.length) {
            RadioColorCell radioColorCell = new RadioColorCell(activity2, resourcesProvider);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), i2, AndroidUtilities.dp(4.0f), i2);
            radioColorCell.setTag(Integer.valueOf(i5));
            radioColorCell.setCheckColor(Theme.getColor(Theme.key_radioBackground, resourcesProvider), Theme.getColor(Theme.key_dialogRadioBackgroundChecked, resourcesProvider));
            radioColorCell.setTextAndValue(strArr2[i5], iArr[i2] == i5);
            linearLayout.addView(radioColorCell);
            final int[] iArr2 = iArr;
            final AlertDialog.Builder builder2 = builder;
            radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda118
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AlertsCreator.lambda$createPrioritySelectDialog$144(iArr2, j, j2, i, notificationsSettings, builder2, runnable, view);
                }
            });
            i5++;
            activity2 = activity;
            linearLayout = linearLayout;
            builder = builder2;
            strArr2 = strArr2;
            iArr = iArr;
            i2 = 0;
        }
        AlertDialog.Builder builder3 = builder;
        builder3.setTitle(LocaleController.getString(R.string.NotificationsImportance));
        builder3.setView(linearLayout);
        builder3.setPositiveButton(LocaleController.getString(R.string.Cancel), null);
        return builder3.create();
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, long j2, ScheduleDatePickerDelegate scheduleDatePickerDelegate, Runnable runnable) {
        return createScheduleDatePickerDialog(context, j, j2, scheduleDatePickerDelegate, runnable, new ScheduleDatePickerColors(), null);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, long j2, ScheduleDatePickerDelegate scheduleDatePickerDelegate, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        return createScheduleDatePickerDialog(context, j, j2, scheduleDatePickerDelegate, runnable, new ScheduleDatePickerColors(resourcesProvider), resourcesProvider);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, final long j, long j2, final ScheduleDatePickerDelegate scheduleDatePickerDelegate, final Runnable runnable, final ScheduleDatePickerColors scheduleDatePickerColors, Theme.ResourcesProvider resourcesProvider) {
        LinearLayout linearLayout;
        NumberPicker numberPicker;
        final Calendar calendar;
        TLRPC.User user;
        TLRPC.UserStatus userStatus;
        if (context == null) {
            return null;
        }
        final long clientUserId = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
        final BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        final NumberPicker numberPicker2 = new NumberPicker(context, resourcesProvider);
        numberPicker2.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker2.setTextOffset(AndroidUtilities.dp(10.0f));
        numberPicker2.setItemCount(5);
        final NumberPicker numberPicker3 = new NumberPicker(context, resourcesProvider) { // from class: org.telegram.ui.Components.AlertsCreator.23
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i) {
                return LocaleController.formatPluralString("Hours", i, new Object[0]);
            }
        };
        numberPicker3.setWrapSelectorWheel(true);
        numberPicker3.setAllItemsCount(24);
        numberPicker3.setItemCount(5);
        numberPicker3.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker3.setTextOffset(-AndroidUtilities.dp(10.0f));
        final NumberPicker numberPicker4 = new NumberPicker(context, resourcesProvider) { // from class: org.telegram.ui.Components.AlertsCreator.24
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i) {
                return LocaleController.formatPluralString("Minutes", i, new Object[0]);
            }
        };
        numberPicker4.setWrapSelectorWheel(true);
        numberPicker4.setAllItemsCount(60);
        numberPicker4.setItemCount(5);
        numberPicker4.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker4.setTextOffset(-AndroidUtilities.dp(34.0f));
        LinearLayout linearLayout2 = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.25
            boolean ignoreLayout = false;

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                this.ignoreLayout = true;
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i3 = point.x > point.y ? 3 : 5;
                numberPicker2.setItemCount(i3);
                numberPicker3.setItemCount(i3);
                numberPicker4.setItemCount(i3);
                numberPicker2.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                numberPicker3.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                numberPicker4.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                this.ignoreLayout = false;
                super.onMeasure(i, i2);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        linearLayout2.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString(j == clientUserId ? R.string.SetReminder : R.string.ScheduleMessage));
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda13
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createScheduleDatePickerDialog$76;
                lambda$createScheduleDatePickerDialog$76 = AlertsCreator.lambda$createScheduleDatePickerDialog$76(view, motionEvent);
                return lambda$createScheduleDatePickerDialog$76;
            }
        });
        if (!DialogObject.isUserDialog(j) || j == clientUserId || (user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(j))) == null || user.bot || (userStatus = user.status) == null || userStatus.expires <= 0) {
            linearLayout = linearLayout2;
            numberPicker = numberPicker4;
        } else {
            String firstName = UserObject.getFirstName(user);
            if (firstName.length() > 10) {
                firstName = firstName.substring(0, 10) + "…";
            }
            linearLayout = linearLayout2;
            numberPicker = numberPicker4;
            final ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, null, 0, scheduleDatePickerColors.iconColor, false, resourcesProvider);
            actionBarMenuItem.setLongClickEnabled(false);
            actionBarMenuItem.setSubMenuOpenSide(2);
            actionBarMenuItem.setIcon(R.drawable.ic_ab_other);
            actionBarMenuItem.setBackgroundDrawable(Theme.createSelectorDrawable(scheduleDatePickerColors.iconSelectorColor, 1));
            frameLayout.addView(actionBarMenuItem, LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, 8.0f, 5.0f, 0.0f));
            actionBarMenuItem.addSubItem(1, LocaleController.formatString("ScheduleWhenOnline", R.string.ScheduleWhenOnline, firstName));
            actionBarMenuItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda14
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AlertsCreator.lambda$createScheduleDatePickerDialog$77(ActionBarMenuItem.this, scheduleDatePickerColors, view);
                }
            });
            actionBarMenuItem.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda15
                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
                public final void onItemClick(int i) {
                    AlertsCreator.lambda$createScheduleDatePickerDialog$78(AlertsCreator.ScheduleDatePickerDelegate.this, builder, i);
                }
            });
            actionBarMenuItem.setContentDescription(LocaleController.getString(R.string.AccDescrMoreOptions));
        }
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setOrientation(0);
        linearLayout3.setWeightSum(1.0f);
        LinearLayout linearLayout4 = linearLayout;
        linearLayout4.addView(linearLayout3, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        final long currentTimeMillis = System.currentTimeMillis();
        final Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(currentTimeMillis);
        final int i = calendar2.get(1);
        final TextView textView2 = new TextView(context) { // from class: org.telegram.ui.Components.AlertsCreator.26
            @Override // android.widget.TextView, android.view.View
            public CharSequence getAccessibilityClassName() {
                return Button.class.getName();
            }
        };
        linearLayout3.addView(numberPicker2, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.5f));
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(365);
        numberPicker2.setWrapSelectorWheel(false);
        numberPicker2.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda16
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i2) {
                String lambda$createScheduleDatePickerDialog$79;
                lambda$createScheduleDatePickerDialog$79 = AlertsCreator.lambda$createScheduleDatePickerDialog$79(currentTimeMillis, calendar2, i, i2);
                return lambda$createScheduleDatePickerDialog$79;
            }
        });
        final NumberPicker numberPicker5 = numberPicker;
        NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda17
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker6, int i2, int i3) {
                AlertsCreator.lambda$createScheduleDatePickerDialog$80(textView2, clientUserId, j, numberPicker2, numberPicker3, numberPicker5, numberPicker6, i2, i3);
            }
        };
        numberPicker2.setOnValueChangedListener(onValueChangeListener);
        numberPicker3.setMinValue(0);
        numberPicker3.setMaxValue(23);
        linearLayout3.addView(numberPicker3, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.2f));
        numberPicker3.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda18
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i2) {
                String lambda$createScheduleDatePickerDialog$81;
                lambda$createScheduleDatePickerDialog$81 = AlertsCreator.lambda$createScheduleDatePickerDialog$81(i2);
                return lambda$createScheduleDatePickerDialog$81;
            }
        });
        numberPicker3.setOnValueChangedListener(onValueChangeListener);
        numberPicker5.setMinValue(0);
        numberPicker5.setMaxValue(59);
        numberPicker5.setValue(0);
        numberPicker5.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda19
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i2) {
                String lambda$createScheduleDatePickerDialog$82;
                lambda$createScheduleDatePickerDialog$82 = AlertsCreator.lambda$createScheduleDatePickerDialog$82(i2);
                return lambda$createScheduleDatePickerDialog$82;
            }
        });
        linearLayout3.addView(numberPicker5, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.3f));
        numberPicker5.setOnValueChangedListener(onValueChangeListener);
        if (j2 <= 0 || j2 == 2147483646) {
            calendar = calendar2;
        } else {
            long j3 = 1000 * j2;
            calendar = calendar2;
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            calendar.set(11, 0);
            int timeInMillis = (int) ((j3 - calendar.getTimeInMillis()) / 86400000);
            calendar.setTimeInMillis(j3);
            if (timeInMillis >= 0) {
                numberPicker5.setValue(calendar.get(12));
                numberPicker3.setValue(calendar.get(11));
                numberPicker2.setValue(timeInMillis);
            }
        }
        final boolean[] zArr = {true};
        checkScheduleDate(textView2, null, clientUserId == j ? 1 : 0, numberPicker2, numberPicker3, numberPicker5);
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView2.setGravity(17);
        textView2.setTextColor(scheduleDatePickerColors.buttonTextColor);
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setBackground(Theme.AdaptiveRipple.filledRect(scheduleDatePickerColors.buttonBackgroundColor, 8.0f));
        linearLayout4.addView(textView2, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda20
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createScheduleDatePickerDialog$83(zArr, clientUserId, j, numberPicker2, numberPicker3, numberPicker5, calendar, scheduleDatePickerDelegate, builder, view);
            }
        });
        builder.setCustomView(linearLayout4);
        BottomSheet show = builder.show();
        show.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda21
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AlertsCreator.lambda$createScheduleDatePickerDialog$84(runnable, zArr, dialogInterface);
            }
        });
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, ScheduleDatePickerDelegate scheduleDatePickerDelegate) {
        return createScheduleDatePickerDialog(context, j, -1L, scheduleDatePickerDelegate, (Runnable) null);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, ScheduleDatePickerDelegate scheduleDatePickerDelegate, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        return createScheduleDatePickerDialog(context, j, -1L, scheduleDatePickerDelegate, runnable, resourcesProvider);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, ScheduleDatePickerDelegate scheduleDatePickerDelegate, Theme.ResourcesProvider resourcesProvider) {
        return createScheduleDatePickerDialog(context, j, -1L, scheduleDatePickerDelegate, null, resourcesProvider);
    }

    public static BottomSheet.Builder createScheduleDatePickerDialog(Context context, long j, ScheduleDatePickerDelegate scheduleDatePickerDelegate, ScheduleDatePickerColors scheduleDatePickerColors) {
        return createScheduleDatePickerDialog(context, j, -1L, scheduleDatePickerDelegate, null, scheduleDatePickerColors, null);
    }

    public static AlertDialog.Builder createSimpleAlert(Context context, String str) {
        return createSimpleAlert(context, null, str);
    }

    public static AlertDialog.Builder createSimpleAlert(Context context, String str, String str2) {
        return createSimpleAlert(context, str, str2, null);
    }

    public static AlertDialog.Builder createSimpleAlert(Context context, String str, String str2, String str3, final Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        if (context == null || str2 == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (str == null) {
            str = LocaleController.getString(R.string.AppName);
        }
        builder.setTitle(str);
        builder.setMessage(str2);
        if (str3 == null) {
            builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        } else {
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            builder.setPositiveButton(str3, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    AlertsCreator.lambda$createSimpleAlert$10(runnable, dialogInterface, i);
                }
            });
        }
        return builder;
    }

    public static AlertDialog.Builder createSimpleAlert(Context context, String str, String str2, Theme.ResourcesProvider resourcesProvider) {
        return createSimpleAlert(context, str, str2, null, null, resourcesProvider);
    }

    public static ActionBarPopupWindow createSimplePopup(BaseFragment baseFragment, View view, View view2, float f, float f2) {
        if (baseFragment == null || view2 == null || view == null) {
            return null;
        }
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(view, -2, -2);
        actionBarPopupWindow.setPauseNotifications(true);
        actionBarPopupWindow.setDismissAnimationDuration(NotificationCenter.updateAllMessages);
        actionBarPopupWindow.setOutsideTouchable(true);
        actionBarPopupWindow.setClippingEnabled(true);
        actionBarPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
        actionBarPopupWindow.setFocusable(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        actionBarPopupWindow.setInputMethodMode(2);
        actionBarPopupWindow.getContentView().setFocusableInTouchMode(true);
        float f3 = 0.0f;
        View view3 = view2;
        float f4 = 0.0f;
        while (view3 != view2.getRootView()) {
            f4 += view3.getX();
            f3 += view3.getY();
            view3 = (View) view3.getParent();
            if (view3 == null) {
                break;
            }
        }
        actionBarPopupWindow.showAtLocation(view2.getRootView(), 0, (int) ((f4 + f) - (view.getMeasuredWidth() / 2.0f)), (int) ((f3 + f2) - (view.getMeasuredHeight() / 2.0f)));
        actionBarPopupWindow.dimBehind();
        return actionBarPopupWindow;
    }

    public static Dialog createSingleChoiceDialog(Activity activity, String[] strArr, String str, int i, final DialogInterface.OnClickListener onClickListener) {
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        int i2 = 0;
        while (i2 < strArr.length) {
            RadioColorCell radioColorCell = new RadioColorCell(activity);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag(Integer.valueOf(i2));
            radioColorCell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            radioColorCell.setTextAndValue(strArr[i2], i == i2);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda109
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AlertsCreator.lambda$createSingleChoiceDialog$146(AlertDialog.Builder.this, onClickListener, view);
                }
            });
            i2++;
        }
        builder.setTitle(str);
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString(R.string.Cancel), null);
        return builder.create();
    }

    public static BottomSheet.Builder createSoundFrequencyPickerDialog(Context context, int i, int i2, final SoundFrequencyDelegate soundFrequencyDelegate, Theme.ResourcesProvider resourcesProvider) {
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors(resourcesProvider);
        final BottomSheet.Builder builder = new BottomSheet.Builder(context, false, resourcesProvider);
        builder.setApplyBottomPadding(false);
        final NumberPicker numberPicker = new NumberPicker(context, resourcesProvider) { // from class: org.telegram.ui.Components.AlertsCreator.40
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i3) {
                return LocaleController.formatPluralString("Times", i3 + 1, new Object[0]);
            }
        };
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10);
        numberPicker.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker.setValue(i - 1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda153
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i3) {
                String lambda$createSoundFrequencyPickerDialog$111;
                lambda$createSoundFrequencyPickerDialog$111 = AlertsCreator.lambda$createSoundFrequencyPickerDialog$111(i3);
                return lambda$createSoundFrequencyPickerDialog$111;
            }
        });
        final NumberPicker numberPicker2 = new NumberPicker(context, resourcesProvider) { // from class: org.telegram.ui.Components.AlertsCreator.41
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i3) {
                return LocaleController.formatPluralString("Times", i3 + 1, new Object[0]);
            }
        };
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(10);
        numberPicker2.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker2.setValue((i2 / 60) - 1);
        numberPicker2.setWrapSelectorWheel(false);
        numberPicker2.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda154
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i3) {
                String lambda$createSoundFrequencyPickerDialog$112;
                lambda$createSoundFrequencyPickerDialog$112 = AlertsCreator.lambda$createSoundFrequencyPickerDialog$112(i3);
                return lambda$createSoundFrequencyPickerDialog$112;
            }
        });
        final NumberPicker numberPicker3 = new NumberPicker(context, resourcesProvider);
        numberPicker3.setMinValue(0);
        numberPicker3.setMaxValue(0);
        numberPicker3.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker3.setValue(0);
        numberPicker3.setWrapSelectorWheel(false);
        numberPicker3.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda155
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i3) {
                String lambda$createSoundFrequencyPickerDialog$113;
                lambda$createSoundFrequencyPickerDialog$113 = AlertsCreator.lambda$createSoundFrequencyPickerDialog$113(i3);
                return lambda$createSoundFrequencyPickerDialog$113;
            }
        });
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.42
            boolean ignoreLayout = false;

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i3, int i4) {
                this.ignoreLayout = true;
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i5 = point.x > point.y ? 3 : 5;
                numberPicker.setItemCount(i5);
                numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i5;
                numberPicker2.setItemCount(i5);
                numberPicker2.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i5;
                numberPicker3.setItemCount(i5);
                numberPicker3.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i5;
                this.ignoreLayout = false;
                super.onMeasure(i3, i4);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        linearLayout.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString(R.string.NotfificationsFrequencyTitle));
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda156
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createSoundFrequencyPickerDialog$114;
                lambda$createSoundFrequencyPickerDialog$114 = AlertsCreator.lambda$createSoundFrequencyPickerDialog$114(view, motionEvent);
                return lambda$createSoundFrequencyPickerDialog$114;
            }
        });
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout2.setWeightSum(1.0f);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        TextView textView2 = new TextView(context) { // from class: org.telegram.ui.Components.AlertsCreator.43
            @Override // android.widget.TextView, android.view.View
            public CharSequence getAccessibilityClassName() {
                return Button.class.getName();
            }
        };
        linearLayout2.addView(numberPicker, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.4f));
        linearLayout2.addView(numberPicker3, LayoutHelper.createLinear(0, -2, 0.2f, 16));
        linearLayout2.addView(numberPicker2, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.4f));
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView2.setGravity(17);
        textView2.setTextColor(scheduleDatePickerColors.buttonTextColor);
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), scheduleDatePickerColors.buttonBackgroundColor, scheduleDatePickerColors.buttonBackgroundPressedColor));
        textView2.setText(LocaleController.getString(R.string.AutoDeleteConfirm));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda157
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker4, int i3, int i4) {
                AlertsCreator.lambda$createSoundFrequencyPickerDialog$115(numberPicker4, i3, i4);
            }
        };
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        numberPicker2.setOnValueChangedListener(onValueChangeListener);
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda158
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createSoundFrequencyPickerDialog$116(NumberPicker.this, numberPicker2, soundFrequencyDelegate, builder, view);
            }
        });
        builder.setCustomView(linearLayout);
        BottomSheet show = builder.show();
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    public static BottomSheet.Builder createStatusUntilDatePickerDialog(Context context, long j, final StatusUntilDatePickerDelegate statusUntilDatePickerDelegate) {
        LinearLayout linearLayout;
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors();
        final BottomSheet.Builder builder = new BottomSheet.Builder(context, false);
        builder.setApplyBottomPadding(false);
        final NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker.setTextOffset(AndroidUtilities.dp(10.0f));
        numberPicker.setItemCount(5);
        final NumberPicker numberPicker2 = new NumberPicker(context) { // from class: org.telegram.ui.Components.AlertsCreator.33
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i) {
                return LocaleController.formatPluralString("Hours", i, new Object[0]);
            }
        };
        numberPicker2.setItemCount(5);
        numberPicker2.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker2.setTextOffset(-AndroidUtilities.dp(10.0f));
        final NumberPicker numberPicker3 = new NumberPicker(context) { // from class: org.telegram.ui.Components.AlertsCreator.34
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i) {
                return LocaleController.formatPluralString("Minutes", i, new Object[0]);
            }
        };
        numberPicker3.setItemCount(5);
        numberPicker3.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker3.setTextOffset(-AndroidUtilities.dp(34.0f));
        LinearLayout linearLayout2 = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.35
            boolean ignoreLayout = false;

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                this.ignoreLayout = true;
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i3 = point.x > point.y ? 3 : 5;
                numberPicker.setItemCount(i3);
                numberPicker2.setItemCount(i3);
                numberPicker3.setItemCount(i3);
                numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                numberPicker2.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                numberPicker3.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i3;
                this.ignoreLayout = false;
                super.onMeasure(i, i2);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        linearLayout2.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString(R.string.SetEmojiStatusUntilTitle));
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda128
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createStatusUntilDatePickerDialog$101;
                lambda$createStatusUntilDatePickerDialog$101 = AlertsCreator.lambda$createStatusUntilDatePickerDialog$101(view, motionEvent);
                return lambda$createStatusUntilDatePickerDialog$101;
            }
        });
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setOrientation(0);
        linearLayout3.setWeightSum(1.0f);
        linearLayout2.addView(linearLayout3, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        final long currentTimeMillis = System.currentTimeMillis();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        final int i = calendar.get(1);
        final int i2 = calendar.get(6);
        TextView textView2 = new TextView(context) { // from class: org.telegram.ui.Components.AlertsCreator.36
            @Override // android.widget.TextView, android.view.View
            public CharSequence getAccessibilityClassName() {
                return Button.class.getName();
            }
        };
        linearLayout3.addView(numberPicker, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.5f));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(365);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda129
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i3) {
                String lambda$createStatusUntilDatePickerDialog$102;
                lambda$createStatusUntilDatePickerDialog$102 = AlertsCreator.lambda$createStatusUntilDatePickerDialog$102(currentTimeMillis, calendar, i, i2, i3);
                return lambda$createStatusUntilDatePickerDialog$102;
            }
        });
        NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda130
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker4, int i3, int i4) {
                AlertsCreator.checkScheduleDate(null, null, 0, NumberPicker.this, numberPicker2, numberPicker3);
            }
        };
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(23);
        linearLayout3.addView(numberPicker2, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.2f));
        numberPicker2.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda131
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i3) {
                String lambda$createStatusUntilDatePickerDialog$104;
                lambda$createStatusUntilDatePickerDialog$104 = AlertsCreator.lambda$createStatusUntilDatePickerDialog$104(i3);
                return lambda$createStatusUntilDatePickerDialog$104;
            }
        });
        numberPicker2.setOnValueChangedListener(onValueChangeListener);
        numberPicker3.setMinValue(0);
        numberPicker3.setMaxValue(59);
        numberPicker3.setValue(0);
        numberPicker3.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda132
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i3) {
                String lambda$createStatusUntilDatePickerDialog$105;
                lambda$createStatusUntilDatePickerDialog$105 = AlertsCreator.lambda$createStatusUntilDatePickerDialog$105(i3);
                return lambda$createStatusUntilDatePickerDialog$105;
            }
        });
        linearLayout3.addView(numberPicker3, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.3f));
        numberPicker3.setOnValueChangedListener(onValueChangeListener);
        if (j <= 0 || j == 2147483646) {
            linearLayout = linearLayout2;
        } else {
            long j2 = 1000 * j;
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            calendar.set(11, 0);
            linearLayout = linearLayout2;
            int timeInMillis = (int) ((j2 - calendar.getTimeInMillis()) / 86400000);
            calendar.setTimeInMillis(j2);
            if (timeInMillis >= 0) {
                numberPicker3.setValue(calendar.get(12));
                numberPicker2.setValue(calendar.get(11));
                numberPicker.setValue(timeInMillis);
            }
        }
        checkScheduleDate(null, null, 0, numberPicker, numberPicker2, numberPicker3);
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView2.setGravity(17);
        textView2.setTextColor(scheduleDatePickerColors.buttonTextColor);
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), scheduleDatePickerColors.buttonBackgroundColor, scheduleDatePickerColors.buttonBackgroundPressedColor));
        textView2.setText(LocaleController.getString(R.string.SetEmojiStatusUntilButton));
        LinearLayout linearLayout4 = linearLayout;
        linearLayout4.addView(textView2, LayoutHelper.createLinear(-1, 48, 83, 16, 15, 16, 16));
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda133
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createStatusUntilDatePickerDialog$106(NumberPicker.this, numberPicker2, numberPicker3, calendar, statusUntilDatePickerDelegate, builder, view);
            }
        });
        builder.setCustomView(linearLayout4);
        BottomSheet show = builder.show();
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        return builder;
    }

    public static AlertDialog createSupportAlert(final BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(baseFragment.getParentActivity(), baseFragment.getResourceProvider());
        SpannableString spannableString = new SpannableString(Html.fromHtml(LocaleController.getString(R.string.AskAQuestionInfo).replace("\n", "<br>")));
        for (URLSpan uRLSpan : (URLSpan[]) spannableString.getSpans(0, spannableString.length(), URLSpan.class)) {
            int spanStart = spannableString.getSpanStart(uRLSpan);
            int spanEnd = spannableString.getSpanEnd(uRLSpan);
            spannableString.removeSpan(uRLSpan);
            spannableString.setSpan(new URLSpanNoUnderline(uRLSpan.getURL()) { // from class: org.telegram.ui.Components.AlertsCreator.3
                @Override // org.telegram.ui.Components.URLSpanNoUnderline, android.text.style.URLSpan, android.text.style.ClickableSpan
                public void onClick(View view) {
                    baseFragment.dismissCurrentDialog();
                    super.onClick(view);
                }
            }, spanStart, spanEnd, 0);
        }
        linksTextView.setText(spannableString);
        linksTextView.setTextSize(1, 16.0f);
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_dialogTextLink, resourcesProvider));
        linksTextView.setHighlightColor(Theme.getColor(Theme.key_dialogLinkSelection, resourcesProvider));
        linksTextView.setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
        linksTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        linksTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider);
        builder.setView(linksTextView);
        builder.setTitle(LocaleController.getString(R.string.AskAQuestion));
        builder.setPositiveButton(LocaleController.getString(R.string.AskButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda24
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AlertsCreator.performAskAQuestion(BaseFragment.this);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        return builder.create();
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0052, code lost:
    
        if (r2 == 0) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static AlertDialog.Builder createTTLAlert(Context context, final TLRPC.EncryptedChat encryptedChat, Theme.ResourcesProvider resourcesProvider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.MessageLifetime));
        final NumberPicker numberPicker = new NumberPicker(context);
        int i = 0;
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);
        int i2 = encryptedChat.ttl;
        if (i2 > 0 && i2 < 16) {
            numberPicker.setValue(i2);
        } else if (i2 == 30) {
            numberPicker.setValue(16);
        } else {
            if (i2 == 60) {
                i = 17;
            } else if (i2 == 3600) {
                i = 18;
            } else if (i2 == 86400) {
                i = 19;
            } else if (i2 == 604800) {
                numberPicker.setValue(20);
            }
            numberPicker.setValue(i);
        }
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda46
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i3) {
                String lambda$createTTLAlert$147;
                lambda$createTTLAlert$147 = AlertsCreator.lambda$createTTLAlert$147(i3);
                return lambda$createTTLAlert$147;
            }
        });
        builder.setView(numberPicker);
        builder.setNegativeButton(LocaleController.getString(R.string.Done), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda47
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                AlertsCreator.lambda$createTTLAlert$148(TLRPC.EncryptedChat.this, numberPicker, dialogInterface, i3);
            }
        });
        return builder;
    }

    public static void createThemeCreateDialog(final BaseFragment baseFragment, int i, final Theme.ThemeInfo themeInfo, final Theme.ThemeAccent themeAccent) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        Activity parentActivity = baseFragment.getParentActivity();
        final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(parentActivity);
        editTextBoldCursor.setBackground(null);
        editTextBoldCursor.setLineColors(Theme.getColor(Theme.key_dialogInputField), Theme.getColor(Theme.key_dialogInputFieldActivated), Theme.getColor(Theme.key_text_RedBold));
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle(LocaleController.getString(R.string.NewTheme));
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString(R.string.Create), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda48
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                AlertsCreator.lambda$createThemeCreateDialog$161(dialogInterface, i2);
            }
        });
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        builder.setView(linearLayout);
        TextView textView = new TextView(parentActivity);
        textView.setText(i != 0 ? AndroidUtilities.replaceTags(LocaleController.getString(R.string.EnterThemeNameEdit)) : LocaleController.getString(R.string.EnterThemeName));
        textView.setTextSize(1, 16.0f);
        textView.setPadding(AndroidUtilities.dp(23.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(23.0f), AndroidUtilities.dp(6.0f));
        int i2 = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i2));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
        editTextBoldCursor.setTextSize(1, 16.0f);
        editTextBoldCursor.setTextColor(Theme.getColor(i2));
        editTextBoldCursor.setMaxLines(1);
        editTextBoldCursor.setLines(1);
        editTextBoldCursor.setInputType(16385);
        editTextBoldCursor.setGravity(51);
        editTextBoldCursor.setSingleLine(true);
        editTextBoldCursor.setImeOptions(6);
        editTextBoldCursor.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
        editTextBoldCursor.setCursorWidth(1.5f);
        editTextBoldCursor.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
        linearLayout.addView(editTextBoldCursor, LayoutHelper.createLinear(-1, 36, 51, 24, 6, 24, 0));
        editTextBoldCursor.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda49
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView2, int i3, KeyEvent keyEvent) {
                boolean lambda$createThemeCreateDialog$162;
                lambda$createThemeCreateDialog$162 = AlertsCreator.lambda$createThemeCreateDialog$162(textView2, i3, keyEvent);
                return lambda$createThemeCreateDialog$162;
            }
        });
        editTextBoldCursor.setText(generateThemeName(themeAccent));
        editTextBoldCursor.setSelection(editTextBoldCursor.length());
        final AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda50
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                AlertsCreator.lambda$createThemeCreateDialog$164(EditTextBoldCursor.this, dialogInterface);
            }
        });
        baseFragment.showDialog(create);
        editTextBoldCursor.requestFocus();
        create.getButton(-1).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda51
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createThemeCreateDialog$167(BaseFragment.this, editTextBoldCursor, themeAccent, themeInfo, create, view);
            }
        });
    }

    public static BottomSheet createTimePickerDialog(Context context, String str, final int i, final int i2, final int i3, final Utilities.Callback callback) {
        if (context == null) {
            return null;
        }
        ScheduleDatePickerColors scheduleDatePickerColors = new ScheduleDatePickerColors();
        BottomSheet.Builder builder = new BottomSheet.Builder(context, false, null);
        builder.setApplyBottomPadding(false);
        final NumberPicker numberPicker = new NumberPicker(context) { // from class: org.telegram.ui.Components.AlertsCreator.19
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i4) {
                return LocaleController.formatPluralString("Hours", i4, new Object[0]);
            }
        };
        final LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.20
            private Text ampmText;
            private boolean isAM;
            private final Text separatorText = new Text(":", 18.0f);

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                int i4 = Theme.key_windowBackgroundWhiteBlackText;
                this.separatorText.draw(canvas, (getWidth() - this.separatorText.getCurrentWidth()) / 2.0f, getHeight() / 2.0f, Theme.getColor(i4), 1.0f);
                if (!LocaleController.is24HourFormat) {
                    boolean z = numberPicker.getValue() % 24 < 12;
                    if (this.isAM != z || this.ampmText == null) {
                        this.isAM = z;
                        this.ampmText = new Text(z ? "AM" : "PM", 18.0f);
                    }
                    this.ampmText.draw(canvas, (getWidth() / 2.0f) + AndroidUtilities.dp(43.0f), (getHeight() / 2.0f) + AndroidUtilities.dp(1.0f), Theme.getColor(i4), 1.0f);
                }
                super.dispatchDraw(canvas);
            }
        };
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        numberPicker.setAllItemsCount(24);
        numberPicker.setItemCount(5);
        numberPicker.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker.setGravity(5);
        numberPicker.setTextOffset(-AndroidUtilities.dp(12.0f));
        final NumberPicker numberPicker2 = new NumberPicker(context) { // from class: org.telegram.ui.Components.AlertsCreator.21
            @Override // org.telegram.ui.Components.NumberPicker
            protected CharSequence getContentDescription(int i4) {
                return LocaleController.formatPluralString("Minutes", i4, new Object[0]);
            }
        };
        numberPicker2.setWrapSelectorWheel(true);
        numberPicker2.setAllItemsCount(60);
        numberPicker2.setItemCount(5);
        numberPicker2.setTextColor(scheduleDatePickerColors.textColor);
        numberPicker2.setGravity(3);
        numberPicker2.setTextOffset(AndroidUtilities.dp(12.0f));
        final Utilities.Callback callback2 = new Utilities.Callback() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda161
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                AlertsCreator.lambda$createTimePickerDialog$61(i2, i3, numberPicker, numberPicker2, i, linearLayout, (Boolean) obj);
            }
        };
        linearLayout.addView(numberPicker, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.5f));
        numberPicker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda162
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i4) {
                String lambda$createTimePickerDialog$62;
                lambda$createTimePickerDialog$62 = AlertsCreator.lambda$createTimePickerDialog$62(i4);
                return lambda$createTimePickerDialog$62;
            }
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda163
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker3, int i4, int i5) {
                AlertsCreator.lambda$createTimePickerDialog$63(Utilities.Callback.this, numberPicker3, i4, i5);
            }
        });
        linearLayout.addView(numberPicker2, LayoutHelper.createLinear(0, NotificationCenter.attachMenuBotsDidLoad, 0.5f));
        numberPicker2.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda164
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i4) {
                String lambda$createTimePickerDialog$64;
                lambda$createTimePickerDialog$64 = AlertsCreator.lambda$createTimePickerDialog$64(i4);
                return lambda$createTimePickerDialog$64;
            }
        });
        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda165
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker3, int i4, int i5) {
                AlertsCreator.lambda$createTimePickerDialog$65(Utilities.Callback.this, numberPicker3, i4, i5);
            }
        });
        callback2.run(Boolean.FALSE);
        LinearLayout linearLayout2 = new LinearLayout(context) { // from class: org.telegram.ui.Components.AlertsCreator.22
            boolean ignoreLayout = false;

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                this.ignoreLayout = true;
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i6 = point.x > point.y ? 3 : 5;
                numberPicker.setItemCount(i6);
                numberPicker2.setItemCount(i6);
                numberPicker.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i6;
                numberPicker2.getLayoutParams().height = AndroidUtilities.dp(42.0f) * i6;
                this.ignoreLayout = false;
                super.onMeasure(i4, i5);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        linearLayout2.setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        TextView textView = new TextView(context);
        textView.setText(str);
        textView.setTextColor(scheduleDatePickerColors.textColor);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.bold());
        frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 12.0f, 0.0f, 0.0f));
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda166
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createTimePickerDialog$66;
                lambda$createTimePickerDialog$66 = AlertsCreator.lambda$createTimePickerDialog$66(view, motionEvent);
                return lambda$createTimePickerDialog$66;
            }
        });
        linearLayout2.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 51, 22, 0, 0, 4));
        linearLayout2.addView(linearLayout, LayoutHelper.createLinear(-1, -2, 1.0f, 0, 0, 12, 0, 12));
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, null);
        buttonWithCounterView.setText(LocaleController.getString(R.string.Select), false);
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda167
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$createTimePickerDialog$67(r1, view);
            }
        });
        linearLayout2.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 0, 16, 12, 16, 12));
        builder.setCustomView(linearLayout2);
        BottomSheet show = builder.show();
        show.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda168
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AlertsCreator.lambda$createTimePickerDialog$68(Utilities.Callback.this, numberPicker, numberPicker2, dialogInterface);
            }
        });
        show.setBackgroundColor(scheduleDatePickerColors.backgroundColor);
        show.fixNavigationBar(scheduleDatePickerColors.backgroundColor);
        BottomSheet create = builder.create();
        final BottomSheet[] bottomSheetArr = {create};
        return create;
    }

    public static Dialog createVibrationSelectDialog(Activity activity, long j, long j2, String str, Runnable runnable) {
        return createVibrationSelectDialog(activity, j, j2, str, runnable, null);
    }

    public static Dialog createVibrationSelectDialog(Activity activity, final long j, final long j2, final String str, final Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        String[] strArr;
        Activity activity2 = activity;
        final int[] iArr = new int[1];
        int i = 0;
        int i2 = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).getInt(str, 0);
        if (j != 0) {
            iArr[0] = i2;
            if (i2 == 3) {
                iArr[0] = 2;
            } else if (i2 == 2) {
                iArr[0] = 3;
            }
            strArr = new String[]{LocaleController.getString(R.string.VibrationDefault), LocaleController.getString(R.string.Short), LocaleController.getString(R.string.Long), LocaleController.getString(R.string.VibrationDisabled)};
        } else {
            iArr[0] = i2;
            if (i2 == 0) {
                iArr[0] = 1;
            } else if (i2 == 1) {
                iArr[0] = 2;
            } else if (i2 == 2) {
                iArr[0] = 0;
            }
            strArr = new String[]{LocaleController.getString(R.string.VibrationDisabled), LocaleController.getString(R.string.VibrationDefault), LocaleController.getString(R.string.Short), LocaleController.getString(R.string.Long), LocaleController.getString(R.string.OnlyIfSilent)};
        }
        String[] strArr2 = strArr;
        LinearLayout linearLayout = new LinearLayout(activity2);
        linearLayout.setOrientation(1);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity2, resourcesProvider);
        int i3 = 0;
        while (i3 < strArr2.length) {
            RadioColorCell radioColorCell = new RadioColorCell(activity2, resourcesProvider);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), i, AndroidUtilities.dp(4.0f), i);
            radioColorCell.setTag(Integer.valueOf(i3));
            radioColorCell.setCheckColor(Theme.getColor(Theme.key_radioBackground, resourcesProvider), Theme.getColor(Theme.key_dialogRadioBackgroundChecked, resourcesProvider));
            radioColorCell.setTextAndValue(strArr2[i3], iArr[i] == i3);
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda116
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AlertsCreator.lambda$createVibrationSelectDialog$134(iArr, j, str, j2, builder, runnable, view);
                }
            });
            i3++;
            linearLayout = linearLayout;
            i = 0;
            activity2 = activity;
        }
        builder.setTitle(LocaleController.getString(R.string.Vibrate));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString(R.string.Cancel), null);
        return builder.create();
    }

    public static Dialog createVibrationSelectDialog(Activity activity, long j, long j2, boolean z, boolean z2, Runnable runnable, Theme.ResourcesProvider resourcesProvider) {
        String str;
        if (j != 0) {
            str = "vibrate_" + j;
        } else {
            str = z ? "vibrate_group" : "vibrate_messages";
        }
        return createVibrationSelectDialog(activity, j, j2, str, runnable, resourcesProvider);
    }

    public static Dialog createWebViewPermissionsRequestDialog(final Context context, Theme.ResourcesProvider resourcesProvider, String[] strArr, int i, String str, String str2, final Consumer consumer) {
        final boolean z;
        int checkSelfPermission;
        boolean shouldShowRequestPermissionRationale;
        if (strArr != null && (context instanceof Activity) && Build.VERSION.SDK_INT >= 23) {
            Activity activity = (Activity) context;
            for (String str3 : strArr) {
                checkSelfPermission = activity.checkSelfPermission(str3);
                if (checkSelfPermission != 0) {
                    shouldShowRequestPermissionRationale = activity.shouldShowRequestPermissionRationale(str3);
                    if (shouldShowRequestPermissionRationale) {
                        z = true;
                        break;
                    }
                }
            }
        }
        z = false;
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        AlertDialog.Builder topAnimation = new AlertDialog.Builder(context, resourcesProvider).setTopAnimation(i, 72, false, Theme.getColor(Theme.key_dialogTopBackground));
        if (z) {
            str = str2;
        }
        return topAnimation.setMessage(AndroidUtilities.replaceTags(str)).setPositiveButton(LocaleController.getString(z ? R.string.PermissionOpenSettings : R.string.BotWebViewRequestAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda83
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                AlertsCreator.lambda$createWebViewPermissionsRequestDialog$3(z, context, atomicBoolean, consumer, dialogInterface, i2);
            }
        }).setNegativeButton(LocaleController.getString(R.string.BotWebViewRequestDontAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda84
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                AlertsCreator.lambda$createWebViewPermissionsRequestDialog$4(atomicBoolean, consumer, dialogInterface, i2);
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda85
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AlertsCreator.lambda$createWebViewPermissionsRequestDialog$5(atomicBoolean, consumer, dialogInterface);
            }
        }).create();
    }

    private static String generateThemeName(Theme.ThemeAccent themeAccent) {
        int i;
        List asList = Arrays.asList("Ancient", "Antique", "Autumn", "Baby", "Barely", "Baroque", "Blazing", "Blushing", "Bohemian", "Bubbly", "Burning", "Buttered", "Classic", "Clear", "Cool", "Cosmic", "Cotton", "Cozy", "Crystal", "Dark", "Daring", "Darling", "Dawn", "Dazzling", "Deep", "Deepest", "Delicate", "Delightful", "Divine", "Double", "Downtown", "Dreamy", "Dusky", "Dusty", "Electric", "Enchanted", "Endless", "Evening", "Fantastic", "Flirty", "Forever", "Frigid", "Frosty", "Frozen", "Gentle", "Heavenly", "Hyper", "Icy", "Infinite", "Innocent", "Instant", "Luscious", "Lunar", "Lustrous", "Magic", "Majestic", "Mambo", "Midnight", "Millenium", "Morning", "Mystic", "Natural", "Neon", "Night", "Opaque", "Paradise", "Perfect", "Perky", "Polished", "Powerful", "Rich", "Royal", "Sheer", "Simply", "Sizzling", "Solar", "Sparkling", "Splendid", "Spicy", "Spring", "Stellar", "Sugared", "Summer", "Sunny", "Super", "Sweet", "Tender", "Tenacious", "Tidal", "Toasted", "Totally", "Tranquil", "Tropical", "True", "Twilight", "Twinkling", "Ultimate", "Ultra", "Velvety", "Vibrant", "Vintage", "Virtual", "Warm", "Warmest", "Whipped", "Wild", "Winsome");
        List asList2 = Arrays.asList("Ambrosia", "Attack", "Avalanche", "Blast", "Bliss", "Blossom", "Blush", "Burst", "Butter", "Candy", "Carnival", "Charm", "Chiffon", "Cloud", "Comet", "Delight", "Dream", "Dust", "Fantasy", "Flame", "Flash", "Fire", "Freeze", "Frost", "Glade", "Glaze", "Gleam", "Glimmer", "Glitter", "Glow", "Grande", "Haze", "Highlight", "Ice", "Illusion", "Intrigue", "Jewel", "Jubilee", "Kiss", "Lights", "Lollypop", "Love", "Luster", "Madness", "Matte", "Mirage", "Mist", "Moon", "Muse", "Myth", "Nectar", "Nova", "Parfait", "Passion", "Pop", "Rain", "Reflection", "Rhapsody", "Romance", "Satin", "Sensation", "Silk", "Shine", "Shadow", "Shimmer", "Sky", "Spice", "Star", "Sugar", "Sunrise", "Sunset", "Sun", "Twist", "Unbound", "Velvet", "Vibrant", "Waters", "Wine", "Wink", "Wonder", "Zone");
        HashMap hashMap = new HashMap();
        hashMap.put(9306112, "Berry");
        hashMap.put(14598550, "Brandy");
        hashMap.put(8391495, "Cherry");
        hashMap.put(16744272, "Coral");
        hashMap.put(14372985, "Cranberry");
        hashMap.put(14423100, "Crimson");
        hashMap.put(14725375, "Mauve");
        hashMap.put(16761035, "Pink");
        hashMap.put(16711680, "Red");
        hashMap.put(16711807, "Rose");
        hashMap.put(8406555, "Russet");
        hashMap.put(16720896, "Scarlet");
        hashMap.put(15856113, "Seashell");
        hashMap.put(16724889, "Strawberry");
        hashMap.put(16760576, "Amber");
        hashMap.put(15438707, "Apricot");
        hashMap.put(16508850, "Banana");
        hashMap.put(10601738, "Citrus");
        hashMap.put(11560192, "Ginger");
        hashMap.put(16766720, "Gold");
        hashMap.put(16640272, "Lemon");
        hashMap.put(16753920, "Orange");
        hashMap.put(16770484, "Peach");
        hashMap.put(16739155, "Persimmon");
        hashMap.put(14996514, "Sunflower");
        hashMap.put(15893760, "Tangerine");
        hashMap.put(16763004, "Topaz");
        hashMap.put(16776960, "Yellow");
        hashMap.put(3688720, "Clover");
        hashMap.put(8628829, "Cucumber");
        hashMap.put(5294200, "Emerald");
        hashMap.put(11907932, "Olive");
        hashMap.put(65280, "Green");
        hashMap.put(43115, "Jade");
        hashMap.put(2730887, "Jungle");
        hashMap.put(12582656, "Lime");
        hashMap.put(776785, "Malachite");
        hashMap.put(10026904, "Mint");
        hashMap.put(11394989, "Moss");
        hashMap.put(3234721, "Azure");
        hashMap.put(Integer.valueOf(NotificationCenter.notificationsCountUpdated), "Blue");
        hashMap.put(18347, "Cobalt");
        hashMap.put(5204422, "Indigo");
        hashMap.put(96647, "Lagoon");
        hashMap.put(7461346, "Aquamarine");
        hashMap.put(1182351, "Ultramarine");
        hashMap.put(128, "Navy");
        hashMap.put(3101086, "Sapphire");
        hashMap.put(7788522, "Sky");
        hashMap.put(32896, "Teal");
        hashMap.put(4251856, "Turquoise");
        hashMap.put(10053324, "Amethyst");
        hashMap.put(5046581, "Blackberry");
        hashMap.put(6373457, "Eggplant");
        hashMap.put(13148872, "Lilac");
        hashMap.put(11894492, "Lavender");
        hashMap.put(13421823, "Periwinkle");
        hashMap.put(8663417, "Plum");
        hashMap.put(6684825, "Purple");
        hashMap.put(14204888, "Thistle");
        hashMap.put(14315734, "Orchid");
        hashMap.put(2361920, "Violet");
        hashMap.put(4137225, "Bronze");
        hashMap.put(3604994, "Chocolate");
        hashMap.put(8077056, "Cinnamon");
        hashMap.put(3153694, "Cocoa");
        hashMap.put(7365973, "Coffee");
        hashMap.put(7956873, "Rum");
        hashMap.put(5113350, "Mahogany");
        hashMap.put(7875865, "Mocha");
        hashMap.put(12759680, "Sand");
        hashMap.put(8924439, "Sienna");
        hashMap.put(7864585, "Maple");
        hashMap.put(15787660, "Khaki");
        hashMap.put(12088115, "Copper");
        hashMap.put(12144200, "Chestnut");
        hashMap.put(15653316, "Almond");
        hashMap.put(16776656, "Cream");
        hashMap.put(12186367, "Diamond");
        hashMap.put(11109127, "Honey");
        hashMap.put(16777200, "Ivory");
        hashMap.put(15392968, "Pearl");
        hashMap.put(15725299, "Porcelain");
        hashMap.put(13745832, "Vanilla");
        hashMap.put(16777215, "White");
        hashMap.put(8421504, "Gray");
        hashMap.put(0, "Black");
        hashMap.put(15266260, "Chrome");
        hashMap.put(3556687, "Charcoal");
        hashMap.put(789277, "Ebony");
        hashMap.put(12632256, "Silver");
        hashMap.put(16119285, "Smoke");
        hashMap.put(2499381, "Steel");
        hashMap.put(5220413, "Apple");
        hashMap.put(8434628, "Glacier");
        hashMap.put(16693933, "Melon");
        hashMap.put(12929932, "Mulberry");
        hashMap.put(11126466, "Opal");
        hashMap.put(5547512, "Blue");
        Theme.ThemeAccent accent = themeAccent == null ? Theme.getCurrentTheme().getAccent(false) : themeAccent;
        if (accent == null || (i = accent.accentColor) == 0) {
            i = AndroidUtilities.calcDrawableColor(Theme.getCachedWallpaper())[0];
        }
        int red = Color.red(i);
        int green = Color.green(i);
        int blue = Color.blue(i);
        String str = null;
        int i2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        for (Map.Entry entry : hashMap.entrySet()) {
            Integer num = (Integer) entry.getKey();
            int red2 = Color.red(num.intValue());
            int i3 = (red + red2) / 2;
            int i4 = red - red2;
            int green2 = green - Color.green(num.intValue());
            int blue2 = blue - Color.blue(num.intValue());
            int i5 = ((((767 - i3) * blue2) * blue2) >> 8) + ((((i3 + 512) * i4) * i4) >> 8) + (green2 * 4 * green2);
            if (i5 < i2) {
                str = (String) entry.getValue();
                i2 = i5;
            }
        }
        if (Utilities.random.nextInt() % 2 == 0) {
            return ((String) asList.get(Utilities.random.nextInt(asList.size()))) + " " + str;
        }
        return str + " " + ((String) asList2.get(Utilities.random.nextInt(asList2.size())));
    }

    private static String getFloodWaitString(String str) {
        int intValue = Utilities.parseInt((CharSequence) str).intValue();
        return LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, intValue < 60 ? LocaleController.formatPluralString("Seconds", intValue, new Object[0]) : LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkRestrictedInviteUsers$53(int i, TLRPC.Chat chat, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
        BaseFragment lastFragment;
        if (!LaunchActivity.isActive || (lastFragment = LaunchActivity.getLastFragment()) == null || lastFragment.getParentActivity() == null) {
            return;
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(lastFragment, lastFragment.getParentActivity(), 11, i, null);
        limitReachedBottomSheet.setRestrictedUsers(chat, arrayList, arrayList2, arrayList3);
        limitReachedBottomSheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createAccountSelectDialog$149(AlertDialog[] alertDialogArr, Runnable runnable, AccountSelectDelegate accountSelectDelegate, View view) {
        AlertDialog alertDialog = alertDialogArr[0];
        if (alertDialog != null) {
            alertDialog.setOnDismissListener(null);
        }
        runnable.run();
        accountSelectDelegate.didSelectAccount(((AccountSelectCell) view).getAccountNumber());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createApkRestrictedDialog$6(Context context, DialogInterface dialogInterface, int i) {
        try {
            context.startActivity(new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES", Uri.parse("package:" + context.getPackageName())));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createAutoDeleteDatePickerDialog$107(int[] iArr, int i) {
        int i2 = iArr[i];
        return i2 == 0 ? LocaleController.getString(R.string.AutoDeleteNever) : i2 < 10080 ? LocaleController.formatPluralString("Days", i2 / 1440, new Object[0]) : i2 < 44640 ? LocaleController.formatPluralString("Weeks", i2 / 10080, new Object[0]) : i2 < 525600 ? LocaleController.formatPluralString("Months", i2 / 44640, new Object[0]) : LocaleController.formatPluralString("Years", i2 / 525600, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createAutoDeleteDatePickerDialog$108(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createAutoDeleteDatePickerDialog$109(AnimatedTextView animatedTextView, NumberPicker numberPicker, int i, int i2) {
        try {
            animatedTextView.setText(LocaleController.getString(i2 == 0 ? R.string.DisableAutoDeleteTimer : R.string.SetAutoDeleteTimer));
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createAutoDeleteDatePickerDialog$110(int[] iArr, NumberPicker numberPicker, ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, View view) {
        scheduleDatePickerDelegate.didSelectDate(true, iArr[numberPicker.getValue()]);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBackgroundActivityDialog$1(Context context, DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBackgroundLocationPermissionDialog$137(Activity activity, DialogInterface dialogInterface, int i) {
        int checkSelfPermission;
        checkSelfPermission = activity.checkSelfPermission("android.permission.ACCESS_BACKGROUND_LOCATION");
        if (checkSelfPermission != 0) {
            activity.requestPermissions(new String[]{"android.permission.ACCESS_BACKGROUND_LOCATION"}, 30);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBirthdayPickerDialog$100(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, int i, BottomSheet.Builder builder, Utilities.Callback callback, View view) {
        TLRPC.TL_birthday tL_birthday = new TLRPC.TL_birthday();
        tL_birthday.day = numberPicker.getValue();
        tL_birthday.month = numberPicker2.getValue() + 1;
        if (numberPicker3.getValue() != i) {
            tL_birthday.flags |= 1;
            tL_birthday.year = numberPicker3.getValue();
        }
        builder.getDismissRunnable().run();
        callback.run(tL_birthday);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createBirthdayPickerDialog$91(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBirthdayPickerDialog$92(NumberPicker numberPicker, int i, NumberPicker numberPicker2, NumberPicker numberPicker3, int i2, int i3, int i4) {
        try {
            if (numberPicker.getValue() == i) {
                numberPicker2.setMinValue(1);
                numberPicker2.setMaxValue(YearMonth.of(2024, numberPicker3.getValue() + 1).lengthOfMonth());
            } else {
                if (numberPicker.getValue() == i2) {
                    numberPicker3.setMinValue(0);
                    numberPicker3.setMaxValue(i3);
                    int value = numberPicker3.getValue();
                    numberPicker2.setMinValue(1);
                    if (value == i3) {
                        numberPicker2.setMaxValue(i4);
                        return;
                    }
                    try {
                        numberPicker2.setMaxValue(YearMonth.of(numberPicker.getValue(), numberPicker3.getValue() + 1).lengthOfMonth());
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        numberPicker2.setMaxValue(31);
                        return;
                    }
                }
                numberPicker2.setMinValue(1);
                numberPicker2.setMaxValue(YearMonth.of(numberPicker.getValue(), numberPicker3.getValue() + 1).lengthOfMonth());
            }
        } catch (Exception e2) {
            FileLog.e(e2);
            numberPicker2.setMaxValue(31);
        }
        numberPicker3.setMinValue(0);
        numberPicker3.setMaxValue(11);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createBirthdayPickerDialog$93(int i) {
        return "" + i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBirthdayPickerDialog$94(Runnable runnable, NumberPicker numberPicker, int i) {
        if (i == 0) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createBirthdayPickerDialog$95(int i) {
        int i2;
        switch (i) {
            case 0:
                i2 = R.string.January;
                break;
            case 1:
                i2 = R.string.February;
                break;
            case 2:
                i2 = R.string.March;
                break;
            case 3:
                i2 = R.string.April;
                break;
            case 4:
                i2 = R.string.May;
                break;
            case 5:
                i2 = R.string.June;
                break;
            case 6:
                i2 = R.string.July;
                break;
            case 7:
                i2 = R.string.August;
                break;
            case 8:
                i2 = R.string.September;
                break;
            case 9:
                i2 = R.string.October;
                break;
            case 10:
                i2 = R.string.November;
                break;
            default:
                i2 = R.string.December;
                break;
        }
        return LocaleController.getString(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createBirthdayPickerDialog$96(int i, int i2) {
        return i2 == i ? "—" : String.format("%02d", Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBirthdayPickerDialog$97(ArrayList arrayList) {
        BaseFragment lastFragment;
        if (arrayList == null || (lastFragment = LaunchActivity.getLastFragment()) == null) {
            return;
        }
        BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
        bottomSheetParams.transitionFromLeft = true;
        bottomSheetParams.allowNestedScroll = false;
        lastFragment.showAsSheet(new PrivacyControlActivity(11), bottomSheetParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBirthdayPickerDialog$98(int i, LinkSpanDrawable.LinksTextView linksTextView) {
        final ArrayList<TLRPC.PrivacyRule> privacyRules = ContactsController.getInstance(i).getPrivacyRules(11);
        String string = LocaleController.getString(R.string.EditProfileBirthdayInfoContacts);
        if (privacyRules != null && !privacyRules.isEmpty()) {
            int i2 = 0;
            while (true) {
                if (i2 >= privacyRules.size()) {
                    break;
                }
                if (privacyRules.get(i2) instanceof TLRPC.TL_privacyValueAllowContacts) {
                    string = LocaleController.getString(R.string.EditProfileBirthdayInfoContacts);
                    break;
                }
                if ((privacyRules.get(i2) instanceof TLRPC.TL_privacyValueAllowAll) || (privacyRules.get(i2) instanceof TLRPC.TL_privacyValueDisallowAll)) {
                    string = LocaleController.getString(R.string.EditProfileBirthdayInfo);
                }
                i2++;
            }
        }
        linksTextView.setText(AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(string, new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda114
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.lambda$createBirthdayPickerDialog$97(privacyRules);
            }
        }), true, AndroidUtilities.dp(2.6666667f), AndroidUtilities.dp(0.66f)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBlockDialogAlert$54(boolean[] zArr, int i, View view) {
        boolean z = !zArr[i];
        zArr[i] = z;
        ((CheckBoxCell) view).setChecked(z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBlockDialogAlert$55(BlockDialogCallback blockDialogCallback, boolean[] zArr, DialogInterface dialogInterface, int i) {
        blockDialogCallback.run(zArr[0], zArr[1]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBotLaunchAlert$26(TLRPC.User user, BaseFragment baseFragment, AlertDialog.Builder builder, View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", user.id);
        if (baseFragment.getMessagesController().checkCanOpenChat(bundle, baseFragment)) {
            baseFragment.presentFragment(new ProfileActivity(bundle));
        }
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBotLaunchAlert$27(Runnable runnable, DialogInterface dialogInterface, int i) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBotLaunchAlert$28(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBotLaunchAlert$29(AlertDialog alertDialog, Context context) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        Browser.openUrl(context, LocaleController.getString(R.string.BotWebViewStartPermissionLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBotLaunchAlert$30(TLRPC.User user, BaseFragment baseFragment, AlertDialog.Builder builder, View view) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", user.id);
        if (baseFragment.getMessagesController().checkCanOpenChat(bundle, baseFragment)) {
            baseFragment.presentFragment(new ProfileActivity(bundle));
        }
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBotLaunchAlert$31(AtomicBoolean atomicBoolean, View view) {
        atomicBoolean.set(!atomicBoolean.get());
        ((CheckBoxCell) view).setChecked(atomicBoolean.get(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createBotLaunchAlert$33(AlertDialog alertDialog, Context context) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        Browser.openUrl(context, LocaleController.getString(R.string.BotWebViewStartPermissionLink));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createCalendarPickerDialog$121(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createCalendarPickerDialog$122(int i) {
        return "" + i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createCalendarPickerDialog$124(int i) {
        int i2;
        switch (i) {
            case 0:
                i2 = R.string.January;
                break;
            case 1:
                i2 = R.string.February;
                break;
            case 2:
                i2 = R.string.March;
                break;
            case 3:
                i2 = R.string.April;
                break;
            case 4:
                i2 = R.string.May;
                break;
            case 5:
                i2 = R.string.June;
                break;
            case 6:
                i2 = R.string.July;
                break;
            case 7:
                i2 = R.string.August;
                break;
            case 8:
                i2 = R.string.September;
                break;
            case 9:
                i2 = R.string.October;
                break;
            case 10:
                i2 = R.string.November;
                break;
            default:
                i2 = R.string.December;
                break;
        }
        return LocaleController.getString(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createCalendarPickerDialog$125(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createCalendarPickerDialog$126(long j, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, Calendar calendar, MessagesStorage.IntCallback intCallback, BottomSheet.Builder builder, View view) {
        checkCalendarDate(j, numberPicker, numberPicker2, numberPicker3);
        calendar.set(1, numberPicker3.getValue());
        calendar.set(2, numberPicker2.getValue());
        calendar.set(5, numberPicker.getValue());
        calendar.set(12, 0);
        calendar.set(11, 0);
        calendar.set(13, 0);
        intCallback.run((int) (calendar.getTimeInMillis() / 1000));
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createCallDialogAlert$42(BaseFragment baseFragment, TLRPC.User user, boolean z, DialogInterface dialogInterface, int i) {
        TLRPC.UserFull userFull = baseFragment.getMessagesController().getUserFull(user.id);
        VoIPHelper.startCall(user, z, userFull != null && userFull.video_calls_available, baseFragment.getParentActivity(), userFull, baseFragment.getAccountInstance());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createChangeBioAlert$43(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createChangeBioAlert$44(long j, int i, EditText editText, DialogInterface dialogInterface, int i2) {
        MessagesController messagesController = MessagesController.getInstance(i);
        if (j > 0) {
            TLRPC.UserFull userFull = messagesController.getUserFull(UserConfig.getInstance(i).getClientUserId());
            String trim = editText.getText().toString().replace("\n", " ").replaceAll(" +", " ").trim();
            if (userFull != null) {
                String str = userFull.about;
                if (!(str != null ? str : "").equals(trim)) {
                    userFull.about = trim;
                    NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userInfoDidLoad, Long.valueOf(j), userFull);
                }
                AndroidUtilities.hideKeyboard(editText);
                dialogInterface.dismiss();
                return;
            }
            TLRPC.TL_account_updateProfile tL_account_updateProfile = new TLRPC.TL_account_updateProfile();
            tL_account_updateProfile.about = trim;
            tL_account_updateProfile.flags = 4 | tL_account_updateProfile.flags;
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 2, Long.valueOf(j));
            ConnectionsManager.getInstance(i).sendRequest(tL_account_updateProfile, new RequestDelegate() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda160
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    AlertsCreator.lambda$createChangeBioAlert$43(tLObject, tL_error);
                }
            }, 2);
            dialogInterface.dismiss();
        }
        long j2 = -j;
        TLRPC.ChatFull chatFull = messagesController.getChatFull(j2);
        String obj = editText.getText().toString();
        if (chatFull != null) {
            String str2 = chatFull.about;
            if (!(str2 != null ? str2 : "").equals(obj)) {
                chatFull.about = obj;
                NotificationCenter notificationCenter = NotificationCenter.getInstance(i);
                int i3 = NotificationCenter.chatInfoDidLoad;
                Boolean bool = Boolean.FALSE;
                notificationCenter.lambda$postNotificationNameOnUIThread$1(i3, chatFull, 0, bool, bool);
            }
            AndroidUtilities.hideKeyboard(editText);
            dialogInterface.dismiss();
            return;
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 2, Long.valueOf(j));
        MessagesController.getInstance(i).updateChatAbout(j2, obj, chatFull);
        dialogInterface.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createChangeBioAlert$46(long j, AlertDialog alertDialog, DialogInterface.OnClickListener onClickListener, TextView textView, int i, KeyEvent keyEvent) {
        if ((i != 6 && (j <= 0 || keyEvent.getKeyCode() != 66)) || !alertDialog.isShowing()) {
            return false;
        }
        onClickListener.onClick(alertDialog, 0);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createChangeNameAlert$47(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createChangeNameAlert$48(EditText editText, long j, int i, EditText editText2, DialogInterface dialogInterface, int i2) {
        if (editText.getText() == null) {
            return;
        }
        if (j > 0) {
            TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
            String obj = editText.getText().toString();
            String obj2 = editText2.getText().toString();
            String str = user.first_name;
            String str2 = user.last_name;
            if (str == null) {
                str = "";
            }
            if (str2 == null) {
                str2 = "";
            }
            if (str.equals(obj) && str2.equals(obj2)) {
                dialogInterface.dismiss();
                return;
            }
            TLRPC.TL_account_updateProfile tL_account_updateProfile = new TLRPC.TL_account_updateProfile();
            tL_account_updateProfile.flags = 3;
            tL_account_updateProfile.first_name = obj;
            user.first_name = obj;
            tL_account_updateProfile.last_name = obj2;
            user.last_name = obj2;
            TLRPC.User user2 = MessagesController.getInstance(i).getUser(Long.valueOf(UserConfig.getInstance(i).getClientUserId()));
            if (user2 != null) {
                user2.first_name = tL_account_updateProfile.first_name;
                user2.last_name = tL_account_updateProfile.last_name;
            }
            UserConfig.getInstance(i).saveConfig(true);
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.mainUserInfoChanged, new Object[0]);
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_NAME));
            ConnectionsManager.getInstance(i).sendRequest(tL_account_updateProfile, new RequestDelegate() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda159
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    AlertsCreator.lambda$createChangeNameAlert$47(tLObject, tL_error);
                }
            });
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 3, Long.valueOf(j));
        } else {
            long j2 = -j;
            TLRPC.Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(j2));
            String obj3 = editText.getText().toString();
            String str3 = chat.title;
            if (str3 != null && str3.equals(obj3)) {
                dialogInterface.dismiss();
                return;
            }
            chat.title = obj3;
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_CHAT_NAME));
            MessagesController.getInstance(i).changeChatTitle(j2, obj3);
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 3, Long.valueOf(j));
        }
        dialogInterface.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createChangeNameAlert$49(EditText editText, EditText editText2, DialogInterface dialogInterface) {
        AndroidUtilities.hideKeyboard(editText);
        AndroidUtilities.hideKeyboard(editText2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createChangeNameAlert$50(AlertDialog alertDialog, DialogInterface.OnClickListener onClickListener, TextView textView, int i, KeyEvent keyEvent) {
        if ((i != 6 && keyEvent.getKeyCode() != 66) || !alertDialog.isShowing()) {
            return false;
        }
        onClickListener.onClick(alertDialog, 0);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createClearDaysDialogAlert$40(boolean[] zArr, View view) {
        boolean z = !zArr[0];
        zArr[0] = z;
        ((CheckBoxCell) view).setChecked(z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createClearDaysDialogAlert$41(MessagesStorage.BooleanCallback booleanCallback, boolean[] zArr, DialogInterface dialogInterface, int i) {
        booleanCallback.run(zArr[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createClearOrDeleteDialogAlert$34(boolean[] zArr, View view) {
        boolean z = !zArr[0];
        zArr[0] = z;
        ((CheckBoxCell) view).setChecked(z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createClearOrDeleteDialogAlert$35(boolean[] zArr, View view) {
        boolean z = !zArr[0];
        zArr[0] = z;
        ((CheckBoxCell) view).setChecked(z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createClearOrDeleteDialogAlert$36(BaseFragment baseFragment, boolean z, boolean z2, TLRPC.Chat chat, TLRPC.User user, boolean z3, boolean z4, MessagesStorage.BooleanCallback booleanCallback, Theme.ResourcesProvider resourcesProvider, boolean[] zArr, int i) {
        if (i >= 50) {
            createClearOrDeleteDialogAlert(baseFragment, z, z2, true, chat, user, false, z3, z4, booleanCallback, resourcesProvider);
        } else if (booleanCallback != null) {
            booleanCallback.run(zArr[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createClearOrDeleteDialogAlert$37(boolean z, boolean z2, boolean z3, final TLRPC.User user, final BaseFragment baseFragment, final boolean z4, final boolean z5, final TLRPC.Chat chat, final boolean z6, final boolean z7, final MessagesStorage.BooleanCallback booleanCallback, final Theme.ResourcesProvider resourcesProvider, final boolean[] zArr, DialogInterface dialogInterface, int i) {
        if (!z && !z2 && !z3) {
            if (UserObject.isUserSelf(user)) {
                createClearOrDeleteDialogAlert(baseFragment, z4, z5, true, chat, user, false, z6, z7, booleanCallback, resourcesProvider);
                return;
            } else if (user != null && zArr[0]) {
                MessagesStorage.getInstance(baseFragment.getCurrentAccount()).getMessagesCount(user.id, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda86
                    @Override // org.telegram.messenger.MessagesStorage.IntCallback
                    public final void run(int i2) {
                        AlertsCreator.lambda$createClearOrDeleteDialogAlert$36(BaseFragment.this, z4, z5, chat, user, z6, z7, booleanCallback, resourcesProvider, zArr, i2);
                    }
                });
                return;
            }
        }
        if (booleanCallback != null) {
            booleanCallback.run(z2 || zArr[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createClearOrDeleteDialogsAlert$38(boolean[] zArr, View view) {
        boolean z = !zArr[0];
        zArr[0] = z;
        ((CheckBoxCell) view).setChecked(z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createClearOrDeleteDialogsAlert$39(MessagesStorage.BooleanCallback booleanCallback, boolean[] zArr, DialogInterface dialogInterface, int i) {
        if (booleanCallback != null) {
            booleanCallback.run(zArr[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createColorSelectDialog$130(LinearLayout linearLayout, int[] iArr, View view) {
        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RadioColorCell radioColorCell = (RadioColorCell) linearLayout.getChildAt(i);
            radioColorCell.setChecked(radioColorCell == view, true);
        }
        iArr[0] = TextColorCell.colorsToSave[((Integer) view.getTag()).intValue()];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createColorSelectDialog$131(long j, String str, int[] iArr, long j2, int i, Runnable runnable, DialogInterface dialogInterface, int i2) {
        int i3;
        String str2;
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        if (j != 0) {
            edit.putInt("color_" + str, iArr[0]);
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannel(j, j2);
        } else {
            if (i == 1) {
                i3 = iArr[0];
                str2 = "MessagesLed";
            } else if (i == 0) {
                i3 = iArr[0];
                str2 = "GroupLed";
            } else if (i == 3) {
                i3 = iArr[0];
                str2 = "StoriesLed";
            } else if (i == 5 || i == 4) {
                i3 = iArr[0];
                str2 = "ReactionLed";
            } else {
                i3 = iArr[0];
                str2 = "ChannelLed";
            }
            edit.putInt(str2, i3);
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(i);
        }
        edit.commit();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createColorSelectDialog$132(long j, int i, Runnable runnable, DialogInterface dialogInterface, int i2) {
        String str;
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        if (j != 0) {
            str = "color_" + j;
        } else {
            str = i == 1 ? "MessagesLed" : i == 0 ? "GroupLed" : i == 3 ? "StoriesLed" : (i == 5 || i == 4) ? "ReactionsLed" : "ChannelLed";
        }
        edit.putInt(str, 0);
        edit.commit();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createColorSelectDialog$133(String str, Runnable runnable, DialogInterface dialogInterface, int i) {
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        edit.remove("color_" + str);
        edit.commit();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createContactInviteDialog$52(String str, BaseFragment baseFragment, DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", str, null));
            intent.putExtra("sms_body", ContactsController.getInstance(baseFragment.getCurrentAccount()).getInviteText(1));
            baseFragment.getParentActivity().startActivityForResult(intent, 500);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDatePickerDialog$69(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i) {
        if (z && i == 0) {
            checkPickerDate(numberPicker, numberPicker2, numberPicker3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createDatePickerDialog$70(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, 1);
        calendar.set(2, i);
        return calendar.getDisplayName(2, 1, Locale.getDefault());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDatePickerDialog$72(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i) {
        if (z && i == 0) {
            checkPickerDate(numberPicker, numberPicker2, numberPicker3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDatePickerDialog$74(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i) {
        if (z && i == 0) {
            checkPickerDate(numberPicker, numberPicker2, numberPicker3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDatePickerDialog$75(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, DatePickerDelegate datePickerDelegate, DialogInterface dialogInterface, int i) {
        if (z) {
            checkPickerDate(numberPicker, numberPicker2, numberPicker3);
        }
        datePickerDelegate.didSelectDate(numberPicker3.getValue(), numberPicker2.getValue(), numberPicker.getValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createDatePickerDialog$85(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createDatePickerDialog$86(long j, Calendar calendar, int i, int i2) {
        if (i2 == 0) {
            return LocaleController.getString(R.string.MessageScheduleToday);
        }
        long j2 = j + (i2 * 86400000);
        calendar.setTimeInMillis(j2);
        return calendar.get(1) == i ? LocaleController.getInstance().getFormatterScheduleDay().format(j2) : LocaleController.getInstance().getFormatterScheduleYear().format(j2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createDatePickerDialog$88(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createDatePickerDialog$89(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDatePickerDialog$90(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, Calendar calendar, ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, View view) {
        boolean checkScheduleDate = checkScheduleDate(null, null, 0, numberPicker, numberPicker2, numberPicker3);
        calendar.setTimeInMillis(System.currentTimeMillis() + (numberPicker.getValue() * 86400000));
        calendar.set(11, numberPicker2.getValue());
        calendar.set(12, numberPicker3.getValue());
        if (checkScheduleDate) {
            calendar.set(13, 0);
        }
        scheduleDatePickerDelegate.didSelectDate(true, (int) (calendar.getTimeInMillis() / 1000));
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TLObject lambda$createDeleteMessagesAlert$150(int i, long j) {
        MessagesController messagesController = MessagesController.getInstance(i);
        return j > 0 ? messagesController.getUser(Long.valueOf(j)) : messagesController.getChat(Long.valueOf(-j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createDeleteMessagesAlert$151(long j, TLObject tLObject) {
        if (tLObject instanceof TLRPC.User) {
            return ((TLRPC.User) tLObject).id != j;
        }
        if (tLObject instanceof TLRPC.Chat) {
            return !ChatObject.hasAdminRights((TLRPC.Chat) tLObject);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDeleteMessagesAlert$152(int[] iArr, int[] iArr2, int i, TLObject tLObject, TLRPC.ChannelParticipant[] channelParticipantArr, int i2, AlertDialog[] alertDialogArr, BaseFragment baseFragment, TLRPC.User user, TLRPC.Chat chat, TLRPC.EncryptedChat encryptedChat, TLRPC.ChatFull chatFull, long j, MessageObject messageObject, SparseArray[] sparseArrayArr, MessageObject.GroupedMessages groupedMessages, int i3, int i4, Runnable runnable, Runnable runnable2, Theme.ResourcesProvider resourcesProvider) {
        iArr[0] = iArr[0] + 1;
        iArr2[i] = 0;
        if (tLObject != null) {
            channelParticipantArr[i] = ((TLRPC.TL_channels_channelParticipant) tLObject).participant;
        }
        if (iArr[0] == i2) {
            try {
                alertDialogArr[0].dismiss();
            } catch (Throwable unused) {
            }
            alertDialogArr[0] = null;
            createDeleteMessagesAlert(baseFragment, user, chat, encryptedChat, chatFull, j, messageObject, sparseArrayArr, groupedMessages, i3, i4, channelParticipantArr, runnable, runnable2, resourcesProvider);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDeleteMessagesAlert$153(final int[] iArr, final int[] iArr2, final int i, final TLRPC.ChannelParticipant[] channelParticipantArr, final int i2, final AlertDialog[] alertDialogArr, final BaseFragment baseFragment, final TLRPC.User user, final TLRPC.Chat chat, final TLRPC.EncryptedChat encryptedChat, final TLRPC.ChatFull chatFull, final long j, final MessageObject messageObject, final SparseArray[] sparseArrayArr, final MessageObject.GroupedMessages groupedMessages, final int i3, final int i4, final Runnable runnable, final Runnable runnable2, final Theme.ResourcesProvider resourcesProvider, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda152
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.lambda$createDeleteMessagesAlert$152(iArr, iArr2, i, tLObject, channelParticipantArr, i2, alertDialogArr, baseFragment, user, chat, encryptedChat, chatFull, j, messageObject, sparseArrayArr, groupedMessages, i3, i4, runnable, runnable2, resourcesProvider);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDeleteMessagesAlert$154(int[] iArr, int i, Runnable runnable, DialogInterface dialogInterface) {
        for (int i2 : iArr) {
            if (i2 != 0) {
                ConnectionsManager.getInstance(i).cancelRequest(i2, true);
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDeleteMessagesAlert$155(AlertDialog[] alertDialogArr, final int[] iArr, final int i, final Runnable runnable, BaseFragment baseFragment) {
        AlertDialog alertDialog = alertDialogArr[0];
        if (alertDialog == null) {
            return;
        }
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda124
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                AlertsCreator.lambda$createDeleteMessagesAlert$154(iArr, i, runnable, dialogInterface);
            }
        });
        baseFragment.showDialog(alertDialogArr[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDeleteMessagesAlert$157(boolean[] zArr, View view) {
        boolean z = !zArr[0];
        zArr[0] = z;
        ((CheckBoxCell) view).setChecked(z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDeleteMessagesAlert$158(boolean[] zArr, View view) {
        boolean z = !zArr[0];
        zArr[0] = z;
        ((CheckBoxCell) view).setChecked(z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDeleteMessagesAlert$159(long j, boolean z, int i, MessageObject messageObject, MessageObject.GroupedMessages groupedMessages, TLRPC.EncryptedChat encryptedChat, long j2, int i2, boolean[] zArr, int i3, SparseArray[] sparseArrayArr, Runnable runnable, DialogInterface dialogInterface, int i4) {
        ArrayList<Long> arrayList;
        TLRPC.Peer peer;
        long clientUserId = z ? UserConfig.getInstance(i).getClientUserId() : j;
        ArrayList<Long> arrayList2 = null;
        if (messageObject != null) {
            ArrayList<Integer> arrayList3 = new ArrayList<>();
            if (groupedMessages != null) {
                for (int i5 = 0; i5 < groupedMessages.messages.size(); i5++) {
                    MessageObject messageObject2 = groupedMessages.messages.get(i5);
                    arrayList3.add(Integer.valueOf(messageObject2.getId()));
                    if (encryptedChat != null && messageObject2.messageOwner.random_id != 0 && messageObject2.type != 10) {
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList<>();
                        }
                        arrayList2.add(Long.valueOf(messageObject2.messageOwner.random_id));
                    }
                }
            } else {
                arrayList3.add(Integer.valueOf(messageObject.getId()));
                if (encryptedChat != null && messageObject.messageOwner.random_id != 0 && messageObject.type != 10) {
                    ArrayList<Long> arrayList4 = new ArrayList<>();
                    arrayList4.add(Long.valueOf(messageObject.messageOwner.random_id));
                    arrayList2 = arrayList4;
                }
            }
            MessagesController.getInstance(i).deleteMessages(arrayList3, arrayList2, encryptedChat, (j2 == 0 || (peer = messageObject.messageOwner.peer_id) == null || peer.chat_id != (-j2)) ? clientUserId : j2, i2, zArr[0], i3);
        } else {
            int i6 = 1;
            int i7 = 1;
            while (i7 >= 0) {
                ArrayList<Integer> arrayList5 = new ArrayList<>();
                for (int i8 = 0; i8 < sparseArrayArr[i7].size(); i8++) {
                    arrayList5.add(Integer.valueOf(sparseArrayArr[i7].keyAt(i8)));
                }
                if (encryptedChat != null) {
                    ArrayList<Long> arrayList6 = new ArrayList<>();
                    for (int i9 = 0; i9 < sparseArrayArr[i7].size(); i9++) {
                        MessageObject messageObject3 = (MessageObject) sparseArrayArr[i7].valueAt(i9);
                        long j3 = messageObject3.messageOwner.random_id;
                        if (j3 != 0 && messageObject3.type != 10) {
                            arrayList6.add(Long.valueOf(j3));
                        }
                    }
                    arrayList = arrayList6;
                } else {
                    arrayList = null;
                }
                int i10 = i7;
                MessagesController.getInstance(i).deleteMessages(arrayList5, arrayList, encryptedChat, (i7 != i6 || j2 == 0) ? clientUserId : j2, i2, zArr[0], i3);
                sparseArrayArr[i10].clear();
                i7 = i10 - 1;
                i6 = 1;
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDeleteMessagesAlert$160(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDrawOverlayGroupCallPermissionDialog$140(Context context, DialogInterface dialogInterface, int i) {
        if (context != null) {
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + context.getPackageName()));
                    Activity findActivity = AndroidUtilities.findActivity(context);
                    if (findActivity instanceof LaunchActivity) {
                        findActivity.startActivityForResult(intent, 105);
                    } else {
                        context.startActivity(intent);
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createDrawOverlayPermissionDialog$139(Activity activity, DialogInterface dialogInterface, int i) {
        if (activity == null || Build.VERSION.SDK_INT < 23) {
            return;
        }
        try {
            activity.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + activity.getPackageName())));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createFreeSpaceDialog$143(LaunchActivity launchActivity, DialogInterface dialogInterface, int i) {
        launchActivity.lambda$runLinkRequest$93(new CacheControlActivity());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createImportDialogAlert$25(Runnable runnable, DialogInterface dialogInterface, int i) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createLanguageAlert$8(LaunchActivity launchActivity, DialogInterface dialogInterface, int i) {
        launchActivity.lambda$runLinkRequest$93(new LanguageSelectActivity());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createLanguageAlert$9(TLRPC.TL_langPackLanguage tL_langPackLanguage, LaunchActivity launchActivity, DialogInterface dialogInterface, int i) {
        StringBuilder sb;
        String str;
        if (tL_langPackLanguage.official) {
            sb = new StringBuilder();
            str = "remote_";
        } else {
            sb = new StringBuilder();
            str = "unofficial_";
        }
        sb.append(str);
        sb.append(tL_langPackLanguage.lang_code);
        LocaleController.LocaleInfo languageFromDict = LocaleController.getInstance().getLanguageFromDict(sb.toString());
        if (languageFromDict == null) {
            languageFromDict = new LocaleController.LocaleInfo();
            languageFromDict.name = tL_langPackLanguage.native_name;
            languageFromDict.nameEnglish = tL_langPackLanguage.name;
            languageFromDict.shortName = tL_langPackLanguage.lang_code;
            languageFromDict.baseLangCode = tL_langPackLanguage.base_lang_code;
            languageFromDict.pluralLangCode = tL_langPackLanguage.plural_code;
            languageFromDict.isRtl = tL_langPackLanguage.rtl;
            languageFromDict.pathToFile = tL_langPackLanguage.official ? "remote" : "unofficial";
        }
        LocaleController.getInstance().applyLanguage(languageFromDict, true, false, false, true, UserConfig.selectedAccount, null);
        launchActivity.rebuildAllFragments(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createLocationRequiredDialog$0(Context context, DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createLocationUpdateDialog$135(int[] iArr, LinearLayout linearLayout, View view) {
        iArr[0] = ((Integer) view.getTag()).intValue();
        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = linearLayout.getChildAt(i);
            if (childAt instanceof RadioColorCell) {
                ((RadioColorCell) childAt).setChecked(childAt == view, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createLocationUpdateDialog$136(int[] iArr, MessagesStorage.IntCallback intCallback, DialogInterface dialogInterface, int i) {
        int i2 = iArr[0];
        intCallback.run(i2 == 0 ? 900 : i2 == 1 ? 3600 : i2 == 2 ? 28800 : ConnectionsManager.DEFAULT_DATACENTER_ID);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createMuteAlert$127(long j, long j2, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i) {
        int i2;
        if (i == 0) {
            i2 = 0;
        } else {
            i2 = 1;
            if (i != 1) {
                i2 = 2;
                if (i != 2) {
                    i2 = 3;
                }
            }
        }
        NotificationsController.getInstance(UserConfig.selectedAccount).setDialogNotificationsSettings(j, j2, i2);
        if (BulletinFactory.canShowBulletin(baseFragment)) {
            BulletinFactory.createMuteBulletin(baseFragment, i2, 0, resourcesProvider).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createMuteAlert$128(ArrayList arrayList, int i, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i2) {
        int i3;
        if (i2 == 0) {
            i3 = 0;
        } else {
            i3 = 1;
            if (i2 != 1) {
                i3 = 2;
                if (i2 != 2) {
                    i3 = 3;
                }
            }
        }
        if (arrayList != null) {
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                NotificationsController.getInstance(UserConfig.selectedAccount).setDialogNotificationsSettings(((Long) arrayList.get(i4)).longValue(), i, i3);
            }
        }
        if (BulletinFactory.canShowBulletin(baseFragment)) {
            BulletinFactory.createMuteBulletin(baseFragment, i3, 0, resourcesProvider).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createMuteForPickerDialog$117(int[] iArr, int i) {
        int i2 = iArr[i];
        return i2 == 0 ? LocaleController.getString(R.string.MuteNever) : i2 < 60 ? LocaleController.formatPluralString("Minutes", i2, new Object[0]) : i2 < 1440 ? LocaleController.formatPluralString("Hours", i2 / 60, new Object[0]) : i2 < 10080 ? LocaleController.formatPluralString("Days", i2 / 1440, new Object[0]) : i2 < 44640 ? LocaleController.formatPluralString("Weeks", i2 / 10080, new Object[0]) : i2 < 525600 ? LocaleController.formatPluralString("Months", i2 / 44640, new Object[0]) : LocaleController.formatPluralString("Years", i2 / 525600, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createMuteForPickerDialog$118(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createMuteForPickerDialog$119(NumberPicker numberPicker, int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createMuteForPickerDialog$120(int[] iArr, NumberPicker numberPicker, ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, View view) {
        scheduleDatePickerDelegate.didSelectDate(true, iArr[numberPicker.getValue()] * 60);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createPopupSelectDialog$145(int[] iArr, int i, AlertDialog.Builder builder, Runnable runnable, View view) {
        int i2;
        String str;
        iArr[0] = ((Integer) view.getTag()).intValue();
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        if (i == 1) {
            i2 = iArr[0];
            str = "popupAll";
        } else if (i == 0) {
            i2 = iArr[0];
            str = "popupGroup";
        } else {
            i2 = iArr[0];
            str = "popupChannel";
        }
        edit.putInt(str, i2);
        edit.commit();
        builder.getDismissRunnable().run();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createPrioritySelectDialog$144(int[] iArr, long j, long j2, int i, SharedPreferences sharedPreferences, AlertDialog.Builder builder, Runnable runnable, View view) {
        int i2 = 0;
        iArr[0] = ((Integer) view.getTag()).intValue();
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        if (j != 0) {
            int i3 = iArr[0];
            if (i3 == 0) {
                i2 = 3;
            } else if (i3 == 1) {
                i2 = 4;
            } else if (i3 == 2) {
                i2 = 5;
            } else if (i3 != 3) {
                i2 = 1;
            }
            edit.putInt("priority_" + j, i2);
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannel(j, j2);
        } else {
            int i4 = iArr[0];
            int i5 = i4 == 0 ? 4 : i4 == 1 ? 5 : i4 == 2 ? 0 : 1;
            if (i == 1) {
                edit.putInt("priority_messages", i5);
                iArr[0] = sharedPreferences.getInt("priority_messages", 1);
            } else if (i == 0) {
                edit.putInt("priority_group", i5);
                iArr[0] = sharedPreferences.getInt("priority_group", 1);
            } else if (i == 2) {
                edit.putInt("priority_channel", i5);
                iArr[0] = sharedPreferences.getInt("priority_channel", 1);
            } else if (i == 3) {
                edit.putInt("priority_stories", i5);
                iArr[0] = sharedPreferences.getInt("priority_stories", 1);
            } else if (i == 4 || i == 5) {
                edit.putInt("priority_react", i5);
                iArr[0] = sharedPreferences.getInt("priority_react", 1);
            }
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(i);
        }
        edit.commit();
        builder.getDismissRunnable().run();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createScheduleDatePickerDialog$76(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$77(ActionBarMenuItem actionBarMenuItem, ScheduleDatePickerColors scheduleDatePickerColors, View view) {
        actionBarMenuItem.toggleSubMenu();
        actionBarMenuItem.setPopupItemsColor(scheduleDatePickerColors.subMenuTextColor, false);
        actionBarMenuItem.setupPopupRadialSelectors(scheduleDatePickerColors.subMenuSelectorColor);
        actionBarMenuItem.redrawPopup(scheduleDatePickerColors.subMenuBackgroundColor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$78(ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, int i) {
        if (i == 1) {
            scheduleDatePickerDelegate.didSelectDate(true, 2147483646);
            builder.getDismissRunnable().run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createScheduleDatePickerDialog$79(long j, Calendar calendar, int i, int i2) {
        if (i2 == 0) {
            return LocaleController.getString(R.string.MessageScheduleToday);
        }
        long j2 = j + (i2 * 86400000);
        calendar.setTimeInMillis(j2);
        if (calendar.get(1) != i) {
            return LocaleController.getInstance().getFormatterScheduleYear().format(j2);
        }
        return LocaleController.getInstance().getFormatterWeek().format(j2) + ", " + LocaleController.getInstance().getFormatterScheduleDay().format(j2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$80(TextView textView, long j, long j2, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, NumberPicker numberPicker4, int i, int i2) {
        checkScheduleDate(textView, null, j == j2 ? 1 : 0, numberPicker, numberPicker2, numberPicker3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createScheduleDatePickerDialog$81(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createScheduleDatePickerDialog$82(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$83(boolean[] zArr, long j, long j2, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, Calendar calendar, ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder, View view) {
        zArr[0] = false;
        boolean checkScheduleDate = checkScheduleDate(null, null, j == j2 ? 1 : 0, numberPicker, numberPicker2, numberPicker3);
        calendar.setTimeInMillis(System.currentTimeMillis() + (numberPicker.getValue() * 86400000));
        calendar.set(11, numberPicker2.getValue());
        calendar.set(12, numberPicker3.getValue());
        if (checkScheduleDate) {
            calendar.set(13, 0);
        }
        scheduleDatePickerDelegate.didSelectDate(true, (int) (calendar.getTimeInMillis() / 1000));
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createScheduleDatePickerDialog$84(Runnable runnable, boolean[] zArr, DialogInterface dialogInterface) {
        if (runnable == null || !zArr[0]) {
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createSimpleAlert$10(Runnable runnable, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createSingleChoiceDialog$146(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener, View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        builder.getDismissRunnable().run();
        onClickListener.onClick(null, intValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createSoundFrequencyPickerDialog$111(int i) {
        return LocaleController.formatPluralString("Times", i + 1, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createSoundFrequencyPickerDialog$112(int i) {
        return LocaleController.formatPluralString("Minutes", i + 1, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createSoundFrequencyPickerDialog$113(int i) {
        return LocaleController.getString(R.string.NotificationsFrequencyDivider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createSoundFrequencyPickerDialog$114(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createSoundFrequencyPickerDialog$115(NumberPicker numberPicker, int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createSoundFrequencyPickerDialog$116(NumberPicker numberPicker, NumberPicker numberPicker2, SoundFrequencyDelegate soundFrequencyDelegate, BottomSheet.Builder builder, View view) {
        soundFrequencyDelegate.didSelectValues(numberPicker.getValue() + 1, (numberPicker2.getValue() + 1) * 60);
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createStatusUntilDatePickerDialog$101(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createStatusUntilDatePickerDialog$102(long j, Calendar calendar, int i, int i2, int i3) {
        if (i3 == 0) {
            return LocaleController.getString(R.string.MessageScheduleToday);
        }
        long j2 = j + (i3 * 86400000);
        calendar.setTimeInMillis(j2);
        int i4 = calendar.get(1);
        int i5 = calendar.get(6);
        if (i4 != i || i5 >= i2 + 7) {
            LocaleController localeController = LocaleController.getInstance();
            return i4 == i ? localeController.getFormatterScheduleDay().format(j2) : localeController.getFormatterScheduleYear().format(j2);
        }
        return LocaleController.getInstance().getFormatterWeek().format(j2) + ", " + LocaleController.getInstance().getFormatterScheduleDay().format(j2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createStatusUntilDatePickerDialog$104(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createStatusUntilDatePickerDialog$105(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createStatusUntilDatePickerDialog$106(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, Calendar calendar, StatusUntilDatePickerDelegate statusUntilDatePickerDelegate, BottomSheet.Builder builder, View view) {
        boolean checkScheduleDate = checkScheduleDate(null, null, 0, numberPicker, numberPicker2, numberPicker3);
        calendar.setTimeInMillis(System.currentTimeMillis() + (numberPicker.getValue() * 86400000));
        calendar.set(11, numberPicker2.getValue());
        calendar.set(12, numberPicker3.getValue());
        if (checkScheduleDate) {
            calendar.set(13, 0);
        }
        statusUntilDatePickerDelegate.didSelectDate((int) (calendar.getTimeInMillis() / 1000));
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createTTLAlert$147(int i) {
        return i == 0 ? LocaleController.getString(R.string.ShortMessageLifetimeForever) : (i < 1 || i >= 16) ? i == 16 ? LocaleController.formatTTLString(30) : i == 17 ? LocaleController.formatTTLString(60) : i == 18 ? LocaleController.formatTTLString(3600) : i == 19 ? LocaleController.formatTTLString(86400) : i == 20 ? LocaleController.formatTTLString(604800) : "" : LocaleController.formatTTLString(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$createTTLAlert$148(TLRPC.EncryptedChat encryptedChat, NumberPicker numberPicker, DialogInterface dialogInterface, int i) {
        int i2 = encryptedChat.ttl;
        int value = numberPicker.getValue();
        if (value < 0 || value >= 16) {
            if (value == 16) {
                value = 30;
            } else if (value == 17) {
                value = 60;
            } else if (value == 18) {
                value = 3600;
            } else {
                if (value != 19) {
                    if (value == 20) {
                        value = 604800;
                    }
                    if (i2 == encryptedChat.ttl) {
                        SecretChatHelper.getInstance(UserConfig.selectedAccount).sendTTLMessage(encryptedChat, null);
                        MessagesStorage.getInstance(UserConfig.selectedAccount).updateEncryptedChatTTL(encryptedChat);
                        return;
                    }
                    return;
                }
                value = 86400;
            }
        }
        encryptedChat.ttl = value;
        if (i2 == encryptedChat.ttl) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createThemeCreateDialog$161(DialogInterface dialogInterface, int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createThemeCreateDialog$162(TextView textView, int i, KeyEvent keyEvent) {
        AndroidUtilities.hideKeyboard(textView);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createThemeCreateDialog$163(EditTextBoldCursor editTextBoldCursor) {
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createThemeCreateDialog$164(final EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda115
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.lambda$createThemeCreateDialog$163(EditTextBoldCursor.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createThemeCreateDialog$166(final EditTextBoldCursor editTextBoldCursor, final AlertDialog alertDialog, final BaseFragment baseFragment) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda150
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.processCreate(EditTextBoldCursor.this, alertDialog, baseFragment);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createThemeCreateDialog$167(final BaseFragment baseFragment, final EditTextBoldCursor editTextBoldCursor, Theme.ThemeAccent themeAccent, Theme.ThemeInfo themeInfo, final AlertDialog alertDialog, View view) {
        if (baseFragment.getParentActivity() == null) {
            return;
        }
        if (editTextBoldCursor.length() == 0) {
            Vibrator vibrator = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
            }
            AndroidUtilities.shakeView(editTextBoldCursor);
            return;
        }
        if (baseFragment instanceof ThemePreviewActivity) {
            Theme.applyPreviousTheme();
            baseFragment.lambda$onBackPressed$321();
        }
        if (themeAccent == null) {
            processCreate(editTextBoldCursor, alertDialog, baseFragment);
            return;
        }
        themeInfo.setCurrentAccentId(themeAccent.id);
        Theme.refreshThemeColors();
        Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda98
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.lambda$createThemeCreateDialog$166(EditTextBoldCursor.this, alertDialog, baseFragment);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$createTimePickerDialog$61(int i, int i2, NumberPicker numberPicker, NumberPicker numberPicker2, int i3, LinearLayout linearLayout, Boolean bool) {
        int i4;
        int i5;
        int i6 = i % 60;
        int i7 = (i - i6) / 60;
        int i8 = i2 % 60;
        int i9 = (i2 - i8) / 60;
        int i10 = 59;
        if (i8 == 0 && i9 > 0) {
            i9--;
            i8 = 59;
        }
        if (bool.booleanValue()) {
            i5 = numberPicker.getValue();
            i4 = numberPicker2.getValue();
        } else {
            i4 = i3 % 60;
            i5 = (i3 - i4) / 60;
            if (i5 == 24) {
                i5--;
                i4 = 59;
            }
        }
        numberPicker.setMinValue(i7);
        numberPicker.setMaxValue(i9);
        if (i5 > i9) {
            numberPicker.setValue(i9);
            i5 = i9;
        } else if (i5 < i7) {
            numberPicker.setValue(i7);
            i5 = i7;
        }
        if (i5 > i7) {
            if (i5 >= i9) {
                if (i7 != i9) {
                    i6 = 0;
                }
            } else if (i7 != i9) {
                numberPicker2.setMinValue(0);
            }
            numberPicker2.setMinValue(i6);
            numberPicker2.setMaxValue(i8);
            if (i4 > numberPicker2.getMaxValue()) {
                if (i4 < numberPicker2.getMinValue()) {
                    i4 = numberPicker2.getMinValue();
                }
                if (!bool.booleanValue()) {
                    numberPicker.setValue(i5);
                    numberPicker2.setValue(i4);
                }
                linearLayout.invalidate();
            }
            i4 = numberPicker2.getMaxValue();
            numberPicker2.setValue(i4);
            if (!bool.booleanValue()) {
            }
            linearLayout.invalidate();
        }
        numberPicker2.setMinValue(i6);
        if (i7 == i9) {
            i10 = i8;
        }
        numberPicker2.setMaxValue(i10);
        if (i4 > numberPicker2.getMaxValue()) {
        }
        numberPicker2.setValue(i4);
        if (!bool.booleanValue()) {
        }
        linearLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createTimePickerDialog$62(int i) {
        boolean z = LocaleController.is24HourFormat;
        String format = String.format("%02d", Integer.valueOf((i % 12 != 0 || z) ? i % (z ? 24 : 12) : 12));
        return i >= 24 ? LocaleController.formatString(R.string.BusinessHoursNextDayPicker, format) : format;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createTimePickerDialog$63(Utilities.Callback callback, NumberPicker numberPicker, int i, int i2) {
        callback.run(Boolean.TRUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$createTimePickerDialog$64(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createTimePickerDialog$65(Utilities.Callback callback, NumberPicker numberPicker, int i, int i2) {
        callback.run(Boolean.TRUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createTimePickerDialog$66(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createTimePickerDialog$67(BottomSheet[] bottomSheetArr, View view) {
        bottomSheetArr[0].dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createTimePickerDialog$68(Utilities.Callback callback, NumberPicker numberPicker, NumberPicker numberPicker2, DialogInterface dialogInterface) {
        callback.run(Integer.valueOf((numberPicker.getValue() * 60) + numberPicker2.getValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createVibrationSelectDialog$134(int[] iArr, long j, String str, long j2, AlertDialog.Builder builder, Runnable runnable, View view) {
        iArr[0] = ((Integer) view.getTag()).intValue();
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
        if (j != 0) {
            int i = iArr[0];
            if (i == 0) {
                edit.putInt(str, 0);
            } else if (i == 1) {
                edit.putInt(str, 1);
            } else if (i == 2) {
                edit.putInt(str, 3);
            } else if (i == 3) {
                edit.putInt(str, 2);
            }
            NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannel(j, j2);
        } else {
            int i2 = iArr[0];
            if (i2 == 0) {
                edit.putInt(str, 2);
            } else if (i2 == 1) {
                edit.putInt(str, 0);
            } else if (i2 == 2) {
                edit.putInt(str, 1);
            } else if (i2 == 3) {
                edit.putInt(str, 3);
            } else if (i2 == 4) {
                edit.putInt(str, 4);
            }
            if (str.equals("vibrate_channel")) {
                NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(2);
            } else if (str.equals("vibrate_group")) {
                NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(0);
            } else if (str.equals("vibrate_react")) {
                NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(4);
            } else {
                NotificationsController.getInstance(UserConfig.selectedAccount).deleteNotificationChannelGlobal(1);
            }
        }
        edit.commit();
        builder.getDismissRunnable().run();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createWebViewPermissionsRequestDialog$3(boolean z, Context context, AtomicBoolean atomicBoolean, Consumer consumer, DialogInterface dialogInterface, int i) {
        if (!z) {
            atomicBoolean.set(true);
            consumer.accept(Boolean.TRUE);
            return;
        }
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createWebViewPermissionsRequestDialog$4(AtomicBoolean atomicBoolean, Consumer consumer, DialogInterface dialogInterface, int i) {
        atomicBoolean.set(true);
        consumer.accept(Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createWebViewPermissionsRequestDialog$5(AtomicBoolean atomicBoolean, Consumer consumer, DialogInterface dialogInterface) {
        if (atomicBoolean.get()) {
            return;
        }
        consumer.accept(Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$performAskAQuestion$22(SharedPreferences sharedPreferences, TLRPC.TL_help_support tL_help_support, AlertDialog alertDialog, int i, BaseFragment baseFragment) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong("support_id2", tL_help_support.user.id);
        SerializedData serializedData = new SerializedData();
        tL_help_support.user.serializeToStream(serializedData);
        edit.putString("support_user", Base64.encodeToString(serializedData.toByteArray(), 0));
        edit.commit();
        serializedData.cleanup();
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(tL_help_support.user);
        MessagesStorage.getInstance(i).putUsersAndChats(arrayList, null, true, true);
        MessagesController.getInstance(i).putUser(tL_help_support.user, false);
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", tL_help_support.user.id);
        baseFragment.presentFragment(new ChatActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$performAskAQuestion$23(AlertDialog alertDialog) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$performAskAQuestion$24(final SharedPreferences sharedPreferences, final AlertDialog alertDialog, final int i, final BaseFragment baseFragment, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda100
                @Override // java.lang.Runnable
                public final void run() {
                    AlertsCreator.lambda$performAskAQuestion$23(AlertDialog.this);
                }
            });
        } else {
            final TLRPC.TL_help_support tL_help_support = (TLRPC.TL_help_support) tLObject;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda99
                @Override // java.lang.Runnable
                public final void run() {
                    AlertsCreator.lambda$performAskAQuestion$22(sharedPreferences, tL_help_support, alertDialog, i, baseFragment);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showAddUserAlert$129(BaseFragment baseFragment, DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(baseFragment.getCurrentAccount()).openByUserName("spambot", baseFragment, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBlockReportSpamAlert$15(CheckBoxCell[] checkBoxCellArr, View view) {
        checkBoxCellArr[((Integer) view.getTag()).intValue()].setChecked(!checkBoxCellArr[r2.intValue()].isChecked(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBlockReportSpamAlert$16(TLRPC.User user, AccountInstance accountInstance, CheckBoxCell[] checkBoxCellArr, long j, TLRPC.Chat chat, TLRPC.EncryptedChat encryptedChat, boolean z, MessagesStorage.IntCallback intCallback, DialogInterface dialogInterface, int i) {
        CheckBoxCell checkBoxCell;
        if (user != null) {
            accountInstance.getMessagesController().blockPeer(user.id);
        }
        if (checkBoxCellArr == null || ((checkBoxCell = checkBoxCellArr[0]) != null && checkBoxCell.isChecked())) {
            accountInstance.getMessagesController().reportSpam(j, user, chat, encryptedChat, chat != null && z);
        }
        if (checkBoxCellArr != null && !checkBoxCellArr[1].isChecked()) {
            intCallback.run(0);
            return;
        }
        if (chat == null || ChatObject.isNotInChat(chat)) {
            accountInstance.getMessagesController().deleteDialog(j, 0);
        } else {
            accountInstance.getMessagesController().deleteParticipantFromChat(-j, accountInstance.getMessagesController().getUser(Long.valueOf(accountInstance.getUserConfig().getClientUserId())));
        }
        intCallback.run(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBlockReportSpamReplyAlert$11(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBlockReportSpamReplyAlert$12(CheckBoxCell[] checkBoxCellArr, View view) {
        checkBoxCellArr[((Integer) view.getTag()).intValue()].setChecked(!checkBoxCellArr[r2.intValue()].isChecked(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBlockReportSpamReplyAlert$13(AccountInstance accountInstance, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.Updates) {
            accountInstance.getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showBlockReportSpamReplyAlert$14(TLRPC.User user, final AccountInstance accountInstance, ChatActivity chatActivity, TLRPC.Chat chat, MessageObject messageObject, CheckBoxCell[] checkBoxCellArr, Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i) {
        UndoView undoView;
        if (user != null) {
            accountInstance.getMessagesStorage().deleteUserChatHistory(chatActivity.getDialogId(), user.id);
        } else {
            accountInstance.getMessagesStorage().deleteUserChatHistory(chatActivity.getDialogId(), -chat.id);
        }
        TLRPC.TL_contacts_blockFromReplies tL_contacts_blockFromReplies = new TLRPC.TL_contacts_blockFromReplies();
        tL_contacts_blockFromReplies.msg_id = messageObject.getId();
        tL_contacts_blockFromReplies.delete_message = true;
        tL_contacts_blockFromReplies.delete_history = true;
        if (checkBoxCellArr[0].isChecked()) {
            tL_contacts_blockFromReplies.report_spam = true;
            if (chatActivity.getParentActivity() != null && (undoView = chatActivity.getUndoView()) != null) {
                undoView.showWithAction(0L, 74, (Runnable) null);
            }
        }
        accountInstance.getConnectionsManager().sendRequest(tL_contacts_blockFromReplies, new RequestDelegate() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda117
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                AlertsCreator.lambda$showBlockReportSpamReplyAlert$13(AccountInstance.this, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0122  */
    /* JADX WARN: Removed duplicated region for block: B:29:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00f7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$showCustomNotificationsDialog$17(long j, int i, boolean z, int i2, MessagesStorage.IntCallback intCallback, int i3, BaseFragment baseFragment, ArrayList arrayList, ArrayList arrayList2, MessagesStorage.IntCallback intCallback2, AlertDialog.Builder builder, View view) {
        int i4;
        int i5;
        int i6;
        int intValue = ((Integer) view.getTag()).intValue();
        if (intValue == 0) {
            if (j != 0) {
                SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(i).edit();
                if (z) {
                    edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + j);
                } else {
                    edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j, 0);
                }
                MessagesStorage.getInstance(i).setDialogFlags(j, 0L);
                edit.commit();
                TLRPC.Dialog dialog = (TLRPC.Dialog) MessagesController.getInstance(i).dialogs_dict.get(j);
                if (dialog != null) {
                    dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
                }
                NotificationsController.getInstance(i).updateServerNotificationsSettings(j, i2);
                if (intCallback != null) {
                    if (z) {
                        intCallback.run(0);
                    } else {
                        intCallback.run(1);
                    }
                }
            } else {
                NotificationsController.getInstance(i).setGlobalNotificationsEnabled(i3, 0);
            }
        } else {
            if (intValue != 3) {
                int currentTime = ConnectionsManager.getInstance(i).getCurrentTime();
                if (intValue == 1) {
                    currentTime += 3600;
                } else if (intValue == 2) {
                    currentTime += 172800;
                } else if (intValue == 4) {
                    i4 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    long j2 = i2;
                    i5 = 1;
                    NotificationsController.getInstance(i).muteUntil(j, 1L, i4);
                    if (j != 0 && intCallback != null) {
                        if (intValue == 4 || z) {
                            intCallback.run(1);
                        } else {
                            intCallback.run(0);
                        }
                    }
                    if (j == 0) {
                        NotificationsController.getInstance(i).setGlobalNotificationsEnabled(i3, ConnectionsManager.DEFAULT_DATACENTER_ID);
                    }
                    if (intCallback2 != null) {
                        intCallback2.run(intValue);
                    }
                    builder.getDismissRunnable().run();
                    i6 = intValue == 0 ? 4 : intValue == i5 ? 0 : intValue == 2 ? 2 : intValue == 4 ? 3 : -1;
                    if (i6 < 0 || !BulletinFactory.canShowBulletin(baseFragment)) {
                        return;
                    }
                    BulletinFactory.createMuteBulletin(baseFragment, i6).show();
                    return;
                }
                i4 = currentTime;
                long j22 = i2;
                i5 = 1;
                NotificationsController.getInstance(i).muteUntil(j, 1L, i4);
                if (j != 0) {
                    if (intValue == 4) {
                    }
                    intCallback.run(1);
                }
                if (j == 0) {
                }
                if (intCallback2 != null) {
                }
                builder.getDismissRunnable().run();
                if (intValue == 0) {
                }
                if (i6 < 0) {
                    return;
                } else {
                    return;
                }
            }
            if (j != 0) {
                Bundle bundle = new Bundle();
                bundle.putLong("dialog_id", j);
                baseFragment.presentFragment(new ProfileNotificationsActivity(bundle));
            } else {
                baseFragment.presentFragment(new NotificationsCustomSettingsActivity(i3, arrayList, arrayList2));
            }
        }
        i5 = 1;
        if (intCallback2 != null) {
        }
        builder.getDismissRunnable().run();
        if (intValue == 0) {
        }
        if (i6 < 0) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showOpenUrlAlert$19(BaseFragment baseFragment, String str, long j, boolean z, Browser.Progress progress) {
        Browser.openUrl(baseFragment.getParentActivity(), Uri.parse(str), j == 0, z, progress);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$showPopupMenu$168(ActionBarPopupWindow actionBarPopupWindow, View view, int i, KeyEvent keyEvent) {
        if (i != 82 || keyEvent.getRepeatCount() != 0 || keyEvent.getAction() != 1 || !actionBarPopupWindow.isShowing()) {
            return false;
        }
        actionBarPopupWindow.dismiss();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$showPopupMenu$169(ActionBarPopupWindow actionBarPopupWindow, android.graphics.Rect rect, View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0 || actionBarPopupWindow == null || !actionBarPopupWindow.isShowing()) {
            return false;
        }
        view.getHitRect(rect);
        if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            return false;
        }
        actionBarPopupWindow.dismiss();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSecretLocationAlert$18(ArrayList arrayList, Runnable runnable, AlertDialog.Builder builder, View view) {
        SharedConfig.setSecretMapPreviewType(((Integer) arrayList.get(((Integer) view.getTag()).intValue())).intValue());
        if (runnable != null) {
            runnable.run();
        }
        builder.getDismissRunnable().run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showUpdateAppAlert$7(Context context, DialogInterface dialogInterface, int i) {
        Browser.openUrl(context, BuildVars.PLAYSTORE_APP_URL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void performAskAQuestion(final BaseFragment baseFragment) {
        String string;
        final int currentAccount = baseFragment.getCurrentAccount();
        final SharedPreferences mainSettings = MessagesController.getMainSettings(currentAccount);
        long prefIntOrLong = AndroidUtilities.getPrefIntOrLong(mainSettings, "support_id2", 0L);
        TLRPC.User user = null;
        if (prefIntOrLong != 0) {
            TLRPC.User user2 = MessagesController.getInstance(currentAccount).getUser(Long.valueOf(prefIntOrLong));
            if (user2 == null && (string = mainSettings.getString("support_user", null)) != null) {
                try {
                    byte[] decode = Base64.decode(string, 0);
                    if (decode != null) {
                        SerializedData serializedData = new SerializedData(decode);
                        TLRPC.User TLdeserialize = TLRPC.User.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                        if (TLdeserialize != null && TLdeserialize.id == 333000) {
                            TLdeserialize = null;
                        }
                        serializedData.cleanup();
                        user = TLdeserialize;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            user = user2;
        }
        if (user == null) {
            final AlertDialog alertDialog = new AlertDialog(baseFragment.getParentActivity(), 3);
            alertDialog.setCanCancel(false);
            alertDialog.show();
            ConnectionsManager.getInstance(currentAccount).sendRequest(new TLRPC.TL_help_getSupport(), new RequestDelegate() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda45
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    AlertsCreator.lambda$performAskAQuestion$24(mainSettings, alertDialog, currentAccount, baseFragment, tLObject, tL_error);
                }
            });
            return;
        }
        MessagesController.getInstance(currentAccount).putUser(user, true);
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", user.id);
        baseFragment.presentFragment(new ChatActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void processCreate(EditTextBoldCursor editTextBoldCursor, AlertDialog alertDialog, BaseFragment baseFragment) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        AndroidUtilities.hideKeyboard(editTextBoldCursor);
        Theme.ThemeInfo createNewTheme = Theme.createNewTheme(editTextBoldCursor.getText().toString());
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.themeListUpdated, new Object[0]);
        new ThemeEditorView().show(baseFragment.getParentActivity(), createNewTheme);
        alertDialog.dismiss();
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (globalMainSettings.getBoolean("themehint", false)) {
            return;
        }
        globalMainSettings.edit().putBoolean("themehint", true).commit();
        try {
            Toast.makeText(baseFragment.getParentActivity(), LocaleController.getString(R.string.CreateNewThemeHelp), 1).show();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0541, code lost:
    
        if (r0.equals("CHAT_SEND_ROUNDVIDEOS_FORBIDDEN") == false) goto L318;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x05cb, code lost:
    
        if (r0.equals("SCHEDULE_TOO_MUCH") == false) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x010c, code lost:
    
        if (r21.text.startsWith("FLOOD_WAIT") != false) goto L467;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0152, code lost:
    
        showAddUserAlert(r21.text, r2, false, r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x014e, code lost:
    
        if (r21.text.startsWith("FLOOD_WAIT") != false) goto L467;
     */
    /* JADX WARN: Removed duplicated region for block: B:325:0x05c3  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x05e6  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x05ea  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x05fd  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x05c5  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x05ce  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x05d7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Dialog processError(int i, TLRPC.TL_error tL_error, BaseFragment baseFragment, TLObject tLObject, Object... objArr) {
        String str;
        BaseFragment lastFragment;
        String string;
        int i2;
        BaseFragment lastFragment2;
        String str2;
        int i3;
        long j;
        TLRPC.InputPeer inputPeer;
        String str3;
        String str4;
        int i4;
        int i5;
        String str5;
        String str6;
        int i6;
        StringBuilder sb;
        int i7;
        String sb2;
        int i8;
        String str7;
        char c = 2;
        boolean z = false;
        if (tL_error != null && tL_error.code != 406 && (str = tL_error.text) != null) {
            boolean z2 = tLObject instanceof TLRPC.TL_messages_sendMessage;
            if (z2 && str.contains("PRIVACY_PREMIUM_REQUIRED")) {
                long peerDialogId = DialogObject.getPeerDialogId(((TLRPC.TL_messages_sendMessage) tLObject).peer);
                if (peerDialogId >= 0) {
                    str7 = UserObject.getFirstName(MessagesController.getInstance(i).getUser(Long.valueOf(peerDialogId)));
                } else {
                    TLRPC.Chat chat = MessagesController.getInstance(i).getChat(Long.valueOf(-peerDialogId));
                    str7 = chat != null ? chat.title : "";
                }
                showSimpleAlert(baseFragment == null ? LaunchActivity.getLastFragment() : baseFragment, LocaleController.getString(R.string.MessagePremiumErrorTitle), LocaleController.formatString(R.string.MessagePremiumErrorMessage, str7));
                MessagesController.getInstance(i).invalidateUserPremiumBlocked(peerDialogId, 0);
            } else {
                boolean z3 = tLObject instanceof TLRPC.TL_messages_initHistoryImport;
                if (z3 || (tLObject instanceof TLRPC.TL_messages_checkHistoryImportPeer) || (tLObject instanceof TLRPC.TL_messages_checkHistoryImport) || (tLObject instanceof TLRPC.TL_messages_startHistoryImport)) {
                    TLRPC.InputPeer inputPeer2 = z3 ? ((TLRPC.TL_messages_initHistoryImport) tLObject).peer : tLObject instanceof TLRPC.TL_messages_startHistoryImport ? ((TLRPC.TL_messages_startHistoryImport) tLObject).peer : null;
                    lastFragment = baseFragment == null ? LaunchActivity.getLastFragment() : baseFragment;
                    if (tL_error.text.contains("USER_IS_BLOCKED")) {
                        string = LocaleController.getString(R.string.ImportErrorTitle);
                        i2 = R.string.ImportErrorUserBlocked;
                    } else if (tL_error.text.contains("USER_NOT_MUTUAL_CONTACT")) {
                        string = LocaleController.getString(R.string.ImportErrorTitle);
                        i2 = R.string.ImportMutualError;
                    } else if (tL_error.text.contains("IMPORT_PEER_TYPE_INVALID")) {
                        if (inputPeer2 instanceof TLRPC.TL_inputPeerUser) {
                            string = LocaleController.getString(R.string.ImportErrorTitle);
                            i2 = R.string.ImportErrorChatInvalidUser;
                        } else {
                            string = LocaleController.getString(R.string.ImportErrorTitle);
                            i2 = R.string.ImportErrorChatInvalidGroup;
                        }
                    } else if (tL_error.text.contains("CHAT_ADMIN_REQUIRED")) {
                        string = LocaleController.getString(R.string.ImportErrorTitle);
                        i2 = R.string.ImportErrorNotAdmin;
                    } else if (tL_error.text.startsWith("IMPORT_FORMAT")) {
                        string = LocaleController.getString(R.string.ImportErrorTitle);
                        i2 = R.string.ImportErrorFileFormatInvalid;
                    } else if (tL_error.text.startsWith("PEER_ID_INVALID")) {
                        string = LocaleController.getString(R.string.ImportErrorTitle);
                        i2 = R.string.ImportErrorPeerInvalid;
                    } else if (tL_error.text.contains("IMPORT_LANG_NOT_FOUND")) {
                        string = LocaleController.getString(R.string.ImportErrorTitle);
                        i2 = R.string.ImportErrorFileLang;
                    } else if (tL_error.text.contains("IMPORT_UPLOAD_FAILED")) {
                        string = LocaleController.getString(R.string.ImportErrorTitle);
                        i2 = R.string.ImportFailedToUpload;
                    } else {
                        if (!tL_error.text.startsWith("FLOOD_WAIT")) {
                            showSimpleAlert(lastFragment, LocaleController.getString(R.string.ImportErrorTitle), LocaleController.getString(R.string.ErrorOccurred) + "\n" + tL_error.text);
                        }
                        showFloodWaitAlert(tL_error.text, lastFragment);
                    }
                    showSimpleAlert(lastFragment, string, LocaleController.getString(i2));
                } else if ((tLObject instanceof TLRPC.TL_account_saveSecureValue) || (tLObject instanceof TLRPC.TL_account_getAuthorizationForm)) {
                    lastFragment2 = baseFragment == null ? LaunchActivity.getLastFragment() : baseFragment;
                    if (tL_error.text.contains("PHONE_NUMBER_INVALID")) {
                        i3 = R.string.InvalidPhoneNumber;
                        str2 = LocaleController.getString(i3);
                        showSimpleAlert(lastFragment2, str2);
                    } else {
                        if (!tL_error.text.startsWith("FLOOD_WAIT")) {
                            if ("APP_VERSION_OUTDATED".equals(tL_error.text)) {
                                showUpdateAppAlert(lastFragment2.getParentActivity(), LocaleController.getString(R.string.UpdateAppAlert), true);
                            } else {
                                str2 = LocaleController.getString(R.string.ErrorOccurred) + "\n" + tL_error.text;
                                showSimpleAlert(lastFragment2, str2);
                            }
                        }
                        i3 = R.string.FloodWait;
                        str2 = LocaleController.getString(i3);
                        showSimpleAlert(lastFragment2, str2);
                    }
                } else {
                    boolean z4 = tLObject instanceof TLRPC.TL_channels_joinChannel;
                    if (z4 || (tLObject instanceof TLRPC.TL_channels_editAdmin) || (tLObject instanceof TLRPC.TL_channels_inviteToChannel) || (tLObject instanceof TLRPC.TL_messages_addChatUser) || (tLObject instanceof TLRPC.TL_messages_startBot) || (tLObject instanceof TLRPC.TL_channels_editBanned) || (tLObject instanceof TLRPC.TL_messages_editChatDefaultBannedRights) || (tLObject instanceof TLRPC.TL_messages_editChatAdmin) || (tLObject instanceof TLRPC.TL_messages_migrateChat) || (tLObject instanceof TLRPC.TL_phone_inviteToGroupCall)) {
                        if (baseFragment != null && tL_error.text.equals("CHANNELS_TOO_MUCH")) {
                            if (baseFragment.getParentActivity() != null) {
                                baseFragment.showDialog(new LimitReachedBottomSheet(baseFragment, baseFragment.getParentActivity(), 5, i, null));
                            } else {
                                baseFragment.presentFragment((z4 || (tLObject instanceof TLRPC.TL_channels_inviteToChannel)) ? new TooManyCommunitiesActivity(0) : new TooManyCommunitiesActivity(1));
                            }
                            return null;
                        }
                        String str8 = tL_error.text;
                        if (baseFragment != null) {
                            if (objArr != null && objArr.length > 0) {
                                z = ((Boolean) objArr[0]).booleanValue();
                            }
                            showAddUserAlert(str8, baseFragment, z, tLObject);
                        } else if (str8.equals("PEER_FLOOD")) {
                            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShowAlert, 1);
                        }
                    } else if (tLObject instanceof TLRPC.TL_messages_createChat) {
                        lastFragment = baseFragment == null ? LaunchActivity.getLastFragment() : baseFragment;
                        if (tL_error.text.equals("CHANNELS_TOO_MUCH")) {
                            if (lastFragment.getParentActivity() != null) {
                                lastFragment.showDialog(new LimitReachedBottomSheet(lastFragment, lastFragment.getParentActivity(), 5, i, null));
                            } else {
                                lastFragment.presentFragment(new TooManyCommunitiesActivity(2));
                            }
                            return null;
                        }
                    } else if (tLObject instanceof TLRPC.TL_channels_createChannel) {
                        lastFragment = baseFragment == null ? LaunchActivity.getLastFragment() : baseFragment;
                        if (tL_error.text.equals("CHANNELS_TOO_MUCH")) {
                            if (lastFragment.getParentActivity() != null) {
                                lastFragment.showDialog(new LimitReachedBottomSheet(lastFragment, lastFragment.getParentActivity(), 5, i, null));
                            } else {
                                lastFragment.presentFragment(new TooManyCommunitiesActivity(2));
                            }
                            return null;
                        }
                    } else if (!(tLObject instanceof TLRPC.TL_messages_editMessage)) {
                        if (z2 || (tLObject instanceof TLRPC.TL_messages_sendMedia) || (tLObject instanceof TLRPC.TL_messages_sendInlineBotResult) || (tLObject instanceof TLRPC.TL_messages_forwardMessages) || (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) || (tLObject instanceof TLRPC.TL_messages_sendScheduledMessages)) {
                            if (z2) {
                                inputPeer = ((TLRPC.TL_messages_sendMessage) tLObject).peer;
                            } else if (tLObject instanceof TLRPC.TL_messages_sendMedia) {
                                inputPeer = ((TLRPC.TL_messages_sendMedia) tLObject).peer;
                            } else if (tLObject instanceof TLRPC.TL_messages_sendInlineBotResult) {
                                inputPeer = ((TLRPC.TL_messages_sendInlineBotResult) tLObject).peer;
                            } else if (tLObject instanceof TLRPC.TL_messages_forwardMessages) {
                                inputPeer = ((TLRPC.TL_messages_forwardMessages) tLObject).to_peer;
                            } else if (tLObject instanceof TLRPC.TL_messages_sendMultiMedia) {
                                inputPeer = ((TLRPC.TL_messages_sendMultiMedia) tLObject).peer;
                            } else if (tLObject instanceof TLRPC.TL_messages_sendScheduledMessages) {
                                inputPeer = ((TLRPC.TL_messages_sendScheduledMessages) tLObject).peer;
                            } else {
                                j = 0;
                                str3 = tL_error.text;
                                char c2 = 5;
                                if (str3 != null || !str3.startsWith("CHAT_SEND_") || !tL_error.text.endsWith("FORBIDDEN")) {
                                    str4 = tL_error.text;
                                    str4.hashCode();
                                    switch (str4.hashCode()) {
                                        case -1809401834:
                                            if (str4.equals("USER_BANNED_IN_CHANNEL")) {
                                                c = 0;
                                                break;
                                            }
                                            c = 65535;
                                            break;
                                        case -454039871:
                                            if (str4.equals("PEER_FLOOD")) {
                                                c = 1;
                                                break;
                                            }
                                            c = 65535;
                                            break;
                                        case 1169786080:
                                            break;
                                        default:
                                            c = 65535;
                                            break;
                                    }
                                    switch (c) {
                                        case 0:
                                            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShowAlert, 5);
                                            break;
                                        case 1:
                                            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShowAlert, 0);
                                            break;
                                        case 2:
                                            i4 = R.string.MessageScheduledLimitReached;
                                            str5 = LocaleController.getString(i4);
                                            break;
                                    }
                                } else {
                                    String str9 = tL_error.text;
                                    TLRPC.Chat chat2 = j < 0 ? MessagesController.getInstance(i).getChat(Long.valueOf(-j)) : null;
                                    String str10 = tL_error.text;
                                    str10.hashCode();
                                    switch (str10.hashCode()) {
                                        case -1813346101:
                                            if (str10.equals("CHAT_SEND_VOICES_FORBIDDEN")) {
                                                c2 = 0;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case -1755013292:
                                            if (str10.equals("CHAT_SEND_PLAIN_FORBIDDEN")) {
                                                c2 = 1;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case -1463451737:
                                            if (str10.equals("CHAT_SEND_AUDIOS_FORBIDDEN")) {
                                                c2 = 2;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case -446466679:
                                            if (str10.equals("CHAT_SEND_POLL_FORBIDDEN")) {
                                                c2 = 3;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case 469767429:
                                            if (str10.equals("CHAT_SEND_DOCS_FORBIDDEN")) {
                                                c2 = 4;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case 788688112:
                                            break;
                                        case 963091938:
                                            if (str10.equals("CHAT_SEND_VIDEOS_FORBIDDEN")) {
                                                c2 = 6;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case 1100757753:
                                            if (str10.equals("CHAT_SEND_GIFS_FORBIDDEN")) {
                                                c2 = 7;
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case 1146489803:
                                            if (str10.equals("CHAT_SEND_PHOTOS_FORBIDDEN")) {
                                                c2 = '\b';
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        case 1701620704:
                                            if (str10.equals("CHAT_SEND_STICKERS_FORBIDDEN")) {
                                                c2 = '\t';
                                                break;
                                            }
                                            c2 = 65535;
                                            break;
                                        default:
                                            c2 = 65535;
                                            break;
                                    }
                                    switch (c2) {
                                        case 0:
                                            i5 = 20;
                                            str9 = ChatObject.getRestrictedErrorText(chat2, i5);
                                            break;
                                        case 1:
                                            i5 = 22;
                                            str9 = ChatObject.getRestrictedErrorText(chat2, i5);
                                            break;
                                        case 2:
                                            i5 = 18;
                                            str9 = ChatObject.getRestrictedErrorText(chat2, i5);
                                            break;
                                        case 3:
                                            i5 = 10;
                                            str9 = ChatObject.getRestrictedErrorText(chat2, i5);
                                            break;
                                        case 4:
                                            i5 = 19;
                                            str9 = ChatObject.getRestrictedErrorText(chat2, i5);
                                            break;
                                        case 5:
                                            i5 = 21;
                                            str9 = ChatObject.getRestrictedErrorText(chat2, i5);
                                            break;
                                        case 6:
                                            i5 = 17;
                                            str9 = ChatObject.getRestrictedErrorText(chat2, i5);
                                            break;
                                        case 7:
                                            i5 = 23;
                                            str9 = ChatObject.getRestrictedErrorText(chat2, i5);
                                            break;
                                        case '\b':
                                            i5 = 16;
                                            str9 = ChatObject.getRestrictedErrorText(chat2, i5);
                                            break;
                                        case '\t':
                                            str9 = ChatObject.getRestrictedErrorText(chat2, 8);
                                            break;
                                    }
                                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 1, str9);
                                }
                            }
                            j = DialogObject.getPeerDialogId(inputPeer);
                            str3 = tL_error.text;
                            char c22 = 5;
                            if (str3 != null) {
                            }
                            str4 = tL_error.text;
                            str4.hashCode();
                            switch (str4.hashCode()) {
                                case -1809401834:
                                    break;
                                case -454039871:
                                    break;
                                case 1169786080:
                                    break;
                            }
                            switch (c) {
                            }
                        } else if (tLObject instanceof TLRPC.TL_messages_importChatInvite) {
                            lastFragment2 = baseFragment == null ? LaunchActivity.getLastFragment() : baseFragment;
                            if (!tL_error.text.startsWith("FLOOD_WAIT")) {
                                if (tL_error.text.equals("USERS_TOO_MUCH")) {
                                    i3 = R.string.JoinToGroupErrorFull;
                                } else if (tL_error.text.equals("CHANNELS_TOO_MUCH")) {
                                    if (lastFragment2.getParentActivity() != null) {
                                        lastFragment2.showDialog(new LimitReachedBottomSheet(lastFragment2, lastFragment2.getParentActivity(), 5, i, null));
                                    } else {
                                        lastFragment2.presentFragment(new TooManyCommunitiesActivity(0));
                                    }
                                } else if (tL_error.text.equals("INVITE_HASH_EXPIRED")) {
                                    showSimpleAlert(lastFragment2, LocaleController.getString(R.string.ExpiredLink), LocaleController.getString(R.string.InviteExpired));
                                } else {
                                    i3 = R.string.JoinToGroupErrorNotExist;
                                }
                                str2 = LocaleController.getString(i3);
                                showSimpleAlert(lastFragment2, str2);
                            }
                            i3 = R.string.FloodWait;
                            str2 = LocaleController.getString(i3);
                            showSimpleAlert(lastFragment2, str2);
                        } else {
                            if (!(tLObject instanceof TLRPC.TL_messages_getAttachedStickers)) {
                                if ((tLObject instanceof TLRPC.TL_account_confirmPhone) || (tLObject instanceof TLRPC.TL_account_verifyPhone) || (tLObject instanceof TLRPC.TL_account_verifyEmail)) {
                                    if (!tL_error.text.contains("PHONE_CODE_EMPTY") && !tL_error.text.contains("PHONE_CODE_INVALID") && !tL_error.text.contains("CODE_INVALID") && !tL_error.text.contains("CODE_EMPTY")) {
                                        if (!tL_error.text.contains("PHONE_CODE_EXPIRED") && !tL_error.text.contains("EMAIL_VERIFY_EXPIRED")) {
                                            if (!tL_error.text.startsWith("FLOOD_WAIT")) {
                                                str6 = tL_error.text;
                                                return showSimpleAlert(baseFragment, str6);
                                            }
                                            i6 = R.string.FloodWait;
                                        }
                                        i6 = R.string.CodeExpired;
                                    }
                                    i6 = R.string.InvalidCode;
                                } else if (tLObject instanceof TLRPC.TL_auth_resendCode) {
                                    if (tL_error.text.contains("PHONE_NUMBER_INVALID")) {
                                        i6 = R.string.InvalidPhoneNumber;
                                    } else {
                                        if (!tL_error.text.contains("PHONE_CODE_EMPTY") && !tL_error.text.contains("PHONE_CODE_INVALID")) {
                                            if (!tL_error.text.contains("PHONE_CODE_EXPIRED")) {
                                                if (!tL_error.text.startsWith("FLOOD_WAIT")) {
                                                    if (tL_error.code != -1000) {
                                                        str6 = LocaleController.getString(R.string.ErrorOccurred) + "\n" + tL_error.text;
                                                        return showSimpleAlert(baseFragment, str6);
                                                    }
                                                }
                                                i6 = R.string.FloodWait;
                                            }
                                            i6 = R.string.CodeExpired;
                                        }
                                        i6 = R.string.InvalidCode;
                                    }
                                } else if (tLObject instanceof TLRPC.TL_account_sendConfirmPhoneCode) {
                                    if (tL_error.code == 400) {
                                        i6 = R.string.CancelLinkExpired;
                                    } else {
                                        if (!tL_error.text.startsWith("FLOOD_WAIT")) {
                                            i6 = R.string.ErrorOccurred;
                                        }
                                        i6 = R.string.FloodWait;
                                    }
                                } else if (!(tLObject instanceof TLRPC.TL_account_changePhone)) {
                                    if (tLObject instanceof TLRPC.TL_account_sendChangePhoneCode) {
                                        if (tL_error.text.contains("PHONE_NUMBER_INVALID")) {
                                            LoginActivity.needShowInvalidAlert(baseFragment, (String) objArr[0], false);
                                        } else {
                                            if (!tL_error.text.contains("PHONE_CODE_EMPTY") && !tL_error.text.contains("PHONE_CODE_INVALID")) {
                                                if (!tL_error.text.contains("PHONE_CODE_EXPIRED")) {
                                                    if (!tL_error.text.startsWith("FLOOD_WAIT")) {
                                                        if (tL_error.text.startsWith("PHONE_NUMBER_OCCUPIED")) {
                                                            sb2 = LocaleController.formatString("ChangePhoneNumberOccupied", R.string.ChangePhoneNumberOccupied, objArr[0]);
                                                            showSimpleAlert(baseFragment, sb2);
                                                        } else {
                                                            if (tL_error.text.startsWith("PHONE_NUMBER_BANNED")) {
                                                                LoginActivity.needShowInvalidAlert(baseFragment, (String) objArr[0], true);
                                                            }
                                                            i8 = R.string.ErrorOccurred;
                                                        }
                                                    }
                                                    i8 = R.string.FloodWait;
                                                }
                                                i8 = R.string.CodeExpired;
                                            }
                                            i8 = R.string.InvalidCode;
                                        }
                                    } else if (tLObject instanceof TLRPC.TL_account_updateUsername) {
                                        String str11 = tL_error.text;
                                        str11.hashCode();
                                        if (str11.equals("USERNAME_INVALID")) {
                                            i8 = R.string.UsernameInvalid;
                                        } else {
                                            if (str11.equals("USERNAME_OCCUPIED")) {
                                                i8 = R.string.UsernameInUse;
                                            }
                                            i8 = R.string.ErrorOccurred;
                                        }
                                    } else {
                                        if (tLObject instanceof TLRPC.TL_contacts_importContacts) {
                                            if (!tL_error.text.startsWith("FLOOD_WAIT")) {
                                                sb = new StringBuilder();
                                                i7 = R.string.ErrorOccurred;
                                            }
                                            i8 = R.string.FloodWait;
                                        } else {
                                            if ((tLObject instanceof TLRPC.TL_account_getPassword) || (tLObject instanceof TLRPC.TL_account_getTmpPassword)) {
                                                if (tL_error.text.startsWith("FLOOD_WAIT")) {
                                                    str5 = getFloodWaitString(tL_error.text);
                                                }
                                            } else if (tLObject instanceof TLRPC.TL_payments_sendPaymentForm) {
                                                String str12 = tL_error.text;
                                                str12.hashCode();
                                                if (str12.equals("BOT_PRECHECKOUT_FAILED")) {
                                                    i4 = R.string.PaymentPrecheckoutFailed;
                                                } else if (str12.equals("PAYMENT_FAILED")) {
                                                    i4 = R.string.PaymentFailed;
                                                }
                                                str5 = LocaleController.getString(i4);
                                            } else if (tLObject instanceof TLRPC.TL_payments_validateRequestedInfo) {
                                                String str13 = tL_error.text;
                                                str13.hashCode();
                                                if (str13.equals("SHIPPING_NOT_AVAILABLE")) {
                                                    i4 = R.string.PaymentNoShippingMethod;
                                                    str5 = LocaleController.getString(i4);
                                                }
                                            } else if (tLObject instanceof TLRPC.TL_payments_assignPlayMarketTransaction) {
                                                sb = new StringBuilder();
                                                i7 = R.string.PaymentConfirmationError;
                                            }
                                            str5 = tL_error.text;
                                        }
                                        sb.append(LocaleController.getString(i7));
                                        sb.append("\n");
                                        sb.append(tL_error.text);
                                        sb2 = sb.toString();
                                        showSimpleAlert(baseFragment, sb2);
                                    }
                                    sb2 = LocaleController.getString(i8);
                                    showSimpleAlert(baseFragment, sb2);
                                } else if (tL_error.text.contains("PHONE_NUMBER_INVALID")) {
                                    i8 = R.string.InvalidPhoneNumber;
                                    sb2 = LocaleController.getString(i8);
                                    showSimpleAlert(baseFragment, sb2);
                                } else {
                                    if (!tL_error.text.contains("PHONE_CODE_EMPTY") && !tL_error.text.contains("PHONE_CODE_INVALID")) {
                                        if (!tL_error.text.contains("PHONE_CODE_EXPIRED")) {
                                            if (!tL_error.text.startsWith("FLOOD_WAIT")) {
                                                if (tL_error.text.contains("FRESH_CHANGE_PHONE_FORBIDDEN")) {
                                                    showSimpleAlert(baseFragment, LocaleController.getString(R.string.FreshChangePhoneForbiddenTitle), LocaleController.getString(R.string.FreshChangePhoneForbidden));
                                                } else {
                                                    sb2 = tL_error.text;
                                                    showSimpleAlert(baseFragment, sb2);
                                                }
                                            }
                                            i8 = R.string.FloodWait;
                                            sb2 = LocaleController.getString(i8);
                                            showSimpleAlert(baseFragment, sb2);
                                        }
                                        i8 = R.string.CodeExpired;
                                        sb2 = LocaleController.getString(i8);
                                        showSimpleAlert(baseFragment, sb2);
                                    }
                                    i8 = R.string.InvalidCode;
                                    sb2 = LocaleController.getString(i8);
                                    showSimpleAlert(baseFragment, sb2);
                                }
                                str6 = LocaleController.getString(i6);
                                return showSimpleAlert(baseFragment, str6);
                            }
                            if (baseFragment != null && baseFragment.getParentActivity() != null) {
                                Toast.makeText(baseFragment.getParentActivity(), LocaleController.getString(R.string.ErrorOccurred) + "\n" + tL_error.text, 0).show();
                            }
                        }
                        showSimpleToast(baseFragment, str5);
                    } else if (!tL_error.text.equals("MESSAGE_NOT_MODIFIED")) {
                        sb2 = LocaleController.getString(R.string.EditMessageError);
                        if (baseFragment == null) {
                            showSimpleToast(null, sb2);
                        }
                        showSimpleAlert(baseFragment, sb2);
                    }
                }
            }
        }
        return null;
    }

    public static void showAddUserAlert(String str, final BaseFragment baseFragment, boolean z, TLObject tLObject) {
        int i;
        String string;
        if (str == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.AppName));
        char c = 65535;
        switch (str.hashCode()) {
            case -2120721660:
                if (str.equals("CHANNELS_ADMIN_LOCATED_TOO_MUCH")) {
                    c = 0;
                    break;
                }
                break;
            case -2012133105:
                if (str.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH")) {
                    c = 1;
                    break;
                }
                break;
            case -1763467626:
                if (str.equals("USERS_TOO_FEW")) {
                    c = 2;
                    break;
                }
                break;
            case -538116776:
                if (str.equals("USER_BLOCKED")) {
                    c = 3;
                    break;
                }
                break;
            case -512775857:
                if (str.equals("USER_RESTRICTED")) {
                    c = 4;
                    break;
                }
                break;
            case -454039871:
                if (str.equals("PEER_FLOOD")) {
                    c = 5;
                    break;
                }
                break;
            case -420079733:
                if (str.equals("BOTS_TOO_MUCH")) {
                    c = 6;
                    break;
                }
                break;
            case 98635865:
                if (str.equals("USER_KICKED")) {
                    c = 7;
                    break;
                }
                break;
            case 517420851:
                if (str.equals("USER_BOT")) {
                    c = '\b';
                    break;
                }
                break;
            case 845559454:
                if (str.equals("YOU_BLOCKED_USER")) {
                    c = '\t';
                    break;
                }
                break;
            case 916342611:
                if (str.equals("USER_ADMIN_INVALID")) {
                    c = '\n';
                    break;
                }
                break;
            case 1047173446:
                if (str.equals("CHAT_ADMIN_BAN_REQUIRED")) {
                    c = 11;
                    break;
                }
                break;
            case 1167301807:
                if (str.equals("USERS_TOO_MUCH")) {
                    c = '\f';
                    break;
                }
                break;
            case 1227003815:
                if (str.equals("USER_ID_INVALID")) {
                    c = '\r';
                    break;
                }
                break;
            case 1253103379:
                if (str.equals("ADMINS_TOO_MUCH")) {
                    c = 14;
                    break;
                }
                break;
            case 1355367367:
                if (str.equals("CHANNELS_TOO_MUCH")) {
                    c = 15;
                    break;
                }
                break;
            case 1377621075:
                if (str.equals("USER_CHANNELS_TOO_MUCH")) {
                    c = 16;
                    break;
                }
                break;
            case 1623167701:
                if (str.equals("USER_NOT_MUTUAL_CONTACT")) {
                    c = 17;
                    break;
                }
                break;
            case 1754587486:
                if (str.equals("CHAT_ADMIN_INVITE_REQUIRED")) {
                    c = 18;
                    break;
                }
                break;
            case 1916725894:
                if (str.equals("USER_PRIVACY_RESTRICTED")) {
                    c = 19;
                    break;
                }
                break;
            case 1965565720:
                if (str.equals("USER_ALREADY_PARTICIPANT")) {
                    c = 20;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                i = R.string.LocatedChannelsTooMuch;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 1:
                i = R.string.PublicChannelsTooMuch;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 2:
                i = R.string.CreateGroupError;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 3:
            case '\b':
            case '\r':
                i = z ? R.string.ChannelUserCantAdd : R.string.GroupUserCantAdd;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 4:
                i = R.string.UserRestricted;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 5:
                builder.setMessage(LocaleController.getString(R.string.NobodyLikesSpam2));
                builder.setNegativeButton(LocaleController.getString(R.string.MoreInfo), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda5
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        AlertsCreator.lambda$showAddUserAlert$129(BaseFragment.this, dialogInterface, i2);
                    }
                });
                break;
            case 6:
                i = z ? R.string.ChannelUserCantBot : R.string.GroupUserCantBot;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 7:
            case 11:
                i = tLObject instanceof TLRPC.TL_channels_inviteToChannel ? R.string.AddUserErrorBlacklisted : R.string.AddAdminErrorBlacklisted;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case '\t':
                i = R.string.YouBlockedUser;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case '\n':
                i = R.string.AddBannedErrorAdmin;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case '\f':
                i = z ? R.string.ChannelUserAddLimit : R.string.GroupUserAddLimit;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 14:
                i = z ? R.string.ChannelUserCantAdmin : R.string.GroupUserCantAdmin;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 15:
                builder.setTitle(LocaleController.getString(R.string.ChannelTooMuchTitle));
                i = tLObject instanceof TLRPC.TL_channels_createChannel ? R.string.ChannelTooMuch : R.string.ChannelTooMuchJoin;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 16:
                builder.setTitle(LocaleController.getString(R.string.ChannelTooMuchTitle));
                i = R.string.UserChannelTooMuchJoin;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 17:
                i = z ? R.string.ChannelUserLeftError : R.string.GroupUserLeftError;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 18:
                i = R.string.AddAdminErrorNotAMember;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 19:
                i = z ? R.string.InviteToChannelError : R.string.InviteToGroupError;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            case 20:
                builder.setTitle(LocaleController.getString(R.string.VoipGroupVoiceChat));
                i = R.string.VoipGroupInviteAlreadyParticipant;
                string = LocaleController.getString(i);
                builder.setMessage(string);
                break;
            default:
                string = LocaleController.getString(R.string.ErrorOccurred) + "\n" + str;
                builder.setMessage(string);
                break;
        }
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        baseFragment.showDialog(builder.create(), true, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:11:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0121  */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v5, types: [int, boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void showBlockReportSpamAlert(BaseFragment baseFragment, final long j, final TLRPC.User user, final TLRPC.Chat chat, final TLRPC.EncryptedChat encryptedChat, final boolean z, TLRPC.ChatFull chatFull, final MessagesStorage.IntCallback intCallback, Theme.ResourcesProvider resourcesProvider) {
        boolean z2;
        int i;
        CharSequence string;
        CharSequence string2;
        final CheckBoxCell[] checkBoxCellArr;
        TextView textView;
        CharSequence charSequence;
        Theme.ResourcesProvider resourcesProvider2 = resourcesProvider;
        ?? r4 = 1;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        final AccountInstance accountInstance = baseFragment.getAccountInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider2);
        SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(baseFragment.getCurrentAccount());
        if (encryptedChat == null) {
            if (!notificationsSettings.getBoolean("dialog_bar_report" + j, false)) {
                z2 = false;
                if (user == null) {
                    builder.setTitle(LocaleController.formatString("BlockUserTitle", R.string.BlockUserTitle, UserObject.getFirstName(user)));
                    builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BlockUserAlert", R.string.BlockUserAlert, UserObject.getFirstName(user))));
                    CharSequence string3 = LocaleController.getString(R.string.BlockContact);
                    final CheckBoxCell[] checkBoxCellArr2 = new CheckBoxCell[2];
                    LinearLayout linearLayout = new LinearLayout(baseFragment.getParentActivity());
                    linearLayout.setOrientation(1);
                    int i2 = 0;
                    for (int i3 = 2; i2 < i3; i3 = 2) {
                        if (i2 != 0 || z2) {
                            CheckBoxCell checkBoxCell = new CheckBoxCell(baseFragment.getParentActivity(), r4, resourcesProvider2);
                            checkBoxCellArr2[i2] = checkBoxCell;
                            checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            checkBoxCellArr2[i2].setTag(Integer.valueOf(i2));
                            if (i2 == 0) {
                                charSequence = string3;
                                checkBoxCellArr2[i2].setText(LocaleController.getString(R.string.DeleteReportSpam), "", r4, false);
                            } else {
                                charSequence = string3;
                                checkBoxCellArr2[i2].setText(LocaleController.formatString("DeleteThisChat", R.string.DeleteThisChat, new Object[0]), "", true, false);
                            }
                            checkBoxCellArr2[i2].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                            linearLayout.addView(checkBoxCellArr2[i2], LayoutHelper.createLinear(-1, -2));
                            checkBoxCellArr2[i2].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda74
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    AlertsCreator.lambda$showBlockReportSpamAlert$15(checkBoxCellArr2, view);
                                }
                            });
                        } else {
                            charSequence = string3;
                        }
                        i2++;
                        resourcesProvider2 = resourcesProvider;
                        string3 = charSequence;
                        r4 = 1;
                    }
                    builder.setView(linearLayout);
                    checkBoxCellArr = checkBoxCellArr2;
                    string2 = string3;
                } else {
                    if (chat == null || !z) {
                        builder.setTitle(LocaleController.getString(R.string.ReportSpamTitle));
                        i = (!ChatObject.isChannel(chat) || chat.megagroup) ? R.string.ReportSpamAlertGroup : R.string.ReportSpamAlertChannel;
                    } else {
                        builder.setTitle(LocaleController.getString(R.string.ReportUnrelatedGroup));
                        if (chatFull != null) {
                            TLRPC.ChannelLocation channelLocation = chatFull.location;
                            if (channelLocation instanceof TLRPC.TL_channelLocation) {
                                string = AndroidUtilities.replaceTags(LocaleController.formatString("ReportUnrelatedGroupText", R.string.ReportUnrelatedGroupText, ((TLRPC.TL_channelLocation) channelLocation).address));
                                builder.setMessage(string);
                                string2 = LocaleController.getString(R.string.ReportChat);
                                checkBoxCellArr = null;
                            }
                        }
                        i = R.string.ReportUnrelatedGroupTextNoAddress;
                    }
                    string = LocaleController.getString(i);
                    builder.setMessage(string);
                    string2 = LocaleController.getString(R.string.ReportChat);
                    checkBoxCellArr = null;
                }
                builder.setPositiveButton(string2, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda75
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        AlertsCreator.lambda$showBlockReportSpamAlert$16(TLRPC.User.this, accountInstance, checkBoxCellArr, j, chat, encryptedChat, z, intCallback, dialogInterface, i4);
                    }
                });
                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                AlertDialog create = builder.create();
                baseFragment.showDialog(create);
                textView = (TextView) create.getButton(-1);
                if (textView == null) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                    return;
                }
                return;
            }
        }
        z2 = true;
        if (user == null) {
        }
        builder.setPositiveButton(string2, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda75
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i4) {
                AlertsCreator.lambda$showBlockReportSpamAlert$16(TLRPC.User.this, accountInstance, checkBoxCellArr, j, chat, encryptedChat, z, intCallback, dialogInterface, i4);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog create2 = builder.create();
        baseFragment.showDialog(create2);
        textView = (TextView) create2.getButton(-1);
        if (textView == null) {
        }
    }

    public static void showBlockReportSpamReplyAlert(final ChatActivity chatActivity, final MessageObject messageObject, long j, final Theme.ResourcesProvider resourcesProvider, final Runnable runnable) {
        if (chatActivity == null || chatActivity.getParentActivity() == null || messageObject == null) {
            return;
        }
        final AccountInstance accountInstance = chatActivity.getAccountInstance();
        TLRPC.User user = j > 0 ? accountInstance.getMessagesController().getUser(Long.valueOf(j)) : null;
        TLRPC.Chat chat = j < 0 ? accountInstance.getMessagesController().getChat(Long.valueOf(-j)) : null;
        if (user == null && chat == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(chatActivity.getParentActivity(), resourcesProvider);
        builder.setDimEnabled(runnable == null);
        builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda42
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AlertsCreator.lambda$showBlockReportSpamReplyAlert$11(runnable, dialogInterface);
            }
        });
        builder.setTitle(LocaleController.getString(R.string.BlockUser));
        int i = R.string.BlockUserReplyAlert;
        builder.setMessage(AndroidUtilities.replaceTags(user != null ? LocaleController.formatString("BlockUserReplyAlert", i, UserObject.getFirstName(user)) : LocaleController.formatString("BlockUserReplyAlert", i, chat.title)));
        LinearLayout linearLayout = new LinearLayout(chatActivity.getParentActivity());
        linearLayout.setOrientation(1);
        final CheckBoxCell[] checkBoxCellArr = {new CheckBoxCell(chatActivity.getParentActivity(), 1, resourcesProvider)};
        checkBoxCellArr[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
        checkBoxCellArr[0].setTag(0);
        checkBoxCellArr[0].setText(LocaleController.getString(R.string.DeleteReportSpam), "", true, false);
        checkBoxCellArr[0].setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
        linearLayout.addView(checkBoxCellArr[0], LayoutHelper.createLinear(-1, -2));
        checkBoxCellArr[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda43
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertsCreator.lambda$showBlockReportSpamReplyAlert$12(checkBoxCellArr, view);
            }
        });
        builder.setView(linearLayout);
        final TLRPC.User user2 = user;
        final TLRPC.Chat chat2 = chat;
        builder.setPositiveButton(LocaleController.getString(R.string.BlockAndDeleteReplies), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda44
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                AlertsCreator.lambda$showBlockReportSpamReplyAlert$14(TLRPC.User.this, accountInstance, chatActivity, chat2, messageObject, checkBoxCellArr, resourcesProvider, dialogInterface, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog create = builder.create();
        chatActivity.showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
        }
    }

    public static void showChatWithAdmin(BaseFragment baseFragment, TLRPC.User user, String str, boolean z, int i) {
        if (baseFragment.getParentActivity() == null) {
            return;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString(z ? R.string.ChatWithAdminChannelTitle : R.string.ChatWithAdminGroupTitle), true);
        LinearLayout linearLayout = new LinearLayout(baseFragment.getParentActivity());
        linearLayout.setOrientation(1);
        TextView textView = new TextView(baseFragment.getParentActivity());
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, 0, 21, 0, 21, 8));
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        textView.setTextSize(1, 16.0f);
        textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ChatWithAdminMessage", R.string.ChatWithAdminMessage, str, LocaleController.formatDateAudio(i, false))));
        TextView textView2 = new TextView(baseFragment.getParentActivity());
        textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        textView2.setGravity(17);
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setText(LocaleController.getString(R.string.IUnderstand));
        textView2.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
        textView2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed)));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, 48, 0, 16, 12, 16, 8));
        builder.setCustomView(linearLayout);
        final BottomSheet show = builder.show();
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda26
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BottomSheet.this.dismiss();
            }
        });
    }

    public static void showCustomNotificationsDialog(BaseFragment baseFragment, long j, int i, int i2, ArrayList arrayList, ArrayList arrayList2, int i3, MessagesStorage.IntCallback intCallback) {
        showCustomNotificationsDialog(baseFragment, j, i, i2, arrayList, arrayList2, i3, intCallback, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v0 */
    /* JADX WARN: Type inference failed for: r11v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r11v3 */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r12v3 */
    /* JADX WARN: Type inference failed for: r6v1, types: [android.widget.TextView, android.view.View] */
    /* JADX WARN: Type inference failed for: r9v1, types: [android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4 */
    public static void showCustomNotificationsDialog(final BaseFragment baseFragment, final long j, final int i, final int i2, final ArrayList arrayList, final ArrayList arrayList2, final int i3, final MessagesStorage.IntCallback intCallback, final MessagesStorage.IntCallback intCallback2) {
        PorterDuffColorFilter porterDuffColorFilter;
        int i4;
        final AlertDialog.Builder builder;
        Object obj;
        Drawable drawable;
        ?? r12 = 0;
        ?? r11 = 1;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        final boolean isGlobalNotificationsEnabled = NotificationsController.getInstance(i3).isGlobalNotificationsEnabled(j, false, false);
        String string = LocaleController.getString(R.string.NotificationsTurnOn);
        int i5 = R.string.MuteFor;
        Drawable drawable2 = null;
        String[] strArr = {string, LocaleController.formatString("MuteFor", i5, LocaleController.formatPluralString("Hours", 1, new Object[0])), LocaleController.formatString("MuteFor", i5, LocaleController.formatPluralString("Days", 2, new Object[0])), (j == 0 && (baseFragment instanceof NotificationsCustomSettingsActivity)) ? null : LocaleController.getString(R.string.NotificationsCustomize), LocaleController.getString(R.string.NotificationsTurnOff)};
        int[] iArr = {R.drawable.notifications_on, R.drawable.notifications_mute1h, R.drawable.notifications_mute2d, R.drawable.notifications_settings, R.drawable.notifications_off};
        LinearLayout linearLayout = new LinearLayout(baseFragment.getParentActivity());
        linearLayout.setOrientation(1);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(baseFragment.getParentActivity());
        int i6 = 0;
        View view = linearLayout;
        while (i6 < 5) {
            if (strArr[i6] == null) {
                i4 = i6;
                builder = builder2;
                obj = view;
                drawable = drawable2;
            } else {
                ?? textView = new TextView(baseFragment.getParentActivity());
                Drawable drawable3 = baseFragment.getParentActivity().getResources().getDrawable(iArr[i6]);
                if (i6 == 4) {
                    textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                    porterDuffColorFilter = new PorterDuffColorFilter(Theme.getColor(Theme.key_text_RedRegular), PorterDuff.Mode.MULTIPLY);
                } else {
                    textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                    porterDuffColorFilter = new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogIcon), PorterDuff.Mode.MULTIPLY);
                }
                drawable3.setColorFilter(porterDuffColorFilter);
                textView.setTextSize(r11, 16.0f);
                textView.setLines(r11);
                textView.setMaxLines(r11);
                textView.setCompoundDrawablesWithIntrinsicBounds(drawable3, drawable2, drawable2, drawable2);
                textView.setTag(Integer.valueOf(i6));
                textView.setBackgroundDrawable(Theme.getSelectorDrawable(r12));
                textView.setPadding(AndroidUtilities.dp(24.0f), r12, AndroidUtilities.dp(24.0f), r12);
                textView.setSingleLine(r11);
                textView.setGravity(19);
                textView.setCompoundDrawablePadding(AndroidUtilities.dp(26.0f));
                textView.setText(strArr[i6]);
                view.addView(textView, LayoutHelper.createLinear(-1, 48, 51));
                i4 = i6;
                builder = builder2;
                obj = view;
                drawable = drawable2;
                textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda108
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        AlertsCreator.lambda$showCustomNotificationsDialog$17(j, i3, isGlobalNotificationsEnabled, i, intCallback2, i2, baseFragment, arrayList, arrayList2, intCallback, builder, view2);
                    }
                });
            }
            i6 = i4 + 1;
            view = obj;
            builder2 = builder;
            drawable2 = drawable;
            r11 = 1;
            r12 = 0;
        }
        AlertDialog.Builder builder3 = builder2;
        builder3.setTitle(LocaleController.getString(R.string.Notifications));
        builder3.setView(view);
        baseFragment.showDialog(builder3.create());
    }

    public static void showFloodWaitAlert(String str, BaseFragment baseFragment) {
        if (str == null || !str.startsWith("FLOOD_WAIT") || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        int intValue = Utilities.parseInt((CharSequence) str).intValue();
        String formatPluralString = intValue < 60 ? LocaleController.formatPluralString("Seconds", intValue, new Object[0]) : LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setMessage(LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        baseFragment.showDialog(builder.create(), true, null);
    }

    public static void showOpenUrlAlert(BaseFragment baseFragment, String str, boolean z, boolean z2) {
        showOpenUrlAlert(baseFragment, str, z, true, z2, false, null, null);
    }

    public static void showOpenUrlAlert(BaseFragment baseFragment, String str, boolean z, boolean z2, Theme.ResourcesProvider resourcesProvider) {
        showOpenUrlAlert(baseFragment, str, z, true, z2, false, null, resourcesProvider);
    }

    public static void showOpenUrlAlert(BaseFragment baseFragment, String str, boolean z, boolean z2, boolean z3, Browser.Progress progress, Theme.ResourcesProvider resourcesProvider) {
        showOpenUrlAlert(baseFragment, str, z, z2, z3, false, progress, resourcesProvider);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void showOpenUrlAlert(final BaseFragment baseFragment, final String str, boolean z, final boolean z2, boolean z3, boolean z4, final Browser.Progress progress, Theme.ResourcesProvider resourcesProvider) {
        String replaceHostname;
        int indexOf;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        final long inlineReturn = baseFragment instanceof ChatActivity ? ((ChatActivity) baseFragment).getInlineReturn() : 0L;
        String scheme = str == null ? null : Uri.parse(str).getScheme();
        if (Browser.isInternalUrl(str, null) || !z3 || "mailto".equalsIgnoreCase(scheme)) {
            Browser.openUrl(baseFragment.getParentActivity(), Uri.parse(str), inlineReturn == 0, z2, z4 && checkInternalBotApp(str), progress, null, false, true, false);
            return;
        }
        if (z) {
            try {
                Uri parse = Uri.parse(str);
                replaceHostname = Browser.replaceHostname(parse, Browser.IDN_toUnicode(parse.getHost()), null);
            } catch (Exception e) {
                FileLog.e((Throwable) e, false);
            }
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    AlertsCreator.lambda$showOpenUrlAlert$19(BaseFragment.this, str, inlineReturn, z2, progress);
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider);
            builder.setTitle(LocaleController.getString(R.string.OpenUrlTitle));
            final AlertDialog[] alertDialogArr = new AlertDialog[1];
            SpannableString spannableString = new SpannableString(replaceHostname);
            spannableString.setSpan(new URLSpan(replaceHostname) { // from class: org.telegram.ui.Components.AlertsCreator.2
                @Override // android.text.style.URLSpan, android.text.style.ClickableSpan
                public void onClick(View view) {
                    runnable.run();
                    AlertDialog alertDialog = alertDialogArr[0];
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                }
            }, 0, spannableString.length(), 33);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.OpenUrlAlert2));
            indexOf = spannableStringBuilder.toString().indexOf("%1$s");
            if (indexOf >= 0) {
                spannableStringBuilder.replace(indexOf, indexOf + 4, (CharSequence) spannableString);
            }
            builder.setMessage(spannableStringBuilder);
            builder.setMessageTextViewClickable(false);
            builder.setPositiveButton(LocaleController.getString(R.string.Open), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    runnable.run();
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            AlertDialog create = builder.create();
            alertDialogArr[0] = create;
            baseFragment.showDialog(create);
        }
        replaceHostname = str;
        final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AlertsCreator.lambda$showOpenUrlAlert$19(BaseFragment.this, str, inlineReturn, z2, progress);
            }
        };
        AlertDialog.Builder builder2 = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider);
        builder2.setTitle(LocaleController.getString(R.string.OpenUrlTitle));
        final AlertDialog[] alertDialogArr2 = new AlertDialog[1];
        SpannableString spannableString2 = new SpannableString(replaceHostname);
        spannableString2.setSpan(new URLSpan(replaceHostname) { // from class: org.telegram.ui.Components.AlertsCreator.2
            @Override // android.text.style.URLSpan, android.text.style.ClickableSpan
            public void onClick(View view) {
                runnable2.run();
                AlertDialog alertDialog = alertDialogArr2[0];
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        }, 0, spannableString2.length(), 33);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(LocaleController.getString(R.string.OpenUrlAlert2));
        indexOf = spannableStringBuilder2.toString().indexOf("%1$s");
        if (indexOf >= 0) {
        }
        builder2.setMessage(spannableStringBuilder2);
        builder2.setMessageTextViewClickable(false);
        builder2.setPositiveButton(LocaleController.getString(R.string.Open), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                runnable2.run();
            }
        });
        builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog create2 = builder2.create();
        alertDialogArr2[0] = create2;
        baseFragment.showDialog(create2);
    }

    public static ActionBarPopupWindow showPopupMenu(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, View view, int i, int i2) {
        final android.graphics.Rect rect = new android.graphics.Rect();
        final ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
        actionBarPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        actionBarPopupWindow.setAnimationEnabled(true);
        actionBarPopupWindow.setOutsideTouchable(true);
        actionBarPopupWindow.setClippingEnabled(true);
        actionBarPopupWindow.setInputMethodMode(2);
        actionBarPopupWindow.setSoftInputMode(0);
        actionBarPopupWindow.setFocusable(true);
        actionBarPopupWindowLayout.setFocusableInTouchMode(true);
        actionBarPopupWindowLayout.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda101
            @Override // android.view.View.OnKeyListener
            public final boolean onKey(View view2, int i3, KeyEvent keyEvent) {
                boolean lambda$showPopupMenu$168;
                lambda$showPopupMenu$168 = AlertsCreator.lambda$showPopupMenu$168(ActionBarPopupWindow.this, view2, i3, keyEvent);
                return lambda$showPopupMenu$168;
            }
        });
        actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x - AndroidUtilities.dp(40.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, Integer.MIN_VALUE));
        actionBarPopupWindow.showAsDropDown(view, i, i2);
        actionBarPopupWindowLayout.updateRadialSelectors();
        ActionBarPopupWindow.startAnimation(actionBarPopupWindowLayout);
        actionBarPopupWindowLayout.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda102
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                boolean lambda$showPopupMenu$169;
                lambda$showPopupMenu$169 = AlertsCreator.lambda$showPopupMenu$169(ActionBarPopupWindow.this, rect, view2, motionEvent);
                return lambda$showPopupMenu$169;
            }
        });
        return actionBarPopupWindow;
    }

    public static AlertDialog showSecretLocationAlert(Context context, int i, final Runnable runnable, boolean z, Theme.ResourcesProvider resourcesProvider) {
        ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        int i2 = MessagesController.getInstance(i).availableMapProviders;
        if ((i2 & 1) != 0) {
            arrayList.add(LocaleController.getString(R.string.MapPreviewProviderTelegram));
            arrayList2.add(0);
        }
        if ((i2 & 2) != 0) {
            arrayList.add(LocaleController.getString(R.string.MapPreviewProviderGoogle));
            arrayList2.add(1);
        }
        if ((i2 & 4) != 0) {
            arrayList.add(LocaleController.getString(R.string.MapPreviewProviderYandex));
            arrayList2.add(3);
        }
        arrayList.add(LocaleController.getString(R.string.MapPreviewProviderNobody));
        arrayList2.add(2);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.MapPreviewProviderTitle));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        builder.setView(linearLayout);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            RadioColorCell radioColorCell = new RadioColorCell(context, resourcesProvider);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag(Integer.valueOf(i3));
            radioColorCell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            radioColorCell.setTextAndValue((CharSequence) arrayList.get(i3), SharedConfig.mapPreviewType == ((Integer) arrayList2.get(i3)).intValue());
            radioColorCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda25
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AlertsCreator.lambda$showSecretLocationAlert$18(arrayList2, runnable, builder, view);
                }
            });
        }
        if (!z) {
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        }
        AlertDialog show = builder.show();
        if (z) {
            show.setCanceledOnTouchOutside(false);
        }
        return show;
    }

    public static void showSendMediaAlert(int i, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider) {
        int i2;
        if (i == 0 || baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity(), resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.UnableForward));
        if (i == 1) {
            i2 = R.string.ErrorSendRestrictedStickers;
        } else if (i == 2) {
            i2 = R.string.ErrorSendRestrictedMedia;
        } else if (i == 3) {
            i2 = R.string.ErrorSendRestrictedPolls;
        } else if (i == 4) {
            i2 = R.string.ErrorSendRestrictedStickersAll;
        } else if (i == 5) {
            i2 = R.string.ErrorSendRestrictedMediaAll;
        } else if (i == 6) {
            i2 = R.string.ErrorSendRestrictedPollsAll;
        } else if (i == 7) {
            i2 = R.string.ErrorSendRestrictedPrivacyVoiceMessages;
        } else if (i == 8) {
            i2 = R.string.ErrorSendRestrictedPrivacyVideoMessages;
        } else if (i == 9) {
            i2 = R.string.ErrorSendRestrictedVideoAll;
        } else if (i == 10) {
            i2 = R.string.ErrorSendRestrictedPhotoAll;
        } else if (i == 11) {
            i2 = R.string.ErrorSendRestrictedVideo;
        } else if (i == 12) {
            i2 = R.string.ErrorSendRestrictedPhoto;
        } else if (i == 13) {
            i2 = R.string.ErrorSendRestrictedVoiceAll;
        } else if (i == 14) {
            i2 = R.string.ErrorSendRestrictedVoice;
        } else if (i == 15) {
            i2 = R.string.ErrorSendRestrictedRoundAll;
        } else if (i == 16) {
            i2 = R.string.ErrorSendRestrictedRound;
        } else if (i == 17) {
            i2 = R.string.ErrorSendRestrictedDocumentsAll;
        } else if (i == 18) {
            i2 = R.string.ErrorSendRestrictedDocuments;
        } else {
            if (i != 19) {
                if (i == 20) {
                    i2 = R.string.ErrorSendRestrictedMusic;
                }
                builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
                baseFragment.showDialog(builder.create(), true, null);
            }
            i2 = R.string.ErrorSendRestrictedMusicAll;
        }
        builder.setMessage(LocaleController.getString(i2));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        baseFragment.showDialog(builder.create(), true, null);
    }

    public static Dialog showSimpleAlert(BaseFragment baseFragment, String str) {
        return showSimpleAlert(baseFragment, null, str);
    }

    public static Dialog showSimpleAlert(BaseFragment baseFragment, String str, String str2) {
        return showSimpleAlert(baseFragment, str, str2, null);
    }

    public static Dialog showSimpleAlert(BaseFragment baseFragment, String str, String str2, Theme.ResourcesProvider resourcesProvider) {
        if (str2 == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        AlertDialog create = createSimpleAlert(baseFragment.getParentActivity(), str, str2, resourcesProvider).create();
        baseFragment.showDialog(create);
        return create;
    }

    public static Toast showSimpleToast(BaseFragment baseFragment, String str) {
        if (str == null) {
            return null;
        }
        Toast makeText = Toast.makeText((baseFragment == null || baseFragment.getParentActivity() == null) ? ApplicationLoader.applicationContext : baseFragment.getParentActivity(), str, 1);
        makeText.show();
        return makeText;
    }

    public static AlertDialog showUpdateAppAlert(final Context context, String str, boolean z) {
        if (context == null || str == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setMessage(str);
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        if (z) {
            builder.setNegativeButton(LocaleController.getString(R.string.UpdateApp), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.AlertsCreator$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    AlertsCreator.lambda$showUpdateAppAlert$7(context, dialogInterface, i);
                }
            });
        }
        return builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateDayPicker(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2, numberPicker2.getValue());
        calendar.set(1, numberPicker3.getValue());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(calendar.getActualMaximum(5));
    }
}
