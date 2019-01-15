package sem.ru.plusminus.mvp.view;

import java.util.List;

import sem.ru.plusminus.mvp.model.Event;

public interface MainView extends BaseView {

    //@StateStrategyType(OneExecutionStateStrategy.class)
    void setItems(List<Event> events);

    void showEmptyText(boolean show);

    void addEvent(Event event);
}
