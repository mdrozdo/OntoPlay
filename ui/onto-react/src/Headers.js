import React, { Component } from 'react';

import Api from './Api';

const MultiHeader = (Component1, Component2) => (props) => {
    return (
        <div>
            <Component1 {...props} />
            <Component2 {...props} />
        </div>
    );
};

const InputNameHeader = (label) => (props) => {
    return (
        <div>
            <label htmlFor='elementName'>{label}</label>
            <input id='elementName' className='form-control' name='elementName' type='text' required='required' value={props.elementName} onChange={ev => props.elementNameChanged(ev.target.value)} />
        </div>
    );
};

const SelectClassHeader = (label) => class extends Component {
    constructor(props) {
        super(props);

        this.state = {
            classes: []
        };
    }

    componentDidMount() {
        const api = new Api();
        return api.getAllClasses()
            .then(classes => {
                this.setState({
                    classes: classes
                });
            });
    }

    handleChange(event) {
        const newClassUri = event.target.value;
        
        this.props.mainClassChanged(newClassUri);
    }

    render() {
        return (
            <div>
                <label htmlFor='input_superClass'>{label}</label>
                <select id='input_superClass' value={this.props.mainClassUri} onChange={(e) => this.handleChange(e)} className='form-control' required='required'>
                    <option key='null' value='null'>Select a class</option>
                    {this.state.classes.map((c) => {
                        return <option key={c.uri} value={c.uri}>{c.localName != '' ? c.localName : c.uri}</option>;
                    })}
                </select>
            </div>
        );
    }
};

export { SelectClassHeader, InputNameHeader, MultiHeader };