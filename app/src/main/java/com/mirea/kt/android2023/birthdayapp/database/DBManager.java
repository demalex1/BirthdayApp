package com.mirea.kt.android2023.birthdayapp.database;

import com.mirea.kt.android2023.birthdayapp.model.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DBManager {

    private Realm realm;
    private static long realmVersion = 1L;

    public DBManager() {
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("my_app_db")
                .schemaVersion(realmVersion)
                .allowWritesOnUiThread(true)
                .build();

        this.realm = Realm.getInstance(configuration);
    }

    public void addPerson(Person person) {
        realm.beginTransaction();
        realm.insert(person);
        realm.commitTransaction();
    }

    public Person getPerson(String name) {
        return realm.where(Person.class).equalTo("name", name).findFirst();
    }

    public List<Person> getAllPersons() {
        RealmResults<Person> results = realm.where(Person.class).findAll();
        return new ArrayList<>(realm.copyFromRealm(results));
    }

    public void deletePerson(String name) {
        Person person = getPerson(name);

        realm.beginTransaction();
        person.deleteFromRealm();
        realm.commitTransaction();
    }

    public void updatePersonDate(String name, Date birthday) {
        Person person = getPerson(name);

        realm.beginTransaction();
        person.setDateOfBirth(birthday);
        realm.commitTransaction();
    }
}
