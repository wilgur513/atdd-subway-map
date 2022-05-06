package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationResponse;

@Repository
public class StationDao {

    public static final RowMapper<Station> ROW_MAPPER = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Station(id, name);
    };
    private final JdbcTemplate jdbcTemplate;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Station save(Station station) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                .prepareStatement("INSERT INTO STATION(name) VALUES(?)", new String[]{"id"});
            ps.setString(1, station.getName());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM STATION", ROW_MAPPER);
    }

    public Optional<Station> findById(Long id) {
        try {
            Station station = jdbcTemplate
                .queryForObject("SELECT id, name FROM STATION WHERE id = ?", ROW_MAPPER, id);
            return Optional.of(station);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existById(Long id) {
        return findById(id).isPresent();
    }

    public boolean existByName(String name) {
        return jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT id FROM STATION WHERE name = ? LIMIT 1 ) AS `exists`",
            Boolean.class, name);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM STATION WHERE id = ?", id);
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM STATION");
    }

    public List<StationResponse> queryByLineId(Long lineId) {
        String sql = "SELECT distinct s.id as id, s.name as name FROM STATION AS s " +
            "JOIN SECTION AS sec ON s.id = sec.down_station_id OR s.id = sec.up_station_id " +
            "WHERE sec.line_id = ?";

        return jdbcTemplate.query(sql, (rs, rn) -> {
            long stationId = rs.getLong("id");
            String stationName = rs.getString("name");
            return new StationResponse(stationId, stationName);
        }, lineId);
    }
}
