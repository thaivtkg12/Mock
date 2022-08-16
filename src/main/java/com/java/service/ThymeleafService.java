package com.java.service;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.java.utils.Constants;

public class ThymeleafService {

   private static TemplateEngine templateEngine = emailTemplateEngine();
   
   private static TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

   private static ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(Constants.MAIL_TEMPLATE_BASE_NAME);
        return messageSource;
    }

   private static ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(Constants.MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(Constants.MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(Constants.ENDCODE);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

   public String getContent(String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }
}