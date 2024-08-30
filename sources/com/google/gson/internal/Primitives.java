package com.google.gson.internal;

import java.lang.reflect.Type;
/* loaded from: classes.dex */
public abstract class Primitives {
    public static boolean isPrimitive(Type type) {
        return (type instanceof Class) && ((Class) type).isPrimitive();
    }
}
