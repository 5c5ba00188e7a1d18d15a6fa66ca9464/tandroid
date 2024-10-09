package com.google.android.exoplayer2.upstream;

/* loaded from: classes.dex */
public interface Allocator {

    /* loaded from: classes.dex */
    public interface AllocationNode {
        Allocation getAllocation();

        AllocationNode next();
    }

    Allocation allocate();

    int getIndividualAllocationLength();

    void release(Allocation allocation);

    void release(AllocationNode allocationNode);

    void trim();
}
