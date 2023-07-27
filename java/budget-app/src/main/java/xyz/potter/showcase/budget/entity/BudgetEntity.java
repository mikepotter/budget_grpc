package xyz.potter.showcase.budget.entity;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Type;

@Entity(name = "budget")
public class BudgetEntity {
    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private String title;

    public UUID getId() {
      return id;
    }
  
    public void setId(UUID id) {
      this.id = id;
    }
  
    public String getTitle() {
      return title;
    }
  
    public void setTitle(String title) {
      this.title = title;
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
      BudgetEntity other = (BudgetEntity) obj;
      return Objects.equals(id, other.id);
    }
}