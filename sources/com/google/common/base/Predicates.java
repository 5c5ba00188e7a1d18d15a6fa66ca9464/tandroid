package com.google.common.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Predicates {

    /* loaded from: classes.dex */
    private static class AndPredicate implements Predicate, Serializable {
        private final List components;

        private AndPredicate(List list) {
            this.components = list;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(Object obj) {
            for (int i = 0; i < this.components.size(); i++) {
                if (!((Predicate) this.components.get(i)).apply(obj)) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(Object obj) {
            if (obj instanceof AndPredicate) {
                return this.components.equals(((AndPredicate) obj).components);
            }
            return false;
        }

        public int hashCode() {
            return this.components.hashCode() + 306654252;
        }

        public String toString() {
            return Predicates.toStringHelper("and", this.components);
        }
    }

    public static Predicate and(Predicate predicate, Predicate predicate2) {
        return new AndPredicate(asList((Predicate) Preconditions.checkNotNull(predicate), (Predicate) Preconditions.checkNotNull(predicate2)));
    }

    private static List asList(Predicate predicate, Predicate predicate2) {
        return Arrays.asList(predicate, predicate2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String toStringHelper(String str, Iterable iterable) {
        StringBuilder sb = new StringBuilder("Predicates.");
        sb.append(str);
        sb.append('(');
        boolean z = true;
        for (Object obj : iterable) {
            if (!z) {
                sb.append(',');
            }
            sb.append(obj);
            z = false;
        }
        sb.append(')');
        return sb.toString();
    }
}
