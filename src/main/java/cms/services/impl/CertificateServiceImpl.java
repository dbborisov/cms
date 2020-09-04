package cms.services.impl;

import cms.data.entities.CertificateEntity;
import cms.data.entities.DeleteCertificateEntity;
import cms.data.models.CertificateDAO;
import cms.data.models.CertificateView;
import cms.data.repositories.CertificateRepository;
import cms.data.repositories.DeletedCertificateRepository;
import cms.services.CertificateService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final CertificateRepository certificateRepository;
    private final ModelMapper modelMapper;
    private final DeletedCertificateRepository deletedCertificateRepository;

    public CertificateServiceImpl(CertificateRepository certificateRepository, ModelMapper modelMapper, DeletedCertificateRepository deletedCertificateRepository) {
        this.certificateRepository = certificateRepository;
        this.modelMapper = modelMapper;
        this.deletedCertificateRepository = deletedCertificateRepository;
    }


    @Override
    public List<CertificateDAO> getAllCertificates() {

        return this.certificateRepository.findAll().stream().map(e->this.modelMapper.map(e,CertificateDAO.class)).collect(Collectors.toList());
    }

    @Override
    public CertificateDAO getById(Long id) {
        return this.modelMapper.map(this.certificateRepository.findById(id),CertificateDAO.class);
    }

    @Override
    public CertificateDAO findByParams(String... params) {
        return null;
    }

    @Override
    public CertificateDAO update(CertificateDAO cer) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<CertificateEntity> byId = this.certificateRepository.findById(id);
        byId.get().setId(null);
        DeleteCertificateEntity save = this.deletedCertificateRepository.save(modelMapper.map(byId.get(), DeleteCertificateEntity.class));
        if(save.getValid_from() != null){
            return true;
        }
        return false;
    }

    @Override
    public CertificateDAO save(CertificateView cer) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(cer.getCertificate().getOriginalFilename());

        // Check if the file's name contains invalid characters
        if(fileName.contains("..")) {
            throw new IllegalArgumentException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        CertificateEntity dbFile = certReader(cer.getCertificate());
        this.certificateRepository.save(dbFile);

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

           dbFile.setFile_name(cer.getOriginalFilename());
           dbFile.setIssuer(cert.getIssuerDN().toString());
           dbFile.setIssuer_cn(Arrays.stream(cert.getIssuerDN().toString().split(",")).filter(e->e.split("=")[0].contains("CN"))
                   .collect(Collectors.joining()).replace("CN=",""));
           dbFile.setSubject(cert.getSubjectDN().toString());
           dbFile.setSubject_cn(Arrays.stream(cert.getSubjectDN().toString().split(",")).filter(e->e.split("=")[0].contains("CN"))
                    .collect(Collectors.joining()).replace("CN=",""));
           dbFile.setSn( cert.getSerialNumber().toString());
           dbFile.setValid_from(cert.getNotBefore());
           dbFile.setValid_to(cert.getNotAfter());
//           dbFile.setCertificate(cer.getBytes());
            dbFile.setCertificate(cert.getEncoded());
            is.close();
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }

        return dbFile;
    }
}
