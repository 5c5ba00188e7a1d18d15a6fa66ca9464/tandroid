package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.DataSource;
import java.io.IOException;
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

        /* renamed from: createDataSourceInternal */
        protected abstract HttpDataSource mo175createDataSourceInternal(RequestProperties requestProperties);

        @Override // com.google.android.exoplayer2.upstream.DataSource.Factory
        /* renamed from: createDataSource  reason: collision with other method in class */
        public final HttpDataSource mo821createDataSource() {
            return mo175createDataSourceInternal(this.defaultRequestProperties);
        }
    }

    /* loaded from: classes.dex */
    public static class HttpDataSourceException extends IOException {
        public HttpDataSourceException(String str, DataSpec dataSpec, int i) {
            super(str);
        }

        public HttpDataSourceException(IOException iOException, DataSpec dataSpec, int i) {
            super(iOException);
        }

        public HttpDataSourceException(String str, IOException iOException, DataSpec dataSpec, int i) {
            super(str, iOException);
        }
    }

    /* loaded from: classes.dex */
    public static final class InvalidContentTypeException extends HttpDataSourceException {
        public InvalidContentTypeException(String str, DataSpec dataSpec) {
            super("Invalid content type: " + str, dataSpec, 1);
        }
    }

    /* loaded from: classes.dex */
    public static final class InvalidResponseCodeException extends HttpDataSourceException {
        public final int responseCode;

        public InvalidResponseCodeException(int i, String str, Map<String, List<String>> map, DataSpec dataSpec) {
            super("Response code: " + i, dataSpec, 1);
            this.responseCode = i;
        }
    }
}
