import React, { Component } from 'react'
import { render } from 'react-dom'

class DatatypeInput extends Component {
    
    render() {
        return <input className='form-control condition-input' type={this.props.inputType} value={this.props.value} onChange={ev => this.props.valueChanged(ev.target.value)} />
    }
}

export default DatatypeInput