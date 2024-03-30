package net.crusadergames.bugwars.repository;

import net.crusadergames.bugwars.model.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MapEntityRepository extends JpaRepository<MapEntity, Long> {

    Optional<MapEntity> findByNameIgnoreCase(String name);
}
