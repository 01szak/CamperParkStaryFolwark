package CPSF.com.demo.service.core;

import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.model.entity.CamperPlaceType;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.repository.CamperPlaceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CamperPlaceTypeService extends CRUDServiceImpl<CamperPlaceType>{

    private final CamperPlaceTypeRepository camperPlaceTypeRepository;

    @Transactional
    public List<CamperPlaceType> createOrUpdate(List<CamperPlaceTypeDTO> camperPlaceTypeDTOs) {
        var cpTypesToUpdate = new ArrayList<CamperPlaceType>();

        for (var dto : camperPlaceTypeDTOs) {
            if (dto.id() == null) {
                return List.of(create(dto));
            }

            var cpType = findById(dto.id());

            cpType.setTypeName(dto.typeName());
            cpType.setPrice(dto.price());
            cpTypesToUpdate.add(cpType);
        }

        return super.update(cpTypesToUpdate);
    }

    private CamperPlaceType create(CamperPlaceTypeDTO camperPlaceTypeDTO) {
        return super.create(new CamperPlaceType(camperPlaceTypeDTO.typeName(), camperPlaceTypeDTO.price()));
    }

    @Override
    protected CRUDRepository<CamperPlaceType> getRepository() {
        return camperPlaceTypeRepository;
    }
}
