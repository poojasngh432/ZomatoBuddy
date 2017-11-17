package com.soumya.wwdablu.zomatobuddy.dagger;

import com.soumya.wwdablu.zomatobuddy.activity.SplashActivity;
import com.soumya.wwdablu.zomatobuddy.model.SearchModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = NetworkModule.class)
@Singleton
public interface NetworkComponent {

    void inject(SplashActivity splashActivity);
    void inject(SearchModel searchModel);
}
