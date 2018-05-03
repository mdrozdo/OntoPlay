import React, { Component } from 'react'
import { Button } from 'react-bootstrap';

import 'bootstrap/dist/css/bootstrap.css'
import './main.css'

class Api {
  getIndividuals(className) {
    return fetch('/api/individuals/class/' + encodeURIComponent(className))
      .then(response => response.json());
  }

  getProperties(className) {
    return fetch('/api/properties/class/' + encodeURIComponent(className))
      .then(response => response.json());
  }

  getOperators(propertyUri, isDescriptionOfIndividual) {
    return fetch('/api/properties/operators/' + encodeURIComponent(propertyUri) + '/' + isDescriptionOfIndividual)
      .then(response => response.json());
  }

  getIndividualDataForUpdate(individualName) {
    return fetch('/api/individuals/data/' + individualName)
      .then(response => response.json());
  }

  getClasses(propertyName) {
    return fetch('/api/class/property/' + encodeURIComponent(propertyName))
      .then(response => response.json());
  }

  getAllClasses() {
    return fetch('/api/class')
      .then(response => response.json());
  }

  add(data, elementName, isAddIndividual) {
    var url = '';
    if (isAddIndividual)
      url = '/individuals/save';
    else
      url = "/class/save";
    var dataToSend = { 'conditionJson': data, 'name': elementName };

    return fetch(url, {
      method: 'POST',
      body: JSON.stringify(dataToSend),
      headers: new Headers({ 'Content-Type': 'application/json' })
    })
      .then(response => response.json());
  }

  getAnnotationProperties(componentUri) {
    return fetch('/api/annotationProperties/get/' + encodeURIComponent(componentUri))
      .then(response => response.json());
  }
}

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

class PropertySelector extends Component {
  constructor(props) {
    super(props);

    this.state = {};

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
    const api = new Api();
    this.setState({ dataLoaded: true });
    return api.getProperties(this.state.classUri)
      .then(properties => {
        this.setState({
          properties: this.state.properties.concat(properties)
        })
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
    return (
      <select className='property-select form-control' value={this.props.value} onChange={this.handleChange}>
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

    this.state = {};

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
    const api = new Api();
    this.setState({ dataLoaded: true });
    return api.getOperators(this.state.propertyUri, this.props.isIndividual)
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
    return (
      <select className='form-control' value={this.props.value} onChange={this.handleChange}>
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

    this.state = {};

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
    const api = new Api();
    this.setState({ dataLoaded: true });
    return api.getClasses(this.state.propertyUri)
      .then(response => {
        this.setState({
          classes: this.state.classes.concat(response)
        })
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
    return (
      <select className='form-control' value={this.props.value} onChange={this.handleChange}>
        {this.state.classes.map((c) => {
          return <option key={c.uri} value={c.uri}>{c.localName}</option>;
        })}
      </select>
    );
  }
}

class DatatypeInput extends Component {

  render() {
    return <input type={this.props.inputType} value={this.props.value} onChange={ev => this.props.valueChanged(ev.target.value)} />
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
  }

  propertySelected(propUri) {
    const newCondition = { ...this.props.condition, propertyUri: propUri };

    this.props.conditionChanged(0, newCondition);
  }

  operatorSelected(operator) {
    const newCondition = { ...this.props.condition, operator: operator };

    this.props.conditionChanged(0, newCondition);
  }

  classSelected(classUri) {
    //TODO: Rethink if this isn't too harsh - maybe it's possible to leave the constraints intact when changing class
    const newCondition = {
      ...this.props.condition, classConstraintValue: {
        classUri: classUri,
        propertyConditions: [
          {}
        ]
      }
    };

    this.props.conditionChanged(0, newCondition);
  }

  isClassRestrictionOperator(operator) {
    return operator === 'constrainedBy' || operator === 'isDescribedWith';
  }

  valueChanged(value) {
    const newCondition = { ...this.props.condition, datatypeValue: value };

    this.props.conditionChanged(0, newCondition);
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

    this.props.conditionChanged(0, newCondition);
  }

  render() {
    const selectedClassUri = this.props.condition.classConstraintValue ? this.props.condition.classConstraintValue.classUri : null;
    const operator = this.props.condition.operator;

    const propertyUri = this.props.condition.propertyUri;
    const inputType = this.state.inputType;


    return (
      <div className='condition-panel'>
        <div className='remove-condition'>
          <a>
            <span className='glyphicon glyphicon-remove'></span>
          </a>
        </div>
        <PropertySelector classUri={this.props.classUri} value={propertyUri} selectionChanged={p => this.propertySelected(p)} />
        {propertyUri &&
          <OperatorSelector isIndividual={this.props.isIndividual} value={operator} propertyUri={propertyUri} selectionChanged={o => this.operatorSelected(o)} inputTypeRetrieved={i => this.inputTypeRetrieved(i)} />
        }
        {operator && this.isClassRestrictionOperator(operator) &&
          <ConditionClassSelector value={selectedClassUri} propertyUri={propertyUri} selectionChanged={c => this.classSelected(c)} />
        }
        {operator && !this.isClassRestrictionOperator(operator) &&
          <DatatypeInput inputType={inputType} value={this.props.condition.datatypeValue} valueChanged={v => this.valueChanged(v)} />
        }
        {selectedClassUri &&
          <ConstraintsBox conditions={this.props.condition.classConstraintValue.propertyConditions} isIndividual={this.props.isIndividual} classUri={this.props.condition.classConstraintValue.classUri} conditionsChanged={this.nestedConditionsChanged} />
        }
      </div>
    );
  }
}

class ConstraintsBox extends Component {
  constructor(props) {
    super(props);

    this.conditionChanged = this.conditionChanged.bind(this);
  }

  // TODO: This is completely untested for >1 conditions
  conditionChanged(index, condition) {
    const newConditions = this.props.conditions.map((e, i) => i == index ? condition : e);
    this.setState({
      condition: newConditions
    });

    this.props.conditionsChanged(newConditions);
  }

  render() {
    return (
      <div>
        <ConditionBox classUri={this.props.classUri} condition={this.props.conditions[0]} conditionChanged={this.conditionChanged} isIndividual={this.props.isIndividual} />
        <div className='condition-operator'>
          <a href='#'><span className='glyphicon glyphicon-plus'></span></a>
        </div>
        <div className='condition-operator'>
          <a >Describe</a>
        </div>
      </div>
    );
  }
}

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
      "SelectClass": SelectClass,
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

export default OntoReact
export { SelectClass }