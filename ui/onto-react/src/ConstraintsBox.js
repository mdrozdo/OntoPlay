import React, { Component } from 'react'
import { render } from 'react-dom'

import { PropertySelector, ConditionClassSelector, IndividualSelector, OperatorSelector } from './Selectors'
import DatatypeInput from './DatatypeInput'

class ConstraintsBox extends Component {
    constructor(props) {
        super(props);

        // this.setState({
        //     nextConditionId: props.propertyConditions.length
        // });

        this.conditionChanged = this.conditionChanged.bind(this);
        this.handleAddCondition = this.handleAddCondition.bind(this);
    }

    // TODO: This is completely untested for >1 conditions
    conditionChanged(index, condition) {
        const newConditions = this.props.propertyConditions.map((e, i) => i == index ? condition : e).filter(e => e !== null);
        
        this.props.conditionsChanged(newConditions);
    }

    handleAddCondition(e) {
        e.preventDefault();
        const newConditions = this.props.propertyConditions.concat([{}]);
        this.props.conditionsChanged(newConditions);
    }

    render() {
        return (
            <div>
                {this.props.propertyConditions.map((c, i) => {
                    return <ConditionBox key={i} index={i} classUri={this.props.classUri} condition={this.props.propertyConditions[i]} conditionChanged={this.conditionChanged} api={this.props.api} />;
                })}
                <div className='condition-operator'>
                    <a href='#' onClick={this.handleAddCondition}><span className='glyphicon glyphicon-plus'></span></a>
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
        this.handleRemoveCondition = this.handleRemoveCondition.bind(this);
    }

    propertySelected(propUri) {
        const newCondition = { propertyUri: propUri };

        this.props.conditionChanged(this.props.index, newCondition);
    }

    operatorSelected(operator) {
        const newCondition = {
            propertyUri: this.props.condition.propertyUri,
            operator: operator
        };

        this.props.conditionChanged(this.props.index, newCondition);
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

        this.props.conditionChanged(this.props.index, newCondition);
    }

    classSelected(classUri) {
        this.setState({
            valueClassUri: classUri
        })
    }

    individualSelected(indUri) {
        const newCondition = { ...this.props.condition, objectValue: indUri };

        this.props.conditionChanged(this.props.index, newCondition);
    }

    valueChanged(value) {
        const newCondition = { ...this.props.condition, datatypeValue: value };

        this.props.conditionChanged(this.props.index, newCondition);
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

        this.props.conditionChanged(this.props.index, newCondition);
    }

    emptyIfNullOrUndefined(value) {
        return value ? value : '';
    }

    handleRemoveCondition(e) {
        e.preventDefault();
        this.props.conditionChanged(this.props.index, null);
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
                    <a href="#" onClick={this.handleRemoveCondition}>
                        <span className='glyphicon glyphicon-remove'></span>
                    </a>
                </div>
                <PropertySelector api={this.props.api} classUri={this.props.classUri} value={this.emptyIfNullOrUndefined(propertyUri)} selectionChanged={p => this.propertySelected(p)} />
                {propertyUri &&
                    <OperatorSelector api={this.props.api} value={this.emptyIfNullOrUndefined(operator)} propertyUri={propertyUri} selectionChanged={o => this.operatorSelected(o)} inputTypeRetrieved={i => this.inputTypeRetrieved(i)} />
                }
                {operator && this.isClassRestrictionOperator(operator) &&
                    <ConditionClassSelector api={this.props.api} value={this.emptyIfNullOrUndefined(selectedClassUri)} propertyUri={propertyUri} selectionChanged={c => this.nestedConditionCreated(c)} />
                }
                {operator && this.isIndividualOperator(operator) &&
                    <ConditionClassSelector api={this.props.api} value={this.emptyIfNullOrUndefined(this.state.valueClassUri)} propertyUri={propertyUri} selectionChanged={c => this.classSelected(c)} />
                }
                {operator && !this.isClassRestrictionOperator(operator) && !this.isIndividualOperator(operator) &&
                    <DatatypeInput inputType={inputType} value={this.emptyIfNullOrUndefined(this.props.condition.datatypeValue)} valueChanged={v => this.valueChanged(v)} />
                }
                {this.state.valueClassUri && this.isIndividualOperator(operator) &&
                    <IndividualSelector api={this.props.api} value={this.emptyIfNullOrUndefined(individualUri)} classUri={this.state.valueClassUri} selectionChanged={i => this.individualSelected(i)} />
                }
                {selectedClassUri && this.isClassRestrictionOperator(operator) &&
                    <ConstraintsBox propertyConditions={this.props.condition.classConstraintValue.propertyConditions} api={this.props.api} classUri={selectedClassUri} conditionsChanged={this.nestedConditionsChanged} />
                }
            </div>
        );
    }
}

export default ConstraintsBox;