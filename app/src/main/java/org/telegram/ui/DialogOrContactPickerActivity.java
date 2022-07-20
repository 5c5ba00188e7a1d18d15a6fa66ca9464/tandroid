package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrollSlidingTextTabStrip;
/* loaded from: classes3.dex */
public class DialogOrContactPickerActivity extends BaseFragment {
    private static final Interpolator interpolator = DialogOrContactPickerActivity$$ExternalSyntheticLambda1.INSTANCE;
    private boolean animatingForward;
    private boolean backAnimation;
    private ContactsActivity contactsActivity;
    private DialogsActivity dialogsActivity;
    private int maximumVelocity;
    private ScrollSlidingTextTabStrip scrollSlidingTextTabStrip;
    private ActionBarMenuItem searchItem;
    private AnimatorSet tabsAnimation;
    private boolean tabsAnimationInProgress;
    private Paint backgroundPaint = new Paint();
    private ViewPage[] viewPages = new ViewPage[2];
    private boolean swipeBackEnabled = true;

    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    /* loaded from: classes3.dex */
    public static class ViewPage extends FrameLayout {
        private ActionBar actionBar;
        private FrameLayout fragmentView;
        private RecyclerListView listView;
        private RecyclerListView listView2;
        private BaseFragment parentFragment;
        private int selectedType;

        public ViewPage(Context context) {
            super(context);
        }
    }

    public DialogOrContactPickerActivity() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("onlySelect", true);
        bundle.putBoolean("checkCanWrite", false);
        bundle.putBoolean("resetDelegate", false);
        bundle.putInt("dialogsType", 9);
        DialogsActivity dialogsActivity = new DialogsActivity(bundle);
        this.dialogsActivity = dialogsActivity;
        dialogsActivity.setDelegate(new DialogOrContactPickerActivity$$ExternalSyntheticLambda3(this));
        this.dialogsActivity.onFragmentCreate();
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("onlyUsers", true);
        bundle2.putBoolean("destroyAfterSelect", true);
        bundle2.putBoolean("returnAsResult", true);
        bundle2.putBoolean("disableSections", true);
        bundle2.putBoolean("needFinishFragment", false);
        bundle2.putBoolean("resetDelegate", false);
        bundle2.putBoolean("allowSelf", false);
        ContactsActivity contactsActivity = new ContactsActivity(bundle2);
        this.contactsActivity = contactsActivity;
        contactsActivity.setDelegate(new DialogOrContactPickerActivity$$ExternalSyntheticLambda2(this));
        this.contactsActivity.onFragmentCreate();
    }

    public /* synthetic */ void lambda$new$1(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        if (arrayList.isEmpty()) {
            return;
        }
        long longValue = ((Long) arrayList.get(0)).longValue();
        if (!DialogObject.isUserDialog(longValue)) {
            return;
        }
        showBlockAlert(getMessagesController().getUser(Long.valueOf(longValue)));
    }

    public /* synthetic */ void lambda$new$2(TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity) {
        showBlockAlert(tLRPC$User);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setTitle(LocaleController.getString("BlockUserMultiTitle", 2131624695));
        boolean z = false;
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setExtraHeight(AndroidUtilities.dp(44.0f));
        this.actionBar.setAllowOverlayTitle(false);
        this.actionBar.setAddToContainer(false);
        this.actionBar.setClipContent(true);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.hasOwnBackground = true;
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass2());
        this.searchItem = actionBarMenuItemSearchListener;
        actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString("Search", 2131628154));
        ScrollSlidingTextTabStrip scrollSlidingTextTabStrip = new ScrollSlidingTextTabStrip(context);
        this.scrollSlidingTextTabStrip = scrollSlidingTextTabStrip;
        scrollSlidingTextTabStrip.setUseSameWidth(true);
        this.actionBar.addView(this.scrollSlidingTextTabStrip, LayoutHelper.createFrame(-1, 44, 83));
        this.scrollSlidingTextTabStrip.setDelegate(new AnonymousClass3());
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context);
        this.fragmentView = anonymousClass4;
        anonymousClass4.setWillNotDraw(false);
        this.dialogsActivity.setParentFragment(this);
        this.contactsActivity.setParentFragment(this);
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                break;
            }
            viewPageArr[i] = new AnonymousClass5(context);
            anonymousClass4.addView(this.viewPages[i], LayoutHelper.createFrame(-1, -1.0f));
            if (i == 0) {
                this.viewPages[i].parentFragment = this.dialogsActivity;
                this.viewPages[i].listView = this.dialogsActivity.getListView();
                this.viewPages[i].listView2 = this.dialogsActivity.getSearchListView();
            } else if (i == 1) {
                this.viewPages[i].parentFragment = this.contactsActivity;
                this.viewPages[i].listView = this.contactsActivity.getListView();
                this.viewPages[i].setVisibility(8);
            }
            this.viewPages[i].listView.setScrollingTouchSlop(1);
            ViewPage[] viewPageArr2 = this.viewPages;
            viewPageArr2[i].fragmentView = (FrameLayout) viewPageArr2[i].parentFragment.getFragmentView();
            ViewPage[] viewPageArr3 = this.viewPages;
            viewPageArr3[i].actionBar = viewPageArr3[i].parentFragment.getActionBar();
            ViewPage[] viewPageArr4 = this.viewPages;
            viewPageArr4[i].addView(viewPageArr4[i].fragmentView, LayoutHelper.createFrame(-1, -1.0f));
            ViewPage[] viewPageArr5 = this.viewPages;
            viewPageArr5[i].addView(viewPageArr5[i].actionBar, LayoutHelper.createFrame(-1, -2.0f));
            this.viewPages[i].actionBar.setVisibility(8);
            int i2 = 0;
            while (i2 < 2) {
                ViewPage[] viewPageArr6 = this.viewPages;
                RecyclerListView recyclerListView = i2 == 0 ? viewPageArr6[i].listView : viewPageArr6[i].listView2;
                if (recyclerListView != null) {
                    recyclerListView.setClipToPadding(false);
                    recyclerListView.setOnScrollListener(new AnonymousClass6(recyclerListView.getOnScrollListener()));
                }
                i2++;
            }
            i++;
        }
        anonymousClass4.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        updateTabs();
        switchToCurrentSelectedMode(false);
        if (this.scrollSlidingTextTabStrip.getCurrentTabId() == this.scrollSlidingTextTabStrip.getFirstTabId()) {
            z = true;
        }
        this.swipeBackEnabled = z;
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.DialogOrContactPickerActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            DialogOrContactPickerActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                DialogOrContactPickerActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.DialogOrContactPickerActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass2() {
            DialogOrContactPickerActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
            DialogOrContactPickerActivity.this.dialogsActivity.getActionBar().openSearchField("", false);
            DialogOrContactPickerActivity.this.contactsActivity.getActionBar().openSearchField("", false);
            DialogOrContactPickerActivity.this.searchItem.getSearchField().requestFocus();
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            DialogOrContactPickerActivity.this.dialogsActivity.getActionBar().closeSearchField(false);
            DialogOrContactPickerActivity.this.contactsActivity.getActionBar().closeSearchField(false);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            DialogOrContactPickerActivity.this.dialogsActivity.getActionBar().setSearchFieldText(editText.getText().toString());
            DialogOrContactPickerActivity.this.contactsActivity.getActionBar().setSearchFieldText(editText.getText().toString());
        }
    }

    /* renamed from: org.telegram.ui.DialogOrContactPickerActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 implements ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate {
        @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
        public /* synthetic */ void onSamePageSelected() {
            ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate.CC.$default$onSamePageSelected(this);
        }

        AnonymousClass3() {
            DialogOrContactPickerActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
        public void onPageSelected(int i, boolean z) {
            if (DialogOrContactPickerActivity.this.viewPages[0].selectedType == i) {
                return;
            }
            DialogOrContactPickerActivity dialogOrContactPickerActivity = DialogOrContactPickerActivity.this;
            dialogOrContactPickerActivity.swipeBackEnabled = i == dialogOrContactPickerActivity.scrollSlidingTextTabStrip.getFirstTabId();
            DialogOrContactPickerActivity.this.viewPages[1].selectedType = i;
            DialogOrContactPickerActivity.this.viewPages[1].setVisibility(0);
            DialogOrContactPickerActivity.this.switchToCurrentSelectedMode(true);
            DialogOrContactPickerActivity.this.animatingForward = z;
        }

        @Override // org.telegram.ui.Components.ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate
        public void onPageScrolled(float f) {
            if (f != 1.0f || DialogOrContactPickerActivity.this.viewPages[1].getVisibility() == 0) {
                if (DialogOrContactPickerActivity.this.animatingForward) {
                    DialogOrContactPickerActivity.this.viewPages[0].setTranslationX((-f) * DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth());
                    DialogOrContactPickerActivity.this.viewPages[1].setTranslationX(DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth() - (DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth() * f));
                } else {
                    DialogOrContactPickerActivity.this.viewPages[0].setTranslationX(DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth() * f);
                    DialogOrContactPickerActivity.this.viewPages[1].setTranslationX((DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth() * f) - DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth());
                }
                if (f != 1.0f) {
                    return;
                }
                ViewPage viewPage = DialogOrContactPickerActivity.this.viewPages[0];
                DialogOrContactPickerActivity.this.viewPages[0] = DialogOrContactPickerActivity.this.viewPages[1];
                DialogOrContactPickerActivity.this.viewPages[1] = viewPage;
                DialogOrContactPickerActivity.this.viewPages[1].setVisibility(8);
            }
        }
    }

    /* renamed from: org.telegram.ui.DialogOrContactPickerActivity$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends FrameLayout {
        private boolean globalIgnoreLayout;
        private boolean maybeStartTracking;
        private boolean startedTracking;
        private int startedTrackingPointerId;
        private int startedTrackingX;
        private int startedTrackingY;
        private VelocityTracker velocityTracker;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context) {
            super(context);
            DialogOrContactPickerActivity.this = r1;
        }

        private boolean prepareForMoving(MotionEvent motionEvent, boolean z) {
            int nextPageId = DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.getNextPageId(z);
            if (nextPageId < 0) {
                return false;
            }
            getParent().requestDisallowInterceptTouchEvent(true);
            this.maybeStartTracking = false;
            this.startedTracking = true;
            this.startedTrackingX = (int) motionEvent.getX();
            ((BaseFragment) DialogOrContactPickerActivity.this).actionBar.setEnabled(false);
            DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.setEnabled(false);
            DialogOrContactPickerActivity.this.viewPages[1].selectedType = nextPageId;
            DialogOrContactPickerActivity.this.viewPages[1].setVisibility(0);
            DialogOrContactPickerActivity.this.animatingForward = z;
            DialogOrContactPickerActivity.this.switchToCurrentSelectedMode(true);
            if (z) {
                DialogOrContactPickerActivity.this.viewPages[1].setTranslationX(DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth());
            } else {
                DialogOrContactPickerActivity.this.viewPages[1].setTranslationX(-DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth());
            }
            return true;
        }

        @Override // android.view.View
        public void forceHasOverlappingRendering(boolean z) {
            super.forceHasOverlappingRendering(z);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
            measureChildWithMargins(((BaseFragment) DialogOrContactPickerActivity.this).actionBar, i, 0, i2, 0);
            int measuredHeight = ((BaseFragment) DialogOrContactPickerActivity.this).actionBar.getMeasuredHeight();
            this.globalIgnoreLayout = true;
            for (int i3 = 0; i3 < DialogOrContactPickerActivity.this.viewPages.length; i3++) {
                if (DialogOrContactPickerActivity.this.viewPages[i3] != null) {
                    if (DialogOrContactPickerActivity.this.viewPages[i3].listView != null) {
                        DialogOrContactPickerActivity.this.viewPages[i3].listView.setPadding(0, measuredHeight, 0, 0);
                    }
                    if (DialogOrContactPickerActivity.this.viewPages[i3].listView2 != null) {
                        DialogOrContactPickerActivity.this.viewPages[i3].listView2.setPadding(0, measuredHeight, 0, 0);
                    }
                }
            }
            this.globalIgnoreLayout = false;
            int childCount = getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = getChildAt(i4);
                if (childAt != null && childAt.getVisibility() != 8 && childAt != ((BaseFragment) DialogOrContactPickerActivity.this).actionBar) {
                    measureChildWithMargins(childAt, i, 0, i2, 0);
                }
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (((BaseFragment) DialogOrContactPickerActivity.this).parentLayout != null) {
                ((BaseFragment) DialogOrContactPickerActivity.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) DialogOrContactPickerActivity.this).actionBar.getMeasuredHeight() + ((int) ((BaseFragment) DialogOrContactPickerActivity.this).actionBar.getTranslationY()));
            }
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.globalIgnoreLayout) {
                return;
            }
            super.requestLayout();
        }

        /* JADX WARN: Removed duplicated region for block: B:20:0x00a0  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean checkTabsAnimationInProgress() {
            if (DialogOrContactPickerActivity.this.tabsAnimationInProgress) {
                int i = -1;
                boolean z = true;
                if (DialogOrContactPickerActivity.this.backAnimation) {
                    if (Math.abs(DialogOrContactPickerActivity.this.viewPages[0].getTranslationX()) < 1.0f) {
                        DialogOrContactPickerActivity.this.viewPages[0].setTranslationX(0.0f);
                        ViewPage viewPage = DialogOrContactPickerActivity.this.viewPages[1];
                        int measuredWidth = DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth();
                        if (DialogOrContactPickerActivity.this.animatingForward) {
                            i = 1;
                        }
                        viewPage.setTranslationX(measuredWidth * i);
                        if (z) {
                            if (DialogOrContactPickerActivity.this.tabsAnimation != null) {
                                DialogOrContactPickerActivity.this.tabsAnimation.cancel();
                                DialogOrContactPickerActivity.this.tabsAnimation = null;
                            }
                            DialogOrContactPickerActivity.this.tabsAnimationInProgress = false;
                        }
                        return DialogOrContactPickerActivity.this.tabsAnimationInProgress;
                    }
                    z = false;
                    if (z) {
                    }
                    return DialogOrContactPickerActivity.this.tabsAnimationInProgress;
                }
                if (Math.abs(DialogOrContactPickerActivity.this.viewPages[1].getTranslationX()) < 1.0f) {
                    ViewPage viewPage2 = DialogOrContactPickerActivity.this.viewPages[0];
                    int measuredWidth2 = DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth();
                    if (!DialogOrContactPickerActivity.this.animatingForward) {
                        i = 1;
                    }
                    viewPage2.setTranslationX(measuredWidth2 * i);
                    DialogOrContactPickerActivity.this.viewPages[1].setTranslationX(0.0f);
                    if (z) {
                    }
                    return DialogOrContactPickerActivity.this.tabsAnimationInProgress;
                }
                z = false;
                if (z) {
                }
                return DialogOrContactPickerActivity.this.tabsAnimationInProgress;
            }
            return false;
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return checkTabsAnimationInProgress() || DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.isAnimatingIndicator() || onTouchEvent(motionEvent);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            DialogOrContactPickerActivity.this.backgroundPaint.setColor(Theme.getColor("windowBackgroundWhite"));
            canvas.drawRect(0.0f, ((BaseFragment) DialogOrContactPickerActivity.this).actionBar.getMeasuredHeight() + ((BaseFragment) DialogOrContactPickerActivity.this).actionBar.getTranslationY(), getMeasuredWidth(), getMeasuredHeight(), DialogOrContactPickerActivity.this.backgroundPaint);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            float f;
            float f2;
            float f3;
            int i;
            boolean z = false;
            if (((BaseFragment) DialogOrContactPickerActivity.this).parentLayout.checkTransitionAnimation() || checkTabsAnimationInProgress()) {
                return false;
            }
            if (motionEvent != null) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                this.velocityTracker.addMovement(motionEvent);
            }
            if (motionEvent != null && motionEvent.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                this.startedTrackingPointerId = motionEvent.getPointerId(0);
                this.maybeStartTracking = true;
                this.startedTrackingX = (int) motionEvent.getX();
                this.startedTrackingY = (int) motionEvent.getY();
                this.velocityTracker.clear();
            } else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                int x = (int) (motionEvent.getX() - this.startedTrackingX);
                int abs = Math.abs(((int) motionEvent.getY()) - this.startedTrackingY);
                if (this.startedTracking && ((DialogOrContactPickerActivity.this.animatingForward && x > 0) || (!DialogOrContactPickerActivity.this.animatingForward && x < 0))) {
                    if (!prepareForMoving(motionEvent, x < 0)) {
                        this.maybeStartTracking = true;
                        this.startedTracking = false;
                        DialogOrContactPickerActivity.this.viewPages[0].setTranslationX(0.0f);
                        DialogOrContactPickerActivity.this.viewPages[1].setTranslationX(DialogOrContactPickerActivity.this.animatingForward ? DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth() : -DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth());
                        DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DialogOrContactPickerActivity.this.viewPages[1].selectedType, 0.0f);
                    }
                }
                if (this.maybeStartTracking && !this.startedTracking) {
                    if (Math.abs(x) >= AndroidUtilities.getPixelsInCM(0.3f, true) && Math.abs(x) > abs) {
                        if (x < 0) {
                            z = true;
                        }
                        prepareForMoving(motionEvent, z);
                    }
                } else if (this.startedTracking) {
                    DialogOrContactPickerActivity.this.viewPages[0].setTranslationX(x);
                    if (DialogOrContactPickerActivity.this.animatingForward) {
                        DialogOrContactPickerActivity.this.viewPages[1].setTranslationX(DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth() + x);
                    } else {
                        DialogOrContactPickerActivity.this.viewPages[1].setTranslationX(x - DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth());
                    }
                    DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DialogOrContactPickerActivity.this.viewPages[1].selectedType, Math.abs(x) / DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth());
                }
            } else if (motionEvent == null || (motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
                this.velocityTracker.computeCurrentVelocity(1000, DialogOrContactPickerActivity.this.maximumVelocity);
                if (motionEvent == null || motionEvent.getAction() == 3) {
                    f2 = 0.0f;
                    f = 0.0f;
                } else {
                    f2 = this.velocityTracker.getXVelocity();
                    f = this.velocityTracker.getYVelocity();
                    if (!this.startedTracking && Math.abs(f2) >= 3000.0f && Math.abs(f2) > Math.abs(f)) {
                        prepareForMoving(motionEvent, f2 < 0.0f);
                    }
                }
                if (this.startedTracking) {
                    float x2 = DialogOrContactPickerActivity.this.viewPages[0].getX();
                    DialogOrContactPickerActivity.this.tabsAnimation = new AnimatorSet();
                    DialogOrContactPickerActivity.this.backAnimation = Math.abs(x2) < ((float) DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth()) / 3.0f && (Math.abs(f2) < 3500.0f || Math.abs(f2) < Math.abs(f));
                    if (!DialogOrContactPickerActivity.this.backAnimation) {
                        f3 = DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth() - Math.abs(x2);
                        if (DialogOrContactPickerActivity.this.animatingForward) {
                            DialogOrContactPickerActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogOrContactPickerActivity.this.viewPages[0], View.TRANSLATION_X, -DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth()), ObjectAnimator.ofFloat(DialogOrContactPickerActivity.this.viewPages[1], View.TRANSLATION_X, 0.0f));
                        } else {
                            DialogOrContactPickerActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogOrContactPickerActivity.this.viewPages[0], View.TRANSLATION_X, DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth()), ObjectAnimator.ofFloat(DialogOrContactPickerActivity.this.viewPages[1], View.TRANSLATION_X, 0.0f));
                        }
                    } else {
                        f3 = Math.abs(x2);
                        if (DialogOrContactPickerActivity.this.animatingForward) {
                            DialogOrContactPickerActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogOrContactPickerActivity.this.viewPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(DialogOrContactPickerActivity.this.viewPages[1], View.TRANSLATION_X, DialogOrContactPickerActivity.this.viewPages[1].getMeasuredWidth()));
                        } else {
                            DialogOrContactPickerActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogOrContactPickerActivity.this.viewPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(DialogOrContactPickerActivity.this.viewPages[1], View.TRANSLATION_X, -DialogOrContactPickerActivity.this.viewPages[1].getMeasuredWidth()));
                        }
                    }
                    DialogOrContactPickerActivity.this.tabsAnimation.setInterpolator(DialogOrContactPickerActivity.interpolator);
                    int measuredWidth = getMeasuredWidth();
                    float f4 = measuredWidth / 2;
                    float distanceInfluenceForSnapDuration = f4 + (AndroidUtilities.distanceInfluenceForSnapDuration(Math.min(1.0f, (f3 * 1.0f) / measuredWidth)) * f4);
                    float abs2 = Math.abs(f2);
                    if (abs2 > 0.0f) {
                        i = Math.round(Math.abs(distanceInfluenceForSnapDuration / abs2) * 1000.0f) * 4;
                    } else {
                        i = (int) (((f3 / getMeasuredWidth()) + 1.0f) * 100.0f);
                    }
                    DialogOrContactPickerActivity.this.tabsAnimation.setDuration(Math.max(150, Math.min(i, 600)));
                    DialogOrContactPickerActivity.this.tabsAnimation.addListener(new AnonymousClass1());
                    DialogOrContactPickerActivity.this.tabsAnimation.start();
                    DialogOrContactPickerActivity.this.tabsAnimationInProgress = true;
                    this.startedTracking = false;
                } else {
                    this.maybeStartTracking = false;
                    ((BaseFragment) DialogOrContactPickerActivity.this).actionBar.setEnabled(true);
                    DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
                }
                VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    this.velocityTracker = null;
                }
            }
            return this.startedTracking;
        }

        /* renamed from: org.telegram.ui.DialogOrContactPickerActivity$4$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
                AnonymousClass4.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                DialogOrContactPickerActivity.this.tabsAnimation = null;
                if (DialogOrContactPickerActivity.this.backAnimation) {
                    DialogOrContactPickerActivity.this.viewPages[1].setVisibility(8);
                } else {
                    ViewPage viewPage = DialogOrContactPickerActivity.this.viewPages[0];
                    DialogOrContactPickerActivity.this.viewPages[0] = DialogOrContactPickerActivity.this.viewPages[1];
                    DialogOrContactPickerActivity.this.viewPages[1] = viewPage;
                    DialogOrContactPickerActivity.this.viewPages[1].setVisibility(8);
                    DialogOrContactPickerActivity dialogOrContactPickerActivity = DialogOrContactPickerActivity.this;
                    dialogOrContactPickerActivity.swipeBackEnabled = dialogOrContactPickerActivity.viewPages[0].selectedType == DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.getFirstTabId();
                    DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DialogOrContactPickerActivity.this.viewPages[0].selectedType, 1.0f);
                }
                DialogOrContactPickerActivity.this.tabsAnimationInProgress = false;
                AnonymousClass4.this.maybeStartTracking = false;
                AnonymousClass4.this.startedTracking = false;
                ((BaseFragment) DialogOrContactPickerActivity.this).actionBar.setEnabled(true);
                DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
            }
        }
    }

    /* renamed from: org.telegram.ui.DialogOrContactPickerActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends ViewPage {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context) {
            super(context);
            DialogOrContactPickerActivity.this = r1;
        }

        @Override // android.view.View
        public void setTranslationX(float f) {
            super.setTranslationX(f);
            if (!DialogOrContactPickerActivity.this.tabsAnimationInProgress || DialogOrContactPickerActivity.this.viewPages[0] != this) {
                return;
            }
            DialogOrContactPickerActivity.this.scrollSlidingTextTabStrip.selectTabWithId(DialogOrContactPickerActivity.this.viewPages[1].selectedType, Math.abs(DialogOrContactPickerActivity.this.viewPages[0].getTranslationX()) / DialogOrContactPickerActivity.this.viewPages[0].getMeasuredWidth());
        }
    }

    /* renamed from: org.telegram.ui.DialogOrContactPickerActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends RecyclerView.OnScrollListener {
        final /* synthetic */ RecyclerView.OnScrollListener val$onScrollListener;

        AnonymousClass6(RecyclerView.OnScrollListener onScrollListener) {
            DialogOrContactPickerActivity.this = r1;
            this.val$onScrollListener = onScrollListener;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            this.val$onScrollListener.onScrollStateChanged(recyclerView, i);
            if (i != 1) {
                int i2 = (int) (-((BaseFragment) DialogOrContactPickerActivity.this).actionBar.getTranslationY());
                int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
                if (i2 == 0 || i2 == currentActionBarHeight) {
                    return;
                }
                if (i2 < currentActionBarHeight / 2) {
                    int i3 = -i2;
                    DialogOrContactPickerActivity.this.viewPages[0].listView.smoothScrollBy(0, i3);
                    if (DialogOrContactPickerActivity.this.viewPages[0].listView2 == null) {
                        return;
                    }
                    DialogOrContactPickerActivity.this.viewPages[0].listView2.smoothScrollBy(0, i3);
                    return;
                }
                int i4 = currentActionBarHeight - i2;
                DialogOrContactPickerActivity.this.viewPages[0].listView.smoothScrollBy(0, i4);
                if (DialogOrContactPickerActivity.this.viewPages[0].listView2 == null) {
                    return;
                }
                DialogOrContactPickerActivity.this.viewPages[0].listView2.smoothScrollBy(0, i4);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            this.val$onScrollListener.onScrolled(recyclerView, i, i2);
            if (recyclerView == DialogOrContactPickerActivity.this.viewPages[0].listView || recyclerView == DialogOrContactPickerActivity.this.viewPages[0].listView2) {
                float translationY = ((BaseFragment) DialogOrContactPickerActivity.this).actionBar.getTranslationY();
                float f = translationY - i2;
                if (f < (-ActionBar.getCurrentActionBarHeight())) {
                    f = -ActionBar.getCurrentActionBarHeight();
                } else if (f > 0.0f) {
                    f = 0.0f;
                }
                if (f == translationY) {
                    return;
                }
                DialogOrContactPickerActivity.this.setScrollY(f);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        DialogsActivity dialogsActivity = this.dialogsActivity;
        if (dialogsActivity != null) {
            dialogsActivity.onResume();
        }
        ContactsActivity contactsActivity = this.contactsActivity;
        if (contactsActivity != null) {
            contactsActivity.onResume();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        DialogsActivity dialogsActivity = this.dialogsActivity;
        if (dialogsActivity != null) {
            dialogsActivity.onPause();
        }
        ContactsActivity contactsActivity = this.contactsActivity;
        if (contactsActivity != null) {
            contactsActivity.onPause();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return this.swipeBackEnabled;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        DialogsActivity dialogsActivity = this.dialogsActivity;
        if (dialogsActivity != null) {
            dialogsActivity.onFragmentDestroy();
        }
        ContactsActivity contactsActivity = this.contactsActivity;
        if (contactsActivity != null) {
            contactsActivity.onFragmentDestroy();
        }
        super.onFragmentDestroy();
    }

    public void setScrollY(float f) {
        this.actionBar.setTranslationY(f);
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i < viewPageArr.length) {
                int i2 = (int) f;
                viewPageArr[i].listView.setPinnedSectionOffsetY(i2);
                if (this.viewPages[i].listView2 != null) {
                    this.viewPages[i].listView2.setPinnedSectionOffsetY(i2);
                }
                i++;
            } else {
                this.fragmentView.invalidate();
                return;
            }
        }
    }

    private void showBlockAlert(TLRPC$User tLRPC$User) {
        if (tLRPC$User == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("BlockUser", 2131624690));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureBlockContact2", 2131624433, ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name))));
        builder.setPositiveButton(LocaleController.getString("BlockContact", 2131624689), new DialogOrContactPickerActivity$$ExternalSyntheticLambda0(this, tLRPC$User));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView == null) {
            return;
        }
        textView.setTextColor(Theme.getColor("dialogTextRed2"));
    }

    public /* synthetic */ void lambda$showBlockAlert$3(TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
        if (MessagesController.isSupportUser(tLRPC$User)) {
            AlertsCreator.showSimpleToast(this, LocaleController.getString("ErrorOccurred", 2131625695));
        } else {
            MessagesController.getInstance(this.currentAccount).blockPeer(tLRPC$User.id);
            AlertsCreator.showSimpleToast(this, LocaleController.getString("UserBlocked", 2131628878));
        }
        finishFragment();
    }

    private void updateTabs() {
        ScrollSlidingTextTabStrip scrollSlidingTextTabStrip = this.scrollSlidingTextTabStrip;
        if (scrollSlidingTextTabStrip == null) {
            return;
        }
        scrollSlidingTextTabStrip.addTextTab(0, LocaleController.getString("BlockUserChatsTitle", 2131624692));
        this.scrollSlidingTextTabStrip.addTextTab(1, LocaleController.getString("BlockUserContactsTitle", 2131624693));
        this.scrollSlidingTextTabStrip.setVisibility(0);
        this.actionBar.setExtraHeight(AndroidUtilities.dp(44.0f));
        int currentTabId = this.scrollSlidingTextTabStrip.getCurrentTabId();
        if (currentTabId >= 0) {
            this.viewPages[0].selectedType = currentTabId;
        }
        this.scrollSlidingTextTabStrip.finishAddingTabs();
    }

    public void switchToCurrentSelectedMode(boolean z) {
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                break;
            }
            viewPageArr[i].listView.stopScroll();
            if (this.viewPages[i].listView2 != null) {
                this.viewPages[i].listView2.stopScroll();
            }
            i++;
        }
        int i2 = 0;
        while (i2 < 2) {
            ViewPage[] viewPageArr2 = this.viewPages;
            RecyclerListView recyclerListView = i2 == 0 ? viewPageArr2[z].listView : viewPageArr2[z ? 1 : 0].listView2;
            if (recyclerListView != null) {
                recyclerListView.getAdapter();
                recyclerListView.setPinnedHeaderShadowDrawable(null);
                if (this.actionBar.getTranslationY() != 0.0f) {
                    ((LinearLayoutManager) recyclerListView.getLayoutManager()).scrollToPositionWithOffset(0, (int) this.actionBar.getTranslationY());
                }
            }
            i2++;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.fragmentView, 0, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, null, null, null, "actionBarTabActiveText"));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, null, null, null, "actionBarTabUnactiveText"));
        arrayList.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{TextView.class}, null, null, null, "actionBarTabLine"));
        arrayList.add(new ThemeDescription(null, 0, null, null, new Drawable[]{this.scrollSlidingTextTabStrip.getSelectorDrawable()}, null, "actionBarTabSelector"));
        arrayList.addAll(this.dialogsActivity.getThemeDescriptions());
        arrayList.addAll(this.contactsActivity.getThemeDescriptions());
        return arrayList;
    }
}
