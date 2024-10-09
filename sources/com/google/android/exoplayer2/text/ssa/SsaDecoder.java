package com.google.android.exoplayer2.text.ssa;

import android.graphics.PointF;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.ssa.SsaStyle;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.common.base.Ascii;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class SsaDecoder extends SimpleSubtitleDecoder {
    private static final Pattern SSA_TIMECODE_PATTERN = Pattern.compile("(?:(\\d+):)?(\\d+):(\\d+)[:.](\\d+)");
    private final SsaDialogueFormat dialogueFormatFromInitializationData;
    private final boolean haveInitializationData;
    private float screenHeight;
    private float screenWidth;
    private Map styles;

    public SsaDecoder(List list) {
        super("SsaDecoder");
        this.screenWidth = -3.4028235E38f;
        this.screenHeight = -3.4028235E38f;
        if (list == null || list.isEmpty()) {
            this.haveInitializationData = false;
            this.dialogueFormatFromInitializationData = null;
            return;
        }
        this.haveInitializationData = true;
        String fromUtf8Bytes = Util.fromUtf8Bytes((byte[]) list.get(0));
        Assertions.checkArgument(fromUtf8Bytes.startsWith("Format:"));
        this.dialogueFormatFromInitializationData = (SsaDialogueFormat) Assertions.checkNotNull(SsaDialogueFormat.fromFormatLine(fromUtf8Bytes));
        parseHeader(new ParsableByteArray((byte[]) list.get(1)));
    }

    private static int addCuePlacerholderByTime(long j, List list, List list2) {
        int i;
        int size = list.size() - 1;
        while (true) {
            if (size < 0) {
                i = 0;
                break;
            }
            if (((Long) list.get(size)).longValue() == j) {
                return size;
            }
            if (((Long) list.get(size)).longValue() < j) {
                i = size + 1;
                break;
            }
            size--;
        }
        list.add(i, Long.valueOf(j));
        list2.add(i, i == 0 ? new ArrayList() : new ArrayList((Collection) list2.get(i - 1)));
        return i;
    }

    private static float computeDefaultLineOrPosition(int i) {
        if (i == 0) {
            return 0.05f;
        }
        if (i != 1) {
            return i != 2 ? -3.4028235E38f : 0.95f;
        }
        return 0.5f;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Cue createCue(String str, SsaStyle ssaStyle, SsaStyle.Overrides overrides, float f, float f2) {
        float computeDefaultLineOrPosition;
        StyleSpan styleSpan;
        SpannableString spannableString = new SpannableString(str);
        Cue.Builder text = new Cue.Builder().setText(spannableString);
        if (ssaStyle != null) {
            if (ssaStyle.primaryColor != null) {
                spannableString.setSpan(new ForegroundColorSpan(ssaStyle.primaryColor.intValue()), 0, spannableString.length(), 33);
            }
            if (ssaStyle.borderStyle == 3 && ssaStyle.outlineColor != null) {
                spannableString.setSpan(new BackgroundColorSpan(ssaStyle.outlineColor.intValue()), 0, spannableString.length(), 33);
            }
            float f3 = ssaStyle.fontSize;
            if (f3 != -3.4028235E38f && f2 != -3.4028235E38f) {
                text.setTextSize(f3 / f2, 1);
            }
            boolean z = ssaStyle.bold;
            if (z && ssaStyle.italic) {
                styleSpan = new StyleSpan(3);
            } else if (z) {
                styleSpan = new StyleSpan(1);
            } else {
                if (ssaStyle.italic) {
                    styleSpan = new StyleSpan(2);
                }
                if (ssaStyle.underline) {
                    spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 33);
                }
                if (ssaStyle.strikeout) {
                    spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), 33);
                }
            }
            spannableString.setSpan(styleSpan, 0, spannableString.length(), 33);
            if (ssaStyle.underline) {
            }
            if (ssaStyle.strikeout) {
            }
        }
        int i = overrides.alignment;
        if (i == -1) {
            i = ssaStyle != null ? ssaStyle.alignment : -1;
        }
        text.setTextAlignment(toTextAlignment(i)).setPositionAnchor(toPositionAnchor(i)).setLineAnchor(toLineAnchor(i));
        PointF pointF = overrides.position;
        if (pointF == null || f2 == -3.4028235E38f || f == -3.4028235E38f) {
            text.setPosition(computeDefaultLineOrPosition(text.getPositionAnchor()));
            computeDefaultLineOrPosition = computeDefaultLineOrPosition(text.getLineAnchor());
        } else {
            text.setPosition(pointF.x / f);
            computeDefaultLineOrPosition = overrides.position.y / f2;
        }
        text.setLine(computeDefaultLineOrPosition, 0);
        return text.build();
    }

    private void parseDialogueLine(String str, SsaDialogueFormat ssaDialogueFormat, List list, List list2) {
        int i;
        StringBuilder sb;
        Assertions.checkArgument(str.startsWith("Dialogue:"));
        String[] split = str.substring(9).split(",", ssaDialogueFormat.length);
        if (split.length != ssaDialogueFormat.length) {
            sb = new StringBuilder();
            sb.append("Skipping dialogue line with fewer columns than format: ");
        } else {
            long parseTimecodeUs = parseTimecodeUs(split[ssaDialogueFormat.startTimeIndex]);
            if (parseTimecodeUs == -9223372036854775807L) {
                sb = new StringBuilder();
            } else {
                long parseTimecodeUs2 = parseTimecodeUs(split[ssaDialogueFormat.endTimeIndex]);
                if (parseTimecodeUs2 != -9223372036854775807L) {
                    Map map = this.styles;
                    SsaStyle ssaStyle = (map == null || (i = ssaDialogueFormat.styleIndex) == -1) ? null : (SsaStyle) map.get(split[i].trim());
                    String str2 = split[ssaDialogueFormat.textIndex];
                    Cue createCue = createCue(SsaStyle.Overrides.stripStyleOverrides(str2).replace("\\N", "\n").replace("\\n", "\n").replace("\\h", " "), ssaStyle, SsaStyle.Overrides.parseFromDialogue(str2), this.screenWidth, this.screenHeight);
                    int addCuePlacerholderByTime = addCuePlacerholderByTime(parseTimecodeUs2, list2, list);
                    for (int addCuePlacerholderByTime2 = addCuePlacerholderByTime(parseTimecodeUs, list2, list); addCuePlacerholderByTime2 < addCuePlacerholderByTime; addCuePlacerholderByTime2++) {
                        ((List) list.get(addCuePlacerholderByTime2)).add(createCue);
                    }
                    return;
                }
                sb = new StringBuilder();
            }
            sb.append("Skipping invalid timing: ");
        }
        sb.append(str);
        Log.w("SsaDecoder", sb.toString());
    }

    private void parseEventBody(ParsableByteArray parsableByteArray, List list, List list2) {
        SsaDialogueFormat ssaDialogueFormat = this.haveInitializationData ? this.dialogueFormatFromInitializationData : null;
        while (true) {
            String readLine = parsableByteArray.readLine();
            if (readLine == null) {
                return;
            }
            if (readLine.startsWith("Format:")) {
                ssaDialogueFormat = SsaDialogueFormat.fromFormatLine(readLine);
            } else if (readLine.startsWith("Dialogue:")) {
                if (ssaDialogueFormat == null) {
                    Log.w("SsaDecoder", "Skipping dialogue line before complete format: " + readLine);
                } else {
                    parseDialogueLine(readLine, ssaDialogueFormat, list, list2);
                }
            }
        }
    }

    private void parseHeader(ParsableByteArray parsableByteArray) {
        while (true) {
            String readLine = parsableByteArray.readLine();
            if (readLine == null) {
                return;
            }
            if ("[Script Info]".equalsIgnoreCase(readLine)) {
                parseScriptInfo(parsableByteArray);
            } else if ("[V4+ Styles]".equalsIgnoreCase(readLine)) {
                this.styles = parseStyles(parsableByteArray);
            } else if ("[V4 Styles]".equalsIgnoreCase(readLine)) {
                Log.i("SsaDecoder", "[V4 Styles] are not supported");
            } else if ("[Events]".equalsIgnoreCase(readLine)) {
                return;
            }
        }
    }

    private void parseScriptInfo(ParsableByteArray parsableByteArray) {
        while (true) {
            String readLine = parsableByteArray.readLine();
            if (readLine == null) {
                return;
            }
            if (parsableByteArray.bytesLeft() != 0 && parsableByteArray.peekUnsignedByte() == 91) {
                return;
            }
            String[] split = readLine.split(":");
            if (split.length == 2) {
                String lowerCase = Ascii.toLowerCase(split[0].trim());
                lowerCase.hashCode();
                if (lowerCase.equals("playresx")) {
                    this.screenWidth = Float.parseFloat(split[1].trim());
                } else if (lowerCase.equals("playresy")) {
                    try {
                        this.screenHeight = Float.parseFloat(split[1].trim());
                    } catch (NumberFormatException unused) {
                    }
                }
            }
        }
    }

    private static Map parseStyles(ParsableByteArray parsableByteArray) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        SsaStyle.Format format = null;
        while (true) {
            String readLine = parsableByteArray.readLine();
            if (readLine == null || (parsableByteArray.bytesLeft() != 0 && parsableByteArray.peekUnsignedByte() == 91)) {
                break;
            }
            if (readLine.startsWith("Format:")) {
                format = SsaStyle.Format.fromFormatLine(readLine);
            } else if (readLine.startsWith("Style:")) {
                if (format == null) {
                    Log.w("SsaDecoder", "Skipping 'Style:' line before 'Format:' line: " + readLine);
                } else {
                    SsaStyle fromStyleLine = SsaStyle.fromStyleLine(readLine, format);
                    if (fromStyleLine != null) {
                        linkedHashMap.put(fromStyleLine.name, fromStyleLine);
                    }
                }
            }
        }
        return linkedHashMap;
    }

    private static long parseTimecodeUs(String str) {
        Matcher matcher = SSA_TIMECODE_PATTERN.matcher(str.trim());
        if (matcher.matches()) {
            return (Long.parseLong((String) Util.castNonNull(matcher.group(1))) * 3600000000L) + (Long.parseLong((String) Util.castNonNull(matcher.group(2))) * 60000000) + (Long.parseLong((String) Util.castNonNull(matcher.group(3))) * 1000000) + (Long.parseLong((String) Util.castNonNull(matcher.group(4))) * 10000);
        }
        return -9223372036854775807L;
    }

    private static int toLineAnchor(int i) {
        switch (i) {
            case -1:
                return Integer.MIN_VALUE;
            case 0:
            default:
                Log.w("SsaDecoder", "Unknown alignment: " + i);
                return Integer.MIN_VALUE;
            case 1:
            case 2:
            case 3:
                return 2;
            case 4:
            case 5:
            case 6:
                return 1;
            case 7:
            case 8:
            case 9:
                return 0;
        }
    }

    private static int toPositionAnchor(int i) {
        switch (i) {
            case -1:
                return Integer.MIN_VALUE;
            case 0:
            default:
                Log.w("SsaDecoder", "Unknown alignment: " + i);
                return Integer.MIN_VALUE;
            case 1:
            case 4:
            case 7:
                return 0;
            case 2:
            case 5:
            case 8:
                return 1;
            case 3:
            case 6:
            case 9:
                return 2;
        }
    }

    private static Layout.Alignment toTextAlignment(int i) {
        switch (i) {
            case -1:
                return null;
            case 0:
            default:
                Log.w("SsaDecoder", "Unknown alignment: " + i);
                return null;
            case 1:
            case 4:
            case 7:
                return Layout.Alignment.ALIGN_NORMAL;
            case 2:
            case 5:
            case 8:
                return Layout.Alignment.ALIGN_CENTER;
            case 3:
            case 6:
            case 9:
                return Layout.Alignment.ALIGN_OPPOSITE;
        }
    }

    @Override // com.google.android.exoplayer2.text.SimpleSubtitleDecoder
    protected Subtitle decode(byte[] bArr, int i, boolean z) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ParsableByteArray parsableByteArray = new ParsableByteArray(bArr, i);
        if (!this.haveInitializationData) {
            parseHeader(parsableByteArray);
        }
        parseEventBody(parsableByteArray, arrayList, arrayList2);
        return new SsaSubtitle(arrayList, arrayList2);
    }
}
