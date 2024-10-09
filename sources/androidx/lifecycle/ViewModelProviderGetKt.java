package androidx.lifecycle;

import androidx.lifecycle.viewmodel.CreationExtras;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
public abstract class ViewModelProviderGetKt {
    public static final CreationExtras defaultCreationExtras(ViewModelStoreOwner owner) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        if (!(owner instanceof HasDefaultViewModelProviderFactory)) {
            return CreationExtras.Empty.INSTANCE;
        }
        CreationExtras defaultViewModelCreationExtras = ((HasDefaultViewModelProviderFactory) owner).getDefaultViewModelCreationExtras();
        Intrinsics.checkNotNullExpressionValue(defaultViewModelCreationExtras, "{\n        owner.defaultV…ModelCreationExtras\n    }");
        return defaultViewModelCreationExtras;
    }
}
