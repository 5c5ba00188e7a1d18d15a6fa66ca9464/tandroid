package com.google.android.gms.auth.api.signin;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.auth.api.signin.internal.zbm;
import com.google.android.gms.common.internal.ApiExceptionUtil;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
/* loaded from: classes.dex */
public abstract class GoogleSignIn {
    public static GoogleSignInClient getClient(Context context, GoogleSignInOptions googleSignInOptions) {
        return new GoogleSignInClient(context, (GoogleSignInOptions) Preconditions.checkNotNull(googleSignInOptions));
    }

    public static Task getSignedInAccountFromIntent(Intent intent) {
        GoogleSignInResult zbd = zbm.zbd(intent);
        GoogleSignInAccount signInAccount = zbd.getSignInAccount();
        return (!zbd.getStatus().isSuccess() || signInAccount == null) ? Tasks.forException(ApiExceptionUtil.fromStatus(zbd.getStatus())) : Tasks.forResult(signInAccount);
    }
}
