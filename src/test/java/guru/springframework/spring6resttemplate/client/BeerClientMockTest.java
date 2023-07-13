package guru.springframework.spring6resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withAccepted;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@Import(RestTemplateBuilderConfig.class)
public class BeerClientMockTest {

  static final String URL = "http://localhost:8080";

  BeerClient beerClient;
  MockRestServiceServer server;

  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  @Autowired
  ObjectMapper objectMapper;

  @Mock
  RestTemplateBuilder mockRestTemplateBuilder = new RestTemplateBuilder(new MockServerRestTemplateCustomizer());

  @BeforeEach
  void setUp() {
    RestTemplate restTemplate = restTemplateBuilder.build();
    server = MockRestServiceServer.bindTo(restTemplate).build();
    when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
    beerClient = new BeerClientImpl(mockRestTemplateBuilder);
  }

  @Test
  void listBeers() throws JsonProcessingException {
    String payload = objectMapper.writeValueAsString(getPage());

    server.expect(method(HttpMethod.GET))
        .andExpect(requestTo(URL + BeerClientImpl.BEER_MAIN_PATH))
        .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

    Page<BeerDTO> page = beerClient.listBeers(null);
    assertThat(page.getContent().size()).isGreaterThan(0);
  }

  @Test
  void createBeer() throws JsonProcessingException {
    UUID id = UUID.randomUUID();
    BeerDTO beerDto = getBeerDto(id);
    String payload = objectMapper.writeValueAsString(beerDto);
    URI uri = UriComponentsBuilder.fromPath(BeerClientImpl.BEER_ID_PATH).build(id);

    server.expect(method(HttpMethod.POST))
        .andExpect(requestToUriTemplate(URL + BeerClientImpl.BEER_MAIN_PATH))
        .andRespond(withAccepted().location(uri));

    server.expect(method(HttpMethod.GET))
        .andExpect(requestToUriTemplate(URL + BeerClientImpl.BEER_ID_PATH, id))
        .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

    BeerDTO beer = beerClient.createBeer(beerDto);
    assertThat(beer.getId()).isEqualTo(id);

  }

  @Test
  void getBeerById() throws JsonProcessingException {
    UUID id = UUID.randomUUID();
    String payload = objectMapper.writeValueAsString(getBeerDto(id));

    server.expect(method(HttpMethod.GET))
        .andExpect(requestToUriTemplate(URL + BeerClientImpl.BEER_ID_PATH, id))
        .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

    BeerDTO beerById = beerClient.getBeerById(id);
    assertThat(beerById.getId()).isEqualTo(id);
  }

  BeerDTO getBeerDto(UUID id) {
    return BeerDTO.builder()
        .id(id)
        .price(new BigDecimal("10.99"))
        .name("Mango Bobs")
        .style(BeerStyle.IPA)
        .quantityOnHand(500)
        .upc("6461684654135")
        .build();
  }

  BeerDTOPageImpl getPage() {
    List<BeerDTO> beerDTOList = Collections.singletonList(getBeerDto(UUID.randomUUID()));
    return new BeerDTOPageImpl(beerDTOList, 1, 25, 1);
  }

}
