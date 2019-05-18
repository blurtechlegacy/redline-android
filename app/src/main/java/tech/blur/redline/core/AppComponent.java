package tech.blur.redline.core;

import dagger.Component;
import org.jetbrains.annotations.NotNull;
import tech.blur.redline.core.modules.ApiModule;
import tech.blur.redline.core.modules.SharedPreferencesModule;
import tech.blur.redline.features.signup.SignUpPresenter;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ApiModule.class, SharedPreferencesModule.class})
public interface AppComponent {

    void inject( SignUpPresenter signUpPresenter);
}

