import CPSF.com.demo.BaseIT;
import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlaceType;
import CPSF.com.demo.service.core.CamperPlaceService;
import CPSF.com.demo.service.core.CamperPlaceTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CamperPlaceTypeIT extends BaseIT {

    @Autowired
    private CamperPlaceTypeService camperPlaceTypeService;

    @Autowired
    private CamperPlaceService camperPlaceService;

    @Test
    public void shouldModifyCamperPlaceAfterCamperPlaceTypePriceChange() {
        var type = "RANDOM_TYPE_NAME";
        var typePrice = new BigDecimal(67);
        var cpPrice = new BigDecimal(69);
        var cpIndex = "1";
        var camperPlaceType = camperPlaceTypeService.create(new CamperPlaceType(type, typePrice));
        var camperPlace = camperPlaceService.create(new CamperPlace(cpIndex, camperPlaceType, cpPrice, List.of()));
        var newPrice = new BigDecimal(2137);
        var camperPlaceTypeDTO = new CamperPlaceTypeDTO(camperPlace.getId(), type, newPrice);

        camperPlaceTypeService.createOrUpdate(List.of(camperPlaceTypeDTO));

        assertThat(camperPlaceService.findById(camperPlace.getId()).getPrice()).isEqualByComparingTo(newPrice);
    }



}