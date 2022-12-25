package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    private final List<ReflectionAccessFilter> reflectionFilters;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingStrategy, Excluder excluder, JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory, List<ReflectionAccessFilter> list) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingStrategy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterAnnotationTypeAdapterFactory;
        this.reflectionFilters = list;
    }

    private boolean includeField(Field field, boolean z) {
        return (this.excluder.excludeClass(field.getType(), z) || this.excluder.excludeField(field, z)) ? false : true;
    }

    private List<String> getFieldNames(Field field) {
        SerializedName serializedName = (SerializedName) field.getAnnotation(SerializedName.class);
        if (serializedName == null) {
            return Collections.singletonList(this.fieldNamingPolicy.translateName(field));
        }
        String value = serializedName.value();
        String[] alternate = serializedName.alternate();
        if (alternate.length == 0) {
            return Collections.singletonList(value);
        }
        ArrayList arrayList = new ArrayList(alternate.length + 1);
        arrayList.add(value);
        Collections.addAll(arrayList, alternate);
        return arrayList;
    }

    @Override // com.google.gson.TypeAdapterFactory
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        if (Object.class.isAssignableFrom(rawType)) {
            ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, rawType);
            if (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
                throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + rawType + ". Register a TypeAdapter for this type or adjust the access filter.");
            }
            boolean z = filterResult == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE;
            if (ReflectionHelper.isRecord(rawType)) {
                return new RecordAdapter(rawType, getBoundFields(gson, typeToken, rawType, z, true), z);
            }
            return new FieldReflectionAdapter(this.constructorConstructor.get(typeToken), getBoundFields(gson, typeToken, rawType, z, false));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <M extends AccessibleObject & Member> void checkAccessible(Object obj, M m) {
        if (Modifier.isStatic(m.getModifiers())) {
            obj = null;
        }
        if (ReflectionAccessFilterHelper.canAccess(m, obj)) {
            return;
        }
        String accessibleObjectDescription = ReflectionHelper.getAccessibleObjectDescription(m, true);
        throw new JsonIOException(accessibleObjectDescription + " is not accessible and ReflectionAccessFilter does not permit making it accessible. Register a TypeAdapter for the declaring type, adjust the access filter or increase the visibility of the element and its declaring type.");
    }

    private BoundField createBoundField(Gson gson, Field field, Method method, String str, TypeToken<?> typeToken, boolean z, boolean z2, boolean z3) {
        boolean isPrimitive = Primitives.isPrimitive(typeToken.getRawType());
        int modifiers = field.getModifiers();
        boolean z4 = Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
        JsonAdapter jsonAdapter = (JsonAdapter) field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = jsonAdapter != null ? this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter) : null;
        return new BoundField(this, str, field.getName(), z, z2, z3, method, field, typeAdapter != null, typeAdapter == null ? gson.getAdapter(typeToken) : typeAdapter, gson, typeToken, isPrimitive, z4) { // from class: com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.1
            final /* synthetic */ Method val$accessor;
            final /* synthetic */ boolean val$blockInaccessible;
            final /* synthetic */ Gson val$context;
            final /* synthetic */ Field val$field;
            final /* synthetic */ TypeToken val$fieldType;
            final /* synthetic */ boolean val$jsonAdapterPresent;
            final /* synthetic */ TypeAdapter val$typeAdapter;

            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            void write(JsonWriter jsonWriter, Object obj) throws IOException, IllegalAccessException {
                Object obj2;
                if (this.serialized) {
                    if (this.val$blockInaccessible) {
                        Method method2 = this.val$accessor;
                        if (method2 == null) {
                            ReflectiveTypeAdapterFactory.checkAccessible(obj, this.val$field);
                        } else {
                            ReflectiveTypeAdapterFactory.checkAccessible(obj, method2);
                        }
                    }
                    Method method3 = this.val$accessor;
                    if (method3 != null) {
                        try {
                            obj2 = method3.invoke(obj, new Object[0]);
                        } catch (InvocationTargetException e) {
                            String accessibleObjectDescription = ReflectionHelper.getAccessibleObjectDescription(this.val$accessor, false);
                            throw new JsonIOException("Accessor " + accessibleObjectDescription + " threw exception", e.getCause());
                        }
                    } else {
                        obj2 = this.val$field.get(obj);
                    }
                    if (obj2 == obj) {
                        return;
                    }
                    jsonWriter.name(this.name);
                    (this.val$jsonAdapterPresent ? this.val$typeAdapter : new TypeAdapterRuntimeTypeWrapper(this.val$context, this.val$typeAdapter, this.val$fieldType.getType())).write(jsonWriter, obj2);
                }
            }
        };
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0162 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0154 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Map<String, BoundField> getBoundFields(Gson gson, TypeToken<?> typeToken, Class<?> cls, boolean z, boolean z2) {
        boolean z3;
        Method method;
        int size;
        int i;
        int i2;
        int i3;
        Field[] fieldArr;
        BoundField boundField;
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = this;
        Class<?> cls2 = cls;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (cls.isInterface()) {
            return linkedHashMap;
        }
        Type type = typeToken.getType();
        TypeToken<?> typeToken2 = typeToken;
        boolean z4 = z;
        Class<?> cls3 = cls2;
        while (cls3 != Object.class) {
            Field[] declaredFields = cls3.getDeclaredFields();
            boolean z5 = true;
            boolean z6 = false;
            if (cls3 != cls2 && declaredFields.length > 0) {
                ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(reflectiveTypeAdapterFactory.reflectionFilters, cls3);
                if (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
                    throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + cls3 + " (supertype of " + cls2 + "). Register a TypeAdapter for this type or adjust the access filter.");
                }
                z4 = filterResult == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE;
            }
            boolean z7 = z4;
            int length = declaredFields.length;
            int i4 = 0;
            while (i4 < length) {
                Field field = declaredFields[i4];
                boolean includeField = reflectiveTypeAdapterFactory.includeField(field, z5);
                boolean includeField2 = reflectiveTypeAdapterFactory.includeField(field, z6);
                if (includeField || includeField2) {
                    if (!z2) {
                        z3 = includeField2;
                    } else if (Modifier.isStatic(field.getModifiers())) {
                        z3 = false;
                    } else {
                        Method accessor = ReflectionHelper.getAccessor(cls3, field);
                        if (!z7) {
                            ReflectionHelper.makeAccessible(accessor);
                        }
                        if (accessor.getAnnotation(SerializedName.class) != null && field.getAnnotation(SerializedName.class) == null) {
                            String accessibleObjectDescription = ReflectionHelper.getAccessibleObjectDescription(accessor, z6);
                            throw new JsonIOException("@SerializedName on " + accessibleObjectDescription + " is not supported");
                        }
                        z3 = includeField2;
                        method = accessor;
                        if (!z7 && method == null) {
                            ReflectionHelper.makeAccessible(field);
                        }
                        Type resolve = $Gson$Types.resolve(typeToken2.getType(), cls3, field.getGenericType());
                        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
                        size = fieldNames.size();
                        BoundField boundField2 = null;
                        i = 0;
                        while (i < size) {
                            String str = fieldNames.get(i);
                            boolean z8 = i != 0 ? false : includeField;
                            Field[] fieldArr2 = declaredFields;
                            BoundField boundField3 = boundField2;
                            int i5 = size;
                            List<String> list = fieldNames;
                            Field field2 = field;
                            int i6 = i4;
                            int i7 = length;
                            boundField2 = boundField3 == null ? (BoundField) linkedHashMap.put(str, createBoundField(gson, field, method, str, TypeToken.get(resolve), z8, z3, z7)) : boundField3;
                            i++;
                            declaredFields = fieldArr2;
                            includeField = z8;
                            length = i7;
                            size = i5;
                            fieldNames = list;
                            field = field2;
                            i4 = i6;
                        }
                        i2 = i4;
                        i3 = length;
                        fieldArr = declaredFields;
                        boundField = boundField2;
                        if (boundField == null) {
                            throw new IllegalArgumentException(type + " declares multiple JSON fields named " + boundField.name);
                        }
                    }
                    method = null;
                    if (!z7) {
                        ReflectionHelper.makeAccessible(field);
                    }
                    Type resolve2 = $Gson$Types.resolve(typeToken2.getType(), cls3, field.getGenericType());
                    List<String> fieldNames2 = reflectiveTypeAdapterFactory.getFieldNames(field);
                    size = fieldNames2.size();
                    BoundField boundField22 = null;
                    i = 0;
                    while (i < size) {
                    }
                    i2 = i4;
                    i3 = length;
                    fieldArr = declaredFields;
                    boundField = boundField22;
                    if (boundField == null) {
                    }
                } else {
                    i2 = i4;
                    i3 = length;
                    fieldArr = declaredFields;
                }
                i4 = i2 + 1;
                z6 = false;
                z5 = true;
                reflectiveTypeAdapterFactory = this;
                declaredFields = fieldArr;
                length = i3;
            }
            typeToken2 = TypeToken.get($Gson$Types.resolve(typeToken2.getType(), cls3, cls3.getGenericSuperclass()));
            cls3 = typeToken2.getRawType();
            reflectiveTypeAdapterFactory = this;
            cls2 = cls;
            z4 = z7;
        }
        return linkedHashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class BoundField {
        final String name;
        final boolean serialized;

        abstract void write(JsonWriter jsonWriter, Object obj) throws IOException, IllegalAccessException;

        protected BoundField(String str, String str2, boolean z, boolean z2) {
            this.name = str;
            this.serialized = z;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Adapter<T, A> extends TypeAdapter<T> {
        final Map<String, BoundField> boundFields;

        Adapter(Map<String, BoundField> map) {
            this.boundFields = map;
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter jsonWriter, T t) throws IOException {
            if (t == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    boundField.write(jsonWriter, t);
                }
                jsonWriter.endObject();
            } catch (IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            }
        }
    }

    /* loaded from: classes.dex */
    private static final class FieldReflectionAdapter<T> extends Adapter<T, T> {
        FieldReflectionAdapter(ObjectConstructor<T> objectConstructor, Map<String, BoundField> map) {
            super(map);
        }
    }

    /* loaded from: classes.dex */
    private static final class RecordAdapter<T> extends Adapter<T, Object[]> {
        static final Map<Class<?>, Object> PRIMITIVE_DEFAULTS = primitiveDefaults();
        private final Map<String, Integer> componentIndices;
        private final Constructor<T> constructor;
        private final Object[] constructorArgsDefaults;

        RecordAdapter(Class<T> cls, Map<String, BoundField> map, boolean z) {
            super(map);
            this.componentIndices = new HashMap();
            Constructor<T> canonicalRecordConstructor = ReflectionHelper.getCanonicalRecordConstructor(cls);
            this.constructor = canonicalRecordConstructor;
            if (z) {
                ReflectiveTypeAdapterFactory.checkAccessible(null, canonicalRecordConstructor);
            } else {
                ReflectionHelper.makeAccessible(canonicalRecordConstructor);
            }
            String[] recordComponentNames = ReflectionHelper.getRecordComponentNames(cls);
            for (int i = 0; i < recordComponentNames.length; i++) {
                this.componentIndices.put(recordComponentNames[i], Integer.valueOf(i));
            }
            Class<?>[] parameterTypes = this.constructor.getParameterTypes();
            this.constructorArgsDefaults = new Object[parameterTypes.length];
            for (int i2 = 0; i2 < parameterTypes.length; i2++) {
                this.constructorArgsDefaults[i2] = PRIMITIVE_DEFAULTS.get(parameterTypes[i2]);
            }
        }

        private static Map<Class<?>, Object> primitiveDefaults() {
            HashMap hashMap = new HashMap();
            hashMap.put(Byte.TYPE, (byte) 0);
            hashMap.put(Short.TYPE, (short) 0);
            hashMap.put(Integer.TYPE, 0);
            hashMap.put(Long.TYPE, 0L);
            hashMap.put(Float.TYPE, Float.valueOf(0.0f));
            hashMap.put(Double.TYPE, Double.valueOf(0.0d));
            hashMap.put(Character.TYPE, (char) 0);
            hashMap.put(Boolean.TYPE, Boolean.FALSE);
            return hashMap;
        }
    }
}
