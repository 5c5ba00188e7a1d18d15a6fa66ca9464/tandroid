package com.google.mlkit.common.sdkinternal;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.Dependency;
import java.util.UUID;

/* loaded from: classes.dex */
public class SharedPrefManager {
    public static final Component COMPONENT = Component.builder(SharedPrefManager.class).add(Dependency.required(MlKitContext.class)).add(Dependency.required(Context.class)).factory(new ComponentFactory() { // from class: com.google.mlkit.common.sdkinternal.zzs
        @Override // com.google.firebase.components.ComponentFactory
        public final Object create(ComponentContainer componentContainer) {
            return new SharedPrefManager((Context) componentContainer.get(Context.class));
        }
    }).build();
    protected final Context zza;

    public SharedPrefManager(Context context) {
        this.zza = context;
    }

    public synchronized String getMlSdkInstanceId() {
        String string = zza().getString("ml_sdk_instance_id", null);
        if (string != null) {
            return string;
        }
        String uuid = UUID.randomUUID().toString();
        zza().edit().putString("ml_sdk_instance_id", uuid).apply();
        return uuid;
    }

    protected final SharedPreferences zza() {
        return this.zza.getSharedPreferences("com.google.mlkit.internal", 0);
    }
}
