package sem.ru.plusminus.mvp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import sem.ru.plusminus.utils.TimeUtil;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Event.class,
        parentColumns = "id",
        childColumns = "eventId",
        onDelete = CASCADE))
public class EventTime {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public long eventId;

    public int eventType;

    public EventTime() {
        this.name=TimeUtil.getCurrentTime();
    }

    public EventTime(String name, int userId) {
        this.name = name;
        this.eventId = userId;
    }
}
