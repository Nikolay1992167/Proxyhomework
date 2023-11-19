package by.clevertec.cach.cacheImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LRUCacheImplTest {

    @InjectMocks
    private LRUCacheImpl<Integer, String> cache;

    @Test
    void shouldValuePutAndGet() {
        // given
        int key = 1;
        String expected = "Actual value";
        cache.put(key, expected);

        // when
        String actual = cache.get(1);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldUpdateValue() {
        // given
        int key = 1;
        String basic = "Line";
        String actual = "Update line";

        cache.put(key, basic);
        cache.put(key, actual);

        // when
        String expected = cache.get(key);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testDelete() {
        // given
        int key = 1;
        String actual = "Actual value";
        cache.put(key, actual);

        // when
        cache.delete(key);
        String expected = cache.get(key);

        // then
        assertThat(expected).isNull();
    }

    @Test
    void shouldDeleteFirstInLine() {
        // given
        int key = 0;
        String value = "Value:";
        for (int i = 0; i < 10; i++) {
            cache.put(key + i, value + i);
        }

        // when
        cache.put(10, "Value:10");

        // then
        assertThat(cache.get(0)).isNull();
    }
}