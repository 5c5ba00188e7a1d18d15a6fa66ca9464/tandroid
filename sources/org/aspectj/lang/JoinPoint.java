package org.aspectj.lang;

/* loaded from: classes.dex */
public interface JoinPoint {

    public interface StaticPart {
        String toString();
    }

    Object getTarget();
}
