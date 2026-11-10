package bryan.david.sagsa.repositories;

import bryan.david.sagsa.models.Capacidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapacidadeRepository extends JpaRepository<Capacidade, Long> {
}