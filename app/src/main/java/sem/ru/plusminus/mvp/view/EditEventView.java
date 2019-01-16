package sem.ru.plusminus.mvp.view;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import sem.ru.plusminus.mvp.model.Event;
import sem.ru.plusminus.mvp.model.EventTime;

public interface EditEventView extends BaseView {

    void onEventInserted(Long id);

    void onEventUpdated(Long id, int pos);

    //@StateStrategyType(OneExecutionStateStrategy.class)
    void setEventTimeItems(List<EventTime> eventTimes);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setCurrentEvent(Event event);
}
