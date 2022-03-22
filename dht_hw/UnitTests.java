import static org.junit.Assert.*;
import org.junit.Test;

public class UnitTests {
    
    @Test
    public void testPut() {

        DHT_server testDHT = new DHT_server();

        assertEquals("error in PUT", 1, testDHT.put(Long("910"), "40".getBytes()));
        assertEquals("error in PUT", 2,testDHT.put(Long("911"), "60".getBytes()));
        assertEquals("error in PUT", 3,testDHT.put(Long("912"), "70".getBytes()));
        assertEquals("error in PUT", 4,testDHT.put(Long("913"), "80".getBytes()));
        assertEquals("error in PUT", 5,testDHT.put(Long("990"), "90".getBytes()));
    }

    @Test
    public void testGet() {

        DHT_server testDHT = new DHT_server();

        assertEquals("error in Get", null, testDHT.get(Long("910")));
        assertEquals("error in Get", null,testDHT.get(Long("911")));
        assertEquals("error in Get", null,testDHT.get(Long("912")));
        assertEquals("error in Get", null,testDHT.get(Long("913")));
        assertEquals("error in Get", null,testDHT.get(Long("990")));
    }

    private static Long Long(String key) {
        return Long.parseLong(key);
    }

}
