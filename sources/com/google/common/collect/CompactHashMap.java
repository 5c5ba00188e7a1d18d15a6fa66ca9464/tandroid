package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import j$.util.Iterator;
import j$.util.function.Consumer;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class CompactHashMap<K, V> extends AbstractMap<K, V> implements Serializable {
    private static final Object NOT_FOUND = new Object();
    transient int[] entries;
    private transient Set<Map.Entry<K, V>> entrySetView;
    private transient Set<K> keySetView;
    transient Object[] keys;
    private transient int metadata;
    private transient int size;
    private transient Object table;
    transient Object[] values;
    private transient Collection<V> valuesView;

    void accessEntry(int i) {
    }

    int adjustAfterRemove(int i, int i2) {
        return i - 1;
    }

    static /* synthetic */ int access$1210(CompactHashMap compactHashMap) {
        int i = compactHashMap.size;
        compactHashMap.size = i - 1;
        return i;
    }

    public static <K, V> CompactHashMap<K, V> create() {
        return new CompactHashMap<>();
    }

    public static <K, V> CompactHashMap<K, V> createWithExpectedSize(int i) {
        return new CompactHashMap<>(i);
    }

    CompactHashMap() {
        init(3);
    }

    CompactHashMap(int i) {
        init(i);
    }

    void init(int i) {
        Preconditions.checkArgument(i >= 0, "Expected size must be >= 0");
        this.metadata = Ints.constrainToRange(i, 1, 1073741823);
    }

    boolean needsAllocArrays() {
        return this.table == null;
    }

    int allocArrays() {
        Preconditions.checkState(needsAllocArrays(), "Arrays already allocated");
        int i = this.metadata;
        int tableSize = CompactHashing.tableSize(i);
        this.table = CompactHashing.createTable(tableSize);
        setHashTableMask(tableSize - 1);
        this.entries = new int[i];
        this.keys = new Object[i];
        this.values = new Object[i];
        return i;
    }

    Map<K, V> delegateOrNull() {
        Object obj = this.table;
        if (obj instanceof Map) {
            return (Map) obj;
        }
        return null;
    }

    Map<K, V> createHashFloodingResistantDelegate(int i) {
        return new LinkedHashMap(i, 1.0f);
    }

    Map<K, V> convertToHashFloodingResistantImplementation() {
        Map<K, V> createHashFloodingResistantDelegate = createHashFloodingResistantDelegate(hashTableMask() + 1);
        int firstEntryIndex = firstEntryIndex();
        while (firstEntryIndex >= 0) {
            createHashFloodingResistantDelegate.put(key(firstEntryIndex), value(firstEntryIndex));
            firstEntryIndex = getSuccessor(firstEntryIndex);
        }
        this.table = createHashFloodingResistantDelegate;
        this.entries = null;
        this.keys = null;
        this.values = null;
        incrementModCount();
        return createHashFloodingResistantDelegate;
    }

    private void setHashTableMask(int i) {
        this.metadata = CompactHashing.maskCombine(this.metadata, 32 - Integer.numberOfLeadingZeros(i), 31);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int hashTableMask() {
        return (1 << (this.metadata & 31)) - 1;
    }

    void incrementModCount() {
        this.metadata += 32;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k, V v) {
        int resizeTable;
        int i;
        if (needsAllocArrays()) {
            allocArrays();
        }
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.put(k, v);
        }
        int[] requireEntries = requireEntries();
        Object[] requireKeys = requireKeys();
        Object[] requireValues = requireValues();
        int i2 = this.size;
        int i3 = i2 + 1;
        int smearedHash = Hashing.smearedHash(k);
        int hashTableMask = hashTableMask();
        int i4 = smearedHash & hashTableMask;
        int tableGet = CompactHashing.tableGet(requireTable(), i4);
        if (tableGet != 0) {
            int hashPrefix = CompactHashing.getHashPrefix(smearedHash, hashTableMask);
            int i5 = 0;
            while (true) {
                int i6 = tableGet - 1;
                int i7 = requireEntries[i6];
                if (CompactHashing.getHashPrefix(i7, hashTableMask) == hashPrefix && Objects.equal(k, requireKeys[i6])) {
                    V v2 = (V) requireValues[i6];
                    requireValues[i6] = v;
                    accessEntry(i6);
                    return v2;
                }
                int next = CompactHashing.getNext(i7, hashTableMask);
                i5++;
                if (next != 0) {
                    tableGet = next;
                } else if (i5 >= 9) {
                    return convertToHashFloodingResistantImplementation().put(k, v);
                } else {
                    if (i3 > hashTableMask) {
                        resizeTable = resizeTable(hashTableMask, CompactHashing.newCapacity(hashTableMask), smearedHash, i2);
                    } else {
                        requireEntries[i6] = CompactHashing.maskCombine(i7, i3, hashTableMask);
                    }
                }
            }
        } else if (i3 > hashTableMask) {
            resizeTable = resizeTable(hashTableMask, CompactHashing.newCapacity(hashTableMask), smearedHash, i2);
            i = resizeTable;
        } else {
            CompactHashing.tableSet(requireTable(), i4, i3);
            i = hashTableMask;
        }
        resizeMeMaybe(i3);
        insertEntry(i2, k, v, smearedHash, i);
        this.size = i3;
        incrementModCount();
        return null;
    }

    void insertEntry(int i, K k, V v, int i2, int i3) {
        setEntry(i, CompactHashing.maskCombine(i2, 0, i3));
        setKey(i, k);
        setValue(i, v);
    }

    private void resizeMeMaybe(int i) {
        int min;
        int length = requireEntries().length;
        if (i <= length || (min = Math.min(1073741823, (Math.max(1, length >>> 1) + length) | 1)) == length) {
            return;
        }
        resizeEntries(min);
    }

    void resizeEntries(int i) {
        this.entries = Arrays.copyOf(requireEntries(), i);
        this.keys = Arrays.copyOf(requireKeys(), i);
        this.values = Arrays.copyOf(requireValues(), i);
    }

    private int resizeTable(int i, int i2, int i3, int i4) {
        Object createTable = CompactHashing.createTable(i2);
        int i5 = i2 - 1;
        if (i4 != 0) {
            CompactHashing.tableSet(createTable, i3 & i5, i4 + 1);
        }
        Object requireTable = requireTable();
        int[] requireEntries = requireEntries();
        for (int i6 = 0; i6 <= i; i6++) {
            int tableGet = CompactHashing.tableGet(requireTable, i6);
            while (tableGet != 0) {
                int i7 = tableGet - 1;
                int i8 = requireEntries[i7];
                int hashPrefix = CompactHashing.getHashPrefix(i8, i) | i6;
                int i9 = hashPrefix & i5;
                int tableGet2 = CompactHashing.tableGet(createTable, i9);
                CompactHashing.tableSet(createTable, i9, tableGet);
                requireEntries[i7] = CompactHashing.maskCombine(hashPrefix, tableGet2, i5);
                tableGet = CompactHashing.getNext(i8, i);
            }
        }
        this.table = createTable;
        setHashTableMask(i5);
        return i5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int indexOf(Object obj) {
        if (needsAllocArrays()) {
            return -1;
        }
        int smearedHash = Hashing.smearedHash(obj);
        int hashTableMask = hashTableMask();
        int tableGet = CompactHashing.tableGet(requireTable(), smearedHash & hashTableMask);
        if (tableGet == 0) {
            return -1;
        }
        int hashPrefix = CompactHashing.getHashPrefix(smearedHash, hashTableMask);
        do {
            int i = tableGet - 1;
            int entry = entry(i);
            if (CompactHashing.getHashPrefix(entry, hashTableMask) == hashPrefix && Objects.equal(obj, key(i))) {
                return i;
            }
            tableGet = CompactHashing.getNext(entry, hashTableMask);
        } while (tableGet != 0);
        return -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.containsKey(obj);
        }
        return indexOf(obj) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.get(obj);
        }
        int indexOf = indexOf(obj);
        if (indexOf == -1) {
            return null;
        }
        accessEntry(indexOf);
        return value(indexOf);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.remove(obj);
        }
        V v = (V) removeHelper(obj);
        if (v == NOT_FOUND) {
            return null;
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object removeHelper(Object obj) {
        if (needsAllocArrays()) {
            return NOT_FOUND;
        }
        int hashTableMask = hashTableMask();
        int remove = CompactHashing.remove(obj, null, hashTableMask, requireTable(), requireEntries(), requireKeys(), null);
        if (remove == -1) {
            return NOT_FOUND;
        }
        V value = value(remove);
        moveLastEntry(remove, hashTableMask);
        this.size--;
        incrementModCount();
        return value;
    }

    void moveLastEntry(int i, int i2) {
        Object requireTable = requireTable();
        int[] requireEntries = requireEntries();
        Object[] requireKeys = requireKeys();
        Object[] requireValues = requireValues();
        int size = size() - 1;
        if (i < size) {
            Object obj = requireKeys[size];
            requireKeys[i] = obj;
            requireValues[i] = requireValues[size];
            requireKeys[size] = null;
            requireValues[size] = null;
            requireEntries[i] = requireEntries[size];
            requireEntries[size] = 0;
            int smearedHash = Hashing.smearedHash(obj) & i2;
            int tableGet = CompactHashing.tableGet(requireTable, smearedHash);
            int i3 = size + 1;
            if (tableGet == i3) {
                CompactHashing.tableSet(requireTable, smearedHash, i + 1);
                return;
            }
            while (true) {
                int i4 = tableGet - 1;
                int i5 = requireEntries[i4];
                int next = CompactHashing.getNext(i5, i2);
                if (next == i3) {
                    requireEntries[i4] = CompactHashing.maskCombine(i5, i + 1, i2);
                    return;
                }
                tableGet = next;
            }
        } else {
            requireKeys[i] = null;
            requireValues[i] = null;
            requireEntries[i] = 0;
        }
    }

    int firstEntryIndex() {
        return isEmpty() ? -1 : 0;
    }

    int getSuccessor(int i) {
        int i2 = i + 1;
        if (i2 < this.size) {
            return i2;
        }
        return -1;
    }

    /* loaded from: classes.dex */
    private abstract class Itr<T> implements Iterator<T>, j$.util.Iterator {
        int currentIndex;
        int expectedMetadata;
        int indexToRemove;

        @Override // j$.util.Iterator
        public /* synthetic */ void forEachRemaining(Consumer consumer) {
            Iterator.-CC.$default$forEachRemaining(this, consumer);
        }

        abstract T getOutput(int i);

        private Itr() {
            this.expectedMetadata = CompactHashMap.this.metadata;
            this.currentIndex = CompactHashMap.this.firstEntryIndex();
            this.indexToRemove = -1;
        }

        @Override // java.util.Iterator, j$.util.Iterator
        public boolean hasNext() {
            return this.currentIndex >= 0;
        }

        @Override // java.util.Iterator, j$.util.Iterator
        public T next() {
            checkForConcurrentModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i = this.currentIndex;
            this.indexToRemove = i;
            T output = getOutput(i);
            this.currentIndex = CompactHashMap.this.getSuccessor(this.currentIndex);
            return output;
        }

        @Override // java.util.Iterator, j$.util.Iterator
        public void remove() {
            checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.indexToRemove >= 0);
            incrementExpectedModCount();
            CompactHashMap compactHashMap = CompactHashMap.this;
            compactHashMap.remove(compactHashMap.key(this.indexToRemove));
            this.currentIndex = CompactHashMap.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
            this.indexToRemove = -1;
        }

        void incrementExpectedModCount() {
            this.expectedMetadata += 32;
        }

        private void checkForConcurrentModification() {
            if (CompactHashMap.this.metadata != this.expectedMetadata) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> set = this.keySetView;
        if (set == null) {
            Set<K> createKeySet = createKeySet();
            this.keySetView = createKeySet;
            return createKeySet;
        }
        return set;
    }

    Set<K> createKeySet() {
        return new KeySetView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class KeySetView extends AbstractSet<K> {
        KeySetView() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return CompactHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return CompactHashMap.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Map<K, V> delegateOrNull = CompactHashMap.this.delegateOrNull();
            if (delegateOrNull != null) {
                return delegateOrNull.keySet().remove(obj);
            }
            return CompactHashMap.this.removeHelper(obj) != CompactHashMap.NOT_FOUND;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public java.util.Iterator<K> iterator() {
            return CompactHashMap.this.keySetIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            CompactHashMap.this.clear();
        }
    }

    java.util.Iterator<K> keySetIterator() {
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.keySet().iterator();
        }
        return new CompactHashMap<K, V>.Itr<K>() { // from class: com.google.common.collect.CompactHashMap.1
            @Override // com.google.common.collect.CompactHashMap.Itr
            K getOutput(int i) {
                return (K) CompactHashMap.this.key(i);
            }
        };
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySetView;
        if (set == null) {
            Set<Map.Entry<K, V>> createEntrySet = createEntrySet();
            this.entrySetView = createEntrySet;
            return createEntrySet;
        }
        return set;
    }

    Set<Map.Entry<K, V>> createEntrySet() {
        return new EntrySetView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class EntrySetView extends AbstractSet<Map.Entry<K, V>> {
        EntrySetView() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return CompactHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            CompactHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public java.util.Iterator<Map.Entry<K, V>> iterator() {
            return CompactHashMap.this.entrySetIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            Map<K, V> delegateOrNull = CompactHashMap.this.delegateOrNull();
            if (delegateOrNull != null) {
                return delegateOrNull.entrySet().contains(obj);
            }
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                int indexOf = CompactHashMap.this.indexOf(entry.getKey());
                return indexOf != -1 && Objects.equal(CompactHashMap.this.value(indexOf), entry.getValue());
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Map<K, V> delegateOrNull = CompactHashMap.this.delegateOrNull();
            if (delegateOrNull != null) {
                return delegateOrNull.entrySet().remove(obj);
            }
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                if (CompactHashMap.this.needsAllocArrays()) {
                    return false;
                }
                int hashTableMask = CompactHashMap.this.hashTableMask();
                int remove = CompactHashing.remove(entry.getKey(), entry.getValue(), hashTableMask, CompactHashMap.this.requireTable(), CompactHashMap.this.requireEntries(), CompactHashMap.this.requireKeys(), CompactHashMap.this.requireValues());
                if (remove == -1) {
                    return false;
                }
                CompactHashMap.this.moveLastEntry(remove, hashTableMask);
                CompactHashMap.access$1210(CompactHashMap.this);
                CompactHashMap.this.incrementModCount();
                return true;
            }
            return false;
        }
    }

    java.util.Iterator<Map.Entry<K, V>> entrySetIterator() {
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.entrySet().iterator();
        }
        return new CompactHashMap<K, V>.Itr<Map.Entry<K, V>>() { // from class: com.google.common.collect.CompactHashMap.2
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.collect.CompactHashMap.Itr
            public Map.Entry<K, V> getOutput(int i) {
                return new MapEntry(i);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class MapEntry extends AbstractMapEntry<K, V> {
        private final K key;
        private int lastKnownIndex;

        MapEntry(int i) {
            this.key = (K) CompactHashMap.this.key(i);
            this.lastKnownIndex = i;
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        private void updateLastKnownIndex() {
            int i = this.lastKnownIndex;
            if (i == -1 || i >= CompactHashMap.this.size() || !Objects.equal(this.key, CompactHashMap.this.key(this.lastKnownIndex))) {
                this.lastKnownIndex = CompactHashMap.this.indexOf(this.key);
            }
        }

        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
        public V getValue() {
            Map<K, V> delegateOrNull = CompactHashMap.this.delegateOrNull();
            if (delegateOrNull != null) {
                return (V) NullnessCasts.uncheckedCastNullableTToT(delegateOrNull.get(this.key));
            }
            updateLastKnownIndex();
            int i = this.lastKnownIndex;
            return i == -1 ? (V) NullnessCasts.unsafeNull() : (V) CompactHashMap.this.value(i);
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            Map<K, V> delegateOrNull = CompactHashMap.this.delegateOrNull();
            if (delegateOrNull != null) {
                return (V) NullnessCasts.uncheckedCastNullableTToT(delegateOrNull.put(this.key, v));
            }
            updateLastKnownIndex();
            int i = this.lastKnownIndex;
            if (i != -1) {
                V v2 = (V) CompactHashMap.this.value(i);
                CompactHashMap.this.setValue(this.lastKnownIndex, v);
                return v2;
            }
            CompactHashMap.this.put(this.key, v);
            return (V) NullnessCasts.unsafeNull();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        Map<K, V> delegateOrNull = delegateOrNull();
        return delegateOrNull != null ? delegateOrNull.size() : this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.containsValue(obj);
        }
        for (int i = 0; i < this.size; i++) {
            if (Objects.equal(obj, value(i))) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> collection = this.valuesView;
        if (collection == null) {
            Collection<V> createValues = createValues();
            this.valuesView = createValues;
            return createValues;
        }
        return collection;
    }

    Collection<V> createValues() {
        return new ValuesView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ValuesView extends AbstractCollection<V> {
        ValuesView() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return CompactHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            CompactHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public java.util.Iterator<V> iterator() {
            return CompactHashMap.this.valuesIterator();
        }
    }

    java.util.Iterator<V> valuesIterator() {
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            return delegateOrNull.values().iterator();
        }
        return new CompactHashMap<K, V>.Itr<V>() { // from class: com.google.common.collect.CompactHashMap.3
            @Override // com.google.common.collect.CompactHashMap.Itr
            V getOutput(int i) {
                return (V) CompactHashMap.this.value(i);
            }
        };
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        if (needsAllocArrays()) {
            return;
        }
        incrementModCount();
        Map<K, V> delegateOrNull = delegateOrNull();
        if (delegateOrNull != null) {
            this.metadata = Ints.constrainToRange(size(), 3, 1073741823);
            delegateOrNull.clear();
            this.table = null;
            this.size = 0;
            return;
        }
        Arrays.fill(requireKeys(), 0, this.size, (Object) null);
        Arrays.fill(requireValues(), 0, this.size, (Object) null);
        CompactHashing.tableClear(requireTable());
        Arrays.fill(requireEntries(), 0, this.size, 0);
        this.size = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object requireTable() {
        Object obj = this.table;
        java.util.Objects.requireNonNull(obj);
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] requireEntries() {
        int[] iArr = this.entries;
        java.util.Objects.requireNonNull(iArr);
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object[] requireKeys() {
        Object[] objArr = this.keys;
        java.util.Objects.requireNonNull(objArr);
        return objArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object[] requireValues() {
        Object[] objArr = this.values;
        java.util.Objects.requireNonNull(objArr);
        return objArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public K key(int i) {
        return (K) requireKeys()[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V value(int i) {
        return (V) requireValues()[i];
    }

    private int entry(int i) {
        return requireEntries()[i];
    }

    private void setKey(int i, K k) {
        requireKeys()[i] = k;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setValue(int i, V v) {
        requireValues()[i] = v;
    }

    private void setEntry(int i, int i2) {
        requireEntries()[i] = i2;
    }
}
