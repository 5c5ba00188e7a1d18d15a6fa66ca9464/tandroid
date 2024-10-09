package j$.util;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Function;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes2.dex */
public interface Map<K, V> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
        /* JADX WARN: Multi-variable type inference failed */
        public static Object $default$compute(java.util.Map map, Object obj, BiFunction biFunction) {
            biFunction.getClass();
            Object obj2 = map.get(obj);
            Object apply = biFunction.apply(obj, obj2);
            if (apply != null) {
                map.put(obj, apply);
                return apply;
            }
            if (obj2 == null && !map.containsKey(obj)) {
                return null;
            }
            map.remove(obj);
            return null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static Object $default$computeIfAbsent(java.util.Map map, Object obj, Function function) {
            Object apply;
            function.getClass();
            Object obj2 = map.get(obj);
            if (obj2 != null || (apply = function.apply(obj)) == null) {
                return obj2;
            }
            map.put(obj, apply);
            return apply;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static Object $default$computeIfPresent(java.util.Map map, Object obj, BiFunction biFunction) {
            biFunction.getClass();
            Object obj2 = map.get(obj);
            if (obj2 != null) {
                Object apply = biFunction.apply(obj, obj2);
                if (apply != null) {
                    map.put(obj, apply);
                    return apply;
                }
                map.remove(obj);
            }
            return null;
        }

        public static void $default$forEach(java.util.Map map, BiConsumer biConsumer) {
            biConsumer.getClass();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                try {
                    biConsumer.accept(entry.getKey(), entry.getValue());
                } catch (IllegalStateException e) {
                    throw new ConcurrentModificationException(e);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static Object $default$merge(java.util.Map map, Object obj, Object obj2, BiFunction biFunction) {
            biFunction.getClass();
            obj2.getClass();
            Object obj3 = map.get(obj);
            if (obj3 != null) {
                obj2 = biFunction.apply(obj3, obj2);
            }
            if (obj2 == null) {
                map.remove(obj);
            } else {
                map.put(obj, obj2);
            }
            return obj2;
        }

        public static Object $default$putIfAbsent(java.util.Map map, Object obj, Object obj2) {
            Object obj3 = map.get(obj);
            return obj3 == null ? map.put(obj, obj2) : obj3;
        }

        public static boolean $default$remove(java.util.Map map, Object obj, Object obj2) {
            Object obj3 = map.get(obj);
            if (!a.A(obj3, obj2)) {
                return false;
            }
            if (obj3 == null && !map.containsKey(obj)) {
                return false;
            }
            map.remove(obj);
            return true;
        }

        public static Object $default$replace(java.util.Map map, Object obj, Object obj2) {
            Object obj3 = map.get(obj);
            return (obj3 != null || map.containsKey(obj)) ? map.put(obj, obj2) : obj3;
        }

        public static boolean $default$replace(java.util.Map map, Object obj, Object obj2, Object obj3) {
            Object obj4 = map.get(obj);
            if (!a.A(obj4, obj2)) {
                return false;
            }
            if (obj4 == null && !map.containsKey(obj)) {
                return false;
            }
            map.put(obj, obj3);
            return true;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static void $default$replaceAll(java.util.Map map, BiFunction biFunction) {
            biFunction.getClass();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                try {
                    try {
                        entry.setValue(biFunction.apply(entry.getKey(), entry.getValue()));
                    } catch (IllegalStateException e) {
                        throw new ConcurrentModificationException(e);
                    }
                } catch (IllegalStateException e2) {
                    throw new ConcurrentModificationException(e2);
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -EL {
        /* JADX WARN: Multi-variable type inference failed */
        public static Object a(java.util.Map map, Object obj, BiFunction biFunction) {
            Object apply;
            if (map instanceof Map) {
                return ((Map) map).compute(obj, biFunction);
            }
            if (!(map instanceof ConcurrentMap)) {
                return -CC.$default$compute(map, obj, biFunction);
            }
            ConcurrentMap concurrentMap = (ConcurrentMap) map;
            biFunction.getClass();
            loop0: while (true) {
                Object obj2 = concurrentMap.get(obj);
                while (true) {
                    apply = biFunction.apply(obj, obj2);
                    if (apply == null) {
                        apply = null;
                        if ((obj2 == null && !concurrentMap.containsKey(obj)) || concurrentMap.remove(obj, obj2)) {
                            break;
                        }
                    } else if (obj2 == null) {
                        obj2 = concurrentMap.putIfAbsent(obj, apply);
                        if (obj2 == null) {
                            break loop0;
                        }
                    } else if (concurrentMap.replace(obj, obj2, apply)) {
                        break;
                    }
                }
            }
            return apply;
        }

        public static /* synthetic */ Object b(java.util.Map map, Object obj, Object obj2) {
            return map instanceof Map ? ((Map) map).putIfAbsent(obj, obj2) : -CC.$default$putIfAbsent(map, obj, obj2);
        }

        public static /* synthetic */ void forEach(java.util.Map map, BiConsumer biConsumer) {
            if (map instanceof Map) {
                ((Map) map).forEach(biConsumer);
            } else if (map instanceof ConcurrentMap) {
                j$.com.android.tools.r8.a.e((ConcurrentMap) map, biConsumer);
            } else {
                -CC.$default$forEach(map, biConsumer);
            }
        }

        public static Object getOrDefault(java.util.Map map, Object obj, Object obj2) {
            if (map instanceof Map) {
                return ((Map) map).getOrDefault(obj, obj2);
            }
            if (map instanceof ConcurrentMap) {
                Object obj3 = ((ConcurrentMap) map).get(obj);
                return obj3 != null ? obj3 : obj2;
            }
            Object obj4 = map.get(obj);
            return (obj4 != null || map.containsKey(obj)) ? obj4 : obj2;
        }
    }

    V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction);

    V computeIfAbsent(K k, Function<? super K, ? extends V> function);

    V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction);

    void forEach(BiConsumer<? super K, ? super V> biConsumer);

    V getOrDefault(Object obj, V v);

    V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction);

    V putIfAbsent(K k, V v);

    boolean remove(Object obj, Object obj2);

    V replace(K k, V v);

    boolean replace(K k, V v, V v2);

    void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction);
}
