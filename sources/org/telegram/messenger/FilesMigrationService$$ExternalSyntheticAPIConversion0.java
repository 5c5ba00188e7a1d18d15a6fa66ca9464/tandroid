package org.telegram.messenger;

import j$.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes3.dex */
public final /* synthetic */ class FilesMigrationService$$ExternalSyntheticAPIConversion0 {
    public static /* synthetic */ Stream m(Path path) {
        return Stream.VivifiedWrapper.convert(Files.list(path));
    }
}
