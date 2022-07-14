package com.google.android.gms.common.data;

import com.google.android.gms.common.internal.Preconditions;
import java.util.ArrayList;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes3.dex */
public abstract class EntityBuffer<T> extends AbstractDataBuffer<T> {
    private boolean zaa = false;
    private ArrayList<Integer> zab;

    public EntityBuffer(DataHolder dataHolder) {
        super(dataHolder);
    }

    protected abstract T getEntry(int i, int i2);

    protected abstract String getPrimaryDataMarkerColumn();

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0071, code lost:
        if (r6.mDataHolder.getString(r4, r7, r3) == null) goto L16;
     */
    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final T get(int i) {
        int i2;
        zaa();
        int zaa = zaa(i);
        int i3 = 0;
        if (i >= 0 && i != this.zab.size()) {
            if (i == this.zab.size() - 1) {
                i2 = ((DataHolder) Preconditions.checkNotNull(this.mDataHolder)).getCount() - this.zab.get(i).intValue();
            } else {
                i2 = this.zab.get(i + 1).intValue() - this.zab.get(i).intValue();
            }
            if (i2 == 1) {
                int zaa2 = zaa(i);
                int windowIndex = ((DataHolder) Preconditions.checkNotNull(this.mDataHolder)).getWindowIndex(zaa2);
                String childDataMarkerColumn = getChildDataMarkerColumn();
                if (childDataMarkerColumn != null) {
                }
            }
            i3 = i2;
        }
        return getEntry(zaa, i3);
    }

    @Override // com.google.android.gms.common.data.AbstractDataBuffer, com.google.android.gms.common.data.DataBuffer
    public int getCount() {
        zaa();
        return this.zab.size();
    }

    private final void zaa() {
        synchronized (this) {
            if (!this.zaa) {
                int count = ((DataHolder) Preconditions.checkNotNull(this.mDataHolder)).getCount();
                ArrayList<Integer> arrayList = new ArrayList<>();
                this.zab = arrayList;
                if (count > 0) {
                    arrayList.add(0);
                    String primaryDataMarkerColumn = getPrimaryDataMarkerColumn();
                    String string = this.mDataHolder.getString(primaryDataMarkerColumn, 0, this.mDataHolder.getWindowIndex(0));
                    for (int i = 1; i < count; i++) {
                        int windowIndex = this.mDataHolder.getWindowIndex(i);
                        String string2 = this.mDataHolder.getString(primaryDataMarkerColumn, i, windowIndex);
                        if (string2 == null) {
                            StringBuilder sb = new StringBuilder(String.valueOf(primaryDataMarkerColumn).length() + 78);
                            sb.append("Missing value for markerColumn: ");
                            sb.append(primaryDataMarkerColumn);
                            sb.append(", at row: ");
                            sb.append(i);
                            sb.append(", for window: ");
                            sb.append(windowIndex);
                            throw new NullPointerException(sb.toString());
                        }
                        if (!string2.equals(string)) {
                            this.zab.add(Integer.valueOf(i));
                            string = string2;
                        }
                    }
                }
                this.zaa = true;
            }
        }
    }

    private final int zaa(int i) {
        if (i < 0 || i >= this.zab.size()) {
            StringBuilder sb = new StringBuilder(53);
            sb.append("Position ");
            sb.append(i);
            sb.append(" is out of bounds for this buffer");
            throw new IllegalArgumentException(sb.toString());
        }
        return this.zab.get(i).intValue();
    }

    protected String getChildDataMarkerColumn() {
        return null;
    }
}
