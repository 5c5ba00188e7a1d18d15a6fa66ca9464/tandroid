package org.telegram.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MrzRecognizer;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.CameraScanActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.ShareLocationDrawable;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
@TargetApi(23)
/* loaded from: classes3.dex */
public class ActionIntroActivity extends BaseFragment implements LocationController.LocationFetchCallback {
    private TextView buttonTextView;
    private int[] colors;
    private String currentGroupCreateAddress;
    private String currentGroupCreateDisplayAddress;
    private Location currentGroupCreateLocation;
    private int currentType;
    private LinearLayout descriptionLayout;
    private TextView descriptionText;
    private TextView descriptionText2;
    private TextView[] desctiptionLines = new TextView[6];
    private Drawable drawable1;
    private Drawable drawable2;
    private boolean flickerButton;
    private RLottieImageView imageView;
    private ActionIntroQRLoginDelegate qrLoginDelegate;
    private boolean showingAsBottomSheet;
    private TextView subtitleTextView;
    private TextView titleTextView;

    /* loaded from: classes3.dex */
    public interface ActionIntroQRLoginDelegate {
        void didFindQRCode(String str);
    }

    public static /* synthetic */ boolean lambda$createView$0(View view, MotionEvent motionEvent) {
        return true;
    }

    public ActionIntroActivity(int i) {
        this.currentType = i;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        int i;
        ActionBar actionBar = this.actionBar;
        int i2 = 0;
        if (actionBar != null) {
            actionBar.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.actionBar.setBackButtonImage(2131165449);
            this.actionBar.setItemsColor(Theme.getColor("windowBackgroundWhiteGrayText2"), false);
            this.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarWhiteSelector"), false);
            this.actionBar.setCastShadows(false);
            this.actionBar.setAddToContainer(false);
            this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        }
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.fragmentView = anonymousClass2;
        anonymousClass2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        ViewGroup viewGroup = (ViewGroup) this.fragmentView;
        viewGroup.setOnTouchListener(ActionIntroActivity$$ExternalSyntheticLambda6.INSTANCE);
        ActionBar actionBar2 = this.actionBar;
        if (actionBar2 != null) {
            viewGroup.addView(actionBar2);
        }
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.imageView = rLottieImageView;
        viewGroup.addView(rLottieImageView);
        TextView textView = new TextView(context);
        this.titleTextView = textView;
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        int i3 = 1;
        this.titleTextView.setGravity(1);
        this.titleTextView.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
        this.titleTextView.setTextSize(1, 24.0f);
        viewGroup.addView(this.titleTextView);
        TextView textView2 = new TextView(context);
        this.subtitleTextView = textView2;
        int i4 = 3;
        textView2.setTextColor(Theme.getColor(this.currentType == 3 ? "featuredStickers_addButton" : "windowBackgroundWhiteBlackText"));
        this.subtitleTextView.setGravity(1);
        float f = 15.0f;
        this.subtitleTextView.setTextSize(1, 15.0f);
        this.subtitleTextView.setSingleLine(true);
        this.subtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        int i5 = 2;
        if (this.currentType == 2) {
            this.subtitleTextView.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
        } else {
            this.subtitleTextView.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
        }
        this.subtitleTextView.setVisibility(8);
        viewGroup.addView(this.subtitleTextView);
        TextView textView3 = new TextView(context);
        this.descriptionText = textView3;
        textView3.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
        this.descriptionText.setGravity(1);
        this.descriptionText.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        this.descriptionText.setTextSize(1, 15.0f);
        int i6 = this.currentType;
        if (i6 == 6 || i6 == 3) {
            this.descriptionText.setPadding(AndroidUtilities.dp(48.0f), 0, AndroidUtilities.dp(48.0f), 0);
        } else if (i6 == 2) {
            this.descriptionText.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
        } else {
            this.descriptionText.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
        }
        viewGroup.addView(this.descriptionText);
        String str = "";
        if (this.currentType == 5) {
            LinearLayout linearLayout = new LinearLayout(context);
            this.descriptionLayout = linearLayout;
            linearLayout.setOrientation(1);
            this.descriptionLayout.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
            this.descriptionLayout.setGravity(LocaleController.isRTL ? 5 : 3);
            viewGroup.addView(this.descriptionLayout);
            int i7 = 0;
            while (i7 < i4) {
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(i2);
                this.descriptionLayout.addView(linearLayout2, LayoutHelper.createLinear(-2, -2, 0.0f, 0.0f, 0.0f, i7 != i5 ? 7.0f : 0.0f));
                int i8 = i7 * 2;
                this.desctiptionLines[i8] = new TextView(context);
                this.desctiptionLines[i8].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.desctiptionLines[i8].setGravity(LocaleController.isRTL ? 5 : 3);
                this.desctiptionLines[i8].setTextSize(i3, f);
                TextView textView4 = this.desctiptionLines[i8];
                String str2 = LocaleController.isRTL ? ".%d" : "%d.";
                Object[] objArr = new Object[i3];
                int i9 = i7 + 1;
                objArr[i2] = Integer.valueOf(i9);
                textView4.setText(String.format(str2, objArr));
                this.desctiptionLines[i8].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                int i10 = i8 + 1;
                this.desctiptionLines[i10] = new TextView(context);
                this.desctiptionLines[i10].setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.desctiptionLines[i10].setGravity(LocaleController.isRTL ? 5 : 3);
                this.desctiptionLines[i10].setTextSize(i3, f);
                if (i7 == 0) {
                    this.desctiptionLines[i10].setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
                    this.desctiptionLines[i10].setHighlightColor(Theme.getColor("windowBackgroundWhiteLinkSelection"));
                    String string = LocaleController.getString("AuthAnotherClientInfo1", 2131624526);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
                    int indexOf = string.indexOf(42);
                    int lastIndexOf = string.lastIndexOf(42);
                    if (indexOf != -1 && lastIndexOf != -1 && indexOf != lastIndexOf) {
                        this.desctiptionLines[i10].setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                        spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 1, (CharSequence) str);
                        spannableStringBuilder.replace(indexOf, indexOf + 1, (CharSequence) str);
                        spannableStringBuilder.setSpan(new URLSpanNoUnderline(LocaleController.getString("AuthAnotherClientDownloadClientUrl", 2131624525)), indexOf, lastIndexOf - 1, 33);
                    }
                    this.desctiptionLines[i10].setText(spannableStringBuilder);
                } else if (i7 == 1) {
                    this.desctiptionLines[i10].setText(LocaleController.getString("AuthAnotherClientInfo2", 2131624527));
                } else {
                    this.desctiptionLines[i10].setText(LocaleController.getString("AuthAnotherClientInfo3", 2131624528));
                }
                if (LocaleController.isRTL) {
                    linearLayout2.setGravity(5);
                    linearLayout2.addView(this.desctiptionLines[i10], LayoutHelper.createLinear(0, -2, 1.0f));
                    linearLayout2.addView(this.desctiptionLines[i8], LayoutHelper.createLinear(-2, -2, 4.0f, 0.0f, 0.0f, 0.0f));
                } else {
                    linearLayout2.addView(this.desctiptionLines[i8], LayoutHelper.createLinear(-2, -2, 0.0f, 0.0f, 4.0f, 0.0f));
                    linearLayout2.addView(this.desctiptionLines[i10], LayoutHelper.createLinear(-2, -2));
                }
                i7 = i9;
                i2 = 0;
                i3 = 1;
                f = 15.0f;
                i4 = 3;
                i5 = 2;
            }
            this.descriptionText.setVisibility(8);
        }
        TextView textView5 = new TextView(context);
        this.descriptionText2 = textView5;
        textView5.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
        this.descriptionText2.setGravity(1);
        this.descriptionText2.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        this.descriptionText2.setTextSize(1, 13.0f);
        this.descriptionText2.setVisibility(8);
        if (this.currentType == 2) {
            i = 0;
            this.descriptionText2.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        } else {
            i = 0;
            this.descriptionText2.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
        }
        viewGroup.addView(this.descriptionText2);
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        this.buttonTextView = anonymousClass3;
        anonymousClass3.setPadding(AndroidUtilities.dp(34.0f), i, AndroidUtilities.dp(34.0f), i);
        this.buttonTextView.setGravity(17);
        this.buttonTextView.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        this.buttonTextView.setTextSize(1, 14.0f);
        this.buttonTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        int i11 = this.currentType;
        this.buttonTextView.setBackground(Theme.AdaptiveRipple.filledRect("featuredStickers_addButton", (i11 == 6 || i11 == 3) ? 6 : 4));
        viewGroup.addView(this.buttonTextView);
        this.buttonTextView.setOnClickListener(new ActionIntroActivity$$ExternalSyntheticLambda3(this));
        switch (this.currentType) {
            case 0:
                this.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                this.imageView.setAnimation(2131558417, 200, 200);
                this.titleTextView.setText(LocaleController.getString("ChannelAlertTitle", 2131624881));
                this.descriptionText.setText(LocaleController.getString("ChannelAlertText", 2131624880));
                this.buttonTextView.setText(LocaleController.getString("ChannelAlertCreate2", 2131624879));
                this.imageView.playAnimation();
                this.flickerButton = true;
                break;
            case 1:
                this.imageView.setBackgroundDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(100.0f), Theme.getColor("chats_archiveBackground")));
                this.imageView.setImageDrawable(new ShareLocationDrawable(context, 3));
                this.imageView.setScaleType(ImageView.ScaleType.CENTER);
                this.titleTextView.setText(LocaleController.getString("PeopleNearby", 2131627453));
                this.descriptionText.setText(LocaleController.getString("PeopleNearbyAccessInfo", 2131627454));
                this.buttonTextView.setText(LocaleController.getString("PeopleNearbyAllowAccess", 2131627455));
                break;
            case 2:
                this.subtitleTextView.setVisibility(0);
                this.descriptionText2.setVisibility(0);
                this.imageView.setImageResource(Theme.getCurrentTheme().isDark() ? 2131165445 : 2131165444);
                this.imageView.setScaleType(ImageView.ScaleType.CENTER);
                TextView textView6 = this.subtitleTextView;
                String str3 = this.currentGroupCreateDisplayAddress;
                if (str3 != null) {
                    str = str3;
                }
                textView6.setText(str);
                this.titleTextView.setText(LocaleController.getString("NearbyCreateGroup", 2131626756));
                this.descriptionText.setText(LocaleController.getString("NearbyCreateGroupInfo", 2131626757));
                this.descriptionText2.setText(LocaleController.getString("NearbyCreateGroupInfo2", 2131626758));
                this.buttonTextView.setText(LocaleController.getString("NearbyStartGroup", 2131626761));
                break;
            case 3:
                this.subtitleTextView.setVisibility(0);
                this.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                this.imageView.setAnimation(2131558569, 200, 200);
                this.imageView.setOnClickListener(new ActionIntroActivity$$ExternalSyntheticLambda4(this));
                UserConfig userConfig = getUserConfig();
                TLRPC$User user = getMessagesController().getUser(Long.valueOf(userConfig.clientUserId));
                if (user == null) {
                    user = userConfig.getCurrentUser();
                }
                if (user != null) {
                    TextView textView7 = this.subtitleTextView;
                    PhoneFormat phoneFormat = PhoneFormat.getInstance();
                    textView7.setText(LocaleController.formatString("PhoneNumberKeepButton", 2131627499, phoneFormat.format("+" + user.phone)));
                }
                this.subtitleTextView.setOnClickListener(new ActionIntroActivity$$ExternalSyntheticLambda2(this));
                this.titleTextView.setText(LocaleController.getString("PhoneNumberChange2", 2131627495));
                this.descriptionText.setText(AndroidUtilities.replaceTags(LocaleController.getString("PhoneNumberHelp", 2131627498)));
                this.buttonTextView.setText(LocaleController.getString("PhoneNumberChange2", 2131627495));
                this.imageView.playAnimation();
                this.flickerButton = true;
                break;
            case 4:
                this.imageView.setBackgroundDrawable(Theme.createCircleDrawable(AndroidUtilities.dp(100.0f), Theme.getColor("chats_archiveBackground")));
                this.imageView.setImageDrawable(new ShareLocationDrawable(context, 3));
                this.imageView.setScaleType(ImageView.ScaleType.CENTER);
                this.titleTextView.setText(LocaleController.getString("PeopleNearby", 2131627453));
                this.descriptionText.setText(LocaleController.getString("PeopleNearbyGpsInfo", 2131627458));
                this.buttonTextView.setText(LocaleController.getString("PeopleNearbyGps", 2131627457));
                break;
            case 5:
                this.colors = new int[8];
                updateColors();
                this.imageView.setAnimation(2131558518, 334, 334, this.colors);
                this.imageView.setScaleType(ImageView.ScaleType.CENTER);
                this.titleTextView.setText(LocaleController.getString("AuthAnotherClient", 2131624524));
                this.buttonTextView.setText(LocaleController.getString("AuthAnotherClientScan", 2131624533));
                this.imageView.playAnimation();
                break;
            case 6:
                this.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                this.imageView.setAnimation(2131558572, 200, 200);
                this.imageView.setFocusable(false);
                this.imageView.setOnClickListener(new ActionIntroActivity$$ExternalSyntheticLambda5(this));
                this.titleTextView.setText(LocaleController.getString("Passcode", 2131627162));
                this.descriptionText.setText(LocaleController.getString("ChangePasscodeInfoShort", 2131624854));
                this.buttonTextView.setText(LocaleController.getString("EnablePasscode", 2131625620));
                this.imageView.playAnimation();
                this.flickerButton = true;
                break;
        }
        if (this.flickerButton) {
            this.buttonTextView.setPadding(AndroidUtilities.dp(34.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(34.0f), AndroidUtilities.dp(8.0f));
            this.buttonTextView.setTextSize(1, 15.0f);
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ActionIntroActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            ActionIntroActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                ActionIntroActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.ActionIntroActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends ViewGroup {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            ActionIntroActivity.this = r1;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            if (((BaseFragment) ActionIntroActivity.this).actionBar != null) {
                ((BaseFragment) ActionIntroActivity.this).actionBar.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), i2);
            }
            switch (ActionIntroActivity.this.currentType) {
                case 0:
                    if (size > size2) {
                        float f = size;
                        ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec((int) (0.45f * f), 1073741824), View.MeasureSpec.makeMeasureSpec((int) (size2 * 0.68f), 1073741824));
                        int i3 = (int) (f * 0.6f);
                        ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                        break;
                    } else {
                        ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((int) (size2 * 0.399f), 1073741824));
                        ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(86.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                        break;
                    }
                case 1:
                case 4:
                case 6:
                    if (ActionIntroActivity.this.currentType == 6) {
                        ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(140.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(140.0f), 1073741824));
                    } else {
                        ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), 1073741824));
                    }
                    if (size > size2) {
                        int i4 = (int) (size * 0.6f);
                        ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText.measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(i4, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                        break;
                    } else {
                        ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        if (ActionIntroActivity.this.currentType == 6) {
                            ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(48.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
                            break;
                        } else {
                            ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec((int) (size * 0.6f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                            break;
                        }
                    }
                case 2:
                    if (size > size2) {
                        float f2 = size;
                        int i5 = (int) (0.45f * f2);
                        ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec((int) (size2 * 0.78f), Integer.MIN_VALUE));
                        ActionIntroActivity.this.subtitleTextView.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        int i6 = (int) (f2 * 0.6f);
                        ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(i6, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText.measure(View.MeasureSpec.makeMeasureSpec(i6, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText2.measure(View.MeasureSpec.makeMeasureSpec(i6, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(i6, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                        break;
                    } else {
                        ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((int) (size2 * 0.44f), Integer.MIN_VALUE));
                        ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.subtitleTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText2.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                        break;
                    }
                case 3:
                    ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(150.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(150.0f), 1073741824));
                    if (size > size2) {
                        float f3 = size;
                        ActionIntroActivity.this.subtitleTextView.measure(View.MeasureSpec.makeMeasureSpec((int) (0.45f * f3), 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        int i7 = (int) (f3 * 0.6f);
                        ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(i7, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText.measure(View.MeasureSpec.makeMeasureSpec(i7, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(i7, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                        break;
                    } else {
                        ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionText.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.subtitleTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(48.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
                        break;
                    }
                case 5:
                    if (!ActionIntroActivity.this.showingAsBottomSheet) {
                        if (size > size2) {
                            float f4 = size;
                            ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec((int) (0.45f * f4), 1073741824), View.MeasureSpec.makeMeasureSpec((int) (size2 * 0.68f), 1073741824));
                            int i8 = (int) (f4 * 0.6f);
                            ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(i8, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                            ActionIntroActivity.this.descriptionLayout.measure(View.MeasureSpec.makeMeasureSpec(i8, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, 0));
                            ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(i8, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                            break;
                        } else {
                            ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((int) (size2 * 0.399f), 1073741824));
                            ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                            ActionIntroActivity.this.descriptionLayout.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, 0));
                            ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                            break;
                        }
                    } else {
                        ActionIntroActivity.this.imageView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec((int) (size2 * 0.32f), 1073741824));
                        ActionIntroActivity.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.descriptionLayout.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, 0));
                        ActionIntroActivity.this.buttonTextView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
                        size2 = ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + ActionIntroActivity.this.imageView.getMeasuredHeight() + ActionIntroActivity.this.titleTextView.getMeasuredHeight() + AndroidUtilities.dp(20.0f) + ActionIntroActivity.this.titleTextView.getMeasuredHeight() + ActionIntroActivity.this.descriptionLayout.getMeasuredHeight();
                        break;
                    }
            }
            setMeasuredDimension(size, size2);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            float f;
            if (((BaseFragment) ActionIntroActivity.this).actionBar != null) {
                ((BaseFragment) ActionIntroActivity.this).actionBar.layout(0, 0, i3, ((BaseFragment) ActionIntroActivity.this).actionBar.getMeasuredHeight());
            }
            int i5 = i3 - i;
            int i6 = i4 - i2;
            switch (ActionIntroActivity.this.currentType) {
                case 0:
                    if (i3 > i4) {
                        int measuredHeight = (i6 - ActionIntroActivity.this.imageView.getMeasuredHeight()) / 2;
                        ActionIntroActivity.this.imageView.layout(0, measuredHeight, ActionIntroActivity.this.imageView.getMeasuredWidth(), ActionIntroActivity.this.imageView.getMeasuredHeight() + measuredHeight);
                        float f2 = i5;
                        float f3 = f2 * 0.4f;
                        int i7 = (int) f3;
                        float f4 = i6;
                        int i8 = (int) (0.22f * f4);
                        ActionIntroActivity.this.titleTextView.layout(i7, i8, ActionIntroActivity.this.titleTextView.getMeasuredWidth() + i7, ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i8);
                        int i9 = (int) (0.39f * f4);
                        ActionIntroActivity.this.descriptionText.layout(i7, i9, ActionIntroActivity.this.descriptionText.getMeasuredWidth() + i7, ActionIntroActivity.this.descriptionText.getMeasuredHeight() + i9);
                        int measuredWidth = (int) (f3 + (((f2 * 0.6f) - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2.0f));
                        int i10 = (int) (f4 * 0.69f);
                        ActionIntroActivity.this.buttonTextView.layout(measuredWidth, i10, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i10);
                        return;
                    }
                    float f5 = i6;
                    int i11 = (int) (0.188f * f5);
                    ActionIntroActivity.this.imageView.layout(0, i11, ActionIntroActivity.this.imageView.getMeasuredWidth(), ActionIntroActivity.this.imageView.getMeasuredHeight() + i11);
                    int i12 = (int) (0.651f * f5);
                    ActionIntroActivity.this.titleTextView.layout(0, i12, ActionIntroActivity.this.titleTextView.getMeasuredWidth(), ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i12);
                    int i13 = (int) (0.731f * f5);
                    ActionIntroActivity.this.descriptionText.layout(0, i13, ActionIntroActivity.this.descriptionText.getMeasuredWidth(), ActionIntroActivity.this.descriptionText.getMeasuredHeight() + i13);
                    int measuredWidth2 = (i5 - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2;
                    int i14 = (int) (f5 * 0.853f);
                    ActionIntroActivity.this.buttonTextView.layout(measuredWidth2, i14, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth2, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i14);
                    return;
                case 1:
                case 4:
                    if (i3 > i4) {
                        int measuredHeight2 = (i6 - ActionIntroActivity.this.imageView.getMeasuredHeight()) / 2;
                        float f6 = i5;
                        int measuredWidth3 = ((int) ((0.5f * f6) - ActionIntroActivity.this.imageView.getMeasuredWidth())) / 2;
                        ActionIntroActivity.this.imageView.layout(measuredWidth3, measuredHeight2, ActionIntroActivity.this.imageView.getMeasuredWidth() + measuredWidth3, ActionIntroActivity.this.imageView.getMeasuredHeight() + measuredHeight2);
                        float f7 = f6 * 0.4f;
                        int i15 = (int) f7;
                        float f8 = i6;
                        int i16 = (int) (0.14f * f8);
                        ActionIntroActivity.this.titleTextView.layout(i15, i16, ActionIntroActivity.this.titleTextView.getMeasuredWidth() + i15, ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i16);
                        int i17 = (int) (0.31f * f8);
                        ActionIntroActivity.this.descriptionText.layout(i15, i17, ActionIntroActivity.this.descriptionText.getMeasuredWidth() + i15, ActionIntroActivity.this.descriptionText.getMeasuredHeight() + i17);
                        int measuredWidth4 = (int) (f7 + (((f6 * 0.6f) - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2.0f));
                        int i18 = (int) (f8 * 0.78f);
                        ActionIntroActivity.this.buttonTextView.layout(measuredWidth4, i18, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth4, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i18);
                        return;
                    }
                    float f9 = i6;
                    int i19 = (int) (0.214f * f9);
                    int measuredWidth5 = (i5 - ActionIntroActivity.this.imageView.getMeasuredWidth()) / 2;
                    ActionIntroActivity.this.imageView.layout(measuredWidth5, i19, ActionIntroActivity.this.imageView.getMeasuredWidth() + measuredWidth5, ActionIntroActivity.this.imageView.getMeasuredHeight() + i19);
                    int i20 = (int) (0.414f * f9);
                    ActionIntroActivity.this.titleTextView.layout(0, i20, ActionIntroActivity.this.titleTextView.getMeasuredWidth(), ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i20);
                    int i21 = (int) (0.493f * f9);
                    ActionIntroActivity.this.descriptionText.layout(0, i21, ActionIntroActivity.this.descriptionText.getMeasuredWidth(), ActionIntroActivity.this.descriptionText.getMeasuredHeight() + i21);
                    int measuredWidth6 = (i5 - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2;
                    int i22 = (int) (f9 * 0.71f);
                    ActionIntroActivity.this.buttonTextView.layout(measuredWidth6, i22, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth6, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i22);
                    return;
                case 2:
                    if (i3 > i4) {
                        float f10 = i6;
                        int measuredHeight3 = ((int) ((0.9f * f10) - ActionIntroActivity.this.imageView.getMeasuredHeight())) / 2;
                        ActionIntroActivity.this.imageView.layout(0, measuredHeight3, ActionIntroActivity.this.imageView.getMeasuredWidth(), ActionIntroActivity.this.imageView.getMeasuredHeight() + measuredHeight3);
                        int measuredHeight4 = measuredHeight3 + ActionIntroActivity.this.imageView.getMeasuredHeight() + AndroidUtilities.dp(10.0f);
                        ActionIntroActivity.this.subtitleTextView.layout(0, measuredHeight4, ActionIntroActivity.this.subtitleTextView.getMeasuredWidth(), ActionIntroActivity.this.subtitleTextView.getMeasuredHeight() + measuredHeight4);
                        float f11 = i5;
                        float f12 = f11 * 0.4f;
                        int i23 = (int) f12;
                        int i24 = (int) (0.12f * f10);
                        ActionIntroActivity.this.titleTextView.layout(i23, i24, ActionIntroActivity.this.titleTextView.getMeasuredWidth() + i23, ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i24);
                        int i25 = (int) (0.26f * f10);
                        ActionIntroActivity.this.descriptionText.layout(i23, i25, ActionIntroActivity.this.descriptionText.getMeasuredWidth() + i23, ActionIntroActivity.this.descriptionText.getMeasuredHeight() + i25);
                        int measuredWidth7 = (int) (f12 + (((f11 * 0.6f) - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2.0f));
                        int i26 = (int) (f10 * 0.6f);
                        ActionIntroActivity.this.buttonTextView.layout(measuredWidth7, i26, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth7, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i26);
                        int measuredHeight5 = (getMeasuredHeight() - ActionIntroActivity.this.descriptionText2.getMeasuredHeight()) - AndroidUtilities.dp(20.0f);
                        ActionIntroActivity.this.descriptionText2.layout(i23, measuredHeight5, ActionIntroActivity.this.descriptionText2.getMeasuredWidth() + i23, ActionIntroActivity.this.descriptionText2.getMeasuredHeight() + measuredHeight5);
                        return;
                    }
                    float f13 = i6;
                    int i27 = (int) (0.197f * f13);
                    ActionIntroActivity.this.imageView.layout(0, i27, ActionIntroActivity.this.imageView.getMeasuredWidth(), ActionIntroActivity.this.imageView.getMeasuredHeight() + i27);
                    int i28 = (int) (0.421f * f13);
                    ActionIntroActivity.this.titleTextView.layout(0, i28, ActionIntroActivity.this.titleTextView.getMeasuredWidth(), ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i28);
                    int i29 = (int) (0.477f * f13);
                    ActionIntroActivity.this.subtitleTextView.layout(0, i29, ActionIntroActivity.this.subtitleTextView.getMeasuredWidth(), ActionIntroActivity.this.subtitleTextView.getMeasuredHeight() + i29);
                    int i30 = (int) (0.537f * f13);
                    ActionIntroActivity.this.descriptionText.layout(0, i30, ActionIntroActivity.this.descriptionText.getMeasuredWidth(), ActionIntroActivity.this.descriptionText.getMeasuredHeight() + i30);
                    int measuredWidth8 = (i5 - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2;
                    int i31 = (int) (f13 * 0.71f);
                    ActionIntroActivity.this.buttonTextView.layout(measuredWidth8, i31, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth8, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i31);
                    int measuredHeight6 = (getMeasuredHeight() - ActionIntroActivity.this.descriptionText2.getMeasuredHeight()) - AndroidUtilities.dp(20.0f);
                    ActionIntroActivity.this.descriptionText2.layout(0, measuredHeight6, ActionIntroActivity.this.descriptionText2.getMeasuredWidth(), ActionIntroActivity.this.descriptionText2.getMeasuredHeight() + measuredHeight6);
                    return;
                case 3:
                    if (i3 > i4) {
                        float f14 = i6;
                        int measuredHeight7 = ((int) ((0.95f * f14) - ActionIntroActivity.this.imageView.getMeasuredHeight())) / 2;
                        int width = (int) ((getWidth() * 0.35f) - ActionIntroActivity.this.imageView.getMeasuredWidth());
                        ActionIntroActivity.this.imageView.layout(width, measuredHeight7, ActionIntroActivity.this.imageView.getMeasuredWidth() + width, ActionIntroActivity.this.imageView.getMeasuredHeight() + measuredHeight7);
                        float f15 = i5;
                        float f16 = f15 * 0.4f;
                        int i32 = (int) f16;
                        int i33 = (int) (0.12f * f14);
                        ActionIntroActivity.this.titleTextView.layout(i32, i33, ActionIntroActivity.this.titleTextView.getMeasuredWidth() + i32, ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i33);
                        int i34 = (int) (0.24f * f14);
                        ActionIntroActivity.this.descriptionText.layout(i32, i34, ActionIntroActivity.this.descriptionText.getMeasuredWidth() + i32, ActionIntroActivity.this.descriptionText.getMeasuredHeight() + i34);
                        float f17 = f15 * 0.6f;
                        int measuredWidth9 = (int) (((f17 - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2.0f) + f16);
                        int i35 = (int) (f14 * 0.8f);
                        ActionIntroActivity.this.buttonTextView.layout(measuredWidth9, i35, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth9, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i35);
                        int measuredWidth10 = (int) (f16 + ((f17 - ActionIntroActivity.this.subtitleTextView.getMeasuredWidth()) / 2.0f));
                        int measuredHeight8 = i35 - (ActionIntroActivity.this.subtitleTextView.getMeasuredHeight() + AndroidUtilities.dp(16.0f));
                        ActionIntroActivity.this.subtitleTextView.layout(measuredWidth10, measuredHeight8, ActionIntroActivity.this.subtitleTextView.getMeasuredWidth() + measuredWidth10, ActionIntroActivity.this.subtitleTextView.getMeasuredHeight() + measuredHeight8);
                        return;
                    }
                    int i36 = (int) (i6 * 0.3f);
                    int measuredWidth11 = (i5 - ActionIntroActivity.this.imageView.getMeasuredWidth()) / 2;
                    ActionIntroActivity.this.imageView.layout(measuredWidth11, i36, ActionIntroActivity.this.imageView.getMeasuredWidth() + measuredWidth11, ActionIntroActivity.this.imageView.getMeasuredHeight() + i36);
                    int measuredHeight9 = i36 + ActionIntroActivity.this.imageView.getMeasuredHeight() + AndroidUtilities.dp(24.0f);
                    ActionIntroActivity.this.titleTextView.layout(0, measuredHeight9, ActionIntroActivity.this.titleTextView.getMeasuredWidth(), ActionIntroActivity.this.titleTextView.getMeasuredHeight() + measuredHeight9);
                    int textSize = (int) (measuredHeight9 + ActionIntroActivity.this.titleTextView.getTextSize() + AndroidUtilities.dp(16.0f));
                    ActionIntroActivity.this.descriptionText.layout(0, textSize, ActionIntroActivity.this.descriptionText.getMeasuredWidth(), ActionIntroActivity.this.descriptionText.getMeasuredHeight() + textSize);
                    int measuredWidth12 = (i5 - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2;
                    int measuredHeight10 = (i6 - ActionIntroActivity.this.buttonTextView.getMeasuredHeight()) - AndroidUtilities.dp(48.0f);
                    ActionIntroActivity.this.buttonTextView.layout(measuredWidth12, measuredHeight10, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth12, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + measuredHeight10);
                    int measuredWidth13 = (i5 - ActionIntroActivity.this.subtitleTextView.getMeasuredWidth()) / 2;
                    int measuredHeight11 = measuredHeight10 - (ActionIntroActivity.this.subtitleTextView.getMeasuredHeight() + AndroidUtilities.dp(32.0f));
                    ActionIntroActivity.this.subtitleTextView.layout(measuredWidth13, measuredHeight11, ActionIntroActivity.this.subtitleTextView.getMeasuredWidth() + measuredWidth13, ActionIntroActivity.this.subtitleTextView.getMeasuredHeight() + measuredHeight11);
                    return;
                case 5:
                    if (ActionIntroActivity.this.showingAsBottomSheet) {
                        ActionIntroActivity.this.imageView.layout(0, 0, ActionIntroActivity.this.imageView.getMeasuredWidth(), ActionIntroActivity.this.imageView.getMeasuredHeight() + 0);
                        float f18 = i6;
                        int i37 = (int) (0.403f * f18);
                        ActionIntroActivity.this.titleTextView.layout(0, i37, ActionIntroActivity.this.titleTextView.getMeasuredWidth(), ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i37);
                        int i38 = (int) (0.631f * f18);
                        int measuredWidth14 = (getMeasuredWidth() - ActionIntroActivity.this.descriptionLayout.getMeasuredWidth()) / 2;
                        ActionIntroActivity.this.descriptionLayout.layout(measuredWidth14, i38, ActionIntroActivity.this.descriptionLayout.getMeasuredWidth() + measuredWidth14, ActionIntroActivity.this.descriptionLayout.getMeasuredHeight() + i38);
                        int measuredWidth15 = (i5 - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2;
                        int i39 = (int) (f18 * 0.853f);
                        ActionIntroActivity.this.buttonTextView.layout(measuredWidth15, i39, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth15, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i39);
                        return;
                    } else if (i3 > i4) {
                        int measuredHeight12 = (i6 - ActionIntroActivity.this.imageView.getMeasuredHeight()) / 2;
                        ActionIntroActivity.this.imageView.layout(0, measuredHeight12, ActionIntroActivity.this.imageView.getMeasuredWidth(), ActionIntroActivity.this.imageView.getMeasuredHeight() + measuredHeight12);
                        float f19 = i5;
                        float f20 = f19 * 0.4f;
                        int i40 = (int) f20;
                        float f21 = i6;
                        int i41 = (int) (0.08f * f21);
                        ActionIntroActivity.this.titleTextView.layout(i40, i41, ActionIntroActivity.this.titleTextView.getMeasuredWidth() + i40, ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i41);
                        float f22 = f19 * 0.6f;
                        int measuredWidth16 = (int) (((f22 - ActionIntroActivity.this.descriptionLayout.getMeasuredWidth()) / 2.0f) + f20);
                        int i42 = (int) (0.25f * f21);
                        ActionIntroActivity.this.descriptionLayout.layout(measuredWidth16, i42, ActionIntroActivity.this.descriptionLayout.getMeasuredWidth() + measuredWidth16, ActionIntroActivity.this.descriptionLayout.getMeasuredHeight() + i42);
                        int measuredWidth17 = (int) (f20 + ((f22 - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2.0f));
                        int i43 = (int) (f21 * 0.78f);
                        ActionIntroActivity.this.buttonTextView.layout(measuredWidth17, i43, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth17, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i43);
                        return;
                    } else {
                        if (AndroidUtilities.displaySize.y < 1800) {
                            float f23 = i6;
                            int i44 = (int) (0.06f * f23);
                            ActionIntroActivity.this.imageView.layout(0, i44, ActionIntroActivity.this.imageView.getMeasuredWidth(), ActionIntroActivity.this.imageView.getMeasuredHeight() + i44);
                            int i45 = (int) (0.463f * f23);
                            ActionIntroActivity.this.titleTextView.layout(0, i45, ActionIntroActivity.this.titleTextView.getMeasuredWidth(), ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i45);
                            f = f23 * 0.543f;
                        } else {
                            float f24 = i6;
                            int i46 = (int) (0.148f * f24);
                            ActionIntroActivity.this.imageView.layout(0, i46, ActionIntroActivity.this.imageView.getMeasuredWidth(), ActionIntroActivity.this.imageView.getMeasuredHeight() + i46);
                            int i47 = (int) (0.551f * f24);
                            ActionIntroActivity.this.titleTextView.layout(0, i47, ActionIntroActivity.this.titleTextView.getMeasuredWidth(), ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i47);
                            f = f24 * 0.631f;
                        }
                        int i48 = (int) f;
                        int measuredWidth18 = (getMeasuredWidth() - ActionIntroActivity.this.descriptionLayout.getMeasuredWidth()) / 2;
                        ActionIntroActivity.this.descriptionLayout.layout(measuredWidth18, i48, ActionIntroActivity.this.descriptionLayout.getMeasuredWidth() + measuredWidth18, ActionIntroActivity.this.descriptionLayout.getMeasuredHeight() + i48);
                        int measuredWidth19 = (i5 - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2;
                        int i49 = (int) (i6 * 0.853f);
                        ActionIntroActivity.this.buttonTextView.layout(measuredWidth19, i49, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth19, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i49);
                        return;
                    }
                case 6:
                    if (i3 > i4) {
                        int measuredHeight13 = (i6 - ActionIntroActivity.this.imageView.getMeasuredHeight()) / 2;
                        float f25 = i5;
                        int measuredWidth20 = ((int) ((0.5f * f25) - ActionIntroActivity.this.imageView.getMeasuredWidth())) / 2;
                        ActionIntroActivity.this.imageView.layout(measuredWidth20, measuredHeight13, ActionIntroActivity.this.imageView.getMeasuredWidth() + measuredWidth20, ActionIntroActivity.this.imageView.getMeasuredHeight() + measuredHeight13);
                        float f26 = f25 * 0.4f;
                        int i50 = (int) f26;
                        float f27 = i6;
                        int i51 = (int) (0.14f * f27);
                        ActionIntroActivity.this.titleTextView.layout(i50, i51, ActionIntroActivity.this.titleTextView.getMeasuredWidth() + i50, ActionIntroActivity.this.titleTextView.getMeasuredHeight() + i51);
                        int i52 = (int) (0.31f * f27);
                        ActionIntroActivity.this.descriptionText.layout(i50, i52, ActionIntroActivity.this.descriptionText.getMeasuredWidth() + i50, ActionIntroActivity.this.descriptionText.getMeasuredHeight() + i52);
                        int measuredWidth21 = (int) (f26 + (((f25 * 0.6f) - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2.0f));
                        int i53 = (int) (f27 * 0.78f);
                        ActionIntroActivity.this.buttonTextView.layout(measuredWidth21, i53, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth21, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + i53);
                        return;
                    }
                    int i54 = (int) (i6 * 0.3f);
                    int measuredWidth22 = (i5 - ActionIntroActivity.this.imageView.getMeasuredWidth()) / 2;
                    ActionIntroActivity.this.imageView.layout(measuredWidth22, i54, ActionIntroActivity.this.imageView.getMeasuredWidth() + measuredWidth22, ActionIntroActivity.this.imageView.getMeasuredHeight() + i54);
                    int measuredHeight14 = i54 + ActionIntroActivity.this.imageView.getMeasuredHeight() + AndroidUtilities.dp(24.0f);
                    ActionIntroActivity.this.titleTextView.layout(0, measuredHeight14, ActionIntroActivity.this.titleTextView.getMeasuredWidth(), ActionIntroActivity.this.titleTextView.getMeasuredHeight() + measuredHeight14);
                    int textSize2 = (int) (measuredHeight14 + ActionIntroActivity.this.titleTextView.getTextSize() + AndroidUtilities.dp(16.0f));
                    ActionIntroActivity.this.descriptionText.layout(0, textSize2, ActionIntroActivity.this.descriptionText.getMeasuredWidth(), ActionIntroActivity.this.descriptionText.getMeasuredHeight() + textSize2);
                    int measuredWidth23 = (i5 - ActionIntroActivity.this.buttonTextView.getMeasuredWidth()) / 2;
                    int measuredHeight15 = (i6 - ActionIntroActivity.this.buttonTextView.getMeasuredHeight()) - AndroidUtilities.dp(48.0f);
                    ActionIntroActivity.this.buttonTextView.layout(measuredWidth23, measuredHeight15, ActionIntroActivity.this.buttonTextView.getMeasuredWidth() + measuredWidth23, ActionIntroActivity.this.buttonTextView.getMeasuredHeight() + measuredHeight15);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: org.telegram.ui.ActionIntroActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends TextView {
        CellFlickerDrawable cellFlickerDrawable;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            ActionIntroActivity.this = r1;
        }

        @Override // android.widget.TextView, android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (ActionIntroActivity.this.flickerButton) {
                if (this.cellFlickerDrawable == null) {
                    CellFlickerDrawable cellFlickerDrawable = new CellFlickerDrawable();
                    this.cellFlickerDrawable = cellFlickerDrawable;
                    cellFlickerDrawable.drawFrame = false;
                    cellFlickerDrawable.repeatProgress = 2.0f;
                }
                this.cellFlickerDrawable.setParentWidth(getMeasuredWidth());
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                this.cellFlickerDrawable.draw(canvas, rectF, AndroidUtilities.dp(4.0f), null);
                invalidate();
            }
        }
    }

    public /* synthetic */ void lambda$createView$2(View view) {
        if (getParentActivity() == null) {
            return;
        }
        switch (this.currentType) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putInt("step", 0);
                presentFragment(new ChannelCreateActivity(bundle), true);
                return;
            case 1:
                getParentActivity().requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
                return;
            case 2:
                if (this.currentGroupCreateAddress == null || this.currentGroupCreateLocation == null) {
                    return;
                }
                Bundle bundle2 = new Bundle();
                bundle2.putLongArray("result", new long[]{getUserConfig().getClientUserId()});
                bundle2.putInt("chatType", 4);
                bundle2.putString("address", this.currentGroupCreateAddress);
                bundle2.putParcelable("location", this.currentGroupCreateLocation);
                presentFragment(new GroupCreateFinalActivity(bundle2), true);
                return;
            case 3:
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("PhoneNumberChangeTitle", 2131627496));
                builder.setMessage(LocaleController.getString("PhoneNumberAlert", 2131627494));
                builder.setPositiveButton(LocaleController.getString("Change", 2131624847), new ActionIntroActivity$$ExternalSyntheticLambda1(this));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
                showDialog(builder.create());
                return;
            case 4:
                try {
                    getParentActivity().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            case 5:
                if (getParentActivity() == null) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= 23 && getParentActivity().checkSelfPermission("android.permission.CAMERA") != 0) {
                    getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 34);
                    return;
                } else {
                    processOpenQrReader();
                    return;
                }
            case 6:
                presentFragment(new PasscodeActivity(1), true);
                return;
            default:
                return;
        }
    }

    public /* synthetic */ void lambda$createView$1(DialogInterface dialogInterface, int i) {
        presentFragment(new LoginActivity().changePhoneNumber(), true);
    }

    public /* synthetic */ void lambda$createView$3(View view) {
        if (!this.imageView.getAnimatedDrawable().isRunning()) {
            this.imageView.getAnimatedDrawable().setCurrentFrame(0, false);
            this.imageView.playAnimation();
        }
    }

    public /* synthetic */ void lambda$createView$4(View view) {
        if (!this.imageView.getAnimatedDrawable().isRunning()) {
            this.imageView.getAnimatedDrawable().setCurrentFrame(0, false);
            this.imageView.playAnimation();
        }
    }

    public /* synthetic */ void lambda$createView$5(View view) {
        getParentLayout().closeLastFragment(true);
    }

    @Override // org.telegram.messenger.LocationController.LocationFetchCallback
    public void onLocationAddressAvailable(String str, String str2, Location location) {
        TextView textView = this.subtitleTextView;
        if (textView == null) {
            return;
        }
        textView.setText(str);
        this.currentGroupCreateAddress = str;
        this.currentGroupCreateDisplayAddress = str2;
        this.currentGroupCreateLocation = location;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        boolean z;
        super.onResume();
        if (this.currentType == 4) {
            int i = Build.VERSION.SDK_INT;
            if (i >= 28) {
                z = ((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isLocationEnabled();
            } else {
                if (i >= 19) {
                    try {
                        if (Settings.Secure.getInt(ApplicationLoader.applicationContext.getContentResolver(), "location_mode", 0) == 0) {
                            z = false;
                        }
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
                z = true;
            }
            if (!z) {
                return;
            }
            presentFragment(new PeopleNearbyActivity(), true);
        }
    }

    public void updateColors() {
        int[] iArr = this.colors;
        if (iArr == null || this.imageView == null) {
            return;
        }
        iArr[0] = 3355443;
        iArr[1] = Theme.getColor("windowBackgroundWhiteBlackText");
        int[] iArr2 = this.colors;
        iArr2[2] = 16777215;
        iArr2[3] = Theme.getColor("windowBackgroundWhite");
        int[] iArr3 = this.colors;
        iArr3[4] = 5285866;
        iArr3[5] = Theme.getColor("featuredStickers_addButton");
        int[] iArr4 = this.colors;
        iArr4[6] = 2170912;
        iArr4[7] = Theme.getColor("windowBackgroundWhite");
        this.imageView.replaceColors(this.colors);
    }

    public void setGroupCreateAddress(String str, String str2, Location location) {
        this.currentGroupCreateAddress = str;
        this.currentGroupCreateDisplayAddress = str2;
        this.currentGroupCreateLocation = location;
        if (location == null || str != null) {
            return;
        }
        LocationController.fetchLocationAddress(location, this);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        if (getParentActivity() == null) {
            return;
        }
        if (i == 2) {
            if (iArr == null || iArr.length == 0) {
                return;
            }
            if (iArr[0] != 0) {
                showDialog(AlertsCreator.createLocationRequiredDialog(getParentActivity(), false));
            } else {
                AndroidUtilities.runOnUIThread(new ActionIntroActivity$$ExternalSyntheticLambda7(this));
            }
        } else if (i != 34) {
        } else {
            if (iArr.length > 0 && iArr[0] == 0) {
                processOpenQrReader();
            } else {
                new AlertDialog.Builder(getParentActivity()).setMessage(AndroidUtilities.replaceTags(LocaleController.getString("QRCodePermissionNoCameraWithHint", 2131627813))).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131627483), new ActionIntroActivity$$ExternalSyntheticLambda0(this)).setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131625245), null).setTopAnimation(2131558493, 72, false, Theme.getColor("dialogTopBackground")).show();
            }
        }
    }

    public /* synthetic */ void lambda$onRequestPermissionsResultFragment$6() {
        presentFragment(new PeopleNearbyActivity(), true);
    }

    public /* synthetic */ void lambda$onRequestPermissionsResultFragment$7(DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            getParentActivity().startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void setQrLoginDelegate(ActionIntroQRLoginDelegate actionIntroQRLoginDelegate) {
        this.qrLoginDelegate = actionIntroQRLoginDelegate;
    }

    /* renamed from: org.telegram.ui.ActionIntroActivity$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 implements CameraScanActivity.CameraScanActivityDelegate {
        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public /* synthetic */ void didFindMrzInfo(MrzRecognizer.Result result) {
            CameraScanActivity.CameraScanActivityDelegate.CC.$default$didFindMrzInfo(this, result);
        }

        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public /* synthetic */ boolean processQr(String str, Runnable runnable) {
            return CameraScanActivity.CameraScanActivityDelegate.CC.$default$processQr(this, str, runnable);
        }

        AnonymousClass4() {
            ActionIntroActivity.this = r1;
        }

        @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
        public void didFindQr(String str) {
            ActionIntroActivity.this.finishFragment(false);
            ActionIntroActivity.this.qrLoginDelegate.didFindQRCode(str);
        }
    }

    private void processOpenQrReader() {
        CameraScanActivity.showAsSheet(this, false, 1, new AnonymousClass4());
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        ActionIntroActivity$$ExternalSyntheticLambda8 actionIntroActivity$$ExternalSyntheticLambda8 = new ActionIntroActivity$$ExternalSyntheticLambda8(this);
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, actionIntroActivity$$ExternalSyntheticLambda8, "windowBackgroundWhite"));
        if (this.actionBar != null) {
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarWhiteSelector"));
        }
        arrayList.add(new ThemeDescription(this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, actionIntroActivity$$ExternalSyntheticLambda8, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.subtitleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.descriptionText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        arrayList.add(new ThemeDescription(this.buttonTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "featuredStickers_buttonText"));
        arrayList.add(new ThemeDescription(this.buttonTextView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, null, null, null, actionIntroActivity$$ExternalSyntheticLambda8, "featuredStickers_addButton"));
        arrayList.add(new ThemeDescription(this.buttonTextView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "featuredStickers_addButtonPressed"));
        arrayList.add(new ThemeDescription(this.desctiptionLines[0], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.desctiptionLines[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.desctiptionLines[1], ThemeDescription.FLAG_LINKCOLOR, null, null, null, null, "windowBackgroundWhiteLinkText"));
        arrayList.add(new ThemeDescription(this.desctiptionLines[2], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.desctiptionLines[3], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.desctiptionLines[4], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.desctiptionLines[5], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, new Drawable[]{this.drawable1}, null, "changephoneinfo_image"));
        arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, new Drawable[]{this.drawable2}, null, "changephoneinfo_image2"));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(Theme.getColor("windowBackgroundWhite", null, true)) > 0.699999988079071d;
    }
}
