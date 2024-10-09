package androidx.lifecycle;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.lifecycle.Lifecycle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class Lifecycling {
    private static Map sCallbackCache = new HashMap();
    private static Map sClassToAdapters = new HashMap();

    /* loaded from: classes.dex */
    class 1 implements LifecycleEventObserver {
        final /* synthetic */ LifecycleEventObserver val$observer;

        @Override // androidx.lifecycle.LifecycleEventObserver
        public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            this.val$observer.onStateChanged(lifecycleOwner, event);
        }
    }

    private static GeneratedAdapter createGeneratedAdapter(Constructor constructor, Object obj) {
        try {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(constructor.newInstance(obj));
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Constructor generatedConstructor(Class cls) {
        try {
            Package r1 = cls.getPackage();
            String canonicalName = cls.getCanonicalName();
            String name = r1 != null ? r1.getName() : "";
            if (!name.isEmpty()) {
                canonicalName = canonicalName.substring(name.length() + 1);
            }
            String adapterName = getAdapterName(canonicalName);
            if (!name.isEmpty()) {
                adapterName = name + "." + adapterName;
            }
            Constructor declaredConstructor = Class.forName(adapterName).getDeclaredConstructor(cls);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return declaredConstructor;
        } catch (ClassNotFoundException unused) {
            return null;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAdapterName(String str) {
        return str.replace(".", "_") + "_LifecycleAdapter";
    }

    private static int getObserverConstructorType(Class cls) {
        Integer num = (Integer) sCallbackCache.get(cls);
        if (num != null) {
            return num.intValue();
        }
        int resolveObserverCallbackType = resolveObserverCallbackType(cls);
        sCallbackCache.put(cls, Integer.valueOf(resolveObserverCallbackType));
        return resolveObserverCallbackType;
    }

    private static boolean isLifecycleParent(Class cls) {
        return cls != null && LifecycleObserver.class.isAssignableFrom(cls);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LifecycleEventObserver lifecycleEventObserver(Object obj) {
        boolean z = obj instanceof LifecycleEventObserver;
        boolean z2 = obj instanceof FullLifecycleObserver;
        if (z && z2) {
            return new FullLifecycleObserverAdapter((FullLifecycleObserver) obj, (LifecycleEventObserver) obj);
        }
        if (z2) {
            return new FullLifecycleObserverAdapter((FullLifecycleObserver) obj, null);
        }
        if (z) {
            return (LifecycleEventObserver) obj;
        }
        Class<?> cls = obj.getClass();
        if (getObserverConstructorType(cls) != 2) {
            return new ReflectiveGenericLifecycleObserver(obj);
        }
        List list = (List) sClassToAdapters.get(cls);
        if (list.size() == 1) {
            createGeneratedAdapter((Constructor) list.get(0), obj);
            return new SingleGeneratedAdapterObserver(null);
        }
        GeneratedAdapter[] generatedAdapterArr = new GeneratedAdapter[list.size()];
        for (int i = 0; i < list.size(); i++) {
            createGeneratedAdapter((Constructor) list.get(i), obj);
            generatedAdapterArr[i] = null;
        }
        return new CompositeGeneratedAdaptersObserver(generatedAdapterArr);
    }

    private static int resolveObserverCallbackType(Class cls) {
        ArrayList arrayList;
        if (cls.getCanonicalName() == null) {
            return 1;
        }
        Constructor generatedConstructor = generatedConstructor(cls);
        if (generatedConstructor != null) {
            sClassToAdapters.put(cls, Collections.singletonList(generatedConstructor));
            return 2;
        }
        if (ClassesInfoCache.sInstance.hasLifecycleMethods(cls)) {
            return 1;
        }
        Class superclass = cls.getSuperclass();
        if (!isLifecycleParent(superclass)) {
            arrayList = null;
        } else {
            if (getObserverConstructorType(superclass) == 1) {
                return 1;
            }
            arrayList = new ArrayList((Collection) sClassToAdapters.get(superclass));
        }
        for (Class<?> cls2 : cls.getInterfaces()) {
            if (isLifecycleParent(cls2)) {
                if (getObserverConstructorType(cls2) == 1) {
                    return 1;
                }
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.addAll((Collection) sClassToAdapters.get(cls2));
            }
        }
        if (arrayList == null) {
            return 1;
        }
        sClassToAdapters.put(cls, arrayList);
        return 2;
    }
}
