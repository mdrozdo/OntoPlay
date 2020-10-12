import React, { Component, Fragment } from 'react';
import { Typeahead, Highlighter, Menu, MenuItem } from 'react-bootstrap-typeahead'; 
import { groupBy } from 'lodash';
//import ProgressBar from 'react-bootstrap/ProgressBar';
import 'react-bootstrap-typeahead/css/Typeahead.css';
import newId from './newId';


function getSelectedOption(options, id){
    if(!id)
        return undefined;

    return options.find(p=>p.id === id);
}

function filterBy(option, state) {
    if (state.selected.length) {
        return true;
    }
    return option.label.toLowerCase().indexOf(state.text.toLowerCase()) > -1;
}

class PropertySelector extends Component {
    constructor(props) {
        super(props);

        this.state = {
            properties: [],
            classUri: props.classUri,
            dataLoaded: false
        };

        this.handleChange = this.handleChange.bind(this);
        this.id = newId();
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.classUri !== nextProps.classUri) {
            //Class changed, properties need to be reset.
            return {
                properties: [],
                classUri: nextProps.classUri,
                dataLoaded: false
            };
        } else {
            return null;
        }
    }


    handleChange(event) {
        
        const newPropUri = event && event.length ? event[0].id : null;

        this.props.selectionChanged(newPropUri);
    }

    loadData() {
        this.setState({ dataLoaded: true });
        return this.props.api.getProperties(this.state.classUri)
            .then(properties => {
                this.setState({
                    properties: this.state.properties.concat(properties)
                });
            });
    }

    componentDidMount() {
        this.loadData();
    }

    componentDidUpdate() {
        if (!this.state.dataLoaded) {
            //Data was not loaded or was reset.    
            this.loadData();
        }
    }

    render() {
        const options = this.state.properties.map((p) => ({
            id: p.uri,
            label: p.localName,
            domainSize: p.domainSize,
            relevance: p.relevance.toFixed(2)
        }));
        const selected = getSelectedOption(options, this.props.value);

        return (
            <Typeahead
                align='left'
                inputProps={{
                    className: 'combobox',
                }}
                filterBy={filterBy}
                onChange={this.handleChange}
                maxResults={1000}
                id={this.id}
                placeholder='Select a property'
                selected={selected ? [selected] : []}
                options={options}
                renderMenu = {(results, menuProps, selected) => {
                    let index = 0;
                    const groups = groupBy(results, 'relevance');
                    const items = Object.keys(groups).sort((a,b) => b-a).map((group) => (
                        <Fragment key={group}>
                            {index !== 0 && <Menu.Divider />}
                            <Menu.Header>{'Relevance: ' + group.toLocaleString()}</Menu.Header>
                            {groups[group].map((i) => {
                                const item =
                                    <MenuItem key={index} option={i} position={index}>
                                        <Highlighter search={selected.text}>
                                            {i.label}
                                        </Highlighter>
                                    </MenuItem>;

                                index += 1;
                                return item;
                            })}
                        </Fragment>
                    ));

                    return <Menu {...menuProps}>{items}</Menu>;
                }
                }
                
            />
        );
    }
}

class OperatorSelector extends Component {
    constructor(props) {
        super(props);

        this.state = {
            operators: [
                {
                    displayValue: 'Select an operator',
                    realValue: 'null'
                }
            ],
            propertyUri: props.propertyUri,
            dataLoaded: false
        };

        this.handleChange = this.handleChange.bind(this);
        this.id = newId();
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.propertyUri !== nextProps.propertyUri) {
            //Property changed, properties need to be reset.
            return {
                operators: [
                    {
                        displayValue: 'Select an operator',
                        realValue: 'null'
                    }
                ],
                propertyUri: nextProps.propertyUri,
                dataLoaded: false
            };
        } else {
            return null;
        }
    }


    handleChange(event) {
        const newOperator = event.target.value !== 'null' ? event.target.value : null;

        this.props.selectionChanged(newOperator);
    }

    loadData() {
        this.setState({ dataLoaded: true });
        return this.props.api.getOperators(this.state.propertyUri)
            .then(response => {
                this.setState({
                    operators: this.state.operators.concat(response.operators),
                    inputType: response.inputType
                });

                this.props.inputTypeRetrieved(response.inputType);
            });
    }

    componentDidMount() {
        this.loadData();
    }

    componentDidUpdate() {
        if (!this.state.dataLoaded) {
            //Data was not loaded or was reset.    
            this.loadData();
        }
    }

    render() {
        const value = this.props.value ? this.props.value : 'null';

        return (
            <select className='form-control condition-input' value={value} onChange={this.handleChange}>
                {this.state.operators.map((o) => {
                    return <option key={o.realValue} value={o.realValue}>{o.displayValue}</option>;
                })}
            </select>
        );
    }
}

class ConditionClassSelector extends Component {
    constructor(props) {
        super(props);

        this.state = {
            classes: [],
            propertyUri: props.propertyUri,
            classUri: props.classUri,
            dataLoaded: false
        };

        this.handleChange = this.handleChange.bind(this);
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.propertyUri !== nextProps.propertyUri) {
            //Property changed, properties need to be reset.
            return {
                classes: [],
                propertyUri: nextProps.propertyUri,
                classUri: nextProps.classUri,
                dataLoaded: false,
            };
        } else {
            return null;
        }
    }


    handleChange(event) {
        const newClass = event && event.length ? event[0].id : null;

        this.props.selectionChanged(newClass);
    }

    loadData() {
        this.setState({ dataLoaded: true });
        return this.props.api.getClasses(this.state.classUri, this.state.propertyUri)
            .then(response => {
                this.setState({
                    classes: this.state.classes.concat(response)
                });
            });
    }

    componentDidMount() {
        this.loadData();
    }

    componentDidUpdate() {
        if (!this.state.dataLoaded) {
            //Data was not loaded or was reset.    
            this.loadData();
        }
    }

    render() {
        const options = this.state.classes.map((p) => ({
            id: p.uri,
            label: p.localName,
        }));
        const selected = getSelectedOption(options, this.props.value);

        return (
            <Typeahead
                align='left'
                inputProps={{
                    className: 'combobox',
                }}
                filterBy={filterBy}
                onChange={this.handleChange}
                id={this.id}
                placeholder='Select a class'
                selected={selected ? [selected] : []}
                options={options}
            />
        );
    }
}

class IndividualSelector extends Component {
    constructor(props) {
        super(props);

        this.state = {
            individuals: [],
            classUri: props.classUri,
            dataLoaded: false
        };

        this.handleChange = this.handleChange.bind(this);
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.classUri !== nextProps.classUri) {
            //Property changed, properties need to be reset.
            return {
                individuals: [],
                classUri: nextProps.classUri,
                dataLoaded: false
            };
        } else {
            return null;
        }
    }


    handleChange(event) {
        const newIndividual = event && event.length ? event[0].id : null;

        this.props.selectionChanged(newIndividual);
    }

    loadData() {
        this.setState({ dataLoaded: true });
        return this.props.api.getIndividuals(this.state.classUri)
            .then(response => {
                this.setState({
                    individuals: this.state.individuals.concat(response)
                });
            });
    }

    componentDidMount() {
        this.loadData();
    }

    componentDidUpdate() {
        if (!this.state.dataLoaded) {
            //Data was not loaded or was reset.    
            this.loadData();
        }
    }

    render() {
        const options = this.state.individuals.map((p) => ({
            id: p.uri,
            label: p.localName,
        }));
        const selected = getSelectedOption(options, this.props.value);

        return (
            <Typeahead
                align='left'
                inputProps={{
                    className: 'combobox'
                }}
                filterBy={filterBy}
                onChange={this.handleChange}
                id={this.id}
                placeholder='Select an individual'
                selected={selected ? [selected] : []}
                options={options}
            />
        );
    }
}

export {PropertySelector, ConditionClassSelector, IndividualSelector, OperatorSelector};