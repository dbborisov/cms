package cms.data.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CertificateView {
    private Long id;
    private String file_name;
    private String sn;
    private LocalDateTime valid_from;
    private LocalDateTime valid_to;
    private String issuer;
    private String issuer_cn;
    private String subject;
    private String subject_cn;
    private MultipartFile certificate;
    private String contacts;
    private String description;
    private String  importance;
    private String used_for;
}
