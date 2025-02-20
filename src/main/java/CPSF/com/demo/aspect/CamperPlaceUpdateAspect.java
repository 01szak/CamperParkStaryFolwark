package CPSF.com.demo.aspect;

import CPSF.com.demo.service.CamperPlaceService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class CamperPlaceUpdateAspect {

    @Autowired
    CamperPlaceService camperPlaceService;

    @Before("execution(* CPSF.com.demo.controller.*.*(..))")
    public void updateIsOccupied() {
        camperPlaceService.findAllCamperPlaces().forEach(camperPlaceService::setIsCamperPlaceOccupied);
    }
}
