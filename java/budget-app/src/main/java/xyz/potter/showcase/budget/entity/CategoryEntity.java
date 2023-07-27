package xyz.potter.showcase.budget.entity;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Type;

@Entity(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID budgetId;
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

    public UUID getBudgetId() {
      return budgetId;
    }

    public void setBudgetId(UUID budgetId) {
      this.budgetId = budgetId;
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
      CategoryEntity other = (CategoryEntity) obj;
      if (!Objects.equals(id, other.id))
        return false;
      if (!Objects.equals(budgetId, other.budgetId))
        return false;
      return Objects.equals(title, other.title);
    }
}