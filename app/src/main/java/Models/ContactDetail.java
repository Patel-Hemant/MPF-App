package Models;

import java.util.ArrayList;

public class ContactDetail {
    String Name;
    ArrayList<String> contacts;

    public ContactDetail(String name, ArrayList<String> contacts) {
        Name = name;
        this.contacts = contacts;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }
}
