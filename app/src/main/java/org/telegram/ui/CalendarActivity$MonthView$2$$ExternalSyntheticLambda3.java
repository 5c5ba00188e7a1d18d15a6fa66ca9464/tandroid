package org.telegram.ui;

import android.view.View;
import org.telegram.ui.CalendarActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class CalendarActivity$MonthView$2$$ExternalSyntheticLambda3 implements View.OnClickListener {
    public final /* synthetic */ CalendarActivity.MonthView.AnonymousClass2 f$0;
    public final /* synthetic */ CalendarActivity.PeriodDay f$1;

    public /* synthetic */ CalendarActivity$MonthView$2$$ExternalSyntheticLambda3(CalendarActivity.MonthView.AnonymousClass2 anonymousClass2, CalendarActivity.PeriodDay periodDay) {
        this.f$0 = anonymousClass2;
        this.f$1 = periodDay;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$onLongPress$2(this.f$1, view);
    }
}
