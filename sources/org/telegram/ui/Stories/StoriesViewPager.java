package org.telegram.ui.Stories;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Stories.PeerStoriesView;
import org.telegram.ui.Stories.StoriesViewPager;
/* loaded from: classes4.dex */
public class StoriesViewPager extends ViewPager {
    int currentAccount;
    public int currentState;
    PeerStoriesView.Delegate delegate;
    ArrayList<Long> dialogs;
    Runnable doOnNextIdle;
    int keyboardHeight;
    float lastProgressToDismiss;
    Runnable lockTouchRunnable;
    PagerAdapter pagerAdapter;
    float progress;
    PeerStoriesView.SharedResources resources;
    int selectedPosition;
    StoryViewer storyViewer;
    int toPosition;
    boolean touchEnabled;
    private boolean touchLocked;
    boolean updateDelegate;

    public void onStateChanged() {
    }

    public StoriesViewPager(final Context context, final StoryViewer storyViewer, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.dialogs = new ArrayList<>();
        this.touchEnabled = true;
        this.lockTouchRunnable = new Runnable() { // from class: org.telegram.ui.Stories.StoriesViewPager.1
            @Override // java.lang.Runnable
            public void run() {
                StoriesViewPager.this.touchLocked = false;
            }
        };
        this.resources = new PeerStoriesView.SharedResources(context);
        this.storyViewer = storyViewer;
        PagerAdapter pagerAdapter = new PagerAdapter() { // from class: org.telegram.ui.Stories.StoriesViewPager.2
            ArrayList<PeerStoriesView> cachedViews = new ArrayList<>();

            @Override // androidx.viewpager.widget.PagerAdapter
            public boolean isViewFromObject(View view, Object obj) {
                return view == obj;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return StoriesViewPager.this.dialogs.size();
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public Object instantiateItem(ViewGroup viewGroup, int i) {
                PeerStoriesView peerStoriesView;
                PageLayout pageLayout = new PageLayout(context);
                if (!this.cachedViews.isEmpty()) {
                    peerStoriesView = this.cachedViews.remove(0);
                    peerStoriesView.reset();
                } else {
                    peerStoriesView = new HwPeerStoriesView(context, storyViewer, StoriesViewPager.this.resources, resourcesProvider) { // from class: org.telegram.ui.Stories.StoriesViewPager.2.1
                        @Override // org.telegram.ui.Stories.PeerStoriesView
                        public boolean isSelectedPeer() {
                            return getParent() != null && ((Integer) ((View) getParent()).getTag()).intValue() == StoriesViewPager.this.getCurrentItem();
                        }
                    };
                }
                pageLayout.peerStoryView = peerStoriesView;
                peerStoriesView.setAccount(StoriesViewPager.this.currentAccount);
                peerStoriesView.setDelegate(StoriesViewPager.this.delegate);
                peerStoriesView.setLongpressed(storyViewer.isLongpressed);
                pageLayout.setTag(Integer.valueOf(i));
                pageLayout.dialogId = StoriesViewPager.this.dialogs.get(i).longValue();
                pageLayout.addView(peerStoriesView);
                peerStoriesView.requestLayout();
                viewGroup.addView(pageLayout);
                return pageLayout;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
                FrameLayout frameLayout = (FrameLayout) obj;
                viewGroup.removeView(frameLayout);
                PeerStoriesView peerStoriesView = (PeerStoriesView) frameLayout.getChildAt(0);
                AndroidUtilities.removeFromParent(peerStoriesView);
                this.cachedViews.add(peerStoriesView);
            }
        };
        this.pagerAdapter = pagerAdapter;
        setAdapter(pagerAdapter);
        setPageTransformer(false, StoriesViewPager$$ExternalSyntheticLambda0.INSTANCE);
        setOffscreenPageLimit(0);
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.Stories.StoriesViewPager.3
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
                StoriesViewPager storiesViewPager = StoriesViewPager.this;
                storiesViewPager.selectedPosition = i;
                storiesViewPager.toPosition = i2 > 0 ? i + 1 : i - 1;
                storiesViewPager.progress = f;
                if (i >= 0 && i < storiesViewPager.dialogs.size()) {
                    StoriesViewPager storiesViewPager2 = StoriesViewPager.this;
                    if (storiesViewPager2.dialogs.get(storiesViewPager2.selectedPosition).longValue() == UserConfig.getInstance(StoriesViewPager.this.currentAccount).clientUserId) {
                        StoriesViewPager storiesViewPager3 = StoriesViewPager.this;
                        storiesViewPager3.delegate.setHideEnterViewProgress(1.0f - storiesViewPager3.progress);
                        return;
                    }
                }
                StoriesViewPager storiesViewPager4 = StoriesViewPager.this;
                int i3 = storiesViewPager4.toPosition;
                if (i3 >= 0 && i3 < storiesViewPager4.dialogs.size()) {
                    StoriesViewPager storiesViewPager5 = StoriesViewPager.this;
                    if (storiesViewPager5.dialogs.get(storiesViewPager5.toPosition).longValue() == UserConfig.getInstance(StoriesViewPager.this.currentAccount).clientUserId) {
                        StoriesViewPager storiesViewPager6 = StoriesViewPager.this;
                        storiesViewPager6.delegate.setHideEnterViewProgress(storiesViewPager6.progress);
                        return;
                    }
                }
                StoriesViewPager.this.delegate.setHideEnterViewProgress(0.0f);
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                PeerStoriesView currentPeerView = StoriesViewPager.this.getCurrentPeerView();
                if (currentPeerView == null) {
                    return;
                }
                StoriesViewPager.this.delegate.onPeerSelected(currentPeerView.getCurrentPeer(), currentPeerView.getSelectedPosition());
                StoriesViewPager.this.updateActiveStory();
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
                StoriesViewPager.this.delegate.setAllowTouchesByViewPager(i != 0);
                Runnable runnable = StoriesViewPager.this.doOnNextIdle;
                if (runnable != null && i == 0) {
                    runnable.run();
                    StoriesViewPager.this.doOnNextIdle = null;
                }
                StoriesViewPager storiesViewPager = StoriesViewPager.this;
                storiesViewPager.currentState = i;
                storiesViewPager.onStateChanged();
            }
        });
        setOverScrollMode(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$1(View view, float f) {
        final PageLayout pageLayout = (PageLayout) view;
        if (Math.abs(f) >= 1.0f) {
            pageLayout.setVisible(false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoriesViewPager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    StoriesViewPager.lambda$new$0(StoriesViewPager.PageLayout.this);
                }
            }, 16L);
            return;
        }
        if (!pageLayout.isVisible) {
            pageLayout.setVisible(true);
            pageLayout.peerStoryView.setDialogId(pageLayout.dialogId);
        }
        pageLayout.peerStoryView.setOffset(f);
        view.setCameraDistance(view.getWidth() * 15);
        view.setPivotX(f < 0.0f ? view.getWidth() : 0.0f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setRotationY(f * 90.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(PageLayout pageLayout) {
        pageLayout.peerStoryView.preloadMainImage(pageLayout.dialogId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActiveStory() {
        for (int i = 0; i < getChildCount(); i++) {
            ((PeerStoriesView) ((FrameLayout) getChildAt(i)).getChildAt(0)).setActive(((Integer) getChildAt(i).getTag()).intValue() == getCurrentItem());
        }
    }

    public void checkAllowScreenshots() {
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= getChildCount()) {
                z = true;
                break;
            }
            PageLayout pageLayout = (PageLayout) getChildAt(i);
            if (pageLayout.isVisible && !pageLayout.peerStoryView.allowScreenshots) {
                break;
            }
            i++;
        }
        this.storyViewer.allowScreenshots(z);
    }

    public boolean canScroll(float f) {
        int i = this.selectedPosition;
        if (i == 0 && this.progress == 0.0f && f < 0.0f) {
            return false;
        }
        return (i == getAdapter().getCount() - 1 && this.progress == 0.0f && f > 0.0f) ? false : true;
    }

    public PeerStoriesView getCurrentPeerView() {
        for (int i = 0; i < getChildCount(); i++) {
            if (((Integer) getChildAt(i).getTag()).intValue() == getCurrentItem()) {
                return (PeerStoriesView) ((FrameLayout) getChildAt(i)).getChildAt(0);
            }
        }
        return null;
    }

    public void setPeerIds(ArrayList<Long> arrayList, int i, int i2) {
        this.dialogs = arrayList;
        this.currentAccount = i;
        setAdapter(null);
        setAdapter(this.pagerAdapter);
        setCurrentItem(i2);
        this.updateDelegate = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.updateDelegate) {
            this.updateDelegate = false;
            PeerStoriesView currentPeerView = getCurrentPeerView();
            if (currentPeerView != null) {
                this.delegate.onPeerSelected(currentPeerView.getCurrentPeer(), currentPeerView.getSelectedPosition());
            }
        }
        updateActiveStory();
    }

    public void setDelegate(PeerStoriesView.Delegate delegate) {
        this.delegate = delegate;
    }

    public boolean useSurfaceInViewPagerWorkAround() {
        return this.storyViewer.USE_SURFACE_VIEW && Build.VERSION.SDK_INT < 33;
    }

    public boolean switchToNext(boolean z) {
        if (z && getCurrentItem() < this.dialogs.size() - 1) {
            setCurrentItem(getCurrentItem() + 1, !useSurfaceInViewPagerWorkAround());
            return true;
        } else if (z || getCurrentItem() <= 0) {
            return false;
        } else {
            setCurrentItem(getCurrentItem() - 1, !useSurfaceInViewPagerWorkAround());
            return true;
        }
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.touchEnabled && !this.touchLocked) {
            try {
                return super.onInterceptTouchEvent(motionEvent);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        return false;
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.touchEnabled || this.touchLocked) {
            if (this.touchLocked) {
                return motionEvent.getAction() == 0 || motionEvent.getAction() == 2;
            }
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void enableTouch(boolean z) {
        this.touchEnabled = z;
    }

    public void setPaused(boolean z) {
        for (int i = 0; i < getChildCount(); i++) {
            ((PeerStoriesView) ((FrameLayout) getChildAt(i)).getChildAt(0)).setPaused(z);
        }
    }

    public long getCurrentDialogId() {
        if (getCurrentItem() < this.dialogs.size()) {
            return this.dialogs.get(getCurrentItem()).longValue();
        }
        return 0L;
    }

    public void onNextIdle(Runnable runnable) {
        this.doOnNextIdle = runnable;
    }

    public void setKeyboardHeight(int i) {
        if (this.keyboardHeight != i) {
            this.keyboardHeight = i;
            PeerStoriesView currentPeerView = getCurrentPeerView();
            if (currentPeerView != null) {
                currentPeerView.requestLayout();
            }
        }
    }

    public void setHorizontalProgressToDismiss(float f) {
        if (Math.abs(f) > 1.0f || this.lastProgressToDismiss == f) {
            return;
        }
        this.lastProgressToDismiss = f;
        setCameraDistance(getWidth() * 15);
        setPivotX(f < 0.0f ? getWidth() : 0.0f);
        setPivotY(getHeight() * 0.5f);
        setRotationY(f * 90.0f);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
    }

    public void lockTouchEvent(long j) {
        this.touchLocked = true;
        onTouchEvent(MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0));
        AndroidUtilities.cancelRunOnUIThread(this.lockTouchRunnable);
        AndroidUtilities.runOnUIThread(this.lockTouchRunnable, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class PageLayout extends FrameLayout {
        long dialogId;
        boolean isVisible;
        public PeerStoriesView peerStoryView;

        public PageLayout(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (this.isVisible) {
                super.dispatchDraw(canvas);
            }
        }

        public void setVisible(boolean z) {
            if (this.isVisible != z) {
                this.isVisible = z;
                invalidate();
                this.peerStoryView.setIsVisible(z);
                StoriesViewPager.this.checkAllowScreenshots();
            }
        }
    }
}
