import React, { Component } from 'react'
import { render } from 'react-dom'

import Api from "./Api";

class SelectClass extends Component {
    constructor(props) {
        super(props);

        this.state = {
            classes: [],
            selectedClassUri: props.mainClassUri
        };
    }

    componentDidMount() {
        const api = new Api();
        return api.getAllClasses()
            .then(classes => {
                this.setState({
                    classes: classes
                })
            });
    }

    handleChange(event) {
        const newClassUri = event.target.value;
        this.setState({
            selectedClassUri: newClassUri
        });

        this.props.mainClassChanged(newClassUri);
    }

    render() {
        return (
            <div>
                <label htmlFor='input_superClass'>Map class {this.props.baseClassName} to: </label>
                <select id='input_superClass' value={this.state.selectedClassUri} onChange={(e) => this.handleChange(e)} className='form-control' required='required'>
                    <option key='null' value='null'>Select a class</option>
                    {this.state.classes.map((c) => {
                        return <option key={c.uri} value={c.uri}>{c.localName != '' ? c.localName : c.uri}</option>
                    })}
                </select>
            </div>
        );
    }
}

export default SelectClass;