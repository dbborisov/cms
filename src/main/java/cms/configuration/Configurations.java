package cms.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
public class Configurations {
    private ModelMapper modelMapper;

    @Bean
    public ModelMapper modelMapper(){
        this.modelMapper = new ModelMapper();
        return  this.modelMapper;
    }


    @Bean(name = "local")
    @ConfigurationProperties(prefix = "spring.datasource.maindb")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "maindb")
    public JdbcTemplate jdbcTemplate(@Qualifier("local") DataSource ds) {
        return new JdbcTemplate(ds);
    }


    @Bean
    public MemoryStatus memoryStatus(){
        return new MemoryStatus();
    }





}