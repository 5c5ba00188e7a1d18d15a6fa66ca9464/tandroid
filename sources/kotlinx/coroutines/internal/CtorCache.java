package kotlinx.coroutines.internal;

import kotlin.jvm.functions.Function1;
/* compiled from: ExceptionsConstructor.kt */
/* loaded from: classes.dex */
public abstract class CtorCache {
    public abstract Function1<Throwable, Throwable> get(Class<? extends Throwable> cls);
}
