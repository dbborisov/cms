package cms.services;

import cms.data.models.CertificateDAO;
import cms.data.models.CertificateView;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CertificateService {

    List<CertificateDAO> getAllCertificates();
    CertificateDAO getById(Long id);
    CertificateDAO findByParams(String... params);
    CertificateDAO update(long id, CertificateDAO cer);
    boolean deleteById (Long id);
    CertificateDAO save(CertificateView cer);
    CertificateDAO viewAdd(CertificateView cer);
    List<CertificateView>  getCertExpires();
    List<CertificateView>  getCertExpireInOneDay();
    List<CertificateView>  getCertExpireInOneWeek();
    List<CertificateView>  getCertExpireInOneMonth();
    CertificateDAO getBySn(String sn);
    List<CertificateView> getCertExpireInOneWeekOwner();

    List<CertificateView>  getAllDeletedCert();
    boolean  compeerForExistingCert(CertificateDAO cert);

}
