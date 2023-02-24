package com.google.android.exoplayer2.text.ttml;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.util.Pair;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class TtmlNode {
    private List<TtmlNode> children;
    public final long endTimeUs;
    public final String imageId;
    public final boolean isTextNode;
    private final HashMap<String, Integer> nodeEndsByRegion;
    private final HashMap<String, Integer> nodeStartsByRegion;
    public final TtmlNode parent;
    public final String regionId;
    public final long startTimeUs;
    public final TtmlStyle style;
    private final String[] styleIds;
    public final String tag;
    public final String text;

    public static TtmlNode buildTextNode(String str) {
        return new TtmlNode(null, TtmlRenderUtil.applyTextElementSpacePolicy(str), -9223372036854775807L, -9223372036854775807L, null, null, "", null, null);
    }

    public static TtmlNode buildNode(String str, long j, long j2, TtmlStyle ttmlStyle, String[] strArr, String str2, String str3, TtmlNode ttmlNode) {
        return new TtmlNode(str, null, j, j2, ttmlStyle, strArr, str2, str3, ttmlNode);
    }

    private TtmlNode(String str, String str2, long j, long j2, TtmlStyle ttmlStyle, String[] strArr, String str3, String str4, TtmlNode ttmlNode) {
        this.tag = str;
        this.text = str2;
        this.imageId = str4;
        this.style = ttmlStyle;
        this.styleIds = strArr;
        this.isTextNode = str2 != null;
        this.startTimeUs = j;
        this.endTimeUs = j2;
        this.regionId = (String) Assertions.checkNotNull(str3);
        this.parent = ttmlNode;
        this.nodeStartsByRegion = new HashMap<>();
        this.nodeEndsByRegion = new HashMap<>();
    }

    public boolean isActive(long j) {
        long j2 = this.startTimeUs;
        return (j2 == -9223372036854775807L && this.endTimeUs == -9223372036854775807L) || (j2 <= j && this.endTimeUs == -9223372036854775807L) || ((j2 == -9223372036854775807L && j < this.endTimeUs) || (j2 <= j && j < this.endTimeUs));
    }

    public void addChild(TtmlNode ttmlNode) {
        if (this.children == null) {
            this.children = new ArrayList();
        }
        this.children.add(ttmlNode);
    }

    public TtmlNode getChild(int i) {
        List<TtmlNode> list = this.children;
        if (list == null) {
            throw new IndexOutOfBoundsException();
        }
        return list.get(i);
    }

    public int getChildCount() {
        List<TtmlNode> list = this.children;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public long[] getEventTimesUs() {
        TreeSet<Long> treeSet = new TreeSet<>();
        int i = 0;
        getEventTimes(treeSet, false);
        long[] jArr = new long[treeSet.size()];
        Iterator<Long> it = treeSet.iterator();
        while (it.hasNext()) {
            jArr[i] = it.next().longValue();
            i++;
        }
        return jArr;
    }

    private void getEventTimes(TreeSet<Long> treeSet, boolean z) {
        boolean equals = "p".equals(this.tag);
        boolean equals2 = "div".equals(this.tag);
        if (z || equals || (equals2 && this.imageId != null)) {
            long j = this.startTimeUs;
            if (j != -9223372036854775807L) {
                treeSet.add(Long.valueOf(j));
            }
            long j2 = this.endTimeUs;
            if (j2 != -9223372036854775807L) {
                treeSet.add(Long.valueOf(j2));
            }
        }
        if (this.children == null) {
            return;
        }
        for (int i = 0; i < this.children.size(); i++) {
            this.children.get(i).getEventTimes(treeSet, z || equals);
        }
    }

    public String[] getStyleIds() {
        return this.styleIds;
    }

    public List<Cue> getCues(long j, Map<String, TtmlStyle> map, Map<String, TtmlRegion> map2, Map<String, String> map3) {
        List<Pair<String, String>> arrayList = new ArrayList<>();
        traverseForImage(j, this.regionId, arrayList);
        TreeMap treeMap = new TreeMap();
        traverseForText(j, false, this.regionId, treeMap);
        traverseForStyle(j, map, map2, this.regionId, treeMap);
        ArrayList arrayList2 = new ArrayList();
        for (Pair<String, String> pair : arrayList) {
            String str = map3.get(pair.second);
            if (str != null) {
                byte[] decode = Base64.decode(str, 0);
                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                TtmlRegion ttmlRegion = (TtmlRegion) Assertions.checkNotNull(map2.get(pair.first));
                arrayList2.add(new Cue.Builder().setBitmap(decodeByteArray).setPosition(ttmlRegion.position).setPositionAnchor(0).setLine(ttmlRegion.line, 0).setLineAnchor(ttmlRegion.lineAnchor).setSize(ttmlRegion.width).setBitmapHeight(ttmlRegion.height).setVerticalType(ttmlRegion.verticalType).build());
            }
        }
        for (Map.Entry entry : treeMap.entrySet()) {
            TtmlRegion ttmlRegion2 = (TtmlRegion) Assertions.checkNotNull(map2.get(entry.getKey()));
            Cue.Builder builder = (Cue.Builder) entry.getValue();
            cleanUpText((SpannableStringBuilder) Assertions.checkNotNull(builder.getText()));
            builder.setLine(ttmlRegion2.line, ttmlRegion2.lineType);
            builder.setLineAnchor(ttmlRegion2.lineAnchor);
            builder.setPosition(ttmlRegion2.position);
            builder.setSize(ttmlRegion2.width);
            builder.setTextSize(ttmlRegion2.textSize, ttmlRegion2.textSizeType);
            builder.setVerticalType(ttmlRegion2.verticalType);
            arrayList2.add(builder.build());
        }
        return arrayList2;
    }

    private void traverseForImage(long j, String str, List<Pair<String, String>> list) {
        if (!"".equals(this.regionId)) {
            str = this.regionId;
        }
        if (isActive(j) && "div".equals(this.tag) && this.imageId != null) {
            list.add(new Pair<>(str, this.imageId));
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            getChild(i).traverseForImage(j, str, list);
        }
    }

    private void traverseForText(long j, boolean z, String str, Map<String, Cue.Builder> map) {
        this.nodeStartsByRegion.clear();
        this.nodeEndsByRegion.clear();
        if ("metadata".equals(this.tag)) {
            return;
        }
        if (!"".equals(this.regionId)) {
            str = this.regionId;
        }
        if (this.isTextNode && z) {
            getRegionOutputText(str, map).append((CharSequence) Assertions.checkNotNull(this.text));
        } else if ("br".equals(this.tag) && z) {
            getRegionOutputText(str, map).append('\n');
        } else if (isActive(j)) {
            for (Map.Entry<String, Cue.Builder> entry : map.entrySet()) {
                this.nodeStartsByRegion.put(entry.getKey(), Integer.valueOf(((CharSequence) Assertions.checkNotNull(entry.getValue().getText())).length()));
            }
            boolean equals = "p".equals(this.tag);
            for (int i = 0; i < getChildCount(); i++) {
                getChild(i).traverseForText(j, z || equals, str, map);
            }
            if (equals) {
                TtmlRenderUtil.endParagraph(getRegionOutputText(str, map));
            }
            for (Map.Entry<String, Cue.Builder> entry2 : map.entrySet()) {
                this.nodeEndsByRegion.put(entry2.getKey(), Integer.valueOf(((CharSequence) Assertions.checkNotNull(entry2.getValue().getText())).length()));
            }
        }
    }

    private static SpannableStringBuilder getRegionOutputText(String str, Map<String, Cue.Builder> map) {
        if (!map.containsKey(str)) {
            Cue.Builder builder = new Cue.Builder();
            builder.setText(new SpannableStringBuilder());
            map.put(str, builder);
        }
        return (SpannableStringBuilder) Assertions.checkNotNull(map.get(str).getText());
    }

    private void traverseForStyle(long j, Map<String, TtmlStyle> map, Map<String, TtmlRegion> map2, String str, Map<String, Cue.Builder> map3) {
        int i;
        if (isActive(j)) {
            String str2 = "".equals(this.regionId) ? str : this.regionId;
            Iterator<Map.Entry<String, Integer>> it = this.nodeEndsByRegion.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<String, Integer> next = it.next();
                String key = next.getKey();
                int intValue = this.nodeStartsByRegion.containsKey(key) ? this.nodeStartsByRegion.get(key).intValue() : 0;
                int intValue2 = next.getValue().intValue();
                if (intValue != intValue2) {
                    applyStyleToOutput(map, (Cue.Builder) Assertions.checkNotNull(map3.get(key)), intValue, intValue2, ((TtmlRegion) Assertions.checkNotNull(map2.get(str2))).verticalType);
                }
            }
            for (i = 0; i < getChildCount(); i++) {
                getChild(i).traverseForStyle(j, map, map2, str2, map3);
            }
        }
    }

    private void applyStyleToOutput(Map<String, TtmlStyle> map, Cue.Builder builder, int i, int i2, int i3) {
        TtmlStyle resolveStyle = TtmlRenderUtil.resolveStyle(this.style, this.styleIds, map);
        SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) builder.getText();
        if (spannableStringBuilder == null) {
            spannableStringBuilder = new SpannableStringBuilder();
            builder.setText(spannableStringBuilder);
        }
        SpannableStringBuilder spannableStringBuilder2 = spannableStringBuilder;
        if (resolveStyle != null) {
            TtmlRenderUtil.applyStylesToSpan(spannableStringBuilder2, i, i2, resolveStyle, this.parent, map, i3);
            if ("p".equals(this.tag)) {
                if (resolveStyle.getShearPercentage() != Float.MAX_VALUE) {
                    builder.setShearDegrees((resolveStyle.getShearPercentage() * (-90.0f)) / 100.0f);
                }
                if (resolveStyle.getTextAlign() != null) {
                    builder.setTextAlignment(resolveStyle.getTextAlign());
                }
                if (resolveStyle.getMultiRowAlign() != null) {
                    builder.setMultiRowAlignment(resolveStyle.getMultiRowAlign());
                }
            }
        }
    }

    private static void cleanUpText(SpannableStringBuilder spannableStringBuilder) {
        DeleteTextSpan[] deleteTextSpanArr;
        for (DeleteTextSpan deleteTextSpan : (DeleteTextSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), DeleteTextSpan.class)) {
            spannableStringBuilder.replace(spannableStringBuilder.getSpanStart(deleteTextSpan), spannableStringBuilder.getSpanEnd(deleteTextSpan), "");
        }
        for (int i = 0; i < spannableStringBuilder.length(); i++) {
            if (spannableStringBuilder.charAt(i) == ' ') {
                int i2 = i + 1;
                int i3 = i2;
                while (i3 < spannableStringBuilder.length() && spannableStringBuilder.charAt(i3) == ' ') {
                    i3++;
                }
                int i4 = i3 - i2;
                if (i4 > 0) {
                    spannableStringBuilder.delete(i, i4 + i);
                }
            }
        }
        if (spannableStringBuilder.length() > 0 && spannableStringBuilder.charAt(0) == ' ') {
            spannableStringBuilder.delete(0, 1);
        }
        for (int i5 = 0; i5 < spannableStringBuilder.length() - 1; i5++) {
            if (spannableStringBuilder.charAt(i5) == '\n') {
                int i6 = i5 + 1;
                if (spannableStringBuilder.charAt(i6) == ' ') {
                    spannableStringBuilder.delete(i6, i5 + 2);
                }
            }
        }
        if (spannableStringBuilder.length() > 0 && spannableStringBuilder.charAt(spannableStringBuilder.length() - 1) == ' ') {
            spannableStringBuilder.delete(spannableStringBuilder.length() - 1, spannableStringBuilder.length());
        }
        for (int i7 = 0; i7 < spannableStringBuilder.length() - 1; i7++) {
            if (spannableStringBuilder.charAt(i7) == ' ') {
                int i8 = i7 + 1;
                if (spannableStringBuilder.charAt(i8) == '\n') {
                    spannableStringBuilder.delete(i7, i8);
                }
            }
        }
        if (spannableStringBuilder.length() <= 0 || spannableStringBuilder.charAt(spannableStringBuilder.length() - 1) != '\n') {
            return;
        }
        spannableStringBuilder.delete(spannableStringBuilder.length() - 1, spannableStringBuilder.length());
    }
}
