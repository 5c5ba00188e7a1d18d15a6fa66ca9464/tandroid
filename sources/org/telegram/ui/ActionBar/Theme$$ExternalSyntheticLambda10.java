package org.telegram.ui.ActionBar;

import java.util.Comparator;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes.dex */
public final /* synthetic */ class Theme$$ExternalSyntheticLambda10 implements Comparator {
    public static final /* synthetic */ Theme$$ExternalSyntheticLambda10 INSTANCE = new Theme$$ExternalSyntheticLambda10();

    private /* synthetic */ Theme$$ExternalSyntheticLambda10() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$sortAccents$0;
        lambda$sortAccents$0 = Theme.lambda$sortAccents$0((Theme.ThemeAccent) obj, (Theme.ThemeAccent) obj2);
        return lambda$sortAccents$0;
    }
}
