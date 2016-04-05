package example.nerdery.spellingcorrection.model;

/**
 * Created by sbastin on 4/4/16.
 */
public class Person  {
    private StringBuilder sb = new StringBuilder();
    String firstName;
    String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        sb.append(firstName).append(" ").append(lastName);
        return sb.toString();
    }
}
