package com.google.gson;

import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
/* loaded from: classes.dex */
public abstract class TypeAdapter<T> {
    public abstract void write(JsonWriter jsonWriter, T t) throws IOException;

    public final TypeAdapter<T> nullSafe() {
        return new TypeAdapter<T>() { // from class: com.google.gson.TypeAdapter.1
            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, T t) throws IOException {
                if (t == null) {
                    jsonWriter.nullValue();
                } else {
                    TypeAdapter.this.write(jsonWriter, t);
                }
            }
        };
    }

    public final JsonElement toJsonTree(T t) {
        try {
            JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
            write(jsonTreeWriter, t);
            return jsonTreeWriter.get();
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }
}
