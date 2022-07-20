package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class SpliceNullCommand extends SpliceCommand {
    public static final Parcelable.Creator<SpliceNullCommand> CREATOR = new AnonymousClass1();

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
    }

    /* renamed from: com.google.android.exoplayer2.metadata.scte35.SpliceNullCommand$1 */
    /* loaded from: classes.dex */
    class AnonymousClass1 implements Parcelable.Creator<SpliceNullCommand> {
        AnonymousClass1() {
        }

        @Override // android.os.Parcelable.Creator
        public SpliceNullCommand createFromParcel(Parcel parcel) {
            return new SpliceNullCommand();
        }

        @Override // android.os.Parcelable.Creator
        public SpliceNullCommand[] newArray(int i) {
            return new SpliceNullCommand[i];
        }
    }
}
