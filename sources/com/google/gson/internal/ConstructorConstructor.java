package com.google.gson.internal;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import j$.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
/* loaded from: classes.dex */
public final class ConstructorConstructor {
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    private final List<ReflectionAccessFilter> reflectionFilters;
    private final boolean useJdkUnsafe;

    public ConstructorConstructor(Map<Type, InstanceCreator<?>> map, boolean z, List<ReflectionAccessFilter> list) {
        this.instanceCreators = map;
        this.useJdkUnsafe = z;
        this.reflectionFilters = list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String checkInstantiable(Class<?> cls) {
        int modifiers = cls.getModifiers();
        if (Modifier.isInterface(modifiers)) {
            return "Interfaces can't be instantiated! Register an InstanceCreator or a TypeAdapter for this type. Interface name: " + cls.getName();
        } else if (!Modifier.isAbstract(modifiers)) {
            return null;
        } else {
            return "Abstract classes can't be instantiated! Register an InstanceCreator or a TypeAdapter for this type. Class name: " + cls.getName();
        }
    }

    public <T> ObjectConstructor<T> get(TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        Class<? super T> rawType = typeToken.getRawType();
        final InstanceCreator<?> instanceCreator = this.instanceCreators.get(type);
        if (instanceCreator != null) {
            return new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.1
                /* JADX WARN: Type inference failed for: r0v1, types: [T, java.lang.Object] */
                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return instanceCreator.createInstance(type);
                }
            };
        }
        final InstanceCreator<?> instanceCreator2 = this.instanceCreators.get(rawType);
        if (instanceCreator2 != null) {
            return new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.2
                /* JADX WARN: Type inference failed for: r0v1, types: [T, java.lang.Object] */
                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return instanceCreator2.createInstance(type);
                }
            };
        }
        ObjectConstructor<T> newSpecialCollectionConstructor = newSpecialCollectionConstructor(type, rawType);
        if (newSpecialCollectionConstructor != null) {
            return newSpecialCollectionConstructor;
        }
        ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, rawType);
        ObjectConstructor<T> newDefaultConstructor = newDefaultConstructor(rawType, filterResult);
        if (newDefaultConstructor != null) {
            return newDefaultConstructor;
        }
        ObjectConstructor<T> newDefaultImplementationConstructor = newDefaultImplementationConstructor(type, rawType);
        if (newDefaultImplementationConstructor != null) {
            return newDefaultImplementationConstructor;
        }
        final String checkInstantiable = checkInstantiable(rawType);
        if (checkInstantiable != null) {
            return new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.3
                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    throw new JsonIOException(checkInstantiable);
                }
            };
        }
        if (filterResult == ReflectionAccessFilter.FilterResult.ALLOW) {
            return newUnsafeAllocator(rawType);
        }
        final String str = "Unable to create instance of " + rawType + "; ReflectionAccessFilter does not permit using reflection or Unsafe. Register an InstanceCreator or a TypeAdapter for this type or adjust the access filter to allow using reflection.";
        return new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.4
            @Override // com.google.gson.internal.ObjectConstructor
            public T construct() {
                throw new JsonIOException(str);
            }
        };
    }

    private static <T> ObjectConstructor<T> newSpecialCollectionConstructor(final Type type, Class<? super T> cls) {
        if (EnumSet.class.isAssignableFrom(cls)) {
            return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.5
                /* JADX WARN: Type inference failed for: r0v7, types: [T, java.util.EnumSet] */
                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    Type type2 = type;
                    if (type2 instanceof ParameterizedType) {
                        Type type3 = ((ParameterizedType) type2).getActualTypeArguments()[0];
                        if (type3 instanceof Class) {
                            return EnumSet.noneOf((Class) type3);
                        }
                        throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                    }
                    throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                }
            };
        }
        if (cls != EnumMap.class) {
            return null;
        }
        return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.6
            /* JADX WARN: Type inference failed for: r1v7, types: [T, java.util.EnumMap] */
            @Override // com.google.gson.internal.ObjectConstructor
            public T construct() {
                Type type2 = type;
                if (type2 instanceof ParameterizedType) {
                    Type type3 = ((ParameterizedType) type2).getActualTypeArguments()[0];
                    if (type3 instanceof Class) {
                        return new EnumMap((Class) type3);
                    }
                    throw new JsonIOException("Invalid EnumMap type: " + type.toString());
                }
                throw new JsonIOException("Invalid EnumMap type: " + type.toString());
            }
        };
    }

    private static <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> cls, ReflectionAccessFilter.FilterResult filterResult) {
        final String tryMakeAccessible;
        if (Modifier.isAbstract(cls.getModifiers())) {
            return null;
        }
        boolean z = false;
        try {
            final Constructor<? super T> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            ReflectionAccessFilter.FilterResult filterResult2 = ReflectionAccessFilter.FilterResult.ALLOW;
            if (filterResult == filterResult2 || (ReflectionAccessFilterHelper.canAccess(declaredConstructor, null) && (filterResult != ReflectionAccessFilter.FilterResult.BLOCK_ALL || Modifier.isPublic(declaredConstructor.getModifiers())))) {
                z = true;
            }
            if (z) {
                if (filterResult == filterResult2 && (tryMakeAccessible = ReflectionHelper.tryMakeAccessible(declaredConstructor)) != null) {
                    return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.8
                        @Override // com.google.gson.internal.ObjectConstructor
                        public T construct() {
                            throw new JsonIOException(tryMakeAccessible);
                        }
                    };
                }
                return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.9
                    /* JADX WARN: Type inference failed for: r0v7, types: [T, java.lang.Object] */
                    @Override // com.google.gson.internal.ObjectConstructor
                    public T construct() {
                        try {
                            return declaredConstructor.newInstance(new Object[0]);
                        } catch (IllegalAccessException e) {
                            throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
                        } catch (InstantiationException e2) {
                            throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(declaredConstructor) + "' with no args", e2);
                        } catch (InvocationTargetException e3) {
                            throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(declaredConstructor) + "' with no args", e3.getCause());
                        }
                    }
                };
            }
            final String str = "Unable to invoke no-args constructor of " + cls + "; constructor is not accessible and ReflectionAccessFilter does not permit making it accessible. Register an InstanceCreator or a TypeAdapter for this type, change the visibility of the constructor or adjust the access filter.";
            return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.7
                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    throw new JsonIOException(str);
                }
            };
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    private static <T> ObjectConstructor<T> newDefaultImplementationConstructor(Type type, Class<? super T> cls) {
        if (Collection.class.isAssignableFrom(cls)) {
            if (SortedSet.class.isAssignableFrom(cls)) {
                return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.10
                    /* JADX WARN: Type inference failed for: r0v0, types: [T, java.util.TreeSet] */
                    @Override // com.google.gson.internal.ObjectConstructor
                    public T construct() {
                        return new TreeSet();
                    }
                };
            }
            if (Set.class.isAssignableFrom(cls)) {
                return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.11
                    /* JADX WARN: Type inference failed for: r0v0, types: [T, java.util.LinkedHashSet] */
                    @Override // com.google.gson.internal.ObjectConstructor
                    public T construct() {
                        return new LinkedHashSet();
                    }
                };
            }
            if (Queue.class.isAssignableFrom(cls)) {
                return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.12
                    /* JADX WARN: Type inference failed for: r0v0, types: [T, java.util.ArrayDeque] */
                    @Override // com.google.gson.internal.ObjectConstructor
                    public T construct() {
                        return new ArrayDeque();
                    }
                };
            }
            return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.13
                /* JADX WARN: Type inference failed for: r0v0, types: [T, java.util.ArrayList] */
                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return new ArrayList();
                }
            };
        } else if (!Map.class.isAssignableFrom(cls)) {
            return null;
        } else {
            if (ConcurrentNavigableMap.class.isAssignableFrom(cls)) {
                return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.14
                    /* JADX WARN: Type inference failed for: r0v0, types: [java.util.concurrent.ConcurrentSkipListMap, T] */
                    @Override // com.google.gson.internal.ObjectConstructor
                    public T construct() {
                        return new ConcurrentSkipListMap();
                    }
                };
            }
            if (ConcurrentMap.class.isAssignableFrom(cls)) {
                return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.15
                    /* JADX WARN: Type inference failed for: r0v0, types: [T, j$.util.concurrent.ConcurrentHashMap] */
                    @Override // com.google.gson.internal.ObjectConstructor
                    public T construct() {
                        return new ConcurrentHashMap();
                    }
                };
            }
            if (SortedMap.class.isAssignableFrom(cls)) {
                return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.16
                    /* JADX WARN: Type inference failed for: r0v0, types: [java.util.TreeMap, T] */
                    @Override // com.google.gson.internal.ObjectConstructor
                    public T construct() {
                        return new TreeMap();
                    }
                };
            }
            if ((type instanceof ParameterizedType) && !String.class.isAssignableFrom(TypeToken.get(((ParameterizedType) type).getActualTypeArguments()[0]).getRawType())) {
                return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.17
                    /* JADX WARN: Type inference failed for: r0v0, types: [java.util.LinkedHashMap, T] */
                    @Override // com.google.gson.internal.ObjectConstructor
                    public T construct() {
                        return new LinkedHashMap();
                    }
                };
            }
            return new ObjectConstructor<T>() { // from class: com.google.gson.internal.ConstructorConstructor.18
                /* JADX WARN: Type inference failed for: r0v0, types: [T, com.google.gson.internal.LinkedTreeMap] */
                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return new LinkedTreeMap();
                }
            };
        }
    }

    private <T> ObjectConstructor<T> newUnsafeAllocator(final Class<? super T> cls) {
        if (this.useJdkUnsafe) {
            return new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.19
                /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    try {
                        return UnsafeAllocator.INSTANCE.newInstance(cls);
                    } catch (Exception e) {
                        throw new RuntimeException("Unable to create instance of " + cls + ". Registering an InstanceCreator or a TypeAdapter for this type, or adding a no-args constructor may fix this problem.", e);
                    }
                }
            };
        }
        final String str = "Unable to create instance of " + cls + "; usage of JDK Unsafe is disabled. Registering an InstanceCreator or a TypeAdapter for this type, adding a no-args constructor, or enabling usage of JDK Unsafe may fix this problem.";
        return new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.20
            @Override // com.google.gson.internal.ObjectConstructor
            public T construct() {
                throw new JsonIOException(str);
            }
        };
    }

    public String toString() {
        return this.instanceCreators.toString();
    }
}
