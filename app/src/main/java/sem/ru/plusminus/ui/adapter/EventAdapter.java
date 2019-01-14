package sem.ru.plusminus.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import sem.ru.plusminus.App;
import sem.ru.plusminus.R;
import sem.ru.plusminus.mvp.model.Event;
import sem.ru.plusminus.mvp.model.EventTime;
import sem.ru.plusminus.utils.TimeUtil;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>
    implements ItemTouchHelperAdapter{

    private List<Event> items;
    private static final String TAG = "EventAdapter";

    public List<Event> getItems() {
        return items;
    }

    public interface OnRvItemClickListener {
        void onEventItemClick(Event event, int position);

        void onEventItemRemoved(int currentItemsSize);
    }

    private OnRvItemClickListener listener;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btnPlus)
        Button btnPlus;
        @BindView(R.id.btnMinus)
        Button btnMinus;
        @BindView(R.id.tvMinus)
        TextView tvMinus;
        @BindView(R.id.tvPlus)
        TextView tvPlus;
        @BindView(R.id.tvName)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                if(listener!=null){
                    listener.onEventItemClick(items.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            btnPlus.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                Event event = items.get(pos);
                event.cntPlus++;
                updateEvent(event, true);
                notifyItemChanged(pos);
            });
            btnMinus.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                Event event = items.get(pos);
                event.cntMinus++;
                updateEvent(event, false);
                notifyItemChanged(pos);
            });
        }
    }

    private void updateEvent(Event event, boolean isPositive){
        Completable.fromAction(()
                -> App.getInstance().getDb().getEventDao().update(event))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "onComplete: success upd event");
                    addEventTime(event, isPositive);
                }, throwable -> {
                    Log.e(TAG, "updateEvent: fail update event");
                    throwable.printStackTrace();
                });
    }

    private void updateEvent(Event event){
        Completable.fromAction(()
                -> App.getInstance().getDb().getEventDao().update(event))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "onComplete: success upd event");
                }, throwable -> {
                    Log.e(TAG, "updateEvent: fail update event");
                    throwable.printStackTrace();
                });
    }

    private void addEventTime(Event event, boolean isPositive){
        EventTime eventTime = new EventTime();
        eventTime.eventId=event.id;
        eventTime.name=isPositive ? event.plusName : event.minusName;
        eventTime.name = eventTime.name+" "+TimeUtil.getCurrentTime();
        eventTime.eventType=isPositive ? 1 : 0;

        Observable
                .fromCallable(() ->
                        App.getInstance().getDb().getEventTimeDao().insert(eventTime))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                            Log.d(TAG, "success insert event");
                        },
                        error -> {
                            Log.e(TAG, "fail insert event");
                        }
                );
    }

    private void deleteEvent(Event event){
        Completable.fromAction(()
                -> App.getInstance().getDb().getEventDao().delete(event))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "onComplete: success del event");
                }, throwable -> {
                    Log.e(TAG, "updateEvent: fail update event");
                    throwable.printStackTrace();
                });
    }

    public EventAdapter(List<Event> items) {
        this.items = items;
    }

    public EventAdapter(List<Event> items, OnRvItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Event event = items.get(i);
        viewHolder.tvMinus.setText(getMinusPercent(event));
        viewHolder.tvPlus.setText(getPlusPercent(event));
        viewHolder.tvName.setText(event.name);
        viewHolder.btnMinus.setText(event.minusName);
        viewHolder.btnPlus.setText(event.plusName);
    }

    private String getMinusPercent(Event event){
        int sum = event.cntMinus+event.cntPlus;
        int p =Math.round((event.cntMinus * 100.0f) / sum);
        return String.format("(%d) %d%s",event.cntMinus, p, "%");
    }

    private String getPlusPercent(Event event){
        int sum = event.cntMinus+event.cntPlus;
        int p =Math.round((event.cntPlus * 100.0f) / sum);
        return String.format("%d%s (%d)",p, "%", event.cntPlus);
    }


    public void addItem(Event event){
        items.add(event);
        notifyItemRangeInserted(items.size()-1, 1);
    }

    public void deleteItem(int position){
        Event event = items.get(position);
        deleteEvent(event);
        items.remove(position);
        notifyItemRemoved(position);
        if(listener!=null) listener.onEventItemRemoved(items.size());
        /*mRecentlyDeletedItemPosition = position;
        mListItems.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();*/

    }

    public void updateItem(int position){
        Event event = items.get(position);
        Observable
                .fromCallable(() -> App.getInstance().getDb().getEventDao().getById(event.id))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventNew -> {
                            items.set(position, eventNew);
                            notifyItemChanged(position);
                        },
                        error -> {
                            error.printStackTrace();
                        }
                );
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
                Event event = items.get(i);
                event.pos=event.pos-1;
                updateEvent(event);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
                Event event = items.get(i);
                event.pos=event.pos+1;
                updateEvent(event);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        Event event = items.get(toPosition);
        event.pos=toPosition;
        updateEvent(event);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        /*items.remove(position);
        notifyItemRemoved(position);*/
    }
}
