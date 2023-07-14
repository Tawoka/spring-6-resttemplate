package guru.springframework.spring6resttemplate.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.web.client.*;
import org.springframework.boot.web.client.*;
import org.springframework.context.annotation.*;
import org.springframework.web.util.*;

@Configuration
public class RestTemplateBuilderConfig {

  @Value("${rest.template.rootUrl}")
  String rootUrl;

  @Value("${rest.template.user.name}")
  String username;

  @Value("${rest.template.user.password}")
  String password;

  @Bean
  RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
    assert rootUrl != null;

    RestTemplateBuilder builder = new RestTemplateBuilder();
    DefaultUriBuilderFactory uriTemplateHandler = new DefaultUriBuilderFactory(rootUrl);

    return configurer.configure(builder)
        .basicAuthentication(username, password)
        .uriTemplateHandler(uriTemplateHandler);
  }

}
