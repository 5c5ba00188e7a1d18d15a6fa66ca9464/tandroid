package org.telegram.ui.Components.Premium;
/* loaded from: classes3.dex */
class DoubledLimitsBottomSheet$Limit {
    final int current;
    final int defaultLimit;
    final int premiumLimit;
    final String subtitle;
    final String title;
    public int yOffset;

    private DoubledLimitsBottomSheet$Limit(String str, String str2, int i, int i2) {
        this.current = -1;
        this.title = str;
        this.subtitle = str2;
        this.defaultLimit = i;
        this.premiumLimit = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ DoubledLimitsBottomSheet$Limit(String str, String str2, int i, int i2, DoubledLimitsBottomSheet$1 doubledLimitsBottomSheet$1) {
        this(str, str2, i, i2);
    }
}
