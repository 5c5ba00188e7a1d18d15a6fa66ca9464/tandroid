package com.google.android.gms.internal.clearcut;
/* loaded from: classes.dex */
public class zzfl extends Enum<zzfl> {
    public static final zzfl zzqc;
    public static final zzfl zzqd;
    public static final zzfl zzqe;
    public static final zzfl zzqf;
    public static final zzfl zzqg;
    public static final zzfl zzqh;
    public static final zzfl zzqi;
    public static final zzfl zzqj;
    public static final zzfl zzqk;
    public static final zzfl zzql;
    public static final zzfl zzqm;
    public static final zzfl zzqn;
    public static final zzfl zzqo;
    public static final zzfl zzqp;
    public static final zzfl zzqq;
    public static final zzfl zzqr;
    public static final zzfl zzqs;
    public static final zzfl zzqt;
    private static final /* synthetic */ zzfl[] zzqw;
    private final zzfq zzqu;
    private final int zzqv;

    static {
        zzfl zzflVar = new zzfl("DOUBLE", 0, zzfq.DOUBLE, 1);
        zzqc = zzflVar;
        zzfl zzflVar2 = new zzfl("FLOAT", 1, zzfq.FLOAT, 5);
        zzqd = zzflVar2;
        zzfq zzfqVar = zzfq.LONG;
        zzfl zzflVar3 = new zzfl("INT64", 2, zzfqVar, 0);
        zzqe = zzflVar3;
        zzfl zzflVar4 = new zzfl("UINT64", 3, zzfqVar, 0);
        zzqf = zzflVar4;
        zzfq zzfqVar2 = zzfq.INT;
        zzfl zzflVar5 = new zzfl("INT32", 4, zzfqVar2, 0);
        zzqg = zzflVar5;
        zzfl zzflVar6 = new zzfl("FIXED64", 5, zzfqVar, 1);
        zzqh = zzflVar6;
        zzfl zzflVar7 = new zzfl("FIXED32", 6, zzfqVar2, 5);
        zzqi = zzflVar7;
        zzfl zzflVar8 = new zzfl("BOOL", 7, zzfq.BOOLEAN, 0);
        zzqj = zzflVar8;
        zzfm zzfmVar = new zzfm("STRING", 8, zzfq.STRING, 2);
        zzqk = zzfmVar;
        zzfq zzfqVar3 = zzfq.MESSAGE;
        zzfn zzfnVar = new zzfn("GROUP", 9, zzfqVar3, 3);
        zzql = zzfnVar;
        zzfo zzfoVar = new zzfo("MESSAGE", 10, zzfqVar3, 2);
        zzqm = zzfoVar;
        zzfp zzfpVar = new zzfp("BYTES", 11, zzfq.BYTE_STRING, 2);
        zzqn = zzfpVar;
        zzfl zzflVar9 = new zzfl("UINT32", 12, zzfqVar2, 0);
        zzqo = zzflVar9;
        zzfl zzflVar10 = new zzfl("ENUM", 13, zzfq.ENUM, 0);
        zzqp = zzflVar10;
        zzfl zzflVar11 = new zzfl("SFIXED32", 14, zzfqVar2, 5);
        zzqq = zzflVar11;
        zzfl zzflVar12 = new zzfl("SFIXED64", 15, zzfqVar, 1);
        zzqr = zzflVar12;
        zzfl zzflVar13 = new zzfl("SINT32", 16, zzfqVar2, 0);
        zzqs = zzflVar13;
        zzfl zzflVar14 = new zzfl("SINT64", 17, zzfqVar, 0);
        zzqt = zzflVar14;
        zzqw = new zzfl[]{zzflVar, zzflVar2, zzflVar3, zzflVar4, zzflVar5, zzflVar6, zzflVar7, zzflVar8, zzfmVar, zzfnVar, zzfoVar, zzfpVar, zzflVar9, zzflVar10, zzflVar11, zzflVar12, zzflVar13, zzflVar14};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public zzfl(String str, int i, zzfq zzfqVar, int i2) {
        super(str, i);
        this.zzqu = zzfqVar;
        this.zzqv = i2;
    }

    public static zzfl[] values() {
        return (zzfl[]) zzqw.clone();
    }

    public final zzfq zzek() {
        return this.zzqu;
    }
}
