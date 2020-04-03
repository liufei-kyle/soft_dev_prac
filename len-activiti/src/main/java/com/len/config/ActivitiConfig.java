package com.len.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;

import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.image.HMProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by liuruijie on 2017/2/20.
 * activiti工作流配置
 */
@Configuration
public class ActivitiConfig {

    @Value("${dataType}")
    private String dataType;

    /**
     * spring 集成 activiti
     */
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(DataSource dataSource, PlatformTransactionManager transactionManager) throws IOException {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        //表不存在创建表
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        //指定数据库
        processEngineConfiguration.setDatabaseType(dataType);
        processEngineConfiguration.setTransactionManager(transactionManager);
        //历史变量
        processEngineConfiguration.setHistory("full");
        //指定字体
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        // for MailTask
        processEngineConfiguration.setProcessDiagramGenerator(new DefaultProcessDiagramGenerator());
        BASE64Encoder encoder = new BASE64Encoder();
        BASE64Decoder decoder = new BASE64Decoder();
        String s1 = new String(decoder.decodeBuffer(encoder.encode("744346614@qq.com".getBytes())));
        processEngineConfiguration.setMailServerHost("smtp.qq.com")
                .setMailServerUseSSL(true)
                .setMailServerPort(465)
                .setMailServerDefaultFrom(s1)
                .setMailServerUsername(s1)
                .setMailServerPassword(new String(decoder.decodeBuffer(encoder.encode("glgjrrbeslnbbaje".getBytes()))));
//                .setMailServerPassword("achieveit2020");
//        processEngineConfiguration.setMailServerHost("smtp.qq.com")
//                .setMailServerPort(25)
//                .setMailServerUseSSL(false)
//                .setMailServerDefaultFrom("744346614@qq.com")
//                .setMailServerUsername("Achieve_It_Official")
//                .setMailServerPassword("otudyjhqwrgubchh");
        return processEngineConfiguration;
    }

    //流程引擎，与spring整合使用factoryBean
    @Bean
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfiguration processEngineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        return processEngineFactoryBean;
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
