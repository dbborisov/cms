package cms.api.rest;

import cms.data.models.CertificateDAO;
import cms.data.models.CertificateView;
import cms.services.CertificateService;
import cms.services.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin()
@Controller
@RequestMapping("/cert")
public class CertificateController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final CertificateService certificate;
    private final EmailService emailService;

    public CertificateController(CertificateService certificate, EmailService emailService) {
        this.certificate = certificate;
        this.emailService = emailService;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    Object getAllCertificates() {
        List<CertificateDAO> allCertificates = this.certificate.getAllCertificates();
        this.LOGGER.debug("The list of certificates contains: \n" + allCertificates);
        return ResponseEntity.ok(allCertificates);
    }

    @GetMapping("/exp")
    Object getAllCertificatesExpInSevenDays() {
        List<CertificateView> allCertificates = this.certificate.getCertExpireInOneWeek();
        this.LOGGER.debug("The list of certificates contains: \n" + allCertificates);
        return ResponseEntity.ok(allCertificates);
    }

    @GetMapping("/all/deleted")
    Object getAllDeletedCert() {
        List<CertificateView> allCertificates = this.certificate.getAllDeletedCert();
        this.LOGGER.debug("The list of certificates contains: \n" + allCertificates);
        return ResponseEntity.ok(allCertificates);
    }


    @GetMapping(value = "/edit/{id}")
    Object getById(@PathVariable Long id) {
        CertificateDAO byId = this.certificate.getById(id);
        System.out.println(byId);
        this.LOGGER.debug("The current certificate entry contains: \n" + byId);
        return new ResponseEntity<CertificateDAO>(byId, HttpStatus.OK);
    }

    @GetMapping(value = "/delete/{id}")
    Object deleteById(@PathVariable Long id) {
        boolean byId = this.certificate.deleteById(id);
        this.LOGGER.debug("The current certificate entry deleted : \n" + id + " and id is " + byId);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    //    @PostMapping("/add")
//    Object addCertificate(@RequestParam MultipartFile file,@RequestParam String name){
//
//        System.out.println(file);
//
//        return this.certificate.save(file);
//    }
    @PostMapping("/add")
    Object addViewCertificate(@ModelAttribute CertificateView file) {
        CertificateDAO certificateDAO = new CertificateDAO();
        try {
            certificateDAO = this.certificate.viewAdd(file);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(e);
        }

        return new ResponseEntity<CertificateDAO>(certificateDAO, HttpStatus.OK);
    }

    @PostMapping(value = "/add/confirm")
    Object addCertificate(@ModelAttribute CertificateView file) {
        System.out.println(file);
        CertificateDAO save = this.certificate.save(file);
        return new ResponseEntity<CertificateDAO>(save, HttpStatus.OK);
    }

    @PostMapping("/update")
    Object updateCert(@ModelAttribute CertificateView file) {
        CertificateDAO certificateDAO = new CertificateDAO();
        CertificateDAO update = new CertificateDAO();
        try {
            long id = file.getId();
            file.setId(null);
            certificateDAO = this.certificate.viewAdd(file);
            update = this.certificate.update(id, certificateDAO);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(e);
        }

        return new ResponseEntity<CertificateDAO>(update, HttpStatus.OK);
    }


}
