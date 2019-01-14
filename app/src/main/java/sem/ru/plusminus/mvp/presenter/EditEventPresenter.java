package sem.ru.plusminus.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sem.ru.plusminus.App;
import sem.ru.plusminus.mvp.model.Event;
import sem.ru.plusminus.mvp.model.EventTime;
import sem.ru.plusminus.mvp.view.EditEventView;

@InjectViewState
public class EditEventPresenter extends BasePresenter<EditEventView> {

    private static final String TAG = "EditEventPresenter";

    private long eventId;
    private int eventPos;
    private int eventsSize;

    public int getEventsSize() {
        return eventsSize;
    }

    private Event currentEvent;

    public long getEventId() {
        return eventId;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public EditEventPresenter(long eventId, int eventPos, int eventsSize) {
        this.eventId = eventId;
        this.eventPos = eventPos;
        this.eventsSize = eventsSize;
    }

    public void insertEvent(Event event){
        Disposable d = Observable
                .fromCallable(() -> App.getInstance().getDb().getEventDao().insert(event))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                            Log.d(TAG, "success insert event");
                            getViewState().onEventInserted(id);
                        },
                        error -> {
                            Log.e(TAG, "fail insert event");
                            getViewState().showError("Ошибка добавления события");
                        }
                );
        unsubscribeOnDestroy(d);
    }

    public void updateEvent(){
        updateEvent(this.currentEvent);
    }

    public void updateEvent(Event event){
        Completable.fromAction(()
                -> App.getInstance().getDb().getEventDao().update(event))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "onComplete: success upd event");
                    getViewState().onEventUpdated(eventId, eventPos);
                }, throwable -> {
                    Log.e(TAG, "updateEvent: fail update event");
                    throwable.printStackTrace();
                });
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getEvents(Observable
                .fromCallable(() -> App.getInstance().getDb()
                        .getEventTimeDao().findTimesForEvent(eventId)));
        loadCurrentEvent();
    }

    private void getEvents(Observable<List<EventTime>> eventsObservable){
        Disposable d = eventsObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(events -> {
                            Log.d(TAG, "success get events time size="+events.size());
                            /*if(events.size()==0){
                                getViewState().showEmptyText(true);
                            }*/
                            getViewState().setEventTimeItems(events);
                        },
                        error -> {
                            Log.e(TAG, "fail load event times");
                            getViewState().showError("Ошибка загрузки времен собатия");
                        }
                );
        unsubscribeOnDestroy(d);
    }

    private void loadCurrentEvent(){
        if(eventId==-1) return;
        Disposable d = Observable
                .fromCallable(() -> App.getInstance().getDb()
                        .getEventDao().getById(eventId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                            Log.d(TAG, "success get current event");
                            currentEvent=event;
                            getViewState().setCurrentEvent(currentEvent);
                        },
                        error -> {
                            Log.e(TAG, "fail load event times");
                            getViewState().showError("Ошибка загрузки времен собатия");
                        }
                );
        unsubscribeOnDestroy(d);
    }
}
