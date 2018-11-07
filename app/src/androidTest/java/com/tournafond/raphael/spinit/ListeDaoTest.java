package com.tournafond.raphael.spinit;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.database.SpinItDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ListeDaoTest {
    // FOR DATA
    private SpinItDatabase database;

    // FOR TEST
    final static ArrayList<String> ACTION_LIST_1 = new ArrayList<String>() {{
        add("ONE");
        add("TWO");
        add("THREE");
        add("FOUR");
    }};
    final static ArrayList<String> PART_LIST_1 = new ArrayList<String>() {{
        add("PIERRE");
        add("PAUL");
        add("JACQUES");
    }};
    final static ArrayList<String> ACTION_LIST_2 = new ArrayList<String>() {{
        add("Manger une veste");
        add("Crier \"au secours !\"");
        add("Sauter dans une flaque d'eau");
        add("Dire \"et mercé !\"");
        add("Crier \"Pastèque !\"");
    }};
    private static Liste NEW_LIST_NORMAL_WITHOUT_PART = new Liste(Liste.NORMAL, "Une liste normale", ACTION_LIST_1, new ArrayList<String>());
    private static Liste NEW_LIST_BONUS = new Liste(Liste.BONUS, "Une liste normale", ACTION_LIST_1, PART_LIST_1);
    private static Liste NEW_LIST_FAVORITE = new Liste(Liste.BONUS, "Une liste normale", ACTION_LIST_2, PART_LIST_1);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                SpinItDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void getItemsWhenNoItemInserted() throws InterruptedException {
        // TEST
        List<Liste> listes = LiveDataTestUtil.getValue(this.database.listeDao().getListes());
        assertTrue(listes.isEmpty());
    }

    @Test
    public void insertAndGetItems() throws InterruptedException {
        // BEFORE : Adding demo user & demo items

        this.database.listeDao().insertListe(NEW_LIST_NORMAL_WITHOUT_PART);
        this.database.listeDao().insertListe(NEW_LIST_BONUS);
        this.database.listeDao().insertListe(NEW_LIST_FAVORITE);

        // TEST
        List<Liste> listes = LiveDataTestUtil.getValue(this.database.listeDao().getListes());
        assertTrue(listes.size() == 3);
    }

    @Test
    public void insertAndUpdateItem() throws InterruptedException {
        // BEFORE : Adding demo user & demo items. Next, update item added & re-save it
        this.database.listeDao().insertListe(NEW_LIST_NORMAL_WITHOUT_PART);
        Liste listeAdded = LiveDataTestUtil.getValue(this.database.listeDao().getListes()).get(0);
        listeAdded.setType(Liste.FAVORI);
        this.database.listeDao().updateListe(listeAdded);

        //TEST
        List<Liste> listes = LiveDataTestUtil.getValue(this.database.listeDao().getListes());
        assertTrue(listes.size() == 1 && listes.get(0).getType() == Liste.FAVORI);
    }

    @Test
    public void insertAndDeleteItem() throws InterruptedException {
        // BEFORE : Adding demo user & demo item. Next, get the item added & delete it.
        this.database.listeDao().insertListe(NEW_LIST_FAVORITE);
        Liste listeAdded = LiveDataTestUtil.getValue(this.database.listeDao().getListes()).get(0);
        this.database.listeDao().deleteListe(listeAdded.getId());

        //TEST
        List<Liste> listes = LiveDataTestUtil.getValue(this.database.listeDao().getListes());
        assertTrue(listes.isEmpty());
    }
}
