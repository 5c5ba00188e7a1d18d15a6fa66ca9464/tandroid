package androidx.loader.app;

import android.os.Bundle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.content.Loader;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public abstract class LoaderManager {

    /* loaded from: classes.dex */
    public interface LoaderCallbacks {
        Loader onCreateLoader(int i, Bundle bundle);

        void onLoadFinished(Loader loader, Object obj);

        void onLoaderReset(Loader loader);
    }

    public static LoaderManager getInstance(LifecycleOwner lifecycleOwner) {
        return new LoaderManagerImpl(lifecycleOwner, ((ViewModelStoreOwner) lifecycleOwner).getViewModelStore());
    }

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract Loader initLoader(int i, Bundle bundle, LoaderCallbacks loaderCallbacks);

    public abstract void markForRedelivery();
}
