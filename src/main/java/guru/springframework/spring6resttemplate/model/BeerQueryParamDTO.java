package guru.springframework.spring6resttemplate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerQueryParamDTO {

  private String name;
  private BeerStyle beerStyle;
  private Boolean showInventory;
  private Integer pageSize;
  private Integer page;

}
