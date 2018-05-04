import React, { Component } from 'react'
import { render } from 'react-dom'

import { PropertySelector, ConditionClassSelector, IndividualSelector, OperatorSelector } from './Selectors'
import DatatypeInput from './DatatypeInput'

class ConstraintsBox extends Component {
    constructor(props) {
        super(props);

        this.conditionChanged = this.conditionChanged.bind(this);
    }

    // TODO: This is completely untested for >1 conditions
    conditionChanged(index, condition) {
        const newConditions = this.props.conditions.map((e, i) => i == index ? condition : e);
        this.setState({
            condition: newConditions
        });

        this.props.conditionsChanged(newConditions);
    }

    render() {
        return (
            <div>
                <ConditionBox classUri={this.props.classUri} condition={this.props.conditions[0]} conditionChanged={this.conditionChanged} isIndividual={this.props.isIndividual} />
                <div className='condition-operator'>
                    <a href='#'><span className='glyphicon glyphicon-plus'></span></a>
                </div>
                <div className='condition-operator'>
                    <a >Describe</a>
                </div>
            </div>
        );
    }
}

class ConditionBox extends Component {
    constructor(props) {
        super(props);

        this.state = {
            inputType: null
        };

        this.valueChanged = this.valueChanged.bind(this);
        this.nestedConditionsChanged = this.nestedConditionsChanged.bind(this);
    }

    propertySelected(propUri) {
        const newCondition = { ...this.props.condition, propertyUri: propUri };

        this.props.conditionChanged(0, newCondition);
    }

    operatorSelected(operator) {
        const newCondition = { ...this.props.condition, operator: operator };

        this.props.conditionChanged(0, newCondition);
    }

    nestedConditionCreated(classUri) {
        const newCondition = {
            ...this.props.condition, classConstraintValue: {
                classUri: classUri,
                propertyConditions: [
                    {}
                ]
            }
        };

        this.props.conditionChanged(0, newCondition);
    }

    classSelected(classUri) {
        this.setState({
            valueClassUri: classUri
        })
    }

    individualSelected(indUri) {
        const newCondition = { ...this.props.condition, objectValue: indUri };

        this.props.conditionChanged(0, newCondition);
    }

    valueChanged(value) {
        const newCondition = { ...this.props.condition, datatypeValue: value };

        this.props.conditionChanged(0, newCondition);
    }


    isClassRestrictionOperator(operator) {
        return operator === 'constrainedBy' || operator === 'isDescribedWith';
    }

    isIndividualOperator(operator) {
        return operator === 'equalToIndividual';
    }



    inputTypeRetrieved(inputType) {
        this.setState({
            inputType: inputType
        });
    }

    nestedConditionsChanged(newConditions) {
        const newCondition = {
            ...this.props.condition, classConstraintValue: {
                classUri: this.props.condition.classConstraintValue.classUri, //Doesn't change
                propertyConditions: newConditions
            }
        };

        this.props.conditionChanged(0, newCondition);
    }

    render() {
        const selectedClassUri = this.props.condition.classConstraintValue ? this.props.condition.classConstraintValue.classUri : null;
        const operator = this.props.condition.operator;

        const propertyUri = this.props.condition.propertyUri;
        const inputType = this.state.inputType;
        const individualUri = this.props.condition.objectValue;


        return (
            <div className='condition-panel row'>
                <div className='remove-condition'>
                    <a>
                        <span className='glyphicon glyphicon-remove'></span>
                    </a>
                </div>
                <PropertySelector classUri={this.props.classUri} value={propertyUri} selectionChanged={p => this.propertySelected(p)} />
                {propertyUri &&
                    <OperatorSelector isIndividual={this.props.isIndividual} value={operator} propertyUri={propertyUri} selectionChanged={o => this.operatorSelected(o)} inputTypeRetrieved={i => this.inputTypeRetrieved(i)} />
                }
                {operator && this.isClassRestrictionOperator(operator) &&
                    <ConditionClassSelector value={selectedClassUri} propertyUri={propertyUri} selectionChanged={c => this.nestedConditionCreated(c)} />
                }
                {operator && this.isIndividualOperator(operator) &&
                    <ConditionClassSelector value={this.state.valueClassUri} propertyUri={propertyUri} selectionChanged={c => this.classSelected(c)} />
                }
                {operator && !this.isClassRestrictionOperator(operator) && !this.isIndividualOperator(operator) &&
                    <DatatypeInput inputType={inputType} value={this.props.condition.datatypeValue} valueChanged={v => this.valueChanged(v)} />
                }
                {this.state.valueClassUri && this.isIndividualOperator(operator) &&
                    <IndividualSelector value={individualUri} classUri={this.state.valueClassUri} selectionChanged={i => this.individualSelected(i)} />
                }
                {selectedClassUri && this.isClassRestrictionOperator(operator) &&
                    <ConstraintsBox conditions={this.props.condition.classConstraintValue.propertyConditions} isIndividual={this.props.isIndividual} classUri={selectedClassUri} conditionsChanged={this.nestedConditionsChanged} />
                }
            </div>
        );
    }
}

export default ConstraintsBox;