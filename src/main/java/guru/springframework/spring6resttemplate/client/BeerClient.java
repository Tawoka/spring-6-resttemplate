package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerQueryParamDTO;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {

  Page<BeerDTO> listBeers(@Nullable BeerQueryParamDTO queryParameters);

  BeerDTO getBeerById(UUID id);

  BeerDTO createBeer(BeerDTO beer);

  BeerDTO updateBeer(BeerDTO beer);

  void deleteBeer(UUID id);

}
