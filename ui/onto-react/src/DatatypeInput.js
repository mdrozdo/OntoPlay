import React from 'react';

function DatatypeInput(props) {
    return <input className='form-control condition-input' type={props.inputType} value={props.value} onChange={ev => props.valueChanged(ev.target.value)} />;
}

export default DatatypeInput;