package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.Keep;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$MessageUserVote;
import org.telegram.tgnet.TLRPC$Poll;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageUserVoteInputOption;
import org.telegram.tgnet.TLRPC$TL_messages_getPollVotes;
import org.telegram.tgnet.TLRPC$TL_messages_votesList;
import org.telegram.tgnet.TLRPC$TL_pollAnswer;
import org.telegram.tgnet.TLRPC$TL_pollAnswerVoters;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public class PollVotesAlert extends BottomSheet {
    public static final Property<UserCell, Float> USER_CELL_PROPERTY = new AnonymousClass1("placeholderAlpha");
    private ActionBar actionBar;
    private AnimatorSet actionBarAnimation;
    private View actionBarShadow;
    private ChatActivity chatActivity;
    private float gradientWidth;
    private Adapter listAdapter;
    private RecyclerListView listView;
    private MessageObject messageObject;
    private TLRPC$InputPeer peer;
    private LinearGradient placeholderGradient;
    private Matrix placeholderMatrix;
    private TLRPC$Poll poll;
    private int scrollOffsetY;
    private Drawable shadowDrawable;
    private TextView titleTextView;
    private float totalTranslation;
    private HashSet<VotesList> loadingMore = new HashSet<>();
    private HashMap<VotesList, Button> votesPercents = new HashMap<>();
    private ArrayList<VotesList> voters = new ArrayList<>();
    private ArrayList<Integer> queries = new ArrayList<>();
    private Paint placeholderPaint = new Paint(1);
    private boolean loadingResults = true;
    private RectF rect = new RectF();

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    static /* synthetic */ float access$3016(PollVotesAlert pollVotesAlert, float f) {
        float f2 = pollVotesAlert.totalTranslation + f;
        pollVotesAlert.totalTranslation = f2;
        return f2;
    }

    static /* synthetic */ float access$3024(PollVotesAlert pollVotesAlert, float f) {
        float f2 = pollVotesAlert.totalTranslation - f;
        pollVotesAlert.totalTranslation = f2;
        return f2;
    }

    /* loaded from: classes3.dex */
    public static class VotesList {
        public boolean collapsed;
        public int collapsedCount = 10;
        public int count;
        public String next_offset;
        public byte[] option;
        public ArrayList<TLRPC$User> users;
        public ArrayList<TLRPC$MessageUserVote> votes;

        public VotesList(TLRPC$TL_messages_votesList tLRPC$TL_messages_votesList, byte[] bArr) {
            this.count = tLRPC$TL_messages_votesList.count;
            this.votes = tLRPC$TL_messages_votesList.votes;
            this.users = tLRPC$TL_messages_votesList.users;
            this.next_offset = tLRPC$TL_messages_votesList.next_offset;
            this.option = bArr;
        }

        public int getCount() {
            if (this.collapsed) {
                return Math.min(this.collapsedCount, this.votes.size());
            }
            return this.votes.size();
        }

        public int getCollapsed() {
            if (this.votes.size() <= 15) {
                return 0;
            }
            return this.collapsed ? 1 : 2;
        }
    }

    /* loaded from: classes3.dex */
    public class SectionCell extends FrameLayout {
        private TextView middleTextView;
        private TextView righTextView;
        private TextView textView;

        protected void onCollapseClick() {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SectionCell(Context context) {
            super(context);
            PollVotesAlert.this = r18;
            setBackgroundColor(Theme.getColor("graySection"));
            TextView textView = new TextView(getContext());
            this.textView = textView;
            textView.setTextSize(1, 14.0f);
            this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.textView.setTextColor(Theme.getColor("key_graySectionText"));
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            int i = 5;
            int i2 = 16;
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            TextView textView2 = new TextView(getContext());
            this.middleTextView = textView2;
            textView2.setTextSize(1, 14.0f);
            this.middleTextView.setTextColor(Theme.getColor("key_graySectionText"));
            this.middleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(getContext(), r18);
            this.righTextView = anonymousClass1;
            anonymousClass1.setTextSize(1, 14.0f);
            this.righTextView.setTextColor(Theme.getColor("key_graySectionText"));
            this.righTextView.setGravity((LocaleController.isRTL ? 3 : 5) | 16);
            this.righTextView.setOnClickListener(new PollVotesAlert$SectionCell$$ExternalSyntheticLambda0(this));
            TextView textView3 = this.textView;
            boolean z = LocaleController.isRTL;
            addView(textView3, LayoutHelper.createFrame(-2, -1.0f, (z ? 5 : 3) | 48, z ? 0 : 16, 0.0f, !z ? 0 : i2, 0.0f));
            addView(this.middleTextView, LayoutHelper.createFrame(-2, -1.0f, (LocaleController.isRTL ? 5 : 3) | 48, 0.0f, 0.0f, 0.0f, 0.0f));
            addView(this.righTextView, LayoutHelper.createFrame(-2, -1.0f, (LocaleController.isRTL ? 3 : i) | 48, 16.0f, 0.0f, 16.0f, 0.0f));
        }

        /* renamed from: org.telegram.ui.Components.PollVotesAlert$SectionCell$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends TextView {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, PollVotesAlert pollVotesAlert) {
                super(context);
                SectionCell.this = r1;
            }

            @Override // android.view.View
            public boolean post(Runnable runnable) {
                return ((BottomSheet) PollVotesAlert.this).containerView.post(runnable);
            }

            @Override // android.view.View
            public boolean postDelayed(Runnable runnable, long j) {
                return ((BottomSheet) PollVotesAlert.this).containerView.postDelayed(runnable, j);
            }
        }

        public /* synthetic */ void lambda$new$0(View view) {
            onCollapseClick();
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824);
            measureChildWithMargins(this.middleTextView, i, 0, makeMeasureSpec, 0);
            measureChildWithMargins(this.righTextView, i, 0, makeMeasureSpec, 0);
            measureChildWithMargins(this.textView, i, this.middleTextView.getMeasuredWidth() + this.righTextView.getMeasuredWidth() + AndroidUtilities.dp(32.0f), makeMeasureSpec, 0);
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(32.0f));
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (LocaleController.isRTL) {
                int left = this.textView.getLeft() - this.middleTextView.getMeasuredWidth();
                TextView textView = this.middleTextView;
                textView.layout(left, textView.getTop(), this.middleTextView.getMeasuredWidth() + left, this.middleTextView.getBottom());
                return;
            }
            int right = this.textView.getRight();
            TextView textView2 = this.middleTextView;
            textView2.layout(right, textView2.getTop(), this.middleTextView.getMeasuredWidth() + right, this.middleTextView.getBottom());
        }

        public void setText(String str, int i, int i2, int i3) {
            SpannableStringBuilder spannableStringBuilder;
            TextView textView = this.textView;
            textView.setText(Emoji.replaceEmoji(str, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
            String format = String.format("%d", Integer.valueOf(i));
            if (LocaleController.isRTL) {
                spannableStringBuilder = new SpannableStringBuilder(String.format("%s%% – ", Integer.valueOf(i)));
            } else {
                spannableStringBuilder = new SpannableStringBuilder(String.format(" – %s%%", Integer.valueOf(i)));
            }
            spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 3, format.length() + 3, 33);
            this.middleTextView.setText(spannableStringBuilder);
            if (i3 == 0) {
                if (PollVotesAlert.this.poll.quiz) {
                    this.righTextView.setText(LocaleController.formatPluralString("Answer", i2, new Object[0]));
                } else {
                    this.righTextView.setText(LocaleController.formatPluralString("Vote", i2, new Object[0]));
                }
            } else if (i3 == 1) {
                this.righTextView.setText(LocaleController.getString("PollExpand", 2131627633));
            } else {
                this.righTextView.setText(LocaleController.getString("PollCollapse", 2131627632));
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.PollVotesAlert$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends AnimationProperties.FloatProperty<UserCell> {
        AnonymousClass1(String str) {
            super(str);
        }

        public void setValue(UserCell userCell, float f) {
            userCell.setPlaceholderAlpha(f);
        }

        public Float get(UserCell userCell) {
            return Float.valueOf(userCell.getPlaceholderAlpha());
        }
    }

    /* loaded from: classes3.dex */
    public class UserCell extends FrameLayout {
        private ArrayList<Animator> animators;
        private BackupImageView avatarImageView;
        private TLRPC$User currentUser;
        private boolean drawPlaceholder;
        private TLRPC$FileLocation lastAvatar;
        private String lastName;
        private int lastStatus;
        private SimpleTextView nameTextView;
        private boolean needDivider;
        private int placeholderNum;
        private float placeholderAlpha = 1.0f;
        private AvatarDrawable avatarDrawable = new AvatarDrawable();

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public UserCell(Context context) {
            super(context);
            PollVotesAlert.this = r12;
            int i = UserConfig.selectedAccount;
            setWillNotDraw(false);
            BackupImageView backupImageView = new BackupImageView(context);
            this.avatarImageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(18.0f));
            BackupImageView backupImageView2 = this.avatarImageView;
            boolean z = LocaleController.isRTL;
            int i2 = 5;
            addView(backupImageView2, LayoutHelper.createFrame(36, 36.0f, (z ? 5 : 3) | 48, z ? 0.0f : 14.0f, 6.0f, z ? 14.0f : 0.0f, 0.0f));
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.nameTextView = simpleTextView;
            simpleTextView.setTextColor(Theme.getColor("dialogTextBlack"));
            this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.nameTextView.setTextSize(16);
            this.nameTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
            SimpleTextView simpleTextView2 = this.nameTextView;
            boolean z2 = LocaleController.isRTL;
            addView(simpleTextView2, LayoutHelper.createFrame(-1, 20.0f, (!z2 ? 3 : i2) | 48, z2 ? 28.0f : 65.0f, 14.0f, z2 ? 65.0f : 28.0f, 0.0f));
        }

        public void setData(TLRPC$User tLRPC$User, int i, boolean z) {
            this.currentUser = tLRPC$User;
            this.needDivider = z;
            this.drawPlaceholder = tLRPC$User == null;
            this.placeholderNum = i;
            if (tLRPC$User == null) {
                this.nameTextView.setText("");
                this.avatarImageView.setImageDrawable(null);
            } else {
                update(0);
            }
            ArrayList<Animator> arrayList = this.animators;
            if (arrayList != null) {
                arrayList.add(ObjectAnimator.ofFloat(this.avatarImageView, View.ALPHA, 0.0f, 1.0f));
                this.animators.add(ObjectAnimator.ofFloat(this.nameTextView, View.ALPHA, 0.0f, 1.0f));
                this.animators.add(ObjectAnimator.ofFloat(this, PollVotesAlert.USER_CELL_PROPERTY, 1.0f, 0.0f));
            } else if (this.drawPlaceholder) {
            } else {
                this.placeholderAlpha = 0.0f;
            }
        }

        @Keep
        public void setPlaceholderAlpha(float f) {
            this.placeholderAlpha = f;
            invalidate();
        }

        @Keep
        public float getPlaceholderAlpha() {
            return this.placeholderAlpha;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f) + (this.needDivider ? 1 : 0), 1073741824));
        }

        /* JADX WARN: Code restructure failed: missing block: B:44:0x0062, code lost:
            if (r1.equals(r11.lastName) == false) goto L46;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void update(int i) {
            TLRPC$FileLocation tLRPC$FileLocation;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
            TLRPC$User tLRPC$User = this.currentUser;
            String str = null;
            TLRPC$FileLocation tLRPC$FileLocation2 = (tLRPC$User == null || (tLRPC$UserProfilePhoto = tLRPC$User.photo) == null) ? null : tLRPC$UserProfilePhoto.photo_small;
            if (i != 0) {
                boolean z = true;
                boolean z2 = (MessagesController.UPDATE_MASK_AVATAR & i) != 0 && (((tLRPC$FileLocation = this.lastAvatar) != null && tLRPC$FileLocation2 == null) || ((tLRPC$FileLocation == null && tLRPC$FileLocation2 != null) || !(tLRPC$FileLocation == null || tLRPC$FileLocation2 == null || (tLRPC$FileLocation.volume_id == tLRPC$FileLocation2.volume_id && tLRPC$FileLocation.local_id == tLRPC$FileLocation2.local_id))));
                if (tLRPC$User != null && !z2 && (MessagesController.UPDATE_MASK_STATUS & i) != 0) {
                    TLRPC$UserStatus tLRPC$UserStatus = tLRPC$User.status;
                    if ((tLRPC$UserStatus != null ? tLRPC$UserStatus.expires : 0) != this.lastStatus) {
                        z2 = true;
                    }
                }
                if (!z2 && this.lastName != null && (i & MessagesController.UPDATE_MASK_NAME) != 0) {
                    if (tLRPC$User != null) {
                        str = UserObject.getUserName(tLRPC$User);
                    }
                }
                z = z2;
                if (!z) {
                    return;
                }
            }
            this.avatarDrawable.setInfo(this.currentUser);
            TLRPC$User tLRPC$User2 = this.currentUser;
            TLRPC$UserStatus tLRPC$UserStatus2 = tLRPC$User2.status;
            if (tLRPC$UserStatus2 != null) {
                this.lastStatus = tLRPC$UserStatus2.expires;
            } else {
                this.lastStatus = 0;
            }
            if (tLRPC$User2 != null) {
                if (str == null) {
                    str = UserObject.getUserName(tLRPC$User2);
                }
                this.lastName = str;
            } else {
                this.lastName = "";
            }
            this.nameTextView.setText(this.lastName);
            this.lastAvatar = tLRPC$FileLocation2;
            TLRPC$User tLRPC$User3 = this.currentUser;
            if (tLRPC$User3 != null) {
                this.avatarImageView.setForUserOrChat(tLRPC$User3, this.avatarDrawable);
            } else {
                this.avatarImageView.setImageDrawable(this.avatarDrawable);
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int i;
            int i2;
            int i3;
            int i4;
            if (this.drawPlaceholder || this.placeholderAlpha != 0.0f) {
                PollVotesAlert.this.placeholderPaint.setAlpha((int) (this.placeholderAlpha * 255.0f));
                int left = this.avatarImageView.getLeft() + (this.avatarImageView.getMeasuredWidth() / 2);
                int top = this.avatarImageView.getTop() + (this.avatarImageView.getMeasuredHeight() / 2);
                canvas.drawCircle(left, top, this.avatarImageView.getMeasuredWidth() / 2, PollVotesAlert.this.placeholderPaint);
                if (this.placeholderNum % 2 == 0) {
                    i2 = AndroidUtilities.dp(65.0f);
                    i = AndroidUtilities.dp(48.0f);
                } else {
                    i2 = AndroidUtilities.dp(65.0f);
                    i = AndroidUtilities.dp(60.0f);
                }
                if (LocaleController.isRTL) {
                    i2 = (getMeasuredWidth() - i2) - i;
                }
                PollVotesAlert.this.rect.set(i2, top - AndroidUtilities.dp(4.0f), i2 + i, AndroidUtilities.dp(4.0f) + top);
                canvas.drawRoundRect(PollVotesAlert.this.rect, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), PollVotesAlert.this.placeholderPaint);
                if (this.placeholderNum % 2 == 0) {
                    i4 = AndroidUtilities.dp(119.0f);
                    i3 = AndroidUtilities.dp(60.0f);
                } else {
                    i4 = AndroidUtilities.dp(131.0f);
                    i3 = AndroidUtilities.dp(80.0f);
                }
                if (LocaleController.isRTL) {
                    i4 = (getMeasuredWidth() - i4) - i3;
                }
                PollVotesAlert.this.rect.set(i4, top - AndroidUtilities.dp(4.0f), i4 + i3, top + AndroidUtilities.dp(4.0f));
                canvas.drawRoundRect(PollVotesAlert.this.rect, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), PollVotesAlert.this.placeholderPaint);
            }
            if (this.needDivider) {
                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(64.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(64.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
    }

    public static void showForPoll(ChatActivity chatActivity, MessageObject messageObject) {
        if (chatActivity == null || chatActivity.getParentActivity() == null) {
            return;
        }
        chatActivity.showDialog(new PollVotesAlert(chatActivity, messageObject));
    }

    /* loaded from: classes3.dex */
    public static class Button {
        private float decimal;
        private int percent;
        private int votesCount;

        private Button() {
        }

        /* synthetic */ Button(AnonymousClass1 anonymousClass1) {
            this();
        }

        static /* synthetic */ float access$3924(Button button, float f) {
            float f2 = button.decimal - f;
            button.decimal = f2;
            return f2;
        }

        static /* synthetic */ int access$4012(Button button, int i) {
            int i2 = button.percent + i;
            button.percent = i2;
            return i2;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PollVotesAlert(ChatActivity chatActivity, MessageObject messageObject) {
        super(chatActivity.getParentActivity(), true);
        int i;
        int i2;
        int i3 = 1;
        fixNavigationBar();
        this.messageObject = messageObject;
        this.chatActivity = chatActivity;
        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) messageObject.messageOwner.media;
        this.poll = tLRPC$TL_messageMediaPoll.poll;
        Activity parentActivity = chatActivity.getParentActivity();
        this.peer = chatActivity.getMessagesController().getInputPeer((int) messageObject.getDialogId());
        ArrayList arrayList = new ArrayList();
        int size = tLRPC$TL_messageMediaPoll.results.results.size();
        Integer[] numArr = new Integer[size];
        int i4 = 0;
        while (i4 < size) {
            TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters = tLRPC$TL_messageMediaPoll.results.results.get(i4);
            if (tLRPC$TL_pollAnswerVoters.voters == 0) {
                i2 = i4;
                i = size;
            } else {
                TLRPC$TL_messages_votesList tLRPC$TL_messages_votesList = new TLRPC$TL_messages_votesList();
                int i5 = tLRPC$TL_pollAnswerVoters.voters;
                i5 = i5 > 15 ? 10 : i5;
                for (int i6 = 0; i6 < i5; i6++) {
                    tLRPC$TL_messages_votesList.votes.add(new TLRPC$TL_messageUserVoteInputOption());
                }
                int i7 = tLRPC$TL_pollAnswerVoters.voters;
                tLRPC$TL_messages_votesList.next_offset = i5 < i7 ? "empty" : null;
                tLRPC$TL_messages_votesList.count = i7;
                this.voters.add(new VotesList(tLRPC$TL_messages_votesList, tLRPC$TL_pollAnswerVoters.option));
                TLRPC$TL_messages_getPollVotes tLRPC$TL_messages_getPollVotes = new TLRPC$TL_messages_getPollVotes();
                tLRPC$TL_messages_getPollVotes.peer = this.peer;
                tLRPC$TL_messages_getPollVotes.id = this.messageObject.getId();
                tLRPC$TL_messages_getPollVotes.limit = tLRPC$TL_pollAnswerVoters.voters <= 15 ? 15 : 10;
                tLRPC$TL_messages_getPollVotes.flags |= i3;
                tLRPC$TL_messages_getPollVotes.option = tLRPC$TL_pollAnswerVoters.option;
                i = size;
                i2 = i4;
                numArr[i2] = Integer.valueOf(chatActivity.getConnectionsManager().sendRequest(tLRPC$TL_messages_getPollVotes, new PollVotesAlert$$ExternalSyntheticLambda4(this, numArr, i4, chatActivity, arrayList, tLRPC$TL_pollAnswerVoters)));
                this.queries.add(numArr[i2]);
            }
            i4 = i2 + 1;
            size = i;
            i3 = 1;
        }
        updateButtons();
        Collections.sort(this.voters, new AnonymousClass2());
        updatePlaceholder();
        Drawable mutate = parentActivity.getResources().getDrawable(2131166140).mutate();
        this.shadowDrawable = mutate;
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogBackground"), PorterDuff.Mode.MULTIPLY));
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(parentActivity);
        this.containerView = anonymousClass3;
        anonymousClass3.setWillNotDraw(false);
        ViewGroup viewGroup = this.containerView;
        int i8 = this.backgroundPaddingLeft;
        viewGroup.setPadding(i8, 0, i8, 0);
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(parentActivity);
        this.listView = anonymousClass4;
        anonymousClass4.setClipToPadding(false);
        this.listView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setSectionsType(2);
        this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        RecyclerListView recyclerListView = this.listView;
        Adapter adapter = new Adapter(parentActivity);
        this.listAdapter = adapter;
        recyclerListView.setAdapter(adapter);
        this.listView.setGlowColor(Theme.getColor("dialogScrollGlow"));
        this.listView.setOnItemClickListener(new PollVotesAlert$$ExternalSyntheticLambda6(this, chatActivity));
        this.listView.setOnScrollListener(new AnonymousClass5());
        TextView textView = new TextView(parentActivity);
        this.titleTextView = textView;
        textView.setTextSize(1, 18.0f);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(14.0f), AndroidUtilities.dp(21.0f));
        this.titleTextView.setTextColor(Theme.getColor("dialogTextBlack"));
        this.titleTextView.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        TextView textView2 = this.titleTextView;
        textView2.setText(Emoji.replaceEmoji(this.poll.question, textView2.getPaint().getFontMetricsInt(), AndroidUtilities.dp(18.0f), false));
        AnonymousClass6 anonymousClass6 = new AnonymousClass6(parentActivity);
        this.actionBar = anonymousClass6;
        anonymousClass6.setBackgroundColor(Theme.getColor("dialogBackground"));
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setItemsColor(Theme.getColor("dialogTextBlack"), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor("dialogButtonSelector"), false);
        this.actionBar.setTitleColor(Theme.getColor("dialogTextBlack"));
        this.actionBar.setSubtitleColor(Theme.getColor("player_actionBarSubtitle"));
        this.actionBar.setOccupyStatusBar(false);
        this.actionBar.setAlpha(0.0f);
        this.actionBar.setTitle(LocaleController.getString("PollResults", 2131627637));
        if (this.poll.quiz) {
            this.actionBar.setSubtitle(LocaleController.formatPluralString("Answer", tLRPC$TL_messageMediaPoll.results.total_voters, new Object[0]));
        } else {
            this.actionBar.setSubtitle(LocaleController.formatPluralString("Vote", tLRPC$TL_messageMediaPoll.results.total_voters, new Object[0]));
        }
        this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass7());
        View view = new View(parentActivity);
        this.actionBarShadow = view;
        view.setAlpha(0.0f);
        this.actionBarShadow.setBackgroundColor(Theme.getColor("dialogShadowLine"));
        this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 1.0f));
    }

    public /* synthetic */ void lambda$new$1(Integer[] numArr, int i, ChatActivity chatActivity, ArrayList arrayList, TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PollVotesAlert$$ExternalSyntheticLambda1(this, numArr, i, tLObject, chatActivity, arrayList, tLRPC$TL_pollAnswerVoters));
    }

    public /* synthetic */ void lambda$new$0(Integer[] numArr, int i, TLObject tLObject, ChatActivity chatActivity, ArrayList arrayList, TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters) {
        RecyclerView.ViewHolder findContainingViewHolder;
        this.queries.remove(numArr[i]);
        if (tLObject != null) {
            TLRPC$TL_messages_votesList tLRPC$TL_messages_votesList = (TLRPC$TL_messages_votesList) tLObject;
            chatActivity.getMessagesController().putUsers(tLRPC$TL_messages_votesList.users, false);
            if (!tLRPC$TL_messages_votesList.votes.isEmpty()) {
                arrayList.add(new VotesList(tLRPC$TL_messages_votesList, tLRPC$TL_pollAnswerVoters.option));
            }
            if (!this.queries.isEmpty()) {
                return;
            }
            int size = arrayList.size();
            boolean z = false;
            for (int i2 = 0; i2 < size; i2++) {
                VotesList votesList = (VotesList) arrayList.get(i2);
                int size2 = this.voters.size();
                int i3 = 0;
                while (true) {
                    if (i3 < size2) {
                        VotesList votesList2 = this.voters.get(i3);
                        if (Arrays.equals(votesList.option, votesList2.option)) {
                            votesList2.next_offset = votesList.next_offset;
                            if (votesList2.count != votesList.count || votesList2.votes.size() != votesList.votes.size()) {
                                z = true;
                            }
                            votesList2.count = votesList.count;
                            votesList2.users = votesList.users;
                            votesList2.votes = votesList.votes;
                        } else {
                            i3++;
                        }
                    }
                }
            }
            this.loadingResults = false;
            RecyclerListView recyclerListView = this.listView;
            if (recyclerListView == null) {
                return;
            }
            if (this.currentSheetAnimationType != 0 || this.startAnimationRunnable != null || z) {
                if (z) {
                    updateButtons();
                }
                this.listAdapter.notifyDataSetChanged();
                return;
            }
            int childCount = recyclerListView.getChildCount();
            ArrayList arrayList2 = new ArrayList();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = this.listView.getChildAt(i4);
                if ((childAt instanceof UserCell) && (findContainingViewHolder = this.listView.findContainingViewHolder(childAt)) != null) {
                    UserCell userCell = (UserCell) childAt;
                    userCell.animators = arrayList2;
                    userCell.setEnabled(true);
                    this.listAdapter.onViewAttachedToWindow(findContainingViewHolder);
                    userCell.animators = null;
                }
            }
            if (!arrayList2.isEmpty()) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList2);
                animatorSet.setDuration(180L);
                animatorSet.start();
            }
            this.loadingResults = false;
            return;
        }
        dismiss();
    }

    /* renamed from: org.telegram.ui.Components.PollVotesAlert$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements Comparator<VotesList> {
        AnonymousClass2() {
            PollVotesAlert.this = r1;
        }

        private int getIndex(VotesList votesList) {
            int size = PollVotesAlert.this.poll.answers.size();
            for (int i = 0; i < size; i++) {
                if (Arrays.equals(PollVotesAlert.this.poll.answers.get(i).option, votesList.option)) {
                    return i;
                }
            }
            return 0;
        }

        public int compare(VotesList votesList, VotesList votesList2) {
            int index = getIndex(votesList);
            int index2 = getIndex(votesList2);
            if (index > index2) {
                return 1;
            }
            return index < index2 ? -1 : 0;
        }
    }

    /* renamed from: org.telegram.ui.Components.PollVotesAlert$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends FrameLayout {
        private boolean ignoreLayout = false;
        private RectF rect = new RectF();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            PollVotesAlert.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i2);
            if (Build.VERSION.SDK_INT >= 21 && !((BottomSheet) PollVotesAlert.this).isFullscreen) {
                this.ignoreLayout = true;
                setPadding(((BottomSheet) PollVotesAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) PollVotesAlert.this).backgroundPaddingLeft, 0);
                this.ignoreLayout = false;
            }
            int paddingTop = size - getPaddingTop();
            ((FrameLayout.LayoutParams) PollVotesAlert.this.listView.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
            ((FrameLayout.LayoutParams) PollVotesAlert.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
            int dp = ((BottomSheet) PollVotesAlert.this).backgroundPaddingTop + AndroidUtilities.dp(15.0f) + AndroidUtilities.statusBarHeight;
            int sectionCount = PollVotesAlert.this.listAdapter.getSectionCount();
            for (int i3 = 0; i3 < sectionCount; i3++) {
                if (i3 == 0) {
                    PollVotesAlert.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i - (((BottomSheet) PollVotesAlert.this).backgroundPaddingLeft * 2)), 1073741824), i2);
                    dp += PollVotesAlert.this.titleTextView.getMeasuredHeight();
                } else {
                    dp += AndroidUtilities.dp(32.0f) + (AndroidUtilities.dp(50.0f) * (PollVotesAlert.this.listAdapter.getCountForSection(i3) - 1));
                }
            }
            int dp2 = (dp < paddingTop ? paddingTop - dp : paddingTop - ((paddingTop / 5) * 3)) + AndroidUtilities.dp(8.0f);
            if (PollVotesAlert.this.listView.getPaddingTop() != dp2) {
                this.ignoreLayout = true;
                PollVotesAlert.this.listView.setPinnedSectionOffsetY(-dp2);
                PollVotesAlert.this.listView.setPadding(0, dp2, 0, 0);
                this.ignoreLayout = false;
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            PollVotesAlert.this.updateLayout(false);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && PollVotesAlert.this.scrollOffsetY != 0 && motionEvent.getY() < PollVotesAlert.this.scrollOffsetY + AndroidUtilities.dp(12.0f) && PollVotesAlert.this.actionBar.getAlpha() == 0.0f) {
                PollVotesAlert.this.dismiss();
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return !PollVotesAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            int dp = AndroidUtilities.dp(13.0f);
            int i = (PollVotesAlert.this.scrollOffsetY - ((BottomSheet) PollVotesAlert.this).backgroundPaddingTop) - dp;
            if (((BottomSheet) PollVotesAlert.this).currentSheetAnimationType == 1) {
                i = (int) (i + PollVotesAlert.this.listView.getTranslationY());
            }
            int dp2 = AndroidUtilities.dp(20.0f) + i;
            int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(15.0f) + ((BottomSheet) PollVotesAlert.this).backgroundPaddingTop;
            if (((BottomSheet) PollVotesAlert.this).backgroundPaddingTop + i < ActionBar.getCurrentActionBarHeight()) {
                float dp3 = dp + AndroidUtilities.dp(4.0f);
                float min = Math.min(1.0f, ((ActionBar.getCurrentActionBarHeight() - i) - ((BottomSheet) PollVotesAlert.this).backgroundPaddingTop) / dp3);
                int currentActionBarHeight = (int) ((ActionBar.getCurrentActionBarHeight() - dp3) * min);
                i -= currentActionBarHeight;
                dp2 -= currentActionBarHeight;
                measuredHeight += currentActionBarHeight;
                f = 1.0f - min;
            } else {
                f = 1.0f;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                int i2 = AndroidUtilities.statusBarHeight;
                i += i2;
                dp2 += i2;
            }
            PollVotesAlert.this.shadowDrawable.setBounds(0, i, getMeasuredWidth(), measuredHeight);
            PollVotesAlert.this.shadowDrawable.draw(canvas);
            if (f != 1.0f) {
                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("dialogBackground"));
                this.rect.set(((BottomSheet) PollVotesAlert.this).backgroundPaddingLeft, ((BottomSheet) PollVotesAlert.this).backgroundPaddingTop + i, getMeasuredWidth() - ((BottomSheet) PollVotesAlert.this).backgroundPaddingLeft, ((BottomSheet) PollVotesAlert.this).backgroundPaddingTop + i + AndroidUtilities.dp(24.0f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
            }
            if (f != 0.0f) {
                int dp4 = AndroidUtilities.dp(36.0f);
                this.rect.set((getMeasuredWidth() - dp4) / 2, dp2, (getMeasuredWidth() + dp4) / 2, dp2 + AndroidUtilities.dp(4.0f));
                int color = Theme.getColor("key_sheet_scrollUp");
                int alpha = Color.alpha(color);
                Theme.dialogs_onlineCirclePaint.setColor(color);
                Theme.dialogs_onlineCirclePaint.setAlpha((int) (alpha * 1.0f * f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
            }
            int color2 = Theme.getColor("dialogBackground");
            Theme.dialogs_onlineCirclePaint.setColor(Color.argb((int) (PollVotesAlert.this.actionBar.getAlpha() * 255.0f), (int) (Color.red(color2) * 0.8f), (int) (Color.green(color2) * 0.8f), (int) (Color.blue(color2) * 0.8f)));
            canvas.drawRect(((BottomSheet) PollVotesAlert.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) PollVotesAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
        }
    }

    /* renamed from: org.telegram.ui.Components.PollVotesAlert$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends RecyclerListView {
        long lastUpdateTime;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context) {
            super(context);
            PollVotesAlert.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        protected boolean allowSelectChildAtPosition(float f, float f2) {
            return f2 >= ((float) (PollVotesAlert.this.scrollOffsetY + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)));
        }

        @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            if (PollVotesAlert.this.loadingResults) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long abs = Math.abs(this.lastUpdateTime - elapsedRealtime);
                if (abs > 17) {
                    abs = 16;
                }
                this.lastUpdateTime = elapsedRealtime;
                PollVotesAlert pollVotesAlert = PollVotesAlert.this;
                PollVotesAlert.access$3016(pollVotesAlert, (((float) abs) * pollVotesAlert.gradientWidth) / 1800.0f);
                while (PollVotesAlert.this.totalTranslation >= PollVotesAlert.this.gradientWidth * 2.0f) {
                    PollVotesAlert pollVotesAlert2 = PollVotesAlert.this;
                    PollVotesAlert.access$3024(pollVotesAlert2, pollVotesAlert2.gradientWidth * 2.0f);
                }
                PollVotesAlert.this.placeholderMatrix.setTranslate(PollVotesAlert.this.totalTranslation, 0.0f);
                PollVotesAlert.this.placeholderGradient.setLocalMatrix(PollVotesAlert.this.placeholderMatrix);
                invalidateViews();
                invalidate();
            }
            super.dispatchDraw(canvas);
        }
    }

    public /* synthetic */ void lambda$new$4(ChatActivity chatActivity, View view, int i) {
        if (chatActivity == null || chatActivity.getParentActivity() == null) {
            return;
        }
        ArrayList<Integer> arrayList = this.queries;
        if (arrayList != null && !arrayList.isEmpty()) {
            return;
        }
        int i2 = 0;
        if (view instanceof TextCell) {
            int sectionForPosition = this.listAdapter.getSectionForPosition(i) - 1;
            int positionInSectionForPosition = this.listAdapter.getPositionInSectionForPosition(i) - 1;
            if (positionInSectionForPosition <= 0 || sectionForPosition < 0) {
                return;
            }
            VotesList votesList = this.voters.get(sectionForPosition);
            if (positionInSectionForPosition != votesList.getCount() || this.loadingMore.contains(votesList)) {
                return;
            }
            if (votesList.collapsed && votesList.collapsedCount < votesList.votes.size()) {
                int min = Math.min(votesList.collapsedCount + 50, votesList.votes.size());
                votesList.collapsedCount = min;
                if (min == votesList.votes.size()) {
                    votesList.collapsed = false;
                }
                this.listAdapter.notifyDataSetChanged();
                return;
            }
            this.loadingMore.add(votesList);
            TLRPC$TL_messages_getPollVotes tLRPC$TL_messages_getPollVotes = new TLRPC$TL_messages_getPollVotes();
            tLRPC$TL_messages_getPollVotes.peer = this.peer;
            tLRPC$TL_messages_getPollVotes.id = this.messageObject.getId();
            tLRPC$TL_messages_getPollVotes.limit = 50;
            int i3 = tLRPC$TL_messages_getPollVotes.flags | 1;
            tLRPC$TL_messages_getPollVotes.flags = i3;
            tLRPC$TL_messages_getPollVotes.option = votesList.option;
            tLRPC$TL_messages_getPollVotes.flags = i3 | 2;
            tLRPC$TL_messages_getPollVotes.offset = votesList.next_offset;
            this.chatActivity.getConnectionsManager().sendRequest(tLRPC$TL_messages_getPollVotes, new PollVotesAlert$$ExternalSyntheticLambda3(this, votesList, chatActivity));
        } else if (!(view instanceof UserCell)) {
        } else {
            UserCell userCell = (UserCell) view;
            if (userCell.currentUser == null) {
                return;
            }
            TLRPC$User currentUser = chatActivity.getCurrentUser();
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", userCell.currentUser.id);
            dismiss();
            ProfileActivity profileActivity = new ProfileActivity(bundle);
            if (currentUser != null && currentUser.id == userCell.currentUser.id) {
                i2 = 1;
            }
            profileActivity.setPlayProfileAnimation(i2);
            chatActivity.presentFragment(profileActivity);
        }
    }

    public /* synthetic */ void lambda$new$3(VotesList votesList, ChatActivity chatActivity, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new PollVotesAlert$$ExternalSyntheticLambda0(this, votesList, tLObject, chatActivity));
    }

    public /* synthetic */ void lambda$new$2(VotesList votesList, TLObject tLObject, ChatActivity chatActivity) {
        if (!isShowing()) {
            return;
        }
        this.loadingMore.remove(votesList);
        if (tLObject == null) {
            return;
        }
        TLRPC$TL_messages_votesList tLRPC$TL_messages_votesList = (TLRPC$TL_messages_votesList) tLObject;
        chatActivity.getMessagesController().putUsers(tLRPC$TL_messages_votesList.users, false);
        votesList.votes.addAll(tLRPC$TL_messages_votesList.votes);
        votesList.next_offset = tLRPC$TL_messages_votesList.next_offset;
        this.listAdapter.notifyDataSetChanged();
    }

    /* renamed from: org.telegram.ui.Components.PollVotesAlert$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends RecyclerView.OnScrollListener {
        AnonymousClass5() {
            PollVotesAlert.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (PollVotesAlert.this.listView.getChildCount() <= 0) {
                return;
            }
            PollVotesAlert.this.updateLayout(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 0) {
                if (((PollVotesAlert.this.scrollOffsetY - ((BottomSheet) PollVotesAlert.this).backgroundPaddingTop) - AndroidUtilities.dp(13.0f)) + ((BottomSheet) PollVotesAlert.this).backgroundPaddingTop >= ActionBar.getCurrentActionBarHeight() || !PollVotesAlert.this.listView.canScrollVertically(1)) {
                    return;
                }
                PollVotesAlert.this.listView.getChildAt(0);
                RecyclerListView.Holder holder = (RecyclerListView.Holder) PollVotesAlert.this.listView.findViewHolderForAdapterPosition(0);
                if (holder == null || holder.itemView.getTop() <= AndroidUtilities.dp(7.0f)) {
                    return;
                }
                PollVotesAlert.this.listView.smoothScrollBy(0, holder.itemView.getTop() - AndroidUtilities.dp(7.0f));
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.PollVotesAlert$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends ActionBar {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            PollVotesAlert.this = r1;
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
            ((BottomSheet) PollVotesAlert.this).containerView.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.Components.PollVotesAlert$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass7() {
            PollVotesAlert.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                PollVotesAlert.this.dismiss();
            }
        }
    }

    private void updateButtons() {
        this.votesPercents.clear();
        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) this.messageObject.messageOwner.media;
        ArrayList arrayList = new ArrayList();
        int size = this.voters.size();
        int i = 100;
        boolean z = false;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            VotesList votesList = this.voters.get(i4);
            Button button = new Button(null);
            arrayList.add(button);
            this.votesPercents.put(votesList, button);
            if (!tLRPC$TL_messageMediaPoll.results.results.isEmpty()) {
                int size2 = tLRPC$TL_messageMediaPoll.results.results.size();
                int i5 = 0;
                while (true) {
                    if (i5 < size2) {
                        TLRPC$TL_pollAnswerVoters tLRPC$TL_pollAnswerVoters = tLRPC$TL_messageMediaPoll.results.results.get(i5);
                        if (Arrays.equals(votesList.option, tLRPC$TL_pollAnswerVoters.option)) {
                            button.votesCount = tLRPC$TL_pollAnswerVoters.voters;
                            button.decimal = (tLRPC$TL_pollAnswerVoters.voters / tLRPC$TL_messageMediaPoll.results.total_voters) * 100.0f;
                            button.percent = (int) button.decimal;
                            Button.access$3924(button, button.percent);
                            if (i2 == 0) {
                                i2 = button.percent;
                            } else if (button.percent != 0 && i2 != button.percent) {
                                z = true;
                            }
                            i -= button.percent;
                            i3 = Math.max(button.percent, i3);
                        } else {
                            i5++;
                        }
                    }
                }
            }
        }
        if (!z || i == 0) {
            return;
        }
        Collections.sort(arrayList, PollVotesAlert$$ExternalSyntheticLambda2.INSTANCE);
        int min = Math.min(i, arrayList.size());
        for (int i6 = 0; i6 < min; i6++) {
            Button.access$4012((Button) arrayList.get(i6), 1);
        }
    }

    public static /* synthetic */ int lambda$updateButtons$5(Button button, Button button2) {
        if (button.decimal > button2.decimal) {
            return -1;
        }
        return button.decimal < button2.decimal ? 1 : 0;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        int size = this.queries.size();
        for (int i = 0; i < size; i++) {
            this.chatActivity.getConnectionsManager().cancelRequest(this.queries.get(i).intValue(), true);
        }
        super.dismissInternal();
    }

    @SuppressLint({"NewApi"})
    public void updateLayout(boolean z) {
        if (this.listView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.listView;
            int paddingTop = recyclerListView.getPaddingTop();
            this.scrollOffsetY = paddingTop;
            recyclerListView.setTopGlowOffset(paddingTop);
            this.containerView.invalidate();
            return;
        }
        View childAt = this.listView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop();
        int dp = AndroidUtilities.dp(7.0f);
        if (top < AndroidUtilities.dp(7.0f) || holder == null || holder.getAdapterPosition() != 0) {
            top = dp;
        }
        boolean z2 = top <= AndroidUtilities.dp(12.0f);
        if ((z2 && this.actionBar.getTag() == null) || (!z2 && this.actionBar.getTag() != null)) {
            this.actionBar.setTag(z2 ? 1 : null);
            AnimatorSet animatorSet = this.actionBarAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.actionBarAnimation = null;
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.actionBarAnimation = animatorSet2;
            animatorSet2.setDuration(180L);
            AnimatorSet animatorSet3 = this.actionBarAnimation;
            Animator[] animatorArr = new Animator[2];
            ActionBar actionBar = this.actionBar;
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            float f = 1.0f;
            fArr[0] = z2 ? 1.0f : 0.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(actionBar, property, fArr);
            View view = this.actionBarShadow;
            Property property2 = View.ALPHA;
            float[] fArr2 = new float[1];
            if (!z2) {
                f = 0.0f;
            }
            fArr2[0] = f;
            animatorArr[1] = ObjectAnimator.ofFloat(view, property2, fArr2);
            animatorSet3.playTogether(animatorArr);
            this.actionBarAnimation.addListener(new AnonymousClass8());
            this.actionBarAnimation.start();
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.listView.getLayoutParams();
        int dp2 = top + (layoutParams.topMargin - AndroidUtilities.dp(11.0f));
        if (this.scrollOffsetY == dp2) {
            return;
        }
        RecyclerListView recyclerListView2 = this.listView;
        this.scrollOffsetY = dp2;
        recyclerListView2.setTopGlowOffset(dp2 - layoutParams.topMargin);
        this.containerView.invalidate();
    }

    /* renamed from: org.telegram.ui.Components.PollVotesAlert$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
        }

        AnonymousClass8() {
            PollVotesAlert.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            PollVotesAlert.this.actionBarAnimation = null;
        }
    }

    public void updatePlaceholder() {
        if (this.placeholderPaint == null) {
            return;
        }
        int color = Theme.getColor("dialogBackground");
        int color2 = Theme.getColor("dialogBackgroundGray");
        int averageColor = AndroidUtilities.getAverageColor(color2, color);
        this.placeholderPaint.setColor(color2);
        float dp = AndroidUtilities.dp(500.0f);
        this.gradientWidth = dp;
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, dp, 0.0f, new int[]{color2, averageColor, color2}, new float[]{0.0f, 0.18f, 0.36f}, Shader.TileMode.REPEAT);
        this.placeholderGradient = linearGradient;
        this.placeholderPaint.setShader(linearGradient);
        Matrix matrix = new Matrix();
        this.placeholderMatrix = matrix;
        this.placeholderGradient.setLocalMatrix(matrix);
    }

    /* loaded from: classes3.dex */
    public class Adapter extends RecyclerListView.SectionsAdapter {
        private Context mContext;

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public Object getItem(int i, int i2) {
            return null;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public String getLetter(int i) {
            return null;
        }

        public Adapter(Context context) {
            PollVotesAlert.this = r1;
            int i = UserConfig.selectedAccount;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder, int i, int i2) {
            if (i == 0 || i2 == 0) {
                return false;
            }
            return PollVotesAlert.this.queries == null || PollVotesAlert.this.queries.isEmpty();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getSectionCount() {
            return PollVotesAlert.this.voters.size() + 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getCountForSection(int i) {
            int i2 = 1;
            if (i == 0) {
                return 1;
            }
            VotesList votesList = (VotesList) PollVotesAlert.this.voters.get(i - 1);
            int count = votesList.getCount() + 1;
            if (TextUtils.isEmpty(votesList.next_offset) && !votesList.collapsed) {
                i2 = 0;
            }
            return count + i2;
        }

        /* renamed from: org.telegram.ui.Components.PollVotesAlert$Adapter$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends SectionCell {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context) {
                super(context);
                Adapter.this = r1;
            }

            @Override // org.telegram.ui.Components.PollVotesAlert.SectionCell
            protected void onCollapseClick() {
                VotesList votesList = (VotesList) getTag(2131230876);
                if (votesList.votes.size() <= 15) {
                    return;
                }
                boolean z = !votesList.collapsed;
                votesList.collapsed = z;
                if (z) {
                    votesList.collapsedCount = 10;
                }
                PollVotesAlert.this.listAdapter.notifyDataSetChanged();
            }
        }

        private SectionCell createSectionCell() {
            return new AnonymousClass1(this.mContext);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public View getSectionHeaderView(int i, View view) {
            if (view == null) {
                view = createSectionCell();
            }
            SectionCell sectionCell = (SectionCell) view;
            if (i == 0) {
                sectionCell.setAlpha(0.0f);
            } else {
                view.setAlpha(1.0f);
                VotesList votesList = (VotesList) PollVotesAlert.this.voters.get(i - 1);
                int i2 = 0;
                votesList.votes.get(0);
                int size = PollVotesAlert.this.poll.answers.size();
                while (true) {
                    if (i2 >= size) {
                        break;
                    }
                    TLRPC$TL_pollAnswer tLRPC$TL_pollAnswer = PollVotesAlert.this.poll.answers.get(i2);
                    if (Arrays.equals(tLRPC$TL_pollAnswer.option, votesList.option)) {
                        Button button = (Button) PollVotesAlert.this.votesPercents.get(votesList);
                        sectionCell.setText(tLRPC$TL_pollAnswer.text, button.percent, button.votesCount, votesList.getCollapsed());
                        sectionCell.setTag(2131230876, votesList);
                        break;
                    }
                    i2++;
                }
            }
            return view;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextCell textCell;
            if (i == 0) {
                textCell = new UserCell(this.mContext);
            } else if (i == 1) {
                if (PollVotesAlert.this.titleTextView.getParent() != null) {
                    ((ViewGroup) PollVotesAlert.this.titleTextView.getParent()).removeView(PollVotesAlert.this.titleTextView);
                }
                textCell = PollVotesAlert.this.titleTextView;
            } else if (i == 2) {
                textCell = createSectionCell();
            } else {
                TextCell textCell2 = new TextCell(this.mContext, 23, true);
                textCell2.setOffsetFromImage(65);
                textCell2.setColors("switchTrackChecked", "windowBackgroundWhiteBlueText4");
                textCell = textCell2;
            }
            return new RecyclerListView.Holder(textCell);
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public void onBindViewHolder(int i, int i2, RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 2) {
                if (itemViewType != 3) {
                    return;
                }
                VotesList votesList = (VotesList) PollVotesAlert.this.voters.get(i - 1);
                ((TextCell) viewHolder.itemView).setTextAndIcon(LocaleController.formatPluralString("ShowVotes", votesList.count - votesList.getCount(), new Object[0]), 2131165259, false);
                return;
            }
            SectionCell sectionCell = (SectionCell) viewHolder.itemView;
            VotesList votesList2 = (VotesList) PollVotesAlert.this.voters.get(i - 1);
            votesList2.votes.get(0);
            int size = PollVotesAlert.this.poll.answers.size();
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC$TL_pollAnswer tLRPC$TL_pollAnswer = PollVotesAlert.this.poll.answers.get(i3);
                if (Arrays.equals(tLRPC$TL_pollAnswer.option, votesList2.option)) {
                    Button button = (Button) PollVotesAlert.this.votesPercents.get(votesList2);
                    sectionCell.setText(tLRPC$TL_pollAnswer.text, button.percent, button.votesCount, votesList2.getCollapsed());
                    sectionCell.setTag(2131230876, votesList2);
                    return;
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 0) {
                int adapterPosition = viewHolder.getAdapterPosition();
                int sectionForPosition = getSectionForPosition(adapterPosition);
                int positionInSectionForPosition = getPositionInSectionForPosition(adapterPosition) - 1;
                UserCell userCell = (UserCell) viewHolder.itemView;
                VotesList votesList = (VotesList) PollVotesAlert.this.voters.get(sectionForPosition - 1);
                TLRPC$MessageUserVote tLRPC$MessageUserVote = votesList.votes.get(positionInSectionForPosition);
                TLRPC$User user = tLRPC$MessageUserVote.user_id != 0 ? PollVotesAlert.this.chatActivity.getMessagesController().getUser(Long.valueOf(tLRPC$MessageUserVote.user_id)) : null;
                boolean z = true;
                if (positionInSectionForPosition == votesList.getCount() - 1 && TextUtils.isEmpty(votesList.next_offset) && !votesList.collapsed) {
                    z = false;
                }
                userCell.setData(user, positionInSectionForPosition, z);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SectionsAdapter
        public int getItemViewType(int i, int i2) {
            if (i == 0) {
                return 1;
            }
            if (i2 == 0) {
                return 2;
            }
            return i2 + (-1) < ((VotesList) PollVotesAlert.this.voters.get(i + (-1))).getCount() ? 0 : 3;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.FastScrollAdapter
        public void getPositionForScrollProgress(RecyclerListView recyclerListView, float f, int[] iArr) {
            iArr[0] = 0;
            iArr[1] = 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        PollVotesAlert$$ExternalSyntheticLambda5 pollVotesAlert$$ExternalSyntheticLambda5 = new PollVotesAlert$$ExternalSyntheticLambda5(this);
        arrayList.add(new ThemeDescription(this.containerView, 0, null, null, null, null, "key_sheet_scrollUp"));
        arrayList.add(new ThemeDescription(this.containerView, 0, null, null, new Drawable[]{this.shadowDrawable}, null, "dialogBackground"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "dialogBackground"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "dialogScrollGlow"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "dialogTextBlack"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "dialogTextBlack"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBTITLECOLOR, null, null, null, null, "player_actionBarSubtitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "dialogTextBlack"));
        arrayList.add(new ThemeDescription(this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "dialogTextBlack"));
        arrayList.add(new ThemeDescription(this.actionBarShadow, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "dialogShadowLine"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, (String[]) null, (Paint[]) null, (Drawable[]) null, pollVotesAlert$$ExternalSyntheticLambda5, "dialogBackground"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, (String[]) null, (Paint[]) null, (Drawable[]) null, pollVotesAlert$$ExternalSyntheticLambda5, "dialogBackgroundGray"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SECTIONS, new Class[]{SectionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "key_graySectionText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SECTIONS, new Class[]{SectionCell.class}, new String[]{"middleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "key_graySectionText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SECTIONS, new Class[]{SectionCell.class}, new String[]{"righTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "key_graySectionText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_SECTIONS, new Class[]{SectionCell.class}, null, null, null, "graySection"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "dialogTextBlack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText4"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        return arrayList;
    }
}
