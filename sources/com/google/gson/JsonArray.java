package com.google.gson;

import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class JsonArray extends JsonElement implements Iterable {
    private final ArrayList elements = new ArrayList();

    private JsonElement getAsSingleElement() {
        int size = this.elements.size();
        if (size == 1) {
            return (JsonElement) this.elements.get(0);
        }
        throw new IllegalStateException("Array must have size 1, but has size " + size);
    }

    public void add(JsonElement jsonElement) {
        if (jsonElement == null) {
            jsonElement = JsonNull.INSTANCE;
        }
        this.elements.add(jsonElement);
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof JsonArray) && ((JsonArray) obj).elements.equals(this.elements));
    }

    @Override // com.google.gson.JsonElement
    public String getAsString() {
        return getAsSingleElement().getAsString();
    }

    public int hashCode() {
        return this.elements.hashCode();
    }

    @Override // java.lang.Iterable
    public Iterator iterator() {
        return this.elements.iterator();
    }
}
