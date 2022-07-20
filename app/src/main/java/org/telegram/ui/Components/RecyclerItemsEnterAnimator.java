package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
/* loaded from: classes3.dex */
public class RecyclerItemsEnterAnimator {
    boolean alwaysCheckItemsAlpha;
    boolean invalidateAlpha;
    private final RecyclerListView listView;
    private final SparseArray<Float> listAlphaItems = new SparseArray<>();
    HashSet<View> ignoreView = new HashSet<>();
    public boolean animateAlphaProgressView = true;
    ArrayList<AnimatorSet> currentAnimations = new ArrayList<>();
    ArrayList<ViewTreeObserver.OnPreDrawListener> preDrawListeners = new ArrayList<>();

    public RecyclerItemsEnterAnimator(RecyclerListView recyclerListView, boolean z) {
        this.listView = recyclerListView;
        this.alwaysCheckItemsAlpha = z;
        recyclerListView.setItemsEnterAnimator(this);
    }

    public void dispatchDraw() {
        if (this.invalidateAlpha || this.alwaysCheckItemsAlpha) {
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                View childAt = this.listView.getChildAt(i);
                int childAdapterPosition = this.listView.getChildAdapterPosition(childAt);
                if (childAdapterPosition >= 0 && !this.ignoreView.contains(childAt)) {
                    Float f = this.listAlphaItems.get(childAdapterPosition, null);
                    if (f == null) {
                        childAt.setAlpha(1.0f);
                    } else {
                        childAt.setAlpha(f.floatValue());
                    }
                }
            }
            this.invalidateAlpha = false;
        }
    }

    public void showItemsAnimated(int i) {
        View progressView = getProgressView();
        RecyclerView.LayoutManager layoutManager = this.listView.getLayoutManager();
        if (progressView != null && layoutManager != null) {
            this.listView.removeView(progressView);
            this.ignoreView.add(progressView);
            this.listView.addView(progressView);
            layoutManager.ignoreView(progressView);
            Animator ofFloat = this.animateAlphaProgressView ? ObjectAnimator.ofFloat(progressView, View.ALPHA, progressView.getAlpha(), 0.0f) : ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addListener(new AnonymousClass1(progressView, layoutManager));
            ofFloat.start();
            i--;
        }
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(progressView, i);
        this.preDrawListeners.add(anonymousClass2);
        this.listView.getViewTreeObserver().addOnPreDrawListener(anonymousClass2);
    }

    /* renamed from: org.telegram.ui.Components.RecyclerItemsEnterAnimator$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        final /* synthetic */ View val$finalProgressView;
        final /* synthetic */ RecyclerView.LayoutManager val$layoutManager;

        AnonymousClass1(View view, RecyclerView.LayoutManager layoutManager) {
            RecyclerItemsEnterAnimator.this = r1;
            this.val$finalProgressView = view;
            this.val$layoutManager = layoutManager;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$finalProgressView.setAlpha(1.0f);
            this.val$layoutManager.stopIgnoringView(this.val$finalProgressView);
            RecyclerItemsEnterAnimator.this.ignoreView.remove(this.val$finalProgressView);
            RecyclerItemsEnterAnimator.this.listView.removeView(this.val$finalProgressView);
        }
    }

    /* renamed from: org.telegram.ui.Components.RecyclerItemsEnterAnimator$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ int val$finalFrom;
        final /* synthetic */ View val$finalProgressView;

        AnonymousClass2(View view, int i) {
            RecyclerItemsEnterAnimator.this = r1;
            this.val$finalProgressView = view;
            this.val$finalFrom = i;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            RecyclerItemsEnterAnimator.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
            RecyclerItemsEnterAnimator.this.preDrawListeners.remove(this);
            int childCount = RecyclerItemsEnterAnimator.this.listView.getChildCount();
            AnimatorSet animatorSet = new AnimatorSet();
            for (int i = 0; i < childCount; i++) {
                View childAt = RecyclerItemsEnterAnimator.this.listView.getChildAt(i);
                int childAdapterPosition = RecyclerItemsEnterAnimator.this.listView.getChildAdapterPosition(childAt);
                if (childAt != this.val$finalProgressView && childAdapterPosition >= this.val$finalFrom - 1 && RecyclerItemsEnterAnimator.this.listAlphaItems.get(childAdapterPosition, null) == null) {
                    RecyclerItemsEnterAnimator.this.listAlphaItems.put(childAdapterPosition, Float.valueOf(0.0f));
                    RecyclerItemsEnterAnimator recyclerItemsEnterAnimator = RecyclerItemsEnterAnimator.this;
                    recyclerItemsEnterAnimator.invalidateAlpha = true;
                    recyclerItemsEnterAnimator.listView.invalidate();
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                    ofFloat.addUpdateListener(new RecyclerItemsEnterAnimator$2$$ExternalSyntheticLambda0(this, childAdapterPosition));
                    ofFloat.addListener(new AnonymousClass1(childAdapterPosition));
                    ofFloat.setStartDelay((int) ((Math.min(RecyclerItemsEnterAnimator.this.listView.getMeasuredHeight(), Math.max(0, childAt.getTop())) / RecyclerItemsEnterAnimator.this.listView.getMeasuredHeight()) * 100.0f));
                    ofFloat.setDuration(200L);
                    animatorSet.playTogether(ofFloat);
                }
            }
            RecyclerItemsEnterAnimator.this.currentAnimations.add(animatorSet);
            animatorSet.start();
            animatorSet.addListener(new C00312(animatorSet));
            return false;
        }

        public /* synthetic */ void lambda$onPreDraw$0(int i, ValueAnimator valueAnimator) {
            RecyclerItemsEnterAnimator.this.listAlphaItems.put(i, (Float) valueAnimator.getAnimatedValue());
            RecyclerItemsEnterAnimator recyclerItemsEnterAnimator = RecyclerItemsEnterAnimator.this;
            recyclerItemsEnterAnimator.invalidateAlpha = true;
            recyclerItemsEnterAnimator.listView.invalidate();
        }

        /* renamed from: org.telegram.ui.Components.RecyclerItemsEnterAnimator$2$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            final /* synthetic */ int val$position;

            AnonymousClass1(int i) {
                AnonymousClass2.this = r1;
                this.val$position = i;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                RecyclerItemsEnterAnimator.this.listAlphaItems.remove(this.val$position);
                RecyclerItemsEnterAnimator recyclerItemsEnterAnimator = RecyclerItemsEnterAnimator.this;
                recyclerItemsEnterAnimator.invalidateAlpha = true;
                recyclerItemsEnterAnimator.listView.invalidate();
            }
        }

        /* renamed from: org.telegram.ui.Components.RecyclerItemsEnterAnimator$2$2 */
        /* loaded from: classes3.dex */
        class C00312 extends AnimatorListenerAdapter {
            final /* synthetic */ AnimatorSet val$animatorSet;

            C00312(AnimatorSet animatorSet) {
                AnonymousClass2.this = r1;
                this.val$animatorSet = animatorSet;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                RecyclerItemsEnterAnimator.this.currentAnimations.remove(this.val$animatorSet);
                if (RecyclerItemsEnterAnimator.this.currentAnimations.isEmpty()) {
                    RecyclerItemsEnterAnimator.this.listAlphaItems.clear();
                    RecyclerItemsEnterAnimator recyclerItemsEnterAnimator = RecyclerItemsEnterAnimator.this;
                    recyclerItemsEnterAnimator.invalidateAlpha = true;
                    recyclerItemsEnterAnimator.listView.invalidate();
                }
            }
        }
    }

    public View getProgressView() {
        int childCount = this.listView.getChildCount();
        View view = null;
        for (int i = 0; i < childCount; i++) {
            View childAt = this.listView.getChildAt(i);
            if (this.listView.getChildAdapterPosition(childAt) >= 0 && (childAt instanceof FlickerLoadingView)) {
                view = childAt;
            }
        }
        return view;
    }

    public void onDetached() {
        cancel();
    }

    public void cancel() {
        if (!this.currentAnimations.isEmpty()) {
            ArrayList arrayList = new ArrayList(this.currentAnimations);
            for (int i = 0; i < arrayList.size(); i++) {
                ((AnimatorSet) arrayList.get(i)).end();
                ((AnimatorSet) arrayList.get(i)).cancel();
            }
        }
        this.currentAnimations.clear();
        for (int i2 = 0; i2 < this.preDrawListeners.size(); i2++) {
            this.listView.getViewTreeObserver().removeOnPreDrawListener(this.preDrawListeners.get(i2));
        }
        this.preDrawListeners.clear();
        this.listAlphaItems.clear();
        this.listView.invalidate();
        this.invalidateAlpha = true;
    }
}
