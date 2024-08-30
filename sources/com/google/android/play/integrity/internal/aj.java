package com.google.android.play.integrity.internal;
/* loaded from: classes.dex */
public final class aj implements an {
    private static final Object a = new Object();
    private volatile an b;
    private volatile Object c = a;

    private aj(an anVar) {
        this.b = anVar;
    }

    public static an b(an anVar) {
        return anVar instanceof aj ? anVar : new aj(anVar);
    }

    @Override // com.google.android.play.integrity.internal.an
    public final Object a() {
        Object obj = this.c;
        Object obj2 = a;
        if (obj == obj2) {
            synchronized (this) {
                try {
                    obj = this.c;
                    if (obj == obj2) {
                        obj = this.b.a();
                        Object obj3 = this.c;
                        if (obj3 != obj2 && obj3 != obj) {
                            throw new IllegalStateException("Scoped provider was invoked recursively returning different results: " + obj3 + " & " + obj + ". This is likely due to a circular dependency.");
                        }
                        this.c = obj;
                        this.b = null;
                    }
                } finally {
                }
            }
        }
        return obj;
    }
}
