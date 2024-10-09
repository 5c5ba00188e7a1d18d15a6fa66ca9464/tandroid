package kotlinx.coroutines;

import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* loaded from: classes.dex */
public abstract class YieldContext extends AbstractCoroutineContextElement {
    public static final Key Key = new Key(null);

    /* loaded from: classes.dex */
    public static final class Key implements CoroutineContext.Key {
        private Key() {
        }

        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}
