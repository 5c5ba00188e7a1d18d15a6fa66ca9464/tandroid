package org.webrtc;
/* loaded from: classes.dex */
public interface Predicate<T> {
    Predicate<T> and(Predicate<? super T> predicate);

    Predicate<T> negate();

    Predicate<T> or(Predicate<? super T> predicate);

    boolean test(T t);

    /* loaded from: classes.dex */
    public final /* synthetic */ class -CC<T> {
        public static Predicate $default$or(final Predicate predicate, final Predicate predicate2) {
            return new Predicate<T>() { // from class: org.webrtc.Predicate.1
                @Override // org.webrtc.Predicate
                public /* synthetic */ Predicate and(Predicate predicate3) {
                    return -CC.$default$and(this, predicate3);
                }

                @Override // org.webrtc.Predicate
                public /* synthetic */ Predicate negate() {
                    return -CC.$default$negate(this);
                }

                @Override // org.webrtc.Predicate
                public /* synthetic */ Predicate or(Predicate predicate3) {
                    return -CC.$default$or(this, predicate3);
                }

                @Override // org.webrtc.Predicate
                public boolean test(T t) {
                    return Predicate.this.test(t) || predicate2.test(t);
                }
            };
        }

        public static Predicate $default$and(final Predicate predicate, final Predicate predicate2) {
            return new Predicate<T>() { // from class: org.webrtc.Predicate.2
                @Override // org.webrtc.Predicate
                public /* synthetic */ Predicate and(Predicate predicate3) {
                    return -CC.$default$and(this, predicate3);
                }

                @Override // org.webrtc.Predicate
                public /* synthetic */ Predicate negate() {
                    return -CC.$default$negate(this);
                }

                @Override // org.webrtc.Predicate
                public /* synthetic */ Predicate or(Predicate predicate3) {
                    return -CC.$default$or(this, predicate3);
                }

                @Override // org.webrtc.Predicate
                public boolean test(T t) {
                    return Predicate.this.test(t) && predicate2.test(t);
                }
            };
        }

        public static Predicate $default$negate(final Predicate predicate) {
            return new Predicate<T>() { // from class: org.webrtc.Predicate.3
                @Override // org.webrtc.Predicate
                public /* synthetic */ Predicate and(Predicate predicate2) {
                    return -CC.$default$and(this, predicate2);
                }

                @Override // org.webrtc.Predicate
                public /* synthetic */ Predicate negate() {
                    return -CC.$default$negate(this);
                }

                @Override // org.webrtc.Predicate
                public /* synthetic */ Predicate or(Predicate predicate2) {
                    return -CC.$default$or(this, predicate2);
                }

                @Override // org.webrtc.Predicate
                public boolean test(T t) {
                    return !Predicate.this.test(t);
                }
            };
        }
    }
}
