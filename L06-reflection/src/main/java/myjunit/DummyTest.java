package myjunit;

import myjunit.annotations.After;
import myjunit.annotations.Before;
import myjunit.annotations.Test;

public class DummyTest {

    @Before
    public void firstBefore()
    {
        System.out.println("executed BEFORE method");
    }

    @Before
    public void secondBefore()
    {
        System.out.println("executed  second BEFORE method");
    }

    @Test
    public void firstTest()
    {
        System.out.println("executed FIRST TEST method");
    }

    @Test
    public void secondTest()
    {
        System.out.println("executed SECOND TEST method");
    }

    @Test
    public void thirdTest()
    {
        throw new RuntimeException("What's the hell?!");
    }

    @After
    public void onlyAfter()
    {
        System.out.println("executed AFTER method");
    }
}
