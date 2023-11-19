package by.clevertec.cach.cacheImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LFUCacheImplTest {

    @InjectMocks
    private LFUCacheImpl<Integer, String> cache;

    @Test
    void shouldReturnNullWhenKeyDoesNotExist() {
        // given
        int key = 1;

        // when
        String actual = cache.get(key);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnValueWhenKeyExists() {
        // given
        int key = 1;
        String expected = "Expected value";
        cache.put(key, expected);

        // when
        String actual = cache.get(key);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldUpdateValueWhenKeyExists() {
        // given
        int key = 1;
        String actual = "Actual value";
        String expected = "Expected value";
        cache.put(key, actual);

        // when
        cache.put(key, expected);

        // then
        assertThat(cache.get(key)).isEqualTo(expected);
    }

    @Test
    void shouldRemoveValueWhenKeyExists() {
        // given
        int key = 1;
        String actual = "Actual value";
        cache.put(key, actual);

        // when
        cache.delete(key);

        // then
        assertThat(cache.get(key)).isNull();
    }
}
