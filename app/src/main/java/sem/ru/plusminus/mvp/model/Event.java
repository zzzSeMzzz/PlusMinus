package sem.ru.plusminus.mvp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Event {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public String plusName;

    public String minusName;

    public int cntPlus;

    public int cntMinus;

    public Event(){
        name="";
        plusName="+";
        minusName="-";
    }

    public Event(String name, String plusName, String minusName, int cntPlus, int cntMinus) {
        this.name = name;
        this.plusName = plusName;
        this.minusName = minusName;
        this.cntPlus = cntPlus;
        this.cntMinus = cntMinus;
    }


}
