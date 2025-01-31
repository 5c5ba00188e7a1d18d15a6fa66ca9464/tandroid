package kotlin.collections;

import java.util.ArrayList;
import java.util.Iterator;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.sequences.SequenceScope;

/* loaded from: classes.dex */
final class SlidingWindowKt$windowedIterator$1 extends RestrictedSuspendLambda implements Function2 {
    final /* synthetic */ Iterator $iterator;
    final /* synthetic */ boolean $partialWindows;
    final /* synthetic */ boolean $reuseBuffer;
    final /* synthetic */ int $size;
    final /* synthetic */ int $step;
    int I$0;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    SlidingWindowKt$windowedIterator$1(int i, int i2, Iterator it, boolean z, boolean z2, Continuation continuation) {
        super(2, continuation);
        this.$size = i;
        this.$step = i2;
        this.$iterator = it;
        this.$reuseBuffer = z;
        this.$partialWindows = z2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        SlidingWindowKt$windowedIterator$1 slidingWindowKt$windowedIterator$1 = new SlidingWindowKt$windowedIterator$1(this.$size, this.$step, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
        slidingWindowKt$windowedIterator$1.L$0 = obj;
        return slidingWindowKt$windowedIterator$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(SequenceScope sequenceScope, Continuation continuation) {
        return ((SlidingWindowKt$windowedIterator$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x014f  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x00d8 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x00ad  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:19:0x0146 -> B:12:0x0149). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:42:0x0118 -> B:30:0x011b). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:63:0x00a2 -> B:50:0x0055). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended;
        int coerceAtMost;
        RingBuffer ringBuffer;
        Iterator it;
        SequenceScope sequenceScope;
        SequenceScope sequenceScope2;
        ArrayList arrayList;
        int i;
        Iterator it2;
        int i2;
        RingBuffer ringBuffer2;
        SequenceScope sequenceScope3;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = this.label;
        if (i3 == 0) {
            ResultKt.throwOnFailure(obj);
            SequenceScope sequenceScope4 = (SequenceScope) this.L$0;
            coerceAtMost = RangesKt___RangesKt.coerceAtMost(this.$size, 1024);
            int i4 = this.$step - this.$size;
            if (i4 < 0) {
                ringBuffer = new RingBuffer(coerceAtMost);
                it = this.$iterator;
                sequenceScope = sequenceScope4;
                while (it.hasNext()) {
                }
                if (this.$partialWindows) {
                }
                return Unit.INSTANCE;
            }
            sequenceScope2 = sequenceScope4;
            arrayList = new ArrayList(coerceAtMost);
            i = 0;
            it2 = this.$iterator;
            i2 = i4;
            while (it2.hasNext()) {
            }
            if (!arrayList.isEmpty()) {
                this.L$0 = null;
                this.L$1 = null;
                this.L$2 = null;
                this.label = 2;
                if (sequenceScope2.yield(arrayList, this) == coroutine_suspended) {
                }
            }
            return Unit.INSTANCE;
        }
        if (i3 == 1) {
            i2 = this.I$0;
            it2 = (Iterator) this.L$2;
            arrayList = (ArrayList) this.L$1;
            sequenceScope2 = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            i = i2;
            if (this.$reuseBuffer) {
                arrayList = new ArrayList(this.$size);
            } else {
                arrayList.clear();
            }
            i2 = i;
            while (it2.hasNext()) {
                Object next = it2.next();
                if (i > 0) {
                    i--;
                } else {
                    arrayList.add(next);
                    if (arrayList.size() == this.$size) {
                        this.L$0 = sequenceScope2;
                        this.L$1 = arrayList;
                        this.L$2 = it2;
                        this.I$0 = i2;
                        this.label = 1;
                        if (sequenceScope2.yield(arrayList, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        i = i2;
                        if (this.$reuseBuffer) {
                        }
                        i2 = i;
                        while (it2.hasNext()) {
                        }
                    }
                }
            }
            if ((!arrayList.isEmpty()) && (this.$partialWindows || arrayList.size() == this.$size)) {
                this.L$0 = null;
                this.L$1 = null;
                this.L$2 = null;
                this.label = 2;
                if (sequenceScope2.yield(arrayList, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
            return Unit.INSTANCE;
        }
        if (i3 != 2) {
            if (i3 == 3) {
                it = (Iterator) this.L$2;
                ringBuffer = (RingBuffer) this.L$1;
                sequenceScope = (SequenceScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                ringBuffer.removeFirst(this.$step);
                while (it.hasNext()) {
                    ringBuffer.add(it.next());
                    if (ringBuffer.isFull()) {
                        int size = ringBuffer.size();
                        int i5 = this.$size;
                        if (size >= i5) {
                            Object arrayList2 = this.$reuseBuffer ? ringBuffer : new ArrayList(ringBuffer);
                            this.L$0 = sequenceScope;
                            this.L$1 = ringBuffer;
                            this.L$2 = it;
                            this.label = 3;
                            if (sequenceScope.yield(arrayList2, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            ringBuffer.removeFirst(this.$step);
                            while (it.hasNext()) {
                            }
                        } else {
                            ringBuffer = ringBuffer.expanded(i5);
                        }
                    }
                }
                if (this.$partialWindows) {
                    ringBuffer2 = ringBuffer;
                    sequenceScope3 = sequenceScope;
                    if (ringBuffer2.size() <= this.$step) {
                    }
                }
                return Unit.INSTANCE;
            }
            if (i3 == 4) {
                ringBuffer2 = (RingBuffer) this.L$1;
                sequenceScope3 = (SequenceScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                ringBuffer2.removeFirst(this.$step);
                if (ringBuffer2.size() <= this.$step) {
                    Object arrayList3 = this.$reuseBuffer ? ringBuffer2 : new ArrayList(ringBuffer2);
                    this.L$0 = sequenceScope3;
                    this.L$1 = ringBuffer2;
                    this.L$2 = null;
                    this.label = 4;
                    if (sequenceScope3.yield(arrayList3, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    ringBuffer2.removeFirst(this.$step);
                    if (ringBuffer2.size() <= this.$step) {
                        if (!ringBuffer2.isEmpty()) {
                            this.L$0 = null;
                            this.L$1 = null;
                            this.L$2 = null;
                            this.label = 5;
                            if (sequenceScope3.yield(ringBuffer2, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                        return Unit.INSTANCE;
                    }
                }
            } else if (i3 != 5) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        ResultKt.throwOnFailure(obj);
        return Unit.INSTANCE;
    }
}
