package cms.data.repositories;


import cms.data.entities.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateEntity, Long>{

    @Query(
            value = "select * from certificates where valid_to < CURRENT_TIMESTAMP()",
            nativeQuery = true)
    List<CertificateEntity> findAllExpiredCert();

    @Query(
            value = "select  * from CERTIFICATES where DATE_TRUNC( 'day',valid_to) >  CURRENT_DATE and  DATE_TRUNC( 'day',valid_to) = (CURRENT_DATE+7)",
            nativeQuery = true)
    List<CertificateEntity> findAllExpiringCertInSevenDaysOwner();


    @Query(
            value = "select  * from CERTIFICATES where DATE_TRUNC( 'day',valid_to) >  CURRENT_DATE and  DATE_TRUNC( 'day',valid_to) < (CURRENT_DATE+7)",
            nativeQuery = true)
    List<CertificateEntity> findAllExpiringCertInSevenDays();

    @Query(
            value = "select  * from CERTIFICATES where DATE_TRUNC( 'day',valid_to) >  CURRENT_DATE and  DATE_TRUNC( 'day',valid_to) = (CURRENT_DATE+30)",
            nativeQuery = true)
    List<CertificateEntity> findAllExpiringCertInThisMonth();

    @Query(
            value = "select  * from CERTIFICATES where DATE_TRUNC( 'day',valid_to) >  CURRENT_DATE and  DATE_TRUNC( 'day',valid_to) = (CURRENT_DATE+1)",
            nativeQuery = true)
    List<CertificateEntity> getOwnersAllExpiringCertInOneDay();



    CertificateEntity findBySn(String sn);

    CertificateEntity findBySnAndSubject(String sn, String subject);

}
