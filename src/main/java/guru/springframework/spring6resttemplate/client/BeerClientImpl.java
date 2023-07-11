package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

  private final static String GET_LIST_PATH = "/api/v1/beer";

  private final RestTemplateBuilder restTemplateBuilder;

  @Override
  public Page<BeerDTO> listBeers() {
    RestTemplate template = restTemplateBuilder.build();

    ResponseEntity<BeerDTOPageImpl> response = template.getForEntity(GET_LIST_PATH, BeerDTOPageImpl.class);

    System.out.println(response.getBody());
    System.out.println(response.getBody().getContent().get(0).getClass().getSimpleName());

    return response.getBody();
  }

}
