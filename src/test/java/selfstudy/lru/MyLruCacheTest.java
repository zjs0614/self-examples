package selfstudy.lru;

import com.zjs.selfstudy.lru.LruCache;
import com.zjs.selfstudy.lru.MockStorage;
import com.zjs.selfstudy.lru.MyLruCache;
import com.zjs.selfstudy.lru.Storage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyLruCacheTest {

    Storage<String, String> storage;
    List<String> keys;

    @Before
    public void beforeClass() {

        this.keys = new ArrayList<String>() {{
            for (int i=0; i<26; i++) {
                String letter = (char)((int)'a' + i) + "";
                this.add(letter);
            }
        }};
        this.storage = new MockStorage<String, String>() {{
            for (String key : keys) {
                put(key, key);
            }
        }};
    }

    @Test
    public void testRetrieveNewObject() {
        int capacity = 5 < this.keys.size() ? 5 : this.keys.size() / 2;
        LruCache<String, String> keyValueCache = new MyLruCache<String, String>(capacity, this.storage);

        for (int i = 0; i < capacity; i++) {
            String key = this.keys.get(i);
            String value = keyValueCache.get(key);
            assertEquals("wrong value", value, key);
            assertTrue("wrong first", ((MyLruCache)keyValueCache).getFirst().getKey().equals(key));
            assertTrue("wrong last", ((MyLruCache)keyValueCache).getLast().getKey().equals(keys.get(0)));
        }

        MyLruCache.CacheNode node = ((MyLruCache)keyValueCache).getFirst();

        for (int i=0; i < capacity; i++) {
            assertTrue(node != null);
            assertEquals(node.getKey(), this.keys.get(capacity - i - 1));
            assertEquals(node.getValue(), this.keys.get(capacity - i - 1));
            assertTrue(((MyLruCache)keyValueCache).getCache().containsKey(node.getKey()));
            assertEquals(((MyLruCache)keyValueCache).getCache().size(), capacity);
            node = node.getNext();
        }


        String key = this.keys.get(capacity);
        String value = keyValueCache.get(key);
        assertEquals("wrong value", value, key);
        assertTrue("wrong first", ((MyLruCache)keyValueCache).getFirst().getKey().equals(key));
        assertTrue("wrong last", ((MyLruCache)keyValueCache).getLast().getKey().equals(keys.get(1)));


        node = ((MyLruCache)keyValueCache).getFirst();

        for (int i=0; i < capacity; i++) {
            assertTrue(node != null);
            assertEquals(node.getKey(), this.keys.get(capacity - i));
            assertEquals(node.getValue(), this.keys.get(capacity - i));
            assertTrue(((MyLruCache)keyValueCache).getCache().containsKey(node.getKey()));
            assertEquals(((MyLruCache)keyValueCache).getCache().size(), capacity);
            node = node.getNext();
        }
    }

}
