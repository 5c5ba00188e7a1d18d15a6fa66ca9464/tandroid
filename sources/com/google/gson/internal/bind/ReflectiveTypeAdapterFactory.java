package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter$FilterResult;
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
import com.google.gson.internal.TroubleshootingGuide;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final List reflectionFilters;

    /* loaded from: classes.dex */
    public static abstract class Adapter extends TypeAdapter {
        private final FieldsData fieldsData;

        Adapter(FieldsData fieldsData) {
            this.fieldsData = fieldsData;
        }

        abstract Object createAccumulator();

        abstract Object finalize(Object obj);

        @Override // com.google.gson.TypeAdapter
        public Object read(JsonReader jsonReader) {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            Object createAccumulator = createAccumulator();
            Map map = this.fieldsData.deserializedFields;
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    BoundField boundField = (BoundField) map.get(jsonReader.nextName());
                    if (boundField == null) {
                        jsonReader.skipValue();
                    } else {
                        readField(createAccumulator, jsonReader, boundField);
                    }
                }
                jsonReader.endObject();
                return finalize(createAccumulator);
            } catch (IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            } catch (IllegalStateException e2) {
                throw new JsonSyntaxException(e2);
            }
        }

        abstract void readField(Object obj, JsonReader jsonReader, BoundField boundField);

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter jsonWriter, Object obj) {
            if (obj == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            try {
                for (BoundField boundField : this.fieldsData.serializedFields) {
                    boundField.write(jsonWriter, obj);
                }
                jsonWriter.endObject();
            } catch (IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class BoundField {
        final Field field;
        final String fieldName;
        final String serializedName;

        protected BoundField(String str, Field field) {
            this.serializedName = str;
            this.field = field;
            this.fieldName = field.getName();
        }

        abstract void readIntoArray(JsonReader jsonReader, int i, Object[] objArr);

        abstract void readIntoField(JsonReader jsonReader, Object obj);

        abstract void write(JsonWriter jsonWriter, Object obj);
    }

    /* loaded from: classes.dex */
    private static final class FieldReflectionAdapter extends Adapter {
        private final ObjectConstructor constructor;

        FieldReflectionAdapter(ObjectConstructor objectConstructor, FieldsData fieldsData) {
            super(fieldsData);
            this.constructor = objectConstructor;
        }

        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        Object createAccumulator() {
            return this.constructor.construct();
        }

        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        Object finalize(Object obj) {
            return obj;
        }

        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        void readField(Object obj, JsonReader jsonReader, BoundField boundField) {
            boundField.readIntoField(jsonReader, obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FieldsData {
        public static final FieldsData EMPTY = new FieldsData(Collections.emptyMap(), Collections.emptyList());
        public final Map deserializedFields;
        public final List serializedFields;

        public FieldsData(Map map, List list) {
            this.deserializedFields = map;
            this.serializedFields = list;
        }
    }

    /* loaded from: classes.dex */
    private static final class RecordAdapter extends Adapter {
        static final Map PRIMITIVE_DEFAULTS = primitiveDefaults();
        private final Map componentIndices;
        private final Constructor constructor;
        private final Object[] constructorArgsDefaults;

        RecordAdapter(Class cls, FieldsData fieldsData, boolean z) {
            super(fieldsData);
            this.componentIndices = new HashMap();
            Constructor canonicalRecordConstructor = ReflectionHelper.getCanonicalRecordConstructor(cls);
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

        private static Map primitiveDefaults() {
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

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        public Object[] createAccumulator() {
            return (Object[]) this.constructorArgsDefaults.clone();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        public Object finalize(Object[] objArr) {
            try {
                return this.constructor.newInstance(objArr);
            } catch (IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            } catch (IllegalArgumentException e2) {
                e = e2;
                throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' with args " + Arrays.toString(objArr), e);
            } catch (InstantiationException e3) {
                e = e3;
                throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' with args " + Arrays.toString(objArr), e);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' with args " + Arrays.toString(objArr), e4.getCause());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        public void readField(Object[] objArr, JsonReader jsonReader, BoundField boundField) {
            Integer num = (Integer) this.componentIndices.get(boundField.fieldName);
            if (num != null) {
                boundField.readIntoArray(jsonReader, num.intValue(), objArr);
                return;
            }
            throw new IllegalStateException("Could not find the index in the constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' for field with name '" + boundField.fieldName + "', unable to determine which argument in the constructor the field corresponds to. This is unexpected behavior, as we expect the RecordComponents to have the same names as the fields in the Java class, and that the order of the RecordComponents is the same as the order of the canonical constructor parameters.");
        }
    }

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingStrategy, Excluder excluder, JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory, List list) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingStrategy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterAnnotationTypeAdapterFactory;
        this.reflectionFilters = list;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkAccessible(Object obj, AccessibleObject accessibleObject) {
        if (Modifier.isStatic(((Member) accessibleObject).getModifiers())) {
            obj = null;
        }
        if (ReflectionAccessFilterHelper.canAccess(accessibleObject, obj)) {
            return;
        }
        String accessibleObjectDescription = ReflectionHelper.getAccessibleObjectDescription(accessibleObject, true);
        throw new JsonIOException(accessibleObjectDescription + " is not accessible and ReflectionAccessFilter does not permit making it accessible. Register a TypeAdapter for the declaring type, adjust the access filter or increase the visibility of the element and its declaring type.");
    }

    private BoundField createBoundField(Gson gson, Field field, final Method method, String str, TypeToken typeToken, boolean z, final boolean z2) {
        final TypeAdapter typeAdapter;
        final boolean isPrimitive = Primitives.isPrimitive(typeToken.getRawType());
        int modifiers = field.getModifiers();
        boolean z3 = Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
        JsonAdapter jsonAdapter = (JsonAdapter) field.getAnnotation(JsonAdapter.class);
        TypeAdapter typeAdapter2 = jsonAdapter != null ? this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter, false) : null;
        boolean z4 = typeAdapter2 != null;
        if (typeAdapter2 == null) {
            typeAdapter2 = gson.getAdapter(typeToken);
        }
        final TypeAdapter typeAdapter3 = typeAdapter2;
        if (z) {
            typeAdapter = z4 ? typeAdapter3 : new TypeAdapterRuntimeTypeWrapper(gson, typeAdapter3, typeToken.getType());
        } else {
            typeAdapter = typeAdapter3;
        }
        final boolean z5 = z3;
        return new BoundField(str, field) { // from class: com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.2
            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            void readIntoArray(JsonReader jsonReader, int i, Object[] objArr) {
                Object read = typeAdapter3.read(jsonReader);
                if (read != null || !isPrimitive) {
                    objArr[i] = read;
                    return;
                }
                throw new JsonParseException("null is not allowed as value for record component '" + this.fieldName + "' of primitive type; at path " + jsonReader.getPath());
            }

            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            void readIntoField(JsonReader jsonReader, Object obj) {
                Object read = typeAdapter3.read(jsonReader);
                if (read == null && isPrimitive) {
                    return;
                }
                if (z2) {
                    ReflectiveTypeAdapterFactory.checkAccessible(obj, this.field);
                } else if (z5) {
                    String accessibleObjectDescription = ReflectionHelper.getAccessibleObjectDescription(this.field, false);
                    throw new JsonIOException("Cannot set value of 'static final' " + accessibleObjectDescription);
                }
                this.field.set(obj, read);
            }

            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            void write(JsonWriter jsonWriter, Object obj) {
                Object obj2;
                if (z2) {
                    AccessibleObject accessibleObject = method;
                    if (accessibleObject == null) {
                        accessibleObject = this.field;
                    }
                    ReflectiveTypeAdapterFactory.checkAccessible(obj, accessibleObject);
                }
                Method method2 = method;
                if (method2 != null) {
                    try {
                        obj2 = method2.invoke(obj, null);
                    } catch (InvocationTargetException e) {
                        String accessibleObjectDescription = ReflectionHelper.getAccessibleObjectDescription(method, false);
                        throw new JsonIOException("Accessor " + accessibleObjectDescription + " threw exception", e.getCause());
                    }
                } else {
                    obj2 = this.field.get(obj);
                }
                if (obj2 == obj) {
                    return;
                }
                jsonWriter.name(this.serializedName);
                typeAdapter.write(jsonWriter, obj2);
            }
        };
    }

    private static IllegalArgumentException createDuplicateFieldException(Class cls, String str, Field field, Field field2) {
        throw new IllegalArgumentException("Class " + cls.getName() + " declares multiple JSON fields named '" + str + "'; conflict is caused by fields " + ReflectionHelper.fieldToString(field) + " and " + ReflectionHelper.fieldToString(field2) + "\nSee " + TroubleshootingGuide.createUrl("duplicate-fields"));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0108  */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r7v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private FieldsData getBoundFields(Gson gson, TypeToken typeToken, Class cls, boolean z, boolean z2) {
        boolean z3;
        Method method;
        String str;
        int i;
        int i2;
        BoundField boundField;
        if (cls.isInterface()) {
            return FieldsData.EMPTY;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        TypeToken typeToken2 = typeToken;
        boolean z4 = z;
        Class cls2 = cls;
        while (cls2 != Object.class) {
            Field[] declaredFields = cls2.getDeclaredFields();
            boolean z5 = true;
            ?? r7 = 0;
            if (cls2 != cls && declaredFields.length > 0) {
                ReflectionAccessFilter$FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, cls2);
                if (filterResult == ReflectionAccessFilter$FilterResult.BLOCK_ALL) {
                    throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + cls2 + " (supertype of " + cls + "). Register a TypeAdapter for this type or adjust the access filter.");
                }
                z4 = filterResult == ReflectionAccessFilter$FilterResult.BLOCK_INACCESSIBLE;
            }
            boolean z6 = z4;
            int length = declaredFields.length;
            int i3 = 0;
            while (i3 < length) {
                Field field = declaredFields[i3];
                boolean includeField = includeField(field, z5);
                boolean includeField2 = includeField(field, r7);
                if (includeField || includeField2) {
                    Method method2 = null;
                    if (z2) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            method = null;
                            z3 = false;
                            if (!z6 && method == null) {
                                ReflectionHelper.makeAccessible(field);
                            }
                            Type resolve = $Gson$Types.resolve(typeToken2.getType(), cls2, field.getGenericType());
                            List<String> fieldNames = getFieldNames(field);
                            str = (String) fieldNames.get(r7);
                            i = i3;
                            i2 = length;
                            BoundField createBoundField = createBoundField(gson, field, method, str, TypeToken.get(resolve), includeField, z6);
                            if (z3) {
                                for (String str2 : fieldNames) {
                                    BoundField boundField2 = (BoundField) linkedHashMap.put(str2, createBoundField);
                                    if (boundField2 != null) {
                                        throw createDuplicateFieldException(cls, str2, boundField2.field, field);
                                    }
                                }
                            }
                            if (includeField && (boundField = (BoundField) linkedHashMap2.put(str, createBoundField)) != null) {
                                throw createDuplicateFieldException(cls, str, boundField.field, field);
                            }
                        } else {
                            method2 = ReflectionHelper.getAccessor(cls2, field);
                            if (!z6) {
                                ReflectionHelper.makeAccessible(method2);
                            }
                            if (method2.getAnnotation(SerializedName.class) != null && field.getAnnotation(SerializedName.class) == null) {
                                throw new JsonIOException("@SerializedName on " + ReflectionHelper.getAccessibleObjectDescription(method2, r7) + " is not supported");
                            }
                        }
                    }
                    z3 = includeField2;
                    method = method2;
                    if (!z6) {
                        ReflectionHelper.makeAccessible(field);
                    }
                    Type resolve2 = $Gson$Types.resolve(typeToken2.getType(), cls2, field.getGenericType());
                    List<String> fieldNames2 = getFieldNames(field);
                    str = (String) fieldNames2.get(r7);
                    i = i3;
                    i2 = length;
                    BoundField createBoundField2 = createBoundField(gson, field, method, str, TypeToken.get(resolve2), includeField, z6);
                    if (z3) {
                    }
                    if (includeField) {
                        throw createDuplicateFieldException(cls, str, boundField.field, field);
                    }
                    continue;
                } else {
                    i = i3;
                    i2 = length;
                }
                i3 = i + 1;
                length = i2;
                r7 = 0;
                z5 = true;
            }
            typeToken2 = TypeToken.get($Gson$Types.resolve(typeToken2.getType(), cls2, cls2.getGenericSuperclass()));
            cls2 = typeToken2.getRawType();
            z4 = z6;
        }
        return new FieldsData(linkedHashMap, new ArrayList(linkedHashMap2.values()));
    }

    private List getFieldNames(Field field) {
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

    private boolean includeField(Field field, boolean z) {
        return !this.excluder.excludeField(field, z);
    }

    @Override // com.google.gson.TypeAdapterFactory
    public TypeAdapter create(Gson gson, TypeToken typeToken) {
        Class rawType = typeToken.getRawType();
        if (Object.class.isAssignableFrom(rawType)) {
            if (ReflectionHelper.isAnonymousOrNonStaticLocal(rawType)) {
                return new TypeAdapter() { // from class: com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.1
                    @Override // com.google.gson.TypeAdapter
                    public Object read(JsonReader jsonReader) {
                        jsonReader.skipValue();
                        return null;
                    }

                    public String toString() {
                        return "AnonymousOrNonStaticLocalClassAdapter";
                    }

                    @Override // com.google.gson.TypeAdapter
                    public void write(JsonWriter jsonWriter, Object obj) {
                        jsonWriter.nullValue();
                    }
                };
            }
            ReflectionAccessFilter$FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, rawType);
            if (filterResult != ReflectionAccessFilter$FilterResult.BLOCK_ALL) {
                boolean z = filterResult == ReflectionAccessFilter$FilterResult.BLOCK_INACCESSIBLE;
                return ReflectionHelper.isRecord(rawType) ? new RecordAdapter(rawType, getBoundFields(gson, typeToken, rawType, z, true), z) : new FieldReflectionAdapter(this.constructorConstructor.get(typeToken), getBoundFields(gson, typeToken, rawType, z, false));
            }
            throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + rawType + ". Register a TypeAdapter for this type or adjust the access filter.");
        }
        return null;
    }
}
