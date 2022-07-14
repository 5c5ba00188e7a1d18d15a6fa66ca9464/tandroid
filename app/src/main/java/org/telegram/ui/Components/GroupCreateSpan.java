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
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes5.dex */
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

    public GroupCreateSpan(Context context, Object object) {
        this(context, object, null);
    }

    public GroupCreateSpan(Context context, ContactsController.Contact contact) {
        this(context, null, contact);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0098, code lost:
        if (r10.equals("non_contacts") != false) goto L30;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public GroupCreateSpan(Context context, Object object, ContactsController.Contact contact) {
        super(context);
        Object imageParent;
        ImageLocation imageLocation;
        String firstName;
        int maxNameWidth;
        Object imageParent2;
        this.rect = new RectF();
        this.colors = new int[8];
        this.currentContact = contact;
        this.deleteDrawable = getResources().getDrawable(R.drawable.delete);
        textPaint.setTextSize(AndroidUtilities.dp(14.0f));
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        this.avatarDrawable = avatarDrawable;
        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        char c = 1;
        if (object instanceof String) {
            imageLocation = null;
            String str = (String) object;
            this.avatarDrawable.setSmallSize(true);
            switch (str.hashCode()) {
                case -1716307998:
                    if (str.equals("archived")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -1237460524:
                    if (str.equals("groups")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -1197490811:
                    break;
                case -567451565:
                    if (str.equals("contacts")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3029900:
                    if (str.equals("bots")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 3496342:
                    if (str.equals("read")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 104264043:
                    if (str.equals("muted")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 1432626128:
                    if (str.equals("channels")) {
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
                    firstName = LocaleController.getString("FilterContacts", R.string.FilterContacts);
                    break;
                case 1:
                    this.avatarDrawable.setAvatarType(5);
                    this.uid = -2147483647L;
                    firstName = LocaleController.getString("FilterNonContacts", R.string.FilterNonContacts);
                    break;
                case 2:
                    this.avatarDrawable.setAvatarType(6);
                    this.uid = -2147483646L;
                    firstName = LocaleController.getString("FilterGroups", R.string.FilterGroups);
                    break;
                case 3:
                    this.avatarDrawable.setAvatarType(7);
                    this.uid = -2147483645L;
                    firstName = LocaleController.getString("FilterChannels", R.string.FilterChannels);
                    break;
                case 4:
                    this.avatarDrawable.setAvatarType(8);
                    this.uid = -2147483644L;
                    firstName = LocaleController.getString("FilterBots", R.string.FilterBots);
                    break;
                case 5:
                    this.avatarDrawable.setAvatarType(9);
                    this.uid = -2147483643L;
                    firstName = LocaleController.getString("FilterMuted", R.string.FilterMuted);
                    break;
                case 6:
                    this.avatarDrawable.setAvatarType(10);
                    this.uid = -2147483642L;
                    firstName = LocaleController.getString("FilterRead", R.string.FilterRead);
                    break;
                default:
                    this.avatarDrawable.setAvatarType(11);
                    this.uid = -2147483641L;
                    firstName = LocaleController.getString("FilterArchived", R.string.FilterArchived);
                    break;
            }
            imageParent = null;
        } else if (object instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) object;
            this.uid = user.id;
            if (UserObject.isReplyUser(user)) {
                String firstName2 = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                this.avatarDrawable.setSmallSize(true);
                this.avatarDrawable.setAvatarType(12);
                imageParent2 = null;
                firstName = firstName2;
                imageLocation = null;
            } else if (UserObject.isUserSelf(user)) {
                String firstName3 = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                this.avatarDrawable.setSmallSize(true);
                this.avatarDrawable.setAvatarType(1);
                imageParent2 = null;
                firstName = firstName3;
                imageLocation = null;
            } else {
                this.avatarDrawable.setInfo(user);
                String firstName4 = UserObject.getFirstName(user);
                ImageLocation imageLocation2 = ImageLocation.getForUserOrChat(user, 1);
                imageParent2 = user;
                firstName = firstName4;
                imageLocation = imageLocation2;
            }
            imageParent = imageParent2;
        } else if (object instanceof TLRPC.Chat) {
            TLRPC.Chat chat = (TLRPC.Chat) object;
            this.avatarDrawable.setInfo(chat);
            this.uid = -chat.id;
            String firstName5 = chat.title;
            ImageLocation imageLocation3 = ImageLocation.getForUserOrChat(chat, 1);
            firstName = firstName5;
            imageLocation = imageLocation3;
            imageParent = chat;
        } else {
            this.avatarDrawable.setInfo(0L, contact.first_name, contact.last_name);
            this.uid = contact.contact_id;
            this.key = contact.key;
            if (!TextUtils.isEmpty(contact.first_name)) {
                firstName = contact.first_name;
            } else {
                firstName = contact.last_name;
            }
            imageLocation = null;
            imageParent = null;
        }
        ImageReceiver imageReceiver = new ImageReceiver();
        this.imageReceiver = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.dp(16.0f));
        this.imageReceiver.setParentView(this);
        this.imageReceiver.setImageCoords(0.0f, 0.0f, AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
        if (AndroidUtilities.isTablet()) {
            maxNameWidth = AndroidUtilities.dp(366.0f) / 2;
        } else {
            maxNameWidth = (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) - AndroidUtilities.dp(164.0f)) / 2;
        }
        CharSequence name = TextUtils.ellipsize(firstName.replace('\n', ' '), textPaint, maxNameWidth, TextUtils.TruncateAt.END);
        StaticLayout staticLayout = new StaticLayout(name, textPaint, 1000, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        this.nameLayout = staticLayout;
        if (staticLayout.getLineCount() > 0) {
            this.textWidth = (int) Math.ceil(this.nameLayout.getLineWidth(0));
            this.textX = -this.nameLayout.getLineLeft(0);
        }
        this.imageReceiver.setImage(imageLocation, "50_50", this.avatarDrawable, 0L, (String) null, imageParent, 1);
        updateColors();
    }

    public void updateColors() {
        int color = this.avatarDrawable.getColor();
        int back = Theme.getColor(Theme.key_groupcreate_spanBackground);
        int delete = Theme.getColor(Theme.key_groupcreate_spanDelete);
        this.colors[0] = Color.red(back);
        this.colors[1] = Color.red(color);
        this.colors[2] = Color.green(back);
        this.colors[3] = Color.green(color);
        this.colors[4] = Color.blue(back);
        this.colors[5] = Color.blue(color);
        this.colors[6] = Color.alpha(back);
        this.colors[7] = Color.alpha(color);
        this.deleteDrawable.setColorFilter(new PorterDuffColorFilter(delete, PorterDuff.Mode.MULTIPLY));
        backPaint.setColor(back);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(AndroidUtilities.dp(57.0f) + this.textWidth, AndroidUtilities.dp(32.0f));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        boolean z = this.deleting;
        if ((z && this.progress != 1.0f) || (!z && this.progress != 0.0f)) {
            long newTime = System.currentTimeMillis();
            long dt = newTime - this.lastUpdateTime;
            if (dt < 0 || dt > 17) {
                dt = 17;
            }
            if (this.deleting) {
                float f = this.progress + (((float) dt) / 120.0f);
                this.progress = f;
                if (f >= 1.0f) {
                    this.progress = 1.0f;
                }
            } else {
                float f2 = this.progress - (((float) dt) / 120.0f);
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
            int color = this.avatarDrawable.getColor();
            float alpha = Color.alpha(color) / 255.0f;
            backPaint.setColor(color);
            backPaint.setAlpha((int) (this.progress * 255.0f * alpha));
            canvas.drawCircle(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), backPaint);
            canvas.save();
            canvas.rotate((1.0f - this.progress) * 45.0f, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.deleteDrawable.setBounds(AndroidUtilities.dp(11.0f), AndroidUtilities.dp(11.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(21.0f));
            this.deleteDrawable.setAlpha((int) (this.progress * 255.0f));
            this.deleteDrawable.draw(canvas);
            canvas.restore();
        }
        canvas.translate(this.textX + AndroidUtilities.dp(41.0f), AndroidUtilities.dp(8.0f));
        int text = Theme.getColor(Theme.key_groupcreate_spanText);
        int textSelected = Theme.getColor(Theme.key_avatar_text);
        textPaint.setColor(ColorUtils.blendARGB(text, textSelected, this.progress));
        this.nameLayout.draw(canvas);
        canvas.restore();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setText(this.nameLayout.getText());
        if (isDeleting() && Build.VERSION.SDK_INT >= 21) {
            info.addAction(new AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.getId(), LocaleController.getString("Delete", R.string.Delete)));
        }
    }
}
