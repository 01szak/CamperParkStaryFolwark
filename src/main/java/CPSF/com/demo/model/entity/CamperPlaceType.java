package CPSF.com.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@Table(name = "camper_place_type")
@NoArgsConstructor
public class CamperPlaceType extends DbObject{

    @Column(name = "type_name")
    @NotNull
    private String typeName;

    @NotNull
    @Positive
    @Column(name = "price")
    private BigDecimal price;

    @OneToMany
    @Column(name = "camper_place_id")
    List<CamperPlace> camperPlaces;

    public CamperPlaceType(String typeName, BigDecimal price) {
        this.typeName = typeName;
        this.price = price;
    }
}
