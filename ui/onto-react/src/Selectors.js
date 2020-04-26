import React, { Component } from 'react';

class PropertySelector extends Component {
    constructor(props) {
        super(props);

        this.state = {
            properties: [
                {
                    uri: 'null',
                    localName: 'Select a property'
                }
            ],
            classUri: props.classUri,
            dataLoaded: false
        };

        this.handleChange = this.handleChange.bind(this);
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.classUri !== nextProps.classUri) {
            //Class changed, properties need to be reset.
            return {
                properties: [
                    {
                        uri: 'null',
                        localName: 'Select a property'
                    }
                ],
                classUri: nextProps.classUri,
                dataLoaded: false
            };
        } else {
            return null;
        }
    }


    handleChange(event) {
        const newPropUri = event.target.value !== 'null' ? event.target.value : null;

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
        const value = this.props.value ? this.props.value : 'null';

        return (
            <select className='form-control condition-input' value={value} onChange={this.handleChange}>
                {this.state.properties.map((p) => {
                    return <option key={p.uri} value={p.uri}>{p.localName}</option>;
                })}
            </select>
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
            classes: [
                {
                    localName: 'Select a class',
                    uri: 'null'
                }
            ],
            propertyUri: props.propertyUri,
            dataLoaded: false
        };

        this.handleChange = this.handleChange.bind(this);
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.propertyUri !== nextProps.propertyUri) {
            //Property changed, properties need to be reset.
            return {
                classes: [
                    {
                        localName: 'Select a class',
                        uri: 'null'
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
        const newClass = event.target.value !== 'null' ? event.target.value : null;

        this.props.selectionChanged(newClass);
    }

    loadData() {
        this.setState({ dataLoaded: true });
        return this.props.api.getClasses(this.state.propertyUri)
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
        const value = this.props.value ? this.props.value : 'null';
        return (
            <select className='form-control condition-input' value={value} onChange={this.handleChange}>
                {this.state.classes.map((c) => {
                    return <option key={c.uri} value={c.uri}>{c.localName}</option>;
                })}
            </select>
        );
    }
}

class IndividualSelector extends Component {
    constructor(props) {
        super(props);

        this.state = {
            individuals: [
                {
                    localName: 'Select an individual',
                    uri: 'null'
                }
            ],
            classUri: props.classUri,
            dataLoaded: false
        };

        this.handleChange = this.handleChange.bind(this);
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.classUri !== nextProps.classUri) {
            //Property changed, properties need to be reset.
            return {
                individuals: [
                    {
                        localName: 'Select an individual',
                        uri: 'null'
                    }
                ],
                classUri: nextProps.classUri,
                dataLoaded: false
            };
        } else {
            return null;
        }
    }


    handleChange(event) {
        const newIndividual = event.target.value !== 'null' ? event.target.value : null;

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
        const value = this.props.value ? this.props.value : 'null';
        return (
            <select className='form-control condition-input' value={value} onChange={this.handleChange}>
                {this.state.individuals.map((c) => {
                    return <option key={c.uri} value={c.uri}>{c.localName}</option>;
                })}
            </select>
        );
    }
}

export {PropertySelector, ConditionClassSelector, IndividualSelector, OperatorSelector};