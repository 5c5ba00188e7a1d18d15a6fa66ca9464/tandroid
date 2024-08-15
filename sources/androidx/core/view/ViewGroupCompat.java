package androidx.core.view;

import android.os.Build;
import android.view.ViewGroup;
import androidx.core.R$id;
/* loaded from: classes.dex */
public final class ViewGroupCompat {
    public static boolean isTransitionGroup(ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.isTransitionGroup(viewGroup);
        }
        Boolean bool = (Boolean) viewGroup.getTag(R$id.tag_transition_group);
        return ((bool == null || !bool.booleanValue()) && viewGroup.getBackground() == null && ViewCompat.getTransitionName(viewGroup) == null) ? false : true;
    }

    /* loaded from: classes.dex */
    static class Api21Impl {
        static void setTransitionGroup(ViewGroup viewGroup, boolean z) {
            viewGroup.setTransitionGroup(z);
        }

        static boolean isTransitionGroup(ViewGroup viewGroup) {
            boolean isTransitionGroup;
            isTransitionGroup = viewGroup.isTransitionGroup();
            return isTransitionGroup;
        }

        static int getNestedScrollAxes(ViewGroup viewGroup) {
            int nestedScrollAxes;
            nestedScrollAxes = viewGroup.getNestedScrollAxes();
            return nestedScrollAxes;
        }
    }
}
