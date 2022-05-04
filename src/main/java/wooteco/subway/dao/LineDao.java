package wooteco.subway.dao;

import org.springframework.util.ReflectionUtils;
import wooteco.subway.domain.Line;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LineDao {

    private static Long seq = 0L;
    private static final List<Line> lines = new ArrayList<>();

    public static Line save(Line line) {
        Line persistLine = createNewObject(line);
        lines.add(persistLine);
        return persistLine;
    }

    private static Line createNewObject(Line line) {
        Field field = ReflectionUtils.findField(Line.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, line, ++seq);
        return line;
    }

    public static Optional<Line> findById(Long id) {
        return lines.stream()
                .filter(it -> it.getId().equals(id))
                .findAny();
    }

    public static List<Line> findAll() {
        return lines;
    }

    public static boolean existById(Long id) {
        return lines.stream()
                .anyMatch(it -> it.getId().equals(id));

    }

    public static boolean existByName(String name) {
        return lines.stream()
                .anyMatch(it -> it.getName().equals(name));
    }

    public static Line update(Line line) {
        Line findLine = findById(line.getId()).orElseThrow();
        lines.remove(findLine);
        lines.add(line);
        return line;
    }

    public static void deleteById(Long id) {
        Line findLine = findById(id).orElseThrow();
        lines.remove(findLine);
    }

    public static void deleteAll() {
        lines.clear();
    }
}
