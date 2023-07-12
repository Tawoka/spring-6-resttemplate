package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerQueryParamDTO;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

  private final static String GET_LIST_PATH = "/api/v1/beer";
  private final static String GET_BEER_PATH = "/api/v1/beer/{beerId}";

  private final RestTemplateBuilder restTemplateBuilder;

  @Override
  public Page<BeerDTO> listBeers(@Nullable BeerQueryParamDTO queryParameters) {
    RestTemplate template = restTemplateBuilder.build();

    String uriString = buildUriString(queryParameters);

    ResponseEntity<BeerDTOPageImpl> response = template.getForEntity(uriString, BeerDTOPageImpl.class);

    System.out.println(response.getBody());
    System.out.println(response.getBody().getContent().get(0).getClass().getSimpleName());

    return response.getBody();
  }

  @Override
  public BeerDTO getBeerById(UUID id) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    return restTemplate.getForObject(GET_BEER_PATH, BeerDTO.class, id);
  }

  @Override
  public BeerDTO createBeer(BeerDTO beer) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    URI uri = restTemplate.postForLocation(GET_LIST_PATH, beer);
    return restTemplate.getForObject(uri.getPath(), BeerDTO.class);
  }

  private String buildUriString(BeerQueryParamDTO queryParameters) {
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_LIST_PATH);
    if (queryParameters != null) {
      addQueryParameters(queryParameters, uriComponentsBuilder);
    }
    return uriComponentsBuilder.toUriString();
  }

  private static void addQueryParameters(BeerQueryParamDTO queryParameters, UriComponentsBuilder uriComponentsBuilder) {
    if (queryParameters.getName() != null) {
      uriComponentsBuilder.queryParam("name", queryParameters.getName());
    }
    if (queryParameters.getBeerStyle() != null) {
      uriComponentsBuilder.queryParam("style", queryParameters.getBeerStyle());
    }
    if (queryParameters.getShowInventory() != null) {
      uriComponentsBuilder.queryParam("showInventory", queryParameters.getShowInventory());
    }
    if (queryParameters.getPage() != null && queryParameters.getPageSize() != null) {
      uriComponentsBuilder.queryParam("size", queryParameters.getPageSize());
      uriComponentsBuilder.queryParam("page", queryParameters.getPage());
    }
  }

}
