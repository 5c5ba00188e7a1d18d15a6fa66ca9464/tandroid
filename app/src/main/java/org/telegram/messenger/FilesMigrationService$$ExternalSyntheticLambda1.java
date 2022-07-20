package org.telegram.messenger;

import j$.util.function.Consumer;
import java.io.File;
import java.nio.file.Path;
/* loaded from: classes.dex */
public final /* synthetic */ class FilesMigrationService$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ FilesMigrationService f$0;
    public final /* synthetic */ File f$1;

    public /* synthetic */ FilesMigrationService$$ExternalSyntheticLambda1(FilesMigrationService filesMigrationService, File file) {
        this.f$0 = filesMigrationService;
        this.f$1 = file;
    }

    @Override // j$.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$moveDirectory$0(this.f$1, (Path) obj);
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return consumer.getClass();
    }
}
