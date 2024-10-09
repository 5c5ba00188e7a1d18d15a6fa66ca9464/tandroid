package org.telegram.ui;

import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.EditWidgetActivity;

/* loaded from: classes4.dex */
public class ContactsWidgetConfigActivity extends ExternalActionActivity {
    private int creatingAppWidgetId = 0;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$0(ArrayList arrayList) {
        Intent intent = new Intent();
        intent.putExtra("appWidgetId", this.creatingAppWidgetId);
        setResult(-1, intent);
        finish();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x007f  */
    @Override // org.telegram.ui.ExternalActionActivity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3, int i, int i2) {
        INavigationLayout iNavigationLayout;
        if (!checkPasscode(intent, z, z2, z3, i, i2)) {
            return false;
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.creatingAppWidgetId = extras.getInt("appWidgetId", 0);
        }
        if (this.creatingAppWidgetId != 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("onlySelect", true);
            bundle.putInt("dialogsType", 10);
            bundle.putBoolean("allowSwitchAccount", true);
            EditWidgetActivity editWidgetActivity = new EditWidgetActivity(1, this.creatingAppWidgetId);
            editWidgetActivity.setDelegate(new EditWidgetActivity.EditWidgetActivityDelegate() { // from class: org.telegram.ui.ContactsWidgetConfigActivity$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.EditWidgetActivity.EditWidgetActivityDelegate
                public final void didSelectDialogs(ArrayList arrayList) {
                    ContactsWidgetConfigActivity.this.lambda$handleIntent$0(arrayList);
                }
            });
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                    iNavigationLayout = this.layersActionBarLayout;
                    iNavigationLayout.addFragmentToStack(editWidgetActivity);
                }
                if (!AndroidUtilities.isTablet()) {
                    this.backgroundTablet.setVisibility(8);
                }
                this.actionBarLayout.showLastFragment();
                if (AndroidUtilities.isTablet()) {
                    this.layersActionBarLayout.showLastFragment();
                }
                intent.setAction(null);
            } else {
                if (this.actionBarLayout.getFragmentStack().isEmpty()) {
                    iNavigationLayout = this.actionBarLayout;
                    iNavigationLayout.addFragmentToStack(editWidgetActivity);
                }
                if (!AndroidUtilities.isTablet()) {
                }
                this.actionBarLayout.showLastFragment();
                if (AndroidUtilities.isTablet()) {
                }
                intent.setAction(null);
            }
        } else {
            finish();
        }
        return true;
    }
}
