package com.google.android.exoplayer2.upstream;

import java.io.IOException;
/* loaded from: classes.dex */
public abstract class DataSourceUtil {
    public static void closeQuietly(DataSource dataSource) {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (IOException unused) {
            }
        }
    }
}
