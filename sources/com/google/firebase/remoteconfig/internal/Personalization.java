package com.google.firebase.remoteconfig.internal;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.firebase.inject.Provider;
import j$.util.DesugarCollections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class Personalization {
    private final Provider analyticsConnector;
    private final Map loggedChoiceIds = DesugarCollections.synchronizedMap(new HashMap());

    public Personalization(Provider provider) {
        this.analyticsConnector = provider;
    }

    public void logArmActive(String str, ConfigContainer configContainer) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.analyticsConnector.get());
    }
}
