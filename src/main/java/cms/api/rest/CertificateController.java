package cms.api.rest;

import cms.data.models.CertificateDAO;
import cms.data.models.CertificateView;
import cms.services.CertificateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/cert")
public class CertificateController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final CertificateService certificate;

    public CertificateController(CertificateService certificate) {
        this.certificate = certificate;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    Object getAllCertificates() {
        List<CertificateDAO> allCertificates = this.certificate.getAllCertificates();
        this.LOGGER.debug("The list of certificates contains: \n" + allCertificates);
        return ResponseEntity.ok(allCertificates);
    }

    @GetMapping(value = "/edit/{id}")
    Object getById(@PathVariable Long id) {
        CertificateDAO byId = this.certificate.getById(id);
        this.LOGGER.debug("The current certificate entry contains: \n" + byId);
        return new ResponseEntity<CertificateDAO>(byId, HttpStatus.OK);
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
        CertificateDAO certificateDAO = null;
        try {
            certificateDAO = this.certificate.viewAdd(file);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(e);
        }

        return new ResponseEntity<CertificateDAO>(certificateDAO, HttpStatus.OK) ;
    }

    @PostMapping(value = "/add/confirm")
    Object addCertificate(@ModelAttribute CertificateView file) {
        System.out.println();
        CertificateDAO save = this.certificate.save(file);
        return new ResponseEntity<CertificateDAO>(save, HttpStatus.OK);
    }


}
