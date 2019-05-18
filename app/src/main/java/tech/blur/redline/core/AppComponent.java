package tech.blur.redline.core;

import dagger.Component;
import tech.blur.redline.core.modules.ApiModule;
import tech.blur.redline.core.modules.SharedPreferencesModule;
import tech.blur.redline.features.map.MapPresenter;
import tech.blur.redline.features.signin.SignInPresenter;
import tech.blur.redline.features.signup.SignUpPresenter;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ApiModule.class, SharedPreferencesModule.class})
public interface AppComponent {

    void inject(SignUpPresenter signUpPresenter);

    void inject(SignInPresenter signInPresenter);

    void inject(MapPresenter mapPresenter);
}

