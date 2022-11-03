package com.google.gson;
/* loaded from: classes.dex */
public interface ReflectionAccessFilter {

    /* loaded from: classes.dex */
    public enum FilterResult {
        ALLOW,
        INDECISIVE,
        BLOCK_INACCESSIBLE,
        BLOCK_ALL
    }

    FilterResult check(Class<?> cls);
}
