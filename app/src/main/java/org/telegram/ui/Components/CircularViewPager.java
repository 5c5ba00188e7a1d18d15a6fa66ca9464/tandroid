package org.telegram.ui.Components;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
/* loaded from: classes3.dex */
public class CircularViewPager extends ViewPager {
    private Adapter adapter;

    public CircularViewPager(Context context) {
        super(context);
        addOnPageChangeListener(new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Components.CircularViewPager$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements ViewPager.OnPageChangeListener {
        private int scrollState;

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
        }

        AnonymousClass1() {
            CircularViewPager.this = r1;
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            if (i == CircularViewPager.this.getCurrentItem() && f == 0.0f && this.scrollState == 1) {
                checkCurrentItem();
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
            if (i == 0) {
                checkCurrentItem();
            }
            this.scrollState = i;
        }

        private void checkCurrentItem() {
            if (CircularViewPager.this.adapter != null) {
                int currentItem = CircularViewPager.this.getCurrentItem();
                int extraCount = CircularViewPager.this.adapter.getExtraCount() + CircularViewPager.this.adapter.getRealPosition(currentItem);
                if (currentItem == extraCount) {
                    return;
                }
                CircularViewPager.this.setCurrentItem(extraCount, false);
            }
        }
    }

    @Override // androidx.viewpager.widget.ViewPager
    @Deprecated
    public void setAdapter(PagerAdapter pagerAdapter) {
        if (pagerAdapter instanceof Adapter) {
            setAdapter((Adapter) pagerAdapter);
            return;
        }
        throw new IllegalArgumentException();
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        super.setAdapter((PagerAdapter) adapter);
        if (adapter != null) {
            setCurrentItem(adapter.getExtraCount(), false);
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Adapter extends PagerAdapter {
        public abstract int getExtraCount();

        public int getRealPosition(int i) {
            int count = getCount();
            int extraCount = getExtraCount();
            if (i < extraCount) {
                return ((count - (extraCount * 2)) - ((extraCount - i) - 1)) - 1;
            }
            int i2 = count - extraCount;
            return i >= i2 ? i - i2 : i - extraCount;
        }
    }
}
