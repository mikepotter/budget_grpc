package xyz.potter.showcase.budget.entity;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Type;

import xyz.potter.showcase.budget.proto.Expense.Quadrant;
import xyz.potter.showcase.budget.proto.CalculateOn;

@Entity(name = "expense")
public class ExpenseEntity {
    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID categoryId;

    private String title;
    private Quadrant quadrant;
	  private boolean fixed;
	  private float low;
	  private float high;
    private float amount;
    private CalculateOn calculateOn;
    private boolean enabled;
    private Integer position;

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

    public Quadrant getQuadrant() {
      return quadrant ;
    }
    public void setQuadrant(Quadrant q) {
      quadrant = q;
    }

    public boolean getFixed() {
      return fixed;
    }
    public void setFixed(boolean f) {
      fixed = f;
    }

    public float getLow() {
      return low;
    }
    public void setLow(float l) {
      low = l;
    }

    public float getHigh() {
      return high;
    }
    public void setHigh(float h) {
      high = h;
    }

    public UUID getId() {
      return id;
    }
    public void setId(UUID id) {
      this.id = id;
    }
  
    public String getTitle() {
      return title;
    }
      public void setTitle(String t) {
      title = t;
    }

    public UUID getCategoryId() {
      return categoryId;
    }
    public void setCategoryId(UUID c) {
      categoryId = c;
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
      ExpenseEntity other = (ExpenseEntity) obj;
      if (!Objects.equals(id, other.id))
        return false;
      if (!Objects.equals(categoryId, other.categoryId))
        return false;
      return Objects.equals(title, other.title);
    }
}