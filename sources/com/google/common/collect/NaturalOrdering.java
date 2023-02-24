package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
/* loaded from: classes.dex */
final class NaturalOrdering extends Ordering<Comparable<?>> implements Serializable {
    static final NaturalOrdering INSTANCE = new NaturalOrdering();

    public String toString() {
        return "Ordering.natural()";
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(Comparable<?> comparable, Comparable<?> comparable2) {
        Preconditions.checkNotNull(comparable);
        Preconditions.checkNotNull(comparable2);
        return comparable.compareTo(comparable2);
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable<?>> Ordering<S> reverse() {
        return ReverseNaturalOrdering.INSTANCE;
    }

    private NaturalOrdering() {
    }
}
