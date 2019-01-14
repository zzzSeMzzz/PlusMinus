package sem.ru.plusminus.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;

import sem.ru.plusminus.mvp.view.EditEventActivityView;

@InjectViewState
public class EditEventAActivityPresenter extends BasePresenter<EditEventActivityView> {

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }
}
