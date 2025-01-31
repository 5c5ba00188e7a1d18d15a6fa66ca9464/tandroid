package kotlin.text;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.sequences.Sequence;

/* loaded from: classes.dex */
final class DelimitedRangesSequence implements Sequence {
    private final Function2 getNextMatch;
    private final CharSequence input;
    private final int limit;
    private final int startIndex;

    public DelimitedRangesSequence(CharSequence input, int i, int i2, Function2 getNextMatch) {
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(getNextMatch, "getNextMatch");
        this.input = input;
        this.startIndex = i;
        this.limit = i2;
        this.getNextMatch = getNextMatch;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator iterator() {
        return new Iterator() { // from class: kotlin.text.DelimitedRangesSequence$iterator$1
            private int counter;
            private int currentStartIndex;
            private IntRange nextItem;
            private int nextSearchIndex;
            private int nextState = -1;

            {
                int i;
                CharSequence charSequence;
                int coerceIn;
                i = DelimitedRangesSequence.this.startIndex;
                charSequence = DelimitedRangesSequence.this.input;
                coerceIn = RangesKt___RangesKt.coerceIn(i, 0, charSequence.length());
                this.currentStartIndex = coerceIn;
                this.nextSearchIndex = coerceIn;
            }

            /* JADX WARN: Code restructure failed: missing block: B:9:0x0021, code lost:
            
                if (r0 < r4) goto L9;
             */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            private final void calcNext() {
                int i;
                CharSequence charSequence;
                Function2 function2;
                CharSequence charSequence2;
                IntRange until;
                IntRange intRange;
                CharSequence charSequence3;
                CharSequence charSequence4;
                int i2;
                if (this.nextSearchIndex < 0) {
                    this.nextState = 0;
                    this.nextItem = null;
                    return;
                }
                i = DelimitedRangesSequence.this.limit;
                int i3 = -1;
                if (i > 0) {
                    int i4 = this.counter + 1;
                    this.counter = i4;
                    i2 = DelimitedRangesSequence.this.limit;
                }
                int i5 = this.nextSearchIndex;
                charSequence = DelimitedRangesSequence.this.input;
                if (i5 <= charSequence.length()) {
                    function2 = DelimitedRangesSequence.this.getNextMatch;
                    charSequence2 = DelimitedRangesSequence.this.input;
                    Pair pair = (Pair) function2.invoke(charSequence2, Integer.valueOf(this.nextSearchIndex));
                    if (pair == null) {
                        int i6 = this.currentStartIndex;
                        charSequence3 = DelimitedRangesSequence.this.input;
                        intRange = new IntRange(i6, StringsKt__StringsKt.getLastIndex(charSequence3));
                        this.nextItem = intRange;
                        this.nextSearchIndex = i3;
                        this.nextState = 1;
                    }
                    int intValue = ((Number) pair.component1()).intValue();
                    int intValue2 = ((Number) pair.component2()).intValue();
                    until = RangesKt___RangesKt.until(this.currentStartIndex, intValue);
                    this.nextItem = until;
                    int i7 = intValue + intValue2;
                    this.currentStartIndex = i7;
                    i3 = i7 + (intValue2 == 0 ? 1 : 0);
                    this.nextSearchIndex = i3;
                    this.nextState = 1;
                }
                int i8 = this.currentStartIndex;
                charSequence4 = DelimitedRangesSequence.this.input;
                intRange = new IntRange(i8, StringsKt__StringsKt.getLastIndex(charSequence4));
                this.nextItem = intRange;
                this.nextSearchIndex = i3;
                this.nextState = 1;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.nextState == -1) {
                    calcNext();
                }
                return this.nextState == 1;
            }

            @Override // java.util.Iterator
            public IntRange next() {
                if (this.nextState == -1) {
                    calcNext();
                }
                if (this.nextState == 0) {
                    throw new NoSuchElementException();
                }
                IntRange intRange = this.nextItem;
                Intrinsics.checkNotNull(intRange, "null cannot be cast to non-null type kotlin.ranges.IntRange");
                this.nextItem = null;
                this.nextState = -1;
                return intRange;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }
        };
    }
}
