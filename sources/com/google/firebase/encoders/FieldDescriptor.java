package com.google.firebase.encoders;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class FieldDescriptor {
    private final String name;
    private final Map properties;

    public static final class Builder {
        private final String name;
        private Map properties = null;

        Builder(String str) {
            this.name = str;
        }

        public FieldDescriptor build() {
            return new FieldDescriptor(this.name, this.properties == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap(this.properties)));
        }

        public Builder withProperty(Annotation annotation) {
            if (this.properties == null) {
                this.properties = new HashMap();
            }
            this.properties.put(annotation.annotationType(), annotation);
            return this;
        }
    }

    private FieldDescriptor(String str, Map map) {
        this.name = str;
        this.properties = map;
    }

    public static Builder builder(String str) {
        return new Builder(str);
    }

    public static FieldDescriptor of(String str) {
        return new FieldDescriptor(str, Collections.emptyMap());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FieldDescriptor)) {
            return false;
        }
        FieldDescriptor fieldDescriptor = (FieldDescriptor) obj;
        return this.name.equals(fieldDescriptor.name) && this.properties.equals(fieldDescriptor.properties);
    }

    public String getName() {
        return this.name;
    }

    public Annotation getProperty(Class cls) {
        return (Annotation) this.properties.get(cls);
    }

    public int hashCode() {
        return (this.name.hashCode() * 31) + this.properties.hashCode();
    }

    public String toString() {
        return "FieldDescriptor{name=" + this.name + ", properties=" + this.properties.values() + "}";
    }
}
