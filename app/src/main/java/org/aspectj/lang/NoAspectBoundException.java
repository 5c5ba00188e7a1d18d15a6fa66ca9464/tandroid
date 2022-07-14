package org.aspectj.lang;
/* loaded from: classes3.dex */
public class NoAspectBoundException extends RuntimeException {
    Throwable cause;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public NoAspectBoundException(String aspectName, Throwable inner) {
        super(r0);
        String str;
        if (inner == null) {
            str = aspectName;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Exception while initializing ");
            stringBuffer.append(aspectName);
            stringBuffer.append(": ");
            stringBuffer.append(inner);
            str = stringBuffer.toString();
        }
        this.cause = inner;
    }

    public NoAspectBoundException() {
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
