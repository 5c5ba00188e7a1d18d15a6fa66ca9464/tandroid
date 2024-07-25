package com.google.android.gms.internal.play_billing;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;
import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
final class zzbk extends zzal implements RandomAccess, zzdm {
    private double[] zzb;
    private int zzc;

    static {
        new zzbk(new double[0], 0, false);
    }

    zzbk() {
        this(new double[10], 0, true);
    }

    private final String zzf(int i) {
        int i2 = this.zzc;
        return "Index:" + i + ", Size:" + i2;
    }

    private final void zzg(int i) {
        if (i < 0 || i >= this.zzc) {
            throw new IndexOutOfBoundsException(zzf(i));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i, Object obj) {
        int i2;
        double doubleValue = ((Double) obj).doubleValue();
        zza();
        if (i < 0 || i > (i2 = this.zzc)) {
            throw new IndexOutOfBoundsException(zzf(i));
        }
        double[] dArr = this.zzb;
        if (i2 < dArr.length) {
            System.arraycopy(dArr, i, dArr, i + 1, i2 - i);
        } else {
            double[] dArr2 = new double[((i2 * 3) / 2) + 1];
            System.arraycopy(dArr, 0, dArr2, 0, i);
            System.arraycopy(this.zzb, i, dArr2, i + 1, this.zzc - i);
            this.zzb = dArr2;
        }
        this.zzb[i] = doubleValue;
        this.zzc++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.play_billing.zzal, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection collection) {
        zza();
        byte[] bArr = zzcg.zzd;
        Objects.requireNonNull(collection);
        if (!(collection instanceof zzbk)) {
            return super.addAll(collection);
        }
        zzbk zzbkVar = (zzbk) collection;
        int i = zzbkVar.zzc;
        if (i == 0) {
            return false;
        }
        int i2 = this.zzc;
        if (ConnectionsManager.DEFAULT_DATACENTER_ID - i2 >= i) {
            int i3 = i2 + i;
            double[] dArr = this.zzb;
            if (i3 > dArr.length) {
                this.zzb = Arrays.copyOf(dArr, i3);
            }
            System.arraycopy(zzbkVar.zzb, 0, this.zzb, this.zzc, zzbkVar.zzc);
            this.zzc = i3;
            ((AbstractList) this).modCount++;
            return true;
        }
        throw new OutOfMemoryError();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override // com.google.android.gms.internal.play_billing.zzal, java.util.AbstractList, java.util.Collection, java.util.List
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzbk) {
            zzbk zzbkVar = (zzbk) obj;
            if (this.zzc != zzbkVar.zzc) {
                return false;
            }
            double[] dArr = zzbkVar.zzb;
            for (int i = 0; i < this.zzc; i++) {
                if (Double.doubleToLongBits(this.zzb[i]) != Double.doubleToLongBits(dArr[i])) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i) {
        zzg(i);
        return Double.valueOf(this.zzb[i]);
    }

    @Override // com.google.android.gms.internal.play_billing.zzal, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.zzc; i2++) {
            long doubleToLongBits = Double.doubleToLongBits(this.zzb[i2]);
            byte[] bArr = zzcg.zzd;
            i = (i * 31) + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
        }
        return i;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Double) {
            double doubleValue = ((Double) obj).doubleValue();
            int i = this.zzc;
            for (int i2 = 0; i2 < i; i2++) {
                if (this.zzb[i2] == doubleValue) {
                    return i2;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.play_billing.zzal, java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object remove(int i) {
        int i2;
        zza();
        zzg(i);
        double[] dArr = this.zzb;
        double d = dArr[i];
        if (i < this.zzc - 1) {
            System.arraycopy(dArr, i + 1, dArr, i, (i2 - i) - 1);
        }
        this.zzc--;
        ((AbstractList) this).modCount++;
        return Double.valueOf(d);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i, int i2) {
        zza();
        if (i2 >= i) {
            double[] dArr = this.zzb;
            System.arraycopy(dArr, i2, dArr, i, this.zzc - i2);
            this.zzc -= i2 - i;
            ((AbstractList) this).modCount++;
            return;
        }
        throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object set(int i, Object obj) {
        double doubleValue = ((Double) obj).doubleValue();
        zza();
        zzg(i);
        double[] dArr = this.zzb;
        double d = dArr[i];
        dArr[i] = doubleValue;
        return Double.valueOf(d);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.play_billing.zzcf
    public final /* bridge */ /* synthetic */ zzcf zzd(int i) {
        if (i < this.zzc) {
            throw new IllegalArgumentException();
        }
        return new zzbk(Arrays.copyOf(this.zzb, i), this.zzc, true);
    }

    public final void zze(double d) {
        zza();
        int i = this.zzc;
        double[] dArr = this.zzb;
        if (i == dArr.length) {
            double[] dArr2 = new double[((i * 3) / 2) + 1];
            System.arraycopy(dArr, 0, dArr2, 0, i);
            this.zzb = dArr2;
        }
        double[] dArr3 = this.zzb;
        int i2 = this.zzc;
        this.zzc = i2 + 1;
        dArr3[i2] = d;
    }

    private zzbk(double[] dArr, int i, boolean z) {
        super(z);
        this.zzb = dArr;
        this.zzc = i;
    }

    @Override // com.google.android.gms.internal.play_billing.zzal, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        zze(((Double) obj).doubleValue());
        return true;
    }
}
