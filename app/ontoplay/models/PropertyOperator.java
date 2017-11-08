package ontoplay.models;

public class PropertyOperator {

    private final String name;
    private final String description;
    private final boolean canDescribeIndividual;

    public PropertyOperator(String name, String description, boolean canDescribeIndividual) {
        this.name = name;
        this.description = description;
        this.canDescribeIndividual = canDescribeIndividual;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((canDescribeIndividual) ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PropertyOperator other = (PropertyOperator) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (canDescribeIndividual != other.canDescribeIndividual)
            return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "PropertyOperator [description=" + description + ", name=" + name + ", canDescribeIndividual=" + canDescribeIndividual + "]";
    }

    public boolean canDescribeIndividual() {
        return canDescribeIndividual;
    }

}
