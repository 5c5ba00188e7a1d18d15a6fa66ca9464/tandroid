package com.google.gson.internal.sql;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import java.sql.Date;
import java.sql.Timestamp;
/* loaded from: classes.dex */
public abstract class SqlTypesSupport {
    public static final DefaultDateTypeAdapter.DateType DATE_DATE_TYPE;
    public static final TypeAdapterFactory DATE_FACTORY;
    public static final boolean SUPPORTS_SQL_TYPES;
    public static final DefaultDateTypeAdapter.DateType TIMESTAMP_DATE_TYPE;
    public static final TypeAdapterFactory TIMESTAMP_FACTORY;
    public static final TypeAdapterFactory TIME_FACTORY;

    static {
        boolean z;
        TypeAdapterFactory typeAdapterFactory;
        try {
            Class.forName("java.sql.Date");
            z = true;
        } catch (ClassNotFoundException unused) {
            z = false;
        }
        SUPPORTS_SQL_TYPES = z;
        if (z) {
            DATE_DATE_TYPE = new DefaultDateTypeAdapter.DateType(Date.class) { // from class: com.google.gson.internal.sql.SqlTypesSupport.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType
                public Date deserialize(java.util.Date date) {
                    return new Date(date.getTime());
                }
            };
            TIMESTAMP_DATE_TYPE = new DefaultDateTypeAdapter.DateType(Timestamp.class) { // from class: com.google.gson.internal.sql.SqlTypesSupport.2
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType
                public Timestamp deserialize(java.util.Date date) {
                    return new Timestamp(date.getTime());
                }
            };
            DATE_FACTORY = SqlDateTypeAdapter.FACTORY;
            TIME_FACTORY = SqlTimeTypeAdapter.FACTORY;
            typeAdapterFactory = SqlTimestampTypeAdapter.FACTORY;
        } else {
            typeAdapterFactory = null;
            DATE_DATE_TYPE = null;
            TIMESTAMP_DATE_TYPE = null;
            DATE_FACTORY = null;
            TIME_FACTORY = null;
        }
        TIMESTAMP_FACTORY = typeAdapterFactory;
    }
}
