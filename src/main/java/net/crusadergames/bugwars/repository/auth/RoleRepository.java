package net.crusadergames.bugwars.repository.auth;

import net.crusadergames.bugwars.model.auth.ERole;
import net.crusadergames.bugwars.model.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
