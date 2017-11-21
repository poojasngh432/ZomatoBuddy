package com.soumya.wwdablu.zomatobuddy.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.widget.TextView;

import com.soumya.wwdablu.zomatobuddy.R;
import com.soumya.wwdablu.zomatobuddy.model.RestaurantDetailsModel;
import com.soumya.wwdablu.zomatobuddy.model.restaurant.RestaurantResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RestaurantDetailsViewModel extends BaseObservable {

    public interface IDetailsAction {
        void onSuccess();
        void onError();
    }

    private RestaurantDetailsModel restaurantDetailsModel;
    private DisposableObserver disposableObserver;
    private IDetailsAction detailsAction;

    private ObservableField<String> restaurantName;
    private ObservableField<String> restaurantLocation;
    private ObservableField<Integer> restaurantDelivering;
    private ObservableField<String> restaurantExpense;
    private ObservableField<String> restaurantCuisines;

    public RestaurantDetailsViewModel(IDetailsAction detailsAction) {

        restaurantDetailsModel = new RestaurantDetailsModel();
        this.detailsAction = detailsAction;

        restaurantName = new ObservableField<>();
        restaurantLocation = new ObservableField<>();
        restaurantDelivering = new ObservableField<>();
        restaurantExpense = new ObservableField<>();
        restaurantCuisines = new ObservableField<>();
    }

    public void getRestaurantDetails(final String resId) {

        disposableObserver = restaurantDetailsModel.getRestaurantDetails(resId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<RestaurantResponse>() {
                @Override
                public void onNext(RestaurantResponse restaurantResponse) {
                    bindData(restaurantResponse);
                    detailsAction.onSuccess();
                }

                @Override
                public void onError(Throwable e) {
                    detailsAction.onError();
                }

                @Override
                public void onComplete() {
                    //
                }
            });
    }

    public String getMenuUrl() {
        return restaurantDetailsModel.getMenuUrl();
    }

    public String getPhotoUrl() {
        return restaurantDetailsModel.getPhotoUrl();
    }

    public String getWebsiteUrl() {
        return restaurantDetailsModel.getWebsite();
    }

    public String getZomatoDeepLink() {
        return restaurantDetailsModel.getZomatoLink();
    }

    public void clean() {

        if(null == disposableObserver && !disposableObserver.isDisposed()) {
            disposableObserver.dispose();
        }
    }

    @BindingAdapter("resolveDeliveryInfo")
    public static final void resolveRestaurantDeliveryingInfo(TextView textView, int isDeliveringNow) {

        textView.setText(0 == isDeliveringNow ? textView.getContext().getString(R.string.open_now)
                : textView.getContext().getString(R.string.closed));

        textView.setTextColor(0 == isDeliveringNow ? Color.parseColor("#33B758") : Color.RED);
    }

    @Bindable
    public ObservableField<String> getRestaurantName() {
        return restaurantName;
    }

    @Bindable
    public ObservableField<String> getRestaurantLocation() {
        return restaurantLocation;
    }

    @Bindable
    public ObservableField<Integer> getRestaurantDelivering() {
        return restaurantDelivering;
    }

    @Bindable
    public ObservableField<String> getRestaurantExpense() {
        return restaurantExpense;
    }

    @Bindable
    public ObservableField<String> getRestaurantCuisines() {
        return restaurantCuisines;
    }

    private void setRestaurantName(String restaurantName) {
        this.restaurantName.set(restaurantName);
    }

    private void setRestaurantLocation(String restaurantLocation) {
        this.restaurantLocation.set(restaurantLocation);
    }

    private void setRestaurantDelivering(int deliveringNow) {
        this.restaurantDelivering.set(deliveringNow);
    }

    private void setRestaurantExpense(String expense) {
        this.restaurantExpense.set(expense);
    }

    private void setRestaurantCuisines(String cuisines) {
        this.restaurantCuisines.set(cuisines);
    }

    private void bindData(RestaurantResponse restaurantResponse) {

        setRestaurantName(restaurantResponse.getName());
        setRestaurantLocation(restaurantResponse.getLocation().getAddress());
        setRestaurantDelivering(restaurantResponse.getIsDeliveringNow());
        setRestaurantExpense(Integer.toString(restaurantResponse.getAverageCostForTwo()));
        setRestaurantCuisines(restaurantResponse.getCuisines());
    }
}