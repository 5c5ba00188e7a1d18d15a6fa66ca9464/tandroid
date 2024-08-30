package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: classes.dex */
public abstract class MultimapBuilder {

    /* loaded from: classes.dex */
    private static final class ArrayListSupplier implements Supplier, Serializable {
        private final int expectedValuesPerKey;

        ArrayListSupplier(int i) {
            this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(i, "expectedValuesPerKey");
        }

        @Override // com.google.common.base.Supplier
        public List get() {
            return new ArrayList(this.expectedValuesPerKey);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ListMultimapBuilder extends MultimapBuilder {
        ListMultimapBuilder() {
            super();
        }

        public abstract ListMultimap build();
    }

    /* loaded from: classes.dex */
    public static abstract class MultimapBuilderWithKeys {
        MultimapBuilderWithKeys() {
        }

        public ListMultimapBuilder arrayListValues() {
            return arrayListValues(2);
        }

        public ListMultimapBuilder arrayListValues(final int i) {
            CollectPreconditions.checkNonnegative(i, "expectedValuesPerKey");
            return new ListMultimapBuilder() { // from class: com.google.common.collect.MultimapBuilder.MultimapBuilderWithKeys.1
                @Override // com.google.common.collect.MultimapBuilder.ListMultimapBuilder
                public ListMultimap build() {
                    return Multimaps.newListMultimap(MultimapBuilderWithKeys.this.createMap(), new ArrayListSupplier(i));
                }
            };
        }

        abstract Map createMap();
    }

    private MultimapBuilder() {
    }

    public static MultimapBuilderWithKeys hashKeys() {
        return hashKeys(8);
    }

    public static MultimapBuilderWithKeys hashKeys(final int i) {
        CollectPreconditions.checkNonnegative(i, "expectedKeys");
        return new MultimapBuilderWithKeys() { // from class: com.google.common.collect.MultimapBuilder.1
            @Override // com.google.common.collect.MultimapBuilder.MultimapBuilderWithKeys
            Map createMap() {
                return Platform.newHashMapWithExpectedSize(i);
            }
        };
    }

    public static MultimapBuilderWithKeys treeKeys() {
        return treeKeys(Ordering.natural());
    }

    public static MultimapBuilderWithKeys treeKeys(final Comparator comparator) {
        Preconditions.checkNotNull(comparator);
        return new MultimapBuilderWithKeys() { // from class: com.google.common.collect.MultimapBuilder.3
            @Override // com.google.common.collect.MultimapBuilder.MultimapBuilderWithKeys
            Map createMap() {
                return new TreeMap(comparator);
            }
        };
    }
}
