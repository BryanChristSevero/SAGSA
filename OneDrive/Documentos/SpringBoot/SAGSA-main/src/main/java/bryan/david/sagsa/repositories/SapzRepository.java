package bryan.david.sagsa.repositories;

import bryan.david.sagsa.models.Sapz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SapzRepository extends JpaRepository<Sapz, Long> {
}