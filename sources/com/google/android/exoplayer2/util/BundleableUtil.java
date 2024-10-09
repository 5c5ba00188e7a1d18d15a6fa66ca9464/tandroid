package com.google.android.exoplayer2.util;

import android.os.Bundle;
import android.util.SparseArray;
import com.google.android.exoplayer2.Bundleable;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BundleableUtil {
    public static void ensureClassLoader(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader((ClassLoader) Util.castNonNull(BundleableUtil.class.getClassLoader()));
        }
    }

    public static ImmutableList fromBundleList(Bundleable.Creator creator, List list) {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (int i = 0; i < list.size(); i++) {
            builder.add((Object) creator.fromBundle((Bundle) Assertions.checkNotNull((Bundle) list.get(i))));
        }
        return builder.build();
    }

    public static SparseArray fromBundleSparseArray(Bundleable.Creator creator, SparseArray sparseArray) {
        SparseArray sparseArray2 = new SparseArray(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            sparseArray2.put(sparseArray.keyAt(i), creator.fromBundle((Bundle) sparseArray.valueAt(i)));
        }
        return sparseArray2;
    }

    public static ArrayList toBundleArrayList(Collection collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(((Bundleable) it.next()).toBundle());
        }
        return arrayList;
    }

    public static SparseArray toBundleSparseArray(SparseArray sparseArray) {
        SparseArray sparseArray2 = new SparseArray(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            sparseArray2.put(sparseArray.keyAt(i), ((Bundleable) sparseArray.valueAt(i)).toBundle());
        }
        return sparseArray2;
    }
}
