package cms.data.repositories;


import cms.data.entities.CertificateEntity;
import cms.data.entities.DeleteCertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletedCertificateRepository extends JpaRepository<DeleteCertificateEntity, Long>{
}
