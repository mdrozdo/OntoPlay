import React, { Component } from 'react';

import {
    PropertySelector,
    ConditionClassSelector,
    IndividualSelector,
    OperatorSelector,
} from './Selectors';
import DatatypeInput from './DatatypeInput';

//Component representing a collection of conditions on a single class.
class ConstraintsBox extends Component {
    constructor(props) {
        super(props);

        // this.setState({
        //     nextConditionId: props.propertyConditions.length
        // });

        this.conditionChanged = this.conditionChanged.bind(this);
        // this.handleAddAndCondition = this.handleAddAndCondition.bind(this);
        this.handleAddOrCondition = this.handleAddOrCondition.bind(this);
    }

    //TODO: remove index
    conditionChanged(index, condition) {
        const newCondition = condition ? { ...condition } : {};
        this.props.conditionsChanged(newCondition);
    }

    handleAddOrCondition(e) {
        e.preventDefault();
        const newConditions = {
            type: 'union',
            contents: [{ ...this.props.propertyConditions }, {}],
        };

        this.props.conditionsChanged(newConditions);
    }

    renderConditionBox() {
        return (
            <div className='condition-panel row'>
                <ConditionBox
                    key={0}
                    index={0}
                    classUri={this.props.classUri}
                    condition={this.props.propertyConditions}
                    conditionChanged={this.conditionChanged}
                    handleAddOrCondition={this.handleAddOrCondition}
                    displayBorder={false}
                    api={this.props.api}
                />
                {/* <div className='condition-operator'>
                    <a href='#' onClick={this.handleAddOrCondition}>
                        <div>Or</div>
                    </a>
                </div> */}
            </div>
        );
    }

    renderUnionBox() {
        return (
            <div>
                <UnionBox
                    classUri={this.props.classUri}
                    union={this.props.propertyConditions}
                    conditionChanged={this.conditionChanged}
                    api={this.props.api}
                />
            </div>
        );
    }

    renderIntersectionBox() {
        return (
            <div className='condition-panel row'>
                <IntersectionBox
                    classUri={this.props.classUri}
                    intersection={this.props.propertyConditions}
                    conditionChanged={this.conditionChanged}
                    handleAddOrCondition={this.handleAddOrCondition}
                    api={this.props.api}
                />
                {/* <div className='condition-operator'>
                    <a href='#' onClick={this.handleAddOrCondition}>
                        <div>OR</div>
                    </a>
                </div> */}
            </div>
        );
    }

    renderValuesBox() {
        return (
            <div className='condition-panel row'>
                <ValuesBox
                    classUri={this.props.classUri}
                    values={this.props.propertyConditions}
                    conditionChanged={this.conditionChanged}
                    api={this.props.api}
                />
            </div>
        );
    }

    render() {
        const constraint = this.props.propertyConditions;
        const constraintType = constraint ? constraint.type : null;

        const boxComponent =
            !constraintType || constraintType == 'condition'
                ? this.renderConditionBox()
                : constraintType == 'union'
                    ? this.renderUnionBox()
                    : constraintType == 'intersection'
                        ? this.renderIntersectionBox()
                        : constraintType == 'values'
                            ? this.renderValuesBox()
                            : null; //Unknown value

        return boxComponent;
    }
}

class UnionBox extends Component {
    constructor(props) {
        super(props);

        this.conditionChanged = this.conditionChanged.bind(this);
        this.handleAddCondition = this.handleAddCondition.bind(this);
    }

    conditionChanged(index, condition) {
        const newContents = this.props.union.contents
            .map((e, i) => (i == index ? condition : e))
            .filter(e => e !== null);

        const newCondition =
            newContents.length > 1
                ? {
                    ...this.props.union,
                    contents: newContents,
                }
                : newContents[0];

        this.props.conditionChanged(this.props.index, newCondition);
    }

    handleAddCondition(e) {
        e.preventDefault();
        const newContents = this.props.union.contents.concat([{}]);

        const newUnion = {
            ...this.props.union,
            contents: newContents,
        };

        this.props.conditionChanged(this.props.index, newUnion);
    }

    render() {
        const contents = this.props.union.contents;

        return (
            <div className='condition-panel row'>
                {contents.map((c, i) => {
                    const constraintType = c.type || null;

                    const boxComponent =
                        !constraintType || constraintType == 'condition'
                            ? this.renderConditionBox(i, c)
                            : constraintType == 'union'
                                ? this.renderUnionBox(i, c)
                                : constraintType == 'intersection'
                                    ? this.renderIntersectionBox(i, c)
                                    : null; //Unknown value

                    return (
                        <div key={'cond' + i}>
                            {boxComponent}
                            {i < contents.length - 1 ? (
                                <div className='group-operator'>OR</div>
                            ) : null}
                        </div>
                    );
                })}
                <div className='condition-operator'>
                    <a href='#' onClick={this.handleAddCondition}>
                        <div>OR</div>
                    </a>
                </div>
                {/* Commented out, because annotations are not ported to React.
                <div className='condition-operator'>
                    <a>Describe</a>
                </div> */}
            </div>
        );
    }

    renderConditionBox(index, condition) {
        return (
            <ConditionBox
                key={index}
                index={index}
                classUri={this.props.classUri}
                condition={condition}
                conditionChanged={this.conditionChanged}
                api={this.props.api}
            />
        );
    }

    renderUnionBox(index, condition) {
        return (
            <div>
                <UnionBox
                    key={index}
                    index={index}
                    classUri={this.props.classUri}
                    union={condition}
                    conditionChanged={this.conditionChanged}
                    api={this.props.api}
                />
                {/* <div className='condition-operator'>
                    <a href='#' onClick={this.handleAddOrCondition}>
                        <div>Or</div>
                    </a>
                </div> */}
            </div>
        );
    }

    renderIntersectionBox(index, condition) {
        return (
            <div>
                <IntersectionBox
                    key={index}
                    index={index}
                    classUri={this.props.classUri}
                    intersection={condition}
                    conditionChanged={this.conditionChanged}
                    api={this.props.api}
                />
            </div>
        );
    }
}

class IntersectionBox extends Component {
    constructor(props) {
        super(props);

        this.conditionChanged = this.conditionChanged.bind(this);
        this.handleAddCondition = this.handleAddCondition.bind(this);
    }

    conditionChanged(index, condition) {
        const newContents = this.props.intersection.contents
            .map((e, i) => (i == index ? condition : e))
            .filter(e => e !== null);

        const newCondition =
            newContents.length > 1
                ? {
                    ...this.props.intersection,
                    contents: newContents,
                }
                : newContents[0];

        this.props.conditionChanged(this.props.index, newCondition);
    }

    handleAddCondition(e) {
        e.preventDefault();
        const newContents = this.props.intersection.contents.concat([{}]);

        const newIntersection = {
            ...this.props.intersection,
            contents: newContents,
        };

        this.props.conditionChanged(this.props.index, newIntersection);
    }

    renderConditionBox(index, condition) {
        return (
            <ConditionBox
                key={index}
                index={index}
                classUri={this.props.classUri}
                condition={condition}
                conditionChanged={this.conditionChanged}
                displayBorder={false}
                displayAndOperator={false}
                api={this.props.api}
            />
        );
    }

    renderUnionBox(index, condition) {
        return (
            <div>
                <UnionBox
                    key={index}
                    index={index}
                    classUri={this.props.classUri}
                    union={condition}
                    conditionChanged={this.conditionChanged}
                    api={this.props.api}
                />
                {/* <div className='condition-operator'>
                    <a href='#' onClick={this.handleAddOrCondition}>
                        <div>Or</div>
                    </a>
                </div> */}
            </div>
        );
    }

    renderIntersectionBox(index, condition) {
        return (
            <div>
                <IntersectionBox
                    key={index}
                    index={index}
                    classUri={this.props.classUri}
                    intersection={condition}
                    conditionChanged={this.conditionChanged}
                    api={this.props.api}
                />
            </div>
        );
    }

    render() {
        const contents = this.props.intersection.contents;
        const displayBorder = !this.props.handleAddOrCondition;
        return (
            <div className={displayBorder?'condition-panel row':''}>
                {contents.map((c, i) => {
                    const constraintType = c.type || null;

                    const boxComponent =
                        !constraintType || constraintType == 'condition'
                            ? this.renderConditionBox(i, c)
                            : constraintType == 'union'
                                ? this.renderUnionBox(i, c)
                                : constraintType == 'intersection'
                                    ? this.renderIntersectionBox(i, c)
                                    : null; //Unknown value

                    return (
                        <div key={'cond' + i}>
                            {boxComponent}
                            {i < contents.length - 1 ? (
                                <div className='group-operator'>AND</div>
                            ) : null}
                        </div>
                    );
                })}
                <div className='condition-operator'>
                    <a href='#' onClick={this.handleAddCondition}>AND</a>
                    {this.props.handleAddOrCondition && (
                        <span>
                            &nbsp;|&nbsp;
                            <a href='#' onClick={this.props.handleAddOrCondition}>OR</a>
                        </span>
                    )}
                </div>
                {/* Commented out, because annotations are not ported to React.
                <div className='condition-operator'>
                    <a>Describe</a>
                </div> */}
            </div>
        );
    }
}

class ValuesBox extends Component {
    constructor(props) {
        super(props);

        this.conditionChanged = this.conditionChanged.bind(this);
        this.handleAddCondition = this.handleAddCondition.bind(this);
    }

    conditionChanged(index, condition) {
        const newContents = this.props.values.contents
            .map((e, i) => (i == index ? condition : e))
            .filter(e => e !== null);

        const newCondition =
            newContents.length > 1
                ? {
                    ...this.props.values,
                    contents: newContents,
                }
                : newContents[0];

        this.props.conditionChanged(this.props.index, newCondition);
    }

    handleAddCondition(e) {
        e.preventDefault();
        const newContents = this.props.values.contents.concat([{}]);

        const newValues = {
            ...this.props.values,
            contents: newContents,
        };

        this.props.conditionChanged(this.props.index, newValues);
    }

    renderConditionBox(index, condition) {
        return (
            <ConditionBox
                key={index}
                index={index}
                classUri={this.props.classUri}
                condition={condition}
                conditionChanged={this.conditionChanged}
                displayBorder={false}
                displayAndOperator={false}
                api={this.props.api}
            />
        );
    }

    render() {
        const contents = this.props.values.contents;

        return (
            <div className='condition-panel row'>
                {contents.map((c, i) => {
                    const constraintType = c.type || null;

                    const boxComponent =
                        !constraintType || constraintType == 'condition'
                            ? this.renderConditionBox(i, c)
                            : null;

                    return (
                        <div key={'cond' + i}>
                            {boxComponent}
                            {i < contents.length - 1 ? (
                                <div className='group-operator'>AND</div>
                            ) : null}
                        </div>
                    );
                })}
                <div className='condition-operator'>
                    <a href='#' onClick={this.handleAddCondition}>
                        <div>And</div>
                    </a>
                </div>
                {/* Commented out, because annotations are not ported to React.
                <div className='condition-operator'>
                    <a>Describe</a>
                </div> */}
            </div>
        );
    }
}

// Component representing a single condition on a property.
class ConditionBox extends Component {
    constructor(props) {
        super(props);

        this.state = {
            inputType: null,
        };

        this.valueChanged = this.valueChanged.bind(this);
        this.nestedConditionsChanged = this.nestedConditionsChanged.bind(this);
        this.handleRemoveCondition = this.handleRemoveCondition.bind(this);
        this.handleAddCondition = this.handleAddCondition.bind(this);
    }

    static defaultProps = {
        displayBorder: true,
        displayAndOperator: true,
    };

    propertySelected(propUri) {
        const newCondition = {
            type: 'condition',
            groupOperator: this.props.condition.groupOperator,
            propertyUri: propUri,
        };

        this.props.conditionChanged(this.props.index, newCondition);
    }

    operatorSelected(operator) {
        const newCondition = {
            type: 'condition',
            groupOperator: this.props.condition.groupOperator,
            propertyUri: this.props.condition.propertyUri,
            operator: operator,
        };

        this.props.conditionChanged(this.props.index, newCondition);
    }

    nestedConditionCreated(classUri) {
        const newCondition = {
            ...this.props.condition,
            classConstraintValue: {
                classUri: classUri,
                propertyConditions: [{}],
            },
        };

        this.props.conditionChanged(this.props.index, newCondition);
    }

    classSelected(classUri) {
        this.setState({
            valueClassUri: classUri,
        });
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
            inputType: inputType,
        });
    }

    nestedConditionsChanged(newConditions) {
        const newCondition = {
            ...this.props.condition,
            classConstraintValue: {
                classUri: this.props.condition.classConstraintValue.classUri, //Doesn't change
                propertyConditions: newConditions,
            },
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

    handleAddCondition(e) {
        e.preventDefault();

        const newCondition = {
            type: 'intersection',
            contents: [{ ...this.props.condition }, {}],
        };

        this.props.conditionChanged(this.props.index, newCondition);
    }

    render() {
        const selectedClassUri = this.props.condition.classConstraintValue
            ? this.props.condition.classConstraintValue.classUri
            : null;
        const operator = this.props.condition.operator;

        const propertyUri = this.props.condition.propertyUri;
        const inputType = this.state.inputType;
        const individualUri = this.props.condition.objectValue;

        return (
            <div>
                <div className='group-operator'>
                    {this.props.condition.groupOperator}
                </div>

                <div className={this.props.displayBorder ? 'condition-panel row' : ''}>
                    <div className='remove-condition'>
                        <a href='#' onClick={this.handleRemoveCondition}>
                            <span className='glyphicon glyphicon-remove' />
                        </a>
                    </div>
                    <PropertySelector
                        api={this.props.api}
                        classUri={this.props.classUri}
                        value={this.emptyIfNullOrUndefined(propertyUri)}
                        selectionChanged={p => this.propertySelected(p)}
                    />
                    {propertyUri && (
                        <OperatorSelector
                            api={this.props.api}
                            value={this.emptyIfNullOrUndefined(operator)}
                            propertyUri={propertyUri}
                            selectionChanged={o => this.operatorSelected(o)}
                            inputTypeRetrieved={i => this.inputTypeRetrieved(i)}
                        />
                    )}
                    {operator && this.isClassRestrictionOperator(operator) && (
                        <ConditionClassSelector
                            api={this.props.api}
                            value={this.emptyIfNullOrUndefined(
                                selectedClassUri
                            )}
                            propertyUri={propertyUri}
                            selectionChanged={c =>
                                this.nestedConditionCreated(c)
                            }
                        />
                    )}
                    {operator && this.isIndividualOperator(operator) && (
                        <ConditionClassSelector
                            api={this.props.api}
                            value={this.emptyIfNullOrUndefined(
                                this.state.valueClassUri
                            )}
                            propertyUri={propertyUri}
                            selectionChanged={c => this.classSelected(c)}
                        />
                    )}
                    {operator &&
                        !this.isClassRestrictionOperator(operator) &&
                        !this.isIndividualOperator(operator) && (
                        <DatatypeInput
                            inputType={inputType}
                            value={this.emptyIfNullOrUndefined(
                                this.props.condition.datatypeValue
                            )}
                            valueChanged={v => this.valueChanged(v)}
                        />
                    )}
                    {this.state.valueClassUri &&
                        this.isIndividualOperator(operator) && (
                        <IndividualSelector
                            api={this.props.api}
                            value={this.emptyIfNullOrUndefined(
                                individualUri
                            )}
                            classUri={this.state.valueClassUri}
                            selectionChanged={i =>
                                this.individualSelected(i)
                            }
                        />
                    )}
                    {selectedClassUri &&
                        this.isClassRestrictionOperator(operator) && (
                        <ConstraintsBox
                            propertyConditions={
                                this.props.condition.classConstraintValue
                                    .propertyConditions
                            }
                            api={this.props.api}
                            classUri={selectedClassUri}
                            conditionsChanged={this.nestedConditionsChanged}
                        />
                    )}
                    {this.props.displayAndOperator && (
                        <div className='condition-internal-operator'>
                            
                            <a href='#' onClick={this.handleAddCondition}>AND</a>
                            {this.props.handleAddOrCondition && (
                                <span> 
                                    &nbsp;|&nbsp;
                                    <a href='#' onClick={this.props.handleAddOrCondition}>OR</a>  
                                </span>  
                            )}
                        </div>
                    )}
                </div>
            </div>
        );
    }
}

export default ConstraintsBox;
