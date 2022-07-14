package j$.util.concurrent;

import com.microsoft.appcenter.ingestion.models.CommonProperties;
import j$.util.Collection;
import j$.util.Iterator;
import j$.util.Spliterator;
import j$.util.function.Consumer;
import j$.util.function.IntFunction;
import j$.util.function.Predicate;
import j$.wrappers.C$r8$wrapper$java$util$function$BiConsumer$VWRP;
import j$.wrappers.C$r8$wrapper$java$util$function$BiFunction$VWRP;
import j$.wrappers.C$r8$wrapper$java$util$function$Consumer$VWRP;
import j$.wrappers.C$r8$wrapper$java$util$function$Function$VWRP;
import j$.wrappers.C$r8$wrapper$java$util$function$IntFunction$VWRP;
import j$.wrappers.C$r8$wrapper$java$util$function$Predicate$VWRP;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import sun.misc.Unsafe;
/* loaded from: classes2.dex */
public class ConcurrentHashMap<K, V> extends AbstractMap<K, V> implements java.util.concurrent.ConcurrentMap<K, V>, Serializable, ConcurrentMap<K, V> {
    private static final long ABASE;
    private static final int ASHIFT;
    private static final long BASECOUNT;
    private static final long CELLSBUSY;
    private static final long CELLVALUE;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    static final int HASH_BITS = Integer.MAX_VALUE;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_ARRAY_SIZE = 2147483639;
    private static final int MIN_TRANSFER_STRIDE = 16;
    static final int MIN_TREEIFY_CAPACITY = 64;
    static final int MOVED = -1;
    static final int RESERVED = -3;
    private static final long SIZECTL;
    private static final long TRANSFERINDEX;
    static final int TREEBIN = -2;
    static final int TREEIFY_THRESHOLD = 8;
    private static final Unsafe U;
    static final int UNTREEIFY_THRESHOLD = 6;
    private static final long serialVersionUID = 7249069246763182397L;
    private volatile transient long baseCount;
    private volatile transient int cellsBusy;
    private volatile transient CounterCell[] counterCells;
    private transient EntrySetView<K, V> entrySet;
    private transient KeySetView<K, V> keySet;
    private volatile transient Node<K, V>[] nextTable;
    private volatile transient int sizeCtl;
    volatile transient Node<K, V>[] table;
    private volatile transient int transferIndex;
    private transient ValuesView<K, V> values;
    private static int RESIZE_STAMP_BITS = 16;
    private static final int MAX_RESIZERS = 65535;
    private static final int RESIZE_STAMP_SHIFT = 16;
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("segments", Segment[].class), new ObjectStreamField("segmentMask", Integer.TYPE), new ObjectStreamField("segmentShift", Integer.TYPE)};

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public /* synthetic */ Object compute(Object obj, BiFunction biFunction) {
        return compute((ConcurrentHashMap<K, V>) obj, (j$.util.function.BiFunction<? super ConcurrentHashMap<K, V>, ? super V, ? extends V>) C$r8$wrapper$java$util$function$BiFunction$VWRP.convert(biFunction));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public /* synthetic */ Object computeIfAbsent(Object obj, Function function) {
        return computeIfAbsent((ConcurrentHashMap<K, V>) obj, (j$.util.function.Function<? super ConcurrentHashMap<K, V>, ? extends V>) C$r8$wrapper$java$util$function$Function$VWRP.convert(function));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public /* synthetic */ Object computeIfPresent(Object obj, BiFunction biFunction) {
        return computeIfPresent((ConcurrentHashMap<K, V>) obj, (j$.util.function.BiFunction<? super ConcurrentHashMap<K, V>, ? super V, ? extends V>) C$r8$wrapper$java$util$function$BiFunction$VWRP.convert(biFunction));
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public /* synthetic */ void forEach(BiConsumer biConsumer) {
        forEach(C$r8$wrapper$java$util$function$BiConsumer$VWRP.convert(biConsumer));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public /* synthetic */ Object merge(Object obj, Object obj2, BiFunction biFunction) {
        return merge((ConcurrentHashMap<K, V>) obj, obj2, C$r8$wrapper$java$util$function$BiFunction$VWRP.convert(biFunction));
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public /* synthetic */ void replaceAll(BiFunction biFunction) {
        replaceAll(C$r8$wrapper$java$util$function$BiFunction$VWRP.convert(biFunction));
    }

    static {
        try {
            Unsafe unsafe = DesugarUnsafe.getUnsafe();
            U = unsafe;
            SIZECTL = unsafe.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("sizeCtl"));
            TRANSFERINDEX = unsafe.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("transferIndex"));
            BASECOUNT = unsafe.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("baseCount"));
            CELLSBUSY = unsafe.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("cellsBusy"));
            CELLVALUE = unsafe.objectFieldOffset(CounterCell.class.getDeclaredField(CommonProperties.VALUE));
            ABASE = unsafe.arrayBaseOffset(Node[].class);
            int scale = unsafe.arrayIndexScale(Node[].class);
            if (((scale - 1) & scale) != 0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /* loaded from: classes2.dex */
    public static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        volatile Node<K, V> next;
        volatile V val;

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        Node(int hash, K key, V val, Node<K, V> node) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.next = node;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        @Override // java.util.Map.Entry
        public final K getKey() {
            return this.key;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        @Override // java.util.Map.Entry
        public final V getValue() {
            return this.val;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        @Override // java.util.Map.Entry
        public final int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        public final String toString() {
            return this.key + "=" + this.val;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        @Override // java.util.Map.Entry
        public final V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        @Override // java.util.Map.Entry
        public final boolean equals(Object o) {
            Map.Entry<?, ?> e;
            Object k;
            Object v;
            K k2;
            Object u;
            return (o instanceof Map.Entry) && (k = (e = (Map.Entry) o).getKey()) != null && (v = e.getValue()) != null && (k == (k2 = this.key) || k.equals(k2)) && (v == (u = this.val) || v.equals(u));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        Node<K, V> find(int h, Object k) {
            Node<K, V> node;
            K ek;
            Node<K, V> node2 = this;
            if (k != null) {
                do {
                    if (node2.hash == h && ((ek = node2.key) == k || (ek != null && k.equals(ek)))) {
                        return node2;
                    }
                    node = node2.next;
                    node2 = node;
                } while (node != null);
                return null;
            }
            return null;
        }
    }

    static final int spread(int h) {
        return ((h >>> 16) ^ h) & Integer.MAX_VALUE;
    }

    private static final int tableSizeFor(int c) {
        int n = c - 1;
        int n2 = n | (n >>> 1);
        int n3 = n2 | (n2 >>> 2);
        int n4 = n3 | (n3 >>> 4);
        int n5 = n4 | (n4 >>> 8);
        int n6 = n5 | (n5 >>> 16);
        if (n6 < 0) {
            return 1;
        }
        if (n6 < 1073741824) {
            return n6 + 1;
        }
        return 1073741824;
    }

    static Class<?> comparableClassFor(Object x) {
        Type[] as;
        if (x instanceof Comparable) {
            Class<?> c = x.getClass();
            if (c == String.class) {
                return c;
            }
            Type[] ts = c.getGenericInterfaces();
            if (ts != null) {
                for (Type t : ts) {
                    if (t instanceof ParameterizedType) {
                        ParameterizedType p = (ParameterizedType) t;
                        if (p.getRawType() == Comparable.class && (as = p.getActualTypeArguments()) != null && as.length == 1 && as[0] == c) {
                            return c;
                        }
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    static int compareComparables(Class<?> kc, Object k, Object x) {
        if (x == null || x.getClass() != kc) {
            return 0;
        }
        return ((Comparable) k).compareTo(x);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    static final <K, V> Node<K, V> tabAt(Node<K, V>[] nodeArr, int i) {
        return (Node) U.getObjectVolatile(nodeArr, (i << ASHIFT) + ABASE);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    static final <K, V> boolean casTabAt(Node<K, V>[] nodeArr, int i, Node<K, V> node, Node<K, V> node2) {
        return U.compareAndSwapObject(nodeArr, ABASE + (i << ASHIFT), node, node2);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    static final <K, V> void setTabAt(Node<K, V>[] nodeArr, int i, Node<K, V> node) {
        U.putObjectVolatile(nodeArr, (i << ASHIFT) + ABASE, node);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    public ConcurrentHashMap() {
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    public ConcurrentHashMap(int initialCapacity) {
        int cap;
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        if (initialCapacity >= 536870912) {
            cap = 1073741824;
        } else {
            cap = tableSizeFor((initialCapacity >>> 1) + initialCapacity + 1);
        }
        this.sizeCtl = cap;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    public ConcurrentHashMap(Map<? extends K, ? extends V> m) {
        this.sizeCtl = 16;
        putAll(m);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    public ConcurrentHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, 1);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    public ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        if (loadFactor <= 0.0f || initialCapacity < 0 || concurrencyLevel <= 0) {
            throw new IllegalArgumentException();
        }
        double d = (initialCapacity < concurrencyLevel ? concurrencyLevel : initialCapacity) / loadFactor;
        Double.isNaN(d);
        long size = (long) (d + 1.0d);
        int cap = size >= 1073741824 ? 1073741824 : tableSizeFor((int) size);
        this.sizeCtl = cap;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public int size() {
        long n = sumCount();
        if (n < 0) {
            return 0;
        }
        if (n <= 2147483647L) {
            return (int) n;
        }
        return Integer.MAX_VALUE;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public boolean isEmpty() {
        return sumCount() <= 0;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public V get(Object key) {
        int n;
        K ek;
        int h = spread(key.hashCode());
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null && (n = nodeArr.length) > 0) {
            Node<K, V> tabAt = tabAt(nodeArr, (n - 1) & h);
            Node<K, V> node = tabAt;
            if (tabAt != null) {
                int eh = node.hash;
                if (eh == h) {
                    K ek2 = node.key;
                    if (ek2 == key || (ek2 != null && key.equals(ek2))) {
                        return node.val;
                    }
                } else if (eh < 0) {
                    Node<K, V> find = node.find(h, key);
                    if (find == null) {
                        return null;
                    }
                    return find.val;
                }
                while (true) {
                    Node<K, V> node2 = node.next;
                    node = node2;
                    if (node2 == null) {
                        break;
                    } else if (node.hash != h || ((ek = node.key) != key && (ek == null || !key.equals(ek)))) {
                    }
                }
                return node.val;
            }
        }
        return null;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> advance = traverser.advance();
                if (advance == null) {
                    break;
                }
                V v = advance.val;
                if (v == value) {
                    return true;
                }
                if (v != null && value.equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public V put(K key, V value) {
        return putVal(key, value, false);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    final V putVal(K key, V value, boolean onlyIfAbsent) {
        K ek;
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        int hash = spread(key.hashCode());
        int binCount = 0;
        Node<K, V>[] nodeArr = this.table;
        while (true) {
            if (nodeArr != null) {
                int n = nodeArr.length;
                if (n != 0) {
                    int i = (n - 1) & hash;
                    Node<K, V> tabAt = tabAt(nodeArr, i);
                    if (tabAt == null) {
                        if (casTabAt(nodeArr, i, null, new Node(hash, key, value, null))) {
                            break;
                        }
                    } else {
                        int fh = tabAt.hash;
                        if (fh == -1) {
                            nodeArr = helpTransfer(nodeArr, tabAt);
                        } else {
                            V oldVal = null;
                            synchronized (tabAt) {
                                if (tabAt(nodeArr, i) == tabAt) {
                                    if (fh >= 0) {
                                        binCount = 1;
                                        Node<K, V> node = tabAt;
                                        while (true) {
                                            if (node.hash != hash || ((ek = node.key) != key && (ek == null || !key.equals(ek)))) {
                                                Node<K, V> node2 = node;
                                                Node<K, V> node3 = node.next;
                                                node = node3;
                                                if (node3 == null) {
                                                    node2.next = new Node<>(hash, key, value, null);
                                                    break;
                                                }
                                                binCount++;
                                            }
                                        }
                                        oldVal = node.val;
                                        if (!onlyIfAbsent) {
                                            node.val = value;
                                        }
                                    } else if (tabAt instanceof TreeBin) {
                                        binCount = 2;
                                        TreeNode<K, V> putTreeVal = ((TreeBin) tabAt).putTreeVal(hash, key, value);
                                        if (putTreeVal != null) {
                                            oldVal = putTreeVal.val;
                                            if (!onlyIfAbsent) {
                                                putTreeVal.val = value;
                                            }
                                        }
                                    }
                                }
                            }
                            if (binCount != 0) {
                                if (binCount >= 8) {
                                    treeifyBin(nodeArr, i);
                                }
                                if (oldVal != null) {
                                    return oldVal;
                                }
                            }
                        }
                    }
                }
            }
            nodeArr = initTable();
        }
        addCount(1L, binCount);
        return null;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        tryPresize(m.size());
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            putVal(e.getKey(), e.getValue(), false);
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public V remove(Object key) {
        return replaceNode(key, null, null);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
    final V replaceNode(Object key, V value, Object cv) {
        int i;
        Node<K, V> tabAt;
        TreeNode<K, V> findTreeNode;
        K ek;
        Object obj = key;
        int hash = spread(key.hashCode());
        Node<K, V>[] nodeArr = this.table;
        while (nodeArr != null) {
            int n = nodeArr.length;
            if (n != 0 && (tabAt = tabAt(nodeArr, (i = (n - 1) & hash))) != null) {
                int fh = tabAt.hash;
                if (fh == -1) {
                    nodeArr = helpTransfer(nodeArr, tabAt);
                } else {
                    V oldVal = null;
                    boolean validated = false;
                    synchronized (tabAt) {
                        if (tabAt(nodeArr, i) == tabAt) {
                            if (fh >= 0) {
                                validated = true;
                                Node<K, V> node = tabAt;
                                Node<K, V> node2 = null;
                                while (true) {
                                    if (node.hash != hash || ((ek = node.key) != obj && (ek == null || !obj.equals(ek)))) {
                                        node2 = node;
                                        Node<K, V> node3 = node.next;
                                        node = node3;
                                        if (node3 == null) {
                                            break;
                                        }
                                    }
                                }
                                V ev = node.val;
                                if (cv == null || cv == ev || (ev != null && cv.equals(ev))) {
                                    oldVal = ev;
                                    if (value != null) {
                                        node.val = value;
                                    } else if (node2 != null) {
                                        node2.next = node.next;
                                    } else {
                                        setTabAt(nodeArr, i, node.next);
                                    }
                                }
                            } else if (tabAt instanceof TreeBin) {
                                validated = true;
                                TreeBin treeBin = (TreeBin) tabAt;
                                TreeNode<K, V> treeNode = treeBin.root;
                                if (treeNode != null && (findTreeNode = treeNode.findTreeNode(hash, obj, null)) != null) {
                                    V pv = findTreeNode.val;
                                    if (cv == null || cv == pv || (pv != null && cv.equals(pv))) {
                                        oldVal = pv;
                                        if (value != null) {
                                            findTreeNode.val = value;
                                        } else if (treeBin.removeTreeNode(findTreeNode)) {
                                            setTabAt(nodeArr, i, untreeify(treeBin.first));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (validated) {
                        if (oldVal != null) {
                            if (value == null) {
                                addCount(-1L, -1);
                            }
                            return oldVal;
                        }
                        return null;
                    }
                }
                obj = key;
            } else {
                return null;
            }
        }
        return null;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:0x004f -> B:43:0x0052). Please submit an issue!!! */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public void clear() {
        int i;
        Throwable th;
        Throwable th2;
        Node<K, V> node;
        long delta = 0;
        int i2 = 0;
        Node<K, V>[] nodeArr = this.table;
        while (nodeArr != null && i2 < nodeArr.length) {
            Node<K, V> tabAt = tabAt(nodeArr, i2);
            if (tabAt == null) {
                i2++;
            } else {
                int fh = tabAt.hash;
                if (fh == -1) {
                    nodeArr = helpTransfer(nodeArr, tabAt);
                    i2 = 0;
                } else {
                    synchronized (tabAt) {
                        try {
                            if (tabAt(nodeArr, i2) == tabAt) {
                                if (fh >= 0) {
                                    node = tabAt;
                                } else if (!(tabAt instanceof TreeBin)) {
                                    node = null;
                                } else {
                                    node = ((TreeBin) tabAt).first;
                                }
                                while (node != null) {
                                    delta--;
                                    node = node.next;
                                }
                                int i3 = i2 + 1;
                                try {
                                    setTabAt(nodeArr, i2, null);
                                    i2 = i3;
                                } catch (Throwable th3) {
                                    th = th3;
                                    i = i3;
                                    try {
                                    } catch (Throwable th4) {
                                        int i4 = i;
                                        th2 = th4;
                                        i2 = i4;
                                        Throwable th5 = th2;
                                        i = i2;
                                        th = th5;
                                        throw th;
                                    }
                                    throw th;
                                }
                            }
                        } catch (Throwable th6) {
                            th2 = th6;
                            Throwable th52 = th2;
                            i = i2;
                            th = th52;
                            throw th;
                        }
                    }
                }
            }
        }
        if (delta != 0) {
            addCount(delta, -1);
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public Set<K> keySet() {
        KeySetView<K, V> keySetView = this.keySet;
        if (keySetView != null) {
            return keySetView;
        }
        KeySetView<K, V> keySetView2 = new KeySetView<>(this, null);
        this.keySet = keySetView2;
        return keySetView2;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValuesView != java.util.concurrent.ConcurrentHashMap$ValuesView<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public Collection<V> values() {
        ValuesView<K, V> valuesView = this.values;
        if (valuesView != null) {
            return valuesView;
        }
        ValuesView<K, V> valuesView2 = new ValuesView<>(this);
        this.values = valuesView2;
        return valuesView2;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySetView<K, V> entrySetView = this.entrySet;
        if (entrySetView != null) {
            return entrySetView;
        }
        EntrySetView<K, V> entrySetView2 = new EntrySetView<>(this);
        this.entrySet = entrySetView2;
        return entrySetView2;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public int hashCode() {
        int h = 0;
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> advance = traverser.advance();
                if (advance == null) {
                    break;
                }
                h += advance.key.hashCode() ^ advance.val.hashCode();
            }
        }
        return h;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
    @Override // java.util.AbstractMap
    public String toString() {
        Node<K, V>[] nodeArr = this.table;
        int f = nodeArr == null ? 0 : nodeArr.length;
        Traverser traverser = new Traverser(nodeArr, f, 0, f);
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Node<K, V> advance = traverser.advance();
        Node<K, V> node = advance;
        if (advance != null) {
            while (true) {
                K k = node.key;
                V v = node.val;
                Object obj = "(this Map)";
                sb.append(k == this ? obj : k);
                sb.append('=');
                if (v != this) {
                    obj = v;
                }
                sb.append(obj);
                Node<K, V> advance2 = traverser.advance();
                node = advance2;
                if (advance2 == null) {
                    break;
                }
                sb.append(',');
                sb.append(' ');
            }
        }
        sb.append('}');
        return sb.toString();
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
    @Override // java.util.AbstractMap, java.util.Map, j$.util.Map
    public boolean equals(Object o) {
        Object mv;
        Object v;
        if (o != this) {
            if (!(o instanceof Map)) {
                return false;
            }
            Map<?, ?> m = (Map) o;
            Node<K, V>[] nodeArr = this.table;
            int f = nodeArr == null ? 0 : nodeArr.length;
            Traverser traverser = new Traverser(nodeArr, f, 0, f);
            while (true) {
                Node<K, V> advance = traverser.advance();
                if (advance != null) {
                    V val = advance.val;
                    Object v2 = m.get(advance.key);
                    if (v2 == null || (v2 != val && !v2.equals(val))) {
                        break;
                    }
                } else {
                    for (Map.Entry<?, ?> e : m.entrySet()) {
                        Object mk = e.getKey();
                        if (mk == null || (mv = e.getValue()) == null || (v = get(mk)) == null || (mv != v && !mv.equals(v))) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /* loaded from: classes2.dex */
    static class Segment<K, V> extends ReentrantLock implements Serializable {
        private static final long serialVersionUID = 2249069246763182397L;
        final float loadFactor;

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Segment != java.util.concurrent.ConcurrentHashMap$Segment<K, V> */
        Segment(float lf) {
            this.loadFactor = lf;
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Segment[] != java.util.concurrent.ConcurrentHashMap$Segment<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
    private void writeObject(ObjectOutputStream s) {
        int sshift = 0;
        int ssize = 1;
        while (ssize < 16) {
            sshift++;
            ssize <<= 1;
        }
        int segmentShift = 32 - sshift;
        int segmentMask = ssize - 1;
        Segment[] segmentArr = new Segment[16];
        for (int i = 0; i < segmentArr.length; i++) {
            segmentArr[i] = new Segment(0.75f);
        }
        s.putFields().put("segments", segmentArr);
        s.putFields().put("segmentShift", segmentShift);
        s.putFields().put("segmentMask", segmentMask);
        s.writeFields();
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> advance = traverser.advance();
                if (advance == null) {
                    break;
                }
                s.writeObject(advance.key);
                s.writeObject(advance.val);
            }
        }
        s.writeObject(null);
        s.writeObject(null);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
    private void readObject(ObjectInputStream s) {
        int n;
        int mask;
        long size;
        boolean insertAtFront;
        long j;
        K qk;
        this.sizeCtl = -1;
        s.defaultReadObject();
        long size2 = 0;
        Node<K, V> node = null;
        while (true) {
            Object readObject = s.readObject();
            Object readObject2 = s.readObject();
            if (readObject == null || readObject2 == null) {
                break;
            }
            node = new Node<>(spread(readObject.hashCode()), readObject, readObject2, node);
            size2++;
        }
        if (size2 == 0) {
            this.sizeCtl = 0;
            return;
        }
        if (size2 >= 536870912) {
            n = 1073741824;
        } else {
            int n2 = (int) size2;
            int sz = n2 + (n2 >>> 1);
            n = tableSizeFor(sz + 1);
        }
        Node<K, V>[] nodeArr = new Node[n];
        int mask2 = n - 1;
        long added = 0;
        while (node != null) {
            Node<K, V> node2 = node.next;
            int h = node.hash;
            int j2 = h & mask2;
            Node<K, V> tabAt = tabAt(nodeArr, j2);
            if (tabAt == null) {
                insertAtFront = true;
                size = size2;
                mask = mask2;
            } else {
                K k = node.key;
                if (tabAt.hash < 0) {
                    if (((TreeBin) tabAt).putTreeVal(h, k, node.val) == null) {
                        added++;
                    }
                    insertAtFront = false;
                    size = size2;
                    mask = mask2;
                } else {
                    int binCount = 0;
                    boolean insertAtFront2 = true;
                    long j3 = size2;
                    size = j3;
                    for (Node<K, V> node3 = tabAt; node3 != null; node3 = node3.next) {
                        if (node3.hash != h || ((qk = node3.key) != k && (qk == null || !k.equals(qk)))) {
                            binCount++;
                        }
                        insertAtFront2 = false;
                        break;
                    }
                    if (insertAtFront2 && binCount >= 8) {
                        added++;
                        node.next = tabAt;
                        TreeNode<K, V> treeNode = null;
                        Node<K, V> node4 = node;
                        TreeNode<K, V> treeNode2 = null;
                        while (node4 != null) {
                            int mask3 = mask2;
                            int mask4 = node4.hash;
                            long added2 = added;
                            TreeNode<K, V> treeNode3 = new TreeNode<>(mask4, node4.key, node4.val, null, null);
                            treeNode3.prev = treeNode2;
                            if (treeNode2 == null) {
                                treeNode = treeNode3;
                            } else {
                                treeNode2.next = treeNode3;
                            }
                            treeNode2 = treeNode3;
                            node4 = node4.next;
                            mask2 = mask3;
                            added = added2;
                        }
                        mask = mask2;
                        setTabAt(nodeArr, j2, new TreeBin(treeNode));
                        insertAtFront = false;
                    } else {
                        mask = mask2;
                        insertAtFront = insertAtFront2;
                    }
                }
            }
            if (!insertAtFront) {
                j = 1;
            } else {
                j = 1;
                added++;
                node.next = tabAt;
                setTabAt(nodeArr, j2, node);
            }
            node = node2;
            size2 = size;
            mask2 = mask;
        }
        this.table = nodeArr;
        this.sizeCtl = n - (n >>> 2);
        this.baseCount = added;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.concurrent.ConcurrentMap, j$.util.Map
    public V putIfAbsent(K key, V value) {
        return putVal(key, value, true);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.concurrent.ConcurrentMap, j$.util.Map
    public boolean remove(Object key, Object value) {
        if (key != null) {
            return (value == null || replaceNode(key, null, value) == null) ? false : true;
        }
        throw new NullPointerException();
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.concurrent.ConcurrentMap, j$.util.Map
    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null) {
            throw new NullPointerException();
        }
        return replaceNode(key, newValue, oldValue) != null;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.concurrent.ConcurrentMap, j$.util.Map
    public V replace(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        return replaceNode(key, value, null);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap, j$.util.concurrent.ConcurrentMap, j$.util.Map
    public V getOrDefault(Object key, V defaultValue) {
        V v = get(key);
        return v == null ? defaultValue : v;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.function.BiConsumer != java.util.function.BiConsumer<? super K, ? super V> */
    @Override // j$.util.concurrent.ConcurrentMap, j$.util.Map
    public void forEach(j$.util.function.BiConsumer<? super K, ? super V> biConsumer) {
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> advance = traverser.advance();
                if (advance != null) {
                    biConsumer.accept((K) advance.key, (V) advance.val);
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.function.BiFunction != java.util.function.BiFunction<? super K, ? super V, ? extends V> */
    @Override // j$.util.concurrent.ConcurrentMap, j$.util.Map
    public void replaceAll(j$.util.function.BiFunction<? super K, ? super V, ? extends V> biFunction) {
        V v;
        if (biFunction == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> advance = traverser.advance();
                if (advance != null) {
                    V v2 = advance.val;
                    Object obj = (K) advance.key;
                    do {
                        V newValue = biFunction.apply(obj, v2);
                        if (newValue == null) {
                            throw new NullPointerException();
                        }
                        if (replaceNode(obj, newValue, v2) == null) {
                            v = get(obj);
                            V oldValue = v;
                            v2 = (Object) oldValue;
                        }
                    } while (v != null);
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x0085, code lost:
        r1 = r10.val;
     */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.function.Function != java.util.function.Function<? super K, ? extends V> */
    @Override // j$.util.concurrent.ConcurrentMap, j$.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public V computeIfAbsent(K key, j$.util.function.Function<? super K, ? extends V> function) {
        Throwable th;
        TreeNode<K, V> findTreeNode;
        K ek;
        if (key == null || function == null) {
            throw new NullPointerException();
        }
        int h = spread(key.hashCode());
        V val = null;
        int binCount = 0;
        Node<K, V>[] nodeArr = this.table;
        while (true) {
            if (nodeArr != null) {
                int n = nodeArr.length;
                if (n != 0) {
                    int i = (n - 1) & h;
                    Node<K, V> tabAt = tabAt(nodeArr, i);
                    if (tabAt == null) {
                        ReservationNode reservationNode = new ReservationNode();
                        synchronized (reservationNode) {
                            try {
                                if (casTabAt(nodeArr, i, null, reservationNode)) {
                                    binCount = 1;
                                    Node node = null;
                                    V apply = function.apply(key);
                                    val = apply;
                                    if (apply != null) {
                                        node = new Node(h, key, val, null);
                                    }
                                    try {
                                        setTabAt(nodeArr, i, node);
                                    } catch (Throwable th2) {
                                        th = th2;
                                        while (true) {
                                            try {
                                                break;
                                            } catch (Throwable th3) {
                                                th = th3;
                                            }
                                        }
                                        throw th;
                                    }
                                }
                                if (binCount != 0) {
                                    break;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        }
                    } else {
                        int fh = tabAt.hash;
                        if (fh == -1) {
                            nodeArr = helpTransfer(nodeArr, tabAt);
                        } else {
                            boolean added = false;
                            synchronized (tabAt) {
                                if (tabAt(nodeArr, i) == tabAt) {
                                    if (fh >= 0) {
                                        binCount = 1;
                                        Node<K, V> node2 = tabAt;
                                        while (true) {
                                            if (node2.hash != h || ((ek = node2.key) != key && (ek == null || !key.equals(ek)))) {
                                                Node<K, V> node3 = node2;
                                                Node<K, V> node4 = node2.next;
                                                node2 = node4;
                                                if (node4 == null) {
                                                    V apply2 = function.apply(key);
                                                    val = apply2;
                                                    if (apply2 != null) {
                                                        added = true;
                                                        node3.next = new Node<>(h, key, val, null);
                                                    }
                                                } else {
                                                    binCount++;
                                                }
                                            }
                                        }
                                    } else if (tabAt instanceof TreeBin) {
                                        binCount = 2;
                                        TreeBin treeBin = (TreeBin) tabAt;
                                        TreeNode<K, V> treeNode = treeBin.root;
                                        if (treeNode != null && (findTreeNode = treeNode.findTreeNode(h, key, null)) != null) {
                                            val = findTreeNode.val;
                                        } else {
                                            V apply3 = function.apply(key);
                                            val = apply3;
                                            if (apply3 != null) {
                                                added = true;
                                                treeBin.putTreeVal(h, key, val);
                                            }
                                        }
                                    }
                                }
                            }
                            if (binCount != 0) {
                                if (binCount >= 8) {
                                    treeifyBin(nodeArr, i);
                                }
                                if (!added) {
                                    return val;
                                }
                            }
                        }
                    }
                }
            }
            nodeArr = initTable();
        }
        if (val != null) {
            addCount(1L, binCount);
        }
        return val;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.function.BiFunction != java.util.function.BiFunction<? super K, ? super V, ? extends V> */
    @Override // j$.util.concurrent.ConcurrentMap, j$.util.Map
    public V computeIfPresent(K key, j$.util.function.BiFunction<? super K, ? super V, ? extends V> biFunction) {
        TreeNode<K, V> findTreeNode;
        K ek;
        if (key == null || biFunction == null) {
            throw new NullPointerException();
        }
        int h = spread(key.hashCode());
        V val = null;
        int delta = 0;
        int binCount = 0;
        Node<K, V>[] nodeArr = this.table;
        while (true) {
            if (nodeArr != null) {
                int n = nodeArr.length;
                if (n != 0) {
                    int i = (n - 1) & h;
                    Node<K, V> tabAt = tabAt(nodeArr, i);
                    if (tabAt == null) {
                        break;
                    }
                    int fh = tabAt.hash;
                    if (fh == -1) {
                        nodeArr = helpTransfer(nodeArr, tabAt);
                    } else {
                        synchronized (tabAt) {
                            if (tabAt(nodeArr, i) == tabAt) {
                                if (fh >= 0) {
                                    binCount = 1;
                                    Node<K, V> node = tabAt;
                                    Node<K, V> node2 = null;
                                    while (true) {
                                        if (node.hash != h || ((ek = node.key) != key && (ek == null || !key.equals(ek)))) {
                                            node2 = node;
                                            Node<K, V> node3 = node.next;
                                            node = node3;
                                            if (node3 == null) {
                                                break;
                                            }
                                            binCount++;
                                        }
                                    }
                                    val = biFunction.apply(key, (V) node.val);
                                    if (val != null) {
                                        node.val = val;
                                    } else {
                                        delta = -1;
                                        Node<K, V> node4 = node.next;
                                        if (node2 != null) {
                                            node2.next = node4;
                                        } else {
                                            setTabAt(nodeArr, i, node4);
                                        }
                                    }
                                } else if (tabAt instanceof TreeBin) {
                                    binCount = 2;
                                    TreeBin treeBin = (TreeBin) tabAt;
                                    TreeNode<K, V> treeNode = treeBin.root;
                                    if (treeNode != null && (findTreeNode = treeNode.findTreeNode(h, key, null)) != null) {
                                        val = biFunction.apply(key, (V) findTreeNode.val);
                                        if (val != null) {
                                            findTreeNode.val = val;
                                        } else {
                                            delta = -1;
                                            if (treeBin.removeTreeNode(findTreeNode)) {
                                                setTabAt(nodeArr, i, untreeify(treeBin.first));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (binCount != 0) {
                            break;
                        }
                    }
                }
            }
            nodeArr = initTable();
        }
        if (delta != 0) {
            addCount(delta, binCount);
        }
        return val;
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x00a3, code lost:
        r5 = r20.apply(r2, (V) r7.val);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00aa, code lost:
        if (r5 == null) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00ac, code lost:
        r7.val = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00af, code lost:
        r6 = -1;
        r12 = r7.next;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00b2, code lost:
        if (r0 == null) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00b4, code lost:
        r0.next = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00b7, code lost:
        setTabAt(r8, r0, r12);
     */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.function.BiFunction != java.util.function.BiFunction<? super K, ? super V, ? extends V> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:91:0x0126 -> B:92:0x0127). Please submit an issue!!! */
    @Override // j$.util.concurrent.ConcurrentMap, j$.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public V compute(K key, j$.util.function.BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Throwable th;
        TreeNode<K, V> treeNode;
        K ek;
        Throwable th2;
        Object obj = key;
        if (obj == null || biFunction == null) {
            throw new NullPointerException();
        }
        int h = spread(key.hashCode());
        Node<K, V>[] nodeArr = this.table;
        int binCount = 0;
        int delta = 0;
        V val = null;
        Object obj2 = obj;
        while (true) {
            if (nodeArr != null) {
                int n = nodeArr.length;
                if (n != 0) {
                    int i = (n - 1) & h;
                    Node<K, V> tabAt = tabAt(nodeArr, i);
                    V v = (Object) null;
                    if (tabAt == null) {
                        ReservationNode reservationNode = new ReservationNode();
                        synchronized (reservationNode) {
                            try {
                                if (casTabAt(nodeArr, i, null, reservationNode)) {
                                    binCount = 1;
                                    Node node = null;
                                    V apply = biFunction.apply(obj2, null);
                                    val = apply;
                                    if (apply != null) {
                                        delta = 1;
                                        node = new Node(h, obj2, val, null);
                                    }
                                    try {
                                        setTabAt(nodeArr, i, node);
                                    } catch (Throwable th3) {
                                        th2 = th3;
                                        while (true) {
                                            try {
                                                break;
                                            } catch (Throwable th4) {
                                                th2 = th4;
                                            }
                                        }
                                        throw th2;
                                    }
                                }
                                if (binCount != 0) {
                                    break;
                                }
                                obj2 = key;
                            } catch (Throwable th5) {
                                th2 = th5;
                            }
                        }
                    } else {
                        int fh = tabAt.hash;
                        if (fh == -1) {
                            nodeArr = helpTransfer(nodeArr, tabAt);
                        } else {
                            synchronized (tabAt) {
                                try {
                                    if (tabAt(nodeArr, i) == tabAt) {
                                        if (fh >= 0) {
                                            Node<K, V> node2 = tabAt;
                                            int binCount2 = 1;
                                            Node<K, V> node3 = null;
                                            while (true) {
                                                try {
                                                    if (node2.hash != h || ((ek = node2.key) != obj2 && (ek == null || !obj2.equals(ek)))) {
                                                        node3 = node2;
                                                        Node<K, V> node4 = node2.next;
                                                        node2 = node4;
                                                        if (node4 == null) {
                                                            val = biFunction.apply(obj2, null);
                                                            if (val != null) {
                                                                delta = 1;
                                                                node3.next = new Node<>(h, obj2, val, null);
                                                            }
                                                        } else {
                                                            binCount2++;
                                                        }
                                                    }
                                                } catch (Throwable th6) {
                                                    th = th6;
                                                    throw th;
                                                }
                                            }
                                            binCount = binCount2;
                                        } else if (tabAt instanceof TreeBin) {
                                            binCount = 1;
                                            TreeBin treeBin = (TreeBin) tabAt;
                                            TreeNode<K, V> treeNode2 = treeBin.root;
                                            if (treeNode2 != null) {
                                                treeNode = treeNode2.findTreeNode(h, obj2, null);
                                            } else {
                                                treeNode = null;
                                            }
                                            if (treeNode != null) {
                                                v = treeNode.val;
                                            }
                                            val = biFunction.apply(obj2, v);
                                            if (val != null) {
                                                if (treeNode != null) {
                                                    treeNode.val = val;
                                                } else {
                                                    delta = 1;
                                                    treeBin.putTreeVal(h, obj2, val);
                                                }
                                            } else if (treeNode != null) {
                                                delta = -1;
                                                if (treeBin.removeTreeNode(treeNode)) {
                                                    setTabAt(nodeArr, i, untreeify(treeBin.first));
                                                }
                                            }
                                        }
                                    }
                                    if (binCount != 0) {
                                        if (binCount >= 8) {
                                            treeifyBin(nodeArr, i);
                                        }
                                    }
                                } catch (Throwable th7) {
                                    th = th7;
                                }
                            }
                        }
                        obj2 = key;
                    }
                }
            }
            nodeArr = initTable();
            obj2 = key;
        }
        if (delta != 0) {
            addCount(delta, binCount);
        }
        return val;
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x00af, code lost:
        r6 = r22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00b7, code lost:
        r0.next = new j$.util.concurrent.ConcurrentHashMap.Node<>(r5, r2, r6, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00bc, code lost:
        r7 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00c1, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00c7, code lost:
        r0 = th;
     */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.function.BiFunction != java.util.function.BiFunction<? super V, ? super V, ? extends V> */
    /* JADX WARN: Removed duplicated region for block: B:134:0x012f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x014e A[SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:133:? -> B:104:0x013e). Please submit an issue!!! */
    @Override // j$.util.concurrent.ConcurrentMap, j$.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public V merge(K key, V value, j$.util.function.BiFunction<? super V, ? super V, ? extends V> biFunction) {
        V val;
        int delta;
        Throwable th;
        V val2;
        V val3;
        K k = key;
        if (k == null || value == null || biFunction == null) {
            throw new NullPointerException();
        }
        int h = spread(key.hashCode());
        Node<K, V>[] nodeArr = this.table;
        int binCount = 0;
        int delta2 = 0;
        V val4 = null;
        while (true) {
            if (nodeArr == null) {
                val = val4;
            } else {
                int n = nodeArr.length;
                if (n == 0) {
                    val = val4;
                } else {
                    int i = (n - 1) & h;
                    Node<K, V> tabAt = tabAt(nodeArr, i);
                    if (tabAt != null) {
                        int delta3 = tabAt.hash;
                        if (delta3 == -1) {
                            nodeArr = helpTransfer(nodeArr, tabAt);
                        } else {
                            synchronized (tabAt) {
                                try {
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                                try {
                                    if (tabAt(nodeArr, i) == tabAt) {
                                        if (delta3 >= 0) {
                                            Node<K, V> node = tabAt;
                                            int binCount2 = 1;
                                            Node<K, V> node2 = null;
                                            while (true) {
                                                try {
                                                    if (node.hash == h) {
                                                        K ek = node.key;
                                                        if (ek == k) {
                                                            break;
                                                        } else if (ek != null) {
                                                            try {
                                                                if (k.equals(ek)) {
                                                                    break;
                                                                }
                                                                val3 = val4;
                                                            } catch (Throwable th3) {
                                                                th = th3;
                                                                throw th;
                                                            }
                                                        } else {
                                                            val3 = val4;
                                                        }
                                                    } else {
                                                        val3 = val4;
                                                    }
                                                    node2 = node;
                                                    Node<K, V> node3 = node.next;
                                                    node = node3;
                                                    if (node3 == null) {
                                                        break;
                                                    }
                                                    binCount2++;
                                                    val4 = val3;
                                                } catch (Throwable th4) {
                                                    th = th4;
                                                }
                                            }
                                            try {
                                                V val5 = node.val;
                                                val4 = biFunction.apply(val5, value);
                                                if (val4 != null) {
                                                    node.val = val4;
                                                } else {
                                                    delta2 = -1;
                                                    try {
                                                        Node<K, V> node4 = node.next;
                                                        if (node2 != null) {
                                                            node2.next = node4;
                                                        } else {
                                                            setTabAt(nodeArr, i, node4);
                                                        }
                                                        val4 = val4;
                                                    } catch (Throwable th5) {
                                                        th = th5;
                                                        throw th;
                                                    }
                                                }
                                                binCount = binCount2;
                                            } catch (Throwable th6) {
                                                th = th6;
                                            }
                                        } else {
                                            val2 = val4;
                                            try {
                                                if (tabAt instanceof TreeBin) {
                                                    binCount = 2;
                                                    TreeBin treeBin = (TreeBin) tabAt;
                                                    TreeNode<K, V> treeNode = treeBin.root;
                                                    TreeNode<K, V> findTreeNode = treeNode == null ? null : treeNode.findTreeNode(h, k, null);
                                                    V val6 = findTreeNode == null ? value : biFunction.apply((V) findTreeNode.val, value);
                                                    if (val6 != null) {
                                                        if (findTreeNode != null) {
                                                            try {
                                                                findTreeNode.val = val6;
                                                            } catch (Throwable th7) {
                                                                th = th7;
                                                                throw th;
                                                            }
                                                        } else {
                                                            delta2 = 1;
                                                            treeBin.putTreeVal(h, k, val6);
                                                            val4 = val6;
                                                        }
                                                    } else if (findTreeNode != null) {
                                                        delta2 = -1;
                                                        if (treeBin.removeTreeNode(findTreeNode)) {
                                                            setTabAt(nodeArr, i, untreeify(treeBin.first));
                                                        }
                                                    }
                                                    val4 = val6;
                                                }
                                            } catch (Throwable th8) {
                                                th = th8;
                                            }
                                        }
                                        if (binCount == 0) {
                                            if (binCount >= 8) {
                                                treeifyBin(nodeArr, i);
                                            }
                                            delta = delta2;
                                        }
                                    } else {
                                        val2 = val4;
                                    }
                                    if (binCount == 0) {
                                    }
                                } catch (Throwable th9) {
                                    th = th9;
                                    throw th;
                                }
                                val4 = val2;
                            }
                        }
                        k = key;
                    } else if (casTabAt(nodeArr, i, null, new Node(h, k, value, null))) {
                        delta = 1;
                        val4 = value;
                        break;
                    } else {
                        k = key;
                    }
                }
            }
            nodeArr = initTable();
            val4 = val;
            k = key;
        }
        if (delta != 0) {
            addCount(delta, binCount);
        }
        return val4;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    public boolean contains(Object value) {
        return containsValue(value);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    public Enumeration<K> keys() {
        Node<K, V>[] nodeArr = this.table;
        int f = nodeArr == null ? 0 : nodeArr.length;
        return new KeyIterator(nodeArr, f, 0, f, this);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    public Enumeration<V> elements() {
        Node<K, V>[] nodeArr = this.table;
        int f = nodeArr == null ? 0 : nodeArr.length;
        return new ValueIterator(nodeArr, f, 0, f, this);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    public long mappingCount() {
        long n = sumCount();
        if (n < 0) {
            return 0L;
        }
        return n;
    }

    /* loaded from: classes2.dex */
    public static final class ForwardingNode<K, V> extends Node<K, V> {
        final Node<K, V>[] nextTable;

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ForwardingNode != java.util.concurrent.ConcurrentHashMap$ForwardingNode<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        ForwardingNode(Node<K, V>[] nodeArr) {
            super(-1, null, null, null);
            this.nextTable = nodeArr;
        }

        /* JADX WARN: Code restructure failed: missing block: B:20:0x002d, code lost:
            if ((r4 instanceof j$.util.concurrent.ConcurrentHashMap.ForwardingNode) == false) goto L32;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x002f, code lost:
            r0 = ((j$.util.concurrent.ConcurrentHashMap.ForwardingNode) r4).nextTable;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0039, code lost:
            return r4.find(r8, r9);
         */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ForwardingNode != java.util.concurrent.ConcurrentHashMap$ForwardingNode<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        @Override // j$.util.concurrent.ConcurrentHashMap.Node
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        Node<K, V> find(int h, Object k) {
            int n;
            K ek;
            Node<K, V>[] nodeArr = this.nextTable;
            loop0: while (k != null && nodeArr != null && (n = nodeArr.length) != 0) {
                Node<K, V> tabAt = ConcurrentHashMap.tabAt(nodeArr, (n - 1) & h);
                Node<K, V> node = tabAt;
                if (tabAt == null) {
                    break;
                }
                while (true) {
                    int eh = node.hash;
                    if (eh != h || ((ek = node.key) != k && (ek == null || !k.equals(ek)))) {
                        if (eh < 0) {
                            break;
                        }
                        Node<K, V> node2 = node.next;
                        node = node2;
                        if (node2 == null) {
                            return null;
                        }
                    }
                }
                return node;
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static final class ReservationNode<K, V> extends Node<K, V> {
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ReservationNode != java.util.concurrent.ConcurrentHashMap$ReservationNode<K, V> */
        ReservationNode() {
            super(-3, null, null, null);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ReservationNode != java.util.concurrent.ConcurrentHashMap$ReservationNode<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.Node
        Node<K, V> find(int h, Object k) {
            return null;
        }
    }

    static final int resizeStamp(int n) {
        return Integer.numberOfLeadingZeros(n) | (1 << (RESIZE_STAMP_BITS - 1));
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    private final Node<K, V>[] initTable() {
        Node<K, V>[] nodeArr;
        while (true) {
            Node<K, V>[] nodeArr2 = this.table;
            nodeArr = nodeArr2;
            if (nodeArr2 != null && nodeArr.length != 0) {
                break;
            }
            int i = this.sizeCtl;
            int sc = i;
            if (i < 0) {
                Thread.yield();
            } else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                try {
                    Node<K, V>[] nodeArr3 = this.table;
                    nodeArr = nodeArr3;
                    if (nodeArr3 == null || nodeArr.length == 0) {
                        int n = sc > 0 ? sc : 16;
                        Node<K, V>[] nodeArr4 = new Node[n];
                        nodeArr = nodeArr4;
                        this.table = nodeArr4;
                        sc = n - (n >>> 2);
                    }
                } finally {
                    this.sizeCtl = sc;
                }
            }
        }
        return nodeArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x001b, code lost:
        if (r0.compareAndSwapLong(r23, r2, r4, r6) == false) goto L6;
     */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void addCount(long x, int check) {
        long s;
        Node<K, V>[] nodeArr;
        int n;
        Node<K, V>[] nodeArr2;
        int m;
        CounterCell a;
        CounterCell[] as = this.counterCells;
        if (as == null) {
            Unsafe unsafe = U;
            long j = BASECOUNT;
            long b = this.baseCount;
            long j2 = b + x;
            s = j2;
        }
        boolean uncontended = true;
        if (as != null && (m = as.length - 1) >= 0 && (a = as[ThreadLocalRandom.getProbe() & m]) != null) {
            Unsafe unsafe2 = U;
            long j3 = CELLVALUE;
            long v = a.value;
            boolean compareAndSwapLong = unsafe2.compareAndSwapLong(a, j3, v, v + x);
            uncontended = compareAndSwapLong;
            if (compareAndSwapLong) {
                if (check <= 1) {
                    return;
                }
                s = sumCount();
                if (check < 0) {
                    return;
                }
                while (true) {
                    int sc = this.sizeCtl;
                    if (s >= sc && (nodeArr = this.table) != null && (n = nodeArr.length) < 1073741824) {
                        int rs = resizeStamp(n);
                        if (sc < 0) {
                            if ((sc >>> RESIZE_STAMP_SHIFT) == rs && sc != rs + 1 && sc != MAX_RESIZERS + rs && (nodeArr2 = this.nextTable) != null && this.transferIndex > 0) {
                                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
                                    transfer(nodeArr, nodeArr2);
                                }
                            } else {
                                return;
                            }
                        } else if (U.compareAndSwapInt(this, SIZECTL, sc, (rs << RESIZE_STAMP_SHIFT) + 2)) {
                            transfer(nodeArr, null);
                        }
                        s = sumCount();
                    } else {
                        return;
                    }
                }
            }
        }
        fullAddCount(x, uncontended);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    final Node<K, V>[] helpTransfer(Node<K, V>[] nodeArr, Node<K, V> node) {
        Node<K, V>[] nodeArr2;
        int sc;
        if (nodeArr != null && (node instanceof ForwardingNode) && (nodeArr2 = ((ForwardingNode) node).nextTable) != null) {
            int rs = resizeStamp(nodeArr.length);
            while (true) {
                if (nodeArr2 != this.nextTable || this.table != nodeArr || (sc = this.sizeCtl) >= 0 || (sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 || sc == MAX_RESIZERS + rs || this.transferIndex <= 0) {
                    break;
                } else if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
                    transfer(nodeArr, nodeArr2);
                    break;
                }
            }
            return nodeArr2;
        }
        return this.table;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    private final void tryPresize(int size) {
        int n;
        Node<K, V>[] nodeArr;
        int c = size >= 536870912 ? 1073741824 : tableSizeFor((size >>> 1) + size + 1);
        while (true) {
            int i = this.sizeCtl;
            int sc = i;
            if (i >= 0) {
                Node<K, V>[] nodeArr2 = this.table;
                if (nodeArr2 == null || (n = nodeArr2.length) == 0) {
                    int n2 = sc > c ? sc : c;
                    if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                        try {
                            if (this.table == nodeArr2) {
                                this.table = new Node[n2];
                                sc = n2 - (n2 >>> 2);
                            }
                        } finally {
                            this.sizeCtl = sc;
                        }
                    } else {
                        continue;
                    }
                } else if (c > sc && n < 1073741824) {
                    if (nodeArr2 == this.table) {
                        int rs = resizeStamp(n);
                        if (sc < 0) {
                            if ((sc >>> RESIZE_STAMP_SHIFT) == rs && sc != rs + 1 && sc != MAX_RESIZERS + rs && (nodeArr = this.nextTable) != null && this.transferIndex > 0) {
                                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
                                    transfer(nodeArr2, nodeArr);
                                }
                            } else {
                                return;
                            }
                        } else if (U.compareAndSwapInt(this, SIZECTL, sc, (rs << RESIZE_STAMP_SHIFT) + 2)) {
                            transfer(nodeArr2, null);
                        }
                    } else {
                        continue;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ForwardingNode != java.util.concurrent.ConcurrentHashMap$ForwardingNode<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:133:0x021f -> B:136:0x022b). Please submit an issue!!! */
    private final void transfer(Node<K, V>[] nodeArr, Node<K, V>[] nodeArr2) {
        Node<K, V>[] nodeArr3;
        int nextn;
        int stride;
        boolean finishing;
        boolean advance;
        Throwable th;
        boolean finishing2;
        boolean advance2;
        int h;
        boolean finishing3;
        Node<K, V> node;
        Node<K, V> node2;
        int nextn2;
        ConcurrentHashMap<K, V> concurrentHashMap = this;
        int n = nodeArr.length;
        int i = NCPU;
        int i2 = i > 1 ? (n >>> 3) / i : n;
        int stride2 = i2;
        int stride3 = i2 < 16 ? 16 : stride2;
        if (nodeArr2 == null) {
            try {
                Node<K, V>[] nodeArr4 = new Node[n << 1];
                concurrentHashMap.nextTable = nodeArr4;
                concurrentHashMap.transferIndex = n;
                nodeArr3 = nodeArr4;
            } catch (Throwable th2) {
                concurrentHashMap.sizeCtl = Integer.MAX_VALUE;
                return;
            }
        } else {
            nodeArr3 = nodeArr2;
        }
        int nextn3 = nodeArr3.length;
        ForwardingNode forwardingNode = new ForwardingNode(nodeArr3);
        boolean advance3 = true;
        boolean finishing4 = false;
        int i3 = 0;
        int bound = 0;
        while (true) {
            if (!advance3) {
                int bound2 = bound;
                if (i3 < 0 || i3 >= n) {
                    stride = stride3;
                    nextn = nextn3;
                    advance = advance3;
                    finishing = finishing4;
                } else if (i3 + n >= nextn3) {
                    stride = stride3;
                    nextn = nextn3;
                    advance = advance3;
                    finishing = finishing4;
                } else {
                    Node<K, V> tabAt = tabAt(nodeArr, i3);
                    if (tabAt == null) {
                        advance3 = casTabAt(nodeArr, i3, null, forwardingNode);
                        stride = stride3;
                        nextn = nextn3;
                    } else {
                        int i4 = tabAt.hash;
                        int fh = i4;
                        if (i4 == -1) {
                            advance3 = true;
                            stride = stride3;
                            nextn = nextn3;
                        } else {
                            synchronized (tabAt) {
                                try {
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                                try {
                                    if (tabAt(nodeArr, i3) != tabAt) {
                                        stride = stride3;
                                        nextn = nextn3;
                                        advance2 = advance3;
                                        finishing2 = finishing4;
                                    } else if (fh >= 0) {
                                        int runBit = fh & n;
                                        Node<K, V> node3 = tabAt;
                                        try {
                                            for (Node<K, V> node4 = tabAt.next; node4 != null; node4 = node4.next) {
                                                try {
                                                    int b = node4.hash & n;
                                                    if (b != runBit) {
                                                        runBit = b;
                                                        node3 = node4;
                                                    }
                                                } catch (Throwable th4) {
                                                    th = th4;
                                                    throw th;
                                                }
                                            }
                                            if (runBit == 0) {
                                                node2 = node3;
                                                node = null;
                                            } else {
                                                node = node3;
                                                node2 = null;
                                            }
                                            Node<K, V> node5 = tabAt;
                                            while (node5 != node3) {
                                                int fh2 = fh;
                                                try {
                                                    int fh3 = node5.hash;
                                                    Node<K, V> node6 = node3;
                                                    K pk = node5.key;
                                                    int stride4 = stride3;
                                                    try {
                                                        V pv = node5.val;
                                                        if ((fh3 & n) == 0) {
                                                            nextn2 = nextn3;
                                                            try {
                                                                node2 = new Node<>(fh3, pk, pv, node2);
                                                                continue;
                                                            } catch (Throwable th5) {
                                                                th = th5;
                                                                throw th;
                                                            }
                                                        } else {
                                                            nextn2 = nextn3;
                                                            node = new Node<>(fh3, pk, pv, node);
                                                            continue;
                                                        }
                                                        node5 = node5.next;
                                                        fh = fh2;
                                                        node3 = node6;
                                                        stride3 = stride4;
                                                        nextn3 = nextn2;
                                                    } catch (Throwable th6) {
                                                        th = th6;
                                                    }
                                                } catch (Throwable th7) {
                                                    th = th7;
                                                }
                                            }
                                            stride = stride3;
                                            nextn = nextn3;
                                            setTabAt(nodeArr3, i3, node2);
                                            setTabAt(nodeArr3, i3 + n, node);
                                            setTabAt(nodeArr, i3, forwardingNode);
                                            advance3 = true;
                                            finishing2 = finishing4;
                                        } catch (Throwable th8) {
                                            th = th8;
                                        }
                                    } else {
                                        stride = stride3;
                                        nextn = nextn3;
                                        try {
                                            if (tabAt instanceof TreeBin) {
                                                TreeBin treeBin = (TreeBin) tabAt;
                                                Node node7 = treeBin.first;
                                                int hc = 0;
                                                int lc = 0;
                                                TreeNode<K, V> treeNode = null;
                                                TreeNode<K, V> treeNode2 = null;
                                                TreeNode<K, V> treeNode3 = null;
                                                TreeNode<K, V> treeNode4 = null;
                                                while (node7 != null) {
                                                    boolean advance4 = advance3;
                                                    try {
                                                        h = node7.hash;
                                                        finishing3 = finishing4;
                                                    } catch (Throwable th9) {
                                                        th = th9;
                                                    }
                                                    try {
                                                        TreeNode<K, V> treeNode5 = new TreeNode<>(h, node7.key, node7.val, null, null);
                                                        if ((h & n) == 0) {
                                                            treeNode5.prev = treeNode3;
                                                            if (treeNode3 == null) {
                                                                treeNode4 = treeNode5;
                                                            } else {
                                                                treeNode3.next = treeNode5;
                                                            }
                                                            treeNode3 = treeNode5;
                                                            lc++;
                                                        } else {
                                                            treeNode5.prev = treeNode;
                                                            if (treeNode == null) {
                                                                treeNode2 = treeNode5;
                                                            } else {
                                                                treeNode.next = treeNode5;
                                                            }
                                                            treeNode = treeNode5;
                                                            hc++;
                                                        }
                                                        node7 = (Node<K, V>) node7.next;
                                                        advance3 = advance4;
                                                        finishing4 = finishing3;
                                                    } catch (Throwable th10) {
                                                        th = th10;
                                                        throw th;
                                                    }
                                                }
                                                finishing2 = finishing4;
                                                Node untreeify = lc <= 6 ? untreeify(treeNode4) : hc != 0 ? new TreeBin(treeNode4) : treeBin;
                                                Node untreeify2 = hc <= 6 ? untreeify(treeNode2) : lc != 0 ? new TreeBin(treeNode2) : treeBin;
                                                setTabAt(nodeArr3, i3, untreeify);
                                                setTabAt(nodeArr3, i3 + n, untreeify2);
                                                setTabAt(nodeArr, i3, forwardingNode);
                                                advance3 = true;
                                            } else {
                                                advance2 = advance3;
                                                finishing2 = finishing4;
                                            }
                                        } catch (Throwable th11) {
                                            th = th11;
                                        }
                                    }
                                } catch (Throwable th12) {
                                    th = th12;
                                    throw th;
                                }
                                advance3 = advance2;
                            }
                            concurrentHashMap = this;
                            finishing4 = finishing2;
                        }
                    }
                    bound = bound2;
                    stride3 = stride;
                    nextn3 = nextn;
                }
                if (finishing) {
                    this.nextTable = null;
                    this.table = nodeArr3;
                    this.sizeCtl = (n << 1) - (n >>> 1);
                    return;
                }
                concurrentHashMap = this;
                Unsafe unsafe = U;
                long j = SIZECTL;
                int sc = concurrentHashMap.sizeCtl;
                int i5 = i3;
                if (!unsafe.compareAndSwapInt(this, j, sc, sc - 1)) {
                    i3 = i5;
                    advance3 = advance;
                    finishing4 = finishing;
                } else if (sc - 2 != (resizeStamp(n) << RESIZE_STAMP_SHIFT)) {
                    return;
                } else {
                    advance3 = true;
                    finishing4 = true;
                    i3 = n;
                }
                bound = bound2;
                stride3 = stride;
                nextn3 = nextn;
            } else {
                int i6 = i3 - 1;
                if (i6 >= bound || finishing4) {
                    i3 = i6;
                    advance3 = false;
                    bound = bound;
                } else {
                    int nextIndex = concurrentHashMap.transferIndex;
                    if (nextIndex <= 0) {
                        i3 = -1;
                        advance3 = false;
                    } else {
                        Unsafe unsafe2 = U;
                        long j2 = TRANSFERINDEX;
                        int nextIndex2 = nextIndex > stride3 ? nextIndex - stride3 : 0;
                        int nextBound = nextIndex2;
                        int bound3 = bound;
                        if (unsafe2.compareAndSwapInt(this, j2, nextIndex, nextIndex2)) {
                            i3 = nextIndex - 1;
                            advance3 = false;
                            bound = nextBound;
                        } else {
                            i3 = i6;
                            bound = bound3;
                        }
                    }
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class CounterCell {
        volatile long value;

        CounterCell(long x) {
            this.value = x;
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    final long sumCount() {
        CounterCell[] as = this.counterCells;
        long sum = this.baseCount;
        if (as != null) {
            for (CounterCell a : as) {
                if (a != null) {
                    sum += a.value;
                }
            }
        }
        return sum;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    private final void fullAddCount(long x, boolean wasUncontended) {
        boolean wasUncontended2;
        int n;
        CounterCell a;
        int m;
        int probe = ThreadLocalRandom.getProbe();
        int h = probe;
        if (probe != 0) {
            wasUncontended2 = wasUncontended;
        } else {
            ThreadLocalRandom.localInit();
            h = ThreadLocalRandom.getProbe();
            wasUncontended2 = true;
        }
        boolean wasUncontended3 = wasUncontended2;
        int h2 = h;
        boolean collide = false;
        while (true) {
            CounterCell[] as = this.counterCells;
            if (as != null && (n = as.length) > 0) {
                CounterCell a2 = as[(n - 1) & h2];
                if (a2 == null) {
                    if (this.cellsBusy != 0) {
                        a = a2;
                    } else {
                        CounterCell r = new CounterCell(x);
                        if (this.cellsBusy != 0) {
                            a = a2;
                        } else {
                            a = a2;
                            if (U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                                boolean created = false;
                                try {
                                    CounterCell[] rs = this.counterCells;
                                    if (rs != null && (m = rs.length) > 0) {
                                        int j = (m - 1) & h2;
                                        if (rs[j] == null) {
                                            rs[j] = r;
                                            created = true;
                                        }
                                    }
                                    if (created) {
                                        return;
                                    }
                                } finally {
                                }
                            }
                        }
                    }
                    collide = false;
                    h2 = ThreadLocalRandom.advanceProbe(h2);
                } else {
                    if (!wasUncontended3) {
                        wasUncontended3 = true;
                    } else {
                        Unsafe unsafe = U;
                        long j2 = CELLVALUE;
                        long v = a2.value;
                        if (!unsafe.compareAndSwapLong(a2, j2, v, v + x)) {
                            if (this.counterCells == as && n < NCPU) {
                                if (!collide) {
                                    collide = true;
                                } else if (this.cellsBusy == 0 && unsafe.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                                    try {
                                        if (this.counterCells == as) {
                                            CounterCell[] rs2 = new CounterCell[n << 1];
                                            for (int i = 0; i < n; i++) {
                                                rs2[i] = as[i];
                                            }
                                            this.counterCells = rs2;
                                        }
                                        this.cellsBusy = 0;
                                        collide = false;
                                    } finally {
                                    }
                                }
                            }
                            collide = false;
                        } else {
                            return;
                        }
                    }
                    h2 = ThreadLocalRandom.advanceProbe(h2);
                }
            } else {
                int h3 = this.cellsBusy;
                if (h3 == 0 && this.counterCells == as && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                    boolean init = false;
                    try {
                        if (this.counterCells == as) {
                            CounterCell[] rs3 = new CounterCell[2];
                            rs3[h2 & 1] = new CounterCell(x);
                            this.counterCells = rs3;
                            init = true;
                        }
                        if (init) {
                            return;
                        }
                    } finally {
                    }
                } else {
                    Unsafe unsafe2 = U;
                    long j3 = BASECOUNT;
                    long v2 = this.baseCount;
                    if (unsafe2.compareAndSwapLong(this, j3, v2, v2 + x)) {
                        return;
                    }
                }
            }
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
    private final void treeifyBin(Node<K, V>[] nodeArr, int index) {
        if (nodeArr != null) {
            int n = nodeArr.length;
            if (n < 64) {
                tryPresize(n << 1);
                return;
            }
            Node<K, V> tabAt = tabAt(nodeArr, index);
            if (tabAt != null && tabAt.hash >= 0) {
                synchronized (tabAt) {
                    if (tabAt(nodeArr, index) == tabAt) {
                        TreeNode<K, V> treeNode = null;
                        TreeNode<K, V> treeNode2 = null;
                        for (Node<K, V> node = tabAt; node != null; node = node.next) {
                            TreeNode<K, V> treeNode3 = new TreeNode<>(node.hash, node.key, node.val, null, null);
                            treeNode3.prev = treeNode2;
                            if (treeNode2 == null) {
                                treeNode = treeNode3;
                            } else {
                                treeNode2.next = treeNode3;
                            }
                            treeNode2 = treeNode3;
                        }
                        setTabAt(nodeArr, index, new TreeBin(treeNode));
                    }
                }
            }
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
    static <K, V> Node<K, V> untreeify(Node<K, V> node) {
        Node<K, V> node2 = null;
        Node<K, V> node3 = null;
        for (Node<K, V> node4 = node; node4 != null; node4 = node4.next) {
            Node<K, V> node5 = new Node<>(node4.hash, node4.key, node4.val, null);
            if (node3 == null) {
                node2 = node5;
            } else {
                node3.next = node5;
            }
            node3 = node5;
        }
        return node2;
    }

    /* loaded from: classes2.dex */
    public static final class TreeNode<K, V> extends Node<K, V> {
        TreeNode<K, V> left;
        TreeNode<K, V> parent;
        TreeNode<K, V> prev;
        boolean red;
        TreeNode<K, V> right;

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        TreeNode(int hash, K key, V val, Node<K, V> node, TreeNode<K, V> treeNode) {
            super(hash, key, val, node);
            this.parent = treeNode;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.Node
        Node<K, V> find(int h, Object k) {
            return findTreeNode(h, k, null);
        }

        /* JADX WARN: Code restructure failed: missing block: B:20:0x002f, code lost:
            if (r3 != null) goto L21;
         */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        final TreeNode<K, V> findTreeNode(int h, Object k, Class<?> kc) {
            if (k != null) {
                TreeNode<K, V> treeNode = this;
                do {
                    TreeNode<K, V> treeNode2 = treeNode.left;
                    TreeNode<K, V> treeNode3 = treeNode.right;
                    int ph = treeNode.hash;
                    if (ph > h) {
                        treeNode = treeNode2;
                        continue;
                    } else if (ph < h) {
                        treeNode = treeNode3;
                        continue;
                    } else {
                        K pk = treeNode.key;
                        if (pk == k || (pk != null && k.equals(pk))) {
                            return treeNode;
                        }
                        if (treeNode2 == null) {
                            treeNode = treeNode3;
                            continue;
                        } else if (treeNode3 == null) {
                            treeNode = treeNode2;
                            continue;
                        } else {
                            if (kc == null) {
                                Class<?> comparableClassFor = ConcurrentHashMap.comparableClassFor(k);
                                kc = comparableClassFor;
                            }
                            int dir = ConcurrentHashMap.compareComparables(kc, k, pk);
                            if (dir != 0) {
                                treeNode = dir < 0 ? treeNode2 : treeNode3;
                                continue;
                            }
                            TreeNode<K, V> findTreeNode = treeNode3.findTreeNode(h, k, kc);
                            if (findTreeNode != null) {
                                return findTreeNode;
                            }
                            treeNode = treeNode2;
                            continue;
                        }
                    }
                } while (treeNode != null);
                return null;
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static final class TreeBin<K, V> extends Node<K, V> {
        static final /* synthetic */ boolean $assertionsDisabled = true;
        private static final long LOCKSTATE;
        static final int READER = 4;
        private static final Unsafe U;
        static final int WAITER = 2;
        static final int WRITER = 1;
        volatile TreeNode<K, V> first;
        volatile int lockState;
        TreeNode<K, V> root;
        volatile Thread waiter;

        static {
            try {
                Unsafe unsafe = DesugarUnsafe.getUnsafe();
                U = unsafe;
                LOCKSTATE = unsafe.objectFieldOffset(TreeBin.class.getDeclaredField("lockState"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        static int tieBreakOrder(Object a, Object b) {
            int d;
            if (a == null || b == null || (d = a.getClass().getName().compareTo(b.getClass().getName())) == 0) {
                int d2 = System.identityHashCode(a) <= System.identityHashCode(b) ? -1 : 1;
                return d2;
            }
            return d;
        }

        /* JADX WARN: Code restructure failed: missing block: B:15:0x0036, code lost:
            if (r9 != null) goto L16;
         */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        TreeBin(TreeNode<K, V> treeNode) {
            super(-2, null, null, null);
            int dir;
            TreeNode<K, V> treeNode2;
            TreeNode<K, V> treeNode3;
            this.first = treeNode;
            TreeNode<K, V> treeNode4 = null;
            TreeNode<K, V> treeNode5 = treeNode;
            while (treeNode5 != null) {
                TreeNode<K, V> treeNode6 = (TreeNode) treeNode5.next;
                treeNode5.right = null;
                treeNode5.left = null;
                if (treeNode4 == null) {
                    treeNode5.parent = null;
                    treeNode5.red = false;
                    treeNode4 = treeNode5;
                } else {
                    K k = treeNode5.key;
                    int h = treeNode5.hash;
                    Class<?> kc = null;
                    TreeNode<K, V> treeNode7 = treeNode4;
                    do {
                        K pk = treeNode7.key;
                        int ph = treeNode7.hash;
                        if (ph > h) {
                            dir = -1;
                        } else if (ph < h) {
                            dir = 1;
                        } else {
                            if (kc == null) {
                                Class<?> comparableClassFor = ConcurrentHashMap.comparableClassFor(k);
                                kc = comparableClassFor;
                            }
                            int dir2 = ConcurrentHashMap.compareComparables(kc, k, pk);
                            if (dir2 != 0) {
                                dir = dir2;
                            }
                            dir = tieBreakOrder(k, pk);
                        }
                        treeNode2 = treeNode7;
                        treeNode3 = dir <= 0 ? treeNode7.left : treeNode7.right;
                        treeNode7 = treeNode3;
                    } while (treeNode3 != null);
                    treeNode5.parent = treeNode2;
                    if (dir <= 0) {
                        treeNode2.left = treeNode5;
                    } else {
                        treeNode2.right = treeNode5;
                    }
                    treeNode4 = balanceInsertion(treeNode4, treeNode5);
                }
                treeNode5 = treeNode6;
            }
            this.root = treeNode4;
            if ($assertionsDisabled || checkInvariants(treeNode4)) {
                return;
            }
            throw new AssertionError();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
        private final void lockRoot() {
            if (!U.compareAndSwapInt(this, LOCKSTATE, 0, 1)) {
                contendedLock();
            }
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
        private final void unlockRoot() {
            this.lockState = 0;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
        private final void contendedLock() {
            boolean waiting = false;
            while (true) {
                int s = this.lockState;
                if ((s & (-3)) == 0) {
                    if (U.compareAndSwapInt(this, LOCKSTATE, s, 1)) {
                        break;
                    }
                } else if ((s & 2) == 0) {
                    if (U.compareAndSwapInt(this, LOCKSTATE, s, s | 2)) {
                        waiting = true;
                        this.waiter = Thread.currentThread();
                    }
                } else if (waiting) {
                    LockSupport.park(this);
                }
            }
            if (waiting) {
                this.waiter = null;
            }
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.Node
        final Node<K, V> find(int h, Object k) {
            K ek;
            Thread w;
            Thread w2;
            TreeNode<K, V> treeNode = null;
            if (k != null) {
                Node<K, V> node = this.first;
                while (node != null) {
                    int s = this.lockState;
                    if ((s & 3) != 0) {
                        if (node.hash == h && ((ek = node.key) == k || (ek != null && k.equals(ek)))) {
                            return node;
                        }
                        node = node.next;
                    } else {
                        Unsafe unsafe = U;
                        long j = LOCKSTATE;
                        if (unsafe.compareAndSwapInt(this, j, s, s + 4)) {
                            try {
                                TreeNode<K, V> treeNode2 = this.root;
                                if (treeNode2 != null) {
                                    treeNode = treeNode2.findTreeNode(h, k, null);
                                }
                                if (DesugarUnsafe.getAndAddInt(unsafe, this, j, -4) == 6 && (w2 = this.waiter) != null) {
                                    LockSupport.unpark(w2);
                                }
                                return treeNode;
                            } catch (Throwable th) {
                                if (DesugarUnsafe.getAndAddInt(U, this, LOCKSTATE, -4) == 6 && (w = this.waiter) != null) {
                                    LockSupport.unpark(w);
                                }
                                throw th;
                            }
                        }
                    }
                }
            }
            return null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:17:0x004a, code lost:
            if (r2 != null) goto L18;
         */
        /* JADX WARN: Code restructure failed: missing block: B:29:0x006e, code lost:
            return r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x00c0, code lost:
            if (j$.util.concurrent.ConcurrentHashMap.TreeBin.$assertionsDisabled != false) goto L57;
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x00c8, code lost:
            if (checkInvariants(r16.root) == false) goto L55;
         */
        /* JADX WARN: Code restructure failed: missing block: B:56:0x00d0, code lost:
            throw new java.lang.AssertionError();
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x00d1, code lost:
            return null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:71:?, code lost:
            return null;
         */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        final TreeNode<K, V> putTreeVal(int h, K k, V v) {
            boolean searched;
            int dir;
            Class<?> kc;
            TreeNode<K, V> treeNode;
            int i = h;
            Class<?> kc2 = null;
            boolean searched2 = false;
            TreeNode<K, V> treeNode2 = this.root;
            while (true) {
                if (treeNode2 == null) {
                    TreeNode<K, V> treeNode3 = new TreeNode<>(h, k, v, null, null);
                    this.root = treeNode3;
                    this.first = treeNode3;
                    break;
                }
                int ph = treeNode2.hash;
                if (ph > i) {
                    dir = -1;
                    searched = searched2;
                    kc = kc2;
                } else if (ph < i) {
                    dir = 1;
                    searched = searched2;
                    kc = kc2;
                } else {
                    K pk = treeNode2.key;
                    if (pk == k || (pk != null && k.equals(pk))) {
                        break;
                    }
                    if (kc2 == null) {
                        Class<?> comparableClassFor = ConcurrentHashMap.comparableClassFor(k);
                        kc2 = comparableClassFor;
                    }
                    int dir2 = ConcurrentHashMap.compareComparables(kc2, k, pk);
                    if (dir2 != 0) {
                        dir = dir2;
                        searched = searched2;
                        kc = kc2;
                    }
                    if (!searched2) {
                        searched2 = true;
                        TreeNode<K, V> treeNode4 = treeNode2.left;
                        if (treeNode4 != null) {
                            TreeNode<K, V> findTreeNode = treeNode4.findTreeNode(i, k, kc2);
                            treeNode = findTreeNode;
                            if (findTreeNode != null) {
                                break;
                            }
                        }
                        TreeNode<K, V> treeNode5 = treeNode2.right;
                        if (treeNode5 != null) {
                            TreeNode<K, V> findTreeNode2 = treeNode5.findTreeNode(i, k, kc2);
                            treeNode = findTreeNode2;
                            if (findTreeNode2 != null) {
                                break;
                            }
                        }
                    }
                    dir = tieBreakOrder(k, pk);
                    searched = searched2;
                    kc = kc2;
                }
                TreeNode<K, V> treeNode6 = treeNode2;
                TreeNode<K, V> treeNode7 = dir <= 0 ? treeNode2.left : treeNode2.right;
                treeNode2 = treeNode7;
                if (treeNode7 == null) {
                    TreeNode<K, V> treeNode8 = this.first;
                    TreeNode<K, V> treeNode9 = new TreeNode<>(h, k, v, treeNode8, treeNode6);
                    this.first = treeNode9;
                    if (treeNode8 != null) {
                        treeNode8.prev = treeNode9;
                    }
                    if (dir <= 0) {
                        treeNode6.left = treeNode9;
                    } else {
                        treeNode6.right = treeNode9;
                    }
                    if (!treeNode6.red) {
                        treeNode9.red = true;
                    } else {
                        lockRoot();
                        try {
                            this.root = balanceInsertion(this.root, treeNode9);
                        } finally {
                            unlockRoot();
                        }
                    }
                } else {
                    i = h;
                    kc2 = kc;
                    searched2 = searched;
                }
            }
            return treeNode2;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeBin != java.util.concurrent.ConcurrentHashMap$TreeBin<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        final boolean removeTreeNode(TreeNode<K, V> treeNode) {
            TreeNode<K, V> treeNode2;
            TreeNode<K, V> treeNode3;
            TreeNode<K, V> treeNode4;
            TreeNode<K, V> treeNode5 = (TreeNode) treeNode.next;
            TreeNode<K, V> treeNode6 = treeNode.prev;
            if (treeNode6 == null) {
                this.first = treeNode5;
            } else {
                treeNode6.next = treeNode5;
            }
            if (treeNode5 != null) {
                treeNode5.prev = treeNode6;
            }
            if (this.first == null) {
                this.root = null;
                return true;
            }
            TreeNode<K, V> treeNode7 = this.root;
            TreeNode<K, V> treeNode8 = treeNode7;
            if (treeNode7 == null || treeNode8.right == null || (treeNode2 = treeNode8.left) == null || treeNode2.left == null) {
                return true;
            }
            lockRoot();
            try {
                TreeNode<K, V> treeNode9 = treeNode.left;
                TreeNode<K, V> treeNode10 = treeNode.right;
                if (treeNode9 != null && treeNode10 != null) {
                    TreeNode<K, V> treeNode11 = treeNode10;
                    while (true) {
                        TreeNode<K, V> treeNode12 = treeNode11.left;
                        if (treeNode12 == null) {
                            break;
                        }
                        treeNode11 = treeNode12;
                    }
                    boolean c = treeNode11.red;
                    treeNode11.red = treeNode.red;
                    treeNode.red = c;
                    TreeNode<K, V> treeNode13 = treeNode11.right;
                    TreeNode<K, V> treeNode14 = treeNode.parent;
                    if (treeNode11 == treeNode10) {
                        treeNode.parent = treeNode11;
                        treeNode11.right = treeNode;
                    } else {
                        TreeNode<K, V> treeNode15 = treeNode11.parent;
                        treeNode.parent = treeNode15;
                        if (treeNode15 != null) {
                            if (treeNode11 == treeNode15.left) {
                                treeNode15.left = treeNode;
                            } else {
                                treeNode15.right = treeNode;
                            }
                        }
                        treeNode11.right = treeNode10;
                        if (treeNode10 != null) {
                            treeNode10.parent = treeNode11;
                        }
                    }
                    treeNode.left = null;
                    treeNode.right = treeNode13;
                    if (treeNode13 != null) {
                        treeNode13.parent = treeNode;
                    }
                    treeNode11.left = treeNode9;
                    if (treeNode9 != null) {
                        treeNode9.parent = treeNode11;
                    }
                    treeNode11.parent = treeNode14;
                    if (treeNode14 == null) {
                        treeNode8 = treeNode11;
                    } else if (treeNode == treeNode14.left) {
                        treeNode14.left = treeNode11;
                    } else {
                        treeNode14.right = treeNode11;
                    }
                    if (treeNode13 != null) {
                        treeNode3 = treeNode13;
                    } else {
                        treeNode3 = treeNode;
                    }
                } else if (treeNode9 != null) {
                    treeNode3 = treeNode9;
                } else if (treeNode10 != null) {
                    treeNode3 = treeNode10;
                } else {
                    treeNode3 = treeNode;
                }
                if (treeNode3 != treeNode) {
                    TreeNode<K, V> treeNode16 = treeNode.parent;
                    treeNode3.parent = treeNode16;
                    if (treeNode16 == null) {
                        treeNode8 = treeNode3;
                    } else if (treeNode == treeNode16.left) {
                        treeNode16.left = treeNode3;
                    } else {
                        treeNode16.right = treeNode3;
                    }
                    treeNode.parent = null;
                    treeNode.right = null;
                    treeNode.left = null;
                }
                this.root = treeNode.red ? treeNode8 : balanceDeletion(treeNode8, treeNode3);
                if (treeNode == treeNode3 && (treeNode4 = treeNode.parent) != null) {
                    if (treeNode == treeNode4.left) {
                        treeNode4.left = null;
                    } else if (treeNode == treeNode4.right) {
                        treeNode4.right = null;
                    }
                    treeNode.parent = null;
                }
                unlockRoot();
                if (!$assertionsDisabled && !checkInvariants(this.root)) {
                    throw new AssertionError();
                }
                return false;
            } catch (Throwable th) {
                unlockRoot();
                throw th;
            }
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            TreeNode<K, V> treeNode3;
            if (treeNode2 != null && (treeNode3 = treeNode2.right) != null) {
                TreeNode<K, V> treeNode4 = treeNode3.left;
                treeNode2.right = treeNode4;
                if (treeNode4 != null) {
                    treeNode4.parent = treeNode2;
                }
                TreeNode<K, V> treeNode5 = treeNode2.parent;
                treeNode3.parent = treeNode5;
                if (treeNode5 == null) {
                    treeNode = treeNode3;
                    treeNode3.red = false;
                } else if (treeNode5.left == treeNode2) {
                    treeNode5.left = treeNode3;
                } else {
                    treeNode5.right = treeNode3;
                }
                treeNode3.left = treeNode2;
                treeNode2.parent = treeNode3;
            }
            return treeNode;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            TreeNode<K, V> treeNode3;
            if (treeNode2 != null && (treeNode3 = treeNode2.left) != null) {
                TreeNode<K, V> treeNode4 = treeNode3.right;
                treeNode2.left = treeNode4;
                if (treeNode4 != null) {
                    treeNode4.parent = treeNode2;
                }
                TreeNode<K, V> treeNode5 = treeNode2.parent;
                treeNode3.parent = treeNode5;
                if (treeNode5 == null) {
                    treeNode = treeNode3;
                    treeNode3.red = false;
                } else if (treeNode5.right == treeNode2) {
                    treeNode5.right = treeNode3;
                } else {
                    treeNode5.left = treeNode3;
                }
                treeNode3.right = treeNode2;
                treeNode2.parent = treeNode3;
            }
            return treeNode;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            treeNode2.red = true;
            while (true) {
                TreeNode<K, V> treeNode3 = treeNode2.parent;
                TreeNode<K, V> treeNode4 = treeNode3;
                if (treeNode3 == null) {
                    treeNode2.red = false;
                    return treeNode2;
                } else if (!treeNode4.red) {
                    break;
                } else {
                    TreeNode<K, V> treeNode5 = treeNode4.parent;
                    TreeNode<K, V> treeNode6 = treeNode5;
                    if (treeNode5 == null) {
                        break;
                    }
                    TreeNode<K, V> treeNode7 = treeNode6.left;
                    TreeNode<K, V> treeNode8 = null;
                    if (treeNode4 == treeNode7) {
                        TreeNode<K, V> treeNode9 = treeNode6.right;
                        if (treeNode9 != null && treeNode9.red) {
                            treeNode9.red = false;
                            treeNode4.red = false;
                            treeNode6.red = true;
                            treeNode2 = treeNode6;
                        } else {
                            if (treeNode2 == treeNode4.right) {
                                treeNode2 = treeNode4;
                                treeNode = rotateLeft(treeNode, treeNode4);
                                TreeNode<K, V> treeNode10 = treeNode2.parent;
                                treeNode4 = treeNode10;
                                if (treeNode10 != null) {
                                    treeNode8 = treeNode4.parent;
                                }
                                treeNode6 = treeNode8;
                            }
                            if (treeNode4 != null) {
                                treeNode4.red = false;
                                if (treeNode6 != null) {
                                    treeNode6.red = true;
                                    treeNode = rotateRight(treeNode, treeNode6);
                                }
                            }
                        }
                    } else if (treeNode7 != null && treeNode7.red) {
                        treeNode7.red = false;
                        treeNode4.red = false;
                        treeNode6.red = true;
                        treeNode2 = treeNode6;
                    } else {
                        if (treeNode2 == treeNode4.left) {
                            treeNode2 = treeNode4;
                            treeNode = rotateRight(treeNode, treeNode4);
                            TreeNode<K, V> treeNode11 = treeNode2.parent;
                            treeNode4 = treeNode11;
                            if (treeNode11 != null) {
                                treeNode8 = treeNode4.parent;
                            }
                            treeNode6 = treeNode8;
                        }
                        if (treeNode4 != null) {
                            treeNode4.red = false;
                            if (treeNode6 != null) {
                                treeNode6.red = true;
                                treeNode = rotateLeft(treeNode, treeNode6);
                            }
                        }
                    }
                }
            }
            return treeNode;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            while (treeNode2 != null && treeNode2 != treeNode) {
                TreeNode<K, V> treeNode3 = treeNode2.parent;
                TreeNode<K, V> treeNode4 = treeNode3;
                if (treeNode3 == null) {
                    treeNode2.red = false;
                    return treeNode2;
                } else if (treeNode2.red) {
                    treeNode2.red = false;
                    return treeNode;
                } else {
                    TreeNode<K, V> treeNode5 = treeNode4.left;
                    TreeNode<K, V> treeNode6 = treeNode5;
                    TreeNode<K, V> treeNode7 = null;
                    if (treeNode5 == treeNode2) {
                        TreeNode<K, V> treeNode8 = treeNode4.right;
                        TreeNode<K, V> treeNode9 = treeNode8;
                        if (treeNode8 != null && treeNode9.red) {
                            treeNode9.red = false;
                            treeNode4.red = true;
                            treeNode = rotateLeft(treeNode, treeNode4);
                            TreeNode<K, V> treeNode10 = treeNode2.parent;
                            treeNode4 = treeNode10;
                            treeNode9 = treeNode10 == null ? null : treeNode4.right;
                        }
                        if (treeNode9 == null) {
                            treeNode2 = treeNode4;
                        } else {
                            TreeNode<K, V> treeNode11 = treeNode9.left;
                            TreeNode<K, V> treeNode12 = treeNode9.right;
                            if ((treeNode12 == null || !treeNode12.red) && (treeNode11 == null || !treeNode11.red)) {
                                treeNode9.red = true;
                                treeNode2 = treeNode4;
                            } else {
                                if (treeNode12 == null || !treeNode12.red) {
                                    if (treeNode11 != null) {
                                        treeNode11.red = false;
                                    }
                                    treeNode9.red = true;
                                    treeNode = rotateRight(treeNode, treeNode9);
                                    TreeNode<K, V> treeNode13 = treeNode2.parent;
                                    treeNode4 = treeNode13;
                                    if (treeNode13 != null) {
                                        treeNode7 = treeNode4.right;
                                    }
                                    treeNode9 = treeNode7;
                                }
                                if (treeNode9 != null) {
                                    treeNode9.red = treeNode4 == null ? false : treeNode4.red;
                                    TreeNode<K, V> treeNode14 = treeNode9.right;
                                    if (treeNode14 != null) {
                                        treeNode14.red = false;
                                    }
                                }
                                if (treeNode4 != null) {
                                    treeNode4.red = false;
                                    treeNode = rotateLeft(treeNode, treeNode4);
                                }
                                treeNode2 = treeNode;
                            }
                        }
                    } else {
                        if (treeNode6 != null && treeNode6.red) {
                            treeNode6.red = false;
                            treeNode4.red = true;
                            treeNode = rotateRight(treeNode, treeNode4);
                            TreeNode<K, V> treeNode15 = treeNode2.parent;
                            treeNode4 = treeNode15;
                            treeNode6 = treeNode15 == null ? null : treeNode4.left;
                        }
                        if (treeNode6 == null) {
                            treeNode2 = treeNode4;
                        } else {
                            TreeNode<K, V> treeNode16 = treeNode6.left;
                            TreeNode<K, V> treeNode17 = treeNode6.right;
                            if ((treeNode16 == null || !treeNode16.red) && (treeNode17 == null || !treeNode17.red)) {
                                treeNode6.red = true;
                                treeNode2 = treeNode4;
                            } else {
                                if (treeNode16 == null || !treeNode16.red) {
                                    if (treeNode17 != null) {
                                        treeNode17.red = false;
                                    }
                                    treeNode6.red = true;
                                    treeNode = rotateLeft(treeNode, treeNode6);
                                    TreeNode<K, V> treeNode18 = treeNode2.parent;
                                    treeNode4 = treeNode18;
                                    if (treeNode18 != null) {
                                        treeNode7 = treeNode4.left;
                                    }
                                    treeNode6 = treeNode7;
                                }
                                if (treeNode6 != null) {
                                    treeNode6.red = treeNode4 == null ? false : treeNode4.red;
                                    TreeNode<K, V> treeNode19 = treeNode6.left;
                                    if (treeNode19 != null) {
                                        treeNode19.red = false;
                                    }
                                }
                                if (treeNode4 != null) {
                                    treeNode4.red = false;
                                    treeNode = rotateRight(treeNode, treeNode4);
                                }
                                treeNode2 = treeNode;
                            }
                        }
                    }
                }
            }
            return treeNode;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TreeNode != java.util.concurrent.ConcurrentHashMap$TreeNode<K, V> */
        static <K, V> boolean checkInvariants(TreeNode<K, V> treeNode) {
            TreeNode<K, V> treeNode2 = treeNode.parent;
            TreeNode<K, V> treeNode3 = treeNode.left;
            TreeNode<K, V> treeNode4 = treeNode.right;
            TreeNode<K, V> treeNode5 = treeNode.prev;
            TreeNode treeNode6 = (TreeNode) treeNode.next;
            if (treeNode5 != null && treeNode5.next != treeNode) {
                return false;
            }
            if (treeNode6 != null && treeNode6.prev != treeNode) {
                return false;
            }
            if (treeNode2 != null && treeNode != treeNode2.left && treeNode != treeNode2.right) {
                return false;
            }
            if (treeNode3 != null && (treeNode3.parent != treeNode || treeNode3.hash > treeNode.hash)) {
                return false;
            }
            if (treeNode4 != null && (treeNode4.parent != treeNode || treeNode4.hash < treeNode.hash)) {
                return false;
            }
            if (treeNode.red && treeNode3 != null && treeNode3.red && treeNode4 != null && treeNode4.red) {
                return false;
            }
            if (treeNode3 != null && !checkInvariants(treeNode3)) {
                return false;
            }
            if (treeNode4 != null && !checkInvariants(treeNode4)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public static final class TableStack<K, V> {
        int index;
        int length;
        TableStack<K, V> next;
        Node<K, V>[] tab;

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TableStack != java.util.concurrent.ConcurrentHashMap$TableStack<K, V> */
        TableStack() {
        }
    }

    /* loaded from: classes2.dex */
    public static class Traverser<K, V> {
        int baseIndex;
        int baseLimit;
        final int baseSize;
        int index;
        Node<K, V> next = null;
        TableStack<K, V> spare;
        TableStack<K, V> stack;
        Node<K, V>[] tab;

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
        Traverser(Node<K, V>[] nodeArr, int size, int index, int limit) {
            this.tab = nodeArr;
            this.baseSize = size;
            this.index = index;
            this.baseIndex = index;
            this.baseLimit = limit;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
        final Node<K, V> advance() {
            Node<K, V>[] nodeArr;
            int n;
            int i;
            Node<K, V> node = this.next;
            TreeNode<K, V> treeNode = node;
            if (node != null) {
                treeNode = treeNode.next;
            }
            while (treeNode == null) {
                if (this.baseIndex >= this.baseLimit || (nodeArr = this.tab) == null || (n = nodeArr.length) <= (i = this.index) || i < 0) {
                    this.next = null;
                    return null;
                }
                Node<K, V> tabAt = ConcurrentHashMap.tabAt(nodeArr, i);
                treeNode = tabAt;
                if (tabAt != null && treeNode.hash < 0) {
                    if (treeNode instanceof ForwardingNode) {
                        this.tab = ((ForwardingNode) treeNode).nextTable;
                        treeNode = null;
                        pushState(nodeArr, i, n);
                    } else if (treeNode instanceof TreeBin) {
                        treeNode = ((TreeBin) treeNode).first;
                    } else {
                        treeNode = null;
                    }
                }
                if (this.stack != null) {
                    recoverState(n);
                } else {
                    int i2 = this.baseSize + i;
                    this.index = i2;
                    if (i2 >= n) {
                        int i3 = this.baseIndex + 1;
                        this.baseIndex = i3;
                        this.index = i3;
                    }
                }
            }
            this.next = treeNode;
            return treeNode;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TableStack != java.util.concurrent.ConcurrentHashMap$TableStack<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
        private void pushState(Node<K, V>[] nodeArr, int i, int n) {
            TableStack<K, V> tableStack = this.spare;
            if (tableStack != null) {
                this.spare = tableStack.next;
            } else {
                tableStack = new TableStack<>();
            }
            tableStack.tab = nodeArr;
            tableStack.length = n;
            tableStack.index = i;
            tableStack.next = this.stack;
            this.stack = tableStack;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$TableStack != java.util.concurrent.ConcurrentHashMap$TableStack<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
        private void recoverState(int n) {
            TableStack<K, V> tableStack;
            while (true) {
                tableStack = this.stack;
                if (tableStack == null) {
                    break;
                }
                int i = this.index;
                int len = tableStack.length;
                int i2 = i + len;
                this.index = i2;
                if (i2 < n) {
                    break;
                }
                n = len;
                this.index = tableStack.index;
                this.tab = tableStack.tab;
                tableStack.tab = null;
                TableStack<K, V> tableStack2 = tableStack.next;
                tableStack.next = this.spare;
                this.stack = tableStack2;
                this.spare = tableStack;
            }
            if (tableStack == null) {
                int i3 = this.index + this.baseSize;
                this.index = i3;
                if (i3 >= n) {
                    int i4 = this.baseIndex + 1;
                    this.baseIndex = i4;
                    this.index = i4;
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class BaseIterator<K, V> extends Traverser<K, V> {
        Node<K, V> lastReturned;
        final ConcurrentHashMap<K, V> map;

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$BaseIterator != java.util.concurrent.ConcurrentHashMap$BaseIterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        BaseIterator(Node<K, V>[] nodeArr, int size, int index, int limit, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, size, index, limit);
            this.map = concurrentHashMap;
            advance();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$BaseIterator != java.util.concurrent.ConcurrentHashMap$BaseIterator<K, V> */
        public final boolean hasNext() {
            return this.next != null;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$BaseIterator != java.util.concurrent.ConcurrentHashMap$BaseIterator<K, V> */
        public final boolean hasMoreElements() {
            return this.next != null;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$BaseIterator != java.util.concurrent.ConcurrentHashMap$BaseIterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        public final void remove() {
            Node<K, V> node = this.lastReturned;
            if (node == null) {
                throw new IllegalStateException();
            }
            this.lastReturned = null;
            this.map.replaceNode(node.key, null, null);
        }
    }

    /* loaded from: classes2.dex */
    public static final class KeyIterator<K, V> extends BaseIterator<K, V> implements Iterator<K>, Enumeration<K>, j$.util.Iterator<K> {
        @Override // j$.util.Iterator
        public /* synthetic */ void forEachRemaining(Consumer consumer) {
            Iterator.CC.$default$forEachRemaining(this, consumer);
        }

        @Override // java.util.Iterator
        public /* synthetic */ void forEachRemaining(java.util.function.Consumer consumer) {
            forEachRemaining(C$r8$wrapper$java$util$function$Consumer$VWRP.convert(consumer));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeyIterator != java.util.concurrent.ConcurrentHashMap$KeyIterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        KeyIterator(Node<K, V>[] nodeArr, int index, int size, int limit, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, index, size, limit, concurrentHashMap);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeyIterator != java.util.concurrent.ConcurrentHashMap$KeyIterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        @Override // java.util.Iterator, j$.util.Iterator
        public final K next() {
            Node<K, V> node = this.next;
            if (node == null) {
                throw new NoSuchElementException();
            }
            K k = node.key;
            this.lastReturned = node;
            advance();
            return k;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeyIterator != java.util.concurrent.ConcurrentHashMap$KeyIterator<K, V> */
        @Override // java.util.Enumeration
        public final K nextElement() {
            return next();
        }
    }

    /* loaded from: classes2.dex */
    public static final class ValueIterator<K, V> extends BaseIterator<K, V> implements java.util.Iterator<V>, Enumeration<V>, j$.util.Iterator<V> {
        @Override // j$.util.Iterator
        public /* synthetic */ void forEachRemaining(Consumer consumer) {
            Iterator.CC.$default$forEachRemaining(this, consumer);
        }

        @Override // java.util.Iterator
        public /* synthetic */ void forEachRemaining(java.util.function.Consumer consumer) {
            forEachRemaining(C$r8$wrapper$java$util$function$Consumer$VWRP.convert(consumer));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValueIterator != java.util.concurrent.ConcurrentHashMap$ValueIterator<K, V> */
        ValueIterator(Node<K, V>[] nodeArr, int index, int size, int limit, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, index, size, limit, concurrentHashMap);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValueIterator != java.util.concurrent.ConcurrentHashMap$ValueIterator<K, V> */
        @Override // java.util.Iterator, j$.util.Iterator
        public final V next() {
            Node<K, V> node = this.next;
            if (node == null) {
                throw new NoSuchElementException();
            }
            V v = node.val;
            this.lastReturned = node;
            advance();
            return v;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValueIterator != java.util.concurrent.ConcurrentHashMap$ValueIterator<K, V> */
        @Override // java.util.Enumeration
        public final V nextElement() {
            return next();
        }
    }

    /* loaded from: classes2.dex */
    static final class EntryIterator<K, V> extends BaseIterator<K, V> implements java.util.Iterator<Map.Entry<K, V>>, j$.util.Iterator<Map.Entry<K, V>> {
        @Override // j$.util.Iterator
        public /* synthetic */ void forEachRemaining(Consumer consumer) {
            Iterator.CC.$default$forEachRemaining(this, consumer);
        }

        @Override // java.util.Iterator
        public /* synthetic */ void forEachRemaining(java.util.function.Consumer consumer) {
            forEachRemaining(C$r8$wrapper$java$util$function$Consumer$VWRP.convert(consumer));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntryIterator != java.util.concurrent.ConcurrentHashMap$EntryIterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        EntryIterator(Node<K, V>[] nodeArr, int index, int size, int limit, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, index, size, limit, concurrentHashMap);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntryIterator != java.util.concurrent.ConcurrentHashMap$EntryIterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        @Override // java.util.Iterator, j$.util.Iterator
        public final Map.Entry<K, V> next() {
            Node<K, V> node = this.next;
            if (node == null) {
                throw new NoSuchElementException();
            }
            K k = node.key;
            V v = node.val;
            this.lastReturned = node;
            advance();
            return new MapEntry(k, v, this.map);
        }
    }

    /* loaded from: classes2.dex */
    public static final class MapEntry<K, V> implements Map.Entry<K, V> {
        final K key;
        final ConcurrentHashMap<K, V> map;
        V val;

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$MapEntry != java.util.concurrent.ConcurrentHashMap$MapEntry<K, V> */
        MapEntry(K key, V val, ConcurrentHashMap<K, V> concurrentHashMap) {
            this.key = key;
            this.val = val;
            this.map = concurrentHashMap;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$MapEntry != java.util.concurrent.ConcurrentHashMap$MapEntry<K, V> */
        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$MapEntry != java.util.concurrent.ConcurrentHashMap$MapEntry<K, V> */
        @Override // java.util.Map.Entry
        public V getValue() {
            return this.val;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$MapEntry != java.util.concurrent.ConcurrentHashMap$MapEntry<K, V> */
        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$MapEntry != java.util.concurrent.ConcurrentHashMap$MapEntry<K, V> */
        public String toString() {
            return this.key + "=" + this.val;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$MapEntry != java.util.concurrent.ConcurrentHashMap$MapEntry<K, V> */
        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            Map.Entry<?, ?> e;
            Object k;
            Object v;
            K k2;
            V v2;
            return (o instanceof Map.Entry) && (k = (e = (Map.Entry) o).getKey()) != null && (v = e.getValue()) != null && (k == (k2 = this.key) || k.equals(k2)) && (v == (v2 = this.val) || v.equals(v2));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$MapEntry != java.util.concurrent.ConcurrentHashMap$MapEntry<K, V> */
        @Override // java.util.Map.Entry
        public V setValue(V value) {
            if (value == null) {
                throw new NullPointerException();
            }
            V v = this.val;
            this.val = value;
            this.map.put(this.key, value);
            return v;
        }
    }

    /* loaded from: classes2.dex */
    public static final class KeySpliterator<K, V> extends Traverser<K, V> implements Spliterator<K> {
        long est;

        @Override // j$.util.Spliterator
        public /* synthetic */ Comparator getComparator() {
            return Spliterator.CC.$default$getComparator(this);
        }

        @Override // j$.util.Spliterator
        public /* synthetic */ long getExactSizeIfKnown() {
            return Spliterator.CC.$default$getExactSizeIfKnown(this);
        }

        @Override // j$.util.Spliterator
        public /* synthetic */ boolean hasCharacteristics(int i) {
            return Spliterator.CC.$default$hasCharacteristics(this, i);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySpliterator != java.util.concurrent.ConcurrentHashMap$KeySpliterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        KeySpliterator(Node<K, V>[] nodeArr, int size, int index, int limit, long est) {
            super(nodeArr, size, index, limit);
            this.est = est;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySpliterator != java.util.concurrent.ConcurrentHashMap$KeySpliterator<K, V> */
        @Override // j$.util.Spliterator
        public Spliterator<K> trySplit() {
            int i = this.baseIndex;
            int f = this.baseLimit;
            int h = (i + f) >>> 1;
            if (h <= i) {
                return null;
            }
            Node<K, V>[] nodeArr = this.tab;
            int i2 = this.baseSize;
            this.baseLimit = h;
            long j = this.est >>> 1;
            this.est = j;
            return new KeySpliterator(nodeArr, i2, h, f, j);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySpliterator != java.util.concurrent.ConcurrentHashMap$KeySpliterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.function.Consumer != java.util.function.Consumer<? super K> */
        @Override // j$.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            if (consumer != null) {
                while (true) {
                    Node<K, V> advance = advance();
                    if (advance != null) {
                        consumer.accept((K) advance.key);
                    } else {
                        return;
                    }
                }
            } else {
                throw new NullPointerException();
            }
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySpliterator != java.util.concurrent.ConcurrentHashMap$KeySpliterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.function.Consumer != java.util.function.Consumer<? super K> */
        @Override // j$.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> advance = advance();
            if (advance == null) {
                return false;
            }
            consumer.accept((K) advance.key);
            return true;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySpliterator != java.util.concurrent.ConcurrentHashMap$KeySpliterator<K, V> */
        @Override // j$.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySpliterator != java.util.concurrent.ConcurrentHashMap$KeySpliterator<K, V> */
        @Override // j$.util.Spliterator
        public int characteristics() {
            return 4353;
        }
    }

    /* loaded from: classes2.dex */
    public static final class ValueSpliterator<K, V> extends Traverser<K, V> implements Spliterator<V> {
        long est;

        @Override // j$.util.Spliterator
        public /* synthetic */ Comparator getComparator() {
            return Spliterator.CC.$default$getComparator(this);
        }

        @Override // j$.util.Spliterator
        public /* synthetic */ long getExactSizeIfKnown() {
            return Spliterator.CC.$default$getExactSizeIfKnown(this);
        }

        @Override // j$.util.Spliterator
        public /* synthetic */ boolean hasCharacteristics(int i) {
            return Spliterator.CC.$default$hasCharacteristics(this, i);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValueSpliterator != java.util.concurrent.ConcurrentHashMap$ValueSpliterator<K, V> */
        ValueSpliterator(Node<K, V>[] nodeArr, int size, int index, int limit, long est) {
            super(nodeArr, size, index, limit);
            this.est = est;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValueSpliterator != java.util.concurrent.ConcurrentHashMap$ValueSpliterator<K, V> */
        @Override // j$.util.Spliterator
        public Spliterator<V> trySplit() {
            int i = this.baseIndex;
            int f = this.baseLimit;
            int h = (i + f) >>> 1;
            if (h <= i) {
                return null;
            }
            Node<K, V>[] nodeArr = this.tab;
            int i2 = this.baseSize;
            this.baseLimit = h;
            long j = this.est >>> 1;
            this.est = j;
            return new ValueSpliterator(nodeArr, i2, h, f, j);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValueSpliterator != java.util.concurrent.ConcurrentHashMap$ValueSpliterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.function.Consumer != java.util.function.Consumer<? super V> */
        @Override // j$.util.Spliterator
        public void forEachRemaining(Consumer<? super V> consumer) {
            if (consumer != null) {
                while (true) {
                    Node<K, V> advance = advance();
                    if (advance != null) {
                        consumer.accept((V) advance.val);
                    } else {
                        return;
                    }
                }
            } else {
                throw new NullPointerException();
            }
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValueSpliterator != java.util.concurrent.ConcurrentHashMap$ValueSpliterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.function.Consumer != java.util.function.Consumer<? super V> */
        @Override // j$.util.Spliterator
        public boolean tryAdvance(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> advance = advance();
            if (advance == null) {
                return false;
            }
            consumer.accept((V) advance.val);
            return true;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValueSpliterator != java.util.concurrent.ConcurrentHashMap$ValueSpliterator<K, V> */
        @Override // j$.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValueSpliterator != java.util.concurrent.ConcurrentHashMap$ValueSpliterator<K, V> */
        @Override // j$.util.Spliterator
        public int characteristics() {
            return 4352;
        }
    }

    /* loaded from: classes2.dex */
    public static final class EntrySpliterator<K, V> extends Traverser<K, V> implements Spliterator<Map.Entry<K, V>> {
        long est;
        final ConcurrentHashMap<K, V> map;

        @Override // j$.util.Spliterator
        public /* synthetic */ Comparator getComparator() {
            return Spliterator.CC.$default$getComparator(this);
        }

        @Override // j$.util.Spliterator
        public /* synthetic */ long getExactSizeIfKnown() {
            return Spliterator.CC.$default$getExactSizeIfKnown(this);
        }

        @Override // j$.util.Spliterator
        public /* synthetic */ boolean hasCharacteristics(int i) {
            return Spliterator.CC.$default$hasCharacteristics(this, i);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySpliterator != java.util.concurrent.ConcurrentHashMap$EntrySpliterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        EntrySpliterator(Node<K, V>[] nodeArr, int size, int index, int limit, long est, ConcurrentHashMap<K, V> concurrentHashMap) {
            super(nodeArr, size, index, limit);
            this.map = concurrentHashMap;
            this.est = est;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySpliterator != java.util.concurrent.ConcurrentHashMap$EntrySpliterator<K, V> */
        @Override // j$.util.Spliterator
        public Spliterator<Map.Entry<K, V>> trySplit() {
            int i = this.baseIndex;
            int f = this.baseLimit;
            int h = (i + f) >>> 1;
            if (h <= i) {
                return null;
            }
            Node<K, V>[] nodeArr = this.tab;
            int i2 = this.baseSize;
            this.baseLimit = h;
            long j = this.est >>> 1;
            this.est = j;
            return new EntrySpliterator(nodeArr, i2, h, f, j, this.map);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySpliterator != java.util.concurrent.ConcurrentHashMap$EntrySpliterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.function.Consumer != java.util.function.Consumer<? super java.util.Map$Entry<K, V>> */
        @Override // j$.util.Spliterator
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer != null) {
                while (true) {
                    Node<K, V> advance = advance();
                    if (advance != null) {
                        consumer.accept(new MapEntry(advance.key, advance.val, this.map));
                    } else {
                        return;
                    }
                }
            } else {
                throw new NullPointerException();
            }
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySpliterator != java.util.concurrent.ConcurrentHashMap$EntrySpliterator<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.function.Consumer != java.util.function.Consumer<? super java.util.Map$Entry<K, V>> */
        @Override // j$.util.Spliterator
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> advance = advance();
            if (advance == null) {
                return false;
            }
            consumer.accept(new MapEntry(advance.key, advance.val, this.map));
            return true;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySpliterator != java.util.concurrent.ConcurrentHashMap$EntrySpliterator<K, V> */
        @Override // j$.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySpliterator != java.util.concurrent.ConcurrentHashMap$EntrySpliterator<K, V> */
        @Override // j$.util.Spliterator
        public int characteristics() {
            return 4353;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class CollectionView<K, V, E> implements Collection<E>, Serializable {
        private static final String oomeMsg = "Required array size too large";
        private static final long serialVersionUID = 7249069246763182397L;
        final ConcurrentHashMap<K, V> map;

        @Override // java.util.Collection
        public abstract boolean contains(Object obj);

        @Override // java.util.Collection, java.lang.Iterable
        public abstract java.util.Iterator<E> iterator();

        @Override // java.util.Collection
        public abstract boolean remove(Object obj);

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        CollectionView(ConcurrentHashMap<K, V> concurrentHashMap) {
            this.map = concurrentHashMap;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        public ConcurrentHashMap<K, V> getMap() {
            return this.map;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        @Override // java.util.Collection
        public final void clear() {
            this.map.clear();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        @Override // java.util.Collection
        public final int size() {
            return this.map.size();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        @Override // java.util.Collection
        public final boolean isEmpty() {
            return this.map.isEmpty();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        @Override // java.util.Collection
        public final Object[] toArray() {
            long sz = this.map.mappingCount();
            if (sz > 2147483639) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int n = (int) sz;
            Object[] r = new Object[n];
            int i = 0;
            java.util.Iterator<E> it = iterator();
            while (it.hasNext()) {
                E e = it.next();
                if (i == n) {
                    if (n >= ConcurrentHashMap.MAX_ARRAY_SIZE) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    if (n >= 1073741819) {
                        n = ConcurrentHashMap.MAX_ARRAY_SIZE;
                    } else {
                        n += (n >>> 1) + 1;
                    }
                    r = Arrays.copyOf(r, n);
                }
                r[i] = e;
                i++;
            }
            return i == n ? r : Arrays.copyOf(r, i);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection
        public final <T> T[] toArray(T[] a) {
            long sz = this.map.mappingCount();
            if (sz > 2147483639) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int m = (int) sz;
            T[] r = a.length >= m ? a : (T[]) ((Object[]) Array.newInstance(a.getClass().getComponentType(), m));
            int n = r.length;
            int i = 0;
            java.util.Iterator<E> it = iterator();
            while (it.hasNext()) {
                E e = it.next();
                if (i == n) {
                    if (n >= ConcurrentHashMap.MAX_ARRAY_SIZE) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    if (n >= 1073741819) {
                        n = ConcurrentHashMap.MAX_ARRAY_SIZE;
                    } else {
                        n += (n >>> 1) + 1;
                    }
                    r = (T[]) Arrays.copyOf(r, n);
                }
                r[i] = e;
                i++;
            }
            if (a != r || i >= n) {
                return i == n ? r : (T[]) Arrays.copyOf(r, i);
            }
            r[i] = null;
            return r;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        public final String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            java.util.Iterator<E> it = iterator();
            if (it.hasNext()) {
                while (true) {
                    Object e = it.next();
                    sb.append(e == this ? "(this Collection)" : e);
                    if (!it.hasNext()) {
                        break;
                    }
                    sb.append(',');
                    sb.append(' ');
                }
            }
            sb.append(']');
            return sb.toString();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        /* JADX WARN: Removed duplicated region for block: B:6:0x000c  */
        @Override // java.util.Collection
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final boolean containsAll(Collection<?> c) {
            if (c != this) {
                for (Object e : c) {
                    if (e == null || !contains(e)) {
                        return false;
                    }
                    while (r0.hasNext()) {
                    }
                }
                return true;
            }
            return true;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        @Override // java.util.Collection
        public final boolean removeAll(Collection<?> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            boolean modified = false;
            java.util.Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (c.contains(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$CollectionView != java.util.concurrent.ConcurrentHashMap$CollectionView<K, V, E> */
        @Override // java.util.Collection
        public final boolean retainAll(Collection<?> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            boolean modified = false;
            java.util.Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (!c.contains(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }
    }

    /* loaded from: classes2.dex */
    public static class KeySetView<K, V> extends CollectionView<K, V, K> implements Set<K>, Serializable, j$.util.Set<K> {
        private static final long serialVersionUID = 7249069246763182397L;
        private final V value;

        @Override // java.lang.Iterable
        public /* synthetic */ void forEach(java.util.function.Consumer consumer) {
            forEach(C$r8$wrapper$java$util$function$Consumer$VWRP.convert(consumer));
        }

        @Override // j$.util.Collection
        public /* synthetic */ boolean removeIf(Predicate predicate) {
            return Collection.CC.$default$removeIf(this, predicate);
        }

        @Override // java.util.Collection
        public /* synthetic */ boolean removeIf(java.util.function.Predicate predicate) {
            return removeIf(C$r8$wrapper$java$util$function$Predicate$VWRP.convert(predicate));
        }

        @Override // j$.util.Collection
        public /* synthetic */ Object[] toArray(IntFunction intFunction) {
            return Collection.CC.$default$toArray(this, intFunction);
        }

        public /* synthetic */ Object[] toArray(java.util.function.IntFunction intFunction) {
            return toArray(C$r8$wrapper$java$util$function$IntFunction$VWRP.convert(intFunction));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView
        public /* bridge */ /* synthetic */ ConcurrentHashMap getMap() {
            return super.getMap();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        KeySetView(ConcurrentHashMap<K, V> concurrentHashMap, V value) {
            super(concurrentHashMap);
            this.value = value;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        public V getMappedValue() {
            return this.value;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public boolean contains(Object o) {
            return this.map.containsKey(o);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public boolean remove(Object o) {
            return this.map.remove(o) != null;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.lang.Iterable
        public java.util.Iterator<K> iterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int f = nodeArr == null ? 0 : nodeArr.length;
            return new KeyIterator(nodeArr, f, 0, f, concurrentHashMap);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        @Override // java.util.Collection, java.util.Set, j$.util.Set, j$.util.Collection
        public boolean add(K e) {
            V v = this.value;
            if (v != null) {
                return this.map.putVal(e, v, true) == null;
            }
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        @Override // java.util.Collection, java.util.Set, j$.util.Set, j$.util.Collection
        public boolean addAll(java.util.Collection<? extends K> c) {
            boolean added = false;
            V v = this.value;
            if (v == null) {
                throw new UnsupportedOperationException();
            }
            for (K e : c) {
                if (this.map.putVal(e, v, true) == null) {
                    added = true;
                }
            }
            return added;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        @Override // java.util.Collection, java.util.Set, j$.util.Set, j$.util.Collection
        public int hashCode() {
            int h = 0;
            java.util.Iterator<K> it = iterator();
            while (it.hasNext()) {
                K e = it.next();
                h += e.hashCode();
            }
            return h;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        @Override // java.util.Collection, java.util.Set, j$.util.Set, j$.util.Collection
        public boolean equals(Object o) {
            Set<?> c;
            return (o instanceof Set) && ((c = (Set) o) == this || (containsAll(c) && c.containsAll(this)));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        @Override // java.util.Collection, java.lang.Iterable, java.util.Set, j$.util.Set, j$.util.Collection, j$.lang.Iterable
        public Spliterator<K> spliterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            long n = concurrentHashMap.sumCount();
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int f = nodeArr == null ? 0 : nodeArr.length;
            return new KeySpliterator(nodeArr, f, 0, f, n < 0 ? 0L : n);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$KeySetView != java.util.concurrent.ConcurrentHashMap$KeySetView<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.function.Consumer != java.util.function.Consumer<? super K> */
        @Override // j$.util.Collection, j$.lang.Iterable
        public void forEach(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> advance = traverser.advance();
                    if (advance != null) {
                        consumer.accept((K) advance.key);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class ValuesView<K, V> extends CollectionView<K, V, V> implements java.util.Collection<V>, Serializable, j$.util.Collection<V> {
        private static final long serialVersionUID = 2249069246763182397L;

        @Override // java.lang.Iterable
        public /* synthetic */ void forEach(java.util.function.Consumer consumer) {
            forEach(C$r8$wrapper$java$util$function$Consumer$VWRP.convert(consumer));
        }

        @Override // j$.util.Collection
        public /* synthetic */ boolean removeIf(Predicate predicate) {
            return Collection.CC.$default$removeIf(this, predicate);
        }

        @Override // java.util.Collection
        public /* synthetic */ boolean removeIf(java.util.function.Predicate predicate) {
            return removeIf(C$r8$wrapper$java$util$function$Predicate$VWRP.convert(predicate));
        }

        @Override // j$.util.Collection
        public /* synthetic */ Object[] toArray(IntFunction intFunction) {
            return Collection.CC.$default$toArray(this, intFunction);
        }

        public /* synthetic */ Object[] toArray(java.util.function.IntFunction intFunction) {
            return toArray(C$r8$wrapper$java$util$function$IntFunction$VWRP.convert(intFunction));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValuesView != java.util.concurrent.ConcurrentHashMap$ValuesView<K, V> */
        ValuesView(ConcurrentHashMap<K, V> concurrentHashMap) {
            super(concurrentHashMap);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValuesView != java.util.concurrent.ConcurrentHashMap$ValuesView<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public final boolean contains(Object o) {
            return this.map.containsValue(o);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValuesView != java.util.concurrent.ConcurrentHashMap$ValuesView<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public final boolean remove(Object o) {
            if (o != null) {
                java.util.Iterator<V> it = iterator();
                while (it.hasNext()) {
                    if (o.equals(it.next())) {
                        it.remove();
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValuesView != java.util.concurrent.ConcurrentHashMap$ValuesView<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.lang.Iterable
        public final java.util.Iterator<V> iterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int f = nodeArr == null ? 0 : nodeArr.length;
            return new ValueIterator(nodeArr, f, 0, f, concurrentHashMap);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValuesView != java.util.concurrent.ConcurrentHashMap$ValuesView<K, V> */
        @Override // java.util.Collection, j$.util.Collection
        public final boolean add(V e) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValuesView != java.util.concurrent.ConcurrentHashMap$ValuesView<K, V> */
        @Override // java.util.Collection, j$.util.Collection
        public final boolean addAll(java.util.Collection<? extends V> c) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValuesView != java.util.concurrent.ConcurrentHashMap$ValuesView<K, V> */
        @Override // java.util.Collection, java.lang.Iterable, j$.util.Collection, j$.lang.Iterable
        public Spliterator<V> spliterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            long n = concurrentHashMap.sumCount();
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int f = nodeArr == null ? 0 : nodeArr.length;
            return new ValueSpliterator(nodeArr, f, 0, f, n < 0 ? 0L : n);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$ValuesView != java.util.concurrent.ConcurrentHashMap$ValuesView<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.function.Consumer != java.util.function.Consumer<? super V> */
        @Override // j$.util.Collection, j$.lang.Iterable
        public void forEach(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> advance = traverser.advance();
                    if (advance != null) {
                        consumer.accept((V) advance.val);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class EntrySetView<K, V> extends CollectionView<K, V, Map.Entry<K, V>> implements Set<Map.Entry<K, V>>, Serializable, j$.util.Set<Map.Entry<K, V>> {
        private static final long serialVersionUID = 2249069246763182397L;

        @Override // java.lang.Iterable
        public /* synthetic */ void forEach(java.util.function.Consumer consumer) {
            forEach(C$r8$wrapper$java$util$function$Consumer$VWRP.convert(consumer));
        }

        @Override // j$.util.Collection
        public /* synthetic */ boolean removeIf(Predicate predicate) {
            return Collection.CC.$default$removeIf(this, predicate);
        }

        @Override // java.util.Collection
        public /* synthetic */ boolean removeIf(java.util.function.Predicate predicate) {
            return removeIf(C$r8$wrapper$java$util$function$Predicate$VWRP.convert(predicate));
        }

        @Override // j$.util.Collection
        public /* synthetic */ Object[] toArray(IntFunction intFunction) {
            return Collection.CC.$default$toArray(this, intFunction);
        }

        public /* synthetic */ Object[] toArray(java.util.function.IntFunction intFunction) {
            return toArray(C$r8$wrapper$java$util$function$IntFunction$VWRP.convert(intFunction));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        @Override // java.util.Collection, java.util.Set, j$.util.Set, j$.util.Collection
        public /* bridge */ /* synthetic */ boolean add(Object obj) {
            return add((Map.Entry) ((Map.Entry) obj));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        EntrySetView(ConcurrentHashMap<K, V> concurrentHashMap) {
            super(concurrentHashMap);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public boolean contains(Object o) {
            Map.Entry<?, ?> e;
            Object k;
            Object r;
            Object v;
            return (!(o instanceof Map.Entry) || (k = (e = (Map.Entry) o).getKey()) == null || (r = this.map.get(k)) == null || (v = e.getValue()) == null || (v != r && !v.equals(r))) ? false : true;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public boolean remove(Object o) {
            Map.Entry<?, ?> e;
            Object k;
            Object v;
            return (o instanceof Map.Entry) && (k = (e = (Map.Entry) o).getKey()) != null && (v = e.getValue()) != null && this.map.remove(k, v);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        @Override // j$.util.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.lang.Iterable
        public java.util.Iterator<Map.Entry<K, V>> iterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int f = nodeArr == null ? 0 : nodeArr.length;
            return new EntryIterator(nodeArr, f, 0, f, concurrentHashMap);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        public boolean add(Map.Entry<K, V> e) {
            return this.map.putVal(e.getKey(), e.getValue(), false) == null;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        @Override // java.util.Collection, java.util.Set, j$.util.Set, j$.util.Collection
        public boolean addAll(java.util.Collection<? extends Map.Entry<K, V>> c) {
            boolean added = false;
            for (Map.Entry<K, V> e : c) {
                if (add((Map.Entry) e)) {
                    added = true;
                }
            }
            return added;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
        @Override // java.util.Collection, java.util.Set, j$.util.Set, j$.util.Collection
        public final int hashCode() {
            int h = 0;
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> advance = traverser.advance();
                    if (advance == null) {
                        break;
                    }
                    h += advance.hashCode();
                }
            }
            return h;
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        @Override // java.util.Collection, java.util.Set, j$.util.Set, j$.util.Collection
        public final boolean equals(Object o) {
            Set<?> c;
            return (o instanceof Set) && ((c = (Set) o) == this || (containsAll(c) && c.containsAll(this)));
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap != java.util.concurrent.ConcurrentHashMap<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        @Override // java.util.Collection, java.lang.Iterable, java.util.Set, j$.util.Set, j$.util.Collection, j$.lang.Iterable
        public Spliterator<Map.Entry<K, V>> spliterator() {
            ConcurrentHashMap<K, V> concurrentHashMap = this.map;
            long n = concurrentHashMap.sumCount();
            Node<K, V>[] nodeArr = concurrentHashMap.table;
            int f = nodeArr == null ? 0 : nodeArr.length;
            return new EntrySpliterator(nodeArr, f, 0, f, n < 0 ? 0L : n, concurrentHashMap);
        }

        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$EntrySetView != java.util.concurrent.ConcurrentHashMap$EntrySetView<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node != java.util.concurrent.ConcurrentHashMap$Node<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Node[] != java.util.concurrent.ConcurrentHashMap$Node<K, V>[] */
        /* JADX WARN: Generic types in debug info not equals: j$.util.concurrent.ConcurrentHashMap$Traverser != java.util.concurrent.ConcurrentHashMap$Traverser<K, V> */
        /* JADX WARN: Generic types in debug info not equals: j$.util.function.Consumer != java.util.function.Consumer<? super java.util.Map$Entry<K, V>> */
        @Override // j$.util.Collection, j$.lang.Iterable
        public void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> advance = traverser.advance();
                    if (advance != null) {
                        consumer.accept(new MapEntry(advance.key, advance.val, this.map));
                    } else {
                        return;
                    }
                }
            }
        }
    }
}
