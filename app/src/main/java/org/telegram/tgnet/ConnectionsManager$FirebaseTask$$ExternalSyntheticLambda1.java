package org.telegram.tgnet;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import org.telegram.tgnet.ConnectionsManager;
/* loaded from: classes.dex */
public final /* synthetic */ class ConnectionsManager$FirebaseTask$$ExternalSyntheticLambda1 implements OnCompleteListener {
    public final /* synthetic */ ConnectionsManager.FirebaseTask f$0;

    public /* synthetic */ ConnectionsManager$FirebaseTask$$ExternalSyntheticLambda1(ConnectionsManager.FirebaseTask firebaseTask) {
        this.f$0 = firebaseTask;
    }

    @Override // com.google.android.gms.tasks.OnCompleteListener
    public final void onComplete(Task task) {
        this.f$0.lambda$doInBackground$2(task);
    }
}
