package com.google.firebase.abt.component;

import android.content.Context;
import com.google.firebase.abt.FirebaseABTesting;
import com.google.firebase.inject.Provider;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class AbtComponent {
    private final Map abtOriginInstances = new HashMap();
    private final Provider analyticsConnector;
    private final Context appContext;

    protected AbtComponent(Context context, Provider provider) {
        this.appContext = context;
        this.analyticsConnector = provider;
    }

    protected FirebaseABTesting createAbtInstance(String str) {
        return new FirebaseABTesting(this.appContext, this.analyticsConnector, str);
    }

    public synchronized FirebaseABTesting get(String str) {
        try {
            if (!this.abtOriginInstances.containsKey(str)) {
                this.abtOriginInstances.put(str, createAbtInstance(str));
            }
        } catch (Throwable th) {
            throw th;
        }
        return (FirebaseABTesting) this.abtOriginInstances.get(str);
    }
}
