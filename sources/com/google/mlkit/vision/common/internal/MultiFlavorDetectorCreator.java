package com.google.mlkit.vision.common.internal;

import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.inject.Provider;
import com.google.mlkit.common.sdkinternal.MlKitContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class MultiFlavorDetectorCreator {
    private final Map zza = new HashMap();

    /* loaded from: classes.dex */
    public interface DetectorCreator {
        MultiFlavorDetector create(DetectorOptions detectorOptions);
    }

    /* loaded from: classes.dex */
    public interface DetectorOptions {
    }

    /* loaded from: classes.dex */
    public interface MultiFlavorDetector {
    }

    /* loaded from: classes.dex */
    public static class Registration {
        private final Class zza;
        private final Provider zzb;
        private final int zzc;

        public Registration(Class cls, Provider provider, int i) {
            this.zza = cls;
            this.zzb = provider;
            this.zzc = i;
        }

        final int zza() {
            return this.zzc;
        }

        final Provider zzb() {
            return this.zzb;
        }

        final Class zzc() {
            return this.zza;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiFlavorDetectorCreator(Set set) {
        HashMap hashMap = new HashMap();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Registration registration = (Registration) it.next();
            Class zzc = registration.zzc();
            if (!this.zza.containsKey(zzc) || registration.zza() >= ((Integer) Preconditions.checkNotNull((Integer) hashMap.get(zzc))).intValue()) {
                this.zza.put(zzc, registration.zzb());
                hashMap.put(zzc, Integer.valueOf(registration.zza()));
            }
        }
    }

    public static synchronized MultiFlavorDetectorCreator getInstance() {
        MultiFlavorDetectorCreator multiFlavorDetectorCreator;
        synchronized (MultiFlavorDetectorCreator.class) {
            multiFlavorDetectorCreator = (MultiFlavorDetectorCreator) MlKitContext.getInstance().get(MultiFlavorDetectorCreator.class);
        }
        return multiFlavorDetectorCreator;
    }

    public MultiFlavorDetector create(DetectorOptions detectorOptions) {
        return ((DetectorCreator) ((Provider) Preconditions.checkNotNull((Provider) this.zza.get(detectorOptions.getClass()))).get()).create(detectorOptions);
    }
}
