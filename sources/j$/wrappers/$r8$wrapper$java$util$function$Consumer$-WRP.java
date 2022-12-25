package j$.wrappers;

import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class $r8$wrapper$java$util$function$Consumer$-WRP implements Consumer {
    final /* synthetic */ j$.util.function.Consumer a;

    private /* synthetic */ $r8$wrapper$java$util$function$Consumer$-WRP(j$.util.function.Consumer consumer) {
        this.a = consumer;
    }

    public static /* synthetic */ Consumer convert(j$.util.function.Consumer consumer) {
        if (consumer == null) {
            return null;
        }
        return consumer instanceof w ? ((w) consumer).a : new $r8$wrapper$java$util$function$Consumer$-WRP(consumer);
    }

    @Override // java.util.function.Consumer
    public /* synthetic */ void accept(Object obj) {
        this.a.accept(obj);
    }

    @Override // java.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return convert(this.a.andThen(w.b(consumer)));
    }
}
