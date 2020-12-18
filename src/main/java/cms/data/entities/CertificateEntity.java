package cms.data.entities;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "certificates")
public class CertificateEntity extends BaseEntity {

    @Column(name = "file_name",nullable = false)
    private String file_name;
    @Column(name = "sn",nullable = false)
    private String sn;
    @Column(name = "valid_from",nullable = false)
    private Date valid_from;
    @Column(name = "valid_to",nullable = false)
//    private LocalDateTime valid_to;
    private Date valid_to;
    @Column(name = "issuer",nullable = false)
    private String issuer;
    @Column(name = "issuer_cn",nullable = false)
    private String issuer_cn;
    @Column(name = "subject",nullable = false)
    private String subject;
    @Column(name = "subject_cn",nullable = false)
    private String subject_cn;
    @Lob
    @Column(name="certificate", length=100000)
    private byte[] certificate;
    @Column(name = "contacts")
    private String contacts;
    @Column(name = "description")
    private String description;
    @Column(name = "updated")
    private boolean updated;
    @Column(name = "importance")
    private String  importance;
    @Column(name = "used_for")
    private String used_for;




}
