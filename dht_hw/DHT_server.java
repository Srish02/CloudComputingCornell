import java.io.BufferedReader;
import java.util.Map;
import java.util.TreeMap;
import java.io.IOException;

import java.util.concurrent.ConcurrentHashMap;

public class DHT_server {
    public static ConcurrentHashMap<Long, TreeMap<Integer,byte[]>> distributedHashTable;
    public int choice;
    String keyValEntered = null;
    int versionNum;
    int nServers;

    private static final long[] byteTable = createLookupTable();
    private static final long HSTART = 0xBB40E64DA205B064L;
    private static final long HMULT = 7664345821815920749L;

    private boolean loop = true;
    
    public DHT_server() {
        distributedHashTable = new ConcurrentHashMap<>();
        versionNum = 0;
        nServers = 0;
    }

    //test code
    // public void run() {
    //     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    //     while (loop) {
    //         switch (choice) {
    //             case 1:
    //                 // client-value pair from the client
    //                 System.out.println("Enter the key and value pair to register: ");
    //                 try {
    //                     keyValEntered = reader.readLine();
    //                 } catch (IOException e) {
    //                     e.printStackTrace();
    //                 }
    //                 String[] hashKey = keyValEntered.split(";");
    //                 long hashVal = hash(hashKey[0]);
    //         }
    //     }
    // }

    private static long[] createLookupTable() {
        long[] byteTable = new long[256];
        long h = 0x544B2FBACAAF1684L;
        for (int i = 0; i < 256; i++) {
          for (int j = 0; j < 31; j++) {
            h = (h >>> 7) ^ h;
            h = (h << 11) ^ h;
            h = (h >>> 10) ^ h;
          }
          byteTable[i] = h;
        }
        return byteTable;
    }

    public static long hash(CharSequence cs) {
        long h = HSTART;
        final long hmult = HMULT;
        final long[] ht = byteTable;
        final int len = cs.length();
        for (int i = 0; i < len; i++) {
          char ch = cs.charAt(i);
          h = (h * hmult) ^ ht[ch & 0xff];
          h = (h * hmult) ^ ht[(ch >>> 8) & 0xff];
        }
        return h;
    }

    public synchronized int put(Long key, byte[] value) {

        if(!distributedHashTable.containsKey(key)) {
            distributedHashTable.put(key,new TreeMap<>());
        }

        distributedHashTable.get(key).put(++versionNum, value);
        System.out.println("PUT" + "(" + versionNum + ") " + key + " = " + value);
        return versionNum;
    }

    public byte[] get(Long key){

		if(!distributedHashTable.containsKey(key)) return null;

        TreeMap<Integer, byte[]> temp = new TreeMap<>();
        temp = distributedHashTable.get(key);
        byte[] val = temp.lastEntry().getValue();

        System.out.println("GET " + key + " = " + val);
        return val;
	}

    public synchronized boolean del(Long key)
	{
		if(distributedHashTable.remove(key,distributedHashTable.get(key)))
			return true;
		else
			return false;
	}

    public byte[] get(Long key, int vNum) {

        if(!distributedHashTable.containsKey(key))  return null;

        TreeMap<Integer, byte[]> temp = new TreeMap<>();
        temp = distributedHashTable.get(key);
        byte[] val;

        if(temp.containsKey(vNum)) {
            val = temp.get(vNum);
        } else {
            Map.Entry<Integer, byte[]> e = temp.lowerEntry(vNum);
            if(e == null) {
                return null;
            } else {
                val = e.getValue();
            }
        }
    
        System.out.println("GET " + key + "("+vNum+") = " + val);
        return val;
    }

}