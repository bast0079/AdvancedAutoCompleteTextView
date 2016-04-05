package example.nerdery.spellingcorrection;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

import example.nerdery.spellingcorrection.adapter.SearchAdapter;
import example.nerdery.spellingcorrection.model.Person;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Person> people = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        people.add(new Person("Steve", "Bastin"));
        people.add(new Person("Richard", "Banasiak"));
        people.add(new Person("Kenton", "Watson"));
        people.add(new Person("Steve", "Johnson"));
        people.add(new Person("Theo", "Kanning"));
        people.add(new Person("Joe", "Rider"));
        people.add(new Person("Patrick", "Fuentes"));
        people.add(new Person("Jeff", "Huston"));
        people.add(new Person("Jayd", "Saucedo"));
        people.add(new Person("Steve", "Barton"));
        people.add(new Person("Stephen", "Hopper"));
        people.add(new Person("Stevie", "Bastin"));
        people.add(new Person("Steve", "Bustin"));
        people.add(new Person("Steve", "Backin"));

        AutoCompleteTextView searchBar = (AutoCompleteTextView) findViewById(R.id.search_bar_normal);
        SearchAdapter adapter = new SearchAdapter(this, R.layout.name_row, people );
        searchBar.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
