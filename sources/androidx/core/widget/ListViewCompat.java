package androidx.core.widget;

import android.widget.ListView;

/* loaded from: classes.dex */
public abstract class ListViewCompat {

    static class Api19Impl {
        static boolean canScrollList(ListView listView, int i) {
            return listView.canScrollList(i);
        }

        static void scrollListBy(ListView listView, int i) {
            listView.scrollListBy(i);
        }
    }

    public static void scrollListBy(ListView listView, int i) {
        Api19Impl.scrollListBy(listView, i);
    }
}
