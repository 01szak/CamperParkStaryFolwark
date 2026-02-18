package CPSF.com.demo.service.core;

import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlaceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CamperPlaceTypeService extends CRUDServiceImpl<CamperPlaceType>{

    @Autowired
    private CamperPlaceService camperPlaceService;

    public List<CamperPlaceType> createOrUpdate(List<CamperPlaceTypeDTO> camperPlaceTypeDTOs) {
        var cpTypesToUpdate = new ArrayList<CamperPlaceType>();
        var cpsToUpdate = new ArrayList<CamperPlace>();

        for (var dto : camperPlaceTypeDTOs) {
            if (dto.id() == null) {
                return List.of(create(dto));
            }

            var cpType = findById(dto.id());
            var cpToModify = cpType.getCamperPlaces();

            cpType.setTypeName(dto.typeName());
            cpType.setPrice(dto.price());
            cpTypesToUpdate.add(cpType);

            cpToModify.forEach(cp -> cp.setPrice(cpType.getPrice()));
            cpsToUpdate.addAll(cpToModify);
        }

        camperPlaceService.update(cpsToUpdate);

        return super.update(cpTypesToUpdate);
    }

    private CamperPlaceType create(CamperPlaceTypeDTO camperPlaceTypeDTO) {
        return super.create(new CamperPlaceType(camperPlaceTypeDTO.typeName(), camperPlaceTypeDTO.price()));
    }

}
