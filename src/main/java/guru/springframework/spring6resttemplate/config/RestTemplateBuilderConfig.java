package guru.springframework.spring6resttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {

  @Value("${rest.template.rootUrl}")
  String rootUrl;

  @Value("${rest.template.user.name}")
  String username;

  @Value("${rest.template.user.password}")
  String password;

  @Bean
  RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer){

    assert rootUrl != null;

    RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());
    DefaultUriBuilderFactory defaultUriBuilderFactory =
        new DefaultUriBuilderFactory(rootUrl);

    RestTemplateBuilder basicAuthenticationBuilder = builder.basicAuthentication(username, password);

    return basicAuthenticationBuilder.uriTemplateHandler(defaultUriBuilderFactory);
  }

}
