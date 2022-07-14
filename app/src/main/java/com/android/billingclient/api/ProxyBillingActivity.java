package com.android.billingclient.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.ResultReceiver;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes3.dex */
public class ProxyBillingActivity extends Activity {
    static final String KEY_IN_APP_MESSAGE_RESULT_RECEIVER = "in_app_message_result_receiver";
    static final String KEY_PRICE_CHANGE_RESULT_RECEIVER = "result_receiver";
    private static final String KEY_SEND_CANCELLED_BROADCAST_IF_FINISHED = "send_cancelled_broadcast_if_finished";
    private static final int REQUEST_CODE_IN_APP_MESSAGE_FLOW = 101;
    private static final int REQUEST_CODE_LAUNCH_ACTIVITY = 100;
    private static final String TAG = "ProxyBillingActivity";
    private ResultReceiver inAppMessageResultReceiver;
    private ResultReceiver priceChangeResultReceiver;
    private boolean sendCancelledBroadcastIfFinished;

    private Intent makeAlternativeBillingIntent(String userChoicedata) {
        Intent intent = new Intent("com.android.vending.billing.ALTERNATIVE_BILLING");
        intent.setPackage(getApplicationContext().getPackageName());
        intent.putExtra("ALTERNATIVE_BILLING_USER_CHOICE_DATA", userChoicedata);
        return intent;
    }

    private Intent makePurchasesUpdatedIntent() {
        Intent intent = new Intent("com.android.vending.billing.PURCHASES_UPDATED");
        intent.setPackage(getApplicationContext().getPackageName());
        return intent;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x004a  */
    @Override // android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ResultReceiver resultReceiver;
        Intent intent;
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = null;
        if (requestCode == 100) {
            int responseCode = com.google.android.gms.internal.play_billing.zzb.zzi(data, TAG).getResponseCode();
            if (resultCode == -1) {
                if (responseCode != 0) {
                    resultCode = -1;
                } else {
                    responseCode = 0;
                    resultReceiver = this.priceChangeResultReceiver;
                    if (resultReceiver == null) {
                        if (data != null) {
                            bundle = data.getExtras();
                        }
                        resultReceiver.send(responseCode, bundle);
                    } else {
                        if (data != null) {
                            if (data.getExtras() != null) {
                                String string = data.getExtras().getString("ALTERNATIVE_BILLING_USER_CHOICE_DATA");
                                if (string != null) {
                                    intent = makeAlternativeBillingIntent(string);
                                } else {
                                    intent = makePurchasesUpdatedIntent();
                                    intent.putExtras(data.getExtras());
                                }
                            } else {
                                intent = makePurchasesUpdatedIntent();
                                com.google.android.gms.internal.play_billing.zzb.zzo(TAG, "Got null bundle!");
                                intent.putExtra("RESPONSE_CODE", 6);
                                intent.putExtra("DEBUG_MESSAGE", "An internal error occurred.");
                            }
                        } else {
                            intent = makePurchasesUpdatedIntent();
                        }
                        sendBroadcast(intent);
                    }
                }
            }
            com.google.android.gms.internal.play_billing.zzb.zzo(TAG, "Activity finished with resultCode " + resultCode + " and billing's responseCode: " + responseCode);
            resultReceiver = this.priceChangeResultReceiver;
            if (resultReceiver == null) {
            }
        } else if (requestCode == 101) {
            int zza = com.google.android.gms.internal.play_billing.zzb.zza(data, TAG);
            ResultReceiver resultReceiver2 = this.inAppMessageResultReceiver;
            if (resultReceiver2 != null) {
                if (data != null) {
                    bundle = data.getExtras();
                }
                resultReceiver2.send(zza, bundle);
            }
        } else {
            com.google.android.gms.internal.play_billing.zzb.zzo(TAG, "Got onActivityResult with wrong requestCode: " + requestCode + "; skipping...");
        }
        this.sendCancelledBroadcastIfFinished = false;
        finish();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        PendingIntent pendingIntent;
        int i;
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            com.google.android.gms.internal.play_billing.zzb.zzn(TAG, "Launching Play Store billing flow");
            if (getIntent().hasExtra("BUY_INTENT")) {
                pendingIntent = (PendingIntent) getIntent().getParcelableExtra("BUY_INTENT");
                i = 100;
            } else if (getIntent().hasExtra("SUBS_MANAGEMENT_INTENT")) {
                pendingIntent = (PendingIntent) getIntent().getParcelableExtra("SUBS_MANAGEMENT_INTENT");
                this.priceChangeResultReceiver = (ResultReceiver) getIntent().getParcelableExtra(KEY_PRICE_CHANGE_RESULT_RECEIVER);
                i = 100;
            } else if (getIntent().hasExtra("IN_APP_MESSAGE_INTENT")) {
                pendingIntent = (PendingIntent) getIntent().getParcelableExtra("IN_APP_MESSAGE_INTENT");
                this.inAppMessageResultReceiver = (ResultReceiver) getIntent().getParcelableExtra(KEY_IN_APP_MESSAGE_RESULT_RECEIVER);
                i = 101;
            } else {
                pendingIntent = null;
                i = 100;
            }
            try {
                this.sendCancelledBroadcastIfFinished = true;
                startIntentSenderForResult(pendingIntent.getIntentSender(), i, new Intent(), 0, 0, 0);
                return;
            } catch (IntentSender.SendIntentException e) {
                com.google.android.gms.internal.play_billing.zzb.zzp(TAG, "Got exception while trying to start a purchase flow.", e);
                ResultReceiver resultReceiver = this.priceChangeResultReceiver;
                if (resultReceiver != null) {
                    resultReceiver.send(6, null);
                } else {
                    ResultReceiver resultReceiver2 = this.inAppMessageResultReceiver;
                    if (resultReceiver2 != null) {
                        resultReceiver2.send(0, null);
                    } else {
                        Intent makePurchasesUpdatedIntent = makePurchasesUpdatedIntent();
                        makePurchasesUpdatedIntent.putExtra("RESPONSE_CODE", 6);
                        makePurchasesUpdatedIntent.putExtra("DEBUG_MESSAGE", "An internal error occurred.");
                        sendBroadcast(makePurchasesUpdatedIntent);
                    }
                }
                this.sendCancelledBroadcastIfFinished = false;
                finish();
                return;
            }
        }
        com.google.android.gms.internal.play_billing.zzb.zzn(TAG, "Launching Play Store billing flow from savedInstanceState");
        this.sendCancelledBroadcastIfFinished = savedInstanceState.getBoolean(KEY_SEND_CANCELLED_BROADCAST_IF_FINISHED, false);
        if (savedInstanceState.containsKey(KEY_PRICE_CHANGE_RESULT_RECEIVER)) {
            this.priceChangeResultReceiver = (ResultReceiver) savedInstanceState.getParcelable(KEY_PRICE_CHANGE_RESULT_RECEIVER);
        } else if (!savedInstanceState.containsKey(KEY_IN_APP_MESSAGE_RESULT_RECEIVER)) {
        } else {
            this.inAppMessageResultReceiver = (ResultReceiver) savedInstanceState.getParcelable(KEY_IN_APP_MESSAGE_RESULT_RECEIVER);
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing() && this.sendCancelledBroadcastIfFinished) {
            Intent makePurchasesUpdatedIntent = makePurchasesUpdatedIntent();
            makePurchasesUpdatedIntent.putExtra("RESPONSE_CODE", 1);
            makePurchasesUpdatedIntent.putExtra("DEBUG_MESSAGE", "Billing dialog closed.");
            sendBroadcast(makePurchasesUpdatedIntent);
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        ResultReceiver resultReceiver = this.priceChangeResultReceiver;
        if (resultReceiver != null) {
            outState.putParcelable(KEY_PRICE_CHANGE_RESULT_RECEIVER, resultReceiver);
        }
        ResultReceiver resultReceiver2 = this.inAppMessageResultReceiver;
        if (resultReceiver2 != null) {
            outState.putParcelable(KEY_IN_APP_MESSAGE_RESULT_RECEIVER, resultReceiver2);
        }
        outState.putBoolean(KEY_SEND_CANCELLED_BROADCAST_IF_FINISHED, this.sendCancelledBroadcastIfFinished);
    }
}
