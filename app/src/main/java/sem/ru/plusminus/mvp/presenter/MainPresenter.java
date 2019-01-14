package sem.ru.plusminus.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sem.ru.plusminus.App;
import sem.ru.plusminus.mvp.model.Event;
import sem.ru.plusminus.mvp.view.MainView;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {

    private static final String TAG = "MainPresenter";

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        //List<Event> events = App.getInstance().getDb().getEventDao().getAll();
        //getViewState().setItems(events);
       // if(events.size()==0) getViewState().showEmptyText(true);
        Observable<List<Event>> eventsObservable = Observable
                .fromCallable(() -> App.getInstance().getDb().getEventDao().getAll());
        getEvents(eventsObservable);
    }

    private void getEvents(Observable<List<Event>> eventsObservable){
        Disposable d = eventsObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(events -> {
                            Log.d(TAG, "success get events size="+events.size());
                            if(events.size()==0){
                                getViewState().showEmptyText(true);
                            }
                            getViewState().setItems(events);
                        },
                        error -> {
                            Log.e(TAG, "auth: failed auth");
                            getViewState().showError("Ошибка загрузки событий");
                        }
                );
        unsubscribeOnDestroy(d);
    }

    public void addEventById(long id){
        Disposable d =
        Observable
                .fromCallable(() -> App.getInstance().getDb().getEventDao().getById(id))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                            Log.d(TAG, "success get event");
                            getViewState().addEvent(event);
                            getViewState().showEmptyText(false);
                        },
                        error -> {
                            Log.e(TAG, "auth: failed auth");
                            getViewState().showError("Ошибка добавления события");
                        }
                );
        unsubscribeOnDestroy(d);
    }
}
