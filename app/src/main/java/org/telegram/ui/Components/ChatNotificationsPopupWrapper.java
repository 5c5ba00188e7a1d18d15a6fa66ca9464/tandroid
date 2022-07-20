package org.telegram.ui.Components;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class ChatNotificationsPopupWrapper {
    View backItem;
    Callback callback;
    int currentAccount;
    long lastDismissTime;
    ActionBarMenuSubItem muteForLastSelected;
    private int muteForLastSelected1Time;
    ActionBarMenuSubItem muteForLastSelected2;
    private int muteForLastSelected2Time;
    ActionBarMenuSubItem muteUnmuteButton;
    ActionBarPopupWindow popupWindow;
    ActionBarMenuSubItem soundToggle;
    public ActionBarPopupWindow.ActionBarPopupWindowLayout windowLayout;

    /* loaded from: classes3.dex */
    public interface Callback {

        /* renamed from: org.telegram.ui.Components.ChatNotificationsPopupWrapper$Callback$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static void $default$dismiss(Callback callback) {
            }
        }

        void dismiss();

        void muteFor(int i);

        void showCustomize();

        void toggleMute();

        void toggleSound();
    }

    public ChatNotificationsPopupWrapper(Context context, int i, PopupSwipeBackLayout popupSwipeBackLayout, boolean z, boolean z2, Callback callback, Theme.ResourcesProvider resourcesProvider) {
        this.currentAccount = i;
        this.callback = callback;
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(context, z ? 2131166087 : 0, resourcesProvider);
        this.windowLayout = actionBarPopupWindowLayout;
        actionBarPopupWindowLayout.setFitItems(true);
        if (popupSwipeBackLayout != null) {
            ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(this.windowLayout, 2131165639, LocaleController.getString("Back", 2131624647), false, resourcesProvider);
            this.backItem = addItem;
            addItem.setOnClickListener(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda6(popupSwipeBackLayout));
        }
        ActionBarMenuSubItem addItem2 = ActionBarMenuItem.addItem(this.windowLayout, 2131165964, LocaleController.getString("SoundOn", 2131628455), false, resourcesProvider);
        this.soundToggle = addItem2;
        addItem2.setOnClickListener(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda4(this, callback));
        ActionBarMenuSubItem addItem3 = ActionBarMenuItem.addItem(this.windowLayout, 2131165818, LocaleController.getString("MuteFor1h", 2131626800), false, resourcesProvider);
        this.muteForLastSelected = addItem3;
        addItem3.setOnClickListener(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda5(this, callback));
        ActionBarMenuSubItem addItem4 = ActionBarMenuItem.addItem(this.windowLayout, 2131165818, LocaleController.getString("MuteFor1h", 2131626800), false, resourcesProvider);
        this.muteForLastSelected2 = addItem4;
        addItem4.setOnClickListener(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda3(this, callback));
        ActionBarMenuItem.addItem(this.windowLayout, 2131165820, LocaleController.getString("MuteForPopup", 2131626803), false, resourcesProvider).setOnClickListener(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda0(this, context, resourcesProvider, i, callback));
        ActionBarMenuItem.addItem(this.windowLayout, 2131165700, LocaleController.getString("NotificationsCustomize", 2131627075), false, resourcesProvider).setOnClickListener(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda2(this, callback));
        ActionBarMenuSubItem addItem5 = ActionBarMenuItem.addItem(this.windowLayout, 0, "", false, resourcesProvider);
        this.muteUnmuteButton = addItem5;
        addItem5.setOnClickListener(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda1(this, callback));
    }

    public /* synthetic */ void lambda$new$1(Callback callback, View view) {
        dismiss();
        callback.toggleSound();
    }

    public /* synthetic */ void lambda$new$2(Callback callback, View view) {
        dismiss();
        callback.muteFor(this.muteForLastSelected1Time);
    }

    public /* synthetic */ void lambda$new$3(Callback callback, View view) {
        dismiss();
        callback.muteFor(this.muteForLastSelected2Time);
    }

    public /* synthetic */ void lambda$new$6(Context context, Theme.ResourcesProvider resourcesProvider, int i, Callback callback, View view) {
        dismiss();
        AlertsCreator.createMuteForPickerDialog(context, resourcesProvider, new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda10(i, callback));
    }

    public static /* synthetic */ void lambda$new$5(int i, Callback callback, boolean z, int i2) {
        AndroidUtilities.runOnUIThread(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda7(i2, i, callback), 16L);
    }

    public static /* synthetic */ void lambda$new$4(int i, int i2, Callback callback) {
        if (i != 0) {
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(i2);
            notificationsSettings.edit().putInt("last_selected_mute_until_time", i).putInt("last_selected_mute_until_time2", notificationsSettings.getInt("last_selected_mute_until_time", 0)).apply();
        }
        callback.muteFor(i);
    }

    public /* synthetic */ void lambda$new$7(Callback callback, View view) {
        dismiss();
        callback.showCustomize();
    }

    public /* synthetic */ void lambda$new$9(Callback callback, View view) {
        dismiss();
        AndroidUtilities.runOnUIThread(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda8(callback));
    }

    private void dismiss() {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
            this.popupWindow.dismiss();
        }
        this.callback.dismiss();
        this.lastDismissTime = System.currentTimeMillis();
    }

    /* renamed from: update */
    public void lambda$update$10(long j) {
        int i;
        int i2;
        int i3;
        if (System.currentTimeMillis() - this.lastDismissTime < 200) {
            AndroidUtilities.runOnUIThread(new ChatNotificationsPopupWrapper$$ExternalSyntheticLambda9(this, j));
            return;
        }
        boolean isDialogMuted = MessagesController.getInstance(this.currentAccount).isDialogMuted(j);
        if (isDialogMuted) {
            this.muteUnmuteButton.setTextAndIcon(LocaleController.getString("UnmuteNotifications", 2131628811), 2131165969);
            i = Theme.getColor("wallet_greenText");
            this.soundToggle.setVisibility(8);
        } else {
            this.muteUnmuteButton.setTextAndIcon(LocaleController.getString("MuteNotifications", 2131626805), 2131165817);
            int color = Theme.getColor("dialogTextRed");
            this.soundToggle.setVisibility(0);
            if (MessagesController.getInstance(this.currentAccount).isDialogNotificationsSoundEnabled(j)) {
                this.soundToggle.setTextAndIcon(LocaleController.getString("SoundOff", 2131628453), 2131165963);
            } else {
                this.soundToggle.setTextAndIcon(LocaleController.getString("SoundOn", 2131628455), 2131165964);
            }
            i = color;
        }
        if (isDialogMuted) {
            i2 = 0;
            i3 = 0;
        } else {
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            i3 = notificationsSettings.getInt("last_selected_mute_until_time", 0);
            i2 = notificationsSettings.getInt("last_selected_mute_until_time2", 0);
        }
        if (i3 != 0) {
            this.muteForLastSelected1Time = i3;
            this.muteForLastSelected.setVisibility(0);
            this.muteForLastSelected.getImageView().setImageDrawable(TimerDrawable.getTtlIcon(i3));
            this.muteForLastSelected.setText(formatMuteForTime(i3));
        } else {
            this.muteForLastSelected.setVisibility(8);
        }
        if (i2 != 0) {
            this.muteForLastSelected2Time = i2;
            this.muteForLastSelected2.setVisibility(0);
            this.muteForLastSelected2.getImageView().setImageDrawable(TimerDrawable.getTtlIcon(i2));
            this.muteForLastSelected2.setText(formatMuteForTime(i2));
        } else {
            this.muteForLastSelected2.setVisibility(8);
        }
        this.muteUnmuteButton.setColors(i, i);
    }

    private String formatMuteForTime(int i) {
        StringBuilder sb = new StringBuilder();
        int i2 = i / 86400;
        int i3 = (i - (86400 * i2)) / 3600;
        if (i2 != 0) {
            sb.append(i2);
            sb.append(LocaleController.getString("SecretChatTimerDays", 2131628209));
        }
        if (i3 != 0) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(i3);
            sb.append(LocaleController.getString("SecretChatTimerHours", 2131628210));
        }
        return LocaleController.formatString("MuteForButton", 2131626802, sb.toString());
    }

    public void showAsOptions(BaseFragment baseFragment, View view, float f, float f2) {
        if (baseFragment == null || baseFragment.getFragmentView() == null) {
            return;
        }
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(this.windowLayout, -2, -2);
        this.popupWindow = actionBarPopupWindow;
        actionBarPopupWindow.setPauseNotifications(true);
        this.popupWindow.setDismissAnimationDuration(220);
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setClippingEnabled(true);
        this.popupWindow.setAnimationStyle(2131689481);
        this.popupWindow.setFocusable(true);
        this.windowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        this.popupWindow.setInputMethodMode(2);
        this.popupWindow.getContentView().setFocusableInTouchMode(true);
        while (view != baseFragment.getFragmentView()) {
            f += view.getX();
            f2 += view.getY();
            view = (View) view.getParent();
        }
        this.popupWindow.showAtLocation(baseFragment.getFragmentView(), 0, (int) (f - (this.windowLayout.getMeasuredWidth() / 2.0f)), (int) (f2 - (this.windowLayout.getMeasuredHeight() / 2.0f)));
        this.popupWindow.dimBehind();
    }
}
