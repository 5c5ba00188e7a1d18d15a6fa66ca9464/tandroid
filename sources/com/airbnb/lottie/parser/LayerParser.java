package com.airbnb.lottie.parser;

import android.graphics.Color;
import android.graphics.Rect;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.BlurEffect;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.content.LBlendMode;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
/* loaded from: classes.dex */
public class LayerParser {
    private static final JsonReader.Options NAMES = JsonReader.Options.of("nm", "ind", "refId", "ty", "parent", "sw", "sh", "sc", "ks", "tt", "masksProperties", "shapes", "t", "ef", "sr", "st", "w", "h", "ip", "op", "tm", "cl", "hd", "ao", "bm");
    private static final JsonReader.Options TEXT_NAMES = JsonReader.Options.of("d", "a");
    private static final JsonReader.Options EFFECTS_NAMES = JsonReader.Options.of("ty", "nm");

    public static Layer parse(LottieComposition lottieComposition) {
        Rect bounds = lottieComposition.getBounds();
        return new Layer(Collections.emptyList(), lottieComposition, "__container", -1L, Layer.LayerType.PRE_COMP, -1L, null, Collections.emptyList(), new AnimatableTransform(), 0, 0, 0, 0.0f, 0.0f, bounds.width(), bounds.height(), null, null, Collections.emptyList(), Layer.MatteType.NONE, null, false, null, null, LBlendMode.NORMAL);
    }

    public static Layer parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        ArrayList arrayList;
        boolean z;
        String str;
        ArrayList arrayList2;
        AnimatableTransform animatableTransform;
        boolean z2;
        Layer.MatteType matteType = Layer.MatteType.NONE;
        LBlendMode lBlendMode = LBlendMode.NORMAL;
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        jsonReader.beginObject();
        Float valueOf = Float.valueOf(1.0f);
        boolean z3 = false;
        Float valueOf2 = Float.valueOf(0.0f);
        Layer.MatteType matteType2 = matteType;
        LBlendMode lBlendMode2 = lBlendMode;
        AnimatableTransform animatableTransform2 = null;
        Layer.LayerType layerType = null;
        String str2 = null;
        AnimatableTextFrame animatableTextFrame = null;
        AnimatableTextProperties animatableTextProperties = null;
        AnimatableFloatValue animatableFloatValue = null;
        BlurEffect blurEffect = null;
        DropShadowEffect dropShadowEffect = null;
        long j = -1;
        float f = 0.0f;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        float f2 = 1.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        boolean z4 = false;
        float f6 = 0.0f;
        long j2 = 0;
        String str3 = "UNSET";
        String str4 = null;
        while (true) {
            boolean z5 = false;
            while (jsonReader.hasNext()) {
                switch (jsonReader.selectName(NAMES)) {
                    case 0:
                        str3 = jsonReader.nextString();
                        z3 = false;
                    case 1:
                        j2 = jsonReader.nextInt();
                        z3 = false;
                    case 2:
                        str2 = jsonReader.nextString();
                        z3 = false;
                    case 3:
                        z2 = z5;
                        int nextInt = jsonReader.nextInt();
                        layerType = Layer.LayerType.UNKNOWN;
                        if (nextInt < layerType.ordinal()) {
                            layerType = Layer.LayerType.values()[nextInt];
                        }
                        z5 = z2;
                        z3 = false;
                    case 4:
                        j = jsonReader.nextInt();
                        z3 = false;
                    case 5:
                        i = (int) (jsonReader.nextInt() * Utils.dpScale());
                        z3 = false;
                    case 6:
                        i2 = (int) (jsonReader.nextInt() * Utils.dpScale());
                        z3 = false;
                    case 7:
                        i3 = Color.parseColor(jsonReader.nextString());
                        z3 = false;
                    case 8:
                        animatableTransform2 = AnimatableTransformParser.parse(jsonReader, lottieComposition);
                        z3 = false;
                    case 9:
                        z2 = z5;
                        int nextInt2 = jsonReader.nextInt();
                        if (nextInt2 >= Layer.MatteType.values().length) {
                            lottieComposition.addWarning("Unsupported matte type: " + nextInt2);
                        } else {
                            matteType2 = Layer.MatteType.values()[nextInt2];
                            int i4 = 1.$SwitchMap$com$airbnb$lottie$model$layer$Layer$MatteType[matteType2.ordinal()];
                            if (i4 == 1) {
                                lottieComposition.addWarning("Unsupported matte type: Luma");
                            } else if (i4 == 2) {
                                lottieComposition.addWarning("Unsupported matte type: Luma Inverted");
                            }
                            lottieComposition.incrementMatteOrMaskCount(1);
                        }
                        z5 = z2;
                        z3 = false;
                    case 10:
                        z2 = z5;
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            arrayList3.add(MaskParser.parse(jsonReader, lottieComposition));
                        }
                        lottieComposition.incrementMatteOrMaskCount(arrayList3.size());
                        jsonReader.endArray();
                        z5 = z2;
                        z3 = false;
                    case 11:
                        z2 = z5;
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            ContentModel parse = ContentModelParser.parse(jsonReader, lottieComposition);
                            if (parse != null) {
                                arrayList4.add(parse);
                            }
                        }
                        jsonReader.endArray();
                        z5 = z2;
                        z3 = false;
                    case 12:
                        z2 = z5;
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            int selectName = jsonReader.selectName(TEXT_NAMES);
                            if (selectName == 0) {
                                animatableTextFrame = AnimatableValueParser.parseDocumentData(jsonReader, lottieComposition);
                            } else if (selectName == 1) {
                                jsonReader.beginArray();
                                if (jsonReader.hasNext()) {
                                    animatableTextProperties = AnimatableTextPropertiesParser.parse(jsonReader, lottieComposition);
                                }
                                while (jsonReader.hasNext()) {
                                    jsonReader.skipValue();
                                }
                                jsonReader.endArray();
                            } else {
                                jsonReader.skipName();
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.endObject();
                        z5 = z2;
                        z3 = false;
                    case 13:
                        z2 = z5;
                        jsonReader.beginArray();
                        ArrayList arrayList5 = new ArrayList();
                        while (jsonReader.hasNext()) {
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                int selectName2 = jsonReader.selectName(EFFECTS_NAMES);
                                if (selectName2 == 0) {
                                    int nextInt3 = jsonReader.nextInt();
                                    if (nextInt3 == 29) {
                                        blurEffect = BlurEffectParser.parse(jsonReader, lottieComposition);
                                    } else if (nextInt3 == 25) {
                                        dropShadowEffect = new DropShadowEffectParser().parse(jsonReader, lottieComposition);
                                    }
                                } else if (selectName2 == 1) {
                                    arrayList5.add(jsonReader.nextString());
                                } else {
                                    jsonReader.skipName();
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                        }
                        jsonReader.endArray();
                        lottieComposition.addWarning("Lottie doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: " + arrayList5);
                        z5 = z2;
                        z3 = false;
                    case 14:
                        f2 = (float) jsonReader.nextDouble();
                        z3 = false;
                    case 15:
                        f3 = (float) jsonReader.nextDouble();
                        z3 = false;
                    case 16:
                        double nextDouble = jsonReader.nextDouble();
                        double dpScale = Utils.dpScale();
                        Double.isNaN(dpScale);
                        f4 = (float) (nextDouble * dpScale);
                        z3 = false;
                    case 17:
                        double nextDouble2 = jsonReader.nextDouble();
                        z2 = z5;
                        double dpScale2 = Utils.dpScale();
                        Double.isNaN(dpScale2);
                        f5 = (float) (nextDouble2 * dpScale2);
                        z5 = z2;
                        z3 = false;
                    case 18:
                        f = (float) jsonReader.nextDouble();
                    case 19:
                        f6 = (float) jsonReader.nextDouble();
                    case 20:
                        animatableFloatValue = AnimatableValueParser.parseFloat(jsonReader, lottieComposition, z3);
                    case 21:
                        str4 = jsonReader.nextString();
                    case 22:
                        z4 = jsonReader.nextBoolean();
                    case 23:
                        if (jsonReader.nextInt() == 1) {
                            z5 = true;
                        }
                        break;
                    case 24:
                        int nextInt4 = jsonReader.nextInt();
                        if (nextInt4 >= LBlendMode.values().length) {
                            lottieComposition.addWarning("Unsupported Blend Mode: " + nextInt4);
                            lBlendMode2 = LBlendMode.NORMAL;
                        } else {
                            lBlendMode2 = LBlendMode.values()[nextInt4];
                        }
                    default:
                        z2 = z5;
                        jsonReader.skipName();
                        jsonReader.skipValue();
                        z5 = z2;
                        z3 = false;
                }
            }
            boolean z6 = z5;
            jsonReader.endObject();
            ArrayList arrayList6 = new ArrayList();
            if (f > 0.0f) {
                z = z6;
                arrayList = arrayList3;
                str = str4;
                arrayList2 = arrayList6;
                arrayList2.add(new Keyframe(lottieComposition, valueOf2, valueOf2, null, 0.0f, Float.valueOf(f)));
            } else {
                arrayList = arrayList3;
                z = z6;
                str = str4;
                arrayList2 = arrayList6;
            }
            if (f6 <= 0.0f) {
                f6 = lottieComposition.getEndFrame();
            }
            ArrayList arrayList7 = arrayList2;
            arrayList7.add(new Keyframe(lottieComposition, valueOf, valueOf, null, f, Float.valueOf(f6)));
            arrayList7.add(new Keyframe(lottieComposition, valueOf2, valueOf2, null, f6, Float.valueOf(Float.MAX_VALUE)));
            if (str3.endsWith(".ai") || "ai".equals(str)) {
                lottieComposition.addWarning("Convert your Illustrator layers to shape layers.");
            }
            boolean z7 = z;
            if (z7) {
                if (animatableTransform2 == null) {
                    animatableTransform2 = new AnimatableTransform();
                }
                AnimatableTransform animatableTransform3 = animatableTransform2;
                animatableTransform3.setAutoOrient(z7);
                animatableTransform = animatableTransform3;
            } else {
                animatableTransform = animatableTransform2;
            }
            return new Layer(arrayList4, lottieComposition, str3, j2, layerType, j, str2, arrayList, animatableTransform, i, i2, i3, f2, f3, f4, f5, animatableTextFrame, animatableTextProperties, arrayList7, matteType2, animatableFloatValue, z4, blurEffect, dropShadowEffect, lBlendMode2);
        }
    }

    /* loaded from: classes.dex */
    static /* synthetic */ class 1 {
        static final /* synthetic */ int[] $SwitchMap$com$airbnb$lottie$model$layer$Layer$MatteType;

        static {
            int[] iArr = new int[Layer.MatteType.values().length];
            $SwitchMap$com$airbnb$lottie$model$layer$Layer$MatteType = iArr;
            try {
                iArr[Layer.MatteType.LUMA.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$airbnb$lottie$model$layer$Layer$MatteType[Layer.MatteType.LUMA_INVERTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }
}
