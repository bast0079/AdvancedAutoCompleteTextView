package example.nerdery.spellingcorrection.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

import example.nerdery.spellingcorrection.model.Searchable;

/**
 * Created by sbastin on 4/4/16.
 */
public class SearchAdapter extends ArrayAdapter<Searchable> implements Filterable{
    public SearchAdapter(Context context, int resource) {
        super(context, resource);
    }


}
