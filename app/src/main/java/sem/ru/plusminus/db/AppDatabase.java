package sem.ru.plusminus.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import sem.ru.plusminus.db.dao.EventDao;
import sem.ru.plusminus.db.dao.EventTimeDao;
import sem.ru.plusminus.mvp.model.Event;
import sem.ru.plusminus.mvp.model.EventTime;

@Database(entities = {EventTime.class, Event.class},
        version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EventTimeDao getEventTimeDao();

    public abstract EventDao getEventDao();
}
