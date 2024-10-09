package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class ArrayTypeAdapter extends TypeAdapter {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.ArrayTypeAdapter.1
        @Override // com.google.gson.TypeAdapterFactory
        public TypeAdapter create(Gson gson, TypeToken typeToken) {
            Type type = typeToken.getType();
            if (!(type instanceof GenericArrayType) && (!(type instanceof Class) || !((Class) type).isArray())) {
                return null;
            }
            Type arrayComponentType = $Gson$Types.getArrayComponentType(type);
            return new ArrayTypeAdapter(gson, gson.getAdapter(TypeToken.get(arrayComponentType)), $Gson$Types.getRawType(arrayComponentType));
        }
    };
    private final Class componentType;
    private final TypeAdapter componentTypeAdapter;

    public ArrayTypeAdapter(Gson gson, TypeAdapter typeAdapter, Class cls) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper(gson, typeAdapter, cls);
        this.componentType = cls;
    }

    @Override // com.google.gson.TypeAdapter
    public Object read(JsonReader jsonReader) {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        ArrayList arrayList = new ArrayList();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            arrayList.add(this.componentTypeAdapter.read(jsonReader));
        }
        jsonReader.endArray();
        int size = arrayList.size();
        if (!this.componentType.isPrimitive()) {
            return arrayList.toArray((Object[]) Array.newInstance((Class<?>) this.componentType, size));
        }
        Object newInstance = Array.newInstance((Class<?>) this.componentType, size);
        for (int i = 0; i < size; i++) {
            Array.set(newInstance, i, arrayList.get(i));
        }
        return newInstance;
    }

    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter jsonWriter, Object obj) {
        if (obj == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginArray();
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            this.componentTypeAdapter.write(jsonWriter, Array.get(obj, i));
        }
        jsonWriter.endArray();
    }
}
