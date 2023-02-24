package com.google.common.base;

import java.util.logging.Logger;
/* loaded from: classes.dex */
final class Platform {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    static {
        Logger.getLogger(Platform.class.getName());
        loadPatternCompiler();
    }

    private Platform() {
    }

    private static PatternCompiler loadPatternCompiler() {
        return new JdkPatternCompiler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class JdkPatternCompiler implements PatternCompiler {
        private JdkPatternCompiler() {
        }
    }
}
