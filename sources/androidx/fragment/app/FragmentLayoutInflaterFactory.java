package androidx.fragment.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.R$styleable;

/* loaded from: classes.dex */
class FragmentLayoutInflaterFactory implements LayoutInflater.Factory2 {
    final FragmentManager mFragmentManager;

    FragmentLayoutInflaterFactory(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x016a  */
    @Override // android.view.LayoutInflater.Factory2
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        final FragmentStateManager createOrGetFragmentStateManager;
        StringBuilder sb;
        String str2;
        View view2;
        if (FragmentContainerView.class.getName().equals(str)) {
            return new FragmentContainerView(context, attributeSet, this.mFragmentManager);
        }
        if (!"fragment".equals(str)) {
            return null;
        }
        String attributeValue = attributeSet.getAttributeValue(null, "class");
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Fragment);
        if (attributeValue == null) {
            attributeValue = obtainStyledAttributes.getString(R$styleable.Fragment_android_name);
        }
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.Fragment_android_id, -1);
        String string = obtainStyledAttributes.getString(R$styleable.Fragment_android_tag);
        obtainStyledAttributes.recycle();
        if (attributeValue == null || !FragmentFactory.isFragmentClass(context.getClassLoader(), attributeValue)) {
            return null;
        }
        int id = view != null ? view.getId() : 0;
        if (id == -1 && resourceId == -1 && string == null) {
            throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + attributeValue);
        }
        Fragment findFragmentById = resourceId != -1 ? this.mFragmentManager.findFragmentById(resourceId) : null;
        if (findFragmentById == null && string != null) {
            findFragmentById = this.mFragmentManager.findFragmentByTag(string);
        }
        if (findFragmentById == null && id != -1) {
            findFragmentById = this.mFragmentManager.findFragmentById(id);
        }
        if (findFragmentById == null) {
            findFragmentById = this.mFragmentManager.getFragmentFactory().instantiate(context.getClassLoader(), attributeValue);
            findFragmentById.mFromLayout = true;
            findFragmentById.mFragmentId = resourceId != 0 ? resourceId : id;
            findFragmentById.mContainerId = id;
            findFragmentById.mTag = string;
            findFragmentById.mInLayout = true;
            FragmentManager fragmentManager = this.mFragmentManager;
            findFragmentById.mFragmentManager = fragmentManager;
            findFragmentById.mHost = fragmentManager.getHost();
            findFragmentById.onInflate(this.mFragmentManager.getHost().getContext(), attributeSet, findFragmentById.mSavedFragmentState);
            createOrGetFragmentStateManager = this.mFragmentManager.addFragment(findFragmentById);
            if (FragmentManager.isLoggingEnabled(2)) {
                sb = new StringBuilder();
                sb.append("Fragment ");
                sb.append(findFragmentById);
                str2 = " has been inflated via the <fragment> tag: id=0x";
                sb.append(str2);
                sb.append(Integer.toHexString(resourceId));
                Log.v("FragmentManager", sb.toString());
            }
            findFragmentById.mContainer = (ViewGroup) view;
            createOrGetFragmentStateManager.moveToExpectedState();
            createOrGetFragmentStateManager.ensureInflatedView();
            view2 = findFragmentById.mView;
            if (view2 != null) {
                throw new IllegalStateException("Fragment " + attributeValue + " did not create a view.");
            }
            if (resourceId != 0) {
                view2.setId(resourceId);
            }
            if (findFragmentById.mView.getTag() == null) {
                findFragmentById.mView.setTag(string);
            }
            findFragmentById.mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: androidx.fragment.app.FragmentLayoutInflaterFactory.1
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view3) {
                    Fragment fragment = createOrGetFragmentStateManager.getFragment();
                    createOrGetFragmentStateManager.moveToExpectedState();
                    SpecialEffectsController.getOrCreateController((ViewGroup) fragment.mView.getParent(), FragmentLayoutInflaterFactory.this.mFragmentManager).forceCompleteAllOperations();
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view3) {
                }
            });
            return findFragmentById.mView;
        }
        if (findFragmentById.mInLayout) {
            throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(resourceId) + ", tag " + string + ", or parent id 0x" + Integer.toHexString(id) + " with another fragment for " + attributeValue);
        }
        findFragmentById.mInLayout = true;
        FragmentManager fragmentManager2 = this.mFragmentManager;
        findFragmentById.mFragmentManager = fragmentManager2;
        findFragmentById.mHost = fragmentManager2.getHost();
        findFragmentById.onInflate(this.mFragmentManager.getHost().getContext(), attributeSet, findFragmentById.mSavedFragmentState);
        createOrGetFragmentStateManager = this.mFragmentManager.createOrGetFragmentStateManager(findFragmentById);
        if (FragmentManager.isLoggingEnabled(2)) {
            sb = new StringBuilder();
            sb.append("Retained Fragment ");
            sb.append(findFragmentById);
            str2 = " has been re-attached via the <fragment> tag: id=0x";
            sb.append(str2);
            sb.append(Integer.toHexString(resourceId));
            Log.v("FragmentManager", sb.toString());
        }
        findFragmentById.mContainer = (ViewGroup) view;
        createOrGetFragmentStateManager.moveToExpectedState();
        createOrGetFragmentStateManager.ensureInflatedView();
        view2 = findFragmentById.mView;
        if (view2 != null) {
        }
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }
}
