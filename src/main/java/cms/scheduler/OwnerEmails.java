package cms.scheduler;

import cms.data.models.CertificateView;
import cms.services.CertificateService;
import cms.services.EmailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@Component
@ConditionalOnProperty(name = "spring.enable.auto.send.cert.notification")
public class OwnerEmails {

    private final EmailService emailService;
    private final CertificateService certificateService;


    public OwnerEmails(EmailService emailService, CertificateService certificateService) {
        this.emailService = emailService;
        this.certificateService = certificateService;
    }

    @Scheduled(cron="${cron.time.check.cert.exp.in.one.day}", zone="Europe/Sofia")
    public  void sendMailsToCertOwnersOnExpirationDay(){
        int days = 1;
        List<CertificateView> certExpireInOneWeek = this.certificateService.getCertExpireInOneDay();

        mailer(days, certExpireInOneWeek);
    }
    @Scheduled(cron="${cron.time.check.cert.exp.in.seven.day}", zone="Europe/Sofia")
    public  void  sendMailsToCerOwnersOnExpirationSevenDays(){
        int days = 7;
        List<CertificateView> certExpireInOneWeek = this.certificateService.getCertExpireInOneWeekOwner();

        mailer(days, certExpireInOneWeek);


    }
    @Scheduled(cron="${cron.time.check.cert.exp.in.month}", zone="Europe/Sofia")
    public  void  sendMailsToCerOwnersOnExpirationOneMonth(){
        int days = 30;
        List<CertificateView> certExpireInOneWeek =this.certificateService.getCertExpireInOneMonth();
        mailer(days, certExpireInOneWeek);
    }

    private void mailer(int days, List<CertificateView> certExpire) {
        for (int i = 0; i < certExpire.size(); i++) {

            String[] contacts = certExpire.get(i).getContacts().split(",");
            if (contacts.length > 0){
                for (int j = 0; j < contacts.length; j++) {
                    if (!contacts[j].isEmpty()){
                        CertificateView certificateView = certExpire.get(i);
                        StringBuilder builder = new StringBuilder();
                        builder.append("Сертификат с сериен номер : ")
                                .append(certificateView.getSn())
                                .append(" и SubjectCN : ")
                                .append(certificateView.getSubject_cn())
                                .append(" изтича след по малко от ")
                                .append(days)
                                .append(" дни!");
                        builder.append("За повече информация свържете се с infosec@borica.bg");


                        this.emailService.sendSimpleMessage(contacts[j],"Изтичащ сертификат",builder.toString());
                    }
                }
            }

        }
    }


}
