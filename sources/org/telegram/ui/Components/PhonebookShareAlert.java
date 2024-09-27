package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.widget.NestedScrollView;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public class PhonebookShareAlert extends BottomSheet {
    private ActionBar actionBar;
    private AnimatorSet actionBarAnimation;
    private View actionBarShadow;
    private Paint backgroundPaint;
    private TextView buttonTextView;
    private TLRPC.User currentUser;
    private ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate delegate;
    private boolean inLayout;
    private boolean isImport;
    private LinearLayout linearLayout;
    private ListAdapter listAdapter;
    private ArrayList other;
    private BaseFragment parentFragment;
    private int phoneEndRow;
    private int phoneStartRow;
    private ArrayList phones;
    private int rowCount;
    private int scrollOffsetY;
    private NestedScrollView scrollView;
    private View shadow;
    private AnimatorSet shadowAnimation;
    private int userRow;
    private int vcardEndRow;
    private int vcardStartRow;

    /* loaded from: classes3.dex */
    private class ListAdapter {
        private ListAdapter() {
        }

        public View createView(Context context, int i) {
            int itemViewType = getItemViewType(i);
            View textCheckBoxCell = itemViewType != 0 ? new TextCheckBoxCell(context) : new UserCell(context);
            onBindViewHolder(textCheckBoxCell, i, itemViewType);
            return textCheckBoxCell;
        }

        public int getItemCount() {
            return PhonebookShareAlert.this.rowCount;
        }

        public int getItemViewType(int i) {
            return i == PhonebookShareAlert.this.userRow ? 0 : 1;
        }

        public void onBindViewHolder(View view, int i, int i2) {
            AndroidUtilities.VcardItem vcardItem;
            int i3;
            if (i2 == 1) {
                TextCheckBoxCell textCheckBoxCell = (TextCheckBoxCell) view;
                if (i < PhonebookShareAlert.this.phoneStartRow || i >= PhonebookShareAlert.this.phoneEndRow) {
                    vcardItem = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.other.get(i - PhonebookShareAlert.this.vcardStartRow);
                    int i4 = vcardItem.type;
                    if (i4 == 1) {
                        i3 = R.drawable.msg_mention;
                    } else if (i4 == 2) {
                        i3 = R.drawable.msg_location;
                    } else if (i4 == 3) {
                        i3 = R.drawable.msg_link;
                    } else {
                        if (i4 != 4) {
                            if (i4 == 5) {
                                i3 = R.drawable.msg_calendar2;
                            } else if (i4 == 6) {
                                i3 = "ORG".equalsIgnoreCase(vcardItem.getRawType(true)) ? R.drawable.msg_work : R.drawable.msg_jobtitle;
                            }
                        }
                        i3 = R.drawable.msg_info;
                    }
                } else {
                    vcardItem = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.phones.get(i - PhonebookShareAlert.this.phoneStartRow);
                    i3 = R.drawable.msg_calls;
                }
                textCheckBoxCell.setVCardItem(vcardItem, i3, i != getItemCount() - 1);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class TextCheckBoxCell extends FrameLayout {
        private Switch checkBox;
        private ImageView imageView;
        private boolean needDivider;
        private TextView textView;
        private TextView valueTextView;

        public TextCheckBoxCell(Context context) {
            super(context);
            float f;
            float f2;
            float f3;
            float f4;
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setTextColor(PhonebookShareAlert.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setSingleLine(false);
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            TextView textView2 = this.textView;
            boolean z = LocaleController.isRTL;
            int i = (z ? 5 : 3) | 48;
            if (z) {
                f = PhonebookShareAlert.this.isImport ? 17 : 64;
            } else {
                f = 72.0f;
            }
            if (LocaleController.isRTL) {
                f2 = 72.0f;
            } else {
                f2 = PhonebookShareAlert.this.isImport ? 17 : 64;
            }
            addView(textView2, LayoutHelper.createFrame(-1, -1.0f, i, f, 10.0f, f2, 0.0f));
            TextView textView3 = new TextView(context);
            this.valueTextView = textView3;
            textView3.setTextColor(PhonebookShareAlert.this.getThemedColor(Theme.key_windowBackgroundWhiteGrayText2));
            this.valueTextView.setTextSize(1, 13.0f);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            TextView textView4 = this.valueTextView;
            boolean z2 = LocaleController.isRTL;
            int i2 = z2 ? 5 : 3;
            if (z2) {
                f3 = PhonebookShareAlert.this.isImport ? 17 : 64;
            } else {
                f3 = 72.0f;
            }
            if (LocaleController.isRTL) {
                f4 = 72.0f;
            } else {
                f4 = PhonebookShareAlert.this.isImport ? 17 : 64;
            }
            addView(textView4, LayoutHelper.createFrame(-2, -2.0f, i2, f3, 35.0f, f4, 0.0f));
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.setColorFilter(new PorterDuffColorFilter(PhonebookShareAlert.this.getThemedColor(Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.MULTIPLY));
            ImageView imageView2 = this.imageView;
            boolean z3 = LocaleController.isRTL;
            addView(imageView2, LayoutHelper.createFrame(-2, -2.0f, (z3 ? 5 : 3) | 48, z3 ? 0.0f : 20.0f, 20.0f, z3 ? 20.0f : 0.0f, 0.0f));
            if (PhonebookShareAlert.this.isImport) {
                return;
            }
            Switch r1 = new Switch(context);
            this.checkBox = r1;
            int i3 = Theme.key_switchTrack;
            int i4 = Theme.key_switchTrackChecked;
            int i5 = Theme.key_windowBackgroundWhite;
            r1.setColors(i3, i4, i5, i5);
            addView(this.checkBox, LayoutHelper.createFrame(37, 40.0f, (LocaleController.isRTL ? 3 : 5) | 16, 22.0f, 0.0f, 22.0f, 0.0f));
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            Switch r0 = this.checkBox;
            if (r0 != null) {
                r0.invalidate();
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.needDivider) {
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(70.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(70.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int measuredHeight = this.textView.getMeasuredHeight() + AndroidUtilities.dp(13.0f);
            TextView textView = this.valueTextView;
            textView.layout(textView.getLeft(), measuredHeight, this.valueTextView.getRight(), this.valueTextView.getMeasuredHeight() + measuredHeight);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            measureChildWithMargins(this.textView, i, 0, i2, 0);
            measureChildWithMargins(this.valueTextView, i, 0, i2, 0);
            measureChildWithMargins(this.imageView, i, 0, i2, 0);
            Switch r7 = this.checkBox;
            if (r7 != null) {
                measureChildWithMargins(r7, i, 0, i2, 0);
            }
            setMeasuredDimension(View.MeasureSpec.getSize(i), Math.max(AndroidUtilities.dp(64.0f), this.textView.getMeasuredHeight() + this.valueTextView.getMeasuredHeight() + AndroidUtilities.dp(20.0f)) + (this.needDivider ? 1 : 0));
        }

        public void setChecked(boolean z) {
            Switch r0 = this.checkBox;
            if (r0 != null) {
                r0.setChecked(z, true);
            }
        }

        public void setVCardItem(AndroidUtilities.VcardItem vcardItem, int i, boolean z) {
            this.textView.setText(vcardItem.getValue(true));
            this.valueTextView.setText(vcardItem.getType());
            Switch r0 = this.checkBox;
            if (r0 != null) {
                r0.setChecked(vcardItem.checked, false);
            }
            ImageView imageView = this.imageView;
            if (i != 0) {
                imageView.setImageResource(i);
            } else {
                imageView.setImageDrawable(null);
            }
            this.needDivider = z;
            setWillNotDraw(!z);
        }
    }

    /* loaded from: classes3.dex */
    public class UserCell extends LinearLayout {
        public UserCell(Context context) {
            super(context);
            String formatUserStatus;
            boolean z;
            setOrientation(1);
            if (PhonebookShareAlert.this.phones.size() == 1 && PhonebookShareAlert.this.other.size() == 0) {
                formatUserStatus = ((AndroidUtilities.VcardItem) PhonebookShareAlert.this.phones.get(0)).getValue(true);
                z = false;
            } else {
                formatUserStatus = (PhonebookShareAlert.this.currentUser.status == null || PhonebookShareAlert.this.currentUser.status.expires == 0) ? null : LocaleController.formatUserStatus(((BottomSheet) PhonebookShareAlert.this).currentAccount, PhonebookShareAlert.this.currentUser);
                z = true;
            }
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setTextSize(AndroidUtilities.dp(30.0f));
            avatarDrawable.setInfo(((BottomSheet) PhonebookShareAlert.this).currentAccount, PhonebookShareAlert.this.currentUser);
            BackupImageView backupImageView = new BackupImageView(context);
            backupImageView.setRoundRadius(AndroidUtilities.dp(40.0f));
            backupImageView.setForUserOrChat(PhonebookShareAlert.this.currentUser, avatarDrawable);
            addView(backupImageView, LayoutHelper.createLinear(80, 80, 49, 0, 32, 0, 0));
            TextView textView = new TextView(context);
            textView.setTypeface(AndroidUtilities.bold());
            textView.setTextSize(1, 17.0f);
            textView.setTextColor(PhonebookShareAlert.this.getThemedColor(Theme.key_dialogTextBlack));
            textView.setSingleLine(true);
            TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
            textView.setEllipsize(truncateAt);
            textView.setText(ContactsController.formatName(PhonebookShareAlert.this.currentUser.first_name, PhonebookShareAlert.this.currentUser.last_name));
            addView(textView, LayoutHelper.createLinear(-2, -2, 49, 10, 10, 10, formatUserStatus != null ? 0 : 27));
            if (formatUserStatus != null) {
                TextView textView2 = new TextView(context);
                textView2.setTextSize(1, 14.0f);
                textView2.setTextColor(PhonebookShareAlert.this.getThemedColor(Theme.key_dialogTextGray3));
                textView2.setSingleLine(true);
                textView2.setEllipsize(truncateAt);
                textView2.setText(formatUserStatus);
                addView(textView2, LayoutHelper.createLinear(-2, -2, 49, 10, 3, 10, z ? 27 : 11));
            }
        }
    }

    public PhonebookShareAlert(BaseFragment baseFragment, ContactsController.Contact contact, TLRPC.User user, Uri uri, File file, String str, String str2) {
        this(baseFragment, contact, user, uri, file, (String) null, str, str2);
    }

    public PhonebookShareAlert(BaseFragment baseFragment, ContactsController.Contact contact, TLRPC.User user, Uri uri, File file, String str, String str2, String str3) {
        this(baseFragment, contact, user, uri, file, str, str2, str3, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x014a  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x02f7  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0303  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public PhonebookShareAlert(BaseFragment baseFragment, ContactsController.Contact contact, TLRPC.User user, Uri uri, File file, String str, String str2, String str3, final Theme.ResourcesProvider resourcesProvider) {
        super(baseFragment.getParentActivity(), false, resourcesProvider);
        ArrayList<TLRPC.User> loadVCardFromStream;
        String str4;
        String str5;
        ArrayList<TLRPC.RestrictionReason> arrayList;
        int itemCount;
        final int i;
        ActionBar actionBar;
        int i2;
        TextView textView;
        int i3;
        ArrayList arrayList2;
        this.backgroundPaint = new Paint(1);
        this.other = new ArrayList();
        this.phones = new ArrayList();
        String formatName = ContactsController.formatName(str2, str3);
        ArrayList arrayList3 = new ArrayList();
        if (uri != null) {
            loadVCardFromStream = AndroidUtilities.loadVCardFromStream(uri, this.currentAccount, false, arrayList3, formatName);
        } else if (file != null) {
            loadVCardFromStream = AndroidUtilities.loadVCardFromStream(Uri.fromFile(file), this.currentAccount, false, arrayList3, formatName);
            file.delete();
            this.isImport = true;
        } else {
            if (str != null) {
                AndroidUtilities.VcardItem vcardItem = new AndroidUtilities.VcardItem();
                vcardItem.type = 0;
                ArrayList<String> arrayList4 = vcardItem.vcardData;
                String str6 = "TEL;MOBILE:+" + str;
                vcardItem.fullData = str6;
                arrayList4.add(str6);
                this.phones.add(vcardItem);
                this.isImport = true;
            } else {
                String str7 = contact.key;
                if (str7 != null) {
                    loadVCardFromStream = AndroidUtilities.loadVCardFromStream(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, str7), this.currentAccount, true, arrayList3, formatName);
                } else {
                    AndroidUtilities.VcardItem vcardItem2 = new AndroidUtilities.VcardItem();
                    vcardItem2.type = 0;
                    ArrayList<String> arrayList5 = vcardItem2.vcardData;
                    String str8 = "TEL;MOBILE:+" + contact.user.phone;
                    vcardItem2.fullData = str8;
                    arrayList5.add(str8);
                    this.phones.add(vcardItem2);
                }
            }
            loadVCardFromStream = null;
        }
        TLRPC.User user2 = (user != null || contact == null) ? user : contact.user;
        if (loadVCardFromStream != null) {
            for (int i4 = 0; i4 < arrayList3.size(); i4++) {
                AndroidUtilities.VcardItem vcardItem3 = (AndroidUtilities.VcardItem) arrayList3.get(i4);
                if (vcardItem3.type == 0) {
                    for (int i5 = 0; i5 < this.phones.size(); i5++) {
                        if (((AndroidUtilities.VcardItem) this.phones.get(i5)).getValue(false).equals(vcardItem3.getValue(false))) {
                            vcardItem3.checked = false;
                            break;
                        }
                    }
                    arrayList2 = this.phones;
                } else {
                    arrayList2 = this.other;
                }
                arrayList2.add(vcardItem3);
            }
            if (!loadVCardFromStream.isEmpty()) {
                TLRPC.User user3 = loadVCardFromStream.get(0);
                arrayList = user3.restriction_reason;
                if (TextUtils.isEmpty(str2)) {
                    str4 = user3.first_name;
                    str5 = user3.last_name;
                } else {
                    str4 = str2;
                    str5 = str3;
                }
                TLRPC.TL_userContact_old2 tL_userContact_old2 = new TLRPC.TL_userContact_old2();
                this.currentUser = tL_userContact_old2;
                if (user2 == null) {
                    tL_userContact_old2.id = user2.id;
                    tL_userContact_old2.access_hash = user2.access_hash;
                    tL_userContact_old2.photo = user2.photo;
                    tL_userContact_old2.status = user2.status;
                    tL_userContact_old2.first_name = user2.first_name;
                    tL_userContact_old2.last_name = user2.last_name;
                    tL_userContact_old2.phone = user2.phone;
                    if (arrayList != null) {
                        tL_userContact_old2.restriction_reason = arrayList;
                    }
                } else {
                    tL_userContact_old2.first_name = str4;
                    tL_userContact_old2.last_name = str5;
                }
                this.parentFragment = baseFragment;
                final Activity parentActivity = baseFragment.getParentActivity();
                updateRows();
                FrameLayout frameLayout = new FrameLayout(parentActivity) { // from class: org.telegram.ui.Components.PhonebookShareAlert.1
                    private boolean ignoreLayout;
                    private RectF rect = new RectF();

                    @Override // android.view.View
                    protected void onDraw(Canvas canvas) {
                        int i6 = PhonebookShareAlert.this.scrollOffsetY - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop;
                        int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(30.0f) + ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop;
                        float dp = AndroidUtilities.dp(12.0f);
                        float min = ((float) (((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop + i6)) < dp ? 1.0f - Math.min(1.0f, ((dp - i6) - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop) / dp) : 1.0f;
                        if (Build.VERSION.SDK_INT >= 21) {
                            int i7 = AndroidUtilities.statusBarHeight;
                            i6 += i7;
                            measuredHeight -= i7;
                        }
                        ((BottomSheet) PhonebookShareAlert.this).shadowDrawable.setBounds(0, i6, getMeasuredWidth(), measuredHeight);
                        ((BottomSheet) PhonebookShareAlert.this).shadowDrawable.draw(canvas);
                        if (min != 1.0f) {
                            PhonebookShareAlert.this.backgroundPaint.setColor(PhonebookShareAlert.this.getThemedColor(Theme.key_dialogBackground));
                            this.rect.set(((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop + i6, getMeasuredWidth() - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop + i6 + AndroidUtilities.dp(24.0f));
                            float f = dp * min;
                            canvas.drawRoundRect(this.rect, f, f, PhonebookShareAlert.this.backgroundPaint);
                        }
                        int themedColor = PhonebookShareAlert.this.getThemedColor(Theme.key_dialogBackground);
                        PhonebookShareAlert.this.backgroundPaint.setColor(Color.argb((int) (PhonebookShareAlert.this.actionBar.getAlpha() * 255.0f), (int) (Color.red(themedColor) * 0.8f), (int) (Color.green(themedColor) * 0.8f), (int) (Color.blue(themedColor) * 0.8f)));
                        canvas.drawRect(((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, PhonebookShareAlert.this.backgroundPaint);
                    }

                    @Override // android.view.ViewGroup
                    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                        if (motionEvent.getAction() != 0 || PhonebookShareAlert.this.scrollOffsetY == 0 || motionEvent.getY() >= PhonebookShareAlert.this.scrollOffsetY || PhonebookShareAlert.this.actionBar.getAlpha() != 0.0f) {
                            return super.onInterceptTouchEvent(motionEvent);
                        }
                        PhonebookShareAlert.this.dismiss();
                        return true;
                    }

                    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
                    protected void onLayout(boolean z, int i6, int i7, int i8, int i9) {
                        PhonebookShareAlert.this.inLayout = true;
                        super.onLayout(z, i6, i7, i8, i9);
                        PhonebookShareAlert.this.inLayout = false;
                        PhonebookShareAlert.this.updateLayout(false);
                    }

                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i6, int i7) {
                        int size = View.MeasureSpec.getSize(i7);
                        if (Build.VERSION.SDK_INT >= 21) {
                            this.ignoreLayout = true;
                            setPadding(((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, 0);
                            this.ignoreLayout = false;
                        }
                        int paddingTop = size - getPaddingTop();
                        View.MeasureSpec.getSize(i6);
                        int unused = ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft;
                        ((FrameLayout.LayoutParams) PhonebookShareAlert.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                        this.ignoreLayout = true;
                        int dp = AndroidUtilities.dp(80.0f);
                        int itemCount2 = PhonebookShareAlert.this.listAdapter.getItemCount();
                        for (int i8 = 0; i8 < itemCount2; i8++) {
                            View createView = PhonebookShareAlert.this.listAdapter.createView(parentActivity, i8);
                            createView.measure(i6, View.MeasureSpec.makeMeasureSpec(0, 0));
                            dp += createView.getMeasuredHeight();
                        }
                        int i9 = dp < paddingTop ? paddingTop - dp : paddingTop / 5;
                        if (PhonebookShareAlert.this.scrollView.getPaddingTop() != i9) {
                            PhonebookShareAlert.this.scrollView.getPaddingTop();
                            PhonebookShareAlert.this.scrollView.setPadding(0, i9, 0, 0);
                        }
                        this.ignoreLayout = false;
                        super.onMeasure(i6, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
                    }

                    @Override // android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        return !PhonebookShareAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
                    }

                    @Override // android.view.View, android.view.ViewParent
                    public void requestLayout() {
                        if (this.ignoreLayout) {
                            return;
                        }
                        super.requestLayout();
                    }
                };
                frameLayout.setWillNotDraw(false);
                this.containerView = frameLayout;
                setApplyTopPadding(false);
                setApplyBottomPadding(false);
                this.listAdapter = new ListAdapter();
                NestedScrollView nestedScrollView = new NestedScrollView(parentActivity) { // from class: org.telegram.ui.Components.PhonebookShareAlert.2
                    private View focusingView;

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // androidx.core.widget.NestedScrollView
                    public int computeScrollDeltaToGetChildRectOnScreen(android.graphics.Rect rect) {
                        if (this.focusingView == null || PhonebookShareAlert.this.linearLayout.getTop() != getPaddingTop()) {
                            return 0;
                        }
                        int computeScrollDeltaToGetChildRectOnScreen = super.computeScrollDeltaToGetChildRectOnScreen(rect);
                        int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() - (((this.focusingView.getTop() - getScrollY()) + rect.top) + computeScrollDeltaToGetChildRectOnScreen);
                        return currentActionBarHeight > 0 ? computeScrollDeltaToGetChildRectOnScreen - (currentActionBarHeight + AndroidUtilities.dp(10.0f)) : computeScrollDeltaToGetChildRectOnScreen;
                    }

                    @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup, android.view.ViewParent
                    public void requestChildFocus(View view, View view2) {
                        this.focusingView = view2;
                        super.requestChildFocus(view, view2);
                    }
                };
                this.scrollView = nestedScrollView;
                nestedScrollView.setClipToPadding(false);
                this.scrollView.setVerticalScrollBarEnabled(false);
                frameLayout.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 77.0f));
                LinearLayout linearLayout = new LinearLayout(parentActivity);
                this.linearLayout = linearLayout;
                linearLayout.setOrientation(1);
                this.scrollView.addView(this.linearLayout, LayoutHelper.createScroll(-1, -1, 51));
                this.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() { // from class: org.telegram.ui.Components.PhonebookShareAlert$$ExternalSyntheticLambda0
                    @Override // androidx.core.widget.NestedScrollView.OnScrollChangeListener
                    public final void onScrollChange(NestedScrollView nestedScrollView2, int i6, int i7, int i8, int i9) {
                        PhonebookShareAlert.this.lambda$new$0(nestedScrollView2, i6, i7, i8, i9);
                    }
                });
                itemCount = this.listAdapter.getItemCount();
                for (i = 0; i < itemCount; i++) {
                    final View createView = this.listAdapter.createView(parentActivity, i);
                    this.linearLayout.addView(createView, LayoutHelper.createLinear(-1, -2));
                    if ((i >= this.phoneStartRow && i < this.phoneEndRow) || (i >= this.vcardStartRow && i < this.vcardEndRow)) {
                        createView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        createView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhonebookShareAlert$$ExternalSyntheticLambda1
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PhonebookShareAlert.this.lambda$new$2(i, createView, view);
                            }
                        });
                        createView.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.PhonebookShareAlert$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnLongClickListener
                            public final boolean onLongClick(View view) {
                                boolean lambda$new$3;
                                lambda$new$3 = PhonebookShareAlert.this.lambda$new$3(i, resourcesProvider, parentActivity, view);
                                return lambda$new$3;
                            }
                        });
                    }
                }
                ActionBar actionBar2 = new ActionBar(parentActivity) { // from class: org.telegram.ui.Components.PhonebookShareAlert.3
                    @Override // android.view.View
                    public void setAlpha(float f) {
                        super.setAlpha(f);
                        ((BottomSheet) PhonebookShareAlert.this).containerView.invalidate();
                    }
                };
                this.actionBar = actionBar2;
                actionBar2.setBackgroundColor(getThemedColor(Theme.key_dialogBackground));
                this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
                ActionBar actionBar3 = this.actionBar;
                int i6 = Theme.key_dialogTextBlack;
                actionBar3.setItemsColor(getThemedColor(i6), false);
                this.actionBar.setItemsBackgroundColor(getThemedColor(Theme.key_dialogButtonSelector), false);
                this.actionBar.setTitleColor(getThemedColor(i6));
                this.actionBar.setOccupyStatusBar(false);
                this.actionBar.setAlpha(0.0f);
                if (this.isImport) {
                    actionBar = this.actionBar;
                    i2 = R.string.ShareContactTitle;
                } else {
                    actionBar = this.actionBar;
                    i2 = R.string.AddContactPhonebookTitle;
                }
                actionBar.setTitle(LocaleController.getString(i2));
                this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
                this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.Components.PhonebookShareAlert.4
                    @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
                    public void onItemClick(int i7) {
                        if (i7 == -1) {
                            PhonebookShareAlert.this.dismiss();
                        }
                    }
                });
                View view = new View(parentActivity);
                this.actionBarShadow = view;
                view.setAlpha(0.0f);
                View view2 = this.actionBarShadow;
                int i7 = Theme.key_dialogShadowLine;
                view2.setBackgroundColor(getThemedColor(i7));
                this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 1.0f));
                View view3 = new View(parentActivity);
                this.shadow = view3;
                view3.setBackgroundColor(getThemedColor(i7));
                this.shadow.setAlpha(0.0f);
                this.containerView.addView(this.shadow, LayoutHelper.createFrame(-1, 1.0f, 83, 0.0f, 0.0f, 0.0f, 77.0f));
                TextView textView2 = new TextView(parentActivity);
                this.buttonTextView = textView2;
                textView2.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
                this.buttonTextView.setGravity(17);
                this.buttonTextView.setTextColor(getThemedColor(Theme.key_featuredStickers_buttonText));
                this.buttonTextView.setTextSize(1, 14.0f);
                if (this.isImport) {
                    textView = this.buttonTextView;
                    i3 = R.string.ShareContactTitle;
                } else {
                    textView = this.buttonTextView;
                    i3 = R.string.AddContactPhonebookTitle;
                }
                textView.setText(LocaleController.getString(i3));
                this.buttonTextView.setTypeface(AndroidUtilities.bold());
                this.buttonTextView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), getThemedColor(Theme.key_featuredStickers_addButton), getThemedColor(Theme.key_featuredStickers_addButtonPressed)));
                frameLayout.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 83, 14.0f, 14.0f, 14.0f, 14.0f));
                this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhonebookShareAlert$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view4) {
                        PhonebookShareAlert.this.lambda$new$5(resourcesProvider, view4);
                    }
                });
            }
        }
        str4 = str2;
        str5 = str3;
        arrayList = null;
        TLRPC.TL_userContact_old2 tL_userContact_old22 = new TLRPC.TL_userContact_old2();
        this.currentUser = tL_userContact_old22;
        if (user2 == null) {
        }
        this.parentFragment = baseFragment;
        final Context parentActivity2 = baseFragment.getParentActivity();
        updateRows();
        FrameLayout frameLayout2 = new FrameLayout(parentActivity2) { // from class: org.telegram.ui.Components.PhonebookShareAlert.1
            private boolean ignoreLayout;
            private RectF rect = new RectF();

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                int i62 = PhonebookShareAlert.this.scrollOffsetY - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop;
                int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(30.0f) + ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop;
                float dp = AndroidUtilities.dp(12.0f);
                float min = ((float) (((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop + i62)) < dp ? 1.0f - Math.min(1.0f, ((dp - i62) - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop) / dp) : 1.0f;
                if (Build.VERSION.SDK_INT >= 21) {
                    int i72 = AndroidUtilities.statusBarHeight;
                    i62 += i72;
                    measuredHeight -= i72;
                }
                ((BottomSheet) PhonebookShareAlert.this).shadowDrawable.setBounds(0, i62, getMeasuredWidth(), measuredHeight);
                ((BottomSheet) PhonebookShareAlert.this).shadowDrawable.draw(canvas);
                if (min != 1.0f) {
                    PhonebookShareAlert.this.backgroundPaint.setColor(PhonebookShareAlert.this.getThemedColor(Theme.key_dialogBackground));
                    this.rect.set(((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop + i62, getMeasuredWidth() - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingTop + i62 + AndroidUtilities.dp(24.0f));
                    float f = dp * min;
                    canvas.drawRoundRect(this.rect, f, f, PhonebookShareAlert.this.backgroundPaint);
                }
                int themedColor = PhonebookShareAlert.this.getThemedColor(Theme.key_dialogBackground);
                PhonebookShareAlert.this.backgroundPaint.setColor(Color.argb((int) (PhonebookShareAlert.this.actionBar.getAlpha() * 255.0f), (int) (Color.red(themedColor) * 0.8f), (int) (Color.green(themedColor) * 0.8f), (int) (Color.blue(themedColor) * 0.8f)));
                canvas.drawRect(((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, PhonebookShareAlert.this.backgroundPaint);
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 || PhonebookShareAlert.this.scrollOffsetY == 0 || motionEvent.getY() >= PhonebookShareAlert.this.scrollOffsetY || PhonebookShareAlert.this.actionBar.getAlpha() != 0.0f) {
                    return super.onInterceptTouchEvent(motionEvent);
                }
                PhonebookShareAlert.this.dismiss();
                return true;
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i62, int i72, int i8, int i9) {
                PhonebookShareAlert.this.inLayout = true;
                super.onLayout(z, i62, i72, i8, i9);
                PhonebookShareAlert.this.inLayout = false;
                PhonebookShareAlert.this.updateLayout(false);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i62, int i72) {
                int size = View.MeasureSpec.getSize(i72);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.ignoreLayout = true;
                    setPadding(((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft, 0);
                    this.ignoreLayout = false;
                }
                int paddingTop = size - getPaddingTop();
                View.MeasureSpec.getSize(i62);
                int unused = ((BottomSheet) PhonebookShareAlert.this).backgroundPaddingLeft;
                ((FrameLayout.LayoutParams) PhonebookShareAlert.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                this.ignoreLayout = true;
                int dp = AndroidUtilities.dp(80.0f);
                int itemCount2 = PhonebookShareAlert.this.listAdapter.getItemCount();
                for (int i8 = 0; i8 < itemCount2; i8++) {
                    View createView2 = PhonebookShareAlert.this.listAdapter.createView(parentActivity2, i8);
                    createView2.measure(i62, View.MeasureSpec.makeMeasureSpec(0, 0));
                    dp += createView2.getMeasuredHeight();
                }
                int i9 = dp < paddingTop ? paddingTop - dp : paddingTop / 5;
                if (PhonebookShareAlert.this.scrollView.getPaddingTop() != i9) {
                    PhonebookShareAlert.this.scrollView.getPaddingTop();
                    PhonebookShareAlert.this.scrollView.setPadding(0, i9, 0, 0);
                }
                this.ignoreLayout = false;
                super.onMeasure(i62, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return !PhonebookShareAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        frameLayout2.setWillNotDraw(false);
        this.containerView = frameLayout2;
        setApplyTopPadding(false);
        setApplyBottomPadding(false);
        this.listAdapter = new ListAdapter();
        NestedScrollView nestedScrollView2 = new NestedScrollView(parentActivity2) { // from class: org.telegram.ui.Components.PhonebookShareAlert.2
            private View focusingView;

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.core.widget.NestedScrollView
            public int computeScrollDeltaToGetChildRectOnScreen(android.graphics.Rect rect) {
                if (this.focusingView == null || PhonebookShareAlert.this.linearLayout.getTop() != getPaddingTop()) {
                    return 0;
                }
                int computeScrollDeltaToGetChildRectOnScreen = super.computeScrollDeltaToGetChildRectOnScreen(rect);
                int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() - (((this.focusingView.getTop() - getScrollY()) + rect.top) + computeScrollDeltaToGetChildRectOnScreen);
                return currentActionBarHeight > 0 ? computeScrollDeltaToGetChildRectOnScreen - (currentActionBarHeight + AndroidUtilities.dp(10.0f)) : computeScrollDeltaToGetChildRectOnScreen;
            }

            @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup, android.view.ViewParent
            public void requestChildFocus(View view4, View view22) {
                this.focusingView = view22;
                super.requestChildFocus(view4, view22);
            }
        };
        this.scrollView = nestedScrollView2;
        nestedScrollView2.setClipToPadding(false);
        this.scrollView.setVerticalScrollBarEnabled(false);
        frameLayout2.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 77.0f));
        LinearLayout linearLayout2 = new LinearLayout(parentActivity2);
        this.linearLayout = linearLayout2;
        linearLayout2.setOrientation(1);
        this.scrollView.addView(this.linearLayout, LayoutHelper.createScroll(-1, -1, 51));
        this.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() { // from class: org.telegram.ui.Components.PhonebookShareAlert$$ExternalSyntheticLambda0
            @Override // androidx.core.widget.NestedScrollView.OnScrollChangeListener
            public final void onScrollChange(NestedScrollView nestedScrollView22, int i62, int i72, int i8, int i9) {
                PhonebookShareAlert.this.lambda$new$0(nestedScrollView22, i62, i72, i8, i9);
            }
        });
        itemCount = this.listAdapter.getItemCount();
        while (i < itemCount) {
        }
        ActionBar actionBar22 = new ActionBar(parentActivity2) { // from class: org.telegram.ui.Components.PhonebookShareAlert.3
            @Override // android.view.View
            public void setAlpha(float f) {
                super.setAlpha(f);
                ((BottomSheet) PhonebookShareAlert.this).containerView.invalidate();
            }
        };
        this.actionBar = actionBar22;
        actionBar22.setBackgroundColor(getThemedColor(Theme.key_dialogBackground));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        ActionBar actionBar32 = this.actionBar;
        int i62 = Theme.key_dialogTextBlack;
        actionBar32.setItemsColor(getThemedColor(i62), false);
        this.actionBar.setItemsBackgroundColor(getThemedColor(Theme.key_dialogButtonSelector), false);
        this.actionBar.setTitleColor(getThemedColor(i62));
        this.actionBar.setOccupyStatusBar(false);
        this.actionBar.setAlpha(0.0f);
        if (this.isImport) {
        }
        actionBar.setTitle(LocaleController.getString(i2));
        this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.Components.PhonebookShareAlert.4
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i72) {
                if (i72 == -1) {
                    PhonebookShareAlert.this.dismiss();
                }
            }
        });
        View view4 = new View(parentActivity2);
        this.actionBarShadow = view4;
        view4.setAlpha(0.0f);
        View view22 = this.actionBarShadow;
        int i72 = Theme.key_dialogShadowLine;
        view22.setBackgroundColor(getThemedColor(i72));
        this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 1.0f));
        View view32 = new View(parentActivity2);
        this.shadow = view32;
        view32.setBackgroundColor(getThemedColor(i72));
        this.shadow.setAlpha(0.0f);
        this.containerView.addView(this.shadow, LayoutHelper.createFrame(-1, 1.0f, 83, 0.0f, 0.0f, 0.0f, 77.0f));
        TextView textView22 = new TextView(parentActivity2);
        this.buttonTextView = textView22;
        textView22.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
        this.buttonTextView.setGravity(17);
        this.buttonTextView.setTextColor(getThemedColor(Theme.key_featuredStickers_buttonText));
        this.buttonTextView.setTextSize(1, 14.0f);
        if (this.isImport) {
        }
        textView.setText(LocaleController.getString(i3));
        this.buttonTextView.setTypeface(AndroidUtilities.bold());
        this.buttonTextView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), getThemedColor(Theme.key_featuredStickers_addButton), getThemedColor(Theme.key_featuredStickers_addButtonPressed)));
        frameLayout2.addView(this.buttonTextView, LayoutHelper.createFrame(-1, 48.0f, 83, 14.0f, 14.0f, 14.0f, 14.0f));
        this.buttonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhonebookShareAlert$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view42) {
                PhonebookShareAlert.this.lambda$new$5(resourcesProvider, view42);
            }
        });
    }

    public PhonebookShareAlert(BaseFragment baseFragment, ContactsController.Contact contact, TLRPC.User user, Uri uri, File file, String str, String str2, Theme.ResourcesProvider resourcesProvider) {
        this(baseFragment, contact, user, uri, file, null, str, str2, resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(NestedScrollView nestedScrollView, int i, int i2, int i3, int i4) {
        updateLayout(!this.inLayout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(AndroidUtilities.VcardItem vcardItem, DialogInterface dialogInterface, int i) {
        if (i == 0) {
            try {
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", vcardItem.getValue(false)));
                if (AndroidUtilities.shouldShowClipboardToast()) {
                    Toast.makeText(this.parentFragment.getParentActivity(), LocaleController.getString(R.string.TextCopied), 0).show();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0023 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0024  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$new$2(int i, View view, View view2) {
        final AndroidUtilities.VcardItem vcardItem;
        ArrayList arrayList;
        boolean z = false;
        int i2 = this.phoneStartRow;
        if (i < i2 || i >= this.phoneEndRow) {
            i2 = this.vcardStartRow;
            if (i < i2 || i >= this.vcardEndRow) {
                vcardItem = null;
                if (vcardItem != null) {
                    return;
                }
                if (!this.isImport) {
                    vcardItem.checked = !vcardItem.checked;
                    if (i >= this.phoneStartRow && i < this.phoneEndRow) {
                        int i3 = 0;
                        while (true) {
                            if (i3 >= this.phones.size()) {
                                break;
                            } else if (((AndroidUtilities.VcardItem) this.phones.get(i3)).checked) {
                                z = true;
                                break;
                            } else {
                                i3++;
                            }
                        }
                        int themedColor = getThemedColor(Theme.key_featuredStickers_buttonText);
                        this.buttonTextView.setEnabled(z);
                        TextView textView = this.buttonTextView;
                        if (!z) {
                            themedColor &= ConnectionsManager.DEFAULT_DATACENTER_ID;
                        }
                        textView.setTextColor(themedColor);
                    }
                    ((TextCheckBoxCell) view).setChecked(vcardItem.checked);
                    return;
                }
                int i4 = vcardItem.type;
                if (i4 == 0) {
                    try {
                        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + vcardItem.getValue(false)));
                        intent.addFlags(268435456);
                        this.parentFragment.getParentActivity().startActivityForResult(intent, 500);
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        return;
                    }
                } else if (i4 == 1) {
                    Browser.openUrl(this.parentFragment.getParentActivity(), "mailto:" + vcardItem.getValue(false));
                    return;
                } else if (i4 != 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.parentFragment.getParentActivity());
                    builder.setItems(new CharSequence[]{LocaleController.getString(R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.PhonebookShareAlert$$ExternalSyntheticLambda5
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i5) {
                            PhonebookShareAlert.this.lambda$new$1(vcardItem, dialogInterface, i5);
                        }
                    });
                    builder.show();
                    return;
                } else {
                    String value = vcardItem.getValue(false);
                    if (!value.startsWith("http")) {
                        value = "http://" + value;
                    }
                    Browser.openUrl(this.parentFragment.getParentActivity(), value);
                    return;
                }
            }
            arrayList = this.other;
        } else {
            arrayList = this.phones;
        }
        vcardItem = (AndroidUtilities.VcardItem) arrayList.get(i - i2);
        if (vcardItem != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0021 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0022  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ boolean lambda$new$3(int i, Theme.ResourcesProvider resourcesProvider, Context context, View view) {
        AndroidUtilities.VcardItem vcardItem;
        ArrayList arrayList;
        ImageView imageView;
        int i2;
        Bulletin make;
        int i3 = this.phoneStartRow;
        if (i < i3 || i >= this.phoneEndRow) {
            i3 = this.vcardStartRow;
            if (i < i3 || i >= this.vcardEndRow) {
                vcardItem = null;
                if (vcardItem != null) {
                    return false;
                }
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", vcardItem.getValue(false)));
                if (BulletinFactory.canShowBulletin(this.parentFragment)) {
                    if (vcardItem.type == 3) {
                        make = BulletinFactory.of((FrameLayout) this.containerView, resourcesProvider).createCopyLinkBulletin();
                    } else {
                        Bulletin.SimpleLayout simpleLayout = new Bulletin.SimpleLayout(context, resourcesProvider);
                        int i4 = vcardItem.type;
                        if (i4 == 0) {
                            simpleLayout.textView.setText(LocaleController.getString(R.string.PhoneCopied));
                            imageView = simpleLayout.imageView;
                            i2 = R.drawable.msg_calls;
                        } else if (i4 == 1) {
                            simpleLayout.textView.setText(LocaleController.getString(R.string.EmailCopied));
                            imageView = simpleLayout.imageView;
                            i2 = R.drawable.msg_mention;
                        } else {
                            simpleLayout.textView.setText(LocaleController.getString(R.string.TextCopied));
                            imageView = simpleLayout.imageView;
                            i2 = R.drawable.msg_info;
                        }
                        imageView.setImageResource(i2);
                        if (AndroidUtilities.shouldShowClipboardToast()) {
                            make = Bulletin.make((FrameLayout) this.containerView, simpleLayout, 1500);
                        }
                    }
                    make.show();
                }
                return true;
            }
            arrayList = this.other;
        } else {
            arrayList = this.phones;
        }
        vcardItem = (AndroidUtilities.VcardItem) arrayList.get(i - i3);
        if (vcardItem != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(boolean z, int i) {
        this.delegate.didSelectContact(this.currentUser, z, i, 0L, false);
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(Theme.ResourcesProvider resourcesProvider, View view) {
        StringBuilder sb;
        if (this.isImport) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(LocaleController.getString(R.string.AddContactTitle));
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            builder.setItems(new CharSequence[]{LocaleController.getString(R.string.CreateNewContact), LocaleController.getString(R.string.AddToExistingContact)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.PhonebookShareAlert.5
                private void fillRowWithType(String str, ContentValues contentValues) {
                    int i = 2;
                    if (!str.startsWith("X-")) {
                        if ("PREF".equalsIgnoreCase(str)) {
                            i = 12;
                        } else if ("HOME".equalsIgnoreCase(str)) {
                            i = 1;
                        } else if (!"MOBILE".equalsIgnoreCase(str) && !"CELL".equalsIgnoreCase(str)) {
                            if ("OTHER".equalsIgnoreCase(str)) {
                                i = 7;
                            } else if ("WORK".equalsIgnoreCase(str)) {
                                i = 3;
                            } else if ("RADIO".equalsIgnoreCase(str) || "VOICE".equalsIgnoreCase(str)) {
                                i = 14;
                            } else if ("PAGER".equalsIgnoreCase(str)) {
                                i = 6;
                            } else if ("CALLBACK".equalsIgnoreCase(str)) {
                                i = 8;
                            } else if ("CAR".equalsIgnoreCase(str)) {
                                i = 9;
                            } else if ("ASSISTANT".equalsIgnoreCase(str)) {
                                i = 19;
                            } else if ("MMS".equalsIgnoreCase(str)) {
                                i = 20;
                            } else if (str.startsWith("FAX")) {
                                i = 4;
                            } else {
                                contentValues.put("data2", (Integer) 0);
                            }
                        }
                        contentValues.put("data2", Integer.valueOf(i));
                        return;
                    }
                    contentValues.put("data2", (Integer) 0);
                    str = str.substring(2);
                    contentValues.put("data3", str);
                }

                private void fillUrlRowWithType(String str, ContentValues contentValues) {
                    int i;
                    int i2;
                    if (!str.startsWith("X-")) {
                        if ("HOMEPAGE".equalsIgnoreCase(str)) {
                            i = 1;
                        } else if ("BLOG".equalsIgnoreCase(str)) {
                            i2 = 2;
                            contentValues.put("data2", i2);
                            return;
                        } else if ("PROFILE".equalsIgnoreCase(str)) {
                            i = 3;
                        } else if ("HOME".equalsIgnoreCase(str)) {
                            i = 4;
                        } else if ("WORK".equalsIgnoreCase(str)) {
                            i = 5;
                        } else if ("FTP".equalsIgnoreCase(str)) {
                            i = 6;
                        } else if ("OTHER".equalsIgnoreCase(str)) {
                            i = 7;
                        } else {
                            contentValues.put("data2", (Integer) 0);
                        }
                        i2 = Integer.valueOf(i);
                        contentValues.put("data2", i2);
                        return;
                    }
                    contentValues.put("data2", (Integer) 0);
                    str = str.substring(2);
                    contentValues.put("data3", str);
                }

                /* JADX WARN: Code restructure failed: missing block: B:100:0x0256, code lost:
                    if ("OTHER".equalsIgnoreCase(r1) != false) goto L62;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:59:0x019d, code lost:
                    if ("OTHER".equalsIgnoreCase(r1) != false) goto L62;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:60:0x019f, code lost:
                    r0 = 3;
                 */
                /* JADX WARN: Removed duplicated region for block: B:12:0x0057 A[LOOP:0: B:10:0x0047->B:12:0x0057, LOOP_END] */
                /* JADX WARN: Removed duplicated region for block: B:136:0x02e7  */
                /* JADX WARN: Removed duplicated region for block: B:138:0x02ec  */
                /* JADX WARN: Removed duplicated region for block: B:16:0x0090  */
                /* JADX WARN: Removed duplicated region for block: B:95:0x0242  */
                @Override // android.content.DialogInterface.OnClickListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent;
                    String str;
                    int i2;
                    int i3;
                    Intent intent2;
                    int i4;
                    boolean z;
                    5 r6;
                    String rawType;
                    ContentValues contentValues;
                    int i5;
                    ContentValues contentValues2;
                    5 r1 = this;
                    int i6 = 1;
                    try {
                        if (i == 0) {
                            intent = new Intent("android.intent.action.INSERT");
                            str = "vnd.android.cursor.dir/raw_contact";
                        } else if (i != 1) {
                            intent = null;
                            intent.putExtra("name", ContactsController.formatName(PhonebookShareAlert.this.currentUser.first_name, PhonebookShareAlert.this.currentUser.last_name));
                            ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
                            boolean z2 = false;
                            for (i2 = 0; i2 < PhonebookShareAlert.this.phones.size(); i2++) {
                                AndroidUtilities.VcardItem vcardItem = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.phones.get(i2);
                                ContentValues contentValues3 = new ContentValues();
                                contentValues3.put("mimetype", "vnd.android.cursor.item/phone_v2");
                                contentValues3.put("data1", vcardItem.getValue(false));
                                r1.fillRowWithType(vcardItem.getRawType(false), contentValues3);
                                arrayList.add(contentValues3);
                            }
                            i3 = 0;
                            boolean z3 = false;
                            while (i3 < PhonebookShareAlert.this.other.size()) {
                                AndroidUtilities.VcardItem vcardItem2 = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.other.get(i3);
                                int i7 = vcardItem2.type;
                                if (i7 == i6) {
                                    contentValues2 = new ContentValues();
                                    contentValues2.put("mimetype", "vnd.android.cursor.item/email_v2");
                                    contentValues2.put("data1", vcardItem2.getValue(z2));
                                    r1.fillRowWithType(vcardItem2.getRawType(z2), contentValues2);
                                } else if (i7 == 3) {
                                    contentValues2 = new ContentValues();
                                    contentValues2.put("mimetype", "vnd.android.cursor.item/website");
                                    contentValues2.put("data1", vcardItem2.getValue(z2));
                                    r1.fillUrlRowWithType(vcardItem2.getRawType(z2), contentValues2);
                                } else if (i7 == 4) {
                                    contentValues2 = new ContentValues();
                                    contentValues2.put("mimetype", "vnd.android.cursor.item/note");
                                    contentValues2.put("data1", vcardItem2.getValue(z2));
                                } else if (i7 == 5) {
                                    contentValues2 = new ContentValues();
                                    contentValues2.put("mimetype", "vnd.android.cursor.item/contact_event");
                                    contentValues2.put("data1", vcardItem2.getValue(z2));
                                    contentValues2.put("data2", (Integer) 3);
                                } else {
                                    intent2 = intent;
                                    i4 = i3;
                                    if (i7 == 2) {
                                        contentValues = new ContentValues();
                                        contentValues.put("mimetype", "vnd.android.cursor.item/postal-address_v2");
                                        String[] rawValue = vcardItem2.getRawValue();
                                        z = z3;
                                        if (rawValue.length > 0) {
                                            contentValues.put("data5", rawValue[0]);
                                        }
                                        if (rawValue.length > 1) {
                                            contentValues.put("data6", rawValue[1]);
                                        }
                                        if (rawValue.length > 2) {
                                            contentValues.put("data4", rawValue[2]);
                                        }
                                        if (rawValue.length > 3) {
                                            contentValues.put("data7", rawValue[3]);
                                        }
                                        if (rawValue.length > 4) {
                                            contentValues.put("data8", rawValue[4]);
                                        }
                                        if (rawValue.length > 5) {
                                            contentValues.put("data9", rawValue[5]);
                                        }
                                        if (rawValue.length > 6) {
                                            contentValues.put("data10", rawValue[6]);
                                        }
                                        String rawType2 = vcardItem2.getRawType(false);
                                        if (!"HOME".equalsIgnoreCase(rawType2)) {
                                            if (!"WORK".equalsIgnoreCase(rawType2)) {
                                            }
                                            int i8 = 2;
                                            contentValues.put("data2", Integer.valueOf(i8));
                                            arrayList.add(contentValues);
                                            r6 = this;
                                        }
                                        contentValues.put("data2", (Integer) 1);
                                        arrayList.add(contentValues);
                                        r6 = this;
                                    } else {
                                        z = z3;
                                        if (i7 == 20) {
                                            contentValues = new ContentValues();
                                            contentValues.put("mimetype", "vnd.android.cursor.item/im");
                                            String rawType3 = vcardItem2.getRawType(true);
                                            String rawType4 = vcardItem2.getRawType(false);
                                            contentValues.put("data1", vcardItem2.getValue(false));
                                            if ("AIM".equalsIgnoreCase(rawType3)) {
                                                i5 = 0;
                                            } else if ("MSN".equalsIgnoreCase(rawType3)) {
                                                i5 = 1;
                                            } else if ("YAHOO".equalsIgnoreCase(rawType3)) {
                                                i5 = 2;
                                            } else if ("SKYPE".equalsIgnoreCase(rawType3)) {
                                                i5 = 3;
                                            } else if ("QQ".equalsIgnoreCase(rawType3)) {
                                                i5 = 4;
                                            } else if ("GOOGLE-TALK".equalsIgnoreCase(rawType3)) {
                                                i5 = 5;
                                            } else if ("ICQ".equalsIgnoreCase(rawType3)) {
                                                i5 = 6;
                                            } else if ("JABBER".equalsIgnoreCase(rawType3)) {
                                                i5 = 7;
                                            } else if ("NETMEETING".equalsIgnoreCase(rawType3)) {
                                                i5 = 8;
                                            } else {
                                                contentValues.put("data5", (Integer) (-1));
                                                contentValues.put("data6", vcardItem2.getRawType(true));
                                                if (!"HOME".equalsIgnoreCase(rawType4)) {
                                                    if (!"WORK".equalsIgnoreCase(rawType4)) {
                                                    }
                                                    int i82 = 2;
                                                    contentValues.put("data2", Integer.valueOf(i82));
                                                    arrayList.add(contentValues);
                                                    r6 = this;
                                                }
                                                contentValues.put("data2", (Integer) 1);
                                                arrayList.add(contentValues);
                                                r6 = this;
                                            }
                                            contentValues.put("data5", Integer.valueOf(i5));
                                            if (!"HOME".equalsIgnoreCase(rawType4)) {
                                            }
                                            contentValues.put("data2", (Integer) 1);
                                            arrayList.add(contentValues);
                                            r6 = this;
                                        } else {
                                            if (i7 == 6 && !z) {
                                                ContentValues contentValues4 = new ContentValues();
                                                contentValues4.put("mimetype", "vnd.android.cursor.item/organization");
                                                r6 = this;
                                                for (int i9 = i4; i9 < PhonebookShareAlert.this.other.size(); i9++) {
                                                    AndroidUtilities.VcardItem vcardItem3 = (AndroidUtilities.VcardItem) PhonebookShareAlert.this.other.get(i9);
                                                    if (vcardItem3.type == 6) {
                                                        String rawType5 = vcardItem3.getRawType(true);
                                                        if ("ORG".equalsIgnoreCase(rawType5)) {
                                                            String[] rawValue2 = vcardItem3.getRawValue();
                                                            if (rawValue2.length != 0) {
                                                                if (rawValue2.length >= 1) {
                                                                    contentValues4.put("data1", rawValue2[0]);
                                                                }
                                                                if (rawValue2.length >= 2) {
                                                                    contentValues4.put("data5", rawValue2[1]);
                                                                }
                                                            }
                                                        } else if ("TITLE".equalsIgnoreCase(rawType5) || "ROLE".equalsIgnoreCase(rawType5)) {
                                                            contentValues4.put("data4", vcardItem3.getValue(false));
                                                            rawType = vcardItem3.getRawType(true);
                                                            if (!"WORK".equalsIgnoreCase(rawType)) {
                                                                contentValues4.put("data2", (Integer) 1);
                                                            } else if ("OTHER".equalsIgnoreCase(rawType)) {
                                                                contentValues4.put("data2", (Integer) 2);
                                                            }
                                                        }
                                                        rawType = vcardItem3.getRawType(true);
                                                        if (!"WORK".equalsIgnoreCase(rawType)) {
                                                        }
                                                    }
                                                }
                                                arrayList.add(contentValues4);
                                                z3 = true;
                                                r1 = r6;
                                                i6 = 1;
                                                z2 = false;
                                                i3 = i4 + 1;
                                                intent = intent2;
                                            }
                                            r6 = this;
                                        }
                                    }
                                    z3 = z;
                                    r1 = r6;
                                    i6 = 1;
                                    z2 = false;
                                    i3 = i4 + 1;
                                    intent = intent2;
                                }
                                arrayList.add(contentValues2);
                                intent2 = intent;
                                i4 = i3;
                                z = z3;
                                r6 = r1;
                                z3 = z;
                                r1 = r6;
                                i6 = 1;
                                z2 = false;
                                i3 = i4 + 1;
                                intent = intent2;
                            }
                            Intent intent3 = intent;
                            5 r62 = r1;
                            intent3.putExtra("finishActivityOnSaveCompleted", true);
                            intent3.putParcelableArrayListExtra("data", arrayList);
                            PhonebookShareAlert.this.parentFragment.getParentActivity().startActivity(intent3);
                            PhonebookShareAlert.this.dismiss();
                            return;
                        } else {
                            intent = new Intent("android.intent.action.INSERT_OR_EDIT");
                            str = "vnd.android.cursor.item/contact";
                        }
                        PhonebookShareAlert.this.parentFragment.getParentActivity().startActivity(intent3);
                        PhonebookShareAlert.this.dismiss();
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        return;
                    }
                    intent.setType(str);
                    intent.putExtra("name", ContactsController.formatName(PhonebookShareAlert.this.currentUser.first_name, PhonebookShareAlert.this.currentUser.last_name));
                    ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>();
                    boolean z22 = false;
                    while (i2 < PhonebookShareAlert.this.phones.size()) {
                    }
                    i3 = 0;
                    boolean z32 = false;
                    while (i3 < PhonebookShareAlert.this.other.size()) {
                    }
                    Intent intent32 = intent;
                    5 r622 = r1;
                    intent32.putExtra("finishActivityOnSaveCompleted", true);
                    intent32.putParcelableArrayListExtra("data", arrayList2);
                }
            });
            builder.show();
            return;
        }
        if (this.currentUser.restriction_reason.isEmpty()) {
            Locale locale = Locale.US;
            TLRPC.User user = this.currentUser;
            sb = new StringBuilder(String.format(locale, "BEGIN:VCARD\nVERSION:3.0\nFN:%1$s\nEND:VCARD", ContactsController.formatName(user.first_name, user.last_name)));
        } else {
            sb = new StringBuilder(this.currentUser.restriction_reason.get(0).text);
        }
        int lastIndexOf = sb.lastIndexOf("END:VCARD");
        if (lastIndexOf >= 0) {
            this.currentUser.phone = null;
            for (int size = this.phones.size() - 1; size >= 0; size--) {
                AndroidUtilities.VcardItem vcardItem = (AndroidUtilities.VcardItem) this.phones.get(size);
                if (vcardItem.checked) {
                    TLRPC.User user2 = this.currentUser;
                    if (user2.phone == null) {
                        user2.phone = vcardItem.getValue(false);
                    }
                    for (int i = 0; i < vcardItem.vcardData.size(); i++) {
                        sb.insert(lastIndexOf, vcardItem.vcardData.get(i) + "\n");
                    }
                }
            }
            for (int size2 = this.other.size() - 1; size2 >= 0; size2--) {
                AndroidUtilities.VcardItem vcardItem2 = (AndroidUtilities.VcardItem) this.other.get(size2);
                if (vcardItem2.checked) {
                    for (int size3 = vcardItem2.vcardData.size() - 1; size3 >= 0; size3 += -1) {
                        sb.insert(lastIndexOf, vcardItem2.vcardData.get(size3) + "\n");
                    }
                }
            }
            this.currentUser.restriction_reason.clear();
            TLRPC.RestrictionReason restrictionReason = new TLRPC.RestrictionReason();
            restrictionReason.text = sb.toString();
            restrictionReason.reason = "";
            restrictionReason.platform = "";
            this.currentUser.restriction_reason.add(restrictionReason);
        }
        BaseFragment baseFragment = this.parentFragment;
        if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).isInScheduleMode()) {
            AlertsCreator.createScheduleDatePickerDialog(getContext(), ((ChatActivity) this.parentFragment).getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.PhonebookShareAlert$$ExternalSyntheticLambda4
                @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                public final void didSelectDate(boolean z, int i2) {
                    PhonebookShareAlert.this.lambda$new$4(z, i2);
                }
            }, resourcesProvider);
            return;
        }
        this.delegate.didSelectContact(this.currentUser, true, 0, 0L, false);
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLayout(boolean z) {
        View childAt = this.scrollView.getChildAt(0);
        int top = childAt.getTop() - this.scrollView.getScrollY();
        if (top < 0) {
            top = 0;
        }
        boolean z2 = top <= 0;
        if ((z2 && this.actionBar.getTag() == null) || (!z2 && this.actionBar.getTag() != null)) {
            this.actionBar.setTag(z2 ? 1 : null);
            AnimatorSet animatorSet = this.actionBarAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.actionBarAnimation = null;
            }
            if (z) {
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.actionBarAnimation = animatorSet2;
                animatorSet2.setDuration(180L);
                AnimatorSet animatorSet3 = this.actionBarAnimation;
                ActionBar actionBar = this.actionBar;
                Property property = View.ALPHA;
                animatorSet3.playTogether(ObjectAnimator.ofFloat(actionBar, property, z2 ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.actionBarShadow, property, z2 ? 1.0f : 0.0f));
                this.actionBarAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.PhonebookShareAlert.7
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PhonebookShareAlert.this.actionBarAnimation = null;
                    }
                });
                this.actionBarAnimation.start();
            } else {
                this.actionBar.setAlpha(z2 ? 1.0f : 0.0f);
                this.actionBarShadow.setAlpha(z2 ? 1.0f : 0.0f);
            }
        }
        if (this.scrollOffsetY != top) {
            this.scrollOffsetY = top;
            this.containerView.invalidate();
        }
        childAt.getBottom();
        this.scrollView.getMeasuredHeight();
        boolean z3 = childAt.getBottom() - this.scrollView.getScrollY() > this.scrollView.getMeasuredHeight();
        if (!(z3 && this.shadow.getTag() == null) && (z3 || this.shadow.getTag() == null)) {
            return;
        }
        this.shadow.setTag(z3 ? 1 : null);
        AnimatorSet animatorSet4 = this.shadowAnimation;
        if (animatorSet4 != null) {
            animatorSet4.cancel();
            this.shadowAnimation = null;
        }
        if (!z) {
            this.shadow.setAlpha(z3 ? 1.0f : 0.0f);
            return;
        }
        AnimatorSet animatorSet5 = new AnimatorSet();
        this.shadowAnimation = animatorSet5;
        animatorSet5.setDuration(180L);
        this.shadowAnimation.playTogether(ObjectAnimator.ofFloat(this.shadow, View.ALPHA, z3 ? 1.0f : 0.0f));
        this.shadowAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.PhonebookShareAlert.8
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                PhonebookShareAlert.this.shadowAnimation = null;
            }
        });
        this.shadowAnimation.start();
    }

    private void updateRows() {
        this.rowCount = 1;
        this.userRow = 0;
        if (this.phones.size() > 1 || !this.other.isEmpty()) {
            if (this.phones.isEmpty()) {
                this.phoneStartRow = -1;
                this.phoneEndRow = -1;
            } else {
                int i = this.rowCount;
                this.phoneStartRow = i;
                int size = i + this.phones.size();
                this.rowCount = size;
                this.phoneEndRow = size;
            }
            if (!this.other.isEmpty()) {
                int i2 = this.rowCount;
                this.vcardStartRow = i2;
                int size2 = i2 + this.other.size();
                this.rowCount = size2;
                this.vcardEndRow = size2;
                return;
            }
        } else {
            this.phoneStartRow = -1;
            this.phoneEndRow = -1;
        }
        this.vcardStartRow = -1;
        this.vcardEndRow = -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean canDismissWithSwipe() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onStart() {
        super.onStart();
        Bulletin.addDelegate((FrameLayout) this.containerView, new Bulletin.Delegate() { // from class: org.telegram.ui.Components.PhonebookShareAlert.6
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean allowLayoutChanges() {
                return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean bottomOffsetAnimated() {
                return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean clipWithGradient(int i) {
                return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getBottomOffset(int i) {
                return AndroidUtilities.dp(74.0f);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ int getTopOffset(int i) {
                return Bulletin.Delegate.-CC.$default$getTopOffset(this, i);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onBottomOffsetChange(float f) {
                Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onHide(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onShow(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
            }
        });
    }

    @Override // android.app.Dialog
    protected void onStop() {
        super.onStop();
        Bulletin.removeDelegate((FrameLayout) this.containerView);
    }

    public void setDelegate(ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate phonebookShareAlertDelegate) {
        this.delegate = phonebookShareAlertDelegate;
    }
}
