package androidx.appcompat.resources;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class Compatibility$Api21Impl {
    public static void inflate(Drawable drawable, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws IOException, XmlPullParserException {
        drawable.inflate(resources, xmlPullParser, attributeSet, theme);
    }

    public static int getChangingConfigurations(TypedArray typedArray) {
        int changingConfigurations;
        changingConfigurations = typedArray.getChangingConfigurations();
        return changingConfigurations;
    }

    public static Drawable createFromXmlInner(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws IOException, XmlPullParserException {
        Drawable createFromXmlInner;
        createFromXmlInner = Drawable.createFromXmlInner(resources, xmlPullParser, attributeSet, theme);
        return createFromXmlInner;
    }
}
