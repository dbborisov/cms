package cms.scheduler;

import cms.data.models.CertificateDAO;
import cms.data.models.CertificateView;
import cms.services.CertificateService;
import cms.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.List;


@Configuration
@Component
@ConditionalOnProperty(name = "spring.enable.auto.check.cert")
public class DailyOperations {

    @Autowired
    private final EmailService emailService;
    private final CertificateService certificateService;

    @Value("${spring.mail.send.to}")
    private String sendTo;

    private String subject;

    public DailyOperations(EmailService emailService, CertificateService certificateService) {
        this.emailService = emailService;
        this.certificateService = certificateService;
    }


//    @ConditionalOnProperty(name = "spring.enable.auto.check.cert")
    @Scheduled(cron="${cron.time.check.cert.exp}", zone="Europe/Sofia")
    public void cert_exp_check(){

        List<CertificateView> allCertificates = this.certificateService.getCertExpires();
        this.subject = "Изтекли сертификати!";
        sender(subject,sendTo,builder(allCertificates,
                "Брой :\n").toString());
    }

//    @ConditionalOnProperty(name = "spring.enable.auto.check.cert")
    @Scheduled(cron="${cron.time.check.cert.exp.in.seven.days}", zone="Europe/Sofia")
    public void cert_will_exp_in_seven_days() {
        List<CertificateView> allCertificates = this.certificateService.getCertExpireInOneWeek();
        this.subject = "Изтичащи сертификати след седем дни!";
        sender(subject,sendTo,builder(allCertificates,
                "Брой Изтичащи сертификати :\n").toString());
    }

    @Scheduled(cron="${cron.time.check.cert.exp.in.thirty.days}", zone="Europe/Sofia")
    public void cert_will_exp_in_thirty_days() {
        List<CertificateView> allCertificates = this.certificateService.getCertExpireInOneMonth();
        this.subject = "Изтичащи сертификати след тийсет дни!";
        sender(this.subject,this.sendTo,builder(allCertificates,
                "Брой Изтичащи сертификати за следващите трисет дни:\n").toString());

//        dborisov@borica.bg
    }

    private void  sender(String subject, String sendTo,String data){
        if(data.length()>70) {
            this.emailService.sendSimpleMessage(sendTo,subject, data);
        }
    }

    private StringBuilder builder( List<CertificateView> list, String text){
        StringBuilder builder = new StringBuilder();
        builder.append(text).append(list.size() );
        builder.append(System.lineSeparator());
        list.forEach(е-> builder.append(е.toString()));
        builder.append(System.lineSeparator());
        return builder;
    }

}
