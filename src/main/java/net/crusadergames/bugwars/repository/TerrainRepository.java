package net.crusadergames.bugwars.repository;

import net.crusadergames.bugwars.model.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TerrainRepository extends JpaRepository<Terrain, Long> {

    Optional<Terrain> findByNameIgnoreCase(String name);
}
