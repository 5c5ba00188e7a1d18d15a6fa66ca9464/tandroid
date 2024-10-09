package com.google.android.play.integrity.internal;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public abstract class d {
    public static final List a(List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            f fVar = (f) it.next();
            Bundle bundle = new Bundle();
            bundle.putInt("event_type", fVar.a());
            bundle.putLong("event_timestamp", fVar.b());
            arrayList.add(bundle);
        }
        return arrayList;
    }

    public static final void b(int i, List list) {
        list.add(f.c(i, System.currentTimeMillis()));
    }
}
