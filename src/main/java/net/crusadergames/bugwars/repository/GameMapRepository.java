package net.crusadergames.bugwars.repository;

import net.crusadergames.bugwars.model.GameMap;
import net.crusadergames.bugwars.model.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameMapRepository extends JpaRepository<GameMap, UUID> {

    Optional<GameMap> findGameMapByName(String name);

    Optional<GameMap> findByNameIgnoreCase(String name);

}
