package j$.util.concurrent;

import java.util.concurrent.locks.LockSupport;
import sun.misc.Unsafe;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class p extends k {
    private static final Unsafe h;
    private static final long i;
    q e;
    volatile q f;
    volatile Thread g;
    volatile int lockState;

    static {
        try {
            Unsafe c = u.c();
            h = c;
            i = c.objectFieldOffset(p.class.getDeclaredField("lockState"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(q qVar) {
        super(-2, null, null, null);
        int i2;
        this.f = qVar;
        q qVar2 = null;
        while (qVar != null) {
            q qVar3 = (q) qVar.d;
            qVar.g = null;
            qVar.f = null;
            if (qVar2 == null) {
                qVar.e = null;
                qVar.i = false;
            } else {
                Object obj = qVar.b;
                int i3 = qVar.a;
                q qVar4 = qVar2;
                Class<?> cls = null;
                while (true) {
                    Object obj2 = qVar4.b;
                    int i4 = qVar4.a;
                    if (i4 > i3) {
                        i2 = -1;
                    } else if (i4 < i3) {
                        i2 = 1;
                    } else {
                        if (cls != null || (cls = ConcurrentHashMap.c(obj)) != null) {
                            int i5 = ConcurrentHashMap.i;
                            int compareTo = (obj2 == null || obj2.getClass() != cls) ? 0 : ((Comparable) obj).compareTo(obj2);
                            if (compareTo != 0) {
                                i2 = compareTo;
                            }
                        }
                        i2 = j(obj, obj2);
                    }
                    q qVar5 = i2 <= 0 ? qVar4.f : qVar4.g;
                    if (qVar5 == null) {
                        break;
                    }
                    qVar4 = qVar5;
                }
                qVar.e = qVar4;
                if (i2 <= 0) {
                    qVar4.f = qVar;
                } else {
                    qVar4.g = qVar;
                }
                qVar = c(qVar2, qVar);
            }
            qVar2 = qVar;
            qVar = qVar3;
        }
        this.e = qVar2;
    }

    static q b(q qVar, q qVar2) {
        while (qVar2 != null && qVar2 != qVar) {
            q qVar3 = qVar2.e;
            if (qVar3 == null) {
                qVar2.i = false;
                return qVar2;
            } else if (qVar2.i) {
                qVar2.i = false;
                return qVar;
            } else {
                q qVar4 = qVar3.f;
                if (qVar4 == qVar2) {
                    qVar4 = qVar3.g;
                    if (qVar4 != null && qVar4.i) {
                        qVar4.i = false;
                        qVar3.i = true;
                        qVar = h(qVar, qVar3);
                        qVar3 = qVar2.e;
                        qVar4 = qVar3 == null ? null : qVar3.g;
                    }
                    if (qVar4 == null) {
                        qVar2 = qVar3;
                    } else {
                        q qVar5 = qVar4.f;
                        q qVar6 = qVar4.g;
                        if ((qVar6 != null && qVar6.i) || (qVar5 != null && qVar5.i)) {
                            if (qVar6 == null || !qVar6.i) {
                                if (qVar5 != null) {
                                    qVar5.i = false;
                                }
                                qVar4.i = true;
                                qVar = i(qVar, qVar4);
                                qVar3 = qVar2.e;
                                qVar4 = qVar3 != null ? qVar3.g : null;
                            }
                            if (qVar4 != null) {
                                qVar4.i = qVar3 == null ? false : qVar3.i;
                                q qVar7 = qVar4.g;
                                if (qVar7 != null) {
                                    qVar7.i = false;
                                }
                            }
                            if (qVar3 != null) {
                                qVar3.i = false;
                                qVar = h(qVar, qVar3);
                            }
                            qVar2 = qVar;
                        }
                        qVar4.i = true;
                        qVar2 = qVar3;
                    }
                } else {
                    if (qVar4 != null && qVar4.i) {
                        qVar4.i = false;
                        qVar3.i = true;
                        qVar = i(qVar, qVar3);
                        qVar3 = qVar2.e;
                        qVar4 = qVar3 == null ? null : qVar3.f;
                    }
                    if (qVar4 == null) {
                        qVar2 = qVar3;
                    } else {
                        q qVar8 = qVar4.f;
                        q qVar9 = qVar4.g;
                        if ((qVar8 != null && qVar8.i) || (qVar9 != null && qVar9.i)) {
                            if (qVar8 == null || !qVar8.i) {
                                if (qVar9 != null) {
                                    qVar9.i = false;
                                }
                                qVar4.i = true;
                                qVar = h(qVar, qVar4);
                                qVar3 = qVar2.e;
                                qVar4 = qVar3 != null ? qVar3.f : null;
                            }
                            if (qVar4 != null) {
                                qVar4.i = qVar3 == null ? false : qVar3.i;
                                q qVar10 = qVar4.f;
                                if (qVar10 != null) {
                                    qVar10.i = false;
                                }
                            }
                            if (qVar3 != null) {
                                qVar3.i = false;
                                qVar = i(qVar, qVar3);
                            }
                            qVar2 = qVar;
                        }
                        qVar4.i = true;
                        qVar2 = qVar3;
                    }
                }
            }
        }
        return qVar;
    }

    static q c(q qVar, q qVar2) {
        q qVar3;
        qVar2.i = true;
        while (true) {
            q qVar4 = qVar2.e;
            if (qVar4 == null) {
                qVar2.i = false;
                return qVar2;
            } else if (!qVar4.i || (qVar3 = qVar4.e) == null) {
                break;
            } else {
                q qVar5 = qVar3.f;
                if (qVar4 == qVar5) {
                    qVar5 = qVar3.g;
                    if (qVar5 == null || !qVar5.i) {
                        if (qVar2 == qVar4.g) {
                            qVar = h(qVar, qVar4);
                            q qVar6 = qVar4.e;
                            qVar3 = qVar6 == null ? null : qVar6.e;
                            qVar4 = qVar6;
                            qVar2 = qVar4;
                        }
                        if (qVar4 != null) {
                            qVar4.i = false;
                            if (qVar3 != null) {
                                qVar3.i = true;
                                qVar = i(qVar, qVar3);
                            }
                        }
                    } else {
                        qVar5.i = false;
                        qVar4.i = false;
                        qVar3.i = true;
                        qVar2 = qVar3;
                    }
                } else if (qVar5 == null || !qVar5.i) {
                    if (qVar2 == qVar4.f) {
                        qVar = i(qVar, qVar4);
                        q qVar7 = qVar4.e;
                        qVar3 = qVar7 == null ? null : qVar7.e;
                        qVar4 = qVar7;
                        qVar2 = qVar4;
                    }
                    if (qVar4 != null) {
                        qVar4.i = false;
                        if (qVar3 != null) {
                            qVar3.i = true;
                            qVar = h(qVar, qVar3);
                        }
                    }
                } else {
                    qVar5.i = false;
                    qVar4.i = false;
                    qVar3.i = true;
                    qVar2 = qVar3;
                }
            }
        }
        return qVar;
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

    static q h(q qVar, q qVar2) {
        q qVar3 = qVar2.g;
        if (qVar3 != null) {
            q qVar4 = qVar3.f;
            qVar2.g = qVar4;
            if (qVar4 != null) {
                qVar4.e = qVar2;
            }
            q qVar5 = qVar2.e;
            qVar3.e = qVar5;
            if (qVar5 == null) {
                qVar3.i = false;
                qVar = qVar3;
            } else if (qVar5.f == qVar2) {
                qVar5.f = qVar3;
            } else {
                qVar5.g = qVar3;
            }
            qVar3.f = qVar2;
            qVar2.e = qVar3;
        }
        return qVar;
    }

    static q i(q qVar, q qVar2) {
        q qVar3 = qVar2.f;
        if (qVar3 != null) {
            q qVar4 = qVar3.g;
            qVar2.f = qVar4;
            if (qVar4 != null) {
                qVar4.e = qVar2;
            }
            q qVar5 = qVar2.e;
            qVar3.e = qVar5;
            if (qVar5 == null) {
                qVar3.i = false;
                qVar = qVar3;
            } else if (qVar5.g == qVar2) {
                qVar5.g = qVar3;
            } else {
                qVar5.f = qVar3;
            }
            qVar3.g = qVar2;
            qVar2.e = qVar3;
        }
        return qVar;
    }

    static int j(Object obj, Object obj2) {
        int compareTo;
        return (obj == null || obj2 == null || (compareTo = obj.getClass().getName().compareTo(obj2.getClass().getName())) == 0) ? System.identityHashCode(obj) <= System.identityHashCode(obj2) ? -1 : 1 : compareTo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.concurrent.k
    public final k a(Object obj, int i2) {
        Object obj2;
        Thread thread;
        Thread thread2;
        q qVar = null;
        if (obj != null) {
            k kVar = this.f;
            while (kVar != null) {
                int i3 = this.lockState;
                if ((i3 & 3) == 0) {
                    Unsafe unsafe = h;
                    long j = i;
                    if (unsafe.compareAndSwapInt(this, j, i3, i3 + 4)) {
                        try {
                            q qVar2 = this.e;
                            if (qVar2 != null) {
                                qVar = qVar2.b(i2, obj, null);
                            }
                            if (u.a(unsafe, this, j) == 6 && (thread2 = this.g) != null) {
                                LockSupport.unpark(thread2);
                            }
                            return qVar;
                        } catch (Throwable th) {
                            if (u.a(h, this, i) == 6 && (thread = this.g) != null) {
                                LockSupport.unpark(thread);
                            }
                            throw th;
                        }
                    }
                } else if (kVar.a == i2 && ((obj2 = kVar.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                    return kVar;
                } else {
                    kVar = kVar.d;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0070, code lost:
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00b3, code lost:
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final q f(int i2, Object obj, Object obj2) {
        int i3;
        q qVar;
        q qVar2 = this.e;
        Class<?> cls = null;
        boolean z = false;
        while (true) {
            if (qVar2 == null) {
                q qVar3 = new q(i2, obj, obj2, null, null);
                this.e = qVar3;
                this.f = qVar3;
                break;
            }
            int i4 = qVar2.a;
            if (i4 > i2) {
                i3 = -1;
            } else if (i4 < i2) {
                i3 = 1;
            } else {
                Object obj3 = qVar2.b;
                if (obj3 == obj || (obj3 != null && obj.equals(obj3))) {
                    break;
                }
                if (cls != null || (cls = ConcurrentHashMap.c(obj)) != null) {
                    int i5 = ConcurrentHashMap.i;
                    int compareTo = (obj3 == null || obj3.getClass() != cls) ? 0 : ((Comparable) obj).compareTo(obj3);
                    if (compareTo != 0) {
                        i3 = compareTo;
                    }
                }
                if (!z) {
                    q qVar4 = qVar2.f;
                    if ((qVar4 == null || (r3 = qVar4.b(i2, obj, cls)) == null) && ((qVar = qVar2.g) == null || (r3 = qVar.b(i2, obj, cls)) == null)) {
                        z = true;
                    }
                }
                i3 = j(obj, obj3);
            }
            q qVar5 = i3 <= 0 ? qVar2.f : qVar2.g;
            if (qVar5 == null) {
                q qVar6 = this.f;
                q qVar7 = new q(i2, obj, obj2, qVar6, qVar2);
                this.f = qVar7;
                if (qVar6 != null) {
                    qVar6.h = qVar7;
                }
                if (i3 <= 0) {
                    qVar2.f = qVar7;
                } else {
                    qVar2.g = qVar7;
                }
                if (qVar2.i) {
                    e();
                    try {
                        this.e = c(this.e, qVar7);
                    } finally {
                        this.lockState = 0;
                    }
                } else {
                    qVar7.i = true;
                }
            } else {
                qVar2 = qVar5;
            }
        }
        return qVar2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0091 A[Catch: all -> 0x0052, TryCatch #0 {all -> 0x0052, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:41:0x0068, B:43:0x006e, B:44:0x0070, B:59:0x0091, B:66:0x00a2, B:62:0x0099, B:64:0x009d, B:65:0x00a0, B:67:0x00a8, B:71:0x00b1, B:73:0x00b5, B:75:0x00b9, B:77:0x00bd, B:81:0x00c6, B:78:0x00c0, B:80:0x00c4, B:70:0x00ad, B:47:0x007a, B:49:0x007e, B:50:0x0081, B:34:0x0055, B:36:0x005b, B:38:0x005f, B:39:0x0062, B:40:0x0064), top: B:87:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00ad A[Catch: all -> 0x0052, TryCatch #0 {all -> 0x0052, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:41:0x0068, B:43:0x006e, B:44:0x0070, B:59:0x0091, B:66:0x00a2, B:62:0x0099, B:64:0x009d, B:65:0x00a0, B:67:0x00a8, B:71:0x00b1, B:73:0x00b5, B:75:0x00b9, B:77:0x00bd, B:81:0x00c6, B:78:0x00c0, B:80:0x00c4, B:70:0x00ad, B:47:0x007a, B:49:0x007e, B:50:0x0081, B:34:0x0055, B:36:0x005b, B:38:0x005f, B:39:0x0062, B:40:0x0064), top: B:87:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00b5 A[Catch: all -> 0x0052, TryCatch #0 {all -> 0x0052, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:41:0x0068, B:43:0x006e, B:44:0x0070, B:59:0x0091, B:66:0x00a2, B:62:0x0099, B:64:0x009d, B:65:0x00a0, B:67:0x00a8, B:71:0x00b1, B:73:0x00b5, B:75:0x00b9, B:77:0x00bd, B:81:0x00c6, B:78:0x00c0, B:80:0x00c4, B:70:0x00ad, B:47:0x007a, B:49:0x007e, B:50:0x0081, B:34:0x0055, B:36:0x005b, B:38:0x005f, B:39:0x0062, B:40:0x0064), top: B:87:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x00bd A[Catch: all -> 0x0052, TryCatch #0 {all -> 0x0052, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:41:0x0068, B:43:0x006e, B:44:0x0070, B:59:0x0091, B:66:0x00a2, B:62:0x0099, B:64:0x009d, B:65:0x00a0, B:67:0x00a8, B:71:0x00b1, B:73:0x00b5, B:75:0x00b9, B:77:0x00bd, B:81:0x00c6, B:78:0x00c0, B:80:0x00c4, B:70:0x00ad, B:47:0x007a, B:49:0x007e, B:50:0x0081, B:34:0x0055, B:36:0x005b, B:38:0x005f, B:39:0x0062, B:40:0x0064), top: B:87:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00c0 A[Catch: all -> 0x0052, TryCatch #0 {all -> 0x0052, blocks: (B:22:0x0030, B:26:0x0039, B:29:0x003f, B:31:0x004d, B:41:0x0068, B:43:0x006e, B:44:0x0070, B:59:0x0091, B:66:0x00a2, B:62:0x0099, B:64:0x009d, B:65:0x00a0, B:67:0x00a8, B:71:0x00b1, B:73:0x00b5, B:75:0x00b9, B:77:0x00bd, B:81:0x00c6, B:78:0x00c0, B:80:0x00c4, B:70:0x00ad, B:47:0x007a, B:49:0x007e, B:50:0x0081, B:34:0x0055, B:36:0x005b, B:38:0x005f, B:39:0x0062, B:40:0x0064), top: B:87:0x0030 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean g(q qVar) {
        q qVar2;
        q qVar3;
        q qVar4 = (q) qVar.d;
        q qVar5 = qVar.h;
        if (qVar5 == null) {
            this.f = qVar4;
        } else {
            qVar5.d = qVar4;
        }
        if (qVar4 != null) {
            qVar4.h = qVar5;
        }
        if (this.f == null) {
            this.e = null;
            return true;
        }
        q qVar6 = this.e;
        if (qVar6 == null || qVar6.g == null || (qVar2 = qVar6.f) == null || qVar2.f == null) {
            return true;
        }
        e();
        try {
            q qVar7 = qVar.f;
            q qVar8 = qVar.g;
            if (qVar7 == null || qVar8 == null) {
                if (qVar7 == null) {
                    if (qVar8 != null) {
                        qVar7 = qVar8;
                    }
                    qVar7 = qVar;
                }
                if (qVar7 != qVar) {
                    q qVar9 = qVar.e;
                    qVar7.e = qVar9;
                    if (qVar9 == null) {
                        qVar6 = qVar7;
                    } else if (qVar == qVar9.f) {
                        qVar9.f = qVar7;
                    } else {
                        qVar9.g = qVar7;
                    }
                    qVar.e = null;
                    qVar.g = null;
                    qVar.f = null;
                }
                if (!qVar.i) {
                    qVar6 = b(qVar6, qVar7);
                }
                this.e = qVar6;
                if (qVar == qVar7 && (qVar3 = qVar.e) != null) {
                    if (qVar != qVar3.f) {
                        qVar3.f = null;
                    } else if (qVar == qVar3.g) {
                        qVar3.g = null;
                    }
                    qVar.e = null;
                }
                this.lockState = 0;
                return false;
            }
            q qVar10 = qVar8;
            while (true) {
                q qVar11 = qVar10.f;
                if (qVar11 == null) {
                    break;
                }
                qVar10 = qVar11;
            }
            boolean z = qVar10.i;
            qVar10.i = qVar.i;
            qVar.i = z;
            q qVar12 = qVar10.g;
            q qVar13 = qVar.e;
            if (qVar10 == qVar8) {
                qVar.e = qVar10;
                qVar10.g = qVar;
            } else {
                q qVar14 = qVar10.e;
                qVar.e = qVar14;
                if (qVar14 != null) {
                    if (qVar10 == qVar14.f) {
                        qVar14.f = qVar;
                    } else {
                        qVar14.g = qVar;
                    }
                }
                qVar10.g = qVar8;
                qVar8.e = qVar10;
            }
            qVar.f = null;
            qVar.g = qVar12;
            if (qVar12 != null) {
                qVar12.e = qVar;
            }
            qVar10.f = qVar7;
            qVar7.e = qVar10;
            qVar10.e = qVar13;
            if (qVar13 == null) {
                qVar6 = qVar10;
            } else if (qVar == qVar13.f) {
                qVar13.f = qVar10;
            } else {
                qVar13.g = qVar10;
            }
            if (qVar12 != null) {
                qVar7 = qVar12;
                if (qVar7 != qVar) {
                }
                if (!qVar.i) {
                }
                this.e = qVar6;
                if (qVar == qVar7) {
                    if (qVar != qVar3.f) {
                    }
                    qVar.e = null;
                }
                this.lockState = 0;
                return false;
            }
            qVar7 = qVar;
            if (qVar7 != qVar) {
            }
            if (!qVar.i) {
            }
            this.e = qVar6;
            if (qVar == qVar7) {
            }
            this.lockState = 0;
            return false;
        } catch (Throwable th) {
            this.lockState = 0;
            throw th;
        }
    }
}
