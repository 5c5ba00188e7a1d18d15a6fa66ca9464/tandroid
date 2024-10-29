package androidx.vectordrawable.graphics.drawable;

import android.animation.TypeEvaluator;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public class ArgbEvaluator implements TypeEvaluator {
    private static final ArgbEvaluator sInstance = new ArgbEvaluator();

    public static ArgbEvaluator getInstance() {
        return sInstance;
    }

    @Override // android.animation.TypeEvaluator
    public Object evaluate(float f, Object obj, Object obj2) {
        int intValue = ((Integer) obj).intValue();
        float f2 = ((intValue >> 24) & NotificationCenter.playerDidStartPlaying) / 255.0f;
        float f3 = ((intValue >> 16) & NotificationCenter.playerDidStartPlaying) / 255.0f;
        float f4 = ((intValue >> 8) & NotificationCenter.playerDidStartPlaying) / 255.0f;
        int intValue2 = ((Integer) obj2).intValue();
        float f5 = ((intValue2 >> 24) & NotificationCenter.playerDidStartPlaying) / 255.0f;
        float f6 = ((intValue2 >> 16) & NotificationCenter.playerDidStartPlaying) / 255.0f;
        float f7 = ((intValue2 >> 8) & NotificationCenter.playerDidStartPlaying) / 255.0f;
        float pow = (float) Math.pow(f3, 2.2d);
        float pow2 = (float) Math.pow(f4, 2.2d);
        float pow3 = (float) Math.pow((intValue & NotificationCenter.playerDidStartPlaying) / 255.0f, 2.2d);
        float pow4 = (float) Math.pow(f6, 2.2d);
        float f8 = f2 + ((f5 - f2) * f);
        float pow5 = pow2 + ((((float) Math.pow(f7, 2.2d)) - pow2) * f);
        float pow6 = pow3 + (f * (((float) Math.pow((intValue2 & NotificationCenter.playerDidStartPlaying) / 255.0f, 2.2d)) - pow3));
        return Integer.valueOf((Math.round(((float) Math.pow(pow + ((pow4 - pow) * f), 0.45454545454545453d)) * 255.0f) << 16) | (Math.round(f8 * 255.0f) << 24) | (Math.round(((float) Math.pow(pow5, 0.45454545454545453d)) * 255.0f) << 8) | Math.round(((float) Math.pow(pow6, 0.45454545454545453d)) * 255.0f));
    }
}
