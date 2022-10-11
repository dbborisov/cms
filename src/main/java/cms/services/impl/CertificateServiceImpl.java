package cms.services.impl;

import cms.data.entities.CertificateEntity;
import cms.data.entities.CertificateViewEntity;
import cms.data.entities.DeleteCertificateEntity;
import cms.data.models.CertificateDAO;
import cms.data.models.CertificateView;
import cms.data.repositories.CertificateRepository;
import cms.data.repositories.CertificateViewRepository;
import cms.data.repositories.DeletedCertificateRepository;
import cms.services.CertificateService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final CertificateRepository certificateRepository;
    private final CertificateViewRepository certificateViewRepository;
    private final ModelMapper modelMapper;
    private final DeletedCertificateRepository deletedCertificateRepository;

    public CertificateServiceImpl(CertificateRepository certificateRepository, CertificateViewRepository certificateViewRepository, ModelMapper modelMapper, DeletedCertificateRepository deletedCertificateRepository) {
        this.certificateRepository = certificateRepository;
        this.certificateViewRepository = certificateViewRepository;
        this.modelMapper = modelMapper;
        this.deletedCertificateRepository = deletedCertificateRepository;
    }


    @Override
    public List<CertificateDAO> getAllCertificates() {

        return this.certificateRepository.findAll().stream().map(e->this.modelMapper.map(e,CertificateDAO.class)).collect(Collectors.toList());
    }

    @Override
    public CertificateDAO getById(Long id) {
        Optional<CertificateEntity> byId = this.certificateRepository.findById(id);
        CertificateDAO dao = this.modelMapper.map(byId.get(), CertificateDAO.class);
        System.out.println(dao);
        return dao;
    }

    @Override
    public CertificateDAO findByParams(String... params) {
        return null;
    }

    @Override
    public CertificateDAO update(long id,CertificateDAO cer) {

        CertificateEntity oldCert = this.certificateRepository.findById(id).get();
        CertificateViewEntity newCert = this.certificateViewRepository.findById(cer.getId()).get();

        newCert.setContacts(oldCert.getContacts());
        newCert.setCertificate_owner(oldCert.getCertificate_owner());
        newCert.setDescription(oldCert.getDescription());
        newCert.setImportance(oldCert.getImportance());
        newCert.setResponsible_for_implementation(oldCert.getResponsible_for_implementation());
        newCert.setUpdated(true);
        newCert.setUsed_for(oldCert.getUsed_for());

        this.certificateRepository.save(modelMapper.map(newCert,CertificateEntity.class));
        this.deleteById(oldCert.getId());
        this.certificateRepository.flush();

        return this.modelMapper.map(newCert,CertificateDAO.class);
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<CertificateEntity> byId = this.certificateRepository.findById(id);
//        byId.get().setId(null);
        this.certificateRepository.deleteById(id);
        DeleteCertificateEntity save = this.deletedCertificateRepository.save(modelMapper.map(byId.get(), DeleteCertificateEntity.class));
        if(save.getValid_from() != null){
            return true;
        }
        return false;
    }

    @Override
    public CertificateDAO save(CertificateView cer) {
        CertificateEntity save = new CertificateEntity();
        Optional<CertificateViewEntity> byId = this.certificateViewRepository.findById(cer.getId());
        if(byId.isPresent()){
            CertificateDAO fromDbView = mappfromView(modelMapper.map(byId.get(), CertificateDAO.class), cer);
            CertificateEntity dbFile =this.modelMapper.map(mappfromView(fromDbView,cer),CertificateEntity.class);
             save = this.certificateRepository.save(dbFile);
        }



        return this.modelMapper.map(save,CertificateDAO.class);
    }

    @Override
    public CertificateDAO viewAdd(CertificateView cer) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(cer.getCertificate().getOriginalFilename());

        // Check if the file's name contains invalid characters
        if(fileName.contains("..")) {
            throw new IllegalArgumentException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        CertificateViewEntity dbFile = modelMapper.map(certReader(cer.getCertificate()),CertificateViewEntity.class);

        boolean certificateView = this.compeerForExistingCert(this.modelMapper.map(dbFile, CertificateDAO.class));
        if(certificateView){
            throw  new IllegalArgumentException("Certificate already exist!!!");
        }

        if(new Date().compareTo(dbFile.getValid_to()) >= 0){
            throw new IllegalArgumentException("Invalid Expiry date!!!");
        }
        CertificateViewEntity save = this.certificateViewRepository.save(dbFile);
        dbFile.setId(save.getId());
//        System.out.println(cer);

//            return dbFileRepository.save(dbFile);
        return this.modelMapper.map(dbFile,CertificateDAO.class);
    }


    private CertificateEntity certReader(MultipartFile cer){
        CertificateEntity dbFile = new CertificateEntity();
        try {
            CertificateFactory fac = CertificateFactory.getInstance("X509");
            InputStream is = cer.getInputStream();
            X509Certificate cert = (X509Certificate) fac.generateCertificate(is);
            //bypass the non object to db
            dbFile.setSubjectAlternativeName("");
            System.out.println(cert.getSerialNumber().toString(16));

           dbFile.setFile_name(cer.getOriginalFilename());
           dbFile.setIssuer(cert.getIssuerDN().toString());
           dbFile.setIssuer_cn(Arrays.stream(cert.getIssuerDN().toString().split(",")).filter(e->e.split("=")[0].contains("CN"))
                   .collect(Collectors.joining()).replace("CN=",""));
           dbFile.setSubject(cert.getSubjectDN().toString());
           dbFile.setSubject_cn(Arrays.stream(cert.getSubjectDN().toString().split(",")).filter(e->e.split("=")[0].contains("CN"))
                    .collect(Collectors.joining()).replace("CN=",""));

           if(cert.getSubjectAlternativeNames() != null) {
               dbFile.setSubjectAlternativeName(cert.getSubjectAlternativeNames().stream().map(e-> e.get(1).toString()).collect(Collectors.joining(",")));
           }

           dbFile.setSn( cert.getSerialNumber().toString(16));
           dbFile.setValid_from(cert.getNotBefore());
           dbFile.setValid_to(cert.getNotAfter());
//           dbFile.setCertificate(cer.getBytes());
            dbFile.setCertificate(cert.getEncoded());
            dbFile.setContacts(Arrays.stream(cert.getSubjectDN().toString().split(",")).filter(e->e.split("=")[0].contains("EMAILADDRESS"))
                    .collect(Collectors.joining()).replace("EMAILADDRESS=",""));
            is.close();
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }

        return dbFile;
    }

    private CertificateDAO mappfromView( CertificateDAO dao, CertificateView cer){
        dao.setId(cer.getId());
        dao.setCertificate_owner(cer.getCertificate_owner());
        dao.setContacts(cer.getContacts());
        dao.setImportance(cer.getImportance());
        dao.setDescription(cer.getDescription());
        dao.setResponsible_for_implementation(cer.getResponsible_for_implementation());
        dao.setUsed_for(cer.getUsed_for());

        return dao;

    }


    @Override
    public List<CertificateView> getCertExpires() {
        return this.certificateRepository.findAllExpiredCert().stream().map(e->this.modelMapper.map(e,CertificateView.class)).collect(Collectors.toList());

    }

    @Override
    public List<CertificateView> getCertExpireInOneDay() {
        return this.certificateRepository.getOwnersAllExpiringCertInOneDay().stream().map(e->this.modelMapper.map(e,CertificateView.class)).collect(Collectors.toList());

    }

    @Override
    public List<CertificateView> getCertExpireInOneWeek() {
         return this.certificateRepository.findAllExpiringCertInSevenDays().stream().map(e->this.modelMapper.map(e,CertificateView.class)).collect(Collectors.toList());

    }

    @Override
    public List<CertificateView> getCertExpireInOneWeekOwner() {
        return this.certificateRepository.findAllExpiringCertInSevenDaysOwner().stream().map(e->this.modelMapper.map(e,CertificateView.class)).collect(Collectors.toList());

    }

    @Override
    public List<CertificateView> getCertExpireInOneMonth() {
        return this.certificateRepository.findAllExpiringCertInThisMonth().stream().map(e->this.modelMapper.map(e,CertificateView.class)).collect(Collectors.toList());

    }

    @Override
    public CertificateDAO getBySn(String sn) {
        return this.modelMapper.map(this.certificateRepository.findBySn(sn), CertificateDAO.class);
    }

    @Override
    public List<CertificateView> getAllDeletedCert() {
        return this.deletedCertificateRepository.findAll().stream().map(e->this.modelMapper.map(e,CertificateView.class)).collect(Collectors.toList());
    }

    @Override
    public boolean compeerForExistingCert(CertificateDAO cert) {
        String subject = cert.getSubject();
        String subject_cn = cert.getSubject_cn();
        String sn = cert.getSn();
        CertificateEntity bySnAndSubject= this.certificateRepository.findBySnAndSubject(sn, subject);
        if(bySnAndSubject != null){
            return true;
        }

        return false ;
    }
}
