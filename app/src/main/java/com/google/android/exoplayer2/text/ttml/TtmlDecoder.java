package com.google.android.exoplayer2.text.ttml;

import android.text.Layout;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.ColorParser;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/* loaded from: classes3.dex */
public final class TtmlDecoder extends SimpleSubtitleDecoder {
    private static final String ATTR_BEGIN = "begin";
    private static final String ATTR_DURATION = "dur";
    private static final String ATTR_END = "end";
    private static final String ATTR_IMAGE = "backgroundImage";
    private static final String ATTR_REGION = "region";
    private static final String ATTR_STYLE = "style";
    private static final int DEFAULT_FRAME_RATE = 30;
    private static final String TAG = "TtmlDecoder";
    private static final String TTP = "http://www.w3.org/ns/ttml#parameter";
    private final XmlPullParserFactory xmlParserFactory;
    private static final Pattern CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
    private static final Pattern OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
    private static final Pattern FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
    private static final Pattern PERCENTAGE_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)% (\\d+\\.?\\d*?)%$");
    private static final Pattern PIXEL_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)px (\\d+\\.?\\d*?)px$");
    private static final Pattern CELL_RESOLUTION = Pattern.compile("^(\\d+) (\\d+)$");
    private static final FrameAndTickRate DEFAULT_FRAME_AND_TICK_RATE = new FrameAndTickRate(30.0f, 1, 1);
    private static final CellResolution DEFAULT_CELL_RESOLUTION = new CellResolution(32, 15);

    public TtmlDecoder() {
        super(TAG);
        try {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            this.xmlParserFactory = newInstance;
            newInstance.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    @Override // com.google.android.exoplayer2.text.SimpleSubtitleDecoder
    protected Subtitle decode(byte[] bytes, int length, boolean reset) throws SubtitleDecoderException {
        ByteArrayInputStream inputStream;
        ArrayDeque<TtmlNode> nodeStack;
        Map<String, TtmlStyle> globalStyles;
        TtsExtent ttsExtent;
        CellResolution cellResolution;
        FrameAndTickRate frameAndTickRate;
        Map<String, TtmlStyle> globalStyles2;
        FrameAndTickRate frameAndTickRate2;
        try {
            XmlPullParser xmlParser = this.xmlParserFactory.newPullParser();
            Map<String, TtmlStyle> globalStyles3 = new HashMap<>();
            Map<String, TtmlRegion> regionMap = new HashMap<>();
            Map<String, String> imageMap = new HashMap<>();
            regionMap.put("", new TtmlRegion(null));
            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(bytes, 0, length);
            xmlParser.setInput(inputStream2, null);
            ArrayDeque<TtmlNode> nodeStack2 = new ArrayDeque<>();
            int eventType = xmlParser.getEventType();
            FrameAndTickRate frameAndTickRate3 = DEFAULT_FRAME_AND_TICK_RATE;
            CellResolution cellResolution2 = DEFAULT_CELL_RESOLUTION;
            TtsExtent ttsExtent2 = null;
            TtmlSubtitle ttmlSubtitle = null;
            int unsupportedNodeDepth = 0;
            int eventType2 = eventType;
            while (eventType2 != 1) {
                TtmlNode parent = nodeStack2.peek();
                if (unsupportedNodeDepth != 0) {
                    globalStyles = globalStyles3;
                    inputStream = inputStream2;
                    int eventType3 = eventType2;
                    nodeStack = nodeStack2;
                    if (eventType3 == 2) {
                        unsupportedNodeDepth++;
                    } else if (eventType3 == 3) {
                        unsupportedNodeDepth--;
                    }
                } else {
                    String name = xmlParser.getName();
                    if (eventType2 == 2) {
                        if (!TtmlNode.TAG_TT.equals(name)) {
                            cellResolution = cellResolution2;
                            ttsExtent = ttsExtent2;
                            frameAndTickRate = frameAndTickRate3;
                        } else {
                            FrameAndTickRate frameAndTickRate4 = parseFrameAndTickRates(xmlParser);
                            CellResolution cellResolution3 = parseCellResolution(xmlParser, DEFAULT_CELL_RESOLUTION);
                            TtsExtent ttsExtent3 = parseTtsExtent(xmlParser);
                            cellResolution = cellResolution3;
                            ttsExtent = ttsExtent3;
                            frameAndTickRate = frameAndTickRate4;
                        }
                        if (isSupportedTag(name)) {
                            if (TtmlNode.TAG_HEAD.equals(name)) {
                                frameAndTickRate2 = frameAndTickRate;
                                inputStream = inputStream2;
                                globalStyles2 = globalStyles3;
                                nodeStack = nodeStack2;
                                parseHeader(xmlParser, globalStyles3, cellResolution, ttsExtent, regionMap, imageMap);
                            } else {
                                frameAndTickRate2 = frameAndTickRate;
                                globalStyles2 = globalStyles3;
                                inputStream = inputStream2;
                                nodeStack = nodeStack2;
                                try {
                                    TtmlNode node = parseNode(xmlParser, parent, regionMap, frameAndTickRate2);
                                    nodeStack.push(node);
                                    if (parent != null) {
                                        parent.addChild(node);
                                    }
                                } catch (SubtitleDecoderException e) {
                                    Log.w(TAG, "Suppressing parser error", e);
                                    unsupportedNodeDepth++;
                                    frameAndTickRate3 = frameAndTickRate2;
                                    cellResolution2 = cellResolution;
                                    ttsExtent2 = ttsExtent;
                                    globalStyles = globalStyles2;
                                }
                            }
                            frameAndTickRate3 = frameAndTickRate2;
                            cellResolution2 = cellResolution;
                            ttsExtent2 = ttsExtent;
                            globalStyles = globalStyles2;
                        } else {
                            Log.i(TAG, "Ignoring unsupported tag: " + xmlParser.getName());
                            unsupportedNodeDepth++;
                            frameAndTickRate3 = frameAndTickRate;
                            globalStyles = globalStyles3;
                            inputStream = inputStream2;
                            cellResolution2 = cellResolution;
                            ttsExtent2 = ttsExtent;
                            nodeStack = nodeStack2;
                        }
                    } else {
                        Map<String, TtmlStyle> globalStyles4 = globalStyles3;
                        inputStream = inputStream2;
                        int eventType4 = eventType2;
                        nodeStack = nodeStack2;
                        if (eventType4 == 4) {
                            parent.addChild(TtmlNode.buildTextNode(xmlParser.getText()));
                            globalStyles = globalStyles4;
                        } else if (eventType4 == 3) {
                            if (!xmlParser.getName().equals(TtmlNode.TAG_TT)) {
                                globalStyles = globalStyles4;
                            } else {
                                globalStyles = globalStyles4;
                                ttmlSubtitle = new TtmlSubtitle(nodeStack.peek(), globalStyles, regionMap, imageMap);
                            }
                            nodeStack.pop();
                        } else {
                            globalStyles = globalStyles4;
                        }
                    }
                }
                xmlParser.next();
                eventType2 = xmlParser.getEventType();
                nodeStack2 = nodeStack;
                inputStream2 = inputStream;
                globalStyles3 = globalStyles;
            }
            return ttmlSubtitle;
        } catch (IOException e2) {
            throw new IllegalStateException("Unexpected error when reading input.", e2);
        } catch (XmlPullParserException xppe) {
            throw new SubtitleDecoderException("Unable to decode source", xppe);
        }
    }

    private FrameAndTickRate parseFrameAndTickRates(XmlPullParser xmlParser) throws SubtitleDecoderException {
        int frameRate = 30;
        String frameRateString = xmlParser.getAttributeValue(TTP, "frameRate");
        if (frameRateString != null) {
            frameRate = Integer.parseInt(frameRateString);
        }
        float frameRateMultiplier = 1.0f;
        String frameRateMultiplierString = xmlParser.getAttributeValue(TTP, "frameRateMultiplier");
        if (frameRateMultiplierString != null) {
            String[] parts = Util.split(frameRateMultiplierString, " ");
            if (parts.length != 2) {
                throw new SubtitleDecoderException("frameRateMultiplier doesn't have 2 parts");
            }
            float numerator = Integer.parseInt(parts[0]);
            float denominator = Integer.parseInt(parts[1]);
            frameRateMultiplier = numerator / denominator;
        }
        FrameAndTickRate frameAndTickRate = DEFAULT_FRAME_AND_TICK_RATE;
        int subFrameRate = frameAndTickRate.subFrameRate;
        String subFrameRateString = xmlParser.getAttributeValue(TTP, "subFrameRate");
        if (subFrameRateString != null) {
            subFrameRate = Integer.parseInt(subFrameRateString);
        }
        int tickRate = frameAndTickRate.tickRate;
        String tickRateString = xmlParser.getAttributeValue(TTP, "tickRate");
        if (tickRateString != null) {
            tickRate = Integer.parseInt(tickRateString);
        }
        return new FrameAndTickRate(frameRate * frameRateMultiplier, subFrameRate, tickRate);
    }

    private CellResolution parseCellResolution(XmlPullParser xmlParser, CellResolution defaultValue) throws SubtitleDecoderException {
        String cellResolution = xmlParser.getAttributeValue(TTP, "cellResolution");
        if (cellResolution == null) {
            return defaultValue;
        }
        Matcher cellResolutionMatcher = CELL_RESOLUTION.matcher(cellResolution);
        if (!cellResolutionMatcher.matches()) {
            Log.w(TAG, "Ignoring malformed cell resolution: " + cellResolution);
            return defaultValue;
        }
        try {
            int columns = Integer.parseInt(cellResolutionMatcher.group(1));
            int rows = Integer.parseInt(cellResolutionMatcher.group(2));
            if (columns == 0 || rows == 0) {
                throw new SubtitleDecoderException("Invalid cell resolution " + columns + " " + rows);
            }
            return new CellResolution(columns, rows);
        } catch (NumberFormatException e) {
            Log.w(TAG, "Ignoring malformed cell resolution: " + cellResolution);
            return defaultValue;
        }
    }

    private TtsExtent parseTtsExtent(XmlPullParser xmlParser) {
        String ttsExtent = XmlPullParserUtil.getAttributeValue(xmlParser, TtmlNode.ATTR_TTS_EXTENT);
        if (ttsExtent == null) {
            return null;
        }
        Matcher extentMatcher = PIXEL_COORDINATES.matcher(ttsExtent);
        if (!extentMatcher.matches()) {
            Log.w(TAG, "Ignoring non-pixel tts extent: " + ttsExtent);
            return null;
        }
        try {
            int width = Integer.parseInt(extentMatcher.group(1));
            int height = Integer.parseInt(extentMatcher.group(2));
            return new TtsExtent(width, height);
        } catch (NumberFormatException e) {
            Log.w(TAG, "Ignoring malformed tts extent: " + ttsExtent);
            return null;
        }
    }

    private Map<String, TtmlStyle> parseHeader(XmlPullParser xmlParser, Map<String, TtmlStyle> globalStyles, CellResolution cellResolution, TtsExtent ttsExtent, Map<String, TtmlRegion> globalRegions, Map<String, String> imageMap) throws IOException, XmlPullParserException {
        String[] parseStyleIds;
        do {
            xmlParser.next();
            if (XmlPullParserUtil.isStartTag(xmlParser, "style")) {
                String parentStyleId = XmlPullParserUtil.getAttributeValue(xmlParser, "style");
                TtmlStyle style = parseStyleAttributes(xmlParser, new TtmlStyle());
                if (parentStyleId != null) {
                    for (String id : parseStyleIds(parentStyleId)) {
                        style.chain(globalStyles.get(id));
                    }
                }
                if (style.getId() != null) {
                    globalStyles.put(style.getId(), style);
                }
            } else if (XmlPullParserUtil.isStartTag(xmlParser, "region")) {
                TtmlRegion ttmlRegion = parseRegionAttributes(xmlParser, cellResolution, ttsExtent);
                if (ttmlRegion != null) {
                    globalRegions.put(ttmlRegion.id, ttmlRegion);
                }
            } else if (XmlPullParserUtil.isStartTag(xmlParser, TtmlNode.TAG_METADATA)) {
                parseMetadata(xmlParser, imageMap);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlParser, TtmlNode.TAG_HEAD));
        return globalStyles;
    }

    private void parseMetadata(XmlPullParser xmlParser, Map<String, String> imageMap) throws IOException, XmlPullParserException {
        String id;
        do {
            xmlParser.next();
            if (XmlPullParserUtil.isStartTag(xmlParser, TtmlNode.TAG_IMAGE) && (id = XmlPullParserUtil.getAttributeValue(xmlParser, "id")) != null) {
                String encodedBitmapData = xmlParser.nextText();
                imageMap.put(id, encodedBitmapData);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlParser, TtmlNode.TAG_METADATA));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0135, code lost:
        if (r4.equals("after") != false) goto L47;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TtmlRegion parseRegionAttributes(XmlPullParser xmlParser, CellResolution cellResolution, TtsExtent ttsExtent) {
        float line;
        float position;
        float width;
        float height;
        float line2;
        int lineAnchor;
        String regionId = XmlPullParserUtil.getAttributeValue(xmlParser, "id");
        if (regionId != null) {
            String regionOrigin = XmlPullParserUtil.getAttributeValue(xmlParser, TtmlNode.ATTR_TTS_ORIGIN);
            if (regionOrigin == null) {
                Log.w(TAG, "Ignoring region without an origin");
                return null;
            }
            Pattern pattern = PERCENTAGE_COORDINATES;
            Matcher originPercentageMatcher = pattern.matcher(regionOrigin);
            Pattern pattern2 = PIXEL_COORDINATES;
            Matcher originPixelMatcher = pattern2.matcher(regionOrigin);
            char c = 1;
            if (originPercentageMatcher.matches()) {
                try {
                    float position2 = Float.parseFloat(originPercentageMatcher.group(1)) / 100.0f;
                    line = Float.parseFloat(originPercentageMatcher.group(2)) / 100.0f;
                    position = position2;
                } catch (NumberFormatException e) {
                    Log.w(TAG, "Ignoring region with malformed origin: " + regionOrigin);
                    return null;
                }
            } else if (originPixelMatcher.matches()) {
                if (ttsExtent == null) {
                    Log.w(TAG, "Ignoring region with missing tts:extent: " + regionOrigin);
                    return null;
                }
                try {
                    int width2 = Integer.parseInt(originPixelMatcher.group(1));
                    int height2 = Integer.parseInt(originPixelMatcher.group(2));
                    float position3 = width2 / ttsExtent.width;
                    line = height2 / ttsExtent.height;
                    position = position3;
                } catch (NumberFormatException e2) {
                    Log.w(TAG, "Ignoring region with malformed origin: " + regionOrigin);
                    return null;
                }
            } else {
                Log.w(TAG, "Ignoring region with unsupported origin: " + regionOrigin);
                return null;
            }
            String regionExtent = XmlPullParserUtil.getAttributeValue(xmlParser, TtmlNode.ATTR_TTS_EXTENT);
            if (regionExtent == null) {
                Log.w(TAG, "Ignoring region without an extent");
                return null;
            }
            Matcher extentPercentageMatcher = pattern.matcher(regionExtent);
            Matcher extentPixelMatcher = pattern2.matcher(regionExtent);
            if (extentPercentageMatcher.matches()) {
                try {
                    width = Float.parseFloat(extentPercentageMatcher.group(1)) / 100.0f;
                    float height3 = Float.parseFloat(extentPercentageMatcher.group(2)) / 100.0f;
                    height = height3;
                } catch (NumberFormatException e3) {
                    Log.w(TAG, "Ignoring region with malformed extent: " + regionOrigin);
                    return null;
                }
            } else if (!extentPixelMatcher.matches()) {
                Log.w(TAG, "Ignoring region with unsupported extent: " + regionOrigin);
                return null;
            } else if (ttsExtent == null) {
                Log.w(TAG, "Ignoring region with missing tts:extent: " + regionOrigin);
                return null;
            } else {
                try {
                    int extentWidth = Integer.parseInt(extentPixelMatcher.group(1));
                    int extentHeight = Integer.parseInt(extentPixelMatcher.group(2));
                    float width3 = extentWidth / ttsExtent.width;
                    float height4 = extentHeight / ttsExtent.height;
                    height = height4;
                    width = width3;
                } catch (NumberFormatException e4) {
                    Log.w(TAG, "Ignoring region with malformed extent: " + regionOrigin);
                    return null;
                }
            }
            String displayAlign = XmlPullParserUtil.getAttributeValue(xmlParser, TtmlNode.ATTR_TTS_DISPLAY_ALIGN);
            if (displayAlign != null) {
                String lowerInvariant = Util.toLowerInvariant(displayAlign);
                switch (lowerInvariant.hashCode()) {
                    case -1364013995:
                        if (lowerInvariant.equals(TtmlNode.CENTER)) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 92734940:
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        lineAnchor = 1;
                        line2 = line + (height / 2.0f);
                        break;
                    case 1:
                        lineAnchor = 2;
                        line2 = line + height;
                        break;
                }
                float regionTextHeight = 1.0f / cellResolution.rows;
                return new TtmlRegion(regionId, position, line2, 0, lineAnchor, width, height, 1, regionTextHeight);
            }
            lineAnchor = 0;
            line2 = line;
            float regionTextHeight2 = 1.0f / cellResolution.rows;
            return new TtmlRegion(regionId, position, line2, 0, lineAnchor, width, height, 1, regionTextHeight2);
        }
        return null;
    }

    private String[] parseStyleIds(String parentStyleIds) {
        String parentStyleIds2 = parentStyleIds.trim();
        return parentStyleIds2.isEmpty() ? new String[0] : Util.split(parentStyleIds2, "\\s+");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00a6, code lost:
        if (r3.equals(com.google.android.exoplayer2.text.ttml.TtmlNode.UNDERLINE) != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0118, code lost:
        if (r3.equals(com.google.android.exoplayer2.text.ttml.TtmlNode.CENTER) != false) goto L72;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TtmlStyle parseStyleAttributes(XmlPullParser parser, TtmlStyle style) {
        char c;
        int attributeCount = parser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeValue = parser.getAttributeValue(i);
            String attributeName = parser.getAttributeName(i);
            char c2 = 4;
            char c3 = 2;
            switch (attributeName.hashCode()) {
                case -1550943582:
                    if (attributeName.equals(TtmlNode.ATTR_TTS_FONT_STYLE)) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -1224696685:
                    if (attributeName.equals(TtmlNode.ATTR_TTS_FONT_FAMILY)) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -1065511464:
                    if (attributeName.equals(TtmlNode.ATTR_TTS_TEXT_ALIGN)) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -879295043:
                    if (attributeName.equals(TtmlNode.ATTR_TTS_TEXT_DECORATION)) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case -734428249:
                    if (attributeName.equals(TtmlNode.ATTR_TTS_FONT_WEIGHT)) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 3355:
                    if (attributeName.equals("id")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 94842723:
                    if (attributeName.equals(TtmlNode.ATTR_TTS_COLOR)) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 365601008:
                    if (attributeName.equals(TtmlNode.ATTR_TTS_FONT_SIZE)) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 1287124693:
                    if (attributeName.equals(TtmlNode.ATTR_TTS_BACKGROUND_COLOR)) {
                        c = 1;
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
                    if ("style".equals(parser.getName())) {
                        style = createIfNull(style).setId(attributeValue);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    style = createIfNull(style);
                    try {
                        style.setBackgroundColor(ColorParser.parseTtmlColor(attributeValue));
                        break;
                    } catch (IllegalArgumentException e) {
                        Log.w(TAG, "Failed parsing background value: " + attributeValue);
                        break;
                    }
                case 2:
                    style = createIfNull(style);
                    try {
                        style.setFontColor(ColorParser.parseTtmlColor(attributeValue));
                        break;
                    } catch (IllegalArgumentException e2) {
                        Log.w(TAG, "Failed parsing color value: " + attributeValue);
                        break;
                    }
                case 3:
                    style = createIfNull(style).setFontFamily(attributeValue);
                    break;
                case 4:
                    try {
                        style = createIfNull(style);
                        parseFontSize(attributeValue, style);
                        break;
                    } catch (SubtitleDecoderException e3) {
                        Log.w(TAG, "Failed parsing fontSize value: " + attributeValue);
                        break;
                    }
                case 5:
                    style = createIfNull(style).setBold(TtmlNode.BOLD.equalsIgnoreCase(attributeValue));
                    break;
                case 6:
                    style = createIfNull(style).setItalic(TtmlNode.ITALIC.equalsIgnoreCase(attributeValue));
                    break;
                case 7:
                    String lowerInvariant = Util.toLowerInvariant(attributeValue);
                    switch (lowerInvariant.hashCode()) {
                        case -1364013995:
                            break;
                        case 100571:
                            if (lowerInvariant.equals("end")) {
                                c2 = 3;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 3317767:
                            if (lowerInvariant.equals(TtmlNode.LEFT)) {
                                c2 = 0;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 108511772:
                            if (lowerInvariant.equals(TtmlNode.RIGHT)) {
                                c2 = 2;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 109757538:
                            if (lowerInvariant.equals(TtmlNode.START)) {
                                c2 = 1;
                                break;
                            }
                            c2 = 65535;
                            break;
                        default:
                            c2 = 65535;
                            break;
                    }
                    switch (c2) {
                        case 0:
                            style = createIfNull(style).setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                            continue;
                        case 1:
                            style = createIfNull(style).setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                            continue;
                        case 2:
                            style = createIfNull(style).setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                            continue;
                        case 3:
                            style = createIfNull(style).setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                            continue;
                        case 4:
                            style = createIfNull(style).setTextAlign(Layout.Alignment.ALIGN_CENTER);
                            continue;
                    }
                case '\b':
                    String lowerInvariant2 = Util.toLowerInvariant(attributeValue);
                    switch (lowerInvariant2.hashCode()) {
                        case -1461280213:
                            if (lowerInvariant2.equals(TtmlNode.NO_UNDERLINE)) {
                                c3 = 3;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case -1026963764:
                            break;
                        case 913457136:
                            if (lowerInvariant2.equals(TtmlNode.NO_LINETHROUGH)) {
                                c3 = 1;
                                break;
                            }
                            c3 = 65535;
                            break;
                        case 1679736913:
                            if (lowerInvariant2.equals(TtmlNode.LINETHROUGH)) {
                                c3 = 0;
                                break;
                            }
                            c3 = 65535;
                            break;
                        default:
                            c3 = 65535;
                            break;
                    }
                    switch (c3) {
                        case 0:
                            style = createIfNull(style).setLinethrough(true);
                            continue;
                        case 1:
                            style = createIfNull(style).setLinethrough(false);
                            continue;
                        case 2:
                            style = createIfNull(style).setUnderline(true);
                            continue;
                        case 3:
                            style = createIfNull(style).setUnderline(false);
                            continue;
                    }
            }
        }
        return style;
    }

    private TtmlStyle createIfNull(TtmlStyle style) {
        return style == null ? new TtmlStyle() : style;
    }

    private TtmlNode parseNode(XmlPullParser parser, TtmlNode parent, Map<String, TtmlRegion> regionMap, FrameAndTickRate frameAndTickRate) throws SubtitleDecoderException {
        String regionId;
        TtmlDecoder ttmlDecoder = this;
        XmlPullParser xmlPullParser = parser;
        long duration = C.TIME_UNSET;
        long startTime = C.TIME_UNSET;
        long endTime = C.TIME_UNSET;
        String regionId2 = "";
        String value = null;
        String[] styleIds = null;
        int attributeCount = parser.getAttributeCount();
        TtmlStyle style = ttmlDecoder.parseStyleAttributes(xmlPullParser, null);
        int i = 0;
        while (i < attributeCount) {
            int attributeCount2 = attributeCount;
            String attr = xmlPullParser.getAttributeName(i);
            String imageId = value;
            String value2 = xmlPullParser.getAttributeValue(i);
            char c = 65535;
            switch (attr.hashCode()) {
                case -934795532:
                    if (attr.equals("region")) {
                        c = 4;
                        break;
                    }
                    break;
                case 99841:
                    if (attr.equals(ATTR_DURATION)) {
                        c = 2;
                        break;
                    }
                    break;
                case 100571:
                    if (attr.equals("end")) {
                        c = 1;
                        break;
                    }
                    break;
                case 93616297:
                    if (attr.equals(ATTR_BEGIN)) {
                        c = 0;
                        break;
                    }
                    break;
                case 109780401:
                    if (attr.equals("style")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1292595405:
                    if (attr.equals(ATTR_IMAGE)) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    long startTime2 = parseTimeExpression(value2, frameAndTickRate);
                    startTime = startTime2;
                    value = imageId;
                    continue;
                    i++;
                    ttmlDecoder = this;
                    xmlPullParser = parser;
                    attributeCount = attributeCount2;
                case 1:
                    long endTime2 = parseTimeExpression(value2, frameAndTickRate);
                    endTime = endTime2;
                    value = imageId;
                    continue;
                    i++;
                    ttmlDecoder = this;
                    xmlPullParser = parser;
                    attributeCount = attributeCount2;
                case 2:
                    long duration2 = parseTimeExpression(value2, frameAndTickRate);
                    duration = duration2;
                    value = imageId;
                    continue;
                    i++;
                    ttmlDecoder = this;
                    xmlPullParser = parser;
                    attributeCount = attributeCount2;
                case 3:
                    String[] ids = ttmlDecoder.parseStyleIds(value2);
                    if (ids.length > 0) {
                        styleIds = ids;
                        value = imageId;
                        continue;
                        i++;
                        ttmlDecoder = this;
                        xmlPullParser = parser;
                        attributeCount = attributeCount2;
                    }
                    break;
                case 4:
                    if (regionMap.containsKey(value2)) {
                        regionId2 = value2;
                        value = imageId;
                        continue;
                        i++;
                        ttmlDecoder = this;
                        xmlPullParser = parser;
                        attributeCount = attributeCount2;
                    }
                    break;
                case 5:
                    if (value2.startsWith("#")) {
                        value = value2.substring(1);
                        continue;
                        i++;
                        ttmlDecoder = this;
                        xmlPullParser = parser;
                        attributeCount = attributeCount2;
                    }
                    break;
            }
            value = imageId;
            i++;
            ttmlDecoder = this;
            xmlPullParser = parser;
            attributeCount = attributeCount2;
        }
        String imageId2 = value;
        if (parent != null) {
            regionId = regionId2;
            if (parent.startTimeUs != C.TIME_UNSET) {
                if (startTime != C.TIME_UNSET) {
                    startTime += parent.startTimeUs;
                }
                if (endTime != C.TIME_UNSET) {
                    endTime += parent.startTimeUs;
                }
            }
        } else {
            regionId = regionId2;
        }
        if (endTime == C.TIME_UNSET) {
            if (duration != C.TIME_UNSET) {
                endTime = startTime + duration;
            } else if (parent != null && parent.endTimeUs != C.TIME_UNSET) {
                endTime = parent.endTimeUs;
            }
        }
        return TtmlNode.buildNode(parser.getName(), startTime, endTime, style, styleIds, regionId, imageId2);
    }

    private static boolean isSupportedTag(String tag) {
        return tag.equals(TtmlNode.TAG_TT) || tag.equals(TtmlNode.TAG_HEAD) || tag.equals(TtmlNode.TAG_BODY) || tag.equals(TtmlNode.TAG_DIV) || tag.equals(TtmlNode.TAG_P) || tag.equals(TtmlNode.TAG_SPAN) || tag.equals(TtmlNode.TAG_BR) || tag.equals("style") || tag.equals(TtmlNode.TAG_STYLING) || tag.equals(TtmlNode.TAG_LAYOUT) || tag.equals("region") || tag.equals(TtmlNode.TAG_METADATA) || tag.equals(TtmlNode.TAG_IMAGE) || tag.equals("data") || tag.equals(TtmlNode.TAG_INFORMATION);
    }

    private static void parseFontSize(String expression, TtmlStyle out) throws SubtitleDecoderException {
        Matcher matcher;
        String[] expressions = Util.split(expression, "\\s+");
        if (expressions.length == 1) {
            matcher = FONT_SIZE.matcher(expression);
        } else if (expressions.length == 2) {
            matcher = FONT_SIZE.matcher(expressions[1]);
            Log.w(TAG, "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
        } else {
            throw new SubtitleDecoderException("Invalid number of entries for fontSize: " + expressions.length + ".");
        }
        if (matcher.matches()) {
            String unit = matcher.group(3);
            char c = 65535;
            switch (unit.hashCode()) {
                case 37:
                    if (unit.equals("%")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3240:
                    if (unit.equals("em")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3592:
                    if (unit.equals("px")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    out.setFontSizeUnit(1);
                    break;
                case 1:
                    out.setFontSizeUnit(2);
                    break;
                case 2:
                    out.setFontSizeUnit(3);
                    break;
                default:
                    throw new SubtitleDecoderException("Invalid unit for fontSize: '" + unit + "'.");
            }
            out.setFontSize(Float.valueOf(matcher.group(1)).floatValue());
            return;
        }
        throw new SubtitleDecoderException("Invalid expression for fontSize: '" + expression + "'.");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00bf, code lost:
        if (r11.equals(org.telegram.ui.ActionBar.Theme.THEME_BACKGROUND_SLUG) != false) goto L39;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static long parseTimeExpression(String time, FrameAndTickRate frameAndTickRate) throws SubtitleDecoderException {
        double d;
        Matcher matcher = CLOCK_TIME.matcher(time);
        char c = 5;
        if (!matcher.matches()) {
            Matcher matcher2 = OFFSET_TIME.matcher(time);
            if (!matcher2.matches()) {
                throw new SubtitleDecoderException("Malformed time expression: " + time);
            }
            String timeValue = matcher2.group(1);
            double offsetSeconds = Double.parseDouble(timeValue);
            String unit = matcher2.group(2);
            switch (unit.hashCode()) {
                case 102:
                    if (unit.equals("f")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case LocationRequest.PRIORITY_LOW_POWER /* 104 */:
                    if (unit.equals("h")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 109:
                    if (unit.equals("m")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 115:
                    if (unit.equals("s")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 116:
                    break;
                case 3494:
                    if (unit.equals("ms")) {
                        c = 3;
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
                    offsetSeconds *= 3600.0d;
                    break;
                case 1:
                    offsetSeconds *= 60.0d;
                    break;
                case 3:
                    offsetSeconds /= 1000.0d;
                    break;
                case 4:
                    double d2 = frameAndTickRate.effectiveFrameRate;
                    Double.isNaN(d2);
                    offsetSeconds /= d2;
                    break;
                case 5:
                    double d3 = frameAndTickRate.tickRate;
                    Double.isNaN(d3);
                    offsetSeconds /= d3;
                    break;
            }
            return (long) (1000000.0d * offsetSeconds);
        }
        String hours = matcher.group(1);
        double durationSeconds = Long.parseLong(hours) * 3600;
        String minutes = matcher.group(2);
        double parseLong = Long.parseLong(minutes) * 60;
        Double.isNaN(durationSeconds);
        Double.isNaN(parseLong);
        double durationSeconds2 = durationSeconds + parseLong;
        String seconds = matcher.group(3);
        double parseLong2 = Long.parseLong(seconds);
        Double.isNaN(parseLong2);
        double durationSeconds3 = durationSeconds2 + parseLong2;
        String fraction = matcher.group(4);
        double d4 = FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
        double durationSeconds4 = durationSeconds3 + (fraction != null ? Double.parseDouble(fraction) : 0.0d);
        String frames = matcher.group(5);
        if (frames == null) {
            d = 0.0d;
        } else {
            d = ((float) Long.parseLong(frames)) / frameAndTickRate.effectiveFrameRate;
        }
        double durationSeconds5 = durationSeconds4 + d;
        String subframes = matcher.group(6);
        if (subframes != null) {
            double parseLong3 = Long.parseLong(subframes);
            double d5 = frameAndTickRate.subFrameRate;
            Double.isNaN(parseLong3);
            Double.isNaN(d5);
            double d6 = parseLong3 / d5;
            double d7 = frameAndTickRate.effectiveFrameRate;
            Double.isNaN(d7);
            d4 = d6 / d7;
        }
        return (long) (1000000.0d * (durationSeconds5 + d4));
    }

    /* loaded from: classes3.dex */
    public static final class FrameAndTickRate {
        final float effectiveFrameRate;
        final int subFrameRate;
        final int tickRate;

        FrameAndTickRate(float effectiveFrameRate, int subFrameRate, int tickRate) {
            this.effectiveFrameRate = effectiveFrameRate;
            this.subFrameRate = subFrameRate;
            this.tickRate = tickRate;
        }
    }

    /* loaded from: classes3.dex */
    public static final class CellResolution {
        final int columns;
        final int rows;

        CellResolution(int columns, int rows) {
            this.columns = columns;
            this.rows = rows;
        }
    }

    /* loaded from: classes3.dex */
    public static final class TtsExtent {
        final int height;
        final int width;

        TtsExtent(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
