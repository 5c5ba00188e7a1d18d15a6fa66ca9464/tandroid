package androidx.recyclerview.widget;

import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.core.util.Pools$Pool;
import androidx.core.util.Pools$SimplePool;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.FileLog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ViewInfoStore {
    final ArrayMap mLayoutHolderMap = new ArrayMap();
    final LongSparseArray mOldChangedHolders = new LongSparseArray();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class InfoRecord {
        static Pools$Pool sPool = new Pools$SimplePool(20);
        int flags;
        RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
        RecyclerView.ItemAnimator.ItemHolderInfo preInfo;

        private InfoRecord() {
        }

        static void drainCache() {
            do {
            } while (sPool.acquire() != null);
        }

        static InfoRecord obtain() {
            InfoRecord infoRecord = (InfoRecord) sPool.acquire();
            return infoRecord == null ? new InfoRecord() : infoRecord;
        }

        static void recycle(InfoRecord infoRecord) {
            infoRecord.flags = 0;
            infoRecord.preInfo = null;
            infoRecord.postInfo = null;
            sPool.release(infoRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ProcessCallback {
        void processAppeared(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2);

        void processDisappeared(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2);

        void processPersistent(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2);

        void unused(RecyclerView.ViewHolder viewHolder);
    }

    private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder viewHolder, int i) {
        InfoRecord infoRecord;
        RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo;
        int indexOfKey = this.mLayoutHolderMap.indexOfKey(viewHolder);
        if (indexOfKey >= 0 && (infoRecord = (InfoRecord) this.mLayoutHolderMap.valueAt(indexOfKey)) != null) {
            int i2 = infoRecord.flags;
            if ((i2 & i) != 0) {
                int i3 = (i ^ (-1)) & i2;
                infoRecord.flags = i3;
                if (i == 4) {
                    itemHolderInfo = infoRecord.preInfo;
                } else {
                    if (i != 8) {
                        throw new IllegalArgumentException("Must provide flag PRE or POST");
                    }
                    itemHolderInfo = infoRecord.postInfo;
                }
                if ((i3 & 12) == 0) {
                    this.mLayoutHolderMap.removeAt(indexOfKey);
                    InfoRecord.recycle(infoRecord);
                }
                return itemHolderInfo;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.flags |= 2;
        infoRecord.preInfo = itemHolderInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addToDisappearedInLayout(RecyclerView.ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.flags |= 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addToOldChangeHolders(long j, RecyclerView.ViewHolder viewHolder) {
        this.mOldChangedHolders.put(j, viewHolder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addToPostLayout(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.postInfo = itemHolderInfo;
        infoRecord.flags |= 8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addToPreLayout(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.preInfo = itemHolderInfo;
        infoRecord.flags |= 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        this.mLayoutHolderMap.clear();
        this.mOldChangedHolders.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecyclerView.ViewHolder getFromOldChangeHolders(long j) {
        return (RecyclerView.ViewHolder) this.mOldChangedHolders.get(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDisappearing(RecyclerView.ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        return (infoRecord == null || (infoRecord.flags & 1) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInPreLayout(RecyclerView.ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        return (infoRecord == null || (infoRecord.flags & 4) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDetach() {
        InfoRecord.drainCache();
    }

    public void onViewDetached(RecyclerView.ViewHolder viewHolder) {
        removeFromDisappearedInLayout(viewHolder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder viewHolder) {
        return popFromLayoutStep(viewHolder, 8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder viewHolder) {
        return popFromLayoutStep(viewHolder, 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x005f, code lost:
    
        if ((r4 & 8) != 0) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void process(ProcessCallback processCallback) {
        InfoRecord infoRecord;
        for (int size = this.mLayoutHolderMap.size() - 1; size >= 0; size--) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) this.mLayoutHolderMap.keyAt(size);
            try {
                infoRecord = (InfoRecord) this.mLayoutHolderMap.removeAt(size);
            } catch (Exception e) {
                FileLog.e(e);
                infoRecord = null;
            }
            if (infoRecord != null) {
                int i = infoRecord.flags;
                if ((i & 3) != 3) {
                    if ((i & 1) != 0) {
                        RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo = infoRecord.preInfo;
                        if (itemHolderInfo != null) {
                            processCallback.processDisappeared(viewHolder, itemHolderInfo, infoRecord.postInfo);
                        }
                    } else {
                        if ((i & 14) != 14) {
                            if ((i & 12) == 12) {
                                processCallback.processPersistent(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
                            } else if ((i & 4) != 0) {
                                processCallback.processDisappeared(viewHolder, infoRecord.preInfo, null);
                            }
                        }
                        processCallback.processAppeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
                    }
                    InfoRecord.recycle(infoRecord);
                }
                processCallback.unused(viewHolder);
                InfoRecord.recycle(infoRecord);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeFromDisappearedInLayout(RecyclerView.ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            return;
        }
        infoRecord.flags &= -2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeViewHolder(RecyclerView.ViewHolder viewHolder) {
        int size = this.mOldChangedHolders.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            if (viewHolder == this.mOldChangedHolders.valueAt(size)) {
                this.mOldChangedHolders.removeAt(size);
                break;
            }
            size--;
        }
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord != null) {
            this.mLayoutHolderMap.remove(viewHolder);
            InfoRecord.recycle(infoRecord);
        }
    }
}
