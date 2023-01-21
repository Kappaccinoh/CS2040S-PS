import static org.junit.Assert.*;

public class WiFiTest {

    @org.junit.Test
    public void computeDistance() {
        int[] houses = {1, 3, 10};
        int numAccessPoints = 2;
        assertEquals(1.0, WiFi.computeDistance(houses, numAccessPoints), 0.5);
    }

    @org.junit.Test
    public void computeDistance1() {
        int[] houses = {8,2,9,6,5,4,10};
        int numAccessPoints = 2;
        System.out.println(WiFi.computeDistance(houses, numAccessPoints));
    }

    @org.junit.Test
    public void computeDistance2() {
        int[] houses = {1,10,20};
        int numAccessPoints = 3;
        System.out.println(WiFi.computeDistance(houses, numAccessPoints));
    }

    @org.junit.Test
    public void computeDistance3() {
        int[] houses = {};
        int numAccessPoints = 2;
        System.out.println(WiFi.computeDistance(houses, numAccessPoints));
    }

    // how to handle properly?
    @org.junit.Test
    public void computeDistance4() {
        int[] houses = {1,10,20};
        int numAccessPoints = 0;
        System.out.println(WiFi.computeDistance(houses, numAccessPoints));
    }

    @org.junit.Test
    public void computeDistance5() {
        int[] houses = {1,10,20};
        int numAccessPoints = 1;
        System.out.println(WiFi.computeDistance(houses, numAccessPoints));
    }

    @org.junit.Test
    public void coverable1() {
        int[] houses = {1, 3, 10};
        int numAccessPoints = 2;
        assertTrue(WiFi.coverable(houses, numAccessPoints, 1.0));
    }

    @org.junit.Test
    public void coverable2() {
        int[] houses = {1, 3, 10};
        int numAccessPoints = 2;
        assertFalse(WiFi.coverable(houses, numAccessPoints, 0.5));
    }

    @org.junit.Test
    public void coverable3() {
        int[] houses = {1,2,3,4,5,20};
        int numAccessPoints = 2;
        assertTrue(WiFi.coverable(houses, numAccessPoints, 2));
    }

    @org.junit.Test
    public void coverable4() {
        int[] houses = {20,1,2,3,4,5};
        int numAccessPoints = 2;
        assertTrue(WiFi.coverable(houses, numAccessPoints, 2));
    }

    @org.junit.Test
    public void coverable5() {
        int[] houses = {20,1,2,3,4,5};
        int numAccessPoints = 2;
        assertFalse(WiFi.coverable(houses, numAccessPoints, 0));
    }

    @org.junit.Test
    public void coverable6() {
        int[] houses = {};
        int numAccessPoints = 2;
        assertTrue(WiFi.coverable(houses, numAccessPoints, 0));
    }

    @org.junit.Test
    public void coverable7() {
        int[] houses = {1,2,3,10,11,12,20,21,22};
        int numAccessPoints = 3;
        assertTrue(WiFi.coverable(houses, numAccessPoints, 1));
    }

    @org.junit.Test
    public void coverable8() {
        int[] houses = {1, 2, 3, 10, 11, 12, 20, 21, 22};
        int numAccessPoints = 3;
        assertFalse(WiFi.coverable(houses, numAccessPoints, 0.9));
    }
}