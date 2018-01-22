package chessclub.com.icc.tt;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRating {

    private TacticalTrainerGame tt = new TacticalTrainerGame(null, null, null, null);

    @Test
    public void testRating() {
        assertEquals(1032,tt.ratingChange(1000, 2000, 100, 100, true));
        assertEquals(2000,tt.ratingChange(2000, 1000, 100, 100, true));
        assertEquals(1000,tt.ratingChange(1000, 2000, 100, 100, false));
        assertEquals(1968,tt.ratingChange(2000, 1000, 100, 100, false));
        assertEquals(1968,tt.ratingChange(2000, 1000, 100, 100, false));
        assertEquals(1012,tt.ratingChange(1000, 2000, 100, 7, true));
        assertEquals(1083,tt.ratingChange(1000, 2000, 7, 100, true));
        assertEquals(1988,tt.ratingChange(2000, 1000, 100, 7, false));
    }
}
