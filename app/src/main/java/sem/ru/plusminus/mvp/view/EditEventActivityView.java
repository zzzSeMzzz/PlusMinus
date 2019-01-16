package sem.ru.plusminus.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface EditEventActivityView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void initFragment();
}
