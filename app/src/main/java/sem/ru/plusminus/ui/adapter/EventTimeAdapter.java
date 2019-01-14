package sem.ru.plusminus.ui.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import sem.ru.plusminus.App;
import sem.ru.plusminus.R;
import sem.ru.plusminus.mvp.model.EventTime;

public class EventTimeAdapter extends RecyclerView.Adapter<EventTimeAdapter.ViewHolder> {

    public interface OnEventTimeListener{
        void onEventTimeDelete(boolean isPositive);
    }

    private List<EventTime> items;
    private static final String TAG = "EventTimeAdapter";
    private OnEventTimeListener listener;

    public List<EventTime> getItems() {
        return items;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvEventTime)
        TextView tvEventTime;
        @BindView(R.id.llEventTime)
        LinearLayout llMain;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public EventTimeAdapter(List<EventTime> items) {
        this.items = items;
    }

    public EventTimeAdapter(List<EventTime> items, OnEventTimeListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public EventTimeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event_time,
                viewGroup, false);
        return new EventTimeAdapter.ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final EventTimeAdapter.ViewHolder viewHolder, int i) {
        EventTime event = items.get(i);
        viewHolder.tvEventTime.setText(event.name);
        boolean isPositive = event.eventType==1;
        viewHolder.llMain.setBackgroundColor(isPositive ?
                ContextCompat.getColor(App.getInstance().getApplicationContext(),
                        R.color.plusColor) : ContextCompat
                .getColor(App.getInstance().getApplicationContext(), R.color.minusColor));
    }


    private void deleteEventTime(EventTime eventTime, boolean isPositive){
        Completable.fromAction(()
                -> App.getInstance().getDb().getEventTimeDao().delete(eventTime))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "onComplete: success del eventTime");
                    if(listener!=null){
                        listener.onEventTimeDelete(isPositive);
                    }
                }, throwable -> {
                    Log.e(TAG, "fail delete eventTime");
                    throwable.printStackTrace();
                });
    }

    public void deleteItem(int position){
        EventTime eventTime = items.get(position);
        boolean isPositive = eventTime.eventType==1;
        deleteEventTime(eventTime, isPositive);
        items.remove(position);
        notifyItemRemoved(position);
    }
}
