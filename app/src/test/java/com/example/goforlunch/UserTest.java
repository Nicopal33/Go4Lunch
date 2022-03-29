package com.example.goforlunch;

import androidx.dynamicanimation.animation.SpringAnimation;

import com.example.goforlunch.adapter.ListRestRecyclerViewAdapter;
import com.example.goforlunch.model.User;
import com.example.goforlunch.utils.PlacesApiService;
import com.google.android.libraries.places.api.model.Place;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserTest {

    @Test
    public void sortUsersTest () {
        final User mUser1 = new User("aa", "aa", "aa", "aa", "",
                new ArrayList<>(), "aa", "aa");
        final User mUser2 = new User("bb", "bb", "bb", "bb", "bb",
                new ArrayList<>(), "bb", "bb");
        final User mUser3 = new User("cc", "cc", "cc", "cc", "cc",
                new ArrayList<>(), "cc", "cc");
        final List<User> users = new ArrayList<>();
        users.add(mUser1);
        users.add(mUser2);
        users.add (mUser3);
        Collections.sort(users, new User.UserRestaurantComparator());
        assertSame(users.get(0), mUser2);
        assertSame(users.get(1), mUser3);
        assertSame(users.get(2), mUser1);
    }

    @Test
    public void meterDistTest() {
        int point = (int) ListRestRecyclerViewAdapter.meterDistanceBetweenPoints
                (44.8429, -0.57296, 43.7423, 0.7436);
        assertEquals(16098.0, point, 0.0);
    }


}