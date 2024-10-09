package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.collection.ArrayMap;
import androidx.core.os.CancellationSignal;
import androidx.core.util.Preconditions;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.SpecialEffectsController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
class DefaultSpecialEffectsController extends SpecialEffectsController {

    /* loaded from: classes.dex */
    static /* synthetic */ class 10 {
        static final /* synthetic */ int[] $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State;

        static {
            int[] iArr = new int[SpecialEffectsController.Operation.State.values().length];
            $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State = iArr;
            try {
                iArr[SpecialEffectsController.Operation.State.GONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[SpecialEffectsController.Operation.State.INVISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[SpecialEffectsController.Operation.State.REMOVED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[SpecialEffectsController.Operation.State.VISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnimationInfo extends SpecialEffectsInfo {
        private FragmentAnim.AnimationOrAnimator mAnimation;
        private boolean mIsPop;
        private boolean mLoadedAnim;

        AnimationInfo(SpecialEffectsController.Operation operation, CancellationSignal cancellationSignal, boolean z) {
            super(operation, cancellationSignal);
            this.mLoadedAnim = false;
            this.mIsPop = z;
        }

        FragmentAnim.AnimationOrAnimator getAnimation(Context context) {
            if (this.mLoadedAnim) {
                return this.mAnimation;
            }
            FragmentAnim.AnimationOrAnimator loadAnimation = FragmentAnim.loadAnimation(context, getOperation().getFragment(), getOperation().getFinalState() == SpecialEffectsController.Operation.State.VISIBLE, this.mIsPop);
            this.mAnimation = loadAnimation;
            this.mLoadedAnim = true;
            return loadAnimation;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SpecialEffectsInfo {
        private final SpecialEffectsController.Operation mOperation;
        private final CancellationSignal mSignal;

        SpecialEffectsInfo(SpecialEffectsController.Operation operation, CancellationSignal cancellationSignal) {
            this.mOperation = operation;
            this.mSignal = cancellationSignal;
        }

        void completeSpecialEffect() {
            this.mOperation.completeSpecialEffect(this.mSignal);
        }

        SpecialEffectsController.Operation getOperation() {
            return this.mOperation;
        }

        CancellationSignal getSignal() {
            return this.mSignal;
        }

        boolean isVisibilityUnchanged() {
            SpecialEffectsController.Operation.State state;
            SpecialEffectsController.Operation.State from = SpecialEffectsController.Operation.State.from(this.mOperation.getFragment().mView);
            SpecialEffectsController.Operation.State finalState = this.mOperation.getFinalState();
            return from == finalState || !(from == (state = SpecialEffectsController.Operation.State.VISIBLE) || finalState == state);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TransitionInfo extends SpecialEffectsInfo {
        private final boolean mOverlapAllowed;
        private final Object mSharedElementTransition;
        private final Object mTransition;

        TransitionInfo(SpecialEffectsController.Operation operation, CancellationSignal cancellationSignal, boolean z, boolean z2) {
            super(operation, cancellationSignal);
            boolean z3;
            Object obj;
            if (operation.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
                Fragment fragment = operation.getFragment();
                this.mTransition = z ? fragment.getReenterTransition() : fragment.getEnterTransition();
                Fragment fragment2 = operation.getFragment();
                z3 = z ? fragment2.getAllowReturnTransitionOverlap() : fragment2.getAllowEnterTransitionOverlap();
            } else {
                Fragment fragment3 = operation.getFragment();
                this.mTransition = z ? fragment3.getReturnTransition() : fragment3.getExitTransition();
                z3 = true;
            }
            this.mOverlapAllowed = z3;
            if (z2) {
                Fragment fragment4 = operation.getFragment();
                obj = z ? fragment4.getSharedElementReturnTransition() : fragment4.getSharedElementEnterTransition();
            } else {
                obj = null;
            }
            this.mSharedElementTransition = obj;
        }

        private FragmentTransitionImpl getHandlingImpl(Object obj) {
            if (obj == null) {
                return null;
            }
            FragmentTransitionImpl fragmentTransitionImpl = FragmentTransition.PLATFORM_IMPL;
            if (fragmentTransitionImpl != null && fragmentTransitionImpl.canHandle(obj)) {
                return fragmentTransitionImpl;
            }
            FragmentTransitionImpl fragmentTransitionImpl2 = FragmentTransition.SUPPORT_IMPL;
            if (fragmentTransitionImpl2 != null && fragmentTransitionImpl2.canHandle(obj)) {
                return fragmentTransitionImpl2;
            }
            throw new IllegalArgumentException("Transition " + obj + " for fragment " + getOperation().getFragment() + " is not a valid framework Transition or AndroidX Transition");
        }

        FragmentTransitionImpl getHandlingImpl() {
            FragmentTransitionImpl handlingImpl = getHandlingImpl(this.mTransition);
            FragmentTransitionImpl handlingImpl2 = getHandlingImpl(this.mSharedElementTransition);
            if (handlingImpl == null || handlingImpl2 == null || handlingImpl == handlingImpl2) {
                return handlingImpl != null ? handlingImpl : handlingImpl2;
            }
            throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + getOperation().getFragment() + " returned Transition " + this.mTransition + " which uses a different Transition  type than its shared element transition " + this.mSharedElementTransition);
        }

        public Object getSharedElementTransition() {
            return this.mSharedElementTransition;
        }

        Object getTransition() {
            return this.mTransition;
        }

        public boolean hasSharedElementTransition() {
            return this.mSharedElementTransition != null;
        }

        boolean isOverlapAllowed() {
            return this.mOverlapAllowed;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultSpecialEffectsController(ViewGroup viewGroup) {
        super(viewGroup);
    }

    private void startAnimations(List list, List list2, boolean z, Map map) {
        StringBuilder sb;
        String str;
        FragmentAnim.AnimationOrAnimator animation;
        final ViewGroup container = getContainer();
        Context context = container.getContext();
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            final AnimationInfo animationInfo = (AnimationInfo) it.next();
            if (animationInfo.isVisibilityUnchanged() || (animation = animationInfo.getAnimation(context)) == null) {
                animationInfo.completeSpecialEffect();
            } else {
                final Animator animator = animation.animator;
                if (animator == null) {
                    arrayList.add(animationInfo);
                } else {
                    final SpecialEffectsController.Operation operation = animationInfo.getOperation();
                    Fragment fragment = operation.getFragment();
                    if (Boolean.TRUE.equals(map.get(operation))) {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "Ignoring Animator set on " + fragment + " as this Fragment was involved in a Transition.");
                        }
                        animationInfo.completeSpecialEffect();
                    } else {
                        final boolean z3 = operation.getFinalState() == SpecialEffectsController.Operation.State.GONE;
                        if (z3) {
                            list2.remove(operation);
                        }
                        final View view = fragment.mView;
                        container.startViewTransition(view);
                        animator.addListener(new AnimatorListenerAdapter() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.2
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator2) {
                                container.endViewTransition(view);
                                if (z3) {
                                    operation.getFinalState().applyState(view);
                                }
                                animationInfo.completeSpecialEffect();
                            }
                        });
                        animator.setTarget(view);
                        animator.start();
                        animationInfo.getSignal().setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.3
                            @Override // androidx.core.os.CancellationSignal.OnCancelListener
                            public void onCancel() {
                                animator.end();
                            }
                        });
                        z2 = true;
                    }
                }
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            final AnimationInfo animationInfo2 = (AnimationInfo) it2.next();
            SpecialEffectsController.Operation operation2 = animationInfo2.getOperation();
            Fragment fragment2 = operation2.getFragment();
            if (z) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    sb = new StringBuilder();
                    sb.append("Ignoring Animation set on ");
                    sb.append(fragment2);
                    str = " as Animations cannot run alongside Transitions.";
                    sb.append(str);
                    Log.v("FragmentManager", sb.toString());
                }
                animationInfo2.completeSpecialEffect();
            } else if (z2) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    sb = new StringBuilder();
                    sb.append("Ignoring Animation set on ");
                    sb.append(fragment2);
                    str = " as Animations cannot run alongside Animators.";
                    sb.append(str);
                    Log.v("FragmentManager", sb.toString());
                }
                animationInfo2.completeSpecialEffect();
            } else {
                final View view2 = fragment2.mView;
                Animation animation2 = (Animation) Preconditions.checkNotNull(((FragmentAnim.AnimationOrAnimator) Preconditions.checkNotNull(animationInfo2.getAnimation(context))).animation);
                if (operation2.getFinalState() != SpecialEffectsController.Operation.State.REMOVED) {
                    view2.startAnimation(animation2);
                    animationInfo2.completeSpecialEffect();
                } else {
                    container.startViewTransition(view2);
                    FragmentAnim.EndViewTransitionAnimation endViewTransitionAnimation = new FragmentAnim.EndViewTransitionAnimation(animation2, container, view2);
                    endViewTransitionAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.4
                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationEnd(Animation animation3) {
                            container.post(new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.4.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    4 r0 = 4.this;
                                    container.endViewTransition(view2);
                                    animationInfo2.completeSpecialEffect();
                                }
                            });
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationRepeat(Animation animation3) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationStart(Animation animation3) {
                        }
                    });
                    view2.startAnimation(endViewTransitionAnimation);
                }
                animationInfo2.getSignal().setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.5
                    @Override // androidx.core.os.CancellationSignal.OnCancelListener
                    public void onCancel() {
                        view2.clearAnimation();
                        container.endViewTransition(view2);
                        animationInfo2.completeSpecialEffect();
                    }
                });
            }
        }
    }

    private Map startTransitions(List list, List list2, final boolean z, final SpecialEffectsController.Operation operation, final SpecialEffectsController.Operation operation2) {
        View view;
        Object obj;
        ArrayList arrayList;
        Object obj2;
        ArrayList arrayList2;
        SpecialEffectsController.Operation operation3;
        SpecialEffectsController.Operation operation4;
        View view2;
        Object mergeTransitionsTogether;
        ArrayMap arrayMap;
        ArrayList arrayList3;
        SpecialEffectsController.Operation operation5;
        ArrayList arrayList4;
        Rect rect;
        View view3;
        FragmentTransitionImpl fragmentTransitionImpl;
        SpecialEffectsController.Operation operation6;
        final View view4;
        boolean z2 = z;
        SpecialEffectsController.Operation operation7 = operation;
        SpecialEffectsController.Operation operation8 = operation2;
        HashMap hashMap = new HashMap();
        Iterator it = list.iterator();
        final FragmentTransitionImpl fragmentTransitionImpl2 = null;
        while (it.hasNext()) {
            TransitionInfo transitionInfo = (TransitionInfo) it.next();
            if (!transitionInfo.isVisibilityUnchanged()) {
                FragmentTransitionImpl handlingImpl = transitionInfo.getHandlingImpl();
                if (fragmentTransitionImpl2 == null) {
                    fragmentTransitionImpl2 = handlingImpl;
                } else if (handlingImpl != null && fragmentTransitionImpl2 != handlingImpl) {
                    throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + transitionInfo.getOperation().getFragment() + " returned Transition " + transitionInfo.getTransition() + " which uses a different Transition  type than other Fragments.");
                }
            }
        }
        if (fragmentTransitionImpl2 == null) {
            Iterator it2 = list.iterator();
            while (it2.hasNext()) {
                TransitionInfo transitionInfo2 = (TransitionInfo) it2.next();
                hashMap.put(transitionInfo2.getOperation(), Boolean.FALSE);
                transitionInfo2.completeSpecialEffect();
            }
            return hashMap;
        }
        View view5 = new View(getContainer().getContext());
        final Rect rect2 = new Rect();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayMap arrayMap2 = new ArrayMap();
        Iterator it3 = list.iterator();
        Object obj3 = null;
        View view6 = null;
        boolean z3 = false;
        while (it3.hasNext()) {
            TransitionInfo transitionInfo3 = (TransitionInfo) it3.next();
            if (!transitionInfo3.hasSharedElementTransition() || operation7 == null || operation8 == null) {
                arrayMap = arrayMap2;
                arrayList3 = arrayList6;
                operation5 = operation7;
                arrayList4 = arrayList5;
                rect = rect2;
                view3 = view5;
                fragmentTransitionImpl = fragmentTransitionImpl2;
                operation6 = operation8;
                view6 = view6;
            } else {
                Object wrapTransitionInSet = fragmentTransitionImpl2.wrapTransitionInSet(fragmentTransitionImpl2.cloneTransition(transitionInfo3.getSharedElementTransition()));
                ArrayList sharedElementSourceNames = operation2.getFragment().getSharedElementSourceNames();
                ArrayList sharedElementSourceNames2 = operation.getFragment().getSharedElementSourceNames();
                ArrayList sharedElementTargetNames = operation.getFragment().getSharedElementTargetNames();
                View view7 = view6;
                int i = 0;
                while (i < sharedElementTargetNames.size()) {
                    int indexOf = sharedElementSourceNames.indexOf(sharedElementTargetNames.get(i));
                    ArrayList arrayList7 = sharedElementTargetNames;
                    if (indexOf != -1) {
                        sharedElementSourceNames.set(indexOf, sharedElementSourceNames2.get(i));
                    }
                    i++;
                    sharedElementTargetNames = arrayList7;
                }
                ArrayList sharedElementTargetNames2 = operation2.getFragment().getSharedElementTargetNames();
                Fragment fragment = operation.getFragment();
                if (z2) {
                    fragment.getEnterTransitionCallback();
                    operation2.getFragment().getExitTransitionCallback();
                } else {
                    fragment.getExitTransitionCallback();
                    operation2.getFragment().getEnterTransitionCallback();
                }
                int i2 = 0;
                for (int size = sharedElementSourceNames.size(); i2 < size; size = size) {
                    arrayMap2.put((String) sharedElementSourceNames.get(i2), (String) sharedElementTargetNames2.get(i2));
                    i2++;
                }
                ArrayMap arrayMap3 = new ArrayMap();
                findNamedViews(arrayMap3, operation.getFragment().mView);
                arrayMap3.retainAll(sharedElementSourceNames);
                arrayMap2.retainAll(arrayMap3.keySet());
                final ArrayMap arrayMap4 = new ArrayMap();
                findNamedViews(arrayMap4, operation2.getFragment().mView);
                arrayMap4.retainAll(sharedElementTargetNames2);
                arrayMap4.retainAll(arrayMap2.values());
                FragmentTransition.retainValues(arrayMap2, arrayMap4);
                retainMatchingViews(arrayMap3, arrayMap2.keySet());
                retainMatchingViews(arrayMap4, arrayMap2.values());
                if (arrayMap2.isEmpty()) {
                    arrayList5.clear();
                    arrayList6.clear();
                    arrayMap = arrayMap2;
                    arrayList3 = arrayList6;
                    operation5 = operation7;
                    arrayList4 = arrayList5;
                    rect = rect2;
                    view3 = view5;
                    fragmentTransitionImpl = fragmentTransitionImpl2;
                    view6 = view7;
                    obj3 = null;
                    operation6 = operation8;
                } else {
                    FragmentTransition.callSharedElementStartEnd(operation2.getFragment(), operation.getFragment(), z2, arrayMap3, true);
                    arrayMap = arrayMap2;
                    ArrayList arrayList8 = arrayList6;
                    OneShotPreDrawListener.add(getContainer(), new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.6
                        @Override // java.lang.Runnable
                        public void run() {
                            FragmentTransition.callSharedElementStartEnd(operation2.getFragment(), operation.getFragment(), z, arrayMap4, false);
                        }
                    });
                    arrayList5.addAll(arrayMap3.values());
                    if (sharedElementSourceNames.isEmpty()) {
                        view6 = view7;
                    } else {
                        view6 = (View) arrayMap3.get((String) sharedElementSourceNames.get(0));
                        fragmentTransitionImpl2.setEpicenter(wrapTransitionInSet, view6);
                    }
                    arrayList3 = arrayList8;
                    arrayList3.addAll(arrayMap4.values());
                    if (!sharedElementTargetNames2.isEmpty() && (view4 = (View) arrayMap4.get((String) sharedElementTargetNames2.get(0))) != null) {
                        OneShotPreDrawListener.add(getContainer(), new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.7
                            @Override // java.lang.Runnable
                            public void run() {
                                fragmentTransitionImpl2.getBoundsOnScreen(view4, rect2);
                            }
                        });
                        z3 = true;
                    }
                    fragmentTransitionImpl2.setSharedElementTargets(wrapTransitionInSet, view5, arrayList5);
                    arrayList4 = arrayList5;
                    rect = rect2;
                    view3 = view5;
                    fragmentTransitionImpl = fragmentTransitionImpl2;
                    fragmentTransitionImpl2.scheduleRemoveTargets(wrapTransitionInSet, null, null, null, null, wrapTransitionInSet, arrayList3);
                    Boolean bool = Boolean.TRUE;
                    operation5 = operation;
                    hashMap.put(operation5, bool);
                    operation6 = operation2;
                    hashMap.put(operation6, bool);
                    obj3 = wrapTransitionInSet;
                }
            }
            operation7 = operation5;
            arrayList5 = arrayList4;
            rect2 = rect;
            view5 = view3;
            operation8 = operation6;
            arrayMap2 = arrayMap;
            z2 = z;
            arrayList6 = arrayList3;
            fragmentTransitionImpl2 = fragmentTransitionImpl;
        }
        View view8 = view6;
        ArrayMap arrayMap5 = arrayMap2;
        ArrayList arrayList9 = arrayList6;
        SpecialEffectsController.Operation operation9 = operation7;
        ArrayList arrayList10 = arrayList5;
        Rect rect3 = rect2;
        View view9 = view5;
        FragmentTransitionImpl fragmentTransitionImpl3 = fragmentTransitionImpl2;
        SpecialEffectsController.Operation operation10 = operation8;
        ArrayList arrayList11 = new ArrayList();
        Iterator it4 = list.iterator();
        Object obj4 = null;
        Object obj5 = null;
        while (it4.hasNext()) {
            TransitionInfo transitionInfo4 = (TransitionInfo) it4.next();
            if (transitionInfo4.isVisibilityUnchanged()) {
                hashMap.put(transitionInfo4.getOperation(), Boolean.FALSE);
                transitionInfo4.completeSpecialEffect();
            } else {
                Object cloneTransition = fragmentTransitionImpl3.cloneTransition(transitionInfo4.getTransition());
                SpecialEffectsController.Operation operation11 = transitionInfo4.getOperation();
                boolean z4 = obj3 != null && (operation11 == operation9 || operation11 == operation10);
                if (cloneTransition == null) {
                    if (!z4) {
                        hashMap.put(operation11, Boolean.FALSE);
                        transitionInfo4.completeSpecialEffect();
                    }
                    arrayList2 = arrayList9;
                    arrayList = arrayList10;
                    view = view9;
                    mergeTransitionsTogether = obj4;
                    operation3 = operation10;
                    view2 = view8;
                } else {
                    final ArrayList arrayList12 = new ArrayList();
                    Object obj6 = obj4;
                    captureTransitioningViews(arrayList12, operation11.getFragment().mView);
                    if (z4) {
                        if (operation11 == operation9) {
                            arrayList12.removeAll(arrayList10);
                        } else {
                            arrayList12.removeAll(arrayList9);
                        }
                    }
                    if (arrayList12.isEmpty()) {
                        fragmentTransitionImpl3.addTarget(cloneTransition, view9);
                        arrayList2 = arrayList9;
                        arrayList = arrayList10;
                        view = view9;
                        operation4 = operation11;
                        obj2 = obj5;
                        operation3 = operation10;
                        obj = obj6;
                    } else {
                        fragmentTransitionImpl3.addTargets(cloneTransition, arrayList12);
                        view = view9;
                        obj = obj6;
                        arrayList = arrayList10;
                        obj2 = obj5;
                        arrayList2 = arrayList9;
                        operation3 = operation10;
                        fragmentTransitionImpl3.scheduleRemoveTargets(cloneTransition, cloneTransition, arrayList12, null, null, null, null);
                        if (operation11.getFinalState() == SpecialEffectsController.Operation.State.GONE) {
                            operation4 = operation11;
                            list2.remove(operation4);
                            ArrayList arrayList13 = new ArrayList(arrayList12);
                            arrayList13.remove(operation4.getFragment().mView);
                            fragmentTransitionImpl3.scheduleHideFragmentView(cloneTransition, operation4.getFragment().mView, arrayList13);
                            OneShotPreDrawListener.add(getContainer(), new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.8
                                @Override // java.lang.Runnable
                                public void run() {
                                    FragmentTransition.setViewVisibility(arrayList12, 4);
                                }
                            });
                        } else {
                            operation4 = operation11;
                        }
                    }
                    if (operation4.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
                        arrayList11.addAll(arrayList12);
                        if (z3) {
                            fragmentTransitionImpl3.setEpicenter(cloneTransition, rect3);
                        }
                        view2 = view8;
                    } else {
                        view2 = view8;
                        fragmentTransitionImpl3.setEpicenter(cloneTransition, view2);
                    }
                    hashMap.put(operation4, Boolean.TRUE);
                    if (transitionInfo4.isOverlapAllowed()) {
                        obj5 = fragmentTransitionImpl3.mergeTransitionsTogether(obj2, cloneTransition, null);
                        mergeTransitionsTogether = obj;
                    } else {
                        mergeTransitionsTogether = fragmentTransitionImpl3.mergeTransitionsTogether(obj, cloneTransition, null);
                        obj5 = obj2;
                    }
                }
                operation10 = operation3;
                obj4 = mergeTransitionsTogether;
                view8 = view2;
                view9 = view;
                arrayList10 = arrayList;
                arrayList9 = arrayList2;
            }
        }
        ArrayList arrayList14 = arrayList9;
        ArrayList arrayList15 = arrayList10;
        SpecialEffectsController.Operation operation12 = operation10;
        Object mergeTransitionsInSequence = fragmentTransitionImpl3.mergeTransitionsInSequence(obj5, obj4, obj3);
        Iterator it5 = list.iterator();
        while (it5.hasNext()) {
            final TransitionInfo transitionInfo5 = (TransitionInfo) it5.next();
            if (!transitionInfo5.isVisibilityUnchanged()) {
                Object transition = transitionInfo5.getTransition();
                SpecialEffectsController.Operation operation13 = transitionInfo5.getOperation();
                boolean z5 = obj3 != null && (operation13 == operation9 || operation13 == operation12);
                if (transition != null || z5) {
                    if (ViewCompat.isLaidOut(getContainer())) {
                        fragmentTransitionImpl3.setListenerForTransitionEnd(transitionInfo5.getOperation().getFragment(), mergeTransitionsInSequence, transitionInfo5.getSignal(), new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.9
                            @Override // java.lang.Runnable
                            public void run() {
                                transitionInfo5.completeSpecialEffect();
                            }
                        });
                    } else {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Container " + getContainer() + " has not been laid out. Completing operation " + operation13);
                        }
                        transitionInfo5.completeSpecialEffect();
                    }
                }
            }
        }
        if (!ViewCompat.isLaidOut(getContainer())) {
            return hashMap;
        }
        FragmentTransition.setViewVisibility(arrayList11, 4);
        ArrayList prepareSetNameOverridesReordered = fragmentTransitionImpl3.prepareSetNameOverridesReordered(arrayList14);
        fragmentTransitionImpl3.beginDelayedTransition(getContainer(), mergeTransitionsInSequence);
        fragmentTransitionImpl3.setNameOverridesReordered(getContainer(), arrayList15, arrayList14, prepareSetNameOverridesReordered, arrayMap5);
        FragmentTransition.setViewVisibility(arrayList11, 0);
        fragmentTransitionImpl3.swapSharedElementTargets(obj3, arrayList15, arrayList14);
        return hashMap;
    }

    void applyContainerChanges(SpecialEffectsController.Operation operation) {
        operation.getFinalState().applyState(operation.getFragment().mView);
    }

    void captureTransitioningViews(ArrayList arrayList, View view) {
        if (!(view instanceof ViewGroup)) {
            if (arrayList.contains(view)) {
                return;
            }
            arrayList.add(view);
            return;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        if (ViewGroupCompat.isTransitionGroup(viewGroup)) {
            if (arrayList.contains(view)) {
                return;
            }
            arrayList.add(viewGroup);
            return;
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getVisibility() == 0) {
                captureTransitioningViews(arrayList, childAt);
            }
        }
    }

    @Override // androidx.fragment.app.SpecialEffectsController
    void executeOperations(List list, boolean z) {
        Iterator it = list.iterator();
        SpecialEffectsController.Operation operation = null;
        SpecialEffectsController.Operation operation2 = null;
        while (it.hasNext()) {
            SpecialEffectsController.Operation operation3 = (SpecialEffectsController.Operation) it.next();
            SpecialEffectsController.Operation.State from = SpecialEffectsController.Operation.State.from(operation3.getFragment().mView);
            int i = 10.$SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[operation3.getFinalState().ordinal()];
            if (i == 1 || i == 2 || i == 3) {
                if (from == SpecialEffectsController.Operation.State.VISIBLE && operation == null) {
                    operation = operation3;
                }
            } else if (i == 4 && from != SpecialEffectsController.Operation.State.VISIBLE) {
                operation2 = operation3;
            }
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList(list);
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            final SpecialEffectsController.Operation operation4 = (SpecialEffectsController.Operation) it2.next();
            CancellationSignal cancellationSignal = new CancellationSignal();
            operation4.markStartedSpecialEffect(cancellationSignal);
            arrayList.add(new AnimationInfo(operation4, cancellationSignal, z));
            CancellationSignal cancellationSignal2 = new CancellationSignal();
            operation4.markStartedSpecialEffect(cancellationSignal2);
            boolean z2 = false;
            if (z) {
                if (operation4 != operation) {
                    arrayList2.add(new TransitionInfo(operation4, cancellationSignal2, z, z2));
                    operation4.addCompletionListener(new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (arrayList3.contains(operation4)) {
                                arrayList3.remove(operation4);
                                DefaultSpecialEffectsController.this.applyContainerChanges(operation4);
                            }
                        }
                    });
                }
                z2 = true;
                arrayList2.add(new TransitionInfo(operation4, cancellationSignal2, z, z2));
                operation4.addCompletionListener(new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (arrayList3.contains(operation4)) {
                            arrayList3.remove(operation4);
                            DefaultSpecialEffectsController.this.applyContainerChanges(operation4);
                        }
                    }
                });
            } else {
                if (operation4 != operation2) {
                    arrayList2.add(new TransitionInfo(operation4, cancellationSignal2, z, z2));
                    operation4.addCompletionListener(new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (arrayList3.contains(operation4)) {
                                arrayList3.remove(operation4);
                                DefaultSpecialEffectsController.this.applyContainerChanges(operation4);
                            }
                        }
                    });
                }
                z2 = true;
                arrayList2.add(new TransitionInfo(operation4, cancellationSignal2, z, z2));
                operation4.addCompletionListener(new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (arrayList3.contains(operation4)) {
                            arrayList3.remove(operation4);
                            DefaultSpecialEffectsController.this.applyContainerChanges(operation4);
                        }
                    }
                });
            }
        }
        Map startTransitions = startTransitions(arrayList2, arrayList3, z, operation, operation2);
        startAnimations(arrayList, arrayList3, startTransitions.containsValue(Boolean.TRUE), startTransitions);
        Iterator it3 = arrayList3.iterator();
        while (it3.hasNext()) {
            applyContainerChanges((SpecialEffectsController.Operation) it3.next());
        }
        arrayList3.clear();
    }

    void findNamedViews(Map map, View view) {
        String transitionName = ViewCompat.getTransitionName(view);
        if (transitionName != null) {
            map.put(transitionName, view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt.getVisibility() == 0) {
                    findNamedViews(map, childAt);
                }
            }
        }
    }

    void retainMatchingViews(ArrayMap arrayMap, Collection collection) {
        Iterator it = arrayMap.entrySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(ViewCompat.getTransitionName((View) ((Map.Entry) it.next()).getValue()))) {
                it.remove();
            }
        }
    }
}
