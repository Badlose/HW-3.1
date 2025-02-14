package ru.hogwarts.school.Service.Impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.Service.InfoService;

import java.util.stream.LongStream;
import java.util.stream.Stream;

@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public long getFastestAnswer() {
        long start = System.currentTimeMillis();
        Long sumMyBestTime = LongStream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, Long::sum);
        System.out.println(System.currentTimeMillis() - start);

        long myStart = System.currentTimeMillis();
        long sumTimeFromCondition = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        System.out.println(System.currentTimeMillis() - myStart);

        System.out.println(sumMyBestTime);
        System.out.println(sumTimeFromCondition);

        return sumMyBestTime;
    }
}
