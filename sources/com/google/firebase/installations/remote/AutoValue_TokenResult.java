package com.google.firebase.installations.remote;

import com.google.firebase.installations.remote.TokenResult;

/* loaded from: classes.dex */
final class AutoValue_TokenResult extends TokenResult {
    private final TokenResult.ResponseCode responseCode;
    private final String token;
    private final long tokenExpirationTimestamp;

    /* loaded from: classes.dex */
    static final class Builder extends TokenResult.Builder {
        private TokenResult.ResponseCode responseCode;
        private String token;
        private Long tokenExpirationTimestamp;

        @Override // com.google.firebase.installations.remote.TokenResult.Builder
        public TokenResult build() {
            String str = "";
            if (this.tokenExpirationTimestamp == null) {
                str = " tokenExpirationTimestamp";
            }
            if (str.isEmpty()) {
                return new AutoValue_TokenResult(this.token, this.tokenExpirationTimestamp.longValue(), this.responseCode);
            }
            throw new IllegalStateException("Missing required properties:" + str);
        }

        @Override // com.google.firebase.installations.remote.TokenResult.Builder
        public TokenResult.Builder setResponseCode(TokenResult.ResponseCode responseCode) {
            this.responseCode = responseCode;
            return this;
        }

        @Override // com.google.firebase.installations.remote.TokenResult.Builder
        public TokenResult.Builder setToken(String str) {
            this.token = str;
            return this;
        }

        @Override // com.google.firebase.installations.remote.TokenResult.Builder
        public TokenResult.Builder setTokenExpirationTimestamp(long j) {
            this.tokenExpirationTimestamp = Long.valueOf(j);
            return this;
        }
    }

    private AutoValue_TokenResult(String str, long j, TokenResult.ResponseCode responseCode) {
        this.token = str;
        this.tokenExpirationTimestamp = j;
        this.responseCode = responseCode;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TokenResult)) {
            return false;
        }
        TokenResult tokenResult = (TokenResult) obj;
        String str = this.token;
        if (str != null ? str.equals(tokenResult.getToken()) : tokenResult.getToken() == null) {
            if (this.tokenExpirationTimestamp == tokenResult.getTokenExpirationTimestamp()) {
                TokenResult.ResponseCode responseCode = this.responseCode;
                TokenResult.ResponseCode responseCode2 = tokenResult.getResponseCode();
                if (responseCode == null) {
                    if (responseCode2 == null) {
                        return true;
                    }
                } else if (responseCode.equals(responseCode2)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.google.firebase.installations.remote.TokenResult
    public TokenResult.ResponseCode getResponseCode() {
        return this.responseCode;
    }

    @Override // com.google.firebase.installations.remote.TokenResult
    public String getToken() {
        return this.token;
    }

    @Override // com.google.firebase.installations.remote.TokenResult
    public long getTokenExpirationTimestamp() {
        return this.tokenExpirationTimestamp;
    }

    public int hashCode() {
        String str = this.token;
        int hashCode = str == null ? 0 : str.hashCode();
        long j = this.tokenExpirationTimestamp;
        int i = (((hashCode ^ 1000003) * 1000003) ^ ((int) (j ^ (j >>> 32)))) * 1000003;
        TokenResult.ResponseCode responseCode = this.responseCode;
        return i ^ (responseCode != null ? responseCode.hashCode() : 0);
    }

    public String toString() {
        return "TokenResult{token=" + this.token + ", tokenExpirationTimestamp=" + this.tokenExpirationTimestamp + ", responseCode=" + this.responseCode + "}";
    }
}
