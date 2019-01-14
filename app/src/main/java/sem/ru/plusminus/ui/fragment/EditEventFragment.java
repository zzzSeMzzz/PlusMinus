package sem.ru.plusminus.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sem.ru.plusminus.R;
import sem.ru.plusminus.mvp.model.Event;
import sem.ru.plusminus.mvp.model.EventTime;
import sem.ru.plusminus.mvp.presenter.EditEventPresenter;
import sem.ru.plusminus.mvp.view.EditEventView;
import sem.ru.plusminus.ui.adapter.EventTimeAdapter;
import sem.ru.plusminus.ui.adapter.SwipeToDeleteCallbackET;

public class EditEventFragment extends MvpAppCompatFragment implements EditEventView,
            EventTimeAdapter.OnEventTimeListener{

    @BindView(R.id.edMinus)
    EditText edMinus;
    @BindView(R.id.edName)
    EditText edName;
    @BindView(R.id.edPlus)
    EditText edPlus;
    @BindView(R.id.rvEventTimes)
    RecyclerView rvEventTimes;

    @InjectPresenter
    EditEventPresenter presenter;

    @ProvidePresenter
    EditEventPresenter provideEditEventPresenter(){
        return new EditEventPresenter(
                getArguments().getLong("id", -1),
                getArguments().getInt("pos", -1),
                getArguments().getInt("size", 0)
        );
    }

    private EventTimeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_event, null);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void showError(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnOk)
    public void onClickSave(View v){
        if(edName.getText().toString().isEmpty()){
            showError("Введите название счетчика");
            return;
        }
        if(presenter.getEventId()==-1) {
            Event event = new Event(
                    edName.getText().toString(),
                    edPlus.getText().toString().isEmpty() ? "+" : edPlus.getText().toString(),
                    edMinus.getText().toString().isEmpty() ? "-" : edMinus.getText().toString(),
                    0,
                    0,
                    presenter.getEventsSize()//по умолдчанию добавляется в последнюю позицию
            );
            presenter.insertEvent(event);
        }else{
            presenter.getCurrentEvent().name=edName.getText().toString();
            presenter.getCurrentEvent().plusName=edPlus.getText().toString();
            presenter.getCurrentEvent().minusName=edMinus.getText().toString();
            presenter.updateEventAndFinish();
        }
    }

    @Override
    public void onEventInserted(Long id) {
        Intent intent = new Intent();
        intent.putExtra("rowId", id);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onEventUpdated(Long id, int pos) {
        Intent intent = new Intent();
        intent.putExtra("rowId", id);
        intent.putExtra("pos", pos);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void setEventTimeItems(List<EventTime> eventTimes) {
        adapter = new EventTimeAdapter(eventTimes, this::onEventTimeDelete);
        rvEventTimes.setAdapter(adapter);
        rvEventTimes.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallbackET(adapter));
        itemTouchHelper.attachToRecyclerView(rvEventTimes);
    }

    @Override
    public void setCurrentEvent(Event event) {
        edMinus.setText(event.minusName);
        edName.setText(event.name);
        edPlus.setText(event.plusName);
    }

    @Override
    public void onEventTimeDelete(boolean isPositive) {
        if(isPositive){
            presenter.getCurrentEvent().cntPlus--;
        }else {
            presenter.getCurrentEvent().cntMinus--;
        }
        presenter.updateEvent();
    }
}
