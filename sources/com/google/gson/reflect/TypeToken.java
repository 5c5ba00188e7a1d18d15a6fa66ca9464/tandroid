package com.google.gson.reflect;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
/* loaded from: classes.dex */
public class TypeToken<T> {
    private final int hashCode;
    private final Class<? super T> rawType;
    private final Type type;

    protected TypeToken() {
        Type typeTokenTypeArgument = getTypeTokenTypeArgument();
        this.type = typeTokenTypeArgument;
        this.rawType = (Class<? super T>) $Gson$Types.getRawType(typeTokenTypeArgument);
        this.hashCode = typeTokenTypeArgument.hashCode();
    }

    private TypeToken(Type type) {
        Objects.requireNonNull(type);
        Type canonicalize = $Gson$Types.canonicalize(type);
        this.type = canonicalize;
        this.rawType = (Class<? super T>) $Gson$Types.getRawType(canonicalize);
        this.hashCode = canonicalize.hashCode();
    }

    private Type getTypeTokenTypeArgument() {
        Type genericSuperclass = TypeToken.class.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            if (genericSuperclass == TypeToken.class) {
                throw new IllegalStateException("TypeToken must be created with a type argument: new TypeToken<...>() {}; When using code shrinkers (ProGuard, R8, ...) make sure that generic signatures are preserved.");
            }
        } else {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            if (parameterizedType.getRawType() == TypeToken.class) {
                return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
            }
        }
        throw new IllegalStateException("Must only create direct subclasses of TypeToken");
    }

    public final Class<? super T> getRawType() {
        return this.rawType;
    }

    public final Type getType() {
        return this.type;
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object obj) {
        return (obj instanceof TypeToken) && $Gson$Types.equals(this.type, ((TypeToken) obj).type);
    }

    public final String toString() {
        return $Gson$Types.typeToString(this.type);
    }

    public static TypeToken<?> get(Type type) {
        return new TypeToken<>(type);
    }

    public static <T> TypeToken<T> get(Class<T> cls) {
        return new TypeToken<>(cls);
    }
}
