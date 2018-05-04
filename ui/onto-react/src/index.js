import React, { Component } from 'react'
import { Button } from 'react-bootstrap';
import SelectClass from './SelectClass';
import ConstraintsBox from './ConstraintsBox';

import 'bootstrap/dist/css/bootstrap.css';
import './main.css';

class OntoReact extends Component {
  constructor(props) {
    super(props);

    this.state = {
      condition: props.condition ? props.condition : {
        classUri: props.mainClass,
        propertyConditions: [{}]
      },
      mainClassUri: props.mainClass
    };

    this.conditionsChanged = this.conditionsChanged.bind(this);
  }

  createHeader(headerName) {
    // This really sucks. Doesn't seem like I can create a component from string.
    // Master branch of nwb has a solution for this - exporting multiple components
    // to UMD (UMD.entry in nwb.config.js), but it's not released yet
    var allHeaders = {
      'SelectClass': SelectClass,
    }
    return allHeaders[headerName];
  }

  mainClassChanged(classUri) {
    const newCondition = {
      classUri: classUri,
      propertyConditions: [
        {}
      ]
    }

    this.setState({
      mainClassUri: classUri,
      condition: newCondition
    })
  }


  conditionsChanged(conditions) {
    const newCondition = { ...this.state.condition, propertyConditions: conditions };
    this.setState({
      condition: newCondition
    });
  }

  render() {
    const conditionJson = JSON.stringify(this.state.condition, null, 2);
    const headerComponent = React.createElement(
      this.props.headerComponent ?
        this.props.headerComponent :
        this.createHeader(this.props.headerComponentName),
      {
        mainClassUri: this.props.mainClass,
        mainClassChanged: (c) => this.mainClassChanged(c)
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
        <div className='form-group'>
          {headerComponent}
        </div>
        <form className='form-inline'>
          <ConstraintsBox conditions={this.state.condition.propertyConditions} isIndividual={this.props.isIndividual} classUri={this.state.condition.classUri} conditionsChanged={this.conditionsChanged} />
          <Button className='btn btn-success'>Save</Button>
          <pre className='code'>{conditionJson}</pre>
        </form>
      </div>
    );
  }
}

export default OntoReact;
export { SelectClass };