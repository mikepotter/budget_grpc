package xyz.potter.showcase.budget.entity;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Type;

import xyz.potter.showcase.budget.proto.Deduction.TaxableType;
import xyz.potter.showcase.budget.proto.CalculateOn;

@Entity(name = "deduction")
public class DeductionEntity {
    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID paycheckId;

    private String title;
    private float amount;
    private CalculateOn calculateOn;
    private TaxableType taxableType;
    private Integer position;
    private boolean enabled;

    public UUID getId() {
      return id;
    }
    public void setId(UUID i) {
      id = i;
    }
  
    public UUID getPaycheckId() {
      return paycheckId;
    }
    public void setPaycheckId(UUID p) {
      paycheckId = p;
    }

    public String getTitle() {
      return title;
    }
      public void setTitle(String t) {
      title = t;
    }

    public Integer getPosition() {
      return position;
    }
    public void setPosition(Integer p) {
      position = p;
    }

    public boolean getEnabled() {
      return enabled;
    }
    public void setEnabled(boolean e) {
       enabled = e;
    }

    public CalculateOn getCalculateOn() {
      return calculateOn;
    }
    public void setCalculateOn(CalculateOn c) {
      calculateOn = c;
    }

    public float getAmount() {
      return amount;
    }
    public void setAmount(float a) {
      amount = a;
    }

    public TaxableType getTaxableType() {
      return taxableType ;
    }
    public void setTaxableType(TaxableType t) {
      taxableType = t;
    }


    @Override
    public int hashCode() {
      return Objects.hash(id);
    }
  
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      DeductionEntity other = (DeductionEntity) obj;
      if (!Objects.equals(id, other.id))
        return false;
      if (!Objects.equals(paycheckId, other.paycheckId))
        return false;
      return Objects.equals(title, other.title);
    }
}