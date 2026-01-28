package CPSF.com.demo.request;

import CPSF.com.demo.enums.CamperPlaceType;

public record CamperPlaceRequest(CamperPlaceType camperPlaceType, double price) {

}
