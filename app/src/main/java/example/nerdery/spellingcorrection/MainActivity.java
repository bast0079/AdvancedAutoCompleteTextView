package example.nerdery.spellingcorrection;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        loadEmployees();
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

    private void loadEmployees() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("nerdery_employees.txt")));
            String nameString;
            while((nameString = reader.readLine()) != null) {
                String[] names = splitIntoFirstAndLastName(nameString);
                this.people.add(new Person(names[0], names[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String[] splitIntoFirstAndLastName(String name) {
        int index = name.indexOf(" ");
        String firstName = name.substring(0, index);
        String lastName = name.substring(index + 1);

        return new String[] {firstName, lastName};
    }
}
