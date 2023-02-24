package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.common.base.Ascii;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public interface HttpDataSource extends DataSource {

    /* loaded from: classes.dex */
    public static final class RequestProperties {
        private final Map<String, String> requestProperties = new HashMap();
        private Map<String, String> requestPropertiesSnapshot;

        public synchronized Map<String, String> getSnapshot() {
            if (this.requestPropertiesSnapshot == null) {
                this.requestPropertiesSnapshot = Collections.unmodifiableMap(new HashMap(this.requestProperties));
            }
            return this.requestPropertiesSnapshot;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class BaseFactory implements DataSource.Factory {
        private final RequestProperties defaultRequestProperties = new RequestProperties();

        protected abstract HttpDataSource createDataSourceInternal(RequestProperties requestProperties);

        @Override // com.google.android.exoplayer2.upstream.DataSource.Factory
        public final HttpDataSource createDataSource() {
            return createDataSourceInternal(this.defaultRequestProperties);
        }
    }

    /* loaded from: classes.dex */
    public static class HttpDataSourceException extends DataSourceException {
        public final int type;

        private static int assignErrorCode(int i, int i2) {
            if (i == 2000 && i2 == 1) {
                return 2001;
            }
            return i;
        }

        public static HttpDataSourceException createForIOException(IOException iOException, DataSpec dataSpec, int i) {
            int i2;
            String message = iOException.getMessage();
            if (iOException instanceof SocketTimeoutException) {
                i2 = 2002;
            } else if (iOException instanceof InterruptedIOException) {
                i2 = 1004;
            } else {
                i2 = (message == null || !Ascii.toLowerCase(message).matches("cleartext.*not permitted.*")) ? 2001 : 2007;
            }
            if (i2 == 2007) {
                return new CleartextNotPermittedException(iOException, dataSpec);
            }
            return new HttpDataSourceException(iOException, dataSpec, i2, i);
        }

        public HttpDataSourceException(DataSpec dataSpec, int i, int i2) {
            super(assignErrorCode(i, i2));
            this.type = i2;
        }

        public HttpDataSourceException(String str, DataSpec dataSpec, int i, int i2) {
            super(str, assignErrorCode(i, i2));
            this.type = i2;
        }

        public HttpDataSourceException(IOException iOException, DataSpec dataSpec, int i, int i2) {
            super(iOException, assignErrorCode(i, i2));
            this.type = i2;
        }

        public HttpDataSourceException(String str, IOException iOException, DataSpec dataSpec, int i, int i2) {
            super(str, iOException, assignErrorCode(i, i2));
            this.type = i2;
        }
    }

    /* loaded from: classes.dex */
    public static final class CleartextNotPermittedException extends HttpDataSourceException {
        public CleartextNotPermittedException(IOException iOException, DataSpec dataSpec) {
            super("Cleartext HTTP traffic not permitted. See https://exoplayer.dev/issues/cleartext-not-permitted", iOException, dataSpec, 2007, 1);
        }
    }

    /* loaded from: classes.dex */
    public static final class InvalidContentTypeException extends HttpDataSourceException {
        public InvalidContentTypeException(String str, DataSpec dataSpec) {
            super("Invalid content type: " + str, dataSpec, 2003, 1);
        }
    }

    /* loaded from: classes.dex */
    public static final class InvalidResponseCodeException extends HttpDataSourceException {
        public final Map<String, List<String>> headerFields;
        public final int responseCode;

        public InvalidResponseCodeException(int i, String str, IOException iOException, Map<String, List<String>> map, DataSpec dataSpec, byte[] bArr) {
            super("Response code: " + i, iOException, dataSpec, 2004, 1);
            this.responseCode = i;
            this.headerFields = map;
        }
    }
}
