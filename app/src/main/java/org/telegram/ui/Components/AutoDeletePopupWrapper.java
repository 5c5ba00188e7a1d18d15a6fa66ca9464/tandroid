package org.telegram.ui.Components;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class AutoDeletePopupWrapper {
    View backItem;
    Callback callback;
    private final ActionBarMenuSubItem disableItem;
    long lastDismissTime;
    public ActionBarPopupWindow.ActionBarPopupWindowLayout windowLayout;

    /* loaded from: classes3.dex */
    public interface Callback {
        void dismiss();

        void setAutoDeleteHistory(int i, int i2);
    }

    public AutoDeletePopupWrapper(Context context, PopupSwipeBackLayout popupSwipeBackLayout, Callback callback, boolean z, Theme.ResourcesProvider resourcesProvider) {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(context, z ? 2131166090 : 0, resourcesProvider);
        this.windowLayout = actionBarPopupWindowLayout;
        actionBarPopupWindowLayout.setFitItems(true);
        this.callback = callback;
        if (popupSwipeBackLayout != null) {
            ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(this.windowLayout, 2131165639, LocaleController.getString("Back", 2131624647), false, resourcesProvider);
            this.backItem = addItem;
            addItem.setOnClickListener(new AutoDeletePopupWrapper$$ExternalSyntheticLambda5(popupSwipeBackLayout));
        }
        ActionBarMenuItem.addItem(this.windowLayout, 2131165644, LocaleController.getString("AutoDelete1Day", 2131624548), false, resourcesProvider).setOnClickListener(new AutoDeletePopupWrapper$$ExternalSyntheticLambda4(this, callback));
        ActionBarMenuItem.addItem(this.windowLayout, 2131165647, LocaleController.getString("AutoDelete7Days", 2131624552), false, resourcesProvider).setOnClickListener(new AutoDeletePopupWrapper$$ExternalSyntheticLambda2(this, callback));
        ActionBarMenuItem.addItem(this.windowLayout, 2131165646, LocaleController.getString("AutoDelete1Month", 2131624550), false, resourcesProvider).setOnClickListener(new AutoDeletePopupWrapper$$ExternalSyntheticLambda1(this, callback));
        ActionBarMenuItem.addItem(this.windowLayout, 2131165700, LocaleController.getString("AutoDeleteCustom", 2131624560), false, resourcesProvider).setOnClickListener(new AutoDeletePopupWrapper$$ExternalSyntheticLambda0(this, context, resourcesProvider, callback));
        ActionBarMenuSubItem addItem2 = ActionBarMenuItem.addItem(this.windowLayout, 2131165707, LocaleController.getString("AutoDeleteDisable", 2131624561), false, resourcesProvider);
        this.disableItem = addItem2;
        addItem2.setOnClickListener(new AutoDeletePopupWrapper$$ExternalSyntheticLambda3(this, callback));
        addItem2.setColors(Theme.getColor("dialogTextRed2"), Theme.getColor("dialogTextRed2"));
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor("actionBarDefaultSubmenuSeparator", resourcesProvider));
        frameLayout.setTag(2131230821, 1);
        this.windowLayout.addView((View) frameLayout, LayoutHelper.createLinear(-1, 8));
        TextView textView = new TextView(context);
        textView.setTag(2131230821, 1);
        textView.setPadding(AndroidUtilities.dp(13.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(8.0f));
        textView.setTextSize(1, 13.0f);
        textView.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        textView.setText(LocaleController.getString("AutoDeletePopupDescription", 2131624568));
        this.windowLayout.addView((View) textView, LayoutHelper.createLinear(-1, -2));
    }

    public /* synthetic */ void lambda$new$1(Callback callback, View view) {
        dismiss();
        callback.setAutoDeleteHistory(86400, 70);
    }

    public /* synthetic */ void lambda$new$2(Callback callback, View view) {
        dismiss();
        callback.setAutoDeleteHistory(604800, 70);
    }

    public /* synthetic */ void lambda$new$3(Callback callback, View view) {
        dismiss();
        callback.setAutoDeleteHistory(2678400, 70);
    }

    public /* synthetic */ void lambda$new$5(Context context, Theme.ResourcesProvider resourcesProvider, Callback callback, View view) {
        dismiss();
        AlertsCreator.createAutoDeleteDatePickerDialog(context, resourcesProvider, new AutoDeletePopupWrapper$$ExternalSyntheticLambda7(callback));
    }

    public static /* synthetic */ void lambda$new$4(Callback callback, boolean z, int i) {
        callback.setAutoDeleteHistory(i * 60, i == 0 ? 71 : 70);
    }

    public /* synthetic */ void lambda$new$6(Callback callback, View view) {
        dismiss();
        callback.setAutoDeleteHistory(0, 71);
    }

    private void dismiss() {
        this.callback.dismiss();
        this.lastDismissTime = System.currentTimeMillis();
    }

    /* renamed from: updateItems */
    public void lambda$updateItems$7(int i) {
        if (System.currentTimeMillis() - this.lastDismissTime < 200) {
            AndroidUtilities.runOnUIThread(new AutoDeletePopupWrapper$$ExternalSyntheticLambda6(this, i));
        } else if (i == 0) {
            this.disableItem.setVisibility(8);
        } else {
            this.disableItem.setVisibility(0);
        }
    }
}
