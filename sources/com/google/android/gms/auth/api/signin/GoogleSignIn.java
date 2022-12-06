package com.google.android.gms.auth.api.signin;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.auth.api.signin.internal.zbm;
import com.google.android.gms.common.internal.ApiExceptionUtil;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
/* compiled from: com.google.android.gms:play-services-auth@@20.4.0 */
/* loaded from: classes.dex */
public final class GoogleSignIn {
    public static Task<GoogleSignInAccount> getSignedInAccountFromIntent(Intent intent) {
        GoogleSignInResult zbd = zbm.zbd(intent);
        GoogleSignInAccount signInAccount = zbd.getSignInAccount();
        if (!zbd.getStatus().isSuccess() || signInAccount == null) {
            return Tasks.forException(ApiExceptionUtil.fromStatus(zbd.getStatus()));
        }
        return Tasks.forResult(signInAccount);
    }

    public static GoogleSignInClient getClient(Context context, GoogleSignInOptions googleSignInOptions) {
        return new GoogleSignInClient(context, (GoogleSignInOptions) Preconditions.checkNotNull(googleSignInOptions));
    }
}
