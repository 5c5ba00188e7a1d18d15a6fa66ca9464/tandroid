package j$.util.stream;

import j$.util.function.BiFunction;
import j$.util.function.Function;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class I1 extends O1 {
    public static final /* synthetic */ int k = 0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public I1(z2 z2Var, j$.util.t tVar, int i) {
        super(z2Var, tVar, new j$.util.function.r() { // from class: j$.util.stream.H1
            @Override // j$.util.function.r
            public final Object apply(long j) {
                return y2.j(j);
            }
        }, new j$.util.function.b() { // from class: j$.util.stream.G1
            @Override // j$.util.function.BiFunction
            public BiFunction andThen(Function function) {
                Objects.requireNonNull(function);
                return new j$.util.concurrent.a(this, function);
            }

            @Override // j$.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return new P1((v1) obj, (v1) obj2);
            }
        });
        if (i == 1) {
            super(z2Var, tVar, new j$.util.function.r() { // from class: j$.util.stream.K1
                @Override // j$.util.function.r
                public final Object apply(long j) {
                    return y2.p(j);
                }
            }, new j$.util.function.b() { // from class: j$.util.stream.J1
                @Override // j$.util.function.BiFunction
                public BiFunction andThen(Function function) {
                    Objects.requireNonNull(function);
                    return new j$.util.concurrent.a(this, function);
                }

                @Override // j$.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return new Q1((x1) obj, (x1) obj2);
                }
            });
        } else if (i != 2) {
        } else {
            super(z2Var, tVar, new j$.util.function.r() { // from class: j$.util.stream.M1
                @Override // j$.util.function.r
                public final Object apply(long j) {
                    return y2.q(j);
                }
            }, new j$.util.function.b() { // from class: j$.util.stream.L1
                @Override // j$.util.function.BiFunction
                public BiFunction andThen(Function function) {
                    Objects.requireNonNull(function);
                    return new j$.util.concurrent.a(this, function);
                }

                @Override // j$.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return new R1((z1) obj, (z1) obj2);
                }
            });
        }
    }

    public I1(z2 z2Var, j$.util.function.m mVar, j$.util.t tVar) {
        super(z2Var, tVar, new b(mVar), new j$.util.function.b() { // from class: j$.util.stream.N1
            @Override // j$.util.function.BiFunction
            public BiFunction andThen(Function function) {
                Objects.requireNonNull(function);
                return new j$.util.concurrent.a(this, function);
            }

            @Override // j$.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return new T1((B1) obj, (B1) obj2);
            }
        });
    }
}
