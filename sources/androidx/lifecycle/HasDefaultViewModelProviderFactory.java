package androidx.lifecycle;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

/* loaded from: classes.dex */
public interface HasDefaultViewModelProviderFactory {

    /* loaded from: classes.dex */
    public abstract /* synthetic */ class -CC {
        public static CreationExtras $default$getDefaultViewModelCreationExtras(HasDefaultViewModelProviderFactory hasDefaultViewModelProviderFactory) {
            return CreationExtras.Empty.INSTANCE;
        }
    }

    CreationExtras getDefaultViewModelCreationExtras();

    ViewModelProvider.Factory getDefaultViewModelProviderFactory();
}
