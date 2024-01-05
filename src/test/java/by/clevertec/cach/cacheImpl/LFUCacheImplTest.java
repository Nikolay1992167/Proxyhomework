package by.clevertec.cach.cacheImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LFUCacheImplTest {

    private LFUCacheImpl<Integer, String> cache;

    @BeforeEach
    void setUp() {
        cache = new LFUCacheImpl<>(3);
        cache.put(1, "first");
        cache.put(2, "second");
        cache.put(3, "third");
    }

    @Test
    void shouldReturnExpectedValueByKey() {
        // given
        String expected = "first";

        // when
        String actual = cache.get(1);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnNullWhenCacheNotContainValueByKey() {
        // given, when
        String actual = cache.get(4);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldRemovedExpectedValueFromMap() {
        // given
        String expected = "fourth";
        cache.put(4, expected);

        // when, then
        assertThat(cache.get(1)).isNull();
        assertThat(cache.get(4)).isEqualTo(expected);
    }

    @Test
    void shouldRemoveValueWithLessFrequency() {
        // given
        cache.get(1);
        cache.get(2);
        cache.put(4, "fourth");

        // when, then
        assertThat(cache.get(3)).isNull();
        cache.put(5, "fifth");
        assertThat(cache.get(4)).isNull();
    }

    @Test
    void shouldReturnDeletedValueWhenCacheContainsExpectedKey() {
        // given
        String expected = "first";

        // when
        String actual = cache.put(1, "fifth");

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnNullWhenCapacityIsLessOrEqualZero() {
        // given
        cache = new LFUCacheImpl<>(0);

        // when
        String actual = cache.put(1, "first");

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldIncrementMinFrequencyInGetMethod() {
        // given
        cache.get(1);
        cache.get(2);
        cache.get(3);

        // when, then
        assertThat(cache.toString()).endsWith("=2)");
    }

    @Test
    void checkToStringMethodStartsWithExpectedValue() {
        // given
        String expected = cache.toString();

        // when, then
        assertThat(expected).startsWith("LFUCacheImpl(capacity=3");
    }

    @Test
    void shouldReturnRemovedValueWhenRemoveByKey() {
        // given
        String expected = "first";

        // when
        String actual = cache.delete(1);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkMethodShouldReturnNullWhenRemoveByKeyNotContain() {
        // given, when
        String actual = cache.delete(4);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldIncrementMinFrequencyInRemoveByKeyMethod() {
        // given, when
        cache.delete(1);
        cache.delete(2);
        cache.delete(3);

        // then
        assertThat(cache.toString()).endsWith("=2)");
    }
}
