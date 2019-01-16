package sem.ru.plusminus.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;

import sem.ru.plusminus.R;
import sem.ru.plusminus.mvp.presenter.EditEventAActivityPresenter;
import sem.ru.plusminus.mvp.view.EditEventActivityView;
import sem.ru.plusminus.ui.fragment.EditEventFragment;

public class EditEventActivity extends MvpBackActivity implements EditEventActivityView {

    @InjectPresenter
    EditEventAActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
    }

    @Override
    public void initFragment() {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        args.putLong("id", getIntent().getLongExtra("id", -1));
        args.putInt("pos", getIntent().getIntExtra("pos", -1));
        args.putInt("size", getIntent().getIntExtra("size", 0));
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    @Override
    public void onBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (!(fragment instanceof EditEventFragment)) {
            super.onBackPressed();
        }else{
            ((EditEventFragment) fragment).onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
