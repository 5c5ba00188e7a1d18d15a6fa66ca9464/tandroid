package androidx.fragment.app;

import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.BackStackRecord;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: classes.dex */
public class FragmentTransition {
    private static final int[] INVERSE_OPS = {0, 3, 0, 1, 5, 4, 7, 6, 9, 8};
    private static final FragmentTransitionImpl PLATFORM_IMPL;
    private static final FragmentTransitionImpl SUPPORT_IMPL;

    static {
        PLATFORM_IMPL = Build.VERSION.SDK_INT >= 21 ? new FragmentTransitionCompat21() : null;
        SUPPORT_IMPL = resolveSupportImpl();
    }

    private static FragmentTransitionImpl resolveSupportImpl() {
        try {
            return (FragmentTransitionImpl) Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    public static void startTransitions(FragmentManagerImpl fragmentManagerImpl, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i, int i2, boolean z) {
        if (fragmentManagerImpl.mCurState < 1) {
            return;
        }
        SparseArray sparseArray = new SparseArray();
        for (int i3 = i; i3 < i2; i3++) {
            BackStackRecord backStackRecord = arrayList.get(i3);
            if (arrayList2.get(i3).booleanValue()) {
                calculatePopFragments(backStackRecord, sparseArray, z);
            } else {
                calculateFragments(backStackRecord, sparseArray, z);
            }
        }
        if (sparseArray.size() == 0) {
            return;
        }
        View view = new View(fragmentManagerImpl.mHost.getContext());
        int size = sparseArray.size();
        for (int i4 = 0; i4 < size; i4++) {
            int keyAt = sparseArray.keyAt(i4);
            ArrayMap<String, String> calculateNameOverrides = calculateNameOverrides(keyAt, arrayList, arrayList2, i, i2);
            FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition) sparseArray.valueAt(i4);
            if (z) {
                configureTransitionsReordered(fragmentManagerImpl, keyAt, fragmentContainerTransition, view, calculateNameOverrides);
            } else {
                configureTransitionsOrdered(fragmentManagerImpl, keyAt, fragmentContainerTransition, view, calculateNameOverrides);
            }
        }
    }

    private static ArrayMap<String, String> calculateNameOverrides(int i, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i2, int i3) {
        ArrayList<String> arrayList3;
        ArrayList<String> arrayList4;
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        for (int i4 = i3 - 1; i4 >= i2; i4--) {
            BackStackRecord backStackRecord = arrayList.get(i4);
            if (backStackRecord.interactsWith(i)) {
                boolean booleanValue = arrayList2.get(i4).booleanValue();
                ArrayList<String> arrayList5 = backStackRecord.mSharedElementSourceNames;
                if (arrayList5 != null) {
                    int size = arrayList5.size();
                    if (booleanValue) {
                        arrayList3 = backStackRecord.mSharedElementSourceNames;
                        arrayList4 = backStackRecord.mSharedElementTargetNames;
                    } else {
                        ArrayList<String> arrayList6 = backStackRecord.mSharedElementSourceNames;
                        arrayList3 = backStackRecord.mSharedElementTargetNames;
                        arrayList4 = arrayList6;
                    }
                    for (int i5 = 0; i5 < size; i5++) {
                        String str = arrayList4.get(i5);
                        String str2 = arrayList3.get(i5);
                        String remove = arrayMap.remove(str2);
                        if (remove != null) {
                            arrayMap.put(str, remove);
                        } else {
                            arrayMap.put(str, str2);
                        }
                    }
                }
            }
        }
        return arrayMap;
    }

    private static void configureTransitionsReordered(FragmentManagerImpl fragmentManagerImpl, int i, FragmentContainerTransition fragmentContainerTransition, View view, ArrayMap<String, String> arrayMap) {
        Fragment fragment;
        Fragment fragment2;
        FragmentTransitionImpl chooseImpl;
        Object obj;
        ViewGroup viewGroup = fragmentManagerImpl.mContainer.onHasView() ? (ViewGroup) fragmentManagerImpl.mContainer.onFindViewById(i) : null;
        if (viewGroup == null || (chooseImpl = chooseImpl((fragment2 = fragmentContainerTransition.firstOut), (fragment = fragmentContainerTransition.lastIn))) == null) {
            return;
        }
        boolean z = fragmentContainerTransition.lastInIsPop;
        boolean z2 = fragmentContainerTransition.firstOutIsPop;
        ArrayList<View> arrayList = new ArrayList<>();
        ArrayList<View> arrayList2 = new ArrayList<>();
        Object enterTransition = getEnterTransition(chooseImpl, fragment, z);
        Object exitTransition = getExitTransition(chooseImpl, fragment2, z2);
        Object configureSharedElementsReordered = configureSharedElementsReordered(chooseImpl, viewGroup, view, arrayMap, fragmentContainerTransition, arrayList2, arrayList, enterTransition, exitTransition);
        if (enterTransition == null && configureSharedElementsReordered == null) {
            obj = exitTransition;
            if (obj == null) {
                return;
            }
        } else {
            obj = exitTransition;
        }
        ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(chooseImpl, obj, fragment2, arrayList2, view);
        ArrayList<View> configureEnteringExitingViews2 = configureEnteringExitingViews(chooseImpl, enterTransition, fragment, arrayList, view);
        setViewVisibility(configureEnteringExitingViews2, 4);
        Object mergeTransitions = mergeTransitions(chooseImpl, enterTransition, obj, configureSharedElementsReordered, fragment, z);
        if (mergeTransitions == null) {
            return;
        }
        replaceHide(chooseImpl, obj, fragment2, configureEnteringExitingViews);
        ArrayList<String> prepareSetNameOverridesReordered = chooseImpl.prepareSetNameOverridesReordered(arrayList);
        chooseImpl.scheduleRemoveTargets(mergeTransitions, enterTransition, configureEnteringExitingViews2, obj, configureEnteringExitingViews, configureSharedElementsReordered, arrayList);
        chooseImpl.beginDelayedTransition(viewGroup, mergeTransitions);
        chooseImpl.setNameOverridesReordered(viewGroup, arrayList2, arrayList, prepareSetNameOverridesReordered, arrayMap);
        setViewVisibility(configureEnteringExitingViews2, 0);
        chooseImpl.swapSharedElementTargets(configureSharedElementsReordered, arrayList2, arrayList);
    }

    private static void replaceHide(FragmentTransitionImpl fragmentTransitionImpl, Object obj, Fragment fragment, ArrayList<View> arrayList) {
        if (fragment == null || obj == null || !fragment.mAdded || !fragment.mHidden || !fragment.mHiddenChanged) {
            return;
        }
        fragment.setHideReplaced(true);
        fragmentTransitionImpl.scheduleHideFragmentView(obj, fragment.getView(), arrayList);
        OneShotPreDrawListener.add(fragment.mContainer, new AnonymousClass1(arrayList));
    }

    /* renamed from: androidx.fragment.app.FragmentTransition$1 */
    /* loaded from: classes.dex */
    public static class AnonymousClass1 implements Runnable {
        final /* synthetic */ ArrayList val$exitingViews;

        AnonymousClass1(ArrayList arrayList) {
            this.val$exitingViews = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentTransition.setViewVisibility(this.val$exitingViews, 4);
        }
    }

    private static void configureTransitionsOrdered(FragmentManagerImpl fragmentManagerImpl, int i, FragmentContainerTransition fragmentContainerTransition, View view, ArrayMap<String, String> arrayMap) {
        Fragment fragment;
        Fragment fragment2;
        FragmentTransitionImpl chooseImpl;
        Object obj;
        ViewGroup viewGroup = fragmentManagerImpl.mContainer.onHasView() ? (ViewGroup) fragmentManagerImpl.mContainer.onFindViewById(i) : null;
        if (viewGroup == null || (chooseImpl = chooseImpl((fragment2 = fragmentContainerTransition.firstOut), (fragment = fragmentContainerTransition.lastIn))) == null) {
            return;
        }
        boolean z = fragmentContainerTransition.lastInIsPop;
        boolean z2 = fragmentContainerTransition.firstOutIsPop;
        Object enterTransition = getEnterTransition(chooseImpl, fragment, z);
        Object exitTransition = getExitTransition(chooseImpl, fragment2, z2);
        ArrayList arrayList = new ArrayList();
        ArrayList<View> arrayList2 = new ArrayList<>();
        Object configureSharedElementsOrdered = configureSharedElementsOrdered(chooseImpl, viewGroup, view, arrayMap, fragmentContainerTransition, arrayList, arrayList2, enterTransition, exitTransition);
        if (enterTransition == null && configureSharedElementsOrdered == null) {
            obj = exitTransition;
            if (obj == null) {
                return;
            }
        } else {
            obj = exitTransition;
        }
        ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(chooseImpl, obj, fragment2, arrayList, view);
        Object obj2 = (configureEnteringExitingViews == null || configureEnteringExitingViews.isEmpty()) ? null : obj;
        chooseImpl.addTarget(enterTransition, view);
        Object mergeTransitions = mergeTransitions(chooseImpl, enterTransition, obj2, configureSharedElementsOrdered, fragment, fragmentContainerTransition.lastInIsPop);
        if (mergeTransitions == null) {
            return;
        }
        ArrayList<View> arrayList3 = new ArrayList<>();
        chooseImpl.scheduleRemoveTargets(mergeTransitions, enterTransition, arrayList3, obj2, configureEnteringExitingViews, configureSharedElementsOrdered, arrayList2);
        scheduleTargetChange(chooseImpl, viewGroup, fragment, view, arrayList2, enterTransition, arrayList3, obj2, configureEnteringExitingViews);
        chooseImpl.setNameOverridesOrdered(viewGroup, arrayList2, arrayMap);
        chooseImpl.beginDelayedTransition(viewGroup, mergeTransitions);
        chooseImpl.scheduleNameReset(viewGroup, arrayList2, arrayMap);
    }

    /* renamed from: androidx.fragment.app.FragmentTransition$2 */
    /* loaded from: classes.dex */
    public static class AnonymousClass2 implements Runnable {
        final /* synthetic */ Object val$enterTransition;
        final /* synthetic */ ArrayList val$enteringViews;
        final /* synthetic */ Object val$exitTransition;
        final /* synthetic */ ArrayList val$exitingViews;
        final /* synthetic */ FragmentTransitionImpl val$impl;
        final /* synthetic */ Fragment val$inFragment;
        final /* synthetic */ View val$nonExistentView;
        final /* synthetic */ ArrayList val$sharedElementsIn;

        AnonymousClass2(Object obj, FragmentTransitionImpl fragmentTransitionImpl, View view, Fragment fragment, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, Object obj2) {
            this.val$enterTransition = obj;
            this.val$impl = fragmentTransitionImpl;
            this.val$nonExistentView = view;
            this.val$inFragment = fragment;
            this.val$sharedElementsIn = arrayList;
            this.val$enteringViews = arrayList2;
            this.val$exitingViews = arrayList3;
            this.val$exitTransition = obj2;
        }

        @Override // java.lang.Runnable
        public void run() {
            Object obj = this.val$enterTransition;
            if (obj != null) {
                this.val$impl.removeTarget(obj, this.val$nonExistentView);
                this.val$enteringViews.addAll(FragmentTransition.configureEnteringExitingViews(this.val$impl, this.val$enterTransition, this.val$inFragment, this.val$sharedElementsIn, this.val$nonExistentView));
            }
            if (this.val$exitingViews != null) {
                if (this.val$exitTransition != null) {
                    ArrayList<View> arrayList = new ArrayList<>();
                    arrayList.add(this.val$nonExistentView);
                    this.val$impl.replaceTargets(this.val$exitTransition, this.val$exitingViews, arrayList);
                }
                this.val$exitingViews.clear();
                this.val$exitingViews.add(this.val$nonExistentView);
            }
        }
    }

    private static void scheduleTargetChange(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, Fragment fragment, View view, ArrayList<View> arrayList, Object obj, ArrayList<View> arrayList2, Object obj2, ArrayList<View> arrayList3) {
        OneShotPreDrawListener.add(viewGroup, new AnonymousClass2(obj, fragmentTransitionImpl, view, fragment, arrayList, arrayList2, arrayList3, obj2));
    }

    private static FragmentTransitionImpl chooseImpl(Fragment fragment, Fragment fragment2) {
        ArrayList arrayList = new ArrayList();
        if (fragment != null) {
            Object exitTransition = fragment.getExitTransition();
            if (exitTransition != null) {
                arrayList.add(exitTransition);
            }
            Object returnTransition = fragment.getReturnTransition();
            if (returnTransition != null) {
                arrayList.add(returnTransition);
            }
            Object sharedElementReturnTransition = fragment.getSharedElementReturnTransition();
            if (sharedElementReturnTransition != null) {
                arrayList.add(sharedElementReturnTransition);
            }
        }
        if (fragment2 != null) {
            Object enterTransition = fragment2.getEnterTransition();
            if (enterTransition != null) {
                arrayList.add(enterTransition);
            }
            Object reenterTransition = fragment2.getReenterTransition();
            if (reenterTransition != null) {
                arrayList.add(reenterTransition);
            }
            Object sharedElementEnterTransition = fragment2.getSharedElementEnterTransition();
            if (sharedElementEnterTransition != null) {
                arrayList.add(sharedElementEnterTransition);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        FragmentTransitionImpl fragmentTransitionImpl = PLATFORM_IMPL;
        if (fragmentTransitionImpl != null && canHandleAll(fragmentTransitionImpl, arrayList)) {
            return fragmentTransitionImpl;
        }
        FragmentTransitionImpl fragmentTransitionImpl2 = SUPPORT_IMPL;
        if (fragmentTransitionImpl2 != null && canHandleAll(fragmentTransitionImpl2, arrayList)) {
            return fragmentTransitionImpl2;
        }
        if (fragmentTransitionImpl != null || fragmentTransitionImpl2 != null) {
            throw new IllegalArgumentException("Invalid Transition types");
        }
        return null;
    }

    private static boolean canHandleAll(FragmentTransitionImpl fragmentTransitionImpl, List<Object> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (!fragmentTransitionImpl.canHandle(list.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static Object getSharedElementTransition(FragmentTransitionImpl fragmentTransitionImpl, Fragment fragment, Fragment fragment2, boolean z) {
        Object obj;
        if (fragment == null || fragment2 == null) {
            return null;
        }
        if (z) {
            obj = fragment2.getSharedElementReturnTransition();
        } else {
            obj = fragment.getSharedElementEnterTransition();
        }
        return fragmentTransitionImpl.wrapTransitionInSet(fragmentTransitionImpl.cloneTransition(obj));
    }

    private static Object getEnterTransition(FragmentTransitionImpl fragmentTransitionImpl, Fragment fragment, boolean z) {
        Object obj;
        if (fragment == null) {
            return null;
        }
        if (z) {
            obj = fragment.getReenterTransition();
        } else {
            obj = fragment.getEnterTransition();
        }
        return fragmentTransitionImpl.cloneTransition(obj);
    }

    private static Object getExitTransition(FragmentTransitionImpl fragmentTransitionImpl, Fragment fragment, boolean z) {
        Object obj;
        if (fragment == null) {
            return null;
        }
        if (z) {
            obj = fragment.getReturnTransition();
        } else {
            obj = fragment.getExitTransition();
        }
        return fragmentTransitionImpl.cloneTransition(obj);
    }

    private static Object configureSharedElementsReordered(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, View view, ArrayMap<String, String> arrayMap, FragmentContainerTransition fragmentContainerTransition, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object obj, Object obj2) {
        Object obj3;
        Rect rect;
        View view2;
        Fragment fragment = fragmentContainerTransition.lastIn;
        Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment != null) {
            fragment.getView().setVisibility(0);
        }
        if (fragment == null || fragment2 == null) {
            return null;
        }
        boolean z = fragmentContainerTransition.lastInIsPop;
        Object sharedElementTransition = arrayMap.isEmpty() ? null : getSharedElementTransition(fragmentTransitionImpl, fragment, fragment2, z);
        ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(fragmentTransitionImpl, arrayMap, sharedElementTransition, fragmentContainerTransition);
        ArrayMap<String, View> captureInSharedElements = captureInSharedElements(fragmentTransitionImpl, arrayMap, sharedElementTransition, fragmentContainerTransition);
        if (arrayMap.isEmpty()) {
            if (captureOutSharedElements != null) {
                captureOutSharedElements.clear();
            }
            if (captureInSharedElements != null) {
                captureInSharedElements.clear();
            }
            obj3 = null;
        } else {
            addSharedElementsWithMatchingNames(arrayList, captureOutSharedElements, arrayMap.keySet());
            addSharedElementsWithMatchingNames(arrayList2, captureInSharedElements, arrayMap.values());
            obj3 = sharedElementTransition;
        }
        if (obj == null && obj2 == null && obj3 == null) {
            return null;
        }
        callSharedElementStartEnd(fragment, fragment2, z, captureOutSharedElements, true);
        if (obj3 != null) {
            arrayList2.add(view);
            fragmentTransitionImpl.setSharedElementTargets(obj3, view, arrayList);
            setOutEpicenter(fragmentTransitionImpl, obj3, obj2, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
            Rect rect2 = new Rect();
            View inEpicenterView = getInEpicenterView(captureInSharedElements, fragmentContainerTransition, obj, z);
            if (inEpicenterView != null) {
                fragmentTransitionImpl.setEpicenter(obj, rect2);
            }
            rect = rect2;
            view2 = inEpicenterView;
        } else {
            view2 = null;
            rect = null;
        }
        OneShotPreDrawListener.add(viewGroup, new AnonymousClass3(fragment, fragment2, z, captureInSharedElements, view2, fragmentTransitionImpl, rect));
        return obj3;
    }

    /* renamed from: androidx.fragment.app.FragmentTransition$3 */
    /* loaded from: classes.dex */
    public static class AnonymousClass3 implements Runnable {
        final /* synthetic */ Rect val$epicenter;
        final /* synthetic */ View val$epicenterView;
        final /* synthetic */ FragmentTransitionImpl val$impl;
        final /* synthetic */ Fragment val$inFragment;
        final /* synthetic */ boolean val$inIsPop;
        final /* synthetic */ ArrayMap val$inSharedElements;
        final /* synthetic */ Fragment val$outFragment;

        AnonymousClass3(Fragment fragment, Fragment fragment2, boolean z, ArrayMap arrayMap, View view, FragmentTransitionImpl fragmentTransitionImpl, Rect rect) {
            this.val$inFragment = fragment;
            this.val$outFragment = fragment2;
            this.val$inIsPop = z;
            this.val$inSharedElements = arrayMap;
            this.val$epicenterView = view;
            this.val$impl = fragmentTransitionImpl;
            this.val$epicenter = rect;
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentTransition.callSharedElementStartEnd(this.val$inFragment, this.val$outFragment, this.val$inIsPop, this.val$inSharedElements, false);
            View view = this.val$epicenterView;
            if (view != null) {
                this.val$impl.getBoundsOnScreen(view, this.val$epicenter);
            }
        }
    }

    private static void addSharedElementsWithMatchingNames(ArrayList<View> arrayList, ArrayMap<String, View> arrayMap, Collection<String> collection) {
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            View valueAt = arrayMap.valueAt(size);
            if (collection.contains(ViewCompat.getTransitionName(valueAt))) {
                arrayList.add(valueAt);
            }
        }
    }

    private static Object configureSharedElementsOrdered(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, View view, ArrayMap<String, String> arrayMap, FragmentContainerTransition fragmentContainerTransition, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object obj, Object obj2) {
        ArrayMap<String, String> arrayMap2;
        Object obj3;
        Object obj4;
        Rect rect;
        Fragment fragment = fragmentContainerTransition.lastIn;
        Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment == null || fragment2 == null) {
            return null;
        }
        boolean z = fragmentContainerTransition.lastInIsPop;
        if (arrayMap.isEmpty()) {
            arrayMap2 = arrayMap;
            obj3 = null;
        } else {
            obj3 = getSharedElementTransition(fragmentTransitionImpl, fragment, fragment2, z);
            arrayMap2 = arrayMap;
        }
        ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(fragmentTransitionImpl, arrayMap2, obj3, fragmentContainerTransition);
        if (arrayMap.isEmpty()) {
            obj4 = null;
        } else {
            arrayList.addAll(captureOutSharedElements.values());
            obj4 = obj3;
        }
        if (obj == null && obj2 == null && obj4 == null) {
            return null;
        }
        callSharedElementStartEnd(fragment, fragment2, z, captureOutSharedElements, true);
        if (obj4 != null) {
            rect = new Rect();
            fragmentTransitionImpl.setSharedElementTargets(obj4, view, arrayList);
            setOutEpicenter(fragmentTransitionImpl, obj4, obj2, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
            if (obj != null) {
                fragmentTransitionImpl.setEpicenter(obj, rect);
            }
        } else {
            rect = null;
        }
        OneShotPreDrawListener.add(viewGroup, new AnonymousClass4(fragmentTransitionImpl, arrayMap, obj4, fragmentContainerTransition, arrayList2, view, fragment, fragment2, z, arrayList, obj, rect));
        return obj4;
    }

    /* renamed from: androidx.fragment.app.FragmentTransition$4 */
    /* loaded from: classes.dex */
    public static class AnonymousClass4 implements Runnable {
        final /* synthetic */ Object val$enterTransition;
        final /* synthetic */ Object val$finalSharedElementTransition;
        final /* synthetic */ FragmentContainerTransition val$fragments;
        final /* synthetic */ FragmentTransitionImpl val$impl;
        final /* synthetic */ Rect val$inEpicenter;
        final /* synthetic */ Fragment val$inFragment;
        final /* synthetic */ boolean val$inIsPop;
        final /* synthetic */ ArrayMap val$nameOverrides;
        final /* synthetic */ View val$nonExistentView;
        final /* synthetic */ Fragment val$outFragment;
        final /* synthetic */ ArrayList val$sharedElementsIn;
        final /* synthetic */ ArrayList val$sharedElementsOut;

        AnonymousClass4(FragmentTransitionImpl fragmentTransitionImpl, ArrayMap arrayMap, Object obj, FragmentContainerTransition fragmentContainerTransition, ArrayList arrayList, View view, Fragment fragment, Fragment fragment2, boolean z, ArrayList arrayList2, Object obj2, Rect rect) {
            this.val$impl = fragmentTransitionImpl;
            this.val$nameOverrides = arrayMap;
            this.val$finalSharedElementTransition = obj;
            this.val$fragments = fragmentContainerTransition;
            this.val$sharedElementsIn = arrayList;
            this.val$nonExistentView = view;
            this.val$inFragment = fragment;
            this.val$outFragment = fragment2;
            this.val$inIsPop = z;
            this.val$sharedElementsOut = arrayList2;
            this.val$enterTransition = obj2;
            this.val$inEpicenter = rect;
        }

        @Override // java.lang.Runnable
        public void run() {
            ArrayMap<String, View> captureInSharedElements = FragmentTransition.captureInSharedElements(this.val$impl, this.val$nameOverrides, this.val$finalSharedElementTransition, this.val$fragments);
            if (captureInSharedElements != null) {
                this.val$sharedElementsIn.addAll(captureInSharedElements.values());
                this.val$sharedElementsIn.add(this.val$nonExistentView);
            }
            FragmentTransition.callSharedElementStartEnd(this.val$inFragment, this.val$outFragment, this.val$inIsPop, captureInSharedElements, false);
            Object obj = this.val$finalSharedElementTransition;
            if (obj != null) {
                this.val$impl.swapSharedElementTargets(obj, this.val$sharedElementsOut, this.val$sharedElementsIn);
                View inEpicenterView = FragmentTransition.getInEpicenterView(captureInSharedElements, this.val$fragments, this.val$enterTransition, this.val$inIsPop);
                if (inEpicenterView == null) {
                    return;
                }
                this.val$impl.getBoundsOnScreen(inEpicenterView, this.val$inEpicenter);
            }
        }
    }

    private static ArrayMap<String, View> captureOutSharedElements(FragmentTransitionImpl fragmentTransitionImpl, ArrayMap<String, String> arrayMap, Object obj, FragmentContainerTransition fragmentContainerTransition) {
        ArrayList<String> arrayList;
        if (arrayMap.isEmpty() || obj == null) {
            arrayMap.clear();
            return null;
        }
        Fragment fragment = fragmentContainerTransition.firstOut;
        ArrayMap<String, View> arrayMap2 = new ArrayMap<>();
        fragmentTransitionImpl.findNamedViews(arrayMap2, fragment.getView());
        BackStackRecord backStackRecord = fragmentContainerTransition.firstOutTransaction;
        if (fragmentContainerTransition.firstOutIsPop) {
            fragment.getEnterTransitionCallback();
            arrayList = backStackRecord.mSharedElementTargetNames;
        } else {
            fragment.getExitTransitionCallback();
            arrayList = backStackRecord.mSharedElementSourceNames;
        }
        arrayMap2.retainAll(arrayList);
        arrayMap.retainAll(arrayMap2.keySet());
        return arrayMap2;
    }

    static ArrayMap<String, View> captureInSharedElements(FragmentTransitionImpl fragmentTransitionImpl, ArrayMap<String, String> arrayMap, Object obj, FragmentContainerTransition fragmentContainerTransition) {
        ArrayList<String> arrayList;
        Fragment fragment = fragmentContainerTransition.lastIn;
        View view = fragment.getView();
        if (arrayMap.isEmpty() || obj == null || view == null) {
            arrayMap.clear();
            return null;
        }
        ArrayMap<String, View> arrayMap2 = new ArrayMap<>();
        fragmentTransitionImpl.findNamedViews(arrayMap2, view);
        BackStackRecord backStackRecord = fragmentContainerTransition.lastInTransaction;
        if (fragmentContainerTransition.lastInIsPop) {
            fragment.getExitTransitionCallback();
            arrayList = backStackRecord.mSharedElementSourceNames;
        } else {
            fragment.getEnterTransitionCallback();
            arrayList = backStackRecord.mSharedElementTargetNames;
        }
        if (arrayList != null) {
            arrayMap2.retainAll(arrayList);
            arrayMap2.retainAll(arrayMap.values());
        }
        retainValues(arrayMap, arrayMap2);
        return arrayMap2;
    }

    static View getInEpicenterView(ArrayMap<String, View> arrayMap, FragmentContainerTransition fragmentContainerTransition, Object obj, boolean z) {
        ArrayList<String> arrayList;
        String str;
        BackStackRecord backStackRecord = fragmentContainerTransition.lastInTransaction;
        if (obj == null || arrayMap == null || (arrayList = backStackRecord.mSharedElementSourceNames) == null || arrayList.isEmpty()) {
            return null;
        }
        if (z) {
            str = backStackRecord.mSharedElementSourceNames.get(0);
        } else {
            str = backStackRecord.mSharedElementTargetNames.get(0);
        }
        return arrayMap.get(str);
    }

    private static void setOutEpicenter(FragmentTransitionImpl fragmentTransitionImpl, Object obj, Object obj2, ArrayMap<String, View> arrayMap, boolean z, BackStackRecord backStackRecord) {
        String str;
        ArrayList<String> arrayList = backStackRecord.mSharedElementSourceNames;
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        if (z) {
            str = backStackRecord.mSharedElementTargetNames.get(0);
        } else {
            str = backStackRecord.mSharedElementSourceNames.get(0);
        }
        View view = arrayMap.get(str);
        fragmentTransitionImpl.setEpicenter(obj, view);
        if (obj2 == null) {
            return;
        }
        fragmentTransitionImpl.setEpicenter(obj2, view);
    }

    private static void retainValues(ArrayMap<String, String> arrayMap, ArrayMap<String, View> arrayMap2) {
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            if (!arrayMap2.containsKey(arrayMap.valueAt(size))) {
                arrayMap.removeAt(size);
            }
        }
    }

    static void callSharedElementStartEnd(Fragment fragment, Fragment fragment2, boolean z, ArrayMap<String, View> arrayMap, boolean z2) {
        if (z) {
            fragment2.getEnterTransitionCallback();
        } else {
            fragment.getEnterTransitionCallback();
        }
    }

    static ArrayList<View> configureEnteringExitingViews(FragmentTransitionImpl fragmentTransitionImpl, Object obj, Fragment fragment, ArrayList<View> arrayList, View view) {
        if (obj != null) {
            ArrayList<View> arrayList2 = new ArrayList<>();
            View view2 = fragment.getView();
            if (view2 != null) {
                fragmentTransitionImpl.captureTransitioningViews(arrayList2, view2);
            }
            if (arrayList != null) {
                arrayList2.removeAll(arrayList);
            }
            if (arrayList2.isEmpty()) {
                return arrayList2;
            }
            arrayList2.add(view);
            fragmentTransitionImpl.addTargets(obj, arrayList2);
            return arrayList2;
        }
        return null;
    }

    static void setViewVisibility(ArrayList<View> arrayList, int i) {
        if (arrayList == null) {
            return;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            arrayList.get(size).setVisibility(i);
        }
    }

    private static Object mergeTransitions(FragmentTransitionImpl fragmentTransitionImpl, Object obj, Object obj2, Object obj3, Fragment fragment, boolean z) {
        boolean z2;
        if (obj == null || obj2 == null || fragment == null) {
            z2 = true;
        } else if (z) {
            z2 = fragment.getAllowReturnTransitionOverlap();
        } else {
            z2 = fragment.getAllowEnterTransitionOverlap();
        }
        if (z2) {
            return fragmentTransitionImpl.mergeTransitionsTogether(obj2, obj, obj3);
        }
        return fragmentTransitionImpl.mergeTransitionsInSequence(obj2, obj, obj3);
    }

    public static void calculateFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean z) {
        int size = backStackRecord.mOps.size();
        for (int i = 0; i < size; i++) {
            addToFirstInLastOut(backStackRecord, backStackRecord.mOps.get(i), sparseArray, false, z);
        }
    }

    public static void calculatePopFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean z) {
        if (!backStackRecord.mManager.mContainer.onHasView()) {
            return;
        }
        for (int size = backStackRecord.mOps.size() - 1; size >= 0; size--) {
            addToFirstInLastOut(backStackRecord, backStackRecord.mOps.get(size), sparseArray, true, z);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0041, code lost:
        if (r10.mAdded != false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0076, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0092, code lost:
        if (r10.mHidden == false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0094, code lost:
        r1 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00b0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x00d5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x00e7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:98:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void addToFirstInLastOut(BackStackRecord backStackRecord, BackStackRecord.Op op, SparseArray<FragmentContainerTransition> sparseArray, boolean z, boolean z2) {
        int i;
        boolean z3;
        boolean z4;
        boolean z5;
        FragmentContainerTransition fragmentContainerTransition;
        FragmentManagerImpl fragmentManagerImpl;
        boolean z6;
        Fragment fragment = op.fragment;
        if (fragment == null || (i = fragment.mContainerId) == 0) {
            return;
        }
        int i2 = z ? INVERSE_OPS[op.cmd] : op.cmd;
        boolean z7 = false;
        if (i2 != 1) {
            if (i2 != 3) {
                if (i2 == 4) {
                    boolean z8 = !z2 ? false : false;
                    z3 = z8;
                    z5 = false;
                    z4 = true;
                    FragmentContainerTransition fragmentContainerTransition2 = sparseArray.get(i);
                    if (z7) {
                    }
                    fragmentContainerTransition = fragmentContainerTransition2;
                    if (!z2) {
                    }
                    if (z3) {
                    }
                    if (!z2) {
                    }
                } else if (i2 != 5) {
                    if (i2 != 6) {
                        if (i2 != 7) {
                            z5 = false;
                            z4 = false;
                            z3 = false;
                            FragmentContainerTransition fragmentContainerTransition22 = sparseArray.get(i);
                            if (z7) {
                                fragmentContainerTransition22 = ensureContainer(fragmentContainerTransition22, sparseArray, i);
                                fragmentContainerTransition22.lastIn = fragment;
                                fragmentContainerTransition22.lastInIsPop = z;
                                fragmentContainerTransition22.lastInTransaction = backStackRecord;
                            }
                            fragmentContainerTransition = fragmentContainerTransition22;
                            if (!z2 && z5) {
                                if (fragmentContainerTransition != null && fragmentContainerTransition.firstOut == fragment) {
                                    fragmentContainerTransition.firstOut = null;
                                }
                                fragmentManagerImpl = backStackRecord.mManager;
                                if (fragment.mState < 1 && fragmentManagerImpl.mCurState >= 1 && !backStackRecord.mReorderingAllowed) {
                                    fragmentManagerImpl.makeActive(fragment);
                                    fragmentManagerImpl.moveToState(fragment, 1, 0, 0, false);
                                }
                            }
                            if (z3 && (fragmentContainerTransition == null || fragmentContainerTransition.firstOut == null)) {
                                fragmentContainerTransition = ensureContainer(fragmentContainerTransition, sparseArray, i);
                                fragmentContainerTransition.firstOut = fragment;
                                fragmentContainerTransition.firstOutIsPop = z;
                                fragmentContainerTransition.firstOutTransaction = backStackRecord;
                            }
                            if (!z2 || !z4 || fragmentContainerTransition == null || fragmentContainerTransition.lastIn != fragment) {
                                return;
                            }
                            fragmentContainerTransition.lastIn = null;
                            return;
                        }
                    }
                } else if (z2) {
                    if (fragment.mHiddenChanged) {
                        if (!fragment.mHidden) {
                        }
                    }
                    z6 = false;
                    z7 = z6;
                    z5 = true;
                    z4 = false;
                    z3 = false;
                    FragmentContainerTransition fragmentContainerTransition222 = sparseArray.get(i);
                    if (z7) {
                    }
                    fragmentContainerTransition = fragmentContainerTransition222;
                    if (!z2) {
                        if (fragmentContainerTransition != null) {
                            fragmentContainerTransition.firstOut = null;
                        }
                        fragmentManagerImpl = backStackRecord.mManager;
                        if (fragment.mState < 1) {
                            fragmentManagerImpl.makeActive(fragment);
                            fragmentManagerImpl.moveToState(fragment, 1, 0, 0, false);
                        }
                    }
                    if (z3) {
                        fragmentContainerTransition = ensureContainer(fragmentContainerTransition, sparseArray, i);
                        fragmentContainerTransition.firstOut = fragment;
                        fragmentContainerTransition.firstOutIsPop = z;
                        fragmentContainerTransition.firstOutTransaction = backStackRecord;
                    }
                    if (!z2) {
                        return;
                    }
                    return;
                } else {
                    z6 = fragment.mHidden;
                    z7 = z6;
                    z5 = true;
                    z4 = false;
                    z3 = false;
                    FragmentContainerTransition fragmentContainerTransition2222 = sparseArray.get(i);
                    if (z7) {
                    }
                    fragmentContainerTransition = fragmentContainerTransition2222;
                    if (!z2) {
                    }
                    if (z3) {
                    }
                    if (!z2) {
                    }
                }
            }
            if (!z2) {
            }
            z3 = z8;
            z5 = false;
            z4 = true;
            FragmentContainerTransition fragmentContainerTransition22222 = sparseArray.get(i);
            if (z7) {
            }
            fragmentContainerTransition = fragmentContainerTransition22222;
            if (!z2) {
            }
            if (z3) {
            }
            if (!z2) {
            }
        }
        if (z2) {
            z6 = fragment.mIsNewlyAdded;
            z7 = z6;
            z5 = true;
            z4 = false;
            z3 = false;
            FragmentContainerTransition fragmentContainerTransition222222 = sparseArray.get(i);
            if (z7) {
            }
            fragmentContainerTransition = fragmentContainerTransition222222;
            if (!z2) {
            }
            if (z3) {
            }
            if (!z2) {
            }
        } else {
            if (!fragment.mAdded) {
            }
            z6 = false;
            z7 = z6;
            z5 = true;
            z4 = false;
            z3 = false;
            FragmentContainerTransition fragmentContainerTransition2222222 = sparseArray.get(i);
            if (z7) {
            }
            fragmentContainerTransition = fragmentContainerTransition2222222;
            if (!z2) {
            }
            if (z3) {
            }
            if (!z2) {
            }
        }
    }

    private static FragmentContainerTransition ensureContainer(FragmentContainerTransition fragmentContainerTransition, SparseArray<FragmentContainerTransition> sparseArray, int i) {
        if (fragmentContainerTransition == null) {
            FragmentContainerTransition fragmentContainerTransition2 = new FragmentContainerTransition();
            sparseArray.put(i, fragmentContainerTransition2);
            return fragmentContainerTransition2;
        }
        return fragmentContainerTransition;
    }

    /* loaded from: classes.dex */
    public static class FragmentContainerTransition {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;

        FragmentContainerTransition() {
        }
    }
}
