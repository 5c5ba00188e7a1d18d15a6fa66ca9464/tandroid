package j$.util;

import j$.util.Map;
import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Function;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class g implements java.util.Map, Serializable, Map {
    private final java.util.Map a;
    final Object b;
    private transient Set c;
    private transient Set d;
    private transient Collection e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(java.util.Map map) {
        map.getClass();
        this.a = map;
        this.b = this;
    }

    private static Set a(Set set, Object obj) {
        Constructor constructor;
        Constructor constructor2;
        constructor = DesugarCollections.f;
        if (constructor == null) {
            return Collections.synchronizedSet(set);
        }
        try {
            constructor2 = DesugarCollections.f;
            return (Set) constructor2.newInstance(set, obj);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new Error("Unable to instantiate a synchronized list.", e);
        }
    }

    @Override // java.util.Map
    public final void clear() {
        synchronized (this.b) {
            this.a.clear();
        }
    }

    @Override // j$.util.Map
    public final Object compute(Object obj, BiFunction biFunction) {
        Object a;
        synchronized (this.b) {
            a = Map.-EL.a(this.a, obj, biFunction);
        }
        return a;
    }

    @Override // java.util.Map
    public final /* synthetic */ Object compute(Object obj, java.util.function.BiFunction biFunction) {
        return compute(obj, BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // j$.util.Map
    public final Object computeIfAbsent(Object obj, Function function) {
        Object $default$computeIfAbsent;
        Object apply;
        synchronized (this.b) {
            java.util.Map map = this.a;
            if (map instanceof Map) {
                $default$computeIfAbsent = ((Map) map).computeIfAbsent(obj, function);
            } else if (map instanceof ConcurrentMap) {
                ConcurrentMap concurrentMap = (ConcurrentMap) map;
                function.getClass();
                Object obj2 = concurrentMap.get(obj);
                $default$computeIfAbsent = (obj2 == null && (apply = function.apply(obj)) != null && (obj2 = concurrentMap.putIfAbsent(obj, apply)) == null) ? apply : obj2;
            } else {
                $default$computeIfAbsent = Map.-CC.$default$computeIfAbsent(map, obj, function);
            }
        }
        return $default$computeIfAbsent;
    }

    @Override // java.util.Map
    public final /* synthetic */ Object computeIfAbsent(Object obj, java.util.function.Function function) {
        return computeIfAbsent(obj, Function.VivifiedWrapper.convert(function));
    }

    @Override // j$.util.Map
    public final Object computeIfPresent(Object obj, BiFunction biFunction) {
        Object $default$computeIfPresent;
        synchronized (this.b) {
            java.util.Map map = this.a;
            if (map instanceof Map) {
                $default$computeIfPresent = ((Map) map).computeIfPresent(obj, biFunction);
            } else {
                if (map instanceof ConcurrentMap) {
                    ConcurrentMap concurrentMap = (ConcurrentMap) map;
                    biFunction.getClass();
                    while (true) {
                        Object obj2 = concurrentMap.get(obj);
                        if (obj2 == null) {
                            $default$computeIfPresent = obj2;
                            break;
                        }
                        Object apply = biFunction.apply(obj, obj2);
                        if (apply != null) {
                            if (concurrentMap.replace(obj, obj2, apply)) {
                                $default$computeIfPresent = apply;
                                break;
                            }
                        } else if (concurrentMap.remove(obj, obj2)) {
                            $default$computeIfPresent = null;
                            break;
                        }
                    }
                }
                $default$computeIfPresent = Map.-CC.$default$computeIfPresent(map, obj, biFunction);
            }
        }
        return $default$computeIfPresent;
    }

    @Override // java.util.Map
    public final /* synthetic */ Object computeIfPresent(Object obj, java.util.function.BiFunction biFunction) {
        return computeIfPresent(obj, BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // java.util.Map
    public final boolean containsKey(Object obj) {
        boolean containsKey;
        synchronized (this.b) {
            containsKey = this.a.containsKey(obj);
        }
        return containsKey;
    }

    @Override // java.util.Map
    public final boolean containsValue(Object obj) {
        boolean containsValue;
        synchronized (this.b) {
            containsValue = this.a.containsValue(obj);
        }
        return containsValue;
    }

    @Override // java.util.Map
    public final Set entrySet() {
        Set set;
        synchronized (this.b) {
            try {
                if (this.d == null) {
                    this.d = a(this.a.entrySet(), this.b);
                }
                set = this.d;
            } catch (Throwable th) {
                throw th;
            }
        }
        return set;
    }

    @Override // java.util.Map
    public final boolean equals(Object obj) {
        boolean equals;
        if (this == obj) {
            return true;
        }
        synchronized (this.b) {
            equals = this.a.equals(obj);
        }
        return equals;
    }

    @Override // j$.util.Map
    public final void forEach(BiConsumer biConsumer) {
        synchronized (this.b) {
            Map.-EL.forEach(this.a, biConsumer);
        }
    }

    @Override // java.util.Map
    public final /* synthetic */ void forEach(java.util.function.BiConsumer biConsumer) {
        forEach(BiConsumer.VivifiedWrapper.convert(biConsumer));
    }

    @Override // java.util.Map
    public final Object get(Object obj) {
        Object obj2;
        synchronized (this.b) {
            obj2 = this.a.get(obj);
        }
        return obj2;
    }

    @Override // java.util.Map, j$.util.Map
    public final Object getOrDefault(Object obj, Object obj2) {
        Object orDefault;
        synchronized (this.b) {
            orDefault = Map.-EL.getOrDefault(this.a, obj, obj2);
        }
        return orDefault;
    }

    @Override // java.util.Map
    public final int hashCode() {
        int hashCode;
        synchronized (this.b) {
            hashCode = this.a.hashCode();
        }
        return hashCode;
    }

    @Override // java.util.Map
    public final boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.b) {
            isEmpty = this.a.isEmpty();
        }
        return isEmpty;
    }

    @Override // java.util.Map
    public final Set keySet() {
        Set set;
        synchronized (this.b) {
            try {
                if (this.c == null) {
                    this.c = a(this.a.keySet(), this.b);
                }
                set = this.c;
            } catch (Throwable th) {
                throw th;
            }
        }
        return set;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0022, code lost:
    
        r3 = r7.apply(r2, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0026, code lost:
    
        if (r3 == null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0034, code lost:
    
        if (r1.remove(r5, r2) == false) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0036, code lost:
    
        r6 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x002c, code lost:
    
        if (r1.replace(r5, r2, r3) == false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x002e, code lost:
    
        r6 = r3;
     */
    @Override // j$.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object merge(Object obj, Object obj2, BiFunction biFunction) {
        Object $default$merge;
        synchronized (this.b) {
            java.util.Map map = this.a;
            if (map instanceof Map) {
                $default$merge = ((Map) map).merge(obj, obj2, biFunction);
            } else if (map instanceof ConcurrentMap) {
                ConcurrentMap concurrentMap = (ConcurrentMap) map;
                biFunction.getClass();
                obj2.getClass();
                loop0: while (true) {
                    Object obj3 = concurrentMap.get(obj);
                    while (true) {
                        if (obj3 != null) {
                            break;
                        }
                        obj3 = concurrentMap.putIfAbsent(obj, obj2);
                        if (obj3 == null) {
                            break loop0;
                        }
                    }
                }
                $default$merge = obj2;
            } else {
                $default$merge = Map.-CC.$default$merge(map, obj, obj2, biFunction);
            }
        }
        return $default$merge;
    }

    @Override // java.util.Map
    public final /* synthetic */ Object merge(Object obj, Object obj2, java.util.function.BiFunction biFunction) {
        return merge(obj, obj2, BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // java.util.Map
    public final Object put(Object obj, Object obj2) {
        Object put;
        synchronized (this.b) {
            put = this.a.put(obj, obj2);
        }
        return put;
    }

    @Override // java.util.Map
    public final void putAll(java.util.Map map) {
        synchronized (this.b) {
            this.a.putAll(map);
        }
    }

    @Override // java.util.Map, j$.util.Map
    public final Object putIfAbsent(Object obj, Object obj2) {
        Object b;
        synchronized (this.b) {
            b = Map.-EL.b(this.a, obj, obj2);
        }
        return b;
    }

    @Override // java.util.Map
    public final Object remove(Object obj) {
        Object remove;
        synchronized (this.b) {
            remove = this.a.remove(obj);
        }
        return remove;
    }

    @Override // java.util.Map, j$.util.Map
    public final boolean remove(Object obj, Object obj2) {
        boolean remove;
        synchronized (this.b) {
            java.util.Map map = this.a;
            remove = map instanceof Map ? ((Map) map).remove(obj, obj2) : Map.-CC.$default$remove(map, obj, obj2);
        }
        return remove;
    }

    @Override // java.util.Map, j$.util.Map
    public final Object replace(Object obj, Object obj2) {
        Object replace;
        synchronized (this.b) {
            java.util.Map map = this.a;
            replace = map instanceof Map ? ((Map) map).replace(obj, obj2) : Map.-CC.$default$replace(map, obj, obj2);
        }
        return replace;
    }

    @Override // java.util.Map, j$.util.Map
    public final boolean replace(Object obj, Object obj2, Object obj3) {
        boolean replace;
        synchronized (this.b) {
            java.util.Map map = this.a;
            replace = map instanceof Map ? ((Map) map).replace(obj, obj2, obj3) : Map.-CC.$default$replace(map, obj, obj2, obj3);
        }
        return replace;
    }

    @Override // j$.util.Map
    public final void replaceAll(BiFunction biFunction) {
        synchronized (this.b) {
            java.util.Map map = this.a;
            if (map instanceof Map) {
                ((Map) map).replaceAll(biFunction);
            } else if (map instanceof ConcurrentMap) {
                ConcurrentMap concurrentMap = (ConcurrentMap) map;
                biFunction.getClass();
                j$.util.concurrent.s sVar = new j$.util.concurrent.s(0, concurrentMap, biFunction);
                if (concurrentMap instanceof j$.util.concurrent.t) {
                    ((j$.util.concurrent.t) concurrentMap).forEach(sVar);
                } else {
                    j$.com.android.tools.r8.a.e(concurrentMap, sVar);
                }
            } else {
                Map.-CC.$default$replaceAll(map, biFunction);
            }
        }
    }

    @Override // java.util.Map
    public final /* synthetic */ void replaceAll(java.util.function.BiFunction biFunction) {
        replaceAll(BiFunction.VivifiedWrapper.convert(biFunction));
    }

    @Override // java.util.Map
    public final int size() {
        int size;
        synchronized (this.b) {
            size = this.a.size();
        }
        return size;
    }

    public final String toString() {
        String obj;
        synchronized (this.b) {
            obj = this.a.toString();
        }
        return obj;
    }

    @Override // java.util.Map
    public final Collection values() {
        Collection collection;
        Constructor constructor;
        Constructor constructor2;
        Collection collection2;
        synchronized (this.b) {
            try {
                if (this.e == null) {
                    Collection values = this.a.values();
                    Object obj = this.b;
                    constructor = DesugarCollections.e;
                    if (constructor == null) {
                        collection2 = Collections.synchronizedCollection(values);
                    } else {
                        try {
                            constructor2 = DesugarCollections.e;
                            collection2 = (Collection) constructor2.newInstance(values, obj);
                        } catch (IllegalAccessException e) {
                            e = e;
                            throw new Error("Unable to instantiate a synchronized list.", e);
                        } catch (InstantiationException e2) {
                            e = e2;
                            throw new Error("Unable to instantiate a synchronized list.", e);
                        } catch (InvocationTargetException e3) {
                            e = e3;
                            throw new Error("Unable to instantiate a synchronized list.", e);
                        }
                    }
                    this.e = collection2;
                }
                collection = this.e;
            } finally {
            }
        }
        return collection;
    }
}
