package cms.data.entities;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "certificates")
public class CertificateEntity extends BaseEntity {

    private String file_name;
    private String sn;
    private LocalDateTime valide_from;
    private LocalDateTime valide_to;
    private String issuer;
    private String issuer_cn;
    private String subject;
    private String subject_cn;
    @Lob
    private byte[] certificate;
    private String contacts;
    private String description;





}
