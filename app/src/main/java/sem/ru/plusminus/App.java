package sem.ru.plusminus;

import android.app.Application;
import android.arch.persistence.room.Room;

import sem.ru.plusminus.db.AppDatabase;

public class App extends Application {

    private static App instance;
    private AppDatabase db;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db =  Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "db")
                //.allowMainThreadQueries()//!!! Bad practices!! mast be rx or threads
                .build();
    }

    public static App getInstance(){
        return instance;
    }

    public AppDatabase getDb() {
        return db;
    }
}
