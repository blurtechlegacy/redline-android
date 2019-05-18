package tech.blur.redline.core;

import dagger.Component;
import tech.blur.redline.core.modules.ApiModule;
import tech.blur.redline.core.modules.SharedPreferencesModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ApiModule.class, SharedPreferencesModule.class})
public interface AppComponent {

}

