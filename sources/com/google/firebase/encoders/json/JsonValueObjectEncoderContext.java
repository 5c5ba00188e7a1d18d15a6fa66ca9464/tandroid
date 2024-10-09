package com.google.firebase.encoders.json;

import android.util.Base64;
import android.util.JsonWriter;
import com.google.firebase.encoders.EncodingException;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import com.google.firebase.encoders.ValueEncoder;
import com.google.firebase.encoders.ValueEncoderContext;
import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class JsonValueObjectEncoderContext implements ObjectEncoderContext, ValueEncoderContext {
    private final ObjectEncoder fallbackEncoder;
    private final boolean ignoreNullValues;
    private final JsonWriter jsonWriter;
    private final Map objectEncoders;
    private final Map valueEncoders;
    private JsonValueObjectEncoderContext childContext = null;
    private boolean active = true;

    /* JADX INFO: Access modifiers changed from: package-private */
    public JsonValueObjectEncoderContext(Writer writer, Map map, Map map2, ObjectEncoder objectEncoder, boolean z) {
        this.jsonWriter = new JsonWriter(writer);
        this.objectEncoders = map;
        this.valueEncoders = map2;
        this.fallbackEncoder = objectEncoder;
        this.ignoreNullValues = z;
    }

    private boolean cannotBeInline(Object obj) {
        return obj == null || obj.getClass().isArray() || (obj instanceof Collection) || (obj instanceof Date) || (obj instanceof Enum) || (obj instanceof Number);
    }

    private JsonValueObjectEncoderContext internalAdd(String str, Object obj) {
        maybeUnNest();
        this.jsonWriter.name(str);
        if (obj != null) {
            return add(obj, false);
        }
        this.jsonWriter.nullValue();
        return this;
    }

    private JsonValueObjectEncoderContext internalAddIgnoreNullValues(String str, Object obj) {
        if (obj == null) {
            return this;
        }
        maybeUnNest();
        this.jsonWriter.name(str);
        return add(obj, false);
    }

    private void maybeUnNest() {
        if (!this.active) {
            throw new IllegalStateException("Parent context used since this context was created. Cannot use this context anymore.");
        }
        JsonValueObjectEncoderContext jsonValueObjectEncoderContext = this.childContext;
        if (jsonValueObjectEncoderContext != null) {
            jsonValueObjectEncoderContext.maybeUnNest();
            this.childContext.active = false;
            this.childContext = null;
            this.jsonWriter.endObject();
        }
    }

    @Override // com.google.firebase.encoders.ObjectEncoderContext
    public ObjectEncoderContext add(FieldDescriptor fieldDescriptor, int i) {
        return add(fieldDescriptor.getName(), i);
    }

    @Override // com.google.firebase.encoders.ObjectEncoderContext
    public ObjectEncoderContext add(FieldDescriptor fieldDescriptor, long j) {
        return add(fieldDescriptor.getName(), j);
    }

    @Override // com.google.firebase.encoders.ObjectEncoderContext
    public ObjectEncoderContext add(FieldDescriptor fieldDescriptor, Object obj) {
        return add(fieldDescriptor.getName(), obj);
    }

    public JsonValueObjectEncoderContext add(int i) {
        maybeUnNest();
        this.jsonWriter.value(i);
        return this;
    }

    public JsonValueObjectEncoderContext add(long j) {
        maybeUnNest();
        this.jsonWriter.value(j);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JsonValueObjectEncoderContext add(Object obj, boolean z) {
        int i = 0;
        if (z && cannotBeInline(obj)) {
            throw new EncodingException(String.format("%s cannot be encoded inline", obj == null ? null : obj.getClass()));
        }
        if (obj == null) {
            this.jsonWriter.nullValue();
            return this;
        }
        if (obj instanceof Number) {
            this.jsonWriter.value((Number) obj);
            return this;
        }
        if (!obj.getClass().isArray()) {
            if (obj instanceof Collection) {
                this.jsonWriter.beginArray();
                Iterator it = ((Collection) obj).iterator();
                while (it.hasNext()) {
                    add(it.next(), false);
                }
                this.jsonWriter.endArray();
                return this;
            }
            if (obj instanceof Map) {
                this.jsonWriter.beginObject();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    Object key = entry.getKey();
                    try {
                        add((String) key, entry.getValue());
                    } catch (ClassCastException e) {
                        throw new EncodingException(String.format("Only String keys are currently supported in maps, got %s of type %s instead.", key, key.getClass()), e);
                    }
                }
                this.jsonWriter.endObject();
                return this;
            }
            ObjectEncoder objectEncoder = (ObjectEncoder) this.objectEncoders.get(obj.getClass());
            if (objectEncoder != null) {
                return doEncode(objectEncoder, obj, z);
            }
            ValueEncoder valueEncoder = (ValueEncoder) this.valueEncoders.get(obj.getClass());
            if (valueEncoder != null) {
                valueEncoder.encode(obj, this);
                return this;
            }
            if (!(obj instanceof Enum)) {
                return doEncode(this.fallbackEncoder, obj, z);
            }
            add(((Enum) obj).name());
            return this;
        }
        if (obj instanceof byte[]) {
            return add((byte[]) obj);
        }
        this.jsonWriter.beginArray();
        if (obj instanceof int[]) {
            int length = ((int[]) obj).length;
            while (i < length) {
                this.jsonWriter.value(r7[i]);
                i++;
            }
        } else if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            int length2 = jArr.length;
            while (i < length2) {
                add(jArr[i]);
                i++;
            }
        } else if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            int length3 = dArr.length;
            while (i < length3) {
                this.jsonWriter.value(dArr[i]);
                i++;
            }
        } else if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            int length4 = zArr.length;
            while (i < length4) {
                this.jsonWriter.value(zArr[i]);
                i++;
            }
        } else if (obj instanceof Number[]) {
            for (Number number : (Number[]) obj) {
                add((Object) number, false);
            }
        } else {
            for (Object obj2 : (Object[]) obj) {
                add(obj2, false);
            }
        }
        this.jsonWriter.endArray();
        return this;
    }

    @Override // com.google.firebase.encoders.ValueEncoderContext
    public JsonValueObjectEncoderContext add(String str) {
        maybeUnNest();
        this.jsonWriter.value(str);
        return this;
    }

    public JsonValueObjectEncoderContext add(String str, int i) {
        maybeUnNest();
        this.jsonWriter.name(str);
        return add(i);
    }

    public JsonValueObjectEncoderContext add(String str, long j) {
        maybeUnNest();
        this.jsonWriter.name(str);
        return add(j);
    }

    public JsonValueObjectEncoderContext add(String str, Object obj) {
        return this.ignoreNullValues ? internalAddIgnoreNullValues(str, obj) : internalAdd(str, obj);
    }

    @Override // com.google.firebase.encoders.ValueEncoderContext
    public JsonValueObjectEncoderContext add(boolean z) {
        maybeUnNest();
        this.jsonWriter.value(z);
        return this;
    }

    public JsonValueObjectEncoderContext add(byte[] bArr) {
        maybeUnNest();
        if (bArr == null) {
            this.jsonWriter.nullValue();
        } else {
            this.jsonWriter.value(Base64.encodeToString(bArr, 2));
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        maybeUnNest();
        this.jsonWriter.flush();
    }

    JsonValueObjectEncoderContext doEncode(ObjectEncoder objectEncoder, Object obj, boolean z) {
        if (!z) {
            this.jsonWriter.beginObject();
        }
        objectEncoder.encode(obj, this);
        if (!z) {
            this.jsonWriter.endObject();
        }
        return this;
    }
}
