package net.crusadergames.bugwars.repository;

import net.crusadergames.bugwars.model.GameMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameMapRepository extends JpaRepository<GameMap, Long> {

    Optional<GameMap> findGameMapByName(String name);

    Optional<GameMap> findByNameIgnoreCase(String name);

}
