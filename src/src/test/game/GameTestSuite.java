package game;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    CoordinateTest.class,
    PlayerTest.class,
    RummiBagTest.class,
    RummiHandTest.class,
    RummiTableTest.class,
    StoneTest.class,
    MoveTraceTest.class,
    RummiGameTest.class
})

public class GameTestSuite {

}
