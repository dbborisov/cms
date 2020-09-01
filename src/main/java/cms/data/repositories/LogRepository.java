package cms.data.repositories;

import cms.data.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {

    Optional<LogEntity> findLogByUsername(String username);

}
