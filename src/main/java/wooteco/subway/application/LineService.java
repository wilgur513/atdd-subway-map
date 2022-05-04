package wooteco.subway.application;

import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.exception.NotExistException;

public class LineService {

    public Line save(String name, String color) {
        if(LineDao.existByName(name)) {
            throw new DuplicateException();
        }
        return LineDao.save(new Line(name, color));
    }

    public Line findById(Long id) {
        return LineDao.findById(id)
                .orElseThrow(NotExistException::new);
    }

    public Line updateById(Long id, String name, String color) {
        return LineDao.update(new Line(id, name, color));
    }
}
