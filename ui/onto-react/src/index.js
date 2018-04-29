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
    return fetch('/api/properties/operators/' + propertyUri + '/' + isDescriptionOfIndividual)
      .then(response => response.json());    
  }

  getIndividualDataForUpdate(individualName) {
    return fetch('/api/individuals/data/' + individualName)
      .then(response => response.json());
  }

  getClasses(propertyName) {
    return fetch('/api/class/property/' + propertyName)
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
        headers: new Headers({'Content-Type': 'application/json'})
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
      selectedClassUri: null
    };
  }

  componentDidMount(){
    const api = new Api();
    return api.getAllClasses()
      .then(classes => {
        this.setState({
          classes: classes
        })
      });
  }

  handleChange(event){
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
        <select id='input_superClass' onChange={(e) => this.handleChange(e)} className='form-control' required='required'>
          <option key='null' value='null'>Select a class</option>
          {this.state.classes.map((c) => {
            return <option key={c.uri} value={c.uri}>{c.localName != '' ? c.localName : c.uri }</option>
          })}
        </select>
      </div>
    );
  }
}

class PropertySelector extends Component {
  constructor(props) {
    super(props);

    this.state = {
      properties: [
        {
          uri: null,
          name: 'Select a property'
        }
      ],
      selectedPropertyUri: null
    };
  }

  loadData(){
    const api = new Api();
    return api.getProperties(this.props.classUri)
      .then(properties => {
        this.setState({
          properties: properties
        })
      });
  }

  componentDidMount() {
    this.loadData();
  }

  componentDidUpdate(){
    this.loadData();
  }

  render() {
    return (
      <select className='form-control' className='property-select form-control'>
        {this.state.properties.map((p) => {
          return <option key={p.uri} value={p.uri}>{p.localName}</option>;
        })}
      </select>
    );
  }
}

class ConditionBox extends Component {
  constructor(props) {
    super(props);

    this.state = {
      condition: props.condition,
      selectedPropertyUri: null
    }    
  }

  propertySelected(p) {
    this.setState({
      selectedPropertyUri: p.uri
    });
  }

  render() {
    return (
      <div className='condition-panel'>
        <div className='remove-condition'>
          <a>
            <span className='glyphicon glyphicon-remove'></span>
          </a>
        </div>
        <PropertySelector classUri={this.props.classUri} selectionChanged={(p) => this.propertySelected(p)}/>
        {this.state.selectedPropertyUri && <div>DUPADUPA</div>}
      </div>
    );
  }
}

class OntoReact extends Component {
  constructor(props) {
    super(props);

    this.state = {
      // results: [],
      condition: props.condition,
      mainClassUri: props.mainClass
    };
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

  componentDidMount() {
    // fetch("/api/properties/class/PhysicalMemory")
    //   .then(response => response.json())
    //   .then(data => {
    //     this.setState({ results: data })
    //   });
  }

  mainClassChanged(classUri){
    this.setState({
      mainClassUri: classUri
    })
  }

  render() {
    const results = this.state.results;
    const conditionJson = JSON.stringify(this.state.condition, null, 2);
    const headerComponent = React.createElement(
      this.props.headerComponent ?
        this.props.headerComponent :
        this.createHeader(this.props.headerComponentName),
        {
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
          <ConditionBox classUri={this.state.mainClassUri}/>
          <div className='condition-operator'>
            <a href='#'><span className='glyphicon glyphicon-plus'></span></a>
          </div>
          <div className='condition-operator'>
            <a >Describe</a>
          </div>
          <Button className='btn btn-success'>Save</Button>
          <pre className='code'>{conditionJson}</pre>
        </form>
      </div>
    );
  }
}

export default OntoReact
export { SelectClass }