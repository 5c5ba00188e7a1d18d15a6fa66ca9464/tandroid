package org.telegram.ui.Charts.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONObject;
import org.telegram.messenger.SegmentTree;
import org.telegram.ui.Charts.data.ChartData;

/* loaded from: classes4.dex */
public class StackLinearChartData extends ChartData {
    public int simplifiedSize;
    public long[][] simplifiedY;
    long[] ySum;
    SegmentTree ySumSegmentTree;

    public StackLinearChartData(JSONObject jSONObject, boolean z) {
        super(jSONObject);
        if (z) {
            long[] jArr = new long[this.lines.size()];
            int[] iArr = new int[this.lines.size()];
            long j = 0;
            for (int i = 0; i < this.lines.size(); i++) {
                int length = this.x.length;
                for (int i2 = 0; i2 < length; i2++) {
                    long j2 = ((ChartData.Line) this.lines.get(i)).y[i2];
                    jArr[i] = jArr[i] + j2;
                    if (j2 == 0) {
                        iArr[i] = iArr[i] + 1;
                    }
                }
                j += jArr[i];
            }
            ArrayList arrayList = new ArrayList();
            for (int i3 = 0; i3 < this.lines.size(); i3++) {
                double d = jArr[i3];
                double d2 = j;
                Double.isNaN(d);
                Double.isNaN(d2);
                if (d / d2 < 0.01d && iArr[i3] > this.x.length / 2.0f) {
                    arrayList.add((ChartData.Line) this.lines.get(i3));
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.lines.remove((ChartData.Line) it.next());
            }
        }
        int length2 = ((ChartData.Line) this.lines.get(0)).y.length;
        int size = this.lines.size();
        this.ySum = new long[length2];
        for (int i4 = 0; i4 < length2; i4++) {
            this.ySum[i4] = 0;
            for (int i5 = 0; i5 < size; i5++) {
                long[] jArr2 = this.ySum;
                jArr2[i4] = jArr2[i4] + ((ChartData.Line) this.lines.get(i5)).y[i4];
            }
        }
        this.ySumSegmentTree = new SegmentTree(this.ySum);
    }

    public StackLinearChartData(ChartData chartData, long j) {
        int binarySearch = Arrays.binarySearch(chartData.x, j);
        int i = binarySearch - 4;
        int i2 = binarySearch + 4;
        if (i < 0) {
            i2 += -i;
            i = 0;
        }
        long[] jArr = chartData.x;
        if (i2 > jArr.length - 1) {
            i -= i2 - jArr.length;
            i2 = jArr.length - 1;
        }
        i = i < 0 ? 0 : i;
        int i3 = (i2 - i) + 1;
        this.x = new long[i3];
        this.xPercentage = new float[i3];
        this.lines = new ArrayList();
        for (int i4 = 0; i4 < chartData.lines.size(); i4++) {
            ChartData.Line line = new ChartData.Line();
            line.y = new long[i3];
            line.id = ((ChartData.Line) chartData.lines.get(i4)).id;
            line.name = ((ChartData.Line) chartData.lines.get(i4)).name;
            line.colorKey = ((ChartData.Line) chartData.lines.get(i4)).colorKey;
            line.color = ((ChartData.Line) chartData.lines.get(i4)).color;
            line.colorDark = ((ChartData.Line) chartData.lines.get(i4)).colorDark;
            this.lines.add(line);
        }
        int i5 = 0;
        while (i <= i2) {
            this.x[i5] = chartData.x[i];
            for (int i6 = 0; i6 < this.lines.size(); i6++) {
                ((ChartData.Line) this.lines.get(i6)).y[i5] = ((ChartData.Line) chartData.lines.get(i6)).y[i];
            }
            i5++;
            i++;
        }
        this.timeStep = 86400000L;
        measure();
    }

    @Override // org.telegram.ui.Charts.data.ChartData
    protected void measure() {
        super.measure();
        this.simplifiedSize = 0;
        int length = this.xPercentage.length;
        int size = this.lines.size();
        int max = Math.max(1, Math.round(length / 140.0f));
        int i = length / max;
        this.simplifiedY = (long[][]) Array.newInstance((Class<?>) Long.TYPE, size, i);
        long[] jArr = new long[size];
        for (int i2 = 0; i2 < length; i2++) {
            for (int i3 = 0; i3 < size; i3++) {
                long j = ((ChartData.Line) this.lines.get(i3)).y[i2];
                if (j > jArr[i3]) {
                    jArr[i3] = j;
                }
            }
            if (i2 % max == 0) {
                for (int i4 = 0; i4 < size; i4++) {
                    this.simplifiedY[i4][this.simplifiedSize] = jArr[i4];
                    jArr[i4] = 0;
                }
                int i5 = this.simplifiedSize + 1;
                this.simplifiedSize = i5;
                if (i5 >= i) {
                    return;
                }
            }
        }
    }
}
