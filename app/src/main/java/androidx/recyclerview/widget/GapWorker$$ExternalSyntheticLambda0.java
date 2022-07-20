package androidx.recyclerview.widget;
/* loaded from: classes.dex */
public final /* synthetic */ class GapWorker$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ RecyclerView f$0;

    public /* synthetic */ GapWorker$$ExternalSyntheticLambda0(RecyclerView recyclerView) {
        this.f$0 = recyclerView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        GapWorker.lambda$prefetchPositionWithDeadline$0(this.f$0);
    }
}
