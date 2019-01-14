package sem.ru.plusminus.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import sem.ru.plusminus.mvp.model.Event;

@Dao
public interface EventDao {

    @Insert
    long insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("SELECT * FROM event ORDER BY pos ASC")
    List<Event> getAll();

    @Query("SELECT * FROM Event WHERE id = :id")
    Event getById(long id);
}
