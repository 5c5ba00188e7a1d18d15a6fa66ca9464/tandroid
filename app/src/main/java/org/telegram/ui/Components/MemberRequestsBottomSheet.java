package org.telegram.ui.Components;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Delegates.MemberRequestsDelegate;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public class MemberRequestsBottomSheet extends UsersAlertBase {
    private final FlickerLoadingView currentLoadingView;
    private final MemberRequestsDelegate delegate;
    private boolean enterEventSent;
    private final StickerEmptyView membersEmptyView;
    private final StickerEmptyView membersSearchEmptyView;
    private final int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private float yOffset;

    public MemberRequestsBottomSheet(BaseFragment baseFragment, long j) {
        super(baseFragment.getParentActivity(), false, baseFragment.getCurrentAccount(), baseFragment.getResourceProvider());
        this.needSnapToTop = false;
        this.isEmptyViewVisible = false;
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(baseFragment, this.container, j, false);
        this.delegate = anonymousClass1;
        anonymousClass1.setShowLastItemDivider(false);
        setDimBehindAlpha(75);
        this.searchView.searchEditText.setHint(LocaleController.getString("SearchMemberRequests", 2131628117));
        MemberRequestsDelegate.Adapter adapter = anonymousClass1.getAdapter();
        this.listViewAdapter = adapter;
        this.searchListViewAdapter = adapter;
        this.listView.setAdapter(adapter);
        anonymousClass1.setRecyclerView(this.listView);
        int indexOfChild = ((ViewGroup) this.listView.getParent()).indexOfChild(this.listView);
        FlickerLoadingView loadingView = anonymousClass1.getLoadingView();
        this.currentLoadingView = loadingView;
        this.containerView.addView(loadingView, indexOfChild, LayoutHelper.createFrame(-1, -1.0f));
        StickerEmptyView emptyView = anonymousClass1.getEmptyView();
        this.membersEmptyView = emptyView;
        this.containerView.addView(emptyView, indexOfChild, LayoutHelper.createFrame(-1, -1.0f));
        StickerEmptyView searchEmptyView = anonymousClass1.getSearchEmptyView();
        this.membersSearchEmptyView = searchEmptyView;
        this.containerView.addView(searchEmptyView, indexOfChild, LayoutHelper.createFrame(-1, -1.0f));
        anonymousClass1.loadMembers();
    }

    /* renamed from: org.telegram.ui.Components.MemberRequestsBottomSheet$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends MemberRequestsDelegate {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(BaseFragment baseFragment, FrameLayout frameLayout, long j, boolean z) {
            super(baseFragment, frameLayout, j, z);
            MemberRequestsBottomSheet.this = r7;
        }

        @Override // org.telegram.ui.Delegates.MemberRequestsDelegate
        public void onImportersChanged(String str, boolean z, boolean z2) {
            if (!hasAllImporters()) {
                if (MemberRequestsBottomSheet.this.membersEmptyView.getVisibility() != 4) {
                    MemberRequestsBottomSheet.this.membersEmptyView.setVisibility(4);
                }
                MemberRequestsBottomSheet.this.dismiss();
            } else if (z2) {
                MemberRequestsBottomSheet.this.searchView.searchEditText.setText("");
            } else {
                super.onImportersChanged(str, z, z2);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        if (this.delegate.isNeedRestoreList && this.scrollOffsetY == 0) {
            this.scrollOffsetY = AndroidUtilities.dp(8.0f);
        }
        super.show();
        this.delegate.isNeedRestoreList = false;
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        if (this.delegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public boolean isNeedRestoreDialog() {
        return this.delegate.isNeedRestoreList;
    }

    @Override // org.telegram.ui.Components.UsersAlertBase
    protected boolean isAllowSelectChildAtPosition(float f, float f2) {
        return f2 >= ((float) (this.scrollOffsetY + this.frameLayout.getMeasuredHeight()));
    }

    @Override // org.telegram.ui.Components.UsersAlertBase
    public void setTranslationY(int i) {
        super.setTranslationY(i);
        this.currentLoadingView.setTranslationY(this.frameLayout.getMeasuredHeight() + i);
        float f = i;
        this.membersEmptyView.setTranslationY(f);
        this.membersSearchEmptyView.setTranslationY(f);
    }

    @Override // org.telegram.ui.Components.UsersAlertBase
    public void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            int paddingTop = this.listView.getVisibility() == 0 ? this.listView.getPaddingTop() - AndroidUtilities.dp(8.0f) : 0;
            if (this.scrollOffsetY == paddingTop) {
                return;
            }
            this.scrollOffsetY = paddingTop;
            setTranslationY(paddingTop);
            return;
        }
        super.updateLayout();
    }

    @Override // org.telegram.ui.Components.UsersAlertBase
    public void search(String str) {
        super.search(str);
        this.delegate.setQuery(str);
    }

    @Override // org.telegram.ui.Components.UsersAlertBase
    protected void onSearchViewTouched(MotionEvent motionEvent, EditTextBoldCursor editTextBoldCursor) {
        if (motionEvent.getAction() == 0) {
            this.yOffset = this.scrollOffsetY;
            this.delegate.setAdapterItemsEnabled(false);
        } else if (motionEvent.getAction() == 1 && Math.abs(this.scrollOffsetY - this.yOffset) < this.touchSlop && !this.enterEventSent) {
            Activity findActivity = AndroidUtilities.findActivity(getContext());
            BaseFragment baseFragment = null;
            if (findActivity instanceof LaunchActivity) {
                LaunchActivity launchActivity = (LaunchActivity) findActivity;
                baseFragment = launchActivity.getActionBarLayout().fragmentsStack.get(launchActivity.getActionBarLayout().fragmentsStack.size() - 1);
            }
            if (baseFragment instanceof ChatActivity) {
                boolean needEnterText = ((ChatActivity) baseFragment).needEnterText();
                this.enterEventSent = true;
                AndroidUtilities.runOnUIThread(new MemberRequestsBottomSheet$$ExternalSyntheticLambda2(this, editTextBoldCursor), needEnterText ? 200L : 0L);
            } else {
                this.enterEventSent = true;
                setFocusable(true);
                editTextBoldCursor.requestFocus();
                AndroidUtilities.runOnUIThread(new MemberRequestsBottomSheet$$ExternalSyntheticLambda0(editTextBoldCursor));
            }
        }
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.delegate.setAdapterItemsEnabled(true);
        }
    }

    public /* synthetic */ void lambda$onSearchViewTouched$1(EditTextBoldCursor editTextBoldCursor) {
        setFocusable(true);
        editTextBoldCursor.requestFocus();
        AndroidUtilities.runOnUIThread(new MemberRequestsBottomSheet$$ExternalSyntheticLambda1(editTextBoldCursor));
    }
}
