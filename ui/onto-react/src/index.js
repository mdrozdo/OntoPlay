import React, { Component } from 'react';
import { Button } from 'react-bootstrap';
import ConstraintsBox from './ConstraintsBox';

import 'bootstrap/dist/css/bootstrap.css';
import './main.css';

//TODO: condition, properties etc should be defined as classes, ideally with Flow annotations.
//TODO: would be good to add some unit tests.
//TODO: make changes required to use "plugin:react/recommended"] 

class OntoReact extends Component {
    constructor(props) {
        super(props);

        this.state = {
            condition: props.condition
                ? props.condition
                : {
                    classUri: props.mainClass,
                    propertyConditions: [{}],
                },
            elementName: props.elementName,
        };

        this.conditionsChanged = this.conditionsChanged.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    mainClassChanged(classUri) {
        const newCondition = {
            classUri: classUri,
            propertyConditions: [{}],
        };

        this.setState({
            condition: newCondition,
        });
    }

    elementNameChanged(elementName) {
        this.setState({
            elementName: elementName,
        });
    }

    conditionsChanged(conditions) {      
        const newCondition = {
            ...this.state.condition,
            propertyConditions: conditions,
        };
        this.setState({
            condition: newCondition,
        });
    }

    handleSubmit() {
        return this.props.api
            .add(JSON.stringify(this.state.condition), this.state.elementName)
            .then(response => {
                alert(response);
            });
    }

    render() {
        const stateJson = JSON.stringify(this.state, null, 2);
        const headerComponent = React.createElement(
            this.props.headerComponent
                ? this.props.headerComponent
                : this.createHeader(this.props.headerComponentName),
            {
                mainClassUri: this.state.condition.classUri,
                mainClassChanged: c => this.mainClassChanged(c),
                elementName: this.state.elementName,
                elementNameChanged: n => this.elementNameChanged(n),
            }
        );
        const title = this.props.title;

        return (
            <div>
                <div className='row'>
                    <div className='col-sm-18'>
                        <h3>{title}</h3>
                    </div>
                </div>
                <div className='form-group'>{headerComponent}</div>
                <form className='form-inline'>
                    <ConstraintsBox
                        propertyConditions={
                            this.state.condition.propertyConditions
                        }
                        api={this.props.api}
                        classUri={this.state.condition.classUri}
                        conditionsChanged={this.conditionsChanged}
                    />
                    <Button
                        className='btn btn-success'
                        onClick={this.handleSubmit}
                    >
                        Save
                    </Button>
                    <pre className='code'>{stateJson}</pre>
                </form>
            </div>
        );
    }
}

// export default ;
import { SelectClassHeader, InputNameHeader, MultiHeader } from './Headers';
import Api from './Api';

export default {
    OntoReact,
    SelectClassHeader,
    InputNameHeader,
    MultiHeader,
    Api,
};
