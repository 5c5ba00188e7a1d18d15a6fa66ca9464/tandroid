package androidx.lifecycle.viewmodel;

import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;

/* loaded from: classes.dex */
public final class InitializerViewModelFactoryBuilder {
    private final List initializers = new ArrayList();

    public final void addInitializer(KClass clazz, Function1 initializer) {
        Intrinsics.checkNotNullParameter(clazz, "clazz");
        Intrinsics.checkNotNullParameter(initializer, "initializer");
        this.initializers.add(new ViewModelInitializer(JvmClassMappingKt.getJavaClass(clazz), initializer));
    }

    public final ViewModelProvider.Factory build() {
        Object[] array = this.initializers.toArray(new ViewModelInitializer[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
        ViewModelInitializer[] viewModelInitializerArr = (ViewModelInitializer[]) array;
        return new InitializerViewModelFactory((ViewModelInitializer[]) Arrays.copyOf(viewModelInitializerArr, viewModelInitializerArr.length));
    }
}
