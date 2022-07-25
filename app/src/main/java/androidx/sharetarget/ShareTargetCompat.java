package androidx.sharetarget;
/* loaded from: classes.dex */
class ShareTargetCompat {
    final String[] mCategories;
    final String mTargetClass;
    final TargetData[] mTargetData;

    /* loaded from: classes.dex */
    static class TargetData {
        final String mMimeType;

        /* JADX INFO: Access modifiers changed from: package-private */
        public TargetData(String scheme, String host, String port, String path, String pathPattern, String pathPrefix, String mimeType) {
            this.mMimeType = mimeType;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShareTargetCompat(TargetData[] data, String targetClass, String[] categories) {
        this.mTargetData = data;
        this.mTargetClass = targetClass;
        this.mCategories = categories;
    }
}
