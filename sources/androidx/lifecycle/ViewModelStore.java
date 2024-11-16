package androidx.lifecycle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public class ViewModelStore {
    private final HashMap mMap = new HashMap();

    public final void clear() {
        Iterator it = this.mMap.values().iterator();
        while (it.hasNext()) {
            ((ViewModel) it.next()).clear();
        }
        this.mMap.clear();
    }

    final ViewModel get(String str) {
        return (ViewModel) this.mMap.get(str);
    }

    Set keys() {
        return new HashSet(this.mMap.keySet());
    }

    final void put(String str, ViewModel viewModel) {
        ViewModel viewModel2 = (ViewModel) this.mMap.put(str, viewModel);
        if (viewModel2 != null) {
            viewModel2.onCleared();
        }
    }
}
