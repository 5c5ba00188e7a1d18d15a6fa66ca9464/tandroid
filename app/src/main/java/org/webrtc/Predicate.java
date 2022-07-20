package org.webrtc;
/* loaded from: classes3.dex */
public interface Predicate<T> {
    Predicate<T> and(Predicate<? super T> predicate);

    Predicate<T> negate();

    Predicate<T> or(Predicate<? super T> predicate);

    boolean test(T t);

    /* renamed from: org.webrtc.Predicate$-CC */
    /* loaded from: classes3.dex */
    public final /* synthetic */ class CC {
        public static Predicate $default$or(Predicate _this, Predicate predicate) {
            return new AnonymousClass1(predicate);
        }

        public static Predicate $default$and(Predicate _this, Predicate predicate) {
            return new AnonymousClass2(predicate);
        }

        public static Predicate $default$negate(Predicate _this) {
            return new AnonymousClass3();
        }
    }

    /* renamed from: org.webrtc.Predicate$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Predicate<T> {
        final /* synthetic */ Predicate val$other;

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate and(Predicate predicate) {
            return CC.$default$and(this, predicate);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate negate() {
            return CC.$default$negate(this);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate or(Predicate predicate) {
            return CC.$default$or(this, predicate);
        }

        AnonymousClass1(Predicate predicate) {
            Predicate.this = r1;
            this.val$other = predicate;
        }

        @Override // org.webrtc.Predicate
        public boolean test(T t) {
            return Predicate.this.test(t) || this.val$other.test(t);
        }
    }

    /* renamed from: org.webrtc.Predicate$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements Predicate<T> {
        final /* synthetic */ Predicate val$other;

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate and(Predicate predicate) {
            return CC.$default$and(this, predicate);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate negate() {
            return CC.$default$negate(this);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate or(Predicate predicate) {
            return CC.$default$or(this, predicate);
        }

        AnonymousClass2(Predicate predicate) {
            Predicate.this = r1;
            this.val$other = predicate;
        }

        @Override // org.webrtc.Predicate
        public boolean test(T t) {
            return Predicate.this.test(t) && this.val$other.test(t);
        }
    }

    /* renamed from: org.webrtc.Predicate$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements Predicate<T> {
        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate and(Predicate predicate) {
            return CC.$default$and(this, predicate);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate negate() {
            return CC.$default$negate(this);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate or(Predicate predicate) {
            return CC.$default$or(this, predicate);
        }

        AnonymousClass3() {
            Predicate.this = r1;
        }

        @Override // org.webrtc.Predicate
        public boolean test(T t) {
            return !Predicate.this.test(t);
        }
    }
}
