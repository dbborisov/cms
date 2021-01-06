package cms.data.repositories;


import cms.data.entities.CertificateViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateViewRepository extends JpaRepository<CertificateViewEntity, Long> {
}
