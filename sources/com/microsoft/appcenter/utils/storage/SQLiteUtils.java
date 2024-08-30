package com.microsoft.appcenter.utils.storage;

import android.database.sqlite.SQLiteQueryBuilder;
/* loaded from: classes.dex */
public abstract class SQLiteUtils {
    public static SQLiteQueryBuilder newSQLiteQueryBuilder() {
        return new SQLiteQueryBuilder();
    }
}
