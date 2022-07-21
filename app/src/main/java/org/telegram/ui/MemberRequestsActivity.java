package org.telegram.ui;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Delegates.MemberRequestsDelegate;
/* loaded from: classes3.dex */
public class MemberRequestsActivity extends BaseFragment {
    private final MemberRequestsDelegate delegate;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.MemberRequestsActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends MemberRequestsDelegate {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(BaseFragment baseFragment, FrameLayout frameLayout, long j, boolean z) {
            super(baseFragment, frameLayout, j, z);
            MemberRequestsActivity.this = r7;
        }

        @Override // org.telegram.ui.Delegates.MemberRequestsDelegate
        public void onImportersChanged(String str, boolean z, boolean z2) {
            if (z2) {
                ((BaseFragment) MemberRequestsActivity.this).actionBar.setSearchFieldText("");
            } else {
                super.onImportersChanged(str, z, z2);
            }
        }
    }

    public MemberRequestsActivity(long j) {
        this.delegate = new AnonymousClass1(this, getLayoutContainer(), j, true);
    }

    /* renamed from: org.telegram.ui.MemberRequestsActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass2() {
            MemberRequestsActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                MemberRequestsActivity.this.finishFragment();
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        String str;
        int i;
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass2());
        this.actionBar.setBackButtonImage(2131165449);
        ActionBar actionBar = this.actionBar;
        if (this.delegate.isChannel) {
            i = 2131628548;
            str = "SubscribeRequests";
        } else {
            i = 2131626639;
            str = "MemberRequests";
        }
        actionBar.setTitle(LocaleController.getString(str, i));
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass3());
        actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString("Search", 2131628155));
        actionBarMenuItemSearchListener.setVisibility(8);
        FrameLayout rootLayout = this.delegate.getRootLayout();
        this.delegate.loadMembers();
        this.fragmentView = rootLayout;
        return rootLayout;
    }

    /* renamed from: org.telegram.ui.MemberRequestsActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass3() {
            MemberRequestsActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
            super.onSearchExpand();
            MemberRequestsActivity.this.delegate.setSearchExpanded(true);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            super.onSearchCollapse();
            MemberRequestsActivity.this.delegate.setSearchExpanded(false);
            MemberRequestsActivity.this.delegate.setQuery(null);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            super.onTextChanged(editText);
            MemberRequestsActivity.this.delegate.setQuery(editText.getText().toString());
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        return this.delegate.onBackPressed();
    }
}
