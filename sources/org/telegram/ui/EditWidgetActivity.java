package org.telegram.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatsWidgetProvider;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsWidgetProvider;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.InviteMembersBottomSheet;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.EditWidgetActivity;

/* loaded from: classes4.dex */
public class EditWidgetActivity extends BaseFragment {
    private int chatsEndRow;
    private int chatsStartRow;
    private int currentWidgetId;
    private EditWidgetActivityDelegate delegate;
    private int infoRow;
    private ItemTouchHelper itemTouchHelper;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private ImageView previewImageView;
    private int previewRow;
    private int rowCount;
    private int selectChatsRow;
    private ArrayList selectedDialogs = new ArrayList();
    private WidgetPreviewCell widgetPreviewCell;
    private int widgetType;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 2 implements RecyclerListView.OnItemLongClickListenerExtended {
        private Rect rect = new Rect();

        2() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(int i, DialogInterface dialogInterface, int i2) {
            if (i2 == 0) {
                EditWidgetActivity.this.selectedDialogs.remove(i - EditWidgetActivity.this.chatsStartRow);
                EditWidgetActivity.this.updateRows();
                if (EditWidgetActivity.this.widgetPreviewCell != null) {
                    EditWidgetActivity.this.widgetPreviewCell.updateDialogs();
                }
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public boolean onItemClick(View view, final int i, float f, float f2) {
            if (EditWidgetActivity.this.getParentActivity() != null && (view instanceof GroupCreateUserCell)) {
                ((ImageView) view.getTag(R.id.object_tag)).getHitRect(this.rect);
                if (!this.rect.contains((int) f, (int) f2)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditWidgetActivity.this.getParentActivity());
                    builder.setItems(new CharSequence[]{LocaleController.getString(R.string.Delete)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.EditWidgetActivity$2$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i2) {
                            EditWidgetActivity.2.this.lambda$onItemClick$0(i, dialogInterface, i2);
                        }
                    });
                    EditWidgetActivity.this.showDialog(builder.create());
                    return true;
                }
            }
            return false;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onLongClickRelease() {
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
        public void onMove(float f, float f2) {
        }
    }

    /* loaded from: classes4.dex */
    public interface EditWidgetActivityDelegate {
        void didSelectDialogs(ArrayList arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateViewHolder$0(GroupCreateUserCell groupCreateUserCell, View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            EditWidgetActivity.this.itemTouchHelper.startDrag(EditWidgetActivity.this.listView.getChildViewHolder(groupCreateUserCell));
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return EditWidgetActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == EditWidgetActivity.this.previewRow) {
                return 2;
            }
            if (i == EditWidgetActivity.this.selectChatsRow) {
                return 1;
            }
            return i == EditWidgetActivity.this.infoRow ? 0 : 3;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 1 || itemViewType == 3;
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0042, code lost:
        
            if (r9 != (r7.this$0.chatsEndRow - 1)) goto L18;
         */
        /* JADX WARN: Code restructure failed: missing block: B:11:0x005e, code lost:
        
            r1 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x005f, code lost:
        
            r8.setObject(r0, null, null, r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:?, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x005b, code lost:
        
            if (r9 != (r7.this$0.chatsEndRow - 1)) goto L18;
         */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0101  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            TLObject chat;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i == EditWidgetActivity.this.infoRow) {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    if (EditWidgetActivity.this.widgetType != 0) {
                        if (EditWidgetActivity.this.widgetType == 1) {
                            i2 = R.string.EditWidgetContactsInfo;
                        }
                        if (SharedConfig.passcodeHash.length() > 0) {
                            spannableStringBuilder.append((CharSequence) "\n\n").append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(R.string.WidgetPasscode2)));
                        }
                        textInfoPrivacyCell.setText(spannableStringBuilder);
                        return;
                    }
                    i2 = R.string.EditWidgetChatsInfo;
                    spannableStringBuilder.append((CharSequence) LocaleController.getString(i2));
                    if (SharedConfig.passcodeHash.length() > 0) {
                    }
                    textInfoPrivacyCell.setText(spannableStringBuilder);
                    return;
                }
                return;
            }
            if (itemViewType == 1) {
                TextCell textCell = (TextCell) viewHolder.itemView;
                textCell.setColors(-1, Theme.key_windowBackgroundWhiteBlueText4);
                Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.poll_add_circle);
                Drawable drawable2 = this.mContext.getResources().getDrawable(R.drawable.poll_add_plus);
                int color = Theme.getColor(Theme.key_switchTrackChecked);
                PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
                drawable.setColorFilter(new PorterDuffColorFilter(color, mode));
                drawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_checkboxCheck), mode));
                textCell.setTextAndIcon(LocaleController.getString(R.string.SelectChats), new CombinedDrawable(drawable, drawable2), EditWidgetActivity.this.chatsStartRow != -1);
                textCell.getImageView().setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
                return;
            }
            if (itemViewType != 3) {
                return;
            }
            GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell) viewHolder.itemView;
            Long l = (Long) EditWidgetActivity.this.selectedDialogs.get(i - EditWidgetActivity.this.chatsStartRow);
            long longValue = l.longValue();
            if (DialogObject.isUserDialog(longValue)) {
                chat = EditWidgetActivity.this.getMessagesController().getUser(l);
            } else {
                chat = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-longValue));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            FrameLayout frameLayout;
            if (i == 0) {
                FrameLayout textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                frameLayout = textInfoPrivacyCell;
            } else if (i == 1) {
                FrameLayout textCell = new TextCell(this.mContext);
                textCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                frameLayout = textCell;
            } else if (i != 2) {
                final GroupCreateUserCell groupCreateUserCell = new GroupCreateUserCell(this.mContext, 0, 0, false);
                ImageView imageView = new ImageView(this.mContext);
                imageView.setImageResource(R.drawable.list_reorder);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                groupCreateUserCell.setTag(R.id.object_tag, imageView);
                groupCreateUserCell.addView(imageView, LayoutHelper.createFrame(40, -1.0f, (LocaleController.isRTL ? 3 : 5) | 16, 10.0f, 0.0f, 10.0f, 0.0f));
                imageView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.EditWidgetActivity$ListAdapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        boolean lambda$onCreateViewHolder$0;
                        lambda$onCreateViewHolder$0 = EditWidgetActivity.ListAdapter.this.lambda$onCreateViewHolder$0(groupCreateUserCell, view, motionEvent);
                        return lambda$onCreateViewHolder$0;
                    }
                });
                imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_pinnedIcon), PorterDuff.Mode.MULTIPLY));
                frameLayout = groupCreateUserCell;
            } else {
                frameLayout = EditWidgetActivity.this.widgetPreviewCell = new WidgetPreviewCell(this.mContext);
            }
            return new RecyclerListView.Holder(frameLayout);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 3 || itemViewType == 1) {
                viewHolder.itemView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
        }

        public boolean swapElements(int i, int i2) {
            int i3 = i - EditWidgetActivity.this.chatsStartRow;
            int i4 = i2 - EditWidgetActivity.this.chatsStartRow;
            int i5 = EditWidgetActivity.this.chatsEndRow - EditWidgetActivity.this.chatsStartRow;
            if (i3 < 0 || i4 < 0 || i3 >= i5 || i4 >= i5) {
                return false;
            }
            Long l = (Long) EditWidgetActivity.this.selectedDialogs.get(i3);
            EditWidgetActivity.this.selectedDialogs.set(i3, (Long) EditWidgetActivity.this.selectedDialogs.get(i4));
            EditWidgetActivity.this.selectedDialogs.set(i4, l);
            notifyItemMoved(i, i2);
            return true;
        }
    }

    /* loaded from: classes4.dex */
    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        private boolean moved;

        public TouchHelperCallback() {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 3 ? ItemTouchHelper.Callback.makeMovementFlags(0, 0) : ItemTouchHelper.Callback.makeMovementFlags(3, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                return false;
            }
            int adapterPosition = viewHolder.getAdapterPosition();
            int adapterPosition2 = viewHolder2.getAdapterPosition();
            if (EditWidgetActivity.this.listAdapter.swapElements(adapterPosition, adapterPosition2)) {
                ((GroupCreateUserCell) viewHolder.itemView).setDrawDivider(adapterPosition2 != EditWidgetActivity.this.chatsEndRow - 1);
                ((GroupCreateUserCell) viewHolder2.itemView).setDrawDivider(adapterPosition != EditWidgetActivity.this.chatsEndRow - 1);
                this.moved = true;
            }
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                EditWidgetActivity.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            } else if (this.moved) {
                if (EditWidgetActivity.this.widgetPreviewCell != null) {
                    EditWidgetActivity.this.widgetPreviewCell.updateDialogs();
                }
                this.moved = false;
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }
    }

    /* loaded from: classes4.dex */
    public class WidgetPreviewCell extends FrameLayout {
        private Drawable backgroundDrawable;
        private BackgroundGradientDrawable.Disposable backgroundGradientDisposable;
        private RectF bitmapRect;
        private ViewGroup[] cells;
        private Drawable oldBackgroundDrawable;
        private BackgroundGradientDrawable.Disposable oldBackgroundGradientDisposable;
        private Paint roundPaint;
        private Drawable shadowDrawable;

        public WidgetPreviewCell(Context context) {
            super(context);
            ImageView imageView;
            int i;
            this.roundPaint = new Paint(1);
            this.bitmapRect = new RectF();
            this.cells = new ViewGroup[2];
            int i2 = 0;
            setWillNotDraw(false);
            setPadding(0, AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f));
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            addView(linearLayout, LayoutHelper.createFrame(-2, -2, 17));
            ChatActionCell chatActionCell = new ChatActionCell(context);
            chatActionCell.setCustomText(LocaleController.getString(R.string.WidgetPreview));
            linearLayout.addView(chatActionCell, LayoutHelper.createLinear(-2, -2, 17, 0, 0, 0, 4));
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(1);
            linearLayout2.setBackgroundResource(R.drawable.widget_bg);
            linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-2, -2, 17, 10, 0, 10, 0));
            EditWidgetActivity.this.previewImageView = new ImageView(context);
            if (EditWidgetActivity.this.widgetType != 0) {
                if (EditWidgetActivity.this.widgetType == 1) {
                    while (i2 < 2) {
                        this.cells[i2] = (ViewGroup) EditWidgetActivity.this.getParentActivity().getLayoutInflater().inflate(R.layout.contacts_widget_item, (ViewGroup) null);
                        linearLayout2.addView(this.cells[i2], LayoutHelper.createLinear(NotificationCenter.audioRouteChanged, -2));
                        i2++;
                    }
                    linearLayout2.addView(EditWidgetActivity.this.previewImageView, LayoutHelper.createLinear(NotificationCenter.audioRouteChanged, NotificationCenter.audioRouteChanged, 17));
                    imageView = EditWidgetActivity.this.previewImageView;
                    i = R.drawable.contacts_widget_preview;
                }
                updateDialogs();
                this.shadowDrawable = Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
            }
            while (i2 < 2) {
                this.cells[i2] = (ViewGroup) EditWidgetActivity.this.getParentActivity().getLayoutInflater().inflate(R.layout.shortcut_widget_item, (ViewGroup) null);
                linearLayout2.addView(this.cells[i2], LayoutHelper.createLinear(-1, -2));
                i2++;
            }
            linearLayout2.addView(EditWidgetActivity.this.previewImageView, LayoutHelper.createLinear(NotificationCenter.channelStarsUpdated, NotificationCenter.audioRouteChanged, 17));
            imageView = EditWidgetActivity.this.previewImageView;
            i = R.drawable.chats_widget_preview;
            imageView.setImageResource(i);
            updateDialogs();
            this.shadowDrawable = Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchSetPressed(boolean z) {
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
            if (disposable != null) {
                disposable.dispose();
                this.backgroundGradientDisposable = null;
            }
            BackgroundGradientDrawable.Disposable disposable2 = this.oldBackgroundGradientDisposable;
            if (disposable2 != null) {
                disposable2.dispose();
                this.oldBackgroundGradientDisposable = null;
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
            if (cachedWallpaperNonBlocking != this.backgroundDrawable && cachedWallpaperNonBlocking != null) {
                if (Theme.isAnimatingColor()) {
                    this.oldBackgroundDrawable = this.backgroundDrawable;
                    this.oldBackgroundGradientDisposable = this.backgroundGradientDisposable;
                } else {
                    BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
                    if (disposable != null) {
                        disposable.dispose();
                        this.backgroundGradientDisposable = null;
                    }
                }
                this.backgroundDrawable = cachedWallpaperNonBlocking;
            }
            float themeAnimationValue = ((BaseFragment) EditWidgetActivity.this).parentLayout.getThemeAnimationValue();
            int i = 0;
            while (i < 2) {
                Drawable drawable = i == 0 ? this.oldBackgroundDrawable : this.backgroundDrawable;
                if (drawable != null) {
                    drawable.setAlpha((i != 1 || this.oldBackgroundDrawable == null || ((BaseFragment) EditWidgetActivity.this).parentLayout == null) ? NotificationCenter.notificationsCountUpdated : (int) (255.0f * themeAnimationValue));
                    if ((drawable instanceof ColorDrawable) || (drawable instanceof GradientDrawable) || (drawable instanceof MotionBackgroundDrawable)) {
                        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                        if (drawable instanceof BackgroundGradientDrawable) {
                            this.backgroundGradientDisposable = ((BackgroundGradientDrawable) drawable).drawExactBoundsSize(canvas, this);
                        } else {
                            drawable.draw(canvas);
                        }
                    } else if (drawable instanceof BitmapDrawable) {
                        if (((BitmapDrawable) drawable).getTileModeX() == Shader.TileMode.REPEAT) {
                            canvas.save();
                            float f = 2.0f / AndroidUtilities.density;
                            canvas.scale(f, f);
                            drawable.setBounds(0, 0, (int) Math.ceil(getMeasuredWidth() / f), (int) Math.ceil(getMeasuredHeight() / f));
                        } else {
                            int measuredHeight = getMeasuredHeight();
                            float max = Math.max(getMeasuredWidth() / drawable.getIntrinsicWidth(), measuredHeight / drawable.getIntrinsicHeight());
                            int ceil = (int) Math.ceil(drawable.getIntrinsicWidth() * max);
                            int ceil2 = (int) Math.ceil(drawable.getIntrinsicHeight() * max);
                            int measuredWidth = (getMeasuredWidth() - ceil) / 2;
                            int i2 = (measuredHeight - ceil2) / 2;
                            canvas.save();
                            canvas.clipRect(0, 0, ceil, getMeasuredHeight());
                            drawable.setBounds(measuredWidth, i2, ceil + measuredWidth, ceil2 + i2);
                        }
                        drawable.draw(canvas);
                        canvas.restore();
                    }
                    if (i == 0 && this.oldBackgroundDrawable != null && themeAnimationValue >= 1.0f) {
                        BackgroundGradientDrawable.Disposable disposable2 = this.oldBackgroundGradientDisposable;
                        if (disposable2 != null) {
                            disposable2.dispose();
                            this.oldBackgroundGradientDisposable = null;
                        }
                        this.oldBackgroundDrawable = null;
                        invalidate();
                    }
                }
                i++;
            }
            this.shadowDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.shadowDrawable.draw(canvas);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(264.0f), 1073741824));
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        /* JADX WARN: Can't wrap try/catch for region: R(21:293|(1:295)(2:389|(1:391)(2:392|(1:394)(3:395|297|(16:388|312|(1:314)(1:387)|315|(2:383|384)(1:317)|318|319|(4:(4:362|363|(2:365|366)(2:368|(1:370))|367)(2:322|323)|324|325|326)(5:375|376|377|378|379)|327|328|(1:330)(1:354)|331|332|(7:334|(1:336)(1:348)|337|(1:339)(1:347)|340|(1:342)(1:346)|343)(3:349|(1:351)(1:353)|352)|344|345)(17:310|311|312|(0)(0)|315|(0)(0)|318|319|(0)(0)|327|328|(0)(0)|331|332|(0)(0)|344|345))))|296|297|(1:299)|388|312|(0)(0)|315|(0)(0)|318|319|(0)(0)|327|328|(0)(0)|331|332|(0)(0)|344|345) */
        /* JADX WARN: Can't wrap try/catch for region: R(8:124|(1:126)(2:134|(1:136)(7:137|(1:139)(1:140)|128|129|130|105|106))|127|128|129|130|105|106) */
        /* JADX WARN: Code restructure failed: missing block: B:132:0x03c2, code lost:
        
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:133:0x03c3, code lost:
        
            org.telegram.messenger.FileLog.e(r0);
            r5 = r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:190:0x04e3, code lost:
        
            if (r3.isMediaEmpty() == false) goto L230;
         */
        /* JADX WARN: Code restructure failed: missing block: B:356:0x085f, code lost:
        
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:68:0x0276, code lost:
        
            if ((r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelMigrateFrom) != false) goto L113;
         */
        /* JADX WARN: Removed duplicated region for block: B:314:0x0791  */
        /* JADX WARN: Removed duplicated region for block: B:317:0x07c0  */
        /* JADX WARN: Removed duplicated region for block: B:321:0x07d7  */
        /* JADX WARN: Removed duplicated region for block: B:330:0x085c A[Catch: all -> 0x085f, TryCatch #6 {all -> 0x085f, blocks: (B:328:0x0853, B:330:0x085c, B:331:0x0863, B:354:0x0861), top: B:327:0x0853 }] */
        /* JADX WARN: Removed duplicated region for block: B:334:0x0876  */
        /* JADX WARN: Removed duplicated region for block: B:349:0x08c2  */
        /* JADX WARN: Removed duplicated region for block: B:354:0x0861 A[Catch: all -> 0x085f, TryCatch #6 {all -> 0x085f, blocks: (B:328:0x0853, B:330:0x085c, B:331:0x0863, B:354:0x0861), top: B:327:0x0853 }] */
        /* JADX WARN: Removed duplicated region for block: B:375:0x0813 A[Catch: all -> 0x080f, TRY_LEAVE, TryCatch #7 {all -> 0x080f, blocks: (B:326:0x0806, B:323:0x0800, B:375:0x0813), top: B:325:0x0806 }] */
        /* JADX WARN: Removed duplicated region for block: B:383:0x07a1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:387:0x0794  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateDialogs() {
            TLRPC.Dialog dialog;
            TLRPC.TL_dialog tL_dialog;
            long longValue;
            TLRPC.Dialog dialog2;
            String str;
            TLRPC.Chat chat;
            TLRPC.FileLocation fileLocation;
            TLRPC.User user;
            TLRPC.FileLocation fileLocation2;
            TLRPC.FileLocation fileLocation3;
            TLRPC.Chat chat2;
            TLRPC.User user2;
            Bitmap decodeFile;
            int i;
            View findViewById;
            int i2;
            int i3;
            Bitmap bitmap;
            AvatarDrawable avatarDrawable;
            int i4;
            TLRPC.UserProfilePhoto userProfilePhoto;
            TLRPC.FileLocation fileLocation4;
            ImageView imageView;
            TLRPC.Dialog dialog3;
            TLRPC.Dialog dialog4;
            TLRPC.Chat chat3;
            String str2;
            TLRPC.User user3;
            TLRPC.FileLocation fileLocation5;
            Bitmap decodeFile2;
            int i5;
            View findViewById2;
            int i6;
            TLRPC.Chat chat4;
            TLRPC.User user4;
            CharSequence charSequence;
            int i7;
            SpannableStringBuilder valueOf;
            char c;
            String format;
            char c2;
            char c3;
            int i8;
            String charSequence2;
            SpannableStringBuilder spannableStringBuilder;
            CharSequence charSequence3;
            CharSequence charSequence4;
            CharSequence charSequence5;
            AvatarDrawable avatarDrawable2;
            int i9;
            TLRPC.UserProfilePhoto userProfilePhoto2;
            TLRPC.FileLocation fileLocation6;
            int i10 = 8;
            long j = 0;
            int i11 = 2;
            int i12 = 0;
            if (EditWidgetActivity.this.widgetType == 0) {
                int i13 = 0;
                while (i13 < i11) {
                    if (EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                        if (i13 < EditWidgetActivity.this.getMessagesController().dialogsServerOnly.size()) {
                            dialog3 = EditWidgetActivity.this.getMessagesController().dialogsServerOnly.get(i13);
                            dialog4 = dialog3;
                        }
                        dialog4 = null;
                    } else {
                        if (i13 < EditWidgetActivity.this.selectedDialogs.size()) {
                            dialog3 = (TLRPC.Dialog) EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(i13)).longValue());
                            if (dialog3 == null) {
                                dialog3 = new TLRPC.TL_dialog();
                                dialog3.id = ((Long) EditWidgetActivity.this.selectedDialogs.get(i13)).longValue();
                            }
                            dialog4 = dialog3;
                        }
                        dialog4 = null;
                    }
                    ViewGroup[] viewGroupArr = this.cells;
                    if (dialog4 == null) {
                        viewGroupArr[i13].setVisibility(i10);
                        i5 = i13;
                    } else {
                        viewGroupArr[i13].setVisibility(i12);
                        if (DialogObject.isUserDialog(dialog4.id)) {
                            user3 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(dialog4.id));
                            if (user3 != null) {
                                if (UserObject.isUserSelf(user3)) {
                                    i9 = R.string.SavedMessages;
                                } else if (UserObject.isReplyUser(user3)) {
                                    i9 = R.string.RepliesTitle;
                                } else if (UserObject.isDeleted(user3)) {
                                    i9 = R.string.HiddenName;
                                } else {
                                    str2 = ContactsController.formatName(user3.first_name, user3.last_name);
                                    if (!UserObject.isReplyUser(user3) && !UserObject.isUserSelf(user3) && (userProfilePhoto2 = user3.photo) != null && (fileLocation6 = userProfilePhoto2.photo_small) != null && fileLocation6.volume_id != j && fileLocation6.local_id != 0) {
                                        fileLocation5 = fileLocation6;
                                        chat3 = null;
                                    }
                                }
                                str2 = LocaleController.getString(i9);
                                if (!UserObject.isReplyUser(user3)) {
                                    fileLocation5 = fileLocation6;
                                    chat3 = null;
                                }
                            } else {
                                str2 = "";
                            }
                            fileLocation5 = null;
                            chat3 = null;
                        } else {
                            TLRPC.Chat chat5 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-dialog4.id));
                            if (chat5 != null) {
                                str2 = chat5.title;
                                TLRPC.ChatPhoto chatPhoto = chat5.photo;
                                if (chatPhoto == null || (fileLocation5 = chatPhoto.photo_small) == null || fileLocation5.volume_id == j || fileLocation5.local_id == 0) {
                                    chat3 = chat5;
                                } else {
                                    chat3 = chat5;
                                    user3 = null;
                                }
                            } else {
                                chat3 = chat5;
                                str2 = "";
                            }
                            user3 = null;
                            fileLocation5 = null;
                        }
                        ((TextView) this.cells[i13].findViewById(R.id.shortcut_widget_item_text)).setText(str2);
                        if (fileLocation5 != null) {
                            try {
                                decodeFile2 = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(fileLocation5, true).toString());
                            } catch (Throwable th) {
                                FileLog.e(th);
                            }
                        } else {
                            decodeFile2 = null;
                        }
                        int dp = AndroidUtilities.dp(48.0f);
                        Bitmap createBitmap = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
                        createBitmap.eraseColor(0);
                        Canvas canvas = new Canvas(createBitmap);
                        if (decodeFile2 == null) {
                            if (user3 != null) {
                                avatarDrawable2 = new AvatarDrawable(user3);
                                if (UserObject.isReplyUser(user3)) {
                                    avatarDrawable2.setAvatarType(12);
                                } else if (UserObject.isUserSelf(user3)) {
                                    avatarDrawable2.setAvatarType(1);
                                }
                            } else {
                                avatarDrawable2 = new AvatarDrawable(chat3);
                            }
                            avatarDrawable2.setBounds(0, 0, dp, dp);
                            avatarDrawable2.draw(canvas);
                        } else {
                            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                            BitmapShader bitmapShader = new BitmapShader(decodeFile2, tileMode, tileMode);
                            if (this.roundPaint == null) {
                                this.roundPaint = new Paint(1);
                                this.bitmapRect = new RectF();
                            }
                            float width = dp / decodeFile2.getWidth();
                            canvas.save();
                            canvas.scale(width, width);
                            this.roundPaint.setShader(bitmapShader);
                            this.bitmapRect.set(0.0f, 0.0f, decodeFile2.getWidth(), decodeFile2.getHeight());
                            canvas.drawRoundRect(this.bitmapRect, decodeFile2.getWidth(), decodeFile2.getHeight(), this.roundPaint);
                            canvas.restore();
                        }
                        canvas.setBitmap(null);
                        ((ImageView) this.cells[i13].findViewById(R.id.shortcut_widget_item_avatar)).setImageBitmap(createBitmap);
                        ArrayList arrayList = (ArrayList) EditWidgetActivity.this.getMessagesController().dialogMessage.get(dialog4.id);
                        MessageObject messageObject = (arrayList == null || arrayList.size() <= 0) ? null : (MessageObject) arrayList.get(0);
                        if (messageObject != null) {
                            long fromChatId = messageObject.getFromChatId();
                            if (fromChatId > j) {
                                user4 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(fromChatId));
                                chat4 = null;
                            } else {
                                chat4 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-fromChatId));
                                user4 = null;
                            }
                            int color = getContext().getResources().getColor(R.color.widget_text);
                            if (messageObject.messageOwner instanceof TLRPC.TL_messageService) {
                                if (ChatObject.isChannel(chat3)) {
                                    TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
                                    charSequence5 = "";
                                    if (!(messageAction instanceof TLRPC.TL_messageActionHistoryClear)) {
                                        charSequence5 = "";
                                    }
                                    i5 = i13;
                                    charSequence4 = charSequence5;
                                }
                                charSequence5 = messageObject.messageText;
                                i5 = i13;
                                charSequence4 = charSequence5;
                            } else {
                                i5 = i13;
                                if (chat3 == null || chat3.id <= j || chat4 != null || (ChatObject.isChannel(chat3) && !ChatObject.isMegagroup(chat3))) {
                                    TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                                    if ((messageMedia instanceof TLRPC.TL_messageMediaPhoto) && (messageMedia.photo instanceof TLRPC.TL_photoEmpty) && messageMedia.ttl_seconds != 0) {
                                        i7 = R.string.AttachPhotoExpired;
                                    } else if ((messageMedia instanceof TLRPC.TL_messageMediaDocument) && (messageMedia.document instanceof TLRPC.TL_documentEmpty) && messageMedia.ttl_seconds != 0) {
                                        i7 = R.string.AttachVideoExpired;
                                    } else if (messageObject.caption != null) {
                                        charSequence3 = (messageObject.isVideo() ? "📹 " : messageObject.isVoice() ? "🎤 " : messageObject.isMusic() ? "🎧 " : messageObject.isPhoto() ? "🖼 " : "📎 ") + ((Object) messageObject.caption);
                                    } else {
                                        if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                                            charSequence = "📊 " + ((TLRPC.TL_messageMediaPoll) messageMedia).poll.question.text;
                                        } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                                            charSequence = "🎮 " + messageObject.messageOwner.media.game.title;
                                        } else if (messageObject.type == 14) {
                                            charSequence = String.format("🎧 %s - %s", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                        } else {
                                            charSequence = messageObject.messageText;
                                            AndroidUtilities.highlightText(charSequence, messageObject.highlightedWords, (Theme.ResourcesProvider) null);
                                        }
                                        CharSequence charSequence6 = charSequence;
                                        charSequence3 = charSequence6;
                                        if (messageObject.messageOwner.media != null) {
                                            charSequence4 = charSequence6;
                                            charSequence3 = charSequence6;
                                        }
                                    }
                                    charSequence3 = LocaleController.getString(i7);
                                } else {
                                    String string = messageObject.isOutOwner() ? LocaleController.getString(R.string.FromYou) : user4 != null ? UserObject.getFirstName(user4).replace("\n", "") : "DELETED";
                                    CharSequence charSequence7 = messageObject.caption;
                                    try {
                                        if (charSequence7 != null) {
                                            String charSequence8 = charSequence7.toString();
                                            if (charSequence8.length() > 150) {
                                                charSequence8 = charSequence8.substring(0, 150);
                                            }
                                            format = String.format("%2$s: \u2068%1$s\u2069", (messageObject.isVideo() ? "📹 " : messageObject.isVoice() ? "🎤 " : messageObject.isMusic() ? "🎧 " : messageObject.isPhoto() ? "🖼 " : "📎 ") + charSequence8.replace('\n', ' '), string);
                                        } else if (messageObject.messageOwner.media == null || messageObject.isMediaEmpty()) {
                                            String str3 = messageObject.messageOwner.message;
                                            if (str3 != null) {
                                                if (str3.length() > 150) {
                                                    c = 0;
                                                    str3 = str3.substring(0, 150);
                                                } else {
                                                    c = 0;
                                                }
                                                Object[] objArr = new Object[2];
                                                objArr[c] = str3.replace('\n', ' ').trim();
                                                objArr[1] = string;
                                                format = String.format("%2$s: \u2068%1$s\u2069", objArr);
                                            } else {
                                                valueOf = SpannableStringBuilder.valueOf("");
                                                spannableStringBuilder = valueOf;
                                                spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, string.length() + 1, 33);
                                                charSequence3 = spannableStringBuilder;
                                            }
                                        } else {
                                            color = getContext().getResources().getColor(R.color.widget_action_text);
                                            TLRPC.MessageMedia messageMedia2 = messageObject.messageOwner.media;
                                            if (messageMedia2 instanceof TLRPC.TL_messageMediaPoll) {
                                                c2 = 1;
                                                c3 = 0;
                                                charSequence2 = String.format("📊 \u2068%s\u2069", ((TLRPC.TL_messageMediaPoll) messageMedia2).poll.question.text);
                                            } else {
                                                c2 = 1;
                                                c3 = 0;
                                                if (messageMedia2 instanceof TLRPC.TL_messageMediaGame) {
                                                    charSequence2 = String.format("🎮 \u2068%s\u2069", messageMedia2.game.title);
                                                } else {
                                                    if (messageObject.type == 14) {
                                                        i8 = 2;
                                                        charSequence2 = String.format("🎧 \u2068%s - %s\u2069", messageObject.getMusicAuthor(), messageObject.getMusicTitle());
                                                    } else {
                                                        i8 = 2;
                                                        charSequence2 = messageObject.messageText.toString();
                                                    }
                                                    String replace = charSequence2.replace('\n', ' ');
                                                    Object[] objArr2 = new Object[i8];
                                                    objArr2[c3] = replace;
                                                    objArr2[c2] = string;
                                                    SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr2));
                                                    valueOf2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), string.length() + 2, valueOf2.length(), 33);
                                                    spannableStringBuilder = valueOf2;
                                                    spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, string.length() + 1, 33);
                                                    charSequence3 = spannableStringBuilder;
                                                }
                                            }
                                            i8 = 2;
                                            String replace2 = charSequence2.replace('\n', ' ');
                                            Object[] objArr22 = new Object[i8];
                                            objArr22[c3] = replace2;
                                            objArr22[c2] = string;
                                            SpannableStringBuilder valueOf22 = SpannableStringBuilder.valueOf(String.format("%2$s: \u2068%1$s\u2069", objArr22));
                                            valueOf22.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage), string.length() + 2, valueOf22.length(), 33);
                                            spannableStringBuilder = valueOf22;
                                            spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, string.length() + 1, 33);
                                            charSequence3 = spannableStringBuilder;
                                        }
                                        spannableStringBuilder.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_nameMessage), 0, string.length() + 1, 33);
                                        charSequence3 = spannableStringBuilder;
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                        charSequence3 = spannableStringBuilder;
                                    }
                                    valueOf = SpannableStringBuilder.valueOf(format);
                                    spannableStringBuilder = valueOf;
                                }
                                ((TextView) this.cells[i5].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                                ViewGroup viewGroup = this.cells[i5];
                                int i14 = R.id.shortcut_widget_item_message;
                                ((TextView) viewGroup.findViewById(i14)).setText(charSequence3.toString());
                                ((TextView) this.cells[i5].findViewById(i14)).setTextColor(color);
                            }
                            color = getContext().getResources().getColor(R.color.widget_action_text);
                            charSequence3 = charSequence4;
                            ((TextView) this.cells[i5].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(messageObject.messageOwner.date));
                            ViewGroup viewGroup2 = this.cells[i5];
                            int i142 = R.id.shortcut_widget_item_message;
                            ((TextView) viewGroup2.findViewById(i142)).setText(charSequence3.toString());
                            ((TextView) this.cells[i5].findViewById(i142)).setTextColor(color);
                        } else {
                            i5 = i13;
                            if (dialog4.last_message_date != 0) {
                                ((TextView) this.cells[i5].findViewById(R.id.shortcut_widget_item_time)).setText(LocaleController.stringForMessageListDate(dialog4.last_message_date));
                            } else {
                                ((TextView) this.cells[i5].findViewById(R.id.shortcut_widget_item_time)).setText("");
                            }
                            ((TextView) this.cells[i5].findViewById(R.id.shortcut_widget_item_message)).setText("");
                        }
                        if (dialog4.unread_count > 0) {
                            ViewGroup viewGroup3 = this.cells[i5];
                            int i15 = R.id.shortcut_widget_item_badge;
                            ((TextView) viewGroup3.findViewById(i15)).setText(String.format("%d", Integer.valueOf(dialog4.unread_count)));
                            this.cells[i5].findViewById(i15).setVisibility(0);
                            if (EditWidgetActivity.this.getMessagesController().isDialogMuted(dialog4.id, 0L)) {
                                findViewById2 = this.cells[i5].findViewById(i15);
                                i6 = R.drawable.widget_counter_muted;
                            } else {
                                findViewById2 = this.cells[i5].findViewById(i15);
                                i6 = R.drawable.widget_counter;
                            }
                            findViewById2.setBackgroundResource(i6);
                        } else {
                            this.cells[i5].findViewById(R.id.shortcut_widget_item_badge).setVisibility(8);
                        }
                    }
                    i13 = i5 + 1;
                    i10 = 8;
                    j = 0;
                    i11 = 2;
                    i12 = 0;
                }
                ViewGroup viewGroup4 = this.cells[0];
                int i16 = R.id.shortcut_widget_item_divider;
                viewGroup4.findViewById(i16).setVisibility(this.cells[1].getVisibility());
                this.cells[1].findViewById(i16).setVisibility(8);
            } else if (EditWidgetActivity.this.widgetType == 1) {
                int i17 = 2;
                int i18 = 0;
                while (i18 < i17) {
                    int i19 = 0;
                    while (i19 < i17) {
                        int i20 = (i18 * 2) + i19;
                        if (EditWidgetActivity.this.selectedDialogs.isEmpty()) {
                            if (i20 < EditWidgetActivity.this.getMediaDataController().hints.size()) {
                                longValue = EditWidgetActivity.this.getMediaDataController().hints.get(i20).peer.user_id;
                                dialog = (TLRPC.Dialog) EditWidgetActivity.this.getMessagesController().dialogs_dict.get(longValue);
                                if (dialog == null) {
                                    tL_dialog = new TLRPC.TL_dialog();
                                    tL_dialog.id = longValue;
                                    dialog2 = tL_dialog;
                                }
                                dialog2 = dialog;
                            }
                            dialog2 = null;
                        } else {
                            if (i20 < EditWidgetActivity.this.selectedDialogs.size()) {
                                dialog = (TLRPC.Dialog) EditWidgetActivity.this.getMessagesController().dialogs_dict.get(((Long) EditWidgetActivity.this.selectedDialogs.get(i20)).longValue());
                                if (dialog == null) {
                                    tL_dialog = new TLRPC.TL_dialog();
                                    longValue = ((Long) EditWidgetActivity.this.selectedDialogs.get(i20)).longValue();
                                    tL_dialog.id = longValue;
                                    dialog2 = tL_dialog;
                                }
                                dialog2 = dialog;
                            }
                            dialog2 = null;
                        }
                        ViewGroup[] viewGroupArr2 = this.cells;
                        if (dialog2 == null) {
                            viewGroupArr2[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item1 : R.id.contacts_widget_item2).setVisibility(4);
                            if (i20 == 0 || i20 == 2) {
                                this.cells[i18].setVisibility(8);
                            }
                            i3 = 1;
                        } else {
                            viewGroupArr2[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item1 : R.id.contacts_widget_item2).setVisibility(0);
                            if (i20 == 0 || i20 == 2) {
                                this.cells[i18].setVisibility(0);
                            }
                            if (DialogObject.isUserDialog(dialog2.id)) {
                                user2 = EditWidgetActivity.this.getMessagesController().getUser(Long.valueOf(dialog2.id));
                                if (UserObject.isUserSelf(user2)) {
                                    i4 = R.string.SavedMessages;
                                } else if (UserObject.isReplyUser(user2)) {
                                    i4 = R.string.RepliesTitle;
                                } else if (UserObject.isDeleted(user2)) {
                                    i4 = R.string.HiddenName;
                                } else {
                                    str = UserObject.getFirstName(user2);
                                    if (!UserObject.isReplyUser(user2) || UserObject.isUserSelf(user2) || user2 == null || (userProfilePhoto = user2.photo) == null || (fileLocation4 = userProfilePhoto.photo_small) == null || fileLocation4.volume_id == 0 || fileLocation4.local_id == 0) {
                                        user = user2;
                                        fileLocation = null;
                                        chat = null;
                                        ((TextView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                        if (fileLocation != null) {
                                            try {
                                                decodeFile = BitmapFactory.decodeFile(EditWidgetActivity.this.getFileLoader().getPathToAttach(fileLocation, true).toString());
                                            } catch (Throwable th2) {
                                                th = th2;
                                                FileLog.e(th);
                                                i = dialog2.unread_count;
                                                if (i <= 0) {
                                                }
                                                findViewById.setVisibility(i2);
                                                i3 = 1;
                                                i19 += i3;
                                                i17 = 2;
                                            }
                                        } else {
                                            decodeFile = null;
                                        }
                                        int dp2 = AndroidUtilities.dp(48.0f);
                                        Bitmap createBitmap2 = Bitmap.createBitmap(dp2, dp2, Bitmap.Config.ARGB_8888);
                                        createBitmap2.eraseColor(0);
                                        Canvas canvas2 = new Canvas(createBitmap2);
                                        if (decodeFile == null) {
                                            if (user != null) {
                                                try {
                                                    avatarDrawable = new AvatarDrawable(user);
                                                    if (UserObject.isReplyUser(user)) {
                                                        avatarDrawable.setAvatarType(12);
                                                    } else if (UserObject.isUserSelf(user)) {
                                                        avatarDrawable.setAvatarType(1);
                                                    }
                                                } catch (Throwable th3) {
                                                    th = th3;
                                                    FileLog.e(th);
                                                    i = dialog2.unread_count;
                                                    if (i <= 0) {
                                                    }
                                                    findViewById.setVisibility(i2);
                                                    i3 = 1;
                                                    i19 += i3;
                                                    i17 = 2;
                                                }
                                            } else {
                                                avatarDrawable = new AvatarDrawable(chat);
                                            }
                                            try {
                                                avatarDrawable.setBounds(0, 0, dp2, dp2);
                                                avatarDrawable.draw(canvas2);
                                                bitmap = null;
                                            } catch (Throwable th4) {
                                                th = th4;
                                                FileLog.e(th);
                                                i = dialog2.unread_count;
                                                if (i <= 0) {
                                                }
                                                findViewById.setVisibility(i2);
                                                i3 = 1;
                                                i19 += i3;
                                                i17 = 2;
                                            }
                                        } else {
                                            Shader.TileMode tileMode2 = Shader.TileMode.CLAMP;
                                            BitmapShader bitmapShader2 = new BitmapShader(decodeFile, tileMode2, tileMode2);
                                            float width2 = dp2 / decodeFile.getWidth();
                                            canvas2.save();
                                            canvas2.scale(width2, width2);
                                            this.roundPaint.setShader(bitmapShader2);
                                            try {
                                                this.bitmapRect.set(0.0f, 0.0f, decodeFile.getWidth(), decodeFile.getHeight());
                                                canvas2.drawRoundRect(this.bitmapRect, decodeFile.getWidth(), decodeFile.getHeight(), this.roundPaint);
                                                canvas2.restore();
                                                bitmap = null;
                                            } catch (Throwable th5) {
                                                th = th5;
                                                FileLog.e(th);
                                                i = dialog2.unread_count;
                                                if (i <= 0) {
                                                }
                                                findViewById.setVisibility(i2);
                                                i3 = 1;
                                                i19 += i3;
                                                i17 = 2;
                                            }
                                        }
                                        canvas2.setBitmap(bitmap);
                                        ((ImageView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap2);
                                        i = dialog2.unread_count;
                                        if (i <= 0) {
                                            ((TextView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_badge1 : R.id.contacts_widget_item_badge2)).setText(i > 99 ? String.format("%d+", 99) : String.format("%d", Integer.valueOf(i)));
                                            findViewById = this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_badge_bg1 : R.id.contacts_widget_item_badge_bg2);
                                            i2 = 0;
                                        } else {
                                            findViewById = this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_badge_bg1 : R.id.contacts_widget_item_badge_bg2);
                                            i2 = 8;
                                        }
                                        findViewById.setVisibility(i2);
                                        i3 = 1;
                                    } else {
                                        fileLocation3 = fileLocation4;
                                        chat2 = null;
                                        chat = chat2;
                                        user = user2;
                                        fileLocation = fileLocation3;
                                        ((TextView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                        if (fileLocation != null) {
                                        }
                                        int dp22 = AndroidUtilities.dp(48.0f);
                                        Bitmap createBitmap22 = Bitmap.createBitmap(dp22, dp22, Bitmap.Config.ARGB_8888);
                                        createBitmap22.eraseColor(0);
                                        Canvas canvas22 = new Canvas(createBitmap22);
                                        if (decodeFile == null) {
                                        }
                                        canvas22.setBitmap(bitmap);
                                        ((ImageView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap22);
                                        i = dialog2.unread_count;
                                        if (i <= 0) {
                                        }
                                        findViewById.setVisibility(i2);
                                        i3 = 1;
                                    }
                                }
                                str = LocaleController.getString(i4);
                                if (UserObject.isReplyUser(user2)) {
                                }
                                user = user2;
                                fileLocation = null;
                                chat = null;
                                ((TextView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                if (fileLocation != null) {
                                }
                                int dp222 = AndroidUtilities.dp(48.0f);
                                Bitmap createBitmap222 = Bitmap.createBitmap(dp222, dp222, Bitmap.Config.ARGB_8888);
                                createBitmap222.eraseColor(0);
                                Canvas canvas222 = new Canvas(createBitmap222);
                                if (decodeFile == null) {
                                }
                                canvas222.setBitmap(bitmap);
                                ((ImageView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap222);
                                i = dialog2.unread_count;
                                if (i <= 0) {
                                }
                                findViewById.setVisibility(i2);
                                i3 = 1;
                            } else {
                                TLRPC.Chat chat6 = EditWidgetActivity.this.getMessagesController().getChat(Long.valueOf(-dialog2.id));
                                str = chat6.title;
                                TLRPC.ChatPhoto chatPhoto2 = chat6.photo;
                                if (chatPhoto2 != null && (fileLocation2 = chatPhoto2.photo_small) != null) {
                                    if (fileLocation2.volume_id != 0 && fileLocation2.local_id != 0) {
                                        fileLocation3 = fileLocation2;
                                        chat2 = chat6;
                                        user2 = null;
                                        chat = chat2;
                                        user = user2;
                                        fileLocation = fileLocation3;
                                        ((TextView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                        if (fileLocation != null) {
                                        }
                                        int dp2222 = AndroidUtilities.dp(48.0f);
                                        Bitmap createBitmap2222 = Bitmap.createBitmap(dp2222, dp2222, Bitmap.Config.ARGB_8888);
                                        createBitmap2222.eraseColor(0);
                                        Canvas canvas2222 = new Canvas(createBitmap2222);
                                        if (decodeFile == null) {
                                        }
                                        canvas2222.setBitmap(bitmap);
                                        ((ImageView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap2222);
                                        i = dialog2.unread_count;
                                        if (i <= 0) {
                                        }
                                        findViewById.setVisibility(i2);
                                        i3 = 1;
                                    }
                                }
                                chat = chat6;
                                fileLocation = null;
                                user = null;
                                ((TextView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_text1 : R.id.contacts_widget_item_text2)).setText(str);
                                if (fileLocation != null) {
                                }
                                int dp22222 = AndroidUtilities.dp(48.0f);
                                Bitmap createBitmap22222 = Bitmap.createBitmap(dp22222, dp22222, Bitmap.Config.ARGB_8888);
                                createBitmap22222.eraseColor(0);
                                Canvas canvas22222 = new Canvas(createBitmap22222);
                                if (decodeFile == null) {
                                }
                                canvas22222.setBitmap(bitmap);
                                ((ImageView) this.cells[i18].findViewById(i19 == 0 ? R.id.contacts_widget_item_avatar1 : R.id.contacts_widget_item_avatar2)).setImageBitmap(createBitmap22222);
                                i = dialog2.unread_count;
                                if (i <= 0) {
                                }
                                findViewById.setVisibility(i2);
                                i3 = 1;
                            }
                        }
                        i19 += i3;
                        i17 = 2;
                    }
                    i18++;
                    i17 = 2;
                }
            }
            int i21 = 0;
            if (this.cells[0].getVisibility() == 0) {
                imageView = EditWidgetActivity.this.previewImageView;
                i21 = 8;
            } else {
                imageView = EditWidgetActivity.this.previewImageView;
            }
            imageView.setVisibility(i21);
        }
    }

    public EditWidgetActivity(int i, int i2) {
        this.widgetType = i;
        this.currentWidgetId = i2;
        ArrayList<TLRPC.User> arrayList = new ArrayList<>();
        ArrayList<TLRPC.Chat> arrayList2 = new ArrayList<>();
        getMessagesStorage().getWidgetDialogIds(this.currentWidgetId, this.widgetType, this.selectedDialogs, arrayList, arrayList2, true);
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        updateRows();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishActivity() {
        if (getParentActivity() == null) {
            return;
        }
        getParentActivity().finish();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                EditWidgetActivity.this.removeSelfFromStack();
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(ArrayList arrayList) {
        this.selectedDialogs.clear();
        this.selectedDialogs.addAll(arrayList);
        updateRows();
        WidgetPreviewCell widgetPreviewCell = this.widgetPreviewCell;
        if (widgetPreviewCell != null) {
            widgetPreviewCell.updateDialogs();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(Context context, View view, int i) {
        if (i == this.selectChatsRow) {
            InviteMembersBottomSheet inviteMembersBottomSheet = new InviteMembersBottomSheet(context, this.currentAccount, null, 0L, this, null);
            inviteMembersBottomSheet.setDelegate(new InviteMembersBottomSheet.InviteMembersBottomSheetDelegate() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.Components.InviteMembersBottomSheet.InviteMembersBottomSheetDelegate
                public final void didSelectDialogs(ArrayList arrayList) {
                    EditWidgetActivity.this.lambda$createView$0(arrayList);
                }
            }, this.selectedDialogs);
            inviteMembersBottomSheet.setSelectedContacts(this.selectedDialogs);
            showDialog(inviteMembersBottomSheet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRows() {
        int size;
        this.previewRow = 0;
        this.rowCount = 2;
        this.selectChatsRow = 1;
        if (this.selectedDialogs.isEmpty()) {
            size = -1;
            this.chatsStartRow = -1;
        } else {
            int i = this.rowCount;
            this.chatsStartRow = i;
            size = i + this.selectedDialogs.size();
            this.rowCount = size;
        }
        this.chatsEndRow = size;
        int i2 = this.rowCount;
        this.rowCount = i2 + 1;
        this.infoRow = i2;
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(final Context context) {
        ActionBar actionBar;
        int i;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        if (this.widgetType == 0) {
            actionBar = this.actionBar;
            i = R.string.WidgetChats;
        } else {
            actionBar = this.actionBar;
            i = R.string.WidgetShortcuts;
        }
        actionBar.setTitle(LocaleController.getString(i));
        this.actionBar.createMenu().addItem(1, LocaleController.getString(R.string.Done).toUpperCase());
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.EditWidgetActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i2) {
                if (i2 == -1) {
                    if (EditWidgetActivity.this.delegate == null) {
                        EditWidgetActivity.this.finishActivity();
                        return;
                    } else {
                        EditWidgetActivity.this.lambda$onBackPressed$321();
                        return;
                    }
                }
                if (i2 != 1 || EditWidgetActivity.this.getParentActivity() == null) {
                    return;
                }
                ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                for (int i3 = 0; i3 < EditWidgetActivity.this.selectedDialogs.size(); i3++) {
                    arrayList.add(MessagesStorage.TopicKey.of(((Long) EditWidgetActivity.this.selectedDialogs.get(i3)).longValue(), 0L));
                }
                EditWidgetActivity.this.getMessagesStorage().putWidgetDialogs(EditWidgetActivity.this.currentWidgetId, arrayList);
                SharedPreferences.Editor edit = EditWidgetActivity.this.getParentActivity().getSharedPreferences("shortcut_widget", 0).edit();
                edit.putInt("account" + EditWidgetActivity.this.currentWidgetId, ((BaseFragment) EditWidgetActivity.this).currentAccount);
                edit.putInt("type" + EditWidgetActivity.this.currentWidgetId, EditWidgetActivity.this.widgetType);
                edit.commit();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(EditWidgetActivity.this.getParentActivity());
                if (EditWidgetActivity.this.widgetType == 0) {
                    ChatsWidgetProvider.updateWidget(EditWidgetActivity.this.getParentActivity(), appWidgetManager, EditWidgetActivity.this.currentWidgetId);
                } else {
                    ContactsWidgetProvider.updateWidget(EditWidgetActivity.this.getParentActivity(), appWidgetManager, EditWidgetActivity.this.currentWidgetId);
                }
                if (EditWidgetActivity.this.delegate != null) {
                    EditWidgetActivity.this.delegate.didSelectDialogs(EditWidgetActivity.this.selectedDialogs);
                } else {
                    EditWidgetActivity.this.finishActivity();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.fragmentView = frameLayout;
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
        this.itemTouchHelper = itemTouchHelper;
        itemTouchHelper.attachToRecyclerView(this.listView);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.EditWidgetActivity$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i2) {
                EditWidgetActivity.this.lambda$createView$1(context, view, i2);
            }
        });
        this.listView.setOnItemLongClickListener(new 2());
        return this.fragmentView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubmenuItemIcon));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        int i3 = Theme.key_windowBackgroundWhiteBlueText4;
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i3));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (this.delegate != null) {
            return super.onBackPressed();
        }
        finishActivity();
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        DialogsActivity.loadDialogs(AccountInstance.getInstance(this.currentAccount));
        getMediaDataController().loadHints(true);
        return super.onFragmentCreate();
    }

    public void setDelegate(EditWidgetActivityDelegate editWidgetActivityDelegate) {
        this.delegate = editWidgetActivityDelegate;
    }
}
