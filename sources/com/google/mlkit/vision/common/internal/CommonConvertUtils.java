package com.google.mlkit.vision.common.internal;
/* compiled from: com.google.mlkit:vision-common@@17.3.0 */
/* loaded from: classes.dex */
public class CommonConvertUtils {
    public static int convertToMVRotation(int i) {
        if (i != 0) {
            if (i != 90) {
                if (i != 180) {
                    if (i == 270) {
                        return 3;
                    }
                    throw new IllegalArgumentException("Invalid rotation: " + i);
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }
}
