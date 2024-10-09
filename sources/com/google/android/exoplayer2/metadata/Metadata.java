package com.google.android.exoplayer2.metadata;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.util.Util;
import com.google.common.primitives.Longs;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public final class Metadata implements Parcelable {
    public static final Parcelable.Creator<Metadata> CREATOR = new Parcelable.Creator() { // from class: com.google.android.exoplayer2.metadata.Metadata.1
        @Override // android.os.Parcelable.Creator
        public Metadata createFromParcel(Parcel parcel) {
            return new Metadata(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public Metadata[] newArray(int i) {
            return new Metadata[i];
        }
    };
    private final Entry[] entries;
    public final long presentationTimeUs;

    /* loaded from: classes.dex */
    public interface Entry extends Parcelable {

        /* loaded from: classes.dex */
        public abstract /* synthetic */ class -CC {
            public static byte[] $default$getWrappedMetadataBytes(Entry entry) {
                return null;
            }

            public static Format $default$getWrappedMetadataFormat(Entry entry) {
                return null;
            }

            public static void $default$populateMediaMetadata(Entry entry, MediaMetadata.Builder builder) {
            }
        }

        byte[] getWrappedMetadataBytes();

        Format getWrappedMetadataFormat();

        void populateMediaMetadata(MediaMetadata.Builder builder);
    }

    public Metadata(long j, List list) {
        this(j, (Entry[]) list.toArray(new Entry[0]));
    }

    public Metadata(long j, Entry... entryArr) {
        this.presentationTimeUs = j;
        this.entries = entryArr;
    }

    Metadata(Parcel parcel) {
        this.entries = new Entry[parcel.readInt()];
        int i = 0;
        while (true) {
            Entry[] entryArr = this.entries;
            if (i >= entryArr.length) {
                this.presentationTimeUs = parcel.readLong();
                return;
            } else {
                entryArr[i] = (Entry) parcel.readParcelable(Entry.class.getClassLoader());
                i++;
            }
        }
    }

    public Metadata(List list) {
        this((Entry[]) list.toArray(new Entry[0]));
    }

    public Metadata(Entry... entryArr) {
        this(-9223372036854775807L, entryArr);
    }

    public Metadata copyWithAppendedEntries(Entry... entryArr) {
        return entryArr.length == 0 ? this : new Metadata(this.presentationTimeUs, (Entry[]) Util.nullSafeArrayConcatenation(this.entries, entryArr));
    }

    public Metadata copyWithAppendedEntriesFrom(Metadata metadata) {
        return metadata == null ? this : copyWithAppendedEntries(metadata.entries);
    }

    public Metadata copyWithPresentationTimeUs(long j) {
        return this.presentationTimeUs == j ? this : new Metadata(j, this.entries);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Metadata.class != obj.getClass()) {
            return false;
        }
        Metadata metadata = (Metadata) obj;
        return Arrays.equals(this.entries, metadata.entries) && this.presentationTimeUs == metadata.presentationTimeUs;
    }

    public Entry get(int i) {
        return this.entries[i];
    }

    public int hashCode() {
        return (Arrays.hashCode(this.entries) * 31) + Longs.hashCode(this.presentationTimeUs);
    }

    public int length() {
        return this.entries.length;
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("entries=");
        sb.append(Arrays.toString(this.entries));
        if (this.presentationTimeUs == -9223372036854775807L) {
            str = "";
        } else {
            str = ", presentationTimeUs=" + this.presentationTimeUs;
        }
        sb.append(str);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.entries.length);
        for (Entry entry : this.entries) {
            parcel.writeParcelable(entry, 0);
        }
        parcel.writeLong(this.presentationTimeUs);
    }
}
