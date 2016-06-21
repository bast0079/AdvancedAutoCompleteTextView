package example.nerdery.spellingcorrection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.nerdery.spellingcorrection.BuildConfig;
import example.nerdery.spellingcorrection.R;
import example.nerdery.spellingcorrection.common.MisspellingTools;
import example.nerdery.spellingcorrection.model.Person;

/**
 * Created by sbastin on 4/4/16.
 */
public class SearchAdapter extends ArrayAdapter<Person> implements Filterable {

    private static final int MAX_RESULTS = 10;
    Context context;
    int layoutResourceId;
    List<Person> persons;
    List<Person> filteredGuests;

    TextView personNameLabel;

    public SearchAdapter(Context context, int layoutResourceId, List<Person> persons) {
        super(context, layoutResourceId, persons);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.persons = persons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutResourceId, parent, false);

        personNameLabel = (TextView) convertView.findViewById(R.id.row_person_name);
        Person guest = filteredGuests.get(position);
        String guestName = guest.getFirstName().concat(" ").concat(guest.getLastName());

        personNameLabel.setText(guestName);

        return convertView;
    }

    @Override
    public int getCount() {
        return filteredGuests != null ? filteredGuests.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    List<Person> temp = new ArrayList<>(persons);
                    filterResults.values = temp;
                    filterResults.count = temp.size();
                }else {
                    List<Person> temp = new ArrayList<>(persons);
                    List<Person> filteredPeople = new ArrayList<>();
                    String constraintName = constraint.toString().replaceAll("\\s", "").toLowerCase();
                    for(Person p : temp) {
                        String friendName = p.getFirstName().concat(p.getLastName()).toLowerCase();
                        if(friendName.contains(constraintName)) {
                            filteredPeople.add(p);
                        }

                        filterResults.values = filteredPeople;
                        filterResults.count = filteredPeople.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results.values != null) {
                    filteredGuests = (List<Person>)results.values;

                    if(BuildConfig.HAS_SPELLING_CORRECTION) {
                        if(filteredGuests.size() == 0 && constraint != null && constraint.length() > 3) {
                            String constraintName = constraint.toString().replaceAll("\\s", "");
                            List<Person> peeps = MisspellingTools.bestMatches(constraintName, persons);
                            int originalLength = peeps.size();
                            int finalIndex = (originalLength <= MAX_RESULTS) ? originalLength : MAX_RESULTS;
                            List<Person> topN = peeps.subList(0, finalIndex);
                            filteredGuests = topN;
                        }
                    }

                    notifyDataSetChanged();
                }
            }
        };
    }
}
