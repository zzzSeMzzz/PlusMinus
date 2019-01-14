package sem.ru.plusminus.ui.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sem.ru.plusminus.App;
import sem.ru.plusminus.R;
import sem.ru.plusminus.mvp.model.EventTime;

public class EventTimeAdapter extends RecyclerView.Adapter<EventTimeAdapter.ViewHolder> {

    private List<EventTime> items;
    private static final String TAG = "EventTimeAdapter";

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
}
