package org.telegram.messenger;

import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.nl.languageid.LanguageIdentification;
/* loaded from: classes.dex */
public class LanguageDetector {

    /* loaded from: classes.dex */
    public interface ExceptionCallback {
        void run(Exception exc);
    }

    /* loaded from: classes.dex */
    public interface StringCallback {
        void run(String str);
    }

    public static boolean hasSupport() {
        return true;
    }

    public static void detectLanguage(String str, StringCallback stringCallback, ExceptionCallback exceptionCallback) {
        detectLanguage(str, stringCallback, exceptionCallback, false);
    }

    public static void detectLanguage(String str, StringCallback stringCallback, ExceptionCallback exceptionCallback, boolean z) {
        if (z) {
            try {
                MlKitContext.zza(ApplicationLoader.applicationContext);
            } catch (IllegalStateException e) {
                if (!z) {
                    detectLanguage(str, stringCallback, exceptionCallback, true);
                    return;
                } else if (exceptionCallback == null) {
                    return;
                } else {
                    exceptionCallback.run(e);
                    return;
                }
            } catch (Exception e2) {
                if (exceptionCallback == null) {
                    return;
                }
                exceptionCallback.run(e2);
                return;
            } catch (Throwable unused) {
                if (exceptionCallback == null) {
                    return;
                }
                exceptionCallback.run(null);
                return;
            }
        }
        LanguageIdentification.getClient().identifyLanguage(str).addOnSuccessListener(new LanguageDetector$$ExternalSyntheticLambda1(stringCallback)).addOnFailureListener(new LanguageDetector$$ExternalSyntheticLambda0(exceptionCallback));
    }

    public static /* synthetic */ void lambda$detectLanguage$0(StringCallback stringCallback, String str) {
        if (stringCallback != null) {
            stringCallback.run(str);
        }
    }

    public static /* synthetic */ void lambda$detectLanguage$1(ExceptionCallback exceptionCallback, Exception exc) {
        if (exceptionCallback != null) {
            exceptionCallback.run(exc);
        }
    }
}
