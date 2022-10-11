package cms.data.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CertificateDAO {
    private Long id;
    private String file_name;
    private String sn;
    private Date valid_from;
    private Date valid_to;
    private String issuer;
    private String issuer_cn;
    private String subject;
    private String subjectAlternativeName;
    private String subject_cn;
    private byte[] certificate;
    private String contacts;
    private String description;
    private boolean updated;
    private String  importance;
    private String used_for;
    private String certificate_owner;
    private String responsible_for_implementation;
}
