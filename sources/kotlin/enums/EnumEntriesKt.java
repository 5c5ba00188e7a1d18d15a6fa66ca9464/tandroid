package kotlin.enums;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
public abstract class EnumEntriesKt {
    public static final EnumEntries enumEntries(final Enum[] entries) {
        Intrinsics.checkNotNullParameter(entries, "entries");
        EnumEntriesList enumEntriesList = new EnumEntriesList(new Function0() { // from class: kotlin.enums.EnumEntriesKt$enumEntries$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public final Enum[] invoke() {
                return entries;
            }
        });
        enumEntriesList.size();
        return enumEntriesList;
    }
}
