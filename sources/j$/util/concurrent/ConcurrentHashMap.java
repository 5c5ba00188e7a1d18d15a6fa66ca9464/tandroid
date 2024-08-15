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
import org.telegram.messenger.LiteMode;
import org.telegram.tgnet.ConnectionsManager;
import sun.misc.Unsafe;
/* loaded from: classes2.dex */
public class ConcurrentHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable, v {
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
    volatile transient m[] a;
    private volatile transient m[] b;
    private volatile transient long baseCount;
    private volatile transient d[] c;
    private volatile transient int cellsBusy;
    private transient j d;
    private transient t e;
    private transient f f;
    private volatile transient int sizeCtl;
    private volatile transient int transferIndex;

    static {
        Class cls = Integer.TYPE;
        serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("segments", o[].class), new ObjectStreamField("segmentMask", cls), new ObjectStreamField("segmentShift", cls)};
        try {
            Unsafe c = w.c();
            j = c;
            k = c.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("sizeCtl"));
            l = c.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("transferIndex"));
            m = c.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("baseCount"));
            n = c.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("cellsBusy"));
            o = c.objectFieldOffset(d.class.getDeclaredField("value"));
            p = c.arrayBaseOffset(m[].class);
            int arrayIndexScale = c.arrayIndexScale(m[].class);
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
        this.sizeCtl = i2 >= 536870912 ? 1073741824 : n(i2 + (i2 >>> 1) + 1);
    }

    public ConcurrentHashMap(int i2, float f, int i3) {
        if (f <= 0.0f || i2 < 0 || i3 <= 0) {
            throw new IllegalArgumentException();
        }
        double d = (i2 < i3 ? i3 : i2) / f;
        Double.isNaN(d);
        long j2 = (long) (d + 1.0d);
        this.sizeCtl = j2 >= 1073741824 ? 1073741824 : n((int) j2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0012, code lost:
        if (r1.compareAndSwapLong(r11, r3, r5, r9) == false) goto L53;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void a(long j2, int i2) {
        int length;
        d dVar;
        long l2;
        m[] mVarArr;
        int length2;
        m[] mVarArr2;
        d[] dVarArr = this.c;
        if (dVarArr == null) {
            Unsafe unsafe = j;
            long j3 = m;
            long j4 = this.baseCount;
            l2 = j4 + j2;
        }
        boolean z = true;
        if (dVarArr != null && (length = dVarArr.length - 1) >= 0 && (dVar = dVarArr[length & ThreadLocalRandom.b()]) != null) {
            Unsafe unsafe2 = j;
            long j5 = o;
            long j6 = dVar.value;
            boolean compareAndSwapLong = unsafe2.compareAndSwapLong(dVar, j5, j6, j6 + j2);
            if (!compareAndSwapLong) {
                z = compareAndSwapLong;
            } else if (i2 <= 1) {
                return;
            } else {
                l2 = l();
                if (i2 < 0) {
                    return;
                }
                while (true) {
                    int i3 = this.sizeCtl;
                    if (l2 < i3 || (mVarArr = this.a) == null || (length2 = mVarArr.length) >= 1073741824) {
                        return;
                    }
                    int numberOfLeadingZeros = Integer.numberOfLeadingZeros(length2) | LiteMode.FLAG_CHAT_SCALE;
                    int i4 = h;
                    if (i3 < 0) {
                        if ((i3 >>> i4) != numberOfLeadingZeros || i3 == numberOfLeadingZeros + 1 || i3 == numberOfLeadingZeros + g || (mVarArr2 = this.b) == null || this.transferIndex <= 0) {
                            return;
                        }
                        if (j.compareAndSwapInt(this, k, i3, i3 + 1)) {
                            o(mVarArr, mVarArr2);
                        }
                    } else if (j.compareAndSwapInt(this, k, i3, (numberOfLeadingZeros << i4) + 2)) {
                        o(mVarArr, null);
                    }
                    l2 = l();
                }
            }
        }
        e(j2, z);
    }

    static final boolean b(m[] mVarArr, int i2, m mVar) {
        long j2 = p;
        return a.a(j, mVarArr, j2 + (i2 << q), mVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Class c(Object obj) {
        Type[] actualTypeArguments;
        if (obj instanceof Comparable) {
            Class<?> cls = obj.getClass();
            if (cls == String.class) {
                return cls;
            }
            Type[] genericInterfaces = cls.getGenericInterfaces();
            if (genericInterfaces != null) {
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
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int d(Class cls, Object obj, Object obj2) {
        if (obj2 == null || obj2.getClass() != cls) {
            return 0;
        }
        return ((Comparable) obj).compareTo(obj2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x009d, code lost:
        if (r24.c != r7) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x009f, code lost:
        r1 = new j$.util.concurrent.d[r8 << 1];
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00a4, code lost:
        if (r2 >= r8) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00a6, code lost:
        r1[r2] = r7[r2];
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00ad, code lost:
        r24.c = r1;
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x001b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0102 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void e(long j2, boolean z) {
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
                d[] dVarArr = this.c;
                if (dVarArr != null && (length = dVarArr.length) > 0) {
                    d dVar = dVarArr[(length - 1) & i3];
                    if (dVar != null) {
                        if (z2) {
                            Unsafe unsafe = j;
                            long j3 = o;
                            long j4 = dVar.value;
                            if (unsafe.compareAndSwapLong(dVar, j3, j4, j4 + j2)) {
                                return;
                            }
                            if (this.c == dVarArr && length < i) {
                                if (!z5) {
                                    z5 = true;
                                } else if (this.cellsBusy == 0 && unsafe.compareAndSwapInt(this, n, 0, 1)) {
                                    try {
                                        break;
                                    } finally {
                                    }
                                }
                            }
                        } else {
                            z2 = true;
                        }
                        i3 = ThreadLocalRandom.a(i3);
                    } else if (this.cellsBusy == 0) {
                        d dVar2 = new d(j2);
                        if (this.cellsBusy == 0 && j.compareAndSwapInt(this, n, 0, 1)) {
                            try {
                                d[] dVarArr2 = this.c;
                                if (dVarArr2 != null && (length2 = dVarArr2.length) > 0) {
                                    int i4 = (length2 - 1) & i3;
                                    if (dVarArr2[i4] == null) {
                                        dVarArr2[i4] = dVar2;
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
                } else if (this.cellsBusy == 0 && this.c == dVarArr && j.compareAndSwapInt(this, n, 0, 1)) {
                    try {
                        if (this.c == dVarArr) {
                            d[] dVarArr3 = new d[2];
                            dVarArr3[i3 & 1] = new d(j2);
                            this.c = dVarArr3;
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

    private final m[] g() {
        while (true) {
            m[] mVarArr = this.a;
            if (mVarArr != null && mVarArr.length != 0) {
                return mVarArr;
            }
            int i2 = this.sizeCtl;
            if (i2 < 0) {
                Thread.yield();
            } else if (j.compareAndSwapInt(this, k, i2, -1)) {
                try {
                    m[] mVarArr2 = this.a;
                    if (mVarArr2 == null || mVarArr2.length == 0) {
                        int i3 = i2 > 0 ? i2 : 16;
                        m[] mVarArr3 = new m[i3];
                        this.a = mVarArr3;
                        i2 = i3 - (i3 >>> 2);
                        mVarArr2 = mVarArr3;
                    }
                    this.sizeCtl = i2;
                    return mVarArr2;
                } catch (Throwable th) {
                    this.sizeCtl = i2;
                    throw th;
                }
            }
        }
    }

    static final void j(m[] mVarArr, int i2, m mVar) {
        j.putObjectVolatile(mVarArr, (i2 << q) + p, mVar);
    }

    static final int k(int i2) {
        return (i2 ^ (i2 >>> 16)) & ConnectionsManager.DEFAULT_DATACENTER_ID;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final m m(m[] mVarArr, int i2) {
        return (m) j.getObjectVolatile(mVarArr, (i2 << q) + p);
    }

    private static final int n(int i2) {
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
    /* JADX WARN: Type inference failed for: r15v15, types: [j$.util.concurrent.m] */
    /* JADX WARN: Type inference failed for: r15v17, types: [j$.util.concurrent.m] */
    /* JADX WARN: Type inference failed for: r6v17, types: [j$.util.concurrent.m] */
    /* JADX WARN: Type inference failed for: r6v22, types: [j$.util.concurrent.m] */
    private final void o(m[] mVarArr, m[] mVarArr2) {
        m[] mVarArr3;
        ConcurrentHashMap<K, V> concurrentHashMap;
        m[] mVarArr4;
        int i2;
        int i3;
        h hVar;
        ConcurrentHashMap<K, V> concurrentHashMap2;
        int i4;
        s sVar;
        s sVar2;
        ConcurrentHashMap<K, V> concurrentHashMap3 = this;
        int length = mVarArr.length;
        int i5 = i;
        int i6 = i5 > 1 ? (length >>> 3) / i5 : length;
        int i7 = i6 < 16 ? 16 : i6;
        if (mVarArr2 == null) {
            try {
                m[] mVarArr5 = new m[length << 1];
                concurrentHashMap3.b = mVarArr5;
                concurrentHashMap3.transferIndex = length;
                mVarArr3 = mVarArr5;
            } catch (Throwable unused) {
                concurrentHashMap3.sizeCtl = ConnectionsManager.DEFAULT_DATACENTER_ID;
                return;
            }
        } else {
            mVarArr3 = mVarArr2;
        }
        int length2 = mVarArr3.length;
        h hVar2 = new h(mVarArr3);
        m[] mVarArr6 = mVarArr;
        ConcurrentHashMap<K, V> concurrentHashMap4 = concurrentHashMap3;
        int i8 = 0;
        int i9 = 0;
        boolean z = true;
        boolean z2 = false;
        while (true) {
            if (z) {
                int i10 = i8 - 1;
                if (i10 >= i9 || z2) {
                    concurrentHashMap = concurrentHashMap4;
                    mVarArr4 = mVarArr6;
                    i8 = i10;
                    i9 = i9;
                } else {
                    int i11 = concurrentHashMap4.transferIndex;
                    if (i11 <= 0) {
                        concurrentHashMap = concurrentHashMap4;
                        mVarArr4 = mVarArr6;
                        i8 = -1;
                    } else {
                        Unsafe unsafe = j;
                        long j2 = l;
                        int i12 = i11 > i7 ? i11 - i7 : 0;
                        concurrentHashMap = concurrentHashMap4;
                        mVarArr4 = mVarArr6;
                        int i13 = i9;
                        if (unsafe.compareAndSwapInt(this, j2, i11, i12)) {
                            i8 = i11 - 1;
                            i9 = i12;
                        } else {
                            mVarArr6 = mVarArr4;
                            i8 = i10;
                            i9 = i13;
                            concurrentHashMap4 = concurrentHashMap;
                        }
                    }
                }
                mVarArr6 = mVarArr4;
                concurrentHashMap4 = concurrentHashMap;
                z = false;
            } else {
                ConcurrentHashMap<K, V> concurrentHashMap5 = concurrentHashMap4;
                m[] mVarArr7 = mVarArr6;
                int i14 = i9;
                s sVar3 = null;
                if (i8 < 0 || i8 >= length || (i4 = i8 + length) >= length2) {
                    i2 = i7;
                    i3 = length2;
                    hVar = hVar2;
                    if (z2) {
                        this.b = null;
                        this.a = mVarArr3;
                        this.sizeCtl = (length << 1) - (length >>> 1);
                        return;
                    }
                    concurrentHashMap2 = this;
                    Unsafe unsafe2 = j;
                    long j3 = k;
                    int i15 = concurrentHashMap2.sizeCtl;
                    int i16 = i8;
                    if (!unsafe2.compareAndSwapInt(this, j3, i15, i15 - 1)) {
                        concurrentHashMap4 = concurrentHashMap2;
                        mVarArr6 = mVarArr7;
                        i8 = i16;
                    } else if (i15 - 2 != ((Integer.numberOfLeadingZeros(length) | LiteMode.FLAG_CHAT_SCALE) << h)) {
                        return;
                    } else {
                        i8 = length;
                        concurrentHashMap4 = concurrentHashMap2;
                        mVarArr6 = mVarArr7;
                        z = true;
                        z2 = true;
                    }
                } else {
                    m m2 = m(mVarArr7, i8);
                    if (m2 == null) {
                        z = b(mVarArr7, i8, hVar2);
                        i2 = i7;
                        i3 = length2;
                        hVar = hVar2;
                        mVarArr6 = mVarArr7;
                        concurrentHashMap4 = concurrentHashMap5;
                    } else {
                        int i17 = m2.a;
                        if (i17 == -1) {
                            concurrentHashMap2 = concurrentHashMap3;
                            i2 = i7;
                            i3 = length2;
                            hVar = hVar2;
                            mVarArr6 = mVarArr7;
                            concurrentHashMap4 = concurrentHashMap5;
                            z = true;
                        } else {
                            synchronized (m2) {
                                if (m(mVarArr7, i8) == m2) {
                                    if (i17 >= 0) {
                                        int i18 = i17 & length;
                                        s sVar4 = m2;
                                        for (s sVar5 = m2.d; sVar5 != null; sVar5 = sVar5.d) {
                                            int i19 = sVar5.a & length;
                                            if (i19 != i18) {
                                                sVar4 = sVar5;
                                                i18 = i19;
                                            }
                                        }
                                        if (i18 == 0) {
                                            sVar = sVar4;
                                        } else {
                                            sVar = null;
                                            sVar3 = sVar4;
                                        }
                                        m mVar = m2;
                                        while (mVar != sVar4) {
                                            int i20 = mVar.a;
                                            int i21 = i7;
                                            Object obj = mVar.b;
                                            int i22 = length2;
                                            Object obj2 = mVar.c;
                                            if ((i20 & length) == 0) {
                                                sVar2 = sVar4;
                                                sVar = new m(i20, obj, obj2, sVar);
                                            } else {
                                                sVar2 = sVar4;
                                                sVar3 = new m(i20, obj, obj2, sVar3);
                                            }
                                            mVar = mVar.d;
                                            i7 = i21;
                                            length2 = i22;
                                            sVar4 = sVar2;
                                        }
                                        i2 = i7;
                                        i3 = length2;
                                        j(mVarArr3, i8, sVar);
                                        j(mVarArr3, i4, sVar3);
                                        j(mVarArr7, i8, hVar2);
                                        hVar = hVar2;
                                    } else {
                                        i2 = i7;
                                        i3 = length2;
                                        if (m2 instanceof r) {
                                            r rVar = (r) m2;
                                            s sVar6 = null;
                                            s sVar7 = null;
                                            m mVar2 = rVar.f;
                                            int i23 = 0;
                                            int i24 = 0;
                                            s sVar8 = null;
                                            while (mVar2 != null) {
                                                r rVar2 = rVar;
                                                int i25 = mVar2.a;
                                                h hVar3 = hVar2;
                                                s sVar9 = new s(i25, mVar2.b, mVar2.c, null, null);
                                                if ((i25 & length) == 0) {
                                                    sVar9.h = sVar7;
                                                    if (sVar7 == null) {
                                                        sVar3 = sVar9;
                                                    } else {
                                                        sVar7.d = sVar9;
                                                    }
                                                    i23++;
                                                    sVar7 = sVar9;
                                                } else {
                                                    sVar9.h = sVar6;
                                                    if (sVar6 == null) {
                                                        sVar8 = sVar9;
                                                    } else {
                                                        sVar6.d = sVar9;
                                                    }
                                                    i24++;
                                                    sVar6 = sVar9;
                                                }
                                                mVar2 = mVar2.d;
                                                rVar = rVar2;
                                                hVar2 = hVar3;
                                            }
                                            r rVar3 = rVar;
                                            h hVar4 = hVar2;
                                            m r = i23 <= 6 ? r(sVar3) : i24 != 0 ? new r(sVar3) : rVar3;
                                            m r2 = i24 <= 6 ? r(sVar8) : i23 != 0 ? new r(sVar8) : rVar3;
                                            j(mVarArr3, i8, r);
                                            j(mVarArr3, i4, r2);
                                            hVar = hVar4;
                                            j(mVarArr, i8, hVar);
                                            mVarArr7 = mVarArr;
                                        }
                                    }
                                    z = true;
                                } else {
                                    i2 = i7;
                                    i3 = length2;
                                }
                                hVar = hVar2;
                            }
                            concurrentHashMap4 = this;
                            mVarArr6 = mVarArr7;
                        }
                    }
                    concurrentHashMap2 = this;
                }
                hVar2 = hVar;
                concurrentHashMap3 = concurrentHashMap2;
                i9 = i14;
                i7 = i2;
                length2 = i3;
            }
        }
    }

    private final void p(m[] mVarArr, int i2) {
        int length = mVarArr.length;
        if (length < 64) {
            q(length << 1);
            return;
        }
        m m2 = m(mVarArr, i2);
        if (m2 == null || m2.a < 0) {
            return;
        }
        synchronized (m2) {
            if (m(mVarArr, i2) == m2) {
                s sVar = null;
                m mVar = m2;
                s sVar2 = null;
                while (mVar != null) {
                    s sVar3 = new s(mVar.a, mVar.b, mVar.c, null, null);
                    sVar3.h = sVar2;
                    if (sVar2 == null) {
                        sVar = sVar3;
                    } else {
                        sVar2.d = sVar3;
                    }
                    mVar = mVar.d;
                    sVar2 = sVar3;
                }
                j(mVarArr, i2, new r(sVar));
            }
        }
    }

    private final void q(int i2) {
        int length;
        m[] mVarArr;
        int n2 = i2 >= 536870912 ? 1073741824 : n(i2 + (i2 >>> 1) + 1);
        while (true) {
            int i3 = this.sizeCtl;
            if (i3 < 0) {
                return;
            }
            m[] mVarArr2 = this.a;
            if (mVarArr2 == null || (length = mVarArr2.length) == 0) {
                int i4 = i3 > n2 ? i3 : n2;
                if (j.compareAndSwapInt(this, k, i3, -1)) {
                    try {
                        if (this.a == mVarArr2) {
                            this.a = new m[i4];
                            i3 = i4 - (i4 >>> 2);
                        }
                    } finally {
                        this.sizeCtl = i3;
                    }
                } else {
                    continue;
                }
            } else if (n2 <= i3 || length >= 1073741824) {
                return;
            } else {
                if (mVarArr2 == this.a) {
                    int numberOfLeadingZeros = Integer.numberOfLeadingZeros(length) | LiteMode.FLAG_CHAT_SCALE;
                    int i5 = h;
                    if (i3 < 0) {
                        if ((i3 >>> i5) != numberOfLeadingZeros || i3 == numberOfLeadingZeros + 1 || i3 == numberOfLeadingZeros + g || (mVarArr = this.b) == null || this.transferIndex <= 0) {
                            return;
                        }
                        if (j.compareAndSwapInt(this, k, i3, i3 + 1)) {
                            o(mVarArr2, mVarArr);
                        }
                    } else if (j.compareAndSwapInt(this, k, i3, (numberOfLeadingZeros << i5) + 2)) {
                        o(mVarArr2, null);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v2, types: [j$.util.concurrent.m] */
    static m r(s sVar) {
        m mVar = null;
        m mVar2 = null;
        for (s sVar2 = sVar; sVar2 != null; sVar2 = sVar2.d) {
            m mVar3 = new m(sVar2.a, sVar2.b, sVar2.c, null);
            if (mVar2 == null) {
                mVar = mVar3;
            } else {
                mVar2.d = mVar3;
            }
            mVar2 = mVar3;
        }
        return mVar;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        long j2;
        int n2;
        boolean z;
        Object obj;
        this.sizeCtl = -1;
        objectInputStream.defaultReadObject();
        long j3 = 0;
        long j4 = 0;
        m mVar = null;
        while (true) {
            Object readObject = objectInputStream.readObject();
            Object readObject2 = objectInputStream.readObject();
            j2 = 1;
            if (readObject == null || readObject2 == null) {
                break;
            }
            j4++;
            mVar = new m(k(readObject.hashCode()), readObject, readObject2, mVar);
        }
        if (j4 == 0) {
            this.sizeCtl = 0;
            return;
        }
        if (j4 >= 536870912) {
            n2 = 1073741824;
        } else {
            int i2 = (int) j4;
            n2 = n(i2 + (i2 >>> 1) + 1);
        }
        m[] mVarArr = new m[n2];
        int i3 = n2 - 1;
        while (mVar != null) {
            m mVar2 = mVar.d;
            int i4 = mVar.a;
            int i5 = i4 & i3;
            m m2 = m(mVarArr, i5);
            if (m2 == null) {
                z = true;
            } else {
                Object obj2 = mVar.b;
                if (m2.a >= 0) {
                    int i6 = 0;
                    for (m mVar3 = m2; mVar3 != null; mVar3 = mVar3.d) {
                        if (mVar3.a == i4 && ((obj = mVar3.b) == obj2 || (obj != null && obj2.equals(obj)))) {
                            z = false;
                            break;
                        }
                        i6++;
                    }
                    z = true;
                    if (z && i6 >= 8) {
                        long j5 = j3 + 1;
                        mVar.d = m2;
                        m mVar4 = mVar;
                        s sVar = null;
                        s sVar2 = null;
                        while (mVar4 != null) {
                            long j6 = j5;
                            s sVar3 = new s(mVar4.a, mVar4.b, mVar4.c, null, null);
                            sVar3.h = sVar2;
                            if (sVar2 == null) {
                                sVar = sVar3;
                            } else {
                                sVar2.d = sVar3;
                            }
                            mVar4 = mVar4.d;
                            sVar2 = sVar3;
                            j5 = j6;
                        }
                        j(mVarArr, i5, new r(sVar));
                        j3 = j5;
                    }
                } else if (((r) m2).f(i4, obj2, mVar.c) == null) {
                    j3 += j2;
                }
                z = false;
            }
            if (z) {
                j3++;
                mVar.d = m2;
                j(mVarArr, i5, mVar);
            }
            j2 = 1;
            mVar = mVar2;
        }
        this.a = mVarArr;
        this.sizeCtl = n2 - (n2 >>> 2);
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
        o[] oVarArr = new o[16];
        for (int i6 = 0; i6 < 16; i6++) {
            oVarArr[i6] = new o();
        }
        objectOutputStream.putFields().put("segments", oVarArr);
        objectOutputStream.putFields().put("segmentShift", i4);
        objectOutputStream.putFields().put("segmentMask", i5);
        objectOutputStream.writeFields();
        m[] mVarArr = this.a;
        if (mVarArr != null) {
            q qVar = new q(mVarArr, mVarArr.length, 0, mVarArr.length);
            while (true) {
                m f = qVar.f();
                if (f == null) {
                    break;
                }
                objectOutputStream.writeObject(f.b);
                objectOutputStream.writeObject(f.c);
            }
        }
        objectOutputStream.writeObject(null);
        objectOutputStream.writeObject(null);
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public void clear() {
        m m2;
        m[] mVarArr = this.a;
        long j2 = 0;
        loop0: while (true) {
            int i2 = 0;
            while (mVarArr != null && i2 < mVarArr.length) {
                m2 = m(mVarArr, i2);
                if (m2 == null) {
                    i2++;
                } else {
                    int i3 = m2.a;
                    if (i3 == -1) {
                        break;
                    }
                    synchronized (m2) {
                        if (m(mVarArr, i2) == m2) {
                            for (m mVar = i3 >= 0 ? m2 : m2 instanceof r ? ((r) m2).f : null; mVar != null; mVar = mVar.d) {
                                j2--;
                            }
                            j(mVarArr, i2, null);
                            i2++;
                        }
                    }
                }
            }
            mVarArr = f(mVarArr, m2);
        }
        if (j2 != 0) {
            a(j2, -1);
        }
    }

    @Override // j$.util.Map
    public final Object compute(Object obj, BiFunction biFunction) {
        int i2;
        m mVar;
        Object obj2;
        Object obj3;
        if (obj == null || biFunction == null) {
            throw null;
        }
        int k2 = k(obj.hashCode());
        m[] mVarArr = this.a;
        int i3 = 0;
        Object obj4 = null;
        int i4 = 0;
        while (true) {
            if (mVarArr != null) {
                int length = mVarArr.length;
                if (length != 0) {
                    int i5 = (length - 1) & k2;
                    m m2 = m(mVarArr, i5);
                    if (m2 == null) {
                        n nVar = new n();
                        synchronized (nVar) {
                            if (b(mVarArr, i5, nVar)) {
                                Object apply = biFunction.apply(obj, null);
                                if (apply != null) {
                                    mVar = new m(k2, obj, apply, null);
                                    i2 = 1;
                                } else {
                                    i2 = i3;
                                    mVar = null;
                                }
                                j(mVarArr, i5, mVar);
                                i3 = i2;
                                obj4 = apply;
                                i4 = 1;
                            }
                        }
                        if (i4 != 0) {
                        }
                    } else {
                        int i6 = m2.a;
                        if (i6 == -1) {
                            mVarArr = f(mVarArr, m2);
                        } else {
                            synchronized (m2) {
                                try {
                                    if (m(mVarArr, i5) == m2) {
                                        if (i6 >= 0) {
                                            m mVar2 = null;
                                            m mVar3 = m2;
                                            int i7 = 1;
                                            while (true) {
                                                if (mVar3.a != k2 || ((obj3 = mVar3.b) != obj && (obj3 == null || !obj.equals(obj3)))) {
                                                    m mVar4 = mVar3.d;
                                                    if (mVar4 == null) {
                                                        Object apply2 = biFunction.apply(obj, null);
                                                        if (apply2 != null) {
                                                            mVar3.d = new m(k2, obj, apply2, null);
                                                            obj2 = apply2;
                                                            i3 = 1;
                                                        } else {
                                                            obj2 = apply2;
                                                        }
                                                    } else {
                                                        i7++;
                                                        mVar2 = mVar3;
                                                        mVar3 = mVar4;
                                                    }
                                                }
                                            }
                                            obj2 = biFunction.apply(obj, mVar3.c);
                                            if (obj2 != null) {
                                                mVar3.c = obj2;
                                            } else {
                                                m mVar5 = mVar3.d;
                                                if (mVar2 != null) {
                                                    mVar2.d = mVar5;
                                                } else {
                                                    j(mVarArr, i5, mVar5);
                                                }
                                                i3 = -1;
                                            }
                                            i4 = i7;
                                            obj4 = obj2;
                                        } else if (m2 instanceof r) {
                                            r rVar = (r) m2;
                                            s sVar = rVar.e;
                                            s b = sVar != null ? sVar.b(k2, obj, null) : null;
                                            Object apply3 = biFunction.apply(obj, b == null ? null : b.c);
                                            if (apply3 == null) {
                                                if (b != null) {
                                                    if (rVar.g(b)) {
                                                        j(mVarArr, i5, r(rVar.f));
                                                    }
                                                    obj4 = apply3;
                                                    i3 = -1;
                                                    i4 = 1;
                                                }
                                                obj4 = apply3;
                                                i4 = 1;
                                            } else if (b != null) {
                                                b.c = apply3;
                                                obj4 = apply3;
                                                i4 = 1;
                                            } else {
                                                rVar.f(k2, obj, apply3);
                                                obj4 = apply3;
                                                i3 = 1;
                                                i4 = 1;
                                            }
                                        }
                                    }
                                } finally {
                                }
                            }
                            if (i4 != 0) {
                                if (i4 >= 8) {
                                    p(mVarArr, i5);
                                }
                            }
                        }
                    }
                }
            }
            mVarArr = g();
        }
        if (i3 != 0) {
            a(i3, i4);
        }
        return obj4;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ Object compute(Object obj, java.util.function.BiFunction biFunction) {
        return compute(obj, BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // j$.util.Map
    public final Object computeIfAbsent(Object obj, Function function) {
        s b;
        Object apply;
        Object obj2;
        Object obj3;
        if (obj == null || function == null) {
            throw null;
        }
        int k2 = k(obj.hashCode());
        m[] mVarArr = this.a;
        Object obj4 = null;
        int i2 = 0;
        while (true) {
            if (mVarArr != null) {
                int length = mVarArr.length;
                if (length != 0) {
                    int i3 = (length - 1) & k2;
                    m m2 = m(mVarArr, i3);
                    boolean z = true;
                    if (m2 == null) {
                        n nVar = new n();
                        synchronized (nVar) {
                            if (b(mVarArr, i3, nVar)) {
                                Object apply2 = function.apply(obj);
                                j(mVarArr, i3, apply2 != null ? new m(k2, obj, apply2, null) : null);
                                obj4 = apply2;
                                i2 = 1;
                            }
                        }
                        if (i2 != 0) {
                            break;
                        }
                    } else {
                        int i4 = m2.a;
                        if (i4 == -1) {
                            mVarArr = f(mVarArr, m2);
                        } else {
                            synchronized (m2) {
                                if (m(mVarArr, i3) == m2) {
                                    if (i4 >= 0) {
                                        m mVar = m2;
                                        int i5 = 1;
                                        while (true) {
                                            if (mVar.a != k2 || ((obj3 = mVar.b) != obj && (obj3 == null || !obj.equals(obj3)))) {
                                                m mVar2 = mVar.d;
                                                if (mVar2 == null) {
                                                    apply = function.apply(obj);
                                                    if (apply != null) {
                                                        mVar.d = new m(k2, obj, apply, null);
                                                    } else {
                                                        obj2 = apply;
                                                    }
                                                } else {
                                                    i5++;
                                                    mVar = mVar2;
                                                }
                                            }
                                        }
                                        obj2 = mVar.c;
                                        apply = obj2;
                                        z = false;
                                        i2 = i5;
                                        obj4 = apply;
                                    } else if (m2 instanceof r) {
                                        r rVar = (r) m2;
                                        s sVar = rVar.e;
                                        if (sVar == null || (b = sVar.b(k2, obj, null)) == null) {
                                            obj4 = function.apply(obj);
                                            if (obj4 != null) {
                                                rVar.f(k2, obj, obj4);
                                                i2 = 2;
                                            }
                                        } else {
                                            obj4 = b.c;
                                        }
                                        i2 = 2;
                                    }
                                }
                                z = false;
                            }
                            if (i2 != 0) {
                                if (i2 >= 8) {
                                    p(mVarArr, i3);
                                }
                                if (!z) {
                                    return obj4;
                                }
                            }
                        }
                    }
                }
            }
            mVarArr = g();
        }
        if (obj4 != null) {
            a(1L, i2);
        }
        return obj4;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ Object computeIfAbsent(Object obj, java.util.function.Function function) {
        return computeIfAbsent(obj, Function.VivifiedWrapper.convert(function));
    }

    @Override // j$.util.Map
    public final Object computeIfPresent(Object obj, BiFunction biFunction) {
        s b;
        Object obj2;
        if (obj == null || biFunction == null) {
            throw null;
        }
        int k2 = k(obj.hashCode());
        m[] mVarArr = this.a;
        int i2 = 0;
        Object obj3 = null;
        int i3 = 0;
        while (true) {
            if (mVarArr != null) {
                int length = mVarArr.length;
                if (length != 0) {
                    int i4 = (length - 1) & k2;
                    m m2 = m(mVarArr, i4);
                    if (m2 == null) {
                        break;
                    }
                    int i5 = m2.a;
                    if (i5 == -1) {
                        mVarArr = f(mVarArr, m2);
                    } else {
                        synchronized (m2) {
                            try {
                                if (m(mVarArr, i4) == m2) {
                                    if (i5 >= 0) {
                                        i3 = 1;
                                        m mVar = null;
                                        m mVar2 = m2;
                                        while (true) {
                                            if (mVar2.a != k2 || ((obj2 = mVar2.b) != obj && (obj2 == null || !obj.equals(obj2)))) {
                                                m mVar3 = mVar2.d;
                                                if (mVar3 == null) {
                                                    break;
                                                }
                                                i3++;
                                                mVar = mVar2;
                                                mVar2 = mVar3;
                                            }
                                        }
                                        obj3 = biFunction.apply(obj, mVar2.c);
                                        if (obj3 != null) {
                                            mVar2.c = obj3;
                                        } else {
                                            m mVar4 = mVar2.d;
                                            if (mVar != null) {
                                                mVar.d = mVar4;
                                            } else {
                                                j(mVarArr, i4, mVar4);
                                            }
                                            i2 = -1;
                                        }
                                    } else if (m2 instanceof r) {
                                        r rVar = (r) m2;
                                        s sVar = rVar.e;
                                        if (sVar != null && (b = sVar.b(k2, obj, null)) != null) {
                                            obj3 = biFunction.apply(obj, b.c);
                                            if (obj3 != null) {
                                                b.c = obj3;
                                            } else {
                                                if (rVar.g(b)) {
                                                    j(mVarArr, i4, r(rVar.f));
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
            mVarArr = g();
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

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public final boolean containsValue(Object obj) {
        obj.getClass();
        m[] mVarArr = this.a;
        if (mVarArr != null) {
            q qVar = new q(mVarArr, mVarArr.length, 0, mVarArr.length);
            while (true) {
                m f = qVar.f();
                if (f == null) {
                    break;
                }
                Object obj2 = f.c;
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

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        f fVar = this.f;
        if (fVar != null) {
            return fVar;
        }
        f fVar2 = new f(this);
        this.f = fVar2;
        return fVar2;
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public final boolean equals(Object obj) {
        V value;
        V v;
        if (obj != this) {
            if (obj instanceof Map) {
                Map map = (Map) obj;
                m[] mVarArr = this.a;
                int length = mVarArr == null ? 0 : mVarArr.length;
                q qVar = new q(mVarArr, length, 0, length);
                while (true) {
                    m f = qVar.f();
                    if (f == null) {
                        for (Map.Entry<K, V> entry : map.entrySet()) {
                            K key = entry.getKey();
                            if (key == null || (value = entry.getValue()) == null || (v = get(key)) == null || (value != v && !value.equals(v))) {
                                return false;
                            }
                        }
                        return true;
                    }
                    Object obj2 = f.c;
                    Object obj3 = map.get(f.b);
                    if (obj3 == null || (obj3 != obj2 && !obj3.equals(obj2))) {
                        break;
                    }
                }
                return false;
            }
            return false;
        }
        return true;
    }

    final m[] f(m[] mVarArr, m mVar) {
        m[] mVarArr2;
        int i2;
        if (!(mVar instanceof h) || (mVarArr2 = ((h) mVar).e) == null) {
            return this.a;
        }
        int numberOfLeadingZeros = Integer.numberOfLeadingZeros(mVarArr.length) | LiteMode.FLAG_CHAT_SCALE;
        while (true) {
            if (mVarArr2 != this.b || this.a != mVarArr || (i2 = this.sizeCtl) >= 0 || (i2 >>> h) != numberOfLeadingZeros || i2 == numberOfLeadingZeros + 1 || i2 == g + numberOfLeadingZeros || this.transferIndex <= 0) {
                break;
            } else if (j.compareAndSwapInt(this, k, i2, i2 + 1)) {
                o(mVarArr, mVarArr2);
                break;
            }
        }
        return mVarArr2;
    }

    @Override // j$.util.concurrent.v, j$.util.Map
    public final void forEach(BiConsumer biConsumer) {
        biConsumer.getClass();
        m[] mVarArr = this.a;
        if (mVarArr == null) {
            return;
        }
        q qVar = new q(mVarArr, mVarArr.length, 0, mVarArr.length);
        while (true) {
            m f = qVar.f();
            if (f == null) {
                return;
            }
            biConsumer.accept(f.b, f.c);
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ void forEach(java.util.function.BiConsumer biConsumer) {
        forEach(BiConsumer.VivifiedWrapper.convert(biConsumer));
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x004d, code lost:
        return (V) r1.c;
     */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public V get(Object obj) {
        int length;
        m m2;
        Object obj2;
        int k2 = k(obj.hashCode());
        m[] mVarArr = this.a;
        if (mVarArr != null && (length = mVarArr.length) > 0 && (m2 = m(mVarArr, (length - 1) & k2)) != null) {
            int i2 = m2.a;
            if (i2 == k2) {
                Object obj3 = m2.b;
                if (obj3 == obj || (obj3 != null && obj.equals(obj3))) {
                    return (V) m2.c;
                }
            } else if (i2 < 0) {
                m a = m2.a(obj, k2);
                if (a != null) {
                    return (V) a.c;
                }
                return null;
            }
            while (true) {
                m2 = m2.d;
                if (m2 == null) {
                    break;
                } else if (m2.a != k2 || ((obj2 = m2.b) != obj && (obj2 == null || !obj.equals(obj2)))) {
                }
            }
        }
        return null;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.concurrent.v, j$.util.Map
    public final Object getOrDefault(Object obj, Object obj2) {
        V v = get(obj);
        return v == null ? obj2 : v;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0052, code lost:
        r7 = r6.c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0054, code lost:
        if (r11 != false) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0056, code lost:
        r6.c = r10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object h(Object obj, Object obj2, boolean z) {
        Object obj3;
        Object obj4;
        if (obj == null || obj2 == null) {
            throw null;
        }
        int k2 = k(obj.hashCode());
        m[] mVarArr = this.a;
        int i2 = 0;
        while (true) {
            if (mVarArr != null) {
                int length = mVarArr.length;
                if (length != 0) {
                    int i3 = (length - 1) & k2;
                    m m2 = m(mVarArr, i3);
                    if (m2 != null) {
                        int i4 = m2.a;
                        if (i4 == -1) {
                            mVarArr = f(mVarArr, m2);
                        } else {
                            synchronized (m2) {
                                if (m(mVarArr, i3) == m2) {
                                    if (i4 >= 0) {
                                        i2 = 1;
                                        m mVar = m2;
                                        while (true) {
                                            if (mVar.a != k2 || ((obj4 = mVar.b) != obj && (obj4 == null || !obj.equals(obj4)))) {
                                                m mVar2 = mVar.d;
                                                if (mVar2 == null) {
                                                    mVar.d = new m(k2, obj, obj2, null);
                                                    break;
                                                }
                                                i2++;
                                                mVar = mVar2;
                                            }
                                        }
                                    } else if (m2 instanceof r) {
                                        s f = ((r) m2).f(k2, obj, obj2);
                                        if (f != null) {
                                            Object obj5 = f.c;
                                            if (!z) {
                                                f.c = obj2;
                                            }
                                            obj3 = obj5;
                                        } else {
                                            obj3 = null;
                                        }
                                        i2 = 2;
                                    }
                                }
                                obj3 = null;
                            }
                            if (i2 != 0) {
                                if (i2 >= 8) {
                                    p(mVarArr, i3);
                                }
                                if (obj3 != null) {
                                    return obj3;
                                }
                            }
                        }
                    } else if (b(mVarArr, i3, new m(k2, obj, obj2, null))) {
                        break;
                    }
                }
            }
            mVarArr = g();
        }
        a(1L, i2);
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public final int hashCode() {
        m[] mVarArr = this.a;
        int i2 = 0;
        if (mVarArr != null) {
            q qVar = new q(mVarArr, mVarArr.length, 0, mVarArr.length);
            while (true) {
                m f = qVar.f();
                if (f == null) {
                    break;
                }
                i2 += f.c.hashCode() ^ f.b.hashCode();
            }
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object i(Object obj, Object obj2, Object obj3) {
        int length;
        int i2;
        m m2;
        boolean z;
        Object obj4;
        s b;
        m r;
        Object obj5;
        int k2 = k(obj.hashCode());
        m[] mVarArr = this.a;
        while (true) {
            if (mVarArr == null || (length = mVarArr.length) == 0 || (m2 = m(mVarArr, (i2 = (length - 1) & k2))) == null) {
                break;
            }
            int i3 = m2.a;
            if (i3 == -1) {
                mVarArr = f(mVarArr, m2);
            } else {
                synchronized (m2) {
                    try {
                        if (m(mVarArr, i2) == m2) {
                            z = true;
                            if (i3 >= 0) {
                                m mVar = null;
                                m mVar2 = m2;
                                while (true) {
                                    if (mVar2.a != k2 || ((obj5 = mVar2.b) != obj && (obj5 == null || !obj.equals(obj5)))) {
                                        m mVar3 = mVar2.d;
                                        if (mVar3 == null) {
                                            break;
                                        }
                                        mVar = mVar2;
                                        mVar2 = mVar3;
                                    }
                                }
                                obj4 = mVar2.c;
                                if (obj3 == null || obj3 == obj4 || (obj4 != null && obj3.equals(obj4))) {
                                    if (obj2 != null) {
                                        mVar2.c = obj2;
                                    } else if (mVar != null) {
                                        mVar.d = mVar2.d;
                                    } else {
                                        r = mVar2.d;
                                        j(mVarArr, i2, r);
                                    }
                                }
                                obj4 = null;
                            } else if (m2 instanceof r) {
                                r rVar = (r) m2;
                                s sVar = rVar.e;
                                if (sVar != null && (b = sVar.b(k2, obj, null)) != null) {
                                    obj4 = b.c;
                                    if (obj3 == null || obj3 == obj4 || (obj4 != null && obj3.equals(obj4))) {
                                        if (obj2 != null) {
                                            b.c = obj2;
                                        } else if (rVar.g(b)) {
                                            r = r(rVar.f);
                                            j(mVarArr, i2, r);
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

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public boolean isEmpty() {
        return l() <= 0;
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public Set<K> keySet() {
        j jVar = this.d;
        if (jVar != null) {
            return jVar;
        }
        j jVar2 = new j(this);
        this.d = jVar2;
        return jVar2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long l() {
        d[] dVarArr = this.c;
        long j2 = this.baseCount;
        if (dVarArr != null) {
            for (d dVar : dVarArr) {
                if (dVar != null) {
                    j2 += dVar.value;
                }
            }
        }
        return j2;
    }

    @Override // j$.util.Map
    public final Object merge(Object obj, Object obj2, BiFunction biFunction) {
        int i2;
        Object obj3;
        Object obj4;
        Object obj5 = obj2;
        if (obj == null || obj5 == null || biFunction == null) {
            throw null;
        }
        int k2 = k(obj.hashCode());
        m[] mVarArr = this.a;
        int i3 = 0;
        Object obj6 = null;
        int i4 = 0;
        while (true) {
            if (mVarArr != null) {
                int length = mVarArr.length;
                if (length != 0) {
                    int i5 = (length - 1) & k2;
                    m m2 = m(mVarArr, i5);
                    i2 = 1;
                    if (m2 != null) {
                        int i6 = m2.a;
                        if (i6 == -1) {
                            mVarArr = f(mVarArr, m2);
                        } else {
                            synchronized (m2) {
                                try {
                                    if (m(mVarArr, i5) == m2) {
                                        if (i6 >= 0) {
                                            m mVar = null;
                                            m mVar2 = m2;
                                            int i7 = 1;
                                            while (true) {
                                                if (mVar2.a != k2 || ((obj4 = mVar2.b) != obj && (obj4 == null || !obj.equals(obj4)))) {
                                                    m mVar3 = mVar2.d;
                                                    if (mVar3 == null) {
                                                        mVar2.d = new m(k2, obj, obj5, null);
                                                        obj3 = obj5;
                                                        i4 = 1;
                                                        break;
                                                    }
                                                    i7++;
                                                    mVar = mVar2;
                                                    mVar2 = mVar3;
                                                }
                                            }
                                            obj3 = biFunction.apply(mVar2.c, obj5);
                                            if (obj3 != null) {
                                                mVar2.c = obj3;
                                            } else {
                                                m mVar4 = mVar2.d;
                                                if (mVar != null) {
                                                    mVar.d = mVar4;
                                                } else {
                                                    j(mVarArr, i5, mVar4);
                                                }
                                                i4 = -1;
                                            }
                                            i3 = i7;
                                            obj6 = obj3;
                                        } else if (m2 instanceof r) {
                                            r rVar = (r) m2;
                                            s sVar = rVar.e;
                                            s b = sVar == null ? null : sVar.b(k2, obj, null);
                                            Object apply = b == null ? obj5 : biFunction.apply(b.c, obj5);
                                            if (apply == null) {
                                                if (b != null) {
                                                    if (rVar.g(b)) {
                                                        j(mVarArr, i5, r(rVar.f));
                                                    }
                                                    obj6 = apply;
                                                    i3 = 2;
                                                    i4 = -1;
                                                }
                                                obj6 = apply;
                                                i3 = 2;
                                            } else if (b != null) {
                                                b.c = apply;
                                                obj6 = apply;
                                                i3 = 2;
                                            } else {
                                                rVar.f(k2, obj, apply);
                                                obj6 = apply;
                                                i3 = 2;
                                                i4 = 1;
                                            }
                                        }
                                    }
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                            if (i3 != 0) {
                                if (i3 >= 8) {
                                    p(mVarArr, i5);
                                }
                                i2 = i4;
                                obj5 = obj6;
                            }
                        }
                    } else if (b(mVarArr, i5, new m(k2, obj, obj5, null))) {
                        break;
                    }
                }
            }
            mVarArr = g();
        }
        if (i2 != 0) {
            a(i2, i3);
        }
        return obj5;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ Object merge(Object obj, Object obj2, java.util.function.BiFunction biFunction) {
        return merge(obj, obj2, BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public V put(K k2, V v) {
        return (V) h(k2, v, false);
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        q(map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            h(entry.getKey(), entry.getValue(), false);
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public V putIfAbsent(K k2, V v) {
        return (V) h(k2, v, true);
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public V remove(Object obj) {
        return (V) i(obj, null, null);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public boolean remove(Object obj, Object obj2) {
        obj.getClass();
        return (obj2 == null || i(obj, null, obj2) == null) ? false : true;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final Object replace(Object obj, Object obj2) {
        if (obj == null || obj2 == null) {
            throw null;
        }
        return i(obj, obj2, null);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.Map
    public final boolean replace(Object obj, Object obj2, Object obj3) {
        if (obj == null || obj2 == null || obj3 == null) {
            throw null;
        }
        return i(obj, obj3, obj2) != null;
    }

    @Override // j$.util.Map
    public final void replaceAll(BiFunction biFunction) {
        biFunction.getClass();
        m[] mVarArr = this.a;
        if (mVarArr == null) {
            return;
        }
        q qVar = new q(mVarArr, mVarArr.length, 0, mVarArr.length);
        while (true) {
            m f = qVar.f();
            if (f == null) {
                return;
            }
            Object obj = f.c;
            Object obj2 = f.b;
            do {
                Object apply = biFunction.apply(obj2, obj);
                apply.getClass();
                if (i(obj2, apply, obj) == null) {
                    obj = get(obj2);
                }
            } while (obj != null);
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public final /* synthetic */ void replaceAll(java.util.function.BiFunction biFunction) {
        replaceAll(BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public int size() {
        long l2 = l();
        if (l2 < 0) {
            return 0;
        }
        return l2 > 2147483647L ? ConnectionsManager.DEFAULT_DATACENTER_ID : (int) l2;
    }

    @Override // java.util.AbstractMap
    public final String toString() {
        m[] mVarArr = this.a;
        int length = mVarArr == null ? 0 : mVarArr.length;
        q qVar = new q(mVarArr, length, 0, length);
        StringBuilder sb = new StringBuilder("{");
        m f = qVar.f();
        if (f != null) {
            while (true) {
                Object obj = f.b;
                Object obj2 = f.c;
                if (obj == this) {
                    obj = "(this Map)";
                }
                sb.append(obj);
                sb.append('=');
                if (obj2 == this) {
                    obj2 = "(this Map)";
                }
                sb.append(obj2);
                f = qVar.f();
                if (f == null) {
                    break;
                }
                sb.append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public Collection<V> values() {
        t tVar = this.e;
        if (tVar != null) {
            return tVar;
        }
        t tVar2 = new t(this);
        this.e = tVar2;
        return tVar2;
    }
}
