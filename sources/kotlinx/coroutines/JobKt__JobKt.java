package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.coroutines.CoroutineContext;
/* compiled from: Job.kt */
/* loaded from: classes.dex */
final /* synthetic */ class JobKt__JobKt {
    public static final void cancel(CoroutineContext coroutineContext, CancellationException cancellationException) {
        Job job = (Job) coroutineContext.get(Job.Key);
        if (job == null) {
            return;
        }
        job.cancel(cancellationException);
    }
}
