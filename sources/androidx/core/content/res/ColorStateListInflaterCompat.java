package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.TypedValue;
import android.util.Xml;
import androidx.core.R$attr;
import androidx.core.R$styleable;
import androidx.core.math.MathUtils;
import org.telegram.messenger.NotificationCenter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public abstract class ColorStateListInflaterCompat {
    private static final ThreadLocal sTempTypedValue = new ThreadLocal();

    public static ColorStateList createFromXml(Resources resources, XmlPullParser xmlPullParser, Resources.Theme theme) {
        int next;
        AttributeSet asAttributeSet = Xml.asAttributeSet(xmlPullParser);
        do {
            next = xmlPullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next == 2) {
            return createFromXmlInner(resources, xmlPullParser, asAttributeSet, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static ColorStateList createFromXmlInner(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        String name = xmlPullParser.getName();
        if (name.equals("selector")) {
            return inflate(resources, xmlPullParser, attributeSet, theme);
        }
        throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": invalid color state list tag " + name);
    }

    private static TypedValue getTypedValue() {
        ThreadLocal threadLocal = sTempTypedValue;
        TypedValue typedValue = (TypedValue) threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }

    public static ColorStateList inflate(Resources resources, int i, Resources.Theme theme) {
        try {
            return createFromXml(resources, resources.getXml(i), theme);
        } catch (Exception e) {
            Log.e("CSLCompat", "Failed to inflate ColorStateList.", e);
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0086, code lost:
    
        if (r9.hasValue(r11) != false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0075, code lost:
    
        if (r9.hasValue(r11) != false) goto L25;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x006f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ColorStateList inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        int depth;
        int color;
        int i;
        int i2;
        int attributeCount;
        int i3;
        Resources resources2 = resources;
        int i4 = 1;
        int depth2 = xmlPullParser.getDepth() + 1;
        int[][] iArr = new int[20];
        int[] iArr2 = new int[20];
        int i5 = 0;
        while (true) {
            int next = xmlPullParser.next();
            if (next == i4 || ((depth = xmlPullParser.getDepth()) < depth2 && next == 3)) {
                break;
            }
            if (next == 2 && depth <= depth2 && xmlPullParser.getName().equals("item")) {
                TypedArray obtainAttributes = obtainAttributes(resources2, theme, attributeSet, R$styleable.ColorStateListItem);
                int i6 = R$styleable.ColorStateListItem_android_color;
                int resourceId = obtainAttributes.getResourceId(i6, -1);
                if (resourceId != -1 && !isColorInt(resources2, resourceId)) {
                    try {
                        color = createFromXml(resources2, resources2.getXml(resourceId), theme).getDefaultColor();
                    } catch (Exception unused) {
                        i6 = R$styleable.ColorStateListItem_android_color;
                    }
                    i = R$styleable.ColorStateListItem_android_alpha;
                    float f = 1.0f;
                    if (!obtainAttributes.hasValue(i)) {
                        i = R$styleable.ColorStateListItem_alpha;
                    }
                    f = obtainAttributes.getFloat(i, 1.0f);
                    if (Build.VERSION.SDK_INT >= 31) {
                        i2 = R$styleable.ColorStateListItem_android_lStar;
                    }
                    i2 = R$styleable.ColorStateListItem_lStar;
                    float f2 = obtainAttributes.getFloat(i2, -1.0f);
                    obtainAttributes.recycle();
                    attributeCount = attributeSet.getAttributeCount();
                    int[] iArr3 = new int[attributeCount];
                    int i7 = 0;
                    for (i3 = 0; i3 < attributeCount; i3++) {
                        int attributeNameResource = attributeSet.getAttributeNameResource(i3);
                        if (attributeNameResource != 16843173 && attributeNameResource != 16843551 && attributeNameResource != R$attr.alpha && attributeNameResource != R$attr.lStar) {
                            int i8 = i7 + 1;
                            if (!attributeSet.getAttributeBooleanValue(i3, false)) {
                                attributeNameResource = -attributeNameResource;
                            }
                            iArr3[i7] = attributeNameResource;
                            i7 = i8;
                        }
                    }
                    int[] trimStateSet = StateSet.trimStateSet(iArr3, i7);
                    iArr2 = GrowingArrayUtils.append(iArr2, i5, modulateColorAlpha(color, f, f2));
                    iArr = (int[][]) GrowingArrayUtils.append(iArr, i5, trimStateSet);
                    i5++;
                }
                color = obtainAttributes.getColor(i6, -65281);
                i = R$styleable.ColorStateListItem_android_alpha;
                float f3 = 1.0f;
                if (!obtainAttributes.hasValue(i)) {
                }
                f3 = obtainAttributes.getFloat(i, 1.0f);
                if (Build.VERSION.SDK_INT >= 31) {
                }
                i2 = R$styleable.ColorStateListItem_lStar;
                float f22 = obtainAttributes.getFloat(i2, -1.0f);
                obtainAttributes.recycle();
                attributeCount = attributeSet.getAttributeCount();
                int[] iArr32 = new int[attributeCount];
                int i72 = 0;
                while (i3 < attributeCount) {
                }
                int[] trimStateSet2 = StateSet.trimStateSet(iArr32, i72);
                iArr2 = GrowingArrayUtils.append(iArr2, i5, modulateColorAlpha(color, f3, f22));
                iArr = (int[][]) GrowingArrayUtils.append(iArr, i5, trimStateSet2);
                i5++;
            }
            i4 = 1;
            resources2 = resources;
        }
        int[] iArr4 = new int[i5];
        int[][] iArr5 = new int[i5];
        System.arraycopy(iArr2, 0, iArr4, 0, i5);
        System.arraycopy(iArr, 0, iArr5, 0, i5);
        return new ColorStateList(iArr5, iArr4);
    }

    private static boolean isColorInt(Resources resources, int i) {
        TypedValue typedValue = getTypedValue();
        resources.getValue(i, typedValue, true);
        int i2 = typedValue.type;
        return i2 >= 28 && i2 <= 31;
    }

    private static int modulateColorAlpha(int i, float f, float f2) {
        boolean z = f2 >= 0.0f && f2 <= 100.0f;
        if (f == 1.0f && !z) {
            return i;
        }
        int clamp = MathUtils.clamp((int) ((Color.alpha(i) * f) + 0.5f), 0, NotificationCenter.notificationsCountUpdated);
        if (z) {
            CamColor fromColor = CamColor.fromColor(i);
            i = CamColor.toColor(fromColor.getHue(), fromColor.getChroma(), f2);
        }
        return (i & 16777215) | (clamp << 24);
    }

    private static TypedArray obtainAttributes(Resources resources, Resources.Theme theme, AttributeSet attributeSet, int[] iArr) {
        return theme == null ? resources.obtainAttributes(attributeSet, iArr) : theme.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }
}
