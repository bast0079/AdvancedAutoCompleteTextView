package example.nerdery.spellingcorrection.model;

/**
 * Created by sbastin on 4/4/16.
 */
public class Person extends Searchable {
    String firstName;
    String lastName;

    @Override
    void combine() {
        StringBuilder sb = new StringBuilder()
                .append(firstName.toLowerCase())
                .append(lastName.toLowerCase());
        searchableString = sb.toString();
    }
}
