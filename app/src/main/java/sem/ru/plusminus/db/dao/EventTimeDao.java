package sem.ru.plusminus.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import sem.ru.plusminus.mvp.model.EventTime;

@Dao
public interface EventTimeDao {

    @Insert
    long insert(EventTime eventTime);

    @Update
    void update(EventTime... eventTimes);

    @Delete
    void delete(EventTime... eventTimes);

    @Query("SELECT * FROM eventtime")
    List<EventTime> getAllEventTimes();

    @Query("SELECT * FROM eventtime WHERE eventId=:eventId")
    List<EventTime> findTimesForEvent(final long eventId);
}
