package cms.data.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CertificateView {
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
    private MultipartFile certificate;
    private String contacts;
    private String description;
    private String  importance;
    private String used_for;
    private String certificate_owner;
    private String responsible_for_implementation;

    @Override
    public String toString() {
        return    System.lineSeparator() +
//                serien nomer tyrsene
//                "  File name= " + file_name + System.lineSeparator() +
                "  Sn='" + sn + System.lineSeparator() +
                "  Valid from=" + valid_from + System.lineSeparator() +
                "  Valid to=" + valid_to + System.lineSeparator() +
                "  Issuer=" + issuer + System.lineSeparator() +
                "  Subject=" + subject + System.lineSeparator() +
                "  SubjectAlternativeName=" + subjectAlternativeName + System.lineSeparator() +
//                "  Description='" + description + System.lineSeparator() +
//                "  Importance='" + importance + System.lineSeparator() +
                "  Used for='" + used_for + System.lineSeparator() +
                "  Certificate owner='" + certificate_owner + System.lineSeparator() +
                "  Responsible for implementation='" + responsible_for_implementation +
                   System.lineSeparator() +
                "----------------------------------------------";
    }
}
