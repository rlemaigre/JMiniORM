package org.jminiorm.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class CaseInsensitiveMap<T> extends LinkedHashMap<String, T> {

    public CaseInsensitiveMap() {
        super();
    }

    public CaseInsensitiveMap(Map<? extends String, ? extends T> m) {
        this();
        putAll(m);
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> m) {
        for (Map.Entry<? extends String, ? extends T> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public T put(String key, T value) {
        return super.put(key.toLowerCase(), value);
    }

    public T get(String key) {
        return super.get(key.toLowerCase());
    }

}
