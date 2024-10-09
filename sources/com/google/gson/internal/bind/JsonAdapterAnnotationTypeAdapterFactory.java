package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes.dex */
public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {
    private static final TypeAdapterFactory TREE_TYPE_CLASS_DUMMY_FACTORY;
    private static final TypeAdapterFactory TREE_TYPE_FIELD_DUMMY_FACTORY;
    private final ConcurrentMap adapterFactoryMap = new ConcurrentHashMap();
    private final ConstructorConstructor constructorConstructor;

    /* loaded from: classes.dex */
    private static class DummyTypeAdapterFactory implements TypeAdapterFactory {
        private DummyTypeAdapterFactory() {
        }

        @Override // com.google.gson.TypeAdapterFactory
        public TypeAdapter create(Gson gson, TypeToken typeToken) {
            throw new AssertionError("Factory should not be used");
        }
    }

    static {
        TREE_TYPE_CLASS_DUMMY_FACTORY = new DummyTypeAdapterFactory();
        TREE_TYPE_FIELD_DUMMY_FACTORY = new DummyTypeAdapterFactory();
    }

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    private static Object createAdapter(ConstructorConstructor constructorConstructor, Class cls) {
        return constructorConstructor.get(TypeToken.get(cls)).construct();
    }

    private static JsonAdapter getAnnotation(Class cls) {
        return (JsonAdapter) cls.getAnnotation(JsonAdapter.class);
    }

    private TypeAdapterFactory putFactoryAndGetCurrent(Class cls, TypeAdapterFactory typeAdapterFactory) {
        TypeAdapterFactory typeAdapterFactory2 = (TypeAdapterFactory) this.adapterFactoryMap.putIfAbsent(cls, typeAdapterFactory);
        return typeAdapterFactory2 != null ? typeAdapterFactory2 : typeAdapterFactory;
    }

    @Override // com.google.gson.TypeAdapterFactory
    public TypeAdapter create(Gson gson, TypeToken typeToken) {
        JsonAdapter annotation = getAnnotation(typeToken.getRawType());
        if (annotation == null) {
            return null;
        }
        return getTypeAdapter(this.constructorConstructor, gson, typeToken, annotation, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TypeAdapter getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken typeToken, JsonAdapter jsonAdapter, boolean z) {
        TypeAdapter treeTypeAdapter;
        Object createAdapter = createAdapter(constructorConstructor, jsonAdapter.value());
        boolean nullSafe = jsonAdapter.nullSafe();
        if (createAdapter instanceof TypeAdapter) {
            treeTypeAdapter = (TypeAdapter) createAdapter;
        } else if (createAdapter instanceof TypeAdapterFactory) {
            TypeAdapterFactory typeAdapterFactory = (TypeAdapterFactory) createAdapter;
            if (z) {
                typeAdapterFactory = putFactoryAndGetCurrent(typeToken.getRawType(), typeAdapterFactory);
            }
            treeTypeAdapter = typeAdapterFactory.create(gson, typeToken);
        } else {
            boolean z2 = createAdapter instanceof JsonSerializer;
            if (!z2) {
                throw new IllegalArgumentException("Invalid attempt to bind an instance of " + createAdapter.getClass().getName() + " as a @JsonAdapter for " + typeToken.toString() + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer.");
            }
            treeTypeAdapter = new TreeTypeAdapter(z2 ? (JsonSerializer) createAdapter : null, null, gson, typeToken, z ? TREE_TYPE_CLASS_DUMMY_FACTORY : TREE_TYPE_FIELD_DUMMY_FACTORY, nullSafe);
            nullSafe = false;
        }
        return (treeTypeAdapter == null || !nullSafe) ? treeTypeAdapter : treeTypeAdapter.nullSafe();
    }

    public boolean isClassJsonAdapterFactory(TypeToken typeToken, TypeAdapterFactory typeAdapterFactory) {
        Objects.requireNonNull(typeToken);
        Objects.requireNonNull(typeAdapterFactory);
        if (typeAdapterFactory == TREE_TYPE_CLASS_DUMMY_FACTORY) {
            return true;
        }
        Class rawType = typeToken.getRawType();
        TypeAdapterFactory typeAdapterFactory2 = (TypeAdapterFactory) this.adapterFactoryMap.get(rawType);
        if (typeAdapterFactory2 != null) {
            return typeAdapterFactory2 == typeAdapterFactory;
        }
        JsonAdapter annotation = getAnnotation(rawType);
        if (annotation == null) {
            return false;
        }
        Class value = annotation.value();
        return TypeAdapterFactory.class.isAssignableFrom(value) && putFactoryAndGetCurrent(rawType, (TypeAdapterFactory) createAdapter(this.constructorConstructor, value)) == typeAdapterFactory;
    }
}
