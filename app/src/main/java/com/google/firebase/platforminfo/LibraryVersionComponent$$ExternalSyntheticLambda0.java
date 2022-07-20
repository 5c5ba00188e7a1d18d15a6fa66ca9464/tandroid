package com.google.firebase.platforminfo;

import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.platforminfo.LibraryVersionComponent;
/* loaded from: classes.dex */
public final /* synthetic */ class LibraryVersionComponent$$ExternalSyntheticLambda0 implements ComponentFactory {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ LibraryVersionComponent.VersionExtractor f$1;

    public /* synthetic */ LibraryVersionComponent$$ExternalSyntheticLambda0(String str, LibraryVersionComponent.VersionExtractor versionExtractor) {
        this.f$0 = str;
        this.f$1 = versionExtractor;
    }

    @Override // com.google.firebase.components.ComponentFactory
    public final Object create(ComponentContainer componentContainer) {
        LibraryVersion lambda$fromContext$0;
        lambda$fromContext$0 = LibraryVersionComponent.lambda$fromContext$0(this.f$0, this.f$1, componentContainer);
        return lambda$fromContext$0;
    }
}
