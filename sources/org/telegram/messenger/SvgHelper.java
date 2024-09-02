package org.telegram.messenger;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseArray;
import androidx.core.graphics.ColorUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.SAXParserFactory;
import org.telegram.messenger.SvgHelper;
import org.telegram.ui.ActionBar.Theme;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
/* loaded from: classes3.dex */
public class SvgHelper {
    private static final double[] pow10 = new double[128];

    /* loaded from: classes3.dex */
    public static class Circle {
        float rad;
        float x1;
        float y1;

        public Circle(float f, float f2, float f3) {
            this.x1 = f;
            this.y1 = f2;
            this.rad = f3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Line {
        float x1;
        float x2;
        float y1;
        float y2;

        public Line(float f, float f2, float f3, float f4) {
            this.x1 = f;
            this.y1 = f2;
            this.x2 = f3;
            this.y2 = f4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class NumberParse {
        private int nextCmd;
        private ArrayList<Float> numbers;

        public NumberParse(ArrayList<Float> arrayList, int i) {
            this.numbers = arrayList;
            this.nextCmd = i;
        }

        public int getNextCmd() {
            return this.nextCmd;
        }

        public float getNumber(int i) {
            return this.numbers.get(i).floatValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Oval {
        RectF rect;

        public Oval(RectF rectF) {
            this.rect = rectF;
        }
    }

    /* loaded from: classes3.dex */
    public static class ParserHelper {
        private char current;
        private int n;
        public int pos;
        private CharSequence s;

        public ParserHelper(CharSequence charSequence, int i) {
            this.s = charSequence;
            this.pos = i;
            this.n = charSequence.length();
            this.current = charSequence.charAt(i);
        }

        private char read() {
            int i = this.pos;
            int i2 = this.n;
            if (i < i2) {
                this.pos = i + 1;
            }
            int i3 = this.pos;
            if (i3 == i2) {
                return (char) 0;
            }
            return this.s.charAt(i3);
        }

        private void reportUnexpectedCharacterError(char c) {
            throw new RuntimeException("Unexpected char '" + c + "'.");
        }

        public void advance() {
            this.current = read();
        }

        public float buildFloat(int i, int i2) {
            double d;
            if (i2 < -125 || i == 0) {
                return 0.0f;
            }
            if (i2 >= 128) {
                return i > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
            } else if (i2 == 0) {
                return i;
            } else {
                if (i >= 67108864) {
                    i++;
                }
                double d2 = i;
                double[] dArr = SvgHelper.pow10;
                if (i2 > 0) {
                    double d3 = dArr[i2];
                    Double.isNaN(d2);
                    d = d2 * d3;
                } else {
                    double d4 = dArr[-i2];
                    Double.isNaN(d2);
                    d = d2 / d4;
                }
                return (float) d;
            }
        }

        public float nextFloat() {
            skipWhitespace();
            float parseFloat = parseFloat();
            skipNumberSeparator();
            return parseFloat;
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0025 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0028 A[LOOP:0: B:13:0x0028->B:17:0x0034, LOOP_START] */
        /* JADX WARN: Removed duplicated region for block: B:19:0x0038  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0058  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x0060  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x0099 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00a4  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x00b7  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x00c1 A[LOOP:3: B:62:0x00c1->B:63:0x00c7, LOOP_START] */
        /* JADX WARN: Removed duplicated region for block: B:65:0x00cb  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x00e4  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x00e8  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public float parseFloat() {
            boolean z;
            int i;
            int i2;
            int i3;
            boolean z2;
            char c;
            char read;
            char read2;
            char c2 = this.current;
            int i4 = 0;
            boolean z3 = true;
            if (c2 == '+') {
                z = true;
            } else if (c2 != '-') {
                z = true;
                switch (this.current) {
                    case '.':
                        i = 0;
                        i2 = 0;
                        i3 = 0;
                        z2 = false;
                        if (this.current == '.') {
                            char read3 = read();
                            this.current = read3;
                            switch (read3) {
                                case '0':
                                    if (i == 0) {
                                        while (true) {
                                            char read4 = read();
                                            this.current = read4;
                                            i2--;
                                            switch (read4) {
                                                case '0':
                                                    break;
                                                case '1':
                                                case '2':
                                                case '3':
                                                case '4':
                                                case '5':
                                                case '6':
                                                case '7':
                                                case '8':
                                                case '9':
                                                default:
                                                    if (!z2) {
                                                        return 0.0f;
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                    break;
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    while (true) {
                                        if (i < 9) {
                                            i++;
                                            i3 = (i3 * 10) + (this.current - '0');
                                            i2--;
                                        }
                                        char read5 = read();
                                        this.current = read5;
                                        switch (read5) {
                                        }
                                    }
                                    break;
                                default:
                                    if (!z2) {
                                        reportUnexpectedCharacterError(read3);
                                        return 0.0f;
                                    }
                                    break;
                            }
                        }
                        c = this.current;
                        if (c != 'E' || c == 'e') {
                            read = read();
                            this.current = read;
                            if (read != '+') {
                                if (read != '-') {
                                    switch (read) {
                                        case '0':
                                        case '1':
                                        case '2':
                                        case '3':
                                        case '4':
                                        case '5':
                                        case '6':
                                        case '7':
                                        case '8':
                                        case '9':
                                            break;
                                        default:
                                            reportUnexpectedCharacterError(read);
                                            return 0.0f;
                                    }
                                    switch (this.current) {
                                        case '0':
                                            while (true) {
                                                char read6 = read();
                                                this.current = read6;
                                                switch (read6) {
                                                }
                                            }
                                        case '1':
                                        case '2':
                                        case '3':
                                        case '4':
                                        case '5':
                                        case '6':
                                        case '7':
                                        case '8':
                                        case '9':
                                            int i5 = 0;
                                            while (true) {
                                                if (i4 < 3) {
                                                    i4++;
                                                    i5 = (i5 * 10) + (this.current - '0');
                                                }
                                                char read7 = read();
                                                this.current = read7;
                                                switch (read7) {
                                                    case '0':
                                                    case '1':
                                                    case '2':
                                                    case '3':
                                                    case '4':
                                                    case '5':
                                                    case '6':
                                                    case '7':
                                                    case '8':
                                                    case '9':
                                                        break;
                                                    default:
                                                        i4 = i5;
                                                        break;
                                                }
                                            }
                                    }
                                } else {
                                    z3 = false;
                                }
                            }
                            read2 = read();
                            this.current = read2;
                            switch (read2) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    break;
                                default:
                                    reportUnexpectedCharacterError(read2);
                                    return 0.0f;
                            }
                            switch (this.current) {
                            }
                        }
                        if (!z3) {
                            i4 = -i4;
                        }
                        int i6 = i4 + i2;
                        if (!z) {
                            i3 = -i3;
                        }
                        return buildFloat(i3, i6);
                    case '/':
                    default:
                        return Float.NaN;
                    case '0':
                        while (true) {
                            char read8 = read();
                            this.current = read8;
                            if (read8 != '.' && read8 != 'E' && read8 != 'e') {
                                switch (read8) {
                                    case '0':
                                        break;
                                    case '1':
                                    case '2':
                                    case '3':
                                    case '4':
                                    case '5':
                                    case '6':
                                    case '7':
                                    case '8':
                                    case '9':
                                        break;
                                    default:
                                        return 0.0f;
                                }
                            }
                        }
                        i = 0;
                        i2 = 0;
                        i3 = 0;
                        z2 = true;
                        if (this.current == '.') {
                        }
                        c = this.current;
                        if (c != 'E') {
                        }
                        read = read();
                        this.current = read;
                        if (read != '+') {
                        }
                        read2 = read();
                        this.current = read2;
                        switch (read2) {
                        }
                        switch (this.current) {
                        }
                        if (!z3) {
                        }
                        int i62 = i4 + i2;
                        if (!z) {
                        }
                        return buildFloat(i3, i62);
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        i = 0;
                        i2 = 0;
                        i3 = 0;
                        while (true) {
                            if (i < 9) {
                                i++;
                                i3 = (i3 * 10) + (this.current - '0');
                            } else {
                                i2++;
                            }
                            char read9 = read();
                            this.current = read9;
                            switch (read9) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    break;
                                default:
                                    z2 = true;
                                    if (this.current == '.') {
                                    }
                                    c = this.current;
                                    if (c != 'E') {
                                    }
                                    read = read();
                                    this.current = read;
                                    if (read != '+') {
                                    }
                                    read2 = read();
                                    this.current = read2;
                                    switch (read2) {
                                    }
                                    switch (this.current) {
                                    }
                                    if (!z3) {
                                    }
                                    int i622 = i4 + i2;
                                    if (!z) {
                                    }
                                    return buildFloat(i3, i622);
                            }
                        }
                }
            } else {
                z = false;
            }
            this.current = read();
            switch (this.current) {
            }
        }

        public void skipNumberSeparator() {
            while (true) {
                int i = this.pos;
                if (i >= this.n) {
                    return;
                }
                char charAt = this.s.charAt(i);
                if (charAt != '\t' && charAt != '\n' && charAt != ' ' && charAt != ',') {
                    return;
                }
                advance();
            }
        }

        public void skipWhitespace() {
            while (true) {
                int i = this.pos;
                if (i >= this.n || !Character.isWhitespace(this.s.charAt(i))) {
                    return;
                }
                advance();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Properties {
        Attributes atts;
        ArrayList<StyleSet> styles;

        private Properties(Attributes attributes, HashMap<String, StyleSet> hashMap) {
            this.atts = attributes;
            String stringAttr = SvgHelper.getStringAttr("style", attributes);
            if (stringAttr != null) {
                ArrayList<StyleSet> arrayList = new ArrayList<>();
                this.styles = arrayList;
                arrayList.add(new StyleSet(stringAttr));
                return;
            }
            String stringAttr2 = SvgHelper.getStringAttr("class", attributes);
            if (stringAttr2 != null) {
                this.styles = new ArrayList<>();
                for (String str : stringAttr2.split(" ")) {
                    StyleSet styleSet = hashMap.get(str.trim());
                    if (styleSet != null) {
                        this.styles.add(styleSet);
                    }
                }
            }
        }

        public String getAttr(String str) {
            ArrayList<StyleSet> arrayList = this.styles;
            String str2 = null;
            if (arrayList != null && !arrayList.isEmpty()) {
                int size = this.styles.size();
                for (int i = 0; i < size; i++) {
                    str2 = this.styles.get(i).getStyle(str);
                    if (str2 != null) {
                        break;
                    }
                }
            }
            return str2 == null ? SvgHelper.getStringAttr(str, this.atts) : str2;
        }

        public Float getFloat(String str) {
            String attr = getAttr(str);
            if (attr == null) {
                return null;
            }
            try {
                return Float.valueOf(Float.parseFloat(attr));
            } catch (NumberFormatException unused) {
                return null;
            }
        }

        public Float getFloat(String str, float f) {
            Float f2 = getFloat(str);
            return f2 == null ? Float.valueOf(f) : f2;
        }

        public Integer getHex(String str) {
            String attr = getAttr(str);
            if (attr == null) {
                return null;
            }
            try {
                return Integer.valueOf(Integer.parseInt(attr.substring(1), 16));
            } catch (NumberFormatException unused) {
                return SvgHelper.getColorByName(attr);
            }
        }

        public String getString(String str) {
            return getAttr(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class RoundRect {
        RectF rect;
        float rx;

        public RoundRect(RectF rectF, float f) {
            this.rect = rectF;
            this.rx = f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class SVGHandler extends DefaultHandler {
        private boolean alphaOnly;
        private Bitmap bitmap;
        private boolean boundsMode;
        private Canvas canvas;
        private int desiredHeight;
        private int desiredWidth;
        private SvgDrawable drawable;
        private float globalScale;
        private HashMap<String, StyleSet> globalStyles;
        private Paint paint;
        private Integer paintColor;
        boolean pushed;
        private RectF rect;
        private RectF rectTmp;
        private float scale;
        private StringBuilder styles;

        private SVGHandler(int i, int i2, Integer num, boolean z, float f) {
            this.scale = 1.0f;
            this.paint = new Paint(1);
            this.rect = new RectF();
            this.rectTmp = new RectF();
            this.globalScale = 1.0f;
            this.pushed = false;
            this.globalStyles = new HashMap<>();
            this.globalScale = f;
            this.desiredWidth = i;
            this.desiredHeight = i2;
            this.paintColor = num;
            if (z) {
                this.drawable = new SvgDrawable();
            }
        }

        private void doColor(Properties properties, Integer num, boolean z) {
            Integer num2 = this.paintColor;
            if (num2 != null) {
                this.paint.setColor(num2.intValue());
            } else {
                this.paint.setColor((num.intValue() & 16777215) | (-16777216));
            }
            Float f = properties.getFloat("opacity");
            if (f == null) {
                f = properties.getFloat(z ? "fill-opacity" : "stroke-opacity");
            }
            this.paint.setAlpha(f == null ? NotificationCenter.didClearDatabase : (int) (f.floatValue() * 255.0f));
        }

        private boolean doFill(Properties properties) {
            if ("none".equals(properties.getString("display"))) {
                return false;
            }
            String string = properties.getString("fill");
            if (string != null && string.startsWith("url(#")) {
                string.substring(5, string.length() - 1);
                return false;
            }
            Integer hex = properties.getHex("fill");
            if (hex != null) {
                doColor(properties, hex, true);
                this.paint.setStyle(Paint.Style.FILL);
                return true;
            } else if (properties.getString("fill") == null && properties.getString("stroke") == null) {
                this.paint.setStyle(Paint.Style.FILL);
                Integer num = this.paintColor;
                if (num != null) {
                    this.paint.setColor(num.intValue());
                } else {
                    this.paint.setColor(-16777216);
                }
                return true;
            } else {
                return false;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:22:0x006f  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0077  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean doStroke(Properties properties) {
            Integer hex;
            Paint paint;
            Paint.Cap cap;
            String string;
            Paint paint2;
            Paint.Join join;
            if ("none".equals(properties.getString("display")) || (hex = properties.getHex("stroke")) == null) {
                return false;
            }
            doColor(properties, hex, false);
            Float f = properties.getFloat("stroke-width");
            if (f != null) {
                this.paint.setStrokeWidth(f.floatValue());
            }
            String string2 = properties.getString("stroke-linecap");
            if ("round".equals(string2)) {
                paint = this.paint;
                cap = Paint.Cap.ROUND;
            } else if (!"square".equals(string2)) {
                if ("butt".equals(string2)) {
                    paint = this.paint;
                    cap = Paint.Cap.BUTT;
                }
                string = properties.getString("stroke-linejoin");
                if (!"miter".equals(string)) {
                    paint2 = this.paint;
                    join = Paint.Join.MITER;
                } else if (!"round".equals(string)) {
                    if ("bevel".equals(string)) {
                        paint2 = this.paint;
                        join = Paint.Join.BEVEL;
                    }
                    this.paint.setStyle(Paint.Style.STROKE);
                    return true;
                } else {
                    paint2 = this.paint;
                    join = Paint.Join.ROUND;
                }
                paint2.setStrokeJoin(join);
                this.paint.setStyle(Paint.Style.STROKE);
                return true;
            } else {
                paint = this.paint;
                cap = Paint.Cap.SQUARE;
            }
            paint.setStrokeCap(cap);
            string = properties.getString("stroke-linejoin");
            if (!"miter".equals(string)) {
            }
            paint2.setStrokeJoin(join);
            this.paint.setStyle(Paint.Style.STROKE);
            return true;
        }

        private void popTransform() {
            if (this.pushed) {
                SvgDrawable svgDrawable = this.drawable;
                if (svgDrawable != null) {
                    svgDrawable.addCommand(null);
                } else {
                    this.canvas.restore();
                }
            }
        }

        private void pushTransform(Attributes attributes) {
            String stringAttr = SvgHelper.getStringAttr("transform", attributes);
            boolean z = stringAttr != null;
            this.pushed = z;
            if (z) {
                Matrix parseTransform = SvgHelper.parseTransform(stringAttr);
                SvgDrawable svgDrawable = this.drawable;
                if (svgDrawable != null) {
                    svgDrawable.addCommand(parseTransform);
                    return;
                }
                this.canvas.save();
                this.canvas.concat(parseTransform);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] cArr, int i, int i2) {
            StringBuilder sb = this.styles;
            if (sb != null) {
                sb.append(cArr, i, i2);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endDocument() {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String str, String str2, String str3) {
            int indexOf;
            str2.hashCode();
            char c = 65535;
            switch (str2.hashCode()) {
                case 103:
                    if (str2.equals(ImageLoader.AUTOPLAY_FILTER)) {
                        c = 0;
                        break;
                    }
                    break;
                case 3079438:
                    if (str2.equals("defs")) {
                        c = 1;
                        break;
                    }
                    break;
                case 109780401:
                    if (str2.equals("style")) {
                        c = 2;
                        break;
                    }
                    break;
                case 917656469:
                    if (str2.equals("clipPath")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 3:
                    this.boundsMode = false;
                    return;
                case 2:
                    StringBuilder sb = this.styles;
                    if (sb != null) {
                        String[] split = sb.toString().split("\\}");
                        for (int i = 0; i < split.length; i++) {
                            String replace = split[i].trim().replace("\t", "").replace("\n", "");
                            split[i] = replace;
                            if (replace.length() != 0 && split[i].charAt(0) == '.' && (indexOf = split[i].indexOf(123)) >= 0) {
                                this.globalStyles.put(split[i].substring(1, indexOf).trim(), new StyleSet(split[i].substring(indexOf + 1)));
                            }
                        }
                        this.styles = null;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public Bitmap getBitmap() {
            return this.bitmap;
        }

        public SvgDrawable getDrawable() {
            return this.drawable;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:159:0x0444, code lost:
            if (r2 != null) goto L90;
         */
        /* JADX WARN: Code restructure failed: missing block: B:160:0x0446, code lost:
            r2.addCommand(r1, r18.paint);
         */
        /* JADX WARN: Code restructure failed: missing block: B:161:0x044d, code lost:
            r18.canvas.drawPath(r1, r18.paint);
         */
        /* JADX WARN: Code restructure failed: missing block: B:99:0x029d, code lost:
            if (r2 != null) goto L90;
         */
        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void startElement(String str, String str2, String str3, Attributes attributes) {
            SvgDrawable svgDrawable;
            Path path;
            String stringAttr;
            int i;
            SvgDrawable svgDrawable2;
            Object line;
            if (!this.boundsMode || str2.equals("style")) {
                str2.hashCode();
                char c = 65535;
                switch (str2.hashCode()) {
                    case -1656480802:
                        if (str2.equals("ellipse")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1360216880:
                        if (str2.equals("circle")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -397519558:
                        if (str2.equals("polygon")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 103:
                        if (str2.equals(ImageLoader.AUTOPLAY_FILTER)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 114276:
                        if (str2.equals("svg")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 3079438:
                        if (str2.equals("defs")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 3321844:
                        if (str2.equals("line")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 3433509:
                        if (str2.equals("path")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 3496420:
                        if (str2.equals("rect")) {
                            c = '\b';
                            break;
                        }
                        break;
                    case 109780401:
                        if (str2.equals("style")) {
                            c = '\t';
                            break;
                        }
                        break;
                    case 561938880:
                        if (str2.equals("polyline")) {
                            c = '\n';
                            break;
                        }
                        break;
                    case 917656469:
                        if (str2.equals("clipPath")) {
                            c = 11;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        Float floatAttr = SvgHelper.getFloatAttr("cx", attributes);
                        Float floatAttr2 = SvgHelper.getFloatAttr("cy", attributes);
                        Float floatAttr3 = SvgHelper.getFloatAttr("rx", attributes);
                        Float floatAttr4 = SvgHelper.getFloatAttr("ry", attributes);
                        if (floatAttr == null || floatAttr2 == null || floatAttr3 == null || floatAttr4 == null) {
                            return;
                        }
                        pushTransform(attributes);
                        Properties properties = new Properties(attributes, this.globalStyles);
                        this.rect.set(floatAttr.floatValue() - floatAttr3.floatValue(), floatAttr2.floatValue() - floatAttr4.floatValue(), floatAttr.floatValue() + floatAttr3.floatValue(), floatAttr2.floatValue() + floatAttr4.floatValue());
                        if (doFill(properties)) {
                            SvgDrawable svgDrawable3 = this.drawable;
                            if (svgDrawable3 != null) {
                                svgDrawable3.addCommand(new Oval(this.rect), this.paint);
                            } else {
                                this.canvas.drawOval(this.rect, this.paint);
                            }
                        }
                        if (doStroke(properties)) {
                            SvgDrawable svgDrawable4 = this.drawable;
                            if (svgDrawable4 != null) {
                                svgDrawable4.addCommand(new Oval(this.rect), this.paint);
                            } else {
                                this.canvas.drawOval(this.rect, this.paint);
                            }
                        }
                        popTransform();
                        return;
                    case 1:
                        Float floatAttr5 = SvgHelper.getFloatAttr("cx", attributes);
                        Float floatAttr6 = SvgHelper.getFloatAttr("cy", attributes);
                        Float floatAttr7 = SvgHelper.getFloatAttr("r", attributes);
                        if (floatAttr5 == null || floatAttr6 == null || floatAttr7 == null) {
                            return;
                        }
                        pushTransform(attributes);
                        Properties properties2 = new Properties(attributes, this.globalStyles);
                        if (doFill(properties2)) {
                            SvgDrawable svgDrawable5 = this.drawable;
                            if (svgDrawable5 != null) {
                                svgDrawable5.addCommand(new Circle(floatAttr5.floatValue(), floatAttr6.floatValue(), floatAttr7.floatValue()), this.paint);
                            } else {
                                this.canvas.drawCircle(floatAttr5.floatValue(), floatAttr6.floatValue(), floatAttr7.floatValue(), this.paint);
                            }
                        }
                        if (doStroke(properties2)) {
                            SvgDrawable svgDrawable6 = this.drawable;
                            if (svgDrawable6 != null) {
                                svgDrawable6.addCommand(new Circle(floatAttr5.floatValue(), floatAttr6.floatValue(), floatAttr7.floatValue()), this.paint);
                            } else {
                                this.canvas.drawCircle(floatAttr5.floatValue(), floatAttr6.floatValue(), floatAttr7.floatValue(), this.paint);
                            }
                        }
                        popTransform();
                        return;
                    case 2:
                    case '\n':
                        NumberParse numberParseAttr = SvgHelper.getNumberParseAttr("points", attributes);
                        if (numberParseAttr != null) {
                            Path path2 = new Path();
                            ArrayList arrayList = numberParseAttr.numbers;
                            if (arrayList.size() > 1) {
                                pushTransform(attributes);
                                Properties properties3 = new Properties(attributes, this.globalStyles);
                                path2.moveTo(((Float) arrayList.get(0)).floatValue(), ((Float) arrayList.get(1)).floatValue());
                                for (int i2 = 2; i2 < arrayList.size(); i2 += 2) {
                                    path2.lineTo(((Float) arrayList.get(i2)).floatValue(), ((Float) arrayList.get(i2 + 1)).floatValue());
                                }
                                if (str2.equals("polygon")) {
                                    path2.close();
                                }
                                if (doFill(properties3)) {
                                    SvgDrawable svgDrawable7 = this.drawable;
                                    if (svgDrawable7 != null) {
                                        svgDrawable7.addCommand(path2, this.paint);
                                    } else {
                                        this.canvas.drawPath(path2, this.paint);
                                    }
                                }
                                if (doStroke(properties3)) {
                                    svgDrawable = this.drawable;
                                    path = path2;
                                    break;
                                }
                                popTransform();
                                return;
                            }
                            return;
                        }
                        return;
                    case 3:
                        if (!"bounds".equalsIgnoreCase(SvgHelper.getStringAttr("id", attributes))) {
                            return;
                        }
                        this.boundsMode = true;
                        return;
                    case 4:
                        Float floatAttr8 = SvgHelper.getFloatAttr("width", attributes);
                        Float floatAttr9 = SvgHelper.getFloatAttr("height", attributes);
                        if ((floatAttr8 == null || floatAttr9 == null) && (stringAttr = SvgHelper.getStringAttr("viewBox", attributes)) != null) {
                            String[] split = stringAttr.split(" ");
                            Float valueOf = Float.valueOf(Float.parseFloat(split[2]));
                            floatAttr9 = Float.valueOf(Float.parseFloat(split[3]));
                            floatAttr8 = valueOf;
                        }
                        if (floatAttr8 == null || floatAttr9 == null) {
                            floatAttr8 = Float.valueOf(this.desiredWidth);
                            floatAttr9 = Float.valueOf(this.desiredHeight);
                        }
                        int ceil = (int) Math.ceil(floatAttr8.floatValue());
                        int ceil2 = (int) Math.ceil(floatAttr9.floatValue());
                        if (ceil == 0 || ceil2 == 0) {
                            ceil = this.desiredWidth;
                            ceil2 = this.desiredHeight;
                        } else {
                            int i3 = this.desiredWidth;
                            if (i3 != 0 && (i = this.desiredHeight) != 0) {
                                float f = ceil;
                                float f2 = ceil2;
                                float min = Math.min(i3 / f, i / f2);
                                this.scale = min;
                                ceil = (int) (f * min);
                                ceil2 = (int) (f2 * min);
                            }
                        }
                        SvgDrawable svgDrawable8 = this.drawable;
                        if (svgDrawable8 != null) {
                            svgDrawable8.width = ceil;
                            svgDrawable8.height = ceil2;
                            return;
                        }
                        Bitmap createBitmap = Bitmap.createBitmap(ceil, ceil2, this.alphaOnly ? Bitmap.Config.ALPHA_8 : Bitmap.Config.ARGB_8888);
                        this.bitmap = createBitmap;
                        createBitmap.eraseColor(0);
                        Canvas canvas = new Canvas(this.bitmap);
                        this.canvas = canvas;
                        float f3 = this.scale;
                        if (f3 != 0.0f) {
                            float f4 = this.globalScale * f3;
                            canvas.scale(f4, f4);
                            return;
                        }
                        return;
                    case 5:
                    case 11:
                        this.boundsMode = true;
                        return;
                    case 6:
                        Float floatAttr10 = SvgHelper.getFloatAttr("x1", attributes);
                        Float floatAttr11 = SvgHelper.getFloatAttr("x2", attributes);
                        Float floatAttr12 = SvgHelper.getFloatAttr("y1", attributes);
                        Float floatAttr13 = SvgHelper.getFloatAttr("y2", attributes);
                        if (doStroke(new Properties(attributes, this.globalStyles))) {
                            pushTransform(attributes);
                            svgDrawable2 = this.drawable;
                            if (svgDrawable2 == null) {
                                this.canvas.drawLine(floatAttr10.floatValue(), floatAttr12.floatValue(), floatAttr11.floatValue(), floatAttr13.floatValue(), this.paint);
                                popTransform();
                                return;
                            }
                            line = new Line(floatAttr10.floatValue(), floatAttr12.floatValue(), floatAttr11.floatValue(), floatAttr13.floatValue());
                            svgDrawable2.addCommand(line, this.paint);
                            popTransform();
                            return;
                        }
                        return;
                    case 7:
                        path = SvgHelper.doPath(SvgHelper.getStringAttr("d", attributes));
                        pushTransform(attributes);
                        Properties properties4 = new Properties(attributes, this.globalStyles);
                        if (doFill(properties4)) {
                            SvgDrawable svgDrawable9 = this.drawable;
                            if (svgDrawable9 != null) {
                                svgDrawable9.addCommand(path, this.paint);
                            } else {
                                this.canvas.drawPath(path, this.paint);
                            }
                        }
                        if (doStroke(properties4)) {
                            svgDrawable = this.drawable;
                            break;
                        }
                        popTransform();
                        return;
                    case '\b':
                        Float floatAttr14 = SvgHelper.getFloatAttr("x", attributes);
                        if (floatAttr14 == null) {
                            floatAttr14 = Float.valueOf(0.0f);
                        }
                        Float floatAttr15 = SvgHelper.getFloatAttr("y", attributes);
                        if (floatAttr15 == null) {
                            floatAttr15 = Float.valueOf(0.0f);
                        }
                        Float floatAttr16 = SvgHelper.getFloatAttr("width", attributes);
                        Float floatAttr17 = SvgHelper.getFloatAttr("height", attributes);
                        Float floatAttr18 = SvgHelper.getFloatAttr("rx", attributes, null);
                        pushTransform(attributes);
                        Properties properties5 = new Properties(attributes, this.globalStyles);
                        if (doFill(properties5)) {
                            SvgDrawable svgDrawable10 = this.drawable;
                            if (svgDrawable10 != null) {
                                svgDrawable10.addCommand(floatAttr18 != null ? new RoundRect(new RectF(floatAttr14.floatValue(), floatAttr15.floatValue(), floatAttr14.floatValue() + floatAttr16.floatValue(), floatAttr15.floatValue() + floatAttr17.floatValue()), floatAttr18.floatValue()) : new RectF(floatAttr14.floatValue(), floatAttr15.floatValue(), floatAttr14.floatValue() + floatAttr16.floatValue(), floatAttr15.floatValue() + floatAttr17.floatValue()), this.paint);
                            } else if (floatAttr18 != null) {
                                this.rectTmp.set(floatAttr14.floatValue(), floatAttr15.floatValue(), floatAttr14.floatValue() + floatAttr16.floatValue(), floatAttr15.floatValue() + floatAttr17.floatValue());
                                this.canvas.drawRoundRect(this.rectTmp, floatAttr18.floatValue(), floatAttr18.floatValue(), this.paint);
                            } else {
                                this.canvas.drawRect(floatAttr14.floatValue(), floatAttr15.floatValue(), floatAttr14.floatValue() + floatAttr16.floatValue(), floatAttr15.floatValue() + floatAttr17.floatValue(), this.paint);
                            }
                        }
                        if (doStroke(properties5)) {
                            svgDrawable2 = this.drawable;
                            if (svgDrawable2 != null) {
                                if (floatAttr18 != null) {
                                    svgDrawable2.addCommand(new RoundRect(new RectF(floatAttr14.floatValue(), floatAttr15.floatValue(), floatAttr14.floatValue() + floatAttr16.floatValue(), floatAttr15.floatValue() + floatAttr17.floatValue()), floatAttr18.floatValue()), this.paint);
                                } else {
                                    line = new RectF(floatAttr14.floatValue(), floatAttr15.floatValue(), floatAttr14.floatValue() + floatAttr16.floatValue(), floatAttr15.floatValue() + floatAttr17.floatValue());
                                    svgDrawable2.addCommand(line, this.paint);
                                }
                            } else if (floatAttr18 != null) {
                                this.rectTmp.set(floatAttr14.floatValue(), floatAttr15.floatValue(), floatAttr14.floatValue() + floatAttr16.floatValue(), floatAttr15.floatValue() + floatAttr17.floatValue());
                                this.canvas.drawRoundRect(this.rectTmp, floatAttr18.floatValue(), floatAttr18.floatValue(), this.paint);
                            } else {
                                this.canvas.drawRect(floatAttr14.floatValue(), floatAttr15.floatValue(), floatAttr14.floatValue() + floatAttr16.floatValue(), floatAttr15.floatValue() + floatAttr17.floatValue(), this.paint);
                            }
                        }
                        popTransform();
                        return;
                    case '\t':
                        this.styles = new StringBuilder();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class StyleSet {
        HashMap<String, String> styleMap;

        private StyleSet(String str) {
            this.styleMap = new HashMap<>();
            for (String str2 : str.split(";")) {
                String[] split = str2.split(":");
                if (split.length == 2) {
                    this.styleMap.put(split[0].trim(), split[1].trim());
                }
            }
        }

        private StyleSet(StyleSet styleSet) {
            HashMap<String, String> hashMap = new HashMap<>();
            this.styleMap = hashMap;
            hashMap.putAll(styleSet.styleMap);
        }

        public String getStyle(String str) {
            return this.styleMap.get(str);
        }
    }

    /* loaded from: classes3.dex */
    public static class SvgDrawable extends Drawable {
        private static float gradientWidth;
        private static long lastUpdateTime;
        private static WeakReference<Drawable> shiftDrawable;
        private static Runnable shiftRunnable;
        private static float totalTranslation;
        private Paint backgroundPaint;
        private float colorAlpha;
        private int currentColorKey;
        private Theme.ResourcesProvider currentResourcesProvider;
        protected int height;
        private Integer overrideColor;
        private Paint overridePaint;
        private ImageReceiver parentImageReceiver;
        protected int width;
        private static final int[] parentPosition = new int[2];
        private static boolean lite = LiteMode.isEnabled(32);
        protected ArrayList<Object> commands = new ArrayList<>();
        protected HashMap<Object, Paint> paints = new HashMap<>();
        private final Bitmap[] backgroundBitmap = new Bitmap[3];
        private final Canvas[] backgroundCanvas = new Canvas[3];
        private final LinearGradient[] placeholderGradient = new LinearGradient[3];
        private final Matrix[] placeholderMatrix = new Matrix[3];
        private final int[] currentColor = new int[2];
        private float crossfadeAlpha = 1.0f;
        SparseArray<Paint> overridePaintByPosition = new SparseArray<>();
        private boolean aspectFill = true;
        private boolean aspectCenter = false;

        /* JADX INFO: Access modifiers changed from: private */
        public void addCommand(Object obj) {
            this.commands.add(obj);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addCommand(Object obj, Paint paint) {
            this.commands.add(obj);
            this.paints.put(obj, new Paint(paint));
        }

        public static void updateLiteValues() {
            lite = LiteMode.isEnabled(32);
        }

        public SvgDrawable clone() {
            SvgDrawable svgDrawable = new SvgDrawable();
            for (int i = 0; i < this.commands.size(); i++) {
                svgDrawable.commands.add(this.commands.get(i));
                Paint paint = this.paints.get(this.commands.get(i));
                if (paint != null) {
                    Paint paint2 = new Paint();
                    paint2.setColor(paint.getColor());
                    paint2.setStrokeCap(paint.getStrokeCap());
                    paint2.setStrokeJoin(paint.getStrokeJoin());
                    paint2.setStrokeWidth(paint.getStrokeWidth());
                    paint2.setStyle(paint.getStyle());
                    svgDrawable.paints.put(this.commands.get(i), paint2);
                }
            }
            svgDrawable.width = this.width;
            svgDrawable.height = this.height;
            return svgDrawable;
        }

        public void copyCommandFromPosition(int i) {
            ArrayList<Object> arrayList = this.commands;
            arrayList.add(arrayList.get(i));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            drawInternal(canvas, false, 0, System.currentTimeMillis(), getBounds().left, getBounds().top, getBounds().width(), getBounds().height());
        }

        public void drawInternal(Canvas canvas, boolean z, int i, long j, float f, float f2, float f3, float f4) {
            long j2;
            int i2;
            int i3 = this.currentColorKey;
            if (i3 >= 0) {
                setupGradient(i3, this.currentResourcesProvider, this.colorAlpha, z);
            }
            float scale = getScale((int) f3, (int) f4);
            if (this.placeholderGradient[i] != null) {
                float f5 = gradientWidth;
                if (f5 > 0.0f && lite) {
                    if (z) {
                        long j3 = j - lastUpdateTime;
                        j2 = j3 <= 64 ? j3 : 64L;
                        if (j2 > 0) {
                            lastUpdateTime = j;
                            totalTranslation += (((float) j2) * f5) / 1800.0f;
                            while (true) {
                                float f6 = totalTranslation;
                                float f7 = gradientWidth * 2.0f;
                                if (f6 < f7) {
                                    break;
                                }
                                totalTranslation = f6 - f7;
                            }
                        }
                    } else if (shiftRunnable == null || shiftDrawable.get() == this) {
                        long j4 = j - lastUpdateTime;
                        j2 = j4 <= 64 ? j4 : 64L;
                        long j5 = j2 >= 0 ? j2 : 0L;
                        lastUpdateTime = j;
                        float f8 = totalTranslation + ((((float) j5) * gradientWidth) / 1800.0f);
                        while (true) {
                            totalTranslation = f8;
                            float f9 = totalTranslation;
                            float f10 = gradientWidth;
                            if (f9 < f10 / 2.0f) {
                                break;
                            }
                            f8 = f9 - f10;
                        }
                        shiftDrawable = new WeakReference<>(this);
                        Runnable runnable = shiftRunnable;
                        if (runnable != null) {
                            AndroidUtilities.cancelRunOnUIThread(runnable);
                        }
                        Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.SvgHelper$SvgDrawable$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                SvgHelper.SvgDrawable.shiftRunnable = null;
                            }
                        };
                        shiftRunnable = runnable2;
                        AndroidUtilities.runOnUIThread(runnable2, ((int) (1000.0f / AndroidUtilities.screenRefreshRate)) - 1);
                    }
                    ImageReceiver imageReceiver = this.parentImageReceiver;
                    if (imageReceiver == null || z) {
                        i2 = 0;
                    } else {
                        int[] iArr = parentPosition;
                        imageReceiver.getParentPosition(iArr);
                        i2 = iArr[0];
                    }
                    int i4 = z ? i + 1 : 0;
                    Matrix matrix = this.placeholderMatrix[i4];
                    if (matrix != null) {
                        matrix.reset();
                        Matrix[] matrixArr = this.placeholderMatrix;
                        (z ? matrixArr[i4] : matrixArr[i4]).postTranslate(((-i2) + totalTranslation) - f, 0.0f);
                        float f11 = 1.0f / scale;
                        this.placeholderMatrix[i4].postScale(f11, f11);
                        this.placeholderGradient[i4].setLocalMatrix(this.placeholderMatrix[i4]);
                        ImageReceiver imageReceiver2 = this.parentImageReceiver;
                        if (imageReceiver2 != null && !z) {
                            imageReceiver2.invalidate();
                        }
                    }
                }
            }
            canvas.save();
            canvas.translate(f, f2);
            if (!this.aspectFill || this.aspectCenter) {
                canvas.translate((f3 - (this.width * scale)) / 2.0f, (f4 - (this.height * scale)) / 2.0f);
            }
            canvas.scale(scale, scale);
            int size = this.commands.size();
            for (int i5 = 0; i5 < size; i5++) {
                Object obj = this.commands.get(i5);
                if (obj instanceof Matrix) {
                    canvas.save();
                    canvas.concat((Matrix) obj);
                } else if (obj == null) {
                    canvas.restore();
                } else {
                    Paint paint = this.overridePaintByPosition.get(i5);
                    if (paint == null) {
                        paint = this.overridePaint;
                    }
                    if (z) {
                        paint = this.backgroundPaint;
                    } else if (paint == null) {
                        paint = this.paints.get(obj);
                    }
                    int alpha = paint.getAlpha();
                    paint.setAlpha((int) (this.crossfadeAlpha * alpha));
                    if (obj instanceof Path) {
                        canvas.drawPath((Path) obj, paint);
                    } else if (obj instanceof Rect) {
                        canvas.drawRect((Rect) obj, paint);
                    } else if (obj instanceof RectF) {
                        canvas.drawRect((RectF) obj, paint);
                    } else if (obj instanceof Line) {
                        Line line = (Line) obj;
                        canvas.drawLine(line.x1, line.y1, line.x2, line.y2, paint);
                    } else if (obj instanceof Circle) {
                        Circle circle = (Circle) obj;
                        canvas.drawCircle(circle.x1, circle.y1, circle.rad, paint);
                    } else if (obj instanceof Oval) {
                        canvas.drawOval(((Oval) obj).rect, paint);
                    } else if (obj instanceof RoundRect) {
                        RoundRect roundRect = (RoundRect) obj;
                        RectF rectF = roundRect.rect;
                        float f12 = roundRect.rx;
                        canvas.drawRoundRect(rectF, f12, f12, paint);
                    }
                    paint.setAlpha(alpha);
                }
            }
            canvas.restore();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.width;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.height;
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        public float getScale(int i, int i2) {
            float f = i / this.width;
            float f2 = i2 / this.height;
            return this.aspectFill ? Math.max(f, f2) : Math.min(f, f2);
        }

        public void overrideWidthAndHeight(int i, int i2) {
            this.width = i;
            this.height = i2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.crossfadeAlpha = i / 255.0f;
        }

        public void setAspectCenter(boolean z) {
            this.aspectCenter = z;
        }

        public void setAspectFill(boolean z) {
            this.aspectFill = z;
        }

        public void setColor(int i) {
            this.overrideColor = Integer.valueOf(i);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public void setColorKey(int i) {
            this.currentColorKey = i;
        }

        public void setColorKey(int i, Theme.ResourcesProvider resourcesProvider) {
            this.currentColorKey = i;
            this.currentResourcesProvider = resourcesProvider;
        }

        public void setPaint(Paint paint) {
            this.overridePaint = paint;
        }

        public void setPaint(Paint paint, int i) {
            this.overridePaintByPosition.put(i, paint);
        }

        public void setParent(ImageReceiver imageReceiver) {
            this.parentImageReceiver = imageReceiver;
        }

        public void setupGradient(int i, float f, boolean z) {
            setupGradient(i, null, f, z);
        }

        public void setupGradient(int i, Theme.ResourcesProvider resourcesProvider, float f, boolean z) {
            Shader bitmapShader;
            Integer num = this.overrideColor;
            int color = num == null ? Theme.getColor(i, resourcesProvider) : num.intValue();
            this.currentResourcesProvider = resourcesProvider;
            int[] iArr = this.currentColor;
            if (iArr[z ? 1 : 0] != color) {
                this.colorAlpha = f;
                this.currentColorKey = i;
                iArr[z ? 1 : 0] = color;
                gradientWidth = AndroidUtilities.displaySize.x * 2;
                if (!lite) {
                    int alphaComponent = ColorUtils.setAlphaComponent(color, 70);
                    if (z) {
                        if (this.backgroundPaint == null) {
                            this.backgroundPaint = new Paint(1);
                        }
                        this.backgroundPaint.setShader(null);
                        this.backgroundPaint.setColor(alphaComponent);
                        return;
                    }
                    for (Paint paint : this.paints.values()) {
                        paint.setShader(null);
                        paint.setColor(alphaComponent);
                    }
                    return;
                }
                float dp = AndroidUtilities.dp(180.0f) / gradientWidth;
                int argb = Color.argb((int) ((Color.alpha(color) / 2) * this.colorAlpha), Color.red(color), Color.green(color), Color.blue(color));
                float f2 = (1.0f - dp) / 2.0f;
                LinearGradient[] linearGradientArr = this.placeholderGradient;
                float f3 = dp / 2.0f;
                Shader.TileMode tileMode = Shader.TileMode.REPEAT;
                linearGradientArr[z ? 1 : 0] = new LinearGradient(0.0f, 0.0f, gradientWidth, 0.0f, new int[]{0, 0, argb, 0, 0}, new float[]{0.0f, f2 - f3, f2, f3 + f2, 1.0f}, tileMode);
                int i2 = Build.VERSION.SDK_INT;
                if (i2 >= 28) {
                    bitmapShader = new LinearGradient(0.0f, 0.0f, gradientWidth, 0.0f, new int[]{argb, argb}, (float[]) null, tileMode);
                } else {
                    Bitmap[] bitmapArr = this.backgroundBitmap;
                    if (bitmapArr[z ? 1 : 0] == null) {
                        bitmapArr[z ? 1 : 0] = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                        this.backgroundCanvas[z ? 1 : 0] = new Canvas(this.backgroundBitmap[z ? 1 : 0]);
                    }
                    this.backgroundCanvas[z ? 1 : 0].drawColor(argb);
                    bitmapShader = new BitmapShader(this.backgroundBitmap[z ? 1 : 0], tileMode, tileMode);
                }
                this.placeholderMatrix[z ? 1 : 0] = new Matrix();
                this.placeholderGradient[z ? 1 : 0].setLocalMatrix(this.placeholderMatrix[z ? 1 : 0]);
                if (z) {
                    if (this.backgroundPaint == null) {
                        this.backgroundPaint = new Paint(1);
                    }
                    if (i2 <= 22) {
                        this.backgroundPaint.setShader(bitmapShader);
                        return;
                    } else {
                        this.backgroundPaint.setShader(new ComposeShader(this.placeholderGradient[z ? 1 : 0], bitmapShader, PorterDuff.Mode.ADD));
                        return;
                    }
                }
                for (Paint paint2 : this.paints.values()) {
                    if (Build.VERSION.SDK_INT <= 22) {
                        paint2.setShader(bitmapShader);
                    } else {
                        paint2.setShader(new ComposeShader(this.placeholderGradient[z ? 1 : 0], bitmapShader, PorterDuff.Mode.ADD));
                    }
                }
            }
        }
    }

    static {
        int i = 0;
        while (true) {
            double[] dArr = pow10;
            if (i >= dArr.length) {
                return;
            }
            dArr[i] = Math.pow(10.0d, i);
            i++;
        }
    }

    public static String decompress(byte[] bArr) {
        try {
            StringBuilder sb = new StringBuilder(bArr.length * 2);
            sb.append('M');
            for (byte b : bArr) {
                int i = b & 255;
                if (i >= 192) {
                    sb.append("AACAAAAHAAALMAAAQASTAVAAAZaacaaaahaaalmaaaqastava.az0123456789-,".charAt(i - 192));
                } else {
                    if (i >= 128) {
                        sb.append(',');
                    } else if (i >= 64) {
                        sb.append('-');
                    }
                    sb.append(b & 63);
                }
            }
            sb.append('z');
            return sb.toString();
        } catch (Exception e) {
            FileLog.e(e);
            return "";
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0064, code lost:
        if (r4 != 'V') goto L9;
     */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0079 A[PHI: r5 r6 r12 r13 
      PHI: (r5v18 float) = (r5v1 float), (r5v5 float), (r5v6 float), (r5v7 float), (r5v8 float), (r5v10 float), (r5v1 float), (r5v1 float) binds: [B:40:0x0076, B:64:0x011a, B:63:0x0114, B:60:0x0106, B:59:0x00ff, B:55:0x00e5, B:48:0x009e, B:47:0x0098] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r6v18 float) = (r6v1 float), (r6v1 float), (r6v1 float), (r6v5 float), (r6v6 float), (r6v8 float), (r6v12 float), (r6v13 float) binds: [B:40:0x0076, B:64:0x011a, B:63:0x0114, B:60:0x0106, B:59:0x00ff, B:55:0x00e5, B:48:0x009e, B:47:0x0098] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r12v11 float) = (r12v1 float), (r12v1 float), (r12v1 float), (r12v1 float), (r12v1 float), (r12v5 float), (r12v1 float), (r12v1 float) binds: [B:40:0x0076, B:64:0x011a, B:63:0x0114, B:60:0x0106, B:59:0x00ff, B:55:0x00e5, B:48:0x009e, B:47:0x0098] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r13v11 float) = (r13v1 float), (r13v1 float), (r13v1 float), (r13v1 float), (r13v1 float), (r13v5 float), (r13v1 float), (r13v1 float) binds: [B:40:0x0076, B:64:0x011a, B:63:0x0114, B:60:0x0106, B:59:0x00ff, B:55:0x00e5, B:48:0x009e, B:47:0x0098] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0160  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x019d  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Path doPath(String str) {
        char c;
        boolean z;
        float f;
        float f2;
        int length = str.length();
        ParserHelper parserHelper = new ParserHelper(str, 0);
        parserHelper.skipWhitespace();
        Path path = new Path();
        char c2 = 0;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        while (true) {
            int i = parserHelper.pos;
            if (i >= length) {
                return path;
            }
            char charAt = str.charAt(i);
            if (charAt != '+' && charAt != '-') {
                switch (charAt) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        break;
                    default:
                        parserHelper.advance();
                        c2 = charAt;
                        c = c2;
                        break;
                }
                z = true;
                switch (c2) {
                    case 'A':
                    case 'a':
                        float nextFloat = parserHelper.nextFloat();
                        float nextFloat2 = parserHelper.nextFloat();
                        f = f5;
                        f2 = f6;
                        drawArc(path, f3, f4, nextFloat, nextFloat2, parserHelper.nextFloat(), parserHelper.nextFloat(), parserHelper.nextFloat(), (int) parserHelper.nextFloat(), (int) parserHelper.nextFloat());
                        f6 = f7;
                        f5 = f8;
                        f3 = nextFloat;
                        f4 = nextFloat2;
                        z = false;
                        break;
                    case 'C':
                    case 'c':
                        float nextFloat3 = parserHelper.nextFloat();
                        float nextFloat4 = parserHelper.nextFloat();
                        float nextFloat5 = parserHelper.nextFloat();
                        float nextFloat6 = parserHelper.nextFloat();
                        float nextFloat7 = parserHelper.nextFloat();
                        float nextFloat8 = parserHelper.nextFloat();
                        if (c2 == 'c') {
                            nextFloat3 += f3;
                            nextFloat5 += f3;
                            nextFloat7 += f3;
                            nextFloat4 += f4;
                            nextFloat6 += f4;
                            nextFloat8 += f4;
                        }
                        float f9 = nextFloat5;
                        float f10 = nextFloat6;
                        float f11 = nextFloat7;
                        path.cubicTo(nextFloat3, nextFloat4, f9, f10, f11, nextFloat8);
                        f3 = f11;
                        f = f5;
                        f4 = nextFloat8;
                        f5 = f10;
                        f2 = f6;
                        f6 = f9;
                        break;
                    case 'H':
                    case 'h':
                        float nextFloat9 = parserHelper.nextFloat();
                        if (c2 == 'h') {
                            path.rLineTo(nextFloat9, 0.0f);
                            f3 += nextFloat9;
                        } else {
                            path.lineTo(nextFloat9, f4);
                            f3 = nextFloat9;
                        }
                        f = f5;
                        f2 = f6;
                        f6 = f7;
                        f5 = f8;
                        z = false;
                        break;
                    case 'L':
                    case 'l':
                        float nextFloat10 = parserHelper.nextFloat();
                        float nextFloat11 = parserHelper.nextFloat();
                        if (c2 == 'l') {
                            path.rLineTo(nextFloat10, nextFloat11);
                            f3 += nextFloat10;
                            f4 += nextFloat11;
                        } else {
                            path.lineTo(nextFloat10, nextFloat11);
                            f3 = nextFloat10;
                            f4 = nextFloat11;
                        }
                        f = f5;
                        f2 = f6;
                        f6 = f7;
                        f5 = f8;
                        z = false;
                        break;
                    case 'M':
                    case 'm':
                        float nextFloat12 = parserHelper.nextFloat();
                        float nextFloat13 = parserHelper.nextFloat();
                        if (c2 == 'm') {
                            f6 += nextFloat12;
                            f5 += nextFloat13;
                            path.rMoveTo(nextFloat12, nextFloat13);
                            f3 += nextFloat12;
                            f4 += nextFloat13;
                            f = f5;
                            f2 = f6;
                            f6 = f7;
                            f5 = f8;
                            z = false;
                            break;
                        } else {
                            path.moveTo(nextFloat12, nextFloat13);
                            f3 = nextFloat12;
                            f2 = f3;
                            f = nextFloat13;
                            f4 = f;
                            f6 = f7;
                            f5 = f8;
                            z = false;
                        }
                    case 'S':
                    case 's':
                        float nextFloat14 = parserHelper.nextFloat();
                        float nextFloat15 = parserHelper.nextFloat();
                        float nextFloat16 = parserHelper.nextFloat();
                        float nextFloat17 = parserHelper.nextFloat();
                        if (c2 == 's') {
                            nextFloat14 += f3;
                            nextFloat16 += f3;
                            nextFloat15 += f4;
                            nextFloat17 += f4;
                        }
                        float f12 = nextFloat15;
                        float f13 = nextFloat16;
                        float f14 = nextFloat17;
                        path.cubicTo((f3 * 2.0f) - f7, (f4 * 2.0f) - f8, nextFloat14, f12, f13, f14);
                        f3 = f13;
                        f4 = f14;
                        f2 = f6;
                        f6 = nextFloat14;
                        f = f5;
                        f5 = f12;
                        break;
                    case 'V':
                    case 'v':
                        float nextFloat18 = parserHelper.nextFloat();
                        if (c2 == 'v') {
                            path.rLineTo(0.0f, nextFloat18);
                            f4 += nextFloat18;
                        } else {
                            path.lineTo(f3, nextFloat18);
                            f4 = nextFloat18;
                        }
                        f = f5;
                        f2 = f6;
                        f6 = f7;
                        f5 = f8;
                        z = false;
                        break;
                    case 'Z':
                    case 'z':
                        path.close();
                        path.moveTo(f6, f5);
                        f = f5;
                        f4 = f;
                        f3 = f6;
                        f2 = f3;
                        break;
                    default:
                        f = f5;
                        f2 = f6;
                        f6 = f7;
                        f5 = f8;
                        z = false;
                        break;
                }
                if (z) {
                    f8 = f5;
                    f7 = f6;
                } else {
                    f7 = f3;
                    f8 = f4;
                }
                parserHelper.skipWhitespace();
                f5 = f;
                f6 = f2;
                c2 = c;
            }
            if (c2 == 'm' || c2 == 'M') {
                c = c2;
                c2 = (char) (c2 - 1);
                z = true;
                switch (c2) {
                    case 'A':
                    case 'a':
                        break;
                    case 'C':
                    case 'c':
                        break;
                    case 'H':
                    case 'h':
                        break;
                    case 'L':
                    case 'l':
                        break;
                    case 'M':
                    case 'm':
                        break;
                    case 'S':
                    case 's':
                        break;
                    case 'V':
                    case 'v':
                        break;
                    case 'Z':
                    case 'z':
                        break;
                }
                if (z) {
                }
                parserHelper.skipWhitespace();
                f5 = f;
                f6 = f2;
                c2 = c;
            } else {
                if (c2 != 'c') {
                    if (c2 != 'C') {
                        if (c2 != 'l') {
                            if (c2 != 'L') {
                                if (c2 != 's') {
                                    if (c2 != 'S') {
                                        if (c2 != 'h') {
                                            if (c2 != 'H') {
                                                if (c2 != 'v') {
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                c = c2;
                z = true;
                switch (c2) {
                    case 'A':
                    case 'a':
                        break;
                    case 'C':
                    case 'c':
                        break;
                    case 'H':
                    case 'h':
                        break;
                    case 'L':
                    case 'l':
                        break;
                    case 'M':
                    case 'm':
                        break;
                    case 'S':
                    case 's':
                        break;
                    case 'V':
                    case 'v':
                        break;
                    case 'Z':
                    case 'z':
                        break;
                }
                if (z) {
                }
                parserHelper.skipWhitespace();
                f5 = f;
                f6 = f2;
                c2 = c;
            }
        }
    }

    private static void drawArc(Path path, float f, float f2, float f3, float f4, float f5, float f6, float f7, int i, int i2) {
    }

    public static Bitmap getBitmap(int i, int i2, int i3, int i4) {
        return getBitmap(i, i2, i3, i4, 1.0f);
    }

    public static Bitmap getBitmap(int i, int i2, int i3, int i4, float f) {
        try {
            InputStream openRawResource = ApplicationLoader.applicationContext.getResources().openRawResource(i);
            XMLReader xMLReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            SVGHandler sVGHandler = new SVGHandler(i2, i3, Integer.valueOf(i4), false, f);
            xMLReader.setContentHandler(sVGHandler);
            xMLReader.parse(new InputSource(openRawResource));
            Bitmap bitmap = sVGHandler.getBitmap();
            if (openRawResource != null) {
                openRawResource.close();
            }
            return bitmap;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static Bitmap getBitmap(File file, int i, int i2, boolean z) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XMLReader xMLReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            SVGHandler sVGHandler = new SVGHandler(i, i2, z ? -1 : null, false, 1.0f);
            if (!z) {
                sVGHandler.alphaOnly = true;
            }
            xMLReader.setContentHandler(sVGHandler);
            xMLReader.parse(new InputSource(fileInputStream));
            Bitmap bitmap = sVGHandler.getBitmap();
            fileInputStream.close();
            return bitmap;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static Bitmap getBitmap(InputStream inputStream, int i, int i2, boolean z) {
        try {
            XMLReader xMLReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            SVGHandler sVGHandler = new SVGHandler(i, i2, z ? -1 : null, false, 1.0f);
            xMLReader.setContentHandler(sVGHandler);
            xMLReader.parse(new InputSource(inputStream));
            return sVGHandler.getBitmap();
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static Bitmap getBitmap(String str, int i, int i2, boolean z) {
        try {
            XMLReader xMLReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            SVGHandler sVGHandler = new SVGHandler(i, i2, z ? -1 : null, false, 1.0f);
            xMLReader.setContentHandler(sVGHandler);
            xMLReader.parse(new InputSource(new StringReader(str)));
            return sVGHandler.getBitmap();
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static Bitmap getBitmapByPathOnly(String str, int i, int i2, int i3, int i4) {
        try {
            Path doPath = doPath(str);
            Bitmap createBitmap = Bitmap.createBitmap(i3, i4, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.scale(i3 / i, i4 / i2);
            Paint paint = new Paint();
            paint.setColor(-1);
            canvas.drawPath(doPath, paint);
            return createBitmap;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static Integer getColorByName(String str) {
        char c;
        int i;
        String lowerCase = str.toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase.hashCode()) {
            case -734239628:
                if (lowerCase.equals("yellow")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 112785:
                if (lowerCase.equals("red")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3027034:
                if (lowerCase.equals("blue")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3068707:
                if (lowerCase.equals("cyan")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 3181155:
                if (lowerCase.equals("gray")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 93818879:
                if (lowerCase.equals("black")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 98619139:
                if (lowerCase.equals("green")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 113101865:
                if (lowerCase.equals("white")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 828922025:
                if (lowerCase.equals("magenta")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                i = -256;
                break;
            case 1:
                i = -65536;
                break;
            case 2:
                i = -16776961;
                break;
            case 3:
                i = -16711681;
                break;
            case 4:
                i = -7829368;
                break;
            case 5:
                i = -16777216;
                break;
            case 6:
                i = -16711936;
                break;
            case 7:
                return -1;
            case '\b':
                i = -65281;
                break;
            default:
                return null;
        }
        return Integer.valueOf(i);
    }

    public static SvgDrawable getDrawable(int i, Integer num) {
        try {
            XMLReader xMLReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            SVGHandler sVGHandler = new SVGHandler(0, 0, num, true, 1.0f);
            xMLReader.setContentHandler(sVGHandler);
            xMLReader.parse(new InputSource(ApplicationLoader.applicationContext.getResources().openRawResource(i)));
            return sVGHandler.getDrawable();
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static SvgDrawable getDrawable(String str) {
        try {
            XMLReader xMLReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            SVGHandler sVGHandler = new SVGHandler(0, 0, null, true, 1.0f);
            xMLReader.setContentHandler(sVGHandler);
            xMLReader.parse(new InputSource(new StringReader(str)));
            return sVGHandler.getDrawable();
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static SvgDrawable getDrawableByPath(Path path, int i, int i2) {
        try {
            SvgDrawable svgDrawable = new SvgDrawable();
            svgDrawable.commands.add(path);
            svgDrawable.paints.put(path, new Paint(1));
            svgDrawable.width = i;
            svgDrawable.height = i2;
            return svgDrawable;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static SvgDrawable getDrawableByPath(String str, int i, int i2) {
        try {
            Path doPath = doPath(str);
            SvgDrawable svgDrawable = new SvgDrawable();
            svgDrawable.commands.add(doPath);
            svgDrawable.paints.put(doPath, new Paint(1));
            svgDrawable.width = i;
            svgDrawable.height = i2;
            return svgDrawable;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Float getFloatAttr(String str, Attributes attributes) {
        return getFloatAttr(str, attributes, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Float getFloatAttr(String str, Attributes attributes, Float f) {
        String stringAttr = getStringAttr(str, attributes);
        if (stringAttr == null) {
            return f;
        }
        if (stringAttr.endsWith("px")) {
            stringAttr = stringAttr.substring(0, stringAttr.length() - 2);
        } else if (stringAttr.endsWith("mm")) {
            return null;
        }
        return Float.valueOf(Float.parseFloat(stringAttr));
    }

    private static Integer getHexAttr(String str, Attributes attributes) {
        String stringAttr = getStringAttr(str, attributes);
        if (stringAttr == null) {
            return null;
        }
        try {
            return Integer.valueOf(Integer.parseInt(stringAttr.substring(1), 16));
        } catch (NumberFormatException unused) {
            return getColorByName(stringAttr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NumberParse getNumberParseAttr(String str, Attributes attributes) {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            if (attributes.getLocalName(i).equals(str)) {
                return parseNumbers(attributes.getValue(i));
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getStringAttr(String str, Attributes attributes) {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            if (attributes.getLocalName(i).equals(str)) {
                return attributes.getValue(i);
            }
        }
        return null;
    }

    private static NumberParse parseNumbers(String str) {
        int length = str.length();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        boolean z = false;
        for (int i2 = 1; i2 < length; i2++) {
            if (z) {
                z = false;
            } else {
                char charAt = str.charAt(i2);
                switch (charAt) {
                    case '\t':
                    case '\n':
                    case ' ':
                    case ',':
                    case '-':
                        if (charAt == '-' && str.charAt(i2 - 1) == 'e') {
                            break;
                        } else {
                            String substring = str.substring(i, i2);
                            if (substring.trim().length() > 0) {
                                arrayList.add(Float.valueOf(Float.parseFloat(substring)));
                                if (charAt == '-') {
                                    i = i2;
                                    break;
                                } else {
                                    i = i2 + 1;
                                    z = true;
                                    break;
                                }
                            } else {
                                i++;
                                continue;
                            }
                        }
                    case ')':
                    case 'A':
                    case 'C':
                    case 'H':
                    case 'L':
                    case 'M':
                    case 'Q':
                    case 'S':
                    case 'T':
                    case 'V':
                    case 'Z':
                    case 'a':
                    case 'c':
                    case 'h':
                    case 'l':
                    case 'm':
                    case 'q':
                    case 's':
                    case 't':
                    case 'v':
                    case 'z':
                        String substring2 = str.substring(i, i2);
                        if (substring2.trim().length() > 0) {
                            arrayList.add(Float.valueOf(Float.parseFloat(substring2)));
                        }
                        return new NumberParse(arrayList, i2);
                }
            }
        }
        String substring3 = str.substring(i);
        if (substring3.length() > 0) {
            try {
                arrayList.add(Float.valueOf(Float.parseFloat(substring3)));
            } catch (NumberFormatException unused) {
            }
            i = str.length();
        }
        return new NumberParse(arrayList, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Matrix parseTransform(String str) {
        float f;
        if (str.startsWith("matrix(")) {
            NumberParse parseNumbers = parseNumbers(str.substring(7));
            if (parseNumbers.numbers.size() == 6) {
                Matrix matrix = new Matrix();
                matrix.setValues(new float[]{((Float) parseNumbers.numbers.get(0)).floatValue(), ((Float) parseNumbers.numbers.get(2)).floatValue(), ((Float) parseNumbers.numbers.get(4)).floatValue(), ((Float) parseNumbers.numbers.get(1)).floatValue(), ((Float) parseNumbers.numbers.get(3)).floatValue(), ((Float) parseNumbers.numbers.get(5)).floatValue(), 0.0f, 0.0f, 1.0f});
                return matrix;
            }
            return null;
        } else if (str.startsWith("translate(")) {
            NumberParse parseNumbers2 = parseNumbers(str.substring(10));
            if (parseNumbers2.numbers.size() > 0) {
                float floatValue = ((Float) parseNumbers2.numbers.get(0)).floatValue();
                r4 = parseNumbers2.numbers.size() > 1 ? ((Float) parseNumbers2.numbers.get(1)).floatValue() : 0.0f;
                Matrix matrix2 = new Matrix();
                matrix2.postTranslate(floatValue, r4);
                return matrix2;
            }
            return null;
        } else if (str.startsWith("scale(")) {
            NumberParse parseNumbers3 = parseNumbers(str.substring(6));
            if (parseNumbers3.numbers.size() > 0) {
                float floatValue2 = ((Float) parseNumbers3.numbers.get(0)).floatValue();
                r4 = parseNumbers3.numbers.size() > 1 ? ((Float) parseNumbers3.numbers.get(1)).floatValue() : 0.0f;
                Matrix matrix3 = new Matrix();
                matrix3.postScale(floatValue2, r4);
                return matrix3;
            }
            return null;
        } else if (str.startsWith("skewX(")) {
            NumberParse parseNumbers4 = parseNumbers(str.substring(6));
            if (parseNumbers4.numbers.size() > 0) {
                float floatValue3 = ((Float) parseNumbers4.numbers.get(0)).floatValue();
                Matrix matrix4 = new Matrix();
                matrix4.postSkew((float) Math.tan(floatValue3), 0.0f);
                return matrix4;
            }
            return null;
        } else if (str.startsWith("skewY(")) {
            NumberParse parseNumbers5 = parseNumbers(str.substring(6));
            if (parseNumbers5.numbers.size() > 0) {
                float floatValue4 = ((Float) parseNumbers5.numbers.get(0)).floatValue();
                Matrix matrix5 = new Matrix();
                matrix5.postSkew(0.0f, (float) Math.tan(floatValue4));
                return matrix5;
            }
            return null;
        } else if (str.startsWith("rotate(")) {
            NumberParse parseNumbers6 = parseNumbers(str.substring(7));
            if (parseNumbers6.numbers.size() > 0) {
                float floatValue5 = ((Float) parseNumbers6.numbers.get(0)).floatValue();
                if (parseNumbers6.numbers.size() > 2) {
                    float floatValue6 = ((Float) parseNumbers6.numbers.get(1)).floatValue();
                    r4 = ((Float) parseNumbers6.numbers.get(2)).floatValue();
                    f = floatValue6;
                } else {
                    f = 0.0f;
                }
                Matrix matrix6 = new Matrix();
                matrix6.postTranslate(f, r4);
                matrix6.postRotate(floatValue5);
                matrix6.postTranslate(-f, -r4);
                return matrix6;
            }
            return null;
        } else {
            return null;
        }
    }
}
