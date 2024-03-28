package org.telegram.ui.Components;

import android.content.ComponentName;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.FloatingToolbar;
/* loaded from: classes3.dex */
public class MenuToItemOptions implements Menu {
    private final ItemOptions itemOptions;
    private final Utilities.Callback<Integer> onMenuClicked;
    private final Runnable premiumLock;

    @Override // android.view.Menu
    public MenuItem add(int i) {
        return null;
    }

    @Override // android.view.Menu
    public MenuItem add(CharSequence charSequence) {
        return null;
    }

    @Override // android.view.Menu
    public int addIntentOptions(int i, int i2, int i3, ComponentName componentName, Intent[] intentArr, Intent intent, int i4, MenuItem[] menuItemArr) {
        return 0;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i) {
        return null;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i, int i2, int i3, int i4) {
        return null;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i, int i2, int i3, CharSequence charSequence) {
        return null;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(CharSequence charSequence) {
        return null;
    }

    @Override // android.view.Menu
    public void clear() {
    }

    @Override // android.view.Menu
    public void close() {
    }

    @Override // android.view.Menu
    public MenuItem findItem(int i) {
        return null;
    }

    @Override // android.view.Menu
    public MenuItem getItem(int i) {
        return null;
    }

    @Override // android.view.Menu
    public boolean hasVisibleItems() {
        return false;
    }

    @Override // android.view.Menu
    public boolean isShortcutKey(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override // android.view.Menu
    public boolean performIdentifierAction(int i, int i2) {
        return false;
    }

    @Override // android.view.Menu
    public boolean performShortcut(int i, KeyEvent keyEvent, int i2) {
        return false;
    }

    @Override // android.view.Menu
    public void removeGroup(int i) {
    }

    @Override // android.view.Menu
    public void removeItem(int i) {
    }

    @Override // android.view.Menu
    public void setGroupCheckable(int i, boolean z, boolean z2) {
    }

    @Override // android.view.Menu
    public void setGroupEnabled(int i, boolean z) {
    }

    @Override // android.view.Menu
    public void setGroupVisible(int i, boolean z) {
    }

    @Override // android.view.Menu
    public void setQwertyMode(boolean z) {
    }

    @Override // android.view.Menu
    public int size() {
        return 0;
    }

    public MenuToItemOptions(ItemOptions itemOptions, Utilities.Callback<Integer> callback, Runnable runnable) {
        this.itemOptions = itemOptions;
        this.onMenuClicked = callback;
        this.premiumLock = runnable;
    }

    @Override // android.view.Menu
    public MenuItem add(int i, final int i2, int i3, CharSequence charSequence) {
        if (this.premiumLock != null && FloatingToolbar.premiumOptions.contains(Integer.valueOf(i2)) && MessagesController.getInstance(UserConfig.selectedAccount).premiumFeaturesBlocked()) {
            return null;
        }
        this.itemOptions.add(charSequence, new Runnable() { // from class: org.telegram.ui.Components.MenuToItemOptions$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MenuToItemOptions.this.lambda$add$0(i2);
            }
        });
        if (this.premiumLock != null && FloatingToolbar.premiumOptions.contains(Integer.valueOf(i2))) {
            this.itemOptions.putPremiumLock(this.premiumLock);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$add$0(int i) {
        this.onMenuClicked.run(Integer.valueOf(i));
    }

    @Override // android.view.Menu
    public MenuItem add(int i, int i2, int i3, int i4) {
        return add(i, i2, i3, LocaleController.getString(i4));
    }
}
