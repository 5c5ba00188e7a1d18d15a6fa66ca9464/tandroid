package androidx.activity.contextaware;

import android.content.Context;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes.dex */
public final class ContextAwareHelper {
    private volatile Context mContext;
    private final Set mListeners = new CopyOnWriteArraySet();

    public void addOnContextAvailableListener(OnContextAvailableListener onContextAvailableListener) {
        if (this.mContext != null) {
            onContextAvailableListener.onContextAvailable(this.mContext);
        }
        this.mListeners.add(onContextAvailableListener);
    }

    public void clearAvailableContext() {
        this.mContext = null;
    }

    public void dispatchOnContextAvailable(Context context) {
        this.mContext = context;
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((OnContextAvailableListener) it.next()).onContextAvailable(context);
        }
    }
}
