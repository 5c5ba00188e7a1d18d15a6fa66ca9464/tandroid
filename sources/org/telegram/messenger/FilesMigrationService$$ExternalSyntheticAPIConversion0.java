package org.telegram.messenger;

import j$.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;
/* loaded from: classes3.dex */
public abstract /* synthetic */ class FilesMigrationService$$ExternalSyntheticAPIConversion0 {
    public static /* synthetic */ Stream m(Path path) {
        return Stream.VivifiedWrapper.convert(Files.list(path));
    }
}
