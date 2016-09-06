package example.nerdery.spellingcorrection;


import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
        initToolbar();
        loadEmployees();
        initAdapter();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initAdapter() {
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_bar_normal);
        SearchAdapter adapter = new SearchAdapter(this, R.layout.name_row, people );
        if(autoCompleteTextView != null) {
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fullName = ((TextView)view
                            .findViewById(R.id.row_person_name))
                            .getText()
                            .toString();

                    autoCompleteTextView.setText(fullName);

                }
            });
        }
    }

    /**
     * Load in your searchable content how you like, i.e. api call, load from file, retrieve from
     * SqlLite db
     */
    private void loadEmployees() {
        BufferedReader reader = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.nerdery_employees);
            reader = new BufferedReader(new InputStreamReader(is));
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
