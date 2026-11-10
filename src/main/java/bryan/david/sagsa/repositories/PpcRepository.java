package bryan.david.sagsa.repositories;

import bryan.david.sagsa.models.Ppc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PpcRepository extends JpaRepository<Ppc, Long> {
}