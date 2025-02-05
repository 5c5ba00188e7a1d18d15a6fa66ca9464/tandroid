package org.telegram.SQLite;

/* loaded from: classes.dex */
public class SQLiteException extends Exception {
    private static final long serialVersionUID = -2398298479089615621L;
    public final int errorCode;

    public SQLiteException() {
        this.errorCode = 0;
    }

    public SQLiteException(int i, String str) {
        super(str);
        this.errorCode = i;
    }

    public SQLiteException(String str) {
        this(0, str);
    }
}
