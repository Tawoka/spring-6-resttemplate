package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerQueryParamDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.core.Is.is;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeerClientImplTest {

  @Autowired
  BeerClient beerClient;

  @Test
  void listBeers() {
    beerClient.listBeers(null);
  }

  @Test
  void listBeersWithName() {
    BeerQueryParamDTO ale = BeerQueryParamDTO.builder().name("ALE").build();
    beerClient.listBeers(ale);
  }

  @Test
  void listBeersPage42Size10AndInventory() {
    BeerQueryParamDTO queryParameters = BeerQueryParamDTO.builder()
        .page(42)
        .pageSize(10)
        .showInventory(true)
        .build();
    beerClient.listBeers(queryParameters);
  }

  @Test
  void getBeerById() {
    Page<BeerDTO> page = beerClient.listBeers(null);
    BeerDTO beerDTO = page.getContent().get(0);
    BeerDTO beerById = beerClient.getBeerById(beerDTO.getId());

    assertThat(beerById).isNotNull();

  }

  @Test
  void createBeer() {
    BeerDTO mangoBobs = BeerDTO.builder()
        .price(new BigDecimal("10.99"))
        .name("Mango Bobs")
        .style(BeerStyle.IPA)
        .quantityOnHand(500)
        .upc("6951384")
        .build();

    BeerDTO beer = beerClient.createBeer(mangoBobs);
    assertThat(beer).isNotNull();
  }
}