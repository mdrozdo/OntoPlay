package ontoplay.models.angular;

import ontoplay.models.PropertyOperator;

import java.util.ArrayList;
import java.util.List;

public class OperatorDTO {

    private List<Operator> operators;
    private String inputType;

    public OperatorDTO() {
        operators = new ArrayList<Operator>();
    }

    public OperatorDTO(String inputType, List<PropertyOperator> operators) {
        this.inputType = inputType;
        this.operators = new ArrayList<Operator>();
        for (PropertyOperator propertyOperator : operators) {
            AddOperator(new Operator(propertyOperator.getDescription(), propertyOperator.getName()));
        }
    }

    public void AddOperator(Operator operator) {
        operators.add(operator);
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }


}
