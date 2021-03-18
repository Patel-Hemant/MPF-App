package Models;

import java.io.Serializable;
import java.util.ArrayList;

public class MissingPersonData implements Serializable {
    private String userId;
    private String name;//
    private String age;//
    private String gender;
    private String address;//
    private String missing_date;//
    private String prize;//
    private String contacts;
    private ArrayList<String> photo_urls;
    private String description;//

    public MissingPersonData() {
    }

    public MissingPersonData(String userId, String name, String age, String gender, String address, String missing_date, String prize, String contacts, ArrayList<String> photo_urls, String description) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.missing_date = missing_date;
        this.prize = prize;
        this.photo_urls = photo_urls;
        this.description = description;
        this.contacts = contacts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMissing_date() {
        return missing_date;
    }

    public void setMissing_date(String missing_date) {
        this.missing_date = missing_date;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public ArrayList<String> getPhoto_urls() {
        return photo_urls;
    }

    public void setPhoto_urls(ArrayList<String> photo_urls) {
        this.photo_urls = photo_urls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
}
