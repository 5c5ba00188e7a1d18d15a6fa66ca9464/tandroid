package com.stripe.android;

import android.os.AsyncTask;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.StripeException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.net.RequestOptions;
import com.stripe.android.net.StripeApiHandler;
import com.stripe.android.util.StripeNetworkUtils;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class Stripe {
    private String defaultPublishableKey;
    TokenCreator tokenCreator = new TokenCreator() { // from class: com.stripe.android.Stripe.1
        @Override // com.stripe.android.Stripe.TokenCreator
        public void create(final Card card, final String str, Executor executor, final TokenCallback tokenCallback) {
            Stripe.this.executeTokenTask(executor, new AsyncTask() { // from class: com.stripe.android.Stripe.1.1
                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Multi-variable type inference failed */
                @Override // android.os.AsyncTask
                public ResponseWrapper doInBackground(Void... voidArr) {
                    Exception exc = null;
                    Object[] objArr = 0;
                    Object[] objArr2 = 0;
                    Object[] objArr3 = 0;
                    try {
                        return new ResponseWrapper(StripeApiHandler.createToken(StripeNetworkUtils.hashMapFromCard(card), RequestOptions.builder(str).build()), exc);
                    } catch (StripeException e) {
                        return new ResponseWrapper(objArr2 == true ? 1 : 0, e);
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public void onPostExecute(ResponseWrapper responseWrapper) {
                    Stripe.this.tokenTaskPostExecution(responseWrapper, tokenCallback);
                }
            });
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ResponseWrapper {
        final Exception error;
        final Token token;

        private ResponseWrapper(Token token, Exception exc) {
            this.error = exc;
            this.token = token;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface TokenCreator {
        void create(Card card, String str, Executor executor, TokenCallback tokenCallback);
    }

    public Stripe(String str) {
        setDefaultPublishableKey(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeTokenTask(Executor executor, AsyncTask asyncTask) {
        if (executor != null) {
            asyncTask.executeOnExecutor(executor, new Void[0]);
        } else {
            asyncTask.execute(new Void[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tokenTaskPostExecution(ResponseWrapper responseWrapper, TokenCallback tokenCallback) {
        Token token = responseWrapper.token;
        if (token != null) {
            tokenCallback.onSuccess(token);
            return;
        }
        Exception exc = responseWrapper.error;
        if (exc == null) {
            exc = new RuntimeException("Somehow got neither a token response or an error response");
        }
        tokenCallback.onError(exc);
    }

    private void validateKey(String str) {
        if (str == null || str.length() == 0) {
            throw new AuthenticationException("Invalid Publishable Key: You must use a valid publishable key to create a token.  For more info, see https://stripe.com/docs/stripe.js.", null, 0);
        }
        if (str.startsWith("sk_")) {
            throw new AuthenticationException("Invalid Publishable Key: You are using a secret key to create a token, instead of the publishable one. For more info, see https://stripe.com/docs/stripe.js", null, 0);
        }
    }

    public void createToken(Card card, TokenCallback tokenCallback) {
        createToken(card, this.defaultPublishableKey, tokenCallback);
    }

    public void createToken(Card card, String str, TokenCallback tokenCallback) {
        createToken(card, str, null, tokenCallback);
    }

    public void createToken(Card card, String str, Executor executor, TokenCallback tokenCallback) {
        try {
            if (card == null) {
                throw new RuntimeException("Required Parameter: 'card' is required to create a token");
            }
            if (tokenCallback == null) {
                throw new RuntimeException("Required Parameter: 'callback' is required to use the created token and handle errors");
            }
            validateKey(str);
            this.tokenCreator.create(card, str, executor, tokenCallback);
        } catch (AuthenticationException e) {
            tokenCallback.onError(e);
        }
    }

    public void setDefaultPublishableKey(String str) {
        validateKey(str);
        this.defaultPublishableKey = str;
    }
}
