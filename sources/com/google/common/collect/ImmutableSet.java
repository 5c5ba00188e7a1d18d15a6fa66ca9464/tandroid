package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

/* loaded from: classes.dex */
public abstract class ImmutableSet extends ImmutableCollection implements Set {
    private transient ImmutableList asList;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int chooseTableSize(int i) {
        int max = Math.max(i, 2);
        if (max >= 751619276) {
            Preconditions.checkArgument(max < 1073741824, "collection too large");
            return 1073741824;
        }
        int highestOneBit = Integer.highestOneBit(max - 1) << 1;
        while (true) {
            double d = highestOneBit;
            Double.isNaN(d);
            if (d * 0.7d >= max) {
                return highestOneBit;
            }
            highestOneBit <<= 1;
        }
    }

    private static ImmutableSet construct(int i, Object... objArr) {
        if (i == 0) {
            return of();
        }
        if (i == 1) {
            Object obj = objArr[0];
            Objects.requireNonNull(obj);
            return of(obj);
        }
        int chooseTableSize = chooseTableSize(i);
        Object[] objArr2 = new Object[chooseTableSize];
        int i2 = chooseTableSize - 1;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < i; i5++) {
            Object checkElementNotNull = ObjectArrays.checkElementNotNull(objArr[i5], i5);
            int hashCode = checkElementNotNull.hashCode();
            int smear = Hashing.smear(hashCode);
            while (true) {
                int i6 = smear & i2;
                Object obj2 = objArr2[i6];
                if (obj2 == null) {
                    objArr[i4] = checkElementNotNull;
                    objArr2[i6] = checkElementNotNull;
                    i3 += hashCode;
                    i4++;
                    break;
                }
                if (obj2.equals(checkElementNotNull)) {
                    break;
                }
                smear++;
            }
        }
        Arrays.fill(objArr, i4, i, (Object) null);
        if (i4 == 1) {
            Object obj3 = objArr[0];
            Objects.requireNonNull(obj3);
            return new SingletonImmutableSet(obj3);
        }
        if (chooseTableSize(i4) < chooseTableSize / 2) {
            return construct(i4, objArr);
        }
        if (shouldTrim(i4, objArr.length)) {
            objArr = Arrays.copyOf(objArr, i4);
        }
        return new RegularImmutableSet(objArr, i3, objArr2, i2, i4);
    }

    public static ImmutableSet copyOf(Collection collection) {
        if ((collection instanceof ImmutableSet) && !(collection instanceof SortedSet)) {
            ImmutableSet immutableSet = (ImmutableSet) collection;
            if (!immutableSet.isPartialView()) {
                return immutableSet;
            }
        }
        Object[] array = collection.toArray();
        return construct(array.length, array);
    }

    public static ImmutableSet copyOf(Object[] objArr) {
        int length = objArr.length;
        return length != 0 ? length != 1 ? construct(objArr.length, (Object[]) objArr.clone()) : of(objArr[0]) : of();
    }

    public static ImmutableSet of() {
        return RegularImmutableSet.EMPTY;
    }

    public static ImmutableSet of(Object obj) {
        return new SingletonImmutableSet(obj);
    }

    public static ImmutableSet of(Object obj, Object obj2) {
        return construct(2, obj, obj2);
    }

    public static ImmutableSet of(Object obj, Object obj2, Object obj3) {
        return construct(3, obj, obj2, obj3);
    }

    public static ImmutableSet of(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        return construct(5, obj, obj2, obj3, obj4, obj5);
    }

    private static boolean shouldTrim(int i, int i2) {
        return i < (i2 >> 1) + (i2 >> 2);
    }

    @Override // com.google.common.collect.ImmutableCollection
    public ImmutableList asList() {
        ImmutableList immutableList = this.asList;
        if (immutableList != null) {
            return immutableList;
        }
        ImmutableList createAsList = createAsList();
        this.asList = createAsList;
        return createAsList;
    }

    ImmutableList createAsList() {
        return ImmutableList.asImmutableList(toArray());
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj instanceof ImmutableSet) && isHashCodeFast() && ((ImmutableSet) obj).isHashCodeFast() && hashCode() != obj.hashCode()) {
            return false;
        }
        return Sets.equalsImpl(this, obj);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    boolean isHashCodeFast() {
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public abstract UnmodifiableIterator iterator();
}
