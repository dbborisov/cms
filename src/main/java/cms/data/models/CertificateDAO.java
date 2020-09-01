package cms.data.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CertificateDAO {
    private String file_name;
    private String sn;
    private LocalDateTime valide_from;
    private LocalDateTime valide_to;
    private String issuer;
    private String issuer_cn;
    private String subject;
    private String subject_cn;
    private byte[] certificate;
    private String contacts;
    private String description;
}
