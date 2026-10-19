package CPSF.com.demo.service.core;

import CPSF.com.demo.model.entity.Organisation;
import CPSF.com.demo.repository.CRUDRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganisationService extends CRUDServiceImpl<Organisation> {

    private final OrganisationRepository organisationRepository;

    @Override
    protected CRUDRepository<Organisation> getRepository() {
        return organisationRepository;
    }

}
