package androidx.lifecycle;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public abstract class ViewModel {
    private final Map mBagOfTags = new HashMap();
    private final Set mCloseables = new LinkedHashSet();
    private volatile boolean mCleared = false;

    private static void closeWithRuntimeException(Object obj) {
        if (obj instanceof Closeable) {
            try {
                ((Closeable) obj).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    final void clear() {
        this.mCleared = true;
        Map map = this.mBagOfTags;
        if (map != null) {
            synchronized (map) {
                try {
                    Iterator it = this.mBagOfTags.values().iterator();
                    while (it.hasNext()) {
                        closeWithRuntimeException(it.next());
                    }
                } finally {
                }
            }
        }
        Set set = this.mCloseables;
        if (set != null) {
            synchronized (set) {
                try {
                    Iterator it2 = this.mCloseables.iterator();
                    while (it2.hasNext()) {
                        closeWithRuntimeException((Closeable) it2.next());
                    }
                } finally {
                }
            }
        }
        onCleared();
    }

    Object getTag(String str) {
        Object obj;
        Map map = this.mBagOfTags;
        if (map == null) {
            return null;
        }
        synchronized (map) {
            obj = this.mBagOfTags.get(str);
        }
        return obj;
    }

    protected void onCleared() {
    }

    Object setTagIfAbsent(String str, Object obj) {
        Object obj2;
        synchronized (this.mBagOfTags) {
            try {
                obj2 = this.mBagOfTags.get(str);
                if (obj2 == null) {
                    this.mBagOfTags.put(str, obj);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (obj2 != null) {
            obj = obj2;
        }
        if (this.mCleared) {
            closeWithRuntimeException(obj);
        }
        return obj;
    }
}
