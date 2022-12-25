package org.aspectj.runtime.reflect;

import java.lang.ref.SoftReference;
import java.util.StringTokenizer;
import org.aspectj.lang.Signature;
/* loaded from: classes.dex */
abstract class SignatureImpl implements Signature {
    private static boolean useCache = true;
    Class declaringType;
    String declaringTypeName;
    ClassLoader lookupClassLoader = null;
    int modifiers;
    String name;
    Cache stringCache;
    private String stringRep;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface Cache {
        String get(int i);

        void set(int i, String str);
    }

    protected abstract String createToString(StringMaker stringMaker);

    /* JADX INFO: Access modifiers changed from: package-private */
    public SignatureImpl(int i, String str, Class cls) {
        this.modifiers = -1;
        this.modifiers = i;
        this.name = str;
        this.declaringType = cls;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x001e  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String toString(StringMaker stringMaker) {
        String str;
        if (useCache) {
            Cache cache = this.stringCache;
            if (cache == null) {
                try {
                    this.stringCache = new CacheImpl();
                } catch (Throwable unused) {
                    useCache = false;
                }
            } else {
                str = cache.get(stringMaker.cacheOffset);
                if (str == null) {
                    str = createToString(stringMaker);
                }
                if (useCache) {
                    this.stringCache.set(stringMaker.cacheOffset, str);
                }
                return str;
            }
        }
        str = null;
        if (str == null) {
        }
        if (useCache) {
        }
        return str;
    }

    public final String toString() {
        return toString(StringMaker.middleStringMaker);
    }

    public int getModifiers() {
        if (this.modifiers == -1) {
            this.modifiers = extractInt(0);
        }
        return this.modifiers;
    }

    public String getName() {
        if (this.name == null) {
            this.name = extractString(1);
        }
        return this.name;
    }

    public Class getDeclaringType() {
        if (this.declaringType == null) {
            this.declaringType = extractType(2);
        }
        return this.declaringType;
    }

    public String getDeclaringTypeName() {
        if (this.declaringTypeName == null) {
            this.declaringTypeName = getDeclaringType().getName();
        }
        return this.declaringTypeName;
    }

    private ClassLoader getLookupClassLoader() {
        if (this.lookupClassLoader == null) {
            this.lookupClassLoader = getClass().getClassLoader();
        }
        return this.lookupClassLoader;
    }

    String extractString(int i) {
        int indexOf = this.stringRep.indexOf(45);
        int i2 = 0;
        while (true) {
            int i3 = i - 1;
            if (i <= 0) {
                break;
            }
            i2 = indexOf + 1;
            indexOf = this.stringRep.indexOf(45, i2);
            i = i3;
        }
        if (indexOf == -1) {
            indexOf = this.stringRep.length();
        }
        return this.stringRep.substring(i2, indexOf);
    }

    int extractInt(int i) {
        return Integer.parseInt(extractString(i), 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Class extractType(int i) {
        return Factory.makeClass(extractString(i), getLookupClassLoader());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Class[] extractTypes(int i) {
        StringTokenizer stringTokenizer = new StringTokenizer(extractString(i), ":");
        int countTokens = stringTokenizer.countTokens();
        Class[] clsArr = new Class[countTokens];
        for (int i2 = 0; i2 < countTokens; i2++) {
            clsArr[i2] = Factory.makeClass(stringTokenizer.nextToken(), getLookupClassLoader());
        }
        return clsArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class CacheImpl implements Cache {
        private SoftReference toStringCacheRef;

        public CacheImpl() {
            makeCache();
        }

        @Override // org.aspectj.runtime.reflect.SignatureImpl.Cache
        public String get(int i) {
            String[] array = array();
            if (array == null) {
                return null;
            }
            return array[i];
        }

        @Override // org.aspectj.runtime.reflect.SignatureImpl.Cache
        public void set(int i, String str) {
            String[] array = array();
            if (array == null) {
                array = makeCache();
            }
            array[i] = str;
        }

        private String[] array() {
            return (String[]) this.toStringCacheRef.get();
        }

        private String[] makeCache() {
            String[] strArr = new String[3];
            this.toStringCacheRef = new SoftReference(strArr);
            return strArr;
        }
    }
}
