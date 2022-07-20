package org.telegram.ui;

import android.view.View;
import android.widget.ImageView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$$ExternalSyntheticLambda16 implements View.OnLongClickListener {
    public final /* synthetic */ ProfileActivity f$0;
    public final /* synthetic */ ImageView f$1;

    public /* synthetic */ ProfileActivity$$ExternalSyntheticLambda16(ProfileActivity profileActivity, ImageView imageView) {
        this.f$0 = profileActivity;
        this.f$1 = imageView;
    }

    @Override // android.view.View.OnLongClickListener
    public final boolean onLongClick(View view) {
        boolean lambda$createActionBar$2;
        lambda$createActionBar$2 = this.f$0.lambda$createActionBar$2(this.f$1, view);
        return lambda$createActionBar$2;
    }
}
