package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class GroupCreateSpan extends View {
    private AvatarDrawable avatarDrawable;
    private int[] colors;
    private ContactsController.Contact currentContact;
    private Drawable deleteDrawable;
    private boolean deleting;
    private ImageReceiver imageReceiver;
    private String key;
    private long lastUpdateTime;
    private StaticLayout nameLayout;
    private float progress;
    private RectF rect;
    private int textWidth;
    private float textX;
    private long uid;
    private static TextPaint textPaint = new TextPaint(1);
    private static Paint backPaint = new Paint(1);

    public GroupCreateSpan(Context context, Object obj) {
        this(context, obj, null);
    }

    public GroupCreateSpan(Context context, ContactsController.Contact contact) {
        this(context, null, contact);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0097, code lost:
        if (r1.equals("non_contacts") != false) goto L30;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:60:0x021b  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0223  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x025f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public GroupCreateSpan(Context context, Object obj, ContactsController.Contact contact) {
        super(context);
        TLRPC$User tLRPC$User;
        String str;
        int i;
        StaticLayout staticLayout;
        TLRPC$User tLRPC$User2;
        this.rect = new RectF();
        this.colors = new int[8];
        this.currentContact = contact;
        this.deleteDrawable = getResources().getDrawable(2131165371);
        textPaint.setTextSize(AndroidUtilities.dp(14.0f));
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        this.avatarDrawable = avatarDrawable;
        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        char c = 1;
        ImageLocation imageLocation = null;
        if (obj instanceof String) {
            String str2 = (String) obj;
            this.avatarDrawable.setSmallSize(true);
            switch (str2.hashCode()) {
                case -1716307998:
                    if (str2.equals("archived")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -1237460524:
                    if (str2.equals("groups")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -1197490811:
                    break;
                case -567451565:
                    if (str2.equals("contacts")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3029900:
                    if (str2.equals("bots")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 3496342:
                    if (str2.equals("read")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 104264043:
                    if (str2.equals("muted")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 1432626128:
                    if (str2.equals("channels")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    this.avatarDrawable.setAvatarType(4);
                    this.uid = -2147483648L;
                    str = LocaleController.getString("FilterContacts", 2131625845);
                    break;
                case 1:
                    this.avatarDrawable.setAvatarType(5);
                    this.uid = -2147483647L;
                    str = LocaleController.getString("FilterNonContacts", 2131625875);
                    break;
                case 2:
                    this.avatarDrawable.setAvatarType(6);
                    this.uid = -2147483646L;
                    str = LocaleController.getString("FilterGroups", 2131625862);
                    break;
                case 3:
                    this.avatarDrawable.setAvatarType(7);
                    this.uid = -2147483645L;
                    str = LocaleController.getString("FilterChannels", 2131625836);
                    break;
                case 4:
                    this.avatarDrawable.setAvatarType(8);
                    this.uid = -2147483644L;
                    str = LocaleController.getString("FilterBots", 2131625835);
                    break;
                case 5:
                    this.avatarDrawable.setAvatarType(9);
                    this.uid = -2147483643L;
                    str = LocaleController.getString("FilterMuted", 2131625865);
                    break;
                case 6:
                    this.avatarDrawable.setAvatarType(10);
                    this.uid = -2147483642L;
                    str = LocaleController.getString("FilterRead", 2131625876);
                    break;
                default:
                    this.avatarDrawable.setAvatarType(11);
                    this.uid = -2147483641L;
                    str = LocaleController.getString("FilterArchived", 2131625832);
                    break;
            }
        } else {
            if (obj instanceof TLRPC$User) {
                TLRPC$User tLRPC$User3 = (TLRPC$User) obj;
                this.uid = tLRPC$User3.id;
                if (UserObject.isReplyUser(tLRPC$User3)) {
                    str = LocaleController.getString("RepliesTitle", 2131627920);
                    this.avatarDrawable.setSmallSize(true);
                    this.avatarDrawable.setAvatarType(12);
                } else if (UserObject.isUserSelf(tLRPC$User3)) {
                    str = LocaleController.getString("SavedMessages", 2131628077);
                    this.avatarDrawable.setSmallSize(true);
                    this.avatarDrawable.setAvatarType(1);
                } else {
                    this.avatarDrawable.setInfo(tLRPC$User3);
                    String firstName = UserObject.getFirstName(tLRPC$User3);
                    imageLocation = ImageLocation.getForUserOrChat(tLRPC$User3, 1);
                    tLRPC$User2 = tLRPC$User3;
                    str = firstName;
                    tLRPC$User = tLRPC$User2;
                }
                tLRPC$User2 = null;
                tLRPC$User = tLRPC$User2;
            } else if (obj instanceof TLRPC$Chat) {
                TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj;
                this.avatarDrawable.setInfo(tLRPC$Chat);
                this.uid = -tLRPC$Chat.id;
                str = tLRPC$Chat.title;
                tLRPC$User = tLRPC$Chat;
                imageLocation = ImageLocation.getForUserOrChat(tLRPC$Chat, 1);
            } else {
                this.avatarDrawable.setInfo(0L, contact.first_name, contact.last_name);
                this.uid = contact.contact_id;
                this.key = contact.key;
                if (!TextUtils.isEmpty(contact.first_name)) {
                    str = contact.first_name;
                } else {
                    str = contact.last_name;
                }
            }
            ImageReceiver imageReceiver = new ImageReceiver();
            this.imageReceiver = imageReceiver;
            imageReceiver.setRoundRadius(AndroidUtilities.dp(16.0f));
            this.imageReceiver.setParentView(this);
            this.imageReceiver.setImageCoords(0.0f, 0.0f, AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
            if (!AndroidUtilities.isTablet()) {
                i = AndroidUtilities.dp(366.0f) / 2;
            } else {
                android.graphics.Point point = AndroidUtilities.displaySize;
                i = (Math.min(point.x, point.y) - AndroidUtilities.dp(164.0f)) / 2;
            }
            staticLayout = new StaticLayout(TextUtils.ellipsize(str.replace('\n', ' '), textPaint, i, TextUtils.TruncateAt.END), textPaint, 1000, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.nameLayout = staticLayout;
            if (staticLayout.getLineCount() > 0) {
                this.textWidth = (int) Math.ceil(this.nameLayout.getLineWidth(0));
                this.textX = -this.nameLayout.getLineLeft(0);
            }
            this.imageReceiver.setImage(imageLocation, "50_50", this.avatarDrawable, 0L, (String) null, tLRPC$User, 1);
            updateColors();
        }
        tLRPC$User = null;
        ImageReceiver imageReceiver2 = new ImageReceiver();
        this.imageReceiver = imageReceiver2;
        imageReceiver2.setRoundRadius(AndroidUtilities.dp(16.0f));
        this.imageReceiver.setParentView(this);
        this.imageReceiver.setImageCoords(0.0f, 0.0f, AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
        if (!AndroidUtilities.isTablet()) {
        }
        staticLayout = new StaticLayout(TextUtils.ellipsize(str.replace('\n', ' '), textPaint, i, TextUtils.TruncateAt.END), textPaint, 1000, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        this.nameLayout = staticLayout;
        if (staticLayout.getLineCount() > 0) {
        }
        this.imageReceiver.setImage(imageLocation, "50_50", this.avatarDrawable, 0L, (String) null, tLRPC$User, 1);
        updateColors();
    }

    public void updateColors() {
        int color = this.avatarDrawable.getColor();
        int color2 = Theme.getColor("groupcreate_spanBackground");
        int color3 = Theme.getColor("groupcreate_spanDelete");
        this.colors[0] = Color.red(color2);
        this.colors[1] = Color.red(color);
        this.colors[2] = Color.green(color2);
        this.colors[3] = Color.green(color);
        this.colors[4] = Color.blue(color2);
        this.colors[5] = Color.blue(color);
        this.colors[6] = Color.alpha(color2);
        this.colors[7] = Color.alpha(color);
        this.deleteDrawable.setColorFilter(new PorterDuffColorFilter(color3, PorterDuff.Mode.MULTIPLY));
        backPaint.setColor(color2);
    }

    public boolean isDeleting() {
        return this.deleting;
    }

    public void startDeleteAnimation() {
        if (this.deleting) {
            return;
        }
        this.deleting = true;
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    public void cancelDeleteAnimation() {
        if (!this.deleting) {
            return;
        }
        this.deleting = false;
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    public long getUid() {
        return this.uid;
    }

    public String getKey() {
        return this.key;
    }

    public ContactsController.Contact getContact() {
        return this.currentContact;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(AndroidUtilities.dp(57.0f) + this.textWidth, AndroidUtilities.dp(32.0f));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int color;
        boolean z = this.deleting;
        if ((z && this.progress != 1.0f) || (!z && this.progress != 0.0f)) {
            long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
            if (currentTimeMillis < 0 || currentTimeMillis > 17) {
                currentTimeMillis = 17;
            }
            if (this.deleting) {
                float f = this.progress + (((float) currentTimeMillis) / 120.0f);
                this.progress = f;
                if (f >= 1.0f) {
                    this.progress = 1.0f;
                }
            } else {
                float f2 = this.progress - (((float) currentTimeMillis) / 120.0f);
                this.progress = f2;
                if (f2 < 0.0f) {
                    this.progress = 0.0f;
                }
            }
            invalidate();
        }
        canvas.save();
        this.rect.set(0.0f, 0.0f, getMeasuredWidth(), AndroidUtilities.dp(32.0f));
        Paint paint = backPaint;
        int[] iArr = this.colors;
        int i = iArr[6];
        float f3 = iArr[7] - iArr[6];
        float f4 = this.progress;
        paint.setColor(Color.argb(i + ((int) (f3 * f4)), iArr[0] + ((int) ((iArr[1] - iArr[0]) * f4)), iArr[2] + ((int) ((iArr[3] - iArr[2]) * f4)), iArr[4] + ((int) ((iArr[5] - iArr[4]) * f4))));
        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), backPaint);
        this.imageReceiver.draw(canvas);
        if (this.progress != 0.0f) {
            backPaint.setColor(this.avatarDrawable.getColor());
            backPaint.setAlpha((int) (this.progress * 255.0f * (Color.alpha(color) / 255.0f)));
            canvas.drawCircle(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), backPaint);
            canvas.save();
            canvas.rotate((1.0f - this.progress) * 45.0f, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.deleteDrawable.setBounds(AndroidUtilities.dp(11.0f), AndroidUtilities.dp(11.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(21.0f));
            this.deleteDrawable.setAlpha((int) (this.progress * 255.0f));
            this.deleteDrawable.draw(canvas);
            canvas.restore();
        }
        canvas.translate(this.textX + AndroidUtilities.dp(41.0f), AndroidUtilities.dp(8.0f));
        textPaint.setColor(ColorUtils.blendARGB(Theme.getColor("groupcreate_spanText"), Theme.getColor("avatar_text"), this.progress));
        this.nameLayout.draw(canvas);
        canvas.restore();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setText(this.nameLayout.getText());
        if (!isDeleting() || Build.VERSION.SDK_INT < 21) {
            return;
        }
        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.getId(), LocaleController.getString("Delete", 2131625368)));
    }
}
