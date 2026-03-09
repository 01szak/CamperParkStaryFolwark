package CPSF.com.demo.service.core;

import CPSF.com.demo.model.dto.CamperPlaceTypeDTO;
import CPSF.com.demo.model.entity.CamperPlace;
import CPSF.com.demo.model.entity.CamperPlaceType;
import CPSF.com.demo.repository.CRUDRepository;
import CPSF.com.demo.repository.CamperPlaceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CamperPlaceTypeService extends CRUDServiceImpl<CamperPlaceType>{

    private final CamperPlaceTypeRepository camperPlaceTypeRepository;

    public List<CamperPlaceType> createOrUpdate(List<CamperPlaceTypeDTO> camperPlaceTypeDTOs, List<Integer> cpIdToOverride) {
        var cpTypesToUpdate = new ArrayList<CamperPlaceType>();

        for (var dto : camperPlaceTypeDTOs) {
            if (dto.id() == null) {
                cpTypesToUpdate.add(create(dto));
                continue;
            }

            var cpType = findById(dto.id());
            var cps = cpType.getCamperPlaces();

            if (!cpIdToOverride.isEmpty() && !cps.isEmpty()) overrideSelectedCamperPlaces(cpIdToOverride, cps);

            cpType.setTypeName(dto.typeName());
            cpType.setPrice(dto.price());
            cpTypesToUpdate.add(cpType);
        }

        return super.update(cpTypesToUpdate);
    }

    private void overrideSelectedCamperPlaces(List<Integer> cpIdToOverride, List<CamperPlace> cps) {
            cps.stream()
                    .filter(c -> cpIdToOverride.contains(c.getId()))
                    .forEach(c -> c.setPrice(null));
    }

    private CamperPlaceType create(CamperPlaceTypeDTO camperPlaceTypeDTO) {
        return super.create(new CamperPlaceType(camperPlaceTypeDTO.typeName(), camperPlaceTypeDTO.price()));
    }

    @Override
    protected CRUDRepository<CamperPlaceType> getRepository() {
        return camperPlaceTypeRepository;
    }
}
