package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;

/* loaded from: classes.dex */
final class ReverseOrdering extends Ordering implements Serializable {
    final Ordering forwardOrder;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReverseOrdering(Ordering ordering) {
        this.forwardOrder = (Ordering) Preconditions.checkNotNull(ordering);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(Object obj, Object obj2) {
        return this.forwardOrder.compare(obj2, obj);
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ReverseOrdering) {
            return this.forwardOrder.equals(((ReverseOrdering) obj).forwardOrder);
        }
        return false;
    }

    public int hashCode() {
        return -this.forwardOrder.hashCode();
    }

    @Override // com.google.common.collect.Ordering
    public Ordering reverse() {
        return this.forwardOrder;
    }

    public String toString() {
        String valueOf = String.valueOf(this.forwardOrder);
        StringBuilder sb = new StringBuilder(valueOf.length() + 10);
        sb.append(valueOf);
        sb.append(".reverse()");
        return sb.toString();
    }
}
