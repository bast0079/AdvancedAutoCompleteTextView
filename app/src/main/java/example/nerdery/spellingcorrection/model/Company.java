package example.nerdery.spellingcorrection.model;

/**
 * Created by sbastin on 4/4/16.
 */
public class Company extends Searchable {
    String companyName;

    @Override
    void combine() {
        searchableString = companyName.replace(" ", "");
    }
}
