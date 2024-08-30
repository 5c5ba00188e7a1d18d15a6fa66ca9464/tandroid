package com.google.firebase.components;

import com.google.firebase.inject.Provider;
import java.util.Set;
/* loaded from: classes.dex */
public interface ComponentContainer {
    Object get(Class cls);

    Provider getProvider(Class cls);

    Set setOf(Class cls);

    Provider setOfProvider(Class cls);
}
