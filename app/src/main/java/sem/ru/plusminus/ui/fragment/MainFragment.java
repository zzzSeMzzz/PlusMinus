package sem.ru.plusminus.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sem.ru.plusminus.R;
import sem.ru.plusminus.mvp.model.Event;
import sem.ru.plusminus.mvp.presenter.MainPresenter;
import sem.ru.plusminus.mvp.view.MainView;
import sem.ru.plusminus.ui.EditEventActivity;
import sem.ru.plusminus.ui.adapter.EventAdapter;
import sem.ru.plusminus.ui.adapter.SwipeToDeleteCallback;

public class MainFragment extends MvpAppCompatFragment implements MainView,
        EventAdapter.OnRvItemClickListener {

    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;
    @BindView(R.id.btnAdd)
    FloatingActionButton btnAdd;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    @InjectPresenter
    MainPresenter presenter;

    public static final int REQUEST_EDIT_EVENT = 1;

    private EventAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onEventItemClick(Event event, int position) {
        showEdit(event.id, position);
    }

    @Override
    public void onEventItemRemoved(int currentItemsSize) {
        if(currentItemsSize==0){
            presenter.getViewState().showEmptyText(true);
        }
    }

    @Override
    public void showError(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyText(boolean show) {
        tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        rvEvents.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setItems(List<Event> events) {
        adapter = new EventAdapter(events, this);
        rvEvents.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(rvEvents);
    }

    private void showEdit(long id, int pos){
        Intent intent = new Intent(getActivity(), EditEventActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("pos", pos);
        startActivityForResult(intent, REQUEST_EDIT_EVENT);
    }

    @OnClick(R.id.btnAdd)
    public void onClickAdd(View v){
        showEdit(-1, -1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_EDIT_EVENT:
                    int pos = data.getIntExtra("pos", -1);
                    if(pos==-1) {//вставляем
                        presenter.addEventById(data.getLongExtra("rowId", -1));
                    }else {//обновляем
                        adapter.updateItem(pos);
                    }
                    break;
            }
        }
    }

    @Override
    public void addEvent(Event event) {
        if(adapter!=null){
            adapter.addItem(event);
        }
    }
}
