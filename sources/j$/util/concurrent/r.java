package j$.util.concurrent;

import java.util.concurrent.locks.LockSupport;
import sun.misc.Unsafe;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class r extends m {
    private static final Unsafe h;
    private static final long i;
    s e;
    volatile s f;
    volatile Thread g;
    volatile int lockState;

    static {
        try {
            Unsafe c = w.c();
            h = c;
            i = c.objectFieldOffset(r.class.getDeclaredField("lockState"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public r(s sVar) {
        super(-2, null, null, null);
        int d;
        int j;
        this.f = sVar;
        s sVar2 = null;
        while (sVar != null) {
            s sVar3 = (s) sVar.d;
            sVar.g = null;
            sVar.f = null;
            if (sVar2 == null) {
                sVar.e = null;
                sVar.i = false;
            } else {
                Object obj = sVar.b;
                int i2 = sVar.a;
                s sVar4 = sVar2;
                Class cls = null;
                while (true) {
                    Object obj2 = sVar4.b;
                    int i3 = sVar4.a;
                    j = i3 > i2 ? -1 : i3 < i2 ? 1 : ((cls == null && (cls = ConcurrentHashMap.c(obj)) == null) || (d = ConcurrentHashMap.d(cls, obj, obj2)) == 0) ? j(obj, obj2) : d;
                    s sVar5 = j <= 0 ? sVar4.f : sVar4.g;
                    if (sVar5 == null) {
                        break;
                    }
                    sVar4 = sVar5;
                }
                sVar.e = sVar4;
                if (j <= 0) {
                    sVar4.f = sVar;
                } else {
                    sVar4.g = sVar;
                }
                sVar = c(sVar2, sVar);
            }
            sVar2 = sVar;
            sVar = sVar3;
        }
        this.e = sVar2;
    }

    static s b(s sVar, s sVar2) {
        while (sVar2 != null && sVar2 != sVar) {
            s sVar3 = sVar2.e;
            if (sVar3 == null) {
                sVar2.i = false;
                return sVar2;
            } else if (sVar2.i) {
                sVar2.i = false;
                return sVar;
            } else {
                s sVar4 = sVar3.f;
                if (sVar4 == sVar2) {
                    sVar4 = sVar3.g;
                    if (sVar4 != null && sVar4.i) {
                        sVar4.i = false;
                        sVar3.i = true;
                        sVar = h(sVar, sVar3);
                        sVar3 = sVar2.e;
                        sVar4 = sVar3 == null ? null : sVar3.g;
                    }
                    if (sVar4 == null) {
                        sVar2 = sVar3;
                    } else {
                        s sVar5 = sVar4.f;
                        s sVar6 = sVar4.g;
                        if ((sVar6 != null && sVar6.i) || (sVar5 != null && sVar5.i)) {
                            if (sVar6 == null || !sVar6.i) {
                                if (sVar5 != null) {
                                    sVar5.i = false;
                                }
                                sVar4.i = true;
                                sVar = i(sVar, sVar4);
                                sVar3 = sVar2.e;
                                sVar4 = sVar3 != null ? sVar3.g : null;
                            }
                            if (sVar4 != null) {
                                sVar4.i = sVar3 == null ? false : sVar3.i;
                                s sVar7 = sVar4.g;
                                if (sVar7 != null) {
                                    sVar7.i = false;
                                }
                            }
                            if (sVar3 != null) {
                                sVar3.i = false;
                                sVar = h(sVar, sVar3);
                            }
                            sVar2 = sVar;
                            sVar = sVar2;
                        }
                        sVar4.i = true;
                        sVar2 = sVar3;
                    }
                } else {
                    if (sVar4 != null && sVar4.i) {
                        sVar4.i = false;
                        sVar3.i = true;
                        sVar = i(sVar, sVar3);
                        sVar3 = sVar2.e;
                        sVar4 = sVar3 == null ? null : sVar3.f;
                    }
                    if (sVar4 == null) {
                        sVar2 = sVar3;
                    } else {
                        s sVar8 = sVar4.f;
                        s sVar9 = sVar4.g;
                        if ((sVar8 != null && sVar8.i) || (sVar9 != null && sVar9.i)) {
                            if (sVar8 == null || !sVar8.i) {
                                if (sVar9 != null) {
                                    sVar9.i = false;
                                }
                                sVar4.i = true;
                                sVar = h(sVar, sVar4);
                                sVar3 = sVar2.e;
                                sVar4 = sVar3 != null ? sVar3.f : null;
                            }
                            if (sVar4 != null) {
                                sVar4.i = sVar3 == null ? false : sVar3.i;
                                s sVar10 = sVar4.f;
                                if (sVar10 != null) {
                                    sVar10.i = false;
                                }
                            }
                            if (sVar3 != null) {
                                sVar3.i = false;
                                sVar = i(sVar, sVar3);
                            }
                            sVar2 = sVar;
                            sVar = sVar2;
                        }
                        sVar4.i = true;
                        sVar2 = sVar3;
                    }
                }
            }
        }
        return sVar;
    }

    static s c(s sVar, s sVar2) {
        s sVar3;
        sVar2.i = true;
        while (true) {
            s sVar4 = sVar2.e;
            if (sVar4 == null) {
                sVar2.i = false;
                return sVar2;
            } else if (!sVar4.i || (sVar3 = sVar4.e) == null) {
                break;
            } else {
                s sVar5 = sVar3.f;
                if (sVar4 == sVar5) {
                    sVar5 = sVar3.g;
                    if (sVar5 == null || !sVar5.i) {
                        if (sVar2 == sVar4.g) {
                            sVar = h(sVar, sVar4);
                            s sVar6 = sVar4.e;
                            sVar3 = sVar6 == null ? null : sVar6.e;
                            sVar4 = sVar6;
                            sVar2 = sVar4;
                        }
                        if (sVar4 != null) {
                            sVar4.i = false;
                            if (sVar3 != null) {
                                sVar3.i = true;
                                sVar = i(sVar, sVar3);
                            }
                        }
                    } else {
                        sVar5.i = false;
                        sVar4.i = false;
                        sVar3.i = true;
                        sVar2 = sVar3;
                    }
                } else if (sVar5 == null || !sVar5.i) {
                    if (sVar2 == sVar4.f) {
                        sVar = i(sVar, sVar4);
                        s sVar7 = sVar4.e;
                        sVar3 = sVar7 == null ? null : sVar7.e;
                        sVar4 = sVar7;
                        sVar2 = sVar4;
                    }
                    if (sVar4 != null) {
                        sVar4.i = false;
                        if (sVar3 != null) {
                            sVar3.i = true;
                            sVar = h(sVar, sVar3);
                        }
                    }
                } else {
                    sVar5.i = false;
                    sVar4.i = false;
                    sVar3.i = true;
                    sVar2 = sVar3;
                }
            }
        }
        return sVar;
    }

    private final void d() {
        boolean z = false;
        while (true) {
            int i2 = this.lockState;
            if ((i2 & (-3)) == 0) {
                if (h.compareAndSwapInt(this, i, i2, 1)) {
                    break;
                }
            } else if ((i2 & 2) == 0) {
                if (h.compareAndSwapInt(this, i, i2, i2 | 2)) {
                    this.g = Thread.currentThread();
                    z = true;
                }
            } else if (z) {
                LockSupport.park(this);
            }
        }
        if (z) {
            this.g = null;
        }
    }

    private final void e() {
        if (h.compareAndSwapInt(this, i, 0, 1)) {
            return;
        }
        d();
    }

    static s h(s sVar, s sVar2) {
        s sVar3 = sVar2.g;
        if (sVar3 != null) {
            s sVar4 = sVar3.f;
            sVar2.g = sVar4;
            if (sVar4 != null) {
                sVar4.e = sVar2;
            }
            s sVar5 = sVar2.e;
            sVar3.e = sVar5;
            if (sVar5 == null) {
                sVar3.i = false;
                sVar = sVar3;
            } else if (sVar5.f == sVar2) {
                sVar5.f = sVar3;
            } else {
                sVar5.g = sVar3;
            }
            sVar3.f = sVar2;
            sVar2.e = sVar3;
        }
        return sVar;
    }

    static s i(s sVar, s sVar2) {
        s sVar3 = sVar2.f;
        if (sVar3 != null) {
            s sVar4 = sVar3.g;
            sVar2.f = sVar4;
            if (sVar4 != null) {
                sVar4.e = sVar2;
            }
            s sVar5 = sVar2.e;
            sVar3.e = sVar5;
            if (sVar5 == null) {
                sVar3.i = false;
                sVar = sVar3;
            } else if (sVar5.g == sVar2) {
                sVar5.g = sVar3;
            } else {
                sVar5.f = sVar3;
            }
            sVar3.g = sVar2;
            sVar2.e = sVar3;
        }
        return sVar;
    }

    static int j(Object obj, Object obj2) {
        int compareTo;
        return (obj == null || obj2 == null || (compareTo = obj.getClass().getName().compareTo(obj2.getClass().getName())) == 0) ? System.identityHashCode(obj) <= System.identityHashCode(obj2) ? -1 : 1 : compareTo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.concurrent.m
    public final m a(Object obj, int i2) {
        Object obj2;
        Thread thread;
        Thread thread2;
        s sVar = null;
        if (obj != null) {
            m mVar = this.f;
            while (mVar != null) {
                int i3 = this.lockState;
                if ((i3 & 3) == 0) {
                    Unsafe unsafe = h;
                    long j = i;
                    if (unsafe.compareAndSwapInt(this, j, i3, i3 + 4)) {
                        try {
                            s sVar2 = this.e;
                            if (sVar2 != null) {
                                sVar = sVar2.b(i2, obj, null);
                            }
                            if (w.a(unsafe, this, j) == 6 && (thread2 = this.g) != null) {
                                LockSupport.unpark(thread2);
                            }
                            return sVar;
                        } catch (Throwable th) {
                            if (w.a(h, this, i) == 6 && (thread = this.g) != null) {
                                LockSupport.unpark(thread);
                            }
                            throw th;
                        }
                    }
                } else if (mVar.a == i2 && ((obj2 = mVar.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                    return mVar;
                } else {
                    mVar = mVar.d;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0060, code lost:
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00a3, code lost:
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final s f(int i2, Object obj, Object obj2) {
        int d;
        int i3;
        s sVar;
        s sVar2 = this.e;
        Class cls = null;
        boolean z = false;
        while (true) {
            if (sVar2 == null) {
                s sVar3 = new s(i2, obj, obj2, null, null);
                this.e = sVar3;
                this.f = sVar3;
                break;
            }
            int i4 = sVar2.a;
            if (i4 > i2) {
                i3 = -1;
            } else if (i4 < i2) {
                i3 = 1;
            } else {
                Object obj3 = sVar2.b;
                if (obj3 == obj || (obj3 != null && obj.equals(obj3))) {
                    break;
                } else if ((cls == null && (cls = ConcurrentHashMap.c(obj)) == null) || (d = ConcurrentHashMap.d(cls, obj, obj3)) == 0) {
                    if (!z) {
                        s sVar4 = sVar2.f;
                        if ((sVar4 == null || (r3 = sVar4.b(i2, obj, cls)) == null) && ((sVar = sVar2.g) == null || (r3 = sVar.b(i2, obj, cls)) == null)) {
                            z = true;
                        }
                    }
                    i3 = j(obj, obj3);
                } else {
                    i3 = d;
                }
            }
            s sVar5 = i3 <= 0 ? sVar2.f : sVar2.g;
            if (sVar5 == null) {
                s sVar6 = this.f;
                s sVar7 = new s(i2, obj, obj2, sVar6, sVar2);
                this.f = sVar7;
                if (sVar6 != null) {
                    sVar6.h = sVar7;
                }
                if (i3 <= 0) {
                    sVar2.f = sVar7;
                } else {
                    sVar2.g = sVar7;
                }
                if (sVar2.i) {
                    e();
                    try {
                        this.e = c(this.e, sVar7);
                    } finally {
                        this.lockState = 0;
                    }
                } else {
                    sVar7.i = true;
                }
            } else {
                sVar2 = sVar5;
            }
        }
        return sVar2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:57:0x008e A[Catch: all -> 0x00c8, TryCatch #0 {all -> 0x00c8, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:39:0x0065, B:41:0x006b, B:42:0x006d, B:57:0x008e, B:64:0x009f, B:60:0x0096, B:62:0x009a, B:63:0x009d, B:65:0x00a5, B:69:0x00ae, B:71:0x00b2, B:73:0x00b6, B:75:0x00ba, B:79:0x00c3, B:76:0x00bd, B:78:0x00c1, B:68:0x00aa, B:45:0x0077, B:47:0x007b, B:48:0x007e, B:32:0x0052, B:34:0x0058, B:36:0x005c, B:37:0x005f, B:38:0x0061), top: B:86:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00aa A[Catch: all -> 0x00c8, TryCatch #0 {all -> 0x00c8, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:39:0x0065, B:41:0x006b, B:42:0x006d, B:57:0x008e, B:64:0x009f, B:60:0x0096, B:62:0x009a, B:63:0x009d, B:65:0x00a5, B:69:0x00ae, B:71:0x00b2, B:73:0x00b6, B:75:0x00ba, B:79:0x00c3, B:76:0x00bd, B:78:0x00c1, B:68:0x00aa, B:45:0x0077, B:47:0x007b, B:48:0x007e, B:32:0x0052, B:34:0x0058, B:36:0x005c, B:37:0x005f, B:38:0x0061), top: B:86:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00b2 A[Catch: all -> 0x00c8, TryCatch #0 {all -> 0x00c8, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:39:0x0065, B:41:0x006b, B:42:0x006d, B:57:0x008e, B:64:0x009f, B:60:0x0096, B:62:0x009a, B:63:0x009d, B:65:0x00a5, B:69:0x00ae, B:71:0x00b2, B:73:0x00b6, B:75:0x00ba, B:79:0x00c3, B:76:0x00bd, B:78:0x00c1, B:68:0x00aa, B:45:0x0077, B:47:0x007b, B:48:0x007e, B:32:0x0052, B:34:0x0058, B:36:0x005c, B:37:0x005f, B:38:0x0061), top: B:86:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x00ba A[Catch: all -> 0x00c8, TryCatch #0 {all -> 0x00c8, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:39:0x0065, B:41:0x006b, B:42:0x006d, B:57:0x008e, B:64:0x009f, B:60:0x0096, B:62:0x009a, B:63:0x009d, B:65:0x00a5, B:69:0x00ae, B:71:0x00b2, B:73:0x00b6, B:75:0x00ba, B:79:0x00c3, B:76:0x00bd, B:78:0x00c1, B:68:0x00aa, B:45:0x0077, B:47:0x007b, B:48:0x007e, B:32:0x0052, B:34:0x0058, B:36:0x005c, B:37:0x005f, B:38:0x0061), top: B:86:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x00bd A[Catch: all -> 0x00c8, TryCatch #0 {all -> 0x00c8, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:39:0x0065, B:41:0x006b, B:42:0x006d, B:57:0x008e, B:64:0x009f, B:60:0x0096, B:62:0x009a, B:63:0x009d, B:65:0x00a5, B:69:0x00ae, B:71:0x00b2, B:73:0x00b6, B:75:0x00ba, B:79:0x00c3, B:76:0x00bd, B:78:0x00c1, B:68:0x00aa, B:45:0x0077, B:47:0x007b, B:48:0x007e, B:32:0x0052, B:34:0x0058, B:36:0x005c, B:37:0x005f, B:38:0x0061), top: B:86:0x0030 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean g(s sVar) {
        s sVar2;
        s sVar3;
        s sVar4 = (s) sVar.d;
        s sVar5 = sVar.h;
        if (sVar5 == null) {
            this.f = sVar4;
        } else {
            sVar5.d = sVar4;
        }
        if (sVar4 != null) {
            sVar4.h = sVar5;
        }
        if (this.f == null) {
            this.e = null;
            return true;
        }
        s sVar6 = this.e;
        if (sVar6 == null || sVar6.g == null || (sVar2 = sVar6.f) == null || sVar2.f == null) {
            return true;
        }
        e();
        try {
            s sVar7 = sVar.f;
            s sVar8 = sVar.g;
            if (sVar7 == null || sVar8 == null) {
                if (sVar7 == null) {
                    if (sVar8 != null) {
                        sVar7 = sVar8;
                    }
                    sVar7 = sVar;
                }
                if (sVar7 != sVar) {
                    s sVar9 = sVar.e;
                    sVar7.e = sVar9;
                    if (sVar9 == null) {
                        sVar6 = sVar7;
                    } else if (sVar == sVar9.f) {
                        sVar9.f = sVar7;
                    } else {
                        sVar9.g = sVar7;
                    }
                    sVar.e = null;
                    sVar.g = null;
                    sVar.f = null;
                }
                if (!sVar.i) {
                    sVar6 = b(sVar6, sVar7);
                }
                this.e = sVar6;
                if (sVar == sVar7 && (sVar3 = sVar.e) != null) {
                    if (sVar != sVar3.f) {
                        sVar3.f = null;
                    } else if (sVar == sVar3.g) {
                        sVar3.g = null;
                    }
                    sVar.e = null;
                }
                return false;
            }
            s sVar10 = sVar8;
            while (true) {
                s sVar11 = sVar10.f;
                if (sVar11 == null) {
                    break;
                }
                sVar10 = sVar11;
            }
            boolean z = sVar10.i;
            sVar10.i = sVar.i;
            sVar.i = z;
            s sVar12 = sVar10.g;
            s sVar13 = sVar.e;
            if (sVar10 == sVar8) {
                sVar.e = sVar10;
                sVar10.g = sVar;
            } else {
                s sVar14 = sVar10.e;
                sVar.e = sVar14;
                if (sVar14 != null) {
                    if (sVar10 == sVar14.f) {
                        sVar14.f = sVar;
                    } else {
                        sVar14.g = sVar;
                    }
                }
                sVar10.g = sVar8;
                sVar8.e = sVar10;
            }
            sVar.f = null;
            sVar.g = sVar12;
            if (sVar12 != null) {
                sVar12.e = sVar;
            }
            sVar10.f = sVar7;
            sVar7.e = sVar10;
            sVar10.e = sVar13;
            if (sVar13 == null) {
                sVar6 = sVar10;
            } else if (sVar == sVar13.f) {
                sVar13.f = sVar10;
            } else {
                sVar13.g = sVar10;
            }
            if (sVar12 != null) {
                sVar7 = sVar12;
                if (sVar7 != sVar) {
                }
                if (!sVar.i) {
                }
                this.e = sVar6;
                if (sVar == sVar7) {
                    if (sVar != sVar3.f) {
                    }
                    sVar.e = null;
                }
                return false;
            }
            sVar7 = sVar;
            if (sVar7 != sVar) {
            }
            if (!sVar.i) {
            }
            this.e = sVar6;
            if (sVar == sVar7) {
            }
            return false;
        } finally {
            this.lockState = 0;
        }
    }
}
