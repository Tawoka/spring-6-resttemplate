package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerQueryParamDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

  @Test
  void updateBeer() {
    BeerDTO mangoBobs = BeerDTO.builder()
        .price(new BigDecimal("10.99"))
        .name("Mango Bobs 2")
        .style(BeerStyle.IPA)
        .quantityOnHand(500)
        .upc("6951384")
        .build();

    BeerDTO beer = beerClient.createBeer(mangoBobs);

    final String name = "Mango Bobs 3";
    beer.setName(name);
    BeerDTO beerDTO = beerClient.updateBeer(beer);

    assertThat(beerDTO.getName()).isEqualTo(name);
  }

  @Test
  void deleteBeer(){
    BeerDTO mangoBobs = BeerDTO.builder()
        .price(new BigDecimal("10.99"))
        .name("Mango Bobs 2")
        .style(BeerStyle.IPA)
        .quantityOnHand(500)
        .upc("6951384")
        .build();

    BeerDTO beer = beerClient.createBeer(mangoBobs);

    beerClient.deleteBeer(beer.getId());

    assertThatExceptionOfType(HttpClientErrorException.class)
        .isThrownBy(() -> beerClient.getBeerById(beer.getId()));
  }
}