package j$.util.concurrent;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Function;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.telegram.tgnet.ConnectionsManager;
import sun.misc.Unsafe;

/* loaded from: classes2.dex */
public class ConcurrentHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable, t {
    private static final int g = (1 << (32 - 16)) - 1;
    private static final int h = 32 - 16;
    static final int i = Runtime.getRuntime().availableProcessors();
    private static final Unsafe j;
    private static final long k;
    private static final long l;
    private static final long m;
    private static final long n;
    private static final long o;
    private static final long p;
    private static final int q;
    private static final ObjectStreamField[] serialPersistentFields;
    private static final long serialVersionUID = 7249069246763182397L;
    volatile transient k[] a;
    private volatile transient k[] b;
    private volatile transient long baseCount;
    private volatile transient c[] c;
    private volatile transient int cellsBusy;
    private transient h d;
    private transient r e;
    private transient h f;
    private volatile transient int sizeCtl;
    private volatile transient int transferIndex;

    static {
        ObjectStreamField objectStreamField = new ObjectStreamField("segments", m[].class);
        Class cls = Integer.TYPE;
        serialPersistentFields = new ObjectStreamField[]{objectStreamField, new ObjectStreamField("segmentMask", cls), new ObjectStreamField("segmentShift", cls)};
        try {
            Unsafe c = u.c();
            j = c;
            k = c.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("sizeCtl"));
            l = c.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("transferIndex"));
            m = c.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("baseCount"));
            n = c.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("cellsBusy"));
            o = c.objectFieldOffset(c.class.getDeclaredField("value"));
            p = c.arrayBaseOffset(k[].class);
            int arrayIndexScale = c.arrayIndexScale(k[].class);
            if (((arrayIndexScale - 1) & arrayIndexScale) != 0) {
                throw new Error("data type scale not a power of two");
            }
            q = 31 - Integer.numberOfLeadingZeros(arrayIndexScale);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public ConcurrentHashMap() {
    }

    public ConcurrentHashMap(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        this.sizeCtl = i2 >= 536870912 ? 1073741824 : m(i2 + (i2 >>> 1) + 1);
    }

    public ConcurrentHashMap(int i2, float f, int i3) {
        if (f <= 0.0f || i2 < 0 || i3 <= 0) {
            throw new IllegalArgumentException();
        }
        double d = (i2 < i3 ? i3 : i2) / f;
        Double.isNaN(d);
        long j2 = (long) (d + 1.0d);
        this.sizeCtl = j2 >= 1073741824 ? 1073741824 : m((int) j2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0012, code lost:
    
        if (r1.compareAndSwapLong(r11, r3, r5, r9) == false) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void a(long j2, int i2) {
        int length;
        c cVar;
        long k2;
        k[] kVarArr;
        int length2;
        k[] kVarArr2;
        c[] cVarArr = this.c;
        if (cVarArr == null) {
            Unsafe unsafe = j;
            long j3 = m;
            long j4 = this.baseCount;
            k2 = j4 + j2;
        }
        boolean z = true;
        if (cVarArr != null && (length = cVarArr.length - 1) >= 0 && (cVar = cVarArr[length & ThreadLocalRandom.b()]) != null) {
            Unsafe unsafe2 = j;
            long j5 = o;
            long j6 = cVar.value;
            boolean compareAndSwapLong = unsafe2.compareAndSwapLong(cVar, j5, j6, j6 + j2);
            if (!compareAndSwapLong) {
                z = compareAndSwapLong;
            } else {
                if (i2 <= 1) {
                    return;
                }
                k2 = k();
                if (i2 < 0) {
                    return;
                }
                while (true) {
                    int i3 = this.sizeCtl;
                    if (k2 < i3 || (kVarArr = this.a) == null || (length2 = kVarArr.length) >= 1073741824) {
                        return;
                    }
                    int numberOfLeadingZeros = Integer.numberOfLeadingZeros(length2) | 32768;
                    int i4 = h;
                    if (i3 < 0) {
                        if ((i3 >>> i4) != numberOfLeadingZeros || i3 == numberOfLeadingZeros + 1 || i3 == numberOfLeadingZeros + g || (kVarArr2 = this.b) == null || this.transferIndex <= 0) {
                            return;
                        }
                        if (j.compareAndSwapInt(this, k, i3, i3 + 1)) {
                            n(kVarArr, kVarArr2);
                        }
                    } else if (j.compareAndSwapInt(this, k, i3, (numberOfLeadingZeros << i4) + 2)) {
                        n(kVarArr, null);
                    }
                    k2 = k();
                }
            }
        }
        d(j2, z);
    }

    static final boolean b(k[] kVarArr, int i2, k kVar) {
        return j$.com.android.tools.r8.a.h(j, kVarArr, (i2 << q) + p, kVar);
    }

    static Class c(Object obj) {
        Type[] actualTypeArguments;
        if (!(obj instanceof Comparable)) {
            return null;
        }
        Class<?> cls = obj.getClass();
        if (cls == String.class) {
            return cls;
        }
        Type[] genericInterfaces = cls.getGenericInterfaces();
        if (genericInterfaces == null) {
            return null;
        }
        for (Type type : genericInterfaces) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (parameterizedType.getRawType() == Comparable.class && (actualTypeArguments = parameterizedType.getActualTypeArguments()) != null && actualTypeArguments.length == 1 && actualTypeArguments[0] == cls) {
                    return cls;
                }
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:93:0x009e, code lost:
    
        if (r24.c != r7) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x00a0, code lost:
    
        r1 = new j$.util.concurrent.c[r8 << 1];
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x00a5, code lost:
    
        if (r2 >= r8) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x00a7, code lost:
    
        r1[r2] = r7[r2];
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x00b0, code lost:
    
        r24.c = r1;
     */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0105 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x001b A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void d(long j2, boolean z) {
        int i2;
        boolean z2;
        boolean z3;
        int length;
        boolean z4;
        int length2;
        int b = ThreadLocalRandom.b();
        if (b == 0) {
            ThreadLocalRandom.f();
            i2 = ThreadLocalRandom.b();
            z2 = true;
        } else {
            i2 = b;
            z2 = z;
        }
        int i3 = i2;
        while (true) {
            boolean z5 = false;
            while (true) {
                c[] cVarArr = this.c;
                if (cVarArr != null && (length = cVarArr.length) > 0) {
                    c cVar = cVarArr[(length - 1) & i3];
                    if (cVar != null) {
                        if (z2) {
                            Unsafe unsafe = j;
                            long j3 = o;
                            long j4 = cVar.value;
                            if (unsafe.compareAndSwapLong(cVar, j3, j4, j4 + j2)) {
                                return;
                            }
                            if (this.c == cVarArr && length < i) {
                                if (!z5) {
                                    z5 = true;
                                } else if (this.cellsBusy == 0 && unsafe.compareAndSwapInt(this, n, 0, 1)) {
                                    try {
                                        break;
                                    } catch (Throwable th) {
                                        throw th;
                                    }
                                }
                            }
                        } else {
                            z2 = true;
                        }
                        i3 = ThreadLocalRandom.a(i3);
                    } else if (this.cellsBusy == 0) {
                        c cVar2 = new c(j2);
                        if (this.cellsBusy == 0 && j.compareAndSwapInt(this, n, 0, 1)) {
                            try {
                                c[] cVarArr2 = this.c;
                                if (cVarArr2 != null && (length2 = cVarArr2.length) > 0) {
                                    int i4 = (length2 - 1) & i3;
                                    if (cVarArr2[i4] == null) {
                                        cVarArr2[i4] = cVar2;
                                        z4 = true;
                                        if (!z4) {
                                            return;
                                        }
                                    }
                                }
                                z4 = false;
                                if (!z4) {
                                }
                            } finally {
                            }
                        }
                    }
                    z5 = false;
                    i3 = ThreadLocalRandom.a(i3);
                } else if (this.cellsBusy == 0 && this.c == cVarArr && j.compareAndSwapInt(this, n, 0, 1)) {
                    try {
                        if (this.c == cVarArr) {
                            c[] cVarArr3 = new c[2];
                            cVarArr3[i3 & 1] = new c(j2);
                            this.c = cVarArr3;
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        if (z3) {
                            return;
                        }
                    } finally {
                    }
                } else {
                    Unsafe unsafe2 = j;
                    long j5 = m;
                    long j6 = this.baseCount;
                    if (unsafe2.compareAndSwapLong(this, j5, j6, j6 + j2)) {
                        return;
                    }
                }
            }
        }
    }

    private final k[] f() {
        while (true) {
            k[] kVarArr = this.a;
            if (kVarArr != null && kVarArr.length != 0) {
                return kVarArr;
            }
            int i2 = this.sizeCtl;
            if (i2 < 0) {
                Thread.yield();
            } else if (j.compareAndSwapInt(this, k, i2, -1)) {
                try {
                    k[] kVarArr2 = this.a;
                    if (kVarArr2 != null) {
                        if (kVarArr2.length == 0) {
                        }
                        this.sizeCtl = i2;
                        return kVarArr2;
                    }
                    int i3 = i2 > 0 ? i2 : 16;
                    k[] kVarArr3 = new k[i3];
                    this.a = kVarArr3;
                    i2 = i3 - (i3 >>> 2);
                    kVarArr2 = kVarArr3;
                    this.sizeCtl = i2;
                    return kVarArr2;
                } catch (Throwable th) {
                    this.sizeCtl = i2;
                    throw th;
                }
            }
        }
    }

    static final void i(k[] kVarArr, int i2, k kVar) {
        j.putObjectVolatile(kVarArr, (i2 << q) + p, kVar);
    }

    static final int j(int i2) {
        return (i2 ^ (i2 >>> 16)) & ConnectionsManager.DEFAULT_DATACENTER_ID;
    }

    static final k l(k[] kVarArr, int i2) {
        return (k) j.getObjectVolatile(kVarArr, (i2 << q) + p);
    }

    private static final int m(int i2) {
        int i3 = i2 - 1;
        int i4 = i3 | (i3 >>> 1);
        int i5 = i4 | (i4 >>> 2);
        int i6 = i5 | (i5 >>> 4);
        int i7 = i6 | (i6 >>> 8);
        int i8 = i7 | (i7 >>> 16);
        if (i8 < 0) {
            return 1;
        }
        if (i8 >= 1073741824) {
            return 1073741824;
        }
        return 1 + i8;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v10, types: [j$.util.concurrent.k] */
    /* JADX WARN: Type inference failed for: r12v12, types: [j$.util.concurrent.k] */
    /* JADX WARN: Type inference failed for: r4v0, types: [j$.util.concurrent.k] */
    /* JADX WARN: Type inference failed for: r5v17, types: [j$.util.concurrent.k] */
    /* JADX WARN: Type inference failed for: r5v22, types: [j$.util.concurrent.k] */
    private final void n(k[] kVarArr, k[] kVarArr2) {
        k[] kVarArr3;
        int i2;
        int i3;
        f fVar;
        ConcurrentHashMap<K, V> concurrentHashMap;
        int i4;
        q qVar;
        int i5;
        ConcurrentHashMap<K, V> concurrentHashMap2 = this;
        k[] kVarArr4 = kVarArr;
        int length = kVarArr4.length;
        int i6 = i;
        int i7 = i6 > 1 ? (length >>> 3) / i6 : length;
        int i8 = i7 < 16 ? 16 : i7;
        if (kVarArr2 == null) {
            try {
                k[] kVarArr5 = new k[length << 1];
                concurrentHashMap2.b = kVarArr5;
                concurrentHashMap2.transferIndex = length;
                kVarArr3 = kVarArr5;
            } catch (Throwable unused) {
                concurrentHashMap2.sizeCtl = ConnectionsManager.DEFAULT_DATACENTER_ID;
                return;
            }
        } else {
            kVarArr3 = kVarArr2;
        }
        int length2 = kVarArr3.length;
        f fVar2 = new f(kVarArr3);
        int i9 = 0;
        int i10 = 0;
        boolean z = true;
        boolean z2 = false;
        while (true) {
            if (z) {
                int i11 = i10 - 1;
                if (i11 >= i9 || z2) {
                    i9 = i9;
                    i10 = i11;
                } else {
                    int i12 = concurrentHashMap2.transferIndex;
                    if (i12 <= 0) {
                        i10 = -1;
                    } else {
                        Unsafe unsafe = j;
                        long j2 = l;
                        int i13 = i12 > i8 ? i12 - i8 : 0;
                        int i14 = i9;
                        if (unsafe.compareAndSwapInt(this, j2, i12, i13)) {
                            i10 = i12 - 1;
                            i9 = i13;
                        } else {
                            i9 = i14;
                            i10 = i11;
                        }
                    }
                }
                z = false;
            } else {
                int i15 = i9;
                q qVar2 = null;
                if (i10 < 0 || i10 >= length || (i4 = i10 + length) >= length2) {
                    i2 = i8;
                    i3 = length2;
                    fVar = fVar2;
                    concurrentHashMap = this;
                    if (z2) {
                        concurrentHashMap.b = null;
                        concurrentHashMap.a = kVarArr3;
                        concurrentHashMap.sizeCtl = (length << 1) - (length >>> 1);
                        return;
                    }
                    Unsafe unsafe2 = j;
                    long j3 = k;
                    int i16 = concurrentHashMap.sizeCtl;
                    int i17 = i10;
                    if (!unsafe2.compareAndSwapInt(this, j3, i16, i16 - 1)) {
                        i10 = i17;
                    } else {
                        if (i16 - 2 != ((Integer.numberOfLeadingZeros(length) | 32768) << h)) {
                            return;
                        }
                        i10 = length;
                        z = true;
                        z2 = true;
                    }
                } else {
                    ?? l2 = l(kVarArr4, i10);
                    if (l2 == 0) {
                        z = b(kVarArr4, i10, fVar2);
                        concurrentHashMap = concurrentHashMap2;
                        i2 = i8;
                        i3 = length2;
                        fVar = fVar2;
                    } else {
                        int i18 = l2.a;
                        if (i18 == -1) {
                            concurrentHashMap = concurrentHashMap2;
                            i2 = i8;
                            i3 = length2;
                            fVar = fVar2;
                            z = true;
                        } else {
                            synchronized (l2) {
                                try {
                                    if (l(kVarArr4, i10) == l2) {
                                        if (i18 >= 0) {
                                            int i19 = i18 & length;
                                            q qVar3 = l2;
                                            for (q qVar4 = l2.d; qVar4 != null; qVar4 = qVar4.d) {
                                                int i20 = qVar4.a & length;
                                                if (i20 != i19) {
                                                    qVar3 = qVar4;
                                                    i19 = i20;
                                                }
                                            }
                                            if (i19 == 0) {
                                                qVar = null;
                                                qVar2 = qVar3;
                                            } else {
                                                qVar = qVar3;
                                            }
                                            k kVar = l2;
                                            while (kVar != qVar3) {
                                                int i21 = kVar.a;
                                                Object obj = kVar.b;
                                                int i22 = i8;
                                                Object obj2 = kVar.c;
                                                if ((i21 & length) == 0) {
                                                    i5 = length2;
                                                    qVar2 = new k(i21, obj, obj2, qVar2);
                                                } else {
                                                    i5 = length2;
                                                    qVar = new k(i21, obj, obj2, qVar);
                                                }
                                                kVar = kVar.d;
                                                i8 = i22;
                                                length2 = i5;
                                            }
                                            i2 = i8;
                                            i3 = length2;
                                            i(kVarArr3, i10, qVar2);
                                            i(kVarArr3, i4, qVar);
                                            i(kVarArr4, i10, fVar2);
                                            fVar = fVar2;
                                        } else {
                                            i2 = i8;
                                            i3 = length2;
                                            if (l2 instanceof p) {
                                                p pVar = (p) l2;
                                                q qVar5 = null;
                                                q qVar6 = null;
                                                k kVar2 = pVar.f;
                                                int i23 = 0;
                                                int i24 = 0;
                                                q qVar7 = null;
                                                while (kVar2 != null) {
                                                    p pVar2 = pVar;
                                                    int i25 = kVar2.a;
                                                    f fVar3 = fVar2;
                                                    q qVar8 = new q(i25, kVar2.b, kVar2.c, null, null);
                                                    if ((i25 & length) == 0) {
                                                        qVar8.h = qVar6;
                                                        if (qVar6 == null) {
                                                            qVar2 = qVar8;
                                                        } else {
                                                            qVar6.d = qVar8;
                                                        }
                                                        i23++;
                                                        qVar6 = qVar8;
                                                    } else {
                                                        qVar8.h = qVar5;
                                                        if (qVar5 == null) {
                                                            qVar7 = qVar8;
                                                        } else {
                                                            qVar5.d = qVar8;
                                                        }
                                                        i24++;
                                                        qVar5 = qVar8;
                                                    }
                                                    kVar2 = kVar2.d;
                                                    pVar = pVar2;
                                                    fVar2 = fVar3;
                                                }
                                                p pVar3 = pVar;
                                                f fVar4 = fVar2;
                                                k q2 = i23 <= 6 ? q(qVar2) : i24 != 0 ? new p(qVar2) : pVar3;
                                                k q3 = i24 <= 6 ? q(qVar7) : i23 != 0 ? new p(qVar7) : pVar3;
                                                i(kVarArr3, i10, q2);
                                                i(kVarArr3, i4, q3);
                                                kVarArr4 = kVarArr;
                                                fVar = fVar4;
                                                i(kVarArr4, i10, fVar);
                                            }
                                        }
                                        z = true;
                                    } else {
                                        i2 = i8;
                                        i3 = length2;
                                    }
                                    fVar = fVar2;
                                } finally {
                                }
                            }
                            concurrentHashMap = this;
                        }
                    }
                }
                fVar2 = fVar;
                concurrentHashMap2 = concurrentHashMap;
                i9 = i15;
                i8 = i2;
                length2 = i3;
            }
        }
    }

    private final void o(k[] kVarArr, int i2) {
        int length = kVarArr.length;
        if (length < 64) {
            p(length << 1);
            return;
        }
        k l2 = l(kVarArr, i2);
        if (l2 == null || l2.a < 0) {
            return;
        }
        synchronized (l2) {
            try {
                if (l(kVarArr, i2) == l2) {
                    q qVar = null;
                    k kVar = l2;
                    q qVar2 = null;
                    while (kVar != null) {
                        q qVar3 = new q(kVar.a, kVar.b, kVar.c, null, null);
                        qVar3.h = qVar2;
                        if (qVar2 == null) {
                            qVar = qVar3;
                        } else {
                            qVar2.d = qVar3;
                        }
                        kVar = kVar.d;
                        qVar2 = qVar3;
                    }
                    i(kVarArr, i2, new p(qVar));
                }
            } finally {
            }
        }
    }

    private final void p(int i2) {
        int length;
        k[] kVarArr;
        int m2 = i2 >= 536870912 ? 1073741824 : m(i2 + (i2 >>> 1) + 1);
        while (true) {
            int i3 = this.sizeCtl;
            if (i3 < 0) {
                return;
            }
            k[] kVarArr2 = this.a;
            if (kVarArr2 == null || (length = kVarArr2.length) == 0) {
                int i4 = i3 > m2 ? i3 : m2;
                if (j.compareAndSwapInt(this, k, i3, -1)) {
                    try {
                        if (this.a == kVarArr2) {
                            this.a = new k[i4];
                            i3 = i4 - (i4 >>> 2);
                        }
                    } finally {
                        this.sizeCtl = i3;
                    }
                } else {
                    continue;
                }
            } else {
                if (m2 <= i3 || length >= 1073741824) {
                    return;
                }
                if (kVarArr2 == this.a) {
                    int numberOfLeadingZeros = Integer.numberOfLeadingZeros(length) | 32768;
                    int i5 = h;
                    if (i3 < 0) {
                        if ((i3 >>> i5) != numberOfLeadingZeros || i3 == numberOfLeadingZeros + 1 || i3 == numberOfLeadingZeros + g || (kVarArr = this.b) == null || this.transferIndex <= 0) {
                            return;
                        }
                        if (j.compareAndSwapInt(this, k, i3, i3 + 1)) {
                            n(kVarArr2, kVarArr);
                        }
                    } else if (j.compareAndSwapInt(this, k, i3, (numberOfLeadingZeros << i5) + 2)) {
                        n(kVarArr2, null);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v2, types: [j$.util.concurrent.k] */
    static k q(q qVar) {
        k kVar = null;
        k kVar2 = null;
        for (q qVar2 = qVar; qVar2 != null; qVar2 = qVar2.d) {
            k kVar3 = new k(qVar2.a, qVar2.b, qVar2.c, null);
            if (kVar2 == null) {
                kVar = kVar3;
            } else {
                kVar2.d = kVar3;
            }
            kVar2 = kVar3;
        }
        return kVar;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        long j2;
        int m2;
        boolean z;
        Object obj;
        this.sizeCtl = -1;
        objectInputStream.defaultReadObject();
        long j3 = 0;
        long j4 = 0;
        k kVar = null;
        while (true) {
            Object readObject = objectInputStream.readObject();
            Object readObject2 = objectInputStream.readObject();
            j2 = 1;
            if (readObject == null || readObject2 == null) {
                break;
            }
            j4++;
            kVar = new k(j(readObject.hashCode()), readObject, readObject2, kVar);
        }
        if (j4 == 0) {
            this.sizeCtl = 0;
            return;
        }
        if (j4 >= 536870912) {
            m2 = 1073741824;
        } else {
            int i2 = (int) j4;
            m2 = m(i2 + (i2 >>> 1) + 1);
        }
        k[] kVarArr = new k[m2];
        int i3 = m2 - 1;
        while (kVar != null) {
            k kVar2 = kVar.d;
            int i4 = kVar.a;
            int i5 = i4 & i3;
            k l2 = l(kVarArr, i5);
            if (l2 == null) {
                z = true;
            } else {
                Object obj2 = kVar.b;
                if (l2.a >= 0) {
                    int i6 = 0;
                    for (k kVar3 = l2; kVar3 != null; kVar3 = kVar3.d) {
                        if (kVar3.a == i4 && ((obj = kVar3.b) == obj2 || (obj != null && obj2.equals(obj)))) {
                            z = false;
                            break;
                        }
                        i6++;
                    }
                    z = true;
                    if (z && i6 >= 8) {
                        long j5 = j3 + 1;
                        kVar.d = l2;
                        k kVar4 = kVar;
                        q qVar = null;
                        q qVar2 = null;
                        while (kVar4 != null) {
                            long j6 = j5;
                            q qVar3 = new q(kVar4.a, kVar4.b, kVar4.c, null, null);
                            qVar3.h = qVar2;
                            if (qVar2 == null) {
                                qVar = qVar3;
                            } else {
                                qVar2.d = qVar3;
                            }
                            kVar4 = kVar4.d;
                            qVar2 = qVar3;
                            j5 = j6;
                        }
                        i(kVarArr, i5, new p(qVar));
                        j3 = j5;
                    }
                } else if (((p) l2).f(i4, obj2, kVar.c) == null) {
                    j3 += j2;
                }
                z = false;
            }
            if (z) {
                j3++;
                kVar.d = l2;
                i(kVarArr, i5, kVar);
            }
            j2 = 1;
            kVar = kVar2;
        }
        this.a = kVarArr;
        this.sizeCtl = m2 - (m2 >>> 2);
        this.baseCount = j3;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        int i2 = 1;
        int i3 = 0;
        while (i2 < 16) {
            i3++;
            i2 <<= 1;
        }
        int i4 = 32 - i3;
        int i5 = i2 - 1;
        m[] mVarArr = new m[16];
        for (int i6 = 0; i6 < 16; i6++) {
            mVarArr[i6] = new m();
        }
        objectOutputStream.putFields().put("segments", mVarArr);
        objectOutputStream.putFields().put("segmentShift", i4);
        objectOutputStream.putFields().put("segmentMask", i5);
        objectOutputStream.writeFields();
        k[] kVarArr = this.a;
        if (kVarArr != null) {
            o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
            while (true) {
                k b = oVar.b();
                if (b == null) {
                    break;
                }
                objectOutputStream.writeObject(b.b);
                objectOutputStream.writeObject(b.c);
            }
        }
        objectOutputStream.writeObject(null);
        objectOutputStream.writeObject(null);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        k l2;
        k[] kVarArr = this.a;
        long j2 = 0;
        loop0: while (true) {
            int i2 = 0;
            while (kVarArr != null && i2 < kVarArr.length) {
                l2 = l(kVarArr, i2);
                if (l2 == null) {
                    i2++;
                } else {
                    int i3 = l2.a;
                    if (i3 == -1) {
                        break;
                    }
                    synchronized (l2) {
                        try {
                            if (l(kVarArr, i2) == l2) {
                                for (k kVar = i3 >= 0 ? l2 : l2 instanceof p ? ((p) l2).f : null; kVar != null; kVar = kVar.d) {
                                    j2--;
                                }
                                i(kVarArr, i2, null);
                                i2++;
                            }
                        } finally {
                        }
                    }
                }
            }
            kVarArr = e(kVarArr, l2);
        }
        if (j2 != 0) {
            a(j2, -1);
        }
    }

    @Override // j$.util.Map
    public final Object compute(Object obj, BiFunction biFunction) {
        k kVar;
        Object obj2;
        if (obj == null) {
            throw null;
        }
        if (biFunction == null) {
            throw null;
        }
        int j2 = j(obj.hashCode());
        k[] kVarArr = this.a;
        int i2 = 0;
        Object obj3 = null;
        int i3 = 0;
        while (true) {
            if (kVarArr != null) {
                int length = kVarArr.length;
                if (length != 0) {
                    int i4 = (length - 1) & j2;
                    k l2 = l(kVarArr, i4);
                    if (l2 == null) {
                        l lVar = new l();
                        synchronized (lVar) {
                            try {
                                if (b(kVarArr, i4, lVar)) {
                                    try {
                                        obj3 = biFunction.apply(obj, null);
                                        if (obj3 != null) {
                                            kVar = new k(j2, obj, obj3, null);
                                            i3 = 1;
                                        } else {
                                            kVar = null;
                                        }
                                        i(kVarArr, i4, kVar);
                                        i2 = 1;
                                    } catch (Throwable th) {
                                        i(kVarArr, i4, null);
                                        throw th;
                                    }
                                }
                            } finally {
                            }
                        }
                        if (i2 != 0) {
                        }
                    } else {
                        int i5 = l2.a;
                        if (i5 == -1) {
                            kVarArr = e(kVarArr, l2);
                        } else {
                            synchronized (l2) {
                                try {
                                    if (l(kVarArr, i4) == l2) {
                                        if (i5 >= 0) {
                                            k kVar2 = null;
                                            k kVar3 = l2;
                                            i2 = 1;
                                            while (true) {
                                                if (kVar3.a == j2 && ((obj2 = kVar3.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                                                    break;
                                                }
                                                k kVar4 = kVar3.d;
                                                if (kVar4 == null) {
                                                    Object apply = biFunction.apply(obj, null);
                                                    if (apply != null) {
                                                        kVar3.d = new k(j2, obj, apply, null);
                                                        obj3 = apply;
                                                    } else {
                                                        obj3 = apply;
                                                    }
                                                } else {
                                                    i2++;
                                                    kVar2 = kVar3;
                                                    kVar3 = kVar4;
                                                }
                                            }
                                            Object apply2 = biFunction.apply(obj, kVar3.c);
                                            if (apply2 != null) {
                                                kVar3.c = apply2;
                                                obj3 = apply2;
                                            } else {
                                                k kVar5 = kVar3.d;
                                                if (kVar2 != null) {
                                                    kVar2.d = kVar5;
                                                } else {
                                                    i(kVarArr, i4, kVar5);
                                                }
                                                obj3 = apply2;
                                                i3 = -1;
                                            }
                                        } else if (l2 instanceof p) {
                                            p pVar = (p) l2;
                                            q qVar = pVar.e;
                                            q b = qVar != null ? qVar.b(j2, obj, null) : null;
                                            Object apply3 = biFunction.apply(obj, b == null ? null : b.c);
                                            if (apply3 != null) {
                                                if (b != null) {
                                                    b.c = apply3;
                                                } else {
                                                    pVar.f(j2, obj, apply3);
                                                    obj3 = apply3;
                                                    i2 = 1;
                                                    i3 = 1;
                                                }
                                            } else if (b != null) {
                                                if (pVar.g(b)) {
                                                    i(kVarArr, i4, q(pVar.f));
                                                }
                                                obj3 = apply3;
                                                i2 = 1;
                                                i3 = -1;
                                            }
                                            obj3 = apply3;
                                            i2 = 1;
                                        }
                                    }
                                } finally {
                                }
                            }
                            if (i2 != 0) {
                                if (i2 >= 8) {
                                    o(kVarArr, i4);
                                }
                            }
                        }
                    }
                }
            }
            kVarArr = f();
        }
        if (i3 != 0) {
            a(i3, i2);
        }
        return obj3;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ Object compute(Object obj, java.util.function.BiFunction biFunction) {
        return compute(obj, BiFunction.VivifiedWrapper.convert(biFunction));
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0078, code lost:
    
        r5 = r5.c;
     */
    @Override // j$.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object computeIfAbsent(Object obj, Function function) {
        q b;
        Object obj2;
        if (obj == null) {
            throw null;
        }
        if (function == null) {
            throw null;
        }
        int j2 = j(obj.hashCode());
        k[] kVarArr = this.a;
        Object obj3 = null;
        int i2 = 0;
        while (true) {
            if (kVarArr != null) {
                int length = kVarArr.length;
                if (length != 0) {
                    int i3 = (length - 1) & j2;
                    k l2 = l(kVarArr, i3);
                    boolean z = true;
                    if (l2 == null) {
                        l lVar = new l();
                        synchronized (lVar) {
                            try {
                                if (b(kVarArr, i3, lVar)) {
                                    try {
                                        obj3 = function.apply(obj);
                                        i(kVarArr, i3, obj3 != null ? new k(j2, obj, obj3, null) : null);
                                        i2 = 1;
                                    } catch (Throwable th) {
                                        i(kVarArr, i3, null);
                                        throw th;
                                    }
                                }
                            } finally {
                            }
                        }
                        if (i2 != 0) {
                        }
                    } else {
                        int i4 = l2.a;
                        if (i4 == -1) {
                            kVarArr = e(kVarArr, l2);
                        } else {
                            synchronized (l2) {
                                try {
                                    if (l(kVarArr, i3) == l2) {
                                        if (i4 >= 0) {
                                            k kVar = l2;
                                            i2 = 1;
                                            while (true) {
                                                if (kVar.a == j2 && ((obj2 = kVar.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                                                    break;
                                                }
                                                k kVar2 = kVar.d;
                                                if (kVar2 == null) {
                                                    Object apply = function.apply(obj);
                                                    if (apply != null) {
                                                        kVar.d = new k(j2, obj, apply, null);
                                                        obj3 = apply;
                                                    } else {
                                                        obj3 = apply;
                                                    }
                                                } else {
                                                    i2++;
                                                    kVar = kVar2;
                                                }
                                            }
                                        } else if (l2 instanceof p) {
                                            p pVar = (p) l2;
                                            q qVar = pVar.e;
                                            if (qVar == null || (b = qVar.b(j2, obj, null)) == null) {
                                                obj3 = function.apply(obj);
                                                if (obj3 != null) {
                                                    pVar.f(j2, obj, obj3);
                                                    i2 = 2;
                                                }
                                            } else {
                                                obj3 = b.c;
                                            }
                                            i2 = 2;
                                        }
                                    }
                                    z = false;
                                } finally {
                                }
                            }
                            if (i2 != 0) {
                                if (i2 >= 8) {
                                    o(kVarArr, i3);
                                }
                                if (!z) {
                                    return obj3;
                                }
                            }
                        }
                    }
                }
            }
            kVarArr = f();
        }
        if (obj3 != null) {
            a(1L, i2);
        }
        return obj3;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ Object computeIfAbsent(Object obj, java.util.function.Function function) {
        return computeIfAbsent(obj, Function.VivifiedWrapper.convert(function));
    }

    @Override // j$.util.Map
    public final Object computeIfPresent(Object obj, BiFunction biFunction) {
        q b;
        Object obj2;
        if (obj == null) {
            throw null;
        }
        if (biFunction == null) {
            throw null;
        }
        int j2 = j(obj.hashCode());
        k[] kVarArr = this.a;
        int i2 = 0;
        Object obj3 = null;
        int i3 = 0;
        while (true) {
            if (kVarArr != null) {
                int length = kVarArr.length;
                if (length != 0) {
                    int i4 = (length - 1) & j2;
                    k l2 = l(kVarArr, i4);
                    if (l2 == null) {
                        break;
                    }
                    int i5 = l2.a;
                    if (i5 == -1) {
                        kVarArr = e(kVarArr, l2);
                    } else {
                        synchronized (l2) {
                            try {
                                if (l(kVarArr, i4) == l2) {
                                    if (i5 >= 0) {
                                        i3 = 1;
                                        k kVar = null;
                                        k kVar2 = l2;
                                        while (true) {
                                            if (kVar2.a == j2 && ((obj2 = kVar2.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                                                break;
                                            }
                                            k kVar3 = kVar2.d;
                                            if (kVar3 == null) {
                                                break;
                                            }
                                            i3++;
                                            kVar = kVar2;
                                            kVar2 = kVar3;
                                        }
                                        obj3 = biFunction.apply(obj, kVar2.c);
                                        if (obj3 != null) {
                                            kVar2.c = obj3;
                                        } else {
                                            k kVar4 = kVar2.d;
                                            if (kVar != null) {
                                                kVar.d = kVar4;
                                            } else {
                                                i(kVarArr, i4, kVar4);
                                            }
                                            i2 = -1;
                                        }
                                    } else if (l2 instanceof p) {
                                        p pVar = (p) l2;
                                        q qVar = pVar.e;
                                        if (qVar != null && (b = qVar.b(j2, obj, null)) != null) {
                                            obj3 = biFunction.apply(obj, b.c);
                                            if (obj3 != null) {
                                                b.c = obj3;
                                            } else {
                                                if (pVar.g(b)) {
                                                    i(kVarArr, i4, q(pVar.f));
                                                }
                                                i2 = -1;
                                            }
                                        }
                                        i3 = 2;
                                    }
                                }
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                        if (i3 != 0) {
                            break;
                        }
                    }
                }
            }
            kVarArr = f();
        }
        if (i2 != 0) {
            a(i2, i3);
        }
        return obj3;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ Object computeIfPresent(Object obj, java.util.function.BiFunction biFunction) {
        return computeIfPresent(obj, BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsValue(Object obj) {
        obj.getClass();
        k[] kVarArr = this.a;
        if (kVarArr != null) {
            o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
            while (true) {
                k b = oVar.b();
                if (b == null) {
                    break;
                }
                Object obj2 = b.c;
                if (obj2 == obj) {
                    return true;
                }
                if (obj2 != null && obj.equals(obj2)) {
                    return true;
                }
            }
        }
        return false;
    }

    final k[] e(k[] kVarArr, k kVar) {
        k[] kVarArr2;
        int i2;
        if (!(kVar instanceof f) || (kVarArr2 = ((f) kVar).e) == null) {
            return this.a;
        }
        int numberOfLeadingZeros = Integer.numberOfLeadingZeros(kVarArr.length) | 32768;
        while (true) {
            if (kVarArr2 != this.b || this.a != kVarArr || (i2 = this.sizeCtl) >= 0 || (i2 >>> h) != numberOfLeadingZeros || i2 == numberOfLeadingZeros + 1 || i2 == g + numberOfLeadingZeros || this.transferIndex <= 0) {
                break;
            }
            if (j.compareAndSwapInt(this, k, i2, i2 + 1)) {
                n(kVarArr, kVarArr2);
                break;
            }
        }
        return kVarArr2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        h hVar = this.f;
        if (hVar != null) {
            return hVar;
        }
        h hVar2 = new h(this, 1);
        this.f = hVar2;
        return hVar2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean equals(Object obj) {
        V value;
        V v;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        k[] kVarArr = this.a;
        int length = kVarArr == null ? 0 : kVarArr.length;
        o oVar = new o(kVarArr, length, 0, length);
        while (true) {
            k b = oVar.b();
            if (b == null) {
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    K key = entry.getKey();
                    if (key == null || (value = entry.getValue()) == null || (v = get(key)) == null || (value != v && !value.equals(v))) {
                        return false;
                    }
                }
                return true;
            }
            Object obj2 = b.c;
            Object obj3 = map.get(b.b);
            if (obj3 == null || (obj3 != obj2 && !obj3.equals(obj2))) {
                break;
            }
        }
        return false;
    }

    @Override // j$.util.Map
    public final void forEach(BiConsumer biConsumer) {
        biConsumer.getClass();
        k[] kVarArr = this.a;
        if (kVarArr == null) {
            return;
        }
        o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
        while (true) {
            k b = oVar.b();
            if (b == null) {
                return;
            } else {
                biConsumer.accept(b.b, b.c);
            }
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ void forEach(java.util.function.BiConsumer biConsumer) {
        forEach(BiConsumer.VivifiedWrapper.convert(biConsumer));
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0055, code lost:
    
        r7 = r6.c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0057, code lost:
    
        if (r11 != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0059, code lost:
    
        r6.c = r10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    final Object g(Object obj, Object obj2, boolean z) {
        Object obj3;
        Object obj4;
        if (obj == null) {
            throw null;
        }
        if (obj2 == null) {
            throw null;
        }
        int j2 = j(obj.hashCode());
        k[] kVarArr = this.a;
        int i2 = 0;
        while (true) {
            if (kVarArr != null) {
                int length = kVarArr.length;
                if (length != 0) {
                    int i3 = (length - 1) & j2;
                    k l2 = l(kVarArr, i3);
                    if (l2 != null) {
                        int i4 = l2.a;
                        if (i4 == -1) {
                            kVarArr = e(kVarArr, l2);
                        } else {
                            synchronized (l2) {
                                try {
                                    if (l(kVarArr, i3) == l2) {
                                        if (i4 >= 0) {
                                            i2 = 1;
                                            k kVar = l2;
                                            while (true) {
                                                if (kVar.a == j2 && ((obj4 = kVar.b) == obj || (obj4 != null && obj.equals(obj4)))) {
                                                    break;
                                                }
                                                k kVar2 = kVar.d;
                                                if (kVar2 == null) {
                                                    kVar.d = new k(j2, obj, obj2, null);
                                                    break;
                                                }
                                                i2++;
                                                kVar = kVar2;
                                            }
                                        } else if (l2 instanceof p) {
                                            q f = ((p) l2).f(j2, obj, obj2);
                                            if (f != null) {
                                                obj3 = f.c;
                                                if (!z) {
                                                    f.c = obj2;
                                                }
                                            } else {
                                                obj3 = null;
                                            }
                                            i2 = 2;
                                        }
                                    }
                                    obj3 = null;
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                            if (i2 != 0) {
                                if (i2 >= 8) {
                                    o(kVarArr, i3);
                                }
                                if (obj3 != null) {
                                    return obj3;
                                }
                            }
                        }
                    } else if (b(kVarArr, i3, new k(j2, obj, obj2, null))) {
                        break;
                    }
                }
            }
            kVarArr = f();
        }
        a(1L, i2);
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x004d, code lost:
    
        return (V) r1.c;
     */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public V get(Object obj) {
        int length;
        k l2;
        Object obj2;
        int j2 = j(obj.hashCode());
        k[] kVarArr = this.a;
        if (kVarArr != null && (length = kVarArr.length) > 0 && (l2 = l(kVarArr, (length - 1) & j2)) != null) {
            int i2 = l2.a;
            if (i2 == j2) {
                Object obj3 = l2.b;
                if (obj3 == obj || (obj3 != null && obj.equals(obj3))) {
                    return (V) l2.c;
                }
            } else if (i2 < 0) {
                k a = l2.a(obj, j2);
                if (a != null) {
                    return (V) a.c;
                }
                return null;
            }
            while (true) {
                l2 = l2.d;
                if (l2 == null) {
                    break;
                }
                if (l2.a != j2 || ((obj2 = l2.b) != obj && (obj2 == null || !obj.equals(obj2)))) {
                }
            }
        }
        return null;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final Object getOrDefault(Object obj, Object obj2) {
        V v = get(obj);
        return v == null ? obj2 : v;
    }

    final Object h(Object obj, Object obj2, Object obj3) {
        int length;
        int i2;
        k l2;
        boolean z;
        Object obj4;
        q b;
        k q2;
        Object obj5;
        int j2 = j(obj.hashCode());
        k[] kVarArr = this.a;
        while (true) {
            if (kVarArr == null || (length = kVarArr.length) == 0 || (l2 = l(kVarArr, (i2 = (length - 1) & j2))) == null) {
                break;
            }
            int i3 = l2.a;
            if (i3 == -1) {
                kVarArr = e(kVarArr, l2);
            } else {
                synchronized (l2) {
                    try {
                        if (l(kVarArr, i2) == l2) {
                            z = true;
                            if (i3 >= 0) {
                                k kVar = null;
                                k kVar2 = l2;
                                while (true) {
                                    if (kVar2.a == j2 && ((obj5 = kVar2.b) == obj || (obj5 != null && obj.equals(obj5)))) {
                                        break;
                                    }
                                    k kVar3 = kVar2.d;
                                    if (kVar3 == null) {
                                        break;
                                    }
                                    kVar = kVar2;
                                    kVar2 = kVar3;
                                }
                                obj4 = kVar2.c;
                                if (obj3 == null || obj3 == obj4 || (obj4 != null && obj3.equals(obj4))) {
                                    if (obj2 != null) {
                                        kVar2.c = obj2;
                                    } else if (kVar != null) {
                                        kVar.d = kVar2.d;
                                    } else {
                                        q2 = kVar2.d;
                                        i(kVarArr, i2, q2);
                                    }
                                }
                                obj4 = null;
                            } else if (l2 instanceof p) {
                                p pVar = (p) l2;
                                q qVar = pVar.e;
                                if (qVar != null && (b = qVar.b(j2, obj, null)) != null) {
                                    obj4 = b.c;
                                    if (obj3 == null || obj3 == obj4 || (obj4 != null && obj3.equals(obj4))) {
                                        if (obj2 != null) {
                                            b.c = obj2;
                                        } else if (pVar.g(b)) {
                                            q2 = q(pVar.f);
                                            i(kVarArr, i2, q2);
                                        }
                                    }
                                }
                                obj4 = null;
                            }
                        }
                        z = false;
                        obj4 = null;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                if (z) {
                    if (obj4 != null) {
                        if (obj2 == null) {
                            a(-1L, -1);
                        }
                        return obj4;
                    }
                }
            }
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int hashCode() {
        k[] kVarArr = this.a;
        int i2 = 0;
        if (kVarArr != null) {
            o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
            while (true) {
                k b = oVar.b();
                if (b == null) {
                    break;
                }
                i2 += b.c.hashCode() ^ b.b.hashCode();
            }
        }
        return i2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return k() <= 0;
    }

    final long k() {
        c[] cVarArr = this.c;
        long j2 = this.baseCount;
        if (cVarArr != null) {
            for (c cVar : cVarArr) {
                if (cVar != null) {
                    j2 += cVar.value;
                }
            }
        }
        return j2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        h hVar = this.d;
        if (hVar != null) {
            return hVar;
        }
        h hVar2 = new h(this, 0);
        this.d = hVar2;
        return hVar2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0064, code lost:
    
        r12 = r20.apply(r9.c, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x006a, code lost:
    
        if (r12 == null) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x006c, code lost:
    
        r9.c = r12;
        r9 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0071, code lost:
    
        r8 = r9.d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0073, code lost:
    
        if (r13 == null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0075, code lost:
    
        r13.d = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x007b, code lost:
    
        r9 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0078, code lost:
    
        i(r6, r10, r8);
     */
    @Override // j$.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object merge(Object obj, Object obj2, BiFunction biFunction) {
        int i2;
        Object obj3;
        Object obj4 = obj2;
        if (obj == null) {
            throw null;
        }
        if (obj4 == null) {
            throw null;
        }
        if (biFunction == null) {
            throw null;
        }
        int j2 = j(obj.hashCode());
        k[] kVarArr = this.a;
        int i3 = 0;
        Object obj5 = null;
        int i4 = 0;
        while (true) {
            if (kVarArr != null) {
                int length = kVarArr.length;
                if (length != 0) {
                    int i5 = (length - 1) & j2;
                    k l2 = l(kVarArr, i5);
                    i2 = 1;
                    if (l2 != null) {
                        int i6 = l2.a;
                        if (i6 == -1) {
                            kVarArr = e(kVarArr, l2);
                        } else {
                            synchronized (l2) {
                                try {
                                    if (l(kVarArr, i5) == l2) {
                                        if (i6 >= 0) {
                                            k kVar = null;
                                            k kVar2 = l2;
                                            i3 = 1;
                                            while (true) {
                                                if (kVar2.a == j2 && ((obj3 = kVar2.b) == obj || (obj3 != null && obj.equals(obj3)))) {
                                                    break;
                                                }
                                                k kVar3 = kVar2.d;
                                                if (kVar3 == null) {
                                                    kVar2.d = new k(j2, obj, obj4, null);
                                                    obj5 = obj4;
                                                    break;
                                                }
                                                i3++;
                                                kVar = kVar2;
                                                kVar2 = kVar3;
                                            }
                                            i4 = 1;
                                        } else if (l2 instanceof p) {
                                            p pVar = (p) l2;
                                            q qVar = pVar.e;
                                            q b = qVar == null ? null : qVar.b(j2, obj, null);
                                            Object apply = b == null ? obj4 : biFunction.apply(b.c, obj4);
                                            if (apply != null) {
                                                if (b != null) {
                                                    b.c = apply;
                                                } else {
                                                    pVar.f(j2, obj, apply);
                                                    obj5 = apply;
                                                    i3 = 2;
                                                    i4 = 1;
                                                }
                                            } else if (b != null) {
                                                if (pVar.g(b)) {
                                                    i(kVarArr, i5, q(pVar.f));
                                                }
                                                obj5 = apply;
                                                i3 = 2;
                                                i4 = -1;
                                            }
                                            obj5 = apply;
                                            i3 = 2;
                                        }
                                    }
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                            if (i3 != 0) {
                                if (i3 >= 8) {
                                    o(kVarArr, i5);
                                }
                                i2 = i4;
                                obj4 = obj5;
                            }
                        }
                    } else if (b(kVarArr, i5, new k(j2, obj, obj4, null))) {
                        break;
                    }
                }
            }
            kVarArr = f();
        }
        if (i2 != 0) {
            a(i2, i3);
        }
        return obj4;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ Object merge(Object obj, Object obj2, java.util.function.BiFunction biFunction) {
        return merge(obj, obj2, BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k2, V v) {
        return (V) g(k2, v, false);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        p(map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            g(entry.getKey(), entry.getValue(), false);
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public V putIfAbsent(K k2, V v) {
        return (V) g(k2, v, true);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        return (V) h(obj, null, null);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public boolean remove(Object obj, Object obj2) {
        obj.getClass();
        return (obj2 == null || h(obj, null, obj2) == null) ? false : true;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final Object replace(Object obj, Object obj2) {
        if (obj == null) {
            throw null;
        }
        if (obj2 != null) {
            return h(obj, obj2, null);
        }
        throw null;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final boolean replace(Object obj, Object obj2, Object obj3) {
        if (obj == null || obj2 == null || obj3 == null) {
            throw null;
        }
        return h(obj, obj3, obj2) != null;
    }

    @Override // j$.util.Map
    public final void replaceAll(BiFunction biFunction) {
        biFunction.getClass();
        k[] kVarArr = this.a;
        if (kVarArr == null) {
            return;
        }
        o oVar = new o(kVarArr, kVarArr.length, 0, kVarArr.length);
        while (true) {
            k b = oVar.b();
            if (b == null) {
                return;
            }
            Object obj = b.c;
            Object obj2 = b.b;
            do {
                Object apply = biFunction.apply(obj2, obj);
                apply.getClass();
                if (h(obj2, apply, obj) == null) {
                    obj = get(obj2);
                }
            } while (obj != null);
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ void replaceAll(java.util.function.BiFunction biFunction) {
        replaceAll(BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        long k2 = k();
        if (k2 < 0) {
            return 0;
        }
        return k2 > 2147483647L ? ConnectionsManager.DEFAULT_DATACENTER_ID : (int) k2;
    }

    @Override // java.util.AbstractMap
    public final String toString() {
        k[] kVarArr = this.a;
        int length = kVarArr == null ? 0 : kVarArr.length;
        o oVar = new o(kVarArr, length, 0, length);
        StringBuilder sb = new StringBuilder("{");
        k b = oVar.b();
        if (b != null) {
            while (true) {
                Object obj = b.b;
                Object obj2 = b.c;
                if (obj == this) {
                    obj = "(this Map)";
                }
                sb.append(obj);
                sb.append('=');
                if (obj2 == this) {
                    obj2 = "(this Map)";
                }
                sb.append(obj2);
                b = oVar.b();
                if (b == null) {
                    break;
                }
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        r rVar = this.e;
        if (rVar != null) {
            return rVar;
        }
        r rVar2 = new r(this);
        this.e = rVar2;
        return rVar2;
    }
}
