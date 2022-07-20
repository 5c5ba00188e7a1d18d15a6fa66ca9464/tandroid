package org.telegram.ui;

import android.content.Context;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.Components.PopupSwipeBackLayout;
/* loaded from: classes3.dex */
public class ChooseSpeedLayout {
    ActionBarMenuSubItem[] speedItems = new ActionBarMenuSubItem[5];
    ActionBarPopupWindow.ActionBarPopupWindowLayout speedSwipeBackLayout;

    /* loaded from: classes3.dex */
    public interface Callback {
        void onSpeedSelected(float f);
    }

    public ChooseSpeedLayout(Context context, PopupSwipeBackLayout popupSwipeBackLayout, Callback callback) {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(context, 0, null);
        this.speedSwipeBackLayout = actionBarPopupWindowLayout;
        actionBarPopupWindowLayout.setFitItems(true);
        ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(this.speedSwipeBackLayout, 2131165639, LocaleController.getString("Back", 2131624647), false, null);
        addItem.setOnClickListener(new ChooseSpeedLayout$$ExternalSyntheticLambda5(popupSwipeBackLayout));
        addItem.setColors(-328966, -328966);
        addItem.setSelectorColor(268435455);
        ActionBarMenuSubItem addItem2 = ActionBarMenuItem.addItem(this.speedSwipeBackLayout, 2131165944, LocaleController.getString("SpeedVerySlow", 2131628464), false, null);
        addItem2.setColors(-328966, -328966);
        addItem2.setOnClickListener(new ChooseSpeedLayout$$ExternalSyntheticLambda2(callback));
        addItem2.setSelectorColor(268435455);
        this.speedItems[0] = addItem2;
        ActionBarMenuSubItem addItem3 = ActionBarMenuItem.addItem(this.speedSwipeBackLayout, 2131165945, LocaleController.getString("SpeedSlow", 2131628462), false, null);
        addItem3.setColors(-328966, -328966);
        addItem3.setOnClickListener(new ChooseSpeedLayout$$ExternalSyntheticLambda0(callback));
        addItem3.setSelectorColor(268435455);
        this.speedItems[1] = addItem3;
        ActionBarMenuSubItem addItem4 = ActionBarMenuItem.addItem(this.speedSwipeBackLayout, 2131165946, LocaleController.getString("SpeedNormal", 2131628461), false, null);
        addItem4.setColors(-328966, -328966);
        addItem4.setOnClickListener(new ChooseSpeedLayout$$ExternalSyntheticLambda3(callback));
        addItem4.setSelectorColor(268435455);
        this.speedItems[2] = addItem4;
        ActionBarMenuSubItem addItem5 = ActionBarMenuItem.addItem(this.speedSwipeBackLayout, 2131165947, LocaleController.getString("SpeedFast", 2131628460), false, null);
        addItem5.setColors(-328966, -328966);
        addItem5.setOnClickListener(new ChooseSpeedLayout$$ExternalSyntheticLambda4(callback));
        addItem5.setSelectorColor(268435455);
        this.speedItems[3] = addItem5;
        ActionBarMenuSubItem addItem6 = ActionBarMenuItem.addItem(this.speedSwipeBackLayout, 2131165948, LocaleController.getString("SpeedVeryFast", 2131628463), false, null);
        addItem6.setColors(-328966, -328966);
        addItem6.setOnClickListener(new ChooseSpeedLayout$$ExternalSyntheticLambda1(callback));
        addItem6.setSelectorColor(268435455);
        this.speedItems[4] = addItem6;
    }

    public void update(float f) {
        for (int i = 0; i < this.speedItems.length; i++) {
            if ((i == 0 && Math.abs(f - 0.25f) < 0.001f) || ((i == 1 && Math.abs(f - 0.5f) < 0.001f) || ((i == 2 && Math.abs(f - 1.0f) < 0.001f) || ((i == 3 && Math.abs(f - 1.5f) < 0.001f) || (i == 4 && Math.abs(f - 2.0f) < 0.001f))))) {
                this.speedItems[i].setColors(-9718023, -9718023);
            } else {
                this.speedItems[i].setColors(-328966, -328966);
            }
        }
    }
}
