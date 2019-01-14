package sem.ru.plusminus.mvp.view;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import sem.ru.plusminus.mvp.model.Event;

public interface MainView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setItems(List<Event> events);

    void showEmptyText(boolean show);

    void addEvent(Event event);
}
