package androidx.core.app;

import android.app.Person;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.core.graphics.drawable.IconCompat;
/* loaded from: classes.dex */
public class Person {
    IconCompat mIcon;
    boolean mIsBot;
    boolean mIsImportant;
    String mKey;
    CharSequence mName;
    String mUri;

    public static Person fromPersistableBundle(PersistableBundle persistableBundle) {
        return Api22Impl.fromPersistableBundle(persistableBundle);
    }

    Person(Builder builder) {
        this.mName = builder.mName;
        this.mIcon = builder.mIcon;
        this.mUri = builder.mUri;
        this.mKey = builder.mKey;
        this.mIsBot = builder.mIsBot;
        this.mIsImportant = builder.mIsImportant;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("name", this.mName);
        IconCompat iconCompat = this.mIcon;
        bundle.putBundle("icon", iconCompat != null ? iconCompat.toBundle() : null);
        bundle.putString("uri", this.mUri);
        bundle.putString("key", this.mKey);
        bundle.putBoolean("isBot", this.mIsBot);
        bundle.putBoolean("isImportant", this.mIsImportant);
        return bundle;
    }

    public PersistableBundle toPersistableBundle() {
        return Api22Impl.toPersistableBundle(this);
    }

    public android.app.Person toAndroidPerson() {
        return Api28Impl.toAndroidPerson(this);
    }

    public CharSequence getName() {
        return this.mName;
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public String getUri() {
        return this.mUri;
    }

    public String getKey() {
        return this.mKey;
    }

    public boolean isBot() {
        return this.mIsBot;
    }

    public boolean isImportant() {
        return this.mIsImportant;
    }

    public String resolveToLegacyUri() {
        String str = this.mUri;
        if (str != null) {
            return str;
        }
        if (this.mName != null) {
            return "name:" + ((Object) this.mName);
        }
        return "";
    }

    /* loaded from: classes.dex */
    public static class Builder {
        IconCompat mIcon;
        boolean mIsBot;
        boolean mIsImportant;
        String mKey;
        CharSequence mName;
        String mUri;

        public Builder setName(CharSequence charSequence) {
            this.mName = charSequence;
            return this;
        }

        public Builder setIcon(IconCompat iconCompat) {
            this.mIcon = iconCompat;
            return this;
        }

        public Builder setUri(String str) {
            this.mUri = str;
            return this;
        }

        public Builder setKey(String str) {
            this.mKey = str;
            return this;
        }

        public Builder setBot(boolean z) {
            this.mIsBot = z;
            return this;
        }

        public Builder setImportant(boolean z) {
            this.mIsImportant = z;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

    /* loaded from: classes.dex */
    static class Api22Impl {
        static Person fromPersistableBundle(PersistableBundle persistableBundle) {
            String string;
            String string2;
            String string3;
            boolean z;
            boolean z2;
            Builder builder = new Builder();
            string = persistableBundle.getString("name");
            Builder name = builder.setName(string);
            string2 = persistableBundle.getString("uri");
            Builder uri = name.setUri(string2);
            string3 = persistableBundle.getString("key");
            Builder key = uri.setKey(string3);
            z = persistableBundle.getBoolean("isBot");
            Builder bot = key.setBot(z);
            z2 = persistableBundle.getBoolean("isImportant");
            return bot.setImportant(z2).build();
        }

        static PersistableBundle toPersistableBundle(Person person) {
            PersistableBundle persistableBundle = new PersistableBundle();
            CharSequence charSequence = person.mName;
            persistableBundle.putString("name", charSequence != null ? charSequence.toString() : null);
            persistableBundle.putString("uri", person.mUri);
            persistableBundle.putString("key", person.mKey);
            persistableBundle.putBoolean("isBot", person.mIsBot);
            persistableBundle.putBoolean("isImportant", person.mIsImportant);
            return persistableBundle;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api28Impl {
        static Person fromAndroidPerson(android.app.Person person) {
            CharSequence name;
            Icon icon;
            IconCompat iconCompat;
            String uri;
            String key;
            boolean isBot;
            boolean isImportant;
            Icon icon2;
            Builder builder = new Builder();
            name = person.getName();
            Builder name2 = builder.setName(name);
            icon = person.getIcon();
            if (icon != null) {
                icon2 = person.getIcon();
                iconCompat = IconCompat.createFromIcon(icon2);
            } else {
                iconCompat = null;
            }
            Builder icon3 = name2.setIcon(iconCompat);
            uri = person.getUri();
            Builder uri2 = icon3.setUri(uri);
            key = person.getKey();
            Builder key2 = uri2.setKey(key);
            isBot = person.isBot();
            Builder bot = key2.setBot(isBot);
            isImportant = person.isImportant();
            return bot.setImportant(isImportant).build();
        }

        static android.app.Person toAndroidPerson(Person person) {
            Person.Builder name;
            Person.Builder icon;
            Person.Builder uri;
            Person.Builder key;
            Person.Builder bot;
            Person.Builder important;
            android.app.Person build;
            name = new Person.Builder().setName(person.getName());
            icon = name.setIcon(person.getIcon() != null ? person.getIcon().toIcon() : null);
            uri = icon.setUri(person.getUri());
            key = uri.setKey(person.getKey());
            bot = key.setBot(person.isBot());
            important = bot.setImportant(person.isImportant());
            build = important.build();
            return build;
        }
    }
}
