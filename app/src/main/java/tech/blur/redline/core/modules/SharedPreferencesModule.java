package tech.blur.redline.core.modules;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import tech.blur.redline.R;

import javax.inject.Singleton;

@Module
public class SharedPreferencesModule {

    private Context context;

    public SharedPreferencesModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreference() {
        return context.getSharedPreferences(context.getResources().getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
    }


}
