package androidx.sharetarget;
/* loaded from: classes.dex */
class ShareTargetCompat {
    final String[] mCategories;
    final String mTargetClass;
    final TargetData[] mTargetData;

    /* loaded from: classes.dex */
    static class TargetData {
        final String mHost;
        final String mMimeType;
        final String mPath;
        final String mPathPattern;
        final String mPathPrefix;
        final String mPort;
        final String mScheme;

        /* JADX INFO: Access modifiers changed from: package-private */
        public TargetData(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
            this.mScheme = str;
            this.mHost = str2;
            this.mPort = str3;
            this.mPath = str4;
            this.mPathPattern = str5;
            this.mPathPrefix = str6;
            this.mMimeType = str7;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShareTargetCompat(TargetData[] targetDataArr, String str, String[] strArr) {
        this.mTargetData = targetDataArr;
        this.mTargetClass = str;
        this.mCategories = strArr;
    }
}
